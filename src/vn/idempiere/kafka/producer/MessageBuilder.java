package vn.idempiere.kafka.producer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import vn.idempiere.kafka.model.MKafkaRegistryColumn;
import vn.idempiere.kafka.model.MKafkaRegistryService;
import vn.idempiere.kafka.model.MKafkaRegistryTable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.compiere.model.MColumn;
import org.compiere.model.MTable;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;

public class MessageBuilder {

	private static final int MAX_RECURSION_DEPTH = 10;
	private CLogger log = CLogger.getCLogger(MessageBuilder.class);

	public List<String> messageProcessor(MKafkaRegistryTable headerTable, List<MKafkaRegistryTable> childTableList,
			int rootRecordId, String eventTopic) {
		
		List<String> message = new ArrayList<String>();
		
		if (childTableList.isEmpty()) {
			 message = buildMessage(null,headerTable,rootRecordId,eventTopic);
			 return message;
		}
		
		for (MKafkaRegistryTable registryTable : childTableList) {
			if (registryTable.get_ValueAsInt("parent_table_id") == headerTable.get_ValueAsInt("ad_table_id")){
				List<String> ms = new ArrayList<String>();
				ms = buildMessage(registryTable, headerTable, rootRecordId, eventTopic);
				message.addAll(ms);
			}
		}
		return message;
	}

	@SuppressWarnings("unused")
	public List<String> buildMessage(MKafkaRegistryTable registryTable,
			MKafkaRegistryTable headerTable, int rootRecordId, String eventTopic) {
				
		List<MKafkaRegistryColumn> columnList = null;
		MTable currentTable = null;
		MTable parentTable = null;
		int currentRootRecord = rootRecordId;
		int chunkSize = 0;
		int totalRecord;
		
		if (registryTable == null) {
			 parentTable = new MTable(Env.getCtx(), headerTable.get_ValueAsInt("ad_table_id"), null);
			 totalRecord = 0;

		}else {
			currentTable = new MTable(Env.getCtx(), registryTable.get_ValueAsInt("ad_table_id"), null);
			parentTable = new MTable(Env.getCtx(), registryTable.get_ValueAsInt("parent_table_id"), null);
			columnList = getColumnByRegistryTableId(
					registryTable.get_ValueAsInt("kafka_registrytable_id"));
			chunkSize = registryTable.get_ValueAsInt("chunkSize");
			totalRecord = getTotalRecord(currentRootRecord, currentTable.getTableName(),parentTable.getTableName());
		}
		 
		int totalChunk = 0;
		if (totalRecord == 0) {
			totalChunk =  1;
		}else {
			totalChunk = (totalRecord + chunkSize - 1) / chunkSize;
		}
		
		MKafkaRegistryService registryService = new MKafkaRegistryService(Env.getCtx(),
				headerTable.get_ValueAsInt("kafka_registryservice_id"), null);
		
		List<String> messageList = new ArrayList<String>();
		
		for (int i = 0; i < totalChunk; i++) {
		    int offset = i * chunkSize;
		    int limit = chunkSize;
		    
		    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		    Map<String, Object> headerMap = new LinkedHashMap<>();
		    Map<String, Object> childMap = new LinkedHashMap<>();

	        // Envelope metadata 
		    headerMap.put("source_system", "iDempiere");
		    headerMap.put("event_type", eventTopic);
		    headerMap.put("topic_name", registryService.get_ValueAsString("topicname"));
		    headerMap.put("registry_service_name", registryService.get_ValueAsString("name"));   
		    //headertable
		    headerMap.put("table_name", parentTable.getTableName());
		    if (headerTable.get_ValueAsBoolean("isenableschema") == true) {
			    headerMap.put("schema", buildSchema(headerTable));
		    }
		    headerMap.put("payload", getHeaderRecord(headerTable, rootRecordId));
		    // Data
		    if (registryTable == null) {
		    	
		    }else {
		    	childMap.put("table_name", currentTable.getTableName());
			    childMap.put("chunk_size", chunkSize);
			    childMap.put("total_chunks", totalChunk);
			    childMap.put("chunk_index", i + 1);
			    childMap.put("total_records", totalRecord);
			    if (registryTable.get_ValueAsBoolean("isenableschema") == true) {
			    	childMap.put("schema", buildSchema(registryTable) );
			    }
			    childMap.put("payload", getChunkRecord(columnList, currentTable, parentTable, rootRecordId, limit, offset,true));
		    }
		          
		    map.put("header_info", headerMap);
		    map.put("child_info",childMap);
		    
		    Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	        String message = gson.toJson(map);		
	        messageList.add(message);
		}
		
		return messageList;
	}

	private List<Map<String, Object>> getChunkRecord(List<MKafkaRegistryColumn> columnList, 
			MTable currentTable, MTable parentTable, int rootRecordId, int limit, int offset, Boolean isLoadChild) {
		List<Map<String, Object>> recordList = new ArrayList<>();;
		String currentTableName = currentTable.getTableName();
		String parentTableName = parentTable.getTableName();
    	int registryTableId = 0;

		StringBuilder sql = new StringBuilder();
	    sql.append("SELECT ").append("*")
	       .append(" FROM ").append(currentTableName)
	       .append(" WHERE ").append(parentTableName).append("_ID")
	       .append(" = ? ORDER BY ").append(currentTableName)
	       .append(" LIMIT ? OFFSET ?");
	    
	    try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null)) {
	        pstmt.setInt(1, rootRecordId);
	        pstmt.setInt(2, limit);
	        pstmt.setInt(3, offset);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> record = new LinkedHashMap<>();
	                
	                for (MKafkaRegistryColumn col : columnList) {
	                	MColumn column = new MColumn(Env.getCtx(), col.get_ValueAsInt("ad_column_id"), null);
	                	String dbColumnName = column.get_ValueAsString("columnname");
	                	if (col.getresponsename() != null) {
	                		String columnName = col.get_ValueAsString("responsename");
	                		Object value = null;
	                		// Handle Timestamp fields
	                		if (isTimeStampField(column)) {
	                			value = rs.getObject(dbColumnName);
	                			if (value != null)
	                				value = ((java.sql.Timestamp) value).getTime();
	                		}else {
	                			value = rs.getObject(dbColumnName);
	                		}
	                		record.put(columnName.toLowerCase(), value);
	                	}else {
	                		String columnName = dbColumnName;
		                    Object value = null;
		                    // Handle Timestamp fields
	                		if (isTimeStampField(column)) {
	                			value = rs.getObject(dbColumnName);
	                			if (value != null)
	                				value = ((java.sql.Timestamp) value).getTime();
	                		}else {
	                			value = rs.getObject(dbColumnName);
	                		}
		                    record.put(columnName.toLowerCase(), value);
	                	}  
	                	registryTableId = col.get_ValueAsInt("kafka_registrytable_id");
	                }
	                
	               if (isLoadChild) {
	            	   MKafkaRegistryTable registryTable = new MKafkaRegistryTable (Env.getCtx(),registryTableId,null);
	            	   if (isContainChild(registryTable)) {
	            		   String pkColumn = currentTableName + "_ID";
	                       int currentRecordId = rs.getInt(pkColumn);
	                       Map<String, Object> childRecords = getChildRecord(registryTable, currentRecordId, 0);
	                       record.putAll(childRecords);
	            	   }
	               }
	                
	                recordList.add(record);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	    
		return recordList;
	}
	
	
	private Boolean isContainChild(MKafkaRegistryTable registryTable) {
		
		StringBuilder sql = new StringBuilder();
	    sql.append("SELECT ").append("COUNT(*)")
	       .append(" FROM ").append("kafka_registrytable")
	       .append(" WHERE ").append("parent_table_id").append(" = ?")
	       .append(" AND ").append("kafka_registryservice_id").append(" = ?");
	    int a = DB.getSQLValueEx(null, sql.toString(), registryTable.get_ValueAsInt("ad_table_id"), registryTable.get_ValueAsInt("kafka_registryservice_id"));
	    
	    if (a > 0) {
	    	return true;
	    }
	    
		return false;
	}

	private int getTotalRecord(int currentRootRecord, String  currentTable, String parenttTable) {
		
		// label for psmt
		String tableName = currentTable;
		String tableId = (parenttTable + "_id");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM ").append(tableName).append(" t WHERE t.").append(tableId).append(" = ?");
		return DB.getSQLValueEx(null, sql.toString(), currentRootRecord);
	}

	private List<MKafkaRegistryColumn> getColumnByRegistryTableId(int registryTableId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT kafka_registrycolumn_id ")
		   .append("FROM kafka_registrycolumn ")
		   .append("WHERE kafka_registrytable_id = ?");

		List<MKafkaRegistryColumn> columnList = new ArrayList<>();

		try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null)) {
			pstmt.setInt(1, registryTableId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int tableId = rs.getInt("kafka_registrycolumn_id");
					MKafkaRegistryColumn column = new MKafkaRegistryColumn(Env.getCtx(), tableId, null);
					columnList.add(column);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return columnList;
	}
	
	private List<MKafkaRegistryTable> getDirectChildren(MKafkaRegistryTable parentRegistryTable) {
	    List<MKafkaRegistryTable> childTables = new ArrayList<>();
	    
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT kafka_registrytable_id ")
	       .append("FROM kafka_registrytable ")
	       .append("WHERE parent_table_id = ? ")
	       .append("AND kafka_registryservice_id = ?");
	    
	    try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null)) {
	        pstmt.setInt(1, parentRegistryTable.get_ValueAsInt("ad_table_id"));
	        pstmt.setInt(2, parentRegistryTable.get_ValueAsInt("kafka_registryservice_id"));
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                int childTableId = rs.getInt("kafka_registrytable_id");
	                MKafkaRegistryTable childTable = new MKafkaRegistryTable(Env.getCtx(), childTableId, null);
	                childTables.add(childTable);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return childTables;
	}
	
	//RECURSIVE for childTable
	private Map<String, Object> getChildRecord(MKafkaRegistryTable registryTable, int parentRecordId, int depth) {
	    if (depth > MAX_RECURSION_DEPTH) {
	    	log.severe("Maximum recursion depth reached for table: " 
	            + registryTable.get_ValueAsString("Name"));
	        return Collections.emptyMap();
	    }

	    Map<String, Object> childDataMap = new LinkedHashMap<>();
	    
	    List<MKafkaRegistryTable> childTables = getDirectChildren(registryTable);
	    
	    for (MKafkaRegistryTable childTable : childTables) {
	        MTable currentTable = new MTable(Env.getCtx(), childTable.get_ValueAsInt("ad_table_id"), null);
	        MTable parentTable = new MTable(Env.getCtx(), childTable.get_ValueAsInt("parent_table_id"), null);
	        
	        String currentTableName = currentTable.getTableName();
	        String parentTableName = parentTable.getTableName();
	        
	        List<MKafkaRegistryColumn> columnList = getColumnByRegistryTableId(
	                childTable.get_ValueAsInt("kafka_registrytable_id"));
	        
	        StringBuilder sql = new StringBuilder();
	        sql.append("SELECT *")
	           .append(" FROM ").append(currentTableName)
	           .append(" WHERE ").append(parentTableName).append("_ID = ?")
	           .append(" ORDER BY ").append(currentTableName);
	        
	        try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null)) {
	            pstmt.setInt(1, parentRecordId);
	            
	            try (ResultSet rs = pstmt.executeQuery()) {
	                List<Map<String, Object>> childRecordList = new ArrayList<>();
	                
	                while (rs.next()) {
	                    Map<String, Object> childRecord = new LinkedHashMap<>();
	                    
	                    for (MKafkaRegistryColumn col : columnList) {
	                        MColumn column = new MColumn(Env.getCtx(), col.get_ValueAsInt("ad_column_id"), null);
	                        String dbColumnName = column.get_ValueAsString("columnname");
	                        
	                        String columnName = col.getresponsename() != null 
	                                ? col.get_ValueAsString("responsename") 
	                                : dbColumnName;
	                        
	                        Object value = rs.getObject(dbColumnName);
	                        childRecord.put(columnName.toLowerCase(), value);
	                    }
	                    // RECURSIVE 
	                    if (isContainChild(childTable)) {
	                        String pkColumn = currentTableName + "_ID";
	                        int currentRecordId = rs.getInt(pkColumn);
	                        
	                        Map<String, Object> grandChildren = getChildRecord(childTable, currentRecordId, depth + 1);
	                        
	                        if (!grandChildren.isEmpty()) {
	                            childRecord.putAll(grandChildren);
	                        }
	                    }
	                    
	                    childRecordList.add(childRecord);
	                }
	                
	                if (!childRecordList.isEmpty()) {
	                    if (childRecordList.size() == 1) {
	                        childDataMap.put(currentTableName.toLowerCase(), childRecordList.get(0));
	                    } else {
	                        childDataMap.put(currentTableName.toLowerCase() + "_list", childRecordList);
	                    }
	                }
	            }
	        } catch (Exception e) {
	        	log.severe("Error fetching child records for table: " 
	                + childTable.get_ValueAsString("Name") + " - " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	    
	    return childDataMap;
	}
	
	private Map<String,Object> getHeaderRecord(MKafkaRegistryTable headerTable, int rootRecordId) {
		Map<String, Object> headerRecord = new LinkedHashMap<>();
		
		List<MKafkaRegistryColumn> headerColumnList = getColumnByRegistryTableId(
				headerTable.get_ValueAsInt("kafka_registrytable_id"));
		
		MTable parentTable = new MTable(Env.getCtx(), headerTable.get_ValueAsInt("ad_table_id"), null);
		
		StringBuilder sql = new StringBuilder();
	    sql.append("SELECT ").append("*")
	       .append(" FROM ").append(parentTable.getTableName())
	       .append(" WHERE ").append(parentTable.getTableName()).append("_ID")
	       .append(" = ?");
	    
	    try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null)) {
	        pstmt.setInt(1, rootRecordId);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                for (MKafkaRegistryColumn col : headerColumnList) {
	                	MColumn column = new MColumn(Env.getCtx(), col.get_ValueAsInt("ad_column_id"), null);
	                	String dbColumnName = column.get_ValueAsString("columnname");
	                	if (col.getresponsename() != null) {
	                		String columnName = col.get_ValueAsString("responsename");
	                		Object value = null;
	                		if (isTimeStampField(column)) {
	                			value = rs.getObject(dbColumnName);
	                			if (value != null)
	                				value = ((java.sql.Timestamp) value).getTime();
	                		}else {
	                			value = rs.getObject(dbColumnName);
	                		}
	                		headerRecord.put(columnName.toLowerCase(), value);
	                	}else {
	                		String columnName = dbColumnName;
		                    Object value = null;
		                    // Handle Timestamp fields	
	                		if (isTimeStampField(column)) {
	                			value = rs.getObject(dbColumnName);
	                			if (value != null)
	                				value = ((java.sql.Timestamp) value).getTime();
	                		}else {
	                			value = rs.getObject(dbColumnName);
	                		}
		                    headerRecord.put(columnName.toLowerCase(), value);
	                	}  
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	    
		return headerRecord;
	}
	
	private Map<String, Object> buildSchema(MKafkaRegistryTable registryTable) {
	    Map<String, Object> schema = new LinkedHashMap<>();
	    List<Map<String, Object>> fields = new ArrayList<>();
	    
	    List<MKafkaRegistryColumn> columns = getColumnByRegistryTableId(registryTable.get_ID());
	    
	    for (MKafkaRegistryColumn regCol : columns) {
	        MColumn column = new MColumn(Env.getCtx(), regCol.getAD_Column_ID(), null);
	        
	        String fieldName;
	        if (regCol.getresponsename() != null) {
	            fieldName = regCol.get_ValueAsString("responsename");
	        } else {
	            fieldName = column.getColumnName();
	        }
	        
	        Map<String, Object> field = new LinkedHashMap<>();
	        
	        int refId = column.getAD_Reference_ID();
	        String sqlDataType = DisplayType.getSQLDataType(refId, column.getColumnName(), column.getFieldLength());
	        
	        if (isTimeStampField(column)) {
	            field.put("type", "Long(8)");
	            field.put("length", 8); 
	        } else {
	            field.put("type", sqlDataType);
	            field.put("length", column.getFieldLength());
	        }
	        
	        field.put("field", fieldName.toLowerCase());
	        
	        fields.add(field);
	    }
	    
	    schema.put("type", "struct");
	    schema.put("fields", fields);
	    
	    return schema;
	}
	
	private Boolean isTimeStampField( MColumn column) {
		int refId = column.getAD_Reference_ID();
		String sqlDataType = DisplayType.getSQLDataType(refId, column.getColumnName(), column.getFieldLength());
		
		if ("timestamp".equalsIgnoreCase(sqlDataType)) {
			return true;
		}
		return false;
	}
	

}

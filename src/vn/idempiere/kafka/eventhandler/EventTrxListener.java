package vn.idempiere.kafka.eventhandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.event.IEventTopics;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;
import org.compiere.util.TrxEventListener;
import org.compiere.util.CLogger;


import vn.idempiere.kafka.model.MAuditlog;
import vn.idempiere.kafka.model.MKafkaRegistryService;
import vn.idempiere.kafka.model.MKafkaRegistryTable;
import vn.idempiere.kafka.producer.KafkaProducer;
import vn.idempiere.kafka.producer.MessageBuilder;

public class EventTrxListener implements TrxEventListener {

	private final int currentTableId;
	private final String currentTableName;
	private final String eventTopic;
	private final int record;
	private final List<String> eventStack;
    private final KafkaEventHandler handler;
    private static final int MAX_RECURSION_DEPTH = 10;
    private CLogger log = CLogger.getCLogger(EventTrxListener.class);


	public EventTrxListener(PO po, String eventTopic, List<String> eventStack, KafkaEventHandler handler) {
		this.currentTableId = po.get_Table_ID();
		this.currentTableName = po.get_TableName();
		this.eventTopic = eventTopic;
		this.record = po.get_ID();
		this.eventStack = eventStack;
        this.handler = handler;

	}

	@Override
	public void afterRollback(Trx trx, boolean success) {
		// TODO Auto-generated method stub
		eventStack.clear();
	}

	@Override
	public void afterClose(Trx trx) {
		// TODO Auto-generated me	thod stub
		eventStack.clear();
	}

	@Override
	public void afterCommit(Trx trx, boolean success) {
		
		
		
		 // Rebind event manager if registry table is modified
		
		if ("Kafka_RegistryTable".equals(currentTableName)) {
            handler.reloadRegisteredTables();
        }

		// TODO Auto-generated method stub
		if (!success)
			return;
		if (eventStack.contains("DOC_AFTER_COMPLETE")) {
			if (!getEventTypeName(eventTopic).equals("DOC_AFTER_COMPLETE")) {
				return;
			}
		}
		
		String kafkaKey = String.valueOf(record);
		if (eventStack.contains("DOC_AFTER_COMPLETE")) {
	         kafkaKey = getDocumentNo(currentTableId, record);
	    }
		
		KafkaProducer kafkaProducer = null;
		

		// litst registryservice co dang ki table bi trigger
		List<MKafkaRegistryService> RegistrServiceList = listRegistryService(currentTableId, eventTopic);

		// duyet tat ca cac registryservice co event trigger
		for (MKafkaRegistryService rs : RegistrServiceList) {
			try {
				// Khởi tạo KafkaProducer
				kafkaProducer = new KafkaProducer(rs);
				int currentRegistryTableID = getRegistryTableIdByRegistryServiceID(
						rs.get_ValueAsInt("kafka_registryservice_id"), currentTableId);

				int headerRegistryTableID = getHeaderTableId(rs.get_ValueAsInt("kafka_registryservice_id"));
				int rootRecordId = getRootRecordId(currentRegistryTableID, record, 0);
				
				if (rootRecordId < 0) {
					throw new IllegalStateException("Could not determine root record ID. Check for configuration errors or infinite loops.");
				}

				MKafkaRegistryTable headerTable = new MKafkaRegistryTable(Env.getCtx(), headerRegistryTableID, null);
				List<MKafkaRegistryTable> childTableList = getChildRegistryTables(
						rs.get_ValueAsInt("kafka_registryservice_id"));
				
				//audit log
				MAuditlog auditlog = new MAuditlog(null, 0, null);
				auditlog.setKafka_RegistryService_ID(rs.get_ValueAsInt("kafka_registryservice_id"));
				auditlog.setTableName(currentTableName);
				auditlog.setRecordID(record);
			    auditlog.setMessageKey(kafkaKey);
				auditlog.setEventType(getEventTypeName(eventTopic));
				auditlog.setTopicName(rs.get_ValueAsString("topicname"));
				
				MessageBuilder messageBuilder = new MessageBuilder();

				// list message cua registryservice
				List<String> ms = messageBuilder.messageProcessor(headerTable, childTableList, rootRecordId,
						getEventTypeName(eventTopic));
				// send message
				for (String message : ms) {
				
					// send Kafka
					kafkaProducer.sendAsync(rs.get_ValueAsString("topicname"), kafkaKey, message, auditlog);
				}
			} catch (Exception e) {
				log.severe("Failed to send Kafka message for service: " + rs.getName());
				MAuditlog auditlog = new MAuditlog(Env.getCtx(), 0, null);
				auditlog.setKafka_RegistryService_ID(rs.get_ValueAsInt("kafka_registry_service_id"));
				auditlog.setTableName(currentTableName);
				auditlog.setRecordID(record);
				if (eventStack.contains("DOC_AFTER_COMPLETE")) {
			         auditlog.setMessageKey(kafkaKey);
			    }
				auditlog.setEventType(getEventTypeName(eventTopic));
				auditlog.setTopicName(rs.get_ValueAsString("topicname"));
				auditlog.setErrorMessage(e.getMessage());
				auditlog.saveEx();
				e.printStackTrace();
			} finally {
				if (kafkaProducer != null) {
					kafkaProducer.close();
				}
			}
		}

	}

	private int getRootRecordId(int currentRegistryTableId, int currentRecordId, int depth) {
		if (depth > MAX_RECURSION_DEPTH) {
			log.severe(currentTableName + ": Maximum recursion depth reached while finding root record ID. Possible configuration error.");
			return -1;
		}
		
		MKafkaRegistryTable registryTable = new MKafkaRegistryTable(Env.getCtx(), currentRegistryTableId, null);
		if (registryTable.get_ValueAsInt("parent_table_id") == 0) {
			return currentRecordId;
		}

		MTable parentTable = new MTable(Env.getCtx(), registryTable.get_ValueAsInt("parent_table_id"), null);
		MTable currentTable = new MTable(Env.getCtx(), registryTable.get_ValueAsInt("ad_table_id"), null);

		// psmt value setup
		String parentTableName = parentTable.getTableName();
		String parentTableId = (parentTableName + "_ID");
		String currentTableName = currentTable.getTableName();
		String currentTableIdName = (currentTableName + "_ID");

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT x.").append(parentTableId).append(" FROM ").append(parentTableName).append(" x ")
				.append(" JOIN ").append(currentTableName).append(" y ").append(" ON x.").append(parentTableId)
				.append(" = y.").append(parentTableId).append(" WHERE y.").append(currentTableIdName).append(" = ").append(currentRecordId);

		int parentRecordId = DB.getSQLValueEx(null, sql.toString());

		// Tìm parent registry table ID
		int parentRegistryTableId = getParentRegistryTableId(currentRegistryTableId);

		if (parentRegistryTableId <= 0) {
			log.severe(currentTableIdName + ": Could not find parent registry table ID. Possible configuration error.");
			return -1;
		}

		// recursion
		return getRootRecordId(parentRegistryTableId, parentRecordId, depth + 1);
	}

	private int getRegistryTableIdByRegistryServiceID(int registryServiceId, int currentTableId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rt.kafka_registrytable_id ")
		   .append("FROM kafka_registrytable rt ")
		   .append("JOIN kafka_registryservice kr ")
		   .append("  ON rt.kafka_registryservice_id = kr.kafka_registryservice_id ")
		   .append("WHERE rt.kafka_registryservice_id = ").append(registryServiceId)
		   .append(" AND rt.ad_table_id = ").append(currentTableId);

		return DB.getSQLValueEx(null, sql.toString());
	}

	private int getParentRegistryTableId(int currentRegistryTableId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rt2.kafka_registrytable_id ")
		   .append("FROM kafka_registrytable rt1 ")
		   .append("JOIN kafka_registrytable rt2 ")
		   .append("  ON rt1.parent_table_id = rt2.ad_table_id ")
		   .append("  AND rt1.kafka_registryservice_id = rt2.kafka_registryservice_id ")
		   .append("WHERE rt1.kafka_registrytable_id = ").append(currentRegistryTableId);

		return DB.getSQLValueEx(null, sql.toString());
	}

	private List<MKafkaRegistryService> listRegistryService(int tableId, String eventTopic) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT rs.kafka_registryservice_id ")
		   .append("FROM kafka_registryservice rs ")
		   .append("JOIN kafka_registrytable rt ")
		   .append("  ON rs.kafka_registryservice_id = rt.kafka_registryservice_id ")
		   .append("JOIN kafka_registryevent re ")
		   .append("  ON rt.kafka_registrytable_id = re.kafka_registrytable_id ")
		   .append("JOIN kafka_eventtype et ")
		   .append("  ON et.kafka_eventtype_id = re.kafka_eventtype_id ")
		   .append("WHERE rt.ad_table_id = ").append(tableId)
		   .append("  AND rs.isactive = 'Y' ")
		   .append("  AND et.name = '").append(getEventTypeName(eventTopic)).append("'");

		List<MKafkaRegistryService> services = new ArrayList<>();

		try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null)) {
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int serviceId = rs.getInt("kafka_registryservice_id");
					MKafkaRegistryService service = new MKafkaRegistryService(Env.getCtx(), serviceId, null);

					services.add(service);
				}
			}
		} catch (Exception e) {
			log.severe("Error loading registry services: " + e);
			e.printStackTrace();
		}

		return services;
	}

	private String getEventTypeName(String eventTopic) {
		if (IEventTopics.PO_AFTER_NEW.equals(eventTopic)) {
			return "PO_AFTER_NEW";
		} else if (IEventTopics.PO_AFTER_CHANGE.equals(eventTopic)) {
			return "PO_AFTER_CHANGE";
		} else if (IEventTopics.PO_AFTER_DELETE.equals(eventTopic)) {
			return "PO_AFTER_DELETE";
		} else if (IEventTopics.DOC_AFTER_COMPLETE.equals(eventTopic)) {
			return "DOC_AFTER_COMPLETE";
		}
		return eventTopic; // fallback
	}

	private int getHeaderTableId(int registryServiceId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rt.kafka_registrytable_id ")
		   .append("FROM kafka_registrytable rt ")
		   .append("JOIN kafka_registryservice kr ")
		   .append("  ON rt.kafka_registryservice_id = kr.kafka_registryservice_id ")
		   .append("WHERE rt.kafka_registryservice_id = ").append(registryServiceId)
		   .append(" AND rt.parent_table_id IS NULL");

		return DB.getSQLValueEx(null, sql.toString());
	}

	private List<MKafkaRegistryTable> getChildRegistryTables(int registryServiceId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT kafka_registrytable_id ")
		   .append("FROM kafka_registrytable ")
		   .append("WHERE kafka_registryservice_id = ").append(registryServiceId)
		   .append("  AND parent_table_id IS NOT NULL");

		List<MKafkaRegistryTable> tableList = new ArrayList<>();

		try (PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null)) {
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					int tableId = rs.getInt("kafka_registrytable_id");
					MKafkaRegistryTable table = new MKafkaRegistryTable(Env.getCtx(), tableId, null);
					tableList.add(table);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tableList;
	}
	
	private String getDocumentNo(int tableId, int recordId) {
	    try {
	        MTable table = MTable.get(Env.getCtx(), tableId);
	        PO po = table.getPO(recordId, null);
	        
	        if (po != null) {
	            // Kiểm tra xem bảng có cột DocumentNo không
	            int columnIndex = po.get_ColumnIndex("DocumentNo");
	            if (columnIndex >= 0) {
	                return po.get_ValueAsString("DocumentNo");
	            }
	        }
	    } catch (Exception e) {
	        log.severe("Error getting DocumentNo: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return null;
	}

}

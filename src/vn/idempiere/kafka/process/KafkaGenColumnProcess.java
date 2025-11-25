package vn.idempiere.kafka.process;


import java.util.List;

import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import vn.idempiere.kafka.model.MKafkaRegistryColumn;
import vn.idempiere.kafka.model.MKafkaRegistryTable;

public class KafkaGenColumnProcess extends SvrProcess{
	
	MKafkaRegistryTable registryTable;
	
	@Override
	protected void prepare() {
		System.out.println("Prepare Kafka Gen Column Process");
		int registryTableRecordId = getRecord_ID();
		registryTable = new MKafkaRegistryTable(getCtx(), registryTableRecordId, get_TrxName());
	}

	@Override
	protected String doIt() throws Exception {
		genColumn(getListColumn());
		return "Generate columns completed";
	}
	
	private List<List<Object>> getListColumn() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ad_column_id FROM ad_column WHERE ad_table_id = ? ");
		return DB.getSQLArrayObjectsEx(get_TrxName(), sql.toString(), registryTable.getAD_Table_ID());
	}
	
	private void genColumn(List<List<Object>> listColumn) {
	    for (List<Object> row : listColumn) {
	        int adColumnId = ((java.math.BigDecimal) row.get(0)).intValue();
	        
	        MKafkaRegistryColumn registryColumn = new MKafkaRegistryColumn(getCtx(), 0, get_TrxName());
	        registryColumn.setKafka_RegistryTable_ID(registryTable.get_ValueAsInt("kafka_registrytable_id"));
	        registryColumn.setAD_Column_ID(adColumnId);
	        registryColumn.saveEx();
	    }
	}


}

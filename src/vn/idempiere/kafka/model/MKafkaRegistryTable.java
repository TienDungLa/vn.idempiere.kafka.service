package vn.idempiere.kafka.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MKafkaRegistryTable extends X_Kafka_RegistryTable{

	private static final long serialVersionUID = 5414406951278424438L;

	public MKafkaRegistryTable(Properties ctx, String Kafka_RegistryTable_UU, String trxName, String[] virtualColumns) {
		super(ctx, Kafka_RegistryTable_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryTable(Properties ctx, String Kafka_RegistryTable_UU, String trxName) {
		super(ctx, Kafka_RegistryTable_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryTable(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryTable(Properties ctx, int Kafka_RegistryTable_ID, String trxName, String[] virtualColumns) {
		super(ctx, Kafka_RegistryTable_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryTable(Properties ctx, int Kafka_RegistryTable_ID, String trxName) {
		super(ctx, Kafka_RegistryTable_ID, trxName);
		// TODO Auto-generated constructor stub
	}

}

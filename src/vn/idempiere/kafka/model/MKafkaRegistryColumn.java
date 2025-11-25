package vn.idempiere.kafka.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MKafkaRegistryColumn extends X_Kafka_RegistryColumn{

	private static final long serialVersionUID = -4962748545237466279L;

	public MKafkaRegistryColumn(Properties ctx, String Kafka_RegistryColumn_UU, String trxName) {
		super(ctx, Kafka_RegistryColumn_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryColumn(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryColumn(Properties ctx, int Kafka_RegistryColumn_ID, String trxName, String[] virtualColumns) {
		super(ctx, Kafka_RegistryColumn_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryColumn(Properties ctx, int Kafka_RegistryColumn_ID, String trxName) {
		super(ctx, Kafka_RegistryColumn_ID, trxName);
		// TODO Auto-generated constructor stub
	}

}

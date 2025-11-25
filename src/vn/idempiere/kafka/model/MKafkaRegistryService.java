package vn.idempiere.kafka.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MKafkaRegistryService extends X_Kafka_RegistryService{

	private static final long serialVersionUID = -1438177756108721617L;

	public MKafkaRegistryService(Properties ctx, String Kafka_RegistryService_UU, String trxName,
			String[] virtualColumns) {
		super(ctx, Kafka_RegistryService_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryService(Properties ctx, String Kafka_RegistryService_UU, String trxName) {
		super(ctx, Kafka_RegistryService_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryService(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryService(Properties ctx, int Kafka_RegistryService_ID, String trxName,
			String[] virtualColumns) {
		super(ctx, Kafka_RegistryService_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MKafkaRegistryService(Properties ctx, int Kafka_RegistryService_ID, String trxName) {
		super(ctx, Kafka_RegistryService_ID, trxName);
		// TODO Auto-generated constructor stub
	}

}

package vn.idempiere.kafka.model;

import java.sql.ResultSet;
import java.util.Properties;

public class MAuditlog extends X_Kafka_Auditlog {

	private static final long serialVersionUID = -3028175630979405480L;

	public MAuditlog(Properties ctx, String Kafka_Auditlog_UU, String trxName, String[] virtualColumns) {
		super(ctx, Kafka_Auditlog_UU, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MAuditlog(Properties ctx, String Kafka_Auditlog_UU, String trxName) {
		super(ctx, Kafka_Auditlog_UU, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAuditlog(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
		// TODO Auto-generated constructor stub
	}

	public MAuditlog(Properties ctx, int Kafka_Auditlog_ID, String trxName, String[] virtualColumns) {
		super(ctx, Kafka_Auditlog_ID, trxName, virtualColumns);
		// TODO Auto-generated constructor stub
	}

	public MAuditlog(Properties ctx, int Kafka_Auditlog_ID, String trxName) {
		super(ctx, Kafka_Auditlog_ID, trxName);
		// TODO Auto-generated constructor stub
	}

	
}

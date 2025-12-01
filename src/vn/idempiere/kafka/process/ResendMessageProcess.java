package vn.idempiere.kafka.process;

import java.util.logging.Level;

import org.compiere.process.ProcessInfo;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

import vn.idempiere.kafka.model.MAuditlog;
import vn.idempiere.kafka.model.MKafkaRegistryService;
import vn.idempiere.kafka.producer.KafkaProducer;

public class ResendMessageProcess extends SvrProcess {

	@Override
	protected void prepare() {
		
	}

	@Override
	protected String doIt() throws Exception {
		// Lấy danh sách ID từ T_Selection (các dòng được chọn từ InfoWindow)
		int AD_PInstance_ID = getAD_PInstance_ID();
		
		String sql = "SELECT T_Selection_ID FROM T_Selection WHERE AD_PInstance_ID=?";
		
		int count = 0;
		java.sql.PreparedStatement pstmt = null;
		java.sql.ResultSet rs = null;
		
		try {
			pstmt = DB.prepareStatement(sql, get_TrxName());
			pstmt.setInt(1, AD_PInstance_ID);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int recordID = rs.getInt(1);
				
				// Xử lý từng record được chọn
				if (processRecord(recordID)) {
					count++;
				}
			}
		} finally {
			DB.close(rs, pstmt);
		}
		
		return "Processed " + count + " record(s)";
	}
	
	private boolean processRecord(int recordID) {
		try {
			// TODO: Implement logic resend message cho record này
			log.severe("Processing record: " + recordID);
			MAuditlog auditlog = new MAuditlog(getCtx(), recordID, get_TrxName());
			MKafkaRegistryService rs = new MKafkaRegistryService(getCtx(),auditlog.get_ValueAsInt("Kafka_RegistryService_ID"), get_TrxName());
			
			KafkaProducer  producer = null;
			try {
				producer = new KafkaProducer(rs);
				producer.resendAsync(auditlog);
			}
			catch (Exception e) {
				log.log(Level.SEVERE, "Error creating KafkaProducer for record " + recordID, e);
				return false;
			}
			finally {
				if (producer != null) {
					producer.flush();
					producer.close();
				}
			}
			
			return true;
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error processing record " + recordID, e);
			return false;
		}
	}
}

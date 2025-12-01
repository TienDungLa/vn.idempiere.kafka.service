package vn.idempiere.kafka.producer;

import java.util.Properties;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import vn.idempiere.kafka.library.KafkaClientFactory;
import vn.idempiere.kafka.model.MAuditlog;
import vn.idempiere.kafka.model.MKafkaRegistryService;

/**
 * Kafka Producer Service
 * Wrapper class to send messages to Kafka topics using the kafka.library
 */
public class KafkaProducer  {
	
	private static final Logger log = Logger.getLogger(KafkaProducer.class.getName());
	
	private Producer<String, String> producer;
	private MKafkaRegistryService rs;
	
	public KafkaProducer(MKafkaRegistryService rs) {
		this.rs = rs;
		initProducer();
	}
	
	private void initProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, rs.get_ValueAsString("bootstrapserversconfig"));
		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000); // Block for max 5 seconds
		props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000); // Request timeout
		props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 10000); // Total time for a message	
		props.put(ProducerConfig.RETRIES_CONFIG, 2); // Number of retries
		
		// Use the factory from kafka.library
		this.producer = KafkaClientFactory.getProducerFactory().createProducer(props);
	}
	
	public RecordMetadata sendSync(String topic, String key, String value) {
		try {
			ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
			RecordMetadata metadata = producer.send(record).get();
			log.info(String.format("Message sent successfully to topic=%s, partition=%d, offset=%d", 
					metadata.topic(), metadata.partition(), metadata.offset()));
			return metadata;
		} catch (Exception e) {
			log.log(Level.SEVERE, "Error sending message synchronously", e);
			throw new RuntimeException("Failed to send message to Kafka", e);
		}
	}
	
	public Future<RecordMetadata> sendAsync(String topic, String key, String value, MAuditlog auditlog) {
		
		auditlog.setMessageData(value);
		auditlog.setMessageSize_Bytes(value != null ? value.getBytes().length : 0);
			
	    try {
	        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);           
	        Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
	        	// nếu gửi message lỗi
	            if (exception != null) {
	                log.log(Level.SEVERE, "Error sending message asynchronously", exception);
	                auditlog.setMessageStatus("Error");
	                auditlog.setErrorStackTrace(exception.toString());
	                auditlog.setErrorMessage(exception.getMessage());
	            // gửi thành công
	            } else {
	                log.info(String.format("Message sent async to topic=%s, partition=%d, offset=%d", 
	                        metadata.topic(), metadata.partition(), metadata.offset()));
	                auditlog.setMessageStatus("Success");
	            }
	            
	            auditlog.saveEx(); 
	        });
	        
	        return future;
	    } catch (Exception e) {
	        log.log(Level.SEVERE, "Error creating async send", e);
	        auditlog.setMessageStatus("Error");
            auditlog.setErrorStackTrace(e.toString());
            auditlog.setErrorMessage(e.getMessage());
            auditlog.saveEx();
	        return null;
	    }
	}
	
	public Future<RecordMetadata> resendAsync(MAuditlog auditlog) {
		String topic = auditlog.get_ValueAsString("TopicName");
		String value = auditlog.get_ValueAsString("MessageData");
		String key = auditlog.get_ValueAsString("MessageKey");
		
		try {
	        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);           
	        Future<RecordMetadata> future = producer.send(record, (metadata, exception) -> {
	        	// nếu gửi message lỗi
	            if (exception != null) {
	                log.log(Level.SEVERE, "Error resending message asynchronously", exception);
	                auditlog.setMessageStatus("Error");
	                auditlog.setErrorStackTrace(exception.toString());
	                auditlog.setErrorMessage(exception.getMessage());
	                auditlog.setResendCount(auditlog.getResendCount() + 1);
	            // gửi thành công
	            } else {
	                log.info(String.format("Message resent async to topic=%s, partition=%d, offset=%d", 
	                        metadata.topic(), metadata.partition(), metadata.offset()));
	                auditlog.setErrorStackTrace(null);
	                auditlog.setErrorMessage(null);
	                auditlog.setResendCount(auditlog.getResendCount() + 1);
	                auditlog.setMessageStatus("Resend Success");
	            }
	            auditlog.saveEx(); 
	        });
	        return future;
	    } catch (Exception e) {
	        log.log(Level.SEVERE, "Error  async resend", e);
	        auditlog.setMessageStatus("Error");
			auditlog.setErrorStackTrace(e.toString());
			auditlog.setErrorMessage(e.getMessage());
			auditlog.saveEx();
	    }	
		return null;
	}
	
	public RecordMetadata send(String topic, String value) {
		return sendSync(topic, null, value);
	}
	
	/**
	 * Flush all buffered messages
	 */
	public void flush() {
		if (producer != null) {
			producer.flush();
			log.info("Producer flushed");
		}
	}
	
	/**
	 * Close the producer
	 */
	public void close() {
		if (producer != null) {
			producer.close();
			log.info("Producer closed");
		}
	}
	
	/**
	 * Get the underlying Kafka Producer instance
	 * @return Producer<String, String>
	 */
	public Producer<String, String> getProducer() {
		return producer;
	}

	
}

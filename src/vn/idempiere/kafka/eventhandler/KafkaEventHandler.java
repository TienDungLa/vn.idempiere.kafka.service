package vn.idempiere.kafka.eventhandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.adempiere.base.event.AbstractEventHandler;
import org.adempiere.base.event.IEventManager;
import org.adempiere.base.event.IEventTopics;
import org.compiere.model.PO;
import org.compiere.util.DB;
import org.compiere.util.Trx;
import org.osgi.service.event.Event;

public class KafkaEventHandler extends AbstractEventHandler {
	List<String> eventStack = new ArrayList<String>();
	int counter = 0;
	@Override
	protected void initialize() {
		// Register handlers for registry table changes
		registerTableEvent(IEventTopics.PO_AFTER_NEW, "Kafka_RegistryTable");
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, "Kafka_RegistryTable");
		registerTableEvent(IEventTopics.PO_AFTER_DELETE, "Kafka_RegistryTable");

		// Register handlers for all monitored tables from the database
		registerCurrentTables();
	}

	@Override
	protected void doHandleEvent(Event event) {
		if (event == null) {
			return;
		}
		
		PO po = getPO(event);
		String tableName = po.get_TableName();
		
		if (tableName == null || tableName.isEmpty()) {
			return;
		}

		// If the registry configuration changes, reload all event handlers.
		if (tableName.equals("Kafka_RegistryTable")) {
			reloadRegisteredTables();
			return; // Stop further processing for this event
		}
		
		eventStack.add(getEventTypeName(event.getTopic()));
		Trx trx = Trx.get(po.get_TrxName(), false);
		trx.addTrxEventListener(new EventTrxListener(po, event.getTopic(),eventStack,this));
	}
	
	/**
	 * Reloads all registered table events.
	 * This is typically called when the monitoring configuration changes.
	 */
	public void reloadRegisteredTables() {
	    if (eventManager != null) {
	        // Unregister all current handlers managed by this instance
	        eventManager.unregister(this);
	        // Re-initialize and register all handlers based on the current configuration
	        initialize();
	    }
	}

	private List<String> queryMonitoredTableNames() {
		String sql = """
				SELECT DISTINCT t.tablename
				FROM kafka_registryservice rs
				JOIN kafka_registrytable rt
				  ON rs.kafka_registryservice_id = rt.kafka_registryservice_id
				JOIN ad_table t
				  ON rt.ad_table_id = t.ad_table_id
				WHERE rs.isactive = 'Y'
				ORDER BY t.tablename
				""";
		
		List<String> tables = new ArrayList<>();

		try (PreparedStatement pstmt = DB.prepareStatement(sql, null); ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				tables.add(rs.getString("tablename"));
			}
		} catch (Exception e) {
//	        log.error("Failed to query monitored tables", e);
		}

		return tables;
	}

	private void registerTableForAllEvents(String tableName) {
		registerTableEvent(IEventTopics.PO_AFTER_NEW, tableName);
		registerTableEvent(IEventTopics.PO_AFTER_CHANGE, tableName);
		registerTableEvent(IEventTopics.PO_AFTER_DELETE, tableName);
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, tableName);
	}

	private void registerCurrentTables() {
		List<String> tables = queryMonitoredTableNames();
		for (String tableName : tables) {
			registerTableForAllEvents(tableName);
		}
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
		
	@Override
	public void unbindEventManager(IEventManager eventManager) {
	    // This method is called by the OSGi framework when the component is deactivated.
	    // It should only perform cleanup.
	    if (this.eventManager != null) {
	        this.eventManager.unregister(this);
	    }
	    super.unbindEventManager(eventManager);
	}
}

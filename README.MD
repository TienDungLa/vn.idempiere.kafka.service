# iDempiere Kafka Integration Plugin

## 1. Introduction

This plugin integrates iDempiere ERP with Apache Kafka, a distributed event streaming platform. It captures data changes and business events (like document completion) within iDempiere and publishes them as messages to Kafka topics.

This solves the challenge of synchronizing iDempiere data with external systems in a reliable, scalable, and real-time manner. By adopting an event-driven architecture, it decouples iDempiere from other applications, allowing for more flexible and resilient enterprise integrations.

**Benefits:**
- **Real-time Data Synchronization:** Instantly propagate business events to other systems.
- **Decoupled Architecture:** Reduces dependencies between iDempiere and other applications.
- **Scalability & Resilience:** Leverages Kafka's high-throughput and fault-tolerant capabilities.
- **Extensibility:** Easily configure new tables and events to be published without changing the code.

## 2. Key Features

- **Event-Driven:** Listens to iDempiere's internal events (`PO_AFTER_NEW`, `PO_AFTER_CHANGE`, `PO_AFTER_DELETE`, `DOC_AFTER_COMPLETE`).
- **Configurable:** A dedicated window allows users to define "Registry Services" to select which tables, events, and columns to monitor and publish.
- **Dynamic Message Payload:** Automatically constructs JSON messages, including support for parent-child relationships (e.g., an invoice and its lines).
- **Data Chunking:** For records with many children (e.g., an order with hundreds of lines), the plugin can split the data into multiple smaller messages ("chunks") to avoid message size limits.
- **Audit Trail:** Logs every message sent (or failed) to a dedicated "Kafka Audit Log" window for traceability and debugging.
- **OSGi-based:** Built as two OSGi bundles: a reusable library for Kafka clients and a service plugin containing the core logic.

## 3. Architecture & Folder Structure

The project is composed of two main OSGi bundles:

1.  **`vn.idempiere.kafka.library`**: A generic library plugin that provides a factory (`KafkaClientFactory`) for creating Kafka producer and consumer instances. This abstracts the core Kafka client dependencies and can be reused by other plugins.
2.  **`vn.idempiere.kafka.serivce`**: The main service plugin that contains all the business logic for event handling and message publishing.

## 4. Main Classes

-   **`KafkaEventHandler`** (in `vn.idempiere.kafka.eventhandler`): Extends `AbstractEventHandler`. It registers listeners for all tables configured in the "Kafka Registry Table" window. When a data change occurs, it initiates the process by attaching an `EventTrxListener` to the current transaction.
-   **`EventTrxListener`** (in `vn.idempiere.kafka.eventhandler`): Implements `TrxEventListener`. This listener waits for the transaction to be successfully committed (`afterCommit`). It then queries the configuration, builds the message using `MessageBuilder`, and sends it via `KafkaProducer`.
-   **`MessageBuilder`** (in `vn.idempiere.kafka.producer`): A powerful class responsible for constructing the JSON message. It fetches the header record and its child records, handles data chunking, and formats the final payload.
-   **`KafkaProducer`** (in `vn.idempiere.kafka.producer`): A wrapper around the official Apache Kafka producer client. It handles the connection to the Kafka broker and the asynchronous sending of messages, including callbacks for logging success or failure.
-   **`MAuditlog`**: The model class for the audit log, used to record the details of each sent message.

## 5. Build & Deploy

### Build

This project uses Apache Maven for building.

1.  **Prerequisites**:
	- 	iDempiere 12
    -   Java JDK (version compatible with your iDempiere target)
    -   Apache Maven 

2.  **Build**:
    Import into your iDempiere project, reload your maven then run the bundle with your project

## 6. Usage

After deployment and configuration, the plugin runs automatically in the background. User interaction is primarily for configuration:

1.  **Menu Entry**: Log in to iDempiere as System Administrator. Navigate to the "Kafka Registry Service" window in the application menu.
2.  **Configuration**:
    -   Create a new **Kafka Registry Service** record. Here you define the connection details for your Kafka broker (Bootstrap Servers) and the target Topic Name.
    -   In the **Registry Table** tab, add the iDempiere tables you want to monitor (e.g., `C_Invoice`, `C_Order`). Define the parent-child relationships if necessary.
    -   For each table, go to the **Registry Column** tab. You can use the "Generate Column" process to auto-populate all columns or add them manually. Select which columns should be included in the final JSON message.
    -   In the **Registry Event** tab, select the events that should trigger a message for this table (e.g., `PO_AFTER_CHANGE`, `DOC_AFTER_COMPLETE`).
3.  **Operation**: Once configured and active, any changes to the monitored tables that match the selected events will cause a message to be published to the configured Kafka topic.
4.  **Auditing**: You can monitor the outcome of the message publishing in the "Kafka Audit Log".

## 7. Extension Points

This plugin implements the following standard iDempiere/OSGi extension points:

-   **`org.adempiere.base.event.IEventHandler`**: Implemented by `KafkaEventHandler` to listen to data and document events from the iDempiere Event Manager. This is the primary entry point for the plugin's logic.
-   **`org.compiere.util.TrxEventListener`**: Implemented by `EventTrxListener` to hook into the transaction lifecycle, ensuring that messages are only sent after the database transaction has been successfully committed.

## 8. Configuration

All configuration for this plugin is done through standard iDempiere windows, which are added by the plugin's PackIn data.
No special `System Configurator` settings are required.

-   **Window**: `Kafka Registry Service`
-   **Process**: `Generate Column` (available from the `Kafka Registry Table` window)
-   **Tables for Configuration**:
    -   `Kafka_RegistryService`: Main configuration entry.
    -   `Kafka_RegistryTable`: Defines tables to watch.
    -   `Kafka_RegistryColumn`: Defines columns to include in the message.
    -   `Kafka_RegistryEvent`: Defines which events to listen for.
-   **Table for Auditing**:
    -   `Kafka_Auditlog`: Stores a log of all sent messages.



## 9. Demo 

	 Xem tài liệu chi tiết tại [Hướng dẫn sử dụng](Hướng dẫn sử dụng Kafka Plugin.docx)
	 
## 10. Author & License

-   **Author**: [DungLT]
-   **Version**: 1.0.0-SNAPSHOT

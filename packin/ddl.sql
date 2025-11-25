-- Xóa và tạo lại bảng adempiere.kafka_eventtype
DROP TABLE IF EXISTS adempiere.kafka_eventtype CASCADE;

CREATE TABLE adempiere.kafka_eventtype (
	kafka_eventtype_id numeric(10) NOT NULL,
	ad_client_id numeric(10) DEFAULT 0 NOT NULL,
	ad_org_id numeric(10) DEFAULT 0 NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	value varchar(40) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) NULL,
	CONSTRAINT kafka_eventtype_key PRIMARY KEY (kafka_eventtype_id),
	CONSTRAINT kafka_eventtype_value_uniq UNIQUE (value)
);


-- Xóa và tạo lại bảng adempiere.kafka_registryservice
DROP TABLE IF EXISTS adempiere.kafka_registryservice CASCADE;

CREATE TABLE adempiere.kafka_registryservice (
	kafka_registryservice_id numeric(10) NOT NULL,
	ad_client_id numeric(10) DEFAULT 0 NOT NULL,
	ad_org_id numeric(10) DEFAULT 0 NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	value varchar(40) NOT NULL,
	"name" varchar(60) NOT NULL,
	description varchar(255) NULL,
	topicname varchar(255) NOT NULL,
	bootstrapserversconfig varchar DEFAULT 'localhost:9090'::character varying NULL,
	CONSTRAINT kafka_registryservice_key PRIMARY KEY (kafka_registryservice_id)
);


-- Xóa và tạo lại bảng adempiere.kafka_auditlog
DROP TABLE IF EXISTS adempiere.kafka_auditlog CASCADE;

CREATE TABLE adempiere.kafka_auditlog (
	kafka_auditlog_id numeric(10) NOT NULL,
	ad_client_id numeric(10) DEFAULT 0 NOT NULL,
	ad_org_id numeric(10) DEFAULT 0 NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	kafka_registryservice_id numeric(10) NULL,
	tablename varchar(60) NULL,
	recordid numeric(10) NULL,
	messagekey varchar(60) NULL,
	eventtype varchar(20) NULL,
	messagestatus varchar(20) NULL,
	errorname varchar(255) NULL,
	errormessage text NULL,
	errorstacktrace text NULL,
	messagedata text NULL,
	messagesize_bytes numeric(10) NULL,
	topicname varchar(60) NULL,
	CONSTRAINT kafka_auditlog_key PRIMARY KEY (kafka_auditlog_id),
	CONSTRAINT kafka_auditlog_service_fk FOREIGN KEY (kafka_registryservice_id) REFERENCES adempiere.kafka_registryservice(kafka_registryservice_id)
);


-- Xóa và tạo lại bảng adempiere.kafka_registrytable
DROP TABLE IF EXISTS adempiere.kafka_registrytable CASCADE;

CREATE TABLE adempiere.kafka_registrytable (
	kafka_registrytable_id numeric(10) NOT NULL,
	ad_client_id numeric(10) DEFAULT 0 NOT NULL,
	ad_org_id numeric(10) DEFAULT 0 NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	kafka_registryservice_id numeric(10) NOT NULL,
	ad_table_id numeric(10) NOT NULL,
	parent_table_id numeric(10) NULL,
	chunksize numeric DEFAULT 10 NULL,
	isenableschema bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	CONSTRAINT kafka_registrytable_key PRIMARY KEY (kafka_registrytable_id),
	CONSTRAINT kafka_registrytable_service_fk FOREIGN KEY (kafka_registryservice_id) REFERENCES adempiere.kafka_registryservice(kafka_registryservice_id)
);


-- Xóa và tạo lại bảng adempiere.kafka_registrycolumn
DROP TABLE IF EXISTS adempiere.kafka_registrycolumn CASCADE;

CREATE TABLE adempiere.kafka_registrycolumn (
	kafka_registrycolumn_id numeric(10) NOT NULL,
	ad_client_id numeric(10) DEFAULT 0 NOT NULL,
	ad_org_id numeric(10) DEFAULT 0 NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	kafka_registrytable_id numeric(10) NOT NULL,
	ad_column_id numeric(10) NOT NULL,
	responsename varchar(60) NULL,
	CONSTRAINT kafka_registrycolumn_key PRIMARY KEY (kafka_registrycolumn_id),
	CONSTRAINT kafka_registrycolumn_table_fk FOREIGN KEY (kafka_registrytable_id) REFERENCES adempiere.kafka_registrytable(kafka_registrytable_id)
);


-- Xóa và tạo lại bảng adempiere.kafka_registryevent
DROP TABLE IF EXISTS adempiere.kafka_registryevent CASCADE;

CREATE TABLE adempiere.kafka_registryevent (
	kafka_registryevent_id numeric(10) NOT NULL,
	ad_client_id numeric(10) DEFAULT 0 NOT NULL,
	ad_org_id numeric(10) DEFAULT 0 NOT NULL,
	isactive bpchar(1) DEFAULT 'Y'::bpchar NOT NULL,
	created timestamp DEFAULT now() NOT NULL,
	createdby numeric(10) NOT NULL,
	updated timestamp DEFAULT now() NOT NULL,
	updatedby numeric(10) NOT NULL,
	kafka_registrytable_id numeric(10) NOT NULL,
	kafka_eventtype_id numeric(10) NOT NULL,
	CONSTRAINT kafka_registryevent_key PRIMARY KEY (kafka_registryevent_id),
	CONSTRAINT kafka_registryevent_eventtype_fk FOREIGN KEY (kafka_eventtype_id) REFERENCES adempiere.kafka_eventtype(kafka_eventtype_id) ON DELETE CASCADE,
	CONSTRAINT kafka_registryevent_table_fk FOREIGN KEY (kafka_registrytable_id) REFERENCES adempiere.kafka_registrytable(kafka_registrytable_id) ON DELETE CASCADE
);
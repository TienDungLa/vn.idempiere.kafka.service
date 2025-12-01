/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package vn.idempiere.kafka.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for Kafka_Auditlog
 *  @author iDempiere (generated) 
 *  @version Release 12
 */
@SuppressWarnings("all")
public interface I_Kafka_Auditlog 
{

    /** TableName=Kafka_Auditlog */
    public static final String Table_Name = "Kafka_Auditlog";

    /** AD_Table_ID=1000022 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Tenant.
	  * Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within tenant
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within tenant
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name ErrorMessage */
    public static final String COLUMNNAME_ErrorMessage = "ErrorMessage";

	/** Set Error Message	  */
	public void setErrorMessage (String ErrorMessage);

	/** Get Error Message	  */
	public String getErrorMessage();

    /** Column name ErrorStackTrace */
    public static final String COLUMNNAME_ErrorStackTrace = "ErrorStackTrace";

	/** Set Error Stack Trace	  */
	public void setErrorStackTrace (String ErrorStackTrace);

	/** Get Error Stack Trace	  */
	public String getErrorStackTrace();

    /** Column name EventType */
    public static final String COLUMNNAME_EventType = "EventType";

	/** Set Event Type.
	  * Type of Event
	  */
	public void setEventType (String EventType);

	/** Get Event Type.
	  * Type of Event
	  */
	public String getEventType();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Kafka_Auditlog_ID */
    public static final String COLUMNNAME_Kafka_Auditlog_ID = "Kafka_Auditlog_ID";

	/** Set Kafka_Auditlog	  */
	public void setKafka_Auditlog_ID (int Kafka_Auditlog_ID);

	/** Get Kafka_Auditlog	  */
	public int getKafka_Auditlog_ID();

    /** Column name Kafka_RegistryService_ID */
    public static final String COLUMNNAME_Kafka_RegistryService_ID = "Kafka_RegistryService_ID";

	/** Set Kafka_RegistryService	  */
	public void setKafka_RegistryService_ID (int Kafka_RegistryService_ID);

	/** Get Kafka_RegistryService	  */
	public int getKafka_RegistryService_ID();

	public I_Kafka_RegistryService getKafka_RegistryService() throws RuntimeException;

    /** Column name MessageData */
    public static final String COLUMNNAME_MessageData = "MessageData";

	/** Set Message Data	  */
	public void setMessageData (String MessageData);

	/** Get Message Data	  */
	public String getMessageData();

    /** Column name MessageKey */
    public static final String COLUMNNAME_MessageKey = "MessageKey";

	/** Set Message Key	  */
	public void setMessageKey (String MessageKey);

	/** Get Message Key	  */
	public String getMessageKey();

    /** Column name MessageSize_Bytes */
    public static final String COLUMNNAME_MessageSize_Bytes = "MessageSize_Bytes";

	/** Set MessageSize_Bytes	  */
	public void setMessageSize_Bytes (int MessageSize_Bytes);

	/** Get MessageSize_Bytes	  */
	public int getMessageSize_Bytes();

    /** Column name MessageStatus */
    public static final String COLUMNNAME_MessageStatus = "MessageStatus";

	/** Set Message Status	  */
	public void setMessageStatus (String MessageStatus);

	/** Get Message Status	  */
	public String getMessageStatus();

    /** Column name RecordID */
    public static final String COLUMNNAME_RecordID = "RecordID";

	/** Set Record ID	  */
	public void setRecordID (int RecordID);

	/** Get Record ID	  */
	public int getRecordID();

    /** Column name ResendCount */
    public static final String COLUMNNAME_ResendCount = "ResendCount";

	/** Set Resend Count	  */
	public void setResendCount (int ResendCount);

	/** Get Resend Count	  */
	public int getResendCount();

    /** Column name TableName */
    public static final String COLUMNNAME_TableName = "TableName";

	/** Set DB Table Name.
	  * Name of the table in the database
	  */
	public void setTableName (String TableName);

	/** Get DB Table Name.
	  * Name of the table in the database
	  */
	public String getTableName();

    /** Column name TopicName */
    public static final String COLUMNNAME_TopicName = "TopicName";

	/** Set Topic name	  */
	public void setTopicName (String TopicName);

	/** Get Topic name	  */
	public String getTopicName();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}

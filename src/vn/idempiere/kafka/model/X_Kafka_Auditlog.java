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
/** Generated Model - DO NOT CHANGE */
package vn.idempiere.kafka.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for Kafka_Auditlog
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="Kafka_Auditlog")
public class X_Kafka_Auditlog extends PO implements I_Kafka_Auditlog, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20251124L;

    /** Standard Constructor */
    public X_Kafka_Auditlog (Properties ctx, int Kafka_Auditlog_ID, String trxName)
    {
      super (ctx, Kafka_Auditlog_ID, trxName);
      /** if (Kafka_Auditlog_ID == 0)
        {
			setKafka_Auditlog_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_Auditlog (Properties ctx, int Kafka_Auditlog_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_Auditlog_ID, trxName, virtualColumns);
      /** if (Kafka_Auditlog_ID == 0)
        {
			setKafka_Auditlog_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_Auditlog (Properties ctx, String Kafka_Auditlog_UU, String trxName)
    {
      super (ctx, Kafka_Auditlog_UU, trxName);
      /** if (Kafka_Auditlog_UU == null)
        {
			setKafka_Auditlog_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_Auditlog (Properties ctx, String Kafka_Auditlog_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_Auditlog_UU, trxName, virtualColumns);
      /** if (Kafka_Auditlog_UU == null)
        {
			setKafka_Auditlog_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Kafka_Auditlog (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_Kafka_Auditlog[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Error Message.
		@param ErrorMessage Error Message
	*/
	public void setErrorMessage (String ErrorMessage)
	{
		set_Value (COLUMNNAME_ErrorMessage, ErrorMessage);
	}

	/** Get Error Message.
		@return Error Message	  */
	public String getErrorMessage()
	{
		return (String)get_Value(COLUMNNAME_ErrorMessage);
	}

	/** Set Error Name.
		@param ErrorName Error Name
	*/
	public void setErrorName (String ErrorName)
	{
		set_Value (COLUMNNAME_ErrorName, ErrorName);
	}

	/** Get Error Name.
		@return Error Name	  */
	public String getErrorName()
	{
		return (String)get_Value(COLUMNNAME_ErrorName);
	}

	/** Set Error Stack Trace.
		@param ErrorStackTrace Error Stack Trace
	*/
	public void setErrorStackTrace (String ErrorStackTrace)
	{
		set_Value (COLUMNNAME_ErrorStackTrace, ErrorStackTrace);
	}

	/** Get Error Stack Trace.
		@return Error Stack Trace	  */
	public String getErrorStackTrace()
	{
		return (String)get_Value(COLUMNNAME_ErrorStackTrace);
	}

	/** EventType AD_Reference_ID=306 */
	public static final int EVENTTYPE_AD_Reference_ID=306;
	/** Process Created = PC */
	public static final String EVENTTYPE_ProcessCreated = "PC";
	/** Process Completed = PX */
	public static final String EVENTTYPE_ProcessCompleted = "PX";
	/** State Changed = SC */
	public static final String EVENTTYPE_StateChanged = "SC";
	/** Set Event Type.
		@param EventType Type of Event
	*/
	public void setEventType (String EventType)
	{

		set_Value (COLUMNNAME_EventType, EventType);
	}

	/** Get Event Type.
		@return Type of Event
	  */
	public String getEventType()
	{
		return (String)get_Value(COLUMNNAME_EventType);
	}

	/** Set Kafka_Auditlog.
		@param Kafka_Auditlog_ID Kafka_Auditlog
	*/
	public void setKafka_Auditlog_ID (int Kafka_Auditlog_ID)
	{
		if (Kafka_Auditlog_ID < 1)
			set_ValueNoCheck (COLUMNNAME_Kafka_Auditlog_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_Kafka_Auditlog_ID, Integer.valueOf(Kafka_Auditlog_ID));
	}

	/** Get Kafka_Auditlog.
		@return Kafka_Auditlog	  */
	public int getKafka_Auditlog_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Kafka_Auditlog_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Kafka_RegistryService getKafka_RegistryService() throws RuntimeException
	{
		return (I_Kafka_RegistryService)MTable.get(getCtx(), I_Kafka_RegistryService.Table_ID)
			.getPO(getKafka_RegistryService_ID(), get_TrxName());
	}

	/** Set Kafka_RegistryService.
		@param Kafka_RegistryService_ID Kafka_RegistryService
	*/
	public void setKafka_RegistryService_ID (int Kafka_RegistryService_ID)
	{
		if (Kafka_RegistryService_ID < 1)
			set_ValueNoCheck (COLUMNNAME_Kafka_RegistryService_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_Kafka_RegistryService_ID, Integer.valueOf(Kafka_RegistryService_ID));
	}

	/** Get Kafka_RegistryService.
		@return Kafka_RegistryService	  */
	public int getKafka_RegistryService_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Kafka_RegistryService_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Message Data.
		@param MessageData Message Data
	*/
	public void setMessageData (String MessageData)
	{
		set_Value (COLUMNNAME_MessageData, MessageData);
	}

	/** Get Message Data.
		@return Message Data	  */
	public String getMessageData()
	{
		return (String)get_Value(COLUMNNAME_MessageData);
	}

	/** Set Message Key.
		@param MessageKey Message Key
	*/
	public void setMessageKey (String MessageKey)
	{
		set_Value (COLUMNNAME_MessageKey, MessageKey);
	}

	/** Get Message Key.
		@return Message Key	  */
	public String getMessageKey()
	{
		return (String)get_Value(COLUMNNAME_MessageKey);
	}

	/** Set MessageSize_Bytes.
		@param MessageSize_Bytes MessageSize_Bytes
	*/
	public void setMessageSize_Bytes (int MessageSize_Bytes)
	{
		set_Value (COLUMNNAME_MessageSize_Bytes, Integer.valueOf(MessageSize_Bytes));
	}

	/** Get MessageSize_Bytes.
		@return MessageSize_Bytes	  */
	public int getMessageSize_Bytes()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MessageSize_Bytes);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Message Status.
		@param MessageStatus Message Status
	*/
	public void setMessageStatus (String MessageStatus)
	{
		set_Value (COLUMNNAME_MessageStatus, MessageStatus);
	}

	/** Get Message Status.
		@return Message Status	  */
	public String getMessageStatus()
	{
		return (String)get_Value(COLUMNNAME_MessageStatus);
	}

	/** Set Record ID.
		@param RecordID Record ID
	*/
	public void setRecordID (int RecordID)
	{
		set_Value (COLUMNNAME_RecordID, Integer.valueOf(RecordID));
	}

	/** Get Record ID.
		@return Record ID	  */
	public int getRecordID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RecordID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DB Table Name.
		@param TableName Name of the table in the database
	*/
	public void setTableName (String TableName)
	{
		set_Value (COLUMNNAME_TableName, TableName);
	}

	/** Get DB Table Name.
		@return Name of the table in the database
	  */
	public String getTableName()
	{
		return (String)get_Value(COLUMNNAME_TableName);
	}

	/** Set Topic name.
		@param TopicName Topic name
	*/
	public void setTopicName (String TopicName)
	{
		set_Value (COLUMNNAME_TopicName, TopicName);
	}

	/** Get Topic name.
		@return Topic name	  */
	public String getTopicName()
	{
		return (String)get_Value(COLUMNNAME_TopicName);
	}
}
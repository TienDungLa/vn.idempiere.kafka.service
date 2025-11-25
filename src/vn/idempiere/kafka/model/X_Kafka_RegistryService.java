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

/** Generated Model for Kafka_RegistryService
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="Kafka_RegistryService")
public class X_Kafka_RegistryService extends PO implements I_Kafka_RegistryService, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20251117L;

    /** Standard Constructor */
    public X_Kafka_RegistryService (Properties ctx, int Kafka_RegistryService_ID, String trxName)
    {
      super (ctx, Kafka_RegistryService_ID, trxName);
      /** if (Kafka_RegistryService_ID == 0)
        {
			setKafka_RegistryService_ID (0);
			setName (null);
			setTopicName (null);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryService (Properties ctx, int Kafka_RegistryService_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_RegistryService_ID, trxName, virtualColumns);
      /** if (Kafka_RegistryService_ID == 0)
        {
			setKafka_RegistryService_ID (0);
			setName (null);
			setTopicName (null);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryService (Properties ctx, String Kafka_RegistryService_UU, String trxName)
    {
      super (ctx, Kafka_RegistryService_UU, trxName);
      /** if (Kafka_RegistryService_UU == null)
        {
			setKafka_RegistryService_ID (0);
			setName (null);
			setTopicName (null);
			setValue (null);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryService (Properties ctx, String Kafka_RegistryService_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_RegistryService_UU, trxName, virtualColumns);
      /** if (Kafka_RegistryService_UU == null)
        {
			setKafka_RegistryService_ID (0);
			setName (null);
			setTopicName (null);
			setValue (null);
        } */
    }

    /** Load Constructor */
    public X_Kafka_RegistryService (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
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
      StringBuilder sb = new StringBuilder ("X_Kafka_RegistryService[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set BOOTSTRAP_SERVERS_CONFIG.
		@param BootstrapServersConfig BOOTSTRAP_SERVERS_CONFIG
	*/
	public void setBootstrapServersConfig (String BootstrapServersConfig)
	{
		set_Value (COLUMNNAME_BootstrapServersConfig, BootstrapServersConfig);
	}

	/** Get BOOTSTRAP_SERVERS_CONFIG.
		@return BOOTSTRAP_SERVERS_CONFIG	  */
	public String getBootstrapServersConfig()
	{
		return (String)get_Value(COLUMNNAME_BootstrapServersConfig);
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
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

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
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

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}
}
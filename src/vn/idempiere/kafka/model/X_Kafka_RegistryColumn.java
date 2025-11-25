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

/** Generated Model for Kafka_RegistryColumn
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="Kafka_RegistryColumn")
public class X_Kafka_RegistryColumn extends PO implements I_Kafka_RegistryColumn, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20251112L;

    /** Standard Constructor */
    public X_Kafka_RegistryColumn (Properties ctx, int Kafka_RegistryColumn_ID, String trxName)
    {
      super (ctx, Kafka_RegistryColumn_ID, trxName);
      /** if (Kafka_RegistryColumn_ID == 0)
        {
			setAD_Column_ID (0);
			setKafka_RegistryColumn_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryColumn (Properties ctx, int Kafka_RegistryColumn_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_RegistryColumn_ID, trxName, virtualColumns);
      /** if (Kafka_RegistryColumn_ID == 0)
        {
			setAD_Column_ID (0);
			setKafka_RegistryColumn_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryColumn (Properties ctx, String Kafka_RegistryColumn_UU, String trxName)
    {
      super (ctx, Kafka_RegistryColumn_UU, trxName);
      /** if (Kafka_RegistryColumn_UU == null)
        {
			setAD_Column_ID (0);
			setKafka_RegistryColumn_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryColumn (Properties ctx, String Kafka_RegistryColumn_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_RegistryColumn_UU, trxName, virtualColumns);
      /** if (Kafka_RegistryColumn_UU == null)
        {
			setAD_Column_ID (0);
			setKafka_RegistryColumn_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Kafka_RegistryColumn (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_Kafka_RegistryColumn[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Column getAD_Column() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Column)MTable.get(getCtx(), org.compiere.model.I_AD_Column.Table_ID)
			.getPO(getAD_Column_ID(), get_TrxName());
	}

	/** Set Column.
		@param AD_Column_ID Column in the table
	*/
	public void setAD_Column_ID (int AD_Column_ID)
	{
		if (AD_Column_ID < 1)
			set_Value (COLUMNNAME_AD_Column_ID, null);
		else
			set_Value (COLUMNNAME_AD_Column_ID, Integer.valueOf(AD_Column_ID));
	}

	/** Get Column.
		@return Column in the table
	  */
	public int getAD_Column_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Column_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Kafka_RegistryColumn.
		@param Kafka_RegistryColumn_ID Kafka_RegistryColumn
	*/
	public void setKafka_RegistryColumn_ID (int Kafka_RegistryColumn_ID)
	{
		if (Kafka_RegistryColumn_ID < 1)
			set_ValueNoCheck (COLUMNNAME_Kafka_RegistryColumn_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_Kafka_RegistryColumn_ID, Integer.valueOf(Kafka_RegistryColumn_ID));
	}

	/** Get Kafka_RegistryColumn.
		@return Kafka_RegistryColumn	  */
	public int getKafka_RegistryColumn_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Kafka_RegistryColumn_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_Kafka_RegistryTable getKafka_RegistryTable() throws RuntimeException
	{
		return (I_Kafka_RegistryTable)MTable.get(getCtx(), I_Kafka_RegistryTable.Table_ID)
			.getPO(getKafka_RegistryTable_ID(), get_TrxName());
	}

	/** Set Kafka_RegistryTable.
		@param Kafka_RegistryTable_ID Kafka_RegistryTable
	*/
	public void setKafka_RegistryTable_ID (int Kafka_RegistryTable_ID)
	{
		if (Kafka_RegistryTable_ID < 1)
			set_ValueNoCheck (COLUMNNAME_Kafka_RegistryTable_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_Kafka_RegistryTable_ID, Integer.valueOf(Kafka_RegistryTable_ID));
	}

	/** Get Kafka_RegistryTable.
		@return Kafka_RegistryTable	  */
	public int getKafka_RegistryTable_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Kafka_RegistryTable_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set responsename.
		@param responsename responsename
	*/
	public void setresponsename (String responsename)
	{
		set_Value (COLUMNNAME_responsename, responsename);
	}

	/** Get responsename.
		@return responsename	  */
	public String getresponsename()
	{
		return (String)get_Value(COLUMNNAME_responsename);
	}
}
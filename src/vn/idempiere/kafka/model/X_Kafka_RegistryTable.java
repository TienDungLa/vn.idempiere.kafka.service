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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for Kafka_RegistryTable
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="Kafka_RegistryTable")
public class X_Kafka_RegistryTable extends PO implements I_Kafka_RegistryTable, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20251119L;

    /** Standard Constructor */
    public X_Kafka_RegistryTable (Properties ctx, int Kafka_RegistryTable_ID, String trxName)
    {
      super (ctx, Kafka_RegistryTable_ID, trxName);
      /** if (Kafka_RegistryTable_ID == 0)
        {
			setAD_Table_ID (0);
			setIsEnableSchema (false);
// N
			setKafka_RegistryService_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryTable (Properties ctx, int Kafka_RegistryTable_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_RegistryTable_ID, trxName, virtualColumns);
      /** if (Kafka_RegistryTable_ID == 0)
        {
			setAD_Table_ID (0);
			setIsEnableSchema (false);
// N
			setKafka_RegistryService_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryTable (Properties ctx, String Kafka_RegistryTable_UU, String trxName)
    {
      super (ctx, Kafka_RegistryTable_UU, trxName);
      /** if (Kafka_RegistryTable_UU == null)
        {
			setAD_Table_ID (0);
			setIsEnableSchema (false);
// N
			setKafka_RegistryService_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_Kafka_RegistryTable (Properties ctx, String Kafka_RegistryTable_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, Kafka_RegistryTable_UU, trxName, virtualColumns);
      /** if (Kafka_RegistryTable_UU == null)
        {
			setAD_Table_ID (0);
			setIsEnableSchema (false);
// N
			setKafka_RegistryService_ID (0);
			setKafka_RegistryTable_ID (0);
        } */
    }

    /** Load Constructor */
    public X_Kafka_RegistryTable (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_Kafka_RegistryTable[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Table getAD_Table() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_ID)
			.getPO(getAD_Table_ID(), get_TrxName());
	}

	/** Set Table.
		@param AD_Table_ID Database Table information
	*/
	public void setAD_Table_ID (int AD_Table_ID)
	{
		if (AD_Table_ID < 1)
			set_Value (COLUMNNAME_AD_Table_ID, null);
		else
			set_Value (COLUMNNAME_AD_Table_ID, Integer.valueOf(AD_Table_ID));
	}

	/** Get Table.
		@return Database Table information
	  */
	public int getAD_Table_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Chunk Size.
		@param ChunkSize Chunk Size
	*/
	public void setChunkSize (BigDecimal ChunkSize)
	{
		set_Value (COLUMNNAME_ChunkSize, ChunkSize);
	}

	/** Get Chunk Size.
		@return Chunk Size	  */
	public BigDecimal getChunkSize()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ChunkSize);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Enable Schema.
		@param IsEnableSchema Enable Schema
	*/
	public void setIsEnableSchema (boolean IsEnableSchema)
	{
		set_Value (COLUMNNAME_IsEnableSchema, Boolean.valueOf(IsEnableSchema));
	}

	/** Get Enable Schema.
		@return Enable Schema	  */
	public boolean isEnableSchema()
	{
		Object oo = get_Value(COLUMNNAME_IsEnableSchema);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	public org.compiere.model.I_AD_Table getParent_Table() throws RuntimeException
	{
		return (org.compiere.model.I_AD_Table)MTable.get(getCtx(), org.compiere.model.I_AD_Table.Table_ID)
			.getPO(getParent_Table_ID(), get_TrxName());
	}

	/** Set Parent Table.
		@param Parent_Table_ID Parent Table
	*/
	public void setParent_Table_ID (int Parent_Table_ID)
	{
		if (Parent_Table_ID < 1)
			set_Value (COLUMNNAME_Parent_Table_ID, null);
		else
			set_Value (COLUMNNAME_Parent_Table_ID, Integer.valueOf(Parent_Table_ID));
	}

	/** Get Parent Table.
		@return Parent Table	  */
	public int getParent_Table_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Parent_Table_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
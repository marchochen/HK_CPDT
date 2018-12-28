package com.cw.wizbank.ae.db;

import java.util.Vector;
import java.sql.*;

import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class DbItemRequirement {
	public long itrItmId;
	public long itrOrder;
	public String itrRequirementType;
	public String itrRequirementSubtype;
	public String itrRequirementRestriction;
	public Timestamp itrRequirementDueDate;
	public int itrAppnFootnoteInd;
	public String itrConditionType;
	public String itrConditionRule;
	public long itrPositiveIatId;
	public long itrNegativeIatId;
	public Timestamp itrProcExecuteTimestamp;
	public Timestamp itr_create_timestamp;
	public String itr_create_usr_id;
	public Timestamp itr_update_timestamp;
	public String itr_update_usr_id;
	
	
	/*
	private static final String SQL_GET = " SELECT itr_requirement_type, itr_requirement_subtype, "
										+ " itr_requirement_restriction, itr_requirement_due_date, itr_appn_footnote_ind, "
										+ "	itr_condition_type, itr_condition_rule, itr_positive_iat_id, itr_negative_iat_id, "
										+ "	itr_proc_execute_timestamp "
										+ " FROM aeItemRequirement "
										+ " WHERE itr_itm_id = ? AND itr_order = ? ";
    
	private static final String SQL_GET_BY_ITM_ID = " SELECT itr_order, itr_requirement_type, itr_requirement_subtype, "
										+ " itr_requirement_restriction, itr_requirement_due_date, itr_appn_footnote_ind, "
										+ "	itr_condition_type, itr_condition_rule, itr_positive_iat_id, itr_negative_iat_id, "
										+ "	itr_proc_execute_timestamp "
										+ " FROM aeItemRequirement "
										+ " WHERE itr_itm_id = ? "
										+ " ORDER BY itr_order ";
    */
	private static final String SQL_GET_BY_MAX_ORDER = " SELECT max(itr_order) as max_itr_order"
										+ " FROM aeItemRequirement "
										+ " WHERE itr_itm_id = ? ";

	private static final String SQL_UPD_ORDER = " UPDATE aeItemRequirement set itr_order=? "
										+ " WHERE itr_itm_id = ? AND itr_order = ? ";

	private static final String SQL_INS = " INSERT INTO aeItemRequirement "
										+ " ( itr_itm_id, itr_order, itr_requirement_type, itr_requirement_subtype, "
										+ "   itr_requirement_restriction, itr_appn_footnote_ind, "
										+ "	  itr_condition_type, itr_condition_rule, itr_positive_iat_id, itr_negative_iat_id, "
										+ "	  itr_proc_execute_timestamp,itr_create_timestamp,itr_create_usr_id,itr_update_timestamp,itr_update_usr_id ) "
										+ " VALUES (?, ?, ?, ?, ?, ?, ?, ";
    private static final String SQL_INS_ext_1 = ", ?, ?, ?,?,?,?,?) ";

	private static final String SQL_UPD = " UPDATE aeItemRequirement "
										+ " SET itr_requirement_type = ?, itr_requirement_subtype = ?, "
										+ " itr_requirement_restriction = ?, itr_appn_footnote_ind = ?, "
										+ " itr_condition_type = ?, "
										+ " itr_positive_iat_id = ?, itr_negative_iat_id = ?, "
										+ " itr_proc_execute_timestamp = ?,itr_update_timestamp = ?,itr_update_usr_id = ? "
										+ " WHERE itr_itm_id = ? AND itr_order = ? ";

	private static final String SQL_UPD_EXEC_STATUS = " UPDATE aeItemRequirement "
										+ " SET itr_proc_execute_timestamp = ? "
										+ " WHERE itr_itm_id = ? AND itr_order = ? ";

	private static final String SQL_DEL = " DELETE FROM aeItemRequirement "
										+ " WHERE itr_itm_id = ? AND itr_order = ? ";

	private static final String SQL_DEL_BY_ITM_ID = " DELETE FROM aeItemRequirement "
										+ " WHERE itr_itm_id = ? ";

	/*
    *Get a record from table
    *Pre-define itrItmId, itrOrder
    */
	public void get(Connection con) throws SQLException, cwSysMessage {
	    String SQL_GET = OuterJoinSqlStatements.getItemRequirement();
		PreparedStatement stmt = con.prepareStatement(SQL_GET);
		int index = 1;
		stmt.setLong(index++, this.itrItmId);
		stmt.setLong(index++, this.itrOrder);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
			this.itrRequirementType			= rs.getString("itr_requirement_type");
			this.itrRequirementSubtype		= rs.getString("itr_requirement_subtype");
			this.itrRequirementRestriction	= rs.getString("itr_requirement_restriction");
			this.itrRequirementDueDate		= rs.getTimestamp("itr_requirement_due_date");
			this.itrAppnFootnoteInd			= rs.getInt("itr_appn_footnote_ind");
			this.itrConditionType			= rs.getString("itr_condition_type");
			this.itrConditionRule			= cwSQL.getClobValue(rs, "itr_condition_rule");
			this.itrPositiveIatId			= rs.getLong("itr_positive_iat_id");
			this.itrNegativeIatId			= rs.getLong("itr_negative_iat_id");
			this.itrProcExecuteTimestamp	= rs.getTimestamp("itr_proc_execute_timestamp");
			this.itr_update_timestamp = rs.getTimestamp("itr_update_timestamp");
			this.itr_update_usr_id = rs.getString("itr_update_usr_id");
        } 
		rs.close();
        stmt.close();
        return;
	}

	/*
    *Get a record from table, which the item is a run
    *Pre-define itrItmId, itrOrder
    @param con Connection to database
    @param parentItmId parent item id
    */
	public void getRun(Connection con, long parentItmId) throws SQLException, cwSysMessage {
	    String SQL_GET_RUN = OuterJoinSqlStatements.getRunRequirement();
		PreparedStatement stmt = con.prepareStatement(SQL_GET_RUN);
		int index = 1;
		stmt.setLong(index++, this.itrItmId);
		stmt.setLong(index++, parentItmId);
		stmt.setLong(index++, this.itrOrder);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
			this.itrRequirementType			= rs.getString("itr_requirement_type");
			this.itrRequirementSubtype		= rs.getString("itr_requirement_subtype");
			this.itrRequirementRestriction	= rs.getString("itr_requirement_restriction");
			this.itrRequirementDueDate		= rs.getTimestamp("itr_requirement_due_date");
			this.itrAppnFootnoteInd			= rs.getInt("itr_appn_footnote_ind");
			this.itrConditionType			= rs.getString("itr_condition_type");
			this.itrConditionRule			= cwSQL.getClobValue(rs, "itr_condition_rule");
			this.itrPositiveIatId			= rs.getLong("itr_positive_iat_id");
			this.itrNegativeIatId			= rs.getLong("itr_negative_iat_id");
			this.itrProcExecuteTimestamp	= rs.getTimestamp("itr_proc_execute_timestamp");
			this.itr_update_timestamp = rs.getTimestamp("itr_update_timestamp");
			this.itr_update_usr_id = rs.getString("itr_update_usr_id");
        } 
		rs.close();
        stmt.close();
        return;
	}
	
	
	/*
    *Get a record from table
    *Pre-define itrItmId
    */
	public ResultSet getByItmId(Connection con) throws SQLException { 
		String SQL_GET_BY_ITM_ID = OuterJoinSqlStatements.getItemRequirementByItemId();
		PreparedStatement stmt = con.prepareStatement(SQL_GET_BY_ITM_ID);
		int index = 1;
		stmt.setLong(index++, this.itrItmId);

        ResultSet rs = stmt.executeQuery();
        return rs;
	}
	
	/*
    *Get a record from table, which this.itrItmId is a run
    *Pre-define itrItmId
    @param con Connection to database
    @param parentItmId parent item id 
    */
	public ResultSet getByRunItmId(Connection con, long parentItmId) throws SQLException {
		String SQL_GET_BY_RUN_ID = OuterJoinSqlStatements.getRunRequirementByItemId();
		PreparedStatement stmt = con.prepareStatement(SQL_GET_BY_RUN_ID);
		int index = 1;
		stmt.setLong(index++, this.itrItmId);
        stmt.setLong(index++, parentItmId);
        ResultSet rs = stmt.executeQuery();
        return rs;
	}
	
	/*
    *Get a max_order for a user-input itm_id 
    *Pre-define itrItmId
    */
	public long getMaxOrder(Connection con) throws SQLException, cwSysMessage { 
	    long maxOrder = -1;
		PreparedStatement stmt = con.prepareStatement(SQL_GET_BY_MAX_ORDER);
		int index = 1;
		stmt.setLong(index++, this.itrItmId);

        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            maxOrder = rs.getLong("max_itr_order");
        }
        rs.close();
        stmt.close();
        return maxOrder;
	}

	/*
    *Insert a record into table
    *Pre-define all fields in the table
    */
    public void ins(Connection con) throws SQLException, cwSysMessage {
		PreparedStatement stmt = null;
		try {
		    StringBuffer tempSql = new StringBuffer();
		    tempSql.append(SQL_INS).append(cwSQL.getClobNull()).append(SQL_INS_ext_1);
		    stmt = con.prepareStatement(tempSql.toString());
		    int index = 1;
            this.itrOrder = getMaxOrder(con) + 1;
		    stmt.setLong(index++, this.itrItmId);
		    stmt.setLong(index++, this.itrOrder);
		    stmt.setString(index++, this.itrRequirementType);
		    stmt.setString(index++, this.itrRequirementSubtype);
		    stmt.setString(index++, this.itrRequirementRestriction);
		    //stmt.setTimestamp(index++, this.itrRequirementDueDate);
		    stmt.setInt(index++, this.itrAppnFootnoteInd);
		    stmt.setString(index++, this.itrConditionType);
		    //stmt.setString(index++, this.itrConditionRule);
		    if(this.itrPositiveIatId > 0) {
		        stmt.setLong(index++, this.itrPositiveIatId);
		    } else {
		        stmt.setNull(index++, java.sql.Types.INTEGER);
		    }
		    if(this.itrNegativeIatId > 0) {
		        stmt.setLong(index++, this.itrNegativeIatId);
		    } else {
		        stmt.setNull(index++, java.sql.Types.INTEGER);
		    }
		    stmt.setTimestamp(index++, this.itrProcExecuteTimestamp);
            stmt.setTimestamp(index++,cwSQL.getTime(con));
            stmt.setString(index++,this.itr_create_usr_id);
            stmt.setTimestamp(index++,cwSQL.getTime(con));
            stmt.setString(index++,this.itr_update_usr_id);
            
            String tableName = "aeItemRequirement";
            String columnName[] = {"itr_condition_rule"};
            String columnValue[] = {this.itrConditionRule};
            StringBuffer condition = new StringBuffer();
            int rc = stmt.executeUpdate();
		    if (rc != 1) {
			    throw new SQLException("Failed to insert record into the aeItemRequirement:" + condition.toString());
            }
            condition.append("itr_itm_id = ").append(this.itrItmId).append(" AND itr_order = ").append(this.itrOrder);
            rc = cwSQL.updateClobFields(con, tableName, columnName, columnValue, condition.toString());
		    if (rc != 1) {
			    throw new SQLException("Failed to update itr_condition_rule to a newly inserted record of aeItemRequirement");
			}
        } finally {
		    if(stmt!=null) stmt.close();
		}
		//insert due dates
		if(this.itrRequirementDueDate != null) {
		    DbItemReqDueDate ird = new DbItemReqDueDate();
		    ird.irdItrItmId = this.itrItmId;
		    ird.irdItrOrder = this.itrOrder;
		    ird.irdChildItmId = this.itrItmId;
		    ird.irdRequirementDueDate = this.itrRequirementDueDate;
		    ird.ins(con);
		}
		return;
	}
    
    /*
    *Update the record by itm_id and order
    *Pre-define itrItmId, itrOrder
    */
    public void upd(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
		    stmt = con.prepareStatement(SQL_UPD);
		    int index = 1;
		    stmt.setString(index++, this.itrRequirementType);
		    stmt.setString(index++, this.itrRequirementSubtype);
		    stmt.setString(index++, this.itrRequirementRestriction);
		    //stmt.setTimestamp(index++, this.itrRequirementDueDate);
		    stmt.setInt(index++, this.itrAppnFootnoteInd);
		    stmt.setString(index++, this.itrConditionType);
		    //stmt.setString(index++, this.itrConditionRule);
		    if(this.itrPositiveIatId > 0) {
		        stmt.setLong(index++, this.itrPositiveIatId);
		    } else {
		        stmt.setNull(index++, java.sql.Types.INTEGER);
		    }
		    if(this.itrNegativeIatId > 0) {
		        stmt.setLong(index++, this.itrNegativeIatId);
		    } else {
		        stmt.setNull(index++, java.sql.Types.INTEGER);
		    }
		    stmt.setTimestamp(index++, this.itrProcExecuteTimestamp);
		    stmt.setTimestamp(index++,cwSQL.getTime(con));
		    stmt.setString(index++,this.itr_update_usr_id);
		    
		    stmt.setLong(index++, this.itrItmId);
		    stmt.setLong(index++, this.itrOrder);
            
		    int rc = stmt.executeUpdate();
		    if (rc != 1) {
			    throw new SQLException("Failed to update record into aeItemRequirement");
            }
            String tableName = "aeItemRequirement";
            String columnName[] = {"itr_condition_rule"};
            String columnValue[] = {this.itrConditionRule};
            StringBuffer condition = new StringBuffer();
            condition.append("itr_itm_id = ").append(this.itrItmId).append(" AND itr_order = ").append(this.itrOrder);
            rc = cwSQL.updateClobFields(con, tableName, columnName, columnValue, condition.toString());
		    if (rc != 1) {
			    throw new SQLException("Failed to update itr_condition_rule into aeItemRequirement:" + condition.toString());
			}
		} finally {
		    if(stmt!=null) stmt.close();
		}
		// update due date
		if(this.itrRequirementDueDate != null) {
		    DbItemReqDueDate ird = new DbItemReqDueDate();
		    ird.irdItrItmId = this.itrItmId;
		    ird.irdItrOrder = this.itrOrder;
		    ird.irdChildItmId = this.itrItmId;
		    ird.irdRequirementDueDate = this.itrRequirementDueDate;
		    ird.upd(con);
		}
		return;
	}

    /*
    *Update the execute statement specified by itm_id, order, 
    *Pre-define itrItmId, itrOrder
    */
    public void updExecStatus(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SQL_UPD_EXEC_STATUS);
		int index = 1;
		stmt.setTimestamp(index++, this.itrProcExecuteTimestamp);
		stmt.setLong(index++, this.itrItmId);
		stmt.setLong(index++, this.itrOrder);

		stmt.executeUpdate();
		stmt.close();
		return;
	}

    /*
    *Update the record by itm_id and order
    *Pre-define itrItmId, itrOrder
    */
    public void updOrder(Connection con, long newItrOrder) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SQL_UPD_ORDER);
		int index = 1;
		stmt.setLong(index++, newItrOrder);
		stmt.setLong(index++, this.itrItmId);
		stmt.setLong(index++, this.itrOrder);

		stmt.executeUpdate();
		stmt.close();
		this.itrOrder = newItrOrder;
		
		return;
	}

    /*
    *Del the record by itm_id and order
    *Pre-define itrItmId, itrOrder
    */
    public void del(Connection con) throws SQLException {
        int index = 1;
        PreparedStatement stmt = null;
        //delete due date
        DbItemReqDueDate.delByRequirement(con, this.itrItmId, this.itrOrder);
		try {
		    stmt = con.prepareStatement(SQL_DEL);
		    stmt.setLong(index++, this.itrItmId);
		    stmt.setLong(index++, this.itrOrder);
		    stmt.executeUpdate();
        } finally {
		    stmt.close();
		}
		return;
	}

    /*
    *Del the record by itm_id and order
    *Pre-define itrItmId
    */
    public void delByItmId(Connection con) throws SQLException {
        int index = 1;
        PreparedStatement stmt = null;        
        //delete due date
        DbItemReqDueDate.delByItem(con, this.itrItmId);
        
        try {
		    stmt = con.prepareStatement(SQL_DEL_BY_ITM_ID);
		    stmt.setLong(index++, this.itrItmId);
		    stmt.executeUpdate();
        } finally {
		    if(stmt!=null) stmt.close();
		}
		return;
	}
	
 
       //add by Tim
    public static final String upd_proc_exec_timestamp = "Update AeItemRequirement "
         +   " Set itr_proc_execute_timestamp = ? "
         +   " Where itr_itm_id = ? and itr_order = ? ";
             
    /* DbItemRequirement.java
    Mark the requirement as proccess and update the timestmap
    @param itr_itm_id
    @param itr_order
    @param upd_time when the requirement processed
    */
    public static void updProcExceTimestamp(Connection con, long itr_itm_id, long itr_order, Timestamp upd_time) throws SQLException {
        
        try{
            Vector itemVec = new Vector();
            PreparedStatement stmt = con.prepareStatement(upd_proc_exec_timestamp);
            //stmt.setBoolean(1, true);
            stmt.setTimestamp(1, upd_time);
            stmt.setLong(2, itr_itm_id);
            stmt.setLong(3, itr_order);
            //System.out.println("upd_proc_exec_timestamp" + upd_proc_exec_timestamp);
            //System.out.println("upd_time" + upd_time);
            //System.out.println("itr_itm_id" + itr_itm_id);
            //System.out.println("itr_order" + itr_order);
        } catch (SQLException  e){
              CommonLog.error(e.getMessage(),e);
            
        }
       
    }
	
}
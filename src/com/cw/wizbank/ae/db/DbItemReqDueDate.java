package com.cw.wizbank.ae.db;

import java.sql.*;

public class DbItemReqDueDate {
	public long irdItrItmId;
	public long irdItrOrder;
    public long irdChildItmId;
	public Timestamp irdRequirementDueDate;

    private static final String SQL_INS_IRD = " INSERT INTO aeItemReqDueDate " 
                                            + " (ird_itr_itm_id, ird_itr_order "
                                            + " ,ird_child_itm_id, ird_requirement_due_date) "
                                            + " VALUES "
                                            + " (?, ?, ?, ?) ";

    private static final String SQL_UPD_IRD = " UPDATE aeItemReqDueDate "
                                            + " SET ird_requirement_due_date = ? "
                                            + " WHERE ird_itr_itm_id = ? "
                                            + " AND ird_itr_order = ? "
                                            + " AND ird_child_itm_id = ? ";

    private static final String DEL_IRD_BY_REQ = 
        " DELETE FROM aeItemReqDueDate " +
        " WHERE ird_itr_itm_id = ? " +
        " AND ird_itr_order = ? ";

    private static final String DEL_IRD_BY_ITM = 
        " DELETE FROM aeItemReqDueDate " +
        " WHERE ird_itr_itm_id = ? ";


    /**
    Save this object to database.
    It will try to update database first, if no row is affected, 
    it inserts new record to database
    @param con Connection to database
    */
    public void save(Connection con) throws SQLException {
        
        if(upd(con) == 0) {
            ins(con);
        }
        return;
    }

    /**
    Insert this object to database
    @param con Connection to database
    */
    public void ins(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_INS_IRD);
            stmt.setLong(1, this.irdItrItmId);
            stmt.setLong(2, this.irdItrOrder);
            stmt.setLong(3, this.irdChildItmId);
            stmt.setTimestamp(4, this.irdRequirementDueDate);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }
    
    /**
    update this object's attribute to database
    @param con connection to database
    @return number of rows affected
    */
    public int upd(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        int count = 0;
        try {
            stmt = con.prepareStatement(SQL_UPD_IRD);
            stmt.setTimestamp(1, this.irdRequirementDueDate);
            stmt.setLong(2, this.irdItrItmId);
            stmt.setLong(3, this.irdItrOrder);
            stmt.setLong(4, this.irdChildItmId);
            count = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return count;
    }

    /**
    Delete due date records of a requirement
    @param con Connection to database
    @param irdItrItmId requirement item id
    @param irdItrOrder requirement order
    */
    public static void delByRequirement(Connection con, long irdItrItmId, long irdItrOrder) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(DEL_IRD_BY_REQ);
            stmt.setLong(1, irdItrItmId);
            stmt.setLong(2, irdItrOrder);
            int i = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    /**
    Delete due date records of an item
    @param con Connection to database
    @param irdItrItmId requirement item id
    */
    public static void delByItem(Connection con, long irdItrItmId) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(DEL_IRD_BY_ITM);
            stmt.setLong(1, irdItrItmId);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

}
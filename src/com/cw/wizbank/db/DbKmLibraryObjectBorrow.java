package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

/**
A database class to represent table kmLibraryObjectBorrow
*/
public class DbKmLibraryObjectBorrow {
    public static final String TRANSACTION_LOG_TYPE = "KMLIB_BORROW";
    public static final String STATUS_CHECKIN = "CHECKIN";
    public static final String STATUS_CHECKOUT = "CHECKOUT";
    public static final String STATUS_BORROW = "BORROW";
    public static final String STATUS_RESERVE = "RESERVE";
    public static final String STATUS_CANCEL = "CANCEL";
    public static final int LATEST_IND_TRUE = 1;
    public static final int LATEST_IND_FALSE = 0;

    public long	lob_id;
    public long lob_lio_bob_nod_id;
    public long lob_usr_ent_id;
	public String lob_status;
	public long lob_loc_id;
	public int lob_renew_no;
	public Timestamp lob_due_timestamp;
	public String lob_create_usr_id;
	public Timestamp lob_create_timestamp;
	public String lob_update_usr_id;
	public Timestamp lob_update_timestamp;
	public int lob_latest_ind;

    public DbKmObject object;
    public DbKmLibraryObjectCopy objectCopy;

    private static final String SQL_GET = " SELECT lob_id, lob_lio_bob_nod_id, lob_usr_ent_id, "
                                        + " lob_status, lob_loc_id, lob_renew_no, lob_create_usr_id, lob_due_timestamp, "
                                        + " lob_create_timestamp, lob_update_usr_id, lob_update_timestamp, lob_latest_ind "
                                        + " FROM kmLibraryObjectBorrow " 
                                        + " WHERE lob_id = ? AND lob_lio_bob_nod_id = ? ";

    private static final String SQL_GET_BY_CALL_COPY = " SELECT lob_id, lob_lio_bob_nod_id, lob_usr_ent_id, "
                                        + " lob_status, lob_loc_id, lob_renew_no, lob_create_usr_id, lob_due_timestamp, "
                                        + " lob_create_timestamp, lob_update_usr_id, lob_update_timestamp, lob_latest_ind "
                                        + " FROM kmLibraryObjectBorrow " 
                                        + " WHERE lob_lio_bob_nod_id = ? "
                                        + " AND lob_loc_id = ? "
                                        + " AND lob_latest_ind = ? ";

    private static final String SQL_GET_TO_CHECKOUT_ITEM = " SELECT lob_id, lob_lio_bob_nod_id, lob_usr_ent_id, "
                                        + " lob_status, lob_loc_id, lob_renew_no, lob_create_usr_id, lob_due_timestamp, "
                                        + " lob_create_timestamp, lob_update_usr_id, lob_update_timestamp, lob_latest_ind "
                                        + " FROM kmLibraryObjectBorrow " 
                                        + " WHERE lob_lio_bob_nod_id = ? "
                                        + " AND lob_usr_ent_id = ? "
                                        + " AND lob_status IN (?, ?) "
                                        + " AND lob_latest_ind = ? ";
                                        
    private static final String SQL_GET_BY_USER_CALL_ID = " SELECT lob_id, lob_lio_bob_nod_id, lob_usr_ent_id, "
                                        + " lob_status, lob_loc_id, lob_renew_no, lob_create_usr_id, lob_due_timestamp, "
                                        + " lob_create_timestamp, lob_update_usr_id, lob_update_timestamp, lob_latest_ind "
                                        + " FROM kmLibraryObjectBorrow " 
                                        + " WHERE lob_lio_bob_nod_id = ? "
                                        + " AND lob_usr_ent_id = ? "
                                        + " AND lob_latest_ind = ? ";
                                        
    private static final String SQL_INS = " INSERT INTO kmLibraryObjectBorrow "
									   + " (lob_lio_bob_nod_id, lob_usr_ent_id, lob_status, lob_loc_id, lob_renew_no, lob_due_timestamp, "
									   + "  lob_create_usr_id, lob_create_timestamp, lob_update_usr_id, lob_update_timestamp, lob_latest_ind) "
									   + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    private static final String SQL_UPD = " UPDATE kmLibraryObjectBorrow "
									   + " SET lob_usr_ent_id = ?, lob_status = ?, lob_loc_id = ?, lob_renew_no = ?,  "
									   + " lob_due_timestamp = ?, lob_update_usr_id = ?, lob_update_timestamp = ?, lob_latest_ind = ? "
									   + " WHERE lob_id = ? AND lob_lio_bob_nod_id = ? ";

    private static final String SQL_UPD_NOT_LATEST = " UPDATE kmLibraryObjectBorrow "
									   + " SET lob_update_usr_id = ?, lob_update_timestamp = ?, lob_latest_ind = ? "
									   + " WHERE lob_id = ? AND lob_lio_bob_nod_id = ? ";

    private static final String SQL_DEL = " DELETE FROM kmLibraryObjectBorrow "
                                        + " WHERE lob_id = ? "
                                        + " AND lob_lio_bob_nod_id = ? ";
    
    private static final String SQL_GET_BY_NODE_ID = " Select lob_usr_ent_id, lob_status, lob_create_timestamp "    
                                                   + " From KmLibraryObjectBorrow "
                                                   + " Where lob_lio_bob_nod_id = ? And lob_latest_ind = ? ";

    private static final String SQL_COPY_ON_LOAN = " SELECT count(*) cnt "
                                                 + " FROM kmLibraryObjectBorrow "
                                                 + " WHERE lob_loc_id = ? and lob_latest_ind = ? ";
    
    private static final String SQL_ITEM_OUTSTANDING_BORROW = " SELECT count(*) cnt "
                                                            + " FROM KmLibraryObjectBorrow "
                                                            + " WHERE lob_lio_bob_nod_id = ? and lob_latest_ind = ? ";

    private static final String SQL_IS_ITEM_RESERVE = " SELECT lob_lio_bob_nod_id "
                                        + " FROM kmlibraryobjectborrow "
                                        + " WHERE lob_lio_bob_nod_id = ? "
                                        + " AND lob_status = ? "
                                        + " AND lob_latest_ind = ? ";
                                        
    private static final String SQL_GET_LIBRARY_BORROW_STATUS_COUNT = " SELECT COUNT(lob_id) cnt "
                                                       + " FROM kmLibraryObjectBorrow "
                                                       + " WHERE lob_lio_bob_nod_id = ? "
                                                       + " AND lob_latest_ind = ? "
                                                       + " AND lob_status = ? ";
                                                       
    private static final String SQL_GET_BY_USER = " SELECT lob_id, lob_lio_bob_nod_id, lob_status, lob_due_timestamp "
                                                + " FROM kmLibraryObjectBorrow "
                                                + " WHERE lob_usr_ent_id = ? " 
                                                + " AND lob_latest_ind = ? ";
                                        
    private static final String SQL_GET_STATUS_BY_USER = " SELECT lob_id, lob_lio_bob_nod_id, lob_renew_no, lob_status "
                                                       + " FROM kmLibraryObjectBorrow "
                                                       + " WHERE lob_usr_ent_id = ? "
                                                       + " AND lob_latest_ind = ? "
                                                       + " AND lob_lio_bob_nod_id IN ";
    
    public DbKmLibraryObjectBorrow() {
	    lob_loc_id = -1;
        lob_renew_no = -1;
	    lob_due_timestamp = null;
    }
    
    public void get(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET);
            stmt.setLong(index++, this.lob_id);
            stmt.setLong(index++, this.lob_lio_bob_nod_id);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                this.lob_usr_ent_id = rs.getLong("lob_usr_ent_id");
	            this.lob_status = rs.getString("lob_status");
	            this.lob_loc_id = rs.getLong("lob_loc_id");
	            this.lob_renew_no = rs.getInt("lob_renew_no");
	            this.lob_due_timestamp = rs.getTimestamp("lob_due_timestamp");
	            this.lob_create_usr_id = rs.getString("lob_create_usr_id");
	            this.lob_create_timestamp = rs.getTimestamp("lob_create_timestamp");
	            this.lob_update_usr_id = rs.getString("lob_update_usr_id");
	            this.lob_update_timestamp = rs.getTimestamp("lob_update_timestamp");
	            this.lob_latest_ind = rs.getInt("lob_latest_ind");
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
    
    public void getByCallCopyId(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_BY_CALL_COPY);
            stmt.setLong(index++, this.lob_lio_bob_nod_id);
	        stmt.setLong(index++, this.lob_loc_id);
	        stmt.setInt(index++, LATEST_IND_TRUE);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                this.lob_id = rs.getLong("lob_id");
                this.lob_usr_ent_id = rs.getLong("lob_usr_ent_id");
	            this.lob_status = rs.getString("lob_status");
	            this.lob_loc_id = rs.getLong("lob_loc_id");
	            this.lob_renew_no = rs.getInt("lob_renew_no");
	            this.lob_due_timestamp = rs.getTimestamp("lob_due_timestamp");
	            this.lob_create_usr_id = rs.getString("lob_create_usr_id");
	            this.lob_create_timestamp = rs.getTimestamp("lob_create_timestamp");
	            this.lob_update_usr_id = rs.getString("lob_update_usr_id");
	            this.lob_update_timestamp = rs.getTimestamp("lob_update_timestamp");
	            this.lob_latest_ind = rs.getInt("lob_latest_ind");
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }

    public void getToCheckoutItem(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_TO_CHECKOUT_ITEM);
            stmt.setLong(index++, this.lob_lio_bob_nod_id);
	        stmt.setLong(index++, this.lob_usr_ent_id);
            stmt.setString(index++, STATUS_BORROW);
            stmt.setString(index++, STATUS_RESERVE);
	        stmt.setInt(index++, LATEST_IND_TRUE);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                this.lob_id = rs.getLong("lob_id");
                this.lob_usr_ent_id = rs.getLong("lob_usr_ent_id");
	            this.lob_status = rs.getString("lob_status");
	            this.lob_loc_id = rs.getLong("lob_loc_id");
	            this.lob_renew_no = rs.getInt("lob_renew_no");
	            this.lob_due_timestamp = rs.getTimestamp("lob_due_timestamp");
	            this.lob_create_usr_id = rs.getString("lob_create_usr_id");
	            this.lob_create_timestamp = rs.getTimestamp("lob_create_timestamp");
	            this.lob_update_usr_id = rs.getString("lob_update_usr_id");
	            this.lob_update_timestamp = rs.getTimestamp("lob_update_timestamp");
	            this.lob_latest_ind = rs.getInt("lob_latest_ind");
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }

    public long ins(Connection con) throws SQLException, cwSysMessage {
        PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(SQL_INS, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setLong(index++, this.lob_lio_bob_nod_id);
        stmt.setLong(index++, this.lob_usr_ent_id);
        stmt.setString(index++, this.lob_status);
        if(this.lob_loc_id == -1) {
            stmt.setNull(index++, java.sql.Types.INTEGER);
        } else {
            stmt.setLong(index++, this.lob_loc_id);
        }
        if(this.lob_renew_no == -1) {
            stmt.setNull(index++, java.sql.Types.INTEGER);
        } else {
            stmt.setLong(index++, this.lob_renew_no);
        }
        if(this.lob_due_timestamp == null) {
            stmt.setNull(index++, java.sql.Types.TIMESTAMP);
        } else {
            stmt.setTimestamp(index++, this.lob_due_timestamp);
        }
        stmt.setString(index++, this.lob_create_usr_id);
        stmt.setTimestamp(index++, this.lob_create_timestamp);
        stmt.setString(index++, this.lob_update_usr_id);
        stmt.setTimestamp(index++, this.lob_update_timestamp);
        stmt.setInt(index++, this.lob_latest_ind);

        stmt.executeUpdate();
        this.lob_id = cwSQL.getAutoId(con, stmt, "kmLibraryObjectBorrow", "lob_id");
        stmt.close();
        
        return this.lob_id;
    }
    
    public void upd(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_UPD);
            stmt.setLong(index++, this.lob_usr_ent_id);
            stmt.setString(index++, this.lob_status);
            if(this.lob_loc_id == -1) {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            } else {
                stmt.setLong(index++, this.lob_loc_id);
            }
            if(this.lob_renew_no == -1) {
                stmt.setNull(index++, java.sql.Types.INTEGER);
            } else {
                stmt.setLong(index++, this.lob_renew_no);
            }
            if(this.lob_due_timestamp == null) {
                stmt.setNull(index++, java.sql.Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(index++, this.lob_due_timestamp);
            }
            stmt.setString(index++, this.lob_update_usr_id);
            stmt.setTimestamp(index++, this.lob_update_timestamp);
            stmt.setInt(index++, this.lob_latest_ind);
            stmt.setLong(index++, this.lob_id);
            stmt.setLong(index++, this.lob_lio_bob_nod_id);

            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
    
    public void updToNotLatest(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
			this.lob_latest_ind = LATEST_IND_FALSE;
            stmt = con.prepareStatement(SQL_UPD_NOT_LATEST);
            stmt.setString(index++, this.lob_update_usr_id);
            stmt.setTimestamp(index++, this.lob_update_timestamp);
            stmt.setInt(index++, this.lob_latest_ind);
            stmt.setLong(index++, this.lob_id);
            stmt.setLong(index++, this.lob_lio_bob_nod_id);

            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
    
    public void del(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_DEL);
            stmt.setLong(index++, this.lob_id);
            stmt.setLong(index++, this.lob_lio_bob_nod_id);

            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }

    public void logTransaction(Connection con, String userId, int type) throws SQLException {
        String[] logKeys = new String[3];
        String[] logKeyValue = new String[3];
        logKeys[0] = "lob_id";
        logKeys[1] = "lob_lio_bob_nod_id";
        logKeys[2] = "lob_loc_id";
        logKeyValue[0] = this.lob_id + "";
        logKeyValue[1] = this.lob_lio_bob_nod_id + "";
        logKeyValue[2] = this.lob_loc_id + "";

        try {
            cwTransactionLog.logTransaction(con, TRANSACTION_LOG_TYPE, userId, null, "kmLibraryObjectBorrow", logKeys, logKeyValue, type);
        } catch(cwException e) {
        	CommonLog.error("Error in performing transaction log...");
        }
    }

    public static ResultSet getByNodeIdNStatus(Connection con, long nod_id, String[] status, String sort_by, String order_by)
        throws SQLException{
            
            String SQL = SQL_GET_BY_NODE_ID;
            if( status != null && status.length > 0 ){
                SQL += " AND lob_status IN ( ? ";
                for(int i=1; i<status.length; i++)
                    SQL += " , ? ";
                SQL += " ) ";
            }
            if( sort_by != null && sort_by.length() > 0 ){
                SQL += " ORDER BY " + sort_by;
                if( order_by != null && order_by.length() > 0 )
                    SQL += " " + order_by;
            }
            int index = 1;
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, nod_id);
            stmt.setLong(index++, 1);
            if( status != null && status.length > 0 ){
                for(int i=0; i<status.length; i++)
                    stmt.setString(index++, status[i]);
            }
            return stmt.executeQuery();
        }

    public static ResultSet getByUserCallIdStatus(Connection con, long usrEntId, long callId, String[] status)
        throws SQLException{
            
            String SQL = SQL_GET_BY_USER_CALL_ID;
            if( status != null && status.length > 0 ){
                SQL += " AND lob_status IN ( ? ";
                for(int i=1; i<status.length; i++)
                    SQL += " , ? ";
                SQL += " ) ";
            }

            int index = 1;
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(index++, callId);
            stmt.setLong(index++, usrEntId);
            stmt.setLong(index++, LATEST_IND_TRUE);
            if( status != null && status.length > 0 ){
                for(int i=0; i<status.length; i++)
                    stmt.setString(index++, status[i]);
            }
            return stmt.executeQuery();
        }

    public boolean onLoan(Connection con) throws SQLException {
        boolean result = true;
        
        PreparedStatement stmt = con.prepareStatement(SQL_COPY_ON_LOAN);
        stmt.setLong(1, lob_loc_id);
        stmt.setLong(2, LATEST_IND_TRUE);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            if (rs.getInt("cnt") == 0) {
                result = false;
            }
        }
 
        stmt.close();
        
        return result;        
    }

    public boolean hasOutstandingBorrow(Connection con) throws SQLException {
        boolean result = true;
        
        PreparedStatement stmt = con.prepareStatement(SQL_ITEM_OUTSTANDING_BORROW);
        stmt.setLong(1, lob_lio_bob_nod_id);
        stmt.setLong(2, LATEST_IND_TRUE);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            if (rs.getInt("cnt") == 0) {
                result = false;
            }
        }
 
        stmt.close();
        
        return result;
    }
    
    public boolean isItemReserve(Connection con) throws SQLException {
        boolean result = false;
        int index = 1;
        
        PreparedStatement stmt = con.prepareStatement(SQL_IS_ITEM_RESERVE);
        stmt.setLong(index++, lob_lio_bob_nod_id);
        stmt.setString(index++, STATUS_RESERVE);
        stmt.setLong(index++, LATEST_IND_TRUE);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            result = true;
        }
 
        stmt.close();
        
        return result;
    }
    
    public static String geteLibRuleAsXML(int borrowLimit, int overdueLimit, int renewLimit, int dueDay){
        
        return "<rule borrow_limit=\"" + borrowLimit + "\" overdue_limit=\"" + overdueLimit + "\" renew_limit=\"" + renewLimit + "\" due_day=\"" + dueDay + "\"/>";
    } 
    
    public static long getLibraryBorrowStatusCount(Connection con, long nod_id, String status)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_LIBRARY_BORROW_STATUS_COUNT);
            int index = 1;
            stmt.setLong(index++, nod_id);
            stmt.setBoolean(index++, true);
            stmt.setString(index++, status);
            ResultSet rs = stmt.executeQuery();
            long count = 0;
            if(rs.next())
                count = rs.getLong("cnt");
            stmt.close();
            return count;
        }
    
    public String getUserSummaryAsXML(Connection con, long usr_ent_id)
        throws SQLException {
            PreparedStatement stmt = con.prepareStatement(SQL_GET_BY_USER);
            stmt.setLong(1, usr_ent_id);
            stmt.setBoolean(2, true);
            ResultSet rs = stmt.executeQuery();
            int borrowed = 0;
            int overdued = 0;
            int reserved = 0;
            Timestamp cur_time = cwSQL.getTime(con);
            while(rs.next()){
                if( rs.getString("lob_status").equalsIgnoreCase(STATUS_BORROW) || 
                    rs.getString("lob_status").equalsIgnoreCase(STATUS_CHECKOUT)) {
                    borrowed++;
                    if( rs.getString("lob_status").equalsIgnoreCase(STATUS_CHECKOUT) && 
                        cur_time.after(rs.getTimestamp("lob_due_timestamp")) ) {
                            overdued++;
                    }
                } else if( rs.getString("lob_status").equalsIgnoreCase(STATUS_RESERVE) ) {
                    reserved++;
                }
            }
            stmt.close();
            String xml = "<summary "
                       + " borrowed=\"" + borrowed + "\" "
                       + " overdue=\"" + overdued + "\" "
                       + " reserved=\"" + reserved + "\" "
                       + "/>";
            return xml;
        }    
        

    public static boolean isValidToBorrow(Connection con, long nod_id, long usr_ent_id, long borrow_limit, long overdue_limit)
        throws SQLException {
            PreparedStatement stmt = con.prepareStatement(SQL_GET_BY_USER);
            stmt.setLong(1, usr_ent_id);
            stmt.setBoolean(2, true);
            //stmt.setLong(3, nod_id);
            ResultSet rs = stmt.executeQuery();
            int borrowed = 0;
            int overdued = 0;
            Timestamp cur_time = cwSQL.getTime(con);
            while(rs.next()){
                if( rs.getString("lob_status").equalsIgnoreCase(STATUS_BORROW) &&
                    rs.getLong("lob_lio_bob_nod_id") != nod_id ) {
                        borrowed++;
                }else if( rs.getString("lob_status").equalsIgnoreCase(STATUS_CHECKOUT) ) {
                    borrowed++;
                    if( cur_time.after(rs.getTimestamp("lob_due_timestamp")) ) {
                        overdued++;
                    }
                }                            
            }
            stmt.close();
            boolean flag = true;
            if( borrowed >= borrow_limit )
                flag = false;
            else if( overdued >= overdue_limit )
                flag = false;
            return flag;
        }    
        

    
    public Hashtable getItemUserStatus(Connection con, long usr_ent_id, Vector vec)
        throws SQLException {
            if( vec == null || vec.isEmpty() )
                return new Hashtable();
            PreparedStatement stmt = con.prepareStatement(SQL_GET_STATUS_BY_USER + cwUtils.vector2list(vec));
            stmt.setLong(1, usr_ent_id);
            stmt.setBoolean(2, true);
            Hashtable hashtable = new Hashtable();
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                DbKmLibraryObjectBorrow obj = new DbKmLibraryObjectBorrow();
                obj.lob_lio_bob_nod_id = rs.getLong("lob_lio_bob_nod_id");
                obj.lob_id = rs.getLong("lob_id");
                obj.lob_status = rs.getString("lob_status");
                obj.lob_renew_no = rs.getInt("lob_renew_no");
                hashtable.put(new Long(obj.lob_lio_bob_nod_id), obj);
            }
            stmt.close();
            return hashtable;
        }        
}
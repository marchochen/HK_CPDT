package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.DbKmObject;
import com.cw.wizbank.db.DbKmLibraryObject;
import com.cw.wizbank.db.DbKmLibraryObjectBorrow;
import com.cw.wizbank.db.DbKmLibraryObjectCopy;
import com.cw.wizbank.db.DbKmNode;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;

public class ViewKmLibraryObject {
    public class itemDetails {
        public long bobNodId;
        public String callNumber;
        public String title;
        public String type;
        public String author;
        public long numRequest;
        public long numInStock;
        public long copyPick;
        public long numCopy;
        public Vector lob_status;
        public Vector lob_id;
        public Vector lob_renew_no;
    }
    public itemDetails itm;
    public DbKmLibraryObject dbKmLibObj;
    public DbKmLibraryObjectBorrow dbKmLibObjBorrow;
    public DbKmLibraryObjectCopy dbKmLibObjCopy;
    
    private static final String SQL_GET_CHECKIN_ITEM = " SELECT lio_bob_nod_id, loc_id "
                                        + " FROM kmBaseObject, kmLibraryObject, kmLibraryObjectCopy "
                                        + " WHERE bob_nod_id = lio_bob_nod_id "
                                        + " AND lio_bob_nod_id = loc_lio_bob_nod_id "
                                        + " AND bob_code = ? "
                                        + " AND loc_copy = ? "
                                        + " AND loc_delete_usr_id IS null "
                                        + " AND loc_delete_timestamp IS null "
                                        + " AND bob_delete_usr_id IS null " 
                                        + " AND bob_delete_timestamp IS null ";
                                        
    private static final String SQL_IS_VALID_CALL_NUM = " SELECT bob_nod_id "
                                        + " FROM kmBaseObject "
                                        + " WHERE bob_code = ? ";
    
    private static final String SQL_IS_CHECKOUT = " SELECT bob_nod_id "
                                        + " FROM kmBaseObject, kmLibraryObject, "
                                        + " kmLibraryObjectCopy, kmLibraryObjectBorrow "
                                        + " WHERE bob_nod_id = lio_bob_nod_id "
                                        + " AND lio_bob_nod_id = loc_lio_bob_nod_id "
                                        + " AND lio_bob_nod_id = lob_lio_bob_nod_id "
                                        + " AND lob_loc_id = loc_id "
                                        + " AND bob_code = ? "
                                        + " AND loc_copy = ? "
                                        + " AND lob_status = ? "
                                        + " AND lob_latest_ind = ? "
                                        + " AND loc_delete_usr_id IS null "
                                        + " AND loc_delete_timestamp IS null "
                                        + " AND bob_delete_usr_id IS null " 
                                        + " AND bob_delete_timestamp IS null ";

    private static final String SQL_GET_CHECKIN_USER = " SELECT usr_ent_id, usr_display_bil "
                                        + " FROM kmBaseObject, kmLibraryObjectBorrow, regUser, kmLibraryObjectCopy "
                                        + " WHERE bob_nod_id = lob_lio_bob_nod_id "
                                        + " AND loc_lio_bob_nod_id = lob_lio_bob_nod_id "
                                        + " AND lob_usr_ent_id = usr_ent_id "
                                        + " AND lob_loc_id = loc_id "
                                        + " AND bob_code = ? "
                                        + " AND loc_copy = ? "
                                        + " AND lob_status = ? "
                                        + " AND lob_latest_ind = ? "
                                        + " AND loc_delete_usr_id IS null "
                                        + " AND loc_delete_timestamp IS null "
                                        + " AND bob_delete_usr_id IS null " 
                                        + " AND bob_delete_timestamp IS null ";
    
    private static final String SQL_GET_AVAILABLE_COPY = " SELECT loc_lio_bob_nod_id, loc_id, loc_copy, loc_desc "
                                        + " FROM kmlibraryobjectcopy "
                                        + " WHERE loc_id NOT IN "
                                        + " (SELECT lob_loc_id FROM kmlibraryobjectborrow WHERE lob_status = ? AND lob_latest_ind = ?) "
                                        + " AND loc_delete_usr_id IS null "
                                        + " AND loc_delete_timestamp IS null "
					                    + " AND loc_lio_bob_nod_id = ? ";
	
	private static final String SQL_GET_ITEM_COPY_INFO = " SELECT lob_usr_ent_id, bob_nod_id, "
	                                    + " obj_title, bob_code, loc_id, loc_copy, "
	                                    + " loc_desc, lob_due_timestamp, lob_renew_no "
	                                    + " FROM kmObject, kmBaseObject, kmLibraryObjectCopy, kmLibraryObjectBorrow "
	                                    + " WHERE bob_nod_id = loc_lio_bob_nod_id "
	                                    + " AND obj_bob_nod_id = bob_nod_id "
	                                    + " AND lob_lio_bob_nod_id = bob_nod_id "
	                                    + " AND lob_loc_id = loc_id "
	                                    + " AND bob_nod_id = ? " 
	                                    + " AND loc_id = ? "
	                                    + " AND lob_status = ? "
	                                    + " AND lob_latest_ind = ? "
                                        + " AND loc_delete_usr_id IS null "
                                        + " AND loc_delete_timestamp IS null "
                                        + " AND bob_delete_usr_id IS null " 
                                        + " AND bob_delete_timestamp IS null ";

    private static final String SQL_GET_CHECKOUT_LIST_TAIL = " GROUP BY bob_nod_id, obj_title, bob_code, lio_num_copy_in_stock, obj_type ";
	                                    

    private static final String SQL_GET_CHECKOUT_REQUEST_INFO = " SELECT lob_id, lob_lio_bob_nod_id, lob_usr_ent_id, "
                                        + " lob_status, lob_loc_id, lob_renew_no,  "
                                        + " lob_create_usr_id, lob_due_timestamp,lob_create_timestamp, "
                                        + " lob_update_usr_id, lob_update_timestamp "
                                        + " FROM kmlibraryobjectborrow "
                                        + " WHERE lob_status in (?, ?) "
                                        + " AND lob_latest_ind = ? ";
    private static final String SQL_GET_CHECKOUT_REQUEST_INFO_TAIL = " AND lob_lio_bob_nod_id = ? "
                                        + " ORDER BY lob_update_timestamp ";


    private static final String SQL_GET_AVAIL_ITEM_COPY = " Select loc_id, loc_copy "
                                                        + " From kmLibraryObjectCopy, kmLibraryObjectBorrow "
                                                        + " Where loc_id = lob_loc_id "
                                                        + " AND loc_delete_usr_id IS null "
                                                        + " AND loc_delete_timestamp IS null "
                                                        + " And loc_lio_bob_nod_id = lob_lio_bob_nod_id "
                                                        + " AND loc_lio_bob_nod_id = ? "
                                                        + " And lob_status <> ? ";

    private static final String SQL_GET_COPY_INFO = " SELECT "
                                        + " bob_nod_id, obj_title, bob_code, "
                                        + " loc_id, loc_copy, loc_desc "
                                        + " FROM  kmBaseObject, kmObject, kmLibraryObjectcopy "
                                        + " WHERE obj_bob_nod_id = bob_nod_id "
                                        + " AND loc_lio_bob_nod_id = bob_nod_id "
                                        + " AND loc_id = ? ";

    private static final String SQL_GET_COPY_HISTORY = " SELECT lob_id, lob_status, "
                                        + " bob_nod_id, obj_title, bob_code, "
                                        + " loc_id, loc_copy, loc_desc, lob_renew_no, lob_due_timestamp, "
                                        + " processUser.usr_ent_id AS process_usr_ent_id, "
                                        + " lob_usr_ent_id AS usr_ent_id, "
                                        + " lob_create_timestamp as lob_update_timestamp, lob_latest_ind "
                                        + " FROM  kmBASeObject, kmObject, regUser ownUser, regUser processUser, "
                                        + " kmlibraryObjectborrow, kmLibraryObjectcopy "
                                        + " WHERE lob_lio_bob_nod_id = bob_nod_id "
                                        + " AND obj_bob_nod_id = bob_nod_id "
                                        + " AND lob_loc_id = loc_id "
                                        + " AND ownUser.usr_ent_id = lob_usr_ent_id "
                                        + " AND processUser.usr_id = lob_create_usr_id "
                                        + " AND lob_loc_id = ? "
                                        + " ORDER BY lob_update_timestamp ";

    private static final String SQL_GET_BORROW_INFO = " SELECT lob_id, lob_lio_bob_nod_id, lob_usr_ent_id, "
                                        + " lob_status, lob_loc_id, lob_renew_no, lob_create_usr_id, lob_due_timestamp, "
                                        + " lob_create_timestamp, lob_update_usr_id, lob_update_timestamp, lob_latest_ind "
                                        + " FROM kmLibraryObjectBorrow " 
                                        + " WHERE lob_lio_bob_nod_id = ? "
                                        + " AND lob_usr_ent_id = ? "
                                        + " AND lob_latest_ind = ? ";
                                        
    private static final String SQL_GET_OVERDUE_USER = " SELECT distinct lob_usr_ent_id "
                                        + " FROM kmlibraryobjectborrow, "
                                        + " ( "
                                        + " SELECT COUNT(lob_id) AS numOverdue, "
                                        + " lob_usr_ent_id AS overdueUser "
                                        + " FROM kmlibraryobjectborrow "
                                        + " WHERE lob_status = ? AND ? > lob_due_timestamp "
                                        + " AND lob_latest_ind = ? GROUP BY lob_usr_ent_id "
                                        + " ) tempQuery "
                                        + " WHERE numOverdue >= ? "
                                        + " AND overdueUser = lob_usr_ent_id ";

    private static final String SQL_GET_OVERBORROW_USER = " SELECT distinct lob_usr_ent_id "
                                        + " FROM kmlibraryobjectborrow, "
                                        + " ( "
                                        + " SELECT COUNT(lob_id) AS numBorrow, "
                                        + " lob_usr_ent_id AS borrowUser "
                                        + " FROM kmlibraryobjectborrow "
                                        + " WHERE lob_status in (?,?) "
                                        + " AND lob_latest_ind = ? GROUP BY lob_usr_ent_id "
                                        + " ) tempQuery "
                                        + " WHERE numBorrow >= ? "
                                        + " AND borrowUser = lob_usr_ent_id ";

    private static final String SQL_GET_OVERDUE = " SELECT obj_bob_nod_id, lob_usr_ent_id, lob_status, lob_loc_id, lob_renew_no, lob_due_timestamp, "
        + " lob_create_usr_id, lob_create_timestamp, lob_update_usr_id, lob_update_timestamp, loc_copy, obj_type, obj_title, obj_author, bob_nature, bob_code "
        + " FROM kmLibraryObjectBorrow, kmLibraryObjectCopy, kmObject, kmBaseObject, kmObjectType "
        + " WHERE lob_latest_ind = ? and lob_due_timestamp < ? "
        + " and lob_loc_id = loc_id "
        + " and lob_lio_bob_nod_id = obj_bob_nod_id and lob_lio_bob_nod_id = bob_nod_id "
        + " and obj_type = oty_code and oty_owner_ent_id = ? ";


    private static final String SQL_GET_NODE_ID_BY_CALL_NUM = " SELECT bob_nod_id, nod_parent_nod_id "
                                                            + " FROM kmBaseObject , kmObject , kmNode "
                                                            + " WHERE bob_code = ? AND bob_delete_timestamp IS NULL "
                                                            + " AND bob_nod_id = obj_bob_nod_id AND obj_publish_ind = ? "
                                                            + " AND bob_nod_id = nod_id ";

	private static final String SQL_GET_ITEM_INFO = " SELECT lob_usr_ent_id, bob_nod_id, "
	                                    + " obj_title, bob_code, loc_id, loc_copy, "
	                                    + " loc_desc, lob_due_timestamp, lob_renew_no "
	                                    + " FROM kmObject, kmBaseObject, kmLibraryObjectCopy, kmLibraryObjectBorrow "
	                                    + " WHERE bob_nod_id = loc_lio_bob_nod_id "
	                                    + " AND obj_bob_nod_id = bob_nod_id "
	                                    + " AND lob_lio_bob_nod_id = bob_nod_id "
	                                    + " AND lob_loc_id = loc_id "
	                                    + " AND bob_nod_id = ? " 
	                                    + " AND loc_id = ? "
	                                    + " AND lob_status = ? "
	                                    + " AND lob_latest_ind = ? ";

    private static final String SQL_GET_BORROWED_BY_ID = " Select lob_id, lob_lio_bob_nod_id, obj_title, obj_type, loc_copy, "
                                                       + " lob_due_timestamp, lob_renew_no, bob_code, "
                                                       + " usr_ent_id, usr_id, usr_ste_usr_id, usr_pwd, usr_display_bil, usr_email, usr_email_2 "
                                                       + " From kmLibraryObjectBorrow, kmLibraryObjectCopy, kmObject, kmBaseObject, RegUser "
                                                       + " Where lob_lio_bob_nod_id = loc_lio_bob_nod_id "
                                                       + " And bob_nod_id = lob_lio_bob_nod_id "
                                                       + " And lob_loc_id = loc_id And lob_lio_bob_nod_id = obj_bob_nod_id "
                                                       + " And usr_ent_id = lob_usr_ent_id And lob_id IN ";

    private static final String SQL_GET_BORROWED_BY_DUE = " Select lob_id, lob_due_timestamp "
                                                        + " From kmNode, kmLibraryObjectBorrow "
                                                        + " Where nod_owner_ent_id = ? "
                                                        + " And nod_id = lob_lio_bob_nod_id "
                                                        + " And lob_latest_ind = ? ";
    
    private static final String SQL_GET_OBJECT_TITLE_BY_CODE = " SELECT obj_title "
                                                             + " FROM kmBaseObject, kmObject "
                                                             + " WHERE bob_nod_id = obj_bob_nod_id "
                                                             + " AND bob_code = ? ";
    
    private static final String SQL_GET_CHILD_OBJECTS_AND_STATUS = " SELECT bob_nod_id, obj_type, "
                        + " bob_code, obj_title, obj_author, lio_num_copy_available, lio_num_copy "
                        + " FROM kmNode, kmBaseObject, kmObject, kmLink, kmLibraryObject "
                        + " WHERE lio_bob_nod_id = bob_nod_id "
                        + " AND lnk_target_nod_id = bob_nod_id "
                        + " AND lnk_nod_id = nod_id "
                        + " AND obj_bob_nod_id = bob_nod_id "
                        + " AND obj_publish_ind = ? "
                        + " AND bob_nature = ? "
                        + " AND nod_ancestor like ? ";

    private static final String SQL_GET_OBJECT_TITLE_BY_LOB_ID = " SELECT obj_title "
                                                               + " FROM kmObject, kmLibraryObjectBorrow "
                                                               + " WHERE lob_lio_bob_nod_id = obj_bob_nod_id "
                                                               + " AND lob_id = ? ";
    
    
    public void getCheckinItem(Connection con, String callNum, String copyNum) throws SQLException {
        PreparedStatement stmt = null;
        this.dbKmLibObj = new DbKmLibraryObject();
        this.dbKmLibObjBorrow = new DbKmLibraryObjectBorrow();
        
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_CHECKIN_ITEM);
            stmt.setString(index++, callNum);
            stmt.setString(index++, copyNum);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                this.dbKmLibObjBorrow.lob_lio_bob_nod_id = rs.getLong("lio_bob_nod_id");
                this.dbKmLibObjBorrow.lob_loc_id = rs.getLong("loc_id");
                this.dbKmLibObjBorrow.getByCallCopyId(con);

                this.dbKmLibObj.lio_bob_nod_id = rs.getLong("lio_bob_nod_id");
                this.dbKmLibObj.get(con);
            }
            rs.close();
            stmt.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
    
    public static String getCheckInUserXML(Connection con, String callNum, String copyNum) throws qdbException, cwException, SQLException {
        StringBuffer xmlBuffer = new StringBuffer();

        PreparedStatement stmt = null;
        try {
            long usr_ent_id = -1;
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_CHECKIN_USER);
            stmt.setString(index++, callNum);
            stmt.setString(index++, copyNum);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                usr_ent_id = rs.getLong("usr_ent_id");
            }
            rs.close();
            dbRegUser regUser = new dbRegUser();
            regUser.usr_ent_id = usr_ent_id;
            regUser.get(con);
            xmlBuffer = regUser.getUserShortXML(con, false, true, false, false);
        } finally {
            if(stmt!=null) stmt.close();
        }
        return xmlBuffer.toString();
    }
    
    public static boolean isValidLibraryObject(Connection con, String callNum, String copyNum) throws SQLException {
        PreparedStatement stmt = null;
        boolean retValue = false;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_CHECKIN_ITEM);
            stmt.setString(index++, callNum);
            stmt.setString(index++, copyNum);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                retValue = true;
            } else {
                retValue = false;
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return retValue;
    }
    
    public static boolean isValidCallNumber(Connection con, String callNum) throws SQLException {
        PreparedStatement stmt = null;
        boolean retValue = false;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_IS_VALID_CALL_NUM);
            stmt.setString(index++, callNum);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                retValue = true;
            } else {
                retValue = false;
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return retValue;
    }

    public static boolean isCheckOut(Connection con, String callNum, String copyNum) throws SQLException {
        PreparedStatement stmt = null;
        boolean retValue = false;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_IS_CHECKOUT);
            stmt.setString(index++, callNum);
            stmt.setString(index++, copyNum);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT); 
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                retValue = true;
            } else {
                retValue = false;
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return retValue;
    }

    public Vector getCheckoutList(Connection con, String orderSQL, Vector checkoutListAll, Vector checkoutListByPage, boolean scopeResult, int borrowLimit, int overdueLimit) throws SQLException {
        Vector checkoutList = new Vector();
        if (!scopeResult){
            checkoutListAll.clear();
        }
        
        StringBuffer sqlBuffer = new StringBuffer(OuterJoinSqlStatements.geteLibCheckOutListSQL());

        StringBuffer scopeSQL = new StringBuffer();
        if(scopeResult){
            scopeSQL = getScopeSQL(checkoutListByPage);
        }
        sqlBuffer.append(scopeSQL);

        sqlBuffer.append(SQL_GET_CHECKOUT_LIST_TAIL);
        if(orderSQL != null) {
            sqlBuffer.append(" ").append(orderSQL);
        }
        
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(sqlBuffer.toString());
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setTimestamp(index++, cwSQL.getTime(con));
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, overdueLimit);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, borrowLimit);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_RESERVE);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            ResultSet rs  = stmt.executeQuery();
            while(rs.next()) {
                itemDetails itm = new itemDetails();
                itm.bobNodId = rs.getLong("bob_nod_id");
                itm.title = rs.getString("title");
                itm.callNumber = rs.getString("call_number");
                itm.numRequest = rs.getLong("numRequest");
                itm.numInStock = rs.getLong("stock");
                itm.type = rs.getString("obj_type");
                itm.copyPick = Math.min(itm.numRequest, itm.numInStock);
                if (!scopeResult){
                    checkoutListAll.addElement(new Long(itm.bobNodId));
                }
                checkoutList.addElement(itm);
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return checkoutList;
    }
    
    public static ArrayList getAvailableCopy(Connection con, long callId) throws SQLException {
        PreparedStatement stmt = null;
        ArrayList avaliableCopies = new ArrayList();
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_AVAILABLE_COPY);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setLong(index++, callId);
            ResultSet rs  = stmt.executeQuery();
            while(rs.next()) {
                DbKmLibraryObjectCopy libObjCopy = new DbKmLibraryObjectCopy();
                libObjCopy.loc_lio_bob_nod_id = rs.getLong("loc_lio_bob_nod_id");
                libObjCopy.loc_id = rs.getLong("loc_id");
                libObjCopy.loc_copy = rs.getString("loc_copy");
                libObjCopy.loc_desc = cwSQL.getClobValue(rs, "loc_desc");
                
                avaliableCopies.add(libObjCopy);
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return avaliableCopies;
    }

    public static ArrayList getRequestInfo(Connection con, long callId, int borrowLimit, int overdueLimit) throws SQLException {
        PreparedStatement stmt = null;
        ArrayList requests = new ArrayList();
        StringBuffer sqlBuffer = new StringBuffer();
        try {
            sqlBuffer = new StringBuffer(SQL_GET_CHECKOUT_REQUEST_INFO);
            Vector validUserVector = getValidUser(con, callId, borrowLimit, overdueLimit);
            if(validUserVector.size() > 0) {
                sqlBuffer.append(" AND lob_usr_ent_id IN " + cwUtils.vector2list(validUserVector) + " ");
            } else {
                sqlBuffer.append(" AND lob_usr_ent_id = -1 ");
            }
            sqlBuffer.append(SQL_GET_CHECKOUT_REQUEST_INFO_TAIL);
            
			int index = 1;
            stmt = con.prepareStatement(sqlBuffer.toString());
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_RESERVE);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setLong(index++, callId);
            ResultSet rs  = stmt.executeQuery();
            while(rs.next()) {
                DbKmLibraryObjectBorrow libObjborrow = new DbKmLibraryObjectBorrow();
                libObjborrow.lob_id = rs.getLong("lob_id");
                libObjborrow.lob_usr_ent_id = rs.getLong("lob_usr_ent_id");
	            libObjborrow.lob_status = rs.getString("lob_status");
	            libObjborrow.lob_create_usr_id = rs.getString("lob_create_usr_id");
	            libObjborrow.lob_create_timestamp = rs.getTimestamp("lob_create_timestamp");
	            libObjborrow.lob_update_usr_id = rs.getString("lob_update_usr_id");
	            libObjborrow.lob_update_timestamp = rs.getTimestamp("lob_update_timestamp");

                requests.add(libObjborrow);
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return requests;
    }

    public static boolean isValidCheckoutUser(Connection con, long usrEntId, int borrowLimit, int overdueLimit) throws SQLException {
        boolean retValue = false;
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(OuterJoinSqlStatements.geteLibisValidCheckoutUserSQL());
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setTimestamp(index++, cwSQL.getTime(con));
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, overdueLimit);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, borrowLimit);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setLong(index++, usrEntId);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                retValue = true;
            } else {
                retValue = false;
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return retValue;
    }

    public void getCheckoutItem(Connection con, long usrEntId, long callId) throws SQLException {
        PreparedStatement stmt = null;
        this.dbKmLibObj = new DbKmLibraryObject();
        this.dbKmLibObjBorrow = new DbKmLibraryObjectBorrow();
        
        this.dbKmLibObjBorrow.lob_lio_bob_nod_id = callId;
        this.dbKmLibObjBorrow.lob_usr_ent_id = usrEntId;
        this.dbKmLibObjBorrow.getToCheckoutItem(con);

        this.dbKmLibObj.lio_bob_nod_id = callId;
        this.dbKmLibObj.get(con);
    }
    
    public String getCheckoutItemXML(Connection con, long callId, long copyId, int borrowLimit, int overdueLimit) throws qdbException, cwException, SQLException {
        StringBuffer xmlBuffer = new StringBuffer();

        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_ITEM_COPY_INFO);
            stmt.setLong(index++, callId);
            stmt.setLong(index++, copyId);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                xmlBuffer.append("<check_out_node id=\"" + callId + "\">");

                ArrayList availablecopies = getAvailableCopy(con, callId);
                ArrayList requests = getRequestInfo(con, callId, borrowLimit, overdueLimit);
                if(requests.size()>0 && availablecopies.size()>0) {
                    xmlBuffer.append("<available_copies>");
                    xmlBuffer.append("<number>" + availablecopies.size() + "</number>");
                    for(Iterator it=availablecopies.iterator(); it.hasNext(); ) {
                        DbKmLibraryObjectCopy dbKmLibObjCopy = (DbKmLibraryObjectCopy) it.next();
                        xmlBuffer.append("<available_copy id=\"" + dbKmLibObjCopy.loc_id + "\">");
                        xmlBuffer.append(cwUtils.esc4XML(dbKmLibObjCopy.loc_copy));
                        xmlBuffer.append("</available_copy>");
                    }
                    xmlBuffer.append("</available_copies>");
                }
                
                xmlBuffer.append("<title>" + cwUtils.esc4XML(rs.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(rs.getString("bob_code")) + "</call_number>");
                xmlBuffer.append("<copy id=\"" + copyId + "\">");
                xmlBuffer.append(cwUtils.esc4XML(rs.getString("loc_copy")));
                xmlBuffer.append("</copy>");
                xmlBuffer.append("<due_timestamp>" + rs.getTimestamp("lob_due_timestamp") + "</due_timestamp>");
                dbRegUser regUser = new dbRegUser();
                regUser.usr_ent_id = rs.getLong("lob_usr_ent_id");
                regUser.get(con);
                xmlBuffer.append(regUser.getUserShortXML(con, false, true, false, false));
                xmlBuffer.append("</check_out_node>");
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return xmlBuffer.toString();
    }

    public static ResultSet getUserHistory(Connection con, long usrEntId) throws cwException, SQLException {
        PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(OuterJoinSqlStatements.geteLibUserHistorySQL());
        stmt.setLong(index++, usrEntId);
        return stmt.executeQuery();
    }

    public static ResultSet getCopyHistory(Connection con, long copyId) throws cwException, SQLException {
        PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(SQL_GET_COPY_HISTORY);
        stmt.setLong(index++, copyId);
        return stmt.executeQuery();
    }

    public static String getCopyInfo(Connection con, long copyId) throws cwException, SQLException {
        StringBuffer xmlBuffer = new StringBuffer();
        PreparedStatement stmt = null;
        try {
		    int index = 1;
            stmt = con.prepareStatement(SQL_GET_COPY_INFO);
            stmt.setLong(index++, copyId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                xmlBuffer.append("<item id=\"" + rs.getLong("bob_nod_id") + "\">");
                xmlBuffer.append("<title>" + cwUtils.esc4XML(rs.getString("obj_title")) + "</title>");
                xmlBuffer.append("<call_number>" + cwUtils.esc4XML(rs.getString("bob_code")) + "</call_number>");
                xmlBuffer.append("<copy id=\"" + rs.getLong("loc_id") + "\">");
                xmlBuffer.append("<copy_number>" + cwUtils.esc4XML(rs.getString("loc_copy")) + "</copy_number>");
                xmlBuffer.append("<desc>" + cwUtils.esc4XML(cwSQL.getClobValue(rs, "loc_desc")) + "</desc>");
                xmlBuffer.append("</copy>");
                xmlBuffer.append("</item>");
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return xmlBuffer.toString();
    }

// private method
    private static Vector getValidUser(Connection con, long callId, int borrowLimit, int overdueLimit) throws SQLException {
        PreparedStatement stmt = null;
        Vector validUserVector = new Vector();
        try {
			int index = 1;
            stmt = con.prepareStatement(OuterJoinSqlStatements.geteLibisValidUserSQL());

            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setTimestamp(index++, cwSQL.getTime(con));
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, overdueLimit);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, borrowLimit);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setLong(index++, callId);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            ResultSet rs  = stmt.executeQuery();
            while(rs.next()) {
                validUserVector.addElement(new Long(rs.getLong("lob_usr_ent_id")));
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return validUserVector;
    }

    private StringBuffer getScopeSQL(Vector checkoutListByPage){
        StringBuffer scopeSQL = new StringBuffer(60);
        if (checkoutListByPage != null && checkoutListByPage.size() > 0){
            scopeSQL.append(" AND bob_nod_id IN  (");

            for (int i=0 ; i< checkoutListByPage.size() ;i++) {
                if (i!=0) {
                    scopeSQL.append(",");
                }
                scopeSQL.append(((Long) checkoutListByPage.elementAt(i)).longValue());
            }
            scopeSQL.append(" ) ");
        }

        return scopeSQL;
    }

    public static ResultSet getUserRec(Connection con, long usr_ent_id, String[] status, String sort_by, String order_by)
        throws SQLException, cwException{
            String SQL = OuterJoinSqlStatements.geteLibUserRecSQL();
            if( status != null && status.length > 0 ){
                SQL += " AND lob_status IN ( ? ";
                for(int i=1; i<status.length; i++)
                    SQL += " ,? ";
                SQL += " ) ";
            }
            if( sort_by != null && sort_by.length() > 0 ) {
                SQL += " ORDER BY " + sort_by;
                if( order_by != null && order_by.length() > 0 )
                    SQL += " " + order_by;
                SQL += " , lob_create_timestamp ASC ";  
            }

            
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, usr_ent_id);
            stmt.setLong(index++, 1);            
            if (status != null){
                for(int i=0; i<status.length; i++)
                    stmt.setString(index++, status[i]);
            }
            return stmt.executeQuery();
        }


    public static ResultSet getItemRec(Connection con, long nod_id, String[] status, String sort_by, String order_by)
        throws SQLException, cwException {
            String SQL = OuterJoinSqlStatements.geteLibItemRecSQL();
            if( status != null && status.length > 0 ){
                SQL += " AND lob_status IN ( ? ";
                for(int i=1; i<status.length; i++)
                    SQL += " ,? ";
                SQL += " ) ";
            }
            if( sort_by != null && sort_by.length() > 0 ) {
                SQL += " ORDER BY " + sort_by;
                if( order_by != null && order_by.length() > 0 )
                    SQL += " " + order_by;
            }
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, nod_id);
            stmt.setLong(index++, 1);
            if (status != null){
                for(int i=0; i<status.length; i++)
                    stmt.setString(index++, status[i]);
            }
            return stmt.executeQuery();
            
        }

    public static ResultSet getAvailItemCopy(Connection con, long nod_id, String sort_by, String order_by)
        throws SQLException, cwException {
            String SQL = SQL_GET_AVAIL_ITEM_COPY;
            if( sort_by != null && sort_by.length() > 0 ) {
                SQL += " ORDER BY " + sort_by;
                if( order_by != null && order_by.length() > 0 )
                    SQL += " " + order_by;
            }
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, nod_id);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            return stmt.executeQuery();
        }

    public static Vector getOverdueUser(Connection con, int overdueLimit) throws cwException, SQLException {
        PreparedStatement stmt = null;
        Vector validUserVector = new Vector();
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_OVERDUE_USER);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setTimestamp(index++, cwSQL.getTime(con));
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, overdueLimit);
            ResultSet rs  = stmt.executeQuery();
            while(rs.next()) {
                validUserVector.addElement(new Long(rs.getLong("lob_usr_ent_id")));
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return validUserVector;
    }

    public static Vector getOverBorrowUser(Connection con, int borrowLimit) throws cwException, SQLException {
        PreparedStatement stmt = null;
        Vector validUserVector = new Vector();
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET_OVERBORROW_USER);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
            stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_BORROW);
            stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
            stmt.setInt(index++, borrowLimit);
            ResultSet rs  = stmt.executeQuery();
            while(rs.next()) {
                validUserVector.addElement(new Long(rs.getLong("lob_usr_ent_id")));
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return validUserVector;
    }

    /**
    get item and its copy information by provide the call_id and copy_id
    @param callId call_id
    @param copyId copy_id
    */
    public static ResultSet getItemInfo(Connection con, long callId, long copyId) throws SQLException, cwException {
	    PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(SQL_GET_ITEM_COPY_INFO);
        stmt.setLong(index++, callId);
        stmt.setLong(index++, copyId);
        stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_CHECKOUT);
        stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
        return stmt.executeQuery();
    }
    
    /**
    get item and its reserve information by provide the call_id 
    @param callId call_id
    */
    public static ResultSet getItemReserveInfo(Connection con, long callId) throws SQLException {
	    PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(OuterJoinSqlStatements.geteLibItemReserveInfoSQL());
        stmt.setString(index++, DbKmLibraryObjectBorrow.STATUS_RESERVE);
        stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
        stmt.setLong(index++, callId);
        return stmt.executeQuery();
    }
    
    /**
    get the current borrow information for a specified user and call_id
    */
    public static ResultSet getBorrowInfo(Connection con, long usrEntId, long callId) throws SQLException {
        PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(SQL_GET_BORROW_INFO);
        stmt.setLong(index++, callId);
	    stmt.setLong(index++, usrEntId);
        stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
	    return stmt.executeQuery();
    }


    public Vector getLibChildObjectsNStatus(Connection con, long nod_id, long usr_ent_id, String sort_by, String order_by)
        throws SQLException {
            PreparedStatement stmt = null;
		    int index = 1;
//		    String SQL = OuterJoinSqlStatements.geteLibChildObjectsNStatus(con);
            String SQL = SQL_GET_CHILD_OBJECTS_AND_STATUS;
            
            if( sort_by != null && sort_by.length() > 0 ) {
                SQL += " ORDER BY " + sort_by;
                if( order_by != null && order_by.length() > 0 )
                    SQL += " " + order_by;
            }
            stmt = con.prepareStatement(SQL);
//            stmt.setLong(index++, usr_ent_id);
//            stmt.setLong(index++, 1);
            stmt.setLong(index++, 1);

            stmt.setString(index++, "LIBRARY");
            stmt.setString(index++, "% " + nod_id + " ");
            ResultSet rs = stmt.executeQuery();
            Vector childVec = new Vector();


            while(rs.next()){
                ViewKmLibraryObject vLibObj = new ViewKmLibraryObject();
                vLibObj.itm = new itemDetails();
                vLibObj.dbKmLibObj = new DbKmLibraryObject();
                vLibObj.dbKmLibObjBorrow = new DbKmLibraryObjectBorrow();
                
                vLibObj.itm.bobNodId = rs.getLong("bob_nod_id");
                vLibObj.itm.type = rs.getString("obj_type");
                vLibObj.itm.title = rs.getString("obj_title");
                vLibObj.itm.callNumber = rs.getString("bob_code");
                vLibObj.itm.numCopy = rs.getLong("lio_num_copy");
                vLibObj.itm.author = rs.getString("obj_author");
                
//                vLibObj.dbKmLibObjBorrow.lob_id = rs.getLong("lob_id");
//                vLibObj.dbKmLibObjBorrow.lob_renew_no = rs.getInt("lob_renew_no");
//                vLibObj.dbKmLibObjBorrow.lob_status = rs.getInt("lob_status");

                ResultSet borrowRS = getBorrowInfo(con, usr_ent_id, rs.getLong("bob_nod_id"));
                vLibObj.itm.lob_status = new Vector();
                vLibObj.itm.lob_id = new Vector();
                vLibObj.itm.lob_renew_no = new Vector();
                while(borrowRS.next()) {
                    vLibObj.itm.lob_status.addElement(borrowRS.getString("lob_status"));
                    vLibObj.itm.lob_id.addElement(new Long(borrowRS.getLong("lob_id")));
                    vLibObj.itm.lob_renew_no.addElement(new Long(borrowRS.getInt("lob_renew_no")));
                }

                vLibObj.dbKmLibObj.lio_num_copy_available = rs.getLong("lio_num_copy_available");                
                
                childVec.addElement(vLibObj);
            }
            stmt.close();
            return childVec;
        }

    public static Vector getOverdueInfo(Connection con, long owner_ent_id, Timestamp ts, String sortCol, String sortOrder) throws SQLException {        
        Vector v = new Vector();
        PreparedStatement stmt = null;
		int index = 1;
        StringBuffer SQLBuf = new StringBuffer(256);
        
        SQLBuf.append(SQL_GET_OVERDUE);
        SQLBuf.append(" ORDER BY ").append(sortCol).append(" ").append(sortOrder);

        stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setInt(index++, DbKmLibraryObjectBorrow.LATEST_IND_TRUE);
        stmt.setTimestamp(index++, ts);
        stmt.setLong(index++, owner_ent_id);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
            borrow.object = new DbKmObject();
            borrow.objectCopy = new DbKmLibraryObjectCopy();

            borrow.lob_lio_bob_nod_id = rs.getLong("obj_bob_nod_id");
            borrow.lob_usr_ent_id = rs.getLong("lob_usr_ent_id");
            borrow.lob_status = rs.getString("lob_status");
            borrow.lob_loc_id = rs.getLong("lob_loc_id");
            borrow.lob_renew_no = rs.getInt("lob_renew_no");
            borrow.lob_due_timestamp = rs.getTimestamp("lob_due_timestamp"); 
            borrow.lob_create_usr_id = rs.getString("lob_create_usr_id");
            borrow.lob_create_timestamp = rs.getTimestamp("lob_create_timestamp");
            borrow.lob_update_usr_id = rs.getString("lob_update_usr_id");
            borrow.lob_update_timestamp = rs.getTimestamp("lob_update_timestamp");
            borrow.objectCopy.loc_copy = rs.getString("loc_copy");
            borrow.object.obj_type = rs.getString("obj_type");
            borrow.object.obj_title = rs.getString("obj_title");
            borrow.object.obj_author = rs.getString("obj_author");
            borrow.object.obj_nature = rs.getString("bob_nature");
            borrow.object.obj_code = rs.getString("bob_code");
                        
            v.addElement(borrow);
        }
        
        stmt.close();
        
        return v;
    }        
    
    
    /**
    * Get object node by call number
    * Only get nod_id and nod_parent_nod_id now.
    */
    public static DbKmNode getNode(Connection con, String call_num)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_NODE_ID_BY_CALL_NUM);
            stmt.setString(1, call_num);
            stmt.setLong(2, 1);
            ResultSet rs = stmt.executeQuery();
            DbKmNode dbNode = new DbKmNode();
            if(rs.next()){
                dbNode.nod_id = rs.getLong("bob_nod_id");
                dbNode.nod_parent_nod_id = rs.getLong("nod_parent_nod_id");
            }
            stmt.close();
            return dbNode;
        }
    
    /**
    get the borrow information for a specified lob_id
    */
    public static ResultSet getBorrowItemInfo(Connection con, long lobId) throws SQLException {
        PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(OuterJoinSqlStatements.geteLibItemInfoSQL());
        stmt.setLong(index++, lobId);
	    return stmt.executeQuery();

    }

    public static ResultSet getBorrowedObjectById(Connection con, Vector vec)
        throws SQLException {
            String SQL = SQL_GET_BORROWED_BY_ID + cwUtils.vector2list(vec);
            PreparedStatement stmt = con.prepareStatement(SQL);
            return stmt.executeQuery();
        }
        
    public static ResultSet getLibraryObjectBorrowByDuedate(Connection con, long site_id, Timestamp start, Timestamp end)
        throws SQLException {
            
            String SQL = SQL_GET_BORROWED_BY_DUE;
            if( start != null )
                SQL += " And lob_due_timestamp >= ? ";
            if( end != null )
                SQL += " And lob_due_timestamp <= ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            
            int index = 1;
            stmt.setLong(index++, site_id);
            stmt.setBoolean(index++, true);
            if( start != null )
                stmt.setTimestamp(index++, start);
            if( end != null )
                stmt.setTimestamp(index++, end);
            return stmt.executeQuery();
            
        }
        
        
    public static String getObjectTitleByCode(Connection con, String bob_code)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_OBJECT_TITLE_BY_CODE);
            stmt.setString(1, bob_code);
            ResultSet rs = stmt.executeQuery();
            String title = null;
            if(rs.next())
                title = rs.getString("obj_title");
            else
                title = "";
            stmt.close();
            return title;
            
        }
        
        
    public static String getObjectTitleByLobId(Connection con, long lob_id)
        throws SQLException{
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_OBJECT_TITLE_BY_LOB_ID);
            stmt.setLong(1, lob_id);
            ResultSet rs = stmt.executeQuery();
            String title = "";
            if(rs.next())
                title = rs.getString("obj_title");
            stmt.close();
            return title;
        }
        
}
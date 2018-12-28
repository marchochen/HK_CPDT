package com.cw.wizbank.db;

import java.util.Vector;

import java.sql.*;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

public class DbTrackingHistory{
    public static final String TKH_TYPE_QUICK_REFERENCE = "QR";
    public static final String TKH_TYPE_APPLICATION = "APP";
    
    public static long TKH_ID_NO_COURSE = 0;
    public static long TKH_ID_NOT_FOUND = -1;
    public static long TKH_ID_UNDEFINED = -2;
    
    public long tkh_id;
    public String tkh_type;
    public long tkh_usr_ent_id;
    public long tkh_cos_res_id;
    public Timestamp tkh_create_timestamp;
    
    /*
    public static long ins(Connection con, long type, long usr_ent_id, long cos_res_id, long app_id){
        dbTrackingHistory dbTkh = new dbTrackingHistory();
        
        Timestamp curTime = con.getTime(con);
        dbTkh.tkh_type = TKH_TYPE_APPLICATION;
        dbTkh.tkh_usr_ent_id = usr_ent_id;
        dbTkh.tkh_cos_res_id = cos_res_id;
        dbTkh.tkh_create_timestamp = curTime;
        dbTkh.ins(con);
        
        aeApplication.updTkh(con, app_id, dbTkh.tkh_id);
        return dbTkh.tkh_id;
        
    }
    */
    public static final String sql_ins_tracking_history = "INSERT INTO TrackingHistory (tkh_type, tkh_usr_ent_id, tkh_cos_res_id, tkh_create_timestamp) VALUES (?, ?, ?, ?)";
    public void ins(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(sql_ins_tracking_history, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, tkh_type);
        stmt.setLong(2, tkh_usr_ent_id);
        stmt.setLong(3, tkh_cos_res_id);
        if (tkh_create_timestamp == null) {
            tkh_create_timestamp = cwSQL.getTime(con);
        }
        stmt.setTimestamp(4, tkh_create_timestamp);
        stmt.executeUpdate();
        tkh_id = cwSQL.getAutoId(con, stmt, "TrackingHistory", "tkh_id");
        stmt.close();
        if (tkh_id==TKH_ID_NOT_FOUND){
            throw new SQLException("Fail to insert TrackingHistory");
        }
                
    }

    public static final String sql_del_tracking_history = "DELETE From TrackingHistory Where tkh_id = ? ";
    public static void del(Connection con, long tkhId) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(sql_del_tracking_history);
        stmt.setLong(1, tkhId);
        stmt.executeUpdate();
        stmt.close();
    }    
    
    public static final String sql_get_tracking_history = "SELECT tkh_type, tkh_usr_ent_id, tkh_cos_res_id, tkh_create_timestamp FROM TrackingHistory where tkh_id = ?";
    public void get(Connection con) throws SQLException, cwException {
        PreparedStatement stmt = con.prepareStatement(sql_get_tracking_history);
        stmt.setLong(1, tkh_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            tkh_type = rs.getString("tkh_type");
            tkh_usr_ent_id = rs.getLong("tkh_usr_ent_id");
            tkh_cos_res_id = rs.getLong("tkh_cos_res_id");
            tkh_create_timestamp = rs.getTimestamp("tkh_create_timestamp");
        }else {
            throw new cwException("Failed to get the tracking history, id=" + tkh_id);
        }
        stmt.close();
    }    
    
    // get all tkh_ids 
    public Vector getTkhIds(Connection con) throws SQLException {
        Vector ids = new Vector();
        StringBuffer SQLBuf = new StringBuffer();

        SQLBuf.append(" Select tkh_id From TrackingHistory WHERE 1=1 ");
        if (tkh_type != null){
            SQLBuf.append(" AND tkh_type = ? ");
        }
        if (tkh_usr_ent_id != 0){
            SQLBuf.append(" AND tkh_usr_ent_id = ? ");
        }
        if (tkh_cos_res_id != 0){
            SQLBuf.append(" AND tkh_cos_res_id = ? ");
        }
        SQLBuf.append("Order by tkh_create_timestamp ASC ");

        PreparedStatement stmt = con.prepareStatement(new String(SQLBuf));
        int col=1;
        if (tkh_type != null){
            stmt.setString(col++, tkh_type);
        }
        if (tkh_usr_ent_id != 0){
            stmt.setLong(col++, tkh_usr_ent_id);
        }
        if (tkh_cos_res_id != 0){
            stmt.setLong(col++, tkh_cos_res_id);
        }
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            ids.addElement(new Long(rs.getLong("tkh_id")));
        }   
        return ids;
    }
    
    private long getMaxTkhId(Connection con) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer();

        SQLBuf.append(" Select tkh_id From TrackingHistory WHERE 1=1 ");
        if (tkh_type != null){
            SQLBuf.append(" AND tkh_type = ? ");
        }
        if (tkh_usr_ent_id != 0){
            SQLBuf.append(" AND tkh_usr_ent_id = ? ");
        }
        if (tkh_cos_res_id != 0){
            SQLBuf.append(" AND tkh_cos_res_id = ? ");
        }
        if (tkh_create_timestamp!=null){
            SQLBuf.append(" AND tkh_create_timestamp = ? ");
        }

        PreparedStatement stmt = con.prepareStatement(new String(SQLBuf));
        int col=1;
        if (tkh_type != null){
            stmt.setString(col++, tkh_type);
        }
        if (tkh_usr_ent_id != 0){
            stmt.setLong(col++, tkh_usr_ent_id);
        }
        if (tkh_cos_res_id != 0){
            stmt.setLong(col++, tkh_cos_res_id);
        }
        if (tkh_create_timestamp!=null){
            stmt.setTimestamp(col++, tkh_create_timestamp);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        int index = 1;
        while(rs.next()){
            long temp_tkh_id = rs.getLong(1);
            if (tkh_id < temp_tkh_id || index == 1){       
                tkh_id = temp_tkh_id;
            }
            index++;
        }
        if(tkh_id == 0){
            tkh_id = TKH_ID_NOT_FOUND;
        }
        rs.close();
        stmt.close();
        return tkh_id;
    }

    public static long getAppTrackingIDByMod(Connection con, long modId, long usr_ent_id)
        throws SQLException, cwException {
            long cosId;
            try {
                cosId = dbModule.getCosId(con, modId);
            }catch( Exception e) {
                throw new cwException(e.getMessage());
            }
            long trackId = 0;
            if (cosId > 0) {
                trackId = getAppTrackingIDByCos(con, cosId, usr_ent_id);
            }
            return trackId;
    }
    

    /*
    If no tracking id is passed from frontend
    1. Get the maxiumn tracking id that is 'APP' type
    2. If no tracking id found, throw exception
    */
    public static long getAppTrackingIDByCos(Connection con, long cos_res_id, long usr_ent_id)
        throws SQLException, cwException {
    
            return getTrackingIDByCos(con, cos_res_id, usr_ent_id, TKH_TYPE_APPLICATION);
        
    }

    /*
    If no tracking id is passed from frontend
    1. Get the maxiumn tracking id that is 'QR' type
    2. If no tracking id found, throw exception
    */
    public static long getQRTrackingIDByCos(Connection con, long cos_res_id, long usr_ent_id)
        throws SQLException, cwException {
    
            return getTrackingIDByCos(con, cos_res_id, usr_ent_id, TKH_TYPE_QUICK_REFERENCE);
    }

    private static long getTrackingIDByCos(Connection con, long cos_res_id, long usr_ent_id, String type)
        throws SQLException, cwException {

            if (cos_res_id <= 0) {
                throw new cwException("Failed to get tracking id , invalid course id.");
            }
            
            DbTrackingHistory dbtkh = new DbTrackingHistory();
            dbtkh.tkh_cos_res_id = cos_res_id;
            dbtkh.tkh_usr_ent_id = usr_ent_id;
            dbtkh.tkh_type = type;
            long trackId = dbtkh.getMaxTkhId(con);

            return trackId;
        }
        
    // For backward compatiblity of assigment
    // Maintain the orignal file structure
    public static boolean isFirstAppnTracking(Connection con, long tkh_id)
        throws SQLException, cwException {
            
            boolean isFirst = true;
            DbTrackingHistory dbtkh = new DbTrackingHistory();
            dbtkh.tkh_id = tkh_id;
            dbtkh.get(con);
            dbtkh.tkh_type = TKH_TYPE_APPLICATION;
            // Get the records with the same type, usr_ent_id and cos_res_id
            Vector  idVec = dbtkh.getTkhIds(con);
            if (((Long) idVec.elementAt(0)).longValue() != tkh_id) {
                isFirst = false;
            }
            return isFirst;
        }            

    public static long getAttemptCntFromCos(Connection con, long cos_id, String type) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer();

        SQLBuf.append(" Select count(tkh_id) From TrackingHistory WHERE ");
        SQLBuf.append(" tkh_type = ? ");
        SQLBuf.append(" AND tkh_cos_res_id = ? ");

        PreparedStatement stmt = con.prepareStatement(new String(SQLBuf));
        stmt.setString(1, type);
        stmt.setLong(2, cos_id);

        ResultSet rs = stmt.executeQuery();
        long cnt=0;
        while (rs.next()){
            cnt = rs.getLong(1);
        }
        rs.close();
        stmt.close();
        return cnt;
    }        


    public static long getAttemptCntFromCos(Connection con, long cos_id, String type, Timestamp startDateTime, Timestamp endDateTime) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer();

        SQLBuf.append(" Select count(distinct tkh_id) From TrackingHistory WHERE ");
        SQLBuf.append(" tkh_type = ? ");
        SQLBuf.append(" AND tkh_cos_res_id = ? ");

//      no need to consider startDatetime in getting attempt count as end time is the only constraint
/*
        if(startDateTime != null) {
            SQLBuf.append(" AND tkh_create_timestamp > ? ");
        } 
*/
        if(endDateTime != null) {
            SQLBuf.append(" AND tkh_create_timestamp < ? ");
        }

        int index = 1;
        PreparedStatement stmt = con.prepareStatement(new String(SQLBuf));
        stmt.setString(index++, type);
        stmt.setLong(index++, cos_id);
        if(endDateTime != null) {
            stmt.setTimestamp(index++, endDateTime);
        }

        ResultSet rs = stmt.executeQuery();
        long cnt=0;
        while (rs.next()){
            cnt = rs.getLong(1);
        }   
        stmt.close();
        return cnt;
    }
    
    public static final String sql_get_tracking_history_count = "SELECT count(tkh_id) FROM TrackingHistory, ResourceContent WHERE " +
    						   " tkh_id = ? AND tkh_usr_ent_id = ? AND tkh_cos_res_id = ? AND rcn_res_id_content = ? AND tkh_type = ? ";
    public static long getAppTrackingIDByCos(Connection con, long tkh_id, long usr_ent_id, long cos_res_id, long mod_id)
    throws SQLException, cwException {
    	long count = 0;
    	int index = 1;
        PreparedStatement stmt = con.prepareStatement(sql_get_tracking_history_count);
        stmt.setLong(index++, tkh_id);
        stmt.setLong(index++, usr_ent_id);
        stmt.setLong(index++, cos_res_id);
        stmt.setLong(index++, mod_id);
        stmt.setString(index++, TKH_TYPE_APPLICATION);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
        	count = rs.getLong(1);
        }   
        stmt.close();
        return count;
    }
    
}
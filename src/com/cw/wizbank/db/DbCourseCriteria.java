package com.cw.wizbank.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.sql.*;

import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.util.cwSQL;

public class DbCourseCriteria{
    public static final String TYPE_COMPLETION = "COMPLETION";
    public static final String TYPE_NOTIFICATION = "NOTIFICATION";
    
    public static final String UPD_METHOD_MANUAL = "MANUAL";
    public static final String UPD_METHOD_AUTO = "AUTO";

    public long ccr_id;
    public long ccr_itm_id;
    public String ccr_type;
    public int ccr_pass_score;
    public float ccr_pass_score_f;
    public boolean ccr_pass_ind = true;
    public boolean ccr_all_cond_ind = true;
    public int ccr_attendance_rate;
    public String ccr_upd_method;
    public int ccr_duration;
    public Timestamp ccr_create_timestamp;
    public String ccr_create_usr_id;
    public Timestamp ccr_upd_timestamp;
    public String ccr_upd_usr_id;
    public String ccr_offline_condition;
    public long ccr_ccr_id_parent ;
    public int passCondCnt;
    public int allCondCnt;
    
    public float cov_progress;
    // kim changed
    //Terry modified added ccr_offline_condition, ccr_upd_timestamp
    public void get(Connection con) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_CourseCriteria);
            stmt.setLong(1, ccr_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                ccr_id = rs.getLong("ccr_id");
                ccr_itm_id = rs.getLong("ccr_itm_id");
                ccr_type = rs.getString("ccr_type");
                ccr_pass_score = rs.getInt("ccr_pass_score");
                ccr_pass_score_f = rs.getFloat("ccr_pass_score");
                ccr_pass_ind = rs.getBoolean("ccr_pass_ind");
                ccr_all_cond_ind = rs.getBoolean("ccr_all_cond_ind");                
                ccr_attendance_rate = rs.getInt("ccr_attendance_rate");
                ccr_duration = rs.getInt("ccr_duration");
                ccr_upd_method = rs.getString("ccr_upd_method");
                ccr_offline_condition = rs.getString("ccr_offline_condition");
                ccr_upd_timestamp = rs.getTimestamp("ccr_upd_timestamp");
                ccr_ccr_id_parent = rs.getLong("ccr_ccr_id_parent");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
/*
    public void getCcrIdByCosNType(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ccr_id_by_cos_n_type);
        stmt.setLong(1, ccr_cos_id);
        stmt.setString(2, ccr_type);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            ccr_id = rs.getLong("ccr_id");
        }
    }
    */
    // kim added
    public void getCcrIdByItmNType(Connection con) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_ccr_id_by_itm_n_type);
            stmt.setLong(1, ccr_itm_id);
            stmt.setString(2, ccr_type);

            rs = stmt.executeQuery();
            if (rs.next()) {
                ccr_id = rs.getLong("ccr_id");
            } else {
            	ccr_id = 0;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
 
    // kim changed
    public void ins(Connection con) throws SQLException{
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_ins_CourseCriteria, PreparedStatement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setLong(index++, ccr_itm_id);
            stmt.setString(index++, ccr_type);
            stmt.setString(index++, ccr_upd_method);
            stmt.setInt(index++, ccr_duration);
            stmt.setInt(index++, ccr_attendance_rate);
            stmt.setInt(index++, ccr_pass_score);
            stmt.setBoolean(index++, ccr_pass_ind);
            stmt.setBoolean(index++, ccr_all_cond_ind);
            stmt.setTimestamp(index++, ccr_create_timestamp);
            stmt.setString(index++, ccr_create_usr_id);
            stmt.setTimestamp(index++, ccr_upd_timestamp);
            stmt.setString(index++, ccr_upd_usr_id);
            stmt.setString(index++, ccr_offline_condition);
            if ( ccr_ccr_id_parent != 0){
    			stmt.setLong(index++, ccr_ccr_id_parent);
    		} else {
    			stmt.setNull(index++, Types.INTEGER);
    		}
            stmt.executeUpdate();

            ccr_id = cwSQL.getAutoId(con, stmt, "CourseCriteria", "ccr_id");
      
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    // kim changed
    public void upd(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_CourseCriteria);
        int index = 1;
        stmt.setString(index++, ccr_upd_method);
        stmt.setInt(index++, ccr_duration);
        stmt.setInt(index++, ccr_attendance_rate);
        stmt.setInt(index++, ccr_pass_score);
        stmt.setBoolean(index++, ccr_pass_ind);
        stmt.setBoolean(index++, ccr_all_cond_ind);
        stmt.setTimestamp(index++, ccr_upd_timestamp);
        stmt.setString(index++, ccr_upd_usr_id);
        stmt.setString(index++, ccr_offline_condition);
        stmt.setLong(index++, ccr_id);
        stmt.executeUpdate();
        stmt.close();

        return;
    }
    // kim not changed
    public void del(Connection con) throws SQLException{
        //delete the cmt
        DbCourseMeasurement.delByCcrId(con, ccr_id);
        // delete the cmr
        DbCourseModuleCriteria.delByCcrId(con, ccr_id);
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_CourseCriteria);
        stmt.setLong(1, ccr_id);
        stmt.setLong(2, ccr_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static Vector getCcrIdByItmId(Connection con, long itm_id) throws SQLException{
        Vector ccrIds = new Vector();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_ccr_id_by_itm);
            stmt.setLong(1, itm_id);

            rs = stmt.executeQuery();
            while (rs.next()) {
                ccrIds.addElement(new Long(rs.getLong("ccr_id")));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return ccrIds;
    }

    public void updCond(Connection con) throws SQLException{
        String sql = "update CourseCriteria set ccr_pass_ind = ? ,ccr_pass_score = ? ,ccr_attendance_rate = ? ,ccr_offline_condition = ?,ccr_upd_timestamp=?,ccr_upd_usr_id=? where ccr_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setBoolean(index++, ccr_pass_ind);
        stmt.setInt(index++, ccr_pass_score);
        if(ccr_attendance_rate == 0){
            stmt.setNull(index++, Types.INTEGER);
        }else{
            stmt.setInt(index++, ccr_attendance_rate);
        }
        stmt.setString(index++, ccr_offline_condition);
        stmt.setTimestamp(index++, cwSQL.getTime(con));
        stmt.setString(index++, ccr_upd_usr_id);
        stmt.setLong(index++, ccr_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    public boolean checkUpdTimestamp(Connection con) throws SQLException{
        boolean result = false;
        String sql = "select ccr_upd_timestamp from CourseCriteria where ccr_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Timestamp upd_timestamp = null;
        int index = 1;
        stmt.setLong(index++, this.ccr_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            upd_timestamp = rs.getTimestamp("ccr_upd_timestamp");
        }
        rs.close();
        stmt.close();
        if(upd_timestamp != null && this.ccr_upd_timestamp.equals(upd_timestamp))
            result = true;
        return result;
    }
    
    
    
/*
    public static Vector getCcrIdByCosId(Connection con, long cos_id) throws SQLException{
        Vector ccrIds = new Vector();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ccr_id_by_cos_id);
        stmt.setLong(1, cos_id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ccrIds.addElement(new Long(rs.getLong("ccr_id")));
        }
        return ccrIds;
    }
*/
    public static long getCcrItmIdByCmtId(Connection con,long cmt_id) throws SQLException{
        long ccr_itm_id = 0;
        String sql = "select ccr_itm_id from CourseCriteria where ccr_id = " +            "(select cmt_ccr_id from CourseMeasurement where cmt_id = ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,cmt_id);
        ResultSet rs = pstmt.executeQuery();
         if(rs.next()){
             ccr_itm_id = rs.getLong("ccr_itm_id");
         }
         pstmt.close();
        return ccr_itm_id;
    }
	/**
	 * @param con
	 * @param mtv_cmt_id
	 * @return
	 */
	public static long getCcrIdByCmtId(Connection con, long cmt_id) throws SQLException {
        long ccr_id = 0;
        String sql = "select ccr_id from CourseCriteria where ccr_id = " +            "(select cmt_ccr_id from CourseMeasurement where cmt_id = ? )" ;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,cmt_id);
        ResultSet rs = pstmt.executeQuery();
         if(rs.next()){
             ccr_id = rs.getLong("ccr_id");
         }
         pstmt.close();
        return ccr_id;
	}
	/**
	 * @param con
	 * @param app_id
	 * @return
	 */
	public static long getCcrIdByAppId(Connection con, long app_id) throws SQLException {
		long ccr_id = 0;
		String sql = "select ccr_id from CourseCriteria where ccr_itm_id = " +
			"(select app_itm_id from aeApplication where app_id = ? )" ;
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,app_id);
		ResultSet rs = pstmt.executeQuery();
		 if(rs.next()){
			 ccr_id = rs.getLong("ccr_id");
		 }
		 pstmt.close();
		return ccr_id;	}
	
	public static long getParentCcrIdByItmID(Connection con, long itm_id, String ccr_type) throws SQLException {
		long ccr_id = 0;
		String sql = "select ccr_id from CourseCriteria,AeItemRelation where ccr_itm_id = ire_parent_itm_id" +
			" and ire_child_itm_id=?" ;
        if(ccr_type == null) {
            sql = sql + " and ccr_type is null";
        }else {
            sql = sql + " and ccr_type = ? ";
        }
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,itm_id);
         if(ccr_type != null) {
                pstmt.setString(2,ccr_type);
            }
		ResultSet rs = pstmt.executeQuery();
		 if(rs.next()){
			 ccr_id = rs.getLong("ccr_id");
		 }
		 pstmt.close();
		return ccr_id;	
	}
	
	public static long getCcrIdByItmID(Connection con, long itm_id, String ccr_type) throws SQLException {
		long ccr_id = 0;
		String sql = "select ccr_id from CourseCriteria where ccr_itm_id = ?" ;
        if(ccr_type == null) {
            sql = sql + " and ccr_type is null";
        }else {
            sql = sql + " and ccr_type = ? ";
        }
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,itm_id);
        if(ccr_type != null) {
            pstmt.setString(2,ccr_type);
        }
		ResultSet rs = pstmt.executeQuery();
		 if(rs.next()){
			 ccr_id = rs.getLong("ccr_id");
		 }
		 pstmt.close();
		return ccr_id;	
	}
	
	public List getChCcrIdList(Connection con, long ccr_id)throws SQLException {
    	List ch_id_list = new ArrayList();
    	String sql = " select ccr_id from CourseCriteria where ccr_ccr_id_parent = ? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,ccr_id);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			ch_id_list.add(new Long(rs.getLong("ccr_id")));
		}
		pstmt.close();
		return ch_id_list;
    	
    }
	
	 public Timestamp getLastUpdTimestamp(Connection con, long ccr_id) throws SQLException{
        String sql = "select ccr_upd_timestamp from CourseCriteria where ccr_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        Timestamp upd_timestamp = null;
        int index = 1;
        stmt.setLong(index++,ccr_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            upd_timestamp = rs.getTimestamp("ccr_upd_timestamp");
        }
        rs.close();
        stmt.close();
        return upd_timestamp;
    }
     
     public static void updParent(Connection con, long ccr_id, long ccr_id_parent)throws SQLException {
        PreparedStatement stmt = con.prepareStatement("UPDATE CourseCriteria SET ccr_ccr_id_parent = ?  WHERE ccr_id = ? ");
        int index = 1;
        stmt.setLong(index++, ccr_id_parent);
        stmt.setLong(index++, ccr_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
     public static String getCcrType( Connection con, long ccr_id) throws SQLException{
         String ccr_type = null;
        String sql = " select ccr_type from CourseCriteria where ccr_id = ? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,ccr_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            ccr_type = rs.getString("ccr_type");
        }
        pstmt.close();
        return ccr_type;
         
     }
    
    public static void updClsCcrParent(Connection con, long itm_id) throws SQLException{
        Vector vtccrid = new Vector();
        String sql = "select ccr_id from CourseCriteria where ccr_itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id = ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,itm_id);
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            vtccrid.addElement(new Long(rs.getLong("ccr_id")));
        }
        pstmt.close();
        long ccr_id_parent = getCcrIdByItmID(con, itm_id, TYPE_COMPLETION);
        for (int i =0; i<vtccrid.size(); i++){
            long ccr_id = ((Long)vtccrid.get(i)).longValue();
            updParent(con, ccr_id, ccr_id_parent);
        }
    }
    
    public static final String _MAP_KEY_APP_ID = "app_id";
    public static final String _MAP_KEY_APP_ENT_ID = "app_ent_id";
    public static final String _MAP_KEY_APP_TKH_ID = "app_tkh_id";
    public static final String _MAP_KEY_ATT_RATE = "att_rate";
    public static final String _MAP_KEY_CCR_ID = "ccr_id";
    public static final String _MAP_KEY_CCR_PASS_IND = "ccr_pass_ind";
    public static final String _MAP_KEY_CCR_PASS_SCORE = "ccr_pass_score";
    public static final String _MAP_KEY_CCR_ATTENDANCE_RATE = "ccr_attendance_rate";
    public static final String _MAP_KEY_COS_RES_ID = "cos_res_id";
    public static final String _MAP_KEY_COV_SCORE = "cov_score";
    
    public static Vector getCourseCriteriaByType(Connection con, String ccrType) throws SQLException {
		Vector cosCriteriaVec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(OuterJoinSqlStatements.getCourseCriteriaByType());
			int index = 1;
			stmt.setString(index++, ccrType);
			rs = stmt.executeQuery();
			while(rs.next()) {
				HashMap cosCriteriaMap = new HashMap();
				cosCriteriaMap.put(_MAP_KEY_APP_ID, new Long(rs.getLong("app_id")));
				cosCriteriaMap.put(_MAP_KEY_APP_ENT_ID, new Long(rs.getLong("app_ent_id")));
				cosCriteriaMap.put(_MAP_KEY_APP_TKH_ID, new Long(rs.getLong("app_tkh_id")));
				cosCriteriaMap.put(_MAP_KEY_ATT_RATE, new Float(rs.getFloat("att_rate")));
				cosCriteriaMap.put(_MAP_KEY_CCR_ID, new Long(rs.getLong("ccr_id")));
				cosCriteriaMap.put(_MAP_KEY_CCR_PASS_IND, new Boolean(rs.getBoolean("ccr_pass_ind")));
				cosCriteriaMap.put(_MAP_KEY_CCR_PASS_SCORE, new Float(rs.getFloat("ccr_pass_score")));
				cosCriteriaMap.put(_MAP_KEY_CCR_ATTENDANCE_RATE, new Integer(rs.getInt("ccr_attendance_rate")));
				cosCriteriaMap.put(_MAP_KEY_COS_RES_ID, new Long(rs.getLong("cos_res_id")));
				cosCriteriaMap.put(_MAP_KEY_COV_SCORE, new Float(rs.getFloat("cov_score")));
				cosCriteriaVec.addElement(cosCriteriaMap);
			}
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
		return cosCriteriaVec;
	}
    
}

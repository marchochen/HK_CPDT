package com.cw.wizbank.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.qdb.dbModule;


public class DbCourseModuleCriteria{
    public long cmr_id;
    public long cmr_ccr_id;
    public long cmr_res_id;
    public String cmr_status;        
    public float cmr_contri_rate;
    public boolean cmr_is_contri_by_score;
    public Timestamp cmr_create_timestamp;
    public String cmr_create_usr_id;
    public Timestamp cmr_upd_timestamp;        
    public String cmr_upd_usr_id;
    public String cmr_status_desc_option;
    public long cmr_cmr_id_parent ;
        
    public static Vector getByCcrId(Connection con, long ccr_id) throws SQLException{
        Vector vtCmr = new Vector();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_CourseModuleCriteria_by_ccr_id);
            stmt.setLong(1, ccr_id);
            rs = stmt.executeQuery();
            while(rs.next()) {
                DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
                cmr.cmr_id = rs.getLong("cmr_id");
                cmr.cmr_ccr_id = rs.getLong("cmr_ccr_id");
                cmr.cmr_res_id = rs.getLong("cmr_res_id");
                cmr.cmr_status = rs.getString("cmr_status");
                cmr.cmr_contri_rate = rs.getFloat("cmr_contri_rate");
                cmr.cmr_is_contri_by_score = rs.getBoolean("cmr_is_contri_by_score");
                cmr.cmr_status_desc_option = rs.getString("cmr_status_desc_option");
                cmr.cmr_cmr_id_parent = rs.getLong("cmr_cmr_id_parent");
                vtCmr.addElement(cmr);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return vtCmr;
    }
    
    public  boolean getOnlineCriteria(Connection con,long ccr_id,long mod_id){
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	try{
    		pst = con.prepareStatement(SqlStatements.sql_get_OnlineModuleCriteria_by_ccr_id);
    		pst.setLong(1,ccr_id);
    		pst.setLong(2,mod_id);
    		rs = pst.executeQuery();
    		if(rs.next()){
				cmr_id = rs.getLong("cmr_id");
				cmr_ccr_id = rs.getLong("cmr_ccr_id");
				cmr_res_id = rs.getLong("cmr_res_id");
				cmr_status = rs.getString("cmr_status");
				cmr_contri_rate = rs.getFloat("cmr_contri_rate");
				cmr_is_contri_by_score = rs.getBoolean("cmr_is_contri_by_score");
				return true;
    		}
    	}catch(SQLException e){
    		throw new RuntimeException("Server error: "+e.getMessage());
    	}finally{
    		cwSQL.cleanUp(rs,pst);
    	}
    	return false;
    }
    
    public static void delByCmrId(Connection con, long cmr_id) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_CourseModuleCriteria);
        stmt.setLong(1, cmr_id);
        stmt.setLong(2, cmr_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void softDelByCmrId(Connection con, long cmr_id, String usr_id) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_soft_del_CourseModuleCriteria);
        Timestamp curTime = cwSQL.getTime(con);
        stmt.setTimestamp(1, curTime);
        stmt.setTimestamp(2, curTime);
        stmt.setString(3, usr_id);
        stmt.setLong(4, cmr_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void delByCcrId(Connection con, long ccr_id) throws SQLException{
    	/*String sql = "delete from CourseModuleCriteria where cmr_cmr_id_parent in (select * from (select cmr_id From CourseModuleCriteria WHERE cmr_ccr_id = ? ) newtab)";
    	PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, ccr_id);
        stmt.executeUpdate();*/
    	PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_CourseModuleCriteria_by_ccr_id);
        stmt.setLong(1, ccr_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void delByModId(Connection con, long mod_id) throws SQLException{
    	 
        String sql = "delete from CourseMeasurement where cmt_cmr_id in (select cmr_id from CourseModuleCriteria where cmr_cmr_id_parent in (select cmr_id From CourseModuleCriteria WHERE cmr_res_id = ? )) or cmt_cmr_id in(select cmr_id From CourseModuleCriteria WHERE cmr_res_id = ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, mod_id);
        stmt.setLong(2, mod_id);
        stmt.executeUpdate();
        stmt.close();
        
    	if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
    		//sql = "delete from CourseModuleCriteria where cmr_cmr_id_parent in ( select cmr_id from ( select cmr_id From CourseModuleCriteria  WHERE cmr_res_id = ? ) as tmp)";
    	}else{
    		sql = "delete from CourseModuleCriteria where cmr_cmr_id_parent in (select cmr_id From CourseModuleCriteria WHERE cmr_res_id = ? )";
    		stmt = con.prepareStatement(sql);
            stmt.setLong(1, mod_id);
            stmt.executeUpdate();
            stmt.close();
    	}
        
        stmt = con.prepareStatement(SqlStatements.sql_del_CourseModuleCriteria_by_mod_id);
        stmt.setLong(1, mod_id);
        stmt.executeUpdate();
        stmt.close();
    }
 
    public void ins(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_CourseModuleCriteria, PreparedStatement.RETURN_GENERATED_KEYS);
        int index = 1;
        stmt.setLong(index++, cmr_ccr_id);
        stmt.setLong(index++, cmr_res_id);
        stmt.setString(index++, cmr_status);
        stmt.setFloat(index++, cmr_contri_rate);
        stmt.setBoolean(index++, cmr_is_contri_by_score);
        stmt.setTimestamp(index++, cmr_create_timestamp);
        stmt.setString(index++, cmr_create_usr_id);
        stmt.setTimestamp(index++, cmr_upd_timestamp);
        stmt.setString(index++, cmr_upd_usr_id);
        if(cmr_status_desc_option == null || cmr_status_desc_option.equals("")){
            stmt.setNull(index++, Types.VARCHAR);
        }else{
            stmt.setString(index++, cmr_status_desc_option);
        }
        if ( cmr_cmr_id_parent  != 0){
			stmt.setLong(index++, cmr_cmr_id_parent );
		} else {
			stmt.setNull(index++, Types.INTEGER);
		}
        stmt.executeUpdate();
        cmr_id = cwSQL.getAutoId(con, stmt, "CourseModuleCriteria", "cmr_id");
        stmt.close();
    }
    
    public static void delOld(Connection con, long ccr_id) throws SQLException {
    	String del_sql_ch = null;
    	if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
    		del_sql_ch = "delete from CourseModuleCriteria  where cmr_cmr_id_parent in ("
    				+" select cmr_id from ( "
    				+" select cmr_id from CourseModuleCriteria where cmr_ccr_id in ( "
    				+" select cmt_cmr_id from coursemeasurement where cmt_is_contri_by_score = 0 and cmt_ccr_id = ? )  and cmr_status = ? ) as temp "
    				+" ) ";
    	}else{
        	del_sql_ch = "delete from CourseModuleCriteria where cmr_cmr_id_parent in (select cmr_id from CourseModuleCriteria where cmr_ccr_id in ("+
        	    	"select cmt_cmr_id from coursemeasurement where cmt_is_contri_by_score = 0 and cmt_ccr_id = ? )  and cmr_status = ?)";
    	}

        PreparedStatement stmt = con.prepareStatement(del_sql_ch);
        int index = 1;
        stmt.setLong(index++, ccr_id);
        stmt.setString(index++, "IFCP");
        stmt.executeUpdate();
    	
        String sql = "delete from CourseModuleCriteria where cmr_ccr_id in (select cmt_cmr_id from coursemeasurement where cmt_is_contri_by_score = 0 and cmt_ccr_id = ? )  and cmr_status = ?";
        stmt = con.prepareStatement(sql);
        index = 1;
        stmt.setLong(index++, ccr_id);
        stmt.setString(index++, "IFCP");
        stmt.executeUpdate();
        stmt.close();
    }

    public void upd(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_CourseModuleCriteria);
        int index = 1;
        stmt.setString(index++, cmr_status);
        stmt.setFloat(index++, cmr_contri_rate);
        stmt.setBoolean(index++, cmr_is_contri_by_score);
        stmt.setTimestamp(index++, cmr_upd_timestamp);
        stmt.setString(index++, cmr_upd_usr_id);
        stmt.setLong(index++, cmr_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void updCond(Connection con) throws SQLException {
        final String sql = "update CourseModuleCriteria set cmr_res_id=?,cmr_status=?,cmr_status_desc_option=?,cmr_upd_timestamp=?,cmr_upd_usr_id=? where cmr_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, cmr_res_id);
        stmt.setString(index++, cmr_status);
        if(cmr_status_desc_option.length() <= 0){
            stmt.setNull(index++, Types.VARCHAR);
        } else {
            stmt.setString(index++, cmr_status_desc_option);
        }
        stmt.setTimestamp(index++, cmr_upd_timestamp);
        stmt.setString(index++, cmr_upd_usr_id);
        stmt.setLong(index++, cmr_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public void updRunCond(Connection con) throws SQLException {
        final String sql = "update CourseModuleCriteria set cmr_status=?,cmr_status_desc_option=?,cmr_upd_timestamp=?,cmr_upd_usr_id=? where cmr_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setString(index++, cmr_status);
        if(cmr_status_desc_option.length() <= 0){
            stmt.setNull(index++, Types.VARCHAR);
        } else {
            stmt.setString(index++, cmr_status_desc_option);
        }
        stmt.setTimestamp(index++, cmr_upd_timestamp);
        stmt.setString(index++, cmr_upd_usr_id);
        stmt.setLong(index++, cmr_id);
        stmt.executeUpdate();
        stmt.close();
    }
    
    public static boolean isActiveCriteria(Connection con, long modId) throws SQLException{
        boolean isActive;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_is_active_criteria);
        stmt.setLong(1, modId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            int cnt = rs.getInt("cnt");
            if (cnt > 0 )
                isActive = true;
            else
                isActive = false;
        }else{
            isActive = false;
        }
        stmt.close();
        return isActive;
    }
    
    public long getCmrId(Connection con, long mod_res_id, long ccr_id) throws SQLException{
		PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_cmr_id);
		stmt.setLong(1, mod_res_id);
		stmt.setLong(2, ccr_id);
		ResultSet rs = stmt.executeQuery();
		long crm_id = 0;
		if (rs.next()){
			crm_id =rs.getLong("cmr_id");
		} 
		stmt.close();
		return crm_id;
    }
	public void getByCmrId(Connection con, long cmr_id) throws SQLException{
		PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_by_cmr_id);
		stmt.setLong(1, cmr_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()){
			this.cmr_id = rs.getLong("cmr_id");
			this.cmr_contri_rate = rs.getLong("cmr_contri_rate");
			this.cmr_res_id = rs.getLong("cmr_res_id");
			this.cmr_status = rs.getString("cmr_status");
		}
		stmt.close();
	}
	
	public void setCmr_statusByOption(String option, String[] status) {
        if ((!option.equalsIgnoreCase("")) && option != null) {
            int index;
            try {
                index = Integer.parseInt(option);
                cmr_status = status[index];
            } catch (NumberFormatException e) {
                cmr_status = null;
            }
        } else {
            cmr_status = null;
        }
    }

	/**
	 * @return
	 */
	public static long getCmrResIdByCmtId(Connection con,long cmt_id) throws SQLException {
		// TODO Auto-generated method stub
		long cmr_res_id = 0;
		String sql = "select cmr_res_id from CourseModuleCriteria where cmr_ccr_id = " +
						"(select cmt_ccr_id from CourseMeasurement where cmt_id = ?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,cmt_id);
		ResultSet rs = pstmt.executeQuery();
		 if(rs.next()){
			cmr_res_id = rs.getLong("cmr_res_id");
		 }
		 pstmt.close();
		return cmr_res_id;
	}
	
	public List getChCmtIdList(Connection con, long cmr_id)throws SQLException {
    	List ch_id_list = new ArrayList();
    	String sql = "select cmr_id from CourseModuleCriteria where cmr_cmr_id_parent = ? ";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,cmr_id);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			ch_id_list.add(new Long(rs.getLong("cmr_id")));
		}
		pstmt.close();
		return ch_id_list;
    	
    }
	
	public HashMap copyCmr2ChItm(Connection con, long parent_ccr_id, long ccr_id, long itm_id,Timestamp curTime, String usr_id)throws SQLException {
		HashMap cmr_id_map = new HashMap();
		Vector all_cmr = getByCcrId( con,  parent_ccr_id);
		for (int i = 0; i<all_cmr.size(); i++) {
			DbCourseModuleCriteria cmr = (DbCourseModuleCriteria)all_cmr.get(i);
			 cmr.cmr_ccr_id = ccr_id;
			 cmr.cmr_res_id = dbModule.getChidltmModID( con, cmr.cmr_res_id, itm_id);
			 cmr.cmr_cmr_id_parent = cmr.cmr_id;
			 cmr.cmr_create_timestamp = curTime;
			 cmr.cmr_upd_timestamp = curTime;
             cmr.cmr_create_usr_id = usr_id;
             cmr.cmr_upd_usr_id = usr_id;
			 cmr.ins( con);
			 cmr_id_map.put(new Long(cmr.cmr_cmr_id_parent),new Long(cmr.cmr_id));
		}
    	return cmr_id_map;
    }
	
      public long getChCmrIdByCcrIDCmrParentID(Connection con, long ccr_id, long parent_cmr_id)throws SQLException {
        	long ch_id = 0;
        	String sql = " select cmr_id from CourseModuleCriteria where cmr_ccr_id = ? and cmr_cmr_id_parent = ?";
    		PreparedStatement pstmt = con.prepareStatement(sql);
    		pstmt.setLong(1,ccr_id);
    		pstmt.setLong(2,parent_cmr_id);
    		ResultSet rs = pstmt.executeQuery();
    		if(rs.next()){
    			ch_id=rs.getLong("cmr_id");
    		}
    		pstmt.close();
    		return ch_id;
        	
         }
      public void updResID(Connection con) throws SQLException{
          String sql = " UPDATE CourseModuleCriteria SET  cmr_res_id = ? WHERE cmr_id = ? ";
          PreparedStatement stmt = con.prepareStatement(sql);
          int index = 1;
          stmt.setLong(index++, cmr_res_id);
          stmt.setLong(index++, cmr_id);
          stmt.executeUpdate();
          stmt.close();
      }

}

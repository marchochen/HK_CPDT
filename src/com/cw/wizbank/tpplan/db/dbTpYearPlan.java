package com.cw.wizbank.tpplan.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.StringUtils;

public class dbTpYearPlan {
    public static final String YPN_STATUS_PREPARED = "PREPARED";
    public static final String YPN_STATUS_PENDING = "PENDING";
    public static final String YPN_STATUS_APPROVED = "APPROVED";
    public static final String YPN_STATUS_DECLINED = "DECLINED";
    
    public static final String YPN_FILE_DIR_UPLOAD = "plan";
    
	public long ypn_year;
	public long ypn_tcr_id;
	public String ypn_file_name;
	public String ypn_status;
	public String ypn_approve_usr_id;
	public Timestamp ypn_approve_timestamp;
	public String ypn_create_usr_id;
	public Timestamp ypn_create_timestamp;
	public String ypn_update_usr_id;
	public Timestamp ypn_update_timestamp;
	public String ypn_submit_usr_id;
	public Timestamp ypn_submit_timestamp;
	
	private static final String INS_YEAR_PLAN_SQL ="INSERT INTO tpYearPlan(ypn_year, ypn_tcr_id, ypn_file_name, ypn_status, ypn_approve_usr_id, ypn_approve_timestamp,ypn_create_timestamp, ypn_create_usr_id, ypn_update_usr_id, ypn_update_timestamp)values(?,?,?,?,?,?,?,?,?,?)";
	public void ins(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(INS_YEAR_PLAN_SQL);
            int index = 1;
            stmt.setLong(index++, ypn_year);
            stmt.setLong(index++, ypn_tcr_id);
            stmt.setString(index++, ypn_file_name);
            stmt.setString(index++, ypn_status);
            stmt.setString(index++, ypn_approve_usr_id);
            stmt.setTimestamp(index++, ypn_approve_timestamp);
            stmt.setTimestamp(index++, ypn_create_timestamp);
            stmt.setString(index++, ypn_create_usr_id);
            stmt.setString(index++, ypn_update_usr_id);
            stmt.setTimestamp(index++, ypn_update_timestamp);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}

	private static final String UPDATE_YEAR_PALN_SQL ="UPDATE tpYearPlan SET ypn_file_name = ?, ypn_status =?, ypn_approve_usr_id = ?, ypn_approve_timestamp =?, ypn_update_usr_id =?, ypn_update_timestamp = ? WHERE ypn_year =? and ypn_tcr_id =?";
	public void upd(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(UPDATE_YEAR_PALN_SQL);
            int index = 1;
            stmt.setString(index++, ypn_file_name);
            stmt.setString(index++, ypn_status);
            stmt.setString(index++, ypn_approve_usr_id);
            stmt.setTimestamp(index++, ypn_approve_timestamp);
            stmt.setString(index++, ypn_update_usr_id);
            stmt.setTimestamp(index++, ypn_update_timestamp);
            stmt.setLong(index++, ypn_year);
            stmt.setLong(index++, ypn_tcr_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	public void get(Connection con) throws SQLException {
    	String sql = " SELECT ypn_year, ypn_tcr_id, ypn_file_name, ypn_status, ypn_approve_usr_id, ypn_approve_timestamp, "
        		   + " ypn_create_timestamp, ypn_create_usr_id, ypn_update_usr_id, ypn_update_timestamp,ypn_submit_usr_id, ypn_submit_timestamp "
        	       + " FROM tpYearPlan "
                   + " WHERE ypn_year = ? and ypn_tcr_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        
        stmt.setLong(1, ypn_year);
        stmt.setLong(2, ypn_tcr_id);
        ResultSet rs = stmt.executeQuery();
        if( rs.next())
        {	
        	ypn_year = rs.getLong("ypn_year");
        	ypn_tcr_id = rs.getLong("ypn_tcr_id");
        	ypn_file_name = rs.getString("ypn_file_name"); 
        	ypn_status = rs.getString("ypn_status");
        	ypn_approve_usr_id = rs.getString("ypn_approve_usr_id");
        	ypn_approve_timestamp = rs.getTimestamp("ypn_approve_timestamp");
        	ypn_create_usr_id = rs.getString("ypn_create_usr_id");
        	ypn_create_timestamp = rs.getTimestamp("ypn_create_timestamp");
        	ypn_update_usr_id = rs.getString("ypn_update_usr_id");
        	ypn_update_timestamp = rs.getTimestamp("ypn_update_timestamp");
        	ypn_submit_usr_id = rs.getString("ypn_submit_usr_id");
        	ypn_submit_timestamp = rs.getTimestamp("ypn_submit_timestamp");
        } 
        stmt.close();
	}
	static final String UPDATE_STATUS_SQL = " UPDATE tpYearPlan  SET ypn_status = ?,  ypn_update_timestamp = ?, ypn_update_usr_id = ?, ypn_submit_timestamp = ?,  ypn_submit_usr_id =? " +
			" WHERE ypn_year = ? and ypn_tcr_id = ? ";
	public void upd_status(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(UPDATE_STATUS_SQL);
            int index = 1;
            stmt.setString(index++, ypn_status);
            stmt.setTimestamp(index++, ypn_update_timestamp);
            stmt.setString(index++, ypn_update_usr_id);
            stmt.setTimestamp(index++, ypn_submit_timestamp);
            stmt.setString(index++, ypn_submit_usr_id);
            stmt.setLong(index++, ypn_year);
            stmt.setLong(index++, ypn_tcr_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	static final String DEL_SQL = " DELETE FROM tpYearPlan WHERE ypn_year = ? and ypn_tcr_id = ? ";	
	public void del(Connection con, WizbiniLoader wizbini) throws SQLException, cwException {
        PreparedStatement stmt = null;
        try {
			stmt = con.prepareStatement(DEL_SQL);
	        stmt.setLong(1, ypn_year);
	        stmt.setLong(2, ypn_tcr_id);
	        stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        delFile(con, wizbini);
        return;
	}
	 
	private void delFile(Connection con, WizbiniLoader wizbini) throws cwException {
	    String saveDirPath = wizbini.getWebDocRoot() + dbUtils.SLASH + YPN_FILE_DIR_UPLOAD + dbUtils.SLASH + ypn_tcr_id + dbUtils.SLASH + ypn_year;
	    dbUtils.delDir(saveDirPath);
	}
	public static void uploadedFile(WizbiniLoader wizbini, long tcr_id, long year, String tmpSaveDirPath) throws cwException {
		String saveDirPath = wizbini.getWebDocRoot() + dbUtils.SLASH + YPN_FILE_DIR_UPLOAD + dbUtils.SLASH + tcr_id + dbUtils.SLASH + year;
	    try {
	        dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
	    } catch(qdbException e) {
	        throw new cwException(e.getMessage());
	    }
	}
	/** check last upd time
     * @param con
     * @throws qdbException
     * @throws cwSysMessage
     */
    public void checkTimeStamp(Connection con) 
    throws qdbException, cwSysMessage
		{
//		   try {
//		        PreparedStatement stmt = con.prepareStatement(
//		        " SELECT ypn_update_timestamp FROM tpYearPlan WHERE ypn_year = ? and ypn_tcr_id = ? ");
//		     
//		        stmt.setLong(1, ypn_year);
//		        stmt.setLong(2, ypn_tcr_id);
//		        ResultSet rs = stmt.executeQuery();
//		        boolean bTSOk = false;
//		        if(rs.next())
//		        {
//		            Timestamp t = rs.getTimestamp(1);
//		            Timestamp tTmp = t;
//		            tTmp.setNanos(ypn_update_timestamp.getNanos());
//		            if(tTmp.equals(ypn_update_timestamp))
//		                bTSOk = true;
//		        }
//		        stmt.close();
//		        if(!bTSOk) {
//		            con.rollback();
//		            throw new cwSysMessage("GEN006");
//		        }
//		        
//		    }catch(SQLException e) {
//		        throw new qdbException("SQL Error: " + e.getMessage()); 
//		    }
		}

	public void checkSubmitTimeStamp(Connection con) throws cwSysMessage, qdbException {
	   try {
		    Timestamp cur_time = cwSQL.getTime(con);
		    
	        PreparedStatement stmt = con.prepareStatement(
	        " SELECT count(*) as cnt FROM tpYearPlan, tpYearSetting WHERE ypn_year = ysg_year " +
	        " AND " + cwSQL.subFieldLocation("ysg_child_tcr_id_lst", "ypn_tcr_id", true) +
	        " AND  ypn_year = ? and ypn_tcr_id = ? AND ? BETWEEN ysg_submit_start_datetime AND ysg_submit_end_datetime");
	        
	        int index = 1;
	        stmt.setLong(index++, ypn_year);
	        stmt.setLong(index++, ypn_tcr_id);
	        stmt.setTimestamp(index++, cur_time);
	        ResultSet rs = stmt.executeQuery();
	        boolean bTSOk = false;
	        if(rs.next())
	        {
		        if(rs.getLong("cnt") > 0) {
		        	bTSOk = true;    	
		        }
	        }
	        stmt.close();
	        if(!bTSOk) {
	            con.rollback();
	            throw new cwSysMessage("TPN003");
	        }
	        
	    }catch(SQLException e) {
	        throw new qdbException("SQL Error: " + e.getMessage()); 
	    }
	}
	
	public String getPlanXml(Connection con, String status, String tcr_id_lst, cwPagination page) throws SQLException{
		StringBuffer result = new StringBuffer();		
		String sql = "select ypn_year, ypn_tcr_id, tcr_title, ysg_submit_end_datetime, ypn_status, ypn_file_name, ypn_update_timestamp, ypn_submit_timestamp "
				   + " from tpYearPlan, tctrainingcenter, tpYearSetting "
				   + " where tcr_parent_tcr_id in " + tcr_id_lst
				   + " and ypn_tcr_id = tcr_id "
				   + " and ysg_year = ypn_year "
				   + " and tcr_parent_tcr_id = ysg_tcr_id ";
		if (status.equalsIgnoreCase("ALL")){
			sql += " and ypn_status in (?, ?, ?)";
		}else{
			sql += " and ypn_status = ? ";
		}
		if(page.sortCol == null) {
			page.sortCol = "ypn_year";
		}
		if(page.sortOrder == null) {
			page.sortOrder = "desc";
		}
		sql +=" order by " + page.sortCol + "  " + page.sortOrder; 
	    PreparedStatement stmt = con.prepareStatement(sql);
	    int index = 1;
	    stmt = con.prepareStatement(sql);
	    if (status.equalsIgnoreCase("ALL")){
		    stmt.setString(index++, YPN_STATUS_APPROVED);
		    stmt.setString(index++, YPN_STATUS_DECLINED);
		    stmt.setString(index++, YPN_STATUS_PENDING);
	    }else if (status.equals(YPN_STATUS_APPROVED)){
		    stmt.setString(index++, YPN_STATUS_APPROVED);
	    }else if (status.equals(YPN_STATUS_DECLINED)){
		    stmt.setString(index++, YPN_STATUS_DECLINED);
	    }else if (status.equals(YPN_STATUS_PENDING)){
		    stmt.setString(index++,YPN_STATUS_PENDING);
	    }

		ResultSet rs = stmt.executeQuery();
		if (page != null){
	        if (page.pageSize <= 0) {
	            page.pageSize = Integer.MAX_VALUE;
	        }
	        if (page.curPage <= 0) {
	            page.curPage = 1;
	        }			
		}
        int count = 1;
	    result.append("<trainingplan_list");
	    if (status.equals("ALL")){
	    	result.append(" status=\"").append("\">");
	    }else{
	    	result.append(" status=\"").append(status).append("\">");
	    }
	    while(rs.next()){
	    	if ((count>(page.curPage-1)*page.pageSize) && (count<=page.curPage*page.pageSize)) {
		    	result.append("<trainingplan ypn_year=\"").append(rs.getLong("ypn_year"))
		    		.append("\" ypn_tcr_id=\"").append(rs.getLong("ypn_tcr_id"))
			    	.append("\" tcr_title=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("tcr_title"))))
			    	.append("\" ysg_submit_end_datetime=\"").append(cwUtils.escNull(rs.getTimestamp("ysg_submit_end_datetime")))
			    	.append("\" ypn_status=\"").append(rs.getString("ypn_status"))
			    	.append("\" ypn_file_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("ypn_file_name"))))
			    	.append("\" ypn_update_timestamp=\"").append(cwUtils.escNull(rs.getTimestamp("ypn_update_timestamp")))
			    	.append("\" ypn_submit_timestamp=\"").append(cwUtils.escNull(rs.getTimestamp("ypn_submit_timestamp")))
			    	.append("\"/>");
	    	}
	    	page.totalRec++;
	    	count++;
	    }
	    stmt.close();
	    result.append("</trainingplan_list>");
        page.totalPage = (int)Math.ceil((float)page.totalRec/(float)page.pageSize);
        result.append(page.asXML());
		return result.toString();		
	}	

	public String getPlanStausCntXml(Connection con, String tcr_vec_lst) throws SQLException{
		StringBuffer result = new StringBuffer();
		String sqlCount = "select count(ypn_status) as ctn,ypn_status from tpYearPlan, tctrainingcenter, tpYearSetting "
				+ "where tcr_parent_tcr_id in  " + tcr_vec_lst
				+ " and ypn_tcr_id = tcr_id "
			    + " and ysg_year = ypn_year "
			    + " and tcr_parent_tcr_id = ysg_tcr_id "
			    + " and ypn_status in (?, ?, ?) ";
		sqlCount += " group by ypn_status";
	    PreparedStatement stmt = con.prepareStatement(sqlCount);	    
	    int index = 1;
	    stmt.setString(index++, YPN_STATUS_APPROVED);
	    stmt.setString(index++, YPN_STATUS_DECLINED);
	    stmt.setString(index++, YPN_STATUS_PENDING);
	    ResultSet rs = stmt.executeQuery();
		result.append("<process_status_cnt_list>");
		long totalcnt = 0;
		long cnt = 0;
	    while(rs.next()){
	    	cnt = rs.getLong("ctn");
			result.append("<process_status_cnt name=\"").append(rs.getString("ypn_status")).append("\" cnt=\"").append(cnt).append("\"/>");
			totalcnt +=cnt;
	    }
	    stmt.close();
	    result.append("<total_process_status_cnt cnt=\"").append(totalcnt).append("\"/>");
		result.append("</process_status_cnt_list>");
		return result.toString();
	}
	
	static final String AUDITING_PLAN_SQL = " UPDATE tpYearPlan SET ypn_status =?, ypn_approve_usr_id = ?, ypn_approve_timestamp =?, ypn_update_usr_id =?, ypn_update_timestamp = ? WHERE ypn_year =? and ypn_tcr_id =? ";
	public void auditingPlan(Connection con) throws SQLException{
        PreparedStatement stmt = null;
	    Timestamp cur_time = cwSQL.getTime(con);
        try {
            stmt = con.prepareStatement(AUDITING_PLAN_SQL);
            int index = 1;
            stmt.setString(index++, ypn_status);
            stmt.setString(index++, ypn_approve_usr_id);
            stmt.setTimestamp(index++, cur_time);            
            stmt.setString(index++, ypn_update_usr_id);
            stmt.setTimestamp(index++, cur_time);
            stmt.setLong(index++, ypn_year);
            stmt.setLong(index++, ypn_tcr_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	private static final String IS_EXIST_YEAR_PLAN_SQL = "SELECT count(*) as cnt FROM tpYearPlan WHERE ypn_year = ? and ypn_tcr_id = ? ";
	public static boolean isExistYearPlan(Connection con, long year, long tcr_id) throws SQLException {
        PreparedStatement stmt = null;
	    boolean result = false;
        try {
            stmt = con.prepareStatement(IS_EXIST_YEAR_PLAN_SQL);
            int index = 1;
            stmt.setLong(index++, year);
            stmt.setLong(index++, tcr_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
            	if(rs.getLong(1) >0) {
            		result = true;
            	}
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return result;
	}
    public boolean saveTpYearPlan(Connection con, loginProfile prof, WizbiniLoader wizbini, String tmpUploadPath, dbTpYearPlan ypn, Timestamp upd_timstamp) throws SQLException, cwSysMessage, cwException, qdbException {
    	Timestamp curTime = cwSQL.getTime(con);
    	boolean success = false;
    	if(ypn != null) {
    		if (upd_timstamp != null) {
    			ypn.ypn_update_timestamp = upd_timstamp;
    			ypn.checkTimeStamp(con);
    		}
    		if(dbTpYearPlan.isExistYearPlan(con, ypn.ypn_year, ypn.ypn_tcr_id)) {
    			ypn.ypn_update_timestamp = curTime;
    			ypn.ypn_update_usr_id = prof.usr_id;
				ypn.upd(con);
				ypn.delFile(con, wizbini);
    			dbTpYearPlan.uploadedFile(wizbini, ypn.ypn_tcr_id, ypn.ypn_year, tmpUploadPath);
				success = true;
    		} else {
	            ypn.ypn_status = dbTpTrainingPlan.TPN_STATUS_PREPARED;
    			ypn.ypn_create_timestamp = curTime;
    			ypn.ypn_create_usr_id = prof.usr_id;
    			ypn.ypn_update_timestamp = curTime;
    			ypn.ypn_update_usr_id = prof.usr_id;
    			ypn.ins(con);
    			dbTpYearPlan.uploadedFile(wizbini, ypn.ypn_tcr_id, ypn.ypn_year, tmpUploadPath);
    			success = true;
    		}
    	}
    	return success;
    }
    public Vector getSupTcYearPlan(Connection con, long sup_tcr_id) throws SQLException {
    	String sql = " SELECT ypn_year FROM tpYearPlan WHERE  ypn_tcr_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, sup_tcr_id);
		ResultSet rs = stmt.executeQuery();
		Vector year_vec = new Vector();
		while (rs.next()) {
			year_vec.add(new Long(rs.getLong("ypn_year")));
		}
		stmt.close();
		return year_vec;
    }
}

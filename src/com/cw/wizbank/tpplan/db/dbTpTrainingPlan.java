package com.cw.wizbank.tpplan.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;


/**
 * @author jackyx
 * @date 2007-10-15
 */
public class dbTpTrainingPlan {
	
    public static final String TPN_TYPE_YEAR = "YEAR";
    public static final String TPN_TYPE_MAKEUP = "MAKEUP";
    
    public static final String TPN_STATUS_PREPARED = "PREPARED";
    public static final String TPN_STATUS_PENDING = "PENDING";
    public static final String TPN_STATUS_APPROVED = "APPROVED";
    public static final String TPN_STATUS_DECLINED = "DECLINED";
    public static final String TPN_STATUS_IMPLEMENTED= "IMPLEMENTED";
    
    public static final String CAT_ID = "CAT_ID";

	public long tpn_id;
	public long tpn_tcr_id;
	public Timestamp tpn_date;
	public String tpn_code;
	public String tpn_name;
	public String tpn_cos_type;
	public String tpn_tnd_title;
	public String tpn_introduction;
	public String tpn_aim;
	public String tpn_target;
	public String tpn_responser;
	public long tpn_lrn_count;
	public String tpn_duration;
	public Timestamp tpn_wb_start_date;
	public Timestamp tpn_wb_end_date;
	public Timestamp tpn_ftf_start_date;
	public Timestamp tpn_ftf_end_date;
	public String tpn_type;
	public float tpn_fee;
	public String tpn_remark;
	public String tpn_status;
	public String tpn_approve_usr_id;
	public Timestamp tpn_approve_timestamp;
	public String tpn_create_usr_id;
	public Timestamp tpn_create_timestamp;
	public String tpn_update_usr_id;
	public Timestamp tpn_update_timestamp;
	public String tpn_submit_usr_id;
	public Timestamp tpn_submit_timestamp;
    
	public String tpn_tcr_title;
    public String full_path;
	
	
	public static final String ins_tp_training_plan_sql = "INSERT INTO tpTrainingPlan(tpn_tcr_id,tpn_date,tpn_code,tpn_name,tpn_cos_type,tpn_tnd_title,tpn_responser,tpn_lrn_count,tpn_wb_start_date,tpn_wb_end_date,tpn_ftf_start_date,tpn_ftf_end_date,tpn_type,tpn_fee,tpn_status,tpn_approve_usr_id,tpn_approve_timestamp,tpn_create_usr_id,tpn_create_timestamp,tpn_update_usr_id,tpn_update_timestamp,tpn_submit_usr_id,tpn_submit_timestamp)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public void ins(Connection con) throws SQLException, cwSysMessage {
		PreparedStatement stmt = con.prepareStatement(ins_tp_training_plan_sql, PreparedStatement.RETURN_GENERATED_KEYS);
		int index = 1;
		stmt.setLong(index++, tpn_tcr_id);
		stmt.setTimestamp(index++, tpn_date);
		stmt.setString(index++, tpn_code);
		stmt.setString(index++, tpn_name);
		stmt.setString(index++, tpn_cos_type);
		stmt.setString(index++, tpn_tnd_title);
		stmt.setString(index++, tpn_responser);
		stmt.setLong(index++, tpn_lrn_count);
		stmt.setTimestamp(index++, tpn_wb_start_date);
		stmt.setTimestamp(index++, tpn_wb_end_date);
		stmt.setTimestamp(index++, tpn_ftf_start_date);
		stmt.setTimestamp(index++, tpn_ftf_end_date);
		stmt.setString(index++, tpn_type);
		stmt.setFloat(index++, tpn_fee);
		stmt.setString(index++, tpn_status);
		stmt.setString(index++, tpn_approve_usr_id);
		stmt.setTimestamp(index++, tpn_approve_timestamp);
		stmt.setString(index++, tpn_create_usr_id);
		stmt.setTimestamp(index++, tpn_create_timestamp);
		stmt.setString(index++, tpn_update_usr_id);
		stmt.setTimestamp(index++, tpn_update_timestamp);
		stmt.setString(index++, tpn_submit_usr_id);
		stmt.setTimestamp(index++, tpn_submit_timestamp);		

		stmt.executeUpdate();
		tpn_id = cwSQL.getAutoId(con, stmt, "tpTrainingPlan", "tpn_id");
		stmt.close();
        String columnName[]={"tpn_introduction","tpn_aim","tpn_target","tpn_duration","tpn_remark"};
        String columnValue[]={tpn_introduction,tpn_aim,tpn_target,tpn_duration,tpn_remark};
        String condition = "tpn_id= " + tpn_id;
        cwSQL.updateClobFields(con, "tpTrainingPlan",columnName,columnValue, condition);
		return;
	}
	private static final String upd_tp_plan_code_sql = "update tpTrainingPlan set tpn_code = ? where tpn_id = ? ";
	public void updCode(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(upd_tp_plan_code_sql);
		int index = 1;
		stmt.setString(index++, tpn_code);
		stmt.setLong(index, tpn_id);
		stmt.executeUpdate();
		stmt.close();
		return;
	}
	

	public static final String upd_tp_training_plan_sql = " UPDATE tpTrainingPlan Set tpn_tcr_id=?, tpn_date=?, tpn_name=?, tpn_cos_type=?, tpn_tnd_title=?, tpn_responser=?, tpn_lrn_count=?, tpn_wb_start_date=?, tpn_wb_end_date=?, tpn_ftf_start_date=?, tpn_ftf_end_date=?, tpn_fee=?, tpn_update_usr_id=?, tpn_update_timestamp=? WHERE tpn_id = ? ";	
	public void upd(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(upd_tp_training_plan_sql);
		int index = 1;
		stmt.setLong(index++, tpn_tcr_id);
		stmt.setTimestamp(index++, tpn_date);
		stmt.setString(index++, tpn_name);
		stmt.setString(index++, tpn_cos_type);
		stmt.setString(index++, tpn_tnd_title);
		stmt.setString(index++, tpn_responser);
		stmt.setLong(index++, tpn_lrn_count);
		stmt.setTimestamp(index++, tpn_wb_start_date);
		stmt.setTimestamp(index++, tpn_wb_end_date);
		stmt.setTimestamp(index++, tpn_ftf_start_date);
		stmt.setTimestamp(index++, tpn_ftf_end_date);
		stmt.setFloat(index++, tpn_fee);
		stmt.setString(index++, tpn_update_usr_id);
		stmt.setTimestamp(index++, tpn_update_timestamp);
		stmt.setLong(index++, tpn_id);
        stmt.executeUpdate();

		stmt.close();
		
	    String columnName[]={"tpn_introduction","tpn_aim","tpn_target","tpn_duration","tpn_remark"};
        String columnValue[]={tpn_introduction,tpn_aim,tpn_target,tpn_duration,tpn_remark};
        String condition = "tpn_id= " + tpn_id;
        cwSQL.updateClobFields(con, "tpTrainingPlan",columnName,columnValue, condition);
		return;
	}
    public boolean isLastUpd(Connection con, Timestamp upd_timestamp) throws SQLException ,cwSysMessage {
        boolean result;
        final String SQL = " Select tpn_update_timestamp From tpTrainingPlan "
                         + " Where tpn_id = ? ";

         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setLong(1, tpn_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next())
        	 tpn_update_timestamp = rs.getTimestamp("tpn_update_timestamp");
         else
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "tpn id = " + tpn_id);

         if(upd_timestamp == null || upd_timestamp == null)
            result = false;
         else {
            upd_timestamp.setNanos(tpn_update_timestamp.getNanos());
            if(upd_timestamp.equals(tpn_update_timestamp))
                result = true;
            else
                result = false;
         }
         stmt.close();
         return result;
    }
	public static final String get_tp_training_plan_sql = "SELECT tpn_id,tpn_tcr_id,tpn_date,tpn_code,tpn_name,tpn_cos_type,tpn_tnd_title,tpn_introduction,tpn_aim,tpn_target,tpn_responser,tpn_lrn_count,tpn_duration,tpn_wb_start_date,tpn_wb_end_date,tpn_ftf_start_date,tpn_ftf_end_date,tpn_type,tpn_fee,tpn_remark,tpn_status,tpn_approve_usr_id,tpn_approve_timestamp,tpn_create_usr_id,tpn_create_timestamp,tpn_update_usr_id,tpn_update_timestamp,tpn_submit_usr_id,tpn_submit_timestamp FROM tpTrainingPlan WHERE tpn_id = ? ";

	public void get(Connection con) throws SQLException {
		PreparedStatement stmt  = con.prepareStatement(get_tp_training_plan_sql);
		stmt.setLong(1, tpn_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			tpn_id = rs.getLong("tpn_id");
			tpn_tcr_id = rs.getLong("tpn_tcr_id");
			tpn_date = rs.getTimestamp("tpn_date");
			tpn_code = rs.getString("tpn_code");
			tpn_name = rs.getString("tpn_name");
			tpn_cos_type = rs.getString("tpn_cos_type");
			tpn_tnd_title = rs.getString("tpn_tnd_title");
			tpn_introduction = cwSQL.getClobValue(rs, "tpn_introduction");
			tpn_aim = cwSQL.getClobValue(rs, "tpn_aim");
			tpn_target =  cwSQL.getClobValue(rs, "tpn_target");
			tpn_responser = rs.getString("tpn_responser");
			tpn_lrn_count = rs.getLong("tpn_lrn_count");
			tpn_duration =  cwSQL.getClobValue(rs, "tpn_duration");
			tpn_wb_start_date = rs.getTimestamp("tpn_wb_start_date");
			tpn_wb_end_date = rs.getTimestamp("tpn_wb_end_date");
			tpn_ftf_start_date = rs.getTimestamp("tpn_ftf_start_date");
			tpn_ftf_end_date = rs.getTimestamp("tpn_ftf_end_date");
			tpn_type = rs.getString("tpn_type");
			tpn_fee = rs.getFloat("tpn_fee");
			tpn_remark =  cwSQL.getClobValue(rs, "tpn_remark");
			tpn_status = rs.getString("tpn_status");
			tpn_approve_usr_id = rs.getString("tpn_approve_usr_id");
			tpn_approve_timestamp = rs.getTimestamp("tpn_approve_timestamp");
			tpn_create_usr_id = rs.getString("tpn_create_usr_id");
			tpn_create_timestamp = rs.getTimestamp("tpn_create_timestamp");
			tpn_update_usr_id = rs.getString("tpn_update_usr_id");
			tpn_update_timestamp = rs.getTimestamp("tpn_update_timestamp");
			tpn_submit_usr_id = rs.getString("tpn_submit_usr_id");
			tpn_submit_timestamp = rs.getTimestamp("tpn_submit_timestamp");
		}
		stmt.close();
		return ;
	}
	
	static final String DEL_SQL = " DELETE FROM tpTrainingPlan WHERE tpn_id = ? ";	
	public void del(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			stmt = con.prepareStatement(DEL_SQL);
	        stmt.setLong(1, tpn_id);
	        stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return;
	}

	static final String UPDATE_STATUS_SQL = " UPDATE tpTrainingPlan  SET tpn_status = ?,  tpn_update_timestamp = ?, tpn_update_usr_id = ?, tpn_submit_timestamp =?, tpn_submit_usr_id = ?  WHERE tpn_id = ? ";
	public void upd_status(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(UPDATE_STATUS_SQL);
            int index = 1;
            stmt.setString(index++, tpn_status);
            stmt.setTimestamp(index++, tpn_update_timestamp);
            stmt.setString(index++, tpn_update_usr_id);
            stmt.setTimestamp(index++, tpn_submit_timestamp);
            stmt.setString(index++, tpn_submit_usr_id);
            stmt.setLong(index++, tpn_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	String get_status_sql = "Select tpn_status, tpn_type From tpTrainingPlan Where tpn_id = ? "; 
	public boolean hasImplementAccess(Connection con, boolean implement) throws SQLException {
        boolean flag = false;
        
        PreparedStatement stmt = con.prepareStatement(get_status_sql);
        int index=1;
        stmt.setLong(index++, tpn_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
        	tpn_status = rs.getString("tpn_status");
        	tpn_type = rs.getString("tpn_type");
        	if(implement) {
        		if(tpn_status.equalsIgnoreCase(TPN_STATUS_APPROVED)) {
        			flag = true;
        		}
        	}else if(tpn_type.equalsIgnoreCase(TPN_TYPE_MAKEUP)) {
	        	if(tpn_status.equalsIgnoreCase(TPN_STATUS_PREPARED) || tpn_status.equalsIgnoreCase(TPN_STATUS_DECLINED)) {
	        		flag = true;
	        	}
        	}
        }
        stmt.close();
        
        return flag;
	}

	String get_tcr_id = "Select tpn_tcr_id From tpTrainingPlan Where tpn_id = ? "; 
	public long getTcrIDbyTpnID(Connection con) throws SQLException {
        long tcr_id = 0;
        PreparedStatement stmt = con.prepareStatement(get_tcr_id);
        int index=1;
        stmt.setLong(index++, tpn_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
        	tcr_id = rs.getLong("tpn_tcr_id");
        }
        stmt.close();
        
        return tcr_id;
	}
	
    /** check last upd time first
     * @param con
     * @throws qdbException
     * @throws cwSysMessage
     */
    public void checkTimeStamp(Connection con) 
    throws  cwSysMessage,SQLException
		{
//		        PreparedStatement stmt = con.prepareStatement(
//		        " SELECT tpn_update_timestamp from tpTrainingPlan where tpn_id = ? " );
//		     
//		        stmt.setLong(1, tpn_id);
//		        ResultSet rs = stmt.executeQuery();
//		        boolean bTSOk = false;
//		        if(rs.next())
//		        {
//		            Timestamp t = rs.getTimestamp(1);
//		            Timestamp tTmp = t;
//		            tTmp.setNanos(tpn_update_timestamp.getNanos());
//		            if(tTmp.equals(tpn_update_timestamp))
//		                bTSOk = true;
//		        }
//		        stmt.close();
//		        if(!bTSOk) {
//		            con.rollback();
//		            throw new cwSysMessage("GEN006");
//		        }
		}
    
	public  String getPlanXml(Connection con, String status, String tcr_id_lst, cwPagination page , String current_role) throws SQLException{
		StringBuffer result = new StringBuffer();		
		String sql = "select tpn_id, tpn_tcr_id, tpn_code, tpn_name, tpn_status, tpn_update_timestamp, tpn_submit_timestamp, tcr_title "
				   + " from tpTrainingPlan, tcTrainingCenter "
				   + " where "
				   + " tpn_tcr_id = tcr_id "
				   + " and tpn_type = ? ";
		if(AccessControlWZB.isRoleTcInd(current_role)){
			sql += " and tcr_parent_tcr_id in " + tcr_id_lst;
		}
		if (status.equalsIgnoreCase("ALL")){
			sql += " and tpn_status in (?, ?, ?)";
		}else{
			sql += " and tpn_status = ? ";
		}
		if (page != null){
			if (page.sortCol == null) {
				page.sortCol ="tpn_submit_timestamp";
			}
			if (page.sortOrder == null) {
				page.sortOrder = "DESC";
			}
			
			if (page != null){
				if (page.pageSize <= 0) {
					page.pageSize = 10;
				}
				if (page.curPage <= 0) {
					page.curPage = 1;
				}			
			}
			sql += " Order BY " + page.sortCol;
			sql += " " + page.sortOrder;
		}
	    PreparedStatement stmt = con.prepareStatement(sql);
	    int index = 1;
	    stmt = con.prepareStatement(sql);
	    stmt.setString(index++, TPN_TYPE_MAKEUP);
	    if (status.equals("ALL")){
		    stmt.setString(index++, TPN_STATUS_APPROVED);
		    stmt.setString(index++, TPN_STATUS_DECLINED);
		    stmt.setString(index++, TPN_STATUS_PENDING);
	    }else if (status.equals(TPN_STATUS_APPROVED)){
		    stmt.setString(index++, TPN_STATUS_APPROVED);
	    }else if (status.equals(TPN_STATUS_DECLINED)){
		    stmt.setString(index++, TPN_STATUS_DECLINED);
	    }else if (status.equals(TPN_STATUS_PENDING)){
		    stmt.setString(index++, TPN_STATUS_PENDING);
	    }
		ResultSet rs = stmt.executeQuery();
        int count = 1;
	    result.append("<trainingplan_list");
	    if (status.equals("ALL")){
	    	result.append(" status=\"").append("\">");
	    }else{
	    	result.append(" status=\"").append(status).append("\">");
	    }
	    while(rs.next()){
	    	if ((count>(page.curPage-1)*page.pageSize) && (count<=page.curPage*page.pageSize)) {
		    	result.append("<trainingplan tpn_id=\"").append(rs.getLong("tpn_id"))
		    		.append("\" tpn_tcr_id=\"").append(rs.getLong("tpn_tcr_id"))
			    	.append("\" tpn_code=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("tpn_code"))))
			    	.append("\" tpn_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("tpn_name"))))
			    	.append("\" tcr_title=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("tcr_title"))))
			    	.append("\" tpn_status=\"").append(rs.getString("tpn_status"))
			    	.append("\" tpn_submit_timestamp=\"").append(rs.getTimestamp("tpn_submit_timestamp"))
			    	.append("\" tpn_update_timestamp=\"").append(rs.getTimestamp("tpn_update_timestamp"))
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
	
	static final String AUDITING_PLAN_SQL = " UPDATE tpTrainingPlan  SET tpn_status = ?,  tpn_approve_usr_id = ?, tpn_approve_timestamp = ?, tpn_update_timestamp = ?, tpn_update_usr_id = ?  WHERE tpn_id = ? ";
	public void auditingPlan(Connection con) throws SQLException{
        PreparedStatement stmt = null;
	    Timestamp cur_time = cwSQL.getTime(con);
        try {
            stmt = con.prepareStatement(AUDITING_PLAN_SQL);
            int index = 1;
            stmt.setString(index++, tpn_status);
            stmt.setString(index++, tpn_approve_usr_id);
            stmt.setTimestamp(index++, cur_time);            
            stmt.setTimestamp(index++, cur_time);
            stmt.setString(index++, tpn_update_usr_id);
            stmt.setLong(index++, tpn_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
	}
	
	public String getPlanStausCntXml(Connection con, String tcr_id_lst , String current_role) throws SQLException{
		StringBuffer result = new StringBuffer();
		String sqlCount = "select count(tpn_status) as ctn,tpn_status from tpTrainingPlan, tctrainingCenter "
			+ " where "
			+ " tpn_tcr_id = tcr_id "
			+ " and tpn_type = ? "
			+ " and tpn_status in (?, ?, ?) ";
		if(AccessControlWZB.isRoleTcInd(current_role)){
			sqlCount+=" and tcr_parent_tcr_id in " + tcr_id_lst ;
		}
			sqlCount += " group by tpn_status";
			
	    PreparedStatement stmt = con.prepareStatement(sqlCount);	    
	    int index = 1;
	    stmt.setString(index++, TPN_TYPE_MAKEUP);
	    stmt.setString(index++, TPN_STATUS_APPROVED);
	    stmt.setString(index++, TPN_STATUS_DECLINED);
	    stmt.setString(index++, TPN_STATUS_PENDING);
	    ResultSet rs = stmt.executeQuery();
		result.append("<process_status_cnt_list>");
		long totalcnt = 0;
		long cnt = 0;
	    while(rs.next()){
	    	cnt = rs.getLong("ctn");
			result.append("<process_status_cnt name=\"").append(rs.getString("tpn_status")).append("\" cnt=\"").append(cnt).append("\"/>");
			totalcnt +=cnt;
	    }
	    stmt.close();
	    result.append("<total_process_status_cnt cnt=\"").append(totalcnt).append("\"/>");
		result.append("</process_status_cnt_list>");
		return result.toString();
	}
    
    public static String getPlanSimpleXml(Connection con,  long tpn_id) throws SQLException{
        StringBuffer result = new StringBuffer();       
        String sql = "select tpn_id, tpn_tcr_id, tpn_code, tpn_name, tpn_pattern_ind, tpn_fee, tpn_lrn_count, tpn_status, tpn_update_timestamp "
                   + " from tpTrainingPlan "
                   + " where tpn_id = ?  ";
       
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt = con.prepareStatement(sql);
        stmt.setLong(index++, tpn_id);       
        ResultSet rs = stmt.executeQuery();        
        if(rs.next()){
            result.append("<trainingplan tpn_id=\"").append(rs.getLong("tpn_id"))
                .append("\" tpn_tcr_id=\"").append(rs.getLong("tpn_tcr_id"))
                .append("\" tpn_code=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("tpn_code"))))
                .append("\" tpn_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("tpn_name"))))
                .append("\" tpn_pattern_ind=\"").append(rs.getBoolean("tpn_pattern_ind"))
                .append("\" tpn_fee=\"").append(rs.getDouble("tpn_fee"))
                .append("\" tpn_lrn_count=\"").append(rs.getLong("tpn_lrn_count"))
                .append("\" tpn_status=\"").append(rs.getString("tpn_status"))
                .append("\" tpn_update_timestamp=\"").append(rs.getTimestamp("tpn_update_timestamp"))
                .append("\"/>");
        }    
        stmt.close();
        return result.toString();       
    }
	public long getTpPlanItemId(Connection con) throws SQLException {
		String sql = "select itm_id from aeItem where itm_plan_code = ? " ;
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, tpn_code);
		ResultSet rs = stmt.executeQuery();
		long itm_id = 0;
		if(rs.next()) {
			itm_id = rs.getLong("itm_id");
		}
		rs.close();
		stmt.close();
		return itm_id;
	}
	public long getTpPlanIdByCode(Connection con) throws SQLException {
		String sql = "select tpn_id from tpTrainingPlan where tpn_code = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, tpn_code);
		ResultSet rs = stmt.executeQuery();
		long tpn_id = 0;
		if(rs.next()) {
			tpn_id = rs.getLong("tpn_id");
		}
		rs.close();
		stmt.close();
		return tpn_id;
	}
}

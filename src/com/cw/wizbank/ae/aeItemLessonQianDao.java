package com.cw.wizbank.ae;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;
import com.sun.corba.se.pept.transport.ContactInfo;

public class aeItemLessonQianDao {
	
	
	public long ilsqd_id;
	public long ilsqd_usr_ent_id;
	public long ilsqd_ils_id;
	public long ilsqd_ils_itm_id;
	public long ilsqd_app_id;
	public int ilsqd_status; //  签到状态  1正常 2迟到 3缺勤
	public Timestamp ilsqd_date;
	public Timestamp ilsqd_create_timestamp;
	
	public static final int DEF_PAGESIZE = 10;
	
	
	private static final String SQL_QIANDAO_INSERT =
		"INSERT INTO aeitemlessonqiandao("
			+ "ilsqd_usr_ent_id, "
			+ "ilsqd_ils_id, "
			+ "ilsqd_ils_itm_id, "
			+ "ilsqd_app_id, "
			+ "ilsqd_status, "
			+ "ilsqd_date, "
			+ "ilsqd_create_timestamp )"
			+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * @throws SQLException
	 */
	public boolean ins(Connection con) throws SQLException {
		boolean result = false;
		int index = 1;
		PreparedStatement stmt = con.prepareStatement(SQL_QIANDAO_INSERT);
        try{
			stmt.setLong(index++, ilsqd_usr_ent_id);
			stmt.setLong(index++, ilsqd_ils_id);
			stmt.setLong(index++, ilsqd_ils_itm_id);
			stmt.setLong(index++, ilsqd_app_id);
			stmt.setLong(index++, ilsqd_status);
			stmt.setTimestamp(index++, ilsqd_date);
			stmt.setTimestamp(index++, ilsqd_create_timestamp);
			if (stmt.executeUpdate() == 1) {
				result = true;
	    	}
        }catch(SQLException e){
            stmt.close();
            //e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
        }
        con.commit();
		return result;
	}
	
	
	public String get_qiandao_lst(Connection con, long itm_id, long ils_id, cwPagination cwPage) throws SQLException{
		String result = "";
		String sql = " select ilsqd_usr_ent_id, ilsqd_date, ilsqd_status, usr_display_bil, ils_title, ils_start_time, ils_end_time "
					+ " from aeItemLessonQianDao inner join regUser on usr_ent_id = ilsqd_usr_ent_id "
					+ " inner join aeItemLesson on ils_id = ilsqd_ils_id where ilsqd_ils_itm_id = ? ";
		int index = 1;
		
		if(ils_id > 0){
			sql += " and ils_id = ? ";
		}
		
		if (cwPage.ts == null)
            cwPage.ts = cwSQL.getTime(con);

        if (cwPage.pageSize == 0){
            cwPage.pageSize = DEF_PAGESIZE;
        }
        if (cwPage.curPage == 0) {
            cwPage.curPage = 1;
        }
        
        if (cwPage.sortCol == null) {
            cwPage.sortCol = "ilsqd_date";
        }
        
        if (cwPage.sortOrder == null) {
            cwPage.sortOrder = "ASC";
        }
        
        
        sql +=" ORDER BY "+ cwPage.sortCol +" "+cwPage.sortOrder;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
        try{
        	stmt = con.prepareStatement(sql);
			stmt.setLong(index++, itm_id);
			if(ils_id > 0){
				stmt.setLong(index++, ils_id);
			}
			rs = stmt.executeQuery();
			result +="<aeitemlesson_qiandao_lst>";
			while(rs.next()) {
				if((count >= (cwPage.curPage-1) * cwPage.pageSize) && count < cwPage.curPage*cwPage.pageSize){
					result += "<usr id=\""+rs.getLong("ilsqd_usr_ent_id")+"\">";
					result += "<usr_name>"+rs.getString("usr_display_bil")+"</usr_name>";
					result += "<qiandao_date>"+cwUtils.format2ymdhms(rs.getTimestamp("ilsqd_date"))+"</qiandao_date>";
					result += "<qiandao_status>"+rs.getString("ilsqd_status")+"</qiandao_status>";
					result += "<ils_title>"+rs.getString("ils_title")+"</ils_title>";
					result += "<ils_start_time>"+cwUtils.format2ymdhms(rs.getTimestamp("ils_start_time"))+"</ils_start_time>";
					result += "<ils_end_time>"+cwUtils.format2ymdhms(rs.getTimestamp("ils_end_time"))+"</ils_end_time>";
					result += "</usr>";
				}
				count++;
	    	}
			result += "</aeitemlesson_qiandao_lst>";
        }catch(SQLException e){
            stmt.close();
            CommonLog.error(e.getMessage(),e);
        }
        cwPage.totalRec = count;
        if (cwPage.pageSize != 0){
            cwPage.totalPage = count / cwPage.pageSize;
            if (count % cwPage.pageSize != 0){
                cwPage.totalPage++;
            }
        }
        result+=cwPage.asXML();
		return result;
	}
	
	/**
	 * @throws SQLException
	 */
	public boolean chk_shifo_qiandao(Connection con) throws SQLException {
		boolean result = false;
		String sql ="select * from aeitemlessonqiandao where ilsqd_ils_id = ? and ilsqd_ils_itm_id = ? and ilsqd_usr_ent_id =? ";
		int index = 1;
		PreparedStatement stmt = null;
		ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
			stmt.setLong(index++, ilsqd_ils_id);
			stmt.setLong(index++, ilsqd_ils_itm_id);
			stmt.setLong(index++, ilsqd_usr_ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				result = true;
	    	}
        }catch(SQLException e){
            stmt.close();
           // e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
        }
		return result;
	}
	/**
	 * 获取签到状态
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int chk_shifo_qiandao_ilsqd_status(Connection con) throws SQLException {
		int result = 0;
		String sql ="select ilsqd_status from aeitemlessonqiandao where ilsqd_ils_id = ? and ilsqd_ils_itm_id = ? and ilsqd_usr_ent_id =? ";
		int index = 1;
		PreparedStatement stmt = null;
		ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
			stmt.setLong(index++, ilsqd_ils_id);
			stmt.setLong(index++, ilsqd_ils_itm_id);
			stmt.setLong(index++, ilsqd_usr_ent_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("ilsqd_status");
	    	}
        }catch(SQLException e){
            stmt.close();
           // e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
        }
		return result;
	}
	
	public Map<String, String> qianDao(Connection con, loginProfile prof, long ils_id){
		Map<String, String> map = new HashMap<String, String>();
		String msg = LangLabel.getValue(prof.cur_lan, "qiandao_time_out");
		String status = "prompt";
		try {
			long itm_id = aeItemLesson.getIlsItmId(con, ils_id);
			aeItem item = new aeItem();
			item.itm_id = itm_id;
			
			if(item.isItemOff(con)){
				msg = LangLabel.getValue(prof.cur_lan, "AEIT27");
			} else {
				boolean isItemOff = false;
				aeItemRelation ire = new aeItemRelation();
	            ire.ire_child_itm_id = itm_id;
	            ire.ire_parent_itm_id = ire.getParentItemId(con);
	            
	            if(ire.ire_parent_itm_id > 0){
	            	item.itm_id = ire.ire_parent_itm_id;
	            	isItemOff = item.isItemOff(con);
	            }
	            
	            if(isItemOff){
	            	msg = LangLabel.getValue(prof.cur_lan, "AEIT27");
	            } else {
					long app_id = aeApplication.getAppIdByAppStatus(con, itm_id, prof.usr_ent_id, aeApplication.ADMITTED);
					Timestamp ts = cwSQL.getTime(con);
					//判断是否报名该课程
					if(app_id>0){
						Timestamp nowTime = cwSQL.getTime(con);
						aeItemLesson ils = new aeItemLesson();
						Vector<aeItemLesson> ils_vec = ils.getRunList(con, ils_id, true);
						for (aeItemLesson aeIls : ils_vec) {
							if(aeIls.ils_id != ils_id){
								continue;
							}
							aeItemLessonQianDao ils_qd = new aeItemLessonQianDao();
							ils_qd.ilsqd_app_id = app_id;
							ils_qd.ilsqd_create_timestamp = ts;
							ils_qd.ilsqd_date = ts;
							ils_qd.ilsqd_ils_itm_id = itm_id;
							ils_qd.ilsqd_usr_ent_id = prof.usr_ent_id;
							ils_qd.ilsqd_ils_id = aeIls.ils_id;
							
							if(aeIls.ils_qiandao == 0 || aeIls.ils_end_time.before(nowTime)){
								continue;
							}
							
							//判断针对该日程是否已经签到过
							if(!ils_qd.chk_shifo_qiandao(con)){
								//判断签到有效期  当前时间在 日程结束时间之前 在有效签到时间之后
								if(aeIls.ils_qiandao_youxiaoqi_time.equals(nowTime)||((aeIls.ils_end_time.after(nowTime) && aeIls.ils_qiandao_youxiaoqi_time.before(nowTime)))){
									//判断是否签到    当前时间在 有效签到时间之后 在开始时间之前
									if((aeIls.ils_qiandao_youxiaoqi_time.equals(nowTime) || aeIls.ils_qiandao_chidao_time.equals(nowTime))
											||(aeIls.ils_qiandao_youxiaoqi_time.before(nowTime) && aeIls.ils_qiandao_chidao_time.after(nowTime))){
										ils_qd.ilsqd_status = 1;
										if(ils_qd.ins(con)){
											ils_qd.upd_chuxilv(con, ils_qd.ilsqd_app_id, ils_qd.ilsqd_usr_ent_id, ils_qd.ilsqd_ils_itm_id, ils_vec.size());
											dbCourseEvaluation.updCourseEvaluation(con, prof, itm_id, app_id);
											msg = LangLabel.getValue(prof.cur_lan, "qiandao_ok");//"您已完成签到!";
											status = "proper";
											break;
										}else{
											msg = LangLabel.getValue(prof.cur_lan, "qiandao_fail");//"签到失败，请与管理员联系!";
											status = "prompt";
											break;
										}
									}
									//判断是否迟到    当前时间在 日程迟到时间之后 在 缺勤时间之前
									if((aeIls.ils_qiandao_queqin_time.equals(nowTime))
											||(aeIls.ils_qiandao_chidao_time.before(nowTime) && aeIls.ils_qiandao_queqin_time.after(nowTime))){
										ils_qd.ilsqd_status = 2;
										if(ils_qd.ins(con)){
											ils_qd.upd_chuxilv(con, ils_qd.ilsqd_app_id, ils_qd.ilsqd_usr_ent_id, ils_qd.ilsqd_ils_itm_id, ils_vec.size());
											dbCourseEvaluation.updCourseEvaluation(con, prof, itm_id, app_id);
											msg = LangLabel.getValue(prof.cur_lan, "qiandao_late");//"您已迟到!";
											status = "proper";
											break;
										}else{
											msg = LangLabel.getValue(prof.cur_lan, "qiandao_fail");//"签到失败，请与管理员联系!";
											status = "prompt";
											break;
										}
										
									}
									//判断是否缺勤    当前时间在 缺勤时间之后 在日程结束时间之前
									if((aeIls.ils_end_time.equals(nowTime))
											||(aeIls.ils_qiandao_queqin_time.before(nowTime) && aeIls.ils_end_time.after(nowTime))){
										ils_qd.ilsqd_status = 3;
										if(ils_qd.ins(con)){
											msg = LangLabel.getValue(prof.cur_lan, "qiandao_absence");//"您已缺勤!";
											status = "proper";
											break;
										}else{
											msg = LangLabel.getValue(prof.cur_lan, "qiandao_fail");//"签到失败，请与管理员联系!";
											status = "prompt";
											break;
										}
									}
								}else{
									SimpleDateFormat df = new SimpleDateFormat(LangLabel.getValue(prof.cur_lan, "date_format"));
									SimpleDateFormat tf = new SimpleDateFormat(LangLabel.getValue(prof.cur_lan, "time_format"));
									msg = LangLabel.getValue(prof.cur_lan, "qiandao_uneffect_time").replaceFirst("\\$data", df.format(aeIls.ils_qiandao_youxiaoqi_time)).replaceFirst("\\$data", tf.format(aeIls.ils_end_time));
									status = "prompt";
									break;
								}
							} else if(aeIls.ils_qiandao_youxiaoqi_time.equals(nowTime)||((aeIls.ils_end_time.after(nowTime) && aeIls.ils_qiandao_youxiaoqi_time.before(nowTime)))){
								//重复签到判断是否完成签到、迟到、缺勤
								int qiandao_status = ils_qd.chk_shifo_qiandao_ilsqd_status(con);
								if(qiandao_status == 1){
									msg = LangLabel.getValue(prof.cur_lan, "qiandao_ok");//"您已完成签到!";
								}else if(qiandao_status == 2){
									msg = LangLabel.getValue(prof.cur_lan, "qiandao_late");//"您已迟到!";
								}else if(qiandao_status == 3){
									msg = LangLabel.getValue(prof.cur_lan, "qiandao_absence");//"您已缺勤!";
								}
								
								status = "proper";
								break;
							}							
						}
					}else{
						msg = LangLabel.getValue(prof.cur_lan, "qiandao_no_apply");//"您没有报名该课程，无须签到！";
						status = "prompt";
					}
	            }
			}
			
		} catch (SQLException e) {
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} catch (cwSysMessage e) {
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		} catch (cwException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (qdbException e) {
			CommonLog.error(e.getMessage(),e);
		} catch (com.cw.wizbank.qdb.qdbErrMessage e) {
			CommonLog.error(e.getMessage(),e);
		}
		map.put("msg", msg);
		map.put("status", status);
		return map;
	}
	
	//更新学员出席率
	public void upd_chuxilv(Connection con, long app_id, long usr_ent_id, long itm_id, long total_qiandao) throws SQLException {
		String sql_1 = "select * from aeitemlessonqiandao where ilsqd_ils_itm_id = ? and ilsqd_usr_ent_id = ? and ilsqd_status < 3";
		String sql_2 = "update  aeAttendance set  att_rate = ?  where att_app_id = ? and att_itm_id = ?";
		int index = 1;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0 ;
        try{
        	stmt = con.prepareStatement(sql_1);
			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, usr_ent_id);			
			rs = stmt.executeQuery();
			while(rs.next()) {
				count++;
	    	}
			index = 1;
			String chuxilv = accuracy(count,total_qiandao,2);
			stmt = con.prepareStatement(sql_2);
			stmt.setString(index++, chuxilv);
			stmt.setLong(index++, app_id);
			stmt.setLong(index++, itm_id);
			stmt.executeUpdate();
        }catch(SQLException e){
            stmt.close();
            //e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
        }
        
        
	}
	
	public static void main(String[] args) {
		System.out.println(accuracy(1, 1, 2));
	}
	public static String accuracy(double num, double total, int scale){  
        DecimalFormat df = (DecimalFormat)NumberFormat.getInstance();  
        //可以设置精确几位小数  
        df.setMaximumFractionDigits(scale);  
        //模式 例如四舍五入  
        df.setRoundingMode(RoundingMode.HALF_UP);  
        double accuracy_num = num / total * 100;  
        return df.format(accuracy_num);  
	}

	public void removeSigninRecord(Connection con, long[] app_id_lst) throws SQLException {
		PreparedStatement stmt = null;
		StringBuffer sql = new StringBuffer();
		sql.append("delete from aeItemLessonQianDao where ilsqd_app_id in (");
		for(int i=0; i<app_id_lst.length; i++) {
			if(i != app_id_lst.length - 1) {
				sql.append("? ,");
			} else {
				sql.append("?");
			}
		}
		sql.append(")");
		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			for(int i=0; i<app_id_lst.length; i++) {
				stmt.setLong(index++, app_id_lst[i]);
			}
			stmt.executeUpdate();
		} catch (SQLException e) {
			stmt.close();
            CommonLog.error(e.getMessage(),e);
		}
	}
}

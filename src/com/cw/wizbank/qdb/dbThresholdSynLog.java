package com.cw.wizbank.qdb;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.cw.wizbank.enterprise.IMSEnterprise;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.LoginLog;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.systemLog.service.LoginActionLogService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.SpringContextUtil;



public class dbThresholdSynLog {
	
	public final static String LOG_TYPE_THRESHOLD = "Perf_Warning_Log";
	public final static String LOG_TYPE_DATA_INTEGRATION = "Data_Integration_Log";
	public final static String LAB_LOG_TYPE_THRESHOLD = "Performance_Warning_Log";
	public final static String LAB_LOG_TYPE_DATA_INTEGRATION = "Data_Integration_Log";
	public final static String LAB_LOG_USER_LOGIN = "User_Login_Log";
	public final static String LAB_LOG_USER_OPERATION = "User_Operation_Log";
	SimpleDateFormat sdf_all=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
	SimpleDateFormat sdf_notime=new SimpleDateFormat("yyyy-MM-dd");	
	private String startdate;
	private String enddate;
	private String nowdate;
	private Timestamp beforedate;
	
	private void getTheDayBefore(Timestamp starttime,Timestamp endtime,int lastdays){
		enddate="";
		nowdate="";
		startdate="";
		if(lastdays>=0 &&(starttime==null &&endtime==null)){
        	Calendar  cal=Calendar.getInstance();
        	this.nowdate=sdf_all.format(new Date());
        	try {
				cal.setTime(sdf_all.parse(nowdate));
			} catch (ParseException e) {
				CommonLog.error(e.getMessage(),e);
			}   
        	this.enddate=this.nowdate;
            cal.add(Calendar.DATE,-lastdays);   
            Date d=cal.getTime(); 
            String tempstr=sdf_notime.format(d).toString()+" 00:00:00.000";
            this.startdate=sdf_notime.format(d);
            this.beforedate=Timestamp.valueOf(tempstr);//sdf_notime.format(d);
        }else{
        	if(starttime!=null){
        		this.startdate=starttime.toString();
        	}
        	if(endtime!=null){
        		this.enddate=endtime.toString();
        	}
        }
	}
	public void  getThresholdLog(Connection con,int lastdays,Timestamp starttime,Timestamp endtime,boolean select_all, PrintWriter pw, String labellang)throws SQLException{
		labellang="ISO-8859-1";//english label
		String reportTitle=LangLabel.getValue(labellang, "lab_threshold_log_title");
		String lab_user_id=LangLabel.getValue(labellang, "lab_user_id");
		String lab_type=LangLabel.getValue(labellang, "lab_threshold_log_type");
    	String lab_date=LangLabel.getValue(labellang, "lab_threshold_log_date");
    	String lab_from=LangLabel.getValue(labellang, "lab_from");
    	String lab_to=LangLabel.getValue(labellang, "lab_to");

    	getTheDayBefore(starttime,endtime,lastdays);     
    	/*pw.write(reportTitle+"\n");
    	pw.write(lab_date+",");
    	if(select_all==true){
    		pw.write("All"+"\n");
    	}else{
    		pw.write(lab_from);    
	    	pw.write(this.startdate);
	    	pw.write(","+lab_to);
	    	pw.write(this.enddate);
	    	pw.write("\n");
    	}
    	
    	*/
    	pw.write(lab_user_id+","+lab_type+","+lab_date+"\n");
		
		String sql="select usr_ste_usr_id,ult_type,ult_login_timestamp from UserLoginTracking left join reguser on(usr_ent_id=ult_usr_ent_id)";
		if(select_all==false){
			if(lastdays>=0&&(starttime==null &&endtime==null)){
				sql+=" where ult_login_timestamp >= ?";
			}else{
				sql+=" where 1=1";
				if(starttime!=null){
					sql+=" and ult_login_timestamp >= ? ";
				}
				if(endtime!=null){
					sql+=" and ult_login_timestamp <= ? ";
				}
			}
		}
		sql+=" order by ult_login_timestamp desc";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	if(select_all==false){
    		if(lastdays>=0&&(starttime==null &&endtime==null)){
    			stmt.setTimestamp(1, this.beforedate);
    		}else{    	
				int i=0;
				if(starttime!=null){
					stmt.setTimestamp(++i, starttime);
				}
				if(endtime!=null){
					stmt.setTimestamp(++i, endtime);
				}
			}
    	}
    	ResultSet rs = stmt.executeQuery();
    	long count =0;
    	StringBuffer result=new StringBuffer();
    	while(rs.next()){
    		result.append(rs.getString(1)).append(",").append(rs.getString(2)).append(",").append(rs.getTimestamp(3).toString()).append("\n");
    		if (count % 500 == 0) {
    			pw.write(result.toString());
    			if (result.length() > 0) {
    	    		result.delete(0, result.length());
    	    	}
    		}
    		count++;
    	}
    	if (result.length() > 0) {
    		pw.write(result.toString());
    		result.delete(0, result.length());
    	}
    	rs.close();
    	stmt.close();
	}
	
	private StringBuffer getSqlCondition(Date starttime, Date endtime, int lastdays){
		if(lastdays>=0 &&(starttime==null &&endtime==null)){
        	Calendar  cal=Calendar.getInstance();
        	try {
				cal.setTime(sdf_all.parse(nowdate));
			} catch (ParseException e) {
				CommonLog.error(e.getMessage(),e);
			}   
            cal.add(Calendar.DATE,-lastdays); 
            starttime = cal.getTime();
		}
		if(endtime == null){
			endtime =DateUtil.getInstance().getDate();
		}
		if(starttime == null){
			Calendar  cEndtime = Calendar.getInstance();
			cEndtime.add(Calendar.MONTH,-12); 
			starttime = cEndtime.getTime();
		}
		StringBuffer sql = new StringBuffer();
		Calendar  cStarttime = Calendar.getInstance();
		Calendar  cEndtime = Calendar.getInstance();
		cStarttime.setTime(starttime);
		cEndtime.setTime(endtime);
		LoginActionLogService loginActionLogService = (LoginActionLogService) SpringContextUtil.getBean("loginActionLogService"); 
    	if(cStarttime.compareTo(cEndtime) < 0){
    		cEndtime.add(Calendar.MONTH,-1);
    		String tableName = loginActionLogService.getTableName(endtime);
    		if(loginActionLogService.existTable(tableName) > 0){
    			return sql.append(tableName).append(',').append(getSqlCondition(cStarttime.getTime(),cEndtime.getTime(),0));
    		}
    	}
		
		return sql;
	}
	
	public void  getUserLoginLog(Connection con,int lastdays,Timestamp starttime,Timestamp endtime,boolean select_all, PrintWriter pw, String labellang)throws SQLException{
		//labellang="ISO-8859-1";//english label
		
		String usr_display_bil=LabelContent.get(labellang, "label_core_system_setting_161");
		String usr_full_name_bil=LabelContent.get(labellang, "label_core_system_setting_162");
		String login_mode=LabelContent.get(labellang, "label_core_system_setting_163");
    	String login_time=LabelContent.get(labellang, "label_core_system_setting_164");

    	getTheDayBefore(starttime,endtime,lastdays);     
    	
    	pw.write(usr_display_bil+","+usr_full_name_bil+","+login_mode+","+login_time+"\n");
		
    	String sqlCondition = getSqlCondition(starttime , endtime, lastdays).toString();
		String sql="select usr_display_bil,usr_full_name_bil,login_mode,login_time from  "+sqlCondition.substring(0, sqlCondition.length()-1);
		if(select_all==false){
			if(lastdays>=0&&(starttime==null &&endtime==null)){
				sql+=" where login_time >= ?";
			}else{
				sql+=" where 1=1";
				if(starttime!=null){
					sql+=" and login_time >= ? ";
				}
				if(endtime!=null){
					sql+=" and login_time <= ? ";
				}
			}
		}
		sql+=" order by login_time desc";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	if(select_all==false){
    		if(lastdays>=0&&(starttime==null &&endtime==null)){
    			stmt.setTimestamp(1, this.beforedate);
    		}else{    	
				int i=0;
				if(starttime!=null){
					stmt.setTimestamp(++i, starttime);
				}
				if(endtime!=null){
					stmt.setTimestamp(++i, endtime);
				}
			}
    	}
    	ResultSet rs = stmt.executeQuery();
    	long count =0;
    	StringBuffer result=new StringBuffer();
    	DateUtil dateUtil = new DateUtil();
    	while(rs.next()){
    		result.append(rs.getString(1)).append(",").append(rs.getString(2)).append(",");
    		if(rs.getInt(3) == LoginLog.MODE_PC){
    			result.append(LabelContent.get(labellang, "label_core_system_setting_174"));
    		}else if(rs.getInt(3) == LoginLog.MODE_APP){
    			result.append(LabelContent.get(labellang, "label_core_system_setting_175"));
    		}else if(rs.getInt(3) == LoginLog.MODE_WECHAT){
    			result.append(LabelContent.get(labellang, "label_core_system_setting_176"));
    		}
    		Date date = rs.getTimestamp(4);
    		result.append(",").append(dateUtil.dateToString(date,DateUtil.patternE)).append("\n");
    		if (count % 500 == 0) {
    			pw.write(result.toString());
    			if (result.length() > 0) {
    	    		result.delete(0, result.length());
    	    	}
    		}
    		count++;
    	}
    	if (result.length() > 0) {
    		pw.write(result.toString());
    		result.delete(0, result.length());
    	}
    	rs.close();
    	stmt.close();
	}
	
	public void  getUserOperationLog(Connection con,int lastdays,Timestamp starttime,Timestamp endtime,boolean select_all, PrintWriter pw, String labellang)throws SQLException{
		
		String title_or_name=LabelContent.get(labellang, "label_core_system_setting_165");
		String usr_id=LabelContent.get(labellang, "label_core_system_setting_166");
		String object_type=LabelContent.get(labellang, "label_core_system_setting_167");
    	String object_id=LabelContent.get(labellang, "label_core_system_setting_168");
    	String object_action=LabelContent.get(labellang, "label_core_system_setting_169");
		String object_action_type=LabelContent.get(labellang, "label_core_system_setting_170");
		String object_action_time=LabelContent.get(labellang, "label_core_system_setting_171");
    	String opt_user_account=LabelContent.get(labellang, "label_core_system_setting_172");
    	String opt_user_full_name=LabelContent.get(labellang, "label_core_system_setting_173");
    	
    	getTheDayBefore(starttime,endtime,lastdays);     
    	
    	pw.write(title_or_name+","+usr_id+","+object_type+","+object_id+","+object_action+","+object_action_type+","+object_action_time+","+opt_user_account+","+opt_user_full_name+"\n");
		
    	
    	   	
		String sql="select object_code,object_title,object_type,object_id,object_action,object_action_type,object_action_time,usr_ste_usr_id,usr_display_bil"; 
		    sql +=	" from objectActionLog";
		    sql +=	" inner join RegUser";
		    sql +=	" on usr_ent_id = object_opt_user_id";
		if(select_all==false){
			if(lastdays>=0&&(starttime==null &&endtime==null)){
				sql+=" where object_action_time >= ?";
			}else{
				sql+=" where 1=1";
				if(starttime!=null){
					sql+=" and object_action_time >= ? ";
				}
				if(endtime!=null){
					sql+=" and object_action_time <= ? ";
				}
			}
		}
		sql+=" order by object_action_time desc";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	if(select_all==false){
    		if(lastdays>=0&&(starttime==null &&endtime==null)){
    			stmt.setTimestamp(1, this.beforedate);
    		}else{    	
				int i=0;
				if(starttime!=null){
					stmt.setTimestamp(++i, starttime);
				}
				if(endtime!=null){
					stmt.setTimestamp(++i, endtime);
				}
			}
    	}
    	ResultSet rs = stmt.executeQuery();
    	long count =0;
    	StringBuffer result=new StringBuffer();
    	DateUtil dateUtil = new DateUtil();
    	while(rs.next()){
    		ObjectActionLog log = new ObjectActionLog();
    		log.setObjectCode(rs.getString(1));
    		log.setObjectTitle(rs.getString(2));
    		log.setObjectType(rs.getString(3));
    		log.setObjectId(rs.getLong(4));
    		log.setObjectAction(rs.getString(5));
    		log.setObjectActionType(rs.getString(6));
    		log.setObjectActionTime(rs.getTimestamp(7));
    		log.setOptUserAccount(rs.getString(8));
    		log.setOptUserFullName(rs.getString(9));
    		
    		result.append(log.getObjectTitle()).append(",");
    		result.append(log.getObjectCode()).append(",");
    		result.append(this.getObjectType(labellang, log.getObjectType())).append(",");
    		result.append(String.valueOf(log.getObjectId())).append(",");
    		result.append(this.getObjectAction(labellang, log.getObjectAction())).append(",");
    		result.append(this.getObjectActionType(labellang, log.getObjectActionType())).append(",");
    		result.append(dateUtil.dateToString(log.getObjectActionTime(),DateUtil.patternE)).append(",");
    		result.append(log.getOptUserAccount()).append(",");
    		result.append(log.getOptUserFullName()).append("\n");
    		
    		if (count % 500 == 0) {
    			pw.write(result.toString());
    			if (result.length() > 0) {
    	    		result.delete(0, result.length());
    	    	}
    		}
    		count++;
    	}
    	if (result.length() > 0) {
    		pw.write(result.toString());
    		result.delete(0, result.length());
    	}
    	rs.close();
    	stmt.close();
	}
	
	public void  getDataIntegrationLog(Connection con,int lastdays,Timestamp starttime,Timestamp endtime,boolean select_all, PrintWriter pw, String labellang)throws SQLException, UnsupportedEncodingException{
		labellang="ISO-8859-1";//english label
		String lab_fail=LangLabel.getValue(labellang, "lab_syn_fail");
		String lab_success=LangLabel.getValue(labellang, "lab_syn_success");
		String lab_type=LangLabel.getValue(labellang, "lab_syn_type");
		String lab_start_date=LangLabel.getValue(labellang, "lab_syn_start_date");
		String lab_end_date=LangLabel.getValue(labellang, "lab_syn_end_date");
		String lab_remark=LangLabel.getValue(labellang, "lab_syn_remark");
		getTheDayBefore(starttime,endtime,lastdays);
		pw.write(lab_start_date+","+lab_end_date+","+lab_success+","+lab_fail+","+lab_type+","+lab_remark+"\n");
		
		String sql="select dil_start_timestamp,dil_end_timestamp,dil_succ_count,dil_fail_count,dil_type,dil_remark from DataImportLog ";
    	StringBuffer result=new StringBuffer();
    	if(select_all==false){
			if(lastdays>=0&&(starttime==null &&endtime==null)){
	    			sql+="  where dil_start_timestamp >= ?";
	    		}else{
	    			sql +=" where 1=1";
	    			if(starttime!=null){
	    				sql+="  and dil_start_timestamp >= ?";
	    			}
	    			if(endtime!=null){
	    				sql+=" and dil_start_timestamp  <= ?";
	    			}
	    		}
    	}
    	sql+=" order by dil_start_timestamp desc";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	if(select_all==false){
    		if(lastdays>=0&&(starttime==null &&endtime==null)){
    			stmt.setTimestamp(1,  this.beforedate);
    		}else{ 
				int i=0;
				if(starttime!=null ){
					i++;
					stmt.setTimestamp(i, starttime);
				}
				if(endtime!=null){
					i++;
					stmt.setTimestamp(i, endtime);
				}
			}
    	}
    	ResultSet rs = stmt.executeQuery();
    	long count =0;
    	while(rs.next()){
    		result.append(rs.getTimestamp(1).toString()).append(",").append(rs.getTimestamp(2).toString()).append(",").append(rs.getString(3)).append(",").append(rs.getString(4)).append(",").append(rs.getString(5)).append(",");
    		String remarkstr="";
    		if(rs.getString(6).equalsIgnoreCase(IMSEnterprise.TYPE_REMARK_USER)){
    			remarkstr=LangLabel.getValue(labellang, "lab_syn_remark_user");
    		}else if(rs.getString(6).equalsIgnoreCase(IMSEnterprise.TYPE_REMARK_ENROLL)){
    			remarkstr=LangLabel.getValue(labellang, "lab_syn_remark_enroll");
    		}else if(rs.getString(6).equalsIgnoreCase(IMSEnterprise.TYPE_REMARK_COURCE)){
    			remarkstr=LangLabel.getValue(labellang, "lab_syn_remark_cource");
    		}
    		result.append(remarkstr).append("\n");
    		if (count % 500 == 0) {
    			pw.write(result.toString());
    			if (result.length() > 0) {
    	    		result.delete(0, result.length());
    	    	}
    		}
    		count++;
    	}
    	if (result.length() > 0) {
    		pw.write(result.toString());
    		result.delete(0, result.length());
    	}
    	rs.close();
    	stmt.close();
	}
	public void  insDataIntegrationLog(Connection con, String type , Timestamp starttime,Timestamp endtime,int successnum ,int failnum , String remark)throws SQLException{
		String sql="insert into DataImportLog values(?,?,?,?,?,?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setTimestamp(1, starttime);
		stmt.setTimestamp(2, endtime);
		stmt.setString(3, type);
		stmt.setInt(4, successnum);
		stmt.setInt(5, failnum);
		stmt.setString(6, remark);
		stmt.executeUpdate();
		stmt.close();
		con.commit();
	}
	
	private String getObjectType(String lang , String val){
		if(StringUtils.isEmpty(val)){
			return null;
		}
		String type = null;
		
		if(ObjectActionLog.OBJECT_TYPE_USR.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_177");
		}else if(ObjectActionLog.OBJECT_TYPE_GRP.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_178");
		}else if(ObjectActionLog.OBJECT_TYPE_UPT.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_179");
		}else if(ObjectActionLog.OBJECT_TYPE_UGR.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_180");
		}else if(ObjectActionLog.OBJECT_TYPE_COS.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_181");
		}else if(ObjectActionLog.OBJECT_TYPE_CLASS.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_182");
		}else if(ObjectActionLog.OBJECT_TYPE_CC.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_183");
		}else if(ObjectActionLog.OBJECT_TYPE_CREDITS.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_184");
		}else if(ObjectActionLog.OBJECT_TYPE_KB.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_185");
		}else if(ObjectActionLog.OBJECT_TYPE_EL.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_186");
		}else if(ObjectActionLog.OBJECT_TYPE_VT.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_187");
		}else if(ObjectActionLog.OBJECT_TYPE_AN.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_188");
		}else if(ObjectActionLog.OBJECT_TYPE_INFO.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_189");
		}else if(ObjectActionLog.OBJECT_TYPE_TC.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_202");
		}else if(ObjectActionLog.OBJECT_TYPE_ONLINE_EXAM.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_207");
		}else if(ObjectActionLog.OBJECT_TYPE_OFFLINE_EXAM.equalsIgnoreCase(val)){
			type = LabelContent.get(lang, "label_core_system_setting_208");
		}
		return type;
	}
	
	private String getObjectAction(String lang , String val){
		if(StringUtils.isEmpty(val)){
			return null;
		}
		String action = null;
		
		if(ObjectActionLog.OBJECT_ACTION_ADD.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_190");
		}else if(ObjectActionLog.OBJECT_ACTION_UPD.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_191");
		}else if(ObjectActionLog.OBJECT_ACTION_ACTIVE.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_192");
		}else if(ObjectActionLog.OBJECT_ACTION_DEL.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_193");
		}else if(ObjectActionLog.OBJECT_ACTION_UPD_PWD.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_194");
		}else if(ObjectActionLog.OBJECT_ACTION_PUB.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_195");
		}else if(ObjectActionLog.OBJECT_ACTION_CANCLE_PUB.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_196");
		}else if(ObjectActionLog.OBJECT_ACTION_APPR.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_197");
		}else if(ObjectActionLog.OBJECT_ACTION_CANCEL_APPR.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_198");
		}else if(ObjectActionLog.OBJECT_ACTION_RESTORE.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_206");
		}
		return action;
	}
	
	private String getObjectActionType(String lang , String val){
		if(StringUtils.isEmpty(val)){
			return null;
		}
		String action = null;

		if(ObjectActionLog.OBJECT_ACTION_TYPE_WEB.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_199");
		}else if(ObjectActionLog.OBJECT_ACTION_TYPE_BATCH.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_200");
		}else if(ObjectActionLog.OBJECT_ACTION_TYPE_IMPORT.equalsIgnoreCase(val)){
			action = LabelContent.get(lang, "label_core_system_setting_201");
		}	

		return action;
	}

}

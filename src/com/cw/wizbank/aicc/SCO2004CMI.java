package com.cw.wizbank.aicc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbModuleEvaluationHistory;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;


public class SCO2004CMI extends HttpServlet
{
    private static final String delim="_XvYvX_";
    private static Logger debugLogger = null;
    public static final String SCORM_VERSION_2004 = "SCORM2004";
    
    private static WizbiniLoader wizbini = null;
    PrintWriter out = null;
    Timestamp cur_time= null;
   
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
        	CommonLog.info("SCO2004CMI.init() START...");
            wizbini = WizbiniLoader.getInstance(config);
            
            // initialize debug logger
            /*
            String logdir = qdbAction.static_env.logFolderPath + dbUtils.SLASH + cwUtils.WIZBANK_LOG_FOLDER + dbUtils.SLASH;
            File dir = new File(logdir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            System.setProperty("log_path", logdir);
            */
            System.setProperty("file_encoding", wizbini.cfgSysSetupadv.getEncoding());
            debugLogger = Logger.getLogger(this.getClass().getName() + ".log");
            PropertyConfigurator.configure(wizbini.getCfgFileLog4jDir());
            
            CommonLog.info("SCO2004CMI.init() END");
        } catch (cwException e) {
            CommonLog.error("init() exception :" + e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        debugLogger.debug(" ");
        debugLogger.debug("-------------scorm 2004 request start---------------");
    	 out = response.getWriter();
    	 long fist_mod_id = 0;
    	 Connection con = null;
    	 try {
             cwSQL sqlCon = new cwSQL();
             sqlCon.setParam(wizbini);
             con = sqlCon.openDB(false);
             cur_time = dbUtils.getTime(con);
             debugLogger.debug("op=" +request.getParameter("op"));
             String uid_ = request.getParameter("uid");
             String cid_ = request.getParameter("cid");
             String iid = request.getParameter("iid");
             String postData = request.getParameter("args");
             debugLogger.debug("uid:--- "+uid_);
             debugLogger.debug("cid:--- "+cid_);
             debugLogger.debug("iid:--- "+iid);
             
             //检查传过来的跟踪ID格式是否正确
             long tkh_id=0;
             long mod_id = 0;
             try{
                 tkh_id = Long.parseLong(uid_);
                 mod_id = Long.parseLong(cid_);
             }catch(Exception e){
                 debugLogger.debug("ERROR: uid or cid error!");
                 return;
             }
             if(tkh_id <1 || mod_id < 1){
                 debugLogger.debug("ERROR: uid or cid error!");
                 return;
             }
             fist_mod_id = mod_id;
            
             
             //检查报名记录是否存在
             long app_id = aeApplication.getAppIdByTkhId( con,  tkh_id);
             if(app_id < 1){
                 debugLogger.debug("ERROR: Can't find application record in the cousre! ");
                 out.println("ERROR: Can't find application record in the cousre! ");
                 return;
             }
             DbTrackingHistory tkh =new DbTrackingHistory();
             tkh.tkh_id = tkh_id;
             tkh.get(con);
             
             //初始始化用户信息
             dbRegUser user = new dbRegUser();
             user.usr_ent_id = tkh.tkh_usr_ent_id;
             user.getByEntId(con);
             loginProfile prof = new loginProfile();
             if (user.usr_id != null) {
                
                 prof.usr_id         = user.usr_id;
                 prof.usr_ent_id     = user.usr_ent_id;
                 prof.current_role   = "NLRN_" + String.valueOf(user.usr_ste_ent_id);
                 prof.root_ent_id    = user.usr_ste_ent_id;
             }
             else {
                 debugLogger.debug("ERROR: Can't find User ! ");
                 out.println("ERROR: Can't find User !");
                 return;
             }
             
        	if (request.getParameter("op") != null) {
        	    if("r".equals(request.getParameter("op")) || "w".equals(request.getParameter("op"))){
        	        mod_id = modRefModId( con, mod_id, iid) ;
                    if( mod_id < 1){
                        debugLogger.debug("ERROR: Can't find module in LMS");
                        return;
                    }
                    
        	        dbModule  mod = new dbModule();
                    mod.mod_res_id = mod_id;
                    
                    
            		if ("r".equals(request.getParameter("op"))) {
            		  //初始化模块学习记录
            		    dbModuleEvaluation mov = initMov( con,  prof,  tkh,  mod, true);
        		        getData( con, tkh_id,  mod_id,  iid, mod);
        		    } else if ("w".equals(request.getParameter("op"))) {
        		      //初始化模块学习记录
        		        dbModuleEvaluation mov = initMov( con,  prof,  tkh,   mod, false);
        		        putData2DB( con,  prof, tkh_id, iid, postData,mod, mov);
        		    }
        	    }
    		    else if ("l".equals(request.getParameter("op"))) {
    		        getinitData( con, tkh_id,  fist_mod_id);
                }
        	}
        	con.commit();
    	} catch (Exception e) {
    	    debugLogger.debug("ERROR:--- "+e.getMessage());
    	    CommonLog.error("ERROR:--- "+e.getMessage());
            //e.printStackTrace();
            out.println("ERROR:--- "+e.getMessage());
    	    try {
    	        if(con != null){
                    con.rollback();
                }
            } catch (SQLException ee) {
                // TODO Auto-generated catch block
            	CommonLog.error(ee.getMessage(),ee);
                //ee.printStackTrace();
            }
           
        } finally {
            try {
                if(con != null){
                    con.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
            	CommonLog.error(e.getMessage(),e);
                //e.printStackTrace();
            }
           debugLogger.debug("-------------scorm 2004 request end---------------");  
        }
       
        return;
    }
    
    private  void getData(Connection con,long tkh_id, long mod_id, String iid, dbModule  mod) throws Exception{
        // Read
        // Check database if a record matching uid + cid + iid exists
        // If not exists, insert a new record
        // Return the record and return the data
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String sql = "";
        try {
            long recordId = initRecord( con,  tkh_id,  iid,   mod) ;
            sql = "select usr_display_bil, srd_courseStatus,srd_courseCompletion,srd_courseCredit,srd_courseLaunchData,srd_courseCount," +
                    " srd_courseLastDate,srd_courseTimeLength,srd_courseTimeLimit,srd_courseRawScore,srd_courseMaxScore,srd_courseMinScore," +
                    " srd_coursePassScore,srd_courseLocation,srd_courseSusData,srd_courseObjective,srd_courseInteractions,srd_courseData1" +
                    " from scoRecord, aeApplication,reguser where srd_tkh_id = app_tkh_id and app_ent_id = usr_ent_id and srd_id=?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, recordId);

            rs = stmt.executeQuery();

            String doStr="";
            String cobValue=null;
            if (rs.next()) {
                doStr=rs.getString("usr_display_bil"); //
                
                doStr+=delim+(rs.getString("srd_courseCompletion")==null?"":rs.getString("srd_courseCompletion")); //
                doStr+=delim+(rs.getString("srd_courseStatus")==null?"":rs.getString("srd_courseStatus")); //
                doStr+=delim+(rs.getString("srd_courseCredit")==null?"":rs.getString("srd_courseCredit")); //
                doStr+=delim+(rs.getString("srd_courseLaunchData")==null?"":rs.getString("srd_courseLaunchData")); //
                doStr+=delim+(rs.getString("srd_courseCount")==null?"":rs.getString("srd_courseCount")); //
                doStr+=delim+(rs.getString("srd_courseLastDate")==null?"":rs.getString("srd_courseLastDate")); //
                doStr+=delim+(rs.getString("srd_courseTimeLength")==null?"":rs.getString("srd_courseTimeLength")); //
                doStr+=delim+(rs.getString("srd_courseTimeLimit")==null?"":rs.getString("srd_courseTimeLimit")); //
                doStr+=delim+(rs.getString("srd_courseRawScore")==null?"":rs.getString("srd_courseRawScore")); //
                doStr+=delim+(rs.getString("srd_courseMaxScore")==null?"":rs.getString("srd_courseMaxScore")); //
                doStr+=delim+(rs.getString("srd_courseMinScore")==null?"":rs.getString("srd_courseMinScore")); //
                doStr+=delim+(rs.getString("srd_coursePassScore")==null?"":rs.getString("srd_coursePassScore")); //
                
                cobValue = cwSQL.getClobValue(rs, "srd_courseLocation");
                doStr+=delim+(cobValue==null?"":cobValue); //
                
                cobValue = cwSQL.getClobValue(rs, "srd_courseSusData");
                doStr+=delim+(cobValue==null?"":cobValue); //
                
                cobValue = cwSQL.getClobValue(rs, "srd_courseObjective");
                doStr+=delim+(cobValue==null?"":cobValue); //
                
                cobValue = cwSQL.getClobValue(rs, "srd_courseInteractions");
                doStr+=delim+(cobValue==null?"":cobValue); //   
                
                cobValue = cwSQL.getClobValue(rs, "srd_courseData1");
                doStr+=delim+(cobValue==null?"":cobValue); //
            }
            debugLogger.debug("Read:--- "+doStr);
            out.println(doStr);
            
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    CommonLog.error(e.getMessage(),e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    //e.printStackTrace();
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }    
    }
    
    private  void getinitData(Connection con,long tkh_id, long fist_mod_id) throws Exception{
        // Read
        // Check database if a record matching uid + cid + iid exists
        // If not exists, insert a new record
        // Return the record and return the data
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String sql = "";
        try {
            sql = "select res_scor_identifier,srd_courseStatus,srd_courseLastDate from resources left join scoRecord on (res_id = srd_mod_id and srd_tkh_id = ?)" +
            		" where res_first_res_id = ? order by srd_courseLastDate " ;
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, tkh_id);
            stmt.setLong(2, fist_mod_id);

            rs = stmt.executeQuery();

            String doStr="";
            int i = 0;
           while (rs.next()) {
               if(i > 0){
                   doStr+=delim; 
               }
               doStr +=rs.getString("res_scor_identifier");
               if(rs.getString("srd_courseStatus") != null && rs.getString("srd_courseStatus").trim().length() > 0){
                   doStr += "=" + rs.getString("srd_courseStatus"); 
               }
                i ++;
            }
            debugLogger.debug("Read:--- "+doStr);
            out.println(doStr);
            
        }finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                    CommonLog.error(e.getMessage(),e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                   // e.printStackTrace();
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }    
    }
    private  void putData2DB(Connection con,loginProfile prof, long tkh_id,  String iid,String postData, dbModule  mod, dbModuleEvaluation mov)  
    throws cwException, SQLException, cwSysMessage, qdbException, qdbErrMessage{
        // Write
        // Check database if a record matching uid + cid + iid exists
        // If not exists, insert a new record
        // Update the record with the received data
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String sql = "";
        long courseTimeLength = 0;
        float courseRawScore =0;
        float courseMaxScore = 0;
        float courseMinScore = 0;
        String mov_status = dbModuleEvaluation.STATUS_IN_PROGRESS;
        try {

            long recordId = initRecord( con,  tkh_id,   iid, mod) ;
           
            debugLogger.debug("postData="+postData);
            String[] st = postData.split(delim);
            debugLogger.debug("postData length="+st.length);
            int i=0;
            
            //是否完成
            String courseStatus = st[i++]; 
            debugLogger.debug("courseStatus="+courseStatus);
            
            //内部地址
            String courseLocation = st[i++]; 
            debugLogger.debug("courseLocation="+courseLocation);
            
          //原始成绩
            String courseRawScore_str = st[i++]; 
            debugLogger.debug("courseRawScore="+courseRawScore_str);
            try {
                if(courseRawScore_str != null && courseRawScore_str.trim().length() > 0)
                    courseRawScore=  new Float(courseRawScore_str.trim());
            } catch (java.lang.NumberFormatException e) {
               // e.printStackTrace();
                CommonLog.error(e.getMessage(),e);
            }
            
            
            //最高分数
            String courseMaxScore_str = st[i++]; 
            debugLogger.debug("courseMaxScore="+courseMaxScore_str);
            try {
                if(courseMaxScore_str != null && courseMaxScore_str.trim().length() > 0)
                courseMaxScore=  new Float(courseMaxScore_str.trim());
            } catch (java.lang.NumberFormatException e) {
               // e.printStackTrace();
                CommonLog.error(e.getMessage(),e);
            }
            
          //最低分数
            String courseMinScore_str = st[i++]; 
            
            debugLogger.debug("courseMinScore="+courseMinScore_str);
            try {
                if(courseMinScore_str != null && courseMinScore_str.trim().length() > 0)
                    courseMinScore=  new Float(courseMinScore_str.trim());
            } catch (java.lang.NumberFormatException e) {
               // e.printStackTrace();
                CommonLog.error(e.getMessage(),e);
            }
            
            
          //当前SCO学习时长
            String courseTimeLength_str = st[i++]; 
            debugLogger.debug("courseTimeLength="+courseTimeLength_str);
            try {
                if(courseTimeLength_str != null && courseTimeLength_str.trim().length() > 0)
                    courseTimeLength=  (new Float(courseTimeLength_str.trim())).longValue();
            } catch (java.lang.NumberFormatException e) {
                //e.printStackTrace();
                CommonLog.error(e.getMessage(),e);
            }
            
            
          // 是否完成当前SCO
            String courseCompletion = st[i++]; 
            debugLogger.debug("courseCompletion="+courseCompletion);
            
         // 内部冗余字段
            String courseSusData = st[i++]; 
            debugLogger.debug("courseSusData="+courseSusData);
            
         //  内部知识点数据;
            String courseObjective = st[i++]; 
            debugLogger.debug("courseObjective="+courseObjective);
            
        //  SCO交互数据
            String courseInteractions = st[i++]; 
            debugLogger.debug("courseInteractions="+courseInteractions);
            
        //  其他冗余字段
            String courseData1 = st[i]; //
            debugLogger.debug("courseData1="+courseData1);
            
            
            mov.mov_last_acc_datetime = cur_time;
            mov.mov_total_time = mov.mov_total_time + courseTimeLength;
            //mov.mov_total_attempt = mov.mov_total_attempt + 1;
            if(mov.mov_score == null || Float.parseFloat(mov.mov_score) < courseRawScore){
                mov.mov_score = String.valueOf(courseRawScore);
            }
            
            
           
           if("completed".equalsIgnoreCase(courseStatus)){
               mov_status = dbAiccPath.STATUS_COMPLETE;
           }
//           else if("Passed".equalsIgnoreCase(courseCompletion)){
//               mov_status = dbAiccPath.STATUS_PASSED;
//           }else if("failed".equalsIgnoreCase(courseCompletion)){
//               mov_status = dbAiccPath.STATUS_FAILED;
//           }
            if(mov.mov_status == null || mov.mov_status.trim().length() < 1 || dbModuleEvaluation.STATUS_IN_PROGRESS.equalsIgnoreCase(mov.mov_status)){
                mov.mov_status  = mov_status;
            }else if(mov.mov_status.equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE)){
                
            }else if(mov.mov_status.equalsIgnoreCase(dbAiccPath.STATUS_PASSED)){
                if(mov_status.equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE)){
                    mov.mov_status  = mov_status; 
                }
            }
            else if(mov.mov_status.equalsIgnoreCase(dbAiccPath.STATUS_FAILED)){
                if(mov_status.equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE) || mov_status.equalsIgnoreCase(dbAiccPath.STATUS_PASSED)){
                    mov.mov_status  = mov_status; 
                }
            }else{
                mov.mov_status = mov_status;
            }
            
            
           
            
            sql = "update scoRecord set srd_courseStatus=?, srd_courseLocation=?, srd_courseRawScore=?, srd_courseMaxScore=?," +
            		" srd_courseMinScore=?,  srd_courseCompletion=?, srd_courseSusData=?, srd_courseObjective=?, " +
            		"srd_courseInteractions=?, srd_courseData1=?, srd_courseCount = ?, srd_courseTimeLength=?, srd_courseLastDate=?" +
            		"where srd_id=?";
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, courseStatus);
            stmt.setString(index++, courseLocation);
            stmt.setFloat(index++, courseRawScore);
            stmt.setFloat(index++, courseMaxScore);
            stmt.setFloat(index++, courseMinScore);
            stmt.setString(index++, courseCompletion);
            stmt.setString(index++, courseSusData);
            stmt.setString(index++, courseObjective);
            stmt.setString(index++, courseInteractions);
            stmt.setString(index++, courseData1);
            stmt.setLong(index++, mov.mov_total_attempt);
            
            stmt.setLong(index++, (long)mov.mov_total_time);
            
            stmt.setTimestamp(index++, cur_time);
            
            stmt.setLong(index++, recordId);
            stmt.executeUpdate();
            
        }  finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
        mov.upd(con);
        addMovHis( con,  mov,  courseTimeLength,  courseRawScore,mov_status);
        con.commit();

        CourseCriteria.setAttendFromModule(con, prof, mov.mov_mod_id, mov.mov_cos_id, mov.mov_ent_id,  tkh_id, mov.mov_status, courseTimeLength, cur_time, cur_time);
        out.println("write_record_success");
    
    }
    
    private long initRecord(Connection con, long tkh_id,  String iid,dbModule  mod) throws SQLException {
        long recordId = 0;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String sql = "select srd_id from scoRecord where srd_tkh_id=? and srd_mod_id=? and srd_itemId=?";

        try {
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, tkh_id);
            stmt.setLong(2, mod.mod_res_id);
            stmt.setString(3, iid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                recordId = rs.getLong("srd_id");
            }
            rs.close();
            stmt.close();
            if(recordId == 0){
                sql = "insert into scoRecord (srd_tkh_id, srd_mod_id, srd_itemId, srd_courseStyle, srd_courseLastDate) values (?, ?, ?, ?, ?)";
                stmt = con.prepareStatement(sql,  PreparedStatement.RETURN_GENERATED_KEYS);
                int index = 1;
                stmt.setLong(index++, tkh_id);
                stmt.setLong(index++,  mod.mod_res_id);
                stmt.setString(index++, iid);
                stmt.setString(index++, "SCORM2004");
                
                stmt.setTimestamp(index++, cur_time);
                stmt.executeUpdate();  
                
                recordId = cwSQL.getAutoId(con, stmt, "scoRecord", "srd_id");
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    //e.printStackTrace();
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
        return recordId;
    }
    
    
    private dbModuleEvaluation initMov(Connection con, loginProfile prof, DbTrackingHistory tkh,   dbModule mod, boolean attempt_count) throws qdbException,SQLException {
        dbModuleEvaluation mov =new dbModuleEvaluation();
        mov.mov_ent_id = prof.usr_ent_id;
        mov.mov_mod_id = mod.mod_res_id;
        mov.mov_tkh_id = tkh.tkh_id;
        mov.mov_cos_id = tkh.tkh_cos_res_id;
        mov.mov_create_usr_id = prof.usr_id;
        mov.mov_last_acc_datetime=cur_time;
        if(!mov.get(con)){
            mov.mov_total_attempt = 1;
            mov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
            mov.ins(con);
        }else{
            mov.mov_update_usr_id = prof.usr_id;
            mov.mov_update_timestamp=cur_time;
            mov.mov_last_acc_datetime=cur_time;
            if(attempt_count){
                mov.mov_total_attempt = mov.mov_total_attempt + 1;
            }
            mov.upd(con);
        }
        dbCourseEvaluation cov = new dbCourseEvaluation();
        cov.cov_cos_id = tkh.tkh_cos_res_id;
        cov.cov_ent_id = prof.usr_ent_id;
        cov.cov_tkh_id = tkh.tkh_id;
        boolean hasACovRecord = cov.get(con);
        if (hasACovRecord) {
            cov.cov_last_acc_datetime=cur_time;
            
            if(cov.cov_commence_datetime == null){
                cov.cov_commence_datetime=cur_time;
            }
            cov.upd(con);
        }

        return mov;
    }
    
    
    private void addMovHis(Connection con, dbModuleEvaluation mov, long courseTimeLength, float courseRawScore, String mov_status) throws qdbException {
        dbModuleEvaluationHistory dbModEvalHist = new dbModuleEvaluationHistory();
        dbModEvalHist.mvh_last_acc_datetime = cur_time;
        dbModEvalHist.mvh_total_time =courseTimeLength;
        dbModEvalHist.mvh_status = mov_status;
        dbModEvalHist.mvh_score = String.valueOf(courseRawScore);
        
        dbModEvalHist.mvh_ent_id = mov.mov_ent_id;

        dbModEvalHist.mvh_mod_id = mov.mov_mod_id;
        dbModEvalHist.mvh_tkh_id = mov.mov_tkh_id;
        dbModEvalHist.mvh_total_attempt = mov.mov_total_attempt;
        dbModEvalHist.mvh_create_usr_id = mov.mov_update_usr_id;
        dbModEvalHist.mvh_create_timestamp = cur_time;
        dbModEvalHist.ins(con);
    }
    
    
    //在删除报名记录时调用
    public static void delRecord(Connection con, long srd_tkh_id) throws SQLException {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            String sql = "delete from  scoRecord where srd_tkh_id = ?";
                stmt = con.prepareStatement(sql);
                int index = 1;
                stmt.setLong(index++, srd_tkh_id);
                stmt.executeUpdate();  

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    //e.printStackTrace();
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
    }
    
    
    public static long modRefModId(Connection con, long fist_res_id, String identifier) throws SQLException {
        ResultSet rs = null;
        long res_id = 0;
        PreparedStatement stmt = null;
        try {
            String sql = "select res_id from resources where res_first_res_id =? and res_scor_identifier=?";
                stmt = con.prepareStatement(sql);
                int index = 1;
                stmt.setLong(index++, fist_res_id);
                stmt.setString(index++, identifier);
                
                rs = stmt.executeQuery();

                String doStr="";
                if (rs.next()) {
                    res_id= rs.getLong("res_id");   
                }

        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    //e.printStackTrace();
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
        return res_id;
    }
    
    public static void main(String[] args){
        String courseTimeLength_str = "9.507"; 
       System.out.println("courseTimeLength="+courseTimeLength_str);
        try {
            if(courseTimeLength_str != null && courseTimeLength_str.trim().length() > 0){
                 long aa =  (new Float(courseTimeLength_str.trim())).longValue();
                 System.out.println("courseTimeLength="+aa);
            }
        } catch (java.lang.NumberFormatException e) {
            //e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
        }
        
    }
   
}


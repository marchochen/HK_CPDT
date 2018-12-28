package com.cw.wizbank.ae;

import java.util.*;
import java.sql.*;

import javax.servlet.http.*;

import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.db.DbLearningSoln;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.db.DbRegUser;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.view.ViewCourseModuleCriteria;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.course.EvalManagement;
import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpd.service.CpdLrnAwardRecordService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.utils.CommonLog;

public class aeAttendance {

    public static final String[] ORDER_ATT_BY_ITEM = {"user", "tel", "status", "remark","usr_id","due_date","att_timestamp","att_update_timestamp"};
    public static final String[] ORDER_ATT_BY_ITEM_DB = {"usr_display_bil", "usr_tel_1", "att_ats_id", "att_remark","usr_ste_usr_id","due_date","att_timestamp","att_update_timestamp"};
    public static final String ATT_ITM_ID     = "att_itm_id";
    public static final String ATT_ENT_LST     = "att_ent_lst";
    public static final String ATT_STATUS_ID     = "att_status";
    public static final String ATT_SORT_BY  = "att_sort_by";
    public static final String ATT_ORDER_BY = "att_order_by";
    public static final String ATT_TIMESTAMP = "att_timestamp";
    public static final String ATT_STATUS_XML = "att_status_xml";
    public static final String ATT_IS_PROGRESS = "att_is_progress";

    public static final int DEF_PAGESIZE = 10;

    public String       gpm_full_path;
    public long         att_itm_id;
    public long         att_app_id;
    public int          att_ats_id;
    public String       att_remark;
    public String       att_rate_remark;
    // decimal in db
    public String       att_rate;
    public String       att_create_usr_id;
    public Timestamp    att_create_timestamp;
    public String       att_update_usr_id;
    public Timestamp    att_timestamp;
    public Timestamp    att_update_timestamp;
    public Timestamp    att_last_update_timestamp;
    public Timestamp[]  att_update_timestamp_lst;

    public dbRegUser usr;
    public Timestamp curTime;

    private boolean recalDate = false;

    public int due_date;
    public String sys_email;

    //For Messaging Subject;
    public static String[] msgSubject = {"1st No Show", "2nd No Show", "No Show"};

    public aeAttendance() {
        usr = new dbRegUser();
    }

    public StringBuffer getSingleAsXML(Connection con, HttpSession sess, long root_ent_id, long itm_id, int status) throws cwException, qdbException, SQLException, cwSysMessage{
        StringBuffer result = new StringBuffer();
        aeItem item = new aeItem();
        item.itm_id = itm_id;
        item.getItem(con);

        this.att_itm_id = itm_id;
        boolean isExist = get(con);
        if (!isExist){
            return result;
        }
        aeAttendanceStatus ats = new aeAttendanceStatus();

        String stateXML = (String) sess.getAttribute(ATT_STATUS_XML);
        if (stateXML == null){
            stateXML = stateAsXML(con, root_ent_id, itm_id, status, ats);
        }

        result.append("<attendance_maintance>").append(cwUtils.NEWL);
        result.append(item.contentAsXML(con, null));
        result.append(stateXML);
        result.append(asXML(con, false));
        result.append(cwUtils.NEWL);
        result.append("</attendance_maintance>");
        result.append(cwUtils.NEWL);
        return result;
    }

    /*
    *   for a list of attendance
    */
    public static String processStatus(Connection con, HttpSession sess, long root_ent_id, long itm_id, int status, cwPagination cwPage)
         throws SQLException ,cwSysMessage, cwException, qdbException, qdbErrMessage
    {
        return processStatus(con, sess, root_ent_id, itm_id, status, cwPage, false, 0, null);
    }
    public static String processStatus(Connection con, HttpSession sess, long root_ent_id, long itm_id, int status, cwPagination cwPage, boolean show_approval_ent_only, long usr_ent_id, String rol_ext_id)
         throws SQLException ,cwSysMessage, cwException, qdbException, qdbErrMessage
    {
        return processStatus(con, sess, root_ent_id, itm_id, status, cwPage, show_approval_ent_only, usr_ent_id, rol_ext_id, null,null,false,null);
    }    
    public static String processStatus(Connection con, HttpSession sess, long root_ent_id, long itm_id, int status, cwPagination cwPage, boolean show_approval_ent_only, long usr_ent_id, String rol_ext_id, WizbiniLoader wizbini, long[] app_id_lst,boolean imp)
    		 throws SQLException ,cwSysMessage, cwException, qdbException, qdbErrMessage
    {
    	return processStatus(con, sess, root_ent_id, itm_id, status, cwPage, show_approval_ent_only, usr_ent_id, rol_ext_id, wizbini, app_id_lst, imp, null);
    }
    public static String processStatus(Connection con, HttpSession sess, long root_ent_id, long itm_id, int status, cwPagination cwPage, boolean show_approval_ent_only, long usr_ent_id, String rol_ext_id, WizbiniLoader wizbini, long[] app_id_lst,boolean imp,String usercode)
         throws SQLException ,cwSysMessage, cwException, qdbException, qdbErrMessage
    {
        StringBuffer result = new StringBuffer();
        StringBuffer orderSQL = new StringBuffer(" ORDER BY ");

        aeItem item = new aeItem();
        boolean fromSession = false;
        boolean scopeResult = false;

        Vector vtAtt = new Vector();
        Vector appByStatus = new Vector();
        Vector appByPage = new Vector();

        Timestamp sess_timestamp = (Timestamp)sess.getAttribute(ATT_TIMESTAMP);
        String sess_sort_by = (String) sess.getAttribute(ATT_SORT_BY);
        String sess_order_by = (String) sess.getAttribute(ATT_ORDER_BY);
        Long sess_itm_id = (Long) sess.getAttribute(ATT_ITM_ID);
        Integer sess_status_id = (Integer) sess.getAttribute(ATT_STATUS_ID);
        String stateXML = (String) sess.getAttribute(ATT_STATUS_XML);

        if (cwPage.ts == null)
            cwPage.ts = cwSQL.getTime(con);

        if (cwPage.pageSize == 0){
            cwPage.pageSize = DEF_PAGESIZE;
        }
        if (cwPage.curPage == 0) {
            cwPage.curPage = 1;
        }

        boolean isProgress = false;

        int start;
        int end;
        
        // '0' implies ONLY the "PROGRESS" records (2003-06-27 kawai)
        if (status == 0) {
            status = aeAttendanceStatus.getIdByType(con, root_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
        }
        
        if (sess_timestamp != null && sess_timestamp.equals(cwPage.ts) &&
            sess_itm_id != null && sess_itm_id.longValue() == itm_id &&
            sess_status_id != null && sess_status_id.intValue() == status &&
            stateXML != null )
        {
            appByStatus = (Vector)sess.getAttribute(ATT_ENT_LST);
            if (sess_sort_by!= null && sess_sort_by.equals(cwPage.sortCol)){
                if (sess_order_by!= null && sess_order_by.equals(cwPage.sortOrder)){
                    start = ((cwPage.curPage-1) * cwPage.pageSize) + 1;
                    end = cwPage.curPage * cwPage.pageSize;
                }else{
                    // same sort col, reverse order
                    start = appByStatus.size() - cwPage.pageSize;
                    if (start<0){
                        start = 0;
                    }
                    end = appByStatus.size();
                }
                for (int i=start ; i<= appByStatus.size() && (i <= end);i++) {
                    appByPage.addElement(appByStatus.elementAt(i-1));
                }
                scopeResult = true;
            }else{
                scopeResult = false;
            }
            fromSession = true;

        } else {

            fromSession = false;

            aeAttendanceStatus currentStatus = new aeAttendanceStatus();

            stateXML = stateAsXML(con, root_ent_id, itm_id, status, currentStatus);

            if (currentStatus.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_PROGRESS)){
                isProgress = true;
            }else{
                isProgress = false;
            }
        }
        orderSQL.append(getOrderSQL(cwPage.sortCol, cwPage.sortOrder));

        if (sess_sort_by!= null && !sess_sort_by.equals(cwPage.sortCol)){
            orderSQL.append(getOrderSQL(sess_sort_by, sess_order_by));
        }

        orderSQL.append("app_id");

        if (fromSession){
            isProgress = ((Boolean)sess.getAttribute(ATT_IS_PROGRESS)).booleanValue();
        }
        
        Vector macthedAppVec = null;
        if (show_approval_ent_only) {
            macthedAppVec = aeQueueManager.getApproverInChargeAppId(con, itm_id, usr_ent_id, rol_ext_id);
            if (macthedAppVec.size() == 0) {
                macthedAppVec.addElement(new Long(0));
            }
        }

        //获取结训记录
        vtAtt = getAttLst(con, root_ent_id, itm_id, status, isProgress, orderSQL.toString(), appByStatus, appByPage, scopeResult, macthedAppVec, wizbini, app_id_lst,usercode);      
        
        if (!fromSession){
            sess.setAttribute(ATT_ITM_ID, new Long(itm_id));
            sess.setAttribute(ATT_STATUS_ID, new Integer(status));
            sess.setAttribute(ATT_TIMESTAMP, cwPage.ts);
            sess.setAttribute(ATT_STATUS_XML, stateXML);
            sess.setAttribute(ATT_ENT_LST, appByStatus);
            sess.setAttribute(ATT_IS_PROGRESS, new Boolean(isProgress));
        }
        if (cwPage.sortCol != null && cwPage.sortOrder != null){
            sess.setAttribute(ATT_SORT_BY, cwPage.sortCol);
            sess.setAttribute(ATT_ORDER_BY, cwPage.sortOrder);
        }
        item.itm_id = itm_id;
        item.getItem(con);

        result.append("<attendance_maintance timestamp=\"");
        result.append(cwPage.ts).append("\" search_code=\""+usercode+"\" >").append(cwUtils.NEWL);

        result.append(item.contentAsXML(con, null, root_ent_id));
		result.append(aeItem.getNavAsXML(con, itm_id));
        result.append("<status_info current_id=\"").append(status).append("\"> ");
        result.append(stateXML);
        result.append("</status_info>").append(cwUtils.NEWL);
        result.append(aeItemFigure.getItemFigureXML(con, itm_id));
        long cos_id = item.getResId(con);
        //get parent info (id and title) for run item
        long itm_parent_id = 0;
       /* by william.weng,cos_id now relate to itm_id ;
        if(item.itm_run_ind) {
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = item.itm_id;
            aeItem ireParentItm = ire.getParentInfo(con);
            if (ireParentItm != null) {
                itm_parent_id = ireParentItm.itm_id;
            }
            cos_id = dbCourse.getCosResId(con,  itm_parent_id);
        }*/
        cos_id =dbCourse.getCosResId(con,itm_id);
        
        //System.out.println("in attencedance:" + cos_id);
        
        if (appByStatus != null){
            cwPage.totalRec = appByStatus.size();
            if (cwPage.pageSize != 0){
                cwPage.totalPage = appByStatus.size() / cwPage.pageSize;
                if (appByStatus.size() % cwPage.pageSize != 0){
                    cwPage.totalPage++;
                }
            }
        }

        result.append(cwPage.asXML());

        result.append("<attendance_list>");
        StringBuffer attListXML = new StringBuffer(1024);
        if (vtAtt != null) {
            int count;
            if (appByPage != null && appByPage.size() != 0) {
                count = 0;
            } else {
                count = (cwPage.curPage-1)*cwPage.pageSize;
            }

			CourseCriteria ccr = new CourseCriteria();
            boolean initSetCtrDate = false;
            if(!ccr.initSetCtrData(con, cos_id, 0)){
      		   //this course's criteria has no cmt now.
            	initSetCtrDate =  true;
      	  	}
            for (int i=count; i<vtAtt.size() && i<count+cwPage.pageSize; i++) {
                aeAttendance att = (aeAttendance) vtAtt.elementAt(i);
                //attListXML.append(dbModuleEvaluation.getModuleEvalFromCosAsXML(con, cos_id, att.att_app_id));
                dbCourseEvaluation cov = new dbCourseEvaluation();
                cov.cov_cos_id = cos_id;
                cov.cov_ent_id = att.usr.usr_ent_id;
                long tkh_id = aeApplication.getTkhId(con, att.att_app_id);
				cov.cov_tkh_id = tkh_id;
                cov.get(con);
                boolean passOtherCriteria = false;
                if(initSetCtrDate){
                	passOtherCriteria = true;
                } else{
                	passOtherCriteria = ccr.checkAllCrtForOne4Att(con,cos_id,tkh_id);
                }
                
                boolean hasOtherCriteria = true;
                if(ccr.cmtLst.size() == 0){
                    hasOtherCriteria = false;
                }
                
                //System.out.println("in attencedance ent_id :" + cov.cov_ent_id);
                //attListXML.append("<cov_score>" +aeUtils.escNull(cov.cov_score) +"</cov_score>");  
                attListXML.append(att.asXML(con, true, cov.cov_score,hasOtherCriteria, passOtherCriteria,imp));
                
            }
        }
        result.append(attListXML);
        result.append("</attendance_list>");
        result.append(cwUtils.NEWL);
        result.append("</attendance_maintance>");
        result.append(cwUtils.NEWL);

        return result.toString();
    }
    public static StringBuffer getOrderSQL(String sort_by, String order_by){
        StringBuffer orderSQL = new StringBuffer();
        for (int i=0; i<ORDER_ATT_BY_ITEM.length; i++) {
                if (ORDER_ATT_BY_ITEM[i].equalsIgnoreCase(sort_by)) {
                    orderSQL.append(ORDER_ATT_BY_ITEM_DB[i]);
                    if (order_by != null && order_by.equalsIgnoreCase("DESC")) {
                        orderSQL.append(" DESC, ");
                    } else {
                        orderSQL.append(" ASC, ");
                    }
                }
        }
        return orderSQL;
    }
    public static int countStatus(Connection con, long itm_id, int ats_id)
        throws SQLException, cwException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int count = 0;

        if (itm_id == 0) {
            throw new cwException("com.cw.wizbank.ae.aeApplication.countStatus: itm_id = 0");
        }

        StringBuffer SQL = new StringBuffer(1024);
//        SQL.append("SELECT COUNT(*) AS CNT FROM aeApplication, aeAttendance");
//        SQL.append(" WHERE att_app_id = app_id ");
//        SQL.append(" AND app_itm_id = ? AND att_ats_id = ? ");
//        SQL.append(" and app_itm_id  = att_itm_id  ");
//        SQL.append(" AND app_id in (select max(app_id) from aeApplication, aeAttendance where att_app_id = app_id and app_itm_id = " + itm_id+ "  group by app_ent_id ) ");
        SQL.append("SELECT COUNT(*) AS CNT FROM aeAttendance ");
        SQL.append(" WHERE att_itm_id = ? AND att_ats_id = ? ");
        SQL.append(" AND att_app_id in (select max(app_id) from aeApplication, aeAttendance where att_app_id = app_id and app_itm_id = ?  group by app_ent_id ) ") ;
        try {
            long appItmId = itm_id;
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            if (itm.getSessionInd(con)){
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = itm_id;
                appItmId = ire.getParentItemId(con);
            }
            stmt = con.prepareStatement(SQL.toString());
            stmt.setLong  (1, itm_id);
            stmt.setInt(2, ats_id);
            stmt.setLong  (3, appItmId);            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("CNT");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return count;
    }
/*
    public static int countRest(Connection con, long itm_id)
        throws SQLException, cwException
    {
        PreparedStatement stmt;
        ResultSet rs;
        int count = 0;

        if (itm_id == 0) {
            throw new cwException("com.cw.wizbank.ae.aeApplication.countStatus: itm_id = 0");
        }

        StringBuffer SQL = new StringBuffer(1024);
        SQL.append("SELECT COUNT(*) AS CNT FROM aeApplication ");
        SQL.append(" WHERE ");
        SQL.append(" app_itm_id = " + itm_id + " AND app_id NOT IN (SELECT att_app_id FROM aeAttendance, aeApplication WHERE att_app_id = app_id AND app_itm_id = " + itm_id + " )");

        stmt = con.prepareStatement(SQL.toString());
        rs = stmt.executeQuery();

        if (rs.next()) {
            count = rs.getInt("CNT");
        }

        return count;
    }
  */
    // new
    // assign currentStatusObj
    public static String stateAsXML(Connection con, long root_ent_id, long itm_id, int currentId, aeAttendanceStatus currentStatus)
        throws SQLException, cwException
    {
        StringBuffer result = new StringBuffer();
        StringBuffer statusDescXML = new StringBuffer();
        StringBuffer countXML = new StringBuffer();

        Vector allStatus = aeAttendanceStatus.getAllByRoot(con, root_ent_id);

        for (int i=0; i<allStatus.size(); i++){
            aeAttendanceStatus stat = (aeAttendanceStatus) allStatus.elementAt(i);
            int count = countStatus(con, itm_id, stat.ats_id);
//            if (stat.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_PROGRESS)){
//                count += countRest(con, itm_id);
//            }

            statusDescXML.append(stat.ats_title_xml);
            countXML.append("<count status_id=\"" + stat.ats_id + "\">" + count + "</count>");
            if (stat.ats_id == currentId){
                currentStatus.ats_id = stat.ats_id;
                currentStatus.ats_title_xml = stat.ats_title_xml;
                currentStatus.ats_type = stat.ats_type;
                currentStatus.ats_ent_id_root = stat.ats_ent_id_root;
            }
        }
        result.append("<status_list>").append(cwUtils.NEWL);
        result.append(statusDescXML);
        result.append("</status_list>").append(cwUtils.NEWL);

        result.append("<count_list>").append(cwUtils.NEWL);
        result.append(countXML);
        result.append("</count_list>").append(cwUtils.NEWL);

        return result.toString();
    }
    public void updMultiStatus(Connection con, String upd_usr_id, long[] app_id_lst, int status, long itm_id) throws cwException, SQLException, cwSysMessage{
        try{
            Timestamp curTime = cwSQL.getTime(con);
            att_update_usr_id = upd_usr_id;
            att_ats_id = status;
            att_itm_id = itm_id;


            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            /*
//      set in saveStatus(con)

            if (itm.itm_eff_end_datetime != null){
                att_timestamp = itm.itm_eff_end_datetime;
            }else{
                att_timestamp = curTime;
            }
            att_update_timestamp = curTime;
*/
//            itm.getRunInd(con);
            long cos_id;
            boolean isSession = itm.getSessionInd(con);
            if (isSession){
                cos_id = 0;
            }else{
                cos_id = itm.getResId(con);
            }
            
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itm_id;
            long parent_itm_id = ire.getParentItemId(con);
            itm.get(con);
            for (int i=0; i<app_id_lst.length; i++){
                att_app_id = app_id_lst[i];
                if (cos_id == 0){
                    saveStatus(con);
                }else{
                    aeApplication app = new aeApplication();
                    app.app_id = att_app_id;
                    app.get(con);
                    saveStatus(con, att_app_id, itm_id, cos_id, app.app_ent_id, false, status, upd_usr_id);                    
                }
                
                if (isSession){
                    // no real time trigger course criteria
//                    autoUpdateAttendanceFromSession(con, upd_usr_id, itm.itm_owner_ent_id, parent_itm_id, att_app_id);
                }
            }
        }catch(qdbException e){
            throw new cwException(e.getMessage());
        }
    }

    public void invalidateSess(HttpSession sess){
        sess.removeAttribute(ATT_TIMESTAMP);
        sess.removeAttribute(ATT_ENT_LST);
        sess.removeAttribute(ATT_STATUS_XML);
    }
    // pre-defined itm_id    
    protected boolean isAttExist(Connection con) throws SQLException{
        //System.out.println("isAttExist(con).ats_id" + att_ats_id);
        boolean isExist = false;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String SQL = "SELECT att_app_id FROM aeAttendance " + cwSQL.noLockTable() + " WHERE att_app_id = ? ";
            
            if (att_itm_id !=0){
                SQL += " AND att_itm_id = ? ";            
            }
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, att_app_id);
            if (att_itm_id !=0){
                stmt.setLong(2, att_itm_id);
            }
            rs = stmt.executeQuery();
            if (rs.next()){
                isExist = true;
            }else{
                isExist = false;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(rs!=null)rs.close();
        }
        
        return isExist;
    }

    static final String INS_ATT_SQL = " INSERT INTO aeAttendance  " + cwSQL.rowLockTable() + ""
                                              + " (att_app_id, att_itm_id , att_ats_id, att_remark, att_create_usr_id, att_create_timestamp, att_update_usr_id, att_update_timestamp, att_timestamp,att_rate_remark) "
                                              + " VALUES "
                                              + " (?, ?, ?, ?, ?, ?, ?, ?, ?,?) ";
    // pre-defined itm_id
    public void ins(Connection con) throws cwException, SQLException{
            if (att_app_id == 0){
                throw new cwException("invalid application id");
            }
            if (att_itm_id == 0){
                throw new cwException("invalid itm id");    
            }

            if (att_create_timestamp == null){
                att_create_timestamp = att_update_timestamp;
            }

            if (att_create_usr_id == null){
                att_create_usr_id = att_update_usr_id;
            }

            PreparedStatement stmt = con.prepareStatement(INS_ATT_SQL);
            int col=1;
            stmt.setLong(col++, att_app_id);
            stmt.setLong(col++, att_itm_id);
            stmt.setInt(col++, att_ats_id);
            stmt.setString(col++, att_remark);
//            stmt.setString(col++, att_rate);
            stmt.setString(col++, att_create_usr_id);
            stmt.setTimestamp(col++, att_create_timestamp);
            stmt.setString(col++, att_update_usr_id);
            stmt.setTimestamp(col++, att_update_timestamp);
            stmt.setTimestamp(col++, att_timestamp);
            stmt.setString(col++,att_rate_remark);

            stmt.executeUpdate();
            stmt.close();
    }
    // pre-defined itm_id
    public void save(Connection con, String upd_usr_id) throws cwException, SQLException, qdbException, cwSysMessage{
        //System.out.println("ats_id" + att_ats_id);
        Timestamp curTime = cwSQL.getTime(con);
        aeApplication app = new aeApplication();
        app.app_id = att_app_id;
        app.get(con);

        //long cos_id = dbCourse.getCosResId(con, app.app_itm_id);
        if (att_itm_id==0){
            att_itm_id = app.app_itm_id;                                            
        }
        aeItem itm = new aeItem();
        itm.itm_id = att_itm_id;
        
//        itm.getRunInd(con);
        // may not set in static saveStatus(connection, long, long...)            
//        if (itm.itm_eff_end_datetime != null){
//            att_timestamp = itm.itm_eff_end_datetime;
//        }else{
//            att_timestamp = curTime;
//        }
            
//        att_update_timestamp = curTime;
        long cos_id;
        boolean isSession = itm.getSessionInd(con);
        if (isSession){
                cos_id = 0;
        }else{
                cos_id = itm.getResId(con);
        }

        if (cos_id == 0)
            saveStatus(con);
        else
            saveStatus(con, att_app_id, att_itm_id, cos_id, app.app_ent_id, false, att_ats_id, upd_usr_id);

        // update the comment field
        att_update_timestamp = curTime;
        att_timestamp = getAttTimestamp(con);
        upd(con);
        if (att_rate!=null){
            updAttRate(con);
        }
        if (isSession){
            // no real time trigger course criteria
//            itm.get(con);
//            autoUpdateAttendanceFromSession(con, upd_usr_id, itm.itm_owner_ent_id, app.app_itm_id, att_app_id);
        }
    }
    /*
    *   save status with app_id, save the aeAttendance table only
    *   call public saveStatus for save to cosEval too
    *   pre-define att_itm_id
    */
    protected void saveStatus(Connection con) throws cwException, SQLException{
        try{
            //System.out.println("saveStatus(con).ats_id" + att_ats_id);

            Timestamp curTime = cwSQL.getTime(con);
                
            if(recalDate){
            	att_timestamp = curTime; 
            }else{
				att_timestamp = getAttTimestamp(con);
            }
            
            att_update_timestamp = curTime;    
            
            // remove the item in the Learning Plan if the status is Completed, Failed or No Show        
            aeAttendanceStatus ats = new aeAttendanceStatus();
            ats.ats_id = att_ats_id;
            ats.get(con);
            
            long grade_ent_id = 0;
            aeApplication app = new aeApplication();
                
            try {
                app.app_id = att_app_id;
                app.get(con);

                grade_ent_id = DbUserGrade.getGradeEntId(con, app.app_ent_id);
            } catch (qdbException e) {
                throw new cwException(e.toString());
            }
                
            DbLearningSoln soln = new DbLearningSoln();
            soln.lsn_ent_id = app.app_ent_id;
                
            aeItem itm = new aeItem();
            itm.itm_id = att_itm_id;
            itm.get(con);
                
            aeItemRelation myAeItemRelation = new aeItemRelation();
            myAeItemRelation.ire_child_itm_id = itm.itm_id;
            long parentID = myAeItemRelation.getParentItemId(con);
                             
            if (parentID > 0) {
                soln.lsn_itm_id = parentID;
            }
            else {
                soln.lsn_itm_id = att_itm_id;
            }
            
            aeAttendance lastAeAttendance = new aeAttendance();
            lastAeAttendance.att_app_id = this.att_app_id;
            lastAeAttendance.att_itm_id = this.att_itm_id;
            lastAeAttendance.get(con);
                    
            if (ats.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_ATTEND) || ats.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_NOSHOW) || ats.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_INCOMPLETE)){
                soln.disable(con, grade_ent_id, -1, att_update_timestamp, lastAeAttendance.att_update_timestamp);
            }
            else {
                // recover the learning soln plan only if the attendance record exist
                if (isAttExist(con)){
                    soln.enable(con, grade_ent_id, -1, att_update_timestamp, lastAeAttendance.att_update_timestamp);
                }
            }
            
            if (isAttExist(con)){
                updStatus(con);
            }else{
                ins(con);
            }
//        if (aeItem.getSessionInd(con, att_itm_id)){
//            att.setAttendFromSession(con);
//        }

//        }catch(qdbException e){
//            throw new cwException(e.getMessage());    
        }catch(cwSysMessage e){
            throw new cwException(e.getMessage());
        }
    }  
    
    // predefine itm_id, app_id
    public Timestamp getAttTimestamp(Connection con) throws cwSysMessage, SQLException{
        boolean bResult = false;
        aeAttendanceStatus ats = new aeAttendanceStatus();
        ats.ats_id = att_ats_id;
        ats.get(con);
                
        boolean isProgress = ats.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_PROGRESS);
        if (isProgress){
            att_timestamp = null;                
            bResult = true;
        }else{
            aeAttendance oldAtt = new aeAttendance();
            oldAtt.att_itm_id = this.att_itm_id;
            oldAtt.att_app_id = this.att_app_id;
            boolean hasRecord = oldAtt.get(con);
            if (hasRecord){
                // use old att_timestamp when status not change
                if (oldAtt.att_ats_id == att_ats_id){
                    att_timestamp = oldAtt.att_timestamp;
                    bResult = true; 
                }
            }
            if (!isProgress && att_timestamp == null){
                bResult = false;
            }
        }
        // recalculate
        if (!bResult){
            aeItem itm = new aeItem();
            itm.itm_id = att_itm_id;
            itm.getItem(con);
            //面授课程判断课程结束时间并且给学员设置完成时间(结训记录中的结训时间)、网上课程为当前时间
            if (itm.itm_eff_end_datetime!=null){
                att_timestamp = itm.itm_eff_end_datetime;
            }else{
                att_timestamp = cwSQL.getTime(con);
            }
        }
        return att_timestamp;
         
    }
    /*
    private static Timestamp getAttTimestamp(Connection con, long app_id) throws SQLException, cwSysMessage, qdbException{
        aeApplication app = new aeApplication();
        app.app_id = app_id;
        app.get(con);

        //long cos_id = dbCourse.getCosResId(con, app.app_itm_id);
        aeItem itm = new aeItem();
        itm.itm_id = app.app_itm_id;
        itm.getItem(con);

        Timestamp timestamp = itm.itm_eff_end_datetime;
        if (timestamp == null){
            timestamp = cwSQL.getTime(con);    
        }
        return timestamp;
    }
    */
    
    
    public static void saveStatus(Connection con, long app_id, long itm_id, long cos_id, long usr_ent_id, boolean setFinal, int ats_id, String upd_usr_id) throws cwException, SQLException, cwSysMessage{
        saveStatus(con, app_id, itm_id, cos_id, usr_ent_id, setFinal, ats_id, upd_usr_id, 0);
    }

    // itm_id = itm_id
    public static void saveStatus(Connection con, long app_id, long itm_id, long cos_id, long usr_ent_id, boolean setFinal, int ats_id, String upd_usr_id, long app_tkh_id) throws cwException, SQLException, cwSysMessage{
        try{
            //System.out.println("saveStatus(con, long..).ats_id" + ats_id);
            aeAttendance att = new aeAttendance();
            att.att_update_usr_id = upd_usr_id;
            att.att_app_id = app_id;
            att.att_ats_id = ats_id;
            att.att_itm_id = itm_id;

            boolean hasAttRecord = att.isAttExist(con); 
            
            aeAttendanceStatus ats = new aeAttendanceStatus();
            ats.ats_id = ats_id;
            ats.get(con);

            long tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, cos_id, usr_ent_id);
            dbCourseEvaluation cov = new dbCourseEvaluation();
            cov.cov_ent_id = usr_ent_id;
            cov.cov_cos_id = cos_id;
            cov.cov_tkh_id = tkh_id;
            cov.get(con);
            String oriStatus = cov.cov_status; //更改前的状态
            String updStatus = ats.ats_cov_status; //现在要修改的状态
            cov.cov_status = ats.ats_cov_status;
            if (ats.ats_attend_ind > 0){
                if (cov.cov_complete_datetime == null){
                    cov.cov_complete_datetime = cov.getCompleteTime(con);
                }
            }
            if (!cov.cov_final_ind){
                cov.cov_final_ind = setFinal;
            }
            if (hasAttRecord){
                if (tkh_id == -1){
                    throw new cwException("Failed to get tracking id.");
                }
                cov.upd(con);
            }else{
                if (app_tkh_id == 0 && (tkh_id == -1 || aeItem.getItemTkhMethod(con, itm_id).equals(aeItem.ITM_TKH_METHOD_SEPARATED) )){
                	CommonLog.debug("insert tracking history");
                        DbTrackingHistory dbTkh = new DbTrackingHistory();
                        dbTkh.tkh_usr_ent_id = usr_ent_id;
                        dbTkh.tkh_cos_res_id = cos_id;
                        dbTkh.tkh_type = DbTrackingHistory.TKH_TYPE_APPLICATION;
                        dbTkh.tkh_create_timestamp = cwSQL.getTime(con);
                        dbTkh.ins(con);
                        aeApplication.updTkhId(con, app_id, dbTkh.tkh_id);
                        dbCourseEvaluation newcov = new dbCourseEvaluation();
                        newcov.cov_ent_id = usr_ent_id;
                        newcov.cov_cos_id = cos_id;
                        newcov.cov_tkh_id = dbTkh.tkh_id;
                        newcov.cov_status = ats.ats_cov_status;
                        if (ats.ats_attend_ind > 0){
                            newcov.cov_complete_datetime = cwSQL.getTime(con);
                        }
                        newcov.cov_final_ind = setFinal;
                        newcov.ins(con);
                }else{
                    if (app_tkh_id == 0){
                        aeApplication.updTkhId(con, app_id, tkh_id);
                    }
                    cov.upd(con);
                }
            }
            att.saveStatus(con);

            if ( ats.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_NOSHOW) ) {
                aeXMessage aeXmsg = new aeXMessage();
                aeApplication app = new aeApplication();
                app.app_id = app_id;
                app.get(con);
                aeXmsg.noShowNotify(con, upd_usr_id, usr_ent_id, getNoShowCount(con, usr_ent_id), /*cos_id,*/app.app_itm_id, msgSubject);
            }
            
            //如果开启了CPD模式
            if(AccessControlWZB.hasCPDFunction()){
	            //如果不是完成状态则删除学员该条CPD时数得分
	            CpdUtilService cpdUtilService = new CpdUtilService();
	            Long upd_usr_ent_id = DbRegUser.getUsrEntIdByUsrId(con, upd_usr_id);
	            //如果修改是完成状态现在改成非完成状态，那么删除学员获得的CPD
	            if(CourseEvaluation.Completed.equalsIgnoreCase(oriStatus) &&
	            		!CourseEvaluation.Completed.equalsIgnoreCase(updStatus)){
	            	//删除学员获得的CPD
	            	CpdLrnAwardRecordService.delForOld(con, itm_id, usr_ent_id, null);
	            	//重新计算其他已完成的学习记录看看能否获得CPD时数
	            	cpdUtilService.calOtherAwardForOld(usr_ent_id, itm_id, upd_usr_ent_id, con);
	            }else if(!CourseEvaluation.Completed.equalsIgnoreCase(oriStatus)  
	            		&& CourseEvaluation.Completed.equalsIgnoreCase(updStatus)){
	            	//如果之前不是完成状态现在改成完成状态,插入CPD时数
	            	aeItem itm = aeItem.getItemById(con, itm_id);
	            	AeItemCPDItem aeItemCPDItem = AeItemCPDItemService.getByItmIdForOld(con, itm_id);
	            	if(null!=aeItemCPDItem){
	                	Long itm_ref_ind = 0l;
	                	if(itm.itm_ref_ind){
	                		itm_ref_ind =1l;
	                	}
	                	/*
	                	Long itm_exam_ind = 0l;
	                	if(itm.itm_exam_ind){
	                		itm_exam_ind =1l;
	                	}*/
	                	cpdUtilService.calAwardHoursForOld(itm_id, itm_ref_ind, itm.itm_type, app_id, usr_ent_id, 
	                			aeItemCPDItem.getAci_hours_end_date(), cov.cov_total_time, att.att_timestamp, CpdUtils.AWARD_HOURS_ACTION_LRN_AW, upd_usr_ent_id, con);
	            	}
	            }
            }

        }catch(qdbException e){
            throw new cwException(e.getMessage());
        }
    }


    static final String UPDATE_ATT_STATUS_SQL = " UPDATE aeAttendance "
                                              + " SET att_ats_id = ?, att_update_usr_id = ?, att_update_timestamp = ? , att_timestamp = ? "
                                              + " WHERE att_app_id = ? AND att_itm_id = ?";

    static final String UPDATE_ATT_SQL = " UPDATE aeAttendance "
                                              + " SET att_ats_id = ?, att_remark = ? , att_update_usr_id = ?, att_update_timestamp = ? , att_timestamp = ? ,att_rate_remark = ?"
                                              + " WHERE att_app_id = ? AND att_itm_id = ? ";
// predefine itm_id       
    public void updStatus(Connection con)throws SQLException{
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(UPDATE_ATT_STATUS_SQL);
            stmt.setInt(1, att_ats_id);
            stmt.setString(2, att_update_usr_id);
            stmt.setTimestamp(3, att_update_timestamp);
            stmt.setTimestamp(4, att_timestamp);
            stmt.setLong(5, att_app_id);
            stmt.setLong(6, att_itm_id);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

// itm_id = itm_id
    // not upd att_rate
    private void upd(Connection con)throws SQLException{
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(UPDATE_ATT_SQL);
            int index = 1;
            stmt.setInt(index++, att_ats_id);
            stmt.setString(index++, att_remark);
//          stmt.setString(index++, att_rate);
            stmt.setString(index++, att_update_usr_id);
            stmt.setTimestamp(index++, att_update_timestamp);
            stmt.setTimestamp(index++, att_timestamp);
			stmt.setString(index++,att_rate_remark);
            stmt.setLong(index++, att_app_id);
            stmt.setLong(index++, att_itm_id);
           
//          stmt.setTimestamp(index++, att_last_update_timestamp);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    // get status, remark by app_id
    // predefine att_itm_id, att_app_id
    public boolean get(Connection con) throws SQLException{
        boolean result = false;
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT usr_ste_usr_id, usr_ent_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, usr_initial_name_bil, usr_full_name_bil, usr_tel_1, ");
        SQL.append("att_app_id, att_itm_id, att_ats_id, att_remark, att_rate, att_create_timestamp, att_update_timestamp,att_update_usr_id, att_timestamp,att_rate_remark FROM aeApplication, aeAttendance, Reguser ");
        SQL.append(" WHERE att_app_id = app_id AND usr_ent_id = app_ent_id AND app_id = " + att_app_id);
        if (att_itm_id!=0){
            SQL.append(" AND att_itm_id = " + att_itm_id);
        }else{
            SQL.append(" AND att_itm_id = app_itm_id ");
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL.toString());
            rs = stmt.executeQuery();
    
            if (rs.next()){
                usr.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                usr.usr_ent_id = rs.getLong("usr_ent_id");
                usr.usr_first_name_bil = rs.getString("usr_first_name_bil");
                usr.usr_last_name_bil = rs.getString("usr_last_name_bil");
                usr.usr_display_bil = rs.getString("usr_display_bil");
                usr.usr_initial_name_bil = rs.getString("usr_initial_name_bil");
                usr.usr_full_name_bil = rs.getString("usr_full_name_bil");
                usr.usr_tel_1 = rs.getString("usr_tel_1");
    
                att_itm_id = rs.getInt("att_itm_id");            
                att_app_id = rs.getInt("att_app_id");            
                att_ats_id = rs.getInt("att_ats_id");
                att_remark = rs.getString("att_remark");
                att_rate = rs.getFloat("att_rate")+"";
                att_create_timestamp = rs.getTimestamp("att_create_timestamp");
                att_update_timestamp = rs.getTimestamp("att_update_timestamp");
                att_update_usr_id = rs.getString("att_update_usr_id");
                att_timestamp = rs.getTimestamp("att_timestamp");
                att_rate_remark =rs.getString("att_rate_remark");
                
                result = true;
            } else {
                result = false;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(rs!=null)rs.close();
        }
        
        return result;
    }
    
    public static Vector getAttLst(Connection con, long root_ent_id, long itm_id, int status_id, boolean isProgress, String orderSQL, Vector appByStatus, Vector appByPage, boolean scopeResult) throws SQLException, cwException, cwSysMessage {
        return getAttLst(con, root_ent_id, itm_id, status_id, isProgress,orderSQL, appByStatus, appByPage, scopeResult, null);
    }
    public static Vector getAttLst(Connection con, long root_ent_id, long itm_id, int status_id, boolean isProgress, String orderSQL, Vector appByStatus, Vector appByPage, boolean scopeResult, Vector matchedAppVec) throws SQLException, cwException, cwSysMessage{
        return getAttLst(con, root_ent_id, itm_id, status_id, isProgress, orderSQL, appByStatus, appByPage, scopeResult, matchedAppVec, null, null,null);
    }
    public static Vector getAttLst(Connection con, long root_ent_id, long itm_id, int status_id, boolean isProgress, String orderSQL, Vector appByStatus, Vector appByPage, boolean scopeResult, Vector matchedAppVec, WizbiniLoader wizbini, long[] app_id_lst,String userCode) throws SQLException, cwException, cwSysMessage{
        StringBuffer SQL = new StringBuffer(1024);
        Vector vtAtt = new Vector();
        Timestamp cur_time = cwSQL.getTime(con);
        long appItmId = itm_id;
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.get(con);
        if (itm.getSessionInd(con)){
            aeItemRelation ire = new aeItemRelation();
            ire.ire_child_itm_id = itm_id;
            appItmId = ire.getParentItemId(con);
        }
        SQL.append("SELECT usr_ste_usr_id, usr_ent_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, usr_initial_name_bil, usr_full_name_bil, usr_tel_1,");
        SQL.append(" app_id, att_ats_id, att_remark,att_rate_remark, att_rate, att_timestamp, att_update_timestamp, ");
        SQL.append(" ern_ancestor_ent_id ern_usg_id, erh_ancestor_ent_id erh_usg_id, ste_name,");
	    SQL.append(" att_create_timestamp, itm_content_eff_duration, itm_content_eff_end_datetime, itm_notify_email, itm_notify_days, ");
	    String temp = " case when att_timestamp is null then ";
        temp += cwSQL.datediff(con, "", cwSQL.replaceNull(con, "itm_content_eff_end_datetime", cwSQL.dateadd(con, "att_create_timestamp", "itm_content_eff_duration")));
        temp += " else 0 end as due_date ";
        SQL.append(temp);
        SQL.append(" FROM aeApplication ");
        SQL.append(" inner join aeAttendance on (att_app_id = app_id and att_itm_id = ?)");
        SQL.append(" inner join RegUser on (usr_ent_id = app_ent_id)");
        SQL.append(" inner join Entity on (ent_id = usr_ent_id)");
        SQL.append(" inner join acSite on (ste_ent_id = usr_ste_ent_id)");
        SQL.append(" inner join aeItem on (app_itm_id = itm_id)");
        SQL.append(" left join EntityRelation on (ern_child_ent_id = usr_ent_id and ern_type = ? and ern_parent_ind = ?)");
        SQL.append(" left join EntityRelationHistory on (erh_child_ent_id = usr_ent_id and erh_type = ? and erh_parent_ind = ? and erh_end_timestamp = ent_delete_timestamp)");
        SQL.append(" WHERE ");
        SQL.append(" app_id in (select max(app_id) from aeApplication, aeAttendance where att_app_id = app_id and app_itm_id = ? group by app_ent_id)");
        String tableName_1 = null;
        String tableName_2 = null;
    	if(app_id_lst != null && app_id_lst.length > 0) {
    		Vector app_temp_vec = new Vector();
            if(app_id_lst != null && app_id_lst.length > 0){
                for(long ids : app_id_lst){
                    app_temp_vec.add(new Long(ids));
                }
            }
          String colName = "tmp_id";
          tableName_1 = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
          cwSQL.insertSimpleTempTable(con, tableName_1, app_temp_vec, cwSQL.COL_TYPE_LONG);
    		SQL.append(" and app_id in ( select "+ colName + " from " + tableName_1 + ")");
    	
    	}

        if (status_id == -1) {
            // '-1' implies retrieving ALL records
            // it means do not specify the ID in the sql
        } else if (aeAttendanceStatus.isValidStatus(con, status_id, root_ent_id)) {
            // must be a valid ID
            SQL.append(" AND att_ats_id = " + status_id + " ");
        } else {
            // not a valid ID!!!
            throw new cwException("Unknown Attendance Status ID:" + status_id);
        }
        
        if (matchedAppVec != null && matchedAppVec.size() > 0) {
//            SQL.append(" AND att_app_id IN " + cwUtils.vector2list(matchedAppVec));
            String colName = "tmp_id";
            tableName_2 = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName_2, matchedAppVec, cwSQL.COL_TYPE_LONG);
            SQL.append(" and att_app_id in ( select "+ colName + " from " + tableName_2 + ")");
         
        }

        StringBuffer scopeSQL = new StringBuffer(1024);
        String appIdTableName = null;
        if(scopeResult && appByPage != null && appByPage.size() > 0){
        	String appIdColName = "tmp_app_id";
        	appIdTableName = cwSQL.createSimpleTemptable(con, appIdColName, cwSQL.COL_TYPE_LONG, 0);
               	
            if (appIdTableName != null) {
                cwSQL.insertSimpleTempTable(con, appIdTableName, appByPage, cwSQL.COL_TYPE_LONG);

                scopeSQL.append(" AND app_id IN  (")
                .append(" SELECT ").append(appIdColName)
                .append(" FROM ").append(appIdTableName).append(")");  
            }
        }

        SQL.append(scopeSQL);
        
        if(userCode!=null&&userCode!=""){
        	SQL.append(" and (lower(usr_display_bil) like ? or lower(usr_ste_usr_id) like ?)");
        }
//        if (isProgress){
//            SQL.append(getAttendanceAsDefaultSQL(itm_id));
//            SQL.append(scopeSQL);
//        }


        SQL.append(orderSQL);
        
        int index = 1;
        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(index++, itm_id);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setLong(index++, appItmId);
        
        if(userCode!=null&&userCode!=""){
        	stmt.setString(index++, "%" + userCode.toLowerCase() + "%");
        	stmt.setString(index++, "%" + userCode.toLowerCase() + "%");
        }

        ResultSet rs = stmt.executeQuery();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);

        while (rs.next()){
        	long usg_id = rs.getLong("ern_usg_id");
        	if(usg_id == 0){
        		usg_id = rs.getLong("erh_usg_id");
        	}
            aeAttendance att = new aeAttendance();

            att.usr.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            att.usr.usr_ent_id = rs.getLong("usr_ent_id");
            att.usr.usr_first_name_bil = rs.getString("usr_first_name_bil");
            att.usr.usr_last_name_bil = rs.getString("usr_last_name_bil");
            att.usr.usr_display_bil = rs.getString("usr_display_bil");
            att.usr.usr_initial_name_bil = rs.getString("usr_initial_name_bil");
            att.usr.usr_full_name_bil = rs.getString("usr_full_name_bil");
            att.usr.usr_tel_1 = rs.getString("usr_tel_1");
            att.gpm_full_path = entityfullpath.getFullPath(con, usg_id);
            if (wizbini!=null && wizbini.cfgSysSetupadv.getOrganization().isMultiple()){
                att.gpm_full_path = rs.getString("ste_name") + wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator() + att.gpm_full_path;
            }
            att.att_app_id = rs.getInt("app_id");
            att.att_ats_id = rs.getInt("att_ats_id");
            att.att_remark = rs.getString("att_remark");
            att.att_rate_remark = rs.getString("att_rate_remark");
            att.att_rate = rs.getFloat("att_rate")+ "";
            att.att_timestamp = rs.getTimestamp("att_timestamp");
            att.att_update_timestamp = rs.getTimestamp("att_update_timestamp");
            att.att_itm_id = itm_id;
            att.due_date  = rs.getInt("due_date");
//            if(att.att_timestamp != null) {
//            	att.due_date = 0;
//            }else{
//            	if(rs.getTimestamp("itm_content_eff_end_datetime") != null){
//            		att.due_date = cwUtils.dateDiff(cur_time,rs.getTimestamp("itm_content_eff_end_datetime"));
//            	}else if(rs.getInt("itm_content_eff_duration") > 0){
//            		Timestamp end_time = cwUtils.dateAdd(rs.getTimestamp("att_create_timestamp"), rs.getInt("itm_content_eff_duration"));
//            		att.due_date = cwUtils.dateDiff(cur_time, end_time);
//            	}else{
//            		att.due_date = 0;
//            	}
//            }
            vtAtt.addElement(att);
            if (!scopeResult){
                appByStatus.addElement(new Long(att.att_app_id));
            }
        }
        stmt.close();
        if (appIdTableName != null) {
            cwSQL.dropTempTable(con, appIdTableName);
        }
        if (tableName_1 != null) {
            cwSQL.dropTempTable(con, tableName_1);
        }
        if (tableName_2 != null) {
            cwSQL.dropTempTable(con, tableName_1);
        }
        return vtAtt;
    }
    public static StringBuffer getScopeSQL(Vector appByPage){
        StringBuffer scopeSQL = new StringBuffer();
        if (appByPage != null && appByPage.size() > 0){
            scopeSQL.append(" AND app_id IN  (");

            for (int i=0 ; i< appByPage.size() ;i++) {
                if (i!=0) {
                    scopeSQL.append(",");
                }
                scopeSQL.append(((Long) appByPage.elementAt(i)).longValue());
            }
            scopeSQL.append(" ) ");
        }
        return scopeSQL;
    }

/*
    public static StringBuffer getAttendanceAsDefaultSQL(long itm_id){
        StringBuffer supSQL = new StringBuffer(1024);
        supSQL.append(" UNION ");
        supSQL.append("SELECT usr_ste_usr_id, usr_ent_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, usr_initial_name_bil, usr_full_name_bil, usr_tel_1, ");
        supSQL.append(" app_id, null, null FROM aeApplication , Reguser");
        supSQL.append(" WHERE ");
        supSQL.append(" usr_ent_id = app_ent_id ");
        if (itm_id != -1){
            supSQL.append(" AND app_itm_id = ").append(itm_id);
        }
        supSQL.append(" AND app_id NOT IN (SELECT att_app_id FROM aeAttendance, aeApplication WHERE att_app_id = app_id ");
        if (itm_id != -1){
            supSQL.append(" AND app_itm_id = ").append(itm_id);
        }
        supSQL.append(" )");
        return supSQL;
    }
  */
  // itm_id = itm_id
    public StringBuffer asXML(Connection con, boolean displayEntAttribute) throws cwException{
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<attendance app_id=\"").append(att_app_id);
        xml.append("\" itm_id=\"").append(att_itm_id);
        xml.append("\" status=\"").append(att_ats_id);
        xml.append("\" rate=\"").append(cwUtils.escNull(att_rate));
        xml.append("\" att_timestamp=\"").append(cwUtils.escNull(att_timestamp));
        xml.append("\" >").append(cwUtils.NEWL);
        xml.append(usr.getUserShortXML(con, false, displayEntAttribute));
        xml.append("<full_path>").append(cwUtils.esc4XML(gpm_full_path)).append("</full_path>");
        xml.append("<remark>");
        xml.append(cwUtils.esc4XML(att_remark));
        xml.append("</remark>").append(cwUtils.NEWL);
        xml.append("<att_update_timestamp>");
        xml.append(att_update_timestamp);
        xml.append("</att_update_timestamp>").append(cwUtils.NEWL);
        xml.append("</attendance>").append(cwUtils.NEWL);
        return xml;
    }
    
    public StringBuffer asXML(Connection con, boolean displayEntAttribute, String cov_score, boolean hasOtherCriteria, boolean passOtherCriteria,boolean imp) throws cwException, SQLException {
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<attendance app_id=\"").append(att_app_id);
        xml.append("\" itm_id=\"").append(att_itm_id);
        xml.append("\" status=\"").append(att_ats_id);
        xml.append("\" due_date=\"").append(cwUtils.escZero(due_date));
        xml.append("\" rate=\"").append(cwUtils.escNull(att_rate));
        xml.append("\" att_timestamp=\"").append(cwUtils.escNull(att_timestamp));
        xml.append("\" att_update_timestamp=\"").append(cwUtils.escNull(att_update_timestamp));
        xml.append("\" >").append(cwUtils.NEWL);
        xml.append(usr.getUserShortXML(con, false, displayEntAttribute));
        xml.append("<full_path>").append(cwUtils.esc4XML(gpm_full_path)).append("</full_path>");
        xml.append("<remark>");
        xml.append(cwUtils.esc4XML(att_remark));
        xml.append("</remark>").append(cwUtils.NEWL);
        if(imp){
        	 xml.append("<att_rate_remark>");
             xml.append(cwUtils.esc4Xmlhrn(cwUtils.esc4XML(att_rate_remark)));
             xml.append("</att_rate_remark>");
        }else{
        	 xml.append("<att_rate_remark>");
             xml.append(cwUtils.esc4XML(att_rate_remark));
             xml.append("</att_rate_remark>");
        }
       
        xml.append("<cov_score>" +aeUtils.escNull(cov_score) +"</cov_score>");
        xml.append("<att_update_timestamp>");
        xml.append(att_update_timestamp);
        xml.append("</att_update_timestamp>").append(cwUtils.NEWL);
        xml.append("<has_other_criteria>");     
        xml.append(hasOtherCriteria);
        xml.append("</has_other_criteria>");
        xml.append("<pass_other_criteria>");      
        
        xml.append(passOtherCriteria);
        xml.append("</pass_other_criteria>");
        
       /* aeUserFigure aeUfg = new aeUserFigure();
        xml.append(aeUfg.getDetailAsXML(con, att_itm_id, att_app_id));
        */
        if(!aeItem.isIntegratedItem(con, att_itm_id)) {
        	xml.append(aeItem.getBeIntegratedLrnXml(con, att_itm_id, usr.usr_ent_id));
        }
        xml.append("</attendance>").append(cwUtils.NEWL);
        return xml;
    }

    private static final String GET_NO_SHOW_CNT_BY_USR = "select count(*) AS CNT from aeAttendance , aeApplication, aeAttendanceStatus "
    + " where app_id = att_app_id AND app_itm_id = att_itm_id AND att_ats_id = ats_id and ats_type = ? and app_ent_id = ? ";
// app_itm_id = att_itm_id
    public static int getNoShowCount(Connection con, long usr_ent_id) throws SQLException{
        int count = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_NO_SHOW_CNT_BY_USR);
            stmt.setString(1, aeAttendanceStatus.STATUS_TYPE_NOSHOW);
            stmt.setLong(2, usr_ent_id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("CNT");
            }
        } finally {
        	if(rs!=null)rs.close();
            if (stmt != null) {
                stmt.close();
            }
        }
        return count;
    }

    private static final String GET_ATTEND_CNT = "select count(*) AS CNT from aeAttendance , aeApplication, aeAttendanceStatus "
    + " where app_id = att_app_id and att_ats_id = ats_id AND app_itm_id = att_itm_id and ats_attend_ind = 1 and app_itm_id = ? ";
// itm_id = itm_id
    public static int getAttendCountByItem(Connection con, long itm_id, Timestamp eff_start_datetime, Timestamp eff_end_datetime) throws SQLException{
        int count = 0;
        String SQL = GET_ATTEND_CNT;
        if (eff_start_datetime!=null)
            SQL += "AND att_timestamp >= ? ";
        if (eff_end_datetime!=null)
            SQL += "AND att_timestamp <= ? ";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL);
            int col=0;
            stmt.setLong(++col, itm_id);
            if (eff_start_datetime!=null)
                stmt.setTimestamp(++col, eff_start_datetime);
            if (eff_end_datetime!=null)
                stmt.setTimestamp(++col, eff_end_datetime);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt("CNT");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return count;
    }

//    public static int getAttendCountByItem(Connection con, long itm_id) throws SQLException{
//        return getAttendCountByItem(con, itm_id, null, null);
//    }
/*
    public static void setAttend(Connection con, long app_id, long root_ent_id, String upd_usr_id) throws cwException, SQLException{
        aeAttendance att = new aeAttendance();
        att.att_update_usr_id = upd_usr_id;
        att.att_update_timestamp = cwSQL.getTime(con);
        att.att_app_id = app_id;
        att.att_ats_id = aeAttendanceStatus.getIdByType(con, root_ent_id, aeAttendanceStatus.STATUS_TYPE_ATTEND);
        att.saveStatus(con);
    }
  */
  
    private static final String sql_del_att_by_app_id_itm_id = 
        " Delete From aeAttendance Where att_app_id = ? AND att_itm_id = ? ";
    private static final String sql_del_att_by_app_id =
        " Delete From aeAttendance Where att_app_id = ? ";
    private static final String sql_del_att_by_itm_id = 
        " Delete From aeAttendance Where att_itm_id = ? ";

    public static void delByAppn(Connection con, long att_app_id, long itm_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_del_att_by_app_id_itm_id);
        stmt.setLong(1, att_app_id);
        stmt.setLong(2, itm_id);
        stmt.executeUpdate();
        stmt.close();
        aeUserFigure.delByAppn(con, att_app_id);
        return;
    }

    public static void delByAppn(Connection con, long att_app_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_del_att_by_app_id);
        stmt.setLong(1, att_app_id);
        stmt.executeUpdate();
        stmt.close();
        aeUserFigure.delByAppn(con, att_app_id);
        return;
    }
    public static void delByItem(Connection con, long itm_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_del_att_by_itm_id);
        stmt.setLong(1, itm_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    private static final String sql_get_ats_type =
        " Select ats_type From aeAttendance, aeAttendanceStatus " +
        " Where ats_id = att_ats_id " +
        " And att_app_id = ? AND att_itm_id = ? ";
    /**
    Get Attendance status of this attendance
    Pre-define variable:<BR>
    <ul>
    <li>att_app_id
    <li>att_itm_id
    </ul>
    @return ats_type
    */
    public String getAttStatus(Connection con) throws SQLException {
        String att_status;
        PreparedStatement stmt = con.prepareStatement(sql_get_ats_type);
        stmt.setLong(1, this.att_app_id);
        stmt.setLong(2, this.att_itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            att_status = rs.getString("ats_type");
        }
        else {
            att_status = null;
        }
        rs.close();
        stmt.close();
        return att_status;
    }

    /*
    *  change the attendance from progrees to COMPLETE OR INCOMPLETE
    *  must from courseEvalution
    */
    // hihihi
    // itm_id = itm_id
    public void changeProgressStatus(Connection con, long parent_itm_id, long ent_id, String cov_status, String update_usr_id,boolean setAttDate, long tkh_id) throws cwException, SQLException, cwSysMessage{
        //System.out.println("changeProgressStatus start..");
        //System.out.println("parent_itm_id:" + parent_itm_id);
        //System.out.println("cov_status:" + cov_status);

        // the status should be I/C/F only
        aeItem itm = new aeItem();
        itm.itm_id = parent_itm_id;
        itm.get(con);

        Vector itm_id_lst = aeItemRelation.getChildItemId(con, parent_itm_id);
        itm_id_lst.addElement(new Long(parent_itm_id));

        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT att_app_id , att_itm_id ");
        SQL.append("FROM aeAttendance, aeApplication , aeAttendanceStatus ");
        SQL.append("WHERE app_id = att_app_id and app_itm_id = att_itm_id and ats_id = att_ats_id ");
        //SQL.append("AND ats_type = ? ");
        SQL.append("AND app_ent_id = ? ");
        SQL.append("AND att_itm_id in " + cwUtils.vector2list(itm_id_lst));
        if(tkh_id > 0) {
            SQL.append("AND app_tkh_id = ? ") ;
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL.toString());
            //stmt.setString(1, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
            stmt.setLong(1, ent_id);
            if(tkh_id > 0) {
                stmt.setLong(2, tkh_id);
            }
            rs = stmt.executeQuery();
            
            att_update_usr_id = update_usr_id;
            // hahaha
            // set in saveStatus
//          att_update_timestamp = itm.itm_eff_end_datetime;
//          if (att_update_timestamp == null){
//              att_update_timestamp = cwSQL.getTime(con);    
//          }
            
            if (cov_status.equals(dbAiccPath.STATUS_COMPLETE) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(cov_status)){
                att_ats_id = aeAttendanceStatus.getIdByType(con, itm.itm_owner_ent_id, aeAttendanceStatus.STATUS_TYPE_ATTEND);
            }else if(dbAiccPath.STATUS_INCOMPLETE.equalsIgnoreCase(cov_status)){
				att_ats_id = aeAttendanceStatus.getIdByType(con, itm.itm_owner_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
            }else{
                att_ats_id = aeAttendanceStatus.getIdByType(con, itm.itm_owner_ent_id, aeAttendanceStatus.STATUS_TYPE_INCOMPLETE);
            }
            
            if(setAttDate){
            	//att_timestamp = cwSQL.getTime(con);
            	if(!dbAiccPath.STATUS_INCOMPLETE.equalsIgnoreCase(cov_status)){
					recalDate = true;
            	}
            	
            }
            
            while (rs.next()){
                att_app_id = rs.getLong("att_app_id");
                att_itm_id = rs.getLong("att_itm_id");
                saveStatus(con);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    public static StringBuffer getAttendanceCountByItemAsXML(Connection con, long itm_id, long root_ent_id, Vector v_ats_id) throws SQLException{
        try{
            if (v_ats_id==null){
                v_ats_id = aeAttendanceStatus.getAllStatusIdByRoot(con, root_ent_id);
            }
            StringBuffer xml = new StringBuffer();
            int count; 
            int ats_id;
            xml.append("<attendance_count_list>").append(cwUtils.NEWL);
            for (int i=0; i<v_ats_id.size(); i++){
                ats_id = ((Integer)v_ats_id.elementAt(i)).intValue();
                // kim:todo
                count = countStatus(con, itm_id, ats_id); 
                xml.append("<count status_id=\"" + ats_id + "\">" + count + "</count>");
            }
            xml.append("</attendance_count_list>").append(cwUtils.NEWL);
            return xml;
        }catch (cwException e){
           // e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
            throw new SQLException("cwException:" + e.getMessage());                
        }
    }
    
    //add for eastcom
    public void updAttMultiRemark(Connection con, long[] app_id_lst, String[] att_remark_lst)throws SQLException, cwSysMessage{
        //

        for (int i=0;i<app_id_lst.length;i++){
            att_app_id = app_id_lst[i];
            att_remark = att_remark_lst[i].trim();
            att_update_timestamp = att_update_timestamp_lst[i];
            updAttRemark(con);
        }
    }
    private void updAttRemark(Connection con)throws SQLException, cwSysMessage{
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_upd_att_remark);
            int count = 0;
            stmt.setString(++count, att_remark);
            stmt.setString(++count, att_update_usr_id);
            stmt.setTimestamp(++count, cwSQL.getTime(con));
            stmt.setLong(++count, att_app_id);
            stmt.setTimestamp(++count,att_update_timestamp);
            int cnt = stmt.executeUpdate();
            if (cnt <1){
                throw new cwSysMessage("USG001");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private void updAttRate(Connection con)throws SQLException, cwSysMessage{
        PreparedStatement stmt = null;
        try {
        	float temp = -100;
        	if(att_rate != null && att_rate.length()>0) {
				temp = Float.parseFloat(att_rate);
        	}
        	
            stmt = con.prepareStatement(SqlStatements.sql_upd_att_rate);
            int index = 1;
            if(temp != -100) {
				stmt.setFloat(index++, temp);
            } else {
            	stmt.setNull(index++, java.sql.Types.FLOAT);
            }
            stmt.setLong(index++, att_app_id);
            stmt.setLong(index++, att_itm_id);
            int cnt = stmt.executeUpdate();
            if (cnt <1){
                throw new cwSysMessage("USG001");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    // predefine app_id, itm_id 
    public float updAttRateFromSession(Connection con, long root_ent_id) throws SQLException, cwSysMessage{
        float rate = getAttRateFromSession(con, root_ent_id, att_app_id);
        att_rate = rate +"";
        updAttRate(con);
        return rate;
    }

    public static float getAttRateFromSession(Connection con, long root_ent_id, long app_id) throws SQLException{
        float rate;
        Vector vtAttendAtsId = aeAttendanceStatus.getAttendIdsVec(con, root_ent_id);
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_session_attendance);
            stmt.setBoolean(1, true);
            stmt.setLong(2, app_id);
            
            rs = stmt.executeQuery();
            int recCount = 0;
            int attendCount = 0;
            int session_ats_id;
            while (rs.next()){
                session_ats_id = rs.getInt("att_ats_id");
                if (vtAttendAtsId.contains(new Long(session_ats_id))){
                    attendCount ++;
                }
                recCount++;
            }
            
            if (recCount==0){
                rate = 100;
            }else{
                rate = (float)attendCount * 100 / recCount;
            }          
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return rate;
    }
    // prefined itm_id, app_id
    

    public static void autoUpdateAttendance(Connection con, String upd_usr_id, long root_ent_id, long app_itm_id,  boolean reScore, boolean notSetFinal) throws SQLException, cwException, cwSysMessage , qdbException, qdbErrMessage{

        ViewCourseModuleCriteria.ViewAttendDate[] modCrit = ViewCourseModuleCriteria.getManualList(con, root_ent_id, DbCourseCriteria.TYPE_COMPLETION, app_itm_id, 0);
        //System.out.println("auto_update start....");
        loginProfile prof = new loginProfile();
        prof.usr_id = upd_usr_id; 
        for (int i = 0; i < modCrit.length; i++) {
            //System.out.println("auto_update has record");
            DbCourseCriteria ccr = new DbCourseCriteria();
            ccr.ccr_id = modCrit[i].ccr_id;
            ccr.ccr_all_cond_ind = modCrit[i].ccr_all_cond_ind;
            ccr.ccr_pass_ind = modCrit[i].ccr_pass_ind;
            ccr.ccr_pass_score = (int)modCrit[i].ccr_pass_score;
            ccr.ccr_attendance_rate = modCrit[i].ccr_attendance_rate;
            ccr.ccr_itm_id = modCrit[i].ccr_itm_id;
            ccr.ccr_type = modCrit[i].ccr_type;
            ccr.ccr_upd_method = modCrit[i].ccr_upd_method;

            boolean unlimited = false;
            
            int duration = modCrit[i].itm_content_eff_duration;
            Timestamp itmContentStartDate = modCrit[i].itm_content_eff_start_datetime;
            Timestamp itmContentEndDate = modCrit[i].itm_content_eff_end_datetime;
            Timestamp startDate;
            Timestamp endDate;
            if (itmContentEndDate!=null){
                startDate = itmContentStartDate;
                endDate = itmContentEndDate;
            }else if (duration !=0){
                startDate = modCrit[i].att_create_timestamp;
                endDate = new Timestamp(startDate.getTime() + (long)duration*24*60*60*1000);
            }else{
                unlimited = true;
                startDate = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                endDate = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
            }

            boolean setFinal;
            if (notSetFinal){
                setFinal = false;
            }else{
                if (unlimited){
                    setFinal = false;
                }else{
                    Timestamp curTime = cwSQL.getTime(con);
                    if (curTime.after(endDate)){
                        setFinal = true;
                    }else{
                        setFinal = false;
                    }
                }
            }

            CourseCriteria.setAttendOhter( con,  prof, modCrit[i].cos_res_id, modCrit[i].cov_ent_id, modCrit[i].cov_tkh_id, modCrit[i].app_id, reScore,  setFinal, false, false);
        }
    }
    
    public Vector getUserEntIdByStatus(Connection con, long itm_id, Vector v_ent_id, Vector v_ats_id)
        throws SQLException {
            
            if( v_ats_id == null || v_ats_id.isEmpty() || v_ent_id == null || v_ent_id.isEmpty() )
                return new Vector();
            
            Vector v_child_itm_id = aeItemRelation.getChildItemId(con, itm_id);
            v_child_itm_id.addElement(new Long(itm_id));
            
            String SQL = " Select distinct app_ent_id "//, Max(app_id) "
                       + " From aeApplication, aeAttendance "
                       + " Where att_app_id = app_id "
                       + " And app_itm_id IN " + cwUtils.vector2list(v_child_itm_id)
                       + " And att_ats_id IN " + cwUtils.vector2list(v_ats_id)
                       + " And app_ent_id IN " + cwUtils.vector2list(v_ent_id);
                       //+ " Group By app_ent_id, app_itm_id ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            //stmt.setLong(1, itm_id);
            ResultSet rs = stmt.executeQuery();
            Vector v_ent_id_lst = new Vector();
            while(rs.next()){
                v_ent_id_lst.addElement(new Long(rs.getLong("app_ent_id")));
            }
            stmt.close();
            return v_ent_id_lst;
        }
 
    
    
    public long[] getAttendanceIdList(Connection con, long[] app_id)
        throws SQLException {
            
            if( app_id == null || app_id.length == 0 )
                return new long[0];
                
            String SQL = " SELECT att_id FROM aeAttendance "
                       + " WHERE att_app_id IN " + cwUtils.array2list(app_id);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            Vector vec = new Vector();
            while(rs.next())
                vec.addElement(new Long(rs.getLong("att_id")));
            stmt.close();
            long[] att_id_lst = new long[vec.size()];
            for(int i=0; i<att_id_lst.length; i++)
                att_id_lst[i] = ((Long)vec.elementAt(i)).longValue();
                
            return att_id_lst;
        }
    
            
    public Hashtable getAppnAtsHash(Connection con, Vector v_app_id)
        throws SQLException {
            
            if( v_app_id == null || v_app_id.isEmpty() )
                return new Hashtable();
            
            String SQL = " SELECT att_app_id, att_ats_id FROM aeAttendance " 
                       + " WHERE att_app_id IN " + cwUtils.vector2list(v_app_id);
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            Hashtable h_app_ats = new Hashtable();
            while(rs.next()){
                h_app_ats.put(new Long(rs.getLong("att_app_id")), new Long(rs.getLong("att_ats_id")));
            }
            stmt.close();
            return h_app_ats;
        }
 
    // overwrite att_timestamp, do not follow standard logic, directly save to datebase  
    // predefined att_app_id, att_itm_id, att_timestamp
    public void updAttTimestamp(Connection con, String upd_usr_id) throws SQLException{
        String sql = "UPDATE aeAttendance SET att_timestamp = ?, att_update_usr_id = ?, att_update_timestamp = ? WHERE att_itm_id = ? AND att_app_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setTimestamp(1, this.att_timestamp);
        stmt.setString(2, upd_usr_id);
        stmt.setTimestamp(3, cwSQL.getTime(con));
        stmt.setLong(4, this.att_itm_id);
        stmt.setLong(5, this.att_app_id);
        stmt.executeUpdate();
        stmt.close();        
    }

	public void updAttUpdTimestamp(Connection con,String upd_usr_id)throws SQLException{
    	String sql = "UPDATE aeAttendance SET att_update_usr_id = ?, att_update_timestamp = ? WHERE att_itm_id = ? AND att_app_id = ?  ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, upd_usr_id);
		stmt.setTimestamp(2, cwSQL.getTime(con));
		stmt.setLong(3, this.att_itm_id);
		stmt.setLong(4, this.att_app_id);
		stmt.executeUpdate();
		stmt.close();  
    }
    
	static final String SQL_SET_ATTRATE_NULL = "update aeAttendance set att_rate=? where att_itm_id = ? and att_ats_id =?";
	static final String SQL_SET_ATTRATE_REMARK_NULL = "update aeAttendance set att_rate=null where att_itm_id = ? and att_ats_id =?";
	static final String SQL_UPD_ATTRATE_REMARK = "update aeAttendance set att_rate_remark =? where att_app_id =? and att_itm_id =?";
   
   private void updAttRateRemark(Connection con) throws SQLException
   {
		PreparedStatement stmt = con.prepareStatement(SQL_UPD_ATTRATE_REMARK);
		stmt.setString(1,this.att_rate_remark);
		stmt.setLong(2,this.att_app_id);
		stmt.setLong(3,this.att_itm_id);
		stmt.executeUpdate();
		stmt.close();
   }
   
    public void setAllAttRateNull(Connection con) throws SQLException
    {
    	PreparedStatement stmt = con.prepareStatement(SQL_SET_ATTRATE_NULL);
    	stmt.setNull(1,java.sql.Types.FLOAT);
    	stmt.setLong(2,this.att_itm_id);
    	stmt.setLong(3,this.att_ats_id);
    	stmt.executeUpdate();
    	stmt.close();
    }
    
    public void setAllAttRateRemarkNull(Connection con)throws SQLException
    {
		PreparedStatement stmt = con.prepareStatement(SQL_SET_ATTRATE_REMARK_NULL);
		stmt.setLong(1,this.att_itm_id);
		stmt.setLong(2,this.att_ats_id);
		stmt.executeUpdate();
		stmt.close();
    }
    
    public void updAttRateAndRateRemark(Connection con) throws cwSysMessage,SQLException
    {
    	updAttRate(con);
    	updAttRateRemark(con);
    }
    
    public void updMultiRate(Connection con, loginProfile prof, long[] app_id_lst, String attd_rate,String attd_remark, long itm_id) throws cwException, SQLException, cwSysMessage,qdbException, qdbErrMessage{
        
        CourseCriteria ccr = new CourseCriteria();
        Timestamp curTime = cwSQL.getTime(con);
        att_update_usr_id = prof.usr_id;
        att_rate = attd_rate;
        att_rate_remark =attd_remark;
        att_itm_id = itm_id;
        Hashtable usr_ent_id = aeApplication.getEntIds( con, app_id_lst);
        long cos_res_id =dbCourse.getCosResId(con,itm_id);
        for (int i=0; i<app_id_lst.length; i++){
            att_app_id = app_id_lst[i];
            updAttRateAndRateRemark(con);
            updAttTimestamp(con,att_update_usr_id);
            long tkh_id = aeApplication.getTkhId(con, att_app_id);
            float rate = Float.parseFloat(att_rate);
//          ccr.setFromAttendanceRate(con,prof,cos_res_id,att_app_id,usr_ent_id[i],tkh_id,rate) ;    
            CourseCriteria.setAttendOhter( con,  prof, cos_res_id, ((Long)usr_ent_id.get(att_app_id)).longValue(), tkh_id, att_app_id, false, false, false, false);
         }

    }

    public String usr_attendance_record(Connection con,long itm_id,long app_id,long root_ent_id,int status) throws SQLException,cwException,cwSysMessage,qdbException, qdbErrMessage{
    	this.att_itm_id = itm_id;
    	this.att_app_id = app_id;
		long cos_id =dbCourse.getCosResId(con,itm_id);
    	get(con);
    	StringBuffer result = new StringBuffer("");
		aeItem item = new aeItem();
		item.itm_id = itm_id;
		item.getItem(con);
		result.append(item.contentAsXML(con, null));
		result.append(aeItem.getNavAsXML(con, itm_id));
    	
		dbCourseEvaluation cov = new dbCourseEvaluation();
		CourseCriteria ccr = new CourseCriteria();
		cov.cov_cos_id = cos_id;
		cov.cov_ent_id = usr.usr_ent_id;
		long tkh_id = aeApplication.getTkhId(con, att_app_id);
		cov.cov_tkh_id = tkh_id;
		cov.get(con);
		aeAttendanceStatus currentStatus = new aeAttendanceStatus();
		result.append(stateAsXML(con, root_ent_id, itm_id, status, currentStatus));
		
		boolean passOtherCriteria = ccr.checkAllCrtForOne(con,cos_id,tkh_id);
        boolean hasOtherCriteria = true;
        if(ccr.cmtLst.size() == 0){
            hasOtherCriteria = false;
        }
        result.append(asXML(con, true, cov.cov_score,hasOtherCriteria, passOtherCriteria,false));
		
		dbRegUser usr = new dbRegUser();
		usr.get(con,att_update_usr_id);
		result.append("<update_usr>");
		result.append(usr.getUserShortXML(con,false,false));
		result.append("</update_usr>");
		result.append(EvalManagement.fulfillOtherCriteriaAsXML(con,app_id,itm_id));
		return result.toString();
    }
    
	public void updAttTimestampWithRemark(Connection con, String upd_usr_id) throws SQLException{
		 String sql = "UPDATE aeAttendance SET att_timestamp = ?, att_remark = ?, att_update_usr_id = ?, att_update_timestamp = ? WHERE att_itm_id = ? AND att_app_id = ? ";
		 PreparedStatement stmt = con.prepareStatement(sql);
		 int i = 1;
		 stmt.setTimestamp(i++, this.att_timestamp);
		 stmt.setString(i++,this.att_remark);
		 stmt.setString(i++, upd_usr_id);
		 stmt.setTimestamp(i++, cwSQL.getTime(con));
		 stmt.setLong(i++, this.att_itm_id);
		 stmt.setLong(i++, this.att_app_id);
		 stmt.executeUpdate();
		 stmt.close();        
	    }
	protected void saveStatusNoFlow(Connection con) throws cwException, SQLException{
		att_timestamp = null;
		att_update_timestamp = cwSQL.getTime(con);    
		ins(con);
	}  
	/**
	 * 
	 * @param con
	 * @param app_id
	 * @param itm_id
	 * @param cos_id
	 * @param usr_ent_id
	 * @param setFinal
	 * @param ats_id
	 * @param upd_usr_id
	 * @param app_tkh_id
	 * @throws cwException
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
    public static void saveStatus_no_flow(Connection con, long app_id, long itm_id, long cos_id, long usr_ent_id, boolean setFinal, int ats_id, String upd_usr_id, long app_tkh_id) throws cwException, SQLException, cwSysMessage{
        try{
            //System.out.println("saveStatus(con, long..).ats_id" + ats_id);
            aeAttendance att = new aeAttendance();
            att.att_update_usr_id = upd_usr_id;
            att.att_app_id = app_id;
            att.att_ats_id = ats_id;
            att.att_itm_id = itm_id;
            CommonLog.debug("insert tracking history");
            DbTrackingHistory dbTkh = new DbTrackingHistory();
            dbTkh.tkh_usr_ent_id = usr_ent_id;
            dbTkh.tkh_cos_res_id = cos_id;
            dbTkh.tkh_type = DbTrackingHistory.TKH_TYPE_APPLICATION;
            dbTkh.tkh_create_timestamp = cwSQL.getTime(con);
            dbTkh.ins(con);
            aeApplication.updTkhId(con, app_id, dbTkh.tkh_id);
            dbCourseEvaluation newcov = new dbCourseEvaluation();
            newcov.cov_ent_id = usr_ent_id;
            newcov.cov_cos_id = cos_id;
            newcov.cov_tkh_id = dbTkh.tkh_id;
            newcov.cov_status = "I";
            newcov.cov_final_ind = setFinal;
            newcov.ins(con);
            att.saveStatusNoFlow(con);
        }catch(qdbException e){
            throw new cwException(e.getMessage());
        }
    }
}
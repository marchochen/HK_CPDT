package com.cw.wizbank.course;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeAttendance;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.ae.db.DbLearningSoln;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbCourseMeasurement;
import com.cw.wizbank.db.DbCourseModuleCriteria;
import com.cw.wizbank.db.DbMeasurementEvaluation;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.view.ViewCourseMeasurement;
import com.cw.wizbank.db.view.ViewCourseModuleCriteria;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbModuleType;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.Module;
import com.cwn.wizbank.services.SnsDoingService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.web.WzbApplicationContext;
public class CourseCriteria{
//    public static final String UPD_METHOD_DATE = "DATE";
//    public static final String UPD_METHOD_DURATION = "DURATION";
//    public static final String UPD_METHOD_MANUAL = "MANUAL";

//    public static final String DEFAULT_UPD_METHOD = UPD_METHOD_MANUAL;

    private  DbCourseCriteria ccr = null;
    public List cmtLst = null;
    private  Map lrnMtv = null;
    private  boolean setInitData = false;
    private  static boolean setAttDate = false;
    private  static float COV_PROGRESS_NULL=0;
    public float cov_progress;

    public void setAttDate(boolean isSet){
    	setAttDate = isSet;
    }

    public static StringBuffer getNotifyAsXML(Connection con, long ent_id, String sender_usr_id) throws qdbException{
        dbRegUser usr = new dbRegUser();
        usr.usr_ent_id = ent_id;
        usr.getByEntId(con);
        StringBuffer xml = new StringBuffer();
        xml.append("<user>");
        xml.append(cwUtils.esc4XML(usr.usr_display_bil));
        xml.append("</user>");

        dbRegUser sender = new dbRegUser();
        sender.usr_ent_id = dbRegUser.getEntId(con, sender_usr_id);
        sender.get(con);
        xml.append("<sender display_name=\"").append(cwUtils.esc4XML(sender.usr_display_bil));
        xml.append("\" email=\"").append(sender.usr_email);
        xml.append("\" usr_id=\"").append(sender.usr_id);
        xml.append("\" usr_ste_usr_id=\"").append(sender.usr_ste_usr_id);
        xml.append("\"/>").append(cwUtils.NEWL);

        dbRegUser recipient = new dbRegUser();
        recipient.usr_ent_id = ent_id;
        recipient.get(con);

        xml.append("<recipient>");
        xml.append("<entity display_name=\"").append(cwUtils.esc4XML(recipient.usr_display_bil));
        xml.append("\" email=\"").append(recipient.usr_email);
        xml.append("\" ent_id=\"").append(recipient.usr_ent_id);
        xml.append("\" usr_id=\"").append(sender.usr_id);
        xml.append("\" ste_usr_id=\"").append(sender.usr_ste_usr_id);
        xml.append("\" usr_ste_usr_id=\"").append(sender.usr_ste_usr_id);
        xml.append("\"/>").append(cwUtils.NEWL);
        xml.append("</recipient>");

        return xml;
    }
    /*
    *   for mote report
    */
    public static Vector getCriteriaLst(Connection con, long ccr_id, Timestamp eff_start_date, Timestamp eff_end_date) throws SQLException{
        Vector vtMod = new Vector();
        ViewCourseModuleCriteria.ViewByCcrId[] modCrit = null;
        if (eff_start_date == null || eff_end_date == null){
            modCrit = ViewCourseModuleCriteria.getByCcrId(con, ccr_id);
        }else{
            modCrit = ViewCourseModuleCriteria.getByCrrIdNDate(con, ccr_id, eff_start_date, eff_end_date);
        }
        for (int i = 0; i < modCrit.length; i++) {
            dbModule dbMod = new dbModule();
            dbMod.mod_res_id = modCrit[i].mod_res_id;
            dbMod.res_title = modCrit[i].res_title;
            dbMod.mod_type = modCrit[i].mod_type;
            vtMod.addElement(dbMod);
        }
        return vtMod;

    }

    public void updMultiStatus(Connection con , String usr_id,
            long[] cmr_lst, String[] status_lst, float[] contri_rate, boolean[] is_contri_by_score_lst)
    throws SQLException, cwException{
        int noOfRecord = cmr_lst.length;
        if (cmr_lst == null || status_lst == null || contri_rate == null || is_contri_by_score_lst == null ||
            cmr_lst.length != noOfRecord || contri_rate.length != noOfRecord || is_contri_by_score_lst.length != noOfRecord ){
            throw new cwException("Error in updMutliStauts, Argument error.");
        }
        Timestamp curTime = cwSQL.getTime(con);
        DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
        cmr.cmr_upd_timestamp = curTime;
        cmr.cmr_upd_usr_id = usr_id;
        for (int i=0; i<cmr_lst.length; i++){
            cmr.cmr_id = cmr_lst[i];
            cmr.cmr_status = status_lst[i];
            cmr.cmr_contri_rate = contri_rate[i];
            cmr.cmr_is_contri_by_score = is_contri_by_score_lst[i];
            cmr.upd(con);
        }
    }
    public static StringBuffer getCriteriaLstAsXML(Connection con, long ccr_id) throws SQLException{
        ViewCourseModuleCriteria.ViewByCcrId[] modCrit = ViewCourseModuleCriteria.getByCcrId(con, ccr_id);
        StringBuffer result = new StringBuffer(2000);
        result.append("<criteria_module_list>").append(cwUtils.NEWL);

        for (int i = 0; i < modCrit.length; i++) {
            result.append("<criteria_module ");
            result.append(" cmr_id=\"");
            result.append(modCrit[i].cmr_id);
            result.append("\" mod_id=\"");
            result.append(modCrit[i].mod_res_id);
            result.append("\" mod_title=\"");
            result.append(cwUtils.esc4XML(modCrit[i].res_title));
            result.append("\" mod_type=\"");
            result.append(modCrit[i].mod_type);
            result.append("\" mod_src_type=\"");
            result.append(modCrit[i].res_src_type);
            result.append("\" mod_max_score=\"");
            result.append(modCrit[i].mod_max_score);
            result.append("\" status=\"");
            result.append(modCrit[i].cmr_status);
            result.append("\" contribute_rate=\"");
            result.append(modCrit[i].cmr_contri_rate);
            result.append("\" is_contri_by_score=\"");
            result.append(modCrit[i].cmr_is_contri_by_score);
            result.append("\" upd_date=\"");
            result.append(modCrit[i].cmr_upd_timestamp);

            result.append("\" />");
        }
        result.append("</criteria_module_list>").append(cwUtils.NEWL);
        return result;
    }


    // kim changed
    public static StringBuffer getCriteriaByItmAsXML(Connection con, long itm_id, String type, long del_cmr_id) throws SQLException, cwSysMessage, qdbException{
        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = type;
        ccr.getCcrIdByItmNType(con);
        return getCriteriaAsXML(con, ccr.ccr_id, del_cmr_id);
    }

    // kim changed
    public static StringBuffer getCriteriaAsXML(Connection con, long ccr_id, long del_cmr_id) throws SQLException, cwSysMessage, qdbException{
//        if (ccr_id == 0){
//            ccr_id = dbCourse.getCosCriteriaId(con, cos_id);
//        }

        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_id = ccr_id;
        ccr.get(con);

        aeItem itm = new aeItem();
        itm.itm_id = ccr.ccr_itm_id;
        itm.getItem(con);
        long cos_id = itm.getResId(con);
        StringBuffer result = new StringBuffer(2000);
        // for backward compatible
        result.append("<course id=\"");
        result.append(cos_id);
        result.append("\" >");

        result.append("<item id=\"").append(itm.itm_id).append("\"");
        result.append(" type=\"").append(itm.itm_type).append("\"");
        result.append(" title=\"").append(cwUtils.esc4XML(itm.itm_title)).append("\"");
        result.append(" content_eff_start_datetime=\"").append(cwUtils.escNull(itm.itm_content_eff_start_datetime)).append("\"");
        result.append(" content_eff_end_datetime=\"").append(cwUtils.escNull(itm.itm_content_eff_end_datetime)).append("\"");
        result.append(" content_eff_duration=\"").append(itm.itm_content_eff_duration).append("\"");

        result.append(" create_run_ind=\"").append(itm.itm_create_run_ind).append("\"");
        result.append(" create_session_ind=\"").append(itm.itm_create_session_ind).append("\"");
        result.append(" run_ind=\"").append(itm.itm_run_ind).append("\"");
        result.append(" session_ind=\"").append(itm.itm_session_ind).append("\"");
        result.append(" apply_ind=\"").append(itm.itm_apply_ind).append("\"");
        result.append(" qdb_ind=\"").append(itm.itm_qdb_ind).append("\"");
        result.append(" auto_enrol_qdb_ind=\"").append(itm.itm_auto_enrol_qdb_ind).append("\"");
        result.append(" has_attendance_ind=\"").append(itm.itm_has_attendance_ind).append("\"");
        result.append(" completion_criteria_ind=\"").append(itm.itm_completion_criteria_ind).append("\"");
        boolean has_session_subordinate;
        if (itm.itm_create_run_ind){
            DbItemType dbIty = new DbItemType();
            dbIty.ity_id = itm.itm_type;
            dbIty.ity_owner_ent_id = itm.itm_owner_ent_id;
            dbIty.ity_run_ind = itm.itm_create_run_ind;
            dbIty.ity_session_ind = itm.itm_create_session_ind;
            Hashtable h_ity_inds = dbIty.getInd(con);
            has_session_subordinate = ((Boolean)h_ity_inds.get("ity_create_session_ind")).booleanValue();
        }else{
            has_session_subordinate = itm.itm_create_session_ind;
        }
        result.append(" has_session_subordinate_ind=\"").append(has_session_subordinate).append("\"");

        result.append(" />");
        // for backward compatible

        result.append("<title>").append(cwUtils.esc4XML(dbCourse.getCosTitle(con, cos_id))).append("</title>");
        if(itm.itm_qdb_ind) {
            result.append("<tob_existence value=\"").append(itm.hasToc(con)).append("\"/>");
        }
        result.append("<criteria id=\"");
        result.append(ccr.ccr_id);
        result.append("\" type=\"");
        result.append(ccr.ccr_type);
        result.append("\" duration=\"");
        if (ccr.ccr_type.equalsIgnoreCase(DbCourseCriteria.TYPE_COMPLETION)){
            result.append(itm.itm_content_eff_duration);
        }else{
            // for reminder criteria
            result.append(ccr.ccr_duration);
        }
        result.append("\" must_pass=\"");
        result.append(ccr.ccr_pass_ind);
        result.append("\" must_meet_all_cond=\"");
        result.append(ccr.ccr_all_cond_ind);
        result.append("\" pass_score_percentage=\"");
        result.append(ccr.ccr_pass_score);
        result.append("\" attendance_rate_percentage=\"");
        result.append(ccr.ccr_attendance_rate);
        result.append("\" upd_method=\"");
        result.append(ccr.ccr_upd_method);
        result.append("\" >").append(cwUtils.NEWL);
        result.append(dbModuleType.getAllModuleStatusXML()).append(cwUtils.NEWL);
        result.append(getCriteriaLstAsXML(con, ccr_id));
/*        if (ins_mod_id>0){
            dbResource res = new dbResource();
            res.res_id = ins_mod_id;
            res.get(con);

            result.append("<ins_module_lst>");
            result.append("<module id=\"").append(ins_mod_id)
                            .append("\" mod_type=\"").append(res.res_subtype)
                            .append("\" mod_title=\"").append(cwUtils.esc4XML(res.res_title))
                            .append("\" />");
            result.append("</ins_module_lst>");
        }
*/
        if (del_cmr_id>0){
            result.append("<del_module_lst>");
            result.append("<criteria_module cmr_id=\"").append(del_cmr_id).append("\" />");
            result.append("</del_module_lst>");
        }
        result.append("</criteria>").append(cwUtils.NEWL);
        result.append("</course>").append(cwUtils.NEWL);
//        System.out.println("course criteria L62 result lenght:"+ result.length());
        return result;
    }

    public void delCriteriaModule(Connection con, long[] cmr_lst) throws SQLException{
        for(int i=0; i<cmr_lst.length; i++){
            DbCourseModuleCriteria.delByCmrId(con, cmr_lst[i]);
        }
    }

    public void softDelCriteriaModule(Connection con, long[] cmr_lst, String usr_id) throws SQLException{
        for(int i=0; i<cmr_lst.length; i++){
            DbCourseModuleCriteria.softDelByCmrId(con, cmr_lst[i], usr_id);
        }
    }




    /*
    public void updCriteriaType(){


    }
    */
    public void pickModule(Connection con, long ccr_id, long mod_id, String status, float rate, boolean isContriByScore, String usr_id)throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
        cmr.cmr_ccr_id = ccr_id;
        cmr.cmr_res_id = mod_id;
        cmr.cmr_status = status;
        cmr.cmr_contri_rate = rate;
        cmr.cmr_is_contri_by_score = isContriByScore;
        cmr.cmr_create_timestamp = curTime;
        cmr.cmr_upd_timestamp = curTime;
        cmr.cmr_create_usr_id = usr_id;
        cmr.cmr_upd_usr_id = usr_id;

        cmr.ins(con);
    }
/*
    public void updCriteriaXML(){
    }
  */
    // kim changed
    public static boolean checkExist(Connection con, long itm_id, String type) throws SQLException{
        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = type;
        ccr.getCcrIdByItmNType(con);
        if (ccr.ccr_id != 0){
            return true;
        }else{
            return false;
        }

    }

    // kim changed
    public static void insCriteria(Connection con, long itm_id, String type, String upd_method, int attendance_rate,
                            int pass_score, boolean must_pass, boolean must_meet_all_cond, String usr_id , String offline_condition, boolean data_trans) throws SQLException, cwException, cwSysMessage{
        if (checkExist(con, itm_id, type) && !data_trans){
            throw new cwException("courseCriteria for item:" + itm_id + " , type: " + type + " is already exists.");
        }
        if (upd_method==null){
            upd_method = DbCourseCriteria.UPD_METHOD_AUTO;
        }
        boolean iscoslevconcls = aeItem.isCosLevContentClass(con, itm_id);
        Timestamp curTime = cwSQL.getTime(con);
        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_upd_method = upd_method;
        ccr.ccr_attendance_rate = attendance_rate;
        ccr.ccr_pass_score = pass_score;
        ccr.ccr_pass_ind = must_pass;
        ccr.ccr_all_cond_ind = must_meet_all_cond;
        ccr.ccr_upd_timestamp = curTime;
        ccr.ccr_upd_usr_id = usr_id;
        ccr.ccr_create_timestamp = curTime;
        ccr.ccr_create_usr_id = usr_id;
        ccr.ccr_offline_condition = offline_condition;
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = type;
        if (iscoslevconcls) {
            ccr.ccr_ccr_id_parent = DbCourseCriteria.getParentCcrIdByItmID(con, itm_id, ccr.ccr_type);
        }

        if(data_trans){
            ccr.ccr_id = DbCourseCriteria.getCcrIdByItmID( con, itm_id, type);
            if( ccr.ccr_id == 0 ){
                ccr.ins(con);
            }else{
                if(ccr.ccr_ccr_id_parent > 0){
                    DbCourseCriteria.updParent( con ,  ccr.ccr_id,  ccr.ccr_ccr_id_parent);
                }
            }
        }else{
            ccr.ins(con);
        }




        //create records inherited from course level
        if (iscoslevconcls) {
        	DbCourseModuleCriteria  cmr = new DbCourseModuleCriteria();
        	DbCourseMeasurement cmt =  new DbCourseMeasurement();
        	HashMap cmr_id_map = new HashMap();
        	cmr_id_map = cmr.copyCmr2ChItm( con, ccr.ccr_ccr_id_parent, ccr.ccr_id, itm_id, curTime, usr_id);
        	cmt.copyCmt2ChItm( con, ccr.ccr_ccr_id_parent,ccr.ccr_id,  cmr_id_map,curTime);
        }

//        dbCourse.updCosCriteriaId(con, cos_id, ccr.ccr_id);
    }
    // kim changed
    public static void updCourseCriteria(Connection con, long ccr_id, String upd_method, int duration, int attendance_rate,
                            int pass_score, boolean must_pass, boolean must_meet_all_cond, String usr_id) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_id = ccr_id;

        ccr.get(con);
        if (upd_method==null){
            upd_method = DbCourseCriteria.UPD_METHOD_AUTO;
        }
        ccr.ccr_upd_method = upd_method;
        ccr.ccr_duration = duration;
        ccr.ccr_attendance_rate = attendance_rate;
        ccr.ccr_pass_score = pass_score;
        ccr.ccr_pass_ind = must_pass;
        ccr.ccr_all_cond_ind = must_meet_all_cond;
        ccr.ccr_upd_timestamp = curTime;
        ccr.ccr_upd_usr_id = usr_id;
        ccr.upd(con);
    }

    // kim : tocheck
    public static boolean isDateValid(Calendar attendTime, Calendar rangeStart, Calendar rangeEnd){
        if (rangeStart == null || rangeEnd == null){
            return true;
        }
        else if (attendTime.before(rangeStart) || attendTime.after(rangeEnd)){
            return false;
        }else{
            return true;
        }
    }





    private void getDbCosCrt(Connection con,long cos_id)throws SQLException{
		if(ccr == null){
			long root_itm_id = dbCourse.getCosItemId(con, cos_id);
			ccr =  new DbCourseCriteria();
			ccr.ccr_itm_id = root_itm_id;
		    ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
		    ccr.getCcrIdByItmNType(con);
		    ccr.get(con);
		}
    }

//    private void getAeApp(Connection con,long tkh_id)throws SQLException{
//			app = new aeApplication();
//			app.app_tkh_id = tkh_id;
//			app.getWithTkhId(con);
//    }

    private void getCmtApp(Connection con){
    	if(cmtLst == null){
			cmtLst = ViewCourseMeasurement.getRelateMeasurement(con,ccr.ccr_id);
    	}

    }

    public boolean  initSetCtrData(Connection con,long cos_id,long tkh_id)throws SQLException{
		getDbCosCrt(con,cos_id);
		if (ccr.ccr_id == 0){
		    return false;
		}
		getCmtApp(con);
		if(cmtLst.size() == 0&&ccr.ccr_attendance_rate==0){
			return false;
		}
		if(lrnMtv == null){
			lrnMtv = ViewCourseMeasurement.getMsmValue(con,ccr.ccr_id);
		}
		setInitData = true;
		return true;
    }





    //update in the way of batch,this method may adapt to "new Rate,new Score,new MarkingSchema"
    public void setFromMarkingSchema(Connection con,loginProfile prof,long ccr_id,boolean reScore, boolean setFinal, boolean reset_status, boolean reset_date)
           throws SQLException,qdbException,cwSysMessage,cwException, qdbErrMessage{
         cmtLst = ViewCourseMeasurement.getRelateMeasurement(con,ccr_id);
         lrnMtv = ViewCourseMeasurement.getMsmValue(con,ccr_id);
         ViewCourseModuleCriteria.ViewAttendDate[] modCrit = ViewCourseModuleCriteria.getAppWithCoscrt(con,ccr_id);
         DbCourseCriteria ccr = new DbCourseCriteria();
         if(modCrit.length != 0) {
            //return;
             //boolean reScore = true;
             ccr.ccr_id = modCrit[0].ccr_id;
             ccr.ccr_all_cond_ind = modCrit[0].ccr_all_cond_ind;
             ccr.ccr_pass_ind = modCrit[0].ccr_pass_ind;
             ccr.ccr_pass_score = (int)modCrit[0].ccr_pass_score;
             ccr.ccr_pass_score_f =modCrit[0].ccr_pass_score;
             ccr.ccr_attendance_rate = modCrit[0].ccr_attendance_rate;
             ccr.ccr_itm_id = modCrit[0].ccr_itm_id;
             ccr.ccr_type = modCrit[0].ccr_type;
             ccr.ccr_upd_method = modCrit[0].ccr_upd_method;
             for(int i=0;i<modCrit.length;i++){
//              judge2SetEval(con,ccr,prof.usr_id, modCrit[i].cov_score,modCrit[i].cov_status,
//                            modCrit[i].cov_ent_id, modCrit[i].cos_res_id,modCrit[i].app_id, modCrit[i].cov_tkh_id,reScore,setFinal);

                setAttendOhter( con,  prof, modCrit[i].cos_res_id, modCrit[i].cov_ent_id, modCrit[i].cov_tkh_id, modCrit[i].app_id
                        ,  reScore,  setFinal,  reset_status,  reset_date);
             }
         }
         //loop te update che child ccr record
         List child_ccr = ccr.getChCcrIdList( con, ccr_id);
         for (int i = 0;i < child_ccr.size();i++){
                if ( child_ccr.get(i) != null) {
                    long ch_ccr_id = ((Long)child_ccr.get(i)).longValue();
                    setFromMarkingSchema( con, prof,ch_ccr_id, reScore, setFinal, reset_status,  reset_date);
                }
            }

    }






    public static boolean checkOrStatus(Connection con, Vector vtCmr, Calendar rangeStart, Calendar rangeEnd, long usr_ent_id, long tkh_id) throws SQLException{
        boolean passOneCond = false;
//        System.out.println("ee");
        for(int i=0; i<vtCmr.size(); i++){
  //          System.out.println("ff");

            DbCourseModuleCriteria cmr = (DbCourseModuleCriteria) vtCmr.elementAt(i);
//            System.out.println("tt");

            dbModuleEvaluation mov = new dbModuleEvaluation();
            mov.mov_ent_id = usr_ent_id;
            mov.mov_mod_id = cmr.cmr_res_id;
            mov.mov_tkh_id = tkh_id;
            boolean hasMov = mov.get(con);
            if (!hasMov){
                continue;
            }
//                        System.out.println("yy");

            Calendar attendDate = Calendar.getInstance();
            attendDate.setTime(mov.mov_last_acc_datetime);
//            System.out.println("gg");

            if (cmr.cmr_status != null){
                if (cmr.cmr_status.indexOf(mov.mov_status)!= -1 && isDateValid(attendDate, rangeStart, rangeEnd)){
                    return true;
                }else{

                }
            }
//                        System.out.println("hh");

        }
        return passOneCond;
    }



  //use from other classes directly
   public boolean checkAllCrtForOne(Connection con, long cos_id, long tkh_id)throws SQLException, qdbErrMessage, qdbException, cwSysMessage{
	  if(!initSetCtrData(con,cos_id,tkh_id)){
		   //this course's criteria has no cmt now.
		   return true;
	  }
	  DbCourseCriteria ccr =new  DbCourseCriteria ();//没用
	  return this.checkAllMsm(con,cmtLst,ccr, lrnMtv,tkh_id);
   }

   //use from other classes directly
   public boolean checkAllCrtForOne4Att(Connection con, long cos_id, long tkh_id)throws SQLException, qdbErrMessage, qdbException, cwSysMessage{
	  DbCourseCriteria ccr =new  DbCourseCriteria ();//没用
	  return this.checkAllMsm(con,cmtLst,ccr, lrnMtv,tkh_id);
   }

   private static boolean checkEffectiveCmt(List list){
   	  DbCourseMeasurement obj = null;
   	  for(int i=0,n=list.size();i<n;i++){
   	  	 obj = (DbCourseMeasurement)list.get(i);
   	  	 if(obj.getCmt_status() != null){
   	  	 	 return true;
   	  	 }
   	  }
   	  return false;
   }

    public static boolean checkAllMeasurement(Connection con, DbCourseCriteria ccr, List list, long usr_ent_id, long tkh_id) throws SQLException, qdbErrMessage, qdbException, cwSysMessage{
		boolean passAllCond = true;
		boolean passCond = true;
		DbCourseMeasurement cmt = null;
		int passCnt=0;
		ccr.passCondCnt=0;
		ccr.allCondCnt=0;
		for(int i=0; i<list.size(); i++){
			cmt = (DbCourseMeasurement) list.get(i);
			if(cmt.getCmt_status() == null){
				continue;
			}else{
				ccr.allCondCnt ++;
			}
			if(cmt.getCmt_cmr_id() == 0){
				passCond = checkOfflineMeasurement(con,cmt,usr_ent_id,tkh_id);
			}else{
				passCond = checkOnlineMeasurement(con,cmt,usr_ent_id,tkh_id);
			}
			if(!passCond){
				passAllCond = false;
			}else{
				passCnt++;
			}
		}
		ccr.passCondCnt = passCnt;
		cmt = null;
		return passAllCond;
	}

	private boolean checkAllMsm(Connection con,List cmtLst,DbCourseCriteria ccr,Map lrnMtv,long tkh_id) throws qdbErrMessage, SQLException, qdbException, cwSysMessage{
		boolean passAllCond = true;
		boolean passCond = true;
		DbCourseMeasurement cmt = null;
		DbMeasurementEvaluation obj = null;
		int passCnt=0;
		ccr.passCondCnt=0;
		ccr.allCondCnt=0;
		HashMap inner = (HashMap)lrnMtv.get(new Long(tkh_id));
/*		if(inner == null){
			return false;
		}*/
		for(int i=0,n=cmtLst.size();i<n;i++){
			cmt = (DbCourseMeasurement) cmtLst.get(i);
			if(inner!=null){
				obj = (DbMeasurementEvaluation)inner.get(new Long(cmt.getCmt_id()));
			}
			if(cmt.getCmt_status() == null){
			   continue;
			}else{
				ccr.allCondCnt++;
			}
			if(obj == null || obj.getMtv_status() == null){
				passCond = false;
			}else{
				passCond = compStatus(con,cmt.getCmt_status(),obj.getMtv_status(),cmt.getRes_id(),tkh_id);
			}
			if(!passCond){
				passAllCond=false;
			}else{
				passCnt++;
			}
		}
		ccr.passCondCnt = passCnt;
/*		if(!checkEffectiveCmt(cmtLst)){
				   return true;
		}*/
		cmt=null;
		obj=null;
		return passAllCond;
	}




	private static boolean getMsmEvalData(Connection con,DbMeasurementEvaluation dbMsmEval,long usr_ent_id,long tkh_id,long cmt_id){
		dbMsmEval.setMtv_cmt_id(cmt_id);
		dbMsmEval.setMtv_ent_id(usr_ent_id);
		dbMsmEval.setMtv_tkh_id(tkh_id);
		return dbMsmEval.fillData(con);
	}

	private static boolean checkOfflineMeasurement(Connection con,DbCourseMeasurement cmt,long usr_ent_id,long tkh_id) throws qdbErrMessage, SQLException, qdbException, cwSysMessage{
		DbMeasurementEvaluation dbMsmEval = new DbMeasurementEvaluation();
	    if(!getMsmEvalData(con,dbMsmEval,usr_ent_id,tkh_id,cmt.getCmt_id())){
	    	return false;
	    }
	    if(dbMsmEval.getMtv_status() == null){
	    	return false;
	    }
		return compStatus(con,cmt.getCmt_status(),dbMsmEval.getMtv_status(),cmt.getRes_id(),tkh_id);

	}

	private static boolean checkOnlineMeasurement(Connection con,DbCourseMeasurement cmt,long usr_ent_id,long tkh_id)throws SQLException, qdbErrMessage, qdbException, cwSysMessage {
		dbModuleEvaluation mov = new dbModuleEvaluation();
		mov.mov_ent_id = usr_ent_id;
		mov.mov_mod_id = cmt.getRes_id();
		mov.mov_tkh_id = tkh_id;
		boolean hasMov = mov.get(con);
		
		
		if (!hasMov){
			return false;
		}
		
		
		return compStatus(con, cmt.getCmt_status(),mov.mov_status, mov.mov_mod_id,tkh_id);
	}

	private static boolean compStatus(Connection con,String criteria,String current, long mod_id, long tkh_id) throws qdbErrMessage, SQLException, qdbException, cwSysMessage{
		if( mod_id > 0){
			dbModule module = new dbModule();
			module.mod_res_id = mod_id;
			module.get(con);
			if(module.mod_type != null && (dbModule.MOD_TYPE_TST.equalsIgnoreCase(module.mod_type) || dbModule.MOD_TYPE_DXT.equalsIgnoreCase(module.mod_type) || dbModule.MOD_TYPE_ASS.equalsIgnoreCase(module.mod_type))){
				if (criteria != null && criteria.indexOf(current)== -1){
					return false;
				}else{
					if("IFCP".equalsIgnoreCase(criteria)){
						if(dbProgress.getLastAttemptNbr( con, mod_id,  tkh_id) > 0){
							return true;
						}else{
							return false;
						}
					}else{
						return true;
					}
				}
			}
		}

		if (criteria != null && criteria.indexOf(current)== -1){
			return false;
		}else{
			return true;
		}

	}

    public static float reCalcNSaveScore(Connection con, Vector vtCmr, long cos_id, long usr_ent_id, long tkh_id, boolean setFinal) throws cwException, SQLException, cwSysMessage{
        try{
            float totalScore = 0;
            for(int i=0; i<vtCmr.size(); i++){
                DbCourseModuleCriteria cmr = (DbCourseModuleCriteria) vtCmr.elementAt(i);
                dbModuleEvaluation mov = new dbModuleEvaluation();
                mov.mov_cos_id = cos_id;
                mov.mov_ent_id = usr_ent_id;
                mov.mov_mod_id = cmr.cmr_res_id;
                mov.mov_tkh_id = tkh_id;
                mov.get(con);
                CommonLog.debug("totalScore : " + totalScore + " mod_id:" + mov.mov_mod_id);
                boolean thisStatusMatch = true;
                if (cmr.cmr_status != null && cmr.cmr_status.length()>0){
                    if (mov.mov_status!=null && cmr.cmr_status.indexOf(mov.mov_status)!= -1){
                        thisStatusMatch = true;
                    }else{
                    	CommonLog.debug("status not match, cmr.cmr_status:" + cmr.cmr_status + " mov_score:" + mov.mov_status);
                        thisStatusMatch = false;
                    }
                }else{
                    thisStatusMatch = true;
                }

                if (cmr.cmr_contri_rate != 0 && thisStatusMatch){
                    if (cmr.cmr_is_contri_by_score){
                        dbModule mod = new dbModule();
                        mod.res_id = mov.mov_mod_id;
                        mod.mod_res_id = mod.res_id;
                        mod.get(con);
                        if (mov.mov_score ==null){
                            mov.mov_score = "0";
                        }
                        if (mod.mod_max_score > 0 ){
                            totalScore += cmr.cmr_contri_rate * Float.valueOf(mov.mov_score).floatValue() / mod.mod_max_score;
                        }else if (Float.valueOf(mov.mov_score).floatValue() <= 100){
                            totalScore += cmr.cmr_contri_rate * Float.valueOf(mov.mov_score).floatValue() / 100;
                        }else{
                            totalScore += 0;
//                            totalScore += cmr.cmr_contri_rate;
                        }
                        CommonLog.debug("calc totalScore : " + totalScore + " mod_id:" + mov.mov_mod_id);

                    }else{
                        totalScore += cmr.cmr_contri_rate ;
                        CommonLog.debug("calc totalScore : " + totalScore + " mod_id:" + mov.mov_mod_id);
                    }
                }
            }
//            Vector vtAppId = aeApplication.getAllAppId(con, dbCourse.getCosItemId(con, cos_id), usr_ent_id);
//            long app_id = 0;
//            long tmp_app_id;
//            for (int i=0; i<vtAppId.size(); i++){
//                tmp_app_id = ((Long) vtAppId.elementAt(i)).longValue();
//                if (tmp_app_id>app_id){
//                    app_id = tmp_app_id;
//                }
//            }

            dbCourseEvaluation cov = new dbCourseEvaluation();
            cov.cov_ent_id = usr_ent_id;
            cov.cov_cos_id = cos_id;
            cov.cov_tkh_id = tkh_id;

            boolean hasRecord = cov.get(con);
            cov.cov_score = new Float(totalScore).toString();

            if (!cov.cov_final_ind){
                cov.cov_final_ind = setFinal;
            }

            // courseEvaluation record must exist

//            if (hasRecord){
                cov.upd(con);
//            }else{
//                cov.cov_status = "I";
//                cov.ins(con);
//            }
               CommonLog.debug("cov_score: " + cov.cov_score);

            return totalScore;
        }catch(qdbException e){
            throw new cwException(e.getMessage());
        }

    }


      public static float reCalcNSaveScore(Connection con, List vtCmr, long cos_id, long usr_ent_id, long tkh_id, boolean setFinal) throws cwException, SQLException, cwSysMessage{
		 try{
			float totalScore = 0;
			for(int i=0; i<vtCmr.size(); i++){
				DbCourseMeasurement cmt = (DbCourseMeasurement) vtCmr.get(i);
				if(cmt.getCmt_cmr_id() == 0){
					totalScore += calFromOffline(con,cmt,usr_ent_id,tkh_id);
				}else{
					totalScore += calFromOnline(con,cmt,usr_ent_id,tkh_id);
				}
			}


			dbCourseEvaluation cov = new dbCourseEvaluation();
			cov.cov_ent_id = usr_ent_id;
			cov.cov_cos_id = cos_id;
			cov.cov_tkh_id = tkh_id;
			boolean hasRecord = cov.get(con);
			cov.cov_score = new Float(totalScore).toString();

			if (!cov.cov_final_ind){
				cov.cov_final_ind = setFinal;
			}

			cov.upd(con);
			CommonLog.debug("cov_score: " + cov.cov_score);

			return totalScore;
		}catch(qdbException e){
			throw new cwException(e.getMessage());
		}

	}


	private float reCalcScore(List vtCmr,Map lrnMtv,long tkh_id){
		float totalScore = 0;
		DbCourseMeasurement cmt = null;
		DbMeasurementEvaluation obj = null;
		HashMap inner = (HashMap)lrnMtv.get(new Long(tkh_id));
		if(inner == null){
			return totalScore;
		}
		for(int i=0,n=vtCmr.size();i<n;i++){
			cmt = (DbCourseMeasurement) vtCmr.get(i);
			obj = (DbMeasurementEvaluation)inner.get(new Long(cmt.getCmt_id()));
			if(obj == null){
				continue;
			}
			totalScore += calFromScoreItem(cmt,obj);
		}
		return totalScore;
	}

	private void updScore(Connection con,long usr_ent_id,long cos_id,long tkh_id,float score,boolean setFinal)throws SQLException,qdbException{
		dbCourseEvaluation cov = new dbCourseEvaluation();
		cov.cov_ent_id = usr_ent_id;
		cov.cov_cos_id = cos_id;
		cov.cov_tkh_id = tkh_id;
		boolean hasRecord = cov.get(con);
		cov.cov_score = new Float(score).toString();

		if (!cov.cov_final_ind){
			cov.cov_final_ind = setFinal;
		}

		cov.upd(con);
		CommonLog.debug("cov_score: " + cov.cov_score);
	}


	private static float calFromOnline(Connection con,DbCourseMeasurement cmt,long usr_ent_id,long tkh_id)throws SQLException{
		dbModuleEvaluation mov = new dbModuleEvaluation();
		mov.mov_ent_id = usr_ent_id;
		mov.mov_mod_id = cmt.getRes_id();
		mov.mov_tkh_id = tkh_id;
		if(!mov.get(con)){
			return 0;
		}

		if(cmt.getCmt_contri_rate()!=0){
			return calFromOnline(cmt,mov);
		}
		return 0;
	}

    private static float calFromOnline(DbCourseMeasurement cmt,dbModuleEvaluation mov){
        float cur_score = 0;
        if (mov.mov_score != null) {
            cur_score = Float.valueOf(mov.mov_score).floatValue();
        }
		return caculateScore(cmt.getCmt_max_score(),cur_score,cmt.getCmt_contri_rate());
    }

	private static float calFromOffline(Connection con,DbCourseMeasurement cmt,long usr_ent_id,long tkh_id){
		DbMeasurementEvaluation dbMsmEval = new DbMeasurementEvaluation();
		if(!getMsmEvalData(con,dbMsmEval,usr_ent_id,tkh_id,cmt.getCmt_id())){
			return 0;
		}
		if(cmt.getCmt_contri_rate()==0){
			return 0;
		}else{
			return caculateScore(cmt.getCmt_max_score(),dbMsmEval.getMtv_score(),cmt.getCmt_contri_rate());
		}
	}

	private static float calFromScoreItem(DbCourseMeasurement cmt,DbMeasurementEvaluation mtv){
		return caculateScore(cmt.getCmt_max_score(),mtv.getMtv_score(),cmt.getCmt_contri_rate());
	}

	private static float caculateScore(float maxScore,float currentScore,float rate){
		if (maxScore > 0 ){
			return rate * currentScore / maxScore;
		}else if (currentScore <= 100){
			return rate * currentScore / 100;
		}else{
			return 0;
	    }
	}


    /*
    public static long getAppId(Connection con, long cos_id, long usr_ent_id) throws SQLException{
        dbCourse cos = new dbCourse();
        long itm_id = cos.getCosItemId(con, cos_id);
        long app_id = aeApplication.getAppId(con, itm_id, usr_ent_id, true);
        return app_id;
    }*/

    public static boolean checkAttendanceIsProgress(Connection con, long app_id, long itm_id, long root_ent_id) throws SQLException{
        aeAttendance att = new aeAttendance();
        att.att_app_id = app_id;
        att.att_itm_id = itm_id;
        att.get(con);  // TODO check what happen if att no exist
        // if no need to recalculate the score and
        // no need to turn attendance (
        if (att.att_ats_id == aeAttendanceStatus.getIdByType(con, root_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS))
        {
            return true;
        }else{
            return false;
        }
    }

    public static void setFinal(Connection con, long cos_id, long usr_ent_id, long tkh_id, boolean setFinal) throws cwException, SQLException{
        try{
            dbCourseEvaluation cov = new dbCourseEvaluation();
            cov.cov_ent_id = usr_ent_id;
            cov.cov_cos_id = cos_id;
            cov.cov_tkh_id = tkh_id;
            boolean hasRecord = cov.get(con);
            if (!cov.cov_final_ind){
                cov.cov_final_ind = setFinal;
            }
            cov.upd(con);
        }catch(qdbException e){
            throw new cwException(e.getMessage());
        }
    }


	public static float getCovProgress(boolean pass_ind, int att_rate,float score ,float pass_score, float current_rate,float ccr_attendance_rate,int passCondCnt,int allCondCnt){
        float total= 0;
        int passNum = 0;
        if(pass_ind){
            float score_rate=(score*100)/pass_score;
            if(score_rate>100){
                score_rate=100;
            }
            passNum++;
            total += score_rate;
        }
        if(att_rate > 0) {
            passNum++;
            float tmp_att_rate = (current_rate*100)/ccr_attendance_rate;
            if(tmp_att_rate>100){
                tmp_att_rate=100;
            }
            total += tmp_att_rate;
        }

        if (allCondCnt>0) {
            passNum++;
            total += (passCondCnt*100)/allCondCnt;
        }
        float cov_progress = 0;
        if (passNum > 0) {
            cov_progress = total / passNum;
        }
        return cov_progress;
    }







    public static String getCourseXml(Connection con, long cos_id, long tkh_id)
        throws SQLException, cwSysMessage, qdbException
    {
        dbCourse cos = new dbCourse();
        cos.cos_res_id = cos_id;
        cos.res_id = cos_id;
        cos.get(con);
        StringBuffer xml= new StringBuffer();
        xml.append("<course id=\"").append(cos_id)
           .append("\" tkh_id=\"").append(tkh_id).append("\">");
        xml.append("<title>").append(cwUtils.esc4XML(cos.res_title)).append("</title>");
        if (cos.cos_structure_xml != null) {
            xml.append(cos.cos_structure_xml);
        }
        xml.append("</course>");
        return xml.toString();
    }
//    get ccr_id by itm_id and cct_type = "COMPLETION"
	public long getCcrIdByItmNType(Connection con,long itm_id) throws SQLException, cwSysMessage, qdbException{
		DbCourseCriteria ccr = new DbCourseCriteria();
		ccr.ccr_itm_id = itm_id;
		ccr.ccr_type = "COMPLETION";
		ccr.getCcrIdByItmNType(con);
		return ccr.ccr_id;

	}

	public static void setAttendFromModule(Connection con, loginProfile prof, long mod_id, long cos_id, long usr_ent_id, long tkh_id, String status, float tracking_time, Timestamp cur_time, Timestamp last_acc_time) throws cwException, SQLException, cwSysMessage, qdbException, qdbErrMessage {
        long itm_id = dbCourse.getCosItemId(con, cos_id);
        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
        ccr.getCcrIdByItmNType(con);
        ccr.get(con);

        dbCourseEvaluation cov = new dbCourseEvaluation();
        String cov_status_old = null;
        cov.cov_cos_id = cos_id;
        cov.cov_ent_id = usr_ent_id;
        cov.cov_tkh_id = tkh_id;
        boolean hasACovRecord = cov.get(con);
        if (hasACovRecord) {
            cov_status_old = cov.cov_status;
        }

        long app_id = aeApplication.getAppIdByTkhId(con, tkh_id);

        aeAttendance att = new aeAttendance();
        att.att_itm_id = itm_id;
        att.att_app_id = app_id;
        boolean hasAttRecord = att.get(con);

        List cmtLst = ViewCourseMeasurement.getRelateMeasurement(con, ccr.ccr_id);

        boolean hasCriteriaOrScore = true;
        boolean matchOneCriteria = false;
        boolean matchOneScoredCriteria = false;

        // 如果没有设置完成准则或积分项目，则跳过完成准则检查和计算分数项目
        if (cmtLst.size() == 0 && ccr.ccr_attendance_rate == 0 && ccr.ccr_pass_score == 0) {
            hasCriteriaOrScore = false;
        }
        if (hasCriteriaOrScore) {
            // Vector vtCmr = DbCourseModuleCriteria.getByCcrId(con, ccr.ccr_id);
            // DbCourseModuleCriteria dbCosModCtr = new DbCourseModuleCriteria();
            for (int j = 0; j < cmtLst.size(); j++) {
                DbCourseMeasurement cmt = (DbCourseMeasurement) cmtLst.get(j);
                if (cmt.getRes_id() == mod_id) {
                    if (cmt.getCmt_status() != null)
                        matchOneCriteria = true; // 模块与结训条件相关
                    if (cmt.getCmt_is_contri_by_score() && cmt.getCmt_contri_rate() > 0)
                        matchOneScoredCriteria = true; // 模块与积分项目相关
                }
            }
            if (matchOneScoredCriteria) {
                for (int i = 0; i < cmtLst.size(); i++) {
                    // 如果模块与积分项目相关联,重新计算学员课程得分
                    DbCourseMeasurement cmr = (DbCourseMeasurement) cmtLst.get(i);
                    if (cmr.getRes_id() == mod_id) {
                        if (cmr.getCmt_contri_rate() > 0 && cmr.getCmt_is_contri_by_score()) {
                            float totalScore = 0;
                            for (int j = 0; j < cmtLst.size(); j++) {
                                DbCourseMeasurement cmt = (DbCourseMeasurement) cmtLst.get(j);
                                if (cmt.getCmt_is_contri_by_score() && cmt.getCmt_contri_rate() > 0) {
                                    if (cmt.getCmt_cmr_id() == 0) {
                                        totalScore += calFromOffline(con, cmt, usr_ent_id, tkh_id);
                                    } else {
                                        totalScore += calFromOnline(con, cmt, usr_ent_id, tkh_id);
                                    }
                                }
                            }
                            cov.cov_score = new Float(totalScore).toString();
                        }
                    }
                }
            }
            if ((matchOneScoredCriteria && ccr.ccr_pass_score > 0) || matchOneCriteria) {
                // 如果模块与积分项目相关，或者与与结训条件相关,则更检查完成准则
                if (cov.cov_status == null || cov.cov_status.equals(dbAiccPath.STATUS_INCOMPLETE)) {
                    boolean pass_score = true;
                    boolean pass_att = true;
                    boolean pass_cond = true;
                    if (ccr.ccr_pass_ind) {
                        if (cov.cov_score == null || (new Float(cov.cov_score).floatValue() < ccr.ccr_pass_score)) {
                            pass_score = false;
                        }
                    }
                    if (ccr.ccr_attendance_rate > 0) {
                        if (att.att_rate == null || new Float(att.att_rate).floatValue() < ccr.ccr_attendance_rate) {
                            pass_att = false;
                        }
                    }
                    if (ccr.ccr_all_cond_ind) {
                        if (!checkAllMeasurement(con, ccr, cmtLst, usr_ent_id, tkh_id)) {
                            pass_cond = false;
                        }
                    }

                    if (pass_score && pass_att && pass_cond) {
                        cov.cov_status = dbAiccPath.STATUS_COMPLETE;
                    } else {
                        if (cov.cov_status == null) {
                            cov.cov_status = dbAiccPath.STATUS_INCOMPLETE;
                        }
                    }

                    float cur_score = 0;
                    float cur_rate = 0;
                    if (cov.cov_score != null && cov.cov_score.length() > 0) {
                        cur_score = new Float(cov.cov_score).floatValue();
                    }
                    if (att.att_rate != null && att.att_rate.length() > 0) {
                        cur_rate = new Float(att.att_rate).floatValue();
                    }

                    float cov_progress = getCovProgress(ccr.ccr_pass_ind, ccr.ccr_attendance_rate, cur_score, ccr.ccr_pass_score_f, cur_rate, ccr.ccr_attendance_rate, ccr.passCondCnt, ccr.allCondCnt);
                    cov.cov_progress = cov_progress;
                }
            }
        }

        cov.cov_total_time += tracking_time;

        if (cov.cov_commence_datetime == null) {
            cov.cov_commence_datetime = cur_time;
        }

        if(last_acc_time != null){
            if(cov.cov_last_acc_datetime == null){
                cov.cov_last_acc_datetime = last_acc_time;
            }else if(last_acc_time.after(cov.cov_last_acc_datetime)){
                cov.cov_last_acc_datetime = last_acc_time;
            }
        }
        // three level: inStatus ....originalStatus.....incomplete
        if (cov.cov_status == null) {
            cov.cov_status = dbAiccPath.STATUS_INCOMPLETE;
        }
        if (cov.cov_status.equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE)) {
            if (cov.cov_complete_datetime == null) {
                cov.cov_complete_datetime = cur_time;

            }
        }
        if (cov_status_old != null && !cov.cov_status.equals(cov_status_old) || !hasACovRecord) {
            if (cov.cov_status.equals(dbAiccPath.STATUS_COMPLETE) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(cov.cov_status)) {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_ATTEND);
                //完成课程发动态
                //SnsDoingService snsDoingService = (SnsDoingService)WzbApplicationContext.getBean("snsDoingService");
        		//snsDoingService.add(itm_id, 0, cov.cov_ent_id, 0, SNS.DOING_ACTION_COMPLETED_COS, 0, SNS.MODULE_COURSE, "", 0);

            } else if (dbAiccPath.STATUS_INCOMPLETE.equalsIgnoreCase(cov.cov_status)) {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
            } else {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_INCOMPLETE);
            }
            if (cov.cov_status.equals(dbAiccPath.STATUS_COMPLETE) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(cov.cov_status)) {
                DbLearningSoln soln = new DbLearningSoln();
                soln.lsn_ent_id = usr_ent_id;
                aeItemRelation myAeItemRelation = new aeItemRelation();
                myAeItemRelation.ire_child_itm_id = itm_id;
                long parentID = myAeItemRelation.getParentItemId(con);

                if (parentID > 0) {
                    soln.lsn_itm_id = parentID;
                } else {
                    soln.lsn_itm_id = att.att_itm_id;
                }

                if (DbLearningSoln.isExist(con, soln.lsn_itm_id, usr_ent_id)) {
                    long grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
                    soln.disable(con, grade_ent_id, -1, cur_time, att.att_update_timestamp);
                }
            }

            att.att_update_usr_id = prof.usr_id;
            if (cov.cov_status.equals(dbAiccPath.STATUS_INCOMPLETE)) {
                att.att_timestamp = null;
            } else {
                att.att_timestamp = att.getAttTimestamp(con);
            }
            att.att_update_timestamp = cur_time;
            if (hasAttRecord) {
                att.updStatus(con);
            } else {
                att.ins(con);
            }
        }

        // Check if the record already exists, do update or insert
        if (hasACovRecord) {
            cov.upd(con);
        } else {
            cov.ins(con);
        }
        
        //如果开启了CPD模式
        if(AccessControlWZB.hasCPDFunction()){
        	//学员完成课程添加获得时数
	        if(dbAiccPath.STATUS_COMPLETE.equals(cov.cov_status)){
	        	CpdUtilService cpdUtilService = new CpdUtilService();
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
	            	}
	            	*/
	            	cpdUtilService.calAwardHoursForOld(itm_id,itm_ref_ind,itm.itm_type, app_id,usr_ent_id, 
	            			aeItemCPDItem.getAci_hours_end_date(), cov.cov_total_time, 
	            			att.att_timestamp, CpdUtils.AWARD_HOURS_ACTION_LRN_AW, prof.getUsr_ent_id(), con);
	        	}
	
	        }
        }
        // 根据完成状态的判断，更新课程相关的积分
        Credit.autoUpdUserCreditsByCos(con, cov_status_old, itm_id, app_id, usr_ent_id, prof.usr_id, cov);
    }

    public static void setAttendOhter(Connection con, loginProfile prof, long cos_id, long usr_ent_id, long tkh_id, long app_id, boolean reScore, boolean setFinal, boolean reset_status, boolean reset_date) throws cwException, SQLException, cwSysMessage, qdbException, qdbErrMessage {
        long itm_id = dbCourse.getCosItemId(con, cos_id);
        Timestamp cur_time = cwSQL.getTime(con);
        DbCourseCriteria ccr = new DbCourseCriteria();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
        ccr.getCcrIdByItmNType(con);
        ccr.get(con);
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.get(con);

        dbCourseEvaluation cov = new dbCourseEvaluation();
        String cov_status_old = null;
        cov.cov_cos_id = cos_id;
        cov.cov_ent_id = usr_ent_id;
        cov.cov_tkh_id = tkh_id;
        boolean hasACovRecord = cov.get(con);
        if (hasACovRecord) {
            cov_status_old = cov.cov_status;
        }
        if ("W".equalsIgnoreCase(cov_status_old)) {
            return;
        }

        if (reset_status) {
            cov.cov_status = dbAiccPath.STATUS_INCOMPLETE;
        }

        aeAttendance att = new aeAttendance();
        att.att_itm_id = itm_id;
        att.att_app_id = app_id;
        boolean hasAttRecord = att.get(con);
        List cmtLst = ViewCourseMeasurement.getRelateMeasurement(con, ccr.ccr_id);

        boolean hasCriteriaOrScore = true;

        // 如果没有设置完成准则或积分项目，则跳过完成准则检查和计算分数项目
        if (cmtLst.size() == 0 && ccr.ccr_attendance_rate == 0 && ccr.ccr_pass_score == 0) {
            hasCriteriaOrScore = false;
        }
        if (hasCriteriaOrScore) {
            // Vector vtCmr = DbCourseModuleCriteria.getByCcrId(con,
            // ccr.ccr_id);
            // DbCourseModuleCriteria dbCosModCtr = new
            // DbCourseModuleCriteria();
            if (reScore) {
                for (int i = 0; i < cmtLst.size(); i++) {
                    // 如果模块与积分项目相关联,重新计算学员课程得分
                    DbCourseMeasurement cmr = (DbCourseMeasurement) cmtLst.get(i);
                    if (cmr.getCmt_contri_rate() > 0 && cmr.getCmt_is_contri_by_score()) {
                        float totalScore = 0;
                        for (int j = 0; j < cmtLst.size(); j++) {
                            DbCourseMeasurement cmt = (DbCourseMeasurement) cmtLst.get(j);
                            if (cmt.getCmt_is_contri_by_score() && cmt.getCmt_contri_rate() > 0) {
                                if (cmt.getCmt_cmr_id() == 0) {
                                    totalScore += calFromOffline(con, cmt, usr_ent_id, tkh_id);
                                } else {
                                    totalScore += calFromOnline(con, cmt, usr_ent_id, tkh_id);
                                }
                            }
                        }
                        cov.cov_score = new Float(totalScore).toString();
                    }
                }
            }
            // 如果模块与积分项目相关，或者与与结训条件相关,则更检查完成准则
            if (cov.cov_status == null || cov.cov_status.equals(dbAiccPath.STATUS_INCOMPLETE)) {
                boolean pass_score = true;
                boolean pass_att = true;
                boolean pass_cond = true;
                if (ccr.ccr_pass_ind) {
                    if (cov.cov_score == null || (new Float(cov.cov_score).floatValue() < ccr.ccr_pass_score)) {
                        pass_score = false;
                    }
                }
                if (ccr.ccr_attendance_rate > 0) {
                    if (att.att_rate == null || new Float(att.att_rate).floatValue() < ccr.ccr_attendance_rate) {
                        pass_att = false;
                    }
                }
                if (ccr.ccr_all_cond_ind) {
                    if (!checkAllMeasurement(con, ccr, cmtLst, usr_ent_id, tkh_id)) {
                        pass_cond = false;
                    }
                }

                if (pass_score && pass_att && pass_cond) {
                    cov.cov_status = dbAiccPath.STATUS_COMPLETE;
                } else {
                    if (cov.cov_status == null) {
                        cov.cov_status = dbAiccPath.STATUS_INCOMPLETE;
                    }
                }

                float cur_score = 0;
                float cur_rate = 0;
                if (cov.cov_score != null && cov.cov_score.length() > 0) {
                    cur_score = new Float(cov.cov_score).floatValue();
                }
                if (att.att_rate != null && att.att_rate.length() > 0) {
                    cur_rate = new Float(att.att_rate).floatValue();
                }

                float cov_progress = getCovProgress(ccr.ccr_pass_ind, ccr.ccr_attendance_rate, cur_score, ccr.ccr_pass_score_f, cur_rate, ccr.ccr_attendance_rate, ccr.passCondCnt, ccr.allCondCnt);
                cov.cov_progress = cov_progress;
            }
        }

        if (!cov.cov_final_ind) {
            cov.cov_final_ind = setFinal;
        }
        // three level: inStatus ....originalStatus.....incomplete
        if (cov.cov_status == null) {
            cov.cov_status = dbAiccPath.STATUS_INCOMPLETE;
        }
        if (cov.cov_final_ind && dbAiccPath.STATUS_INCOMPLETE.equalsIgnoreCase(cov.cov_status)) {
            cov.cov_status = dbAiccPath.STATUS_FAILED;
        }
        if (cov.cov_status.equalsIgnoreCase(dbAiccPath.STATUS_COMPLETE)) {
            if (cov.cov_complete_datetime == null) {
                cov.cov_complete_datetime = cur_time;
            } else if (reset_date) {
                cov.cov_complete_datetime = cur_time;
            }
        } else {
            cov.cov_complete_datetime = null;
        }

        boolean updateCpdHours = false;
        if (cov_status_old != null && !cov.cov_status.equals(cov_status_old) || !hasACovRecord) {
            if (cov.cov_status.equals(dbAiccPath.STATUS_COMPLETE) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(cov.cov_status)) {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_ATTEND);
                //完成课程发动态
                //SnsDoingService snsDoingService = (SnsDoingService)WzbApplicationContext.getBean("snsDoingService");
        		//snsDoingService.add(itm_id, 0, cov.cov_ent_id, 0, SNS.DOING_ACTION_COMPLETED_COS, 0, SNS.MODULE_COURSE, "", 0);

            } else if (dbAiccPath.STATUS_INCOMPLETE.equalsIgnoreCase(cov.cov_status)) {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
            } else {
                att.att_ats_id = aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_INCOMPLETE);
            }
            if (cov.cov_status.equals(dbAiccPath.STATUS_COMPLETE) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(cov.cov_status)) {
                DbLearningSoln soln = new DbLearningSoln();
                soln.lsn_ent_id = usr_ent_id;
                aeItemRelation myAeItemRelation = new aeItemRelation();
                myAeItemRelation.ire_child_itm_id = itm_id;
                long parentID = myAeItemRelation.getParentItemId(con);

                if (parentID > 0) {
                    soln.lsn_itm_id = parentID;
                } else {
                    soln.lsn_itm_id = att.att_itm_id;
                }

                if (DbLearningSoln.isExist(con, soln.lsn_itm_id, usr_ent_id)) {
                    long grade_ent_id = DbUserGrade.getGradeEntId(con, usr_ent_id);
                    soln.disable(con, grade_ent_id, -1, cur_time, att.att_update_timestamp);
                }
            }

        }

        if (cov.cov_status.equals(dbAiccPath.STATUS_INCOMPLETE)) {
            att.att_timestamp = null;
        } else {
            if (att.att_timestamp == null || reset_date) {
                if (itm.itm_eff_end_datetime!=null){
                    att.att_timestamp  = itm.itm_eff_end_datetime;
                }else{
                    att.att_timestamp = cur_time;
                }
            }
        }
        att.att_update_timestamp = cur_time;
        att.att_update_usr_id = prof.usr_id;
        if (hasAttRecord) {
            att.updStatus(con);
        } else {
            att.ins(con);
        }

        // Check if the record already exists, do update or insert
        cov.cov_update_timestamp = cur_time;
        if (hasACovRecord) {
            cov.upd(con);
        } else {
            cov.ins(con);
        }



        // 根据完成状态的判断，更新课程相关的积分
        Credit.autoUpdUserCreditsByCos(con, cov_status_old, itm_id, app_id, usr_ent_id, prof.usr_id, cov);
        
        //如果开启了CPD模式
        if(AccessControlWZB.hasCPDFunction()){
        	//学员完成课程添加获得时数
	        if(dbAiccPath.STATUS_COMPLETE.equals(cov.cov_status)){
	        	CpdUtilService cpdUtilService = new CpdUtilService();
	        	itm = aeItem.getItemById(con, itm_id);
	        	AeItemCPDItem aeItemCPDItem = AeItemCPDItemService.getByItmIdForOld(con, itm_id);
	        	if(null!=aeItemCPDItem){
	            	Long itm_ref_ind = 0l;
	            	if(itm.itm_ref_ind){
	            		itm_ref_ind =1l;
	            	}
	            	cpdUtilService.calAwardHoursForOld(itm_id,itm_ref_ind,itm.itm_type, app_id,usr_ent_id, 
	            			aeItemCPDItem.getAci_hours_end_date(), cov.cov_total_time, 
	            			att.att_timestamp, CpdUtils.AWARD_HOURS_ACTION_LRN_AW, prof.getUsr_ent_id(), con);
	        	}
	
	        }
        }
    }

	public DbCourseCriteria getCcr() {
		return ccr;
	}

	public void setCcr(DbCourseCriteria ccr) {
		this.ccr = ccr;
	}
    
    
}

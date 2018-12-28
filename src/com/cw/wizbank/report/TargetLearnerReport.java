package com.cw.wizbank.report;

import java.net.*;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import javax.servlet.http.*;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.ae.aeItemRequirement;
import com.cw.wizbank.ae.aeAttendance;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewItemTargetGroup;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.DbUserClassification;
import com.cwn.wizbank.utils.CommonLog;

public class TargetLearnerReport extends LearnerReport {

    static final String SESS_TARGET_LEARNER_REPORT_PAGINATION_TIMESTAMP = "SESS_TARGET_LEARNER_REPORT_PAGINATION_TIMESTAMP";
    static final String SESS_TARGET_LEARNER_REPORT_USER_ENT_ID = "SESS_TARGET_LEARNER_REPORT_USER_ENT_ID";
    static final String SESS_TARGET_LEARNER_REPORT_USER_GROUP_FULL_PATH = "SESS_TARGET_LEARNER_REPORT_USER_GROUP_FULL_PATH";
    
    public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) 
        throws SQLException, qdbException, cwException, IOException, MalformedURLException, Exception {

            return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, Report.TARGET_LEARNER, null);

        }
    
    
    public static String getMetaData(Connection con, loginProfile prof) throws SQLException, cwSysMessage {
        StringBuffer result = new StringBuffer();
        CommonLog.debug("Target Learner Get Meta");
        result.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>").append(cwUtils.NEWL);
        result.append(getAttendanceStatusList(con, prof.root_ent_id));
        
        return result.toString();
    }
    

    public String[] getTargetLearningReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination cwPage, loginProfile prof, qdbEnv static_env, long rsp_id, boolean split_ind)
        throws SQLException, cwSysMessage, cwException {
            ViewReportSpec spec = new ViewReportSpec();
            ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
            
            String[] reportXML = getTargetLearningReportHelper(con, sess, cwPage, prof, static_env, data, split_ind);

            for(int i=0; i<reportXML.length; i++)
                reportXML[i] = super.getReport(con, request, prof, rsp_id, data.rsp_rte_id, null, reportXML[i], data.rsp_xml, Report.TARGET_LEARNER, data.rsp_title);
            return reportXML;

        }

    public String[] getTargetLearningReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination cwPage, loginProfile prof, qdbEnv static_env, long rte_id, String[] spec_name, String[] spec_value, String rsp_title, boolean split_ind)
        throws SQLException, cwSysMessage, qdbException, cwException {
            
            ViewReportSpec spec = new ViewReportSpec();
            ViewReportSpec.Data data = spec.getNewData();

            DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);
            data.rte_id = rte_id;
            data.rsp_xml = rpt_spec.rsp_xml;
            
            String[] reportXML = getTargetLearningReportHelper(con, sess, cwPage, prof, static_env, data, split_ind);
            //DbReportSpec dbSpec = Report.toSpec(0, null, spec_name, spec_value);
            for(int i=0; i<reportXML.length; i++)
                reportXML[i] = super.getReport(con, request, prof, 0, rte_id, null, reportXML[i], data.rsp_xml, Report.TARGET_LEARNER, rsp_title);
            return reportXML;
        }

    public String[] getTargetLearningReportHelper(Connection con, HttpSession sess, cwPagination cwPage, loginProfile prof, qdbEnv static_env, ViewReportSpec.Data data, boolean split_ind)
        throws SQLException, cwSysMessage, cwException {
       
            Vector v_ent_id = new Vector();
            Hashtable h_group_path = new Hashtable();
            Hashtable spec_pairs = getSpecPairs(data.rsp_xml);
            
            Timestamp ts = (Timestamp) sess.getAttribute(SESS_TARGET_LEARNER_REPORT_PAGINATION_TIMESTAMP);
            if (ts != null && ts.equals(cwPage.ts)) {
                
                v_ent_id = (Vector)sess.getAttribute(SESS_TARGET_LEARNER_REPORT_USER_ENT_ID);
                h_group_path = (Hashtable)sess.getAttribute(SESS_TARGET_LEARNER_REPORT_USER_GROUP_FULL_PATH);
                
            } else {

                Vector spec_values;
                
                //Get targeted Learner
                long itm_id = 0;
                String type = null;
                String applyMethod = null;

                if(spec_pairs.containsKey("sort_col")){
                    if(cwPage.sortCol == null)
                        cwPage.sortCol = cwPagination.esc4SortSql((String)((Vector)spec_pairs.get("sort_col")).elementAt(0));
                }

                if(spec_pairs.containsKey("sort_order")){
                    if(cwPage.sortOrder == null)
                        cwPage.sortOrder = cwPagination.esc4SortSql((String)((Vector)spec_pairs.get("sort_order")).elementAt(0));
                }

                if(spec_pairs.containsKey("itm_id")) {
                    spec_values = (Vector)spec_pairs.get("itm_id");
                    itm_id = Long.parseLong( (String)spec_values.elementAt(0) );
                }

                if(spec_pairs.containsKey("target_type")){
                    spec_values = (Vector)spec_pairs.get("target_type");
                    if( spec_values.indexOf(DbItemTargetRuleDetail.ITE_ELECTIVE) != -1 
                    && spec_values.indexOf(DbItemTargetRuleDetail.ITE_COMPULSORY) != -1 ){
                        applyMethod = null;    //type null, will not check apply method in the query
                    } else if( spec_values.indexOf(DbItemTargetRuleDetail.ITE_ELECTIVE) != -1 ){
                        applyMethod = DbItemTargetRuleDetail.ITE_ELECTIVE;
                    } else if ( spec_values.indexOf(DbItemTargetRuleDetail.ITE_COMPULSORY) != -1 ) {
                        applyMethod = DbItemTargetRuleDetail.ITE_COMPULSORY;
                    }
                }

                Vector targetedLrn = ViewItemTargetGroup.getTargetGroupsLrn(con, itm_id, null, applyMethod);
/*
System.out.println("targetedLrn ( " + targetedLrn.size() + " ) = " + targetedLrn);
*/
                //Filter the learner
                String search_criteria = null;
                Vector takenUser = null;
                Vector prereqUser = null;
                Vector exemptUser = null;
                
                if( spec_pairs.containsKey("search_criteria") ) {
                    spec_values = (Vector)spec_pairs.get("search_criteria");
                    
                    if( spec_values.indexOf("TAKEN") != -1 || spec_values.indexOf("NOT_TAKEN") != -1 ) {
                        Vector v_ats_id = new Vector();
                        v_ats_id.addElement(new Long(aeAttendanceStatus.getIdByType(con, prof.root_ent_id, aeAttendanceStatus.STATUS_TYPE_ATTEND)));
                        aeAttendance aeAtt = new aeAttendance();
                        if( spec_values.indexOf("TAKEN") != -1 ) {
                            takenUser = aeAtt.getUserEntIdByStatus(con, itm_id, targetedLrn, v_ats_id);
                        } else {
                            Vector v = aeAtt.getUserEntIdByStatus(con, itm_id, targetedLrn, v_ats_id);
                            takenUser = (Vector)targetedLrn.clone();
                            for(int i=0; i<v.size(); i++)
                                takenUser.remove((Long)v.elementAt(i));
                        }
                    } 

if(takenUser != null)
	CommonLog.debug("takenUser ( " + takenUser.size() + " ) = " + takenUser);


                    if( spec_values.indexOf("PREREQUISITE") != -1 || spec_values.indexOf("NOT_PREREQUISITE") != -1 ) {
                        aeItemRequirement atr = new aeItemRequirement();
                        prereqUser = (Vector)targetedLrn.clone();
                        for(int i=prereqUser.size()-1; i>=0; i--){
                            if( spec_values.indexOf("PREREQUISITE") != -1 ) {
                                if( !atr.checkPrerequisite(con, ((Long)prereqUser.elementAt(i)).longValue(), itm_id) )
                                    prereqUser.removeElementAt(i);
                            } else  {
                                if( atr.checkPrerequisite(con, ((Long)prereqUser.elementAt(i)).longValue(), itm_id) )
                                    prereqUser.removeElementAt(i);                                
                            }
                        }
                    }

if( prereqUser != null )
	CommonLog.debug("prereqUser ( " + prereqUser.size() + " ) = " + prereqUser);

                    if( spec_values.indexOf("EXEMPTED") != -1 || spec_values.indexOf("NOT_EXEMPTED") != -1) {
                        aeItemRequirement atr = new aeItemRequirement();
                        exemptUser = (Vector)targetedLrn.clone();
                        boolean bItemExempt = atr.checkItemExemptionExist(con, itm_id);
                        boolean bUserExempt = atr.checkUserExemptionExist(con, itm_id);
                        for(int i=exemptUser.size()-1; i>=0; i--){
                            if( spec_values.indexOf("EXEMPTED") != -1) {
                                if( !atr.checkUserExemption(con, ((Long)exemptUser.elementAt(i)).longValue(), itm_id) 
                                 && !atr.checkItemExemption(con, ((Long)exemptUser.elementAt(i)).longValue(), itm_id) )
                                    exemptUser.removeElementAt(i);
                            } else {
                                if( (atr.checkUserExemptionExist(con, itm_id) && atr.checkUserExemption(con, ((Long)exemptUser.elementAt(i)).longValue(), itm_id)) 
                                ||  (atr.checkItemExemptionExist(con, itm_id) && atr.checkItemExemption(con, ((Long)exemptUser.elementAt(i)).longValue(), itm_id)) )
                                    exemptUser.removeElementAt(i);                                
                            }
                        }
                    }

if( exemptUser != null )
	CommonLog.debug("exemptUser ( " + exemptUser.size() + " ) = " + exemptUser);

                
                //Filter the learenr by attendance status
                /*
                Vector attLearner = null;
                if( spec_pairs.containsKey("ats_id") ) {
                    spec_values = (Vector)spec_pairs.get("ats_id");
                    Vector v = new Vector();
                    for(int i=0; i<spec_values.size(); i++)
                        v.addElement(new Long((String)spec_values.elementAt(i)));
                    aeAttendance aeAtt = new aeAttendance();
                    attLearner = aeAtt.getUserEntIdByStatus(con, itm_id, targetedLrn, v);
                }
                */
                
                    boolean isOr = true;

                    if( spec_pairs.containsKey("search_criteria_condition") ) {
                        spec_values = (Vector)spec_pairs.get("search_criteria_condition");
                        String val = (String)spec_values.elementAt(0);
                        if( val.equalsIgnoreCase("OR") )
                            isOr = true;
                        else if( val.equalsIgnoreCase("AND") )
                            isOr = false;
                    }
                    
                    spec_values = (Vector)spec_pairs.get("search_criteria");
                    if( isOr ) {
                        v_ent_id = dbUtils.vectorUnion( dbUtils.vectorUnion(takenUser, prereqUser), exemptUser );
                    } else {
                        v_ent_id = targetedLrn;
                        CommonLog.debug("ent_id 1 = " + v_ent_id);
                        if( spec_values.indexOf("EXEMPTED") != -1 || spec_values.indexOf("NOT_EXEMPTED") != -1) {
                            v_ent_id = dbUtils.vectorIntersect(v_ent_id, exemptUser);
                            CommonLog.debug("ent_id 2 = " + v_ent_id);
                        }
                        if( spec_values.indexOf("PREREQUISITE") != -1 || spec_values.indexOf("NOT_PREREQUISITE") != -1 ){
                            v_ent_id = dbUtils.vectorIntersect(v_ent_id, prereqUser);
                            CommonLog.debug("ent_id 3 = " + v_ent_id);
                        }
                        if( spec_values.indexOf("TAKEN") != -1 || spec_values.indexOf("NOT_TAKEN") != -1 ){
                            v_ent_id = dbUtils.vectorIntersect(v_ent_id, takenUser);
                            CommonLog.debug("ent_id 4 = " + v_ent_id);
                        }
                    }
                } else {
                    v_ent_id = targetedLrn;
                }

                dbEntityRelation dbEr = new dbEntityRelation();
                if(v_ent_id != null && !v_ent_id.isEmpty()) {
                    v_ent_id = dbEr.sortUserList(con, v_ent_id, cwPage.sortCol, cwPage.sortOrder);
                    dbUtils.removeDuplicate(v_ent_id);
                    CommonLog.debug("v_ent_id ( " + v_ent_id.size() + " ) = " + v_ent_id);
                } else {
                    v_ent_id = new Vector();
                }
                
                
                if( spec_pairs.containsKey("usr_content_lst") ) {
                    spec_values = (Vector)spec_pairs.get("usr_content_lst");
                    if( spec_values.indexOf("USR_PARENT_USG") != -1 ) {
                        try{
                            h_group_path = dbEr.getUserGroupFullPathHash(con, v_ent_id);
                            CommonLog.debug("h_group_path = " + h_group_path);
                        }catch(Exception e){
                        	CommonLog.error(e.getMessage(),e);
                            h_group_path = new Hashtable();
                        }
                    }
                }                
                sess.setAttribute(SESS_TARGET_LEARNER_REPORT_PAGINATION_TIMESTAMP, cwSQL.getTime(con));
                sess.setAttribute(SESS_TARGET_LEARNER_REPORT_USER_ENT_ID, v_ent_id);
                sess.setAttribute(SESS_TARGET_LEARNER_REPORT_USER_GROUP_FULL_PATH, h_group_path);
                cwPage.ts = (Timestamp)sess.getAttribute(SESS_TARGET_LEARNER_REPORT_PAGINATION_TIMESTAMP);
            }
            
            String[] xml = null;
            if( split_ind ) {
                xml = getSplittedReport(con, cwPage,prof, static_env, v_ent_id, h_group_path, spec_pairs, data);
            } else {
                xml = new String[1];
                xml[0] = getReport(con, cwPage, prof, static_env, v_ent_id, h_group_path, spec_pairs, data);
            }
            return xml;
        }
    
    
    public String[] getSplittedReport(Connection con, cwPagination cwPage, loginProfile prof, qdbEnv static_env, Vector v_ent_id, Hashtable h_group_path, Hashtable spec_pairs, ViewReportSpec.Data data)
        throws SQLException, cwException {
            
            Vector v_xml = new Vector();
            if(cwPage.pageSize == 0)
                cwPage.pageSize = 10;

            cwPage.totalPage = (int)Math.ceil((double)cwPage.totalRec / cwPage.pageSize );
            for(int i=0; i<cwPage.totalPage; i++){
                cwPage.curPage = i + 1;
                v_xml.addElement(getReport(con, cwPage, prof, static_env, v_ent_id, h_group_path, spec_pairs, data));
            }
            String[] xml = new String[v_xml.size()];
            for(int i=0; i<v_xml.size(); i++)
                xml[i] = (String)v_xml.elementAt(i);
            return xml;
        }


    public String getReport(Connection con, cwPagination cwPage, loginProfile prof, qdbEnv static_env, Vector v_ent_id, Hashtable h_group_path, Hashtable spec_pairs, ViewReportSpec.Data data)
        throws SQLException, cwException {
/*
System.out.println("v_ent_id = " + v_ent_id);
System.out.println("spec pair = " + spec_pairs);
*/
            StringBuffer buf = new StringBuffer();
            if (cwPage.pageSize == 0) {
                cwPage.pageSize = 10;
            } else if( cwPage.pageSize == -1 ) {
                cwPage.pageSize = v_ent_id.size();
            }
            
            if (cwPage.curPage == 0) {
                cwPage.curPage = 1;
            }

            cwPage.totalRec = v_ent_id.size();
            
            cwPage.totalPage = (int)Math.ceil((double)cwPage.totalRec / cwPage.pageSize );
            
            int start = (cwPage.curPage - 1) * cwPage.pageSize;
            int end = cwPage.curPage * cwPage.pageSize;
            buf.append("<report_list>");
            

            Hashtable usr_grade_hash = null;
            Hashtable usr_idc_hash = null;
            //Hashtable usr_class_hash = null;
            //Vector v_usr_class = new Vector();
            Hashtable h_usr_class = new Hashtable();
            
            Vector spec_values = new Vector();
            if( spec_pairs.containsKey("usr_content_lst") ) {
                spec_values = (Vector)spec_pairs.get("usr_content_lst");
            }
            
            CommonLog.debug("spec_values = " + spec_values);
            String colName = "tmp_usr_ent_id";
            String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, v_ent_id, cwSQL.COL_TYPE_LONG);

            if( spec_values.indexOf("USR_CURRENT_UGR") != -1 )
                usr_grade_hash = dbEntityRelation.getGradeRelation(con, tableName, colName);

            if( spec_values.indexOf("IDC_PARENT_IDC") != -1 )
                usr_idc_hash = dbEntityRelation.getIdcRelation(con, tableName, colName);
                
            Vector v_class_type = DbUserClassification.getAllUserClassificationTypeInOrg(con, prof.root_ent_id, null);
            for(int i=0; i<v_class_type.size(); i++){
                if( spec_values.indexOf("USR_CURRENT_CLASS" + (i + 1)) != -1 )
                    h_usr_class.put(new Integer(i+1), dbEntityRelation.getClassRelation(con, tableName, colName, "USR_CURRENT_CLASS" + (i + 1)));
            }
            cwSQL.dropTempTable(con, tableName);

            for(int i=start; i<end && i<cwPage.totalRec; i++){
                buf.append("<record>");
                                
                dbRegUser applicant = new dbRegUser();
                applicant.usr_ent_id = ((Long) v_ent_id.elementAt(i)).longValue();
                try{
                    applicant.get(con);
                    buf.append(applicant.getUserReportXML(con, prof));
                }catch(qdbException e){
                    throw new cwException(e.getMessage());
                }

                if( spec_values.indexOf("USR_CURRENT_UGR") != -1 ) {
                    buf.append("<grade_list>");
                    buf.append("<group>").append(cwUtils.esc4XML((String)usr_grade_hash.get(applicant.usr_ent_id))).append("</group>");
                    buf.append("</grade_list>");
                }
                
                if( spec_values.indexOf("IDC_PARENT_IDC") != -1 ) {
                    buf.append("<idc_list>");
                    Vector idc_vec = (Vector)usr_idc_hash.get((Long) v_ent_id.elementAt(i));
                    if (idc_vec != null && idc_vec.size() > 0 ) {
                        for (int j=0; j<idc_vec.size(); j++) {
                            buf.append("<group>").append(cwUtils.esc4XML((String)idc_vec.elementAt(j))).append("</group>");
                        }
                    }
                    buf.append("</idc_list>");
                }
                
                Enumeration enumeration = h_usr_class.keys();
                while(enumeration.hasMoreElements()){
                    Integer typeNo = (Integer)enumeration.nextElement();
                    Hashtable _h_usr_class = (Hashtable)h_usr_class.get(typeNo);
                    buf.append("<class").append(typeNo).append("_list>");
                    Vector idc_vec = (Vector)_h_usr_class.get((Long) v_ent_id.elementAt(i));
                    if (idc_vec != null && idc_vec.size() > 0 ) {
                        for (int k=0; k<idc_vec.size(); k++) {
                            buf.append("<group>").append(cwUtils.esc4XML((String)idc_vec.elementAt(k))).append("</group>");
                        }
                    }
                    buf.append("</class").append(typeNo).append("_list>");
                }
                
                if( spec_values.indexOf("USR_PARENT_USG") != -1 )
                    buf.append("<group_fullpath>").append(cwUtils.esc4XML((String)h_group_path.get((Long)v_ent_id.elementAt(i)))).append("</group_fullpath>");

                buf.append("</record>");
            }
            
            buf.append("</report_list>");
            buf.append(cwPage.asXML());
            return buf.toString();
        }   
}
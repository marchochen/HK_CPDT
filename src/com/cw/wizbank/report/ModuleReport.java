package com.cw.wizbank.report;


import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;

import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.view.ViewModuleReport;

import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbResourceContent;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbUtils;

import com.cw.wizbank.ae.aeItemAccess;

import com.cw.wizbank.util.*;


public class ModuleReport extends ReportTemplate {
    
    static final String SPEC_KEY_COS_ID = "cos_id";
    static final String SPEC_KEY_MOD_ID = "mod_id";
    static final String SPEC_KEY_MOD_ID_LST = "mod_id_lst";
    static final String SPEC_KEY_ATTEMPT_NBR = "attempt_nbr";
    
    private class QueStat {
        long correct_cnt = 0;
        long wrong_cnt = 0;
    }
    
    public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) throws cwException, SQLException, cwException, Exception, IOException {

        return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, Report.MODULE_PREFIX, null);            
    }
     
    public static String getMetaData(Connection con, loginProfile prof) throws SQLException {
        StringBuffer result = new StringBuffer();
        
        result.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>").append(cwUtils.NEWL);
        Vector itmVec = aeItemAccess.getResponItemAsVector(con, prof.usr_ent_id, prof.current_role, aeItemAccess.ACCESS_TYPE_ROLE);
        result.append(ViewModuleReport.getAssignedCourse(con, itmVec));
        return result.toString();
    }

    public static String getModuleListAsXML(Connection con, long[] cos_id_lst, String[] mod_type_lst, long rte_id) throws SQLException, cwException
    {
        StringBuffer xml = new StringBuffer();
        xml.append("<report_template>");
        
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        data = spec.getReportTemplate(con, rte_id);

        xml.append("<template id=\"").append(data.rte_id).append("\" type=\"").append(data.rte_type).append("\">").append(cwUtils.NEWL);               
        xml.append(data.rte_title_xml).append(cwUtils.NEWL);
        xml.append("<xsl_list>").append(cwUtils.NEWL);
        xml.append("<xsl type=\"get\">").append(cwUtils.esc4XML(data.rte_get_xsl)).append("</xsl>").append(cwUtils.NEWL);
        xml.append("<xsl type=\"execute\">").append(cwUtils.esc4XML(data.rte_exe_xsl)).append("</xsl>").append(cwUtils.NEWL);
        xml.append("<xsl type=\"download\">").append(cwUtils.esc4XML(data.rte_dl_xsl)).append("</xsl>").append(cwUtils.NEWL);
        xml.append("</xsl_list>").append(cwUtils.NEWL);
        xml.append("</template>").append(cwUtils.NEWL);
        
        xml.append("<module_list>");
        if (cos_id_lst != null && cos_id_lst.length > 0 
            && mod_type_lst != null && mod_type_lst.length > 0) {
            xml.append(dbCourse.getModuleListAsXML(con, cos_id_lst, mod_type_lst));
        }
        xml.append("</module_list>");
        xml.append("</report_template>");
       
        return xml.toString();
    }
    
    /*
    Get the xml for Individual's Quiz Result.
    */
    public static String getIndividualResultAsXML(Connection con, long cos_id, long mod_id, long attempt_nbr) throws SQLException, cwSysMessage, qdbException
    {
        Vector allGroupVec = ViewModuleReport.getGroupedLeaner(con, cos_id);
        long[] mod_id_lst = new long[1];
        mod_id_lst[0] = mod_id;
        Hashtable modHash = ViewModuleReport.getLearnerProgress(con, mod_id_lst, attempt_nbr);
        Hashtable usrProgressHash = (Hashtable) modHash.get(new Long(mod_id));

        dbModule dbmod = new dbModule();
        dbmod.mod_res_id = mod_id;
        dbmod.res_id = mod_id;
        dbmod.get(con);

        StringBuffer xml = new StringBuffer();
        xml.append("<report_body>");
        xml.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>");
        String tmp_end_datetime = new String();
        if(dbmod.mod_eff_end_datetime != null){
            if(dbUtils.isMaxTimestamp(dbmod.mod_eff_end_datetime) == true){
                tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
            }
            else{
                tmp_end_datetime = dbmod.mod_eff_end_datetime.toString();
            }
        }
        xml.append("<module id=\"").append(mod_id).append("\" type=\"")
           .append(dbmod.mod_type).append("\" max_score=\"")
           .append(dbmod.mod_max_score).append("\" pass_score=\"")
           .append(dbmod.mod_pass_score).append("\" eff_start_datetime=\"")
           .append(dbmod.mod_eff_start_datetime).append("\" eff_end_datetime=\"")
           .append(tmp_end_datetime).append("\">");
        xml.append("<title>").append(cwUtils.esc4XML(dbmod.res_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(dbmod.res_title)).append("</desc>");
        xml.append("</module>");
        xml.append("<report_list>");
        Hashtable groupHash = null;
        dbUserGroup usg = null;
        Vector usrVec = null;
        dbRegUser usr = null;
        dbProgress pgr = null;
        
        float usr_score = 0;
        String atm_date = null;
        float total = 0;
        int total_cnt = 0;
        for (int i=0;i<allGroupVec.size();i++) {
            groupHash = (Hashtable) allGroupVec.elementAt(i);
            usg = (dbUserGroup) groupHash.get("GROUP");
            usrVec = (Vector) groupHash.get("USER");
            xml.append("<group id=\"").append(usg.usg_ent_id).append("\" display_bil=\"")
               .append(cwUtils.esc4XML(usg.usg_display_bil)).append("\">");
            xml.append("<user_list>");
            float group_total = 0;
            int group_cnt = 0;
            for (int j=0;j<usrVec.size();j++) {
                
                usr = (dbRegUser) usrVec.elementAt(j);
                xml.append("<user id=\"").append(usr.usr_id)
                   .append("\" ent_id=\"").append(usr.usr_ent_id)
                   .append("\" display_bil=\"").append(usr.usr_display_bil).append("\" ");

                usr_score = -1;
                atm_date = new String();
                if (usrProgressHash != null) {
                    pgr = (dbProgress) usrProgressHash.get(usr.usr_id);
                    // attempted yet and max_score > 0
                    if (pgr != null && pgr.pgr_max_score > 0) {
                        usr_score = ((float) pgr.pgr_score/(float) pgr.pgr_max_score) * 100;
                        atm_date = pgr.pgr_complete_datetime.toString();
                        group_total += usr_score;
                        group_cnt ++;
                    }
                }
                xml.append("percentage=\"").append(usr_score)
                   .append("\" complete_datetime=\"").append(atm_date)
                   .append("\"/>");
            }
            xml.append("</user_list>");
            float group_avg = -1;
            if (group_cnt > 0) {
                group_avg = group_total/group_cnt;
                total += group_total;
                total_cnt += group_cnt;
            }
            xml.append("<stat average=\"").append(group_avg).append("\"/>");
            xml.append("</group>");
        }
        
        float total_avg = -1;
        if (total_cnt > 0) {
            total_avg = total/total_cnt;
        }
        xml.append("<stat average=\"").append(total_avg).append("\"/>");
        xml.append("</report_list>");
        xml.append("</report_body>");
        return xml.toString();
    }

    /*
    Get the xml for Individual's Quiz Result.
    */
    public String getQuestionAnalysisAsXML(Connection con, long cos_id, long mod_id, long attempt_nbr) throws SQLException, qdbException, cwSysMessage
    {
        Vector usrVec = ViewModuleReport.getEnrolledLearner(con, cos_id);
        Vector queVec = dbResourceContent.getChildAss(con, mod_id);
        
        Hashtable usrProgressHash = ViewModuleReport.getLearnerProgressAttempt(con, cos_id, mod_id, attempt_nbr);
        StringBuffer xml = new StringBuffer();
        dbRegUser usr = null;
        dbProgressAttempt atm = null;
        Vector atmVec = null;

        dbModule dbmod = new dbModule();
        dbmod.mod_res_id = mod_id;
        dbmod.res_id = mod_id;
        dbmod.get(con);

        xml.append("<report_body>");
        xml.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>");
        String tmp_end_datetime = new String();
        if(dbmod.mod_eff_end_datetime != null){
            if(dbUtils.isMaxTimestamp(dbmod.mod_eff_end_datetime) == true){
                tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
            }
            else{
                tmp_end_datetime = dbmod.mod_eff_end_datetime.toString();
            }
        }
        xml.append("<module id=\"").append(mod_id).append("\" type=\"")
            .append(dbmod.mod_type).append("\" max_score=\"")
            .append(dbmod.mod_max_score).append("\" pass_score=\"")
            .append(dbmod.mod_pass_score).append("\" eff_start_datetime=\"")
            .append(dbmod.mod_eff_start_datetime).append("\" eff_end_datetime=\"")
            .append(tmp_end_datetime).append("\">");
            
        xml.append("<title>").append(cwUtils.esc4XML(dbmod.res_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(dbmod.res_title)).append("</desc>");
        xml.append("</module>");
        xml.append("<report_list>");
        
        Hashtable questionHash = new Hashtable();
        QueStat qstat = new QueStat();

        for (int i=0;i<usrVec.size();i++) {
            usr = (dbRegUser) usrVec.elementAt(i);
            
            xml.append("<user id=\"").append(usr.usr_id)
                .append("\" ent_id=\"").append(usr.usr_ent_id)
                .append("\" display_bil=\"").append(usr.usr_display_bil).append("\">");

            atmVec = (Vector) usrProgressHash.get(usr.usr_id);
            // attempted
            if (atmVec != null) {
                for (int j=0;j<atmVec.size();j++) {
                    atm = (dbProgressAttempt) atmVec.elementAt(j);
                    qstat = (QueStat) questionHash.get(new Long(atm.atm_int_res_id));
                    if (qstat == null) {
                        qstat = new QueStat();
                    }
                    
                    if (atm.atm_score == atm.atm_max_score) {
                        atm.atm_correct_ind = true;
                        qstat.correct_cnt +=  1;
                    }else {
                        atm.atm_correct_ind = false;
                        qstat.wrong_cnt += 1;
                    }
                    questionHash.put(new Long(atm.atm_int_res_id), qstat);
                    
                    xml.append("<question id=\"").append(atm.atm_int_res_id)
                       .append("\" order=\"").append(atm.atm_order)
                       .append("\" correct=\"").append(atm.atm_correct_ind)
                       .append("\"/>");
                    
                }
            }
            xml.append("</user>");
        }
        
        xml.append("<question_stat>");
        dbResourceContent rcn = null;
        float correct_percentage = 0;;
        float incorrect_percentage = 0;
        for (int i=0;i<queVec.size();i++) {
            rcn = (dbResourceContent) queVec.elementAt(i);
            qstat = (QueStat) questionHash.get(new Long(rcn.rcn_res_id_content));
            if (qstat == null || ((qstat.correct_cnt + qstat.wrong_cnt) == 0)) {
                correct_percentage = -1;
                incorrect_percentage = -1;
            }else {
                long total = qstat.correct_cnt + qstat.wrong_cnt;
                correct_percentage = ((float) qstat.correct_cnt/total) * 100;
                incorrect_percentage = ((float) qstat.wrong_cnt/total) * 100;
            }
            
            xml.append("<question id=\"").append(rcn.rcn_res_id_content)
               .append("\" order=\"").append(rcn.rcn_order)
               .append("\" correct_percentage=\"").append(correct_percentage)
               .append("\" incorrect_percentage=\"").append(incorrect_percentage)
               .append("\"/>");
        }
        
        xml.append("</question_stat>");
        xml.append("</report_list>");
        xml.append("</report_body>");
        
        return xml.toString();
    }
    
    /*
    Get the Individual's Quiz Results in a number of Quizzes
    */
    public static String getQuizzesResultAsXML(Connection con, long[] mod_id_lst, long attempt_nbr) throws SQLException
    {
        Hashtable modCourseHash = ViewModuleReport.getModuleCourse(con, mod_id_lst);
        Hashtable modInfoHash = ViewModuleReport.getModuleInfo(con, mod_id_lst);
        Vector courseVec = ViewModuleReport.getDistinctCourse(con, mod_id_lst);
        Vector usrVec = ViewModuleReport.getEnrolledLearner(con, courseVec);
        Hashtable courseEnrolHash = new Hashtable();
        Hashtable moduleAbsentHash = new Hashtable();
        long cosID;
        for (int i=0;i<courseVec.size();i++) {
            cosID = ((Long) courseVec.elementAt(i)).longValue();
            courseEnrolHash.put(new Long(cosID), ViewModuleReport.getEnrolledLearnerHash(con, cosID));
        }
        for (int i=0;i<mod_id_lst.length;i++) {
            moduleAbsentHash.put(new Long(mod_id_lst[i]), new Vector()); 
        }
        
        Hashtable modProgressHash = ViewModuleReport.getLearnerProgress(con, mod_id_lst, attempt_nbr);
        StringBuffer xml = new StringBuffer();
        xml.append("<report_body>");
        xml.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>");
        xml.append("<report_list>");
        dbRegUser usr = null;
        Hashtable usrHash = null;
        dbProgress pgr = null;
        
        // Total score for each modules
        float[] mod_usr_total = new float[mod_id_lst.length];
        for (int i=0;i<mod_id_lst.length;i++) {
            mod_usr_total[i] = 0;
        }
        
        // for each user
        for (int i=0;i<usrVec.size();i++) {
            usr = (dbRegUser) usrVec.elementAt(i);
            xml.append("<user id=\"").append(usr.usr_id)
               .append("\" ent_id=\"").append(usr.usr_ent_id)
               .append("\" display_bil=\"").append(usr.usr_display_bil).append("\">");
            float mod_total = 0;
            int mod_cnt = 0;
            // for each module
            for (int j=0;j<mod_id_lst.length;j++) {
                // find the course which the module belongs to
                Long courseID = (Long) modCourseHash.get(new Long(mod_id_lst[j]));
                // get the list of enrolled user to the course
                Hashtable enrolledUserHash = (Hashtable) courseEnrolHash.get(courseID);
                float usr_score = -1;
                // check if the user enrolled to the course
                if (enrolledUserHash.containsKey(usr.usr_id)) {

                    boolean bParticipated = false;
                    usrHash = (Hashtable) modProgressHash.get(new Long(mod_id_lst[j]));
                    if (usrHash != null) {
                        pgr = (dbProgress) usrHash.get(usr.usr_id);
                        if (pgr != null && pgr.pgr_max_score > 0) {
                            usr_score = (pgr.pgr_score/pgr.pgr_max_score)*100;
                            mod_total += usr_score;
                            mod_cnt ++;
                            bParticipated = true;
                            mod_usr_total[j] += usr_score;
                        }
                    }
                    if (!bParticipated) {
                        Vector absentVec = (Vector) moduleAbsentHash.get(new Long(mod_id_lst[j]));
                        absentVec.addElement(usr);
                        moduleAbsentHash.put(new Long(mod_id_lst[j]), absentVec);
                    }
                }
                xml.append("<module id=\"").append(mod_id_lst[j])
                   .append("\" score=\"").append(usr_score).append("\"/>");
            }
            float mod_avg = -1;
            if (mod_cnt > 0) {
                mod_avg = mod_total/mod_cnt;
            }
            xml.append("<average percentage=\"").append(mod_avg).append("\"/>");
            xml.append("</user>");
        }

        xml.append("<module_stat>");
        for (int i=0;i<mod_id_lst.length;i++) {
            Long courseID = (Long) modCourseHash.get(new Long(mod_id_lst[i]));
            Hashtable enrolledUserHash = (Hashtable) courseEnrolHash.get(courseID);
            Vector absentVec = (Vector) moduleAbsentHash.get(new Long(mod_id_lst[i]));
            dbModule dbmod = (dbModule) modInfoHash.get(new Long(mod_id_lst[i]));
            float mod_usr_avg = -1;
            int attendedCnt = enrolledUserHash.size() - absentVec.size();
            if (attendedCnt > 0) {
                mod_usr_avg = mod_usr_total[i]/attendedCnt;
            }
            xml.append("<module id=\"").append(mod_id_lst[i])
                .append("\" total=\"").append(enrolledUserHash.size())
                .append("\" attended=\"").append(attendedCnt)
                .append("\" avg_score=\"").append(mod_usr_avg)
                .append("\">");
            xml.append("<title>").append(cwUtils.esc4XML(dbmod.res_title)).append("</title>");
            xml.append("<absent_list>");
            for (int j=0;j<absentVec.size();j++) {
                usr = (dbRegUser) absentVec.elementAt(j);
                xml.append("<user id=\"").append(usr.usr_id)
                   .append("\" display_bil=\"").append(usr.usr_display_bil)
                   .append("\"/>");
            }
            xml.append("</absent_list>");
            xml.append("</module>");
        }
        xml.append("</module_stat>");
        xml.append("</report_list>");
        xml.append("</report_body>");
        return xml.toString();
    }
    
    public String[] getModuleReport(Connection con, loginProfile prof, String rpt_type, long rte_id, String[] spec_name, String[] spec_value) throws SQLException, cwSysMessage, cwException, IOException, qdbException {
        
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;
        
        long cos_id = 0;
        long mod_id = 0;
        long[] mod_id_lst = null;
        long attempt_nbr = 1;
        
        Hashtable spec_pairs = getSpecPairs(data.rsp_xml);
        Vector spec_values;
        
        if (spec_pairs.containsKey(SPEC_KEY_COS_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_COS_ID);

            cos_id = ((new Long((String)spec_values.elementAt(0)))).longValue(); 
        }

        
        if (spec_pairs.containsKey(SPEC_KEY_MOD_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_MOD_ID);

            mod_id = ((new Long((String)spec_values.elementAt(0)))).longValue(); 
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_MOD_ID_LST)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_MOD_ID_LST);
            mod_id_lst = new long[spec_values.size()];
            for (int i=0; i<spec_values.size(); i++) {
                mod_id_lst[i] = ((new Long((String)spec_values.elementAt(i)))).longValue(); 
            }
        }

        if (spec_pairs.containsKey(SPEC_KEY_ATTEMPT_NBR)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATTEMPT_NBR);

            attempt_nbr = ((new Long((String)spec_values.elementAt(0)))).longValue(); 
            
        }
        
        String[] xml = new String[1];
        if (rpt_type.equalsIgnoreCase(Report.MODULE_IND)) {
            xml[0] = getIndividualResultAsXML(con, cos_id, mod_id, attempt_nbr);
        }else if (rpt_type.equalsIgnoreCase(Report.MODULE_CMP)) {
            xml[0] = getQuizzesResultAsXML(con, mod_id_lst, attempt_nbr);
        }else if (rpt_type.equalsIgnoreCase(Report.MODULE_QUE)) {
            xml[0] = getQuestionAnalysisAsXML(con, cos_id, mod_id, attempt_nbr);
        }else {
            throw new cwException("Unkown report type.");
        }
        return xml;
    }

}
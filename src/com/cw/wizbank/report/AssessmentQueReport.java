/*
 * Created on 2006-3-21
 * Joyce Jiang
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.view.ViewReportSpec.Data;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CharUtils;
import com.cwn.wizbank.utils.FormatUtil;

public class AssessmentQueReport extends ReportTemplate {
    
    static final String SPEC_KEY_ITM_ID = "itm_id";
    static final String SPEC_KEY_MOD_ID = "mod_id";
    static final String SPEC_KEY_USR_ENT_ID = "usr_ent_id";
    static final String SPEC_KEY_USG_ENT_ID = "usg_ent_id";
    static final String SPEC_KEY_ATT_CREATE_START_DATETIME = "att_create_start_datetime";
    static final String SPEC_KEY_ATT_CREATE_END_DATETIME = "att_create_end_datetime";
    static final String SPEC_KEY_ATTEMPT_START_DATETIME = "attempt_start_datetime";
    static final String SPEC_KEY_ATTEMPT_END_DATETIME = "attempt_end_datetime";
    static final String SPEC_KEY_ATTEMPT_TYPE = "attempt_type";
    static final String SPEC_KEY_GROUP_BY = "group_by";
    static final String SPEC_KEY_CONTENT_LST = "content_lst";
    
    static final String  NUMBERED_ATTEMPTS_TYPE = "NUMBERED";
    static final String  ALL_ATTEMPTS_TYPE = "ALL";

    static final String GROUP_BY_QUESTION = "QUE";
    static final String GROUP_BY_RESOURCE_FOLDER = "RES_FDR";
    static final String GROUP_BY_QUESTION_TYPE = "QUE_TYPE";
    
    public class Attempt_times {
        public boolean not_graded = false;
        public int times;
        public long attempt_cnt;
        public long correct_cnt;
        public long incorrect_cnt;
        public long partial_correct_cnt;
        public long not_graded_cnt;
        public long usr_total_score;
        public long atm_max_score;
        public long usr_score_sum;
        public Vector learner_vec;
        public Vector correct_learner;
        public Vector que_res_id;
    
        public Attempt_times(){
            learner_vec = new Vector();
            correct_learner = new Vector();
            que_res_id = new Vector();
        }
    }


    public String[] getAssessmentReportByRsp(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, WizbiniLoader wizbini,boolean showSelection) 
        throws cwSysMessage, qdbException, SQLException, qdbErrMessage, IOException, cwException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);

        String[] reportXML = getAssessmentReportHelper(con, prof, wizbini, data, false, null, null, null,showSelection);
        for (int i = 0; i < reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, null, reportXML[i], null, Report.ASSESSMENT_QUE_GRP, null);
        }
        return reportXML;
    }

    public String[] getAssessmentReportBySpec(Connection con, HttpServletRequest request, loginProfile prof, long rte_id, String[] spec_name, String[] spec_value, WizbiniLoader wizbini,boolean showSelection) 
        throws cwSysMessage, qdbException, SQLException, qdbErrMessage, IOException, cwException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);
        
        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;
        
        String[] reportXML = getAssessmentReportHelper(con, prof, wizbini, data, false, null, null, null,showSelection);
        for (int i = 0; i < reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, null, reportXML[i], data.rsp_xml, Report.ASSESSMENT_QUE_GRP, null);
        }
        return reportXML;
    }
    
    public String getXlsFileByRsp(Connection con,loginProfile prof, WizbiniLoader wizbini, long rsp_id, String curLan, String xlsFilePath, String enCoding,boolean showSelection) throws qdbException, SQLException, qdbErrMessage, IOException, cwException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
        String[] reportFileName = getAssessmentReportHelper(con, prof, wizbini, data, true, curLan, xlsFilePath, enCoding,showSelection);
        return reportFileName[0];
    }

    public String getXlsFileBySpec(Connection con, loginProfile prof, WizbiniLoader wizbini, long rte_id, String[] spec_name, String[] spec_value, String curLan, String xlsFilePath, String enCoding,boolean showSelection) throws qdbException, SQLException, qdbErrMessage, IOException, cwException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);
        
        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;
        
        String[] reportFileName = getAssessmentReportHelper(con, prof, wizbini, data, true, curLan, xlsFilePath, enCoding,showSelection);
        return reportFileName[0];
    }
    
    private String[] getAssessmentReportHelper(Connection con, loginProfile prof, WizbiniLoader wizbini, Data data, boolean download, String curLan, String xlsFilePath, String encoding,boolean showSelection) throws qdbException, SQLException, qdbErrMessage, IOException, cwException {
        Hashtable spec_pairs = getSpecPairs(data.rsp_xml);
        
        long itm_id = 0;
        long mod_id = 0;
        String[] ent_id_lst = null;
        Timestamp att_create_start_datetime = null;
        Timestamp att_create_end_datetime = null;
        Timestamp attempt_start_datetime = null;
        Timestamp attempt_end_datetime = null;
        String[] attempt_type = null;
        String  group_by = null;
        Vector content_vec = new Vector();
        Vector spec_values;
        boolean all_user_ind = false;
        boolean answer_for_lrn = false;
        boolean answer_for_course_lrn = false;
        String lrn_scope_sql = null;
        if(wizbini.cfgTcEnabled) {
        	if(spec_pairs.containsKey("all_user_ind")) {
        		   spec_values = (Vector)spec_pairs.get("all_user_ind");
    		   if(((String)spec_values.get(0)).equals("1")) {
    			   all_user_ind  = true;
    		   }
        	}
        	if(spec_pairs.containsKey("answer_for_lrn")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_lrn");
        		if(((String)spec_values.get(0)).equals("1")) {
        			answer_for_lrn  = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_course_lrn")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_course_lrn");
        		if(((String)spec_values.get(0)).equals("1")) {
        			answer_for_course_lrn  = true;
        		}
        	}
            if(all_user_ind && spec_pairs.containsKey("answer_for_lrn") && spec_pairs.containsKey("answer_for_course_lrn")) {
//            	Vector vec = new Vector();
            	if((answer_for_lrn && answer_for_course_lrn )||(!answer_for_lrn && !answer_for_course_lrn )) {
//            		vec = ViewLearnerReport.getAllLrn(con, prof.usr_ent_id, prof.root_ent_id);
            		lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
            	} else if (answer_for_lrn && !answer_for_course_lrn) {
//            		vec = ViewLearnerReport.getMyRspLrn(con, prof.usr_ent_id, prof.root_ent_id);
            		lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
            	} else if (!answer_for_lrn && answer_for_course_lrn) {
//            		vec = ViewLearnerReport.getMyRspCosEnrollLrn(con, prof.usr_ent_id, prof.root_ent_id);
            		lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
            	}
/*            	if(vec.size() == 0) {
            		ent_id_lst = new String[1];
            		ent_id_lst[0] = "0";
            	} else {
                    ent_id_lst = new String[vec.size()];
                    for (int i=0; i<vec.size(); i++) {
                    	Long ent_id = (Long)vec.elementAt(i);
                        ent_id_lst[i] = ent_id.longValue() + "";
                    }            		
            	}*/
            }
        }
        if (spec_pairs.containsKey(SPEC_KEY_ITM_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_ID);
           
            itm_id = Long.parseLong((String)spec_values.elementAt(0));
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_MOD_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_MOD_ID);
            
            mod_id = Long.parseLong((String)spec_values.elementAt(0));
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_USR_ENT_ID)||spec_pairs.containsKey(SPEC_KEY_USG_ENT_ID)) {
         if((spec_values=(Vector)spec_pairs.get(SPEC_KEY_USR_ENT_ID))==null){
             spec_values = (Vector)spec_pairs.get(SPEC_KEY_USG_ENT_ID);
          }

            ent_id_lst = new String[spec_values.size()];

            for (int i=0; i<spec_values.size(); i++) {
                ent_id_lst[i] = (String)spec_values.elementAt(i);
            }
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_ATT_CREATE_START_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATT_CREATE_START_DATETIME);

            att_create_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        if (spec_pairs.containsKey(SPEC_KEY_ATT_CREATE_END_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATT_CREATE_END_DATETIME);

            att_create_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_ATTEMPT_START_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATTEMPT_START_DATETIME);

            attempt_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }else{
            attempt_start_datetime = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
        }

        if (spec_pairs.containsKey(SPEC_KEY_ATTEMPT_END_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATTEMPT_END_DATETIME);

            attempt_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }else{
            attempt_end_datetime = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_ATTEMPT_TYPE)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ATTEMPT_TYPE);

            attempt_type = new String[spec_values.size()];

            for (int i=0; i<spec_values.size(); i++) {
                attempt_type[i] = (String)spec_values.elementAt(i);
            }
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_GROUP_BY)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_GROUP_BY);

            group_by = (String)spec_values.elementAt(0);
        }
        content_vec = (Vector)spec_pairs.get(SPEC_KEY_CONTENT_LST);

        String[] reportXML=null;
        reportXML = new String[1];
        reportXML[0] = getReport(con, data.rsp_title, itm_id, mod_id, ent_id_lst, att_create_start_datetime, att_create_end_datetime,
                                 attempt_start_datetime, attempt_end_datetime, content_vec, attempt_type, group_by, download, curLan,
                                 xlsFilePath, encoding, all_user_ind, answer_for_lrn, answer_for_course_lrn, lrn_scope_sql,showSelection);

        return reportXML;
    }

    private String getReport(Connection con, String rsp_title, long itm_id, long mod_id, String[] ent_id_lst, 
                   Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, 
                   Timestamp attempt_start_datetime, Timestamp attempt_end_datetime, Vector content_vec,
                   String[] attempt_type, String group_by, boolean download, String curLan, String xlsFilePath,
                   String encoding, boolean all_user_ind, boolean answer_for_lrn, boolean answer_for_course_lrn, String lrn_scope_sql,boolean showSelection) 
    throws qdbException, SQLException, qdbErrMessage, IOException, cwException {
        String result = "";
        Hashtable data_hash = getAssessmentQueData(con, mod_id, ent_id_lst, att_create_start_datetime, att_create_end_datetime, attempt_start_datetime, attempt_end_datetime, attempt_type, group_by, lrn_scope_sql);
        Hashtable attempt_hash = (Hashtable)data_hash.get("attempt_hash");
        Vector id_vec = (Vector)data_hash.get("id_vec");
        
        if(!download){
            result = getAssessmentQueStatXml(attempt_hash, id_vec, group_by, attempt_type);
        }else{
            AssessmentQueReportXls assrpt = new AssessmentQueReportXls(con, rsp_title, itm_id, mod_id, ent_id_lst, att_create_start_datetime, att_create_end_datetime,
                                                                       attempt_start_datetime, attempt_end_datetime, content_vec,group_by, attempt_type,  curLan, xlsFilePath
                                                                       ,encoding, all_user_ind,answer_for_lrn, answer_for_course_lrn);
            result = assrpt.outputSurveyQueReport(attempt_hash, id_vec,showSelection);
        }
        return result;
    }
    
    private Hashtable getAssessmentQueData(Connection con, long mod_id, String[] ent_id_lst, 
                      Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, 
                      Timestamp attempt_start_datetime, Timestamp attempt_end_datetime, 
                      String[] attempt_type, String group_by, String lrn_scope_sql) throws SQLException, qdbException {
        Vector mod_vec = getChildModID(con, mod_id);
        Hashtable que_hash = getQuestions(con, mod_vec, mod_id);
        Vector parent_child_que = (Vector)que_hash.get("parent_child_vec");
        Vector que_vec = (Vector)que_hash.get("que_vec");
        Hashtable que_attempt_hash = new Hashtable();
        Hashtable attempt_hash = new Hashtable();
        Vector id_vec = new Vector();
        Hashtable data_hash = new Hashtable();
        
        if (que_vec.size() > 0) {

            String usrIdTableName = null;
            String usrIdColName = null;
            Vector usr_ent_ids = null;

            if (ent_id_lst != null && ent_id_lst.length > 0) {
                usrIdColName = "tmp_usr_ent_id";
                Vector empty_vec = new Vector();
                for (int i = 0;i < ent_id_lst.length;i++) {
                    empty_vec.addElement(new Long(ent_id_lst[i]));
                }
                Vector ent_id_vec = new Vector();
                usr_ent_ids = ReportExporter.getUserEntId(con, ent_id_vec, empty_vec);
                usrIdTableName = cwSQL.createSimpleTemptable(con, usrIdColName, cwSQL.COL_TYPE_LONG, 20);
            }

            if (usrIdTableName != null) {
                cwSQL.insertSimpleTempTable(con, usrIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
            }

            if (usrIdTableName == null && lrn_scope_sql != null) {
                usrIdColName = "tmp_usr_ent_id";
                usrIdTableName = cwSQL.createSimpleTemptable(con, usrIdColName, cwSQL.COL_TYPE_LONG, 0, lrn_scope_sql);
            }
            Vector atm_vec = getQueAttemptStat(con, mod_vec, usrIdTableName, usrIdColName, att_create_start_datetime, att_create_end_datetime);
            Vector sc_que_vev = getScQueVec(atm_vec, parent_child_que);
            atm_vec = cwUtils.unionVectors(atm_vec, sc_que_vev, false);
            
            que_attempt_hash = getQueAttemptHash(atm_vec, attempt_start_datetime, attempt_end_datetime, group_by);
            if(group_by.equals(GROUP_BY_RESOURCE_FOLDER) && que_attempt_hash.size() > 0) {
                Hashtable fdr_attempt_hash = getAttemptByQueFdr(que_attempt_hash, que_vec);
                attempt_hash = (Hashtable)fdr_attempt_hash.get("fdr_hash");
                id_vec = (Vector)fdr_attempt_hash.get("fdr_vec");
            }else if(group_by.equals(GROUP_BY_QUESTION_TYPE) && que_attempt_hash.size() > 0) {
                Hashtable qtype_attempt_hash = getAttemptByQueType(que_attempt_hash, que_vec);
                attempt_hash = (Hashtable)qtype_attempt_hash.get("que_type_hash");
                id_vec = (Vector)qtype_attempt_hash.get("que_type_vec");
            }else{
                attempt_hash = que_attempt_hash;
                id_vec = que_vec;
            }
        }        
        data_hash.put("attempt_hash", attempt_hash);
        data_hash.put("id_vec", id_vec);
        
        return data_hash;
    }
    
    private Vector getScQueVec(Vector atm_vec, Vector parent_child_que) {
        Vector sc_que_vec = new Vector();
        
        long[] parent_child_id = new long[2];
        Vector all_tkh_id = new Vector();
        Vector diff_tkh_id = new Vector();
        
        for(int k=0; k<parent_child_que.size(); k++) {
            parent_child_id = (long[])parent_child_que.elementAt(k);
            long parent_id = parent_child_id[0];
            long child_id = parent_child_id[1];
            
            dbProgressAttempt atm = null;
            for(int i=0; i<atm_vec.size(); i++) {
                atm = (dbProgressAttempt)atm_vec.elementAt(i);
                if(atm.atm_int_res_id == child_id){
                    atm.atm_int_res_id = parent_id;
                    if(!diff_tkh_id.contains(new Long(atm.atm_tkh_id))){
                        diff_tkh_id.add(new Long(atm.atm_tkh_id));    
                    }
                    sc_que_vec.add(atm);
                    all_tkh_id.add(new Long(atm.atm_tkh_id));
                }
            }
        }

        sc_que_vec = bubble_Sort(cwUtils.vec2longArray(all_tkh_id), sc_que_vec);
        int sc_que_vec_size = sc_que_vec.size();
        dbProgressAttempt atm1 = null;
        Hashtable que_grp_hash = new Hashtable();
        Vector que_vec = null;
        Vector nbr_id_vec = null;
        if(sc_que_vec_size>0) {
            for(int j=0; j<sc_que_vec_size; j++) {
                atm1 = (dbProgressAttempt)sc_que_vec.elementAt(j);
                String string_tkh_id = "tkh_id_" + atm1.atm_tkh_id;
                if(que_grp_hash.get(new Long(atm1.atm_tkh_id)) == null) {
                    que_vec = new Vector();
                    nbr_id_vec = new Vector();

                    que_grp_hash.put(new Long(atm1.atm_tkh_id), que_vec);
                    que_grp_hash.put(string_tkh_id, nbr_id_vec);
                }else{
                    que_vec = (Vector)que_grp_hash.get(new Long(atm1.atm_tkh_id));
                    nbr_id_vec = (Vector)que_grp_hash.get(string_tkh_id);
                } 
                que_vec.add(atm1);
                nbr_id_vec.add(new Long(atm1.atm_pgr_attempt_nbr));
            }

        }
        
        Vector result_vec = new Vector();
        for(int k=0; k<diff_tkh_id.size(); k++) {
            long tkh_id = ((Long)diff_tkh_id.elementAt(k)).longValue();
            String string_tkh_id = "tkh_id_" + tkh_id;
            que_vec = (Vector)que_grp_hash.get(diff_tkh_id.elementAt(k));
            nbr_id_vec = (Vector)que_grp_hash.get(string_tkh_id);
            long[] nbr_id = cwUtils.vec2longArray(nbr_id_vec);
            
            result_vec = cwUtils.unionVectors(bubble_Sort(nbr_id, que_vec), result_vec, false);
        }
        Vector parent_id_vec = new Vector();
        for(int i=0; i<result_vec.size(); i++) {
            atm1 = (dbProgressAttempt)result_vec.elementAt(i);
            parent_id_vec.add(new Long(atm1.atm_int_res_id));
        }
        result_vec = bubble_Sort(cwUtils.vec2longArray(parent_id_vec), result_vec);
        return result_vec;
    }
    
    
    public static Vector bubble_Sort(long[] id, Vector v){
        dbProgressAttempt temp1;
        long temp2;
        long[] new_array = id;
        Vector new_v = v;

        for(int j=0; j<v.size(); j++ ){
            for(int k=0; k<v.size()-1-j; k++){
                long s = new_array[k]- new_array[k+1];
                if(s>0){
                    temp1 = (dbProgressAttempt)new_v.elementAt(k);
                    new_v.set(k, new_v.elementAt(k+1));
                    new_v.set(k+1, temp1);
                    
                    temp2 =  new_array[k];
                    new_array[k] = new_array[k+1];
                    new_array[k+1] = temp2;
                }
            }
        }  
        return new_v;
    }

    private Hashtable getAttemptByQueFdr(Hashtable que_attempt_hash, Vector que_vec) {
        Vector que = null;
        Vector fdr_vec = new Vector();
        Vector fdr_id_vec = new Vector();
        Hashtable fdr_hash = new Hashtable();
        for(int i=0; i<que_vec.size(); i++){
            que = (Vector)que_vec.elementAt(i);
            if(!fdr_id_vec.contains(((Long)que.elementAt(4)).toString())) {
                fdr_id_vec.add(((Long)que.elementAt(4)).toString());
                fdr_vec.add(que);
            }
        }
        
        fdr_hash.put("fdr_vec", fdr_vec);
        fdr_hash.put("fdr_hash", getAttemptTimes(que_attempt_hash, que_vec, fdr_id_vec, GROUP_BY_RESOURCE_FOLDER));
        return fdr_hash;
    }
    
    private Hashtable getAttemptByQueType(Hashtable que_attempt_hash, Vector que_vec) {
        Vector que = null;
        Vector type_vec = new Vector();
        Vector que_type_vec = new Vector();
        Hashtable type_hash = new Hashtable();
        for(int i=0; i<que_vec.size(); i++){
            que = (Vector)que_vec.elementAt(i);
            if(!type_vec.contains(que.elementAt(2))) {
                type_vec.add(que.elementAt(2));
                que_type_vec.add(que);
            }
        }
        
        type_hash.put("que_type_vec", que_type_vec);
        type_hash.put("que_type_hash", getAttemptTimes(que_attempt_hash, que_vec, type_vec, GROUP_BY_QUESTION_TYPE));
        return type_hash;
    }

    private Hashtable getAttemptTimes(Hashtable que_attempt_hash, Vector que_vec, Vector group_id_vec, String group_by) {
        
        Hashtable temp_attempt_hash = new Hashtable();
        Vector que = null;
        for(int j=0; j< group_id_vec.size(); j++) {
            String  temp_id = (String)group_id_vec.elementAt(j);
            Vector que_attempt_vec = new Vector();
            Vector temp_attempt_vec = new Vector();
            
            for(int i=0; i<que_vec.size(); i++){
                boolean is_sub_que = false;
                que = (Vector)que_vec.elementAt(i);
                if(group_by.equalsIgnoreCase(GROUP_BY_RESOURCE_FOLDER)){
                    if(temp_id.equalsIgnoreCase(((Long)que.elementAt(4)).toString())){
                        is_sub_que = true;
                    }
                }else{
                    if(temp_id.equalsIgnoreCase((String)que.elementAt(2))){
                        is_sub_que = true;
                    }
                }
                if(is_sub_que){
                    que_attempt_vec = (Vector)que_attempt_hash.get(que.elementAt(0));
                    Attempt_times que_times = null;
                    Attempt_times temp_times = null;
                    if(que_attempt_hash.get(que.elementAt(0))!= null){
                        for(int k=0; k<que_attempt_vec.size(); k++) {
                            que_times = (Attempt_times)que_attempt_vec.elementAt(k);
                            if(temp_attempt_vec.size() < k+1 ) {
                                temp_times = new Attempt_times();
                                temp_times.times = que_times.times;
                                temp_times.attempt_cnt = que_times.attempt_cnt;
                                temp_times.correct_cnt = que_times.correct_cnt;
                                temp_times.incorrect_cnt = que_times.incorrect_cnt;
                                temp_times.partial_correct_cnt = que_times.partial_correct_cnt;
                                temp_times.not_graded_cnt = que_times.not_graded_cnt;
                                temp_times.atm_max_score = que_times.atm_max_score*que_times.attempt_cnt;
                                temp_times.usr_total_score = que_times.usr_total_score;
                                temp_times.learner_vec = que_times.learner_vec;
                                temp_times.que_res_id = que_times.que_res_id;
                                temp_times.correct_learner = que_times.correct_learner;
                                temp_attempt_vec.add(temp_times);
                            }else{
                                temp_times = (Attempt_times)temp_attempt_vec.elementAt(k);
                                temp_times.attempt_cnt = temp_times.attempt_cnt + que_times.attempt_cnt;
                                temp_times.correct_cnt = temp_times.correct_cnt + que_times.correct_cnt;
                                temp_times.incorrect_cnt = temp_times.incorrect_cnt + que_times.incorrect_cnt;
                                temp_times.partial_correct_cnt = temp_times.partial_correct_cnt + que_times.partial_correct_cnt;
                                temp_times.not_graded_cnt = temp_times.not_graded_cnt +  que_times.not_graded_cnt;
                                temp_times.atm_max_score = temp_times.atm_max_score + que_times.atm_max_score*que_times.attempt_cnt;
                                temp_times.usr_total_score = temp_times.usr_total_score +  que_times.usr_total_score;
                                temp_times.learner_vec = cwUtils.unionVectors(temp_times.learner_vec, que_times.learner_vec, false);
                                temp_times.correct_learner = dbUtils.vectorIntersect(temp_times.correct_learner, que_times.correct_learner);
                                temp_times.que_res_id = cwUtils.unionVectors(temp_times.que_res_id, que_times.que_res_id, false);
                            }
                        }
                    }
                }
            }
            temp_attempt_hash.put(group_id_vec.elementAt(j), temp_attempt_vec);
        }
        return temp_attempt_hash;
    }

    private String getAssessmentQueStatXml(Hashtable que_attempt_hash, Vector que_vec, String group_by, String[] attempt_type) {
        StringBuffer body = new StringBuffer();
        
        if(que_attempt_hash.size()>0) {
            Vector total_rec_Vec = new Vector();
            body.append("<report_list>");
            body.append("<atm_stat_list group_by=\"").append(group_by).append("\">").append(cwUtils.NEWL);
            body.append(asXML(que_vec, que_attempt_hash, group_by, attempt_type, total_rec_Vec));
            body.append("</atm_stat_list>").append(cwUtils.NEWL);
            if(!group_by.equalsIgnoreCase(GROUP_BY_QUESTION_TYPE)) {
                cwPagination page = new cwPagination();
                page.pageSize = 20;
                page.curPage = 1;
                page.totalPage = 1;
                page.totalRec = ((Integer)total_rec_Vec.get(0)).intValue();
                body.append(page.asXML());
            }
            body.append("</report_list>");
        }
        return body.toString();
    }

    private StringBuffer asXML(Vector que_vec, Hashtable que_attempt_hash, String group_by, String[] attempt_type, Vector total_rec_Vec) {
        StringBuffer xml = new StringBuffer();
        Vector que = null;
        Vector que_attempt_vec = null;
        Vector attempt_type_vec = cwUtils.String2vector(attempt_type);
        int total_rec = 0;
        for(int i=0; i<que_vec.size(); i++) {
            que = (Vector)que_vec.get(i);
            if((group_by.equalsIgnoreCase(GROUP_BY_QUESTION) && que_attempt_hash.get(que.elementAt(0)) != null) || !group_by.equalsIgnoreCase(GROUP_BY_QUESTION)) {
                total_rec ++;
                if(i < 20) {
                    xml.append("<atm_stat ");
                    if(group_by.equalsIgnoreCase(GROUP_BY_QUESTION)){
                        que_attempt_vec = (Vector)que_attempt_hash.get(que.elementAt(0));
                        xml.append("res_id=\"").append(que.elementAt(0)).append("\" res_title=\"").append(cwUtils.esc4XML((String)que.elementAt(1)));
                        xml.append("\" res_type=\"").append(que.elementAt(2)).append("\" res_diff=\"").append(que.elementAt(3));
                        xml.append("\" res_fdr_id=\"").append(que.elementAt(4)).append("\" res_fdr_title=\"").append(cwUtils.esc4XML((String)que.elementAt(5))); 
                        int que_score = que.size() >= 7 ? (Integer)que.elementAt(6) : 0;
                        xml.append("\" que_score=\"").append(que_score);
                    }else if(group_by.equalsIgnoreCase(GROUP_BY_QUESTION_TYPE)){
                        que_attempt_vec = (Vector)que_attempt_hash.get(que.elementAt(2));
                        xml.append("res_type=\"").append(que.elementAt(2));
                    }else{
                        que_attempt_vec = (Vector)que_attempt_hash.get(((Long)que.elementAt(4)).toString());
                        xml.append("res_fdr_id=\"").append(que.elementAt(4)).append("\" res_fdr_title=\"").append(cwUtils.esc4XML((String)que.elementAt(5)));    
                    }
                    xml.append("\">").append(cwUtils.NEWL);
                    Attempt_times times = null;
                    Attempt_times all = null;
        
                    long attempt_cnt = 0;
                    long learners = 0;
                    long total_score = 0;
                    long usr_total_score = 0;
                    long perfect_attempts = 0;
        
                    long cor_percent = 0;
                    long incor_percent = 0;
                    long par_cor_percent = 0;
                    long not_graded_percent = 0;
                    double avg_score_precent = 0;
                    
                    long all_total_score = 0;
                    long all_usr_total_score = 0;
                    long all_perfect_attempts = 0;
                    if(attempt_type_vec.contains(ALL_ATTEMPTS_TYPE)){
                        all = new Attempt_times();
                        all.times = 0;
                    }
                    for(int j=0; j<que_attempt_vec.size(); j++) {
                        times = (Attempt_times)que_attempt_vec.elementAt(j);
                        attempt_cnt = times.attempt_cnt;
                        learners = times.learner_vec.size();
                        perfect_attempts = times.correct_learner.size();
                        usr_total_score = times.usr_total_score;
                        if(group_by.equalsIgnoreCase(GROUP_BY_QUESTION)) {
                            total_score = times.atm_max_score * attempt_cnt;
                        }else{
                            total_score = times.atm_max_score;
                        }
                        
                        cor_percent = Math.round(new Float(times.correct_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                        incor_percent = Math.round(new Float(times.incorrect_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                        par_cor_percent = Math.round(new Float(times.partial_correct_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                        not_graded_percent = Math.round(new Float(times.not_graded_cnt).floatValue()/new Float(attempt_cnt).floatValue()*100);
                        avg_score_precent = FormatUtil.getInstance().scaleDouble(new Double(usr_total_score).doubleValue()/new Double(total_score).doubleValue(), 2);
                        
                        if(attempt_type_vec.contains(ALL_ATTEMPTS_TYPE)){
                            all.attempt_cnt = all.attempt_cnt + attempt_cnt;
                            all.correct_cnt = all.correct_cnt + times.correct_cnt;
                            all.incorrect_cnt = all.incorrect_cnt + times.incorrect_cnt;
                            all.partial_correct_cnt = all.partial_correct_cnt +  times.partial_correct_cnt;
                            all.not_graded_cnt = all.not_graded_cnt + times.not_graded_cnt;
                            all.learner_vec = cwUtils.unionVectors(all.learner_vec, times.learner_vec, false);
                            all_total_score = all_total_score + total_score;
                            all_usr_total_score = all_usr_total_score + usr_total_score;
                            all.que_res_id = cwUtils.unionVectors(all.que_res_id, times.que_res_id, false);
                            all_perfect_attempts = all_perfect_attempts + perfect_attempts;
                        }
                        if(attempt_type_vec.contains(NUMBERED_ATTEMPTS_TYPE)){
                            xml.append("<attempt id=\"").append(times.times).append("\" attempt_cnt=\"").append(attempt_cnt);
                            xml.append("\" correct=\"").append(times.correct_cnt).append("\" cor_percent=\"").append(cor_percent);
                            xml.append("\" incorrect=\"").append(times.incorrect_cnt).append("\" incor_percent=\"").append(incor_percent);
                            xml.append("\" partial_correct=\"").append(times.partial_correct_cnt).append("\" par_cor_percent=\"").append(par_cor_percent);
                            xml.append("\" not_graded=\"").append(times.not_graded_cnt).append("\" not_graded_percent=\"").append(not_graded_percent);
                            xml.append("\" avg_score_precent=\"").append(avg_score_precent).append("\" learners=\"").append(learners);
                            xml.append("\" perfect_attempts=\"").append(perfect_attempts).append("\" questions=\"").append(times.que_res_id.size());
                            xml.append("\"/>").append(cwUtils.NEWL);
                        }
                    }
                    if(attempt_type_vec.contains(ALL_ATTEMPTS_TYPE)){
                        long all_cor_percent = Math.round(new Float(all.correct_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                        long all_incor_percent = Math.round(new Float(all.incorrect_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                        long all_par_cor_percent = Math.round(new Float(all.partial_correct_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                        long all_not_graded_percent = Math.round(new Float(all.not_graded_cnt).floatValue()/new Float(all.attempt_cnt).floatValue()*100);
                        double all_avg_score_precent = FormatUtil.getInstance().scaleDouble(new Double(all_usr_total_score).doubleValue()/new Double(all_total_score).doubleValue(),2);
                            
                        xml.append("<attempt id=\"").append(all.times).append("\" attempt_cnt=\"").append(all.attempt_cnt);
                        xml.append("\" correct=\"").append(all.correct_cnt).append("\" cor_percent=\"").append(all_cor_percent);
                        xml.append("\" incorrect=\"").append(all.incorrect_cnt).append("\" incor_percent=\"").append(all_incor_percent);
                        xml.append("\" partial_correct=\"").append(all.partial_correct_cnt).append("\" par_cor_percent=\"").append(all_par_cor_percent);
                        xml.append("\" not_graded=\"").append(all.not_graded_cnt).append("\" not_graded_percent=\"").append(all_not_graded_percent);
                        xml.append("\" avg_score_precent=\"").append(all_avg_score_precent).append("\" learners=\"").append(all.learner_vec.size());
                        xml.append("\" perfect_attempts=\"").append(all_perfect_attempts).append("\" questions=\"").append(all.que_res_id.size());
                        xml.append("\"/>").append(cwUtils.NEWL);
                    }
                    xml.append("</atm_stat>").append(cwUtils.NEWL);
                }
            }
        }
        total_rec_Vec.add(new Integer(total_rec));
        return xml;
    }

    private Hashtable getQueAttemptHash(Vector atm_vec, Timestamp attempt_start_datetime, Timestamp attempt_end_datetime, String group_by){
            Hashtable que_attempt_hash = new Hashtable();
            dbProgressAttempt atm = null;
            long que_id = 0;
            long tkh_id = 0;
            int times = 0;
            long attempt_nbr = 0;
            Timestamp pgr_complete_datetime = null;
            Attempt_times attempt_times = null;
            Vector attempt_vec = null;
            if (atm_vec.size() > 0) {
                for (int i = 0; i < atm_vec.size(); i++) {
                    
                    atm = (dbProgressAttempt) atm_vec.elementAt(i);

                    if (i > 0 && (que_id != atm.atm_int_res_id || tkh_id != atm.atm_tkh_id || attempt_nbr != atm.atm_pgr_attempt_nbr)) {
                        setQueAttemptStat1(attempt_times, pgr_complete_datetime, attempt_start_datetime, attempt_end_datetime, tkh_id);
                    }

                    if (!que_attempt_hash.containsKey(new Long(atm.atm_int_res_id))) {

                        attempt_times = new Attempt_times();
                        attempt_vec = new Vector();
                        times = 1;
                        attempt_times.times = times;
                        if (atm.pgr_complete_datetime.after(attempt_start_datetime) && atm.pgr_complete_datetime.before(attempt_end_datetime)) {
                            attempt_times.attempt_cnt = 1;
                            attempt_times.que_res_id.add(new Long(atm.atm_int_res_id));
                        }
                        setQueAttemptStat2(atm, attempt_times, attempt_start_datetime, attempt_end_datetime, false);
                        
                        attempt_vec.add(attempt_times);
                        que_attempt_hash.put(new Long(atm.atm_int_res_id), attempt_vec);
                    } else if (tkh_id == atm.atm_tkh_id ) {
                        if (attempt_nbr == atm.atm_pgr_attempt_nbr) {
                            if (atm.pgr_complete_datetime.after(attempt_start_datetime) && atm.pgr_complete_datetime.before(attempt_end_datetime)) {
                                attempt_times.usr_score_sum = attempt_times.usr_score_sum + atm.atm_score;
                                attempt_times.atm_max_score = attempt_times.atm_max_score+ atm.atm_max_score;
                                attempt_times.usr_total_score = attempt_times.usr_total_score + atm.atm_score;
                            }
                        } else {
                            times = times + 1;
                            Vector tmp_attempt_vec = (Vector) que_attempt_hash.get(new Long(atm.atm_int_res_id));
                            if (tmp_attempt_vec.size() < times) {
                                attempt_times = new Attempt_times();
                                attempt_times.times = times;
                                if (atm.pgr_complete_datetime.after(attempt_start_datetime) && atm.pgr_complete_datetime.before(attempt_end_datetime)) {
                                    attempt_times.attempt_cnt = 1;
                                    attempt_times.que_res_id.add(new Long(atm.atm_int_res_id));
                                }
                                setQueAttemptStat2(atm, attempt_times, attempt_start_datetime, attempt_end_datetime, false);

                                attempt_vec.add(attempt_times);
                                que_attempt_hash.put(new Long(atm.atm_int_res_id), attempt_vec);
                            } else {
                                attempt_times = (Attempt_times) tmp_attempt_vec.elementAt(times - 1);
                                setQueAttemptStat2(atm, attempt_times, attempt_start_datetime, attempt_end_datetime, true);
                            }
                        }
                    } else if (tkh_id != atm.atm_tkh_id) {
                        times = 1;
                        Vector tmp_attempt_vec = (Vector) que_attempt_hash.get(new Long(atm.atm_int_res_id));
                        attempt_times = (Attempt_times) tmp_attempt_vec.elementAt(times - 1);
                        setQueAttemptStat2(atm, attempt_times, attempt_start_datetime, attempt_end_datetime, true);
                    }

                    if (i == atm_vec.size() - 1) {
                        setQueAttemptStat1(attempt_times, atm.pgr_complete_datetime, attempt_start_datetime, attempt_end_datetime, atm.atm_tkh_id);
                    }
                    que_id = atm.atm_int_res_id;
                    tkh_id = atm.atm_tkh_id;
                    attempt_nbr = atm.atm_pgr_attempt_nbr;
					pgr_complete_datetime = atm.pgr_complete_datetime;
                }
           }
           
           return que_attempt_hash;
    }
    
    private static void setQueAttemptStat1(Attempt_times attempt_times, Timestamp pgr_complete_datetime, Timestamp attempt_start_datetime, Timestamp attempt_end_datetime, long tkh_id) {
        if (pgr_complete_datetime.after(attempt_start_datetime) && pgr_complete_datetime.before(attempt_end_datetime)) {

            if (attempt_times.usr_score_sum == attempt_times.atm_max_score) {
                attempt_times.correct_cnt = attempt_times.correct_cnt + 1;
                attempt_times.correct_learner.add((new Long(tkh_id)));
            } else if (attempt_times.not_graded) {
                attempt_times.not_graded_cnt = attempt_times.not_graded_cnt + 1;
            } else if (attempt_times.usr_score_sum == 0) {
                attempt_times.incorrect_cnt = attempt_times.incorrect_cnt + 1;
            } else if (attempt_times.usr_score_sum < attempt_times.atm_max_score) {
                attempt_times.partial_correct_cnt = attempt_times.partial_correct_cnt + 1;
            }
        }
    }
    
    private static void setQueAttemptStat2(dbProgressAttempt atm, Attempt_times attempt_times, Timestamp attempt_start_datetime, Timestamp attempt_end_datetime, boolean add_attempt_cnt){
        
        if (atm.pgr_complete_datetime.after(attempt_start_datetime) && atm.pgr_complete_datetime.before(attempt_end_datetime)) {
            if (atm.atm_score == -1) {
                attempt_times.not_graded = true;
            } else {
                attempt_times.not_graded = false;
                attempt_times.usr_total_score = attempt_times.usr_total_score + atm.atm_score;
            }
            attempt_times.atm_max_score = atm.atm_max_score;
            attempt_times.usr_score_sum = atm.atm_score;
            if (!attempt_times.learner_vec.contains(atm.atm_pgr_usr_id)) {
                attempt_times.learner_vec.add(atm.atm_pgr_usr_id);
            }
            
            if(add_attempt_cnt){
                attempt_times.attempt_cnt = attempt_times.attempt_cnt + 1;
            }
        }
    }
    
    private Vector getQueAttemptStat(Connection con, Vector mod_vec, String usrIdTableName, String usrIdColName, 
                              Timestamp att_create_start_datetime, Timestamp att_create_end_datetime) 
        throws SQLException {
        
        Vector atm_vec = new Vector();
        StringBuffer attempt_sql = new StringBuffer(1024);
        attempt_sql.append(" select atm_tkh_id, atm_pgr_usr_id , atm_pgr_attempt_nbr, res_res_id_root, atm_int_res_id, atm_correct_ind, atm_score, atm_max_score,  pgr_complete_datetime from ProgressAttempt ,Progress, Resources ");
        if(att_create_start_datetime != null || att_create_end_datetime != null) {
            attempt_sql.append(" ,aeAttendance, aeApplication ");
        }
        attempt_sql.append(" where atm_pgr_res_id = pgr_res_id and atm_pgr_res_id in ").append( cwUtils.vector2list(mod_vec))
        .append("  and atm_pgr_usr_id = pgr_usr_id and atm_tkh_id = pgr_tkh_id  and atm_pgr_attempt_nbr = pgr_attempt_nbr ")
        .append("  and atm_int_res_id = res_id ");
        if (usrIdTableName != null && usrIdColName != null) {
            attempt_sql.append(" and atm_pgr_usr_id in ( ").append("select usr_id from ").append(usrIdTableName).append(",RegUser where ").append(usrIdColName).append("=usr_ent_id").append(")");
        }
        if(att_create_start_datetime != null || att_create_end_datetime != null) {
            attempt_sql.append(" and atm_tkh_id = app_tkh_id and app_id = att_app_id ");
            if(att_create_start_datetime != null){
                attempt_sql.append(" and att_create_timestamp >= ?");
            }
            if(att_create_end_datetime != null){
                attempt_sql.append(" and att_create_timestamp <= ?");
            }
        }
        attempt_sql.append(" order by  atm_int_res_id, pgr_tkh_id, pgr_complete_datetime");
        PreparedStatement stmt = con.prepareStatement(attempt_sql.toString());
                                  
        int index = 1;
        if (att_create_start_datetime != null) {
            stmt.setTimestamp(index++, att_create_start_datetime);
        }
        if (att_create_end_datetime != null) {
            stmt.setTimestamp(index++, att_create_end_datetime);
        }                      

        ResultSet rs = stmt.executeQuery();
        dbProgressAttempt atm = null; 
        while (rs.next()){
            atm = new dbProgressAttempt();
            atm.atm_int_res_id = rs.getLong("res_res_id_root");
            atm.atm_tkh_id = rs.getLong("atm_tkh_id");
            atm.atm_pgr_usr_id = rs.getString("atm_pgr_usr_id");
            atm.atm_pgr_attempt_nbr = rs.getLong("atm_pgr_attempt_nbr");
            atm.atm_correct_ind = rs.getBoolean("atm_correct_ind");
            atm.atm_score = rs.getLong("atm_score");
            atm.atm_max_score = rs.getLong("atm_max_score");
            atm.pgr_complete_datetime = rs.getTimestamp("pgr_complete_datetime");
            
            atm_vec.add(atm);
        }
        stmt.close();
        if (usrIdTableName != null) {
            cwSQL.dropTempTable(con, usrIdTableName);
        }
    return atm_vec;
   }
    
    
    
    private Hashtable getQuestions(Connection con, Vector mod_vec, long root_mod_id) throws SQLException {
        Hashtable que_hash = new Hashtable();
        Vector que = null;
        
        long que_id = 0;
        long que_fdr_id = 0;
        String que_fdr_title = "";
        String que_title = "";
        String que_type = "";
        String que_xml = "";
        
        int que_score = 0;
        
        long que_difficulty = 0;
        
        long child_que_id = 0;
        long[] parent_child_id = null;
        Vector parent_child_vec = new Vector();
        Vector que_vec = new Vector();
         
        StringBuffer que_sql = new StringBuffer(1024);
        

        que_sql.append("select obj_id, obj_desc, c_root.res_id as c_root_res_id , c_root.res_title as c_root_res_title, que_score, que_xml, p_root.res_title as p_root_res_title, ")
        .append(" p_root.res_subtype as p_root_res_type , c_root.res_subtype as c_root_res_type , ")
        .append(" p_root.res_difficulty as p_root_res_difficulty, c_root.res_difficulty as c_root_res_difficulty, ");
        que_sql.append(cwSQL.replaceNull("p_root.res_id", Long.toString(root_mod_id))).append(" as p_root_res_id ")
        .append(" from ProgressAttempt inner join ResourceObjective on (atm_int_res_id = rob_res_id) ")
        .append(" inner join Objective on (rob_obj_id = obj_id) ")
        .append(" inner join Resources child on (atm_int_res_id = child.res_id) ")
        .append(" left join ResourceContent on (atm_int_res_id = rcn_res_id_content) ")
        .append(" left join Resources parent on (rcn_res_id = parent.res_id) ")
        .append(" left join Resources p_root on (p_root.res_id = parent.res_res_id_root) ") 
        .append(" left join Resources c_root on (c_root.res_id = child.res_res_id_root) ")
        .append(" left join Question p_que on (p_que.que_res_id = child.res_id) ") 
        .append(" where atm_pgr_res_id in").append( cwUtils.vector2list(mod_vec))       
        .append(" order by c_root_res_id ");
        
        PreparedStatement stmt = con.prepareStatement(que_sql.toString());
//        stmt.setLong(1, root_mod_id);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            que = new Vector();
            
            que_fdr_id = rs.getLong("obj_id");
            que_fdr_title = rs.getString("obj_desc");
            
            if(rs.getString("p_root_res_type")==null ||
              (!rs.getString("p_root_res_type").equalsIgnoreCase(dbResource.RES_SUBTYPE_FSC)
               && !rs.getString("p_root_res_type").equalsIgnoreCase(dbResource.RES_SUBTYPE_DSC))) {
                que_id = rs.getLong("c_root_res_id");
                que_title = rs.getString("c_root_res_title");
                que_type = rs.getString("c_root_res_type");
                que_difficulty = rs.getLong("c_root_res_difficulty");
                que_score = rs.getInt("que_score");
                que_xml = cwSQL.getClobValue(rs, "que_xml");
 
            }else{
                que_id = rs.getLong("p_root_res_id");
                que_title = rs.getString("p_root_res_title");
                que_type = rs.getString("p_root_res_type");
                que_difficulty = rs.getLong("p_root_res_difficulty");
                
                child_que_id = rs.getLong("c_root_res_id");
                parent_child_id = new long[2];
                parent_child_id[0] = que_id;
                parent_child_id[1] = child_que_id;
                
                parent_child_vec.add(parent_child_id);
            }
            
            que.add(new Long(que_id));
            que.add(que_title);
            que.add(que_type);
            que.add(new Long(que_difficulty));
            que.add(new Long(que_fdr_id));
            que.add(que_fdr_title);
            que.add(que_score);
            //que.add(CharUtils.filterHTML(que_xml));
            String que_test_xml = getQueTestInfo(que_xml,que_type);
            que.add(CharUtils.filterHTML(que_test_xml));
            
            que_vec.add(que); 
        }
        stmt.close();
        
        Vector temp_que_vec = new Vector();   
        if(que_vec.size()>0){
            Vector que_vec_id = new Vector();
            for(int i=0; i<que_vec.size(); i++){
                if(!que_vec_id.contains(((Vector)que_vec.elementAt(i)).elementAt(0))){
                    que_vec_id.add(((Vector)que_vec.elementAt(i)).elementAt(0));
                    temp_que_vec.add(que_vec.elementAt(i));
                }
            }
        }
            
        que_hash.put("que_vec", temp_que_vec);
        que_hash.put("parent_child_vec" , parent_child_vec);
            
        return que_hash;
    }
    
    /**
     * 获取题目信息
     * @param queXml
     * @param queType
     * @return
     */
    public String getQueTestInfo(String queXml,String queType) {
		try {
			Document doc = DocumentHelper.parseText(queXml);
			if(!queType.equals(dbQuestion.QUE_TYPE_FILLBLANK)){
				Element body = doc.getRootElement();
		        Element html = body.element("html");
		        queXml = html.getText();	
			}else{
	            Element body = doc.getRootElement();
	            StringBuilder subjectBuilder = new StringBuilder();
	            for(Object node : body.elements()){
	                if(node instanceof Element){
	                    Element element = (Element)node;
	                    if("html".equals(element.getQName().getName())){
	                        subjectBuilder.append(element.getText());
	                    }else if("interaction".equals(element.getQName().getName())){
	                        subjectBuilder.append("[_]");
	                    }
	                }
	            }
	            queXml = subjectBuilder.toString();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    	return queXml;
    }
    
    public Vector  getChildModID(Connection con, long parent_mod_id) throws SQLException {
        Vector mod_vec = new Vector();
        String sql = "select mod_res_id from Module where mod_mod_res_id_parent = ? ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1, parent_mod_id);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            mod_vec.add(new Long(rs.getLong("mod_res_id")));
        }
        if(mod_vec.size() == 0){
            mod_vec.add(new Long(parent_mod_id));
        }
        pstmt.close();
        return mod_vec;

    }
}

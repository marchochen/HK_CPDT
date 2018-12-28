package com.cw.wizbank.report;

import java.sql.*;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.accesscontrol.AcReportTemplate;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbAcRole;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.security.AclFunction;

public class Report {
    static final boolean DEBUG = true;

    static final String LEARNER = "LEARNER";
    static final String COURSE = "COURSE";
	static final String LEARNING_ACTIVITY_COS = "LEARNING_ACTIVITY_COS";
	static final String LEARNING_ACTIVITY_BY_COS = "LEARNING_ACTIVITY_BY_COS";
	static final String LEARNING_ACTIVITY_LRN = "LEARNING_ACTIVITY_LRN";
    static final String TARGET_LEARNER = "TARGET_LEARNER";
    static final String SURVEY_PREFIX = "SURVEY_";
    static final String SURVEY_COS_PREFIX = "SURVEY_COS_";
    static final String MODULE_PREFIX = "MODULE_";
    static final String MODULE_IND = "MODULE_IND";
    static final String MODULE_CMP = "MODULE_CMP";
    static final String MODULE_QUE = "MODULE_QUE";
    static final String MODULE = "LEARNING_MODULE";
    static final String KCRC = "KCRC";
    static final String SELF = "SELF";
    static final String SELF_CPT = "SELF_CPT";
    static final String GROUP = "GROUP";
    static final String GROUP_CPT = "GROUP_CPT";
    static final String NULL_VALUE = "_NULL_";
    static final String GLOBAL_ENROLLMENT = "GLOBAL_ENROLLMENT";
    static final String ASSESSMENT_QUE_GRP = "ASSESSMENT_QUE_GRP";
    static final String MODULE_EVN_OF_COS = "MOD_EVN_OF_COS";
    static final String EXAM_PAPER_STAT = "EXAM_PAPER_STAT";
    static final String TRAIN_FEE_STAT = "TRAIN_FEE_STAT";
    static final String TRAIN_COST_STAT = "TRAIN_COST_STAT";
    static final String FM_FEE = "FM_FEE";
    static final String CREDIT = "CREDIT";
    static final String EXP_TARGET_LRN= "EXP_TARGET_LRN";
    static final String EXP_AEITEM_LESSON = "EXP_AEITEM_LESSON";
    static final String QUE_STATISTIC = "EXP_QUE_STATISTIC";
    
    public static final String COLNAME_TMP_USR_ENT_ID = "tmp_usr_ent_id";

    public static String getReportList(Connection con, loginProfile prof, String[] report_type, boolean show_public) throws SQLException {
//        System.out.println("report_type: " + report_type);
//        if (report_type!=null){
//                System.out.println("report_type size: " + report_type.size());
//            if (report_type.size()>0){
//                System.out.println("report_type : " + report_type[0]);
//            }                
//        }

        StringBuffer result = new StringBuffer();
        ViewReportSpec spec = new ViewReportSpec();
        //store the input report template type, if any
        Vector report_type_vec = null;
        //store the assessible report template from accesscontrol
        Vector acc_report_type_vec = null;
        
        //access control to restrict report_type can access
        if (report_type != null) {
            report_type_vec = new Vector();
            for (int i=0; i<report_type.length; i++) {
                 report_type_vec.addElement(report_type[i].toUpperCase());
            }
        }
        AcReportTemplate acRte = new AcReportTemplate(con);
        if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_TRAINING_REPORT_MGT)){
        	acc_report_type_vec =  acRte.getAccessibleReportTemplateType(prof.usr_ent_id, AcRole.ROLE_TADM_1);
        }else{
            acc_report_type_vec = 
                    acRte.getAccessibleReportTemplateType(prof.usr_ent_id, prof.current_role);
        }
        
        if(report_type_vec == null) {
            report_type_vec = acc_report_type_vec;
        } else {
            Vector vTemp = new Vector();
            for(int i=0; i<report_type_vec.size(); i++) {
                if(acc_report_type_vec.contains(report_type_vec.elementAt(i))) {
                    vTemp.addElement(report_type_vec.elementAt(i));
                }
            }
            report_type_vec = vTemp;
        }
        //Access control end
        Vector template_lst = spec.getReportTemplateList(con, prof.root_ent_id, null);
        StringBuffer template_id_lst = new StringBuffer();
        StringBuffer all_template_xml = new StringBuffer();
        template_id_lst.append("(0");
        
        for (int i=0; i<template_lst.size(); i++) {
            ViewReportSpec.Data data = (ViewReportSpec.Data)template_lst.elementAt(i);
            all_template_xml.append("<template type=\"").append(data.rte_type).append("\">");
            all_template_xml.append(aeUtils.escNull(data.rte_title_xml)).append("</template>");

            if (report_type_vec != null && report_type_vec.contains(data.rte_type.toUpperCase())) { 
                template_id_lst.append(", ").append(data.rte_id);
            }
        }
        
        template_id_lst.append(")");        

        Vector spec_lst = null;
        if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_TRAINING_REPORT_MGT)){
        		spec_lst = spec.getReportSpecList(con, prof.usr_ent_id, template_id_lst.toString(), show_public);
        }else{
        	spec_lst = spec.getReportSpecList(con, prof.usr_ent_id, template_id_lst.toString(), show_public);
        }
        
        result.append("<meta>");
        result.append("<template_list>").append(all_template_xml.toString()).append("</template_list>");
        result.append("</meta>");
        
        result.append("<report_detail>").append(cwUtils.NEWL);
        result.append("<template_list>").append(cwUtils.NEWL);
        
        for (int i=0; i<template_lst.size(); i++) {
            ViewReportSpec.Data data = (ViewReportSpec.Data)template_lst.elementAt(i);

            if (report_type_vec != null && report_type_vec.contains(data.rte_type.toUpperCase())) { 
                result.append("<template id=\"").append(data.rte_id).append("\" type=\"").append(data.rte_type).append("\">").append(cwUtils.NEWL);               
                result.append(aeUtils.escNull(data.rte_title_xml)).append(cwUtils.NEWL);
                result.append("<xsl_list>").append(cwUtils.NEWL);
                result.append("<xsl type=\"get\">").append(cwUtils.esc4XML(aeUtils.escNull(data.rte_get_xsl))).append("</xsl>").append(cwUtils.NEWL);
                result.append("<xsl type=\"execute\">").append(cwUtils.esc4XML(aeUtils.escNull(data.rte_exe_xsl))).append("</xsl>").append(cwUtils.NEWL);
                result.append("<xsl type=\"download\">").append(cwUtils.esc4XML(aeUtils.escNull(data.rte_dl_xsl))).append("</xsl>").append(cwUtils.NEWL);
                result.append("</xsl_list>").append(cwUtils.NEWL);
                result.append("</template>").append(cwUtils.NEWL);
            }
        }

        result.append("</template_list>").append(cwUtils.NEWL);
        result.append("<spec_list>").append(cwUtils.NEWL);
        
        for (int i=0; i<spec_lst.size(); i++) {
            ViewReportSpec.Data data = (ViewReportSpec.Data)spec_lst.elementAt(i);
            result.append("<spec template_id=\"").append(data.rsp_rte_id).append("\" spec_id=\"").append(data.rsp_id).append("\" ent_id=\"").append(data.rsp_ent_id).append("\">").append(cwUtils.NEWL);
            result.append("<title>").append(cwUtils.esc4XML(aeUtils.escNull(data.rsp_title))).append("</title>").append(cwUtils.NEWL);
            result.append(data.rsp_xml).append(cwUtils.NEWL);
            result.append("</spec>").append(cwUtils.NEWL);
        }
        
        result.append("</spec_list>").append(cwUtils.NEWL);
        result.append("</report_detail>").append(cwUtils.NEWL);
        result.append("<enableCPD>").append(AccessControlWZB.hasCPDFunction()).append("</enableCPD>");
        return result.toString();
    }     
    
    public static void insUpdReportSpec(Connection con, loginProfile prof, long usr_ent_id, long rte_id, long rsp_id, String rsp_title, String[] spec_name, String[] spec_value) throws SQLException, cwException {
        if ((spec_name == null && spec_value == null) ||
            (spec_name != null && spec_value != null && spec_name.length == spec_value.length)) {
            StringBuffer spec_xml = new StringBuffer();
            
            spec_xml.append("<data_list>");
            
            if (spec_name != null) {
                for (int i=0; i<spec_name.length; i++) {
                    if (spec_name[i] != NULL_VALUE && spec_value[i] != NULL_VALUE) {
                        String[] values = cwUtils.splitToString(spec_value[i], "~");

                        for (int j=0; j<values.length; j++) {
                            spec_xml.append("<data name=\"").append(cwUtils.esc4XML(spec_name[i])).append("\" value=\"").append(cwUtils.esc4XML(values[j])).append("\"/>");
                        }
                    }
                }
            }

            spec_xml.append("</data_list>");

            DbReportSpec spec = new DbReportSpec();
            
            spec.rsp_id = rsp_id;
            spec.rsp_rte_id = rte_id;

            if (usr_ent_id == 0) {
                spec.rsp_ent_id = prof.usr_ent_id;
            } else {
                spec.rsp_ent_id = usr_ent_id;
            }
            
            spec.rsp_title = rsp_title;
            spec.rsp_xml = spec_xml.toString();
            spec.rsp_create_usr_id = prof.usr_id;
            spec.rsp_upd_usr_id = prof.usr_id;

            if (rsp_id == 0) {
                spec.ins(con);
            } else {
                spec.upd(con);
            }
        } else {
            throw new cwException("com.cw.wizbank.report.insReportSpec: # of spec_name and # of spec_value doesn't match");
        }        
    }

    public static DbReportSpec toSpec(long rte_id, String rsp_title, String[] spec_name, String[] spec_value) {
        DbReportSpec spec = null;

        if ((spec_name == null && spec_value == null) ||
            (spec_name != null && spec_value != null && spec_name.length == spec_value.length)) {
            StringBuffer spec_xml = new StringBuffer();
            spec = new DbReportSpec();
            
            spec_xml.append("<data_list>");
            
            if (spec_name != null) {
                for (int i=0; i<spec_name.length; i++) {
                    if (spec_name[i] != NULL_VALUE && spec_value[i] != NULL_VALUE) {
                        String[] values = cwUtils.splitToString(spec_value[i], "~");

                        for (int j=0; j<values.length; j++) {
                            spec_xml.append("<data name=\"").append(cwUtils.esc4XML(spec_name[i])).append("\" value=\"").append(cwUtils.esc4XML(values[j])).append("\"/>");
                        }
                    }
                }
            }

            spec_xml.append("</data_list>");
            
            spec.rsp_rte_id = rte_id;
            spec.rsp_title = rsp_title;
            spec.rsp_xml = spec_xml.toString();            
        }
        return spec;
    }
    
    public static void delReportSpec(Connection con, loginProfile prof, long rsp_id) throws SQLException, cwException {
        DbReportSpec spec = new DbReportSpec();
            
        spec.rsp_id = rsp_id;
        spec.del(con);        
    }

    private static final String get_list_for_home_sql = 
    "select rte_id, rte_type, rte_get_xsl "
    + "from acReportTemplate "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") + ", ReportTemplate "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"")
    + "where (ac_rte_ent_id = ? or ac_rte_ent_id is null) "
    + "and ac_rte_rol_ext_id = ? "
    + "and ac_rte_ftn_ext_id = ? "
    + "and ac_rte_id = rte_id "
    + "and rte_owner_ent_id = ? "
    + "order by rte_seq_no ";
    public static StringBuffer getListForHome(Connection con, long root_ent_id, long usr_ent_id, String usr_cur_role) throws SQLException {
        PreparedStatement stmt = null;
        StringBuffer result = new StringBuffer();
        result.append("<rte_list>");
        int idx = 1;
        int rte_id = 0;
        String rte_type = null;
        String rte_get_xsl = null;
        try {
            stmt = con.prepareStatement(get_list_for_home_sql);
            stmt.setLong(idx++, usr_ent_id);
            stmt.setString(idx++, usr_cur_role);
            stmt.setString(idx++, AcReportTemplate.FTN_RTE_READ);
            stmt.setLong(idx++, root_ent_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                idx = 1;
                rte_id = rs.getInt(idx++);
                rte_type = rs.getString(idx++);
                rte_get_xsl = rs.getString(idx++);
                if (rs.wasNull()) {
                    rte_get_xsl = "";
                }
                result.append("<rte id=\"")
                      .append(rte_id)
                      .append("\" type=\"")
                      .append(rte_type)
                      .append("\" xsl=\"")
                      .append(cwUtils.esc4XML(rte_get_xsl))
                      .append("\"/>")
                      ;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        result.append("</rte_list>");
        
        return result;
    }
}

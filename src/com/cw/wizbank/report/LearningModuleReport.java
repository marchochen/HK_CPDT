/*
 * Created on 2006-3-20
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.*;
//import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class LearningModuleReport extends ReportTemplate {
    public static final int PAGE_COUNT = 20;
    
    public class ReportDate{
        public long tkh_id; 
        public long usr_ent_id;
        public String usr_ste_usr_id;
        public String usr_display_bil;
        public String usg_full_path;
        public String ugr_name;
        public String email;
        public String phone;
        public String staffNo;
        public Timestamp enrollmentDate;  
        public long completionStatus;
        Vector vtMod;
    }
    
    public static class ModDate{
        public long id;
        public String statue;
        public float scort;
        public String grade;
        public String mod_type;
        public float max_score;
        public String title;
    }
     
    public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) throws cwException, SQLException, cwException, Exception, IOException {

        return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, Report.MODULE, null);
    }

    public static long getItmIdParent(Connection con, long id) throws SQLException {
        long itm_id_parent = 0;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.SQLCOSIDBYCLSID);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            itm_id_parent = rs.getLong("ire_parent_itm_id");
        }
        stmt.close();
        return itm_id_parent;
    }
   
    public Vector getModRpt(Connection con, loginProfile prof, Hashtable spec_pairs, long usr_ste_ent_id, boolean tc_enabled) throws SQLException {
        Vector vtModRpt = null;
        Vector vtRptlist = new Vector();
        Vector vtModId = null;
        Vector vtUsgId = null;
        Vector usr_content_lst = new Vector();
        Vector content_lst = new Vector();
        Vector vtChildModId;
        Vector vtEndId = new Vector();
        ModDate moddate = null;

        if (spec_pairs.containsKey("mod_id")) {
            vtModId = (Vector) spec_pairs.get("mod_id");
        }
        if (spec_pairs.containsKey("usg_ent_id")) {
            vtUsgId = (Vector) spec_pairs.get("usg_ent_id");
            vtEndId = getEntIdForUsg(con, vtUsgId);
        }
        if (spec_pairs.containsKey("usr_content_lst")) {
            usr_content_lst = (Vector) spec_pairs.get("usr_content_lst");
        }
        if (spec_pairs.containsKey("content_lst")) {
            content_lst = (Vector) spec_pairs.get("content_lst");
        }
        vtChildModId = getChildModId(con, vtModId);
        //根据所选用户组，得到所有的用户
        String sql = SqlStatements.SQL_MOD_RPT(con, prof, false, spec_pairs, vtChildModId, vtEndId, tc_enabled);
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, usr_ste_ent_id);
        if (spec_pairs.containsKey("att_create_start_datetime")) {
        	stmt.setTimestamp(index++, Timestamp.valueOf((String)((Vector) spec_pairs.get("att_create_start_datetime")).get(0)));
        }
        if (spec_pairs.containsKey("att_create_end_datetime")) {
        	stmt.setTimestamp(index++, Timestamp.valueOf((String)((Vector) spec_pairs.get("att_create_end_datetime")).get(0)));
        }
        ResultSet rs = stmt.executeQuery();
        ReportDate reportdate = null;
        long cur_tkh_id = 0;
        long pre_tkh_id = 0;
        long total_tkh_id = 0;
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        while (rs.next()) {
            cur_tkh_id = rs.getLong("mov_tkh_id");
            if (pre_tkh_id != cur_tkh_id) {
                if (vtRptlist.size() < PAGE_COUNT) {
                    if (vtModRpt != null && vtModRpt.size() > 0) {
                        reportdate.vtMod = vtModRpt;
                        vtRptlist.addElement(reportdate);
                    }
                    vtModRpt = new Vector();
                    reportdate = new ReportDate();
                    reportdate.tkh_id = rs.getLong("mov_tkh_id");
                    reportdate.usr_ent_id = rs.getLong("usr_ent_id");
                    if (usr_content_lst.contains("usr_id")) {
                        reportdate.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                    }
                    if (usr_content_lst.contains("usr_display_bil")) {
                        reportdate.usr_display_bil = rs.getString("usr_display_bil");
                    }
                    if (usr_content_lst.contains("USR_PARENT_USG")) {
                        long usg_id = rs.getLong("ern_usg_id");
                        if(usg_id == 0){
                        	usg_id = rs.getLong("erh_usg_id");
                        }
                        reportdate.usg_full_path = entityfullpath.getFullPath(con, usg_id);
                    }
                    if (usr_content_lst.contains("USR_CURRENT_UGR")) {
                    	long ugr_id = rs.getLong("ern_ugr_id");
                    	if(ugr_id == 0){
                    		ugr_id = rs.getLong("erh_ugr_id");
                    	}
                        reportdate.ugr_name = entityfullpath.getEntityName(con, ugr_id);
                    }
                    if (usr_content_lst.contains("usr_email")) {
                        reportdate.email = rs.getString("usr_email");
                    }
                    if (usr_content_lst.contains("usr_tel_1")) {
                        reportdate.phone = rs.getString("usr_tel_1");
                    }
                    if (usr_content_lst.contains("usr_extra_2")) {
                        reportdate.staffNo = rs.getString("usr_extra_2");
                    }
                    if (content_lst.contains("att_create_timestamp")) {
                        reportdate.enrollmentDate = rs.getTimestamp("att_create_timestamp");
                    }
                    if (content_lst.contains("att_status")) {
                        reportdate.completionStatus = rs.getLong("att_ats_id");
                    }
                }
                total_tkh_id++;
            }
            if (vtRptlist.size() < PAGE_COUNT) {
                //如果选择的是统一内容的课程的模块，报名到班级的模块id取parent
                moddate = new ModDate();
                if (vtChildModId.size() > 0) {
                    if (rs.getLong("mod_mod_res_id_parent") > 0) {
                        moddate.id = rs.getLong("mod_mod_res_id_parent");
                    } else {
                        moddate.id = rs.getLong("mov_mod_id");
                    }
                } else {
                    moddate.id = rs.getLong("mov_mod_id");
                }
                moddate.statue = rs.getString("mov_status");
                moddate.scort = rs.getFloat("mov_score");
                moddate.mod_type = rs.getString("mod_type");
                moddate.max_score = rs.getFloat("mod_max_score");
                moddate.grade = rs.getString("pgr_grade");
                vtModRpt.addElement(moddate);
            }
            pre_tkh_id = cur_tkh_id;
        }
        if (reportdate != null && vtRptlist.size() < PAGE_COUNT) {
            reportdate.vtMod = vtModRpt;
            vtRptlist.addElement(reportdate);
        }
        stmt.close();
        vtRptlist.addElement(new Long(total_tkh_id));
        return vtRptlist;
    }
   
    public String getSelectModXML(Connection con, Hashtable spec_pairs) throws SQLException {
        StringBuffer modXML = new StringBuffer();
        Vector mod_id_lst = new Vector();
        if (spec_pairs.containsKey("mod_id")) {
            mod_id_lst = (Vector) spec_pairs.get("mod_id");
        }
        String[] mod_id = new String[mod_id_lst.size()];
        for (int i = 0; i < mod_id_lst.size(); i++) {
            mod_id[i] = (String) mod_id_lst.get(i);
        }
        Vector vt = getSelectMod(con, mod_id)[1];
        modXML.append("<mod_list>");
        for (int j = 0; j < vt.size(); j++) {
            ModDate moddata = (ModDate) vt.get(j);
            modXML.append("<mod id=\"" + moddata.id + "\"");
            modXML.append(" title=\"" + cwUtils.esc4XML(moddata.title) + "\"");
            modXML.append(" type=\"" + moddata.mod_type + "\"");
            modXML.append(" max_score=\"" + moddata.max_score + "\"" + "/>");
        }
        modXML.append("</mod_list>");
        return modXML.toString();
    }

    public String getModReportAsXML(Connection con,loginProfile prof, Hashtable spec_pairs, long usr_ste_ent_id, boolean tc_enabled) throws SQLException {
        Vector vtReport = getModRpt(con, prof, spec_pairs, usr_ste_ent_id, tc_enabled);
        StringBuffer XML = new StringBuffer();
        //所选模块的id ,类型，积分信息的XML
        XML.append(getSelectModXML(con, spec_pairs));
        XML.append("<report_list>");
        ReportDate reportdate;
        ModDate moddate;
        for (int i = 0; i < vtReport.size() - 1; i++) {
            reportdate = (ReportDate) vtReport.get(i);
            XML.append("<record tkh_id=\"" + reportdate.tkh_id + "\"");
            XML.append(" usr_ent_id=\"" + reportdate.usr_ent_id + "\"");
            XML.append(" usr_ste_usr_id=\"" + cwUtils.esc4XML(reportdate.usr_ste_usr_id) + "\"");
            XML.append(" usr_display_bil=\"" + cwUtils.esc4XML(reportdate.usr_display_bil) + "\"");
            XML.append(" usg_full_path=\"" + cwUtils.esc4XML(reportdate.usg_full_path) + "\"");
            XML.append(" ugr_name=\"" + cwUtils.esc4XML(reportdate.ugr_name) + "\"");
            XML.append(" usr_email=\"" + cwUtils.esc4XML(reportdate.email) + "\"");
            XML.append(" usr_tel_1=\"" + cwUtils.esc4XML(reportdate.phone) + "\"");
            XML.append(" usr_extra_2=\"" + cwUtils.esc4XML(reportdate.staffNo) + "\"");
            XML.append(" att_create_timestamp=\"" + reportdate.enrollmentDate + "\"");
            XML.append(" att_ats_id=\"" + reportdate.completionStatus + "\">");
            for (int j = 0; j < reportdate.vtMod.size(); j++) {
                moddate = (ModDate) reportdate.vtMod.get(j);
                XML.append("<mov mod_id=\"" + moddate.id + "\"");
                XML.append(" status=\"" + moddate.statue + "\"");
                if (moddate.mod_type.equals(dbModule.MOD_TYPE_ASS) && moddate.max_score < 0) {
                    XML.append(" grade=\"" + moddate.grade + "\"");
                } else {
                    XML.append(" score=\"" + moddate.scort + "\"");
                }
                XML.append("/>");
            }
            XML.append("</record>");
        }
        XML.append("</report_list>");
        cwPagination pageination = new cwPagination();
        pageination.totalRec = ((Long) vtReport.get(vtReport.size() - 1)).intValue();
        pageination.pageSize = PAGE_COUNT;
        pageination.curPage = 1;
        XML.append(pageination.asXML());
        return XML.toString();
    }

    public String getReportXML(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id, String rsp_title, String[] spec_name, String[] spec_value, boolean tc_enabled) throws SQLException, cwException, cwSysMessage {
        String reportxml = "";
        Hashtable spec_pairs;
        String rsp_xml;
        if (rsp_id > 0) {
            ViewReportSpec spec = new ViewReportSpec();
            ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
            rsp_xml = data.rsp_xml;
            spec_pairs = this.getSpecPairs(data.rsp_xml);

        } else {
            DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);
            rsp_xml = rpt_spec.rsp_xml;
            spec_pairs = this.getSpecPairs(rpt_spec.rsp_xml);
        }
        String xml = getModReportAsXML(con, prof, spec_pairs, prof.root_ent_id, tc_enabled);
        if (rsp_id > 0) {
            reportxml = getReport(con, request, prof, rsp_id, rte_id, null, xml, null, Report.MODULE, null);
        } else {
            reportxml = getReport(con, request, prof, 0, rte_id, null, xml, rsp_xml, Report.MODULE, rsp_title);
        }
        return reportxml;
    }
   
    public static Vector getChildModId(Connection con, Vector id) throws SQLException {
   	 String TableName = null;
    	String ColName = "p_mod_ids";
    	
    	TableName = cwSQL.createSimpleTemptable(con, ColName, cwSQL.COL_TYPE_LONG, 0);
		if(TableName != null) {
			cwSQL.insertSimpleTempTable(con, TableName, id, cwSQL.COL_TYPE_STRING);
		}
		Vector vt = new Vector();
       StringBuffer sql = new StringBuffer();
       sql.append("select mod_res_id from Module where mod_mod_res_id_parent in (select "+ColName+" from "+TableName+")");
       
       PreparedStatement stmt = null;
       stmt = con.prepareStatement(sql.toString());
       ResultSet rs = stmt.executeQuery();
       while (rs.next()) {
           vt.addElement(new Long(rs.getLong("mod_res_id")));
       }
       stmt.close();
       if(TableName != null ){
			   cwSQL.dropTempTable(con, TableName);
		   }
       return vt;
   }
    
    public static Vector getEntIdForUsg(Connection con, Vector id) throws SQLException {
        Vector vt = new Vector();
        Vector allEndId = new Vector();
        for (int i = 0; i < id.size(); i++) {
            StringBuffer sql = new StringBuffer();
            sql.append("select ern_child_ent_id from EntityRelation where ern_ancestor_ent_id = ");
            sql.append(id.get(i));
            PreparedStatement stmt = con.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                vt.addElement(new Long(rs.getLong("ern_child_ent_id")));
            }
            stmt.close();
            for (int j = 0; j < vt.size(); j++) {
                if (!allEndId.contains((Long) vt.get(j))) {
                    allEndId.addElement((Long) vt.get(j));
                }
            }
        }
        return allEndId;
    }
    
    public static Vector[] getSelectMod(Connection con, String[] mod_id) throws SQLException {
        Vector vtModId = new Vector();
        for (int i = 0; i < mod_id.length; i++) {
            vtModId.addElement(new Long(mod_id[i]));
        }
        String colName = "tmp_mod_id";
        String tmp_modId_table = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tmp_modId_table, vtModId, cwSQL.COL_TYPE_LONG);
        String sql = "select mod_res_id, mod_type, res_title, mod_max_score  from Module,Resources" + " where mod_res_id = res_id and mod_res_id in (select tmp_mod_id from " + tmp_modId_table + ")" + " order by res_title, mod_res_id";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        Vector vt = new Vector();
        vtModId.clear();
        while (rs.next()) {
            vtModId.addElement(Long.toString(rs.getLong("mod_res_id")));
            ModDate moddata = new ModDate();
            moddata.id = rs.getLong("mod_res_id");
            moddata.mod_type = rs.getString("mod_type");
            moddata.max_score = rs.getFloat("mod_max_score");
            moddata.title = rs.getString("res_title");
            vt.addElement(moddata);
        }
        stmt.close();
        return new Vector[] { vtModId, vt };
    }
}

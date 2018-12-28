/*
 * Created on 2006-3-23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import jxl.write.WriteException;

import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;


public class ModuleRptExport extends ReportExporter {

    public ModuleRptExport(Connection incon, ExportController inController) {
        super(incon, inController);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see com.cw.wizbank.report.ReportExporter#getReportXls(com.cw.wizbank.qdb.loginProfile, com.cw.wizbank.report.ReportExporter.SpecData, com.cw.wizbank.config.organization.usermanagement.UserManagement)
     */
    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
        // TODO Auto-generated method stub
        Vector vtChildModId = new Vector();
        Vector vtEndId = new Vector();
        Hashtable spec_pairs = new Hashtable();
        Vector modId = new Vector();
        Vector sel_mod = new Vector();
        //join all content_vec together.
        Vector rpt_content = new Vector();
        if (specData.usr_content_vec != null) {
            rpt_content.addAll(specData.usr_content_vec);
        }
        if (specData.content_vec != null) {
            rpt_content.addAll(specData.content_vec);
        }

        if (specData.mod_id_lst != null) {
            modId = LearningModuleReport.getSelectMod(con, specData.mod_id_lst)[0];
            sel_mod = LearningModuleReport.getSelectMod(con, specData.mod_id_lst)[1];
            spec_pairs.put("mod_id", modId);
            vtChildModId = LearningModuleReport.getChildModId(con, modId);
        }
        if(wizbini.cfgTcEnabled) {
        	Vector vec0 = new Vector();
        	vec0.add("0");
    		Vector vec1 = new Vector();
    		vec1.add("1");
        	if (specData.all_user_ind) {
        		spec_pairs.put("all_user_ind",vec1);
        	} else {
        		spec_pairs.put("all_user_ind",vec0);
        	}
        	if (specData.answer_for_lrn) {
        		spec_pairs.put("answer_for_lrn",vec1);
        	} else {
        		spec_pairs.put("answer_for_lrn",vec0);
        	}
        	if (specData.answer_for_course_lrn) {
        		spec_pairs.put("answer_for_course_lrn",vec1);
        	} else {
        		spec_pairs.put("answer_for_course_lrn",vec0);
        	}
        }
        if (specData.usr_ent_id != null && specData.usr_ent_id.size() > 0) {
            spec_pairs.put("usr_ent_id", specData.usr_ent_id);
        }

        Vector usg_ent_id_lst = null;
        if (specData.usg_ent_id_lst != null) {
            usg_ent_id_lst = new Vector();
            for (int i = 0; i < specData.usg_ent_id_lst.length; i++) {
                usg_ent_id_lst.addElement(specData.usg_ent_id_lst[i]);
            }
            spec_pairs.put("usg_ent_id", usg_ent_id_lst);
            vtEndId = LearningModuleReport.getEntIdForUsg(con, usg_ent_id_lst);
        }
        
        if (specData.usr_content_vec != null) {
            spec_pairs.put("usr_content_lst", specData.usr_content_vec);
        }
        if (specData.content_vec != null) {
            spec_pairs.put("content_lst", specData.content_vec);
        }

        if (specData.att_create_start_datetime != null) {
            Vector vt = new Vector();
            vt.addElement(specData.att_create_start_datetime.toString());
            spec_pairs.put("att_create_start_datetime", vt);
        }

        if (specData.att_create_end_datetime != null) {
            Vector vt = new Vector();
            vt.addElement(specData.att_create_end_datetime.toString());
            spec_pairs.put("att_create_end_datetime", vt);
        }

        if (specData.ats_id_lst != null) {
            Vector vt = new Vector();
            if (specData.ats_id_lst.length > 1) {
                vt.addElement("0");
                spec_pairs.put("ats_id", vt);
            } else {
                vt.addElement(specData.ats_id_lst[0]);
                spec_pairs.put("ats_id", vt);
            }
        }

        if (specData.sortCol != null) {
            Vector vt = new Vector();
            vt.addElement(specData.sortCol);
            spec_pairs.put("sort_col", vt);
        }

        if (specData.sortOrder != null) {
            Vector vt = new Vector();
            vt.addElement(specData.sortOrder);
            spec_pairs.put("sort_order", vt);
        }
        
        if (specData.itm_id_lst != null) {
            Vector vt = new Vector();
            for (int i = 0; i < specData.itm_id_lst.length; i++) {
            	vt.addElement(specData.itm_id_lst[i]);
            }
            spec_pairs.put("itm_id", vt);
        }
        
        if (specData.ent_id_lst != null) {
        	 Vector vt = new Vector();
             for (int i = 0; i < specData.ent_id_lst.length; i++) {
            	 vt.addElement(specData.ent_id_lst[i]);
             }
             spec_pairs.put("usr_ent_id", vt);
        }
        

        PreparedStatement stmt = getRptStmt(con, prof, true, spec_pairs, vtChildModId, vtEndId, wizbini.cfgTcEnabled);
        int index = 1;
        stmt.setLong(index++, prof.root_ent_id);
        if (spec_pairs.containsKey("att_create_start_datetime")) {
        	stmt.setTimestamp(index++, Timestamp.valueOf((String)((Vector) spec_pairs.get("att_create_start_datetime")).get(0)));
        }
        if (spec_pairs.containsKey("att_create_end_datetime")) {
        	stmt.setTimestamp(index++, Timestamp.valueOf((String)((Vector) spec_pairs.get("att_create_end_datetime")).get(0)));
        }
        ResultSet result = stmt.executeQuery();
        int rpt_count = 0;
        if (result.next()) {
            rpt_count = result.getInt(1);
            controller.setTotalRow(rpt_count);
        }
        stmt.close();

        if (rpt_count > 0) {
            stmt = getRptStmt(con, prof, false, spec_pairs, vtChildModId, vtEndId, wizbini.cfgTcEnabled);
            int idx = 1;
            stmt.setLong(idx++, prof.root_ent_id);
            if (spec_pairs.containsKey("att_create_start_datetime")) {
            	stmt.setTimestamp(idx++, Timestamp.valueOf((String)((Vector) spec_pairs.get("att_create_start_datetime")).get(0)));
            }
            if (spec_pairs.containsKey("att_create_end_datetime")) {
            	stmt.setTimestamp(idx++, Timestamp.valueOf((String)((Vector) spec_pairs.get("att_create_end_datetime")).get(0)));
            }
            result = stmt.executeQuery();
            ModuleRptExportHelper rptBuilder = new ModuleRptExportHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit,wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getUrl());
            rptBuilder.writeCondition(con, specData);
            Hashtable hashtable = rptBuilder.writeTableHead(rpt_content, specData.cur_lang, um, sel_mod);
            resultData resData = null;
            LearningModuleReport.ModDate moddate = null;
            Vector vtModRpt = new Vector();
            long cur_tkh_id = 0;
            long pre_tkh_id = 0;
            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
            while (result.next() && !controller.isCancelled()) {
                cur_tkh_id = result.getLong("mov_tkh_id");
                if (pre_tkh_id != cur_tkh_id) {
                    if (vtModRpt != null && vtModRpt.size() > 0) {
                        resData.vtmod = vtModRpt;
                        rptBuilder.writeData(resData, rpt_content, specData.cur_lang, hashtable);
                    }
                    vtModRpt = new Vector();
                    resData = new resultData();
                    resData.app_tkh_id = result.getLong("mov_tkh_id");
                    resData.usr_ent_id = result.getLong("usr_ent_id");
                    if (specData.usr_content_vec != null) {
                        if (specData.usr_content_vec.contains("usr_id")) {
                            resData.usr_ste_usr_id = result.getString("usr_ste_usr_id");
                        }
                        if (specData.usr_content_vec.contains("usr_display_bil")) {
                            resData.usr_display_bil = result.getString("usr_display_bil");
                        }
                        if ((specData.usr_content_vec).contains("USR_PARENT_USG")) {
                        	long usg_id = result.getLong("ern_usg_id");
                            if(usg_id == 0){
                            	usg_id = result.getLong("erh_usg_id");
                            }
                            resData.group_name = entityfullpath.getFullPath(con, usg_id);
                        }
                        if ((specData.usr_content_vec).contains("USR_CURRENT_UGR")) {
                            long ugr_id = result.getLong("ern_ugr_id");
                            if(ugr_id == 0){
                            	ugr_id = result.getLong("erh_ugr_id");
                            }
                            resData.grade_name = entityfullpath.getEntityName(con, ugr_id);
                        }
                        if ((specData.usr_content_vec).contains("usr_email")) {
                            resData.usr_email = result.getString("usr_email");
                        }
                        if ((specData.usr_content_vec).contains("usr_tel_1")) {
                            resData.usr_tel_1 = result.getString("usr_tel_1");
                        }
                        if ((specData.usr_content_vec).contains("usr_extra_2")) {
                            resData.usr_extra_2 = result.getString("usr_extra_2");
                        }
                    }
                    if (specData.content_vec != null) {
                        if (specData.content_vec.contains("att_create_timestamp")) {
                            resData.att_create_timestamp = result.getTimestamp("att_create_timestamp");
                        }
                        if (specData.content_vec.contains("att_status")) {
                            resData.att_ats_id = LearnerRptExportHelper.getAtsTitle(result.getLong("att_ats_id"), specData.cur_lang);
                        }
                    }
                }
                //如果选择的是统一内容的课程的模块，报名到班级的模块id取parent
                moddate = new LearningModuleReport.ModDate();
                if (vtChildModId.size() > 0) {
                    if (result.getLong("mod_mod_res_id_parent") > 0) {
                        moddate.id = result.getLong("mod_mod_res_id_parent");
                    } else {
                        moddate.id = result.getLong("mov_mod_id");
                    }
                } else {
                    moddate.id = result.getLong("mov_mod_id");
                }
                moddate.statue = result.getString("mov_status");
                moddate.scort = result.getFloat("mov_score");
                moddate.mod_type = result.getString("mod_type");
                moddate.max_score = result.getFloat("mod_max_score");
                moddate.grade = result.getString("pgr_grade");
                vtModRpt.addElement(moddate);

                pre_tkh_id = cur_tkh_id;
                controller.next(); //控制进度条
            }
            if (cur_tkh_id != 0 && pre_tkh_id != 0) {
                resData.vtmod = vtModRpt;
                rptBuilder.writeData(resData, rpt_content, specData.cur_lang, hashtable);
            }
            stmt.close();
            if (!controller.isCancelled()) {
                controller.setFile(rptBuilder.finalizeFile());
            }
        }
    }
    
    public static PreparedStatement getRptStmt(Connection con,loginProfile prof, boolean isCount, Hashtable spec_pairs, Vector vtChildModId, Vector vtEndId, boolean tc_enbaled) throws SQLException {
        String sql = "";
        //String sql2 = SqlStatements.SQL_MOD_RPT(con, prof, isCount, spec_pairs, vtChildModId, vtEndId, tc_enbaled);
        sql = SqlStatements.SQL_MOD_RPT(con,prof, isCount, spec_pairs, vtChildModId, vtEndId, tc_enbaled);
        PreparedStatement stmt = con.prepareStatement(sql);
        return stmt;
    }
    
    public static String getUsgTitle(Connection con, long id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(" SELECT usg_display_bil FROM userGroup " + " where usg_ent_id = ? ");
        stmt.setLong(1, id);
        String usg_display_bil = new String("");

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            usg_display_bil = rs.getString("usg_display_bil");
        }
        stmt.close();
        return usg_display_bil;
    }
}

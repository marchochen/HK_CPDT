/*
 * Created on 2006-3-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.config.organization.usermanagement.LabelType;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.report.ModuleRptExport;

import jxl.write.WriteException;

public class ModuleRptExportHelper extends ExportHelper {

    public ModuleRptExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit);
        // TODO Auto-generated constructor stub
    }

    public ModuleRptExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String url) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit, url);
        // TODO Auto-generated constructor stub
    }

    public void writeCondition(Connection con, ReportExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage, cwException {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        } else {
            reportTitle = getRptTitle(specData.cur_lang, Report.MODULE);
        }
        setTitleText(reportTitle);

        String[] select_mod = null;
        if (specData.itm_id_lst != null) {
            getNewRow();
            StringBuffer itm_title = new StringBuffer();
            aeItem aeItm = new aeItem();
            long parent_id = LearningModuleReport.getItmIdParent(con, specData.itm_id_lst[0]);
            if (parent_id > 0) {
                aeItm.itm_id = parent_id;
                itm_title.append(aeItem.getItemTitle(con, parent_id) + "(");
                itm_title.append(aeItm.getItemCode(con) + ")");
                itm_title.append(" > ");
            }
            aeItm.itm_id = specData.itm_id_lst[0];
            itm_title.append(aeItem.getItemTitle(con, specData.itm_id_lst[0]) + "(");
            itm_title.append(aeItm.getItemCode(con) + ")");

            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_cos"), ColumnA);
            setCellContent(itm_title.toString(), ColumnB);
        }
        if (specData.mod_id_lst != null) {
            getNewRow();
            StringBuffer mod_title = new StringBuffer();
            String title = "";
            for (int i = 0; i < specData.mod_id_lst.length; i++) {
                title = dbResource.getResTitle(con, Long.parseLong(specData.mod_id_lst[i]));
                mod_title.append(title);
                if (i != specData.mod_id_lst.length - 1) {
                    mod_title.append(", ");
                }
            }
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_module"), ColumnA);
            setCellContent(mod_title.toString(), ColumnB);
        }
        if (specData.ent_id_lst != null && !specData.all_user_ind) {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_lrn_group"), ColumnA);
            String ent_name = LearnerRptExporter.getEntName(con, specData.ent_id_lst);
            setCellContent(ent_name, ColumnB);
        } else if (specData.usg_ent_id_lst != null && !specData.all_user_ind) {
            getNewRow();
            StringBuffer usg_title = new StringBuffer();
            for (int i = 0; i < specData.usg_ent_id_lst.length; i++) {
                usg_title.append(ModuleRptExport.getUsgTitle(con, Long.parseLong(specData.usg_ent_id_lst[i])));
                if (i != specData.usg_ent_id_lst.length - 1) {
                    usg_title.append(", ");
                }
            }
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_lrn_group"), ColumnA);
            setCellContent(usg_title.toString(), ColumnB);
        } else {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_lrn_group"), ColumnA);
            if(specData.all_user_ind && specData.answer_for_lrn && !specData.answer_for_course_lrn) {
            	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_answer_for_lrn"), ColumnB);
            } else if (specData.all_user_ind && !specData.answer_for_lrn && specData.answer_for_course_lrn) {
            	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_answer_for_course_lrn"), ColumnB);
            } else  {
            	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_by_all_user"), ColumnB);
            }
        }
        if (specData.att_create_start_datetime != null || specData.att_create_end_datetime != null) {
            getNewRow();
            StringBuffer timeStr = new StringBuffer();
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_from")).append(" ");
            if (specData.att_create_start_datetime != null) {
                timeStr.append(aeUtils.escNull(specData.att_create_start_datetime));
            } else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_to")).append(" ");
            if (specData.att_create_end_datetime != null) {
                timeStr.append(aeUtils.escNull(specData.att_create_end_datetime));
            } else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }

            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_att_create_timestamp"), ColumnA);
            setCellContent(timeStr.toString(), ColumnB);
        }

        if (specData.ats_id_lst != null) {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_att_status"), ColumnA);
            String status = LearnerRptExportHelper.getAtsTitle(specData.ats_id_lst, specData.cur_lang);
            setCellContent(status, ColumnB);
        }
        getNewRow();
    }

    public void writeData(ReportExporter.resultData resData, Vector rpt_content_vec, String cur_lang, Hashtable hashtable) throws WriteException {
        Iterator obj = rpt_content_vec.iterator();
        short index = 0;
        getNewRow();
        LearningModuleReport.ModDate moddata = null;
        while (obj.hasNext()) {
            String content_Str = (String) obj.next();
            if (content_Str.equals("usr_id")) {
                setCellContent(resData.usr_ste_usr_id, index++);
            } else if (content_Str.equals("usr_display_bil")) {
                setCellContent(resData.usr_display_bil, index++);
            } else if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)) {
                setCellContent(resData.group_name, index++);
            } else if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR)) {
                setCellContent(resData.grade_name, index++);
            } else if (content_Str.equals("usr_email")) {
                setCellContent(resData.usr_email, index++);
            } else if (content_Str.equals("usr_tel_1")) {
                setCellContent(resData.usr_tel_1, index++);
            } else if (content_Str.equals("usr_extra_2")) {
                setCellContent(resData.usr_extra_2, index++);
            } else if (content_Str.equals("att_create_timestamp")) {
                setCellContent(resData.att_create_timestamp, index++);
            } else if (content_Str.equals("att_status")) {
                setCellContent(resData.att_ats_id, index++);
            }
        }

        Vector vtState = new Vector();
        int starCol = rpt_content_vec.size();
        vtState.addElement(Integer.toString(starCol));
        for (int i = 0; i < resData.vtmod.size(); i++) {
            moddata = (LearningModuleReport.ModDate) resData.vtmod.get(i);
            String type = moddata.mod_type;
            long id = moddata.id;
            int col = ((Long) hashtable.get(new Long(id))).intValue();
            short count = (short) col;
            setCellContent(getMtstTitle(moddata.statue, moddata.mod_type, moddata.max_score, moddata.scort, moddata.grade, cur_lang), count++);
            if (type.equals(dbModule.MOD_TYPE_ASS)) {
                if (moddata.max_score < 0) {
                    setCellContent(getGrade(moddata.grade, cur_lang), count++);
                } else {
                    setCellContent(moddata.scort, count++);
                }
            }
            if (type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_NETG_COK) || type.equals(dbModule.MOD_TYPE_DXT) || type.equals(dbModule.MOD_TYPE_TST) || type.equals(dbModule.MOD_TYPE_SCO)) {
                setCellContent(moddata.scort, count++);
            }
        }
    }

    /**
     * @param rpt_content
     */
    public Hashtable writeTableHead(Vector rpt_content, String cur_lang, UserManagement um, Vector sel_mod) throws WriteException {
        Iterator iter_content = rpt_content.iterator();
        short index = 0;
        Iterator lt = null;
        LabelType labelType;
        Hashtable hashtable = new Hashtable();
        getNewRow();
        short count = (short) rpt_content.size();
        //生成title
        for (int j = 0; j < sel_mod.size(); j++) {
            LearningModuleReport.ModDate moddata = (LearningModuleReport.ModDate) sel_mod.get(j);
            String type = moddata.mod_type;
            hashtable.put(new Long(moddata.id), new Long(count));
            setCellContent(moddata.title, count++, this.getStyleByName(STYLE_BOLD_FONT));
            if (type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_NETG_COK) || type.equals(dbModule.MOD_TYPE_SCO) || type.equals(dbModule.MOD_TYPE_ASS) || type.equals(dbModule.MOD_TYPE_DXT) || type.equals(dbModule.MOD_TYPE_TST)) {
                count++;
            }
        }
        getNewRow();
        while (iter_content.hasNext()) {
            String iterStr = (String) iter_content.next();
            if (iterStr.equals("usr_id")) {
                lt = um.getUserProfile().getProfileAttributes().getUserId().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType) lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            } else if (iterStr.equals("usr_display_bil")) {
                lt = um.getUserProfile().getProfileAttributes().getName().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType) lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            } else if (iterStr.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)) {
                lt = um.getUserProfile().getProfileAttributes().getGroup().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType) lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            } else if (iterStr.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR)) {
                lt = um.getUserProfile().getProfileAttributes().getGrade().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType) lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            } else if (iterStr.equals("usr_email")) {
                lt = um.getUserProfile().getProfileAttributes().getEmail().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType) lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            } else if (iterStr.equals("usr_tel_1")) {
                lt = um.getUserProfile().getProfileAttributes().getPhone().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType) lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            } else if (iterStr.equals("usr_extra_2")) {
                setCellContent(LangLabel.getValue(cur_lang, "label_core_cpt_d_management_231"), index++);
            } else if (iterStr.equals("att_create_timestamp")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_att_create_timestamp"), index++);
            } else if (iterStr.equals("att_status")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_att_status"), index++);
            }
        }
        for (int j = 0; j < sel_mod.size(); j++) {
            LearningModuleReport.ModDate moddata = (LearningModuleReport.ModDate) sel_mod.get(j);
            setCellContent(LangLabel.getValue(cur_lang, "lab_status"), index++);
            String type = moddata.mod_type;
            if (type.equals(dbModule.MOD_TYPE_ASS)) {
                if (moddata.max_score < 0) {
                    setCellContent(LangLabel.getValue(cur_lang, "lab_mod_grade"), index++);
                } else {
                    setCellContent(LangLabel.getValue(cur_lang, "lab_mod_score"), index++);
                }
            }
            if (type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_NETG_COK) || type.equals(dbModule.MOD_TYPE_DXT) || type.equals(dbModule.MOD_TYPE_TST) || type.equals(dbModule.MOD_TYPE_SCO)) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_mod_score"), index++);
            }
        }
        return hashtable;
    }

    public static String getGrade(String grade, String cur_lang) {
        String result = null;
        if (grade != null) {
            if (grade.equals("A+")) {
                result = LangLabel.getValue(cur_lang, "lab_A+");
            }
            if (grade.equals("A")) {
                result = LangLabel.getValue(cur_lang, "lab_A");
            }
            if (grade.equals("A-")) {
                result = LangLabel.getValue(cur_lang, "lab_A-");
            }
            if (grade.equals("B+")) {
                result = LangLabel.getValue(cur_lang, "lab_B+");
            }
            if (grade.equals("B")) {
                result = LangLabel.getValue(cur_lang, "lab_B");
            }
            if (grade.equals("B-")) {
                result = LangLabel.getValue(cur_lang, "lab_B-");
            }
            if (grade.equals("C+")) {
                result = LangLabel.getValue(cur_lang, "lab_C+");
            }
            if (grade.equals("C")) {
                result = LangLabel.getValue(cur_lang, "lab_C");
            }
            if (grade.equals("C-")) {
                result = LangLabel.getValue(cur_lang, "lab_C-");
            }
            if (grade.equals("D")) {
                result = LangLabel.getValue(cur_lang, "lab_D");
            }
            if (grade.equals("F")) {
                result = LangLabel.getValue(cur_lang, "lab_F");
            }
        }
        return result;
    }

    public static String getMtstTitle(String ats, String type, float max_score, float scort, String grade, String cur_lang) {
        String status = null;
        if (ats.equals("C")) {
            if (type.equals(dbModule.MOD_TYPE_VOD) || type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_NETG_COK) || type.equals(dbModule.MOD_TYPE_SCO) || type.equals(dbModule.MOD_TYPE_SVY) || type.equals(dbModule.MOD_TYPE_RDG)) {
                status = LangLabel.getValue(cur_lang, "lab_completed");
            } else if (type.equals(dbModule.MOD_TYPE_ASS)) {
                if (max_score < 0) {
                    if (grade != null && !grade.equals("")) {
                        status = LangLabel.getValue(cur_lang, "lab_graded");
                    } else {
                        status = LangLabel.getValue(cur_lang, "lab_not_graded");
                    }
                } else {
                    if (scort > 0) {
                        status = LangLabel.getValue(cur_lang, "lab_graded");
                    } else {
                        status = LangLabel.getValue(cur_lang, "lab_not_graded");
                    }
                }
            }
        } else if (ats.equals("I")) {
            if (type.equals(dbModule.MOD_TYPE_VOD) || type.equals(dbModule.MOD_TYPE_RDG) || type.equals(dbModule.MOD_TYPE_REF) || type.equals(dbModule.MOD_TYPE_GLO)) {
                status = LangLabel.getValue(cur_lang, "lab_viewed");
            } else if (type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_SCO)) {
                status = LangLabel.getValue(cur_lang, "lab_incomplete");
            } else if (type.equals(dbModule.MOD_TYPE_TST) || type.equals(dbModule.MOD_TYPE_STX) || type.equals(dbModule.MOD_TYPE_DXT) || type.equals(dbModule.MOD_TYPE_SVY) || type.equals(dbModule.MOD_TYPE_ASS)) {
                status = LangLabel.getValue(cur_lang, "lab_attempted");
            } else if (type.equals(dbModule.MOD_TYPE_FOR) || type.equals(dbModule.MOD_TYPE_FAQ) || type.equals(dbModule.MOD_TYPE_CHAT)) {
                status = LangLabel.getValue(cur_lang, "lab_participated");
            } else if (type.equals(dbModule.MOD_TYPE_NETG_COK)) {
                status = LangLabel.getValue(cur_lang, "lab_started");
            }
        } else if (ats.equals("N")) {
            if (type.equals(dbModule.MOD_TYPE_VOD) || type.equals(dbModule.MOD_TYPE_TST) || type.equals(dbModule.MOD_TYPE_STX) || type.equals(dbModule.MOD_TYPE_DXT) || type.equals(dbModule.MOD_TYPE_SVY) || type.equals(dbModule.MOD_TYPE_RDG) || type.equals(dbModule.MOD_TYPE_REF) || type.equals(dbModule.MOD_TYPE_GLO)) {
                status = LangLabel.getValue(cur_lang, "lab_not_viewed");
            } else if (type.equals(dbModule.MOD_TYPE_ASS)) {
                status = LangLabel.getValue(cur_lang, "lab_not_submitted");
            } else if (type.equals(dbModule.MOD_TYPE_FOR) || type.equals(dbModule.MOD_TYPE_FAQ) || type.equals(dbModule.MOD_TYPE_CHAT)) {
                status = LangLabel.getValue(cur_lang, "lab_not_participated");
            } else {
                status = LangLabel.getValue(cur_lang, "lab_not_attempted");
            }
        } else if (ats.equals("P")) {
            if (type.equals(dbModule.MOD_TYPE_TST) || type.equals(dbModule.MOD_TYPE_STX) || type.equals(dbModule.MOD_TYPE_DXT) || type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_NETG_COK) || type.equals(dbModule.MOD_TYPE_SCO) || type.equals(dbModule.MOD_TYPE_ASS) ) {
                status = LangLabel.getValue(cur_lang, "lab_passed");
            }
        } else if (ats.equals("F")) {
            if (type.equals(dbModule.MOD_TYPE_TST) || type.equals(dbModule.MOD_TYPE_STX) || type.equals(dbModule.MOD_TYPE_DXT) || type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_NETG_COK) || type.equals(dbModule.MOD_TYPE_SCO)|| type.equals(dbModule.MOD_TYPE_ASS) ) {
                status = LangLabel.getValue(cur_lang, "lab_failed");
            }
        } else if (ats.equals("B")) {
            if (type.equals(dbModule.MOD_TYPE_AICC_AU) || type.equals(dbModule.MOD_TYPE_SCO)) {
                status = LangLabel.getValue(cur_lang, "lab_browsed");
            }
        } else {
            status = LangLabel.getValue(cur_lang, "lab_not_attempted");
        }
        return status;
    }
}

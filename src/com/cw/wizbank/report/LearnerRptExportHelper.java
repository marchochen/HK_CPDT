/*
 * Created on 2005-9-16
 *
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import jxl.write.WriteException;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.config.organization.usermanagement.LabelType;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * @author dixson
 *
 */
public class LearnerRptExportHelper extends ExportHelper {
    public LearnerRptExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit,String url) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit, url);
    }

	public void writeCondition(Connection con, ReportExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        }
        else {
            reportTitle = getRptTitle(specData.cur_lang, Report.LEARNER);
        }
        setTitleText(reportTitle);
        if (specData.ent_id_lst != null&& specData.all_user_ind == false) {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_lrn_group"), ColumnA);
            if (specData.staff != null) {
                setCellContent(LangLabel.getValue(specData.cur_lang, specData.staff), ColumnB);                
            }else {
                String ent_name = LearnerRptExporter.getEntName(con, specData.ent_id_lst);
                setCellContent(ent_name, ColumnB);
            }
        }
        else {
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
       
        if (specData.itm_id_lst != null && !specData.all_cos_ind && specData.tnd_id_lst == null) {
            getNewRow();
            StringBuffer itm_title = new StringBuffer();
            for (int i = 0;i < specData.itm_id_lst.length;i ++) {
                itm_title.append(aeItem.getItemTitle(con, specData.itm_id_lst[i]));
                if (i != specData.itm_id_lst.length - 1) {
                    itm_title.append(", ");
                }
            }
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_cos"), ColumnA);
            setCellContent(itm_title.toString(), ColumnB);
        } else if(specData.tnd_id_lst != null && !specData.all_cos_ind && specData.itm_id_lst == null) {
        	getNewRow();
            String tnd_name = LearnerRptExporter.getTntName(con, specData.tnd_id_lst);
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_categories"), ColumnA);
            setCellContent(tnd_name, ColumnB);
        }
        else {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_cos"), ColumnA);
            if(specData.all_cos_ind && specData.answer_for_course && !specData.answer_for_lrn_course) {
            	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_answer_for_course"), ColumnB);	
            } else if (specData.all_cos_ind && !specData.answer_for_course && specData.answer_for_lrn_course){
            	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_answer_for_lrn_course"), ColumnB);
            } else {
                setCellContent(LangLabel.getValue(specData.cur_lang, "lab_all_cos"), ColumnB);	
            }
        }

        if (specData.att_create_start_datetime != null || specData.att_create_end_datetime != null) {
            getNewRow();
            StringBuffer timeStr = new StringBuffer();
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_from")).append(" ");
            if (specData.att_create_start_datetime != null) {
                timeStr.append(aeUtils.escNull(specData.att_create_start_datetime));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }
            timeStr.append(" ");
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_to")).append(" ");
            if (specData.att_create_end_datetime != null) {
                timeStr.append(aeUtils.escNull(specData.att_create_end_datetime));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }

            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_att_create_timestamp"), ColumnA);
            setCellContent(timeStr.toString(), ColumnB);
        }

        if (specData.ats_id_lst != null) {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_att_status"), ColumnA);
            String status = getAtsTitle(specData.ats_id_lst, specData.cur_lang);
            setCellContent(status, ColumnB);
        }
        getNewRow();
    }

    public void writeData(ReportExporter.resultData resData, Vector rpt_content_vec, String cur_lang) throws WriteException {
        Iterator obj = rpt_content_vec.iterator();
        short index = 0;
        getNewRow();
        while (obj.hasNext()) {
            String content_Str = (String)obj.next();
            if (content_Str.equals("usr_id") && resData.usr_ste_usr_id != null) {
            	setCellContent(resData.usr_ste_usr_id, index++); 
            } else if (content_Str.equals("usr_display_bil") && resData.usr_display_bil != null) {
                setCellContent(resData.usr_display_bil, index++);
            } else if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG) && resData.group_name != null) {
                setCellContent(resData.group_name, index++);
            } else if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR) && resData.grade_name != null && !UNSPECIFIED.equalsIgnoreCase(resData.grade_name)) {
                setCellContent(resData.grade_name, index++);
            } else if (content_Str.equals("usr_email") && resData.usr_email != null && resData.usr_email.length() > 0) {
                setCellContent(resData.usr_email, index++);
            } else if (content_Str.equals("usr_tel_1") && resData.usr_tel_1 != null && resData.usr_tel_1.length() > 0) {
                setCellContent(resData.usr_tel_1, index++);
            } else if (content_Str.equals("field01") && resData.itm_code != null) {
                setCellContent(resData.itm_code, index++);
            } else if (content_Str.equals("field02") && resData.itm_title != null) {
                setCellContent(resData.itm_title, index++);
            } else if (content_Str.equals("itm_type") && resData.itm_type != null) {
                setCellContent(resData.itm_dummy_type, index++);
            } else if (content_Str.equals("catalog") && resData.catNav != null && resData.catNav.length() > 0) {
                setCellContent(resData.catNav, index++);
            } else if(content_Str.equals("training_center") && resData.tcr_title != null) {
            	setCellContent(resData.tcr_title, index++);
            } else if (content_Str.equals("att_create_timestamp") && resData.att_create_timestamp != null) {
                setCellContent(resData.att_create_timestamp, index++);
            } else if (content_Str.equals("att_status") && resData.att_ats_id != null) {
                setCellContent(resData.att_ats_id, index++);
            } else if (content_Str.equals("att_timestamp") && resData.att_timestamp != null) {
                setCellContent(resData.att_timestamp, index++);
            } else if (content_Str.equals("cov_commence_datetime") && resData.cov_commence_datetime != null) {
                setCellContent(resData.cov_commence_datetime, index++);
            } else if (content_Str.equals("cov_last_acc_datetime") && resData.cov_last_acc_datetime != null) {
                setCellContent(resData.cov_last_acc_datetime, index++);
            } else if (content_Str.equals("total_attempt") && resData.att_times > 0) {
                setCellContent(resData.att_times, index++);
            } else if (content_Str.equals("cov_total_time") && resData.cov_total_time > 0) {
                setTimeCountCellContent(resData.cov_total_time, index++);
            } else if (content_Str.equals("cov_score") && resData.cov_score > 0) {
                setCellContent(cwUtils.formatNumber(resData.cov_score, 1), index++);
            }else {
            	setCellContent(ExportHelper.NULL_REPLACE, index++);
            }
        }
    }

    /**
     * @param rpt_content
     */
    public void writeTableHead(Vector rpt_content, String cur_lang, UserManagement um) throws WriteException {
        Iterator iter_content = rpt_content.iterator();
        short index = 0;
        Iterator lt = null;
        LabelType labelType;
        getNewRow();
        while (iter_content.hasNext()) {
            String iterStr = (String)iter_content.next();
            if (iterStr.equals("usr_id")) {
                lt = um.getUserProfile().getProfileAttributes().getUserId().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            }

            if (iterStr.equals("usr_display_bil")) {
                lt = um.getUserProfile().getProfileAttributes().getName().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            }

            if (iterStr.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)) {
                lt = um.getUserProfile().getProfileAttributes().getGroup().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            }
            if (iterStr.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR)) {
                lt = um.getUserProfile().getProfileAttributes().getGrade().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            }
            if (iterStr.equals("usr_email")) {
                lt = um.getUserProfile().getProfileAttributes().getEmail().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            }
            if (iterStr.equals("usr_tel_1")) {
                lt = um.getUserProfile().getProfileAttributes().getPhone().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++);
                    }
                }
            }
            if (iterStr.equals("field01")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_item_code"), index++);
            }
            if (iterStr.equals("field02")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_course_title"), index++);
            }
            if (iterStr.equals("itm_type")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_type"), index++);
            }
            if (iterStr.equals("catalog")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_categories"), index++);
            }
            if(iterStr.equals("training_center")) {
            	setCellContent(LangLabel.getValue(cur_lang, "lab_tc"), index++);
            }
            if (iterStr.equals("att_create_timestamp")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_period"), index++);
            }
            if (iterStr.equals("att_status")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_att_status"), index++);
            }
            if (iterStr.equals("att_timestamp")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_att_date"), index++);
            }
            if (iterStr.equals("cov_commence_datetime")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_cov_commence_datetime"), index++);
            }
            if (iterStr.equals("cov_last_acc_datetime")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_cov_last_acc_datetime"), index++);
            }
            if (iterStr.equals("total_attempt")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt"), index++);
            }
            if (iterStr.equals("cov_total_time")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_total_time"), index++);
            }
            if (iterStr.equals("cov_score")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_mod_score"), index++);
            }
        }
    }

    public static String getAtsTitle(String[] ats_id_lst, String cur_lang) {
        StringBuffer status = new StringBuffer();
        if (ats_id_lst.length == 4) {
            status.append(LangLabel.getValue(cur_lang, "lab_all"));
        }
        else {
            for (int i = 0; i < ats_id_lst.length; i++) {
                if (ats_id_lst[i].equalsIgnoreCase("1")) {
                    status.append(LangLabel.getValue(cur_lang, "lab_completed"));
                }
                if (ats_id_lst[i].equalsIgnoreCase("2")) {
                    status.append(LangLabel.getValue(cur_lang, "lab_enroll"));
                }
                if (ats_id_lst[i].equalsIgnoreCase("3")) {
                    status.append(LangLabel.getValue(cur_lang, "lab_incompleted"));
                }
                if (ats_id_lst[i].equalsIgnoreCase("4")) {
                    status.append(LangLabel.getValue(cur_lang, "lab_withdrawn"));
                }
                if (i != ats_id_lst.length - 1) {
                    status.append(", ");
                }
            }
        }
        return status.toString();
    }

    public static String getAtsTitle(long ats_id, String cur_lang) {
        String status = null;
        if (ats_id == 1) {
            status = LangLabel.getValue(cur_lang, "lab_completed");
        }
        if (ats_id == 2) {
            status = LangLabel.getValue(cur_lang, "lab_enroll");
        }
        if (ats_id == 3) {
            status = LangLabel.getValue(cur_lang, "lab_incompleted");
        }
        if (ats_id == 4) {
            status = LangLabel.getValue(cur_lang, "lab_withdrawn");
        }
        if(ats_id <= 0){
        	status = LangLabel.getValue(cur_lang, "94");
        }
        return status.toString();
    }
}
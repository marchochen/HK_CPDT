package com.cw.wizbank.report;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.config.organization.usermanagement.LabelType;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

/**
 * @author jackyx
 * @date 2007-04-11
 */
public class ReportCosRptExporter  extends ExportHelper {
	private static final String SEPCHARACTER = ":";
    public ReportCosRptExporter(String tempDir, String tempDirName, String winName, String encoding, int processUnit, String url) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit, url);
    } 
    
    public void writeCondition(Connection con, ReportCosExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        }
        else {
            reportTitle = getRptTitle(specData.cur_lang, Report.LEARNING_ACTIVITY_COS);
        }
        setTitleText(reportTitle);
        if (specData.itm_id_lst != null && !specData.all_cos_ind) {
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
        } else if (specData.tnd_id_lst != null && !specData.all_cos_ind) {
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
        if(specData.include_no_record) {
        	getNewRow();
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_include_no_record_cos"), ColumnA);
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_yes"), ColumnB);
        } else {
        	getNewRow();
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_include_no_record_cos"), ColumnA);
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_no_1"), ColumnB);        	
        }
        if (specData.ent_id != null && !specData.all_user_ind) {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_lrn"), ColumnA);
            if (specData.staff != null) {
                setCellContent(LangLabel.getValue(specData.cur_lang, specData.staff), ColumnB);                
            }else {
                String ent_name = LearnerRptExporter.getEntName(con, specData.ent_id);
                setCellContent(ent_name, ColumnB);
            }
        } else if(specData.usg_ent_id_lst != null  && !specData.all_user_ind){
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_group"), ColumnA);
            String ent_name = LearnerRptExporter.getEntName(con, specData.usg_ent_id_lst);
            setCellContent(ent_name, ColumnB);
       
        } else {
        	getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_lrn"), ColumnA);
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
                timeStr.append(aeUtils.escNull(simple.format(specData.att_create_start_datetime)));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }
            timeStr.append(" ").append(LangLabel.getValue(specData.cur_lang, "lab_to")).append(" ");
            if (specData.att_create_end_datetime != null) {
                timeStr.append(aeUtils.escNull(simple.format(specData.att_create_end_datetime)));
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
            String status = LearnerRptExportHelper.getAtsTitle(specData.ats_id_lst, specData.cur_lang);
            setCellContent(status, ColumnB);
        }
        getNewRow();
    }
    public void writeSummaryData(ReportCosExporter.resultCosData resCosData, String cur_lang) throws WriteException {
    	setBlankRow(1);
    	setCellContent(LangLabel.getValue(cur_lang,"lab_rpt_sum"), ColumnA, getStyleByName(STYLE_TITLE_TEXT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_cos")+ SEPCHARACTER, ColumnA);
    	setCellContent(resCosData.total_cos, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_lrn")+ SEPCHARACTER, ColumnA);
    	setCellContent(resCosData.total_lrn, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_enroll") + SEPCHARACTER, ColumnA);
    	setCellContent(resCosData.total_enroll, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	for(int i=0; i< resCosData.ats_id_lst.length; i++){
    		getNewRow();
    		setCellContent(LearnerRptExportHelper.getAtsTitle(resCosData.ats_id_lst[i], cur_lang) + SEPCHARACTER, ColumnA);
    		setCellContent(resCosData.ats_id_value[i], ColumnB, getStyleByName(STYLE_BLUE_FONT));
    		setCellContent(resCosData.ats_id_per[i], ColumnC, getStyleByName(STYLE_BLUE_FONT));
    	}
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt_1") + SEPCHARACTER, ColumnA);
    	setCellContent(resCosData.total_attempt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_timespent")+SEPCHARACTER, ColumnA);
    	setCellContent(transTimes(resCosData.times, cur_lang), ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score")+SEPCHARACTER, ColumnA);
    	setCellContent(resCosData.averge_sroce, ColumnB, getStyleByName(STYLE_BLUE_FONT));    		
    }
    public void writeCosTableHead(int[] ats_id_lst, String cur_lang) throws WriteException  {
    	getNewRow();
    	short index = 0;
    	setCellContent(LangLabel.getValue(cur_lang, "lab_item_code"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_item_title"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_enroll"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	for(int i=0; i< ats_id_lst.length; i++){
    		setCellContent(LearnerRptExportHelper.getAtsTitle(ats_id_lst[i], cur_lang), index++,getStyleByName(STYLE_TITLE_TEXT));
    		setCellContent("(%)", index++,getStyleByName(STYLE_TITLE_TEXT));
    	}
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt_1"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_timespent"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score"), index++,getStyleByName(STYLE_TITLE_TEXT));
    }
    public void writeCosData(ReportCosExporter.resultCosData resCosData, String cur_lang) throws WriteException {
    	short index = 0;
    	getNewRow();
    	setCellContent(resCosData.itm_code, index++);
    	setCellContent(resCosData.itm_title, index++);
    	if(resCosData.total_enroll > 0) {
        	setCellContent(resCosData.total_enroll, index++);
        	for(int i=0; i< resCosData.ats_id_lst.length; i++){
        		setCellContent(resCosData.ats_id_value[i], index++);
        		setCellContent(resCosData.ats_id_per[i], index++);
        	}
        	setCellContent(resCosData.total_attempt, index++);
        	setCellContent(transTimes(resCosData.times, cur_lang), index++);
        	setCellContent(resCosData.averge_sroce, index++);    		
    	} else {
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), (short)(index + 6));    		
    	}
    }
    public void writeCosDataHead(ReportCosExporter.resultCosData resCosData, String cur_lang)throws WriteException {
    	setBlankRow(1);
    	setCellContent(resCosData.itm_code +" - " + resCosData.itm_title, ColumnA,getStyleByName(STYLE_TITLE_TEXT));
    	if(resCosData.total_enroll > 0){
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "lab_total_enroll") + SEPCHARACTER, ColumnA);
        	setCellContent(resCosData.total_enroll, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	for(int i=0; i< resCosData.ats_id_lst.length; i++){
        		getNewRow();
        		setCellContent(LearnerRptExportHelper.getAtsTitle(resCosData.ats_id_lst[i], cur_lang) + SEPCHARACTER, ColumnA);
        		setCellContent(resCosData.ats_id_value[i], ColumnB, getStyleByName(STYLE_BLUE_FONT));
        		setCellContent(resCosData.ats_id_per[i], ColumnC, getStyleByName(STYLE_BLUE_FONT));
        	}
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt_1") + SEPCHARACTER, ColumnA);
        	setCellContent(resCosData.total_attempt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "lab_total_timespent")+SEPCHARACTER, ColumnA);
        	setCellContent(transTimes(resCosData.times, cur_lang), ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score")+SEPCHARACTER, ColumnA);
        	setCellContent(resCosData.averge_sroce, ColumnB, getStyleByName(STYLE_BLUE_FONT));    
    	} else {
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), ColumnA);  
    	}
    }
    public static String transTimes(String times, String cur_lang){
    	String trans = null;
    	String day = "0";
    	String time = "00:00:00";
    	if(times != null && times.length() > 0){
    		day = times.substring(0, times.indexOf("#"));
    		time = times.substring(times.indexOf("#") + 1, times.length());
    	}
		trans = day + " " + LangLabel.getValue(cur_lang, "lab_days") + " "  + time;
    	return trans;
    }
	public static String transScore(String score) {
		if(score != null) {
			BigDecimal big = new BigDecimal(score);
			BigDecimal one = new BigDecimal("1.0");
			score =  big.divide(one, 1, BigDecimal.ROUND_HALF_DOWN).toString(); 
		}
		return score;
	}
    public void writeData(ReportCosExporter.resultData resData, Vector rpt_content_vec, String cur_lang) throws WriteException {
        Iterator obj = rpt_content_vec.iterator();
        short index = 0;
        getNewRow();
        while (obj.hasNext()) {
            String content_Str = (String)obj.next();
            if (content_Str.equals("usr_id")) {
                setCellContent(resData.usr_ste_usr_id, index++);
            }
            if (content_Str.equals("usr_display_bil")) {
                setCellContent(resData.usr_display_bil, index++);
            }
            if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)) {
                setCellContent(resData.group_name, index++);
            }
            if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR)) {
                setCellContent(resData.grade_name, index++);
            }
            if (content_Str.equals("usr_email")) {
                setCellContent(resData.usr_email, index++);
            }
            if (content_Str.equals("usr_tel_1")) {
                setCellContent(resData.usr_tel_1, index++);
            }
            if (content_Str.equals("att_create_timestamp")) {
            	if(resData.att_create_timestamp != null) {
                    setCellContent(simple.format(resData.att_create_timestamp), index++);
            	} else {
            		setCellContent(resData.att_create_timestamp, index++);
            	}
            }
            if (content_Str.equals("att_status")) {
                setCellContent(LearnerRptExportHelper.getAtsTitle(resData.att_ats_id, cur_lang), index++);
            }
            if (content_Str.equals("att_timestamp")) {
            	if(resData.att_timestamp != null){
            		setCellContent(simple.format(resData.att_timestamp), index++);
            	} else {
            		setCellContent(resData.att_timestamp, index++);	
            	}
            }
            if (content_Str.equals("cov_commence_datetime")) {
        		setCellContent(resData.cov_commence_datetime, index++);	
            }
            if (content_Str.equals("cov_last_acc_datetime")) {
            	setCellContent(resData.cov_last_acc_datetime, index++);	
            }
            if (content_Str.equals("total_attempt")) {
                setCellContent(resData.totalAttempt, index++);
            }
            if (content_Str.equals("cov_total_time")) {
                setCellContent(dbAiccPath.getTime(resData.cov_total_time), index++);
            }
            if (content_Str.equals("cov_score")) {
                setCellContent(transScore(resData.cov_score), index++);
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
                        setCellContent(labelType.getValue(), index++,getStyleByName(STYLE_TITLE_TEXT));
                    }
                }
            }

            if (iterStr.equals("usr_display_bil")) {
                lt = um.getUserProfile().getProfileAttributes().getName().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++,getStyleByName(STYLE_TITLE_TEXT));
                    }
                }
            }

            if (iterStr.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)) {
                lt = um.getUserProfile().getProfileAttributes().getGroup().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++,getStyleByName(STYLE_TITLE_TEXT));
                    }
                }
            }
            if (iterStr.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR)) {
                lt = um.getUserProfile().getProfileAttributes().getGrade().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++,getStyleByName(STYLE_TITLE_TEXT));
                    }
                }
            }
            if (iterStr.equals("usr_email")) {
                lt = um.getUserProfile().getProfileAttributes().getEmail().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++,getStyleByName(STYLE_TITLE_TEXT));
                    }
                }
            }
            if (iterStr.equals("usr_tel_1")) {
                lt = um.getUserProfile().getProfileAttributes().getPhone().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++,getStyleByName(STYLE_TITLE_TEXT));
                    }
                }
            }
            if (iterStr.equals("att_create_timestamp")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_att_create_timestamp"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            if (iterStr.equals("att_status")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_att_status"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            if (iterStr.equals("att_timestamp")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_att_date"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            if (iterStr.equals("cov_commence_datetime")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_cov_commence_datetime"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            if (iterStr.equals("cov_last_acc_datetime")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_cov_last_acc_datetime"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            if (iterStr.equals("total_attempt")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt_1"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            if (iterStr.equals("cov_total_time")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_total_time"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            if (iterStr.equals("cov_score")) {
                setCellContent(LangLabel.getValue(cur_lang, "lab_score"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
        }
    }




}

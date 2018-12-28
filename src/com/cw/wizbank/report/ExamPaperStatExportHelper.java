package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.config.organization.usermanagement.LabelType;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.report.ViewExamPaperStatReport.ModInfo;
import com.cw.wizbank.report.ViewExamPaperStatReport.SummaryStat;
import com.cw.wizbank.report.ViewExamPaperStatReport.TestInfo;
import com.cw.wizbank.report.ViewExamPaperStatReport.UsrInfo;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

import jxl.write.WriteException;

public class ExamPaperStatExportHelper  extends ExportHelper {
	private static final String SEPCHARACTER = ":";
    public ExamPaperStatExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit);
    } 
    
    public void writeCondition(Connection con, ExamPaperStatExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        }
        else {
            reportTitle = getRptTitle(specData.cur_lang, Report.EXAM_PAPER_STAT);
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
        } else if (specData.mod_id_lst != null) {
        	getNewRow();
        	String res_name = dbResource.getResTitle(con, specData.mod_id_lst);
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_asm"), ColumnA);
            setCellContent(res_name, ColumnB);
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
        if (specData.att_start_datetime != null || specData.att_end_datetime != null) {
            getNewRow();
            StringBuffer timeStr = new StringBuffer();
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_from")).append(" ");
            if (specData.att_start_datetime != null) {
                timeStr.append(aeUtils.escNull(simple.format(specData.att_start_datetime)));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }
            timeStr.append(" ").append(LangLabel.getValue(specData.cur_lang, "lab_to")).append(" ");
            if (specData.att_end_datetime != null) {
                timeStr.append(aeUtils.escNull(simple.format(specData.att_end_datetime)));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }

            setCellContent(LangLabel.getValue(specData.cur_lang, "740"), ColumnA);
            setCellContent(timeStr.toString(), ColumnB);
        }
        getNewRow();
    }
    
    public void writeSummaryData(SummaryStat sumStat, String cur_lang) throws WriteException {
    	setBlankRow(1);
    	setCellContent(LangLabel.getValue(cur_lang,"lab_rpt_sum"), ColumnA, getStyleByName(STYLE_TITLE_TEXT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_cos")+ SEPCHARACTER, ColumnA);
    	setCellContent(sumStat.itmCnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "741")+ SEPCHARACTER, ColumnA);
    	setCellContent(sumStat.tstCnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_lrn") + SEPCHARACTER, ColumnA);
    	setCellContent(sumStat.usrCnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "742") + SEPCHARACTER, ColumnA);
    	setCellContent(sumStat.examineeCnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    }
    
    public void writeExamStatTableHead(String cur_lang) throws WriteException  {
    	getNewRow();
    	short index = 0;
    	setCellContent(LangLabel.getValue(cur_lang, "lab_item_title"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "791"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "743"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "792"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "744"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "745"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "746"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "747"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	
    	setCellContent(LangLabel.getValue(cur_lang, "789") + ViewExamPaperStatReport.LAB_90_100, index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent("(%)", index++,getStyleByName(STYLE_TITLE_TEXT));
    	
    	setCellContent(LangLabel.getValue(cur_lang, "790") + ViewExamPaperStatReport.LAB_80_90, index++, getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent("(%)", index++,getStyleByName(STYLE_TITLE_TEXT));
    	
    	setCellContent(LangLabel.getValue(cur_lang, "lab_normal") + ViewExamPaperStatReport.LAB_70_80, index++, getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent("(%)", index++,getStyleByName(STYLE_TITLE_TEXT));
    	
    	setCellContent(LangLabel.getValue(cur_lang, "lab_passed") + ViewExamPaperStatReport.LAB_60_70, index++, getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent("(%)", index++,getStyleByName(STYLE_TITLE_TEXT));
    	
    	setCellContent(LangLabel.getValue(cur_lang, "lab_failed") + ViewExamPaperStatReport.LAB_0_60, index++, getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent("(%)", index++,getStyleByName(STYLE_TITLE_TEXT));
    }
    
    public void writeExamStatData(TestInfo tstInfo, String cur_lang) throws WriteException {
    	short index = 0;
    	int usr_cnt = tstInfo.usrLst.size();
    	getNewRow();
    	setCellContent(tstInfo.itm_title, index++);
    	setCellContent(tstInfo.res_title, index++);
    	if(usr_cnt > 0) {
        	setCellContent(usr_cnt, index++);
        	setCellContent(tstInfo.mod_max_score, index++);
        	setCellContent(cwUtils.formatNumber(tstInfo.usr_total_score / (float)usr_cnt, 1), index++);
        	setCellContent(tstInfo.usr_max_score, index++);
        	setCellContent(tstInfo.usr_least_score, index++);
        	setCellContent(tstInfo.usr_pass_cnt, index++);
        	setCellContent(cwUtils.formatNumber(tstInfo.usr_pass_cnt / (float)usr_cnt * 100, 1), index++);
        	setCellContent(tstInfo.score_90_100_cnt, index++);
        	setCellContent(cwUtils.formatNumber(tstInfo.score_90_100_cnt / (float)usr_cnt * 100, 1), index++);
        	setCellContent(tstInfo.score_80_90_cnt, index++);
        	setCellContent(cwUtils.formatNumber(tstInfo.score_80_90_cnt / (float)usr_cnt * 100, 1), index++);
        	setCellContent(tstInfo.score_70_80_cnt, index++);
        	setCellContent(cwUtils.formatNumber(tstInfo.score_70_80_cnt / (float)usr_cnt * 100, 1), index++);
        	setCellContent(tstInfo.score_60_70_cnt, index++);
        	setCellContent(cwUtils.formatNumber(tstInfo.score_60_70_cnt / (float)usr_cnt * 100, 1), index++);
        	setCellContent(tstInfo.score_0_60_cnt, index++);
        	setCellContent(cwUtils.formatNumber(tstInfo.score_0_60_cnt / (float)usr_cnt * 100, 1), index++);
    	} else {
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), index, getStyleByName(STYLE_FONT_ALIGNMENT_CENTER));
    		sheet.mergeCells(index, nowRowNum, 16, nowRowNum);
    	}
    }
    
    public void writeExamDataSummary(TestInfo tstInfo, String cur_lang)throws WriteException {
    	int usr_cnt = tstInfo.usrLst.size();
    	setBlankRow(1);
    	setCellContent(tstInfo.itm_title +" - " + tstInfo.res_title, ColumnA,getStyleByName(STYLE_TITLE_TEXT));
    	if(usr_cnt > 0){
    		getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "792") + SEPCHARACTER, ColumnA);
        	setCellContent(tstInfo.mod_max_score, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score") + SEPCHARACTER, ColumnA);
        	setCellContent(cwUtils.formatNumber(tstInfo.usr_total_score / (float)usr_cnt, 1), ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "744") + SEPCHARACTER, ColumnA);
        	setCellContent(tstInfo.usr_max_score, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "745") + SEPCHARACTER, ColumnA);
        	setCellContent(tstInfo.usr_least_score, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "746") + SEPCHARACTER, ColumnA);
        	setCellContent(tstInfo.usr_pass_cnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "1209") + SEPCHARACTER, ColumnA);
        	setCellContent(cwUtils.formatNumber(tstInfo.usr_pass_cnt / (float)usr_cnt * 100, 1), ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	getNewRow();
        	setCellContent(LangLabel.getValue(cur_lang, "743") + SEPCHARACTER, ColumnA);
        	setCellContent(usr_cnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        	
        	getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "1208") + ViewExamPaperStatReport.LAB_90_100 + SEPCHARACTER, ColumnA);
    		setCellContent(tstInfo.score_90_100_cnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    		setCellContent(cwUtils.formatNumber(tstInfo.score_90_100_cnt / (float)usr_cnt * 100, 1), ColumnC, getStyleByName(STYLE_BLUE_FONT));
    		
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "1208") + ViewExamPaperStatReport.LAB_80_90 + SEPCHARACTER, ColumnA);
    		setCellContent(tstInfo.score_80_90_cnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    		setCellContent(cwUtils.formatNumber(tstInfo.score_80_90_cnt / (float)usr_cnt * 100, 1), ColumnC, getStyleByName(STYLE_BLUE_FONT));
    		
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "1208") + ViewExamPaperStatReport.LAB_70_80 + SEPCHARACTER, ColumnA);
    		setCellContent(tstInfo.score_70_80_cnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    		setCellContent(cwUtils.formatNumber(tstInfo.score_70_80_cnt / (float)usr_cnt * 100, 1), ColumnC, getStyleByName(STYLE_BLUE_FONT));
    		
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "1208") + ViewExamPaperStatReport.LAB_60_70 + SEPCHARACTER, ColumnA);
    		setCellContent(tstInfo.score_60_70_cnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    		setCellContent(cwUtils.formatNumber(tstInfo.score_60_70_cnt / (float)usr_cnt * 100, 1), ColumnC, getStyleByName(STYLE_BLUE_FONT));
    		
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "1208") + ViewExamPaperStatReport.LAB_0_60 + SEPCHARACTER, ColumnA);
    		setCellContent(tstInfo.score_0_60_cnt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
    		setCellContent(cwUtils.formatNumber(tstInfo.score_0_60_cnt / (float)usr_cnt * 100, 1), ColumnC, getStyleByName(STYLE_BLUE_FONT));
    		
    	} else {
    		getNewRow();
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), ColumnA, getStyleByName(STYLE_FONT_ALIGNMENT_CENTER));
    		sheet.mergeCells(ColumnA, nowRowNum, ColumnB, nowRowNum);
    	}
    }
    
    public void writeUsrData(ExamPaperStatExportHelper rptBuilder, long mod_res_id, UsrInfo usrInfo, Vector rpt_content_vec, String cur_lang) throws WriteException {
        Iterator obj = rpt_content_vec.iterator();
        short index = 0;
//        getNewRow();
        String content_Str = null;
        int num = 1;
        for(Entry<String, ModInfo> mod : usrInfo.modHash.entrySet()){
        	ModInfo modInfo = mod.getValue();
        	if(mod_res_id == modInfo.mov_mod_id){
        			getNewRow();
        			index = 0;
		        while (obj.hasNext()) {
		            content_Str = (String)obj.next();
	//	        }
		            if (content_Str.equals("usr_id")) {
		                setCellContent(usrInfo.usr_ste_usr_id, index++);
		            }
		            if (content_Str.equals("usr_display_bil")) {
		                setCellContent(usrInfo.usr_display_bil, index++);
		            }
		            if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_PARENT_USG)) {
		                setCellContent(usrInfo.usg_display_bil, index++);
		            }
		            if (content_Str.equals(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR)) {
		                setCellContent(usrInfo.ugr_display_bil, index++);
		            }
		            if (content_Str.equals("usr_email")) {
		                setCellContent(usrInfo.usr_email, index++);
		            }
		            if (content_Str.equals("usr_tel_1")) {
		                setCellContent(usrInfo.usr_tel_1, index++);
		            }
		            if (content_Str.equals("usr_competency")) {
		                setCellContent(usrInfo.sks_title, index++);
		            }
		            if (content_Str.equals("usr_extra_2")) {
		                setCellContent(usrInfo.usr_extra_2, index++);
		            }
		        }
		        setCellContent(modInfo.start_visit_time, index++);	
		        setCellContent(modInfo.last_visit_time, index++);	
		        setCellContent(modInfo.mov_total_attempt, index++);
		        if (modInfo.is_pass) {
		        	setCellContent(LangLabel.getValue(cur_lang, "lab_yes"), index++);
		        } else {
		        	setCellContent(LangLabel.getValue(cur_lang, "lab_no_1"), index++);
		        }
		        setCellContent(modInfo.mov_score, index++);
        		
        	}
        }
    }
    /**
     * @param rpt_content
     */
    public void writeUsrTableHead(Vector rpt_content, String cur_lang, UserManagement um) throws WriteException {
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
            
            if (iterStr.equals("usr_competency")) {
                lt = um.getUserProfile().getProfileAttributes().getCompetency().getLabel().iterator();
                while (lt.hasNext()) {
                    labelType = (LabelType)lt.next();
                    if (labelType.getLang().equals(cur_lang)) {
                        setCellContent(labelType.getValue(), index++,getStyleByName(STYLE_TITLE_TEXT));
                    }
                }
            }
        }
        setCellContent(LangLabel.getValue(cur_lang, "label_core_cpt_d_management_231"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_cov_commence_datetime"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_cov_last_acc_datetime"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt_1"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "750"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_score"), index++,getStyleByName(STYLE_TITLE_TEXT));
    }




}

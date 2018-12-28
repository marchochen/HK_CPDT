package com.cw.wizbank.report;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.config.organization.usermanagement.LabelType;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.report.ReportLrnExporter.resultData;
import com.cw.wizbank.report.ReportLrnExporter.resultLrnData;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

/**
 * @author jackyx
 * @date 2007-04-11
 */
public class ReportLrnRptExporter  extends ExportHelper {
	private static final String SEPCHARACTER = ":";
    public ReportLrnRptExporter(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit);
    } 

    public void writeCondition(Connection con, ReportLrnExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        }
        else {
            reportTitle = getRptTitle(specData.cur_lang, Report.LEARNING_ACTIVITY_LRN);
        }
        setTitleText(reportTitle);
        if (specData.ent_id != null && !specData.all_user_ind) {
            getNewRow();
            setCellContent(LangLabel.getValue(specData.cur_lang, "lab_lrn"), ColumnA);
            if (specData.staff != null) {
                setCellContent(LangLabel.getValue(specData.cur_lang, specData.staff), ColumnB);                
            }else {
                String ent_name = LearnerRptExporter.getEntName(con, specData.ent_id);
                setCellContent(ent_name, ColumnB);
            }
        } else if(specData.usg_ent_id_lst != null && !specData.all_user_ind ){
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
        if(specData.include_no_record) {
        	getNewRow();
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_include_no_record_lrn"), ColumnA);
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_yes"), ColumnB);
        } else {
        	getNewRow();
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_include_no_record_lrn"), ColumnA);
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_no_1"), ColumnB);        	
        }


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
    public void writeSummaryData(ReportLrnExporter.resultLrnData resCosData, String cur_lang) throws WriteException {
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
    	setCellContent(ReportCosRptExporter.transTimes(resCosData.times, cur_lang), ColumnB, getStyleByName(STYLE_BLUE_FONT));
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score")+SEPCHARACTER, ColumnA);
    	setCellContent(resCosData.averge_sroce, ColumnB, getStyleByName(STYLE_BLUE_FONT));    		
    }
    public void writeLrnTableHead(int[] ats_id_lst, String cur_lang) throws WriteException  {
    	getNewRow();
    	short index = 0;
    	setCellContent(LangLabel.getValue(cur_lang, "lab_user_id"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_name"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_enroll"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	for(int i=0; i< ats_id_lst.length; i++){
    		setCellContent(LearnerRptExportHelper.getAtsTitle(ats_id_lst[i], cur_lang), index++,getStyleByName(STYLE_TITLE_TEXT));
    		setCellContent("(%)", index++,getStyleByName(5));
    	}
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt_1"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_total_timespent"), index++,getStyleByName(STYLE_TITLE_TEXT));
    	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score"), index++,getStyleByName(STYLE_TITLE_TEXT));
    }
    public void writeLrnData(ReportLrnExporter.resultLrnData resCosData, String cur_lang) throws WriteException {
    	short index = 0;
    	getNewRow();
    	setCellContent(resCosData.usr_ste_usr_id, index++);
    	setCellContent(resCosData.usr_display_bil, index++);
    	if(resCosData.total_enroll > 0) {
        	setCellContent(resCosData.total_enroll, index++);
        	for(int i=0; i< resCosData.ats_id_lst.length; i++){
        		setCellContent(resCosData.ats_id_value[i], index++);
        		setCellContent(resCosData.ats_id_per[i], index++);
        	}
        	setCellContent(resCosData.total_attempt, index++);
        	setCellContent(ReportCosRptExporter.transTimes(resCosData.times, cur_lang), index++);
        	setCellContent(resCosData.averge_sroce, index++);    		
    	} else {
    		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), (short)(index + 6));    		
    	}
    }
    public int writeLrnDataHead(Vector<resultLrnData> sum_vec, String cur_lang,boolean export_stat_only)throws WriteException, cwException {
    	
    	String usr_ste_usr_id = "";
    	String usr_display_bil = "";
    	int total_enroll = 0;
    	float total_time = 0;
    	float total_sroce = 0;
    	int total_attempt = 0;
    	int attempts_user = 0;
    	String averge_sroce = "";
    	String times = "";
    	int status_1 = 0;
    	int status_2 = 0;
    	int status_3 = 0;
    	int status_4 = 0;
    	StatisticBean sb = new StatisticBean();
    	for (int i = 0; i < sum_vec.size(); i++) {
    		resultLrnData sum_data = sum_vec.elementAt(i);
    		usr_ste_usr_id = sum_data.usr_ste_usr_id;
    		usr_display_bil = sum_data.usr_display_bil;
    		total_time += sum_data.total_time;
    		total_sroce += sum_data.total_sroce;
    		total_attempt += sum_data.total_attempt;
    		attempts_user += sum_data.attempts_user;
    		sb.setTotalTimeSpent(total_time);
    		times = sb.transTime();
    		if(sum_data.att_ats_id > 0){
    			total_enroll += 1;
    		}
    		if(sum_data.att_ats_id == 1){
    			status_1 ++;
    		}else if (sum_data.att_ats_id == 2) {
    			status_2 ++;
			}else if (sum_data.att_ats_id == 3) {
				status_3 ++;
			}else if (sum_data.att_ats_id == 4) {
				status_4 ++;
			}
		}
    
    	if(export_stat_only){
    		 getNewRow();
    		short index = 0;
        	setCellContent(usr_ste_usr_id, index++);
        	setCellContent(usr_display_bil, index++);
        	if(total_enroll > 0) {
        		averge_sroce = getAverScore(total_enroll,total_sroce);
            	setCellContent(total_enroll, index++);
            	setCellContent(status_1, index++);
        		setCellContent(getEnrolledPerc(total_enroll,status_1),index++);
        		setCellContent(status_2, index++);
        		setCellContent(getEnrolledPerc(total_enroll,status_2),index++);
        		setCellContent(status_3, index++);
        		setCellContent(getEnrolledPerc(total_enroll,status_3),index++);
        		setCellContent(status_4, index++);
        		setCellContent(getEnrolledPerc(total_enroll,status_4),index++);
            	setCellContent(total_attempt, index++);
            	setCellContent(ReportCosRptExporter.transTimes(times, cur_lang), index++);
            	setCellContent(averge_sroce, index++);    		
        	} else {
        		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), (short)(index + 6));    		
        	}
    	}else {
    		setBlankRow(1);
    		setCellContent(usr_ste_usr_id +" - " + usr_display_bil, ColumnA,getStyleByName(STYLE_TITLE_TEXT));
        	if(total_enroll > 0){
        		averge_sroce = getAverScore(total_enroll,total_sroce);
            	getNewRow();
            	setCellContent(LangLabel.getValue(cur_lang, "lab_total_enroll") + SEPCHARACTER, ColumnA);
            	setCellContent(total_enroll, ColumnB, getStyleByName(STYLE_BLUE_FONT));

            	getNewRow();
        		setCellContent(LangLabel.getValue(cur_lang, "lab_completed") + SEPCHARACTER, ColumnA);
        		setCellContent(status_1, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        		setCellContent(getEnrolledPerc(total_enroll,status_1), ColumnC, getStyleByName(STYLE_BLUE_FONT));
        		getNewRow();
        		setCellContent(LangLabel.getValue(cur_lang, "lab_enroll") + SEPCHARACTER, ColumnA);
        		setCellContent(status_2, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        		setCellContent(getEnrolledPerc(total_enroll,status_2), ColumnC, getStyleByName(STYLE_BLUE_FONT));
        		getNewRow();
        		setCellContent(LangLabel.getValue(cur_lang, "lab_incompleted") + SEPCHARACTER, ColumnA);
        		setCellContent(status_3, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        		setCellContent(getEnrolledPerc(total_enroll,status_3), ColumnC, getStyleByName(STYLE_BLUE_FONT));
        		getNewRow();
        		setCellContent(LangLabel.getValue(cur_lang, "lab_withdrawn") + SEPCHARACTER, ColumnA);
        		setCellContent(status_4, ColumnB, getStyleByName(STYLE_BLUE_FONT));
        		setCellContent(getEnrolledPerc(total_enroll,status_4), ColumnC, getStyleByName(STYLE_BLUE_FONT));
        		
            	getNewRow();
            	setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt_1") + SEPCHARACTER, ColumnA);
            	setCellContent(total_attempt, ColumnB, getStyleByName(STYLE_BLUE_FONT));
            	getNewRow();
            	setCellContent(LangLabel.getValue(cur_lang, "lab_total_timespent")+SEPCHARACTER, ColumnA);
            	setCellContent(ReportCosRptExporter.transTimes(times, cur_lang), ColumnB, getStyleByName(STYLE_BLUE_FONT));
            	getNewRow();
            	setCellContent(LangLabel.getValue(cur_lang, "lab_ave_score")+SEPCHARACTER, ColumnA);
            	setCellContent(averge_sroce, ColumnB, getStyleByName(STYLE_BLUE_FONT));    
        	} else {
        		getNewRow();
        		setCellContent(LangLabel.getValue(cur_lang, "lab_no_record"), ColumnA);  
        	}
		}
    	
    	return total_enroll;
    }
    
    public void writeData(Vector<resultData> vec_usr,String cur_lang) throws WriteException {
      
        for (int i = 0; i < vec_usr.size(); i++) {
        	resultData resData = vec_usr.elementAt(i);
			getNewRow();
			short index = 0;
			setCellContent(resData.t_code, index++);
			setCellContent(resData.t_title, index++);
			setCellContent(aeItemDummyType.getItemLabelByDummyType(cur_lang, resData.itm_dummy_type), index++);
			setCellContent(resData.catNav, index++);
			setCellContent(resData.tcr_title, index++);
			if (resData.att_create_timestamp != null) {
				setCellContent(simple.format(resData.att_create_timestamp), index++);
			} else {
				setCellContent(resData.att_create_timestamp, index++);
			}
			setCellContent(LearnerRptExportHelper.getAtsTitle(resData.att_ats_id, cur_lang), index++);
			if(resData.att_timestamp != null){
        		setCellContent(simple.format(resData.att_timestamp), index++);
        	} else {
        		setCellContent(resData.att_timestamp, index++);	
        	}
			setCellContent(resData.cov_commence_datetime, index++);	
			setCellContent(resData.cov_last_acc_datetime, index++);	
			setCellContent(resData.totalAttempt, index++);
			setCellContent(dbAiccPath.getTime(resData.cov_total_time), index++);
			 setCellContent(ReportCosRptExporter.transScore(resData.cov_score), index++);
		}
    }

    /**
     * @param rpt_content
     */
    public void writeTableHead(String cur_lang) throws WriteException {
        short index = 0;
        getNewRow();
        
        setCellContent(LangLabel.getValue(cur_lang, "lab_course_code"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_course_title"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_type"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_categories"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_tc"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_att_create_timestamp"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_att_status"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_att_date"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_cov_commence_datetime"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_cov_last_acc_datetime"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_total_attempt"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_total_time"), index++,getStyleByName(STYLE_TITLE_TEXT));
        setCellContent(LangLabel.getValue(cur_lang, "lab_score"), index++,getStyleByName(STYLE_TITLE_TEXT));
    }
    public String getEnrolledPerc(int totalEnroll,int record) throws cwException {
		String s = "";
		if (totalEnroll == 0) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#0.0%");
		try {
			
			BigDecimal b1 = new BigDecimal(Integer.toString(record * 100));
			BigDecimal b2 = new BigDecimal(Integer.toString(totalEnroll));
			s = b1.divide(b2, 1, BigDecimal.ROUND_HALF_UP).toString();

		} catch (Exception e) {
			throw new cwException(e.getMessage());
		}
		return s;
	}
    
    public String getAverScore(int totalLrn,float totalScore) throws cwException {
		String s = "";
		if (totalLrn == 0 || totalScore == 0) {
			return "0";
		}

		BigDecimal tScore = new BigDecimal(totalScore);
		BigDecimal tLrn = new BigDecimal(totalLrn);
		try {
			s = tScore.divide(tLrn, 1, BigDecimal.ROUND_HALF_UP).toString();
		} catch (Exception e) {
			throw new cwException(e.getMessage());
		}
		return s;
	}
}

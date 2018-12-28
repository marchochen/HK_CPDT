package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class TrainCostStatExportHelper  extends ExportHelper {
	private static final String EMPTY = "";
	
    public TrainCostStatExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit);
    } 
    
    public int writeCondition(Connection con, ExamPaperStatExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        }
        else {
            reportTitle = getRptTitle(specData.cur_lang, Report.TRAIN_FEE_STAT);
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
        } else {
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
        
        if (specData.start_datetime != null || specData.end_datetime != null) {
            getNewRow();
            StringBuffer timeStr = new StringBuffer();
            timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_from")).append(" ");
            if (specData.start_datetime != null) {
                timeStr.append(aeUtils.escNull(simple.format(specData.start_datetime)));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }
            timeStr.append(" ").append(LangLabel.getValue(specData.cur_lang, "lab_to")).append(" ");
            if (specData.end_datetime != null) {
                timeStr.append(aeUtils.escNull(simple.format(specData.end_datetime)));
            }
            else {
                timeStr.append(LangLabel.getValue(specData.cur_lang, "lab_na"));
            }

            setCellContent(LangLabel.getValue(specData.cur_lang, "711"), ColumnA);
            setCellContent(timeStr.toString(), ColumnB);
        }
        getNewRow();
        setCellContent(LangLabel.getValue(specData.cur_lang, "730"), ColumnA);
        //记录总培训费用的行数，统计之后再把值写进来
        int row = nowRowNum;
        getNewRow();
        return row;
    }
 
    public void writeTableHead(List rpt_content, String cur_lang) throws WriteException  {
    	Iterator iter_content = rpt_content.iterator();
        short index = 0;
        getNewRow();
        String iterStr = null;
        while (iter_content.hasNext()) {
            iterStr = (String)iter_content.next();
            if (iterStr.equals("p_itm_code")) {
            	setCellContent(LangLabel.getValue(cur_lang,"804"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }

            if (iterStr.equals("p_itm_title")) {
            	setCellContent(LangLabel.getValue(cur_lang,"562"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            
            if (iterStr.equals("c_itm_code")) {
            	setCellContent(LangLabel.getValue(cur_lang,"lab_c_code"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            
            if (iterStr.equals("c_itm_title")) {
            	setCellContent(LangLabel.getValue(cur_lang,"lab_c_title"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            
            if (iterStr.equals("itm_cost_budget")) {
            	setCellContent(LangLabel.getValue(cur_lang,"718"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            
            if (iterStr.equals("itm_cost_actual")) {
            	setCellContent(LangLabel.getValue(cur_lang,"719"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            
            if (iterStr.equals("itm_cost_exec_rate")) {
            	setCellContent(LangLabel.getValue(cur_lang,"720"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            
            if (iterStr.equals("no_of_training_attend")) {
            	setCellContent(LangLabel.getValue(cur_lang,"731"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
            
            if (iterStr.equals("charge_per_head")) {
            	setCellContent(LangLabel.getValue(cur_lang,"732"), index++,getStyleByName(STYLE_TITLE_TEXT));
            }
        }
    }
    
    public void writeData(List rpt_content, String cur_lang, ResultSet rs, double actual, int usrCnt) throws RowsExceededException, WriteException, SQLException {
    	Iterator obj = rpt_content.iterator();
        short index = 0;
        getNewRow();
        String content_Str = null;
        String type = rs.getString("itm_type");
        while (obj.hasNext()) {
            content_Str = (String)obj.next();
            if (content_Str.equals("p_itm_code")) {
            	if (type.equals(aeItem.ITM_TYPE_SELFSTUDY) ||type.equals(aeItem.ITM_TYPE_VIDEO)) {
            		setCellContent(rs.getString("c_itm_code"), index++);
            	} else {
            		setCellContent(rs.getString("p_itm_code"), index++);
            	}
            }

            if (content_Str.equals("p_itm_title")) {
            	if (type.equals(aeItem.ITM_TYPE_SELFSTUDY) ||type.equals(aeItem.ITM_TYPE_VIDEO)) {
            		setCellContent(rs.getString("c_itm_title"), index++);
            	} else {
            		setCellContent(rs.getString("p_itm_title"), index++);
            	}
            }
            
            if (content_Str.equals("c_itm_code")) {
            	if (type.equals(aeItem.ITM_TYPE_SELFSTUDY) ||type.equals(aeItem.ITM_TYPE_VIDEO)) {
            		setCellContent(EMPTY, index++);
            	} else {
            		setCellContent(rs.getString("c_itm_code"), index++);
            	}
            }
            
            if (content_Str.equals("c_itm_title")) {
            	if (type.equals(aeItem.ITM_TYPE_SELFSTUDY) ||type.equals(aeItem.ITM_TYPE_VIDEO)) {
            		setCellContent(EMPTY, index++);
            	} else {
            		setCellContent(rs.getString("c_itm_title"), index++);
            	}
            }
            
    		double budget = rs.getDouble("budget");
            
            if (content_Str.equals("itm_cost_budget")) {
            	setCostValue(budget, index++);
            }
            
            if (content_Str.equals("itm_cost_actual")) {
            	setCostValue(actual, index++);
            }
            
            if (content_Str.equals("itm_cost_exec_rate")) {
            	setRateValue(actual, budget, index++);
            }
            
            if (content_Str.equals("no_of_training_attend")) {
            	if (usrCnt > 0) {
            		setCellContent(usrCnt, index++);
            	} else {
            		setCellContent(EMPTY, index++);
            	}
            }
            
            if (content_Str.equals("charge_per_head")) {
            	if (usrCnt > 0) {
            		setCostValue(actual / (double)usrCnt, index++);
            	} else {
            		setCellContent(EMPTY, index++);
            	}
            }
        }
    }
    
    public void setRateValue(double actual, double budget, short cellNum) throws RowsExceededException, WriteException {
    	if (budget == 0) {
    		setCellContent(EMPTY, cellNum);
    	} else {
    		setCellContent(TrainFeeStatReport.nf.format(actual / budget), cellNum);
    	}
    }
    
    public void setCostValue(double value, short cellNum) throws RowsExceededException, WriteException {
    	if (value > 0) {
    		setCellContent(TrainFeeStatReport.df.format(value), cellNum);
    	} else {
    		setCellContent(EMPTY, cellNum);
    	}
    }
}

package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.report.FMFeeReport.FacilityInfo;
import com.cw.wizbank.report.FMFeeReport.ReservationInfo;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class FMFeeExportHelper  extends ExportHelper {
	private static final String EMPTY = "";
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	
    public FMFeeExportHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit);
    } 
    
    public void writeCondition(Connection con, ExamPaperStatExporter.SpecData specData) throws IOException, WriteException, SQLException, cwSysMessage {
        String reportTitle = null;
        if (specData.rsp_title != null) {
            reportTitle = specData.rsp_title;
        }
        else {
            reportTitle = getRptTitle(specData.cur_lang, Report.FM_FEE);
        }
        setTitleText(reportTitle);
        
        getNewRow();
        setCellContent(LangLabel.getValue(specData.cur_lang, "807"), ColumnA);
        if (specData.fac_type > 0) {
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_ftp_" + specData.fac_type), ColumnB);
        } else {
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_all"), ColumnB);
        }
        
        if (specData.tcr_id > 0) {
        	getNewRow();
        	setCellContent(LangLabel.getValue(specData.cur_lang, "lab_tc"), ColumnA);
            setCellContent(DbTrainingCenter.getTcrTitle(con, specData.tcr_id), ColumnB);
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

            setCellContent(LangLabel.getValue(specData.cur_lang, "806"), ColumnA);
            setCellContent(timeStr.toString(), ColumnB);
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
        getNewRow();
    }
 
    public void writeTableHead(String cur_lang) throws WriteException  {
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang, "738"), (short)5, getStyleByName(STYLE_BOLD_FONT));
    	sheet.mergeCells(5, nowRowNum, 7, nowRowNum);
    	short index = 0;
    	getNewRow();
    	setCellContent(LangLabel.getValue(cur_lang,"lab_ftn_ACT_LINK"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"395"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"735"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"736"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"737"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"129"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"593"), index++, getStyleByName(STYLE_BOLD_FONT));
    	setCellContent(LangLabel.getValue(cur_lang,"805"), index++, getStyleByName(STYLE_BOLD_FONT));
    }
    
    public void writeData(String rsv_purpose, String usr_display_bil, int rsv_participant_no, ReservationInfo resInfo, String cur_lang) throws RowsExceededException, WriteException, SQLException {
        short index = 0;
        getNewRow();
        setCellContent(rsv_purpose, index++);
        setCellContent(simple.format(resInfo.fsh_date), index++);
        setCellContent(usr_display_bil, index++);
        setCellContent(rsv_participant_no, index++);
        setCostValue(resInfo.total_fee, index++);
        
        FacilityInfo facInfo = null;
        int i, size = resInfo.facilityLst.size();
        
        for (i = 0; i < size; i++) {
        	facInfo = (FacilityInfo)resInfo.facilityLst.get(i);
        	if (i > 0) {
        		getNewRow();
        	}
        	setCellContent(sdf.format(facInfo.fsh_start_time) + " - " + sdf.format(facInfo.fsh_end_time), index);
            setCellContent(facInfo.fac_title, (short)(index + 1));
            setCostValue(facInfo.fac_fee, (short)(index + 2));
        }
        getNewRow();
    }
    
    public void setCostValue(double value, short cellNum) throws RowsExceededException, WriteException {
    	if (value > 0) {
    		setCellContent(TrainFeeStatReport.df.format(value), cellNum);
    	} else {
    		setCellContent(EMPTY, cellNum);
    	}
    }
}

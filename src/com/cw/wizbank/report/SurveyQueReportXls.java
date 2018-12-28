/*
 * Created on 2005-7-5
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.cw.wizbank.ae.db.view.ViewSurveyQueReport;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author Dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SurveyQueReportXls extends ReportXlsFileCreator {
    Connection con;
    ViewSurveyQueReport viewSuvQueRpt;
    SurveyQueReport.SurveyQueRptParam param;

    /**
     * @param incon            Connection
     */
    public SurveyQueReportXls(Connection incon, SurveyQueReport.SurveyQueRptParam surveyParam) {
        viewSuvQueRpt = new ViewSurveyQueReport(incon);
        con = incon;
        param = surveyParam;
        xlsFilePath = param.xlsFilePath;
        columnWidth = new int[] { 5000, 10000, 3000, 3000, 3000, 3000, 3000, 3000 };
    }

    public String outputSurveyQueReport(String userItem, String userSpecifiedItem, double responseCount, double enrolledCount, double responseRate, ArrayList dataList, boolean tc_enabled) throws SQLException, FileNotFoundException, qdbErrMessage, IOException, cwException {
        try {
            File xlsFile = new File(param.xlsFilePath);
            if (!xlsFile.exists()) {
                xlsFile.mkdirs();
            }
            wb = Workbook.createWorkbook(new File(param.xlsFilePath + cwUtils.SLASH + OutputFileName + ".xls"));
            sheet = wb.createSheet("First Sheet", 0);
            setColumnWidth(columnWidth);
            wbSetting = new WorkbookSettings();
            wbSetting.setEncoding(param.encoding);
            wbSetting.setGCDisabled(true);
            
            //set report title
            String reportTitle = null;
            if (param.rsp_title != null) {
                reportTitle = param.rsp_title;
            }
            else {
                reportTitle = ExportHelper.getRptTitle(param.cur_lan, SurveyReport.RTE_TYPE_SURVEY_QUE_GRP);
            }
            setTitleText(reportTitle);
    
            //write the head of report to workbook
            ViewSurveyQueReport.HeadData headData = viewSuvQueRpt.getRptHead(userItem, param);
            setRptHeadData(headData, param, tc_enabled);
            setBlankRow(ColumnH);
            //write report summary
            setReportSum(param, responseCount, enrolledCount, responseRate);
            //write report question summary
            setQueSum(param, dataList);
            //write report detail to workbook
            setTitleText(LangLabel.getValue(param.cur_lan, "lab_que_sta"));
            setReportDetail(userSpecifiedItem, param, dataList);
            String outputName = null;
            setColumnWidth(columnWidth);
            wb.write();
            wb.close();
            if (fileCount > 1) {
                zipXlsRpt(param.xlsFilePath);
                outputName = OutputFileName + ".zip";
            }
            else {
                outputName = OutputFileName + ".xls";
            }
            return outputName;
        }
        catch (RowsExceededException e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }
        catch (WriteException e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }
    }

    /*
     * get the detail data of report,and set it to workbook
     */
    private void setReportDetail(String userSpecifiedItem, SurveyQueReport.SurveyQueRptParam param, ArrayList dataList) throws SQLException, IOException, RowsExceededException, WriteException {
        Iterator obj = dataList.iterator();
        while (obj.hasNext()) {
            SurveyQueData gx = (SurveyQueData)obj.next();
            if (gx.queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
                int startRowNum = 0;
                int endRowNum = 0;
                //write que top line
                getNewRow();
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_no"), ColumnA, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_que"), ColumnB, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
                sheet.mergeCells(ColumnC, nowRowNum, ColumnE, nowRowNum);
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_choice"), ColumnC, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_score"), ColumnF, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_count"), ColumnG, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_per"), ColumnH, getStyleByName(STYLE_TITLE_WITH_FILLBACK));

                getNewRow();
                setCellContent((int)gx.order, ColumnA, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
                if(null != gx.que_text && gx.que_text != ""){
                	setCellContent(gx.que_text, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
                }else{
                	setCellContent(gx.res_title, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
                }

                for (int i = 0; i < gx.condition.length; i++) {
                    if (i != 0) {
                        getNewRow();
                    }
                    setCellContent(gx.condition_text[i], ColumnC, getStyleByName(STYLE_QUESTION_AND_ANSWER));
                    sheet.mergeCells(ColumnC, nowRowNum, ColumnE, nowRowNum);
                    setCellContent(gx.score[i], ColumnF);
                    setCellContent(gx.conditionCount[i], ColumnG);
                    setCellContent(SurveyQueReport.formatNum(gx.percentage[i], SurveyQueReport.DEC_POINT), ColumnH);
                }
            }
            if (gx.queType.equals(dbQuestion.QUE_TYPE_FILLBLANK)) {
                getNewRow();
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_no"), ColumnA, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_que"), ColumnB, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
                sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_ans"), ColumnC, getStyleByName(STYLE_TITLE_WITH_FILLBACK));

                getNewRow();
                setCellContent((int)gx.order, ColumnA, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
                if(null != gx.que_text && gx.que_text != ""){
                	setCellContent(gx.que_text, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
                }else{
                	setCellContent(gx.res_title, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
                }
                param.que_id = gx.order;
                int resultNull = viewSuvQueRpt.getXslFBAns(param, userSpecifiedItem, (int)gx.order, this);
                if (resultNull == 0) {
                    setCellContent("", ColumnC);
                    sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
                }
            }
        }
    }

    private void setQueSum(SurveyQueReport.SurveyQueRptParam param, ArrayList dataList) throws RowsExceededException, WriteException {
        getNewRow();
        WritableCellFormat cf = getStyleByName(STYLE_TITLE_WITH_FILLBACK);
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_no"), ColumnA, cf);
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_que_rep_title"), ColumnB, cf);
        sheet.mergeCells(ColumnC, nowRowNum, ColumnD, nowRowNum);
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_total"), ColumnC, cf);
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_avg_score"), ColumnE, cf);
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_sd"), ColumnF, cf);
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_answered"), ColumnG, cf);
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_unanswered"), ColumnH, cf);

        Iterator obj = dataList.iterator();
        while (obj.hasNext()) {
            SurveyQueData gx = (SurveyQueData)obj.next();
            getNewRow();
            setCellContent(gx.order, ColumnA, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
            
            if(null != gx.que_text && gx.que_text != ""){
            	setCellContent(gx.que_text, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
            }else{
            	setCellContent(gx.res_title, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
            }
            setCellContent(gx.totalResponseCount, ColumnC);
            sheet.mergeCells(ColumnC, nowRowNum, ColumnD, nowRowNum);
            
            if (gx.queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
                double avScore = SurveyQueReport.formatNum(gx.avScore, SurveyQueReport.DEC_POINT);
                double stDeviation = SurveyQueReport.formatNum(gx.stDeviation, SurveyQueReport.DEC_POINT);
                setCellContent(avScore, ColumnE);
                setCellContent(stDeviation, ColumnF);
            }
            else {
                String avScore = "--";
                String stDeviation = "--";
                setCellContent(avScore, ColumnE, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
                setCellContent(stDeviation, ColumnF, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
            }
            setCellContent(SurveyQueReport.formatNum(gx.answered, SurveyQueReport.DEC_POINT), ColumnG);
            setCellContent(SurveyQueReport.formatNum(gx.unanswered, SurveyQueReport.DEC_POINT), ColumnH);
        }
    }

    private void setReportSum(SurveyQueReport.SurveyQueRptParam param, double responseCount, double enrolledCount, double responseRate) throws cwException, WriteException {
        setTitleText(LangLabel.getValue(param.cur_lan, "lab_rpt_sum"));
        getNewRow();
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_enr_count"), ColumnA);
        setCellContent(enrolledCount, ColumnB);
//        setCellContent("", ColumnC);
        sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
        getNewRow();
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_res_count"), ColumnA);
        setCellContent(responseCount, ColumnB);
//        setCellContent("", ColumnC);
        sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
        getNewRow();
        setCellContent(LangLabel.getValue(param.cur_lan, "lab_res_rate"), ColumnA);
        setCellContent(SurveyQueReport.formatNum(responseRate, SurveyQueReport.DEC_POINT), ColumnB);
//        setCellContent("", ColumnC);
        sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
    }

    /*
     * set report head data from a hashMap to workbook
     */
    public void setRptHeadData(ViewSurveyQueReport.HeadData headData, SurveyQueReport.SurveyQueRptParam param, boolean tc_enabled) throws cwException, RowsExceededException, WriteException {
        Iterator obj;
        if (!headData.cos_catalog.isEmpty()) {
            getNewRow();
            StringBuffer cos_catalog = new StringBuffer();
            obj = headData.cos_catalog.iterator();
            while (obj.hasNext()) {
                cos_catalog.append((String)obj.next());
                if (obj.hasNext()) {
                    cos_catalog.append(", ");
                }
            }
            setCellContent(LangLabel.getValue(param.cur_lan, "lab_cos_catalog"), ColumnA);
            setCellContent(cos_catalog.toString(), ColumnB);
            sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
        }
        if(tc_enabled) {
            if(param.all_cos_ind) {
                getNewRow();
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_cos"), ColumnA);
                if(param.answer_for_course && !param.answer_for_lrn_course) {
                	setCellContent(LangLabel.getValue(param.cur_lan, "lab_answer_for_course"), ColumnB);	
                } else if (!param.answer_for_course && param.answer_for_lrn_course){
                	setCellContent(LangLabel.getValue(param.cur_lan, "lab_answer_for_lrn_course"), ColumnB);
                } else {
                    setCellContent(LangLabel.getValue(param.cur_lan, "lab_all_cos"), ColumnB);	
                }            	
            }
        }
        if (!headData.cos_title.isEmpty()) {
     
            StringBuffer cos_title = new StringBuffer();
            obj = headData.cos_title.iterator();
            while (obj.hasNext()) {
                cos_title.append((String)obj.next());
                if (obj.hasNext()) {
                    cos_title.append(", ");
                }
            }
            if(tc_enabled == false || param.all_cos_ind == false) {
                getNewRow();
                setCellContent(LangLabel.getValue(param.cur_lan, "lab_cos"), ColumnA);
                setCellContent(cos_title.toString(), ColumnB);
                sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);            	
            }
        }


        if (headData.itm_type != null) {
            getNewRow();
            String labtype = headData.itm_type;
            StringBuffer lab_type = new StringBuffer();
            //SELFSTUDY|-|COS, CLASSROOM|-|COS, INTEGRATED
            if(labtype.indexOf("SELFSTUDY|-|COS") > -1)
            {
            	 lab_type.append(LangLabel.getValue(param.cur_lan, "SELFSTUDY")).append(LangLabel.getValue(param.cur_lan, "COS")).append(", ");
            }
            if(labtype.indexOf("CLASSROOM|-|COS") > -1)
            {
            	lab_type.append(LangLabel.getValue(param.cur_lan, "CLASSROOM|-|COS")).append(", ");
            }
            if(labtype.indexOf("INTEGRATED") > -1)
            {
            	lab_type.append(LangLabel.getValue(param.cur_lan, "INTEGRATED"));
            }
            /*int labEqualIdx = labtype.indexOf(',');
            if (labEqualIdx > -1) {
                lab_type.append(LangLabel.getValue(param.cur_lan, "SELFSTUDY")).append(LangLabel.getValue(param.cur_lan, "COS")).append(", ").append(LangLabel.getValue(param.cur_lan, "CLASSROOM|-|COS")).append(", ").append(LangLabel.getValue(param.cur_lan, "INTEGRATED"));
            }
            else {
                lab_type.append(LangLabel.getValue(param.cur_lan, labtype));
            }*/
            setCellContent((LangLabel.getValue(param.cur_lan, "lab_cos")+LangLabel.getValue(param.cur_lan, "lab_type")), ColumnA);
            setCellContent(lab_type.toString(), ColumnB);
            sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
        }
        
        if (headData.mod_title != null) {
            getNewRow();
            setCellContent(LangLabel.getValue(param.cur_lan, "lab_mod"), ColumnA);
            setCellContent(headData.mod_title, ColumnB);
            sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
        }

        if (headData.period_from != null || headData.period_to != null) {
            getNewRow();
            StringBuffer period = new StringBuffer();
            if (headData.period_from != null) {
                period.append(LangLabel.getValue(param.cur_lan, "lab_from")).append(" ").append(headData.period_from).append(" ");
            }
            if (headData.period_to != null) {
                period.append(LangLabel.getValue(param.cur_lan, "lab_to")).append(" ").append(headData.period_to).append(" ");
            }
            setCellContent(LangLabel.getValue(param.cur_lan, "lab_period"), ColumnA);
            setCellContent(period.toString(), ColumnB);
            sheet.mergeCells(ColumnB, nowRowNum, ColumnH, nowRowNum);
        }
    }

    public void setFBAns(String responseBil, boolean isFirst) throws RowsExceededException, WriteException {
        if (!isFirst) {
            getNewRow();
        }
        setCellContent(responseBil, ColumnC, getStyleByName(STYLE_QUESTION_AND_ANSWER));
        sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
    }

}

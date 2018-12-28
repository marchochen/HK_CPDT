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

import com.cw.wizbank.ae.db.ViewEvnSurveyQueReport;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class EvnSurveyQueReportXls extends ReportXlsFileCreator {

	Connection con = null;
	ViewEvnSurveyQueReport viewEvnSuvQueRpt;
	EvnSurveyQueReport.EvnSurveyQueRptParam param;

	public EvnSurveyQueReportXls(Connection con, EvnSurveyQueReport.EvnSurveyQueRptParam surveyParam) {
		viewEvnSuvQueRpt = new ViewEvnSurveyQueReport(con);
		this.con = con;
		param = surveyParam;
		xlsFilePath = param.xlsFilePath;
		columnWidth = new int[] { 5000, 10000, 3000, 3000, 3000, 3000, 3000, 3000 };
	}

	public String outputSurveyQueReport(double responseCount, double enrolledCount, double responseRate,
			ArrayList dataList) throws SQLException, FileNotFoundException, qdbErrMessage, IOException, cwException {
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

			// set report title
			String reportTitle = null;
			if (param.rsp_title != null) {
				reportTitle = param.rsp_title;
			} else {
				 reportTitle = ExportHelper.getRptTitle(param.cur_lan, SurveyReport.RTE_TYPE_SURVEY_IND);
			}
			setTitleText(reportTitle);

			// write the head of report to workbook
			ViewEvnSurveyQueReport.HeadData headData = viewEvnSuvQueRpt.getRptHead(param.mod_res_id);
			setRptHeadData(headData);
			setBlankRow(ColumnH);
			// write report summary
			setReportSum(param, responseCount, enrolledCount, responseRate);
			// write report question summary
			setQueSum(param, dataList);
			// write report detail to workbook
			setTitleText(LangLabel.getValue(param.cur_lan, "lab_que_sta"));
			setReportDetail(param, dataList);
			String outputName = null;
			setColumnWidth(columnWidth);
			wb.write();
			wb.close();
			if (fileCount > 1) {
				zipXlsRpt(param.xlsFilePath);
				outputName = OutputFileName + ".zip";
			} else {
				outputName = OutputFileName + ".xls";
			}
			return outputName;
		} catch (RowsExceededException e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException(e.getMessage());
		} catch (WriteException e) {
			CommonLog.error(e.getMessage(),e);
			throw new cwException(e.getMessage());
		}
	}

	/*
	 * get the detail data of report,and set it to workbook
	 */
	private void setReportDetail(EvnSurveyQueReport.EvnSurveyQueRptParam param, ArrayList dataList) throws SQLException,
			IOException, RowsExceededException, WriteException {
		Iterator obj = dataList.iterator();
		while (obj.hasNext()) {
			SurveyQueData gx = (SurveyQueData) obj.next();
			if (gx.queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
				// write que top line
				getNewRow();
				setCellContent(LangLabel.getValue(param.cur_lan, "lab_no"), ColumnA, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
				setCellContent(LangLabel.getValue(param.cur_lan, "lab_que"), ColumnB, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
				sheet.mergeCells(ColumnC, nowRowNum, ColumnE, nowRowNum);
				setCellContent(LangLabel.getValue(param.cur_lan, "lab_choice"), ColumnC, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
				setCellContent(LangLabel.getValue(param.cur_lan, "lab_score"), ColumnF, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
				setCellContent(LangLabel.getValue(param.cur_lan, "lab_count"), ColumnG, getStyleByName(STYLE_TITLE_WITH_FILLBACK));
				setCellContent(LangLabel.getValue(param.cur_lan, "lab_per"), ColumnH, getStyleByName(STYLE_TITLE_WITH_FILLBACK));

				getNewRow();
				setCellContent((int) gx.order, ColumnA, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
				setCellContent(gx.que_text, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));

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
				setCellContent((int) gx.order, ColumnA, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
				setCellContent(gx.res_title, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
				int resultNull = viewEvnSuvQueRpt.getXslFBAns(param.mod_res_id, gx.order, this);
				if (resultNull == 0) {
					setCellContent("", ColumnC);
					sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
				}
			}
		}
	}

	private void setQueSum(EvnSurveyQueReport.EvnSurveyQueRptParam param, ArrayList dataList) throws RowsExceededException, WriteException {
		getNewRow();
		WritableCellFormat cf = getStyleByName(STYLE_TITLE_WITH_FILLBACK);
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_no"), ColumnA, cf);
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_que"), ColumnB, cf);
		sheet.mergeCells(ColumnC, nowRowNum, ColumnD, nowRowNum);
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_total"), ColumnC, cf);
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_avg_score"), ColumnE, cf);
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_sd"), ColumnF, cf);
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_answered"), ColumnG, cf);
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_unanswered"), ColumnH, cf);

		Iterator obj = dataList.iterator();
		while (obj.hasNext()) {
			SurveyQueData gx = (SurveyQueData) obj.next();
			getNewRow();
			setCellContent(gx.order, ColumnA, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
			setCellContent(gx.que_text, ColumnB, getStyleByName(STYLE_QUESTION_AND_ANSWER));
			setCellContent(gx.totalResponseCount, ColumnC);
			sheet.mergeCells(ColumnC, nowRowNum, ColumnD, nowRowNum);

			if (gx.queType.equals(dbQuestion.QUE_TYPE_MULTI)) {
				double avScore = SurveyQueReport.formatNum(gx.avScore, SurveyQueReport.DEC_POINT);
				double stDeviation = SurveyQueReport.formatNum(gx.stDeviation, SurveyQueReport.DEC_POINT);
				setCellContent(avScore, ColumnE);
				setCellContent(stDeviation, ColumnF);
			} else {
				String avScore = "--";
				String stDeviation = "--";
				setCellContent(avScore, ColumnE, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
				setCellContent(stDeviation, ColumnF, getStyleByName(STYLE_FORMATED_NUMBER_ALIGNMENT_RIGHT));
			}
			setCellContent(SurveyQueReport.formatNum(gx.answered, SurveyQueReport.DEC_POINT), ColumnG);
			setCellContent(SurveyQueReport.formatNum(gx.unanswered, SurveyQueReport.DEC_POINT), ColumnH);
		}
	}

	private void setReportSum(EvnSurveyQueReport.EvnSurveyQueRptParam param, double responseCount, double enrolledCount, double responseRate)
			throws cwException, WriteException {
		setTitleText(LangLabel.getValue(param.cur_lan, "lab_rpt_sum"));
		getNewRow();
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_public_count"), ColumnA);
		setCellContent(enrolledCount, ColumnB);
		// setCellContent("", ColumnC);
		sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
		getNewRow();
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_res_count"), ColumnA);
		setCellContent(responseCount, ColumnB);
		// setCellContent("", ColumnC);
		sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
		getNewRow();
		setCellContent(LangLabel.getValue(param.cur_lan, "lab_res_rate"), ColumnA);
		setCellContent(SurveyQueReport.formatNum(responseRate, SurveyQueReport.DEC_POINT), ColumnB);
		// setCellContent("", ColumnC);
		sheet.mergeCells(ColumnC, nowRowNum, ColumnH, nowRowNum);
	}

	/*
	 * set report head data from a hashMap to workbook
	 */
	public void setRptHeadData(ViewEvnSurveyQueReport.HeadData headData)
			throws cwException, RowsExceededException, WriteException {
		if (headData.mod_title != null) {
			getNewRow();
			setCellContent(LangLabel.getValue(param.cur_lan, "lab_mod_evn"), ColumnA);
			setCellContent(headData.mod_title, ColumnB);
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


package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.ViewExamPaperStatReport.SummaryStat;
import com.cw.wizbank.report.ViewExamPaperStatReport.TestInfo;
import com.cw.wizbank.report.ViewExamPaperStatReport.UsrInfo;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

public  class ExamPaperStatExporter extends ReportExporter  {
    
    public ExamPaperStatExporter(Connection incon, ExportController inController) {
    		super(incon, inController);
    }
    
    static final String DEFAULT_SORT_ORDER = "ASC";
 
    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
		Map modHash = ExamPaperStatReport.setModIdToList(con, specData.itm_id_lst, specData.tnd_id_lst, specData.mod_id_lst, prof);
		List modIds = (List)modHash.get("modIds");
		int itmCnt = ((Integer)modHash.get("itmCnt")).intValue();
		
		if (specData.sortOrder == null) {
			specData.sortOrder = DEFAULT_SORT_ORDER;
		}

		StringBuffer col_order = new StringBuffer();
		col_order.append(" ORDER BY ");
		col_order.append(specData.sortCol).append(" ").append(specData.sortOrder);
		
		ViewExamPaperStatReport viewExam = new ViewExamPaperStatReport();
		HashMap testData = viewExam.getTestData(con, specData.ent_id_lst, itmCnt, modIds, specData.att_start_datetime, specData.att_end_datetime, 
										col_order.toString(), specData.show_stat_only, false, controller, specData.lrn_scope_sql);
		ExamPaperStatExportHelper rptBuilder = new ExamPaperStatExportHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit);
		if (testData != null) {
			Map tstHash = (Map)testData.get(ViewExamPaperStatReport.TST_HASH);
			Map usrHash = (Map) testData.get(ViewExamPaperStatReport.USR_HASH);
			List orderLst = (List)testData.get(ViewExamPaperStatReport.ORDER_LST);
			SummaryStat sumStat =(SummaryStat)testData.get(ViewExamPaperStatReport.SUMMARY_STAT);
			
			rptBuilder.writeCondition(con, specData);
			//write report summary
			rptBuilder.writeSummaryData(sumStat, specData.cur_lang);
			if(specData.show_stat_only) {
				rptBuilder.writeExamStatTableHead(specData.cur_lang);
			}
			
			TestInfo tstInfo = null;
			Long modIdObj = null;
			for (int i = 0; i < orderLst.size() && !controller.isCancelled(); i++) {
				modIdObj = (Long)orderLst.get(i);
				tstInfo = (TestInfo)tstHash.get(modIdObj);
				writeExcelData(specData, rptBuilder, tstInfo, usrHash, um);
			}
        }
		if (!controller.isCancelled()) {
            controller.setFile(rptBuilder.finalizeFile());
        }
        
    }

	private void writeExcelData(SpecData specData, ExamPaperStatExportHelper rptBuilder, TestInfo tstInfo, 
									Map usrHash, UserManagement um) throws WriteException, cwException, SQLException {
		//write data to excel
		if(specData.show_stat_only) {
			rptBuilder.writeExamStatData(tstInfo, specData.cur_lang);
			controller.next();
		} else {
			rptBuilder.writeExamDataSummary(tstInfo, specData.cur_lang);
			int s = tstInfo.usrLst.size();
			if (s > 0) {
				rptBuilder.writeUsrTableHead(specData.rpt_content, specData.cur_lang, um);
				Long usrIdObj = null;
				UsrInfo usrInfo = null;
				for (int i = 0; i < s && !controller.isCancelled(); i++) {
					usrIdObj = (Long)tstInfo.usrLst.get(i);
					usrInfo = (UsrInfo)usrHash.get(usrIdObj);
					rptBuilder.writeUsrData(rptBuilder, tstInfo.res_id, usrInfo, specData.rpt_content, specData.cur_lang);
					controller.next();
				}
			}
			rptBuilder.getNewRow();
		}
	}
}

package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import jxl.write.WriteException;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemLesson;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.report.ReportExporter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class aeItemLessonExport  extends ReportExporter{
	
	public String filePath;
	
	public aeItemLessonExport(Connection incon, ExportController inController) {
		super(incon, inController);
	}
	
	@Override
	public void getReportXls(loginProfile prof, SpecData specData,
			UserManagement um, WizbiniLoader wizbini) throws qdbException,
			cwException, WriteException, SQLException, cwSysMessage,
			qdbErrMessage, IOException {
		aeItemLesson aeItemLesson = new aeItemLesson();
		Vector<aeItemLesson> vec = aeItemLesson.getRunList(con, specData.ils_id, false);
		controller.setTotalRow(vec.size()+2);
		aeItemLessonExportXls rptBuilder = new aeItemLessonExportXls(specData.tempDir, specData.relativeTempDirName, 
					specData.window_name, specData.encoding, specData.process_unit, "timetable", wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getUrl());
		//String rpt_title = aeItem.getItemTitle(con, specData.ils_id);
		String rpt_title = vec.get(0).ils_title;
		rptBuilder.outputReport(specData, vec, rpt_title, controller, prof);

		if (!controller.isCancelled()) {
            controller.setFile(rptBuilder.finalizeFile());
        }
		
		filePath = rptBuilder.webFilePath;
	}

}


package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;

public  class TrainCostStatExporter extends ReportExporter  {	
    public TrainCostStatExporter(Connection incon, ExportController inController) {
    		super(incon, inController);
    }

    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
    	
    	
    	ResultSet rs = null;
		PreparedStatement stmt = null;
    	
    	Vector entIds = new Vector();
		List itmIds = new ArrayList();
		TrainCostStatReport.setIdsToList(con, entIds, itmIds, specData.ent_id_lst, specData.itm_id_lst, specData.tnd_id_lst, prof);
		
		String entIdTableName = null;
        String entIdColName = "usr_ent_ids";
        String itmTableName = null;
        String itmIdColName = "tmp_itm_id";
        TrainCostStatExportHelper rptBuilder = new TrainCostStatExportHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit);
        try {
			if (entIds.size() > 0) {
				entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);
			}
			
			if (itmIds.size() > 0) {
				itmTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
			}
	
			if(entIdTableName != null) {
				cwSQL.insertSimpleTempTable(con, entIdTableName, entIds, cwSQL.COL_TYPE_LONG);
			}
			
			if(entIdTableName == null && specData.lrn_scope_sql != null) {
			    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0, specData.lrn_scope_sql);
			}
			
			if(itmTableName != null){
				cwSQL.insertSimpleTempTable(con, itmTableName, itmIds, cwSQL.COL_TYPE_LONG);
			}
			
			stmt  = TrainCostStatReport.getTrainCostStatStmt(con, entIdTableName, entIdColName, itmTableName, itmIdColName, 
															specData.start_datetime, specData.end_datetime, true);
			rs = stmt.executeQuery();
			int total = 0;
			if (rs.next()) {
				total = rs.getInt(1);
			}
			
			controller.setTotalRow(total + 4);
			controller.next();
			
			int totalRow = rptBuilder.writeCondition(con, specData);
			controller.next();
			
			rptBuilder.writeTableHead(specData.rpt_content, specData.cur_lang);
			controller.next();

			stmt  = TrainCostStatReport.getTrainCostStatStmt(con, entIdTableName, entIdColName, itmTableName, itmIdColName, 
															specData.start_datetime, specData.end_datetime, false);
			rs = stmt.executeQuery();
			int usrCnt = 0;
			double actual = 0;
			double totalCost = 0;
			while (rs.next()) {
	        	actual = rs.getDouble("actual");
	        	usrCnt = rs.getInt("app_cnt");
	        	if (usrCnt > 0) {
	        		//计算总培训费用
					totalCost += actual;
				}
				rptBuilder.writeData(specData.rpt_content, specData.cur_lang, rs, actual, usrCnt);
				controller.next();
			}
			//写总培训费用
			rptBuilder.setCellContent(TrainFeeStatReport.df.format(totalCost), totalRow, rptBuilder.ColumnB);
			controller.next();
			
			if (!controller.isCancelled()) {
	            controller.setFile(rptBuilder.finalizeFile());
	        }
        } finally {
        	if(itmTableName != null){
				cwSQL.dropTempTable(con, itmTableName);
	        }
	        if(entIdTableName != null) {
	        	cwSQL.dropTempTable(con, entIdTableName);	
	        }
			cwSQL.cleanUp(rs, stmt);
        }
    }
	
}


package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.FMFeeReport.ClassInfo;
import com.cw.wizbank.report.FMFeeReport.FacilityInfo;
import com.cw.wizbank.report.FMFeeReport.ReservationInfo;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public  class FMFeeExporter extends ReportExporter  {	
    public FMFeeExporter(Connection incon, ExportController inController) {
    		super(incon, inController);
    }

    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
    	
    	
    	ResultSet rs = null;
		PreparedStatement stmt = null;

    	List itmIds = FMFeeReport.getItmIdLst(con, specData.itm_id_lst, specData.tnd_id_lst, prof);
		
		String itmTableName = null;
        String itmIdColName = "tmp_itm_id";
        FMFeeExportHelper rptBuilder = new FMFeeExportHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit);
        try {
        	FMFeeReport fmRpt = new FMFeeReport();
        	
        	if (itmIds.size() > 0) {
				itmTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
			}
			
			if(itmTableName != null){
				cwSQL.insertSimpleTempTable(con, itmTableName, itmIds, cwSQL.COL_TYPE_LONG);
			}
			
			int total = getTotalRec(con, itmTableName, itmIdColName, prof.root_ent_id, specData.tcr_id, specData.start_datetime, 
									specData.end_datetime, specData.fac_type, fmRpt);

			if (total == 0) {
				controller.setTotalRow(total);
				return;
			} else {
				controller.setTotalRow(total + 4);
			}
			controller.next();
			
			rptBuilder.writeCondition(con, specData);
			controller.next();
			
			rptBuilder.writeTableHead(specData.cur_lang);
			controller.next();
			
			stmt = fmRpt.getRptStmt(con, itmTableName, itmIdColName, prof.root_ent_id, specData.tcr_id, specData.start_datetime, 
					specData.end_datetime, specData.fac_type, false);
			rs = stmt.executeQuery();
			controller.next();
			
			long new_rsv_id = 0;
			long rsv_id = 0;
			Timestamp new_fsh_date = null;
			Timestamp fsh_date = null;
			
			ClassInfo classInfo = null;
			ReservationInfo resInfo = null;
			FacilityInfo facInfo = null;

			while (rs.next()) {
				new_rsv_id = rs.getLong("rsv_id");
				new_fsh_date = rs.getTimestamp("fsh_date");
				if (rsv_id != new_rsv_id || !new_fsh_date.equals(fsh_date)) {
					fsh_date = new_fsh_date;
					
					if (rsv_id != new_rsv_id) {
						rsv_id = new_rsv_id;
						writeRptData(classInfo, rptBuilder, specData.cur_lang);
						
						classInfo = fmRpt.new ClassInfo();
						classInfo.rsv_purpose = rs.getString("rsv_purpose");
						classInfo.rsv_participant_no = rs.getInt("rsv_participant_no");
						classInfo.usr_display_bil = rs.getString("usr_display_bil");
					}
					
					resInfo = fmRpt.new ReservationInfo();
					classInfo.resLst.add(resInfo);
					resInfo.fsh_date = fsh_date;
				}
				
				facInfo = fmRpt.new FacilityInfo();
				resInfo.facilityLst.add(facInfo);
				facInfo.fsh_start_time = rs.getTimestamp("fsh_start_time");
				facInfo.fsh_end_time = rs.getTimestamp("fsh_end_time");
				facInfo.fac_title = rs.getString("fac_title");
				facInfo.fac_fee = rs.getDouble("fac_fee");
				resInfo.total_fee += facInfo.fac_fee;
			}
			writeRptData(classInfo, rptBuilder, specData.cur_lang);
			
			if (!controller.isCancelled()) {
	            controller.setFile(rptBuilder.finalizeFile());
	        }
        } finally {
        	if(itmTableName != null){
				cwSQL.dropTempTable(con, itmTableName);
	        }
			cwSQL.cleanUp(rs, stmt);
        }
    }
    
    public int getTotalRec(Connection con, String itmTableName, String itmIdColName, long root_ent_id, long tcr_id, 
							Timestamp start_datetime, Timestamp end_datetime, int fac_type, FMFeeReport fmRpt) throws SQLException {
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	int count = 0;
    	try {
    		if (itmTableName == null) {
    			return 0;
    		}
    		stmt = fmRpt.getRptStmt(con, itmTableName, itmIdColName, root_ent_id, tcr_id, start_datetime, end_datetime, fac_type, true);
    		rs = stmt.executeQuery();
    		while (rs.next()) {
    			count++;
    		}
    		return count;
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    }
	
    private void writeRptData(ClassInfo classInfo, FMFeeExportHelper rptBuilder, String cur_lang) throws RowsExceededException, WriteException, SQLException {
    	if (classInfo != null) {
	    	ReservationInfo resInfo = null;
	    	int i, size = classInfo.resLst.size();
	    	for (i = 0; i < size; i++) {
	    		resInfo = (ReservationInfo)classInfo.resLst.get(i);
	    		rptBuilder.writeData(classInfo.rsv_purpose, classInfo.usr_display_bil, classInfo.rsv_participant_no, resInfo, cur_lang);
	    		controller.next();
	    	}
    	}
    }
}

package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import jxl.write.WriteException;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class CreditReportExporter extends ReportExporter {

	public CreditReportExporter(Connection incon, ExportController inController) {
		super(incon, inController);
	}

	public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException,
			WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {
		Hashtable hash = null;
		Vector vtdata = null;
		CreditReportHelper rptBuilder = new CreditReportHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit);
        Credit creditManager = new Credit();
        if(specData.all_usg_ind){
			Vector ent_id_vec = null;
        	if(wizbini.cfgTcEnabled){
        		//所有我为officer的用户组
        		ent_id_vec = dbUserGroup.getAllTargetGroupIdForOfficer(con, prof.usr_ent_id);
        	}else{
        		//所有用户组
        		ent_id_vec = dbUserGroup.getAllGroup(con, prof.root_ent_id);
        	}
        	specData.ent_id_lst = new String[ent_id_vec.size()];
            for (int i = 0; i < ent_id_vec.size(); i++) {
            	specData.ent_id_lst[i] = ((Object)ent_id_vec.elementAt(i)).toString();
            }
        }
        //报告摘要
        rptBuilder.writeCondition(con, specData);
		if (specData.isDetail){//明细报表
			vtdata = creditManager.creditDetailOfUser(con,specData.ent_id_lst,specData.include_del_usr,specData.att_create_start_datetime,specData.att_create_end_datetime);
	        controller.setTotalRow(vtdata.size());
        	rptBuilder.writeUserTableHead(specData.cur_lang, um, specData.isDetail);
	        for (int i=0; i<vtdata.size(); i++){
        		rptBuilder.writeDtailData(con, (com.cw.wizbank.JsonMod.credit.Credit.resultData)vtdata.get(i), specData.cur_lang);
                controller.next();
	        }	        
		}else{//用户组报表	        
			hash = creditManager.getScoreData(con,specData.ent_id_lst,specData.include_del_usr,specData.att_create_start_datetime,specData.att_create_end_datetime);
	        controller.setTotalRow(hash.size());
			Vector[] vtlit = new Vector[2];
			Vector vtgroupdata = null;
			Vector vtresdata = null;
			if(specData.show_usg_only){
				rptBuilder.writegroupTableHead(specData.cur_lang, um);
			}
	    	Enumeration enumeration = hash.keys();
	    	while(enumeration.hasMoreElements()){
	    		vtlit = (Vector[])hash.get((Long)enumeration.nextElement());
	    		vtresdata = vtlit[0];
	    		vtgroupdata = vtlit[1];
		        if (specData.show_usg_only){		        	
					rptBuilder.writegroupData(((com.cw.wizbank.JsonMod.credit.Credit.dataByGroup)vtgroupdata.get(0)), specData.cur_lang);
				}else{	        	
	                rptBuilder.writeStatisticDataHead(((com.cw.wizbank.JsonMod.credit.Credit.dataByGroup)vtgroupdata.get(0)),specData.cur_lang);
		        	rptBuilder.writeUserTableHead(specData.cur_lang, um, specData.isDetail);
		        	for (int j=0; j<vtresdata.size(); j++){
		        		rptBuilder.writeData((com.cw.wizbank.JsonMod.credit.Credit.resultData)vtresdata.get(j), specData.cur_lang);
		        	}
		        	rptBuilder.getNewRow();
				}
		        controller.next();
	    	}
		} 
        if (!controller.isCancelled()) {
            controller.setFile(rptBuilder.finalizeFile());
        }
	}

}

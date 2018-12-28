package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Vector;

import com.cw.wizbank.config.organization.usermanagement.LabelType;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.report.ModuleEvnOfCosExporter.ResultData;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.LangLabel;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ModuleEvnOfCosRptExporter extends ExportHelper {
	

	public ModuleEvnOfCosRptExporter(String tempDir, String tempDirName, String winName, String encoding, int processUnit,String url) throws WriteException,
			IOException {
		super(tempDir, tempDirName, winName, encoding, processUnit,url);
	}
	
	public void writeTableHead(String cur_lang, UserManagement um) throws RowsExceededException, WriteException {
//		setBlankRow(1);
		getNewRow();
		
		short index = 0;
		Iterator lt = null;
        LabelType labelType;
		
        //usr_display_bil
        lt = um.getUserProfile().getProfileAttributes().getName().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++);
            }
        }
        
        //usg_display_bil
        lt = um.getUserProfile().getProfileAttributes().getGroup().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++);
            }
        }
        
        //ugr_display_bil
        lt = um.getUserProfile().getProfileAttributes().getGrade().getLabel().iterator();
        while (lt.hasNext()) {
            labelType = (LabelType)lt.next();
            if (labelType.getLang().equals(cur_lang)) {
                setCellContent(labelType.getValue(), index++);
            }
        }
        
        //module title
        setCellContent(LangLabel.getValue(cur_lang, "669"), index++);
        //access start time
        setCellContent(LangLabel.getValue(cur_lang, "670"), index++);
        //access end time
        setCellContent(LangLabel.getValue(cur_lang, "671"), index++);
        //total time
        setCellContent(LangLabel.getValue(cur_lang, "672"), index++);
        //status
        setCellContent(LangLabel.getValue(cur_lang, "673"), index++);
        //score
        setCellContent(LangLabel.getValue(cur_lang, "lab_mod_score"), index++);
	}

	public void writeData(Connection con, ModuleEvnOfCosExporter.ResultData rsData, EntityFullPath entityfullpath, String cur_lang) throws RowsExceededException,
			WriteException {
		short index = 0;
		getNewRow();
		if (rsData.usr_display_bil != null && !rsData.usr_display_bil.trim().equals("")) {
			setCellContent(rsData.usr_display_bil, index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "142"), index++);
		}

		if (rsData.usg_ent_id > 0) {
			setCellContent(entityfullpath.getFullPath(con, rsData.usg_ent_id), index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
		}

		if (rsData.ugr_display_bil != null && !rsData.ugr_display_bil.trim().equals("")) {
			setCellContent(rsData.ugr_display_bil, index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
		}

		if (rsData.mod_title != null && !rsData.mod_title.trim().equals("")) {
			setCellContent(rsData.mod_title, index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
		}

		if (rsData.mov_last_acc_datetime != null) {
			Timestamp startAccessTime = new Timestamp(rsData.mov_last_acc_datetime.getTime() - (long) (Math.round(rsData.mov_total_time) * 1000));
			setCellContent(startAccessTime, index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
		}

		if (rsData.mov_last_acc_datetime != null) {
			setCellContent(rsData.mov_last_acc_datetime, index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
		}

		if (rsData.mov_total_time > 0) {
			setTimeCountCellContent(rsData.mov_total_time, index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
		}
		
		//set status of module
		if (rsData.mov_status != null) {
			setCellContent(ModuleRptExportHelper.getMtstTitle(rsData.mov_status, rsData.mod_type, rsData.mod_max_score, rsData.mov_score,
					rsData.pgr_grade, cur_lang), index++);
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_not_attempted"), index++);
		}

		//set score of module
		if (rsData.mod_type.equals(dbModule.MOD_TYPE_ASS)) {
			if (rsData.mod_max_score < 0) {
				String scoreStr = ModuleRptExportHelper.getGrade(rsData.pgr_grade, cur_lang);
				if(scoreStr != null) {
					setCellContent(scoreStr, index++);
				} else {
					setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
				}
			} else if(rsData.mov_score == 0){
				setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
			} else {
				setCellContent(rsData.mov_score, index++);
			}
		} else if (rsData.mod_type.equals(dbModule.MOD_TYPE_AICC_AU) || rsData.mod_type.equals(dbModule.MOD_TYPE_NETG_COK)
				|| rsData.mod_type.equals(dbModule.MOD_TYPE_DXT) || rsData.mod_type.equals(dbModule.MOD_TYPE_TST)
				|| rsData.mod_type.equals(dbModule.MOD_TYPE_SCO)) {
			if(rsData.mov_score > 0) {
				setCellContent(rsData.mov_score, index++);
			} else {
				setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
			}
		} else {
			setCellContent(LangLabel.getValue(cur_lang, "lab_na"), index++);
		}
	}
	
}

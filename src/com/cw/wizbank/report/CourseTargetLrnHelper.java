/*
 * Created on 2005-9-16
 *
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import jxl.write.WriteException;

import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.report.ReportExporter.SpecData;
import com.cw.wizbank.util.LangLabel;

/**
 * @author dixson
 *
 */
public class CourseTargetLrnHelper extends ExportHelper {
    
    //private static Timestamp MAXDATETIME=Timestamp.valueOf("9999-12-31 23:59:59");
    public CourseTargetLrnHelper(String tempDir, String tempDirName, String winName, String encoding, int processUnit) throws WriteException, IOException {
        super(tempDir, tempDirName, winName, encoding, processUnit);
    }

  

    public void writeData(Vector t_usr, SpecData specData, String cur_lang,Connection con) throws WriteException,SQLException {
        short index = 0;
        getNewRow();
        setCellContent(t_usr.get(1) != null ? t_usr.get(1).toString() : "" , index++);
        setCellContent(t_usr.get(2) != null ? t_usr.get(2).toString() : "", index++);
        setCellContent(t_usr.get(3) != null ? t_usr.get(3).toString() : "", index++);
        setCellContent(t_usr.get(4) != null ? t_usr.get(4).toString() : "", index++);
        setCellContent(t_usr.get(5) != null ? t_usr.get(5).toString() : "", index++);
        setCellContent(t_usr.get(6) != null ? t_usr.get(6).toString() : "", index++);
    }

    /**
     * @param rpt_content
     */
    public void writeTableHead(SpecData specData, String cur_lang, UserManagement um) throws WriteException {
        short index = 0;
        getNewRow();
        setCellContent(LangLabel.getValue(cur_lang, "lab_user_id"), index++);  
        setCellContent(LangLabel.getValue(cur_lang, "5"), index++); 
        setCellContent(LangLabel.getValue(cur_lang, "lab_email"), index++); 
        setCellContent(LangLabel.getValue(cur_lang, "lab_group"), index++); 
        setCellContent(LangLabel.getValue(cur_lang, "lab_grade"), index++); 
        setCellContent(LangLabel.getValue(cur_lang, "912"), index++);
    }
}
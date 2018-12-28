/*
 * Created on 2005-9-15
 */
package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;



import com.cw.wizbank.competency.CmSkillSetManager;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.itemtarget.ManageItemTarget;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

import jxl.write.WriteException;

/**
 * @author dixson
 */
public class CourseTargetLrnExporter extends ReportExporter{
    public CourseTargetLrnExporter(Connection incon, ExportController inController) {
        super(incon, inController);
    }


    public void getReportXls(loginProfile prof, SpecData specData, UserManagement um, WizbiniLoader wizbini) throws qdbException, cwException, WriteException, SQLException, cwSysMessage, qdbErrMessage, IOException {

        Vector vtReport = ManageItemTarget.getTargetUserLst( con, specData.itm_id_lst[0]);
        int  rpt_count = vtReport.size();
         controller.setTotalRow(rpt_count);
        
        if (rpt_count > 0) {
           

            EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
           
            
            CourseTargetLrnHelper rptBuilder = new CourseTargetLrnHelper(specData.tempDir, specData.relativeTempDirName, specData.window_name, specData.encoding, specData.process_unit);
            rptBuilder.writeTableHead(specData, specData.cur_lang, um);

            for (int i=0; i<vtReport.size(); i++) {  
                Vector t_usr = (Vector)vtReport.get(i);
                rptBuilder.writeData(t_usr, specData, specData.cur_lang, con); 
                controller.next();
                controller.currentRow = i+ 1;
            }
            if (!controller.isCancelled()) {
                controller.setFile(rptBuilder.finalizeFile());
            }
        }
    }

    
}

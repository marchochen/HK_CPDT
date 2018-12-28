package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.db.view.ViewAcReportTemplate;

/**
Logical Layer Access Control on ReportTemplate<BR>
*/
public class AcReportTemplate extends AcInstance {

    /**
    function ext id, read
    */
    public static final String FTN_RTE_READ = "RTE_READ";

    
    public AcReportTemplate(Connection con) {
        super(con);
        view = new ViewAcReportTemplate(con); 
    }

    public Vector getAccessibleReportTemplateType(long ent_id, String role_ext_id) 
        throws SQLException {
        Vector v = new Vector();
        long[] ac_rte_id = getGrantedInstance(ent_id, role_ext_id, FTN_RTE_READ);
        v = ((ViewAcReportTemplate)view).getReportTemplateType(ac_rte_id);
        return v;
    }
}
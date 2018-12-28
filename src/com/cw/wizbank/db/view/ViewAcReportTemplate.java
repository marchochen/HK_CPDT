package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.util.cwUtils;

/**
Database Layer Instance Access Control on ReportTemplate<BR>
Access Control Table: acReportTemplate<BR>
*/
public class ViewAcReportTemplate extends ViewAcInstance {
    
    /**
    ftn_type of this view
    */
    private static final String FtnType = "REPORT_TEMPLATE";
    
    /**
    all the thing need to do in the constructor is to initialize a set of super class variables<BR>
    see ViewAcInstance for details
    */
    public ViewAcReportTemplate(Connection con) {
        super(con);
        
        //access control table name
        dbTableName = "acReportTemplate";
        
        //instance id column name of the access control table
        colInstanceId = "ac_rte_id";

        //entity id column name of the access control table
        colEntityId = "ac_rte_ent_id";
        
        //role ext id column name of the access control table
        colRoleExtId = "ac_rte_rol_ext_id";
        
        //function ext id column of the access control table
        colFunctionExtId = "ac_rte_ftn_ext_id";
        
        //owner indicator column name of the access control table
        colOwnerInd = "ac_rte_owner_ind";
        
        //create user id column name of the access control table
        colCreateUsrId = "ac_rte_create_usr_id";
        
        //create timestamp column name of the access control table
        colCreateTimestamp = "ac_rte_create_timestamp";
        
        //funtion types of this instance will have
        ftn_types.addElement(FtnType);
    }    
    
    public Vector getReportTemplateType(long[] rte_id) throws SQLException {
        
        Vector v = new Vector();
        PreparedStatement stmt = null;
        if(rte_id != null && rte_id.length > 0) {
            try {
                stmt = con.prepareStatement(SqlStatements.SQL_GET_RTE_TYPE + cwUtils.array2list(rte_id));
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    v.addElement(rs.getString("rte_type"));
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
        return v;
    }
}
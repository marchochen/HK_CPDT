package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.db.sql.SqlStatements;

/**
Database layer for table aeItemType
*/
public class DbTemplateView{

    public long tvw_tpl_id;
    public String tvw_id;
    public String tvw_xml;
    public boolean tvw_cat_ind;
    public boolean tvw_target_ind;
    public boolean tvw_cm_ind;
    public boolean tvw_mote_ind;
    public boolean tvw_itm_acc_ind;
    public boolean tvw_res_ind;
    public boolean tvw_rsv_ind;
    public boolean tvw_filesize_ind;
    public boolean tvw_km_published_version_ind;
    public boolean tvw_km_domain_ind;
    // added for cost center group
    public boolean tvw_cost_center_ind;
    public Timestamp tvw_create_timestamp;
    public String tvw_create_usr_id;
    public boolean tvw_wrk_tpl_ind;
    public boolean tvw_ctb_ind;
    public boolean tvw_tcr_ind;

    /**
    Item Accreditation
    */
    //==================public boolean tvw_iad_ind;
    
    /**
    Initialize the class variables by reading database.<BR>
    Pre-define variables:
    <ul>
    <li>tvw_tpl_id</li>
    <li>tvw_id</li>
    </ul>
    */
    public void get(Connection con) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_templateView);
        stmt.setLong(1, this.tvw_tpl_id);
        stmt.setString(2, this.tvw_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {

            this.tvw_create_timestamp = rs.getTimestamp("tvw_create_timestamp");
            this.tvw_create_usr_id = rs.getString("tvw_create_usr_id");
            this.tvw_cat_ind = rs.getBoolean("tvw_cat_ind");
            this.tvw_target_ind = rs.getBoolean("tvw_target_ind");
            this.tvw_cm_ind = rs.getBoolean("tvw_cm_ind");
            this.tvw_mote_ind = rs.getBoolean("tvw_mote_ind");
            this.tvw_itm_acc_ind = rs.getBoolean("tvw_itm_acc_ind");
            //this.tvw_run_ind = rs.getBoolean("tvw_run_ind");
            this.tvw_res_ind = rs.getBoolean("tvw_res_ind");
            this.tvw_rsv_ind = rs.getBoolean("tvw_rsv_ind");
            this.tvw_filesize_ind = rs.getBoolean("tvw_filesize_ind");
            this.tvw_km_published_version_ind = rs.getBoolean("tvw_km_published_version_ind");
            this.tvw_km_domain_ind = rs.getBoolean("tvw_km_domain_ind");
            this.tvw_xml = cwSQL.getClobValue(rs, "tvw_xml");
            this.tvw_wrk_tpl_ind = rs.getBoolean("tvw_wrk_tpl_ind");
            this.tvw_ctb_ind = rs.getBoolean("tvw_ctb_ind");
            this.tvw_tcr_ind = rs.getBoolean("tvw_tcr_ind");
            // added for cost center group
            this.tvw_cost_center_ind = rs.getBoolean("tvw_cost_center_ind");
            //==================this.tvw_iad_ind = rs.getBoolean("tvw_iad_ind");
        }
        stmt.close();
        return;
    }

}
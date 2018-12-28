package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.Hashtable;

public class dbDisplayOption
{
    public static   String VIEW_LIST = "LIST";
    public static   String VIEW_DETAIL = "DETAIL";
    public static   String VIEW_LRN_READ = "LRN_READ";
    public static   String VIEW_IST_READ = "IST_READ";
    public static   String VIEW_IST_EDIT = "IST_EDIT";
    
    public boolean  dpo_default; 
    
    public long     dpo_res_id;
    public String   dpo_res_type;
    public String   dpo_res_subtype;
    public String   dpo_view;
    public boolean  dpo_icon_ind;
    public boolean  dpo_title_ind;
    public boolean  dpo_lan_ind;
    public boolean  dpo_desc_ind;
    public boolean  dpo_instruct_ind;
    public boolean  dpo_eff_start_datetime_ind;
    public boolean  dpo_eff_end_datetime_ind;
    public boolean  dpo_difficulty_ind;
    public boolean  dpo_time_limit_ind;
    public boolean  dpo_suggested_time_ind;
    public boolean  dpo_duration_ind;
    public boolean  dpo_max_score_ind;
    public boolean  dpo_pass_score_ind;
    public boolean  dpo_pgr_start_datetime_ind;
    public boolean  dpo_pgr_complete_datetime_ind;
    public boolean  dpo_pgr_last_acc_datetime_ind;
    public boolean  dpo_pgr_attempt_nbr_ind;
    public boolean  dpo_max_usr_attempt_ind;
    public boolean  dpo_instructor_ind;
    public boolean  dpo_organization_ind;
    public boolean  dpo_moderator_ind;
    public boolean  dpo_evt_datetime_ind;
    public boolean  dpo_evt_venue_ind;
    public boolean  dpo_status_ind;
    
    public dbDisplayOption() {;}
    
    // Save display options
    // Update the record if exist otherwise insert new record if not exist
    public void save(Connection con) 
        throws qdbException
    {
        try{
            // Don't need to save options if default values is used
            if(dpo_default)
                return; 
                
            PreparedStatement stmt = con.prepareStatement(
                  "  SELECT dpo_res_id from DisplayOption WHERE " 
                + "          dpo_res_id = ? AND dpo_res_type = ? "
                + "          dpo_res_subtype = ? AND dpo_view = ?  ");
            
            
            ResultSet rs = stmt.executeQuery();
            String SQL = new String();
            // record exists , do update
            if (rs.next()) {
                SQL = " UPDATE DisplayOption SET "
                + "            dpo_icon_ind = ? "
                + "           ,dpo_title_ind = ? "
                + "           ,dpo_lan_ind = ? "
                + "           ,dpo_desc_ind = ? "
                + "           ,dpo_instruct_ind = ? "
                + "           ,dpo_eff_start_datetime_ind = ? "
                + "           ,dpo_eff_end_datetime_ind = ? "
                + "           ,dpo_difficutly_ind = ? "
                + "           ,dpo_time_limit_ind = ? "
                + "           ,dpo_suggested_time_ind = ? "
                + "           ,dpo_duration_ind = ? " 
                + "           ,dpo_max_score_ind = ? "
                + "           ,dpo_pass_score_ind = ? "
                + "           ,dpo_pgr_start_datetime_ind = ? "
                + "           ,dpo_pgr_complete_datetime_ind = ? "
                + "           ,dpo_pgr_last_acc_datetime_ind = ? "
                + "           ,dpo_pgr_attempt_nbr_ind = ? "
                + "           ,dpo_max_usr_attempt_ind = ? "
                + "           ,dpo_instructor_ind = ? "
                + "           ,dpo_organization_ind = ? "
                + "           ,dpo_moderator_ind = ? "
                + "           ,dpo_evt_datetime_ind = ? "
                + "           ,dpo_evt_venue_ind = ? "
                + "           ,dpo_status_ind = ? "
                + "     WHERE "
                + "           dpo_res_id = ? " 
                + "       AND dpo_res_type = ? " 
                + "       AND dpo_res_subtype = ? " 
                + "       AND dpo_view = ? ";
            // no record exists, do insert
            }else {
                SQL = " INSERT INTO DisplayOption "
                + "         (dpo_icon_ind "
                + "         ,dpo_title_ind "
                + "         ,dpo_lan_ind "
                + "         ,dpo_desc_ind "
                + "         ,dpo_instruct_ind "
                + "         ,dpo_eff_start_datetime_ind "
                + "         ,dpo_eff_end_datetime_ind "
                + "         ,dpo_difficulty_ind "
                + "         ,dpo_time_limit_ind "
                + "         ,dpo_suggested_time_ind "
                + "         ,dpo_duration_ind "
                + "         ,dpo_max_score_ind "
                + "         ,dpo_pass_score_ind "
                + "         ,dpo_pgr_start_datetime_ind "
                + "         ,dpo_pgr_complete_datetime_ind "
                + "         ,dpo_pgr_last_acc_datetime_ind "
                + "         ,dpo_pgr_attempt_nbr_ind "
                + "         ,dpo_max_usr_attempt_ind "
                + "         ,dpo_instructor_ind "
                + "         ,dpo_organization_ind "
                + "         ,dpo_moderator_ind "
                + "         ,dpo_evt_datetime_ind "
                + "         ,dpo_evt_venue_ind "
                + "         ,dpo_status_ind "
                + "         ,dpo_res_id " 
                + "         ,dpo_res_type "
                + "         ,dpo_res_subtype "
                + "         ,dpo_view ) "

                + "      VALUES (?,?,?,?,?, "
                + "              ?,?,?,?,?, "
                + "              ?,?,?,?,?, "
                + "              ?,?,?,?,?, "
                + "              ?,?,?,?,?,?) "; 
            }

            stmt = con.prepareStatement(SQL);
            
            stmt.setLong(5, dpo_res_id);
            stmt.setString(6, dpo_res_type);
            stmt.setString(7, dpo_res_subtype);
            stmt.setString(8, dpo_view);
            stmt.setBoolean(9, dpo_icon_ind);
            stmt.setBoolean(10, dpo_title_ind);
            stmt.setBoolean(11, dpo_lan_ind);
            stmt.setBoolean(12, dpo_desc_ind);
            stmt.setBoolean(13, dpo_instruct_ind);
            stmt.setBoolean(14, dpo_eff_start_datetime_ind);
            stmt.setBoolean(15, dpo_eff_end_datetime_ind);
            stmt.setBoolean(16, dpo_difficulty_ind);
            stmt.setBoolean(17, dpo_time_limit_ind);
            stmt.setBoolean(18, dpo_suggested_time_ind);
            stmt.setBoolean(19, dpo_duration_ind);
            stmt.setBoolean(20, dpo_max_score_ind);
            stmt.setBoolean(21, dpo_pass_score_ind);
            stmt.setBoolean(22, dpo_pgr_start_datetime_ind);
            stmt.setBoolean(23, dpo_pgr_complete_datetime_ind);
            stmt.setBoolean(24, dpo_pgr_last_acc_datetime_ind);
            stmt.setBoolean(25, dpo_pgr_attempt_nbr_ind);
            stmt.setBoolean(26, dpo_max_usr_attempt_ind);
            stmt.setBoolean(27, dpo_instructor_ind);
            stmt.setBoolean(28, dpo_organization_ind);
            stmt.setBoolean(29, dpo_moderator_ind);
            stmt.setBoolean(30, dpo_evt_datetime_ind);
            stmt.setBoolean(31, dpo_evt_venue_ind);
            stmt.setBoolean(32, dpo_status_ind);

            stmt.setLong(1, dpo_res_id);
            stmt.setString(2, dpo_res_type);
            stmt.setString(3, dpo_res_subtype);
            stmt.setString(4, dpo_view);
            
            if (stmt.executeUpdate() != 1) {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to save display option."); 
            }
            stmt.close();           
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    // Delete the option of a resource with specified id and view
    public void del(Connection con) 
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                    " DELETE From DisplayOption WHERE " 
                +   "    dpo_res_id = ?  AND " 
                +   "    dpo_view = ? " );
            
            stmt.setLong(1,dpo_res_id);
            stmt.setString(2,dpo_view);                
                
            stmt.executeUpdate(); 
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public void get(Connection con)
        throws qdbException
    {
        try {
            boolean use_default = true;
            
            if(dpo_res_type.equalsIgnoreCase(dbResource.RES_TYPE_COS) && dpo_res_subtype==null)
                dpo_res_subtype = dbResource.RES_TYPE_COS;
                
            PreparedStatement stmt = con.prepareStatement(
                  "  SELECT dpo_res_id from DisplayOption WHERE " 
                + "          dpo_res_id = ? AND dpo_res_type = ? "
                + "          AND dpo_res_subtype = ? AND dpo_view = ?  ");

            stmt.setLong(1, dpo_res_id);
            stmt.setString(2, dpo_res_type);
            stmt.setString(3, dpo_res_subtype);
            stmt.setString(4, dpo_view);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                use_default = false;
            }
            stmt.close();
            
            String SQL =  "  SELECT "
                + "          dpo_icon_ind "
                + "         ,dpo_title_ind "
                + "         ,dpo_lan_ind "
                + "         ,dpo_desc_ind "
                + "         ,dpo_instruct_ind "
                + "         ,dpo_eff_start_datetime_ind "
                + "         ,dpo_eff_end_datetime_ind "
                + "         ,dpo_difficulty_ind "
                + "         ,dpo_time_limit_ind "
                + "         ,dpo_suggested_time_ind "
                + "         ,dpo_duration_ind "
                + "         ,dpo_max_score_ind "
                + "         ,dpo_pass_score_ind "
                + "         ,dpo_pgr_start_datetime_ind "
                + "         ,dpo_pgr_complete_datetime_ind "
                + "         ,dpo_pgr_last_acc_datetime_ind "
                + "         ,dpo_pgr_attempt_nbr_ind "
                + "         ,dpo_max_usr_attempt_ind "
                + "         ,dpo_instructor_ind "
                + "         ,dpo_organization_ind "
                + "         ,dpo_moderator_ind "
                + "         ,dpo_evt_datetime_ind "
                + "         ,dpo_evt_venue_ind "
                + "         ,dpo_status_ind "
                + "      FROM DisplayOption WHERE "
                + "         dpo_res_id = ? AND "
                + "         dpo_res_type = ? AND "
                + "         dpo_res_subtype = ? AND "
                + "         dpo_view = ? "; 

            stmt = con.prepareStatement(SQL);
 
            if (!use_default) {
                stmt.setLong(1, dpo_res_id);
            }else {
                // dpo_res_id = 0 stands for the default options of the specified view and type
                stmt.setLong(1, 0);
            }
             
            stmt.setString(2, dpo_res_type);
            stmt.setString(3, dpo_res_subtype);
            stmt.setString(4, dpo_view);
            
            rs = stmt.executeQuery() ;
            if (rs.next())
            {
                dpo_icon_ind  = rs.getBoolean("dpo_icon_ind");
                dpo_title_ind = rs.getBoolean("dpo_title_ind");
                dpo_lan_ind = rs.getBoolean("dpo_lan_ind");
                dpo_desc_ind = rs.getBoolean("dpo_desc_ind");
                dpo_instruct_ind = rs.getBoolean("dpo_instruct_ind");
                dpo_eff_start_datetime_ind = rs.getBoolean("dpo_eff_start_datetime_ind");
                dpo_eff_end_datetime_ind = rs.getBoolean("dpo_eff_end_datetime_ind");
                dpo_difficulty_ind = rs.getBoolean("dpo_difficulty_ind");
                dpo_time_limit_ind = rs.getBoolean("dpo_time_limit_ind");
                dpo_suggested_time_ind = rs.getBoolean("dpo_suggested_time_ind");
                dpo_duration_ind = rs.getBoolean("dpo_duration_ind");
                dpo_max_score_ind = rs.getBoolean("dpo_max_score_ind");
                dpo_pass_score_ind = rs.getBoolean("dpo_pass_score_ind");
                dpo_pgr_start_datetime_ind = rs.getBoolean("dpo_pgr_start_datetime_ind");
                dpo_pgr_complete_datetime_ind = rs.getBoolean("dpo_pgr_complete_datetime_ind");
                dpo_pgr_last_acc_datetime_ind = rs.getBoolean("dpo_pgr_last_acc_datetime_ind");
                dpo_pgr_attempt_nbr_ind = rs.getBoolean("dpo_pgr_attempt_nbr_ind");
                dpo_max_usr_attempt_ind = rs.getBoolean("dpo_max_usr_attempt_ind");
                dpo_instructor_ind = rs.getBoolean("dpo_instructor_ind");
                dpo_organization_ind = rs.getBoolean("dpo_organization_ind");
                dpo_moderator_ind = rs.getBoolean("dpo_moderator_ind");
                dpo_evt_datetime_ind = rs.getBoolean("dpo_evt_datetime_ind");
                dpo_evt_venue_ind = rs.getBoolean("dpo_evt_venue_ind");
                dpo_status_ind = rs.getBoolean("dpo_status_ind");
            }else {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to get display option.");
            }
            
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    public static Hashtable getDisplayOption(Connection con) throws SQLException{
    	Hashtable hash = new Hashtable();
        String SQL =  "  SELECT "
        	+ "			dpo_view "	
        	+ "			,dpo_res_subtype "
            + "         ,dpo_icon_ind "
            + "         ,dpo_title_ind "
            + "         ,dpo_lan_ind "
            + "         ,dpo_desc_ind "
            + "         ,dpo_instruct_ind "
            + "         ,dpo_eff_start_datetime_ind "
            + "         ,dpo_eff_end_datetime_ind "
            + "         ,dpo_difficulty_ind "
            + "         ,dpo_time_limit_ind "
            + "         ,dpo_suggested_time_ind "
            + "         ,dpo_duration_ind "
            + "         ,dpo_max_score_ind "
            + "         ,dpo_pass_score_ind "
            + "         ,dpo_pgr_start_datetime_ind "
            + "         ,dpo_pgr_complete_datetime_ind "
            + "         ,dpo_pgr_last_acc_datetime_ind "
            + "         ,dpo_pgr_attempt_nbr_ind "
            + "         ,dpo_max_usr_attempt_ind "
            + "         ,dpo_instructor_ind "
            + "         ,dpo_organization_ind "
            + "         ,dpo_moderator_ind "
            + "         ,dpo_evt_datetime_ind "
            + "         ,dpo_evt_venue_ind "
            + "         ,dpo_status_ind "
            + "      FROM DisplayOption WHERE "
            + "         dpo_res_type = ?  "
            + "         AND dpo_view = ?  ";
            
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setString(index++, "MOD");
        stmt.setString(index++, "LRN_READ");
        ResultSet rs = stmt.executeQuery() ;
        while (rs.next())
        {   dbDisplayOption dp = new dbDisplayOption(); 
        	dp.dpo_view = rs.getString("dpo_view");
        	dp.dpo_res_subtype = rs.getString("dpo_res_subtype");
        	dp.dpo_icon_ind  = rs.getBoolean("dpo_icon_ind");
        	dp.dpo_title_ind = rs.getBoolean("dpo_title_ind");
        	dp.dpo_lan_ind = rs.getBoolean("dpo_lan_ind");
        	dp.dpo_desc_ind = rs.getBoolean("dpo_desc_ind");
        	dp.dpo_instruct_ind = rs.getBoolean("dpo_instruct_ind");
        	dp.dpo_eff_start_datetime_ind = rs.getBoolean("dpo_eff_start_datetime_ind");
        	dp.dpo_eff_end_datetime_ind = rs.getBoolean("dpo_eff_end_datetime_ind");
        	dp.dpo_difficulty_ind = rs.getBoolean("dpo_difficulty_ind");
        	dp.dpo_time_limit_ind = rs.getBoolean("dpo_time_limit_ind");
        	dp.dpo_suggested_time_ind = rs.getBoolean("dpo_suggested_time_ind");
        	dp.dpo_duration_ind = rs.getBoolean("dpo_duration_ind");
        	dp.dpo_max_score_ind = rs.getBoolean("dpo_max_score_ind");
        	dp.dpo_pass_score_ind = rs.getBoolean("dpo_pass_score_ind");
        	dp.dpo_pgr_start_datetime_ind = rs.getBoolean("dpo_pgr_start_datetime_ind");
        	dp.dpo_pgr_complete_datetime_ind = rs.getBoolean("dpo_pgr_complete_datetime_ind");
        	dp.dpo_pgr_last_acc_datetime_ind = rs.getBoolean("dpo_pgr_last_acc_datetime_ind");
        	dp.dpo_pgr_attempt_nbr_ind = rs.getBoolean("dpo_pgr_attempt_nbr_ind");
        	dp.dpo_max_usr_attempt_ind = rs.getBoolean("dpo_max_usr_attempt_ind");
        	dp.dpo_instructor_ind = rs.getBoolean("dpo_instructor_ind");
        	dp.dpo_organization_ind = rs.getBoolean("dpo_organization_ind");
        	dp.dpo_moderator_ind = rs.getBoolean("dpo_moderator_ind");
        	dp.dpo_evt_datetime_ind = rs.getBoolean("dpo_evt_datetime_ind");
        	dp.dpo_evt_venue_ind = rs.getBoolean("dpo_evt_venue_ind");
        	dp.dpo_status_ind = rs.getBoolean("dpo_status_ind");
        	hash.put(dp.dpo_res_subtype, dp);
        }
        rs.close();
        stmt.close();
    	return hash;
    }
    
    public String getViewAsXML(Connection con)
        throws qdbException
    {
        String xml = "";
        
        if(dpo_view != null) {            
            get(con);
            xml  = "<display>" + dbUtils.NEWL;
            xml += formatXML();
            xml += "</display>" + dbUtils.NEWL;
        }
        return xml;
    }
    public String getViewAsXML() {
        String xml = "";
        if(dpo_view != null) {            
            xml  = "<display>" + dbUtils.NEWL;
            xml += formatXML();
            xml += "</display>" + dbUtils.NEWL;
        }
        return xml;
    }
    /*
    public String allViewAsXML(Connection con)
        throws qdbException
    {
        dbDisplayOption dopView1 = new dbDisplayOption();
        //dbDisplayOption dopView2 = new dbDisplayOption();
        
        dopView1.dpo_res_id = dpo_res_id;
        dopView1.dpo_res_type = dpo_res_type;
        dopView1.dpo_res_subtype = dpo_res_subtype;
        dopView1.dpo_view = VIEW_LIST;
        dopView1.get(con);
        
        dopView2.dpo_res_id = dpo_res_id; 
        dopView2.dpo_res_type = dpo_res_type; 
        dopView2.dpo_res_subtype = dpo_res_subtype;
        dopView2.dpo_view = VIEW_DETAIL; 
        dopView2.get(con);
        
        String xml = "";
        xml  = "<display>" + dbUtils.NEWL;
        xml += dopView1.formatXML();
        //xml += dopView2.formatXML();
        xml += "</display>" + dbUtils.NEWL;
        
        return xml;
    }
*/
    public String asXML(Connection con)
    {
        String xml = "";
        xml  = "<display>" + dbUtils.NEWL;
        xml += formatXML();
        xml += "</display>" + dbUtils.NEWL;
        return xml;
    }
    
    private String formatXML()
    {
        String xml = "";
        xml  = "<option res_id=\"" + dpo_res_id + "\" view=\"" + dpo_view + "\">" + dbUtils.NEWL;
        
        xml += "<general icon=\"" + dpo_icon_ind + "\" title=\"" + dpo_title_ind 
              + "\" language=\"" + dpo_lan_ind + "\" desc=\"" + dpo_desc_ind 
              + "\" instruct=\"" + dpo_instruct_ind 
              + "\" instructor=\"" + dpo_instructor_ind 
              + "\" moderator=\"" + dpo_moderator_ind 
              + "\" organization=\"" + dpo_organization_ind 
              + "\" status=\"" + dpo_status_ind + "\"/>" + dbUtils.NEWL;
        
        xml += "<progress difficulty=\"" + dpo_difficulty_ind + "\" duration=\"" + dpo_duration_ind + "\" attempt_nbr=\"" + dpo_pgr_attempt_nbr_ind
              + "\" max_usr_attempt=\"" + dpo_max_usr_attempt_ind + "\" time_limit=\"" + dpo_time_limit_ind + "\" suggested_time=\"" + dpo_suggested_time_ind 
              + "\" max_score=\"" + dpo_max_score_ind + "\" pass_score=\"" + dpo_pass_score_ind + "\"/>" + dbUtils.NEWL;
              
        xml += "<datetime eff_start=\"" + dpo_eff_start_datetime_ind + "\" eff_end=\"" + dpo_eff_end_datetime_ind 
              + "\" pgr_start=\"" + dpo_pgr_start_datetime_ind + "\" pgr_complete=\"" + dpo_pgr_complete_datetime_ind 
              + "\" pgr_last_acc=\"" + dpo_pgr_last_acc_datetime_ind + "\"/>" + dbUtils.NEWL;
        
        xml += "<event datetime=\"" + dpo_evt_datetime_ind + "\" venue=\"" + dpo_evt_venue_ind + "\"/>" + dbUtils.NEWL;

        xml += "</option>" + dbUtils.NEWL;
        
        return xml;
    }
}

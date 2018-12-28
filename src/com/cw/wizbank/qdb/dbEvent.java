package com.cw.wizbank.qdb;

import java.sql.*;
import com.cw.wizbank.util.cwSysMessage;

public class dbEvent extends dbModule {

    public long evt_res_id;
    public Timestamp evt_datetime;
    public String evt_venue;
    
    public static String [] eventTypes = {"EXM", "VST", "ORI", "GAG"}; 
    
    
    public static boolean isEventType(String mod_type) {
        int i;
        boolean result=false;
        
        if(mod_type != null) {
            for(i=0;i<eventTypes.length;i++) {
                if(mod_type.equalsIgnoreCase(eventTypes[i])) {
                    result = true;
                    break;
                }
            }
        }        
        return result;
    }
    
    
    public void ins(Connection con, loginProfile prof)
        throws qdbException
    {
        try {
            // calls dbModule.ins()
            super.ins(con, prof);
                       
            // if ok
            mod_res_id = res_id;
            evt_res_id = res_id;
            
            PreparedStatement stmt = con.prepareStatement(
                " INSERT INTO Event "
                + " ( evt_res_id "
                + " , evt_datetime "
                + " , evt_venue "
                + " ) "
                + " VALUES (?,?,?) "); 
            
            stmt.setLong(1, evt_res_id);
            stmt.setTimestamp(2, evt_datetime);
            stmt.setString(3, evt_venue);
            
            if (stmt.executeUpdate()!= 1) {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to insert Event.");
            }
            stmt.close();
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }          
    }
    

    public String asXML(Connection con, loginProfile prof, String dpo_view, String ssoXml)
        throws qdbException, cwSysMessage
    {
            String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
            result += "<module id=\""+ res_id + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date +"\">" + dbUtils.NEWL;
            
            // author's information
            result += prof.asXML() + dbUtils.NEWL;
            result += ssoXml;

            result += dbResourcePermission.aclAsXML(con,res_id,prof);
            // Module Header 
            result += getModHeader(con, prof);
            //result += getEventAsXML(con);
            result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkh_id);
            result += getDisplayOption(con, dpo_view);
            result += "<body>" + dbUtils.NEWL;

            result += "</body>" + dbUtils.NEWL;
            result += "</module>"; 
            
            return result;
    }


    public String getEventAsXML(Connection con) throws qdbException, cwSysMessage {
        try {
            String xml = "";
            String SQL = "Select evt_datetime, evt_venue from Event "
                       + "Where evt_res_id = ? ";
                       
            get(con);
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, evt_res_id);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next() == true) {
                xml = "<event>" + dbUtils.NEWL
                    + "<datetime>" + rs.getTimestamp("evt_datetime") + "</datetime>" + dbUtils.NEWL
                    + "<venue>" + rs.getString("evt_venue") + "</venue>" + dbUtils.NEWL
                    + "</event>" + dbUtils.NEWL; 
            }
            else {
                stmt.close();
                throw new qdbException("Cannot find the event record. evt_res_id = " + evt_res_id);
            }
            
            stmt.close();
            return xml;
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }
    

    public void initialize(dbModule dbmod) {
        evt_res_id = dbmod.mod_res_id;
        
        mod_res_id = dbmod.mod_res_id;
        mod_type = dbmod.mod_type;
        mod_max_score = dbmod.mod_max_score;
        mod_pass_score = dbmod.mod_pass_score;
        mod_instruct = dbmod.mod_instruct;
        mod_max_attempt = dbmod.mod_max_attempt;
        mod_max_usr_attempt = 1;
        mod_score_ind = dbmod.mod_score_ind;
        mod_score_reset = dbmod.mod_score_reset;
        mod_in_eff_start_datetime = dbmod.mod_in_eff_start_datetime;
        mod_in_eff_end_datetime = dbmod.mod_in_eff_end_datetime;
        mod_usr_id_instructor = dbmod.mod_usr_id_instructor;
        tkh_id = dbmod.tkh_id;

        res_id = dbmod.res_id;
        res_lan = dbmod.res_lan;
        res_title = dbmod.res_title;
        res_desc = dbmod.res_desc;
        res_type = RES_TYPE_MOD; // override
        res_subtype = dbmod.res_subtype; // override
        res_annotation = dbmod.res_annotation;
        res_format = dbmod.res_format;
        res_difficulty = dbmod.res_difficulty;
        res_privilege = dbmod.res_privilege;
        res_usr_id_owner = dbmod.res_usr_id_owner;
        res_tpl_name = dbmod.res_tpl_name;
        res_mod_res_id_test = dbmod.res_mod_res_id_test;
        res_status = dbmod.res_status;
        res_upd_user = dbmod.res_upd_user;
        res_upd_date = dbmod.res_upd_date;
        res_src_type = dbmod.res_src_type;
        res_src_link = dbmod.res_src_link;        
        //res_url = dbmod.res_url;
        //res_filename = dbmod.res_filename;
        res_duration = dbmod.res_duration;
        
        res_instructor_name = dbmod.res_instructor_name;
        res_instructor_organization = dbmod.res_instructor_organization;
    }


    public void get(Connection con)
            throws qdbException, cwSysMessage
    {
        try {
            mod_res_id = evt_res_id;
            super.get(con);
      
            PreparedStatement stmt = con.prepareStatement(
            "SELECT  evt_datetime, " 
            + " evt_venue "
            + " FROM Event "
            + " where evt_res_id =?");
            
            // set the values for prepared statements
            stmt.setLong(1, evt_res_id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                evt_datetime = rs.getTimestamp("evt_datetime"); 
                evt_venue = rs.getString("evt_venue");
            }
            else{
                stmt.close();
                throw new qdbException( "No data for event. id = " + evt_res_id );
            }
            
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public void upd(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try {  
            // check User Right
            //if (!dbResourcePermission.hasPermission(con, evt_res_id, prof,
            //                                dbResourcePermission.RIGHT_WRITE)) {
            //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
            //}            
            
            super.upd(con, prof);
            //super.updChild(con, files);
            
        
            String upd_mSQL = "UPDATE Event SET "
                + "   evt_datetime = ? "
                + " , evt_venue = ? "
                + " where evt_res_id = ? "; 
        
            PreparedStatement stmt = con.prepareStatement(upd_mSQL);
        
            stmt.setTimestamp(1, evt_datetime);
            stmt.setString(2, evt_venue); 
            stmt.setLong(3, evt_res_id);
            
            if (stmt.executeUpdate()!= 1) {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to update Event.");
            }
            
            stmt.close();
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }            
    }

    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        try {      
           
            PreparedStatement stmt = con.prepareStatement(
                "DELETE From Event where evt_res_id=?");

            stmt.setLong(1, evt_res_id);
           
            if( stmt.executeUpdate()!= 1) {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to delete Event. No such record.");
            } else {
                //super.delChild(con);
                super.del(con, prof);
            }      
            stmt.close();
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    //delete without checking privilege
    public void aeDel(Connection con)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        try {      
           
            PreparedStatement stmt = con.prepareStatement(
                "DELETE From Event where evt_res_id=?");

            stmt.setLong(1, evt_res_id);
           
            if( stmt.executeUpdate()!= 1) {
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to delete Event. No such record.");
            } else {
                //super.delChild(con);
                super.del(con);
            }      
            stmt.close();
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

}
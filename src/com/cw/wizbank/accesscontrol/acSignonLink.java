package com.cw.wizbank.accesscontrol;

import java.util.Vector;
import java.sql.*;

import com.cw.wizbank.util.*;

public class acSignonLink{
    public long slk_id;
    public long slk_ste_id_owner;
    public String slk_link_title;
    public String slk_base_url;
    public long slk_ste_id;
    public String slk_usr_id;
    public String slk_usr_role;
    public String slk_label_lan;
    public String slk_url_success;
    public String slk_window_target;


    public Vector getByRoot(Connection con, long root_ent_id) throws SQLException{
        Vector vtSignonlink = new Vector();
        String SQL = "SELECT slk_id, slk_ste_id_owner, slk_link_title, slk_base_url, slk_ste_id, slk_usr_id, slk_usr_role, slk_label_lan, slk_url_success, slk_window_target "
                    + "FROM acSignonLink "
                    + "WHERE slk_ste_id_owner = ? OR slk_ste_id_owner is null "
                    + "ORDER BY slk_ste_id_owner ";
                    
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, root_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            acSignonLink link = new acSignonLink();
            link.slk_id = rs.getLong("slk_id");
            link.slk_ste_id_owner = rs.getLong("slk_ste_id_owner"); 
            link.slk_link_title = rs.getString("slk_link_title");
            link.slk_base_url = rs.getString("slk_base_url");
            link.slk_ste_id = rs.getLong("slk_ste_id"); 
            link.slk_usr_id = rs.getString("slk_usr_id");
            link.slk_usr_role = rs.getString("slk_usr_role");
            link.slk_label_lan = rs.getString("slk_label_lan");
            link.slk_url_success = rs.getString("slk_url_success");
            link.slk_window_target = rs.getString("slk_window_target");
            vtSignonlink.addElement(link);
        }
        stmt.close();
        return vtSignonlink;
    }
    /*
        get by id
    */
    public void get(Connection con) throws SQLException{
        Vector vtSignonlink = new Vector();
        String SQL = "SELECT slk_id, slk_ste_id_owner, slk_link_title, slk_base_url, slk_ste_id, slk_usr_id, slk_usr_role, slk_label_lan, slk_url_success, slk_window_target "
                    + "FROM acSignonLink "
                    + "WHERE slk_id = ? ";
                    
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, slk_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()){
            slk_id = rs.getLong("slk_id"); 
            slk_ste_id_owner = rs.getLong("slk_ste_id_owner"); 
            slk_link_title = rs.getString("slk_link_title");
            slk_base_url = rs.getString("slk_base_url");
            slk_ste_id = rs.getLong("slk_ste_id"); 
            slk_usr_id = rs.getString("slk_usr_id");
            slk_usr_role = rs.getString("slk_usr_role");
            slk_label_lan = rs.getString("slk_label_lan");
            slk_url_success = rs.getString("slk_url_success");
            slk_window_target = rs.getString("slk_window_target");
        }else{
            throw new SQLException("signon link not found, slk_id:" + slk_id);    
        }
        stmt.close();
    }
    
    public StringBuffer asXML(){
        StringBuffer xml = new StringBuffer();
        xml.append("<link ");
        xml.append("id=\"").append(slk_id);
        xml.append("\" link_title=\"").append(cwUtils.esc4XML(slk_link_title));
        xml.append("\" base_url=\"").append(slk_base_url);
        xml.append("\" site_id=\"").append(cwUtils.escZero(slk_ste_id_owner));
        xml.append("\" usr_id=\"").append(slk_usr_id);
        xml.append("\" usr_role=\"").append(cwUtils.esc4XML(cwUtils.escNull(slk_usr_role)));
        xml.append("\" label_lan=\"").append(cwUtils.esc4XML(slk_label_lan));
        xml.append("\" url_success=\"").append(cwUtils.esc4XML(slk_url_success));
        xml.append("\" window_target=\"").append(cwUtils.esc4XML(slk_window_target));
        xml.append("\" />").append(cwUtils.NEWL);
        
        return xml;                
    }

    public StringBuffer getSignonLinkLstAsXML(Connection con, long root_ent_id) throws SQLException{
        Vector vtSignonlink = getByRoot(con, root_ent_id);
        StringBuffer xml = new StringBuffer();
        xml.append("<signon_link_list site_id_owner=\"").append(root_ent_id).append("\">");
        
        for (int i=0; i<vtSignonlink.size();i++){
            acSignonLink link = (acSignonLink)vtSignonlink.elementAt(i);    
            xml.append(link.asXML());
        }
        xml.append("</signon_link_list>");
        
        return xml;
    }
    
} 
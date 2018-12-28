package com.cw.wizbank.db.view;

import java.sql.*;
import com.cw.wizbank.db.DbKmNodeActnHistory;
import com.cw.wizbank.util.*;

/**
A database class to get result related to kmNode
*/
public class ViewKmNodeActn {

    public long         action_id;
    public long         node_id;
    
    // Variables for formating the action history xml
    // START
    public long         modified_node_id;
    public String       type;
    public String       title;
    public String       usr_id;
    public String       usr_display_bil;
    public Timestamp    update_timestamp;
    // optional
    public String       version;
    public String       title_org;
    public String       comments;
    public String       ancestor_list;
    // END
    
    
    /**
    Trigger an action and save the record
    */
    public void save(Connection con) throws SQLException, cwSysMessage   {
        DbKmNodeActnHistory nah = new DbKmNodeActnHistory();
        nah.nah_nod_id = node_id;
        nah.nah_type = type;
        nah.nah_update_timestamp = update_timestamp;
        nah.nah_xml = formatXML();
        nah.ins(con);
        
        action_id = nah.nah_id;
        
    }

    /**
    Format the action history xml
    */
    private String formatXML() {
        
        // For optional fields, replace the "null" to ""
        if (title_org == null) {
            title_org = title;
        }
        if (version == null) {
            version = new String();
        }
        if (comments == null) {
            comments = new String();
        }
        if (ancestor_list == null) {
            ancestor_list = new String();
        }
        
        boolean bTitleChanged = true;
        if (title_org.equals(title)) {
            bTitleChanged = false;
        }
        
        StringBuffer xml = new StringBuffer();
        xml.append("<detail node_id=\"").append(modified_node_id)
            .append("\" type=\"").append(type).append("\">")
            .append("<title changed=\"").append(bTitleChanged).append("\">")
            .append("<fr>").append(cwUtils.esc4XML(title_org)).append("</fr>")
            .append("<to>").append(cwUtils.esc4XML(title)).append("</to>")
            .append("</title>")
            .append("<update usr_id=\"").append(usr_id)
            .append("\" display_bil=\"").append(cwUtils.esc4XML(usr_display_bil))
            .append("\" timestamp=\"").append(update_timestamp)
            .append("\"/>")
            .append("<version>").append(version).append("</version>")
            .append("<comments>").append(cwUtils.esc4XML(comments)).append("</comments>")
            .append("<ancestor_list>").append(ancestor_list).append("</ancestor_list>")
            .append("</detail>");
        return xml.toString();

    }



}


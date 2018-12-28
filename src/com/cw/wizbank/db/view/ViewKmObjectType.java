package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.aeTemplate;
import com.cw.wizbank.db.DbKmObjectType;

/**
A database class to represent table kmObjectType 
*/
public class ViewKmObjectType {

    public DbKmObjectType dbObjectType;
    public Vector vTemplate;

/*
    public long      tpl_id;
    public long      tpl_ttp_id;
    public String    tpl_title;
    public String    tpl_xml;
    public long      tpl_owner_ent_id;
    public Timestamp tpl_create_timestamp;
    public String    tpl_create_usr_id;
    public Timestamp tpl_upd_timestamp;
    public String    tpl_upd_usr_id;
*/    
    //non-database field
    //store the tpl_ttp_id's logical values (e.g. WORKFLOW, APPNFORM, ITEM)
    public String    tpl_type;
    
    public static final String SQL_GET_OTY_TPL = 
        " SELECT tpl_id, tpl_ttp_id, tpl_title, tpl_xml, tpl_owner_ent_id, " +
        "        tpl_create_timestamp, tpl_create_usr_id, " +
        "        tpl_upd_timestamp, tpl_upd_usr_id, ttp_title " +
        " FROM kmObjectTypeTemplate, aeTemplate, aeTemplateType " +
        " WHERE ott_oty_code = ? " +
        " AND ott_oty_owner_ent_id = ? " +
        " AND ott_tpl_id = tpl_id " +
        " AND ott_ttp_id = ttp_id ";
    
    /**
    Perform a deep clone on this ViewKmObjectType
    @return a new image of this ViewKmObjectType
    */
    public ViewKmObjectType deepClone() {
        ViewKmObjectType imageObjectType = new ViewKmObjectType();
        
        if(this.dbObjectType == null) {
            imageObjectType.dbObjectType = null;
        } else {
            imageObjectType.dbObjectType = this.dbObjectType.deepClone();
        }
        
        if(this.vTemplate == null) {
            imageObjectType.vTemplate = null;
        } else {
            imageObjectType.vTemplate = new Vector();
            for(int i=0; i<vTemplate.size(); i++) {
                imageObjectType.vTemplate.addElement(((aeTemplate)this.vTemplate.elementAt(i)).deepClone());
            }
        }
        return imageObjectType;
    }
    
    
    /**
    Get all the Object Types and their Templates from database
    @param con Connection to database
    @return Vector of ViewKmObjectType
    */
    public static Vector getAllObjectTypes(Connection con) throws SQLException {
        //1. get all DbKmObjectType
        Vector vDbObjectType = DbKmObjectType.getAllObjectTypes(con);

        //2. for each kmObjectType, get their Templates
        //   and store into Vector vViewOty
        Vector vViewObjectType = new Vector();
        for(int i=0; i<vDbObjectType.size(); i++) {
            ViewKmObjectType viewObjectType = new ViewKmObjectType();
            viewObjectType.dbObjectType = (DbKmObjectType) vDbObjectType.elementAt(i);
            viewObjectType.vTemplate = new Vector();
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(SQL_GET_OTY_TPL);
                stmt.setString(1, viewObjectType.dbObjectType.oty_code);
                stmt.setLong(2, viewObjectType.dbObjectType.oty_owner_ent_id);
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    aeTemplate tpl = new aeTemplate();
                    tpl.tpl_id = rs.getLong("tpl_id");
                    tpl.tpl_ttp_id = rs.getLong("tpl_ttp_id");
                    tpl.tpl_title = rs.getString("tpl_title");
                    tpl.tpl_owner_ent_id = rs.getLong("tpl_owner_ent_id");
                    tpl.tpl_create_timestamp = rs.getTimestamp("tpl_create_timestamp");
                    tpl.tpl_create_usr_id = rs.getString("tpl_create_usr_id");
                    tpl.tpl_upd_timestamp = rs.getTimestamp("tpl_upd_timestamp");
                    tpl.tpl_upd_usr_id = rs.getString("tpl_upd_usr_id");
                    tpl.tpl_type = rs.getString("ttp_title");
                    tpl.tpl_xml = cwSQL.getClobValue(rs, "tpl_xml");
                    viewObjectType.vTemplate.addElement(tpl);
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
            
            vViewObjectType.addElement(viewObjectType);
        }
        
        return vViewObjectType;
    }
    

}
package com.cw.wizbank.ae.db;

import java.util.Vector;
import java.sql.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.db.sql.SqlStatements;

/**
Database layer for table aeItemType
*/
public class DbCatalogItemType{

    private static final String ALL_TYPES = "ALL_TYPES";

    /**
    database field
    */
    public long cit_cat_id;

    /**
    database field
    */
    public long cit_ity_owner_ent_id;

    /**
    database field
    */
    public String cit_ity_id;
    
    /**
    database field
    */
    public String cit_create_usr_id;
    
    /**
    database field
    */
    public Timestamp cit_create_timestamp;

    /**
    Insert this object into aeCatalogItemType<Br>
    Pre-define variable:
    <ul>
    <li>cit_cat_id
    <li>cit_ity_owner_ent_id
    <li>cit_ity_id
    </ul>
    */
    public void ins(Connection con, String usr_id, Timestamp cur_time) throws SQLException {
        
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        this.cit_create_timestamp = cur_time;
        this.cit_create_usr_id = usr_id;
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_cit);
        int index = 1;
        stmt.setLong(index++, this.cit_cat_id);
        stmt.setLong(index++, this.cit_ity_owner_ent_id);
        if(this.cit_ity_id.equalsIgnoreCase(ALL_TYPES)) {
            stmt.setNull(index++, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(index++, this.cit_ity_id);
        }
        stmt.setString(index++, this.cit_create_usr_id);
        stmt.setTimestamp(index++, this.cit_create_timestamp);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    /**
    Delete from aeCatalogItemType base on input cat_id<BR>
    */
    public static void delByCatalog(Connection con, long cat_id) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_cit_by_cat);
        stmt.setLong(1, cat_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    /**
    Get assigned item types of input cat_id
    @return Vector of cit_ity_id (String)
    */
    public static Vector getCatalogItemType(Connection con, long cat_id) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_cit_by_cat);
        stmt.setLong(1, cat_id);
        ResultSet rs = stmt.executeQuery();
        Vector v_itemType = new Vector();
        while(rs.next()) {
            v_itemType.addElement(rs.getString("cit_ity_id"));
        }
        stmt.close();
        return v_itemType;
    }

    /**
    Get XML of assigned item types of input cat_id
    */
    public static String getCatalogItemTypeAsXML(Connection con, long cat_id) throws SQLException {
        
        Vector v_itemType = getCatalogItemType(con, cat_id);
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<assigned_item_type_list>");
        for(int i=0; i<v_itemType.size(); i++) {
            xmlBuf.append("<item_type id=\"").append((String)v_itemType.elementAt(i)).append("\"/>");
        }
        xmlBuf.append("</assigned_item_type_list>");
        return xmlBuf.toString();
    }

}
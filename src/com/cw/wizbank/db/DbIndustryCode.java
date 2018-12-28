package com.cw.wizbank.db;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

/**
database layer of table IndustryCode
*/
public class DbIndustryCode {

    public long idc_ent_id;
    
    public String idc_display_bil;
    
    public long idc_ent_id_root;
    
    public String idc_type;

    public static String IDC_TYPE_ROOT = "ROOT";
    
    /**
    get the root industry code object<BR>
    pre-define variables:<BR>
    <ul>
    <li>idc_ent_id_root
    </ul>
    */
    public void getRootIndustryCode(Connection con) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_root_idc);
        int index = 1;
        stmt.setLong(index++, this.idc_ent_id_root);
        stmt.setString(index++, IDC_TYPE_ROOT);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.idc_ent_id = rs.getLong("idc_ent_id");
            this.idc_display_bil = rs.getString("idc_display_bil");
            this.idc_type = IDC_TYPE_ROOT;
        }
        stmt.close();
        return;
    }

    /**
    insert a record into dbTableName<BR>
    pre-define variable:<BR>
    <ul>
    <li>idc_ent_id
    <li>idc_display_bil
    <li>idc_root_ent_id
    </ul>
    */
    
    public void ins(Connection con) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_idc);
        int index = 1;
        stmt.setLong(index++, this.idc_ent_id);
        stmt.setString(index++, this.idc_display_bil);
        stmt.setLong(index++, this.idc_ent_id_root);
        
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    /**
    Update IndustryCode
    pre-define variable:
    <ul>
    <li>idc_display_bil
    <li>idc_ent_id
    </ul>
    */
    public void upd(Connection con, String usr_id) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_idc);
        int index = 1;
        stmt.setString(index++, this.idc_display_bil);
        stmt.setLong(index++, this.idc_ent_id);
        
        stmt.executeUpdate();
        stmt.close();
        
        con.commit();
        EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
        entityfullpath.updateChildFullPath(con, this.idc_ent_id, this.idc_display_bil, dbEntityRelation.ERN_TYPE_IDC_PARENT_IDC);
        
        return;
    }
    
    /**
    delete IndustryCode
    pre-defined variable:<BR>
    <ul>
    <li>idc_ent_id
    </ul>
    */
    public void del(Connection con, String ent_delete_usr_id) throws SQLException {

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_idc);
        int index = 1;
        stmt.setLong(index++, this.idc_ent_id);
        
        stmt.executeUpdate();
        stmt.close();
        
        dbEntityRelation dbER = new dbEntityRelation();
        dbER.ern_ancestor_ent_id = idc_ent_id;
        dbER.ern_type = dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC;
        dbER.delAsAncestor(con, ent_delete_usr_id);
        return;
    }
 
    /**
    pre-define variables:<BR>
    <ul>
    <li>idc_ent_id
    </ul>
    */
    public void get(Connection con) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_idc);
        int index = 1;
        stmt.setLong(index++, this.idc_ent_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.idc_display_bil = rs.getString("idc_display_bil");
            this.idc_ent_id_root = rs.getLong("idc_ent_id_root");
            this.idc_type = rs.getString("idc_type");
        }
        rs.close();
        stmt.close();
        return;
    }

    /*
    *   pre-define ent_id_root, then search with display
    */
    public void search(Connection con, String s_display_bil) throws SQLException{

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_search_idc);
        int index = 1;
        stmt.setLong(index++, this.idc_ent_id_root);
		stmt.setString(index++, "%" + s_display_bil.toLowerCase() + "%");
        
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            this.idc_ent_id = rs.getLong("idc_ent_id");
            this.idc_display_bil = rs.getString("idc_display_bil");
        }
        
        stmt.close();
        return;
    }
    
    public static Vector getIndCodeEntIds(Connection con, long ent_id) throws SQLException, cwException {
        Vector vec = new Vector();
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_my_industry_code);
        stmt.setLong(1, ent_id);
        stmt.setString(2, dbEntityRelation.ERN_TYPE_USR_FOCUS_IDC);
        stmt.setBoolean(3, true);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            long code_ent_id = rs.getLong("ern_ancestor_ent_id");
            
            if (code_ent_id != 0) {
                vec.addElement(new Long(code_ent_id));   
            }
        }
        stmt.close();

        return vec;
    }    
    
    // get EntId by ste ugr id and the organisation
    public boolean getBySteUid(Connection con, String ent_ste_uid) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_indusrty_by_ste_uid);
        stmt.setString(1, ent_ste_uid);
        stmt.setLong(2, idc_ent_id_root);
                                
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            idc_ent_id = rs.getLong("idc_ent_id");
            idc_display_bil = rs.getString("idc_display_bil");
            idc_ent_id_root = rs.getLong("idc_ent_id_root");
            idc_type = rs.getString("idc_type");            
        }
        stmt.close();
        return true;
    }

}
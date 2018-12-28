package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.ae.db.view.ViewItemTemplate;

/**
Database layer for table aeItemType
*/
public class DbItemTemplate{

    /**
    database field
    */
    public long itp_itm_id;

    /**
    database field
    */
    public long itp_ttp_id;
    
    /**
    database field
    */
    public long itp_tpl_id;
    
    /**
    database field
    */
    public Timestamp itp_create_timestamp;

    /**
    database field
    */
    public String itp_create_usr_id;

    /**
    insert this ItemTemplate into Table<BR>
    Pre-define variables:
    <ul>
    <li>itp_itm_id
    <li>itp_tpl_id
    </ul>
    */
    public void ins(Connection con, String templateType, String usr_id, Timestamp cur_time) 
        throws SQLException {
        
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        this.itp_create_timestamp = cur_time;
        this.itp_create_usr_id = usr_id;
        
        ViewItemTemplate viItmTpl = new ViewItemTemplate();
        viItmTpl.itemId = this.itp_itm_id;
        viItmTpl.templateId = this.itp_tpl_id;
        //viItmTpl.createTimestamp = this.itp_create_timestamp;
        //viItmTpl.createUserId = this.itp_create_usr_id;
        viItmTpl.templateType = templateType;
        viItmTpl.insItemTemplate(con, this.itp_create_usr_id, this.itp_create_timestamp);
        return;
    }

    /**
    insert this ItemTemplate into Table<BR>
    Pre-define variables:
    <ul>
    <li>itp_itm_id
    <li>itp_tpl_id
    <li>itp_ttp_id
    </ul>
    */
    public void ins(Connection con, String usr_id, Timestamp cur_time) 
        throws SQLException {
        
        if(cur_time == null) {
            cur_time = cwSQL.getTime(con);
        }
        this.itp_create_timestamp = cur_time;
        this.itp_create_usr_id = usr_id;

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_item_template);
        int index = 1;
        stmt.setLong(index++, this.itp_itm_id);
        stmt.setLong(index++, this.itp_tpl_id);
        stmt.setLong(index++, this.itp_ttp_id);
        stmt.setString(index++, this.itp_create_usr_id);
        stmt.setTimestamp(index++, this.itp_create_timestamp);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    /**
     * delete all item usage of this item
     */
    public static void delByItem(Connection con, long itemId) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_itm_all_tpl);
        int col = 1;
        stmt.setLong(col++, itemId);
        stmt.executeUpdate();
        stmt.close();
    }
}
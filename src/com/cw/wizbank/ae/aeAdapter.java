package com.cw.wizbank.ae;

import java.io.IOException;
import java.sql.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.qdb.qdbException;

public class aeAdapter {    
    public static int insOpenItem(Connection con, long ent_id, long app_id, String create_usr_id, String type, float fee, String ccy, Timestamp due_datetime, String itm_code, String itm_title, String src, String ref, String accountType)
        throws cwException, SQLException, qdbException
    {
        int id = 0;
        ResultSet rs;
        PreparedStatement stmt = con.prepareStatement("INSERT INTO aeAppnOpenItem (aoi_app_id, aoi_src, aoi_ref) VALUES (?, ?, ?)");
            
        stmt.setLong(1, app_id);
        stmt.setString(2, src);
        stmt.setString(3, ref);
            
        if (stmt.executeUpdate()!= 1) {
        	if(stmt!=null)stmt.close();
            throw new cwException("com.cw.wizbank.ae.aeAdapter.insOpenItem: Fail to insert record to aeAppnOpenItem");
        } else {
            stmt.close();
                
            aeOpenItem openItem = new aeOpenItem(con, ent_id, accountType);
            openItem.oim_ccy = ccy;
            openItem.oim_org_amt = fee;
            openItem.oim_os_amt = fee;
            openItem.oim_due_datetime = due_datetime;
            openItem.oim_type = type;
            openItem.oim_src = src;
            openItem.oim_ref = ref;
            openItem.oim_desc = itm_code + " " + itm_title;
            openItem.oim_create_usr_id = create_usr_id;
            openItem.oim_upd_usr_id = create_usr_id;
            
            id = openItem.ins();
        }

        return id;
    }
    
    public static void updAppn(Connection con, String upd_usr_id, String src, String ref)
        throws cwException, cwSysMessage, SQLException, qdbException, IOException
    {
        ResultSet rs;
        PreparedStatement stmt = con.prepareStatement("SELECT aoi_app_id FROM aeAppnOpenItem WHERE aoi_src = ? AND aoi_ref = ?");

        stmt.setString(1, src);
        stmt.setString(2, ref);
        rs = stmt.executeQuery();
            
        if (rs.next()) {
            aeQueueManager.updAppn(con, rs.getLong("aoi_app_id"), upd_usr_id);
        } else {
            throw new cwException("com.cw.wizbank.ae.aeAdapter.updAppn: can't retrieve record from aeAppnOpenItem where aoi_src = " + src + " and aoi_ref = " + ref);
        }
        stmt.close();
    }
}
package com.cw.wizbank.ae;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.db.DbAppnApprovalList;

public class aeAppnActnHistory {
    public long      aah_id;
    public long      aah_app_id;
    public long      aah_process_id;
    public String    aah_fr;
    public String    aah_to;
    public String    aah_verb;
    public long      aah_action_id;
    public String    aah_create_usr_id;
    public Timestamp aah_create_timestamp; 
    public String    aah_upd_usr_id;
    public Timestamp aah_upd_timestamp;
    public long      status_id;
    public String    status;

    public String aah_actn_type;
    
    public aeAppnActnHistory() {
        ;
    }


    public long ins(Connection con) 
        throws SQLException, qdbException
    {
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer bufSQL = new StringBuffer();

        bufSQL.append("INSERT INTO aeAppnActnHistory ");
        bufSQL.append("(aah_app_id, aah_process_id, aah_fr, aah_to, aah_verb, aah_action_id, ");
        bufSQL.append("aah_create_usr_id, aah_create_timestamp, aah_upd_usr_id, aah_upd_timestamp, aah_actn_type)");
        bufSQL.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        stmt = con.prepareStatement(bufSQL.toString(), PreparedStatement.RETURN_GENERATED_KEYS);        
        stmt.setLong     (1, aah_app_id);
        stmt.setLong     (2, aah_process_id);
        stmt.setString   (3, aah_fr);
        stmt.setString   (4, aah_to);
        stmt.setString   (5, aah_verb);
        stmt.setLong     (6, aah_action_id);
        stmt.setString   (7, aah_create_usr_id);
        stmt.setTimestamp(8, aah_create_timestamp);
        stmt.setString   (9, aah_upd_usr_id);
        stmt.setTimestamp(10, aah_upd_timestamp);
        stmt.setString   (11, aah_actn_type);
     
        if (stmt.executeUpdate()!= 1) {
            stmt.close();
            throw new qdbException("com.cw.wizbank.ae.aeAppnActnHistory.ins: Fail to insert action to DB");
        }
        aah_id = cwSQL.getAutoId(con, stmt, "aeAppnActnHistory", "aah_id");        
        stmt.close();
        return aah_id;
    }
    
/*    public void upd(Connection con)
        throws SQLException, qdbException
    {
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer bufSQL = new StringBuffer();
        
        bufSQL.append("UPDATE aeAppnActnHistory SET ");
        bufSQL.append("aah_upd_usr_id = ?, aah_upd_timestamp = ? WHERE aah_id = ?");

        aah_upd_timestamp = dbUtils.getTime(con); 
        stmt = con.prepareStatement(bufSQL.toString());        
        stmt.setString   (1, aah_upd_usr_id);
        stmt.setTimestamp(2, aah_upd_timestamp);
        stmt.setLong     (3, aah_id);
 
        if (stmt.executeUpdate()!= 1) {
            con.rollback();
        }

        stmt.close();
    }*/
    
    public void get(Connection con) 
        throws SQLException, qdbException
    {
        PreparedStatement stmt;
        ResultSet rs;

        stmt = con.prepareStatement("SELECT * FROM aeAppnActnHistory WHERE aah_id = ?");
        stmt.setLong(1, aah_id);
        rs = stmt.executeQuery();
                
        if (rs.next()) {
            aah_id               = rs.getLong     ("aah_id");
            aah_app_id           = rs.getLong     ("aah_app_id");
            aah_process_id       = rs.getLong     ("aah_process_id");
            aah_fr               = rs.getString   ("aah_fr");
            aah_to               = rs.getString   ("aah_to");
            aah_verb             = rs.getString   ("aah_verb");
            aah_action_id        = rs.getLong     ("aah_action_id");
            aah_create_usr_id    = rs.getString   ("aah_create_usr_id");
            aah_create_timestamp = rs.getTimestamp("aah_create_timestamp");
            aah_upd_usr_id       = rs.getString   ("aah_upd_usr_id");
            aah_upd_timestamp    = rs.getTimestamp("aah_upd_timestamp");
            aah_actn_type        = rs.getString   ("aah_actn_type");
        } else {
            throw new qdbException("com.cw.wizbank.ae.aeAppnHistory.get: Fail to retrieve action history from DB");
        }
            
        stmt.close();
    }
    
    private static final String sql_get_latest_actn = 
        " select * from aeAppnActnHistory " +
        " where aah_app_id = ? " +
        " and (aah_process_id <> 0 OR (aah_process_id = 0 AND aah_verb is not null)) " +
        " and aah_id = (select max(aah_id) from aeAppnActnHistory " +
        "               where aah_app_id = ? " +
        "               and (aah_process_id <> 0 OR (aah_process_id = 0 AND aah_verb is not null))) ";
        
//        " order by aah_create_timestamp desc ";
    
    public void getLatestHistory(Connection con) throws SQLException {
        
        PreparedStatement stmt = con.prepareStatement(sql_get_latest_actn);
        stmt.setLong(1, this.aah_app_id);
        stmt.setLong(2, this.aah_app_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            aah_id               = rs.getLong     ("aah_id");
            aah_app_id           = rs.getLong     ("aah_app_id");
            aah_process_id       = rs.getLong     ("aah_process_id");
            aah_fr               = rs.getString   ("aah_fr");
            aah_to               = rs.getString   ("aah_to");
            aah_verb             = rs.getString   ("aah_verb");
            aah_action_id        = rs.getLong     ("aah_action_id");
            aah_create_usr_id    = rs.getString   ("aah_create_usr_id");
            aah_create_timestamp = rs.getTimestamp("aah_create_timestamp");
            aah_upd_usr_id       = rs.getString   ("aah_upd_usr_id");
            aah_upd_timestamp    = rs.getTimestamp("aah_upd_timestamp");
        } 
        stmt.close();
        return;
    }
    
    public String contentAsXML(Connection con)
        throws qdbException
    {
        StringBuffer result = new StringBuffer();
        dbRegUser user = new dbRegUser();
        
        user.usr_id = aah_upd_usr_id;
        user.usr_ent_id = user.getEntId(con);
        
        try {
            user.get(con);
        } catch (qdbException e) {
            ;   
        }
        
        result.append("<action process_id=\"");
        result.append(aah_process_id);
        result.append("\" id=\"");
        result.append(aah_id);
        result.append("\" fr=\"");
        result.append(dbUtils.esc4XML(aah_fr));
        result.append("\" to=\"");
        result.append(dbUtils.esc4XML(aah_to));
        result.append("\" verb=\"");
        result.append(dbUtils.esc4XML(aah_verb));
        // result.append("\" actn_type=\"");
        //result.append(dbUtils.esc4XML(aah_actn_type));
        result.append("\" upd_timestamp=\"");
        result.append(aah_upd_timestamp);
        result.append("\" type=\"");
        result.append((aah_actn_type != null) ? aah_actn_type : "");
        result.append("\">");
        result.append(dbUtils.NEWL);
/*        result.append("<action_by id=\"");
        result.append(user.usr_id);
        result.append("\" ent_id=\"");
        result.append(user.usr_ent_id);
        result.append("\" last_name=\"");
        result.append(dbUtils.esc4XML(user.usr_last_name_bil));
        result.append("\" first_name=\"");
        result.append(dbUtils.esc4XML(user.usr_first_name_bil));
        result.append("\">");
        result.append(dbUtils.NEWL);*/
        result.append("<display_name>");
        result.append(dbUtils.esc4XML(user.usr_display_bil));
        result.append("</display_name>");
        try {
            String approval_role = getApprovalRole(con);
            if(approval_role != null) {
                result.append("<approval_role>");
                result.append(approval_role);
                result.append("</approval_role>");
            }
        } catch(SQLException sqle) {
            throw new qdbException(sqle.getMessage());
        }
        result.append(dbUtils.NEWL);
/*        result.append("</action_by>");
        result.append(dbUtils.NEWL);*/
        result.append("</action>");
        result.append(dbUtils.NEWL);
        
        return result.toString();
    }
    
    public static long[] getActnList(Connection con , long app_id)
        throws SQLException, qdbException
    {
        long[] lst = null;
        Long id;
        Vector v_lst = new Vector();
        PreparedStatement stmt;
        ResultSet rs;

        stmt = con.prepareStatement("SELECT aah_id FROM aeAppnActnHistory WHERE aah_app_id = ? ORDER BY aah_process_id, aah_id");
        stmt.setLong(1, app_id);
        rs = stmt.executeQuery();
            
        while (rs.next()) {
            id = new Long(rs.getLong("aah_id"));
            v_lst.addElement(id);
        }
        rs.close();
        stmt.close();
        if (v_lst != null && v_lst.size() != 0) {
            lst = new long[v_lst.size()];
                
            for (int i=0; i<v_lst.size(); i++) {
                id = (Long)v_lst.elementAt(i);
                lst[i] = id.longValue();
            }
        }
            
        return lst;
    }    
    
    private static final String sql_del_aah_by_app_id = 
        " Delete From aeAppnActnHistory Where aah_app_id = ? ";
    public static void delByAppn(Connection con, long aah_app_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_del_aah_by_app_id);
        stmt.setLong(1, aah_app_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    public static void updUpdateTimestamp(Connection con, long app_id, String update_usr_id, Timestamp upd_timestamp) throws SQLException{
        String sql = "UPDATE aeAppnActnHistory SET aah_upd_timestamp = ? , aah_upd_usr_id = ? WHERE aah_app_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setTimestamp(1, upd_timestamp);
        stmt.setString(2, update_usr_id);
        stmt.setLong(3, app_id);
        stmt.executeUpdate();        
        stmt.close();        
    }

    public String getApprovalRole(Connection con) throws SQLException {
        return DbAppnApprovalList.getAppnActnApprovalRole(con, this.aah_id);
    }    
}

package com.cw.wizbank.ae;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;

public class aeAppnCommHistory {
    public long      ach_id;
    public long      ach_app_id;
    public long      ach_aah_id;
    public String    ach_content;
    public Timestamp ach_create_timestamp;
    public String    ach_create_usr_id;
    public Timestamp ach_upd_timestamp;
    public String    ach_upd_usr_id;
    
    public aeAppnCommHistory() {
        ;
    }

    public void ins(Connection con)
        throws SQLException, qdbException
    {
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer bufSQL = new StringBuffer();

        bufSQL.append("INSERT INTO aeAppnCommHistory ");
        bufSQL.append("(ach_app_id, ach_content, ");
/*
        if (ach_aah_id != 0) {
            bufSQL.append("ach_aah_id = ").append(ach_aah_id).append(", ");
        }
*/        
        if (ach_aah_id != 0) {
            bufSQL.append("ach_aah_id, ");
        }

        bufSQL.append("ach_create_timestamp, ach_create_usr_id, ach_upd_timestamp, ach_upd_usr_id)");
        bufSQL.append(" VALUES (?, ?, ?, ?, ?, ?");
        if(ach_aah_id != 0) {
            bufSQL.append(", ?");
        }
        bufSQL.append(")");

        stmt = con.prepareStatement(bufSQL.toString());
        int index = 1;
        stmt.setLong     (index++, ach_app_id);
        stmt.setString   (index++, ach_content);
        if (ach_aah_id != 0) {
            stmt.setLong     (index++, ach_aah_id);
        }
        stmt.setTimestamp(index++, ach_create_timestamp);
        stmt.setString   (index++, ach_create_usr_id);
        stmt.setTimestamp(index++, ach_upd_timestamp);
        stmt.setString   (index++, ach_upd_usr_id);
     
        if (stmt.executeUpdate()!= 1) {
            stmt.close();
            throw new qdbException("com.cw.wizbank.ae.aeAppnCommHistory.ins: Fail to insert comment history to DB");
        }
                
        stmt.close();
    }
    
/*    public void upd(Connection con)
        throws SQLException, qdbException    
    {
        PreparedStatement stmt;
        ResultSet rs;
        StringBuffer bufSQL = new StringBuffer();
        
        bufSQL.append("UPDATE aeAppnCommHistory SET ");
        bufSQL.append("ach_upd_timestamp = ?, ach_upd_usr_id = ? WHERE ach_id = ?");

        ach_upd_timestamp = dbUtils.getTime(con); 
        stmt = con.prepareStatement(bufSQL.toString());        
        stmt.setTimestamp(1, ach_upd_timestamp);
        stmt.setString   (2, ach_upd_usr_id);
        stmt.setLong     (3, ach_id);
 
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

        stmt = con.prepareStatement("SELECT ach_id, ach_app_id, ach_aah_id, ach_content, ach_create_timestamp, ach_create_usr_id, ach_upd_timestamp, ach_upd_usr_id FROM aeAppnCommHistory WHERE ach_id = ?");
        stmt.setLong(1, ach_id);
        rs = stmt.executeQuery();
                
        if (rs.next()) {
            ach_id               = rs.getLong     ("ach_id");
            ach_app_id           = rs.getLong     ("ach_app_id");
            ach_aah_id           = rs.getLong     ("ach_aah_id");
            ach_content          = rs.getString   ("ach_content");
            ach_create_timestamp = rs.getTimestamp("ach_create_timestamp");
            ach_create_usr_id    = rs.getString   ("ach_create_usr_id");
            ach_upd_timestamp    = rs.getTimestamp("ach_upd_timestamp");
            ach_upd_usr_id       = rs.getString   ("ach_upd_usr_id");
        } else {
            throw new qdbException("com.cw.wizbank.ae.aeAppnCommHistory.get: Fail to retrieve comment history from DB");   
        }
            
        stmt.close();
    }    

    public String contentAsXML(Connection con)
        throws qdbException
    {
        StringBuffer result = new StringBuffer();
        dbRegUser user = new dbRegUser();
        
        user.usr_id = ach_upd_usr_id;
        user.usr_ent_id = user.getEntId(con);
        
        try {
            user.get(con);
        } catch (qdbException e) {
            ;   
        }
        
        result.append("<comment id=\"");
        result.append(ach_id);
        result.append("\" upd_timestamp=\"");
        result.append(ach_upd_timestamp);
        result.append("\" action_id=\"");
        result.append(ach_aah_id);        
        result.append("\">");
        result.append(dbUtils.NEWL);
/*        result.append("<comment_by id=\"");
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
        result.append(dbUtils.NEWL);
/*        result.append("</comment_by>");
        result.append(dbUtils.NEWL);*/
        result.append("<content>");
        result.append(dbUtils.esc4XML(ach_content));
        result.append("</content>");
        result.append(dbUtils.NEWL);
        result.append("</comment>");
        result.append(dbUtils.NEWL);
        
        return result.toString();
    }
    
    public static long[] getCommList(Connection con , long app_id)
        throws SQLException, qdbException
    {
        long[] lst = null;
        Long id;
        Vector v_lst = new Vector();
        PreparedStatement stmt;
        ResultSet rs;

        stmt = con.prepareStatement("SELECT ach_id FROM aeAppnCommHistory WHERE ach_app_id = ? ORDER BY ach_id");
        stmt.setLong(1, app_id);
        rs = stmt.executeQuery();
            
        while (rs.next()) {
            id = new Long(rs.getLong("ach_id"));
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
    
    private static final String sql_del_ach_by_app_id = 
        " Delete From aeAppnCommHistory Where ach_app_id = ? ";
    public static void delByAppn(Connection con, long ach_app_id) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(sql_del_ach_by_app_id);
        stmt.setLong(1, ach_app_id);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    private static final String SQL_UPD_ACH
        = " UPDATE aeAppnCommHistory SET ach_content = ?, ach_upd_timestamp = ?, ach_upd_usr_id = ? "
        + " WHERE ach_id = ? AND ach_upd_timestamp = ? ";
        
    
    /**
     * Update the comment history record (the content) represented by this object from database
     * @param con Connection to database
     * @throws SQLException
     * @throws cwSysMessage
     */
    public void upd(Connection con) throws SQLException, cwSysMessage, qdbException {
        PreparedStatement stmt = null;
        Timestamp curTime = cwSQL.getTime(con);
        
        //give an empty string
        if(ach_content == null) {
            ach_content = "";
        }
        
        // if ach_id doesn't exist, insert a new record
        if(ach_id == 0) {
            ach_upd_timestamp = curTime;
            ach_create_timestamp = curTime;
            ach_create_usr_id = ach_upd_usr_id;
            ins(con); 
        } 
        else {
            try {
                stmt = con.prepareStatement(SQL_UPD_ACH);
                stmt.setString(1, ach_content);
                stmt.setTimestamp(2, curTime);
                stmt.setString(3, ach_upd_usr_id);
                stmt.setLong(4, ach_id);
                stmt.setTimestamp(5, ach_upd_timestamp);
                if(stmt.executeUpdate() == 0) {
                    throw new cwSysMessage("GEN006");
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }
    }
    
    /**
     * get an XML showing details of the applicant, course, application action and the comment
     * if ach_id > 0, get this record from database
     * else if ach_aah_id > 0, get the comment of that action, if that action contains no comment, 
     * the returned XML will not have the comment details in it. 
     * @param con Connection to database
     * @return XML showing details of the applicant, course, application action and the comment
     * @throws SQLException
     */
    public String getAppnCommentAsXML(Connection con) throws SQLException, qdbException, cwSysMessage {
        
        StringBuffer xmlBuf = new StringBuffer(1024);
        aeAppnActnHistory aah = null;

        if(ach_id > 0) {
            //if a comment id is given
            // get the comment details
            get(con);
        } else if (ach_aah_id > 0) {
            //if only action id is given
            //try to the the action's comment
            getByActnId(con);
        } else {
            throw new qdbException("Either commention id or action id should be given");
        }

        //get the application action details
        if(ach_aah_id > 0) {
            aah = new aeAppnActnHistory();
            aah.aah_id = ach_aah_id;
            aah.get(con);
        }
        
        //get the application id
        //if this comment can be found from database, ach_app_id should not be null
        //if this comment cannot be found, the application id should be got from action history
        long app_id = (ach_app_id > 0) ? ach_app_id : aah.aah_app_id;
        
        //get the application details
        aeApplication app = new aeApplication();
        app.app_id = app_id;
        app.get(con);
            
        //begin to generate XML
        xmlBuf.append("<application app_id=\"").append(app.app_id).append("\">")
              .append(app.applicantAsXML())
              .append("<item id=\"").append(app.app_itm_id).append("\">")
              .append(aeItem.getNavAsXML(con, app.app_itm_id))
              .append("</item>");
        if(aah!=null) {
           xmlBuf.append(aah.contentAsXML(con));
        }
        if(ach_id > 0) {
            xmlBuf.append(contentAsXML(con));
        }
        xmlBuf.append("</application>");
        return xmlBuf.toString();
    }

    private static final String SQL_GET_BY_ACTION_ID
        = " SELECT ach_id, ach_app_id, ach_content, ach_create_timestamp, ach_create_usr_id, ach_upd_timestamp, ach_upd_usr_id "
        + " FROM aeAppnCommHistory WHERE ach_aah_id = ? ";   
        
    public void getByActnId(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_BY_ACTION_ID);
            stmt.setLong(1, ach_aah_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ach_id               = rs.getLong     ("ach_id");
                ach_app_id           = rs.getLong     ("ach_app_id");
                ach_content          = rs.getString   ("ach_content");
                ach_create_timestamp = rs.getTimestamp("ach_create_timestamp");
                ach_create_usr_id    = rs.getString   ("ach_create_usr_id");
                ach_upd_timestamp    = rs.getTimestamp("ach_upd_timestamp");
                ach_upd_usr_id       = rs.getString   ("ach_upd_usr_id");
            }            
        }finally {
            if(stmt!=null) stmt.close();
        }
    }
}

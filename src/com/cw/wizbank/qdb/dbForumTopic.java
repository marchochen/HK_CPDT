package com.cw.wizbank.qdb;

import java.util.*;
import java.sql.*;
import javax.servlet.http.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class dbForumTopic {
    public static final int PAGE_SIZE = 10;

    public long      fto_id;
    public long      fto_res_id;
    public String    fto_title;
    public String    fto_usr_id;
    public Timestamp fto_create_datetime;
    public Timestamp fto_last_post_datetime;
    public String    usr_display_bil;

    public dbForumTopic() {
        fto_id = 0;
    }

    public void ins(Connection con, loginProfile prof, dbForumMessage msg)
        throws qdbException, qdbErrMessage
    {
        // check User Right
        /*
        if (prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) {
            dbModule mod = new dbModule();
            mod.mod_res_id = fto_res_id;
            mod.checkModifyPermission(con, prof);
        } else {
            long cosId  = dbModule.getCosId(con,fto_res_id);
            if (!dbResourcePermission.hasPermission(con, cosId, prof,
                                        dbResourcePermission.RIGHT_EXECUTE)) {
                throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_EXECUTE_MSG);
            }
        }
        */

//        if (!dbResourcePermission.hasPermission(con, fto_res_id, prof,
//                                        dbResourcePermission.RIGHT_READ)) {
//            long cosId  = dbModule.getCosId(con, fto_res_id);
//            if (!dbResourcePermission.hasPermission(con, cosId, prof,
//                                        dbResourcePermission.RIGHT_READ)) {
//                throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
//            }
//        }

        try {
            fto_create_datetime = dbUtils.getTime(con);

            PreparedStatement stmt = con.prepareStatement(
                " INSERT INTO ForumTopic "
                + " ( fto_res_id "
                + " , fto_title "
                + " , fto_usr_id "
                + " , fto_create_datetime "
                + " , fto_last_post_datetime "
                + " ) "
                + " VALUES (?,?,?,?,?) ", PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, fto_res_id);
            stmt.setString(2, fto_title);
            stmt.setString(3, prof.usr_id);
            stmt.setTimestamp(4, fto_create_datetime);
            if(fto_last_post_datetime == null) {
                stmt.setTimestamp(5, null);
            }else{
                stmt.setTimestamp(5, fto_last_post_datetime);
            }
            int stmtResult=stmt.executeUpdate();
            if ( stmtResult!=1)                            
            {            
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to create Forum Topic.");
            } else {
            	fto_id = cwSQL.getAutoId(con, stmt, "ForumTopic", "fto_id");
                stmt.close();
            }
/*            msg.fmg_fto_id = fto_id;
            msg.fmg_fto_res_id = fto_res_id;
            msg.ins(con, prof, fto_create_datetime);*/
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        // check User Right
        /*
        try {
            dbModule mod = new dbModule();
            mod.mod_res_id = fto_res_id;
            mod.checkModifyPermission(con, prof);
        }
        catch(qdbErrMessage e) {
            throw new qdbErrMessage("ACL002");
        }
        */
//        if (!dbResourcePermission.hasPermission(con, fto_res_id, prof,
//                                        dbResourcePermission.RIGHT_WRITE)) {
//            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
//        }

        try {
            dbForumMessage.delAllTopicMessages(con, prof, fto_res_id, fto_id);

            PreparedStatement stmt = con.prepareStatement(
                "DELETE From ForumTopic where fto_id = ?");

            stmt.setLong(1, fto_id);

            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {            
                con.rollback();
                throw new qdbException("Fails to delete Forum Topic");
            }
            

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void get(Connection con)
            throws qdbException ,cwSysMessage
    {
        try {
/* Tune by Stanley
            PreparedStatement stmt = con.prepareStatement(
            "SELECT "
            + " fto_res_id, "
            + " fto_title, "
            + " fto_usr_id, "
            + " fto_create_datetime "
            + " FROM ForumTopic "
            + " WHERE fto_id = ? ");*/

            PreparedStatement stmt = con.prepareStatement("select fto_title, fto_create_datetime, fto_res_id, fto_usr_id, usr_display_bil from ForumTopic, RegUser where fto_id = ? and fto_usr_id = usr_id");

            // set the values for prepared statements
            stmt.setLong(1, fto_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                fto_res_id = rs.getLong("fto_res_id");
                fto_title = rs.getString("fto_title");
                fto_usr_id = rs.getString("fto_usr_id");
                fto_create_datetime= rs.getTimestamp("fto_create_datetime");
                usr_display_bil = rs.getString("usr_display_bil");
            }
            else
            {
                //throw new qdbException( "No data for forum. id = " + fto_id );
                stmt.close();
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Forum ID = " + fto_id );
            }

            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static long[] getAllTopicIDs(Connection con, long res_id, String sortCol, String sortOrder)
        throws qdbException
    {
        try {
            Vector topicLst = new Vector();
            int len;
            Long element;
            StringBuffer SQLBuf = new StringBuffer(256);
            
            SQLBuf.append("SELECT fto_id FROM ForumTopic where fto_res_id = ? ");
            
            if (sortCol != null) {
                SQLBuf.append(" ORDER BY ").append(sortCol);
            } else {
                SQLBuf.append(" ORDER BY ").append("fto_title");
            }
            
            if (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) {
                SQLBuf.append(" DESC");
            } else {
                SQLBuf.append(" ASC");
            }
            
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                topicLst.addElement(new Long(rs.getLong("fto_id")));
            }

            stmt.close();

            len = topicLst.size();

            if (len > 0) {
                long[] lst = new long[len];

                for (int i=0; i<len; i++) {
                    element = (Long) topicLst.elementAt(i);
                    lst[i] = element.longValue();
                }

                return lst;
            } else {
                return null;
            }
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public static void delAllTopics(Connection con, loginProfile prof, long res_id)
        throws qdbException, qdbErrMessage
    {
        // check User Right
        // Check at the module level
        //if (!dbResourcePermission.hasPermission(con, res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}

        try {
            dbForumMessage.delAllForumMessages(con, prof, res_id);

            PreparedStatement stmt = con.prepareStatement(
                "DELETE From ForumTopic where fto_res_id = ?");

            stmt.setLong(1, res_id);
            stmt.executeUpdate();
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

/* Tune by Stanley
    public String contentWithoutMsgsAsXML(Connection con, loginProfile prof, int MARK_MSG)
        throws qdbException
    {
        String result = "";
        long[] msgLst = dbForumMessage.getAllMsgIDs(con, fto_id, null);

        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);

        result += "<topic id=\"" + fto_id + "\" createBy=\"" + dbUtils.esc4XML(user.usr_display_bil) +
                  "\" createDatetime=\"" + fto_create_datetime +
                  "\" numMsg=\"";

        if (msgLst != null) {
            result += msgLst.length;
        } else {
            result += "0";
        }

        if (MARK_MSG == 1) {
            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);
        }

        result += "\">" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(fto_title) + "</title>" + dbUtils.NEWL;
        result += "</topic>" + dbUtils.NEWL;

        return result;
    }*/

    public static String contentWithoutMsgsAsXML(Connection con, loginProfile prof, int MARK_MSG, Vector topic_lst)
        throws qdbException, SQLException
    {
        StringBuffer result = new StringBuffer();
        Hashtable msg_count_hash = dbForumMessage.getMsgCount(con, cwUtils.vector2list(topic_lst));
        StringBuffer SQLBuf = new StringBuffer(256);

        for(int i=0;i<topic_lst.size();i++){
        	Long fto_id = (Long)topic_lst.elementAt(i);
        	Long count_l = (Long)msg_count_hash.get(fto_id);
            long count = 0;
            if (count_l != null) {
                count = count_l.longValue();
            }
            
            SQLBuf = new StringBuffer(256);
            SQLBuf.append("select fto_id, fto_title, fto_create_datetime, fto_last_post_datetime, usr_display_bil, usr_nickname from ForumTopic, RegUser where fto_id = " + fto_id + " and fto_usr_id = usr_id ");
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            	result.append("<topic id=\"").append(fto_id).append("\" createBy=\"").append(dbUtils.esc4XML(rs.getString("usr_display_bil")));
            	result.append("\" nickname=\"").append(dbUtils.esc4XML(rs.getString("usr_nickname")));
                result.append("\" createDatetime=\"").append(rs.getTimestamp("fto_create_datetime"));
                
                result.append("\" lastPostDatetime=\"").append(cwUtils.escNull(rs.getTimestamp("fto_last_post_datetime")));
                result.append("\" numMsg=\"").append(count);
                          
                if (MARK_MSG == 1) { 
                    result.append("\" numMsgRead=\"").append(dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id.longValue(), prof.usr_id));            
                }
                          
                result.append("\">");
                result.append("<title>").append(dbUtils.esc4XML(rs.getString("fto_title"))).append("</title>");
                result.append("</topic>");
            }
            stmt.close();
        }
        
        return result.toString();
    }        

    public static String contentWithoutMsgsAsXML(Connection con, loginProfile prof, int MARK_MSG, String topic_lst, String sortCol, String sortOrder)
        throws qdbException, SQLException
    {
        StringBuffer result = new StringBuffer();
        Hashtable msg_count_hash = dbForumMessage.getMsgCount(con, topic_lst);
        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append("select fto_id, fto_title, fto_create_datetime, fto_last_post_datetime, usr_ent_id, usr_nickname, usr_display_bil from ForumTopic, RegUser where fto_id in " + topic_lst + " and fto_usr_id = usr_id ");
        
        if (sortCol != null) {
            SQLBuf.append(" order by ").append(sortCol);
        } else {
            SQLBuf.append(" order by fto_title");
        }
        
        if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
            SQLBuf.append(" desc");
        } else {
            SQLBuf.append(" asc");
        }
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        ResultSet rs = stmt.executeQuery();
        String createBy = null;
        while (rs.next()) {
            long fto_id = rs.getLong("fto_id");
            Long count_l = (Long)msg_count_hash.get(new Long(fto_id));
            long count = 0;
            
            if (count_l != null) {
                count = count_l.longValue();
            }
            
            createBy = rs.getString("usr_nickname");
            if (createBy == null || createBy.length() == 0) {
            	createBy = rs.getString("usr_display_bil");
            }
            
            result.append("<topic id=\"").append(fto_id).append("\" createBy=\"").append(dbUtils.esc4XML(createBy));
            result.append("\" usr_ent_id=\"").append(rs.getLong("usr_ent_id"));
            result.append("\" createDatetime=\"").append(rs.getTimestamp("fto_create_datetime"));
            result.append("\" lastPostDatetime=\"").append(cwUtils.escNull(rs.getTimestamp("fto_last_post_datetime")));            
            result.append("\" numMsg=\"").append(count);

            if (MARK_MSG == 1) {
                result.append("\" numMsgRead=\"").append(dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id));
            }

            result.append("\">");
            result.append("<title>").append(dbUtils.esc4XML(rs.getString("fto_title"))).append("</title>");
            result.append("</topic>");
        }

        stmt.close();

        return result.toString();
    }

/* Tune by Stanley (This function is retired)
    public String contentWithMsgsAsXML(Connection con, HttpSession sess, loginProfile prof, boolean unthread, int MARK_MSG, String order, int page, int msg_length)
        throws qdbException ,cwSysMessage
    {
        long[] msgLst = null;
        String result = "";
        String sess_type = (String)sess.getAttribute(dbForum.FOR_TYPE);
        String sess_cmd = (String)sess.getAttribute(dbForum.FOR_CMD);
        Long tempId = (Long)sess.getAttribute(dbForum.FOR_ID);
        long id = 0;

        if (tempId != null) {
            id = tempId.longValue();
        }

        if (unthread && sess_type != null && sess_type.equals(dbForum.FOR_MSG) &&
            sess_cmd != null && sess_cmd.equals(dbForum.FOR_VIEW) &&
            page != 0 && id == fto_id) {
            msgLst = (long[])sess.getAttribute(dbForum.FOR_MSG_RESULT);
        } else {
            msgLst = dbForumMessage.getAllMsgIDs(con, fto_id, order);
            sess.setAttribute(dbForum.FOR_TYPE, dbForum.FOR_MSG);
            sess.setAttribute(dbForum.FOR_CMD, dbForum.FOR_VIEW);
            sess.setAttribute(dbForum.FOR_ID, new Long(fto_id));

            if (msgLst != null) {
                sess.setAttribute(dbForum.FOR_MSG_RESULT, msgLst);     
            }
        }

        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);

        if (page == 0) {
            page = 1;
        }

        result += "<topic id=\"" + fto_id + "\" createBy=\"" + dbUtils.esc4XML(user.usr_display_bil) +
                  "\" createDatetime=\"" + fto_create_datetime +
                  "\" page_size=\"" + PAGE_SIZE +
                  "\" cur_page=\"" + page +
                  "\" numMsg=\"";

        if (msgLst != null) {
            result += msgLst.length;
        } else {
            result += "0";
        }

        if (MARK_MSG == 1) {
            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);
        }

        result += "\">" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(fto_title) + "</title>" + dbUtils.NEWL;

        if (unthread) {
            dbForumMessage msg;

            if (msgLst != null) {
                int count = (page-1) * PAGE_SIZE;

                for (int i=count; i<msgLst.length && i<count+PAGE_SIZE; i++) {
                    msg = new dbForumMessage();
                    msg.fmg_id = msgLst[i];
                    msg.get(con);
                    result += msg.contentAsXML(con, prof, MARK_MSG);
                }
            }
        } else {
                //Get All Messages in the Topic
                dbForumMessage msg = new dbForumMessage();
                try{
                    result += msg.getAllMsgContent(con, fto_id, msg_length, prof);
                }catch(SQLException e) {
                    throw new qdbException ( e.getMessage() );
                }
        }

        result += "</topic>" + dbUtils.NEWL;

        return result;
    }*/

    public static String contentWithMsgsAsXML(Connection con, HttpSession sess, loginProfile prof, long topic_id, boolean unthread, int MARK_MSG, String order, int page, int page_size, int msg_length)
        throws qdbException ,cwSysMessage, SQLException
    {
        /*long[] msgLst = null;*/
        Hashtable hMessages = null;
        Vector vMessages = null;
        StringBuffer result = new StringBuffer();

        if (unthread) {
            String sess_type = (String)sess.getAttribute(dbForum.FOR_TYPE);
            String sess_cmd = (String)sess.getAttribute(dbForum.FOR_CMD);
            Long tempId = (Long)sess.getAttribute(dbForum.FOR_ID);
            long id = 0;

            if (tempId != null) {
                id = tempId.longValue();                
            }

            if (sess_type != null && sess_type.equals(dbForum.FOR_MSG) && 
                sess_cmd != null && sess_cmd.equals(dbForum.FOR_VIEW) &&
                page != 0 && id == topic_id) {
                hMessages = (Hashtable)sess.getAttribute(dbForum.FOR_MSG_RESULT);
            } else {
                hMessages = dbForumMessage.getUnthreadedMessages(con, topic_id, msg_length, prof);
                // If any message found, save it to the session
                if (hMessages != null) {
                    sess.setAttribute(dbForum.FOR_TYPE, dbForum.FOR_MSG);
                    sess.setAttribute(dbForum.FOR_CMD, dbForum.FOR_VIEW);
                    sess.setAttribute(dbForum.FOR_ID, new Long(topic_id));    
                    sess.setAttribute(dbForum.FOR_MSG_RESULT, hMessages); 
                }
            }
            if(hMessages != null) {
                vMessages = (Vector)hMessages.get(dbForumMessage.CHAINED_MESSAGES);
            }
        }else {
            // Unthread mode, clear the session
            // User could see the changes when using the thread mode again
            sess.removeAttribute(dbForum.FOR_TYPE);
            sess.removeAttribute(dbForum.FOR_CMD);
            sess.removeAttribute(dbForum.FOR_ID);
            sess.removeAttribute(dbForum.FOR_MSG_RESULT);
        }

        PreparedStatement stmt = con.prepareStatement("select fto_id, fto_title, fto_create_datetime, usr_ent_id, usr_display_bil, usr_nickname from ForumTopic, RegUser where fto_id = ? and fto_usr_id = usr_id");
        stmt.setLong(1, topic_id);
        ResultSet rs = stmt.executeQuery();

        if (page == 0) {
            page = 1;
        }
        
        String createBy = null;
        if (rs.next()) {
            long fto_id = rs.getLong("fto_id");
            createBy = rs.getString("usr_nickname");
            if (createBy == null || createBy.length() == 0) {
            	createBy = rs.getString("usr_display_bil");
            }
            result.append("<topic id=\"").append(fto_id).append("\" createBy=\"").append(dbUtils.esc4XML(createBy));
            result.append("\" usr_ent_id=\"").append(rs.getLong("usr_ent_id"));
            result.append("\" createDatetime=\"").append(rs.getTimestamp("fto_create_datetime"));
            result.append("\" page_size=\"").append(page_size);
            result.append("\" cur_page=\"").append(page);
            // fix error :no msg exist ; add by richard
            if (vMessages != null) {
                result.append("\" numMsg=\"").append(vMessages.size());
            } else {
                result.append("\" numMsg=\"").append(0);
            }
            //result.append("\" numMsg=\"").append(msgLst.length);

            if (MARK_MSG == 1) {
                result.append("\" numMsgRead=\"").append(dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id));
            }

            result.append("\">");
            result.append("<title>").append(dbUtils.esc4XML(rs.getString("fto_title"))).append("</title>");
        } else {
        	stmt.close();
            throw new qdbException("No such topic id: " + topic_id);
        }

        stmt.close();

        if (unthread) {
            if (hMessages != null) {
                Hashtable userTable = (Hashtable)hMessages.get(dbForumMessage.USER_TABLE);
                int count = (page-1) * page_size;

                for (int i=count; i<vMessages.size() && i<count+page_size; i++) {
                    dbForumMessage msg = (dbForumMessage) vMessages.elementAt(i);
                    String usr_display_bil = (String)userTable.get(msg.fmg_usr_id);
                    result.append(msg.contentAsXML(con, prof, usr_display_bil, MARK_MSG, ""));
                }
            }
        } else {
            //Get All Messages in the Topic
            dbForumMessage msg = new dbForumMessage();
            try{
                result.append(msg.getAllMsgContent(con, topic_id, msg_length, prof));
            }catch(SQLException e) {
                throw new qdbException ( e.getMessage() );
            }
        }

        result.append("</topic>");

        return result.toString();
    }

    public String contentAsXML(Connection con, loginProfile prof, long[] msgLst, int MARK_MSG, int page)
        throws qdbException ,cwSysMessage
    {
        String result = "";

/* Tune by Stanley
        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);*/

        if (page == 0) {
            page = 1;
        }

/* Tune by Stanley
        result += "<topic id=\"" + fto_id + "\" createBy=\"" + dbUtils.esc4XML(user.usr_display_bil) +*/
        result += "<topic id=\"" + fto_id + "\" createBy=\"" + dbUtils.esc4XML(usr_display_bil) +
                  "\" page_size=\"" + PAGE_SIZE +
                  "\" cur_page=\"" + page +
                  "\" createDatetime=\"" + fto_create_datetime +
                  "\" numMsg=\"";

        if (msgLst != null) {
            result += msgLst.length;
        } else {
            result += "0";
        }

        if (MARK_MSG == 1) {
            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);
        }

        result += "\">" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(fto_title) + "</title>" + dbUtils.NEWL;

        dbForumMessage msg;

        if (msgLst != null) {
            int count = (page-1) * PAGE_SIZE;

            for (int i=count; i<msgLst.length && i<count+PAGE_SIZE; i++) {
                msg = new dbForumMessage();
                msg.fmg_id = msgLst[i];
                msg.get(con);
                result += msg.contentAsXML(con, prof, MARK_MSG);
            }
        }

        result += "</topic>" + dbUtils.NEWL;

        return result;
    }

/* Tune by Stanely (This function is retired)
    public String contentShowAllAsXML(Connection con, loginProfile prof, long[] msgLst, int MARK_MSG)
        throws qdbException ,cwSysMessage
    {
        String result = "";

        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);

        result += "<topic id=\"" + fto_id + "\" createBy=\"" + dbUtils.esc4XML(user.usr_display_bil) +
                  "\" createDatetime=\"" + fto_create_datetime +
                  "\" numMsg=\"";

        if (msgLst != null) {
            result += msgLst.length;
        } else {
            result += "0";
        }

        if (MARK_MSG == 1) {
            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);
        }

        result += "\">" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(fto_title) + "</title>" + dbUtils.NEWL;

        dbForumMessage msg;

        if (msgLst != null) {
            for (int i=0; i<msgLst.length; i++) {
                msg = new dbForumMessage();
                msg.fmg_id = msgLst[i];
                msg.get(con);
                result += msg.contentAsXML(con, prof, MARK_MSG);
            }
        }

        result += "</topic>" + dbUtils.NEWL;

        return result;
    } */

    public String contentShowAllAsXML(Connection con, loginProfile prof, long[] msgLst, int MARK_MSG)
        throws qdbException ,cwSysMessage, SQLException
    {
        return contentShowAllAsXML(con, prof, msgLst, MARK_MSG, false);
    }
    
    public String contentShowAllAsXML(Connection con, loginProfile prof, long[] msgLst, int MARK_MSG, boolean reply_ind)
        throws qdbException ,cwSysMessage, SQLException
    {
        StringBuffer result = new StringBuffer();

/*        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);*/

        result.append("<topic id=\"").append(fto_id).append("\" createBy=\"").append(dbUtils.esc4XML(usr_display_bil));
        result.append("\" createDatetime=\"").append(fto_create_datetime);
/*        result.append("\" numMsg=\"");

        if (msgLst != null) {
            result += msgLst.length;
        } else {
            result += "0";
        }

        if (MARK_MSG == 1) {
            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);
        }*/

        result.append("\">");
        result.append("<title>").append(dbUtils.esc4XML(fto_title)).append("</title>");

        dbForumMessage msg;

        if (msgLst != null && msgLst.length != 0) {
            Vector vec = new Vector();

            for (int i=0; i<msgLst.length; i++) {
                vec.addElement(new Long(msgLst[i]));
/*                msg = new dbForumMessage();
                msg.fmg_id = msgLst[i];
                msg.get(con);
                result.append(msg.contentAsXML(con, prof, MARK_MSG));*/
            }

            result.append(dbForumMessage.contentAsXML(con, prof, MARK_MSG, cwUtils.vector2list(vec), reply_ind));
        }

        result.append("</topic>");

        return result.toString();
    }

    public long numMsgUnread(Connection con, loginProfile prof)
        throws qdbException
    {
        return  dbForumMessage.numOfMsgFromTopic(con, fto_id) -
                dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);
    }

    public static long[] searchTopics(Connection con, long res_id, String phrase, int phrase_cond, String created_by, int created_by_cond, Timestamp created_after, Timestamp created_before, String order)
        throws qdbException
    {
        try {
            if (res_id == 0) {
                return null;
            }

            String list = "";
            String phrase1 = "";
            String phrase2 = "";
            String phrase3 = "";
            String phrase4 = "";
            String created_by1 = "";
            String created_by2 = "";
            String created_by3 = "";
            String created_by4 = "";
            int phraseOn = 0;
            int created_byOn = 0;
            int index = 1;
            String[] s_lst;
            long[] l_lst = null;
            String SQL = "";

            if (created_by != null && created_by.length() != 0) {
                SQL = "SELECT fto_id FROM ForumTopic, RegUser WHERE fto_res_id = ? AND usr_id = fto_usr_id ";
            } else {
                SQL = "SELECT fto_id FROM ForumTopic WHERE fto_res_id = ? ";
            }

            if (phrase != null && phrase.length() != 0) {
                phrase1 = "%[^a-z]" + phrase + "[^a-z]%";
                phrase2 = phrase + "[^a-z]%";
                phrase3 = "%[^a-z]" + phrase;
                phrase4 = phrase;
                phraseOn = 1;

                if (phrase_cond == 1) {
					SQL += " AND (lower(fto_title) LIKE ? OR lower(fto_title) LIKE ? OR lower(fto_title) LIKE ? OR lower(fto_title) LIKE ?) ";
                } else {
                    phrase1 = "%" + phrase + "%";
					SQL += " AND lower(fto_title) LIKE ? ";
                }
            }

            if (created_by != null && created_by.length() != 0) {
                created_by1 = "%[^a-z]" + created_by + "[^a-z]%";
                created_by2 = created_by + "[^a-z]%";
                created_by3 = "%[^a-z]" + created_by;
                created_by4 = created_by;
                created_byOn = 1;

                if (created_by_cond == 1) {
//					SQL += " AND (lower(usr_display_bil) LIKE ? OR lower(usr_display_bil) LIKE ? OR lower(usr_display_bil) LIKE ? OR lower(usr_display_bil) LIKE ?) ";
                	SQL += " AND (lower(" + cwSQL.replaceNull("usr_nickname", "usr_display_bil") + ") LIKE ? OR lower(" + cwSQL.replaceNull("usr_nickname", "usr_display_bil") + ") LIKE ? OR lower(" + cwSQL.replaceNull("usr_nickname", "usr_display_bil") + ") LIKE ? OR lower(" + cwSQL.replaceNull("usr_nickname", "usr_display_bil") + ") LIKE ?) ";
                } else {
                    created_by1 = "%" + created_by + "%";
//					SQL += " AND lower(usr_display_bil) LIKE ? ";
                    SQL += " AND lower(" + cwSQL.replaceNull("usr_nickname", "usr_display_bil") + ") LIKE ? ";
                }
            }

            if (created_after != null) {
                SQL += " AND fto_create_datetime >= ? ";
            }

            if (created_before != null) {
                SQL += " AND fto_create_datetime <= ? ";
            }

            if (order != null && order.equalsIgnoreCase("ASC")) {
                SQL += " ORDER BY fto_title ASC";
            } else {
                SQL += " ORDER BY fto_title DESC";
            }

            PreparedStatement stmt = con.prepareStatement(SQL);

            stmt.setLong(index++, res_id);

            if (phraseOn == 1) {
                stmt.setString(index++, phrase1.toLowerCase());

                if (phrase_cond == 1) {
                    stmt.setString(index++, phrase2.toLowerCase());
                    stmt.setString(index++, phrase3.toLowerCase());
                    stmt.setString(index++, phrase4.toLowerCase());
                }
            }

            if (created_byOn == 1) {
                stmt.setString(index++, created_by1.toLowerCase());

                if (created_by_cond == 1) {
                    stmt.setString(index++, created_by2.toLowerCase());
                    stmt.setString(index++, created_by3.toLowerCase());
                    stmt.setString(index++, created_by4.toLowerCase());
                }
            }

            if (created_after != null) {
                stmt.setTimestamp(index++, created_after);
            }

            if (created_before != null) {
                stmt.setTimestamp(index++, created_before);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list += rs.getString("fto_id") + "~";
            }

            stmt.close();

            s_lst = dbUtils.split(list, "~");

            if (s_lst != null && s_lst.length != 0) {
                l_lst = new long[s_lst.length];

                for (int i=0; i<s_lst.length; i++) {
                    l_lst[i] = Long.parseLong(s_lst[i]);
                }
            }

            return l_lst;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public static List getAllTopicByResID(Connection con, long res_id) throws qdbException {
        try {
            List all_topic = new ArrayList();
            PreparedStatement stmt = con
                    .prepareStatement("select * from ForumTopic where fto_res_id = ? order by fto_id ASC");

            // set the values for prepared statements
            stmt.setLong(1, res_id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dbForumTopic topic = new dbForumTopic();
                topic.fto_id= rs.getLong("fto_id");
                topic.fto_res_id = rs.getLong("fto_res_id");
                topic.fto_title = rs.getString("fto_title");
                topic.fto_usr_id = rs.getString("fto_usr_id");
                topic.fto_create_datetime = rs.getTimestamp("fto_create_datetime");
                topic.fto_last_post_datetime = rs.getTimestamp("fto_last_post_datetime");
                all_topic.add(topic);
            } 

            stmt.close();
            return all_topic;
        } catch (SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
}
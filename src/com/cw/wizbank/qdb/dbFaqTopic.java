package com.cw.wizbank.qdb;

import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import com.cw.wizbank.util.cwSysMessage;

public class dbFaqTopic {
    public static final int PAGE_SIZE = 10;
    
    public long      fto_id;
    public long      fto_res_id;
    public String    fto_title;
    public String    fto_usr_id;
    public Timestamp fto_create_datetime;
    
    public dbFaqTopic() {
        fto_id = 0;    
    }
        
    public void ins(Connection con, loginProfile prof) 
        throws qdbException, qdbErrMessage
    {
        // check User Right
        /*
        dbModule mod = new dbModule();
        mod.mod_res_id = fto_res_id;
        mod.checkModifyPermission(con, prof);
        */
//        if (!dbResourcePermission.hasPermission(con, fto_res_id, prof,
//                                        dbResourcePermission.RIGHT_WRITE)) {
//            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
//        }            
        
        try {
            fto_create_datetime = dbUtils.getTime(con);  
            
            PreparedStatement stmt = con.prepareStatement(
                " INSERT INTO ForumTopic "
                + " ( fto_res_id "
                + " , fto_title "
                + " , fto_usr_id "
                + " , fto_create_datetime "
                + " ) "
                + " VALUES (?,?,?,?) "); 
                            
            stmt.setLong(1, fto_res_id);
            stmt.setString(2, fto_title);
            stmt.setString(3, prof.usr_id);
            stmt.setTimestamp(4, fto_create_datetime);


            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {           
                con.rollback();
                throw new qdbException("Failed to create Faq Topic.");
            }
                        
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }                  
    }
    
    public void del(Connection con, loginProfile prof) 
        throws qdbException, qdbErrMessage
    {
        // check User Right
        /*
        dbModule mod = new dbModule();
        mod.mod_res_id = fto_res_id;
        mod.checkModifyPermission(con, prof);
        */
//        if (!dbResourcePermission.hasPermission(con, fto_res_id, prof,
//                                        dbResourcePermission.RIGHT_WRITE)) {
//            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
//        }            

        try {               
            dbFaqMessage.delAllTopicMessages(con, prof, fto_res_id, fto_id);
                 
            PreparedStatement stmt = con.prepareStatement(
                "DELETE From ForumTopic where fto_id = ?");
                
            stmt.setLong(1, fto_id);

            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {           
                con.rollback();
                throw new qdbException("Fails to delete");
            }
         
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public void get(Connection con)
            throws qdbException ,cwSysMessage
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "SELECT "
            + " fto_res_id, "
            + " fto_title, "
            + " fto_usr_id, "
            + " fto_create_datetime "
            + " FROM ForumTopic "
            + " WHERE fto_id = ? ");
            
            // set the values for prepared statements
            stmt.setLong(1, fto_id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                fto_res_id = rs.getLong("fto_res_id");
                fto_title = rs.getString("fto_title"); 
                fto_usr_id = rs.getString("fto_usr_id");
                fto_create_datetime= rs.getTimestamp("fto_create_datetime");
            }
            else
            {
            	stmt.close();
                //throw new qdbException( "No data for faq. id = " + fto_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Faq ID = " + fto_id );
            }
                
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static long[] getAllTopicIDs(Connection con, long res_id, String order)
        throws qdbException
    {
        try {
            Vector topicLst = new Vector();
            int len;
            Long element;
            String SQL = "SELECT fto_id FROM ForumTopic where fto_res_id = ? ORDER BY fto_id";
            
            if (order != null && order.equalsIgnoreCase("asc")) {
                SQL += " ASC";
            } else {
                SQL += " DESC";
            }
            
            PreparedStatement stmt = con.prepareStatement(SQL);
                
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
        // Check at module level
        //if (!dbResourcePermission.hasPermission(con, res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}            
        
        try {
            dbFaqMessage.delAllFaqMessages(con, prof, res_id);            
            
            PreparedStatement stmt = con.prepareStatement(
                "DELETE From ForumTopic where fto_res_id = ?");
                
            stmt.setLong(1, res_id);
            stmt.executeUpdate();
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public String contentWithoutMsgsAsXML(Connection con, loginProfile prof, int MARK_MSG)
        throws qdbException
    {
        String result = "";
        long[] queLst = dbFaqMessage.getAllQuestionIDs(con, fto_id, null);

        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);            

        result += "<topic id=\"" + fto_id + "\" createDatetime=\"" + fto_create_datetime + 
                  "\" numQuestion=\"";
                  
        if (queLst != null) {
            result += queLst.length;
        } else {
            result += "0";
        }

        if (MARK_MSG == 1) { 
//            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);            
        }
                                   
        result += "\">" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(fto_title) + "</title>" + dbUtils.NEWL;
        result += "</topic>" + dbUtils.NEWL;

        return result;
    }    

    public String contentWithMsgsAsXML(Connection con, HttpSession sess, loginProfile prof, int MARK_MSG, int answerOn, int commentOn, String order, int page, int page_size)
        throws qdbException ,cwSysMessage
    {
        long[] queLst = null;  
        String result = "";
        String sess_type = (String)sess.getAttribute(dbFaq.FAQ_TYPE);
        String sess_cmd = (String)sess.getAttribute(dbFaq.FAQ_CMD);
        Long tempId = (Long)sess.getAttribute(dbFaq.FAQ_ID);
        long id = 0;
        
        if (tempId != null) {
            id = tempId.longValue();                
        }
        
        if (sess_type != null && sess_type.equals(dbFaq.FAQ_MSG) && 
            sess_cmd != null && sess_cmd.equals(dbFaq.FAQ_VIEW) &&
            page != 0 && id == fto_id) {
            queLst = (long[])sess.getAttribute(dbFaq.FAQ_RESULT);
//System.out.println("### dbFaqTopic: FROM SESSION!!!");    
        } else {            
            queLst = dbFaqMessage.getAllQuestionIDs(con, fto_id, order);
            sess.setAttribute(dbFaq.FAQ_TYPE, dbFaq.FAQ_MSG);
            sess.setAttribute(dbFaq.FAQ_CMD, dbFaq.FAQ_VIEW);
            sess.setAttribute(dbFaq.FAQ_ID, new Long(fto_id));    
                
            if (queLst != null) {
                sess.setAttribute(dbFaq.FAQ_RESULT, queLst);     
            }
            else {
                sess.removeAttribute(dbFaq.FAQ_RESULT);
            }
//System.out.println("### dbFaqTopic: FROM DATABASE!!!");            
        }

        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);            

        if (page == 0) {
            page = 1;
        }
        if (page_size <= 0) {
            page_size = PAGE_SIZE;
        }

        result += "<topic id=\"" + fto_id +
                  "\" createDatetime=\"" + fto_create_datetime + 
                  "\" page_size=\"" + page_size +
                  "\" cur_page=\"" + page +                  
                  "\" numQuestion=\"";
                  
        if (queLst != null) {
            result += queLst.length;
        } else {
            result += "0";
        }
                      
        if (MARK_MSG == 1) { 
//            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);            
        }
                  
        result += "\">" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(fto_title) + "</title>" + dbUtils.NEWL;
                  
        if (queLst != null) {                
            int count = (page-1) * page_size;
            dbFaqMessage msg;
                
            for (int i=count; i<queLst.length && i<count+page_size; i++) {
                msg = new dbFaqMessage();
                msg.fmg_id = queLst[i];
                result += msg.getAllMsgContent(con, prof, MARK_MSG, 0, commentOn);
            }
        }
        
        result += "</topic>" + dbUtils.NEWL;

        return result;
    }    

    public String contentAsXML(Connection con, loginProfile prof, long[] msgLst, int MARK_MSG, int page, int page_size, int answerOn, int commentOn)
        throws qdbException ,cwSysMessage
    {
        String result = "";

        dbRegUser user = new dbRegUser();
        user.usr_id = fto_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);            

        if (page == 0) {
            page = 1;
        }
        if (page_size <= 0) {
            page_size = PAGE_SIZE;
        }

        result += "<topic id=\"" + fto_id +
                  "\" page_size=\"" + page_size + 
                  "\" cur_page=\"" + page +
                  "\" createDatetime=\"" + fto_create_datetime + 
                  "\" numQuestion=\"";
                  
        if (msgLst != null) {
            result += msgLst.length;
        } else {
            result += "0";
        }
                  
        if (MARK_MSG == 1) { 
//            result += "\" numMsgRead=\"" + dbForumMarkMsg.numOfReadMsgFromTopic(con, fto_id, prof.usr_id);            
        }
                  
        result += "\">" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(fto_title) + "</title>" + dbUtils.NEWL;
                  
        dbFaqMessage msg;

        if (msgLst != null) {            
            int count = (page-1) * page_size;
            
            for (int i=count; i<msgLst.length && i<count+page_size; i++) {
                msg = new dbFaqMessage();
                msg.fmg_id = msgLst[i];
                msg.get(con);
                result += msg.getAllMsgContent(con, prof, MARK_MSG, answerOn, commentOn);
            }
        }
        
        result += "</topic>" + dbUtils.NEWL;

        return result;
    }
}
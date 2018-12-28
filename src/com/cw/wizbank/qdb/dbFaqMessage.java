package com.cw.wizbank.qdb;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwSQL;

public class dbFaqMessage {    
    public static final String FAQ_QUESTION = "Q";
    public static final String FAQ_ANSWER = "A";
    public static final String FAQ_COMMENT = "C";
    
    public long      fmg_id;
    public long      fmg_fto_id;
    public long      fmg_fto_res_id;
    public String    fmg_msg;
    public String    fmg_image;
    public String    fmg_usr_id;
    public long      fmg_fmg_id_parent;
    public long      fmg_fmg_fto_id_parent;
    public long      fmg_fmg_fto_res_id_parent;    
    public Timestamp fmg_create_datetime; 
    public String    fmg_type;
    public String    fmg_title;
    public String    fmg_msg_searchable_content;
    
    public dbFaqMessage() {
        fmg_id = 0;
        fmg_type = "";
    }
    
    public void ins(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
            /*
            // check User Right
            if (fmg_type.equals(FAQ_ANSWER)) {
                // check User Right
                dbModule mod = new dbModule();
                mod.mod_res_id = fmg_fto_res_id;
                mod.checkModifyPermission(con, prof);
                
//                if (!dbResourcePermission.hasPermission(con, fmg_fto_res_id, prof,
//                                                dbResourcePermission.RIGHT_WRITE)) {
//                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
//                }
            } else if (fmg_type.equals(FAQ_COMMENT) || fmg_type.equals(FAQ_QUESTION)) {
                if (prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) {
                    dbModule mod = new dbModule();
                    mod.mod_res_id = fmg_fto_res_id;
                    mod.checkModifyPermission(con, prof);
                } else {
                    long cosId  = dbModule.getCosId(con,fmg_fto_res_id); 
                    if (!dbResourcePermission.hasPermission(con, cosId, prof,
                                                dbResourcePermission.RIGHT_EXECUTE)) {
                        throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_EXECUTE_MSG);
                    }
                }
                
//                if (!dbResourcePermission.hasPermission(con, fmg_fto_res_id, prof,
//                                                dbResourcePermission.RIGHT_READ)) {
//                    long cosId  = dbModule.getCosId(con, fmg_fto_res_id); 
//                    if (!dbResourcePermission.hasPermission(con, cosId, prof,
//                                                dbResourcePermission.RIGHT_READ)) {
//                        throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
//                    }
//                }
            } else {
                throw new qdbException("Wrong Faq Message Type.");   
            }
            */
            PreparedStatement stmt;
            fmg_create_datetime = dbUtils.getTime(con);  
            fmg_usr_id = prof.usr_id;
            if(fmg_msg != null) {
                fmg_msg_searchable_content = fmg_msg.toLowerCase();
            }
            
            if (fmg_fmg_id_parent != 0) {
                fmg_fmg_fto_id_parent = fmg_fto_id;
                fmg_fmg_fto_res_id_parent = fmg_fto_res_id;

                stmt = con.prepareStatement(
                    " INSERT INTO ForumMessage "
                    + " ( fmg_fto_id "
                    + " , fmg_fto_res_id "
                    + " , fmg_msg "
                    + " , fmg_image "
                    + " , fmg_usr_id "
                    + " , fmg_fmg_id_parent "
                    + " , fmg_fmg_fto_id_parent "
                    + " , fmg_fmg_fto_res_id_parent "
                    + " , fmg_create_datetime "
                    + " , fmg_type "
                    + " , fmg_msg_searchable_content "
                    + " ) "
                    + " VALUES (?,?," + cwSQL.getClobNull() + ",?,?,?,?,?,?,?," + cwSQL.getClobNull() + ") ", PreparedStatement.RETURN_GENERATED_KEYS); 
                int index = 1;                                  
                stmt.setLong(index++, fmg_fto_id);
                stmt.setLong(index++, fmg_fto_res_id);
//                stmt.setString(3, fmg_msg);
                stmt.setString(index++, fmg_image);
                stmt.setString(index++, fmg_usr_id);
                stmt.setLong(index++, fmg_fmg_id_parent);
                stmt.setLong(index++, fmg_fmg_fto_id_parent);
                stmt.setLong(index++, fmg_fmg_fto_res_id_parent);
                stmt.setTimestamp(index++, fmg_create_datetime);
                stmt.setString(index++, fmg_type);
            } else {
                stmt = con.prepareStatement(
                    " INSERT INTO ForumMessage "
                    + " ( fmg_fto_id "
                    + " , fmg_fto_res_id "
                    + " , fmg_msg "
                    + " , fmg_image "
                    + " , fmg_usr_id "
                    + " , fmg_create_datetime "
                    + " , fmg_type "
                    + " , fmg_title "
                    + " , fmg_msg_searchable_content "
                    + " ) "
                    + " VALUES (?,?," + cwSQL.getClobNull() + ",?,?,?,?,?," + cwSQL.getClobNull() + ") ", PreparedStatement.RETURN_GENERATED_KEYS); 
                int index = 1;                                    
                stmt.setLong(index++, fmg_fto_id);
                stmt.setLong(index++, fmg_fto_res_id);
//                stmt.setString(3, fmg_msg);
                stmt.setString(index++, fmg_image);
                stmt.setString(index++, fmg_usr_id);
                stmt.setTimestamp(index++, fmg_create_datetime);
                stmt.setString(index++, fmg_type);
                stmt.setString(index++, fmg_title);
            }
            int stmtResult=stmt.executeUpdate();
            if ( stmtResult!=1)                            
            {
                stmt.close();
            	con.rollback();
            	throw new qdbException("Failed to create Faq Message.");
            }
            fmg_id = cwSQL.getAutoId(con, stmt, "ForumMessage", "fmg_id");
            stmt.close();
            // update clob field fmg_msg
            String columnName[]={"fmg_msg", "fmg_msg_searchable_content"};
            String columnValue[]={fmg_msg, fmg_msg_searchable_content};
            String condition = "fmg_id= " + fmg_id;
            cwSQL.updateClobFields(con, "ForumMessage",columnName,columnValue, condition);
                        
        }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }
    
    public void del(Connection con, loginProfile prof) 
        throws qdbException, qdbErrMessage
    {
        
        /*
        // check User Right
        dbModule mod = new dbModule();
        mod.mod_res_id = fmg_fto_res_id;
        mod.checkModifyPermission(con, prof);
        */
//        if (!dbResourcePermission.hasPermission(con, fmg_fto_res_id, prof,
//                                        dbResourcePermission.RIGHT_WRITE)) {
//            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
//        }

        int getOk = getWithNoException(con);
        
        if (getOk == 1) {
            delHelper(con, fmg_id);
        }
    }
    
    private void delHelper(Connection con, long message_id)
        throws qdbException, qdbErrMessage
    {
        try { 
            // delete childend first
            PreparedStatement stmt = con.prepareStatement(
                " SELECT fmg_id FROM ForumMessage WHERE fmg_fmg_id_parent = ? ");
                            
            stmt.setLong(1, message_id);
            ResultSet rs = stmt.executeQuery();
            long child_id;
            
            while (rs.next()) {
                child_id = rs.getLong("fmg_id");
                delHelper(con, child_id);
            }
            
//            dbForumMarkMsg.delByMsg(con, message_id);
            
            stmt = con.prepareStatement(" DELETE From ForumMessage WHERE fmg_id = ? ");
            stmt.setLong(1, message_id);
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
            + " fmg_fto_id, "
            + " fmg_fto_res_id, "
            + " fmg_msg, "
            + " fmg_image, "
            + " fmg_usr_id, "
            + " fmg_fmg_id_parent, "
            + " fmg_create_datetime, "
            + " fmg_type, "
            + " fmg_title "
            + " FROM ForumMessage "
            + " where fmg_id = ? ");
            
            // set the values for prepared statements
            stmt.setLong(1, fmg_id);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                fmg_fto_id = rs.getLong("fmg_fto_id");
                fmg_fto_res_id = rs.getLong("fmg_fto_res_id"); 
                fmg_msg = cwSQL.getClobValue(rs, "fmg_msg");
                fmg_image = rs.getString("fmg_image");
                fmg_usr_id = rs.getString("fmg_usr_id"); 
                fmg_fmg_id_parent = rs.getLong("fmg_fmg_id_parent");
                fmg_create_datetime= rs.getTimestamp("fmg_create_datetime");
                fmg_type = rs.getString("fmg_type");
                fmg_title = rs.getString("fmg_title");
            }
            else
            {
            	stmt.close();
                //throw new qdbException( "No data for faq message. id = " + fmg_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Faq message ID = " + fmg_id );
            }
            
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public int getWithNoException(Connection con)
            throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
            "SELECT "
            + " fmg_fto_id, "
            + " fmg_fto_res_id, "
            + " fmg_msg, "
            + " fmg_image, "
            + " fmg_usr_id, "
            + " fmg_fmg_id_parent, "
            + " fmg_create_datetime, "
            + " fmg_type, "
            + " fmg_title "
            + " FROM ForumMessage "
            + " where fmg_id = ? ");
            
            // set the values for prepared statements
            stmt.setLong(1, fmg_id);
            
            ResultSet rs = stmt.executeQuery();
            int cnt =0;
            if (rs.next()) {
                fmg_fto_id = rs.getLong("fmg_fto_id");
                fmg_fto_res_id = rs.getLong("fmg_fto_res_id"); 
                fmg_msg = cwSQL.getClobValue(rs, "fmg_msg");
                fmg_image = rs.getString("fmg_image");
                fmg_usr_id = rs.getString("fmg_usr_id"); 
                fmg_fmg_id_parent = rs.getLong("fmg_fmg_id_parent");
                fmg_create_datetime= rs.getTimestamp("fmg_create_datetime");
                fmg_type = rs.getString("fmg_type");
                fmg_title = rs.getString("fmg_title");
                cnt = 1;
            } else {
                cnt = 0;
            }
            
            stmt.close();
            
            return cnt;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }    
    
     public static long[] getAllQuestionIDs(Connection con, long fto_id, String order)
        throws qdbException
    {
        try {
            Vector msgLst = new Vector();
            int len;
            Long element;
            String SQL = "SELECT fmg_id FROM ForumMessage where fmg_fto_id = ? AND fmg_fmg_id_parent is NULL ORDER BY fmg_id";
            
            if (order != null && order.equalsIgnoreCase("ASC")) {
                SQL += " ASC";
            } else {
                SQL += " DESC";
            }            
            
            PreparedStatement stmt = con.prepareStatement(SQL);
                
            stmt.setLong(1, fto_id);
            ResultSet rs = stmt.executeQuery();
                
            while (rs.next()) {
                msgLst.addElement(new Long(rs.getLong("fmg_id")));
            }
            
            stmt.close();
            
            len = msgLst.size();
            
            if (len > 0) {
                long[] lst = new long[len];
                
                for (int i=0; i<len; i++) {
                    element = (Long) msgLst.elementAt(i);
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
    
    public static void delAllFaqMessages(Connection con, loginProfile prof, long for_res_id)
        throws qdbException, qdbErrMessage
    {
        // check User Right
        // Check at upper level
        //if (!dbResourcePermission.hasPermission(con, for_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}            
        
        try {            
//            dbForumMarkMsg.delByForum(con, for_res_id);
            
            PreparedStatement stmt = con.prepareStatement(
                "DELETE From ForumMessage where fmg_fto_res_id = ?");
                
            stmt.setLong(1, for_res_id);
            stmt.executeUpdate();
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
    
    public static void delAllTopicMessages(Connection con, loginProfile prof, long for_res_id, long fto_id)
        throws qdbException, qdbErrMessage
    {
        // check User Right
//        dbModule mod = new dbModule();
//        mod.mod_res_id = for_res_id;
//        mod.checkModifyPermission(con, prof);
        
//        if (!dbResourcePermission.hasPermission(con, for_res_id, prof,
//                                        dbResourcePermission.RIGHT_WRITE)) {
//            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
//        }            
        
        try {
//            dbForumMarkMsg.delByTopic(con, fto_id);
            
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From ForumMessage WHERE fmg_fto_id = ? ");
                
            stmt.setLong(1, fto_id);
            stmt.executeUpdate();
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }
        
    public String getAllMsgContent(Connection con, loginProfile prof, int MARK_MSG, int answerOn, int commentOn)
        throws qdbException ,cwSysMessage
    {
        try {
            String result = "";
            String SQL = "";
            
            get(con);
            
            dbRegUser user = new dbRegUser();
            user.usr_id = fmg_usr_id;
            user.usr_ent_id = user.getEntId(con);
            user.get(con);            
            
            result += "<message id=\"" + fmg_id + 
                    "\" topicId=\"" + fmg_fto_id +
                    "\" modID=\"" + fmg_fto_res_id + 
                    "\" modImage=\"" + fmg_image +
                    "\" createBy=\"" + dbUtils.esc4XML(user.usr_display_bil) +  
                    "\" parentId=\"" + fmg_fmg_id_parent +
                    "\" type=\"" + fmg_type;  
                    
            if (answerOn == 0 && commentOn == 0) {
                boolean hasAns = isAnswered(con);
                
                result += "\" isAnswered=\"";
                
                if (hasAns) {
                    result += "true";
                } else {
                    result += "false";
                }
            }

            if (MARK_MSG == 1) {
//                        result += "\" read=\"" + dbForumMarkMsg.isMsgRead(con, prof, fmg_id);
            }

            result += "\" createDatetime=\"" + fmg_create_datetime + "\">";
            result += "<title>" + dbUtils.esc4XML(fmg_title) + "</title>" + dbUtils.NEWL;
            result += "<body>" + dbUtils.esc4XML(fmg_msg) + "</body>" + dbUtils.NEWL; 

            SQL = "SELECT fmg_id FROM ForumMessage WHERE fmg_fmg_id_parent = ? ";

            if (answerOn == 0) {
                SQL += "AND fmg_type != '" + FAQ_ANSWER + "' ";
            }

            if (commentOn == 0) {
                SQL += "AND fmg_type != '" + FAQ_COMMENT + "' ";
            }
                
            SQL += "ORDER BY fmg_type DESC, fmg_id ASC";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
                                
            stmt.setLong(1, fmg_id);
            ResultSet rs = stmt.executeQuery();
            long child_id;
            dbFaqMessage msg;
                
            while (rs.next()) {
                msg = new dbFaqMessage();
                msg.fmg_id = rs.getLong("fmg_id");
                result += msg.getAllMsgContent(con, prof, MARK_MSG, answerOn, commentOn);
            }

            stmt.close();
            
            result += "</message>" + dbUtils.NEWL;
            return result;
            
        }  catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }   
    }    
    
    public static long[] searchMsgs(Connection con, long fto_id, String phrase, int phrase_cond, String created_by, 
                                    int created_by_cond, Timestamp created_after, Timestamp created_before, String order, 
                                    int search_que, int search_ans, int search_com)
        throws qdbException
    {
        try {
            if (fto_id == 0) {
                return null;   
            }
            
            if ((phrase == null || phrase.length() == 0) &&
                (created_by == null || created_by.length() == 0) &&
                (created_after == null) &&
                (created_before == null)) {
                return null;
            }
            
            String list = ""; 
            int phraseOn = 0;
            int created_byOn = 0;
            int index = 1;
            long[] l_lst = null;
            Vector v_lst = new Vector();
            long id;
            Long temp;
            String type;
            String SQL = "";
            String dbproduct = cwSQL.getDbProductName();
            
            if (created_by != null && created_by.length() != 0) {
                SQL = "SELECT fmg_id, fmg_type, fmg_fmg_id_parent FROM ForumMessage, RegUser WHERE fmg_fto_id = ? AND usr_id = fmg_usr_id ";
            } else {
                SQL = "SELECT fmg_id, fmg_type, fmg_fmg_id_parent FROM ForumMessage WHERE fmg_fto_id = ? ";
            }

            if (search_que == 1 || search_ans == 1 || search_com == 1) {
                SQL += "AND (";
                
                if (search_que == 1) {
                    SQL += "fmg_type = '" + FAQ_QUESTION + "' ";   
                }
                
                if (search_ans == 1) {
                    if (search_que == 1) {
                        SQL += " OR ";
                    }
                    
                    SQL += "fmg_type = '" + FAQ_ANSWER + "' ";    
                }
                
                if (search_com == 1) {
                    if (search_que == 1 || search_ans == 1) {
                        SQL += " OR ";
                    }
                    
                    SQL += "fmg_type = '" + FAQ_COMMENT + "' ";
                }
                
                SQL += ") ";
            }
            
            if (phrase != null && phrase.length() != 0) {
                phraseOn = 1;
                if (dbproduct.indexOf(cwSQL.ProductName_ORACLE) >= 0) {
                    SQL += " AND ( DBMS_LOB.INSTR(fmg_msg_searchable_content,?, 1, 1) >= 1 OR";
                    SQL += " LOWER(fmg_title) LIKE ? ) ";
                }else{
                    phrase = "%" + phrase + "%";
                    SQL += " AND ( fmg_msg_searchable_content LIKE ? OR";
                    SQL += " fmg_title LIKE ? ) ";
                }
            }
 
            if (created_by != null && created_by.length() != 0) {
                created_byOn = 1;            
                created_by = "%" + created_by + "%";
				SQL += " AND lower(usr_display_bil) LIKE ? ";                    
            }             
                        
            if (created_after != null) {
                SQL += " AND fmg_create_datetime >= ? ";   
            }

            if (created_before != null) {
                SQL += " AND fmg_create_datetime <= ? ";
            }
                        
            if (order != null && order.equalsIgnoreCase("ASC")) {
                SQL += " ORDER BY fmg_id ASC";
            } else {
                SQL += " ORDER BY fmg_id DESC";
            }
             
            PreparedStatement stmt = con.prepareStatement(SQL);
                
            stmt.setLong(index++, fto_id);
 
            if (phraseOn == 1) {
                stmt.setString(index++, phrase.toLowerCase());
				stmt.setString(index++, phrase.toLowerCase());
            }
            
            if (created_byOn == 1) {
                stmt.setString(index++, created_by.toLowerCase());
            }
            
            if (created_after != null) {
                stmt.setTimestamp(index++, created_after); 
            }

            if (created_before != null) {
                stmt.setTimestamp(index++, created_before);
            }
                      
            ResultSet rs = stmt.executeQuery();
                
            while (rs.next()) {
                  type = rs.getString("fmg_type");

                  if (type.equals(FAQ_QUESTION)) {
                      id = rs.getLong("fmg_id");
                  } else {
                      id = rs.getLong("fmg_fmg_id_parent");
                  }
                  
                  if (! v_lst.contains(new Long(id))) {
                      v_lst.addElement(new Long(id));
                  }
            }
            
            stmt.close();
            l_lst = new long[v_lst.size()];
                        
            if (v_lst != null && v_lst.size() != 0) {
                for (int i=0; i<v_lst.size(); i++) {
                    temp = (Long)v_lst.elementAt(i);                    
                    l_lst[i] = temp.longValue();    
                }
            }
                
            return l_lst;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }     
    }
    
    public boolean isAnswered(Connection con)
        throws qdbException  
    {
        try {
            boolean hasAns = false;
            ResultSet rs;
            String SQL = "SELECT fmg_id FROM ForumMessage WHERE fmg_fmg_id_parent = ? AND fmg_type = '" + FAQ_ANSWER + "' ";

            PreparedStatement stmt = con.prepareStatement(SQL);
                                
            stmt.setLong(1, fmg_id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                hasAns = true;
            } 

            stmt.close();
            
            return hasAns;
            
        }  catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }   
    }  
    
    public void updAns(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage
    {
        try {
            /*
            // check User Right
            if (fmg_type.equals(FAQ_ANSWER)) {
                // check User Right
                dbModule mod = new dbModule();
                mod.mod_res_id = fmg_fto_res_id;
                mod.checkModifyPermission(con, prof);

//                if (!dbResourcePermission.hasPermission(con, fmg_fto_res_id, prof,
//                                                dbResourcePermission.RIGHT_WRITE)) {
//                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
//                }
            } else {
                return;
            }
            */
            if(fmg_msg != null) {
                fmg_msg_searchable_content = fmg_msg.toLowerCase();
            }
            fmg_create_datetime = dbUtils.getTime(con);  
        
            PreparedStatement stmt = con.prepareStatement(
                " Update ForumMessage set "
                + " fmg_image = ?"
                + " , fmg_usr_id = ?"
                + " , fmg_create_datetime = ?"
                + " WHERE fmg_id = ? AND fmg_fmg_id_parent = ? "); 
                                    
            stmt.setString(1, fmg_image);
            stmt.setString(2, prof.usr_id);
            stmt.setTimestamp(3, fmg_create_datetime);
            stmt.setLong(4, fmg_id);
            stmt.setLong(5, fmg_fmg_id_parent);
                            
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                throw new qdbException("Failed to update Faq Answer.");
            }     
            String columnName[]={"fmg_msg", "fmg_msg_searchable_content"};
            String columnValue[]={fmg_msg, fmg_msg_searchable_content};
            String condition = "fmg_id= " + fmg_id;
            cwSQL.updateClobFields(con, "ForumMessage",columnName,columnValue, condition);
            
        }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }        
}
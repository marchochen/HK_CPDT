package com.cw.wizbank.qdb;

import java.util.*;
import java.sql.*;

import com.oroinc.text.perl.*;
import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;

public class dbForumMessage {    

    public static final String USER_TABLE = "FMG_USER_TABLE";
    public static final String CHAINED_MESSAGES = "FMG_CHAINED_MESSAGES";
    
    public static final String MSG_TYPE_FMG = "fmg";
    
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
    
    public HashMap directory_info_map;
    
    public dbForumMessage() {
        ;
    }
    
    public void ins(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage, cwException
    {
        try {
            // check User Right
        /*
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
        */
        /*
        long cosId  = dbModule.getCosId(con,fmg_fto_res_id); 
        if (!dbResourcePermission.hasPermission(con, cosId, prof,
                                    dbResourcePermission.RIGHT_EXECUTE)) {
            try {
                dbModule mod = new dbModule();
                mod.mod_res_id = fmg_fto_res_id;
                mod.checkModifyPermission(con, prof);
            }
            catch(qdbErrMessage e) {
                throw new qdbErrMessage("ACL002");
            }
        }
        */

//            if (!dbResourcePermission.hasPermission(con, fmg_fto_res_id, prof,
//                                            dbResourcePermission.RIGHT_READ)) {
//                long cosId  = dbModule.getCosId(con, fmg_fto_res_id); 
//                if (!dbResourcePermission.hasPermission(con, cosId, prof,
//                                            dbResourcePermission.RIGHT_READ)) {
//                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
//                }
//            }
            
            PreparedStatement stmt;
            fmg_create_datetime = dbUtils.getTime(con);  
            fmg_usr_id = prof.usr_id;
            if(fmg_msg != null) {
//            	String temp2 = new String(fmg_msg);
//            	StringBuffer temp = getTextInLink(temp2);
            	
                fmg_msg_searchable_content = (cwUtils.unescHTML(cwUtils.removeTag(dbMessage.unescImgHTML(fmg_msg)))).toLowerCase();
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
                    + " , fmg_title "
                    + " , fmg_msg_searchable_content "
                    + " ) "
                    + " VALUES (?,?," + cwSQL.getClobNull() + ",?,?,?,?,?,?,?,?," + cwSQL.getClobNull() +") ", PreparedStatement.RETURN_GENERATED_KEYS); 
    
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
                stmt.setString(index++, fmg_title);
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
                    + " VALUES (?,?," + cwSQL.getClobNull() + ",?,?,?,?,?,"+ cwSQL.getClobNull() +") ", PreparedStatement.RETURN_GENERATED_KEYS); 
                                    
                int index = 1;                                
                stmt.setLong(index++, fmg_fto_id);
                stmt.setLong(index++, fmg_fto_res_id);
//                stmt.setString(index++, fmg_msg);
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
                throw new qdbException("Failed to create Forum Message.");
            }        
            fmg_id = cwSQL.getAutoId(con, stmt, "ForumMessage", "fmg_id");
            stmt.close();
            
            //update fmg_msg for mutimedia
            if(this.directory_info_map != null) {
            	fmg_msg = dbMessage.getMultimediaMsgBody(fmg_msg, MSG_TYPE_FMG + fmg_id, this.directory_info_map);
            }
            
            // update clob field fmg_msg
            String columnName[]={"fmg_msg", "fmg_msg_searchable_content"};
            String columnValue[]={fmg_msg, fmg_msg_searchable_content};
            String condition = "fmg_id= " + fmg_id;
            cwSQL.updateClobFields(con, "ForumMessage",columnName,columnValue, condition);
            
            stmt = con.prepareStatement("update ForumTopic set fto_last_post_datetime = ? where fto_id = ?");
            stmt.setTimestamp(1, fmg_create_datetime);
            stmt.setLong(2, fmg_fto_id);

            stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)
            {            
                con.rollback();
                throw new qdbException("Failed to update Forum Topic (last post datetime).");
            }        
            
        }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }        
    }

    public void ins(Connection con, loginProfile prof, Timestamp create_datetime)
        throws qdbException, qdbErrMessage
    {
        fmg_create_datetime = create_datetime;
        fmg_usr_id = prof.usr_id;
        if(fmg_msg != null) {
            fmg_msg_searchable_content = (cwUtils.unescHTML(cwUtils.removeTag(fmg_msg))).toLowerCase();
        }
        // check User Right
        /*
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
        */
//        if (!dbResourcePermission.hasPermission(con, fmg_fto_res_id, prof,
//                                        dbResourcePermission.RIGHT_READ)) {
//            long cosId  = dbModule.getCosId(con, fmg_fto_res_id); 
//            if (!dbResourcePermission.hasPermission(con, cosId, prof,
//                                        dbResourcePermission.RIGHT_READ)) {
//                throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
//            }
//        }
        
//        fmg_fmg_fto_id_parent = fmg_fto_id;
//        fmg_fmg_fto_res_id_parent = fmg_fto_res_id;

        try {
            PreparedStatement stmt = con.prepareStatement(
                " INSERT INTO ForumMessage "
                + " ( fmg_fto_id "
                + " , fmg_fto_res_id "
                + " , fmg_msg "
                + " , fmg_image "
                + " , fmg_usr_id "
                + " , fmg_create_datetime "
                + " , fmg_type "
                + " , fmg_title "
                + " , fmg_msg_search_content "
                + " ) "
                + " VALUES (?,?," + cwSQL.getClobNull() + ",?,?,?,?,?," + cwSQL.getClobNull() + ") ", PreparedStatement.RETURN_GENERATED_KEYS);

            int index = 1;                                    
            stmt.setLong(index++, fmg_fto_id);
            stmt.setLong(index++, fmg_fto_res_id);
//            stmt.setString(3, fmg_msg);
            stmt.setString(index++, fmg_image);
            stmt.setString(index++, fmg_usr_id);
            stmt.setTimestamp(index++, fmg_create_datetime);
            stmt.setString(index++, fmg_type);
            stmt.setString(index++, fmg_title);

            int stmtResult=stmt.executeUpdate();
            if ( stmtResult!=1)                            
            {            
                stmt.close();
                con.rollback();
                throw new qdbException("Failed to create Forum Message.");
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
        // check User Right
        /*
        if (prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) {
            dbModule mod = new dbModule();
            mod.mod_res_id = fmg_fto_res_id;
            mod.checkModifyPermission(con, prof);
        }
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
                //delete files of child message
                delDirByFmgId(child_id+"");
            }
            stmt.close();
            dbForumMarkMsg.delByMsg(con, message_id);
            
            //delete files of parent message
            delDirByFmgId(message_id+"");
            
            stmt = con.prepareStatement(" DELETE From ForumMessage WHERE fmg_id = ? ");
            stmt.setLong(1, message_id);

            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {            
                con.rollback();
                throw new qdbException("Fails to delete Forum Message");
            }
                        

            stmt = con.prepareStatement("select max(fmg_create_datetime) last_post_datetime from ForumMessage where fmg_fto_id = ?");
            stmt.setLong(1, fmg_fto_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Timestamp last_post_datetime = rs.getTimestamp("last_post_datetime");
                PreparedStatement stmt1 = con.prepareStatement("update ForumTopic set fto_last_post_datetime = ? where fto_id = ?");

                if (last_post_datetime != null) {
                    stmt1.setTimestamp(1, last_post_datetime);
                } else {
                    stmt1.setNull(1, java.sql.Types.TIMESTAMP);
                }

                stmt1.setLong(2, fmg_fto_id);

            	stmtResult=stmt1.executeUpdate();
            	stmt1.close();
            	if ( stmtResult!=1)                            
            	{                
                    con.rollback();
                    throw new qdbException("Fails to update Forum Topic (fto_last_post_datetime)");
                }                
            }

            stmt.close();
            
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
                //throw new qdbException( "No data for forum message. id = " + fmg_id );
                throw new cwSysMessage(dbUtils.MSG_REC_NOT_FOUND, "Forum Message ID = " + fmg_id );
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
    
    public static long[] getAllMsgIDs(Connection con, long fto_id, String order)
        throws qdbException
    {
        try {
            Vector msgLst = new Vector();
            int len;
            Long element;
            
            String SQL = "SELECT fmg_id FROM ForumMessage where fmg_fto_id = ? ORDER BY fmg_id";
            
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

     public static long[] getAllRootMessageIDs(Connection con, long fto_id)
        throws qdbException
    {
        try {
            Vector msgLst = new Vector();
            int len;
            Long element;
            
            PreparedStatement stmt = con.prepareStatement(
                " SELECT fmg_id FROM ForumMessage where fmg_fto_id = ? AND fmg_fmg_id_parent is NULL ORDER BY fmg_id ");
                
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
     
    
    public static void delAllForumMessages(Connection con, loginProfile prof, long for_res_id)
        throws qdbException, qdbErrMessage
    {
    	//delete file of forum message
    	delFilesOfFmg(con, for_res_id);
        
        try {            
            dbForumMarkMsg.delByForum(con, for_res_id);
            
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
    	//delete file of forum message
    	delFilesOfFmg(con, for_res_id);           
        
        try {
            dbForumMarkMsg.delByTopic(con, fto_id);
            
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From ForumMessage WHERE fmg_fto_id = ? ");
                
            stmt.setLong(1, fto_id);
            stmt.executeUpdate();
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public String contentAsXML(Connection con, loginProfile prof, String usr_display_bil, int MARK_MSG, String nickname)
        throws qdbException
    {
        String result = "";
        String usr_name = null;
        if (nickname != null && nickname.length() > 0) {
        	usr_name = nickname;
        } else {
        	usr_name = usr_display_bil;
        }
        result += "<message id=\"" + fmg_id + 
                  "\" topicId=\"" + fmg_fto_id +
                  "\" modID=\"" + fmg_fto_res_id + 
                  "\" modImage=\"" + fmg_image +
                  "\" createBy=\"" + dbUtils.esc4XML(usr_display_bil) +  
                  "\" nickname=\"" + dbUtils.esc4XML(usr_name) +  
                  "\" parentId=\"" + fmg_fmg_id_parent;

                  if (MARK_MSG == 1) {
                      result += "\" read=\"" + dbForumMarkMsg.isMsgRead(con, prof, fmg_id);
                  }

        result += "\" createDatetime=\"" + fmg_create_datetime + "\">";
        result += "<subject>" + cwUtils.escNull(dbUtils.esc4XML(fmg_title)) + "</subject>" + dbUtils.NEWL;
        result += "<body>";
        result += dbUtils.esc4XML(fmg_msg);
        result += "</body>" + dbUtils.NEWL; 
        result += "</message>" + dbUtils.NEWL;
        return result;
    }    
    
    public String contentAsXML(Connection con, loginProfile prof, int MARK_MSG)
        throws qdbException
    {
        String result = "";
        dbRegUser user = new dbRegUser();
        user.usr_id = fmg_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);
        
        return contentAsXML(con, prof, user.usr_display_bil, MARK_MSG, user.usr_nickname);
    }    

    public static String contentAsXML(Connection con, loginProfile prof, int MARK_MSG, String msg_lst)
        throws qdbException, SQLException
    {
        return contentAsXML(con, prof, MARK_MSG, msg_lst, false);
    }

    public static String contentAsXML(Connection con, loginProfile prof, int MARK_MSG, String msg_lst, boolean reply_ind)
        throws qdbException, SQLException
    {
        StringBuffer result = new StringBuffer();
        PreparedStatement stmt = con.prepareStatement("SELECT fmg_id, fmg_fmg_id_parent from ForumMessage where fmg_fmg_id_parent in " + msg_lst);
        ResultSet rs = stmt.executeQuery();   
        
        Hashtable msgId_childId_pair = new Hashtable();
        
        while (rs.next()) {
            Long child_id = new Long(rs.getLong("fmg_id"));
            Long parent_id = new Long(rs.getLong("fmg_fmg_id_parent"));
            
            Vector child_id_vec = (Vector)msgId_childId_pair.get(parent_id);
            
            if (child_id_vec == null) {
                child_id_vec = new Vector();
            }
            
            child_id_vec.addElement(child_id);
            msgId_childId_pair.put(parent_id, child_id_vec);
        }

        stmt.close();

        stmt = con.prepareStatement("SELECT fmg_id, fmg_fto_id, fmg_fto_res_id, fmg_msg, fmg_image, fmg_usr_id, fmg_fmg_id_parent, fmg_create_datetime, fmg_type, fmg_title, usr_display_bil, usr_nickname FROM ForumMessage, RegUser where fmg_usr_id = usr_id and fmg_id in " + msg_lst + " order by fmg_id DESC");
        rs = stmt.executeQuery();          

        while (rs.next()) {
/*            dbRegUser user = new dbRegUser();
            user.usr_id = fmg_usr_id;
            user.usr_ent_id = user.getEntId(con);
            user.get(con);*/

            long fmg_id = rs.getLong("fmg_id");
            long fmg_fto_id = rs.getLong("fmg_fto_id");
            long fmg_fto_res_id = rs.getLong("fmg_fto_res_id");
            String fmg_msg = cwSQL.getClobValue(rs, "fmg_msg");
            String fmg_image = rs.getString("fmg_image");
            String fmg_usr_id = rs.getString("fmg_usr_id");
            long fmg_fmg_id_parent = rs.getLong("fmg_fmg_id_parent");
            Timestamp fmg_create_datetime = rs.getTimestamp("fmg_create_datetime");
            String fmg_type = rs.getString("fmg_type");
            String fmg_title = rs.getString("fmg_title");
            String usr_display_bil = rs.getString("usr_display_bil");
            String usr_nickname = rs.getString("usr_nickname");

            result.append("<message id=\"").append(fmg_id).append("\" topicId=\"").append(fmg_fto_id);
            result.append("\" modID=\"").append(fmg_fto_res_id).append("\" modImage=\"").append(fmg_image);
            result.append("\" createBy=\"").append(dbUtils.esc4XML(usr_display_bil));
            String usr_name = null;
            if (usr_nickname != null && usr_nickname.length() > 0) {
            	usr_name = usr_nickname;
            } else {
            	usr_name = usr_display_bil;
            }
            result.append("\" nickname=\"").append(dbUtils.esc4XML(usr_name)); 
            result.append("\" parentId=\"").append(fmg_fmg_id_parent);

            if (MARK_MSG == 1) {
                result.append("\" read=\"").append(dbForumMarkMsg.isMsgRead(con, prof, fmg_id));
            }

            result.append("\" createDatetime=\"").append(fmg_create_datetime).append("\">");
            result.append("<childId_list>");

            Vector childId_vec = (Vector)msgId_childId_pair.get(new Long(fmg_id));

            if (childId_vec != null) {
                for (int i=0; i<childId_vec.size(); i++) {
                    result.append("<id>").append(childId_vec.elementAt(i)).append("</id>");
                }
            }
            
            result.append("</childId_list>");
            result.append("<subject>").append(cwUtils.escNull(dbUtils.esc4XML(fmg_title))).append("</subject>");            
            result.append("<body>");
            if(reply_ind) {
                result.append(cwUtils.esc4XmlJs(formatContent4Reply(fmg_msg, usr_display_bil)));
            } else {
                result.append(dbUtils.esc4XML(fmg_msg));
            }
            result.append("</body>"); 
            result.append("</message>");
        }

        stmt.close();

        return result.toString();
    } 

    public static long numOfMsgFromTopic(Connection con, long fto_id)
        throws qdbException
    {
        try {
            Vector msgLst = new Vector();
            int len;
            Long element;
            
            PreparedStatement stmt = con.prepareStatement(
                " SELECT COUNT(*) FROM ForumMessage where fmg_fto_id = ? ");
                
            stmt.setLong(1, fto_id);
            ResultSet rs = stmt.executeQuery();
            
            long cnt =0;
            if(rs.next()) {
                cnt = rs.getLong(1);   
            } else {
            	stmt.close();
                throw new qdbException("Error : Cannot get the statistic. ");
            }
            
            stmt.close();
            return cnt;
            
         } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }     
    }
    
    public String getAllMsgContent(Connection con, loginProfile prof, int MARK_MSG)
        throws qdbException ,cwSysMessage
    {
        try {
            String result = "";
            
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
                    "\" parentId=\"" + fmg_fmg_id_parent;

                    if (MARK_MSG == 1) {
                        result += "\" read=\"" + dbForumMarkMsg.isMsgRead(con, prof, fmg_id);
                    }

            result += "\" createDatetime=\"" + fmg_create_datetime + "\">";
            result += "<subject>" + cwUtils.escNull(dbUtils.esc4XML(fmg_title)) + "</subject>" + dbUtils.NEWL;
            result += "<body>" + dbUtils.esc4XML(fmg_msg) + "</body>" + dbUtils.NEWL; 
            
            PreparedStatement stmt = con.prepareStatement(
                " SELECT fmg_id FROM ForumMessage WHERE fmg_fmg_id_parent = ? ORDER BY fmg_id");
                                
            stmt.setLong(1, fmg_id);
            ResultSet rs = stmt.executeQuery();
            long child_id;
            dbForumMessage msg;
                
            while (rs.next()) {
                msg = new dbForumMessage();
                msg.fmg_id = rs.getLong("fmg_id");
                result += msg.getAllMsgContent(con, prof, MARK_MSG);
            }
            
            stmt.close();
            
            result += "</message>" + dbUtils.NEWL;
            return result;
        }  catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }   
    }    
    
    public static long[] searchMsgs(Connection con, long fto_id, String phrase, int phrase_cond, String created_by, int created_by_cond, Timestamp created_after, Timestamp created_before, String order)
        throws qdbException
    {
        try {
            if (fto_id == 0) {
                return null;   
            }
            
            String list = ""; 
            int phraseOn = 0;
            int created_byOn = 0;
            int index = 1;
            String[] s_lst;
            long[] l_lst = null;
            String SQL = "";
            String dbproduct = cwSQL.getDbProductName();
    
            if (created_by != null && created_by.length() != 0) {
                SQL = "SELECT fmg_id FROM ForumMessage, RegUser WHERE fmg_fto_id = ? AND usr_id = fmg_usr_id ";
            } else {
                SQL = "SELECT fmg_id FROM ForumMessage WHERE fmg_fto_id = ? ";
            }
            
            if (phrase != null && phrase.length() != 0) {
                phraseOn = 1;
                SQL += " AND ( ";
                if (dbproduct.indexOf(cwSQL.ProductName_ORACLE) >= 0) {
                    SQL += "  DBMS_LOB.INSTR(fmg_msg_searchable_content,?, 1, 1) >= 1 ";
                }else{
                    phrase = "%" + phrase + "%";
                    SQL += " fmg_msg_searchable_content LIKE ? ";
                }
                SQL += " OR lower(fmg_title) LIKE ? ) ";
            }
 
            if (created_by != null && created_by.length() != 0) {
                created_byOn = 1;            
                created_by = "%" + created_by + "%";
                SQL += " AND lower(" + cwSQL.replaceNull("usr_nickname", "usr_display_bil") + ") LIKE ? ";                    

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
                list += rs.getString("fmg_id") + "~";
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
    
    
    private static final String SQL = " SELECT fmg_id, fmg_fto_res_id, fmg_image, "
                                    + " fmg_usr_id, fmg_fmg_id_parent, fmg_create_datetime, "
                                    + " fmg_msg, fmg_title "
                                    + " FROM ForumMessage "
                                    + " WHERE fmg_fto_id = ? "
                                    + " ORDER BY fmg_create_datetime ASC ";

    public static Hashtable getUnthreadedMessages(Connection con, long fto_id, int msg_length, loginProfile prof)
        throws qdbException, SQLException {
        
        PreparedStatement stmt = null;
        //Vector rootMsgVec = new Vector();
        //Vector msgVec = null;
        Vector returnVec = new Vector();
        Vector usrIdVec = new Vector();
        //Hashtable parentIdTable = new Hashtable();
        
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, fto_id);
            ResultSet rs = stmt.executeQuery();                

            while( rs.next() ) {
                dbForumMessage msg = new dbForumMessage();
                msg.fmg_id = rs.getLong("fmg_id");
                msg.fmg_fto_id = fto_id;
                msg.fmg_fto_res_id = rs.getLong("fmg_fto_res_id");
                msg.fmg_image = rs.getString("fmg_image");
                msg.fmg_usr_id = rs.getString("fmg_usr_id");
                msg.fmg_fmg_id_parent = rs.getLong("fmg_fmg_id_parent");
                msg.fmg_create_datetime = rs.getTimestamp("fmg_create_datetime");
                msg.fmg_msg = cwSQL.getClobValue(rs, "fmg_msg");
                msg.fmg_title = rs.getString("fmg_title");
                
                if( usrIdVec.indexOf(msg.fmg_usr_id) == -1 )
                    usrIdVec.addElement(msg.fmg_usr_id);

                // list the messages in the order of create datetime instead of thread order
                returnVec.addElement(msg);
                //if( msg.fmg_fmg_id_parent == 0 )
                //    rootMsgVec.addElement(msg);         //  put root mesage into vector
                //else {
                //    // Use hashtable to group message with same parent id
                //    if( parentIdTable.containsKey(new Long(msg.fmg_fmg_id_parent)) ) {
                //        //parent id find in hashtable, append a message to the vector get from hashtable
                //        //and put vector back to table
                //        msgVec = (Vector)parentIdTable.get(new Long(msg.fmg_fmg_id_parent));
                //        msgVec.addElement(msg);
                //        parentIdTable.put(new Long(msg.fmg_fmg_id_parent), msgVec);
                //        
                //    } else {
                //        //parent id not in table, new a vector containing a message and put to table
                //        msgVec = new Vector();
                //        msgVec.addElement(msg);
                //        parentIdTable.put(new Long(msg.fmg_fmg_id_parent), msgVec);
                //        
                //    }                    
                //}
            }
        } finally {
            if(stmt != null) stmt.close();
        }
        Hashtable userTable = dbRegUser.getNameTable(con, usrIdVec, prof.root_ent_id);

        ////chain the untreaded message
        //returnVec = new Vector();
        //for(int i=0; i<rootMsgVec.size(); i++) {
        //    chainUnthreadedMessage((dbForumMessage)rootMsgVec.elementAt(i), parentIdTable, returnVec);
        //}
        
        Hashtable returnHash = new Hashtable();
        returnHash.put(USER_TABLE, userTable);
        returnHash.put(CHAINED_MESSAGES, returnVec);
        return returnHash;
    }

    //private static void chainUnthreadedMessage(dbForumMessage msg, Hashtable parentIdTable, Vector returnVec) {
    //    returnVec.addElement(msg);
    //    Vector msgVec = (Vector)parentIdTable.get(new Long(msg.fmg_id));
    //    if(msgVec != null) {
    //        for(int i=0; i<msgVec.size(); i++) {
    //            chainUnthreadedMessage((dbForumMessage)msgVec.elementAt(i), parentIdTable, returnVec);
    //        }
    //    }
    //    return;
    //}
    
    
    /**
    Get all message in the topic As XML
    @param forum topic id
    @param message length
    @return XML    
    */
    public String getAllMsgContent(Connection con, long fto_id, int msg_length, loginProfile prof)
        throws qdbException, SQLException {
            StringBuffer xml = new StringBuffer();

            String SQL = " SELECT fmg_id, fmg_fto_res_id, fmg_image, "
                       + " fmg_usr_id, fmg_fmg_id_parent, fmg_create_datetime, fmg_msg, fmg_title "
                       + " FROM ForumMessage "
                       + " WHERE fmg_fto_id = ? "
                       + " ORDER BY fmg_create_datetime ASC ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, fto_id);
            ResultSet rs = stmt.executeQuery();                
            
            dbForumMessage msg;
            Vector rootMsgVec = new Vector();
            Vector msgVec;
            Hashtable parentIdTable = new Hashtable();
            Vector usrIdVec = new Vector();
            while( rs.next() ) {
                msg = new dbForumMessage();
                msg.fmg_id = rs.getLong("fmg_id");
                msg.fmg_fto_id = fto_id;
                msg.fmg_fto_res_id = rs.getLong("fmg_fto_res_id");
                msg.fmg_image = rs.getString("fmg_image");
                msg.fmg_usr_id = rs.getString("fmg_usr_id");
                msg.fmg_fmg_id_parent = rs.getLong("fmg_fmg_id_parent");
                msg.fmg_create_datetime = rs.getTimestamp("fmg_create_datetime");
                msg.fmg_msg = truncateMsg(cwSQL.getClobValue(rs, "fmg_msg"), msg_length);
                msg.fmg_title = rs.getString("fmg_title");
                
                if( usrIdVec.indexOf(msg.fmg_usr_id) == -1 )
                    usrIdVec.addElement(msg.fmg_usr_id);

                if( msg.fmg_fmg_id_parent == 0 )
                    rootMsgVec.addElement(msg);         //  put root mesage into vector
                else {
                    // Use hashtable to group message with same parent id
                    if( parentIdTable.containsKey(new Long(msg.fmg_fmg_id_parent)) ) {
                        //parent id find in hashtable, append a message to the vector get from hashtable
                        //and put vector back to table
                        msgVec = (Vector)parentIdTable.get(new Long(msg.fmg_fmg_id_parent));
                        msgVec.addElement(msg);
                        parentIdTable.put(new Long(msg.fmg_fmg_id_parent), msgVec);
                        
                    } else {
                        //parent id not in table, new a vector containing a message and put to table
                        msgVec = new Vector();
                        msgVec.addElement(msg);
                        parentIdTable.put(new Long(msg.fmg_fmg_id_parent), msgVec);
                        
                    }                    
                }
                
            }

            stmt.close();
            
            Hashtable userTable = dbRegUser.getNameTable(con, usrIdVec, prof.root_ent_id);
            
            for(int i=0; i<rootMsgVec.size(); i++) {
                
                msg = (dbForumMessage)rootMsgVec.elementAt(i);
                xml.append("<message id=\"").append(msg.fmg_id).append("\" ")
                   .append(" topicId=\"").append(msg.fmg_fto_id).append("\" ")
                   .append(" modID=\"").append(msg.fmg_fto_res_id).append("\" ")
                   .append(" modImage=\"").append(msg.fmg_image).append("\" ")
                   .append(" createBy=\"").append(dbUtils.esc4XML((String)userTable.get(msg.fmg_usr_id))).append("\" ")
                   .append(" parentId=\"0\" ")
                   .append(" createDatetime=\"").append(msg.fmg_create_datetime).append("\">").append(dbUtils.NEWL)
                   .append("<subject>").append(cwUtils.escNull(dbUtils.esc4XML(msg.fmg_title))).append("</subject>").append(dbUtils.NEWL)
                   .append("<body>").append(dbUtils.esc4XML(msg.fmg_msg)).append("</body>").append(dbUtils.NEWL);
                
                getChildMessageAsXML(new Long(msg.fmg_id), parentIdTable, userTable, xml);
                
                xml.append("</message>").append(dbUtils.NEWL);
                
            }
            return xml.toString();
            
        }
    
 
    /**
    TrunCate the string to the length specified
    @param string
    @param length
    @return string
    */
    public static String truncateMsg(String text, int length) {
        String space = " ";
        String dot_dot_dot = ".....";
        if( text.length() <= length )
            return text;
        else {
            text = text.substring(0, length);
            int index = text.lastIndexOf(space);
            if( index == -1 )
                return text + dot_dot_dot;
            else
                return text.substring(0, index) + dot_dot_dot;
        }
        
    }
 
 
    /**
    Get all messages belong to the parent id from the hashtable recursively
    @param parent id
    @param hashtable - key : parent id, element : vector of message
    @param hashtable - key : usr_id, element : user display bil
    @param string of xml will append to the stringbuffer
    @return XML
    */
    public void getChildMessageAsXML(Long parent_id, Hashtable parentIdTable, Hashtable userTable, StringBuffer xml)
        throws qdbException {                        
            Vector msgVec;
            dbForumMessage msg;
            if( parentIdTable.containsKey(parent_id) ){ 
                
                msgVec = (Vector)parentIdTable.get(parent_id);
                parentIdTable.remove(parent_id);
                for(int i=0; i<msgVec.size(); i++) {
                    
                    msg = (dbForumMessage)msgVec.elementAt(i);
                
                    xml.append("<message id=\"").append(msg.fmg_id).append("\" ")
                       .append(" topicId=\"").append(msg.fmg_fto_id).append("\" ")
                       .append(" modID=\"").append(msg.fmg_fto_res_id).append("\" ")
                       .append(" modImage=\"").append(msg.fmg_image).append("\" ")
                       .append(" createBy=\"").append(dbUtils.esc4XML((String)userTable.get(msg.fmg_usr_id))).append("\" ")
                       .append(" parentId=\"").append(msg.fmg_fmg_id_parent).append("\" ")
                       .append(" createDatetime=\"").append(msg.fmg_create_datetime).append("\">").append(dbUtils.NEWL)
                       .append("<subject>").append(cwUtils.escNull(dbUtils.esc4XML(msg.fmg_title))).append("</subject>")
                       .append("<body>").append(dbUtils.esc4XML(msg.fmg_msg)).append("</body>").append(dbUtils.NEWL);
                
                    getChildMessageAsXML(new Long(msg.fmg_id), parentIdTable, userTable, xml);
                
                    xml.append("</message>").append(dbUtils.NEWL);
                }
                
            }                        
                
        }
        
    public static Hashtable getMsgCount(Connection con, String topic_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("select fmg_fto_id, count(fmg_id) as num_msg from ForumMessage where fmg_fto_id in " + topic_lst + " group by fmg_fto_id");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            long fto_id = rs.getLong("fmg_fto_id");
            long count = rs.getLong("num_msg");
            hash.put(new Long(fto_id), new Long(count));
        }

        stmt.close();
        
        return hash;
    }           

    private static String formatContent4Reply(String fmg_msg, String usr_display_bil) {
        String replyContent = null;
        if(fmg_msg!=null && fmg_msg.length()>0) {
            Perl5Util perl = new Perl5Util();
            replyContent = new String(fmg_msg);

            if(perl.match("#<HR noshade>#i", replyContent)) {
                replyContent = perl.substitute("s#<HR noshade>#</BLOCKQUOTE><HR noshade>#i", replyContent);
            } else {
                replyContent = replyContent + "</BLOCKQUOTE>";
            }
            replyContent = "<BR/><BR/><HR noshade><BLOCKQUOTE><P>" + usr_display_bil + " wrote:</P>" + replyContent;
            
            /*
            replyContent = perl.substitute("s#<P>#<P>>#ig", replyContent);
            replyContent = perl.substitute("s#<BR>#<BR>>#ig", replyContent);
            replyContent = perl.substitute("s#<BR/>#<BR/>>#ig", replyContent);
            replyContent = perl.substitute("s#<HR>[^\\(<P>\\)]#<HR>>#ig", replyContent);
            //replyContent = perl.substitute("s#\n#\n>#ig", replyContent);
            //replyContent = perl.substitute("s#\r#\r>#ig", replyContent);
            //replyContent = perl.substitute("s#\t#\t>#ig", replyContent);
            //replyContent = perl.substitute("s#\f#\f>#ig", replyContent);
            if(!replyContent.startsWith("<P>")) {
                replyContent = ">" + replyContent;
            }
            replyContent = "<BR/><BR/><HR>" + replyContent;
            */
        }
        return replyContent;
    }
    
    public static List getTopicMgs(Connection con, long fto_id) throws qdbException {
        try {
            List all_msg = new ArrayList();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM ForumMessage where fmg_fto_id = ? order by fmg_id ASC ");
            // set the values for prepared statements
            stmt.setLong(1, fto_id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dbForumMessage fmg = new dbForumMessage();
                fmg.fmg_id = rs.getLong("fmg_id");
                fmg.fmg_fto_id = rs.getLong("fmg_fto_id");
                fmg.fmg_fto_res_id = rs.getLong("fmg_fto_res_id");
                fmg.fmg_msg = cwSQL.getClobValue(rs, "fmg_msg");
                fmg.fmg_image = rs.getString("fmg_image");
                fmg.fmg_usr_id = rs.getString("fmg_usr_id");
                fmg.fmg_fmg_id_parent = rs.getLong("fmg_fmg_id_parent");
                fmg.fmg_fmg_fto_id_parent = rs.getLong("fmg_fmg_fto_id_parent");
                fmg.fmg_fmg_fto_res_id_parent = rs.getLong("fmg_fmg_fto_res_id_parent");
                fmg.fmg_create_datetime = rs.getTimestamp("fmg_create_datetime");
                fmg.fmg_type = rs.getString("fmg_type");
                fmg.fmg_title = rs.getString("fmg_title");
                fmg.fmg_msg_searchable_content = cwSQL.getClobValue(rs, "fmg_msg_searchable_content");
                all_msg.add(fmg);
            } 
           
            stmt.close();
            return all_msg;
        } catch (SQLException e) {
        	throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public static Vector getFmgIdByResId(Connection con, long fto_fto_res_id) throws qdbException {
    	Vector fmgIdVec = new Vector();
    	try {
    		PreparedStatement stmt = con.prepareStatement("SELECT fmg_id FROM ForumMessage where fmg_fto_res_id = ? order by fmg_id ASC ");
    		int index = 1;
    		stmt.setLong(index++, fto_fto_res_id);
    		ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
            	long fmg_id = rs.getLong("fmg_id");
            	fmgIdVec.add(new Long(fmg_id));
            }
    	} catch(SQLException e) {
    		throw new qdbException("SQL Error: " + e.getMessage());
    	}
    	return fmgIdVec;
    }
    
    public static void delFilesOfFmg(Connection con, long forResId) throws qdbException {
    	//delete file of announ
    	Vector fmgIdVec = getFmgIdByResId(con, forResId);
    	for(int fmgIdIndex = 0; fmgIdIndex < fmgIdVec.size(); fmgIdIndex++) {
    		 String fmgId = (Long)fmgIdVec.elementAt(fmgIdIndex) + "";
    		 delDirByFmgId(fmgId);
    	}
    }

	private static void delDirByFmgId(String fmgId) {
		if(fmgId != null && fmgId.trim().length() > 0) {
			String msgDirAbsPath;
			msgDirAbsPath = Dispatcher.getWizbini().getFileUploadAnnDirAbs() + cwUtils.SLASH + MSG_TYPE_FMG + fmgId;
			dbUtils.delDir(msgDirAbsPath);
		}
	}

	// //////bookreview by user id
	public static dbForumMessage messageOfBookReview(Connection conn, long review_id) throws SQLException {
		int index = 1;
		String sql = "select res_id,fto_id,fmg_id,fmg_msg,fmg_usr_id,fmg_title, " + " fmg_create_datetime,fmg_update_timestamp,fmg_rating,usr_display_bil,fto_nod_id,obj_title "
				+ " from resources,forumtopic,forummessage,reguser,kmObject " + " where res_id=fto_res_id " + " and res_id=fmg_fto_res_id " + " and fmg_usr_id=usr_id "
				+ " and fto_nod_id=obj_bob_nod_id " + " and fmg_id=?" + " order by fmg_update_timestamp ";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(index++, review_id);
		ResultSet rs = stmt.executeQuery();
		dbForumMessage fm = null;
		if (rs.next()) {
			fm = new dbForumMessage();
			fm.fmg_fto_res_id = rs.getLong("res_id");
			fm.fmg_fto_id = rs.getLong("fto_id");
			fm.fmg_id = rs.getLong("fmg_id");
			fm.fmg_msg = rs.getString("fmg_msg");
			fm.fmg_usr_id = rs.getString("fmg_usr_id");
			fm.fmg_create_datetime = rs.getTimestamp("fmg_create_datetime");
//			fm.fmg_update_datetime = rs.getTimestamp("fmg_update_timestamp");
//			fm.usr_bill = rs.getString("usr_display_bil");
//			fm.fmg_rating = rs.getInt("fmg_rating");
			fm.fmg_title = rs.getString("fmg_title");
//			fm.fto_nod_id = rs.getLong("fto_nod_id");
//			fm.itm_title = rs.getString("obj_title");
		}
		stmt.close();
		return fm;
	}
	
}
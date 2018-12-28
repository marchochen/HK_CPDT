package com.cw.wizbank.qdb;

import java.sql.*;

public class dbForumMarkMsg {    
    public long      fmm_fmg_id;
    public long      fmm_fmg_fto_id;
    public long      fmm_fmg_fto_res_id;
    public String    fmm_usr_id;
        
    public dbForumMarkMsg() {
        ;   
    }
    
    public static void ins(Connection con, String usr_id, long res_id, long fto_id, long fmg_id)
        throws qdbException, qdbErrMessage
    {        
        try {
            PreparedStatement stmt = con.prepareStatement(
                " INSERT INTO ForumMarkMsg "
                + " ( fmm_fmg_id "
                + " , fmm_fmg_fto_id "
                + " , fmm_fmg_fto_res_id "
                + " , fmm_usr_id "
                + " ) "
                + " VALUES (?,?,?,?) "); 

            stmt.setLong(1, fmg_id);
            stmt.setLong(2, fto_id);
            stmt.setLong(3, res_id);
            stmt.setString(4, usr_id);

            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)                            
            {
                con.rollback();
                throw new qdbException("Failed to mark message(s) as read.");
            }        
                        
            
        }catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static void del(Connection con, String usr_id, long fmg_id)
        throws qdbException, qdbErrMessage
    {
        try {             
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From ForumMarkMsg where fmm_fmg_id = ? AND fmm_usr_id = ? ");
            
            stmt.setLong(1, fmg_id);
            stmt.setString(2, usr_id);

            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1)            
            {
                con.rollback();
                throw new qdbException("Fails to delete ForumMarkMsg. Message id = " + fmg_id);
            }     
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }   
    
    public static void delByMsg(Connection con, long fmg_id)
        throws qdbException, qdbErrMessage
    {        
        try {             
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From ForumMarkMsg WHERE fmm_fmg_id = ? ");
            
            stmt.setLong(1, fmg_id);
            stmt.executeUpdate();
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }

    public static void delByTopic(Connection con, long topic_id) 
        throws qdbException, qdbErrMessage
    {
        try {             
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From ForumMarkMsg where fmm_fmg_fto_id = ? ");
            
            stmt.setLong(1, topic_id);
            stmt.executeUpdate();
            stmt.close();

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }   
    
    public static void delByForum(Connection con, long res_id) 
        throws qdbException, qdbErrMessage
    {
        try {             
            PreparedStatement stmt = con.prepareStatement(
                " DELETE From ForumMarkMsg where fmm_fmg_fto_res_id = ? ");
            
            stmt.setLong(1, res_id);
            stmt.executeUpdate();
            stmt.close();
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }
    }   
        
    public static String isMsgRead(Connection con, loginProfile prof, long msg_id)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                " SELECT fmm_fmg_id FROM ForumMarkMsg where fmm_fmg_id = ? AND fmm_usr_id = ? ");
                
            stmt.setLong(1, msg_id);
            stmt.setString(2, prof.usr_id);
            ResultSet rs = stmt.executeQuery();
            
            String status = new String();
            if (rs.next()) {
                status = "TRUE";
            } else {
                status = "FALSE";
            }
            
            stmt.close();
            return status ; 
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }     
    }
    
    public static long numOfReadMsgFromTopic(Connection con, long fto_id, String usr_id)
        throws qdbException
    {
        try {
            PreparedStatement stmt = con.prepareStatement(
                " SELECT COUNT(*) FROM ForumMarkMsg where fmm_fmg_fto_id = ? AND fmm_usr_id = ? ");
                
            stmt.setLong(1, fto_id);
            stmt.setString(2, usr_id);
            ResultSet rs = stmt.executeQuery();
            long cnt =0;          
            
            if(rs.next()) {
                cnt = rs.getLong(1); 
            } else {
                throw new qdbException("Error : Cannot get the numOFReadMsgFromTopic. ");
            }
            
            stmt.close();
            return cnt;
            
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }     
    }
}
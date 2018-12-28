package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.newmessage.MessageDao;
import com.cw.wizbank.util.*;

public class DbItemMessage {
    
    public final static String TYPE_JI = "JI";
    public final static String TYPE_REMINDER = "REMINDER";
    
    public long img_itm_id;
    public long img_msg_id;
    public Timestamp img_create_timestamp;
    public Timestamp img_update_timestamp;
    public String img_create_usr_id;
    public String img_update_usr_id;
    public String img_msg_type;
    public String img_mtp_type;
    public long img_app_id;
        
  
    
    public DbItemMessage() {
    }
    
    
    public void ins(Connection con) throws SQLException{
            String sql = "INSERT INTO aeItemMessage " +
                " (img_itm_id, img_app_id, img_msg_id, img_msg_type, img_mtp_type, img_create_timestamp, img_create_usr_id, " +
                " img_update_timestamp, img_update_usr_id) values (?, ?, ?, ?, ?, ?, ?,?, ?) ";
            
            
            PreparedStatement stmt = con.prepareStatement(sql);
            int para = 1;
            
            Timestamp ts = cwSQL.getTime(con);    
            stmt.setLong(para++, img_itm_id);  
            stmt.setLong(para++, img_app_id);
            stmt.setLong(para++, img_msg_id);
            stmt.setString(para++, img_msg_type);
            stmt.setString(para++, img_mtp_type);
            stmt.setTimestamp(para++, ts);
            stmt.setString(para++, img_create_usr_id);
            stmt.setTimestamp(para++, ts);
            stmt.setString(para++, img_update_usr_id);
                                    
            if (stmt.executeUpdate()!= 1) {
            	stmt.close();
                con.rollback();
                throw new SQLException("Failed to insert ItemMessage.");
            }
            stmt.close();
        } 
    
   public static void delByItem(Connection con, long itm_id) throws SQLException{
            String sql = "delete from aeItemMessage where" +
                " img_itm_id = ?";
            
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, itm_id);
            stmt.executeUpdate();
            
            stmt.close();
        } 
        
   public static void delByApp(Connection con, long app_id) throws SQLException{
       String sql = "delete from aeItemMessage where" +
           " img_app_id = ?";
       
       
       PreparedStatement stmt = con.prepareStatement(sql);
       stmt.setLong(1, app_id);
       stmt.executeUpdate();
       
       stmt.close();
   }
   
   public static void delByAppAndMtpType(Connection con, Vector<Long> msg_id_vec, String mtp_type) throws SQLException{
   	   StringBuffer sql = new StringBuffer();
   	   sql.append("delete from aeItemMessage WHERE img_msg_id in ( ");
       for(int i = 0 ; i < msg_id_vec.size(); i++) {
       	sql.append("'" + msg_id_vec.get(i) + "'").append(",");
       }
       sql.delete(sql.length() - 1, sql.length());
       sql.append(")");
       
       sql.append(" and img_mtp_type in (");
       String[] mtp_types = mtp_type.split(",");
       for(int i = 0 ; i < mtp_types.length; i++) {
       	String str = mtp_types[i];
       	sql.append("'" + str + "'").append(",");
       }
       sql.delete(sql.length() - 1, sql.length());
       sql.append(")");
       
       PreparedStatement stmt = con.prepareStatement(sql.toString());
       stmt.executeUpdate();
       
       stmt.close();
   } 
   
	public static Vector<Long> getMsgByItem(Connection con, long itm_id, String mtp_type, String msg_type) throws SQLException {
		 String sql = " SELECT img_msg_id FROM aeItemMessage "
            		+ " WHERE img_itm_id = ? and img_mtp_type = ? and img_msg_type = ?";

		 PreparedStatement stmt = con.prepareStatement(sql);
		 int index=1;
		 stmt.setLong(index++, itm_id);
		 stmt.setString(index++, mtp_type);
		 stmt.setString(index++, msg_type);
		
		 ResultSet rs = stmt.executeQuery();
		 Vector<Long> result = new Vector<Long>();
		 while( rs.next() ) {
		     result.addElement(rs.getLong("img_msg_id"));
		 }
		 cwSQL.cleanUp(rs, stmt);
		 return result;	
	}
	
    public static Vector<Long> getMsgByAppId(Connection con, long img_app_id, String msg_type,String mtp_type)
    throws SQLException, cwException {
    	
    	String[] mtp_types = mtp_type.split(",");
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT img_msg_id, img_mtp_type, img_msg_type from aeItemMessage WHERE img_app_id = ? and img_msg_type = ? and img_mtp_type in ( ");
        for(int i = 0 ; i < mtp_types.length; i++) {
        	String str = mtp_types[i];
        	sql.append("'" + str + "'").append(",");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(")");
        if(msg_type.equalsIgnoreCase(MessageDao.MESSAGE_CONTENT_TYPE_EMAIL)) {
        	sql.append(" and EXISTS (select 1 from emailMsgRecHistory where emrh_emsg_id = img_msg_id and emrh_status = 'N')");
        }
        
        if(msg_type.equalsIgnoreCase(MessageDao.MESSAGE_CONTENT_TYPE_PC)) {
        	sql.append(" and EXISTS (select 1 from webMessage where wmsg_id = img_msg_id and wmsg_target_datetime > GETDATE())");
        }

        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index=1;
        stmt.setLong(index++, img_app_id);
		stmt.setString(index++, msg_type);

        ResultSet rs = stmt.executeQuery();
        Vector<Long> result = new Vector<Long>();
        while( rs.next() ) {
		     result.addElement(rs.getLong("img_msg_id"));
		 }
        cwSQL.cleanUp(rs, stmt);
        return result;
    }
}
package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;


public class DbMgItmSelectedMessage {
    
    public long ism_itm_id;
    public long ism_msg_id;
    public String ism_type;
    
    
    // insert a record into item selected messaage table
    public void ins(Connection con)
        throws SQLException, cwException {
         
            String DbMgItmSelectedMessage_INS = " INSERT INTO mgitmSelectedMessage "
                                               + " ( ism_itm_id, ism_msg_id, ism_type ) "
                                               + " VALUES ( ?, ?, ? ) ";

            PreparedStatement stmt = con.prepareStatement(DbMgItmSelectedMessage_INS);
            stmt.setLong(1, ism_itm_id);
            stmt.setLong(2, ism_msg_id);
            stmt.setString(3, ism_type);
            
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to insert a record into item selected message table , item id = " + ism_itm_id + " , message id = " + ism_msg_id );
            
            stmt.close();
            return;            
        }
    
    
    /// get the message id by specific item id and message type (eg. tna, ji....)
    public static long[] getMessageIds(Connection con, long itm_id, String type)
        throws SQLException {
            
            Vector msgIdsVec = new Vector();
            
            String DbMgItmSelectedMessage_GET_MESSAGE_ID = " SELECT ism_msg_id FROM mgitmSelectedMessage "
                                                          + " WHERE ism_itm_id = ? AND ism_type = ? ";
            
            PreparedStatement stmt = con.prepareStatement(DbMgItmSelectedMessage_GET_MESSAGE_ID);
            stmt.setLong(1, itm_id);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            
            while( rs.next() )
                msgIdsVec.addElement(new Long(rs.getLong("ism_msg_id")));

            stmt.close();
            if(msgIdsVec.isEmpty())
                return new long[0];
            else {
                long[] msg_ids = new long[msgIdsVec.size()];
                for(int i=0; i<msg_ids.length; i++)
                    msg_ids[i] = ((Long)msgIdsVec.elementAt(i)).longValue();                
                return msg_ids;
            }
            
        }
        
        
    public static void delRecords(Connection con, long msg_id)
        throws SQLException, cwException {
            
            String DbMgItmSelectedMessage_DEL_RECORDS = " DELETE From mgitmSelectedMessage "
                                                      + " WHERE ism_msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgItmSelectedMessage_DEL_RECORDS);
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            /*
            if( stmt.executeUpdate() < 1 )
                throw new cwException("Failed to delete record in item selected table , message id = " + msg_id );
            */
            stmt.close();
            return;
        }
        
        
}    
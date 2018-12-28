package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;


public class DbMgTnaSelectedMessage {
    
    public long tsm_tna_id;
    public long tsm_msg_id;
    
    public void ins(Connection con)
        throws SQLException, cwException {
            
            String DbMgTnaSelectedMessage_INS = " INSERT INTO mgtnaSelectedMessage "
                                              + " ( tsm_tna_id, tsm_msg_id ) "
                                              + " VALUES ( ?, ? ) ";
            
            PreparedStatement stmt = con.prepareStatement(DbMgTnaSelectedMessage_INS);
            stmt.setLong(1, tsm_tna_id);
            stmt.setLong(2, tsm_msg_id);
            
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to insert a record into tna selected message table, tna id = " + tsm_tna_id + " , message id = " + tsm_msg_id );
            stmt.close();
            return;            
        }       
        
        
    public static long[] getMessageIds(Connection con, long tna_id)
        throws SQLException {
            
            Vector msgIdsVec = new Vector();
            
            String DbMgTnaSelectedMessage_GET_MESSAGE_ID = " SELECT tsm_msg_id FROM mgtnaSelectedMessage "
                                                         + " WHERE tsm_tna_id = ? " ;
                                                         
            PreparedStatement stmt = con.prepareStatement(DbMgTnaSelectedMessage_GET_MESSAGE_ID);
            stmt.setLong(1, tna_id);
            
            ResultSet rs = stmt.executeQuery();
            while( rs.next() )
                msgIdsVec.addElement(new Long(rs.getLong("tsm_msg_id")));
        
            long[] msg_ids = new long[msgIdsVec.size()];
            for(int i=0; i<msg_ids.length; i++)
                msg_ids[i] = ((Long)msgIdsVec.elementAt(i)).longValue();
            
            stmt.close();
            return msg_ids;
        }
}
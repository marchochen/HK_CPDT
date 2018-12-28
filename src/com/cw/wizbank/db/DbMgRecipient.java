package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class DbMgRecipient {
        
    public final static String CARBON_COPY      =   "CARBONCOPY";
    public final static String RECIPIENT        =   "RECIPIENT";
    
    public final static String COMMA            =   ",";
    
    public long rec_id;
    public long rec_msg_id;
    public long rec_ent_id;
    public String rec_type;
    
    
    //insert a new message recipient
    public void ins(Connection con)
        throws SQLException, cwException {

            String DbMgRecipient_INS = " INSERT INTO mgRecipient "
                                     + " ( rec_msg_id, rec_ent_id, rec_type ) "
                                     + " VALUES( ?, ?, ? ) ";

            PreparedStatement stmt = con.prepareStatement(DbMgRecipient_INS, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, rec_msg_id);
            stmt.setLong(2, rec_ent_id);
            stmt.setString(3, rec_type);
            
            if( stmt.executeUpdate() != 1 ){
            	stmt.close();
                throw new cwException("Failed to insert into a Recipient table.");
            }
            this.rec_id = cwSQL.getAutoId(con, stmt, "mgRecipient", "rec_id");
            stmt.close();    
            return;
        }
        
    public static long getMaxId(Connection con)
        throws SQLException, cwException {
            
            String DbMgRecipient_GET_MAX_ID = " SELECT MAX(rec_id) FROM mgRecipient ";
            PreparedStatement stmt = con.prepareStatement(DbMgRecipient_GET_MAX_ID);
            ResultSet rs = stmt.executeQuery();
            long id;
            if( rs.next() ) 
                id = rs.getLong(1);            
            else{
            	if(rs!=null)rs.close();
            	stmt.close();
                throw new cwException("Failed to get the recipient id.");
            }
            rs.close();
            stmt.close();
            return id;
        }
        
        
/*    
    // update send status and sent datetime
    public void upd(Connection con)
        throws SQLException, cwException {
            
            String DbMgRecipient_UPD = " UPDATE mgRecipient "
                                     + " SET rec_send_status = ? , rec_sent_datetime = ? "
                                     + " WHERE rec_msg_id = ? AND rec_ent_id = ? ";
            
            
            PreparedStatement stmt = con.prepareStatement(DbMgRecipient_UPD);
            stmt.setString(1, rec_send_status);
            stmt.setTimestamp(2, rec_sent_datetime);
            stmt.setLong(3, rec_msg_id);
            stmt.setLong(4, rec_ent_id);
            
            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update a Recipient table of msg_id = " + rec_msg_id);

            return;
        }
*/

/*
    //get the message should be sent
    //return a vector with element of message id and recipient id
    public static Vector getSendMessage(Connection con) 
        throws SQLException, cwException {
            
            Vector idVec = new Vector();
            Vector msgIdVec = new Vector();
            Vector entIdVec = new Vector();
            Timestamp curTime = null;
            try{
                curTime = dbUtils.getTime(con);
            }catch(qdbException e) {
                throw new cwException ("Failed to get timestamp from database : " + e);
            }
            
            
            String DbMgRecipient_GET_SEND_MESSAGE = " SELECT rec_msg_id, rec_ent_id "
                                                  + " FROM mgRecipient, mgMessage "
                                                  + " WHERE rec_send_status = ? "
                                                  + " AND   msg_target_datetime < ? "
                                                  + " AND   msg_id = rec_msg_id " ;
                                                  
            
            PreparedStatement stmt = con.prepareCall(DbMgRecipient_GET_SEND_MESSAGE);
            stmt.setString(1, NO);
            stmt.setTimestamp(2, curTime);
            
            ResultSet rs = stmt.executeQuery();
            
            while( rs.next() ) {
                msgIdVec.addElement( new Long(rs.getLong("rec_msg_id")));
                entIdVec.addElement( new Long(rs.getLong("rec_ent_id")));
            }
            
            idVec.addElement(msgIdVec);
            idVec.addElement(entIdVec);
            return idVec;
        }
*/



    public static void delRecords(Connection con, long msg_id)
        throws SQLException, cwException {

            String DbMgRecipient_DEL_RECORDS = " DELETE From mgRecipient "
                                             + " WHERE rec_msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgRecipient_DEL_RECORDS);            
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }


    public static void delRecords(Connection con, long[] msg_id)
        throws SQLException, cwException {

            String DbMgRecipient_DEL_RECORDS = " DELETE From mgRecipient "
                                             + " WHERE rec_msg_id IN " + cwUtils.array2list(msg_id);

            PreparedStatement stmt = con.prepareStatement(DbMgRecipient_DEL_RECORDS);
            stmt.executeUpdate();


            stmt.close();
            return;
        }
        

    public static void delRecords(Connection con, long msg_id, long[] rec_ent_id)
        throws SQLException, cwException {

            StringBuffer str = new StringBuffer().append("(0");
            for(int i=0; i<rec_ent_id.length; i++)
                str.append(COMMA).append(rec_ent_id[i]);
            str.append(")");            

            String DbMgRecipient_DEL_RECORDS = " DELETE From mgRecipient "
                                             + " WHERE rec_msg_id = ? "
                                             + " AND   rec_ent_id IN "
                                             + str.toString();

            PreparedStatement stmt = con.prepareStatement(DbMgRecipient_DEL_RECORDS);
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }

    public static void delNotInList(Connection con, long[] msg_id_list, long[] rec_ent_id_list)
        throws SQLException{
            
            if( msg_id_list == null || msg_id_list.length == 0 )
                return;
            
            String SQL = " DELETE FROM mgRecipient "
                       + " WHERE rec_msg_id IN " + cwUtils.array2list(msg_id_list);
            if( rec_ent_id_list != null && rec_ent_id_list.length > 0 )
                SQL += " AND rec_ent_id NOT IN " + cwUtils.array2list(rec_ent_id_list);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
            
            
        }


    public static Vector getRecipientId(Connection con, long msg_id, String rec_type, Vector vec)
        throws SQLException {
            
            String SQL = " SELECT rec_id FROM mgRecipient "
                       + " WHERE rec_msg_id = ? "
                       + " AND rec_type = ? "
                       + " AND rec_ent_id IN " + cwUtils.vector2list(vec);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, msg_id);
            stmt.setString(2, rec_type);
            ResultSet rs = stmt.executeQuery();
            Vector recIdVec = new Vector();
            while(rs.next())
                recIdVec.addElement(new Long(rs.getLong("rec_id")));
            stmt.close();
            return recIdVec;
        }


    public void delByRecId(Connection con, Vector recIdVec)
        throws SQLException {
            
            if( recIdVec.isEmpty() )
                return;
            String SQL = " DELETE FROM mgRecipient "
                       + " WHERE rec_id IN " + cwUtils.vector2list(recIdVec);
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
        }


}
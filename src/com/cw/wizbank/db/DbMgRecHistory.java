package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class DbMgRecHistory {
    
    public long mgh_rec_id;
    public long mgh_mst_msg_id;
    public long mgh_mst_xtp_id;
    public String mgh_status;
    public Timestamp mgh_sent_datetime;
    public int mgh_attempted;
    
    public final static String NO       =   "N";
    public final static String YES      =   "Y";
    
    public final static String COMMA    =   ",";
    
    public void ins(Connection con) 
        throws SQLException, cwException {
        
            String DbMgRecHistory_INS = " INSERT INTO mgRecHistory "
                                      + " ( mgh_rec_id, mgh_mst_msg_id, mgh_mst_xtp_id, "
                                      + "   mgh_status, mgh_sent_datetime ) "
                                      + " VALUES( ?, ?, ?, ?, ? ) ";
                                      
            PreparedStatement stmt = con.prepareStatement(DbMgRecHistory_INS);
            stmt.setLong(1, mgh_rec_id);
            stmt.setLong(2, mgh_mst_msg_id);
            stmt.setLong(3, mgh_mst_xtp_id);
            stmt.setString(4, NO);
            stmt.setTimestamp(5, null);
            
            if( stmt.executeUpdate() != 1 ){
            	stmt.close();
                throw new cwException("Failed to insert a new Recipient History.");
            }
            stmt.close();
            return;
        }
        
    public void ins(Connection con, String status) 
        throws SQLException, cwException {
        
            String DbMgRecHistory_INS = " INSERT INTO mgRecHistory "
                                      + " ( mgh_rec_id, mgh_mst_msg_id, mgh_mst_xtp_id, "
                                      + "   mgh_status, mgh_sent_datetime ) "
                                      + " VALUES( ?, ?, ?, ?, ? ) ";
                                      
            PreparedStatement stmt = con.prepareStatement(DbMgRecHistory_INS);
            stmt.setLong(1, mgh_rec_id);
            stmt.setLong(2, mgh_mst_msg_id);
            stmt.setLong(3, mgh_mst_xtp_id);
            stmt.setString(4, status);
            stmt.setTimestamp(5, null);
            
            if( stmt.executeUpdate() != 1 ){
            	stmt.close();
                throw new cwException("Failed to insert a new Recipient History.");
            }
            
            stmt.close();
            return;
        }
        
        
    public void get(Connection con)
        throws SQLException, cwException {
            
            String SQL = " SELECT mgh_attempted, mgh_status, mgh_sent_datetime "
                       + " FROM mgRecHistory "
                       + " WHERE mgh_rec_id = ? AND mgh_mst_xtp_id = ? AND mgh_mst_msg_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, mgh_rec_id);
            stmt.setLong(2, mgh_mst_xtp_id);
            stmt.setLong(3, mgh_mst_msg_id);
            
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                mgh_status = rs.getString("mgh_status");
                mgh_sent_datetime = rs.getTimestamp("mgh_sent_datetime");
                mgh_attempted = rs.getInt("mgh_attempted");
            } else 
                throw new cwException ("Error, record not find, rec_id = " + mgh_rec_id);
            
            stmt.close();
        }
 
    public void upd(Connection con)
        throws SQLException, cwException {
            
            String DbMgRecHistory_UPD = " UPDATE mgRecHistory SET "
                                      + " mgh_status = ? , mgh_sent_datetime = ?, mgh_attempted = ? "
                                      + " WHERE mgh_rec_id = ? AND mgh_mst_msg_id = ? "
                                      + " AND mgh_mst_xtp_id = ? ";
            PreparedStatement stmt = con.prepareStatement(DbMgRecHistory_UPD);
            stmt.setString(1, mgh_status);
            stmt.setTimestamp(2, mgh_sent_datetime);
            stmt.setLong(3, mgh_attempted);
            stmt.setLong(4, mgh_rec_id);            
            stmt.setLong(5, mgh_mst_msg_id);
            stmt.setLong(6, mgh_mst_xtp_id);

            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update Recipient history, rec_id : " + mgh_rec_id + " , msg_id : " + mgh_mst_msg_id + " , xtp_id : " + mgh_mst_xtp_id );
    
            stmt.close();
            return;
        }
 
        
    public static void delRecords(Connection con, long msg_id)
        throws SQLException, cwException {
            
            String DbMgRecHistory_DEL_RECORDS = " DELETE From mgRecHistory "
                                              + " WHERE mgh_mst_msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgRecHistory_DEL_RECORDS);
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            //if( stmt.executeUpdate() < 1 )
            //    throw new cwException("Failed to delete record in recipient history table , message id = " + msg_id );
                
            stmt.close();
            return;
        }
        

    public static void delRecords(Connection con, long[] msg_id)
        throws SQLException, cwException {
            
            String DbMgRecHistory_DEL_RECORDS = " DELETE From mgRecHistory "
                                              + " WHERE mgh_mst_msg_id IN " + cwUtils.array2list(msg_id);

            PreparedStatement stmt = con.prepareStatement(DbMgRecHistory_DEL_RECORDS);
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
            
            String DbMgRecHistory_DEL_RECORDS = " DELETE From mgRecHistory "
                                              + " WHERE mgh_rec_id IN "
                                              + " ( SELECT rec_id "
                                              + "   FROM mgRecipient " 
                                              + "   WHERE rec_msg_id = ? "
                                              + "   AND   rec_ent_id IN "
                                              +     str.toString() 
                                              + " ) ";

            PreparedStatement stmt = con.prepareStatement(DbMgRecHistory_DEL_RECORDS);
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            
            stmt.close();
            return;
            
        }

    public static void delNotInList(Connection con, long[] msg_id_list, long[] rec_ent_id_list)
        throws SQLException{
            
            if( msg_id_list == null || msg_id_list.length == 0 )
                return;
            
            String SQL = " DELETE FROM mgRecHistory "
                       + " WHERE mgh_rec_id IN " 
                       + " ( SELECT rec_id "
                       + "   FROM mgRecipient "
                       + "   WHERE rec_msg_id IN " + cwUtils.array2list(msg_id_list);
                    
            if( rec_ent_id_list != null && rec_ent_id_list.length > 0 )   
                SQL += "   AND rec_ent_id NOT IN " + cwUtils.array2list(rec_ent_id_list);
                       
            SQL += " ) ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
            
            
        }


    public void updRecStatus(Connection con, Vector vec)
        throws SQLException {
            
            String SQL = " UPDATE mgRecHistory "
                       + " SET mgh_status = ?, "
                       + " mgh_sent_datetime = ? "
                       //+ " mgh_attempted = ? "
                       + " WHERE mgh_rec_id IN " + cwUtils.vector2list(vec);
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, mgh_status);
            stmt.setTimestamp(2, mgh_sent_datetime);
            //stmt.setLong(3, mgh_attempted);
            stmt.executeUpdate();
            stmt.close();
            return;
            
    }

    
    public static void updRecStatus(Connection con, long msgId, String mgh_status, Timestamp mgh_sent_datetime, long mgh_attempted)
        throws SQLException {
            
            String SQL = " UPDATE mgRecHistory "
                       + " SET mgh_status = ?, "
                       + " mgh_sent_datetime = ?, "
                       + " mgh_attempted = ? "
                       + " WHERE mgh_mst_msg_id = " + msgId ;
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, mgh_status);
            stmt.setTimestamp(2, mgh_sent_datetime);
            stmt.setLong(3, mgh_attempted);
            stmt.executeUpdate();
            stmt.close();
            return;
            
    }

    public void delByRecId(Connection con, Vector recIdVec)
        throws SQLException {
            
            if( recIdVec.isEmpty() )
                return;
            String SQL = " DELETE FROM mgRecHistory "
                       + " WHERE mgh_rec_id IN " + cwUtils.vector2list(recIdVec);
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
        }


}
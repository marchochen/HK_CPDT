package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class DbCmAssessmentNotify {
    
    public final static String NOTIFICATION = "NOTIFICATION";
    public final static String COLLECTION = "COLLECTION";
    
    public long asn_asm_id;
    public long asn_msg_id;
    public String asn_type;
    public String asn_asu_type;
    
    public void ins(Connection con)
        throws SQLException {
            
            String SQL = " INSERT INTO cmAssessmentNotify "
                       + " ( asn_asm_id, asn_msg_id, asn_type, asn_asu_type) " 
                       + " VALUES (?, ?, ?, ?) ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asn_asm_id);
            stmt.setLong(2, asn_msg_id);
            stmt.setString(3, asn_type);
            stmt.setString(4, asn_asu_type);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to insert Assessment Nofity Record");
            stmt.close();
            return;
        }
    
    public static long[] getMessageIdListByAsuType(Connection con, long asm_id, String asu_type)
        throws SQLException{
            
            String SQL = " SELECT asn_msg_id FROM cmAssessmentNotify "
                       + " WHERE asn_asm_id = ? " 
                       + " AND asn_asu_type = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            stmt.setString(2, asu_type);
            ResultSet rs = stmt.executeQuery();
            Vector vec = new Vector();
            while(rs.next())
                vec.addElement(new Long(rs.getLong("asn_msg_id")));
            stmt.close();
            long[] msg_id_list = new long[vec.size()];
            for(int i=0; i<msg_id_list.length; i++)
                msg_id_list[i] = ((Long)vec.elementAt(i)).longValue();
            return msg_id_list;
        }

    
    public static long[] getAssessorMessageIdList(Connection con, long asm_id)
        throws SQLException{
            
            String SQL = " SELECT asn_msg_id FROM cmAssessmentNotify "
                       + " WHERE asn_asm_id = ? " 
                       + " AND asn_asu_type <> ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            stmt.setString(2, "RESOLVED");
            ResultSet rs = stmt.executeQuery();
            Vector vec = new Vector();
            while(rs.next())
                vec.addElement(new Long(rs.getLong("asn_msg_id")));
            stmt.close();
            long[] msg_id_list = new long[vec.size()];
            for(int i=0; i<msg_id_list.length; i++)
                msg_id_list[i] = ((Long)vec.elementAt(i)).longValue();
            return msg_id_list;
        }

    public static long[] getMessageIdList(Connection con, long asm_id)
        throws SQLException{
            
            String SQL = " SELECT asn_msg_id FROM cmAssessmentNotify "
                       + " WHERE asn_asm_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            ResultSet rs = stmt.executeQuery();
            Vector vec = new Vector();
            while(rs.next())
                vec.addElement(new Long(rs.getLong("asn_msg_id")));
            stmt.close();
            long[] msg_id_list = new long[vec.size()];
            for(int i=0; i<msg_id_list.length; i++)
                msg_id_list[i] = ((Long)vec.elementAt(i)).longValue();
            return msg_id_list;
        }
        
        
    public static long getMessageId(Connection con, long asm_id, String asn_type)
        throws SQLException{
            
            String SQL = " SELECT asn_msg_id FROM cmAssessmentNotify "
                       + " WHERE asn_asm_id = ? AND asn_type = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            stmt.setString(2, asn_type);
            ResultSet rs = stmt.executeQuery();
            long msg_id = 0;
            if(rs.next())
                msg_id = rs.getLong("asn_msg_id");
            else
                throw new SQLException("Failed to ge the message id, asm_id = " + asm_id);
            stmt.close();
            return msg_id;
        }

    public static long getMessageId(Connection con, long asm_id, String asn_type, String asn_asu_type)
        throws SQLException{
            
            final String SQL = " SELECT asn_msg_id FROM cmAssessmentNotify "
                             + " WHERE asn_asm_id = ? AND asn_type = ? AND asn_asu_type = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, asm_id);
            stmt.setString(2, asn_type);
            stmt.setString(3, asn_asu_type);
            ResultSet rs = stmt.executeQuery();
            long msg_id = 0;
            if(rs.next())
                msg_id = rs.getLong("asn_msg_id");
            else
                throw new SQLException("Failed to ge the message id, asm_id = " + asm_id);
            stmt.close();
            return msg_id;
        }

        
    public static void delRecords(Connection con, long[] msg_id_list)
        throws SQLException{
            
            String SQL = " DELETE FROM cmAssessmentNotify "
                       + " WHERE asn_msg_id IN " + cwUtils.array2list(msg_id_list);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
}
    


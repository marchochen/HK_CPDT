package com.cw.wizbank.ae;

import java.sql.*;
import com.cw.wizbank.qdb.*;

public class aeAccount {
    private final static int ACCOUNT_NOT_EXIST = -1;
    
    //SQL statement for insert an account
    private final static String CREATE_ACCOUNT
    = " INSERT INTO aeAccount ( acn_ent_id, acn_type, acn_create_timestamp, acn_create_usr_id, acn_upd_timestamp, acn_upd_usr_id ) "
    + " VALUES ( ?, ?, ?, ?, ?, ? ) ";
        
    // SQL statement for get an account id    
    private final static String GET_ACCOUNT_ID
    = " SELECT acn_id AS ID FROM aeAccount WHERE acn_ent_id = ? AND acn_type = ? ";
                    
    // Insert an account for a user
    public static void ins(Connection con, long acn_ent_id, String acn_type, String acn_create_usr_id)
        throws SQLException, qdbException {
                
            Timestamp cur_time = dbUtils.getTime(con);                
            PreparedStatement stmt = con.prepareStatement(CREATE_ACCOUNT);
            stmt.setLong(1, acn_ent_id);
            stmt.setString(2, acn_type);
            stmt.setTimestamp(3, cur_time);
            stmt.setString(4, acn_create_usr_id);
            stmt.setTimestamp(5, cur_time);
            stmt.setString(6, acn_create_usr_id);
            if( stmt.executeUpdate() != 1 ) {                    
                throw new SQLException("Failed to add an Account.");
            }
            else {
                stmt.close();
                return;
            }        
        }
    
        
    //  Return account ID of the user, if account not exists, return -1    
    public static int getAccountId(Connection con, long acn_ent_id, String acn_type)
        throws SQLException {

            int ID;                
            PreparedStatement stmt = con.prepareStatement(GET_ACCOUNT_ID);
            stmt.setLong(1, acn_ent_id);
            stmt.setString(2, acn_type);
                
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                ID = rs.getInt("ID");
                rs.close();
                stmt.close();                    
                return ID;
            }
            else {
            	if(rs!=null)rs.close();
                stmt.close();
                return ACCOUNT_NOT_EXIST;
            }
                
        }
 
        
        
}

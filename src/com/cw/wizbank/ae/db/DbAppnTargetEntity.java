package com.cw.wizbank.ae.db;


import java.sql.*;

public class DbAppnTargetEntity {
    
    public long ate_app_id;
    public long ate_usr_ent_id;
    public String ate_rol_ext_id;
    public String ate_create_usr_id;
    public Timestamp ate_create_timestamp;
    
    public final String SQL_INS = " INSERT INTO aeAppnTargetEntity  "
                                + " ( ate_app_id, ate_usr_ent_id, ate_rol_ext_id, ate_create_usr_id, ate_create_timestamp ) "
                                + " VALUES ( ?, ?, ?, ?, ? ) ";
    
    private final String SQL_CHECK_UNIQUE = " SELECT count(*) AS num FROM aeAppnTargetEntity "
                                          + " WHERE ate_app_id = ? "
                                          + " AND ate_usr_ent_id = ? "
                                          + " AND ate_rol_ext_id = ? ";
    
    public final String SQL_DEL_BY_USR_ENT_ID = " DELETE FROM aeAppnTargetEntity "
                                              + " WHERE ate_usr_ent_id = ? ";
    
    public final String SQL_DEL_BY_APP_ID = " DELETE FROM aeAppnTargetEntity "
                                          + " WHERE ate_app_id = ? ";
    
    /*
    *Insert a record into table
    *Pre-define all fields in the table
    */
    public void ins(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_INS);
            int index = 1;
            stmt.setLong(index++, this.ate_app_id);
            stmt.setLong(index++, this.ate_usr_ent_id);
            stmt.setString(index++, this.ate_rol_ext_id);
            stmt.setString(index++, this.ate_create_usr_id);
            stmt.setTimestamp(index++, this.ate_create_timestamp);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to insert record into the aeAppnTargetEntity");
            stmt.close();
            return;
            
        }

    public boolean isExist(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL_CHECK_UNIQUE);
        int index = 1;
        long cnt;
        stmt.setLong(index++, this.ate_app_id);
        stmt.setLong(index++, this.ate_usr_ent_id);
        stmt.setString(index++, this.ate_rol_ext_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            cnt = rs.getLong(1);
        }
        else {
            cnt = 0;
        }
        stmt.close();
        if(cnt>0) {
            return true;
        } else {
            return false;
        }
    }
        
    
    /*
    *Del the record by approver user entity id
    *Pre-define ate_usr_ent_id
    */
    public void delByUsrEntId(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_BY_USR_ENT_ID);
            stmt.setLong(1, this.ate_usr_ent_id);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }

    /*
    *Del the record by application id
    *Pre-define ate_app_id
    */
    public void delByAppId(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_BY_APP_ID);
            stmt.setLong(1, this.ate_app_id);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
    
}
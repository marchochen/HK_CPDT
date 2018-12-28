package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.*;

public class DbKmNodeAssignment {

    public long nam_nod_id;
    public long nam_ent_id;
    public String nam_type;
    public String nam_create_usr_id;
    public Timestamp nam_create_timestamp;
    
    public final static String NODE_ASSIGNMENT_TYPE_ASSIGN      =   "ASSIGN";
    public final static String NODE_ASSIGNMENT_TYPE_SELFADD     =   "SELFADD";
    
    private final static String SQL_INS = " INSERT INTO kmNodeAssignment ( "
                                        + " nam_nod_id, nam_ent_id, nam_type, "
                                        + " nam_create_usr_id, nam_create_timestamp ) "
                                        + " VALUES ( ?, ?, ?, ?, ? ) ";
    
    private final static String SQL_DEL = " DELETE FROM kmNodeAssignment "
                                        + " WHERE nam_nod_id = ? AND nam_ent_id = ? AND nam_type = ? ";
    
    
    private final static String SQL_DEL_ALL_BY_NODE_ID = " DELETE FROM kmNodeAssignment "
                                                       + " WHERE nam_nod_id = ? ";
                                                       //+ " AND nam_type = ? ";
    
    private final static String SQL_DEL_NOT_EXIST = " DELETE FROM kmNodeAssignment "
                                                  + " WHERE nam_nod_id = ? "
                                                  + " AND nam_type = ? "
                                                  + " AND nam_ent_id NOT IN ";
    
    private final static String SQL_CHECK_EXISTENCE = " SELECT nam_nod_id FROM kmNodeAssignment "
                                                    + " WHERE nam_nod_id = ? AND nam_ent_id = ? "
                                                    + " AND nam_type = ? ";
    
    
    
    public void ins(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_INS);
            int index = 1;
            if( this.nam_create_timestamp == null )
                this.nam_create_timestamp = cwSQL.getTime(con);
            stmt.setLong(index++, this.nam_nod_id);
            stmt.setLong(index++, this.nam_ent_id);
            stmt.setString(index++, this.nam_type);
            stmt.setString(index++, this.nam_create_usr_id);
            stmt.setTimestamp(index++, this.nam_create_timestamp);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
    
    public void del(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_DEL);
            int index = 1;
            stmt.setLong(index++, this.nam_nod_id);
            stmt.setLong(index++, this.nam_ent_id);
            stmt.setString(index++, this.nam_type);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
 
    public void delNotExist(Connection con, long[] a_ent_id)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_NOT_EXIST + cwUtils.array2list(a_ent_id));
            int index = 1;
            stmt.setLong(index++, this.nam_nod_id);
            stmt.setString(index++, this.nam_type);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
    
    
    public static void delAllByID(Connection con, long nod_id)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_DEL_ALL_BY_NODE_ID);
            stmt.setLong(1, nod_id);
            stmt.executeUpdate();
            stmt.close();
            return;

        }
    
    public void delAll(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = null;
            if( this.nam_type != null && this.nam_type.length() > 0 ) {
                stmt = con.prepareStatement(SQL_DEL_ALL_BY_NODE_ID + " AND nam_type = ? ");
            } else {
                stmt = con.prepareStatement(SQL_DEL_ALL_BY_NODE_ID);
            }
            int index = 1;
            stmt.setLong(index++, this.nam_nod_id);
            if( this.nam_type != null && this.nam_type.length() > 0 ) {
                stmt.setString(index++, this.nam_type);
            }
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
    
    
    public boolean isExisted(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_CHECK_EXISTENCE);
            int index = 1;
            stmt.setLong(index++, this.nam_nod_id);
            stmt.setLong(index++, this.nam_ent_id);
            stmt.setString(index++, this.nam_type);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if(rs.next())
                flag = true;
            stmt.close();
            return flag;
        }
 
    
    public static boolean existedInWorkplace(Connection con, long nod_id, long usr_ent_id, String usrGroupList)
        throws SQLException {
            PreparedStatement stmt = con.prepareStatement(
                          " SELECT nam_nod_id FROM kmNodeAssignment "
                        + " WHERE nam_nod_id = ? AND ( nam_ent_id = ? OR nam_ent_id IN " 
                        + usrGroupList + " ) " 
                    );
            int index = 1;
            stmt.setLong(index++, nod_id);
            stmt.setLong(index++, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            boolean flag = false;
            if(rs.next())
                flag = true;
            stmt.close();
            return flag;
    
        }
    
}
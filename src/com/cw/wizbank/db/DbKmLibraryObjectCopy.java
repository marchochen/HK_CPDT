package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

/**
A database class to represent table kmLibraryObjectCopy
*/
public class DbKmLibraryObjectCopy {

    public long loc_lio_bob_nod_id;
	public long loc_id;
	public String loc_copy;
	public String loc_desc;
	public String loc_create_usr_id;
	public Timestamp loc_create_timestamp;
	public String loc_update_usr_id;
	public Timestamp loc_update_timestamp;
	public String loc_delete_usr_id;
	public Timestamp loc_delete_timestamp;

    private static final String SQL_GET = " SELECT loc_copy, loc_desc, loc_create_usr_id, "
                                        + " loc_create_timestamp, loc_update_usr_id, loc_update_timestamp, "
                                        + " loc_delete_usr_id, loc_delete_timestamp "
                                        + " FROM kmLibraryObjectCopy "
                                        + " WHERE loc_lio_bob_nod_id = ? AND loc_id = ? ";

    private static final String SQL_UPD = " UPDATE kmLibraryObjectCopy "
									   + " SET loc_copy = ?, loc_update_usr_id = ?, loc_update_timestamp = ?"
									   + " WHERE loc_id = ? ";
    
    private static final String SQL_GET_COPIES = " SELECT loc_id, loc_copy, loc_desc, loc_create_usr_id, "
                                               + " loc_create_timestamp, loc_update_usr_id, loc_update_timestamp, "
                                               + " loc_delete_usr_id, loc_delete_timestamp "
                                               + " FROM kmLibraryObjectCopy "
                                               + " WHERE loc_lio_bob_nod_id = ? and loc_delete_timestamp is null ";

    private static final String SQL_CHECK_COPY_EXISTED = " SELECT count(*) cnt "
                                                       + " FROM KmLibraryObjectCopy "
                                                       + " WHERE loc_lio_bob_nod_id = ? and loc_copy = ? and loc_delete_timestamp is null";

    private static final String SQL_CHECK_COPY_EXISTED_EXCLUDE_SELF = " SELECT count(*) cnt "
                                                                    + " FROM KmLibraryObjectCopy "
                                                                    + " WHERE loc_lio_bob_nod_id = ? and loc_id <> ? and loc_copy = ? and loc_delete_timestamp is null";


    private static final String SQL_DELETE_COPY = " UPDATE kmLibraryObjectCopy "
									            + " SET loc_delete_usr_id = ?, loc_delete_timestamp = ?"
									            + " WHERE loc_id = ? ";

    private static final String SQL_LAST_COPY = " SELECT count(*) cnt "
                                              + " FROM KmLibraryObjectCopy "
                                              + " WHERE loc_lio_bob_nod_id = ? and loc_delete_timestamp is null ";
    
    
    public void get(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET);
            stmt.setLong(index++, this.loc_lio_bob_nod_id);
            stmt.setLong(index++, this.loc_id);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                this.loc_copy = rs.getString("loc_copy");
                this.loc_desc = cwSQL.getClobValue(rs, "loc_desc");
                this.loc_create_usr_id = rs.getString("loc_create_usr_id");
	            this.loc_create_timestamp = rs.getTimestamp("loc_create_timestamp");
                this.loc_update_usr_id = rs.getString("loc_update_usr_id");
	            this.loc_update_timestamp = rs.getTimestamp("loc_update_timestamp");
                this.loc_delete_usr_id = rs.getString("loc_delete_usr_id");
	            this.loc_delete_timestamp = rs.getTimestamp("loc_delete_timestamp");
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }

    public void ins(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        StringBuffer sqlBuf = new StringBuffer(256);

        if (this.loc_create_timestamp == null) {
            this.loc_create_timestamp = cwSQL.getTime(con);
            this.loc_update_timestamp = loc_create_timestamp;
        }
            
        sqlBuf.append(" INSERT INTO kmLibraryObjectCopy ")
                .append(" (loc_lio_bob_nod_id, loc_copy, loc_desc, loc_create_usr_id, loc_create_timestamp, ")
				.append(" loc_update_usr_id, loc_update_timestamp )")
				.append(" VALUES (?, ?, ")
				.append(cwSQL.getClobNull())
				.append(", ?, ?, ?, ?) ");

		int index = 1;
        stmt = con.prepareStatement(sqlBuf.toString());
        stmt.setLong(index++, this.loc_lio_bob_nod_id);
        stmt.setString(index++, this.loc_copy);
        stmt.setString(index++, this.loc_create_usr_id);
        stmt.setTimestamp(index++, this.loc_create_timestamp);
        stmt.setString(index++, this.loc_update_usr_id);
        stmt.setTimestamp(index++, this.loc_update_timestamp);

        stmt.executeUpdate();
        stmt.close();
        
        String columnName[]  = new String[1];
        String columnValue[] = new String[1];
        StringBuffer condition = new StringBuffer(512);
        columnName[0] = "loc_desc";
        columnValue[0] = this.loc_desc;
        condition.append(" loc_lio_bob_nod_id = ").append(this.loc_lio_bob_nod_id)
        	.append(" and loc_copy = ").append(this.loc_copy)
        	.append(" and loc_create_timestamp = ").append(this.loc_create_timestamp);
        cwSQL.updateClobFields(con, "kmLibraryObjectCopy", columnName, columnValue, condition.toString());
    }
    
    public void upd(Connection con) throws SQLException {
        if (this.loc_update_timestamp == null) {
            this.loc_update_timestamp = cwSQL.getTime(con);   
        }

        PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(SQL_UPD);
        stmt.setString(index++, this.loc_copy);
        stmt.setString(index++, this.loc_update_usr_id);
        stmt.setTimestamp(index++, this.loc_update_timestamp);
        stmt.setLong(index++, this.loc_id);

        stmt.executeUpdate();
        stmt.close();

        String columnName[]  = new String[1];
        String columnValue[] = new String[1];
        StringBuffer condition = new StringBuffer(64);
        columnName[0] = "loc_desc";
        columnValue[0] = this.loc_desc;
        condition.append(" loc_id = ").append(this.loc_id);
        cwSQL.updateClobFields(con, "kmLibraryObjectCopy", columnName, columnValue, condition.toString());
    }
 
    public static Vector getCopies(Connection con, long obj_bob_nod_id, long loc_id) throws SQLException {
        Vector v = new Vector();

        if (loc_id > 0) {
            DbKmLibraryObjectCopy copy = new DbKmLibraryObjectCopy();
            copy.loc_lio_bob_nod_id = obj_bob_nod_id;
            copy.loc_id = loc_id;
            copy.get(con);
            v.addElement(copy);
        } else {
            PreparedStatement stmt = con.prepareStatement(SQL_GET_COPIES);
            stmt.setLong(1, obj_bob_nod_id);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                DbKmLibraryObjectCopy copy = new DbKmLibraryObjectCopy();
                copy.loc_id = rs.getLong("loc_id");
                copy.loc_copy = rs.getString("loc_copy");
                copy.loc_desc = cwSQL.getClobValue(rs, "loc_desc");
                copy.loc_create_usr_id = rs.getString("loc_create_usr_id");
	            copy.loc_create_timestamp = rs.getTimestamp("loc_create_timestamp");
                copy.loc_update_usr_id = rs.getString("loc_update_usr_id");
	            copy.loc_update_timestamp = rs.getTimestamp("loc_update_timestamp");
                copy.loc_delete_usr_id = rs.getString("loc_delete_usr_id");
	            copy.loc_delete_timestamp = rs.getTimestamp("loc_delete_timestamp");
	            v.addElement(copy);
            }                
            
            stmt.close();            
        }
        
        return v;
    }
    
    public boolean isCopyExisted(Connection con) throws SQLException {
        boolean result = true;
        
        PreparedStatement stmt = null;
        
        if (loc_id != 0) {
            stmt = con.prepareStatement(SQL_CHECK_COPY_EXISTED_EXCLUDE_SELF);            
        } else {
            stmt = con.prepareStatement(SQL_CHECK_COPY_EXISTED);
        }
        
        int index = 1;
        stmt.setLong(index++, loc_lio_bob_nod_id);

        if (loc_id != 0) {
            stmt.setLong(index++, loc_id);
        }
        
        stmt.setString(index++, loc_copy);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            if (rs.getInt("cnt") == 0) {
                result = false;
            }
        }
 
        stmt.close();
        
        return result;        
    }

    public int del(Connection con) throws SQLException {
        if (this.loc_delete_timestamp == null) {
            this.loc_delete_timestamp = cwSQL.getTime(con);   
        }

        PreparedStatement stmt = null;
		int index = 1;
        stmt = con.prepareStatement(SQL_DELETE_COPY);
        stmt.setString(index++, this.loc_delete_usr_id);
        stmt.setTimestamp(index++, this.loc_delete_timestamp);
        stmt.setLong(index++, this.loc_id);

        int code = stmt.executeUpdate();
        stmt.close();

        return code;
    }

    public boolean isLastCopy(Connection con) throws SQLException {
        boolean result = false;
        
        PreparedStatement stmt = con.prepareStatement(SQL_LAST_COPY);
        stmt.setLong(1, loc_lio_bob_nod_id);
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            if (rs.getInt("cnt") == 1) {
                result = true;
            }
        }
 
        stmt.close();
        
        return result;        
    }

}
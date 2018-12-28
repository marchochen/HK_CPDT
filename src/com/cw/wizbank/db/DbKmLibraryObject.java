package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

/**
A database class to represent table kmLibraryObject
*/
public class DbKmLibraryObject {

    public long lio_bob_nod_id;
    public long	lio_num_copy;
    public long lio_num_copy_available;
    public long lio_num_copy_in_stock;

    private static final String SQL_GET = " SELECT lio_num_copy, lio_num_copy_available, lio_num_copy_in_stock "
                                        + " FROM kmLibraryObject "
                                        + " WHERE lio_bob_nod_Id = ? ";
                                        
    private static final String SQL_INS = " INSERT INTO kmLibraryObject "
									   + " (lio_bob_nod_Id, lio_num_copy, lio_num_copy_available, lio_num_copy_in_stock) "
									   + " VALUES (?, ?, ?, ?) ";

    private static final String SQL_UPD = " UPDATE kmLibraryObject "
									   + " SET lio_num_copy = ?, "
									   + " lio_num_copy_available = ?, "
									   + " lio_num_copy_in_stock = ? "
									   + " WHERE lio_bob_nod_Id = ?";

    private static final String SQL_GET_STATUS = " SELECT lio_bob_nod_id, lio_num_copy, lio_num_copy_available "
                                               + " FROM kmLibraryObject "
                                               + " WHERE lio_bob_nod_id IN ";

    public void get(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_GET);
            stmt.setLong(index++, this.lio_bob_nod_id);
            ResultSet rs  = stmt.executeQuery();
            if(rs.next()) {
                this.lio_num_copy = rs.getLong("lio_num_copy");
	            this.lio_num_copy_available = rs.getLong("lio_num_copy_available");
	            this.lio_num_copy_in_stock = rs.getLong("lio_num_copy_in_stock");
            }
            rs.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
    
    public void ins(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_INS);
            stmt.setLong(index++, this.lio_bob_nod_id);
            stmt.setLong(index++, this.lio_num_copy);
            stmt.setLong(index++, this.lio_num_copy_available);
            stmt.setLong(index++, this.lio_num_copy_in_stock);

            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
    
    public void upd(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
			int index = 1;
            stmt = con.prepareStatement(SQL_UPD);
            stmt.setLong(index++, this.lio_num_copy);
            stmt.setLong(index++, this.lio_num_copy_available);
            stmt.setLong(index++, this.lio_num_copy_in_stock);
            stmt.setLong(index++, this.lio_bob_nod_id);

            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
    }
    
    
    public Hashtable getItemStatus(Connection con, Vector vec)
        throws SQLException {
            if( vec == null || vec.isEmpty() )
                return new Hashtable();
            PreparedStatement stmt = con.prepareStatement(SQL_GET_STATUS + cwUtils.vector2list(vec));
            ResultSet rs = stmt.executeQuery();
            Hashtable hashtable = new Hashtable();
            while(rs.next()){
                DbKmLibraryObject obj = new DbKmLibraryObject();
                obj.lio_bob_nod_id = rs.getLong("lio_bob_nod_id");
                obj.lio_num_copy = rs.getInt("lio_num_copy");
                obj.lio_num_copy_available = rs.getInt("lio_num_copy_available");
                hashtable.put(new Long(obj.lio_bob_nod_id), obj);
            }
            stmt.close();
            return hashtable;
        }
    
}
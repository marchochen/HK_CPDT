package com.cw.wizbank.db;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;


public class DbBookmark {

    //table column names 
    public int boo_id;
    public int boo_ent_id;
    public int boo_res_id;
    public String boo_title;
    public String boo_url;
    public Timestamp boo_create_timestamp;
    
    public DbBookmark() {
    }  
    
    public void ins(Connection con) throws qdbException{
        try{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_bookmark);
            stmt.setInt(1, boo_ent_id);
            stmt.setInt(2, boo_res_id);
            stmt.setString(3, boo_title);
            stmt.setString(4, boo_url);
            stmt.setTimestamp(5, boo_create_timestamp);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new qdbException("Failed to create Bookmark.");
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }                  
    }    

    /**
    *   get all bookmark for that usr_ent_id (from profile)
    */    
    public Vector getAll(Connection con) throws qdbException{
        try{
            Vector bookmarkVec = new Vector();
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_all_bookmark);
            stmt.setInt(1, boo_ent_id);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next())
            {
                DbBookmark boo = new DbBookmark();
                boo.boo_id = rs.getInt("boo_id");
                boo.boo_ent_id = boo_ent_id;
                boo.boo_res_id = rs.getInt("boo_res_id");
                boo.boo_title = rs.getString("boo_title");
                boo.boo_url = rs.getString("boo_url");
                boo.boo_create_timestamp = rs.getTimestamp("boo_create_timestamp");
                bookmarkVec.addElement(boo);
            }            
            stmt.close();
            return bookmarkVec;

        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }                  
    }    
    
    public void delMultiBookmark(Connection con, String[] booLst) throws qdbException{
        try{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_multi_bookmark + cwUtils.array2list(booLst));
            stmt.setInt(1, boo_ent_id);
            if (stmt.executeUpdate()< 1) {
                con.rollback();
                throw new qdbException("Failed to delete Bookmark.");
            }
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage()); 
        }                  
    } 

    public static String array2list(String[] id) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(0");
        if(id!=null) {
            for(int i=0;i<id.length;i++) 
                listBuf.append(",").append(id[i]);
        }
        listBuf.append(")");    
        list = new String(listBuf);
        return list;
    }
}
package com.cw.wizbank.ae;

import java.sql.*;

import com.cwn.wizbank.utils.CommonLog;

public class aeAccess {

    public long acc_ent_id;
    public long acc_cat_id;
    public String acc_create_usr_id;
    public Timestamp acc_create_timestamp;




    public boolean exists(Connection con) throws SQLException {
        int count;
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(2500);
        SQLBuf.append(" Select count(*) From aeAccess ");
        SQLBuf.append(" Where acc_ent_id = ? ");
        SQLBuf.append(" And acc_cat_id = ? ");
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, acc_ent_id);
        stmt.setLong(2, acc_cat_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            count = rs.getInt(1);
        else 
            throw new SQLException(SQL + ". acc_ent_id = " + acc_ent_id + " acc_cat_id = " + acc_cat_id + " returns null result set");
            
        if(count == 1)
            result = true;
        else
            result = false;
        
        stmt.close();
        return result;
    }
    

    public void ins(Connection con) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Insert into aeAccess ");
        SQLBuf.append(" (acc_ent_id, acc_cat_id, acc_create_timestamp, acc_create_usr_id) ");
        SQLBuf.append(" Values ");
        SQLBuf.append(" (?, ?, ?, ?) ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, acc_ent_id);
        stmt.setLong(2, acc_cat_id);
        stmt.setTimestamp(3, acc_create_timestamp);
        stmt.setString(4, acc_create_usr_id);
        
        stmt.executeUpdate();
        stmt.close();
    }
    
    
/********* static functions **********/     

    public static String prepareList(long[] ent_id, long cat_create_usr_ent_id) {
        StringBuffer listBuf = new StringBuffer(30);
        listBuf.append("(").append(cat_create_usr_ent_id);
        
        for(int i=0; i<ent_id.length; i++) 
            listBuf.append(",").append(ent_id[i]);
            
        listBuf.append(")");
        String list = new String(listBuf);
        return list;
    }
    
    
    public static void delNotInList(Connection con, String list, long cat_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Delete From aeAccess ");
        SQLBuf.append(" Where acc_cat_id = ? ");
        SQLBuf.append(" And acc_ent_id not in ").append(list);
        
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        stmt.executeUpdate();
        stmt.close();
    }


    public static void delCat(Connection con, long cat_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Delete From aeAccess ");
        SQLBuf.append(" Where acc_cat_id = ? ");
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        stmt.executeUpdate();
        stmt.close();
    }
    

    public static void delEnt(Connection con, long ent_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Delete From aeAccess ");
        SQLBuf.append(" Where acc_ent_id = ? ");
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ent_id);
        stmt.executeUpdate();
        stmt.close();
    }
    

    public static void insInArray(Connection con, long[] ent_id, long cat_id, 
                           String acc_create_usr_id, Timestamp acc_create_timestamp) throws SQLException {
        
        aeAccess acc;
        
        for(int i=0;i<ent_id.length;i++) {
            acc  = new aeAccess();
            acc.acc_ent_id = ent_id[i];
            acc.acc_cat_id = cat_id;
            acc.acc_create_timestamp = acc_create_timestamp;
            acc.acc_create_usr_id = acc_create_usr_id;
            if(!acc.exists(con))
                acc.ins(con);
        }
                            
    }
                           

    public static void updAccess(Connection con, long[] ent_id, long cat_create_usr_ent_id, long cat_id,
                                 String acc_create_usr_id, Timestamp acc_create_timestamp) throws SQLException {
        String IDlist = prepareList(ent_id, cat_create_usr_ent_id);
        CommonLog.debug("list=" + IDlist);
        delNotInList(con, IDlist, cat_id);
        CommonLog.debug("after del");
        insInArray(con, ent_id, cat_id, acc_create_usr_id, acc_create_timestamp);
        CommonLog.debug("after ins");
    }

}
package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;

public class dbEntityLoginHistory
{  

    public long         elh_ent_id;
    public Timestamp    elh_login_datetime;
    public long         elh_normal_cnt;
    public long         elh_trusted_cnt;

    public static final String NORMAL_LOGIN = "normal_login";
    public static final String TRUSTED_LOGIN = "trusted_login";
    
    public static final String LOGGED_BY_DATE = "date_log";
    public static final String LOGGED_BY_MONTH = "month_log";
    
    public dbEntityLoginHistory(){;}

    private static final String sql_ins = " INSERT INTO EntityLoginHistory "
                + " (elh_ent_id, elh_login_datetime, elh_normal_cnt, elh_trusted_cnt) "
                + " VALUES (?,?,?,?) ";

    private static final String sql_check_exist = " SELECT count(*) FROM EntityLoginHistory WHERE "
                + " elh_ent_id = ? and elh_login_datetime = ? ";
                
    private static final String sql_inc_normal_cnt = " UPDATE EntityLoginHistory SET "
                + " elh_normal_cnt = elh_normal_cnt + 1 WHERE "
                + " elh_ent_id = ? and elh_login_datetime = ? ";

    private static final String sql_inc_trusted_cnt = " UPDATE EntityLoginHistory SET "
                + " elh_trusted_cnt = elh_trusted_cnt + 1 WHERE "
                + " elh_ent_id = ? and elh_login_datetime = ? ";
                                
    public void increment(Connection con, String login_type, String log_type) throws SQLException, cwException
    {
        Timestamp curTime = cwSQL.getTime(con);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(curTime);
        int year_ = rightNow.get(Calendar.YEAR);
        int month_ = rightNow.get(Calendar.MONTH);
        int date_ = rightNow.get(Calendar.DATE);
        
        if (log_type.equals(LOGGED_BY_DATE)) {
            elh_login_datetime = new Timestamp(year_-1900, month_, date_, 0, 0, 0, 0);
        }else {
            elh_login_datetime = new Timestamp(year_-1900, month_, 1, 0, 0, 0, 0);
        }
        if (checkExists(con)) {
            upd(con, login_type);
        }else {
            ins(con, login_type);
        }
    }
    
    public void ins(Connection con, String login_type) throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement(sql_ins);
        stmt.setLong(1, elh_ent_id);
        stmt.setTimestamp(2, elh_login_datetime);
        if (login_type.equals(NORMAL_LOGIN)) {
            stmt.setLong(3, 1);
            stmt.setLong(4, 0);
        }else {
            stmt.setLong(3, 0);
            stmt.setLong(4, 1);
        }
        
        stmt.executeUpdate();
        stmt.close();
        
    }

    public void upd(Connection con, String login_type) throws SQLException
    {
        PreparedStatement stmt = null;        
        if (login_type.equals(NORMAL_LOGIN)) {
            stmt = con.prepareStatement(sql_inc_normal_cnt);
        }else {
            stmt = con.prepareStatement(sql_inc_trusted_cnt);
        }

        stmt.setLong(1, elh_ent_id);
        stmt.setTimestamp(2, elh_login_datetime);
        
        stmt.executeUpdate();
        stmt.close();
        
    }
    
    public boolean checkExists(Connection con) throws SQLException, cwException
    {
        PreparedStatement stmt = con.prepareStatement(sql_check_exist);
        stmt.setLong(1, elh_ent_id);
        stmt.setTimestamp(2, elh_login_datetime);
        ResultSet rs = stmt.executeQuery();
        
        boolean bExist = false;
        if (rs.next()) {
            if (rs.getInt(1) > 0) {
                bExist = true;
            }
        }else {
            stmt.close();
            throw new cwException ("Failed to get the login history.");
        }
        
        stmt.close();
        return bExist;

    }
    
    /**
     * @deprecated (2003-07-31 kawai)
     * please modify to not returning a ResultSet if this method is to be reused
     */
    /*
    public ResultSet getSiteCnt(Connection con, Timestamp startTime, Timestamp endTime)
        throws SQLException, cwException {
            
        String SQL = " SELECT elh_login_datetime, elh_normal_cnt, elh_trusted_cnt "
                   + " FROM EntityLoginHistory WHERE elh_ent_id = ? ";
                
                if( startTime != null )
                    SQL += " AND elh_login_datetime >= ? ";
                if( endTime != null )
                    SQL += " AND elh_login_datetime <= ? ";
                
                SQL += " ORDER BY elh_login_datetime ASC ";
            
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, elh_ent_id);
            if( startTime != null )
                stmt.setTimestamp(index++, startTime);
            if( endTime != null )
                stmt.setTimestamp(index++, endTime);
            ResultSet rs = stmt.executeQuery();            
            return rs;
        }
*/

    public Timestamp getStratTime(Connection con)
        throws SQLException, cwException {
            
            String SQL = " SELECT MIN(elh_login_datetime) "
                       + " FROM EntityLoginHistory "
                       + " WHERE elh_ent_id = ? "
                       + " GROUP BY elh_ent_id ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, elh_ent_id);
            ResultSet rs = stmt.executeQuery();
            Timestamp startTime = null;
            if(rs.next())
                startTime = rs.getTimestamp("elh_login_datetime");

            stmt.close();
            return startTime;
        }
        
}
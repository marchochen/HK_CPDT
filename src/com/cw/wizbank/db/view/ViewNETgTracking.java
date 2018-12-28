package com.cw.wizbank.db.view;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.db.sql.*;

public class ViewNETgTracking {


    public static String getCookieName(Connection con, long mod_id) throws SQLException {

        String SQL = SqlStatements.sql_get_netg_cookie_name;
        
        String returnValue = null;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, mod_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            returnValue = cwSQL.getClobValue(rs, "mod_core_vendor");
        }
        else {
            returnValue = null;
        }        
        
        stmt.close();            
        
        return returnValue;
    }
     
    public static String getLastCookie(Connection con, long cos_id, long mod_id, long ent_id, long tkh_id) throws SQLException {
        String SQL = SqlStatements.sql_get_netg_cookie;
        String returnValue = null;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_id);
        stmt.setLong(2, mod_id);
        stmt.setLong(3, ent_id);
        stmt.setLong(4, tkh_id);
        ResultSet rs = stmt.executeQuery();
            
        if (rs.next()) {
            returnValue =  rs.getString(1);
        }
        else {
            returnValue =  null;
        }

        stmt.close();
        
        return returnValue;
    }

    public static String getNETgStatus(Connection con, long cos_id, long mod_id, long ent_id, long tkh_id) throws SQLException {
        String SQL = SqlStatements.sql_get_netg_status;
        String returnValue = null;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_id);
        stmt.setLong(2, mod_id);
        stmt.setLong(3, ent_id);
        stmt.setLong(4, tkh_id);
        ResultSet rs = stmt.executeQuery();
            
        if (rs.next()) {
            returnValue =  rs.getString(1);
        }
        else {
            returnValue =  null;
        }

        stmt.close();
        
        return returnValue;
    }

    public static float getNETgScore(Connection con, long cos_id, long mod_id, long ent_id, long tkh_id) throws SQLException {
        String SQL = SqlStatements.sql_get_netg_score;
        float returnValue = -1;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_id);
        stmt.setLong(2, mod_id);
        stmt.setLong(3, ent_id);
        stmt.setLong(4, tkh_id);
        ResultSet rs = stmt.executeQuery();
            
        if (rs.next()) {
            returnValue =  rs.getFloat(1);
        }
        else {
            returnValue =  -1;
        }

        stmt.close();
        
        return returnValue;
    }
    

    public static boolean setCookie(Connection con, long cos_id, long mod_id, long ent_id, String cookie_value, long tkh_id) throws SQLException {
        String SQL = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        SQL = SqlStatements.sql_get_netg_cookie;

        Timestamp cur_time = cwSQL.getTime(con);

        stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cos_id);
        stmt.setLong(2, mod_id);
        stmt.setLong(3, ent_id);
        stmt.setLong(4, tkh_id);
        ResultSet rs = stmt.executeQuery();
            
        // record already exist, update the existing record
        if (rs.next()) {
            SQL = SqlStatements.sql_upd_netg_cookie;

            stmt2 = con.prepareStatement(SQL);
            stmt2.setString(1, cookie_value);
            stmt2.setTimestamp(2, cur_time);
            stmt2.setLong(3, mod_id);
            stmt2.setLong(4, ent_id);
            stmt2.setLong(5, cos_id);
            stmt2.setLong(6, tkh_id);
            if(stmt2.executeUpdate() != 1 ) {
                stmt2.close();
                return false;
            }
            else {
                stmt2.close();
                return true;
            }            
        }
        // add a new record
        else {
            
            SQL = "INSERT INTO ModuleEvaluation (mov_cos_id, mov_ent_id, mov_mod_id, mov_total_attempt, mov_last_acc_datetime, mov_total_time, mov_status, mov_score, mov_credit, mov_core_lesson, mov_tkh_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmt2 = con.prepareStatement(SQL);
            stmt2.setLong(1, cos_id);
            stmt2.setLong(2, ent_id);
            stmt2.setLong(3, mod_id);
            stmt2.setInt(4, 1);
            stmt2.setTimestamp(5, cur_time);
            stmt2.setFloat(6, 0);
            stmt2.setString(7, "I");
            stmt2.setFloat(8, 0);
            stmt2.setString(9, "C");
            stmt2.setString(10, cookie_value);
            stmt2.setLong(11, tkh_id);
            if(stmt2.executeUpdate() != 1 ) {
                stmt2.close();
                return false;
            }
            else {
                stmt2.close();
                return true;
            }            
        }
    }

    public static boolean updDuration(Connection con, long cos_id, long mod_id, long ent_id, int duration, long tkh_id) throws SQLException, qdbException {
        String SQL = null;
        PreparedStatement stmt = null;

        Timestamp cur_time = dbUtils.getTime(con);

        SQL = SqlStatements.sql_upd_netg_duration;
        stmt = con.prepareStatement(SQL);
        stmt.setFloat(1, duration);
        stmt.setLong(2, mod_id);
        stmt.setLong(3, ent_id);
        stmt.setLong(4, cos_id);
        stmt.setLong(5, tkh_id);
        
        if(stmt.executeUpdate() != 1 ) {
            stmt.close();
            return false;
        }
        else {
            stmt.close();
            return true;
        }            
    }

    public static boolean updTracking(Connection con, long cos_id, long mod_id, long ent_id, float score, String status, int duration, long tkh_id, Timestamp cur_time) throws SQLException {
        String SQL = null;
        PreparedStatement stmt = null;

        //Timestamp cur_time = dbUtils.getTime(con);

        if (score >= 0 && status != null) {
            SQL = SqlStatements.sql_upd_netg_tracking;

            stmt = con.prepareStatement(SQL);
            stmt.setFloat(1, score);
            stmt.setString(2, status);
            stmt.setFloat(3, duration);
            stmt.setTimestamp(4, cur_time);
            stmt.setLong(5, mod_id);
            stmt.setLong(6, ent_id);
            stmt.setLong(7, cos_id);
            stmt.setLong(8, tkh_id);
        }
        else if (score >= 0) {
            SQL = SqlStatements.sql_upd_netg_score;

            stmt = con.prepareStatement(SQL);
            stmt.setFloat(1, score);
            stmt.setFloat(2, duration);
            stmt.setTimestamp(3, cur_time);
            stmt.setLong(4, mod_id);
            stmt.setLong(5, ent_id);
            stmt.setLong(6, cos_id);
            stmt.setLong(7, tkh_id);
        }
        else if (status != null) {
            SQL = SqlStatements.sql_upd_netg_status;

            stmt = con.prepareStatement(SQL);
            stmt.setString(1, status);
            stmt.setFloat(2, duration);
            stmt.setTimestamp(3, cur_time);
            stmt.setLong(4, mod_id);
            stmt.setLong(5, ent_id);
            stmt.setLong(6, cos_id);
            stmt.setLong(7, tkh_id);
        }
        else {
            SQL = SqlStatements.sql_upd_netg_duration;

            stmt = con.prepareStatement(SQL);
            stmt.setFloat(1, duration);
            stmt.setTimestamp(2, cur_time);
            stmt.setLong(3, mod_id);
            stmt.setLong(4, ent_id);
            stmt.setLong(5, cos_id);
            stmt.setLong(6, tkh_id);
        }

        if(stmt.executeUpdate() != 1 ) {
            stmt.close();
            return false;
        }
        else {
            stmt.close();
            return true;
        }            
    }

    public static Vector getPostAssObjID(Connection con, long mod_id) throws SQLException {
        String developerID = null;
        String SQL = SqlStatements.sql_get_netg_obj_and_developer_id;
        Vector vtObjID = new Vector();
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, mod_id);
        ResultSet rs = stmt.executeQuery();
            
        while (rs.next()) {
            developerID = rs.getString(2);
            // post-assessment
            if (developerID.startsWith("0") == false) {
                vtObjID.addElement(new Integer(rs.getInt(1)));
            }
            // pre-assessment
            else {
                // do nothing
            }
        }

        stmt.close();
        
        return vtObjID;
    }
    
    public static int getAttemptedObjCount(Connection con, long ent_id, Vector vtObjID, long tkh_id) throws SQLException {
        String SQL = SqlStatements.sql_get_netg_attempted_obj_count;
        int att = 0;
        if (vtObjID.size() > 0) {
            SQL += "(";
            SQL += ((Integer)(vtObjID.elementAt(0))).toString();
            for (int i=1; i<vtObjID.size(); i++) {
                SQL += "," + ((Integer)(vtObjID.elementAt(i))).toString();
            }
            SQL += ")";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, ent_id);
            stmt.setLong(2, tkh_id);
            ResultSet rs = stmt.executeQuery();
           
            if (rs.next()) {
            	att =rs.getInt(1);
            } else {
            	att= -1;
            }
            stmt.close();
        }
        else {
        	att =0;
        }
        return att;
    }
    
    public static int getObjID(Connection con, long mod_id, String developer_id) throws SQLException {
        String SQL = SqlStatements.sql_get_netg_obj_id;
        int returnValue = -1;

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, mod_id);
        stmt.setString(2, developer_id);
        ResultSet rs = stmt.executeQuery();
            
        if (rs.next()) {
            returnValue =  rs.getInt(1);
        }
        else {
            returnValue =  -1;
        }

        stmt.close();

        return returnValue;
    }
    
    public static boolean insObjTracking(Connection con, int ent_id, int obj_id, int score, String status, long tkh_id) throws SQLException {
        String SQL = null;
        PreparedStatement stmt = null;
        /* ResultSet rs = null;
        int attempt_number = 0;

        SQL = SqlStatements.sql_count_netg_obj_attempt;
        
        stmt = con.prepareStatement(SQL);
        stmt.setInt(1, ent_id);
        stmt.setInt(2, obj_id);
        stmt.setLong(3, tkh_id);
        rs = stmt.executeQuery();
            
        if (rs.next()) {
            attempt_number =  rs.getInt(1);
        }
        else {
            attempt_number =  0;
        }
        
        stmt.close();
        
        attempt_number++;
            */    
        // insert a record into Accomplishment
        SQL = SqlStatements.sql_ins_netg_obj_tracking;

        stmt = con.prepareStatement(SQL);
        stmt.setInt(1, ent_id);
        stmt.setInt(2, obj_id);
        //stmt.setInt(3, attempt_number);
        if (score < 0) {
            stmt.setNull(3, java.sql.Types.FLOAT);
        }
        else {
            stmt.setFloat(3, (float)score);
        }
        stmt.setString(4, status);
        stmt.setLong(5, tkh_id);

        if(stmt.executeUpdate() != 1 ) {
            stmt.close();
            return false;
        }
        else {
            stmt.close();
            return true;
        }            
    }
}
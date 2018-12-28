package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.cwSQL;

public class DbUploadLog {
    
    public static final String TYPE_QUE = "QUE";
    public static final String SUBTYPE_MC = "MC";
    
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    public static final String PROCESS_TYPE_IMPORT = "IMPORT";
    public static final String PROCESS_TYPE_EXPORT = "EXPORT";
    
    public long     ulg_id;
    public String   ulg_type;
    public String   ulg_subtype;
    public String   ulg_usr_id_owner;
    public String   ulg_process;
    public String   ulg_method;
    //public String   ulg_path;
    public String   ulg_file_name;
    public String   ulg_desc;
    //public String   ulg_file_lastmod;
    //public long     ulg_file_size;
    //public int      ulg_rec_cnt;
    //public int      ulg_success_cnt;
    public String   ulg_status;
    public Timestamp    ulg_create_datetime;
    public Timestamp    ulg_upd_datetime;
    
    private static String sql_ins_ = "INSERT INTO UploadLog ("
        + " ulg_type, "
        + " ulg_subtype, "
        + " ulg_usr_id_owner, "
        + " ulg_process, "
        + " ulg_method, "
        //+ " ulg_path, "
        + " ulg_file_name, "
        //+ " ulg_file_lastmod, "
        //+ " ulg_file_size, "
        //+ " ulg_rec_cnt, "
        //+ " ulg_success_cnt, "
        + " ulg_status, "
        + " ulg_create_datetime, "
        + " ulg_upd_datetime ) "
        + " VALUES (?,?,?,?,?,?,?,?,? ) ";
        
    public int ins(Connection con) throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement(sql_ins_, PreparedStatement.RETURN_GENERATED_KEYS);
        int index = 0;
        stmt.setString(++index, ulg_type);
        stmt.setString(++index, ulg_subtype);
        stmt.setString(++index, ulg_usr_id_owner);
        stmt.setString(++index, ulg_process);
        stmt.setString(++index, ulg_method);
        //stmt.setString(++index, ulg_path);
        stmt.setString(++index, ulg_file_name);
        //stmt.setString(++index, ulg_file_lastmod);
        //stmt.setLong(++index, ulg_file_size);
        //stmt.setInt(++index, ulg_rec_cnt);
        //stmt.setInt(++index, ulg_success_cnt);
        stmt.setString(++index, ulg_status);
        stmt.setTimestamp(++index, ulg_create_datetime);
        stmt.setTimestamp(++index, ulg_upd_datetime);
        int cnt = stmt.executeUpdate();
        ulg_id = cwSQL.getAutoId(con, stmt, "UploadLog", "ulg_id");
        stmt.close();
        
        String tableName = "UploadLog";
        String columnName[] = {"ulg_desc"};
        String columnValue[] = {ulg_desc};
        StringBuffer condition = new StringBuffer();
        condition.append("ulg_id = ").append(ulg_id);
        cwSQL.updateClobFields(con, tableName, columnName, columnValue, condition.toString());
        
        return cnt;
    }



    private static String sql_get_ = "SELECT "
        + " ulg_type, "
        + " ulg_subtype, "
        + " ulg_usr_id_owner, "
        //+ " ulg_path, "
        + " ulg_file_name, "
        + " ulg_desc, "
        + " ulg_process, "
        + " ulg_method, "
        //+ " ulg_file_lastmod, "
        //+ " ulg_file_size, "
        //+ " ulg_rec_cnt, "
        //+ " ulg_success_cnt, "
        + " ulg_status, "
        + " ulg_create_datetime, "
        + " ulg_upd_datetime "
        + " FROM UploadLog WHERE ulg_id = ? ";

    public boolean get(Connection con) throws SQLException
    {
        boolean bExist = false;
        PreparedStatement stmt = con.prepareStatement(sql_get_);
        stmt.setLong(1, ulg_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            ulg_type = rs.getString("ulg_type");
            ulg_subtype = rs.getString("ulg_subtype");
            ulg_usr_id_owner = rs.getString("ulg_usr_id_owner");
            ulg_desc = cwSQL.getClobValue(rs, "ulg_desc");
            //ulg_path = rs.getString("ulg_path");
            ulg_file_name = rs.getString("ulg_file_name");
            ulg_process = rs.getString("ulg_process");
            ulg_method = rs.getString("ulg_method");
            //ulg_file_lastmod = rs.getString("ulg_file_lastmod");
            //ulg_file_size = rs.getLong("ulg_file_size");
            //ulg_rec_cnt = rs.getInt("ulg_rec_cnt");
            //ulg_success_cnt = rs.getInt("ulg_success_cnt");
            ulg_status = rs.getString("ulg_status");
            ulg_create_datetime = rs.getTimestamp("ulg_create_datetime");
            ulg_upd_datetime = rs.getTimestamp("ulg_upd_datetime");
            bExist = true;
        }
        stmt.close();
        
        return bExist;
        
    }

/*
    public String asXML()
    {
        StringBuffer xml = new StringBuffer();
        
        int fail_cnt = ulg_rec_cnt - ulg_success_cnt;
        
        xml.append("<uploadlog id=\"").append(ulg_id)
           .append("\" type=\"").append(ulg_type)
           .append("\" subtype=\"").append(ulg_subtype)
           .append("\" status=\"").append(ulg_status)
           .append("\" create_date=\"").append(ulg_create_datetime)
           .append("\" update_date=\"").append(ulg_upd_datetime)
           .append("\">").append(cwUtils.NEWL);
        xml.append("<file path=\"").append(ulg_path)
           .append("\" name=\"").append(ulg_file_name)
           .append("\" lastmod=\"").append(ulg_file_lastmod)
           .append("\" size=\"").append(ulg_file_size)
           .append("\"/>").append(cwUtils.NEWL);
        xml.append("<record total=\"").append(ulg_rec_cnt)
           .append("\" success=\"").append(ulg_success_cnt)
           .append("\" fail=\"").append(fail_cnt)
           .append("\"/>").append(cwUtils.NEWL);
        xml.append("</uploadlog>").append(cwUtils.NEWL);
        
        return xml.toString();
    }
*/


    private static String sql_upd_status_ = "UPDATE UploadLog SET "
        + " ulg_status = ?, ulg_upd_datetime = ? "
        + " WHERE ulg_id = ? ";
        
    public int updStatus(Connection con) throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement(sql_upd_status_);
        int index =0;
        stmt.setString(++index, ulg_status);
        stmt.setTimestamp(++index, ulg_upd_datetime);
        stmt.setLong(++index, ulg_id);
        int cnt = stmt.executeUpdate();
        stmt.close();
        return cnt;
    }
    
    
    /*
    private static String sql_upd_count_ = "UPDATE UploadLog SET "
        + " ulg_rec_cnt = ?, ulg_success_cnt = ? "
        + " WHERE ulg_id = ? ";
        
    public int updCount(Connection con) throws SQLException
    {
        PreparedStatement stmt = con.prepareStatement(sql_upd_count_);
        int index =0;
        stmt.setInt(++index, ulg_rec_cnt);
        stmt.setInt(++index, ulg_success_cnt);
        stmt.setLong(++index, ulg_id);
        int cnt = stmt.executeUpdate();
        return cnt;
    }
    */
 
}
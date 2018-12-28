package com.cw.wizbank.ae;

import java.util.*;
import java.io.Serializable;
import java.sql.*;

import com.cw.wizbank.cache.wizbCacheManager;
import com.cw.wizbank.util.cwUtils;

/*
*   to find the run's info with the course itm_id
*/
public class aeAttendanceStatus implements Serializable{
    public static final String STATUS_TYPE_ATTEND = "ATTEND";			// 已完成
    public static final String STATUS_TYPE_NOSHOW = "NOSHOW";
    public static final String STATUS_TYPE_INCOMPLETE = "INCOMPLETE";	// 未完成
    public static final String STATUS_TYPE_WITHDRAWN = "WITHDRAWN";		// 已放弃
    public static final String STATUS_TYPE_PROGRESS = "PROGRESS";		// 正在进行中

    int ats_id;
    String ats_title_xml;
    String ats_type;
    int ats_attend_ind;
    int ats_default_ind;
    long ats_ent_id_root;
    String ats_cov_status;

    public String getAtsId(){       
    	return new Integer(this.ats_id).toString();
    }
    aeAttendanceStatus(){
        ats_id = -1;
        ats_title_xml = "";
        ats_type = STATUS_TYPE_PROGRESS;
        ats_ent_id_root = -1;
    }


    public void get(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(
            "SELECT ats_id, ats_title_xml, ats_type, ats_ent_id_root , ats_cov_status, ats_attend_ind FROM aeAttendanceStatus WHERE ats_id = ? ");
            stmt.setLong(1, ats_id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                ats_id = rs.getInt("ats_id");
                ats_type = rs.getString("ats_type");
                ats_title_xml = getAttendanceStatusXml(ats_id, ats_type);
                ats_ent_id_root = rs.getLong("ats_ent_id_root");
                ats_cov_status = rs.getString("ats_cov_status");
                ats_attend_ind = rs.getInt("ats_attend_ind");
            }
            rs.close();
            stmt.close();
    }
    /** return a vector of status id (Integer)
    */
    public static Vector getAllStatusIdByRoot(Connection con, long ats_ent_id_root) throws SQLException{
        Vector vtId = new Vector();
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try {
            // get the attendance status id of the specified root
            stmt1 = con.prepareStatement("SELECT ats_id FROM aeAttendanceStatus WHERE ats_ent_id_root = ? ");
            stmt1.setLong(1, ats_ent_id_root);
            rs1 = stmt1.executeQuery();
            while(rs1.next()) {
                Integer id = new Integer(rs1.getInt("ats_id"));
                vtId.addElement(id);
            }
            
            // if there is no attendance status of the specified root, get the default
            if (vtId.size() == 0) {
                stmt2 = con.prepareStatement("SELECT ats_id FROM aeAttendanceStatus WHERE ats_default_ind = 1 ");
                rs2 = stmt2.executeQuery();
                while(rs2.next()) {
                    Integer id = new Integer(rs2.getInt("ats_id"));
                    vtId.addElement(id);
                }
            }
        } finally {
            if (stmt1 != null) {
                stmt1.close();
            }
            if (stmt2 != null) {
                stmt2.close();
            }
        }
        
        return vtId;
    }

    public static Vector getAllByRoot(Connection con, long ats_ent_id_root) throws SQLException{
        Vector result = new Vector();
        PreparedStatement stmt = con.prepareStatement(
            "SELECT ats_id, ats_title_xml, ats_type, ats_ent_id_root, ats_cov_status, ats_attend_ind FROM aeAttendanceStatus WHERE ats_ent_id_root = ? order by ats_id asc ");
            stmt.setLong(1, ats_ent_id_root);
            ResultSet rs = stmt.executeQuery();

        PreparedStatement stmt2 = con.prepareStatement(
            "SELECT ats_id, ats_title_xml, ats_type, ats_ent_id_root, ats_cov_status, ats_attend_ind FROM aeAttendanceStatus WHERE ats_default_ind = 1 order by ats_id asc ");

            while (rs.next())
            {
                aeAttendanceStatus ats = new aeAttendanceStatus();
                ats.ats_id = rs.getInt("ats_id");
                ats.ats_type = rs.getString("ats_type");
                ats.ats_title_xml = getAttendanceStatusXml(ats.ats_id, ats.ats_type);
                ats.ats_ent_id_root = rs.getLong("ats_ent_id_root");
                ats.ats_cov_status = rs.getString("ats_cov_status");
                ats.ats_attend_ind = rs.getInt("ats_attend_ind");
                result.addElement(ats);
            }
            stmt.close();

            if (result.size() == 0){
                rs = stmt2.executeQuery();
                while (rs.next()){
                    aeAttendanceStatus ats = new aeAttendanceStatus();
                    ats.ats_id = rs.getInt("ats_id");
                    ats.ats_type = rs.getString("ats_type");
                    ats.ats_title_xml = getAttendanceStatusXml(ats.ats_id, ats.ats_type);
                    ats.ats_ent_id_root = rs.getLong("ats_ent_id_root");
                    ats.ats_cov_status = rs.getString("ats_cov_status");
                    ats.ats_attend_ind = rs.getInt("ats_attend_ind");
                    result.addElement(ats);
                }
            }
            stmt2.close();

            return result;
    }

    // own a private set of status
    public static boolean hasOwnStatus(Connection con, long root_ent_id) throws SQLException{
        boolean hasStatus = false;
        PreparedStatement stmt = null;
        ResultSet rs =null;
        try {
            stmt = con.prepareStatement("SELECT ats_id FROM aeAttendanceStatus WHERE ats_ent_id_root = ? ");
            stmt.setLong(1, root_ent_id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                hasStatus = true;
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(rs!=null)rs.close();
        }
        return hasStatus;
    }

    // return 0 when no status
    public static int getIdByType(Connection con, long root_ent_id, String type) throws SQLException{
        int ats_id =0;
        
        boolean hasOwnStatus = hasOwnStatus(con, root_ent_id);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (hasOwnStatus){
                stmt = con.prepareStatement(
                    "SELECT ats_id FROM aeAttendanceStatus WHERE ats_type = ? AND ats_ent_id_root = ? ");
                
                stmt.setString(1, type);
                stmt.setLong(2, root_ent_id);
                rs = stmt.executeQuery();
                
                if (rs.next()){
                    ats_id = rs.getInt("ats_id");
                }
            }else{
                // get default status
                stmt = con.prepareStatement(
                    "SELECT ats_id FROM aeAttendanceStatus WHERE ats_type = ? AND ats_default_ind = 1 ");
                
                stmt.setString(1, type);
                rs = stmt.executeQuery();
                
                if (rs.next()){
                    ats_id = rs.getInt("ats_id");
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if(rs!=null) rs.close();
        }
        
        return ats_id;
    }

    public static int getIdByCovStatus(Connection con, long root_ent_id, String covStatus) throws SQLException{
        int ats_id =0;

        boolean hasOwnStatus = hasOwnStatus(con, root_ent_id);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (hasOwnStatus){
                stmt = con.prepareStatement(
                    "SELECT ats_id FROM aeAttendanceStatus WHERE ats_cov_status = ? AND ats_ent_id_root = ? ");
                
                stmt.setString(1, covStatus);
                stmt.setLong(2, root_ent_id);
                rs = stmt.executeQuery();
                
                if (rs.next()){
                    ats_id = rs.getInt("ats_id");
                }
            }else{
                // get default status
                stmt = con.prepareStatement(
                    "SELECT ats_id FROM aeAttendanceStatus WHERE ats_cov_status = ? AND ats_default_ind = 1 ");
                
                stmt.setString(1, covStatus);
                
                rs = stmt.executeQuery();
                if (rs.next()){
                    ats_id = rs.getInt("ats_id");
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return ats_id;
    }
    
    // retrun hashtable cov_status as key, ats_id as value
    public static Hashtable getAllCovStatus(Connection con, long root_ent_id) throws SQLException{
        int ats_id;
        String cov_status;
        
        Hashtable htStatus = new Hashtable();
        boolean hasOwnStatus = hasOwnStatus(con, root_ent_id);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (hasOwnStatus){
                stmt = con.prepareStatement(
                    "SELECT ats_id, ats_cov_status FROM aeAttendanceStatus WHERE ats_ent_id_root = ? ");
                
                stmt.setLong(1, root_ent_id);
                rs = stmt.executeQuery();
                
                while (rs.next()){
                    ats_id = rs.getInt("ats_id");
                    cov_status = rs.getString("ats_cov_status");
                    htStatus.put(cov_status, new Integer(ats_id));
                }
            }else{
                // get default status
                stmt = con.prepareStatement(
                    "SELECT ats_id , ats_cov_status FROM aeAttendanceStatus WHERE ats_default_ind = ? ");
                
                stmt.setBoolean(1, true);
                
                rs = stmt.executeQuery();
                while (rs.next()){
                    ats_id = rs.getInt("ats_id");
                    cov_status = rs.getString("ats_cov_status");
                    htStatus.put(cov_status, new Integer(ats_id));
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return htStatus;
    }
    
    public static boolean isValidStatus(Connection con, int ats_id, long ats_ent_id_root) throws SQLException{
        boolean result = false;
        boolean hasOwnStatus = hasOwnStatus(con, ats_ent_id_root);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            if (hasOwnStatus){
                stmt = con.prepareStatement(
                    "SELECT ats_id FROM aeAttendanceStatus WHERE ats_id = ? AND ats_ent_id_root = ? ");
                
                stmt.setLong(1, ats_id);
                stmt.setLong(2, ats_ent_id_root);
                rs = stmt.executeQuery();
                
                if (rs.next()){
                    result = true;
                }
            }else{
                stmt = con.prepareStatement(
                "SELECT ats_id FROM aeAttendanceStatus WHERE ats_id = ? AND ats_default_ind = 1 ");
                
                stmt.setLong(1, ats_id);
                rs = stmt.executeQuery();
                if (rs.next()){
                    result = true;
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return result;
    }

    public static boolean isValidStatus(Connection con, String type, long ats_ent_id_root) throws SQLException{
        long id = getIdByType(con, ats_ent_id_root, type);
        if (id == 0){
            return false;
        }else{
            return true;
        }
    }
    // similiar with statusAsXML, with list tag
    public static String getAllStatusAsXML(Connection con, long root_ent_id) throws SQLException
    {        
        String xml = "<attendance_status_list>";
        xml += statusAsXML(con, root_ent_id);
        xml += "</attendance_status_list>";
        return xml;        
    }
    
    /**
     * cached att status xml for all org
     */
    private static final String ATT_STATUS_XML_CACHE = "ATT_STATUS_XML_CACHE";
    public static String statusAsXML(Connection con, long root_ent_id)
        throws SQLException
    {
        HashMap cache = wizbCacheManager.getInstance().getCachedHashmap(ATT_STATUS_XML_CACHE, true);
        if (cache.get(new Long(root_ent_id)) == null) {
            StringBuffer result = new StringBuffer();
            Vector allStatus = getAllByRoot(con, root_ent_id);
    
            for (int i=0; i<allStatus.size(); i++){
                aeAttendanceStatus stat = (aeAttendanceStatus) allStatus.elementAt(i);
                result.append(stat.ats_title_xml);
            }
            cache.put(new Long(root_ent_id), result.toString());
            return result.toString();
        } else {
            return (String) cache.get(new Long(root_ent_id));
        }
    }

    public static final String ATTENDSTATUS_IDS_CACHE = "ATTENDSTATUS_IDS_CACHE";
    public static Vector getAttendIdsVec(Connection con, long root_ent_id) throws SQLException {
        HashMap cache = wizbCacheManager.getInstance().getCachedHashmap(ATTENDSTATUS_IDS_CACHE, true);
        Vector ats_vec = new Vector();
        if (cache!= null && cache.get(new Long(root_ent_id)) != null) {
            ats_vec = (Vector) cache.get(new Long(root_ent_id));
        } else {
            ats_vec = new Vector();
            PreparedStatement stmt = con.prepareStatement("select ats_id from aeAttendanceStatus where ats_attend_ind = 1 and (ats_ent_id_root = ? or ats_ent_id_root is null)");
            stmt.setLong(1, root_ent_id);
            ResultSet rs = stmt.executeQuery();        
            
            while(rs.next()) {
                ats_vec.addElement(new Long(rs.getLong("ats_id")));   
            }
    
            stmt.close();
            cache.put(new Long(root_ent_id), ats_vec);
        }
        return ats_vec;
    }
    
    public static int getAttendNum(Connection con) throws SQLException {
		int rowNum = 0;
		PreparedStatement stmt = con.prepareStatement("select ats_id from aeAttendanceStatus");
		ResultSet rs = stmt.executeQuery();        
        
		if(rs.next()) {
			rs.last();
			rowNum = rs.getRow();  
		}

		stmt.close();
		return rowNum;
    }
    
    public static String getAttendanceStatusXml(int ats_id, String ats_type) {
    	String ats_xml = "<status id=\"" + ats_id + "\" type=\"" + cwUtils.escNull(ats_type) +"\"/>";
    	return ats_xml;
    }
}

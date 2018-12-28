package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.db.DbSuperviseTargetEntity;

public class ViewSuperviseTargetEntity {

    public static class ViewSupervisedGroup{
        public String usg_display_bil;
        public long usg_ent_id;
        public String usg_role;
        public String ent_ste_uid;
        public Timestamp spt_eff_start_datetime;
        public Timestamp spt_eff_end_datetime;
    }
    
    public static class ViewDirectSupervisor{
        public String usr_display_bil;
        public long usr_ent_id;
        public String usr_ste_usr_id;
        public String usr_status;
        public Timestamp spt_eff_start_datetime;
        public Timestamp spt_eff_end_datetime;
    }    
    
    private static final String SQL_GET_DIRECT_STAFF = 
        " from RegUser, SuperviseTargetEntity " +
        " where spt_source_usr_ent_id = ? " +
        " and spt_target_ent_id = usr_ent_id " +
        " and spt_type = ? " +
        " and usr_status = ? " + 
        " and spt_eff_start_datetime <= ? " +
        " and spt_eff_end_datetime >= ? ";
    
    private static final String SQL_GET_GROUP_STAFF = 
        " from RegUser, UserGroup, EntityRelation " +
        " where ern_child_ent_id = usr_ent_id " +
        " and usr_status = ? ";

    private static final String SQL_GET_DIRECT_SUPERVISOR = 
        " SELECT usr_display_bil , usr_ent_id , usr_ste_usr_id, usr_status, spt_eff_start_datetime, spt_eff_end_datetime FROM RegUser, SuperviseTargetEntity " +
            "WHERE spt_source_usr_ent_id = usr_ent_id " +
            "AND spt_target_ent_id = ? " + 
            "AND spt_type = ? ";

    private static final String SQL_GET_SUPERVISED_GROUP = 
        " SELECT usg_display_bil , usg_ent_id , ent_ste_uid, usg_role, spt_eff_start_datetime, spt_eff_end_datetime FROM UserGroup, Entity , SuperviseTargetEntity " +
            "WHERE spt_target_ent_id = usg_ent_id " +
            "AND usg_ent_id = ent_id " + 
            "AND spt_source_usr_ent_id = ? " + 
            "AND spt_type = ? ";

    private static final String SQL_GET_SUPERVISED_GROUP_ENT_ID = 
        " SELECT spt_target_ent_id FROM SuperviseTargetEntity "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") +
            "WHERE " +
            "spt_source_usr_ent_id = ? " + 
            "AND spt_type = ? " + 
            "AND spt_eff_start_datetime <= ? " +
            "AND spt_eff_end_datetime >= ? ";

    /**
    Get the group supervisors of the input entity
    @param con Connection to database
    @parma target_ent_id entity id of the target entity
    @return Vector of ViewSuperviseTargetEntity.ViewDirectSupervisor
    */
    public Vector getGroupSupervisors(Connection con, long target_ent_id) throws SQLException {
        PreparedStatement stmt = null;
        Vector v = new Vector();
        Timestamp curTime = cwSQL.getTime(con);
        try {
            stmt =con.prepareStatement(SqlStatements.SQL_GET_GROUP_SUPERVISORS);
            stmt.setLong(1, target_ent_id);
            stmt.setTimestamp(2, curTime);
            stmt.setTimestamp(3, curTime);
            stmt.setString(4, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ViewDirectSupervisor supervisor = new ViewDirectSupervisor();
                supervisor.usr_ent_id = rs.getLong("usr_ent_id");
                supervisor.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                supervisor.usr_display_bil = rs.getString("usr_display_bil");
                supervisor.usr_status = rs.getString("usr_status");
                supervisor.spt_eff_start_datetime = rs.getTimestamp("spt_eff_start_datetime");
                supervisor.spt_eff_end_datetime = rs.getTimestamp("spt_eff_end_datetime");
                v.addElement(supervisor);
            }
        } finally {
            if(stmt!=null) {stmt.close();}
        }
        return v;
    }
    
    
    /**
    Get the entity id of the group supervisors
    @param con Connection to database
    @param ancestors ancestor list of groups
    @return Hashtable of Vectors. Keys are the Long target_ent_id (e.g. group ent_id) and the Vector contains the entity id of the supervisors
    */
    public Hashtable getGroupSupervisors(Connection con, String ancestors) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs =null;
        StringBuffer SQLBuf = new StringBuffer(256);
        Hashtable h = new Hashtable();
        SQLBuf.append(" Select spt_source_usr_ent_id, spt_target_ent_id ")
              .append(" From SuperviseTargetEntity ")
              .append(" Where spt_type = ? ")
              .append(" And spt_target_ent_id in ").append(ancestors).append(" ")
              .append(" And spt_eff_start_datetime < ? ")
              .append(" And spt_eff_end_datetime > ? ");
        Timestamp curTime = cwSQL.getTime(con);
        try {
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setString(1, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE);
            stmt.setTimestamp(2, curTime);
            stmt.setTimestamp(3, curTime);
            rs = stmt.executeQuery();
            while(rs.next()) {
                Long source_usr_ent_id = new Long(rs.getLong("spt_source_usr_ent_id"));
                Long target_ent_id = new Long(rs.getLong("spt_target_ent_id"));
                Vector v = (Vector) h.get(target_ent_id);
                if(v == null) {
                    v = new Vector();
                }
                v.addElement(source_usr_ent_id);
                h.put(target_ent_id, v);
            }
        } finally {
        	if(rs!=null)rs.close();
            if(stmt!=null) stmt.close();
        }
        return h;
    }

    /**
    Get SQL for selecting ent_id of direct staff.
    The SQL has 5 parameters: 1) supervisor's ent_id (spt_source_usr_ent_id)
                              2) 'DIRECT_SUPERVISE' (spt_type)
                              3) 'OK' (usr_status)
                              4) current time
                              5) current time
    @return SQL for selecting ent_id of direct staff
    */
    public String getDirectStaffEntIdSQL() {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" select usr_ent_id ")
              .append(SQL_GET_DIRECT_STAFF);

        return SQLBuf.toString();
    }
    
    /**
    Get SQL for ent_id and usr_display_bil of direct staff.
    The SQL has 5 parameters: 1) supervisor's ent_id (spt_source_usr_ent_id)
                              2) 'DIRECT_SUPERVISE' (spt_type)
                              3) 'OK' (usr_status)
                              4) current time
                              5) current time
    @return SQL for selecting ent_id of direct staff
    */
    public String getDirectStaffInfoSQL() {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" select usr_ent_id , usr_display_bil ")
              .append(SQL_GET_DIRECT_STAFF);

        return SQLBuf.toString();
    }
    
    /**
    Get SQL for selecting ent_id of all staff.
    The SQL has 8 parameters:1) supervisor's ent_id (spt_source_usr_ent_id)
                              2) 'DIRECT_SUPERVISE' (spt_type)
                              3) 'OK' (usr_status)
                              4) current time
                              5) current time
                              6) 'OK' (usr_status)
                              7) current time
                              8) current time
    @return SQL for selecting ent_id of direct staff
    */
    private String getStaffEntIdSQL(Connection con, long spt_source_usr_ent_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" select usr_ent_id ")
              .append(SQL_GET_DIRECT_STAFF);
        SQLBuf.append(" union ");
        SQLBuf.append(" select usr_ent_id ")
              .append(SQL_GET_GROUP_STAFF)
              .append(getAncesterPatternSQL(con, spt_source_usr_ent_id));

        return SQLBuf.toString();
    }

    /**
    Get SQL for getting a supervisor's direct staff count.
    The SQL has 5 parameters: 1) supervisor's ent_id (spt_source_usr_ent_id)
                              2) 'DIRECT_SUPERVISE' (spt_type)
                              3) 'OK' (usr_status)
                              4) current time
                              5) current time
    @return SQL for selecting ent_id of direct staff
    */
    public String getDirectStaffCountSQL() {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" select count(*) ")
              .append(SQL_GET_DIRECT_STAFF);

        return SQLBuf.toString();
    }

    /**
    Get SQL for getting all staff count.
    The SQL has 8 parameters:1) supervisor's ent_id (spt_source_usr_ent_id)
                              2) 'DIRECT_SUPERVISE' (spt_type)
                              3) 'OK' (usr_status)
                              4) current time
                              5) current time
                              6) 'OK' (usr_status)
                              7) current time
                              8) current time
    @return SQL for selecting ent_id of direct staff
    */
    public String getStaffCountSQL(Connection con, long spt_source_usr_ent_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" select count(*) from ( ");
        SQLBuf.append(getStaffEntIdSQL(con, spt_source_usr_ent_id));
        SQLBuf.append(" ) myStaff");
        
        return SQLBuf.toString();
    }
    
    /**
    Get the count of supervisor's staff
    @param con Connection to database
    @param spt_source_usr_ent_id supervisor's ent_id
    @return count of the supervisor's staff 
    */
    public long getStaffCount(Connection con, long spt_source_usr_ent_id) throws SQLException {
        String SQL = getStaffCountSQL(con, spt_source_usr_ent_id);
        long count = 0;
        PreparedStatement stmt = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            stmt.setString(6, dbRegUser.USR_STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                count = rs.getLong(1);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return count;
    }
    
    /**
    Get the count of supervisor's direct staff
    @param con Connection to database
    @param spt_source_usr_ent_id supervisor's ent_id
    @return count of the supervisor's direct staff 
    */
    public long getDirectStaffCount(Connection con, long spt_source_usr_ent_id) throws SQLException {
        String SQL = getDirectStaffCountSQL();
        long count = 0;
        PreparedStatement stmt = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                count = rs.getLong(1);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return count;
    }
    /**
    Test if the specified supervisor has any staff
    @param con Connection to database
    @param spt_source_usr_ent_id supervisor's ent_id
    @return true if and only if the specified supervisor has at least one staff; false otherwise
    */
    public boolean hasStaff(Connection con, long spt_source_usr_ent_id) throws SQLException {
        String SQL = "select count(*) from ( "
            + "select usr_ent_id  from RegUser "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") + ", SuperviseTargetEntity "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") 
            + "where spt_source_usr_ent_id = ? and spt_target_ent_id = usr_ent_id and spt_type = ? and usr_status = ? and spt_eff_start_datetime <= ? and spt_eff_end_datetime >= ? "
            + "union "
            + "select usg_ent_id  from SuperviseTargetEntity "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") + ", UserGroup "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"")
            + "where spt_source_usr_ent_id = ? and spt_type = ? and spt_target_ent_id = usg_ent_id and spt_eff_start_datetime <= ? and spt_eff_end_datetime >= ? "
            + ") myStaff ";
    	long count = 0;
    	PreparedStatement stmt = null;
    	try {
    		Timestamp curTime = cwSQL.getTime(con);
    		stmt = con.prepareStatement(SQL);
    		stmt.setLong(1, spt_source_usr_ent_id);
    		stmt.setString(2, "DIRECT_SUPERVISE");
    		stmt.setString(3, dbRegUser.USR_STATUS_OK);
    		stmt.setTimestamp(4, curTime);
    		stmt.setTimestamp(5, curTime);
    		stmt.setLong(6, spt_source_usr_ent_id);
    		stmt.setString(7, "SUPERVISE");
    		stmt.setTimestamp(8, curTime);
    		stmt.setTimestamp(9, curTime);
    		
    		ResultSet rs = stmt.executeQuery();
    		if(rs.next()) {
    			count = rs.getLong(1);
    		}
    	} finally {
    		if(stmt!=null) stmt.close();
    	}
  		return count > 0 ? true : false;
    }

    /**
    Get the entity ids of the specified supervisor's staff
    @param con Connection to database
    @param spt_source_usr_ent_id supervisor's ent_id
    @return Vector of Long objects containing the entity ids of the supervisor's staff
    */
    public Vector getStaffEntIdVector(Connection con, long spt_source_usr_ent_id) throws SQLException {
        String SQL = getStaffEntIdSQL(con, spt_source_usr_ent_id);
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            stmt.setString(6, dbRegUser.USR_STATUS_OK);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong("usr_ent_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

    /**
    Get the entity ids of the specified supervisor's direct staff
    @param con Connection to database
    @param spt_source_usr_ent_id supervisor's ent_id
    @return Vector of Long objects containing the entity ids of the supervisor's direct staff
    */
    public Vector getDirectStaffEntIdVector(Connection con, long spt_source_usr_ent_id) throws SQLException {
        String SQL = getDirectStaffEntIdSQL();
        long count = 0;
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                v.addElement(new Long(rs.getLong("usr_ent_id")));
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

    /**
    Get ent_id and display_bil of the specified supervisor's direct staff
    @param con Connection to database
    @param spt_source_usr_ent_id supervisor's ent_id
    @return Vector of reguser objects containing the ent_id and usr_display_bil of the supervisor's direct staff
    */

    public Vector getDirectStaffInfoVector(Connection con, long spt_source_usr_ent_id) throws SQLException {
        String SQL = getDirectStaffInfoSQL();
        long count = 0;
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = rs.getLong("usr_ent_id");
                usr.ent_id = usr.usr_ent_id;
                usr.usr_display_bil = rs.getString("usr_display_bil");
                v.addElement(usr);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }

    /**
    Test if the specified source_usr_ent_id and spt_target_ent_id are supervisor-n-staff
    @param con Connection to database
    @param spt_source_usr_ent_id supervisor's entity id
    @param spt_target_usr_ent_id staff's entity id
    @return true if spt_target_usr_ent_id is a staff of spt_source_usr_ent_id; false otherwise
    */
    public boolean isMyStaff(Connection con, long spt_source_usr_ent_id, long spt_target_usr_ent_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append(" select usr_ent_id from (")
              .append(getStaffEntIdSQL(con, spt_source_usr_ent_id))
              .append(") myStaff ")
              .append(" where usr_ent_id = ? ");
        boolean result = false;
        PreparedStatement stmt = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setString(2, "DIRECT_SUPERVISE");
            stmt.setString(3, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(4, curTime);
            stmt.setTimestamp(5, curTime);
            stmt.setString(6, dbRegUser.USR_STATUS_OK);
            stmt.setLong(7, spt_target_usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                result = true;
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return result;
    }
    
    /**
    Get supervise groups of the specified supervisor
    @param con Connection to database
    @parma spt_source_usr_ent_id supervisor's entity id
    @return Vector of dbUserGroup objects representing the supervisor's supervise group
    */
    public Vector getSuperviseGroup(Connection con, long spt_source_usr_ent_id, String cur_lan) throws SQLException {
        Vector v = new Vector();
        PreparedStatement stmt = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SqlStatements.SQL_SPT_GET_APPR_GROUP);
            stmt.setLong(1, spt_source_usr_ent_id);
            stmt.setTimestamp(2, curTime);
            stmt.setTimestamp(3, curTime);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                dbUserGroup usg = new dbUserGroup();
                usg.ent_id = rs.getLong("ent_id");
                usg.ent_type = rs.getString("ent_type");
                usg.ent_upd_date = rs.getTimestamp("ent_upd_date");
                usg.ent_syn_date = rs.getTimestamp("ent_syn_date");
                usg.ent_ste_uid = rs.getString("ent_ste_uid");
                usg.ent_syn_ind = rs.getBoolean("ent_syn_ind");
                usg.usg_ent_id = rs.getLong("usg_ent_id");
                usg.usg_code = rs.getString("usg_code");
                usg.usg_name = rs.getString("usg_name");
                usg.usg_role = rs.getString("usg_role");
                if (dbUserGroup.USG_ROLE_ROOT.equalsIgnoreCase(usg.usg_role)) {
                	usg.usg_display_bil = LangLabel.getValue(cur_lan, "668");
        		} else {
        			usg.usg_display_bil = rs.getString("usg_display_bil");
        		}
                usg.usg_level = rs.getString("usg_level");
                usg.usg_usr_id_admin = rs.getString("usg_usr_id_admin");
                usg.usg_ent_id_root = rs.getLong("usg_ent_id_root");
                usg.usg_desc = rs.getString("usg_desc");
                v.addElement(usg);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        
        return v;
    }

    public static ViewDirectSupervisor[] getDirectSupervisor(Connection con, long usr_ent_id) throws SQLException{
        Vector vTmp = new Vector();
        ViewDirectSupervisor spvr = null;
        PreparedStatement stmt = con.prepareStatement(SQL_GET_DIRECT_SUPERVISOR);
        stmt.setLong(1, usr_ent_id);
        stmt.setString(2, DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            spvr = new ViewDirectSupervisor();
            spvr.usr_display_bil = rs.getString("usr_display_bil");
            spvr.usr_ent_id = rs.getLong("usr_ent_id");
            spvr.usr_status = rs.getString("usr_status");
            spvr.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            spvr.spt_eff_start_datetime = rs.getTimestamp("spt_eff_start_datetime");
            spvr.spt_eff_end_datetime = rs.getTimestamp("spt_eff_end_datetime");
            vTmp.addElement(spvr);            
        }
        rs.close();
        stmt.close();
        
        ViewDirectSupervisor result[] = new ViewDirectSupervisor[vTmp.size()];
        result = (ViewDirectSupervisor[])vTmp.toArray(result);
        return result;              
    }                        
    
    public static Hashtable getDirectSupervisorByUsrEntIds(Connection con, Vector vtUsrEntId) throws SQLException{
        Hashtable htUsrDSupervisor = new Hashtable();
        if (vtUsrEntId.size() == 0){
            return htUsrDSupervisor;
        }
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_ent_id", cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, vtUsrEntId, cwSQL.COL_TYPE_LONG);
        
        PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getUsrDirectSupervisorByUsrEntIds(tableName));
        stmt.setString(1, DbSuperviseTargetEntity.SPT_TYPE_DIRECT_SUPERVISE);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            String usr_ste_usr_id = rs.getString("target_ste_usr_id");
            Vector vtDSupervisor = (Vector)htUsrDSupervisor.get(usr_ste_usr_id);
            if (vtDSupervisor==null)
                vtDSupervisor = new Vector();
    
            String supervisor_ste_usr_id = rs.getString("supervisor_ste_usr_id");
            if (supervisor_ste_usr_id==null)
                supervisor_ste_usr_id = "";
            vtDSupervisor.addElement(supervisor_ste_usr_id);
            htUsrDSupervisor.put(usr_ste_usr_id, vtDSupervisor);
        }
        stmt.close();
        cwSQL.dropTempTable(con,tableName);
        return htUsrDSupervisor;              
    }                        

    /**
     * Get the user's actively (by eff_start/end_datetime) supervised user groups 
     * @param con Connection to database
     * @param usr_ent_id entity id of the superviser
     * @return long array of ent_id of supervised user groups
     * @throws SQLException
     */
    public static long[] getActiveSupervisedGroupEntId(Connection con, long usr_ent_id) throws SQLException{
        Vector vTmp = new Vector();
        Timestamp curTime = cwSQL.getTime(con);
        
        PreparedStatement stmt = con.prepareStatement(SQL_GET_SUPERVISED_GROUP_ENT_ID);
        stmt.setLong(1, usr_ent_id);
        stmt.setString(2, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE);
        stmt.setTimestamp(3, curTime);
        stmt.setTimestamp(4, curTime);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            vTmp.addElement(new Long(rs.getLong(1)));
        }
        stmt.close();
        return cwUtils.vec2longArray(vTmp);              
    }                        


    public static ViewSupervisedGroup[] getSupervisedGroup(Connection con, long usr_ent_id) throws SQLException{
        Vector vTmp = new Vector();

        PreparedStatement stmt = con.prepareStatement(SQL_GET_SUPERVISED_GROUP);
        stmt.setLong(1, usr_ent_id);
        stmt.setString(2, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()){
            ViewSupervisedGroup grp = new ViewSupervisedGroup();
            grp.usg_display_bil = rs.getString("usg_display_bil");
            grp.usg_ent_id = rs.getLong("usg_ent_id");
            grp.ent_ste_uid = rs.getString("ent_ste_uid");
            grp.usg_role = rs.getString("usg_role");
            grp.spt_eff_start_datetime = rs.getTimestamp("spt_eff_start_datetime");
            grp.spt_eff_end_datetime = rs.getTimestamp("spt_eff_end_datetime");
            vTmp.addElement(grp);
        }
        stmt.close();
        
        ViewSupervisedGroup result[] = new ViewSupervisedGroup[vTmp.size()];
        result = (ViewSupervisedGroup[])vTmp.toArray(result);
        return result;              
    }                        

    public static Hashtable getSupervisedGroupByUsrEntIds(Connection con, Vector vtUsrEntId) throws SQLException{
        Hashtable htUsrSpvGrp = new Hashtable();
        if (vtUsrEntId.size() == 0){
            return htUsrSpvGrp;
        }
        String tableName = cwSQL.createSimpleTemptable(con, "tmp_usr_ent_id", cwSQL.COL_TYPE_LONG, 0);
        cwSQL.insertSimpleTempTable(con, tableName, vtUsrEntId, cwSQL.COL_TYPE_LONG);
        
        PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.getUsrSupervisedGroupByUsrEntIds(tableName));
        stmt.setString(1, DbSuperviseTargetEntity.SPT_TYPE_SUPERVISE);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            String usr_ste_usr_id = rs.getString("usr_ste_usr_id");
            Vector vtSpvGrp = (Vector)htUsrSpvGrp.get(usr_ste_usr_id);
            if (vtSpvGrp==null)
                vtSpvGrp = new Vector();
                
            String groupCode = rs.getString("ent_ste_uid");
            if (groupCode==null)
                groupCode = "";
            vtSpvGrp.addElement(groupCode);
            htUsrSpvGrp.put(usr_ste_usr_id, vtSpvGrp);
        }
        stmt.close();
        cwSQL.dropTempTable(con,tableName);
        return htUsrSpvGrp;              
    }                        
  
    public static StringBuffer getDirectSupervisorAsXML(Connection con, long usr_ent_id) throws SQLException{
        StringBuffer xml = new StringBuffer();
        
        ViewDirectSupervisor[] spvrs = getDirectSupervisor(con, usr_ent_id); 
        xml.append("<direct_supervisor>").append(cwUtils.NEWL);
        Timestamp tmp_eff_start;
        Timestamp tmp_eff_end;
        for (int i=0; i<spvrs.length; i++){
            
            tmp_eff_start = spvrs[i].spt_eff_start_datetime;
            tmp_eff_end = spvrs[i].spt_eff_end_datetime;
            
            xml.append("<entity id=\"").append(spvrs[i].usr_ent_id).append("\" type=\"USR\" eff_start_date=\"")
                .append(dbUtils.isMinTimestamp(tmp_eff_start) ? dbUtils.IMMEDIATE : tmp_eff_start.toString())
                .append("\" eff_end_date=\"")
                .append(dbUtils.isMaxTimestamp(tmp_eff_end) ? dbUtils.UNLIMITED : tmp_eff_end.toString())
                .append("\" display_bil=\"").append(cwUtils.esc4XML(spvrs[i].usr_display_bil))
                .append("\" ste_usr_id=\"").append(spvrs[i].usr_ste_usr_id)
                .append("\" status=\"").append(spvrs[i].usr_status).append("\" />");
        }
        xml.append("</direct_supervisor>").append(cwUtils.NEWL);
        return xml;
    }
    
    public static StringBuffer getSupervisedGroupAsXML(Connection con, long usr_ent_id) throws SQLException{
        StringBuffer xml = new StringBuffer();
        
        ViewSupervisedGroup[] grps = getSupervisedGroup(con, usr_ent_id); 
        xml.append("<supervise_target_list>").append(cwUtils.NEWL);
        Timestamp tmp_eff_start;
        Timestamp tmp_eff_end;
        for (int i=0; i<grps.length; i++){
            
            tmp_eff_start = grps[i].spt_eff_start_datetime;
            tmp_eff_end = grps[i].spt_eff_end_datetime;
            
            xml.append("<entity id=\"").append(grps[i].usg_ent_id).append("\" type=\"USG\" eff_start_date=\"")
                .append(dbUtils.isMinTimestamp(tmp_eff_start) ? dbUtils.IMMEDIATE : tmp_eff_start.toString())
                .append("\" eff_end_date=\"")
                .append(dbUtils.isMaxTimestamp(tmp_eff_end) ? dbUtils.UNLIMITED : tmp_eff_end.toString())
                .append("\" display_bil=\"").append(cwUtils.esc4XML(grps[i].usg_display_bil))
                .append(grps[i].usg_role != null ? "\" usg_role=\"" + cwUtils.esc4XML(grps[i].usg_role) : "")
                .append("\" status=\"OK\" />");
        }        
        xml.append("</supervise_target_list>").append(cwUtils.NEWL);
        return xml;
    }

    /**
     * Get a SQL segment of the supervisor's supervised groups as
     * @param con Connection to database
     * @param spt_source_usr_ent_id  user entity id of the supervisor
     * @return SQL segment
     * @throws SQLException
     */
    private static String getAncesterPatternSQL(Connection con, long spt_source_usr_ent_id) throws SQLException {
        
        long[] grp_ent_ids = getActiveSupervisedGroupEntId(con, spt_source_usr_ent_id);
        StringBuffer SQLBuf = new StringBuffer();
        if(grp_ent_ids == null || grp_ent_ids.length == 0) {
            //to ensure no result will be returned as the user does not have any supervised group
            SQLBuf.append(" and 1=2 ");
        } else {
            SQLBuf.append(" and (");
            for(int i=0 ;i<grp_ent_ids.length; i++) {
                if(i > 0) {
                    SQLBuf.append(" or ");
                }
                SQLBuf.append(" ern_ancestor_ent_id = ").append(grp_ent_ids[i]);
            }
            SQLBuf.append(" )");
        }
        return SQLBuf.toString();
    }
    
}
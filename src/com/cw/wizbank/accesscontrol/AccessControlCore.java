package com.cw.wizbank.accesscontrol;

import com.cw.wizbank.util.cwSQL;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.dbRegUser;

public class AccessControlCore {
  
    static final String ROL_EXT_ID = "ROL_EXT_ID";
    static final String ROL_XML = "ROL_XML";
    static final String ROL_AUTH_LEVEL = "ROL_AUTH_LEVEL";
    static final String ROL_STATUS_OK = "OK";
    static final String ROL_STATUS_HIDDEN = "HIDDEN";
    static final String ROL_TARGET_ENT_TYPE_USR = "USR";
    static final String ROL_TARGET_ENT_TYPE_USG = "USG";
    static final String ROL_TARGET_ENT_TYPE_USR_OR_USG = "USR_OR_USG";
    
    public static class ViewSynEntityRole {
        public long erl_ent_id;
        public long erl_rol_id;
        public Timestamp erl_creation_timestamp;
        public Timestamp erl_syn_timestamp;
        public Timestamp erl_eff_start_datetime;
        public Timestamp erl_eff_end_datetime;
        public String usr_ste_usr_id;
    }
/* API start */


    //check if the entity has a role
    boolean hasEntityRole(Connection con, long ent_id, long rol_id) throws SQLException {
        /*
        if(!isEntityExist(con, ent_id)) 
            throw new SQLException("Entity " + ent_id + " does not exist");
        if(!isRoleExist(con, rol_id)) 
            throw new SQLException("Role " + rol_id + " does not exist");
        */
        return isEntityRoleRelationExist(con, ent_id, rol_id);
    }

    boolean hasEntityRole(Connection con, long ent_id, long rol_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        /*
        if(!isEntityExist(con, ent_id)) 
            throw new SQLException("Entity " + ent_id + " does not exist");
        if(!isRoleExist(con, rol_id)) 
            throw new SQLException("Role " + rol_id + " does not exist");
        */
        return isEntityRoleRelationExist(con, ent_id, rol_id, startDate, endDate);
    }
    



    
    //returns the roles that an entity belongs to
    //login_ind is to indicate if need to check (rol_url_home is not NULL) 
    String[] getRoles(Connection con, long ent_id, boolean login_ind) throws SQLException {
        /*
        if(!isEntityExist(con, ent_id)) 
            throw new SQLException("Entity " + ent_id + " does not exist");
        */
        String[] roles;
        String role;
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(512);        
        SQLBuf.append(" Select distinct rol_ext_id, rol_seq_id From acEntityRole, acRole ")
              .append(" Where erl_ent_id = ? ")
              .append(" AND erl_eff_start_datetime <= ? ")
              .append(" AND erl_eff_end_datetime >= ? ")
              .append(" And rol_id = erl_rol_id ");
        if(login_ind) {
            SQLBuf.append(" And rol_url_home is not null ");
            SQLBuf.append(" And rol_status = ? ");
        }
        SQLBuf.append(" Order By rol_seq_id ");
                   
        Timestamp curTime = cwSQL.getTime(con);                    
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1,ent_id);
        stmt.setTimestamp(2, curTime);
        stmt.setTimestamp(3, curTime);
        if(login_ind) {
            stmt.setString(4, ROL_STATUS_OK);
        }
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            role = rs.getString("rol_ext_id");
            v.addElement(role);
        }
        stmt.close();
        roles = Vector2String(v);
        return roles;
    }

   
    
    //returns the entities belong to a role
    long[] containEntities(Connection con, long rol_id) throws SQLException {
        /*
        if(!isRoleExist(con, rol_id)) 
            throw new SQLException("Role " + rol_id + " does not exist");
        */
        long[] entities;
        Long entity;
        Vector v = new Vector();
        String SQL = " Select erl_ent_id From acEntityRole "
                   + " Where erl_rol_id = ? "
                   + " AND erl_eff_start_datetime <= ? "
                   + " AND erl_eff_end_datetime >= ? ";
                   
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rol_id);
        stmt.setTimestamp(2, curTime);
        stmt.setTimestamp(3, curTime);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            entity = new Long(rs.getLong("erl_ent_id"));
            v.addElement(entity);
        }
        stmt.close();
        entities = Vector2long(v);
        return entities;
    }   
    
    private static final String sql_get_not_syn_ent_role = 
        " SELECT usr_ent_id, usr_ste_usr_id, erl_eff_start_datetime, erl_eff_end_datetime FROM acEntityRole, Reguser, Entity " +
        " WHERE erl_rol_id = ? AND usr_status = ? " +
        " AND ent_syn_date > ? " + 
        " AND usr_syn_rol_ind = ? " +
        " AND usr_ent_id = ent_id " +
        " AND usr_ent_id = erl_ent_id " +
        " AND (erl_syn_timestamp < ? OR erl_syn_timestamp IS NULL )" +
        " AND ent_delete_usr_id IS NULL " +         // Fai: for soft-delete
        " AND ent_delete_timestamp IS NULL ";
    public ViewSynEntityRole[] getNotSynEntityRole(Connection con, long rol_id, Timestamp synTimestamp) throws SQLException {
        int idx = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            idx = 1;
            stmt = con.prepareStatement(sql_get_not_syn_ent_role);
            stmt.setLong(idx++, rol_id);
            stmt.setString(idx++, dbRegUser.USR_STATUS_OK);
            stmt.setTimestamp(idx++, synTimestamp);
            stmt.setBoolean(idx++, true);
            stmt.setTimestamp(idx++, synTimestamp);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                idx = 1;
                ViewSynEntityRole entRole = new ViewSynEntityRole();
                entRole.erl_ent_id = rs.getLong(idx++);
                entRole.usr_ste_usr_id = rs.getString(idx++);
                entRole.erl_eff_start_datetime = rs.getTimestamp(idx++);
                entRole.erl_eff_end_datetime = rs.getTimestamp(idx++);
                tempResult.addElement(entRole);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewSynEntityRole result[] = new ViewSynEntityRole[tempResult.size()];
        result = (ViewSynEntityRole[])tempResult.toArray(result);
        
        return result;
    }

    // return a hashtable , ent_id as key, usr login id as value
    Hashtable getNoRoleUsr(Connection con) throws SQLException{
        Hashtable htUsr = new Hashtable();
        String SQL = "SELECT usr_ent_id, usr_ste_usr_id FROM Reguser " 
                    + " WHERE usr_status = ? AND usr_ent_id NOT IN "
                    + " (SELECT  erl_ent_id FROM acEntityRole) ";
                    
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setString(1, dbRegUser.USR_STATUS_OK);
            rs = stmt.executeQuery();
            while (rs.next()) {
                htUsr.put(new Long(rs.getLong("usr_ent_id")), rs.getString("usr_ste_usr_id"));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return htUsr;
    }
    

    // for backward compatible. should not be used again
    public String getEntityByRoleSQL(long rol_id) throws SQLException {
        String SQL = " Select erl_ent_id From acEntityRole "
                   + " Where erl_rol_id = " + rol_id;
        return SQL;
    }
    //returns a SQL that will get the ent_id of entities who belongs to this role
    public String getEntityByRoleSQL(long rol_id, String ent_id_colName) throws SQLException {
        String SQL = " Select erl_ent_id From acEntityRole "
                   + " Where erl_rol_id = " + rol_id
                   + " AND erl_eff_start_datetime <= ? "
                   + " AND erl_eff_end_datetime >= ? ";
        return SQL;
    }    
    //returns a SQL that will get the ent_id of entities who belongs to this role
    public String getEntityByRoleSQL(long rol_id, Timestamp curTime) throws SQLException {
        String SQL = " Select erl_ent_id From acEntityRole "
                   + " Where erl_rol_id = " + rol_id
                   + " AND erl_eff_start_datetime <= ? "
                   + " AND erl_eff_end_datetime >= ? ";
        return SQL;
    }    
    // for backward compatible. retired method. should not be used again 
    public String getEntityByFunctionSQL(long ftn_id) throws SQLException {
        String SQL = " Select erl_ent_id From acEntityRole, acRoleFunction "
                   + " Where erl_rol_id = rfn_rol_id "
                   + " And rfn_ftn_id = " + ftn_id;
        return SQL;
    }    
    //returns a SQL that will get the ent_id of entities who belongs to this role
    public String getEntityByFunctionSQL(long ftn_id, Timestamp curTime) throws SQLException {
        String SQL = " Select erl_ent_id From acEntityRole, acRoleFunction "
                   + " Where erl_rol_id = rfn_rol_id "
                   + " And rfn_ftn_id = " + ftn_id
                   + " AND erl_eff_start_datetime <= '" + curTime + "'"
                   + " AND erl_eff_end_datetime >= '" + curTime + "'";
        return SQL;
    }    
    
    void assignEntity2Role(Connection con, long ent_id, long rol_id) throws SQLException {
        assignEntity2Role(con, ent_id, rol_id, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
    }

    //assign an entity a role
    void assignEntity2Role(Connection con, long ent_id, long rol_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        /*
        if(!isEntityExist(con, ent_id))
            throw new SQLException("Entity " + ent_id + " does not exist");
        if(!isRoleExist(con, rol_id))
            throw new SQLException("Role " + rol_id + " does not exist");
        */
        if(isEntityRoleRelationExist(con, ent_id, rol_id, startDate, endDate))
            throw new SQLException("Entity Role Relation, ent_id = " + ent_id + " rol_id = " + rol_id + " startDate = "+ startDate + " endDate = "+ endDate + " already exists");
        
        insEntityRole(con, ent_id, rol_id, startDate, endDate);
    }
    
    
    //remove an entity from a role
    void rmEntityFromRole(Connection con, long ent_id, long rol_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        /*
        if(!isEntityExist(con, ent_id))
            throw new SQLException("Entity " + ent_id + " does not exist");
        if(!isRoleExist(con, rol_id))
            throw new SQLException("Role " + rol_id + " does not exist");
        */
//        if(!isEntityRoleRelationExist(con, ent_id, rol_id, startDate, endDate))
//            throw new SQLException("Entity Role Relation, ent_id = " + ent_id + " rol_id = " + rol_id + " startDate = "+ startDate + " endDate = " + endDate + " does not exist");

        delEntityRole(con, ent_id, rol_id, startDate, endDate);
    }
    
    
    //remove an entity from all role
    void rmEntityRoles(Connection con, long ent_id) throws SQLException {
        /*
        if(!isEntityExist(con, ent_id))
            throw new SQLException("Entity " + ent_id + " does not exist");
        */
        delEntityRoleOfEntity(con, ent_id);
    }
    

    //grant privilege on a function to a role
    void grantRolePrivilege(Connection con, long rol_id, long ftn_id) throws SQLException {
        /*
        if(!isRoleExist(con, rol_id))
            throw new SQLException("Role " + rol_id + " does not exist");
        if(!isFunctionExist(con, ftn_id))
            throw new SQLException("Function " + ftn_id + " does not exist");
        */
        if(isRoleFunctionRelationExist(con, rol_id, ftn_id))
            throw new SQLException("Role Function Relation, rol_id = " + rol_id + " ftn_id = " + ftn_id + " already exists");
        
        insRoleFunction(con, rol_id, ftn_id);        
    }
    

    //remove privilege on a function to a role
    void rmRolePrivilege(Connection con, long rol_id, long ftn_id) throws SQLException {
        /*
        if(!isRoleExist(con, rol_id))
            throw new SQLException("Role " + rol_id + " does not exist");
        if(!isFunctionExist(con, ftn_id))
            throw new SQLException("Function " + ftn_id + " does not exist");
        */
        if(!isRoleFunctionRelationExist(con, rol_id, ftn_id))
            throw new SQLException("Role Function Relation, rol_id = " + rol_id + " ftn_id = " + ftn_id + " does not exist");
        
        delRoleFunction(con, rol_id, ftn_id);        
    }



    //add an entity
    public void addEntity(Connection con, String ent_type) throws SQLException {
        insEntity(con, ent_type);
    }
    
    
    void addRole(Connection con, String rol_ext_id) throws SQLException {
        /*
        if(isRoleExist(con, rol_ext_id))
            throw new SQLException("Role " + rol_ext_id + " already exists");
        */
        insRole(con, rol_ext_id);
    }
    

    
    public long[] getAllEntities(Connection con) throws SQLException {
        long[] entities;
        Long entity;
        Vector v = new Vector();
        String SQL = " Select ent_id From Entity " +
                     " WHERE ent_delete_usr_id IS NULL " +      // Fai: for soft-delete
                     " AND ent_delete_timestamp IS NULL ";

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);

        while (rs.next()) {
            entity = new Long(rs.getLong("ent_id"));
            v.addElement(entity);
        }
        stmt.close();
        entities = Vector2long(v);
        return entities;
    }

    
    public long[] getAllRoles(Connection con) throws SQLException {
        long[] roles;
        Long role;
        Vector v = new Vector();
        String SQL = " Select rol_id From acRole Order By rol_seq_id";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);

        while (rs.next()) {
            role = new Long(rs.getLong("rol_id"));
            v.addElement(role);
        }
        stmt.close();
        roles = Vector2long(v);
        return roles;
    }
    public String getRoleExtId(Connection con, long ste_id, String rol_ste_uid) throws SQLException {
    	String sql = "Select rol_ext_id From acRole where rol_ste_ent_id = ? and rol_ste_uid = ? ";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	int index = 1;
    	stmt.setLong(index++,ste_id);
    	stmt.setString(index++, rol_ste_uid);
    	ResultSet rs = stmt.executeQuery();
    	String rol_ext_id = null;
    	if(rs.next()) {
    		rol_ext_id = rs.getString("rol_ext_id");
    	}
    	stmt.close();
    	return rol_ext_id;
    }

/* API end */    

  

    // insert an entity into Entity
    void insEntity(Connection con, String ent_type) throws SQLException {
        Timestamp curTime =cwSQL.getTime(con);
        String SQL = " Insert into Entity "
                   + " (ent_type, ent_creation_timestamp) "
                   + " Values "
                   + " (?, ?) ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ent_type);
        stmt.setTimestamp(2, curTime);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected != 1) {
            String err = "Execute " 
                       + SQL + " ent_type = " + ent_type + " ent_creation_timestamp = " + curTime 
                       + " returns " + rowAffected + " affected ";
            throw new SQLException(err);
        }
    }
    
    
    // insert an entity into Entity, AccessControlManager will use this one
    long insEntity(Connection con, String ent_type, Timestamp curTime) throws SQLException {
        String SQL = " Insert into Entity "
                   + " (ent_type, ent_creation_timestamp) "
                   + " Values "
                   + " (?, ?) ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, ent_type);
        stmt.setTimestamp(2, curTime);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected != 1) {
            String err = "Execute " 
                       + SQL + " ent_type = " + ent_type + " ent_creation_timestamp = " + curTime 
                       + " returns " + rowAffected + " affected ";
            throw new SQLException(err);
        }
        
        return maxEntityID(con);
    }
    
    
    void insRole(Connection con, String rol_ext_id) throws SQLException {
        Timestamp curTime =cwSQL.getTime(con);
        String SQL = " Insert into acRole "
                   + " (rol_ext_id, rol_creation_timestamp) "
                   + " Values "
                   + " (?, ?) ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, rol_ext_id);
        stmt.setTimestamp(2, curTime);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected != 1) {
            String err = "Execute " 
                       + SQL + " rol_ext_id = " + rol_ext_id + " rol_creation_timestamp = " + curTime 
                       + " returns " + rowAffected + " affected ";
            throw new SQLException(err);
        }
    }
    

    //delete a role from acRole
    void delRole(Connection con, long rol_id) throws SQLException {
        String SQL = " Delete FROM acRole Where rol_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rol_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected!=1) {
            String err = "Execute " + SQL + " rol_id = " + rol_id + " returns " + rowAffected + " affected"; 
            throw new SQLException(err);
        }
    }



    //delete all records in acEntityRole belongs to an entity
    void delEntityRoleOfEntity(Connection con, long ent_id) throws SQLException {
        String SQL = " Delete FROM acEntityRole Where erl_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ent_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
    }
    

    //delete all records in acEntityRole belongs to a role
    void delEntityRoleOfRole(Connection con, long rol_id) throws SQLException {
        String SQL = " Delete FROM acEntityRole Where erl_rol_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rol_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
    }


    //delete all records in acRoleFunction belongs to a role
    void delRoleFunctionOfRole(Connection con, long rol_id) throws SQLException {
        String SQL = " Delete FROM acRoleFunction Where rfn_rol_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rol_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
    }


    //delete all records in acRoleFunction belongs to a function
    void delRoleFunctionOfFunction(Connection con, long ftn_id) throws SQLException {
        String SQL = " Delete FROM acRoleFunction Where rfn_ftn_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ftn_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
    }


    //check if the Entity exists and return T/F
    boolean isEntityExist(Connection con, long ent_id) throws SQLException {
        boolean result=false;
        long count=0;
        String err;
        String SQL = " Select count(*) from Entity "
                   + " Where ent_id = ? "
                   + " AND ent_delete_usr_id IS NULL "          // Fai: for soft-delete
                   + " AND ent_delete_timestamp IS NULL ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1,ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            count = rs.getLong(1);
        }
        else {
            err = "Execute " 
                + SQL + " ent_id = " + ent_id 
                + " return a null result set ";
                
            throw new SQLException(err);
        }
        if(count > 0)
            result = true;
        else
            result = false;
        
        stmt.close();
        return result;    
    }


    // not null for startdate, enddate
    boolean isEntityRoleRelationExist(Connection con, long ent_id, long rol_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        boolean result=false;
        long count=0;
        String err;
        String SQL = " Select count(*) from acEntityRole "
                   + " Where erl_ent_id = ? "
                   + " And erl_rol_id = ? "
                   + " AND erl_eff_start_datetime = ? "
                   + " AND erl_eff_end_datetime = ? ";
                   
        Timestamp curTime = cwSQL.getTime(con);                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1,ent_id);
        stmt.setLong(2,rol_id);
        stmt.setTimestamp(3,startDate);
        stmt.setTimestamp(4,endDate);

        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            count = rs.getLong(1);
        }
        else {
            err = "Execute " 
                + SQL + " ent_id = " + ent_id + " rol_id = " + rol_id 
                + " return a null result set ";
                
            throw new SQLException(err);
        }
        if(count > 0)
            result = true;
        else
            result = false;
        
        stmt.close();
        return result;    
    }

    
    //check if the EntityRole relation exists and active and return T/F
    boolean isEntityRoleRelationExist(Connection con, long ent_id, long rol_id) throws SQLException {
        boolean result=false;
        long count=0;
        String err;
        String SQL = " SELECT count(*) FROM acEntityRole "
                   + " WHERE erl_ent_id = ? "
                   + " AND erl_rol_id = ? "
                   + " AND erl_eff_start_datetime <= ? "
                   + " AND erl_eff_end_datetime >= ? ";
                   
        Timestamp curTime = cwSQL.getTime(con);                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1,ent_id);
        stmt.setLong(2,rol_id);
        stmt.setTimestamp(3,curTime);
        stmt.setTimestamp(4,curTime);

        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            count = rs.getLong(1);
        }
        else {
            err = "Execute " 
                + SQL + " ent_id = " + ent_id + " rol_id = " + rol_id 
                + " return a null result set ";
                
            throw new SQLException(err);
        }
        if(count > 0)
            result = true;
        else
            result = false;
        
        stmt.close();
        return result;    
    }

    
    //check if the RoleFunction relation exists and return T/F
    boolean isRoleFunctionRelationExist(Connection con, long rol_id, long ftn_id) throws SQLException {
        boolean result=false;
        long count=0;
        String err;
        String SQL = " Select count(*) from acRoleFunction "
                   + " Where rfn_rol_id = ? "
                   + " And rfn_ftn_id = ? ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1,rol_id);
        stmt.setLong(2,ftn_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            count = rs.getLong(1);
        }
        else {
            err = "Execute " 
                + SQL + " rol_id = " + rol_id + " ftn_id = " + ftn_id 
                + " return a null result set ";
            if(rs!=null)rs.close();
            if (stmt != null)stmt.close();   
            throw new SQLException(err);
        }
        if(count > 0)
            result = true;
        else
            result = false;
        rs.close();
        stmt.close();
        return result;    
    }

    void insEntityRole(Connection con, long ent_id, long rol_id) throws SQLException {
        insEntityRole(con, ent_id, rol_id, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
    }
    
    //insert a record into EntityRole
    void insEntityRole(Connection con, long ent_id, long rol_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        Timestamp curTime =cwSQL.getTime(con);
        String SQL = " Insert into acEntityRole "
                   + " (erl_ent_id, erl_rol_id, erl_creation_timestamp, erl_eff_start_datetime, erl_eff_end_datetime) "
                   + " Values "
                   + " (?, ?, ?, ?, ?) ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ent_id);
        stmt.setLong(2, rol_id);
        stmt.setTimestamp(3,curTime);
        stmt.setTimestamp(4,startDate);
        stmt.setTimestamp(5,endDate);

        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected!=1) {
            String err = "Execute " 
                       + SQL + " ent_id = " + ent_id + " rol_id = " + rol_id + " startDate = "+ startDate + " endDate = " + endDate + " creation_timestamp = " + curTime
                       + " returns " + rowAffected + " affected ";
            throw new SQLException(err);
        }        
    }

    //update syn timestamp for EntityRole
    void updEntityRoleSynTimestamp(Connection con, long ent_id, long rol_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        Timestamp curTime =cwSQL.getTime(con);
        String SQL = " UPDATE acEntityRole SET erl_syn_timestamp = ? WHERE erl_ent_id = ? AND erl_rol_id = ? AND erl_eff_start_datetime = ? AND erl_eff_end_datetime = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setTimestamp(1,curTime);
        stmt.setLong(2, ent_id);
        stmt.setLong(3, rol_id);
        stmt.setTimestamp(4,startDate);
        stmt.setTimestamp(5,endDate);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
    }
    
    //insert a record into acRoleFunction
    void insRoleFunction(Connection con, long rol_id, long ftn_id) throws SQLException {
        Timestamp curTime =cwSQL.getTime(con);
        String SQL = " Insert into acRoleFunction "
                   + " (rfn_rol_id, rfn_ftn_id, rfn_create_timestamp) "
                   + " Values "
                   + " (?, ?, ?) ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rol_id);
        stmt.setLong(2, ftn_id);
        stmt.setTimestamp(3,curTime);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected!=1) {
            String err = "Execute " 
                       + SQL + " rol_id = " + rol_id + " ftn_id = " + ftn_id + " create_timestamp = " + curTime
                       + " returns " + rowAffected + " affected ";
            throw new SQLException(err);
        }        
    }
    // for backward compatible
    void delEntityRole(Connection con, long ent_id, long rol_id) throws SQLException {
        delEntityRole(con, ent_id, rol_id, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
    }
    
    //delete a record from acEntityRole
    void delEntityRole(Connection con, long ent_id, long rol_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        String SQL = " Delete FROM acEntityRole "
                   + " Where erl_ent_id = ? "
                   + " And erl_rol_id = ? "
                   + " AND erl_eff_start_datetime = ? "
                   + " AND erl_eff_end_datetime = ? ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ent_id);
        stmt.setLong(2, rol_id);
        stmt.setTimestamp(3, startDate);
        stmt.setTimestamp(4, endDate);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
      //  if(rowAffected!=1) {
     //       String err = "Execute "
   //                    + SQL + " ent_id = " + ent_id + " rol_id = " + rol_id + " start_datetime = " + startDate + " end_datetime = " + endDate
  //                     + " returns " + rowAffected + " affected ";
 //           throw new SQLException(SQL);
//        }
    }
    
    //delete a record from acRoleFunction
    void delRoleFunction(Connection con, long rol_id, long ftn_id) throws SQLException {
        String SQL = " Delete FROM acRoleFunction "
                   + " Where rfn_rol_id = ? "
                   + " And rfn_ftn_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rol_id);
        stmt.setLong(2, ftn_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected!=1) {
            String err = "Execute "
                       + SQL + " rol_id = " + rol_id + " ftn_id = " + ftn_id 
                       + " returns " + rowAffected + " affected ";
            throw new SQLException(SQL);
        }
    }

    
    //input a Vector contains Long and return an int[]
    long[] Vector2long(Vector v) {
        int count=v.size();
        long[] result = new long[count];
        
        for(int i=0; i<count; i++) {
            result[i] = ((Long)v.elementAt(i)).intValue();
        }
        return result;
    }

    String[] Vector2String(Vector v) {
        int count=v.size();
        String[] result = new String[count];
        
        for(int i=0; i<count; i++) {
            result[i] = ((String)v.elementAt(i));
        }
        return result;
    }

    long maxEntityID(Connection con) throws SQLException{
        Statement stmt = con.createStatement();
        String SQL = " Select max(ent_id) from Entity " +
                     " WHERE ent_delete_usr_id IS NULL " +      // Fai: for soft-delete
                     " AND ent_delete_timestamp IS NULL ";
        long max;
        
        try {
            ResultSet rs = stmt.executeQuery(SQL);
            if (rs.next()) {
                max = rs.getLong(1);
            } else {
                throw new SQLException("Execute " + SQL + " returns a null result set");
            }
        } finally {
            stmt.close();
        }
        return max;
    }
    
}
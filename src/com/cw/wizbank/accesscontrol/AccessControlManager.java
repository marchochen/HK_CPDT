package com.cw.wizbank.accesscontrol;

import com.cw.wizbank.cache.wizbCacheManager;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.web.WzbApplicationContext;

import java.sql.*;
import java.util.*;

public class AccessControlManager {
    
    public static final String USER = "U";
    public static final String GROUP = "G";

    static final long ROOT = -1;
    static final long NO_GROUP_ASSIGNED = -1;
    AccessControlCore aclCore = new AccessControlCore();
    
/* API start */
    
    //check if the role function relation exist
    public boolean isRoleFunctionRelationExist(Connection con, String rol_ext_id, 
                                                String ftn_ext_id) throws SQLException {
        return AccessControlWZB.hasRolePrivilege(rol_ext_id, ftn_ext_id) ;

    }
    
    //delete all records in acRoleFunction belongs to a role
    public void delRoleFunctionOfRole(Connection con, String rol_ext_id) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);
        aclCore.delRoleFunctionOfRole(con, rol_id);
    }
    //check if an group has privilege on a function
    public boolean hasGroupPrivilege(Connection con, String usg_ext_id) throws SQLException {
        long usg_ent_id = getGroupEntityID(con, usg_ext_id);
        return hasGroupPrivilege(con, usg_ent_id);
    }    


    
    //get the entities that a role contains
    public String[][] containEntities(Connection con, String rol_ext_id) throws SQLException {
        String[][] Entities;
        long[] EntitiesID;
        long rol_id = getRoleID(con, rol_ext_id);
        
        EntitiesID = aclCore.containEntities(con, rol_id);
        Entities = EntID2ExtID(con, EntitiesID);
        return Entities;
    }
    
    //returns a SQL that will get the ent_id of entities who belongs to this role
    public String getEntityByRoleExistsSQL(Connection con, String rol_ext_id, String ent_id_colName) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);
        return " exists (" + aclCore.getEntityByRoleSQL(rol_id, ent_id_colName) + " and erl_ent_id = "+ent_id_colName+")";
    }    

    //returns a SQL that will get the ent_id of entities who belongs to this role
    public String getEntityByFunctionSQL(Connection con, String ftn_ext_id) throws SQLException {
        long ftn_id = getFunctionID(con, ftn_ext_id);
        Timestamp curTime = cwSQL.getTime(con);                  
        return aclCore.getEntityByFunctionSQL(ftn_id, curTime);
    }
    //returns a SQL that will get the ent_id of entities who belongs to this role
    public String getEntityByRoleSQL(Connection con, String rol_ext_id) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);
        Timestamp curTime = cwSQL.getTime(con);
        return aclCore.getEntityByRoleSQL(rol_id, curTime);
    }    
    
    public void assignUser2Role(Connection con, String usr_ext_id, String rol_ext_id) throws SQLException {
        assignUser2Role(con, usr_ext_id, rol_ext_id, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
    }
    //assign an user to a role
    public void assignUser2Role(Connection con, String usr_ext_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        long usr_ent_id = getUserEntityID(con, usr_ext_id);
        long rol_id = getRoleID(con, rol_ext_id);
        
        aclCore.assignEntity2Role(con, usr_ent_id, rol_id, startDate, endDate);
    }

    public void assignGroup2Role(Connection con, String usg_ext_id, String rol_ext_id) throws SQLException {
        assignGroup2Role(con, usg_ext_id, rol_ext_id, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
    }    
    //assign a group to a role
    public void assignGroup2Role(Connection con, String usg_ext_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        long usg_ent_id = getGroupEntityID(con, usg_ext_id);
        long rol_id = getRoleID(con, rol_ext_id);
        
        aclCore.assignEntity2Role(con, usg_ent_id, rol_id, startDate, endDate);
    }
    
    // for backward compatible
    public void rmUserFromRole(Connection con, String usr_ext_id, String rol_ext_id) throws SQLException {
        rmUserFromRole(con, usr_ext_id, rol_ext_id, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
    }

    //remove an user from a role
    public void rmUserFromRole(Connection con, String usr_ext_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        long usr_ent_id = getUserEntityID(con, usr_ext_id);
        long rol_id = getRoleID(con, rol_ext_id);
        
        aclCore.rmEntityFromRole(con, usr_ent_id, rol_id, startDate, endDate);
    }


    //remove a group from a role
    public void rmGroupFromRole(Connection con, String usg_ext_id, String rol_ext_id, Timestamp startDate, Timestamp endDate) throws SQLException {
        long usg_ent_id = getGroupEntityID(con, usg_ext_id);
        long rol_id = getRoleID(con, rol_ext_id);
        
        aclCore.rmEntityFromRole(con, usg_ent_id, rol_id, startDate, endDate);
    }


    //grant privilege on a finction to a role
    public void grantRolePrivilege(Connection con, String rol_ext_id, String ftn_ext_id) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);
        long ftn_id = getFunctionID(con, ftn_ext_id);
        
        aclCore.grantRolePrivilege(con, rol_id, ftn_id);
    }
    
    
    //remove privilege on a finction from a role
    public void rmRolePrivilege(Connection con, String rol_ext_id, String ftn_ext_id) throws SQLException {
        long rol_id = getRoleID(con, rol_ext_id);
        long ftn_id = getFunctionID(con, ftn_ext_id);
        
        aclCore.rmRolePrivilege(con, rol_id, ftn_id);
    }

    //Add an user
    public void addUser(Connection con, String usr_ext_id) throws SQLException {
        if(isUserExist(con, usr_ext_id)) 
            throw new SQLException("User " + usr_ext_id + " already exists");
        
        Timestamp curTime = cwSQL.getTime(con);
        long usr_ent_id = aclCore.insEntity(con, USER, curTime);
        insUser(con, usr_ent_id, usr_ext_id, curTime);
    }

    
    //Add a group with no parent
    public void addGroup(Connection con, String usg_ext_id) throws SQLException {
        if(isGroupExist(con, usg_ext_id)) 
            throw new SQLException("Group " + usg_ext_id + " already exists");
        
        Timestamp curTime = cwSQL.getTime(con);
        long usg_ent_id = aclCore.insEntity(con, GROUP, curTime);
        insGroup(con, usg_ent_id, usg_ext_id, curTime);
    }
    

    //Add a group with a parent
    public void addGroup(Connection con, String usg_ext_id, String usg_parent_ext_id) throws SQLException {
        if(isGroupExist(con, usg_ext_id)) 
            throw new SQLException("Group " + usg_ext_id + " already exists");
        long usg_parent_ent_id = getGroupEntityID(con, usg_parent_ext_id);
        
        Timestamp curTime = cwSQL.getTime(con);
        long usg_ent_id = aclCore.insEntity(con, GROUP, curTime);
        insGroup(con, usg_ent_id, usg_ext_id, usg_parent_ent_id, curTime);
    }


    //add a Role
    public void addRole(Connection con, String rol_ext_id) throws SQLException {
        aclCore.addRole(con, rol_ext_id);
    }


   

    //change the group's parent
    public void changeGroupParent(Connection con, String usg_ext_id, String usg_parent_ext_id) throws SQLException {
        long usg_ent_id = getGroupEntityID(con, usg_ext_id);
        long usg_parent_ent_id = getGroupEntityID(con, usg_parent_ext_id);
        Vector v = new Vector();
        //add the child into the vector and try to see if the parent is assigned, will it form a loop
        v.addElement(new Long(usg_ent_id));
        if(willGroupsLoopBack(con, v, usg_parent_ent_id)) 
            throw new SQLException("Cannot move group " + usg_ext_id + " under group " + usg_parent_ext_id + " as a loop will be formed");
        else
            changeGroupParent(con, usg_ent_id, usg_parent_ent_id);
    }


    public String[] getAllUsers(Connection con) throws SQLException {
        return getAllUserExtID(con);
    }
    
    
    public String[] getAllGroups(Connection con) throws SQLException {
        return getAllGroupExtID(con);
    }


    public String[] getAllRoles(Connection con) throws SQLException {
        return getAllRoleExtID(con);
    }


    public String[] getAllFunctions(Connection con) throws SQLException {
        return getAllFunctionExtID(con);
    }
    
/* API end */
    
    boolean hasGroupPrivilege(Connection con, long usg_ent_id) throws SQLException {
        boolean result;
        long usg_parent_ent_id;

        
            //group has no prilvilege, check its parent
            usg_parent_ent_id = getGroupParent(con, usg_ent_id);
            if(usg_parent_ent_id == ROOT)
                result = false;
            else
                result = hasGroupPrivilege(con, usg_parent_ent_id);
        return result;
    }    
    
    
    //change the group's parent by updating EntityRelation
    void changeGroupParent(Connection con, long usg_ent_id, long usg_parent_ent_id) throws SQLException {
        String SQL = " Update UserGroup Set usg_parent_ent_id = ? "
                   + " Where usg_ent_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_parent_ent_id);
        stmt.setLong(2, usg_ent_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected!=1) {
            String err = "Execute " + SQL 
                       + " usg_ent_id = " + usg_ent_id
                       + " usg_parent_ent_id = " + usg_parent_ent_id
                       + " affected " + rowAffected + " rows";
            
            throw new SQLException(err);
        }
    }
    
    
    boolean isParent(Connection con, long usg_ent_id) throws SQLException {
        boolean result;
        long count;
        String SQL = " Select count(*) From UserGroup "
                   + " Where usg_parent_ent_id = ? ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) 
            count = rs.getLong(1);
        else {	
            stmt.close();
            String err = "Execute " + SQL 
                       + " usg_parent_ent_id = " + usg_ent_id
                       + " return a null result set";
            throw new SQLException(err);
        }
        stmt.close();
        if(count>0)
            result = true;
        else
            result = false;
            
        return result;
    }
    
    
    //check if the groups are in a loop
    boolean willGroupsLoopBack(Connection con, Vector v, long usg_ent_id) throws SQLException {
        boolean result;
        
        if(v.contains(new Long(usg_ent_id)))
            //a loop is found
            result = true;
        else {
            //get the parentID
            long usg_parent_ent_id = getGroupParent(con, usg_ent_id);
            if(usg_parent_ent_id == ROOT)
                //reach the top
                result = false;
            else {
                //search again
                v.addElement(new Long(usg_ent_id));
                result = willGroupsLoopBack(con, v, usg_parent_ent_id);
            }
        }
        return result;
    }
    
    
    //get a group's parent
    long getGroupParent(Connection con, long usg_ent_id) throws SQLException {
        long usg_parent_ent_id;
        String SQL = " Select usg_parent_ent_id From UserGroup "
                   + " Where usg_ent_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            usg_parent_ent_id = rs.getLong("usg_parent_ent_id");
            if(usg_parent_ent_id == 0)
                usg_parent_ent_id = ROOT;
        }
        else {
            String err = "Execute " + SQL
                       + " usg_ent_id = " + usg_ent_id 
                       + " return a null result set";
            
            throw new SQLException(err);
        }        
        
        stmt.close();
        return usg_parent_ent_id;
    }
    
    //insert an user into RegUser
    void insUser(Connection con, long usr_ent_id, String usr_ext_id, Timestamp curTime) throws SQLException {
        String SQL = " Insert into RegUser "
                   + " (usr_ent_id, usr_ext_id, usr_creation_timestamp) "
                   + " Values "
                   + " (?, ?, ?) ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usr_ent_id);
        stmt.setString(2, usr_ext_id);
        stmt.setTimestamp(3, curTime);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected != 1) {
            String err = "Execute " + SQL 
                       + " usr_ent_id = " + usr_ent_id
                       + " usr_ext_id = " + usr_ext_id
                       + " usr_creation_timestamp = " + curTime;
                       
            throw new SQLException(err);
        }
    }
    

    //insert a group into Group, with no parent
    void insGroup(Connection con, long usg_ent_id, String usg_ext_id, Timestamp curTime) throws SQLException {
        String SQL = " Insert into UserGroup "
                   + " (usg_ent_id, usg_ext_id, usg_creation_timestamp) "
                   + " Values "
                   + " (?, ?, ?) ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_ent_id);
        stmt.setString(2, usg_ext_id);
        stmt.setTimestamp(3, curTime);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected != 1) {
            String err = "Execute " + SQL 
                       + " usg_ent_id = " + usg_ent_id
                       + " usg_ext_id = " + usg_ext_id
                       + " usg_creation_timestamp = " + curTime;
                       
            throw new SQLException(err);
        }
    }


    //insert a group into Group, with parent
    void insGroup(Connection con, long usg_ent_id, String usg_ext_id, long usg_parent_ent_id, Timestamp curTime) throws SQLException {
        String SQL = " Insert into UserGroup "
                   + " (usg_ent_id, usg_ext_id, usg_creation_timestamp, usg_parent_ent_id) "
                   + " Values "
                   + " (?, ?, ?, ?) ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_ent_id);
        stmt.setString(2, usg_ext_id);
        stmt.setTimestamp(3, curTime);
        stmt.setLong(4, usg_parent_ent_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected != 1) {
            String err = "Execute " + SQL 
                       + " usg_ent_id = " + usg_ent_id
                       + " usg_ext_id = " + usg_ext_id
                       + " usg_creation_timestamp = " + curTime
                       + " usg_parent_ent_id = " + usg_parent_ent_id;
                       
            throw new SQLException(err);
        }
    }

    
    //delete an user from RegUser
    void delUser(Connection con, long usr_ent_id) throws SQLException {
        String SQL = " Delete FROM RegUser Where usr_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usr_ent_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected!=1) {
            String err = "Execute " + SQL + " usr_ent_id = " + usr_ent_id + " returns " + rowAffected + " affected"; 
            throw new SQLException(err);
        }
    }    
    
    
    //delete a group from Group
    public void delGroup(Connection con, long usg_ent_id) throws SQLException {
        String SQL = " Delete FROM UserGroup Where usg_ent_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_ent_id);
        int rowAffected = stmt.executeUpdate();
        stmt.close();
        if(rowAffected!=1) {
            String err = "Execute " + SQL + " usg_ent_id = " + usg_ent_id + " returns " + rowAffected + " affected"; 
            throw new SQLException(err);
        }
    }    

    //look up RegUser to check if an User exists
    boolean isUserExist(Connection con, String usr_ext_id) throws SQLException {
        boolean result=false;
        long count=0;
        String err;
        String SQL = " Select count(*) from RegUser "
                   + " Where usr_ext_id = ? ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1,usr_ext_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            count = rs.getLong(1);
        }
        else {
            err = "Execute " 
                + SQL + " usr_ext_id = " + usr_ext_id 
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
    
    
    //look up Group to check if a Group exists
    boolean isGroupExist(Connection con, String usg_ext_id) throws SQLException {
        boolean result=false;
        long count=0;
        String err;
        String SQL = " Select count(*) from UserGroup "
                   + " Where usg_ext_id = ? ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1,usg_ext_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            count = rs.getLong(1);
        }
        else {
            err = "Execute " 
                + SQL + " usg_ext_id = " + usg_ext_id 
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

    //get the internal ent_id for a User
    long getUserEntityID(Connection con, String usr_ext_id) throws SQLException {
        long usr_ent_id;
        String SQL = " Select usr_ent_id From RegUser "
                   + " Where usr_ext_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, usr_ext_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            usr_ent_id = rs.getLong("usr_ent_id");
        else
            throw new SQLException("User " + usr_ext_id + " does not exists");
        
        stmt.close();
        return usr_ent_id;
    }
    
    
    //get the internal ent_id for a Group
    long getGroupEntityID(Connection con, String usg_ext_id) throws SQLException {
        long usg_ent_id;
        String SQL = " Select usg_ent_id From UserGroup "
                   + " Where usg_ext_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, usg_ext_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            usg_ent_id = rs.getLong("usg_ent_id");
        else
            throw new SQLException("Group " + usg_ext_id + " does not exists");
        
        stmt.close();
        return usg_ent_id;
    }

    
    //get the internal ent_id for a Role
    private static final String ROLE_EXTID2ID_CACHE = "ROLE_EXTID2ID_CACHE";
    long getRoleID(Connection con, String rol_ext_id) throws SQLException {
        HashMap cache = wizbCacheManager.getInstance().getCachedHashmap(ROLE_EXTID2ID_CACHE, true);
        if (cache.get(rol_ext_id) == null) {
            long rol_id;
            String SQL = " Select rol_id From acRole "
                       + " Where rol_ext_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, rol_ext_id);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
                rol_id = rs.getLong("rol_id");
            else{
            	if(rs!=null)rs.close();
                if (stmt != null)stmt.close();
                throw new SQLException("Role " + rol_ext_id + " does not exists");
            }
            rs.close();
            stmt.close();
            cache.put(rol_ext_id, new Long(rol_id));
            return rol_id;
        } else {
            return ((Long) cache.get(rol_ext_id)).longValue();
        }
    }

    
    //get the internal ent_id for a Function
    private static final String FUNCTION_EXTID2ID_CACHE = "FUNCTION_EXTID2ID_CACHE";
    long getFunctionID(Connection con, String ftn_ext_id) throws SQLException {
        HashMap cache = wizbCacheManager.getInstance().getCachedHashmap(FUNCTION_EXTID2ID_CACHE, true);
        if (cache.get(ftn_ext_id) == null) {
            long ftn_id;
            String SQL = " Select ftn_id From acFunction "
                       + " Where ftn_ext_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, ftn_ext_id);
            ResultSet rs = stmt.executeQuery();
            
            if(rs.next())
                ftn_id = rs.getLong("ftn_id");
            else{
            	if(rs!=null)rs.close();
                stmt.close();
                throw new SQLException("Function " + ftn_ext_id + " does not exists");
            }
            rs.close();
            stmt.close();
            cache.put(ftn_ext_id, new Long(ftn_id));
            return ftn_id;
        } else {
            return ((Long) cache.get(ftn_ext_id)).longValue();
        }
    }    

    
    //get the external ent_id for a User
    String getUserExtID(Connection con, long usr_ent_id) throws SQLException {
        String usr_ext_id;
        String SQL = " Select usr_ext_id From acFunction "
                   + " Where usr_ent_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usr_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            usr_ext_id = rs.getString("usr_ext_id");
        else
            throw new SQLException("User " + usr_ent_id + " does not exists");
        
        stmt.close();
        return usr_ext_id;
    }        
    

    //get the external ent_id for a Group
    String getGroupExtID(Connection con, long usg_ent_id) throws SQLException {
        String usg_ext_id;
        String SQL = " Select usg_ext_id From UserGroup "
                   + " Where usg_ent_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, usg_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            usg_ext_id = rs.getString("usg_ext_id");
        else
            throw new SQLException("Group " + usg_ent_id + " does not exists");
        
        stmt.close();
        return usg_ext_id;
    }        
    
    
    //get the external ent_id for a Role
    String getRoleExtID(Connection con, long rol_id) throws SQLException {
        String rol_ext_id;
        String SQL = " Select rol_ext_id From acRole "
                   + " Where rol_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, rol_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            rol_ext_id = rs.getString("rol_ext_id");
        else
            throw new SQLException("Role " + rol_id + " does not exists");
        
        stmt.close();
        return rol_ext_id;
    }


    //get the external ent_id for a Function
    String getFunctionExtID(Connection con, long ftn_id) throws SQLException {
        String ftn_ext_id;
        String SQL = " Select ftn_ext_id From acFunction "
                   + " Where ftn_id = ? ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ftn_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next())
            ftn_ext_id = rs.getString("ftn_ext_id");
        else
            throw new SQLException("Function " + ftn_id + " does not exists");
        
        stmt.close();
        return ftn_ext_id;
    }

    
    //given an array of User ID and returns the ExtID array
    String[] UserID2ExtID(Connection con, long[] userID) throws SQLException {
        int count = userID.length;
        String ExtID[] = new String[count];
        
        for(int i=0;i<count;i++) {
            ExtID[i] = getUserExtID(con, userID[i]);
        }
        return ExtID;
    }
    
    
    //given an array of Group ID and returns the ExtID array
    String[] GroupID2ExtID(Connection con, long[] groupID) throws SQLException {
        int count = groupID.length;
        String ExtID[] = new String[count];
        
        for(int i=0;i<count;i++) {
            ExtID[i] = getGroupExtID(con, groupID[i]);
        }
        return ExtID;
    }
    
    
    //given an array of Role ID and returns the ExtID array
    String[] RoleID2ExtID(Connection con, long[] roleID) throws SQLException {
        int count = roleID.length;
        String ExtID[] = new String[count];
        
        for(int i=0;i<count;i++) {
            ExtID[i] = getRoleExtID(con, roleID[i]);
        }
        return ExtID;
    }
    
    
    //given an array of Function ID and returns the ExtID array
    String[] FunctionID2ExtID(Connection con, long[] funcID) throws SQLException {
        int count = funcID.length;
        String ExtID[] = new String[count];
        
        for(int i=0;i<count;i++) {
            ExtID[i] = getFunctionExtID(con, funcID[i]);
        }
        return ExtID;
    }

    
    //returns the Entity external ID and it's type
    String[][] EntID2ExtID(Connection con, long[] entID) throws SQLException {
        int count = entID.length;
        String ExtID[][] = new String[count][2];
        String[] users;
        String[] groups;
        String list = new String();
        
        if(count > 0) {
            list = formatIDList(entID);
            users = UserID2ExtID(con,list);
            groups = GroupID2ExtID(con,list);
            
            for(int i=0;i<users.length;i++) {
                ExtID[i][0] = users[i];
                ExtID[i][1] = USER;
            }
            for(int i=users.length;i<count;i++) {
                ExtID[i][0] = groups[i-users.length];
                ExtID[i][1] = GROUP;
            }
        }
        return ExtID;
    }
    
    
    //return the users external ID
    String[] UserID2ExtID(Connection con, String list) throws SQLException {
        String[] users;
        String user;
        Vector v = new Vector();
        String SQL = " Select usr_ext_id From RegUser "
                   + " Where usr_ent_id in " + list;
                   
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while(rs.next()) {
            user = rs.getString("usr_ext_id");
            v.addElement(user);
        }
        stmt.close();
        users = Vector2String(v);
        return users;
    }
    

    //return the groups external ID
    String[] GroupID2ExtID(Connection con, String list) throws SQLException {
        String[] groups;
        String group;
        Vector v = new Vector();
        String SQL = " Select usg_ext_id From UserGroup "
                   + " Where usg_ent_id in " + list;
                   
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);
        while(rs.next()) {
            group = rs.getString("usg_ext_id");
            v.addElement(group);
        }
        stmt.close();
        groups = Vector2String(v);
        return groups;
    }


    //input a Vector contains String and return a String[]
    String[] Vector2String(Vector v) {
        int count=v.size();
        String[] result = new String[count];
        
        for(int i=0; i<count; i++) {
            result[i] = (String)v.elementAt(i);
        }
        return result;
    }
    
    
    //format the int[] into (1,3,4,...)
    //assume the int[] is not empty
    String formatIDList(long[] ID) {
        String list = "(";
        int count = ID.length;
        
        for(int i=0; i<count;i++) {
            list += ID[i] + ",";
        }
        
        list = list.substring(0,list.length()-1) + ")";
        return list;
    }

    
    //get the external ent_id for all User
    String[] getAllUserExtID(Connection con) throws SQLException {
        Vector v = new Vector();
        String[] users;
        String user;
        String SQL = " Select usr_ext_id From RegUser ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            user = rs.getString("usr_ext_id");
            v.addElement(user);
        }
        stmt.close();
        users = Vector2String(v);
        return users;
    }        


    //get the external ent_id for all Groups
    String[] getAllGroupExtID(Connection con) throws SQLException {
        Vector v = new Vector();
        String[] groups;
        String group;
        String SQL = " Select usg_ext_id From UserGroup ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            group = rs.getString("usg_ext_id");
            v.addElement(group);
        }
        stmt.close();
        groups = Vector2String(v);
        return groups;
    }        


    //get the external ent_id for all Roles
    String[] getAllRoleExtID(Connection con) throws SQLException {
        Vector v = new Vector();
        String[] roles;
        String role;
        String SQL = " Select rol_ext_id From acRole ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            role = rs.getString("rol_ext_id");
            v.addElement(role);
        }
        stmt.close();
        roles = Vector2String(v);
        return roles;
    }        


    //get the external ent_id for all Functions
    String[] getAllFunctionExtID(Connection con) throws SQLException {
        Vector v = new Vector();
        String[] functions;
        String function;
        String SQL = " Select ftn_ext_id From acFunction ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()) {
            function = rs.getString("ftn_ext_id");
            v.addElement(function);
        }
        stmt.close();
        functions = Vector2String(v);
        return functions;
    }        


	long getRoleAuthLevel(Connection con, long root_ent_id, String rol_ext_id)
		throws SQLException, cwSysMessage {
			
			String SQL = "Select rol_auth_level From acRole Where rol_ext_id = ? And rol_ste_ent_id = ? ";
			PreparedStatement stmt = con.prepareStatement(SQL);
			stmt.setString(1, rol_ext_id);
			stmt.setLong(2, root_ent_id);
			long auth_level = 0;
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				auth_level = rs.getLong("rol_auth_level");
			}else{
				stmt.close();
				throw new cwSysMessage("GEN005", "rol_ext_id = " + rol_ext_id);
			}
			stmt.close();
			return auth_level;
		}
	
	long getRoleMinAuthLevel(Connection con, long root_ent_id, String[] rol_ext_id_list)
		throws SQLException, cwSysMessage {
			if( rol_ext_id_list == null || rol_ext_id_list.length == 0 ) {
				throw new cwSysMessage("GEN005", " rol_ext_id is NULL");
			}
			StringBuffer SQLBuf = new StringBuffer(256);
			/*
			 *Using Select MIN(rol_auth_level) will return NULL in MS-SQL if no rol_ext_id match
			 *and may return other value in different DB
			 *So select rol_auth_level and order it ASC, 
			 *if no rol_ext_id match, resultset will empty and throw exception
			 *otherwise the first reslt is the min(rol_auth_level)
			 */
			SQLBuf.append(" Select rol_auth_level ")
				  .append(" From acRole ")
				  .append(" Where rol_ste_ent_id = ? ")
				  .append(" And rol_ext_id In ( ? ");
			for(int i=1; i<rol_ext_id_list.length; i++){
				SQLBuf.append(" , ? ");
			}
			SQLBuf.append(" ) ");
			SQLBuf.append(" Order By rol_auth_level ASC ");
			PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
			int index = 1;
			stmt.setLong(index++, root_ent_id);
			for(int i=0; i<rol_ext_id_list.length; i++){
				stmt.setString(index++, rol_ext_id_list[i]);
			}
			long auth_level = 0;
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				auth_level = rs.getLong(1);
			}else{
				stmt.close();
				throw new cwSysMessage("GEN005", " rol_ext_id not match");
			}
			stmt.close();
			return auth_level;
		}
	
	

}
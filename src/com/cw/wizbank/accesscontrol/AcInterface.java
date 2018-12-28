package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.db.view.ViewAcInstance;

/**
Interface between access control layer and objects
*/
public interface AcInterface {
    
    /**
    indicate for all roles
    */
    public static final String ALL_ROLES = ViewAcInstance.ALL_ROLES;
    
    /**
    indicate for all functions
    */
    public static final String ALL_FUNCTIONS = ViewAcInstance.ALL_FUNCTIONS;
    
    /**
    indicate for all entities
    */
    public static final long ALL_ENTITIES = ViewAcInstance.ALL_ENTITIES;
    
    /**
    indicate for all instances
    */
    public static final long ALL_INSTANCES = ViewAcInstance.ALL_INSTANCES;

    /**
    indicate need to walk through the user group tree to find entity ancestors  <BR>
    */
    public static final Vector FIND_OUT_ANCESTORS = ViewAcInstance.FIND_OUT_ANCESTORS;




    /**
    assign the user, role pair to an instance (grant the pair all instance level function to the instance)
    */
    public void assignEntity(long res_id, long ent_id, String rol_ext_id, 
                                boolean owner_ind, String create_usr_id, Timestamp create_timestamp) 
                                throws SQLException;

    /**
    remove the user, role pair from an instance (remove all the instance functions of the user, role pair). It will not remove owner record at any case
    */
    public void rmEntity(long res_id, long ent_id, String rol_ext_id) throws SQLException;

    /**
    remove instance level function from the user, role pair
    @param deleteOwner indicate if need to delete owner
    */
    public void rmPrivilege(long res_id, long ent_id, String rol_ext_id, 
                            String ftn_ext_id, boolean deleteOwner) throws SQLException;

    /**
    grant the user, role pair an instance level function
    */
    public void grantPrivilege(long res_id, long ent_id, String rol_ext_id, 
                                String ftn_ext_id, boolean owner_ind, String create_usr_id, 
                                Timestamp create_timestamp) throws SQLException;
}
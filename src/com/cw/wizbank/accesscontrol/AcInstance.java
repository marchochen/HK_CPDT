package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.view.ViewAcInstance;

/**
Logical Layer Access Control <BR>
It provides API for other "Ac" classes extend it. <BR>
It should contain the business logic on the access control of objects (e.g. catalog)<BR>
Those API will call the database API from the "view" package.
Class extends this one should have a constructor define the view being used. e.g.<BR>
<pre>
public AcResources() {
    view = new ViewAcResources();
}
</pre>
view defines which database API will be used
*/
public abstract class AcInstance implements AcInterface {

    /**
    access control object to handle function level functions
    */
    protected AccessControlWZB acl;

    /**
    access control object to handle instance level functions
    */
    protected ViewAcInstance view;

    /**
    database connection
    */
    protected Connection con;
    
    public void setCon(Connection con) {
        this.con = con;
    }
    
    public Connection getCon() {
        return this.con;
    }

    public AcInstance(Connection con) {
        super();
        setCon(con);
    }

    /**
    check if the user, role pair has specified functional privilege<BR>
    will not look up the user group tree as it is functional level <BR>
    @param ent_id must be provided
    @param rol_ext_id must br provided
    @exception SQLException database access error
    @return true if has privilege, false if hasn't
    */
    

    /**
    check if the user, role pair has a specified instance privilege<BR>
    will look up the user group tree as it is instance level<BR>
    @param v_ancestors the vector of the parent groups entity id <BR>will look up the ancestors itself if v_ancestors == FIND_OUT_ANCESTORS <BR>if you don't want it to do so, initialize v_ancestors with an empty Vector() <BR>
    All other params must be provided<BR>
    */
    public boolean hasInstancePrivilege(long res_id, long ent_id, String rol_ext_id, 
                                        String ftn_ext_id, Vector v_ancestors) throws SQLException {
        
        return view.hasPrivilege(res_id, ent_id, rol_ext_id, ftn_ext_id, v_ancestors);
    }

    /**
    assign an entity to instance.<BR>
    @param create_timestamp if it is null, will get the database time instead<BR>
    All other param must be provided
    */
    public void assignEntity(long res_id, long ent_id, String rol_ext_id, 
                                boolean owner_ind, String create_usr_id, Timestamp create_timestamp) 
                                throws SQLException {

        view.assignEntity2Instance(res_id, ent_id, rol_ext_id, owner_ind, 
                                    create_usr_id, create_timestamp);
        return;
    }
    
    /**
    remove entity from instance. behaves like rmPrivilege. the only difference between them are:
    <ul>
    <li>instance_id must be supplied here
    <li>if ent_id == ALL_ENTITIES, will only delete records having "ent_id is null", but not all records regardless what ent_id is
    <li>if rol_ext_id == ALL_ROLES, will only delete records having "rol_ext_id is null", but not all records
    <li>will remove all the functions here
    <li>will not remove owner record at any case
    </ul>
    @param res_id must be provided
    @param ent_id ALL_ENTITIES to indicate ent_id is null
    @param rol_ext_id ALL_ROLES to indicate rol_ext_id is null
    */
    public void rmEntity(long res_id, long ent_id, String rol_ext_id) 
        throws SQLException {
        
        view.rmEntityFromInstance(res_id, ent_id, rol_ext_id);
        return;
    }
    
    /**
    remove function from entity<BR>
    @param instance_id use ALL_INSTANCE to indicate whatever instance_id is
    @param ent_id ALL_ENTITIES to indicate whatever ent_id is
    @param rol_ext_id ALL_ROLES to indicate whatever rol_ext_id is
    @param ftn_ext_id use ALL_FUNCTION to indicate whatever ftn_ext_id is
    @param deleteOwner delete owner records as well if true, only delete non owner records if false
    @exception SQLException database access error
    */
    public void rmPrivilege(long res_id, long ent_id, String rol_ext_id, 
                            String ftn_ext_id, boolean deleteOwner) 
                            throws SQLException {
        
        view.rmPrivilege(res_id, ent_id, rol_ext_id, ftn_ext_id, deleteOwner);
    }

    /**
    grant function to entity<BR>
    @param ent_id if ent_id == ALL_ENTITIES, insert null to database <BR>
    @param rol_ext_id if rol_ext_id == ALL_ROLES, insert null to database <BR>
    @param create_timestamp if not provide, insert the current database time<BR>
    other param must be supplied  <BR>
    @exception SQLException database access error
    */
    public void grantPrivilege(long res_id, long ent_id, String rol_ext_id, 
                                String ftn_ext_id, boolean owner_ind, String create_usr_id, 
                                Timestamp create_timestamp) throws SQLException {
        
         view.grantPrivilege(res_id, ent_id, rol_ext_id, ftn_ext_id, owner_ind, 
                                create_usr_id, create_timestamp);
    }
    
    /**
    Given user, role, function, find out the instance granted<BR>
    @param rol_ext_id may pass ALL_ROLES to indicate not to construct Where rol_ext_id = '..' in SQL
    @return a long array of instance_id
    */
    public long[] getGrantedInstance(long ent_id, String rol_ext_id, String ftn_ext_id)
        throws SQLException {
            
        return view.getGrantedInstance(ent_id, rol_ext_id, ftn_ext_id);        
    }
    
    /**
    Given instance, role, funtion, find out the entity (users) that has assigned the function<BR>
    @param rol_ext_id may pass ALL_ROLES to indicate not to construct Where rol_ext_id = '..' in SQL
    @return a long array of ent_id
    */
    public long[] getAssignedEntity(long ent_id, String rol_ext_id, String ftn_ext_id) throws SQLException {
        
        return view.getAssignedEntity(ent_id, rol_ext_id, ftn_ext_id);
    }
}
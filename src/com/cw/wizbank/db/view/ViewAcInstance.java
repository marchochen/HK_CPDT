/**
This package contains views on database
@since 2001-07-18
*/
package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.qdb.dbUserGroup; //need the traceParentId()
import com.cw.wizbank.qdb.dbUtils;     //need vec2list()
import com.cw.wizbank.qdb.qdbException;

/**
ViewAcInstance is an abstract class to due with <B>instance level</B> access control <BR>
It is aimed to provide database access API to "view" extends it.<BR>
Any object wants to implement instance level access control should have a class extend this one.<BR>
<I>e.g. ViewAcModule extends ViewAcInstance</I> <BR>
In the extended class, there are some variables need to be initialized. They are: <BR>
<I>Note that all database transactions will not be commited or rollbacked in this class.</I><BR>
<ul>
<li>dbTableName <BR>
<li>colInstanceId <BR>
<li>colEntityId <BR>
<li>colRoleExtId  <BR>
<li>colFunctionExtId  <BR>
<li>colOwnerInd  <BR>
<li>colCreateUsrId  <BR>
<li>colCreateTimestamp  <BR>
<li>ftn_types <BR>
</ul>
*/
public abstract class ViewAcInstance {
    
    /**
    ALL_ROLES indicate "for all roles" <BR>
    */
    public static final String ALL_ROLES = "AC_ALL_ROLES";
    
    /**
    ALL_ENTITIES indicate "for all entities" <BR>
    */
    public static final long ALL_ENTITIES = 0;

    /**
    ALL_INSTANCE indicate "for all instances" <BR>
    */
    public static final long ALL_INSTANCES = 0;
    
    /**
    ALL_FUNCTIONS indicate "for all functions" <BR>
    */
    public static final String ALL_FUNCTIONS = "AC_ALL_FUNCTIONS";

    /**
    used by: <BR>
    public boolean hasPrivillege(long instance_id, long ent_id, String rol_ext_id, String ftn_ext_id, Vector v_ancestors) throws SQLException <BR>
    to indicate need to walk through the user group tree to find entity ancestors  <BR>
    **/
    public static final Vector FIND_OUT_ANCESTORS = null;
    
    /**
    store the value indicate instance level function in acFunction.ftn_level
    */
    protected static final String FTN_LEVEL_INSTANCE = "INSTANCE";
    
    /**
    store the table name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    dbTableName = "acModule"; <BR>
    */
    protected String dbTableName;

    /**
    store the instance column name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    colInstanceId = "acm_mod_id"; <BR>
    */
    protected String colInstanceId;
    
    /**
    store the entity column name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    colEntityId = "acm_ent_id"; <BR>
    */
    protected String colEntityId;
    
    /**
    store the role column name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    colEntityId = "acm_rol_ext_id"; <BR>
    */
    protected String colRoleExtId;

    /**
    store the function column name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    colEntityId = "acm_ftn_ext_id"; <BR>
    */
    protected String colFunctionExtId;

    /**
    store the owner_ind column name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    colEntityId = "acm_owner_ind"; <BR>
    */
    protected String colOwnerInd;

    /**
    store the create_usr_id column name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    colEntityId = "acm_create_usr_id"; <BR>
    */
    protected String colCreateUsrId;

    /**
    store the create_timestamp column name <BR>
    any class extend this class should initialize this variable <BR>
    for example: <BR>
    colEntityId = "acm_create_timestamp"; <BR>
    */
    protected String colCreateTimestamp;
    
    /**
    store the function type <BR>
    any class extend this class should initialize this variables <BR>
    it stores the function types 
    */
    protected Vector ftn_types = new Vector();
    
    /**
    database connection
    */
    protected Connection con;
    
    /**
    assign connection 
    */
    protected void setCon(Connection con) {
        this.con = con;
    }
    
    /**
    get the object's connection
    */
    protected Connection getCon() {
        return this.con;
    }
    
    public ViewAcInstance(Connection con) {
        super();
        setCon(con);
    }
    
    /**
    remove entity from instance. behaves like rmPrivilege. the difference between them are:
    <ul>
    <li>instance_id must be supplied here
    <li>if ent_id == ALL_ENTITIES, will only delete records having "ent_id is null", but not all records regardless what ent_id is
    <li>if rol_ext_id == ALL_ROLES, will only delete records having "rol_ext_id is null", but not all records
    <li>will remove all the functions here
    <li>will not delete owner record at any case
    </ul>
    @param instance_id must be provided
    @param ent_id ALL_ENTITIES to indicate ent_id is null
    @param rol_ext_id ALL_ROLES to indicate rol_ext_id is null
    */
    public void rmEntityFromInstance(long instance_id, long ent_id, String rol_ext_id) 
        throws SQLException {
        
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append("Delete From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colInstanceId).append(" = ? ");
        if(ent_id != ALL_ENTITIES) {
            SQLBuf.append(" And ").append(colEntityId).append(" = ? ");
        }
        else {
            SQLBuf.append(" And ").append(colEntityId).append(" is null ");
        }
        if(!rol_ext_id.equals(ALL_ROLES)) {
            SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        }
        else {
            SQLBuf.append(" And ").append(colFunctionExtId).append(" is null ");
        }
        SQLBuf.append(" And ").append(colOwnerInd).append(" = ? ");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, instance_id);
        if(ent_id != ALL_ENTITIES) {
            stmt.setLong(index++, ent_id);
        }
        if(!rol_ext_id.equals(ALL_ROLES)) {
            stmt.setString(index++, rol_ext_id);
        }
        stmt.setBoolean(index++, false);
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    /**
    assign an entity to an instance and grant all instance level functions to that entity according to the role <BR>
    if the user does not belongs to the role, SQLException will be thrown
    @param instance_id the instance talking about
    @param ent_id an entity id or ALL_ENTITIES
    @param rol_ext_id need to specified the role, so that can look up what instance functions this role has
    @exception SQLException database access error
    */
    public void assignEntity2Instance(long instance_id, long ent_id, String rol_ext_id, 
                                            boolean owner_ind, String create_usr_id, 
                                            Timestamp create_timestamp) throws SQLException {
        
        if(rol_ext_id != null && !rol_ext_id.equals(ALL_ROLES)) {
            
            StringBuffer SQLBuf = new StringBuffer(2048);

            rmEntityFromInstance(instance_id, ent_id, rol_ext_id);
            
            if(create_timestamp == null) {
                create_timestamp = cwSQL.getTime(con);
            }
            
            SQLBuf.append("Insert into ").append(dbTableName);
            SQLBuf.append(" (").append(colInstanceId).append(",");
            SQLBuf.append(colEntityId).append(",");
            SQLBuf.append(colRoleExtId).append(",");
            SQLBuf.append(colFunctionExtId).append(",");
            SQLBuf.append(colOwnerInd).append(",");
            SQLBuf.append(colCreateUsrId).append(",");
            SQLBuf.append(colCreateTimestamp).append(")");
            SQLBuf.append(" Select ?,");
            if(ent_id == ALL_ENTITIES) {
                SQLBuf.append("null,");
            }
            else {
                SQLBuf.append("?,");
            }
            SQLBuf.append("?,ftn_ext_id,?,?,?");
            SQLBuf.append(" From acFunction, acRoleFunction, acRole ");
            SQLBuf.append(" Where ftn_type in ").append(generateStringSQLList(ftn_types));
            SQLBuf.append(" And ftn_level = ? ");
            SQLBuf.append(" And ftn_id = rfn_ftn_id ");
            SQLBuf.append(" And rfn_rol_id = rol_id ");
            SQLBuf.append(" And rol_ext_id = ? ");
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setLong(index++, instance_id);
            if(ent_id != ALL_ENTITIES) {
                stmt.setLong(index++, ent_id);
            }
            stmt.setString(index++, rol_ext_id);
            stmt.setBoolean(index++, owner_ind);
            stmt.setString(index++, create_usr_id);
            stmt.setTimestamp(index++, create_timestamp);
            stmt.setString(index++, FTN_LEVEL_INSTANCE);
            stmt.setString(index++, rol_ext_id);
            stmt.executeUpdate();
            stmt.close();
        }
        return;
    }
    
    /**
    generate a SQL list for String <BR>
    e.g. ('MODULE','RESOURCE','') <BR>
    @param str_array a String array need to be filled into the SQL list <BR>
    @return if str_array is empty, return ('') <BR>
    if str_array is not empty, return the list generated
    */
    protected String generateStringSQLList(Vector v) {
        
        StringBuffer list = new StringBuffer(512);
        
        if(v == null || v.size() == 0) {
            list.append("('')");
        }
        else {
            list.append("(");
            for(int i=0; i<v.size(); i++) {
                list.append("'").append((String)v.elementAt(i)).append("',");
            }
            list.append("'')");
        }
        return list.toString();
    }
    
    /**
    delete records from access control table<BR>
    @param instance_id use ALL_INSTANCE to indicate whatever instance_id is
    @param ent_id use ALL_ENTITIES to indicate whatever ent_id is
    @param rol_ext_id use ALL_ROLES to indicate whatever rol_ext_id is
    @param ftn_ext_id use ALL_FUNCTION to indicate whatever ftn_ext_id is
    @param deleteOwner delete owner records as well if true, only delete non owner records if false
    @exception SQLException database access error
    */
    public void rmPrivilege(long instance_id, long ent_id, String rol_ext_id, 
                                String ftn_ext_id, boolean deleteOwner) throws SQLException {
    
        StringBuffer SQLBuf = new StringBuffer(2048);
        
        if(rol_ext_id == null) {
            rol_ext_id = ALL_ROLES;
        }
        if(ftn_ext_id == null) {
            ftn_ext_id = ALL_FUNCTIONS;
        }
        
        SQLBuf.append("Delete From ").append(dbTableName);
        SQLBuf.append(" Where 1=1 ");
        if(instance_id != ALL_INSTANCES) {
            SQLBuf.append("And ").append(colInstanceId).append(" = ? ");
        }
        if(ent_id != ALL_ENTITIES) {
            SQLBuf.append("And ").append(colEntityId).append(" = ? ");
        }
        if(!rol_ext_id.equals(ALL_ROLES)) {
            SQLBuf.append("And ").append(colRoleExtId).append(" = ? ");
        }
        if(!ftn_ext_id.equals(ALL_FUNCTIONS)) {
            SQLBuf.append("And ").append(colFunctionExtId).append(" = ? ");
        }
        if(!deleteOwner) {
            SQLBuf.append("And ").append(colOwnerInd).append(" = ? ");
        }
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        if(instance_id != ALL_INSTANCES) {
            stmt.setLong(index++, instance_id);
        }
        if(ent_id != ALL_ENTITIES) {
            stmt.setLong(index++, ent_id);
        }
        if(!rol_ext_id.equals(ALL_ROLES)) {
            stmt.setString(index++, rol_ext_id);
        }
        if(!ftn_ext_id.equals(ALL_FUNCTIONS)) {
            stmt.setString(index++, ftn_ext_id);
        }
        if(!deleteOwner) {
            stmt.setBoolean(index++, false);
        }
        stmt.executeUpdate();
        stmt.close();
        return;
    }
    
    /**
    look up database to see if a access control record exist
    @param ent_id set it to ALL_ENTITIES means "for all entities"
    @param rol_ext_id set it to ALL_ROLES means "for all roles"
    @return true if record, false if not
    @exception database access error
    */
    protected boolean isRecordExist(long instance_id, long ent_id, String rol_ext_id,
                                    String ftn_ext_id) throws SQLException {
        
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(512);
        SQLBuf.append("Select ").append(colEntityId).append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colInstanceId).append(" = ? ");
        if(ent_id != ALL_ENTITIES) {
            SQLBuf.append(" And ").append(colEntityId).append(" = ? ");
        }
        else {
            SQLBuf.append(" And ").append(colEntityId).append(" is null ");
        }
        if(!rol_ext_id.equals(ALL_ROLES)) { 
            SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        }
        else {
            SQLBuf.append(" And ").append(colRoleExtId).append(" is null ");
        }
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, instance_id);
        if(ent_id != ALL_ENTITIES) {
            stmt.setLong(index++, ent_id);
        }
        if(!rol_ext_id.equals(ALL_ROLES)) { 
            stmt.setString(index++, rol_ext_id);
        }
        stmt.setString(index++, ftn_ext_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            result = true;
        }
        else {
            result = false;
        }
        stmt.close();
        return result;
    }
    
    /**
    insert one record into dbTableName <BR>
    will check the existence of the record 1st, if record exists, do nothing
    @param ent_id if ent_id == ALL_ENTITIES, insert null to database <BR>
    @param rol_ext_id if rol_ext_id == ALL_ROLES, insert null to database <BR>
    other fields must be supplied  <BR>
    @exception SQLException database access error
    */
    public void grantPrivilege(long instance_id, long ent_id, String rol_ext_id, 
                                    String ftn_ext_id, boolean owner_ind, String create_usr_id, 
                                    Timestamp create_timestamp) throws SQLException {
        
        StringBuffer SQLBuf = new StringBuffer(2048);
        
        //if the input role is null, treat it as for all roles
        if(rol_ext_id == null) {
            rol_ext_id = ALL_ROLES;
        }

        if(!isRecordExist(instance_id, ent_id, rol_ext_id, ftn_ext_id)) {
            //get the current database time if no create_timestamp is given
            if(create_timestamp == null) {
                create_timestamp = cwSQL.getTime(con);
            }

            SQLBuf.append("Insert into ").append(dbTableName);
            SQLBuf.append(" (").append(colInstanceId).append(",");
            SQLBuf.append(colEntityId).append(",");
            SQLBuf.append(colRoleExtId).append(",");
            SQLBuf.append(colFunctionExtId).append(",");
            SQLBuf.append(colOwnerInd).append(",");
            SQLBuf.append(colCreateUsrId).append(",");
            SQLBuf.append(colCreateTimestamp).append(")");
            SQLBuf.append(" Values ");
            SQLBuf.append("(?,");   //instance_id
            if(ent_id == ALL_ENTITIES) {
                SQLBuf.append("null,");
            }
            else {
                SQLBuf.append("?,");
            }
            if(rol_ext_id.equals(ALL_ROLES)) {
                SQLBuf.append("null,");
            }
            else {
                SQLBuf.append("?,");
            }
            SQLBuf.append("?, ?, ?, ?)");

            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setLong(index++, instance_id);
            if(ent_id != ALL_ENTITIES) {
                stmt.setLong(index++, ent_id);
            }
            if(!rol_ext_id.equals(ALL_ROLES)) {
                stmt.setString(index++, rol_ext_id);
            }
            stmt.setString(index++, ftn_ext_id);
            stmt.setBoolean(index++, owner_ind);
            stmt.setString(index++, create_usr_id);
            stmt.setTimestamp(index++, create_timestamp);
            stmt.executeUpdate();
            stmt.close();
        }
        return;
    }
    
    /**
    check if the entity has privilege on the instance<BR>
    case 1: entity has a record on access control table <BR>
    case 2: has a record with "for all entities" <BR>
    case 3: has a record with "for all roles" <BR>
    case 4: has a record with "for all entities" and "for all roles" <BR>
    @param v_ancestors the vector of the parent groups entity id <BR>will look up the ancestors itself if v_ancestors == FIND_OUT_ANCESTORS <BR>if you don't want it to do so, initialize v_ancestors with an empty Vector() <BR>
    All other params must be provided<BR>
    @return true if entity has privillege, false if hasn't
    @exception SQLException database access error
    */
    public boolean hasPrivilege(long instance_id, long ent_id, String rol_ext_id, 
                                    String ftn_ext_id, Vector v_ancestors) throws SQLException {
        
        String entIdSQLList;
        
        //find out the ancestors if not given
        if(v_ancestors == FIND_OUT_ANCESTORS) {  
            try {
                v_ancestors = dbUserGroup.traceParentID(con, ent_id);
            }
            catch(qdbException qdbe) {
                throw new SQLException(qdbe.getMessage());
            }
        }
        
        //add the entity itself to v_ancestors and then
        //convert v_ancestors to a SQL list (1, 2, 5) and then
        //ask database if one of the entities in the list has privilege
        v_ancestors.addElement(new Long(ent_id));
        entIdSQLList = dbUtils.vec2list(v_ancestors);
        return hasPrivilege(instance_id, entIdSQLList, rol_ext_id, ftn_ext_id);        
    }
    
    /**
    check if one of entities on the entIdSQLList has privilege <BR>
    case 1: entity has a record on access control table <BR>
    case 2: has a record with "for all entities" <BR>
    case 3: has a record with "for all roles" <BR>
    case 4: has a record with "for all entities" and "for all roles" <BR>
    @param entIdSQLList a SQL list looks like (1, 2, 3) <BR>
    @return true if entity has privillege, false if hasn't
    @exception SQLException database access error
    */
    protected boolean hasPrivilege(long instance_id, String entIdSQLList, String rol_ext_id, 
                                        String ftn_ext_id) throws SQLException { 
        
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(2048);
        SQLBuf.append("Select ").append(colEntityId).append(" AS e");
        SQLBuf.append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colInstanceId).append(" = ? ");
        SQLBuf.append(" And ").append(colEntityId).append(" in ").append(entIdSQLList);
        SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
        SQLBuf.append(" Union ");
        SQLBuf.append("Select ").append(colEntityId).append(" AS e");
        SQLBuf.append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colInstanceId).append(" = ? ");
        SQLBuf.append(" And ").append(colEntityId).append(" is null ");
        SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
        SQLBuf.append(" Union ");
        SQLBuf.append("Select ").append(colEntityId).append(" AS e");
        SQLBuf.append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colInstanceId).append(" = ? ");
        SQLBuf.append(" And ").append(colEntityId).append(" in ").append(entIdSQLList);
        SQLBuf.append(" And ").append(colRoleExtId).append(" is null ");
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
        SQLBuf.append(" Union ");
        SQLBuf.append("Select ").append(colEntityId).append(" AS e");
        SQLBuf.append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colInstanceId).append(" = ? ");
        SQLBuf.append(" And ").append(colEntityId).append(" is null ");
        SQLBuf.append(" And ").append(colRoleExtId).append(" is null ");
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, instance_id);
        stmt.setString(index++, rol_ext_id);
        stmt.setString(index++, ftn_ext_id);
        stmt.setLong(index++, instance_id);
        stmt.setString(index++, rol_ext_id);
        stmt.setString(index++, ftn_ext_id);
        stmt.setLong(index++, instance_id);
        stmt.setString(index++, ftn_ext_id);
        stmt.setLong(index++, instance_id);
        stmt.setString(index++, ftn_ext_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            result = true;
        }
        else {
            result = false;
        }
        stmt.close();
        return result;
    }
    
    /**
    find out the ent_id of users that has assigned the function<BR>
    @param rol_ext_id may pass ALL_ROLES to indicate not to construct Where rol_ext_id = '..' in SQL
    @return a long array of ent_id
    */
    public long[] getAssignedEntity(long instance_id, String rol_ext_id, String ftn_ext_id) throws SQLException {

        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
            
        SQLBuf.append(" Select ").append(colEntityId).append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colInstanceId).append(" = ? ");
        if(!rol_ext_id.equals(ALL_ROLES)) {
            SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        }
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
            
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, instance_id);
        if(!rol_ext_id.equals(ALL_ROLES)) {
            stmt.setString(index++, rol_ext_id);
        }
        stmt.setString(index++, ftn_ext_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v.addElement(new Long(rs.getLong(colEntityId)));
        }
        stmt.close();
        return vector2long(v);
    }

    /**
    Given user, role, function, find out the instance granted<BR>
    @param rol_ext_id may pass ALL_ROLES to indicate not to construct Where rol_ext_id = '..' in SQL
    @return a long array of instance_id
    */
    public long[] getGrantedInstance(long ent_id, String rol_ext_id, String ftn_ext_id) throws SQLException {

        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
            
        SQLBuf.append(" Select ").append(colInstanceId).append(" From ").append(dbTableName);
        SQLBuf.append(" Where (").append(colEntityId).append(" = ? ")
              .append(" Or ").append(colEntityId).append(" is null) ");
        if(!rol_ext_id.equals(ALL_ROLES)) {
            SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        }
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
            
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, ent_id);
        if(!rol_ext_id.equals(ALL_ROLES)) {
            stmt.setString(index++, rol_ext_id);
        }
        stmt.setString(index++, ftn_ext_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v.addElement(new Long(rs.getLong(colInstanceId)));
        }
        stmt.close();
        return vector2long(v);
    }

    /**
    Given user, role find out the instance granted no matter what function is<BR>
    @param rol_ext_id may pass ALL_ROLES to indicate not to construct Where rol_ext_id = '..' in SQL
    @return a long array of instance_id
    */
    public long[] getGrantedInstance(long ent_id, String rol_ext_id) throws SQLException {

        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(256);
            
        SQLBuf.append(" Select distinct(").append(colInstanceId).append(") From ").append(dbTableName);
        SQLBuf.append(" Where (").append(colEntityId).append(" = ? ")
              .append(" Or ").append(colEntityId).append(" is null) ");
        if(!rol_ext_id.equals(ALL_ROLES)) {
            SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        }
            
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setLong(index++, ent_id);
        if(!rol_ext_id.equals(ALL_ROLES)) {
            stmt.setString(index++, rol_ext_id);
        }
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v.addElement(new Long(rs.getLong(colInstanceId)));
        }
        stmt.close();
        return vector2long(v);
    }
    
    /**
    format a vector into long array
    */
    protected long[] vector2long(Vector v) {
        
        if(v == null) {
            return null;
        }
        
        long[] l = new long[v.size()];
        for(int i=0; i<v.size(); i++) {
            l[i] = ((Long) v.elementAt(i)).longValue();
        }
        return l;
    }

}
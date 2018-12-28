package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.cwSQL;

/**
ViewAcAbsInstance is an abstract class to due with accesscontrol of abstract instance 
such as HomePage, Reports<BR>
It is aimed to provide database access API to "view" extends it.<BR>
<I>Note that all database transactions will not be commited or rollbacked in this class.</I><BR>
There are some variables need to be initialized in classes extend it. They are: <BR>
<ul>
<li>dbTableName <BR>
<li>colEntityId <BR>
<li>colRoleExtId  <BR>
<li>colFunctionExtId  <BR>
<li>colOwnerInd  <BR>
<li>colCreateUsrId  <BR>
<li>colCreateTimestamp  <BR>
<li>ftn_types <BR>
</ul>
*/
public abstract class ViewAcAbsInstance {
    
    /**
    ALL_ROLES indicate "for all roles" <BR>
    */
    public static final String ALL_ROLES = "AC_ALL_ROLES";
    
    /**
    ALL_ENTITIES indicate "for all entities" <BR>
    */
    public static final long ALL_ENTITIES = 0;

    /**
    indicate need to find ancestors itself
    */
    public Vector FIND_OUT_ANCESTORS = null;

    /**
    ALL_FUNCTIONS indicate "for all functions" <BR>
    */
    public static final String ALL_FUNCTIONS = "AC_ALL_FUNCTIONS";

    /**
    access control table name
    */
    protected String dbTableName;

    /**
    entity id column name of the access control table
    */
    protected String colEntityId;
        
    /**
    role ext id column name of the access control table
    */
    protected String colRoleExtId;
    
    /**
    function ext id column of the access control table
    */
    protected String colFunctionExtId;
    
    /**
    create user id column name of the access control table
    */
    protected String colCreateUsrId;
    
    /**
    create timestamp column name of the access control table
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
    
    public ViewAcAbsInstance(Connection con) {
        super();
        setCon(con);
    }

    /**
    check if the role has a function privilege<BR>
    case 1: entity has a record on access control table <BR>
    case 2: has a record with "for all roles" <BR>
    @return true if entity has privillege, false if hasn't
    @exception SQLException database access error
    */
    public boolean hasPrivilege(String rol_ext_id, String ftn_ext_id) 
        throws SQLException { 
        
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(512);
        
        SQLBuf.append("Select ").append(colRoleExtId).append(" AS e");
        SQLBuf.append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colRoleExtId).append(" = ?");
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ?");
        SQLBuf.append(" Union ");
        SQLBuf.append("Select ").append(colRoleExtId).append(" AS e");
        SQLBuf.append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colRoleExtId).append(" is null");
        SQLBuf.append(" And ").append(colFunctionExtId).append(" = ?");
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setString(index++, rol_ext_id);
        stmt.setString(index++, ftn_ext_id);
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
    @param rol_ext_id if rol_ext_id == ALL_ROLES, insert null to database <BR>
    other fields must be supplied  <BR>
    @exception SQLException database access error
    */
    public void grantPrivilege(String rol_ext_id, String ftn_ext_id, 
                                String create_usr_id, Timestamp create_timestamp) 
                                throws SQLException {
        
        StringBuffer SQLBuf = new StringBuffer(2048);
        
        //if the input role is null, treat it as for all roles
        if(rol_ext_id == null) {
            rol_ext_id = ALL_ROLES;
        }

        if(!isRecordExist(rol_ext_id, ftn_ext_id)) {
            //get the current database time if no create_timestamp is given
            if(create_timestamp == null) {
                create_timestamp = cwSQL.getTime(con);
            }

            SQLBuf.append("Insert into ").append(dbTableName).append(" ("); 
            SQLBuf.append(colRoleExtId).append(" , ");
            SQLBuf.append(colFunctionExtId).append(" , ");
            SQLBuf.append(colCreateUsrId).append(" , ");
            SQLBuf.append(colCreateTimestamp).append(" ) ");
            SQLBuf.append(" Values ");
            if(rol_ext_id.equals(ALL_ROLES)) {
                SQLBuf.append("(null,");
            }
            else {
                SQLBuf.append("(?,");
            }
            SQLBuf.append("?, ?, ?)");
            
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            if(!rol_ext_id.equals(ALL_ROLES)) {
                stmt.setString(index++, rol_ext_id);
            }
            stmt.setString(index++, ftn_ext_id);
            stmt.setString(index++, create_usr_id);
            stmt.setTimestamp(index++, create_timestamp);
            stmt.executeUpdate();
            stmt.close();
        }
        return;
    }

    /**
    look up database to see if a access control record exist
    @param ent_id set it to ALL_ENTITIES means "for all entities"
    @param rol_ext_id set it to ALL_ROLES means "for all roles"
    @return true if record, false if not
    @exception database access error
    */
    protected boolean isRecordExist(String rol_ext_id,
                                    String ftn_ext_id) throws SQLException {
        
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(512);
        
        SQLBuf.append("Select ").append(colRoleExtId).append(" From ").append(dbTableName);
        SQLBuf.append(" Where ").append(colFunctionExtId).append(" = ? ");
        if(!rol_ext_id.equals(ALL_ROLES)) { 
            SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
        }
        else {
            SQLBuf.append(" And ").append(colRoleExtId).append(" is null ");
        }
        
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        stmt.setString(index++, ftn_ext_id);
        if(!rol_ext_id.equals(ALL_ROLES)) { 
            stmt.setString(index++, rol_ext_id);
        }
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
    delete records from access control table<BR>
    @param rol_ext_id use ALL_ROLES to indicate whatever rol_ext_id is
    @param ftn_ext_id use ALL_FUNCTION to indicate whatever ftn_ext_id is
    @exception SQLException database access error
    */
    public void rmPrivilege(String rol_ext_id, 
                                String ftn_ext_id) throws SQLException {
    
        StringBuffer SQLBuf = new StringBuffer(2048);
        
        if(rol_ext_id == null) {
            rol_ext_id = ALL_ROLES;
        }
        if(ftn_ext_id == null) {
            ftn_ext_id = ALL_FUNCTIONS;
        }
        
        SQLBuf.append("Delete From ").append(dbTableName);
        SQLBuf.append(" Where 1=1 ");
        if(!rol_ext_id.equals(ALL_ROLES)) {
            SQLBuf.append("And ").append(colRoleExtId).append(" = ? ");
        }
        if(!ftn_ext_id.equals(ALL_FUNCTIONS)) {
            SQLBuf.append("And ").append(colFunctionExtId).append(" = ? ");
        }
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        int index = 1;
        if(!rol_ext_id.equals(ALL_ROLES)) {
            stmt.setString(index++, rol_ext_id);
        }
        if(!ftn_ext_id.equals(ALL_FUNCTIONS)) {
            stmt.setString(index++, ftn_ext_id);
        }
        stmt.executeUpdate();
        stmt.close();
        return;
    }
}
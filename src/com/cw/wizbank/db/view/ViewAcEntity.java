package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;

/**
Database Layer Instance Access Control on Resources<BR>
Access Control Table: acResources<BR>
*/
public class ViewAcEntity extends ViewAcInstance {
    
    /**
    ftn_type of this view
    */
    private static final String FtnType = "ENTITY";
    
    /**
    all the thing need to do in the constructor is to initialize a set of super class variables<BR>
    see ViewAcInstance for details
    */
    public ViewAcEntity(Connection con) {
        super(con);
        
        //access control table name
        dbTableName = "acEntity";
        
        //instance id column name of the access control table
        colInstanceId = "ac_ent_id";

        //entity id column name of the access control table
        colEntityId = "ac_ent_ent_id";
        
        //role ext id column name of the access control table
        colRoleExtId = "ac_ent_rol_ext_id";
        
        //function ext id column of the access control table
        colFunctionExtId = "ac_ent_ftn_ext_id";
        
        //owner indicator column name of the access control table
        colOwnerInd = "ac_ent_owner_ind";
        
        //create user id column name of the access control table
        colCreateUsrId = "ac_ent_create_usr_id";
        
        //create timestamp column name of the access control table
        colCreateTimestamp = "ac_ent_create_timestamp";
        
        //funtion types of this instance will have
        ftn_types.addElement(FtnType);
    }    

    /**
    get the ent_id of those users who can access the instance(entity) because of the user 
    can access the instance's ancestor
    @param rol_ext_id may pass ALL_ROLES to indicate not to construct Where rol_ext_id = '..' in SQL
    @return a long array of ent_id
    */
    public long[] getInheritedEntity(long instance_id, String rol_ext_id, String ftn_ext_id) 
        throws SQLException {
        try {
            Vector v = new Vector();
            StringBuffer SQLBuf = new StringBuffer(256);
            
            //get the ancestor user groups
            Vector v_ancestors = dbUserGroup.traceParentID(con, instance_id);
            String parentEntIdSQLList = dbUtils.vec2list(v_ancestors);

            SQLBuf.append(" Select ").append(colEntityId).append(" From ").append(dbTableName);
            SQLBuf.append(" Where ").append(colInstanceId).append(" in ").append(parentEntIdSQLList);
            if(!rol_ext_id.equals(ALL_ROLES)) {
                SQLBuf.append(" And ").append(colRoleExtId).append(" = ? ");
            }
            SQLBuf.append(" And ").append(colFunctionExtId).append(" = ? ");
                
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
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
        catch(qdbException qdbe) {
            throw new SQLException(qdbe.getMessage());
        }
    }
}
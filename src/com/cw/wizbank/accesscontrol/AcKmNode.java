package com.cw.wizbank.accesscontrol;

import java.sql.*;
import com.cw.wizbank.db.DbKmNodeAccess;
import com.cw.wizbank.db.DbKmNodeAssignment;
import com.cw.wizbank.db.DbKmNode;

/**
Logical Layer Access Control on Catalog<BR>
*/
public class AcKmNode extends AcInstance {

    /**
    function ext id, Knowledge Managament Maintainence
    */
//    public static final String FTN_KM_MGT = "KM_MGT";
//    public static final String FTN_KB_MGT = "KB_MGT";
//    public static final String FTN_KB_MGT_ADMIN = "KB_MGT_ADMIN";
    
    public String usrGroupsList = null; 
    public long usr_ent_id;
    
    /**
    Contructor
    @param usr_ent_id_ entity id of the user
    @param usrGroups_ ancestor ids of the user
    */
    public AcKmNode(Connection con, long usr_ent_id_, String usrGroupsList_) {
        super(con);
        usr_ent_id = usr_ent_id_;
        usrGroupsList = usrGroupsList_;
    }

    /**
    check if the user has privilege to check in object base on
    if the object is checked out by an given user
    */
    public boolean isCheckedOutByUser(long node_id) throws SQLException {
        
        return DbKmNodeAccess.isCheckedOutByUser(con, node_id, this.usr_ent_id);
    }
    
    public boolean isLibraryType(long node_id) throws SQLException {
        
        return DbKmNodeAccess.isLibraryType(con, node_id);
    }
    
    /**
    check if the user has privilege to check in/check out/publish an object 
    base on the user's privilege parent workfloder
    */
    public boolean hasObjMgtPrivilege(long objId) throws SQLException {
        return hasInsPrivilege(DbKmNode.getParentID(con, objId), null);
    }
    
    /**
    check if the user has privilege to read an object 
    base on the user's privilege parent workfolder
    */
    public boolean hasObjReadPrivilege(long objId) throws SQLException {
        return hasReadPrivilege(DbKmNode.getParentID(con, objId));
    }

    /**
    check if the user has privilege to read an object 
    base on the user's privilege parent folder id
    */
    public boolean hasObjReadPrivilege(long objId, long folderId) throws SQLException {
        return hasReadPrivilege((folderId > 0) ? folderId : DbKmNode.getParentID(con, objId));
    }
    
    /**
    check if the user has the privilege to create new folder/sub-folder/objects under a node
    */
    public boolean hasInsPrivilege(long node_id, String rol_ext_id) throws SQLException {
        
        // If create folder at the top level, check by RoleFuction
        if (node_id > 0) {
            return DbKmNodeAccess.hasAddNodeRight(this.con, node_id, usrGroupsList);
        }else {
            return true;
        }
    }

    /**
    check if the user has the privilege to modify a node / modify object 
    */
    public boolean hasMgtPrivilege(long node_id) throws SQLException {
        
        return DbKmNodeAccess.hasEditNodeRight(this.con, node_id, usrGroupsList);
    }

    /**
    check if the user has privilege to read a a node
    */
    public boolean hasReadPrivilege(long node_id) throws SQLException {

        return DbKmNodeAccess.hasReadNodeRight(this.con, node_id, usrGroupsList);
    }
    
    
    public boolean existedInWorkplace(long node_id) throws SQLException {
        
        return DbKmNodeAssignment.existedInWorkplace(this.con, node_id, this.usr_ent_id, usrGroupsList);
    }
    
    
}
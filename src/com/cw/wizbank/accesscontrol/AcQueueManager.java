package com.cw.wizbank.accesscontrol;

import java.sql.*;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Queue Manager <BR>
*/
public class AcQueueManager {

    /**
    function ext id, offline catalog/tree node read
    */
    public static final String FTN_QM_ADMIN = "QM_ADMIN";

    private Connection con;
    
    private AccessControlWZB acl;

    public AcQueueManager(Connection con) {
        setCon(con);
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
    public Connection getCon() {
        return this.con;
    }

    /**
    check if the user, role pair has the privilege to read offline resources
    */
    public boolean hasAdminPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        return AccessControlWZB.hasRolePrivilege( rol_ext_id, new String []{AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION,AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION}) ;
    }
}
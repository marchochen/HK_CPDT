package com.cw.wizbank.accesscontrol;

import java.sql.*;

import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Syllabus<BR>
*/
public class AcSyllabus extends AcInstance {

    /**
    function ext id, ins/upd/del of syllabus
    */
   // public static final String FTN_SYB_MGT = "SYB_MGT";

    public AcSyllabus(Connection con) {
        super(con);
        //view = new ViewAcResources(con); 
    }

    /**
    check if the user, role pair has the privilege to insert syllabus
    */
    public boolean hasInsPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
    	return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN) ;
    }

    /**
    check if the user, role pair has the privilege to update syllabus
    */
    public boolean hasUpdPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
        return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN) ;
    }

    /**
    check if the user, role pair has the privilege to delete syllabus
    */
    public boolean hasDelPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
    	return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN) ;
    }
}
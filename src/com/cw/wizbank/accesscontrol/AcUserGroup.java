package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Catalog<BR>
*/
public class AcUserGroup extends AcEntity {


    
    public AcUserGroup(Connection con) {
        super(con);
    }

    /**
    */
    public boolean hasManagePrivilege(String current_role) 
        throws SQLException {
        return AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_USR_INFO);
    }
    
    public boolean canManageGroup(loginProfile prof, long usg_ent_id, boolean tcEnable) throws SQLException {
    	boolean result = false;
    	AccessControlWZB acl = new AccessControlWZB();
        if(!tcEnable ||(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_USR_INFO) && !AccessControlWZB.isRoleTcInd(prof.current_role))) {
        	result = true;
        } else if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_USR_INFO)){
        	Vector vec = dbUserGroup.getAllTargetGroupIdForOfficer(con, prof.usr_ent_id);
        	if(ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role)) {
        		vec.add(new Long(prof.root_ent_id));
        	}
        	if(vec.contains(new Long(prof.root_ent_id)) || vec.contains(new Long(usg_ent_id))) {
        		result = true;
        	}
        }
        return result;
    }
    
    public boolean canMaitainUsgInfo (long usr_ent_id, String current_role, long root_ent_id, long usg_ent_id, boolean tcEnable) throws SQLException {
    	boolean result = false;
    	AccessControlWZB acl = new AccessControlWZB();
        if(!tcEnable || (AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_USR_INFO) && !AccessControlWZB.isRoleTcInd(current_role))) {
        	result = true;
        } else if(AccessControlWZB.hasRolePrivilege(current_role, AclFunction.FTN_AMD_USR_INFO)){
    		Vector vec = dbUserGroup.getAllowMaitainUsgByEntId(con, usr_ent_id, root_ent_id);
        	if(ViewTrainingCenter.isSuperTA(con, root_ent_id, usr_ent_id, current_role)) {
        		vec.add(new Long(root_ent_id));
        	}
        	if(vec.contains(new Long(root_ent_id)) || vec.contains(new Long(usg_ent_id))) {
        		result = true;
        	}
        }
    	return result;
    }
}
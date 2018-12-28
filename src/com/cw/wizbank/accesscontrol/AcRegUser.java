package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Catalog<BR>
*/
public class AcRegUser extends AcEntity {

/*    public static final String FTN_USR_MGT_NON_ADMIN = "USR_MGT_NON_ADMIN";
    public static final String FTN_USR_MGT_ADMIN = "USR_MGT_ADMIN";
    public static final String FTN_USR_MGT_OWN = "USR_MGT_OWN";
    public static final String FTN_USR_REACTIVATE = "USR_REACTIVATE";
	public static final String FTN_USR_MGT_LOWER_ROLE = "USR_MGT_LOWER_ROLE";
	public static final String FTN_USR_MGT_SAME_AND_LOWER_ROLE = "USR_MGT_SAME_AND_LOWER_ROLE";
    public static final String FTN_USR_IMPORT = "USR_IMPORT";
    public static final String FTN_USR_MGT_GRADE = "USR_MGT_GRADE";*/
    
//    public static final String FTN_USR_MGT_NON_ADMIN = "USR_INFO_MAIN";
//    public static final String FTN_USR_MGT_ADMIN = "USR_INFO_MAIN";
//    public static final String FTN_USR_MGT_OWN = "USR_INFO_MAIN";
//    public static final String FTN_USR_REACTIVATE = "USR_INFO_MAIN";
//	public static final String FTN_USR_MGT_LOWER_ROLE = "USR_INFO_MAIN";
//	public static final String FTN_USR_MGT_SAME_AND_LOWER_ROLE = "USR_INFO_MAIN";//USR_MGT_SAME_AND_LOWER_ROLE
//    public static final String FTN_USR_IMPORT = "USR_INFO_MAIN";
//    public static final String FTN_USR_MGT_GRADE = "USR_INFO_MAIN";
    
    public AcRegUser(Connection con) {
        super(con);
        //view = new ViewAcResources(con); 
    }

    /**
    check if the user, role pair can re-activate suspended user account
    */
    public boolean hasReactivePrivilege(long ent_id, String rol_ext_id) 
        throws SQLException {
       
        return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_USR_INFO) ;
    }
    
    /**
    check if the user, role pair can approve PENDING accounts
    */
    public boolean hasApprovalPrivilege(loginProfile prof, long target_ent_id, boolean tcEnable) 
        throws SQLException {
        boolean hasRight = AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_USR_INFO) ;
        boolean isRoleTcInd = AccessControlWZB.isRoleTcInd(prof.current_role);
        if(hasRight && isRoleTcInd) {
            if(hasRight) {
           		hasRight = canMgtUser(prof, target_ent_id, tcEnable);
            }
        }
        return hasRight;
    }

    /**
    check if the user, role pair can insert an user with the input usr_roles
    */
    public boolean hasInsPrivilege(long ent_id, long root_ent_id, String rol_ext_id, String[] usr_roles) 
        throws SQLException {
        
			boolean hasRight = false;
			try{
				AccessControlManager acMgr = new AccessControlManager();
				if(acl == null) {
					acl = new AccessControlWZB();
				}
				AccessControlWZB acl = new AccessControlWZB();
				long auth_level = acMgr.getRoleAuthLevel(con, root_ent_id, rol_ext_id);
				long target_auth_level = acl.getRoleMinAuthLevel(con, root_ent_id, usr_roles);
				
				if( (hasSameAndLowerRoleUsrMgt(ent_id, rol_ext_id) && (auth_level <= target_auth_level) )
					|| ( hasLowerRoleUsrMgt(ent_id, rol_ext_id) && (auth_level < target_auth_level) ) ) {
						hasRight = true;
					}
			}catch(cwSysMessage e){
				hasRight = false;
			}
						
			return hasRight;
        }    
        
    public boolean hasDelPrivilege(long ent_id, long root_ent_id, String rol_ext_id, long target_ent_id) 
        throws SQLException {
    		if(rol_ext_id.startsWith("ADM"))
    			return true;
			boolean hasRight = false;
			try{
				AccessControlManager acMgr = new AccessControlManager();
				long auth_level = acMgr.getRoleAuthLevel(con, root_ent_id, rol_ext_id);
				long target_auth_level = dbRegUser.getUserRoleMinAuthLevel(con, target_ent_id);
				
				if((hasSameAndLowerRoleUsrMgt(ent_id, rol_ext_id) && auth_level <= target_auth_level) || ( hasLowerRoleUsrMgt(ent_id, rol_ext_id) && auth_level < target_auth_level)) {
					hasRight = true;
				}
			}catch(cwSysMessage e){
				hasRight = false;
			}
						
			return hasRight;
			
    }

    public boolean hasTrashPrivilege(long ent_id, long root_ent_id, String rol_ext_id, long target_ent_id) 
        throws SQLException{
        	
			return hasDelPrivilege(ent_id, root_ent_id, rol_ext_id, target_ent_id);
			
    }

    public boolean hasUpdPrivilege(long ent_id, long root_ent_id, String rol_ext_id, long target_ent_id, String[] usr_roles)
        throws SQLException{
            
            boolean hasRight = false;
		
            if ( ent_id == target_ent_id ) {
				return true;
            }

			try{
				AccessControlManager acMgr = new AccessControlManager();
				if(acl == null) {
					acl = new AccessControlWZB();
				}					
				long auth_level = acMgr.getRoleAuthLevel(con, root_ent_id, rol_ext_id);
				long target_auth_level = dbRegUser.getUserRoleMinAuthLevel(con, target_ent_id);
				
				//If user not update user role, by-pass checking the new role auth. level
				long target_new_auth_level = Long.MAX_VALUE;
				if( usr_roles != null && usr_roles.length > 0 ) {
					target_new_auth_level = acl.getRoleMinAuthLevel(con, root_ent_id, usr_roles); 
				}

				if( ( auth_level <= target_auth_level && auth_level <= target_new_auth_level )
				|| (  auth_level < target_auth_level && auth_level < target_new_auth_level ) ) {
					hasRight = true;
				}
			}catch(cwSysMessage e){
				hasRight = false;
			}
            
			return hasRight;
        }
        
        public boolean hasAssignRolePrivilege(long ent_id, String rol_ext_id, long target_ent_id, String[] usr_roles)
            throws SQLException{
            return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_USR_INFO);
        }
       
  
 
		public boolean hasLowerRoleUsrMgt(long ent_id, String rol_ext_id)
			throws SQLException {
				return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_USR_INFO);
			}
 
		public boolean hasSameAndLowerRoleUsrMgt(long ent_id, String rol_ext_id)
			throws SQLException {
				
				return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_USR_INFO);
			}
		
	
	    
	    public boolean canMgtUser(loginProfile prof, long target_ent_id, boolean tcEnable) throws SQLException {
	    	boolean result = false;
	    	boolean hasRight = AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_USR_INFO) ;
	        boolean isRoleTcInd = AccessControlWZB.isRoleTcInd(prof.current_role);
	        
	        boolean isAdmin = false;
	      //判断是否是admin
			if(prof.usr_status != null && prof.usr_status.equals(dbRegUser.USR_STATUS_SYS) && prof.usr_ste_usr_id.equals("admin")){
				isAdmin = true;
			}
	        
	        if(!tcEnable ||(hasRight && !isRoleTcInd) || (WizbiniLoader.getInstance().cfgSysSetupadv.isTcIndependent() && isAdmin)
//	        		|| prof.usr_ent_id == target_ent_id
	        		) {
	        	result = true;
	        } else if(hasRight){
	        	dbEntityRelation dbEr = new dbEntityRelation();
	        	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
	        	dbEr.ern_child_ent_id = target_ent_id;
	        	Vector vtParentRecord = dbEr.getLatestRecord(con);
	        	if(vtParentRecord != null && vtParentRecord.size() > 0) {
        			for(int i = 0; i < vtParentRecord.size(); i++){
	        			long usg_ent_id = ((dbEntityRelation)vtParentRecord.elementAt(i)).ern_ancestor_ent_id;
	        			AcUserGroup acUsg = new AcUserGroup(con);
		        		result = acUsg.canManageGroup(prof, usg_ent_id, tcEnable);
		        		if(result){
		        			break;
		        		}
        			}
        		}
	        	
	        	
	        }
            return result;
	    }

	    public boolean canMaitainUsrInfo (long usr_ent_id, String current_role, long root_ent_id, long target_ent_id, boolean tcEnable) throws SQLException {
	    	boolean result = false;
	    	boolean hasRight = AccessControlWZB.hasRolePrivilege( current_role,AclFunction.FTN_AMD_USR_INFO) ;
	        boolean isRoleTcInd = AccessControlWZB.isRoleTcInd(current_role);
	        
	        
	        if(!tcEnable || (hasRight && !isRoleTcInd)) {
	        	result = true;
	        } else if(hasRight){
	        	dbEntityRelation dbEr = new dbEntityRelation();
	        	dbEr.ern_type = dbEntityRelation.ERN_TYPE_USR_PARENT_USG;
	        	dbEr.ern_child_ent_id = target_ent_id;
	        	Vector vtParentRecord = dbEr.getLatestRecord(con);
        		if(vtParentRecord != null && vtParentRecord.size() > 0) {
        			Vector vec = dbUserGroup.getAllowMaitainUsgByEntId(con, usr_ent_id, root_ent_id);
        			for(int i = 0; i < vtParentRecord.size(); i++){
	        			long usg_ent_id = ((dbEntityRelation)vtParentRecord.elementAt(i)).ern_ancestor_ent_id;
	        			if(vec.contains(new Long(usg_ent_id))) {
	        				result = true;
	        				break;
	        			}
        			}
        		}
	        }
	    	return result;
	    }

}
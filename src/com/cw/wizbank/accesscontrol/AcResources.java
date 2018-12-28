package com.cw.wizbank.accesscontrol;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.db.view.ViewAcResources;
import com.cw.wizbank.db.view.ViewObjectiveAccess;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Resources<BR>
*/
public class AcResources extends AcInstance {

    
    public AcResources(Connection con) {
        super(con);
        view = new ViewAcResources(con); 
    }

    

    /**
    check if the role has read permission for inserting ResourcePermission
    */
    public boolean hasResPermissionRead(String rol_ext_id) throws SQLException {
    
        return true;
    }

    /**
    check if the role has write permission for inserting ResourcePermission
    */
    public boolean hasResPermissionWrite(String rol_ext_id) throws SQLException {
    
        return true;
    }

    /**
    check if the role has execute permission for inserting ResourcePermission
    */
    public boolean hasResPermissionExec(String rol_ext_id) throws SQLException {
        return true;
    }
    
  
    
    public boolean checkResPermission(loginProfile prof, String res_ids[]) throws cwSysMessage, SQLException{
        boolean hasRight = true;

        //check resources permission
        for(int i=0;i<res_ids.length;i++) {
            long res_id = Long.parseLong(res_ids[i]);
            if (!checkResPermission(prof, res_id)){
                hasRight = false;
                break;
            }
        }
        return hasRight;
    }

    
        // Check wether a user has right to modify a resource
    // Case 1 : User has "write" permission in the resource permission table
    // Case 2 : Expert Admin, Admin, Expert Course Designers have permission on the modify all others public resources
    public boolean checkResPermission(loginProfile prof, long res_id) 
        throws cwSysMessage, SQLException
    {
            try {
                boolean hasRight = false;

                // User has write permission on the resoure
                if (dbResourcePermission.hasPermission(con, res_id, prof,
                                                    dbResourcePermission.RIGHT_WRITE))
                {
                        hasRight = true;
                }else {
                    String resPriv = dbResource.getResPrivilege(con, res_id);
                    String resOwnerId = dbResource.getResUsrIdOwner(con, res_id);

                    if (resPriv.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
                        long rootId = dbRegUser.getRootGpId(con, resOwnerId); 
                        if (rootId == prof.root_ent_id) // Same Organization
                        {
                            AccessControlWZB acl = new AccessControlWZB();
                            if(  AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_RES_MAIN) )
                                hasRight = true;
                        }
                    }
                }
                return hasRight;
                
            }catch (qdbException e) {
                throw new SQLException("SQL Error : " + e.getMessage());
            }
    }



    /**
    check if can read a resource
    @param res_id must be provided
    @param ent_id must be provided
    @parma rol_ext_id must be provided
    @param v_ancestors pass FIND_OUT_ANCESTORS to ask it to find out ancestors itself
    @return true if <BR>
    <ul>
    <li>has RES_READ on the resource
    </ul>
    */
    public boolean hasReadPrivilege(long res_id, long ent_id, 
                                        String rol_ext_id, Vector v_ancestors) 
                                        throws SQLException {
        
        return true;
    }

    /**
    check if the user, role pair has the privilege to read offline resources
    */
    public boolean hasOffReadPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
        return true;
    }

    
    
	public boolean hasManagePrivilege(long ent_id,long obj_id,String rol_ext_id) throws SQLException{
		AcObjective acObj = new AcObjective(con);
		return acObj.hasManagePrivilege(ent_id,obj_id,rol_ext_id);
	}
    
	public boolean hasResPrivilege(long ent_id,long res_id,String rol_ext_id) throws SQLException{
			AcObjective acObj = new AcObjective(con);
		    //DbObjective obj = new DbObjective();
		    long rootObjId = dbObjective.getResObjRootId(con,res_id);
            boolean result = false;
            if(rootObjId > 0) {
                result = acObj.hasManagePrivilege(ent_id,rootObjId,rol_ext_id);
            } else {
                result = false;
            }
            return result;
		}
        /*
		 * get the access of the resource
		 */
		public String getResAccess(Connection con,long resId,long entId) throws SQLException{
			String access = "";
			ViewObjectiveAccess doa =new ViewObjectiveAccess();
			//DbObjective obj = new DbObjective();
			long rootObjId = dbObjective.getResObjRootId(con,resId);
			access = doa.getRootObjAccess(con,rootObjId,entId);
			return access;
		}

}
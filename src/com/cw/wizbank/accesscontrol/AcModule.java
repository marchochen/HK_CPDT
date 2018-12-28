package com.cw.wizbank.accesscontrol;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.view.ViewAcModule;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.qdbException;

import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.AclFunction;


/**
Logical Layer Access Control on Module<BR>
*/
public class AcModule extends AcResources {

   

    public AcModule(Connection con) {
        super(con);
        view = new ViewAcModule(con);
    }

    /**
    check if the user, role pair has the privilege to read offline course
    */
    public boolean hasOffReadPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
        return true;
    }

    /**
    check if the user, role pair has insert privilege<BR>
    @param cos_res_id must be provided
    @param ent_id must be provided
    @param rol_ext_id must br provided
    @param v_ancestors pass FIND_OUT_ANCESTORS to ask it to find out ancestors itself
    @exception SQLException database access error
    @return true if <BR>
    <ul>
    <li>has functional privilege RES_NEW and
    <li>has COS_CONTENT_MGT on the parent course
    </ul>
    */
    public boolean hasInsPrivilege(long cos_res_id, long ent_id, 
                                    String rol_ext_id, Vector v_ancestors) 
                                    throws SQLException {
   
        return true;
    }
    
    /**
    check if can read a module
    @param v_ancestors pass FIND_OUT_ANCESTORS to ask the function to find ancestors itself
    @return true if<BR>
    <ul>
    <li>has instance privilege RES_READ on parent course
    </ul>
    */
    public boolean hasReadPrivilege(long mod_res_id, long ent_id, 
                                    String rol_ext_id, Vector v_ancestors) 
                                    throws SQLException {
        
    	return true;
    }
    
   // Check wether a user has right to modify a course according to ACL
   // Case 1 : User has write permission in the resource permission
   // Case 2 : User has COS_MGT_IN_ORG right in ACL
   public boolean checkModifyPermission(loginProfile prof, long res_id) 
        throws SQLException, cwSysMessage
   {
        try{
            boolean hasRight = false;
            dbModule mod = new dbModule();
            mod.mod_res_id = res_id;
            mod.res_id = res_id;
            mod.get(con);
            if (mod.mod_is_public){
                hasRight =  AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_RES_MAIN) ;    
            }else{
                hasRight = AccessControlWZB.hasRolePrivilege( prof.current_role, new String [] {AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT,AclFunction.FTN_AMD_EXAM_MAIN_CONTENT,AclFunction.FTN_AMD_TEACHING_COURSE_LIST});
            }

            return hasRight;
            
        }catch(qdbException g){
            throw new SQLException(g.getMessage());    
        } 
   }

   
    public boolean checkReadPermission(loginProfile prof, long res_id) 
   {
        return true;
   }


 
  

}
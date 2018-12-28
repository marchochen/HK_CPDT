package com.cw.wizbank.accesscontrol;

import java.sql.*;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeCatalogAccess;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Catalog<BR>
*/
public class AcCatalog extends AcInstance {

   


    public AcCatalog(Connection con) {
        super(con);
        //view = new ViewAcResources(con); 
    }

    /**
    check if the user, role pair has the privilege to create catalog
    */
    public boolean hasInsPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
        return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_CAT_MAIN);
    }

    /**
    check if the user, role pair has the privilege to create global catalog
    */
    public boolean hasGlbInsPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
        return  AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_CAT_MAIN);
    }

    
   /**
    check if the user, role pair has the privilege to read offline catalog
    */
    public boolean hasOffReadPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
    	 return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_CAT_MAIN);
    }

    /**
    check if the user has privilege to read a catalog
    @param ent_ids usr_ent_id + ancestors ent_id
    */
    public boolean hasReadPrivilege(long cat_id, long ent_id, String rol_ext_id, 
                                    long[] ent_ids) throws SQLException, cwSysMessage {

        boolean hasRight;
        if(!hasOffReadPrivilege(ent_id, rol_ext_id)) {
            
            if(aeCatalog.isCatOff(con, cat_id))
                hasRight = false;
            else
                hasRight = true;
        }
        else
            hasRight = true;
                
        if(hasRight) {
            hasRight = (aeCatalog.isPublic(this.con, cat_id) ||
                        aeCatalogAccess.hasAccessRight(this.con, cat_id, ent_ids));
        }
        return hasRight;
    }

    /**
    check if has update catalog privilege<BR>
    @param owner_ent_id root entity id of the user who wants to update the catalog
    */
    public boolean hasUpdPrivilege(long cat_id, long ent_id, String rol_ext_id, long owner_ent_id) throws SQLException, cwSysMessage, qdbException {
        return AccessControlWZB.hasRolePrivilege( rol_ext_id,AclFunction.FTN_AMD_CAT_MAIN) ;
    }
    
}
package com.cw.wizbank.accesscontrol;

import java.sql.*;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.util.cwSysMessage;

/**
Logical Layer Access Control on Catalog<BR>
*/
public class AcTreeNode extends AcInstance {

    private AcCatalog accat;
    
    public AcTreeNode(Connection con) {
        super(con);
        this.accat = new AcCatalog(con);
        //view = new ViewAcResources(con); 
    }

    /**
    check if the user, role pair has the privilege to read offline resources
    */
    public boolean hasOffReadPrivilege(long ent_id, String rol_ext_id) throws SQLException {
        
        return accat.hasOffReadPrivilege(ent_id, rol_ext_id);
    }

    /**
    check if the user has privilege to read a tree node
    @param ent_ids usr_ent_id + ancestors ent_id
    */
    public boolean hasReadPrivilege(long ent_id, String rol_ext_id, 
                                    long tnd_id, long tnd_cat_id, long[] ent_ids) 
                                    throws SQLException, cwSysMessage {
        
        if(!accat.hasOffReadPrivilege(ent_id, rol_ext_id)) {
            
            if(aeCatalog.isCatOff(con, tnd_cat_id))
                return false;
            if(aeTreeNode.isNodeOff(con, tnd_id))
                throw new cwSysMessage("AECA04");
        }
        return (accat.hasReadPrivilege(tnd_cat_id, ent_id, rol_ext_id, ent_ids));
    }

    /**
    check if has update tree node privilege<BR>
    @param tnd_id catalog id of the tree node
    @param owner_ent_id root entity id of the user who wants to update the catalog
    */
    public boolean hasUpdPrivilege(long tnd_id, long ent_id, String rol_ext_id, long owner_ent_id) 
        throws SQLException, cwSysMessage, qdbException {
        
        aeTreeNode tnd = new aeTreeNode();
        tnd.tnd_id = tnd_id;
        tnd.tnd_cat_id = tnd.getCatalogId(con);

        AcCatalog accat = new AcCatalog(con);
        return accat.hasUpdPrivilege(tnd.tnd_cat_id, ent_id, rol_ext_id, owner_ent_id);
    }

    /**
    check if has insert tree node privilege<BR>
    @param tnd_id catalog id of the tree node
    @param owner_ent_id root entity id of the user who wants to update the catalog
    */
    public boolean hasInsPrivilege(long tnd_parent_tnd_id, long ent_id, String rol_ext_id, long owner_ent_id) 
        throws SQLException, cwSysMessage, qdbException  {
        
        if(tnd_parent_tnd_id != 0) {
            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = tnd_parent_tnd_id;
            parentNode.tnd_cat_id = parentNode.getCatalogId(con);

            AcCatalog accat = new AcCatalog(con);
            return accat.hasUpdPrivilege(parentNode.tnd_cat_id, ent_id, rol_ext_id, owner_ent_id);
        }
        else {
            return true;
        }
    }
}
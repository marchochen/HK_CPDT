package com.cw.wizbank.enterprise;

//import standard java classes
import java.lang.Long;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.*;

//import generated IMS classes

//import cyberwisdom classes
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.aeCatalogAccess;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.ae.aeTreeNodeRelation;
import com.cw.wizbank.ae.db.DbCatalogItemType;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

import com.cw.wizbank.db.DbEntityRelation;
import com.cw.wizbank.db.DbRegUser;
import com.cw.wizbank.db.DbEntity;
import com.cw.wizbank.db.DbUserGroup;
import com.cw.wizbank.db.DbRoleTargetEntity;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.accesscontrol.AcRegUser;
import com.cw.wizbank.accesscontrol.AccessControlCore;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cwn.wizbank.security.AclFunction;

/**
IMSCleanUp works as a sweeper to clean up not-in-syn records in wizBank
after import.
*/
public class IMSCleanUp{

    private Timestamp synTimestamp;

    /**
    Construct an IMSCleanUp with the given synTimestamp.
    @param synTimestamp Start syn time of the application.
    */
    public IMSCleanUp(Timestamp synTimestamp) {
        this.synTimestamp = synTimestamp;
    }

    /**
    Clean up not-in-syn aeItem from wizBank.
    @param con Connection to database
    @param wbItemType wizBank's item type to be cleaned up
    @param wbProfile wizBank's loginProfile describe person to make this action
    @param isDeleteItem, otherwise only turn the item offline 
    */
    public void cleanUpItem(Connection con, loginProfile wbProfile, String wbItemType[], String action)
        throws SQLException, cwException, cwSysMessage, qdbException {
        for (int i=0; i<wbItemType.length; i++){
            //Get not-in-syn Items and update timestamp
            // include session and run
            Hashtable h_itemIdNUpdTimestamp =
                aeItem.getNotInSynItemIdNUpdTimestamp(con, this.synTimestamp,
                                                    wbProfile.root_ent_id, wbItemType[i], null, null);

            //For each item, update itm_status to aeItem.ITM_STATUS_OFF
            Enumeration enumeration = h_itemIdNUpdTimestamp.keys();
            while(enumeration!=null && enumeration.hasMoreElements()) {
                aeItem itm = new aeItem();
                Long key = (Long)enumeration.nextElement();
                itm.itm_id = key.longValue();
                itm.itm_upd_timestamp = (Timestamp)h_itemIdNUpdTimestamp.get(key);
                try {
                    if (action != null && action.equalsIgnoreCase("DELETE")){
                        itm.getItemType(con);
                        if(itm.getQdbInd(con)) {
                            dbCourse cos = new dbCourse();
                            cos.cos_itm_id = itm.itm_id;
                            itm.delWZBCourse(con, wbProfile, cos, itm.itm_upd_timestamp);
                        }else {
                            itm.delItem(con, wbProfile.root_ent_id, itm.itm_upd_timestamp, wbProfile);
                        }
                    }else{
                        itm.updItemStatus(con, itm.itm_id, aeItem.ITM_STATUS_OFF, itm.itm_upd_timestamp, wbProfile.root_ent_id, wbProfile.usr_id, cwSQL.getTime(con));
                        //detach aeItem from tree nodes
                        itm.attachTreeNodes(con, new long[0], wbProfile.usr_id, wbProfile.root_ent_id);
                    }
                    con.commit();
                    IMSUtils.writeLog(IMSUtils.CLEAN_FILE, "Item " + itm.itm_id + " CLEANED.");
                }catch(cwSysMessage syse){
                    try{
                        con.rollback();
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "Item " + itm.itm_id + " CANNOT BE DELETED", new cwException(syse.getSystemMessage(wbProfile.label_lan)));
                    }catch(SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup item: itm_id = " + itm.itm_id, sqle);
                    }
                }catch(Exception e){
                    try{
                        con.rollback();
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "Item " + itm.itm_id + " CANNOT BE DELETED", e);
                    }catch(SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup item: itm_id = " + itm.itm_id, sqle);
                    }
                }
            }
        }
        return;
    }

    /**
    Clean up not-in-syn aeItemAccess from wizBank.
    NOTE: it does not take in wizBank's loginProfile nor wizBank's site id
    as the site id information already implies from the wizBanks's role id
    @param con Connection to database
    @param wbProfile wizBank's loginProfile describe person to make this action
    @param iacRoleMap role map to decide which wizBank's rol_id to be deleted.
    */
    public void cleanUpItemAccess(Connection con, loginProfile wbProfile, String[] wbItemType)
        throws SQLException, cwException {
        for (int k=0; k<wbItemType.length; k++){                        
            //parse roleMap into Hashtable
    //        Hashtable h_roleMap = IMSItemAccess.parseRoleMap(iacRoleMap);
            //get wizBank's role id as Vector
    //        Vector v_wbRoleId = IMSUtils.hashValuesAsVector(h_roleMap);
            //get not-in-syn aeItemAccess
            Vector v_wbIac = aeItemAccess.getNotInSynIac(con, this.synTimestamp, wbItemType[k], wbProfile.root_ent_id, new Boolean(false),
                                                    null,
                                                    aeItemAccess.ACCESS_TYPE_ROLE);
            //delete those not-in-syn aeItemAccess
            for(int i=0; i<v_wbIac.size(); i++) {
                aeItemAccess wbIac = (aeItemAccess)v_wbIac.elementAt(i);
                try {
                    wbIac.del(con);
                    con.commit();
                    IMSUtils.writeLog(IMSUtils.CLEAN_FILE, "ItemAccess (" + wbIac.iac_itm_id + " , " + wbIac.iac_ent_id + "," + wbIac.iac_access_id + ") CLEANED.");
                } 
                catch(Exception e) {
                    try{
                        con.rollback();
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "ItemAccess (" + wbIac.iac_itm_id + " , " + wbIac.iac_ent_id + "," + wbIac.iac_access_id + ") CANNOT BE DELETED", e);
                    }catch(SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup itemAccess (" + wbIac.iac_itm_id + " , " + wbIac.iac_ent_id + "," + wbIac.iac_access_id + ")", sqle);
                    }
                }
            }
        }            
        // no need to clean instructor and trainer role of the user
        return;
    }

    /**
    Clean up not-in-syn aeApplication from wizBank.
    @param con Connection to database
    @param wbProfile wizBank's loginProfile describe person to make this action
    */
    public void cleanUpApplication(Connection con, loginProfile wbProfile, String[] wbItemType)
        throws SQLException, cwException {
        for (int k=0; k<wbItemType.length; k++){
            aeQueueManager qman = new aeQueueManager();
            //get not-in-syn aeApplication

            Vector v_wbAppId = qman.getNotInSynAppId(con, this.synTimestamp, wbProfile.root_ent_id, wbItemType[k], new Boolean(false));
            //delete those not-in-syn aeApplication
            for(int i=0; i<v_wbAppId.size(); i++) {
                aeApplication wbApp = new aeApplication();
                wbApp.app_id = ((Long)v_wbAppId.elementAt(i)).longValue();
                try {
                    wbApp.del(con);
                    con.commit();
                    IMSUtils.writeLog(IMSUtils.CLEAN_FILE, "Application " + wbApp.app_id + " CLEANED.");
                }
                catch(Exception e) {
                    try{
                        con.rollback();
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "Application " + wbApp.app_id + " CANNOT BE DELETED", e);
                    }catch(SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup Application " + wbApp.app_id, sqle);
                    }
                }
            }
        }
        return;
    }

    public void cleanUpPerson(Connection con, long siteId, loginProfile prof, Timestamp deleteTime) throws SQLException, qdbException {
//        long lostFoundId = dbUserGroup.getLostFoundID(con, siteId);
        Hashtable htUsrEntIdNUsrSteUsrId = DbRegUser.getNotInSynUsrEntIdNUsrSteUsrId(con, this.synTimestamp, siteId, IMSEnterpriseApp.importSource);
        Enumeration enumeration = htUsrEntIdNUsrSteUsrId.keys();
        while(enumeration!=null && enumeration.hasMoreElements()) {
            Long L_usrEntId = (Long)enumeration.nextElement();
            try{
                DbRegUser usr = new DbRegUser();
                usr.usr_ent_id = L_usrEntId.longValue();
                usr.ent_id = usr.usr_ent_id;
                usr.usr_ste_ent_id = siteId;
                usr.deleteUser(con, prof.usr_id, deleteTime);
                con.commit();
                IMSUtils.writeLog(IMSUtils.CLEAN_FILE, htUsrEntIdNUsrSteUsrId.get(L_usrEntId) + " CLEANED");
            }catch (Exception e){
                try{
                    con.rollback();
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, htUsrEntIdNUsrSteUsrId.get(L_usrEntId) + "FAILURE IN CLEANUP PROCESS");
                }catch(SQLException sqle){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup person: " + htUsrEntIdNUsrSteUsrId.get(L_usrEntId) + " ",  e);
                }                
            }
        }
    }


    public void cleanUpEntityRelation(Connection con, long siteId, String ErnType, String clean_usr_id, Timestamp deleteTime) throws cwException, SQLException{
    	DbEntityRelation.ViewSynEntityRelation[] entRel = null;
        if (ErnType.startsWith(DbEntity.ENT_TYPE_USER)){
            int index = ErnType.lastIndexOf("_");
            String parentType = ErnType.substring(index+1, ErnType.length());
            entRel = DbEntityRelation.getNotInSynUsrRelation(con, this.synTimestamp, siteId, ErnType, parentType, IMSEnterpriseApp.importSource);
        }else if (ErnType.startsWith(DbEntity.ENT_TYPE_USER_GROUP)){
            entRel = DbEntityRelation.getNotInSynUsgRelation(con, this.synTimestamp, siteId, ErnType);
        }else{
            throw new cwException("only user, usergroup relations are supported.");
        }
        for (int i = 0; i < entRel.length; i++) {
        	DbEntityRelation ern = new DbEntityRelation();
        	ern.ern_ancestor_ent_id = entRel[i].group_ent_id;
        	ern.ern_child_ent_id = entRel[i].member_ent_id;
        	ern.ern_type = ErnType;
            String tmpLogMessage = ErnType + ":" + entRel[i].group_uid + ", " + entRel[i].member_uid;
            try{
            	ern.delAsChild(con, clean_usr_id, deleteTime);
                con.commit();
                IMSUtils.writeLog(IMSUtils.CLEAN_FILE, tmpLogMessage + " CLEANED.");
            }catch (Exception e){
                try{
                    con.rollback();
                    IMSUtils.writeLog(IMSUtils.CLEAN_FILE, tmpLogMessage + " FAILURE IN CLEANUP PROCESS.");
                }catch(SQLException sqle){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup group relation: " + tmpLogMessage + " ", e);
                }
            }
        }
    }

    public void cleanUpUsergroup(Connection con, long siteId, loginProfile wbProfile) throws SQLException{
        Hashtable htUserGroup = DbUserGroup.getNotInSynUsgNUid(con, this.synTimestamp, siteId);
        Enumeration enumeration = htUserGroup.keys();
        while(enumeration!=null && enumeration.hasMoreElements()) {
            Long L_entId = (Long)enumeration.nextElement();
            try{
               
                DbUserGroup dbUsg = new DbUserGroup();
                dbUsg.usg_ent_id = L_entId.longValue();
                dbUsg.ent_id = dbUsg.usg_ent_id;
                if(!DbUserGroup.hadChild( con, dbUsg.ent_id)){
                    dbUsg.delGroup(con, false, false, wbProfile.usr_id);
                    con.commit();
                    IMSUtils.writeLog(IMSUtils.CLEAN_FILE, htUserGroup.get(L_entId) + " CLEANED");
                } 
            }catch (Exception e){
                try{
                    con.rollback();
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, htUserGroup.get(L_entId) + "FAILURE IN CLEANUP PROCESS");
                }catch(SQLException sqle){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup group: " + htUserGroup.get(L_entId) + " ", e);
                }
            }
        }
    }
    
    
    public void cleanUpUsrRoleTargetEntity(Connection con, long siteId, String[] importRole) throws SQLException, cwException{
        AccessControlWZB acl = new AccessControlWZB();
        Hashtable htRole = acl.getAllRoleUid(con, siteId, "rol_ste_uid");
        
        Vector vtHasTargetEntRole = dbUtils.getRoleByTargetEntInd(con, siteId, true);
        for (int i=0; i<importRole.length; i++){
            if (vtHasTargetEntRole.contains(importRole[i])){
                cleanUpTargetGroup(con, siteId, importRole[i]);
            }            
            cleanUpUserRole(con, importRole[i]);
        }
/*        
//        Vector vtImportedRole = new Vector();
//        Vector vtHasTargetEntImportedRole = new Vector();
        Enumeration e_role = htRole.elements();
        while(e_role.hasMoreElements()) {
            String rol_ext_id = (String) e_role.nextElement();
            vtImportedRole.addElement(rol_ext_id);
            if (vtHasTargetEntRole.contains(rol_ext_id)){
                vtHasTargetEntImportedRole.addElement(rol_ext_id);
            }
        }

        for (int i=0; i<vtHasTargetEntImportedRole.size(); i++){
            cleanUpTargetGroup(con, siteId, (String)vtHasTargetEntImportedRole.elementAt(i));
        }
        for (int i=0; i<vtImportedRole.size(); i++){
            cleanUpUserRole(con, (String)vtImportedRole.elementAt(i));
        }
        */
    }
    
    private void cleanUpTargetGroup(Connection con, long siteId, String role) throws SQLException {
        DbRoleTargetEntity.ViewSynRoleTargetEntity[] synRte = DbRoleTargetEntity.getNotInSynUsrRoleTargetEntity(con, this.synTimestamp, siteId, role);
        
        for (int i = 0; i < synRte.length; i++) {
            DbRoleTargetEntity rte = new DbRoleTargetEntity();
            rte.rte_usr_ent_id = synRte[i].rte_usr_ent_id;
            rte.rte_rol_ext_id = synRte[i].rte_rol_ext_id;
            rte.rte_group_id = synRte[i].rte_group_id;
            rte.rte_ent_id = synRte[i].rte_ent_id;
            rte.rte_eff_start_datetime = synRte[i].rte_eff_start_datetime;
            rte.rte_eff_end_datetime = synRte[i].rte_eff_end_datetime;

            String tmpLogMessage = synRte[i].group_uid + ", " + synRte[i].user_uid + " MANAGEMENT RELATIONSHIP ";
            try{
                    rte.del(con, true);
                    con.commit();
                    IMSUtils.writeLog(IMSUtils.CLEAN_FILE, tmpLogMessage + " CLEANED.");
            }catch (Exception e){
                try{
                    con.rollback();
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, tmpLogMessage + " FAILURE IN CLEANUP PROCESS." , e);
                }catch(SQLException sqle){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup management realtion: " + tmpLogMessage + " ", e);
                }
            }
        }
    }

    public void cleanUpUserRole(Connection con, String rol_ext_id) throws SQLException{
        try{
            AccessControlWZB acl = new AccessControlWZB();
            AccessControlCore.ViewSynEntityRole[] entRole = acl.getNotSynEntityRole(con, rol_ext_id, this.synTimestamp);
            for (int i = 0; i < entRole.length; i++) {
                try{
                	boolean canRemoveRole = true;
                	if(AccessControlWZB.hasRolePrivilege(rol_ext_id,  AclFunction.FTN_AMD_USR_INFO)) {
                		Vector vSoleItem = ViewTrainingCenter.getSoleItemWithTheOnlyOfficer(con, entRole[i].erl_ent_id, rol_ext_id);
                		if(vSoleItem != null && vSoleItem.size() > 0) {
                			canRemoveRole = false;
                		}
                		if(canRemoveRole) {
            	        	ViewTrainingCenter.delOfficerRoleFromTc(con, entRole[i].erl_ent_id, rol_ext_id);
            	        	aeItemAccess.delByEntIdAndRole(con, entRole[i].erl_ent_id, rol_ext_id);
                		}
                	}
                	if(canRemoveRole) {
                		acl.rmUserFromRole(con, entRole[i].erl_ent_id, rol_ext_id, entRole[i].erl_eff_start_datetime, entRole[i].erl_eff_end_datetime);
                		con.commit();
                		IMSUtils.writeLog(IMSUtils.CLEAN_FILE, entRole[i].usr_ste_usr_id + " " + rol_ext_id + " start: " + entRole[i].erl_eff_start_datetime + " end: " + entRole[i].erl_eff_end_datetime + " ROLE CLEANED.");
                	}
                }catch (Exception e){
                    try{
                        con.rollback();
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, entRole[i].usr_ste_usr_id  + " FAILURE IN CLEANUP ROLE: " + rol_ext_id + " start: " + entRole[i].erl_eff_start_datetime + " end: " + entRole[i].erl_eff_end_datetime , e);
                    }catch(SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK cleanup ROLE for : " + entRole[i].usr_ste_usr_id  + " " + rol_ext_id + " start: " + entRole[i].erl_eff_start_datetime + " end: " + entRole[i].erl_eff_end_datetime , e);
                    }
                }
            }
        }catch (Exception e){
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, " ERROR IN CLEANUP ROLE: " + rol_ext_id , e);
        }
    }



    /**
    * Clean up not-in-syn tree node.<br>
    * If the delete tree node is a catalog tree node or normal tree node,
    * check there is any child belong to it. Delete it if no child otherwise keep it.
    * @param blackListGroup 
    */
    public void cleanUpTreeNode(Connection con, long site_id, Vector blackListGroup)
        throws SQLException, cwSysMessage, qdbException{

            //Get all the syn-ed tree node id
            Vector synedTndIdVec = aeTreeNode.getSynedTreeNodeId(con, site_id, synTimestamp);
            //Get all not-in-syn tree node
            Vector tndVec = aeTreeNode.getNotInSynTreeNode(con, site_id, synTimestamp);
            aeTreeNode aeTnd = null;
            aeItem itm = null;
            String status = null;
            for(int i=0; i<tndVec.size(); i++){
                try{
                    aeTnd = (aeTreeNode)tndVec.elementAt(i);
                    //if it is a item/link tree node
                    //delete it and cascading update item count on it's ancestor
                    if( aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_LINK) || aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_ITEM) ){

                        aeTreeNode parentNode = new aeTreeNode();
                        parentNode.tnd_id = aeTnd.tnd_parent_tnd_id;
                        parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);

                        if(aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_LINK) ) {
                            status = aeTnd.getNodeStatus(con);
                            if(status.equals(aeTreeNode.TND_STATUS_ON))
                                parentNode.cascadeUpdOnItmCntBy(con, -1);
                        } else {
                            itm = new aeItem();
                            itm.itm_id = aeTnd.tnd_itm_id;
                            if( blackListGroup.indexOf(itm.getItemCode(con)) != -1 )
                                continue;
                            status = itm.getItemStatus(con);
                            if(status.equals(aeItem.ITM_STATUS_ON))
                                parentNode.cascadeUpdOnItmCntBy(con, -1);
                        }
                        aeTreeNodeRelation.delTnrByTnd(con, aeTnd.tnd_id);
                        aeTnd.del(con);
                        parentNode.cascadeUpdItmCntBy(con, -1);
                        
                        IMSUtils.writeLog(IMSUtils.CLEAN_FILE, "Tree Node ID-" + aeTnd.tnd_id + " CLEANED.");
                    } 
                    // if it is a catalog/normal tree node, get all childs belong to it
                    // and check it can be deleted or not by the child item type and syn-date
                    else if( aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_CAT) || aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_NORMAL) ){

                        Vector childTndVec;
                        if( aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_CAT) ){
                            childTndVec = aeTnd.getCatalogChildTreeNode(con, aeTnd.tnd_cat_id);
                        }else{
                            childTndVec = aeTnd.getChildTreeNode(con, aeTnd.tnd_id);
                        }
                        aeTreeNode _aeTnd = null;
                        boolean valid = true;
                        //if the child is in-syn or item code is null, then the catalog/normal is not valid to delete
                        for(int j=0; j<childTndVec.size(); j++){
                            _aeTnd = (aeTreeNode)childTndVec.elementAt(j);
                            if(_aeTnd.tnd_code == null){
                                valid = false;
                                break;
                            }else if( synedTndIdVec.indexOf(new Long(_aeTnd.tnd_id)) != - 1 ) {
                                valid = false;
                                break;
                            } else {
                                if( _aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_ITEM) ) {
                                    itm = new aeItem();
                                    itm.itm_id = _aeTnd.tnd_itm_id;
                                    if( blackListGroup.indexOf(itm.getItemCode(con)) != -1 )
                                        valid = false;
                                }
                            }
                        }
                        if(valid){
                            aeTnd.delChildTreeNode(con, aeTnd.tnd_id);
                            aeTnd.del(con);
                            //if the catalog tree node is valid to delete,
                            //also delete the catalog
                            if( aeTnd.tnd_type.equalsIgnoreCase(aeTreeNode.TND_TYPE_CAT) ){
                                DbCatalogItemType.delByCatalog(con, aeTnd.tnd_cat_id);
                                aeCatalogAccess.delCat(con, aeTnd.tnd_cat_id);
                                aeCatalog aeCat = new aeCatalog();
                                aeCat.cat_id = aeTnd.tnd_cat_id;
                                aeCat.del(con);
                            }
                            IMSUtils.writeLog(IMSUtils.CLEAN_FILE, aeTnd.tnd_code + " CLEANED.");
                        }else {                            
                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, aeTnd.tnd_code  + " CANNOT DELETE.");
                        }
                    }
                    con.commit();
                }catch(Exception e){
                    try{
                        con.rollback();
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, aeTnd.tnd_code + " FAILED TO CLEAN UP.");
                    }catch(SQLException sqle){
                        IMSUtils.writeLog(IMSUtils.FAILURE_FILE, aeTnd.tnd_code + " FAILED TO ROOLBACK CLEAN UP PROCESS." );
                    }                    
                }
                
            }
            return;
        }


    public void attachPerson(Connection con, long siteId, loginProfile wbProfile) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        Hashtable htUser = DbRegUser.getOrphan(con, siteId);
        Enumeration enumeration = htUser.keys();
        while(enumeration!=null && enumeration.hasMoreElements()) {
            Long L_entId = (Long)enumeration.nextElement();
            try{
                DbEntityRelation dber = new DbEntityRelation();
                dber.ern_ancestor_ent_id = siteId;
                dber.ern_child_ent_id = L_entId.longValue();
                dber.ern_type = DbEntityRelation.ERN_TYPE_USR_PARENT_USG;
                dber.ern_syn_timestamp = curTime;
                dber.insEr(con, wbProfile.usr_id);
                con.commit();
                IMSUtils.writeLog(IMSUtils.CLEAN_FILE, htUser.get(L_entId) + " ATTACHED TO ROOT.");
            }catch (Exception e){
                try{
                    con.rollback();
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, htUser.get(L_entId) + " ATTACHED TO ROOT.");
                }catch(SQLException sqle){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, htUser.get(L_entId)  + " CANNOT ROLLBACK in ATTACHED TO ROOT." , e);
                }
            }
        }
    }

    public void attachPersonDefaultRole(Connection con, long siteId){
        try{
            String defaultRole = AccessControlWZB.getDefaultRoleBySite(con, siteId);
            if (defaultRole!=null){
                AccessControlWZB acl = new AccessControlWZB();
                Hashtable htUsr = acl.getNoRoleUsr(con);
                Enumeration enumeration = htUsr.keys();
                while(enumeration!=null && enumeration.hasMoreElements()) {
                    Long L_entId = (Long)enumeration.nextElement();
                    try{
//                        DbEntity ent = new DbEntity();
//                        ent.ent_id = L_entId.longValue();
//                        ent.get(con);
//                        if (ent.ent_syn_rol_ind){
                        DbRegUser usr = new DbRegUser();
                        usr.usr_ent_id = L_entId.longValue();
                        usr.getUsrSynInfo(con);
                        if (usr.usr_syn_rol_ind){            
                            acl.assignUser2Role(con, L_entId.longValue(), defaultRole, Timestamp.valueOf(cwUtils.MIN_TIMESTAMP), Timestamp.valueOf(cwUtils.MAX_TIMESTAMP));
                            con.commit();
                            IMSUtils.writeLog(IMSUtils.CLEAN_FILE, htUsr.get(L_entId) + " ATTACHED TO DEFAULT ROLE .");
                        }else{
                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "WARNING: USER (" + htUsr.get(L_entId) + ") HAS NO ROLE.");
                        }
                    }catch (Exception e){
                        try{
                            con.rollback();
                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, htUsr.get(L_entId) + " HAS NO ROLE." , e);
                        }catch(SQLException sqle){
                            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "CANNOT ROLLBACK ATTACHED TO DEFAULT ROLE for " +  htUsr.get(L_entId), e);
                        }
                    }
                }
            }
        }catch (Exception e){
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, " ERROR IN ATTACH TO DEFAULT ROLE", e);
        }
    }

    public void attachUsergroup(Connection con, long siteId, loginProfile wbProfile) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        Hashtable htUsg = DbUserGroup.getOrphan(con, siteId);
        Enumeration enumeration = htUsg.keys();
        while(enumeration!=null && enumeration.hasMoreElements()) {
            Long L_entId = (Long)enumeration.nextElement();
            try{
            	DbEntityRelation dber = new DbEntityRelation();
            	dber.ern_ancestor_ent_id = siteId;
            	dber.ern_child_ent_id = L_entId.longValue();
            	dber.ern_type = DbEntityRelation.ERN_TYPE_USG_PARENT_USG;
            	dber.ern_syn_timestamp = curTime;
            	dber.insEr(con, wbProfile.usr_id);
                con.commit();
                IMSUtils.writeLog(IMSUtils.CLEAN_FILE, htUsg.get(L_entId) + " ATTACHED TO ROOT.");
            }catch (Exception e){
                try{
                    con.rollback();
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, htUsg.get(L_entId) + " ATTACHED TO ROOT.");
                }catch(SQLException sqle){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, htUsg.get(L_entId)  + " CANNOT ROLLBACK in ATTACHED TO ROOT." , e);
                }
            }
        }
    }
}
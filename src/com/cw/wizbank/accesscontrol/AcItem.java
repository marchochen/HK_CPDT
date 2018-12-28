package com.cw.wizbank.accesscontrol;

import java.sql.*;

import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemLessonInstructor;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.instructor.InstructorManager;
import com.cwn.wizbank.security.AclFunction;

/**
Logical Layer Access Control on Catalog<BR>
*/
public class AcItem extends AcInstance {

    /**
    function ext id, offline catalog/tree node read
    *//*
    public static final String FTN_ITM_OFF_READ = "ITM_COS_MAIN";//ITM_OFF_READ

    *//**
    function ext id, read item which is the target to the user    
    *//*  
      //Will never refer again  Deprecate
    //public static final String FTN_ITM_NON_TARGET_READ = "ITM_NON_TARGET_READ";    
    *//**    
    function ext id, create item privilege
    *//*
    public static final String FTN_ITM_CREATE = "PLAN_CARRY_OUT";//ITM_CREATE

    *//**
    function ext id, item management in organization
    *//*
    public static final String FTN_ITM_MGT_IN_ORG = "ITM_COS_MAIN";

    *//**
    function ext id, request item approval
    *//*
    public static final String FTN_ITM_REQ_APPROVAL = "ITM_REQ_APPROVAL";
    
    *//**
    function ext id, approve item
    *//*
    public static final String FTN_ITM_APPROVE = "ITM_APPROVE";

    *//**
    function ext id, item management from aeItemAccess
    *//*
    public static final String FTN_ITM_MGT_IN_ITM_ACC = "ITM_MGT_IN_ITM_ACC";

	*//**
	function ext id, item management from aeItemAccess
	*//*
	public static final String FTN_INSTR_IN_ITM_ACC = "INSTR_IN_ITM_ACC";

    *//**
    function ext id, item management bounded by the approval status
    *//*
    public static final String FTN_ITM_MGT_ASSISTANT = "ITM_MGT_ASSISTANT";

    *//**
    function ext id, run item management of parent item
    *//*
    public static final String FTN_RUN_MGT_IN_ITM = "RUN_MGT_IN_ITM";
    
    *//**
    function ext id, function to apply item
    *//*
    public static final String FTN_ITM_APPLY = "ITM_APPLY";

    *//**
    function ext id, function to read pre-approve item
    *//*
    public static final String FTN_ITM_PRE_APPR_READ = "ITM_PRE_APPR_READ";

    *//**
    function ext id, function to indicate if respon all items in item search
    *//*
    public static final String FTN_ITM_SEARCH_RESPON_ALL = "ITM_SEARCH_RESPON_ALL";

    *//**
    function ext id, function to view item's DETAIL_VIEW
    *//*
    public static final String FTN_ITM_DETAIL_VIEW = "ITM_DETAIL_VIEW";    

    public static final String FTN_ITM_INTEGRATED_MAIN = "ITM_COS_MAIN";    //ITM_INTEGRATED_MAIN
    
    public static final String FTN_ITM_MGT = "ITM_MGT";
    public static final String FTN_ITM_MGT_CONTENT = "ITM_COS_MAIN";//ITM_MGT_CONTENT
    public static final String FTN_ITM_MGT_PERFORMANCE = "ITM_COS_MAIN";//ITM_MGT_PERFORMANCE
    public static final String FTN_ITM_MGT_APPLICATION = "ITM_COS_MAIN";//ITM_MGT_APPLICATION
*/   
	public AcItem(Connection con) {
        super(con);
        //view = new ViewAcResources(con); 
    }

 
    /**
    check if the user, role pair has the privilege to read offline resources
    */
    public boolean hasOffReadPrivilege(long ent_id, String rol_ext_id) throws SQLException {
    	return AccessControlWZB.hasRolePrivilege(rol_ext_id, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN,AclFunction.FTN_AMD_TEACHING_COURSE_LIST});
        
//        return hasFunctionPrivilege(ent_id, rol_ext_id, FTN_ITM_OFF_READ);
        
    }    
    
    /**    check if the user, role pair has the privilege to read the item which is not target to this user    
    */    
    /**
     * Deleted this method
     * */
    /*
    public boolean hasNonTargetReadPrivilege(long ent_id, String rol_ext_id) throws SQLException {        
        return hasFunctionPrivilege(ent_id, rol_ext_id, FTN_ITM_NON_TARGET_READ);        
    }
  */
    /**
    check if the user has privilege to read a item
    */
    public boolean hasReadPrivilege(long itm_id, long ent_id, String rol_ext_id) 
                                    throws SQLException, cwSysMessage {
        boolean hasRight;
        
        if(!hasOffReadPrivilege(ent_id, rol_ext_id)) {
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            if(itm.isItemOff(con))
                hasRight = false;
            else
                hasRight = true;
        }
        else
            hasRight = true;
        
        return hasRight;
    }

    /**
    check if the user has privilege to read a item
    */
    public boolean hasReadPrivilege(aeItem itm, long ent_id, String rol_ext_id, boolean checkStatus) 
                                    throws SQLException, cwSysMessage {
        boolean hasRight;
        
        if(checkStatus) {
            if(itm.itm_status != null && itm.itm_status.equals(aeItem.ITM_STATUS_OFF))
                hasRight = false;
            else
                hasRight = true;
        }
        else
            hasRight = true;
        
        return hasRight;
    }

    /**
    check if has insert item privilege<BR>
    @param tnd_cat_id catalog id of the tree node
    @param owner_ent_id root entity id of the user who wants to update the catalog
    */
    public boolean hasInsPrivilege(long ent_id, String rol_ext_id, 
                                   long tnd_parent_tnd_id, long owner_ent_id) 
        throws SQLException, cwSysMessage, qdbException {
        
        boolean hasRight;
        hasRight =  AccessControlWZB.hasRolePrivilege(rol_ext_id, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
        if(hasRight) {
            if(tnd_parent_tnd_id != 0) {
                aeTreeNode parentNode = new aeTreeNode();
                parentNode.tnd_id = tnd_parent_tnd_id;
                parentNode.tnd_cat_id = parentNode.getCatalogId(con);

                AcCatalog accat = new AcCatalog(con);
                hasRight = accat.hasUpdPrivilege(parentNode.tnd_cat_id, ent_id, rol_ext_id, owner_ent_id);
            }
            else {
                hasRight = true;
            }
        }
        return hasRight;
    }

    /**
    Check if has update item privilege<BR>
    @param itm_id item id of the item
    @param owner_ent_id root entity id of the user who wants to update the catalog
    @return true if<BR>
    Case 1: user, role pair has ITM_MGT_IN_ORG and the item is in the same organization as the user<BR>
    Case 2: user, role pair is one of the training center officers of the associated training center of the item<BR>
    Case 3: user, role pair is a class And base on 2<BR>
    */
    public boolean hasUpdPrivilege(long itm_id, long ent_id, String rol_ext_id, 
                                   long owner_ent_id) 
        throws SQLException, cwSysMessage {
        
        boolean result = false;
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.getItem(con);
        return hasUpdPrivilege(itm, ent_id, rol_ext_id, owner_ent_id);        
    }

   
//	public boolean hasUpdPrivilege(aeItem itm, long ent_id, String rol_ext_id, 
//								   long owner_ent_id) 
//		throws SQLException, cwSysMessage {
//    
//		boolean result = false;
//    
//		if(hasFunctionPrivilege(ent_id, rol_ext_id, FTN_ITM_MGT_IN_ORG)
//			&& owner_ent_id == itm.itm_owner_ent_id) {
//			result = true;
//		}
//		else {
//			aeItemAccess iac = new aeItemAccess();
//			iac.iac_ent_id = ent_id;
//			iac.iac_itm_id = itm.itm_id;
//			iac.iac_access_id = rol_ext_id;
//			iac.iac_access_type = aeItemAccess.ACCESS_TYPE_ROLE;
//			if(hasFunctionPrivilege(ent_id, rol_ext_id, FTN_ITM_MGT_IN_ITM_ACC)
//				&& iac.isExist(con)) {
//				result = true;
//			}
//			else if(itm.itm_run_ind) {
//				aeItemRelation ire = new aeItemRelation();
//				ire.ire_child_itm_id = itm.itm_id;
//				ire.ire_parent_itm_id = ire.getParentItemId(con);
//				result = (hasFunctionPrivilege(ent_id, rol_ext_id, FTN_RUN_MGT_IN_ITM)
//						 && hasUpdPrivilege(ire.ire_parent_itm_id, ent_id, rol_ext_id, owner_ent_id));
//			}
//		}
//		return result;
//		//return itm.hasUpdPrivilege(this.con, owner_ent_id);
//	}

	public boolean hasUpdPrivilege(aeItem itm, long ent_id, String rol_ext_id, long owner_ent_id)throws SQLException{
			boolean result = false;
			if( AccessControlWZB.hasRolePrivilege(rol_ext_id, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN})&& owner_ent_id == itm.itm_owner_ent_id) {
					result = true;
			}else{
                    result = ViewTrainingCenter.isItemOfficer(con,itm,owner_ent_id,ent_id,rol_ext_id,false)
                             &&  AccessControlWZB.hasRolePrivilege(rol_ext_id, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
			}
			return result;
		}

  

    /**
    Check if user, role pair has privilege to respon all items in item search<BR>
    @return true if user, role pair has ITM_APPROVE function
    */
    public boolean hasResponAllPrivilege(long ent_id, String rol_ext_id) 
        throws SQLException {
        
        return AccessControlWZB.hasRolePrivilege(rol_ext_id, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
    }


/**
 * @param itm_id
 * @param ent_id
 * @param rol_ext_id
 * @param owner_ent_id
 * @return
 * @throws SQLException
 * @throws cwSysMessage
 * based on the condition of hasUpdPrivilege,otherwise,
 * the role pair has function INSTR_IN_ITM_ACC AND the pair is assigned to the item according to aeItemAccess.
 */
	public boolean hasMaintainPrivilege(long itm_id, long ent_id, String rol_ext_id, 
								   long owner_ent_id) 
		throws cwSysMessage, SQLException, qdbException {
        
		//boolean result = false;
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		itm.getItem(con);
		return hasMaintainPrivilege(itm, ent_id, rol_ext_id, owner_ent_id);        
	}

//	public boolean hasMaintainPrivilege(aeItem itm, long ent_id, String rol_ext_id, 
//								   long owner_ent_id) 
//		throws SQLException, cwSysMessage {
//        
//		boolean result = false;
//        
//		if(hasFunctionPrivilege(ent_id, rol_ext_id, FTN_ITM_MGT_IN_ORG)
//			&& owner_ent_id == itm.itm_owner_ent_id) {
//			result = true;
//		}
//		else {
//			aeItemAccess iac = new aeItemAccess();
//			iac.iac_ent_id = ent_id;
//			iac.iac_itm_id = itm.itm_id;
//			iac.iac_access_id = rol_ext_id;
//			iac.iac_access_type = aeItemAccess.ACCESS_TYPE_ROLE;
//			if(hasFunctionPrivilege(ent_id, rol_ext_id, FTN_ITM_MGT_IN_ITM_ACC)
//				&& iac.isExist(con)) {
//				result = true;
//			}
//			else{
//				if(hasFunctionPrivilege(ent_id, rol_ext_id, FTN_INSTR_IN_ITM_ACC)
//				&& iac.isExist(con)) {
//				result = true;
//				}
//
//				else if(itm.itm_run_ind) {
//				aeItemRelation ire = new aeItemRelation();
//				ire.ire_child_itm_id = itm.itm_id;
//				ire.ire_parent_itm_id = ire.getParentItemId(con);
//				result = (hasFunctionPrivilege(ent_id, rol_ext_id, FTN_RUN_MGT_IN_ITM)
//						 && hasUpdPrivilege(ire.ire_parent_itm_id, ent_id, rol_ext_id, owner_ent_id));
//				}
//			}
//		}
//	return result;
//	}

	public boolean hasMaintainPrivilege(aeItem itm, long ent_id, String rol_ext_id, long owner_ent_id) throws SQLException, qdbException, cwSysMessage {
			boolean result = false;
			result = hasUpdPrivilege(itm,ent_id, rol_ext_id, owner_ent_id);
            boolean hasInstrInItmAcc = AccessControlWZB.hasRolePrivilege(rol_ext_id, new String []{AclFunction.FTN_AMD_EXAM_MAIN_VIEW,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW}); 
			if(!result && hasInstrInItmAcc){
				//check if the entity has function "FTN_INSTR_IN_ITM_ACC"
				aeItemAccess iac = new aeItemAccess();
                iac.iac_itm_id = itm.itm_id;
                iac.iac_ent_id = ent_id;
                iac.iac_access_id = rol_ext_id;
                iac.iac_access_type = aeItemAccess.ACCESS_TYPE_ROLE;
				result = iac.isExist(con);
			}
            if(!result){
                result = aeItemLessonInstructor.isAnInstrOfAnItm(con,ent_id,itm.itm_id);
            }
            if(!result && itm.itm_run_ind && hasInstrInItmAcc) {
                aeItemRelation ire = new aeItemRelation();
                ire.ire_child_itm_id = itm.itm_id;
                ire.ire_parent_itm_id = ire.getParentItemId(con);

                aeItemAccess iac = new aeItemAccess();
                iac.iac_itm_id = ire.ire_parent_itm_id;
                iac.iac_ent_id = ent_id;
                iac.iac_access_id = rol_ext_id;
                iac.iac_access_type = aeItemAccess.ACCESS_TYPE_ROLE;
                result = iac.isExist(con);
            }
			return result;
		}
	

    
    public boolean hasIntgMgtPrivilege(long ent_id, String rol_ext_id) throws SQLException {
    	return AccessControlWZB.hasRolePrivilege(rol_ext_id, new String []{AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_TEACHING_COURSE_LIST});
    }
    
  

}
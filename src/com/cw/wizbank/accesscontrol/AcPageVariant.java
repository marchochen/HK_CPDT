package com.cw.wizbank.accesscontrol;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeLearningSoln;
import com.cw.wizbank.ae.db.DbItemFigure;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.ae.db.view.ViewItemTemplate;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.learningplan.LearningPlan;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.config.organization.itemcostmgt.ItemCostType;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbKmBaseObject;
import com.cw.wizbank.db.DbKmNode;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.km.library.KMLibraryObjectManager;
import com.cw.wizbank.lcms.LcmsModule;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tpplan.db.dbTpTrainingPlan;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

/**
this class is design to answer questions from front end 
like "should I show this link/button?"
*/
public class AcPageVariant {

	public loginProfile prof;

	public long instance_id;

	public long ent_owner_ent_id;

	public String root_id;

	public long ent_id;

	public String usr_id;

	public String rol_ext_id;

	public String msg_type;

	public String[] itm_types;

	public boolean itm_show_run_ind;

	public long obj_id;

	private AccessControlWZB acl;

	private Connection con;

	private aeItem itm;

	private aeItem parent;

	private Boolean checkItmPageVariant = null;
	private Vector vtItmPageVariant = null;
	
	public boolean tc_enable_ind;

	private DbKmBaseObject baseObj;

	private WizbiniLoader wizbini;
	
	public long tcr_id;

	public void setWizbiniLoader(WizbiniLoader wizbini) {
		this.wizbini = wizbini;
		return;
	}

	public void setItem(aeItem itm) {
		this.itm = itm;
	}

	public aeItem getItem(aeItem itm) {
		return this.itm;
	}

	public void setBaseObject(DbKmBaseObject baseObj) {
		this.baseObj = baseObj;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public Connection getCon() {
		return this.con;
	}

	public AcPageVariant(Connection con) {
		super();
		setCon(con);
	}

	/**
	answer the questions[] and
	generate the question and answer as XML
	*/
	public String answerPageVariantAsXML(String[] questions) throws SQLException {
		if (questions == null) {
			return "";
		}
		//these booleans are used to prevent calling the same funtion for serval times
		//once a function is called, return value saved into boolean 
		//and flag turns to true 
		boolean hasUsrMgt = false;
		boolean hasUsrMgtFlag = false;
		boolean hasUsgMgt = false;
		boolean hasUsgMgtFlag = false;
		boolean hasItmMgt = false;
		boolean hasItmMgtFlag = false;
		boolean hasCatMgt = false;
		boolean hasCatMgtFlag = false;
		boolean hasSysMsgMgt = false;
		boolean hasSysMsgMgtFlag = false;
		boolean isUsrDeleted = false;
		boolean isUsrDeletedFlag = false;
		boolean hasPubForMgt = false;
		boolean hasPubForMgtFlag = false;
		boolean hasModifyQuizModuleBtn = false;
		boolean hasModifyQuizModuleBtnFlag = false;
		boolean hasCcrMgt = false;
		boolean hasCcrMgtFlag = false;
		boolean hasFacMgt = false;
		boolean hasFacMgtFlag = false;
		boolean hasRsvMgt = false;
		boolean hasRsvMgtFlag = false;
		boolean hasCosContentMgt = false;
		boolean hasCosContentMgtFlag = false;
		boolean hasKbAddCategory = false;
		boolean hasKbAddCategoryFlag = false;
		boolean hasKbEditCategory = false;
		boolean hasKbEditCategoryFlag = false;
		boolean hasKbDelCategory = false;
		boolean hasKbDelCategoryFlag = false;
		boolean hasKbAddWorkFolder = false;
		boolean hasKbAddWorkFolderFlag = false;
		boolean hasKbEditWorkFolder = false;
		boolean hasKbEditWorkFolderFlag = false;
		boolean hasKbDelWorkFolder = false;
		boolean hasKbDelWorkFolderFlag = false;
		boolean hasKmAddNode = false;
		boolean hasKmAddNodeFlag = false;
		boolean hasKmNodeMgt = false;
		boolean hasKmNodeMgtFlag = false;
		boolean hasItmAdd = false;
		boolean hasItmAddFlag = false;
		boolean hasItmDetailView = false;
		boolean hasItmDetailViewFlag = false;
		//new items
		boolean hasEditObjBtn = false;
		boolean hasEditObjBtnFlag = false;
		boolean hasRemoveObjBtn = false;
		boolean hasRemoveObjBtnFlag = false;
		boolean hasAddObjBtn = false;
		boolean hasAddObjBtnFlag = false;
		boolean hasAccessRightBtn = false;
		boolean hasAccessRightBtnFlag = false;
		boolean isObjRoot = false;
		boolean isObjRootFlag = false;

		boolean hasPasteResBtn = false;
		boolean hasPasteResBtnFlag = false;
		boolean hasRemoveResBtn = false;
		boolean hasRemoveResBtnFlag = false;
		boolean hasAddResBtn = false;
		boolean hasAddResBtnFlag = false;
		boolean hasTurnOnResBtn = false;
		boolean hasTurnOnResBtnFlag = false;
		boolean hasTurnOffResBtn = false;
		boolean hasTurnOffResBtnFlag = false;
		boolean hasEditResBtn = false;
		boolean hasEditResBtnFlag = false;

		boolean hasAddQueBtn = false;
		boolean hasAddQueBtnFlag = false;

//		boolean hasEditLessonBtn = false;
//		boolean hasEditLessonBtnFlag = false;
		
		boolean hasEditPlanBtn = false;
		boolean hasEditPlanBtnFlag = false;
		boolean hasReferPlanBtn  = false;
		boolean hasReferPlanBtnFlag  = false;
		boolean hasDeletePlanBtn  = false;
		boolean hasDeletePlanBtnFlag  = false;
		boolean hasImplementPlanBtn  = false;
		boolean hasImplementPlanBtnFlag  = false;
		boolean hasApprovedPlanBtn = false;
		boolean hasIntegratedMgt = false;
		boolean hasLcmsOfflinePkgBtn = false;
		
		boolean hasShareBtn = false;

		boolean[] answers = new boolean[questions.length];

		//for each question, dispatch it to the corresponding funcion
		for (int i = 0; i < questions.length; i++) {

			if (questions[i].equalsIgnoreCase("hasUsrEditBtn")) {
				if (!isUsrDeletedFlag) {
					isUsrDeleted = isUsrDeleted();
				}
				answers[i] = hasUsrEditFtn() && !isUsrDeleted;
			}
			else if (questions[i].equalsIgnoreCase("hasUsrTrashBtn")) {
				if (!isUsrDeletedFlag) {
					isUsrDeleted = isUsrDeleted();
				}
				answers[i] = hasUsrEditFtn() && !isUsrDeleted;
			}
			else if (questions[i].equalsIgnoreCase("hasUsrDelBtn")) {
				if (!isUsrDeletedFlag) {
					isUsrDeleted = isUsrDeleted();
				}
				answers[i] = hasUsrEditFtn() && !isUsrDeleted;
			}
			else if (questions[i].equalsIgnoreCase("hasUsrCopyBtn")) {

				if (!isUsrDeletedFlag) {
					isUsrDeleted = isUsrDeleted();
				}
				answers[i] = hasUsrEditFtn() && !isUsrDeleted;
			}
			else if (questions[i].equalsIgnoreCase("hasUsrAddBtn")) {
				answers[i] = (hasUsrMgtFlag) ? hasUsrMgt : hasUsrMgtFtn();
				hasUsrMgt = answers[i];
				hasUsrMgtFlag = true;
				//answers[i] = hasUsrMgtFtn();
			}
			else if (questions[i].equalsIgnoreCase("hasUsgAddBtn")) {
				boolean isRoleTcInd = AccessControlWZB.isRoleTcInd(rol_ext_id);
				boolean hasUsrInfoPrivilege = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_USR_INFO);
				if(!isRoleTcInd){//如果角色与培训中心无关，看是否有【用户信息】的权限
					answers[i] = hasUsrInfoPrivilege;
				}else{//如果角色与培训中心有关
					answers[i] = hasUsrInfoPrivilege && (!isUsgRoot() || ViewTrainingCenter.isSuperTA(this.con, this.ent_owner_ent_id, this.ent_id, this.rol_ext_id));
				}
			}
			else if (questions[i].equalsIgnoreCase("hasUsgPasteBtn")) {
				answers[i] = (hasUsgMgtFlag) ? hasUsgMgt : hasUsgMgtFtn();
				hasUsgMgt = answers[i];
				hasUsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasUsgCopyBtn")) {
				answers[i] = (hasUsgMgtFlag) ? hasUsgMgt : hasUsgMgtFtn();
				hasUsgMgt = answers[i];
				hasUsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasUsgEditBtn")) {
				answers[i] = (hasUsgMgtFlag) ? hasUsgMgt : hasUsgMgtFtn();
				hasUsgMgt = answers[i];
				hasUsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasUsgDelBtn")) {
				answers[i] = (hasUsgMgtFlag) ? hasUsgMgt : hasUsgMgtFtn();
				hasUsgMgt = answers[i];
				hasUsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasUsgLostFoundLink")) {
				answers[i] = ((hasUsgMgtFlag) ? hasUsgMgt : hasUsgMgtFtn()) && isUsgRoot();
				hasUsgMgt = answers[i];
				hasUsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasUsrBatchUploadTag")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_USR_INFO);
			}
			else if (questions[i].equalsIgnoreCase("hasUsrReactivateTab")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_USR_ACTIVATE);
			}
			else if (questions[i].equalsIgnoreCase("hasUsrApprovalTab")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_USR_REGIETER_APP);
			}
			else if (questions[i].equalsIgnoreCase("hasUsrGradeMgtTab")) {
				answers[i] = !tc_enable_ind || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_GRADE_MAIN)
						|| (wizbini.cfgSysSetupadv.isTcIndependent() && hasManageTcr());
			}
			else if (questions[i].equalsIgnoreCase("hasUsrPositionMgtTab")) {
				answers[i] = !tc_enable_ind || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_POSITION_MAIN)
						|| (wizbini.cfgSysSetupadv.isTcIndependent() && hasManageTcr());
			}
			else if (questions[i].equalsIgnoreCase("hasItmEditBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				answers[i] = hasItmMgt && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasItmEditBtn") : true);
			}
			else if (questions[i].equalsIgnoreCase("hasCourseLessonBtn")) {
                /*
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				answers[i] = hasItmMgt && hasCourseLessonFtn();
                */
                answers[i] = hasCourseLessonFtn();
			}
			else if (questions[i].equalsIgnoreCase("hasItmPrerequisiteBtn")) {
				answers[i] = (hasItmMgtFlag) ? hasItmMgt : hasItmMgtFtn();
				hasItmMgt = answers[i];
				hasItmMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasItmRequirementBtn")) {
				answers[i] = !tc_enable_ind || ((hasItmMgtFlag) ? hasItmMgt : hasItmMgtFtn());
				hasItmMgt = answers[i];
				hasItmMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasItmDelBtn")) {
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				answers[i] = hasItmMgtCancelledFtn() && (isItmRun() || isItmSession() || !isItmApproved() || isItmCancelled()) && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasItmDelBtn") : true);
			}
			else if (questions[i].equalsIgnoreCase("hasItmOnOffBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				answers[i] = hasItmMgt && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasItmOnOffBtn") : true);
			} else if (questions[i].equalsIgnoreCase("hasItmPerformanceBtn")) {
			    answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  new String[]{AclFunction.FTN_AMD_EXAM_MAIN_PERFORMANCE,AclFunction.FTN_AMD_ITM_COS_MAIN_PERFORMANCE} );
			}
			else if (questions[i].equalsIgnoreCase("hasItmProcEnrolBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				answers[i] = (hasItmMgt || hasItmApplicationMgtFtn())  && AccessControlWZB.hasRolePrivilege( rol_ext_id,  new String[]{AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION,AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION })  && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasItmProcEnrolBtn") : true);
			}
			else if (questions[i].equalsIgnoreCase("hasItmCancelRunBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				answers[i] = hasItmMgt && isItmRun() && isItmCanCancel();
			}
			else if (questions[i].equalsIgnoreCase("hasItmCancelSessionBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				answers[i] = hasItmMgt && isItmSession() && isItmCanCancel();
			}
			else if (questions[i].equalsIgnoreCase("hasItmCancelCourseBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				answers[i] = hasItmMgt && !isItmRun() && !isItmSession() && isItmCanCancel();
			}
			else if (questions[i].equalsIgnoreCase("hasItmBookSystemBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				//answers[i] = hasItmMgt && (isItmRun() || isItmApplicable());
				answers[i] = hasItmMgt && isItmRun() && isItmRsvEnabled();
			}
			else if (questions[i].equalsIgnoreCase("hasItmJIBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				//answers[i] = hasItmMgt && (isItmRun() || isItmApplicable());
				answers[i] = hasItmMgt && hasItmJIFtn() && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasItmJIBtn") : true);
			}
			else if (questions[i].equalsIgnoreCase("hasItmAttendanceUpdateByCriteriaBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				//answers[i] = hasItmMgt && (isItmRun() || isItmApplicable());
				answers[i] = hasItmMgt && hasItmAttendanceUpdateByCriteriaFtn();
			}
			else if (questions[i].equalsIgnoreCase("hasCompletionCriteriaBtn")) {
				/*
                if(!hasItmMgtFlag) {
                    hasItmMgt = hasItmMgtFtn();
                }
                */
                //answers[i] = hasItmMgt && (isItmRun() || isItmApplicable());
                answers[i] = hasItmMaintainFtn() && hasCompletionCriteriaFtn();
            }
            else if(questions[i].equalsIgnoreCase("hasItmCostBtn")){
            	answers[i] = hasItmMgtFtn() && hasItemCostBtn();
            }
            else if(questions[i].equalsIgnoreCase("hasCourseContentBtn")) {
//                if(!hasCosContentMgtFlag) {
//                	//check if the user has the modify access to the course
//                    hasCosContentMgt = hasCosContentMgtFtn();
//                }
//                if (checkItmPageVariant==null){
//                    setItmPageVariant();
//                }                    
//                //answers[i] = hasItmMgt && (isItmRun() || isItmApplicable());
//                //the last one of this expression seems no use.
//                answers[i] = hasCosContentMgt && hasCourseContentFtn() && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasCourseContentBtn") : true);
				  answers[i] = (hasItmMaintainFtn() || hasItmContentMgtFtn()) && hasCourseContentFtn();
            }
			else if (questions[i].equalsIgnoreCase("hasItmAttendanceBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				answers[i] = hasItmMgt && hasItmAttendanceFtn() && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasItmAttendanceBtn") : true);
				//answers[i] = hasItmMgt && (isWizbCos()) && this.itm.itm_life_status == null;
				//answers[i] = hasItmMgt && (isItmRun() || isItmApplicable());
			}
			else if (questions[i].equalsIgnoreCase("hasIadBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				answers[i] = hasItmMgt && hasIadBtn();
			}
			else if (questions[i].equalsIgnoreCase("hasItmAddRunBtn")) {
                /*
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
                */
				if (checkItmPageVariant == null) {
					setItmPageVariant();
				}
				answers[i] = hasItmRunInfoFtn() && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasItmAddRunBtn") : true);
			}
			else if (questions[i].equalsIgnoreCase("hasItmAddSessionBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				answers[i] = hasItmMgt && hasItmSessionInfoFtn();
			}
			else if (questions[i].equalsIgnoreCase("hasItmRunInfoBtn")) {
				answers[i] = hasItmRunInfoFtn();
			}
			else if (questions[i].equalsIgnoreCase("hasItmSessionInfoBtn")) {
				answers[i] = hasItmSessionInfoFtn();
			}
			else if (questions[i].equalsIgnoreCase("hasItmReqApprovalBtn")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("hasReqApprPub")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				answers[i] = hasItmMgt && hasMakeApprovalActnFtn(aeItem.ITM_APPROVAL_ACTION_REQ_APPR);
			}
			else if (questions[i].equalsIgnoreCase("hasCancelReqApprPub")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				answers[i] = hasItmMgt && hasMakeApprovalActnFtn(aeItem.ITM_APPROVAL_ACTION_CANCEL_REQ_APPR);
			}
			else if (questions[i].equalsIgnoreCase("hasApprPub")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				answers[i] = hasItmMgt && hasMakeApprovalActnFtn(aeItem.ITM_APPROVAL_ACTION_APPR_PUB);
			}
			else if (questions[i].equalsIgnoreCase("hasDeclineApprPub")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				answers[i] = hasItmMgt && hasMakeApprovalActnFtn(aeItem.ITM_APPROVAL_ACTION_DECLINE_APPR_PUB);
			}
			else if (questions[i].equalsIgnoreCase("hasPub")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				answers[i] = hasItmMgt && hasMakeApprovalActnFtn(aeItem.ITM_APPROVAL_ACTION_PUB);
			}
			else if (questions[i].equalsIgnoreCase("hasUnpub")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
					hasItmMgtFlag = true;
				}
				answers[i] = hasItmMgt && hasMakeApprovalActnFtn(aeItem.ITM_APPROVAL_ACTION_UNPUB);
			}
			else if (questions[i].equalsIgnoreCase("hasAddCataBtn")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
			}
			else if (questions[i].equalsIgnoreCase("hasAddGlbCataBtn")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
			}
			else if (questions[i].equalsIgnoreCase("hasCataLostFoundLink")) {
				answers[i] =AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
			}
			else if (questions[i].equalsIgnoreCase("hasCataLostFoundRemoveItemBtn")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
			}
			else if (questions[i].equalsIgnoreCase("hasItemDtlLink")) {
				answers[i] = hasItmDetailViewFlag ? hasItmDetailView : true;
				hasItmDetailView = answers[i];
				hasItmDetailViewFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowItemOwner")) {
				answers[i] = hasItmDetailViewFlag ? hasItmDetailView : (true);
				hasItmDetailView = answers[i];
				hasItmDetailViewFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowItemUpdateTime")) {
				answers[i] = hasItmDetailViewFlag ? hasItmDetailView : (true);
				hasItmDetailView = answers[i];
				hasItmDetailViewFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowItemStatus")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowItemQuota")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowItemEnrolled")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowItemTrainingCenter")) {
				answers[i] = tc_enable_ind;
			}
			else if (questions[i].equalsIgnoreCase("noShowItemCount")) {
				answers[i] = dbUtils.isUserLrnRole(con, this.ent_id, this.rol_ext_id);
			}
			else if (questions[i].equalsIgnoreCase("ShowItemLifeStatus")) {
				answers[i] = false;
			}
			else if (questions[i].equalsIgnoreCase("hasEditCataBtn")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
				hasCatMgt = answers[i];
				hasCatMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasAddNodeBtn")) {
				answers[i] = (hasCatMgtFlag) ? hasCatMgt :AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
				hasCatMgt = answers[i];
				hasCatMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasDelCataBtn")) {
				answers[i] = (hasCatMgtFlag) ? hasCatMgt : AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
				hasCatMgt = answers[i];
				hasCatMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowNodeItemType")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowNodeStatus")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("hasAddItemBtn")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW);
			}
			else if (questions[i].equalsIgnoreCase("hasAddSysAnnBtn")) {
				answers[i] = ((hasSysMsgMgtFlag) ? hasSysMsgMgt : AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_MSG_MAIN));
				hasSysMsgMgt = answers[i];
				hasSysMsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasEditSysAnnBtn")) {
				answers[i] = ((hasSysMsgMgtFlag) ? hasSysMsgMgt :  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_MSG_MAIN));
				hasSysMsgMgt = answers[i];
				hasSysMsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasDelSysAnnBtn")) {
				answers[i] = ((hasSysMsgMgtFlag) ? hasSysMsgMgt :  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_MSG_MAIN));
				hasSysMsgMgt = answers[i];
				hasSysMsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasGetAllAnnBtn")) {
				answers[i] = ((hasSysMsgMgtFlag) ? hasSysMsgMgt :  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_MSG_MAIN));
				hasSysMsgMgt = answers[i];
				hasSysMsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("ShowSysAnnStatus")) {
				answers[i] = ((hasSysMsgMgtFlag) ? hasSysMsgMgt :  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_MSG_MAIN));
				hasSysMsgMgt = answers[i];
				hasSysMsgMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasRemoveAppnBtn")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION);
			}
			else if (questions[i].equalsIgnoreCase("hasAddLrnSolnBtn")) {
				answers[i] = (!hasExistLrnSoln()) && isItyApplicable() && isItmCompulsoryOrElectiveOrNull() && (!isUserTargeted()) ;
			}
			else if (questions[i].equalsIgnoreCase("hasAddLrnPlanBtn")) {
				answers[i] =  false;
			}
			else if (questions[i].equalsIgnoreCase("canSelfEnrol")) {
				answers[i] = (isUserManager() || !isItmCompulsory() || !isUserTargeted()) ;
			}
			else if (questions[i].equalsIgnoreCase("hasForAddBtn")) {
				answers[i] = ((hasPubForMgtFlag) ? hasPubForMgt : true);
				hasPubForMgt = answers[i];
				hasPubForMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasForEditBtn")) {
				answers[i] = ((hasPubForMgtFlag) ? hasPubForMgt : hasPubForMgtFtn());
				hasPubForMgt = answers[i];
				hasPubForMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasForDelBtn")) {
				answers[i] = ((hasPubForMgtFlag) ? hasPubForMgt : hasPubForMgtFtn());
				hasPubForMgt = answers[i];
				hasPubForMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasRemoveTopicBtn")) {
				answers[i] = isModModerator();
			}
			else if (questions[i].equalsIgnoreCase("hasRemoveMsgBtn")) {
				answers[i] = isModModerator();
			}
			//For Assessment 
			else if (questions[i].equalsIgnoreCase("hasAddQuizModuleBtn")) {

				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasChgQuizModuleStatus")) {

				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasStartQuizModuleBtn")) {

				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasEditQuizModuleBtn")) {
				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasDelQuizModuleBtn")) {
				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasChgQuizModuleStatusBtn")) {
				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasAddObjectiveBtn")) {
				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasDelObjectiveBtn")) {
				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasAddUsgGrpBtn")) {
				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasDelUsgGrpBtn")) {
				answers[i] = hasModifyQuizModuleBtnFlag ? hasModifyQuizModuleBtn : hasModifyQuizModuleBtnFtn();
				hasModifyQuizModuleBtn = answers[i];
				hasModifyQuizModuleBtnFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("submitTest")) {
				answers[i] = AccessControlWZB.isLrnRole(rol_ext_id);
			}
			else if (questions[i].equalsIgnoreCase("hasEditCcrPeriodBtn")) {
				answers[i] = (hasCcrMgtFlag) ? hasCcrMgt : hasCcrMgtFtn();
				hasCcrMgt = answers[i];
				hasCcrMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasAddCcrBtn")) {
				answers[i] = (hasCcrMgtFlag) ? hasCcrMgt : hasMaintainFtn();
				hasCcrMgt = answers[i];
				hasCcrMgtFlag = true;

			}
			else if (questions[i].equalsIgnoreCase("hasRmCcrBtn")) {
				answers[i] = (hasCcrMgtFlag) ? hasCcrMgt : hasMaintainFtn();
				hasCcrMgt = answers[i];
				hasCcrMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasDelCcrBtn")) {
				answers[i] = (hasCcrMgtFlag) ? hasCcrMgt : hasMaintainFtn();
				hasCcrMgt = answers[i];
				hasCcrMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasEditCcrBtn")) {
				answers[i] = (hasCcrMgtFlag) ? hasCcrMgt : hasMaintainFtn();
				//Christ:change from hasCcrMgtFtn() to hasEditFtn();
				hasCcrMgt = answers[i];
				hasCcrMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasAddFacBtn")) {
				answers[i] = hasAddFacFtn();
			}
			else if (questions[i].equalsIgnoreCase("hasEditFacBtn")) {
				answers[i] = (hasFacMgtFlag) ? hasFacMgt : hasFacMgtFtn();
				hasFacMgt = answers[i];
				hasFacMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasDelFacBtn")) {
				answers[i] = (hasFacMgtFlag) ? hasFacMgt : hasFacMgtFtn();
				hasFacMgt = answers[i];
				hasFacMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasEditRsvBtn")) {
				answers[i] = (hasRsvMgtFlag) ? hasRsvMgt : hasRsvMgtFtn();
				hasRsvMgt = answers[i];
				hasRsvMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasDelRsvBtn")) {
				answers[i] = (hasRsvMgtFlag) ? hasRsvMgt : hasRsvMgtFtn();
				hasRsvMgt = answers[i];
				hasRsvMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasLearningSolnFcn")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("hasItmEditWorkflowBtn")) {
				if (!hasItmMgtFlag) {
					hasItmMgt = hasItmMgtFtn();
				}
				answers[i] = hasItmMgt && hasEditWorkflowTemplateBtn();
			}
			// KB
			else if (questions[i].equalsIgnoreCase("hasKbCategoryAddBtn")) {
				answers[i] = (hasKbAddCategoryFlag) ? hasKbAddCategory : true;
				hasKbAddCategory = answers[i];
				hasKbAddCategoryFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKbCategoryEditBtn")) {
				answers[i] = (hasKbEditCategoryFlag) ? hasKbEditCategory : true;
				hasKbEditCategory = answers[i];
				hasKbEditCategoryFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKbCategoryDelBtn")) {
				answers[i] = (hasKbDelCategoryFlag) ? hasKbDelCategory : true;
				hasKbDelCategory = answers[i];
				hasKbDelCategoryFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKbWorkFolderAddBtn")) {
				answers[i] = (hasKbAddWorkFolderFlag) ? hasKbAddWorkFolder : true;
				hasKbAddWorkFolder = answers[i];
				hasKbAddWorkFolderFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKbWorkFolderAddItemBtn")) {
				answers[i] = true;
				hasKbAddWorkFolder = answers[i];
				hasKbAddWorkFolderFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKbWorkFolderEditBtn")) {
				answers[i] = (hasKbEditWorkFolderFlag) ? hasKbEditWorkFolder : true;
				hasKbEditWorkFolder = answers[i];
				hasKbEditWorkFolderFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKbWorkFolderDelBtn")) {
				answers[i] = (hasKbDelWorkFolderFlag) ? hasKbDelWorkFolder : true;
				hasKbDelWorkFolder = answers[i];
				hasKbDelWorkFolderFlag = true;
			}
			// KM
			else if (questions[i].equalsIgnoreCase("hasKmNodeAddBtn")) {
				answers[i] = (hasKmAddNodeFlag) ? hasKmAddNode :true;
				hasKmAddNode = answers[i];
				hasKmAddNodeFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKmNodeEditBtn")) {
				answers[i] = (hasKmNodeMgtFlag) ? hasKmNodeMgt : true;
				hasKmNodeMgt = answers[i];
				hasKmNodeMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKmNodeDelBtn")) {
				answers[i] = (hasKmNodeMgtFlag) ? hasKmNodeMgt : true;
				hasKmNodeMgt = answers[i];
				hasKmNodeMgtFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjCheckInBtn")) {
				answers[i] = true;
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjCheckOutBtn")) {
				AcKmNode knode = new AcKmNode(this.con, this.ent_id, this.prof.usrGroupsList());
				answers[i] =true; //(baseObj.bob_nature == null || !baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY) );
			}
			else if (questions[i].equalsIgnoreCase("hasKmNodeToWorkPlaceBtn")) {
				answers[i] =true ;
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjPublishBtn")) {
				answers[i] = true; //(baseObj.bob_nature == null || !baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY));
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjDeleteBtn")) {
				answers[i] =true; //(baseObj.bob_nature == null || !baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY));
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjMarkDeleteBtn")) {
				answers[i] = (baseObj.bob_nature != null && baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY) && baseObj.bob_delete_timestamp == null);
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjEditBtn")) {
				answers[i] = (baseObj.bob_nature != null && baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY) && baseObj.bob_delete_timestamp == null);
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjAddToDomainBtn")) {
				answers[i] = (baseObj.bob_nature != null && baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY) && baseObj.bob_delete_timestamp == null);
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjCopyMainBtn")) {
				answers[i] = (baseObj.bob_nature != null && baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY) && baseObj.bob_delete_timestamp == null);
			}
			else if (questions[i].equalsIgnoreCase("hasKmObjChangeFolderBtn")) {
				answers[i] = (baseObj.bob_nature != null && baseObj.bob_nature.equals(KMLibraryObjectManager.KM_NATURE_LIBRARY) && baseObj.bob_delete_timestamp == null);
			}
			else if (questions[i].equalsIgnoreCase("hasItmAddBtn")) {
				answers[i] = (hasItmAddFlag) ? hasItmAdd : (AccessControlWZB.hasRolePrivilege( rol_ext_id,  new String []{AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW,AclFunction.FTN_AMD_EXAM_MAIN_VIEW}) );
				hasItmAdd = answers[i];
				hasItmAddFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasCataOffPrivilege")) {
				answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN);
			}
			else if (questions[i].equalsIgnoreCase("hasEditObjBtn")) {
				answers[i] = (hasEditObjBtnFlag) ? hasEditObjBtn : hasEditAccess();
				hasEditObjBtn = answers[i];
				hasEditObjBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasMoveObjBtn")) {
				hasEditObjBtn = (hasEditObjBtnFlag) ? hasEditObjBtn : hasEditAccess();
				hasEditObjBtnFlag = true;

				if (hasEditObjBtn) {
					isObjRoot = (isObjRootFlag) ? isObjRoot : isObjRoot();
					isObjRootFlag = true;
					answers[i] = !isObjRoot;
				}
				else {
					answers[i] = false;
				}
			}
			else if (questions[i].equalsIgnoreCase("hasRemoveObjBtn")) {
				answers[i] = (hasRemoveObjBtnFlag) ? hasRemoveObjBtn : hasEditAccess();
				hasRemoveObjBtn = answers[i];
				hasRemoveObjBtn = true;
			}
			else if (questions[i].equalsIgnoreCase("hasAddObjBtn")) {
				answers[i] = (hasAddObjBtnFlag) ? hasAddObjBtn : hasEditAccess();
				hasAddObjBtn = answers[i];
				hasAddObjBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasAccessRightBtn")) {
				answers[i] = (hasAccessRightBtnFlag) ? hasAccessRightBtn : hasAccessRight();
				hasAccessRightBtn = answers[i];
				hasAccessRightBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasPasteResBtn")) {
				answers[i] = (hasPasteResBtnFlag) ? hasPasteResBtn : hasEditAccess();
				hasPasteResBtn = answers[i];
				hasPasteResBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasRemoveResBtn")) {
				answers[i] = (hasRemoveResBtnFlag) ? hasRemoveResBtn : hasEditAccess();
				hasRemoveResBtn = answers[i];
				hasRemoveResBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasAddResBtn")) {
				answers[i] = (hasAddResBtnFlag) ? hasAddResBtn : hasEditAccess();
				hasAddResBtn = answers[i];
				hasAddResBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasTurnOnResBtn")) {
				answers[i] = (hasTurnOnResBtnFlag) ? hasTurnOnResBtn : hasEditAccess();
				hasTurnOnResBtn = answers[i];
				hasTurnOnResBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasTurnOffResBtn")) {
				answers[i] = (hasTurnOffResBtnFlag) ? hasTurnOffResBtn : hasEditAccess();
				hasTurnOffResBtn = answers[i];
				hasTurnOffResBtnFlag = true;
			}
			else if (questions[i].equalsIgnoreCase("hasEditResBtn")) {
				answers[i] = (hasEditResBtnFlag) ? hasEditResBtn : hasEditAccess();
				hasEditResBtn = answers[i];
				hasEditResBtnFlag = true;
			}
//			else if (questions[i].equalsIgnoreCase("hasEditLessonBtn")) {
//				if (!hasEditLessonBtnFlag) {
//					hasEditLessonBtn = hasItmMgtFtn();
//					hasEditLessonBtnFlag = true;
//				}
//				if (checkItmPageVariant == null) {
//					setItmPageVariant();
//				}
//				answers[i] = hasEditLessonBtn && (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasEditLessonBtn") : true);
//			}
            //for user import 
            else if (questions[i].equalsIgnoreCase("hasUsrImporBtn")) {
                answers[i] = hasUsrImportBtn();
            }
            else if(questions[i].equalsIgnoreCase("canMaitainUsg")) {
            	answers[i] = canMaitainUsg();
            }else if(questions[i].equalsIgnoreCase("canMaitainUsr")) {
            	answers[i] = canMaitainUsr();
            }
    		else if (questions[i].equalsIgnoreCase("hasEditPlanBtn")) {
				answers[i] = (hasEditPlanBtnFlag) ? hasEditPlanBtn : hasEditPlan();
				hasEditPlanBtn = answers[i];
				hasEditPlanBtnFlag = true;
			}
			
			else if (questions[i].equalsIgnoreCase("hasReferPlanBtn")) {
				answers[i] = (hasReferPlanBtnFlag) ? hasReferPlanBtn : hasEditPlan();
				hasReferPlanBtn = answers[i];
				hasReferPlanBtnFlag = true;
			}
			
			else if (questions[i].equalsIgnoreCase("hasDeletePlanBtn")) {
				answers[i] = (hasDeletePlanBtnFlag) ? hasDeletePlanBtn : hasEditPlan();
				hasDeletePlanBtn = answers[i];
				hasDeletePlanBtnFlag = true;
			}
            
			else if (questions[i].equalsIgnoreCase("hasImplementPlanBtn")) {
				answers[i] = (hasImplementPlanBtnFlag) ? hasImplementPlanBtn : hasImplementPlan();
				hasImplementPlanBtn = answers[i];
				hasImplementPlanBtnFlag = true;
			} else if (questions[i].equalsIgnoreCase("hasApprovedPlanBtn")) {
				answers[i]= hasApprovedPlanBtn();
				hasApprovedPlanBtn = answers[i];
			} else if (questions[i].equalsIgnoreCase("hasCoursesBtn")) {
				hasIntegratedMgt = true;
				answers[i] = hasIntegratedMgt && aeItem.isIntegratedItem(con, instance_id);
			} else if (questions[i].equalsIgnoreCase("hasLcmsOfflinePkgBtn")) {
				//企业版才有离线助手功能
				hasLcmsOfflinePkgBtn = AccessControlWZB.hasRolePrivilege(this.rol_ext_id, LcmsModule.FTN_LCMS_MAIN);
				if(hasLcmsOfflinePkgBtn){
					//必须在配置文件setupadv.xml中配置了lcms 为 TRUE
					hasLcmsOfflinePkgBtn = qdbAction.getWizbini().cfgSysSetupadv.getLcms().isEnabled();
				}
				//还必须是管理员
				answers[i] = hasLcmsOfflinePkgBtn && AccessControlWZB.hasRolePrivilege(this.rol_ext_id, LcmsModule.FTN_LCMS_MAIN);
			} else if (questions[i].equalsIgnoreCase("hasItemAddBtn")) {
			    answers[i] = AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW);
			} else if (questions[i].equalsIgnoreCase("hasShareBtn")){
				//二级培训中心独立去掉分享按钮
				answers[i] = hasShareBtn();
			}
		}
		return formatAnswerAsXML(questions, answers);
	}

	/**
	generate the question and answer as XML
	*/
	public String formatAnswerAsXML(String[] questions, boolean[] answers) {

		StringBuffer xmlBuf = new StringBuffer(512);
		xmlBuf.append("<page_variant ");

		for (int i = 0; i < questions.length; i++) {
			xmlBuf.append(questions[i]).append("=\"").append(answers[i]).append("\" ");
		}

		xmlBuf.append(" />");
		return xmlBuf.toString();
	}

	boolean hasItemCostBtn() throws SQLException {
		boolean flag = false;
		aeItem item = new aeItem();
		item.itm_id = this.instance_id;
		try {
			item.get(con);
			String itm_type = item.itm_type;
			boolean run_ind = item.itm_run_ind;
			ItemCostType costType = null;
			List costAttriList = item.getItemCostAttriList(wizbini, root_id);
			for (int i = 0, n = costAttriList.size(); i < n; i++) {
				costType = (ItemCostType) costAttriList.get(i);
				flag = itm_type.equalsIgnoreCase(costType.getItmType()) && (run_ind == costType.isItmRunInd());
				if (flag)
					break;
			}
		}
		catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		return flag;
	}

	/*
	 * check if the current user has the privilege to edit the projective
	 */
	boolean hasEditAccess() throws SQLException {
	
		return true;
	}

	//not the top level and has the access to edit,the page will has the move button
	boolean isObjRoot() throws SQLException {
		boolean flag;
		//DbObjective obj = new DbObjective();
		flag = dbObjective.isRootObj(con, this.obj_id);
		return flag;
	}

	//the top level will has the access right button;
	boolean hasAccessRight() throws SQLException {
		boolean flag;
		//DbObjective obj = new DbObjective();
		flag = dbObjective.isRootObj(con, this.obj_id);
		return flag;
	}

	boolean hasAddQue() throws SQLException, qdbException {
		boolean canAdd = false;
		dbObjective obj = new dbObjective();
		obj.obj_id = this.obj_id;
		obj.get(con);
		if (obj.obj_status.equalsIgnoreCase(dbObjective.OBJ_STATUS_OK))
			canAdd = true;
		return canAdd;
	}

	/**
	check if the user has add catalog button<BR>
	pre-defined variables:<BR>
	<ul>
	<li>ent_id
	<li>rol_ext_id
	</ul>
	*/
	public boolean hasAddCatalogFtn() throws SQLException {
		
		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_CAT_MAIN) ;

		
	}

	/**
	check if the user has add system announcement button<BR>
	pre-defined variables:<BR>
	<ul>
	<li>ent_id
	<li>rol_ext_id
	</ul>
	*/
	public boolean hasAddSysMessageFtn() throws SQLException {

		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_MSG_MAIN) ;
	}

	/**
	check if the user has edit info button in user manager<BR>
	pre-defined variables:<BR>
	<ul>
	<li>instance_id
	<li>ent_id
	<li>rol_ext_id
	</ul>
	*/
	public boolean hasUsrEditFtn() throws SQLException {
		
		
		boolean result = false;
		if (this.acl == null) {
			this.acl = new AccessControlWZB();
		}
		

			try {
				AccessControlManager acMgr = new AccessControlManager();
				long auth_level = acMgr.getRoleAuthLevel(con, this.ent_owner_ent_id, this.rol_ext_id);
				long target_auth_level = dbRegUser.getUserRoleMinAuthLevel(con, this.instance_id);

				AcRegUser acUsr = new AcRegUser(con);
				if ((AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_USR_INFO_MGT) && auth_level <= target_auth_level)) {
					result = true;
				}
			}
			catch (cwSysMessage e) {
				result = false;
			}
		
		return result;

	}

	/**
	Check if the user status is "DELETED"<BR>
	Pre-define variable:
	<ul>
	<li>instance_id
	</ul>
	*/
	private boolean isUsrDeleted() throws SQLException {
		boolean result;
		dbRegUser usr = new dbRegUser();
		usr.usr_ent_id = this.instance_id;
		usr.ent_id = this.instance_id;
		try {
			usr.get(this.con);
		}
		catch (qdbException e) {
			return true;
		}
		if (usr.usr_status != null && usr.usr_status.equalsIgnoreCase(dbRegUser.USR_STATUS_DELETED)) {
			result = true;
		}
		else {
			result = false;
		}
		return result;
	}

	/**
	check if the user has USR_MGT_ADMIN or USR_MGT_NON_ADMIN function <BR>
	pre-defined variables:<BR>
	<ul>
	<li>ent_id
	<li>rol_ext_id
	</ul>
	*/
	public boolean hasUsrMgtFtn() throws SQLException {

		return  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_USR_INFO) ;
	}

	/**
	Check if the user, role pair has USG_MGT function <BR>
	Pre-defined variables:<BR>
	<ul>
	<li>ent_id
	<li>rol_ext_id
	</ul>
	*/
	public boolean hasUsgMgtFtn() throws SQLException {

		return  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_USR_INFO) ;
	}

	/**
	Check if user, role pair has item management function <BR>
	Pre-defined variables:<BR>
	<ul>
	<li>instance_id
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	</ul>
	*/
	private boolean hasItmMgtFtn() throws SQLException {
        return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW) ;
	}

	private boolean hasItmContentMgtFtn() throws SQLException {
	    return  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_CONTENT) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT) ;
	}
	
	private boolean hasItmApplicationMgtFtn() throws SQLException {
        return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_APPLICATION) ;
    }
	/**
	Check if user, role pair has item management function <BR>
	Pre-defined variables:<BR>
	<ul>
	<li>instance_id
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	</ul>
	*/
	private boolean hasItmMaintainFtn() throws SQLException {

		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW) ;
	}

	/**
	Check if user, role pair has function to maintain attendance and completion criteria <BR>
	Pre-defined variables:<BR>
	<ul>christ
	<li>instance_id
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	</ul>
	*/
	private boolean hasMaintainFtn() throws SQLException {

		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW) ;
	}
	

	// same as hasItmMgtFtn, but no life_status checking
	private boolean hasItmMgtCancelledFtn() throws SQLException {
		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW) ;
	}

	private boolean isItmApproved() throws SQLException {

		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
			}
			return this.itm.isItemApproved(this.con);
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean isItmRsvEnabled() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			return this.itm.hasRsvPropriety(con);
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean isItmRun() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			return this.itm.itm_run_ind;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean isItmSession() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			return this.itm.itm_session_ind;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	/**
	Check if user, role pair has item management function <BR>
	Pre-defined variables:<BR>
	<ul>
	<li>instance_id
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	</ul>
	*/
	private boolean hasItmRunInfoFtn() throws SQLException {
		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW) ;
	}

	/**
	Check if user, role pair has item management function(检查角色是否具有课堂信息按钮) <BR>
	Pre-defined variables:<BR>
	<ul>
	<li>instance_id
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	</ul>
	*/
	private boolean hasItmSessionInfoFtn() throws SQLException {
		//屏蔽课堂信息按钮
		return false;
//		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW) ;
	}

	

	/**
	Check if user, role pair has request item approval function <BR>
	Pre-defined variables:<BR>
	<ul>
	<li>instance_id
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	</ul>
	*/
	private boolean hasItmReqApprovalFtn() throws SQLException {
		return AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_VIEW) ;
	}


	



	/**
	Check if this user group is the root organization<BR>
	Pre-define variable:
	<ul>
	<li>instance_id
	</ul>
	*/
	private boolean isUsgRoot() throws SQLException {
		boolean result;
		//System.out.println("instance_id = " + this.instance_id);
		if (this.instance_id == 0) {
			result = true;
		}
		else {
			dbUserGroup usg = new dbUserGroup();
			usg.usg_ent_id = this.instance_id;
			usg.ent_id = this.instance_id;
			try {
				usg.get(con);
			}
			catch (qdbException e) {
				return false;
			}
			if (usg.usg_role != null && usg.usg_role.equalsIgnoreCase(dbUserGroup.USG_ROLE_ROOT)) {
				result = true;
			}
			else {
				result = false;
			}
		}
		return result;
	}

	/**
	* Check Add Quiz Permission<BR>
	* Pre-Define variable :
	* <ul>
	* <li>instance id</li>
	* </ul>
	*/
	public boolean hasModifyQuizModuleBtnFtn() throws SQLException {

		return true;
	}

	//ent_id, instance_id
	private boolean hasExistLrnSoln() throws SQLException {
		return aeLearningSoln.existSoln(this.con, this.ent_id, this.instance_id, this.ent_owner_ent_id);
	}





	private boolean hasPubForMgtFtn() throws SQLException {
		return true;
	}

	private boolean isModModerator() throws SQLException {
		return true;
	}

	private boolean hasCcrMgtFtn() throws SQLException {
		//        try {
		return hasItmMgtFtn();
		//            AcCourse acCos = new AcCourse(this.con);
		//            return acCos.checkModifyPermission(this.prof, this.instance_id);
		//        }
		//        catch (qdbException e) {
		//            return false;
		//        }
	}

	/**
	Check if has Add Facility button
	Pre-define variable:
	<ul>
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	</ul>
	*/
	private boolean hasAddFacFtn() throws SQLException {

		return true;
	}

	/**
	Check if has Add Facility button
	Pre-define variable:
	<ul>
	<li>ent_id
	<li>rol_ext_id
	<li>ent_owner_ent_id
	<li>instance_id
	</ul>
	*/
	private boolean hasFacMgtFtn() throws SQLException {

		return true;
	}

	/**
	Check if has Add Facility button
	Pre-define variable:
	<ul>
	<li>ent_id
	<li>rol_ext_id
	<li>instance_id
	</ul>
	*/
	private boolean hasRsvMgtFtn() throws SQLException {

		return true;
	}

	private boolean hasItmAttendanceFtn() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
		}
		catch (cwSysMessage e) {
			;
		}

		boolean result;
		if (this.itm.itm_life_status != null) {
			result = false;
		}
		else {
			result = this.itm.itm_has_attendance_ind;
			/*
			if(isItmRun()) {
			    result = true;
			} else {
			    if(this.itm.itm_create_run_ind) {
			        result = false;
			    } else {
			        if(isWizbCos()) {
			            if(this.itm.itm_auto_enrol_qdb_ind) {
			                result = false;
			            } else {
			                result = true;
			            }
			        } else {
			            result = false;
			        }
			    }
			}
			*/
		}
		return result;
		//(isWizbCos()) && this.itm.itm_life_status == null
	}

	private boolean isUserTargeted() throws SQLException {
		boolean result = false;
		dbRegUser usr = new dbRegUser();
		usr.usr_ent_id = this.ent_id;
		try {
			result = usr.amITargeted(con, this.instance_id);
		}
		catch (cwException e) {
			result = false;
		}
		//System.out.println("isUserTargeted = " + result);
		return result;
	}

	/**
	Check if this.ent_id is Manager or Above
	*/
	private boolean isUserManager() throws SQLException {
		boolean result = false;
		dbRegUser usr = new dbRegUser();
		usr.usr_ent_id = this.ent_id;
		result = usr.isManager(con);
		//System.out.println("isUserManager = " + result);
		return result;
	}

	/**
	Check if this item is compulsory
	*/
	private boolean isItmCompulsory() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			//System.out.println("isItmCompulsory = " + this.itm.itm_apply_method.equals(aeItem.ITM_APPLY_METHOD_COMPULSORY));
			return (this.itm.itm_apply_method != null && this.itm.itm_apply_method.equals(aeItem.ITM_APPLY_METHOD_COMPULSORY));
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	/**
	Check if this item is compulsory or elevtive
	*/
	private boolean isItmCompulsoryOrElectiveOrNull() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}

			return (this.itm.itm_apply_method == null || this.itm.itm_apply_method.equals(aeItem.ITM_APPLY_METHOD_COMPULSORY) || this.itm.itm_apply_method.equals(aeItem.ITM_APPLY_METHOD_ELECTIVE));
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean hasItmJIFtn() throws SQLException {

		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			return this.itm.itm_ji_ind;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean hasCompletionCriteriaFtn() throws SQLException {

		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			return this.itm.itm_completion_criteria_ind;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean hasCosContentMgtFtn() throws SQLException {
		return  AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_CONTENT);
		

	}

	private boolean hasCourseContentFtn() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			/*
			if (this.itm.itm_run_ind){
			    if (this.parent == null){
			        this.parent = new aeItem();
			        aeItemRelation ire = new aeItemRelation();
			        ire.ire_child_itm_id = this.itm.itm_id;
			        this.parent.itm_id = ire.getParentItemId(con);
			        if(this.parent.itm_id != 0) {
			            this.parent.getItem(con);
			            return this.parent.itm_qdb_ind;
			        }else{
			            return false;
			        }
			    }else{
			        return this.parent.itm_qdb_ind;
			    }
			}
			*/
			AcItem acitm = new AcItem(con);
			return (this.itm.itm_qdb_ind || this.itm.itm_create_run_ind) && (AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_ITM_COS_MAIN_CONTENT) || AccessControlWZB.hasRolePrivilege( rol_ext_id,  AclFunction.FTN_AMD_EXAM_MAIN_CONTENT));
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean hasItmAttendanceUpdateByCriteriaFtn() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			if (this.itm.itm_session_ind) {
				return false;
			}
			else if (this.itm.itm_create_session_ind) {
				return true;
			}
			else {
				// for unlimited course content 
				if (this.itm.itm_content_eff_duration != 0 || this.itm.itm_content_eff_start_datetime != null || this.itm.itm_content_eff_end_datetime != null) {
					return false;
				}

				long itm_id = aeItem.getRootItemId(con, instance_id);
				DbCourseCriteria ccr = new DbCourseCriteria();
				ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
				ccr.ccr_itm_id = itm_id;
				ccr.getCcrIdByItmNType(con);
				if (ccr.ccr_id != 0) {
					//                  ccr.get(con);
					//                  if (ccr.ccr_upd_method!=null && ccr.ccr_upd_method.equals(DbCourseCriteria.UPD_METHOD_MANUAL)){
					return true;

				}
				else {
					return false;
				}

			}
		}
		catch (cwSysMessage e) {
			return false;
		}

	}

	private boolean hasIadBtn() throws SQLException {
		return false;
	}

	/**
	Check if this item type applicable
	*/
	private boolean isItyApplicable() throws SQLException {
		DbItemType[] dbItys;

		try {
			dbItys = DbItemType.getApplicableItemTypeInOrg(con, this.ent_owner_ent_id);
			boolean result = false;
			if (dbItys == null || dbItys.length == 0) {
				result = false;
			}
			else {
				if (this.itm == null) {
					this.itm = new aeItem();
					this.itm.itm_id = instance_id;
					this.itm.getItem(con);
				}
				for (int i = 0; i < dbItys.length; i++) {
					DbItemType dbIty = dbItys[i];

					if (dbIty.ity_id.equals(this.itm.itm_type)) {
						result = true;
						break;
					}
				}
			}
			return result;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}



	private boolean isUserApprover() throws SQLException {
		return dbUtils.isUserApprRole(this.con, this.ent_id, this.rol_ext_id);
	}

	/**
	* return true if the item has no pending application 
	* and the item has a workflow template linked to it
	* otherwise return false;
	*/
	private boolean hasEditWorkflowTemplateBtn() throws SQLException {

		ViewItemTemplate viewIt = new ViewItemTemplate();
		viewIt.itemId = this.instance_id;
		boolean hasWorkflow = (viewIt.getItemSelectedWorkflowTemplateId(con) > 0);
		//boolean hasPendingAppn = aeApplication.isItemAppnExist(con, this.instance_id, aeApplication.PENDING);
		return (hasWorkflow);
	}



	private boolean hasLrnPlanSelfInitiatedFtn() throws SQLException {
		CommonLog.info("root_id:" + this.root_id);
		return ((LearningPlan) wizbini.cfgOrgLearningPlan.get(this.root_id)).getSelfInitiated().isEnabled();
	}

	private boolean isItmCanCancel() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			return this.itm.itm_can_cancel_ind;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}

	private boolean isItmCancelled() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			if (this.itm.itm_life_status != null && this.itm.itm_life_status.equalsIgnoreCase(aeItem.ITM_LIFE_STATUS_CANCELLED)) {
				return true;
			}
			return false;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}






	private void setItmPageVariant() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			if (this.itm.itm_run_ind || this.itm.itm_session_ind) {
				long rootItmId = aeItem.getRootItemId(con, this.itm.itm_id);
				this.itm.itm_approval_status = aeItem.getApprovalStatus(con, this.itm.itm_id);
			}
			if (this.itm.itm_approval_status == null) {
				checkItmPageVariant = new Boolean(false);
			}
			else {
				checkItmPageVariant = new Boolean(true);
				AcItemPageVariant acItmPV = new AcItemPageVariant(this.con);
				vtItmPageVariant = acItmPV.getItemPageVariant(this.itm.itm_approval_status, this.itm.itm_run_ind, true);
			}
			if (vtItmPageVariant != null) {
				StringBuffer tmp = new StringBuffer();
				for (int i = 0; i < vtItmPageVariant.size(); i++) {
					tmp.append(vtItmPageVariant.elementAt(i) + ", ");
				}
			}
		}
		catch (cwSysMessage e) {
			//e.printStackTrace(System.out);
			CommonLog.error(e.getMessage());
			checkItmPageVariant = new Boolean(true);
			vtItmPageVariant = new Vector();
			return;
		}

	}

	public boolean hasMakeApprovalActnFtn(String action) throws SQLException {
		return true;
	}
    
    private boolean hasUsrImportBtn() throws SQLException {
    	AcUserGroup acUsg = new AcUserGroup(this.con);
		return acUsg.canMaitainUsgInfo(this.ent_id, this.rol_ext_id, this.ent_owner_ent_id, this.instance_id, this.tc_enable_ind);
    }

	public boolean hasCourseLessonFtn() throws SQLException{
		try {
			boolean result = false;
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			if (checkItmPageVariant == null) {
				setItmPageVariant();
			}
			if ((this.itm.itm_apply_ind && this.itm.itm_run_ind)|| this.itm.itm_create_run_ind) {
				result = (checkItmPageVariant.booleanValue() ? vtItmPageVariant.contains("hasCourseLessonBtn") : true);
			}
			else {
				result = false;
			}
			return result;
		}
		catch (cwSysMessage e) {
			return false;
		}
	}
	


	
	private boolean canMaitainUsg() throws SQLException {
		AcUserGroup acUsg = new AcUserGroup(this.con);
		return acUsg.canMaitainUsgInfo(this.ent_id, this.rol_ext_id, this.ent_owner_ent_id, this.instance_id, this.tc_enable_ind);
	}
	
	private boolean canMaitainUsr() throws SQLException {
		AcRegUser acUsr = new AcRegUser(this.con);
		return acUsr.canMaitainUsrInfo(this.ent_id, this.rol_ext_id, this.ent_owner_ent_id, this.instance_id, this.tc_enable_ind);
	}
	
	
	private boolean hasEditPlan() throws SQLException {
		boolean flag = false;
		dbTpTrainingPlan tp = new dbTpTrainingPlan();
		tp.tpn_id = this.instance_id;
		flag = tp.hasImplementAccess(con, false);
		return flag;
	}
	private boolean hasImplementPlan() throws SQLException {
		boolean flag = false;
		dbTpTrainingPlan tp = new dbTpTrainingPlan();
		tp.tpn_id = this.instance_id;
		flag = tp.hasImplementAccess(con, true);
		return flag;
	}
	private boolean hasApprovedPlanBtn() throws SQLException {
		boolean flag = false;
    	AcTrainingCenter actc = new AcTrainingCenter(con);
		if(actc.canMgtTcForTp(prof.usr_ent_id, prof.current_role, prof.root_ent_id, this.tcr_id)) {
			flag = true;
		}
		return flag;
	}

	
	private boolean hasManageTcr() throws SQLException {
		return ViewTrainingCenter.hasManageTcInd(con, ent_id);
	}
	
	private boolean hasShareBtn() throws SQLException {
		try {
			if (this.itm == null) {
				this.itm = new aeItem();
				this.itm.itm_id = instance_id;
				this.itm.getItem(con);
			}
			//如果课程类型为“AUDIOVIDEO”并且二级培训中心独立，或者课程是公开课程，分享按钮将不显示
			if(this.itm.itm_type.equalsIgnoreCase(aeItem.ITM_TYPE_AUDIOVIDEO) || wizbini.cfgSysSetupadv.isTcIndependent()||this.itm.itm_ref_ind){
				return false;
			} else {
				return true;
			}
		}
		catch (cwSysMessage e) {
			return false;
		}
	}
}

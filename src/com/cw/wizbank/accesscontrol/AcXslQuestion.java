package com.cw.wizbank.accesscontrol;

import java.util.Hashtable;
import java.util.Enumeration;

/**
This class is to define stylesheet questions.
*/
public class AcXslQuestion {
    
    private static Hashtable xslQuestions = null;
    /**
    Get Xsl Questions of the input xsl_name from the input xslQuestions<BR>
    If xsl_name not found as a key of xslQuestions, get the first questions that 
    it's key is contained in the xsl_name
    */
    public static String[] getOneXslQuestions(String xsl_name, Hashtable xslQuestions) {
        
        String[] oneXslQuestions = null;
        if(xslQuestions == null) {
            oneXslQuestions = null;
        } else {
            oneXslQuestions = (String []) xslQuestions.get(xsl_name);
            if(oneXslQuestions == null) {
                Enumeration keys = xslQuestions.keys();
                while(keys.hasMoreElements()) {
                    String key = (String)keys.nextElement();
                    if(key != null && xsl_name.indexOf(key) > -1) {
                        oneXslQuestions = (String []) xslQuestions.get(key);
                        break;
                    }
                }
            }
        }
        return oneXslQuestions;
    }
    
    /**
    Build a Hashtable that will store the questions for stylesheet. The Hashtable will use stylesheet file name as key
    @return a Hashtable that contains questions as String[]
    */
    public static Hashtable getQuestions() {
        if (xslQuestions == null) {
            xslQuestions = new Hashtable();
    
            //put the xsl and question into Hashtable
            xslQuestions.put("usr_manager.xsl", new String[] {"hasUsrAddBtn",
                                                              "hasUsgAddBtn",
                                                              "hasUsgPasteBtn",
                                                              "hasUsgCopyBtn",
                                                              "hasUsgEditBtn",
                                                              "hasUsgDelBtn",
                                                              "hasUsgLostFoundLink",
                                                              "hasUsrBatchUploadTag",
                                                              "hasUsrReactivateTab", 
    														  "hasUsrApprovalTab",
                                                              "hasUsrImporBtn",
                                                              "hasUsrGradeMgtTab",
                                                              "canMaitainUsg",
                                                              "hasUsrPositionMgtTab"});
    
            xslQuestions.put("usr_reg_approval_lst.xsl", new String[] {"hasUsrBatchUploadTag",
                                                                       "hasUsrReactivateTab",
    																   "hasUsrApprovalTab",
    																   "hasUsrGradeMgtTab",
    																   "hasUsrPositionMgtTab"});
    
            xslQuestions.put("usr_reactivate_lst.xsl", new String[] {"hasUsrBatchUploadTag",
    																 "hasUsrReactivateTab",
    																 "hasUsrApprovalTab",
    																 "hasUsrGradeMgtTab",
    																 "hasUsrPositionMgtTab"});
    
    		xslQuestions.put("ugr_tree.xsl", new String[] {"hasUsrAddBtn",
    																  "hasUsgAddBtn",
    																  "hasUsgPasteBtn",
    																  "hasUsgCopyBtn",
    																  "hasUsgEditBtn",
    																  "hasUsgDelBtn",
    																  "hasUsgLostFoundLink",
    																  "hasUsrBatchUploadTag",
    																  "hasUsrReactivateTab", 
    																  "hasUsrApprovalTab",
    																  "hasUsrPositionMgtTab"});
    		
    		xslQuestions.put("usr_position_list.xsl", new String[] {"hasUsrBatchUploadTag",
																	 "hasUsrReactivateTab",
																	 "hasUsrApprovalTab",
																	 "hasUsrGradeMgtTab",
																	 "hasUsrPositionMgtTab"});
    
            xslQuestions.put("usr_detail.xsl", new String[] {"hasUsrCopyBtn",
                                                             "hasUsrEditBtn",
                                                             "hasUsrDelBtn",
                                                             "hasUsrTrashBtn",
                                                             "canMaitainUsr"});    
    
            xslQuestions.put("usr_del_detail.xsl", new String[] {"hasUsrCopyBtn",
                                                                 "hasUsrDelBtn"});    
    
            xslQuestions.put("usr_search_result.xsl", new String[] {"hasUsrCopyBtn"});
            
            xslQuestions.put("attendance_lst.xsl", new String[] {});
    
             //for classroom
            xslQuestions.put("itm_details.xsl", new String[] {"hasItmEditBtn",
                                                             "hasItmOnOffBtn",
                                                             "hasItmRunInfoBtn",
                                                             "hasItmSessionInfoBtn",
    //                                                         "hasItmNotifyBtn",
                                                             "hasItmReqApprovalBtn",
                                                             "hasItmProcEnrolBtn",
                                                             "hasItmCancelCourseBtn",
                                                             "hasItmCancelRunBtn",
                                                             "hasItmCancelSessionBtn",
                                                             "hasItmBookSystemBtn",
                                                             "hasItmJIBtn",
                                                             "hasCourseContentBtn", 
                                                             "hasCompletionCriteriaBtn", 
                                                             //"hasItmAttendanceBtn",
                                                             "hasItmCostBtn",
                                                             "hasItmPrerequisiteBtn",
                                                             "hasItmDelBtn",
                                                             "hasIadBtn",
                                                             "hasItmEditWorkflowBtn",
                                                             "hasItmRequirementBtn",
                                                             "hasCataOffPrivilege",
                                                             "hasReqApprPub",
                                                             "hasCancelReqApprPub",
                                                             "hasApprPub",
                                                             "hasDeclineApprPub",
                                                             "hasPub",
                                                             "hasUnpub",
                                                             "hasCoursesBtn",
                                                             "hasCourseLessonBtn",
                                                             "hasItmPerformanceBtn",
                                                             "hasLcmsOfflinePkgBtn", 
                                                             "hasShareBtn"
    														 });
            //for class
            xslQuestions.put("itm_run_view.xsl", new String[] {"hasItmEditBtn",
                                                               "hasItmOnOffBtn",
                                                               "hasItmRunInfoBtn",
                                                               "hasItmSessionInfoBtn",
    //                                                           "hasItmNotifyBtn",
    			                                               "hasItmCostBtn",
                                                               "hasItmReqApprovalBtn",
                                                               "hasItmProcEnrolBtn",
                                                               "hasItmCancelRunBtn",
                                                               "hasItmCancelSessionBtn",
                                                               "hasItmBookSystemBtn",
                                                               "hasItmJIBtn",
                                                               "hasCourseContentBtn", 
                                                               "hasCompletionCriteriaBtn", 
                                                               "hasItmAttendanceBtn",
                                                               "hasItmDelBtn",
                                                               "hasIadBtn",
    														   "hasItmRequirementBtn",
                                                               "hasRunAccreditationBtn",
    			                                               "hasItmPrerequisiteBtn",
                                                               "hasCataOffPrivilege",
                                                               "hasCourseLessonBtn"
                                                               });
    
            xslQuestions.put("itm_run_view_cancel.xsl", new String[] {"hasItmEditBtn",
                                                                      "hasItmOnOffBtn",
                                                                      "hasItmRunInfoBtn",
                                                                      "hasItmSessionInfoBtn",
    //                                                                  "hasItmNotifyBtn",
                                                                      "hasItmReqApprovalBtn",
                                                                      "hasItmProcEnrolBtn",
                                                                      "hasItmCancelRunBtn",
                                                                      "hasItmCancelSessionBtn",
                                                                      "hasItmBookSystemBtn",
                                                                      "hasItmJIBtn",
                                                                       "hasCompletionCriteriaBtn", 
                                                                       "hasItmDelBtn",
                                                                      "hasItmAttendanceBtn"});
    
            xslQuestions.put("itm_approved_detail.xsl", new String[] {"hasItmEditBtn",
                                                                      "hasItmOnOffBtn",
                                                                      "hasItmRunInfoBtn",
                                                                      "hasItmSessionInfoBtn",
    //                                                                  "hasItmNotifyBtn",
                                                                      "hasItmReqApprovalBtn",
                                                                      "hasItmEnrolBtn"});
    
            xslQuestions.put("ae_get_content_info.xsl", new String[] {"hasCompletionCriteriaBtn","hasItmAttendanceBtn"});
    
    		xslQuestions.put("ae_get_instr_content_info.xsl", new String[] {"hasCompletionCriteriaBtn"});
    
            xslQuestions.put("catalog_lst.xsl", new String[] {"hasAddCataBtn",
                                                              "hasCataLostFoundLink"});
    
            xslQuestions.put("glb_catalog_lst.xsl", new String[] {"hasAddGlbCataBtn"});
            /*
            xslQuestions.put("orphan_itm_lst.xsl", new String[] {"hasLostFindRemoveItemBtn",
                                                                 "hasLstviewItemDtlLink",
                                                                 "hasLstviewLrnerItemDtlLink"});
            */
            xslQuestions.put("itm_node_lst.xsl", new String[] {"hasEditCataBtn",
                                                               "hasAddNodeBtn",
                                                               "hasDelCataBtn",
                                                               "ShowNodeItemType",
                                                               "ShowNodeStatus",
                                                               "hasItemDtlLink",
                                                               "ShowItemStatus",
                                                               "noShowItemCount"});
    
            xslQuestions.put("glb_itm_node_lst.xsl", new String[] {"hasEditCataBtn",
                                                               "hasAddNodeBtn",
                                                               "hasDelCataBtn",
                                                               "ShowNodeItemType",
                                                               "ShowNodeStatus",
                                                               "hasItemDtlLink",
                                                               //"hasAddItemBtn",
                                                               "ShowItemStatus"});
    
            xslQuestions.put("announ_lst.xsl", new String[] {"hasAddSysAnnBtn",
                                                             "hasDelSysAnnBtn",
                                                             "hasEditSysAnnBtn",
                                                             "ShowSysAnnStatus"});
    
            xslQuestions.put("announ_lst_popup.xsl", new String[] {"hasAddSysAnnBtn",
                                                                   "hasDelSysAnnBtn",
                                                                   "hasEditSysAnnBtn",
                                                                   "ShowSysAnnStatus"});
                                                             
            xslQuestions.put("gen_ann.xsl", new String[] {"hasAddSysAnnBtn",
                                                          "hasDelSysAnnBtn",
                                                          "hasEditSysAnnBtn",
                                                          "hasGetAllAnnBtn"});
    
            xslQuestions.put("application_lst.xsl", new String[] {"hasRemoveAppnBtn"});
    
            xslQuestions.put("appr_application_lst.xsl", new String[] {"hasItemDtlLink"});
    
            xslQuestions.put("itm_search_result.xsl", new String[] {"hasItemDtlLink",
                                                                    "ShowItemStatus",
                                                                    "ShowItemLifeStatus",
                                                                    "ShowItemOwner",
                                                                    "ShowItemUpdateTime",
                                                                    "ShowItemTrainingCenter",
                                                                    "ShowItemQuota",
                                                                    "hasItemAddBtn",
                                                                    "ShowItemEnrolled"});
    		xslQuestions.put("itm_search_result_main.xsl", new String[] {"hasItemDtlLink",
    																		"ShowItemStatus",
    																		"ShowItemLifeStatus",
    																		"ShowItemOwner",
    																		"ShowItemUpdateTime"});
            
            xslQuestions.put("orphan_itm_lst.xsl", new String[] {"hasItemDtlLink",
                                                                 "hasCataLostFoundRemoveItemBtn"});
            
            xslQuestions.put("rpt_cos_res.xsl", new String[] {"hasItemDtlLink"});
    
            xslQuestions.put("rpt_cos_track_res.xsl", new String[] {"hasItemDtlLink"});
    
            xslQuestions.put("yunnan_rpt_cos_track_res.xsl", new String[] {"hasItemDtlLink"});
    
            // [2002-07-31] Added a new variant , hasAddLrnPlanBtn, which replace hasAddLrnSolnBtn
            // The hasAddLrnPlanBtn is the same as hasAddLrnSolnBtn  
            // except checking for the applicable of an item
            xslQuestions.put("itm_lrn_details.xsl", new String[] {"hasAddLrnSolnBtn",
                                                                  "canSelfEnrol", 
                                                                  "hasAddLrnPlanBtn"});
            
            xslQuestions.put("itm_run_lst.xsl", new String[] {"hasItmAddRunBtn"});
            xslQuestions.put("itm_session_lst.xsl", new String[] {"hasItmAddSessionBtn"});
            
            xslQuestions.put("forum_maintain.xsl", new String[] {"hasForAddBtn",
                                                                 "hasForEditBtn",
                                                                 "hasForDelBtn"});
            
            xslQuestions.put("forum.xsl", new String[] {"hasRemoveTopicBtn"});
    
            xslQuestions.put("forum_search_top_result.xsl", new String[] {"hasRemoveTopicBnt"});
            
            xslQuestions.put("forum_view_msg.xsl", new String[] {"hasRemoveMsgBtn"});
            
            
            xslQuestions.put("quiz_module_lst.xsl", new String[] { "hasAddQuizModuleBtn",
                                                                   "hasChgQuizModuleStatus",
                                                                   "hasStartQuizModuleBtn"});
            /*
            xslQuestions.put("home.xsl", new String[] { "hasAddQuizModuleBtn",
                                                        "hasChgQuizModuleStatus",
                                                        "hasStartQuizModuleBtn"});
            */
                                                                   
            xslQuestions.put("quiz_module.xsl", new String[] { "hasEditQuizModuleBtn",
                                                               "hasDelQuizModuleBtn",
                                                               "hasChgQuizModuleStatusBtn",
                                                               "hasAddObjectiveBtn",
                                                               "hasDelObjectiveBtn",
                                                               "hasAddUsgGrpBtn",
                                                               "hasDelUsgGrpBtn" });
    
            xslQuestions.put("tst_player1.xsl", new String[] {"submitTest"});
            xslQuestions.put("tst_player2.xsl", new String[] {"submitTest"});
            xslQuestions.put("tst_player3.xsl", new String[] {"submitTest"});
            xslQuestions.put("tst_player4.xsl", new String[] {"submitTest"});
            xslQuestions.put("tst_player_utils.xsl", new String[] {"submitTest"});
            xslQuestions.put("flash_player.xsl", new String[] {"submitTest"});
            xslQuestions.put("magic.xsl", new String[] {"submitTest"});
            xslQuestions.put("rocket.xsl", new String[] {"submitTest"});
            xslQuestions.put("joker.xsl", new String[] {"submitTest"});
            xslQuestions.put("tst_view_many.xsl", new String[] {"submitTest"});
            
            xslQuestions.put("crit_lst.xsl", new String[] { "hasAddCcrBtn",
                                                            "hasEditCcrBtn",
                                                            "hasRmCcrBtn",
                                                            "hasDelCcrBtn",
                                                            "hasEditCcrPeriodBtn"});
    
                                                            
    //        xslQuestions.put("crit_detail.xsl", new String[] { "hasEditCcrPeriodBtn"});
            
            xslQuestions.put("crit_mod_lst.xsl", new String[] { "hasAddCcrBtn",
                                                            "hasEditCcrBtn",
                                                            "hasRmCcrBtn",
                                                            "hasDelCcrBtn"});
    
            xslQuestions.put("fm_facility_list.xsl", new String[] { "hasAddFacBtn" });
    
            xslQuestions.put("fm_facility_record", new String[] { "hasEditFacBtn",
                                                                   "hasDelFacBtn" });
    
            xslQuestions.put("fm_rsv_details.xsl", new String[] { "hasEditRsvBtn",
                                                                  "hasDelRsvBtn" });
    
            xslQuestions.put("gen_catalog.xsl", new String[] { "hasItemDtlLink" });
    
            xslQuestions.put("forum", new String[] { "temp" });
            
            xslQuestions.put("itm_lst.xsl", new String[] { "ShowItemStatus", 
                                                           "ShowItemLifeStatus",
                                                           "hasItmAddBtn" });
    
            xslQuestions.put("appr_application_lst.xsl", new String[] { "hasItemDtlLink" });
    
            xslQuestions.put("enrol_assignment_cos_lst.xsl", new String[] { "hasItemDtlLink" });
    
            xslQuestions.put("application_form_confirm.xsl", new String[] { "hasLearningSolnFcn" });
            
            xslQuestions.put("application_simple_form.xsl", new String[] { "hasLearningSolnFcn" });
    
            xslQuestions.put("lrn_application_process_status.xsl", new String[] { "hasLearningSolnFcn" });
    
            xslQuestions.put("km_domain_lst.xsl", new String[] {"hasKmNodeAddBtn","hasKbCategoryAddBtn"});
    
            xslQuestions.put("km_work_lst.xsl", new String[] {"hasKmNodeAddBtn","hasKbWorkFolderAddBtn"});
    
            xslQuestions.put("km_domain_node_lst.xsl", new String[] {"hasKmNodeAddBtn",
                                                              "hasKmNodeEditBtn",
                                                              "hasKmNodeDelBtn",
                                                              "hasKmNodeToWorkPlaceBtn",
                                                              "hasKbCategoryAddBtn",
                                                              "hasKbCategoryEditBtn",
                                                              "hasKbCategoryDelBtn"});
    
            xslQuestions.put("km_work_node_lst.xsl", new String[] {"hasKmNodeAddBtn",
                                                              "hasKmNodeEditBtn",
                                                              "hasKmNodeDelBtn",
                                                              "hasKmNodeToWorkPlaceBtn",
                                                              "hasKbWorkFolderAddBtn",
                                                              "hasKbWorkFolderAddItemBtn",
                                                              "hasKbWorkFolderEditBtn",
                                                              "hasKbWorkFolderDelBtn"});
    
            xslQuestions.put("km_obj_adm_details.xsl", new String[] {"hasKmObjCopyMainBtn",
                                                                "hasKmObjCheckInBtn",
                                                                "hasKmObjCheckOutBtn",
                                                                "hasKmObjEditBtn",
                                                                "hasKmObjPublishBtn",
                                                                "hasKmObjAddToDomainBtn",
                                                                "hasKmObjChangeFolderBtn",
                                                                "hasKmObjDeleteBtn",
                                                                "hasKmObjMarkDeleteBtn"});
    
            xslQuestions.put("km_lib_domain_lst.xsl", new String[] {"hasKmNodeAddBtn"});
    
            xslQuestions.put("km_lib_work_lst.xsl", new String[] {"hasKmNodeAddBtn"});
    
            xslQuestions.put("km_lib_domain_node_lst.xsl", new String[] {"hasKmNodeAddBtn",
                                                              "hasKmNodeEditBtn",
                                                              "hasKmNodeDelBtn",
                                                              "hasKmNodeToWorkPlaceBtn"});
    
            xslQuestions.put("km_lib_work_node_lst.xsl", new String[] {"hasKmNodeAddBtn",
                                                              "hasKmNodeEditBtn",
                                                              "hasKmNodeDelBtn",
                                                              "hasKmNodeToWorkPlaceBtn"});
    
            xslQuestions.put("km_lib_obj_adm_details.xsl", new String[] {"hasKmObjCopyMainBtn",
                                                                "hasKmObjCheckInBtn",
                                                                "hasKmObjCheckOutBtn",
                                                                "hasKmObjEditBtn",
                                                                "hasKmObjPublishBtn",
                                                                "hasKmObjAddToDomainBtn",
                                                                "hasKmObjChangeFolderBtn",
                                                                "hasKmObjDeleteBtn",
                                                                "hasKmObjMarkDeleteBtn"});
                                                                
            xslQuestions.put("home.xsl", new String[] {"hasAddSysAnnBtn", 
                                                       "hasDelSysAnnBtn",
                                                       "hasEditSysAnnBtn",
                                                       "hasGetAllAnnBtn"});        
     
    	xslQuestions.put("res_obj_node_lst.xsl", new String[] {"hasEditObjBtn",
                                                                   "hasMoveObjBtn",
                                                                   "hasRemoveObjBtn",
                                                                   "hasAddObjBtn",
                                                                   "hasAccessRightBtn" });
                                                                   
            xslQuestions.put("tst_info_content.xsl", new String[] { "hasAddQueBtn" });
            
            xslQuestions.put("res_inst.xsl", new String[] {"hasAddResBtn",
                                                           "hasRemoveResBtn",
                                                           "hasPasteResBtn",
                                                           "hasTurnOnResBtn",
                                                           "hasTurnOffResBtn" });
                                                           
            xslQuestions.put("res_lst.xsl", new String[] {"hasAddResBtn",
                                                          "hasRemoveResBtn",
                                                          "hasPasteResBtn",
                                                          "hasTurnOnResBtn",
                                                          "hasTurnOffResBtn" });
                                                          
            xslQuestions.put("que_get.xsl",new String[]{"hasEditResBtn","hasRemoveResBtn"});
            
            xslQuestions.put("res_get.xsl",new String[]{"hasEditResBtn","hasRemoveResBtn"});
    		
            xslQuestions.put("res_srh_que_ind.xsl",new String[]{"hasEditResBtn","hasRemoveResBtn"});
            
            xslQuestions.put("res_srh_res_ind.xsl",new String[]{"hasEditResBtn","hasRemoveResBtn"});
            
            xslQuestions.put("res_srh_aicc_ind.xsl",new String[]{"hasEditResBtn","hasRemoveResBtn"});
            
            xslQuestions.put("res_srh_sco_ind.xsl",new String[]{"hasEditResBtn","hasRemoveResBtn"});
            
            xslQuestions.put("res_srh_netg_ind.xsl",new String[]{"hasEditResBtn","hasRemoveResBtn"});
            
//            xslQuestions.put("ae_get_course_lesson_info.xsl", new String[]{"hasEditLessonBtn"});
//            
//            xslQuestions.put("ae_get_run_lesson_info.xsl", new String[]{"hasEditLessonBtn"});
    
    		xslQuestions.put("run_search_result_main.xsl", new String[] {"hasItmEditBtn",
    														             "hasItmProcEnrolBtn",
    														             "hasCourseContentBtn"});
    
    		xslQuestions.put("training_plan_detail.xsl", new String[] {"hasEditPlanBtn",
    														           "hasReferPlanBtn",
    														           "hasDeletePlanBtn",
    														           "hasImplementPlanBtn",
    														           "hasApprovedPlanBtn"});
        }
        return xslQuestions;
    }
}
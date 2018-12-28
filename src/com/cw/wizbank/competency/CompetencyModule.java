package com.cw.wizbank.competency;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbCmAssessment;
import com.cw.wizbank.db.DbCmAssessmentUnit;
import com.cw.wizbank.db.DbCmSkill;
import com.cw.wizbank.db.DbCmSkillBase;
import com.cw.wizbank.db.DbCmSkillEntity;
import com.cw.wizbank.db.DbCmSkillSet;
import com.cw.wizbank.db.DbCmSkillSetCoverage;
import com.cw.wizbank.db.DbCmSkillTreeNode;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.DbUserSkillSet;
import com.cw.wizbank.db.view.ViewCmAssessment;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.db.view.ViewCmSkill;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class CompetencyModule extends ServletModule {
    
    public static final String MODULENAME = "competency";
    
    public static final String SESS_COMPETENCY_SKILL_CHILD_TS
                                                            = "competency_skill_child_ts";
    public static final String SESS_COMPETENCY_SKILL_CHILD_LIST 
                                                            = "competency_skill_child_list";
    public static final String SESS_COMPETENCY_NODE_LIST    = "competency_node_list";
    public static final String SESS_COMPETENCY_SCALE_LIST   = "competency_scale_list";
    
    public static final String SMSG_CMP_INS_SKILL           = "GEN004";
	public static final String SMSG_CMP_UPD_SKILL           = "GEN003";
    public static final String SMSG_CMP_DEL_SKILL           = "GEN002";
    public static final String SMSG_CMP_INS_SCALE           = "GEN004";
    public static final String SMSG_CMP_UPD_SCALE           = "GEN003";
    public static final String SMSG_CMP_DEL_SCALE           = "GEN002";
    public static final String SMSG_CMP_INS_SKILLSET        = "GEN004";
    public static final String SMSG_CMP_UPD_SKILLSET        = "GEN003";
    public static final String SMSG_CMP_DEL_SKILLSET        = "GEN002";
    public static final String SMSG_CMP_INS_SVY             = "GEN004";
    public static final String SMSG_CMP_UPD_SVY             = "GEN003";
    public static final String SMSG_CMP_DEL_SVY             = "GEN002";
    public static final String SMSG_CMP_INS_ASS             = "GEN004";
    public static final String SMSG_CMP_UPD_ASS             = "GEN003";
    public static final String SMSG_CMP_DEL_ASS             = "GEN002";
	
	public static final String SMSG_CMP_INS_COMP_GROUP		= "GEN004";
	public static final String SMSG_CMP_UPD_COMP_GROUP		= "GEN003";
	public static final String SMSG_CMP_DEL_COMP_GROUP		= "GEN002";
	public static final String SMSG_CMP_INS_COMP_SKILL		= "GEN004";
	public static final String SMSG_CMP_UPD_COMP_SKILL		= "GEN003";
	public static final String SMSG_CMP_DEL_COMP_SKILL		= "GEN002";
    
    public static final String SMSG_CMP_INVALID_TIMESTAMP   = "GEN006";
    


    public static final String SMSG_CMP_TRASH_SKILL         = "CMP001";
    public static final String SMSG_CMP_NODE_NOT_EMPTY      = "CMP002";
    public static final String SMSG_CMP_SKILL_USED          = "CMP003";
    public static final String SMSG_CMP_SCALE_USED          = "CMP004";
    public static final String SMSG_CMP_SVY_USED            = "CMP005";
    public static final String SMSG_CMP_ASS_STARTED         = "CMP006";    
    public static final String SMSG_CMP_ASS_SUBMITED        = "CMP007";
    public static final String SMSG_CMP_ASS_SUBMIT_SUCCESS  = "CMP008";
    public static final String SMSG_CMP_ASS_SAVE_SUCCESS    = "CMP009";
    public static final String SMSG_CMP_ASS_NOT_STARTED_YET = "CMP010";
    public static final String SMSG_CMP_ASS_EXCEED_DUE_DATE = "CMP011";
    public static final String SMSG_CMP_ASS_DELETED         = "CMP012";
    public static final String SMSG_CMP_NOT_EXISTED         = "CMP013";
    public static final String SMSG_CMP_SCALE_NOT_INHERIT   = "CMP014";
    public static final String SMSG_CMP_SCALE_NOT_CHANGEABLE= "CMP015";
    public static final String SMSG_CMP_SKILL_OR_SUCCESSOR_IN_USE = "CMP016";
    public static final String SMSG_CMP_INVALID_NOTIFICATION_DATE = "CMP017";
    public static final String SMSG_CMP_INVALID_COLLECTION_DATE = "CMP018";
    public static final String SMSG_CMP_SKILL_NOT_EMPTY      = "CMP019";
    public static final String SMSG_CMP_SCALE_IN_USED       = "CMP020";
    public static final String SMSG_CMP_GROUP_NAME_EXIST       = "CMP021";
	public static final String SMSG_CMP_NAME_EXIST       = "CMP022";

    
    public static final String DELIMITER                    =   "~";    
    public static final String HASH_SKILL_SET_TITLE         =   "skill_set_title";
    public static final String HASH_SKILL_LEVEL_LIST        =   "skill_level_list";
    public static final String HASH_SKILL_ID_LIST           =   "skill_id_list";
    public static final String HASH_SKILL_PRIORITY_LIST     =   "skill_priority_list";
    public static final String HASH_SKILL_COMMENT_IND       =   "skill_set_comment_ind";
    public static final String HASH_ENTITY_ID               =   "entity_id";
    public static final String HASH_SURVEY_ID               =   "survey_id";
    public static final String HASH_ASSESSMENT_TITLE        =   "assessment_title";
    public static final String HASH_REVIEW_START_DATETIME   =   "review_start";
    public static final String HASH_REVIEW_END_DATETIME     =   "review_end";
    public static final String HASH_NOTIFICATION_DATETIME   =   "notification_date";
    public static final String HASH_DUE_DATETIME            =   "due_date";    
    public static final String SKILL_TEMPLATE               =   "SKT";
    public static final String DELETED                      =   "DELETED";
    
    public void process() throws SQLException, IOException, cwException {

        //String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);
        HttpSession sess = request.getSession(false);

        CompetencyReqParam urlp = null;

        urlp = new CompetencyReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
    
            // if all command need authorized users
            if (prof == null) {
            	
            	if( urlp.cmd.equalsIgnoreCase("assessment_notify_xml") ){
            		urlp.assessmentNotify();
            		
            		try{
            			String xml = CmAssessmentNotify.getAssessmentNotifyXML(con, urlp.sender_usr_id, urlp.usr_ent_id, urlp.asm_id, static_env.MAIL_ACCOUNT, static_env.DES_KEY);
            			out.println(xml.trim());
            		}catch(Exception e) {
            			CommonLog.error(e.getMessage(),e);
            			out.println("FAILED");
            		}            		
            		return;
            		
            	} else 
                	response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            
            }else if (urlp.cmd.equalsIgnoreCase("GET_PROF") ||
                      urlp.cmd.equalsIgnoreCase("GET_PROF_XML")) {
                String xml = formatXML(new String(), MODULENAME); 
                
                if (urlp.cmd.equalsIgnoreCase("GET_PROF")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            // Testing identity column 
            }else if (urlp.cmd.equalsIgnoreCase("DO_ID_test")) {
                //System.out.println("before doing id test");
                //ViewCmSkill.do_id_test(con);
                //System.out.println("after doing id test");
                con.commit();
            
            }else if (urlp.cmd.equalsIgnoreCase("INS_NODE")) {
                out.println("System error: ins_node has been retired ");
                /*
                urlp.getNode();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.node = urlp.node;
                skillmgr.insNode(con, prof.usr_id, prof.root_ent_id);
                con.commit();

                response.sendRedirect(urlp.url_success);
                */
            }else if (urlp.cmd.equalsIgnoreCase("UPD_NODE")) {
                out.println("System error: upd_node has been retired ");
                /*
                urlp.getNode();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.node = urlp.node;
                skillmgr.updNode(con, prof.usr_id);
                con.commit();
                
                response.sendRedirect(urlp.url_success);
                */
            }else if (urlp.cmd.equalsIgnoreCase("DEL_NODE")) {
                out.println("System error: del_node has been retired ");
                /*
                urlp.getNode();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.node = urlp.node;
                skillmgr.delNode(con);
                con.commit();
                
                response.sendRedirect(urlp.url_success);
                */
            }else if (urlp.cmd.equalsIgnoreCase("PASTE_NODE")) {
                out.println("System error: paste_node has been retired ");
                /*
                urlp.getNode();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.node = urlp.node;
                skillmgr.pasteNode(con, prof.usr_id);
                con.commit();
                
                response.sendRedirect(urlp.url_success);
                */
            }else if (urlp.cmd.equalsIgnoreCase("GET_NODE") || 
                      urlp.cmd.equalsIgnoreCase("GET_NODE_XML")) {
                out.println("System error: get_node has been retired ");
                /*
                urlp.getNode();
                urlp.node.get(con);
                String xml = formatXML(urlp.node.asXML(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_NODE")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
                */
            }else if (urlp.cmd.equalsIgnoreCase("GET_NODE_LIST") || 
                      urlp.cmd.equalsIgnoreCase("GET_NODE_LIST_XML")) {
                
                out.println("System error: get_node_list has been retired ");
                /*
                urlp.getNode();
                urlp.getOrder();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.node = urlp.node;
                skillmgr.node.skb_owner_ent_id = prof.root_ent_id;

                // For pick skill in insert and update the skill set
                // append the picked skils in the xml
                String xml = skillmgr.getNodeListAsXML(con, sess, urlp.cur_page, urlp.pagesize, urlp.pagetime);
                if( sess != null) {
                    Hashtable data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                    if( data != null ) {
                        StringBuffer pickedSkillXML = new StringBuffer();
                        //long[] skb_id = (long[])data.get(HASH_SKILL_ID_LIST);
                        Vector vec = (Vector)data.get(HASH_SKILL_ID_LIST);
                        pickedSkillXML.append("<picked_skills>").append(cwUtils.NEWL);
                        for(int i=0; i<vec.size(); i++)
                            pickedSkillXML.append("<skill id=\"").append(vec.elementAt(i)).append("\" />").append(cwUtils.NEWL);
                        pickedSkillXML.append("</picked_skills>").append(cwUtils.NEWL);
                        xml += pickedSkillXML.toString();
                    }
                }
                
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_NODE_LIST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            */
            } else if( urlp.cmd.equalsIgnoreCase("PREP_INS_COMP_GROUP") ||
            			urlp.cmd.equalsIgnoreCase("PREP_INS_COMP_GROUP_XML")) {
            	
				CmSkillManager skillMgr = new CmSkillManager();
				String metaXml = skillMgr.getSkillReferenceDataAsXML(con, prof.root_ent_id, null);
				String xml = formatXML("", metaXml, MODULENAME);
				if (urlp.cmd.equalsIgnoreCase("PREP_INS_COMP_GROUP")) {
					generalAsHtml(xml, out, urlp.stylesheet);
				} else {
					static_env.outputXML(out, xml);
				}            	
				return;
				
            } else if( urlp.cmd.equalsIgnoreCase("INS_COMP_GROUP")) {
            	
            	urlp.insCompGroup();
            	if( DbCmSkillBase.isTitleExist(con, urlp.skillTreeNode.skb_title, DbCmSkillBase.COMPETENCY_GROUP, 0, prof.root_ent_id, 0) ){
            		cwSysMessage msg = new cwSysMessage(SMSG_CMP_GROUP_NAME_EXIST, urlp.skillTreeNode.skb_title);
            		msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
            		return;
            	}
            	urlp.skillTreeNode.skb_owner_ent_id = prof.root_ent_id;
            	urlp.skillTreeNode.skb_create_usr_id = prof.usr_id;
            	urlp.skillTreeNode.ins(con);
            	con.commit();
				cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_COMP_GROUP);
				msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);            	
            	return;
            	
            } else if( urlp.cmd.equalsIgnoreCase("PREP_UPD_COMP_GROUP") ||
            			urlp.cmd.equalsIgnoreCase("PREP_UPD_COMP_GROUP_XML")) {
            	
            	urlp.getCompGroup();
            	urlp.skillTreeNode.get(con, true);
				
				CmSkillManager skillMgr = new CmSkillManager();
				String metaXml = skillMgr.getSkillReferenceDataAsXML(con, prof.root_ent_id, null);            	
				
				if( ViewCmAssessment.checkSkillSuccessorInASY(con, urlp.skillTreeNode.skb_id) ) {
					metaXml += "<child_in_use>true</child_in_use>";
				} else {
					metaXml += "<child_in_use>false</child_in_use>";
				}

            	String xml = formatXML(urlp.skillTreeNode.asXML(), metaXml, MODULENAME);
            	if( urlp.cmd.equalsIgnoreCase("PREP_UPD_COMP_GROUP") ){
            		generalAsHtml(xml, out, urlp.stylesheet);
            	} else {
            		static_env.outputXML(out, xml);
            	}
            	return;
            	
            } else if( urlp.cmd.equalsIgnoreCase("UPD_COMP_GROUP")){
            	
            	urlp.updCompGroup();
				if( DbCmSkillBase.isTitleExist(con, urlp.skillTreeNode.skb_title, DbCmSkillBase.COMPETENCY_GROUP, urlp.skillTreeNode.skb_id, prof.root_ent_id, 0) ){
					cwSysMessage msg = new cwSysMessage(SMSG_CMP_GROUP_NAME_EXIST, urlp.skillTreeNode.skb_title);
					msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
					return;
				}
				            	
            	if( !urlp.skillTreeNode.equalsTimestamp(con) ) {
					cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
					msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
            		return;
            	}
            	CmSkillManager skillMgr = new CmSkillManager();
            	skillMgr.updCompGroup(con, prof, urlp.skillTreeNode);
            	con.commit();
				cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_COMP_GROUP);
				msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
				return;
            
            } else if( urlp.cmd.equalsIgnoreCase("DEL_COMP_GROUP")) {
            	
            	urlp.delCompGroup();
				if( !urlp.skillTreeNode.equalsTimestamp(con) ) {
					cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
					msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
					return;
				}            	
            	CmSkillManager skillMgr = new CmSkillManager();
            	skillMgr.delCompGroup(con, prof, urlp.skillTreeNode);
				con.commit();
            	cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_COMP_GROUP);
            	msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
            	return;
            	
            } else if( urlp.cmd.equalsIgnoreCase("GET_COMP_GRP_LIST") || 
            			urlp.cmd.equalsIgnoreCase("GET_COMP_GRP_LIST_XML")){
            	
            	String xml = formatXML( DbCmSkillTreeNode.getTitleListAsXML(con, prof.root_ent_id),MODULENAME );
            	if( urlp.cmd.equalsIgnoreCase("GET_COMP_GRP_LIST") ) {
					generalAsHtml(xml, out, urlp.stylesheet);
            	} else {
					static_env.outputXML(out, xml);
            	}
            	return;
            	
            } else if( urlp.cmd.equalsIgnoreCase("GET_COMP_GRP") 
            	|| urlp.cmd.equalsIgnoreCase("GET_COMP_GRP_XML") ){
            	
            	urlp.pagination();
            	urlp.getCompGroup();
            	CmSkillManager skillMgr = new CmSkillManager();
            	String xml = skillMgr.getCompGroupAndChildXML(con, sess, urlp.skillTreeNode.skb_id, urlp.cwPage);
            	xml = formatXML(xml, MODULENAME); 
            	if( urlp.cmd.equalsIgnoreCase("GET_COMP_GRP") ) {
					generalAsHtml(xml, out, urlp.stylesheet);
            	} else {
					static_env.outputXML(out, xml);
            	}
            	return;
            	
            } else if( urlp.cmd.equalsIgnoreCase("PREP_INS_COMPETENCY") 
            	|| urlp.cmd.equalsIgnoreCase("PREP_INS_COMPETENCY_XML")) {
            	
            	urlp.getCompGroup();
				urlp.skillTreeNode.get(con);
				String xml = formatXML(urlp.skillTreeNode.asXML(), MODULENAME);
				if( urlp.cmd.equalsIgnoreCase("PREP_INS_COMPETENCY") ){
					generalAsHtml(xml, out, urlp.stylesheet);
				} else {
					static_env.outputXML(out, xml);
				}
				return;
				            	
            } else if( urlp.cmd.equalsIgnoreCase("INS_COMPETENCY")) {
            	
            	urlp.insCompSkill();
				if( DbCmSkillBase.isTitleExist(con, urlp.skill.skb_title, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL, 0, prof.root_ent_id, urlp.skill.skb_parent_skb_id) ){
					cwSysMessage msg = new cwSysMessage(SMSG_CMP_NAME_EXIST, urlp.skill.skb_title);
					msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
					return;
				}            	
            	CmSkillManager skillMgr = new CmSkillManager();
            	skillMgr.insCompetency(con, prof, urlp.skill, urlp.behav_desc);
				con.commit();
				cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_COMP_SKILL);
				msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);            	
				return;            	
            	
            } else if( urlp.cmd.equalsIgnoreCase("GET_COMPETENCY") 
            	|| urlp.cmd.equalsIgnoreCase("GET_COMPETENCY_XML")) {

            	urlp.getCompSkill();
            	CmSkillManager skillMgr = new CmSkillManager();
            	String xml = DbCmSkillBase.getAncestorXml(con, urlp.skill.skl_skb_id)
            	 			+ skillMgr.getCompetencyAsXml(con, urlp.skill.skl_skb_id);
            	
            	String metaXml = null; 
				if( ViewCmAssessment.checkSkillSuccessorInASY(con, urlp.skill.skl_skb_id) ) {
					metaXml += "<child_in_use>true</child_in_use>";
				} else {
					metaXml += "<child_in_use>false</child_in_use>";
				}            	 
            	xml = formatXML( xml, metaXml, MODULENAME );
            	 
            	if( urlp.cmd.equalsIgnoreCase("GET_COMPETENCY") ){
					generalAsHtml(xml, out, urlp.stylesheet);
            	} else {
					static_env.outputXML(out, xml);
            	}

            } else if( urlp.cmd.equalsIgnoreCase("DEL_COMPETENCY")) {
            	
            	urlp.delCompSkill();
				if( !urlp.skill.equalsTimestamp(con) ) {
					cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
					msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
					return;
				}            	
            	
				CmSkillManager skillMgr = new CmSkillManager();
				skillMgr.skill = urlp.skill;
				skillMgr.delSkill(con, prof.usr_id);
				con.commit();
                
				cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_SKILL);
				msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
            	return;
            	
            } else if( urlp.cmd.equalsIgnoreCase("DEL_COMPETENCY_LIST")) {
            	
            	urlp.delCompSkill();
            	CmSkillManager skillMgr = new CmSkillManager();
				DbCmSkill skill = new DbCmSkill();
				if( urlp.skillIdList != null && urlp.skillIdList.length > 0 ) {
					for(int i=0; i<urlp.skillIdList.length; i++) {
						skill.skb_id = urlp.skillIdList[i]; 
						skill.skl_skb_id = skill.skb_id;
						skillMgr.skill = skill;
						skillMgr.delSkill(con, prof.usr_id);
					}
					con.commit();					
				}
				cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_SKILL);
				msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
				return;

            } else if( urlp.cmd.equalsIgnoreCase("UPD_COMPETENCY")) {
            	urlp.updCompSkill();
				if( DbCmSkillBase.isTitleExist(con, urlp.skill.skb_title, DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL, urlp.skill.skb_id, prof.root_ent_id, urlp.skill.skb_parent_skb_id) ){
					cwSysMessage msg = new cwSysMessage(SMSG_CMP_NAME_EXIST, urlp.skill.skb_title);
					msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
					return;
				}
				            	
				if( !urlp.skill.equalsTimestamp(con) ) {
					cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
					msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
					return;
				}
            	CmSkillManager skillMgr = new CmSkillManager();
            	skillMgr.updCompetency(con, prof, urlp.skill, urlp.behav_desc, urlp.behav_order, urlp.behav_skb_id);
            	con.commit();
				cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_COMP_SKILL);
				msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);            	
            	
            } else if (urlp.cmd.equalsIgnoreCase("PREP_UPD_SKILL") || 
            
            
                urlp.cmd.equalsIgnoreCase("PREP_UPD_SKILL_XML")) {
                urlp.prepUpdSkill();
                CmSkillManager skillMgr = new CmSkillManager();
                String xml = skillMgr.getSkill4UpdateAsXML(con, urlp.skill.skl_skb_id, urlp.scale_id_lst);
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("PREP_UPD_SKILL")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("PREP_INS_SKILL") || 
                      urlp.cmd.equalsIgnoreCase("PREP_INS_SKILL_XML")) {
                urlp.prepInsSkill();
                CmSkillManager skillMgr = new CmSkillManager();
                String xml = skillMgr.getSkillReferenceDataAsXML(con, prof.root_ent_id, urlp.scale_id_lst);
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("PREP_INS_SKILL")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("GET_COMP_SKILL_CONTENT") || 
                      urlp.cmd.equalsIgnoreCase("GET_COMP_SKILL_CONTENT_XML")) {
                urlp.getCompSkillContent();
                urlp.getOrder();
                DbCmSkill skill = urlp.skill;
                urlp.skill.skb_owner_ent_id = prof.root_ent_id;
                CmSkillManager skillmgr = new CmSkillManager();
                String xml = skillmgr.getCompSkillContentAsXML(con, urlp.skill.skl_skb_id, 
                                                               urlp.cur_page, urlp.pagesize,
                                                               sess, urlp.pagetime);
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_COMP_SKILL_CONTENT")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("INS_SKILL")) {
                
                urlp.insSkill();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.skill = urlp.skill;
                skillmgr.insSkill(con, prof.usr_id, prof.root_ent_id);
                con.commit();
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_SKILL);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);

            }else if (urlp.cmd.equalsIgnoreCase("UPD_SKILL")) {
                
                urlp.updSkill();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.skill = urlp.skill;
                skillmgr.updSkill(con, prof.usr_id, prof.root_ent_id);
                con.commit();
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_SKILL);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);

            } else if (urlp.cmd.equalsIgnoreCase("DEL_SKILL")) {
                urlp.getSkill();
                
//                if( DbCmSkillSetCoverage.checkSkillExist(con, urlp.skill.skl_skb_id) ) {
//                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_SKILL_USED);
//                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
//                    return;
//                }
                
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.skill = urlp.skill;
                skillmgr.delSkill(con, prof.usr_id);
                con.commit();
                
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_SKILL);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);

            }else if (urlp.cmd.equalsIgnoreCase("TRASH_SKILL")) {
                out.println("System error: TRASH_SKILL has been retired ");
/*
                urlp.getSkill();
                
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.skill = urlp.skill;
                skillmgr.trashSkill(con);
                con.commit();
                
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_TRASH_SKILL);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
*/
            }else if (urlp.cmd.equalsIgnoreCase("GET_SKILL") || 
                      urlp.cmd.equalsIgnoreCase("GET_SKILL_XML")) {
                        
                urlp.getSkill();
                
                ViewCmSkill vSkill = new ViewCmSkill();
                
                String xml = vSkill.getSkillAsXML(con, urlp.skill.skl_skb_id, urlp.skill.skb_ssl_id);
                if( urlp.showAll ) {
                    CmSkillManager skillmgr = new CmSkillManager();
                    xml += skillmgr.getAllScaleAsXML(con, prof.root_ent_id, urlp.skill.skb_ssl_id);
                }
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_SKILL")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if( urlp.cmd.equalsIgnoreCase("GET_SKILL_Q") ||
                      urlp.cmd.equalsIgnoreCase("GET_SKILL_Q_XML") ) {
                
                urlp.getSkill();
                StringBuffer xmlBody = new StringBuffer();
                if( urlp.skill.skl_skb_id > 0 ) {
                    urlp.skill.get(con);                
                    xmlBody.append("<skill id=\"").append(urlp.skill.skl_skb_id).append("\">").append(cwUtils.NEWL);
                    xmlBody.append("<title>").append(urlp.skill.skb_title).append("</title>").append(cwUtils.NEWL);
                    xmlBody.append(urlp.skill.skl_xml).append(cwUtils.NEWL);
                    xmlBody.append("</skill>").append(cwUtils.NEWL);
                } else {
                    xmlBody.append("<skill id=\"\">").append(cwUtils.NEWL);
                    xmlBody.append("<title/>").append(cwUtils.NEWL);                    
                    xmlBody.append("</skill>").append(cwUtils.NEWL);                    
                }
                String xml = formatXML(xmlBody.toString(), MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("GET_SKILL_Q") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);
                
            }else if (urlp.cmd.equalsIgnoreCase("INS_SCALE")) {
                urlp.getScale();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.skillScale = urlp.skillScale;
                skillmgr.skillLevelVec = urlp.skillLevelVec;
                
                skillmgr.insScale(con, prof.usr_id, prof.root_ent_id);
                con.commit();
                
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_SCALE);
                msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
            
            }else if (urlp.cmd.equalsIgnoreCase("UPD_SCALE")) {
                urlp.getScale();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.skillScale = urlp.skillScale;
                skillmgr.skillLevelVec = urlp.skillLevelVec;
                
                skillmgr.updScale(con, prof.usr_id);
                con.commit();
                
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_SCALE);
                msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
            
            }else if (urlp.cmd.equalsIgnoreCase("DEL_SCALE")) {
                urlp.getScale();
                CmSkillManager skillmgr = new CmSkillManager();
                skillmgr.skillScale = urlp.skillScale;
                
                skillmgr.delScale(con, prof.usr_id);
                con.commit();
                
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_SCALE);
                msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
                
            }else if (urlp.cmd.equalsIgnoreCase("GET_SCALE") || 
                      urlp.cmd.equalsIgnoreCase("GET_SCALE_XML")) {
                urlp.getScale();
                ViewCmSkill vSkill = new ViewCmSkill();
                String xml= vSkill.getScaleAsXML(con, urlp.skillScale.ssl_id);
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_SCALE")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("GET_SCALE_LIST") || 
                      urlp.cmd.equalsIgnoreCase("GET_SCALE_LIST_XML")) {
                urlp.getScale();
                urlp.getOrder();
                
                CmSkillManager skillmgr = new CmSkillManager();
                String xml = new String();
                if (urlp.showAll) {
                    xml = skillmgr.getAllScaleAsXML(con, prof.root_ent_id, urlp.skillScale.ssl_id);
                }else  {
                    xml = skillmgr.getScaleListAsXML(con, sess, prof.root_ent_id, urlp.cur_page, urlp.pagesize, urlp.pagetime, urlp.order_by, urlp.sort_by);
                }
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_SCALE_LIST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if ( urlp.cmd.equalsIgnoreCase("GET_SKILL_SET") ||
                       urlp.cmd.equalsIgnoreCase("GET_SKILL_SET_XML") ) {                                
                
                urlp.getSkillSet();
                String xml = new String();
                if( urlp.refresh ) {
                    // refresh data in session 
                    if( urlp.skillSet.sks_skb_id > 0 ) {
                        //get data from database
                        ViewCmSkill skillView = new ViewCmSkill();
                        xml = skillView.getSkillSetAsXML(con, urlp.skillSet.sks_skb_id);
                    }
                    if( sess != null )
                        sess.removeAttribute("Competency_SKILL_SET");
                } else {
                    // get data from session
                    CmSkillSetManager SsMgr = new CmSkillSetManager();
                    SsMgr.sks_skb_id = urlp.skillSet.sks_skb_id;
                    xml = SsMgr.getSkillSetAsXML(con, sess);
                }
                xml = formatXML(xml, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("GET_SKILL_SET") ) 
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);

            }else if( urlp.cmd.equalsIgnoreCase("SAVE_SKILL_N_Q") ) {
                urlp.saveSkillNQ();
                Hashtable data = null;
                if( sess != null )
                    data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                if( data == null )
                    data = new Hashtable();
                for(int i=0; i<urlp.skillIdList.length; i++) {
                    CmSkillSetManager.saveQuestionToSess(con, data, urlp.skillIdList[i], urlp.assesseeQList[i], null, urlp.commentIndList[i]);
                }
                
                String id_lst = new String();
                if( urlp.picked_skl_id_lst != null )
                    id_lst += urlp.picked_skl_id_lst;
                if( urlp.skl_id_lst != null ) {
                    if( urlp.picked_skl_id_lst != null )
                        id_lst += DELIMITER;
                    id_lst += urlp.skl_id_lst;
                }
                Vector vec = cwUtils.splitToVec(id_lst, DELIMITER);
                data.put(HASH_SKILL_SET_TITLE, urlp.sks_title);
                data.put(HASH_SKILL_COMMENT_IND, new Boolean(urlp.sks_comment_ind));
                data.put(HASH_SKILL_ID_LIST, vec);
                sess.setAttribute("Competency_SKILL_SET", data);
                response.sendRedirect(urlp.url_success);
                
            }else if( urlp.cmd.equalsIgnoreCase("PICK_SKILL") ) {

                urlp.pickSkill();
                Hashtable data = new Hashtable();                
                data.put(HASH_SKILL_SET_TITLE, urlp.sks_title);
                
                String id_lst = new String();
                if( urlp.picked_skl_id_lst != null )
                    id_lst += urlp.picked_skl_id_lst;
                if( urlp.skl_id_lst != null ) {
                    if( urlp.picked_skl_id_lst != null )
                        id_lst += DELIMITER;
                    id_lst += urlp.skl_id_lst;
                }

                //long[] skl_id_lst = CompetencyReqParam.strSplitToLong(id_lst, DELIMITER);
                Vector skl_id_lst = cwUtils.splitToVec(id_lst, DELIMITER);
                float[] level_lst = cwUtils.splitToFloat(urlp.level_lst, DELIMITER);
                long[] priority_lst = cwUtils.splitToLong(urlp.priority_lst, DELIMITER);
                data.put(HASH_SKILL_ID_LIST, skl_id_lst);
                data.put(HASH_SKILL_LEVEL_LIST, level_lst);
                data.put(HASH_SKILL_PRIORITY_LIST, priority_lst);
                sess.setAttribute("Competency_SKILL_SET", data);
                response.sendRedirect(urlp.url_success);
                
            }else if( urlp.cmd.equalsIgnoreCase("GET_SKILL_SET_LIST") || 
                      urlp.cmd.equalsIgnoreCase("GET_SKILL_SET_LIST_XML") ) {
                
                urlp.getSkillSetList();
                urlp.getOrder();
                CmSkillSetManager SsMgr = new CmSkillSetManager();
                String xml = SsMgr.getSkillSetListAsXML(con, sess, urlp.cur_page, urlp.pagesize, urlp.pagetime, urlp.order_by, urlp.skillSet.sks_type, urlp.sort_by, prof.root_ent_id);
                xml = formatXML(xml, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_SKILL_SET_LIST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
                return;
                
            }else if( urlp.cmd.equalsIgnoreCase("INS_SKILL_SET") ){
                
                urlp.updSkillSet();
                DbCmSkillSet skillSet = new DbCmSkillSet();
                skillSet.sks_type = "SKP";
                skillSet.sks_title = urlp.sks_title;
                skillSet.sks_owner_ent_id = prof.root_ent_id;
                skillSet.sks_create_usr_id = prof.usr_id;
                skillSet.sks_update_usr_id = prof.usr_id;         
                long skt_id=DbCmSkillEntity.ins(con, skillSet.sks_type);
                skillSet.sks_ske_id=skt_id;

                if( skillSet.ins(con) != 1 )
                    throw new cwException("Failed to insert a Skill Set.");
              
                long[] skb_id_lst = cwUtils.splitToLong(urlp.skl_id_lst, DELIMITER);
                float[] level_lst = cwUtils.splitToFloat(urlp.level_lst, DELIMITER);
                long[] priority_lst = cwUtils.splitToLong(urlp.priority_lst, DELIMITER);

                DbCmSkillSetCoverage[] Ssc = new DbCmSkillSetCoverage[skb_id_lst.length];
                for(int i=0; i<Ssc.length; i++) {
                    Ssc[i] = new DbCmSkillSetCoverage();
                    Ssc[i].ssc_skb_id = skb_id_lst[i];
                    Ssc[i].ssc_level = level_lst[i];
                    Ssc[i].ssc_priority = priority_lst[i];
                    // bAnswered is originally designed for during Assessment, whether to 
                    // rate in competency level or rate in behaviour indicator level
                    // but since SkillSetCoverage is shared by Assessment and Competency Profile
                    // so in the case of Competency Profile, bAnswered has to be "true"
                    // in order to save "ssc_level", the same applies to "UPD_SKILL_SET" below
                    // (2003-07-16 kawai)
                    Ssc[i].bAnswered = true;
                }
                
                urlp.skillSetMgr.sks_skb_id = skillSet.sks_skb_id;
                urlp.skillSetMgr.insCoverage(con, Ssc);
                
                con.commit();
                if( sess != null )
                    sess.removeAttribute("Competency_SKILL_SET");
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_SKILLSET);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                
            }else if( urlp.cmd.equalsIgnoreCase("UPD_SKILL_SET") ){                                
                
                urlp.updSkillSet();
                if( DbCmAssessment.checkSurvey(con, urlp.skillSetMgr.sks_skb_id) ) {
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_SVY_USED);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                    return;
                }
                
                DbCmSkillSet skillSet = new DbCmSkillSet();
                skillSet.sks_skb_id = urlp.skillSetMgr.sks_skb_id;
                if( !skillSet.validUpdTimestamp(con, urlp.last_upd_timestamp) ){
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                    return;                    
                }
                
                skillSet.sks_title = urlp.sks_title;
                skillSet.sks_update_usr_id = prof.usr_id;
                skillSet.sks_update_timestamp = cwSQL.getTime(con);
                skillSet.upd(con);
                
                long[] skb_id_lst = cwUtils.splitToLong(urlp.skl_id_lst, DELIMITER);
                float[] level_lst = cwUtils.splitToFloat(urlp.level_lst, DELIMITER);
                long[] priority_lst = cwUtils.splitToLong(urlp.priority_lst, DELIMITER);
                
                DbCmSkillSetCoverage[] Ssc = new DbCmSkillSetCoverage[skb_id_lst.length];
                for(int i=0; i<Ssc.length; i++) {
                    Ssc[i] = new DbCmSkillSetCoverage();                    
                    Ssc[i].ssc_skb_id = skb_id_lst[i];
                    Ssc[i].ssc_level = level_lst[i];
                    Ssc[i].ssc_priority = priority_lst[i];
                    Ssc[i].bAnswered = true;
                }
                urlp.skillSetMgr.updCoverage(con, Ssc, true, true);
                if( sess != null )
                    sess.removeAttribute("Competency_SKILL_SET");
                con.commit();
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_SKILLSET);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                
            }else if( urlp.cmd.equalsIgnoreCase("DEL_SKILL_SET_CHECK")||urlp.cmd.equalsIgnoreCase("DEL_SKILL_SET_CHECK_XML")){
                urlp.delSkillSet();
                long[] sks_id_lst = cwUtils.splitToLong(urlp.sks_id_lst, DELIMITER);
                CmSkillSetManager skillMgt =new CmSkillSetManager();
                String xml=skillMgt.getItmAndUsrRelationSkillSetXml(con, sks_id_lst);
                xml = formatXML(xml, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("DEL_SKILL_SET_CHECK") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml); 
            } else if( urlp.cmd.equalsIgnoreCase("DEL_SKILL_SET")){
                
                urlp.delSkillSet();
                long[] sks_id_lst = cwUtils.splitToLong(urlp.sks_id_lst, DELIMITER);
                DbCmSkillSetCoverage Ssc = new DbCmSkillSetCoverage();
                String skeIdLst=Ssc.getSksSkeId(con, sks_id_lst);
                if(skeIdLst.length()>0){
	                Ssc.delAllSkills(con, sks_id_lst);
	                DbUserSkillSet.delUserSkillSet(con, skeIdLst);
	                DbItemTargetRuleDetail.delItmBySkillSet(con, skeIdLst);
	                DbCmSkillSet skillSet = new DbCmSkillSet();
	                skillSet.del(con, sks_id_lst);
	                DbCmSkillEntity.del(con, skeIdLst);
	                con.commit();
                }
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_SKILL);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
            }else if( urlp.cmd.equalsIgnoreCase("GET_SURVEY") || 
                      urlp.cmd.equalsIgnoreCase("GET_SURVEY_XML") ) {
                
                urlp.getSkillSet();                
                CmSkillSetManager skillSetMgr = new CmSkillSetManager();
                skillSetMgr.sks_skb_id = urlp.skillSet.sks_skb_id;
                String xml = skillSetMgr.getSurveyXML(con);
                xml = formatXML(xml, MODULENAME);

                if( urlp.cmd.equalsIgnoreCase("GET_SKILL_SET") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);

            }else if( urlp.cmd.equalsIgnoreCase("EDIT_SURVEY") ||
                      urlp.cmd.equalsIgnoreCase("EDIT_SURVEY_XML") ) {
                
                urlp.getSkillSet();
                String xml = new String();
                if( urlp.refresh ) {
                    // get from database and clear session
                    CmSkillSetManager skillSetMgr = new CmSkillSetManager();
                    skillSetMgr.sks_skb_id = urlp.skillSet.sks_skb_id;
                    xml = skillSetMgr.editSurveyXML(con, prof.root_ent_id, urlp.showSkillPath);                    
                    if(sess != null) {

                        sess.removeAttribute("Competency_SKILL_SET");
                        //sess.setAttribute("Competency_SKILL_SET");
                    }
                }else {

                    // get from session
                    CmSkillSetManager skillSetMgr = new CmSkillSetManager();
                    skillSetMgr.sks_skb_id = urlp.skillSet.sks_skb_id;
                    xml = skillSetMgr.editSurveyXML(con, prof.root_ent_id, sess, urlp.showSkillPath);
                }
                
                xml = formatXML(xml, MODULENAME);

                if( urlp.cmd.equalsIgnoreCase("EDIT_SURVEY") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);                
                
            }else if(urlp.cmd.equalsIgnoreCase("DEL_SURVEY")) {
                
                urlp.delSurvey();
                if( DbCmAssessment.checkSurvey(con, urlp.skillSetMgr.sks_skb_id) ) {
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_SVY_USED);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                    return;
                }
                urlp.skillSetMgr.delSurvey(con);
                con.commit();
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_SVY);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);

                
            }else if( urlp.cmd.equalsIgnoreCase("PICK_SKILL_SET") ) {
                
                urlp.pickSkillSet();
                Hashtable data = null;                
                if( sess != null) {
                    data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                    if( data == null ) 
                        data = new Hashtable();                    
                }
                Vector vec = cwUtils.splitToVec(urlp.picked_skl_id_lst, "~");
                urlp.skillSetMgr.pickSkillSet(con, vec);
                
                data.put(HASH_SKILL_SET_TITLE, urlp.sks_title);
                data.put(HASH_SKILL_COMMENT_IND, new Boolean(urlp.sks_comment_ind));
                data.put(HASH_SKILL_ID_LIST, vec);
                sess.setAttribute("Competency_SKILL_SET", data);
                response.sendRedirect(urlp.url_success);
                
            }else if( urlp.cmd.equalsIgnoreCase("PICK_SURVEY_SKILL") ) {
                
                urlp.pickSkillSet();
                Hashtable data = null;
                if( sess != null) {
                    data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                    if( data == null )
                        data = new Hashtable();
                }
                
                String id_lst = new String();
                if( urlp.picked_skl_id_lst != null )
                    id_lst += urlp.picked_skl_id_lst;
                if( urlp.skl_id_lst != null ) {
                    if( urlp.picked_skl_id_lst != null )
                        id_lst += DELIMITER;
                    id_lst += urlp.skl_id_lst;
                }

                Vector vec = cwUtils.splitToVec(id_lst, DELIMITER);

                data.put(HASH_SKILL_SET_TITLE, urlp.sks_title);
                data.put(HASH_SKILL_COMMENT_IND, new Boolean(urlp.sks_comment_ind));
                data.put(HASH_SKILL_ID_LIST, vec);
                sess.setAttribute("Competency_SKILL_SET", data);
                response.sendRedirect(urlp.url_success);
                
            }else if( urlp.cmd.equalsIgnoreCase("GETQ") ||
                      urlp.cmd.equalsIgnoreCase("GETQ_XML") ) {
            
                urlp.getQ();
                Hashtable data = null;
                if( sess != null ) {
                    data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                }
                String xml = new String();
//                DbCmSkillSetCoverage DbSsc = new DbCmSkillSetCoverage();
//                DbSsc.ssc_sks_skb_id = urlp.sks_id;
//                long sks_id;
//                Vector vec = DbSsc.getSkillBaseId(con);
//                if( vec.size() > 0 )
//                    sks_id = ((Long)vec.elementAt(0)).longValue();
//                else    
//                    sks_id = 0;
                xml = CmSkillSetManager.getQuestionFromSess(con, data, urlp.skl_id, urlp.sks_id);
                xml = formatXML(xml, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("GETQ") )
                    generalAsHtml(xml.toString(), out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml.toString());                
            
            }else if( urlp.cmd.equalsIgnoreCase("SAVEQ") ){
                
                urlp.saveQ();
                Hashtable data = null;
                if( sess != null )
                    data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                if( data == null )
                    data = new Hashtable();
                CmSkillSetManager.saveQuestionToSess(con, data, urlp.skill.skl_skb_id, urlp.assesseeQ, urlp.assessorQ, urlp.sks_comment_ind);
                sess.setAttribute("Competency_SKILL_SET", data);
                response.sendRedirect(urlp.url_success);

            }else if( urlp.cmd.equalsIgnoreCase("INS_SURVEY") ) {
                urlp.updSurvey();
                CmSkillSetManager skillSetMgr = new CmSkillSetManager();
                long sks_id = skillSetMgr.createSurvey(con, SKILL_TEMPLATE, urlp.sks_title, urlp.sks_comment_ind, prof.root_ent_id, prof.usr_id);
                long[] skb_id_lst = cwUtils.splitToLong(urlp.skl_id_lst, DELIMITER);

                Hashtable data = null;
                String[] str = new String[3];
                if( sess!= null ) {
                    data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                }
                DbCmSkillSetCoverage[] DbSsc = new DbCmSkillSetCoverage[skb_id_lst.length];
                String ssc_xml;
                for(int i=0; i< DbSsc.length; i++) {
                    DbSsc[i] = new DbCmSkillSetCoverage();
                    DbSsc[i].ssc_sks_skb_id = sks_id;
                    DbSsc[i].ssc_skb_id = skb_id_lst[i];
                    DbSsc[i].ssc_priority = i+1;
                    DbSsc[i].ssc_xml = CmSkillSetManager.getQuestionFromSess(con, data, DbSsc[i].ssc_skb_id, sks_id);
                }
                skillSetMgr.insCoverage(con, DbSsc);
                
                con.commit();
                if( sess != null )
                    sess.removeAttribute("Competency_SKILL_SET");
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_SVY);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                
            }else if( urlp.cmd.equalsIgnoreCase("UPD_SURVEY") ) {
                
                urlp.updSurvey();
                
                if( DbCmAssessment.checkSurvey(con, urlp.skillCoverage.ssc_sks_skb_id) ) {
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_SVY_USED);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                    return;
                }                                
                
                CmSkillSetManager skillSetMgr = new CmSkillSetManager();
                /*
                Vector vec = urlp.skillCoverage.getSkillBaseId(con);
                if( vec == null || vec.size() == 0 ) {
                    cwSysMessage msg = new cwSysMessage("Survey not found.");
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                    return;
                }
                long sks_id = ((Long)vec.elementAt(0)).longValue();
                */
                DbCmSkillSet DbSs = new DbCmSkillSet();
                //Check update timestamp
                DbSs.sks_skb_id = urlp.skillCoverage.ssc_sks_skb_id;
                if( !DbSs.validUpdTimestamp(con, urlp.last_upd_timestamp) ){
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_failure, out);
                    return;                    
                }
                    
                DbSs.sks_title = urlp.sks_title;                
                if( urlp.sks_comment_ind )
                    DbSs.sks_xml = "<comment provide=\"Y\"/>";
                else
                    DbSs.sks_xml = "<comment provide=\"N\"/>";                            
                DbSs.sks_update_usr_id = prof.usr_id;                
                DbSs.upd(con);


                long[] skb_id_lst = cwUtils.splitToLong(urlp.skl_id_lst, DELIMITER);
                Hashtable data = null;
                String[] str = new String[3];
                if( sess!= null ) {                    
                    data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                }
                DbCmSkillSetCoverage[] DbSsc = new DbCmSkillSetCoverage[skb_id_lst.length];
                String ssc_xml;
                for(int i=0; i< DbSsc.length; i++) {
                    DbSsc[i] = new DbCmSkillSetCoverage();
                    DbSsc[i].ssc_sks_skb_id = urlp.skillCoverage.ssc_sks_skb_id;
                    DbSsc[i].ssc_skb_id = skb_id_lst[i];
                    DbSsc[i].ssc_priority = i+1;
                    DbSsc[i].ssc_xml = CmSkillSetManager.getQuestionFromSess(con, data, DbSsc[i].ssc_skb_id, urlp.skillCoverage.ssc_sks_skb_id);
                }
                skillSetMgr.sks_skb_id = urlp.skillCoverage.ssc_sks_skb_id;
                skillSetMgr.updCoverage(con, DbSsc, true, true);
                
                con.commit();
                if( sess != null )
                    sess.removeAttribute("Competency_SKILL_SET");
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_SVY);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                
            }else if( urlp.cmd.equalsIgnoreCase("get_ent_lst") ||
                      urlp.cmd.equalsIgnoreCase("get_ent_lst_xml") ){
                
                urlp.getEntList();
                String xml;
                try{
                    xml = urlp.dbUsg.getMemberListXML(con, sess, prof, urlp.cur_page, urlp.pagesize, urlp.pagetime);
                }catch(qdbException e) {
                    throw new cwException (e.getMessage());
                }
                
                if( urlp.asm_id > 0 ) {
                    DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
                    dbAssU.asu_asm_id = urlp.asm_id;
                    Vector idVec = dbAssU.getAuEntityId(con);
                    StringBuffer xmlBody = new StringBuffer();
                    xmlBody.append("<picked_entity asm_id=\"").append(urlp.asm_id).append("\">")
                           .append(cwUtils.NEWL);
                    for(int i=0; i<idVec.size(); i++)
                        xmlBody.append("<entity id=\"").append(idVec.elementAt(i)).append("\"/>").append(cwUtils.NEWL);
                    xmlBody.append("</picked_entity>").append(cwUtils.NEWL);
                    xml += xmlBody.toString();
                }
                
                xml = formatXML(xml, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("get_ent_lst") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);                          
            
            } else if( urlp.cmd.equalsIgnoreCase("SELECTED_USER") ||
                       urlp.cmd.equalsIgnoreCase("SELECTED_USER_XML") ) {
                        
                urlp.pickUser();
                long[] ids = cwUtils.splitToLong(urlp.usr_ent_id_lst, DELIMITER);
                CmAssessmentManager assMgr = new CmAssessmentManager();
                assMgr.asm_id = urlp.asm_id;
                String xml = assMgr.generateSelectedNameList(con, ids);                
                xml = formatXML(xml, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("SELECTED_USER") )
                    generalAsHtml(xml.toString(), out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);
                                
            }else if( urlp.cmd.equalsIgnoreCase("PICK_USER") ){
                urlp.pickUser();
                
                if( urlp.asm_id > 0 ) {
                    //Check last upd timestamp and update timestamp
                    DbCmAssessment dbAss = new DbCmAssessment();
                    dbAss.asm_id = urlp.asm_id;
                    if( !dbAss.validUpdTimestap(con, urlp.last_upd_timestamp) ){
                        cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
                        msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                        return;                    
                    }
                    dbAss.updTimestamp(con);
                    
                    long[] ids = cwUtils.splitToLong(urlp.usr_ent_id_lst, DELIMITER);
                    long[] weights = cwUtils.splitToLong(urlp.usr_weight_lst, DELIMITER);
                    String[] types = cwUtils.splitToString(urlp.usr_type_lst, DELIMITER);                                        
                    
                    DbCmAssessmentUnit[] dbAssU = new DbCmAssessmentUnit[ids.length];                    
                    for(int i=0; i< dbAssU.length; i++) {
                        dbAssU[i] = new DbCmAssessmentUnit();
                        dbAssU[i].asu_asm_id = urlp.asm_id;
                        dbAssU[i].asu_ent_id = ids[i];
                        dbAssU[i].asu_attempt_nbr = 1;
                        dbAssU[i].asu_weight = weights[i];
                        dbAssU[i].asu_type = types[i];
                        dbAssU[i].asu_submit_ind = false;
                        //dbAssU[i].asu_notify_ind = false;
                    }
                    CmAssessmentManager assMgr = new CmAssessmentManager();
                    assMgr.asm_id = urlp.asm_id;
                    if( urlp.sender_usr_id == null ) {
                        urlp.sender_usr_id = acSite.getSysUsrId(con, prof.root_ent_id);
                    }
                    assMgr.pickRator(con, prof, dbAssU, false, urlp.sender_usr_id
                                    ,urlp.notify_msg_subject, urlp.collect_msg_subject);
                    con.commit();
                } else {
                    Hashtable data = new Hashtable();
                    data.put(HASH_ENTITY_ID, new Long(urlp.usr_ent_id));
                    data.put(HASH_SURVEY_ID, new Long(urlp.sks_id));
                    data.put(HASH_ASSESSMENT_TITLE, urlp.sks_title);
                    if( urlp.review_start != null )
                        data.put(HASH_REVIEW_START_DATETIME, urlp.review_start);
                    if( urlp.review_end != null )
                        data.put(HASH_REVIEW_END_DATETIME, urlp.review_end);
                    if( urlp.notify_date != null )    
                        data.put(HASH_NOTIFICATION_DATETIME, urlp.notify_date);
                    if( urlp.due_date != null )    
                        data.put(HASH_DUE_DATETIME, urlp.due_date);                
                    sess.setAttribute("Competency_ASSESSMENT", data);
                }
                
                response.sendRedirect(urlp.url_success);
                
            }else if( urlp.cmd.equalsIgnoreCase("PICK_SURVEY") ) {
                
                urlp.pickUser();
                Hashtable data = new Hashtable();
                data.put(HASH_ENTITY_ID, new Long(urlp.usr_ent_id));
                data.put(HASH_SURVEY_ID, new Long(urlp.sks_id));
                data.put(HASH_ASSESSMENT_TITLE, urlp.sks_title);
                if( urlp.review_start != null )
                    data.put(HASH_REVIEW_START_DATETIME, urlp.review_start);
                if( urlp.review_end != null )
                    data.put(HASH_REVIEW_END_DATETIME, urlp.review_end);
                if( urlp.notify_date != null )    
                    data.put(HASH_NOTIFICATION_DATETIME, urlp.notify_date);
                if( urlp.due_date != null )    
                    data.put(HASH_DUE_DATETIME, urlp.due_date);
                
                sess.setAttribute("Competency_ASSESSMENT", data);
                response.sendRedirect(urlp.url_success);
                                
            }else if( urlp.cmd.equalsIgnoreCase("edit_assessment") ||
                      urlp.cmd.equalsIgnoreCase("edit_assessment_xml") ) {
                
                urlp.editAssessment();
                String xml = new String();
                xml = urlp.assMgr.getAssessmentReferenceDataAsXML(con, prof.root_ent_id);
                if( urlp.refresh ) {
                    if( urlp.assMgr.asm_id > 0 ) {
                        if( (DbCmAssessment.getStatus(con, urlp.assMgr.asm_id)).equalsIgnoreCase(DELETED) ) {
                            cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_DELETED);
                            msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_failure, out);
                        }
                        xml += urlp.assMgr.getAssessmentXML(con);
                    }
                    sess.removeAttribute("Competency_ASSESSMENT");
                } else {
                    throw new cwException("refresh=true in edit_assessment is not supported");                        
                }
                
                String curTimeTag = "<cur_time>" + cwSQL.getTime(con) + "</cur_time>";
                xml = formatXML(xml, curTimeTag, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("edit_assessment") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);
                    
/* no longer function
            }else if( urlp.cmd.equalsIgnoreCase("INS_ASSESSMENT") ) {
                urlp.updAssessment();
                CmAssessmentManager mgr = new CmAssessmentManager();
                if( urlp.sender_usr_id == null )
                    urlp.sender_usr_id = acSite.getSysUsrId(con, prof.root_ent_id);
                mgr.insAssessment(con, urlp.dbAss, prof, 
                                  urlp.sender_usr_id, urlp.notify_msg_subject, 
                                  urlp.collect_msg_subject, urlp.self_ass, 
                                  urlp.mgmt_assessor_ent_id_list);
*/
                /*
                /*
                urlp.dbAss.asm_create_usr_id = prof.usr_id;                
                urlp.dbAss.ins(con);

                DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
                dbAssU.asu_asm_id = urlp.dbAss.asm_id;
                dbAssU.asu_ent_id = prof.usr_ent_id;
                dbAssU.asu_attempt_nbr = 1;
                dbAssU.asu_weight = 1;
                dbAssU.asu_type = "RESOLVED";
                dbAssU.asu_submit_ind = false;
                dbAssU.ins(con);
                CmAssessmentNotify assNot = new CmAssessmentNotify();
                
                if( urlp.sender_usr_id == null )
                    urlp.sender_usr_id = acSite.getSysUsrId(con, prof.root_ent_id);
                assNot.insNotification(con, urlp.dbAss.asm_id, urlp.sender_usr_id, urlp.dbAss.asm_eff_start_datetime, urlp.notify_msg_subject, false, prof);
                assNot.insCollectionNotification(con, urlp.dbAss.asm_id, urlp.sender_usr_id, urlp.dbAss.asm_eff_end_datetime, urlp.collect_msg_subject, false, prof);
                //assNot.insNotification(con, urlp.dbAss.asm_id, "s1u3", urlp.dbAss.asm_eff_start_datetime, "notification", false, prof);
                //assNot.insCollectionNotification(con, urlp.dbAss.asm_id, "s1u3", urlp.dbAss.asm_eff_end_datetime, "collection", false, prof);
                
                if( urlp.self_ass ) {
                    dbAssU.asu_asm_id = urlp.dbAss.asm_id;
                    dbAssU.asu_ent_id = urlp.dbAss.asm_ent_id;
                    dbAssU.asu_attempt_nbr = 1;
                    dbAssU.asu_weight = 1;
                    dbAssU.asu_type = "SELF";
                    dbAssU.asu_submit_ind = false;
                    dbAssU.ins(con);
                    
                    Hashtable msgXtpTable = ViewCmAssessment.getMsgTemplateId(con, dbAssU.asu_asm_id);
                    assNot.insNotificationRecipient(con, msgXtpTable, dbAssU.asu_ent_id);
                    
                }
                */
/* no longer function
                con.commit();
                if( sess != null )
                    sess.removeAttribute("Competency_ASSESSMENT");
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_ASS);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
*/                

/* no longer function
            } else if( urlp.cmd.equalsIgnoreCase("UPD_ASSESSMENT") ) {
                
                urlp.updAssessment();
                CmAssessmentManager mgr = new CmAssessmentManager();
                mgr.updAssessment(con, urlp.dbAss, prof);
                /*
                DbCmAssessment dbAss = new DbCmAssessment();
                dbAss.asm_id = urlp.dbAss.asm_id;
                dbAss.get(con);
                if( !dbAss.asm_update_timestamp.equals(urlp.dbAss.asm_update_timestamp) ) {
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);                    
                }
                //if( dbAss.getSurveyId(con) != urlp.dbAss.asm_sks_skb_id ) {
                if( dbAss.asm_sks_skb_id != urlp.dbAss.asm_sks_skb_id ) {
                    if( ViewCmAssessment.ratorStarted(con, urlp.dbAss.asm_id) ) {
                        throw new cwSysMessage(SMSG_CMP_ASS_STARTED);
                    }
                }
                
                Timestamp curTime = cwSQL.getTime(con);
                if( urlp.dbAss.asm_eff_start_datetime.before(curTime) && !urlp.dbAss.asm_eff_start_datetime.equals(dbAss.asm_eff_start_datetime)){
                    throw new cwSysMessage(SMSG_CMP_INVALID_NOTIFICATION_DATE);
                }
                if( urlp.dbAss.asm_eff_end_datetime.before(curTime) && !urlp.dbAss.asm_eff_end_datetime.equals(dbAss.asm_eff_end_datetime)){
                    throw new cwSysMessage(SMSG_CMP_INVALID_COLLECTION_DATE);
                }
                
                urlp.dbAss.asm_update_usr_id = prof.usr_id;
                urlp.dbAss.upd(con);
                
                CmAssessmentNotify assNot = new CmAssessmentNotify();
                assNot.updNotification(con, urlp.dbAss.asm_id, urlp.dbAss.asm_eff_start_datetime, urlp.dbAss.asm_eff_end_datetime, prof);
                */
/*
no longer function
                con.commit();
                if( sess != null )
                    sess.removeAttribute("Competency_ASSESSMENT");
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_ASS);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
*/                
            }else if( urlp.cmd.equalsIgnoreCase("DEL_ASSESSMENT") ) {
                
                urlp.updAssessment();
                DbCmAssessment dbAss = new DbCmAssessment();
                dbAss.asm_id = urlp.dbAss.asm_id;
                dbAss.asm_status = DELETED;
                dbAss.updStatus(con);

                CmAssessmentNotify assNot = new CmAssessmentNotify();
                assNot.delNotification(con, dbAss.asm_id);
                
                con.commit();
                cwSysMessage msg = new cwSysMessage(SMSG_CMP_DEL_ASS);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                
            }else if( urlp.cmd.equalsIgnoreCase("GET_ASS_LIST") ||
                      urlp.cmd.equalsIgnoreCase("GET_ASS_LIST_XML") ) {
//                        out.println("GET_ASS_LIST has been retired.");
/*                
                    urlp.getOrder();
                    CmAssessmentManager assMgr = new CmAssessmentManager();
                    String xml = assMgr.getAssessmentListAsXML(con, sess, urlp.cur_page, urlp.pagesize, urlp.pagetime, urlp.order_by, urlp.sort_by, prof.root_ent_id);
                    xml = formatXML(xml, MODULENAME);
                    
                    if (urlp.cmd.equalsIgnoreCase("GET_ASS_LIST")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }
  */              
                    return;
                    
            }else if( urlp.cmd.equalsIgnoreCase("REFRESH_ASS_LIST") ||
                      urlp.cmd.equalsIgnoreCase("REFRESH_ASS_LIST_XML") ) {
            		boolean hasTc= false;
                    urlp.refreshAssList();
                    urlp.getOrder();                                        
                    CmAssessmentManager assMgr = new CmAssessmentManager();   
                    String tcr_id_lst="(0";
                    if(wizbini.cfgTcEnabled){
                    	hasTc=true;
                    	ViewTrainingCenter tcView =new ViewTrainingCenter();
                    	List tcLst=tcView.getTrainingCenterByOfficer(con,prof.usr_ent_id,prof.current_role, false);
                    	if(tcLst !=null){
                    		for(int i=0;i<tcLst.size(); i++){
                    			DbTrainingCenter tcr =(DbTrainingCenter)tcLst.get(i);
                    			tcr_id_lst+=","+tcr.tcr_id;
                    		}                    		
                    	}
                    }
                    tcr_id_lst+=")";
                    String xml = assMgr.getRefreshedAssListAsXML(con, sess, urlp.status_prepared, urlp.status_collected, urlp.status_notified, urlp.status_resolved, urlp.cur_page, urlp.order_by, urlp.sort_by, urlp.pagesize, urlp.pagetime, prof.root_ent_id, hasTc, tcr_id_lst);
                    xml = formatXML(xml, MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("REFRESH_ASS_LIST")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }            
            }else if( urlp.cmd.equalsIgnoreCase("GET_ASS_DETAIL") ||
                      urlp.cmd.equalsIgnoreCase("GET_ASS_DETAIL_XML") ){
                    
                    urlp.editAssessment();
                    
                    if( (DbCmAssessment.getStatus(con, urlp.assMgr.asm_id)).equalsIgnoreCase(DELETED) ) {
                        cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_DELETED);
                        msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_failure, out);
                    }
                    String xml = "<assessment_detail>" + cwUtils.NEWL;
                    xml += urlp.assMgr.getAssessmentXML(con);
                    xml += urlp.assMgr.getAssessmentUnitsAsXML(con);
                    xml += "</assessment_detail>" + cwUtils.NEWL;
                    xml = formatXML(xml, MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("GET_ASS_DETAIL")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }
            }else if( urlp.cmd.equalsIgnoreCase("DEL_ASSESSOR") ) {
                
                urlp.pickUser();
                long[] ids = cwUtils.splitToLong(urlp.usr_ent_id_lst, DELIMITER);
                DbCmAssessmentUnit dbAssU = new DbCmAssessmentUnit();
                dbAssU.asu_asm_id = urlp.asm_id;
                dbAssU.delNotInList(con, ids);
                
                CmAssessmentNotify assNot = new CmAssessmentNotify();
                assNot.delNotificationRecipientNotInList(con, urlp.asm_id, ids);

                con.commit();
                response.sendRedirect(urlp.url_success);                
            }else if( urlp.cmd.equalsIgnoreCase("PREVIEW_ASS") ||
                      urlp.cmd.equalsIgnoreCase("PREVIEW_ASS_XML") ) {
                
                    urlp.editAssessment();
                    if( (DbCmAssessment.getStatus(con, urlp.assMgr.asm_id)).equalsIgnoreCase(DELETED) ) {
                        cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_DELETED);
                        msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_failure, out);
                    }
                    String xml = "<assessment_survey>" + cwUtils.NEWL;
                    xml += urlp.assMgr.getAssessmentXML(con);
                    DbCmAssessment DbAss = new DbCmAssessment();
                    DbAss.asm_id = urlp.assMgr.asm_id;
                    DbAss.get(con);
                    CmSkillSetManager skillSetMgr = new CmSkillSetManager();
                    skillSetMgr.sks_skb_id = DbAss.asm_sks_skb_id;
                    xml += skillSetMgr.viewSurveyAsXML(con);
                    xml += "</assessment_survey>";
                    xml = formatXML(xml, MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("PREVIEW_ASS")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }
            }else if( urlp.cmd.equalsIgnoreCase("SUBMIT_TST_ASS") ){
                
                urlp.submitTstAss();
                /*
                DbCmAssessment dbAss = new DbCmAssessment();
                dbAss.asm_id = urlp.asm_id;
                dbAss.get(con);
                Timestamp curTime = cwSQL.getTime(con);
                if( dbAss.asm_eff_end_datetime.before(curTime) ) {                    
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_EXCEED_DUE_DATE);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_failure, out);
                    return;
                } else if( dbAss.asm_eff_start_datetime.after(curTime) ) {                    
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_NOT_STARTED_YET);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_failure, out);
                    return;
                }
                */
                
                CmAssessmentManager assMgr = new CmAssessmentManager();
                assMgr.asm_id = urlp.asm_id;
                assMgr.submitTestAssessment(con, prof.usr_ent_id, urlp.mod_res_id);
                con.commit();
                response.sendRedirect(urlp.url_success);
                
            }else if( urlp.cmd.equalsIgnoreCase("SUBMIT_ASS") ){
                
                urlp.submitSurvey();
                DbCmAssessment dbAss = new DbCmAssessment();
                dbAss.asm_id = urlp.asm_id;
                dbAss.get(con);
                Timestamp curTime = cwSQL.getTime(con);
                if( dbAss.asm_eff_end_datetime.before(curTime) ) {                    
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_EXCEED_DUE_DATE);
                    msgBox(ServletModule.MSG_STATUS, msg, urlp.url_failure, out);
                    return;
                } else if( dbAss.asm_eff_start_datetime.after(curTime) ) {                    
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_NOT_STARTED_YET);
                    msgBox(ServletModule.MSG_STATUS, msg, urlp.url_failure, out);
                    return;
                }
                
                CmAssessmentManager assMgr = new CmAssessmentManager();
                assMgr.asm_id = urlp.asm_id;
                assMgr.submitAssessment(con, prof, urlp.skillIdList, urlp.answerList, urlp.comment, urlp.submit, urlp.last_upd_timestamp);
                con.commit();
                
                cwSysMessage msg = null;
                if( urlp.submit )
                    msg = new cwSysMessage(SMSG_CMP_ASS_SUBMIT_SUCCESS);
                else
                    msg = new cwSysMessage(SMSG_CMP_ASS_SAVE_SUCCESS);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                
                
            }else if( urlp.cmd.equalsIgnoreCase("GET_ASSESSOR_RESULT") ||
                      urlp.cmd.equalsIgnoreCase("GET_ASSESSOR_RESULT_XML") ){
                
                urlp.getAssessorResult();
                CmAssessmentManager assMgr = new CmAssessmentManager();
                assMgr.asm_id = urlp.asm_id;
                String xml = "<assessment_survey>" + cwUtils.NEWL;
                    xml += assMgr.getAssessmentXML(con, prof);
                if( urlp.usr_ent_id == ReqParam.LONG_PARAMETER_NOT_FOUND )
                    urlp.usr_ent_id = prof.usr_ent_id;
                xml += assMgr.getAssessorResult(con, urlp.asm_id, urlp.usr_ent_id);
                xml += "</assessment_survey>" + cwUtils.NEWL;
                xml = formatXML(xml, MODULENAME);
                
                if (urlp.cmd.equalsIgnoreCase("GET_ASSESSOR_RESULT")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
                
            }else if( urlp.cmd.equalsIgnoreCase("GET_ASS_RESULT") || 
                      urlp.cmd.equalsIgnoreCase("GET_ASS_RESULT_XML") ) {
                
                urlp.editAssessment();
                if( (DbCmAssessment.getStatus(con, urlp.assMgr.asm_id)).equalsIgnoreCase(DELETED) ) {
                        cwSysMessage msg = new cwSysMessage(SMSG_CMP_ASS_DELETED);
                        msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_failure, out);
                    }                
                String xml = urlp.assMgr.getAssessmentXML(con);
                xml += urlp.assMgr.overallResult(con);
                
                xml = formatXML(xml, MODULENAME);
                
                if( urlp.cmd.equalsIgnoreCase("GET_ASS_RESULT") ) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }
                else {
                    static_env.outputXML(out, xml);
                }
            }else if( urlp.cmd.equalsIgnoreCase("SAVE_RESOLVED") ) {

                urlp.saveResolved();
                CmAssessmentManager assMgr = new CmAssessmentManager();
                assMgr.asm_id = urlp.asm_id;
                assMgr.saveResolvedScore(con, urlp.h_id_value, prof, urlp.last_upd_timestamp);
                con.commit();
                response.sendRedirect(urlp.url_success);                

            }else if( urlp.cmd.equalsIgnoreCase("set_user_search") ||
                      urlp.cmd.equalsIgnoreCase("set_user_search_xml") ) {
                        
                urlp.setUserSearch();
                
                StringBuffer xmlBody = new StringBuffer();
                CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                xmlBody.append("<skill_sets>").append(cwUtils.NEWL)
                       .append(gapAnalyzer.getSkillSetInfoAsXML(con, prof.root_ent_id))
                       .append("</skill_sets>").append(cwUtils.NEWL);
                   
                if( urlp.refresh ) {
                    if( sess != null )
                        sess.removeAttribute("Competency_SKILL_SET");
                } else {
                    CmSkillManager skillMgr = new CmSkillManager();
                    xmlBody.append(skillMgr.genSkillListFromSess(con, sess));
                }
                
                String xml = formatXML(xmlBody.toString(), MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("set_user_search") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);            
            }else if( urlp.cmd.equalsIgnoreCase("pick_search_skill")) {

                urlp.pickSkill();
                Hashtable data = new Hashtable();                                
                
                String id_lst = new String();
                if( urlp.picked_skl_id_lst != null )
                    id_lst += urlp.picked_skl_id_lst;
                if( urlp.skl_id_lst != null ) {
                    if( urlp.picked_skl_id_lst != null )
                        id_lst += DELIMITER;
                    id_lst += urlp.skl_id_lst;
                }

                Vector skl_id_lst = cwUtils.splitToVec(id_lst, DELIMITER);
                Vector level_lst = cwUtils.splitToVec(urlp.level_lst, DELIMITER);
                for(int i=level_lst.size(); i<skl_id_lst.size(); i++)
                    level_lst.addElement(new Long(1));
                data.put(HASH_SKILL_ID_LIST, skl_id_lst);
                data.put(HASH_SKILL_LEVEL_LIST, level_lst);
                sess.setAttribute("Competency_SKILL_SET", data);
                response.sendRedirect(urlp.url_success);
                
            }else if( urlp.cmd.equalsIgnoreCase("pick_search_skill_set") ) {
                
                urlp.pickSkillSet();
                Vector skl_id_lst = cwUtils.splitToVec(urlp.picked_skl_id_lst, DELIMITER);
                Vector level_lst = cwUtils.splitToVec(urlp.level_lst, DELIMITER);
                StringBuffer xmlBody = new StringBuffer();
                urlp.skillSetMgr.pickSkillSet(con, skl_id_lst);
                
                for(int i=level_lst.size(); i<skl_id_lst.size(); i++)
                    level_lst.addElement(new Long(1));
                
                Hashtable data = new Hashtable();
                data.put(HASH_SKILL_ID_LIST, skl_id_lst);
                data.put(HASH_SKILL_LEVEL_LIST, level_lst);
                sess.setAttribute("Competency_SKILL_SET", data);
                response.sendRedirect(urlp.url_success);                
                
            }else if( urlp.cmd.equalsIgnoreCase("SEARCH_USER") 
                  ||  urlp.cmd.equalsIgnoreCase("SEARCH_USER_XML") ) {
                
                    urlp.pickSkill();
                    StringBuffer xmlBody = new StringBuffer();
                    xmlBody.append("<search_result>").append(cwUtils.NEWL);
                    Vector skl_id_lst = cwUtils.splitToVec(urlp.skl_id_lst, DELIMITER);
                    Vector level_lst = cwUtils.splitToVec(urlp.level_lst, DELIMITER);
                    Hashtable data = new Hashtable();
                    data.put(HASH_SKILL_ID_LIST, skl_id_lst);
                    data.put(HASH_SKILL_LEVEL_LIST, level_lst);
                    sess.setAttribute("Competency_SKILL_SET", data);
                    Hashtable resultHash = CmSkillGapAnalyzer.searchUser(con, skl_id_lst, level_lst, prof, wizbini);
                    Enumeration enumeration = resultHash.keys();
                    Vector entIdVec = cwUtils.enum2vector(enumeration);
                    if( !entIdVec.isEmpty() ) {
                        Vector userVec = dbRegUser.getByEntIdsAsVec(con, entIdVec);
                        Long ent_id;
                        for(int i=0; i<userVec.size(); i++) {
                            dbRegUser usr = (dbRegUser)userVec.elementAt(i);
                            ent_id = new Long(usr.usr_ent_id);
                            xmlBody.append("<usr id=\"").append(usr.usr_id).append("\" ")
                                   .append(" ent_id=\"").append(ent_id).append("\" ")
                                   .append(" display_bil=\"").append(usr.usr_display_bil).append("\" ");
                            if( ((Boolean)resultHash.get(ent_id)).booleanValue() )
                                xmlBody.append(" status=\"ABOVE\" />").append(cwUtils.NEWL);
                            else
                                xmlBody.append(" status=\"BELOW\" />").append(cwUtils.NEWL);
                        }
                    }
                    xmlBody.append("</search_result>").append(cwUtils.NEWL);
                    
                    String xml = formatXML(xmlBody.toString(), MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("SEARCH_USER") )
                        generalAsHtml(xml, out, urlp.stylesheet);                    
                    else
                        static_env.outputXML(out, xml);                    
            
            }else if( urlp.cmd.equalsIgnoreCase("SEARCH_USER_BY_SKILL") 
                  ||  urlp.cmd.equalsIgnoreCase("SEARCH_USER_BY_SKILL_XML") ) {
                
                    urlp.searchUserBySkill();
                    //put searching data into session for comparing usage later.
                    Vector skl_id_lst = cwUtils.splitToVec(urlp.skl_id_lst, DELIMITER);
                    Vector level_lst = cwUtils.splitToVec(urlp.level_lst, DELIMITER);
                    Hashtable data = new Hashtable();
                    data.put(HASH_SKILL_ID_LIST, skl_id_lst);
                    data.put(HASH_SKILL_LEVEL_LIST, level_lst);
                    sess.setAttribute("Competency_SKILL_SET", data);
                    
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    String xml = gapAnalyzer.searchUserBySkillAsXML(con, prof,
                                                                    cwUtils.long2vector(urlp.skillIdList), 
                                                                    cwUtils.float2vector(urlp.levelList),
                                                                    cwUtils.String2vector(urlp.typeList),
                                                                    urlp.reviewDate,wizbini);
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("SEARCH_USER_BY_SKILL") )
                        generalAsHtml(xml, out, urlp.stylesheet);                    
                    else
                        static_env.outputXML(out, xml);                    
            
            }else if( urlp.cmd.equalsIgnoreCase("SEARCH_USER_BY_SKILL_SET") 
                  ||  urlp.cmd.equalsIgnoreCase("SEARCH_USER_BY_SKILL_SET_XML") ) {
                
                    urlp.searchUserBySkillSet();
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    String xml = gapAnalyzer.searchUserBySkillSetAsXML(con, prof, urlp.skillSetId,
                                                                    urlp.reviewDate,wizbini);
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("SEARCH_USER_BY_SKILL_SET") )
                        generalAsHtml(xml, out, urlp.stylesheet);                    
                    else
                        static_env.outputXML(out, xml);                    
            
            }else if( urlp.cmd.equalsIgnoreCase("GET_USER_SKILL") 
                   || urlp.cmd.equalsIgnoreCase("GET_USER_SKILL_XML") ) {
                    
                    urlp.getUserSkill();
                    CmSkillGapAnalyzer gapAnalyzer = new CmSkillGapAnalyzer();
                    
                    Hashtable data = (Hashtable)sess.getAttribute("Competency_SKILL_SET");
                    Vector skl_id_lst = (Vector)data.get(HASH_SKILL_ID_LIST);
                    Vector level_lst = (Vector)data.get(HASH_SKILL_LEVEL_LIST);
                    Hashtable skillLevelHash = new Hashtable();
                    for(int i=0; i< skl_id_lst.size(); i++)
                        skillLevelHash.put(skl_id_lst.elementAt(i), level_lst.elementAt(i));
                        
                    String xml = gapAnalyzer.cmpSearch(con, urlp.usr_ent_id, skillLevelHash, urlp.source_show_all);
                    
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("GET_USER_SKILL") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out,xml);
                
            }else if( urlp.cmd.equalsIgnoreCase("SEARCH_SKILL_SIMPLE_XML") ||
                      urlp.cmd.equalsIgnoreCase("SEARCH_SKILL_SIMPLE") ) {
                        
                    urlp.skillSearch();                     
                    CmSkillManager skillMgr = new CmSkillManager();
                    String xml = skillMgr.skillSearchAsXML(con, sess, prof.root_ent_id, urlp.cur_page, urlp.pagesize, 
                                                           urlp.pagetime, urlp.sort_by, urlp.skb_title,
                                                           null, null, null, Integer.MIN_VALUE, false);
                     
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("SEARCH_SKILL_SIMPLE") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else    
                        static_env.outputXML(out, xml);                
            }else if( urlp.cmd.equalsIgnoreCase("SEARCH_SKILL_ADV_XML") ||
                      urlp.cmd.equalsIgnoreCase("SEARCH_SKILL_ADV") ) {
                    
                    urlp.skillSearch();                     
                    CmSkillManager skillMgr = new CmSkillManager();
                    String xml = skillMgr.skillSearchAsXML(con, sess, prof.root_ent_id, urlp.cur_page, 
                                                           urlp.pagesize, urlp.pagetime, urlp.sort_by, 
                                                           urlp.skb_title, urlp.skb_description,
                                                           urlp.sle_label, urlp.sle_description,
                                                           urlp.sle_level, true );
                     
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("SEARCH_SKILL_ADV") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);                
            }else if( urlp.cmd.equalsIgnoreCase("SEARCH_SCALE_SIMPLE") ||
                      urlp.cmd.equalsIgnoreCase("SEARCH_SCALE_SIMPLE_XML") ) {
                
                    urlp.scaleSearch();
                    CmSkillManager skillMgr = new CmSkillManager();
                    String xml = skillMgr.scaleSearchAsXML(con, sess, prof.root_ent_id, urlp.cur_page,
                                                           urlp.pagesize, urlp.pagetime, urlp.sort_by,
                                                           urlp.order_by, urlp.ssl_title, null,
                                                           null, null, Integer.MIN_VALUE, false);

                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("SEARCH_SCALE_SIMPLE") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);
            }else if( urlp.cmd.equalsIgnoreCase("SEARCH_SCALE_ADV") ||
                      urlp.cmd.equalsIgnoreCase("SEARCH_SCALE_ADV_XML") ) {
                
                    urlp.scaleSearch();
                    CmSkillManager skillMgr = new CmSkillManager();
                    String xml = skillMgr.scaleSearchAsXML(con, sess, prof.root_ent_id, urlp.cur_page,
                                                           urlp.pagesize, urlp.pagetime, urlp.sort_by,
                                                           urlp.order_by, urlp.ssl_title, urlp.ssl_share,
                                                           urlp.sle_label, urlp.sle_description, 
                                                           urlp.sle_level, true);

                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("SEARCH_SCALE_ADV") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);
            }else if( urlp.cmd.equalsIgnoreCase("GET_ASSIGNED_ASS") ||
                      urlp.cmd.equalsIgnoreCase("GET_ASSIGNED_ASS_XML") ) {
                
                    urlp.getAssignedAss();
                    CmAssessmentManager assMgr = new CmAssessmentManager();
                    String xml;
                    if( urlp.usr_ent_id > 0 )
                        xml = assMgr.getAssignedAss(con, urlp.usr_ent_id, true);
                    else
                        xml = assMgr.getAssignedAss(con, prof.usr_ent_id, true);
                        
                    xml = formatXML(xml, MODULENAME);
                    if( urlp.cmd.equalsIgnoreCase("GET_ASSIGNED_ASS") )
                        generalAsHtml(xml, out, urlp.stylesheet);
                    else
                        static_env.outputXML(out, xml);                
            }else if( urlp.cmd.equalsIgnoreCase("UPD_AU_LIST") ) {
                    
                    urlp.updAssessmentUnits();                    
                    
                    long[] entIdArray = cwUtils.splitToLong(urlp.usr_ent_id_lst, DELIMITER);
                    String[] typeArray = cwUtils.splitToString(urlp.usr_type_lst, DELIMITER);
                    long[] weightArray = cwUtils.splitToLong(urlp.usr_weight_lst, DELIMITER);
                    
                    urlp.assMgr.updAssessmentUnits(con, entIdArray, typeArray, weightArray);
                    con.commit();
                    
                    response.sendRedirect(urlp.url_success);
            }else if( urlp.cmd.equalsIgnoreCase("GET_INS_ASSESSMENT") ||
                      urlp.cmd.equalsIgnoreCase("GET_INS_ASSESSMENT_XML") ) {
                urlp.editAssessment();
                String xml = new String();
                xml = urlp.assMgr.getAssessmentReferenceDataAsXML(con, prof.root_ent_id, true);
                
                String curTimeTag = "<cur_time>" + cwSQL.getTime(con) + "</cur_time>";
                xml = formatXML(xml, curTimeTag, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("GET_INS_ASSESSMENT") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);
            }else if( urlp.cmd.equalsIgnoreCase("GET_PREP_ASSESSMENT") ||
                      urlp.cmd.equalsIgnoreCase("GET_PREP_ASSESSMENT_XML") ) {
                    urlp.batchPrepAssessment();
                    
                    String xml = "<batch_assessment_list>" + cwUtils.NEWL;
                    // for each assessment
                    xml += urlp.assMgr.getPrepAssessmentXML(con, urlp.batchAsm, urlp.usr_type,
                                urlp.self_ass, urlp.selfNotifyDate, urlp.selfDueDate, 
                                urlp.assessorRoleList, urlp.assessorNotifyDate, urlp.assessorDueDate,
                                urlp.resolverRoleList, urlp.resolverNotifyDate, urlp.resolverDueDate);
                    xml += "</batch_assessment_list>" + cwUtils.NEWL;
                    xml = formatXML(xml, MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("GET_PREP_ASSESSMENT")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }
            }else if( urlp.cmd.equalsIgnoreCase("INS_ASSESSMENT") ||
                      urlp.cmd.equalsIgnoreCase("INS_ASSESSMENT_XML") ) {
                urlp.batchInsAssessment();
                    
                CmAssessmentManager mgr = new CmAssessmentManager();
                if( urlp.sender_usr_id == null ) {
                    urlp.sender_usr_id = acSite.getSysUsrId(con, prof.root_ent_id);
                }
                
                // insert assessment and assessment unit
                mgr.insAssessment(con, urlp.batchAsm[0], prof, urlp.sender_usr_id, 
                                urlp.notify_msg_subject, urlp.collect_msg_subject, 
                                urlp.self_ass, urlp.selfNotifyDate, urlp.selfDueDate,
                                urlp.assessorEntIdList, urlp.assessorTypeList,
                                urlp.auaTypeList, urlp.auaNotifyDateList, urlp.auaDueDateList);
                                  
                con.commit();
                if( sess != null )
                    sess.removeAttribute("Competency_ASSESSMENT");
                if( urlp.multi_ins ) {
                    response.sendRedirect(urlp.url_success);
                } else {
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_INS_ASS);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                }

            }else if( urlp.cmd.equalsIgnoreCase("UPDATE_PREP_ASSESSMENT") ||
                      urlp.cmd.equalsIgnoreCase("UPDATE_PREP_ASSESSMENT_XML") ) {
                // edit batch-type assessment, not edit in a batch
                urlp.editAssessment();
                String xml = new String();
                xml = urlp.assMgr.getAssessmentReferenceDataAsXML(con, prof.root_ent_id);
                xml += "<assessment_detail>" + cwUtils.NEWL;
                xml += urlp.assMgr.getAssessmentXML(con);
                xml += urlp.assMgr.getAssessmentUnitTypeAttrAsXML(con);
                xml += urlp.assMgr.getAssessmentUnitsAsXML(con, true);
                xml += "</assessment_detail>" + cwUtils.NEWL;
                
                String curTimeTag = "<cur_time>" + cwSQL.getTime(con) + "</cur_time>";
                xml = formatXML(xml, curTimeTag, MODULENAME);
                if( urlp.cmd.equalsIgnoreCase("UPDATE_PREP_ASSESSMENT") )
                    generalAsHtml(xml, out, urlp.stylesheet);
                else
                    static_env.outputXML(out, xml);
                    
            } else if( urlp.cmd.equalsIgnoreCase("UPDATE_ASSESSMENT") ||
                       urlp.cmd.equalsIgnoreCase("UPDATE_ASSESSMENT_XML") ) {

                urlp.batchUpdateAssessment();
                if( urlp.sender_usr_id == null ) {
                    urlp.sender_usr_id = acSite.getSysUsrId(con, prof.root_ent_id);
                }

                DbCmAssessment dbAss = new DbCmAssessment();
                dbAss.asm_id = urlp.batchAsm[0].asm_id;
                if( !dbAss.validUpdTimestap(con, urlp.last_upd_timestamp) ){
                    cwSysMessage msg = new cwSysMessage(SMSG_CMP_INVALID_TIMESTAMP);
                    msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);
                    return;                    
                }
                dbAss.updTimestamp(con);

                CmAssessmentManager mgr = new CmAssessmentManager();
                mgr.updAssessment(con, urlp.batchAsm[0], prof, urlp.sender_usr_id
                                 ,urlp.auaTypeList, urlp.auaNotifyDateList
                                 ,urlp.auaDueDateList
                                 ,urlp.notify_msg_subject, urlp.collect_msg_subject);

                for(int i=0; i<urlp.assessorTypeEntIdList.length; i++) {
                    mgr.updAssessmentUnits(con, urlp.batchAsm[0].asm_id, urlp.assessorTypeEntIdList[i], urlp.assessorTypeList[i], urlp.weightList[i]);
                }
                
                con.commit();

                cwSysMessage msg = new cwSysMessage(SMSG_CMP_UPD_ASS);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);

            }else if( urlp.cmd.equalsIgnoreCase("VIEW_SURVEY") ||
                      urlp.cmd.equalsIgnoreCase("VIEW_SURVEY_XML") ) {
                
                urlp.getSkillSet();
                String xml = new String();
                CmSkillSetManager skillSetMgr = new CmSkillSetManager();
                skillSetMgr.sks_skb_id = urlp.skillSet.sks_skb_id;
                xml = skillSetMgr.viewSurveyAsXML(con);                    
                if(sess != null) {
                    sess.removeAttribute("Competency_SKILL_SET");
                }
                
                xml = formatXML(xml, MODULENAME);

                if( urlp.cmd.equalsIgnoreCase("VIEW_SURVEY") ) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            }else {
                throw new cwSysMessage("GEN000");
            }
        } catch(SQLException e) {    
            CommonLog.error(e.getMessage(),e);
            throw e;
        }catch (cwSysMessage se) {
            try {
                 con.rollback();
                 CommonLog.error(se.getMessage(),se);
                 msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
                 CommonLog.error("SQL error: " + sqle.getMessage(),sqle);
             }
        }
    }
        
}



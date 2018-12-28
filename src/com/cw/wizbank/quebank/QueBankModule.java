package com.cw.wizbank.quebank;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbQuestion;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.quebank.quecontainer.DynamicAssessment;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedAssessment;
import com.cw.wizbank.quebank.quecontainer.FixedQueContainer;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cwn.wizbank.utils.CommonLog;

public class QueBankModule extends ServletModule {

    public QueBankModule() {
        ;
    }

    public void process() throws SQLException, cwException, IOException {
        //System.out.println("IN NETgTrackingModule");

        if (prof == null)
        	CommonLog.info("login profile is null.");
        else
        	CommonLog.info("loginProfile  > usr_id :" + prof.usr_id);

        // get output stream for normal content to client
        PrintWriter out = response.getWriter();

        QueBankReqParam urlp = null;

        urlp = new QueBankReqParam(bMultipart, request, multi, clientEnc, static_env.ENCODING);

        // service processing starts here
        try {
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            } else if (urlp.cmd == null) {
                throw new cwException("invalid command");

            } else if (urlp.cmd.toUpperCase().startsWith("INS_FIXED_ASSESSMENT")) {
                urlp.fixed_que_container();

                FixedAssessment myFixedAssessment = new FixedAssessment();

                myFixedAssessment.res_usr_id_owner = prof.usr_id;
                myFixedAssessment.res_upd_user = prof.usr_id;

                myFixedAssessment.res_type = urlp.res_type;
                myFixedAssessment.res_subtype = urlp.res_subtype;
                myFixedAssessment.res_title = urlp.res_title;
                myFixedAssessment.res_desc = urlp.res_desc;
                if (urlp.res_duration > -1) {
                    myFixedAssessment.res_duration = urlp.res_duration;
                }
                myFixedAssessment.res_lan = urlp.res_lan;
                myFixedAssessment.res_privilege = urlp.res_privilege;
                myFixedAssessment.res_status = urlp.res_status;
                if (urlp.res_difficulty > -1) {
                    myFixedAssessment.res_difficulty = urlp.res_difficulty;
                }
                myFixedAssessment.qct_allow_shuffle_ind = urlp.qct_allow_shuffle_ind;
                myFixedAssessment.qct_select_logic = urlp.qct_select_logic;
              	if(wizbini.cfgTcEnabled) {
     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
     	        	String code = acTc.hasObjInMgtTc(prof.usr_ent_id, urlp.obj_id, prof.current_role); 
     	        	if(!code.equals(dbObjective.CAN_MGT_OBJ)) {
     	        		throw new qdbErrMessage(code);
     	        	}
              	}
                myFixedAssessment.ins(con, urlp.obj_id, prof);

                urlp.url_success = cwUtils.substituteURL(urlp.url_success, myFixedAssessment.res_id);

                con.commit();
                msgBox(MSG_STATUS, new cwSysMessage("ASM009"), urlp.url_success, out);
            } else if (urlp.cmd.toUpperCase().startsWith("INS_DYNAMIC_ASSESSMENT")) {
            	urlp.fixed_que_container();

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();

                myDynamicAssessment.res_usr_id_owner = prof.usr_id;
                myDynamicAssessment.res_upd_user = prof.usr_id;

                myDynamicAssessment.res_type = urlp.res_type;
                myDynamicAssessment.res_subtype = urlp.res_subtype;
                myDynamicAssessment.res_title = urlp.res_title;
                myDynamicAssessment.res_desc = urlp.res_desc;
                if (urlp.res_duration > -1) {
                    myDynamicAssessment.res_duration = urlp.res_duration;
                }
                myDynamicAssessment.res_lan = urlp.res_lan;
                myDynamicAssessment.res_privilege = urlp.res_privilege;
                myDynamicAssessment.res_status = urlp.res_status;
                if (urlp.res_difficulty > -1) {
                    myDynamicAssessment.res_difficulty = urlp.res_difficulty;
                }
                myDynamicAssessment.qct_allow_shuffle_ind = urlp.qct_allow_shuffle_ind;
                myDynamicAssessment.qct_select_logic = urlp.qct_select_logic;
              	if(wizbini.cfgTcEnabled) {
     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
     	        	String code = acTc.hasObjInMgtTc(prof.usr_ent_id, urlp.obj_id, prof.current_role); 
     	        	if(!code.equals(dbObjective.CAN_MGT_OBJ)) {
     	        		throw new qdbErrMessage(code);
     	        	}
              	}
                myDynamicAssessment.ins(con, urlp.obj_id, prof);

                urlp.url_success = cwUtils.substituteURL(urlp.url_success, myDynamicAssessment.res_id);

                con.commit();

                msgBox(MSG_STATUS, new cwSysMessage("ASM009"), urlp.url_success, out);
                
            } else if (urlp.cmd.toUpperCase().startsWith("UPD_FIXED_ASSESSMENT")) {
                urlp.fixed_que_container();

                FixedAssessment myFixedAssessment = new FixedAssessment();

                myFixedAssessment.res_id = urlp.res_id;
                myFixedAssessment.res_usr_id_owner = prof.usr_id;
                myFixedAssessment.res_upd_user = prof.usr_id;

                myFixedAssessment.res_type = urlp.res_type;
                myFixedAssessment.res_subtype = urlp.res_subtype;
                myFixedAssessment.res_title = urlp.res_title;
                myFixedAssessment.res_desc = urlp.res_desc;
                if (urlp.res_duration > -1) {
                    myFixedAssessment.res_duration = urlp.res_duration;
                }
                myFixedAssessment.res_lan = urlp.res_lan;
                myFixedAssessment.res_privilege = urlp.res_privilege;
                myFixedAssessment.res_status = urlp.res_status;
                if (urlp.res_difficulty > -1) {
                    myFixedAssessment.res_difficulty = urlp.res_difficulty;
                }
                myFixedAssessment.qct_allow_shuffle_ind = urlp.qct_allow_shuffle_ind;
                myFixedAssessment.qct_select_logic = urlp.qct_select_logic;
                myFixedAssessment.res_upd_date = urlp.res_timestamp;
              	if(wizbini.cfgTcEnabled) {
     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
     	        	String code = acTc.hasResInMgtTc(prof.usr_ent_id, urlp.res_id, prof.current_role);
     	        	if(!code.equals(dbResource.CAN_MGT_RES)) {
     	        		throw new qdbErrMessage(code);
     	        	}
              	}
                myFixedAssessment.upd(con, prof);

                con.commit();

                msgBox(MSG_STATUS, new cwSysMessage("ASM010"), urlp.url_success, out);
            } else if (urlp.cmd.toUpperCase().startsWith("UPD_DYNAMIC_ASSESSMENT")) {
                urlp.fixed_que_container();

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();

                myDynamicAssessment.res_usr_id_owner = prof.usr_id;
                myDynamicAssessment.res_upd_user = prof.usr_id;

                myDynamicAssessment.res_id = urlp.res_id;
                myDynamicAssessment.res_type = urlp.res_type;
                myDynamicAssessment.res_subtype = urlp.res_subtype;
                myDynamicAssessment.res_title = urlp.res_title;
                myDynamicAssessment.res_desc = urlp.res_desc;
                if (urlp.res_duration > -1) {
                    myDynamicAssessment.res_duration = urlp.res_duration;
                }
                myDynamicAssessment.res_lan = urlp.res_lan;
                myDynamicAssessment.res_privilege = urlp.res_privilege;
                myDynamicAssessment.res_status = urlp.res_status;
                if (urlp.res_difficulty > -1) {
                    myDynamicAssessment.res_difficulty = urlp.res_difficulty;
                }
                myDynamicAssessment.qct_allow_shuffle_ind = urlp.qct_allow_shuffle_ind;
                myDynamicAssessment.qct_select_logic = urlp.qct_select_logic;
                myDynamicAssessment.res_upd_date = urlp.res_timestamp;
              	if(wizbini.cfgTcEnabled) {
     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
     	        	String code = acTc.hasResInMgtTc(prof.usr_ent_id, urlp.res_id, prof.current_role); 
     	        	if(!code.equals(dbResource.CAN_MGT_RES)) {
     	        		throw new qdbErrMessage(code);
     	        	}
              	}
                myDynamicAssessment.upd(con, prof);

                con.commit();

                msgBox(MSG_STATUS, new cwSysMessage("ASM010"), urlp.url_success, out);
            } else if (urlp.cmd.toUpperCase().startsWith("VALIDATE_DYNAMIC_ASSESSMENT")) {
                urlp.que_container();

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();

                myDynamicAssessment.res_id = urlp.res_id;

                if (myDynamicAssessment.hasEnoughQue(con)) {
                    msgBox(MSG_STATUS, new cwSysMessage("ASM005"), urlp.url_success, out);
                } else {
                    msgBox(MSG_ERROR, new cwSysMessage("ASM006"), urlp.url_success, out);
                }

            } else if (urlp.cmd.toUpperCase().startsWith("ADD_QUE_CONTAINER_OBJ")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                FixedAssessment myFixedAssessment = new FixedAssessment();
                myFixedAssessment.res_id = urlp.res_id;
                myFixedAssessment.res_type = urlp.res_type;
                myFixedAssessment.res_subtype = urlp.res_subtype;
                myFixedAssessment.get(con);

                myFixedAssessment.addObj(con, urlp.obj_id, prof.usr_id);

                con.commit();

                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.toUpperCase().startsWith("DEL_QUE_CONTAINER_OBJ")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                FixedAssessment myFixedAssessment = new FixedAssessment();
                myFixedAssessment.res_id = urlp.res_id;
                myFixedAssessment.delObj(con, urlp.obj_id, prof);

                con.commit();

                msgBox(MSG_STATUS, new cwSysMessage("ASM007"), urlp.url_success, out);
            } else if (urlp.cmd.toUpperCase().startsWith("ADD_QUE")) {
                urlp.fixed_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                FixedAssessment myFixedAssessment = new FixedAssessment();
                myFixedAssessment.res_id = urlp.res_id;
                myFixedAssessment.res_type = urlp.res_type;
                myFixedAssessment.res_subtype = urlp.res_subtype;
                myFixedAssessment.addQuestion(con, urlp.que_id_lst, static_env.INI_DIR_UPLOAD, prof, false, null);

                con.commit();

                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.toUpperCase().startsWith("DEL_QUE")) {
                urlp.fixed_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                FixedAssessment myFixedAssessment = new FixedAssessment();
                myFixedAssessment.res_id = urlp.res_id;
                myFixedAssessment.res_upd_date = urlp.res_timestamp;
                myFixedAssessment.removeQuestion(con, urlp.que_id_lst, prof);

                con.commit();

                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.toUpperCase().startsWith("GET_FIXED_ASSESSMENT")) {
                urlp.fixed_que_container();
              	if(wizbini.cfgTcEnabled) {
     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
     	        	String code = acTc.hasResInMgtTc(prof.usr_ent_id, urlp.res_id, prof.current_role); 
     	        	if(!code.equals(dbResource.CAN_MGT_RES)) {
     	        		throw new qdbErrMessage(code);
     	        	}
              	}
                AcResources acres = new AcResources(con);
                boolean checkStatusRes = (!acres.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));

                FixedAssessment myFixedAssessment = new FixedAssessment();
                myFixedAssessment.res_id = urlp.res_id;
                myFixedAssessment.res_type = urlp.res_type;
                myFixedAssessment.res_subtype = urlp.res_subtype;
                myFixedAssessment.get(con);

                String xml = "";
                xml = myFixedAssessment.asXML(con, prof, checkStatusRes);

                if (urlp.cmd.equalsIgnoreCase("GET_FIXED_ASSESSMENT_XML")) {
                    out.println(xml);
                } else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }

            } else if (urlp.cmd.equalsIgnoreCase("get_fsc_content") || urlp.cmd.equalsIgnoreCase("get_fsc_content_xml")) {
                urlp.getFSCContent();

                AcResources acres = new AcResources(con);
                boolean checkStatusRes = (!acres.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));

                FixedScenarioQue que = new FixedScenarioQue();
                que.res_id = urlp.res_id;
                que.get(con);

                String xml = que.getObjAsXML(con, prof);

                if (urlp.cmd.equalsIgnoreCase("get_fsc_content_xml")) {
                    out.println(xml);
                } else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }

            } else if (urlp.cmd.equalsIgnoreCase("get_dsc_content") || urlp.cmd.equalsIgnoreCase("get_dsc_content_xml")) {
                urlp.getDSCContent();

                AcResources acres = new AcResources(con);
                boolean checkStatusRes = (!acres.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));

                DynamicScenarioQue que = new DynamicScenarioQue();
                que.res_id = urlp.res_id;
                que.get(con);

                String xml = que.getObjAsXML(con, prof);

                if (urlp.cmd.equalsIgnoreCase("get_dsc_content_xml")) {
                    out.println(xml);
                } else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }
            } else if (urlp.cmd.equalsIgnoreCase("upd_qct_shuffle_ind")) {
                urlp.updFSCShuffleInd();

                // check User Right on the Question
                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                FixedScenarioQue que = new FixedScenarioQue();
                que.res_id = urlp.res_id;
                que.res_upd_date = urlp.res_upd_timestamp;
                que.qct_allow_shuffle_ind = urlp.qct_allow_shuffle_ind;
                que.updateShuffleInd(con, prof);

                con.commit();
                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.equalsIgnoreCase("reorder_que")) {
                urlp.reorderQue();

                // check User Right on the Container
                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }
                
                FixedQueContainer container = new FixedQueContainer();
                container.res_id = urlp.res_id;
                container.res_upd_date = urlp.res_upd_timestamp;
                if (!container.canReorder(con)) {
                    throw new cwSysMessage("MOD004");
                }

                container.reorderQ(con, urlp.que_id_lst, urlp.que_order_lst, prof);
                con.commit();
                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.toUpperCase().equals("GET_DYNAMIC_ASSESSMENT") || urlp.cmd.toUpperCase().equals("GET_DYNAMIC_ASSESSMENT_XML")) {
                urlp.fixed_que_container();
              	if(wizbini.cfgTcEnabled) {
     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
     	        	String code = acTc.hasResInMgtTc(prof.usr_ent_id, urlp.res_id, prof.current_role); 
     	        	if(!code.equals(dbResource.CAN_MGT_RES)) {
     	        		throw new qdbErrMessage(code);
     	        	}
              	}
                AcResources acres = new AcResources(con);
                boolean checkStatusRes = (!acres.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();
                myDynamicAssessment.res_id = urlp.res_id;
                myDynamicAssessment.res_type = urlp.res_type;
                myDynamicAssessment.res_subtype = urlp.res_subtype;
                myDynamicAssessment.get(con);

                String xml = "";
                xml = myDynamicAssessment.asXML(con, prof, urlp.mode);

                if (urlp.cmd.equalsIgnoreCase("GET_DYNAMIC_ASSESSMENT_XML")) {
                    out.println(xml);
                } else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }

            } else if (urlp.cmd.toUpperCase().equals("GET_ASSESSMENT_WITH_TPL") || urlp.cmd.toUpperCase().equals("GET_ASSESSMENT_WITH_TPL_XML")) {
                urlp.fixed_que_container();
                urlp.get_tpl();

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();
                myDynamicAssessment.res_id = urlp.res_id;
                myDynamicAssessment.get(con);
              	if(wizbini.cfgTcEnabled) {
     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
     	        	String code = acTc.hasResInMgtTc(prof.usr_ent_id, urlp.res_id, prof.current_role); 
     	        	if(!code.equals(dbResource.CAN_MGT_RES)) {
     	        		throw new qdbErrMessage(code);
     	        	}
              	}
                String xml = "";
                xml = myDynamicAssessment.asXML(con, prof, false, urlp.mode);
                String result = urlp.dbtpl.tplListAsXML(con, prof, urlp.course_id, urlp.dpo_view, urlp.dbtpl.tpl_type, urlp.dbtpl.tpl_type, xml);
                if (urlp.cmd.equalsIgnoreCase("GET_ASSESSMENT_WITH_TPL_XML")) {
                    out.println(result);
                } else {
                    generalAsHtml(result, out, urlp.stylesheet);
                }
            } else if (urlp.cmd.toUpperCase().startsWith("GET_ASSESSMENT_OBJ")) {
                urlp.fixed_que_container();

                AcResources acres = new AcResources(con);
                boolean checkStatusRes = (!acres.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
                if(wizbini.cfgTcEnabled && urlp.res_id >0 && urlp.obj_id >0){
                	Vector vec  = dbResourceObjective.getObjId(con, urlp.res_id);
                	dbResourceObjective dbres = (dbResourceObjective)vec.get(0);
                	if(dbres !=null) {
                		
                		dbObjective res_obj  = new dbObjective(dbres.rob_obj_id);
                		res_obj.get(con);
                		
                		dbObjective obj  = new dbObjective(urlp.obj_id);
                		obj.get(con);
                		
                		/*if(res_obj.obj_tcr_id != obj.obj_tcr_id && (!res_obj.obj_share_ind && !obj.obj_share_ind) ) {//应该考虑共享文件夹
                			throw new qdbErrMessage("OBJ001");
                		}*/
                	}
                }
                
                FixedAssessment myFixedAssessment = new FixedAssessment();
                myFixedAssessment.res_id = urlp.res_id;
                myFixedAssessment.res_type = urlp.res_type;
                myFixedAssessment.res_subtype = urlp.res_subtype;
                myFixedAssessment.get(con);

                String xml = "";
                xml = myFixedAssessment.getObjAsXML(con, prof, urlp.mode);

                if (urlp.cmd.equalsIgnoreCase("GET_ASSESSMENT_OBJ_XML")) {
                    out.println(xml);
                } else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }

            } else if (urlp.cmd.toUpperCase().startsWith("GET_CRIT_SPEC")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();
                myDynamicAssessment.res_id = urlp.res_id;
                myDynamicAssessment.res_type = urlp.res_type;
                myDynamicAssessment.res_subtype = urlp.res_subtype;

                String xml = "";
                xml = myDynamicAssessment.getSpecAsXML(con, urlp.qcs_id, prof);

                if (urlp.cmd.equalsIgnoreCase("GET_CRIT_SPEC_XML")) {
                    out.println(xml);
                } else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }
            } else if (urlp.cmd.toUpperCase().startsWith("GET_DSC_CRIT_SPEC")) {
                urlp.getDSCCritSpec();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                DynamicScenarioQue que = new DynamicScenarioQue();
                que.res_id = urlp.res_id;
                que.get(con);

                String xml = que.getSpecAsXML(con, urlp.qcs_id, prof);
                if (urlp.cmd.equalsIgnoreCase("GET_DSC_CRIT_SPEC_XML")) {
                    out.println(xml);
                } else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }
            } else if (urlp.cmd.toUpperCase().startsWith("ADD_CRIT_SPEC")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();
                myDynamicAssessment.res_id = urlp.res_id;
                myDynamicAssessment.res_type = urlp.res_type;
                myDynamicAssessment.res_subtype = urlp.res_subtype;
                myDynamicAssessment.get(con);

                myDynamicAssessment.addSpec(con, urlp.qcs_type, urlp.qcs_score, urlp.qcs_difficulty, urlp.qcs_privilege, urlp.qcs_duration, urlp.qcs_qcount, urlp.qcs_obj_id, prof.usr_id);

                con.commit();

                msgBox(MSG_STATUS, new cwSysMessage("CRT001"), urlp.url_success, out);
            } else if (urlp.cmd.toUpperCase().startsWith("ADD_DSC_CRIT_SPEC")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }
                
                //if a module include this sc question has been Visited,spec can't added
                if (DynamicScenarioQue.mod_is_visited(con, urlp.res_id)) {
                    throw new cwSysMessage("MOD004");
                }

                DynamicScenarioQue que = new DynamicScenarioQue();
                que.res_id = urlp.res_id;
                que.res_type = urlp.res_type;
                que.res_subtype = urlp.res_subtype;
                que.get(con);

                que.addSpec(con, urlp.qcs_type, urlp.qcs_score, urlp.qcs_difficulty, urlp.qcs_privilege, urlp.qcs_duration, urlp.qcs_qcount, urlp.qcs_obj_id, prof.usr_id);
                //update the max score of the module include this dsc question after spec added.
                dbQuestion.udpModMaxScore(con, que.res_id);

                con.commit();
                response.sendRedirect(urlp.url_success);
                //msgBox(MSG_STATUS, con, new cwSysMessage("CRT001"), prof, urlp.url_success, out); 
            } else if (urlp.cmd.toUpperCase().startsWith("UPD_CRIT_SPEC")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();
                myDynamicAssessment.res_id = urlp.res_id;
                myDynamicAssessment.res_type = urlp.res_type;
                myDynamicAssessment.res_subtype = urlp.res_subtype;
                myDynamicAssessment.get(con);

                myDynamicAssessment.updSpec(con, urlp.qcs_id, urlp.qcs_type, urlp.qcs_score, urlp.qcs_difficulty, urlp.qcs_privilege, urlp.qcs_duration, urlp.qcs_qcount, prof.usr_id);

                con.commit();

                msgBox(MSG_STATUS, new cwSysMessage("CRT002"), urlp.url_success, out);
            } else if (urlp.cmd.toUpperCase().startsWith("UPD_DSC_CRIT_SPEC")) {
                urlp.updDSCCritSpec();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                //if a module include this sc question has been Visited,spec can't edit
                if (DynamicScenarioQue.mod_is_visited(con, urlp.res_id)) {
                    throw new cwSysMessage("MOD004");
                }
                
                DynamicScenarioQue que = new DynamicScenarioQue();
                que.res_id = urlp.res_id;
                que.res_type = urlp.res_type;
                que.res_subtype = urlp.res_subtype;
                que.get(con);

                que.updSpec(con, urlp.qcs_id, urlp.qcs_type, urlp.qcs_score, urlp.qcs_difficulty, urlp.qcs_privilege, urlp.qcs_duration, urlp.qcs_qcount, prof.usr_id);
                //update the max score of the module include this dsc question after spec updated.
                dbQuestion.udpModMaxScore(con, que.res_id);

                con.commit();
                response.sendRedirect(urlp.url_success);
                //msgBox(MSG_STATUS, con, new cwSysMessage("CRT002"), prof, urlp.url_success, out); 
            } else if (urlp.cmd.toUpperCase().startsWith("DEL_CRIT_SPEC")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                DynamicAssessment myDynamicAssessment = new DynamicAssessment();
                myDynamicAssessment.res_id = urlp.res_id;
                myDynamicAssessment.res_type = urlp.res_type;
                myDynamicAssessment.res_subtype = urlp.res_subtype;
                myDynamicAssessment.get(con);

                myDynamicAssessment.removeSpec(con, urlp.qcs_id);

                con.commit();

                msgBox(MSG_STATUS, new cwSysMessage("CRT003"), urlp.url_success, out);
            } else if (urlp.cmd.toUpperCase().startsWith("DEL_DSC_CRIT_SPEC")) {
                urlp.dynamic_que_container();

                AcResources acres = new AcResources(con);
                if (!acres.checkResPermission(prof, urlp.res_id)) {
                    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                //if a module include this sc question has been Visited,spec can't be deleted
                if (DynamicScenarioQue.mod_is_visited(con, urlp.res_id)) {
                    throw new cwSysMessage("MOD004");
                }
                
                DynamicScenarioQue que = new DynamicScenarioQue();
                que.res_id = urlp.res_id;
                que.get(con);

                que.removeSpec(con, urlp.qcs_id);
                //update the max score of the module include this dsc question after spec deleted.
                dbQuestion.udpModMaxScore(con, que.res_id);
                con.commit();
                response.sendRedirect(urlp.url_success);
                //msgBox(MSG_STATUS, con, new cwSysMessage("CRT003"), prof, urlp.url_success, out); 
            } else {
                // do nothing
            }
        } catch (cwSysMessage e) {
            try {
                con.rollback();
                msgBox(ServletModule.MSG_ERROR, e, urlp.url_failure, out);
            } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
            } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
            }
        } catch (qdbException e) {
            CommonLog.error(e.getMessage(),e);
            try {
                con.rollback();
                msgBox(ServletModule.MSG_ERROR, new cwSysMessage(e.toString()), urlp.url_failure, out);
            } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
            } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
            }
        } catch (qdbErrMessage e) {
            try {
                con.rollback();
                msgBox(ServletModule.MSG_ERROR, new cwSysMessage(e.getId()), urlp.url_failure, out);
            } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
            } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
            }
        }
    }
}
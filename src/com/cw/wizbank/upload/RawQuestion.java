package com.cw.wizbank.upload;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.db.DbQueContainerSpec;
import com.cw.wizbank.db.DbUploadLog;
import com.cw.wizbank.db.DbRawQuestion;
import com.cw.wizbank.db.view.ViewQueContainer;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

public class RawQuestion {
    
    private Hashtable objTitleHash = null;
    
    public final static String SUCCESS_CNT = "SUCCESS_CNT";
    public final static String FAILURE_CNT = "FAILURE_CNT";
    public final static String ERROR_CNT = "ERROR_CNT";
    /*
    public static String uploadHistAsXML(Connection con,long ulg_id, Vector failVec)
        throws SQLException, cwSysMessage
    {
        DbUploadLog ulg = new DbUploadLog();
        ulg.ulg_id = ulg_id;
        boolean bExist = ulg.get(con);
        if (!bExist) {
            throw new cwSysMessage(UploadUtils.SMSG_ULG_INVALID_ULG_RECROD);
        }
        StringBuffer xml = new StringBuffer();
        xml.append("<upload_history>").append(cwUtils.NEWL);
        xml.append(ulg.asXML());
        xml.append(RawQuestion.asXML(con, ulg_id));
        xml.append("<failed_lines>").append(cwUtils.NEWL);
        for (int i=0;i<failVec.size();i++) {
            xml.append("<line num=\"").append(((Long) failVec.elementAt(i)).toString())
               .append("\"/>").append(cwUtils.NEWL);
        }
        xml.append("</failed_lines>").append(cwUtils.NEWL);
        xml.append("</upload_history>").append(cwUtils.NEWL);
        
        return xml.toString();
    }
    */
    public static String asXML(Connection con,long ulg_id)
        throws SQLException, cwSysMessage
    {
        
        Vector raqVec = null;
        raqVec = DbRawQuestion.getQuetionSet(con, ulg_id);

        DbRawQuestion raq = null;
        
        StringBuffer xml = new StringBuffer();
        xml.append("<question_list upload_id=\"").append(ulg_id).append("\">").append(cwUtils.NEWL);
        
        for (int i=0;i<raqVec.size();i++) {
        
            raq = (DbRawQuestion) raqVec.elementAt(i);
            
            xml.append("<question line_num=\"").append(raq.raq_line_num)
               .append("\" language=\"").append(raq.raq_lan);
               
               if (raq.resource_id > 0) {
                   xml.append("\" resource_id=\"").append(raq.resource_id);
               } else {
                   xml.append("\" resource_id=\"");
               }
               xml.append("\" owner=\"").append(raq.raq_usr_id_owner)
               .append("\" type=\"").append(raq.raq_type)
               .append("\">").append(cwUtils.NEWL);
            
            xml.append("<header difficulty=\"").append(raq.raq_difficulty)
               .append("\" duration=\"").append(raq.raq_duration)
               .append("\" privilege=\"").append(raq.raq_privilege)
               .append("\" status=\"").append(raq.raq_status)
               .append("\" que_shuffle=\"").append(raq.raq_shuffle)
               .append("\" que_submit_file=\"").append(raq.raq_submit_file_ind)
               .append("\">").append(cwUtils.NEWL)
               .append("<title>").append(cwUtils.esc4XML(raq.raq_title))
               .append("</title>").append(cwUtils.NEWL);
               if (raq.raq_criteria != null && raq.raq_criteria.length() > 0) {
                   xml.append("<dsc_criteria>").append(raq.raq_criteria).append("</dsc_criteria>");
               }
               xml.append("<desc>").append(cwUtils.esc4XML(raq.raq_desc)).append("</desc>")
               .append("<objective id=\"").append(raq.raq_obj_id)
               .append("\" title=\"").append( cwUtils.esc4XML(dbObjective.getDesc(con, raq.raq_obj_id)) )
               .append("\" />").append(cwUtils.NEWL);
            /*
            if( raq.raq_criteria != null ) {
            	xml.append(raq.raq_criteria);
            }
            */
            xml.append("</header>").append(cwUtils.NEWL);
            
            if (raq.raq_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_FILLBLANK)) {
                String inter_start_tag = "<interaction";
                String inter_end_tag = "</interaction>";
                String order_attri = "order=\"";                
                while (raq.raq_que_xml.indexOf(inter_start_tag) != -1) {
                    int inter_start_index = raq.raq_que_xml.indexOf(inter_start_tag);
                    String temp_raq_que_xml = raq.raq_que_xml.substring(inter_start_index);
                    int inter_end_index = inter_start_index + temp_raq_que_xml.indexOf(inter_end_tag) + inter_end_tag.length();
                    temp_raq_que_xml = temp_raq_que_xml.substring(temp_raq_que_xml.indexOf(order_attri) + order_attri.length());
                    
                    int order = 0;
                    for (int j=0; j<temp_raq_que_xml.length(); j++) {
                        if (temp_raq_que_xml.charAt(j) == '"') {
                            order = Integer.parseInt(temp_raq_que_xml.substring(0, j));
                            break;
                        }
                    }
                    
                    raq.raq_que_xml = raq.raq_que_xml.substring(0, inter_start_index) + "[Blank" + order + "]" + raq.raq_que_xml.substring(inter_end_index);

                }
                xml.append(raq.raq_que_xml).append(cwUtils.NEWL);
            }
            else {
                xml.append(raq.raq_que_xml).append(cwUtils.NEWL);
            }                           
            
            xml.append(raq.raq_outcome_xml_1).append(cwUtils.NEWL);
            xml.append(raq.raq_explain_xml_1).append(cwUtils.NEWL);

            xml.append("</question>").append(cwUtils.NEWL);
        }
        xml.append("</question_list>").append(cwUtils.NEWL);
        return xml.toString();
        
    }

    public static void cancelUpload(Connection con, long ulg_id) throws SQLException
    {
        DbUploadLog ulg = new DbUploadLog();
        ulg.ulg_id = ulg_id;
        ulg.ulg_status = DbUploadLog.STATUS_CANCELLED;
        ulg.ulg_upd_datetime = cwSQL.getTime(con);
        ulg.updStatus(con);
        DbRawQuestion.del(con, ulg.ulg_id);
    }

    public static long uploadQue(Connection con, extendQue inQue, long ulg_id, int linenum, int int_count)
        throws SQLException
    {
        DbRawQuestion raq = new DbRawQuestion();
        raq.raq_ulg_id = ulg_id;
        raq.raq_line_num = linenum;
        raq.resource_id = inQue.resource_id;
        raq.raq_title = inQue.title;
        raq.raq_desc = inQue.desc; 
        raq.raq_lan = inQue.lan;
        raq.raq_duration = inQue.dur;
        raq.raq_difficulty = inQue.diff;
        raq.raq_privilege = inQue.folder; 
        raq.raq_status = inQue.onoff;
        raq.raq_type = inQue.queType;
        raq.raq_score = inQue.getScore();
        raq.raq_usr_id_owner = inQue.owner_id;
        raq.raq_int_cnt = int_count;
        raq.raq_que_xml =  inQue.getBody(); 
        raq.raq_criteria = inQue.criteria;
        raq.raq_shuffle = inQue.shuffle;
        raq.raq_sc_sub_shuffle = inQue.sc_sub_shuffle;
        raq.raq_parent_id = inQue.parent_id;
		raq.raq_submit_file_ind = inQue.submit_file_ind;
        raq.raq_criteria = inQue.sc_criteria;
		
        raq.raq_outcome_xml_1 = "";
        raq.raq_explain_xml_1 = "";
        if (raq.raq_int_cnt >=1 )  {
            for (int i=0; i<raq.raq_int_cnt; i++) {
                raq.raq_outcome_xml_1 += inQue.inter[i].getOutcome();
                raq.raq_explain_xml_1 += inQue.inter[i].getExplanation();
            }
        }
        
        raq.raq_obj_id = inQue.obj_id[0];
        
        raq.ins(con);
        return raq.raq_id;
        
    }

    public Hashtable save2DB(Connection con, qdbEnv static_env, long site_id, long ulg_id, loginProfile prof,boolean tc_enabled)
        throws SQLException, IOException, cwException
    {
        // Default resourcepermission
        boolean read = false;
        boolean write = false;
        boolean execute = false;
        
        Hashtable h_cnt = new Hashtable();
        int success_cnt = 0;
        int error_cnt = 0;
        int failure_cnt = 0;
        
        UploadUtils util = new UploadUtils();
        if (prof.current_role !=null && prof.current_role.length() > 0) {
            AcResources acres = new AcResources(con);
            read = acres.hasResPermissionRead(prof.current_role);
            write = acres.hasResPermissionWrite(prof.current_role);
            execute = acres.hasResPermissionExec(prof.current_role);
        }

        Vector raqVec = null;
        try {
            raqVec = DbRawQuestion.getQuetionSet(con, ulg_id);
        }catch (Exception e) {
        	CommonLog.info(e.getMessage(),e);
            throw new SQLException(e.getMessage());
        }            
        DbRawQuestion raq = null;
        dbQuestion dbque = null;
		
		Hashtable h_parent_q_id = new Hashtable();
		Hashtable h_parent_q_title = new Hashtable();
        
        Vector sc_que_id = new Vector();
        Timestamp updStartTime;
        try {
            updStartTime = dbUtils.getTime(con);
        }
        catch (qdbException e) {
            CommonLog.error(e.getMessage(),e);
            throw new SQLException();
        }
        boolean pass_que_fdsc = true;
        for (int i=0;i<raqVec.size();i++) {
            dbque = new dbQuestion();
            raq = (DbRawQuestion) raqVec.elementAt(i);
            
            //for update the question
            dbque.que_res_id = raq.resource_id;
            dbque.res_lan = raq.raq_lan;
            dbque.res_upd_user = raq.raq_usr_id_owner;
            dbque.res_usr_id_owner = dbque.res_upd_user;
            dbque.res_title = raq.raq_title;
            dbque.res_desc = raq.raq_desc;
            dbque.res_type = dbResource.RES_TYPE_QUE;
            dbque.res_subtype = raq.raq_type;
            dbque.res_duration = raq.raq_duration;
            dbque.res_difficulty = raq.raq_difficulty;
            dbque.res_privilege = raq.raq_privilege;
            dbque.res_status = raq.raq_status;
            dbque.que_type = dbque.res_subtype;
            dbque.que_int_count = raq.raq_int_cnt;
            dbque.que_score = raq.raq_score;
            dbque.que_xml = raq.raq_que_xml;
            dbque.que_submit_file_ind = raq.raq_submit_file_ind;

            boolean passed = true;
     		if( raq.raq_parent_id > 0 ) {
     			dbque.res_mod_res_id_test = ((Long) h_parent_q_id.get(new Long(raq.raq_parent_id))).longValue();
     		}
//         	if(dbque.que_res_id > 0) {
//         		long temp_res_id = dbque.que_res_id;
//         		if(raq.raq_parent_id > 0) {
//         			temp_res_id = dbque.res_mod_res_id_test;
//         		}
//         		if(tc_enabled) {
//     	        	AcTrainingCenter acTc = new AcTrainingCenter(con);
//     	        	if(!acTc.hasResInMgtTc(prof.usr_ent_id, temp_res_id, prof.current_role).equals(dbResource.CAN_MGT_RES)) {
//     	        		 passed = false;
//     	        	}         			
//         		}
//                AcResources acRes = new AcResources(con);
//                if (!acRes.hasResPrivilege(prof.usr_ent_id, temp_res_id, prof.current_role)) {
//                    passed  = false;
//                }
//          	}
//         	if(raq.raq_obj_id > 0){
//         		if(tc_enabled) {
//         			AcTrainingCenter acTc = new AcTrainingCenter(con);
//         			if(!acTc.hasObjInMgtTc(prof.usr_ent_id, raq.raq_obj_id, prof.current_role).equals(dbObjective.CAN_MGT_OBJ)) {
//         				passed = false;
//         			}
//         		}
//         		AcResources acRes = new AcResources(con);
//         		if (!acRes.hasManagePrivilege(prof.usr_ent_id, raq.raq_obj_id, prof.current_role)) {
//         			passed = false;
//         		}
//         	}
         	// when res_type is not fsc or dsc, this pass_que_fdsc will default true
         	if(pass_que_fdsc ||  dbque.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_DSC) || 
     				dbque.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_FSC) ) {
         		Vector dsc_criteria = new Vector();
         		if (raq.raq_criteria != null && raq.raq_criteria.length() > 0) {
         			StringTokenizer st = new StringTokenizer(raq.raq_criteria, ",");
         			while (st.hasMoreTokens()) {
         				String tempSpec = st.nextToken();
         				long[] criteria = new long[2];
         				criteria[1] = Long.parseLong(tempSpec.substring(0, tempSpec.indexOf("(")));
         				criteria[0] = Long.parseLong(tempSpec.substring(tempSpec.indexOf("(") + 1, tempSpec.indexOf(")")));
         				dsc_criteria.addElement(criteria);
         			}
         		}
         		
         		if (dbque.que_int_count >=1) {
         			
         			String outcome_start_tag = "<outcome";
         			String outcome_end_tag = "</outcome>";
         			String exp_start_tag = "<explanation";
         			String exp_end_tag = "</explanation>";
         			int curr_int_order = 0;
         			while (raq.raq_outcome_xml_1 != null && raq.raq_outcome_xml_1.trim().length() > 0 && raq.raq_outcome_xml_1.indexOf(outcome_start_tag) != -1) {
         				curr_int_order++;
         				
         				dbInteraction intObj = new dbInteraction();
         				intObj.int_order = curr_int_order;
         				
         				int outcome_start_index = raq.raq_outcome_xml_1.indexOf(outcome_start_tag);
         				int outcome_end_index = raq.raq_outcome_xml_1.indexOf(outcome_end_tag) + outcome_end_tag.length();
         				
         				int exp_start_index = raq.raq_explain_xml_1.indexOf(exp_start_tag);
         				int exp_end_index = raq.raq_explain_xml_1.indexOf(exp_end_tag) + exp_end_tag.length();
         				
         				intObj.int_xml_outcome = raq.raq_outcome_xml_1.substring(outcome_start_index,outcome_end_index);
         				intObj.int_xml_explain = raq.raq_explain_xml_1.substring(exp_start_index,exp_end_index);
         				
         				raq.raq_outcome_xml_1 = raq.raq_outcome_xml_1.substring(outcome_end_index);
         				raq.raq_explain_xml_1 = raq.raq_explain_xml_1.substring(exp_end_index);
         				dbque.ints.addElement(intObj);
         			}
         			/*
         			 intObj.int_order = 1;
         			 intObj.int_xml_outcome = raq.raq_outcome_xml_1;
         			 intObj.int_xml_explain = raq.raq_explain_xml_1;
         			 dbque.ints.addElement(intObj);
         			 */
         			
         		}
         		
         		String[] robs = new String[1];
         		if( raq.raq_obj_id > 0 ) {
         			robs[0] = Long.toString(raq.raq_obj_id);
         		}else{
         			robs[0] = null;
         		}
         		
         		
         		boolean update = false;
         		if (dbque.que_res_id > 0) {
         			update = true;
         		}
         		
         		if( raq.raq_parent_id > 0 ) {
         			try {
         				long new_parent_id = ((Long) h_parent_q_id.get(new Long(raq.raq_parent_id))).longValue();
         				if (dbque.que_res_id > 0 && !dbque.isSameParent(con, new_parent_id)) {
         					throw new qdbErrMessage();
         				}
         			} catch (qdbErrMessage e) {
         				//System.out.println(e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         			}
         		}
         		
         		if (sc_que_id.size() > 0 && dbque.res_mod_res_id_test != ((Long)sc_que_id.lastElement()).longValue()) {
         			try {
         				scTypeSubQueSync(con, ((Long)sc_que_id.lastElement()).longValue(), updStartTime, prof.usr_id);
         			}
         			catch (qdbException e) {
         				throw new SQLException();
         			}
         			catch (qdbErrMessage e) {
         				//System.out.println(e);
         				CommonLog.error(e.getMessage(),e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         			}
         			catch (cwSysMessage e) {
         				//System.out.println(e);
         				CommonLog.error(e.getMessage(),e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         			}
         		}
         		
         		if( dbque.res_mod_res_id_test > 0 ) {
         			if (!sc_que_id.contains(new Long(dbque.res_mod_res_id_test))) {
         				sc_que_id.addElement(new Long(dbque.res_mod_res_id_test));
         			}
         		}
         		
         		try {
         			saveQue(con, dbque, robs, site_id, prof.current_role, read, write, execute);
         		}
         		catch (qdbException e) {
         			//System.out.println(e);
         			CommonLog.error(e.getMessage(),e);
         			con.rollback();
         			passed = false;
         			//write to error log
         			error_cnt++;
         			//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         		}
         		catch (cwSysMessage e) {
         			//System.out.println(e);
         			CommonLog.error(e.getMessage(),e);
         			con.rollback();
         			passed = false;
         			//write to error log
         			error_cnt++;
         			//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         		}
         		
         		if( dbque.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_DSC) || 
         				dbque.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_FSC) ) {
         			try {
         				saveQueContainer(con, dbque.que_res_id, raq.raq_shuffle, raq.raq_criteria, raq.raq_type, update);
         			}
         			catch (qdbException e) {
         				//System.out.println(e);
         				CommonLog.error(e.getMessage(),e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         			}
         			h_parent_q_id.put(new Long(raq.raq_line_num), new Long(dbque.que_res_id));
         			h_parent_q_title.put(new Long(dbque.que_res_id), dbque.res_title);
         		}
         		// Commit for each question
         		
         		if (!dsc_criteria.isEmpty()) {
         			long[] tempSpec = null;
         			Iterator iter = dsc_criteria.iterator();
         			DynamicScenarioQue dscQue = new DynamicScenarioQue();
         			DbQueContainerSpec qcs = new DbQueContainerSpec();
         			dscQue.res_id = dbque.que_res_id;
         			dscQue.res_upd_user = dbque.res_upd_user;
         			try {
         				dscQue.removeAllSpec(con);
         				while (iter.hasNext()) {
         					tempSpec = (long[])iter.next();
         					qcs.qcs_res_id = dscQue.res_id;
         					qcs.qcs_score = tempSpec[1];
         					dscQue.addSpec(con, dbQuestion.RES_SUBTYPE_DSC, tempSpec[1], 0, "", 0, tempSpec[0], 0, dbque.res_usr_id_owner);
         				}
         			}
         			catch (cwSysMessage e) {
         				//System.out.println(e);
         				CommonLog.error(e.getMessage(),e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         			}
         			catch (qdbException e) {
         				//System.out.println(e);
         				CommonLog.error(e.getMessage(),e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         			}
         			
         		}
         		
         		if ((i == raqVec.size() - 1) && sc_que_id.size() > 0) {
         			try {
         				scTypeSubQueSync(con, ((Long)sc_que_id.lastElement()).longValue(), updStartTime, prof.usr_id);
         			}
         			catch (qdbException e) {
         				throw new SQLException();
         			}
         			catch (qdbErrMessage e) {
         				//System.out.println(e);
         				CommonLog.error(e.getMessage(),e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         			}
         			catch (cwSysMessage e) {
         				//System.out.println(e);
         				CommonLog.error(e.getMessage(),e);
         				con.rollback();
         				passed = false;
         				//write to error log
         				error_cnt++;
         				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
         			}
         		}
         		if(passed) {
         			con.commit();	
         		} else {
         			con.rollback();
         		}
         	}
            
            if( dbque.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_DSC) || 
                	dbque.que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_FSC) ) {
            	if(passed) {
            		pass_que_fdsc = true;
            	} else {
            		pass_que_fdsc = false;
            	}
            }
            if(!pass_que_fdsc){
            	passed = false;
            }
     		if( passed ) {
     			success_cnt ++;
     			//write to success log
     			if( dbque.res_mod_res_id_test == 0 ) {
     				util.writeSuccessMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)));
     			} else {
     				util.writeSuccessMCLog(static_env, ulg_id, raq.raq_title, (String) h_parent_q_title.get(new Long(dbque.res_mod_res_id_test)));
     			}
     		} else {
     			//write to failure log
     			failure_cnt++;
     			if( dbque.res_mod_res_id_test == 0 ) {
     				util.writeFailureMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)));
     			} else {
     				util.writeFailureMCLog(static_env, ulg_id, raq.raq_title, (String) h_parent_q_title.get(new Long(dbque.res_mod_res_id_test)));
     			}
     		}
            
        }

        
        h_cnt.put(SUCCESS_CNT, new Integer(success_cnt));
        h_cnt.put(FAILURE_CNT, new Integer(failure_cnt));
        h_cnt.put(ERROR_CNT, new Integer(error_cnt));
        
        return h_cnt;
    }

    private void scTypeSubQueSync(Connection con, long sc_que_id, Timestamp updStartTime, String usr_id) 
        throws SQLException, qdbException, qdbErrMessage, cwSysMessage {
            
        StringBuffer sc_sub_que_sync = new StringBuffer();
        sc_sub_que_sync.append("select rcn_res_id_content from Resources, ResourceContent")
                       .append(" where res_id = rcn_res_id_content ")
                       .append(" and res_upd_date < ? ")
                       .append(" and rcn_res_id = ? ");
        
        PreparedStatement stmt = con.prepareStatement(sc_sub_que_sync.toString());
        stmt.setTimestamp(1, updStartTime);
        stmt.setLong(2, sc_que_id);
        ResultSet rs;
        Vector sub_que_ids = new Vector();
        try {
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                sub_que_ids.addElement(new String(rs.getString("rcn_res_id_content")));
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        String[] ids_str = null;
        if (sub_que_ids.size() > 0) {
            ids_str = cwUtils.vec2strArray(sub_que_ids);
        }
        
        if (ids_str != null && ids_str.length > 0) {
            dbResourceContent resObj = new dbResourceContent();
            resObj.del_res_by_content_id(con, ids_str);
            for (int i = 0;i < ids_str.length;i++) {
                dbQuestion dbque = new dbQuestion();
                dbque.res_id = Long.parseLong(ids_str[i]);
                dbque.que_res_id = Long.parseLong(ids_str[i]);
                dbque.res_upd_user = usr_id;
                dbque.del(con);
            }
        }
    }

    private void saveQueContainer(Connection con, long que_res_id, boolean shuffle, String criteria, String raq_type, boolean update) throws qdbException {
        //insert into QueContainer
        String qct_select_logic = null;
        if (raq_type.equals(dbQuestion.RES_SUBTYPE_DSC)) {
            qct_select_logic = "RND";
        }
        ViewQueContainer queContainer = new ViewQueContainer();
        queContainer.res_id = que_res_id;
        queContainer.qct_select_logic = qct_select_logic;
        if (shuffle) {
            queContainer.qct_allow_shuffle_ind = 1;
        }
        
        if (update) {
            queContainer.upd(con);
        } else {
            queContainer.ins(con);
        }
        return;

    }

    public String getObjDec(Connection con, Long obj_id)
        throws SQLException {
            String desc = null;
            if( this.objTitleHash == null )
                this.objTitleHash = new Hashtable();
            
            if( this.objTitleHash.contains(obj_id) )
                return (String)this.objTitleHash.get(obj_id);
            else {
                desc = dbObjective.getDesc(con, obj_id.longValue());
                this.objTitleHash.put(obj_id, desc);
            }
            return desc;
        }

    public static void saveQue(Connection con, dbQuestion dbque, String[] robs, long site_id, String role, boolean read, boolean write, boolean execute)
        throws SQLException, qdbException, cwSysMessage
    {
            if (dbque.que_res_id > 0) {
                if (dbque.que_type.equals(dbQuestion.RES_SUBTYPE_FSC)) {
                    dbque.que_score = dbQuestion.getScore(con, dbque.que_res_id);
                }
                    
                dbque.upd(con, robs);
            } else {
                dbque.ins(con, robs, dbResource.RES_TYPE_QUE);
            }
            
            dbResourcePermission.save(con,dbque.que_res_id,dbque.res_usr_id_owner,role,read,write,execute);
            // Public Folder
            if (dbque.res_privilege.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
                // CW Admin Group
                // open resource for all 
                //if(prof.isPublic)
                if (robs[0] != null && dbObjective.isPublicObjective(con, Long.parseLong(robs[0]))) 
                    dbResourcePermission.save(con,dbque.que_res_id,0,null,true,false,false);
                // open resource for those within the organization
                else
                    dbResourcePermission.save(con,dbque.que_res_id,site_id,null,true,false,false);
            }
    }

/*
	private void saveQueContainer(Connection con, long que_res_id, boolean shuffle, String criteria_xml, String que_type)
		throws SQLException, cwException{

			ViewQueContainer myViewQueContainer = new ViewQueContainer();
			myViewQueContainer.res_id = que_res_id;
			dbQuestion dbQue = new dbQuestion();
			dbQue.que_res_id = que_res_id;
			if( shuffle ) {
				myViewQueContainer.qct_allow_shuffle_ind = 1;
			}else{
				myViewQueContainer.qct_allow_shuffle_ind = 0;
			}
			try{
				myViewQueContainer.ins(con);
			}catch(qdbException e){
				throw new cwException(e.getMessage());
			}
			if( que_type.equalsIgnoreCase(dbQuestion.RES_SUBTYPE_DSC) && criteria_xml != null ) {
				Node xml = null;
				Document doc = null;
				DbQueContainerSpec spec = new DbQueContainerSpec();
				spec.qcs_res_id = que_res_id;
				try{
					DOMParser xmlParser = new DOMParser();
					xmlParser.parse(new InputSource(new StringReader(criteria_xml)));
					doc = xmlParser.getDocument();
					xml = doc.getFirstChild();
				}catch(IOException e){
					throw new cwException(e.getMessage());
				}catch(SAXException e){
					throw new cwException(e.getMessage());
				}
				NodeList criteriaNodeList = xml.getChildNodes();
				for(int i=0; i<criteriaNodeList.getLength(); i++) {
					Node criteriaNode = criteriaNodeList.item(i);
				   if( criteriaNode.getNodeType() == Node.ELEMENT_NODE && 
					   (criteriaNode.getNodeName()).equalsIgnoreCase("criteria") ) {
					   		
					   		NodeList nodelist = criteriaNode.getChildNodes();
					   		for(int j=0; j<nodelist.getLength(); j++){
					   			Node node = nodelist.item(j);
					   			if( node.getNodeType() == Node.ELEMENT_NODE &&
					   				node.getNodeName().equalsIgnoreCase("score")) {
									spec.qcs_score = Integer.parseInt(node.getFirstChild().getNodeValue()); 
					   			}else if( node.getNodeType() == Node.ELEMENT_NODE && 
					   				node.getNodeName().equalsIgnoreCase("que_count")) {
					   				spec.qcs_qcount = Integer.parseInt(node.getFirstChild().getNodeValue());
					   			}
					   		}
					   }
					dbQue.que_score += spec.qcs_score * spec.qcs_qcount; 
					try{
						spec.ins(con, que_type);
						dbQue.updScore(con);
					}catch(cwSysMessage e) {
						throw new cwException(e.getMessage());
					}catch(qdbException e){
						throw new cwException(e.getMessage());
					}
				}
			}
		}
*/
    /**
     * 导入公共调查问卷
     */
    public Hashtable save2DB(Connection con, qdbEnv static_env, long site_id, long ulg_id, loginProfile prof,boolean tc_enabled,boolean is_suq)
            throws SQLException, IOException, cwException
        {
            // Default resourcepermission
            boolean read = false;
            boolean write = false;
            boolean execute = false;
            
            Hashtable h_cnt = new Hashtable();
            int success_cnt = 0;
            int error_cnt = 0;
            int failure_cnt = 0;
            
            UploadUtils util = new UploadUtils();
            if (prof.current_role !=null && prof.current_role.length() > 0) {
                AcResources acres = new AcResources(con);
                read = acres.hasResPermissionRead(prof.current_role);
                write = acres.hasResPermissionWrite(prof.current_role);
                execute = acres.hasResPermissionExec(prof.current_role);
            }

            Vector raqVec = null;
            try {
                raqVec = DbRawQuestion.getQuetionSet(con, ulg_id);
            }catch (Exception e) {
            	CommonLog.error(e.getMessage(),e);
                throw new SQLException(e.getMessage());
            }            
            DbRawQuestion raq = null;
            dbQuestion dbque = null;
    		
    		Hashtable h_parent_q_id = new Hashtable();
    		Hashtable h_parent_q_title = new Hashtable();
            
            Vector sc_que_id = new Vector();
            Timestamp updStartTime;
            try {
                updStartTime = dbUtils.getTime(con);
            }
            catch (qdbException e) {
                CommonLog.error(e.getMessage(),e);
                throw new SQLException();
            }
            boolean pass_que_fdsc = true;
            for (int i=0;i<raqVec.size();i++) {
                dbque = new dbQuestion();
                raq = (DbRawQuestion) raqVec.elementAt(i);
                
                //for update the question
                dbque.que_res_id = raq.resource_id;
                dbque.res_lan = raq.raq_lan;
                dbque.res_upd_user = raq.raq_usr_id_owner;
                dbque.res_usr_id_owner = dbque.res_upd_user;
                dbque.res_title = raq.raq_title;
                dbque.res_desc = raq.raq_desc;
              
               	dbque.res_type = dbResource.RES_TYPE_QUE;
                dbque.res_subtype = raq.raq_type;
                dbque.res_duration = raq.raq_duration;
                dbque.res_difficulty = raq.raq_difficulty;
                dbque.res_privilege = raq.raq_privilege;
                dbque.res_status = raq.raq_status;
                dbque.que_type = dbque.res_subtype;
                dbque.que_int_count = raq.raq_int_cnt;
                dbque.que_score = raq.raq_score;
                dbque.que_xml = raq.raq_que_xml;
                dbque.que_submit_file_ind = raq.raq_submit_file_ind;

                boolean passed = true;
         		if( raq.raq_parent_id > 0 ) {
         			dbque.res_mod_res_id_test = ((Long) h_parent_q_id.get(new Long(raq.raq_parent_id))).longValue();
         		}else{
         			dbque.res_mod_res_id_test =raq.raq_obj_id;
         		}
             	
             	
             	// when res_type is not fsc or dsc, this pass_que_fdsc will default true
             
             		
             		
             		if (dbque.que_int_count >=1) {
             			
             			String outcome_start_tag = "<outcome";
             			String outcome_end_tag = "</outcome>";
             			String exp_start_tag = "<explanation";
             			String exp_end_tag = "</explanation>";
             			int curr_int_order = 0;
             			while (raq.raq_outcome_xml_1 != null && raq.raq_outcome_xml_1.trim().length() > 0 && raq.raq_outcome_xml_1.indexOf(outcome_start_tag) != -1) {
             				curr_int_order++;
             				
             				dbInteraction intObj = new dbInteraction();
             				intObj.int_order = curr_int_order;
             				
             				int outcome_start_index = raq.raq_outcome_xml_1.indexOf(outcome_start_tag);
             				int outcome_end_index = raq.raq_outcome_xml_1.indexOf(outcome_end_tag) + outcome_end_tag.length();
             				
             				int exp_start_index = raq.raq_explain_xml_1.indexOf(exp_start_tag);
             				int exp_end_index = raq.raq_explain_xml_1.indexOf(exp_end_tag) + exp_end_tag.length();
             				
             				intObj.int_xml_outcome = raq.raq_outcome_xml_1.substring(outcome_start_index,outcome_end_index);
             				intObj.int_xml_explain = raq.raq_explain_xml_1.substring(exp_start_index,exp_end_index);
             				
             				raq.raq_outcome_xml_1 = raq.raq_outcome_xml_1.substring(outcome_end_index);
             				raq.raq_explain_xml_1 = raq.raq_explain_xml_1.substring(exp_end_index);
             				dbque.ints.addElement(intObj);
             			}
             			/*
             			 intObj.int_order = 1;
             			 intObj.int_xml_outcome = raq.raq_outcome_xml_1;
             			 intObj.int_xml_explain = raq.raq_explain_xml_1;
             			 dbque.ints.addElement(intObj);
             			 */
             			
             		}
             		
             		String[] robs = new String[1];
             		if( raq.raq_obj_id > 0 ) {
             			robs[0] = Long.toString(raq.raq_obj_id);
             		}else{
             			robs[0] = null;
             		}
             		
             		
             		boolean update = false;
             		if (dbque.que_res_id > 0) {
             			update = true;
             		}
             		
             		if( raq.raq_parent_id > 0 ) {
             			try {
             				long new_parent_id = ((Long) h_parent_q_id.get(new Long(raq.raq_parent_id))).longValue();
             				if (dbque.que_res_id > 0 && !dbque.isSameParent(con, new_parent_id)) {
             					throw new qdbErrMessage();
             				}
             			} catch (qdbErrMessage e) {
             				//System.out.println(e);
             				con.rollback();
             				passed = false;
             				//write to error log
             				error_cnt++;
             				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
             			}
             		}
             		
             		if (sc_que_id.size() > 0 && dbque.res_mod_res_id_test != ((Long)sc_que_id.lastElement()).longValue()) {
             			try {
             				scTypeSubQueSync(con, ((Long)sc_que_id.lastElement()).longValue(), updStartTime, prof.usr_id);
             			}
             			catch (qdbException e) {
             				throw new SQLException();
             			}
             			catch (qdbErrMessage e) {
             				//System.out.println(e);
             				CommonLog.error(e.getMessage(),e);
             				con.rollback();
             				passed = false;
             				//write to error log
             				error_cnt++;
             				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
             			}
             			catch (cwSysMessage e) {
             				//System.out.println(e);
             				CommonLog.error(e.getMessage(),e);
             				con.rollback();
             				passed = false;
             				//write to error log
             				error_cnt++;
             				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
             			}
             		}
             		
             		if( dbque.res_mod_res_id_test > 0 ) {
             			if (!sc_que_id.contains(new Long(dbque.res_mod_res_id_test))) {
             				sc_que_id.addElement(new Long(dbque.res_mod_res_id_test));
             			}
             		}
             		
             		try {
             			dbque.ins(con, robs, dbResource.RES_TYPE_SUQ);
             		}
             		catch (qdbException e) {
             			//System.out.println(e);
             			CommonLog.error(e.getMessage(),e);
             			con.rollback();
             			passed = false;
             			//write to error log
             			error_cnt++;
             			//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
             		}
             		
             		
             		// Commit for each question
             		
             		
             		//删除原来问卷
//             		if ((i == raqVec.size() - 1) && sc_que_id.size() > 0) {
//             			try {
//             				scTypeSubQueSync(con, ((Long)sc_que_id.lastElement()).longValue(), updStartTime, prof.usr_id);
//             			}
//             			catch (qdbException e) {
//             				throw new SQLException();
//             			}
//             			catch (qdbErrMessage e) {
//             				//System.out.println(e);
//             				e.printStackTrace(System.out);
//             				con.rollback();
//             				passed = false;
//             				//write to error log
//             				error_cnt++;
//             				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
//             			}
//             			catch (cwSysMessage e) {
//             				//System.out.println(e);
//             				e.printStackTrace(System.out);
//             				con.rollback();
//             				passed = false;
//             				//write to error log
//             				error_cnt++;
//             				//util.writeErrorMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)), e.getMessage());
//             			}
//             		}
             		if(passed) {
             			con.commit();	
             		} else {
             			con.rollback();
             		}
             
                
               
               
         		if( passed ) {
         			success_cnt ++;
         			//write to success log
         			if( dbque.res_mod_res_id_test == 0 ) {
         				util.writeSuccessMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)));
         			} else {
         				util.writeSuccessMCLog(static_env, ulg_id, raq.raq_title, dbResource.getResTitle(con,new Long(dbque.res_mod_res_id_test)));
         			}
         		} else {
         			//write to failure log
         			failure_cnt++;
         			if( dbque.res_mod_res_id_test == 0 ) {
         				util.writeFailureMCLog(static_env, ulg_id, raq.raq_title, getObjDec(con, new Long(raq.raq_obj_id)));
         			} else {
         				util.writeFailureMCLog(static_env, ulg_id, raq.raq_title, dbResource.getResTitle(con,new Long(dbque.res_mod_res_id_test)));
         			}
         		}
                
        	}

            
            h_cnt.put(SUCCESS_CNT, new Integer(success_cnt));
            h_cnt.put(FAILURE_CNT, new Integer(failure_cnt));
            h_cnt.put(ERROR_CNT, new Integer(error_cnt));
            
            return h_cnt;
        }

}
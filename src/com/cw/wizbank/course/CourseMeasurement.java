/*
 * Created on 2004-10-20
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.course;

/**
 * @author vincent
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.math.BigDecimal;
import java.sql.*;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbCourseMeasurement;
import com.cw.wizbank.db.DbMeasurementEvaluation;
import com.cw.wizbank.db.DbCourseModuleCriteria;
import com.cw.wizbank.db.view.ViewCourseModuleCriteria;
import com.cw.wizbank.ae.aeItem;
public class CourseMeasurement {
             
	static final String TITLE_EXIST = "AEIT15";
	static final String REC_UPDATED = "AEQM09";
	static final String EVAL_EXIST = "AEQM05";
	
	public String getCmtLstAsXML(Connection con, long ccr_id, long cmt_id_del, HttpSession sess, aeItem itm )
		throws SQLException, qdbException, cwSysMessage {
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		DbCourseModuleCriteria dbcmr = new DbCourseModuleCriteria();
		dbModule dbm = new dbModule();
		StringBuffer result = new StringBuffer(2000);
		result.append("<marking_scheme_list ccr_id=\"").append(ccr_id).append("\">").append(cwUtils.NEWL);
		//set  editable
	    if((itm.itm_create_run_ind&&itm.itm_content_def!=null&&itm.itm_content_def.equalsIgnoreCase(aeItem.PARENT))||//if class room course and prarent module
			(!itm.itm_create_run_ind&&!itm.itm_run_ind&&itm.itm_apply_ind)||  //if on line course
			(!itm.itm_create_run_ind&&itm.itm_run_ind&&itm.itm_apply_ind&&itm.itm_content_def!=null&&itm.itm_content_def.equalsIgnoreCase(aeItem.CHILD))//if class and chrild module
			){
	    	result.append("<editable>true</editable>");
		 }else{
			 result.append("<editable>false</editable>");
		 }
		long[] cmtIds = dbcmt.getCmtLstByItmId(con, ccr_id);
		if (cmtIds == null) {
			if (sess.getAttribute("cmt_title") != null) {
				result.append("<item cmt_id=\"0\">").append(cwUtils.NEWL);
				result.append("<cmt_ccr_id>");
				result.append(ccr_id);
				result.append("</cmt_ccr_id>").append(cwUtils.NEWL);
				result.append("<cmt_title>");
				result.append(cwUtils.esc4XML(sess.getAttribute("cmt_title").toString()));
				result.append("</cmt_title>").append(cwUtils.NEWL);
				result.append("<cmt_max_score>");
				result.append(sess.getAttribute("cmt_max_score"));
				result.append("</cmt_max_score>").append(cwUtils.NEWL);
				result.append("<cmt_pass_score>");
				result.append(sess.getAttribute("cmt_pass_score"));
				result.append("</cmt_pass_score>").append(cwUtils.NEWL);
				result.append("<cmt_contri_rate>");
				result.append("0.0");
				result.append("</cmt_contri_rate>").append(cwUtils.NEWL);
				result.append("</item>").append(cwUtils.NEWL);
				sess.removeAttribute("cmt_title");
				sess.removeAttribute("cmt_max_score");
				sess.removeAttribute("cmt_pass_score");
			}	
			if (sess.getAttribute("mod_res_id") != null){
				result.append("<mod_res_id>");
				result.append(sess.getAttribute("mod_res_id"));
				result.append("</mod_res_id>").append(cwUtils.NEWL);
				sess.removeAttribute("mod_res_id");
			}								
			result.append("</marking_scheme_list>").append(cwUtils.NEWL);
			return result.toString();
		} else {
            BigDecimal b;
            float pass_score;
			for (int i = 0; i < cmtIds.length; i++) {
				dbcmt = dbcmt.getByCmtId(con,cmtIds[i]);
				if(dbcmt.getCmt_cmr_id() != 0){
					dbcmr.getByCmrId(con, dbcmt.getCmt_cmr_id());
					dbcmt.setCmt_contri_rate(dbcmr.cmr_contri_rate);
					dbm.mod_res_id = dbcmr.cmr_res_id;
                    //dbm.getScoreByModResId(con);
                    dbm.get(con);
                    
                    if("TST".equalsIgnoreCase(dbm.mod_type) || "DXT".equalsIgnoreCase(dbm.mod_type) || "ASS".equalsIgnoreCase(dbm.mod_type)){
                        dbm.mod_pass_score = dbm.mod_max_score * (dbm.mod_pass_score/100);
                      b = new BigDecimal(dbm.mod_pass_score);
                      dbm.mod_pass_score = b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
                    }
                    

					dbcmt.setCmt_max_score(dbm.mod_max_score);
					dbcmt.setCmt_pass_score(dbm.mod_pass_score);
				}
				if (cmtIds[i] != cmt_id_del) {
					result.append("<item cmt_id=\"");
				} else {
					result.append("<item_del cmt_id_del=\"");
				}
//                if(dbcmt.getCmt_cmr_id() != 0){
//                    b = new BigDecimal(dbcmt.getCmt_max_score()*dbcmt.getCmt_pass_score()/100);
//                    pass_score = b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
//                }else{
                    pass_score = dbcmt.getCmt_pass_score();
//                }
				result.append(dbcmt.getCmt_id());
				result.append("\">").append(cwUtils.NEWL);
				result.append("<cmt_ccr_id>");
				result.append(dbcmt.getCmt_ccr_id());
				result.append("</cmt_ccr_id>").append(cwUtils.NEWL);
				result.append("<cmt_title>");
				result.append(cwUtils.esc4XML(dbcmt.getCmt_title()));
				result.append("</cmt_title>").append(cwUtils.NEWL);
				result.append("<cmt_cmr_id>");
				result.append(dbcmt.getCmt_cmr_id());
				result.append("</cmt_cmr_id>").append(cwUtils.NEWL);
                if(dbcmt.getCmt_cmr_id() != 0){
                    result.append("<cmt_mod_title>");
                    result.append(cwUtils.esc4XML(dbm.res_title));
                    result.append("</cmt_mod_title>").append(cwUtils.NEWL);
                }
                result.append("<cmt_max_score>");
				result.append(dbcmt.getCmt_max_score());
				result.append("</cmt_max_score>").append(cwUtils.NEWL);
				result.append("<cmt_pass_score>");
				result.append(pass_score);
				result.append("</cmt_pass_score>").append(cwUtils.NEWL);
				result.append("<cmt_contri_rate>");
				result.append(dbcmt.getCmt_contri_rate());
				result.append("</cmt_contri_rate>").append(cwUtils.NEWL);
				result.append("<cmt_is_contri_by_score>");
				result.append(dbcmt.getCmt_is_contri_by_score());
				result.append("</cmt_is_contri_by_score>").append(cwUtils.NEWL);
				result.append("<cmt_update_timestamp>");
				result.append(dbcmt.getCmt_update_timestamp());
				result.append("</cmt_update_timestamp>").append(cwUtils.NEWL);								
				if(cmtIds[i] != cmt_id_del)
					result.append("</item>").append(cwUtils.NEWL);
				else 
					result.append("</item_del>");
			} 
		}
			if (sess.getAttribute("cmt_title") != null) {
				result.append("<item cmt_id=\"0\">").append(cwUtils.NEWL);
				result.append("<cmt_ccr_id>");
				result.append(ccr_id);
				result.append("</cmt_ccr_id>").append(cwUtils.NEWL);
				result.append("<cmt_title>");
				result.append(cwUtils.esc4XML(sess.getAttribute("cmt_title").toString()));
				result.append("</cmt_title>").append(cwUtils.NEWL);
				result.append("<cmt_max_score>");
				result.append(sess.getAttribute("cmt_max_score"));
				result.append("</cmt_max_score>").append(cwUtils.NEWL);
				result.append("<cmt_pass_score>");
				result.append(sess.getAttribute("cmt_pass_score"));
				result.append("</cmt_pass_score>").append(cwUtils.NEWL);
				result.append("<cmt_contri_rate>");
				result.append("0.0");
				result.append("</cmt_contri_rate>").append(cwUtils.NEWL);
				result.append("</item>").append(cwUtils.NEWL);
				sess.removeAttribute("cmt_title");
				sess.removeAttribute("cmt_max_score");
				sess.removeAttribute("cmt_pass_score");
			}	
			if (sess.getAttribute("mod_res_id") != null) {
				result.append("<mod_res_id>");
				result.append(sess.getAttribute("mod_res_id"));
				result.append("</mod_res_id>").append(cwUtils.NEWL);
				sess.removeAttribute("mod_res_id");
			}	
			result.append("</marking_scheme_list>").append(cwUtils.NEWL);
			return result.toString();
	}
 
	public Long insScoreCmt(Connection con, long mod_res_id, long ccr_id, String cmt_title, long itm_id, float cmt_contri_rate, float cmt_max_score, float cmt_pass_score, loginProfile prof)
		throws SQLException, cwSysMessage, qdbException {
			
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		boolean createRunInd = itm.getCreateRunInd(con);
		Timestamp curTime = cwSQL.getTime(con);
		long cmr_parent_id = 0;
		long cmt_parent_id = 0;
        String type = DbCourseCriteria.getCcrType( con, ccr_id);
		HashMap parent_info = insScoreCmtExe(con, mod_res_id, ccr_id, cmt_title, cmt_contri_rate, cmt_max_score, cmt_pass_score, prof.usr_id, curTime, cmr_parent_id, cmt_parent_id);
		if (createRunInd) {
			List ch_itm_id_list = itm.getChItemIDList( con, itm_id);
			dbModule dbm = new dbModule();
			float mod_max_score = cmt_max_score;
			float mod_pass_score = cmt_pass_score;
			if(mod_res_id != 0){
				dbm.mod_res_id = mod_res_id;
				dbm.get(con);
				mod_max_score = dbm.mod_max_score;
				mod_pass_score = dbm.mod_pass_score;
			}
			for (int i=0;i<ch_itm_id_list.size();i++) {
				if (ch_itm_id_list.get(i) != null){
					long child_mod_res_id = 0;
					cmr_parent_id = 0;
					cmt_parent_id = 0;
					long ch_itm_id = ((Long)ch_itm_id_list.get(i)).longValue();
					long ch_ccr_id=  DbCourseCriteria.getCcrIdByItmID( con,  ch_itm_id, type) ;
					if (parent_info.get("CMR_PARENT_ID") != null) {
						cmr_parent_id = ((Long)parent_info.get("CMR_PARENT_ID")).longValue();
					}
					if (parent_info.get("CMT_PARENT_ID") != null) {
						cmt_parent_id = ((Long)parent_info.get("CMT_PARENT_ID")).longValue();
					}
					if (mod_res_id != 0){
						child_mod_res_id = dbModule.getChidltmModID( con,  mod_res_id,  ch_itm_id);
					}
					insScoreCmtExe(con, child_mod_res_id, ch_ccr_id, cmt_title, cmt_contri_rate, cmt_max_score, cmt_pass_score, prof.usr_id, curTime, cmr_parent_id, cmt_parent_id);
					
					//新增计分规则的时候把他的班级的分数也要一起更新dbmodule
					if(mod_res_id != 0 && child_mod_res_id != 0){
						dbm.mod_res_id = child_mod_res_id;
						dbm.get(con);
						dbm.mod_max_score = mod_max_score;
						dbm.mod_pass_score = mod_pass_score;
						dbm.updMaxScoreAndPassScore(con);
					}
				}
			}	
		}
		if(null!= parent_info && null!=parent_info.get("CMT_PARENT_ID")){
			 cmt_parent_id = (Long)parent_info.get("CMT_PARENT_ID");
		}
		return cmt_parent_id;
	}
	
	private HashMap insScoreCmtExe	(Connection con, long mod_res_id, long ccr_id, String cmt_title,  float cmt_contri_rate, float cmt_max_score, float cmt_pass_score, String create_usr_id, Timestamp curTime, long cmr_parent_id, long cmt_parent_id)
	throws SQLException, cwSysMessage {
		
		HashMap insert_id_info = new HashMap();
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		DbCourseModuleCriteria dbcmr = new DbCourseModuleCriteria();
		long cmt_cmr_id = 0;
		
		if (mod_res_id != 0){
			if (dbcmr.getCmrId(con, mod_res_id, ccr_id) == 0){
				dbcmr.cmr_ccr_id = ccr_id;
				dbcmr.cmr_res_id = mod_res_id;
				dbcmr.cmr_contri_rate = cmt_contri_rate;
				dbcmr.cmr_create_timestamp = curTime;
				dbcmr.cmr_create_usr_id = create_usr_id;
				dbcmr.cmr_upd_timestamp = curTime;
				dbcmr.cmr_upd_usr_id = create_usr_id;
				dbcmr.cmr_is_contri_by_score = true;
				dbcmr.cmr_status = null;
				dbcmr.cmr_cmr_id_parent = cmr_parent_id;
				dbcmr.ins(con);	
				cmt_cmr_id = dbcmr.cmr_id;
				insert_id_info.put("CMR_PARENT_ID",new Long(dbcmr.cmr_id));
			} else throw new cwSysMessage("GEN000");
		}
		dbcmt.setCmt_ccr_id(ccr_id);
		dbcmt.setCmt_title(cmt_title);
		if (dbcmt.isTitleExist(con))
			throw new cwSysMessage(TITLE_EXIST);
		dbcmt.setCmt_cmr_id(cmt_cmr_id);
		if (cmt_cmr_id == 0)
			dbcmt.setCmt_max_score(cmt_max_score);
		if (cmt_cmr_id == 0)
			dbcmt.setCmt_pass_score(cmt_pass_score);
		if (cmt_cmr_id == 0)
			dbcmt.setCmt_contri_rate(cmt_contri_rate);
		dbcmt.setCmt_status(null);
		dbcmt.setCmt_is_contri_by_score(true);
		dbcmt.setCmt_create_timestamp(curTime);
		dbcmt.setCmt_update_timestamp(curTime);
		dbcmt.setCmt_create_usr_id(create_usr_id);
		dbcmt.setCmt_update_usr_id(create_usr_id);
		dbcmt.setCmt_delete_timestamp(null);
		dbcmt.setCmt_cmt_id_parent(cmt_parent_id);
		dbcmt.ins(con);
		insert_id_info.put("CMT_PARENT_ID",new Long(dbcmt.getCmt_id()));
		return insert_id_info;
	}
	public boolean isTitleExist(Connection con, long ccr_id, String cmt_title, HttpSession sess) throws SQLException, cwSysMessage {
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		dbcmt.setCmt_ccr_id(ccr_id);
		dbcmt.setCmt_title(cmt_title);
		if (dbcmt.isTitleExist(con)){
			if (sess.getAttribute("cmt_title") != null){
				sess.removeAttribute("cmt_title");
				sess.removeAttribute("cmt_max_score");
				sess.removeAttribute("cmt_pass_score");
			}
			if (sess.getAttribute("mod_res_id") != null)
				sess.removeAttribute("mod_res_id");
			throw new cwSysMessage(TITLE_EXIST);
		}
		else return false;
	}
	public boolean modCmtPercent(Connection con, long[] cmt_id_list, float[] cmt_id_percent_list, Timestamp[] upd_timestamp_list, long cmt_id_del, loginProfile prof, long itm_id) throws SQLException, cwSysMessage {
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		Timestamp curTime = cwSQL.getTime(con);
		aeItem itm=new aeItem();
		itm.itm_id=itm_id;
		boolean createRunInd=itm.getCreateRunInd(con);
		for (int i=0; i<cmt_id_list.length; i++){
			if (cmt_id_list[i] !=0 && cmt_id_list[i] != cmt_id_del) {
			modCmtPercentExe( con,cmt_id_list[i],  cmt_id_del, cmt_id_percent_list[i],  prof.usr_id, curTime, upd_timestamp_list[i]);
				if (createRunInd) {
					List ch_cmt_id_list = dbcmt.getChCmtIdList( con, cmt_id_list[i]);
					for(int j = 0; j<ch_cmt_id_list.size(); j++){
						if(ch_cmt_id_list.get(j)!=null){					
							modCmtPercentExe( con,((Long)ch_cmt_id_list.get(j)).longValue(),  cmt_id_del, cmt_id_percent_list[i],  prof.usr_id, curTime, upd_timestamp_list[i]);				
						}
					}	
				}
			}		
		}
		return true;
	}
	private void modCmtPercentExe(Connection con, long cmt_id, long cmt_id_del, float perent, String usr_id, Timestamp curTime, Timestamp updTime)throws SQLException, cwSysMessage {
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		DbCourseModuleCriteria dbcmr = new DbCourseModuleCriteria();
		if (cmt_id != 0 && cmt_id != cmt_id_del){
			dbcmt.getByCmtId(con, cmt_id);
			updTime = dbcmt.getCmt_update_timestamp();
			if (dbcmt.getCmt_cmr_id() != 0){
				dbcmr.getByCmrId(con, dbcmt.getCmt_cmr_id());
				dbcmr.cmr_contri_rate = perent;
				dbcmr.cmr_upd_timestamp = curTime;
				dbcmr.cmr_upd_usr_id = usr_id;
				dbcmr.cmr_is_contri_by_score = true;
				//dbcmr.cmr_status = null;	
				dbcmr.upd(con);	
				dbcmt.setCmt_update_timestamp(curTime);
				dbcmt.setCmt_update_usr_id(usr_id);
				if (dbcmt.upd(con, updTime) == 0){
					throw new cwSysMessage(REC_UPDATED);
				}
			} else{
				dbcmt.setCmt_update_timestamp(curTime);
				dbcmt.setCmt_update_usr_id(usr_id);
				dbcmt.setCmt_contri_rate(perent);				
				if (dbcmt.upd(con, updTime) == 0){
					throw new cwSysMessage(REC_UPDATED);
				}
			}
		}
		
	}
	public void delCmt(Connection con, long cmt_id, loginProfile prof) throws SQLException{
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		DbMeasurementEvaluation dbmv = new DbMeasurementEvaluation();
		dbcmt.getByCmtId(con,cmt_id);
		long cmr_id = dbcmt.getCmt_cmr_id();
		if (cmr_id != 0){
			DbCourseModuleCriteria.delByCmrId(con, cmr_id);
		}
		dbcmt.setCmt_id(cmt_id);
		dbcmt.setCmt_update_usr_id(prof.usr_id);
		dbcmt.delByCmtId(con);
		dbmv.delByCmtId(con,cmt_id);
		
		 //loop te delete che child Cmt record 
		List child_cmt = dbcmt.getChCmtIdList( con,  cmt_id);
		for (int i = 0;i < child_cmt.size();i++){
			if ( child_cmt.get(i) != null) {
				long ch_cmt_id = ((Long)child_cmt.get(i)).longValue();
				delCmt(con, ch_cmt_id, prof);
			}
		}
	}
	
	/**
	 * 
	 * @param con Connection Object
	 * @param itm_id itm_id of aeItem
	 * @return
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public String getCosCondLstAsXML(Connection con, long itm_id)
		                   throws SQLException, cwSysMessage {
        DbCourseCriteria ccr = new DbCourseCriteria();
        DbCourseMeasurement cmt = new DbCourseMeasurement();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
        ccr.getCcrIdByItmNType(con);
        ccr.get(con);
         
        StringBuffer cmtBuffer = new StringBuffer();
        cmtBuffer.append("<cmt_online>");
        //get online module        
        Vector cmtVec = DbCourseMeasurement.getCmtLstByType(con, ccr.ccr_id, 0);
        for(int i = 0; i < cmtVec.size(); i++){
            cmt = (DbCourseMeasurement) cmtVec.get(i);
            cmtBuffer.append("<item id=\"").append(cmt.getCmt_id()).append("\" ")
                     .append("desc_option=\"").append(cmt.getCmt_status_desc_option()).append("\">").append(cwUtils.NEWL)
                     .append("<title>").append(cwUtils.esc4XML(cmt.getCmt_title())).append("</title>").append(cwUtils.NEWL)
                     .append("</item>").append(cwUtils.NEWL);
        }
        if(cmtVec.size() < 10 ){
            for(int j = cmtVec.size(); j < 10; j++){
                cmtBuffer.append("<item/>").append(cwUtils.NEWL);
            }
        }
        cmtBuffer.append("</cmt_online>").append(cwUtils.NEWL);
        //get scoring item
        Vector cmtVec2 = DbCourseMeasurement.getCmtLstByType(con, ccr.ccr_id, 1);
        cmtBuffer.append("<cmt_scoring>").append(cwUtils.NEWL);
        for(int i = 0; i < cmtVec2.size(); i++){
            cmt = (DbCourseMeasurement) cmtVec2.get(i);
            cmtBuffer.append("<item id=\"").append(cmt.getCmt_id()).append("\" ")
                     .append("desc_option=\"").append(cmt.getCmt_status_desc_option()).append("\">").append(cwUtils.NEWL)
                     .append("<title>").append(cwUtils.esc4XML(cmt.getCmt_title())).append("</title>").append(cwUtils.NEWL)
                     .append("</item>").append(cwUtils.NEWL);
        }
        cmtBuffer.append("</cmt_scoring>").append(cwUtils.NEWL);
        
        StringBuffer result = new StringBuffer();
        result.append("<comp_cond_lst>").append(cwUtils.NEWL)
              .append("<ccr pass_ind=\"").append(ccr.ccr_pass_ind).append("\" ")
              .append("id=\"").append(ccr.ccr_id).append("\" ")
              .append("pass_score=\"").append(ccr.ccr_pass_score).append("\" ")
              .append("attend_rate=\"").append(ccr.ccr_attendance_rate).append("\" ")
              .append("last_update=\"").append(ccr.ccr_upd_timestamp).append("\">").append(cwUtils.NEWL);
        result.append(cmtBuffer)
              .append("<offline_cond>").append(cwUtils.esc4XML(ccr.ccr_offline_condition)).append("</offline_cond>")
              .append("</ccr>")
              .append("</comp_cond_lst>").append(cwUtils.NEWL);
		return result.toString();
	}
	
	public String getCmtAsXml(Connection con, long cmt_id) throws SQLException{
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		DbCourseModuleCriteria dbcmr = new DbCourseModuleCriteria();
		dbModule dbm = new dbModule();
		StringBuffer result = new StringBuffer(2000);
		dbcmt.getByCmtId(con, cmt_id);
		
		Float mod_pass_score = dbcmt.getCmt_pass_score();
		if(dbcmt.getCmt_cmr_id() > 0){
		    String mod_type = dbm.getCrmModType( con, dbcmt.getCmt_cmr_id());
             if("TST".equalsIgnoreCase(mod_type) || "DXT".equalsIgnoreCase(mod_type) || "ASS".equalsIgnoreCase(mod_type)){
                 mod_pass_score = dbcmt.getCmt_max_score() * (mod_pass_score/100);
             }
		}
		result.append("<score_item cmt_id=\"").append(dbcmt.getCmt_id()).append("\">").append(cwUtils.NEWL);
		result.append("<cmt_ccr_id>").append(dbcmt.getCmt_ccr_id()).append("</cmt_ccr_id>").append(cwUtils.NEWL);
		result.append("<cmt_title>").append(cwUtils.esc4XML(dbcmt.getCmt_title())).append("</cmt_title>").append(cwUtils.NEWL);
		result.append("<cmt_cmr_id>").append(dbcmt.getCmt_cmr_id()).append("</cmt_cmr_id>").append(cwUtils.NEWL);
		result.append("<cmt_max_score>").append(dbcmt.getCmt_max_score()).append("</cmt_max_score>").append(cwUtils.NEWL);
		result.append("<cmt_pass_score>").append(mod_pass_score).append("</cmt_pass_score>").append(cwUtils.NEWL);
		result.append("<cmt_contri_rate>").append(dbcmt.getCmt_contri_rate()).append("</cmt_contri_rate>").append(cwUtils.NEWL);
		result.append("<cmt_is_contri_by_score>").append(dbcmt.getCmt_is_contri_by_score()).append("</cmt_is_contri_by_score>").append(cwUtils.NEWL);
		result.append("<cmt_update_timestamp>").append(dbcmt.getCmt_update_timestamp()).append("</cmt_update_timestamp>").append(cwUtils.NEWL);		
		if (dbcmt.getCmt_cmr_id() != 0 ){			
			dbcmr.getByCmrId(con, dbcmt.getCmt_cmr_id());
			result.append("<mod_res_id>").append(dbcmr.cmr_res_id).append("</mod_res_id>").append(cwUtils.NEWL);		
		} else {
			result.append("<mod_res_id>").append(0).append("</mod_res_id>").append(cwUtils.NEWL);					
		}
		result.append("</score_item>").append(cwUtils.NEWL);
		if (dbcmt.getCmt_cmr_id() != 0 )
		result.append(dbm.getModuleAsXmlByCmrResId(con,dbcmr.cmr_res_id));
		return result.toString();
	}
	
	public boolean updCmt(Connection con, long cmt_id, long ccr_id, String title, long mod_res_id, float max_score, float pass_score, Timestamp upd_timestamp, loginProfile prof) throws SQLException, cwSysMessage, qdbException{
		Timestamp curTime;
		try {
			curTime = dbUtils.getTime(con);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		dbModule dbm = new dbModule();
		Hashtable insert_id = new Hashtable();
		long cmr_parent_id = 0;
		if (updCmtExe( con,  cmt_id,  ccr_id,  title,  mod_res_id,  max_score,  pass_score,  upd_timestamp,  prof, cmr_parent_id, insert_id, curTime)) {
			List ch_cmt_id_list = dbcmt.getChCmtIdList( con, cmt_id);
			if (insert_id.get("CMR_PARENT_ID") != null) {
				cmr_parent_id = ((Long)insert_id.get("CMR_PARENT_ID")).longValue();
			}
			float mod_max_score = max_score;
			float mod_pass_score = pass_score;
			if(mod_res_id != 0){
				dbm.mod_res_id = mod_res_id;
				dbm.get(con);
				mod_max_score = dbm.mod_max_score;
				mod_pass_score = dbm.mod_pass_score;
			}
			for (int i = 0; i < ch_cmt_id_list.size();i++) {
				if (ch_cmt_id_list.get(i) != null) {
					long ch_mod_res_id = 0;
					long ch_cmt_id = ((Long)ch_cmt_id_list.get(i)).longValue();
					long ch_itm_id = DbCourseCriteria.getCcrItmIdByCmtId( con, ch_cmt_id);
					long ch_ccr_id = DbCourseCriteria.getCcrIdByCmtId( con,  ch_cmt_id);
					if (mod_res_id != 0){
						ch_mod_res_id = dbModule.getChidltmModID( con,  mod_res_id,  ch_itm_id);
					}
                    upd_timestamp = dbcmt.getLastUpdTimestamp( con, ch_cmt_id);
					if (!updCmtExe( con,  ch_cmt_id,  ch_ccr_id,  title,  ch_mod_res_id,  max_score,  pass_score,  upd_timestamp,  prof, cmr_parent_id, insert_id, curTime)) {
						throw new cwSysMessage(REC_UPDATED);
					}
					//修改计分规则的时候把他的班级的分数也要一起更新dbmodule
					if(mod_res_id != 0 && ch_mod_res_id != 0){
						dbm.mod_res_id = ch_mod_res_id;
						dbm.get(con);
						dbm.mod_max_score = mod_max_score;
						dbm.mod_pass_score = mod_pass_score;
						dbm.updMaxScoreAndPassScore(con);
					}
				}
			}
			return true;
		}else{
			throw new cwSysMessage(REC_UPDATED);
		}
	}
	private boolean updCmtExe(Connection con, long cmt_id, long ccr_id, String title, long mod_res_id, float max_score, float pass_score, Timestamp upd_timestamp, loginProfile prof, long cmr_id_parent, Hashtable insert_id, Timestamp curTime) throws SQLException, cwSysMessage{
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		DbCourseModuleCriteria dbcmr = new DbCourseModuleCriteria();
		DbMeasurementEvaluation dbmv = new DbMeasurementEvaluation();
		
		dbcmt.getByCmtId(con, cmt_id);

		if (!dbcmt.checkTitleForUpd(con,title,ccr_id,cmt_id)){
			throw new cwSysMessage(TITLE_EXIST);
		} else {
			if (dbcmt.getCmt_cmr_id()== 0){
				if (mod_res_id == 0){
					if (dbmv.checkEvalByCmtId(con, cmt_id)) {
						if (dbcmt.getCmt_max_score() != max_score) {
							throw new cwSysMessage(EVAL_EXIST);
						}
					}
					dbcmt.setCmt_title(title);
					dbcmt.setCmt_max_score(max_score);
					dbcmt.setCmt_pass_score(pass_score);
					dbcmt.setCmt_update_usr_id(prof.usr_id);
					dbcmt.setCmt_update_timestamp(curTime);				
					if( dbcmt.upd(con, upd_timestamp) == 0 ){
						throw new cwSysMessage(REC_UPDATED);
					} else return true;
				} else {
					if(dbcmr.getCmrId(con,mod_res_id,ccr_id) == 0){
						dbcmr.cmr_ccr_id = ccr_id;
						dbcmr.cmr_res_id = mod_res_id;
						dbcmr.cmr_contri_rate = dbcmt.getCmt_contri_rate();
						dbcmr.cmr_create_timestamp = curTime;
						dbcmr.cmr_create_usr_id = prof.usr_id;
						dbcmr.cmr_upd_timestamp = curTime;
						dbcmr.cmr_upd_usr_id = prof.usr_id;
						dbcmr.cmr_is_contri_by_score = true;
						dbcmr.cmr_status = dbcmt.getCmt_status();;
						dbcmr.cmr_cmr_id_parent = cmr_id_parent;
						dbcmr.ins(con);
						insert_id.put("CMR_PARENT_ID",new Long(dbcmr.cmr_id));
						dbmv.delByCmtId(con,cmt_id);
						dbcmt.setCmt_cmr_id(dbcmr.getCmrId(con,mod_res_id,ccr_id));
						dbcmt.setCmt_title(title);
						dbcmt.setCmt_contri_rate(0);
						dbcmt.setCmt_max_score(0);
						dbcmt.setCmt_pass_score(0);
						dbcmt.setCmt_update_timestamp(curTime);
						dbcmt.setCmt_update_usr_id(prof.usr_id);
						if(dbcmt.upd(con, upd_timestamp) == 0){
							throw new cwSysMessage(REC_UPDATED);
						} else return true;
					} else throw new cwSysMessage(REC_UPDATED);
				}
			} else {
				if (mod_res_id == 0){
					dbcmr.getByCmrId(con,dbcmt.getCmt_cmr_id());
					dbcmt.setCmt_contri_rate(dbcmr.cmr_contri_rate);
					dbcmr.softDelByCmrId(con, dbcmt.getCmt_cmr_id(), prof.usr_id);
					dbcmt.setCmt_cmr_id(0);
					dbcmt.setCmt_title(title);
					dbcmt.setCmt_max_score(max_score);
					dbcmt.setCmt_pass_score(pass_score);
					dbcmt.setCmt_update_timestamp(curTime);
					dbcmt.setCmt_update_usr_id(prof.usr_id);
					if (dbcmt.upd(con, upd_timestamp) == 0){
						throw new cwSysMessage(REC_UPDATED);
					} else return true;
				} else {
					dbcmr.getByCmrId(con,dbcmt.getCmt_cmr_id());
					dbcmr.cmr_is_contri_by_score = true;
//					dbcmr.cmr_status = null;
					dbcmr.cmr_ccr_id = ccr_id;
					dbcmr.cmr_upd_timestamp = curTime;
					dbcmr.cmr_upd_usr_id = prof.usr_id;
					dbcmr.upd(con);
                    if (dbcmr.cmr_res_id != mod_res_id){
                        dbcmr.cmr_res_id = mod_res_id;
                        dbcmr.updResID(con);
                    }
					dbcmt.setCmt_title(title);
					dbcmt.setCmt_update_timestamp(curTime);
					dbcmt.setCmt_update_usr_id(prof.usr_id);
					if(dbcmt.upd(con, upd_timestamp) == 0){
						throw new cwSysMessage(REC_UPDATED);
					} else return true;
				}
			}
		}
	}
	
	/**
	 * 
	 * @param con Connection Object
	 * @param itm_id itm_id of aeItem
	 * @return
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	//public String getRunCondLstAsXML(Connection con, long itm_id)
	public String getCondLstAsXML(Connection con, long itm_id)
		                   throws SQLException, cwSysMessage {
        DbCourseCriteria ccr = new DbCourseCriteria();
        DbCourseMeasurement cmt = new DbCourseMeasurement();
        ccr.ccr_itm_id = itm_id;
        ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
        ccr.getCcrIdByItmNType(con);
        ccr.get(con);
        
        aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		itm.getItem(con);		
         
        StringBuffer cmtBuffer = new StringBuffer();
        cmtBuffer.append("<cmt_online>");
        //get online module        
        ViewCourseModuleCriteria.ViewCmr[] viewCmr = ViewCourseModuleCriteria.getCmrLstByType(con, ccr.ccr_id, 0);
        for (int i = 0; i < viewCmr.length; i++) {
            cmtBuffer.append("<item cmt_id=\"").append(viewCmr[i].cmt_id).append("\" ")
            		 .append("cmr_id=\"").append(viewCmr[i].cmr_id).append("\" ")
            		 .append("res_id=\"").append(viewCmr[i].cmr_res_id).append("\" ")
            		 .append("desc_option=\"").append(viewCmr[i].cmr_status_desc_option).append("\">").append(cwUtils.NEWL)
            		 .append("<title>").append(cwUtils.esc4XML(viewCmr[i].cmt_title)).append("</title>").append(cwUtils.NEWL)
            		 .append("<cmr_status>").append(viewCmr[i].cmr_status).append("</cmr_status>").append(cwUtils.NEWL)
            		 .append("<res_subtype>").append(viewCmr[i].res_subtype).append("</res_subtype>").append(cwUtils.NEWL)
            		 .append("</item>");
        }
        Vector cmtVec = DbCourseMeasurement.getCmtLstByType(con, ccr.ccr_id, 0);
        for (int i = 0; i < cmtVec.size(); i++) {
            cmt = (DbCourseMeasurement) cmtVec.get(i);
            cmtBuffer.append("<item cmt_id=\"").append(cmt.getCmt_id()).append("\" ")
            		 .append("cmr_id=\"\" ")
            		 .append("res_id=\"\" ")
                     .append("desc_option=\"").append(cmt.getCmt_status_desc_option()).append("\">").append(cwUtils.NEWL)
                     .append("<title>").append(cwUtils.esc4XML(cmt.getCmt_title())).append("</title>").append(cwUtils.NEWL)
                     .append("</item>").append(cwUtils.NEWL);
        }
        if ((viewCmr.length + cmtVec.size()) < 10) {
            for (int j = (viewCmr.length + cmtVec.size()); j < 10; j++) {
                cmtBuffer.append("<item/>").append(cwUtils.NEWL);
            }
        }
        cmtBuffer.append("</cmt_online>").append(cwUtils.NEWL);
        //get scoring item
        cmtBuffer.append("<cmt_scoring>").append(cwUtils.NEWL);
        ViewCourseModuleCriteria.ViewCmr[] viewCmr2 = ViewCourseModuleCriteria.getCmrLstByType(con, ccr.ccr_id, 1);
        for (int i = 0; i < viewCmr2.length; i++) {
            cmtBuffer.append("<item cmt_id=\"").append(viewCmr2[i].cmt_id).append("\" ")
            		 .append("cmr_id=\"").append(viewCmr2[i].cmr_id).append("\" ")
            		 .append("res_id=\"").append(viewCmr2[i].cmr_res_id).append("\" ")
            		 .append("desc_option=\"").append(viewCmr2[i].cmr_status_desc_option).append("\">").append(cwUtils.NEWL)
            		 .append("<title>").append(cwUtils.esc4XML(viewCmr2[i].cmt_title)).append("</title>").append(cwUtils.NEWL)
            		 .append("</item>");
        }
        Vector cmtVec2 = DbCourseMeasurement.getCmtLstByType(con, ccr.ccr_id, 1);
        for (int i = 0; i < cmtVec2.size(); i++) {
            cmt = (DbCourseMeasurement) cmtVec2.get(i);
            cmtBuffer.append("<item cmt_id=\"").append(cmt.getCmt_id()).append("\" ")
            		 .append("cmr_id=\"\" ")
                     .append("desc_option=\"").append(cmt.getCmt_status_desc_option()).append("\">").append(cwUtils.NEWL)
                     .append("<title>").append(cwUtils.esc4XML(cmt.getCmt_title())).append("</title>").append(cwUtils.NEWL)
                     .append("</item>").append(cwUtils.NEWL);
        }
        cmtBuffer.append("</cmt_scoring>").append(cwUtils.NEWL);
        
        StringBuffer result = new StringBuffer();
        result.append("<comp_cond_lst>").append(cwUtils.NEWL)
              .append("<ccr pass_ind=\"").append(ccr.ccr_pass_ind).append("\" ")
              .append("id=\"").append(ccr.ccr_id).append("\" ")
              .append("pass_score=\"").append(ccr.ccr_pass_score).append("\" ")
              .append("attend_rate=\"").append(ccr.ccr_attendance_rate).append("\" ")
              .append("last_update=\"").append(ccr.ccr_upd_timestamp).append("\">").append(cwUtils.NEWL);
		long cos_id = dbCourse.getCosResId(con, itm_id);
		dbModule mod = new dbModule();
		result.append(mod.getAllOnlineModuleAsXml(con, cos_id, ccr.ccr_id));
        result.append(cmtBuffer)
              .append("<offline_cond>").append(cwUtils.esc4XML(ccr.ccr_offline_condition)).append("</offline_cond>")
              .append("</ccr>").append(cwUtils.NEWL)
              .append("</comp_cond_lst>").append(cwUtils.NEWL);
        
        if((itm.itm_create_run_ind&&itm.itm_content_def!=null&&itm.itm_content_def.equalsIgnoreCase(aeItem.PARENT))||//if class room course and prarent module
    			(!itm.itm_create_run_ind&&!itm.itm_run_ind&&itm.itm_apply_ind)||  //if on line course
    			(!itm.itm_create_run_ind&&itm.itm_run_ind&&itm.itm_apply_ind&&itm.itm_content_def!=null&&itm.itm_content_def.equalsIgnoreCase(aeItem.CHILD))//if class and chrild module
    			){
    	    	result.append("<editable>true</editable>");
    		 }else{
    			 result.append("<editable>false</editable>");
    		 }
		return result.toString();
	}
	
	public boolean modCourseCriteriaCond(Connection con, loginProfile prof,
            long ccr_id, long itm_id, String pass_status, String attend_status,
            int ccr_pass_score, int ccr_attendance_rate, Vector cmtOnlineVec,
            Vector cmtScoringVec, String offline_cond, Timestamp upd_timestamp)
            throws SQLException, cwSysMessage {
        boolean result = false;
        Timestamp curTime;
		try {
			curTime = dbUtils.getTime(con);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
    	aeItem itm = new aeItem();
		itm.itm_id = itm_id;
        String type = DbCourseCriteria.getCcrType( con, ccr_id);
		boolean createRunInd = itm.getCreateRunInd(con);
        if (ccr_id > 0 && upd_timestamp != null) {
        	List parent_id_list = new ArrayList();
        	parent_id_list = modCourseCriteriaCondExe( con,  prof, ccr_id,  itm_id,  pass_status,  attend_status, ccr_pass_score,  ccr_attendance_rate,  cmtOnlineVec, cmtScoringVec,  offline_cond,  upd_timestamp,  parent_id_list, curTime);
    		if (createRunInd) {
    			
    			List ch_itm_id_list = itm.getChItemIDList( con, itm_id);
    			for (int i=0;i<ch_itm_id_list.size();i++){
    				if (ch_itm_id_list.get(i) != null){
    					long ch_itm_id = ((Long)ch_itm_id_list.get(i)).longValue();
    					long ch_ccr_id=  DbCourseCriteria.getCcrIdByItmID( con,  ch_itm_id, type) ;
    					Vector ch_cmtOnlineVec = new Vector();
    					Vector ch_cmtScoringVec = new Vector();
    	                for (int j = 0; j < cmtOnlineVec.size(); j++) {
    	                    DbCourseMeasurement cmt = (DbCourseMeasurement) cmtOnlineVec.get(j);
    	                    DbCourseMeasurement ch_cmt = new DbCourseMeasurement();
    	                    ch_cmt.setCmt_title(cmt.getCmt_title());
    	                    ch_cmt.setCmt_id(ch_cmt.getChCmtIdByCcrIDCmtParentID( con, ch_ccr_id, cmt.getCmt_id()));
    	                    ch_cmt.setCmt_status_desc_option(cmt.getCmt_status_desc_option());
    	                    ch_cmt.setCmt_ccr_id(ch_ccr_id);
    	                    ch_cmtOnlineVec.addElement(ch_cmt);
    	                }
    	                for (int j = 0; j < cmtScoringVec.size(); j++) {
    	                    DbCourseMeasurement cmt = (DbCourseMeasurement) cmtScoringVec.get(j);
    	                    DbCourseMeasurement ch_cmt = new DbCourseMeasurement();
    	                    ch_cmt.setCmt_id(ch_cmt.getChCmtIdByCcrIDCmtParentID( con, ch_ccr_id, cmt.getCmt_id()));
    	                    ch_cmt.setCmt_status_desc_option(cmt.getCmt_status_desc_option());
    	                    ch_cmtScoringVec.addElement(ch_cmt);
    	                }
    	                modCourseCriteriaCondExe( con,  prof, ch_ccr_id,  ch_itm_id,  pass_status,  attend_status, ccr_pass_score,  ccr_attendance_rate,  ch_cmtOnlineVec, ch_cmtScoringVec,  offline_cond,  upd_timestamp,parent_id_list, curTime);
    	                	
    				}
    			}
    		}
        		
        }
        return result;
    }
	
	private List modCourseCriteriaCondExe(Connection con, loginProfile prof,
            long ccr_id, long itm_id, String pass_status, String attend_status,
            int ccr_pass_score, int ccr_attendance_rate, Vector cmtOnlineVec,
            Vector cmtScoringVec, String offline_cond, Timestamp upd_timestamp,
            List parent_id_list,Timestamp cur_time)
            throws SQLException, cwSysMessage {
		List insert_id_list = new ArrayList();
        if (ccr_id > 0 && upd_timestamp != null) {
            DbCourseCriteria ccr = new DbCourseCriteria();
            ccr.ccr_id = ccr_id;
            ccr.ccr_upd_timestamp = upd_timestamp;
            if (ccr.checkUpdTimestamp(con)) {
                if (pass_status.equalsIgnoreCase("on")) {
                    ccr.ccr_pass_ind = true;
                    ccr.ccr_pass_score = (int) ccr_pass_score;
                } else {
                    ccr.ccr_pass_ind = false;
                    ccr.ccr_pass_score = 0;
                }

                if (attend_status.equalsIgnoreCase("on")) {
                    ccr.ccr_attendance_rate = (int) ccr_attendance_rate;
                } else {
                    ccr.ccr_attendance_rate = 0;
                }
                ccr.ccr_offline_condition = offline_cond;
                ccr.ccr_upd_usr_id = prof.usr_id;
                ccr.updCond(con);
                
                for (int i = 0; i < cmtOnlineVec.size(); i++) {
                	long cmt_parent_id = 0;
                	Hashtable insert_id = new Hashtable();
                    DbCourseMeasurement cmt = (DbCourseMeasurement) cmtOnlineVec.get(i);
                    
                    if(parent_id_list.size()>i && parent_id_list.get(i) != null){
	                	Hashtable parent_id = (Hashtable)parent_id_list.get(i);
	                	if (parent_id.get("CMT_PARENT_ID") != null) {
	        				cmt_parent_id = ((Long)parent_id.get("CMT_PARENT_ID")).longValue();
	        			}
                	}
                    if (cmt.getCmt_title() != null && cmt.getCmt_title().length() > 0 && cmt.getCmt_id() == 0) {
                        cmt.setCmt_statusByOption(cmt
                                .getCmt_status_desc_option(),
                                DbCourseMeasurement.online_cond);
                        cmt.setCmt_ccr_id(ccr_id);
                        cmt.setCmt_is_contri_by_score(false);
                        cmt.setCmt_create_timestamp(cur_time);
                        cmt.setCmt_create_usr_id(prof.usr_id);
                        cmt.setCmt_update_timestamp(cur_time);
                        cmt.setCmt_update_usr_id(prof.usr_id);
                        cmt.setCmt_cmt_id_parent(cmt_parent_id);
                        cmt.ins(con);
                        insert_id.put("CMT_PARENT_ID",new Long(cmt.getCmt_id()));
                    } else if(cmt.getCmt_title() != null && cmt.getCmt_title().length() > 0 && cmt.getCmt_id() > 0){
                        cmt.setCmt_statusByOption(cmt
                                .getCmt_status_desc_option(),
                                DbCourseMeasurement.online_cond);
                        cmt.setCmt_ccr_id(ccr_id);
                        cmt.setCmt_is_contri_by_score(false);
                        cmt.setCmt_update_timestamp(cur_time);
                        cmt.setCmt_update_usr_id(prof.usr_id);
                        cmt.updCosOnlineCond(con);
                    } else if(cmt.getCmt_title() == null || cmt.getCmt_title().length() <= 0) {
                        cmt.delByCmtId(con);
                    }
                    insert_id_list.add(insert_id);
                }

                for (int i = 0; i < cmtScoringVec.size(); i++) {
                    DbCourseMeasurement cmt = (DbCourseMeasurement) cmtScoringVec
                            .get(i);
                    if (cmt.getCmt_id() > 0) {
                        cmt.setCmt_statusByOption(cmt
                                .getCmt_status_desc_option(),
                                DbCourseMeasurement.online_cond);
                        cmt.setCmt_update_timestamp(cur_time);
                        cmt.setCmt_update_usr_id(prof.usr_id);
                        cmt.updCosScoringCond(con);
                    }
                }
            }
        }
        return insert_id_list;
    }
	
	public boolean modCriteriaCond(Connection con, loginProfile prof,
            long ccr_id, long itm_id, String pass_status, String attend_status,
            int ccr_pass_score, int ccr_attendance_rate, Vector cmtOnlineVec,
            Vector cmtScoringVec, String offline_cond, Timestamp upd_timestamp)
            throws SQLException, cwSysMessage {
		boolean result = false;
		Timestamp curTime;
		try {
			curTime = dbUtils.getTime(con);
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		}
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		boolean createRunInd = itm.getCreateRunInd(con);
		String type = DbCourseCriteria.getCcrType( con, ccr_id);
        if (ccr_id > 0 && upd_timestamp != null) {
        	List parent_id_list = new ArrayList();
        	parent_id_list = modRunCriteriaCondExe( con,  prof, ccr_id,  itm_id,  pass_status,  attend_status, ccr_pass_score, 
        			                                ccr_attendance_rate,  cmtOnlineVec, cmtScoringVec,  offline_cond,  upd_timestamp,  parent_id_list, curTime);
	        if (createRunInd) {
    			List ch_itm_id_list = itm.getChItemIDList( con, itm_id);
    			for (int i=0;i<ch_itm_id_list.size();i++){
    				long ch_itm_id = ((Long)ch_itm_id_list.get(i)).longValue();
					long ch_ccr_id=  DbCourseCriteria.getCcrIdByItmID( con,  ch_itm_id, type) ;
					Vector ch_cmtOnlineVec = new Vector();
					Vector ch_cmtScoringVec = new Vector();
					DbCourseMeasurement cmt = new DbCourseMeasurement();
					DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
					DbCourseCriteria ccr = new DbCourseCriteria();
					upd_timestamp = ccr.getLastUpdTimestamp(con, ch_ccr_id);
					for (int j = 0; j < cmtOnlineVec.size(); j++) {
	                    Hashtable hashcm = (Hashtable) cmtOnlineVec.get(j);
	                    long cmt_id, cmr_id, res_id;
                    	Hashtable ch_hashcm = new Hashtable();
                    	
                        ch_hashcm.put("status_desc_option",hashcm.get("status_desc_option"));
                        ch_hashcm.put("cmt_title",hashcm.get("cmt_title"));
                        cmt_id = ((Long) hashcm.get("cmt_id")).longValue();
                        ch_hashcm.put("cmt_id",new Long(cmt.getChCmtIdByCcrIDCmtParentID( con, ch_ccr_id, cmt_id)));
                        cmr_id = ((Long) hashcm.get("cmr_id")).longValue();
                        ch_hashcm.put("cmr_id",new Long(cmr.getChCmrIdByCcrIDCmrParentID( con, ch_ccr_id, cmr_id)));
                        res_id = ((Long) hashcm.get("res_id")).longValue();
                        ch_hashcm.put("res_id",new Long(dbModule.getChidltmModID( con,res_id, ch_itm_id)));
                        ch_hashcm.put("cmr_status", hashcm.get("cmr_status"));
                        ch_cmtOnlineVec.add(ch_hashcm);
					}
					
					 //Scoring Item
	                for (int j = 0; j < cmtScoringVec.size(); j++) {
	                    Hashtable hashcm = (Hashtable) cmtScoringVec.get(j);
	                    long cmt_id, cmr_id;
	                    //Timestamp time_stamp = cwSQL.getTime(con);
                    	Hashtable ch_hashcm = new Hashtable();
                    	ch_hashcm.put("status_desc_option",hashcm.get("status_desc_option"));
                        cmt_id = ((Long) hashcm.get("cmt_id")).longValue();
                        ch_hashcm.put("cmt_id",new Long(cmt.getChCmtIdByCcrIDCmtParentID( con, ch_ccr_id, cmt_id)));
                        cmr_id = ((Long) hashcm.get("cmr_id")).longValue();
                        ch_hashcm.put("cmr_id",new Long(cmr.getChCmrIdByCcrIDCmrParentID( con, ch_ccr_id, cmr_id)));
                        ch_cmtScoringVec.add(ch_hashcm);
	                }
	               modRunCriteriaCondExe( con,  prof, ch_ccr_id,  ch_itm_id,  pass_status,  attend_status, ccr_pass_score, ccr_attendance_rate, 
	            		                  ch_cmtOnlineVec, ch_cmtScoringVec,  offline_cond,  upd_timestamp,parent_id_list, curTime) ;
    				
    			}
	        }
	        result = true;
	   }
		return result;
	}
	
	private List modRunCriteriaCondExe(Connection con, loginProfile prof,
            long ccr_id, long itm_id, String pass_status, String attend_status,
            int ccr_pass_score, int ccr_attendance_rate, Vector cmtOnlineVec,
            Vector cmtScoringVec, String offline_cond, Timestamp upd_timestamp,
            List parent_id_list, Timestamp curTime)
            throws SQLException, cwSysMessage {
		List insert_id_list = new ArrayList();
        if (ccr_id > 0 && upd_timestamp != null) {
            DbCourseCriteria ccr = new DbCourseCriteria();
            ccr.ccr_id = ccr_id;
            ccr.ccr_upd_timestamp = upd_timestamp;
            if (ccr.checkUpdTimestamp(con)) {
                if (pass_status.equalsIgnoreCase("on")) {
                    ccr.ccr_pass_ind = true;
                    ccr.ccr_pass_score = (int) ccr_pass_score;
                } else {
                    ccr.ccr_pass_ind = false;
                    ccr.ccr_pass_score = 0;
                }
                if (attend_status.equalsIgnoreCase("on")) {
                    ccr.ccr_attendance_rate = (int) ccr_attendance_rate;
                } else {
                    ccr.ccr_attendance_rate = 0;
                }
                ccr.ccr_offline_condition = offline_cond;
                ccr.ccr_upd_usr_id = prof.usr_id;
                ccr.updCond(con);
                
                if (cmtOnlineVec.size() == 0) {
                    DbCourseModuleCriteria.delOld(con, ccr_id);
                }
                for (int i = 0; i < cmtOnlineVec.size(); i++) {
                	long cmr_parent_id = 0;
                    long cmt_parent_id = 0;
                	Hashtable insert_id = new Hashtable();
	                if(parent_id_list.size()>i && parent_id_list.get(i) != null){
	                	Hashtable parent_id = (Hashtable)parent_id_list.get(i);
	                	if (parent_id.get("CMT_PARENT_ID") != null) {
	        				cmt_parent_id = ((Long)parent_id.get("CMT_PARENT_ID")).longValue();
	        			}
			        	if (parent_id.get("CMR_PARENT_ID") != null) {
	        				cmr_parent_id = ((Long)parent_id.get("CMR_PARENT_ID")).longValue();
	        			}
                	}
                    Hashtable hashcm = (Hashtable) cmtOnlineVec.get(i);
                    long cmt_id, cmr_id, res_id;
                    String desc_option, title, cmr_status;
                    desc_option = (String) hashcm.get("status_desc_option");
                    title = (String) hashcm.get("cmt_title");
                    cmt_id = ((Long) hashcm.get("cmt_id")).longValue();
                    cmr_id = ((Long) hashcm.get("cmr_id")).longValue();
                    res_id = ((Long) hashcm.get("res_id")).longValue();
                    cmr_status = (String) hashcm.get("cmr_status");
                    if (title.length() > 0 && cmr_id == 0 && cmt_id == 0) {
                        //new added
                        if (res_id > 0) {
                            //new added and linked
                            DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
                            cmr.cmr_res_id = res_id;
                            cmr.cmr_ccr_id = ccr_id;
//                            cmr.setCmr_statusByOption(desc_option, DbCourseMeasurement.online_cond);
                            cmr.cmr_status = cmr_status;
                            cmr.cmr_status_desc_option = desc_option;
                            cmr.cmr_is_contri_by_score = false;
                            cmr.cmr_create_timestamp = curTime;
                            cmr.cmr_create_usr_id = prof.usr_id;
                            cmr.cmr_upd_timestamp = curTime;
                            cmr.cmr_upd_usr_id = prof.usr_id;
                            cmr.cmr_cmr_id_parent = cmr_parent_id;
                            cmr.ins(con);
                            insert_id.put("CMR_PARENT_ID",new Long(cmr.cmr_id));

                            DbCourseMeasurement cmt = new DbCourseMeasurement();
                            cmt.setCmt_ccr_id(ccr_id);
                            cmt.setCmt_title(title);
                            cmt.setCmt_cmr_id(cmr.cmr_id);
                            cmt.setCmt_is_contri_by_score(false);
                            cmt.setCmt_create_timestamp(curTime);
                            cmt.setCmt_create_usr_id(prof.usr_id);
                            cmt.setCmt_update_timestamp(curTime);
                            cmt.setCmt_update_usr_id(prof.usr_id);
                            cmt.setCmt_cmt_id_parent(cmt_parent_id);
                            cmt.ins(con);
                            insert_id.put("CMT_PARENT_ID",new Long(cmt.getCmt_id()));
                        } else if (res_id == 0) {
                            //new added but not linked
                            DbCourseMeasurement cmt = new DbCourseMeasurement();
                            cmt.setCmt_statusByOption(desc_option,
                                    DbCourseMeasurement.online_cond);
                            cmt.setCmt_ccr_id(ccr_id);
                            cmt.setCmt_title(title);
                            cmt.setCmt_status_desc_option(desc_option);
                            cmt.setCmt_is_contri_by_score(false);
                            cmt.setCmt_create_timestamp(curTime);
                            cmt.setCmt_create_usr_id(prof.usr_id);
                            cmt.setCmt_update_timestamp(curTime);
                            cmt.setCmt_update_usr_id(prof.usr_id);
                            cmt.setCmt_cmt_id_parent(cmt_parent_id);
                            cmt.ins(con);
                            insert_id.put("CMT_PARENT_ID",new Long(cmt.getCmt_id()));
                        }

                    } else if (cmr_id == 0 && cmt_id != 0) {
                        // not linked
                        if(title == null || title.length() <= 0 ){
                            DbCourseMeasurement cmt = new DbCourseMeasurement();
                            cmt.setCmt_id(cmt_id);
                            cmt.delByCmtId(con);
                        } else {
                            if (res_id > 0) {
                                // now linked
                                DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
                                cmr.cmr_res_id = res_id;
                                cmr.cmr_ccr_id = ccr_id;
//                                cmr.setCmr_statusByOption(desc_option, DbCourseMeasurement.online_cond);
                                cmr.cmr_status = cmr_status;
                                cmr.cmr_status_desc_option = desc_option;
                                cmr.cmr_is_contri_by_score = false;
                                cmr.cmr_create_timestamp = curTime;
                                cmr.cmr_create_usr_id = prof.usr_id;
                                cmr.cmr_upd_timestamp = curTime;
                                cmr.cmr_upd_usr_id = prof.usr_id;  
                                cmr.cmr_cmr_id_parent = cmr_parent_id;
                                cmr.ins(con);
                                insert_id.put("CMR_PARENT_ID",new Long(cmr.cmr_id));
                                
                                DbCourseMeasurement cmt = new DbCourseMeasurement();
                                cmt.setCmt_id(cmt_id);
                                cmt.setCmt_title(title);
                                cmt.setCmt_update_timestamp(curTime);
                                cmt.setCmt_update_usr_id(prof.usr_id);
                                cmt.setCmt_cmr_id(cmr.cmr_id);
                                cmt.updCosOnlineCond(con);
                            } else if (res_id == 0) {
                                // still not linked
                                DbCourseMeasurement cmt = new DbCourseMeasurement();
                                cmt.setCmt_ccr_id(ccr_id);
                                cmt.setCmt_id(cmt_id);
                                cmt.setCmt_title(title);
                                cmt.setCmt_status_desc_option(desc_option);
                                cmt.setCmt_is_contri_by_score(false);
                                cmt.setCmt_statusByOption(desc_option,
                                        DbCourseMeasurement.online_cond);
                                cmt.setCmt_update_timestamp(curTime);
                                cmt.setCmt_update_usr_id(prof.usr_id);
                                cmt.updCosOnlineCond(con);
                            }
                        }
                    } else if (cmr_id != 0 && cmt_id != 0) {
                        //an online module has already linked
                        if(title == null || title.length() <= 0){
                            DbCourseMeasurement cmt = new DbCourseMeasurement();
                            cmt.setCmt_id(cmt_id);
                            cmt.delByCmtId(con);
                            DbCourseModuleCriteria.delByCmrId(con, cmr_id);
                        } else {
                            if (res_id > 0) {
                                //still linked, maybe change the link or title
                                DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
                                cmr.cmr_id = cmr_id;
                                cmr.cmr_res_id = res_id;
                                cmr.cmr_ccr_id = ccr_id;
//                                cmr.setCmr_statusByOption(desc_option, DbCourseMeasurement.online_cond);
                                cmr.cmr_status = cmr_status;
                                cmr.cmr_status_desc_option = desc_option;
                                cmr.cmr_is_contri_by_score = false;
                                cmr.cmr_create_timestamp = curTime;
                                cmr.cmr_create_usr_id = prof.usr_id;
                                cmr.cmr_upd_timestamp = curTime;
                                cmr.cmr_upd_usr_id = prof.usr_id;
                                cmr.updCond(con);
                                
                                DbCourseMeasurement cmt = new DbCourseMeasurement();
                                cmt.getByCmtId(con, cmt_id);
                                cmt.setCmt_title(title);
                                cmt.updCosOnlineCond(con);
                            } else if (res_id == 0) {
                                //linked but now removed
                                DbCourseMeasurement cmt = new DbCourseMeasurement();
                                cmt.setCmt_ccr_id(ccr_id);
                                cmt.setCmt_id(cmt_id);
                                cmt.setCmt_title(title);
                                cmt.setCmt_status_desc_option(desc_option);
                                cmt.setCmt_is_contri_by_score(false);
                                cmt.setCmt_statusByOption(desc_option,
                                        DbCourseMeasurement.online_cond);
                                cmt.setCmt_update_timestamp(curTime);
                                cmt.setCmt_update_usr_id(prof.usr_id);
                                cmt.updCosOnlineCond(con);
                                
                                DbCourseModuleCriteria.delByCmrId(con, cmr_id);
                            }
                        }
                    }
                    insert_id_list.add(insert_id);
                }
                
                //Scoring Item
                for (int i = 0; i < cmtScoringVec.size(); i++) {
                    Hashtable hashcm = (Hashtable) cmtScoringVec.get(i);
                    String desc_option;
                    long cmt_id, cmr_id;
                    desc_option = (String) hashcm.get("status_desc_option");
                    cmt_id = ((Long) hashcm.get("cmt_id")).longValue();
                    cmr_id = ((Long) hashcm.get("cmr_id")).longValue();
                    if ( cmt_id != 0 && cmr_id ==0 ) {
                        DbCourseMeasurement cmt = new DbCourseMeasurement();
                        cmt.setCmt_id(cmt_id);
                        cmt.setCmt_statusByOption(desc_option, DbCourseMeasurement.scoring_cond);
                        cmt.setCmt_status_desc_option(desc_option);
                        cmt.setCmt_update_timestamp(curTime);
                        cmt.setCmt_update_usr_id(prof.usr_id);
                        cmt.updCosScoringCond(con);
                    } else if(cmt_id != 0 && cmr_id != 0) {
                        DbCourseModuleCriteria cmr = new DbCourseModuleCriteria();
                        cmr.cmr_id = cmr_id;
                        cmr.cmr_status_desc_option = desc_option;
                        cmr.setCmr_statusByOption(desc_option, DbCourseMeasurement.scoring_cond);
                        cmr.cmr_upd_timestamp = curTime;
                        cmr.cmr_upd_usr_id = prof.usr_id;
                        cmr.updRunCond(con);
                    }
                }
            }
        }
        return insert_id_list;
    }


/* William Weng*/
	public static class CmtCmr{
		public long ccr_id;
	  	public long cmr_id;
	  	public String title;
	  	public long cmt_id;
	  	public long res_id;
	  	public String status_desc_option;
	  	public String status;
	  	public String subtype;
  }
    public Vector getOnlineModuleAsVt(Connection con,long itm_id)throws SQLException,cwSysMessage{
    	Vector vt = new Vector();
		DbCourseCriteria ccr = new DbCourseCriteria();
		DbCourseMeasurement cmt = new DbCourseMeasurement();
		ccr.ccr_itm_id = itm_id;
		ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
		ccr.getCcrIdByItmNType(con);
		ccr.get(con);
		ViewCourseModuleCriteria.ViewCmr[] viewCmr = ViewCourseModuleCriteria.getCmrLstByType(con, ccr.ccr_id, 0);
		Vector cmtVec = DbCourseMeasurement.getCmtLstByType(con, ccr.ccr_id, 0);
		for (int i = 0; i < viewCmr.length; i++){
			CmtCmr cc = new CmtCmr();
			cc.ccr_id = ccr.ccr_id;
			cc.cmt_id=viewCmr[i].cmt_id;
			cc.cmr_id =viewCmr[i].cmr_id;
			cc.res_id =viewCmr[i].cmr_res_id;
			cc.title =viewCmr[i].res_title;
			cc.status_desc_option = viewCmr[i].cmr_status_desc_option;
			cc.status = viewCmr[i].cmr_status;
			cc.subtype = viewCmr[i].res_subtype;
			vt.addElement(cc);
		}
		for (int i = 0; i < cmtVec.size(); i++) {
			CmtCmr cc = new CmtCmr();
			cmt = (DbCourseMeasurement) cmtVec.get(i);
			cc.ccr_id =ccr.ccr_id;
			cc.cmt_id = cmt.getCmt_id();
			cc.cmr_id =-1;
			cc.res_id=-1;
			cc.title =cmt.getCmt_title();
			cc.status_desc_option = cmt.getCmt_status_desc_option();
			cc.status = cmt.getCmt_status();
			vt.addElement(cc);	
		}
    	return vt;
    }
    public Vector getScoringItemAsVt(Connection con, long itm_id)throws SQLException,cwSysMessage{
		Vector vt = new Vector();
		DbCourseCriteria ccr = new DbCourseCriteria();
		DbCourseMeasurement cmt = new DbCourseMeasurement();
		ccr.ccr_itm_id = itm_id;
		ccr.ccr_type = DbCourseCriteria.TYPE_COMPLETION;
		ccr.getCcrIdByItmNType(con);
		ccr.get(con);
		ViewCourseModuleCriteria.ViewCmr[] viewCmr = ViewCourseModuleCriteria.getCmrLstByType(con, ccr.ccr_id, 1);
		Vector cmtVec = DbCourseMeasurement.getCmtLstByType(con, ccr.ccr_id, 1);
		for (int i = 0; i < viewCmr.length; i++){
					CmtCmr cc = new CmtCmr();
					cc.ccr_id = ccr.ccr_id;
					cc.cmt_id=viewCmr[i].cmt_id;
					cc.cmr_id =viewCmr[i].cmr_id;
					cc.res_id =viewCmr[i].cmr_res_id;
					cc.title =viewCmr[i].cmt_title;
					cc.status_desc_option = viewCmr[i].cmr_status_desc_option;
					cc.status = viewCmr[i].cmr_status;
					vt.addElement(cc);
				}
				for (int i = 0; i < cmtVec.size(); i++) {
					CmtCmr cc = new CmtCmr();
					cmt = (DbCourseMeasurement) cmtVec.get(i);
					cc.ccr_id =ccr.ccr_id;
					cc.cmt_id = cmt.getCmt_id();
					cc.cmr_id =-1;
					cc.res_id=-1;
					cc.title =cmt.getCmt_title();
					cc.status_desc_option = cmt.getCmt_status_desc_option();
					cc.status = cmt.getCmt_status();
					vt.addElement(cc);	
				}
		return vt;
    }
    
    public static String getTitleByCmtId(Connection con, long cmt_id) throws SQLException{
    	return DbCourseMeasurement.getCmtTitleByCmtId(con, cmt_id);
    }
}
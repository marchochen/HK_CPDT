package com.cw.wizbank.JsonMod.studyGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Integer;



import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.JsonMod.commonBean.GoldenManOptionBean;
import com.cw.wizbank.JsonMod.commonBean.JsonTreeBean;
import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupMgtBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupResBean;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.qdb.dbForum;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.studyGroup.StudyGroupDAO;
import com.cw.wizbank.studyGroup.StudyGroupDB;
import com.cw.wizbank.studyGroup.StudyGroupMemDB;
import com.cw.wizbank.studyGroup.StudyGroupResDB;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;
import com.cwn.wizbank.utils.CommonLog;

public class StudyGroup {
	
	public static final String FTN_STUDY_GROUP_MAIN="STUDY_GROUP_MAIN";
    private static final String MSG_TPL_JOIN_STUDYGROUP_NOTIFY = "JOIN_STUDYGROUP_REMINDER";
    private static final String MSG_TPL_JOIN_STUDYGROUP_NOTIFY_CMD = "GET_STUDYGROUP_REMINDER_XML";
    private static final String NAME[] = {"cmd", "ent_ids", "sender_id", "sgp_id"};
    private static final String TYPE[] = {"STATIC", "DYNAMIC", "STATIC", "STATIC"};

	/**
	 * 获取学习小组列表（分三部分显示）
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @param hasTc
	 * @throws SQLException
	 */
	public void getSgpLst(Connection con, loginProfile prof, StudyGroupModuleParam param, HashMap resultMap ,boolean hasTc)throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector joinVc =new Vector();
		Hashtable joinHs =new Hashtable();
		Vector mgtVc =new Vector();
		Hashtable mgtHs =new Hashtable();
		Vector otherVc =new Vector();
		String pageIdStr="";
		String notInIdStr="";
		HashMap joinMap=new HashMap();
		HashMap mgtMap=new HashMap();
		HashMap otherMap=new HashMap();

		mgtHs = sgpDAO.getMyMgtSgps(con, prof, param, mgtVc , hasTc);
		mgtMap.put("total", new Integer(param.getTotal_rec()));
		String allMgtId = (String)mgtHs.get("allIdStr");
		joinHs = sgpDAO.getMyJoinSgps(con, prof, param, joinVc ,hasTc, allMgtId);
		joinMap.put("total", new Integer(param.getTotal_rec()));
		if(joinHs.get("allIdStr")!=null){
			notInIdStr +=(String)joinHs.get("allIdStr");
		}
		if(notInIdStr.length()==0&&mgtHs.get("allIdStr")!=null){
			notInIdStr+=(String)mgtHs.get("allIdStr");
		}else if(notInIdStr.length()>0&&mgtHs.get("allIdStr")!=null){
			notInIdStr=notInIdStr+","+(String)mgtHs.get("allIdStr");
		}else if(notInIdStr.length()==0 &&mgtHs.get("allIdStr")==null){
			notInIdStr="0";	
		}
		String otherIdStr=sgpDAO.getOtherSgps(con, prof, param, otherVc, notInIdStr ,hasTc);
		otherMap.put("total", new Integer(param.getTotal_rec()));
		if(joinHs != null&& joinHs.get("idStr")!=null){
			pageIdStr=pageIdStr+(String)joinHs.get("idStr");
		}
		if(mgtHs !=null && mgtHs.get("idStr")!=null){
			if(pageIdStr.length()>0){
				pageIdStr=pageIdStr+","+(String)mgtHs.get("idStr");
			}else{
				pageIdStr=(String)mgtHs.get("idStr");
			}
		}
		if(otherIdStr.length()>0){
			if(pageIdStr.length()>0){
				pageIdStr=pageIdStr+","+otherIdStr;				
			}else{
				pageIdStr=otherIdStr;	
			}
		}
		
		Hashtable memHs =new Hashtable();
		Hashtable resHs =new Hashtable();
		Hashtable topicHs =new Hashtable();
		Hashtable mgtMailHs =new Hashtable();
		if(pageIdStr.length()>0){
			sgpDAO.getStudyGroupMemAndMgtNum(con, pageIdStr, memHs);//(con, prof, pageIdStr, memHs);
			sgpDAO.getResCntHs(con, prof, pageIdStr, resHs);
			sgpDAO.getTopicCntHs(con, prof, pageIdStr, topicHs);	
		}
		if(otherIdStr !=null && otherIdStr.length()>0){
			sgpDAO.getSgpTaEmailHs(con, otherIdStr, mgtMailHs);/*getMgtEmailHs(con, otherIdStr, mgtMailHs)*/;
		}
		setMemMgtTopicCutAndMail(con, prof, param, joinVc, memHs, resHs, topicHs, mgtMailHs);
		setMemMgtTopicCutAndMail(con, prof, param, mgtVc, memHs, resHs, topicHs, mgtMailHs);
		setMemMgtTopicCutAndMail(con, prof, param, otherVc, memHs, resHs, topicHs, mgtMailHs);
		
		joinMap.put("sgp_lst", joinVc);
		mgtMap.put("sgp_lst", mgtVc);
		otherMap.put("sgp_lst", otherVc);
	
		resultMap.put("join_sgp", joinMap);
		resultMap.put("mgt_sgp", mgtMap);
		resultMap.put("pub_sgp", otherMap);

	}
	
	/**
	 * 获取我参加的学习小组的列表
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @param hasTc
	 * @throws SQLException
	 */
	public void getJoinSgpLst(Connection con, loginProfile prof, StudyGroupModuleParam param, HashMap resultMap ,boolean hasTc)throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector joinVc =new Vector();
		Hashtable joinHs =new Hashtable();		
		String pageIdStr="";
		HashMap joinMap=new HashMap();
		if(hasTc){
			if(param.getTcr_id()==0){
				long tcr_id= DbTrainingCenter.getSuperTcId(con, 1);
				param.setTcr_id(tcr_id);
			}
		}
		Hashtable mgtHs = new Hashtable();
		Vector mgtVc = new Vector();
		mgtHs = sgpDAO.getMyMgtSgps(con, prof, param, mgtVc , hasTc);
		String allMgtId = (String)mgtHs.get("allIdStr");
		joinHs = sgpDAO.getMyJoinSgps(con, prof, param, joinVc ,hasTc, allMgtId);
		joinMap.put("total", new Integer(param.getTotal_rec()));
		if(joinHs != null&& joinHs.get("idStr")!=null)
			pageIdStr=pageIdStr+(String)joinHs.get("idStr");
		Hashtable memHs =new Hashtable();
		Hashtable resHs =new Hashtable();
		Hashtable topicHs =new Hashtable();
		Hashtable mgtMailHs =new Hashtable();
		if(pageIdStr.length()>0){
			sgpDAO.getStudyGroupMemAndMgtNum(con, pageIdStr, memHs);//getMemCntHs(con, prof, pageIdStr, memHs);
			sgpDAO.getResCntHs(con, prof, pageIdStr, resHs);
			sgpDAO.getTopicCntHs(con, prof, pageIdStr, topicHs);	
		}
		setMemMgtTopicCutAndMail(con, prof, param, joinVc, memHs, resHs, topicHs, mgtMailHs);	
		joinMap.put("sgp_lst", joinVc);
		resultMap.put("join_sgp", joinMap);
	}
	
	/**
	 * 获取我管理的学习小组的列表
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @param hasTc
	 * @throws SQLException
	 */
	public void getMgtSgpLst(Connection con, loginProfile prof, StudyGroupModuleParam param, HashMap resultMap ,boolean hasTc)throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector mgtVc =new Vector();
		Hashtable mgtHs =new Hashtable();
		String pageIdStr="";
		HashMap mgtMap=new HashMap();
		if(hasTc){
			if(param.getTcr_id()==0){
				long tcr_id= DbTrainingCenter.getSuperTcId(con, 1);
				param.setTcr_id(tcr_id);
			}
		}
		mgtHs = sgpDAO.getMyMgtSgps(con, prof, param, mgtVc , hasTc);
		mgtMap.put("total", new Integer(param.getTotal_rec()));
		if(mgtHs !=null && mgtHs.get("idStr")!=null)
			pageIdStr=(String)mgtHs.get("idStr");
		Hashtable memHs =new Hashtable();
		Hashtable resHs =new Hashtable();
		Hashtable topicHs =new Hashtable();
		Hashtable mgtMailHs =new Hashtable();
		if(pageIdStr.length()>0){
			sgpDAO.getStudyGroupMemAndMgtNum(con, pageIdStr, memHs);//getMemCntHs(con, prof, pageIdStr, memHs);
			sgpDAO.getResCntHs(con, prof, pageIdStr, resHs);
			sgpDAO.getTopicCntHs(con, prof, pageIdStr, topicHs);	
		}
		setMemMgtTopicCutAndMail(con, prof, param, mgtVc, memHs, resHs, topicHs, mgtMailHs);
		mgtMap.put("sgp_lst", mgtVc);	
		resultMap.put("mgt_sgp", mgtMap);

	}
	
	/**
	 * 获取其他学习小组的列表
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @param hasTc
	 * @throws SQLException
	 */
	public void getOtherSgpLst(Connection con, loginProfile prof, StudyGroupModuleParam param, HashMap resultMap ,boolean hasTc)throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector joinVc =new Vector();
		Hashtable joinHs =new Hashtable();
		Vector mgtVc =new Vector();
		Hashtable mgtHs =new Hashtable();
		Vector otherVc =new Vector();
		String pageIdStr="";
		String notInIdStr="";
		HashMap otherMap=new HashMap();
		if(hasTc){
			if(param.getTcr_id()==0){
				long tcr_id= DbTrainingCenter.getSuperTcId(con, 1);
				param.setTcr_id(tcr_id);
			}
		}
		mgtHs = sgpDAO.getMyMgtSgps(con, prof, param, mgtVc , hasTc);
		String allMgtId = (String)mgtHs.get("allIdStr");
		joinHs = sgpDAO.getMyJoinSgps(con, prof, param, joinVc ,hasTc,allMgtId);
		
		if(joinHs.get("allIdStr")!=null){
			notInIdStr +=(String)joinHs.get("allIdStr");
		}
		if(notInIdStr.length()==0&&mgtHs.get("allIdStr")!=null){
			notInIdStr+=(String)mgtHs.get("allIdStr");
		}else if(notInIdStr.length()>0&&mgtHs.get("allIdStr")!=null){
			notInIdStr=notInIdStr+","+(String)mgtHs.get("allIdStr");
		}else if(notInIdStr.length()==0 &&mgtHs.get("allIdStr")==null){
			notInIdStr="0";	
		}
	
		String otherIdStr=sgpDAO.getOtherSgps(con, prof, param, otherVc, notInIdStr ,hasTc);
		otherMap.put("total", new Integer(param.getTotal_rec()));
		if(otherIdStr.length()>0)
			pageIdStr=otherIdStr;
		
		Hashtable memHs =new Hashtable();
		Hashtable resHs =new Hashtable();
		Hashtable topicHs =new Hashtable();
		Hashtable mgtMailHs =new Hashtable();
		if(pageIdStr.length()>0){
			sgpDAO.getStudyGroupMemAndMgtNum(con, pageIdStr, memHs);//getMemCntHs(con, prof, pageIdStr, memHs);
			sgpDAO.getResCntHs(con, prof, pageIdStr, resHs);
			sgpDAO.getTopicCntHs(con, prof, pageIdStr, topicHs);	
		}
		if(otherIdStr !=null && otherIdStr.length()>0){
			sgpDAO.getSgpTaEmailHs(con, otherIdStr, mgtMailHs);/*getMgtEmailHs(con, otherIdStr, mgtMailHs)*/;
		}
		setMemMgtTopicCutAndMail(con, prof, param, otherVc, memHs, resHs, topicHs, mgtMailHs);		
		otherMap.put("sgp_lst", otherVc);
		resultMap.put("pub_sgp", otherMap);

	}
	
	/**
	 * 学习小组的统计信息
	 * @param con
	 * @param prof
	 * @param param
	 * @param sgrVc
	 * @param memHs
	 * @param resHs
	 * @param topicHs
	 * @param mgtMailHs
	 * @throws SQLException
	 */	
	public void setMemMgtTopicCutAndMail(Connection con, loginProfile prof, StudyGroupModuleParam param, Vector sgrVc,
											Hashtable memHs , Hashtable resHs , Hashtable topicHs ,Hashtable mgtMailHs)throws SQLException{
	
		if(sgrVc !=null && sgrVc.size()>0){
			Iterator iter=sgrVc.iterator();
			while(iter.hasNext()){
				StudyGroupBean sgrp=(StudyGroupBean)iter.next();
				Long tempSgpId = new Long(sgrp.getSgp_id());
				if(memHs != null &&memHs.containsKey(tempSgpId)){
					Integer tempCut=(Integer)memHs.get(tempSgpId);
					sgrp.setSgm_member_total(tempCut.intValue());
				}
				if(resHs != null &&resHs.containsKey(tempSgpId)){
					Integer tempCut=(Integer)resHs.get(tempSgpId);
					sgrp.setSgr_res_total(tempCut.intValue());
				}
				if(topicHs != null &&topicHs.containsKey(tempSgpId)){
					Integer tempCut=(Integer)topicHs.get(tempSgpId);
					sgrp.setSgr_topic_total(tempCut.intValue());
				}
				if(mgtMailHs !=null && !mgtMailHs.isEmpty()&&mgtMailHs.containsKey(tempSgpId)){
					Hashtable mgtHs= (Hashtable)mgtMailHs.get(tempSgpId);
					Enumeration Enum=mgtHs.elements();
					Vector mgtVc= new Vector();
					while(Enum.hasMoreElements()){
						StudyGroupMgtBean mgt =(StudyGroupMgtBean)Enum.nextElement();
						mgtVc.add(mgt);
					}
					sgrp.setMgtVc(mgtVc);
				}
			
			}
		}
	}
	
	/**
	 * 搜索学习小组
	 * @param con
	 * @param prof
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Vector searchSgpResult(Connection con, loginProfile prof, StudyGroupModuleParam param,boolean hasTc) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector sgpVc =new Vector();
		Hashtable sgpStatusHt= new Hashtable();
		String sgpIdStr=sgpDAO.searchStudyGroup(con, prof, param, sgpVc,hasTc);
		Hashtable mailHs= new Hashtable();
		if(sgpIdStr!=null &&sgpIdStr.length()>0){
			sgpDAO.getStudyGroupStatus(con, prof, sgpIdStr, sgpStatusHt);
			sgpDAO.getSgpTaEmailHs(con, sgpIdStr, mailHs);/*getMgtEmailHs(con, sgpIdStr, mailHs);*/
		}
		if(!sgpVc.isEmpty()){
			Iterator iter=sgpVc.iterator();
			while(iter.hasNext()){
				StudyGroupBean sgp=(StudyGroupBean)iter.next();
				Long idObj=new Long(sgp.getSgp_id());
				if(sgpStatusHt.containsKey(idObj)){				
					sgp.setCan_view(true);
				}else{
					sgp.setCan_view(false);
				}
				if(mailHs.containsKey(idObj)){
					Hashtable mgtHs= (Hashtable)mailHs.get(idObj);
					Enumeration Enum=mgtHs.elements();
					Vector mgtVc= new Vector();
					while(Enum.hasMoreElements()){
						StudyGroupMgtBean mgt =(StudyGroupMgtBean)Enum.nextElement();
						mgtVc.add(mgt);
					}
					sgp.setMgtVc(mgtVc);
				}
			}
		}
		return sgpVc;
	}
	
	/**
	 * 判断是否最顶层的培训中心
	 * @param param
	 * @param tc_lst
	 * @return
	 */
	public boolean isTopTC(StudyGroupModuleParam param, Vector tc_lst){
		boolean isTop= false;
		if(param.getTcr_id()==0){
			isTop=true;
		}else{
			if(tc_lst !=null &&tc_lst.size()>0){
				Iterator iter=tc_lst.iterator();
				while(iter.hasNext()){
					TCBean tc= (TCBean)iter.next();
					if(tc.getTcr_id()==param.getTcr_id() && tc.getTcr_parent_tcr_id()==0){				
						isTop=true;
					}
				}
			}
		}
		return isTop;
	}
	
	/**
	 * 获取学习中心的信息
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @throws SQLException
	 */
	public void getSgpInfo(Connection con, loginProfile prof, StudyGroupModuleParam param, HashMap resultMap,WizbiniLoader wizbini,StudyGroupBean sgp) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		//StudyGroupBean sgp=sgpDAO.getStudyGroupDetail(con, param.getSgp_id());
		resultMap.put("sgp_info", sgp);
		boolean isMgt=sgpDAO.isCurUsrMgt(con, prof, param.getSgp_id());
		HashMap isMgtMap= new HashMap();
		isMgtMap.put("is_manager", new Boolean (isMgt));
		if(prof.current_role.startsWith("TADM")){
			isMgtMap.put("is_Ta", new Boolean(true));
		}else{
			isMgtMap.put("is_Ta", new Boolean(false));
		}
			
		resultMap.put("cur_usr", isMgtMap);
		//
		Vector resVc =sgpDAO.getStudyGroupResLst(con, prof, param,wizbini);
		HashMap resMap = new HashMap();
		resMap.put("total", new Integer(param.getTotal_rec()));
		resMap.put("res_lst", resVc);
		resultMap.put("sgp_res", resMap);
		//
		HashMap topicMap =new HashMap();
		sgpDAO.getSgpForumTopic(con, param.getSgp_id(),topicMap);
		resultMap.put("discuss",topicMap);
		//后台接收的limit 默认值10,而成员列表里默认是5,为了不造成冲突,这部分的代码放最后执行
		param.setLimit(5);
		Vector memVc = sgpDAO.getStudyGroupMemLst(con, prof,param,wizbini);
		HashMap usrMap = new HashMap();
		usrMap.put("total", new Integer(param.getTotal_rec()));
		usrMap.put("usr_lst", memVc);
		resultMap.put("sgp_usr", usrMap);
	}
	
	/**
	 * 修改学习中心的资料
	 * @param con
	 * @param param
	 * @throws SQLException
	 */
	public void updSgpInfo(Connection con, StudyGroupModuleParam param) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupDB sgp = new StudyGroupDB();
		sgp.sgp_title=param.getSgp_title();
		sgp.sgp_desc = param.getSgp_desc();
		sgp.sgp_public_type = param.getSgp_public_type();
		sgp.sgp_id = param.getSgp_id();	
		sgp.sgp_tcr_id = param.getTcr_id();
		sgpDAO.updStudyGroupInfo(con, sgp);
	}
	
	/**
	 * 添加资源
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws qdbException 
	 * @throws IOException 
	 */
	public void addSgpRes(Connection con, loginProfile prof,StudyGroupModuleParam param, String fileName,String tempfile,WizbiniLoader wizbini) throws SQLException, qdbException, IOException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupResDB res =new StudyGroupResDB();
		Timestamp cur_time = cwSQL.getTime(con);
		res.sgs_desc = param.getSgs_desc();
		res.sgs_title = param.getSgs_title();
		res.sgs_type = param.getSgs_type();
		if(StudyGroupDAO.SGS_TYPE_DOC.equalsIgnoreCase(param.getSgs_type())){
			res.sgs_content =fileName;
		}else{
			res.sgs_content = param.getSgs_content();
		}
		res.sgs_create_usr_id = prof.usr_ste_usr_id;
		res.sgs_create_timestamp = cur_time;
		res.sgs_upd_usr_id = prof.usr_ste_usr_id;
		res.sgs_upd_timestamp = cur_time;
		sgpDAO.addStudyGroupRes(con, res,param.getSgp_id());
		if(res.sgs_id>0 &&StudyGroupDAO.SGS_TYPE_DOC.equalsIgnoreCase(param.getSgs_type())){
			File sourceFile =new File(tempfile);
			String dirStr=wizbini.getFileUploadSgpResDisAbs()+  cwUtils.SLASH  +res.sgs_id;
			File dir=new File(dirStr);
			if(!dir.exists()){
				dir.mkdirs();
			}
			UploadUtils.fileSaveAs(sourceFile, dir);
			if(sourceFile.exists()){
				File parentFile=sourceFile.getParentFile();
				sourceFile.delete();
				parentFile.delete();
			}
		}

	}
	
	/**
	 * 修改资源
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws IOException 
	 */
	public void updSgpRes(Connection con, loginProfile prof,StudyGroupModuleParam param, String fileName,String tempfile,WizbiniLoader wizbini) throws SQLException, IOException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupResDB res =new StudyGroupResDB();
		StudyGroupResBean oldRes=sgpDAO.getStudyGroupRes(con, param.getSgs_id());
		Timestamp cur_time = cwSQL.getTime(con);
		res.sgs_id =param.getSgs_id();		
		if(StudyGroupDAO.SGS_TYPE_DOC.equalsIgnoreCase(param.getSgs_type())){
			if(param.isIs_keep_doc()){
				res.sgs_content=""; 
			}else{
				res.sgs_content =fileName;
			}
		}else{
			res.sgs_content = param.getSgs_content();
		}
		res.sgs_desc = param.getSgs_desc();
		res.sgs_title = param.getSgs_title();
		res.sgs_type = param.getSgs_type();
		res.sgs_upd_usr_id = prof.usr_ste_usr_id;
		res.sgs_upd_timestamp = cur_time;
		sgpDAO.updStudyGroupRes(con, res);
		if(res.sgs_id>0 &&StudyGroupDAO.SGS_TYPE_DOC.equalsIgnoreCase(param.getSgs_type()) && !param.isIs_keep_doc()){
			File sourceFile =new File(tempfile);
			String dirStr=wizbini.getFileUploadSgpResDisAbs()+"\\"+res.sgs_id;
			File dir=new File(dirStr);
			UploadUtils.fileSaveAs(sourceFile, dir);
			if(sourceFile.exists()){
				File parentFile=sourceFile.getParentFile();
				sourceFile.delete();
				parentFile.delete();
			}
		}
		if(res.sgs_id>0 &&StudyGroupDAO.SGS_TYPE_DOC.equalsIgnoreCase(oldRes.getSgs_type()) && !param.isIs_keep_doc()){
			String dirStr=wizbini.getFileUploadSgpResDisAbs()+"\\"+res.sgs_id+"\\"+oldRes.getSgs_content();
			File oldFile =new File(dirStr);
			if(oldFile.exists()){
				oldFile.delete();	
			}
		}
	}
	
	/**
	 * 删除资源
	 * @param con
	 * @param param
	 * @throws SQLException
	 */
	public void delSgpRes(Connection con, StudyGroupModuleParam param, WizbiniLoader wizbini) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupResDB res =new StudyGroupResDB();
		StudyGroupResBean oldRes=sgpDAO.getStudyGroupRes(con, param.getSgs_id());
		res.sgs_id = param.getSgs_id();
		res.sgs_type=StudyGroupDAO.SGR_TYPE_SGP_RESOURCE;
		sgpDAO.delStudyGroupRes(con, res);
		if(res.sgs_id>0 &&StudyGroupDAO.SGS_TYPE_DOC.equalsIgnoreCase(oldRes.getSgs_type())){
			String dirStr=wizbini.getFileUploadSgpResDisAbs()+"\\"+res.sgs_id+"\\"+oldRes.getSgs_content();
			File oldFile =new File(dirStr);
			if(oldFile.exists()){
				File parentFile=oldFile.getParentFile();
				oldFile.delete();
				parentFile.delete();
			}
		}
	}
	
	/**
	 * 通过手动方式添加成员
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws cwSysMessage 
	 * @throws cwException 
	 */
	public void addCustSgpMem(Connection con, loginProfile prof,StudyGroupModuleParam param) throws SQLException, cwException, cwSysMessage{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupMemDB mem = new StudyGroupMemDB();
		Timestamp cur_time = cwSQL.getTime(con);
		Vector idVc =new Vector();
		if(param.getSgm_ent_id_usr() !=null &&param.getSgm_ent_id_usr().length()>0){
			idVc=cwUtils.splitToVecString(param.getSgm_ent_id_usr(), "~");
		}
		StudyGroupMemDB oldMem = new StudyGroupMemDB();
		oldMem.sgm_sgp_id=param.getSgp_id();
		oldMem.sgm_type= StudyGroupDAO.SGM_TYPE_USR;
		Vector oldEntIDVec = sgpDAO.getSgmEntID(con, oldMem);
		sgpDAO.delStudyGroupMem(con, oldMem);
		if(!idVc.isEmpty()){
			Iterator iter= idVc.iterator();
			while(iter.hasNext()){
				String idStr=(String)iter.next();
				mem.sgm_sgp_id = param.getSgp_id();			
				mem.sgm_ent_id =Long.parseLong(idStr);
				mem.sgm_type =StudyGroupDAO.SGM_TYPE_USR;
				mem.sgm_status="ADMITTED";
				mem.sgm_create_timestamp=cur_time;
				mem.sgm_create_usr_id =prof.usr_ste_usr_id;
				mem.sgm_upd_timestamp =cur_time;
				mem.sgm_upd_usr_id = prof.usr_ste_usr_id;
				sgpDAO.insStudyGroupMem(con, mem);
			}
		}
		if(oldEntIDVec.size() > 0){
			for(int i=0; i<oldEntIDVec.size(); i++){
				if(idVc.contains(oldEntIDVec.get(i))){
					idVc.remove(oldEntIDVec.get(i));
				}
			}
		}
		if(idVc.size() > 0 && StudyGroupDAO.isSendEmail(con, param.getSgp_id())){
			insNotify(con, prof, param.getSgp_id(), cwUtils.stringArray2LongArray(cwUtils.vec2strArray(idVc)));
			CommonLog.info("insert study group notify successfull. study group notify type = " + mem.sgm_type);
		}
	}
	
	/**
	 * 通过用户组方式添加成员
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws cwSysMessage 
	 * @throws cwException 
	 */
	public void addUsgSgpMem(Connection con, loginProfile prof,StudyGroupModuleParam param) throws SQLException, cwException, cwSysMessage{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Timestamp cur_time = cwSQL.getTime(con);
		Vector idVc =new Vector();
		if(param.getSgm_ent_id_usg() !=null &&param.getSgm_ent_id_usg().length()>0){
			idVc=cwUtils.splitToVecString(param.getSgm_ent_id_usg(), "~");
		}
		StudyGroupMemDB oldMem = new StudyGroupMemDB();
		oldMem.sgm_sgp_id=param.getSgp_id();
		oldMem.sgm_type= StudyGroupDAO.SGM_TYPE_USG;
		Vector oldEntIDVec = sgpDAO.getSgmEntID(con, oldMem);
		sgpDAO.delStudyGroupMem(con, oldMem);
		if(!idVc.isEmpty()){
			Iterator iter= idVc.iterator();
			while(iter.hasNext()){
				StudyGroupMemDB mem = new StudyGroupMemDB();
				String idStr=(String)iter.next();
				mem.sgm_sgp_id = param.getSgp_id();			
				mem.sgm_ent_id =Long.parseLong(idStr);
				mem.sgm_type =StudyGroupDAO.SGM_TYPE_USG;
				mem.sgm_status="ADMITTED";
				mem.sgm_create_timestamp=cur_time;
				mem.sgm_create_usr_id =prof.usr_ste_usr_id;
				mem.sgm_upd_timestamp =cur_time;
				mem.sgm_upd_usr_id = prof.usr_ste_usr_id;
				sgpDAO.insStudyGroupMem(con, mem);
			}
		}
		if(oldEntIDVec.size() > 0){
			for(int i=0; i<oldEntIDVec.size(); i++){
				if(idVc.contains(oldEntIDVec.get(i))){
					idVc.remove(oldEntIDVec.get(i));
				}
			}
		}
		if(idVc.size() > 0 && StudyGroupDAO.isSendEmail(con, param.getSgp_id())){
			insNotify(con, prof, param.getSgp_id(), cwUtils.stringArray2LongArray(cwUtils.vec2strArray(idVc)));
			CommonLog.info("insert study group notify successfull. study group notify type = " + StudyGroupDAO.SGM_TYPE_USG);
		}
	}
	
	/**
	 * 通过课程或班级方式添加成员
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws cwException 
	 * @throws NumberFormatException 
	 * @throws cwSysMessage 
	 */
	public void addItmSgpMem(Connection con, loginProfile prof,StudyGroupModuleParam param) throws SQLException, NumberFormatException, cwException, cwSysMessage{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupMemDB mem = new StudyGroupMemDB();
		Timestamp cur_time = cwSQL.getTime(con);
		Vector idVc =new Vector();
		if(param.getSgm_ent_id_itm() !=null &&param.getSgm_ent_id_itm().length()>0){
			idVc=cwUtils.splitToVecString(param.getSgm_ent_id_itm(), "~");
		}
		StudyGroupMemDB oldMem = new StudyGroupMemDB();
		oldMem.sgm_sgp_id=param.getSgp_id();
		oldMem.sgm_type= StudyGroupDAO.SGM_TYPE_ITM;
		Vector oldEntIDVec = sgpDAO.getSgmEntID(con, oldMem);
		sgpDAO.delStudyGroupMem(con, oldMem);
		if(!idVc.isEmpty()){
			Iterator iter= idVc.iterator();
			while(iter.hasNext()){
				String idStr=(String)iter.next();
				mem.sgm_sgp_id = param.getSgp_id();			
				mem.sgm_ent_id =Long.parseLong(idStr);
				mem.sgm_type =StudyGroupDAO.SGM_TYPE_ITM;
				mem.sgm_status="ADMITTED";
				mem.sgm_create_timestamp=cur_time;
				mem.sgm_create_usr_id =prof.usr_ste_usr_id;
				mem.sgm_upd_timestamp =cur_time;
				mem.sgm_upd_usr_id = prof.usr_ste_usr_id;
				sgpDAO.insStudyGroupMem(con, mem);
			}
		}
		if(oldEntIDVec.size() > 0){
			for(int i=0; i<oldEntIDVec.size(); i++){
				if(idVc.contains(oldEntIDVec.get(i))){
					idVc.remove(oldEntIDVec.get(i));
				}
			}
		}
		if(!idVc.isEmpty() && StudyGroupDAO.isSendEmail(con, param.getSgp_id())){
			Vector ent_ids = new Vector();
            String[] status = { "Admitted" };
			Iterator iter= idVc.iterator();
			while(iter.hasNext()){
				String idStr=(String)iter.next();
				long[] ent_id_array = aeApplication.getEnrollUser(con, Long.parseLong(idStr), status);
				ent_ids = cwUtils.unionVectors(ent_ids, cwUtils.long2vector(ent_id_array), false);
			}
			
			if(ent_ids.size() > 0){
				insNotify(con, prof, param.getSgp_id(), cwUtils.vec2longArray(ent_ids));
				CommonLog.info("insert study group notify successfull. study group notify type = " + StudyGroupDAO.SGM_TYPE_ITM);
			}
		}
	}
	
	/**
	 * 删除学习小组成员
	 * @param con
	 * @param param
	 * @throws SQLException
	 */
	public void delSgpMem(Connection con, StudyGroupModuleParam param) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupMemDB mem = new StudyGroupMemDB();
		mem.sgm_id=param.getSgm_id();
		mem.sgm_type =param.getSgs_type();
		sgpDAO.delStudyGroupMem(con, mem);
	}
	
	/**
	 * 获取通过手动方式添加的成员列表
	 * @param con
	 * @param param
	 * @param resultMap
	 * @throws SQLException
	 */
	public void getCustSgpMem(Connection con, StudyGroupModuleParam param, HashMap resultMap) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector memVc = sgpDAO.getCustAddMem(con, param);
		Vector gldVc = new Vector(); 
		GoldenManOptionBean gld =new GoldenManOptionBean();
		gld.setName("goldenman_myteamdetails_user");
		gld.setValue(memVc);
		gldVc.add(gld);
		cwXSL.outPutGoldManOption(resultMap, gldVc);
	}
	/**
	 * 获取通过自动方式添加的成员列表（两部分）
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @throws SQLException
	 */
	public void getAutoSgpMem(Connection con, loginProfile prof, StudyGroupModuleParam param, HashMap resultMap) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector usgVc = new Vector();
		Vector itmVc = new Vector();
		Vector gldVc = new Vector(); 
		usgVc= sgpDAO.getStudyGroupMemGroup(con, param);
		GoldenManOptionBean gld =new GoldenManOptionBean();
		gld.setName("goldenman_myteamdetails_group");
		gld.setValue(usgVc);
		gldVc.add(gld);
		if(prof.current_role.startsWith("TADM")){
			itmVc= sgpDAO.getStudyGroupMemItm(con, param);
			GoldenManOptionBean gldItm =new GoldenManOptionBean();
			gldItm.setName("goldenman_myteamdetails_item");
			gldItm.setValue(itmVc);
			gldVc.add(gldItm);
			//resultMap.put("itm_lst", itmVc);
		}
		cwXSL.outPutGoldManOption(resultMap, gldVc);
		
	}

	
	/**
	 * 添加学习小组
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void insSgp(Connection con, loginProfile prof, StudyGroupModuleParam param) throws SQLException, qdbException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupDB sgp= new StudyGroupDB();
		dbModule mod= new dbModule();
		Vector mgtVc=cwUtils.splitToVecString(param.getSgp_mgt_str(), "~");
		if(mgtVc!=null &&!mgtVc.isEmpty()){
			mod.mod_instructor_ent_id_lst =new long[mgtVc.size()] ;
			for(int i=0; i< mgtVc.size(); i++){
				String idStr=(String)mgtVc.elementAt(i);
				long ent_id=Long.parseLong(idStr);
				mod.mod_instructor_ent_id_lst[i]=ent_id;
			}
		}
		sgp.sgp_tcr_id = param.getTcr_id();
		sgp.sgp_title = param.getSgp_title();
		sgp.sgp_desc = param.getSgp_desc();
		sgp.sgp_public_type =param.getSgp_public_type();
		sgp.sgp_send_email_ind = param.getSgp_send_email_ind();
		
		long sgp_id=sgpDAO.insStudyGroup(con, prof, sgp, mgtVc);
		mod.mod_type=dbModule.MOD_TYPE_FOR;
		mod.res_type=dbModule.MOD_TYPE_FOR;
		mod.res_subtype=dbResource.RES_SUBTYPE_DIS;
		mod.res_title=sgp.sgp_title;
		mod.mod_is_public=true;
		mod.ins(con, prof);
		mod.setSgpDisMod(con,  mod.res_id);
		sgpDAO.insForumSgpRelation(con, prof, sgp_id, mod.res_id);
	}
	
	
	/**
	 * 学习小组的管理员页面xml
	 * @param con
	 * @param prof
	 * @param param
	 * @param noTc
	 * @return
	 * @throws SQLException
	 * @throws qdbException
	 */
	public String getMyMgtSgpXml(Connection con, loginProfile prof, StudyGroupModuleParam param , boolean noTc) throws SQLException, qdbException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		Vector mgtVc =new Vector();
		Hashtable mgtHs =new Hashtable();
		String pageIdStr="";
		HashMap mgtMap=new HashMap();
		boolean show_all = false;
		//xsl分页
		if (param.getSortCol()!= null && param.getSortCol().length() > 0) {
            param.setSort(param.getSortCol());
        }else{
        	param.setSort("sgp_upd_timestamp");
        }
        if (param.getSortOrder() != null && param.getSortOrder().length() > 0) {
            param.setDir(param.getSortOrder());
        }else{
        	 param.setDir("DESC");
        }
        if(param.getPage_size()==0){
        	param.setPage_size(10);
        }
        if(param.getCur_page()==0){
        	param.setCur_page(1);
        }
        param.setStart(param.getPage_size() * (param.getCur_page()-1));
        param.setLimit(param.getPage_size());
        List tcrLst = null;
        if(param.getTcr_id()==-1){
	    	ViewTrainingCenter tcView= new ViewTrainingCenter();
	    	tcrLst=tcView.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, false);
	    	DbTrainingCenter tcr=(DbTrainingCenter)tcrLst.get(0);
        	param.setTcr_id(tcr.getTcr_id());
        }else if(param.getTcr_id()==0){
        	show_all = true;
        	ViewTrainingCenter tcView= new ViewTrainingCenter();
	    	tcrLst=tcView.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, true);
	    	StringBuffer strBuf =new StringBuffer();
	    	strBuf.append("(");
	    	for(int i=0; i<tcrLst.size();i++){
	    		DbTrainingCenter tcr=(DbTrainingCenter)tcrLst.get(i);
	    		if(i!=0){
	    			strBuf.append(",");
	    		}
	    		strBuf.append(tcr.getTcr_id());
	    	}
	    	strBuf.append(")");
        	param.setTcr_id_lst(strBuf.toString());
        }
		mgtHs = sgpDAO.getTaMgtSgps(con, prof, param, mgtVc , noTc);
		mgtMap.put("total", new Integer(param.getTotal_rec()));
		if(mgtHs !=null && mgtHs.get("idStr")!=null)
			pageIdStr=(String)mgtHs.get("idStr");
		Hashtable memHs =new Hashtable();
		Hashtable mgtMailHs =new Hashtable();
		
		if(pageIdStr !=null && pageIdStr.length()>0){
			sgpDAO.getStudyGroupMemAndMgtNum(con, pageIdStr, memHs);
			sgpDAO.getMgtEmailHs(con, pageIdStr, mgtMailHs);
			setMemMgtTopicCutAndMail(con, prof, param, mgtVc, memHs, null, null, mgtMailHs);
		}

		StringBuffer xmlBuf= new StringBuffer();
		xmlBuf.append("<sgp_list" + (show_all ? " show_all=\"true\">" : ">"));
		if(mgtVc !=null &&!mgtVc.isEmpty()){
			Iterator iter= mgtVc.iterator();
			while(iter.hasNext()){
				StudyGroupBean sgp = (StudyGroupBean)iter.next();
				StringBuffer mgtBuff=new StringBuffer();
				Vector mgtLst=sgp.getMgtVc();
				if(mgtLst !=null &&!mgtLst.isEmpty()){
					for(int i=0; i<mgtLst.size(); i++){
						StudyGroupMgtBean mgt= (StudyGroupMgtBean)mgtLst.elementAt(i);
						mgtBuff.append(mgt.getUsr_display_bil()).append(",");
					}	
					mgtBuff.deleteCharAt(mgtBuff.length()-1);
				}
				xmlBuf.append("<sgp id =\"").append(sgp.getSgp_id()).append("\"")
				     .append(" title=\"").append(cwUtils.esc4XML(sgp.getSgp_title())).append("\"")
				     .append(" public_type=\"").append(sgp.getSgp_public_type()).append("\"")
				     .append(" tcr_id=\"").append(sgp.getSgp_tcr_id()).append("\"")
				     .append(" upd_timestamp=\"").append(sgp.getSgp_upd_timestamp()).append("\"")
				     .append(" member_count=\"").append(memHs.get(new Long(sgp.getSgp_id()))).append("\"")
				     .append(" manager_lst=\"").append(cwUtils.esc4XML(mgtBuff.toString())).append("\"/>");					
			}
		}

		cwPagination pagn = new cwPagination();
        pagn.totalRec = param.getTotal_rec();
        pagn.totalPage = (int)Math.ceil((float) param.getTotal_rec()/param.getLimit());
        pagn.pageSize = param.getLimit();
        pagn.curPage =  param.getCur_page();
        pagn.sortCol = cwPagination.esc4SortSql(param.getSort());
        pagn.sortOrder = cwPagination.esc4SortSql(param.getDir());
        pagn.ts = null;
        xmlBuf.append(pagn.asXML());
      if(!noTc){
    	  long  tcr_id=0;
    	  if(param.getTcr_id()==0){
    		  tcr_id=DbTrainingCenter.getSuperTcId(con, 1);
    	  }else{
    		  tcr_id=param.getTcr_id();    		  
    	  }
    	  xmlBuf.append(TrainingCenter.getTcrAsXml(con,tcr_id, prof.root_ent_id));    	  
      }     
		xmlBuf.append("</sgp_list>");
		if (!noTc && show_all) {
			xmlBuf.append("<tcr_list>");
			for(int i=0; i<tcrLst.size();i++){
	    		DbTrainingCenter tcr=(DbTrainingCenter)tcrLst.get(i);
	    		xmlBuf.append(tcr.obj2Xml(con, false));
	    	}
			xmlBuf.append("</tcr_list>");
		}
		return xmlBuf.toString();
	}
	
	/**
	 * 更新学习小组
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws qdbException
	 */
	public void updSgp(Connection con, loginProfile prof, StudyGroupModuleParam param) throws SQLException, qdbException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		long tcr_id =sgpDAO.getSgpTc(con, param.getSgp_id());
		if(tcr_id!=param.getTcr_id()){
			sgpDAO.delStudyGroupMemberBySgp(con,  param.getSgp_id());
		}
		sgpDAO.delStudyGroupMgtBySgp(con,  param.getSgp_id());
		Vector mgtVc=cwUtils.splitToVecString(param.getSgp_mgt_str(), "~");

		StudyGroupDB sgp= new StudyGroupDB();
		sgp.sgp_tcr_id = param.getTcr_id();
		sgp.sgp_title = param.getSgp_title();
		sgp.sgp_desc = param.getSgp_desc();
		sgp.sgp_public_type =param.getSgp_public_type();
		sgp.sgp_send_email_ind =param.getSgp_send_email_ind();
		sgp.sgp_id = param.getSgp_id();
		sgp.sgp_upd_timestamp=param.getSgp_upd_timestamp();
		updSgpDisTitle(con, sgp.sgp_id, sgp.sgp_title);

		sgpDAO.updStudyGroupInfo(con, sgp);
		sgpDAO.insStudyGroupMgt(con, prof,  param.getSgp_id(), mgtVc);
	}
	
    public void updSgpDisTitle(Connection con,long sgp_id, String title) throws SQLException{
    	String sql="update resources set res_title=? where res_id =(" +
    			"select sgr_ent_id from studygrouprelation where sgr_sgp_id=? and sgr_type ='SGP_DISCUSS')";
    	PreparedStatement stmt = null; 
	    stmt = con.prepareStatement(sql);
	    stmt.setString(1, title);
	    stmt.setLong(2, sgp_id);
	    stmt.executeUpdate();
	    if(stmt !=null)stmt.close();
    }
	
	
	/**
	 * 删除学校小组
	 * @param con
	 * @param prof
	 * @param param
	 * @throws SQLException
	 * @throws qdbException
	 * @throws qdbErrMessage
	 * @throws cwSysMessage
	 */
	public void delSgp(Connection con, loginProfile prof, long sgp_id) throws SQLException, qdbException, qdbErrMessage, cwSysMessage{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();		
		long res_id=sgpDAO.getSgpForumResId(con, sgp_id);
		sgpDAO.delAllStudyGroupRelation(con, sgp_id,true);
		sgpDAO.delStudyGroupMemberBySgp(con,  sgp_id);
		sgpDAO.delStudyGroupResourceBySgp(con, sgp_id);
		sgpDAO.delStudyGroupById(con,  sgp_id);	
		if(res_id !=0){
			dbModule dbmod =new dbModule();
			dbmod.res_id=res_id;
			dbmod.mod_res_id = res_id;
			dbmod.get(con);
			dbForum forum =new dbForum(dbmod);
			forum.del(con,prof);	
		}
	}
	
	/**
	 * 获取学习小组信息xml
	 * @param con
	 * @param prof
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public String getSgpInfoXml(Connection con, loginProfile prof, StudyGroupModuleParam param) throws SQLException{
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupBean sgp =sgpDAO.getStudyGroupDetail(con, param.getSgp_id());
		String sgp_id_str=String.valueOf(param.getSgp_id());
		Hashtable mgtHs =new Hashtable();
		if(sgp_id_str !=null &&sgp_id_str.length()>0){
			sgpDAO.getMgtEmailHs(con, sgp_id_str, mgtHs);
			if(mgtHs !=null && !mgtHs.isEmpty()&&mgtHs.containsKey(new Long(param.getSgp_id()))){
				Hashtable mgtLstHs= (Hashtable)mgtHs.get(new Long(param.getSgp_id()));
				Enumeration Enum=mgtLstHs.elements();
				Vector mgtVc= new Vector();
				while(Enum.hasMoreElements()){
					StudyGroupMgtBean mgt =(StudyGroupMgtBean)Enum.nextElement();
					mgtVc.add(mgt);
				}
				sgp.setMgtVc(mgtVc);
			}
		}
		
		StringBuffer sgpXml =new StringBuffer();
		sgpXml.append("<sgp id =\"").append(sgp.getSgp_id()).append("\"")
	     .append(" title=\"").append(cwUtils.esc4XML(sgp.getSgp_title())).append("\"")
	     .append(" tcr_id=\"").append(sgp.getTcr_id()).append("\"")
	     .append(" public_type=\"").append(sgp.getSgp_public_type()).append("\"")
	     .append(" send_email_ind=\"").append(sgp.getSgp_send_email_ind()).append("\"")
	     .append(" upd_timestamp=\"").append(sgp.getSgp_upd_timestamp()).append("\">");
		sgpXml.append("<desc>").append(cwUtils.esc4XML(sgp.getSgp_desc())).append("</desc>");	
		sgpXml.append("<tcr id=\"").append(sgp.getTcr_id()).append("\"  title=\"").append(cwUtils.esc4XML(sgp.getTcr_title())).append("\"/>");		
		sgpXml.append(" <manager_lst>");
		Vector mgtLst=sgp.getMgtVc();
		if(mgtLst !=null &&!mgtLst.isEmpty()){
			for(int i=0; i<mgtLst.size(); i++){
				StudyGroupMgtBean mgt= (StudyGroupMgtBean)mgtLst.elementAt(i);
				sgpXml.append("<manager id=\"").append(mgt.getUsr_ent_id()).append("\">").append(cwUtils.esc4XML(mgt.getUsr_display_bil())).append("</manager> ");
			}	
		}		
		sgpXml.append(" </manager_lst>");	
		sgpXml.append("</sgp>");
		return sgpXml.toString();
	}
	public String getTcrIdLst(Vector tcrVc){
		Vector idVc =new Vector();
		String idStr="(";
		if(tcrVc!=null && !tcrVc.isEmpty()){
			for(int i=0; i<tcrVc.size(); i++){
				TCBean tcr =(TCBean)tcrVc.elementAt(i);
				if(i!=0){
					idStr+=",";
				}
				idStr+=tcr.getTcr_id();
			}
		}
		idStr+=")";
		return idStr;	
	}
	  
    public static Vector getStudyGroupTcr(Connection con, long sgp_id, loginProfile prof) throws SQLException{
    	String sql = "select tcr_id from studygroup,tcTrainingCenter " +
    			" where sgp_tcr_id = tcr_id and  tcr_ste_ent_id = ? and tcr_status = ? and  sgp_id = ?";
    	PreparedStatement stmt = con.prepareStatement(sql);
    	stmt.setLong(1, prof.root_ent_id);
    	stmt.setString(2, DbTrainingCenter.STATUS_OK);
    	stmt.setLong(3, sgp_id);    	
    	ResultSet rs = stmt.executeQuery();
    	Vector tcVt = new Vector();
    	while(rs.next()){
    		tcVt.addElement(new Long(rs.getLong("tcr_id")));			    		
    	}
    	if(stmt !=null) stmt.close();
    	return tcVt;
    }
	
    public static void insNotify(Connection con, loginProfile prof, long sgp_id, long[] ent_ids)
    throws SQLException, cwException, cwSysMessage {
	    String subject = LangLabel.getValue(prof.cur_lan, "856")+StudyGroupDAO.getSgpTitleByID(con, sgp_id) + LangLabel.getValue(prof.cur_lan, "857");    	
    	DbMgMessage dbMsg = new DbMgMessage();
        dbMsg.msg_send_usr_id = prof.usr_id;
        dbMsg.msg_create_usr_id = prof.usr_id;                        
        dbMsg.msg_create_timestamp = cwSQL.getTime(con);
        dbMsg.msg_target_datetime = cwSQL.getTime(con);
        dbMsg.msg_subject = subject;
       
    	String[] value = {MSG_TPL_JOIN_STUDYGROUP_NOTIFY_CMD, "GET_ENT_ID", prof.usr_id, sgp_id+""};
        Vector vec = new Vector();
        String[] xtp_subtype = {"HTML"};
        vec = notifyParams(NAME, TYPE, value);
        
        Message msg = new Message();
        msg.insNotify(con, ent_ids, null, MSG_TPL_JOIN_STUDYGROUP_NOTIFY, xtp_subtype, dbMsg, vec);
        return;
    }
    
    public static Vector notifyParams(String[] name, String[] type, String[] value) {
        
        Vector params = new Vector();
        Vector paramsName = new Vector();
        Vector paramsType = new Vector();
        Vector paramsValue = new Vector();
                
        for(int i=0; i<name.length; i++) {
            paramsName.addElement(name[i]);
            paramsType.addElement(type[i]);
            paramsValue.addElement(value[i]);
        }

        params.addElement(paramsName);
        params.addElement(paramsType);
        params.addElement(paramsValue);
        return params;
    }

	public String getEmailContentXML(Connection con, long sgp_id) throws SQLException {
		StudyGroupDAO sgpDAO= new StudyGroupDAO();
		StudyGroupBean sgp =sgpDAO.getStudyGroupDetail(con, sgp_id);
		StringBuffer sgpXml =new StringBuffer();
		sgpXml.append("<sgp id =\"").append(sgp.getSgp_id()).append("\"")
	     .append(" title=\"").append(cwUtils.esc4XML(sgp.getSgp_title())).append("\">");
		sgpXml.append("<desc>").append(cwUtils.esc4XML(sgp.getSgp_desc())).append("</desc>");	
		sgpXml.append("</sgp>");
		return sgpXml.toString();
	}
}

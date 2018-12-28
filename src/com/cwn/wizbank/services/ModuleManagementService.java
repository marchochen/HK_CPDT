package com.cwn.wizbank.services;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.content.EvalAccess;
import com.cw.wizbank.content.Reference;
import com.cw.wizbank.db.DbCtReference;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbAssignment;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbModuleSpec;
import com.cw.wizbank.qdb.dbProgress;
import com.cw.wizbank.qdb.dbProgressAttemptSave;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbSSOLink;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbMailSender;
import com.cw.wizbank.qdb.qdbMailman;
import com.cw.wizbank.qdb.qdbTestInstance;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.report.LearnerRptExporter;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.Module;
import com.cwn.wizbank.entity.Progress;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.Xml2MapUtil;

@Service
public class ModuleManagementService extends BaseService<Module> {

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	@Autowired
	ModuleService moduleService;
	
	@Autowired
	ProgressService progressService;
	
	String DELIMITER = "[|]";
	String DELIMITERDESC = "[||]";
	
	/**
	 * 获取题目
	 * @param prof
	 * @param wizbini
	 * @param tkh_id
	 * @param mod_id
	 * @param window_name
	 * @return
	 */
	public Map<String, Object> getTstMap(Connection con, loginProfile prof, WizbiniLoader wizbini, HttpSession sess, long tkh_id, long mod_id,
			String window_name) throws Exception {
		String[] robs = new String[30];
		Vector<Long> queIdVec = new Vector<Long>();
		
		dbModule dbmod = new dbModule();
		dbmod.res_upd_user = prof.usr_id;
		dbmod.mod_res_id = mod_id;
		dbmod.get(con);
		
		dbModuleSpec dbmsp = new dbModuleSpec();
		dbmsp.msp_res_id = mod_id;
		dbmsp.msp_duration = -1;
		
		float time_limit = dbmod.res_duration;
		if (tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
			tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con,
					dbmod.mod_res_id, prof.usr_ent_id);
		}

		/*if(DbTrackingHistory.getAppTrackingIDByCos(con, tkh_id, prof.usr_ent_id, dbModule.getCosId(con, dbmod.mod_res_id), dbmod.mod_res_id) != 1){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "USR033");
			return map;
		}*/
		dbModuleEvaluation mov = new dbModuleEvaluation();
		mov.mov_ent_id = prof.usr_ent_id;
		mov.mov_tkh_id = tkh_id;
		mov.mov_mod_id = mod_id;
		mov.get(con);
		
		dbProgress dbp = new dbProgress();
		if (dbp.numEssayNotMarked_test(con, dbmod.mod_res_id, tkh_id) > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "PGR011");
			return map;
        }
		
		ExportController controller = new ExportController();
		if(sess.getAttribute(window_name + LearnerRptExporter.EXPORT_CONTROLLER) != null){
			sess.removeAttribute(window_name + LearnerRptExporter.EXPORT_CONTROLLER);
			sess.setAttribute(window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
		} else {
			sess.setAttribute(window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
		}
		
		dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
		dbpas.pasTkhId = tkh_id;
		dbpas.pasResId = dbmod.mod_res_id;
		dbpas.pasTimeLeft = ((long) time_limit) * 60;
		dbpas.pasFlag = null;
		dbpas.pas_create_usr_id = prof.usr_id;
		dbpas.pas_update_usr_id = prof.usr_id;
		if(dbmod.mod_max_usr_attempt > 0 && mov.mov_total_attempt >= dbmod.mod_max_usr_attempt && !dbpas.chkforExist(con)){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("error", "PGR005");
			return map;
		}
		
		String xml = dbmod.genTestAsXML_test(con, prof, dbmsp, robs, time_limit, wizbini.getFileUploadResDirAbs(), queIdVec, 
				tkh_id, false, true, controller, qdbAction.tests_memory, sess, 0);
		
		// save progress tracking data
		if (dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_TST) || dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)) {
			if (dbmod.mod_managed_ind == 1 && dbmod.mod_started_ind == 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("error", "PGR005");
				return map;
			}
			dbmod.saveTrackingDataAtLaunch(con, prof, tkh_id, 0);
		}
		
		if (!dbpas.chkforExist(con)) {
			dbpas.ins(con, cwSQL.getTime(con));
		}
		
		return Xml2MapUtil.xml2Map(xml);
	}
	
	/**
	 * 统计答题情况
	 * @param prof
	 * @param tkh_id
	 * @param mod_id
	 * @param que_id_str 题目id
	 * @param que_anwser_option_str 回答答案
	 * @param start_time 开始考试时间
	 * @return
	 */
	public qdbTestInstance sendTstResult(Connection con, loginProfile prof, long tkh_id, long mod_id, String que_id_str,String que_anwser_option_id_str, String que_anwser_option_str, 
			Timestamp start_time) throws Exception {
		con.setAutoCommit(false);
		long[] que_id_lst = cwUtils.splitToLong(que_id_str, DELIMITER);
        String[] que_anwser_option_lst = cwUtils.splitToString(que_anwser_option_str, DELIMITER);
        long[] que_anwser_option_id_lst = cwUtils.splitToLong(que_anwser_option_id_str, DELIMITER);
        
        qdbTestInstance tst = qdbTestInstance.markAndSaveMobile(con, prof, tkh_id, mod_id, que_id_lst,que_anwser_option_id_lst, que_anwser_option_lst, start_time);
        con.commit();
        return tst;
	}
	
	/**
	 * 提交作业操作	 
	 * @param prof
	 * @param wizbini
	 * @param tkh_id
	 * @param mod_id
	 * @param step 操作号
	 * @param comment_list 简介
	 * @param file_list 文件
	 * @param file_detail_str
	 * @return
	 */
	public void submitAssignment(Connection con, loginProfile prof, WizbiniLoader wizbini, long tkh_id, long mod_id, long step, 
			List<String> comment_list, List<MultipartFile> file_list, String file_detail_str)  throws Exception {
		try{	
			con.setAutoCommit(false);
			dbAssignment dbass = new dbAssignment();
			dbass.res_id = mod_id;
			dbass.ass_res_id = mod_id;
			dbass.INI_DIR_UPLOAD = wizbini.getFileUploadResDirAbs();
			dbass.bFileUpload = true;
			java.util.Date ts = new java.util.Date();
	        SimpleDateFormat fm = new SimpleDateFormat("SSSHHmmss");
	        
	        dbass.tmpUploadDir = wizbini.getFileUploadTmpDirAbs() + dbUtils.SLASH + fm.format(ts);
	        dbUtils.delFiles(dbass.tmpUploadDir);
			File saveDir = new File(dbass.tmpUploadDir);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}
			
			Hashtable<String, String> file_hash = new Hashtable<String, String>();
			for(int i=0; i < file_list.size(); i++){
				file_hash.put(file_list.get(i).getOriginalFilename(), comment_list.get(i));
				File targetFile = new File(dbass.tmpUploadDir, file_list.get(i).getOriginalFilename());
				file_list.get(i).transferTo(targetFile);
			}
			
			if(!"".equalsIgnoreCase(file_detail_str)){
				String[] file_detail_list = cwUtils.splitToString(file_detail_str, DELIMITER);
				for(int i=0;i<file_detail_list.length;i=i+2){
					file_hash.put(file_detail_list[i].replace(DELIMITERDESC, ""), file_detail_list[i+1].replace(DELIMITERDESC, ""));
					
				}
			}
			
			dbass.submitAssignment(con, prof, file_hash, step, tkh_id);
			
			if (step == 6){
                dbModuleEvaluation dbmov = new dbModuleEvaluation();
                dbmov.mov_cos_id = dbModule.getCosId(con, dbass.ass_res_id);
                dbmov.mov_ent_id = prof.usr_ent_id;
                dbmov.mov_mod_id = dbass.ass_res_id;
                dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
                dbmov.mov_tkh_id = tkh_id;
                dbmov.save(con, prof);
            }

            if (step == 6) {
            	dbass.get(con);
                con.commit();
                if (dbass.ass_notify_ind.equals(dbAssignment.NOTIFY_ON) ) {
                    String cos_title = dbCourse.getCosTitle(con, dbModule.getCosId(con, dbass.ass_res_id));
                    String template;

                    qdbEnv static_env = new qdbEnv();
                    if (prof.label_lan.equalsIgnoreCase("ISO-8859-1")) {
                        template = static_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL;
                    } else if (prof.label_lan.equalsIgnoreCase("Big5")) {
                        template = static_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_CH;
                    } else {
                        template = static_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_GB;
                    }
                    qdbMailSender sender = new qdbMailSender(Application.MAIL_SERVER_HOST, wizbini.cfgSysSetupadv.getEncoding(), qdbMailman.HTML);
                    acSite ac_site = new acSite();
                    ac_site.ste_ent_id = prof.root_ent_id;
                    dbRegUser dbusr = new dbRegUser();

                    if (Application.MAIL_SERVER_ACCOUNT_TYPE.equalsIgnoreCase("NOTES") ) {
                        dbusr.usr_ent_id = ac_site.getSiteSysEntId(con);
                        dbusr.get(con);
                        String[] sender_ = {dbusr.usr_email_2, dbusr.usr_display_bil};
                        String[] bcc_ = {dbusr.usr_email_2, dbusr.usr_display_bil};

                        dbusr.usr_ent_id = prof.usr_ent_id;
                        dbusr.get(con);
                        String[] receiver_ = {dbusr.usr_email_2, dbusr.usr_display_bil};
                        String[] contents = {dbass.res_title, cos_title, sender_[1]};
                        try {
                            sender.sendWithTemplate( sender_, receiver_, null, bcc_, null, template, contents, Application.MAIL_SERVER_USER, Application.MAIL_SERVER_PASSWORD, Application.MAIL_SERVER_AUTH_ENABLED);
                        } catch (Exception e) {
                            con.rollback();
                            return;
                        }
                    } else {
                        dbusr.usr_ent_id = ac_site.getSiteSysEntId(con);
                        dbusr.get(con);
                        String[] sender_ = {dbusr.usr_email, dbusr.usr_display_bil};
                        String[] bcc_ = {dbusr.usr_email, dbusr.usr_display_bil};

                        dbusr.usr_ent_id = prof.usr_ent_id;
                        dbusr.get(con);
                        String[] receiver_ = {dbusr.usr_email, dbusr.usr_display_bil};
                        String[] contents = {dbass.res_title, cos_title, sender_[1]};
                        try {
                            sender.sendWithTemplate( sender_, receiver_, null, bcc_, null, template, contents, Application.MAIL_SERVER_USER, Application.MAIL_SERVER_PASSWORD, Application.MAIL_SERVER_AUTH_ENABLED);
                        } catch (Exception e) {
                            con.rollback();
                            return;
                        }
                    }
                }
            }
            con.commit();
		} catch(Exception e) {
			con.rollback();
			e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 获取作业成绩	 
	 * @param prof
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	public Map<String, Object> getAssScore(Connection con, loginProfile prof, long tkh_id, long mod_id) throws Exception{
		dbAssignment dbass = new dbAssignment();
		dbass.ass_res_id = mod_id;
		dbass.RES_FOLDER = qdbEnv.RES_FOLDER;
		
		String xml = dbass.getUserReport1(con, prof.usr_id, prof, tkh_id);
		return Xml2MapUtil.xml2Map(xml);
	}
	
	/**
	 * 获取作业内容	 
	 * @param prof
	 * @param wizbini
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	public Map<String, Object> getAssContent(Connection con, loginProfile prof, WizbiniLoader wizbini, long tkh_id, long mod_id) throws Exception{
		dbAssignment dbass = new dbAssignment();
		dbass.res_upd_user = prof.usr_id;
		dbass.ass_res_id = mod_id;
		dbass.get(con);
		String ssoXml = dbSSOLink.ssoLinkAsXML(prof.root_id, wizbini);
		
		Map<String, Object> map = Xml2MapUtil.xml2Map(dbass.asXML(con, prof, null, tkh_id, ssoXml));
		String xml = dbModuleEvaluation.getStatus(con, prof, mod_id, tkh_id, mod_id);
		map.putAll(Xml2MapUtil.xml2Map(xml));
		return map;
	}
	
	/**
	 * 获取教材或视频点播内容
	 * @param prof
	 * @param wizbini
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	public Map<String, Object> getRdgContent(Connection con, loginProfile prof, WizbiniLoader wizbini, long tkh_id, long mod_id) throws Exception{
		dbModule dbmod = new dbModule();
		dbmod.res_upd_user = prof.usr_id;
		dbmod.mod_res_id = mod_id;
		dbmod.get(con);
		EvalAccess evaAcc = new EvalAccess();
		evaAcc.eac_res_id = dbmod.res_id;
		String other_xml = evaAcc.getTargetDisplayByRes_IDASXML(con);
		
		Map<String, Object> map = Xml2MapUtil.xml2Map(dbmod.asXML(con, prof, null, other_xml, wizbini.cfgTcEnabled));
		String xml = dbModuleEvaluation.getStatus(con, prof, mod_id, tkh_id, mod_id);
		map.putAll(Xml2MapUtil.xml2Map(xml));
		return map;
	}
	
	/**
	 * 获取参考内容
	 * @param prof
	 * @param wizbini
	 * @param tkh_id
	 * @param mod_id
	 * @return
	 */
	public Map<String, Object> getRefContent(Connection con, loginProfile prof, WizbiniLoader wizbini, long tkh_id, long mod_id) throws Exception{
		DbCtReference myDbReference = new DbCtReference();
		dbModule myDbModule = new dbModule();
		myDbReference.ref_res_id = (int) mod_id;
		myDbModule.res_id = mod_id; 
		myDbModule.mod_res_id = mod_id;
		myDbModule.tkh_id = tkh_id;
		Reference myReference = new Reference(myDbReference, myDbModule);
		
		Map<String, Object> map = Xml2MapUtil.xml2Map(myReference.getReferenceList(con, prof));
		String xml = dbModuleEvaluation.getStatus(con, prof, mod_id, tkh_id, mod_id);
		map.putAll(Xml2MapUtil.xml2Map(xml));
		return map;
	}
	
	/**
	 * 获取aicc学习报告
	 * @param prof
	 * @param tkh_id
	 * @param res_id
	 * @param mod_id
	 * @return
	 */
	public Map<String, Object> getAiccReport(Connection con, loginProfile prof, long tkh_id, long res_id, long mod_id) throws Exception{
		dbModuleEvaluation dbmov = new dbModuleEvaluation();
		dbmov.mov_ent_id = prof.usr_ent_id;
		dbmov.mov_tkh_id = tkh_id;
		dbmov.mov_mod_id = mod_id;
		dbmov.mov_cos_id = res_id;
		
		String xml = dbmov.getLearnerAiccRptAsXML(con, prof);
		return Xml2MapUtil.xml2Map(xml);
	}
	
	/**
	 * 离线学习的学习记录处理
	 * @param tkh_id
	 * @param res_id
	 * @param mod_id
	 * @param duration 学习时长
	 * @param last_time 最后学习时间
	 * @return
	 */
	public void sendModuleTrack(Connection con, loginProfile prof, long tkhId, long resId, long modId, long duration, Timestamp last_time) throws Exception{
		try {
			dbModuleEvaluation dbmov = new dbModuleEvaluation();
	        dbmov.mod_time = duration;
	        dbmov.mov_cos_id = resId;
	        dbmov.mov_mod_id = modId;
	        dbmov.mov_tkh_id = tkhId;
	        dbmov.mov_ent_id = prof.usr_ent_id;
	        dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
	        dbmov.mov_last_acc_datetime = last_time;
	        dbmov.save(con, prof);
	        con.commit();
		}  catch(Exception e) {
			con.rollback();
			CommonLog.error(e.getMessage(),e);
		}
	}
	
	
	/**
	 * 获取考试报告详情
	 * @param con
	 * @param prof
	 * @param tkh_id
	 * @param mod_id
	 * @param attempt
	 * @param que_id_lst
	 * @return
	 * @throws qdbException
	 * @throws qdbErrMessage
	 * @throws cwSysMessage
	 * @throws cwException
	 * @throws SQLException
	 */
	public Map<String, Object>  getTstReportDetailMap(Connection con, loginProfile prof, long tkh_id, long mod_id, int attempt, String[] que_id_lst) throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException {
		
		qdbEnv static_env = new qdbEnv();
		dbModule dbmod = new dbModule();
		dbmod.res_upd_user = prof.usr_id;
		dbmod.mod_res_id = mod_id;
		dbmod.get(con);
		/*
		 * 判断是否显示考试答题详情 
		 * mod_showInd			每次参加测验后向学员显示试题和答案     0-不显示，1-显示
		 * mod_passedInd		每次参加测验合格后向学员显示试题和答案	0-不显示，1-显示
		 * 
		 */
		Module mod = moduleService.get(mod_id);
		Long mod_passedInd = mod.getMod_show_a_a_passed_ind();
		Long mod_showInd = mod.getMod_show_answer_ind();
		//通过分数
		Double mod_pass_score =mod.getMod_max_score()*mod.getMod_pass_score()/100;
		Double score = null;
		
		//获取所有测试分数
		Progress pgr = new Progress();
		pgr.setPgr_res_id((int)mod_id);
		pgr.setPgr_tkh_id((int)tkh_id);
		pgr.setPgr_usr_id(prof.usr_id);
		List<Progress> pgr_list = progressService.selectAllTstResult(pgr);
		boolean has_pass = false;
		if(pgr_list != null && pgr_list.size() > 0){
			//查询所有的测试的分数，循环与通过的分数对比
			for (Progress progress : pgr_list) {
				if(progress.getPgr_attempt_nbr() <= 0){
					score = progressService.get(prof.usr_id).getPgr_score();
				}else{
					score = progress.getPgr_score();
				}
				//如果有一次的分数合格了。那该学生本考试的所有的考试报告都可以查看
				if(score >= mod_pass_score){
					has_pass = true;
				}
			}
		}
		
		String xml = "";
		Map<String, Object> map = new HashMap<String, Object>();
		 xml = dbmod.getUserReport(con, prof.usr_id, que_id_lst, attempt, prof, null, static_env, tkh_id);
		map = Xml2MapUtil.xml2Map(xml);
		
		//获取当前分数
		if(mod_showInd==1){
			//显示该次考试报告详情
			map.put("mod_showInd", true); 
			map.put("mod_passedInd", true);
		}else if(mod_passedInd==1 && has_pass){
			//成绩合格显示考试报告详情
			map.put("mod_showInd", true); 
			map.put("mod_passedInd", true);
		}else{
			//当没有考试报告时，给出相应的判断是哪种状态不显示
			map.put("mod_showInd", false); //后台设置不显示
			map.put("mod_passedInd", true);
			if(mod_passedInd==1 && !has_pass){
				map.put("mod_passedInd", false);//成绩合格显示（但这次考试不合格）
			}
		}
		
		return map;
	}
	
	public Map<String, Object> aeInsMultiAppn(Connection con, loginProfile prof, long itm_id, boolean auto_enroll_ind, String ent_ids) throws cwException, SQLException, qdbException, cwSysMessage, IOException{		
		Map<String, Object> map = new HashMap<String, Object>();
		aeQueueManager qm = new aeQueueManager();
		String[] ent_id_lst_str = ent_ids.split("~");

		if (!auto_enroll_ind){
			qm.auto_enroll_ind = false;
		}

		//获取item
		aeItem item = new aeItem();
		item.itm_id = itm_id;
		item.getItem(con);
		//查询已经报名的数目
		int enrol_cnt = aeApplication.countItemAppn(con, item.itm_id, false);
		//加上即将报名的数量
		enrol_cnt += ent_id_lst_str.length;
		if (item.itm_not_allow_waitlist_ind && (item.itm_capacity > 0 && item.itm_capacity < enrol_cnt)) {
			map.put("code", "500");
			map.put("msg", LangLabel.getValue(prof.cur_lan, "AEQM11"));
			return map;
		}

		String xml = null;
		long[] ent_id_lst = new long[ent_id_lst_str.length];
        for(int i=0;i<ent_id_lst.length;i++){
			ent_id_lst[i] = Long.parseLong(ent_id_lst_str[i]);
			//检查是否重复报名
		 	xml = qm.checkAppnConflictAsXML(con, itm_id , ent_id_lst[i], true);
		 	if(null != xml){
				map = Xml2MapUtil.xml2Map(xml);
				map.put("code", "500");
				return map;
			}
		}

		//批量报名
		qm.insMultiAppn(con, itm_id, ent_id_lst, prof);
		map.put("code", "200");
		qm.insMultiAppnBySuperviser(con, itm_id, ent_id_lst, prof);
        con.commit();		 
		
		return map;
	}
	
}

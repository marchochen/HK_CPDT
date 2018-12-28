package com.cwn.wizbank.services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemLessonQianDao;
import com.cw.wizbank.ae.aeQueueManager;
import com.cw.wizbank.ae.aeReqParam;
import com.cw.wizbank.ae.aeTemplate;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.DBUserPwdResetHis;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.know.dao.KnowCatalogRelationDAO;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.know.dao.KnowVoteDetailDAO;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.personalization.PsnPreference;
import com.cw.wizbank.qdb.CurrentActiveUser;
import com.cw.wizbank.qdb.SystemSetting;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbProgressAttemptSave;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbSns;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.trunkinterface.InterfaceManagement;
import com.cw.wizbank.trunkinterface.InterfaceModuleParam;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.AcSite;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.persistence.AcSiteMapper;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.CwnUtil;

import net.sf.json.JSONObject;

@Service
public class ForCallOldAPIService extends BaseService<AeApplication> {

	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	AcSiteMapper acSiteMapper;

	/*
	 * 学员在页面上报名时，调用该API status : 0 报名成功，并可以开始学习； 1 报名成功，处于等待审批状态； 2 报名成功，处理等待队列；
	 * -1 报名不成功， msg 为不成功的原因
	 */
	public void insAppForPage(Connection con, loginProfile prof, Model model, long usr_ent_id,
			long itm_id) throws Exception {
		try {
			aeApplication app = new aeApplication();
			app.insAppForPage(con, prof, model, usr_ent_id, itm_id, true);
		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
		
	}

	/*
	 * 学员在页面上取消报名时，调用该API status : 0 报名成功，并可以开始学习； 1 报名成功，处于等待审批状态； 2
	 * 报名成功，处理等待队列； -1 报名不成功， msg 为不成功的原因
	 */
	public void cancelApp(Connection con, loginProfile prof, Model model, long app_id)
			throws Exception {
		try {
			aeApplication app = new aeApplication();
            /*if(!app.canCancel(con)){//不能取消报名
               return;
            }*/
			app.cancelApp(con, prof, model, app_id);
		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		} 

	}


	/**
	 * 返回值说明 -1 不显示任何操作按钮 
	 * 0 显示购买 
	 * 1 显示开始学习（报自动报名后，直接开始学习） 
	 * 2 已报名，并可以开始学习，且可以取消报名 
	 * 3已报名，并可以开始学习，但不可以取消报名， 
	 * 4 显示“报名“按钮，因为课程需要审批”， 
	 * 5 已报名，但状态为”等待审批，可以取消报名“ 
	 * 6 已报名，但状态为”等待队列，可以取消报名“，
	 */
	public long getButton(Connection con, long itm_id, long usr_ent_id, long app_id) throws SQLException,
			cwException {
		
		return aeItem.itmButtonCon(con, itm_id, usr_ent_id, null, app_id);
	}

	/**
	 * 是否能提名
	 * 
	 * @param hasStaff
	 * @param cur_lan
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
/*	public boolean canNorminate(Connection con, boolean hasStaff, String cur_lan, long itm_id)
			throws SQLException {
		
		return canNorminate(con, hasStaff, cur_lan, itm_id);
	}*/

	/**
	 * 是否能提名
	 * 
	 * @param con
	 * @param hasStaff
	 * @param cur_lan
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
	public boolean canNorminate(Connection con, boolean hasStaff,
			String cur_lan, long itm_id) throws SQLException {
		try {
			boolean result = false;
			AeItem item = aeItemService.get(itm_id);

			// long itm_run_ind = item.getItm_run_ind();
			// long itm_create_run_ind = item.getItm_create_run_ind();
			Long itm_not_allow_waitlist_ind = item
					.getItm_not_allow_waitlist_ind();
			Date itm_appn_start_datetime = item.getItm_appn_start_datetime();
			Date itm_appn_end_datetime = item.getItm_appn_end_datetime();
			long itm_capacity = item.getItm_capacity() == null ? 0 : item
					.getItm_capacity();
			long itm_apply_ind = item.getItm_apply_ind();

			if (itm_apply_ind == 1) {
				if (hasStaff) {
					if ((itm_appn_start_datetime != null && itm_appn_start_datetime
							.before(getDate()))
							&& (itm_appn_end_datetime != null && itm_appn_end_datetime
									.after(getDate()))) {
						result = true;
					}
				}
			}
			if (result && itm_not_allow_waitlist_ind != null
					&& itm_not_allow_waitlist_ind > 0) {
				String[] process_status_lst = aeReqParam.split(
						LangLabel.getValue(cur_lan, "process_status"), "~");
				int enrol_cnt = aeApplication.countProcessStatus(con, itm_id,
						process_status_lst);

				if ((itm_capacity != 0 && itm_capacity <= enrol_cnt)) {
					result = false;
				}
			}
			return result;
		} catch (qdbException e) {
			throw new SQLException(e.getMessage());
		}
	}
	
	public boolean quotaIsFull(Connection con, long itm_id) throws SQLException, cwSysMessage, cwException {
		boolean isFull = false;
		aeItem item = new aeItem();
		item.itm_id = itm_id;
		item.getItem(con);
		int enrol_cnt = 0;
    	if(item.itm_app_approval_type != null) {
    		enrol_cnt = aeApplication.countItemAppnAndPend(con, item.itm_id);
    	} else {
    		enrol_cnt = aeApplication.countItemAppn(con, item.itm_id, true);
    	}
    	if(item.itm_capacity > 0 && enrol_cnt >= item.itm_capacity) {
    		isFull = true;
    	}
    	return isFull;
	}

	/**
	 * 
	 * @param creditsType
	 *            积分类型 积分类型请看com.cw.wizbank.JsonMod.credit.Credit.java中定义的
	 * @param sourceId
	 *            与相关的课程/文章id,如果没跟课程/文章相关，就给0
	 * @param usr_ent_id
	 * @param userId
	 * @param itm_id
	 *            与相关的课程id,如果没跟课程相关，就给0
	 * @param app_id
	 *            与课程相关的学员app_id, 如果跟非报名相关，就给0
	 */
	public void updUserCredits(Connection con, String creditsType, long usr_ent_id,
			String usr_id, long sourceId, long app_id) throws Exception {

		try {

			Credit credit = new Credit();
			credit.updUserCredits(con, creditsType, sourceId, (int) usr_ent_id,
					usr_id, 0, app_id, 0, 0);

		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
	}

	public void updUserCredits(Connection con, String creditsType, long usr_ent_id,
			String usr_id, long sourceId, double manualScore, long app_id)
			throws Exception {
		try {

			Credit credit = new Credit();
			credit.updUserCredits(con, creditsType, sourceId, (int) usr_ent_id,
					usr_id, (int) manualScore, app_id, 0, 0);

		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
	}

	/**
	 * 获取报名所需要的信息
	 * 
	 * @return
	 */
	public JSONObject getworkflow(Connection con, loginProfile prof) throws Exception {
		String convertedJson = "";
		StringBuffer tpl_xml = new StringBuffer();
		tpl_xml.append("<xml>")
				.append(aeTemplate.getFirstRawTpl(con, aeTemplate.WORKFLOW,
						prof.root_ent_id, "")).append("</xml>");
		convertedJson = qdbAction.static_env.transformXML(
				tpl_xml.toString(), "workflow_to_json.xsl", null);
		convertedJson = cwUtils.decodeUnicode(convertedJson.toString());
		JSONObject strucObj = JSONObject.fromObject(convertedJson);
		return strucObj;
	
	}

	/**
	 * 判断是否发找回密码邮件
	 * 
	 * @param usr_id
	 *            用户名
	 * @param usr_email
	 *            邮箱
	 * @return
	 */
	public boolean forget_pwd(Connection con, HttpServletRequest request, loginProfile prof,
			String usr_id, String usr_email, WizbiniLoader wizbini, String lang)
			throws Exception {
		boolean result = false;
		if(lang == null)
			lang = "en-us";

		try {
			dbRegUser dbUsr = new dbRegUser();
			String email = dbUsr.getUsrEmail(con, usr_id);
			if(wizbini.cfgSysSetupadv.isCheckEmail()) {
				if (cwUtils.notEmpty(email) && email.equals(usr_email)) {
					result = sendUpdatePwdEmail(con, request, prof, wizbini, dbUsr, usr_email, lang);
				}
			} else {
				String reg = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
				if(dbUsr.usr_ent_id > 0 && Pattern.compile(reg).matcher(usr_email).matches()) {
					result = sendUpdatePwdEmail(con, request, prof, wizbini, dbUsr, usr_email, lang);
				}
			}
		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
		return result;
	}
	
	public boolean sendUpdatePwdEmail(Connection con, HttpServletRequest request, loginProfile prof, WizbiniLoader wizbini,
			dbRegUser dbUsr, String usr_email, String lang) throws SQLException, cwException, qdbException {
		String ip = request.getRemoteAddr();
		long prh_id = DBUserPwdResetHis.ins(con, dbUsr.usr_ent_id, ip);
		if(!wizbini.cfgSysSetupadv.isCheckEmail())
			dbUsr.updateUserEmail(con, dbUsr.usr_ent_id, usr_email);
		
		// 对id进行加密
		String sid = (prh_id + CwnUtil.FORGET_PWD_KEY_1) + "?key=" + (CwnUtil.FORGET_PWD_KEY_1 - prh_id -CwnUtil.FORGET_PWD_KEY_2);
		
		acSite site = new acSite();
		site.ste_ent_id = 1;
		site.get(con);
		int link_max_days = ((UserManagement) wizbini.cfgOrgUserManagement
				.get(String.valueOf(site.ste_id))).getForgetPassword()
				.getLinkLastDays();
		dbUsr.sendMailToForgetPwdUser(con,
				acSite.getSysUsrId(con, prof.root_ent_id),
				LangLabel.getValue(prof.cur_lan, "675"), sid,
				link_max_days,prh_id, lang);
		
		return true;
	}
	
	/**
	 * 重置密码
	 * 
	 * @param wizbini
	 * @param prh_id
	 * @param userId
	 * @param password
	 * @throws qdbException
	 * @throws SQLException
	 * @throws IOException
	 */
	public String resetPassword(Connection con, WizbiniLoader wizbini, long prh_id,
			String userId, String password ,loginProfile prof) throws SQLException, qdbException,
			IOException {
		
		// sid = dbRegUser.decrypt(sid, new
		// StringBuffer(WzbUtils.FORGET_PWD_ENCRYPTIONKEY)
		// .reverse().toString());
		// String sidArr[] = cwUtils.splitToString(sid, "|_|");
		long siteEntId = 1;
		AcSite site = acSiteMapper.get(siteEntId);
		DBUserPwdResetHis his = new DBUserPwdResetHis();
		long usr_ent_id = his.getEntId(con, prh_id);
		String usr_id = dbRegUser.getUserLoginId(con, usr_ent_id);

		int link_max_days = ((UserManagement) wizbini.cfgOrgUserManagement
				.get(String.valueOf(site.getSte_id()))).getForgetPassword()
				.getLinkLastDays();
		int max_attempted = ((UserManagement) wizbini.cfgOrgUserManagement
				.get(String.valueOf(site.getSte_id()))).getForgetPassword()
				.getMaxAttempt();
		int attempted = his.getAttempted(con, prh_id);

		if (!usr_id.equals(userId)) {
			if (attempted < max_attempted) {
				his.updStutusAndAttempted(con, prh_id, attempted,
						max_attempted);
				return "login_input_error";
			} else {
				return "login_input_error_three";
			}
		} else {
			if (!his.checkPwdResetLink(con, prh_id, link_max_days,
					max_attempted, usr_id)) {
				return "login_connect_fail";
			} else {
				dbRegUser dbUsr = new dbRegUser();
				dbUsr.usr_ste_usr_id = usr_id;
				dbUsr.usr_ent_id = usr_ent_id;
				dbUsr.usr_pwd = password;
				dbUsr.updPwd(con,prof);
				his.updStutusAndAttempted(con, prh_id, attempted,
						max_attempted);
				return "login_update_pwd_ok";
			}
		}
		
	}
	
	/**
	 * 检查系统当前用户总是否已超过允许的最磊用户数。
	 * @throws Exception 
	 * 
	 */
	public boolean checkUserNumExceedLimit(Connection con, long root_ent_id) throws Exception {
		boolean exceed = true;
		try{
			exceed = dbRegUser.checkUserNumExceedLimit(con, root_ent_id);
		}catch( Exception e){
			throw e;
		}
		return exceed;
	}

/*	public AcSite getAcSite(Connection con, Long ste_ent_id)
			throws SQLException {
		String sql = " select ste_ent_id, ste_id, ste_name, ste_join_datetime, ste_status, ste_domain, ste_login_url, "
				+ " ste_trusted, ste_ird_id, ste_max_users, ste_eff_start_date, ste_eff_end_date, ste_lan_xml, "
				+ " ste_ldap_host, ste_ldap_dn from acSite where ste_ent_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		AcSite ac = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, ste_ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				ac = new AcSite();
				ac.setSteEntId(rs.getLong("ste_ent_id"));
				ac.setSteId(rs.getString("ste_id"));
				ac.setSteName(rs.getString("ste_name"));
				ac.setSteJoinDatetime(rs.getTimestamp("ste_join_datetime"));
				ac.setSteStatus(rs.getString("ste_status"));
				ac.setSteDomain(rs.getString("ste_domain"));
				ac.setSteLoginUrl(rs.getString("ste_login_url"));
				ac.setSteIrdId(rs.getLong("ste_ird_id"));
				ac.setSteTrusted(rs.getInt("ste_trusted"));
				// Encoded value
				ac.setSteMaxUsers(rs.getString("ste_max_users"));
				ac.setSteEffStartDate(rs.getString("ste_eff_start_date"));
				ac.setSteEffEndDate(rs.getString("ste_eff_end_date"));
				ac.setSteLanXml(rs.getString("ste_lan_xml"));
				ac.setSteLdapDn(rs.getString("ste_ldap_dn"));
				ac.setSteLdapHost(rs.getString("ste_ldap_host"));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return ac;
	}*/

	/**
	 * 删除提问
	 * 
	 * @param que_id
	 *            问题id
	 */
	public void deleteQusetion(Connection con, long que_id) throws Exception {

		try {
			KnowQuestionDAO queDao = new KnowQuestionDAO();
			// 删除问题相关赞
			dbSns.deleteKnowLike(con, que_id);
			dbSns.deleteKnowLikeLog(con, que_id);
			// 删除问题相关动态
			dbSns.deleteKnowDoing(con, que_id);
			// delete vote details of question
			KnowVoteDetailDAO.deleteByQueId(con, que_id);
			// delete answer of question
			KnowAnswerDAO.delAnsByQueId(con, que_id);
			// delete question
			queDao.del(con, que_id);
			// delete the relation of question between question and catalog
			KnowCatalogRelationDAO.delRelationOfQue(con, que_id);
			
		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		} 
	}
	
	/**
	* 删除回答
	 * 
	 * @param que_id
	 *            问题id
	 */
	public void deleteAnswer(Connection con, long que_id, long usr_ent_id) throws Exception {

		try {
			// 删除回答相关动态
			dbSns.deleteKnowAnswerDoing(con, que_id, usr_ent_id);
			
		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		} 
	}


	/**
	 * 旧的登录接口
	 * 
	 * @param prof
	 * @param wizbini
	 * @param usr_ste_usr_id
	 * @param usr_pwd
	 * @param login_role
	 * @param site_id
	 * @return
	 * @throws qdbException
	 * @throws cwException
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public String login(Connection con, loginProfile prof, WizbiniLoader wizbini,
			String usr_ste_usr_id, String usr_pwd, String login_role,
			long site_id, String developer, boolean isRemember, String login_lan)
			throws qdbException, cwException, SQLException, cwSysMessage {
		
		acSite site = new acSite();
		site.ste_ent_id = site_id;
		
		// auth_login
		String loginStatus = dbRegUser.auth_login(con, prof,
				usr_ste_usr_id, usr_pwd, login_role, site, true, wizbini,
				prof.label_lan, login_lan, developer);
		
		return loginStatus;
	}
	
	/**
	 * 檢驗第一次登錄是否需要更改密碼
	 * 		true : 需要
	 * 		false : 不需要
	 * @param con
	 * @param usr_ste_usr_id
	 * @return
	 */
	public boolean isNeedChangePwd(Connection con, String usr_ste_usr_id) {
		
		return dbRegUser.isNeedChangePwd(con, usr_ste_usr_id);
	}

	@SuppressWarnings("rawtypes")
	public HashMap getPublicEvalDetail(Connection con, long mod_id) throws Exception {
		HashMap resultMap = new HashMap();
		InterfaceManagement lm = new InterfaceManagement(new InterfaceModuleParam());
		
		lm.getPublicEvalDetail(con, resultMap, mod_id);

		return resultMap;		
	}
	
	public void submitPublicEval(Connection con, loginProfile prof,long mod_id, String que_id_lst, 
			String que_anwser_option_lst,long used_time, Timestamp start_time) throws Exception {
		InterfaceManagement lm = new InterfaceManagement(new InterfaceModuleParam());

		try {
			lm.submitPublicEval(con, prof, mod_id, que_id_lst, que_anwser_option_lst, used_time, start_time);
		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
	}
	
	/**
	 * 审批报名
	 * 
	 * @param app_id
	 * @param time
	 * @param process_id
	 * @param fr
	 * @param to
	 * @param verb
	 * @param action_id
	 * @param status_id
	 * @param app_priority
	 * @return
	 */
	public void doAppnActn(Connection con, loginProfile prof, long app_id, Timestamp upd_time, long process_id, String fr, String to, String verb, 
			long action_id, long status_id) throws Exception{
	
		aeQueueManager qm = new aeQueueManager();
		Timestamp cur_time = cwSQL.getTime(con);
		qm.doAppnActn(con, app_id, process_id, fr, to, verb, action_id, status_id, prof, null, upd_time, cur_time, false, "");
		
	}
	
	/**
	 * 多语言切换
	 * 
	 * @param curLang
	 * @return
	 */
	public void changeLang(Connection con, loginProfile prof, String curLang) throws Exception{
		PsnPreference psnPreference = new PsnPreference(); 
		try{
	        psnPreference.savePreerenceLang(con, prof.usr_ent_id, curLang, prof.usr_id);
	        //update prof
	        prof.cur_lan = curLang;
	        prof.label_lan = cwUtils.langToLabel(curLang);
		} catch(Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
	}
	
	/**
	 * 离线签到
	 * 
	 * @param itm_id 课程id 
	 * @return
	 */
	public Map<String, String> offlineRegistration(Connection con, loginProfile prof, long ils_id){
		aeItemLessonQianDao ilsQd = new aeItemLessonQianDao();
		return ilsQd.qianDao(con, prof, ils_id);
	}
	
	
	
	public void saveEntityRelation(Connection con, long usrEntId, long[] usr_attribute_ent_ids,String[] usr_attribute_relation_types, boolean del_ind, String usr_id)throws Exception{
		 dbRegUser dbusr = new dbRegUser();
		 dbusr.usr_ent_id = usrEntId;
		 dbusr.ent_id = usrEntId;
		 dbusr.saveEntityRelation(con, usr_attribute_ent_ids, usr_attribute_relation_types, false, usr_id);
	}
	
	/**
	 * 获取在线问答站内信信息内容
	 * @author bill
	 * @param con
	 * @param mtp
	 * @param prof
	 * @param knowQuestion
	 * @param learnerName
	 * @return
	 * @throws SQLException 
	 */
	public String[] getKnowQuestionContent(Connection con, MessageTemplate mtp, loginProfile prof, KnowQuestion knowQuestion, String learnerName) throws SQLException{
		MessageService ms = new MessageService();
		String[] strs = null;
		try {
			strs = ms.getKnowQuestionContent(con, mtp, prof, knowQuestion, learnerName);
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
		return strs;
	}
	
	/**
	 * 是否拥有下属
	 * @throws Exception 
	 */
	public boolean hasStaff(Connection con,loginProfile prof) throws Exception{
		boolean hasStaff = false;
		try {
			Supervisor sup = new Supervisor(con, prof.usr_ent_id);
			if (sup.hasStaff(con)) {
			    hasStaff = true;
			} else {
			    hasStaff = false;
			}
		} catch (cwException e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
		return hasStaff;
	}
	
	/**
	 * 获取企业当前登录用户数
	 * @param con
	 * @param eipTcrId
	 * @return
	 * @throws SQLException
	 */
	public long getCurActUserCntByEipTcrId(Connection con,long eipTcrId){
		try {
			return CurrentActiveUser.getCurActUserCntByEipTcrId(con, eipTcrId);
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
			return 0;
		}
	}
	

	public boolean hasEffTc(Connection con,long usrEntId) throws SQLException{
		boolean  hasEffTc = false;
		 try {
			 hasEffTc = ViewTrainingCenter.hasEffTc(con,usrEntId);
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
			throw e;
		}
		 return hasEffTc;
	}
	
	public Long getEntSysUserId(Connection con) throws SQLException{
		return dbRegUser.getEntSysUserId(con);
	}
	
	/**
	 * 判断是否达到了用户警告值
	 * @throws SQLException 
	 * @author bill.lai
	 * 
	 */
	public boolean getWarn(Connection con) throws SQLException{
		// is out the warning threshold setting
        Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
        long blockThreshold = 0;
		long warnThreshold = 0;
		if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString().length() > 0){
			blockThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString());
		}
		if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString().length() > 0){
			warnThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString());
		}
        long loginUserCount = CurrentActiveUser.getcurActiveUserCount(con);
        boolean warn = false;
        if((blockThreshold > 0 && loginUserCount >= blockThreshold)
        		|| (warnThreshold > 0 && loginUserCount >= warnThreshold)) {
        	warn = true;
        }
       return warn;
	}
	
	
	/**
	 * 升级数据库使用，把最顶层培训中的邮件模板复制到每个企业。
	 * @throws SQLException 
	 * @author bill.lai
	 * 
	 */
	public void coptyTemplates(Connection con, long to_tcr_id) throws SQLException{
		MessageService msgService = new MessageService();
		msgService.coptyTemplates( con, 0, to_tcr_id) ;
	}
	
	/**
	 * 检查考试是否有保存模未提交的记录
	 * 
	 * @param tkh_id 报名记录的跟踪id
	 * @param mod_id  模块的ID  
	 * @return
	 */
	public boolean chkforExist(Connection con, long tkh_id, long mod_id) throws Exception {
		dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
		dbpas.pasTkhId = tkh_id;
		dbpas.pasResId = mod_id;
		
		return dbpas.chkforExist(con);
	}
	

    /**
     * 检查学员是否提交测验
     * @param con
     * @param usr_id
     * @param res_id
     * @return
     * @throws qdbException
     */
    public boolean chkforSubmit2(Connection con, String usr_id, long tkh_id) throws qdbException {
		dbProgressAttempt dbpa = new dbProgressAttempt();
		dbpa.atm_pgr_usr_id = usr_id;
		dbpa.atm_tkh_id = tkh_id;
		
		return dbpa.chkforSubmit2(con);
	}

    /**
     * 获取实体的全路径
     * @param con
     * @param ent_id
     * @return
     * @throws SQLException
     */
    public String getEntityFullPath (Connection con,Long ent_id ) throws SQLException{
        EntityFullPath entityFullPath = EntityFullPath.getInstance(con);
        return entityFullPath.getFullPath(con, ent_id);
    }

    /**
     * 登录人数超出系统设置的值
     * @param con
     * @param prof
     * @param wizbini
     * @return
     * @throws SQLException
     */
    public boolean loginUserExceedLimit(Connection con,loginProfile prof,WizbiniLoader wizbini) throws SQLException {

        Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
        long blockThreshold = 0;
        long warnThreshold = 0;
        long LoginUserCount = 0;
        //当二级培训中心为true时,则执行
        if(wizbini.cfgSysSetupadv.isTcIndependent()){
            //查询企业充许最大在线人数
            blockThreshold = SystemSetting.getEipMaxpeakcount(con,prof.my_top_tc_id);
            //取得当前企业在线人数
            LoginUserCount = SystemSetting.getEippeakcount(con,prof.my_top_tc_id);
        }else{
            if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString().length() > 0){
                blockThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString());
            }
            if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString().length() > 0){
                warnThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString());
            }
            LoginUserCount = CurrentActiveUser.getcurActiveUserCount(con);
        }

        if (blockThreshold > 0) {
            if(LoginUserCount > blockThreshold) {
                return true;
            }
        }

        return false;
    }
}

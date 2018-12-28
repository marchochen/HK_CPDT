package com.cw.wizbank.JsonMod.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.Application;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.commonBean.GoldenManOptionBean;
import com.cw.wizbank.JsonMod.commonBean.OptionBean;
import com.cw.wizbank.accesscontrol.AcRegUser;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.batch.user.DeleteUser;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.db.DBUserPwdResetHis;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.personalization.PsnPreference;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbXMessage;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;
import com.cwn.wizbank.cpd.service.CpdDbUtilsForOld;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.CwnUtil;


public class UserModule extends ServletModule {
	
	public static final String PROF_ATTR_LST = "prof_attr_lst";
	
	public static final String USER_DETAIL="user_detail";
	
	public static final String JSON_GRANTED_ROLES = "granted_roles";
	
	public static final String FORGET_PWD_ENCRYPTIONKEY ="wizb";
	
	//private static final String 
	
	UserModuleParam modParam;
	
	public UserModule()
	{
		super();
		modParam = new UserModuleParam();
		param = modParam;
	}
	
	public void process() throws IOException, cwException, SQLException
	{
		
		try {
				if(this.prof == null || this.prof.usr_ent_id == 0) {
					if(modParam.getCmd().equalsIgnoreCase("user_pwd_reset_notify_xml")){
						StringBuffer xmlBuf = new StringBuffer();
						qdbXMessage msgWorker = new qdbXMessage();
						xmlBuf.append(msgWorker.getSenderXml(con, modParam.getSender_id(), Application.MAIL_SERVER_ACCOUNT_TYPE));
		                xmlBuf.append(msgWorker.getRecipientXml(con, modParam.getUsr_ent_id(), Application.MAIL_SERVER_ACCOUNT_TYPE, wizbini.cfgSysSetupadv.getDesKey()));
		                String serverName = request.getServerName();
		                int severPort = request.getServerPort();
		                String scheme = request.getScheme();
		                String sid =modParam.getSid();
		                String link = scheme+"://"+serverName+":"+severPort+"/app/user/to_reset_password?sid="+modParam.getSid();
		               
						acSite site = new acSite();
						site.ste_ent_id = 1;
						site.get(con);
						int link_max_days = ((UserManagement) wizbini.cfgOrgUserManagement
								.get(String.valueOf(site.ste_id)))
								.getForgetPassword().getLinkLastDays();
						long prh_id = new Long(sid).longValue();
						DBUserPwdResetHis his = new DBUserPwdResetHis();						
		                Timestamp req_time = his.getPrhCreateTime(con, prh_id);
		                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		                
		                xmlBuf.append("<link max_days=\"").append(link_max_days).append("\" ").append("req_time=\"").append(dateformat.format(req_time)).append("\">").append(cwUtils.esc4XML(link)).append("</link>");
		                resultXml = xmlBuf.toString();

					}else if(modParam.getCmd().equalsIgnoreCase("reset_pwd_check")){
						 String sid = modParam.getSid();
						 String dec_sid = dbRegUser.decrypt(sid,  new StringBuffer(FORGET_PWD_ENCRYPTIONKEY).reverse().toString());
						 String sidArr[] = cwUtils.splitToString(dec_sid, "|_|");
						 acSite site = new acSite();
						 site.ste_ent_id = new Long(sidArr[3]).longValue();
						 site.get(con);
						 try {
							 prof = new loginProfile();
							 dbRegUser.guest_login(con, prof, site, true, wizbini);
							 sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
						} catch (cwSysMessage e) {
							CommonLog.error(e.getMessage(),e);
						}
						DBUserPwdResetHis his = new DBUserPwdResetHis(); 
						long prh_id = new Long(sidArr[0]).longValue();
						String usr_id = sidArr[1];					
						int link_max_days =((UserManagement)wizbini.cfgOrgUserManagement.get(String.valueOf(site.ste_id))).getForgetPassword().getLinkLastDays();
						int max_attempted =((UserManagement)wizbini.cfgOrgUserManagement.get(String.valueOf(site.ste_id))).getForgetPassword().getMaxAttempt();
						if(!his.checkPwdResetLink(con, prh_id, link_max_days, max_attempted, usr_id)){
							encoding = prof.label_lan;//encoding was setted as "UTF-8" in Dispatcher before UserModule which is because the prof is null, so set the value again otherwise the message will be shown in wrong.
							sysMsg = getErrorMsg("677", modParam.getUrl_failure());	
						}else{
							setMetaMap();
							resultJson.put("sid", sid);
						}
					}else{						
						throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
					}
				} else {// 若已经登陆
					
					User user=new User();
					
					if(modParam.getCmd().equalsIgnoreCase("home")) {
					   if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						List rol_lst = user.getUserRolesJsonCanLogin(con, prof.usr_ent_id, param.getCur_time());
						MetaMap.put(JSON_GRANTED_ROLES, rol_lst);

						user.getHome(con, prof, wizbini, modParam, resultJson);
						//获取学员所在的培训中心的模板
						Long tcr_id = user.getModuleCode(con, modParam, sess, prof, resultJson);
						//获取培训中心下的自定义项目
						user.getDefinedProject(con, resultJson, tcr_id);
						//获取企业QQ号码列表
						user.getCompanyQQInfor(con, resultJson, tcr_id);
					}else if(modParam.getCmd().equalsIgnoreCase("home_gadget")) { 
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						user.getHomeGadget(con, prof, modParam.getType(), resultJson,wizbini);
						hasMetaAndSkin = false;
					} else if(modParam.getCmd().equalsIgnoreCase("get_my_profile")){
					      if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Object userInfo=user.getUserProfile(con, prof.usr_ent_id, wizbini, this.prof.root_id,defJsonConfig, prof);

						if(userInfo!=null) {
							resultJson.put(USER_DETAIL,user.user_detail);
							resultJson.put("user",userInfo);
							resultJson.put(PROF_ATTR_LST, wizbini.getUsrMgtJson(prof.root_id, prof.cur_lan));
							user.setUserTcrId(con, resultJson, prof);
						}
						JsonHelper.disableEsc4JsonAll(defJsonConfig);
					} else if(modParam.getCmd().equalsIgnoreCase("get_usr_profile"))	{
                          if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Object userInfo=user.getUserProfile(con, modParam.getUsr_ent_id(), wizbini, this.prof.root_id,defJsonConfig, prof);

						if(userInfo!=null) {
							resultJson.put(USER_DETAIL,user.user_detail);
							resultJson.put("user",userInfo);
							resultJson.put(PROF_ATTR_LST, wizbini.getUsrMgtJson(prof.root_id, prof.cur_lan));
						}
						JsonHelper.disableEsc4JsonAll(defJsonConfig);
					} else if(modParam.getCmd().equalsIgnoreCase("usr_reg_prep")) {	
						resultJson.put(PROF_ATTR_LST, wizbini.getUsrMgtJson(prof.root_id, prof.cur_lan));

						DbUserGrade defaultUgr =DbUserGrade.getDefaultGrade(con, prof.root_ent_id);
						OptionBean  option=new OptionBean();
						option.setId(defaultUgr.ugr_ent_id);
						option.setText(defaultUgr.ugr_display_bil);
						String grade_name=cwXSL.getGoldenMan(prof.label_lan, modParam.getGoldenman_param());
						GoldenManOptionBean bean=new GoldenManOptionBean();
						Vector vc=new Vector();
						vc.add(option);
						bean.setValue(vc);
						bean.setName(grade_name);
						
						Vector vector=new Vector();
						vector.add(bean);
						
						cwXSL.outPutGoldManOption(resultJson,vector );
						cwXSL.getGoldenManHtml(prof.label_lan, modParam.getGoldenman_param(),resultJson);
					} else if(modParam.getCmd().equalsIgnoreCase("change_lang")) {
						String curLang = modParam.getLang();
						PsnPreference psnPreference = new PsnPreference(); 
		                psnPreference.savePreerenceLang(con, prof.usr_ent_id, curLang, prof.usr_id);
		                //update prof
		                prof.cur_lan = curLang;
		                prof.label_lan = cwUtils.langToLabel(curLang);
		                redirectUrl = modParam.getUrl_success();

					} else if(modParam.getCmd().equalsIgnoreCase("forget_pwd")){					
						dbRegUser dbUsr = new dbRegUser();
						String email = dbUsr.getUsrEmail(con, modParam.getUsr_id());
						if(email == null || email.length() == 0){
							sysMsg = getErrorMsg("676", modParam.getUrl_success());						
						}else{
							if(modParam.getUsr_email().equals(email)){
								String ip = request.getRemoteAddr();
								long prh_id = DBUserPwdResetHis.ins(con, dbUsr.usr_ent_id, ip);
								String sid = (prh_id + CwnUtil.FORGET_PWD_KEY_1) + "?key=" + (CwnUtil.FORGET_PWD_KEY_1 - prh_id -CwnUtil.FORGET_PWD_KEY_2);
								acSite site = new acSite();
								int link_max_days = ((UserManagement) wizbini.cfgOrgUserManagement .get(String.valueOf(site.ste_id))).getForgetPassword().getLinkLastDays();
								dbUsr.sendMailToForgetPwdUser(con,site.getSysUsrId(con, prof.root_ent_id), LangLabel.getValue(prof.cur_lan, "675"), sid,link_max_days, prh_id, wizbini.cfgSysSkinList.getDefaultLang());
								sysMsg = getErrorMsg("681", modParam.getUrl_success());									
							}else{
								sysMsg = getErrorMsg("678", modParam.getUrl_failure());		
							}
						}						
					} else if(modParam.getCmd().equalsIgnoreCase("reset_pwd_check")){
						String sid = modParam.getSid();
						sid = dbRegUser.decrypt(sid, new StringBuffer(
								FORGET_PWD_ENCRYPTIONKEY).reverse().toString());
						String sidArr[] = cwUtils.splitToString(sid, "|_|");
						acSite site = new acSite();
						site.ste_ent_id = new Long(sidArr[3]).longValue();
						site.get(con);
						DBUserPwdResetHis his = new DBUserPwdResetHis();
						long prh_id = new Long(sidArr[0]).longValue();
						String usr_id = sidArr[1];
	
						int link_max_days = ((UserManagement) wizbini.cfgOrgUserManagement
								.get(String.valueOf(site.ste_id)))
								.getForgetPassword().getLinkLastDays();
						int max_attempted = ((UserManagement) wizbini.cfgOrgUserManagement
								.get(String.valueOf(site.ste_id)))
								.getForgetPassword().getMaxAttempt();
						if (!his.checkPwdResetLink(con, prh_id, link_max_days,
								max_attempted, usr_id)) {
							sysMsg = getErrorMsg("677", modParam.getUrl_failure());
						} else {
							resultJson.put(PROF_ATTR_LST, wizbini.getUsrMgtJson(
									prof.root_id, prof.cur_lan));
						}

					}else if(modParam.getCmd().equalsIgnoreCase("reset_pwd")){
						String sid = modParam.getSid();
						sid = dbRegUser.decrypt(sid, new StringBuffer(
								FORGET_PWD_ENCRYPTIONKEY).reverse().toString());
						String sidArr[] = cwUtils.splitToString(sid, "|_|");
						acSite site = new acSite();
						site.ste_ent_id = new Long(sidArr[3]).longValue();
						site.get(con);
						DBUserPwdResetHis his = new DBUserPwdResetHis();
						long prh_id = new Long(sidArr[0]).longValue();
						String usr_id = sidArr[1];	
						int link_max_days = ((UserManagement) wizbini.cfgOrgUserManagement
								.get(String.valueOf(site.ste_id)))
								.getForgetPassword().getLinkLastDays();
						int max_attempted = ((UserManagement) wizbini.cfgOrgUserManagement
								.get(String.valueOf(site.ste_id)))
								.getForgetPassword().getMaxAttempt();
						int attempted = his.getAttempted(con, prh_id);
						if(!usr_id.equals(modParam.getUsr_id())){
							if(attempted < max_attempted){
								his.updStutusAndAttempted(con, prh_id, attempted, max_attempted);								
								sysMsg = getErrorMsg("678", modParam.getUrl_failure());
							}else{
								sysMsg = getErrorMsg("679", modParam.getUrl_success());
							}
						}else{
							if (!his.checkPwdResetLink(con, prh_id, link_max_days,
									max_attempted, usr_id)) {
								sysMsg = getErrorMsg("678", modParam.getUrl_failure());
							}else{
								String pwd =modParam.getUsr_pwd();
								dbRegUser dbUsr = new dbRegUser();
								dbUsr.usr_ste_usr_id =usr_id;
								dbUsr.usr_ent_id = dbUsr.getEntIdStatusOk(con, usr_id, site.ste_ent_id);
								dbUsr.usr_pwd =pwd;
								dbUsr.updPwd(con,prof);
								his.updStutusAndAttempted(con, prh_id, attempted, max_attempted);
								sysMsg = getErrorMsg("680",modParam.getUrl_success());
							}
						}
						
					}else if (modParam.getCmd().equalsIgnoreCase("delete_user")) {
						if (modParam.getUsr_ent_id() > 0) {
							AcRegUser acusr = new AcRegUser(con);
							if (!acusr.hasDelPrivilege(prof.usr_ent_id, prof.root_ent_id, prof.current_role, modParam.getUsr_ent_id())) {
								sysMsg = getErrorMsgByStatus(ServletModule.MSG_ERROR, "MSG002", modParam.getUrl_success());
								return;
							} else {
								dbRegUser usr = new dbRegUser();
								usr.usr_ent_id = modParam.getUsr_ent_id();
								usr.get(con);

								if (dbRegUser.USR_STATUS_DELETED.equalsIgnoreCase(usr.usr_status)) {
									DeleteUser.deleteUser(con, modParam.getUsr_ent_id());
									//如果开放了cpd功能 删除用户相关的CPD数据
									if(AccessControlWZB.hasCPDFunction()){
										CpdDbUtilsForOld.delUserCpd(con, usr.usr_ent_id);
									}
									con.commit();
									
									ObjectActionLog log = new ObjectActionLog(usr.usr_ent_id, 
											usr.usr_ste_usr_id,
											usr.usr_display_bil,
											ObjectActionLog.OBJECT_TYPE_USR,
											ObjectActionLog.OBJECT_ACTION_DEL,
											ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
											prof.getUsr_ent_id(),
											prof.getUsr_last_login_date(),
											prof.getIp()
									);
									SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
									
								}
							}
						}
						sysMsg = getErrorMsg("USR008", modParam.getUrl_success());
					} else if (modParam.getCmd().equalsIgnoreCase("all_delete_user")) {
						
						String id_list = modParam.getId_lst();
						String ent_id_str[] = id_list.split("~");
						int hasDeleteFail = 0;
						AcRegUser acusr = new AcRegUser(con);
						for(int i=0; i<ent_id_str.length;i++){
							modParam.setUsr_ent_id(Long.parseLong(ent_id_str[i]));	
							if (modParam.getUsr_ent_id() > 0) {
								boolean canMgtUser = acusr.canMgtUser(prof, modParam.getUsr_ent_id() , wizbini.cfgTcEnabled);
								if (!canMgtUser) {
									hasDeleteFail++;
									continue ;
								} else {
									dbRegUser usr = new dbRegUser();
									usr.usr_ent_id = modParam.getUsr_ent_id();
									usr.get(con);
		
									if (dbRegUser.USR_STATUS_DELETED.equalsIgnoreCase(usr.usr_status)) {
										DeleteUser.deleteUser(con, modParam.getUsr_ent_id());
										//如果开放了cpd功能 删除用户相关的CPD数据
										if(AccessControlWZB.hasCPDFunction()){
											CpdDbUtilsForOld.delUserCpd(con, usr.usr_ent_id);
										}
										con.commit();
										
										ObjectActionLog log = new ObjectActionLog(usr.usr_ent_id, 
												usr.usr_ste_usr_id,
												usr.usr_display_bil,
												ObjectActionLog.OBJECT_TYPE_USR,
												ObjectActionLog.OBJECT_ACTION_DEL,
												ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
												prof.getUsr_ent_id(),
												prof.getUsr_last_login_date(),
												prof.getIp()
										);
										SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
									}
								}
							}
							if(hasDeleteFail==0)
								sysMsg = getErrorMsg("AUSR008", modParam.getUrl_success());
							else
								sysMsg = getErrorMsg("AUSR040", String.valueOf(hasDeleteFail) , modParam.getUrl_success());
						}
					}else{
						throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
					}
				}

		} catch (qdbException e) {
			throw new cwException(e.getMessage(), e);
		} 
	}
}

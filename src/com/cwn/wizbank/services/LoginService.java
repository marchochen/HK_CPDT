package com.cwn.wizbank.services;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.entity.AcSite;
import com.cwn.wizbank.entity.LoginLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.WebMessage;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.AcSiteMapper;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.UUIDGenerator;
import com.cwn.wizbank.wechat.service.WechatService;

@Service
public class LoginService extends BaseService<RegUser> {

	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	@Autowired
	APITokenService apiTokenService;
	
	@Autowired
	AcSiteMapper acSiteMapper;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	RegUserMapper regUserMapper;
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	WebMessageService webMessageService;
	
	@Autowired
	WechatService wechatService;

	public String login(Model model,HttpServletRequest request, loginProfile prof, WizbiniLoader wizbini,
			String usr_ste_usr_id, String usr_pwd, String userOpenId, String login_role,
			long site_id, String developer, boolean isRemember, String login_lan) throws qdbException, cwException, SQLException, cwSysMessage {

		String loginStatus = "";
		
		if(regUserService.checkUserNameCount(usr_ste_usr_id)){
			loginStatus = dbRegUser.CODE_USER_SYSTEM_ISSUE;
		} else {		
			loginStatus = forCallOldAPIService.login(null, prof, wizbini,
					usr_ste_usr_id, usr_pwd, login_role, site_id, developer,
					isRemember, login_lan);
		} 

		APIToken atk = null;
		if (dbRegUser.CODE_LOGIN_SUCCESS.equals(loginStatus)) {
			if(APIToken.API_DEVELOPER_WEIXIN.equals(developer)){
				atk = doWechatLoginBind(prof, usr_ste_usr_id, userOpenId, developer);
				
				LoginLog loginLog = new LoginLog((int)prof.getUsr_ent_id(),prof.usr_ste_usr_id,prof.usr_display_bil,LoginLog.MODE_WECHAT,request.getRemoteAddr(),prof.login_date,LoginLog.USR_LOGIN_STATUS_SUCCESS,null);
                SystemLogContext.saveLog(loginLog, SystemLogTypeEnum.LOGIN_ACTION_LOG);
			}else{

				atk = apiTokenService.getTokenBySteId(usr_ste_usr_id, developer, userOpenId);
				
				if(atk == null){
					atk = insertAPIToken(prof, usr_ste_usr_id, userOpenId,
							developer);
				} else {
					atk.setAtk_wechat_open_id(userOpenId);
					atk.setAtk_expiry_timestamp(getExpireDate());
					apiTokenService.update(atk);
				}
				LoginLog loginLog = new LoginLog((int)prof.getUsr_ent_id(),prof.usr_ste_usr_id,prof.usr_display_bil,LoginLog.MODE_APP,request.getRemoteAddr(),prof.login_date,LoginLog.USR_LOGIN_STATUS_SUCCESS,null);
                SystemLogContext.saveLog(loginLog, SystemLogTypeEnum.LOGIN_ACTION_LOG);
			}
			
			if(forCallOldAPIService.isNeedChangePwd(null, usr_ste_usr_id)) {
				model.addAttribute("status",  dbRegUser.CODE_CHANGE_PWD);
			}else{
				model.addAttribute("status", loginStatus);
			}
			model.addAttribute("token", atk == null ? "" : atk.getAtk_id());
			
			
		}else{
			model.addAttribute("status", loginStatus);
			int loginMode = LoginLog.MODE_APP;
			if(APIToken.API_DEVELOPER_WEIXIN.equals(developer)){
				loginMode = LoginLog.MODE_WECHAT;
			}
			//登录失败  记录log
			LoginLog loginLog = new LoginLog((int)prof.getUsr_ent_id(),usr_ste_usr_id,prof.usr_display_bil,loginMode,request.getRemoteAddr(),prof.login_date,LoginLog.USR_LOGIN_STATUS_FAIL,loginStatus);
            SystemLogContext.saveLog(loginLog, SystemLogTypeEnum.LOGIN_ACTION_LOG);
		}
		return loginStatus;
		
	}

	private APIToken doWechatLoginBind(loginProfile prof,
			String usr_ste_usr_id, String userOpenId, String developer) {
		APIToken atk;
		//删除微信客户端之前绑定的账号
		apiTokenService.deleteByWechatOpenId(userOpenId);
		
		if(Application.MULTIPLE_LOGIN){//如果允许多人登录
			atk = insertAPIToken(prof, usr_ste_usr_id, userOpenId,
					developer);
		}else{//如果不允许多人登录
			
			//获取已经绑定该用户的记录列表
			List<APIToken> userBindList = apiTokenService.getList(usr_ste_usr_id, developer, null);
			for(APIToken a : userBindList){
				//提醒之前绑定的微信客户端
				sendMessageToWechatClient(a.getAtk_usr_ent_id());
				//删除之前的绑定
				apiTokenService.delete(a.getAtk_id());
			}
			
			//添加新的绑定
			atk = insertAPIToken(prof, usr_ste_usr_id, userOpenId,
					developer);
		}
		return atk;
	}

	private APIToken insertAPIToken(loginProfile prof, String usr_ste_usr_id,
			String userOpenId, String developer) {
		APIToken atk = new APIToken();
		atk.setAtk_id(UUIDGenerator.generateUUID());
		atk.setAtk_create_timestamp(getDate());
		atk.setAtk_developer_id(developer);
		atk.setAtk_expiry_timestamp(getExpireDate());
		atk.setAtk_usr_ent_id(prof.usr_ent_id);
		atk.setAtk_usr_id(usr_ste_usr_id);
		atk.setAtk_wechat_open_id(userOpenId);
		apiTokenService.add(atk);
		return atk;
	}
	
	private void sendMessageToWechatClient(long usr_ent_id) {
		//微信提示不用国际化
		String subject = "系统提示";
		String content = "由于你在其它地方绑定微信号绑定了该帐号，原先的绑定已被取消。如果非本人操作，请尽快登录到平台更新密码";
		
		//获取超级管理员的id，作为发送者
		AcSite as = acSiteMapper.get(1L);
		long sysAdminUsrEntId = as.getSte_default_sys_ent_id();
		
		WebMessage persistedMessage = webMessageService.insertMessage(sysAdminUsrEntId, usr_ent_id, subject, content, "SYS");
		wechatService.pushMsgToWechat(sysAdminUsrEntId, usr_ent_id, Long.parseLong(persistedMessage.getWmsg_id()), subject, content);
	}

	public Date getExpireDate(){
		Calendar cld = Calendar.getInstance();
		cld.setTime(getDate());
		cld.add(Calendar.YEAR, 10);
		return cld.getTime(); 
		
	}
	
	
	public String login(Model model,HttpServletRequest request, loginProfile prof, WizbiniLoader wizbini,
			String usr_ste_usr_id, String usr_pwd, String login_role,
			long site_id, String developer, boolean isRemember, String loginLan) throws qdbException, cwException, SQLException, cwSysMessage {
		return this.login(model,request, prof, wizbini, usr_ste_usr_id, usr_pwd, null, login_role, site_id, developer, isRemember, loginLan);
	}
	
	
	
    public void initLoginProfile(loginProfile prof, String usr_id, long usr_ent_id
    		,long siteId
    		) throws MessageException {
        try {
            prof.usr_ent_id = usr_ent_id;
            prof.usr_id = usr_id;

            AcSite as = acSiteMapper.get(siteId);
            
            prof.root_ent_id = as.getSte_ent_id();
            prof.root_id = as.getSte_id();
            prof.usr_id = "s" + as.getSte_ent_id() + "u" + usr_ent_id;
            prof.usr_ste_usr_id = usr_id;

            prof.current_role = AccessControlWZB.ROL_EXT_ID_NLRN;
            prof.encoding = qdbAction.getWizbini().cfgSysSetupadv.getEncoding();
            prof.label_lan = "GB2312";
            prof.cur_lan = prof.getCurLan(prof.label_lan);
            prof.login_date = regUserMapper.getLastLoginDate(usr_ent_id);
            
			prof.my_top_tc_id = tcTrainingCenterService.getTopTcrId(qdbAction.getWizbini(), usr_ent_id);
			
			prof.usr_status = regUserMapper.get(usr_ent_id).getUsr_status();

        } catch (Exception e) {
        	CommonLog.error(e.getMessage(),e);
            throw new MessageException("Error in get acSite or set Prof : " + e.getMessage());
        }
    }
    
    
	/**
	 * 从ApiToken中获取数据初始化prof
	 */
	public void initProfFromToken (loginProfile prof, String atk_id) throws MessageException {
		
		APIToken atk = apiTokenService.getById(atk_id);
		Date curTime = getDate();
		//无效的
		if(atk == null || atk.getAtk_usr_id() == null || atk.getAtk_usr_id().length() <= 0){
			throw new MessageException("ERROR_TOKEN_INVALID");
		}
		//删除已经过期的Token
		if(atk.getAtk_expiry_timestamp().before(curTime)){
			apiTokenService.delete(atk_id);
			throw new MessageException("ERROR_TOKEN_INVALID");
		}
	     
	    initLoginProfile(prof, atk.getAtk_usr_id(), atk.getAtk_usr_ent_id(), 1);
		atk.setAtk_expiry_timestamp(getExpireDate());
		apiTokenService.update(atk);
		
	}
}

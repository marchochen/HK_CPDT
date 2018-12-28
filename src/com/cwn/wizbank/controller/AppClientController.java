package com.cwn.wizbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AppClient;
import com.cwn.wizbank.services.AppClientService;

/**
 * 移动端获取推送配置信息的controller
 *
 * @author andrew.xiao 2016/8/10
 *
 */
@Controller("appClientController")
@RequestMapping("appClient")
public class AppClientController {

	@Autowired
	private AppClientService appClientService;
	
	public AppClientService getAppClientService() {
		return appClientService;
	}

	public void setAppClientService(AppClientService appClientService) {
		this.appClientService = appClientService;
	}


	@RequestMapping("")
	@ResponseBody
	public void insertOrUpdate(AppClient appClient,loginProfile profile){
		if(appClient == null || profile == null){
			return;
		}
		appClient.setUsrEntId(profile.usr_ent_id);
		appClientService.insertOrUpdate(appClient);
	}
	
	/**
	 * 更新客户端状态
	 * @param appClient
	 */
	@RequestMapping("updateStatus")
	@ResponseBody
	public void updateStatus(AppClient appClient,loginProfile profile){
		if(appClient == null || profile == null){
			return;
		}
		appClient.setUsrEntId(profile.usr_ent_id);
		appClientService.updateStatus(appClient);
	}
	
	
	/**
	 * 删除客户端记录
	 * @param appClient
	 */
	@RequestMapping("delete")
	@ResponseBody
	public void delete(AppClient appClient,loginProfile profile){
		if(appClient == null || profile == null){
			return;
		}
		appClient.setUsrEntId(profile.usr_ent_id);
		appClientService.delete(appClient);
	}

}

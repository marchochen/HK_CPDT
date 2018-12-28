package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AppClient;
import com.cwn.wizbank.persistence.AppClientMapper;

/**
 *  记录用户app客户端情况的服务，用于推送
 *  
 *  @andrew.xiao 2016/8/9
 */

@Service
public class AppClientService extends BaseService<AppClient>{

	@Autowired
	private AppClientMapper appClientMapper;
	
	public AppClientMapper getAppClientMapper() {
		return appClientMapper;
	}

	public void setAppClientMapper(AppClientMapper appClientMapper) {
		this.appClientMapper = appClientMapper;
	}

	/**
	 * 根据用户Id获取有效的AppClient记录：有效：用户id对应的token不过期（即APIToken表中的token不过期），AppClient的状态为在线状态
	 * @param usrEntId 用户Id
	 * @return AppClient集合
	 */
	public List<AppClient> getValidRecodeByUsrEntId(long usrEntId) {
		List<Long> usrEntIdList = new ArrayList<Long>();
		usrEntIdList.add(usrEntId);
		return this.getValidRecodeByUsrEntIdList(usrEntIdList);
	}

	/**
	 * 根据用户Id集合获取有效的AppClient记录：有效：用户id对应的token不过期（即APIToken表中的token不过期），AppClient的状态为在线状态
	 * @param usrEntIdList 用户Id集合
	 * @return AppClient集合
	 */
	public List<AppClient> getValidRecodeByUsrEntIdList(List<Long> usrEntIdList) {
		if(usrEntIdList == null || usrEntIdList.size() == 0){
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usrEntIdList", usrEntIdList);
		params.put("curTime",new Date());
		return appClientMapper.getValidRecodeByUsrEntIdList(params);
	}

	public void insertOrUpdate(AppClient appClient) {
		
		appClient.setStatus(AppClient.STATUS_ONLINE);
		appClientMapper.deleteByUsrEntIdOrClientId(appClient);
		appClientMapper.insert(appClient);
	}
	

	/**
	 * 更新客户端状态
	 * @param appClient
	 */
	public void updateStatus(AppClient appClient) {
		appClientMapper.updateStatus(appClient);
	}

	/**
	 * 删除客户端记录
	 * @param appClient
	 */
	public void delete(AppClient appClient) {
		appClientMapper.delete(appClient);
	}

}

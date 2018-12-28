package com.cwn.wizbank.persistence;


import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AppClient;

/**
 * AppClient Mapper
 * @author Andrew 2016/6/9
 *
 */
public interface AppClientMapper extends BaseMapper<AppClient>{


	/**
	 * 根据用户Id集合获取AppClient
	 * @param params Map，里面包含用户Id集合，和当前时间
	 * @return AppClient集合
	 */
	List<AppClient> getValidRecodeByUsrEntIdList(Map<String,Object> params);
	
	
	/**
	 * 通过Clientid，手机标识，appId查找对象
	 * @param client
	 * @return
	 */
	AppClient getByClientIdAndMobileIndAndAppIdAndStatus(AppClient client);

	/**
	 * 更新客户端状态
	 * @param appClient
	 */
	void updateStatus(AppClient appClient);
	
	/**
	 * 根据用户Id或者ClientId删除记录
	 * @param appClient
	 */
	void deleteByUsrEntIdOrClientId(AppClient appClient);
	
	
	/**
	 * 删除客户端记录
	 * @param appClient
	 */
	void delete(AppClient exitAppClient);


}
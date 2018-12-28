package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.APIToken;


public interface APITokenMapper extends BaseMapper<APIToken>{


	APIToken get(String id);

	List<APIToken> selectList(Map<String, Object> params);
	
	void delete(String id);
	
	List<APIToken> getByWechatOpenId(String id);

	List<APIToken> getByWechatEntId(String usr_ent_id);

	/**
	 * 根据用户和微信openid删除微信token记录
	 * @param params
	 */
	void deleteByWechatOpenId(Map<String, Object> params); 

}
package com.cwn.wizbank.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.persistence.APITokenMapper;
/**
 *  service 实现
 */
@Service
public class APITokenService extends BaseService<APIToken> {

	@Autowired
	APITokenMapper apiTokenMapper;

	public APIToken getById(String id) {
		return apiTokenMapper.get(id);
	}
	
	public void delete(String id){
		apiTokenMapper.delete(id);
	}
	
	public APIToken getTokenBySteId(String userSteEntId, String developer, String userOpenId){
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userSteEntId", userSteEntId);
		params.put("developer", developer);
		params.put("userOpenId", userOpenId);
		List<APIToken> list = apiTokenMapper.selectList(params);
		
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public void setAPITokenMapper(APITokenMapper apiTokenMapper){
		this.apiTokenMapper = apiTokenMapper;
	}


	public APIToken getByWechatOpenId(String userOpenId) {
		List<APIToken> list = apiTokenMapper.getByWechatOpenId(userOpenId);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	public APIToken getByWechatEntId(String usr_ent_id) {
		List<APIToken> list = apiTokenMapper.getByWechatEntId(usr_ent_id);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 根据用户和微信openid删除微信token记录
	 * @param userSteEntId
	 * @param userOpenId
	 */
	public void deleteByWechatOpenId(String userOpenId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userOpenId", userOpenId);
		apiTokenMapper.deleteByWechatOpenId(params);
	}
	
	public List<APIToken> getList(String userSteEntId, String developer, String userOpenId){
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("userSteEntId", userSteEntId);
		params.put("developer", developer);
		params.put("userOpenId", userOpenId);
		List<APIToken> list = apiTokenMapper.selectList(params);
		
		return list == null ? new ArrayList<APIToken>() : list;
	}
}
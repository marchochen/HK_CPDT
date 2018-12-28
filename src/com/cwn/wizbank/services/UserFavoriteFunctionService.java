package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserFavoriteFunction;
import com.cwn.wizbank.persistence.UserFavoriteFunctionMapper;
/**
 *  service 实现
 */
@Service
public class UserFavoriteFunctionService extends BaseService<UserFavoriteFunction> {

	@Autowired
	UserFavoriteFunctionMapper userFavoriteFunctionMapper;

	public List<UserFavoriteFunction> getList(long userEntId, String roleExtId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("roleExtId", roleExtId);
		map.put("userEntId", userEntId);
		return userFavoriteFunctionMapper.selectList(map);
	}

	public void addFavorite(long usrEntId, String roleExtId, int funId) {
		UserFavoriteFunction uff = new UserFavoriteFunction();
		uff.setUff_create_datetime(getDate());
		uff.setUff_fun_id(funId);
		uff.setUff_usr_ent_id(usrEntId);
		uff.setUff_role_ext_id(roleExtId);
		userFavoriteFunctionMapper.insert(uff);
	}
	
	public void addMtpFavorite(long userEntId, String roleExtId, int[] funIds) {
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("userEntId", userEntId);
		
		map.put("roleExtId", roleExtId);
	
		List<UserFavoriteFunction> userFavoriteFunctions = userFavoriteFunctionMapper.selectList(map);
		
		if(userFavoriteFunctions != null)
		{
			for(UserFavoriteFunction userFavoriteFunction : userFavoriteFunctions )
			{
				deleteFavorite(userFavoriteFunction.getUff_usr_ent_id(), userFavoriteFunction.getUff_fun_id());
			}
		}
		
		for(int fun : funIds) {
			map = new HashMap<String,Object>();
			map.put("userEntId", userEntId);
			map.put("funId", fun);
			addFavorite(userEntId, roleExtId, fun);
		}
	}
	
	public void deleteFavorite(long userEntId, int funId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("funId", funId);
		map.put("userEntId", userEntId);
		userFavoriteFunctionMapper.deleteFavorite(map);
	}
	
}
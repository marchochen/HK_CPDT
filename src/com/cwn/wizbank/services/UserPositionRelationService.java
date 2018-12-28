package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserPositionRelation;
import com.cwn.wizbank.persistence.UserPositionRelationMapper;

/**
 * service 实现
 */
@Service
public class UserPositionRelationService extends BaseService<UserPositionRelation> {

	@Autowired
	public UserPositionRelationMapper userPositionRelationMapper;

	public void update(UserPositionRelation positionRelation,long usr_ent_id){
		userPositionRelationMapper.update(positionRelation);
	}
	public int getCountById(String id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("upt_upc_id", id);
		return userPositionRelationMapper.getCountById(map);
	}
	public void batchDel(String ids){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("ids", ids);
		userPositionRelationMapper.batchDel(map);
	}
}
package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserPositionLrnItem;
import com.cwn.wizbank.persistence.UserPositionLrnItemMapper;

/**
 * 岗位学习地图相关课程service 实现
 */
@Service
public class UserPositionLrnItemService extends BaseService<UserPositionLrnItem> {

	@Autowired
	UserPositionLrnItemMapper userPositionLrnItemMapper;
	public void add(UserPositionLrnItem userPositionLrnItem){
		userPositionLrnItemMapper.add(userPositionLrnItem);
	}
	public List<UserPositionLrnItem> getItemByMapList(long upi_upm_id){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("upi_upm_id", upi_upm_id);
		return userPositionLrnItemMapper.getItemByMapList(map);
	}
	public void deleteByUpmId(long upm_id){
		userPositionLrnItemMapper.deleteByUpmId(upm_id);
	}
}
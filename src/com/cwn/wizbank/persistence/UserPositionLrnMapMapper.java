package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserPositionLrnMap;
import com.cwn.wizbank.utils.Page;

public interface UserPositionLrnMapMapper  extends BaseMapper<UserPositionLrnMap>{
	
	
	public Long add(UserPositionLrnMap userPositionLrnMap);
	public List<UserPositionLrnMap> getPositionMapList(Page<UserPositionLrnMap> page);
	public int getCountById(Map<String,Object> map);
	public List<UserPositionLrnMap> getPostFrontMapList(Map<String,Object> map);
	public UserPositionLrnMap getByFieldName(Map<String,Object> map);
	public List<UserPositionLrnMap> getPositionMapNotOneList(Map<String,Object> map);
	public boolean updateStatus(Map<String,Object> map);
	public List<UserPositionLrnMap> getPositionMapFrontList(Page<UserPositionLrnMap> page);
	public int getPositionMapNotOneListSize(Map<String,Object> map);
}

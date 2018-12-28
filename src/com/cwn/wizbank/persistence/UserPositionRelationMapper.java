package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.UserPositionRelation;

public interface UserPositionRelationMapper  extends BaseMapper<UserPositionRelation>{
	public int getCountById(Map<String,Object> map);
	public void batchDel(Map<String,Object> map);
}

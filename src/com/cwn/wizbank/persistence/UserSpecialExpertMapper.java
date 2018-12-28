package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.UserSpecialExpert;

public interface UserSpecialExpertMapper  extends BaseMapper<UserSpecialExpert>{
	
	public Long add(UserSpecialExpert userSpecialExpert);
	public void deleteByUstId(long id);
	public List<UserSpecialExpert> getExpertsByUstId(Map<String, Object> map);
}

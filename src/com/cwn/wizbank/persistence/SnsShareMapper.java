package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.SnsShare;


public interface SnsShareMapper extends BaseMapper<SnsShare>{

	SnsShare get(long targetId, long usr_ent_id);

	SnsShare getByUserId(Map<String, Object> map);
	
	void delByTargetIdAndModule(SnsShare snsShare);
	void delErrorData(Map<String, Object> map);


}
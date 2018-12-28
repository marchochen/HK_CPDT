package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AeItemAccess;


public interface AeItemAccessMapper extends BaseMapper<AeItemAccess>{

	List<AeItemAccess> getInstructorsByItmId(long itmId);
	
	List<AeItemAccess> hasInstructorsByItmId(long itmId);

	List<AeItemAccess> getSoleAccessItem(Map<String, Object> map);

	void delByEntId(long usr_ent_id);
	
	List<Long> getUniqueInstructorsByItmId(long itmId);
}
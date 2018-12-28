package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeItemAccess;
import com.cwn.wizbank.persistence.AeItemAccessMapper;
/**
 *  service 实现
 */
@Service
public class AeItemAccessService extends BaseService<AeItemAccess> {

	@Autowired
	AeItemAccessMapper aeItemAccessMapper;

	public void setAeItemAccessMapper(AeItemAccessMapper aeItemAccessMapper){
		this.aeItemAccessMapper = aeItemAccessMapper;
	}

	public AeItemAccessMapper getAeItemAccessMapper() {
		return aeItemAccessMapper;
	}

	public List<AeItemAccess> getSoleAccessItem(long usr_ent_id, String adm_ext_id) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("usr_ent_id", usr_ent_id);
		map.put("iac_access_id", adm_ext_id);
		return this.aeItemAccessMapper.getSoleAccessItem(map);
	}
	
	public List<AeItemAccess> getInstructorsByItmId(long itmId){
		return this.aeItemAccessMapper.hasInstructorsByItmId(itmId);
	}
	
}
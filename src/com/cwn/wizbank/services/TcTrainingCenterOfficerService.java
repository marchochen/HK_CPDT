package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.TcTrainingcenterofficer;
import com.cwn.wizbank.persistence.TcTrainingcenterofficerMapper;
/**
 *  service 实现
 */
@Service
public class TcTrainingCenterOfficerService extends BaseService<TcTrainingcenterofficer> {

	@Autowired
	TcTrainingcenterofficerMapper tcTrainingCenterOfficerMapper;

	public void delOfficerRoleFromTc(long usr_ent_id, String rol_ext_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		tcTrainingCenterOfficerMapper.delOfficerRoleFromTc(map);
	}
}
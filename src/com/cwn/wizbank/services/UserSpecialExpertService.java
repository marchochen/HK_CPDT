package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.entity.UserSpecialExpert;
import com.cwn.wizbank.persistence.UserSpecialExpertMapper;
import com.cwn.wizbank.utils.ImageUtil;

/**
 * 专题对应专家service 实现
 */
@Service
public class UserSpecialExpertService extends BaseService<UserSpecialExpert> {

	@Autowired
	UserSpecialExpertMapper userSpecialExpertMapper;
	public void add(UserSpecialExpert userSpecialExpert){
		userSpecialExpertMapper.add(userSpecialExpert);
	}
	public void deleteByUstId(long use_ust_id){
		userSpecialExpertMapper.deleteByUstId(use_ust_id);
	}
	public List<UserSpecialExpert> getExpertsByUstId(WizbiniLoader wizbini,long use_ust_id,long top_tc_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("use_ust_id", use_ust_id);
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
		map.put("top_tc_id", top_tc_id);
		}
		List<UserSpecialExpert> specialExperts=userSpecialExpertMapper.getExpertsByUstId(map);
		for (UserSpecialExpert userSpecialExpert : specialExperts) {
			userSpecialExpert.setNum(specialExperts.size());
			ImageUtil.combineImagePath(userSpecialExpert);
		}
		return specialExperts;
	}
}
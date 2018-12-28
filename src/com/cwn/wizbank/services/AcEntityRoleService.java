package com.cwn.wizbank.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AcEntityRole;
import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.persistence.AcEntityRoleMapper;
import com.cwn.wizbank.utils.Page;

/**
 * service 实现
 */
@Service
public class AcEntityRoleService extends BaseService<AcFunction> {

	@Autowired
	AcEntityRoleMapper acEntityRoleMapper;
	
	/**
	 * 判断是否拥有某个角色
	 * @param usr_ent_id
	 * @param rol_ext_id
	 * @return
	 */
	public boolean hasRole(long usr_ent_id, String rol_ext_id){
		boolean flag = false;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("usr_ent_id", usr_ent_id);
		params.put("rol_ext_id", rol_ext_id);
		flag = acEntityRoleMapper.hasRole(params);
		return flag;
	}
	
	
	
}
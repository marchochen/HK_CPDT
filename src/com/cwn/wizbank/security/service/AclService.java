package com.cwn.wizbank.security.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.persistence.AclMapper;
import com.cwn.wizbank.utils.CommonLog;

@Service
public class AclService {
	@Autowired
	AclMapper aclMapper;

	public boolean hasAnyPermission(String rol_ext_id, String... permissions) {
		
		String permission_s = "";
		if (permissions == null) {
			return true;
		} else {
			if (StringUtils.isEmpty(rol_ext_id)) {
				return false;
			}
		}
		boolean hasPermission = false;
		for (String permission : permissions) {
			Map<String, Object> map = new HashMap<String, Object>();
			permission_s += permission;
			map.put("rol_ext_id", rol_ext_id);
			map.put("ftn_ext_id", permission);
			if (aclMapper.hasPermission(map)) {
				hasPermission = true;
				break;
			}
			
		}
		
     if(hasPermission){
			
		}else{
//			System.out.println("[访问权限] : role =" +rol_ext_id + " ftn="+ permission_s + "  是否通过 ： " + hasPermission );
			try{
				//throw new Exception ("[访问权限] : role =" +rol_ext_id + " ftn="+ permission_s + "  是否通过 ： " + hasPermission );
			}catch (Exception e){
				CommonLog.error(e.getMessage(),e);
			}
			
			
		}
		return hasPermission;
	};
	
	
	public boolean hasAnyPermission(long userEntId, String... permissions) {
		if (permissions == null) {
			return true;
		} else {
			if (userEntId < 0) {
				return false;
			}
		}
		boolean hasPermission = false;
		for (String permission : permissions) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userEntId", userEntId);
			map.put("ftnExtId", permission);
			if (aclMapper.hasUserPermission(map)) {
				hasPermission = true;
				break;
			}
			//System.out.println("[访问权限] : " + permission + "  是否通过 ： " + hasPermission);
		}
		return hasPermission;
	};

    public boolean hasCPDFunction(Map<String, Object> map){
    	String result = aclMapper.hasCPDFunction(map);
		if(result == null){
			return false;
		}
		Integer rInt = Integer.valueOf(result);
		return rInt==1?true:false;
    }
}

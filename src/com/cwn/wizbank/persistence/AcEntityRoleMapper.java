package com.cwn.wizbank.persistence;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.entity.AcEntityRole;
import com.cwn.wizbank.utils.Page;

public interface AcEntityRoleMapper extends BaseMapper<AcEntityRole>{

	void delByUsrEntId(long usrEntId);
	
	boolean hasRole(Map<String, Object> params);

}

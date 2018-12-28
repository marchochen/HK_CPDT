package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UsrRoleTargetEntity;
import com.cwn.wizbank.persistence.UsrRoleTargetEntityMapper;
/**
 *  service 实现
 */
@Service
public class UsrRoleTargetEntityService extends BaseService<UsrRoleTargetEntity> {

	@Autowired
	UsrRoleTargetEntityMapper usrRoleTargetEntityMapper;

	public void setUsrRoleTargetEntityMapper(UsrRoleTargetEntityMapper usrRoleTargetEntityMapper){
		this.usrRoleTargetEntityMapper = usrRoleTargetEntityMapper;
	}
}
package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.CreditsType;
import com.cwn.wizbank.persistence.CreditsTypeMapper;
/**
 *  service 实现
 */
@Service
public class CreditsTypeService extends BaseService<CreditsType> {

	@Autowired
	CreditsTypeMapper creditsTypeMapper;

	public boolean hasCreditsTypeByCode(String cty_code){
		if(creditsTypeMapper.getCreditsTypeByCode(cty_code) != null){
			return true;
		}
		return false;
	}
	
	public void setCreditsTypeMapper(CreditsTypeMapper creditsTypeMapper){
		this.creditsTypeMapper = creditsTypeMapper;
	}
}
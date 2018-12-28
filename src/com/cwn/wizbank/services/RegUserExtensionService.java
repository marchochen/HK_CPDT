package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.RegUserExtension;
import com.cwn.wizbank.persistence.RegUserExtensionMapper;
/**
 *  service 实现
 */
@Service
public class RegUserExtensionService extends BaseService<RegUserExtension> {

	@Autowired
	RegUserExtensionMapper regUserExtensionMapper;

	public void setRegUserExtensionMapper(RegUserExtensionMapper regUserExtensionMapper){
		this.regUserExtensionMapper = regUserExtensionMapper;
	}
}
package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeItemExtension;
import com.cwn.wizbank.persistence.AeItemExtensionMapper;
/**
 *  service 实现
 */
@Service
public class AeItemExtensionService extends BaseService<AeItemExtension> {

	@Autowired
	AeItemExtensionMapper aeItemExtensionMapper;

	public void setAeItemExtensionMapper(AeItemExtensionMapper aeItemExtensionMapper){
		this.aeItemExtensionMapper = aeItemExtensionMapper;
	}
}
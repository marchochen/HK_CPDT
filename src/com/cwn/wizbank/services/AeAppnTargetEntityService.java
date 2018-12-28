package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeAppnTargetEntity;
import com.cwn.wizbank.persistence.AeAppnTargetEntityMapper;
/**
 *  service 实现
 */
@Service
public class AeAppnTargetEntityService extends BaseService<AeAppnTargetEntity> {

	@Autowired
	AeAppnTargetEntityMapper aeAppnTargetEntityMapper;

	public void setAeAppnTargetEntityMapper(AeAppnTargetEntityMapper aeAppnTargetEntityMapper){
		this.aeAppnTargetEntityMapper = aeAppnTargetEntityMapper;
	}
}
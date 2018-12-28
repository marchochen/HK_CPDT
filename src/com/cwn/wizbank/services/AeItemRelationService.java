package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeItemRelation;
import com.cwn.wizbank.persistence.AeItemRelationMapper;
/**
 *  service 实现
 */
@Service
public class AeItemRelationService  extends BaseService<AeItemRelation> {

	@Autowired
	AeItemRelationMapper aeItemRelationMapper;

	
	
	public void setAeItemRelationMapper(AeItemRelationMapper aeItemRelationMapper){
		this.aeItemRelationMapper = aeItemRelationMapper;
	}
}
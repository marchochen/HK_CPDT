package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeTreeNodeRelation;
import com.cwn.wizbank.persistence.AeTreeNodeRelationMapper;
/**
 *  service 实现
 */
@Service
public class AeTreeNodeRelationService extends BaseService<AeTreeNodeRelation> {

	@Autowired
	AeTreeNodeRelationMapper aeTreeNodeRelationMapper;

	public void setAeTreeNodeRelationMapper(AeTreeNodeRelationMapper aeTreeNodeRelationMapper){
		this.aeTreeNodeRelationMapper = aeTreeNodeRelationMapper;
	}
}
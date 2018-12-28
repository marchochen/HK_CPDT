package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.IntegCompleteCondition;
import com.cwn.wizbank.persistence.IntegCompleteConditionMapper;
/**
 *  service 实现
 */
@Service
public class IntegCompleteConditionService extends BaseService<IntegCompleteCondition> {

	@Autowired
	IntegCompleteConditionMapper integCompleteConditionMapper;

	public void setIntegCompleteConditionMapper(IntegCompleteConditionMapper integCompleteConditionMapper){
		this.integCompleteConditionMapper = integCompleteConditionMapper;
	}
}
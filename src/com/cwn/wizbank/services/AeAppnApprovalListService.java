package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeAppnApprovalList;
import com.cwn.wizbank.persistence.AeAppnApprovalListMapper;
/**
 *  service 实现
 */
@Service
public class AeAppnApprovalListService extends BaseService<AeAppnApprovalList> {

	@Autowired
	AeAppnApprovalListMapper aeAppnApprovalListMapper;

	public void setAeAppnApprovalListMapper(AeAppnApprovalListMapper aeAppnApprovalListMapper){
		this.aeAppnApprovalListMapper = aeAppnApprovalListMapper;
	}

	public AeAppnApprovalListMapper getAeAppnApprovalListMapper() {
		return aeAppnApprovalListMapper;
	}
	
	
}
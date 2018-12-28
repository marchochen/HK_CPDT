package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeCatalogAccess;
import com.cwn.wizbank.persistence.AeCatalogAccessMapper;
/**
 *  service 实现
 */
@Service
public class AeCatalogAccessService extends BaseService<AeCatalogAccess> {

	@Autowired
	AeCatalogAccessMapper aeCatalogAccessMapper;

	public void setAeCatalogAccessMapper(AeCatalogAccessMapper aeCatalogAccessMapper){
		this.aeCatalogAccessMapper = aeCatalogAccessMapper;
	}
}
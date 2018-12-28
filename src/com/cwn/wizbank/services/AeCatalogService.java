package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeCatalog;
import com.cwn.wizbank.persistence.AeCatalogMapper;
/**
 *  service 实现
 */
@Service
public class AeCatalogService extends BaseService<AeCatalog> {

	@Autowired
	AeCatalogMapper aeCatalogMapper;

	public void setAeCatalogMapper(AeCatalogMapper aeCatalogMapper){
		this.aeCatalogMapper = aeCatalogMapper;
	}
}
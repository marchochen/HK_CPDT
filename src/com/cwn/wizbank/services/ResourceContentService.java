package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.ResourceContent;
import com.cwn.wizbank.persistence.ResourceContentMapper;
/**
 *  service 实现
 */
@Service
public class ResourceContentService extends BaseService<ResourceContent> {

	@Autowired
	ResourceContentMapper ResourceContentMapper;

	public void setResourceContentMapper(ResourceContentMapper ResourceContentMapper){
		this.ResourceContentMapper = ResourceContentMapper;
	}
}
package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.Template;
import com.cwn.wizbank.persistence.TemplateMapper;
/**
 *  service 实现
 */
@Service
public class TemplateService extends BaseService<Template> {

	@Autowired
	TemplateMapper templateMapper;

	public void setTemplateMapper(TemplateMapper templateMapper){
		this.templateMapper = templateMapper;
	}
}
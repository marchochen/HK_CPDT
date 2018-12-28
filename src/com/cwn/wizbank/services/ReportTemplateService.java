package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.ReportTemplate;
import com.cwn.wizbank.persistence.ReportTemplateMapper;
/**
 *  service 实现
 */
@Service
public class ReportTemplateService extends BaseService<ReportTemplate> {

	@Autowired
	ReportTemplateMapper ReportTemplateMapper;

	public void setReportTemplateMapper(ReportTemplateMapper ReportTemplateMapper){
		this.ReportTemplateMapper = ReportTemplateMapper;
	}
}
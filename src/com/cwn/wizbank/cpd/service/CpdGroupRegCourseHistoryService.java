package com.cwn.wizbank.cpd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.CpdGroupRegCourseHistory;
import com.cwn.wizbank.persistence.CpdGroupRegCourseHistoryMapper;
import com.cwn.wizbank.services.BaseService;

@Service
public class CpdGroupRegCourseHistoryService  extends BaseService<CpdGroupRegCourseHistory>{

	@Autowired
	CpdGroupRegCourseHistoryMapper cpdGroupRegCourseHistoryMapper;
}

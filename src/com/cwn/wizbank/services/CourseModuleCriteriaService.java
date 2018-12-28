package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.CourseModuleCriteria;
import com.cwn.wizbank.persistence.CourseModuleCriteriaMapper;
/**
 *  service 实现
 */
@Service
public class CourseModuleCriteriaService extends BaseService<CourseModuleCriteria> {

	@Autowired
	CourseModuleCriteriaMapper courseModuleCriteriaMapper;

	public void setCourseModuleCriteriaMapper(CourseModuleCriteriaMapper courseModuleCriteriaMapper){
		this.courseModuleCriteriaMapper = courseModuleCriteriaMapper;
	}
}
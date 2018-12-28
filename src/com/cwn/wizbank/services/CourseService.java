package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.Course;
import com.cwn.wizbank.persistence.CourseMapper;
/**
 *  service 实现
 */
@Service
public class CourseService extends BaseService<Course> {

	@Autowired
	CourseMapper courseMapper;

	public void setCourseMapper(CourseMapper courseMapper){
		this.courseMapper = courseMapper;
	}
}
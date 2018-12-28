package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.Course;


public interface CourseMapper extends BaseMapper<Course>{

	public Long getResIdByItmId(long itmId);

	public Course getCourseByItmId(long itmId);

}
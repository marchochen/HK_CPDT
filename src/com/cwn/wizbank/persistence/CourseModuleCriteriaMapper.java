package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.CourseModuleCriteria;


public interface CourseModuleCriteriaMapper extends BaseMapper<CourseModuleCriteria>{

	List<CourseModuleCriteria> getCourseModuleCriteriaByResId(String cmr_res_id);



}
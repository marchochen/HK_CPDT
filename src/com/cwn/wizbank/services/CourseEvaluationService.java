package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.CourseEvaluation;
import com.cwn.wizbank.persistence.CourseEvaluationMapper;
/**
 *  service 实现
 */
@Service
public class CourseEvaluationService extends BaseService<CourseEvaluation> {

	@Autowired
	CourseEvaluationMapper courseEvaluationMapper;

	
	public String getCourseEvaluationStatus(long tkhId, long userEntId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("thkId", tkhId);
		params.put("userEntId", userEntId);
		return courseEvaluationMapper.getCourseEvaluationStatus(params);
	}
	
	public void updateLastAccessTime(long cov_cos_id, long cov_ent_id, long cov_tkh_id){
		CourseEvaluation cov = new CourseEvaluation();
		//cov.setCov_commence_datetime(getDate());
        cov.setCov_cos_id(cov_cos_id);
        cov.setCov_ent_id(cov_ent_id);
        cov.setCov_tkh_id(cov_tkh_id);
        cov.setCov_commence_datetime(getDate());
        courseEvaluationMapper.updateCommenceTime(cov);

	}
	
	public void updateCommenceTime(long cov_cos_id, long cov_ent_id, long cov_tkh_id){
		CourseEvaluation cov = new CourseEvaluation();
		cov.setCov_last_acc_datetime(getDate());
        cov.setCov_cos_id(cov_cos_id);
        cov.setCov_ent_id(cov_ent_id);
        cov.setCov_tkh_id(cov_tkh_id);
        courseEvaluationMapper.updateLastAccessTime(cov);
	}
	
	public void setCourseEvaluationMapper(CourseEvaluationMapper courseEvaluationMapper){
		this.courseEvaluationMapper = courseEvaluationMapper;
	}

	public CourseEvaluationMapper getCourseEvaluationMapper() {
		return courseEvaluationMapper;
	}
}
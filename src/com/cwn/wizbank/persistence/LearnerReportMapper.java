package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.vo.CatalogTreeVo;
import com.cwn.wizbank.entity.vo.LearnerReportParamVo;
import com.cwn.wizbank.entity.vo.LearnerDetailReportVo;


public interface LearnerReportMapper extends BaseMapper<LearnerDetailReportVo>{

	public List<LearnerDetailReportVo> getLearnerReportByUser(LearnerReportParamVo param);
	
	public List<Map> getUserGroupData(Map param);
	
	public List<Map> getUserGradeData(Map param);
	
	public List<Map> getAttemptCount();
	
	public List<LearnerDetailReportVo> getLearnerReportByCourse(LearnerReportParamVo param);
	
	public List<CatalogTreeVo> getCourseCatalogs();
}

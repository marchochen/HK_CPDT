package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CourseEvaluation;


public interface CourseEvaluationMapper extends BaseMapper<CourseEvaluation>{

	public String getCourseEvaluationStatus(Map<String ,Object> map);
	
	public CourseEvaluation getCourseEvaluationByThkId(long tkhId);

	/**
	 * 更新最后访问时间
	 * @param cov
	 */
	public void updateLastAccessTime(CourseEvaluation cov);
	
	/**
	 * 更新开始时间
	 * @param cov
	 */
	public void updateCommenceTime(CourseEvaluation cov);
	
	public List<CourseEvaluation> getCourseEvaluationByApp(Map map);

    /**
     * 使状态为完成
     * @param cov
     */
    public void completeCourse(CourseEvaluation cov);
	
	
}
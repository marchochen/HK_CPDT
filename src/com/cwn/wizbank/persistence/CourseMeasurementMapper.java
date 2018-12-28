package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CourseMeasurement;


public interface CourseMeasurementMapper extends BaseMapper<CourseMeasurement>{

	List<CourseMeasurement> getCourseMeasurementWithLrn(Map<String ,Object> map);
	List<CourseMeasurement> getCourseMeasurement(long ccr_id);
	List<CourseMeasurement> getCourseMeasurementByCmtTitle(String cmt_title);
}
package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.CourseMeasurement;
import com.cwn.wizbank.persistence.CourseMeasurementMapper;
/**
 *  service 实现
 */
@Service
public class CourseMeasurementService extends BaseService<CourseMeasurement> {

	@Autowired
	CourseMeasurementMapper courseMeasurementMapper;

	public void setCourseMeasurementMapper(CourseMeasurementMapper courseMeasurementMapper){
		this.courseMeasurementMapper = courseMeasurementMapper;
	}
}
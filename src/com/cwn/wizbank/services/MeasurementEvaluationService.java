package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.MeasurementEvaluation;
import com.cwn.wizbank.persistence.MeasurementEvaluationMapper;
/**
 *  service 实现
 */
@Service
public class MeasurementEvaluationService extends BaseService<MeasurementEvaluation> {

	@Autowired
	MeasurementEvaluationMapper measurementEvaluationMapper;

	public void setMeasurementEvaluationMapper(MeasurementEvaluationMapper measurementEvaluationMapper){
		this.measurementEvaluationMapper = measurementEvaluationMapper;
	}
}
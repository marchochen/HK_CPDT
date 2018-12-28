package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.ModuleEvaluation;


public interface ModuleEvaluationMapper extends BaseMapper<ModuleEvaluation>{
	
	ModuleEvaluation getById(Map map);

}
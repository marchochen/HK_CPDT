package com.cwn.wizbank.services;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.ModuleEvaluation;
import com.cwn.wizbank.entity.Resources;
import com.cwn.wizbank.persistence.ModuleEvaluationMapper;
/**
 *  service 实现
 */
@Service
public class ModuleEvaluationService extends BaseService<ModuleEvaluation> {

	@Autowired
	ModuleEvaluationMapper moduleEvaluationMapper;

	@Autowired
	ResourcesService resourcesService;
	
	public void setModuleEvaluationMapper(ModuleEvaluationMapper moduleEvaluationMapper){
		this.moduleEvaluationMapper = moduleEvaluationMapper;
	}
	
	public ModuleEvaluation getByIds(long modId,long tkhId,long userId){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("modId", modId);
		params.put("tkhId", tkhId);
		params.put("userId", userId);
		ModuleEvaluation moduleEvaluation = moduleEvaluationMapper.getById(params);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("tkh_id", tkhId);
		map.put("res_id", modId);
		List<Resources> list = resourcesService.getSubmitNum(map);
		if(null != list && list.size() > 0){
			if(null != list.get(0).getSubmit_num()){
				moduleEvaluation.setMov_total_attempt(Integer.valueOf(list.get(0).getSubmit_num().toString()));
			}
		}
		return  moduleEvaluation;
	}

}
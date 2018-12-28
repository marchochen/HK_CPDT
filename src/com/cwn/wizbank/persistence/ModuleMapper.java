package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.Module;
import com.cwn.wizbank.utils.Page;


public interface ModuleMapper extends BaseMapper<Module>{

	List<Module> getPublicEvaluation(Page<Module> page);

	List<Module> selectModuleDetail(Page<Module> page);
	
	Module selectModuleById(Page<Module> page);
	
	Module selectExamTotalById(Module Module);
	
	Module selectTopicTotalById(Module Module);

	Module getModTypeById(long mod_id);
	
}
package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;



import com.cwn.wizbank.entity.Resources;
import com.cwn.wizbank.utils.Page;


public interface ResourcesMapper extends BaseMapper<Resources>{
	
	List<Resources> getCosContent(Map<String ,Object> map);
	
	List<Resources> getCosContentWithLrn(Map<String ,Object> map);
	
	List<Resources> selectMyEvaluation(Page<Resources> page);
	
	Integer selectMyEvaluationCount(Map<String,Object> map);
	
	/**
	 * 根据subType 和 userEntId 获取用户没有完成的模块
	 * @param map
	 * @return
	 */
	List<Resources> selectModuleList(Page<Resources> page);

	List<Resources> getSubmitNum(Map<String,Object> map);
	
}
package com.cwn.wizbank.services;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.Module;
import com.cwn.wizbank.persistence.ModuleMapper;
import com.cwn.wizbank.utils.Page;
/**
 * service 实现
 */
@Service
public class ModuleService extends BaseService<Module> {

	@Autowired
	ModuleMapper moduleMapper;
	
	public List<Module> getPublicEvaluation(Page<Module> page, long userEntId, long tcrId , int isMobile){
		page.getParams().put("userEntId", userEntId);
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("isMobile", isMobile);
		page.getParams().put("curDate", getDate());
		return moduleMapper.getPublicEvaluation(page);
	}
	
	/**
	 * 获取模块情况
	 * @param page
	 * @param res_id
	 * @return
	 */
	public List<Module> getModuleDetail(Page<Module> page, long mod_id){
		//项目反馈，改方法碰到数据库多，查询慢，所以将SQL分成3个来查询，提高效率
		page.getParams().put("mod_id", mod_id);
		Module module=moduleMapper.selectModuleById(page);
		Module examTotal=moduleMapper.selectExamTotalById(module);
		Module TopicTotal= moduleMapper.selectTopicTotalById(module);
		module.setExam_total(examTotal.getExam_total());
		module.setTopic_total(TopicTotal.getTopic_total());
		page.getResults().clear();
		page.getResults().add(module);
		return page.getResults();
	}
	
	public Module getModTypeById(long mod_id){
		return moduleMapper.getModTypeById(mod_id);
	}

	public void setModuleMapper(ModuleMapper moduleMapper) {
		this.moduleMapper = moduleMapper;
	}
	
	public Timestamp getDatabaseTime(){
		return moduleMapper.getDate();
	}
}
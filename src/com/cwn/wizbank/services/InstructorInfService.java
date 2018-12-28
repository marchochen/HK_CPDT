package com.cwn.wizbank.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.InstructorCos;
import com.cwn.wizbank.entity.InstructorInf;
import com.cwn.wizbank.persistence.InstructorInfMapper;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class InstructorInfService extends BaseService<InstructorInf> {

	@Autowired
	InstructorInfMapper instructorInfMapper;
	
	@Autowired
	InstructorCosService instructorCosService;


	public Page<InstructorInf> page(Page<InstructorInf> page){
		instructorInfMapper.page(page);
		List<InstructorInf> list = page.getResults();
		for(InstructorInf instr : list) {
			ImageUtil.combineImagePath(instr);
		}
		return page;
	}

	public Page<InstructorCos> pageCos(Page<InstructorCos> page, long instrId) {
		page.getParams().put("instrId", instrId);
		instructorCosService.pageCos(page);
		return page;
	}

	public InstructorInf getInstructorDetail(long id, long usr_ent_id) {
		InstructorInf instr = instructorInfMapper.getInstructor(id);
		ImageUtil.combineImagePath(instr);
		return instr;
	}
	public Page<InstructorInf> getInstructors(Page<InstructorInf> page){
		if(null!= page.getParams().get("searchContent")){
			String searchText = String.valueOf(page.getParams().get("searchContent"));
			page.getParams().put("searchContent",  "%" + searchText.trim().toLowerCase() + "%");
		}
		
		instructorInfMapper.getInstructors(page);
		List<InstructorInf> list = page.getResults();
		for(InstructorInf instr : list) {
			ImageUtil.combineImagePath(instr);
		}
		return page;
	}
}
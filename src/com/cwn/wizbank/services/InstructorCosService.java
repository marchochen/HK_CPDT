package com.cwn.wizbank.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.InstructorCos;
import com.cwn.wizbank.persistence.InstructorCosMapper;
import com.cwn.wizbank.utils.Page;
/**
 *  service 实现
 */
@Service
public class InstructorCosService extends BaseService<InstructorCos> {

	@Autowired
	InstructorCosMapper instructorCosMapper;

	
	List<InstructorCos> getInstructorCos(long userEntId){
		return instructorCosMapper.getCosList(userEntId);
	}


	public Page<InstructorCos> pageCos(Page<InstructorCos> page) {
		instructorCosMapper.pageCos(page);		
		return page;
	}
}
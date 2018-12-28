package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.InstructorInf;
import com.cwn.wizbank.utils.Page;


public interface InstructorInfMapper extends BaseMapper<InstructorInf>{

	InstructorInf getInstructor(long id);
	List<InstructorInf> getInstructors(Page<InstructorInf> page);
}
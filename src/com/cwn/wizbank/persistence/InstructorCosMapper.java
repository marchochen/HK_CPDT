package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.InstructorCos;
import com.cwn.wizbank.utils.Page;


public interface InstructorCosMapper extends BaseMapper<InstructorCos>{

	List<InstructorCos> getCosList(long userEntId);

	List<AeItem> pageCos(Page<InstructorCos> page);

}
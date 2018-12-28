package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.CourseCriteria;


public interface CourseCriteriaMapper extends BaseMapper<CourseCriteria>{
	CourseCriteria getByItm(long itm_id);

}
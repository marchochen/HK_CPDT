package com.cwn.wizbank.cpdt.persistence;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.cpdt.entity.CpdtGroup;
import com.cwn.wizbank.persistence.BaseMapper;

public interface CpdtGroupMapper extends BaseMapper<CpdtGroup> {
	
	CpdtGroup getGroupCode(@Param("cgCode") String cgCode);

}

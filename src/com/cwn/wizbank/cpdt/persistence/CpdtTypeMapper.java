package com.cwn.wizbank.cpdt.persistence;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.cpdt.entity.CpdtType;
import com.cwn.wizbank.persistence.BaseMapper;

public interface CpdtTypeMapper extends BaseMapper<CpdtType> {

	CpdtType getTypeByCode(@Param("ctLicenseType") String ctLicenseType);
	
}

package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.AcFunction;


public interface AcFunctionMapper extends BaseMapper<AcFunction> {

	
	public AcFunction getByExtId(String extId);
	public AcFunction get(long ftn_id);
	public List<AcFunction> getAllFunctions(String role);

	public void deleteAll();
	public void truncate();

}
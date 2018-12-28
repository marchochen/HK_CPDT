package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CpdGroupPeriod;
import com.cwn.wizbank.utils.Page;

public interface CpdGroupPeriodMapper extends BaseMapper<CpdGroupPeriod>{

	public List<CpdGroupPeriod> getPeriod(Map map);
	
	public List<CpdGroupPeriod> searchAll(Page<CpdGroupPeriod> page);
	
	public void delete(CpdGroupPeriod cpdGroupPeriod);
	
	public boolean isExistForTime(CpdGroupPeriod cpdGroupPeriod);
	
	public int insertReturnID(CpdGroupPeriod cpdGroupPeriod);
}

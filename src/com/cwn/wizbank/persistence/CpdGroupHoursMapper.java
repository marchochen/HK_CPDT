package com.cwn.wizbank.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.entity.CpdGroupHours;

public interface CpdGroupHoursMapper extends BaseMapper<CpdGroupHours>{

	public List<CpdGroupHours> getHoursByPeriod(@Param(value="cgp_id")Long cgp_id);
	
	public void deleteByCgpId(CpdGroupHours cpdGroupHours);
	
	public List<CpdGroupHours> getByCghCgpID(Long id);
	
}

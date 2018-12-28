package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CpdGroupRegHours;

public interface CpdGroupRegHoursMapper extends BaseMapper<CpdGroupRegHours> {

	public List<CpdGroupRegHours> getByCgrId(Long cgrId);
	
	public void deleteByCgrId(Long cgrId);
	
	public Boolean isExist(CpdGroupRegHours cpdGroupRegHours);
	
	public void updateHours(CpdGroupRegHours cpdGroupRegHours);
	
	public List<CpdGroupRegHours>  getCpdGroupRegHours(Map map);
}

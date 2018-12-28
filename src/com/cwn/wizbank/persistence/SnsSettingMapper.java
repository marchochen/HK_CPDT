package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.SnsSetting;


public interface SnsSettingMapper extends BaseMapper<SnsSetting>{

	SnsSetting selectSnsSet(Map<String, Long> map);
	
}
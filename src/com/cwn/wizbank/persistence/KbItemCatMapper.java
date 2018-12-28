package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.KbItemCat;

public interface KbItemCatMapper extends BaseMapper<KbItemCat> {
	public void updateKbcId(Map<String, Object> map);
}
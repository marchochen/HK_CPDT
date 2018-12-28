package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.SnsValuation;


public interface SnsValuationMapper extends BaseMapper<SnsValuation>{

	int getCount(Map<String, Object> params);

	void updateScore(SnsValuation valuation);
	
	void delValuation(Map<String, Object> params);


}
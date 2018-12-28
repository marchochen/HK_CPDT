package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.SnsCount;


public interface SnsCountMapper extends BaseMapper<SnsCount>{

	SnsCount getByTargetInfo(Map<String,Object> map);

	void deleteRecord(Map<String, Object> map);
	void delErrorData(Map<String, Object> map);


}
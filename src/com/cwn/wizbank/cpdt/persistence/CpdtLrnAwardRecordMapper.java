package com.cwn.wizbank.cpdt.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.cpdt.entity.CpdtLrnAwardRecord;
import com.cwn.wizbank.persistence.BaseMapper;

public interface CpdtLrnAwardRecordMapper extends BaseMapper<CpdtLrnAwardRecord> {
	
	List<CpdtLrnAwardRecord> queryAlreadyAward(Map<String,Object> params);

}

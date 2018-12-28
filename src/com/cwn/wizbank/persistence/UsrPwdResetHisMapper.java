package com.cwn.wizbank.persistence;

import java.util.Map;

import com.cwn.wizbank.entity.UsrPwdResetHis;

public interface UsrPwdResetHisMapper extends BaseMapper<UsrPwdResetHis> {

	UsrPwdResetHis selectUsrPwdResetHis(Map<String, Object> map);
	
	void updatePrhStatusN(long prh_id);
	
}

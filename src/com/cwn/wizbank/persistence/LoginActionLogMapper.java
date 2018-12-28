package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.LoginLog;


public interface LoginActionLogMapper extends BaseMapper<LoginLog> {
	
	void saveLog(Map<String ,Object> map);
    
	void delOneYear(Map<String ,Object> map);
	
	void createLoginLogTableByDate(Map<String ,Object> map);
	
	int existTable(String tableName);
	
	List<LoginLog> selectByUptId(Map<String ,Object> map);
	
	List<LoginLog> selectByGrpIdOrUgrId(Map<String ,Object> map);
	
	List<Map> getUserLoginLog(Map<String ,Object> map);
	
    List<Map> getPerfWarningLog(Map map);
}

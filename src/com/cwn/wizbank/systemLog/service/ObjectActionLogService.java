package com.cwn.wizbank.systemLog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.persistence.ObjectActionLogMapper;

@Service
public class ObjectActionLogService extends SystemActionLogService<ObjectActionLog>{
	
	@Autowired
	ObjectActionLogMapper objectActionLogMapper;

	@Override
	void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveLog(ObjectActionLog objectActionLog) {
		objectActionLogMapper.saveLog(objectActionLog);
		
	}


}

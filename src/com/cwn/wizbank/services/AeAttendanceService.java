package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeAttendance;
import com.cwn.wizbank.persistence.AeAttendanceMapper;
/**
 *  service 实现
 */
@Service
public class AeAttendanceService extends BaseService<AeAttendance> {

	@Autowired
	AeAttendanceMapper aeAttendanceMapper;

	public void setAeAttendanceMapper(AeAttendanceMapper aeAttendanceMapper){
		this.aeAttendanceMapper = aeAttendanceMapper;
	}
	
	public AeAttendanceMapper getAeAttendanceMapper() {
		return aeAttendanceMapper;
	}
	
}
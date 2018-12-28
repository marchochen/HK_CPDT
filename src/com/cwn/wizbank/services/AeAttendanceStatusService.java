package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeAttendanceStatus;
import com.cwn.wizbank.persistence.AeAttendanceStatusMapper;
/**
 *  service 实现
 */
@Service
public class AeAttendanceStatusService extends BaseService<AeAttendanceStatus> {

	@Autowired
	AeAttendanceStatusMapper aeAttendanceStatusMapper;

	public void setAeAttendanceStatusMapper(AeAttendanceStatusMapper aeAttendanceStatusMapper){
		this.aeAttendanceStatusMapper = aeAttendanceStatusMapper;
	}
}
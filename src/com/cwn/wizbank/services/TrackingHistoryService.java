package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.Course;
import com.cwn.wizbank.entity.TrackingHistory;
import com.cwn.wizbank.persistence.TrackingHistoryMapper;
/**
 *  service 实现
 */
@Service
public class TrackingHistoryService extends BaseService<Course> {

	@Autowired
	TrackingHistoryMapper trackingHistoryMapper;
	
}
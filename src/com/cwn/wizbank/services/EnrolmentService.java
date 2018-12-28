package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.Enrolment;
import com.cwn.wizbank.persistence.EnrolmentMapper;
/**
 * service 实现
 */
@Service
public class EnrolmentService extends BaseService<Enrolment> {

	@Autowired
	EnrolmentMapper enrolmentMapper;


	public void setEnrolmentMapper(EnrolmentMapper enrolmentMapper) {
		this.enrolmentMapper = enrolmentMapper;
	}
}
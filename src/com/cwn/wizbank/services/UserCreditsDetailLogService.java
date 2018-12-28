package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserCreditsDetailLog;
import com.cwn.wizbank.persistence.UserCreditsDetailLogMapper;
/**
 *  service 实现
 */
@Service
public class UserCreditsDetailLogService extends BaseService<UserCreditsDetailLog> {

	@Autowired
	UserCreditsDetailLogMapper userCreditsDetailLogMapper;

	public void setUserCreditsDetailLogMapper(UserCreditsDetailLogMapper userCreditsDetailLogMapper){
		this.userCreditsDetailLogMapper = userCreditsDetailLogMapper;
	}
}
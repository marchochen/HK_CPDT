package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.UserGrade;
import com.cwn.wizbank.persistence.UserGradeMapper;
/**
 * service 实现
 */
@Service
public class UserGradeService extends BaseService<UserGrade> {

	@Autowired
	UserGradeMapper userGradeMapper;

}
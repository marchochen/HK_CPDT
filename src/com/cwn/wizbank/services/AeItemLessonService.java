package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeItemLesson;
import com.cwn.wizbank.persistence.AeItemLessonMapper;
/**
 *  service 实现
 */
@Service
public class AeItemLessonService extends BaseService<AeItemLesson> {

	@Autowired
	AeItemLessonMapper aeItemLessonMapper;

	public void setAeItemLessonMapper(AeItemLessonMapper aeItemLessonMapper){
		this.aeItemLessonMapper = aeItemLessonMapper;
	}
}
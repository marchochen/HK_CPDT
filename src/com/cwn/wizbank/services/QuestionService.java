package com.cwn.wizbank.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.Question;
import com.cwn.wizbank.persistence.QuestionMapper;
/**
 *  service 实现
 */
@Service
public class QuestionService extends BaseService<Question> {

	@Autowired
	QuestionMapper QuestionMapper;

	public void setQuestionMapper(QuestionMapper QuestionMapper){
		this.QuestionMapper = QuestionMapper;
	}
}
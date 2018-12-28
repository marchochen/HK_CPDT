package com.cwn.wizbank.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.HelpQuestion;
import com.cwn.wizbank.persistence.HelpQuestionMapper;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

@Service
public class HelpQuestionService extends BaseService<HelpQuestion>{
	
	@Autowired
	HelpQuestionMapper helpQuestionMapper;
	
	public Page<HelpQuestion> getQuestionList(Page<HelpQuestion> page){
		 helpQuestionMapper.getQuestionList(page);
		 return page;
	}
	
	public HelpQuestion getQuestion(HelpQuestion question){
		return helpQuestionMapper.getQuestion(question);
	}
	
	public Integer checkNumber(HelpQuestion question){
		return helpQuestionMapper.checkNumber(question);
	}
	
	public void publishQuestion(Integer id , Integer publishAct){
		HelpQuestion que = new HelpQuestion();
		que.setHq_id(id);
		HelpQuestion question = helpQuestionMapper.getQuestion(que);
		//question.setHq_is_publish(publishAct);
		helpQuestionMapper.update(question);
	}
	
	public void save(HelpQuestion question){
		Date date=new Date(System.currentTimeMillis());
		question.setHq_create_timestamp(date);
		//add(question);
		helpQuestionMapper.insert(question);
	}
	
	public HelpQuestion updateQuestion(Integer id , HelpQuestion vo){
		HelpQuestion que = new HelpQuestion();
		que.setHq_id(id);
		HelpQuestion question = helpQuestionMapper.getQuestion(que);
		Date date=new Date(System.currentTimeMillis());
		question.setHq_update_timestamp(date);
		question.setHq_content_cn(vo.getHq_content_cn());
		question.setHq_content_us(vo.getHq_content_us());
		question.setHq_title(vo.getHq_title());
		question.setHq_type_id(vo.getHq_type_id());
		question.setHq_width(vo.getHq_width());
		question.setHq_height(vo.getHq_height());
		question.setHq_template(vo.getHq_template());
		question.setHqt_number(vo.getHqt_number());
		
		helpQuestionMapper.update(question);
		return question;
	}
	
	public void delHelpQuestion(Integer id){
		helpQuestionMapper.delHelpQuestion(id);
	}

}

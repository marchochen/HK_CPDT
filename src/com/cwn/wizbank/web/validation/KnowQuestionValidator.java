package com.cwn.wizbank.web.validation;

import org.springframework.validation.Errors;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.KnowQuestion;

public class KnowQuestionValidator extends WzbValidator {
	String captcha = null;

	public KnowQuestionValidator(loginProfile prof) {
		super(prof.cur_lan);
	}

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return KnowQuestion.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors e) {
		WzbValidationUtils.validateEmpty(e, "que_title", getLabel("338"));
		//WzbValidationUtils.validateLength(e, "que_content", 500, getLabel("339")+500);  
	}
}
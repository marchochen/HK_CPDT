package com.cwn.wizbank.web.validation;

import org.springframework.validation.Errors;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.SnsGroup;

public class SnsGroupValidator extends WzbValidator {
	String captcha = null;

	public SnsGroupValidator(loginProfile prof) {
		super(prof.cur_lan);
	}

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return SnsGroup.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors e) {
		WzbValidationUtils.validateEmpty(e, "s_grp_title", getLabel("338"));
		WzbValidationUtils.validateLength(e, "s_grp_title", 80, getLabel("339")+80);
	}
}
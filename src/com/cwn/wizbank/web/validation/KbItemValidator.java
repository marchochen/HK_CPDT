package com.cwn.wizbank.web.validation;

import org.springframework.validation.Errors;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.KbItem;

public class KbItemValidator extends WzbValidator {
	String captcha = null;

	public KbItemValidator(loginProfile prof) {
		super(prof.cur_lan);
	}

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return KbItem.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors e) {
		WzbValidationUtils.validateEmpty(e, "kbi_title", getLabel("338"));
		WzbValidationUtils.validateLength(e, "kbi_title", 255, getLabel("339")+255);
		WzbValidationUtils.validateEmpty(e, "kbi_content", getLabel("338"));
		WzbValidationUtils.validateEmpty(e, "kbi_catalog_ids", getLabel("338"));
		WzbValidationUtils.validateEmpty(e, "kbi_desc", getLabel("338"));
		WzbValidationUtils.validatePatten(e, "kbi_desc", "^[\\s\\S]{1," + 255 + "}$", getLabel("339")+255);
	}
}
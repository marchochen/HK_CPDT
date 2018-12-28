package com.cwn.wizbank.web.validation;

import org.springframework.validation.Errors;

import com.cwn.wizbank.entity.AcRole;

public class AcRoleValidator extends WzbValidator {

	public AcRoleValidator(String curLang) {
		super(curLang);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return AcRole.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		WzbValidationUtils.validateEmpty(e, "rol_title", getLabel("338"));
		//WzbValidationUtils.validateLength(e, "rol_title", 10, getLabel("339")+10);
		WzbValidationUtils.validateEmpty(e, "acFunction.ftn_id", getLabel("338"));
	}

}

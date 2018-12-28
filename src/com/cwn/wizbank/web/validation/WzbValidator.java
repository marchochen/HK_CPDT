package com.cwn.wizbank.web.validation;

import org.springframework.validation.Validator;

import com.cw.wizbank.util.LangLabel;

public abstract class WzbValidator implements Validator {
	protected String curLang = null;

	public WzbValidator(String curLang) {
		this.curLang = curLang;
	}

	protected String getLabel(String code) {
		return LangLabel.getValue(curLang, code);
	}
}

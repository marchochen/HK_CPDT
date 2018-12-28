package com.cwn.wizbank.web.validation;

import org.springframework.validation.Errors;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.utils.LabelContent;

public class KbCatalogValidator extends WzbValidator {
	String captcha = null;
	protected String curLang = null;

	public KbCatalogValidator(loginProfile prof) {
		super(prof.cur_lan);
		this.curLang = prof.cur_lan;
	}

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return KbCatalog.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors e) {
		WzbValidationUtils.validateEmpty(e, "kbc_title", getLabel("338"));
		WzbValidationUtils.validateInputLength(e, "kbc_title", 20, LabelContent.get(curLang, "label_core_knowledge_management_17") + LabelContent.get(curLang, "label_title_length_warn_20"));
		WzbValidationUtils.validateInputLength(e, "kbc_desc", 400, LabelContent.get(curLang, "label_core_knowledge_management_34") + LabelContent.get(curLang, "label_title_length_warn_400"));  
		WzbValidationUtils.validateEmpty(e, "tcTrainingCenter.tcr_id", getLabel("338"));
	}
}
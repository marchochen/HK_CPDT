package com.cwn.wizbank.web.validation;

import org.springframework.validation.Errors;

import com.cwn.wizbank.entity.Tag;
import com.cwn.wizbank.utils.LabelContent;

public class TagValidator extends WzbValidator {
	protected String curLang = null;

	public TagValidator(String curLang) {
		super(curLang);
		this.curLang = curLang;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Tag.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		WzbValidationUtils.validateEmpty(e, "tag_title", getLabel("338"));
		WzbValidationUtils.validateInputLength(e, "tag_title", 20, LabelContent.get(curLang, "label_core_knowledge_management_73") + LabelContent.get(curLang, "label_title_length_warn_20"));
		WzbValidationUtils.validateEmpty(e, "tcTrainingCenter.tcr_id", getLabel("338"));
	}

}

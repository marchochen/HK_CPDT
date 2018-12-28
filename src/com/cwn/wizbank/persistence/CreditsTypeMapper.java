package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.CreditsType;


public interface CreditsTypeMapper extends BaseMapper<CreditsType>{

	CreditsType getCreditsTypeByCode(String cty_code);

}
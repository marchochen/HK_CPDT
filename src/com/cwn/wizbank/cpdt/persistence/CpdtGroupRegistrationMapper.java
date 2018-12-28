package com.cwn.wizbank.cpdt.persistence;

import java.util.Map;

import com.cwn.wizbank.cpdt.entity.CpdtGroupRegistration;
import com.cwn.wizbank.persistence.BaseMapper;

public interface CpdtGroupRegistrationMapper extends BaseMapper<CpdtGroupRegistration> {

	CpdtGroupRegistration getNewestRegInfo(Map<String,Object> params);
	
}

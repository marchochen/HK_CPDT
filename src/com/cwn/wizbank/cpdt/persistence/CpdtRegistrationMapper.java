package com.cwn.wizbank.cpdt.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.cpdt.entity.CpdtRegistration;
import com.cwn.wizbank.cpdt.entity.CpdtType;
import com.cwn.wizbank.persistence.BaseMapper;

public interface CpdtRegistrationMapper extends BaseMapper<CpdtRegistration> {
	
	CpdtRegistration getNewestRegInfo(Map<String,Object> params);
	
    List<CpdtType> getCpdtType();

}

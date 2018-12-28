package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.ImsLog;
import com.cwn.wizbank.utils.Page;

public interface ImsLogMapper  extends BaseMapper<ImsLog>{

	public List<ImsLog> searchAll(Page<ImsLog> page);
	
    
}

package com.cwn.wizbank.persistence;

import java.util.Map;

public interface AclMapper {
	
	public boolean hasPermission(Map<String, Object> map);
	
	public boolean hasUserPermission(Map<String, Object> map);
	
	public String hasCPDFunction(Map<String, Object> map);
	
}

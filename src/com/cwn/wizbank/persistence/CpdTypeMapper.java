package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.utils.Page;

public interface CpdTypeMapper  extends BaseMapper<CpdType>{

	public boolean isExistForType(CpdType type);
	
	public List<CpdType> getCpdType(CpdType type);
	
	public int getMaxOrder();
	
	public List<CpdType> searchAll(Page<CpdType> page);
	
	public void delete(CpdType type);
	
	public void updateOrder(CpdType type);
    
    public void updLastEmailSendTime(CpdType type);
	
	public List<CpdType> getAllCptTypeAndGroup(Map<String ,Object> map);

    public List<CpdType> getCpdTypeOutStandingEmail(Map<String ,Object> map);

    public CpdType getTypeByCode(CpdType cpdType);

	public boolean isTypeExist(String temp);

	public boolean isGroupExist(Map<String ,Object> map);

	public boolean isGroupBelongLicense(Map<String, Object> map);
    
	
}

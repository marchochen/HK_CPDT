package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.utils.Page;

public interface CpdGroupMapper  extends BaseMapper<CpdGroup>{

	public boolean isExistForType(CpdGroup type);
	
	public CpdGroup getCpdGroup(CpdGroup type);
	
	public int getMaxOrder();
	
	public List<CpdGroup> searchAll(Page<CpdGroup> page);
	
	public void delete(CpdGroup type);
	
	public void updateOrder(CpdGroup type);
	
	public void deleteAllByCtID(Map<String,Object> map);
	
	public List<CpdGroup> getAllOrder(Map<String ,Object> map);

    public List<CpdGroup> getUserGroupRegi(Map<String,Object> params);
    
    public List<CpdGroup> searchByType(Map<String,Object> map);
    
    public int getCountGroupByCtId(long id);

    public CpdGroup getGroupByCode(CpdGroup cpdGroup);
    
    public CpdGroup getGroupCode(CpdGroup cpdGroup);
    
    
}

package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CpdGroupRegHours;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.utils.Page;

public interface CpdGroupRegistrationMapper extends BaseMapper<CpdGroupRegistration>{
	
	public List<CpdGroupRegistration> getCpdGroupRegBeforeTime(Map param);

    public List<CpdGroupRegistration> getPreGroupRegistrationNoId(Map param);
	
    public List<CpdGroupRegistration>  getByNowDate(Map<String, Object> params);
    
    public List<CpdGroupRegistration> getByUsrEntIdAndCrId(Page<CpdGroupRegistration> page);

    public List<CpdGroupRegistration> getCpdGroupReg(Map param);
    
    public List<CpdGroupRegistration> getCpdGroupRegNotPast(Map param);

    public List<CpdGroupRegistration> getCpdRegHoursNotPast(Map param);
    
    public List<CpdGroupRegistration> getCpdCrGroupExist(Map param);
    
    public int getCountByCgID(long cg_id);
    
    public List<CpdGroupRegistration>  getCpdGroupRegistration(Map map);

    public  CpdGroupRegistration getHoursDate(Map map);
    
    public  CpdGroupRegistration getHoursDateHistory(Map map);
    
    public  List<CpdGroupRegistration> getUserHours(Map map);

	public CpdRegistration getGroupRegistration(Map<String, Object> map);

	public Long getGroupId(Map map);
    
    public  CpdGroupRegistration isRegisterCpdgroupForImport(CpdGroupRegistration cpdGroupRegistration);
    

}

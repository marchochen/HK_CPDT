package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cw.wizbank.db.DbIMSLog;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.utils.Page;

public interface CpdRegistrationMapper extends BaseMapper<CpdRegistration>{
	
    public List<CpdRegistration> searchAll(Page<CpdRegistration> page);
    
    public List<CpdType> getCpdType();

    public CpdType getCpdTypeByid(long ct_id);

    public List<CpdGroup> getCpdGroupMap(long id);

    public CpdGroup getCpdGroupById(long cg_id);
    
    public CpdRegistration getDetail(long cr_id);

    public List<CpdRegistration> isExistForRegistration(CpdRegistration cpdRegistration);
    
    public List<CpdRegistration> isInfoRegistrationBydate(CpdRegistration cpdRegistration);
    
    public List<CpdRegistration> isInfoRegistrationBydateNul(CpdRegistration cpdRegistration);

    public CpdRegistration isRegisterForImport(CpdRegistration cpdRegistration);
    
    public void insertGroupRegi(CpdGroupRegistration cpdGroupRegistration);

    public List<CpdRegistration> getCpdRegistration(Map map);
    
    public List<CpdRegistration> getCpdRegistrationByPeriod(Map map);
    
    public void delCpdRegistration(Map<String,Object> params);

    public void delCpdGroupRegistration(Map<String,Object> params);
    
    public void updCpdGroupRegistration(CpdGroupRegistration cpdGroupRegistration);
    
    public int getCountByCtID(long ct_id);
    
    public List<CpdRegistration> getCpdLicenseRegistrationReport(Map<String,Object> map);

    public List<Long> getUsrCpdReg(Map<String,Object> map);

	public List<DbIMSLog> getIMSLog();

	public Long getCpdRegistrationByType(String license_type);

	public Long getCpdGroupRegistrationByType(Map<String, Object> map);

	public Long getCpdTypeId(Map<String, Object> map);

}

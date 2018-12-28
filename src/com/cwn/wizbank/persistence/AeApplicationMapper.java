package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.utils.Page;


public interface AeApplicationMapper extends BaseMapper<AeApplication>{

	List<AeApplication> getSignup(Page<AeApplication> page);
	
	List<AeApplication> getUsrApps(Map<String ,Object> map);
	
	AeApplication getByTkhId(long id);
	
	List<AeApplication> getSubordinateApprovalDetail(Page<AeApplication> page);
	
	Long getCount(long itmId);
	
	Long selectItemIdByTkhId(long tkh_id);

	int getSignupCount(Map<String, Object> map);

	
	
	long selectPedingAppCount(Map<String, Object> map);

	
	List<AeApplication> getMyCourse(Page<AeApplication> page);
	
	List<AeApplication>  getCItem(Map map);

    AeApplication getAeApplicationByitmUsrId(AeApplication aeApplication);

    List<AeApplication> getAeApplicationByitmAttend(AeApplication aeApplication);
    
	
}
package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.EnterpriseInfoPortal;
import com.cwn.wizbank.persistence.BaseMapper;

public interface EnterpriseInfoPortalMapper extends BaseMapper<EnterpriseInfoPortal> {
	public EnterpriseInfoPortal getEipByDomain(String domain);

	public EnterpriseInfoPortal getByCode(String eip_code);

	/**
	 * 获取所有企业列表
	 * @return
	 */
	public List<EnterpriseInfoPortal> getEnterpriseInfoPortalList();
	
	/**
	 * 根据培训中心获取直播并发数
	 * @param tcr_id
	 * @return
	 */
	public List<EnterpriseInfoPortal> getLiveMaxCountByTcrId(long tcr_id);
	
	public List<EnterpriseInfoPortal> getEnterpriseInfoPortalListByTcrId(long tcr_id);
	
}

package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.SystemStatistics;


/**
 * 数据统计Mapper
 * @author andrew.xiao
 *
 */
public interface SystemStatisticsMapper extends BaseMapper<SystemStatistics>{

	/**
	 * 根据企业培训中心ID获取SystemStatistic
	 * @param eipTcrID 企业培训中心ID
	 * @return
	 */
	SystemStatistics getSystemStatisticsByEipTcrId(Long eipTcrID);

	/**
	 * updateByEipTcrID
	 * @param ss 要更新的SystemStatistics，其中ssc_eip_tcr_id会作为更新条件
	 */
	void updateByEipTcrID(SystemStatistics ss);

}

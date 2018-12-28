package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.cpd.vo.IndividualReportVo;
import com.cwn.wizbank.entity.CpdLrnAwardRecord;
import com.cwn.wizbank.utils.Page;


public interface CpdLrnAwardRecordMapper extends BaseMapper<CpdLrnAwardRecord> {
	
	public List<CpdLrnAwardRecord> getCpdLrnAwardRecord(Map map);
	
	public void deleteCpdLrnAwardRecord(Map map);
	
	public com.cwn.wizbank.cpd.vo.CpdHourVO sumAwardHours(Map map);
	
    public com.cwn.wizbank.cpd.vo.CpdHourVO sumAwardHoursOutding(Map map);
	
	public List<CpdLrnAwardRecord> getMaxCPDCoreHours(Map map);
	
	public List<CpdLrnAwardRecord> getMaxCPDNonCoreHours(Map map);
	
	public List<CpdLrnAwardRecord> searchAll(Page<CpdLrnAwardRecord> page);
	
	public List<CpdLrnAwardRecord> findCpdAwardUser(Map map);
	
	public List<CpdLrnAwardRecord> searchAllByAppId(Map map);
	
	public void  updateById(CpdLrnAwardRecord clar);

	public void  updateManulInd(CpdLrnAwardRecord clar);
	
	
	public List<IndividualReportVo> getRecordForIndividualReport(Map map);
	
	public List<com.cwn.wizbank.cpd.vo.CpdGroupSumVo> getCpdGroupSumList(Map map);

	public List<CpdLrnAwardRecord> getNowGroupRegHoursReport(Map<String, Object> map);
	
	public List<CpdLrnAwardRecord> searchAllLrnAward(Map<String,Object> map);
}

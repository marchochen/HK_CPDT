package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.cpd.vo.IndividualReportVo;
import com.cwn.wizbank.entity.Article;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.utils.Page;

public interface CpdGroupRegHoursHistoryMapper  extends BaseMapper<CpdGroupRegHoursHistory>{
	
	public List<CpdGroupRegHoursHistory> getCpdGroupRegHoursHistory(Map map);
	
    public List<CpdGroupRegHoursHistory> getCpdGroupRegHoursHistoryPeriod(Map map);
    
    public List<RegUser> getUserDetail(Map map);
    
    public List<RegUser> getUserDetailForExcel(Map map);
    
	public List<Integer> getCpdGroupRegHoursHistoryYear();

	public List<CpdGroupRegHoursHistory>  getGroupRegHoursReport(Map<String,Object> map);
	
	public List<IndividualReportVo> getRecordForIndividualReport(Map map);
	
	public List<com.cwn.wizbank.cpd.vo.CpdGroupSumVo> getCpdGroupSumList(Map map);
	
	public List<CpdGroupRegHoursHistory> getHistoryView(Page<CpdGroupRegHoursHistory> page);
	
	public Long getCghiID(CpdGroupRegHoursHistory cpdGroupRegHoursHistory);
	
}

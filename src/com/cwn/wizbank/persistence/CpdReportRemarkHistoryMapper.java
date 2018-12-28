package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CpdReportRemarkHistory;

public interface CpdReportRemarkHistoryMapper extends BaseMapper<CpdReportRemarkHistory>{

	public List<CpdReportRemarkHistory> getCpdReportRemarkHistory (Map map);
	
    public List<CpdReportRemarkHistory> getRemarkHistoryForExcel (Map map);
	
	
	
}

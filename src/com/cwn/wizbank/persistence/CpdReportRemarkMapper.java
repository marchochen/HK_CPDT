package com.cwn.wizbank.persistence;

import java.util.List;
import java.util.Map;

import com.cwn.wizbank.entity.CpdReportRemark;

public interface CpdReportRemarkMapper  extends BaseMapper<CpdReportRemark>{

	public CpdReportRemark getCpdReportRemark (Map map);
	
	public List<CpdReportRemark> findAll();
}

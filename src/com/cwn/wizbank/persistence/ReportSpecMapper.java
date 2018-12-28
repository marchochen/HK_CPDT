package com.cwn.wizbank.persistence;

import java.util.List;

import com.cwn.wizbank.entity.ReportSpec;
import com.cwn.wizbank.utils.Page;


public interface ReportSpecMapper extends BaseMapper<ReportSpec>{

	List<ReportSpec> selectReportSpecList(Page<ReportSpec> page);
	
	ReportSpec selectId(Long rsp_id);
	
}
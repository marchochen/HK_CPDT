package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.GourpLoginReport;

public interface GourpLoginReportMapper extends BaseMapper<GourpLoginReport> {

	
	void insert(GourpLoginReport gourpLoginReport);
	
	void update(GourpLoginReport gourpLoginReport);
	
	int checkNumber(GourpLoginReport gourpLoginReport);
}

package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.PositionLoginReport;

public interface PositionLoginReportMapper extends BaseMapper<PositionLoginReport> {

	void insert(PositionLoginReport positionLoginReport);
	
	void update(PositionLoginReport positionLoginReport);
		
    int checkNumber(PositionLoginReport positionLoginReport);
    
}

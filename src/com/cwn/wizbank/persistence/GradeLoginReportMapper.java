package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.GradeLoginReport;

public interface GradeLoginReportMapper extends BaseMapper<GradeLoginReport> {
	
    void insert(GradeLoginReport gradeLoginReport);	

    void update(GradeLoginReport gradeLoginReport);
	
	int checkNumber(GradeLoginReport gradeLoginReport); 
}

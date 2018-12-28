package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.CourseRecord;

public interface CourseRecordMapper extends BaseMapper<CourseRecord> {
public CourseRecord getCourseRecord(CourseRecord courseRecord);

public void insertRecord(CourseRecord courseRecord);

public void updateCourseRecord(CourseRecord courseRecord);
}

package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.CourseRecord;
import com.cwn.wizbank.persistence.CourseRecordMapper;

@Service
public class CourseRecordService extends BaseService<CourseRecord> {

	@Autowired
	CourseRecordMapper courseRecordMapper;

	public void insertRecord(CourseRecord courseRecord) {
		CourseRecord record = courseRecordMapper.getCourseRecord(courseRecord);
		if(record != null){
			courseRecord.setPcr_duration(record.getPcr_duration() + courseRecord.getPcr_duration());
			courseRecordMapper.updateCourseRecord(courseRecord);
		}else{
			courseRecordMapper.insertRecord(courseRecord);
		}
	}

	public CourseRecord getCourseRecord(CourseRecord courseRecord) {
		CourseRecord record = courseRecordMapper.getCourseRecord(courseRecord);
       return record == null ? null : record;
       
	}
}

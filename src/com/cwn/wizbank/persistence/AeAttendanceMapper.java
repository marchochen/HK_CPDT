package com.cwn.wizbank.persistence;

import com.cwn.wizbank.entity.AeAttendance;


public interface AeAttendanceMapper extends BaseMapper<AeAttendance>{
    
    public void updateComplete(AeAttendance aeAttendance);
    
    AeAttendance getEnrollmentTime(Integer att_app_id);
}
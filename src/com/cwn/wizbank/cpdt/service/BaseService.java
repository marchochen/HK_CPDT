/**
 * 
 */
package com.cwn.wizbank.cpdt.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpdt.persistence.AeItemCpdtGourpItemMapper;
import com.cwn.wizbank.cpdt.persistence.AeItemCpdtItemMapper;
import com.cwn.wizbank.cpdt.persistence.CpdtGroupMapper;
import com.cwn.wizbank.cpdt.persistence.CpdtGroupRegistrationMapper;
import com.cwn.wizbank.cpdt.persistence.CpdtLrnAwardRecordMapper;
import com.cwn.wizbank.cpdt.persistence.CpdtRegistrationMapper;
import com.cwn.wizbank.cpdt.persistence.CpdtTypeMapper;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.persistence.AeAttendanceMapper;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.persistence.CourseEvaluationMapper;
import com.cwn.wizbank.persistence.CourseMapper;
import com.cwn.wizbank.persistence.CpdLrnAwardRecordMapper;
import com.cwn.wizbank.persistence.ImsLogMapper;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.persistence.TrackingHistoryMapper;

/**
 * 
* Title: BaseService.java 
* Description: 暂时用于管理mapper的引用，减少其他server类的引用Mapper的实例
* @author Jaren  
* @date 2018年11月23日
 */
public class BaseService {
	
    @Autowired
    RegUserMapper regUserMapper;
    @Autowired
    AeItemMapper aeItemMapper;
    @Autowired
    AeApplicationMapper aeApplicationMapper;
    @Autowired
    AeAttendanceMapper aeAttendanceMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TrackingHistoryMapper trackingHistoryMapper;
    @Autowired
    CourseEvaluationMapper courseEvaluationMapper;
    @Autowired
    ImsLogMapper imsLogMapper;
    @Autowired
    CpdtTypeMapper cpdtTypeMapper;
    @Autowired
    CpdtRegistrationMapper cpdtRegistrationMapper;
    @Autowired
    CpdtGroupMapper cpdtGroupMapper;
    @Autowired
    CpdtGroupRegistrationMapper cpdtGroupRegistrationMapper;
    @Autowired
    AeItemCpdtGourpItemMapper aeItemCpdtGourpItemMapper;
    @Autowired
    CpdtLrnAwardRecordMapper cpdtLrnAwardRecordMapper;
    @Autowired
    AeItemCpdtItemMapper aeItemCpdtItemMapper;
    
	
}

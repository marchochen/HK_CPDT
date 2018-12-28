package com.cwn.wizbank.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.CourseCriteria;
import com.cwn.wizbank.entity.CourseTabsRemind;
import com.cwn.wizbank.persistence.AeItemMapper;
import com.cwn.wizbank.persistence.CourseTabsRemindMapper;

@Service
public class CourseTabsRemindService {
	@Autowired
	CourseTabsRemindMapper courseTabsRemindMapper;
	
	@Autowired
	AeItemMapper aeItemMapper;
	
	public CourseTabsRemind getCourseTabsRemind(long itm_id){
		CourseTabsRemind courseTabsRemind = new CourseTabsRemind();
		AeItem aeItem = aeItemMapper.get(itm_id);
		if(aeItem != null){
			if(AeItem.SELFSTUDY.equals(aeItem.getItm_type())){
				courseTabsRemind.setRmdCompletionCriteriaSettings(getRmdCompletionCriteriaSettings(itm_id));
				courseTabsRemind.setRmdOnlineContent(getRmdOnlineContent(itm_id));
				courseTabsRemind.setRmdTargetLearner(getRmdTargetLearner(itm_id));
				courseTabsRemind.setRmdCourseScoreSettings(getRmdCourseScoreSettings(itm_id));
			} else if (AeItem.CLASSROOM.equals(aeItem.getItm_type())) {
				if(aeItem.getItm_run_ind() == 1){
					courseTabsRemind.setRmdCompletionCriteriaSettings(getRmdCompletionCriteriaSettings(itm_id));
					courseTabsRemind.setRmdOnlineContent(getRmdOnlineContent(itm_id));
					courseTabsRemind.setRmdCourseScoreSettings(getRmdCourseScoreSettings(itm_id));
					courseTabsRemind.setRmdTimetable(getRmdTimetable(itm_id));
					courseTabsRemind.setRmdCourseScoreSettings(getRmdCourseScoreSettings(itm_id));
					courseTabsRemind.setRmdCompletionCriteriaSettings(getRmdCompletionCriteriaSettings(itm_id));
				} else {
					courseTabsRemind.setRmdTargetLearner(getRmdTargetLearner(itm_id));
					courseTabsRemind.setRmdOnlineContent(getRmdOnlineContent(itm_id));
					courseTabsRemind.setRmdClassManagement(getRmdClassManagement(itm_id));
					courseTabsRemind.setRmdTimetable(getRmdTimetable(itm_id));
					courseTabsRemind.setRmdCourseScoreSettings(getRmdCourseScoreSettings(itm_id));
					courseTabsRemind.setRmdCompletionCriteriaSettings(getRmdCompletionCriteriaSettings(itm_id));
				}
			} else if (AeItem.INTEGRATED.equals(aeItem.getItm_type())) {
				courseTabsRemind.setRmdTargetLearner(getRmdTargetLearner(itm_id));
				courseTabsRemind.setRmdCoursePackage(getRmdCoursePackage(itm_id));
			} else if(AeItem.AUDIOVIDEO.equals(aeItem.getItm_type())){
				courseTabsRemind.setRmdOnlineContent(getRmdOnlineContent(itm_id));
			}
		}
		return courseTabsRemind;
	}
	
	public boolean getRmdOnlineContent(long itm_id){
		boolean flag = false;
		long cos_content_count = courseTabsRemindMapper.getRmdOnlineContent(itm_id);
		if(cos_content_count < 1 ){
			flag = true;
		}
		return flag;
	}
	
	public boolean getRmdCompletionCriteriaSettings(long itm_id){
		boolean flag = false;
		CourseCriteria courseCriteria = courseTabsRemindMapper.getCourseCriteria(itm_id);
		if (courseCriteria != null && courseCriteria.getCcr_id() > 0) {
			if (((courseCriteria.getCcr_attendance_rate() == null || courseCriteria.getCcr_attendance_rate() <= 0)
					&& (courseCriteria.getCcr_pass_score() == null || courseCriteria.getCcr_pass_score() < 0.0000000001)
					&& (courseCriteria.getCcr_offline_condition() == null || "".equals(courseCriteria.getCcr_offline_condition())))) {
				long cnt = courseTabsRemindMapper.getRmdCompletionCriteriaSettings(itm_id);
				if (cnt == 0) {
					flag = true;
				}
			}
		}
		return flag;
	}
	

	
	public boolean getRmdTargetLearner(long itm_id){
		boolean flag = false;
		long cnt = courseTabsRemindMapper.getRmdTargetLearner(itm_id);
		if(cnt == 0){
			flag = true;
		}
		return flag;
	}
	

	
	public boolean getRmdCourseScoreSettings(long itm_id){
		boolean flag = false;
		long cnt = courseTabsRemindMapper.getRmdCourseScoreSettings(itm_id);
		if(cnt == 0){
			flag = true;
		}
		return flag;
	}
	

	
	public boolean getRmdTimetable(long itm_id){
		boolean flag = false;
		long cnt = courseTabsRemindMapper.getRmdTimetable(itm_id);
		if(cnt == 0){
			flag = true;
		}
		return flag;
	}
	

	
	public boolean getRmdClassManagement(long itm_id){
		boolean flag = false;
		long cnt = courseTabsRemindMapper.getRmdClassManagement(itm_id);
		if(cnt == 0){
			flag = true;
		}
		return flag;
	}
	

	public boolean getRmdCoursePackage(long itm_id){
		boolean flag = false;
		long cnt = courseTabsRemindMapper.getRmdCoursePackage(itm_id);
		if(cnt == 0){
			flag = true;
		}
		return flag;
	}

}

package com.cw.wizbank.JsonMod.study.bean;

import java.sql.Timestamp;
import java.util.List;

/**
 * 待审批的课程(供Json使用)
 * @author kimyu
 */
public class PendingCourseBean extends CourseBean {
	private Timestamp app_create_timestamp; // 申请日期(即报名日期)
	private Timestamp aal_action_timestamp; // 上次更新日期(管理员上次审批日期或学员报名日期)
	private Timestamp last_approval_date; 	// 上次审批日期(注：该字段如不取别名，会与aal_action_timestamp重名)
	private String usr_display_bil; 		// 上一审批者
	private List next_approver; 			// 下一(批)审批者(注：该字段只能取别名)
	private String itm_dummy_type;

	public Timestamp getApp_create_timestamp() {
		return app_create_timestamp;
	}

	public void setApp_create_timestamp(Timestamp app_create_timestamp) {
		this.app_create_timestamp = app_create_timestamp;
	}

	public Timestamp getAal_action_timestamp() {
		return aal_action_timestamp;
	}

	public void setAal_action_timestamp(Timestamp aal_action_timestamp) {
		this.aal_action_timestamp = aal_action_timestamp;
	}

	public Timestamp getLast_approval_date() {
		return last_approval_date;
	}

	public void setLast_approval_date(Timestamp last_approval_date) {
		this.last_approval_date = last_approval_date;
	}

	public String getUsr_display_bil() {
		return usr_display_bil;
	}

	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}

	public List getNext_approver() {
		return next_approver;
	}

	public void setNext_approver(List next_approver) {
		this.next_approver = next_approver;
	}

	public String getItm_dummy_type() {
		return itm_dummy_type;
	}

	public void setItm_dummy_type(String itm_dummy_type) {
		this.itm_dummy_type = itm_dummy_type;
	}
	
}

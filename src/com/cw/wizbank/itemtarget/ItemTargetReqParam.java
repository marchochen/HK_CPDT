package com.cw.wizbank.itemtarget;

import java.sql.Timestamp;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

public class ItemTargetReqParam extends ReqParam{

	public long itm_id;
	public long rule_id;
	public String itm_target_type;
	public String target_group_lst;
	public String target_grade_lst;
	public String target_skill_lst;
	public String target_enrol_type;
	public String last_target_enrol_type;
	public Timestamp timestamp;
	public Timestamp last_upd_time;
	public boolean is_del_all;
	public static final String deli = ",";

	public int itr_group_ind;
	public int itr_grade_ind;
	public int itr_skill_ind;
	public int itr_compulsory_ind;
	public boolean is_new_cos;
	
	public ItemTargetReqParam(ServletRequest inReq, String clientEnc_, String encoding_)	throws cwException {
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		super.common();
	}

	public void getItemTargetParam () throws cwException {
		pagination();
		itm_id = getLongParameter("itm_id");
		rule_id = getLongParameter("rule_id");
		itm_target_type = getStringParameter("itm_target_type");
		target_group_lst = getStringParameter("target_group_lst");
		target_grade_lst = getStringParameter("target_grade_lst");
		target_skill_lst = getStringParameter("target_skill_lst");
		timestamp = getTimestampParameter("upd_timestamp");
		target_enrol_type = getStringParameter("target_enrol_type");
		last_target_enrol_type = getStringParameter("last_target_enrol_type");
		last_upd_time = getTimestampParameter("last_upd_time");
		is_del_all = getBooleanParameter("is_del_all");

		itr_group_ind = getIntParameter("itr_group_ind");
		itr_grade_ind = getIntParameter("itr_grade_ind");
		itr_skill_ind = getIntParameter("itr_skill_ind");
		itr_compulsory_ind = getIntParameter("itr_compulsory_ind");
		is_new_cos = getBooleanParameter("is_new_cos");
		return;
	}

}

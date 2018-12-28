package com.cw.wizbank.JsonMod.supervise;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;


public class SuperviseModuleParam extends BaseParam{
	private String staff_type; //下属类型
	private long group_id=0;	//下属部门id
	private String search_staff_str;	//搜索下属关键字
	private long usr_ent_id;
	private boolean return_xml;

	
	//下属报表
	private String rspTitle;
	private String s_usg_ent_id_lst;
	private Timestamp att_create_start_datetime;
	private Timestamp att_create_end_datetime;
	private int ats_id;
	private String ent_id_str;
	private String usr_name_str;
	private String itm_id_str;
	private String itm_title_str;
	private String tnd_id_str;
	private String tnd_title_str;
	private int rsp_id;
	
	
	public long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public String getStaff_type() {
		return staff_type;
	}

	public void setStaff_type(String staff_type) {
		this.staff_type = staff_type;
	}

	public String getSearch_staff_str() {
		return search_staff_str;
	}

	public void setSearch_staff_str(String search_staff_str) throws cwException {
		search_staff_str=cwUtils.unicodeFrom(search_staff_str, clientEnc, encoding);
		this.search_staff_str = search_staff_str;
	}

	public int getAts_id() {
		return ats_id;
	}

	public void setAts_id(int ats_id) {
		this.ats_id = ats_id;
	}

	public Timestamp getAtt_create_end_datetime() {
		return att_create_end_datetime;
	}

	public void setAtt_create_end_datetime(Timestamp att_create_end_datetime) {
		this.att_create_end_datetime = att_create_end_datetime;
	}



	public Timestamp getAtt_create_start_datetime() {
		return att_create_start_datetime;
	}

	public void setAtt_create_start_datetime(Timestamp att_create_start_datetime) {
		this.att_create_start_datetime = att_create_start_datetime;
	}

	public String getItm_id_str() {
		return itm_id_str;
	}

	public void setItm_id_str(String itm_id_str) {
		this.itm_id_str = itm_id_str;
	}

	public String getItm_title_str() {
		return itm_title_str;
	}

	public void setItm_title_str(String itm_title_str) throws cwException {
		itm_title_str=cwUtils.unicodeFrom(itm_title_str, clientEnc, encoding);
		this.itm_title_str = itm_title_str;
	}

	public String getTnd_id_str() {
		return tnd_id_str;
	}

	public void setTnd_id_str(String tnd_id_str) {
		this.tnd_id_str = tnd_id_str;
	}

	public String getTnd_title_str() {
		return tnd_title_str;
	}

	public void setTnd_title_str(String tnd_title_str) throws cwException {
		tnd_title_str=cwUtils.unicodeFrom(tnd_title_str, clientEnc, encoding);
		this.tnd_title_str = tnd_title_str;
	}

	public String getEnt_id_str() {
		return ent_id_str;
	}

	public void setEnt_id_str(String ent_id_str) {
		this.ent_id_str = ent_id_str;
	}

	public String getUsr_name_str() {
		return usr_name_str;
	}

	public void setUsr_name_str(String usr_name_str) throws cwException {
		usr_name_str=cwUtils.unicodeFrom(usr_name_str, clientEnc, encoding);
		this.usr_name_str = usr_name_str;
	}

	public String getRspTitle() {
		return rspTitle;
	}

	public void setRspTitle(String rspTitle) throws cwException {
		rspTitle=cwUtils.unicodeFrom(rspTitle, clientEnc, encoding);
		this.rspTitle = rspTitle;
	}

	public int getRsp_id() {
		return rsp_id;
	}

	public void setRsp_id(int rsp_id) {
		this.rsp_id = rsp_id;
	}

	public long getUsr_ent_id() {
		return usr_ent_id;
	}

	public void setUsr_ent_id(long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}

	public String getS_usg_ent_id_lst() {
		return s_usg_ent_id_lst;
	}

	public void setS_usg_ent_id_lst(String s_usg_ent_id_lst) {
		this.s_usg_ent_id_lst = s_usg_ent_id_lst;
	}
	
	public boolean getReturn_xml() {
		return return_xml;
	}
	
	public void setReturn_xml(boolean return_xml) {
		this.return_xml = return_xml;
	}



}

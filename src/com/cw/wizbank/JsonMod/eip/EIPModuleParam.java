package com.cw.wizbank.JsonMod.eip;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;

public class EIPModuleParam extends BaseParam {
	private long eip_id;
	private String eip_code;
	private String eip_name;
	private long eip_tcr_id;
	private long eip_account_num;
	private String eip_status;
	private String eip_domain;
	private Timestamp eip_update_timestamp;	
	private int login_bg_type; // 0： 保留现有图片    1：使用wizbank默认图片    2：上传新图片
	private String eip_login_bg;
	private int mobile_login_bg_type; // 0： 保留现有图片    1：使用默认图片    2：上传新图片
	private String eip_mobile_login_bg;
	private long eip_dps_share_usr_inf_ind;
	private long eip_max_peak_count; //企业最大在线人数
	private long eip_live_max_count;//直播并发数
	private String eip_live_mode;//直播模式
	private String eip_live_qcloud_secretid; //简易直播模式账号
	private String eip_live_qcloud_secretkey;//简易直播模式密码
	
	public long getEip_dps_share_usr_inf_ind() {
		return eip_dps_share_usr_inf_ind;
	}
	public void setEip_dps_share_usr_inf_ind(long eip_dps_share_usr_inf_ind) {
		this.eip_dps_share_usr_inf_ind = eip_dps_share_usr_inf_ind;
	}
	public String getEip_login_bg() {
		return eip_login_bg;
	}
	public void setEip_login_bg(String eip_login_bg) {
		this.eip_login_bg = eip_login_bg;
	}
	public int getLogin_bg_type() {
		return login_bg_type;
	}
	public void setLogin_bg_type(int login_bg_type) {
		this.login_bg_type = login_bg_type;
	}
	public long getEip_account_num() {
		return eip_account_num;
	}
	public void setEip_account_num(long eip_account_num) {
		this.eip_account_num = eip_account_num;
	}
	public String getEip_code() {
		return eip_code;
	}
	public void setEip_code(String eip_code) throws cwException {
		this.eip_code = eip_code;//cwUtils.unicodeFrom(eip_code, clientEnc, encoding);
	}
	public String getEip_domain() {
		return eip_domain;
	}
	public void setEip_domain(String eip_domain) throws cwException {
		this.eip_domain = eip_domain;//cwUtils.unicodeFrom(eip_domain, clientEnc, encoding);
	}
	public long getEip_id() {
		return eip_id;
	}
	public void setEip_id(long eip_id) {
		this.eip_id = eip_id;
	}
	public String getEip_name() {
		return eip_name;
	}
	public void setEip_name(String eip_name) throws cwException {
		this.eip_name = eip_name;//cwUtils.unicodeFrom(eip_name, clientEnc, encoding);
	}
	public String getEip_status() {
		return eip_status;
	}
	public void setEip_status(String eip_status) {
		this.eip_status = eip_status;
	}
	public long getEip_tcr_id() {
		return eip_tcr_id;
	}
	public void setEip_tcr_id(long eip_tcr_id) {
		this.eip_tcr_id = eip_tcr_id;
	}
	public Timestamp getEip_update_timestamp() {
		return eip_update_timestamp;
	}
	public void setEip_update_timestamp(Timestamp eip_update_timestamp) {
		this.eip_update_timestamp = eip_update_timestamp;
	}
	public int getMobile_login_bg_type() {
		return mobile_login_bg_type;
	}
	public void setMobile_login_bg_type(int mobile_login_bg_type) {
		this.mobile_login_bg_type = mobile_login_bg_type;
	}
	public String getEip_mobile_login_bg() {
		return eip_mobile_login_bg;
	}
	public void setEip_mobile_login_bg(String eip_mobile_login_bg) {
		this.eip_mobile_login_bg = eip_mobile_login_bg;
	}
	public long getEip_max_peak_count() {
		return eip_max_peak_count;
	}
	public void setEip_max_peak_count(long eip_max_peak_count) {
		this.eip_max_peak_count = eip_max_peak_count;
	}
	public long getEip_live_max_count() {
		return eip_live_max_count;
	}
	public void setEip_live_max_count(long eip_live_max_count) {
		this.eip_live_max_count = eip_live_max_count;
	}
	public String getEip_live_mode() {
		return eip_live_mode;
	}
	public void setEip_live_mode(String eip_live_mode) {
		this.eip_live_mode = eip_live_mode;
	}
	public String getEip_live_qcloud_secretid() {
		return eip_live_qcloud_secretid;
	}
	public void setEip_live_qcloud_secretid(String eip_live_qcloud_secretid) {
		this.eip_live_qcloud_secretid = eip_live_qcloud_secretid;
	}
	public String getEip_live_qcloud_secretkey() {
		return eip_live_qcloud_secretkey;
	}
	public void setEip_live_qcloud_secretkey(String eip_live_qcloud_secretkey) {
		this.eip_live_qcloud_secretkey = eip_live_qcloud_secretkey;
	}
	
	
}

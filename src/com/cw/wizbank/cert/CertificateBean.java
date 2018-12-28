package com.cw.wizbank.cert;

import java.sql.Timestamp;

public class CertificateBean {
	private long cfc_id;
	private String cfc_code;
	private String cfc_title;
	private String cfc_img;
	private long cfc_tcr_id;
	private String cfc_status;
	private Timestamp cfc_end_datetime;
	private Timestamp cfc_create_datetime;
	private String cfc_create_user_id;
	private Timestamp cfc_update_datetime;
	private String cfc_update_user_id;
	private Timestamp cfc_delete_datetime;
	private String cfc_delete_user_id;
	private long cfc_itm_id;
	private long cfc_tkh_id;

	public long getCfc_id() {
		return cfc_id;
	}

	public void setCfc_id(long cfc_id) {
		this.cfc_id = cfc_id;
	}

	public String getCfc_title() {
		return cfc_title;
	}

	public void setCfc_title(String cfc_title) {
		this.cfc_title = cfc_title;
	}

	public String getCfc_img() {
		return cfc_img;
	}

	public void setCfc_img(String cfc_img) {
		this.cfc_img = cfc_img;
	}

	public long getCfc_tcr_id() {
		return cfc_tcr_id;
	}

	public void setCfc_tcr_id(long cfc_tcr_id) {
		this.cfc_tcr_id = cfc_tcr_id;
	}

	public String getCfc_status() {
		return cfc_status;
	}

	public void setCfc_status(String cfc_status) {
		this.cfc_status = cfc_status;
	}

	public Timestamp getCfc_create_datetime() {
		return cfc_create_datetime;
	}

	public void setCfc_create_datetime(Timestamp cfc_create_datetime) {
		this.cfc_create_datetime = cfc_create_datetime;
	}

	public String getCfc_create_user_id() {
		return cfc_create_user_id;
	}

	public void setCfc_create_user_id(String cfc_create_user_id) {
		this.cfc_create_user_id = cfc_create_user_id;
	}

	public Timestamp getCfc_update_datetime() {
		return cfc_update_datetime;
	}

	public void setCfc_update_datetime(Timestamp cfc_update_datetime) {
		this.cfc_update_datetime = cfc_update_datetime;
	}

	public String getCfc_update_user_id() {
		return cfc_update_user_id;
	}

	public void setCfc_update_user_id(String cfc_update_user_id) {
		this.cfc_update_user_id = cfc_update_user_id;
	}

	public Timestamp getCfc_delete_datetime() {
		return cfc_delete_datetime;
	}

	public void setCfc_delete_datetime(Timestamp cfc_delete_datetime) {
		this.cfc_delete_datetime = cfc_delete_datetime;
	}

	public String getCfc_delete_user_id() {
		return cfc_delete_user_id;
	}

	public void setCfc_delete_user_id(String cfc_delete_user_id) {
		this.cfc_delete_user_id = cfc_delete_user_id;
	}

	public String getCfc_code() {
		return cfc_code;
	}

	public void setCfc_code(String cfc_code) {
		this.cfc_code = cfc_code;
	}

	public Timestamp getCfc_end_datetime() {
		return cfc_end_datetime;
	}

	public void setCfc_end_datetime(Timestamp cfc_end_datetime) {
		this.cfc_end_datetime = cfc_end_datetime;
	}

	public long getCfc_itm_id() {
		return cfc_itm_id;
	}

	public void setCfc_itm_id(long cfc_itm_id) {
		this.cfc_itm_id = cfc_itm_id;
	}

	public long getCfc_tkh_id() {
		return cfc_tkh_id;
	}

	public void setCfc_tkh_id(long cfc_tkh_id) {
		this.cfc_tkh_id = cfc_tkh_id;
	}

}
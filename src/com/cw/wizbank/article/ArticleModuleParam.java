/**
 * 
 */
package com.cw.wizbank.article;

import java.util.Date;

import com.cw.wizbank.JsonMod.BaseParam;

/**
 * @author Leon.li 2013-5-24 5:10:49 emporary storage for the data form from
 *         request
 */
public class ArticleModuleParam extends BaseParam {
	
	int art_id;

	String art_title;

	String art_introduction;

	String art_keywords;

	String art_content;

	int art_user_id;

	Date art_create_datetime;

	Date art_update_datetime;

	int art_update_user_id;

	String art_location;

	String art_type;

	int art_status;

	int art_tcr_id;

	int art_push_mobile;

	int art_is_html;
	
    String encryp_tart_id;
	String encryp_aty_id;
    
	public String art_id_str;
	public String art_icon_file;
	public String art_icon_select;
	public long aty_id;
	public String aty_title;

	public long aty_tcr_id;
	public String aty_id_list;
	
	public String default_image;

	private long tcr_id;
	
	public String getEncryp_aty_id() {
		return encryp_aty_id;
	}

	public void setEncryp_aty_id(String encryp_aty_id) {
		this.encryp_aty_id = encryp_aty_id;
	}

	public String getEncryp_tart_id() {
		return encryp_tart_id;
	}

	public void setEncryp_tart_id(String encryp_tart_id) {
		this.encryp_tart_id = encryp_tart_id;
	}
	
	public int getArt_id() {
		return art_id;
	}

	public void setArt_id(int art_id) {
		this.art_id = art_id;
	}

	public String getArt_title() {
		return art_title;
	}

	public String getArt_icon_file() {
		return art_icon_file;
	}

	public void setArt_icon_file(String art_icon_file) {
		this.art_icon_file = art_icon_file;
	}

	public void setArt_title(String art_title) {
		this.art_title = art_title;
	}

	public String getArt_introduction() {
		return art_introduction;
	}

	public void setArt_introduction(String art_introduction) {
		this.art_introduction = art_introduction;
	}

	public String getArt_keywords() {
		return art_keywords;
	}

	public void setArt_keywords(String art_keywords) {
		this.art_keywords = art_keywords;
	}

	public String getArt_content() {
		return art_content;
	}

	public void setArt_content(String art_content) {
		this.art_content = art_content;
	}

	public int getArt_user_id() {
		return art_user_id;
	}

	public void setArt_user_id(int art_user_id) {
		this.art_user_id = art_user_id;
	}

	public String getArt_icon_select() {
		return art_icon_select;
	}

	public void setArt_icon_select(String art_icon_select) {
		this.art_icon_select = art_icon_select;
	}

	public Date getArt_create_datetime() {
		return art_create_datetime;
	}

	public void setArt_create_datetime(Date art_create_datetime) {
		this.art_create_datetime = art_create_datetime;
	}

	public Date getArt_update_datetime() {
		return art_update_datetime;
	}

	public void setArt_update_datetime(Date art_update_datetime) {
		this.art_update_datetime = art_update_datetime;
	}

	public int getArt_update_user_id() {
		return art_update_user_id;
	}

	public void setArt_update_user_id(int art_update_user_id) {
		this.art_update_user_id = art_update_user_id;
	}

	public String getArt_location() {
		return art_location;
	}

	public void setArt_location(String art_location) {
		this.art_location = art_location;
	}

	public String getArt_type() {
		return art_type;
	}

	public void setArt_type(String art_type) {
		this.art_type = art_type;
	}

	public int getArt_status() {
		return art_status;
	}

	public void setArt_status(int art_status) {
		this.art_status = art_status;
	}

	public int getArt_tcr_id() {
		return art_tcr_id;
	}

	public void setArt_tcr_id(int art_tcr_id) {
		this.art_tcr_id = art_tcr_id;
	}

	public int getArt_push_mobile() {
		return art_push_mobile;
	}

	public void setArt_push_mobile(int art_push_mobile) {
		this.art_push_mobile = art_push_mobile;
	}

	public int getArt_is_html() {
		return art_is_html;
	}

	public void setArt_is_html(int art_is_html) {
		this.art_is_html = art_is_html;
	}

	public String getArt_id_str() {
		return art_id_str;
	}

	public void setArt_id_str(String art_id_str) {
		this.art_id_str = art_id_str;
	}

	public long getAty_id() {
		return aty_id;
	}

	public void setAty_id(long aty_id) {
		this.aty_id = aty_id;
	}

	public String getAty_title() {
		return aty_title;
	}

	public void setAty_title(String aty_title) {
		this.aty_title = aty_title;
	}

	public long getAty_tcr_id() {
		return aty_tcr_id;
	}

	public void setAty_tcr_id(long aty_tcr_id) {
		this.aty_tcr_id = aty_tcr_id;
	}

	public String getAty_id_list() {
		return aty_id_list;
	}

	public void setAty_id_list(String aty_id_list) {
		this.aty_id_list = aty_id_list;
	}

	public String getDefault_image() {
		return default_image;
	}

	public void setDefault_image(String default_image) {
		this.default_image = default_image;
	}
	
	public void setTcr_id(Long tcr_id) {
		this.tcr_id = tcr_id;
	}
	public Long getTcr_id() {
		return tcr_id;
	}
	

}

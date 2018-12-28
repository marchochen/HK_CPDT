package com.cwn.wizbank.entity;

/**
 * 专题对应专家
 * <p>
 * Title:UserSpecialExpert
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author halo.pan
 *
 * @date 2016年4月12日 下午4:30:23
 *
 */
public class UserSpecialExpert implements java.io.Serializable {
	private static final long serialVersionUID = -3990685155039469200L;
	private long use_id;
	private long use_ust_id;
	private long use_ent_id;

	private String href;
	private String title;
	private String usr_photo;
	private Double iti_score;
    private String iti_level;
    String abs_img;
    int num;
    String iti_expertise_areas;
    String areas;
	String iti_type;
	String more;
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsr_photo() {
		return usr_photo;
	}

	public void setUsr_photo(String usr_photo) {
		this.usr_photo = usr_photo;
	}


	public Double getIti_score() {
		return iti_score;
	}

	public void setIti_score(Double iti_score) {
		this.iti_score = iti_score;
	}

	public String getIti_level() {
		return iti_level;
	}

	public void setIti_level(String iti_level) {
		this.iti_level = iti_level;
	}

	public long getUse_id() {
		return use_id;
	}

	public void setUse_id(long use_id) {
		this.use_id = use_id;
	}

	public long getUse_ust_id() {
		return use_ust_id;
	}

	public void setUse_ust_id(long use_ust_id) {
		this.use_ust_id = use_ust_id;
	}

	public long getUse_ent_id() {
		return use_ent_id;
	}

	public void setUse_ent_id(long use_ent_id) {
		this.use_ent_id = use_ent_id;
	}

	public String getAbs_img() {
		return abs_img;
	}

	public void setAbs_img(String abs_img) {
		this.abs_img = abs_img;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getIti_expertise_areas() {
		return iti_expertise_areas;
	}

	public void setIti_expertise_areas(String iti_expertise_areas) {
		this.iti_expertise_areas = iti_expertise_areas;
	}

	public String getIti_type() {
		return iti_type;
	}

	public void setIti_type(String iti_type) {
		this.iti_type = iti_type;
	}


	public String getAreas() {
		return areas;
	}

	public void setAreas(String areas) {
		this.areas = areas;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}
	

}
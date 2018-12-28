/**
 * 
 */
package com.cwn.wizbank.entity.vo;

/**
 * @author leon.li
 *
 */
public class SearchResultVo {
	
	public long id;
	
	public String title;
	
	public String introduction;
	
	public String content;
	
	public String type;
	
	public String photo;
	
	/**
	 * 针对搜索结果为课程而设置的属性，表示是不是推送到移动端的课程标记
	 */
	public String itm_mobile_ind;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getItm_mobile_ind() {
		return itm_mobile_ind;
	}

	public void setItm_mobile_ind(String itm_mobile_ind) {
		this.itm_mobile_ind = itm_mobile_ind;
	}

}

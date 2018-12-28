package com.cw.wizbank.JsonMod;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * 用于论坛搜索
 * @author kimyu
 *
 */
public class ForumModuleParam extends BaseParam {
	private String search_id;	// 高级搜索的类型(判别用户从课程页面、还是从知道、还是从讨论区搜索)，此search_id对应一个是搜索条件(放入Session中)
	
	private String phrase;			// 论坛搜索关键字
	private String created_by;		// 发表人
	private int search_type_topic;	// 是否按主题搜索(1：是 0：否)
	private int search_type_msg;	// 是否按文章内容搜索(1：是 0：否)
	private int created_after_days;	// 搜索天数
	private String sort_order;		// 排序方式(可选值：asc、desc)
	
	public String getSearch_id() {
		return search_id;
	}

	public void setSearch_id(String search_id) {
		this.search_id = search_id;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) throws cwException {
		phrase = cwUtils.unicodeFrom(phrase, clientEnc, encoding);	// 设置编码格式
		this.phrase = phrase;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) throws cwException {
		created_by = cwUtils.unicodeFrom(created_by, clientEnc, encoding);	// 设置编码格式
		this.created_by = created_by;
	}

	public int getSearch_type_topic() {
		return search_type_topic;
	}

	public void setSearch_type_topic(int search_type_topic) {
		this.search_type_topic = search_type_topic;
	}

	public int getSearch_type_msg() {
		return search_type_msg;
	}

	public void setSearch_type_msg(int search_type_msg) {
		this.search_type_msg = search_type_msg;
	}

	public int getCreated_after_days() {
		return created_after_days;
	}

	public void setCreated_after_days(int created_after_days) {
		this.created_after_days = created_after_days;
	}

	public String getSort_order() {
		return sort_order;
	}

	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}
	
	
}

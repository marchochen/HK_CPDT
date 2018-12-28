package com.cw.wizbank.JsonMod.commonBean;

import java.util.Vector;

import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.util.LangLabel;

public class JsonTreeBean {
	private long id; 
	private String text;
	private String href;
	private String usg_role;
	private String cur_lan;

	private String type;
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		if (dbUserGroup.USG_ROLE_ROOT.equalsIgnoreCase(this.getUsg_role())) {
			text = LangLabel.getValue(this.getCur_lan(), "668");
		}
		
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
	public String getUsg_role() {
		return usg_role;
	}
	public void setUsg_role(String usg_role) {
		this.usg_role = usg_role;
	} 
	public void setCur_lan(String cur_lan) {
		this.cur_lan = cur_lan;
	}
	public String getCur_lan() {
		return cur_lan;
	}

}

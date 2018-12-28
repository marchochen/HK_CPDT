package com.cw.wizbank.JsonMod.supervise.bean;

public class GroupBean {
	private long usg_ent_id;
	private String usg_display_bil;
	private boolean hasChild;
	public long getUsg_ent_id() {
		return usg_ent_id;
	}
	public void setUsg_ent_id(long usg_ent_id) {
		this.usg_ent_id = usg_ent_id;
	}
	public String getUsg_display_bil() {
		return usg_display_bil;
	}
	public void setUsg_display_bil(String usg_display_bil) {
		this.usg_display_bil = usg_display_bil;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
}

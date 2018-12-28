package com.cw.wizbank.JsonMod.studyGroup.bean;

import java.util.Vector;

public class ItmSgpForumBean {
	private long sgp_id;
	private Vector topicLst;
	private long mod_id;
	
	public long getMod_id() {
		return mod_id;
	}
	public void setMod_id(long mod_id) {
		this.mod_id = mod_id;
	}
	public long getSgp_id() {
		return sgp_id;
	}
	public void setSgp_id(long sgp_id) {
		this.sgp_id = sgp_id;
	}
	public Vector getTopicLst() {
		return topicLst;
	}
	public void setTopicLst(Vector topicLst) {
		this.topicLst = topicLst;
	}

}

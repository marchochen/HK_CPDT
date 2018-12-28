package com.cw.wizbank.JsonMod.Course.bean;

public class OfflineMeasureBean {
	private long cmt_id;
	private String cmt_title;
	private int cmt_duration;
    private String cmt_place;
    private float score;
    private String status;
    private String cmt_status;
    
	public String getCmt_status() {
		return cmt_status;
	}
	public void setCmt_status(String cmt_status) {
		this.cmt_status = cmt_status;
	}
	public int getCmt_duration() {
		return cmt_duration;
	}
	public void setCmt_duration(int cmt_duration) {
		this.cmt_duration = cmt_duration;
	}
	public long getCmt_id() {
		return cmt_id;
	}
	public void setCmt_id(long cmt_id) {
		this.cmt_id = cmt_id;
	}
	public String getCmt_place() {
		return cmt_place;
	}
	public void setCmt_place(String cmt_place) {
		this.cmt_place = cmt_place;
	}
	public String getCmt_title() {
		return cmt_title;
	}
	public void setCmt_title(String cmt_title) {
		this.cmt_title = cmt_title;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}  

}

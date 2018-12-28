package com.cw.wizbank.JsonMod.Course.bean;

public class ScoreBean {
	
	private long id;  
    private String title;  
    private float max_score; 
    private int contri_rate; 
    private int pass_score_rate; 
    private float score;
    private String res_status;
    
	public String getRes_status() {
		return res_status;
	}
	public void setRes_status(String res_status) {
		this.res_status = res_status;
	}
	public int getContri_rate() {
		return contri_rate;
	}
	public void setContri_rate(int contri_rate) {
		this.contri_rate = contri_rate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getMax_score() {
		return max_score;
	}
	public void setMax_score(float max_score) {
		this.max_score = max_score;
	}
	public int getPass_score_rate() {
		return pass_score_rate;
	}
	public void setPass_score_rate(int pass_score_rate) {
		this.pass_score_rate = pass_score_rate;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}

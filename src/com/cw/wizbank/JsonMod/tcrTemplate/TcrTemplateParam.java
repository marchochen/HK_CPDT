package com.cw.wizbank.JsonMod.tcrTemplate;

import com.cw.wizbank.JsonMod.tcrCommon.TcrParam;

public class TcrTemplateParam extends TcrParam {
	private long tt_id;
	private int dis_fun_ind;

	public long getTt_id() {
		return tt_id;
	}

	public void setTt_id(long tt_id) {
		this.tt_id = tt_id;
	}
	
	public void setDis_fun_ind(int dis_fun_ind) {
		this.dis_fun_ind = dis_fun_ind;
	}
	
	public int getDis_fun_ind() {
		return dis_fun_ind;
	}
	

}

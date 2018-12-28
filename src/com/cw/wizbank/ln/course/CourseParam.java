package com.cw.wizbank.ln.course;

import com.cw.wizbank.JsonMod.BaseParam;

public class CourseParam extends BaseParam {
	private long[] tcr_id_lsts;
	private long[] itm_id_lsts;

	public long[] getTcr_id_lsts() {
		return tcr_id_lsts;
	}

	public void setTcr_id_lsts(long[] tcr_id_lsts) {
		this.tcr_id_lsts = tcr_id_lsts;
	}

	public long[] getItm_id_lsts() {
		return itm_id_lsts;
	}

	public void setItm_id_lsts(long[] itm_id_lsts) {
		this.itm_id_lsts = itm_id_lsts;
	}

}
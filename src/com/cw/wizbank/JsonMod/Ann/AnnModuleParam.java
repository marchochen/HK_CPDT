package com.cw.wizbank.JsonMod.Ann;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class AnnModuleParam extends BaseParam {

	// 对应页面上的一些请求
	private long tcr_id;
	private String tcr_title;
	private long msg_id;
	
	public long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(long msg_id) {
		this.msg_id = msg_id;
	}
	public long getTcr_id() {
		return tcr_id;
	}
	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}
	public String getTcr_title() {
		return tcr_title;
	}
	public void setTcr_title(String tcr_title) throws cwException {
		tcr_title=cwUtils.unicodeFrom(tcr_title, clientEnc, encoding);
		this.tcr_title = tcr_title;
	}   

}

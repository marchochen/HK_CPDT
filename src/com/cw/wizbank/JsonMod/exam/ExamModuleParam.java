package com.cw.wizbank.JsonMod.exam;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class ExamModuleParam extends BaseParam {
	private String msg_body; //信息内容
	private boolean isPause; //是否暂停考试
	private boolean isTerminate; //是否交卷
	private boolean isMarkAsZero; //是否置零分
	private long itm_id;
	private long usr_ent_id;
	private long mod_id;
	private String ent_id_lst;
	
	public boolean getIsMarkAsZero() {
		return isMarkAsZero;
	}
	public void setIsMarkAsZero(boolean isMarkAsZero) {
		this.isMarkAsZero = isMarkAsZero;
	}
	
	public boolean getIsPause() {
		return isPause;
	}
	public void setIsPause(boolean isPause) {
		this.isPause = isPause;
	}
	
	public boolean getIsTerminate() {
		return isTerminate;
	}
	public void setIsTerminate(boolean isTerminate) {
		this.isTerminate = isTerminate;
	}
	
	public String getMsg_body() {
		return msg_body;
	}
	public void setMsg_body(String msg_body) throws cwException {
		this.msg_body = cwUtils.unicodeFrom(msg_body, clientEnc, encoding);;
	}
	
	public long getItm_id() {
		return itm_id;
	}
	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}
	
	public long getUsr_ent_id() {
		return usr_ent_id;
	}
	public void setUsr_ent_id(long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}
	
	public long getMod_id() {
		return mod_id;
	}
	public void setMod_id(long mod_id) {
		this.mod_id = mod_id;
	}

	public String getEnt_id_lst() {
		return this.ent_id_lst;
	}
	public void setEnt_id_lst(String ent_id_lst) {
		this.ent_id_lst = ent_id_lst;
	}
}

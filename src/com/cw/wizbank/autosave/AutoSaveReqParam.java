package com.cw.wizbank.autosave;

import java.sql.Timestamp;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

public class AutoSaveReqParam extends ReqParam{
	
	public long cur_tkh_id; 
	public long cur_mod_id;
	public long cur_que_id;
	//public long cur_int_order;
	public long autosave_pos;
	//public long autosave_order;
	public int time_left;
	public String flag;
	//public String dbflag;
	//public String responseBil;
	public String responsebil_order_flag;
	public long mod_id;
	public long tkh_id;
	public boolean save_by_usr;
	public Timestamp start_time;
	public int int_size;
	
	public AutoSaveReqParam(
		ServletRequest inReq,
		String clientEnc_,
		String encoding_)
		throws cwException {
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		super.common();
	}
	
	public void getAutoSaveParam () throws cwException {
		cur_mod_id = getLongParameter("cur_mod_id");
		cur_tkh_id = getLongParameter("cur_tkh_id");
		cur_que_id = getLongParameter("cur_que_id");
		//cur_int_order = getLongParameter("cur_int_order");
		autosave_pos = getLongParameter("autosave_pos");
		//autosave_order = getLongParameter("autosave_order");
		time_left = getIntParameter("time_left");
		flag = getStringParameter("flag");
		//dbflag = getStringParameter("db_flag" + cur_int_order);
		//responseBil = unicode(getStringParameter("autosave_q" + autosave_pos + "_i" + cur_int_order));
		responsebil_order_flag = unicode(getStringParameter("responsebil_order_dbflag"));
		mod_id = getLongParameter("mod_id");
		tkh_id = getLongParameter("tkh_id");
		save_by_usr = getBooleanParameter("save_by_usr");
		start_time = getTimestampParameter("start_time");
		int_size = getIntParameter("int_size");
		return;
	}
	
	public String getCurInteraction (int i) throws cwException {
		return unicode(getStringParameter("responsebil_order_dbflag"+i));
	}
	
}

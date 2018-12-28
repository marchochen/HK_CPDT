package com.cw.wizbank.JsonMod.credit;

import java.sql.Connection;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class CreditModuleAccess {
	private WizbiniLoader wizbini = null; 
	private loginProfile prof = null;
	private Connection con = null;
	private CreditModuleParam modParam = null;
	
	public CreditModuleAccess(WizbiniLoader wizbini, loginProfile prof, Connection con, CreditModuleParam modParam){
		this.wizbini = wizbini;
		this.prof = prof;
		this.con = con;
		this.modParam = modParam;
	}
	
	public void process() throws cwException {
		if (prof == null || prof.usr_ent_id == 0) {
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		}
	}
}
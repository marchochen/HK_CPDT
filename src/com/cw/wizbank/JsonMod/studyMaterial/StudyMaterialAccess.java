package com.cw.wizbank.JsonMod.studyMaterial;

import java.sql.Connection;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class StudyMaterialAccess {
	private loginProfile prof = null;
	private Connection con = null;
	private StudyMaterialModuleParam modParam = null;

	public StudyMaterialAccess(loginProfile prof, Connection con, StudyMaterialModuleParam modParam) {
		this.prof = prof;
		this.modParam = modParam;
		this.con = con;
	}

	public void process() throws cwException {
		if (prof == null || prof.usr_ent_id == 0) {
			throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
		} else if (modParam.getCmd().equalsIgnoreCase(StudyMaterialModule.GET_SM_CENTER)) {
		} else if(modParam.getCmd().equalsIgnoreCase(StudyMaterialModule.ADV_SRH_RES_PREP)) {
		} else if(modParam.getCmd().equalsIgnoreCase(StudyMaterialModule.GET_TC_CAT_TREE)) {
		} else if(modParam.getCmd().equalsIgnoreCase(StudyMaterialModule.ADV_SRH_RES)) {
		} else if(modParam.getCmd().equalsIgnoreCase(StudyMaterialModule.GET_CAT_DETAIL_LST)) {
		} else {
			throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
		}
	}
}

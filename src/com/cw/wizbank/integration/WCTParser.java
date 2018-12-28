package com.cw.wizbank.integration;

import java.sql.Connection;

import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
*
* Parse the submited WCT related parameters to suit our system.
*/
public class WCTParser extends ResourceParser {

	private static final int WCT_URL_MAX_LENGTH = 255;
	private static final int WCT_ANNOTATE_MAX_LENGTH = 2000;
	private static final String[] WCT_URL_DEFAULT_PREFIX = { "http://" };

	private static final String WCT_DEAFULT_LANGUAGE = "ISO-8859-1";
	private static final String WCT_DEFAULT_PRIVILEGE = dbResource.RES_PRIV_CW;
	private static final String WCT_DEFAULT_SRC_TYPE = "URL";
	private static final String WCT_DEFAULT_RES_TYPE = dbResource.RES_TYPE_GEN;
	protected static final String TYPE = "WCT";

	private String res_url;
	private String res_annotate;

	protected void parse(Connection con, loginProfile prof)
		throws cwException {
		
		//Checked in ResourceReqParam
		//ResourceUtils.checkCatalogId(con, this.cat_id);
		try {
			dbResource dbRes = new dbResource();
			dbRes.res_annotation = cwUtils.esc4XML(this.res_annotate);
			if (this.html_ind && this.res_annotate.length() > 0) {
				dbRes.res_annotation =
					HTML_OPEN_TAG + dbRes.res_annotation + HTML_CLOSE_TAG;
			}
			dbRes.res_desc = this.res_desc;
			dbRes.res_difficulty = this.res_diff;
			dbRes.res_duration = this.res_dur;
			dbRes.res_lan = WCT_DEAFULT_LANGUAGE;
			dbRes.res_privilege = WCT_DEFAULT_PRIVILEGE;
			dbRes.res_src_link = this.res_url;
			dbRes.res_src_type = WCT_DEFAULT_SRC_TYPE;
			dbRes.res_status = this.res_status;
			dbRes.res_subtype = TYPE;
			dbRes.res_title = this.res_title;
			dbRes.res_type = WCT_DEFAULT_RES_TYPE;
			dbRes.res_upd_user = prof.usr_id;
			dbRes.res_usr_id_owner = prof.usr_id;
			String[] catId = new String[this.cat_id.length];
			for(int i=0; i<catId.length; i++) {
				catId[i] = Long.toString(this.cat_id[i]);
			}
			dbRes.ins_res2(con, catId, prof);
			return;
		} catch (Exception e) {
			throw new cwException(ResourceUtils.getUnexpectedFailureCode());
		}
	}

	/**
	 * Sets resource url.
	 * @param value a string
	 * @throws cwException Throwns if value is null 
	 * or exceed length ( WCT_URL_MAX_LENGTH )
	 */
	protected void setResUrl(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_WCT_URL,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}

//The url may be a relative path, comment the following checking
/*
		boolean validUrl = false;
		for (int i = 0; i < WCT_URL_DEFAULT_PREFIX.length; i++) {
			if (value.startsWith(WCT_URL_DEFAULT_PREFIX[i])) {
				validUrl = true;
				break;
			}
		}
		if (!validUrl) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_WCT_URL,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
*/
		if (value.length() <= WCT_URL_MAX_LENGTH) {
			this.res_url = value;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_WCT_URL,
					ResourceUtils.ERROR_INVALID_LENGTH));
		}
		return;
	}


	/**
	 * Sets resource annotate.
	 * @param value a string
	 * @throws cwException Throwns if value exceed default length
	 */
	protected void setResAnnotate(String value) throws cwException {
		if (value == null) {
			this.res_annotate = "";
			return;
		} else {
			if (value.length() <= WCT_ANNOTATE_MAX_LENGTH) {
				this.res_annotate = value;
			} else {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_WCT_ANNOTATE,
						ResourceUtils.ERROR_INVALID_LENGTH));
			}
		}
		return;
	}

}

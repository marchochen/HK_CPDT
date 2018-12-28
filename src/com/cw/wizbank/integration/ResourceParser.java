package com.cw.wizbank.integration;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.accesscontrol.AcResources;

import java.sql.Connection;
import java.util.StringTokenizer;
/**
 *
 * Parse the submited RESOURCE related parameters to suit our system. 
 */
public abstract class ResourceParser {
	
	protected final static String CATALOG_ID_DELIMITER = ",";
	protected final static int RES_TITLE_MAX_LENGTH = 255;
	protected final static String RES_DEFAULT_QUE_TYPE =
		dbResource.RES_TYPE_QUE;
	protected final static int RES_DEFAULT_TEXT_LENGTH = 10000;
	protected final static int RES_DEFAULT_MIN_DIFF = 1;
	protected final static int RES_DEFAULT_MAX_DIFF = 3;
	protected final static float RES_DEFAULT_MIN_DUR = 0f;
	protected final static float RES_DEFAULT_MAX_DUR = 999999999.99f;
	protected final static String RES_STATUS_ON = "ON";
	protected final static String RES_STATUS_OFF = "OFF";
	protected final static String RES_HTML_IND_YES = "Y";
	protected final static String RES_HTML_IND_NO = "N";
	protected static final String HTML_OPEN_TAG = "<html>";
	protected static final String HTML_CLOSE_TAG = "</html>";

	long site_id;
	long[] cat_id;
	String res_type;
	String res_title;
	int res_diff;
	float res_dur;
	String res_status;
	String res_desc;
	boolean html_ind;
	
	/**
	 * Parse the submited parameters and save resource to database.
	 * @param con database connection
	 * @param prof a logn profile
	 * @throws cwException Thrown if error parameters
	 */
	protected abstract void parse(Connection con, loginProfile prof)
		throws cwException;

	/**
	 * Saves question to database and assigned resource permission by user role.
	 * @param con database connection
	 * @param prof a login profile
	 * @param extQ an extendQue object
	 * @throws cwException Thrown if expected error occurs
	 */
	protected void saveQ(Connection con, loginProfile prof, extendQue extQ)
		throws cwException {
		try {

			AcResources acres = new AcResources(con);
			boolean read = acres.hasResPermissionRead(prof.current_role);
			boolean write = acres.hasResPermissionWrite(prof.current_role);
			boolean execute = acres.hasResPermissionExec(prof.current_role);

			dbQuestion dbQue = new dbQuestion();
			dbQue.res_lan = extQ.lan;
			dbQue.res_upd_user = extQ.owner_id;
			dbQue.res_usr_id_owner = extQ.owner_id;
			dbQue.res_title = extQ.title;
			dbQue.res_desc = extQ.desc;
			dbQue.res_type = RES_DEFAULT_QUE_TYPE;
			dbQue.res_subtype = extQ.inter[0].type;
			dbQue.res_duration = extQ.dur;
			dbQue.res_difficulty = extQ.diff;
			dbQue.res_privilege = extQ.folder;
			dbQue.res_status = extQ.onoff;
			dbQue.que_type = extQ.inter[0].type;
			dbQue.que_int_count = extQ.inter.length;
			dbQue.que_score = extQ.getScore();
			dbQue.que_xml = extQ.getBody();

			for (int i = 0; i < dbQue.que_int_count; i++) {
				dbInteraction dbInt = new dbInteraction();
				dbInt.int_order = (i + 1);
				dbInt.int_xml_outcome = extQ.inter[i].getOutcome();
				dbInt.int_xml_explain = extQ.inter[i].getExplanation();
				dbQue.ints.addElement(dbInt);
			}
			String[] catId = { Long.toString(extQ.obj_id[0])};
			dbQue.ins(con, catId, dbResource.RES_TYPE_QUE);
			dbResourcePermission.save(
				con,
				dbQue.que_res_id,
				dbQue.res_usr_id_owner,
				prof.current_role,
				read,
				write,
				execute);

			if (dbQue.res_privilege.equalsIgnoreCase(dbResource.RES_PRIV_CW)) {
				if (dbObjective
					.isPublicObjective(con, Long.parseLong(catId[0]))) {
					dbResourcePermission.save(
						con,
						dbQue.que_res_id,
						0,
						null,
						true,
						false,
						false);
				} else {
					dbResourcePermission.save(
						con,
						dbQue.que_res_id,
						prof.root_ent_id,
						null,
						true,
						false,
						false);
				}
			}
			return;
		} catch (Exception e) {
			throw new cwException(ResourceUtils.getUnexpectedFailureCode());
		}
	}
	
	
	/**
	 * Sets site id.
	 * @param value a string
	 * @throws cwException Throwns if value is nuill or not a long value
	 */
	protected void setSiteId(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_SITE_ID,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		try {
			this.site_id = Long.parseLong(value);
		} catch (Exception e) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_SITE_ID,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		return;
	}

	/**
	 * Sets catalog id. eg: 2,3,4
	 * @param value a string
	 * @throws cwException Throwns if value is null or not a long value
	 */
	protected void setCatId(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_CAT_ID,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		try {
			StringTokenizer tokens = new StringTokenizer(value, CATALOG_ID_DELIMITER);
			this.cat_id = new long[tokens.countTokens()];
			if( this.cat_id.length == 0 ) {
				throw new cwException();
			}
			int count = 0;
			while(tokens.hasMoreTokens()) {
				this.cat_id[count++] = Long.parseLong(tokens.nextToken());
			}
		} catch (Exception e) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_CAT_ID,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		return;
	}
	
	/**
	 * Sets resource title.
	 * @param value s string
	 * @throws cwException Throwns if value is null or exceed default length 
	 * (RES_DEFAULT_TEXT_LENGTH)
	 */
	protected void setResTitle(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_TITLE,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		if (value.length() <= RES_TITLE_MAX_LENGTH) {
			this.res_title = value;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_TITLE,
					ResourceUtils.ERROR_INVALID_LENGTH));
		}
		return;
	}
	
	
	/**
	 * Sets resource difficulty
	 * @param value a string
	 * @throws cwException Throwns if value is null or not an integer 
	 * or exceed range (RES_DEFAULT_MIN_DIFF, RES_DEFAULT_MAX_DIFF)
	 */
	protected void setResDiff(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_DIFF,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		try {
			this.res_diff = Integer.parseInt(value);
		} catch (Exception e) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_DIFF,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		if (this.res_diff < RES_DEFAULT_MIN_DIFF
			|| this.res_diff > RES_DEFAULT_MAX_DIFF) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_DIFF,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		return;
	}


	/**
	 * Sets resource duration
	 * @param value a string
	 * @throws cwException Throwns if value is null or not a number 
	 * or exceed range (RES_DEFAULT_MIN_DUR, RES_DEFAULT_MAX_DUR)
	 */
	protected void setResDur(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_DUR,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		try {
			this.res_dur = Float.parseFloat(value);
		} catch (Exception e) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_DUR,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		if (this.res_dur <= RES_DEFAULT_MIN_DUR
			|| this.res_dur > RES_DEFAULT_MAX_DUR) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_DUR,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		return;
	}
	
	/**
	 * Sets resource status.
	 * @param value a string
	 * @throws cwException Throwns if value is null or not a default value 
	 * ( RES_STATUS_ON, RES_STATUS_OFF )
	 */
	protected void setResStatus(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_STATUS,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		
		if( value.equalsIgnoreCase(RES_STATUS_ON) ) {
			this.res_status = RES_STATUS_ON;
		} else if ( value.equalsIgnoreCase(RES_STATUS_OFF) ) {
			this.res_status = RES_STATUS_OFF;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_RES_STATUS,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		return;
	}
	
	/**
	 * Sets resource description.
	 * @param value a string
	 * @throws cwException Throwns if value exceed default length 
	 * (RES_DEFAULT_TEXT_LENGTH)
	 */
	protected void setResDesc(String value) throws cwException {
		if (value == null) {
			this.res_desc = "";
		} else {
			if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
				this.res_desc = value;
			} else {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_RES_DESC,
						ResourceUtils.ERROR_INVALID_LENGTH));
			}
		}
		return;
	}
	
	/**
	 * Sets html indicator. Default value is false.
	 * @param value a string
	 * @throws cwException Throwns if value not a default value 
	 * (RES_HTML_IND_YES, RES_HTML_IND_NO)
	 */
	protected void setHtmlInd(String value) throws cwException {
		if (value == null) {
			this.html_ind = false;
			return;
		}
		
		if( value.equalsIgnoreCase(RES_HTML_IND_YES) ) {
			this.html_ind = true;
		} else if( value.equalsIgnoreCase(RES_HTML_IND_NO) ) {
			this.html_ind = false;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_HTML_IND,
					ResourceUtils.ERROR_INVALID_FORMAT));

		}
		
		return;
	}
}

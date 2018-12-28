package com.cw.wizbank.integration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.accesscontrol.acSite;

/**
 *
 * Resources integration related utils.
 */
public class ResourceUtils {

	private static final String CODE_SUCCESS_PREFIX = "DIS";
	private static final String CODE_FAILURE_PREFIX = "DIF";
	private static final String CODE_DELIMITER = "-";

	//Error Code
	protected static final String ERROR_INVALID_LENGTH = "01";
	protected static final String ERROR_EMPTY_PARAM = "02";
	protected static final String ERROR_INVALID_FORMAT = "03";
	protected static final String ERROR_NOT_EXIST = "04";

	//Code for resource
	protected static final String CODE_CMD_VALUE = "000";
	protected static final String CODE_SITE_ID = "001";
	protected static final String CODE_RES_TYPE = "002";
	protected static final String CODE_CAT_ID = "003";
	protected static final String CODE_CAT_PATH = "003";
	protected static final String CODE_RES_TITLE = "004";
	protected static final String CODE_RES_DIFF = "005";
	protected static final String CODE_RES_DUR = "006";
	protected static final String CODE_RES_STATUS = "007";
	protected static final String CODE_HTML_IND = "008";
	protected static final String CODE_RES_DESC = "009";
	protected static final String CODE_CAT_PATH_DELIMITER = "010";

	//Code for WCT
	protected static final String CODE_WCT_URL = "101";
	protected static final String CODE_WCT_ANNOTATE = "102";

	//Code for MC
	protected static final String CODE_MC_QUE_BODY = "200";
	protected static final String CODE_MC_OPTION_01 = "201";
	protected static final String CODE_MC_OPTION_02 = "202";
	protected static final String CODE_MC_OPTION_03 = "203";
	protected static final String CODE_MC_OPTION_04 = "204";
	protected static final String CODE_MC_OPTION_05 = "205";
	protected static final String CODE_MC_OPTION_06 = "206";
	protected static final String CODE_MC_OPTION_07 = "207";
	protected static final String CODE_MC_OPTION_08 = "208";
	protected static final String CODE_MC_OPTION_09 = "209";
	protected static final String CODE_MC_OPTION_10 = "210";
	protected static final String CODE_MC_ANSWER = "211";
	protected static final String CODE_MC_SCORE = "212";
	protected static final String CODE_MC_SHUFFLE_IND = "213";
	protected static final String CODE_MC_EXP = "214";

	//Code for TF
	protected static final String CODE_TF_QUE_BODY = "301";
	protected static final String CODE_TF_ANSWER = "302";
	protected static final String CODE_TF_SCORE = "303";
	protected static final String CODE_TF_EXP = "304";

	//Code for FB
	protected static final String CODE_FB_QUE_BODY = "400";
	protected static final String CODE_FB_BLANK_ANSWER_01 = "401";
	protected static final String CODE_FB_BLANK_ANSWER_02 = "402";
	protected static final String CODE_FB_BLANK_ANSWER_03 = "403";
	protected static final String CODE_FB_BLANK_ANSWER_04 = "404";
	protected static final String CODE_FB_BLANK_ANSWER_05 = "405";
	protected static final String CODE_FB_BLANK_ANSWER_06 = "406";
	protected static final String CODE_FB_BLANK_ANSWER_07 = "407";
	protected static final String CODE_FB_BLANK_ANSWER_08 = "408";
	protected static final String CODE_FB_BLANK_ANSWER_09 = "409";
	protected static final String CODE_FB_BLANK_ANSWER_10 = "410";
	protected static final String CODE_FB_BLANK_SCORE_01 = "411";
	protected static final String CODE_FB_BLANK_SCORE_02 = "412";
	protected static final String CODE_FB_BLANK_SCORE_03 = "413";
	protected static final String CODE_FB_BLANK_SCORE_04 = "414";
	protected static final String CODE_FB_BLANK_SCORE_05 = "415";
	protected static final String CODE_FB_BLANK_SCORE_06 = "416";
	protected static final String CODE_FB_BLANK_SCORE_07 = "417";
	protected static final String CODE_FB_BLANK_SCORE_08 = "418";
	protected static final String CODE_FB_BLANK_SCORE_09 = "419";
	protected static final String CODE_FB_BLANK_SCORE_10 = "420";

	protected static final int CATALOG_FULL_PATH_DELIMITER_LENGTH = 5;

	/**
	 * Gets success code.
	 * @return a code
	 */
	protected static String getSuccessCode() {
		return ResourceUtils.CODE_SUCCESS_PREFIX + "001";
	}

	/**
	 * Gets unexpected error code.
	 * @return a code
	 */
	protected static String getUnexpectedFailureCode() {
		return ResourceUtils.CODE_FAILURE_PREFIX + "001";
	}

	/**
	 * Constructs a failure code.
	 * @param code a parameter code
	 * @param error an error code
	 * @return a code
	 */
	protected static String getFailureCode(String code, String error) {
		return ResourceUtils.CODE_FAILURE_PREFIX
			+ ResourceUtils.CODE_DELIMITER
			+ code
			+ ResourceUtils.CODE_DELIMITER
			+ error;
	}

	/**
	 * Gets option parameter code by option number.
	 * @param index an option number
	 * @return a code
	 */
	protected static String getOptionCode(int index) {

		switch (index) {
			case 1 :
				return CODE_MC_OPTION_01;
			case 2 :
				return CODE_MC_OPTION_02;
			case 3 :
				return CODE_MC_OPTION_03;
			case 4 :
				return CODE_MC_OPTION_04;
			case 5 :
				return CODE_MC_OPTION_05;
			case 6 :
				return CODE_MC_OPTION_06;
			case 7 :
				return CODE_MC_OPTION_07;
			case 8 :
				return CODE_MC_OPTION_08;
			case 9 :
				return CODE_MC_OPTION_09;
			case 10 :
				return CODE_MC_OPTION_10;
			default :
				return "";
		}
	}

	/**
	 * Gets parameter blank answer code by blank number.
	 * @param index a blank number
	 * @return a code
	 */
	protected static String getBlankAnswerCode(int index) {
		switch (index) {
			case 1 :
				return CODE_FB_BLANK_ANSWER_01;
			case 2 :
				return CODE_FB_BLANK_ANSWER_02;
			case 3 :
				return CODE_FB_BLANK_ANSWER_03;
			case 4 :
				return CODE_FB_BLANK_ANSWER_04;
			case 5 :
				return CODE_FB_BLANK_ANSWER_05;
			case 6 :
				return CODE_FB_BLANK_ANSWER_06;
			case 7 :
				return CODE_FB_BLANK_ANSWER_07;
			case 8 :
				return CODE_FB_BLANK_ANSWER_08;
			case 9 :
				return CODE_FB_BLANK_ANSWER_09;
			case 10 :
				return CODE_FB_BLANK_ANSWER_10;
			default :
				return "";
		}
	}

	/**
	 * Gets parameter blank score code by blank number.
	 * @param index a blank number
	 * @return a code
	 */
	protected static String getBlankScoreCode(int index) {
		switch (index) {
			case 1 :
				return CODE_FB_BLANK_SCORE_01;
			case 2 :
				return CODE_FB_BLANK_SCORE_02;
			case 3 :
				return CODE_FB_BLANK_SCORE_03;
			case 4 :
				return CODE_FB_BLANK_SCORE_04;
			case 5 :
				return CODE_FB_BLANK_SCORE_05;
			case 6 :
				return CODE_FB_BLANK_SCORE_06;
			case 7 :
				return CODE_FB_BLANK_SCORE_07;
			case 8 :
				return CODE_FB_BLANK_SCORE_08;
			case 9 :
				return CODE_FB_BLANK_SCORE_09;
			case 10 :
				return CODE_FB_BLANK_SCORE_10;
			default :
				return "";
		}
	}

	/**
	 * Parse the string to integer.
	 * 
	 * @param value string to be parsed
	 * @return int a integer
	 * @throws NumberFormatException Thrown if invalid number format 
	 * or not a integer.
	 */
	/*
	protected static int parseInt(String value) throws NumberFormatException {
		try {
			int val = Integer.parseInt(value);
			if (val != Float.parseFloat(value)) {
				throw new NumberFormatException("Not a Integer.");
			}
			return val;
		} catch (Exception e) {
			throw new NumberFormatException("Invalid Number Format.");
		}
	
	}
	*/

	/**
	 * Check the existence of catalog id.
	 * @param con database connection
	 * @param catId a catalog id
	 * @throws cwException Throwns if catalog not exist
	 */
	protected static void checkCatalogId(Connection con, long[] catId)
		throws cwException {

		dbObjective dbObj = new dbObjective();
		for (int i = 0; i < catId.length; i++) {
			dbObj.obj_id = catId[i];
			try {
				dbObj.get(con);
			} catch (Exception e) {
				throw new cwException(
					getFailureCode(CODE_CAT_ID, ERROR_NOT_EXIST));
			}
			return;
		}
	}

	/**
	 * Gets catalog id by catalog full path.
	 * @param con database connection
	 * @param prof user profile for create catalog if necessary
	 * @param catalogPath catalog full path
	 * @param catalogPathDelimiter catalog full path delimiter
	 * @param b create not exist catalog, true to create and vice versa
	 * @return catalog id
	 * @throws cwException Throwns if catalog not exist or null/EMPTY is submitted
	 */
	protected static long[] getCatalogId(
		Connection con,
		loginProfile prof,
		String catalogPath,
		String catalogPathDelimiter,
		boolean b)
		throws cwException {
			
		long[] cat_id = null;
		if (catalogPath == null || catalogPath.length() == 0) {
			throw new cwException(
				getFailureCode(CODE_CAT_PATH, ERROR_NOT_EXIST));
		}

		if (catalogPathDelimiter == null
			|| catalogPathDelimiter.length() == 0) {
			throw new cwException(
				getFailureCode(CODE_CAT_PATH_DELIMITER, ERROR_NOT_EXIST));
		} else if (
			catalogPathDelimiter.length()
				> CATALOG_FULL_PATH_DELIMITER_LENGTH) {
			throw new cwException(
				getFailureCode(CODE_CAT_PATH_DELIMITER, ERROR_INVALID_LENGTH));
		}

		StringTokenizer tokens =
			new StringTokenizer(catalogPath, catalogPathDelimiter);
		long[] parentId = null;
		long[] tmpId = null;
		String catalogName = null;
		boolean isNew = false;
		try {
			while (tokens.hasMoreTokens()) {
				catalogName = tokens.nextToken();
				if (!isNew) {
					tmpId =
						dbObjective.getCatalogId(con, catalogName, parentId);
				}
				if (tmpId == null || tmpId.length == 0) {
					if (b) {
						isNew = true;
						dbObjective dbObj = new dbObjective();
						dbObj.obj_syl_id = prof.root_ent_id;
						dbObj.obj_type = dbObjective.OBJ_TYPE_SYB;
						dbObj.obj_desc = catalogName;
						if (parentId == null || parentId.length == 0) {
							dbObj.obj_obj_id_parent = 0;
							dbObj.ins(con, prof);
							parentId = new long[1];
							parentId[0] = dbObj.obj_id;
						} else {
							for (int i = 0; i < parentId.length; i++) {
								dbObj.obj_obj_id_parent = parentId[i];
								dbObj.ins(con, prof);
								parentId[i] = dbObj.obj_id;
							}
						}
					}
				} else {
					parentId = tmpId;
				}
			}

			if (parentId == null || parentId.length == 0) {
				throw new Exception();
			} else {
				cat_id = parentId;
			}
		} catch (Exception e) {
			throw new cwException(
				getFailureCode(CODE_CAT_PATH, ERROR_NOT_EXIST));
		}
		return cat_id;
	}

	/**
	 * Gets catalog id list by catalog full path.
	 * @param con database connection
	 * @param prof user profile for create catalog if necessary
	 * @param catalogPath catalog full path
	 * @param catalogPathDelimiter catalog full path delimiter
	 * @param b create not exist catalog, true to create and vice versa
	 * @return catalog id list
	 * @throws cwException Throwns if catalog not exist or null/EMPTY is submitted
	 */
	protected static String getCatalogIdList(
		Connection con,
		loginProfile prof,
		String catalogPath,
		String catalogPathDelimiter,
		boolean b)
		throws cwException {
			
		StringBuffer list = new StringBuffer(256);
		long[] id =
			getCatalogId(con, prof, catalogPath, catalogPathDelimiter, b);
		for (int i = 0; i < id.length; i++) {
			list.append(id[i]).append(",");
		}
		if (list.length() > 0) {
			list.delete(list.length() - 1, list.length());
		}
		return list.toString();
	}


	/**
	 * Get system user login profile.
	 * 
	 * @param con Database connection
	 * @param siteId Organization Id
	 * @param wizbini configuration object.
	 * @return loginProfile of System user.
	 * @throws cwException Thrown if login failure.
	 */
	protected static loginProfile getProf(
		Connection con,
		long siteId,
		WizbiniLoader wizbini)
		throws cwException {
		try {
			String sysUsrId = acSite.getSysUsrId(con, siteId);
			loginProfile prof = new loginProfile();
			acSite site = new acSite();
			site.ste_ent_id = siteId;
			dbRegUser dbUsr = new dbRegUser();
			dbUsr.get(con, sysUsrId);
			String code =
				dbRegUser.login(
					con,
					prof,
					dbUsr.usr_ste_usr_id,
					dbUsr.usr_pwd,
					site,
					true,
					wizbini);
			if (!code.equals(dbRegUser.CODE_LOGIN_SUCCESS)) {
				throw new cwException(ResourceUtils.getUnexpectedFailureCode());
			}
			return prof;
		} catch (Exception e) {
			throw new cwException(e.getMessage());
		}
	}

	/**
	 * Check from database is the specified domain trusted 
	 * by the organization.
	 * 
	 * @param con Database Connection
	 * @param remoteHost Host to be checked
	 * @param referer Site referer
	 * @param siteId Organization id
	 * @return boolean true if host is trusted by organization
	 * @throws SQLException
	 */
	protected static boolean isTrustDomain(
		Connection con,
		String remoteHost,
		String referer,
		long siteId)
		throws SQLException {

		acSite site = new acSite();
		site.ste_ent_id = siteId;
		site.get(con);
		if (site.ste_domain != null) {
			String[] validDomains =
				cwUtils.splitToString(site.ste_domain, acSite.DOMAIN_SEPARATOR);
			for (int i = 0; i < validDomains.length; i++) {
				if (remoteHost != null
					&& remoteHost.toLowerCase().indexOf(
						validDomains[i].toLowerCase()) >= 0) {
					return true;
				}
				if (referer != null
					&& referer.toLowerCase().indexOf(
						validDomains[i].toLowerCase()) >= 0) {
					return true;
				}
			}
		}
		return false;
	}


}

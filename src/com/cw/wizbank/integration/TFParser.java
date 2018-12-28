package com.cw.wizbank.integration;

import java.sql.Connection;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.extendQue;
import com.cw.wizbank.util.cwException;

/**
 * Parse the submited TF related parameters to suit our system.
 * Some methods are overrided methods because it throw differnet ERROR CODE. 
 */
public class TFParser extends MCParser {

	private static final String TF_OPTION_TRUE_VALUE = "TRUE";
	private static final String TF_OPTION_FALSE_VALUE = "FALSE";

	private static final String TF_ANSWER_T = "T";
	private static final String TF_ANSWER_F = "F";
	
	private static final int TF_DEFAULT_MIN_SCORE = 0;

	protected static final String TYPE = "TF";

	private static final String TF_DEAFULT_LANGUAGE = "ISO-8859-1";
	private static final String TF_DEFAULT_PRIVILEGE = dbResource.RES_PRIV_CW;

	protected TFParser() {
		super();
		return;
	}

	protected void parse(Connection con, loginProfile prof)
		throws cwException {
			//Checked in ResourceReqParam
			//ResourceUtils.checkCatalogId(con, this.cat_id);
			setOptionValue();
			for(int i=0; i<this.cat_id.length; i++) {
				extendQue extQ = constructExtendQue(prof, 2, this.cat_id[i]);
				saveQ(con, prof, extQ);
			}
			return;
		}

	/**
	 * Sets 2 options 'True' and 'False' for the question. 
	 */
	private void setOptionValue() {
		this.que_option.put(new Integer(1), TF_OPTION_TRUE_VALUE);
		this.que_option.put(new Integer(2), TF_OPTION_FALSE_VALUE);
		return;
	}
	
	
	protected extendQue constructExtendQue(loginProfile prof, int optionSize, long catalogId) {
		extendQue extQ = super.constructExtendQue(prof, optionSize, catalogId);
		extQ.inter[0].type = TYPE;
		return extQ;
	}

	/**
	 * Sets question answer.
	 * @param value a string
	 * @throws cwException Throwns if value is null 
	 * or invalid value ( TF_ANSWER_T, TF_ANSWER_F )
	 */
	protected void setAnswer(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_TF_ANSWER,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}

		if (value.equalsIgnoreCase(TF_ANSWER_T)) {
			this.que_answer = new int[1];
			this.que_answer[0] = 1;
		} else if (value.equalsIgnoreCase(TF_ANSWER_F)) {
			this.que_answer = new int[1];
			this.que_answer[0] = 2;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_TF_ANSWER,
					ResourceUtils.ERROR_NOT_EXIST));
		}
		return;
	}

	protected void setQueBody(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_TF_QUE_BODY,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
			this.que_body = value;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_TF_QUE_BODY,
					ResourceUtils.ERROR_INVALID_LENGTH));
		}
		return;
	}

	protected void setQueScore(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_TF_SCORE,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		try {
			this.que_score = Integer.parseInt(value);
		} catch (Exception e) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_TF_SCORE,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		if( this.que_score <= TF_DEFAULT_MIN_SCORE ) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_TF_SCORE,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		
		return;
	}

	protected void setQueExp(String value) throws cwException {
		if (value == null) {
			this.que_exp = "";
		} else {
			if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
				this.que_exp = value;
			} else {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_TF_EXP,
						ResourceUtils.ERROR_INVALID_LENGTH));
			}
		}
		return;
	}

}

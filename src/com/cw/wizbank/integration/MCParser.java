package com.cw.wizbank.integration;

import java.sql.Connection;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.extendQue;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * Parse the submited MC related parameters to suit our system.
 */
public class MCParser extends ResourceParser {

	private final static String MC_ANSWER_DELIMITER = ",";
	private static final String MC_DEAFULT_LANGUAGE = "ISO-8859-1";
	private static final String MC_DEFAULT_PRIVILEGE = dbResource.RES_PRIV_CW;
	private static final int MC_DEFAULT_MIN_SCORE = 0; 

	protected static final String TYPE = "MC";

	String que_body;
	Hashtable que_option;
	boolean shuffle_ind;
	int[] que_answer;
	int que_score;
	String que_exp;

	protected MCParser() {
		que_option = new Hashtable();
		return;
	}

	protected void parse(Connection con, loginProfile prof)
		throws cwException {
		
		int optionSize = getOptionSize();
		checkAnswer(optionSize);
		//Checked in ResourceReqParam
		//ResourceUtils.checkCatalogId(con, this.cat_id);
		for(int i=0; i<this.cat_id.length; i++) {
			extendQue extQ = constructExtendQue(prof, optionSize, this.cat_id[i]);
			saveQ(con, prof, extQ);
		}
		return;

	}

	/**
	 * Constructs an extendQue object.
	 * @param prof a login profile
	 * @param optionSize option size
	 * @param catalogId catalog id
	 * @return en extendQue object
	 */
	protected extendQue constructExtendQue(loginProfile prof, int optionSize, long catalogId) {

		extendQue extQ = new extendQue(1, 1, optionSize, 0, 0);
		extQ.obj_id[0] = catalogId;
		extQ.title = this.res_title;
		extQ.cont = cwUtils.esc4XML(this.que_body);
		if (this.html_ind) {
			extQ.cont =	HTML_OPEN_TAG + extQ.cont + HTML_CLOSE_TAG;
		}
		if (this.shuffle_ind) {
			extQ.inter[0].shuffle = "Y";
		} else {
			extQ.inter[0].shuffle = "N";
		}
		extQ.diff = this.res_diff;
		extQ.dur = this.res_dur;
		extQ.onoff = this.res_status;
		extQ.desc = this.res_desc;
		for (int i = 0; i < optionSize; i++) {
			extQ.inter[0].opt[i].cont =
				cwUtils.esc4XML(
					(String) this.que_option.get(new Integer(i + 1)));
			if (this.html_ind) {
				extQ.inter[0].opt[i].cont =
					HTML_OPEN_TAG + extQ.inter[0].opt[i].cont + HTML_CLOSE_TAG;
			}
			
			extQ.inter[0].opt[i].exp = this.que_exp;
			extQ.inter[0].opt[i].cont_html = this.html_ind;
		}
		for (int i = 0; i < this.que_answer.length; i++) {
			extQ.inter[0].opt[this.que_answer[i] - 1].score = this.que_score;
		}
		extQ.lan = MC_DEAFULT_LANGUAGE;
		extQ.folder = MC_DEFAULT_PRIVILEGE;
		extQ.inter[0].type = TYPE;
		extQ.owner_id = prof.usr_id;
		if (this.que_answer.length == 1) {
			extQ.inter[0].logic = extendQue.MC_LOGIC_SINGLE;
		} else {
			extQ.inter[0].logic = extendQue.MC_LOGIC_AND;
		}
		return extQ;
	}


	/**
	 * Gets number of option submited.
	 * @return number of option
	 * @throws cwException Thrown if option size less than 2 or 
	 * an option skipped
	 */
	private int getOptionSize() throws cwException {
		int size = this.que_option.size();
		for (int i = 1; i <= size; i++) {
			if (!this.que_option.containsKey(new Integer(i))) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.getOptionCode(i),
						ResourceUtils.ERROR_EMPTY_PARAM));
			}
		}
		if (size == 0) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_OPTION_01,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		if (size == 1) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_OPTION_02,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		return size;
	}

	/**
	 * Checks answer value is valid.
	 * @param optionSize number of option
	 * @throws cwException Throwns if answer is larger than number of option
	 */
	private void checkAnswer(int optionSize) throws cwException {

		for (int i = 0; i < this.que_answer.length; i++) {
			if (this.que_answer[i] > optionSize) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_MC_ANSWER,
						ResourceUtils.ERROR_INVALID_FORMAT));
			}
		}
	}
	
	/**
	 * Sets question body.
	 * @param value a string
	 * @throws cwException Throwns if value is null or exceed default length
	 * (RES_DEFAULT_TEXT_LENGTH)
	 */	
	protected void setQueBody(String value) throws cwException {

		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_QUE_BODY,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
			this.que_body = value;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_QUE_BODY,
					ResourceUtils.ERROR_INVALID_LENGTH));
		}
		return;

	}

	/**
	 * Sets option value.
	 * @param value a string
	 * @param optionNum a option number
	 * @throws cwException Throwns if option number already existed 
	 * or value exceed length
	 */
	protected void setQueOption(String value, int optionNum)
		throws cwException {

		if (value == null) {
			return;
		} else {
			Integer optNum = new Integer(optionNum);
			if (que_option.containsKey(optNum)) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.getOptionCode(optionNum),
						ResourceUtils.ERROR_INVALID_FORMAT));
			} else {
				if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
					que_option.put(optNum, value);
				} else {
					throw new cwException(
						ResourceUtils.getFailureCode(
							ResourceUtils.getOptionCode(optionNum),
							ResourceUtils.ERROR_INVALID_LENGTH));
				}
			}
		}
		return;
	}
	
	
	/**
	 * Sets question shuffle indicator.
	 * @param value a string
	 * @throws cwException Throwns if value is null 
	 * or invalid value (RES_HTML_IND_YES, RES_HTML_IND_NO)
	 */
	protected void setShuffleInd(String value) throws cwException {
		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_SHUFFLE_IND,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}

		if (value.equalsIgnoreCase(RES_HTML_IND_YES)) {
			this.shuffle_ind = true;
		} else if (value.equalsIgnoreCase(RES_HTML_IND_NO)) {
			this.shuffle_ind = false;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_SHUFFLE_IND,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}

		return;
	}


	/**
	 * Sets question answer.
	 * @param value a string
	 * @throws cwException Throwns if value is null 
	 * or invalid option number or duplicated answer 
	 */
	protected void setAnswer(String value) throws cwException {

		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_ANSWER,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}

		StringTokenizer tokens =
			new StringTokenizer(value, MC_ANSWER_DELIMITER);
		this.que_answer = new int[tokens.countTokens()];
		if (this.que_answer.length == 0) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_ANSWER,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		int i = 0;
		while (tokens.hasMoreTokens()) {
			try {
				this.que_answer[i++] =
					Integer.parseInt(tokens.nextToken().trim());
			} catch (Exception e) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_MC_ANSWER,
						ResourceUtils.ERROR_INVALID_FORMAT));
			}
		}
		for (int j = 0; j < this.que_answer.length; j++) {
			for (int k = 0; k < this.que_answer.length; k++) {
				if (this.que_answer[j] == this.que_answer[k] && j != k) {
					throw new cwException(
						ResourceUtils.getFailureCode(
							ResourceUtils.CODE_MC_ANSWER,
							ResourceUtils.ERROR_INVALID_FORMAT));
				}
			}
		}
		return;
	}

	/**
	 * Sets question score.
	 * @param value a string
	 * @throws cwException Throwns if value is null or not an integer 
	 * or exceed range (MC_DEFAULT_MIN_SCORE)
	 */
	protected void setQueScore(String value) throws cwException {

		if (value == null) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_SCORE,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		try {
			this.que_score = Integer.parseInt(value);
		} catch (Exception e) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_SCORE,
					ResourceUtils.ERROR_INVALID_FORMAT));
		}
		if( this.que_score <= MC_DEFAULT_MIN_SCORE ) {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_MC_SCORE,
					ResourceUtils.ERROR_INVALID_FORMAT));			
		}
		return;
	}

	/**
	 * Sets question explanation.
	 * @param value a string
	 * @throws cwException Throwns if value exceed default length
	 */
	protected void setQueExp(String value) throws cwException {
		if (value == null) {
			this.que_exp = "";
		} else {
			if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
				this.que_exp = value;
			} else {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_MC_EXP,
						ResourceUtils.ERROR_INVALID_LENGTH));
			}
		}
		return;
	}

}

package com.cw.wizbank.integration;

import java.sql.Connection;
import java.util.Hashtable;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.extendQue;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * Parse the submited FB related parameters to suit our system.
 */
public class FBParser extends ResourceParser {

	private static final String FB_DEAFULT_LANGUAGE = "ISO-8859-1";
	private static final String FB_DEFAULT_PRIVILEGE = dbResource.RES_PRIV_CW;
	private static final String FB_DEFAULT_CASE_SENSE = "N";
	private static final String FB_DEFAULT_SPACE_SENSE = "N";
	private static final String FB_DEFAULT_OPTION_TYPE = "Text";
	private static final int FB_DEFAULT_MIN_SCORE = 0; 

	protected static final String TYPE = "FB";

	private static final String FB_BLANK_PREFIX = "[blank";
	private static final String FB_BLANK_TAG_NAME = "[blank]";

	private Hashtable que_blank_answer;
	private Hashtable que_blank_score;
	private String que_body;
	
	/**
	 * Initialize variables que_blank_answer and que_blank_score.
	 *
	 */
	protected FBParser() {
		this.que_blank_answer = new Hashtable();
		this.que_blank_score = new Hashtable();
		return;
	}
	
	protected void parse(Connection con, loginProfile prof)
		throws cwException {
			
			int blankSize = getBlankSize();
			checkBlank(blankSize);
			convertBlank(blankSize);
			//Checked in ResourceReqParam
			//ResourceUtils.checkCatalogId(con, this.cat_id);
			for(int i=0; i<this.cat_id.length; i++) {
				extendQue extQ = constructExtendQue(prof, blankSize, this.cat_id[i]);
				saveQ(con, prof, extQ);				
			}
			return;
		}

	/**
	 * Constructs an extendQue.
	 * @param prof a login profile
	 * @param blankSize blank size
	 * @param catalogId catalog id
	 * @return en extendQue object
	 */
	private extendQue constructExtendQue(loginProfile prof, int blankSize, long catalogId) {

		extendQue extQ = new extendQue(1, blankSize, 1, 0, 0);
		extQ.obj_id[0] = catalogId;
		extQ.title = this.res_title;
		extQ.cont = cwUtils.esc4XML(this.que_body);
		if (this.html_ind) {
			extQ.cont =	constructHtmlQueBody(extQ.cont);
		}
		extQ.diff = this.res_diff;
		extQ.dur = this.res_dur;
		extQ.onoff = this.res_status;
		extQ.desc = this.res_desc;
		extQ.lan = FB_DEAFULT_LANGUAGE;
		extQ.folder = FB_DEFAULT_PRIVILEGE;
		extQ.inter[0].type = TYPE;
		extQ.owner_id = prof.usr_id;

		for (int i = 0; i < blankSize; i++) {
			Integer I = new Integer(i + 1);
			extQ.inter[i].type = TYPE;
			extQ.inter[i].opt[0].cont =
				cwUtils.esc4XML((String) this.que_blank_answer.get(I));
			extQ.inter[i].att_len = extQ.inter[i].opt[0].cont.length();
			extQ.inter[i].opt[0].score =
				((Integer) this.que_blank_score.get(I)).intValue();
			extQ.inter[i].opt[0].case_sense = FB_DEFAULT_CASE_SENSE;
			extQ.inter[i].opt[0].spc_sense = FB_DEFAULT_SPACE_SENSE;
			extQ.inter[i].opt[0].type = FB_DEFAULT_OPTION_TYPE;
		}
		return extQ;
	}

	/**
	 * Constructs html question body. <br>
	 * eg. From: This is [blank] question.
	 * To : &lt;html>This is &lt;/htm>[blank]&lt;html> question.&lt;/html>
	 * @param queBody a string to be parsed
	 * @return a parsed string
	 */
	private String constructHtmlQueBody(String queBody) {
		StringBuffer strBuf = new StringBuffer(queBody);
		int cursor = 0;
		int beginSearch = 0;
		int blankLength = FB_BLANK_TAG_NAME.length();
		while ((cursor =
			strBuf.toString().indexOf(FB_BLANK_TAG_NAME, beginSearch))
			!= -1) {
			strBuf.insert(beginSearch, HTML_OPEN_TAG);
			strBuf.insert(cursor + HTML_OPEN_TAG.length(), HTML_CLOSE_TAG);
			//start search index should shift TAG length to find next BLANK.
			beginSearch =
				cursor
					+ blankLength
					+ HTML_OPEN_TAG.length()
					+ HTML_CLOSE_TAG.length();
		}
		if(beginSearch < strBuf.length()) {
			strBuf.insert(beginSearch, HTML_OPEN_TAG);
			strBuf.insert(strBuf.length(), HTML_CLOSE_TAG);
		}
		return strBuf.toString();
	}

	/**
	 * Convert all [blank no.] (eg. [blank 1]) to [blank].
	 * @param blankSize number of blank
	 */
	private void convertBlank(int blankSize) {
		for (int i = 1; i <= blankSize; i++) {
			String blank = FB_BLANK_PREFIX + "" + i + "]";
			int begin = this.que_body.indexOf(blank);
			int end = begin + blank.length();
			int length = this.que_body.length();
			this.que_body =
				this.que_body.substring(0, begin)
					+ FB_BLANK_TAG_NAME
					+ this.que_body.substring(end, length);
		}
		return;
	}

	/**
	 * Checks any blank value or score skipped.
	 * @param blankSize blank size
	 * @throws cwException Throwns if blank value or score skipped
	 */
	private void checkBlank(int blankSize) throws cwException {

		for (int i = 1; i <= blankSize; i++) {
			Integer I = new Integer(i);
			if (!this.que_blank_answer.containsKey(I)) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.getBlankAnswerCode(i),
						ResourceUtils.ERROR_EMPTY_PARAM));
			}
			if (!this.que_blank_score.containsKey(I)) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.getBlankScoreCode(i),
						ResourceUtils.ERROR_EMPTY_PARAM));
			}
		}
		return;
	}

	/**
	 * Count number of blank in question body.
	 * @return number of blank
	 * @throws cwException Thrown if blank format incorrect
	 */
	private int getBlankSize() throws cwException {
		
		int blankSize = 0;
		int index = 0;
		while( (index = this.que_body.indexOf(FB_BLANK_PREFIX, index)) != -1 ) {
			blankSize++;
			index++;
		}
		index = 0;
		for (int i = 1; i <= blankSize; i++) {
			String blank = FB_BLANK_PREFIX + "" + i + "]";
			//Check blank order
			if ((index = this.que_body.indexOf(blank, index)) == -1) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_FB_QUE_BODY,
						ResourceUtils.ERROR_INVALID_FORMAT));
			}
			//Check duplicate blank
			if (this.que_body.indexOf(blank, index + 1) != -1) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.CODE_FB_QUE_BODY,
						ResourceUtils.ERROR_INVALID_FORMAT));
			}
			index++;
		}
		return blankSize;
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
					ResourceUtils.CODE_FB_QUE_BODY,
					ResourceUtils.ERROR_EMPTY_PARAM));
		}
		if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
			this.que_body = value;
		} else {
			throw new cwException(
				ResourceUtils.getFailureCode(
					ResourceUtils.CODE_FB_QUE_BODY,
					ResourceUtils.ERROR_INVALID_LENGTH));
		}
		return;

	}


	/**
	 * Sets blank answer value.
	 * @param value a string
	 * @param blankNum a blank number
	 * @throws cwException Throwns if blank value already existed or 
	 * blank value exceed length (RES_DEFAULT_TEXT_LENGTH)
	 */
	protected void setBlankAnswer(String value, int blankNum)
		throws cwException {
		if (value == null) {
			return;
		} else {
			Integer bkNum = new Integer(blankNum);
			if (que_blank_answer.containsKey(bkNum)) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.getBlankAnswerCode(blankNum),
						ResourceUtils.ERROR_INVALID_FORMAT));
			} else {
				if (value.length() <= RES_DEFAULT_TEXT_LENGTH) {
					que_blank_answer.put(bkNum, value);
				} else {
					throw new cwException(
						ResourceUtils.getFailureCode(
							ResourceUtils.getBlankAnswerCode(blankNum),
							ResourceUtils.ERROR_INVALID_LENGTH));
				}
			}
		}
		return;
	}


	/**
	 * Sets blank answer score.
	 * @param value a string 
	 * @param blankNum a blank number
	 * @throws cwException Throwns if blank score already existed or 
	 * not an integer or exceed range (FB_DEFAULT_MIN_SCORE)
	 */
	protected void setBlankScore(String value, int blankNum)
		throws cwException {
		if (value == null) {
			return;
		} else {
			Integer bkNum = new Integer(blankNum);
			if (que_blank_score.containsKey(bkNum)) {
				throw new cwException(
					ResourceUtils.getFailureCode(
						ResourceUtils.getBlankScoreCode(blankNum),
						ResourceUtils.ERROR_INVALID_FORMAT));
			} else {
				Integer score = null; 
				try{
					score = new Integer(Integer.parseInt(value));
				}catch(Exception e){
					throw new cwException(ResourceUtils.getFailureCode(
						ResourceUtils.getBlankScoreCode(blankNum), 
						ResourceUtils.ERROR_INVALID_FORMAT));
				}
				if( score.intValue() <= FB_DEFAULT_MIN_SCORE ) {
					throw new cwException(ResourceUtils.getFailureCode(
						ResourceUtils.getBlankScoreCode(blankNum), 
						ResourceUtils.ERROR_INVALID_FORMAT));
				}
				this.que_blank_score.put(bkNum, score);
			}
		}
		return;
	}

}

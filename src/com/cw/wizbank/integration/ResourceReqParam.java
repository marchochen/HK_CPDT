package com.cw.wizbank.integration;

import javax.servlet.ServletRequest;
import java.sql.Connection;
import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.*;
import com.cw.wizbank.config.WizbiniLoader;

/**
 * 
 * Parse submited parameter(s) used by ResourceModule
 */
public class ResourceReqParam extends ReqParam {

	protected ResourceParser resParser = null;
	protected String type = null;
	protected Connection con = null;
	protected WizbiniLoader wizbini = null;
	
	protected ResourceReqParam(
		Connection con,
		WizbiniLoader wizbini,
		ServletRequest inReq,
		String clientEnc_,
		String encoding_)
		throws cwException {
			
		this.con = con;
		this.wizbini = wizbini;
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		super.common();
		type = getStringParameter("type");

		return;
		
	}

	/**
	 * Sets Resource related parameters.
	 * @param resParser a ResourceParser object
	 * @throws cwException Thrown if invalid format of the submited parameters
	 */
	protected void ResourceParam(ResourceParser resParser) throws cwException {

		resParser.setSiteId(getStringParameter("site_id"));
		resParser.setCatId(
			ResourceUtils.getCatalogIdList(
				con,
				ResourceUtils.getProf(con, resParser.site_id, wizbini),
				unicode(getStringParameter("cat_path")),
				unicode(getStringParameter("cat_path_delimiter")),
				true));
		resParser.setResTitle(unicode(getStringParameter("res_title")));
		resParser.setHtmlInd(getStringParameter("html_ind"));
		resParser.setResDiff(getStringParameter("res_diff"));
		resParser.setResDur(getStringParameter("res_dur"));
		resParser.setResStatus(getStringParameter("res_status"));
		resParser.setResDesc(unicode(getStringParameter("res_desc")));
		return;

	}

	/**
	 * Sets Web Content related parameters.
	 * @throws cwException Thrown if invalid format of the submited parameters
	 */
	protected void WCTParam() throws cwException {

		WCTParser wctParser = new WCTParser();
		wctParser.setResAnnotate(unicode(getStringParameter("res_annotate")));
		wctParser.setResUrl(getStringParameter("res_url"));
		ResourceParam(wctParser);
		resParser = (ResourceParser) wctParser;
		return;

	}
	/**
	 * Sets MC related parameters.
	 * @throws cwException Thrown if invalid format of the submited parameters
	 */
	protected void MCParam() throws cwException {

		MCParser mcParser = new MCParser();
		mcParser.setQueBody(unicode(getStringParameter("que_body")));
		mcParser.setQueOption(unicode(getStringParameter("que_option_01")), 1);
		mcParser.setQueOption(unicode(getStringParameter("que_option_02")), 2);
		mcParser.setQueOption(unicode(getStringParameter("que_option_03")), 3);
		mcParser.setQueOption(unicode(getStringParameter("que_option_04")), 4);
		mcParser.setQueOption(unicode(getStringParameter("que_option_05")), 5);
		mcParser.setQueOption(unicode(getStringParameter("que_option_06")), 6);
		mcParser.setQueOption(unicode(getStringParameter("que_option_07")), 7);
		mcParser.setQueOption(unicode(getStringParameter("que_option_08")), 8);
		mcParser.setQueOption(unicode(getStringParameter("que_option_09")), 9);
		mcParser.setQueOption(unicode(getStringParameter("que_option_10")), 10);
		mcParser.setShuffleInd(getStringParameter("shuffle_ind"));
		mcParser.setAnswer(getStringParameter("que_answer"));
		mcParser.setQueScore(getStringParameter("que_score"));
		mcParser.setQueExp(getStringParameter("que_exp"));
		ResourceParam(mcParser);
		resParser = (ResourceParser) mcParser;
		return;

	}

	/**
	 * Sets Fill Blank related parameters.
	 * @throws cwException Thrown if invalid format of the submited parameters
	 */
	protected void FBParam() throws cwException {
		FBParser fbParser = new FBParser();
		fbParser.setQueBody(unicode(getStringParameter("que_body")));
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_01")), 1);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_02")), 2);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_03")), 3);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_04")), 4);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_05")), 5);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_06")), 6);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_07")), 7);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_08")), 8);
		fbParser.setBlankAnswer(unicode(getStringParameter("que_blank_09")), 9);
		fbParser.setBlankAnswer(
			unicode(getStringParameter("que_blank_10")),
			10);
		fbParser.setBlankScore(getStringParameter("que_blank_01_score"), 1);
		fbParser.setBlankScore(getStringParameter("que_blank_02_score"), 2);
		fbParser.setBlankScore(getStringParameter("que_blank_03_score"), 3);
		fbParser.setBlankScore(getStringParameter("que_blank_04_score"), 4);
		fbParser.setBlankScore(getStringParameter("que_blank_05_score"), 5);
		fbParser.setBlankScore(getStringParameter("que_blank_06_score"), 6);
		fbParser.setBlankScore(getStringParameter("que_blank_07_score"), 7);
		fbParser.setBlankScore(getStringParameter("que_blank_08_score"), 8);
		fbParser.setBlankScore(getStringParameter("que_blank_09_score"), 9);
		fbParser.setBlankScore(getStringParameter("que_blank_10_score"), 10);
		ResourceParam(fbParser);
		resParser = (ResourceParser) fbParser;
		return;
	}

	/**
	 * Sets True-False related parameters.
	 * @throws cwException Thrown if invalid format of the submited parameters
	 */
	protected void TFParam() throws cwException {

		TFParser tfParser = new TFParser();
		tfParser.setQueBody(unicode(getStringParameter("que_body")));
		tfParser.setAnswer(getStringParameter("que_answer"));
		tfParser.setQueScore(getStringParameter("que_score"));
		tfParser.setQueExp(unicode(getStringParameter("que_exp")));
		ResourceParam(tfParser);
		resParser = (ResourceParser) tfParser;
		return;

	}

}

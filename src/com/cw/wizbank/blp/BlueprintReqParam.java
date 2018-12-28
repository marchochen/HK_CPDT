/*
 * Created on 2004-9-23
 *
 */
package com.cw.wizbank.blp;

import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.ServletRequest;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author terry
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class BlueprintReqParam extends ReqParam {
	private static final boolean DEBUG = true;

	public final static String DELIMITER = "~";

	public Timestamp LastModifyStamp;

	public String requestType;
	public String FileName;
	public boolean bFileUploaded;

	loginProfile prof = null;

	/**
	 * Init
	 * 
	 * @param request_:
	 *            the Servlet requst Obj
	 * @param clientEnc_
	 * @param encoding_
	 * @throws cwException
	 */
	BlueprintReqParam(ServletRequest request_, String clientEnc_,
			String encoding_) throws cwException {
		this.req = request_;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;

		if (DEBUG) {
			//Print submited param
			Enumeration enumeration = req.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String name = (String) enumeration.nextElement();
				String[] values = req.getParameterValues(name);
				if (values != null)
					for (int i = 0; i < values.length; i++)
						CommonLog.debug(name + "    (" + i + "):"
								+ values[i]);
			}
		}
	}

	/**
	 * set the login profile
	 */
	public void setProfile(loginProfile profile) {
		this.prof = profile;
	}

	/**
	 * @return Returns the sourceType.
	 */
	public void getRequestType() {
		this.requestType = bMultipart? multi.getParameter("res_format") :req.getParameter("res_format");
	}
	public String getRequestZipIndex(){
	   return bMultipart? multi.getParameter("upload_zipfile_index") :req.getParameter("upload_zipfile_index");
	}
	public String getRequestZipIndex2(){
		   return bMultipart? multi.getParameter("upload_zipfile_index2") :req.getParameter("upload_zipfile_index2");
	}
	
	public String getUpdateTimestamp(){
	    return bMultipart ? multi.getParameter("res_timestamp"): req.getParameter("res_timestamp");
	}
	
	public String getRequestFilename(){
	    return bMultipart? multi.getParameter("file_name") :req.getParameter("file_name");
	}
	public String getRequestURL(){
	    return bMultipart? multi.getParameter("upload_url") :req.getParameter("upload_url");
	}
	public String getRequestSucceedURL(){
		return bMultipart? multi.getParameter("url_succeed") :req.getParameter("url_succeed");
	}
    public void setFileUploaded(boolean bFileUploaded_) {
        this.bFileUploaded = bFileUploaded_;
        return;
    }
}
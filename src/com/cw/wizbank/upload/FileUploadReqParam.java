/*
 * FileUploadReqParam.java
 * 
 * Created on Aug 29, 2011
 */
package com.cw.wizbank.upload;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

/**
 * 
 * @author elvea
 */
public class FileUploadReqParam extends ReqParam {
	public String imgFile;

	public FileUploadReqParam(ServletRequest inReq, String clientEnc_, String encoding_) throws cwException {
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		super.common();
	}

	public void upload() throws cwException {
		imgFile = (bMultipart) ? multi.getFilesystemName("imgFile") : req.getParameter("imgFile");
		return;
	}
}

package com.cw.wizbank.lcms;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

public class LcmsModuleParam extends ReqParam
{
	public long site_id;
	public String usr_ste_usr_id;
	public String usr_pwd;
	public String login_role;
	public String mode;
	public String status;
	
	public long itm_id;
	
	public LcmsModuleParam(ServletRequest inReq, String clientEnc_,
			String encoding_) throws cwException
	{
		this.req = inReq;
		this.clientEnc = clientEnc_;
		this.encoding = encoding_;
		common();
		commonSelf();
		return;
	}
	
	public void commonSelf() throws cwException {
		//this.site_id = getLongParameter("site_id");
		this.site_id = 1;
		this.usr_ste_usr_id = getStringParameter("usr_id");//!!
		this.usr_pwd = getStringParameter("usr_pwd");
	}
	
	public void getLoginInfo() throws cwException {
		login_role = getStringParameter("login_role");
		mode = getStringParameter("mode");
	}
	
	public void getEnrollCourseInfo() throws cwException {
		itm_id = getLongParameter("course_id");//!!
	}
	
	public void getUploadStatusInfo() throws cwException {
		status = getStringParameter("status");
	}
}

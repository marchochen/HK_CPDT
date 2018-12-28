package com.cw.wizbank.login;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class LoginReqParam extends ReqParam{
	
	public long site_id;
	public String usr_ste_usr_id;
	public String usr_pwd;
	public String login_role;
	public String mode;
	public String url_change_pwd;
	public String label_lan;
	public String login_lan;
	public boolean create_new;

	public dbRegUser usr;	

	public LoginReqParam(ServletRequest inReq, String clientEnc_,String encoding_) throws cwException {
	   this.req = inReq;
	   this.clientEnc = clientEnc_;
	   this.encoding = encoding_;
	   common();
	   return;
	 }
	
	public void getLoginInfo() throws cwException {
		site_id = getLongParameter("site_id");
		usr_ste_usr_id = getStringParameter("usr_id");
		usr_pwd = getStringParameter("usr_pwd");
		login_role = getStringParameter("login_role");
		url_change_pwd = getStringParameter("url_change_pwd");
		login_lan = getStringParameter("login_lan");
		if(getStringParameter("label_lan") != null) {
			label_lan = cwUtils.langToLabel(getStringParameter("label_lan"));
		}
		mode = getStringParameter("mode");
	}
	
	public void getAffLoginUsrInfo() throws cwException, UnsupportedEncodingException {
		String val;
		usr = new dbRegUser();
		
		create_new = getBooleanParameter("create_new");
		
		usr.usr_pwd = getStringParameter("usr_pwd");
		
        usr.usr_display_bil = dbUtils.unicodeFrom(getStringParameter("usr_display_bil"), this.clientEnc, this.encoding, false);
        
        usr.usr_gender = getStringParameter("usr_gender");
        
        val = req.getParameter("usr_bday");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_bday =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_bday = null;
                usr.is_valid_usr_bday = false;
            }
        }
        
        usr.usr_email = getStringParameter("usr_email");
        
        usr.usr_tel_1 = getStringParameter("usr_tel_1");
        
        usr.usr_fax_1 = getStringParameter("usr_fax_1");
        
        usr.usr_job_title = dbUtils.unicodeFrom(getStringParameter("usr_job_title"), this.clientEnc, this.encoding, false);
        
        usr.grade_code = getStringParameter("grade_code");
        
        usr.group_code = getStringParameter("group_code");
        
        usr.direct_supervisor_usr_lst = cwUtils.splitToString(getStringParameter("direct_supervisor_usr_lst"), "~");
        
        val = req.getParameter("usr_join_date");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_join_datetime =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_join_datetime = null;
                usr.is_valid_usr_join_datetime = false;
            }
        }

		usr.role_code_lst = cwUtils.splitToString(getStringParameter("usr_role_code_lst"), "~");
		
		usr.supervise_target_group_code_lst = cwUtils.splitToString(getStringParameter("supervise_target_group_code_lst"), "~");
		
		usr.usr_extra_1 = dbUtils.unicodeFrom(getStringParameter("usr_extra_1"), this.clientEnc, this.encoding, false);
		usr.usr_extra_2 = dbUtils.unicodeFrom(getStringParameter("usr_extra_2"), this.clientEnc, this.encoding, false);
        usr.usr_extra_3 = dbUtils.unicodeFrom(getStringParameter("usr_extra_3"), this.clientEnc, this.encoding, false);
        usr.usr_extra_4 = dbUtils.unicodeFrom(getStringParameter("usr_extra_4"), this.clientEnc, this.encoding, false);
        usr.usr_extra_5 = dbUtils.unicodeFrom(getStringParameter("usr_extra_5"), this.clientEnc, this.encoding, false);
        usr.usr_extra_6 = dbUtils.unicodeFrom(getStringParameter("usr_extra_6"), this.clientEnc, this.encoding, false);
        usr.usr_extra_7 = dbUtils.unicodeFrom(getStringParameter("usr_extra_7"), this.clientEnc, this.encoding, false);
        usr.usr_extra_8 = dbUtils.unicodeFrom(getStringParameter("usr_extra_8"), this.clientEnc, this.encoding, false);
        usr.usr_extra_9 = dbUtils.unicodeFrom(getStringParameter("usr_extra_9"), this.clientEnc, this.encoding, false);
        usr.usr_extra_10 = dbUtils.unicodeFrom(getStringParameter("usr_extra_10"), this.clientEnc, this.encoding, false);

        val = req.getParameter("usr_extra_datetime_11");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_11 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_11 = null;
                usr.is_valid_usr_extra_datetime_11 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_12");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_12 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_12 = null;
                usr.is_valid_usr_extra_datetime_12 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_13");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_13 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_13 = null;
                usr.is_valid_usr_extra_datetime_13 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_14");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_14 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_14 = null;
                usr.is_valid_usr_extra_datetime_14 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_15");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_15 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_15 = null;
                usr.is_valid_usr_extra_datetime_15 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_16");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_16 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_16 = null;
                usr.is_valid_usr_extra_datetime_16 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_17");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_17 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_17 = null;
                usr.is_valid_usr_extra_datetime_17 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_18");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_18 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_18 = null;
                usr.is_valid_usr_extra_datetime_18 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_19");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_19 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_19 = null;
                usr.is_valid_usr_extra_datetime_19 = false;
            }
        }
        
        val = req.getParameter("usr_extra_datetime_20");
        if (val != null && val.length() != 0) { 
            try{
                usr.usr_extra_datetime_20 =  Timestamp.valueOf(val); 
            }catch(IllegalArgumentException e){
                usr.usr_extra_datetime_20 = null;
                usr.is_valid_usr_extra_datetime_20 = false;
            }
        }

        usr.usr_extra_singleoption_21 = getStringParameter("usr_extra_singleoption_21");								
        usr.usr_extra_singleoption_22 = getStringParameter("usr_extra_singleoption_22");								
        usr.usr_extra_singleoption_23 = getStringParameter("usr_extra_singleoption_23");								
        usr.usr_extra_singleoption_24 = getStringParameter("usr_extra_singleoption_24");								
        usr.usr_extra_singleoption_25 = getStringParameter("usr_extra_singleoption_25");								
        usr.usr_extra_singleoption_26 = getStringParameter("usr_extra_singleoption_26");								
        usr.usr_extra_singleoption_27 = getStringParameter("usr_extra_singleoption_27");								
        usr.usr_extra_singleoption_28 = getStringParameter("usr_extra_singleoption_28");								
        usr.usr_extra_singleoption_29 = getStringParameter("usr_extra_singleoption_29");								
        usr.usr_extra_singleoption_30 = getStringParameter("usr_extra_singleoption_30");	
        
        usr.usr_extra_multipleoption_31 = getStringParameter("usr_extra_multipleoption_31_lst");									
        usr.usr_extra_multipleoption_32 = getStringParameter("usr_extra_multipleoption_32_lst");									
        usr.usr_extra_multipleoption_33 = getStringParameter("usr_extra_multipleoption_33_lst");									
        usr.usr_extra_multipleoption_34 = getStringParameter("usr_extra_multipleoption_34_lst");									
        usr.usr_extra_multipleoption_35 = getStringParameter("usr_extra_multipleoption_35_lst");									
        usr.usr_extra_multipleoption_36 = getStringParameter("usr_extra_multipleoption_36_lst");									
        usr.usr_extra_multipleoption_37 = getStringParameter("usr_extra_multipleoption_37_lst");									
        usr.usr_extra_multipleoption_38 = getStringParameter("usr_extra_multipleoption_38_lst");									
        usr.usr_extra_multipleoption_39 = getStringParameter("usr_extra_multipleoption_39_lst");									
        usr.usr_extra_multipleoption_40 = getStringParameter("usr_extra_multipleoption_40_lst");
	}


}

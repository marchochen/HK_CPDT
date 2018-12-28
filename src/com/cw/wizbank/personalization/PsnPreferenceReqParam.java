package com.cw.wizbank.personalization;

import javax.servlet.ServletRequest;
import com.cw.wizbank.*;
import com.cw.wizbank.util.cwException;
import com.oreilly.servlet.*;


// public class for processing any parameters at the request URL
// constructor requires a servlet request object
// method "common()" for the set of shared parameters
// add your own method for your own set of parameters
public class PsnPreferenceReqParam extends ReqParam{
    
    public String skin_id;
    public String lang;
    public long major_tc_id;
    
    ServletUtils sutils = new ServletUtils();

    public PsnPreferenceReqParam (ServletRequest inReq, String clientEnc_, String encoding_) throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_; 
        this.encoding = encoding_;

    }
    
    //parameters needed in message
    public void preference() throws cwException {
        
        String var;
        
        skin_id = getStringParameter("skin_id");
        lang = getStringParameter("lang");
        major_tc_id = getLongParameter("major_tc_id");
    }

   
}
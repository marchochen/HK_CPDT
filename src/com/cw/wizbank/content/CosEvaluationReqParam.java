package com.cw.wizbank.content;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

public class CosEvaluationReqParam extends ReqParam {
    public long mod_id;
    public long mod_tcr_id;
    public CosEvaluationReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;  
            super.common();            
        
    }
    
    public void mod() throws cwException {
    	String paraname = "mod_tcr_id";
    	String var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
    	if (var != null && var.length() > 0)
    		this.mod_tcr_id = Long.valueOf(var).longValue();
    }
    
}
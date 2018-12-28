package com.cw.wizbank.content;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;

public class EvaluationReqParam extends ReqParam {
    public long mod_id;
    boolean show_effective_ind = false;
    public long tcr_id;
    
    public EvaluationReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;  
            super.common();            
        
    }
    
    public void report() throws cwException{
        mod_id = getLongParameter("mod_id");
    }
    
    public void get_public_eval_lst() throws cwException{
        show_effective_ind = getBooleanParameter("filter");
        tcr_id = getLongParameter("tcr_id");
    }

}
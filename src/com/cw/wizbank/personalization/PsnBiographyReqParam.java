package com.cw.wizbank.personalization;

import javax.servlet.ServletRequest;
import com.cw.wizbank.*;
import com.cw.wizbank.util.cwException;
import com.oreilly.servlet.*;

public class PsnBiographyReqParam extends ReqParam{
    public long usr_ent_id;
    public String option_lst;
    public String self_desc;
    public String sql;
    
    ServletUtils sutils = new ServletUtils();

    public PsnBiographyReqParam (ServletRequest inReq, String clientEnc_, String encoding_) throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_; 
        this.encoding = encoding_;
    }
    
    public void sql() throws cwException {
        sql = getStringParameter("sql");
    }
    //parameters needed in message
    public void biography() throws cwException {
        
        String var;
        
        option_lst = getStringParameter("option_lst");
        usr_ent_id = getLongParameter("usr_ent_id");
        self_desc = unicode(getStringParameter("self_desc"));
    }

   
}
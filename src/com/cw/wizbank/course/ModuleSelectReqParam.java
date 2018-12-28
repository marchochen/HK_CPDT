package com.cw.wizbank.course;

import javax.servlet.ServletRequest;

import com.cw.wizbank.ReqParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class ModuleSelectReqParam extends ReqParam{
    
    // for Rrerequisite ModueParam
    //public int itm_id;
   //public List mod_id_list=new ArrayList();
    //public List pre_module_id_list=new ArrayList();
   // public List pre_module_status_list=new ArrayList();
    
    public String search_type;
    public String title_code;
    public String dis_cos_type;
//  for module select 
    public String is_multiple;
    public String dis_mod_type;
    public String fieldname;
    public String sel_type;
    public String sel_mod_status;
    public String sel_win_title;
    public String width;
    public long itm_id;
    public long itm_tcr_id;
    public long course_id;
    
        
    public ModuleSelectReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
    throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_;
        this.encoding = encoding_;  
        super.common();
    }

    public void selModule() throws cwException{

        String var;
        var = req.getParameter("search_type");
        if( var != null && var.length() > 0) {
            search_type = var;
        }
        
        var = req.getParameter("title_code");
        if( var != null && var.length() > 0) {
            title_code = cwUtils.unicodeFrom(var,  this.clientEnc, this.encoding);
        }
        
        var = req.getParameter("dis_cos_type");
        if( var != null && var.length() > 0) {
            dis_cos_type = var;
        }
        
        var = req.getParameter("is_multiple");
        if ( var!= null && var.length()!= 0 ) {
            is_multiple = var;
        }
            
        var = req.getParameter("dis_mod_type");
        if ( var!= null && var.length()!= 0 ) {
            dis_mod_type = var; 
        }
        
        var = req.getParameter("field_name");
        if ( var!= null && var.length()!= 0 ) {
            fieldname = var;
        }
            
        var = req.getParameter("sel_type");
        if ( var!= null && var.length()!= 0 ) {
            sel_type = var;
        }
        
        var = req.getParameter("sel_mod_status");
        if ( var!= null && var.length()!= 0 ) {
            sel_mod_status = var;
        }
            
        var = req.getParameter("title");
        if ( var!= null && var.length()!= 0 ) {
            sel_win_title = var;
        }
           
        var = req.getParameter("itm_id");
        if (var != null && var.length() != 0) {
           itm_id = Long.parseLong(var);
        }
        
        var = req.getParameter("width");
        if ( var!= null && var.length()!= 0 ) {
            width = var;
        }
        var = req.getParameter("course_id");
        if (var != null && var.length() != 0) {
        	course_id = Long.parseLong(var);
        }
        var = req.getParameter("itm_tcr_id");
        if ( var!= null && var.length()!= 0 ) {
            itm_tcr_id = Long.parseLong(var);
        }
     }
    
    
}


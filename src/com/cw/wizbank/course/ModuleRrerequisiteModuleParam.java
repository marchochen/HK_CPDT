package com.cw.wizbank.course;

import java.util.*;

import javax.servlet.ServletRequest;

import com.cw.wizbank.*;
import com.cw.wizbank.util.cwException;
public class ModuleRrerequisiteModuleParam extends ReqParam{
	
	// for Rrerequisite ModueParam
    public int itm_id;
    public List mod_id_list=new ArrayList();
    public List pre_module_id_list=new ArrayList();
    public List pre_module_status_list=new ArrayList();
    
	    
	public ModuleRrerequisiteModuleParam(ServletRequest inReq, String clientEnc_, String encoding_)
    throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_;
        this.encoding = encoding_;  
        super.common();
}

	public void preModule() {
		String var;
		var = req.getParameter("itm_id");
		if( var != null && var.length() > 0){
			try{
				itm_id = Integer.parseInt(var);
			}catch( NumberFormatException e ) {
				itm_id = 0 ;
			}
		}
	    String[] item_mod_list= req.getParameterValues("mod_id");
	    if(item_mod_list!=null){
	    	for(int i=0;i<item_mod_list.length;i++){
	    		String mod_id=item_mod_list[i];
	    		String pre_mod_id = req.getParameter("mod_pre_"+mod_id+"_id");
	    		String check_status=req.getParameter("mod_pre_"+mod_id+"_status");
	    		 mod_id_list.add(Long.valueOf(mod_id.trim()));
	    		if(pre_mod_id!=null&&pre_mod_id.length()>0&&check_status!=null&&check_status.length()>0){
	    		     pre_module_id_list.add(new Long(pre_mod_id.trim()));
	    		     pre_module_status_list.add(check_status.trim());
	    		}else{
	    			 pre_module_id_list.add(null);
	    		     pre_module_status_list.add(null);
	    		}
	    	}
	    }
     }
}

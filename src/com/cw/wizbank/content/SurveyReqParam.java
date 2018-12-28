package com.cw.wizbank.content;

import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;

import java.sql.Timestamp;
import com.cw.wizbank.*;
import com.cw.wizbank.util.*;

import com.cw.wizbank.ae.aeItem;



public class SurveyReqParam extends ReqParam {
    public Survey svy;
    public aeItem itm;
    public long itm_id;
    public long[] run_id_lst;
    public String dpo_view;
    public String ent_ids;
    public String cc_ent_ids;
    public int curPage;
    public String usr_order;
    public String mov_status;
    public Timestamp startDate;
    public Timestamp endDate;
    public long[] ent_id_lst;
//    public float rate;
    
    public SurveyReqParam(ServletRequest inReq, String clientEnc_, String encoding_)
        throws cwException {
            this.req = inReq;
            this.clientEnc = clientEnc_;
            this.encoding = encoding_;  
            super.common();            
        
            String  var = req.getParameter("page");
            if (var!= null && var.length()!= 0 ){
                curPage = Integer.parseInt(var);
            }
    }

    public void module() throws cwException{
        svy = new Survey();
        String var;
        var = req.getParameter("res_id");
        if (var!= null && var.length()!= 0 ){
            svy.res_id = Long.parseLong(var);    
            svy.mod_res_id = Long.parseLong(var); 
        }

        var = req.getParameter("mod_id");
        if (var!= null && var.length()!= 0 ){
            svy.res_id = Long.parseLong(var);    
            svy.mod_res_id = Long.parseLong(var); 
        }
        
        var = req.getParameter("dpo_view");
        if ( var!= null && var.length()!= 0 ) 
            dpo_view = var;
            
        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try {
                itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm_id = 0;
            }
        } else {
            itm_id = 0;
        }        

        var = req.getParameter("res_title_order");
        if ( var!= null && var.length()!= 0 ){
            OrderObj myOrderObj = new OrderObj();
            myOrderObj.colName = "res_title";
            myOrderObj.order = var;
            svy.vtOrder.addElement(myOrderObj);
        } 

        var = req.getParameter("usr_order");
        if ( var!= null && var.length()!= 0 ){
            OrderObj myOrderObj = new OrderObj();
            myOrderObj.colName = "usr_display_bil";
            myOrderObj.order = var;
            svy.vtOrder.addElement(myOrderObj);
            
            usr_order = var;
        } 

        var = req.getParameter("start_order");
        if ( var!= null && var.length()!= 0 ){
            OrderObj myOrderObj = new OrderObj();
            myOrderObj.colName = "mod_eff_start_datetime";
            myOrderObj.order = var;
            svy.vtOrder.addElement(myOrderObj);
        } 

        var = req.getParameter("end_order");
        if ( var!= null && var.length()!= 0 ){
            OrderObj myOrderObj = new OrderObj();
            myOrderObj.colName = "mod_eff_end_datetime";
            myOrderObj.order = var;
            svy.vtOrder.addElement(myOrderObj);
        } 
        
        var = req.getParameter("mov_status");
        if( var != null && var.length() > 0 )
            mov_status = var;
  
        var = req.getParameter("ent_id_lst");
        if( var != null && var.length() > 0 )
            ent_id_lst = cwUtils.splitToLong(var, "~");

        var = req.getParameter("run_id_lst");
        if( var != null && var.length() > 0 )
            run_id_lst = cwUtils.splitToLong(var, "~");

        startDate = getTimestampParameter("start_datetime");
        endDate = getTimestampParameter("end_datetime");

/*
        var = req.getParameter("rate");
        if ( var!= null && var.length()!= 0 ){
            try{
                rate = Float.valueOf(var).floatValue();
            } catch (NumberFormatException e) {
                rate = 0;
            }
        }else{
            rate = 0;
        }
  */          
    }
    
    String url_redirect_param;
    public void messaging()
        throws cwException {
        String var;
        
        var = req.getParameter("ent_ids");
        if ( var!= null && var.length()!= 0 ) 
            ent_ids = var;
            
        var = req.getParameter("cc_ent_ids");
        if ( var!= null && var.length()!= 0 ) 
            cc_ent_ids = var;
            
        var = req.getParameter("url_redirect_param");
        if( var != null && var.length()!= 0 )
            url_redirect_param = unicode(var);
            
    }

    public void item() throws UnsupportedEncodingException {
        itm = new aeItem();
        String var;
        
        var = req.getParameter("itm_id");
        if (var != null && var.length() > 0) {
            try {
                itm.itm_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm.itm_id = 0;
            }
        } else {
            itm.itm_id = 0;
        }        


        //item tree node parent id
        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            try {
                itm.tnd_id = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                itm.tnd_id = 0;
            }
        } else {
            itm.tnd_id = 0;
        }
    }
    
    boolean refresh;
    long tnd_id;
    long mod_id;
    public void ins_notify() 
        throws cwException {
        
        refresh     =   getBooleanParameter("refresh");

        String var = null;                    
        var = req.getParameter("tnd_id");
        if (var != null && var.length() > 0) {
            try {
                tnd_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                tnd_id = 0;
            }
        } else {
            tnd_id = 0;
        }
        
        var = req.getParameter("mod_id");
        if (var != null && var.length() > 0) {
            try {
                mod_id = Long.parseLong(var);
            } catch (NumberFormatException e) {
                mod_id = 0;
            }
        } else {
            mod_id = 0;
        }
        

    }
    
    
    String msg_subject;
    String msg_body;
    String msg_method;
    Timestamp msg_send_time;
    String type;
    int cur_page;
    int pagesize;
    long ent_id;
    Timestamp timestamp;
    public void pick_ent_lst()
        throws cwException {
        /*    
            msg_subject     =   getStringParameter("subject");
            
            msg_body        =   getStringParameter("content");
            
            msg_method      =   getStringParameter("method");
            
            msg_send_time   =   getTimestampParameter("timestamp");
        */    
            type            =   getStringParameter("type");
            
            cur_page        =   getIntParameter("cur_page");
            
            pagesize        =   getIntParameter("pagesize");
            
            timestamp       =   getTimestampParameter("timestamp");
            
            String var = null;
            var = req.getParameter("ent_id");
            if (var != null && var.length() > 0) {
                try {
                    ent_id = Long.parseLong(var);
                } catch (NumberFormatException e) {
                    ent_id = 0;
                }
            } else {
                ent_id = 0;
            }
            
        }
        
    String dd,mm,yy;
    public void pick_ent()
        throws cwException {
            msg_subject     =   unicode(getStringParameter("subject"));
            
            msg_body        =   unicode(getStringParameter("content"));
            
            msg_method      =   getStringParameter("subtype");
            
            //msg_send_time   =   getTimestampParameter("timestamp");
            dd              =   unicode(getStringParameter("dd"));
            yy              =   unicode(getStringParameter("yy"));
            mm              =   unicode(getStringParameter("mm"));
                        
            ent_ids         =   getStringParameter("ent_ids");            
            
            type            =   getStringParameter("type");
        }
        
    //{{DECLARE_CONTROLS
    //}}
}
package com.cw.wizbank.netg;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import com.cw.wizbank.util.*;
import com.oreilly.servlet.*;

// public class for processing any parameters at the request URL
// constructor requires a servlet request object
// method "common()" for the set of shared parameters
// add your own method for your own set of parameters
public class NETgTrackingReqParam {
    private ServletRequest req;
    private String clientEnc;
    private String encoding;
    private MultipartRequest multi;
    private boolean bMultiPart = false;
    
    public String cmd;
    public String stylesheet;
    public String url_success;
    public String url_failure;
    public String usr_id;
    public String home;
    
    public int cos_id;
    public int mod_id;
    public long tkh_id;
    public long usr_ent_id;
    
    public String cos_url;
    
    public NETgTrackingReqParam(boolean bMultiPart_, ServletRequest inReq, MultipartRequest multi_, String clientEnc_, String encoding_) throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_; 
        this.encoding = encoding_;
        this.bMultiPart = bMultiPart_;
        this.multi = multi_;
        
        common();
    }
    
    // common parameters needed in all commands
    public void common() throws cwException {            
        String var;
        
        // command
        var = (bMultiPart) ? multi.getParameter("cmd") : req.getParameter("cmd");
        if (var != null && var.length() > 0)
            cmd = var;
        else
            cmd = null;
        // stylesheet filename
        var = (bMultiPart) ? multi.getParameter("stylesheet") : req.getParameter("stylesheet");
        if (var != null && var.length() > 0)
            stylesheet = var;
        else
            stylesheet = null;
        // url success
        url_success = cwUtils.getRealPath((HttpServletRequest) req, req.getParameter("url_success"));
        // url failure
        url_failure = cwUtils.getRealPath((HttpServletRequest) req, req.getParameter("url_failure"));
    }

    //parameters needed in message
    public void netg_tracking_info() throws UnsupportedEncodingException {
        String var;
        
        // cos id
        var = (bMultiPart) ? multi.getParameter("cos_id") : req.getParameter("cos_id");
        if (var != null && var.length() > 0) {
            cos_id = Integer.parseInt(var);
        }
        else {
            cos_id = -1;
        }
        // mod id
        var = (bMultiPart) ? multi.getParameter("mod_id") : req.getParameter("mod_id");
        if (var != null && var.length() > 0) {
            mod_id = Integer.parseInt(var);
        }
        else {
            mod_id = -1;
        }
        // course url
        var = (bMultiPart) ? multi.getParameter("cos_url") : req.getParameter("cos_url");
        if (var != null && var.length() > 0) {
        	try{
            	cos_url = cwUtils.getRealPath((HttpServletRequest)req, var);
        	}catch(cwException e) {
        		cos_url = null;
        	}
        }
        else {
            cos_url = null;
        }
        // home url
        var = (bMultiPart) ? multi.getParameter("home") : req.getParameter("home");
        if (var != null && var.length() > 0) {
            home = var;
        }
        else {
            home = null;
        }
        // tracking id
        var = (bMultiPart) ? multi.getParameter("tkh_id") : req.getParameter("tkh_id");
        if (var != null && var.length() > 0) {
            tkh_id = Long.parseLong(var);
        }
        else {
            tkh_id = -1;
        }  
        // usr_ent_id
        var = (bMultiPart) ? multi.getParameter("usr_id") : req.getParameter("usr_id");
        if (var != null && var.length() > 0) {
            usr_ent_id = Long.parseLong(var);
        }
        else {
            usr_ent_id = -1;
        }  
    }
    
}
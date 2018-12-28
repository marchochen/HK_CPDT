package com.cw.wizbank.content;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

import com.cw.wizbank.db.*;

// public class for processing any parameters at the request URL
// constructor requires a servlet request object
// method "common()" for the set of shared parameters
// add your own method for your own set of parameters
public class ReferenceReqParam {
    private ServletRequest req;
    private String clientEnc;
    private String encoding;
    
    public String cmd;
    public String stylesheet;
    public String url_success;
    public String url_failure;
    public String usr_id;

    // reference attribute
    public DbCtReference myDbReference;
    public dbModule myDbModule;
    public String ref_id_list;
    
    public ReferenceReqParam(ServletRequest inReq, String clientEnc_, String encoding_) throws cwException {
        this.req = inReq;
        this.clientEnc = clientEnc_; 
        this.encoding = encoding_;
        
        common();
    }
    
    // common parameters needed in all commands
    public void common() throws cwException {            
        String var;
        
        // command
        var = req.getParameter("cmd");
        if (var != null && var.length() > 0)
            cmd = var;
        else
            cmd = null;
        // stylesheet filename
        var = req.getParameter("stylesheet");
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
    public void reference_info() throws UnsupportedEncodingException {
        
        myDbReference = new DbCtReference();
        myDbModule = new dbModule();

        // handle both content types
        boolean bMultiPart = false;
        String conType = req.getContentType();

        if( conType != null && conType.toLowerCase().startsWith("multipart/form-data") ) {
            bMultiPart = true;
        }
        
        String var;

        var = req.getParameter("ref_id");
        if (var != null && var.length() > 0) {
            myDbReference.ref_id = Integer.parseInt(var);
        }
        else {
            myDbReference.ref_id = 0;
        }

        var = req.getParameter("ref_id_list");
        if (var != null && var.length() > 0) {
            ref_id_list = var;
        }
        else {
            ref_id_list = null;
        }

        var = req.getParameter("mod_id");
        if (var != null && var.length() > 0) {
            myDbReference.ref_res_id = Integer.parseInt(var);
            myDbModule.res_id = Long.parseLong(var); 
            myDbModule.mod_res_id = Long.parseLong(var);
        }
        else {
            myDbReference.ref_res_id = 0;
            myDbModule.res_id = 0; 
            myDbModule.mod_res_id = 0;
        }
        
        var = req.getParameter("tkh_id");
        if (var != null && var.length() > 0) {
        	myDbModule.tkh_id = Long.parseLong(var);
        }
   
        var = req.getParameter("ref_type");
        if (var != null && var.length() > 0) {
            myDbReference.ref_type = var;
        }
        else {
            myDbReference.ref_type = null;
        }
        
        var = req.getParameter("ref_title");
        if (var != null && var.length() > 0) {
            myDbReference.ref_title = dbUtils.unicodeFrom(var, clientEnc, encoding, bMultiPart);
        }
        else {
            myDbReference.ref_title = null;
        }
        
        var = req.getParameter("ref_description");
        if (var != null && var.length() > 0) {
            myDbReference.ref_description = dbUtils.unicodeFrom(var, clientEnc, encoding, bMultiPart);
        }
        else {
            myDbReference.ref_description = null;
        }
        
        var = req.getParameter("ref_url");
        if (var != null && var.length() > 0) {
            myDbReference.ref_url = dbUtils.unicodeFrom(var, clientEnc, encoding, bMultiPart);
        }
        else {
            myDbReference.ref_url = null;
        }
    }
    
}
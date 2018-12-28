package com.cw.wizbank.importcos;

import javax.servlet.ServletRequest;

import java.io.UnsupportedEncodingException;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.*;
import com.oreilly.servlet.*;

// public class for processing any parameters at the request URL
// constructor requires a servlet request object
// method "common()" for the set of shared parameters
// add your own method for your own set of parameters
public class ImportCosReqParam {
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
    
    public int cos_id;
    public String csfFileName;
    public String cdfFileName;
    public String imsmanifestFileName;
    public String cosUrlPrefix;
    
    public long src_res_id;
    public boolean is_inner;

    // reference attribute
    public DbCtReference myDbReference;
    public dbModule myDbModule;
    public String ref_id_list;
    
    public String sco_ver;
    
    int mod_mobile_ind;
    String mod_test_style;
    
    public ImportCosReqParam(boolean bMultiPart_, ServletRequest inReq, MultipartRequest multi_, String clientEnc_, String encoding_) {
        this.req = inReq;
        this.clientEnc = clientEnc_; 
        this.encoding = encoding_;
        this.bMultiPart = bMultiPart_;
        this.multi = multi_;
        
        common();
    }
    
    // common parameters needed in all commands
    public void common() {            
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
        var = (bMultiPart) ? multi.getParameter("url_success") : req.getParameter("url_success");
        if (var != null && var.length() > 0)
            url_success = var;
        else 
            url_success = null;
        // url failure
        var = (bMultiPart) ? multi.getParameter("url_failure") : req.getParameter("url_failure");
        if (var != null && var.length() > 0)
            url_failure = var;
        else
            url_failure = null;
    }

    //parameters needed in message
    public void import_cos_info() throws UnsupportedEncodingException {
        String var;
        
        // cos id
        var = (bMultiPart) ? multi.getParameter("cos_id") : req.getParameter("cos_id");
        if (var != null && var.length() > 0) {
            cos_id = Integer.parseInt(var);
        }
        else {
            cos_id = -1;
        }
        var = (bMultiPart) ? multi.getParameter("cos_url_prefix") : req.getParameter("cos_url_prefix");
        if (var != null && var.length() > 0) {
            cosUrlPrefix = var;
        }
        else {
            cosUrlPrefix = null;
        }        
        // CDF file name for NETg cookie interface
        var = (bMultiPart) ? multi.getParameter("cdf_file_name") : req.getParameter("cdf_file_name");
        if (var != null && var.length() > 0) {
            cdfFileName = var;
        }
        else {
            cdfFileName = null;
        }
        // CSF file name for SCORM 1.1
        var = (bMultiPart) ? multi.getParameter("csf_file_name") : req.getParameter("csf_file_name");
        if (var != null && var.length() > 0) {
            csfFileName = var;
        }
        else {
            csfFileName = null;
        }
        // Imsmanifest file name for SCORM 1.2
        var = (bMultiPart) ? multi.getParameter("imsmanifest_file_name") : req.getParameter("imsmanifest_file_name");
        if (var != null && var.length() > 0) {
            imsmanifestFileName = var;
        }
        else {
            imsmanifestFileName = null;
        }

        var = (bMultiPart) ? multi.getParameter("src_res_id") : req.getParameter("src_res_id");
        if (var != null && var.length() > 0) {
            src_res_id = Long.parseLong(var);
        }
        else {
            src_res_id = 0;
        }

        var = (bMultiPart) ? multi.getParameter("is_inner") : req.getParameter("is_inner");
        if (var != null && var.length() > 0) {
        	is_inner = Boolean.valueOf(var.trim()).booleanValue();
        }
        else {
        	is_inner = false;
        }
        
        var = (bMultiPart) ? multi.getParameter("sco_ver") : req.getParameter("sco_ver");
        if (var != null && var.length() > 0) {
        	sco_ver = var;
        } else {
        	sco_ver = "1.2";
        }
        
        var = (bMultiPart) ? multi.getParameter("mod_mobile_ind") : req.getParameter("mod_mobile_ind");
        if (var != null && var.length() > 0 && var.trim().equals("1")) {
        	mod_mobile_ind = Integer.valueOf(var.trim()).intValue();
        } else {
        	mod_mobile_ind = 0;
        }
        
        var = (bMultiPart) ? multi.getParameter("mod_test_style") : req.getParameter("mod_test_style");
        if (var != null && var.length() > 0) {
        	mod_test_style = var;
        } else {
        	mod_test_style = "only";
        }

    }
    
}
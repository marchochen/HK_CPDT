package com.cw.wizbank.personalization;

import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.db.DbBookmark;
import com.oreilly.servlet.*;


// public class for processing any parameters at the request URL
// constructor requires a servlet request object
// method "common()" for the set of shared parameters
// add your own method for your own set of parameters
public class BookmarkReqParam {
    private ServletRequest req;
    private String clientEnc;
    private String encoding;
    
    public String cmd;
    public String stylesheet;
    public String url_success;
    public String url_failure;
    public DbBookmark dbBookmark;
    public String[] booLst;
    
    ServletUtils sutils = new ServletUtils();

    
    public BookmarkReqParam(ServletRequest inReq, String clientEnc_, String encoding_) {
        this.req = inReq;
        this.clientEnc = clientEnc_; 
        this.encoding = encoding_;
        
        common();
    }
    
    // common parameters needed in all commands
    public void common() {            
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
        var = req.getParameter("url_success");
        if (var != null && var.length() > 0)
            url_success = var;
        else 
            url_success = null;
        // url failure
        var = req.getParameter("url_failure");
        if (var != null && var.length() > 0)
            url_failure = var;
        else
            url_failure = null;
    }

    //parameters needed in message
    public void insBoo() throws UnsupportedEncodingException{
        
        dbBookmark = new DbBookmark();
        
        String var;
        
        var = req.getParameter("boo_title");
        if (var != null && var.length() > 0)
            dbBookmark.boo_title = dbUtils.unicodeFrom(var, clientEnc, encoding, false);
        
        var = req.getParameter("boo_url");
        if (var != null && var.length() > 0)
            dbBookmark.boo_url = dbUtils.unicodeFrom(var, clientEnc, encoding, false);
                
        var = req.getParameter("boo_res_id");
        if (var != null && var.length() > 0)
            dbBookmark.boo_res_id = Integer.parseInt(var);
/*
        var = req.getParameters("boo_id");
        if (var != null && var.length() > 0)
            dbboo.boo_res_id = Integer.parseInt(var);
*/
/*        val = (bMultiPart) ? multi.getParameter(prmNm) : request.getParameter(prmNm);
        if ( val!= null && val.length()!= 0 )
            booLst = sutils.split(val, "~");   
            */

    }

    public void delBoo() {
        
        dbBookmark = new DbBookmark();
        String var;
        var = req.getParameter("boo_lst");
        if (var!= null && var.length()!= 0 )
            booLst = sutils.split(var, "~");   

    }

    public void getAll() {
        dbBookmark = new DbBookmark();
    }
    
}
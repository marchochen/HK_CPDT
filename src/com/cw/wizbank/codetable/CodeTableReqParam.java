package com.cw.wizbank.codetable;


import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import com.cw.wizbank.*;
import com.cw.wizbank.util.*;

public class CodeTableReqParam extends ReqParam {
	String ctb_xml;
	Timestamp upd_timestamp;
	String ctb_type_bef;
	String ctb_id_bef;
	String ctb_title;
	String ctb_id;
	String ctb_type;
    boolean exact;
    String[] ctb_id_lst;
    Timestamp[] ctb_upd_timestamp_lst;
    Timestamp ctb_in_upd_timestamp;
    String orderBy;
    String sortOrder;
    Timestamp search_timestamp;
    int page;

    public CodeTableReqParam(ServletRequest inReq, String clientEnc_, String encoding_) 
        throws cwException {        
            this.req = inReq;
            this.clientEnc = clientEnc_; 
            this.encoding = encoding_;        
            common();
            return;
    }    

    public void myCommon() throws cwException {
        
        //this.entityType = getStringParameter("ent_type");
    }
    
    //parameters needed in code table related commands
    public void codeTable(String clientEnc, String env_encoding) throws 
        UnsupportedEncodingException, cwException {
        String var;

        //for code table search
        var = req.getParameter("exact");
        if (var != null && var.length() > 0) {
            exact = Boolean.valueOf(var).booleanValue();
        } else {
            exact = false;
        }       
        
        //code table type
        var = req.getParameter("ctb_type");
        if (var != null) 
            ctb_type = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
		var = req.getParameter("ctb_id_bef");
		if (var != null) 
			ctb_id_bef = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);
		var = req.getParameter("ctb_type_bef");
		if (var != null) 
			ctb_type_bef = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);				
        //code table id
        var = req.getParameter("ctb_id");
        if (var != null) 
            ctb_id = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table id list
        ctb_id_lst = getStrArrayParameter("ctb_id_lst", "~");
        /*
        var = req.getParameter("ctb_id_lst");
        System.out.println("ctb_id_lst = " + var);
        if ( var!= null && var.length()!= 0 ) {
            ctb_id_lst = getStrArrayParameter(var, "~");
            System.out.println("ctb_id_lst = " + ctb_id_lst);
            if(ctb_id_lst != null) {
                for (int i=0; i<ctb_id_lst.length; i++) {
                    System.out.println("ctb_id_lst["+i+"]=" + ctb_id_lst[i]);
                }
            }
        }
        else
            ctb_id_lst = new String[0];
        */
  
        //code table title
        var = req.getParameter("ctb_title");
        if (var != null) 
            ctb_title = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table title
        var = req.getParameter("ctb_xml");
        if (var != null && var.length() > 0) 
            ctb_xml = cwUtils.unicodeFrom(var, clientEnc, env_encoding, false);

        //code table upd timestamp
        var = req.getParameter("ctb_upd_timestamp");
        if (var != null && var.length() > 0) {
            ctb_in_upd_timestamp = Timestamp.valueOf(var);
        }
        else
            ctb_in_upd_timestamp = null;
		var = req.getParameter("upd_timestamp");
		if (var != null && var.length() > 0) {
			upd_timestamp = Timestamp.valueOf(var);
		}
		else
			ctb_in_upd_timestamp = null;
        //code table upd timestamp list
        ctb_upd_timestamp_lst = getTimestampArrayParameter("ctb_upd_timestamp_lst", "~");
        /*
        var = req.getParameter("ctb_upd_timestamp_lst");
        if ( var!= null && var.length()!= 0 ) 
            ctb_upd_timestamp_lst = getTimestampArrayParameter(var, "~");
        else
            ctb_upd_timestamp_lst = new Timestamp[0];
        */
        
        //code table look up order
        var = req.getParameter("orderby");
        if (var != null && var.length() > 0) 
            orderBy = var;

        //code table look up sort order
        var = req.getParameter("sortorder");
        if (var != null && var.length() > 0) 
            sortOrder = var;

        var = req.getParameter("search_timestamp");
        if (var != null && var.length() > 0)
            search_timestamp = Timestamp.valueOf(var);
        else
            search_timestamp = null;

        var = req.getParameter("page");
        if (var != null && var.length() > 0) {
            try {
                page = Integer.parseInt(var);
            } catch (NumberFormatException e) {
                page = 0;
            }
        } else {
            page = 0;
        }
    }
    

    // convert a String[] to Timestamp[]
    protected Timestamp[] getTimestampArrayParameter(String paraname, String separator) 
        throws cwException {

        try {
            String[] s = getStrArrayParameter(paraname, separator);
            if(s != null) {
                Timestamp[] t = new Timestamp[s.length];
                
                for(int i=0; i<s.length; i++) {
                    t[i] = Timestamp.valueOf(s[i]);
                }
                return t;
            }
            else
                return null;
        }
        catch(IllegalArgumentException e) {
            throw new cwException("EntityCodeReqParam.getLongArrayParameter: Cannot convert string to number");
        }
    }

    
    // convert a String[] to long[]
    protected long[] getLongArrayParameter(String paraname, String separator) 
        throws cwException {

        try {
            String[] s = getStrArrayParameter(paraname, separator);
            if(s != null) {
                long[] l = new long[s.length];
                
                for(int i=0; i<s.length; i++) {
                    l[i] = Long.parseLong(s[i]);
                }
                return l;
            }
            else
                return null;
        }
        catch(NumberFormatException e) {
            throw new cwException("EntityCodeReqParam.getLongArrayParameter: Cannot convert string to number");
        }
    }
}
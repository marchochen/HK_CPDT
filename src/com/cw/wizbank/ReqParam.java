package com.cw.wizbank;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
// Utils classes
import com.oreilly.servlet.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwPagination;

/**
An abstract class to parse URL parameters. <BR>
It will automtaically detect whether the request is mutlipart or not. <BR>
*/

public abstract class ReqParam{

    /**
    request paramter : cmd
    */
    public String cmd;
    /**
    request paramter : stylesheet
    */
    public String stylesheet;
    /**
    request paramter : url_success
    */
    public String url_success;
    /**
    request paramter : url_failure
    */
    public String url_failure;
    /**
    constant : integer type of parameter not found 
    */
    public static final int INT_PARAMETER_NOT_FOUND     = Integer.MIN_VALUE;
    /**
    constant : long type of parameter not found 
    */
    public static final long LONG_PARAMETER_NOT_FOUND    = Long.MIN_VALUE;
    /**
    constant : long type of parameter not found 
    */
    public static final float FLOAT_PARAMETER_NOT_FOUND    = Float.MIN_VALUE;
    
    public static final double DOUBLE_PARAMETER_NOT_FOUND    = Double.MIN_VALUE;
    /**
    ServeltRequest 
    */
    protected ServletRequest req;
    /**
    encoding of the client : request.getCharacterEncoding()
    */
    protected String clientEnc;
    /**
    encoding of the web site
    */
    protected String encoding;
    /**
    whether the request is multipart
    */
    protected boolean bMultipart = false;

    /**
    object using com.oreilly.servlet.* package to handle multipart request
    */
    protected MultipartRequest multi;

    /**
    object contain the parameter for pagination
    */
    public cwPagination cwPage;
    public int courseID;

    /**
    initiate the multipart variables
    @param multi_ MultipartRequest object
    */
    public void setMultiPart(MultipartRequest multi_)
        throws cwException {
            this.bMultipart = true;
            this.multi = multi_;
    }
    
    /**
    get the common parameters which supposed to be exist in all request
    */
    public void common() throws cwException {

        // command
        cmd = getStringParameter("cmd");
        courseID = getIntParameter("courseID");
        
        // stylesheet
        String style_sheet = getStringParameter("stylesheet");
        if(style_sheet != null &&(style_sheet.indexOf("WEB-INF/") < 0)){
        	stylesheet = style_sheet;
        }
        // url success
        url_success = cwUtils.getRealPath((HttpServletRequest) req, getStringParameter("url_success"));

        // url failure
        url_failure = cwUtils.getRealPath((HttpServletRequest) req, getStringParameter("url_failure"));

    }
    /**
    get the common parameters which may need in pagination
    */
    public void pagination(){
        cwPage = new cwPagination();
        
        String var;
        
        var = req.getParameter("cur_page");
        if (var != null && var.length() > 0) {
            cwPage.curPage = Integer.parseInt(var);
        }
        else
            cwPage.curPage = 0;

        var = req.getParameter("page_size");
        if (var != null && var.length() > 0) {
            cwPage.pageSize = Integer.parseInt(var);
        }
        else
            cwPage.pageSize = 0;

        var = req.getParameter("sort_col");
        if (var != null && var.length() > 0) {
            cwPage.sortCol = cwPagination.esc4SortSql(var);
        }

        var = req.getParameter("sort_order");
        if (var != null && var.length() > 0) {
            cwPage.sortOrder = cwPagination.esc4SortSql(var);
        }

        var = req.getParameter("timestamp");
        if (var != null && var.length() > 0) {
            cwPage.ts = Timestamp.valueOf(var);
        }
        else
            cwPage.ts = null;
        
    }

    /**
    get the value of a long type of parameter given the parameter name <BR>
    Return : long value of the parameter <BR>
             LONG_PARAMETER_NOT_FOUND if the parameter cannot be found <BR>
    */
    protected long getLongParameter(String paraname) throws cwException {
        
        long value;
        String var = null;
        try {

            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            String val = cwUtils.esc4JS(var);
            if (var != null && var.length() > 0)
                value = Long.valueOf(val).longValue();
            else 
                value = LONG_PARAMETER_NOT_FOUND;
            
            return value;
            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }



    /**
    get the value of a float type of parameter given the parameter name <BR>
    Return : float value of the parameter <BR>
             FLOAT_PARAMETER_NOT_FOUND if the parameter cannot be found <BR>
    */
    protected float getFloatParameter(String paraname) throws cwException {
        
        float value;
        String var = null;
        try {

            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            if (var != null && var.length() > 0)
                value = Float.valueOf(var).floatValue();
            else 
                value = FLOAT_PARAMETER_NOT_FOUND;
            
            return value;
            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }
    
    protected double getDoubleParameter(String paraname) throws cwException {
        
        double value;
        String var = null;
        try {

            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            if (var != null && var.length() > 0)
                value = Double.valueOf(var).doubleValue();
            else 
                value = 0;
            
            return value;
            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }



    /**
    get the value of a integer type of parameter given the parameter name <BR>
    Return : integer value of the parameter <BR>
             INT_PARAMETER_NOT_FOUND if the parameter cannot be found <BR>
    */
    protected int getIntParameter(String paraname) throws cwException {
        
        int value;
        String var = null;
        try {
            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            if (var != null && var.length() > 0) {
                value = Integer.valueOf(var).intValue();
            }else  {
                value = INT_PARAMETER_NOT_FOUND;
            }
            return value;                            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }

    /**
    get the value of a string type of parameter given the parameter name <BR>
    Return : String value of the parameter <BR>
             null if the parameter cannot be found <BR>
    */
    protected String getStringParameter(String paraname) throws cwException {
        Enumeration param = req.getParameterNames();
        String value;
        String var = null;
        try {
            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            if (var != null && var.length() > 0) {
                value = var;
            }else  {
                value = null;
            }
            return value;                            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }

    /**
    get the value of a timestamp type of parameter given the parameter name <BR>
    Return : Timestamp object <BR>
             null if the parameter cannot be found <BR>
    */
    protected Timestamp getTimestampParameter(String paraname) throws cwException {
        
        Timestamp value;
        String var = null;
        try {
            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            if (var != null && var.length() > 0) {
                value = cwUtils.parse(var);
            }else  {
                value = null;
            }
            return value;                            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }

    /**
    get the value of a boolean type of parameter given the parameter name <BR>
    Return : boolean value of the parameter <BR>
             true if the value is one of {"TRUE", "YES", "ON", "Y"}
             false if the parameter cannot be found <BR>
    */
    protected boolean getBooleanParameter(String paraname) throws cwException {

        boolean value;
        String var = null;
        try {
            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            if (var != null && var.length() > 0) {
                if (var.equalsIgnoreCase("TRUE") ||
                    var.equalsIgnoreCase("YES") ||
                    var.equalsIgnoreCase("Y") ||
                    var.equalsIgnoreCase("ON")) {
                    value = true;
                }else {
                    value = false;                       
                }
            }else  {
                value = false;
            }
            return value;                            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }

    /**
    get the array of a String type of parameter given the parameter name and separator <BR>
    Return : string array containing the values of the parameter <BR>
             null if the parameter cannot be found <BR>
    */
    protected String[] getStrArrayParameter(String paraname, String delimiter) throws cwException {
        
        String[] value = null;
        String var = null;
        try {
            var = (bMultipart) ? multi.getParameter(paraname) : req.getParameter(paraname);
            if (var != null && var.length() > 0) {
                value = split(var, delimiter);
            }
            return value;                            
        }catch (Exception e) {
            throw new cwException ("Parameter format exception : name=" + paraname + ",value=" + var);
        }
    }

    /**
    get the array of a long type of parameter given the parameter name and separator <BR>
    Return : long array containing the values of the parameter <BR>
             null if the parameter cannot be found <BR>
    */
    protected long[] getLongArrayParameter(String paraname, String delimiter) throws cwException {
        
        String[] str = getStrArrayParameter(paraname, delimiter);
        if(str == null) return null;
        long[] f = new long[str.length];
        for(int i=0; i<str.length; i++) {
            f[i] = Long.parseLong(str[i]);
        }

        return f;
    }

    /**
    get the array of a float type of parameter given the parameter name and separator <BR>
    Return : float array containing the values of the parameter <BR>
             null if the parameter cannot be found <BR>
    */
    protected float[] getFloatArrayParameter(String paramName, String delimiter)
        throws cwException {
        String[] str = getStrArrayParameter(paramName, delimiter);
        if(str == null) return null;
        float[] f = new float[str.length];
        for(int i=0; i<str.length; i++) {
            f[i] = Float.parseFloat(str[i]);
        }
        return f;
    }

    /**
    get the array of a boolean type of parameter given the parameter name and separator <BR>
    Return : boolean array containing the values of the parameter <BR>
             null if the parameter cannot be found <BR>
    */
    protected boolean[] getBooleanArrayParameter(String paramName, String delimiter)
        throws cwException {
        String[] str = getStrArrayParameter(paramName, delimiter);
        if(str == null) return null;
        boolean[] b = new boolean[str.length];
        for(int i=0; i<str.length; i++) {
            if (str[i] != null &&
                (str[i].equalsIgnoreCase("TRUE") ||
                str[i].equalsIgnoreCase("YES") ||
                str[i].equalsIgnoreCase("Y") ||
                str[i].equalsIgnoreCase("ON"))) {
                    b[i] = true;
            } else {
                b[i] = false;
            }
        }
        return b;
    }

    
    /**
    method to split a given parameter to a string array using the given separator <BR>
    Return : string array containing the values of the parameter <BR>
             null if the parameter cannot be found <BR>
    */
    protected static String[] split(String in, String delimiter)
    {
        Vector q = new Vector();
        String[] result = null;
        
        if (in == null || in.length()==0)
            return result;
        
        int pos =0;
        pos = in.indexOf(delimiter);
        
        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos); 
            }
            q.addElement(val);
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.addElement(in);
        }

        result = new String[q.size()];
        for (int i=0; i<q.size();i++) {
            result[i] = (String) q.elementAt(i);
        }
        
        return result;
        
    }
    
    /**
    Convert input stream recieved from client to unicode
    @param in input stream recieved from client
    @return a unicode string which is converted using correct encoding
    */
    public String unicode(String in)
        throws cwException
    {
//        try {
//            if (bMultipart)
//                return in;
//            else if ( in==null || in.length()==0)
//                return in;
//            else
//                return new String(in.getBytes(clientEnc), encoding);
//        }catch (UnsupportedEncodingException e) {
//            throw new cwException(e.getMessage());
//        }   
        return in;
    }

    
}
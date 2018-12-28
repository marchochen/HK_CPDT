package com.cw.wizbank.ae;

import java.io.*;
import java.net.*;

import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.SendHttpRequest;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.utils.CommonLog;

import java.util.Vector;
import java.math.*;
import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

// class for utility methods
public class aeUtils {
    
    public static final Timestamp EMPTY_DATE = new Timestamp(0);
    public static final String MSG_REC_NOT_FOUND = "GEN005";
    
    public static String escEmptyDate(Timestamp in) {
        if(in == null || in.equals(aeUtils.EMPTY_DATE))
            return "";
        else
            return in.toString();
    }
    
    /**
    Convert an array of string to a string with the format "('id1', 'id2', 'id3')"
    Note that this function will not esc the "'"
    */
    public static String array2SQLList(String[] id) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(");
        if(id!=null) {
            for(int i=0;i<id.length-1;i++){ 
                listBuf.append("'").append(id[i]).append("'").append(",");
            }
            listBuf.append("'").append(id[id.length-1]).append("'");
        }
        listBuf.append(")");    
        return listBuf.toString();
    }
    
    public static Timestamp getTimeAfter(Timestamp t, int field, int amount) {
        Calendar c = Calendar.getInstance();
        
        c.setTime(t);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(field, amount);
        
        Timestamp result = new Timestamp(c.getTime().getTime());
        return result;
    }
    
    
    public static Timestamp[] getMonthBeginEnd(Timestamp t) {
        Timestamp result[] = new Timestamp[2];
        
        Calendar c = Calendar.getInstance();
        
        c.setTime(t);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.set(Calendar.DAY_OF_MONTH, 1);
        result[0] = new Timestamp(c.getTime().getTime());
        
        
        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND , 59);
        c.set(Calendar.MILLISECOND , 999);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        result[1] = new Timestamp(c.getTime().getTime());
        
        return result;
    }

    
    public static Timestamp[] getWeekBeginDates(Timestamp t1, Timestamp t2, int WeekBeginDay) {
        Calendar c = Calendar.getInstance();
        Vector v = new Vector();
        
        c.setTime(t1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        int dayDiff = c.get(Calendar.DAY_OF_WEEK) - WeekBeginDay;
        
        if(dayDiff < 0)
            dayDiff = -1*dayDiff;
        else if(dayDiff > 0)
            dayDiff = 7 - dayDiff;
        c.add(Calendar.DAY_OF_WEEK, dayDiff);
        Timestamp temp = new Timestamp(c.getTime().getTime());
        while(temp.before(t2) || temp.equals(t2)) 
        {
            v.addElement(temp);
            c.add(Calendar.WEEK_OF_MONTH, 1);
            temp = new Timestamp(c.getTime().getTime());
        }
        //while(t2.after(new Timestamp(c.getTime().getTime())) || t2.equals(new Timestamp(c.getTime().getTime())));
        
        return vec2TimestampArray(v);
    }    
    
    public static Timestamp getFirstDateOfWeek(int WeekOfMonth, int Month, int Year, int WeekBeginDay) {
        Timestamp t1 ;
        
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_WEEK, WeekBeginDay);
                
        c.set(Year, Month, 1);
        c.set(Calendar.DAY_OF_WEEK_IN_MONTH, WeekOfMonth);
        t1 =  new Timestamp(c.getTime().getTime());   
        return t1;
    }
    
    
    public static String transformXML(String inXML, String inXSLFilename, qdbEnv inEnv, String xsl_root) throws cwException {
/*        try {
            if (inXSLFilename == null || inXSLFilename.length() == 0)
                throw new cwException("Invalid stylesheet for transform");
            
            StringWriter outXML = new StringWriter(2048);
            inEnv.procXSLFile(inXML, inXSLFilename, outXML, null);
            outXML.close();
            return outXML.toString();
        } catch (IOException e) {
            throw new cwException(e.getMessage());
        }*/
        return inEnv.transformXML(inXML, inXSLFilename, xsl_root);
    }  
    
    public static Timestamp[] vec2TimestampArray(Vector vec) {
        Timestamp result[] = new Timestamp[vec.size()];
        for(int i=0; i<vec.size(); i++) {
            result[i] = (Timestamp)vec.elementAt(i);
        }
        return result;            

    }
    
    public static Vector floatArray2Vec(float[] floatArray) {
        Vector vec = new Vector();
        for(int i=0; i<floatArray.length; i++)
            vec.addElement(new Float(floatArray[i]));
        return vec;
    }
    
    public static float[] vec2floatArray(Vector vec) {
        float[] floatArray = new float[vec.size()];
        for(int i=0; i<vec.size(); i++)
            floatArray[i] = ((Float)vec.elementAt(i)).floatValue();
        return floatArray;            
    }

    public static Vector intArray2Vec(int[] intArray) {
        Vector vec = new Vector();
        for(int i=0; i<intArray.length; i++)
            vec.addElement(new Integer(intArray[i]));
        return vec;
    }
    
    public static String[] vec2StringArray(Vector vec) {
        String[] strArray = new String[vec.size()];
        for(int i=0; i<vec.size(); i++)
            strArray[i] = (String)vec.elementAt(i);
        return strArray;
    }

    public static int[] vec2intArray(Vector vec) {
        int[] intArray = new int[vec.size()];
        for(int i=0; i<vec.size(); i++)
            intArray[i] = ((Integer)vec.elementAt(i)).intValue();
        return intArray;
    }

    public static Vector longArray2Vec(long[] longArray) {
        Vector vec = new Vector();
        for(int i=0; i<longArray.length; i++)
            vec.addElement(new Long(longArray[i]));
        return vec;
    }
    
    public static long[] vec2longArray(Vector vec) {
        long[] longArray = new long[vec.size()];
        for(int i=0; i<vec.size(); i++)
            longArray[i] = ((Long)vec.elementAt(i)).longValue();
        return longArray;
    }
    
    public static long[][] vec2long2DimArray(Vector v1, Vector v2) {
        int size=0;
        if(v1.size()<=v2.size())
            size = v1.size();
        else
            size = v2.size();
            
        long[][] longArray = new long[2][size];
        for(int i=0; i<size; i++) {
            longArray[0][i] = ((Long)v1.elementAt(i)).longValue();
            longArray[1][i] = ((Long)v2.elementAt(i)).longValue();
        }
        return longArray;
    }
    
    public static String twoDecPt(float var) {
        String floatStr = Float.toString(var);
        if( floatStr.indexOf(".") + 3 <= floatStr.length() )
            return floatStr.substring(0, floatStr.indexOf(".")+3);
        else
            return floatStr.substring(0, floatStr.indexOf(".")+2)+"0";
    }
    
    
    public static float roundingFloat(float var, int decPt) {                 
        BigDecimal decNum = new  BigDecimal((new Float(var)).doubleValue());
        return (decNum.setScale(decPt, BigDecimal.ROUND_HALF_UP)).floatValue();         
    }
    
    public static String valueTemplate(String inTemplateXML, String inDetailsXML, qdbEnv inEnv) throws cwException {
        if (inTemplateXML == null) inTemplateXML = "";
        if (inDetailsXML  == null) inDetailsXML  = "";
        StringBuffer xml = new StringBuffer(4096);
        xml.append("<applyeasy>").append(inTemplateXML).append(inDetailsXML).append("</applyeasy>");
        String out = transformXML(xml.toString(), inEnv.INI_XSL_VALTPL, inEnv, null);
        return out;
    }

    public static String escZero(long in) {
        String result;
        if(in == 0) 
            result = "";
        else
            result = Long.toString(in);
        
        return result;
    }

    public static String escZero(float in) {
        String result;
        if(in == 0) 
            result = "";
        else
            result = Float.toString(in);
        
        return result;
    }
    
    public static String escNull(Object in) {
        String result;
        if(in == null) 
            result = "";
        else
            result = in.toString();
        
        return result;
    }
    
    public static String prepareSQLList(long[] id) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(0");
        if(id!=null) {
            for(int i=0;i<id.length;i++) 
                listBuf.append(",").append(id[i]);
        }
        listBuf.append(")");    
        list = new String(listBuf);
        return list;
    }

    public static String prepareSQLList(Vector v) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(0");
        if(v!=null) {
            for(int i=0;i<v.size();i++) 
                listBuf.append(",").append(((Long)v.elementAt(i)).longValue());
        }
        listBuf.append(")");    
        list = new String(listBuf);
        return list;
    }

    public static String prepareSQLList(long[] ent_id, long cat_create_usr_ent_id) {
        StringBuffer listBuf = new StringBuffer(30);
        listBuf.append("(").append(cat_create_usr_ent_id);
        
        for(int i=0; i<ent_id.length; i++) 
            listBuf.append(",").append(ent_id[i]);
            
        listBuf.append(")");
        String list = new String(listBuf);
        return list;
    }



    public static String urlRedirect(String args, HttpServletRequest request)
        throws Exception {
            
            int index = args.indexOf("?");
            String url = args.substring(0, index);
            int length = args.length();
            args = args.substring(index+1, length);
            CommonLog.debug("url = " + url);
            CommonLog.debug("args = " + args);
            return urlRedirect(url, args, request);
    
        }



    /**
    Make a http connection and post the args
    @param url of the http connection
    @param args to be passed
    @param http request
    @return value returned by the http connection
    */
    public static String urlRedirect(String url, String args, HttpServletRequest request)
        throws Exception {
                                    
            StringBuffer xml = new StringBuffer();
            
            try{
                args += "&";


                StringBuffer requestParam = new StringBuffer();
                int index = args.indexOf("&");
                while( index > 0 ) {
                    String element = args.substring(0, index);
                    int subIndex = element.indexOf("=");
                    int strLength = element.length();
                    requestParam.append(URLEncoder.encode(element.substring(0,subIndex)));
                    requestParam.append("=");
                    requestParam.append(URLEncoder.encode(element.substring(subIndex+1,strLength)));
                    requestParam.append("&");
                    args = args.substring(index+1);
                    index = args.indexOf("&");
                }
                if(url != null && url.length() > 0){
                    xml.append( SendHttpRequest.sendUrl(url, requestParam.toString(), null, request, null));
                }

            } catch (Exception e) {
                CommonLog.error("Thread Error : " + e);
            }

            return (xml.toString()).trim();
        }        
        
    /**
    Intersect the input Vectors. 
    If one of the input Vectors is null, return an empty Vector
    */
    public static Vector intersectVectors(Vector v1, Vector v2) {
        Vector v = new Vector();
        if(v1 != null && v2 != null) {
            for(int i=0; i<v1.size(); i++) {
                if(v2.contains(v1.elementAt(i))) {
                    v.addElement(v1.elementAt(i));
                }
            }
        }
        return v;
    }

    /**
    Union the input Vectors. 
    */
    public static Vector unionVectors(Vector v1, Vector v2) {
        Vector v = new Vector();
        if(v1 == null && v2 == null) {
            return null;
        } else if(v1 == null) {
            return v2;
        } else if(v2 == null) {
            return v1;
        } else {
            for(int i=0; i<v1.size(); i++) {
                v.addElement(v1.elementAt(i));
            }
            for(int i=0; i<v2.size(); i++) {
                if(!v.contains(v2.elementAt(i))) {
                    v.addElement(v2.elementAt(i));
                }
            }
        }
        return v;
    }
}

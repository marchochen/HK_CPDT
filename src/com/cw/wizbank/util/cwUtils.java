package com.cw.wizbank.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;

import sun.misc.BASE64Encoder;



// Perl regular expression
import com.oreilly.servlet.MultipartRequest;
import com.oroinc.text.perl.*;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.qdbAction;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.ContextPath;

/**
A utility class to store all common APIs
*/
public class cwUtils
{

    //public static final String AUTH_LOGIN_PROFILE    = "auth_login_profile";
    //public static final String AUTH_LOGIN_ENV        = "auth_login_env";

    public static final String ENC_ENG = "ISO-8859-1";
    public static final String ENC_UTF = "UTF-8";
    public static final String ENC_UNICODE_LITTLE = "UnicodeLittle";

    public static final String NEWL = System.getProperty("line.separator");
    public static final String SLASH = System.getProperty("file.separator");

    public static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"" + ENC_UTF + "\" standalone=\"no\" ?>" + NEWL;

    public static final String APP_SERVER_WEBSPHERE = "websphere";

    public static final String IMMEDIATE = "IMMEDIATE";
    public static final String UNLIMITED = "UNLIMITED";
    public static final String MIN_TIMESTAMP = "1753-01-01 00:00:00";
    public static final String MAX_TIMESTAMP = "9999-12-31 23:59:59.000";

    public static final String TOKEN_ID = "$id";

    public static final byte[] BOM_UTF8 = {(byte)0xEF, (byte)0xBB, (byte)0xBF};

	private static final String ProviderSunJCE  = "com.sun.crypto.provider.SunJCE";
    private static final String WzbMacAlg       = "HmacSHA1";
    private static final String WzbMacKey       = "This is wizBank verify key";

	public static final String MESSAGE_SESSION_TIMEOUT = "SESSION TIMEOUT";
	public static final String MESSAGE_NO_RECOGNIZABLE_CMD = "No recognizable command specified:";
	public static final String MESSAGE_NULL_CMD = "Invalid Command";

    public static final String RESPONSE_HEADER_SESSION_TIMEOUT = "Wzb-Session-Timeout";
    public static final String RESPONSE_HEADER_RELOGIN_URL = "Wzb-Relogin-Url";
    public static final String RESPONSE_HEADER_LOCATION = "Wzb-Location";
    public static final String RESPONSE_HEADER_SYSTEM_MESSAGE = "Wzb-System-Message";
    public static final String RESPONSE_HEADER_MESSAGE_TITLE = "Wzb-Message-Title";
    public static final String RESPONSE_HEADER_MESSAGE_BUTTON = "Wzb-Message-Button";

    public static final String WIZBANK_LOG_FOLDER = "log";

    public static final String TYPE_IMAGE_GIF = "gif";
    public static final String[] LANGS = {"ISO-8859-1", "Big5", "GB2312"};
    //private static final byte[] BOM_UTF16LE = {(byte)0xFF, (byte)0xFE};
    //private static final byte[] BOM_UTF16BE = {(byte)0xFE, (byte)0xFF};

    public static Perl5Util perl = new Perl5Util();

    public cwUtils () {;}

    /**
    Mapping of charset and language
    */
    public static String CharsetToLanguage(String encoding)
    {
        Hashtable map = new Hashtable();

        map.put("ISO-8859-1", "en");
        map.put("Shift_JIS", "ja");
        map.put("EUC-KR", "ko");     // Requires JDK 1.1.6
        map.put("ISO-8859-5", "uk");
        map.put("GB2312", "zh");
        map.put("Big5", "zh_TW");

        String val = (String) map.get(encoding);
        return val;
    }

    /**
    Return the real path of a url which can be real or relative
    */
    public static String getRealPath(HttpServletRequest request, String url)
        throws cwException
    {
    	if (url == null)
            return url;

        String realURL = new String();

        // url is not real path
        if (url.toLowerCase().startsWith("http") ||
            url.toLowerCase().startsWith("javascript")||
             url.toLowerCase().startsWith("exedjs_function")
            ) {
                
            realURL = url;
            
        }else {
                
            if (url.startsWith("../") ) {
            	
                url = ContextPath.getContextPath() + url.substring(url.lastIndexOf("../")+2,url.length());
                
            }
                
            realURL = url;
        }

        return realURL;
    }

    /**
    Parse a xml file and do the following transformation if matched <BR>
    1. "null" --> ""
    2. >null< --> ><
    */
    public static String escNull(String in)

    {
        if (in == null){
            return "";
        }
        // Replace all "null" to ""
        String out = perl.substitute("s#\"null\"#\"\"#ig", in);
        // Replace all >null< to ><
        out = perl.substitute("s#>null<#><#ig", out);
        return out;
    }

    /**
    Check if a string array contains the specified string (Not case sensitive)
    */
    public static boolean strArrayContains(String[] array, String matchStr)
    {
            return strArrayContains(array, matchStr, false);
    }

    /**
    Check if a string array contains the specified string (case sensitive)
    */
    public static boolean strArrayContains(String[] array, String matchStr, boolean cs)
    {

            for (int i=0;i<array.length;i++) {
                if (cs && array[i].equals(matchStr))
                    return true;
                else if (!cs && array[i].equalsIgnoreCase(matchStr))
                    return true;
            }
            return false;
    }

    /**
    Escape special characters in xml
    */
	public static String esc4Html(String in) {
//		return esc4XML(in);
		
		 if(in==null || in.length()==0)
             return in;
             
         String result="";
         if(perl.match("#<#i", in))
             in = perl.substitute("s#<#&lt;#ig", in);
         if(perl.match("#\"#i", in))
             in = perl.substitute("s#\"#&quot;#ig", in);
         
         if(perl.match("#Content-Type:#i", in))
             in = perl.substitute("s#Content-Type:#&quot;#ig", in);
         
         if(perl.match("#script#i", in))
             in = perl.substitute("s#^script# #ig", in);
         return in;
	}	  


   public static String esc4JS(String in) {
		
		 return esc4JS(in, false);
	}
	
	
	public static String esc4JS(String in,boolean isUrl) {
		
		 if(in==null || in.length()==0)
            return in;
            
        String result="";

        if(perl.match("#\"#i", in))
            in = perl.substitute("s#\"#&quot;#ig", in);
        
        if(perl.match("#Content-Type:#i", in))
            in = perl.substitute("s#Content-Type:#Content_Type:#ig", in);
        
        if(perl.match("#<#i", in))
            in = perl.substitute("s#<#&lt;#ig", in);
        
        if(perl.match("#alert#i", in))
            in = perl.substitute("s#alert#al ert#ig", in);
        
       
        
       

       
        
        if(!isUrl){
        	 if(perl.match("#'#i", in)){
               	 in = perl.substitute("s#'# #ig", in);
                }
        	 if(perl.match("#\"#i", in)){
               	 in = perl.substitute("s#\"# #ig", in);
                }
	       	 if(perl.match("#script#i", in))
	                in = perl.substitute("s#script#sc ript#ig", in);
	       	 
	       	 if(perl.match("#window#i", in) && perl.match("#location#i", in))
	                in = perl.substitute("s#window#win dow#ig", in);
	        }
        return in;
	}
	
    public static String esc4XML(String in) {
        if (in == null || in.length() == 0)
            return in;

        String result = "";

        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);
        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);
        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);
        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);

        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);
        if (perl.match("#&#i", in))
            in = perl.substitute("s#&#&amp;#ig", in);
        if (perl.match("#<#i", in))
            in = perl.substitute("s#<#&lt;#ig", in);
        if (perl.match("#\"#i", in))
            in = perl.substitute("s#\"#&quot;#ig", in);
        
        if (perl.match("#♂#i", in))
            in = perl.substitute("s#♂# #ig", in);
        
        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);

        return in;
    }

    public static String esc4XmlJs(String in) {
        if (in == null || in.length() == 0)
            return in;

        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);
        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);
        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);
        if (perl.match("##i", in))
            in = perl.substitute("s## #ig", in);

        if (perl.match("#&#i", in))
            in = perl.substitute("s#&#&amp;#ig", in);
        if(perl.match("#<#i", in))
            in = perl.substitute("s#<#&lt;#ig", in);
        if(perl.match("#\"#i", in))
            in = perl.substitute("s#\"#&quot;#ig", in);
        if(perl.match("#\r\n#i", in))
            in = perl.substitute("s#\r\n#\\n#ig", in);
        if(perl.match("#'#i", in))
            in = perl.substitute("s#'#\\&\\#39;#ig", in);
        if(perl.match("#\"#i", in))
            in = perl.substitute("s#\"#\\&quot;#ig", in);
        return in;
    }

    /**
     * 鎹㈣
     * @param in
     * @return
     */
    public static String esc4Xmlhrn(String in) {
        if (in == null || in.length() == 0)
            return in;

        if(perl.match("#\r\n#i", in))
            in = perl.substitute("s#\r\n##ig", in);
       
        return in;
    }
    /**
    Escape special characters in json.
    */
    public static String esc4Json(String in)
    {
        if(in==null || in.length()==0)
            return in;

        in = in.replaceAll("Content-Type:", "Content_Type:");
        in = in.replaceAll("alert", "al#ert");
        in = in.replaceAll("javascript", "javasc#ript");
        in = in.replaceAll("<\\s*script", "<sc#ript");
        in = in.replaceAll("window.", "window-");
        in = in.replaceAll("ontoggle", "on#toggle");
        in = in.replaceAll("<details", "<det#ails");
        in = in.replaceAll("&lt;details", "&lt;det#ails");
        in = in.replaceAll("iframe", "ifr#ame");
        return in;
    }
       
    
    /**
    replace # to \#
    */
    public static String escHash(String in)
        {
            if(in==null || in.length()==0)
                return in;

            String result="";
            if (in.indexOf("#") >=0){
                String left_str = in.substring(0, in.indexOf("#"));
                String right_str = in.substring(in.indexOf("#") + 1);
                in = left_str + "\\#" + escHash(right_str);
            }
            return in;
        }

    /**
    Get the client character encoding
    */
    public static String getCharacterEncoding(ServletContext sc, String enc)
    {
    	return (enc == null || enc.length() == 0) ? ENC_ENG : enc;
/*        String clientEnc = null;

        if (sc.getServerInfo().toLowerCase().indexOf(APP_SERVER_WEBSPHERE) > 0) {
            clientEnc = enc;
        }

        if (clientEnc == null || clientEnc.length()==0) {
            clientEnc = ENC_ENG;
        }

        return clientEnc;*/
    }

    /**
    Convert input stream recieved from client to unicode
    @param in input stream recieved from client
    @param inEnc client encoding
    @param fmEnc expected encoding of the input stream
    @param bMultipart the input stream is parsed by Multipart Object
    @return a unicode string which is converted using correct encoding
    */
     public static String unicodeFrom(String in, String inEnc, String fmEnc, boolean bMultipart)
        throws cwException
    {
//        try {
//            if (bMultipart)
//                return in;
//            else if ( in==null || in.length()==0)
//                return in;
//            else
//                return new String(in.getBytes(inEnc), fmEnc);
//        }catch (UnsupportedEncodingException e) {
//            throw new cwException(e.getMessage());
//        }
         return in;
    }

    /**
    Convert input stream recieved from client to unicode
    */
    public static String unicodeFrom(String in, String inEnc, String fmEnc)
        throws cwException
    {
        // default value of bMultipart is fasle;
        return unicodeFrom(in, inEnc, fmEnc, false);
    }

    /**
    Convert an array of long to a string with the format "(id1, id2, id3)"
    */
    public static String array2list(long[] id) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(");
        if(id!=null) {
            for(int i=0;i<id.length-1;i++){
                listBuf.append(id[i]).append(",");
            }
            listBuf.append(id[id.length-1]);
        }
        listBuf.append(")");
        return listBuf.toString();
    }

    /**
    Convert an array of int to a string with the format "(id1, id2, id3)"
    */
    public static String array2list(int[] id) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(");
        if(id!=null) {
            for(int i=0;i<id.length-1;i++){
                listBuf.append(id[i]).append(",");
            }
            listBuf.append(id[id.length-1]);
        }
        listBuf.append(")");
        return listBuf.toString();
    }

    /**
    Convert an array of string to a string with the format "(id1, id2, id3)"
    */

    public static String array2list(String[] id) {
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

    /**
    Convert a vector of Long Object to a string with the format "(id1, id2, id3)"
    */
    public static String vector2list(Vector idVec)
    {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(");

        if (idVec != null) {
            for(int i=0;i<idVec.size();i++){
                if (i > 0)
                    listBuf.append(",");

                listBuf.append(((Long) idVec.elementAt(i)).longValue());
            }
        }
        listBuf.append(")");
        return listBuf.toString();
    }

    /**
    Convert a String array to a vector
    */
    public static Vector String2vector(String[] s)
    {
        return String2vector(s, true);
    }

    public static Vector String2vector(String[] s, boolean keepDuplicate)
    {
        Vector v = new Vector();
        for(int i=0; i<s.length; i++) {
            if(keepDuplicate) {
                v.addElement(s[i]);
            } else {
                if(!v.contains(s[i])) {
                    v.addElement(s[i]);
                }
            }
        }
        return v;
    }

    /**
    Convert a String array to a ArrayList
    */
    public static ArrayList string2ArrayList(String[] s)
    {
        return string2ArrayList(s, true);
    }

    public static ArrayList string2ArrayList(String[] s, boolean keepDuplicate)
    {
    	ArrayList v = new ArrayList();
        for(int i=0; i<s.length; i++) {
            if(keepDuplicate) {
                v.add(s[i]);
            } else {
                if(!v.contains(s[i])) {
                    v.add(s[i]);
                }
            }
        }
        return v;
    }

    /**
    Convert a String array to a ArrayList
    */
    public static ArrayList string2LongArrayList(String[] s)
    {
        return string2LongArrayList(s, true);
    }

    public static ArrayList string2LongArrayList(String[] s, boolean keepDuplicate)
    {
    	ArrayList v = new ArrayList();
        for(int i=0; i<s.length; i++) {
            if(keepDuplicate) {
                v.add(new Long(s[i]));
            } else {
                if(!v.contains(s[i])) {
                    v.add(new Long(s[i]));
                }
            }
        }
        return v;
    }

    /**
    Convert a long array to a ArrayList
    */
    public static ArrayList long2ArrayList(long[] l)
    {
        return long2ArrayList(l, true);
    }

    public static ArrayList long2ArrayList(long[] l, boolean keepDuplicate)
    {
    	ArrayList v = new ArrayList();
    	Long obj = null;
        for(int i=0; i<l.length; i++) {
        	obj = new Long(l[i]);
            if(keepDuplicate) {
                v.add(obj);
            } else {
                if(!v.contains(obj)) {
                    v.add(obj);
                }
            }
        }
        return v;
    }

    /**
    Convert a float array to a vector
    */
    public static Vector float2vector(float[] f)
    {
        Vector v = new Vector();
        for(int i=0; i<f.length; i++) {
            v.addElement(new Float(f[i]));
        }
        return v;
    }

    /**
    Convert a long array to a vector
    */
    public static Vector long2vector(long[] l)
    {
        Vector v = new Vector();
        for(int i=0; i<l.length; i++) {
            v.addElement(new Long(l[i]));
        }
        return v;
    }

    /**
    Convert an enumeration to a vector
    */
    public static Vector enum2vector(Enumeration enumeration)
    {

        Vector idVec = new Vector();
        while (enumeration.hasMoreElements()) {
            idVec.addElement((Long) enumeration.nextElement());
        }

        return idVec;
    }

    /**
    Rounding the float point number
    */
    public static float roundingFloat(float var, int decPt) {
        BigDecimal decNum = new  BigDecimal((new Float(var)).doubleValue());
        return (decNum.setScale(decPt, BigDecimal.ROUND_HALF_UP)).floatValue();
    }


    //Vector with Long object
    public static Vector splitToVec(String in, String delimiter) {

        Vector q = new Vector();
        if( in == null || in.length() == 0 ) {
            return q;
        }

        int pos =0;
        pos = in.indexOf(delimiter);

        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos);
            }
            q.addElement(new Long(val.trim()));
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.addElement(new Long(in.trim()));
        }
        return q;
    }


    //Vector with String object
    public static Vector splitToVecString(String in, String delimiter) {

        Vector q = new Vector();
        if( in == null || in.length() == 0 ) {
            return q;
        }

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
        return q;
    }


    // default not trim when split
    public static String[] splitToString(String in, String delimiter) {
        return splitToString(in, delimiter, false);
    }
    public static String[] splitToString(String in, String delimiter, boolean bTrim) {

        String obj[] = null;
        if( in == null || in.length() == 0 ) {
            obj = new String[0];
            return obj;
        }

        boolean addColumn = false;
        if(in.lastIndexOf(delimiter) == in.length()-1){
        	addColumn = true;;
        }
        
        Vector q = new Vector();
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
        if(addColumn){
        	q.addElement("");
        }

        obj = new String[q.size()];
        for (int i=0; i<obj.length;i++) {
            obj[i] = (String)q.elementAt(i);
            if (bTrim){
                obj[i] = obj[i].trim();
            }
        }

        return obj;
    }



    public static float[] splitToFloat(String in, String delimiter) {

        float obj[] = null;
        if( in == null || in.length() == 0 ) {
            obj = new float[0];
            return obj;
        }

        Vector q = new Vector();
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

        obj = new float[q.size()];
        for (int i=0; i<obj.length;i++) {
            obj[i] = Float.parseFloat((String)q.elementAt(i));
        }

        return obj;
    }




    public static long[] splitToLong(String in, String delimiter) {

        long obj[] = null;
        if( in == null || in.length() == 0 ) {
            obj = new long[0];
            return obj;
        }

        List q = new ArrayList();
        int pos =0;
        pos = in.indexOf(delimiter);

        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos);
            }
            if( val != null && val.length() > 0 )
                q.add(val);
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.add(in);
        }

        obj = new long[q.size()];
        for (int i=0; i<obj.length;i++) {
            obj[i] = Long.parseLong(((String)q.get(i)).trim());
        }

        return obj;
    }

    public static boolean[] splitToBoolean(String in, String delimiter) {

        boolean obj[] = null;

        if( in == null || in.length() == 0 ) {
            obj = new boolean[0];
            return obj;
        }

        Vector q = new Vector();
        int pos =0;
        pos = in.indexOf(delimiter);

        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos);
            }
            if( val != null && val.length() > 0 )
                q.addElement(val);
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.addElement(in);
        }

        obj = new boolean[q.size()];
        for (int i=0; i<obj.length;i++) {
            obj[i] = Boolean.valueOf((String)q.elementAt(i)).booleanValue();
        }

        return obj;
    }

    public static Vector splitFileToVector(File sourceFile,String enc,String delimiter)throws cwException, cwSysMessage
    {

    	return splitFileToVector(sourceFile,enc,delimiter,true);
    }

	public static Vector splitFileToVector(File sourceFile,String enc,String delimiter, boolean bTrim)throws cwException, cwSysMessage
	{
		Vector result=new Vector();

		try {
			  if( !cwUtils.isValidEncodedFile(sourceFile, cwUtils.ENC_UNICODE_LITTLE) ) {
				  throw new cwSysMessage("GEN008");
		    }

			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile),enc));
			char []line = new char[1];
			StringBuffer inline = new StringBuffer();
			while(in.read(line,0,1)!= -1){
				inline.append(line);
				String charc = new String(line);
				if(charc.equalsIgnoreCase("\r")){
					String[] inElement= splitToString(inline.toString(),delimiter,bTrim);
					result.addElement(inElement);
					inline = new StringBuffer();
				}
			}
			/*
			String inline = in.readLine();
			while (inline != null) {
				String[] inElement=splitToString(inline,delimiter,bTrim);
				result.addElement(inElement);
				inline = in.readLine();
			}
			*/
			in.close();

		} catch (FileNotFoundException e) {
			throw new cwException("file error:" + e.getMessage() + " not found.");
		} catch (IOException e) {
			throw new cwException("read file error:" + e.getMessage());
		}

		return result;
	}

/**christ.qiu
 * @param sourceFile
 * @param enc
 * @param cols_len
 * @return
 * @throws cwException
 * @throws cwSysMessage
 */

	public static Vector splitFileByColLen(String filepath,String enc,int[]cols_len)throws cwException, IOException, cwSysMessage
	{
		File srcFile = new File(filepath);
		if (!srcFile.exists()){//make sure the file is exist
			//throw new cwSysMessage("GEN009");
			throw new cwException("File does not exist.");
		}
		if( !cwUtils.isValidEncodedFile(srcFile, cwUtils.ENC_UNICODE_LITTLE)){
			//TODO
			//make sure the file encoding is correct
			//throw new cwSysMessage("ULG012");
			throw new cwException("File encoding error.");
		}
		//TODO
		return splitFileByColLen(filepath,enc,cols_len,true);
	}

	public static Vector splitFileByColLen(String filepath,String enc,int[]cols_len, boolean bTrim)throws cwException, cwSysMessage
	{
		File srcFile = new File(filepath);
		if (!srcFile.exists()){//make sure the file is exist
//			throw new cwSysMessage("GEN009");
			throw new cwException("File does not exist.");
		}
		//TODO
		Vector result=new Vector();
		try {
			  if( !cwUtils.isValidEncodedFile(srcFile, cwUtils.ENC_UNICODE_LITTLE) ) {
//				  throw new cwSysMessage("ULG012");
				throw new cwException("File encoding error.");
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile),enc));
			String inline = in.readLine();
		while (inline != null) {
			String[] inElement = splitStrByLen(inline,cols_len,bTrim);
			if(inElement!=null){
				result.addElement(inElement);
			}
			inline = in.readLine();
			}
		in.close();
		} catch (FileNotFoundException e) {
			throw new cwException("file error:" + e.getMessage() + " not found.");
		} catch (IOException e) {
			throw new cwException("read file error:" + e.getMessage());
		}
		return result;
	}
/*
 * @author christ.qiu
 * @param in
 * @param cols_len
 * @return String[]
 */
	public static String[] splitStrByLen(String in, int[]cols_len) {
		//TODO
		String[] cols_value = null;
		if(in!=null){
			cols_value = splitStrByLen(in, cols_len, true);
		}
		return cols_value;
	}
	public static String[] splitStrByLen(String in, int[]cols_len, boolean bTrim) {
        //TODO
		String obj[] = null;
		if( in == null || in.length() == 0 ) {
			obj = new String[0];
			return obj;
		}
		int begin_index=0;
		int len;
		int end_index;
		Vector q = new Vector();
		for (int i = 0; i < cols_len.length;i++) {
			len = cols_len[i];
			end_index = begin_index+len;
			String val = in.substring(begin_index,end_index);
			q.addElement(val);
			begin_index = end_index;
		}
		obj = new String[q.size()];
		for (int i=0; i<obj.length;i++) {
			obj[i] = (String)q.elementAt(i);
			if (bTrim){
				obj[i] = obj[i].trim();
			}
		}
		return obj;
	}

    /**
    Get the servlet information
    */
    public static String getRequestInfo(HttpServletRequest req, ServletConfig cfg)
    {
        StringBuffer attribute = new StringBuffer();

        String NEWL = "<br>";

        // details of the servlet request
        attribute.append("ServletRequest:").append(NEWL)
        .append("getCharacterEncoding() = ").append(req.getCharacterEncoding()).append(NEWL)
        .append("getContentLength() = ").append(req.getContentLength()).append(NEWL)
        .append("getContentType() = ").append(req.getContentType()).append(NEWL)
        .append("getProtocol() = ").append(req.getProtocol()).append(NEWL)
        .append("getRemoteAddr() = ").append(req.getRemoteAddr()).append(NEWL)
        .append("getRemoteHost() = ").append(req.getRemoteHost()).append(NEWL)
        .append("getScheme() = ").append(req.getScheme()).append(NEWL)
        .append("getServerName() = ").append(req.getServerName()).append(NEWL)
        .append("getServerPort() = ").append(req.getServerPort()).append(NEWL)
        .append("isSecure() = ").append(req.isSecure()).append(NEWL)
        ;

        // details of the http servlet request
        attribute.append("HttpServletRequest:").append(NEWL)
        .append("getAuthType() = ").append(req.getAuthType()).append(NEWL)
        .append("getContextPath() = ").append(req.getContextPath()).append(NEWL)
        .append("getMethod() = ").append(req.getMethod()).append(NEWL)
        .append("getPathInfo() = ").append(req.getPathInfo()).append(NEWL)
        .append("getPathTranslated() = ").append(req.getPathTranslated()).append(NEWL)
        .append("getQueryString() = ").append(req.getQueryString()).append(NEWL)
        .append("getRemoteUser() = ").append(req.getRemoteUser()).append(NEWL)
        .append("getRequestedSessionId() = ").append(req.getRequestedSessionId()).append(NEWL)
        .append("getRequestURI() = ").append(req.getRequestURI()).append(NEWL)
        .append("getRequestURL() = ").append(req.getRequestURL().toString()).append(NEWL)
        .append("getServletPath() = ").append(req.getServletPath()).append(NEWL)
        .append("isRequestedSessionIdFromCookie() = ").append(req.isRequestedSessionIdFromCookie()).append(NEWL)
        .append("isRequestedSessionIdFromURL() = ").append(req.isRequestedSessionIdFromURL()).append(NEWL)
        .append("isRequestedSessionIdValid() = ").append(req.isRequestedSessionIdValid()).append(NEWL)
        ;

        // details of each cookie
        Cookie reqCookies[] = req.getCookies();
        if (reqCookies != null) {
            for (int i = 0; i < reqCookies.length; i++) {
                attribute.append("Cookie ").append(i).append(":").append(NEWL)
                .append("getComment() = ").append(reqCookies[i].getComment()).append(NEWL)
                .append("getDomain() = ").append(reqCookies[i].getDomain()).append(NEWL)
                .append("getMaxAge() = ").append(reqCookies[i].getMaxAge()).append(NEWL)
                .append("getName() = ").append(reqCookies[i].getName()).append(NEWL)
                .append("getPath() = ").append(reqCookies[i].getPath()).append(NEWL)
                .append("getSecure() = ").append(reqCookies[i].getSecure()).append(NEWL)
                .append("getValue() = ").append(reqCookies[i].getValue()).append(NEWL)
                .append("getVersion() = ").append(reqCookies[i].getVersion()).append(NEWL)
                ;
            }
        }

        // details of each http header
        Enumeration headerNameList = req.getHeaderNames();
        while (headerNameList.hasMoreElements()) {
            String headerName = (String)headerNameList.nextElement();
            String headerValue = req.getHeader(headerName);
            attribute.append("Http Header[").append(headerName).append("(")
            .append(headerValue).append(")]").append(NEWL)
            ;
        }

        // details of the http session
        HttpSession sess = req.getSession(false);
        if (sess != null) {
            attribute.append("HttpSession:").append(NEWL)
            .append("getCreationTime() = ").append(sess.getCreationTime()).append(NEWL)
            .append("getId() = ").append(sess.getId()).append(NEWL)
            .append("getLastAccessedTime() = ").append(sess.getLastAccessedTime()).append(NEWL)
            .append("getMaxInactiveInterval() = ").append(sess.getMaxInactiveInterval()).append(NEWL)
            .append("isNew() = ").append(sess.isNew()).append(NEWL)
            ;
            // details of each http session object
            Enumeration sessObjNameList = sess.getAttributeNames();
            while (sessObjNameList.hasMoreElements()) {
                String sessObjName = (String)sessObjNameList.nextElement();
                Object sessObj = sess.getAttribute(sessObjName);
                attribute.append("Session Object[").append(sessObjName).append("(")
                .append(sessObj.getClass().getName()).append(")]").append(NEWL)
                ;
            }
        }

        // details of the servlet config
        attribute.append("ServletConfig:").append(NEWL)
        .append("getServletName() = ").append(cfg.getServletName()).append(NEWL);
        // details of each init parameter
        Enumeration cfgInitParamNameList = cfg.getInitParameterNames();
        while (cfgInitParamNameList.hasMoreElements()) {
            String cfgInitParamName = (String)cfgInitParamNameList.nextElement();
            String cfgInitParamValue = cfg.getInitParameter(cfgInitParamName);
            attribute.append("Init Parameter[").append(cfgInitParamName).append("(")
            .append(cfgInitParamValue).append(")]").append(NEWL)
            ;
        }

        // details of the servlet context
        ServletContext ctx = cfg.getServletContext();
        attribute.append("ServletContext:").append(NEWL)
        .append("getMajorVersion() = ").append(ctx.getMajorVersion()).append(NEWL)
        .append("getMinorVersion() = ").append(ctx.getMinorVersion()).append(NEWL)
        .append("getRealPath(\"/\") = ").append(ctx.getRealPath("/")).append(NEWL)
        .append("getServerInfo() = ").append(ctx.getServerInfo()).append(NEWL)
        .append("getServletContextName() = ").append(ctx.getServletContextName()).append(NEWL)
        ;
        // details of each context attribute
        Enumeration ctxObjNameList = ctx.getAttributeNames();
        while (ctxObjNameList.hasMoreElements()) {
            String ctxObjName = (String)ctxObjNameList.nextElement();
            Object ctxObj = ctx.getAttribute(ctxObjName);
            attribute.append("Context Object[").append(ctxObjName).append("(")
            .append(ctxObj.getClass().getName()).append(")]").append(NEWL)
            ;
        }
        // details of each init parameter
        Enumeration ctxInitParamNameList = ctx.getInitParameterNames();
        while (ctxInitParamNameList.hasMoreElements()) {
            String ctxInitParamName = (String)ctxInitParamNameList.nextElement();
            String ctxInitParamValue = ctx.getInitParameter(ctxInitParamName);
            attribute.append("Init Parameter[").append(ctxInitParamName).append("(")
            .append(ctxInitParamValue).append(")]").append(NEWL)
            ;
        }

        return attribute.toString();
    }

    public static boolean isMaxTimestamp(Timestamp ts) {
      if(ts.equals(Timestamp.valueOf(MAX_TIMESTAMP)))
          return true;
      else
          return false;
    }

    //given a xsl file name, get it's virtual path
    //the xsl is either from xsl_root of organization or from ini file
    public static String getVirtualXslPath(String INI_XSL_HOME, String xsl_root,
                                            String xslName, ServletContext context) {

        String virtualXslPath;
	    String absoluteXslFile;

        if(xsl_root != null && xsl_root.length() > 0) {
            virtualXslPath = SLASH  + xsl_root + SLASH + xslName;
	        absoluteXslFile = context.getRealPath(virtualXslPath);
	        File f = new File(absoluteXslFile);
	        if(f.exists()) {
	            return virtualXslPath;
	        }
	    }
	    virtualXslPath = SLASH  + INI_XSL_HOME + SLASH  + xslName;
        return virtualXslPath;
    }

    // remove duplicate element in the vector
    public static void removeDuplicate(List vec) {
    	List compVec = new ArrayList();
        Long id;
        int pos=0;
        while (pos < vec.size()) {
            id = (Long)vec.get(pos);
            // If the object does not exists in the comparison vector,
            // that meansit is the first occurrence of the object,
            // otherwise , it is a duplicated one.
            if (!compVec.contains(id)) {
                compVec.add(id);
                pos ++;
            }else {
                // remove duplicated object
                vec.remove(pos);
            }
        }

        return;
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

    /**
     * 字符串数组转换成相应的长整型数组
     * @param s
     * @return
     */
    public static long[] stringArray2LongArray(String[] s) {

    	long[] ids = null;

    	if (s != null && s.length > 0) {
	    	ids = new long[s.length];

	        for(int i=0; i<s.length; i++)
	            ids[i] = Long.parseLong(s[i]);
    	}

	    return ids;
    }

    public static String[] longArray2stringArray(long[] in) {

        String[] str = new String[in.length];
        for(int i=0; i<in.length; i++)
            str[i] = Long.toString(in[i]);
        return str;

    }

    /**
    * If user role skin match the url path, return true
    * @param client HttpServletRequest
    * @param skin root of the user
    * @return boolean true if match
    */
    public static boolean checkUserRoleSkin(HttpServletRequest req, String currentRoleSkinRoot)
        throws IOException{

        boolean result = false;
        if (req.getRequestURI().indexOf(currentRoleSkinRoot) > -1) {
            result = true;
        }

        return result;
    }

    public static long[] vec2longArray(Vector v) {
        if(v==null) {
            return null;
        }
        long[] l = new long[v.size()];
        for(int i=0; i<v.size(); i++) {
            l[i] = ((Long)v.elementAt(i)).longValue();
        }
        return l;
    }

    public static String[] vec2strArray(Vector v) {
        if(v==null) {
            return null;
        }
        String[] l = new String[v.size()];
        for(int i=0; i<v.size(); i++) {
            l[i] = (String)v.elementAt(i);
        }
        return l;
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
    combine the input Vectors,
    */
    public static Vector unionVectors(Vector v1, Vector v2, boolean keepDuplicate) {
        if (v1 == null && v2 == null){
            return new Vector();
        }else if (v1 == null || v1.size() == 0){
            return v2;
        }else if (v2 == null || v2.size() == 0){
            return v1;
        }
        // use v2 as the base of the result
        Vector v = (Vector)v2.clone();

        for(int i=0; i<v1.size(); i++) {
            if (keepDuplicate){
                // add directly
                v.addElement(v1.elementAt(i));
            }else {
                // check if elements of vector1 are elements of vector2
                //if true --> add element to the result vector
                //if false --> proceed with next element
                if(!v2.contains(v1.elementAt(i))) {
                   v.addElement(v1.elementAt(i));
                }
            }
        }
        return v;
    }


    public static void sort(long a[]){
	    sort(a, 0, a.length-1);
    }

    private static void sort(long a[], int lo0, int hi0){
	    int lo = lo0;
	    int hi = hi0;

    	if (lo >= hi) {
	        return;
	    }
	    long mid = a[(lo + hi) / 2];
	    while (lo < hi) {
	        while (lo < hi && a[lo] < mid) {
    		    lo++;
	        }
	        while (lo < hi && a[hi] > mid) {
	    	    hi--;
	        }
	        if (lo < hi) {
		        long T = a[lo];
		        a[lo] = a[hi];
		        a[hi] = T;
	        }
	    }
	    if (hi < lo) {
	        int T = hi;
	        hi = lo;
	        lo = T;
	    }
	    sort(a, lo0, lo);
	    sort(a, lo == lo0 ? lo+1 : lo, hi0);
	    return;
    }

    /**
    Convert an array of string to a string with the format "id1~id2~id3) where ~ is delimiter"
    */

    public static String array2list(String[] id, String delimiter) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        if(id!=null) {
            for(int i=0;i<id.length-1;i++){
                listBuf.append(id[i]).append(delimiter);
            }
            listBuf.append(id[id.length-1]);
        }
        return listBuf.toString();
    }

	/**
		* Random the object in vector. If error, this method returns original vector.
		* @param vec The vector to be randomized.
		*
		*/
	   public static Vector randomVec(Vector vec){
		   try{
			   return cwUtils.randomDrawFromVec(vec, vec.size());
		   }catch(cwException e){
			   return vec;
		   }
	   }

	   /**
		* Randomly draw required number of object from vector provided.
		* @param vec Vector of object.
		* @param count Number of object requirement.
		* @return Vector of object.
		* @throws cwException Thrown if required object more than vector size.
		*/
	   public static Vector randomDrawFromVec(List vec, long count)
		   throws cwException{
		   	   Random random = new Random();
			   if( count > vec.size() ){
				   throw new cwException("Required object more than vector size.");
			   }
			   Vector v_tmp = new Vector();
			   for(int i=0; i<count; i++){
				   int index = random.nextInt(vec.size());
				   v_tmp.addElement(vec.get(index));
				   vec.remove(index);
			   }
			   return v_tmp;
		   }

	public static Vector randomDrawFromVec(Vector vec, long count, Random random)
		throws cwException{
		if( count > vec.size() ){
			throw new cwException("Required object more than vector size.");
		}
		Vector v_tmp = new Vector();
		for(int i=0; i<count; i++){
			int index = random.nextInt(vec.size());
			v_tmp.addElement(vec.elementAt(index));
			vec.removeElementAt(index);
		}
		return v_tmp;
	}


    /**
    Used for URL_SUCCESS and URL_FAILURE to replace the id token in the url
    */
    public static String substituteURL(String url, long id) {
        String returnURL = null;

        // replace the ID token in the url
        if (url.indexOf(cwUtils.TOKEN_ID) != -1) {
            StringBuffer sbURL = new StringBuffer(url);
            sbURL.replace(url.indexOf(cwUtils.TOKEN_ID), url.indexOf(cwUtils.TOKEN_ID) + cwUtils.TOKEN_ID.length(), Long.toString(id));
            returnURL = sbURL.toString();
        }
        else if (url.indexOf(URLEncoder.encode(cwUtils.TOKEN_ID)) != -1) {
            StringBuffer sbURL = new StringBuffer(url);
            sbURL.replace(url.indexOf(URLEncoder.encode(cwUtils.TOKEN_ID)), url.indexOf(URLEncoder.encode(cwUtils.TOKEN_ID)) + (URLEncoder.encode(cwUtils.TOKEN_ID)).length(), Long.toString(id));
            returnURL = sbURL.toString();
        }else{
			returnURL = url;
        }

        return returnURL;
    }

    public static String langToLabel(String lang){
        Hashtable map = new Hashtable();

        map.put("en-us", "ISO-8859-1");
        map.put("zh-cn", "GB2312");
        map.put("zh-hk", "Big5");

        String val = (String) map.get(lang);
        return val;
    }

    public static Timestamp[] getMonthBeginEnd(Timestamp today) {
        Timestamp result[] = new Timestamp[2];

        Calendar c = Calendar.getInstance();

        c.setTime(today);
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

        CommonLog.debug(String.valueOf(result[0]));
        CommonLog.debug(String.valueOf(result[1]));

        return result;
    }

    public static Timestamp[] getLastMonthBeginEnd(Timestamp today) {
        Timestamp result[] = new Timestamp[2];

        Calendar c = Calendar.getInstance();

        c.setTime(today);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, -1);
        result[0] = new Timestamp(c.getTime().getTime());


        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND , 59);
        c.set(Calendar.MILLISECOND , 999);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DAY_OF_MONTH, -1);
        result[1] = new Timestamp(c.getTime().getTime());

        CommonLog.debug(String.valueOf(result[0]));
        CommonLog.debug(String.valueOf(result[1]));
        return result;
    }

    public static Timestamp[] getWeekBeginEnd(Timestamp today) {
        Timestamp result[] = new Timestamp[2];

        Calendar c = Calendar.getInstance();

        c.setTime(today);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);

        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        switch(weekDay) {
            case Calendar.SUNDAY : break;
            case Calendar.MONDAY : c.add(Calendar.DAY_OF_MONTH, -1); break;
            case Calendar.TUESDAY : c.add(Calendar.DAY_OF_MONTH, -2); break;
            case Calendar.WEDNESDAY : c.add(Calendar.DAY_OF_MONTH, -3); break;
            case Calendar.THURSDAY : c.add(Calendar.DAY_OF_MONTH, -4); break;
            case Calendar.FRIDAY : c.add(Calendar.DAY_OF_MONTH, -5); break;
            case Calendar.SATURDAY : c.add(Calendar.DAY_OF_MONTH, -6); break;
        }

        //c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = new Timestamp(c.getTime().getTime());


        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND , 59);
        c.set(Calendar.MILLISECOND , 999);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.DAY_OF_MONTH, 6);
        result[1] = new Timestamp(c.getTime().getTime());

        CommonLog.debug(String.valueOf(result[0]));
        CommonLog.debug(String.valueOf(result[1]));
        return result;
    }

    public static Timestamp[] getLastWeekBeginEnd(Timestamp today) {
        Timestamp result[] = new Timestamp[2];

        Calendar c = Calendar.getInstance();

        c.setTime(today);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.DAY_OF_MONTH, -7);

        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        switch(weekDay) {
            case Calendar.SUNDAY : break;
            case Calendar.MONDAY : c.add(Calendar.DAY_OF_MONTH, -1); break;
            case Calendar.TUESDAY : c.add(Calendar.DAY_OF_MONTH, -2); break;
            case Calendar.WEDNESDAY : c.add(Calendar.DAY_OF_MONTH, -3); break;
            case Calendar.THURSDAY : c.add(Calendar.DAY_OF_MONTH, -4); break;
            case Calendar.FRIDAY : c.add(Calendar.DAY_OF_MONTH, -5); break;
            case Calendar.SATURDAY : c.add(Calendar.DAY_OF_MONTH, -6); break;
        }

        //c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = new Timestamp(c.getTime().getTime());


        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND , 59);
        c.set(Calendar.MILLISECOND , 999);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.DAY_OF_MONTH, 6);
        result[1] = new Timestamp(c.getTime().getTime());

        CommonLog.debug(String.valueOf(result[0]));
        CommonLog.debug(String.valueOf(result[1]));

        return result;
    }

    public static Timestamp[] getYesterdayBeginEnd(Timestamp today) {
        Timestamp result[] = new Timestamp[2];

        Calendar c = Calendar.getInstance();

        c.setTime(today);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.DAY_OF_MONTH, -1);

        //c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = new Timestamp(c.getTime().getTime());


        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND , 59);
        c.set(Calendar.MILLISECOND , 999);
        c.set(Calendar.AM_PM, Calendar.AM);
        result[1] = new Timestamp(c.getTime().getTime());

        CommonLog.debug(String.valueOf(result[0]));
        CommonLog.debug(String.valueOf(result[1]));

        return result;
    }

    public static Timestamp[] getTodayBeginEnd(Timestamp today) {
        Timestamp result[] = new Timestamp[2];

        Calendar c = Calendar.getInstance();

        c.setTime(today);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);

        //c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = new Timestamp(c.getTime().getTime());


        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND , 59);
        c.set(Calendar.MILLISECOND , 999);
        c.set(Calendar.AM_PM, Calendar.AM);
        result[1] = new Timestamp(c.getTime().getTime());

        CommonLog.debug(String.valueOf(result[0]));
        CommonLog.debug(String.valueOf(result[1]));

        return result;
    }

    public static Timestamp[] getLastNDaysBeginEnd(Timestamp today, int NDays) {
        Timestamp result[] = new Timestamp[2];

        Calendar c = Calendar.getInstance();

        c.setTime(today);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND , 0);
        c.set(Calendar.MILLISECOND , 0);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.DAY_OF_MONTH, 1 - NDays);
        //c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        result[0] = new Timestamp(c.getTime().getTime());


        c.set(Calendar.HOUR, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND , 59);
        c.set(Calendar.MILLISECOND , 999);
        c.set(Calendar.AM_PM, Calendar.AM);
        c.add(Calendar.DAY_OF_MONTH, NDays - 1);
        result[1] = new Timestamp(c.getTime().getTime());

        CommonLog.debug(String.valueOf(result[0]));
        CommonLog.debug(String.valueOf(result[1]));

        return result;
    }

    /**
    Remove all the tags of the input String.
    e.g. input "<B>Data</B>" will return "Data"
    Note that the data of the input String itself must not contains '<' or '>'
    @param in input String
    @return a String with all the tags of the input String removed
    */
    public static String removeTag(String in) {
        if(in==null || in.length()==0) {
            return in;
        } else {
            String out = new String(in);
            int beginIndex = 0;
            int endIndex = 0;
            do {
                StringBuffer outBuf = new StringBuffer(out);
                beginIndex = out.indexOf('<');
                endIndex = out.indexOf('>');
                if(beginIndex >=0 && endIndex >=0) {
                    out = new String(outBuf.delete(beginIndex, endIndex + 1));
                }
            } while (beginIndex >= 0 && endIndex >= 0);
            return out;
        }
    }

    /**
    Unescape the escaped HTML characters
    The function will convert "&quto;"  --> a double quote
                              "&lt;"    --> "<"
                              "&gt;"    --> "<"
                              "amp;"    --> "&"
    @param in input String
    @return a String with all escaped HTML characters of the input String unescaped
    */
    public static String unescHTML(String in) {
        if(in==null || in.length()==0) {
            return in;
        } else {
            in = perl.substitute("s#&nbsp;# #ig", in);
            in = perl.substitute("s#&quot;#\"#ig", in);
            in = perl.substitute("s#&lt;#<#ig", in);
            in = perl.substitute("s#&gt;#>#ig", in);
            in = perl.substitute("s#&amp;#&#ig", in);
            return in;
        }
    }

	/**
	 * Checks the file encoding is equal to the specified encoding.
	 * @param file a File object
	 * @param enc encoding to be checked
	 * @return true if encoding correct otherwise false
	 * @throws IOException Throwns if failure to access the file
	 */
	public static boolean isValidEncodedFile(File file, String enc)
		throws IOException {
			boolean flag = false;
			FileInputStream fis = new FileInputStream(file);
			if( ENC_UTF.equalsIgnoreCase(enc) ) {
				if( fis.read()== 239 && fis.read() == 187 && fis.read() == 191 ) {
					flag = true;
				}
			} else if( ENC_UNICODE_LITTLE.equalsIgnoreCase(enc) ) {
				if( fis.read() == 255 && fis.read() == 254 ) {
					flag = true;
				}
			}
			fis.close();
			return flag;
		}

        /**
        Set the content type to the HttpServletResponse
        @param contentType the content type string, e.g. "application/vnd.ms-excel"
        @param response HttpServletResponse object
        @param wizbini WizbiniLoader object
        */
		public static void setContentType(String contentType, HttpServletResponse response, WizbiniLoader wizbini) {
            //try to set the content type with the charset used in setupadv.xml
            //if an IllegalStateException is thrown, try to set the charset with the one got from response
            //we need to do it in this way because:
            //in weblogic, if the charset is first set to "UTF-8"
            //the value returned by response.getCharacterEncoding() is "UTF8"
            //if we set the charset again with "UTF-8" (possibly got from wizbini.cfgSysSetupadv.getEncoding())
            //weblogic 7.0 will throw IllegalStateException: cannot change the charset from "UTF8" to "UTF-8"
            //we need to set the charset with "UTF8" (from response.getCharacterEncoding())
            //however, in weblogic 6.1, if we set the charset to "UTF8" (from response.getCharacterEncoding())
            //it will throw IllegalStateException: cannot change charset from "UTF-8" to "UTF8"
            //that's why use a try block to set the ContentType
            try {
                response.setContentType(contentType + "; charset=" + wizbini.cfgSysSetupadv.getEncoding());
            } catch (IllegalStateException e) {
                response.setContentType(contentType + "; charset=" + response.getCharacterEncoding());
            }
            return;
		}

	/**
	 * escape all character reference of DOS line break to character reference of xml line break
	 * 2004.03.23 kawai
	 */
    public static String escCrLfForXml(String in) {
        String result = null;
        if (in != null) {
            result = perl.substitute("s#&\\#13;&\\#10;#&\\#10;#ig", in);
        }
        return result;
    }

    public static boolean isNumeric(String s){
        char[] sChars = s.toCharArray();
        for (int i = 0; i < sChars.length; i++){
            if (!Character.isDigit(sChars[i]))
                return false;
        }
        return true;
    }

    /**
     * return a new FileInputStream which the file expected is of UTF-8 encoding
     * check if the input stream contains a UTF8 byte order mark (BOM). skip it if found.
     */
    public static FileInputStream openUTF8FileStream(File inFile) throws IOException {
        FileInputStream inStream = new FileInputStream(inFile);

        boolean foundBOM = true;
        byte[] inBOM = new byte[BOM_UTF8.length];
        if (inStream.read(inBOM) == BOM_UTF8.length) {
            for (int i = 0; i < BOM_UTF8.length; i++) {
                if (inBOM[i] != BOM_UTF8[i]) {
                    foundBOM = false;
                    break;
                }
            }
        }
        else {
            foundBOM = false;
        }
        if (!foundBOM) {
            inStream.close();
            inStream = new FileInputStream(inFile);
        }
        return inStream;
    }

    /**
    Escape special characters in "\  %"
    */
    public static String esc4SQL(String in)
        {
            if(in==null || in.length()==0)
                return in;
            in = perl.substitute("s#\\\\#\\\\#ig", in);
            in = perl.substitute("s#%#\\%#ig", in);
            in = perl.substitute("s#_#\\_#ig", in);
            return in;
        }

	public static String hash(String inStr) {
        String result = null;
        try {
            Security.addProvider((Provider)Class.forName(ProviderSunJCE).newInstance());

            Key macKeyObj = new SecretKeySpec(WzbMacKey.getBytes(ENC_UTF), WzbMacAlg);
            Mac macObj = Mac.getInstance(WzbMacAlg);
            macObj.init(macKeyObj);
            byte[] resBytes = macObj.doFinal(inStr.getBytes(ENC_UTF));

            BASE64Encoder encoder = new BASE64Encoder();
            result = new String(encoder.encode(resBytes));
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
        }
        return result;
    }

    public static boolean checkHmac(String plainText, String encyptedText) {
		boolean consistent = false;
		String plainTextHash = hash(plainText);
		if (plainTextHash.equals(encyptedText)) {
			consistent = true;
		}
		return consistent;
	}
    public static final String USED_XSL_LST = "used_xsl_lst";
    public static final String USED_XSL_SIZE = "used_xsl_size";
    public static void setUsedXsl2Sess(HttpServletRequest request, HttpSession sess, boolean bMultiPart, MultipartRequest multi) {
    	if(sess != null){
	    	int lst_size = 0;
	    	Integer used_xsl_size = (Integer)sess.getAttribute(USED_XSL_SIZE);
	    	if(used_xsl_size != null){
	    		lst_size = used_xsl_size.intValue();
	    	}
	    	String stylesheet = (bMultiPart) ? multi.getParameter("stylesheet") : request.getParameter("stylesheet");
	    	String count = (bMultiPart) ? multi.getParameter("used_xsl_size") : request.getParameter("used_xsl_size");
	    	if(count != null && count.length() > 0){
	    		try{
	    			lst_size = Integer.parseInt(count);
	    		} catch (Exception e){
	    	        CommonLog.error(e.getMessage(),e);
	    	        CommonLog.error("error error used_xsl_count " + count);
	    		}
	    	}
    		Vector vc = (Vector)sess.getAttribute(USED_XSL_LST);
    		if(lst_size <= 0){
    			if(vc != null)
    				sess.removeAttribute(USED_XSL_LST);
    		}
    		else if(vc == null && (stylesheet != null && stylesheet.trim().length() > 0)){
    			vc = new Vector();
    			vc.addElement(stylesheet);
    			sess.setAttribute(USED_XSL_LST, vc);
    		}
    		else if(vc != null && (stylesheet != null && stylesheet.trim().length() > 0)){
    			if(vc.contains(stylesheet)){
    				vc.remove(stylesheet);
    			}
				vc.add(0,stylesheet);
				sess.setAttribute(USED_XSL_LST, vc);
    		}
    		//cmd "get_used_xsl_lst" have no stylesheet, use to change the used_xsl_size
    		else if (vc != null){
    			while(vc.size() > lst_size){
					vc.remove(vc.size()-1);
				}
				sess.setAttribute(USED_XSL_LST, vc);
    		}

    		sess.setAttribute(USED_XSL_SIZE, new Integer(lst_size));
    	}
	}
    public static void removeUsedXslFromSess(HttpSession sess, String stylesheet) {
    	if(sess != null){
    		Vector vc = (Vector)sess.getAttribute(USED_XSL_LST);
    		if(vc != null && (stylesheet != null && stylesheet.trim().length() > 0)){
    			if(vc.contains(stylesheet)){
    				vc.remove(stylesheet);
    				sess.setAttribute(USED_XSL_LST, vc);
    			}
    		}
    	}
	}

    public static Vector allKeyForList(Map table) {
    	Vector keyVec = new Vector();
    	if (table != null) {
    		Iterator keys = table.keySet().iterator();
        	while (keys.hasNext()) {
        		keyVec.add(keys.next());
            }
    	}
    	return keyVec;
    }

    private static final Pattern[] EscJsLabelPat = {Pattern.compile("\\\\"), Pattern.compile("\""), Pattern.compile("\'")};
	private static final String[] EscJsLabelRpl = {"\\\\\\\\", "\\\\\"", "\\\\'"};
	private static final int EscJsLabelPatLen = EscJsLabelPat.length;
	/**
	 * escape labels to be used in js
	 * notes:
	 * 1. backslash must be the first pattern to replace
	 * 2. in java code, two-backslash represents one-backslash;
	 *    in replacement string, two-backslash represents one-backslash;
	 *    as a result, four-backslash in java code represents one-backslash in result string
	 */
	public static String escJsLabel(String in) {
		for (int i = 0; i < EscJsLabelPatLen; i++) {
			in = EscJsLabelPat[i].matcher(in).replaceAll(EscJsLabelRpl[i]);
		}
		return in;
	}

	public static String getPageName(HttpServletRequest request) {
		String pageName = null;
		Enumeration referer = request.getHeaders("referer");
		String refererUrl = null;
		if(referer.hasMoreElements()) {
			refererUrl = (String)referer.nextElement();
		}
		if(refererUrl != null) {
			String[] splited = Pattern.compile(".html").split(refererUrl);
			if(splited != null && splited.length > 0) {
				String val = splited[0];
				pageName = val.substring(val.lastIndexOf("/") + 1);
			}
		}
		return pageName;
	}

	public static String getContentFromHtmlStr(String htmlStr) {
		String resultStr = null;

		if(htmlStr == null || "".equals(htmlStr)) {
			resultStr = htmlStr;
		} else {
			resultStr = htmlStr.replaceAll("<[^<]+>", "");
			resultStr = resultStr.replaceAll("^\\s", "");
			resultStr = unescHTML(resultStr);
		}

		return resultStr;
	}

	public static void sendRedirect(HttpServletResponse response, String location) {
		response.addHeader(cwUtils.RESPONSE_HEADER_LOCATION, location);
	}

	/**
	 * 获取某个集合中指定数目的元素集
	 * @param vec 目标集合
	 * @param num 指定的数目
	 * @return 集合中指定数目的元素集
	 */
	public static Vector getSpecifiedNumOfVec(Vector vec, int num) {

		if (vec != null && vec.size() > 0) {
			Vector tempVec = new Vector();

			int count = 1;
			for (Iterator iter = vec.iterator(); iter.hasNext();) {

				if (count > num) {
					break;
				}

				tempVec.addElement(iter.next());

				count++;
			}

			return tempVec;
		} else {
			return vec;
		}

	}
	/**
	 * 图片在规定长、宽范围内做比例缩小(由于jdk的限制，目前不支持gif格式的图片)
	 * 格式：jpg、png
	 * @param fileName
	 * @param width
	 * @param height
	 * @throws cwException
	 * @throws IOException
	 */
	public static void resizeImage(String fileName, int width, int height) throws cwException, IOException{
//		BufferedImage srcImage;
//		File saveFile = new File(fileName);
//		File fromFile = new File(fileName);
//		if(fromFile.exists() && fromFile.isFile()){
//			String file_ext = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
//			if(!TYPE_IMAGE_GIF.equalsIgnoreCase(file_ext)){
//				srcImage = ImageIO.read(fromFile);
//				if(srcImage == null ) {
//					throw new cwException("this file is not a image");
//				}
//				if ((width > 0 && height > 0) && !(srcImage.getWidth()<width && srcImage.getHeight() < height)) {
//					srcImage = resize(srcImage, width, height);
//				}
//				ImageIO.write(srcImage, file_ext, saveFile);
//			}
//		}
	}
	/**
	 * 缩放操作
	 * @param source
	 * @param targetW
	 * @param targetH
	 * @return
	 */
	public static BufferedImage resize(BufferedImage source, int targetW,
			int targetH) {
		// targetW，targetH分别表示目标长和宽
		int type = source.getType();
		BufferedImage target = null;
		double sx = (double) targetW / source.getWidth();
		double sy = (double) targetH / source.getHeight();
		// 这里想实现在targetW，targetH范围内实现等比缩放。
		if (sx > sy) {
			sx = sy;
			targetW = (int) (sx * source.getWidth());
		} else {
			sy = sx;
			targetH = (int) (sy * source.getHeight());
		}
		if (type == BufferedImage.TYPE_CUSTOM) { // handmade
			ColorModel cm = source.getColorModel();
			WritableRaster raster = cm.createCompatibleWritableRaster(targetW,
					targetH);
			boolean alphaPremultiplied = cm.isAlphaPremultiplied();
			target = new BufferedImage(cm, raster, alphaPremultiplied, null);
		} else{
			target = new BufferedImage(targetW, targetH, type);
		}
		Graphics2D g = target.createGraphics();
		// smoother than exlax:
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
		g.dispose();
		return target;
	}

	public static int dateDiff(Timestamp startTime,Timestamp endTime) {
		int duration = 0;
    	if(startTime != null && endTime != null) {
    		long d1 = startTime.getTime();
    		long d2 = endTime.getTime();
    		double diff = (double)(d2-d1)/1000/3600/24;
    		duration = (int) Math.floor(diff);
    	}
    	return duration;
    }

    public static Timestamp dateAdd(Timestamp date, int amount) {
		Timestamp addDate = null;
    	if(date != null) {
			Calendar cal=Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, amount);
			addDate = new Timestamp(cal.getTime().getTime());
		}

    	return addDate;

    }

	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DEFAULT_DATETIME_FORMAT_ymdhms = "yyyy-MM-dd HH:mm:ss";
	public static Timestamp parse(String time) throws ParseException {
		return parse(time, DEFAULT_DATETIME_FORMAT);
	}

	public static Timestamp parse(String time, String patten) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(patten);
		Timestamp value = null;
		try {
			value = new Timestamp(format.parse(time).getTime());
		} catch (Exception e) {
			value = new Timestamp(format.parse(time + ".000").getTime());
		}
		return value;
	}

	public static String format(Timestamp time) {
		return format(time, DEFAULT_DATETIME_FORMAT);
	}
	
	public static String format2simple(Timestamp time) {
		return format(time, DEFAULT_DATE_FORMAT);
	}
	
	public static String format2ymdhms(Timestamp time) {
		return format(time, DEFAULT_DATETIME_FORMAT_ymdhms);
	}
	
	public static String format2HHMM(Timestamp time) {
		String hh = String.valueOf(time.getHours());
		String mm = String.valueOf(time.getMinutes());
		if(mm.length()==1){
			mm = "0"+mm;
		}
		return hh+":"+mm;
	}

	public static String format(Timestamp time, String patten) {
		SimpleDateFormat format = new SimpleDateFormat(patten);
		return format.format(time);
	}

	/**
	 * 按指定的标准对double类型的变量进行格式化(小数部分四舍五入)
	 *
	 * @param number 需要转换的数值
	 * @param scale 小数部份最大保留位数
	 * @return 格式化后的数值字符串
	 */
	public static String formatNumber(double number, int scale) {
		BigDecimal bd = new BigDecimal(number);
		if (scale < 0) {
			scale = 0;
		}
		return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	public static String formatNumber(float number, int scale) {
		BigDecimal bd = new BigDecimal(number);
		if (scale < 0) {
			scale = 0;
		}
		return bd.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static String transPathSeperator4Linux(String path) {
	    if (path != null && path.length() > 0) {
	        return path.replaceAll("\\\\", "\\" + SLASH);
	    } else {
	        return "";
	    }
	}
    public static String replaceSlashToHttp(String src) {
    	if (src == null) {
    		return "";
    	}
    	return src.replaceAll("\\\\", "/");
    }

    /**
	 * 将unicode编码的汉字转换成中文
	 */
    public static String decodeUnicode(String str) {
		String[] tmp = str.split(";&#|&#|;");
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < tmp.length; i++) {
			if (tmp[i].matches("\\d{5}")) {
				sb.append((char) Integer.parseInt(tmp[i]));
			} else {
				sb.append(tmp[i]);
			}
		}
		return sb.toString(); 
	}

	public static boolean notEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static String getFileURL(String url) {
		if (isEmpty(url)) {
			return "";
		}
		String result = null;
		if (url.toLowerCase().startsWith("http")) {
			result = url;
			if (!result.endsWith("/")) {
				result += "/";
			}
		} else {
			result = ContextPath.getContextPath() + "/" + url + "/";
		}
		return result;
	}

	public static String getFileAbsoluteURL(String url) {
		if (isEmpty(url)) {
			return "";
		}
		String result = null;
		if (url.toLowerCase().startsWith("http")) {
			result = url;
			if (!result.endsWith("/")) {
				result += "/";
			}
		} else {
			result  = ContextPath.getContextPath() +"/" + url + "/";
		}
		return result;
	}


	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String path, String name) {
		Cookie[] cookies = request.getCookies();
		if (path == null || path.length() < 1) {
			path = "/";
		}
		try {

			for (int i = 0; i < cookies.length; i++) {
				CommonLog.debug(cookies[i].getName() + ":" + cookies[i].getValue());
				if (cookies[i].getName() != null && cookies[i].getName().equalsIgnoreCase(name)) {
					Cookie cookie = new Cookie(cookies[i].getName(), null);
					cookie.setMaxAge(0);
					cookie.setPath(path);
					response.addCookie(cookie);
				}
			}

		} catch (Exception ex) {
		}
	}	 

	 /**
     * 生成设置长度的随机码
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
    	String base = "abcdefghijklmnopqrstuvwxyz!@$0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();   
     }
    public static String getUrlByisPhishing(String url){
//    	boolean isPhishing = false;
//    	if(url != null){
//    		if(url.startsWith("https") || url.startsWith("http") || url.startsWith("www")){
//    			if(!url.startsWith(qdbAction.domain)){
//    				isPhishing = true;
//    			}
//    		}
//    	}
//    	if(isPhishing){
//    		url = "";
//    	}
    	return url;
    }
    
    /**
     * 往前面补零
     * @param num
     * @param maxlength
     * @return
     */
    public static String putAssignZero(long num,int maxlength){
    	String str = "";
    	int length = maxlength - String.valueOf(num).length();
    	for(int i = 0; i<length; i++) {
    		str += "0";
    	}
    	return str+num;
    }
    
}

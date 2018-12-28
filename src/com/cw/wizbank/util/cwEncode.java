package com.cw.wizbank.util;

import java.io.*;
import java.util.*;


/**
A utility class to store all common APIs
*/
public class cwEncode
{

      
    public cwEncode () {;}
  
    
    /**
    To generate and decode system encoded value  <BR>
    e.g. Expiry Date, Maxium number of users allowed of a site etc
    */
    
    private static final int magic1 = 61;
    private static final int magic2 = 30;
    private static final int magic3 = 46;
    private static final int magic4 = 27;
    private static final int magic5 = 343;

    public static String encodeKey(String value) {

        int intTemp = 0;
        int checkDigit = 0;
        String strTemp = null;
        String result = "";
        for (int i=0; i<value.length(); i++) {
            intTemp = ((i+1) * ((i+1) + magic1) + (i+1) * ((i+1) + magic2)) % magic3;
            intTemp += value.charAt(i);
            if (intTemp < 10) {
                strTemp = "00" + Integer.toString(intTemp);
            }
            else if (intTemp < 100) {
                strTemp = "0" + Integer.toString(intTemp);
            }
            else {
                strTemp = Integer.toString(intTemp);
            }
                result = result.concat(strTemp);
            checkDigit += intTemp;
        }
        checkDigit = (checkDigit * magic4) % magic5;
        if (checkDigit < 10) {
            result = result.concat("00");
            result = result.concat(Integer.toString(checkDigit));
        }
        else if (checkDigit < 100) {
            result = result.concat("0");
            result = result.concat(Integer.toString(checkDigit));
        }
        else {
            result = result.concat(Integer.toString(checkDigit));
        }
         
        return result;
    }
 
    public static boolean checkValidKey(String value) {
        
        if (value == null)  {
            return false;
        }

        int intTemp = 0;
        int index = 0;
        int checkDigit = 0;
        String strTemp = null;
        String result = "";
         
        if ((value.length() % 3) != 0) {
            return false;
        }
        else {
            for (int i=0; i<value.length()-3; i+=3) {
                checkDigit += Integer.parseInt(value.substring(i, i+3));
            }
            checkDigit = (checkDigit * magic4) % magic5;
            if (checkDigit == Integer.parseInt(value.substring(value.length()-3))) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static String decodeKey(String value) {
         
        int intTemp = 0;
        int index = 0;
        int checkDigit = 0;
        String strTemp = null;
        String result = "";
        for (int i=0; i<value.length()-3; i+=3) {
            index++;
            intTemp = (index * (index + magic1) + index * (index + magic2)) % magic3;
            intTemp = Integer.parseInt(value.substring(i, i+3)) - intTemp;
            result = result.concat(String.valueOf((char)intTemp));
        }
     
        return result;
    }
     
    /**
     * The following is an almost identical clone of java.net.URLEncoder.
     * since the orignal <code>encode<code> method assumes default character
     * encoding when converting the string into bytes, it is not useful when
     * the string is not to be decoded by the default character encoding. as
     * a result, the method is modified to accept a desired encoding.
     * (2005-01-05 kawai)
     */
    static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');

    /* The list of characters that are not encoded have been determined by
       referencing O'Reilly's "HTML: The Definitive Guide" (page 164). */

    static {
        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }
        dontNeedEncoding.set(' '); /* encoding a space to a + is done in the encode() method */
        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
    }

    /**
     * Translates a string into <code>x-www-form-urlencoded</code> format.
     *
     * @param   s   <code>String</code> to be translated.
     * @param   inEncoding   the encoding for converting the string into bytes.
     * @return  the translated <code>String</code>.
     */
    public static String encodeURL(String s, String inEncoding) throws UnsupportedEncodingException {
        int maxBytesPerChar = 10;
        StringBuffer out = new StringBuffer(s.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
        OutputStreamWriter writer = new OutputStreamWriter(buf, inEncoding);

        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);
            if (dontNeedEncoding.get(c)) {
                if (c == ' ') {
                    c = '+';
                }
                out.append((char) c);
            } else {
                // convert to external encoding before hex conversion
                try {
                    writer.write(c);
                    writer.flush();
                } catch (IOException e) {
                    buf.reset();
                    continue;
                }
                byte[] ba = buf.toByteArray();
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
                    // converting to use uppercase letter as part of
                    // the hex value if ch is a letter.
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                    ch = Character.forDigit(ba[j] & 0xF, 16);
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }
                    out.append(ch);
                }
                buf.reset();
            }
        }

        return out.toString();
    }
}


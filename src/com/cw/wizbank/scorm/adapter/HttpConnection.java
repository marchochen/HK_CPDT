package com.cw.wizbank.scorm.adapter;

import java.io.*;
import java.net.*;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.cwn.wizbank.utils.CommonLog;

//import com.ms.security.*;

public class HttpConnection {
    static final String NEWL = System.getProperty("line.separator");
    
    URL target = null;
    URLConnection targetCon = null;
    String encoding = null;
    int stringBufferSize = 32768;
    StringBuffer result;

    public HttpConnection(String strURL) {
        initConnection(strURL, null);
    }

    public HttpConnection(String strURL, String enc) {
        initConnection(strURL, enc);
    }

    public void initConnection(String strURL, String enc) {
        try {            
        	CommonLog.debug("connecting to " + strURL + "...");
            target = new URL(strURL);
            targetCon = target.openConnection();
            targetCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            targetCon.setRequestProperty("method", "post");
            targetCon.setDoOutput( true );            
            if (enc != null) {
                encoding = enc;
            }
        } catch(Exception e) {
        	CommonLog.error("error in creating the URL object");
        	CommonLog.error(e.getMessage(),e);
        }
    }
    
    public String doConnect(String query) {
        try {
            // error in creating the URL connection
            if (targetCon == null) {
                return null;
            }
            
            DataOutputStream dos = (DataOutputStream)AccessController.doPrivileged(new PrivilegedAction() {public Object run() {
                try{
                    return new DataOutputStream(targetCon.getOutputStream());
                }catch(Exception e){
                    CommonLog.error(e.getMessage(),e);
                    return null;
                }
            };});
         //   DataOutputStream dos = new DataOutputStream(targetCon.getOutputStream() );
            OutputStreamWriter output = null;
            if (encoding != null) {
                output = new OutputStreamWriter(dos, encoding);
            }
            else {
                output = new OutputStreamWriter(dos, "GB2312");
            }
            output.write(query);
            output.flush();
            output.close();
            dos.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(targetCon.getInputStream()));
            String inputLine = "";

            CommonLog.debug("start writing...");
            result = new StringBuffer(stringBufferSize);
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.length() == 0) {
                    continue;
                }
                else if (inputLine.trim().length() == 0) {
                    continue;
                }
                else {
                    result.append(inputLine.trim() + NEWL);
                }
            }
            CommonLog.debug("result:" + result.toString());
            CommonLog.debug("finish writing...");
            in.close();
            
            int headerCnt = 1;
            String header = targetCon.getHeaderFieldKey(headerCnt);
            String hvalue = targetCon.getHeaderField(headerCnt);
            while (header != null) {
            	CommonLog.debug(header + ": " + hvalue);
                headerCnt++;
                header = targetCon.getHeaderFieldKey(headerCnt);
                hvalue = targetCon.getHeaderField(headerCnt);
            }
            
            return result.toString();

        } catch(Exception e) {
        	CommonLog.error("Error in connecting to server");
        	CommonLog.error(e.getMessage(),e);
            return null;
        }
    }
    
    public String doGetHeaderField(String fieldName) {
        return targetCon.getHeaderField(fieldName);
    }
    
    public void doClose() {
    }

}


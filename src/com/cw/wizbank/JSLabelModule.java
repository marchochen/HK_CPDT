package com.cw.wizbank;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.utils.CommonLog;

/**
 * handles requests of javascript files which need
 * to be served in different output encodings(unicode vs. original encoding).
 * it is very similar to the work of a simple file servlet
 * (2003-10-08 kawai)
 */
public class JSLabelModule extends HttpServlet {
    private static final String JS_PATH_PREFIX     = "/js/";
    private static final String URI_PATH_SEPARATOR = "/";
    private static final String HEADER_MOD_SINCE   = "If-Modified-Since";
    private static final String HEADER_USR_AGENT   = "User-Agent";
    private static final String HEADER_LAST_MOD    = "Last-Modified";
    private static final String HEADER_CTN_LEN     = "Content-Length";
    private static final String HEADER_CTN_TYPE_JS = "text/javascript; charset=";
    private static final String NON_UNICODE_OS     = "windows 98";
    private static final String NON_UNICODE_IE     = "msie 5.0";
    private static final long MOD_TIME_THRESHOLD   = 1000;
    private static final int MAP_LANG_IDX          = 0;
    private static final int MAP_ENC_IDX           = 1;
    private static final int MAP_LANG_IDX_DEFAULT  = 0;
    private static final String[][] LANG_ENC_MAP = { {"en","ISO-8859-1"}
                                                    ,{"gb","GB2312"}
                                                    ,{"ch","Big5"}
                                                   };
    
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqFile = request.getRequestURI();
        // get the actual uri of the requested file in case there is any other servlet mappings
        String reqFileActual = reqFile.substring(reqFile.lastIndexOf(JS_PATH_PREFIX));
        // get the absolute path(in the server) of the requested file
        File serverFile = new File(wizbini.getWebDocRoot() + reqFileActual);
        
        if (serverFile.exists()) {
            // serve the file only when it is the client's first time access
            // or client's copy is older than the server's
            long serverFileModified = serverFile.lastModified();
            long clientFileModified = request.getDateHeader(HEADER_MOD_SINCE);
            // since timestamp of the client's file may have been rounded to
            // the nearest second, a threshold has to be added
            if (clientFileModified == -1 || (clientFileModified + MOD_TIME_THRESHOLD) < serverFileModified) {
                // get the language of the requested file through the uri
                String reqFileLang = reqFileActual.substring(JS_PATH_PREFIX.length(), reqFileActual.lastIndexOf(URI_PATH_SEPARATOR));
                // if the language is not known, use the default encoding to read the file
                int reqFileMapIdx = MAP_LANG_IDX_DEFAULT;
                for (int i = 0; i < LANG_ENC_MAP.length; i++) {
                    if (reqFileLang.equals(LANG_ENC_MAP[i][MAP_LANG_IDX])) {
                        reqFileMapIdx = i;
                        break;
                    }
                }
                
                String outFileContent;
                String outFileEnc;
                int outFileContentLen;
                
                // read in the requested file
                // not using method readLine so as to preserve any original line breaks
                int serverFileLen = (int)serverFile.length();
                byte[] tempByteBuf = new byte[serverFileLen];
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(serverFile));
                in.read(tempByteBuf, 0, serverFileLen);
                in.close();
                outFileContent = new String(tempByteBuf, LANG_ENC_MAP[reqFileMapIdx][MAP_ENC_IDX]);
                
                // if the OS of the client does not support unicode, serve the
                // file with its original encoding
                // !!!core part of this function!!!
                String clientAgent = request.getHeader(HEADER_USR_AGENT);
                if (clientAgent.toLowerCase().indexOf(NON_UNICODE_OS) != -1 && clientAgent.toLowerCase().indexOf(NON_UNICODE_IE) != -1 ) {
                    outFileEnc = LANG_ENC_MAP[reqFileMapIdx][MAP_ENC_IDX];
                    outFileContentLen = serverFileLen;
                } else {
                    outFileEnc = wizbini.cfgSysSetupadv.getEncoding();
                    outFileContentLen = outFileContent.getBytes(outFileEnc).length;
                }
                
                response.setDateHeader(HEADER_LAST_MOD, serverFileModified);
                response.setIntHeader(HEADER_CTN_LEN, outFileContentLen);
                response.setContentType(HEADER_CTN_TYPE_JS + outFileEnc);
                PrintWriter out = response.getWriter();
                out.print(outFileContent);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    // using xml in place of wizb.ini for system parameters
    private static WizbiniLoader wizbini = null;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
        	CommonLog.info("JSLabelModule.init() START...");
            wizbini = WizbiniLoader.getInstance(config);
            CommonLog.info("JSLabelModule.init() END");
        } catch (cwException e) {
        	CommonLog.error("init() exception :" + e.getMessage());
            throw new ServletException(e.getMessage());
        }
    }
}

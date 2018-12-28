package com.cw.wizbank.personalization;

import java.io.*;
import java.sql.SQLException;

import com.cw.wizbank.*;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;


public class BookmarkModule extends ServletModule
{
    
    public BookmarkModule() {;}
    
    public void process() throws SQLException, cwException, IOException {
        
        if (prof ==null)
        	CommonLog.info("login profile is null.");
        else 
        	CommonLog.info("loginProfile  > usr_id :" + prof.usr_id);
        // get output stream for normal content to client
        PrintWriter out = response.getWriter();
        
        BookmarkReqParam urlp = new BookmarkReqParam(request, clientEnc, static_env.ENCODING);

        // service processing starts here
        try {
//            String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);

            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
//                response.sendRedirect(url_relogin);
            
            }else if (urlp.cmd == null) {
                throw new cwException("invalid command");
            }else if (urlp.cmd.equalsIgnoreCase("INS_BOO")) {
                urlp.insBoo();
                Bookmark bmk = new Bookmark();
                bmk.insBookmark(con, prof, urlp.dbBookmark);
                con.commit();
                response.sendRedirect(urlp.url_success);

            }else if (urlp.cmd.equalsIgnoreCase("DEL_BOO")) {
                urlp.delBoo();
                Bookmark bmk = new Bookmark();
                bmk.delMultiBookmark(con, prof, urlp.booLst, urlp.dbBookmark);
                con.commit();
                response.sendRedirect(urlp.url_success);

            }else if (urlp.cmd.equalsIgnoreCase("GET_ALL_BOO") || 
                        urlp.cmd.equalsIgnoreCase("GET_ALL_BOO_XML")) {
                urlp.getAll();
                Bookmark bmk = new Bookmark();
                String xml = bmk.bookmarkAsXML(con, prof, urlp.dbBookmark);
                if(urlp.cmd.equalsIgnoreCase("GET_ALL_BOO_XML"))
                    static_env.outputXML(out, xml);
                if(urlp.cmd.equalsIgnoreCase("GET_ALL_BOO"))
                    generalAsHtml(xml, out, urlp.stylesheet);

            }else{
                throw new cwException("invalid command");
            }
            
        }
        /*
        catch (cwSysMessage e) {
             try {
                 con.rollback();
                 msgBox(WBServletModule.MSG_STATUS, con, e, prof, urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
             }
             
        }
        */
        // Test only
        catch (Exception e) {
                 con.rollback();
                out.println("Server error: " + e.getMessage());
        }
      
    }

}
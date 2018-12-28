/*
 * Created on 2006-2-22
 */
package com.cw.wizbank.export;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExportModule extends ServletModule {

    public void process() throws cwException, IOException {
        ExportParam exp = null;
        exp = new ExportParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            exp.setMultiPart(multi);
        }
        
        exp.common();

        PrintWriter out = response.getWriter();

        HttpSession sess = request.getSession(false);
        // if all command need authorized users
        if (sess == null || prof == null) {
            response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            //                response.sendRedirect(url_relogin);
        }
        else {
            if (exp.cmd.equals("export_que")) {
                exp.exportQue();

                ExportControllerCommon exportProgresser = new ExportControllerCommon();
                if (sess.getAttribute(exp.window_name + ExportControllerCommon.CONTROLLER_NAME) == null) {
                    sess.setAttribute(exp.window_name + ExportControllerCommon.CONTROLLER_NAME, exportProgresser);
                }

                ExportQue export = new ExportQue(con, exportProgresser, prof);
                try {
                    export.exportQue(exp, wizbini, exp.que_type);
                }
                catch (Exception e) {
                    exportProgresser.setErrorMsg(e.getMessage());
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
    }
}

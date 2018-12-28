/*
 * Created on 2005-9-21
 *
 */
package com.cw.wizbank.export;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * @author dixson
 *
 */
public class ResExportProgressModule extends ServletModule {
    public void process() throws IOException, cwException, SQLException {
        cwUtils.setContentType("text/xml", response, wizbini);
        PrintWriter out = response.getWriter();
        ExportParam urlp = new ExportParam(request, clientEnc, static_env.ENCODING);
        HttpSession sess = request.getSession(true);

        if (urlp.cmd == null) {
            throw new cwException("Invalid Command");
        }
        else
        if (urlp.cmd.equalsIgnoreCase("get_rpt_pro")) {
            String obj_index = request.getParameter("window_name");
//            System.out.println(obj_index);
            String obj_sess_name = obj_index + ExportControllerCommon.CONTROLLER_NAME;
            ExportControllerCommon exporter = (ExportControllerCommon)sess.getAttribute(obj_sess_name);
            out.println(exporter.getProgress());
        }
        else
        if (urlp.cmd.equalsIgnoreCase("cancel")) {
            String obj_index = request.getParameter("window_name");
//            System.out.println(obj_index);
            String obj_sess_name = obj_index + ExportControllerCommon.CONTROLLER_NAME;
            ExportControllerCommon exporter = (ExportControllerCommon)sess.getAttribute(obj_sess_name);
            exporter.cancel();
            sess.removeAttribute(obj_sess_name);
        }
    }
}

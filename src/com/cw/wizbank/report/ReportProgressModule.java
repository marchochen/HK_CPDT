/*
 * Created on 2005-9-21
 *
 */
package com.cw.wizbank.report;

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
public class ReportProgressModule extends ServletModule {
    public void process() throws IOException, cwException, SQLException {
        cwUtils.setContentType("text/xml", response, wizbini);
        PrintWriter out = response.getWriter();
        ReportReqParam urlp = new ReportReqParam(request, clientEnc, static_env.ENCODING);
        HttpSession sess = request.getSession(true);

        if (urlp.cmd == null) {
            throw new cwException("Invalid Command");
        }
        else
        if (urlp.cmd.equalsIgnoreCase("get_rpt_pro")) {
            String obj_index = request.getParameter("window_name");
            String obj_sess_name = obj_index + LearnerRptExporter.EXPORT_CONTROLLER;
            ExportController rptTest = (ExportController)sess.getAttribute(obj_sess_name);
            if(rptTest != null) {
            	out.println(rptTest.getProgress());
            }
        }
        else
        if (urlp.cmd.equalsIgnoreCase("cancel")) {
            String obj_index = request.getParameter("window_name");
            String obj_sess_name = obj_index + LearnerRptExporter.EXPORT_CONTROLLER;
            ExportController rptTest = (ExportController)sess.getAttribute(obj_sess_name);
            rptTest.cancel();
            sess.removeAttribute(obj_sess_name);
        }
    }
}

package com.cw.wizbank.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.ServletModule;
import com.cwn.wizbank.utils.CommonLog;

public class ModulePrerequisiteModule extends ServletModule {

    private final static String SMSG_UPD_MSG = "GEN003";
    public final static String SMSG_DEL_MSG = "MOD013";

    public void process() throws SQLException, IOException, cwException {
        ModuleRrerequisiteModuleParam urlp = null;

        urlp = new ModuleRrerequisiteModuleParam(request, clientEnc, static_env.ENCODING);
        if (bMultipart) {
            urlp.setMultiPart(multi);
        }

        urlp.common();
        urlp.preModule();
        PrintWriter out = response.getWriter();

        try {
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            } else {
                ModulePrerequisiteManagement modulePre = new ModulePrerequisiteManagement();
                if (urlp.cmd.equals("get") || urlp.cmd.equals("get_xml")) {
                    String xml = null;
                    String metaXML = null;
                    if (urlp.itm_id != 0) {
                        aeItem itm = new aeItem();
                        itm.itm_id = urlp.itm_id;
                        itm.get(con);
                        metaXML = itm.contentInfoAsXML(con);
                        xml = modulePre.getItemModLstAsXML(con, itm);
                    }

                    xml += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
                    String result = formatXML(xml, metaXML, "prerequisite");
                    if (urlp.cmd.equals("get_xml")) {
                        static_env.outputXML(out, result);
                    }
                    else
                    if (urlp.cmd.equals("get")) {
                        generalAsHtml(result, out, urlp.stylesheet);
                    }
                } else if (urlp.cmd.equals("upd")) {
                    modulePre.upd(con, urlp.mod_id_list,
                            urlp.pre_module_id_list,
                            urlp.pre_module_status_list, urlp.itm_id,
                            prof.usr_id);
                    con.commit();
                    msgBox(MSG_STATUS, new cwSysMessage(SMSG_UPD_MSG), urlp.url_success, out);
                }
            }
        } catch (qdbException e) {
            CommonLog.error(e.getMessage(),e);
            con.rollback();
            out.println("QDB error: " + e.getMessage());
            CommonLog.error(e.getMessage(),e);
        } catch (cwSysMessage se) {
            try {
                con.rollback();
                msgBox(ServletModule.MSG_ERROR, se, urlp.url_failure, out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
                CommonLog.error("SQL error: " + sqle.getMessage(),sqle);
            }
        }
    }
}

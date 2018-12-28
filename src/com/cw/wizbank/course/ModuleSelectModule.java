package com.cw.wizbank.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;

public class ModuleSelectModule extends ServletModule {

    public void process() throws SQLException, IOException, cwException {
        ModuleSelectReqParam urlp = new ModuleSelectReqParam(request, clientEnc, static_env.ENCODING);
        if (bMultipart) {
            urlp.setMultiPart(multi);
        }

        urlp.common();
        urlp.selModule();
        PrintWriter out = response.getWriter();

        try {
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            } else {
                ModuleSelect modsel = new ModuleSelect();
                if (urlp.cmd.equalsIgnoreCase("GEN_SEL_MOD_WIN") || urlp.cmd.equalsIgnoreCase("GEN_SEL_MOD_WIN_XML")) {
                    String xml = modsel.getpageXML(con, urlp.is_multiple, urlp.dis_cos_type, urlp.dis_mod_type, urlp.fieldname, urlp.sel_type, urlp.sel_win_title, urlp.width, urlp.course_id);
                   // String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, dob.prof.root_id);            
                    String metaXML = null;
                    String result = formatXML(xml, metaXML, "selmod");
                    if(urlp.cmd.equalsIgnoreCase("GEN_SEL_MOD_WIN_XML")) {
                       static_env.outputXML(out, result);
                    }
                    if(urlp.cmd.equalsIgnoreCase("GEN_SEL_MOD_WIN")) {
                        generalAsHtml(result, out, urlp.stylesheet);
                    }
                    
                } else if (urlp.cmd.equalsIgnoreCase("GET_SEL_MOD")|| urlp.cmd.equalsIgnoreCase("GET_SEL_MOD_XML")) {
                    ModuleSelect sel_mod = new ModuleSelect();
                    String xml = sel_mod.getItmMods(con, urlp.is_multiple, urlp.dis_mod_type, urlp.sel_type, urlp.sel_mod_status, urlp.itm_id, urlp.width) ;
                 
                   // String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, dob.prof.root_id);            
                    String metaXML = null;
                    String result = formatXML(xml, metaXML, "selmod");
                    if(urlp.cmd.equalsIgnoreCase("GET_SEL_MOD_XML")) {
                       static_env.outputXML(out, result);
                    }
                    if(urlp.cmd.equalsIgnoreCase("GET_SEL_MOD")) {
                        generalAsHtml(result, out, urlp.stylesheet);
                    }
                    
                }
            }
        } catch (qdbException e){
            throw new SQLException(); 
        }
    }
}



package com.cw.wizbank.course;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class ModuleSelectXmlModule extends ServletModule {
    public void process() throws IOException, cwException, SQLException {
        cwUtils.setContentType("text/xml", response, wizbini);
        PrintWriter out = response.getWriter();
        ModuleSelectReqParam urlp = new ModuleSelectReqParam(request, clientEnc, static_env.ENCODING);
        HttpSession sess = request.getSession(true);
        if (bMultipart) {
            urlp.setMultiPart(multi);
        }

        urlp.common();
        urlp.selModule();
        ModuleSelect sel_mod = new ModuleSelect();
        if (urlp.cmd == null) {
            throw new cwException("Invalid Command");
        }
        else
        if (urlp.cmd.equalsIgnoreCase("search_cos")) {
            out.println(sel_mod.getCosListXML( con,  urlp.search_type,  urlp.title_code,  urlp.dis_cos_type, prof, urlp.itm_tcr_id, wizbini.cfgTcEnabled));
        }else {
            throw new cwException("Invalid Command");
        }
       
    }
}


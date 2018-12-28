package com.cw.wizbank.personalization;

import java.io.*;
import java.sql.*;

import com.cw.wizbank.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.qdb.dbRegUser;
import com.cwn.wizbank.utils.CommonLog;


public class PsnBiographyModule extends ServletModule
{
    public static final String moduleName = "PsnBiography"; 

    public void process() throws SQLException, IOException, cwException {
        PsnBiographyReqParam urlp = null;

        urlp = new PsnBiographyReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            }else if (urlp.cmd == null) {
                throw new cwException("invalid command");
            }else if (urlp.cmd.equalsIgnoreCase("get_biography") || urlp.cmd.equalsIgnoreCase("get_biography_xml")) {
                urlp.biography();
                PsnBiography psnBiography = new PsnBiography(); 
            
                dbRegUser dbusr = new dbRegUser();
                dbusr.usr_ent_id = urlp.usr_ent_id;
                dbusr.ent_id = urlp.usr_ent_id;
                dbusr.get(con); 
                StringBuffer xml = new StringBuffer();
                xml.append(psnBiography.getBiographyAsXML(con, urlp.usr_ent_id));
                xml.append(dbusr.getUserXML(con, prof));
                
                String metaXML;
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.instance_id = dbusr.usr_ent_id;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.rol_ext_id = prof.current_role;
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
                String result = formatXML(xml.toString(), metaXML, "user_manager");

                if (urlp.cmd.equalsIgnoreCase("get_biography")) {
                    generalAsHtml(result, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, result);
                }
            }else if (urlp.cmd.equalsIgnoreCase("save_my_biography")) {
                urlp.biography();
                PsnBiography psnBiography = new PsnBiography(); 
                psnBiography.saveBiography(con, prof.usr_ent_id, urlp.option_lst, urlp.self_desc, prof.usr_id);
                con.commit();
//                response.sendRedirect(urlp.url_success);
                cwSysMessage e = new cwSysMessage("PSN001");
                msgBox(MSG_STATUS, e, urlp.url_success, out);

            }else if (urlp.cmd.equalsIgnoreCase("test_sql")) {
                 urlp.sql();
                 CommonLog.info(urlp.sql);
                PreparedStatement stmt = con.prepareStatement(urlp.sql);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()){
                    out.println(rs.getObject(1));
                }
                cwSQL.cleanUp(rs, stmt);
                return;
            }else{
                throw new cwException("invalid command");
            }
            
        }
        catch (Exception e) {
                 con.rollback();
                 CommonLog.error("Server error: " + e.getMessage(),e);
        }
      
    }

}
package com.cw.wizbank.personalization;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;


public class PsnPreferenceModule extends ServletModule
{
    public static final String moduleName = "PsnPreference"; 

    public void process() throws SQLException, IOException, cwException {
        PsnPreferenceReqParam urlp = null;

        urlp = new PsnPreferenceReqParam(request, clientEnc, static_env.ENCODING);

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
            }else if (urlp.cmd.equalsIgnoreCase("get_my_preference") || urlp.cmd.equalsIgnoreCase("get_my_preference_xml")) {
                PsnPreference psnPreference = new PsnPreference();
                StringBuffer xml = new StringBuffer();
                xml.append(psnPreference.getPreferenceListAsXML((Personalization)wizbini.cfgOrgPersonalization.get(prof.root_id), wizbini, wizbini.cfgSysSkinList));
                xml.append(psnPreference.getMajorTcXML(con, prof.usr_ent_id, prof.current_role, wizbini.cfgTcEnabled));                
            
                String result = formatXML(xml.toString(), moduleName);
                if (urlp.cmd.equalsIgnoreCase("get_my_preference")) {
                    generalAsHtml(result, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, result);
                }

//                getPreferenceListAsXML(con, prof.usr_ent_id, writer.toString()); 


            }else if (urlp.cmd.equalsIgnoreCase("save_my_preference")) {
                urlp.preference();
                PsnPreference psnPreference = new PsnPreference(); 
                psnPreference.savePreference(con, prof.usr_ent_id, urlp.skin_id, urlp.lang, prof.usr_id, prof.current_role, urlp.major_tc_id);
                if (urlp.skin_id!=null)
                    prof.current_role_skin_root = urlp.skin_id;
                if (urlp.lang!=null)
                    prof.label_lan = cwUtils.langToLabel(urlp.lang);
                    prof.cur_lan = prof.getCurLan(prof.label_lan);
                
                con.commit();
                response.sendRedirect(urlp.url_success);
            }else if (urlp.cmd.equalsIgnoreCase("del_my_preference")) {
                PsnPreference psnPreference = new PsnPreference(); 
                psnPreference.delPreference(con, prof.usr_ent_id, prof.current_role, prof.usr_id);
                con.commit();
                
                String[] prefer_array = PsnPreference.getPreferenceByEntId(con, prof.usr_ent_id, prof.current_role, (Personalization)wizbini.cfgOrgPersonalization.get(prof.root_id), null, null);
                prof.current_role_skin_root = prefer_array[0];
                prof.label_lan = prefer_array[1];
                prof.cur_lan = prefer_array[2];
                response.sendRedirect(urlp.url_success);
                
            }else{
                throw new cwException("invalid command");
            }
            
        }
        catch (Exception e) {
                 con.rollback();
                out.println("Server error: " + e.getMessage());
        }
      
    }

}
/*
 * Created on 2006-1-3
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.qdb;

import java.io.StringWriter;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.sso.SsoLink;
import com.cw.wizbank.util.cwException;

/**
 * @author dixson
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class dbSSOLink {
    public String getSSOHomeAsXML(loginProfile prof, WizbiniLoader wizbini) throws cwException {

        StringBuffer resultBuf = new StringBuffer();
        String xmlhead = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        resultBuf.append(xmlhead);
        resultBuf.append("<sso_link_query>");
        resultBuf.append("<tc_enabled>").append(wizbini.cfgTcEnabled).append("</tc_enabled>");
        resultBuf.append(prof.asXML()).append("<links>");

        StringWriter writer = new StringWriter();
        wizbini.marshal(((SsoLink)wizbini.cfgOrgSSO.get(prof.root_id)).getLinks(), writer);
        String sso_xml = writer.toString();
        sso_xml = sso_xml.substring(sso_xml.indexOf("<", 2));
        resultBuf.append(sso_xml).append("</links></sso_link_query>");
        return resultBuf.toString();
    }
    
    public static String ssoLinkAsXML(String root_id, WizbiniLoader wizbini) throws cwException {
        //add for sso link get
        StringBuffer resultBuf = new StringBuffer();
        resultBuf.append("<sso_link_query><links>");
        StringWriter writer = new StringWriter();
        wizbini.marshal(((SsoLink)wizbini.cfgOrgSSO.get(root_id)).getLinks(), writer);
        String sso_xml = writer.toString();
        sso_xml = sso_xml.substring(sso_xml.indexOf("<", 2));
        resultBuf.append(sso_xml).append("</links></sso_link_query>");

        return resultBuf.toString();
    }

}

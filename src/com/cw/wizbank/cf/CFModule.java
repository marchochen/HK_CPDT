package com.cw.wizbank.cf;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cwn.wizbank.utils.CommonLog;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class CFModule extends ServletModule    {
    public static final    String MODULENAME    = "cf";
    public static final    String DELIMITER    = "~";

    // for certificate
    private int owner_ent_id;

    public CFModule() {
    }

    public void    process() throws SQLException, IOException,    cwException {

        HttpSession    sess = request.getSession(false);

        CFReqParam urlp    = null;

        urlp = new CFReqParam(request, clientEnc, static_env.ENCODING);
        urlp.setProfile(super.prof);

        if (bMultipart)    {
            urlp.setMultiPart(multi);
        }

        urlp.common();

        PrintWriter    out    = response.getWriter();

        try    {
            // if all command need authorized users
            if (prof ==    null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            } else if (urlp.cmd.equalsIgnoreCase("GET_CF_LST")    ||
                       urlp.cmd.equalsIgnoreCase("GET_CF_LST_XML")) {
                // get parameters
                urlp.getCtf();
                // logic
                CFCertificate ctf = new CFCertificate(con);

                owner_ent_id = (int)prof.root_ent_id;
                StringBuffer xmlBuf    = ctf.getCertificateListAsXML(owner_ent_id,urlp.status,urlp.sort_by,urlp.page_size,urlp.page);
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = urlp.prof.root_ent_id;
                acPageVariant.ent_id = urlp.prof.usr_ent_id;
                acPageVariant.usr_id = urlp.prof.usr_id;
                acPageVariant.rol_ext_id = urlp.prof.current_role;
//                acPageVariant.instance_id = urlp.rsv_id;
                String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                /* page variant END */

                String xml = formatXML(xmlBuf.toString(), metaXML, MODULENAME);

                if (urlp.cmd.equalsIgnoreCase("GET_CF_LST")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("GET_CF_USR_LST")    ||
                       urlp.cmd.equalsIgnoreCase("GET_CF_USR_LST_XML")) {
                // get parameters
                urlp.getCfn();
                // logic
                CFCertificate cfn = new CFCertificate(con);
                owner_ent_id = (int)prof.root_ent_id;
                StringBuffer xmlBuf    = cfn.getCertificateUsrListAsXML(owner_ent_id,urlp.ctf_id,urlp.status,urlp.sort_by,urlp.page_size,urlp.page);
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = urlp.prof.root_ent_id;
                acPageVariant.ent_id = urlp.prof.usr_ent_id;
                acPageVariant.usr_id = urlp.prof.usr_id;
                acPageVariant.rol_ext_id = urlp.prof.current_role;
//                acPageVariant.instance_id = urlp.rsv_id;
                String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                /* page variant END */

                String xml = formatXML(xmlBuf.toString(), metaXML, MODULENAME);

                if (urlp.cmd.equalsIgnoreCase("GET_CF_USR_LST")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("UPD_CTF_STATUS")) {
                // get parameters
                urlp.updCtf();
                // logic
                CFCertificate ctf = new CFCertificate(con);

                owner_ent_id = (int)prof.root_ent_id;
                ctf.updateCertificateStatus(urlp.status,urlp.ctf_id_lst,owner_ent_id,prof.usr_id);
                con.commit();
                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.equalsIgnoreCase("UPD_CFN_STATUS")) {
                // get parameters
                urlp.updCfn();
                // logic
                CFCertificate cfn = new CFCertificate(con);

                owner_ent_id = (int)prof.root_ent_id;
//                for (int www=0;www<urlp.ctf_id_lst.length;www++){
//                    System.out.println(urlp.ctf_id_lst[www]);
//                }
                cfn.updateCertificationStatus(urlp.status,urlp.cfn_ent_id_lst,urlp.cfn_ctf_id_lst,owner_ent_id,prof.usr_id);
                con.commit();
                response.sendRedirect(urlp.url_success);
            } else {
                throw new cwSysMessage("GEN000");
            }
        } catch (cwSysMessage se) {
            try    {
                con.rollback();
                msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
            } catch (SQLException sqlEx) {
                out.println("SQL error: " + sqlEx.getMessage());
                CommonLog.error("SQL error: " + sqlEx.getMessage());
            }
        } catch (qdbException qe) {
            try    {
                con.rollback();
                CommonLog.error(qe.getMessage(),qe);
            } catch (SQLException sqlEx) {
                out.println("SQL error: " + sqlEx.getMessage());
                CommonLog.error("SQL error: " + sqlEx.getMessage(),sqlEx);
            }
        }
    }
}
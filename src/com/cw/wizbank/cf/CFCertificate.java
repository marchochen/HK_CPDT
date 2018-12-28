package com.cw.wizbank.cf;

import java.util.Hashtable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.view.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


public class CFCertificate {
    private String title;
    private static boolean DEBUG = true;
    private static final int PAGE_SIZE = 20;
    /**
     * indicate the completion status
     * ON: not finish issuing
     * OFF: finish issuing
     */
    private String status;
    private int qualifiedNum;
    private int certificatedNum;
    private Connection con;
    public static final String SMSG_CF_GET_PARAM_FAIL = "SMSG_CF_GET_PARAM_FAIL";
    // certificate properties
    private int ctf_id;
    private String ctf_title;
    private String ctf_status;
    private String ctf_link;
    /**
     * not in use at present
     */
    private String certificateLink;

    public CFCertificate(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }
    // default constructor
    public CFCertificate() {

    }

    /**
     * @return String
     */
    public void getCtfTitle(int ctf_id) throws SQLException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        this.ctf_title = cf.getCtfTitle(ctf_id);
    }


    public StringBuffer getCertificateUsrListAsXML(int owner_ent_id,int ctf_id, String filter_status, String order,int page_size,int page)
           throws SQLException, cwSysMessage,qdbException,cwException {
        // page_size initial
        if ((page_size == 0)||(page_size<0)) {
            page_size = PAGE_SIZE;
        }
        // page initial
        if ((page == 0)||(page<0)) {
            page = 1;
        }
        if ((order == null)||(order.equalsIgnoreCase(""))) {
            order = "ASC";
        }
        ViewCfCertificate cf = new ViewCfCertificate(con);
        Hashtable[] ctfUsrLstTB = cf.getCertificateUsrLst(ctf_id,filter_status,order);
        Hashtable[] cfStatusTB = cf.getCertificateStatusList("cfn");
        int ctf_num_cert = cf.getCertificateUsrNum(owner_ent_id,ctf_id,"Certified");
        int ctf_num_not_cert = cf.getCertificateUsrNum(owner_ent_id,ctf_id,"Not Certified");



        int num_of_page = (int)Math.ceil(ctfUsrLstTB.length / (page_size * 1.0));
        if (page > num_of_page) {
            page = num_of_page;
        }
        int page_start = page_size * (page-1);
        int page_end = page_size * page;
        if (page_end > ctfUsrLstTB.length) {
            page_end = ctfUsrLstTB.length;
        }
        String cur_status_id = "";

        // xml
        StringBuffer xmlBuf = new StringBuffer(1024);
        // page info
        xmlBuf.append("<pagination total_rec=\"").append(ctfUsrLstTB.length).append("\"")
              .append(" total_page=\"").append(num_of_page).append("\"");
        xmlBuf.append(" page_size=\"").append(page_size).append("\"");
        xmlBuf.append(" cur_page=\"").append(page).append("\"");
        xmlBuf.append(" sort_col=\"").append("usr_display_bil").append("\"");
        xmlBuf.append(" sort_order=\"").append(order).append("\"/>");
        // certificate_issue
        dbRegUser usr = new dbRegUser();
        int qualification_ind = 0;
        String cfStatus = "";
        String cfn_status = "";
        StringBuffer tmpXML = new StringBuffer(1024);
        Timestamp issue_date;
        String crt_id = "";
        String crt_not_id = "";
        for (int i=0;i<cfStatusTB.length;i++) {
            cfStatus = cfStatusTB[i].get("ctb_title").toString();
            tmpXML.append("<status id=\"").append(cfStatusTB[i].get("ctb_id")).append("\" title=\"").append(cfStatus).append("\" count=\"");
            if (cfStatus.equalsIgnoreCase("Certified")) {
                tmpXML.append(ctf_num_cert).append("\"/>");
                crt_id = cfStatusTB[i].get("ctb_id").toString();
            } else {
                if (cfStatus.equalsIgnoreCase("Not Certified")) {
                    crt_not_id = cfStatusTB[i].get("ctb_id").toString();
                    tmpXML.append(ctf_num_not_cert).append("\"/>");
                }
            }
            if (cfStatus.equalsIgnoreCase(filter_status)) {
                cur_status_id = cfStatusTB[i].get("ctb_id").toString();
            }
        }
        if ((ctfUsrLstTB != null)&&(ctfUsrLstTB.length > 0)) {


            xmlBuf.append("<certificate_issue><certificate id=\"").append(ctf_id).append("\" ctf_title=\"").append(ctfUsrLstTB[0].get("ctf_title"))
                  .append("\"/><status_list cur_status_id=\"").append(cur_status_id)
                  .append("\">")
                  .append(tmpXML.toString())
                  .append("</status_list>");
            for (int j=page_start;j<page_end;j++) {
                //
                if (ctfUsrLstTB[j].get("usr_ent_id").toString().equalsIgnoreCase("")) {
                    usr.usr_ent_id = 0;
                }else {
                    usr.usr_ent_id = Integer.parseInt(ctfUsrLstTB[j].get("usr_ent_id").toString());
                }
                usr.get(con);
                if (ctfUsrLstTB[j].get("cfn_qualification_ind").toString().equalsIgnoreCase("")) {
                    qualification_ind = 0;
                } else {
                    qualification_ind = Integer.parseInt(ctfUsrLstTB[j].get("cfn_qualification_ind").toString());
                }
                cfn_status = ctfUsrLstTB[j].get("cfn_status").toString();
                issue_date = (Timestamp)ctfUsrLstTB[j].get("cfn_upd_timestamp");
                xmlBuf.append("<cert_lrn>")
                      //usr info
                      .append(usr.getUserShortXML(con,false,true))
                      .append("<qualified_ind>");
                if (qualification_ind == 1) {
                    xmlBuf.append("true");
                } else {
                    xmlBuf.append("false");
                }
                xmlBuf.append("</qualified_ind><issue_status_id>");

                if (cfn_status.equalsIgnoreCase("Certified")) {
                    xmlBuf.append(crt_id).append("</issue_status_id><issue_date>").append(issue_date).append("</issue_date>");
                } else { if(cfn_status.equalsIgnoreCase("Not Certified"))
                    xmlBuf.append(crt_not_id).append("</issue_status_id><issue_date/>");
                }
                xmlBuf.append("</cert_lrn>");
            }
            xmlBuf.append("</certificate_issue>");
        } else {
            getCtfTitle(ctf_id);
            xmlBuf.append("<certificate_issue><certificate id=\"").append(ctf_id).append("\" ctf_title=\"").append(ctf_title)
                  .append("\"/><status_list cur_status_id=\"").append(cur_status_id)
                  .append("\">")
                  .append(tmpXML.toString())
                  .append("</status_list>").append("</certificate_issue>");
        }
        return xmlBuf;
    }
    /**
     *
     * @param owner_ent_id
     * @param filter_status
     * @return
     */
    public StringBuffer getCertificateListAsXML(int owner_ent_id, String filter_status, String order,int page_size,int page)
           throws SQLException, cwSysMessage {
        // page_size initial
        if ((page_size == 0)||(page_size<0)) {
            page_size = PAGE_SIZE;
        }
        // page initial
        if ((page == 0)||(page<0)) {
            page = 1;
        }
        if ((order == null)||(order.equalsIgnoreCase(""))) {
            order = "ASC";
        }
        // get certificate list by owner_ent_id , order
        ViewCfCertificate cf = new ViewCfCertificate(con);
        int ctf_num_on = cf.getCertificateNum("ON",owner_ent_id);
        int ctf_num_off = cf.getCertificateNum("OFF",owner_ent_id);
        if (DEBUG) {
//            System.out.println("ctf_num_on: " + ctf_num_on + " and ctf_num_off: " + ctf_num_off);
        }
        Hashtable[] cfDtlLstTB = cf.getCertificateDtlList(owner_ent_id,filter_status,order);
        Hashtable[] cfAppliedTB = cf.getCertificateAppliedList(owner_ent_id,filter_status,order);
        Hashtable[] cfStatusTB = cf.getCertificateStatusList("ctf");
        int num_of_page = (int)Math.ceil(cfAppliedTB.length / (page_size * 1.0));
        if (page > num_of_page) {
            page = num_of_page;
        }
        int page_start = page_size * (page-1);
        int page_end = page_size * page;
        if (page_end > cfAppliedTB.length) {
            page_end = cfAppliedTB.length;
        }
        // xml
        StringBuffer xmlBuf = new StringBuffer(1024);
        // page info
        xmlBuf.append("<pagination total_rec=\"").append(cfAppliedTB.length).append("\"")
              .append(" total_page=\"").append(num_of_page).append("\"");
        xmlBuf.append(" page_size=\"").append(page_size).append("\"");
        xmlBuf.append(" cur_page=\"").append(page).append("\"");
        xmlBuf.append(" sort_col=\"").append("ctf_title").append("\"");
        xmlBuf.append(" sort_order=\"").append(order).append("\"/>");

        // certificate list
        String cfStatus = "";
        String cur_status_id = "";
        StringBuffer tmpXML = new StringBuffer(1024);
        for (int i=0;i<cfStatusTB.length;i++) {
            cfStatus = cfStatusTB[i].get("ctb_title").toString();
            tmpXML.append("<status id=\"").append(cfStatusTB[i].get("ctb_id")).append("\" title=\"").append(cfStatus).append("\" count=\"");
            if (cfStatus.equalsIgnoreCase("ON")) {
                tmpXML.append(ctf_num_on).append("\"/>");
            } else {
                if (cfStatus.equalsIgnoreCase("OFF")) {
                    tmpXML.append(ctf_num_off).append("\"/>");
                }
            }
            if (cfStatus.equalsIgnoreCase(filter_status)) {
                cur_status_id = cfStatusTB[i].get("ctb_id").toString();
            }
        }
        xmlBuf.append("<certificate_list>")
              .append("<status_list cur_status_id=\"").append(cur_status_id).append("\">")
              .append(tmpXML.toString());

        xmlBuf.append("</status_list>");
        // get certificate details:
        int issued_count = 0;
        int qualified_count = 0;
        int total_count = 0;
        int ctf_id = 0;
        String ctf_title = "";
        String ctf_status = "";

        //
        if ((cfDtlLstTB != null) && (cfDtlLstTB.length > 0)&&(cfAppliedTB != null)&&(cfAppliedTB.length > 0)) {
            for (int j=page_start;j<page_end;j++) {
                qualified_count = 0;
                issued_count = 0;
                for (int m = 0;m<cfDtlLstTB.length;m++){
                    if (cfAppliedTB[j].get("ctf_id").equals(cfDtlLstTB[m].get("ctf_id"))){
                        if (cfDtlLstTB[m].get("cfn_qualified_num").toString().equalsIgnoreCase("")) {
                            qualified_count = qualified_count + 0;
                        } else {
                            qualified_count = qualified_count + Integer.parseInt(cfDtlLstTB[m].get("cfn_qualified_num").toString());
                        }
                        if (cfDtlLstTB[m].get("cfn_status").equals("Certified")) {
                            if (cfDtlLstTB[m].get("lrn_num").toString().equalsIgnoreCase("")) {
                                issued_count = issued_count + 0;
                            }else {
                                issued_count = issued_count + Integer.parseInt(cfDtlLstTB[m].get("lrn_num").toString());
                            }
                        }
                    }
                }
                if (cfAppliedTB[j].get("count_applied").toString().equalsIgnoreCase("")) {
                    total_count = 0;
                } else {
                    total_count = Integer.parseInt(cfAppliedTB[j].get("count_applied").toString());
                }

                xmlBuf.append("<certificate id=\"").append(cfAppliedTB[j].get("ctf_id"))
                      .append("\" status_id=\"").append(cfAppliedTB[j].get("ctb_id"))
                      .append("\" title=\"").append(cfAppliedTB[j].get("ctf_title"))
                      .append("\" issued_count=\"").append(issued_count)
                      .append("\" qualified_count=\"").append(qualified_count)
                      .append("\" total_count=\"").append(total_count).append("\"/>");

            }
        }
        xmlBuf.append("</certificate_list>");

//        if (DEBUG) {
//            System.out.println("getCertificationListAsXML: ");
//            System.out.println(xmlBuf);
//        }
        return xmlBuf;

    }
    public void updateCertificateStatus(String ctf_status, String[] ctf_id_lst, int owner_ent_id,String usr_id)
        throws SQLException ,cwSysMessage,cwException {
        //
        if (ctf_id_lst != null) {
            ViewCfCertificate cf = new ViewCfCertificate(con);
            cf.updateCertificateStatus(ctf_status, ctf_id_lst,owner_ent_id,usr_id);
        } else {
            throw new cwSysMessage(SMSG_CF_GET_PARAM_FAIL);
        }
    }
    //
    public void deleteCertificate(int ctf_id, int owner_ent_id) throws SQLException ,cwSysMessage,cwException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        cf.deleteCertificate(ctf_id,owner_ent_id);

    }
    public long insertCertificate(CFCertificate pCertificate, int owner_ent_id, String usr_id)
            throws SQLException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
         long ctf_id = cf.insertCertificate(ctf_title, ctf_status, ctf_link,owner_ent_id,usr_id);
         return ctf_id;

    }
    public void updateCertificate(CFCertificate pCertificate, int owner_ent_id, String usr_id)
            throws SQLException ,cwSysMessage,cwException {
        //

        ViewCfCertificate cf = new ViewCfCertificate(con);
        cf.updateCertificate(ctf_id, ctf_title, owner_ent_id, usr_id);

    }
    //
    public void updateCertificationStatus(String cfn_status,String[] cfn_ent_id_lst, String[] cfn_ctf_id_lst, int owner_ent_id,String usr_id)
        throws SQLException ,cwSysMessage,cwException {
        //
        if ((cfn_ent_id_lst != null)&&(cfn_ctf_id_lst !=null)) {
            ViewCfCertificate cf = new ViewCfCertificate(con);
            cf.updateCertificationStatus(cfn_status, cfn_ent_id_lst,cfn_ctf_id_lst,owner_ent_id,usr_id);
        } else {
            throw new cwSysMessage(SMSG_CF_GET_PARAM_FAIL);
        }
    }

    /**
     * @return String
     */
    public String getCertificateLink() {
        return null;
    }
    //--------------------------------------------
     public String getCertificationStatus(int cfn_ctf_id, int cfn_ent_id)
           throws SQLException {
        //
        ViewCfCertificate cf = new ViewCfCertificate(con);
        return cf.getCertificationStatus(cfn_ctf_id,cfn_ent_id);

    }
    //(int ctf_id, int lrn_ent_id, int owner_ent_id, int create_usr_id)
    public void insertCertification(int cfn_ctf_id, int cfn_ent_id, String cfn_status,int cfn_qualification_ind,int owner_ent_id, String usr_id)
            throws SQLException ,cwSysMessage,cwException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        cf.insertCertification(cfn_ctf_id, cfn_ent_id, cfn_qualification_ind,cfn_status, owner_ent_id,usr_id);

    }
    //
    public void deleteCertification(int cfn_ctf_id, int cfn_ent_id, int owner_ent_id)
           throws SQLException {

        ViewCfCertificate cf = new ViewCfCertificate(con);
        cf.deleteCertification(cfn_ctf_id, cfn_ent_id, owner_ent_id);
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.ctf_title = title;
    }
    public void setCtfID(int ctf_id) {
        this.ctf_id = ctf_id;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.ctf_status = status;
    }

    /**
     * @param qualifiedNum
     */
    public void setQualifiedNum(int qualifiedNum) {
        this.qualifiedNum = qualifiedNum;
    }

    /**
     * @param certificatedNum
     */
    public void setCertificatedNum(int certificatedNum) {
        this.certificatedNum = certificatedNum;

    }

    /**
     * @param certificateLink
     */
    public void setCertificateLink(String certificateLink) {
        this.ctf_link = certificateLink;

    }
    // for item linkage
    public void insCertificationItem(int itm_id,int cfn_ent_id,int cfn_qualification_ind,String cfn_status ,int owner_ent_id,String usr_id) throws SQLException ,cwSysMessage,cwException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        Hashtable[] ind = cf.getItyCtfInd(itm_id);
        int ity_certificate_ind = 0;
        int ctf_id = 0;
        if ((ind!=null)&&(ind.length>0)){
            if (!(ind[0].get("ity_certificate_ind").toString().equalsIgnoreCase(""))) ity_certificate_ind = Integer.parseInt(ind[0].get("ity_certificate_ind").toString());
            if (!(ind[0].get("itm_ctf_id").toString().equalsIgnoreCase(""))) ctf_id = Integer.parseInt(ind[0].get("itm_ctf_id").toString());
        }
        if (ity_certificate_ind ==1) {
            this.insertCertification(ctf_id,cfn_ent_id, cfn_status ,cfn_qualification_ind ,owner_ent_id, usr_id);
        }
    }
    public void delCertificationItem(int itm_id,int cfn_ent_id,int owner_ent_id) throws SQLException ,cwSysMessage,cwException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        Hashtable[] ind = cf.getItyCtfInd(itm_id);
        int ity_certificate_ind = 0;
        int ctf_id = 0;
        if ((ind!=null)&&(ind.length>0)){
            if (!(ind[0].get("ity_certificate_ind").toString().equalsIgnoreCase(""))) ity_certificate_ind = Integer.parseInt(ind[0].get("ity_certificate_ind").toString());
            if (!(ind[0].get("itm_ctf_id").toString().equalsIgnoreCase(""))) ctf_id = Integer.parseInt(ind[0].get("itm_ctf_id").toString());
        }
        if (ity_certificate_ind ==1) {
            this.deleteCertification(ctf_id,cfn_ent_id,owner_ent_id);
        }
    }
    //delete cfn by appID
    public void delCertificationItemAppID(int app_id,int owner_ent_id) throws SQLException ,cwSysMessage,cwException {
        ViewCfCertificate cf = new ViewCfCertificate(con);
        Hashtable[] ind = cf.getItyCtfIndAppID(app_id);
        int ity_certificate_ind = 1;
        int itm_ctf_id = 0;
        int app_ent_id = 0;
        if ((ind!=null)&&(ind.length>0)){
            if (!(ind[0].get("ity_certificate_ind").toString().equalsIgnoreCase(""))) ity_certificate_ind = Integer.parseInt(ind[0].get("ity_certificate_ind").toString());
            if (!(ind[0].get("itm_ctf_id").toString().equalsIgnoreCase(""))) itm_ctf_id = Integer.parseInt(ind[0].get("itm_ctf_id").toString());
            if (!(ind[0].get("app_ent_id").toString().equalsIgnoreCase(""))) app_ent_id = Integer.parseInt(ind[0].get("app_ent_id").toString());
        }
        if (ity_certificate_ind ==1) {
            this.deleteCertification(itm_ctf_id,app_ent_id,owner_ent_id);

        }
    }
}
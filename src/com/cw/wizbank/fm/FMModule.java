package    com.cw.wizbank.fm;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;



// added for procUploadedFiles(...)
import java.util.Enumeration;
import java.io.File;
import java.io.FileInputStream;

import com.cw.wizbank.util.*;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;

/* DENNIS ACL BEGIN */
import com.cw.wizbank.accesscontrol.AcReservation;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
/* DENNIS ACL END */
import com.cw.wizbank.db.view.ViewFmFacilitySchedule;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

public class FMModule extends ServletModule    {
    public static final    String MODULENAME    = "fm";
    public static final    String DELIMITER    = "~";

    // system message ...
    public static final    String SMSG_FMT_INS_FACILITY    = "FMT004";
    public static final String SMSG_FMT_GET_RSV_NONE    = "FMT001";
    public static final String SMSG_FMT_GET_FSH_NONE    = "FMT002";

    // variables for reservation
    private int root_ent_id;

    public void    process() throws SQLException, IOException,    cwException    {

        HttpSession    sess = request.getSession(false);

        FMReqParam urlp    = null;

        urlp = new FMReqParam(request, clientEnc, static_env.ENCODING);
        urlp.setProfile(super.prof);

        if (bMultipart)    {
            urlp.setMultiPart(multi);
            this.procUploadedFiles(this.tmpUploadPath, urlp);
            urlp.setFileUploaded(this.bFileUploaded);
        }

        urlp.common();

        PrintWriter    out    = response.getWriter();

        try    {
            // if all command need authorized users
            if (prof ==    null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            } else if (urlp.cmd.equalsIgnoreCase("ins_fac")) {
                /**
                 * insert facility
                 */
                Hashtable param = new Hashtable();
                FMFacilityManager facilityMgr = new FMFacilityManager(super.con);
                urlp.getFacilityInsterion(param);
                int fac_id = facilityMgr.insFacility(param);

                // added to process the uploaded facility thumbnail
                String saveDirPath = static_env.INI_FM_DIR_UPLOAD + dbUtils.SLASH + fac_id;
                this.getUploadFile(this.tmpUploadPath, saveDirPath);

                con.commit();

                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.equalsIgnoreCase("upd_fac")) {
                /**
                 * update facility
                 */
                Hashtable param = new Hashtable();
                FMFacilityManager facilityMgr = new FMFacilityManager(super.con);
                urlp.getFacilityUpdate(param);
                String fac_url_type = null;
                if(param.containsKey("fac_url_type")) {
                	fac_url_type = (String)param.get("fac_url_type");
                }
                /* access control BEGIN */
   
                if(!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_FACILITY_INFO) ) {
                    throw new cwSysMessage("ACL002");
                }
                /* access control END */
                facilityMgr.updFacility(param);

                // added to process the uploaded facility thumbnail
                if(fac_url_type == null || fac_url_type.equalsIgnoreCase("UPLOAD")) {
                	String saveDirPath = static_env.INI_FM_DIR_UPLOAD + dbUtils.SLASH
                	+ ((Integer)param.get("fac_id")).intValue();
                	dbUtils.delFiles(saveDirPath);
                	this.getUploadFile(this.tmpUploadPath, saveDirPath);
                }

                con.commit();

                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.equalsIgnoreCase("del_fac")) {
                /**
                 * remove facility
                 */
                Hashtable param = new Hashtable();
                FMFacilityManager facilityMgr = new FMFacilityManager(super.con);
                urlp.getFacilityDeletion(param);
                /* access control BEGIN */
                if(!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_FACILITY_INFO)){
                    throw new cwSysMessage("ACL002");
                }
                /* access control END */
                facilityMgr.removeFacility(param);

                // added to process the uploaded facility thumbnail
                    String saveDirPath = static_env.INI_FM_DIR_UPLOAD + dbUtils.SLASH
                                       + ((Integer)param.get("fac_id")).intValue();
                    dbUtils.delDir(saveDirPath);

                con.commit();
                msgBox(ServletModule.MSG_STATUS, new cwSysMessage("LN173"), urlp.url_success, out);
                //response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.equalsIgnoreCase("get_fac") ||
                       urlp.cmd.equalsIgnoreCase("get_fac_xml")) {
                /**
                 * show facility details
                 * (sub-type specific additional information)
                 */
                Hashtable param = new Hashtable();
                FMFacilityManager facilityMgr = new FMFacilityManager(super.con);
                urlp.getFacilityGet(param);
                String xml = facilityMgr.getFacilityDetails(param);
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = urlp.prof.root_ent_id;
                acPageVariant.ent_id = urlp.prof.usr_ent_id;
                acPageVariant.rol_ext_id = urlp.prof.current_role;
                acPageVariant.instance_id = ((Integer)param.get("fac_id")).intValue();
                String metaXML = acPageVariant.answerPageVariantAsXML(AcXslQuestion.getOneXslQuestions(urlp.stylesheet, xslQuestions));
                /* page variant END */
                xml += "<fac_dir_url>" + wizbini.cfgSysSetupadv.getFileUpload().getFacDir().getUrl() + "</fac_dir_url>";
                xml = formatXML(xml, metaXML, MODULENAME);

                // output
                if (urlp.cmd.equalsIgnoreCase("get_fac")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("get_fac_lst") ||
                       urlp.cmd.equalsIgnoreCase("get_fac_lst_xml")) {
                /**
                 * list facilities
                 * (filter by type, group by sub-type)
                 */
            	String title =new String();  //��ʾ����Ԥ������
            	try {
            		title=request.getParameter("title").toString();
				} catch (Exception e) {
					title="";
					// TODO: handle exception
				}
            	
                Hashtable param = new Hashtable();
                FMFacilityManager facilityMgr = new FMFacilityManager(super.con);
                urlp.getFacilityList(param);
                String xml = facilityMgr.getFacilityList(param);
                if(!title.equals("")){
                	xml+="<showtitle>"+title+"</showtitle>";
                }
                
                
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = urlp.prof.root_ent_id;
                acPageVariant.ent_id = urlp.prof.usr_ent_id;
                acPageVariant.rol_ext_id = urlp.prof.current_role;
                String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                /* page variant END */

                xml = formatXML(xml, metaXML, MODULENAME);

        
                
                // output
                if (urlp.cmd.equalsIgnoreCase("get_fac_lst")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("get_rsv_lst") ||
                       urlp.cmd.equalsIgnoreCase("get_rsv_lst_xml")) {
                /**
                 * list the reservation record on the specified condition
                 */
                Hashtable param = new Hashtable();
                FMReservationManager rsvRecordMgr = new FMReservationManager(super.con);
                urlp.getRsvRecordList(param);
                String xml = rsvRecordMgr.getRsvRecordList(param);
                String metaXml = rsvRecordMgr.getRsvRecordListParam(param);

                xml = formatXML(xml, metaXml, MODULENAME);

                String fileName = (String)param.get("filename");
                Integer download = (Integer)param.get("download");
                if (urlp.cmd.equalsIgnoreCase("get_rsv_lst")) {
                    if (download != null && download.intValue() != 0) {
                        response.setHeader("Cache-Control", ""); 
                        response.setHeader("Pragma", ""); 
                        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls;");
                        cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                    }
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("get_calendar") ||
                       urlp.cmd.equalsIgnoreCase("get_calendar_xml")) {
                /**
                 * calendar view
                 * (availability determination, external fac. handling)
                 */
                Hashtable param = new Hashtable();
                FMReservationManager rsvRecordMgr = new FMReservationManager(super.con);
                urlp.getCalendar(param);
                String xml = rsvRecordMgr.getCalendarList(param);

                xml = formatXML(xml, MODULENAME);

                if (urlp.cmd.equalsIgnoreCase("get_calendar")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            /**
             *
             * for cmd = get_rsv
             *
             */
            } else if (urlp.cmd.equalsIgnoreCase("GET_RSV")    ||
                       urlp.cmd.equalsIgnoreCase("GET_RSV_XML")) {
                // get parameters
                urlp.getRsvID();
                // logic
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                // reservationMgr.rsv_id    = urlp.rsv_id;
                root_ent_id    = (int)prof.root_ent_id;
                StringBuffer xmlBuf    = reservationMgr.getRsvAsXML(urlp.rsv_id,root_ent_id);
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = urlp.prof.root_ent_id;
                acPageVariant.ent_id = urlp.prof.usr_ent_id;
                acPageVariant.usr_id = urlp.prof.usr_id;
                acPageVariant.rol_ext_id = urlp.prof.current_role;
                acPageVariant.instance_id = urlp.rsv_id;
                String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                /* page variant END */

                String xml = formatXML(xmlBuf.toString(), metaXML, MODULENAME);

                if (urlp.cmd.equalsIgnoreCase("GET_RSV")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("GET_CART") ||
                       urlp.cmd.equalsIgnoreCase("GET_CART_XML")) {

                FMReservationManager reservationMgr    = new FMReservationManager(con);
                root_ent_id    = (int)prof.root_ent_id;
                StringBuffer xmlBuf    = reservationMgr.getFshAsXML(prof.usr_id,root_ent_id);
                ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(con);
                xmlBuf.append("<fac_total_cost>")
                	  .append(cwUtils.formatNumber(fshDtl.getTotalCost(0, root_ent_id), 1))
                	  .append("</fac_total_cost>");
                String xml = formatXML(xmlBuf.toString(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_CART")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("DEL_FSH")) {
                // get parameters
                urlp.getFshList();
                /* access control BEGIN */
    
                if(!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_FACILITY_INFO)) {
                    throw new cwSysMessage("ACL002");
                }
                /* access control END */
                root_ent_id    = (int)prof.root_ent_id;
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                reservationMgr.delFshInCart(prof.usr_id,root_ent_id,urlp.fsh_fac_id_lst,urlp.fsh_start_time_lst,urlp.fsh_upd_timestamp_lst);

                con.commit();

                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.equalsIgnoreCase("PREP_CANCEL_FSH")    ||
                       urlp.cmd.equalsIgnoreCase("PREP_CANCEL_FSH_XML")) {
                urlp.getFshList();
                urlp.getRsvID();
                /* access control BEGIN */
 
                if(!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_FACILITY_INFO)) {
                    throw new cwSysMessage("ACL002");
                }
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                root_ent_id    = (int)prof.root_ent_id;
                StringBuffer xmlBuf    = reservationMgr.prepCancelFshAsXML(prof.usr_id,root_ent_id,urlp.rsv_id,urlp.fsh_fac_id_lst,urlp.fsh_start_time_lst,urlp.fsh_upd_timestamp_lst);
                // xml StringBuffer    toString() here

                String xml = formatXML(xmlBuf.toString(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("PREP_CANCEL_FSH")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("CANCEL_FSH"))    {
                urlp.getFshList();
                urlp.getRsvID();
                urlp.getCancelType();
                /* access control BEGIN */
                if(!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_FACILITY_INFO) ){
                    throw new cwSysMessage("ACL002");
                }
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                root_ent_id    = (int)prof.root_ent_id;
                // ae_fm_linkage ; boolean link_feedback_ind default in booking system is "true" --> call linkage method
                boolean link_feedback_ind = true;
                reservationMgr.cancelFsh(root_ent_id,prof.usr_id,urlp.rsv_id,urlp.fsh_fac_id_lst,urlp.fsh_start_time_lst,urlp.fsh_upd_timestamp_lst,urlp.cancel_type,urlp.cancel_reason,link_feedback_ind);
                con.commit();
                response.sendRedirect(urlp.url_success);
            } else if (urlp.cmd.equalsIgnoreCase("PREP_CANCEL_RSV")    ||
                       urlp.cmd.equalsIgnoreCase("PREP_CANCEL_RSV_XML")) {
                urlp.getRsvID();
                urlp.getRsvUpdTime();
                /* access control BEGIN */
                if(!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_FACILITY_INFO)) {
                    throw new cwSysMessage("ACL002");
                }
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                root_ent_id    = (int)prof.root_ent_id;
                StringBuffer xmlBuf    = reservationMgr.prepCancelRsvAsXML(prof.usr_id,root_ent_id,urlp.rsv_id,urlp.rsv_upd_timestamp);
                String xml = formatXML(xmlBuf.toString(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("PREP_CANCEL_RSV")) {
                    generalAsHtml(xml, out,    urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            } else if (urlp.cmd.equalsIgnoreCase("CANCEL_RSV"))    {
                urlp.getRsvID();
                urlp.getRsvUpdTime();
                urlp.getCancelType();
                /* access control BEGIN */
                if(!AccessControlWZB.hasRolePrivilege( prof.current_role,  AclFunction.FTN_AMD_FACILITY_INFO)) {
                    throw new cwSysMessage("ACL002");
                }
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                root_ent_id    = (int)prof.root_ent_id;
                // ae_fm_linkage ; boolean link_feedback_ind default in booking system is "true" --> call linkage method
                boolean link_feedback_ind = true;
                reservationMgr.cancelRsv(root_ent_id,prof.usr_id,urlp.rsv_id,urlp.rsv_upd_timestamp,urlp.cancel_type,urlp.cancel_reason,link_feedback_ind);
                con.commit();
                response.sendRedirect(urlp.url_success);
            }
            /* DENNIS BEGIN */
            else if (urlp.cmd.equalsIgnoreCase("CHECK_FAC_AVAIL")
                     || urlp.cmd.equalsIgnoreCase("CHECK_FAC_AVAIL_XML"))    {
                urlp.checkFacAvail();
             
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                String xml = reservationMgr.queryFsh(urlp.action, urlp.int_fac_id_lst,
                                                     urlp.fsh_start_date,
                                                     urlp.fsh_end_date,
                                                     urlp.fsh_start_time,
                                                     urlp.fsh_end_time,
                                                     urlp.rsv_id,
                                                     prof.usr_id,
                                                     (int)prof.root_ent_id);
                if(xml == null) {
                    //facility schedules saved to cart
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                } else {
                    //unlock facility schedule table
                    con.rollback();
                    xml = formatXML(xml, MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("CHECK_FAC_AVAIL")) {
                        generalAsHtml(xml, out,    urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("CHECK_FSH_CONFLICT")
                    || urlp.cmd.equalsIgnoreCase("CHECK_FSH_CONFLICT_XML"))    {
                urlp.checkFshConflict();
               
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                String xml = reservationMgr.queryFsh(urlp.action, urlp.int_fac_id_lst,
                                                     urlp.ts_fsh_date_lst,
                                                     urlp.ts_fsh_start_time_lst,
                                                     urlp.ts_fsh_end_time_lst,
                                                     urlp.rsv_id,
                                                     prof.usr_id,
                                                     (int)prof.root_ent_id);
                if(xml == null) {
                    //facility schedules saved to cart
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                } else {
                    //unlock facility schedule table
                    con.rollback();
                    xml = formatXML(xml, MODULENAME);
                    if (urlp.cmd.equalsIgnoreCase("CHECK_FAC_AVAIL")) {
                        generalAsHtml(xml, out,    urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("INS_RSV"))    {
                urlp.insRsv();
               
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                String xml =
                    reservationMgr.insRsv(urlp.rsv_purpose,
                                          urlp.rsv_desc,
                                          ((urlp.int_rsv_ent_id > 0) ?
                                            urlp.int_rsv_ent_id : (int)urlp.prof.usr_ent_id),
                                          urlp.rsv_participant_no,
                                          urlp.rsv_main_fac_id,
                                          urlp.int_fac_id_lst,
                                          urlp.ts_fsh_start_time_lst,
                                          urlp.ts_fsh_upd_timestamp_lst,
                                          urlp.fsh_status_lst,
                                          prof.usr_id, (int)prof.root_ent_id);
                con.commit();
                xml = formatXML(xml, MODULENAME);
                generalAsHtml(xml, out,    urlp.stylesheet);
                //response.sendRedirect(urlp.url_success);
            }
            else if (urlp.cmd.equalsIgnoreCase("UPD_RSV"))    {
                urlp.updRsv();
               
                /* access control END */
                FMReservationManager reservationMgr    = new FMReservationManager(con);
                String xml =
                    reservationMgr.updRsv(urlp.rsv_id,
                                          urlp.rsv_purpose,
                                          urlp.rsv_desc,
                                          ((urlp.int_rsv_ent_id > 0) ?
                                            urlp.int_rsv_ent_id : (int)urlp.prof.usr_ent_id),
                                          urlp.rsv_participant_no,
                                          urlp.rsv_main_fac_id,
                                          urlp.rsv_upd_timestamp,
                                          urlp.int_fac_id_lst,
                                          urlp.ts_fsh_start_time_lst,
                                          urlp.ts_fsh_upd_timestamp_lst,
                                          urlp.fsh_status_lst,
                                          prof.usr_id, (int)prof.root_ent_id);
                con.commit();
                xml = formatXML(xml, MODULENAME);
                generalAsHtml(xml, out,    urlp.stylesheet);
                //response.sendRedirect(urlp.url_success);

            }
            /* DENNIS END */
            else if (urlp.cmd.equalsIgnoreCase("SEARCH_FAC_FEE_PREP")
                    || urlp.cmd.equalsIgnoreCase("SEARCH_FAC_FEE_PREP_XML")) {
            	FMFacilityManager facilityMgr = new FMFacilityManager(super.con);
                String xml = facilityMgr.getFMFeePrepAsXml(prof.root_ent_id);
                String noheader = "<noheader>" + request.getParameter("noheader") + "</noheader>";
                xml = formatXML(xml + noheader, null, MODULENAME);
                // output
                if (urlp.cmd.equalsIgnoreCase("SEARCH_FAC_FEE_PREP")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
            }
            else {
                throw new cwSysMessage("GEN000");
            }
        } catch (cwSysMessage se) {
            try    {
                con.rollback();
                msgBox(ServletModule.MSG_ERROR, se, urlp.url_failure, out);
            } catch (SQLException sqlEx) {
                out.println("SQL error: " + sqlEx.getMessage());
            }
        }
    }

    private void encodeURI(String showTitle) {
		// TODO Auto-generated method stub
		
	}

	/**
     * added to check the uploaded files
     * emily, 2002.9.19
     */
    private boolean bFileUploaded = false;
    private void procUploadedFiles(String tmpSaveDirPath, FMReqParam urlp)
            throws cwException {
        try {
            // added to check file uploaded or not....
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String)files.nextElement();
                String fileName = multi.getFilesystemName(name);
                if (fileName != null && fileName.length() > 0) {
                    File fileUploaded = new File(tmpSaveDirPath, fileName);
                    FileInputStream fis = new FileInputStream(fileUploaded);
                    if (fis.read() != -1) {
                        this.bFileUploaded = true;
                    }
                    fis.close();
                }
            }
        } catch (IOException e) {
            CommonLog.error(e.getMessage(),e);
        }
    }

    private void getUploadFile(String tmpSaveDirPath, String saveDirPath) throws cwException {
        try {
            if (bFileUploaded) {
                dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
            }
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
    }
}
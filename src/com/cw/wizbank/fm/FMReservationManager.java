package com.cw.wizbank.fm;

/* DENNIS BEGIN */
import java.util.Enumeration;
/* DENNIS END */
import java.util.Hashtable;
import java.util.Vector;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.db.DbFmFacilityType;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.codetable.*;
import com.cwn.wizbank.utils.CommonLog;

public class FMReservationManager {
    // String for cwSysMessage
    //   for update failure
    public static final String SMSG_FMT_UPD_FAIL = "FMT001";
    public static final String SMSG_FMT_DEL_FAIL = "FMT002";
    //    for parameter not found
    public static final String  SMSG_FMT_GET_PARAM_FAIL = "FMT003";
    //   for record not found
    public static final String SMSG_FMT_GET_RSV_NONE = "FMT004";
    public static final String SMSG_FMT_GET_FSH_NONE = "FMT005";
    //   "The Facility Schedule Record Not Found !"
    public static final String SMSG_FMT_GET_FTP_NONE = "FMT006";

    // String system name
    //   SYSNAME
    public static final String SYSNAME = "fmFacilityManagement";

    // status of Reservation ; in Database
    public static final String RSV_STATUS_OK = "OK";
    private static final String RSV_STATUS_CANCEL = "CANCEL";
    // status of facilitySchedule ; in Database
    public static final String FSH_STATUS_PENCILLED_IN = "PENCILLED_IN";
    public static final String FSH_STATUS_RESERVED = "RESERVED";
    public static final String FSH_STATUS_CANCELLED = "CANCELLED";
    // for facility management system cancel type
    public static final String FM_CANCEL_TYPE = "fmCancelType";
    
    public long rsv_id;
    //----------------------------------------------------------------------------------------
    // RsvRecordManager other variables here
    private final static boolean DEBUG = false;

    // available status
    private final static String SLOT_AVAILABLE    = "AVAILABLE";
    private final static String SLOT_PART        = "PART";
    private final static String SLOT_FULL        = "FULL";
    private final static String SLOT_ALMOSTFULL    = "ALMOSTFULL";

    // today flag
    private final static String YES        = "YES";
    private final static String NO        = "NO";
    // days of a week
    private final static int dayNum = 7;
    private final static int day    = 24*60*60*1000;

    // the variable hold the system slot value
    private static String[][] slots = null;
    // the variable hold the minimized time gap
    private static int minGap = 0;
    private static int maxScheduleCount = 0;

    private int period = 0;
    private Connection con = null;
    /**
     *
     * @param con
     * @throws SQLException
     */
    public FMReservationManager(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }

    /**
     *
     * @param rsv_id
     * @param root_ent_id
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    public StringBuffer getRsvAsXML(int rsv_id,int root_ent_id)
        throws SQLException, cwSysMessage {
        // get reservation by rsv_id,root_ent_id
        ViewFmReservation rsvDtl = new ViewFmReservation(con);
        Hashtable[] rsvDtlTB = rsvDtl.getRsvDtl( rsv_id,root_ent_id);
        // check existing of reservation record;
        if (rsvDtlTB.length == 0) {
            throw new cwSysMessage(SMSG_FMT_GET_RSV_NONE);//SMSG_FMT_GET_RSV = "The Reservation Record Not Found!";
        }
        // get facilitySchedule
        ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(con);
        Hashtable[] fshDtlTB = fshDtl.getFshDtlRsv(rsv_id,root_ent_id);
        //get facilityType ,ftp_parent_type_id = null
        DbFmFacilityType facType = new DbFmFacilityType(con);
        Hashtable[] facTypeTB = facType.getFtp(root_ent_id);
        // proRsvDtlAsXML
        StringBuffer xmlBufRsvDtl = new StringBuffer(1024);
        xmlBufRsvDtl.append(procRsvDtlAsXML(rsvDtlTB));
        xmlBufRsvDtl.append("<fac_total_cost>")
        		    .append(cwUtils.formatNumber(fshDtl.getTotalCost(rsv_id, root_ent_id), 1))
        		    .append("</fac_total_cost>");
        // proFshDtlAsXML
        StringBuffer xmlBufFshList = new StringBuffer(1024);
        xmlBufFshList.append(procFshListAsXML(facTypeTB,fshDtlTB));
        // murge XML
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<reservation id=\"").append(rsv_id).append("\" status=\"")
              .append((String)rsvDtlTB[0].get("rsv_status")).append("\" main_fac_id=\"")
              .append((rsvDtlTB[0].get("rsv_main_fac_id")))
              .append("\">");
        xmlBuf.append(xmlBufFshList);
        xmlBuf.append(xmlBufRsvDtl);
        xmlBuf.append("</reservation>");
        ////
        return xmlBuf;
    }
    /**
     *
     * @param usr_id
     * @param root_ent_id
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    // private String getFshAsXML(String usr_id,int root_ent_id)
    public StringBuffer getFshAsXML(String usr_id,int root_ent_id)
        throws SQLException, cwSysMessage {

        // get facilitySchedule by rsv_id is null,fsh_owner_ent_id,fsh_create_usr_id
        ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(con);
        Hashtable[] fshDtlTB = fshDtl.getFshDtlCart(usr_id,root_ent_id);
        //get facilityType ,ftp_parent_type_id = null
        DbFmFacilityType facType = new DbFmFacilityType(con);
        // facType.ftp_parent_ftp_id is null;
        Hashtable[] facTypeTB = facType.getFtp(root_ent_id);

        // proRsvDtlAsXML
        StringBuffer xmlBufFshList = new StringBuffer(1024);
        xmlBufFshList.append(procFshListAsXML(facTypeTB,fshDtlTB));
        // murge XML

        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<reservation_cart>");
        xmlBuf.append(xmlBufFshList);
        xmlBuf.append("</reservation_cart>");
        ////
        return xmlBuf;
    }

    /**
     *
     * @param usr_id
     * @param root_ent_id
     * @param fsh_fac_id_lst
     * @param fsh_start_time_lst
     * @param fsh_upd_timestamp_lst
     * @throws SQLException
     * @throws cwSysMessage
     */
    // method process the xml

    public void delFshInCart(String usr_id,int root_ent_id,String[] fsh_fac_id_lst,String[] fsh_start_time_lst,String[] fsh_upd_timestamp_lst)
            throws SQLException, cwSysMessage {

        ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(con);
        fshDtl.delFshInCart(usr_id,root_ent_id,fsh_fac_id_lst,fsh_start_time_lst,fsh_upd_timestamp_lst);
    }
    /**
     *
     * @param usr_id
     * @param root_ent_id
     * @param rsv_id
     * @param fsh_fac_id_lst
     * @param fsh_start_time_lst
     * @param fsh_upd_timestamp_lst
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    // prepCancelFshAsXML(String usr_id,int root_ent_id,int rsv_id,String[] fsh_fac_id_lst,String[] fsh_start_time_lst,String[] fsh_upd_timestamp_lst)
    public StringBuffer prepCancelFshAsXML(String usr_id,int root_ent_id,int rsv_id,String[] fsh_fac_id_lst,String[] fsh_start_time_lst,String[] fsh_upd_timestamp_lst)
            throws SQLException, cwSysMessage {
        //---------------------------------------------------------------
        ViewFmFacilitySchedule prepCancelFshDtl = new ViewFmFacilitySchedule(con);
        Hashtable[] prepCancelFshDtlTB = prepCancelFshDtl.prepCancelFsh(usr_id,root_ent_id,rsv_id,fsh_fac_id_lst,fsh_start_time_lst,fsh_upd_timestamp_lst);
        if (prepCancelFshDtlTB.length <= 0 ) {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NONE);
        }
        //get facilityType ,ftp_parent_type_id = null
        DbFmFacilityType facType = new DbFmFacilityType(con);
        // facType.ftp_parent_ftp_id is null;
        Hashtable[] facTypeTB = facType.getFtp(root_ent_id);
        // proFshListAsXML
        StringBuffer xmlBufFshList = new StringBuffer(1024);
        xmlBufFshList.append(procFshListAsXML(facTypeTB,prepCancelFshDtlTB));

        // get reservation by rsv_id,root_ent_id
        ViewFmReservation rsvDtlCancel = new ViewFmReservation(con);
        Hashtable[] rsvDtlCancelTB = rsvDtlCancel.getRsvDtl(rsv_id,root_ent_id);
        // proRsvDtlAsXML
        StringBuffer xmlBufRsvDtl = new StringBuffer(1024);
        if (rsvDtlCancelTB.length > 0) {
            xmlBufRsvDtl.append(procRsvDtlAsXML(rsvDtlCancelTB));
        } else {
            throw new cwSysMessage(SMSG_FMT_GET_RSV_NONE);
        }

        // get Cancel Type List AsXML
        String[] ctb_types = {FM_CANCEL_TYPE};
        StringBuffer xmlBufCancelTypeLst = new StringBuffer(1024);
        // static method getAll(con ,ctb_types) in CodeTable
        xmlBufCancelTypeLst.append("<cancel_type_list>");
        xmlBufCancelTypeLst.append(CodeTable.getAll(con ,ctb_types));
        xmlBufCancelTypeLst.append("</cancel_type_list>");

        // merge XML
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<reservation id=\"").append(rsv_id).append("\" status=\"");
        xmlBuf.append((String)(rsvDtlCancelTB[0].get("rsv_status"))).append("\">");
        xmlBuf.append(xmlBufFshList);
        xmlBuf.append(xmlBufRsvDtl);
        xmlBuf.append("</reservation>");
        xmlBuf.append(xmlBufCancelTypeLst);
        ////
        return xmlBuf;

    }
    /**
     *
     * @param usr_id
     * @param root_ent_id
     * @param rsv_id
     * @param fsh_fac_id_lst
     * @param fsh_start_time_lst
     * @param fsh_upd_timestamp_lst
     * @param cancel_type_id
     * @param cancel_reason
     * @throws SQLException
     * @throws cwSysMessage
     */
    // cancel facilitySchedule
    public void cancelFsh(int root_ent_id,String usr_id,int rsv_id,String[] fsh_fac_id_lst,String[] fsh_start_time_lst,String[] fsh_upd_timestamp_lst,String cancel_type_id,String cancel_reason,boolean link_feedback_ind)
            throws SQLException, cwSysMessage,cwException {
        //
        if ((fsh_fac_id_lst != null)&&(fsh_start_time_lst != null)&&(fsh_upd_timestamp_lst != null)) {
            ViewFmFacilitySchedule cancelFshDtl = new ViewFmFacilitySchedule(con);
            cancelFshDtl.cancelFsh(root_ent_id,usr_id,rsv_id,FSH_STATUS_CANCELLED,fsh_fac_id_lst,fsh_start_time_lst,fsh_upd_timestamp_lst,cancel_type_id,cancel_reason,link_feedback_ind);
        } else {
            throw new cwSysMessage(SMSG_FMT_GET_PARAM_FAIL);
        }
    }
    /**
     *
     * @param usr_id
     * @param root_ent_id
     * @param rsv_id
     * @param rsv_upd_timestamp
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    public StringBuffer prepCancelRsvAsXML(String usr_id,int root_ent_id,int rsv_id,Timestamp rsv_upd_timestamp)
            throws SQLException, cwSysMessage {
        //---------------------------------------------------------------
        //
        // get reservation by rsv_id,root_ent_id
        ViewFmReservation rsvDtl = new ViewFmReservation(con);
        Hashtable[] rsvDtlTB = rsvDtl.prepCancelRsv(usr_id,root_ent_id,rsv_id,rsv_upd_timestamp);
        if (rsvDtlTB.length == 0) {
            throw new cwSysMessage(SMSG_FMT_GET_RSV_NONE);//SMSG_FMT_GET_RSV = "The Reservation Record Not Found!";
        }
        ViewFmFacilitySchedule prepCancelFshDtl = new ViewFmFacilitySchedule(con);
        Hashtable[] prepCancelFshDtlTB = prepCancelFshDtl.getFshDtlRsv(rsv_id,root_ent_id);
        //get facilityType ,ftp_parent_type_id = null
        DbFmFacilityType facType = new DbFmFacilityType(con);
        // facType.ftp_parent_ftp_id is null;
        Hashtable[] facTypeTB = facType.getFtp(root_ent_id);
        // proFshListAsXML
        StringBuffer xmlBufFshList = new StringBuffer(1024);
        xmlBufFshList.append(procFshListAsXML(facTypeTB,prepCancelFshDtlTB));

        // proRsvDtlAsXML
        StringBuffer xmlBufRsvDtl = new StringBuffer(1024);
        xmlBufRsvDtl.append(procRsvDtlAsXML(rsvDtlTB));

        // get Cancel Type List AsXML
        String[] ctb_types = {FM_CANCEL_TYPE};
        StringBuffer xmlBufCancelTypeLst = new StringBuffer(1024);
        // static method getAll(con ,ctb_types) in CodeTable
        xmlBufCancelTypeLst.append("<cancel_type_list>");
        xmlBufCancelTypeLst.append(CodeTable.getAll(con ,ctb_types));
        xmlBufCancelTypeLst.append("</cancel_type_list>");

        // murge XML
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<reservation id=\"").append(rsv_id).append("\" status=\"")
              .append(cwUtils.esc4XML((String)rsvDtlTB[0].get("rsv_status"))).append("\">");
        xmlBuf.append(xmlBufFshList);
        xmlBuf.append(xmlBufRsvDtl);
        xmlBuf.append("</reservation>");
        // add cancel information
        xmlBuf.append(xmlBufCancelTypeLst);
        ////
        return xmlBuf;
    }
    /**
     *
     * @param usr_id
     * @param root_ent_id
     * @param rsv_id
     * @param rsv_upd_timestamp
     * @param cancel_type
     * @param cancel_reason
     * @throws SQLException
     * @throws cwSysMessage
     */
    //cancelRsv(prof.usr_id,root_ent_id,urlp.rsv_id,urlp.rsv_upd_timestamp,urlp.cancel_type_id,urlp.cancel_reason);
    public void cancelRsv(int root_ent_id,String usr_id,int rsv_id,Timestamp rsv_upd_timestamp,String cancel_type,String cancel_reason,boolean link_feedback_ind)
            throws SQLException, cwSysMessage,cwException {
        //
        ViewFmReservation cancelRsvDtl = new ViewFmReservation(con);
        cancelRsvDtl.cancelRsv(root_ent_id,usr_id,rsv_id,RSV_STATUS_CANCEL,FSH_STATUS_CANCELLED,rsv_upd_timestamp,cancel_type,cancel_reason,link_feedback_ind);

    }

    /**
     *
     * @param rsvDtlTB
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    //// public String proRsvDtlAsXML(Hashtable[] rsvDtlTB)
    private StringBuffer procRsvDtlAsXML(Hashtable[] rsvDtlTB)
           throws SQLException, cwSysMessage {
        StringBuffer xmlBufRsvDtl = new StringBuffer(1024);
        if ((rsvDtlTB != null)&&(rsvDtlTB.length > 0)) {
            xmlBufRsvDtl.append("<reservation_details>");
            xmlBufRsvDtl.append("<purpose>").append(cwUtils.esc4XML(rsvDtlTB[0].get("rsv_purpose").toString())).append("</purpose>");
            xmlBufRsvDtl.append("<desc>").append(cwUtils.esc4XML(rsvDtlTB[0].get("rsv_desc").toString())).append("</desc>");
            xmlBufRsvDtl.append("<reserve_user ent_id=\"").append(rsvDtlTB[0].get("rsv_ent_id"));
            xmlBufRsvDtl.append("\" first_name=\"").append(cwUtils.esc4XML(rsvDtlTB[0].get("usr_first_name_bil").toString()));
            xmlBufRsvDtl.append("\" last_name=\"").append(cwUtils.esc4XML(rsvDtlTB[0].get("usr_last_name_bil").toString()));
            xmlBufRsvDtl.append("\" display_name=\"").append(cwUtils.esc4XML(rsvDtlTB[0].get("usr_display_bil").toString())).append("\"/>");
            xmlBufRsvDtl.append("<participant_no>");
            xmlBufRsvDtl.append(rsvDtlTB[0].get("rsv_participant_no"));
            xmlBufRsvDtl.append("</participant_no>");
            xmlBufRsvDtl.append("<cancel_user id=\"").append(rsvDtlTB[0].get("rsv_cancel_usr_id"));
            xmlBufRsvDtl.append("\" timestamp=\"").append(rsvDtlTB[0].get("rsv_cancel_timestamp"));
            xmlBufRsvDtl.append("\" type=\"").append(rsvDtlTB[0].get("rsv_cancel_type"));
            xmlBufRsvDtl.append("\" reason=\"").append(cwUtils.esc4XML(rsvDtlTB[0].get("rsv_cancel_reason").toString()));
            xmlBufRsvDtl.append("\"/> <create_user id=\"").append(rsvDtlTB[0].get("rsv_create_usr_id"));
            xmlBufRsvDtl.append("\" timestamp=\"").append(rsvDtlTB[0].get("rsv_create_timestamp"));
            xmlBufRsvDtl.append("\"/> <update_user id=\"").append(rsvDtlTB[0].get("rsv_upd_usr_id"));
            xmlBufRsvDtl.append("\" timestamp=\"")
            			.append(rsvDtlTB[0].get("rsv_upd_timestamp"))
            			.append("\" first_name=\"")
            			.append(cwUtils.escNull(cwUtils.esc4XML(rsvDtlTB[0].get("usr_first_name_bil").toString())))
            			.append("\" last_name=\"")
            			.append(cwUtils.escNull(cwUtils.esc4XML(rsvDtlTB[0].get("usr_last_name_bil").toString())))
            			.append("\" display_name=\"")
            			.append(cwUtils.escNull(cwUtils.esc4XML(rsvDtlTB[0].get("usr_display_bil").toString())));
            xmlBufRsvDtl.append("\"/></reservation_details>");
        } else {
            throw new cwSysMessage(SMSG_FMT_GET_RSV_NONE);
        }

        return xmlBufRsvDtl;
    }
    /**
     *
     * @param facTypeTB
     * @param fshDtlTB
     * @return
     */
    // procFshListAsXML(Hashtable[] facTypeTB,Hashtable[] fshDtlTB)
    private StringBuffer procFshListAsXML(Hashtable[] facTypeTB,Hashtable[] fshDtlTB)
           throws cwSysMessage {
        StringBuffer xmlFshList = new StringBuffer(1024);
        xmlFshList.append("<facility_schedule_list>");
        int ftp_id = 0;
        int fac_ftp_id = 0;
        String ftp_main_status;
        //int i = 0;
        //int j = 0;
        if ((facTypeTB != null)&&(facTypeTB.length > 0)) {

            for (int j = 0; j < facTypeTB.length; j++) {
                ftp_id = ((Integer)facTypeTB[j].get("ftp_id")).intValue();
                ftp_main_status = (((Integer)facTypeTB[j].get("ftp_main_indc")).equals(new Integer(1)))?"YES":"NO";
                xmlFshList.append("<facility_type id=\"").append(ftp_id)
                          .append("\" main=\"").append(ftp_main_status)
                          .append("\" xsl_prefix=\"").append(cwUtils.esc4XML(facTypeTB[j].get("ftp_xsl_prefix").toString())).append("\">")
                          .append(facTypeTB[j].get("ftp_title_xml"));
                if ((fshDtlTB != null)&&(fshDtlTB.length > 0)) {
                    for (int i = 0; i < fshDtlTB.length; i++) {
                        fac_ftp_id = ((Integer)(fshDtlTB[i].get("fac_ftp_id"))).intValue();
                        if (fac_ftp_id == ftp_id) {
                        xmlFshList.append("<facility_schedule date=\"").append(fshDtlTB[i].get("fsh_date"))
                                  .append("\" start_time=\"").append(fshDtlTB[i].get("fsh_start_time"))
                                  .append("\" end_time=\"").append(fshDtlTB[i].get("fsh_end_time"))
                                  .append("\" status=\"").append(fshDtlTB[i].get("fsh_status")).append("\">")
                                  ;
                        xmlFshList.append("<facility id=\"").append(fshDtlTB[i].get("fac_id")).append("\">")
                                  .append("<basic>").append("<title>").append(cwUtils.esc4XML(fshDtlTB[i].get("fac_title").toString()))
                                  .append("</title>").append("<desc>").append(cwUtils.esc4XML(fshDtlTB[i].get("fac_desc").toString()))
                                  .append("</desc>").append("</basic>").append("</facility>")
                                  ;
                        xmlFshList.append("<update_user id=\"").append(fshDtlTB[i].get("fsh_upd_usr_id"))
                                  .append("\" timestamp=\"").append(fshDtlTB[i].get("fsh_upd_timestamp"))
                                  .append("\"/>")
                                  ;
                        xmlFshList.append("<cancel_user id=\"").append(fshDtlTB[i].get("fsh_cancel_usr_id"))
                                  .append("\" timestamp=\"").append(fshDtlTB[i].get("fsh_cancel_timestamp"))
                                  .append("\" type=\"").append(fshDtlTB[i].get("fsh_cancel_type"))
                                  .append("\" reason=\"").append(cwUtils.esc4XML(fshDtlTB[i].get("fsh_cancel_reason").toString()))
                                  .append("\"/>")
                                  .append("</facility_schedule>")
                                  ;
                        } else {
                            //i--;
                            //break;
                        }
                    }
                }
            xmlFshList.append("</facility_type>");
            }
        } else {
            //throw new cwSysMessage(SMSG_FMT_GET_FTP_NONE);
        }
        xmlFshList.append("</facility_schedule_list>");
        return xmlFshList;
    }
    //---------------------------------------------------------------------------------------------------
    // RsvRecordManager methods here
    public String getRsvRecordList(Hashtable param) throws SQLException, cwSysMessage, cwException {
        ViewFmReservation rsvRecord = new ViewFmReservation(this.con);

        // current user info
        int owner_ent_id = ((Integer)param.get("owner_ent_id")).intValue();
        String usr_id = (String)param.get("user_id");
        int usr_ent_id = ((Integer)param.get("user_ent_id")).intValue();

        Timestamp start = (Timestamp)param.get("start_date");
        Timestamp end = (Timestamp)param.get("end_date");

        String own_type = null;
        if (param.get("own_type") != null)
            own_type = (String)param.get("own_type");

        // parse the facility id
        String[] fac_id_str = (String[])param.get("fac_id");
        int[] fac_id = new int[fac_id_str.length];
        for (int i = 0; i < fac_id_str.length; i++) {
            fac_id[i] = Integer.parseInt(fac_id_str[i]);
        }
        String[] status = null;
        if (param.get("status") != null)
            status = (String[])param.get("status");
        else {
            status = new String[2];
            status[0] = this.FSH_STATUS_RESERVED;
            status[1] = this.FSH_STATUS_PENCILLED_IN;
        }

        Hashtable[] result = rsvRecord.getRecord(owner_ent_id, usr_id, usr_ent_id, start, end, own_type, fac_id, status);
        return this.recordListAsXML(start.toString(), end.toString(), result);
    }

    public String getRsvRecordListParam(Hashtable param) {
        StringBuffer result = new StringBuffer();
        result.append("<param");
        result.append(" start_date=\"").append(((Timestamp)param.get("start_date")).toString());
        result.append("\" end_date=\"").append(((Timestamp)param.get("end_date")).toString());
        result.append("\" own_type=\"").append(cwUtils.escNull(param.get("own_type")));
        result.append("\" status=\"").append(cwUtils.escNull(param.get("param_status")));
        result.append("\" fac_id=\"").append(param.get("param_fac_id"));
        result.append("\"/>");

        return result.toString();
    }

    public String getCalendarList(Hashtable param) throws SQLException, cwSysMessage, cwException {
        ViewFmReservation rsvRecord = new ViewFmReservation(this.con);

        // current user info
        int owner_ent_id = ((Integer)param.get("owner_ent_id")).intValue();
        String usr_id = (String)param.get("user_id");
        int usr_ent_id = ((Integer)param.get("user_ent_id")).intValue();

        Timestamp start = (Timestamp)param.get("start_date");
        Timestamp end = (Timestamp)param.get("end_date");

        // parse the facility id
        int i = 0;
        String[] fac_id_str = null;
        int[] fac_id;
        if (param.get("fac_id") != null) {
            fac_id_str = (String[])param.get("fac_id");
            fac_id = new int[fac_id_str.length];
            for (i = 0; i < fac_id_str.length; i++) {
                fac_id[i] = Integer.parseInt(fac_id_str[i]);
            }
        } else
            fac_id = new int[0];

        int[] ext_fac_id;
        // parse the external facility id
        if (param.get("ext_fac_id") != null) {
            fac_id_str = (String[])param.get("ext_fac_id");
            ext_fac_id = new int[fac_id_str.length];
            for (i = 0; i < fac_id_str.length; i++)
                ext_fac_id[i] = Integer.parseInt(fac_id_str[i]);
        } else
            ext_fac_id = new int[0];
        if (this.DEBUG)
        	CommonLog.debug(String.valueOf(ext_fac_id.length));

        String[] status = new String[2];
        status[0] = this.FSH_STATUS_RESERVED;
        status[1] = this.FSH_STATUS_PENCILLED_IN;
        Vector scheduleList = rsvRecord.getCalendar(owner_ent_id, start, end, fac_id, ext_fac_id, status);

        int[] time = new int[2];
        this.slots = rsvRecord.getSlotValue(owner_ent_id);
        time = rsvRecord.getLimitTime(owner_ent_id);

        if (time == null || this.slots == null)
            throw new cwSysMessage(ViewFmReservation.SMSG_FMT_GET_TIME_NONE);

        this.minGap = time[0];
        this.maxScheduleCount = this.day / time[1];

        Timestamp[] dateList = this.getDays(start, end);
        if (this.DEBUG)
        	CommonLog.debug(String.valueOf(dateList.length));
        return this.calendarAsXML(start, end, fac_id, ext_fac_id, dateList,
                                  this.checkAvailability(dateList, fac_id, ext_fac_id, scheduleList));
    }

    /**
     * generate the xml file for the reservation record
     * @param:    Hashtable[]    -    the reservation record list
     * @return:    String        -    the xml file
     */
    private String recordListAsXML(String start, String end, Hashtable[] source)
        throws SQLException {
        if (source == null)
            return null;

        ViewFmReservation rsvRecord = new ViewFmReservation(this.con);
        StringBuffer result = new StringBuffer("<reservation_record>");
        result.append("<date_range from=\"").append(start).append("\" to=\"").append(end).append("\"/>");
        result.append("<facility_schedule_list>");
        for (int i = 0; i < source.length; i++) {
            // facility schedule
            result.append("<facility_schedule date=\"");
            result.append(((Timestamp)source[i].get("fsh_date")).toString());
            result.append("\" start_time=\"");
            result.append(((Timestamp)source[i].get("fsh_start_time")).toString());
            result.append("\" end_time=\"");
            result.append(((Timestamp)source[i].get("fsh_end_time")).toString());
            result.append("\" status=\"");
            result.append(((String)source[i].get("fsh_status")).toUpperCase());
            result.append("\">");
            // facility information
            result.append("<facility id=\"");
            result.append(((Integer)source[i].get("fac_id")).toString());
            result.append("\"><basic><title>");
            result.append(cwUtils.esc4XML((String)source[i].get("fac_title"))).append("</title>");
            if (source[i].containsKey("fac_fee")) {
            	result.append("<fac_fee>").append(cwUtils.formatNumber(((Double)source[i].get("fac_fee")).doubleValue(), 1)).append("</fac_fee>");
            }
            result.append("</basic></facility>");
            // reservation information, MAY BE NULL!
            if (source[i].get("rsv_ent_id") != null) {
                result.append("<reservation id=\"");
                result.append(((Integer)source[i].get("rsv_id")).toString());
                result.append("\" status=\"");
                result.append(((String)source[i].get("rsv_status")).toUpperCase());
                result.append("\"><reservation_details><purpose>");
                result.append(cwUtils.esc4XML((String)source[i].get("rsv_purpose")));
                result.append("</purpose><reserve_user ent_id=\"");
                // the user name reserved for
                result.append(((Integer)source[i].get("rsv_ent_id")).toString());
                result.append("\" first_name=\"");
                result.append(cwUtils.esc4XML(cwUtils.escNull(source[i].get("first_name"))));
                result.append("\" last_name=\"");
                result.append(cwUtils.esc4XML(cwUtils.escNull(source[i].get("last_name"))));
                result.append("\" display_name=\"");
                result.append(cwUtils.esc4XML(cwUtils.escNull(source[i].get("display_name"))));
                result.append("\"/><participant_no>");
                result.append(((Integer)source[i].get("rsv_participant_no")).toString());
                result.append("</participant_no></reservation_details></reservation>");
            }
            // cancel information
            result.append("<cancel_user id=\"").append(cwUtils.esc4XML(cwUtils.escNull((String)source[i].get("fsh_cancel_usr_id"))));
            result.append("\" timestamp=\"");
            if (source[i].get("fsh_cancel_timestamp") != null)
                result.append(((Timestamp)source[i].get("fsh_cancel_timestamp")).toString());
            result.append("\" type=\"").append(cwUtils.esc4XML(cwUtils.escNull((String)source[i].get("fsh_cancel_type"))));
            result.append("\" reason=\"").append(cwUtils.esc4XML(cwUtils.escNull((String)source[i].get("fsh_cancel_reason"))));
            // create user & create timestamp
            result.append("\"/><create_user id=\"").append(cwUtils.esc4XML(cwUtils.escNull((String)source[i].get("fsh_create_usr_id"))));
            result.append("\" timestamp=\"").append(((Timestamp)source[i].get("fsh_create_timestamp")).toString());
            // last update user & last update timestamp
            result.append("\"/><update_user id=\"").append(cwUtils.esc4XML(cwUtils.escNull((String)source[i].get("fsh_create_usr_id"))));
            result.append("\" timestamp=\"").append(((Timestamp)source[i].get("fsh_create_timestamp")).toString());
            result.append("\"/></facility_schedule>");
        }
        result.append("</facility_schedule_list>");
        result.append("</reservation_record>");

        if (this.DEBUG)
        	CommonLog.debug(result.toString());
        return result.toString();
    }

    /**
     * get the days of the week
     */
    private Timestamp[] getDays(Timestamp start, Timestamp end) {
        Vector dateList = new Vector();
        Calendar day = Calendar.getInstance();
        day.setTime(start);
        int count = 0;

        while (!(new Timestamp(day.getTime().getTime())).after(end)) {
            dateList.addElement(new Timestamp(day.getTime().getTime()));
            day.add(Calendar.DATE, 1);
            count++;
        }

        this.period = count / this.dayNum;
        Timestamp[] resultArray = new Timestamp[dateList.size()];
        for(int i=0; i<resultArray.length; i++) {
        	resultArray[i] = (Timestamp)dateList.get(i);
        }
        return resultArray;
    }

    private FMDateStatus[][] checkAvailability(Timestamp[] dateList, int[] idList,
                                             int[] extList, Vector source) {
        /**
         * check the status for all retrieved records
         * and put them into an array[x][y] of DateStatus
         * x    -    facility_id
         * y    -    facility_schedule date
         */
        FMDateStatus[][] status = new FMDateStatus[idList.length+extList.length][dateList.length];
        Vector records = new Vector(this.maxScheduleCount);
        Timestamp date = null;
        int fac_id = 0, id = 0;
        int i = 0, j = 0, x = 0, y = 0, index = 0, k = 0;
        FMDateStatus dateStatus = null;
        String[] availability = new String[3];
        String facTitle = null, facStatus = null;

        boolean[] flag;
        // go through each facility
        for (i = 0; i < idList.length + extList.length; i++) {
            index = 0;
            if (i < idList.length)
                id = idList[i];
            else
                id = extList[i - idList.length];

            // clear the variable
            flag = new boolean[source.size()];
            for (k = 0; k < source.size(); k++)
                flag[k] = false;
//            facTitle = facStatus = null;

            // go through each date
            for (j = 0; j < dateList.length; j++) {
                // go through each record
                for (k = index; k < source.size() + 1; k++) {
                    if (k < source.size()) {
                        date = (Timestamp)((Hashtable)source.elementAt(k)).get("fsh_date");
                        fac_id = ((Integer)((Hashtable)source.elementAt(k)).get("fac_id")).intValue();
                        if (j == 0) {
                            facTitle = (String)((Hashtable)source.elementAt(k)).get("fac_title");
                            facStatus = (String)((Hashtable)source.elementAt(k)).get("fac_status");
                        }
                    } else {
                        date = null;
                        fac_id = 0;
                    }

                    if (j == 0 && id == fac_id)
                        index = k;

                    if (j == 0 && id != fac_id && records.size() == 0) {
//                        facTitle = facStatus = null;
                        continue;
                    }
                    if (!dateList[j].equals(date) || id != fac_id) {
                        if (records.size() != 0) {
                            availability = this.check(records);
                            dateStatus = new FMDateStatus();
//                            dateStatus.setTitle(facTitle);
//                            dateStatus.setStatus(facStatus);
                            dateStatus.setTitle((String)((Hashtable)source.elementAt(k-1)).get("fac_title"));
                            dateStatus.setStatus((String)((Hashtable)source.elementAt(k-1)).get("fac_status"));
                            dateStatus.setSlot1(availability[0]);
                            dateStatus.setSlot2(availability[1]);
                            dateStatus.setSlot3(availability[2]);
                            // add to facility list
                            status[x][y] = dateStatus;
                            records.removeAllElements();
                            if (k >= source.size() || k >= index) {
                                facTitle = (String)((Hashtable)source.elementAt(k-1)).get("fac_title");
                                facStatus = (String)((Hashtable)source.elementAt(k-1)).get("fac_status");
                                date = (Timestamp)((Hashtable)source.elementAt(k-1)).get("fsh_date");
                            } else {
                                facTitle = (String)((Hashtable)source.elementAt(k)).get("fac_title");
                                facStatus = (String)((Hashtable)source.elementAt(k)).get("fac_status");
                                date = (Timestamp)((Hashtable)source.elementAt(k)).get("fsh_date");
                            }
                        } else {
                            // no facility schedule in this day
                            dateStatus = new FMDateStatus();
                            dateStatus.setTitle(facTitle);
                            dateStatus.setStatus(facStatus);
                            dateStatus.setSlot1(this.SLOT_AVAILABLE);
                            dateStatus.setSlot2(this.SLOT_AVAILABLE);
                            dateStatus.setSlot3(this.SLOT_AVAILABLE);
                            // add to facility list
                            status[i][j] = dateStatus;
                        }
                        break;
                    } else {
                        records.addElement(source.elementAt(k));
                        flag[k] = true;
                        x = i;
                        y = j;
                        index = k + 1;
                    }
                }
                // current k = source.size()+1
                if (records.size() != 0) {
                    availability = this.check(records);
                    dateStatus = new FMDateStatus();
//                    dateStatus.setTitle(facTitle);
//                    dateStatus.setStatus(facStatus);
                    dateStatus.setTitle((String)((Hashtable)source.elementAt(k-2)).get("fac_title"));
                    dateStatus.setStatus((String)((Hashtable)source.elementAt(k-2)).get("fac_status"));
                    dateStatus.setSlot1(availability[0]);
                    dateStatus.setSlot2(availability[1]);
                    dateStatus.setSlot3(availability[2]);
                    // add to facility list
                    status[x][y] = dateStatus;
                    records.removeAllElements();
                }

                if (index == k && date == null && j == 0) {
                    flag[k] = true;
                    index = k + 1;
                }
                k = source.size() - 1;
                while (k >= 0 && j == dateList.length - 1) {
                    if (flag[k])
                        source.removeElementAt(k);
                    k = k - 1;
                }
            }
        }
        return status;
    }

    /**
     * check schedule
     */
    private String[] check(Vector scheduleList) {
        /**
         * assume:
         * facility_schedule.start_time    < facility_schedule.end_time
         * system_slot.start_time        < system_slot.end_time
         * slot[0]    -    slot label
         * slot[1]    -    start time
         * slot[2]    -    end time
         */
        String[] result = new String[3];
        boolean hasRecord    = false;
        String slotStatus = this.SLOT_AVAILABLE;

        Hashtable record = null;
        String prevStart = null, prevEnd = null;
        String curStart = null, curEnd = null;

        for (int i = 0; i < this.slots.length; i++) {
            prevStart = this.slots[i][1];
            prevEnd = this.slots[i][2];
            hasRecord = false;
            for (int j = 0; j < scheduleList.size(); j++) {
                record = (Hashtable)scheduleList.elementAt(j);
                curStart = ((Timestamp)record.get("fsh_start_time")).toString().substring(11);
                curEnd = ((Timestamp)record.get("fsh_end_time")).toString().substring(11);
                if (this.slots[i][1].compareTo(curEnd) >= 0 ||
                    this.slots[i][2].compareTo(curStart) <= 0)
                    continue;
                else {
                    if (this.slots[i][1].compareTo(curStart) > 0)
                        curStart = this.slots[i][1];
                    if (this.slots[i][2].compareTo(curEnd) < 0)
                        curEnd = this.slots[i][2];
                    // check the time gap
                    if (hasRecord)
                        slotStatus = this.checkGap(prevEnd, curStart);
                    else
                        slotStatus = this.checkGap(this.slots[i][1], curStart);
                    hasRecord = true;

                    prevStart = curStart;
                    prevEnd = curEnd;
                }
                // there is at least 1-hour gap
                if (slotStatus.equals(this.SLOT_PART)) {
                    result[i] = slotStatus;
                    break;
                }
                if (result[i] == null ||
                    result[i].equals(this.SLOT_FULL) && slotStatus.equals(this.SLOT_ALMOSTFULL))
                    result[i] = slotStatus;
            }
            /**
             * handle the last record
             */
            if (hasRecord && !slotStatus.equals(this.SLOT_PART)) {
                slotStatus = this.checkGap(prevEnd, this.slots[i][2]);
                if (slotStatus.equals(this.SLOT_PART) ||
                    result[i].equals(this.SLOT_FULL) && slotStatus.equals(this.SLOT_ALMOSTFULL))
                    result[i] = slotStatus;
            }
            if (!hasRecord)
                result[i] = this.SLOT_AVAILABLE;
        }

        return result;
    }

    private String checkGap(String prevEnd, String curStart) {
        long pe = 0, cs = 0;

        prevEnd = "1970-01-01 " + prevEnd;
        curStart = "1970-01-01 " + curStart;
        pe = Timestamp.valueOf(prevEnd).getTime();
        cs = Timestamp.valueOf(curStart).getTime();
        long gap = cs - pe;

        if (gap > 0 && gap < this.minGap)
            return this.SLOT_ALMOSTFULL;
        if (gap == 0)
            return this.SLOT_FULL;
        if (gap >= this.minGap)
            return this.SLOT_PART;

        // if arrive here, there must be conflict
        return this.SLOT_FULL;
    }

    /**
     * generate the xml file for the reservation record
     * @param:    Hashtable[]    -    the reservation record list
     * @return:    String        -    the xml file
     */
    private String calendarAsXML(Timestamp start, Timestamp end, int[] facList, int[] extList,
    		Timestamp[] dateList, FMDateStatus[][] source) {
        if (source == null)
            return null;

        Calendar day = Calendar.getInstance();

        StringBuffer result = new StringBuffer("<calendar>");
        // the last week
        day.setTime(start);
        day.add(Calendar.WEEK_OF_YEAR, -1);
        Timestamp tmp = new Timestamp(day.getTime().getTime());
        result.append("<date_range last_week=\"").append(tmp.toString());
        result.append("\" from=\"").append(start.toString());
        result.append("\" to=\"").append(end.toString());
        // the next week
        day.setTime(end);
        day.add(Calendar.WEEK_OF_YEAR, 1);
        tmp = new Timestamp(day.getTime().getTime());
        result.append("\" next_week=\"").append(tmp.toString());
        // period
        result.append("\" period=\"").append(new Integer(this.period).toString()).append("\"/>");
        // today
        try {
            tmp = cwSQL.getTime(this.con);
            tmp.setHours(0);
            tmp.setMinutes(0);
            tmp.setSeconds(0);
            tmp.setNanos(0);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }

        for (int i = 0; i < this.period; i++) {
            result.append("<facility_usage_list>");

            int facCount = 0, dateCount = this.dayNum * i;

            // day of the week
            result.append("<week>");
            for (int j = dateCount; j < dateCount + this.dayNum; j++) {
                result.append("<day today=\"");
                if (tmp.toString().equals(dateList[j]))
                    result.append(this.YES);
                else
                    result.append(this.NO);
                result.append("\">").append(dateList[j]).append("</day>");
            }
            result.append("</week>");

            // availability
            for (facCount = 0; facCount < facList.length; facCount++) {
                result.append("<facility_availability>");
                result.append("<facility id=\"").append(new Integer(facList[facCount]).toString());
                result.append("\" status=\"").append(source[facCount][0].getStatus());
                result.append("\" title=\"").append(cwUtils.esc4XML(source[facCount][0].getTitle()));
                result.append("\"/>");
                for (int j = dateCount; j < dateCount + this.dayNum; j++) {
                    result.append("<day_usage slot_").append(this.slots[0][0]).append("=\"");
                    result.append(source[facCount][j].getSlot1());
                    result.append("\" slot_").append(this.slots[1][0]).append("=\"");
                    result.append(source[facCount][j].getSlot2());
                    result.append("\" slot_").append(this.slots[2][0]).append("=\"");
                    result.append(source[facCount][j].getSlot3());
                    result.append("\"/>");
                }
                result.append("</facility_availability>");
            }
            boolean existed = false;
            for (; facCount < facList.length + extList.length; facCount++) {
                // if there are schedule with the special external facility
                existed = false;
                for (int j = dateCount; j < dateCount + this.dayNum; j++) {
                    if (!source[facCount][j].getSlot1().equals(this.SLOT_AVAILABLE) ||
                        !source[facCount][j].getSlot2().equals(this.SLOT_AVAILABLE) ||
                        !source[facCount][j].getSlot3().equals(this.SLOT_AVAILABLE)
                        ) {
                        existed = true;
                        break;
                    }
                }

                if (existed) {
                    result.append("<facility_availability>");
                    result.append("<facility id=\"").append(extList[facCount-facList.length]);
                    result.append("\" status=\"").append(source[facCount][0].getStatus());
                    result.append("\" title=\"").append(cwUtils.esc4XML(source[facCount][0].getTitle()));
                    result.append("\"/>");
                    for (int j = dateCount; j < dateCount + this.dayNum; j++) {
                        result.append("<day_usage slot_").append(this.slots[0][0]).append("=\"");
                        result.append(source[facCount][j].getSlot1());
                        result.append("\" slot_").append(this.slots[1][0]).append("=\"");
                        result.append(source[facCount][j].getSlot2());
                        result.append("\" slot_").append(this.slots[2][0]).append("=\"");
                        result.append(source[facCount][j].getSlot3());
                        result.append("\"/>");
                    }
                    result.append("</facility_availability>");
                }
            }
            result.append("</facility_usage_list>");
        }
        result.append("</calendar>");

        if (this.DEBUG){
        	 CommonLog.debug(result.toString());
        }
        return result.toString();
    }

    /* DENNIS BEGIN */
    public static final String QUERY_ACTION_RESERVE = "reserve";
    public static final String QUERY_ACTION_SEARCH = "search";
    public static final String QUERY_ACTION_CHECK = "check";

    /**
    Generate facility schedule objects according to the input information.<BR>
    A facility schedule object is generated from the 4 input lists' elements
    having the same index.  Therefore the 4 input list should have the same length.
    @return Vector of ViewFmFacilitySchedule
    */
    public Vector generateFsh4Query(int[] fac_id_lst, Timestamp[] fsh_date_lst,
                               Timestamp[] fsh_start_time_lst,
                               Timestamp[] fsh_end_time_lst,
                               int rsv_id,
                               String usr_id, int owner_ent_id)
                               throws SQLException {

        Vector v_fshDtl = new Vector();
        if(fac_id_lst != null && fsh_date_lst != null && fsh_start_time_lst != null && fsh_end_time_lst != null) {
            Timestamp cur_time = cwSQL.getTime(this.con);
            for(int i=0; i<fac_id_lst.length; i++) {
                ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(this.con);
                fshDtl.setFsh_fac_id(fac_id_lst[i]);
                fshDtl.setFsh_start_time(fsh_start_time_lst[i]);
                fshDtl.setFsh_end_time(fsh_end_time_lst[i]);
                fshDtl.setFsh_date(fsh_date_lst[i]);
                fshDtl.setFsh_owner_ent_id(owner_ent_id);
                fshDtl.setFsh_create_usr_id(usr_id);
                fshDtl.setFsh_create_timestamp(cur_time);
                fshDtl.setFsh_upd_usr_id(usr_id);
                fshDtl.setFsh_upd_timestamp(cur_time);
                fshDtl.setFsh_status(FSH_STATUS_RESERVED);
                fshDtl.setFsh_rsv_id(rsv_id);
                v_fshDtl.addElement(fshDtl);
            }
        }
        ViewFmFacilitySchedule fshSorter = new ViewFmFacilitySchedule(this.con);
        return fshSorter.orderByFacTitle(v_fshDtl);
    }

    /**
    Generate facility schedule objects according to the input information.<BR>
    A facility schedule object is generated on each facility, for each date
    between fsh_start_date, fsh_end_date inclusively, from fsh_start_time to
    fsh_end_time.
    @return Vector of ViewFmFacilitySchedule
    */
    public Vector generateFsh4Query(int[] fac_id_lst,
                               Timestamp fsh_start_date,
                               Timestamp fsh_end_date,
                               Timestamp fsh_start_time,
                               Timestamp fsh_end_time,
                               int rsv_id,
                               String usr_id, int owner_ent_id)
                               throws SQLException {

        Vector v_fshDtl = new Vector();
        if(fac_id_lst != null && fsh_start_date != null && fsh_end_date != null
           && fsh_start_time != null && fsh_end_time != null) {
            Vector v_fsh_date = getDaysBetween(fsh_start_date, fsh_end_date);
            Timestamp cur_time = cwSQL.getTime(this.con);
            for(int i=0; i<fac_id_lst.length; i++) {
                for(int j=0; j<v_fsh_date.size(); j++) {
                    ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(this.con);
                    fshDtl.setFsh_fac_id(fac_id_lst[i]);
                    Timestamp date = (Timestamp)v_fsh_date.elementAt(j);
                    fshDtl.setFsh_date(date);
                    Timestamp start_time = new Timestamp(date.getTime());
                    start_time.setHours(fsh_start_time.getHours());
                    start_time.setMinutes(fsh_start_time.getMinutes());
                    start_time.setSeconds(fsh_start_time.getSeconds());
                    start_time.setNanos(fsh_start_time.getNanos());
                    fshDtl.setFsh_start_time(start_time);
                    Timestamp end_time = new Timestamp(date.getTime());
                    end_time.setHours(fsh_end_time.getHours());
                    end_time.setMinutes(fsh_end_time.getMinutes());
                    end_time.setSeconds(fsh_end_time.getSeconds());
                    end_time.setNanos(fsh_end_time.getNanos());
                    fshDtl.setFsh_end_time(end_time);
                    fshDtl.setFsh_owner_ent_id(owner_ent_id);
                    fshDtl.setFsh_create_usr_id(usr_id);
                    fshDtl.setFsh_create_timestamp(cur_time);
                    fshDtl.setFsh_upd_usr_id(usr_id);
                    fshDtl.setFsh_upd_timestamp(cur_time);
                    fshDtl.setFsh_status(FSH_STATUS_RESERVED);
                    fshDtl.setFsh_rsv_id(rsv_id);
                    v_fshDtl.addElement(fshDtl);
                }
            }
        }
        ViewFmFacilitySchedule fshSorter = new ViewFmFacilitySchedule(this.con);
        return fshSorter.orderByFacTitle(v_fshDtl);
    }

    /**
    Get the days between input start_date and end_date inclusively.<BR>
    @return Vector of Timestamp. A empty Vector will be returned if start_date < end_date
    */
    private static Vector getDaysBetween(Timestamp start_date, Timestamp end_date) {
        Vector v_days = new Vector();
        if(!start_date.after(end_date)) {
            Timestamp temp;
            Calendar c = Calendar.getInstance();
            c.setTime(start_date);
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND , 0);
            c.set(Calendar.MILLISECOND , 0);
            c.set(Calendar.AM_PM, Calendar.AM);
            temp = new Timestamp(c.getTime().getTime());
            while(temp.before(end_date) || temp.equals(end_date)) {
                v_days.addElement(temp);
                c.add(Calendar.DATE, 1);
                temp = new Timestamp(c.getTime().getTime());
            }
        }
        return v_days;
    }

    /**
    Make query (check conflict) on the input facility schedule information.<BR>
    @param action QUERY_ACTION_RESERVE or QUERY_ACTION_SEARCH or QUERY_ACTION_CHECK
    @param rsv_id reservation id which this manager is working on, if rsv_id > 0. Else this manager is working on a new reservation.
    @param usr_id in the loginProfile
    @param organization entity of the user
    @return XML showing details of facility schedule,
    or null if action = QUERY_ACTION_RESERVE and all facility schedules has no conflict
    */
    public String queryFsh(String action, int[] fac_id_lst,
                           Timestamp[] fsh_date_lst,
                           Timestamp[] fsh_start_time_lst,
                           Timestamp[] fsh_end_time_lst,
                           int rsv_id,
                           String usr_id, int owner_ent_id)
                           throws SQLException, cwException {
        String xml = null;
        if(action != null && (action.equalsIgnoreCase(QUERY_ACTION_RESERVE)
           || action.equalsIgnoreCase(QUERY_ACTION_SEARCH)
           || action.equalsIgnoreCase(QUERY_ACTION_CHECK))) {

            //format the input info into instances of ViewFmFacilitySchedule
            Vector v_fshDtl = generateFsh4Query(fac_id_lst, fsh_date_lst, fsh_start_time_lst, fsh_end_time_lst, rsv_id, usr_id, owner_ent_id);
            xml = queryFsh(action, v_fshDtl, rsv_id);
        }
        return xml;
    }

    /**
    Make query (check conflict) on the input facility schedule information.<BR>
    @param action QUERY_ACTION_RESERVE or QUERY_ACTION_SEARCH or QUERY_ACTION_CHECK
    @param rsv_id reservation id which this manager is working on, if rsv_id > 0. Else this manager is working on a new reservation.
    @param usr_id in the loginProfile
    @param organization entity of the user
    @return XML showing details of facility schedule,
    or null if action = QUERY_ACTION_RESERVE and all facility schedules has no conflict
    */
    public String queryFsh(String action, int[] fac_id_lst,
                           Timestamp fsh_start_date,
                           Timestamp fsh_end_date,
                           Timestamp fsh_start_time,
                           Timestamp fsh_end_time,
                           int rsv_id,
                           String usr_id, int owner_ent_id)
                           throws SQLException, cwException {
        String xml = null;
        if(action != null && (action.equalsIgnoreCase(QUERY_ACTION_RESERVE)
           || action.equalsIgnoreCase(QUERY_ACTION_SEARCH)
           || action.equalsIgnoreCase(QUERY_ACTION_CHECK))) {

            //format the input info into instances of ViewFmFacilitySchedule
            Vector v_fshDtl = generateFsh4Query(fac_id_lst, fsh_start_date, fsh_end_date, fsh_start_time, fsh_end_time, rsv_id, usr_id, owner_ent_id);
            xml = queryFsh(action, v_fshDtl, rsv_id);
        }
        return xml;
    }

    /**
    Make query on the facility schedules in the input Vector
    @return XML showing details of facility schedules,
    or null if action = QUERY_ACTION_RESERVE and all facility schedules has no conflict
    */
    private String queryFsh(String action, Vector v_fshDtl, int rsv_id) throws SQLException, cwException {
        String xml = null;
        //lock table fmFacilityScheule
        ViewFmFacilitySchedule fshLock = new ViewFmFacilitySchedule(this.con);
        fshLock.lockTable();
        //make the query according to action
        if(action.equalsIgnoreCase(QUERY_ACTION_RESERVE)) {
            xml = reserveFsh(v_fshDtl, rsv_id);
        } else if(action.equalsIgnoreCase(QUERY_ACTION_SEARCH)) {
            xml = getNonConflictFshInVectorAsXML(v_fshDtl, rsv_id);
        } else if(action.equalsIgnoreCase(QUERY_ACTION_CHECK)) {
            xml = getAllFshNConflictInVectorAsXML(v_fshDtl, rsv_id);
        }
        return xml;
    }

    /**
    From the input Vector, if all facility schedule has no conflicts, save all of them to database.
    else vendor XML showing all facility schedules and their conflicts
    @return if not all facility schedules have no conflict, return xml showing all facility schedules
            and their conflicts; else return null
    */
    private String reserveFsh(Vector v_fshDtl, int rsv_id) throws SQLException, cwException {
        String xml = null;
        if(v_fshDtl != null) {
            //check if all facility schedules has no conflict
            boolean allNoConflict = true;   //indicate if all facility schedules in v_fshDtl have no conflict
            for(int i=0; i<v_fshDtl.size(); i++) {
                ViewFmFacilitySchedule fshDtl = (ViewFmFacilitySchedule)v_fshDtl.elementAt(i);
                if(fshDtl.hasConflict(FSH_STATUS_CANCELLED)) {
                    allNoConflict = false;
                    break;
                }
            }
            if(allNoConflict) {
                //save all facility schedule to database
                for(int i=0; i<v_fshDtl.size(); i++) {
                    ViewFmFacilitySchedule fshDtl = (ViewFmFacilitySchedule)v_fshDtl.elementAt(i);
                    if(rsv_id > 0) {
                        fshDtl.setFsh_rsv_id(rsv_id);
                    }
                    fshDtl.ins();
                }
            } else {
                //else vendor xml for all facility schedules and their conflicts
                xml = getAllFshNConflictInVectorAsXML(v_fshDtl, rsv_id);
            }
        }
        return xml;
    }

    /**
    From the input Vector, vendor XML showing facility schedules have no conflict
    */
    private String getNonConflictFshInVectorAsXML(Vector v_fshDtl, int rsv_id) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        //xmlBuf.append("<facility_availability>");
        xmlBuf.append("<facility_availability");
        if(rsv_id > 0) {
            xmlBuf.append(" rsv_id=\"").append(rsv_id).append("\"");
        }
        xmlBuf.append(">");
        xmlBuf.append("<facility_schedule_list>");
        if(v_fshDtl != null) {
            for(int i=0; i<v_fshDtl.size(); i++) {
                ViewFmFacilitySchedule fshDtl = (ViewFmFacilitySchedule) v_fshDtl.elementAt(i);
                if(!fshDtl.hasConflict(FSH_STATUS_CANCELLED)) {
                    xmlBuf.append(getFshNConflictAsXML(fshDtl));
                }
            }
        }
        xmlBuf.append("</facility_schedule_list>");
        xmlBuf.append("</facility_availability>");
        return xmlBuf.toString();
    }

    /**
    From the input Vector, vendor XML showing all facility schedules and their conflicts
    */
    private String getAllFshNConflictInVectorAsXML(Vector v_fshDtl, int rsv_id) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        //xmlBuf.append("<facility_availability>");
        xmlBuf.append("<facility_availability");
        if(rsv_id > 0) {
            xmlBuf.append(" rsv_id=\"").append(rsv_id).append("\"");
        }
        xmlBuf.append(">");
        xmlBuf.append("<facility_schedule_list>");
        if(v_fshDtl != null) {
            for(int i=0; i<v_fshDtl.size(); i++) {
                ViewFmFacilitySchedule fshDtl = (ViewFmFacilitySchedule) v_fshDtl.elementAt(i);
                xmlBuf.append(getFshNConflictAsXML(fshDtl));
            }
        }
        xmlBuf.append("</facility_schedule_list>");
        xmlBuf.append("</facility_availability>");
        return xmlBuf.toString();
    }

    /**
    Vendor XML showing details and conflicts of the input facility schedule
    */
    private String getFshNConflictAsXML(ViewFmFacilitySchedule fshDtl) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        //facility schedule info
        xmlBuf.append("<facility_schedule date=\"").append(cwUtils.escNull(fshDtl.getFsh_date())).append("\"")
              .append(" start_time=\"").append(cwUtils.escNull(fshDtl.getFsh_start_time())).append("\"")
              .append(" end_time=\"").append(cwUtils.escNull(fshDtl.getFsh_end_time())).append("\">");
        //facility schedule create user info
        /*
        xmlBuf.append("<create_user ent_id=\"").append(cwUtils.escZero(fshDtl.getFsh_create_usr_ent_id())).append("\"")
              .append(" first_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getFsh_create_usr_first_name_bil()))).append("\"")
              .append(" last_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getFsh_create_usr_last_name_bil()))).append("\"")
              .append(" display_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getFsh_create_usr_display_bil()))).append("\"/>");
        */
        //facility info
        xmlBuf.append("<facility id=\"").append(cwUtils.escZero(fshDtl.getFsh_fac_id())).append("\">")
              .append("<basic><title>").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getFac_title()))).append("</title></basic>")
              .append("</facility>");
        //reservation detail
        if(fshDtl.getFsh_rsv_id() != 0) {
            xmlBuf.append("<reservation id=\"").append(cwUtils.escZero(fshDtl.getFsh_rsv_id())).append("\"")
                  .append(" status=\"").append(cwUtils.escNull(fshDtl.getRsv_status())).append("\">")
                  .append("<reservation_details>")
                  .append("<purpose>").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getRsv_purpose()))).append("</purpose>")
                  .append("<participant_no>").append(fshDtl.getRsv_participant_no()).append("</participant_no>")
                  .append("<reserve_user ent_id=\"").append(cwUtils.escZero(fshDtl.getRsv_ent_usr_ent_id())).append("\"")
                  .append(" first_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getRsv_ent_usr_first_name_bil()))).append("\"")
                  .append(" last_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getRsv_ent_usr_last_name_bil()))).append("\"")
                  .append(" display_name=\"").append(cwUtils.esc4XML(cwUtils.escNull(fshDtl.getRsv_ent_usr_display_bil()))).append("\"/>")
                  .append("</reservation_details>")
                  .append("</reservation>");
        }
        //facility schedule conflict
        if(fshDtl.hasConflict(FSH_STATUS_CANCELLED)) {
            Vector v_conflictFshDtl = fshDtl.getConflictFsh(FSH_STATUS_CANCELLED);
            xmlBuf.append("<conflict_list>");
            for(int i=0; i < v_conflictFshDtl.size(); i++) {
                ViewFmFacilitySchedule conflictFshDtl = (ViewFmFacilitySchedule)v_conflictFshDtl.elementAt(i);
                xmlBuf.append(getFshNConflictAsXML(conflictFshDtl));
            }
            xmlBuf.append("</conflict_list>");
        }
        xmlBuf.append("</facility_schedule>");
        return xmlBuf.toString();
    }

    /**
    Make reservetion and check out the shopping cart
    */
    public String insRsv(String rsv_purpose, String rsv_desc,
                       int rsv_ent_id, int rsv_participant_no,
                       int rsv_main_fac_id, int[] fsh_fac_id_lst,
                       Timestamp[] fsh_start_time_lst,
                       Timestamp[] fsh_upd_timestamp_lst,
                       String[] fsh_status_lst,
                       String usr_id, int owner_ent_id) throws SQLException {
        //insert into fmReservation
        Timestamp cur_time = cwSQL.getTime(this.con);
        ViewFmReservation rsvDtl = new ViewFmReservation(this.con);
        rsvDtl.setRsv_purpose(rsv_purpose);
        rsvDtl.setRsv_desc(rsv_desc);
        rsvDtl.setRsv_ent_id(rsv_ent_id);
        rsvDtl.setRsv_participant_no(rsv_participant_no);
        rsvDtl.setRsv_main_fac_id(rsv_main_fac_id);
        rsvDtl.setRsv_status(RSV_STATUS_OK);
        rsvDtl.setRsv_owner_ent_id(owner_ent_id);
        rsvDtl.setRsv_create_timestamp(cur_time);
        rsvDtl.setRsv_create_usr_id(usr_id);
        rsvDtl.setRsv_upd_timestamp(cur_time);
        rsvDtl.setRsv_upd_usr_id(usr_id);
        rsvDtl.ins();
        
        rsv_id = rsvDtl.getRsv_id();
        
        //update fmFacilitySchedule
        Hashtable h_v_fshDtl = generateVFsh4Upd(fsh_fac_id_lst, fsh_start_time_lst,
                                                fsh_upd_timestamp_lst, fsh_status_lst,
                                                owner_ent_id);
        Enumeration keys = h_v_fshDtl.keys();
        ViewFmFacilitySchedule fshUpdater = new ViewFmFacilitySchedule(this.con);
        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            Vector v_fshDtl = (Vector) h_v_fshDtl.get(key);
            fshUpdater.updFsh(rsvDtl.getRsv_id(), key, v_fshDtl, true);
        }
        return getFmInterfaceXML(rsvDtl);
    }

    /**
    Make reservetion and check out the shopping cart
    */
    public String updRsv(int rsv_id, String rsv_purpose,
                       String rsv_desc, int rsv_ent_id,
                       int rsv_participant_no,
                       int rsv_main_fac_id,
                       Timestamp rsv_upd_timestamp,
                       int[] fsh_fac_id_lst,
                       Timestamp[] fsh_start_time_lst,
                       Timestamp[] fsh_upd_timestamp_lst,
                       String[] fsh_status_lst,
                       String usr_id, int owner_ent_id) throws SQLException, cwSysMessage {
        //insert into fmReservation
        Timestamp cur_time = cwSQL.getTime(this.con);
        ViewFmReservation rsvDtl = new ViewFmReservation(this.con);
        rsvDtl.setRsv_id(rsv_id);
        rsvDtl.setRsv_desc(rsv_desc);
        rsvDtl.setRsv_purpose(rsv_purpose);
        rsvDtl.setRsv_ent_id(rsv_ent_id);
        rsvDtl.setRsv_participant_no(rsv_participant_no);
        rsvDtl.setRsv_main_fac_id(rsv_main_fac_id);
        rsvDtl.setRsv_upd_timestamp(cur_time);
        rsvDtl.setRsv_upd_usr_id(usr_id);
        rsvDtl.setRsv_owner_ent_id(owner_ent_id);
        rsvDtl.upd(rsv_upd_timestamp);

        //update fmFacilitySchedule
        Hashtable h_v_fshDtl = generateVFsh4Upd(fsh_fac_id_lst, fsh_start_time_lst,
                                                fsh_upd_timestamp_lst, fsh_status_lst,
                                                owner_ent_id);
        Enumeration keys = h_v_fshDtl.keys();
        ViewFmFacilitySchedule fshUpdater = new ViewFmFacilitySchedule(this.con);
        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            Vector v_fshDtl = (Vector) h_v_fshDtl.get(key);
            fshUpdater.updFsh(rsvDtl.getRsv_id(), key, v_fshDtl, false);
        }
        return getFmInterfaceXML(rsvDtl);
    }

    /**
    From the input info, generate Vectors of ViewFmFacilitySchedule (group by fsh_status)
    for updating facility schedule
    @return a Hashtable of ViewFmFacilitySchedule Vectors with fsh_status as key
    */
    private Hashtable generateVFsh4Upd(int[] fsh_fac_id_lst,
                                       Timestamp[] fsh_start_time_lst,
                                       Timestamp[] fsh_upd_timestamp_lst,
                                       String[] fsh_status_lst,
                                       int owner_ent_id) throws SQLException {

        Hashtable h_v_fshDtl = new Hashtable();
        if(fsh_fac_id_lst != null && fsh_fac_id_lst.length > 0) {
            for(int i=0; i<fsh_fac_id_lst.length; i++) {
                ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(this.con);
                fshDtl.setFsh_fac_id(fsh_fac_id_lst[i]);
                fshDtl.setFsh_start_time(fsh_start_time_lst[i]);
                fshDtl.setFsh_upd_timestamp(fsh_upd_timestamp_lst[i]);
                fshDtl.setFsh_status(fsh_status_lst[i]);
                fshDtl.setFsh_owner_ent_id(owner_ent_id);
                if(h_v_fshDtl.containsKey(fsh_status_lst[i])) {
                    Vector v_fshDtl = (Vector) h_v_fshDtl.get(fsh_status_lst[i]);
                    v_fshDtl.addElement(fshDtl);
                } else {
                    Vector v_fshDtl = new Vector();
                    v_fshDtl.addElement(fshDtl);
                    h_v_fshDtl.put(fsh_status_lst[i], v_fshDtl);
                }
            }
        }
        return h_v_fshDtl;
    }

    /**
    Get XML which used as a interface between facility management and outside system
    */
    private String getFmInterfaceXML(ViewFmReservation rsvDtl) throws SQLException {

        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<reservation id=\"").append(rsvDtl.getRsv_id()).append("\">")
              .append("<url_param_list>")
              .append("<url_param>")
              .append("<name>rsv_id</name>")
              .append("<value>").append(rsvDtl.getRsv_id()).append("</value>")
              .append("</url_param>")
              .append("<url_param>")
              .append("<name>rsv_start_datetime</name>")
              .append("<value>").append(rsvDtl.getRsv_start_time(FSH_STATUS_CANCELLED)).append("</value>")
              .append("</url_param>")
              .append("<url_param>")
              .append("<name>rsv_end_datetime</name>")
              .append("<value>").append(rsvDtl.getRsv_end_time(FSH_STATUS_CANCELLED)).append("</value>")
              .append("</url_param>")
              .append("</url_param_list>")
              .append("</reservation>");
        return xmlBuf.toString();
    }


/* DENNIS END */

    public static void updRsvPurpose4Itm(Connection con, long itm_id, boolean create_run_ind) throws SQLException {
        ViewFmReservation fmRsv = new ViewFmReservation(con);
        fmRsv.updRsvPurpose4Itm(itm_id, create_run_ind);
    }
}
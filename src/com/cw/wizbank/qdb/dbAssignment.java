package com.cw.wizbank.qdb;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.DbTrackingHistory;

public class dbAssignment extends dbModule {
    /**
     * CLOB column
     * Table:       Assignment
     * Column:      ass_files_desc_xml
     * Nullable:    YES
     */
    public static final String MOD_TYPE = "MOD";
    public static final String MOD_TYPE_ASS = "ASS";
    public static final String COMMENT_DIR = "comment";
    public static final String TEMP = "_temp";
    public static final String NOTIFY_ON = "T";
    public static final String NOTIFY_OFF = "F";

    public long      ass_res_id;
    public long      ass_max_upload;
    public String    ass_email;
    public String    ass_submission;
    public String    ass_notify_ind;
    public Timestamp ass_due_datetime;
    public long      ass_due_date_day;
    public String    ass_files_desc_xml;

    public String    INI_DIR_UPLOAD;
    public String    RES_FOLDER;
    public static String    SUBMIT_PATH = "ad3523fdads3242fafd";

    public boolean   bFileUpload;
    public String    tmpUploadDir;
    public String    pgrAttXML;
    public long      attempt_nbr;
    public long      no_of_upload;

    public dbAssignment() {
        super();
        ass_max_upload = -1;          // default value (i.e. unlimited upload)
        ass_email = null;
        ass_submission = null;
        ass_notify_ind = NOTIFY_ON;     // default value
        ass_due_datetime = null;
        ass_due_date_day = 0;
        ass_files_desc_xml = null;

        bFileUpload = false;
        tmpUploadDir = null;
        pgrAttXML = null;
        attempt_nbr = 1;
        no_of_upload = 0;
    }

    public void initialize(dbModule dbmod) {
        ass_res_id = dbmod.mod_res_id;

        mod_res_id = dbmod.mod_res_id;
        mod_type = dbmod.mod_type;
        mod_max_score = dbmod.mod_max_score;
        mod_pass_score = dbmod.mod_pass_score;
        mod_instruct = dbmod.mod_instruct;
        mod_max_attempt = dbmod.mod_max_attempt;
        mod_max_usr_attempt = 1;
        mod_score_ind = dbmod.mod_score_ind;
        mod_score_reset = dbmod.mod_score_reset;
        mod_in_eff_start_datetime = dbmod.mod_in_eff_start_datetime;
        mod_in_eff_end_datetime = dbmod.mod_in_eff_end_datetime;
        mod_usr_id_instructor = dbmod.mod_usr_id_instructor;
        //mod_logic = dbmod.mod_logic;
        tkh_id = dbmod.tkh_id;

        res_id = dbmod.res_id;
        res_lan = dbmod.res_lan;
        res_title = dbmod.res_title;
        res_desc = dbmod.res_desc;
        res_type = MOD_TYPE; // override
        res_subtype = MOD_TYPE_ASS; // override
        res_annotation = dbmod.res_annotation;
        res_format = dbmod.res_format;
        res_difficulty = dbmod.res_difficulty;
        //res_duration = dbmod.res_duration;
        res_privilege = dbmod.res_privilege;
        res_usr_id_owner = dbmod.res_usr_id_owner;
        res_tpl_name = dbmod.res_tpl_name;
        //res_id_root = dbmod.res_id_root;
        res_mod_res_id_test = dbmod.res_mod_res_id_test;
        res_status = dbmod.res_status;
        //res_create_date = dbmod.res_create_date;
        res_upd_user = dbmod.res_upd_user;
        res_upd_date = dbmod.res_upd_date;
        res_src_type = dbmod.res_src_type;
        res_src_link = dbmod.res_src_link;
        //res_url = dbmod.res_url;
        //res_filename = dbmod.res_filename;
        //res_code = dbmod.res_code;

        res_instructor_name = dbmod.res_instructor_name;
        res_instructor_organization = dbmod.res_instructor_organization;
    }

    public void ins(Connection con, loginProfile prof, String[] files_desc_lst, String[] files)
        throws qdbException
    {
        try {
            // calls dbModule.ins()
            super.ins(con, prof);
            //super.insChild(con, files);

            ass_files_desc_xml = "<Description>";

            if (files_desc_lst != null) {
                for (int i = 1; i < files_desc_lst.length + 1; i++) {
                    ass_files_desc_xml += "<body id=\"" + i + "\">" + dbUtils.esc4XML(files_desc_lst[i-1]) + "</body>";
                }
            }

            ass_files_desc_xml += "</Description>";

            // if ok
            mod_res_id = res_id;
            ass_res_id = res_id;

            // << BEGIN for oracle migration!
            PreparedStatement stmt = con.prepareStatement(
                " INSERT INTO Assignment "
                + " ( ass_res_id "
                + " , ass_max_upload "
                + " , ass_email "
                + " , ass_submission "
                + " , ass_notify_ind "
                + " , ass_due_datetime "
                + " , ass_due_date_day "
                //+ " , ass_files_desc_xml "
                + " ) "
                + " VALUES (?,?,?,?,?,?,?) ");
                //+ " VALUES (?,?,?," + cwSQL.getClobNull(con) + ",?,?," + cwSQL.getClobNull(con) + ") ");

            int count = 1;
            stmt.setLong(count++, ass_res_id);
            stmt.setLong(count++, ass_max_upload);
            stmt.setString(count++, "");
            stmt.setString(count++, ass_submission);
            stmt.setString(count++, ass_notify_ind);
            stmt.setTimestamp(count++, ass_due_datetime);
            stmt.setLong(count++, ass_due_date_day);
            //stmt.setString(count++, ass_files_desc_xml);
            //add stmt.close()
            //modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult != 1) {
                con.rollback();
                throw new qdbException("Failed to insert Assignment.");
            }
            
            // Update ass_files_desc_xml
            // construct the condition
            String condition = "ass_res_id = " + ass_res_id;
            // construct the column & value
            String[] columnName = new String[1];
            String[] columnValue = new String[1];
            columnName[0] = "ass_files_desc_xml";
            columnValue[0] = ass_files_desc_xml;
            cwSQL.updateClobFields(con, "Assignment", columnName, columnValue, condition);
            // >> END

            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void upd(Connection con, loginProfile prof, String[] files_desc_lst, String[] files, boolean isChangeDate)
        throws qdbException, qdbErrMessage, cwSysMessage
    {
        try {
            super.upd(con, prof, isChangeDate);
            //super.updChild(con, files);

            ass_files_desc_xml = "<Description>";

            if (files_desc_lst != null) {
                for (int i = 1; i < files_desc_lst.length + 1; i++) {
                    ass_files_desc_xml += "<body id=\"" + i + "\">" + dbUtils.esc4XML(files_desc_lst[i-1]) + "</body>";
                }
            }

            ass_files_desc_xml += "</Description>";

            // << BEGIN for oracle migration!
            String upd_mSQL = "UPDATE Assignment SET "
                + "   ass_max_upload = ? "
                + " , ass_email = ? "
                + " , ass_submission = ? "
                + " , ass_notify_ind = ? ";
            if (isChangeDate) {
                upd_mSQL += " , ass_due_datetime = ? " 
                          + " , ass_due_date_day = ? ";
            }
                //+ " , ass_files_desc_xml = " + cwSQL.getClobNull(con)
            upd_mSQL += " where ass_res_id = ? ";

            PreparedStatement stmt = con.prepareStatement(upd_mSQL);

            int count = 1;
            stmt.setLong(count++, ass_max_upload);
            stmt.setString(count++, ass_email);
            stmt.setString(count++, ass_submission);
            stmt.setString(count++, ass_notify_ind);
            if (isChangeDate) {
                stmt.setTimestamp(count++, ass_due_datetime);
                stmt.setLong(count++, ass_due_date_day);
            }
            //stmt.setString(count++, ass_files_desc_xml);
            stmt.setLong(count++, ass_res_id);
            //add stmt.close()
            //modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1) {
                con.rollback();
                throw new qdbException("Failed to update Assignment.");
            }
            
            // Update ass_files_desc_xml
            // construct the condition
            String condition = "ass_res_id = " + ass_res_id;
            // construct the column & value
            String[] columnName = new String[1];
            String[] columnValue = new String[1];
            columnName[0] = "ass_files_desc_xml";
            columnValue[0] = ass_files_desc_xml;
            cwSQL.updateClobFields(con, "Assignment", columnName, columnValue, condition);
/*          // Update ass_files_desc_xml
            stmt = con.prepareStatement(
                      " SELECT ass_files_desc_xml FROM Assignment "
                    + "     WHERE ass_res_id = ? FOR UPDATE ",
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE );

            stmt.setLong(1, ass_res_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                cwSQL.setClobValue(con, rs, "ass_files_desc_xml", ass_files_desc_xml);
                rs.updateRow();
            }
            stmt.close();*/
            // >> END

            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    /**
     * this is cloned from the original upd method.
     * specially modified for class module of a course using common content
     */
    public void upd2(Connection con, loginProfile prof) throws qdbException, qdbErrMessage, SQLException {
        super.upd2(con, prof);
        String upd_mSQL = "UPDATE Assignment SET "
                        + " ass_due_datetime = ? "
                        + " , ass_due_date_day = ? "
                        + " where ass_res_id = ? ";
        PreparedStatement stmt = con.prepareStatement(upd_mSQL);
        int idx = 1;
        stmt.setTimestamp(idx++, ass_due_datetime);
        stmt.setLong(idx++, ass_due_date_day);
        stmt.setLong(idx++, ass_res_id);
        int stmtResult = stmt.executeUpdate();
        stmt.close();
        if (stmtResult != 1) {
            throw new SQLException("Failed to update Assignment.");
        }
    }
    
    // delete without checking privilege
    public void aeDel(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        try {
            dbProgressAttachment pgrAttach = new dbProgressAttachment();
            pgrAttach.delAll(con, prof, ass_res_id, attempt_nbr);

            dbProgress dbpgr = new dbProgress();
            dbpgr.pgr_res_id = ass_res_id;
            dbpgr.pgr_attempt_nbr = attempt_nbr;
            dbpgr.delAll(con);

            PreparedStatement stmt = con.prepareStatement(
                "DELETE From Assignment where ass_res_id=?");

            stmt.setLong(1, ass_res_id);
            //add stmt.close()
            //modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1){
                con.rollback();
                throw new qdbException("Failed to delete Assignment. No such record.");
            } else {
                //super.delChild(con);
                super.del(con);

                String saveDirPath = INI_DIR_UPLOAD + dbUtils.SLASH + ass_res_id;
                dbUtils.delDir(saveDirPath);
            }
            
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void del(Connection con, loginProfile prof)
        throws qdbException, qdbErrMessage ,cwSysMessage
    {
        try {
            dbProgressAttachment pgrAttach = new dbProgressAttachment();
            pgrAttach.delAll(con, prof, ass_res_id, attempt_nbr);

            dbProgress dbpgr = new dbProgress();
            dbpgr.pgr_res_id = ass_res_id;
            dbpgr.pgr_attempt_nbr = attempt_nbr;
            dbpgr.delAll(con, prof);

            PreparedStatement stmt = con.prepareStatement(
                "DELETE From Assignment where ass_res_id=?");

            stmt.setLong(1, ass_res_id);
            //add stmt.close()
            //modified by Lear
            int stmtResult=stmt.executeUpdate();
            stmt.close();
            if ( stmtResult!=1){
                con.rollback();
                throw new qdbException("Failed to delete Assignment. No such record.");
            } else {
                //super.delChild(con);
                super.del(con, prof);

                String saveDirPath = INI_DIR_UPLOAD + dbUtils.SLASH + ass_res_id;
                dbUtils.delDir(saveDirPath);
            }
            
            return;
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }

    public void get(Connection con)
            throws qdbException, cwSysMessage
    {
        try {
            mod_res_id = ass_res_id;
            super.get(con);

            PreparedStatement stmt = con.prepareStatement(
            "SELECT  ass_max_upload, "
            + " ass_email, "
            + " ass_submission, "
            + " ass_notify_ind, "
            + " ass_due_datetime, "
            + " ass_due_date_day, "
            + " ass_files_desc_xml "
            + " FROM Assignment "
            + " where ass_res_id =?");

            // set the values for prepared statements
            stmt.setLong(1, ass_res_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
            {
                ass_max_upload = rs.getLong("ass_max_upload");
                ass_email = rs.getString("ass_email");
                ass_notify_ind = rs.getString("ass_notify_ind");
                ass_due_datetime = rs.getTimestamp("ass_due_datetime");
                ass_due_date_day = rs.getLong("ass_due_date_day");
                ass_submission = rs.getString("ass_submission");
                // << BEGIN for oracle migration!
                //ass_files_desc_xml = rs.getString("ass_files_desc_xml");
                ass_files_desc_xml = cwSQL.getClobValue(rs, "ass_files_desc_xml");
                // >> END
            }
            else
            {
				stmt.close();
                throw new qdbException( "No data for assignment. id = " + res_id );
            }
            
            stmt.close();
        } catch(SQLException e) {
            throw new qdbException("SQL Error: " + e.getMessage());
        }
    }
    
    public String getUserReport1(Connection con, String usrId, loginProfile prof, long tkhId)
            throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException {
    	String result ;
    	result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<student_report>" + dbUtils.NEWL;
    	result += getUserReport(con, usrId, prof, tkhId);
    	result += "</student_report>" + dbUtils.NEWL;
    	return result;
    }

    public String getUserReport(Connection con, String usrId, loginProfile prof, long tkhId)
            throws qdbException, qdbErrMessage, cwSysMessage, cwException, SQLException
    {
        if (tkhId == DbTrackingHistory.TKH_ID_UNDEFINED) {
            //System.out.println("!!!! get tracking id in dbAssignment.getUserReport() ");
            long entId = dbRegUser.getEntId(con, usrId);
            tkhId = DbTrackingHistory.getAppTrackingIDByMod(con, ass_res_id, usr_ent_id);
        }
        /*
        if (! prof.usr_id.equals(usrId)) {
            checkModifyPermission(con, prof);

//        if ((!prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) &&
//            (!prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER)) &&
//            (!prof.usr_id.equals(usrId))) {
        }
        */

        String result ;
        dbProgress pgr = new dbProgress();
        pgr.pgr_usr_id = usrId;
        pgr.pgr_res_id = ass_res_id;
        pgr.pgr_tkh_id = tkhId;
        pgr.get(con, attempt_nbr);
        result = pgr.rptUserAssAsXML(con, prof, RES_FOLDER, COMMENT_DIR);

        return result;
    }

    public String asXML(Connection con, loginProfile prof, String dpo_view, long tkhId, String ssoXml)
        throws qdbException, SQLException
    {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<module id=\""+ res_id +"\" is_public=\""+res_status+ "\" tkh_id=\"" + tkhId + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date +"\">" + dbUtils.NEWL;
        
        // author's information
        result += prof.asXML() + dbUtils.NEWL;
        
        //for sso link
        result += ssoXml;
        
        // user email
        dbRegUser  user = new dbRegUser();
        user.usr_id = prof.usr_id;
        user.usr_ent_id = prof.usr_ent_id;
        user.get(con);
        result += "<email value=\"" + cwUtils.esc4XML(user.usr_email) + "\" />" + dbUtils.NEWL;

        result += dbResourcePermission.aclAsXML(con,res_id,prof);
        // Module Header

        result += getAssHeader(con, prof.usr_id, prof.root_ent_id, tkhId);

        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkhId);

        result += getDisplayOption(con, dpo_view);
        result += getIsEnrollmentRelatedAsXML(con);

        result += "<body>" + dbUtils.NEWL;
        result += getAssResContent(con) + dbUtils.NEWL;
        result += "</body>" + dbUtils.NEWL;
        result += "</module>";

        return result;
    }


    public String asXML(Connection con, loginProfile prof, Hashtable fileLst, String[] file_order, String dpo_view, long tkhId)
        throws qdbException
    {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<module id=\""+ res_id + "\" tkh_id=\"" + tkhId + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date +"\">" + dbUtils.NEWL;

        // author's information
        result += prof.asXML() + dbUtils.NEWL;

        // user email
        dbRegUser  user = new dbRegUser();
        user.usr_id = prof.usr_id;
        user.usr_ent_id = prof.usr_ent_id;
        user.get(con);
        result += "<email value=\"" + cwUtils.esc4XML(user.usr_email) + "\" />" + dbUtils.NEWL;

        result += dbResourcePermission.aclAsXML(con,res_id,prof);
        // Module Header
        result += getAssHeader(con, prof.usr_id, prof.root_ent_id, tkhId);
        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkhId);
        result += getDisplayOption(con, dpo_view);
        result += "<body>" + dbUtils.NEWL;

        if (fileLst != null && file_order != null) {
//            String saveDirPathTemp = RES_FOLDER + dbUtils.SLASH + ass_res_id + dbUtils.SLASH + user.usr_id + TEMP + dbUtils.SLASH;
            String saveDirPathTemp = getAssPath(con, RES_FOLDER, ass_res_id, user.usr_id, tkhId) + TEMP + dbUtils.SLASH;

            result += "<uploadPathTemp path=\"" + saveDirPathTemp + "\">" + dbUtils.NEWL;

            //Enumeration lst = fileLst.keys();
            String filename = null;

            for (int i=1; i<=file_order.length; i++) {
                filename = file_order[i-1];

                if (! filename.equals(".")) {
                    result += "<file id=\"" + i + "\" name=\"" + cwUtils.esc4XML(filename) + "\">" +
                    cwUtils.esc4XML((String)fileLst.get(filename)) + "</file>" + dbUtils.NEWL;
                }
            }

            result += "</uploadPathTemp>" + dbUtils.NEWL;
        }

        result += getAssResContent(con);

        result += "</body>" + dbUtils.NEWL;
        result += "</module>";

        return result;
    }

    public String asXML(Connection con, loginProfile prof, Vector attachLst, String dpo_view, long tkhId)
        throws qdbException
    {
        String result = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        result += "<module id=\""+ res_id + "\" tkh_id=\"" + tkhId + "\" language=\"" + res_lan + "\" timestamp=\"" + res_upd_date +"\">" + dbUtils.NEWL;

        // author's information
        result += prof.asXML() + dbUtils.NEWL;

        // user email
        dbRegUser  user = new dbRegUser();
        user.usr_id = prof.usr_id;
        user.usr_ent_id = prof.usr_ent_id;
        user.get(con);
        result += "<email value=\"" + cwUtils.esc4XML(user.usr_email) + "\" />" + dbUtils.NEWL;

        result += dbResourcePermission.aclAsXML(con,res_id,prof);
        // Module Header
        result += getAssHeader(con, prof.usr_id, prof.root_ent_id, tkhId);
        result += dbModuleEvaluation.getModuleEvalAsXML(con, res_id, prof.usr_ent_id, tkhId);
        //result += dbCourse
        result += getDisplayOption(con, dpo_view);
        result += "<body>" + dbUtils.NEWL;

        if (attachLst != null) {
//            String saveDirPathTemp = RES_FOLDER + dbUtils.SLASH + ass_res_id + dbUtils.SLASH + user.usr_id + TEMP + dbUtils.SLASH;
            String saveDirPathTemp = getAssPath(con, RES_FOLDER, ass_res_id, user.usr_id, tkhId) + TEMP + dbUtils.SLASH;
            
            result += "<uploadPathTemp path=\"" + saveDirPathTemp + "\">" + dbUtils.NEWL;

            dbAttachment attach = null;

            for (int i=0; i<attachLst.size(); i++) {
                attach = (dbAttachment)attachLst.elementAt(i);
                result += "<file id=\"" + attach.att_id + "\" name=\"" + attach.att_filename + "\">" +
                            attach.att_desc + "</file>" + dbUtils.NEWL;
            }

            result += "</uploadPathTemp>" + dbUtils.NEWL;
        }

        result += getAssResContent(con);

        result += "</body>" + dbUtils.NEWL;
        result += "</module>";

        return result;
    }

    public String getAssHeader(Connection con, String usr_id, long root_ent_id)
            throws qdbException
    {
        return getAssHeader(con, usr_id, root_ent_id, -1);
    }
    public String getAssHeader(Connection con, String usr_id, long root_ent_id, long tkhId)
            throws qdbException
    {
    	Timestamp curTime = dbUtils.getTime(con);
    	String result;
        String tmp_end_datetime = null;
        String ATTEMPTED = RES_ATTEMPTED_FALSE;
        // Check if the module was attempted
        long cnt = dbProgress.attemptNum(con , mod_res_id);
        if (cnt>0)
            ATTEMPTED = RES_ATTEMPTED_TRUE;

        // added by Emily, 2002-06-14
        // check if the due_datetime need to be converted to "UNLIMITED"
        String tmp_ass_due_datetime = null;
        if(ass_due_datetime != null)
            if(dbUtils.isMaxTimestamp(ass_due_datetime) == true)
                tmp_ass_due_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
            else
                tmp_ass_due_datetime = ass_due_datetime.toString();

        //Dennis, 2000-11-13, impl release control
        //check if the end_datetime need to be converted to "UNLIMITED"
        if(mod_eff_end_datetime != null)
            if(dbUtils.isMaxTimestamp(mod_eff_end_datetime) == true)
                tmp_end_datetime = dbUtils.UNLIMITED; //convert to String to "UNLIMITED"
            else
                tmp_end_datetime = mod_eff_end_datetime.toString();


        //Dennis, display option, get the attributes that needed to be displayed
        Timestamp PSTART = null;
        Timestamp PCOMPLETE = null;
        Timestamp PLASTACC = null;
        long PATTEMPTNBR = 0;

        dbProgress pgr = new dbProgress();
        pgr.pgr_usr_id = usr_id;
        pgr.pgr_res_id = mod_res_id;
        pgr.pgr_tkh_id = tkhId;
        PATTEMPTNBR = dbProgress.usrAttemptNum(con,mod_res_id,usr_id,tkhId);
        if(PATTEMPTNBR > 0) {
            try {
                pgr.get(con,1);
                PSTART = pgr.pgr_start_datetime;

                pgr.get(con);
                PCOMPLETE = pgr.pgr_complete_datetime;
                PLASTACC = pgr.pgr_last_acc_datetime;
            }
            catch(qdbErrMessage e) {
                throw new qdbException (e.getMessage());
            }
        }
        // Course id
        long cosId = dbModule.getCosId(con, mod_res_id);

        result = "<header course_id=\"" + cosId + "\" difficulty=\"" + res_difficulty;
        result +=       "\" mod_id=\"" + mod_res_id;
        result +=       "\" max_upload=\"" + ass_max_upload;
        result +=       "\" notify_email=\"" + cwUtils.esc4XML(ass_email);
        result +=       "\" notify=\"" + ass_notify_ind;
        result +=       "\" due_datetime=\"" + tmp_ass_due_datetime;
        result +=       "\" due_date_day=\"" + ass_due_date_day;
        result +=       "\" max_score=\"" + mod_max_score ;
        result +=       "\" pass_score=\"" + mod_pass_score + "\" privilege=\"" ;
                                                             // should be change to res_type
        result +=       res_privilege + "\" status=\"" + res_status + "\" type=\"" + res_type ;
        result +=       "\" subtype=\"" + res_subtype ;
        result +=       "\" max_attempt=\"" + mod_max_attempt + "\" max_usr_attempt=\"" + mod_max_usr_attempt ;
        result +=       "\" score_ind=\"" + mod_score_ind;
        result +=       "\" score_reset=\"" + mod_score_reset;
        result +=       "\" logic=\"" + mod_logic ;
        result +=       "\" eff_start_datetime=\"" + mod_eff_start_datetime;
        result +=       "\" eff_end_datetime=\"" + tmp_end_datetime;
        result +=       "\" attempted=\"" + ATTEMPTED;
        result +=       "\" attempt_nbr=\"" + PATTEMPTNBR;
        result +=       "\" pgr_start=\"" + PSTART;
        result +=       "\" pgr_complete=\"" + PCOMPLETE;
        result +=       "\" pgr_last_acc=\"" + PLASTACC ;
        result +=       "\" cur_time=\"" + curTime;
        result +=		"\" mod_mobile_ind=\"" + mod_mobile_ind + "\">";

        result += dbUtils.NEWL;
        result += "<source type=\"" + res_src_type + "\">"+ dbUtils.esc4XML(res_src_link) + "</source>" + dbUtils.NEWL;
//        result += "<url>" + dbUtils.esc4XML(res_url) + "</url>" + dbUtils.NEWL;
//        result += "<filename>" + dbUtils.esc4XML(res_filename) + "</filename>" + dbUtils.NEWL;
        result += "<title>" + dbUtils.esc4XML(res_title) + "</title>" + dbUtils.NEWL;
        result += "<desc>" + dbUtils.esc4XML(res_desc) + "</desc>" + dbUtils.NEWL;
        result += "<annotation>" + res_annotation + "</annotation>" + dbUtils.NEWL;
        result += "<instruction>" + dbUtils.esc4XML(mod_instruct) + "</instruction>" + dbUtils.NEWL;
        result += "<submission>" + dbUtils.esc4XML(ass_submission) + "</submission>" + dbUtils.NEWL;
        result += "<instructor>" + dbUtils.esc4XML(res_instructor_name) + "</instructor>" + dbUtils.NEWL;

        result += dbCourse.getInstructorList(con, cosId, mod_usr_id_instructor, root_ent_id);


        //result += "<moderator>" + dbUtils.esc4XML(getOwnerName(con)) + "</moderator>" + dbUtils.NEWL;
        result += "<organization>" + dbUtils.esc4XML(res_instructor_organization) + "</organization>" + dbUtils.NEWL;

        if (ass_max_upload != -1) {
            result += ass_files_desc_xml + dbUtils.NEWL;
        } else {
            result += "<Description>" + dbUtils.NEWL;

            for (int i=1; i<=no_of_upload; i++) {
                result += "<body id=\"" + i + "\"/>" + dbUtils.NEWL;
            }

            result += "</Description>" + dbUtils.NEWL;
        }

        result += "</header>" + dbUtils.NEWL;

        return result;
    }

    public void grade(Connection con, loginProfile prof, dbProgress dbpgr, String filename, String comment, long tkhId)
        throws qdbException, cwSysMessage, cwException, SQLException
    {
        try{
        // check User Right
        /*checkModifyPermission(con, prof);*/
        //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}
    
        String grade = dbpgr.pgr_grade;
        float score = dbpgr.pgr_score;
        dbRegUser usr = new dbRegUser();
        usr.usr_id = dbpgr.pgr_usr_id;
        usr.usr_ent_id = usr.getEntId(con);
        usr.get(con);
        if (tkhId == DbTrackingHistory.TKH_ID_UNDEFINED) {
            //System.out.println("!!!! get tracking id in dbAssignment.grade()");
            tkhId = DbTrackingHistory.getAppTrackingIDByMod(con, ass_res_id, usr_ent_id);
        }

        String commentDirPath = getAssPath(con, INI_DIR_UPLOAD, ass_res_id, usr.usr_id, tkhId) + dbUtils.SLASH + COMMENT_DIR;

        if (bFileUpload && filename != null && filename.length() != 0) {
            // Copy all the files to the permanet directory
//System.out.println("commentDirPath: " + commentDirPath);
//System.out.println("tmpUploadDir: " + tmpUploadDir);
            dbUtils.moveDir(tmpUploadDir, commentDirPath);

            // Remove un-necessary uploaded files
            File dir = new File(commentDirPath);
            String[] fList = dir.list();
            File fh = null;

            if (fList != null) {
                for (int i = 0; i < fList.length; i++) {
                    if (! filename.equals(fList[i])) {
                        fh = new File(dir, fList[i]);
                        fh.delete();
                    }
                }
            }

        }

        dbpgr.pgr_res_id = ass_res_id;
        dbpgr.pgr_attempt_nbr = attempt_nbr;
        dbpgr.pgr_tkh_id = tkhId;
        // get the pgr_complete_datetime
        dbpgr.get(con);
        dbModule mod = new dbModule();
        float mod_pass_score = mod.getPassingScore(con, ass_res_id);
        mod.mod_res_id = ass_res_id;
        mod.get(con);
        float pass_score = mod.mod_pass_score;
        dbpgr.pgr_grade = grade;
        dbpgr.pgr_score = (float)(Math.round(score*100))/100;
    	dbpgr.pgr_status = dbProgress.PGR_STATUS_GRADED;      
    	if((mod_pass_score !=0 && score < pass_score) || (grade != null && grade.equals("F"))){        	
    		dbpgr.pgr_completion_status = dbProgress.COMPLETION_STATUS_FAILED;         	
        }else{
        	dbpgr.pgr_completion_status = dbProgress.COMPLETION_STATUS_PASSED;
        }
        dbpgr.updResult(con);
        dbProgressAttachment pgrAttach = new dbProgressAttachment(dbpgr.pgr_usr_id, ass_res_id, attempt_nbr, tkhId);
        pgrAttach.delAllTechAtt(con, prof);
        Vector attLst = new Vector();
        dbAttachment attach = new dbAttachment();

        attach.att_type = "TEACHER";
        attach.att_filename = filename;
        attach.att_desc = comment;

        attLst.addElement(attach);
        pgrAttach.ins(con, prof, attLst);
        // add by kim
        dbModuleEvaluation dbmov = new dbModuleEvaluation();
        dbmov.mov_cos_id = dbModule.getCosId(con, ass_res_id);
        dbmov.mov_ent_id = dbRegUser.getEntId(con, dbpgr.pgr_usr_id);
        dbmov.mov_mod_id = ass_res_id;
        dbmov.mov_tkh_id = tkhId;
        dbmov.getLastAccess(con);
        if(dbpgr.pgr_status.equalsIgnoreCase(dbProgress.PGR_STATUS_NOT_GRADED)) {
            dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
        } else {
            if((mod_pass_score !=0 && score < pass_score) || (dbpgr.pgr_grade != null && dbpgr.pgr_grade.equals("F"))){        	
            	dbmov.mov_status = dbAiccPath.STATUS_FAILED;         	
            }else{
            	dbmov.mov_status = dbAiccPath.STATUS_PASSED;
            }
        }
        if (dbpgr.pgr_grade == null){
            dbmov.mov_score = "" + dbpgr.pgr_score;
        }
        else{
            if (dbpgr.pgr_grade.equals("A+"))
                dbmov.mov_score = "" + (-101);
            else if (dbpgr.pgr_grade.equals("A"))
                dbmov.mov_score = "" + (-102);
            else if (dbpgr.pgr_grade.equals("A-"))
                dbmov.mov_score = "" + (-103);
            else if (dbpgr.pgr_grade.equals("B+"))
                dbmov.mov_score = "" + (-104);
            else if (dbpgr.pgr_grade.equals("B"))
                dbmov.mov_score = "" + (-105);
            else if (dbpgr.pgr_grade.equals("B-"))
                dbmov.mov_score = "" + (-106);
            else if (dbpgr.pgr_grade.equals("C+"))
                dbmov.mov_score = "" + (-107);
            else if (dbpgr.pgr_grade.equals("C"))
                dbmov.mov_score = "" + (-108);
            else if (dbpgr.pgr_grade.equals("C-"))
                dbmov.mov_score = "" + (-109);
            else if (dbpgr.pgr_grade.equals("D"))
                dbmov.mov_score = "" + (-110);
            else if (dbpgr.pgr_grade.equals("F"))
                dbmov.mov_score = "" + (-111);
        }
        dbmov.attempt_counted = true;
        dbmov.save(con, prof);
      }catch(qdbErrMessage em){
            throw new cwSysMessage(em.getMessage());
      }
    }

    public void reset(Connection con, loginProfile prof, dbProgress dbpgr, long tkhId)
        throws qdbException, qdbErrMessage
    {
        // check User Right
        /*checkModifyPermission(con, prof);*/
        //if (!dbResourcePermission.hasPermission(con, mod_res_id, prof,
        //                                dbResourcePermission.RIGHT_WRITE)) {
        //    throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
        //}

        dbProgressAttachment pgrAttach = new dbProgressAttachment(dbpgr.pgr_usr_id, ass_res_id, attempt_nbr, tkhId);
        pgrAttach.delAll(con, prof);

/*        dbpgr.pgr_res_id = ass_res_id;
        dbpgr.pgr_attempt_nbr = attempt_nbr;
        dbpgr.pgr_status = "NOT GRADED";
        dbpgr.upd(con);*/

        dbpgr.pgr_res_id = ass_res_id;
        dbpgr.pgr_attempt_nbr = attempt_nbr;
        dbpgr.del(con, prof);

        dbRegUser  user = new dbRegUser();
        user.usr_id = dbpgr.pgr_usr_id;
        user.usr_ent_id = user.getEntId(con);
        user.get(con);

        String saveDirPath = getAssPath(con, INI_DIR_UPLOAD, ass_res_id, user.usr_id, tkhId);
        String saveDirPathTemp = saveDirPath + TEMP;

        dbUtils.delDir(saveDirPath);
        dbUtils.delDir(saveDirPathTemp);
    }

    public void delFile(Connection con, loginProfile prof, String filename)
        throws qdbException, qdbErrMessage
    {
        String saveDirPathTemp = INI_DIR_UPLOAD + dbUtils.SLASH + res_id + dbUtils.SLASH + SUBMIT_PATH + dbUtils.SLASH + prof.usr_id + TEMP;
        File fh = new File(saveDirPathTemp, filename);

        if (fh.exists()) {
            fh.delete();
        }
    }

    public String getAssResContent(Connection con) throws qdbException {
        try {
            String xmlBody = "";
            long resIDContent;
            long subNbr;
            long order;
            String filename;
            String type;
            String subType;

            String SQL = "Select rcn_res_id_content, rcn_sub_nbr, rcn_order, "
                       + "res_src_link, res_type, res_subtype "
                       //+ "res_filename, res_type, res_subtype "
                       + "From ResourceContent, Resources "
                       + "Where rcn_res_id = ? "
                       + "And res_id = rcn_res_id_content "
                       + "Order by rcn_order ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, ass_res_id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()==true) {

                resIDContent = rs.getLong("rcn_res_id_content");
                subNbr = rs.getLong("rcn_sub_nbr");
                order = rs.getLong("rcn_order");
                filename = rs.getString("res_src_link");
                type = rs.getString("res_type");
                subType = rs.getString("res_subtype");


                xmlBody += "<item id=\"" + resIDContent + "\" type=\"" + type + "\" subtype=\"" + subType
                         + "\" order=\"" + order + "\" sub_num=\"" + subNbr  + "\">" + dbUtils.NEWL
                         + "<filename>" + dbUtils.esc4XML(filename) + "</filename>" + dbUtils.NEWL
                         + "</item>" + dbUtils.NEWL;
            }

            stmt.close();
            return xmlBody;
        }
        catch(SQLException e) {
            throw new qdbException (e.getMessage());
        }
    }

    public void submitAssignment(Connection con, loginProfile prof, Hashtable fileLst, long step, long tkhId)
        throws qdbException, qdbErrMessage , cwSysMessage
    {
        // check User Right
        /*
        long cosId  = getCosId(con,mod_res_id);
        if (!dbResourcePermission.hasPermission(con, cosId, prof,
                                    dbResourcePermission.RIGHT_EXECUTE)) {
            throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_EXECUTE_MSG);
        }
        */
        dbProgress pgr = new dbProgress();
        pgr.pgr_usr_id = prof.usr_id;
        pgr.pgr_res_id = ass_res_id;
        pgr.pgr_attempt_nbr = attempt_nbr;
        pgr.pgr_tkh_id = tkhId;
        String status = pgr.getStatus(con, attempt_nbr);

        if (status != null && pgr.pgr_completion_status !=null && pgr.pgr_completion_status.equals(dbProgress.COMPLETION_STATUS_PASSED)) {
            //assignment has been submitted/graded
            throw new qdbErrMessage("PGR001");
        }

        String saveDirPath = getAssPath(con, INI_DIR_UPLOAD, res_id, prof.usr_id, tkhId);
        String saveDirPathTemp = saveDirPath + TEMP;
        dbProgressAttachment pgrAttach = new dbProgressAttachment(prof.usr_id, ass_res_id, attempt_nbr, tkhId);

        if (step == 1 || step == 3 || step == 4 || step == 5) {
            if (step==1) {
                dbUtils.delDir(saveDirPathTemp);
            } else if (step == 7) {
                dbUtils.copyDir(saveDirPath, saveDirPathTemp);
            }

            if (bFileUpload && step == 5) {
                dbUtils.moveDir(tmpUploadDir, saveDirPathTemp);
                // Remove un-necessary uploaded files
                File dir = new File(saveDirPathTemp);
                String[] fList = dir.list();
                File fh = null;

                if (fList != null) {
                    for (int i = 0; i < fList.length; i++) {
                        if (! fileLst.containsKey(fList[i])) {
                            fh = new File(dir, fList[i]);
                            fh.delete();
                        }
                    }
                }
            }
        } else if (step == 6) {
            pgrAttach.delAll(con, prof);
            dbUtils.delDir(saveDirPath);
            Timestamp cur_time = dbUtils.getTime(con);

            if (status != null) {
                pgr.del(con, prof);
            }

            dbUtils.moveDir(saveDirPathTemp, saveDirPath);
            get(con);

            pgr.pgr_max_score = mod_max_score;
            pgr.pgr_start_datetime = cur_time;
            pgr.pgr_status = dbProgress.PGR_STATUS_NOT_GRADED;
            pgr.pgr_completion_status = dbAiccPath.STATUS_INCOMPLETE;
            pgr.ins(con, 0);

            if (fileLst != null) {
                Vector attLst = new Vector();
                Enumeration lst = fileLst.keys();
                String filename = null;
                String desc = null;

                while (lst.hasMoreElements()) {
                    dbAttachment attach = new dbAttachment();
                    filename = (String) lst.nextElement();
                    desc = (String) fileLst.get(filename);
                    attach.att_type = "STUDENT";
                    attach.att_filename = filename;

                    if (! desc.equalsIgnoreCase("null")) {
                        attach.att_desc = desc;
                    }

                    attLst.addElement(attach);
                }

                pgrAttach.ins(con, prof, attLst);
            }
        }
    }
    
    public static String[] getFoloderLstByTrackingIDs(Connection con, String[] tkh_id_lst) throws cwException, SQLException
    {
        Vector folderVec = new Vector();
        for (int i=0;i<tkh_id_lst.length;i++) {
            String folder = getUserIdByTrackingID(con, Long.parseLong(tkh_id_lst[i]));
            if (folder != null) {
                
                //if (!DbTrackingHistory.isFirstAppnTracking(con, Long.parseLong(tkh_id_lst[i]))) {
                    folder += "_" + tkh_id_lst[i];
                //}
                folderVec.addElement(folder);
            }
        }
        String[] folder_lst = new String[folderVec.size()];
        for (int i=0;i<folderVec.size();i++) {
            folder_lst[i] = (String) folderVec.elementAt(i);
        }
        return folder_lst;
    }
    
    public static String getAssPath(Connection con, String root, long ass_res_id, String usr_id, long tkhId) throws qdbException {
        String path = root + dbUtils.SLASH + ass_res_id + dbUtils.SLASH + SUBMIT_PATH + dbUtils.SLASH + usr_id;
        //try {
            //if (!DbTrackingHistory.isFirstAppnTracking(con, tkhId)) {
                path += "_" + tkhId;
            //}
        //}catch (SQLException e) {
        //    throw new qdbException(e.getMessage());
        //} catch (cwException cwe) {
        //    throw new qdbException(cwe.getMessage());
        //}
        return path;
    }

    private static final String sql_get_usr_id_by_tracking_id = "Select usr_id From RegUser, TrackingHistory where usr_ent_id = tkh_usr_ent_id AND tkh_id = ? ";
    private static String getUserIdByTrackingID(Connection con, long tkhId) throws SQLException {
        String usr_id = null;
        PreparedStatement stmt = con.prepareStatement(sql_get_usr_id_by_tracking_id);
        stmt.setLong(1, tkhId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            usr_id = rs.getString("usr_id");
        }
        cwSQL.cleanUp(rs, stmt);
        return usr_id;
    }
    
    /**
     * Get the due date of this assignment by tracking history id.
     * For relative due date, if the course is a run, 
     * use effective start date as the base to calculate the actual due date.
     * otherwise, use application admitted timestamp as the base instead.
     * For absolute due date, return UNLIMITED if the date is unlimited
     * 2003-07-26 kawai
     */
    private static String sql_get_eff_start_by_tkh_id = 
        "select itm_eff_start_datetime from aeApplication, aeItem, aeItemRelation "
      + "where app_tkh_id = ? and app_ent_id = ? and app_itm_id = itm_id and itm_id = ire_child_itm_id and ire_parent_itm_id = ? ";
    private static String sql_get_att_create_by_tkh_id = 
        "select att_create_timestamp from aeApplication, aeAttendance "
      + "where app_tkh_id = ? and app_ent_id = ? and app_itm_id = ? and app_id = att_app_id ";
    public String getDueDateByAppTrackingID(Connection con, long app_ent_id, long app_tkh_id) throws SQLException, cwSysMessage {
        String result = null;
        
        if (this.ass_due_date_day > 0) {
            Timestamp baseTs = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            String sql = null;
            // check if it is a classroom course
            long cos_itm_id = getModuleItemId(con, this.ass_res_id);
            aeItem appItem = new aeItem();
            appItem.itm_id = cos_itm_id;
            appItem.get(con);
            if (appItem.itm_create_run_ind) {
                sql = sql_get_eff_start_by_tkh_id;
            } else {
                sql = sql_get_att_create_by_tkh_id;
            }
            // retrieve the date as the base to calculate the actual due date
            try {
                int idx = 1;
                stmt = con.prepareStatement(sql);
                stmt.setLong(idx++, app_tkh_id);
                stmt.setLong(idx++, app_ent_id);
                stmt.setLong(idx++, cos_itm_id);
                idx = 1;
                rs = stmt.executeQuery();
                if (rs.next()) {
                    baseTs = rs.getTimestamp(idx++);
                } else {
                    baseTs = null;
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
            
            if (baseTs != null) {
                // calculate the actual due date
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(baseTs);
                dateCal.add(Calendar.DAY_OF_MONTH, (int)this.ass_due_date_day);
                Timestamp dueTs = new Timestamp(((java.util.Date)dateCal.getTime()).getTime());
                result = dueTs.toString();
            }
        } else if (this.ass_due_datetime != null) {
            if (dbUtils.isMaxTimestamp(this.ass_due_datetime)) {
                result = dbUtils.UNLIMITED;
            } else {
                result = this.ass_due_datetime.toString();
            }
        }
        
        return result;
    }
    public static String getDueDate(long ass_due_date_day, Timestamp ass_due_datetime, boolean itm_create_run_ind, Timestamp itm_eff_start_datetime, Timestamp att_create_timestamp){
    	String result = null;
    	if (ass_due_date_day > 0) {
    		Timestamp baseTs = null;
    		if(itm_create_run_ind) {
    			baseTs = itm_eff_start_datetime;
    		} else {
    			baseTs = att_create_timestamp;
    		}
    		if(baseTs !=null){
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(baseTs);
                dateCal.add(Calendar.DAY_OF_MONTH, (int)ass_due_date_day);
                Timestamp dueTs = new Timestamp(((java.util.Date)dateCal.getTime()).getTime());
                result =JsonHelper.toJsonDateFormat(dueTs); 
    		}
    	} else if (ass_due_datetime != null) {
            if (dbUtils.isMaxTimestamp(ass_due_datetime)) {
                result = dbUtils.UNLIMITED;
            } else {
            	  result =JsonHelper.toJsonDateFormat(ass_due_datetime);
            }
    	}
    	return result;
    }
}

package com.cw.wizbank.db.view;

import java.util.Hashtable;
import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.sql.*;
import com.cwn.wizbank.utils.CommonLog;

public class ViewFmFacilitySchedule {
    // String for cwSysMessage
    //   for update failure
    private static final String SMSG_FMT_UPD_FAIL = "FMT001";
    private static final String SMSG_FMT_DEL_FAIL = "FMT002";
    //    for parameter not found
    public static final String  SMSG_FMT_GET_PARAM_FAIL = "FMT003";
    //   for record not found
    private static final String SMSG_FMT_GET_RSV_NO_EXIST = "FMT004";
    private static final String SMSG_FMT_GET_FSH_NO_EXIST = "FMT005";
    //   "The Facility Schedule Record Not Found !"
    private static final String SMSG_FMT_GET_FTP_NONE = "FMT006";
    //
    public static final String FSH_STATUS_PENCILLED_IN = "PENCILLED_IN";
    public static final String FSH_STATUS_RESERVED = "RESERVED";
    public static final String FSH_STATUS_CANCELLED = "CANCELLED";

    private Connection con = null;

    // constructor
    public ViewFmFacilitySchedule(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }
    // cancel FacilitySchedule
    public void cancelFsh(int root_ent_id,String usr_id,int rsv_id,String fsh_status,String[] fsh_fac_id_lst,String[] fsh_start_time_lst,String[] fsh_upd_timestamp_lst,String cancel_type,String cancel_reason,boolean link_feedback_ind)
            throws SQLException ,cwSysMessage ,cwException {

        StringBuffer sqlStr = new StringBuffer(1024);
        sqlStr.append(SqlStatements.sql_cancel_fsh);
        // if multi ,add the multi sql___by fsh_fac_id_lst.length
        int count = 0;
        if ((fsh_fac_id_lst != null)&&(fsh_start_time_lst != null)&&(fsh_upd_timestamp_lst != null)) {
            int fsh_fac_id_lst_length = fsh_fac_id_lst.length;
            int fsh_start_time_lst_length = fsh_start_time_lst.length;
            int fsh_upd_timestamp_lst_length = fsh_upd_timestamp_lst.length;
            if ((fsh_fac_id_lst_length > 0)&&(fsh_fac_id_lst_length == fsh_start_time_lst_length )&&(fsh_start_time_lst_length == fsh_upd_timestamp_lst_length)) {

                sqlStr.append(SqlStatements.sql_and);
                sqlStr.append("(");
                for (int i = 0; i < fsh_fac_id_lst_length ; i++ ) {
                    sqlStr.append(SqlStatements.sql_upd_fsh_multi);
                    sqlStr.append(SqlStatements.sql_or);
                }
                // Strip of "or"
                sqlStr.delete(sqlStr.length() - 3 , sqlStr.length());
                sqlStr.append(")");
            } else {
                throw new cwSysMessage(SMSG_FMT_GET_PARAM_FAIL);
            }
            // prepare statement
            PreparedStatement stmt = con.prepareStatement(sqlStr.toString());
            // get timestamp from database
            Timestamp timestampDb = cwSQL.getTime(con);
            // set parameters ahead
            stmt.setString(++count,fsh_status);
            stmt.setTimestamp(++count,timestampDb);
            stmt.setString(++count,usr_id);
            stmt.setTimestamp(++count,timestampDb);
            stmt.setString(++count,usr_id);
            stmt.setString(++count,cancel_type);
            stmt.setString(++count,cancel_reason);
            stmt.setInt(++count,rsv_id);
            // if multi sql,set the multi parameters
            if ((fsh_fac_id_lst_length > 0)&&(fsh_fac_id_lst_length == fsh_start_time_lst_length )&&(fsh_start_time_lst_length == fsh_upd_timestamp_lst_length)) {
                int fsh_fac_id = 0;
                Timestamp fsh_start_time;
                Timestamp fsh_upd_timestamp;
                for (int j = 0; j < fsh_fac_id_lst_length ; j++ ) {
                    // prepare parameters
                    fsh_fac_id = Integer.parseInt(fsh_fac_id_lst[j]);
                    fsh_start_time = Timestamp.valueOf(fsh_start_time_lst[j]);
                    fsh_upd_timestamp = Timestamp.valueOf(fsh_upd_timestamp_lst[j]);
                    stmt.setInt(++count,fsh_fac_id);
                    stmt.setTimestamp(++count,fsh_start_time);
                    stmt.setTimestamp(++count,fsh_upd_timestamp);
                }
            }

            if (stmt.executeUpdate() < 1) {
                throw new cwSysMessage(SMSG_FMT_UPD_FAIL);
            }

        stmt.close();
        } else {
            throw new cwSysMessage(SMSG_FMT_GET_PARAM_FAIL);
        }

        // for reset main
        // cancel fsh check rsv_main_fac_id ;reset to null
        int count2 = 0;
        // prepare statement
        PreparedStatement stmt2 = con.prepareStatement(SqlStatements.sql_reset_rsv_main_fac_id_get);
        stmt2.setInt(++count2,rsv_id);
        ResultSet rs = stmt2.executeQuery();

        if (!(rs.next())) {
            // update;reset rsv_main_fac_id
            int count3 = 0;
            // prepare statement
            PreparedStatement stmt3 = con.prepareStatement(SqlStatements.sql_reset_rsv_main_fac_id);
            stmt3.setInt(++count3,rsv_id);
            stmt3.executeUpdate();
            stmt3.close();
        }
        stmt2.close();

        // for ae_fm_linage
        if(link_feedback_ind) {
            refreshRsv(rsv_id,root_ent_id);
        }
    }
    
    // for ae_fm_linkage
    public void refreshRsv(int rsv_id,int root_ent_id)
           throws SQLException ,cwException {
        try {
            PreparedStatement stmt3 = con.prepareStatement(SqlStatements.sql_get_site_rsv_link);
            int count3 = 0;
            stmt3.setInt(++count3,root_ent_id);
            ResultSet rs = stmt3.executeQuery();

            String[] ste_rsv_link = null;
            String className = null;
            Object aeClass = new Object();
            Timestamp rsv_start_date = null;
            Timestamp rsv_end_date = null;
            ViewFmReservation viewRsv = new ViewFmReservation(con);
            viewRsv.setRsv_id(rsv_id);
            rsv_start_date = viewRsv.getRsv_start_time(FSH_STATUS_CANCELLED);
            rsv_end_date = viewRsv.getRsv_end_time(FSH_STATUS_CANCELLED);

            String methodName = "refreshRsv";
            Class[] parameterTypes = {Integer.TYPE,Class.forName("java.sql.Timestamp"),Class.forName("java.sql.Timestamp")};
            Object[] parameters = {new Integer(rsv_id),rsv_start_date,rsv_end_date};
            Class[] parameterTypes_constructor = {Class.forName("java.sql.Connection")};
            Object[] parameters_constructor = {con};
            while (rs.next()) {
                ste_rsv_link = viewRsv.split((String)(rs.getString("ste_rsv_link")),viewRsv.DELIMITER);
                if (ste_rsv_link != null) {
                    for (int i = 0; i<ste_rsv_link.length;i++) {
                        className = (String)ste_rsv_link[i];
                        aeClass = Class.forName(className).getConstructor(parameterTypes_constructor).newInstance(parameters_constructor);
                        aeClass.getClass().getDeclaredMethod(methodName,parameterTypes).invoke(aeClass,parameters);
                    }
                }
            }

            stmt3.close();
        } catch (Exception e) {
            CommonLog.error(e.getMessage(),e);
            throw new cwException(e.getMessage());
        }
    }

    public void delFshInCart(String usr_id,int root_ent_id,String[] fsh_fac_id_lst,String[] fsh_start_time_lst,String[] fsh_upd_timestamp_lst)
            throws SQLException ,cwSysMessage {
        // sqlStatements
        StringBuffer sqlStr = new StringBuffer(1024);
        sqlStr.append(SqlStatements.sql_del_fsh_cart);
        PreparedStatement stmt;
        int count = 0;
        if ((fsh_fac_id_lst != null)&&(fsh_start_time_lst != null)&&(fsh_upd_timestamp_lst != null)) {
            // if multi ,add the multi sql___by fsh_fac_id_lst.length
            int fsh_fac_id_lst_length = fsh_fac_id_lst.length;
            int fsh_start_time_lst_length = fsh_start_time_lst.length;
            int fsh_upd_timestamp_lst_length = fsh_upd_timestamp_lst.length;
            if ((fsh_fac_id_lst_length > 0)&&(fsh_fac_id_lst_length == fsh_start_time_lst_length )&&(fsh_start_time_lst_length == fsh_upd_timestamp_lst_length)) {
                sqlStr.append(SqlStatements.sql_and);
                sqlStr.append("(");
                for (int i = 0; i < fsh_fac_id_lst_length ; i++ ) {
                    sqlStr.append(SqlStatements.sql_upd_fsh_multi);
                    sqlStr.append(SqlStatements.sql_or);
                }
                // Strip of "or"
                sqlStr.delete(sqlStr.length() - 3 , sqlStr.length());
                sqlStr.append(")");
            }
            // prepare statement
            stmt = con.prepareStatement(sqlStr.toString());
            // if multi sql,set the multi parameters
            if ((fsh_fac_id_lst_length > 0)&&(fsh_fac_id_lst_length == fsh_start_time_lst_length )&&(fsh_start_time_lst_length == fsh_upd_timestamp_lst_length)) {
                int fsh_fac_id = 0;
                Timestamp fsh_start_time;
                Timestamp fsh_upd_timestamp;
                for (int j = 0; j < fsh_fac_id_lst_length ; j++ ) {
                    // prepare parameters
                    fsh_fac_id = Integer.parseInt(fsh_fac_id_lst[j]);
                    fsh_start_time = Timestamp.valueOf(fsh_start_time_lst[j]);
                    fsh_upd_timestamp = Timestamp.valueOf(fsh_upd_timestamp_lst[j]);
                    stmt.setInt(++count,fsh_fac_id);
                    stmt.setTimestamp(++count,fsh_start_time);
                    stmt.setTimestamp(++count,fsh_upd_timestamp);
                }
            }
        } else {
            // prepare statement
            // clear cart (when closing window)
            // need check the create_usr_id and owner_ent_id
            sqlStr.append(SqlStatements.sql_and);
            sqlStr.append(SqlStatements.sql_fsh_audit_trail);
            stmt = con.prepareStatement(sqlStr.toString());
            stmt.setString(++count,usr_id);
            stmt.setInt(++count,root_ent_id);

        }
        //del count lines and line > 0 then stmt.executeUpdate() < 1 means executeUpdate Error occurred
        // count = 0 means no fsh can be deleted,will not throw cwMessage

        if ((stmt.executeUpdate() < 1)&&(count > 0)) {
            throw new cwSysMessage(SMSG_FMT_DEL_FAIL);
        }

        stmt.close();
    }


    // getFshDtl___rsv_id,root_ent_id
    public Hashtable[] getFshDtlRsv(int rsv_id,int root_ent_id)
           throws SQLException ,cwSysMessage {

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_fsh_RsvId);
        int count = 0;
        stmt.setInt(++count, rsv_id);
        stmt.setInt(++count,root_ent_id);
        stmt.setInt(++count, rsv_id);
        stmt.setInt(++count,root_ent_id);

        ResultSet rs = stmt.executeQuery();
        // process ResultSet to Hashtable[] ; by ViewFmRsvDtl.procResult(ResultSet rs);
        Hashtable[] resultTb = ViewFmReservation.procResult(rs);
        stmt.close();
        if ((resultTb == null)||(resultTb.length <= 0) ) {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NO_EXIST);
        }
        return resultTb;

    }
    //
    public Hashtable[] prepCancelFsh(String usr_id,int root_ent_id,int rsv_id,String[] fsh_fac_id_lst,String[] fsh_start_time_lst,String[] fsh_upd_timestamp_lst)
            throws SQLException ,cwSysMessage {
        // sqlStatements
        StringBuffer sqlStr = new StringBuffer(1024);
        sqlStr.append(SqlStatements.sql_cancel_fsh_prep);
        // if multi ,add the multi sql___by fsh_fac_id_lst.length
        int fsh_fac_id_lst_length = fsh_fac_id_lst.length;
        int fsh_start_time_lst_length = fsh_start_time_lst.length;
        int fsh_upd_timestamp_lst_length = fsh_upd_timestamp_lst.length;
        if ((fsh_fac_id_lst_length > 0)&&(fsh_fac_id_lst_length == fsh_start_time_lst_length )&&(fsh_start_time_lst_length == fsh_upd_timestamp_lst_length)) {
            sqlStr.append(SqlStatements.sql_and);
            sqlStr.append("(");
            for (int i = 0; i < fsh_fac_id_lst_length ; i++ ) {
                sqlStr.append(SqlStatements.sql_upd_fsh_multi);
                sqlStr.append(SqlStatements.sql_or);
            }
            // Strip of "or"
            sqlStr.delete(sqlStr.length() - 3 , sqlStr.length());
            sqlStr.append(")");
        } else {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NO_EXIST);
        }
        sqlStr.append(SqlStatements.sql_orderBy);
        // prepare statement
        PreparedStatement stmt = con.prepareStatement(sqlStr.toString());
        // set two parameters ahead
        int count = 0;
        stmt.setInt(++count,rsv_id);
        stmt.setInt(++count,root_ent_id);
        // if multi sql,set the multi parameters
        if (fsh_fac_id_lst_length > 0) {
            int fsh_fac_id = 0;
            Timestamp fsh_start_time;
            Timestamp fsh_upd_timestamp;
            for (int j = 0; j < fsh_fac_id_lst_length ; j++ ) {
                // prepare parameters
                fsh_fac_id = Integer.parseInt(fsh_fac_id_lst[j]);
                fsh_start_time = Timestamp.valueOf(fsh_start_time_lst[j]);
                fsh_upd_timestamp = Timestamp.valueOf(fsh_upd_timestamp_lst[j]);
                stmt.setInt(++count ,fsh_fac_id);
                stmt.setTimestamp(++count ,fsh_start_time);
                stmt.setTimestamp(++count ,fsh_upd_timestamp);
            }
        }
        ResultSet rs = stmt.executeQuery();
        if (rs.isAfterLast() && rs.isBeforeFirst()) {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NO_EXIST);
        }
        // process ResultSet to Hashtable[] ; by ViewFmReservation.procResult(ResultSet rs);
        Hashtable[] resultTb = ViewFmReservation.procResult(rs);

        // close the prepareStatement
        stmt.close();
        if ((resultTb == null)||(resultTb.length <= 0) ) {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NO_EXIST);
        }
        // return
        return resultTb;

    }
    //getFshDtl___rsv_id is null,fsh_create_usr_id,root_ent_id
    public Hashtable[] getFshDtlCart(String usr_id,int root_ent_id)
           throws SQLException ,cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_fsh_cart);
        int count = 0;
        stmt.setString(++count, usr_id);
        stmt.setInt(++count,root_ent_id);
        stmt.setString(++count, usr_id);        
        stmt.setInt(++count,root_ent_id);        

        ResultSet rs = stmt.executeQuery();
        /*
        if (rs.isAfterLast() && rs.isBeforeFirst()) {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NO_EXIST);
        }
        */
        Hashtable[] resultTb = ViewFmReservation.procResult(rs);
        stmt.close();
        /*
        if ((resultTb == null)||(resultTb.length <= 0) ) {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NO_EXIST);
        }
        */
        return resultTb;
    }
    /**
     * public Vector getFshAuditTrail
     * @param fsh_fac_id
     * @param fsh_start_time
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    // for access control
    //
    public Vector getFshAuditTrail(int fsh_fac_id, Timestamp fsh_start_time)
                throws SQLException ,cwSysMessage{
        //
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_FshCreateUserID);
        int count = 0;
        stmt.setInt(++count, fsh_fac_id);
        stmt.setTimestamp(++count,fsh_start_time);
        ResultSet rs = stmt.executeQuery();

        if (rs.isAfterLast() && rs.isBeforeFirst()) {
            throw new cwSysMessage(SMSG_FMT_GET_FSH_NO_EXIST);
        }

        Vector result = ViewFmReservation.procResultVector(rs);
        // close statement
        stmt.close();
        return result;
    }

    /* DENNIS BEGIN */
    //Property of a facility schedule
    private int fsh_fac_id;
    private Timestamp fsh_start_time;
    private Timestamp fsh_end_time;
    private Timestamp fsh_date;
    private int fsh_rsv_id;
    private String rsv_purpose;
    private String rsv_status;
    private int rsv_participant_no;
    private String fsh_status;
    private int fsh_create_usr_ent_id;
    private String fsh_create_usr_first_name_bil;
    private String fsh_create_usr_last_name_bil;
    private String fsh_create_usr_display_bil;
    private String fac_title;
    private int fsh_owner_ent_id;
    private Timestamp fsh_create_timestamp;
    private String fsh_create_usr_id;
    private Timestamp fsh_upd_timestamp;
    private String fsh_upd_usr_id;
    //private Connection conn;
    private Vector v_conflictFsh;
    private String rsv_ent_usr_first_name_bil;
    private String rsv_ent_usr_last_name_bil;
    private String rsv_ent_usr_display_bil;
    private int rsv_ent_usr_ent_id;
    
    /**
    Use for checking conflict in facility schedule.
    If isDbRecord = true, no need to search database to see if this facility schedule
    having conflict.
    */
    private boolean isDbRecord = false;

    /* Setters */
    public void setFsh_fac_id(int fac_id) {
        this.fsh_fac_id = fac_id;
    }
    public void setFsh_start_time(Timestamp time) {
        this.fsh_start_time = time;
    }
    public void setFsh_end_time(Timestamp time) {
        this.fsh_end_time = time;
    }
    public void setFsh_date(Timestamp date) {
        this.fsh_date = date;
    }
    public void setFsh_rsv_id(int rsv_id) {
        this.fsh_rsv_id = rsv_id;
    }
    public void setRsv_purpose(String rsv_purpose) {
        this.rsv_purpose = rsv_purpose;
    }
    public void setRsv_status(String rsv_status) {
        this.rsv_status = rsv_status;
    }
    public void setRsv_participant_no(int rsv_participant_no) {
        this.rsv_participant_no = rsv_participant_no;
    }
    public void setFsh_status(String fsh_status) {
        this.fsh_status = fsh_status;
    }
    public void setFsh_create_usr_ent_id(int fsh_create_usr_ent_id) {
        this.fsh_create_usr_ent_id = fsh_create_usr_ent_id;
    }
    public void setFsh_create_usr_first_name_bil(String fsh_create_usr_first_name_bil) {
        this.fsh_create_usr_first_name_bil = fsh_create_usr_first_name_bil;
    }
    public void setFsh_create_usr_last_name_bil(String fsh_create_usr_last_name_bil) {
        this.fsh_create_usr_last_name_bil = fsh_create_usr_last_name_bil;
    }
    public void setFsh_create_usr_display_bil(String fsh_create_usr_display_bil) {
        this.fsh_create_usr_display_bil = fsh_create_usr_display_bil;
    }
    public void setFac_title(String fac_title) {
        this.fac_title = fac_title;
    }
    public void setFsh_owner_ent_id(int owner_ent_id) {
        this.fsh_owner_ent_id = owner_ent_id;
    }
    public void setFsh_create_timestamp(Timestamp timestamp) {
        this.fsh_create_timestamp = timestamp;
    }
    public void setFsh_create_usr_id(String usr_id) {
        this.fsh_create_usr_id = usr_id;
    }
    public void setFsh_upd_timestamp(Timestamp timestamp) {
        this.fsh_upd_timestamp = timestamp;
    }
    public void setFsh_upd_usr_id(String usr_id) {
        this.fsh_upd_usr_id = usr_id;
    }
    public void setCon(Connection conn) {
        this.con = conn;
    }
    public void setRsv_ent_usr_first_name_bil(String rsv_ent_usr_first_name_bil) {
        this.rsv_ent_usr_first_name_bil = rsv_ent_usr_first_name_bil;
    }
    public void setRsv_ent_usr_last_name_bil(String rsv_ent_usr_last_name_bil) {
        this.rsv_ent_usr_last_name_bil = rsv_ent_usr_last_name_bil;
    }
    public void setRsv_ent_usr_display_bil(String rsv_ent_usr_display_bil) {
        this.rsv_ent_usr_display_bil = rsv_ent_usr_display_bil;
    }
    public void setRsv_ent_usr_ent_id(int rsv_ent_usr_ent_id) {
        this.rsv_ent_usr_ent_id = rsv_ent_usr_ent_id;
    }    /*
    public void setV_conflictFsh(Vector v) {
        this.v_conflictFsh = v;
    }
    */

    /* Getters */
    public int getFsh_fac_id() {
        return this.fsh_fac_id;
    }
    public Timestamp getFsh_start_time() {
        return this.fsh_start_time;
    }
    public Timestamp getFsh_end_time() {
        return this.fsh_end_time;
    }
    public Timestamp getFsh_date() {
        return this.fsh_date;
    }
    public int getFsh_rsv_id() {
        return this.fsh_rsv_id;
    }
    public String getRsv_purpose() {
        return this.rsv_purpose;
    }
    public String getRsv_status() {
        return this.rsv_status;
    }
    public int getRsv_participant_no() {
        return this.rsv_participant_no;
    }
    public String getFsh_status() {
        return this.fsh_status;
    }
    public int getFsh_create_usr_ent_id() {
        return this.fsh_create_usr_ent_id;
    }
    public String getFsh_create_usr_first_name_bil() {
        return this.fsh_create_usr_first_name_bil;
    }
    public String getFsh_create_usr_last_name_bil() {
        return this.fsh_create_usr_last_name_bil;
    }
    public String getFsh_create_usr_display_bil() {
        return this.fsh_create_usr_display_bil;
    }
    public String getFac_title() {
        return this.fac_title;
    }
    public int getFsh_owner_ent_id() {
        return this.fsh_owner_ent_id;
    }
    public Timestamp getFsh_create_timestamp() {
        return this.fsh_create_timestamp;
    }
    public String getFsh_create_usr_id() {
        return this.fsh_create_usr_id;
    }
    public Timestamp getFsh_upd_timestamp() {
        return this.fsh_upd_timestamp;
    }
    public String getFsh_upd_usr_id() {
        return this.fsh_upd_usr_id;
    }
    //public
    public Connection getConn() {
        return this.con;
    }
    public String getRsv_ent_usr_first_name_bil() {
        return this.rsv_ent_usr_first_name_bil;
    }
    public String getRsv_ent_usr_last_name_bil() {
        return this.rsv_ent_usr_last_name_bil;
    }
    public String getRsv_ent_usr_display_bil() {
        return this.rsv_ent_usr_display_bil;
    }
    public int getRsv_ent_usr_ent_id() {
        return this.rsv_ent_usr_ent_id;
    }    /*
    public Vector getV_conflictFsh() throws SQLException{
        return this.v_conflictFsh;
    }
    */

    /**
    Find out facility schedules in database has conflict with this facility schedule.<BR>
    A facility schedule is said has conflict with this facility schedule if
    (DB.end_time > this.start_time AND DB.start_time < this.start_time)
    @param notIncludingStatus will not treat the facility schedules having this status as a conflict
    @return Vector of ViewFmFacilitySchedule containing conflict facility schedule
    */
    public Vector getConflictFsh(String notIncludingStatus) throws SQLException {
        if(this.v_conflictFsh == null) {
            if(this.isDbRecord) {
                this.v_conflictFsh = new Vector();
            }
            else {

                String nullString = cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING);
                String nullInt = cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER);
                StringBuffer SQLBuf = new StringBuffer(1024);
                
                SQLBuf.append(" Select rsv_id, rsv_purpose, rsv_status, rsv_participant_no, ")
                      .append(" fsh_date, fsh_start_time, fsh_end_time, fsh_status, ")
                      .append(" usr_ent_id, usr_first_name_bil, usr_last_name_bil, usr_display_bil, ")
                      .append(" fac_id, fac_title ")
                      .append(" From fmFacilitySchedule, fmFacility, RegUser, fmReservation ")
                      .append(" Where  ") 
                      .append(" fac_id = fsh_fac_id ") 
                      .append(" And usr_ent_id = rsv_ent_id ")
                      .append(" And fac_id = ? ")
                      .append(" And fsh_status <> ? ")
                      .append(" And fsh_end_time > ? ") 
                      .append(" And fsh_start_time < ? ")
                      .append(" And fsh_rsv_id = rsv_id ");
                /*
                SQLBuf.append(SqlStatements.get_conflict_fsh_select)
                      .append(SqlStatements.get_conflict_fsh_from).append(", fmReservation ")
                      .append(SqlStatements.get_conflict_fsh_where)
                      .append(" And rsv_id = fsh_rsv_id ");
                */
                SQLBuf.append(" Union ");
                SQLBuf.append(" Select ").append(nullInt).append(" as rsv_id, ")
                      .append(nullString).append(" as rsv_purpose, ")
                      .append(nullString).append(" as rsv_status, ")
                      .append(nullInt).append(" as rsv_participant_no, ")
                      .append(" fsh_date, fsh_start_time, fsh_end_time, fsh_status, ")
                      .append(nullInt).append(" as usr_ent_id, ")
                      .append(nullString).append(" as usr_first_name_bil, ")
                      .append(nullString).append(" as usr_last_name_bil, ")
                      .append(nullString).append(" as usr_display_bil, ")
                      .append(" fac_id, fac_title ")
                      .append(" From fmFacilitySchedule, fmFacility ")
                      .append(" Where  ") 
                      .append(" fac_id = fsh_fac_id ") 
                      .append(" And fac_id = ? ")
                      .append(" And fsh_status <> ? ")
                      .append(" And fsh_end_time > ? ") 
                      .append(" And fsh_start_time < ? ")
                      .append(" And fsh_rsv_id is null ");
                SQLBuf.append(" order by fsh_start_time ");

                //String SQL = OuterJoinSqlStatements.getConflictFsh(this.con);
                PreparedStatement stmt = this.con.prepareStatement(SQLBuf.toString());
                stmt.setInt(1, getFsh_fac_id());
                stmt.setString(2, notIncludingStatus);
                stmt.setTimestamp(3, getFsh_start_time());
                stmt.setTimestamp(4, getFsh_end_time());

                stmt.setInt(5, getFsh_fac_id());
                stmt.setString(6, notIncludingStatus);
                stmt.setTimestamp(7, getFsh_start_time());
                stmt.setTimestamp(8, getFsh_end_time());

                ResultSet rs = stmt.executeQuery();
                this.v_conflictFsh = new Vector();
                while(rs.next()) {
                    ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(this.con);
                    fshDtl.setFsh_rsv_id(rs.getInt("rsv_id"));
                    fshDtl.setRsv_purpose(rs.getString("rsv_purpose"));
                    fshDtl.setRsv_status(rs.getString("rsv_status"));
                    fshDtl.setRsv_participant_no(rs.getInt("rsv_participant_no"));
                    fshDtl.setFsh_date(rs.getTimestamp("fsh_date"));
                    fshDtl.setFsh_start_time(rs.getTimestamp("fsh_start_time"));
                    fshDtl.setFsh_end_time(rs.getTimestamp("fsh_end_time"));
                    fshDtl.setRsv_ent_usr_ent_id(rs.getInt("usr_ent_id"));
                    fshDtl.setRsv_ent_usr_first_name_bil(rs.getString("usr_first_name_bil"));
                    fshDtl.setRsv_ent_usr_last_name_bil(rs.getString("usr_last_name_bil"));
                    fshDtl.setRsv_ent_usr_display_bil(rs.getString("usr_display_bil"));
                    fshDtl.setFsh_fac_id(rs.getInt("fac_id"));
                    fshDtl.setFac_title(rs.getString("fac_title"));
                    fshDtl.isDbRecord = true;
                    this.v_conflictFsh.addElement(fshDtl);
                }
                stmt.close();
            }
        }
        return this.v_conflictFsh;
    }
    
    /**
    Check if this facility schedule has conflict.<BR>
    @param notIncludingStatus will not treat the facility schedules having this status as a conflict
    @return true if there exists facility schedule having conflict with this one.
            false if this.isDbRecord is false OR there exists facility schedule having conflict with this one.
    */
    public boolean hasConflict(String notIncludingStatus) throws SQLException {
        boolean hasConflict;
        if(isDbRecord) {
            hasConflict = false;
        } else {
            if(v_conflictFsh == null) {
                getConflictFsh(notIncludingStatus);
            }
            hasConflict = (v_conflictFsh.size() > 0);
        }
        return hasConflict;
    }

    /**
    Excursive lock table fmFacilitySchedule
    */
    public void lockTable() throws SQLException {
        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_lock_fsh);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    /**
    Take in a Vector of ViewFmFacilitySchedule and sort it in the order of facility name asc
    @return a new Vector of ViewFmFacilitySchedule ordered by facility name
    */
    public Vector orderByFacTitle(Vector v_fshDtl) throws SQLException {
        Vector v_fshDtlWithTitle = getFacTitle(v_fshDtl);
        Vector v_orderedFshDtl = new Vector();
        for(int i=0; i<v_fshDtlWithTitle.size(); i++) {
            ViewFmFacilitySchedule fshDtlWithTitle = (ViewFmFacilitySchedule) v_fshDtlWithTitle.elementAt(i);
            for(int j=0; j<v_fshDtl.size(); j++) {
                ViewFmFacilitySchedule inFshDtl = (ViewFmFacilitySchedule) v_fshDtl.elementAt(j);
                if(inFshDtl.getFsh_fac_id() == fshDtlWithTitle.getFsh_fac_id()) {
                    ViewFmFacilitySchedule orderedFshDtl = new ViewFmFacilitySchedule(this.con);
                    orderedFshDtl.setFsh_fac_id(fshDtlWithTitle.getFsh_fac_id());
                    orderedFshDtl.setFac_title(fshDtlWithTitle.getFac_title());
                    orderedFshDtl.setFsh_date(inFshDtl.getFsh_date());
                    orderedFshDtl.setFsh_start_time(inFshDtl.getFsh_start_time());
                    orderedFshDtl.setFsh_end_time(inFshDtl.getFsh_end_time());
                    orderedFshDtl.setFsh_owner_ent_id(inFshDtl.getFsh_owner_ent_id());
                    orderedFshDtl.setFsh_create_usr_id(inFshDtl.getFsh_create_usr_id());
                    orderedFshDtl.setFsh_create_timestamp(inFshDtl.getFsh_create_timestamp());
                    orderedFshDtl.setFsh_upd_usr_id(inFshDtl.getFsh_upd_usr_id());
                    orderedFshDtl.setFsh_upd_timestamp(inFshDtl.getFsh_upd_timestamp());
                    orderedFshDtl.setFsh_status(inFshDtl.getFsh_status());
                    v_orderedFshDtl.addElement(orderedFshDtl);
                }
            }
        }
        return v_orderedFshDtl;
    }


    /**
    Order the input Vector of ViewFmFacilitySchedule by facility title and return the result as a new Vector
    @return Vecotr of ViewFmFacilitySchedule with fac_title. Note that the returning Vector will
    only have distinct fac_id in it. Therefore the input Vector and return Vector may
    have different size.
    */
    private Vector getFacTitle(Vector v_fshDtl) throws SQLException {
        Vector v_fshDtlWithTitle = new Vector();
        if(v_fshDtl != null && v_fshDtl.size() > 0) {
            String sql_fsh_fac_id = getFacIdList(v_fshDtl);
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(" Select fac_id, fac_title ")
                  .append(" From fmFacility ")
                  .append(" Where fac_id in ").append(sql_fsh_fac_id)
                  .append(" And fac_status = ? ")
                  .append(" Order by fac_title asc ");
            PreparedStatement stmt = this.con.prepareStatement(SQLBuf.toString());
            stmt.setString(1, "ON");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ViewFmFacilitySchedule fshDtl = new ViewFmFacilitySchedule(this.con);
                fshDtl.setFsh_fac_id(rs.getInt("fac_id"));
                fshDtl.setFac_title(rs.getString("fac_title"));
                v_fshDtlWithTitle.addElement(fshDtl);
            }
            stmt.close();
        }
        return v_fshDtlWithTitle;
    }

    /**
    Convert the input Vector of ViewFmFacilitySchedule to a String of facility id in the format of
    "(fac_id1, fac_id2, fac_id3)"
    */
    private String getFacIdList(Vector v_fshDtl) {
        StringBuffer listBuf = new StringBuffer(100);
        String list;
        listBuf.append("(");
        if(v_fshDtl != null && v_fshDtl.size() > 0) {
            int i=0;
            for(i=0; i<v_fshDtl.size()-1; i++) {
                listBuf.append(((ViewFmFacilitySchedule)v_fshDtl.elementAt(i)).getFsh_fac_id()).append(",");
            }
            listBuf.append(((ViewFmFacilitySchedule)v_fshDtl.elementAt(i)).getFsh_fac_id());
        }
        listBuf.append(")");
        return listBuf.toString();
    }

    /**
    Add this facility schedule into cart
    */
    public void add2Cart()
        throws SQLException {

        if((getFsh_start_time()).after(getFsh_end_time())
            || (getFsh_start_time()).equals(getFsh_end_time())) {
            throw new SQLException("fsh_start_time MUST be before fsh_end_date");
        }
         if(getFsh_create_timestamp() == null) {
            Timestamp cur_time = cwSQL.getTime(this.con);
            setFsh_create_timestamp(cur_time);
            setFsh_upd_timestamp(cur_time);
        }
        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_add_fsh_2_cart);
        stmt.setInt(1, getFsh_fac_id());
        stmt.setTimestamp(2, getFsh_date());
        stmt.setTimestamp(3, getFsh_start_time());
        stmt.setTimestamp(4, getFsh_end_time());
        stmt.setString(5, getFsh_status());
        stmt.setInt(6, getFsh_owner_ent_id());
        stmt.setTimestamp(7, getFsh_create_timestamp());
        stmt.setString(8, getFsh_create_usr_id());
        stmt.setTimestamp(9, getFsh_upd_timestamp());
        stmt.setString(10, getFsh_upd_usr_id());
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    /**
    Add this facility schedule into a existing reservation
    */
    private void add2Rsv()
        throws SQLException, cwException {

        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_add_fsh_2_rsv);
        stmt.setInt(1, getFsh_fac_id());
        stmt.setTimestamp(2, getFsh_date());
        stmt.setTimestamp(3, getFsh_start_time());
        stmt.setTimestamp(4, getFsh_end_time());
        stmt.setString(5, getFsh_status());
        stmt.setInt(6, getFsh_owner_ent_id());
        stmt.setTimestamp(7, getFsh_create_timestamp());
        stmt.setString(8, getFsh_create_usr_id());
        stmt.setTimestamp(9, getFsh_upd_timestamp());
        stmt.setString(10, getFsh_upd_usr_id());
        stmt.setInt(11, getFsh_rsv_id());
        stmt.executeUpdate();
        stmt.close();
        
        //linking interface to outside system
        refreshRsv(getFsh_rsv_id(),getFsh_owner_ent_id());
        return;
    }

    /**
    Insert this facility schedule into database. <BR>
    Insert to a reservation id this.fsh_rsv_id > 0, else insert to cart
    */
    public void ins() throws SQLException, cwException {
        if((getFsh_start_time()).after(getFsh_end_time())
            || (getFsh_start_time()).equals(getFsh_end_time())) {
            throw new SQLException("fsh_start_time MUST be before fsh_end_date");
        }
        if(getFsh_create_timestamp() == null) {
            Timestamp cur_time = cwSQL.getTime(this.con);
            setFsh_create_timestamp(cur_time);
            setFsh_upd_timestamp(cur_time);
        }
        if(getFsh_rsv_id() > 0) {
            add2Rsv();
        } else {
            add2Cart();
        }
    }

    /**
    Update fsh_status and ,if isIns = true, fsh_rsv_id for the facility schedules
    in the input Vector.
    @param isIns indicate if these facility schedules are updated resulting from
    insertion of reservation
    */
    public void updFsh(int rsv_id, String fsh_status, Vector v_fshDtl, boolean isIns)
        throws SQLException {

        if(v_fshDtl != null && v_fshDtl.size() > 0) {
            StringBuffer SQLBuf = new StringBuffer(512);
            SQLBuf.append(" Update fmFacilitySchedule Set fsh_status = ? ");
            if(isIns) {
                SQLBuf.append(" ,fsh_rsv_id = ? ");
            }
            SQLBuf.append(" Where ");
            for(int i=0 ; i< v_fshDtl.size(); i++) {
                if(i > 0) {
                    SQLBuf.append(" OR ");
                }
                SQLBuf.append(" (fsh_fac_id = ? ")
                    .append(" and fsh_start_time = ? ")
                    .append(" and fsh_owner_ent_id = ? ")
                    .append(" and fsh_upd_timestamp = ? ");
                if(isIns) {
                    SQLBuf.append(" and fsh_rsv_id is null ");
                }
                SQLBuf.append(") ");
            }
            PreparedStatement stmt = this.con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setString(index++, fsh_status);
            if(isIns) {
                stmt.setInt(index++, rsv_id);
            }
            for(int i=0 ; i< v_fshDtl.size(); i++) {
                ViewFmFacilitySchedule fshDtl = (ViewFmFacilitySchedule) v_fshDtl.elementAt(i);
                stmt.setInt(index++, fshDtl.getFsh_fac_id());
                stmt.setTimestamp(index++, fshDtl.getFsh_start_time());
                stmt.setInt(index++, fshDtl.getFsh_owner_ent_id());
                stmt.setTimestamp(index++, fshDtl.getFsh_upd_timestamp());
            }
            int cnt = stmt.executeUpdate();
            stmt.close();
            if(cnt != v_fshDtl.size()) {
                throw new SQLException("Some facility schedules cannot be updated.");
            }
        }
        return;
    }

    /* DENNIS END */
    
    public double getTotalCost(int rsv_id, int root_ent_id) throws SQLException {
    	double totalCost = 0;
    	String sql = " SELECT sum(fac_fee) FROM fmFacilitySchedule INNER JOIN fmFacility ON (fsh_fac_id = fac_id) "
    		       + " WHERE fsh_cancel_usr_id is null AND fsh_owner_ent_id = ? AND ";
    	if (rsv_id > 0) {
    		sql += " fsh_rsv_id = ? ";
    	} else {
    		sql += " fsh_rsv_id is null ";
    	}
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	try {
    		pst = con.prepareStatement(sql);
    		int index = 1;
    		pst.setInt(index++, root_ent_id);
    		if (rsv_id > 0) {
    			pst.setInt(index++, rsv_id);
    		}
    		rs = pst.executeQuery();
    		if (rs.next()) {
    			totalCost = rs.getDouble(1);
    		}
    	} finally {
    		cwSQL.cleanUp(rs, pst);
    	}
    	return totalCost;
    }
    
}
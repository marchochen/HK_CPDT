package    com.cw.wizbank.db.view;

import java.util.Hashtable;
import java.util.Vector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.sql.*;
import com.cwn.wizbank.utils.CommonLog;

public class ViewFmReservation {
    // String DELIMITER
    public static final String DELIMITER = "~";
    // String for cwSysMessage
    //   for update failure
    public static final String SMSG_FMT_UPD_FAIL = "FMT001";
    public static final String SMSG_FMT_DEL_FAIL = "FMT002";
    //   for record not found
    public static final String SMSG_FMT_GET_RSV_NO_EXIST = "FMT004";
    public static final String SMSG_FMT_GET_FSH_NO_EXIST = "FMT005";
    //   "The Facility Schedule Record Not Found !"
    public static final String SMSG_FMT_GET_FTP_NONE = "FMT006";
    public static final String SMSG_FMT_GET_FAC_NONE = "FMT007";
    //------------------------------------------------------------------------

    public static final String SMSG_FMT_GET_TIME_NONE = "FMT003";

    private final static boolean DEBUG = false;
    // own type
    private final static String OWN_TYPE_CREATED    = "CREATED";
    private final static String OWN_TYPE_RESERVED    = "RESERVED";
    public static final String FSH_STATUS_PENCILLED_IN = "PENCILLED_IN";
    public static final String FSH_STATUS_RESERVED = "RESERVED";
    public static final String FSH_STATUS_CANCELLED = "CANCELLED";

    private Connection con = null;
    //------------------------------------------------------------------------
    // constructure
    public ViewFmReservation(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }

    // get reservation detail
    public Hashtable[] getRsvDtl(int rsv_id,int root_ent_id)
                throws SQLException ,cwSysMessage{

        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_rsv_RsvId);
        int count = 0;
        stmt.setInt(++count, rsv_id);
        stmt.setInt(++count,root_ent_id);
        ResultSet rs = stmt.executeQuery();

/*        if (rs.isAfterLast() && rs.isBeforeFirst()) {
            throw new cwSysMessage(SMSG_FMT_GET_RSV_NO_EXIST);
        }*/

        Hashtable[] resultTb = procResult(rs);
        if (resultTb.length == 0)
        	throw new cwSysMessage(SMSG_FMT_GET_RSV_NO_EXIST);
        stmt.close();
        return resultTb;
    }
    /**
     * cancel reservation
     * @param usr_id
     * @param root_ent_id
     * @param rsv_id
     * @param rsv_status
     * @param fsh_status
     * @param rsv_upd_timestamp
     * @param cancel_type
     * @param cancel_reason
     * @throws SQLException
     * @throws cwSysMessage
     */
    public void cancelRsv(int root_ent_id,String usr_id,int rsv_id,String rsv_status,String fsh_status,Timestamp rsv_upd_timestamp,String cancel_type,String cancel_reason,boolean link_feedback_ind)
                throws SQLException ,cwSysMessage,cwException {

        StringBuffer sqlStr = new StringBuffer(1024);
        sqlStr.append(SqlStatements.sql_cancel_rsv);

        // prepare statement
        PreparedStatement stmt = con.prepareStatement(sqlStr.toString());

        // get timestamp from database
        Timestamp timestampDb = cwSQL.getTime(con);
        // set parameters ahead
        int count = 0;
        stmt.setString(++count,rsv_status);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        stmt.setTimestamp(++count,timestampDb);
        stmt.setString(++count,usr_id);
        stmt.setString(++count,cancel_type);
        stmt.setString(++count,cancel_reason);
        stmt.setInt(++count,rsv_id);
      //stmt.setTimestamp(++count,rsv_upd_timestamp);

        if (stmt.executeUpdate() < 1) {
            throw new cwSysMessage(SMSG_FMT_UPD_FAIL);
        }
        stmt.close();

        ////////////////////////////////////////////
        // cancel fsh linked with this reservation
        // sql = update fmFacilitySchedule set fsh_status = ? ,fsh_upd_timestamp = ? ,
        // fsh_upd_usr_id = ? ,fsh_cancel_type = ? , fsh_cancel_reason = ?
        // WHERE fsh_create_usr_id = ? and fsh_owner_ent_id = ?
        StringBuffer sqlStr2 = new StringBuffer(1024);
        sqlStr2.append(SqlStatements.sql_cancel_fsh);

        // "update fmFacilitySchedule set fsh_status = ? ,fsh_upd_timestamp = ? ,fsh_upd_usr_id = ? ,fsh_cancel_timestamp = ?,
        // fsh_cancel_usr_id = ?,fsh_cancel_type = ? , fsh_cancel_reason = ? WHERE rsv_id = ? and fsh_status <> 'CANCELLED'";
        // prepare statement
        PreparedStatement stmt2 = con.prepareStatement(sqlStr2.toString());
        int count2 = 0;
        stmt2.setString(++count2,fsh_status);
        stmt2.setTimestamp(++count2,timestampDb);
        stmt2.setString(++count2,usr_id);
        stmt2.setTimestamp(++count2,timestampDb);
        stmt2.setString(++count2,usr_id);
        stmt2.setString(++count2,cancel_type);
        stmt2.setString(++count2,cancel_reason);
        stmt2.setInt(++count2,rsv_id);

        if (stmt2.executeUpdate() < 1) {
            throw new cwSysMessage(SMSG_FMT_UPD_FAIL);
        }
        stmt2.close();
        // for ae_fm_linage
        if(link_feedback_ind) {
            unlinkRsv(rsv_id,root_ent_id);
        }
    }
    // for ae_fm_linkage
    public void unlinkRsv(int rsv_id,int root_ent_id)
           throws SQLException ,cwException {
        try {
            PreparedStatement stmt3 = con.prepareStatement(SqlStatements.sql_get_site_rsv_link);
            int count3 = 0;
            stmt3.setInt(++count3,root_ent_id);
            ResultSet rs = stmt3.executeQuery();

            String[] ste_rsv_link = null;
            String className = null;
            Object aeClass = new Object();
            String methodName = "unlinkRsv";
            Class[] parameterTypes = {Integer.TYPE};
            Object[] parameters = {new Integer(rsv_id)};
            Class[] parameterTypes_constructor = {Class.forName("java.sql.Connection")};
            Object[] parameters_constructor = {con};
            while (rs.next()) {
                ste_rsv_link = split((String)(rs.getString("ste_rsv_link")),DELIMITER);
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
            throw new cwException(e.getMessage());
        }
    }
    /**
    method to split a given parameter to a string array using the given separator <BR>
    Return : string array containing the values of the parameter <BR>
             null if the parameter cannot be found <BR>
    */
    static String[] split(String in, String delimiter)
    {
        Vector q = new Vector();
        String[] result = null;

        if (in == null || in.length()==0)
            return result;

        int pos =0;
        pos = in.indexOf(delimiter);

        while (pos >= 0) {
            String val = new String();
            if (pos>0) {
                val = in.substring(0,pos);
            }
            q.addElement(val);
            in = in.substring(pos + delimiter.length(), in.length());
            pos = in.indexOf(delimiter);
        }

        if (in.length() > 0) {
            q.addElement(in);
        }

        result = new String[q.size()];
        for (int i=0; i<q.size();i++) {
            result[i] = (String) q.elementAt(i);
        }

        return result;

    }
    // prepCancelRsv(con,usr_id,root_ent_id,rsv_id,rsv_upd_timestamp);

    public Hashtable[] prepCancelRsv(String usr_id,int root_ent_id,int rsv_id,Timestamp rsv_upd_timestamp)
           throws SQLException ,cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_cancel_rsv_prep);
        int count = 0;
        stmt.setInt(++count, rsv_id);
        stmt.setInt(++count,root_ent_id);
        //stmt.setString(++count,rsv_upd_timestamp);
        //stmt.setTimestamp(++count, rsv_upd_timestamp);
        
        ResultSet rs = stmt.executeQuery();

        /*if (rs.isAfterLast() && rs.isBeforeFirst()) {
            //System.out.println("in view");
        	throw new cwSysMessage(SMSG_FMT_GET_RSV_NO_EXIST);
        }*/

        Hashtable[] resultTb = procResult(rs);
        if (resultTb.length == 0)
        	throw new cwSysMessage(SMSG_FMT_GET_RSV_NO_EXIST);
        stmt.close();
        return resultTb;
    }
    // process the ResultSet
    // static for other use;maybe put into a new class , or util class
    // return Hashtable[]
    public static Hashtable[] procResult(ResultSet rs) throws SQLException {
        Hashtable record = new Hashtable();
        Vector result = new Vector();

        ResultSetMetaData rsmd = rs.getMetaData();
        //for loop variable colName
        int columnCount = rsmd.getColumnCount();
        String colName = "";
        while (rs.next()) {
            record = new Hashtable();
            for(int i = 1 ;i <= columnCount; i++) {
                // getColumnName
                colName = rsmd.getColumnName(i);
                // put into the Hashtable
                //System.out.println(""+colName+rs.getObject(colName));

                // << BEGIN for ORACLE migration
                Object objValue;
                switch (rsmd.getColumnType(i)) {
                    case Types.DATE :
                    case Types.TIME :
                    case Types.TIMESTAMP :
                        objValue = rs.getTimestamp(colName);
                        break;
                    case Types.INTEGER :
                    case Types.NUMERIC :
                        if (rs.getString(colName)==null)objValue=null;
                        else
                          objValue = Integer.valueOf(rs.getString(colName));
                        break;
                    case Types.VARCHAR :
                    case Types.CHAR :
                        objValue = rs.getString(colName);
                        break;
                    default :
                        objValue = rs.getString(colName);
                        break;
                }
                // >> END

//                if ((rs.getObject(colName)) == null) {
                if (objValue == null) {
                    // if null put a String object("") into the hashtable
                    // "" will be the value of the property or the content of the element
                    // to be confirmed from front(xsl,javascript.etc)
                    record.put(colName.toLowerCase(), "");
                } else {
//                    record.put(colName,rs.getObject(colName));
                    record.put(colName.toLowerCase(), objValue);
                }
            }
            result.addElement(record);
        }
        Hashtable[] resultArray = new Hashtable[result.size()];
        return (Hashtable[])result.toArray(resultArray);
    }
    // process the ResultSet
    // static for other use;maybe put into a new class , or util class
    // return Vector
    public static Vector procResultVector(ResultSet rs) throws SQLException {
        Hashtable record = new Hashtable();
        Vector result = new Vector();
        ResultSetMetaData rsmd = rs.getMetaData();
        //for loop variable colName
        int columnCount = rsmd.getColumnCount();
        String colName = "";
        while (rs.next()) {
            record = new Hashtable();
            for(int i = 1 ;i <= columnCount; i++) {
                // getColumnName
                colName = rsmd.getColumnName(i);
                // put into the Hashtable

                // << BEGIN for ORACLE migration
                Object objValue;
                switch (rsmd.getColumnType(i)) {
                    case Types.DATE :
                    case Types.TIME :
                    case Types.TIMESTAMP :
                        objValue = rs.getTimestamp(colName);
                        break;
                    case Types.INTEGER :
                    case Types.NUMERIC :
                        objValue = Integer.valueOf(rs.getString(colName));
                        break;
                    case Types.VARCHAR :
                    case Types.CHAR :
                        objValue = rs.getString(colName);
                        break;
                    default :
                        objValue = rs.getString(colName);
                        break;
                }
                // >> END

//                if ((rs.getObject(colName)) == null) {
                if (objValue == null) {
                    // if null put a String object("") into the hashtable
                    // "" will be the value of the property or the content of the element
                    // to be confirmed from front(xsl,javascript.etc)
                    record.put(colName.toLowerCase(), "");
                } else {
//                    record.put(colName,rs.getObject(colName));
                    record.put(colName.toLowerCase(), objValue);
                }
            }
            result.addElement(record);
        }
        // Hashtable[] resultArray = new Hashtable[result.size()];
        return result;
    }

    // for access control
    // public Vector getRsvAuditTrail
    /**
     *
     * @param rsv_id
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     */
    public Vector getRsvAuditTrail(int rsv_id)
                throws SQLException ,cwSysMessage{
        //
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_RsvCreateUserID);
        int count = 0;
        stmt.setInt(++count, rsv_id);

        ResultSet rs = stmt.executeQuery();

        Vector result = ViewFmReservation.procResultVector(rs);
        if (result.size() == 0) {
            throw new cwSysMessage(SMSG_FMT_GET_RSV_NO_EXIST);
        }
        
        // close statement
        stmt.close();
        return result;
    }
    public Vector getFacAuditTrail(int fac_id)
                throws SQLException ,cwSysMessage{
        //
        //
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_FacCreateUserID);
        int count = 0;
        stmt.setInt(++count, fac_id);

        ResultSet rs = stmt.executeQuery();

/*        if (rs.isAfterLast() && rs.isBeforeFirst()) {
            throw new cwSysMessage(SMSG_FMT_GET_FAC_NONE);
        }
*/
        Vector result = ViewFmReservation.procResultVector(rs);
        if (result == null || result.size() == 0) {
        	throw new cwSysMessage(SMSG_FMT_GET_FAC_NONE);
        }
        // close statement
        stmt.close();
        return result;
    }



    //---------------------------------------------------------------------------------------------------
    public Hashtable[] getRecord(int owner_ent_id, String usr_id, int usr_ent_id,
                                 Timestamp start, Timestamp end, String own_type,
                                 int[] fac_id, String[] status) throws SQLException {
        /**
         * construct the sql statement
         */
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append(OuterJoinSqlStatements.getRecordList());
        int i = 0;
        // construct the "fac_id IN " sub_clause
        StringBuffer fac_id_list = new StringBuffer("(");
        for (i = 0; i < fac_id.length; i++)
            if (i != fac_id.length - 1)
                fac_id_list.append("?, ");
            else
                fac_id_list.append("?");
        fac_id_list.append(")");
        sqlStr.append(OuterJoinSqlStatements.sql_get_record_list_fac_id).append(fac_id_list);

        // construct the "status IN " sub_clause
        StringBuffer status_list = new StringBuffer("(");
        for (i = 0; i < status.length; i++)
            if (i != status.length - 1)
                status_list.append("?, ");
            else
                status_list.append("?");
        status_list.append(")");
        sqlStr.append(OuterJoinSqlStatements.sql_get_record_list_status).append(status_list);

        // created by current user / reserved for current user
        if (own_type != null) {
            if (own_type.equalsIgnoreCase(this.OWN_TYPE_CREATED))
                sqlStr.append(OuterJoinSqlStatements.sql_get_record_list_created);
            if (own_type.equalsIgnoreCase(this.OWN_TYPE_RESERVED))
                sqlStr.append(OuterJoinSqlStatements.sql_get_record_list_reserved);
        }

        // join the table together
        sqlStr.append(OuterJoinSqlStatements.sql_get_record_list_join);

        if (this.DEBUG)
        	CommonLog.debug(sqlStr.toString());

        PreparedStatement stmt = con.prepareStatement(sqlStr.toString());
        int j = 1;
        stmt.setInt(j++, owner_ent_id);    // owner_ent_id
        stmt.setTimestamp(j++, start);    // start date
        stmt.setTimestamp(j++, end);    // end date
        // facility id
        for (i = 0; i < fac_id.length; i++)
            stmt.setInt(j++, fac_id[i]);
        // status
        for (i = 0; i < status.length; i++)
            stmt.setString(j++, status[i]);
        // own_type
        if (own_type != null) {
            if (own_type.equalsIgnoreCase(this.OWN_TYPE_CREATED))
                stmt.setString(j, usr_id);
            if (own_type.equalsIgnoreCase(this.OWN_TYPE_RESERVED))
                stmt.setInt(j, usr_ent_id);
        }

        // execute the query
        ResultSet rs = stmt.executeQuery();

        Vector result = new Vector();
        Hashtable record = null;
        while (rs.next()) {
            record = new Hashtable();
            record.put("fsh_status", rs.getString("fsh_status"));
            record.put("fsh_date", rs.getTimestamp("fsh_date"));
            record.put("fsh_start_time", rs.getTimestamp("fsh_start_time"));
            record.put("fsh_end_time", rs.getTimestamp("fsh_end_time"));
            record.put("fsh_create_usr_id", rs.getString("fsh_create_usr_id"));
            record.put("fsh_create_timestamp", rs.getTimestamp("fsh_create_timestamp"));
            record.put("fsh_upd_usr_id", rs.getString("fsh_upd_usr_id"));
            record.put("fsh_upd_timestamp", rs.getTimestamp("fsh_upd_timestamp"));
            // added for download csv
            if (rs.getString("fsh_cancel_usr_id") != null) {
                record.put("fsh_cancel_usr_id", rs.getString("fsh_cancel_usr_id"));
                record.put("fsh_cancel_timestamp", rs.getTimestamp("fsh_cancel_timestamp"));
                if (rs.getString("fsh_cancel_type") != null)
                    record.put("fsh_cancel_type", rs.getString("fsh_cancel_type"));
                if (rs.getString("fsh_cancel_reason") != null)
                    record.put("fsh_cancel_reason", rs.getString("fsh_cancel_reason"));
            }
            record.put("fac_id", new Integer(rs.getInt("fac_id")));
            record.put("fac_title", rs.getString("fac_title"));
            record.put("fac_fee", new Double(rs.getDouble("fac_fee")));
            if (rs.getInt("rsv_id") != 0) {
                record.put("rsv_id", new Integer(rs.getInt("rsv_id")));
                record.put("rsv_status", rs.getString("rsv_status"));
                record.put("rsv_purpose", rs.getString("rsv_purpose"));
                record.put("rsv_ent_id", new Integer(rs.getInt("rsv_ent_id")));
                if (rs.getString("usr_first_name_bil") != null)
                    record.put("first_name", rs.getString("usr_first_name_bil"));
                if (rs.getString("usr_last_name_bil") != null)
                    record.put("last_name", rs.getString("usr_last_name_bil"));
                record.put("display_name", rs.getString("usr_display_bil"));
                if (rs.getInt("rsv_participant_no") != 0)
                    record.put("rsv_participant_no", new Integer(rs.getInt("rsv_participant_no")));
            }
            result.addElement(record);
        }
        stmt.close();

        if (this.DEBUG)
        	CommonLog.debug(String.valueOf(result.size()));
        Hashtable[] resultArray = new Hashtable[result.size()];
        return (Hashtable[])result.toArray(resultArray);
    }

    public Vector getCalendar(int owner_ent_id, Timestamp start, Timestamp end,
                              int[] fac_id, int[] ext_fac_id, String[] status)
        throws SQLException {
        /**
         * construct the sql statement
         */
        StringBuffer sqlStr = new StringBuffer();
        String str = OuterJoinSqlStatements.getFacilitySchedule();
        sqlStr.append(str.substring(0, str.indexOf(OuterJoinSqlStatements.IDENTIFIER)));

        int i = 0;
        // construct the "fac_id IN " sub_clause
        StringBuffer fac_id_list = new StringBuffer("(");
        for (i = 0; i < fac_id.length + ext_fac_id.length; i++) {
            fac_id_list.append("?, ");
        }

        sqlStr.append(fac_id_list.substring(0, fac_id_list.length() - 2)).append(")");

        sqlStr.append(str.substring(str.indexOf(OuterJoinSqlStatements.IDENTIFIER) + 1));

        PreparedStatement stmt = con.prepareStatement(sqlStr.toString());
        int j = 1;
        stmt.setInt(j++, owner_ent_id);    // owner_ent_id
        stmt.setTimestamp(j++, start);    // start date
        stmt.setTimestamp(j++, end);    // end date
        // status
        stmt.setString(j++, status[0]);
        stmt.setString(j++, status[1]);
        // facility id
        for (i = 0; i < fac_id.length; i++) {
            stmt.setInt(j++, fac_id[i]);
        }
        for (i = 0; i < ext_fac_id.length; i++) {
            stmt.setInt(j++, ext_fac_id[i]);
        }

        if (this.DEBUG)
        	CommonLog.debug(sqlStr.toString());

        // execute the query
        ResultSet rs = stmt.executeQuery();
//        if (rs.isAfterLast() && rs.isBeforeFirst()) {
//            throw new SQLException("Fails to list the calendar.");
//        }

        Vector result = new Vector();
        Hashtable record = null;

        while (rs.next()) {
            record = new Hashtable();
            record.put("fac_id",    new Integer(rs.getInt("fac_id")));
            record.put("fac_title",        rs.getString("fac_title"));
            record.put("fac_status",    rs.getString("fac_status"));
            // may be null!
            if (rs.getTimestamp("fsh_date") != null) {
                record.put("fsh_date",        rs.getTimestamp("fsh_date"));
                record.put("fsh_start_time",rs.getTimestamp("fsh_start_time"));
                record.put("fsh_end_time",    rs.getTimestamp("fsh_end_time"));
//                record.put("fsh_date",        rs.getTimestamp("fsh_date"));
//                record.put("fsh_start_time",rs.getTimestamp("fsh_start_time"));
//                record.put("fsh_end_time",    rs.getTimestamp("fsh_end_time"));
            }
            result.addElement(record);
        }

        stmt.close();
        return result;
    }

    /**
     * get the initial time slot with the special owner
     * @param owner_id
     * @return
     * @throws SQLException
     */
    public String[][] getSlotValue(int owner_id) throws cwSysMessage {
        String[][] result = null;
        try {
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_slot);
            stmt.setInt(1, owner_id);

            // execute the query
            ResultSet rs = stmt.executeQuery();

            result = new String[3][3];
            int i = 0;
            while (rs.next()) {
                result[i][0] = rs.getString("tsl_id");
                result[i][1] = rs.getString("tsl_start_time");
                result[i++][2] = rs.getString("tsl_end_time");
            }
            stmt.close();
        } catch (SQLException e) {
            throw new cwSysMessage(this.SMSG_FMT_GET_TIME_NONE);
        }

        return result;
    }

    /**
     * get the minimum gap for calendar and the shortest length of a booking
     * @param owner_id
     * @return
     * @throws SQLException
     */
    public int[] getLimitTime(int owner_id) throws cwSysMessage {
        int[] result = null;
        try {
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_limit_time);
            stmt.setInt(1, owner_id);

            // execute the query
            ResultSet rs = stmt.executeQuery();

            result = new int[2];
            if (rs.next()) {
                result[0] = rs.getInt("ste_rsv_min_gap");
                result[1] = rs.getInt("ste_rsv_min_len");
            }
            stmt.close();
        } catch (SQLException e) {
            throw new cwSysMessage(this.SMSG_FMT_GET_TIME_NONE);
        }

        return result;
    }

    /* DENNIS BEGIN */
    //Property of a reservation
    private int rsv_id;
    private String rsv_purpose;
    private String rsv_desc;
    private int rsv_ent_id;
    private int rsv_participant_no;
    private int rsv_main_fac_id;
    private String rsv_status;
    private int rsv_owner_ent_id;
    private Timestamp rsv_create_timestamp;
    private String rsv_create_usr_id;
    private Timestamp rsv_upd_timestamp;
    private String rsv_upd_usr_id;
    private Timestamp rsv_start_time;
    private Timestamp rsv_end_time;

    //Setters
    public void setRsv_id(int rsv_id) {
        this.rsv_id = rsv_id;
    }
    public void setRsv_purpose(String purpose) {
        this.rsv_purpose = purpose;
    }
    public void setRsv_desc(String desc) {
        this.rsv_desc = desc;
    }
    public void setRsv_ent_id(int ent_id) {
        this.rsv_ent_id = ent_id;
    }
    public void setRsv_participant_no(int participant_no) {
        this.rsv_participant_no = participant_no;
    }
    public void setRsv_main_fac_id(int fac_id) {
        this.rsv_main_fac_id = fac_id;
    }
    public void setRsv_status(String rsv_status) {
        this.rsv_status = rsv_status;
    }
    public void setRsv_owner_ent_id(int owner_ent_id) {
        this.rsv_owner_ent_id = owner_ent_id;
    }
    public void setRsv_create_timestamp(Timestamp timestamp) {
        this.rsv_create_timestamp = timestamp;
    }
    public void setRsv_create_usr_id(String usr_id) {
        this.rsv_create_usr_id = usr_id;
    }
    public void setRsv_upd_timestamp(Timestamp timestamp) {
        this.rsv_upd_timestamp = timestamp;
    }
    public void setRsv_upd_usr_id(String usr_id) {
        this.rsv_upd_usr_id = usr_id;
    }
    private void setRsv_start_time(Timestamp time) {
        this.rsv_start_time= time;
    }
    private void setRsv_end_time(Timestamp time) {
        this.rsv_end_time= time;
    }

    //Getters
    public int getRsv_id() {
        return this.rsv_id;
    }
    public String getRsv_purpose() {
        return this.rsv_purpose;
    }
    public String getRsv_desc() {
        return this.rsv_desc;
    }
    public int getRsv_ent_id() {
        return this.rsv_ent_id;
    }
    public int getRsv_participant_no() {
        return this.rsv_participant_no;
    }
    public int getRsv_main_fac_id() {
        return this.rsv_main_fac_id;
    }
    public String getRsv_status() {
        return this.rsv_status;
    }
    public int getRsv_owner_ent_id() {
        return this.rsv_owner_ent_id;
    }
    public Timestamp getRsv_create_timestamp() {
        return this.rsv_create_timestamp;
    }
    public String getRsv_create_usr_id() {
        return this.rsv_create_usr_id;
    }
    public Timestamp getRsv_upd_timestamp() {
        return this.rsv_upd_timestamp;
    }
    public String getRsv_upd_usr_id() {
        return this.rsv_upd_usr_id;
    }
    public Timestamp getRsv_start_time(String notIncludingFshStatus) throws SQLException {
        if(this.rsv_start_time == null) {
            getRsvStartEndTime(notIncludingFshStatus);
        }
        return this.rsv_start_time;
    }
    public Timestamp getRsv_end_time(String notIncludingFshStatus) throws SQLException {
        if(this.rsv_end_time == null) {
            getRsvStartEndTime(notIncludingFshStatus);
        }
        return this.rsv_end_time;
    }

    /**
    Insert this reservation into database and set the new rsv_id
    */
    public void ins() throws SQLException {
        //insert this reservation into database
        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_ins_rsv, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(1, getRsv_purpose());
        stmt.setString(2, getRsv_desc());
        stmt.setInt(3, getRsv_ent_id());
        stmt.setInt(4, getRsv_participant_no());
        if(getRsv_main_fac_id() > 0) {
            stmt.setInt(5, getRsv_main_fac_id());
        } else {
            stmt.setString(5, null);
        }
        stmt.setString(6, getRsv_status());
        stmt.setInt(7, getRsv_owner_ent_id());
        stmt.setTimestamp(8, getRsv_create_timestamp());
        stmt.setString(9, getRsv_create_usr_id());
        stmt.setTimestamp(10, getRsv_upd_timestamp());
        stmt.setString(11, getRsv_upd_usr_id());
        stmt.executeUpdate();
   
        //get back the max rsv_id
        setRsv_id((int)cwSQL.getAutoId(con, stmt, "fmReservation", "rsv_id"));
     	stmt.close();
        return;
    }

    public void upd(Timestamp lastUpdTime) throws SQLException, cwSysMessage {
        if(getRsv_id() <= 0) {
            throw new cwSysMessage(SMSG_FMT_GET_RSV_NO_EXIST);
        }
        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_upd_rsv);
        stmt.setString(1, getRsv_purpose());
		stmt.setString(2, getRsv_desc());
        stmt.setInt(3, getRsv_ent_id());
        stmt.setInt(4, getRsv_participant_no());
        if(getRsv_main_fac_id() > 0) {
            stmt.setInt(5, getRsv_main_fac_id());
        } else {
            stmt.setString(5, null);
        }
        stmt.setTimestamp(6, getRsv_upd_timestamp());
        stmt.setString(7, getRsv_upd_usr_id());
        stmt.setInt(8, getRsv_id());
        //stmt.setTimestamp(9, lastUpdTime);
        int cnt = stmt.executeUpdate();
        stmt.close();
        if(cnt != 1) {
            throw new cwSysMessage(SMSG_FMT_UPD_FAIL);
        }
        return;
    }


    /**
    Get the start, end time of this reservation.
    @param notIncludingStatus will not consider the facility schedules having this status in the calculation of reservaton start, end time
    */
    public Timestamp[] getRsvStartEndTime(String notIncludingFshStatus) throws SQLException {
        return getRsvStartEndTime(notIncludingFshStatus, "com.cw.wizbank.db.view.ViewFmRoom");
    }


    public Timestamp[] getRsvStartEndTime(String notIncludingFshStatus, String includingFshType) throws SQLException {
        Timestamp[] rsvStartEndTime = new Timestamp[2];
        //PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_get_rsv_start_end_time);
        String SQL = " Select min(fsh_start_time) as rsv_start_time , "
                   + " max(fsh_end_time) as rsv_end_time "
                   + " From fmFacilitySchedule ";

        if( includingFshType != null )
            SQL += " , fmFacilityType, fmFacility ";

        SQL += " Where fsh_rsv_id = ? "
             + " And fsh_status <> ? ";

        if( includingFshType != null ) {
            SQL += " And fsh_fac_id = fac_id "
                +  " And fac_ftp_id = ftp_id "
                +  " And ftp_class_name = ? ";
        }
        PreparedStatement stmt = this.con.prepareStatement(SQL);
        stmt.setInt(1, getRsv_id());
        stmt.setString(2, notIncludingFshStatus);
        if( includingFshType != null )
            stmt.setString(3, includingFshType);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            setRsv_start_time(rs.getTimestamp("rsv_start_time"));
            setRsv_end_time(rs.getTimestamp("rsv_end_time"));
        } else {
            setRsv_start_time(null);
            setRsv_end_time(null);
        }
        stmt.close();
        //not use getters because it will loop forever if rsv_start_time, rsv_end_time is null
        //rsvStartEndTime[0] = getRsv_start_time(notIncludingFshStatus);
        //rsvStartEndTime[1] = getRsv_end_time(notIncludingFshStatus);
        rsvStartEndTime[0] = this.rsv_start_time;
        rsvStartEndTime[1] = this.rsv_end_time;
        return rsvStartEndTime;
    }
    /* DENNIS END */

    public void updRsvPurpose4Itm(long itm_id, boolean create_run_ind) throws SQLException {
        String SQL = null;

        if (create_run_ind) {
            SQL = SqlStatements.updRsvPurposeForItemWithCourseLevelId(); //SqlStatements.upd_rsv_purpose_for_item_with_course_level_id;
        } else {
            SQL = SqlStatements.updRsvPurposeForItemWithRunLevelId(); //SqlStatements.upd_rsv_purpose_for_item_with_run_level_id;
        }

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.setLong(2, itm_id);
        stmt.executeUpdate();
        stmt.close();
    }
}

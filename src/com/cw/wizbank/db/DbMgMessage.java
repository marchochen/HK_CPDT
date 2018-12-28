package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;

public class DbMgMessage {
    /**
     * CLOB column
     * Table:       mgMessage
     * Column:      msg_addition_note
     * Nullable:    YES
     */
    public final static String COMMA       =   ",";
    public final static String subjectTokenTag =   "$data";

    public long msg_id;
    public String msg_send_usr_id;
    public String msg_subject;
    public String msg_addition_note;
    public Timestamp msg_target_datetime;
    public String msg_create_usr_id;
    public Timestamp msg_create_timestamp;
    public String msg_update_usr_id;
    public Timestamp msg_update_timestamp;
    public boolean msg_bcc_sys_ind;

    //non-datafield
    public Vector msg_subject_token;

    //insert a new message
    public void ins(Connection con) throws SQLException, cwException {
        // << BEGIN for oracle migration!
        String DbMgMessage_INS = "INSERT INTO mgMessage ("
                               + " msg_subject, msg_send_usr_id,   "
                               //+ " msg_addition_note, "
                               + " msg_target_datetime, msg_create_usr_id, "
                               + " msg_create_timestamp, msg_bcc_sys_ind ) "
                               + "VALUES ( ?, ?, ?, ?, ?, ? ) ";

        PreparedStatement stmt = con.prepareStatement(DbMgMessage_INS, PreparedStatement.RETURN_GENERATED_KEYS);
        int count = 1;
        stmt.setString(count++, msg_subject);
        stmt.setString(count++, msg_send_usr_id);
        //stmt.setString(count++, msg_addition_note);
        stmt.setTimestamp(count++, msg_target_datetime);
        stmt.setString(count++, msg_create_usr_id);
        stmt.setTimestamp(count++, msg_create_timestamp);
        stmt.setBoolean(count++, msg_bcc_sys_ind);
        // >> END

        if( stmt.executeUpdate() != 1){
        	stmt.close();
            throw new cwException("Failed to insert a Message.");
        }
        msg_id = cwSQL.getAutoId(con, stmt, "mgMessage", "msg_id");
        stmt.close();
        // update msg_addition_note
        this.updMsgAddNote(con);
        // >> END
        return;
    }


    //upd message
    public void upd(Connection con)
        throws SQLException, cwException {


            try{
                msg_update_timestamp = dbUtils.getTime(con);
            }catch(qdbException e) {
                throw new cwException("Get TimeStamp Error in insert Message : " + e);
            }

            // << BEGIN for oracle migration!
            String DbMgMessage_UPD = " UPDATE mgMessage SET msg_subject = ? , " //+ msg_addition_note = ?, "
                                   + " msg_update_usr_id = ?, msg_update_timestamp = ?, msg_bcc_sys_ind = ? "
                                   + " WHERE msg_id = ? ";

            int count = 1;
            PreparedStatement stmt = con.prepareStatement(DbMgMessage_UPD);
            //stmt.setString(count++, msg_send_usr_id);
            stmt.setString(count++, msg_subject);
            //stmt.setString(count++, msg_addition_note);
            stmt.setString(count++, msg_update_usr_id);
            stmt.setTimestamp(count++, msg_update_timestamp);
            stmt.setBoolean(count++, msg_bcc_sys_ind);
            stmt.setLong(count++, msg_id);
            // >> END

            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update the message, id = " + msg_id);

            stmt.close();

            // << BEGIN for oracle migration!
            // update msg_addition_note
            this.updMsgAddNote(con);
            // >> END
            return;
        }


    // Get all field of the specified message id from the database
    public void get(Connection con)
        throws SQLException, cwException {

            String DbMgMessage_GET = " SELECT msg_send_usr_id, "
                                   + " msg_subject, "
                                   + " msg_addition_note, "
                                   + " msg_target_datetime, "
                                   + " msg_create_usr_id, "
                                   + " msg_create_timestamp, "
                                   + " msg_update_usr_id, "
                                   + " msg_update_timestamp, "
                                   + " msg_bcc_sys_ind "
                                   + " FROM mgMessage "
                                   + " WHERE msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgMessage_GET);
            stmt.setLong(1, msg_id);

            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                msg_send_usr_id     = rs.getString("msg_send_usr_id");
                msg_subject         = rs.getString("msg_subject");
                // << BEGIN for oracle migration!
                //msg_addition_note   = rs.getString("msg_addition_note");
                msg_addition_note   = cwSQL.getClobValue(rs, "msg_addition_note");
                // >> END
                msg_target_datetime = rs.getTimestamp("msg_target_datetime");
                msg_create_usr_id   = rs.getString("msg_create_usr_id");
                msg_create_timestamp= rs.getTimestamp("msg_create_timestamp");
                msg_update_usr_id   = rs.getString("msg_update_usr_id");
                msg_update_timestamp= rs.getTimestamp("msg_update_timestamp");
                msg_bcc_sys_ind     = rs.getBoolean("msg_bcc_sys_ind");
            }
            else
                throw new cwException("Failed to get the message detial, id = " + msg_id);

            stmt.close();
        }


    //Get the Max. message id
    public static long getMaxId(Connection con)
        throws SQLException, cwException {

            String DbMgMessage_GET_MAX_ID = " SELECT MAX(msg_id) FROM mgMessage ";
            PreparedStatement stmt = con.prepareStatement(DbMgMessage_GET_MAX_ID);
            ResultSet rs = stmt.executeQuery();
            long id;
            if( rs.next() ) {
                id = rs.getLong(1);
            }
            else {
            	if(rs!=null)rs.close();
            	stmt.close();
                throw new cwException("Failed to get the message id.");
            }
            rs.close();
            stmt.close();
            return id;
        }

/*
    // Get Sender usr_id
    public static String getUsrId(Connection con, long msg_id)
        throws SQLException, cwException {

            String DbMgMessage_GET_ENT_ID = " Select msg_send_usr_id FROM mgMessage "
                                          + " WHERE msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgMessage_GET_ENT_ID);
            stmt.setLong(1, msg_id);

            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                return rs.getString("msg_send_usr_id");
            else
                throw new cwException("Failed to get sender entity id of the message. ");

        }
*/
    //update target timestamp of the message
    public void updTimestamp(Connection con, String usr_id)
        throws SQLException, cwException {

            Timestamp curTime = null;
            try{
                curTime = dbUtils.getTime(con);
            }catch( qdbException e) {
                throw new cwException("Failed to get timestamp : " + e);
            }

            String DbMgMessage_UPD_SEND_TIMESTAMP = " UPDATE mgMessage "
                                                  + " SET msg_target_datetime = ? ,"
                                                  + " msg_update_usr_id = ? , "
                                                  + " msg_update_timestamp = ? "
                                                  + " WHERE msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgMessage_UPD_SEND_TIMESTAMP);
            stmt.setTimestamp(1, msg_target_datetime);
            stmt.setString(2, usr_id);
            stmt.setTimestamp(3, curTime);
            stmt.setLong(4, msg_id);

            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update timestamp of the message : " + msg_id  );

            stmt.close();
            return;
        }
        
      //update subject of the message
    public void updSubject(Connection con, String usr_id)
        throws SQLException, cwException {

            Timestamp curTime = null;
            try{
                curTime = dbUtils.getTime(con);
            }catch( qdbException e) {
                throw new cwException("Failed to get timestamp : " + e);
            }

            String DbMgMessage_UPD_SUBJECT = " UPDATE mgMessage "
                                                  + " SET msg_subject = ? ,"
                                                  + " msg_update_usr_id = ? , "
                                                  + " msg_update_timestamp = ? "
                                                  + " WHERE msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgMessage_UPD_SUBJECT);
            stmt.setString(1, msg_subject);
            stmt.setString(2, usr_id);
            stmt.setTimestamp(3, curTime);
            stmt.setLong(4, msg_id);

            if( stmt.executeUpdate() != 1 )
                throw new cwException("Failed to update subject of the message : " + msg_id  );

            stmt.close();
            return;
        }
/*
    // update timestamp of messages with specificed time
    public static void updTimestamps(Connection con, long[] msg_ids, Timestamp send_time, loginProfile prof)
        throws SQLException, cwException {

            String condition;
            if( send_time == null )
                condition = " AND msg_update_timestamp is not null ";
            else
                condition = " AND msg_update_timestamp != ? ";

            Timestamp curTime = null;
            try{
                curTime = dbUtils.getTime(con);
            }catch( qdbException e) {
                throw new cwException("Fialed to get timestamp : " + e);
            }

            StringBuffer msg_id_str = new StringBuffer().append("(0");
            for(int i=0; i<msg_ids.length; i++)
                msg_id_str.append(COMMA).append(msg_ids[i]);
            msg_id_str.append(")");

            String DbMgMessage_UPD_SEND_TIMESTAMPS = " UPDATE mgMessage "
                                                   + " SET msg_target_datetime = ? ,"
                                                   + " msg_update_usr_id = ? , "
                                                   + " msg_update_timestamp = ? , "
                                                   + " WHERE msg_id IN "
                                                   +   msg_id_str.toString()
                                                   +   condition ;

            PreparedStatement stmt = con.prepareStatement(DbMgMessage_UPD_SEND_TIMESTAMPS);
            stmt.setTimestamp(1, send_time);
            stmt.setString(2, prof.usr_id);
            stmt.setTimestamp(3, curTime);
            if(send_time != null)
                stmt.setTimestamp(4, send_time);

            if( stmt.executeUpdate() != msg_ids.length )
                throw new cwException("Failed to update timestamp of messages . ");

            return;
        }
*/


    public static void delRecords(Connection con, long msg_id)
        throws SQLException, cwException {
            
            String DbMgMessage_DEL_RECORDS = " DELETE From mgMessage "
                                           + " WHERE msg_id = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgMessage_DEL_RECORDS);
            stmt.setLong(1, msg_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }

    public static void delRecords(Connection con, long[] msg_id)
        throws SQLException, cwException {
            
            String DbMgMessage_DEL_RECORDS = " DELETE From mgMessage "
                                           + " WHERE msg_id IN " + cwUtils.array2list(msg_id);

            PreparedStatement stmt = con.prepareStatement(DbMgMessage_DEL_RECORDS);
            stmt.executeUpdate();
            stmt.close();
            return;
        }
        
    // << BEGIN for oracle migration!
    private void updMsgAddNote(Connection con) throws SQLException {
        // Update msg_addition_note
        // construct the condition
        String condition = "msg_id = " + msg_id;
        // construct the column & value
        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = "msg_addition_note";
        columnValue[0] = msg_addition_note;
        cwSQL.updateClobFields(con, "mgMessage", columnName, columnValue, condition);
    }
    // >> END
}

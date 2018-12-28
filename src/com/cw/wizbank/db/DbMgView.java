package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.db.DbXslTemplate;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;

public class DbMgView {
    
    public final static String COMMA    =   ",";
    public final static String JI       =   "JI";    
    
    public static class ViewMgStatus {
        public long rec_ent_id;
        public Timestamp msg_target_datetime;
    }
    
    public static long[] getSendMessageId(Connection con)
        throws SQLException, cwException {

            Vector idsVec = new Vector();
            Timestamp curTime;
            try{
                curTime = dbUtils.getTime(con);
            }catch( qdbException e) {
                throw new cwException("Failed to get the timestamp from database.");
            }
                        
            String DbMgView_GET_SEND_MESSAGE_ID = " SELECT DISTINCT msg_id "
                                                + " FROM mgMessage, mgRecHistory "
                                                + " WHERE msg_id = mgh_mst_msg_id "
                                                + " AND   mgh_status = 'N' "
                                                + " AND   msg_target_datetime < ? ";
            
            PreparedStatement stmt = con.prepareStatement(DbMgView_GET_SEND_MESSAGE_ID);
            stmt.setTimestamp(1, curTime);
            ResultSet rs = stmt.executeQuery();
            while( rs.next() )
                idsVec.addElement(new Long(rs.getLong("msg_id")));
        
            long[] ids = new long[idsVec.size()];
            for(int i=0; i<idsVec.size(); i++)
                ids[i] = ((Long)idsVec.elementAt(i)).longValue();
            
            stmt.close();
            return ids;
        }
        

    public static Vector getRecipientStatus(Connection con, long msg_id, String rec_type, String mgh_status, long mgh_mst_xtp_id, int attempt)
        throws SQLException, cwException {

            return getRecipientStatus(con, msg_id, rec_type, mgh_status, mgh_mst_xtp_id, null, attempt);

        }

    // get recipient id by specific message id, recipient type and recipient status
    public static Vector getRecipientStatus(Connection con, long msg_id, String rec_type, String mgh_status, long mgh_mst_xtp_id, String idGroup, int attempt)
        throws SQLException, cwException {
            
            Vector vec = new Vector();
            Vector recIdVec = new Vector();
            Vector entIdVec = new Vector();
            Vector statusVec = new Vector();
            
            Vector recTypeVec = new Vector();
            
            String DbMgView_GET_RECIPIENT_STATUS = " SELECT DISTINCT rec_id, rec_ent_id,  mgh_status, rec_type "                                                 
                                                 + " FROM mgRecipient, mgRecHistory "
                                                 + " WHERE rec_id = mgh_rec_id AND mgh_mst_msg_id = ? ";
            
            if(rec_type != null)
                DbMgView_GET_RECIPIENT_STATUS += " AND rec_type = ? ";
            
            if(mgh_status != null)
                DbMgView_GET_RECIPIENT_STATUS += " AND mgh_status = ? ";
                
            if(mgh_mst_xtp_id != 0)
                DbMgView_GET_RECIPIENT_STATUS += " AND mgh_mst_xtp_id = ? ";
                
            if(idGroup != null)
                DbMgView_GET_RECIPIENT_STATUS += " AND rec_ent_id IN " + idGroup;
            
            if( attempt > 0 )
                DbMgView_GET_RECIPIENT_STATUS += " AND ( mgh_attempted < ? OR mgh_attempted is null ) ";

            PreparedStatement stmt = con.prepareStatement(DbMgView_GET_RECIPIENT_STATUS);
            int index = 1;
            stmt.setLong(index++, msg_id);
            if(rec_type != null)
                stmt.setString(index++, rec_type);
            if(mgh_status != null)
                stmt.setString(index++, mgh_status);        
            if(mgh_mst_xtp_id != 0)
                stmt.setLong(index++, mgh_mst_xtp_id);
            if( attempt > 0 )
                stmt.setLong(index++, attempt);
        
            ResultSet rs = stmt.executeQuery();
            while( rs.next() ) {
                recIdVec.addElement(new Long(rs.getLong("rec_id")));
                entIdVec.addElement(rs.getString("rec_ent_id"));
                statusVec.addElement(rs.getString("mgh_status"));
                recTypeVec.addElement(rs.getString("rec_type"));
            }
            
            vec.addElement(recIdVec);
            vec.addElement(entIdVec);
            vec.addElement(statusVec);
            vec.addElement(recTypeVec);
            
            stmt.close();
            return vec;
            
        }
        
        
        
    public static Hashtable getRecipientNotificationDate(Connection con, long msg_ids[], String rec_type, String mgh_status, long mgh_mst_xtp_id)
        throws SQLException, cwException {
            
            Hashtable recipHashtable = new Hashtable();
            
            StringBuffer msgIdStr = new StringBuffer().append("(0");
            for(int i=0; i<msg_ids.length; i++)
                msgIdStr.append(COMMA).append(msg_ids[i]);
            msgIdStr.append(")");    
            
            
            String DbMgView_GET_RECIPIENT_NOTIFICATION_DATE = " SELECT rec_ent_id, MAX(mgh_sent_datetime) "
                                                            + " FROM mgRecipient, mgRecHistory "
                                                            + " WHERE rec_id = mgh_rec_id "
                                                            + " AND rec_msg_id IN "
                                                            + msgIdStr.toString();
            if( rec_type != null )
                DbMgView_GET_RECIPIENT_NOTIFICATION_DATE += " AND rec_type = ? ";
                
            if( mgh_status != null )
                DbMgView_GET_RECIPIENT_NOTIFICATION_DATE += " AND mgh_status = ? ";
                
            if( mgh_mst_xtp_id != 0 )
                DbMgView_GET_RECIPIENT_NOTIFICATION_DATE += " AND mgh_mst_xtp_id = ? ";
                
            
            DbMgView_GET_RECIPIENT_NOTIFICATION_DATE += " GROUP BY rec_ent_id ";

        
            PreparedStatement stmt = con.prepareStatement(DbMgView_GET_RECIPIENT_NOTIFICATION_DATE);
            int index = 1;
            if( rec_type != null )
                stmt.setString(index++, rec_type);
            
            if( mgh_status != null )
                stmt.setString(index++, mgh_status);
            
            if( mgh_mst_xtp_id != 0 )
                stmt.setLong(index++, mgh_mst_xtp_id);
        
            ResultSet rs = stmt.executeQuery();
            Timestamp latestDate;
            while( rs.next() ) {
                latestDate = rs.getTimestamp(2);
                if( latestDate == null )
                    continue;
                recipHashtable.put(new Long(rs.getLong(1)), latestDate);
            }
            

            stmt.close();
            return recipHashtable;
        
        }
 
 
/*
     public static long getLatestJI(Connection con, long itm_id)
        throws SQLException, cwException {            
            return getLatestItmMessage(con, itm_id, "JI");
        }
 
     public static long getLatestReminder(Connection con, long itm_id)
        throws SQLException, cwException {            
            return getLatestItmMessage(con, itm_id, "REMINDER");
        }
 */
 
    // return message id of the specific item id
    // if no ji belong to this item, return 0
    public static long getLatestItmMessage(Connection con, long itm_id, String type)
        throws SQLException, cwException {
            
            String DbMgView_GET_LATEST_ITEM_MSG = " SELECT msg_id "
                                                + " FROM mgitmSelectedMessage, mgMessage "
                                                + " WHERE ism_itm_id = ? "
                                                + " AND ism_msg_id = msg_id "
                                                + " AND ism_type = ? "
                                                + " AND msg_create_timestamp "
                                                + " = ( SELECT MAX(msg_create_timestamp) "
                                                + "     FROM mgMessage, mgitmSelectedMessage "
                                                + "     WHERE ism_itm_id = ? "
                                                + "     AND msg_id = ism_msg_id "
                                                + "     AND ism_type = ? ) ";

            PreparedStatement stmt = con.prepareStatement(DbMgView_GET_LATEST_ITEM_MSG);
            stmt.setLong(1, itm_id);
            stmt.setString(2, type);
            stmt.setLong(3, itm_id);
            stmt.setString(4, type);
            
            ResultSet rs = stmt.executeQuery();
            long id;
            if( rs.next() )
                id = rs.getLong("msg_id");
            else                
                id =  0;
            
            stmt.close();
            return id;
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /**
         * @deprecated (2003-07-30 kawai)
         * please move the stmt.close() outside of the if-then block if this method is to be reused
         */
        /*
        //return last notifiction date of TNA
        // if no nitify any user , return null
        public static Timestamp getLatestDate(Connection con, long tna_id)
            throws SQLException, cwException {
                                
                String DbMgView_GET_LATEST_NOTIFICATION_DATE = " select msg_create_timestamp "
                                                             + " FROM mgtnaselectedMessage, mgMessage "
                                                             + " WHERE tsm_tna_id = ? "
                                                             + " AND   tsm_msg_id = msg_id "
                                                             + " AND msg_create_timestamp "
                                                             + " = ( SELECT MAX(msg_create_timestamp) "
                                                             + "     FROM mgMessage, mgtnaSelectedMessage "
                                                             + "     WHERE tsm_tna_id = ? "
                                                             + "     AND   tsm_msg_id = msg_id ) ";

                PreparedStatement stmt = con.prepareStatement(DbMgView_GET_LATEST_NOTIFICATION_DATE);
                stmt.setLong(1, tna_id);
                stmt.setLong(2, tna_id);
                
                ResultSet rs = stmt.executeQuery();
                Timestamp time;
                if( rs.next() ) {
                    time = rs.getTimestamp("msg_create_timestamp");
                    stmt.close();
                    return time;
                }
                else
                    return null;                
            }
        */
            
        
        
        /**
         * @deprecated (2003-07-30 kawai)
         * please modify to not returning a ResultSet if this method is to be reused
         */
        /**
        Get the last notification date of the item
        @param tnaIdVec vetor with tna id
        @return return rseult set of tna id and last notification date
        */
        /*
        public static ResultSet getLatestDate(Connection con, Vector tnaIdVec)
            throws SQLException, cwException {
                
                String SQL = " SELECT tsm_tna_id, MAX(msg_create_timestamp) AS latest_date "
                           + " FROM mgMessage, mgTnaSelectedMessage "
                           + " WHERE msg_id = tsm_msg_id "
                           + " AND tsm_tna_id IN "
                           + cwUtils.vector2list(tnaIdVec)
                           + " GROUP BY tsm_tna_id ";
                
                PreparedStatement stmt = con.prepareStatement(SQL);
                ResultSet rs = stmt.executeQuery();
                return rs;
            }
        */


    //get the send method of the message
    public static String[] getSendMethod(Connection con, long msg_id)
        throws SQLException, cwException {
            
            Vector methodVec = new Vector();
            
            String DbMgView_GET_MESSAGE_SEND_METHOD = " SELECT xtp_subtype "
                                                    + " FROM mgMessage, xslmgSelectedTemplate, xslTemplate "
                                                    + " WHERE msg_id = mst_msg_id "
                                                    + " AND   mst_xtp_id = xtp_id "
                                                    + " AND   msg_id = ? ";
                                                    
            PreparedStatement stmt = con.prepareStatement(DbMgView_GET_MESSAGE_SEND_METHOD);
            stmt.setLong(1, msg_id);
            
            ResultSet rs = stmt.executeQuery();
            while( rs.next() )
                methodVec.addElement(rs.getString("xtp_subtype"));    
            
            String[] methods = new String[methodVec.size()];
            for(int i=0; i<methods.length; i++)
                methods[i] = (String)methodVec.elementAt(i);
            
            stmt.close();
            return methods;

        }
        
        

    public static Vector getItemMessageRecipient(Connection con, long itm_id, String type, String recip_type)
        throws SQLException, cwException {
            String DbMgView_GET_ITEM_MESSAGE_RECIPIENT = " SELECT DISTINCT rec_ent_id "
                                                       + " FROM mgitmSelectedMessage, mgMessage, mgRecipient "
                                                       + " WHERE ism_msg_id = msg_id "
                                                       + " AND   msg_id     = rec_msg_id "
                                                       + " AND   ism_itm_id = ? "
                                                       + " AND   ism_type   = ? ";
            if( recip_type != null)
                DbMgView_GET_ITEM_MESSAGE_RECIPIENT += "AND rec_type = ? ";
            
            PreparedStatement stmt = null;
            ResultSet rs = null;
            Vector tempResult = null;
            try {
                stmt = con.prepareStatement(DbMgView_GET_ITEM_MESSAGE_RECIPIENT);
                stmt.setLong(1, itm_id);
                stmt.setString(2, type);
                if( recip_type != null)
                    stmt.setString(3, recip_type);
                rs = stmt.executeQuery();
                
                tempResult = new Vector();
                while (rs.next()) {
                    tempResult.addElement(new Long(rs.getLong("rec_ent_id")));
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
            
            return tempResult;
        }
            

    public static long getXtpId(Connection con, long msg_id, String type)
        throws SQLException, cwException {
            
            String DbMgView_GET_XTP_ID = " SELECT xtp_id FROM xslmgSelectedTemplate, xslTemplate "
                                       + " WHERE mst_msg_id = ? "
                                       + " AND   mst_xtp_id = xtp_id "
                                       + " AND   xtp_subtype = ? ";
                                       
            PreparedStatement stmt = con.prepareStatement(DbMgView_GET_XTP_ID);
            stmt.setLong(1, msg_id);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            long id;
            if( rs.next() )
                id = rs.getLong("xtp_id");
            else
                throw new cwException("Failed to get the template id , message id = " + msg_id );
            
            stmt.close();
            return id;
        }



    /**
    Get the message id and the target datetime belong to the item
    */
    public static Hashtable getItemMessage(Connection con, long itm_id, String type)
        throws SQLException {
            
            String DbMgView_GET_ITEM_MESSAGE = " SELECT msg_id, msg_target_datetime "
                                             + " FROM mgMessage, mgitmSelectedMessage "
                                             + " WHERE ism_msg_id = msg_id AND ism_itm_id = ? AND ism_type = ? ";

            PreparedStatement stmt = con.prepareStatement(DbMgView_GET_ITEM_MESSAGE);
            stmt.setLong(1, itm_id);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            Hashtable msgDatetime = new Hashtable();
            while( rs.next() ) {

                if( rs.getTimestamp("msg_target_datetime") != null )
                    msgDatetime.put(new Long(rs.getLong("msg_id")), rs.getTimestamp("msg_target_datetime"));
            }
            stmt.close();
            
            return msgDatetime;
            
        }



    /**
    Get the original item message
    */
    public static DbMgMessage getOriginalItmMessage(Connection con, long itm_id, String type)
        throws SQLException {
        
        String DbMgView_GET_ORIGINAL_ITM_MSG = " SELECT msg_id, msg_send_usr_id, msg_subject, msg_addition_note, "
                                            + " msg_target_datetime, msg_create_usr_id, msg_create_timestamp, "
                                            + " msg_update_usr_id, msg_update_timestamp, msg_bcc_sys_ind "
                                            + " FROM mgitmSelectedMessage, mgMessage "
                                            + " WHERE ism_itm_id = ? "
                                            + " AND ism_msg_id = msg_id "
                                            + " AND ism_type = ? "
                                            + " AND msg_create_timestamp "
                                            + " = ( SELECT MIN(msg_create_timestamp) "
                                            + "     FROM mgMessage, mgitmSelectedMessage "
                                            + "     WHERE ism_itm_id = ? "
                                            + "     AND msg_id = ism_msg_id "
                                            + "     AND ism_type = ? ) ";
        int idx = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        DbMgMessage msgObj = null;
        try {
            idx = 1;
            stmt = con.prepareStatement(DbMgView_GET_ORIGINAL_ITM_MSG);
            stmt.setLong(idx++, itm_id);
            stmt.setString(idx++, type);
            stmt.setLong(idx++, itm_id);
            stmt.setString(idx++, type);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                msgObj = new DbMgMessage();
                msgObj.msg_id = rs.getLong("msg_id");
                msgObj.msg_send_usr_id = rs.getString("msg_send_usr_id");
                msgObj.msg_subject = rs.getString("msg_subject");
                msgObj.msg_addition_note = cwSQL.getClobValue(rs, "msg_addition_note");
                msgObj.msg_target_datetime = rs.getTimestamp("msg_target_datetime");
                msgObj.msg_create_usr_id = rs.getString("msg_create_usr_id");
                msgObj.msg_create_timestamp = rs.getTimestamp("msg_create_timestamp");
                msgObj.msg_update_usr_id = rs.getString("msg_update_usr_id");
                msgObj.msg_update_timestamp = rs.getTimestamp("msg_update_timestamp");
                msgObj.msg_bcc_sys_ind = rs.getBoolean("msg_bcc_sys_ind");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return msgObj;
    }



    public static Vector getMessageParams(Connection con, long msg_id, long xtp_id)
        throws SQLException {
            
            String DbMgViev_GET_MSG_PARAM_NAME_VALUE = " SELECT xpv_type, xpv_value, xpn_name "
                                                + " FROM mgxslParamValue, xslParamName "
                                                + " WHERE xpv_mst_msg_id = ? "
                                                + " AND   xpv_mst_xtp_id = ? "
                                                + " AND   xpn_id = xpv_xpn_id ";

            PreparedStatement stmt = con.prepareStatement(DbMgViev_GET_MSG_PARAM_NAME_VALUE);
            stmt.setLong(1, msg_id);
            stmt.setLong(2, xtp_id);
            ResultSet rs = stmt.executeQuery();
            Vector params = new Vector();
            Vector paramsName = new Vector();
            Vector paramsType = new Vector();
            Vector paramsValue = new Vector();
            while( rs.next() ) {
                paramsName.addElement(rs.getString("xpn_name"));
                paramsType.addElement(rs.getString("xpv_type"));
                paramsValue.addElement(rs.getString("xpv_value"));
            }
            stmt.close();
            params.addElement(paramsName);
            params.addElement(paramsType);
            params.addElement(paramsValue);
            
            return params;
        }



    /**
    Get the specified user status of the reminder
    */
    public ViewMgStatus[] getUserReminderStatus(Connection con, long itm_id, String[] entIds)
        throws SQLException {
        
        StringBuffer idList = new StringBuffer().append("(0");
        for(int i=0; i<entIds.length; i++)
            idList.append(",").append(entIds[i]);
        idList.append(")");
        
        String DbMgViev_GET_REMINDER_STATUS = 
            " SELECT msg_target_datetime, rec_ent_id "
          + " FROM mgMessage, mgitmSelectedMessage, mgRecipient "
          + " WHERE ism_itm_id = ? "
          + " AND ism_type = ? "
          + " AND ism_msg_id = msg_id "
          + " AND rec_msg_id = msg_id "
          + " AND rec_type = ? "
          + " AND rec_ent_id IN " + idList.toString();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(DbMgViev_GET_REMINDER_STATUS);
            stmt.setLong(1, itm_id);
            stmt.setString(2, "REMINDER");
            stmt.setString(3, "RECIPIENT");
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewMgStatus mgSts = new ViewMgStatus();
                mgSts.rec_ent_id = rs.getLong("rec_ent_id");
                mgSts.msg_target_datetime = rs.getTimestamp("msg_target_datetime");
                tempResult.addElement(mgSts);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewMgStatus result[] = new ViewMgStatus[tempResult.size()];
        result = (ViewMgStatus[])tempResult.toArray(result);
        
        return result;
    }


    /**
    Get the specified user status of the reminder
    */
    public ViewMgStatus[] getUserJIStatus(Connection con, long itm_id, String[] entIds)
        throws SQLException {
        
        StringBuffer idList = new StringBuffer().append("(0");
        for(int i=0; i<entIds.length; i++)
            idList.append(",").append(entIds[i]);
        idList.append(")");
        
        String DbMgViev_GET_JI_STATUS = " SELECT rec_ent_id, MAX(msg_target_datetime) AS target_datetime "
                                      + " FROM mgMessage, mgRecipient, mgitmSelectedMessage "
                                      + " WHERE ism_msg_id = rec_msg_id "
                                      + " AND msg_id = ism_msg_id "
                                      + " AND ism_itm_id = ? "
                                      + " AND ism_type = ? "
                                      + " AND rec_type = ? "
                                      + " AND rec_ent_id IN " + idList.toString()
                                      + " GROUP BY rec_ent_id ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(DbMgViev_GET_JI_STATUS);
            stmt.setLong(1, itm_id);
            stmt.setString(2, "JI");
            stmt.setString(3, "RECIPIENT");
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewMgStatus mgSts = new ViewMgStatus();
                mgSts.rec_ent_id = rs.getLong("rec_ent_id");
                mgSts.msg_target_datetime = rs.getTimestamp("target_datetime");
                tempResult.addElement(mgSts);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewMgStatus result[] = new ViewMgStatus[tempResult.size()];
        result = (ViewMgStatus[])tempResult.toArray(result);
        
        return result;
    }


    /**
    Reset Reminder Recipient Status ( resend message to recipient )
    @param itm_id, reminder linked to the item
    */
    public static void resetRecipientStatus(Connection con, long itm_id)
        throws SQLException {
    
            String SQL = " UPDATE mgRecHistory SET mgh_status = ? , mgh_sent_datetime = ?, mgh_attempted = ? "
                       + " FROM mgRecHistory, mgItmSelectedMessage "
                       + " where ism_itm_id = ? AND ism_type = ? "
                       + " AND mgh_mst_msg_id = ism_msg_id ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, "N");
            stmt.setTimestamp(2, null);
            stmt.setLong(3, 0);
            stmt.setLong(4, itm_id);
            stmt.setString(5, "REMINDER");
            stmt.executeUpdate();
            stmt.close();
            return;

        }
  public static boolean checkItmEntExists(Connection con, long itm_id, long rec_ent_id, String ism_type, String rec_type)
        throws SQLException
    {
        StringBuffer SQL = new StringBuffer();
        SQL.append("SELECT COUNT(*) AS TOTAL_COUNT ");
        SQL.append("FROM mgitmSelectedMessage, mgRecipient ");
        SQL.append("WHERE rec_msg_id = ism_msg_id AND ism_itm_id = ? AND rec_ent_id = ? AND ism_type = ? AND rec_type = ? ");

        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(1, itm_id);
        stmt.setLong(2, rec_ent_id);
        stmt.setString(3, ism_type);
        stmt.setString(4, rec_type);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            if (rs.getLong("TOTAL_COUNT") > 0){
                stmt.close();
                return true;    
            }else{
                stmt.close();
                return false;    
            }
        }else{
            stmt.close();
            throw new SQLException("cannot get the message history on the user");    
        }
    }
    
    
    public DbXslTemplate[] getTemplate(Connection con, long msg_id, String type)
        throws SQLException{
            
        String SQL = " SELECT xtp_id, xtp_xsl, xtp_xml_url, xtp_title "
                   + " FROM xslTemplate , xslMgSelectedTemplate "
                   + " WHERE xtp_id = mst_xtp_id "
                   + " AND mst_type = ? "
                   + " AND mst_msg_id = ? ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setString(1, type);
            stmt.setLong(2, msg_id);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while(rs.next()){
                DbXslTemplate xslTpl = new DbXslTemplate();
                xslTpl.xtp_id = rs.getLong("xtp_id");
                xslTpl.xtp_xsl = rs.getString("xtp_xsl");
                xslTpl.xtp_xml_url = rs.getString("xtp_xml_url");
                xslTpl.xtp_title = rs.getString("xtp_title");
                tempResult.addElement(xslTpl);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        DbXslTemplate result[] = new DbXslTemplate[tempResult.size()];
        result = (DbXslTemplate[])tempResult.toArray(result);
        
        return result;
    }
 
    
    public void updateSender(Connection con, long msg_id, String usr_id)
        throws SQLException {
            
            String SQL = " Update mgXslParamValue "
                       + " Set xpv_value = ? "
                       + " where xpv_mst_msg_id = ? "
                       + " and xpv_xpn_id IN ( "
                       + " Select xpn_id "
                       + " from xslParamName, xslMgSelectedTemplate "
                       + " where xpn_name = ? and mst_xtp_id = xpn_xtp_id " 
                       + " and mst_msg_id = ? ) ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, usr_id);
            stmt.setLong(2, msg_id);
            stmt.setString(3, "sender_id");
            stmt.setLong(4, msg_id);
            stmt.executeUpdate();
            stmt.close();
            return;
        }
 
    
    public static Vector getItemRecipient(Connection con, long itm_id, long usr_ent_id)
        throws SQLException{
            
            String SQL = " Select rec_id "
                       + " From mgItmSelectedMessage , mgRecipient "
                       + " Where ism_itm_id = ? "
                       + " AND rec_msg_id = ism_msg_id "
                       + " AND rec_ent_id = ? " ;
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            stmt.setLong(2, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            Vector recIdVec = new Vector();
            while(rs.next())
                recIdVec.addElement(new Long(rs.getLong("rec_id")));
            stmt.close();
            return recIdVec;
    }
    
    /**
     * get recipient id by specific message id, recipient type and recipient status
     * @return vector contains string arrays of user's ent_id and display name. 
     */
    public static Vector getReplyEntIdAndName(Connection con, long msg_id, String rec_type)
        throws SQLException, cwException {
            Vector entIdVec = new Vector();

            String DbMgView_GET_REPLY_TO_IDS = " SELECT distinct rec_ent_id,usr_display_bil,usr_email "                                                 
                                                 + " FROM mgRecipient,RegUser "
                                                 + " WHERE rec_ent_id=usr_ent_id and rec_msg_id = ? "
                                                 + " AND rec_type = ? ";

            PreparedStatement pstmt = con.prepareStatement(DbMgView_GET_REPLY_TO_IDS);
            int index = 1;
            pstmt.setLong(index++, msg_id);
            pstmt.setString(index++, rec_type);

            ResultSet rs = pstmt.executeQuery();
            while( rs.next() ) {
                entIdVec.addElement(new String[]{ rs.getString("rec_ent_id"), rs.getString("usr_email"), rs.getString("usr_display_bil")});
            }
            cwSQL.closeResultSet(rs);
            cwSQL.closePreparedStatement(pstmt);
            return entIdVec;
            
        }
}
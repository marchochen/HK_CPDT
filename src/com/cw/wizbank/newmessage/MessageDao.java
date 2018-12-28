package com.cw.wizbank.newmessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.newmessage.entity.EmailMessage;
import com.cw.wizbank.newmessage.entity.EmailMsgRecHistory;
import com.cw.wizbank.newmessage.entity.WebMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class MessageDao {
	public static String MESSAGE_CONTENT_TYPE_EMAIL = "EMAIL";
	public static String MESSAGE_CONTENT_TYPE_PC = "PC";
	public static String MESSAGE_CONTENT_TYPE_MOBILE = "MOBILE";
	
    public void insEmailMessage(Connection con, EmailMessage emsg) throws SQLException {
        String sql = "INSERT INTO emailMessage ("
                      + " emsg_mtp_id, emsg_send_ent_id, emsg_rec_ent_ids, emsg_cc_ent_ids, emsg_subject, emsg_content, emsg_attachment, " +
                            "emsg_target_datetime, emsg_create_ent_id, emsg_create_timestamp,emsg_itm_id, emsg_cc_email)"
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setLong(index++, emsg.getEmsg_mtp_id());
            stmt.setLong(index++, emsg.getEmsg_send_ent_id());
            stmt.setString(index++, emsg.getEmsg_rec_ent_ids());
            stmt.setString(index++, emsg.getEmsg_cc_ent_ids());
            stmt.setString(index++, emsg.getEmsg_subject());
            stmt.setString(index++, emsg.getEmsg_content());
            stmt.setString(index++, emsg.getEmsg_attachment());
            stmt.setTimestamp(index++, emsg.getEmsg_target_datetime());
            stmt.setLong(index++, emsg.getEmsg_create_ent_id());
            stmt.setTimestamp(index++, emsg.getEmsg_create_timestamp());
            stmt.setLong(index++, emsg.getEmsg_itm_id());
            stmt.setString(index++, emsg.getEmsg_cc_email());

            stmt.executeUpdate();
            emsg.setEmsg_id(cwSQL.getAutoId(con, stmt, "emailMessage", "emsg_id"));
        } finally {
            cwSQL.closePreparedStatement(stmt);
        }
    }
    
    
    public void getEmailMessage(Connection con, EmailMessage emsg) throws SQLException {
        String sql = "Select emsg_mtp_id, emsg_send_ent_id, emsg_rec_ent_ids, emsg_cc_ent_ids, emsg_subject, emsg_content, emsg_attachment, " +
        		"emsg_target_datetime ,emsg_itm_id , emsg_cc_email, mtp_type From emailMessage left join messageTemplate  on mtp_id = emsg_mtp_id where emsg_id = ? ";

        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, emsg.getEmsg_id());
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            emsg.setEmsg_mtp_id(rs.getLong("emsg_mtp_id"));
            emsg.setEmsg_send_ent_id(rs.getLong("emsg_send_ent_id"));
            emsg.setEmsg_rec_ent_ids(rs.getString("emsg_rec_ent_ids"));
            emsg.setEmsg_cc_ent_ids(rs.getString("emsg_cc_ent_ids"));
            emsg.setEmsg_subject(rs.getString("emsg_subject"));
            emsg.setEmsg_content(rs.getString("emsg_content"));
            emsg.setEmsg_attachment(rs.getString("emsg_attachment"));
            emsg.setEmsg_target_datetime(rs.getTimestamp("emsg_target_datetime"));
            emsg.setEmsg_itm_id(rs.getLong("emsg_itm_id"));
            emsg.setEmsg_cc_email(rs.getString("emsg_cc_email"));
            emsg.setMtp_type(rs.getString("mtp_type"));
        }
        cwSQL.cleanUp(rs, stmt);
    }
    
    public void insWebMessage(Connection con, WebMessage wmsg, long rec_ent_id) throws SQLException {
        String sql = "INSERT INTO webMessage ("
                  + " wmsg_mtp_id, wmsg_send_ent_id, wmsg_type, wmsg_subject,"
                  + " wmsg_content_pc,wmsg_admin_content_pc, wmsg_content_mobile, wmsg_attachment, wmsg_target_datetime, wmsg_create_ent_id, wmsg_create_timestamp, wmsg_rec_ent_id)"
                  + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        PreparedStatement stmt = null;
        try {
	        stmt = con.prepareStatement(sql);
//			for(int i=0; i<rec_ent_id.length; i++){
		        int index = 1;
		        stmt.setLong(index++, wmsg.getWmsg_mtp_id());
		        stmt.setLong(index++, wmsg.getWmsg_send_ent_id());
		        stmt.setString(index++, wmsg.getWmsg_type());
		        stmt.setString(index++, wmsg.getWmsg_subject());
		        stmt.setString(index++, wmsg.getWmsg_content_pc());
		        stmt.setString(index++, wmsg.getWmsg_admin_content_pc());
		        stmt.setString(index++, wmsg.getWmsg_content_mobile());
		        stmt.setString(index++, wmsg.getWmsg_attachment());
		        stmt.setTimestamp(index++, wmsg.getWmsg_target_datetime());
		        stmt.setLong(index++, wmsg.getWmsg_create_ent_id());
		        stmt.setTimestamp(index++, wmsg.getWmsg_create_timestamp());
				stmt.setLong(index++, rec_ent_id);
//				stmt.addBatch();
//			}
	        stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
    }

    public void insEmsgHis(Connection con, EmailMsgRecHistory history) throws SQLException {
        String sql = "INSERT INTO emailMsgRecHistory ("
                      + " emrh_emsg_id, emrh_status, emrh_sent_datetime, emrh_attempted)"
                      + " VALUES ( ?, ?, ?, ?) ";

        PreparedStatement stmt = null;
        try {
	        stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
	        int index = 1;
	        stmt.setLong(index++, history.getEmrh_emsg_id());
	        stmt.setString(index++, history.getEmrh_status());
	        stmt.setTimestamp(index++, history.getEmrh_sent_datetime());
	        stmt.setLong(index++, 0);

	        stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
    }
    
    public static long getEmailMessagMaxId(Connection con)
    	throws SQLException, cwException {

        String sql = " SELECT MAX(emsg_id) FROM emailMessage ";
        PreparedStatement stmt = con.prepareStatement(sql);
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
        cwSQL.cleanUp(rs, stmt);
        return id;
    }
    
    public static long getWebMessagMaxId(Connection con)
    	throws SQLException, cwException {

        String sql = " SELECT MAX(wmsg_id) FROM webMessage ";
        PreparedStatement stmt = con.prepareStatement(sql);
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
        cwSQL.cleanUp(rs, stmt);
        return id;
    }
    
    /**批量修改Message发送时间
     * @param con
     * @param target_datetime: 发送时间
     * @param msg_id_lst: message id list
     * @param msg_type: EMAIL | PC (邮件｜站内信)
     * @param upd_msg_status: 是否更改已有Message的发送状态
     * @throws SQLException
     * @throws cwException
     */
    public void updMessageTargetTimestamp(Connection con, Timestamp target_datetime, Vector<Long> msg_id_lst, String msg_type, boolean upd_msg_status)
	    throws SQLException, cwException {
    	
    	if(msg_type.equalsIgnoreCase(MESSAGE_CONTENT_TYPE_EMAIL)){
    		updEmsgTargetTimestamp(con, target_datetime, msg_id_lst);
    		if(upd_msg_status){
    			updEmsgHisStatus(con, msg_id_lst, EmailMsgRecHistory.SEND_TYPE_NO, null, 0);
    		}
    	}else if(msg_type.equalsIgnoreCase(MESSAGE_CONTENT_TYPE_PC));{
    		updWmsgTargetTimestamp(con, target_datetime, msg_id_lst);
    	}
    }

	/**更新邮件发送时间
	 * @param con
	 * @param target_datetime
	 * @param msg_id_lst
	 * @throws SQLException
	 */
	private void updEmsgTargetTimestamp(Connection con, Timestamp target_datetime, Vector<Long> msg_id_lst) 
		throws SQLException {
		String sql = "update emailMessage set emsg_target_datetime = ? where emsg_id in " + cwUtils.vector2list(msg_id_lst);

		PreparedStatement stmt = null;
		try {
		    stmt = con.prepareStatement(sql);
		    int index =1 ;
		    stmt.setTimestamp(index++, target_datetime);
		    
		    stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}

	/**更新站内信发送时间
	 * @param con
	 * @param target_datetime
	 * @param msg_id_lst
	 * @param upd_msg_status
	 * @throws SQLException
	 */
	private void updWmsgTargetTimestamp(Connection con, Timestamp target_datetime, Vector<Long> msg_id_lst) throws SQLException {
		String sql = "update webMessage set wmsg_target_datetime = ? where wmsg_id in " + cwUtils.vector2list(msg_id_lst);
		PreparedStatement stmt = null;
		try {
		    stmt = con.prepareStatement(sql);
		    int index =1 ;
		    stmt.setTimestamp(index++, target_datetime);
		    
		    stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
	}
    
    /**更新邮件发送状态
     * @param con
     * @param msgIdVec
     * @param status
     * @param sent_datetime
     * @param attempted
     * @throws SQLException
     */
    public void updEmsgHisStatus(Connection con, Vector<Long> msgIdVec, String status, Timestamp sent_datetime, long attempted)
    throws SQLException {
        
        String SQL = " UPDATE emailMsgRecHistory "
                   + " SET emrh_status = ?, "
                   + " emrh_sent_datetime = ?, "
                   + " emrh_attempted = ? "
                   + " WHERE emrh_emsg_id in "+ cwUtils.vector2list(msgIdVec);
        PreparedStatement stmt = null;
        try{           
	        stmt = con.prepareStatement(SQL);
        	int index = 1;
            stmt.setString(index++, status);
            stmt.setTimestamp(index++, sent_datetime);
            stmt.setLong(index++, attempted);
	        stmt.executeUpdate();
	    } finally {
			cwSQL.closePreparedStatement(stmt);
		}
    }

	/**删除消息
	 * @param con
	 * @param msg_id_vec 消息ID
	 * @param msg_type 邮件|站内信 （email | pc）
	 * @throws SQLException 
	 */
	public void delMessage(Connection con, Vector<Long> msg_id_vec, String msg_type) throws SQLException {
    	if(msg_type.equalsIgnoreCase(MESSAGE_CONTENT_TYPE_EMAIL)){
    		delEmailMsgHis(con, msg_id_vec);
    		delEmailMsg(con, msg_id_vec);

    	}else if(msg_type.equalsIgnoreCase(MESSAGE_CONTENT_TYPE_PC)){
    		delWebMsgHis(con, msg_id_vec);
    		delWebMsg(con, msg_id_vec);
    		
    	}
	}
	
	/**删除邮件
	 * @param con
	 * @param msg_id_lst
	 * @throws SQLException
	 */
	private void delEmailMsg(Connection con, Vector<Long> msg_id_lst) 
		throws SQLException {
		System.out.println(cwUtils.vector2list(msg_id_lst));
		String sql = "Delete from emailMessage where emsg_id in " + cwUtils.vector2list(msg_id_lst);
		
		PreparedStatement stmt = null;
		try {
		    stmt = con.prepareStatement(sql);
		    stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**删除邮件历史记录
	 * @param con
	 * @param msg_id_lst
	 * @throws SQLException
	 */
	private void delEmailMsgHis(Connection con, Vector<Long> msg_id_lst) 
		throws SQLException {
		String sql = "Delete FROM emailMsgRecHistory where emrh_emsg_id in " + cwUtils.vector2list(msg_id_lst);
	
		PreparedStatement stmt = null;
		try {
		    stmt = con.prepareStatement(sql);
		    stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**删除站内信
	 * @param con
	 * @param msg_id_lst
	 * @throws SQLException
	 */
	private void delWebMsg(Connection con, Vector<Long> msg_id_lst) 
		throws SQLException {
		String sql = "Delete from webMessage where wmsg_id in " + cwUtils.vector2list(msg_id_lst);
		
		PreparedStatement stmt = null;
		try {
		    stmt = con.prepareStatement(sql);
		    stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}
	
	/**删除站内信阅读记录
	 * @param con
	 * @param msg_id_lst
	 * @throws SQLException
	 */
	private void delWebMsgHis(Connection con, Vector<Long> msg_id_lst) 
		throws SQLException {
		String sql = "Delete from webMsgReadHistory where wmrh_wmsg_id in " + cwUtils.vector2list(msg_id_lst);
	
		PreparedStatement stmt = null;
		try {
		    stmt = con.prepareStatement(sql);
		    stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}

	public long getAttempt(Connection con, long msg_id) throws SQLException {
		String sql = " select emrh_attempted from emailMsgRecHistory where emrh_emsg_id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, msg_id);
        ResultSet rs = stmt.executeQuery();
        long id = 0;
        if( rs.next() ) {
            id = rs.getLong("emrh_attempted");
        }
        cwSQL.cleanUp(rs, stmt);
        return id;
	}
	
	
	/**
	 * 插入邮件和站内的中间表
	 * @param con
	 * @param emsg_id
	 * @param wmsg_id
	 * @throws SQLException
	 */
	public static void insertRelationBetweenEmailMessageAndWebMessage(Connection con,long emsg_id,long wmsg_id) throws SQLException{
		
		String sql = " INSERT INTO emsgRwmsg (emsg_id, wmsg_id) VALUES (?, ?) ";

      PreparedStatement stmt = null;
      try {
	        stmt = con.prepareStatement(sql);
			int index = 1;
	        stmt.setLong(index++, emsg_id);
	        stmt.setLong(index++, wmsg_id);
	        stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
		
	}

	public static List<WebMessage> selectWebMessageListByEmsgId(Connection con,long emsgId) throws SQLException {
		
		List<WebMessage> webMessageList = new ArrayList<WebMessage>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = " select * from webMessage where wmsg_id in (select wmsg_id from emsgRwmsg where emsg_id =  ?) ";
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, emsgId);
	        rs = stmt.executeQuery();
	        
	        while(rs.next()) {
	        	WebMessage msg = new WebMessage();
	        	
	        	msg.setWmsg_id(rs.getString("wmsg_id"));
	        	msg.setWmsg_send_ent_id(rs.getLong("wmsg_send_ent_id"));
	        	msg.setWmsg_rec_ent_id(rs.getLong("wmsg_rec_ent_id"));
	        	msg.setWmsg_subject(rs.getString("wmsg_subject"));
	        	msg.setWmsg_content_mobile(rs.getString("wmsg_content_mobile"));
	        	msg.setWmsg_content_pc(rs.getString("wmsg_content_pc"));
	        	
	        	webMessageList.add(msg);
	        }
	        
	        return webMessageList;
		}finally{
			cwSQL.cleanUp(rs, stmt);
		}
	}
}

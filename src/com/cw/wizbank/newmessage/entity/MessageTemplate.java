package com.cw.wizbank.newmessage.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.util.cwSQL;

public class MessageTemplate {
    public final static String TYPE_JI = "JI";
    public final static String TYPE_REMINDER = "REMINDER";
    
	long mtp_id;
	String mtp_type;
	String mtp_subject;
	String mtp_content;
	String mtp_content_email_link;
	String mtp_content_pc_link;
	String mtp_content_mobile_link;
	boolean mtp_web_message_ind;
	boolean mtp_active_ind;
	long mtp_tcr_id;
	String mtp_remark_label;
	long mtp_update_ent_id;
	Timestamp mtp_update_timestamp;
	boolean isUpd;
	String mtp_header_img;
	String mtp_footer_img;
	
	/**
	 * weteam 推送 新增的 字段
	 */
	String courseTitle;
	
	
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	public long getMtp_id() {
		return mtp_id;
	}
	public void setMtp_id(long mtp_id) {
		this.mtp_id = mtp_id;
	}
	public String getMtp_type() {
		return mtp_type;
	}
	public void setMtp_type(String mtp_type) {
		this.mtp_type = mtp_type;
	}
	public String getMtp_subject() {
		return mtp_subject;
	}
	public void setMtp_subject(String mtp_subject) {
		this.mtp_subject = mtp_subject;
	}
	public String getMtp_content() {
		return mtp_content;
	}
	public void setMtp_content(String mtp_content) {
		this.mtp_content = mtp_content;
	}
	public String getMtp_content_email_link() {
		return mtp_content_email_link;
	}
	public void setMtp_content_email_link(String mtp_content_email_link) {
		this.mtp_content_email_link = mtp_content_email_link;
	}
	public String getMtp_content_pc_link() {
		return mtp_content_pc_link;
	}
	public void setMtp_content_pc_link(String mtp_content_pc_link) {
		this.mtp_content_pc_link = mtp_content_pc_link;
	}
	public String getMtp_content_mobile_link() {
		return mtp_content_mobile_link;
	}
	public void setMtp_content_mobile_link(String mtp_content_mobile_link) {
		this.mtp_content_mobile_link = mtp_content_mobile_link;
	}
	public boolean isMtp_web_message_ind() {
		return mtp_web_message_ind;
	}
	public void setMtp_web_message_ind(boolean mtp_web_message_ind) {
		this.mtp_web_message_ind = mtp_web_message_ind;
	}
	public boolean isMtp_active_ind() {
		return mtp_active_ind;
	}
	public void setMtp_active_ind(boolean mtp_active_ind) {
		this.mtp_active_ind = mtp_active_ind;
	}
	public long getMtp_tcr_id() {
		return mtp_tcr_id;
	}
	public void setMtp_tcr_id(long mtp_tcr_id) {
		this.mtp_tcr_id = mtp_tcr_id;
	}
	public String getMtp_remark_label() {
		return mtp_remark_label;
	}
	public void setMtp_remark_label(String mtp_remark_label) {
		this.mtp_remark_label = mtp_remark_label;
	}
	public long getMtp_update_ent_id() {
		return mtp_update_ent_id;
	}
	public void setMtp_update_ent_id(long mtp_update_ent_id) {
		this.mtp_update_ent_id = mtp_update_ent_id;
	}
	public Timestamp getMtp_update_timestamp() {
		return mtp_update_timestamp;
	}
	public void setMtp_update_timestamp(Timestamp mtp_update_timestamp) {
		this.mtp_update_timestamp = mtp_update_timestamp;
	}
	public boolean isUpd() {
		return isUpd;
	}
	public void setUpd(boolean isUpd) {
		this.isUpd = isUpd;
	}
	public String getMtp_header_img() {
		return mtp_header_img;
	}
	public void setMtp_header_img(String mtp_header_img) {
		this.mtp_header_img = mtp_header_img;
	}
	public String getMtp_footer_img() {
		return mtp_footer_img;
	}
	public void setMtp_footer_img(String mtp_footer_img) {
		this.mtp_footer_img = mtp_footer_img;
	}
	
	public static boolean isExist(Connection con, long mtp_id) throws SQLException {
		boolean exist = false;
		String sql = "select mtp_id from messageTemplate where mtp_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, mtp_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				exist = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return exist;
	}
	
	public void get(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sql = "select mtp_id, mtp_type, mtp_subject, mtp_content, mtp_content_email_link, mtp_content_pc_link, mtp_content_mobile_link, mtp_active_ind, mtp_web_message_ind, mtp_remark_label, mtp_header_img, mtp_footer_img from messageTemplate " +
					 "where mtp_id = ?  ";

		try {
			stmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int index = 1;
			stmt.setLong(index++, mtp_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				this.setMtp_id(rs.getLong("mtp_id"));
				this.setMtp_type(rs.getString("mtp_type"));
				this.setMtp_subject(rs.getString("mtp_subject"));
				this.setMtp_content_email_link(rs.getString("mtp_content_email_link"));
				this.setMtp_content_pc_link(rs.getString("mtp_content_pc_link"));
				this.setMtp_content_mobile_link(rs.getString("mtp_content_mobile_link"));
				this.setMtp_active_ind(rs.getBoolean("mtp_active_ind"));
				this.setMtp_web_message_ind(rs.getBoolean("mtp_web_message_ind"));
				this.setMtp_remark_label(rs.getString("mtp_remark_label"));
				this.setMtp_header_img(rs.getString("mtp_header_img"));
				this.setMtp_footer_img(rs.getString("mtp_footer_img"));
				String mtp_content = rs.getString("mtp_content");
				if(!this.isUpd){
					Map<String, String> map = new HashMap<String, String>();
					MessageService msgService = new MessageService();
					map.put("mtp_header_img", msgService.getEmailLinkPre(rs.getString("mtp_header_img")));
					map.put("mtp_footer_img", msgService.getEmailLinkPre(rs.getString("mtp_footer_img")));
					mtp_content = changeMtpContentImg(map, mtp_content);
				}
				this.setMtp_content(mtp_content);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	
	public void update(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer("UPDATE messageTemplate");
		sql.append(" SET");
		sql.append(" mtp_subject = ?,");
		sql.append(" mtp_content = ?,");
		sql.append(" mtp_web_message_ind =?,");
		sql.append(" mtp_active_ind = ?,");
		sql.append(" mtp_update_ent_id = ?,");
		sql.append(" mtp_update_timestamp = ?");
		if(mtp_header_img != null && !("").equals(mtp_header_img)){
			sql.append(", mtp_header_img = ?");
		}
		if(mtp_footer_img != null && !("").equals(mtp_footer_img)){
			sql.append(", mtp_footer_img = ?");
		}
		sql.append(" WHERE mtp_id = ?");

		PreparedStatement stmt = null;

		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setString(index++, mtp_subject);
			stmt.setString(index++, mtp_content);
			stmt.setBoolean(index++, mtp_web_message_ind);
			stmt.setBoolean(index++, mtp_active_ind);
			stmt.setLong(index++, mtp_update_ent_id);
			stmt.setTimestamp(index++, mtp_update_timestamp);
			if(mtp_header_img != null && !("").equals(mtp_header_img)){
				stmt.setString(index++, mtp_header_img);
			}
			if(mtp_footer_img != null && !("").equals(mtp_footer_img)){
				stmt.setString(index++, mtp_footer_img);
			}
			
			stmt.setLong(index++, mtp_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	
	
	
	public void getByTcr(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sql = " select mtp_id, mtp_type, mtp_subject, mtp_content, mtp_content_email_link, mtp_content_pc_link, mtp_content_mobile_link, mtp_active_ind, mtp_web_message_ind, mtp_remark_label, mtp_header_img, mtp_footer_img from messageTemplate " +
					 " where mtp_type = ?  and mtp_tcr_id = ? ";
		try {
			stmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			int index = 1;
			stmt.setString(index++, mtp_type);
			//目前邮件不分培训中心
			stmt.setLong(index++, mtp_tcr_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.setMtp_id(rs.getLong("mtp_id"));
				this.setMtp_type(rs.getString("mtp_type"));
				this.setMtp_subject(rs.getString("mtp_subject"));
				this.setMtp_content_email_link(rs.getString("mtp_content_email_link"));
				this.setMtp_content_pc_link(rs.getString("mtp_content_pc_link"));
				this.setMtp_content_mobile_link(rs.getString("mtp_content_mobile_link"));
				this.setMtp_active_ind(rs.getBoolean("mtp_active_ind"));
				this.setMtp_web_message_ind(rs.getBoolean("mtp_web_message_ind"));
				this.setMtp_remark_label(rs.getString("mtp_remark_label"));
				this.setMtp_header_img(rs.getString("mtp_header_img"));
				this.setMtp_footer_img(rs.getString("mtp_footer_img"));
				this.setMtp_content(rs.getString("mtp_content"));
			}else{
				//LN模式 企业未找到自定义的模板 则用平台中根即最顶层培训中心id加载模板
				cwSQL.cleanUp(rs, stmt);
				long root_tcr_id = ViewTrainingCenter.getRootTcId(con);
				stmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				index = 1;
				stmt.setString(index++, mtp_type);
				stmt.setLong(index++, root_tcr_id);
				rs = stmt.executeQuery();
				if (rs.next()) {
					this.setMtp_id(rs.getLong("mtp_id"));
					this.setMtp_type(rs.getString("mtp_type"));
					this.setMtp_subject(rs.getString("mtp_subject"));
					this.setMtp_content_email_link(rs.getString("mtp_content_email_link"));
					this.setMtp_content_pc_link(rs.getString("mtp_content_pc_link"));
					this.setMtp_content_mobile_link(rs.getString("mtp_content_mobile_link"));
					this.setMtp_active_ind(rs.getBoolean("mtp_active_ind"));
					this.setMtp_web_message_ind(rs.getBoolean("mtp_web_message_ind"));
					this.setMtp_remark_label(rs.getString("mtp_remark_label"));
					this.setMtp_header_img(rs.getString("mtp_header_img"));
					this.setMtp_footer_img(rs.getString("mtp_footer_img"));
					this.setMtp_content(rs.getString("mtp_content"));
				}
				
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	
	/**
	 * 获取 顶级培训中心存在，但指定二级培训中心不存中的邮件模板
	 * @param con
	 * @param root_tcr_id  顶级培训中心ID
	 * * @param des_tcr_id  二级培训中心ID
	 * @return
	 * @throws SQLException
	 */
	
	public List<Long> getRecordForCopy(Connection con, long root_tcr_id, long des_tcr_id) throws SQLException {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			List<Long> list = new ArrayList<Long>();
			String sql = " select mtp_id from messageTemplate where mtp_tcr_id = ? and mtp_type not in(select mtp_type from messageTemplate where mtp_tcr_id= ?) " ;
			try {
				
				stmt = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				int index = 1;
				stmt.setLong(index++, root_tcr_id);
				stmt.setLong(index++, des_tcr_id);
				rs = stmt.executeQuery();
				while (rs.next()) {
					list.add(rs.getLong("mtp_id"));
				}
			} finally {
				cwSQL.cleanUp(rs, stmt);
			}
			return list;
	}
	
	/**
	 * 把指定的模板 copy到二级培训中心
	 * @param con
	 
	 * @param dis_tcr_id  二级培训中心ID
	 * @param mtp_id  模板ID
	 * @return
	 * @throws SQLException
	 */
	public long copyMessageTemplate(Connection con, long dis_tcr_id, long mtp_id) throws SQLException {
		long new_mtp_id = 0;
		StringBuffer sql = new StringBuffer();
		sql.append(" select mtp_type,mtp_subject,mtp_content,mtp_content_email_link,mtp_content_pc_link,mtp_content_mobile_link,mtp_web_message_ind,mtp_active_ind,mtp_remark_label,mtp_update_ent_id,mtp_update_timestamp,mtp_header_img,mtp_footer_img");
		sql.append(" from messageTemplate where mtp_id = ?");
		PreparedStatement stmt = null;

		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setLong(index++, mtp_id);
			ResultSet set = stmt.executeQuery();
			MessageTemplate messageTemplate = null;
			if(set.next()){
				messageTemplate = new MessageTemplate();
				messageTemplate.setMtp_type(set.getString("mtp_type"));
				messageTemplate.setMtp_subject(set.getString("mtp_subject"));
				messageTemplate.setMtp_content(set.getString("mtp_content"));
				messageTemplate.setMtp_content_email_link(set.getString("mtp_content_email_link"));
				messageTemplate.setMtp_content_pc_link(set.getString("mtp_content_pc_link"));
				messageTemplate.setMtp_content_mobile_link(set.getString("mtp_content_mobile_link"));
				messageTemplate.setMtp_web_message_ind(set.getBoolean("mtp_web_message_ind"));
				messageTemplate.setMtp_active_ind(set.getBoolean("mtp_active_ind"));
				messageTemplate.setMtp_remark_label(set.getString("mtp_remark_label"));
				messageTemplate.setMtp_update_ent_id(set.getLong("mtp_update_ent_id"));
				messageTemplate.setMtp_update_timestamp(set.getTimestamp("mtp_update_timestamp"));
				messageTemplate.setMtp_header_img(set.getString("mtp_header_img"));
				messageTemplate.setMtp_footer_img(set.getString("mtp_footer_img"));
			}
			cwSQL.cleanUp(set, stmt);
			if(messageTemplate != null){
				sql = new StringBuffer();
				sql.append("insert into messageTemplate ");
				sql.append(" (mtp_type,mtp_subject,mtp_content,mtp_content_email_link,mtp_content_pc_link,mtp_content_mobile_link,mtp_web_message_ind,mtp_active_ind,mtp_tcr_id,mtp_remark_label,mtp_update_ent_id,mtp_update_timestamp,mtp_header_img,mtp_footer_img)");
				sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				stmt = con.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
				index = 1;
				stmt.setString(index++, messageTemplate.getMtp_type());
				stmt.setString(index++, messageTemplate.getMtp_subject());
				stmt.setString(index++, messageTemplate.getMtp_content());
				stmt.setString(index++, messageTemplate.getMtp_content_email_link());
				stmt.setString(index++, messageTemplate.getMtp_content_pc_link());
				stmt.setString(index++, messageTemplate.getMtp_content_mobile_link());
				stmt.setBoolean(index++, messageTemplate.isMtp_web_message_ind());
				stmt.setBoolean(index++, messageTemplate.isMtp_active_ind());
				stmt.setLong(index++, dis_tcr_id);
				stmt.setString(index++, messageTemplate.getMtp_remark_label());
				stmt.setLong(index++, messageTemplate.getMtp_update_ent_id());
				stmt.setTimestamp(index++, messageTemplate.getMtp_update_timestamp());
				stmt.setString(index++, messageTemplate.getMtp_header_img());
				stmt.setString(index++, messageTemplate.getMtp_footer_img());
				stmt.executeUpdate();
				new_mtp_id = cwSQL.getAutoId(con, stmt, "messageTemplate", "mtp_id");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return new_mtp_id;
	}
	/**
	 * 把指定培训中心下的邮件模板删除
	 * @param con
	 * @param tcr_id  二级培训中心ID
	 * @return
	 * @throws SQLException
	 */
	public void deleteTemplateByTc(Connection con, long tcr_id) throws SQLException {
		StringBuffer sql = new StringBuffer("delete from messageTemplate where mtp_tcr_id = ?");
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setLong(index++, tcr_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	
	public static boolean isActive(Connection con, long mtp_tcr_id, String mtp_type) throws SQLException {
		boolean exist = false;
		String sql = "select mtp_active_ind from messageTemplate where mtp_tcr_id = ? and mtp_type = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, mtp_tcr_id);
			stmt.setString(index++, mtp_type);
			rs = stmt.executeQuery();
			if (rs.next()) {
				exist = rs.getBoolean("mtp_active_ind");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return exist;
	}
	
	public static boolean isMtpWebMessage(Connection con, long mtp_tcr_id, String mtp_type) throws SQLException {
		boolean exist = false;
		String sql = "select mtp_web_message_ind from messageTemplate where mtp_tcr_id = ? and mtp_type = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, mtp_tcr_id);
			stmt.setString(index++, mtp_type);
			rs = stmt.executeQuery();
			if (rs.next()) {
				exist = rs.getBoolean("mtp_web_message_ind");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return exist;
	}
	
	public String changeMtpContentImg(Map<String, String> map, String mtp_content) throws SQLException {
		String header_img = "<div style=\"border:1px solid #f2f2f2;width:100%;\">";
		String footer_img = "</div>";
		if(map.get("mtp_header_img") != null && !("").equalsIgnoreCase(map.get("mtp_header_img"))){
			String mtp_header_img = map.get("mtp_header_img");
			header_img += "<img src=\"" + mtp_header_img + "\" width=\"100%\">";
		}
		if(map.get("mtp_footer_img") != null && !("").equalsIgnoreCase(map.get("mtp_footer_img"))){
			String mtp_footer_img = map.get("mtp_footer_img");
			footer_img += "<img src=\"" + mtp_footer_img + "\" width=\"100%\">";
		}
		return mtp_content.replace("[Header image]", header_img + "<div style=\"padding:10px;\">").replace("[Footer image]", footer_img + "</div>");
	}
	
	public String changeMtpContentImgForWebTeam(Map<String, String> map, String mtp_content) throws SQLException {
		
		return mtp_content.replace("[Header image]", "").replace("[Footer image]", "");
	}
}

package com.cw.wizbank.ae;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.cw.wizbank.JsonMod.Course.bean.CourseBean;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.ae.db.DbAppnTargetEntity;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * 学员报名类
 * @author kimyu
 *
 */
public class aeApplication {
	public long app_id;
	public long app_ent_id; // 参照Entity表的ent_id 
	public long app_itm_id;
	public String app_status; // 报名状态(可选值：Admitted)
	public String app_process_status; // 报名处理状态(可选值：已报名)
	public String app_process_xml;
	public String app_xml;
	public Timestamp app_create_timestamp;
	public String app_create_usr_id;
	public Timestamp app_upd_timestamp;
	public String app_upd_usr_id;
	public String app_ext1;
	public long app_ext2;
	public String app_ext3;
	public int app_notify_status;
	public Timestamp app_notify_datetime;
	public String app_ext4;
	public String app_usr_prof_xml;
	public String app_priority;
	public long app_tkh_id;
	public String gpm_full_path;
	public String ste_name;
	public String app_nominate_type;

	public long actn_process_id;
	public long actn_status_id;
	public long actn_action_id;
	public String actn_fr;
	public String actn_to;
	public String actn_verb;

	private String usr_id;
	public String usr_ste_usr_id;
	public long usr_ent_id;
	private String usr_last_name_bil;
	private String usr_first_name_bil;
	public String usr_display_bil;
	public String usr_tel_1;
	public String usr_group_name;
	public String usr_grade_name;

	public aeAppnActnHistory actn1;
	public aeAppnActnHistory actn2;
	public aeAppnCommHistory comm;

	public long itm_id;
	private String itm_code;
	public String itm_type;
	private String itm_title;
	private Timestamp itm_appn_start_datetime;
	private Timestamp itm_appn_end_datetime;
	public long itm_capacity;
	private long itm_unit;
	private String itm_status;
	private Timestamp itm_eff_start_datetime;
	private Timestamp itm_eff_end_datetime;
	public boolean itm_run_ind;
	public long itm_owner_ent_id;
	public String workFlowTpl;
	public long workFlowTplId;
	public boolean canBeAdmitted;
	public Hashtable export_cols_hash;
	public boolean itm_not_allow_waitlist_ind;
    public boolean is_complete_del;

	public static final String PENDING = "Pending";
	public static final String ADMITTED = "Admitted";
	public static final String WAITING = "Waiting";
	public static final String REJECTED = "Rejected";
	public static final String WITHDRAWN = "Withdrawn";
	
	public static final String NOMINATE_TYPE_SUP = "SUP";

    public static final String NOMINATE_TYPE_TADM = "TADM";
    
    public static final String PENDING_APPROVAL = "pending approval";

	public static final String[] QUEUES = { PENDING, ADMITTED, WAITING, REJECTED, WITHDRAWN };
	public static final String[] ORDER_APP = { "applicant", "item", "type", "code" };
	public static final String[] ORDER_APP_DB = { "usr_ste_usr_id", "itm_title", "itm_type", "itm_code" };
	public static final String[] ORDER_APP_BY_ITEM = { "applicant", "last_update" };
	public static final String[] ORDER_APP_BY_ITEM_DB = { "usr_ste_usr_id", "app_upd_timestamp" };

	/*    public static final String HIGH_PRIORITY = "1";
	    public static final String MEDIUM_PRIORITY = "2";
	    public static final String LOW_PRIORITY = "3";    */
	public static final String PRIORITY_NULL = "NULL";

	public final static String COMMA = ",";

	public static class ViewAppnUser {
		public String usr_id;
		public long usr_ent_id;
		public String usr_display_bil;
		public String usr_email_2;
		public int app_notify_status;
		public Timestamp app_notify_datetime;
	}

	public aeApplication() {
		;
	}

	//get the raw template of item the application belongs to
	public String getRawTemplate(Connection con, String tpl_type) throws SQLException {
		aeItem itm = new aeItem();
		itm.itm_id = app_itm_id;
		return itm.getRawTemplate(con, tpl_type);
	}

	public void ins(Connection con) throws SQLException, cwException, qdbException {
		PreparedStatement stmt;
		StringBuffer bufSQL = new StringBuffer();
		Timestamp cur_time;

		String clobNull = cwSQL.getClobNull();

		bufSQL.append("INSERT INTO aeApplication ");
		bufSQL.append("(app_ent_id, app_itm_id, app_status, app_process_status, app_process_xml, app_xml, ");
		bufSQL.append(" app_create_timestamp, app_create_usr_id, app_upd_timestamp, app_upd_usr_id, app_ext1, app_ext2, app_ext3, app_notify_status, app_tkh_id,app_nominate_type)");
		bufSQL.append(" VALUES (?, ?, ?, ?,").append(clobNull).append(",").append(clobNull).append(", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		cur_time = dbUtils.getTime(con);
		app_create_timestamp = cur_time;
		app_upd_timestamp = cur_time;
		stmt = con.prepareStatement(bufSQL.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setLong(1, app_ent_id);
		stmt.setLong(2, app_itm_id);
		stmt.setString(3, app_status);
		stmt.setString(4, app_process_status);
		//stmt.setString   (4, app_process_xml);
		//stmt.setString   (5, app_xml);
		stmt.setTimestamp(5, app_create_timestamp);
		stmt.setString(6, app_create_usr_id);
		stmt.setTimestamp(7, app_upd_timestamp);
		stmt.setString(8, app_upd_usr_id);
		stmt.setString(9, app_ext1);
		stmt.setLong(10, app_ext2);
		stmt.setString(11, app_ext3);
		stmt.setLong(12, 0);
		stmt.setLong(13, app_tkh_id);
		stmt.setString(14, app_nominate_type);

		if (stmt.executeUpdate() != 1) {
			stmt.close();
			throw new cwException("com.cw.wizbank.ae.aeApplication.ins: Fail to add application to DB");
		}

		// stmt.close();
		app_id = cwSQL.getAutoId(con, stmt, "aeApplication", "app_id");

		//update app_process_xml, app_xml for Oracle
		// for oracle clob
		String condition = "app_id = " + app_id;
		String tableName = "aeApplication";
		String[] colName = { "app_process_xml", "app_xml" };
		String[] colValue = { app_process_xml, app_xml };
		cwSQL.updateClobFields(con, tableName, colName, colValue, condition);

		stmt.close();
	}

	private static final String sql_upd_app_status = " Update aeApplication Set app_status = ? " + " Where app_id = ? ";

	public void updAppnStatus(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(sql_upd_app_status);
		stmt.setString(1, this.app_status);
		stmt.setLong(2, this.app_id);
		stmt.executeUpdate();
		stmt.close();
	}

	public void upd(Connection con) throws SQLException, qdbException {
		PreparedStatement stmt;
		StringBuffer bufSQL = new StringBuffer();

		bufSQL.append("UPDATE aeApplication SET ");
		bufSQL.append("app_status = ?,  app_process_status = ?, app_process_xml =").append(cwSQL.getClobNull()).append(", app_upd_timestamp = ?, app_upd_usr_id = ? ");

		if (app_priority != null) {
			bufSQL.append(", app_priority = ? ");
		}

		bufSQL.append(" WHERE app_id = ?");
		//bufSQL.append("app_status = ?,  app_upd_timestamp = ?, app_upd_usr_id = ? WHERE app_id = ?");

		int index = 1;
		app_upd_timestamp = dbUtils.getTime(con);
		stmt = con.prepareStatement(bufSQL.toString());
		stmt.setString(index++, app_status);
		stmt.setString(index++, app_process_status);
		//stmt.setString   (2, app_process_xml);
		stmt.setTimestamp(index++, app_upd_timestamp);
		stmt.setString(index++, app_upd_usr_id);

		if (app_priority != null) {
			if (app_priority.equalsIgnoreCase(PRIORITY_NULL)) {
				stmt.setString(index++, cwSQL.get_null_sql("String"));
			} else {
				stmt.setString(index++, app_priority);
			}
		}

		stmt.setLong(index++, app_id);

		if (stmt.executeUpdate() != 1) {
			stmt.close();
			throw new qdbException("com.cw.wizbank.ae.aeApplication.ins: Fail to update application from DB");
		}

		stmt.close();

		// Update app_process_xml
		//for oracle clob
		String condition = "app_id = " + app_id;
		String tableName = "aeApplication";
		String[] colName = { "app_process_xml" };
		String[] colValue = { app_process_xml };
		cwSQL.updateClobFields(con, tableName, colName, colValue, condition);
	}

	/**
	Update aeApplication without updating the last update time<BR>
	Used by Event Trigger
	*/
	public void updNoTime(Connection con) throws SQLException, qdbException {
		PreparedStatement stmt;
		StringBuffer bufSQL = new StringBuffer();

		bufSQL.append("UPDATE aeApplication SET ");
		bufSQL.append("app_status = ?,  app_process_status = ?, app_process_xml =").append(cwSQL.getClobNull());

		if (app_priority != null) {
			bufSQL.append(", app_priority = ? ");
		}

		bufSQL.append(" WHERE app_id = ?");

		app_upd_timestamp = dbUtils.getTime(con);
		stmt = con.prepareStatement(bufSQL.toString());
		int index = 1;
		stmt.setString(index++, app_status);
		stmt.setString(index++, app_process_status);

		if (app_priority != null) {
			if (app_priority.equalsIgnoreCase(PRIORITY_NULL)) {
				stmt.setString(index++, cwSQL.get_null_sql("String"));
			} else {
				stmt.setString(index++, app_priority);
			}
		}

		stmt.setLong(index++, app_id);

		if (stmt.executeUpdate() != 1) {
			stmt.close();
			throw new qdbException("com.cw.wizbank.ae.aeApplication.ins: Fail to update application from DB");
		}

		stmt.close();

		// Update app_process_xml
		//for oracle clob
		String condition = "app_id = " + app_id;
		String tableName = "aeApplication";
		String[] colName = { "app_process_xml" };
		String[] colValue = { app_process_xml };
		cwSQL.updateClobFields(con, tableName, colName, colValue, condition);
	}

	public void get(Connection con) throws SQLException, qdbException {
		PreparedStatement stmt;
		ResultSet rs;

		stmt = con
				.prepareStatement("SELECT app_ent_id, app_itm_id, app_status, app_process_status, app_process_xml, app_xml, app_create_timestamp, app_create_usr_id, app_upd_timestamp, app_upd_usr_id, app_notify_status, app_notify_datetime, app_usr_prof_xml, app_ext4, app_tkh_id, usr_id, usr_ent_id, usr_ste_usr_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil,app_nominate_type FROM aeApplication, RegUser WHERE app_id = ? AND usr_ent_id = app_ent_id");
		stmt.setLong(1, app_id);
		rs = stmt.executeQuery();

		if (rs.next()) {
			app_ent_id = rs.getLong("app_ent_id");
			app_itm_id = rs.getLong("app_itm_id");
			app_status = rs.getString("app_status");
			app_process_status = rs.getString("app_process_status");
			//app_process_xml      = rs.getString   ("app_process_xml");
			//app_xml              = rs.getString   ("app_xml");
			app_process_xml = cwSQL.getClobValue(rs, "app_process_xml");
			app_xml = cwSQL.getClobValue(rs, "app_xml");
			app_create_timestamp = rs.getTimestamp("app_create_timestamp");
			app_create_usr_id = rs.getString("app_create_usr_id");
			app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
			app_upd_usr_id = rs.getString("app_upd_usr_id");
			app_notify_status = rs.getInt("app_notify_status");
			app_notify_datetime = rs.getTimestamp("app_notify_datetime");
			app_usr_prof_xml = cwSQL.getClobValue(rs, "app_usr_prof_xml");
			app_ext4 = cwSQL.getClobValue(rs, "app_ext4");
			app_tkh_id = rs.getLong("app_tkh_id");
			usr_id = rs.getString("usr_id");
			usr_ent_id = rs.getLong("usr_ent_id");
			usr_ste_usr_id = rs.getString("usr_ste_usr_id");
			usr_last_name_bil = rs.getString("usr_last_name_bil");
			usr_first_name_bil = rs.getString("usr_first_name_bil");
			usr_display_bil = rs.getString("usr_display_bil");
			app_nominate_type = rs.getString("app_nominate_type");
		} else {
			if (rs != null)
				rs.close();
			stmt.close();
			throw new qdbException("com.cw.wizbank.ae.aeApplication.get: Fail to retrieve application from DB");
		}
		rs.close();
		stmt.close();
	}

	public void getWithItem(Connection con) throws SQLException, qdbException {
		PreparedStatement stmt;
		ResultSet rs;

		stmt = con
				.prepareStatement("SELECT app_ent_id, app_itm_id, app_status, app_process_status, app_process_xml, app_xml, app_create_timestamp, app_create_usr_id, app_upd_timestamp, app_upd_usr_id, app_usr_prof_xml, app_ext4, app_tkh_id, usr_id, usr_ent_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil, usr_ste_usr_id, itm_id, itm_code, itm_type, itm_title, itm_appn_start_datetime, itm_appn_end_datetime, itm_capacity, itm_unit, itm_status, itm_eff_start_datetime, itm_eff_end_datetime, itm_run_ind, itm_owner_ent_id, itm_not_allow_waitlist_ind FROM aeApplication, aeItem, RegUser WHERE app_id = ? AND usr_ent_id = app_ent_id AND itm_id = app_itm_id");
		stmt.setLong(1, app_id);
		rs = stmt.executeQuery();

		if (rs.next()) {
			app_ent_id = rs.getLong("app_ent_id");
			app_itm_id = rs.getLong("app_itm_id");
			app_status = rs.getString("app_status");
			app_process_status = rs.getString("app_process_status");
			//app_process_xml      = rs.getString   ("app_process_xml");
			//app_xml              = rs.getString   ("app_xml");
			app_process_xml = cwSQL.getClobValue(rs, "app_process_xml");
			app_xml = cwSQL.getClobValue(rs, "app_xml");
			app_create_timestamp = rs.getTimestamp("app_create_timestamp");
			app_create_usr_id = rs.getString("app_create_usr_id");
			app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
			app_upd_usr_id = rs.getString("app_upd_usr_id");
			app_usr_prof_xml = cwSQL.getClobValue(rs, "app_usr_prof_xml");
			app_ext4 = cwSQL.getClobValue(rs, "app_ext4");
			app_tkh_id = rs.getLong("app_tkh_id");
			usr_id = rs.getString("usr_id");
			usr_ent_id = rs.getLong("usr_ent_id");
			usr_last_name_bil = rs.getString("usr_last_name_bil");
			usr_first_name_bil = rs.getString("usr_first_name_bil");
			usr_display_bil = rs.getString("usr_display_bil");
			usr_ste_usr_id = rs.getString("usr_ste_usr_id");

			itm_id = rs.getLong("itm_id");
			itm_code = rs.getString("itm_code");
			itm_type = rs.getString("itm_type");
			itm_title = rs.getString("itm_title");
			itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
			itm_appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
			itm_capacity = rs.getLong("itm_capacity");
			itm_unit = rs.getLong("itm_unit");
			itm_status = rs.getString("itm_status");
			itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
			itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
			itm_run_ind = rs.getBoolean("itm_run_ind");
			itm_owner_ent_id = rs.getLong("itm_owner_ent_id");
			itm_not_allow_waitlist_ind = rs.getBoolean("itm_not_allow_waitlist_ind");

			aeItem item = new aeItem();
			aeTemplate tpl = new aeTemplate();
			item.itm_id = itm_id;
			tpl.tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
			tpl.get(con);
			workFlowTpl = tpl.tpl_xml;
			workFlowTplId = tpl.tpl_id;
		} else {
			if (rs != null)
				rs.close();
			stmt.close();
			throw new qdbException("com.cw.wizbank.ae.aeApplication.getWithItem: Fail to retrieve application from DB");
		}
		rs.close();
		stmt.close();

	}

	/**
	 * 通过tkh_id查询app_id,itm_id的值
	 * @param con
	 * @throws SQLException
	 */
	public void getByThkId(Connection con) throws SQLException {
		PreparedStatement pst = con.prepareStatement("select app_id, app_itm_id from aeApplication where app_tkh_id = ? ");
		pst.setLong(1, app_tkh_id);
		ResultSet rs = pst.executeQuery();
		if (rs.next()) {
			app_id = rs.getLong("app_id");
			app_itm_id = rs.getLong("app_itm_id");
		}
		rs.close();
		pst.close();
		return;
	}

	private static String getByTkhId = "SELECT app_id,app_ent_id, app_itm_id, app_status, app_process_status," + "app_create_timestamp, app_create_usr_id, app_upd_timestamp, app_upd_usr_id, app_notify_status,"
			+ "app_notify_datetime, app_tkh_id FROM aeApplication WHERE app_tkh_id = ?";

	public void getWithTkhId(Connection con) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(getByTkhId);
			pst.setLong(1, app_tkh_id);
			rs = pst.executeQuery();
			if (rs.next()) {
				app_id = rs.getLong("app_id");
				app_ent_id = rs.getLong("app_ent_id");
				app_itm_id = rs.getLong("app_itm_id");
				app_status = rs.getString("app_status");
				app_process_status = rs.getString("app_process_status");
				app_create_timestamp = rs.getTimestamp("app_create_timestamp");
				app_create_usr_id = rs.getString("app_create_usr_id");
				app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
				app_upd_usr_id = rs.getString("app_upd_usr_id");
				app_notify_status = rs.getInt("app_notify_status");
				app_notify_datetime = rs.getTimestamp("app_notify_datetime");
				app_tkh_id = rs.getLong("app_tkh_id");
			} else {
				throw new SQLException("tkh_id = " + app_tkh_id + " has no corresponding record in table:aeApplication");
			}
		} catch (SQLException e) {
			throw new RuntimeException("Server error: " + e.getMessage());
		} finally {
			cwSQL.cleanUp(rs, pst);
		}
	}

	private static final String SQL_GET_QUE_FROM_ITM = " select app_id from aeApplication where app_itm_id = ? and app_status = ? ";

	public static long[] getQueueFromItem(Connection con, long itm_id, String queue_type) throws SQLException {
		PreparedStatement stmt = null;
		long[] app_id_lst = null;
		Vector v = null;
		try {
			stmt = con.prepareStatement(SQL_GET_QUE_FROM_ITM);
			stmt.setLong(1, itm_id);
			stmt.setString(2, queue_type);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (v == null) {
					v = new Vector();
				}
				v.addElement(new Long(rs.getLong("app_id")));
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}

		if (v != null) {
			app_id_lst = aeUtils.vec2longArray(v);
		}

		return app_id_lst;
	}

	public static long[] getQueueFromItem(Connection con, long root_ent_id, long itm_id, String queue_type, String sort_by, String order_by) throws SQLException, qdbException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long id;
		long[] queue = null;
		Vector v_queue = new Vector();
		StringBuffer SQL = new StringBuffer();
		StringBuffer orderSQL = new StringBuffer();

		if (itm_id == 0) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.getQueueFromItem: itm_id = 0");
		}

		SQL.append("SELECT app_id FROM aeApplication, aeItem, RegUser WHERE ");

		if (aeApplication.isValidQueue(queue_type)) {
			SQL.append("app_status = ? AND ");
		}

		SQL.append("app_itm_id = ? AND itm_id = app_itm_id AND usr_ent_id = app_ent_id AND itm_owner_ent_id = ? ORDER BY ");

		for (int i = 0; i < ORDER_APP_BY_ITEM.length; i++) {
			if (ORDER_APP_BY_ITEM[i].equalsIgnoreCase(sort_by)) {
				SQL.append(ORDER_APP_BY_ITEM_DB[i]);

				if (order_by.equalsIgnoreCase("DESC")) {
					SQL.append(" DESC, ");
				} else {
					SQL.append(" ASC, ");
				}
			} else {
				orderSQL.append(ORDER_APP_BY_ITEM_DB[i]);

				if (order_by.equalsIgnoreCase("DESC")) {
					orderSQL.append(" DESC, ");
				} else {
					orderSQL.append(" ASC, ");
				}
			}
		}

		SQL.append(orderSQL.toString());
		SQL.append("app_id");
		try {
			stmt = con.prepareStatement(SQL.toString());
			int index = 1;

			if (aeApplication.isValidQueue(queue_type)) {
				stmt.setString(index++, queue_type);
			}

			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, root_ent_id);

			rs = stmt.executeQuery();

			while (rs.next()) {
				id = new Long(rs.getLong("app_id"));
				v_queue.addElement(id);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		if (v_queue != null && v_queue.size() != 0) {
			queue = new long[v_queue.size()];

			for (int i = 0; i < v_queue.size(); i++) {
				id = (Long) v_queue.elementAt(i);
				queue[i] = id.longValue();
			}
		}

		return queue;
	}

	public static String printQueueFromItem(Connection con, long root_ent_id, long itm_id, String queue_type, String sort_by) throws SQLException, qdbException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long id;
		StringBuffer SQL = new StringBuffer();
		StringBuffer orderSQL = new StringBuffer();
		StringBuffer result = new StringBuffer();

		if (itm_id == 0) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.getQueueFromItem: itm_id = 0");
		}

		result.append("User ID,Last Name, First Name, Display Name").append(dbUtils.NEWL);

		SQL.append("SELECT usr_id, usr_ste_usr_id, usr_last_name_bil, usr_first_name_bil, usr_display_bil FROM aeApplication, aeItem, RegUser WHERE ");

		if (aeApplication.isValidQueue(queue_type)) {
			SQL.append("app_status = ? AND ");
		}

		SQL.append("app_itm_id = ? AND itm_id = app_itm_id AND usr_ent_id = app_ent_id AND itm_owner_ent_id = ? ORDER BY ");

		for (int i = 0; i < ORDER_APP_BY_ITEM.length; i++) {
			if (ORDER_APP_BY_ITEM[i].equalsIgnoreCase(sort_by)) {
				SQL.append(ORDER_APP_BY_ITEM_DB[i]);
			} else {
				orderSQL.append(ORDER_APP_BY_ITEM_DB[i]);
			}
		}
		try {
			stmt = con.prepareStatement(SQL.toString());
			int index = 1;

			if (aeApplication.isValidQueue(queue_type)) {
				stmt.setString(index++, queue_type);
			}

			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, root_ent_id);

			rs = stmt.executeQuery();

			while (rs.next()) {
				result.append(aeUtils.escNull(rs.getString("usr_ste_usr_id"))).append(",").append(rs.getString("usr_last_name_bil")).append(",").append(rs.getString("usr_first_name_bil")).append(",")
						.append(rs.getString("usr_display_bil")).append(dbUtils.NEWL);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return result.toString();
	}

	public static long[] getQueue(Connection con, long root_ent_id, String queue_type, String sort_by, String order_by, boolean showHistory) throws SQLException, qdbException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long id;
		long[] queue = null;
		Vector v_queue = new Vector();
		StringBuffer SQL = new StringBuffer();
		StringBuffer orderSQL = new StringBuffer();

		if (!aeApplication.isValidQueue(queue_type)) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.getQueue: Invalid Queue Type");
		}

		SQL.append("SELECT app_id FROM aeApplication, aeItem, RegUser WHERE app_status = ? AND itm_id = app_itm_id AND usr_ent_id = app_ent_id AND itm_owner_ent_id = ? AND itm_eff_end_datetime ");

		if (showHistory) {
			SQL.append(" < ? ");
		} else {
			SQL.append(" >= ? ");
		}

		SQL.append(" ORDER BY ");

		for (int i = 0; i < ORDER_APP.length; i++) {
			if (ORDER_APP[i].equalsIgnoreCase(sort_by)) {
				SQL.append(ORDER_APP_DB[i]);

				if (order_by.equalsIgnoreCase("DESC")) {
					SQL.append(" DESC, ");
				} else {
					SQL.append(" ASC, ");
				}
			} else {
				orderSQL.append(ORDER_APP_DB[i]);

				if (order_by.equalsIgnoreCase("DESC")) {
					orderSQL.append(" DESC, ");
				} else {
					orderSQL.append(" ASC, ");
				}
			}
		}

		SQL.append(orderSQL.toString());
		SQL.append("app_id");
		try {
			stmt = con.prepareStatement(SQL.toString());
			stmt.setString(1, queue_type);
			stmt.setLong(2, root_ent_id);
			stmt.setTimestamp(3, dbUtils.getTime(con));
			rs = stmt.executeQuery();

			while (rs.next()) {
				id = new Long(rs.getLong("app_id"));
				v_queue.addElement(id);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		if (v_queue != null && v_queue.size() != 0) {
			queue = new long[v_queue.size()];

			for (int i = 0; i < v_queue.size(); i++) {
				id = (Long) v_queue.elementAt(i);
				queue[i] = id.longValue();
			}
		}

		return queue;
	}

	public static int countQueue(Connection con, long itm_id, String queue_type) throws SQLException, qdbException {
		PreparedStatement stmt;
		ResultSet rs;
		int count = 0;

		if (itm_id == 0) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.countQueue: itm_id = 0");
		}

		if (!aeApplication.isValidQueue(queue_type)) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.countQueue: Invalid Queue Type");
		}

		stmt = con.prepareStatement("SELECT COUNT(*) AS CNT FROM aeApplication WHERE app_status = ? AND app_itm_id = ?");
		stmt.setString(1, queue_type);
		stmt.setLong(2, itm_id);
		rs = stmt.executeQuery();

		if (rs.next()) {
			count = rs.getInt("CNT");
		}
		rs.close();
		stmt.close();
		return count;
	}

	public static int countQueue(Connection con, Vector vItemId, String queue_type) throws SQLException, qdbException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
		StringBuffer SQLBuf = new StringBuffer();

		if (vItemId == null || vItemId.size() == 0) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.countQueue: vItemId is null or size() = 0");
		}

		if (!aeApplication.isValidQueue(queue_type)) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.countQueue: Invalid Queue Type");
		}

		SQLBuf.append(" Select count(*) as cnt from aeApplication ").append(" where app_status = ? ").append(" and app_itm_id in ").append(cwUtils.vector2list(vItemId));

		try {
			stmt = con.prepareStatement(SQLBuf.toString());
			stmt.setString(1, queue_type);
			rs = stmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt("CNT");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return count;
	}

	public static int countProcessStatus(Connection con, long itm_id, String[] process_status_lst) throws SQLException, qdbException {
		PreparedStatement stmt;
		ResultSet rs;
		int count = 0;

		if (itm_id == 0) {
			throw new qdbException("com.cw.wizbank.ae.aeApplication.countProcessStatus: itm_id = 0");
		}
		StringBuffer SQLBuf = new StringBuffer(512);
		SQLBuf.append("SELECT COUNT(*) AS CNT FROM aeApplication WHERE app_itm_id = ?");
		if (process_status_lst != null) {
			SQLBuf.append(" AND app_process_status in (");
			for (int i = 0; i < process_status_lst.length; i++) {
				if (i != 0) {
					SQLBuf.append(",");
				}
				SQLBuf.append("?");
			}
			SQLBuf.append(")");
		}
		stmt = con.prepareStatement(SQLBuf.toString());
		int index = 1;
		stmt.setLong(index++, itm_id);
		if (process_status_lst != null) {
			for (int i = 0; i < process_status_lst.length; i++) {
				stmt.setString(index++, process_status_lst[i]);
			}
		}
		rs = stmt.executeQuery();

		if (rs.next()) {
			count = rs.getInt("CNT");
		}
		rs.close();
		stmt.close();
		return count;
	}

	public String contentAsXML(Connection con, HttpSession sess, boolean includeItem, boolean includeProcess, boolean includeHistory) throws SQLException, qdbException {
		return contentAsXML(con, sess, includeItem, includeProcess, includeHistory, false);
	}

	public String contentAsXML(Connection con, HttpSession sess, boolean includeItem, boolean includeProcess, boolean includeHistory, boolean includePendingApprovalRole) throws SQLException, qdbException {
		StringBuffer result = new StringBuffer();

		result.append("<application id=\"");
		result.append(app_id);
		result.append("\" status=\"");
		result.append(app_status);
		result.append("\" tkh_id=\"");
		result.append(cwUtils.escZero(app_tkh_id));
		result.append("\" update_datetime=\"");
		result.append(app_upd_timestamp);
		result.append("\" update_usr_id=\"");
		result.append(app_upd_usr_id);
		result.append("\" create_datetime=\"");
		result.append(app_create_timestamp);
		result.append("\" create_usr_id=\"");
		result.append(app_create_usr_id);
		result.append("\">");
		result.append(dbUtils.NEWL);
		result.append(applicantAsXML());

		if (includeItem) {
			result.append(itemAsXML(con));
		}

		if (includeProcess) {
			result.append(app_process_xml);
		}

		if (includeHistory) {
			result.append(appnHistoryAsXML(con));
			result.append(appnAttemptAsXML(con, sess));
		}

		if (includePendingApprovalRole) {
			result.append("<pending_approval_role>").append(cwUtils.escNull(getPendingApprovalRole(con))).append("</pending_approval_role>");
		}

		result.append(dbUtils.NEWL);
		if (app_ext4 != null) {
			result.append(app_ext4);
		}
		result.append(dbUtils.NEWL);
		result.append("</application>");
		result.append(dbUtils.NEWL);

		return result.toString();
	}

	String applicantAsXML() {
		StringBuffer result = new StringBuffer();

		result.append("<applicant usr_id=\"");
		result.append(dbUtils.esc4XML(aeUtils.escNull(usr_ste_usr_id)));
		result.append("\" ent_id=\"");
		result.append(usr_ent_id);
		result.append("\" last_name=\"");
		result.append(dbUtils.esc4XML(usr_last_name_bil));
		result.append("\" first_name=\"");
		result.append(dbUtils.esc4XML(usr_first_name_bil));
		result.append("\" notify_status=\"");
		result.append(app_notify_status);
		result.append("\" notify_date=\"");
		result.append(app_notify_datetime);
		result.append("\">");
		result.append(dbUtils.NEWL);
		result.append("<display_name>");
		result.append(dbUtils.esc4XML(usr_display_bil));
		result.append("</display_name>");
		result.append(dbUtils.NEWL);
		result.append("</applicant>");
		result.append(dbUtils.NEWL);

		return result.toString();
	}

	public String appnHistoryAsXML(Connection con) throws SQLException, qdbException {
		StringBuffer result = new StringBuffer();
		long[] actn_list;
		long[] comm_list;
		aeAppnActnHistory actn;
		aeAppnCommHistory comm;

		result.append("<history>");
		result.append(dbUtils.NEWL);

		actn_list = aeAppnActnHistory.getActnList(con, app_id);
		comm_list = aeAppnCommHistory.getCommList(con, app_id);

		result.append("<queue_history>");
		result.append(dbUtils.NEWL);

		if (actn_list != null) {
			for (int i = 0; i < actn_list.length; i++) {
				actn = new aeAppnActnHistory();
				actn.aah_id = actn_list[i];
				actn.get(con);

				if (actn.aah_process_id == 0 && actn.aah_verb == null) {
					result.append(actn.contentAsXML(con));
				}
			}
		}

		result.append("</queue_history>");
		result.append(dbUtils.NEWL);
		result.append("<action_history>");
		result.append(dbUtils.NEWL);

		if (actn_list != null) {
			for (int i = 0; i < actn_list.length; i++) {
				actn = new aeAppnActnHistory();
				actn.aah_id = actn_list[i];
				actn.get(con);

				if ((actn.aah_process_id != 0) || (actn.aah_process_id == 0 && actn.aah_verb != null)) {
					result.append(actn.contentAsXML(con));
				}
			}
		}

		result.append("</action_history>");
		result.append(dbUtils.NEWL);
		result.append("<comment_history>");
		result.append(dbUtils.NEWL);

		if (comm_list != null) {
			for (int i = 0; i < comm_list.length; i++) {
				comm = new aeAppnCommHistory();
				comm.ach_id = comm_list[i];
				comm.get(con);
				result.append(comm.contentAsXML(con));
			}
		}

		result.append("</comment_history>");
		result.append(dbUtils.NEWL);
		result.append("</history>");
		result.append(dbUtils.NEWL);

		return result.toString();
	}

	public String appnLatestHistoryAsXML(Connection con) throws SQLException, qdbException {
		StringBuffer result = new StringBuffer();
		aeAppnActnHistory actn = new aeAppnActnHistory();
		actn.aah_app_id = this.app_id;
		actn.getLatestHistory(con);

		result.append("<history>").append("<action_history>").append(dbUtils.NEWL).append(actn.contentAsXML(con)).append("</action_history>").append("</history>").append(dbUtils.NEWL);

		return result.toString();
	}

	private String appnAttemptAsXML(Connection con, HttpSession sess) throws qdbException {
		StringBuffer result = new StringBuffer();
		StringBuffer queueBuf = new StringBuffer();
		StringBuffer actnBuf = new StringBuffer();
		StringBuffer commBuf = new StringBuffer();
		aeAppnActnHistory actn;
		aeAppnCommHistory comm;
		Vector v_action;
		Vector v_comment;
		Long id;
		String type;

		id = (Long) sess.getAttribute(aeQueueManager.APPN_HISTORY_ID);
		v_action = (Vector) sess.getAttribute(aeQueueManager.APPN_HISTORY_ACTION);
		v_comment = (Vector) sess.getAttribute(aeQueueManager.APPN_HISTORY_COMMENT);

		if (id != null && id.longValue() == app_id) {
			if (v_action != null && v_action.size() != 0) {
				for (int i = 0; i < v_action.size(); i++) {
					actn = (aeAppnActnHistory) v_action.elementAt(i);

					if (app_id == actn.aah_app_id) {
						if (actn.aah_process_id == 0) {
							queueBuf.append(actn.contentAsXML(con));
						} else {
							actnBuf.append(actn.contentAsXML(con));
						}
					}
				}
			}

			if (v_comment != null && v_comment.size() != 0) {
				for (int i = 0; i < v_comment.size(); i++) {
					comm = (aeAppnCommHistory) v_comment.elementAt(i);

					if (app_id == comm.ach_app_id) {
						commBuf.append(comm.contentAsXML(con));
					}
				}
			}
		}

		result.append("<attempt>");
		result.append(dbUtils.NEWL);
		result.append("<queue_attempt>");
		result.append(dbUtils.NEWL);
		result.append(queueBuf.toString());
		result.append("</queue_attempt>");
		result.append(dbUtils.NEWL);
		result.append("<action_attempt>");
		result.append(dbUtils.NEWL);
		result.append(actnBuf.toString());
		result.append("</action_attempt>");
		result.append(dbUtils.NEWL);
		result.append("<comment_attempt>");
		result.append(dbUtils.NEWL);
		result.append(commBuf.toString());
		result.append("</comment_attempt>");
		result.append(dbUtils.NEWL);
		result.append("</attempt>");
		result.append(dbUtils.NEWL);

		return result.toString();
	}

	public static String queueStatAsXML(Connection con, long itm_id, String queue_type) throws SQLException, qdbException {
		StringBuffer result = new StringBuffer();

		result.append("<queue ");

		if (QUEUES != null) {
			for (int i = 0; i < QUEUES.length; i++) {
				result.append(QUEUES[i]);
				result.append("=\"");
				result.append(countQueue(con, itm_id, QUEUES[i]));
				result.append("\" ");
			}
		}

		result.append("current=\"");

		if (aeApplication.isValidQueue(queue_type)) {
			result.append(queue_type);
		} else {
			result.append("all");
		}

		result.append("\"/>");
		result.append(dbUtils.NEWL);

		return result.toString();
	}

	public boolean checkUpdTimestamp(Timestamp time) {
		app_upd_timestamp.setNanos(time.getNanos());
		return app_upd_timestamp.equals(time);
	}

	public static boolean isValidQueue(String queue_type) {
		if (queue_type != null) {
			for (int i = 0; i < QUEUES.length; i++) {
				if (queue_type.equalsIgnoreCase(QUEUES[i])) {
					return true;
				}
			}
		}

		return false;
	}

	public String itemAsXML(Connection con) throws SQLException, qdbException {
		StringBuffer result = new StringBuffer();
		int numOFAdmitted = 0;
		int count;
		String capacity_status;

		//change capacity_status to pending_count
		/*
		        if (itm_capacity == 0) {
		            capacity_status = aeItem.ITM_UNLIMITED;
		        } else {
		            count = aeApplication.countQueue(con, itm_id, aeApplication.ADMITTED);

		            if (count == 0) {
		                capacity_status = aeItem.ITM_EMPTY;
		            } else if (itm_capacity > count) {
		                capacity_status = aeItem.ITM_AVAILABLE;
		            } else {
		                capacity_status = aeItem.ITM_FULL;
		            }
		        }
		*/

		int pending_count = aeApplication.countQueue(con, itm_id, aeApplication.PENDING);

		result.append("<item id=\"");
		result.append(itm_id);
		result.append("\" title=\"");
		result.append(dbUtils.esc4XML(itm_title));
		result.append("\" code=\"");
		result.append(dbUtils.esc4XML(itm_code));
		result.append("\" type=\"");
		result.append(dbUtils.esc4XML(itm_type));
		result.append("\" app_start_days_to=\"");
		result.append(daysFromNow(con, itm_appn_start_datetime));
		result.append("\" app_start_datetime=\"");
		result.append(itm_appn_start_datetime);
		result.append("\" app_end_days_to=\"");
		result.append(daysFromNow(con, itm_appn_end_datetime));
		result.append("\" app_end_datetime=\"");
		result.append(itm_appn_end_datetime);
		result.append("\" capacity=\"");
		result.append(itm_capacity);
		//result.append("\" capacity_status=\"");
		//result.append(capacity_status);
		result.append("\" pending_count=\"");
		result.append(pending_count);
		result.append("\" status=\"");
		result.append(dbUtils.esc4XML(itm_status));
		result.append("\" eff_start_days_to=\"");
		result.append(daysFromNow(con, itm_eff_start_datetime));
		result.append("\" eff_start_datetime=\"");
		result.append(itm_eff_start_datetime);
		result.append("\" eff_end_days_to=\"");
		result.append(daysFromNow(con, itm_eff_end_datetime));
		result.append("\" eff_end_datetime=\"");
		result.append(itm_eff_end_datetime);
		if (this.itm_run_ind) {
			aeItemRelation aeIre = new aeItemRelation();
			aeIre.ire_child_itm_id = this.itm_id;
			aeItem ireParentItm = aeIre.getParentInfo(con);
			if (ireParentItm != null) {
				result.append("\" parent_itm_id=\"").append(ireParentItm.itm_id);
			}
		}
		result.append("\">");
		result.append(dbUtils.NEWL);
		result.append("<title>");
		result.append(dbUtils.esc4XML(itm_title));
		result.append("</title>");
		result.append(dbUtils.NEWL);
		result.append("</item>");
		result.append(dbUtils.NEWL);

		return result.toString();
	}

	private long daysFromNow(Connection con, Timestamp time) throws qdbException {
		long days_to = 0;
		Timestamp cur_time = dbUtils.getTime(con);

		if (time != null) {
			if (time.after(cur_time)) {
				Calendar date1 = Calendar.getInstance();
				Calendar date2 = Calendar.getInstance();
				Date d1;
				Date d2;
				long mini1;
				long mini2;

				date1.setTime(cur_time);
				date1.set(Calendar.HOUR_OF_DAY, 0);
				date1.set(Calendar.MINUTE, 0);
				date1.set(Calendar.SECOND, 0);
				date1.set(Calendar.MILLISECOND, 0);
				d1 = date1.getTime();
				mini1 = d1.getTime();

				date2.setTime(time);
				date2.set(Calendar.HOUR_OF_DAY, 0);
				date2.set(Calendar.MINUTE, 0);
				date2.set(Calendar.SECOND, 0);
				date2.set(Calendar.MILLISECOND, 0);
				d2 = date2.getTime();
				mini2 = d2.getTime();

				days_to = (mini2 - mini1) / (24 * 60 * 60 * 1000);
			}
		}

		return days_to;
	}

	public static boolean hasApplied(Connection con, long itm_id, long ent_id) throws SQLException {
		return (getAppId(con, itm_id, ent_id, true) != 0);
	}

	private static final String sql_get_app_id_no_run = " SELECT app_id FROM aeApplication " + " WHERE app_itm_id = ? " + " AND app_ent_id = ? "
			+
			//        " AND app_status <> ? AND app_status <> ? " +
			" AND (Exists (select att_app_id from aeAttendance, aeAttendanceStatus where att_app_id = app_id AND att_itm_id = app_itm_id AND att_ats_id = ats_id and ats_type = ?) "
			+ " OR Not Exists (select att_app_id from aeAttendance where att_app_id = app_id AND att_itm_id = app_itm_id )) ";
	private static final String sql_get_app_id_has_run = " SELECT app_id FROM aeApplication, aeItemRelation " + " WHERE app_itm_id = ire_child_itm_id " + " AND ire_parent_itm_id = ? " + " AND app_ent_id = ?"
			+
			//        " AND app_status <> ? AND app_status <> ? " +
			" AND (Exists (select att_app_id from aeAttendance, aeAttendanceStatus where att_app_id = app_id AND att_itm_id = app_itm_id AND att_ats_id = ats_id and ats_type = ?) "
			+ " OR Not Exists (select att_app_id from aeAttendance where att_app_id = app_id AND att_itm_id = app_itm_id )) ";

	/*
	    checkStatus => filter the rejected and withdrawal record
	*/
	public static long getAppId(Connection con, long itm_id, long ent_id, boolean checkStatus) throws SQLException {
		return getAppId(con, itm_id, ent_id, checkStatus, 0);
	}

	public static long getAppId(Connection con, long itm_id, long ent_id, boolean checkStatus, long excludedItmId) throws SQLException {
		long result;
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		String SQL = (itm.getCreateRunInd(con)) ? sql_get_app_id_has_run : sql_get_app_id_no_run;
		if (checkStatus) {
			SQL += " AND app_status <> ? AND app_status <> ? ";
		}

		if (excludedItmId > 0) {
			SQL += " AND app_itm_id <> ? ";
		}

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL);
			int index = 1;
			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, ent_id);
			stmt.setString(index++, aeAttendanceStatus.STATUS_TYPE_PROGRESS);

			if (checkStatus) {
				stmt.setString(index++, REJECTED);
				stmt.setString(index++, WITHDRAWN);
			}

			if (excludedItmId > 0) {
				stmt.setLong(index++, excludedItmId);
			}
			rs = stmt.executeQuery();

			if (rs.next()) {
				result = rs.getLong("app_id");
			} else {
				result = 0;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}

	private static final String sql_get_all_app_id_no_run = " SELECT app_id FROM aeApplication " + " WHERE app_itm_id = ? " + " AND app_ent_id = ? ";
	private static final String sql_get_all_app_id_has_run = " SELECT app_id FROM aeApplication, aeItemRelation " + " WHERE app_itm_id = ire_child_itm_id " + " AND ire_parent_itm_id = ? " + " AND app_ent_id = ?";

	// get all app_id                
	public static Vector getAllAppId(Connection con, long itm_id, long ent_id) throws SQLException {
		Vector vtAppLst = new Vector();
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		String SQL = (itm.getCreateRunInd(con)) ? sql_get_all_app_id_has_run : sql_get_all_app_id_no_run;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL);
			stmt.setLong(1, itm_id);
			stmt.setLong(2, ent_id);

			rs = stmt.executeQuery();

			while (rs.next()) {
				vtAppLst.addElement(new Long(rs.getLong("app_id")));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (rs != null)
				rs.close();
		}
		return vtAppLst;
	}

	// get the maximum id
	private long getMaxAppID(Connection con) throws SQLException {
		long id;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT MAX(app_id) FROM aeApplication");
		if (rs.next())
			id = rs.getLong(1);
		else
			id = 0;
		stmt.close();
		return id;
	}

	//update ji status of the users
	public static void updNotifyStatus(Connection con, long itm_id, long[] ent_ids, int status, Timestamp datetime) throws SQLException, cwException {

		StringBuffer entStr = new StringBuffer().append("(0");
		for (int i = 0; i < ent_ids.length; i++)
			entStr.append(COMMA).append(ent_ids[i]);
		entStr.append(")");

		String aeApplication_UPDATE_NOTIFY_STATUS = " UPDATE aeApplication SET app_notify_status = ? , app_notify_datetime = ? " + " WHERE app_itm_id = ? " + " AND app_ent_id IN " + entStr.toString();

		PreparedStatement stmt = con.prepareStatement(aeApplication_UPDATE_NOTIFY_STATUS);
		stmt.setInt(1, status);
		if (status == 2)
			stmt.setTimestamp(2, datetime);
		else
			stmt.setNull(2, java.sql.Types.TIMESTAMP);
		stmt.setLong(3, itm_id);
		stmt.executeUpdate();
		stmt.close();

		return;

	}

	private static final String sql_get_item_enrolled_user = "SELECT app_ent_id FROM aeApplication WHERE app_itm_id = ? ";

	public static long[] getEnrollUser(Connection con, long itm_id, String[] status) throws SQLException, cwException {
		StringBuffer status_cond = new StringBuffer();
		if (status != null && status.length > 0) {
			status_cond.append(" AND app_status IN ( ? ");
			for (int i = 1; i < status.length; i++) {
				status_cond.append(" ,? ");
			}
			status_cond.append(" ) ");
		}
		StringBuffer sql_get_user = new StringBuffer(sql_get_item_enrolled_user);
		sql_get_user.append(status_cond.toString());

		int index = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector tempResult = null;
		try {
			index = 1;
			stmt = con.prepareStatement(sql_get_user.toString());
			stmt.setLong(index++, itm_id);
			if (status != null && status.length > 0) {
				for (int i = 0; i < status.length; i++) {
					stmt.setString(index++, status[i]);
				}
			}

			tempResult = new Vector();
			rs = stmt.executeQuery();
			while (rs.next()) {
				tempResult.addElement(new Long(rs.getLong("app_ent_id")));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		long[] result = cwUtils.vec2longArray(tempResult);

		return result;
	}

	public static long[] getAdmittedUserList(Connection con, long itm_id) throws SQLException {
		ViewAppnUser[] appnUser = getAdmittedUser(con, itm_id, -1);
		long[] entId = new long[appnUser.length];
		for (int i = 0; i < entId.length; i++) {
			entId[i] = appnUser[i].usr_ent_id;
		}
		return entId;
	}

	/**
	Get notification status of the enrolled user
	@param item id
	@param notification status 0:not send notification, 1:send by system, 2:send manually, -1:all enrolled users
	@return array of result set
	*/
	public static ViewAppnUser[] getAdmittedUser(Connection con, long itm_id, int notify_status) throws SQLException {
		String SQL = " SELECT usr_id, usr_ent_id, usr_display_bil, app_notify_status, app_notify_datetime " + " FROM aeApplication, RegUser " + " WHERE app_ent_id = usr_ent_id " + " AND app_itm_id = ? "
				+ " AND app_status = ? ";
		if (notify_status != -1)
			SQL += " AND app_notify_status = ? ";

		int idx = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector tempResult = null;
		try {
			idx = 1;
			stmt = con.prepareStatement(SQL);
			stmt.setLong(idx++, itm_id);
			stmt.setString(idx++, "Admitted");
			if (notify_status != -1)
				stmt.setLong(idx++, notify_status);
			rs = stmt.executeQuery();

			tempResult = new Vector();
			while (rs.next()) {
				idx = 1;
				ViewAppnUser appnUser = new ViewAppnUser();
				appnUser.usr_id = rs.getString(idx++);
				appnUser.usr_ent_id = rs.getLong(idx++);
				appnUser.usr_display_bil = rs.getString(idx++);
				appnUser.app_notify_status = rs.getInt(idx++);
				appnUser.app_notify_datetime = rs.getTimestamp(idx++);
				tempResult.addElement(appnUser);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		ViewAppnUser result[] = new ViewAppnUser[tempResult.size()];
		result = (ViewAppnUser[]) tempResult.toArray(result);

		return result;
	}

	public ViewAppnUser[] getEnrolledUser(Connection con, long itm_id, String process_status, String order_by, String sort_by) throws SQLException {
		String SQL = " SELECT usr_id, usr_ent_id, usr_display_bil, app_notify_status, app_notify_datetime " + " FROM aeApplication, RegUser " + " WHERE app_ent_id = usr_ent_id " + " AND app_itm_id = ? ";
		if (process_status != null && process_status.length() > 0)
			SQL += " AND app_process_status = ? ";
		if (order_by != null && order_by.length() > 0) {
			SQL += " ORDER BY " + order_by;
			if (sort_by != null && sort_by.length() > 0)
				SQL += " " + sort_by;
			else
				SQL += " ASC";
		} else {
			SQL += " ORDER BY usr_display_bil ASC ";
		}

		int idx = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Vector tempResult = null;
		try {
			stmt = con.prepareStatement(SQL);
			stmt.setLong(1, itm_id);
			if (process_status != null && process_status.length() > 0)
				stmt.setString(2, process_status);
			rs = stmt.executeQuery();

			tempResult = new Vector();
			while (rs.next()) {
				idx = 1;
				ViewAppnUser appnUser = new ViewAppnUser();
				appnUser.usr_id = rs.getString(idx++);
				appnUser.usr_ent_id = rs.getLong(idx++);
				appnUser.usr_display_bil = rs.getString(idx++);
				appnUser.app_notify_status = rs.getInt(idx++);
				appnUser.app_notify_datetime = rs.getTimestamp(idx++);
				tempResult.addElement(appnUser);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		ViewAppnUser result[] = new ViewAppnUser[tempResult.size()];
		result = (ViewAppnUser[]) tempResult.toArray(result);

		return result;
	}

	/**
	Get user status of the items
	@param vector of items id
	@param user entity id
	@return hashtable ( key:item id, value:application status)
	*/
	public Hashtable getUserItemStatus(Connection con, Vector itmIdVec, long usr_ent_id) throws SQLException {

		String sql_get_user_item_status = " SELECT app_itm_id, app_status FROM aeApplication " + " WHERE app_ent_id = ? AND app_itm_id IN " + cwUtils.vector2list(itmIdVec);

		PreparedStatement stmt = con.prepareStatement(sql_get_user_item_status);
		stmt.setLong(1, usr_ent_id);
		ResultSet rs = stmt.executeQuery();
		Hashtable itmStatus = new Hashtable();
		while (rs.next()) {

			itmStatus.put(new Long(rs.getLong("app_itm_id")), rs.getString("app_status"));

		}

		stmt.close();
		return itmStatus;

	}

	/**
	 Get user status of the items
	 @param long items id
	 @param user entity id
	 @return String of the status
	 */
	public static String getUserItemStatus(Connection con, long itm_id, long usr_ent_id) throws SQLException {

		String sql_get_user_item_status = " SELECT app_itm_id, app_status FROM aeApplication " + " WHERE app_ent_id = ? AND app_itm_id = ? ";

		PreparedStatement stmt = con.prepareStatement(sql_get_user_item_status);
		stmt.setLong(1, usr_ent_id);
		stmt.setLong(2, itm_id);
		ResultSet rs = stmt.executeQuery();
		String status = null;
		if (rs.next()) {
			status = rs.getString("app_status");
		}
		stmt.close();
		return status;

	}

	private static final String sql_get_appn_cnt = "SELECT COUNT(*) AS CNT FROM aeApplication WHERE app_itm_id = ? and app_status not in (?, ?) ";
	private static final String sql_get_appn_att_cnt = "SELECT COUNT(*) AS CNT FROM aeApplication, aeAttendance WHERE app_itm_id = ? AND att_app_id = app_id AND att_itm_id = app_itm_id ";

	/**
	Count the application records of the input item
	@return application count of the item
	*/
	public static int countItemAppn(Connection con, long itm_id, boolean attendance_cnt_ind) throws SQLException, cwException {
		PreparedStatement stmt;
		ResultSet rs;
		int count = 0;

		if (itm_id == 0) {
			throw new cwException("com.cw.wizbank.ae.aeApplication.countItemAppn: itm_id = 0");
		}
		String SQL = (attendance_cnt_ind) ? sql_get_appn_att_cnt : sql_get_appn_cnt;
		stmt = con.prepareStatement(SQL);
		stmt.setLong(1, itm_id);

		if (!attendance_cnt_ind) {
			stmt.setString(2, REJECTED);
			stmt.setString(3, WITHDRAWN);
		}

		rs = stmt.executeQuery();

		if (rs.next()) {
			count = rs.getInt("CNT");
		}
		stmt.close();
		return count;
	}
	
	/**
	 * 获取该课程已报名与正在审批的人数
	 * @param con
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 * @throws cwException
	 */
	public static int countItemAppnAndPend(Connection con, long itm_id) throws SQLException, cwException {
		PreparedStatement stmt;
		ResultSet rs;
		int count = 0;
		
		if (itm_id == 0) {
			throw new cwException("com.cw.wizbank.ae.aeApplication.countItemAppn: itm_id = 0");
		}
		String SQL = "SELECT COUNT(*) as 'CNT' FROM aeApplication WHERE app_itm_id = ? and (app_process_status = ? or app_process_status = ?)";
		stmt = con.prepareStatement(SQL);
		int index = 1;
		stmt.setLong(index++, itm_id);
		stmt.setString(index++, "Enrolled");
		stmt.setString(index++, "Pending Approval");
		
		rs = stmt.executeQuery();
		
		if (rs.next()) {
			count = rs.getInt("CNT");
		}
		stmt.close();
		return count;
	}
	
	/**
    查询该课程处于审批状态的人数
    @return application count of the item
    */
	public static int countItemAppPending(Connection con, long itm_id) throws SQLException, cwException {
	        PreparedStatement stmt;
	        ResultSet rs;
	        int count = 0;

	        if (itm_id == 0) {
	            throw new cwException("com.cw.wizbank.ae.aeApplication.countItemAppn: itm_id = 0");
	        }
	        String SQL ="SELECT COUNT(*) AS CNT  FROM aeApplication where  app_itm_id =? AND app_status = 'Pending' ";
	        stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, itm_id);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            count = rs.getInt("CNT");
	        }
	        stmt.close();
	        return count;
	 }

	   
	   
	private static final String sql_get_latest_appn_att = "SELECT max(app_id) AS app_id FROM aeApplication WHERE app_itm_id = ? AND app_id in " + " (select att_app_id from aeAttendance where att_itm_id = app_itm_id ) "
			+ " group by app_ent_id ";

	public static Vector getLatestItmAppnLstWAtt(Connection con, long itm_id) throws SQLException, cwException {
		Vector vtAppIdLst = new Vector();
		if (itm_id == 0) {
			throw new cwException("com.cw.wizbank.ae.aeApplication.countItemAppn: itm_id = 0");
		}
		PreparedStatement stmt = con.prepareStatement(sql_get_latest_appn_att);
		stmt.setLong(1, itm_id);

		ResultSet rs = stmt.executeQuery();
		long appId;
		while (rs.next()) {
			vtAppIdLst.addElement(new Long(rs.getLong("app_id")));
		}
		rs.close();
		stmt.close();
		return vtAppIdLst;
	}

	public String getItemApplyView(Connection con, String tvw_id, qdbEnv inEnv) throws SQLException, cwException, cwSysMessage {

		StringBuffer xmlBuf = new StringBuffer(1024);
		aeItem itm = new aeItem();
		itm.itm_id = this.app_itm_id;
		itm.get(con);

		if (itm.itm_run_ind) {
			aeItem parent = new aeItem();
			aeItemRelation ire = new aeItemRelation();
			ire.ire_child_itm_id = itm.itm_id;
			parent.itm_id = ire.getParentItemId(con);
			parent.get(con);
			xmlBuf.append(parent.ItemDetailAsXML(con, inEnv, false, false, tvw_id, false, false, 0, 0, false, false, false, null, null, false));
		}
		xmlBuf.append(itm.ItemDetailAsXML(con, inEnv, false, false, tvw_id, false, false, 0, 0, false, false, false, null, null, false));
		return xmlBuf.toString();
	}

	private static final String sql_get_app_by_itm = " Select app_id, app_ent_id from aeApplication where app_itm_id = ? ";

	public static void delByItem(Connection con, long itm_id) throws SQLException, cwException {

		PreparedStatement stmt = con.prepareStatement(sql_get_app_by_itm);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		aeApplication app = new aeApplication();
		while (rs.next()) {
			app.app_id = rs.getLong("app_id");
			app.app_ent_id = rs.getLong("app_ent_id");
			app.app_itm_id = itm_id;
			app.del(con);
			/*
			aeAppnCommHistory.delByAppn(con, app.app_id);
			aeAppnActnHistory.delByAppn(con, app.app_id);
			aeAttendance.delByAppn(con, app.app_id);
			DbAppnEnrolRelation.delByAppn(con, app.app_id);
			app.delAppn(con);
			*/
		}
		stmt.close();
		return;
	}

	private static final String sql_get_admitted_app_by_itm = " Select app_id from aeApplication where app_itm_id = ? " + " And app_status = ? ";

	public static void delAdmittedAppnByItem(Connection con, long itm_id) throws SQLException, cwException {

		PreparedStatement stmt = con.prepareStatement(sql_get_app_by_itm);
		stmt.setLong(1, itm_id);
		stmt.setString(2, ADMITTED);
		ResultSet rs = stmt.executeQuery();
		aeApplication app = new aeApplication();
		while (rs.next()) {
			app.app_id = rs.getLong("app_id");
			app.app_ent_id = rs.getLong("app_ent_id");
			app.app_itm_id = itm_id;
			app.del(con);
			/*
			aeAppnCommHistory.delByAppn(con, app.app_id);
			aeAppnActnHistory.delByAppn(con, app.app_id);
			aeAttendance.delByAppn(con, app.app_id);
			//no need to delete aeAppnEnrolRelation as there should be no aeAppnEnrolRelation records before calling this method
			//DbAppnEnrolRelation.delByAppn(con, this.app_id);
			app.delAppn(con);
			*/
		}
		stmt.close();
		return;
	}

	public void del(Connection con, Timestamp upd_timestamp) throws SQLException, cwSysMessage, cwException {
		try {
			getWithItem(con);
		} catch (qdbException e) {
			throw new cwException(e.getMessage());
		}
		if (!checkUpdTimestamp(upd_timestamp)) {
			throw new cwSysMessage("AEQM02");
		}
		del(con);
		/*
		aeAppnCommHistory.delByAppn(con, this.app_id);
		aeAppnActnHistory.delByAppn(con, this.app_id);
		aeAttendance.delByAppn(con, this.app_id);
		DbAppnEnrolRelation.delByAppn(con, this.app_id);
		delAppn(con);
		*/
		return;
	}

	//need to set app_id
	public void del(Connection con) throws SQLException, cwException {
		try {
			if (itm_owner_ent_id == 0) {
				getWithItem(con);
			}
		} catch (qdbException e) {
			throw new cwException(e.getMessage());
		}
		//delete appn comment history
		aeAppnCommHistory.delByAppn(con, this.app_id);
		//del AppnApprovelList
		DbAppnApprovalList.delAppnApprovalList(con, this.app_id);
		//delete appn action history
		aeAppnActnHistory.delByAppn(con, this.app_id);
		//delete appn attendance
		aeAttendance.delByAppn(con, this.app_id);
		//if the underlying course has no application attached to it
		//remove resource permission
		aeItem itm = new aeItem();
		itm.itm_id = app_itm_id;

		dbCourse cos = new dbCourse();
		cos.cos_res_id = itm.getResId(con);
		cos.tkh_id = app_tkh_id;
		
		cos.unenroll(con, app_ent_id, itm_owner_ent_id);
		//del appn target entity
		DbAppnTargetEntity dbAte = new DbAppnTargetEntity();
		dbAte.ate_app_id = this.app_id;
		dbAte.delByAppId(con);

		//del Appn
		delAppn(con);
	}

	private static final String sql_del_app = " Delete From aeApplication Where app_id = ? ";

	private void delAppn(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(sql_del_app);
		stmt.setLong(1, this.app_id);
		stmt.executeUpdate();
		stmt.close();
		return;
	}

	/**
	Check if item capcaity exceeds
	includeCurApp:
		true - current application record is inserted already
		false - current application record is NOT YET inserted into db
	@return true item capacity exceeds
	*/
	public boolean isItemCapacityExceed(Connection con, String[] process_status_lst, boolean includeCurApp) throws SQLException, cwException {

		boolean result;
		int count;
		if (itm_capacity > 0) {
			try {
				if (aeApplication.countQueue(con, itm_id, WAITING) > 0) {
					result = true;
				} else {
					count = aeApplication.countProcessStatus(con, itm_id, process_status_lst);
					if (includeCurApp) {
						result = (count > itm_capacity);
					} else {
						result = (count >= itm_capacity);
					}
				}
			} catch (qdbException e) {
				throw new cwException(e.getMessage());
			}
		} else {
			result = false;
		}
		return result;
	}

	/**
	Update app_itm_id of this application<BR>
	Pre-define variable:
	<ul>
	<li>app_id
	</ul>
	*/
	public void updAppnItemId(Connection con, long itm_id, Timestamp upd_timestamp) throws SQLException, cwException, cwSysMessage {
		try {
			get(con);
		} catch (qdbException e) {
			throw new cwException(e.getMessage());
		}
		if (!checkUpdTimestamp(upd_timestamp)) {
			throw new cwSysMessage("AEQM02");
		}
		updAppnItemId(con, itm_id);
		return;
	}

	private static final String sql_upd_app_itm_id = " Update aeApplication set app_itm_id = ? " + " And app_id = ? ";

	/**
	Update app_itm_id of this application without checking timestamp<BR>
	Pre-define variable:
	<ul>
	<li>app_id
	</ul>
	*/
	private void updAppnItemId(Connection con, long itm_id) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(sql_upd_app_itm_id);
		stmt.setLong(1, itm_id);
		stmt.setLong(2, this.app_id);
		stmt.executeUpdate();
		stmt.close();
		return;
	}

	private static final String has_pending_appn_by_itm_no_run = " Select * from aeApplication Where app_itm_id = ? " + " And (app_status = ? OR app_status = ?) ";
	private static final String has_pending_appn_by_itm_has_run = " Select * from aeApplication, aeItemRelation " + " Where ire_parent_itm_id = ? And app_itm_id = ire_child_itm_id "
			+ " And (app_status = ? OR app_status = ?) ";

	/**
	Check if input item has any pending application <BR>
	i.e. app_status = "PENDING" or app_status = "WAITING" <BR>
	If create_run_ind is true, see if the input item's runs have pending appns
	rather than the input item itself
	*/
	public static boolean hasPendingAppnByItem(Connection con, long itm_id, boolean create_run_ind) throws SQLException {
		boolean result;
		String SQL = (create_run_ind) ? has_pending_appn_by_itm_has_run : has_pending_appn_by_itm_no_run;
		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setLong(1, itm_id);
		stmt.setString(2, PENDING);
		stmt.setString(3, WAITING);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			result = true;
		} else {
			result = false;
		}
		stmt.close();
		return result;
	}

	private static final String sql_is_usr_applied_itm = " Select count(app_id) From aeApplication Where app_itm_id = ? And app_ent_id = ? ";

	/**
	Check if the input user has application record on the input item
	*/
	public static boolean isExist(Connection con, long itm_id, long usr_ent_id) throws SQLException {
		boolean isExist;
		PreparedStatement stmt = con.prepareStatement(sql_is_usr_applied_itm);
		stmt.setLong(1, itm_id);
		stmt.setLong(2, usr_ent_id);
		ResultSet rs = stmt.executeQuery();
		long cnt = 0;
		if (rs.next()) {
			cnt = rs.getLong(1);
		}
		stmt.close();
		return (cnt > 0);
	}

	public static Hashtable hasApplicationBefore(Connection con, long itm_id, boolean run_ind, Vector ent_vec, String queue_status) throws SQLException {
		Hashtable hash = new Hashtable();
		StringBuffer sql = new StringBuffer();

		if (ent_vec != null && ent_vec.size() != 0) {
			if (run_ind) {
				sql.append(" select app_ent_id, app_id from aeItem item1, aeItem item2, aeItemRelation, aeApplication where ");
				sql.append(" item1.itm_id in (select item3.itm_id from aeItem item1, aeItem item2, aeItem item3, aeItemRelation where item1.itm_id = ? and ire_child_itm_id = item1.itm_id and ");
				sql.append(" item2.itm_id = ire_parent_itm_id and item2.itm_code = item3.itm_code) ");
				sql.append(" and item1.itm_id = ire_parent_itm_id and ire_child_itm_id = item2.itm_id and ");
				sql.append(" item2.itm_id = app_itm_id and app_ent_id in ").append(cwUtils.vector2list(ent_vec));

				if (queue_status != null) {
					sql.append(" and app_status = ? ");
				}

				//            sql.append(" group by app_ent_id ");
			} else {
				sql.append(" select app_ent_id, app_id from aeItem item1, aeItem item2, aeApplication where item1.itm_id = ? and item1.itm_code = item2.itm_code and ");
				sql.append(" app_itm_id = item2.itm_id and app_ent_id in ").append(cwUtils.vector2list(ent_vec));

				if (queue_status != null) {
					sql.append(" and app_status = ? ");
				}

				//            sql.append(" group by app_ent_id ");
			}

			PreparedStatement stmt = con.prepareStatement(sql.toString());

			int index = 1;
			stmt.setLong(index++, itm_id);

			if (queue_status != null) {
				stmt.setString(index++, queue_status);
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Long ent_id = new Long(rs.getLong("app_ent_id"));
				Long app_id = new Long(rs.getLong("app_id"));

				Vector vec = (Vector) hash.get(ent_id);

				if (vec == null) {
					vec = new Vector();
				}

				vec.addElement(app_id);
				hash.put(ent_id, vec);
			}

			stmt.close();
		}

		return hash;
	}

	/*
	*   for delete user.
	*   delete all pending, waiting application
	*/
	public static void delAllApp(Connection con, long ent_id) throws SQLException, cwException {
		String sql = "SELECT app_id , app_itm_id FROM aeApplication where app_ent_id = ? AND app_status in (?, ?)";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, ent_id);
			stmt.setString(2, PENDING);
			stmt.setString(3, WAITING);

			rs = stmt.executeQuery();

			while (rs.next()) {
				aeApplication app = new aeApplication();
				app.app_ent_id = ent_id;
				app.app_id = rs.getLong("app_id");
				app.app_itm_id = rs.getLong("app_itm_id");
				app.del(con);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static final String sql_get_itm_appn_count_by_status = " Select count(*) as cnt, app_status " + " From aeApplication " + " Where app_itm_id = ? " + " Group by app_status ";

	static String getAppnCountByItemAsXML(Connection con, long itm_id) throws SQLException {

		StringBuffer xmlBuf = new StringBuffer(512);
		PreparedStatement stmt = con.prepareStatement(sql_get_itm_appn_count_by_status);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		boolean b_pending = false;
		boolean b_admitted = false;
		boolean b_waiting = false;
		boolean b_rejected = false;
		boolean b_withdrawn = false;
		xmlBuf.append("<appn_count_list>");
		while (rs.next()) {
			String app_status = rs.getString("app_status");
			long count = rs.getLong("cnt");
			if (app_status.equals(PENDING)) {
				b_pending = true;
			} else if (app_status.equals(ADMITTED)) {
				b_admitted = true;
			} else if (app_status.equals(WAITING)) {
				b_waiting = true;
			} else if (app_status.equals(REJECTED)) {
				b_rejected = true;
			} else if (app_status.equals(WITHDRAWN)) {
				b_withdrawn = true;
			}
			xmlBuf.append("<appn_count status=\"").append(app_status).append("\"");
			xmlBuf.append(" count=\"").append(count).append("\"/>");
		}

		stmt.close();
		if (b_pending == false) {
			xmlBuf.append("<appn_count status=\"").append(PENDING).append("\"");
			xmlBuf.append(" count=\"").append(0).append("\"/>");
		}
		if (b_admitted == false) {
			xmlBuf.append("<appn_count status=\"").append(ADMITTED).append("\"");
			xmlBuf.append(" count=\"").append(0).append("\"/>");
		}
		if (b_waiting == false) {
			xmlBuf.append("<appn_count status=\"").append(WAITING).append("\"");
			xmlBuf.append(" count=\"").append(0).append("\"/>");
		}
		if (b_rejected == false) {
			xmlBuf.append("<appn_count status=\"").append(REJECTED).append("\"");
			xmlBuf.append(" count=\"").append(0).append("\"/>");
		}
		if (b_withdrawn == false) {
			xmlBuf.append("<appn_count status=\"").append(WITHDRAWN).append("\"");
			xmlBuf.append(" count=\"").append(0).append("\"/>");
		}
		xmlBuf.append("</appn_count_list>");
		return xmlBuf.toString();
	}

	/* Check if the user has been completed (attendance status is either attended, incomplete, or no-show) the course (or the run, the old version)
	   input: usr_ent_id
	          itm_id = course level itm_id */
	public static boolean hasCompletedCourse(Connection con, long usr_ent_id, long itm_id) throws SQLException {
		boolean completed = false;
		PreparedStatement stmt = con
				.prepareStatement("select app_id from aeApplication, aeAttendance, aeAttendanceStatus where app_ent_id = ? AND app_id = att_app_id AND att_itm_id = app_itm_id AND att_ats_id = ats_id and ats_type <> 'Progress' and (app_itm_id in (select item2.itm_id from aeItem as item1, aeItem as item2 where item1.itm_id = ? and item1.itm_code = item2.itm_code) or app_itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id in (select item2.itm_id from aeItem as item1, aeItem as item2 where item1.itm_id = ? and item1.itm_code = item2.itm_code)))");
		stmt.setLong(1, usr_ent_id);
		stmt.setLong(2, itm_id);
		stmt.setLong(3, itm_id);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			completed = true;
		}

		stmt.close();

		return completed;
	}

	public static boolean isItemAppnExist(Connection con, long itm_id, String app_status) throws SQLException {

		String SQL = " Select COUNT(app_ent_id) " + " From aeApplication " + " Where (app_itm_id  = ? " + " Or app_itm_id In ( " + " Select ire_child_itm_id " + " From aeItemRelation "
				+ " Where ire_parent_itm_id = ? )) " + " And app_status = ? ";
		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setLong(1, itm_id);
		stmt.setLong(2, itm_id);
		stmt.setString(3, app_status);
		ResultSet rs = stmt.executeQuery();
		long count = 0;
		if (rs.next())
			count = rs.getLong(1);
		stmt.close();
		if (count > 0)
			return true;
		else
			return false;
	}

	public Vector getAppn(Connection con, Vector v_itm_id, String[] appn_status) throws SQLException, cwException {
		Vector vec = new Vector();

		if (v_itm_id == null || v_itm_id.isEmpty())
			return vec;

		String SQL = " SELECT app1.app_id, app1.app_upd_timestamp  " + " FROM aeApplication app1" + " WHERE app1.app_itm_id IN " + cwUtils.vector2list(v_itm_id);

		if (appn_status != null && appn_status.length > 0) {
			SQL += " AND app1.app_process_status IN ( ? ";
			for (int i = 1; i < appn_status.length; i++)
				SQL += ", ? ";
			SQL += " ) ";
		}
		SQL += " AND app1.app_create_timestamp = ( " + " SELECT MAX(app2.app_create_timestamp) " + " FROM aeApplication app2 " + " WHERE app2.app_itm_id = app1.app_itm_id " + " AND app2.app_ent_id = app1.app_ent_id "
				+ " GROUP BY app2.app_ent_id, app2.app_itm_id ) ";

		PreparedStatement stmt = con.prepareStatement(SQL);
		int index = 1;
		if (appn_status != null && appn_status.length > 0) {
			for (int i = 0; i < appn_status.length; i++)
				stmt.setString(index++, appn_status[i]);
		}
		ResultSet rs = stmt.executeQuery();
		aeApplication app = null;
		while (rs.next()) {
			app = new aeApplication();
			app.app_id = rs.getLong("app_id");
			app.app_upd_timestamp = rs.getTimestamp("app_upd_timestamp");
			vec.addElement(app);
		}
		stmt.close();
		return vec;
	}

	public Vector getAppnEntId(Connection con, Vector v_itm_id, String[] appn_status) throws SQLException, cwException {
		Vector vec = new Vector();

		if (v_itm_id == null || v_itm_id.isEmpty())
			return vec;

		String SQL = " SELECT app_ent_id, MAX(app_create_timestamp) " + " FROM aeApplication " + " WHERE app_itm_id IN " + cwUtils.vector2list(v_itm_id);

		if (appn_status != null && appn_status.length > 0) {
			SQL += " AND app_process_status IN ( ? ";
			for (int i = 1; i < appn_status.length; i++)
				SQL += ", ? ";
			SQL += " ) ";
		}
		SQL += " Group by app_ent_id, app_itm_id ";

		PreparedStatement stmt = con.prepareStatement(SQL);
		int index = 1;
		if (appn_status != null && appn_status.length > 0) {
			for (int i = 0; i < appn_status.length; i++)
				stmt.setString(index++, appn_status[i]);
		}
		ResultSet rs = stmt.executeQuery();
		while (rs.next())
			vec.addElement(new Long(rs.getLong("app_ent_id")));
		stmt.close();
		return vec;
	}

	public static String getAppnListAsXML(Connection con, Vector v_usr_ent_id, Vector v_itm_id, String[] appn_status) throws SQLException, cwException {

		StringBuffer data = new StringBuffer();
		String SQL = " SELECT itm_id, itm_title, COUNT(app1.app_id), app_process_status " + " FROM aeItem, aeApplication app1" + " WHERE app1.app_itm_id = itm_id " + " AND app1.app_ent_id IN "
				+ cwUtils.vector2list(v_usr_ent_id) + " AND app1.app_itm_id IN " + cwUtils.vector2list(v_itm_id);
		if (appn_status != null && appn_status.length > 0) {
			SQL += " AND app1.app_process_status IN ( ? ";
			for (int i = 1; i < appn_status.length; i++)
				SQL += " , ? ";
			SQL += " ) ";
		}
		SQL += " AND app1.app_create_timestamp = ( " + " SELECT MAX(app2.app_create_timestamp) " + " FROM aeApplication app2 " + " WHERE app2.app_itm_id = app1.app_itm_id " + " AND app2.app_ent_id = app1.app_ent_id "
				+ " GROUP BY app2.app_ent_id, app2.app_itm_id ) " + " GROUP BY itm_id, itm_title, app_process_status ";

		PreparedStatement stmt = con.prepareStatement(SQL);
		int index = 1;
		if (appn_status != null && appn_status.length > 0) {
			for (int i = 0; i < appn_status.length; i++)
				stmt.setString(index++, appn_status[i]);
		}

		int[] status_count = new int[appn_status.length];
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			for (int i = 0; i < appn_status.length; i++) {
				if (appn_status[i].equalsIgnoreCase(rs.getString("app_process_status"))) {
					status_count[i] += rs.getInt(3);
				}
			}

		}
		stmt.close();
		int count = 0;
		//data.append("<application_status_list>");
		for (int i = 0; i < appn_status.length; i++) {
			data.append("<status value=\"").append(appn_status[i]).append("\"").append(">").append(status_count[i]).append("</status>");
			count += status_count[i];
		}
		//data.append("</application_status_list>");

		StringBuffer meta = new StringBuffer();
		meta.append("<application_status_list total=\"").append(count).append("\">").append(data).append("</application_status_list>");
		return meta.toString();

	}

	//update app_usr_prof_xml
	public void updateProf(Connection con, long app_id, long usr_ent_id, loginProfile prof, Timestamp app_upd_timestamp) throws SQLException, cwSysMessage, cwException, qdbException {
		//update app_usr_prof_xml(Clob) first
		String condition = "app_id = " + app_id;
		// construct the column & value
		String[] columnName = new String[1];
		String[] columnValue = new String[1];
		columnName[0] = "app_usr_prof_xml";
		//dbRegUser.getUserXML as user profile
		dbRegUser user = new dbRegUser();
		user.usr_ent_id = usr_ent_id;
		user.get(con);
		columnValue[0] = user.getUserXML(con, prof);
		if (cwSQL.updateClobFields(con, "aeApplication", columnName, columnValue, condition) < 1)
			throw new cwSysMessage("USG001");
	}

	public static long[] getUserEntIdList(Connection con, long[] app_id) throws SQLException, cwException {

		long[] usr_ent_id_lst = new long[app_id.length];
		String SQL = " SELECT app_ent_id FROM aeApplication WHERE app_id IN " + cwUtils.array2list(app_id);

		PreparedStatement stmt = con.prepareStatement(SQL);
		ResultSet rs = stmt.executeQuery();
		int index = 0;
		while (rs.next()) {
			usr_ent_id_lst[index++] = rs.getLong("app_ent_id");
		}
		stmt.close();
		return usr_ent_id_lst;
	}

	public long getCreateUsrEntId(Connection con) throws SQLException {
		long usr_ent_id = 0;
		String SQL = " SELECT usr_ent_id FROM aeApplication,RegUser WHERE app_id = ? and app_create_usr_id = usr_id ";

		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setLong(1, app_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			usr_ent_id = rs.getLong("usr_ent_id");
		}
		rs.close();
		stmt.close();
		return usr_ent_id;
	}

	public static long getTkhId(Connection con, long app_id) throws SQLException {
		String SQL = " SELECT app_tkh_id FROM aeApplication WHERE app_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL);
			stmt.setLong(1, app_id);

			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			} else {
				throw new SQLException("invalid app_id in aeApplication.getTkhId, app_id = " + app_id);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public static void updTkhId(Connection con, long app_id, long app_tkh_id) throws SQLException {
		String SQL = " UPDATE aeApplication SET app_tkh_id = ? WHERE app_id = ? ";
		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setLong(1, app_tkh_id);
		stmt.setLong(2, app_id);

		if (stmt.executeUpdate() != 1) {
			stmt.close();
			throw new SQLException("com.cw.wizbank.ae.aeApplication.updTkhId: Fail to update application tkh_id from DB");
		}

		stmt.close();
	}

	private static final String sql_get_app_id_no_run_by_itm_id = " SELECT app_id, app_ent_id FROM aeApplication " + " WHERE app_itm_id = ? "
			+ " AND (Exists (select att_app_id from aeAttendance, aeAttendanceStatus where att_app_id = app_id AND att_itm_id = app_itm_id AND att_ats_id = ats_id and ats_type = ?) " +
			//" OR Not Exists (select att_app_id from aeAttendance where att_app_id = app_id AND att_itm_id = app_itm_id ) " + 
			" ) ";
	private static final String sql_get_app_id_has_run_by_itm_id = " SELECT app_id, app_ent_id FROM aeApplication, aeItemRelation " + " WHERE app_itm_id = ire_child_itm_id " + " AND ire_parent_itm_id = ? "
			+ " AND (Exists (select att_app_id from aeAttendance, aeAttendanceStatus where att_app_id = app_id AND att_itm_id = app_itm_id AND att_ats_id = ats_id and ats_type = ?) " +
			//" OR Not Exists (select att_app_id from aeAttendance where att_app_id = app_id AND att_itm_id = app_itm_id ) " +
			" ) ";

	/* aeItemRequirement.java
	Get all the application that are related to the item
	@param itm_id id of the item
	*/
	public static Vector getApplicationByItmId(Connection con, long itm_id, String atsStatus) throws SQLException {
		Vector appVec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql_get_app_id_no_run_by_itm_id + " UNION " + sql_get_app_id_has_run_by_itm_id);
			stmt.setLong(1, itm_id);
			stmt.setString(2, atsStatus);
			stmt.setLong(3, itm_id);
			stmt.setString(4, atsStatus);
			rs = stmt.executeQuery();
			while (rs.next()) {
				aeApplication app = new aeApplication();
				app.app_id = rs.getLong("app_id");
				app.app_ent_id = rs.getLong("app_ent_id");

				appVec.addElement(app);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return appVec;
	}

	private static final String sql_get_app_id_by_cov_status = " SELECT app_id " + " FROM aeApplication, aeAttendance, aeAttendanceStatus " + " WHERE app_itm_id = ? " + " AND app_ent_id = ? "
			+ " AND att_app_id = app_id " + " AND ats_id = att_ats_id " + " AND ats_cov_status = ? ";

	//and att_timestamp <= getdate()

	public static Vector getAppIdByCovStatus(Connection con, long usr_ent_id, long itm_id, Timestamp att_timestamp, String cov_status) throws SQLException {
		return getAppIdByCovStatus(con, sql_get_app_id_by_cov_status, usr_ent_id, itm_id, att_timestamp, cov_status);
	}

	private static final String sql_get_run_app_id_by_cov_status = " SELECT app_id " + " FROM aeApplication, aeAttendance, aeAttendanceStatus, aeItemRelation " + " WHERE ire_parent_itm_id = ? "
			+ " AND app_itm_id = ire_child_itm_id " + " AND app_ent_id = ? " + " AND att_app_id = app_id " + " AND ats_id = att_ats_id " + " AND ats_cov_status = ? ";

	//and att_timestamp <= getdate()
	public static Vector getRunAppIdByCovStatus(Connection con, long usr_ent_id, long itm_id, Timestamp att_timestamp, String cov_status) throws SQLException {

		return getAppIdByCovStatus(con, sql_get_run_app_id_by_cov_status, usr_ent_id, itm_id, att_timestamp, cov_status);

	}

	private static Vector getAppIdByCovStatus(Connection con, String SQL, long usr_ent_id, long itm_id, Timestamp att_timestamp, String cov_status) throws SQLException {
		PreparedStatement stmt = null;
		Vector v = new Vector();
		try {
			StringBuffer SQLBuf = new StringBuffer(256);
			SQLBuf.append(SQL);
			if (att_timestamp != null) {
				SQLBuf.append(" AND att_timestamp <= ? ");
			}
			stmt = con.prepareStatement(SQLBuf.toString());
			stmt.setLong(1, itm_id);
			stmt.setLong(2, usr_ent_id);
			stmt.setString(3, cov_status);
			if (att_timestamp != null) {
				stmt.setTimestamp(4, att_timestamp);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				v.addElement(new Long(rs.getLong("app_id")));
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return v;
	}

	/*
	for ims export
	return hashtable, app_id as key, process_status as value
	*/
	public Hashtable getAppProcessStatus(Connection con, long itm_id, String[] app_process_status, Timestamp startTime, Timestamp endTime) throws SQLException {
		Hashtable htResultList = new Hashtable();
		StringBuffer SQL = new StringBuffer();
		SQL.append("SELECT usr_ste_usr_id, app_id, app_process_status FROM RegUser, aeApplication WHERE app_ent_id = usr_ent_id AND app_itm_id = ? and app_process_status in ").append(
				cwUtils.array2list(app_process_status));
		if (startTime != null)
			SQL.append(" AND app_upd_timestamp > ?  ");
		if (endTime != null)
			SQL.append(" AND app_upd_timestamp < ?  ");

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL.toString());
			int index = 1;
			stmt.setLong(index++, itm_id);
			if (startTime != null)
				stmt.setTimestamp(index++, startTime);
			if (endTime != null)
				stmt.setTimestamp(index++, endTime);

			rs = stmt.executeQuery();

			Hashtable ht = null;
			String usr_ste_usr_id;
			while (rs.next()) {
				ht = new Hashtable();
				usr_ste_usr_id = rs.getString("usr_ste_usr_id");
				if (usr_ste_usr_id != null) {
					ht.put("usr_ste_usr_id", usr_ste_usr_id);
					ht.put("app_process_status", rs.getString("app_process_status"));
					htResultList.put(new Long(rs.getLong("app_id")), ht);
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return htResultList;
	}

	public Hashtable getUpdTimestamp(Connection con, Vector v_app_id) throws SQLException {
		Hashtable htUpdTimestamp = new Hashtable();
		if (v_app_id.size() == 0) {
			return htUpdTimestamp;
		}
		String sql = "SELECT app_id, app_upd_timestamp FROM aeApplication WHERE app_id in " + cwUtils.vector2list(v_app_id);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				htUpdTimestamp.put(new Long(rs.getLong("app_id")), rs.getTimestamp("app_upd_timestamp"));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return htUpdTimestamp;
	}

	public static Hashtable getEntIds(Connection con, long[] app_ids) throws SQLException {
		Hashtable htEntId = new Hashtable();
		if (app_ids == null || app_ids.length < 1) {
			return htEntId;
		}

		String sql = "SELECT app_id, app_ent_id FROM aeApplication WHERE app_id in " + cwUtils.array2list(app_ids);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				htEntId.put(new Long(rs.getLong("app_id")), new Long(rs.getLong("app_ent_id")));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return htEntId;
	}

	private static final String sql_get_latest_app_id_no_run = " SELECT max(app_id) as app_id FROM aeApplication " + " WHERE app_itm_id = ? " + " AND app_ent_id = ? ";
	private static final String sql_get_latest_app_id_has_run = " SELECT max(app_id) as app_id FROM aeApplication, aeItemRelation " + " WHERE app_itm_id = ire_child_itm_id " + " AND ire_parent_itm_id = ? "
			+ " AND app_ent_id = ?";

	// get all app_id                
	public static long getLatestAppId(Connection con, long itm_id, long ent_id) throws SQLException {
		long app_id = 0;
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		String SQL = (itm.getCreateRunInd(con)) ? sql_get_latest_app_id_has_run : sql_get_latest_app_id_no_run;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL);
			stmt.setLong(1, itm_id);
			stmt.setLong(2, ent_id);

			rs = stmt.executeQuery();

			if (rs.next()) {
				app_id = rs.getLong("app_id");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return app_id;
	}

	public static long getLatestApplicationId(Connection con, long itm_id, long usr_ent_id) throws SQLException {

		String SQL = " Select Max(app_id) From aeApplication Where app_itm_id = ? And app_ent_id = ? ";
		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setLong(1, itm_id);
		stmt.setLong(2, usr_ent_id);
		ResultSet rs = stmt.executeQuery();
		long app_id = 0;
		if (rs.next()) {
			app_id = rs.getLong(1);
		}
		stmt.close();
		return app_id;
	}

	// return the application confirmed timestamp for non-run item and return class start date for run
	public static Timestamp getApplicationTimestamp(Connection con, long itm_id, long usr_ent_id) throws SQLException {
		Timestamp result = null;
		PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_confirmed_application_timestamp);
		int index = 1;
		stmt.setLong(index++, itm_id);
		stmt.setLong(index++, itm_id);
		stmt.setString(index++, ADMITTED);
		stmt.setLong(index++, usr_ent_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			if (rs.getBoolean("itm_run_ind")) {
				result = rs.getTimestamp("itm_eff_start_datetime");
			} else {
				result = rs.getTimestamp("app_create_timestamp");
			}
		}
		stmt.close();

		return result;
	}

	public static final String FAKE_STATUS_REMOVED = "Removed";

	public static StringBuffer getApplicationStatusAsXML(Connection con, long app_id) throws qdbException, SQLException {
		StringBuffer xml = new StringBuffer();
		aeApplication app = new aeApplication();
		app.app_id = app_id;
		if (app_id > 0) {
			app.get(con);
			xml.append("<application id=\"").append(app.app_id).append("\" ent_id=\"").append(app.app_ent_id).append("\" itm_id=\"").append(app.app_itm_id).append("\" status=\"").append(app.app_status)
					.append("\" process_status=\"").append(app.app_process_status).append("\" />");
		} else {
			xml.append("<application id=\"").append("\" ent_id=\"").append("\" itm_id=\"").append("\" status=\"").append(FAKE_STATUS_REMOVED).append("\" process_status=\"").append("\" />");
		}
		return xml;
	}

	public String getAttandanceStatus(Connection con) throws SQLException {

		String SQL = " Select ats_type " + "From aeAttendance, aeAttendanceStatus " + "Where att_app_id = ? " + "And att_ats_id = ats_id ";
		PreparedStatement stmt = con.prepareStatement(SQL);
		stmt.setLong(1, this.app_id);
		ResultSet rs = stmt.executeQuery();
		String ats_type = null;
		if (rs.next()) {
			ats_type = rs.getString("ats_type");
		}
		stmt.close();
		return ats_type;
	}

	/**
	Get the pending approval role of this application.
	Pre-defined variables: app_id
	@param con Connection to database
	@return pending approval role of this application; or null if the application is not in pending approval status
	*/
	public String getPendingApprovalRole(Connection con) throws SQLException {
		return DbAppnApprovalList.getAppnPendingApprovalRole(con, this.app_id);
	}

	/**
	Get the current status' "Remove" action of this application
	Pre-define variables: app_id
	@param con Connection to database
	@return Vector with the following [index] and value:
	        [0]: process id
	        [1]: status id
	        [2]: action id
	        [3]: from status
	        [4]: to status
	        [5]: verb
	*/
	public Vector getCurrentRemoveAction(Connection con) throws SQLException, qdbException, IOException {

		//get the application's details
		if (this.app_itm_id == 0) {
			get(con);
		}

		//get the details of the application's current process
		aeWorkFlow wkf = new aeWorkFlow(dbUtils.xmlHeader);
		Vector vProcess = wkf.parseProcessXML(this.app_process_xml);
		String processId = (String) vProcess.elementAt(0);
		String processName = (String) vProcess.elementAt(1);
		String statusId = (String) vProcess.elementAt(2);
		String statusName = (String) vProcess.elementAt(3);

		//get the item's workflow template id
		aeItem item = new aeItem();
		item.itm_id = this.app_itm_id;
		long tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);

		//get the remove action and return
		return wkf.getRemoveAction(tpl_id, processId, statusId);
	}

	public static boolean isExistAppId(Connection con, long app_id) throws SQLException {
		String sql = "select app_id from aeApplication where app_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, app_id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return true;
		}
		pstmt.close();
		return false;
	}

	/**
	 * @return
	 */
	public static boolean isExistTkhId(Connection con, long app_id) throws SQLException {
		long tkh_id = 0;
		String sql = "select app_tkh_id from aeApplication where app_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, app_id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			tkh_id = rs.getLong("app_tkh_id");
			if (tkh_id > 0) {
				return true;
			}
		}
		pstmt.close();
		return false;
	}

	/**
	 * @param con
	 * @param mtv_tkh_id
	 * @return
	 */
	public static long getAppIdByTkhId(Connection con, long tkh_id) throws SQLException {
		long app_id = 0;
		String sql = "select app_id from aeApplication where app_tkh_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, tkh_id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			app_id = rs.getLong("app_id");
		}
		pstmt.close();
		return app_id;
	}

	private static final String sql_get_latest_ses_app_id_no_run = " SELECT max(app_id) as app_id FROM aeApplication, Courseevaluation " + " WHERE app_tkh_id = cov_tkh_id and app_itm_id = ? "
			+ " AND app_ent_id = ? and cov_status in ('I','C')";
	private static final String sql_get_latest_ses_app_id_has_run = " SELECT max(app_id) as app_id FROM aeApplication, aeItemRelation, Courseevaluation  "
			+ " WHERE app_tkh_id = cov_tkh_id and app_itm_id = ire_child_itm_id " + " AND ire_parent_itm_id = ? " + " AND app_ent_id = ? and cov_status in ('I','C')";

	// get all app_id                
	public static long getLatestSesAppId(Connection con, long itm_id, long ent_id) throws SQLException {
		long app_id = 0;
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		String SQL = (itm.getCreateRunInd(con)) ? sql_get_latest_ses_app_id_has_run : sql_get_latest_ses_app_id_no_run;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL);
			stmt.setLong(1, itm_id);
			stmt.setLong(2, ent_id);

			rs = stmt.executeQuery();

			if (rs.next()) {
				app_id = rs.getLong("app_id");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return app_id;
	}

	public static boolean hasEffectiveAppId(Connection con, long itm_id, long usr_ent_id) throws SQLException {
		String SQL = " Select app_id From aeApplication Where (app_itm_id = ? or app_itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id = ?)) And app_ent_id = ?  and app_status in (?,?)";
		PreparedStatement stmt = con.prepareStatement(SQL);
		int index = 1;
		stmt.setLong(index++, itm_id);
		stmt.setLong(index++, itm_id);
		stmt.setLong(index++, usr_ent_id);
		stmt.setString(index++, aeApplication.ADMITTED);
		stmt.setString(index++, aeApplication.PENDING);
		ResultSet rs = stmt.executeQuery();
		long app_id = 0;
		if (rs.next()) {
			app_id = rs.getLong(1);
		}
		stmt.close();
		return app_id > 0 ? true : false;
	}

	/**
	 * Check if the application can be canceled by learner.
	 * @param con
	 * @param app_id
	 * @return
	 * @throws SQLException
	 */
	public boolean canCancel(Connection con) throws SQLException {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

			// check self-enrolled
			if (app_create_usr_id != null && !app_create_usr_id.substring(app_create_usr_id.indexOf("u")+1).equals(app_ent_id + "")) {
				return false;
			}
			//check current time between enroll period
			Timestamp now = cwSQL.getTime(con);
			String sqlCheckAppn = "select itm_id from aeItem where  " + " itm_appn_start_datetime <= ? and itm_appn_end_datetime >= ? and itm_id = ? ";
			stmt = con.prepareStatement(sqlCheckAppn);
			int index = 1;
			stmt.setTimestamp(index++, now);
			stmt.setTimestamp(index++, now);
			stmt.setLong(index++, app_itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
			} else {
				return false;
			}

			//check studied
			String sqlCheckNeverAcc = "select cov_last_acc_datetime from courseevaluation " + " where cov_tkh_id = ? ";
			stmt = con.prepareStatement(sqlCheckNeverAcc);
			stmt.setLong(1, app_tkh_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Timestamp accTime = rs.getTimestamp("cov_last_acc_datetime");
				if (accTime != null) {
					return false;
				}
			}
			//check attendance rate
			String sqlCheckAtt = "select * from aeAttendance " + " where att_app_id = ? and att_rate is not null";
			stmt = con.prepareStatement(sqlCheckAtt);
			stmt.setLong(1, app_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return false;
			}
			//check approved 
			String sqlCheckNeverAppr = " select aal_id from aeAppnApprovalList " + " where aal_app_id = ? and aal_action_taker_usr_ent_id is not null ";
			stmt = con.prepareStatement(sqlCheckNeverAppr);
			stmt.setLong(1, app_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return false;
			}
			//check scoring schema set
			String sqlCheckMev = "select mtv_tkh_id from measurementEvaluation " + " where mtv_tkh_id = ? ";
			stmt = con.prepareStatement(sqlCheckMev);
			stmt.setLong(1, app_tkh_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return false;
			}
			return true;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public static void getApplication(Connection con, long itm_id, long usr_ent_id, CourseBean courseBean) throws SQLException {
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		if (itm.checkEnrolled(con, usr_ent_id, null)) {
			long app_id = aeApplication.getLatestApplicationId(con, itm_id, usr_ent_id);
			if (app_id > 0) {
				long tkh_id = aeApplication.getTkhId(con, app_id);
				long cos_id = dbCourse.getCosResId(con, itm_id);
				courseBean.setApp_id(app_id);
				courseBean.setApp_tkh_id(tkh_id);
				courseBean.setApp_cos_id(cos_id);
				courseBean.setApp_ent_id(usr_ent_id);
			}
		}
	}
	
	

	/* status :
	 0 	报名成功，并可以开始学习；
	 1 	报名成功，处于等待审批状态；
	 2 	报名成功，处理等待队列；
	 -1 	报名不成功，
	 msg 	为不成功的原因
	 */
	public void insAppForPage(Connection con , loginProfile prof, Model model, long usr_ent_id, long itm_id, boolean confilict_check) throws Exception {
		try {

 			if (usr_ent_id == 0) {
				usr_ent_id = prof.usr_ent_id;
			}
			model.addAttribute("status", 0); 
			aeQueueManager qm = new aeQueueManager();
			aeItem item = new aeItem();
			item.itm_id = itm_id;
			item.getItem(con);
			int enrol_cnt = aeApplication.countItemAppn(con, item.itm_id, false);
			if (confilict_check && item.itm_not_allow_waitlist_ind && (item.itm_capacity > 0 && item.itm_capacity <= enrol_cnt)) {
				model.addAttribute("status", -1);
				model.addAttribute("msg", LangLabel.getValue(prof.cur_lan, "AEQM11"));
				return;
			}

			ArrayList<String> conf_msg_ls = new ArrayList<String>();
			qm.checkAppnConflict(con, itm_id, usr_ent_id, true, conf_msg_ls);
			if (conf_msg_ls != null && conf_msg_ls.size() > 0) {
				model.addAttribute("status", -1);
				StringBuffer msg = new StringBuffer("");
				for(String label : conf_msg_ls){
					msg.append(LangLabel.getValue(prof.cur_lan, "ENROL_" + label) + "~");
				}
				model.addAttribute("msg", msg.toString());
				return;
			}
			aeApplication aeApp = insApp(con, prof, usr_ent_id, itm_id, confilict_check, item);
			if (aeApplication.PENDING.equalsIgnoreCase(aeApp.app_status)) {
				model.addAttribute("status", 1);
			} else if (aeApplication.WAITING.equalsIgnoreCase(aeApp.app_status)) {
				model.addAttribute("status", 2);
			}

			
		} catch (cwSysMessage e) {
			con.rollback();
			//e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
			model.addAttribute("status", -1);
			model.addAttribute("msg", e.getSystemMessage(prof.cur_lan));
		} finally {
			
		}
	}

	public aeApplication insApp(Connection con, loginProfile prof, long usr_ent_id, long itm_id, boolean confilict_check, aeItem item) throws Exception {
		aeQueueManager qm = new aeQueueManager();
		if (item == null) {
			item = new aeItem();
			item.itm_id = itm_id;
			item.getItem(con);
		}
		aeApplication aeApp = null;
		if (!confilict_check || item.itm_app_approval_type == null || item.itm_app_approval_type.length() == 0) {
			aeApp = qm.insAppNoWorkflow(con, null, usr_ent_id, itm_id, null, null, prof, item);
			/*if(item.itm_integrated_ind){
			    Vector vec = IntegratedLrn.getCourse( con,  itm_id) ;
			    if(vec != null && vec.size() > 0){
			        for(int i=0; i<vec.size(); i++) {
			            IntegratedLrn itgLrn = (IntegratedLrn)vec.elementAt(i);
			            try{
			                if(itgLrn != null && itgLrn.itm_apply_ind)
			                    insApp( con,  prof,  usr_ent_id, itgLrn.itm_id,  confilict_check, null);
			            }catch(Exception e){
			                e.printStackTrace();
			            }
			        }
			    }
			}*/
		} else {
			aeApp = qm.insApplication(con, null, usr_ent_id, itm_id, prof, 0, 0, 0, 0, null, null, confilict_check, 0, null, null);
		}
		return aeApp;
	}

	public void cancelApp(Connection con, loginProfile prof, Model model, long app_id) throws Exception {
		try {
			if(null!=model){
				model.addAttribute("status", 0);
			}
			aeQueueManager qm = new aeQueueManager();
			aeApplication app = new aeApplication();
			app.app_id = app_id;
			app.getWithItem(con);

			aeAppnActnHistory actn = new aeAppnActnHistory();
			aeTemplate tpl = new aeTemplate();
			aeItem item = new aeItem();
			long tpl_id;
			Vector<aeAppnActnHistory> appn_history = new Vector<aeAppnActnHistory>();
			Timestamp cur_timestamp;
			aeWorkFlow workFlow = new aeWorkFlow(dbUtils.xmlHeader);
			String curStatus;
			StringBuffer action = new StringBuffer();

			app.app_id = app_id;
			app.getWithItem(con);
			item.itm_id = app.app_itm_id;
			tpl_id = item.getTemplateId(con, aeTemplate.WORKFLOW);
			tpl.tpl_id = tpl_id;
			tpl.get(con);

			long action_id;
			long status_id;
			long process_id;
			String fr;
			
			String work_flow_lan = ((Personalization)qdbAction.wizbini.cfgOrgPersonalization.get(prof.root_id)).getSkinList().getDefaultLang();
			String to = LangLabel.getValue(work_flow_lan, "work_flow_app_status_canel");
			String verb = LangLabel.getValue(work_flow_lan, "work_flow_app_status_canel");
			if (aeApplication.ADMITTED.equalsIgnoreCase(app.app_status)) {
				action_id = 3;
				status_id = 1;
				process_id = 1;
				fr = app.app_process_status;

			} else if (aeApplication.PENDING.equalsIgnoreCase(app.app_status)) {
				action_id = 3;
				status_id = 1;
				process_id = 1;
				fr = app.app_process_status;

			} else if (aeApplication.WAITING.equalsIgnoreCase(app.app_status)) {
				action_id = 3;
				status_id = 2;
				process_id = 1;
				fr = app.app_process_status;

			} else {
				if(null!=model){
					model.addAttribute("status", 1); 
					model.addAttribute("status", LangLabel.getValue(prof.cur_lan, "XMG002"));
				}
				return;
			}

			action.append("<current process_id=\"").append(process_id).append("\" status_id=\"").append(status_id);
			action.append("\" action_id=\"").append(action_id).append("\"/>");

			cur_timestamp = dbUtils.getTime(con);
			actn.aah_app_id = app_id;
			actn.aah_process_id = process_id;
			actn.aah_fr = fr;
			actn.aah_to = to;
			actn.status_id = status_id;
			actn.aah_verb = verb;
			actn.aah_action_id = action_id;
			actn.aah_create_usr_id = prof.usr_id;
			actn.aah_create_timestamp = cur_timestamp;
			actn.aah_upd_usr_id = prof.usr_id;
			actn.aah_upd_timestamp = cur_timestamp;

			curStatus = app.app_process_xml;

			actn.status = workFlow.checkStatus(action.toString(), curStatus, tpl.tpl_xml);
			appn_history.add(actn);

			qm.saveAppnActn(con, app, null, false, appn_history, app.app_upd_timestamp, prof);

			// Waiting list自動按先進先出原則處理
			qm.doAppnAutoIntoQueue(con, prof, item.itm_id);
		} catch (Exception e) {
			con.rollback();
			CommonLog.error(e.getMessage(),e);
			//e.printStackTrace();
			if(null!=model){
				model.addAttribute("status", -1);
				model.addAttribute("MSG", e.getMessage());
			}
		} finally {
			
		}
	}
	
	public static aeApplication getFirstWaitingLearner(Connection con, long itm_id) {
		aeApplication app = null;
		PreparedStatement stmt = null;
		try {
			String sql = "SELECT app_id, app_ent_id, app_itm_id FROM aeApplication"
					+ " WHERE app_itm_id = ? and app_status = ? and app_process_status = ?"
					+ " ORDER BY app_create_timestamp ASC";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, itm_id);
			stmt.setString(index++, "Waiting");
			stmt.setString(index++, "Waitlisted");
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				app = new aeApplication();
				app.app_id = rs.getLong("app_id");
				app.app_ent_id = rs.getLong("app_ent_id");
				app.app_itm_id = rs.getLong("app_itm_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return app;
	}
	
	public static void updateCreateUsrId(Connection con, long app_ent_id, long itm_id) {
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE aeApplication SET app_create_usr_id = ? WHERE app_ent_id = ? and app_itm_id = ?";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, "s1u" + app_ent_id);
			stmt.setLong(index++, app_ent_id);
			stmt.setLong(index++, itm_id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteWaitingLearner(Connection con, long app_id) {
		PreparedStatement stmt = null;
		try {
			String sql = "DELETE FROM aeAppnActnHistory WHERE aah_app_id = ?";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, app_id);
			if(stmt.executeUpdate() != -1) {
				sql = "DELETE FROM aeApplication WHERE app_id = ?";
				stmt = con.prepareStatement(sql);
				index = 1;
				stmt.setLong(index++, app_id);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static loginProfile getUserByUsrEntId(Connection con, long usr_ent_id) {
		loginProfile prof = null;
		PreparedStatement stmt = null;
		try {
			String sql = "SELECT usr_id , usr_pwd, usr_ste_usr_id, usr_ent_id, usr_display_bil, usr_pwd_upd_timestamp,"
					+ " usr_last_login_date, usr_last_login_role,usr_login_trial, usr_status, usr_login_status FROM Reguser"
					+ " where usr_ent_id = ?";
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, usr_ent_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				prof = new loginProfile();
				prof.usr_id = rs.getString("usr_id");
				prof.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
				prof.usr_ent_id = rs.getLong("usr_ent_id");
				prof.usr_display_bil = rs.getString("usr_display_bil");
				prof.usr_pwd_upd_timestamp = rs.getTimestamp("usr_pwd_upd_timestamp");
				prof.usr_last_login_date = rs.getTimestamp("usr_last_login_date");
				prof.last_login_status = rs.getString("usr_login_status");
				prof.usr_status = rs.getString("usr_status");
				prof.current_role = "NLRN_1";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return prof;
	}

	/**
	 * 根据课程id和报名状态获取报名id
	 * @param itm_id 课程id
	 * @param ent_id 用户id
	 * @param app_status 报名状态 
	 */
	public static long getAppIdByAppStatus(Connection con, long itm_id, long ent_id, String app_status) throws SQLException {
		long result;
		aeItem itm = new aeItem();
		itm.itm_id = itm_id;
		String SQL = (itm.getCreateRunInd(con)) ? sql_get_app_id_has_run : sql_get_app_id_no_run;

		SQL += " and app_status = ? ";
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL);
			int index = 1;
			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, ent_id);
			stmt.setString(index++, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
			stmt.setString(index++, app_status);

			rs = stmt.executeQuery();

			if (rs.next()) {
				result = rs.getLong("app_id");
			} else {
				result = 0;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return result;
	}
	
	/**
     * 获取 @param usr_ent_id 用户等待审批课程标题
     * @param con
     * @param usr_ent_id
     * @param spiltor 分隔符
     * @return
     */
	public static String pendingItmTitles(Connection con, long usr_ent_id,String spiltor) throws SQLException{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select itm_title from aeItem where itm_id in ")
		   .append("(select app_itm_id from aeApplication where app_status = ? and app_ent_id = ?)");
		StringBuffer result = new StringBuffer();
		
		PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
		try{
			stmt = con.prepareStatement(sql.toString());
		    stmt.setString(1, PENDING);
		    stmt.setLong(2, usr_ent_id);
		    rs = stmt.executeQuery();
		    
		    while(rs.next()){
		    	result.append(rs.getString(1)+spiltor);
		    }
		    
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}
        
		return result.toString();
		
	}
	/**
	 * 查询用户已报名的但处于Pending Approval appId
	 * @param con
	 * @param usr_ent_id
	 * @return
	 */
	public static List getPendingApprovalAppId(Connection con , long usr_ent_id){
		List appIdList = new ArrayList();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT app_id,app_ent_id,app_itm_id,app_status,app_process_status FROM aeApplication");
		sb.append(" where app_ent_id = ? and app_process_status = ? ");
		PreparedStatement stmt = null;
		ResultSet rs = null; 
			try {
				stmt = con.prepareStatement(sb.toString());
				stmt.setLong(1, usr_ent_id);
				stmt.setString(2,PENDING_APPROVAL);
				rs = stmt.executeQuery();
				aeApplication ae = null;
				while(rs.next()){
					ae = new aeApplication();
					ae.app_id = rs.getLong("app_id");
					ae.app_itm_id = rs.getLong("app_itm_id");
					appIdList.add(ae);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				cwSQL.cleanUp(rs, stmt);
			}
			return appIdList;
		
	}

}
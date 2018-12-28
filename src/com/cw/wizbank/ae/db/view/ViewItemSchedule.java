package com.cw.wizbank.ae.db.view;

import java.util.HashMap;
import java.util.Vector;
import java.util.Calendar;
import java.sql.*;
import java.sql.SQLException;
import com.cw.wizbank.ae.aeItemLesson;

public class ViewItemSchedule {

	private Connection con = null;

	private final static String sql_itm_has_timetable = "select ils_itm_id from aeItemLesson where itm_id=ils_itm_id and ils_start_time > ?";
	private final static String sql_ils_time = " ils_end_time >= ? and ils_start_time < ?";
	private final static String sql_eff_time = " itm_eff_end_datetime >= ? and itm_eff_start_datetime < ?";

	private final static String sql_get_cos = "select itm_id,itm_title,itm_life_status ,itm_status from aeItem,aeItemRelation where itm_id =ire_parent_itm_id and ire_child_itm_id =?";

	public ViewItemSchedule(Connection con) throws SQLException {
		if (con == null)
			throw new SQLException("connection not available");
		else {
			this.con = con;
		}
	}

	private Vector getAllItmInTcrDay(long tcr_id, String day) throws SQLException {
		String sql_get_itm_in_tcr_day =
			"select distinct(itm_id),itm_title,itm_status,itm_life_status,"
				+ "itm_run_ind,itm_apply_ind  from aeItem where 1=1 and ("
				+ "( exists ("
				+ sql_itm_has_timetable
				+" and "
				+ sql_ils_time
				+ ")) or (not exists ("
				+ sql_itm_has_timetable
				+ ") and "
				+sql_eff_time
				+"))";
		
		if (tcr_id > 0) {
			sql_get_itm_in_tcr_day += " and itm_tcr_id= ? ";
		}
		Vector result = new Vector();

		Timestamp dayStart = Timestamp.valueOf(day);
		Calendar tmp = Calendar.getInstance();
		tmp.setTime(dayStart);
		tmp.add(Calendar.DATE, 1);
		Timestamp dayEnd = new Timestamp(tmp.getTime().getTime());

		PreparedStatement stmt = con.prepareStatement(sql_get_itm_in_tcr_day);
		int i = 1;
		stmt.setTimestamp(i++, Timestamp.valueOf(aeItemLesson.NULL_ILS_TIME_LIMIT));
		stmt.setTimestamp(i++, dayStart);
		stmt.setTimestamp(i++, dayEnd);
		stmt.setTimestamp(i++, Timestamp.valueOf(aeItemLesson.NULL_ILS_TIME_LIMIT));
		stmt.setTimestamp(i++, dayStart);
		stmt.setTimestamp(i++, dayEnd);
		if (tcr_id > 0) {
			stmt.setLong(i++, tcr_id);
		}
		ResultSet rs = stmt.executeQuery();
		HashMap record = new HashMap();
		while (rs.next()) {
			record = new HashMap();
			record.put("itm_id", new Long(rs.getLong("itm_id")));
			record.put("itm_apply_ind", new Integer(rs.getInt("itm_apply_ind")));
			record.put("itm_status", new String(rs.getString("itm_status")));
			String itm_life_status = rs.getString("itm_life_status");
			if (itm_life_status == null)
				itm_life_status = "";
			record.put("itm_life_status", itm_life_status);
			record.put("itm_title", new String(rs.getString("itm_title")));
			record.put("itm_run_ind", new Integer(rs.getInt("itm_run_ind")));
			result.addElement(record);
		}

		stmt.close();
		return result;
	}
	public Vector getDailySchedule(String day, long tc_id) throws SQLException {
		Vector result = new Vector();
		Vector VtAllItmIntcrDay = new Vector();
		VtAllItmIntcrDay = getAllItmInTcrDay(tc_id, day);
		HashMap record = new HashMap();
		for (int i = 0, size = VtAllItmIntcrDay.size(); i < size; i++) {
			record = new HashMap();
			record = (HashMap) VtAllItmIntcrDay.elementAt(i);
			int itm_run_ind = ((Integer) record.get("itm_run_ind")).intValue();
			int itm_apply_ind = ((Integer) record.get("itm_apply_ind")).intValue();
			long itm_id = ((Long) record.get("itm_id")).longValue();
			String itm_life_status = (String) record.get("itm_life_status");
			String itm_status = (String) record.get("itm_status");
			if (itm_status.equalsIgnoreCase("ON") || itm_life_status.equalsIgnoreCase("cancelled")) {
				if (itm_run_ind == 1 && itm_apply_ind == 1) {
					HashMap temp = getRunParent(itm_id, itm_life_status);
					String itm_parent_status = (String) record.get("itm_status");
					if (itm_parent_status.equalsIgnoreCase("ON") || itm_life_status.equalsIgnoreCase("cancelled")) {
						result.add(temp);
					}
				} else if (itm_run_ind == 0 && itm_apply_ind == 1) {
					result.add(getSelfStudy(record));
				}
			}
		}

		return result;
	}

	private HashMap getRunParent(long itm_id, String itm_life_status) throws SQLException {
		HashMap result = new HashMap();
		PreparedStatement stmt = con.prepareStatement(sql_get_cos);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			result.put("itm_id", new Long(rs.getLong("itm_id")));
			result.put("itm_title", new String(rs.getString("itm_title")));
			result.put("itm_life_status", itm_life_status); //itm_life_status of class.
			result.put("itm_status", new String(rs.getString("itm_status")));
		}
		stmt.close();
		return result;
	}
	private HashMap getSelfStudy(HashMap record) {
		HashMap result = new HashMap();
		result.put("itm_id", record.get("itm_id"));
		result.put("itm_title", record.get("itm_title"));
		result.put("itm_life_status", record.get("itm_life_status"));
		result.put("itm_status", record.get("itm_status"));
		return result;
	}

}
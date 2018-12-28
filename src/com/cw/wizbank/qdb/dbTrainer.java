package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class dbTrainer {
	public long tra_id;
	public String tra_name;
	public String tra_org;
	public Timestamp tra_create_datetime;
	public String tra_create_user_id;
	public Timestamp tra_update_datetime;
	public String tra_update_user_id;
	public int cur_page;
	public int page_size;
	
	public void ins(Connection con, loginProfile prof) throws SQLException, qdbErrMessage {
		String sql = "insert into Trainer(tra_name, tra_org, tra_create_datetime, tra_create_user_id) values (?, ?, ?, ?)";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, tra_name);
			stmt.setString(index++, tra_org);
			stmt.setTimestamp(index++, cwSQL.getTime(con));
			stmt.setString(index++, prof.usr_id);
			stmt.executeUpdate();
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new qdbErrMessage("SQL Error: " + e.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void upd(Connection con, loginProfile prof) throws SQLException, qdbErrMessage {
		String sql = "update Trainer set tra_name = ?,  tra_org = ?,  tra_update_datetime = ?, tra_update_user_id = ? where tra_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, tra_name);
			stmt.setString(index++, tra_org);
			stmt.setTimestamp(index++, cwSQL.getTime(con));
			stmt.setString(index++, prof.usr_id);
			stmt.setLong(index++, tra_id);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new qdbErrMessage("SQL Error: " + e.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void del(Connection con, String[] tra_id_lst) throws SQLException, qdbErrMessage {
		PreparedStatement stmt = null;
		try {
			String sql = "delete from Trainer where tra_id in " + cwUtils.array2list(tra_id_lst);
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			throw new qdbErrMessage("SQL Error: " + e.getMessage());
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public void del(Connection con, long itm_id) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = "delete from aeItemTrainer where ait_itm_id = ?";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public String getTrainerXML(Connection con, boolean isPaging) throws SQLException {
		StringBuffer result = new StringBuffer();
		String sql = "select * from trainer";
		if (tra_id > 0) {
			sql += " where tra_id = ?";
		}
		if (tra_id > 0 && tra_name != null && tra_name.length() > 0) {
			sql += " and tra_name like '%" + tra_name + "%'";
		} else if (tra_name != null && tra_name.length() > 0) {
			sql += " where tra_name like '%" + tra_name + "%'";
		}
		if ((tra_id > 0 || (tra_name != null && tra_name.length() > 0)) && tra_org != null && tra_org.length() > 0) {
			sql += " and tra_org like '%" + tra_org + "%'";
		} else if (tra_org != null && tra_org.length() > 0) {
			sql += " where tra_org like '%" + tra_org + "%'";
		}
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			if (tra_id > 0) {
				stmt.setLong(1, tra_id);
			}
			ResultSet rs = stmt.executeQuery();
			int cnt = 0;
			int start = (cur_page - 1) * page_size + 1;
			int end = cur_page * page_size;
			while (rs.next()) {
				cnt++;
				if (isPaging) {
					if (cnt >= start && cnt <= end) {
						result.append("<tra tra_id=\"").append(rs.getLong("tra_id"));
						result.append("\" tra_name=\"").append(dbUtils.esc4XML(rs.getString("tra_name")));
						result.append("\" tra_org=\"").append(dbUtils.esc4XML(rs.getString("tra_org")));
						result.append("\" />").append(dbUtils.NEWL);
					}
				} else {
					result.append("<tra tra_id=\"").append(rs.getLong("tra_id"));
					result.append("\" tra_name=\"").append(dbUtils.esc4XML(rs.getString("tra_name")));
					result.append("\" tra_org=\"").append(dbUtils.esc4XML(rs.getString("tra_org")));
					result.append("\" />").append(dbUtils.NEWL);
				}
			}
			result.append("<page cur_page=\"" + cur_page + "\" page_size=\"" + page_size + "\" total=\"" + cnt + "\" />" + dbUtils.NEWL);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result.toString();
	}
	
	public String getTrainerXML(Connection con, long itm_id) throws SQLException {
		StringBuffer result = new StringBuffer();
		String sql = "select * from Trainer, aeItemTrainer where ait_tra_id = tra_id and ait_itm_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.append("<tra tra_id=\"").append(rs.getLong("tra_id"));
				result.append("\" tra_name=\"").append(dbUtils.esc4XML(rs.getString("tra_name")));
				result.append("\" tra_org=\"").append(dbUtils.esc4XML(rs.getString("tra_org")));
				result.append("\" />").append(dbUtils.NEWL);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result.toString();
	}
	
	public void saveItemTrainer(Connection con, long itm_id, String[] itm_trainer_lst) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String delete_sql = "delete from aeItemTrainer where ait_itm_id = ?";
			stmt = con.prepareStatement(delete_sql);
			stmt.setLong(1, itm_id);
			stmt.executeUpdate();
			if(itm_trainer_lst != null && itm_trainer_lst.length > 0){
				String sql = "insert into aeItemTrainer (ait_itm_id, ait_tra_id) values (?, ?)";
				long ids[] = cwUtils.stringArray2LongArray(itm_trainer_lst);
				if(itm_trainer_lst != null && ids.length > 0){
					for(int i = 0; i < ids.length; i++){
						stmt = con.prepareStatement(sql);
						stmt.setLong(1, itm_id);
						stmt.setLong(2, ids[i]);
						stmt.executeUpdate();
					}
				}
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public String getTrainerName(Connection con, long tra_id) throws SQLException {
		PreparedStatement stmt = null;
		String result = "";
		try {
			String sql = "select * from Trainer where tra_id = ?";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, tra_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result = rs.getString("tra_name");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result;
	}
	
	public boolean getTrainerName(Connection con, String tra_name) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = "select * from Trainer where tra_name = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, tra_name);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				return true;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return false;
	}
	
	public boolean learnerResponseTrainer(Connection con, long itm_id) throws SQLException {
		PreparedStatement stmt = null;
		try {
			String sql = "select * from ProgressAttempt "
					+ " where atm_int_res_id in (select res_id from Resources where res_tra_id in " 
					+ " (select ait_tra_id from aeItemTrainer where ait_itm_id = " 
					+ " (select distinct app_itm_id from aeApplication ,ModuleEvaluation where app_tkh_id =  mov_tkh_id and app_itm_id = ?)))";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				return true;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return false;
	}
	
}

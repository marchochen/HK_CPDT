package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;

import com.cw.wizbank.Application;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class SystemSetting {

	public String sys_cfg_type;
	public String sys_cfg_value;
	public Timestamp sys_cfg_create_timestamp;
	public String sys_cfg_create_usr_id;
	public Timestamp sys_cfg_update_timestamp;
	public String sys_cft_update_usr_id;

	public final static String SYS_CFG_TYPE_WARN = "THR_WARN";
	public final static String SYS_CFG_TYPE_BLOCK = "THR_BLOCK";
	public final static String SYS_CFG_TYPE_EMAIL = "THR_SPT_EMAIL";
	public final static String SYS_MULTIPLE_LOGIN_IND = "MULTIPLE_LOGIN_IND";

	public void updSysSetting(Connection con) throws SQLException {
		String updSql = "update SystemSetting " 
				+ " set sys_cfg_value =?, sys_cft_update_usr_id =?, sys_cfg_update_timestamp=? " 
				+ " where sys_cfg_type=?";

		PreparedStatement stmt = con.prepareStatement(updSql);
		int index = 1;
		stmt.setString(index++, sys_cfg_value);
		stmt.setString(index++, sys_cft_update_usr_id);
		stmt.setTimestamp(index++, sys_cfg_update_timestamp);
		stmt.setString(index++, sys_cfg_type);
		stmt.executeUpdate();
		stmt.close();
	}

	public static void updateSystemSetting(Connection con, String usr_id, String thr_warn_value, String thr_block_value, String thr_spt_email_value,String multiple_login_ind) throws SQLException {
		SystemSetting ss = new SystemSetting();
		Timestamp curTime = cwSQL.getTime(con);

		// if thr_warn_value/thr_block_value have value, parse it to long
		if (thr_warn_value != null && thr_warn_value.length() > 0) {
			thr_warn_value = String.valueOf(Long.parseLong(thr_warn_value));
		}
		if (thr_block_value != null && thr_block_value.length() > 0) {
			thr_block_value = String.valueOf(Long.parseLong(thr_block_value));
		}
		ss.sys_cfg_type = SYS_CFG_TYPE_WARN;
		ss.sys_cfg_value = thr_warn_value;
		ss.sys_cft_update_usr_id = usr_id;
		ss.sys_cfg_update_timestamp = curTime;
		ss.updSysSetting(con);

		ss.sys_cfg_type = SYS_CFG_TYPE_BLOCK;
		ss.sys_cfg_value = thr_block_value;
		ss.sys_cft_update_usr_id = usr_id;
		ss.sys_cfg_update_timestamp = curTime;
		ss.updSysSetting(con);

		ss.sys_cfg_type = SYS_CFG_TYPE_EMAIL;
		ss.sys_cfg_value = thr_spt_email_value;
		ss.sys_cft_update_usr_id = usr_id;
		ss.sys_cfg_update_timestamp = curTime;
		ss.updSysSetting(con);
		
		ss.sys_cfg_type = SYS_MULTIPLE_LOGIN_IND;
		ss.sys_cfg_value = multiple_login_ind;
		ss.sys_cft_update_usr_id = usr_id;
		ss.sys_cfg_update_timestamp = curTime;
		Application.MULTIPLE_LOGIN = "1".equals(multiple_login_ind);
		ss.updSysSetting(con);
		
	}

	public static String getSystemSettingXml(Connection con) throws SQLException {
		StringBuffer sysSetXml = new StringBuffer();
		sysSetXml.append("<sys_setting current_active_user=\"").append(CurrentActiveUser.getcurActiveUserCount(con)).append("\">");
		String getSql = "select * from SystemSetting";

		PreparedStatement stmt = con.prepareStatement(getSql);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			sysSetXml.append("<sys_set type=\"").append(cwUtils.esc4XML(rs.getString("sys_cfg_type"))).append("\" value=\"").append(cwUtils.esc4XML(rs.getString("sys_cfg_value"))).append("\"/>");
		}
		sysSetXml.append("</sys_setting>");
		stmt.close();

		return sysSetXml.toString();
	}
	//查询企业充许最大在线人数
	public static Hashtable getCurSystemSetting(Connection con) throws SQLException {
		Hashtable curSysSet = new Hashtable();
		String getSql = "select * From SystemSetting " + (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) " : "");

		PreparedStatement stmt = con.prepareStatement(getSql);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			curSysSet.put(rs.getString("sys_cfg_type"), cwUtils.escNull(rs.getString("sys_cfg_value")));
		}
		stmt.close();
		return curSysSet;
	}
	//查询企业充许最大在线人数
	public static long getEipMaxpeakcount(Connection con,long eipmaxpeakcount) throws SQLException{
		long maxpeakcount = 0;
		String getsql = " select eip_max_peak_count From EnterpriseInfoPortal where eip_tcr_id = '"+eipmaxpeakcount+"'  ";
		PreparedStatement stmt = con.prepareStatement(getsql);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			maxpeakcount=rs.getLong("eip_max_peak_count");
		}
		return maxpeakcount;
		
	}
	
	public static long getEippeakcount(Connection con,long eipmaxpeakcount) throws SQLException{
		long peakcount = 0;
		String getsql = "select COUNT(cau_eip_tcr_id) cau_eip_peak_count from CurrentActiveUser where  cau_eip_tcr_id =  '"+eipmaxpeakcount+"'  ";
		PreparedStatement stmt = con.prepareStatement(getsql);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			peakcount=rs.getLong("cau_eip_peak_count");
		}
		return peakcount;
		
	}
	
}

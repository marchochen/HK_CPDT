package com.cw.wizbank.ae;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class aeItemExtension {
	public long ies_itm_id;
	public String ies_lang;
	public String ies_objective;
	public String ies_contents;
	public String ies_duration;
	public String ies_audience;
	public String ies_prerequisites;
	public String ies_exemptions;
	public String ies_remarks;
	public String ies_enroll_confirm_remarks;
	public String ies_schedule;
	public String ies_itm_ref_materials_1;
	public String ies_itm_ref_materials_2;
	public String ies_itm_ref_materials_3;
	public String ies_itm_ref_materials_4;
	public String ies_itm_ref_materials_5;
	public String ies_itm_ref_url_1;
	public String ies_itm_ref_url_2;
	public String ies_itm_ref_url_3;
	public String ies_itm_ref_url_4;
	public String ies_itm_ref_url_5;
	public String ies_itm_rel_materials_1;
	public String ies_itm_rel_materials_2;
	public String ies_itm_rel_materials_3;
	public String ies_itm_rel_materials_4;
	public String ies_itm_rel_materials_5;
	public String ies_itm_rel_materials_6;
	public String ies_itm_rel_materials_7;
	public String ies_itm_rel_materials_8;
	public String ies_itm_rel_materials_9;
	public String ies_itm_rel_materials_10;
	public boolean ies_top_ind;
	public String ies_top_icon;
	public float ies_credit;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void ins(Connection con, long itm_id, Vector vExtensionColName, Vector vExtensionColType, Vector vExtensionColValue) throws SQLException {
		vExtensionColName.addElement("ies_itm_id");
		vExtensionColType.addElement(DbTable.COL_TYPE_LONG);
		vExtensionColValue.addElement(itm_id);

		DbTable dbTab = new DbTable(con);
		PreparedStatement stmt = dbTab.ins4AutoId("aeItemExtension", vExtensionColName, vExtensionColType, vExtensionColValue);
		if (stmt != null) {
			stmt.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public void upd(Connection con, long itm_id, Vector vExtensionColName, Vector vExtensionColType, Vector vExtensionColValue) throws SQLException {
		DbTable dbTab = new DbTable(con);
		if (vExtensionColName != null && vExtensionColName.size() > 0)
			dbTab.upd("aeItemExtension", vExtensionColName, vExtensionColType, vExtensionColValue, itm_id, "ies_itm_id");
	}

	public void del(Connection con, long itm_id) throws SQLException {
		String sql = " delete from aeItemExtension where ies_itm_id = ? ";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(null, stmt);
		}
	}

	public void get(Connection con, long itm_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = "select * from aeItemExtension inner join aeItem on (ies_itm_id = itm_id) where ies_itm_id = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.ies_lang = rs.getString("ies_lang");
				this.ies_objective = rs.getString("ies_objective");
				this.ies_contents = rs.getString("ies_contents");
				this.ies_duration = rs.getString("ies_duration");
				this.ies_audience = rs.getString("ies_audience");
				this.ies_prerequisites = rs.getString("ies_prerequisites");
				this.ies_exemptions = rs.getString("ies_exemptions");
				this.ies_remarks = rs.getString("ies_remarks");
				this.ies_enroll_confirm_remarks = rs.getString("ies_enroll_confirm_remarks");
				this.ies_schedule = rs.getString("ies_schedule");
				this.ies_itm_ref_materials_1 = rs.getString("ies_itm_ref_materials_1");
				this.ies_itm_ref_materials_2 = rs.getString("ies_itm_ref_materials_2");
				this.ies_itm_ref_materials_3 = rs.getString("ies_itm_ref_materials_3");
				this.ies_itm_ref_materials_4 = rs.getString("ies_itm_ref_materials_4");
				this.ies_itm_ref_materials_5 = rs.getString("ies_itm_ref_materials_5");
				this.ies_itm_ref_url_1 = rs.getString("ies_itm_ref_url_1");
				this.ies_itm_ref_url_2 = rs.getString("ies_itm_ref_url_2");
				this.ies_itm_ref_url_3 = rs.getString("ies_itm_ref_url_3");
				this.ies_itm_ref_url_4 = rs.getString("ies_itm_ref_url_4");
				this.ies_itm_ref_url_5 = rs.getString("ies_itm_ref_url_5");
				this.ies_itm_rel_materials_1 = rs.getString("ies_itm_rel_materials_1");
				this.ies_itm_rel_materials_2 = rs.getString("ies_itm_rel_materials_2");
				this.ies_itm_rel_materials_3 = rs.getString("ies_itm_rel_materials_3");
				this.ies_itm_rel_materials_4 = rs.getString("ies_itm_rel_materials_4");
				this.ies_itm_rel_materials_5 = rs.getString("ies_itm_rel_materials_5");
				this.ies_itm_rel_materials_6 = rs.getString("ies_itm_rel_materials_6");
				this.ies_itm_rel_materials_7 = rs.getString("ies_itm_rel_materials_7");
				this.ies_itm_rel_materials_8 = rs.getString("ies_itm_rel_materials_8");
				this.ies_itm_rel_materials_9 = rs.getString("ies_itm_rel_materials_9");
				this.ies_itm_rel_materials_10 = rs.getString("ies_itm_rel_materials_10");
				this.ies_top_ind = rs.getBoolean("ies_top_ind");
				this.ies_top_icon = rs.getString("ies_top_icon");
				this.ies_credit = rs.getFloat("ies_credit");
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public String getXml(Connection con, long itm_id) throws SQLException {
		this.get(con, itm_id);

		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<itm_ext>");
		xmlBuf.append("<ies_lang>").append(cwUtils.esc4XML(this.ies_lang)).append("</ies_lang>");
		xmlBuf.append("<ies_objective>").append(cwUtils.esc4XML(this.ies_objective)).append("</ies_objective>");
		xmlBuf.append("<ies_contents>").append(cwUtils.esc4XML(this.ies_contents)).append("</ies_contents>");
		xmlBuf.append("<ies_duration>").append(cwUtils.esc4XML(this.ies_duration)).append("</ies_duration>");
		xmlBuf.append("<ies_audience>").append(cwUtils.esc4XML(this.ies_audience)).append("</ies_audience>");
		xmlBuf.append("<ies_prerequisites>").append(cwUtils.esc4XML(this.ies_prerequisites)).append("</ies_prerequisites>");
		xmlBuf.append("<ies_exemptions>").append(cwUtils.esc4XML(this.ies_exemptions)).append("</ies_exemptions>");
		xmlBuf.append("<ies_remarks>").append(cwUtils.esc4XML(this.ies_remarks)).append("</ies_remarks>");
		xmlBuf.append("<ies_enroll_confirm_remarks>").append(cwUtils.esc4XML(this.ies_enroll_confirm_remarks)).append("</ies_enroll_confirm_remarks>");
		xmlBuf.append("<ies_schedule>").append(cwUtils.esc4XML(this.ies_schedule)).append("</ies_schedule>");
		xmlBuf.append("<ies_remarks>").append(cwUtils.esc4XML(this.ies_remarks)).append("</ies_remarks>");

		xmlBuf.append("<ies_itm_ref_materials_1>").append(cwUtils.esc4XML(this.ies_itm_ref_materials_1)).append("</ies_itm_ref_materials_1>");
		xmlBuf.append("<ies_itm_ref_materials_2>").append(cwUtils.esc4XML(this.ies_itm_ref_materials_2)).append("</ies_itm_ref_materials_2>");
		xmlBuf.append("<ies_itm_ref_materials_3>").append(cwUtils.esc4XML(this.ies_itm_ref_materials_3)).append("</ies_itm_ref_materials_3>");
		xmlBuf.append("<ies_itm_ref_materials_4>").append(cwUtils.esc4XML(this.ies_itm_ref_materials_4)).append("</ies_itm_ref_materials_4>");
		xmlBuf.append("<ies_itm_ref_materials_5>").append(cwUtils.esc4XML(this.ies_itm_ref_materials_5)).append("</ies_itm_ref_materials_5>");

		xmlBuf.append("<ies_itm_ref_url_1>").append(cwUtils.esc4XML(this.ies_itm_ref_url_1)).append("</ies_itm_ref_url_1>");
		xmlBuf.append("<ies_itm_ref_url_2>").append(cwUtils.esc4XML(this.ies_itm_ref_url_2)).append("</ies_itm_ref_url_2>");
		xmlBuf.append("<ies_itm_ref_url_3>").append(cwUtils.esc4XML(this.ies_itm_ref_url_3)).append("</ies_itm_ref_url_3>");
		xmlBuf.append("<ies_itm_ref_url_4>").append(cwUtils.esc4XML(this.ies_itm_ref_url_4)).append("</ies_itm_ref_url_4>");
		xmlBuf.append("<ies_itm_ref_url_5>").append(cwUtils.esc4XML(this.ies_itm_ref_url_5)).append("</ies_itm_ref_url_5>");

		xmlBuf.append("<ies_itm_rel_materials_1>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_1)).append("</ies_itm_rel_materials_1>");
		xmlBuf.append("<ies_itm_rel_materials_2>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_2)).append("</ies_itm_rel_materials_2>");
		xmlBuf.append("<ies_itm_rel_materials_3>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_3)).append("</ies_itm_rel_materials_3>");
		xmlBuf.append("<ies_itm_rel_materials_4>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_4)).append("</ies_itm_rel_materials_4>");
		xmlBuf.append("<ies_itm_rel_materials_5>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_5)).append("</ies_itm_rel_materials_5>");
		xmlBuf.append("<ies_itm_rel_materials_6>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_6)).append("</ies_itm_rel_materials_6>");
		xmlBuf.append("<ies_itm_rel_materials_7>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_7)).append("</ies_itm_rel_materials_7>");
		xmlBuf.append("<ies_itm_rel_materials_8>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_8)).append("</ies_itm_rel_materials_8>");
		xmlBuf.append("<ies_itm_rel_materials_9>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_9)).append("</ies_itm_rel_materials_9>");
		xmlBuf.append("<ies_itm_rel_materials_10>").append(cwUtils.esc4XML(this.ies_itm_rel_materials_10)).append("</ies_itm_rel_materials_10>");

		xmlBuf.append("<ies_top_ind>").append(this.ies_top_ind).append("</ies_top_ind>");
		xmlBuf.append("<ies_top_icon>").append(cwUtils.esc4XML(this.ies_top_icon)).append("</ies_top_icon>");
		xmlBuf.append("</itm_ext>");
		return xmlBuf.toString();
	}
}
package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSysMessage;

public class UserGrade {
	private static final String TYPE = "UGR";
	
	public long ugr_ent_id;
	public String ugr_display_bil;
	public String ugr_type;
	public long ugr_ent_id_root;
	public Timestamp ent_upd_date;
	public String ent_ste_uid;
	public long ugr_seq_no;
	public long ugr_tcr_id;
	public String ugr_tcr_title;
	public String ugr_code;
	
	public Vector vChild; // Vector of DbMailGroup
	
	public UserGrade() {
	}
	
	public static void updOrder(Connection con, String ugr_order) throws SQLException {
		StringTokenizer st = new StringTokenizer(ugr_order, ",");
		String ugr_ent_id;
		
		Vector vId = new Vector();
		while (st.hasMoreTokens()) {
			ugr_ent_id = st.nextToken().trim();
			vId.add(new Long(ugr_ent_id));
		}
		
		DbUserGrade userGrade = new DbUserGrade();
		for (int i=0; i<vId.size(); i++) {
			userGrade.ugr_ent_id = ((Long) vId.elementAt(i)).longValue();
			userGrade.ugr_seq_no = i+1;
			userGrade.updSeqNo(con);
		}
	}
	
	private static int index;
	
	public static StringBuffer getAllUserGradesAsXML(Connection con, loginProfile prof) throws SQLException {
		// Get only user grades whose ugr_default_ind = 0
		
		DbUserGrade root = DbUserGrade.getAllUserGrades(con, false, prof);
		index = 0;
		StringBuffer xml = getUserGradeAsXML(root);
		return xml;
	}
	
	public static StringBuffer getUserGradeAsXML(DbUserGrade ugr) {
		StringBuffer xml = new StringBuffer();
		boolean bRoot = false;
		if (index==0) {
			bRoot = true;
		}
		if (!bRoot) {
			xml.append("<item identifier=\"")
				.append("ITEM").append(index)
				.append("\" identifierref=\"").append(ugr.ugr_ent_id)
				.append("\" title=\"").append(dbUtils.esc4XML(ugr.ugr_display_bil))
				.append("\" itemtype=\"").append(TYPE)
				.append("\" restype=\"").append(TYPE)
				.append("\">");
		}
		else {
			xml.append("<tableofcontents identifier=\"TOC1")
				.append("\" title=\"").append(ugr.ugr_display_bil)
				.append("\">");
		}
		for (int i=0; i<ugr.vChild.size(); i++) {
			index = index+1;
			DbUserGrade child = (DbUserGrade) ugr.vChild.elementAt(i);
			xml.append(getUserGradeAsXML(child));
		}
		if (!bRoot) {
			xml.append("</item>");
		}
		else {
			xml.append("</tableofcontents>");
		}
		return xml;
	}
		
	/**
	  * ugr_ent_id should be set.
	  */
	public void get(Connection con) throws qdbException {
		DbUserGrade ugr = new DbUserGrade();
		ugr.ugr_ent_id = this.ugr_ent_id;
		ugr.get(con);
		this.ugr_display_bil = ugr.ugr_display_bil;
		this.ugr_type = ugr.ugr_type;
		this.ent_ste_uid = ugr.ent_ste_uid;
		this.ent_upd_date = ugr.ent_upd_date;
		this.ugr_seq_no = ugr.ugr_seq_no;
		this.ugr_tcr_id = ugr.ugr_tcr_id;
		this.ugr_tcr_title = ugr.ugr_tcr_title;
		this.ugr_code = ugr.ugr_code;
	}

	/**
	  * ugr_ent_id should be set.
	  */
	public StringBuffer asXML(Connection con) throws qdbException {
		StringBuffer xml = new StringBuffer();
		
		this.get(con);

		xml.append("<item ")
			.append("identifierref=\"").append(this.ugr_ent_id)
			.append("\" title=\"").append(dbUtils.esc4XML(this.ugr_display_bil))
			.append("\" grade_code=\"").append(dbUtils.esc4XML(this.ent_ste_uid))
			.append("\" ugr_seq_no=\"").append(this.ugr_seq_no)
			.append("\" ugr_tcr_id=\"").append(this.ugr_tcr_id)
			.append("\" ugr_tcr_title=\"").append(dbUtils.esc4XML(this.ugr_tcr_title))
			.append("\" ugr_code=\"").append(dbUtils.esc4XML(this.ugr_code))
			.append("\" itemtype=\"").append(TYPE)
			.append("\" restype=\"").append(TYPE)
			.append("\" timestamp=\"").append(ent_upd_date)
			.append("\"/>");
			
		return xml;
	}
	
	/**
	  * ugr_display_bil should be set.
	  */
	public void ins(Connection con, String create_usr_id)
		throws qdbException, SQLException, qdbErrMessage,cwSysMessage{
		long parent_ugr_ent_id = this.ugr_ent_id;
		
		// 1. Insert a record into UserGrade (and Entity) table
		DbUserGrade userGrade = new DbUserGrade();
		userGrade.ent_type = dbEntity.ENT_TYPE_USER_GRADE;
		userGrade.ent_ste_uid = this.ugr_display_bil;
		userGrade.ent_syn_ind = false;
		userGrade.ugr_display_bil = this.ugr_display_bil;
		userGrade.ugr_ent_id_root = this.ugr_ent_id_root;
		userGrade.ent_ste_uid = this.ent_ste_uid;
		userGrade.ugr_type = null;
		userGrade.ugr_tcr_id = this.ugr_tcr_id;
		userGrade.ugr_code=this.ugr_code;
		if (userGrade.exists(con)) {
			throw new qdbErrMessage(DbUserGrade.GRADE_CODE_ALREADY_EXISTS);
		}
		
		userGrade.ins(con);
		this.ugr_ent_id = userGrade.ugr_ent_id;
		
		if (parent_ugr_ent_id==0) {
			parent_ugr_ent_id = DbUserGrade.getGradeRoot(con, this.ugr_ent_id_root);
		}
		
		// 2. Create parent-child relationship in EntityRelation table
		dbEntityRelation dbEr = new dbEntityRelation();
		dbEr.ern_ancestor_ent_id = parent_ugr_ent_id;
		dbEr.ern_child_ent_id = this.ugr_ent_id;
		dbEr.ern_type = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
		dbEr.ern_syn_timestamp = cwSQL.getTime(con);
		dbEr.ern_remain_on_syn = false;
		dbEr.insEr(con, create_usr_id);
		
		//ins FullPath
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		entityfullpath.enclose(con, this.ugr_ent_id);
	}
	
	/**
	  * ugr_ent_id and ugr_display_bil should be set.
	  */
	public void upd(Connection con, String upd_usr_id)
		throws qdbException, cwException, SQLException, qdbErrMessage {
		DbUserGrade userGrade = new DbUserGrade();
		userGrade.ugr_ent_id = this.ugr_ent_id;
		userGrade.ugr_display_bil = this.ugr_display_bil;
		
		userGrade.ugr_ent_id_root = this.ugr_ent_id_root;
		userGrade.ent_ste_uid = this.ent_ste_uid;
		userGrade.ent_upd_date = this.ent_upd_date;
		userGrade.ugr_code=this.ugr_code;
		if (userGrade.exists2(con)) {
			throw new qdbErrMessage(DbUserGrade.GRADE_CODE_ALREADY_EXISTS);
		}
		userGrade.updDesc(con, upd_usr_id);
	}
	
	/**
	  * ugr_ent_id should be set.
	  */
	public StringBuffer getAffectedLrnSolListAsXML(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT itm_id, itm_code, itm_title FROM aeItem, aeItemTargetRuleDetail")
			.append(" WHERE itm_id = ird_itm_id ")
			.append(" AND ird_grade_id IN ")
			.append("( SELECT DISTINCT(ern_child_ent_id) FROM EntityRelation")
			.append(" WHERE ern_type=?")
			.append(" AND ern_parent_ind = ? AND ")
			.append("( ern_child_ent_id=? OR ern_ancestor_ent_id = ? ) ")
			.append(")")
			.append("ORDER BY itm_code ASC");
		
		
		PreparedStatement stmt;
		stmt = con.prepareStatement(sql.toString());
		int index=1;
		stmt.setString(index++, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
		stmt.setBoolean(index++, true);
		stmt.setLong(index++, this.ugr_ent_id);
		stmt.setLong(index++, this.ugr_ent_id);
		ResultSet rs = stmt.executeQuery();
		
		StringBuffer xml = new StringBuffer();
		xml.append("<affected_lrn_soln_list>");
		while (rs.next()) {
			xml.append("<item itm_id=\"")
				.append(rs.getLong("itm_id"))
				.append("\" itm_code=\"")
				.append(cwUtils.esc4XML(rs.getString("itm_code")))
				.append("\" itm_title=\"")
				.append(cwUtils.esc4XML(rs.getString("itm_title")))
				.append("\"/>");
		}
		xml.append("</affected_lrn_soln_list>");
		stmt.close();
				
		return xml;
	}
	
	/**
	  * ugr_ent_id should be set.
	  */
	public StringBuffer getAffectedUserListAsXML(Connection con) throws SQLException {
		ResultSet rs = getAffectedUserList(con);
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		StringBuffer xml = new StringBuffer();
		xml.append("<affected_user_list>");
		while (rs.next()) {
			long grade_id = rs.getLong("grade_id");
			long group_id = rs.getLong("group_id");
			xml.append("<user usr_ent_id=\"")
				.append(rs.getLong("usr_ent_id"))
				.append("\" usr_ste_usr_id=\"")
				.append(cwUtils.esc4XML(rs.getString("usr_ste_usr_id")))
				.append("\" usr_display_bil=\"")
				.append(cwUtils.esc4XML(rs.getString("usr_display_bil")))
				.append("\" grade=\"")
				.append(cwUtils.esc4XML(entityfullpath.getEntityName(con, grade_id)))
				.append("\" user_group=\"")
				.append(cwUtils.esc4XML(entityfullpath.getFullPath(con, group_id)))
				.append("\"/>");
		}
		xml.append("</affected_user_list>");
		
		return xml;
	}
	
	/**
	  * ugr_ent_id should be set.
	  */
	private ResultSet getAffectedUserList(Connection con) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ")
			.append("usr_ent_id, usr_ste_usr_id, usr_display_bil, ")
			.append("er_grade.ern_ancestor_ent_id grade_id, ")
			.append("er_group.ern_ancestor_ent_id group_id  ")
			.append("FROM ")
			.append("EntityRelation er_grade, RegUser, EntityRelation er_group ")
			.append("WHERE ")
			.append("er_grade.ern_child_ent_id=usr_ent_id AND ")
			.append("er_group.ern_child_ent_id=usr_ent_id AND ")
			.append("er_grade.ern_type=? AND ")
			.append("er_group.ern_type=? AND ")
			.append("er_grade.ern_ancestor_ent_id = ? and ")
			.append("er_group.ern_parent_ind = ? ")
			.append("ORDER BY usr_ste_usr_id ASC");
		
		PreparedStatement stmt;
		stmt = con.prepareStatement(sql.toString());
		int index=1;
		stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
		stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		stmt.setLong(index++, this.ugr_ent_id);
		stmt.setBoolean(index++, true);
		ResultSet rs = stmt.executeQuery();
		
		return rs;
	}
		
	/**
	  * ugr_ent_id should be set.
	  */
	public void del(Connection con, String del_usr_id)
		throws SQLException, qdbException, qdbErrMessage {
		DbUserGrade userGrade = new DbUserGrade();
		userGrade.ent_id = this.ugr_ent_id;
		userGrade.ent_upd_date = this.ent_upd_date;
		userGrade.checkTimeStamp(con);
		
		Timestamp now = dbUtils.getTime(con);
		userGrade.ent_upd_date = now;
		userGrade.ent_syn_ind = false;
		userGrade.upd(con);
		userGrade.del(con, del_usr_id, now);
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append(" SELECT DISTINCT ird_itr_id, ird_itm_id FROM aeItem, aeItemTargetRuleDetail")
			.append(" WHERE itm_id = ird_itm_id AND ird_grade_id IN ")
			.append(" (SELECT DISTINCT (ern_child_ent_id) FROM EntityRelation ")
			.append("  WHERE ern_type = ? ")
            .append("  AND ern_parent_ind = ? ")
            .append("  AND (ern_child_ent_id =? OR ern_ancestor_ent_id = ?))");

		Timestamp curTime = cwSQL.getTime(con);
		PreparedStatement stmt1;
		stmt1 = con.prepareStatement(sql1.toString());
		int index=1;
		stmt1.setString(index++, dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR);
		stmt1.setBoolean(index++, true);
		stmt1.setLong(index++, this.ugr_ent_id);
		stmt1.setLong(index++, this.ugr_ent_id);
		Hashtable hash = new Hashtable();
		ResultSet rs1 = stmt1.executeQuery();
		while(rs1.next()) {
			long ird_itr_id = rs1.getLong("ird_itr_id");
			long ird_itm_id = rs1.getLong("ird_itm_id");
			hash.put(new Long(ird_itr_id), new Long(ird_itm_id));
		}
		if (stmt1 != null) stmt1.close();
		
		StringBuffer sql2 = new StringBuffer();		
		sql2.append("DELETE FROM aeItemTargetRuleDetail WHERE ")
		.append("ird_itr_id=? AND ")
		.append("ird_itm_id =? ");
		
		
		StringBuffer sql3 = new StringBuffer();		
		sql3.append("DELETE FROM aeItemTargetRule WHERE ")
		.append("itr_id =? AND ")
		.append("itr_itm_id =? ");
		
	
		
		if(hash != null && !hash.isEmpty()) {
			//delete from aeItemTargetRuleDetail
			PreparedStatement stmt2 = con.prepareStatement(sql2.toString());
			Enumeration itr_keys = hash.keys();
			while(itr_keys.hasMoreElements()) {
				Long itr_id = (Long)itr_keys.nextElement();
				Long itr_itm_id = (Long)hash.get(itr_id);
				stmt2.setLong(1, itr_id.longValue());
				stmt2.setLong(2, itr_itm_id.longValue());
				stmt2.executeUpdate();
			}
			if(stmt2 != null) stmt2.close();
			stmt2.close();
			//delete from aeItemTargetRuleDetail
			PreparedStatement stmt3 = con.prepareStatement(sql3.toString());
			itr_keys = hash.keys();
			while(itr_keys.hasMoreElements()) {
				Long itr_id = (Long)itr_keys.nextElement();
				Long itr_itm_id = (Long)hash.get(itr_id);
				stmt3.setLong(1, itr_id.longValue());
				stmt3.setLong(2, itr_itm_id.longValue());
				stmt3.executeUpdate();
			}
			if(stmt3 != null) stmt3.close();
		}
		
		ResultSet rs2 = getAffectedUserList(con);
		Vector vUsrEntId = new Vector();
		while (rs2.next()) {
			vUsrEntId.add(new Long(rs2.getLong("usr_ent_id")));	
		}

		dbEntityRelation er = new dbEntityRelation();
		er.ern_child_ent_id = this.ugr_ent_id;
		er.ern_type = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
		er.delAsChild(con, del_usr_id, null);

		er.ern_ancestor_ent_id = this.ugr_ent_id;
		er.ern_type = dbEntityRelation.ERN_TYPE_UGR_PARENT_UGR;
		er.delAsAncestor(con, del_usr_id);
		
		DbUserGrade defaultGrade = DbUserGrade.getDefaultGrade(con, this.ugr_ent_id_root);
		
		// Create user-grade relationship in EntityRelation table
		for (int i=0; i<vUsrEntId.size(); i++) { 
		 	er.ern_ancestor_ent_id = defaultGrade.ugr_ent_id;
		 	er.ern_child_ent_id = ( (Long) vUsrEntId.elementAt(i)).longValue();
		 	er.ern_type = dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR;
		 	er.ern_syn_timestamp = curTime;
		 	er.ern_remain_on_syn = false;
			
		 	er.insEr(con, del_usr_id);
		}
		//根据ugr_ent_id删除userGrade中的记录
		String sql4="delete from UserGrade where ugr_ent_id= ?";
		PreparedStatement deleteStatement=con.prepareStatement(sql4);
		deleteStatement.setLong(1, this.ugr_ent_id);
		deleteStatement.executeUpdate();
		deleteStatement.close();
	}
	
	public static String getGradesAsXmlForProfession(Connection con) throws SQLException {
		StringBuffer sb = new StringBuffer();
		String sql = "select ugr_ent_id, ugr_display_bil from userGrade where ugr_type is null and ugr_default_ind = 0";
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		sb.append("<ugr_list>");
		while(rs.next()) {
			sb.append("<ugr>");
			sb.append("<ugr_ent_id>").append(rs.getLong("ugr_ent_id")).append("</ugr_ent_id>");
			sb.append("<ugr_display_bil>").append(rs.getString("ugr_display_bil")).append("</ugr_display_bil>");
			sb.append("</ugr>");
		}
		sb.append("</ugr_list>");
		rs.close();
		stmt.close();
		return sb.toString();
	}
	
	public static String getTitleById(Connection con, long ugr_id) throws SQLException {
		String sql = "select ugr_display_bil from userGrade where ugr_ent_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, ugr_id);
		ResultSet rs = stmt.executeQuery();
		String result = null;
		if(rs.next()) {
			result = rs.getString("ugr_display_bil");
		}
		rs.close();
		stmt.close();
		return result;
	}
	
}

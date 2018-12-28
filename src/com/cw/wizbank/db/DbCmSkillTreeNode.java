/*
 * Created on 2003/5/19
 *
 */
package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;
/**
 * @author WaiLun
 *
 */
public class DbCmSkillTreeNode extends DbCmSkillBase {
	
	/* Data base Field */
	public long stn_skb_id;
	
	

	/**
	 * Insert a competency tree node
	 * @param con database connection
	 * @return row count
	 * @throws SQLException
	 * @throws cwException
	 */
	public int ins(Connection con)
		throws SQLException, cwException {
			this.skb_type = this.COMPETENCY_GROUP; 
			DbCmSkillBase.ins(con, this);
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_TREE_NODE_INS);
			stmt.setLong(1, this.skb_id);
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ) {
				stmt.close();
				throw new cwException("Failed to insert a skill tree node");
			}
			stmt.close();
			return count;
			
		}
		
	public void get(Connection con)
		throws SQLException, cwSysMessage {
			get(con, false);
			return;		
		}
		
	public void get(Connection con, boolean bLoadScale)
		throws SQLException, cwSysMessage {

			super.get(con, bLoadScale);
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_TREE_NODE_GET_ALL);
			stmt.setLong(1, this.skb_id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				this.stn_skb_id = rs.getLong("stn_skb_id");		
			} else {
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill tree node id = " + this.stn_skb_id);
			}
			stmt.close();
			
			return;
		}
		
			
		
	public int upd(Connection con)
		throws SQLException, cwSysMessage {
			int count = super.upd(con);
			return count;
		}
	
	public int del(Connection con)
		throws SQLException, cwSysMessage {
			
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_TREE_NODE_DEL);
			stmt.setLong(1, this.skb_id);
			int count = 0;
			if( (count = stmt.executeUpdate()) != 1 ){
				stmt.close();
				throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " skill tree node id = " + this.skb_id);
			}
			stmt.close();
			
			super.del(con);
			
			return count;
		}

	public int softDel(Connection con)
		throws SQLException, cwSysMessage{
			
			int count = super.softDel(con);
			return count;

		}


	public String asXML(){

		StringBuffer xml = new StringBuffer(1024);
		xml.append("<comp_group ")
			.append(" skb_id=\"").append(this.skb_id).append("\" ")
			.append(" skb_owner_ent_id=\"").append(this.skb_owner_ent_id).append("\" ")
			.append(" skb_parent_skb_id=\"").append(this.skb_parent_skb_id).append("\" ")
			.append(" skb_ssl_id=\"").append(this.skb_ssl_id).append("\" ")
			.append(" skb_order=\"").append(this.skb_order).append("\" ")
			.append(" skb_type=\"").append(this.skb_type).append("\" ")
			.append(" skb_create_usr_id=\"").append(this.skb_create_usr_id).append("\" ")
			.append(" skb_create_timestamp=\"").append(this.skb_create_timestamp).append("\" ")
			.append(" skb_update_usr_id=\"").append(this.skb_update_usr_id).append("\" ")
			.append(" skb_update_timestamp=\"").append(this.skb_update_timestamp).append("\" ")
			.append(" skb_delete_usr_id=\"").append(this.skb_delete_usr_id).append("\" ")
			.append(" skb_delete_timestamp=\"").append(this.skb_delete_timestamp).append("\" ")
			.append(">")
			.append("<skb_title>").append(cwUtils.esc4XML(this.skb_title)).append("</skb_title>")
			.append("<skb_desc>").append(cwUtils.esc4XML(this.skb_description)).append("</skb_desc>")
			.append("<skb_ancestor>").append(this.skb_ancestor).append("</skb_ancestor>");
		if( this.cmScale != null ){
			xml.append("<scale id=\"").append(this.cmScale.ssl_id).append("\">")
				.append("<ssl_title>").append(cwUtils.esc4XML(this.cmScale.ssl_title)).append("</ssl_title>")
				.append("</scale>");
		}
		
		xml.append("</comp_group>");
		return xml.toString();

	}
		
	
	
	public static String getTitleListAsXML(Connection con, long root_ent_id)
		throws SQLException {
			
			StringBuffer xml = new StringBuffer();
			PreparedStatement stmt = con.prepareStatement(SqlStatements.SQL_CM_SKILL_TREE_NODE_GET_TITLE_LIST);
			stmt.setString(1, DbCmSkillBase.COMPETENCY_GROUP);
			stmt.setLong(2, root_ent_id);
			ResultSet rs = stmt.executeQuery();
			xml.append("<comp_group_list>");
			while(rs.next()){
				xml.append("<comp_group ")
					.append("id=\"").append(rs.getLong("skb_id")).append("\">")
					.append("<title>").append(cwUtils.esc4XML(rs.getString("skb_title"))).append("</title>")
					.append("</comp_group>");
			}
			xml.append("</comp_group_list>");
			stmt.close();
			return xml.toString();
			
		}
	
	
	public static Vector getChild(Connection con, long parent_skb_id, String skb_type, cwPagination cwPage, Vector v_skb_id)
		throws SQLException, cwException {
			
			Vector v_skb = new Vector();
			StringBuffer SQLBuf = new StringBuffer();
			SQLBuf.append(SqlStatements.SQL_CM_TREE_NODE_GET_CHILD_SKILL_BASE);
			if( v_skb_id != null && !v_skb_id.isEmpty()) {
				SQLBuf.append(" And skb_id In ").append(cwUtils.vector2list(v_skb_id));
			}
			SQLBuf.append(" Order By ").append(cwPage.sortCol)
					.append(" ").append(cwPage.sortOrder);
			
			PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());			
			stmt.setLong(1, parent_skb_id);
			stmt.setString(2, skb_type);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				DbCmSkill skill = new DbCmSkill();
				skill.skb_id = rs.getLong("skb_id");
				skill.skb_type = rs.getString("skb_type");
				skill.skb_title = rs.getString("skb_title");
				skill.skb_description = rs.getString("skb_description");
				skill.skb_ssl_id = rs.getLong("skb_ssl_id");
				skill.skl_xml = rs.getString("skl_xml");
				skill.skl_derive_rule = rs.getString("skl_derive_rule");
				skill.skl_rating_ind = rs.getBoolean("skl_rating_ind");
				v_skb.addElement(skill);
			}
			stmt.close();
			return v_skb;
			
		}
	
}

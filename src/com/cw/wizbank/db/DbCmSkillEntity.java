package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;

import com.cw.wizbank.util.cwSQL;

public class DbCmSkillEntity {
	
	public static String SOURCE_WIZBANK="WIZBANK";
	public static String SOURCE_SYN="SYN";
	
	public static long ins(Connection con ,String type ) throws SQLException{
		String sql="insert into cmSkillEntity(ske_type) values(?)";

		PreparedStatement stmt = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
		long skt_id=0;
		int index=1;
		stmt.setString(index++, type);
		int cnt=stmt.executeUpdate();
		if(cnt !=1){
			stmt.close();
			con.rollback();
		}else{
			skt_id=cwSQL.getAutoId(con, stmt, "cmSkillEntity", "ske_id");
		}
		if(stmt!=null)stmt.close();
		return skt_id;
	}
	
	public static void del(Connection con ,String ske_id_lst)throws SQLException{
		String sql="delete from cmSkillEntity  where ske_id in " +ske_id_lst;
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.executeUpdate();
		if(stmt!=null)stmt.close();
	}
	
	public static Hashtable getDisplayName(Connection con ,String ske_id_lst)throws SQLException{
		String sql="select "+cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)+" parent_skb_title, sks_ske_id ske_id,sks_title title from cmSkillSet ";
		if (ske_id_lst != null && ske_id_lst.length() > 0) {
			sql += " where sks_ske_id in (" + ske_id_lst + ")";
		}
		sql += " union" +
			   " select parent.skb_title parent_skb_title,sk.skb_ske_id ske_id,sk.skb_title title From cmSkillBase sk, cmSkillBase parent" +
			   " where sk.skb_type = ? and sk.skb_delete_timestamp is null  and sk.skb_parent_skb_id = parent.skb_id " +
			   " and parent.skb_type = ? ";
		if (ske_id_lst != null && ske_id_lst.length() > 0) {
			sql += "and sk.skb_ske_id in (" + ske_id_lst + ")";
		}
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1,  DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL);
		stmt.setString(2,  DbCmSkillBase.COMPETENCY_GROUP);
		ResultSet rs =stmt.executeQuery();
		Hashtable disHs =new Hashtable();
		while(rs.next()){
			String parent=rs.getString("parent_skb_title");
			String title="";
			if(parent!=null){
				title=parent+"/"+rs.getString("title");
			}else{
				title=rs.getString("title");
			}
			disHs.put(new Long(rs.getLong("ske_id")), title);
		}
		if(stmt!=null)stmt.close();
		return disHs;	
	}
	
	public static Hashtable getDisplayNameNew(Connection con ,String ske_id_lst)throws SQLException{
		String sql = "select usr_ent_id, usr_job_title, usr_display_bil from UserPosition where usr_ent_id in (" + ske_id_lst + ")";
		PreparedStatement stmt = con.prepareStatement(sql);
//		stmt.setString(1,  DbCmSkillBase.COMPETENCY_COMPOSITE_SKILL);
//		stmt.setString(2,  DbCmSkillBase.COMPETENCY_GROUP);
		ResultSet rs =stmt.executeQuery();
		Hashtable disHs =new Hashtable();
		while(rs.next()){
			String usr_job_title = rs.getString("usr_job_title");
			String usr_display_bil = rs.getString("usr_display_bil");
			usr_job_title = usr_job_title + "(" + usr_display_bil + ")";
			long usr_ent_id = rs.getLong("usr_ent_id");
			disHs.put(usr_ent_id, usr_job_title);
		}
		if(stmt!=null)stmt.close();
		return disHs;	
	}
	
	
}

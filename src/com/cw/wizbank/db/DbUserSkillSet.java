package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwUtils;

public class DbUserSkillSet{
	public static Vector getUserBysKillSet(Connection con ,String skb_id_lst) throws SQLException{
		String sql=" select usr_ent_id, usr_display_bil, usr_ste_usr_id ,ern_ancestor_ent_id from reguserSkillSet,reguser,cmSkillSet,entityrelation" +
				" where uss_ent_id=usr_ent_id and uss_ske_id=sks_ske_id " +
				" and ern_type='USR_PARENT_USG' and ern_parent_ind=1 and ern_child_ent_id= usr_ent_id" +
				" and sks_skb_id in "+skb_id_lst;
		PreparedStatement stmt = con.prepareStatement(sql);
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		ResultSet rs = stmt.executeQuery();
		Vector userVc = new Vector();
		while(rs.next()){
			 loginProfile user =new loginProfile();
			 user.usr_ent_id = rs.getLong("usr_ent_id");
			 user.usr_display_bil = rs.getString("usr_display_bil");
			 user.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
			 long group_id=rs.getLong("ern_ancestor_ent_id");	
			 user.directGroup =entityfullpath.getFullPath(con, group_id);
			 userVc.add(user);
		}
		if(stmt !=null)stmt.close();
		return userVc;
	}
	
	public static String getSkillSetXmlById(Connection con ,String skb_id_lst)throws SQLException{
		String sql="select sks_title,sks_skb_id,sks_ske_id from cmskillset where sks_skb_id in"+skb_id_lst;
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		String xml="<skillset_lst>";
		while(rs.next()){
			xml=xml+"<skillset sks_skb_id=\""+rs.getLong("sks_skb_id")+"\" sks_title=\""+cwUtils.esc4XML(rs.getString("sks_title"))+"\" sks_ske_id=\""+rs.getLong("sks_ske_id")+"\"/>";
		}
		 xml+="</skillset_lst>";
		if(stmt !=null)stmt.close();
		 return xml;
	}
	
	public static void delUserSkillSet(Connection con, String ske_id_lst) throws SQLException{
		String sql="delete from reguserSkillSet where uss_ske_id in"+ske_id_lst;
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.executeUpdate();
		if(stmt !=null)stmt.close();
	}
}

package com.cw.wizbank.ae.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;

public class DbItemTargetRuleDetail {
    public static final String IRD_TYPE_TARGET_LEARNER = "TARGET_LEARNER";
    public static final String IRD_TYPE_TARGET_ENROLLMENT = "TARGET_ENROLLMENT";

    public final static String ITE_TYPE_TARGETED_PREVIEW = "TARGETED_PREVIEW";
    public final static String ITE_ELECTIVE = "ELECTIVE";
    public final static String ITE_COMPULSORY = "COMPULSORY";
    
	public long ird_itm_id;
	public long ird_itr_id;
	public long ird_group_id;
	public long ird_grade_id;
	public long ird_upt_id;
	public String ird_type;
	public String ird_create_usr_id;
	public Timestamp ird_create_timestamp;
	public String ird_update_usr_id;
	public Timestamp ird_update_timestamp;
	
	public void ins(Connection con) throws SQLException {
		String sql = "insert into aeItemTargetRuleDetail (ird_itm_id, ird_itr_id, ird_group_id, ird_grade_id, ird_type, ird_create_usr_id, ird_create_timestamp, ird_update_usr_id, ird_update_timestamp,ird_upt_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, ird_itm_id);
		stmt.setLong(index++, ird_itr_id);
		stmt.setLong(index++, ird_group_id);
		stmt.setLong(index++, ird_grade_id);
		stmt.setString(index++, ird_type);
		stmt.setString(index++, ird_create_usr_id);
		stmt.setTimestamp(index++, ird_create_timestamp);
		stmt.setString(index++, ird_update_usr_id);
		stmt.setTimestamp(index++, ird_update_timestamp);
		stmt.setLong(index++, ird_upt_id);
		stmt.executeUpdate();
        stmt.close();
	}

	public void delByItem(Connection con) throws SQLException {
		String sql = "delete from aeItemTargetRuleDetail where ird_itm_id = ? and ird_type=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, ird_itm_id);
		stmt.setString(index++, ird_type);
        stmt.executeUpdate();
        stmt.close();
	}
	
	public void del(Connection con) throws SQLException {
		String sql = "delete from aeItemTargetRuleDetail where ird_itr_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, ird_itr_id);
        stmt.executeUpdate();
        stmt.close();
	}
	
	public static boolean hasRecord(Connection con, long itm_id, String type) throws SQLException {
		boolean ret = false;
		StringBuffer sql = new StringBuffer(512);
		sql.append(" select * from aeItemTargetRuleDetail")
			.append(" where ird_itm_id = ?");
		if(type != null) {
			sql.append(" and ird_type = ?");
		}
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, itm_id);
		if(type != null) {
			stmt.setString(index++, type);
		}
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			ret = true;
		}
		rs.close();
		stmt.close();
		return ret;
	}
	
	public static  Vector getItmBySkillSet(Connection con,String skb_id_lst) throws SQLException {
		String sql ="select itm_id,itm_code, itm_title from aeItemTargetRuleDetail,aeitem,cmSkillSet" +
				" where ird_upt_id=sks_ske_id and ird_itm_id = itm_id and sks_skb_id in "+skb_id_lst;	
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		Vector itmVc =new Vector();
		while(rs.next()) {
			aeItem itm =new aeItem();
			itm.itm_id = rs.getLong("itm_id");
			itm.itm_code = rs.getString("itm_code");
			itm.itm_title = rs.getString("itm_title");
			itmVc.add(itm);
		}
		if(stmt !=null)stmt.close();
		return itmVc;
	}
	
	public static void delItmBySkillSet(Connection con,String ske_id_lst) throws SQLException{
		String sql="delete from aeItemTargetRuleDetail where ird_upt_id in "+ske_id_lst;
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.executeUpdate();
		if(stmt !=null)stmt.close();
	}

}

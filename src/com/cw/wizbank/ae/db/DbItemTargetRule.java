package com.cw.wizbank.ae.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.util.cwSQL;

public class DbItemTargetRule {
	public long itr_id;
	public long itr_itm_id;
	public String itr_group_id;
	public String itr_grade_id;
	public String itr_upt_id;
	public String itr_type;
	public String itr_create_usr_id;
	public Timestamp itr_create_timestamp;
	public String itr_update_usr_id;
	public Timestamp itr_update_timestamp;
	public Timestamp last_update_timestamp;
	public int itr_group_ind;
	public int itr_grade_ind;
	public int itr_position_ind;
	public int itr_compulsory_ind;
	public String rulesource = "1";//规则来源   1课程规则2岗位规则3职级规则 

	public void ins(Connection con) throws SQLException {
		String sql = "insert into aeItemTargetRule (itr_itm_id, itr_group_id, itr_grade_id, itr_type, itr_create_usr_id, itr_create_timestamp, itr_update_usr_id, itr_update_timestamp, itr_upt_id, itr_group_ind, itr_grade_ind, itr_position_ind,itr_compulsory_ind) values (?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?,?)";
		PreparedStatement stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		int index = 1;
		stmt.setLong(index++, itr_itm_id);
		stmt.setString(index++, itr_group_id);
		stmt.setString(index++, itr_grade_id);
		stmt.setString(index++, itr_type);
		stmt.setString(index++, itr_create_usr_id);
		stmt.setTimestamp(index++, itr_create_timestamp);
		stmt.setString(index++, itr_update_usr_id);
		stmt.setTimestamp(index++, itr_update_timestamp);
		stmt.setString(index++, itr_upt_id);
		stmt.setInt(index++, itr_group_ind);
		stmt.setInt(index++, itr_grade_ind);
		stmt.setInt(index++, itr_position_ind);
		stmt.setInt(index++, itr_compulsory_ind);
		
        stmt.executeUpdate();
        itr_id = cwSQL.getAutoId(con, stmt, "aeItemTargetRule", "itr_id");
        stmt.close();
	}
	
	public void upd(Connection con) throws SQLException {
		String sql = "update aeItemTargetRule set itr_group_id = ?, itr_grade_id = ?, itr_update_usr_id = ?, itr_update_timestamp = ? ,itr_upt_id=?,itr_group_ind =?, itr_grade_ind =?, itr_position_ind = ?,itr_compulsory_ind=? where itr_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, itr_group_id);
		stmt.setString(index++, itr_grade_id);
		stmt.setString(index++, itr_update_usr_id);
		stmt.setTimestamp(index++, itr_update_timestamp);
		stmt.setString(index++, itr_upt_id);
		stmt.setInt(index++, itr_group_ind);
        stmt.setInt(index++, itr_grade_ind);
        stmt.setInt(index++, itr_position_ind);
        stmt.setInt(index++, itr_compulsory_ind);
        
		stmt.setLong(index++, itr_id);
        stmt.executeUpdate();
        stmt.close();
	}

	public void del(Connection con) throws SQLException {
		String sql = "delete from aeItemTargetRule where itr_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itr_id);
        stmt.executeUpdate();
        stmt.close();
	}
	
	public void delByItem(Connection con) throws SQLException {
		String sql = "delete from aeItemTargetRule where itr_itm_id = ? and itr_type=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itr_itm_id);
		stmt.setString(index++, itr_type);
        stmt.executeUpdate();
        stmt.close();
	}

	public static Vector get(Connection con, long itm_id, String type) throws SQLException {
		Vector vec = new Vector();
		String sql = "select * from aeItemTargetRule where itr_itm_id = ? and itr_type = ? order by itr_update_timestamp desc";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itm_id);
		stmt.setString(index++, type);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			DbItemTargetRule itr = new DbItemTargetRule();
			itr.itr_id = rs.getLong("itr_id");
			itr.itr_itm_id = rs.getLong("itr_itm_id");
			itr.itr_group_id = rs.getString("itr_group_id");
			itr.itr_grade_id = rs.getString("itr_grade_id");
			itr.itr_type = rs.getString("itr_type");
			itr.itr_create_usr_id = rs.getString("itr_create_usr_id");
			itr.itr_create_timestamp = rs.getTimestamp("itr_create_timestamp");
			itr.itr_update_usr_id = rs.getString("itr_update_usr_id");
			itr.itr_update_timestamp = rs.getTimestamp("itr_update_timestamp");
			itr.itr_upt_id = rs.getString("itr_upt_id");
			itr.itr_upt_id = rs.getString("itr_upt_id");
            itr.itr_group_ind = rs.getInt("itr_group_ind");
            itr.itr_grade_ind = rs.getInt("itr_grade_ind");
            itr.itr_position_ind = rs.getInt("itr_position_ind");
            itr.itr_compulsory_ind = rs.getInt("itr_compulsory_ind");
            
			vec.addElement(itr);
		}
        rs.close();
        stmt.close();
		return vec;
	}

	
	/**
	 * 根据岗位学习地图获取课程规则信息  damon0331
	 * @param con
	 * @param itm_id 要查询的课程ID
	 * @return
	 * @throws SQLException
	 */
	public static Vector getByPst(Connection con, long itm_id) throws SQLException {
		Vector vec = new Vector();
		String sql = "select upi.*,upm.* from UserPositionLrnItem upi join UserPositionLrnMap upm on upi.upi_upm_id = upm.upm_id  where upi_itm_id = ? and upm.upm_status = 1 order by upi_id desc";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itm_id);
//		stmt.setString(index++, type);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			DbItemTargetRule itr = new DbItemTargetRule();
			//不能保存itr_id，后续会判断该值是否为空来执行不同sql
//			itr.itr_id = rs.getLong("upi_id");
			itr.itr_itm_id = rs.getLong("upi_itm_id");
			itr.itr_group_id = "-1";
			itr.itr_grade_id = "-1";
//			itr.itr_type = rs.getString("itr_type");
			itr.itr_create_usr_id = rs.getString("upm_create_usr_id");
			itr.itr_create_timestamp = rs.getTimestamp("upm_update_time");
			itr.itr_update_usr_id = rs.getString("upm_update_usr_id");
//			itr.itr_update_timestamp = rs.getTimestamp("itr_update_timestamp");
			//岗位ID
			itr.itr_upt_id = rs.getString("upm_upt_id");
//            itr.itr_group_ind = rs.getInt("itr_group_ind");
//            itr.itr_grade_ind = rs.getInt("itr_grade_ind");
            itr.itr_position_ind = 1;
            //是否必修
            itr.itr_compulsory_ind = 1;
            itr.rulesource = "2";//岗位规则
            
			vec.addElement(itr);
		}
        rs.close();
        stmt.close();
		return vec;
	}
	
	/**
	 * 根据职级学习地图获取课程规则信息  damon0401
	 * @param con
	 * @param itm_id 要查询的课程ID
	 * @return
	 * @throws SQLException
	 */
	public static Vector getByProf(Connection con, long itm_id) throws SQLException {
		Vector vec = new Vector();
		String sql = "select psi.*,pfs.* from ProfessionLrnItem psi join profession pfs on psi.psi_pfs_id = pfs.pfs_id where psi_itm_id = ? and pfs.pfs_status = 1 order by psi_id desc";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itm_id);
//		stmt.setString(index++, type);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			DbItemTargetRule itr = new DbItemTargetRule();
			//不能保存itr_id，后续会判断该值是否为空来执行不同sql
//			itr.itr_id = rs.getLong("psi_id");
			itr.itr_itm_id = rs.getLong("psi_itm_id");
			itr.itr_group_id = "-1";
			itr.itr_upt_id = "-1";
//			itr.itr_type = rs.getString("itr_type");
			itr.itr_create_usr_id = rs.getString("pfs_create_usr_id");
			itr.itr_create_timestamp = rs.getTimestamp("pfs_update_time");
			itr.itr_update_usr_id = rs.getString("pfs_update_usr_id");
//			itr.itr_update_timestamp = rs.getTimestamp("itr_update_timestamp");
			//职级ID
			itr.itr_grade_id = rs.getString("psi_ugr_id");
//            itr.itr_group_ind = rs.getInt("itr_group_ind");
//            itr.itr_grade_ind = rs.getInt("itr_grade_ind");
            itr.itr_grade_ind = 1;
            //是否必修
            itr.itr_compulsory_ind = 1;
            itr.rulesource = "3";//职级规则
            
			vec.addElement(itr);
		}
		
        rs.close();
        stmt.close();
		return vec;
	}
	
	public static DbItemTargetRule getById(Connection con, long itr_id) throws SQLException {
		DbItemTargetRule itr = new DbItemTargetRule();
		String sql = "select * from aeItemTargetRule where itr_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itr_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			itr.itr_id = rs.getLong("itr_id");
			itr.itr_itm_id = rs.getLong("itr_itm_id");
			itr.itr_group_id = rs.getString("itr_group_id");
			itr.itr_grade_id = rs.getString("itr_grade_id");
			itr.itr_type = rs.getString("itr_type");
			itr.itr_create_usr_id = rs.getString("itr_create_usr_id");
			itr.itr_create_timestamp = rs.getTimestamp("itr_create_timestamp");
			itr.itr_update_usr_id = rs.getString("itr_update_usr_id");
			itr.itr_update_timestamp = rs.getTimestamp("itr_update_timestamp");
			itr.itr_upt_id = rs.getString("itr_upt_id");
			itr.itr_group_ind = rs.getInt("itr_group_ind");
			itr.itr_grade_ind = rs.getInt("itr_grade_ind");
			itr.itr_position_ind = rs.getInt("itr_position_ind");
			itr.itr_compulsory_ind = rs.getInt("itr_compulsory_ind");
		}
        rs.close();
        stmt.close();
		return itr;
	}
	
	public static Timestamp getLastTimestamp(Connection con, long itm_id, String type) throws SQLException {
		Timestamp last_upd_time = null;;
		String sql = "select * from aeItemTargetRule where itr_itm_id = ? and itr_type = ? order by itr_update_timestamp desc";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itm_id);
		stmt.setString(index++, type);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			last_upd_time = rs.getTimestamp("itr_update_timestamp");
		}
		rs.close();
		stmt.close();
		return last_upd_time;
	}
	
}

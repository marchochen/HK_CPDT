package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.entity.CpdRegistration;

public class dbCPDType {

	public static boolean isTypeExist(Connection con, String temp) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		boolean flag = false;
		try{
			sql.append("select count(ct_id) from cpdType where ct_license_type = ?");
			pst = con.prepareStatement(sql.toString());
			pst.setString(1, temp);
			rs = pst.executeQuery();
			while(rs.next()){
				if(rs.getInt(1) > 0){
					flag = true;
				}
			}
		}finally{
			cwSQL.cleanUp(rs, pst);
		}
		return flag;
	}

	public static boolean isGroupExist(Connection con, String temp,
			String license_type) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		boolean flag = false;
		try{
			sql.append("select count(cg_code) from cpdGroup where cg_code = ? and cg_ct_id = (select ct_id from cpdType where  ct_status = 'OK'  and  ct_license_type = ?)");
			pst = con.prepareStatement(sql.toString());
			pst.setString(1,temp);
			pst.setString(2,license_type);
			rs = pst.executeQuery();
			while(rs.next()){
				if(rs.getInt(1) > 0){
					flag = true;
				}
			}
		}finally{
			cwSQL.cleanUp(rs, pst);
		}
		return flag;
	}

	public static CpdRegistration getGroupRegistration(Connection con,
			String user_id, String license_type) throws SQLException {
		//select * from cpdRegistration where cr_usr_ent_id = (select usr_ent_id from reguser where usr_ste_usr_id = #{usr_ste_usr_id}) and cr_ct_id = (select ct_id from cpdType where ct_license_type = #{license_type})
		PreparedStatement pst = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		CpdRegistration cr = new CpdRegistration();
		try{
			sql.append("select cr_reg_datetime from cpdRegistration where cr_usr_ent_id = (select usr_ent_id from reguser where usr_ste_usr_id = ?) and cr_ct_id = (select ct_id from cpdType where  ct_status = 'OK'  and  ct_license_type = ?)");
			pst = con.prepareStatement(sql.toString());
			pst.setString(1,user_id);
			pst.setString(2,license_type);
			rs = pst.executeQuery();
			if(rs.next()){
				cr.cr_reg_datetime = rs.getTimestamp("cr_reg_datetime");
			}
		}finally{
			cwSQL.cleanUp(rs, pst);
		}
		return null;
	}

}

package com.cw.wizbank.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class EvalAccess {
	public long eac_res_id;

	public long eac_target_ent_id;

	public int eac_order;

	public Timestamp eac_create_timestamp;

	public String eac_create_usr_id;

	public EvalAccess() {
	}

	public void batchAppendEvalAccess(Connection con, long eva_res_id,
			long[] target_ent_id, String create_usr_id) throws SQLException {
		if (target_ent_id != null) {			
			this.eac_res_id = eva_res_id;
			this.eac_create_timestamp = cwSQL.getTime(con);
			this.eac_create_usr_id = create_usr_id;
			for (int i = 0; i < target_ent_id.length; i++) {
				this.eac_target_ent_id = target_ent_id[i];
				this.eac_order = i + 1;
				this.addEvalAccess(con);
				
			}
		}
	}

	public void addEvalAccess(Connection con) throws SQLException {
		String sql = "insert into EvalAccess(eac_res_id,eac_target_ent_id,eac_order,eac_create_timestamp,eac_create_usr_id)"
				+ " values(?,?,?,?,?)";
		PreparedStatement pstmt = con.prepareStatement(sql);
		int index = 1;
		pstmt.setLong(index++, eac_res_id);
		pstmt.setLong(index++, eac_target_ent_id);
		pstmt.setLong(index++, eac_order);
		pstmt.setTimestamp(index++, eac_create_timestamp);
		pstmt.setString(index, eac_create_usr_id);
		pstmt.executeUpdate();
		pstmt.close();
	}

	public void delEvalAccessByRes_ID(Connection con) throws SQLException {
		String sql = "delete from EvalAccess where eac_res_id = ?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		int index = 1;
		pstmt.setLong(index++, eac_res_id);
		pstmt.executeUpdate();
		pstmt.close();
	}
	
	public String getTargetDisplayByRes_IDASXML(Connection con) throws SQLException{
		StringBuffer xml = new StringBuffer();
		String sql = "(select usg_ent_id,usg_display_bil,eac_order, '" + dbEntity.ENT_TYPE_USER_GROUP + "' as ent_type from EvalAccess,UserGroup " +
					 "where usg_ent_id = eac_target_ent_id and eac_res_id = ?)"+
					 "union "+
					 "(select usr_ent_id,usr_display_bil,eac_order, '" + dbEntity.ENT_TYPE_USER + "' as ent_type from EvalAccess,RegUser " +
					 "where usr_ent_id = eac_target_ent_id and eac_res_id = ?) order by ent_type desc";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setLong(1,this.eac_res_id);
		pstmt.setLong(2,this.eac_res_id);
		ResultSet rs = pstmt.executeQuery();
		
		xml.append("<eval_target_lst>");
		EntityFullPath entityFullePath = EntityFullPath.getInstance(con);
		while(rs.next()){
			long entId = rs.getLong(1);
			String entDisplayBil = rs.getString(2);
			String type = rs.getString("ent_type");
			if(dbEntity.ENT_TYPE_USER_GROUP.equals(type)) {
				entDisplayBil = entityFullePath.getFullPath(con, entId);
				entDisplayBil = entDisplayBil.replaceAll("/", ">");
			} 
			xml.append("<target ent_id=\""+entId+"\">").append(cwUtils.esc4XML(entDisplayBil)).append("</target>");
		}
		pstmt.close();
		xml.append("</eval_target_lst>");
		return xml.toString();
	}
	
}

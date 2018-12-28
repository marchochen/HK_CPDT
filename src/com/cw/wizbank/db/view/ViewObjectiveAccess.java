/*
 * Created on 2004-6-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
 
package com.cw.wizbank.db.view;

import java.util.*;
import java.sql.*;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.DbObjectiveAccess;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cwn.wizbank.utils.CommonLog;
/**
 * @author davidq
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewObjectiveAccess {
	
	 //The Value of access right.In order to satisfy getField(**)
	public static final int LRN_VIEW = 0;
	public static final int READER = 1;
	public static final int AUTHOR = 2;
	public static final int OWNER = 3;
	
	public String getRootObjAccess(Connection con, long rootId, long entId)
			throws SQLException {
			StringBuffer sql = new StringBuffer();
			sql.append(
				"select oac_access_type from objectiveAccess where oac_obj_id = ? and (oac_ent_id = ? or oac_ent_id is null) ");
			PreparedStatement pst = null;
			ResultSet rs = null;
			String access = "";
			try {
				pst = con.prepareStatement(sql.toString());
				pst.setLong(1, rootId);
				pst.setLong(2, entId);

				rs = pst.executeQuery();
				if(rs.next()){
					access = rs.getString("oac_access_type");
					while(rs.next()){
						String newAccess = rs.getString("oac_access_type");
						int i = getClass().getField(access).getInt(new Object());
						int j = getClass().getField(newAccess).getInt(new Object());
						if(i<j)
						  access = newAccess;
					}
				}	
				else
					access = "";
			} catch (Exception e) {
			     throw new SQLException(e.getMessage());
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException sqle) {
						;
					}
				}
				if (pst != null) {
					try {
						pst.close();
					} catch (SQLException sqle) {
						;
					}
				}
			}

			return access;
		}	
		
	public Map getAccessFolders(Connection con, long entId,long syl_id, boolean tc_enabled,long tcr_id, String order_by)
			throws SQLException {
			Map folders = new HashMap();
			Vector v = new Vector();
			dbObjective obj = null;
			StringBuffer sql = new StringBuffer();
			String oldAccess = "";
			if(tc_enabled) {
				sql.append(" select obj_id,obj_type,obj_desc,obj_tcr_id,tcr_title, oac_access_type from objectiveAccess ")
				   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ");
				if(tcr_id > 0) {
					sql.append(" INNER JOIN tcTrainingCenter on (obj_tcr_id = tcr_id) ")
					   .append(" where  tcr_id = ? ")
					   .append(" and tcr_status = ? ");
				} else  {
					sql.append(" inner join (select distinct(child.tcr_id) as tcr_id, child.tcr_title as tcr_title from tcTrainingCenter ancestor ")
					   .append(" inner join tcTrainingCenterOfficer on (tco_tcr_id = ancestor.tcr_id) ")
					   .append(" Left join tcRelation on (tcn_ancestor = ancestor.tcr_id) ")
					   .append(" inner join tcTrainingCenter child on (child.tcr_id = tcn_child_tcr_id or child.tcr_id = ancestor.tcr_id) ")
					   .append(" where tco_usr_ent_id =? ")
					   .append(" and child.tcr_status = ? ")
					   .append(" and ancestor.tcr_status = ?")
					   .append(" ) A on (obj_tcr_id = tcr_id) ")
					   .append(" WHERE  1=1 ");
				}
			    sql.append("  and (oac_ent_id = ? or oac_ent_id is null) and obj_syl_id = ? and oac_access_type <> ? ORDER BY ")
				   .append(order_by);				
			} else {
				sql.append(" select obj_id,obj_type,obj_desc, obj_tcr_id, oac_access_type from objectiveAccess,objective ")
				   .append(" where oac_obj_id = obj_id and (oac_ent_id = ? or oac_ent_id is null) and obj_syl_id = ? ")
				   .append(" and oac_access_type <> ? ORDER BY obj_type ASC , obj_desc ASC ");
			}

			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				pst = con.prepareStatement(sql.toString());
				int index = 1;
				if(tc_enabled) {
					if(tcr_id > 0) {
						pst.setLong(index++, tcr_id);
						pst.setString(index++, DbTrainingCenter.STATUS_OK);
					} else {
						pst.setLong(index++, entId);
						pst.setString(index++, DbTrainingCenter.STATUS_OK);
						pst.setString(index++, DbTrainingCenter.STATUS_OK);
					}
				}
				pst.setLong(index++, entId);
				pst.setLong(index++, syl_id);
				pst.setString(index++, AcObjective.LRN_VIEW);
				rs = pst.executeQuery();
				while (rs.next()) {
					obj = new dbObjective();
					obj.obj_id = rs.getLong("obj_id");
					obj.obj_type = rs.getString("obj_type");
					obj.obj_desc = rs.getString("obj_desc");
					obj.access = rs.getString("oac_access_type");
					obj.obj_tcr_id = rs.getLong("obj_tcr_id");
					//v.addElement(obj);
					if((oldAccess = (String)folders.get(new Long(obj.obj_id)))!= null){
						int i = getClass().getField(oldAccess).getInt(new Object());
						int j = getClass().getField(obj.access).getInt(new Object());
						//Put the senior accesss type into hashmap
						if(i < j ){
							folders.remove(new Long(obj.obj_id));
							folders.put(new Long(obj.obj_id),obj.access);
						}
					}else{
					  folders.put(new Long(obj.obj_id),obj.access);
					  v.addElement(obj);
					}
				}
				folders.put("objVec", v);
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException sqle) {
						;
					}
				}
				if (pst != null) {
					try {
						pst.close();
					} catch (SQLException sqle) {
						;
					}
				}
			}
			return folders;
	}
	public Map getInstrAccessFolders(Connection con, long entId,long syl_id, boolean tc_enabled, long tcr_id, String order_by)
		throws SQLException {
		Map folders = new HashMap();
		Vector v = new Vector();
		dbObjective obj = null;
		StringBuffer sql = new StringBuffer();
		String oldAccess = "";
		sql.append(" select obj_id,obj_type,obj_desc,obj_tcr_id, oac_access_type, tcr_title from objectiveAccess ")
		   .append(" INNER JOIN objective on ( obj_id = oac_obj_id ) ")
		   .append(" INNER JOIN tcTrainingCenter on (obj_tcr_id = tcr_id ) ");
		if(tc_enabled ){
			if(tcr_id >0) {
			} else {
				sql.append(" inner join tcTrainingCenterTargetEntity on (tce_tcr_id = tcr_id) ")
				   .append(" inner join EntityRelation on (ern_ancestor_ent_id = tce_ent_id) ");
			}
		}
		sql.append(" WHERE  1=1 and tcr_status = ? ");
		if(tc_enabled ) {
			if(tcr_id >0 ){
				sql.append(" and obj_tcr_id = ? ");
			} else {
				sql.append(" and ern_type =? ")
				   .append(" and ern_child_ent_id = ? ")
				   .append(" AND ern_parent_ind = ? ");
			}
		}
		sql.append(" and (oac_ent_id = ? or oac_ent_id is null) and obj_syl_id = ? ORDER BY ").append(order_by);
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement(sql.toString());
			int index = 1;
			pst.setString(index++, DbTrainingCenter.STATUS_OK);
			if(tc_enabled ) {
				if(tcr_id > 0) {
					pst.setLong(index++, tcr_id);
				} else {
					pst.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
					pst.setLong(index++, entId);
					pst.setBoolean(index++, true);
				}
			}
			pst.setLong(index++, entId);
			pst.setLong(index++, syl_id);
			rs = pst.executeQuery();
			while (rs.next()) {
				obj = new dbObjective();
				obj.obj_id = rs.getLong("obj_id");
				obj.obj_type = rs.getString("obj_type");
				obj.obj_desc = rs.getString("obj_desc");
				obj.access = rs.getString("oac_access_type");
				obj.obj_tcr_id = rs.getLong("obj_tcr_id");
				//v.addElement(obj);
				if((oldAccess = (String)folders.get(new Long(obj.obj_id)))!= null){
					int i = getClass().getField(oldAccess).getInt(new Object());
					int j = getClass().getField(obj.access).getInt(new Object());
					//Put the senior accesss type into hashmap
					if(i < j ){
						folders.remove(new Long(obj.obj_id));
						folders.put(new Long(obj.obj_id),obj.access);
					}
				}else{
				  folders.put(new Long(obj.obj_id),obj.access);
				  v.addElement(obj);
				}
			}
			folders.put("objVec", v);
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqle) {
					;
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException sqle) {
					;
				}
			}
		}
		return folders;
	}

	public  void delByObjId(Connection con, long oac_obj_id) throws SQLException {
		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(SqlStatements.SQL_DEL_OAC);
			stmt.setLong(1,oac_obj_id);
			stmt.executeUpdate();
		}finally {
			if(stmt!=null) stmt.close();
		}
	}
	public  Vector getByObjNType(Connection con, long oac_obj_id, String oac_access_type) throws SQLException {
		PreparedStatement stmt = null;
		Vector v = new Vector();
		try{
			stmt = con.prepareStatement(SqlStatements.SQL_GET_OAC);
			stmt.setLong(1,oac_obj_id);
			stmt.setString(2,oac_access_type);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				DbObjectiveAccess oac = new DbObjectiveAccess();
				oac.oac_ent_id = rs.getLong("oac_ent_id");
				oac.oac_obj_id = rs.getLong("oac_obj_id");
				oac.oac_access_type = rs.getString("oac_access_type");
				v.addElement(oac);
			}
		} finally {
			if(stmt!=null) stmt.close();
		}
		return v;
	}	   
}

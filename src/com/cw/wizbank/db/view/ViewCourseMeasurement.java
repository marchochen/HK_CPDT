/*
 * Created on 2004-10-20
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.db.DbCourseMeasurement;
import com.cw.wizbank.db.DbMeasurementEvaluation;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.util.cwSQL;
	
/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewCourseMeasurement {
	private static String cosCtrMsmValue = "select cmt_id mt_id,null mt_res_id,cmt_ccr_id,mtv_cos_id mt_cos_id,mtv_ent_id mt_ent_id,mtv_tkh_id mt_tkh_id,mtv_status mt_status,mtv_score mt_score "+
                                           " from courseMeasurement,measurementEvaluation  where cmt_ccr_id = ? and cmt_cmr_id is null and cmt_id=mtv_cmt_id  and cmt_delete_timestamp is null "+
                                           " union "+
                                           " SELECT cmt_id, cmr_res_id, cmt_ccr_id,mov_cos_id,mov_ent_id,mov_tkh_id,mov_status mt_status,mov_score mt_score "+
                                           " FROM coursemeasurement,CourseModuleCriteria,module,moduleEvaluation  WHERE cmt_cmr_id = cmr_id and cmt_ccr_id = ? and cmr_res_id = mod_res_id AND mod_res_id = mov_mod_id and cmr_del_timestamp is null "+ 
                                           " order by mt_id ";
	
	static String UPD_STATUS = "update measurementEvaluation set mtv_status = ? where mtv_cmt_id = ? and mtv_score is not null and ";
	public static List getRelateMeasurement(Connection con,long ccr_id){
		List list = new ArrayList();
		DbCourseMeasurement cmt = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(getCourseRelateMeasurementSQL(con));
			pst.setLong(1,ccr_id);
			pst.setLong(2,ccr_id);
			
			rs = pst.executeQuery();
			while(rs.next()){
				cmt = new DbCourseMeasurement(rs.getLong("mt_id"));
				cmt.setCmt_cmr_id(rs.getLong("mt_cmr_id"));
				cmt.setRes_id(rs.getLong("mt_res_id"));
				cmt.setCmt_status(rs.getString("mt_status"));
				cmt.setCmt_contri_rate(rs.getFloat("mt_contri_rate"));
				cmt.setCmt_is_contri_by_score(rs.getBoolean("mt_is_contri_by_score"));
				cmt.setCmt_max_score(rs.getFloat("mt_max_score"));
				
				list.add(cmt);
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.cleanUp(rs,pst);
		}
		return list;
	}
	
	public static Map getMsmValue(Connection con,long ccr_id){
		Map map = new HashMap();
		Map inner = null;
		Long id;
		PreparedStatement pst = null;
		ResultSet rs = null;
		DbMeasurementEvaluation obj = null;
		try{
			pst = con.prepareStatement(getMsmMeasurementSQL(con));
			pst.setLong(1,ccr_id);
			pst.setLong(2,ccr_id);
			
			rs = pst.executeQuery();
			while(rs.next()){
				obj = new DbMeasurementEvaluation();
				obj.setMtv_cmt_id(rs.getLong("mt_id"));
				obj.setMtv_cos_id(rs.getLong("mt_cos_id"));
				obj.setMtv_ent_id(rs.getLong("mt_ent_id"));
				obj.setMtv_status(rs.getString("mt_status"));
				obj.setMtv_score(rs.getFloat("mt_score"));
				obj.setMtv_tkh_id(rs.getLong("mt_tkh_id")); 
				id = new Long(obj.getMtv_tkh_id());               
				if(map.containsKey(id)){
					inner = (HashMap)map.get(id);
					inner.put(new Long(obj.getMtv_cmt_id()),obj);
				}else{
				  inner = new HashMap();
				  inner.put(new Long(obj.getMtv_cmt_id()),obj);
				  map.put(id,inner);
				}
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.cleanUp(rs,pst);
		}
		return map;
	}
	
	public static void updLrnStatus(Connection con,String status,long cmt_id,float score){
		DbCourseMeasurement dbcmt = new DbCourseMeasurement();
		PreparedStatement pst = null;
		String tmp = null;
		try{
			if(dbAiccPath.STATUS_FAILED.equalsIgnoreCase(status)){
				tmp = UPD_STATUS + " mtv_score < ?";
			}else if(dbAiccPath.STATUS_PASSED.equalsIgnoreCase(status)){
				tmp = UPD_STATUS + " mtv_score >= ?";
			}
			pst = con.prepareStatement(tmp);
			pst.setString(1,status);
			pst.setLong(2,cmt_id);
			pst.setFloat(3,score);
			
			pst.executeUpdate();
			// loop to update child record
			List ch_cmt_id_list = dbcmt.getChCmtIdList( con, cmt_id);
			for (int i = 0; i < ch_cmt_id_list.size();i++) {
				if (ch_cmt_id_list.get(i) != null) {
					long ch_cmt_id = ((Long)ch_cmt_id_list.get(i)).longValue();
					updLrnStatus( con, status, ch_cmt_id, score);
				}
			}
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.closePreparedStatement(pst);
		}
	}
	
	public static String getCourseRelateMeasurementSQL(Connection con) throws SQLException {
		String sql = "select cmt_id mt_id,cmt_cmr_id mt_cmr_id," + cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG)  + " mt_res_id,cmt_ccr_id,cmt_status mt_status,cmt_contri_rate mt_contri_rate,cmt_is_contri_by_score mt_is_contri_by_score,cmt_max_score mt_max_score "+
        " from courseMeasurement where cmt_ccr_id = ? and cmt_cmr_id is null and cmt_delete_timestamp is null "+
        " union "+
        " SELECT cmt_id mt_id, cmt_cmr_id mt_cmr_id,cmr_res_id  mt_res_id,cmr_ccr_id, cmr_status mt_status, cmr_contri_rate mt_contri_rate, cmr_is_contri_by_score mt_is_contri_by_score, mod_max_score mt_max_score"+
        " FROM coursemeasurement,CourseModuleCriteria,module WHERE cmt_cmr_id = cmr_id and cmt_ccr_id = ? and cmr_res_id = mod_res_id AND cmr_del_timestamp is null "+
        " order by mt_id ";
		return sql;
	}
	public static String getMsmMeasurementSQL(Connection con) throws SQLException {
		String sql = "select cmt_id mt_id," + cwSQL.get_null_sql(cwSQL.COL_TYPE_LONG)  + " mt_res_id,cmt_ccr_id,mtv_cos_id mt_cos_id,mtv_ent_id mt_ent_id,mtv_tkh_id mt_tkh_id, "+ 
		" mtv_status mt_status,mtv_score mt_score " +
        " from courseMeasurement,measurementEvaluation "+ 
        " where cmt_ccr_id = ? and cmt_cmr_id is null and cmt_id=mtv_cmt_id and cmt_delete_timestamp is null " + 
        " union "+
        " SELECT cmt_id mt_id, cmr_res_id mt_res_id, cmt_ccr_id,mov_cos_id mt_cos_id,mov_ent_id mt_ent_id,mov_tkh_id mt_tkh_id, "+
        " mov_status mt_status,mov_score mt_score " + 
        " FROM coursemeasurement,CourseModuleCriteria,module,moduleEvaluation "+ 
        " WHERE cmt_cmr_id = cmr_id and cmt_ccr_id = ? and cmr_res_id = mod_res_id AND mod_res_id = mov_mod_id and cmr_del_timestamp is null " +
        " order by mt_id ";
		return sql;
	}
}

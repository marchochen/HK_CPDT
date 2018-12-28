package com.cw.wizbank.ae;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.batch.user.DeleteUser;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

//彻底删除课程
public class itemCompleteDelete {
	
	public static void delOtherRecord(Connection con, long res_id, boolean is_complete_del) throws SQLException, cwException {
		List<Long> lst = getAppIdAndTkhIdVector(con, res_id);
		aeApplication app = new aeApplication();
		for(int j = 0; j < lst.size(); j = j+2){
			app.app_tkh_id = lst.get(j);
			app.app_id = lst.get(j+1);
			app.is_complete_del = is_complete_del;
			// 在删除报名时，已同时删除学习记录
			app.del(con);
		}
//		//To delete TrackingHistory records have no corresponding Application					
//		List<Long> v_tkh = getTkhIdNoAppIDVector(con, res_id);
//		for(int j = 0; j<v_tkh.size(); j++){
//			delCompletedRecordByTkh_id(con, v_tkh.get(j));
//		}
	}
	
    private static final String SQL_GET_ENROLLMENT = "select tkh_id,app_id from TrackingHistory,Course,aeApplication where cos_res_id=tkh_cos_res_id and tkh_id=app_tkh_id and cos_res_id=?";
	public static List<Long> getAppIdAndTkhIdVector(Connection con, long cos_res_id) throws SQLException {
		List<Long> v = new ArrayList<Long>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL_GET_ENROLLMENT);
			stmt.setLong(1, cos_res_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				v.add(rs.getLong("tkh_id"));
				v.add(rs.getLong("app_id"));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return v;
	}
	
	private static final String SQL_GET_TKHIDNOAPPID = "select tkh_id from TrackingHistory where tkh_id not in (select app_tkh_id from aeApplication where app_tkh_id is not null) and tkh_cos_res_id = ? ";
	public static List<Long> getTkhIdNoAppIDVector(Connection con, long cos_res_id) throws SQLException {
		List<Long> v = new ArrayList<Long>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL_GET_TKHIDNOAPPID);
			stmt.setLong(1, cos_res_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				v.add(rs.getLong("tkh_id"));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return v;
	}
	
    //delete completed record just by tkh_id
	public static void delCompletedRecordByTkh_id(Connection con,long tkh_id)throws SQLException, cwException{
		if(tkh_id > 0){
			DeleteUser.upd(con, " delete from  scoRecord where srd_tkh_id = ? ",tkh_id);
			DeleteUser.upd(con, " delete from accomplishment where apm_tkh_id =? ",tkh_id);
			DeleteUser.upd(con, " delete from progressattemptsaveanswer where psa_tkh_id =? ",tkh_id);
			DeleteUser.upd(con, " delete from progressattemptsave where pas_tkh_id = ?", tkh_id);
			DeleteUser.upd(con, " delete from progressattachment where pat_tkh_id = ? ", tkh_id);
			DeleteUser.upd(con, " delete from ProgressAttempt where atm_tkh_id = ?", tkh_id);
			DeleteUser.upd(con, " delete from progress where pgr_tkh_id =?", tkh_id);
			DeleteUser.upd(con, " delete from moduleevaluation where mov_tkh_id = ?", tkh_id);
			DeleteUser.upd(con, " delete from courseevaluation where cov_tkh_id = ?", tkh_id);
			DeleteUser.upd(con, " delete from progress where pgr_tkh_id =? ", tkh_id);
			DeleteUser.upd(con, " delete from measurementevaluation where mtv_tkh_id =?", tkh_id);
			DeleteUser.upd(con, " delete from moduleevaluationhistory where mvh_tkh_id =?", tkh_id);
			DeleteUser.upd(con, " delete from aeItemComments where ict_tkh_id=? ", tkh_id);
			DeleteUser.upd(con, " DELETE From TrackingHistory Where tkh_id = ? ", tkh_id);
		}
	}
	
    

    // 删除课程或者班级的预订资源
	public static void delItmFacRsvByItmId(Connection con, long itm_id) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			long itm_rsv_id = 0;

			String sql = " select itm_rsv_id from aeItem where itm_id = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				itm_rsv_id = rs.getLong("itm_rsv_id");
			}
			cwSQL.cleanUp(rs, stmt);

			if (itm_rsv_id > 0) {
				sql = "  update aeItem set itm_rsv_id = null where itm_id = ? ";
				stmt = con.prepareStatement(sql);
				stmt.setLong(1, itm_id);
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);

				sql = "  delete from fmFacilitySchedule where fsh_rsv_id = ? ";
				stmt = con.prepareStatement(sql);
				stmt.setLong(1, itm_rsv_id);
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);

				sql = "  delete from fmReservation where rsv_id = ? ";
				stmt = con.prepareStatement(sql);
				stmt.setLong(1, itm_rsv_id);
				stmt.executeUpdate();
				cwSQL.cleanUp(rs, stmt);
			}

			List chril_itm_lst = new aeItem().getChItemIDList(con, itm_id);
			if (chril_itm_lst != null && chril_itm_lst.size() > 0) {
				for (int i = 0; i < chril_itm_lst.size(); i++) {
					long c_itm_id = ((Long) chril_itm_lst.get(i)).longValue();
					delItmFacRsvByItmId(con, c_itm_id);
				}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}


}

package com.cw.wizbank.integratedlrn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.cw.wizbank.ae.aeAttendance;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbIntegCompleteCondition;
import com.cw.wizbank.db.DbIntegCourseCriteria;
import com.cw.wizbank.db.DbIntegRelationItem;
import com.cw.wizbank.db.view.ViewCourseModuleCriteria;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class IntegratedLrn {
    
    public static class itmCondition {
        long completed_elective_count ;
        Vector condition_lst = new Vector();
        int total_elective = 0;
        int total_conpulsory = 0;
    }

	public long itm_id;
	public String itm_code;
	public String itm_title;
	public long icd_id;
	public int icd_completed_item_count;
	public String icd_type;
	public Timestamp icd_update_timestamp;
	public String usr_display_bil;
	public boolean itm_apply_ind;
	
	public static Vector getCourse(Connection con, long itm_id) throws SQLException {
		Vector vec = new Vector();
		StringBuffer sql = new StringBuffer(250);
		sql.append("select itm_id, itm_code, itm_title, itm_apply_ind, icd_id, icd_completed_item_count, icd_type, icd_update_timestamp, usr_display_bil")
			.append(" from aeItem")
			.append(" inner join IntegRelationItem on(itm_id = iri_relative_itm_id)")
			.append(" inner join IntegCompleteCondition on(icd_id = iri_icd_id)")
			.append(" inner join RegUser on(icd_update_usr_id = usr_id)")
			.append(" inner join IntegCourseCriteria on(icc_id = icd_icc_id and icc_itm_id = ?)")
			.append(" order by icd_type, icd_update_timestamp desc");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, itm_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			IntegratedLrn itgLrn = new IntegratedLrn();
			itgLrn.itm_id = rs.getLong("itm_id");
			itgLrn.itm_code = rs.getString("itm_code");
			itgLrn.itm_title = rs.getString("itm_title");
			itgLrn.icd_id = rs.getLong("icd_id");
			itgLrn.icd_completed_item_count = rs.getInt("icd_completed_item_count");
			itgLrn.icd_type = rs.getString("icd_type");
			itgLrn.icd_update_timestamp = rs.getTimestamp("icd_update_timestamp");
			itgLrn.usr_display_bil = rs.getString("usr_display_bil");
			itgLrn.itm_apply_ind = rs.getBoolean("itm_apply_ind");
			vec.add(itgLrn);
		}
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
		return vec;
	}

	public static String getCourseListXml(Connection con, IntegratedLrnParam param) throws SQLException, cwSysMessage {
		StringBuffer xml = new StringBuffer();
		DbIntegCourseCriteria dbIcc = new DbIntegCourseCriteria();
		dbIcc.icc_itm_id = param.getItm_id();
		dbIcc.get(con);
		
		DbIntegCompleteCondition dbIcd = new DbIntegCompleteCondition();
		dbIcd.icd_icc_id = dbIcc.icc_id;
		dbIcd.get(con);
		
		xml.append("<criteria").append(" itm_id=\"").append(dbIcc.icc_itm_id)
			.append("\" itm_title=\"").append(cwUtils.esc4XML(aeItem.getItemTitle(con, dbIcc.icc_itm_id)))
			.append("\" icc_id=\"").append(dbIcc.icc_id)
			.append("\" icc_update_timestamp=\"").append(dbIcc.icc_update_timestamp)
			.append("\" completed_elective_count=\"").append(dbIcc.icc_completed_elective_count).append("\" >");
		
		Vector vec = getCourse(con, dbIcc.icc_itm_id);
		xml.append("<icd_lst>");
		long cur_icd_id = 0;
		for(int i=0; i<vec.size(); i++) {
			IntegratedLrn itgLrn = (IntegratedLrn)vec.elementAt(i);
			if(cur_icd_id != itgLrn.icd_id) {
				if(cur_icd_id > 0) {
					xml.append("</icd>");
				}
				xml.append("<icd id=\"").append(itgLrn.icd_id)
					.append("\" type=\"").append(itgLrn.icd_type)
					.append("\" completed_item_count=\"").append(itgLrn.icd_completed_item_count)
					.append("\" update_timestamp=\"").append(itgLrn.icd_update_timestamp)
					.append("\" update_usr_id=\"").append(cwUtils.esc4XML(itgLrn.usr_display_bil))
					.append("\">");
			}
			xml.append("<item id=\"").append(itgLrn.itm_id)
				.append("\" code=\"").append(cwUtils.esc4XML(itgLrn.itm_code))
				.append("\" title=\"").append(cwUtils.esc4XML(itgLrn.itm_title))
				.append("\"/>");
			cur_icd_id = itgLrn.icd_id;
		}
		if(vec.size() > 0) {
			xml.append("</icd>");
		}
		xml.append("</icd_lst>");
		xml.append("</criteria>");
		return xml.toString();
	}
	
	public static String getConditionXml(Connection con, IntegratedLrnParam param) throws SQLException, cwSysMessage {
		String icd_type = param.getIcd_type();

		StringBuffer tmpXml = new StringBuffer();
		if(param.getIcd_id() > 0) {
			DbIntegCompleteCondition dbIcd = new DbIntegCompleteCondition();
			dbIcd.icd_id = param.getIcd_id();
			dbIcd.get(con);
			icd_type = dbIcd.icd_type;
			tmpXml.append("<icd id=\"").append(dbIcd.icd_id)
				.append("\" update_timestamp=\"").append(dbIcd.icd_update_timestamp)
				.append("\" />");
			tmpXml.append("<completed_item_count>").append(dbIcd.icd_completed_item_count).append("</completed_item_count>");
			tmpXml.append("<itm_lst>");
			StringBuffer sql = new StringBuffer();
			sql.append("select itm_id, itm_title")
				.append(" from aeItem")
				.append(" inner join IntegRelationItem on (iri_relative_itm_id = itm_id and iri_icd_id = ?)");
			PreparedStatement stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setLong(index++, dbIcd.icd_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				tmpXml.append("<itm id=\"").append(rs.getLong("itm_id"))
					.append("\" title=\"").append(cwUtils.esc4XML(rs.getString("itm_title")))
					.append("\"/>");
			}
			tmpXml.append("</itm_lst>");			
		}

		StringBuffer xml = new StringBuffer();
		xml.append("<complete_condition")
			.append(" icc_id=\"").append(param.getIcc_id())
			.append("\" itm_id=\"").append(param.getItm_id())
			.append("\" itm_title=\"").append(cwUtils.esc4XML(aeItem.getItemTitle(con, param.getItm_id()))).append("\">");
		xml.append("<type>").append(icd_type).append("</type>");
		xml.append(tmpXml);
		xml.append("</complete_condition>");
		return xml.toString();
	}
	
	public static void setCondition(Connection con, IntegratedLrnParam param, loginProfile prof) throws SQLException, cwSysMessage {
		DbIntegCompleteCondition dbIcd = new DbIntegCompleteCondition();
		dbIcd.icd_type = param.getIcd_type();
		dbIcd.icd_completed_item_count = param.getIcd_completed_item_count();
		dbIcd.icd_update_timestamp = param.getCur_time();
		dbIcd.icd_update_usr_id = prof.usr_id;

		DbIntegRelationItem dbIri = new DbIntegRelationItem();
		if(param.getIcd_id() > 0) {
			dbIcd.icd_id = param.getIcd_id();
			
			Timestamp t = DbIntegCompleteCondition.getUpdTimestamp(con, dbIcd.icd_id);
			if(param.getIcd_update_timestamp() != null 
					&& (t == null || !param.getIcd_update_timestamp().equals(t))) {
				throw new cwSysMessage("GEN006");
			}
			
			dbIcd.upd(con);

			dbIri.iri_icd_id = param.getIcd_id();
			dbIri.del(con);
		} else {
			dbIcd.icd_icc_id = param.getIcc_id();
			dbIcd.icd_create_timestamp = param.getCur_time();
			dbIcd.icd_create_usr_id = prof.usr_id;
			dbIcd.ins(con);

			dbIri.iri_icd_id = dbIcd.icd_id;
		}
		dbIri.ins(con, param.getItm_condition_list());
	}
	
	public static void delCondition(Connection con, IntegratedLrnParam param) throws SQLException, cwSysMessage {
		Timestamp t = DbIntegCompleteCondition.getUpdTimestamp(con, param.getIcd_id());
		if(param.getIcd_update_timestamp() != null 
				&& (t == null || !param.getIcd_update_timestamp().equals(t))) {
			throw new cwSysMessage("GEN006");
		}

		DbIntegRelationItem dbIri = new DbIntegRelationItem();
		dbIri.iri_icd_id = param.getIcd_id();
		dbIri.del(con);
		
		DbIntegCompleteCondition dbIcd = new DbIntegCompleteCondition();
		dbIcd.icd_id = param.getIcd_id();
		dbIcd.del(con);
	}
	
	public static void setCriteria(Connection con, IntegratedLrnParam param, loginProfile prof) throws SQLException, cwSysMessage {
		DbIntegCourseCriteria dbIcc = new DbIntegCourseCriteria();
		dbIcc.icc_completed_elective_count = param.getIcc_completed_elective_count();
		dbIcc.icc_update_timestamp = param.getCur_time();
		dbIcc.icc_update_usr_id = prof.usr_id;
		dbIcc.icc_itm_id = param.getItm_id();
		dbIcc.upd(con);
	}
	
	public static void del(Connection con, long itm_id) throws SQLException {
		DbIntegCourseCriteria dbIcc = new DbIntegCourseCriteria();
		dbIcc.icc_itm_id = itm_id;
		dbIcc.get(con);

		DbIntegCompleteCondition dbIcd = new DbIntegCompleteCondition();
		dbIcd.icd_icc_id = dbIcc.icc_id;
		Vector icdIds = dbIcd.getByIccId(con, true);

		DbIntegRelationItem dbIri = new DbIntegRelationItem();
		dbIri.iri_icd_id = dbIcd.icd_id;

		dbIri.del(con, cwUtils.vector2list(icdIds));

		dbIcd.delByIccId(con);

		dbIcc.del(con);
	}
	
	public itmCondition getItmCondition(Connection con, long itm_id) throws SQLException {
        StringBuffer sql = new StringBuffer(250);
        sql.append("select icd_id ,icc_completed_elective_count, icd_completed_item_count, icd_type ")
            .append(" from IntegCourseCriteria, IntegCompleteCondition,aeItem")
            .append(" where icc_itm_id = itm_id and icc_id = icd_icc_id and icc_itm_id = ?");
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        int index = 1;
        stmt.setLong(index++, itm_id);
        ResultSet rs = stmt.executeQuery();
        itmCondition itm_cond = new itmCondition();
        
        while(rs.next()) {
            DbIntegCompleteCondition icd = new DbIntegCompleteCondition();
            itm_cond.completed_elective_count = rs.getLong("icc_completed_elective_count");
            icd.icd_type = rs.getString("icd_type");
            icd.icd_id = rs.getLong("icd_id");
            icd.icd_completed_item_count = rs.getInt("icd_completed_item_count");
            icd.IntegItmlst = DbIntegRelationItem.getRelItmLst( con, icd.icd_id);
            itm_cond.condition_lst.add(icd);
            if(icd.icd_type != null && icd.icd_type.equalsIgnoreCase(DbIntegCompleteCondition.TYPE_ELECTIVE)){
                itm_cond.total_elective++;
            }else{
                itm_cond.total_conpulsory++;
            }
        }
        if(itm_cond.completed_elective_count > itm_cond.total_elective){
            itm_cond.completed_elective_count = itm_cond.total_elective;
        }
        
        if(rs != null) {
            rs.close();
        }
        if(stmt != null) {
            stmt.close();
        }
        return itm_cond;
    }
	
	public boolean hasCompletedItmCondition(Connection con, DbIntegCompleteCondition icd, long usr_ent_id) throws SQLException {
	    boolean re = false;
	    StringBuffer tem_lst = new StringBuffer();
	    for(int i = 0; i< icd.IntegItmlst.size(); i++){
	        tem_lst.append(icd.IntegItmlst.get(i)).append(", ");
	    }
	    tem_lst.append("0");
	    PreparedStatement stmt = null;
		try{
		    String SQL = "select count(distinct(app_itm_id)) from aeApplication,courseEvaluation " +
		    		"where app_tkh_id = cov_tkh_id and app_ent_id = ? and cov_status = ? and (app_itm_id in("+tem_lst.toString()+")" +
		    				" or app_itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id in("+tem_lst.toString()+") ))";
		    stmt = con.prepareStatement(SQL);
	        int index = 1;
	        stmt.setLong(index++,usr_ent_id);
	        stmt.setString(index++,"C");
	        ResultSet rs = stmt.executeQuery();
	        int count = 0;
	        if(rs.next()){
	            count = rs.getInt(1);
	        }
	        if(count > icd.icd_completed_item_count || count == icd.icd_completed_item_count){
	            re = true; 
	        }
        } finally {
    		cwSQL.closePreparedStatement(stmt);
        }
        stmt.close();
	    return re;
        
    }
	
	public void markCourseApp(Logger logger, Connection con, loginProfile prof,Vector app_lst, long root_ent_id) throws SQLException, cwException, cwSysMessage {
        Hashtable con_has = new Hashtable();
        Hashtable expire_has = new Hashtable();
        if (app_lst != null) {
            int status_C = aeAttendanceStatus.getIdByType(con, root_ent_id,aeAttendanceStatus.STATUS_TYPE_ATTEND);
            int status_F = aeAttendanceStatus.getIdByType(con, root_ent_id,aeAttendanceStatus.STATUS_TYPE_INCOMPLETE);
            ViewCourseModuleCriteria.ViewAttendDate[] modCrit = ViewCourseModuleCriteria.getExpiredList(con, root_ent_id,DbCourseCriteria.TYPE_COMPLETION, true);
            for (int i = 0; i < modCrit.length; i++) {
                expire_has.put(String.valueOf(modCrit[i].app_id), "YES");
            }
            for (int i = 0; i < app_lst.size(); i++) {
                ViewCourseModuleCriteria.ViewAttendDate app = (ViewCourseModuleCriteria.ViewAttendDate) app_lst.get(i);

                if (logger != null) {
                    logger.debug("++++++ reEvaluation Date: "+ cwSQL.getTime(con));
                }
                itmCondition itm_cond;
                if (con_has.get(String.valueOf(app.app_itm_id)) != null) {
                    itm_cond = (itmCondition) con_has.get(String.valueOf(app.app_itm_id));
                } else {
                    itm_cond = getItmCondition(con, app.app_itm_id);
                    con_has.put(String.valueOf(app.app_itm_id), itm_cond);
                }
                int has_completed_conpulsory_count = 0;
                int has_completed_elective_count = 0;
                if (itm_cond != null && itm_cond.condition_lst.size() > 0) {
                    for (int j = 0; j < itm_cond.condition_lst.size(); j++) {
                        DbIntegCompleteCondition icd = (DbIntegCompleteCondition) itm_cond.condition_lst.get(j);
                        if (hasCompletedItmCondition(con, icd, app.cov_ent_id)) {
                            if (icd.icd_type != null&& icd.icd_type.equalsIgnoreCase(DbIntegCompleteCondition.TYPE_ELECTIVE)) {
                                has_completed_elective_count++;
                            } else {
                                has_completed_conpulsory_count++;
                            }
                        }
                    }
                    if (itm_cond.condition_lst.size() > 0) {
                        //float progress = (has_completed_elective_count + has_completed_conpulsory_count)/(float)(itm_cond.total_conpulsory + itm_cond.completed_elective_count);
                    	float progress = (has_completed_elective_count + has_completed_conpulsory_count)/(float)(itm_cond.condition_lst.size());
                        if (progress > 1) {
                            progress = 1;
                        }
                        progress = progress * 100;
                        // update回表courseEvaluation 的进度
                        
                        dbCourseEvaluation.updateCosProgress(con,app.cos_res_id, app.cov_ent_id, app.cov_tkh_id,progress);
                    }
                    if (has_completed_conpulsory_count >= itm_cond.total_conpulsory && has_completed_elective_count >= itm_cond.completed_elective_count) {
                        // 把学习状态置为已完成
                        aeAttendance.saveStatus(con, app.app_id,app.app_itm_id, app.cos_res_id, app.cov_ent_id,false, status_C, prof.usr_id);
                    } else {
                        if (expire_has.get(String.valueOf(app.app_id)) != null) {
                            // 把学习状态置为未完成
                            aeAttendance.saveStatus(con, app.app_id,app.app_itm_id, app.cos_res_id,app.cov_ent_id, false, status_F,prof.usr_id);
                        }
                    }
                }
            }
        }
    }
}
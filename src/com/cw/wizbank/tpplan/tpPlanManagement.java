/**
 * 
 */
package com.cw.wizbank.tpplan;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTcRelation;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tpplan.db.dbTpTrainingPlan;
import com.cw.wizbank.tpplan.db.dbTpYearPlan;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.security.AclFunction;


/**
 * @author jackyx
 * @date 2007-10-15
 */
public class tpPlanManagement {

	public static final String TP_INS_SUCCESS = "TPN005";
	public static final String SORT_BY_ASC = "ASC";
	public static final String SORT_BY_DESC = "DESC";
	
	public tpPlanManagement() {
		;
	}

	
	public static class tpYearPlan {
		public long yp_year;
		public long yp_ypn_tcr_id;
		public long yp_tcr_id;
		public Timestamp yp_submit_end_time;
		public Timestamp yp_submit_start_time;
		public Timestamp yp_approve_timestamp;
		public Timestamp yp_upd_time;
		public String yp_file_name;
		public String yp_status;
		public String yp_tcr_title;
		public boolean hasSubBtn;
		public boolean hasRepBtn;
		public boolean hasDelBtn;
		public boolean hasDwlBtn;
		public boolean hasAddBtn;
	}
    

	/**
	 * get the add training plan preview as xml
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public String getTraingingPlanPreviewXML(Connection con, loginProfile prof) throws SQLException  {
		StringBuffer xml = new StringBuffer();
		Timestamp curTime = cwSQL.getTime(con);
		ViewTrainingCenter viewTc = new ViewTrainingCenter();
		List tcr_lst = viewTc.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, false);
		long def_tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
		xml.append("<default_tc tcr_id=\"").append(def_tcr_id).append("\"/>");
		xml.append("<my_charge_tc_lst>");
		if (tcr_lst != null) {
			Iterator tcr_itr = tcr_lst.iterator();
			while (tcr_itr.hasNext()) {
				DbTrainingCenter tc = (DbTrainingCenter) tcr_itr.next();
				if (tc != null) {
					xml.append("<tc tcr_id=\"").append(tc.tcr_id).append("\">").append(cwUtils.esc4XML(tc.tcr_title)).append("</tc>");
				}
			}
		}
		xml.append("</my_charge_tc_lst>");
		xml.append("<plan type=\"").append(dbTpTrainingPlan.TPN_TYPE_MAKEUP).append("\">").append(cwUtils.NEWL);
		xml.append("<current_timestamp>").append(curTime).append("</current_timestamp>");
		xml.append("</plan>").append(cwUtils.NEWL);
		return xml.toString();
	}

	/**
	 * get taringing plan information as xml
	 * @param con
	 * @param tpn_id
	 * @return
	 * @throws SQLException
	 */
	public String getTrainingPlanInfoAsXML(Connection con, loginProfile prof, long tpn_id) throws SQLException {
		StringBuffer xml = new StringBuffer();
		
		ViewTrainingCenter viewTc = new ViewTrainingCenter();
		List tcr_lst = viewTc.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, false);
		xml.append("<my_charge_tc_lst>");
		if (tcr_lst != null) {
			Iterator tcr_itr = tcr_lst.iterator();
			while (tcr_itr.hasNext()) {
				DbTrainingCenter tc = (DbTrainingCenter) tcr_itr.next();
				if (tc != null) {
					xml.append("<tc tcr_id=\"").append(tc.tcr_id).append("\">")
							.append(cwUtils.esc4XML(tc.tcr_title)).append(
									"</tc>");
				}
			}
		}
		xml.append("</my_charge_tc_lst>");
		
		dbTpTrainingPlan tpTp = new dbTpTrainingPlan();
		tpTp.tpn_id = tpn_id;
		tpTp.get(con);
		xml.append(getPlanDetailsAsXML(con, tpTp));
		if(tpTp.tpn_status.equalsIgnoreCase(dbTpTrainingPlan.TPN_STATUS_IMPLEMENTED)) {
			long tpn_itm_id =tpTp.getTpPlanItemId(con);
			boolean itm_run_ind = aeItem.getRunInd(con, tpn_itm_id);
			xml.append("<tp_item tp_id=\"").append(tpTp.tpn_id).append("\" itm_id=\"").append(tpn_itm_id).append("\" itm_run_ind=\"").append(itm_run_ind).append("\"/>");
		}
		return xml.toString();
	}
	public static String getPlanDetailsAsXML(Connection con, dbTpTrainingPlan tpTp) throws SQLException {
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append("<plan id=\"").append(tpTp.tpn_id).append("\" type=\"").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_type))).append("\">").append(cwUtils.NEWL);
		xmlBuf.append("<plan_date>").append(tpTp.tpn_date).append("</plan_date>");
		xmlBuf.append("<code>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_code))).append("</code>");
		xmlBuf.append("<name>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_name))).append("</name>");
		xmlBuf.append("<cos_type>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_cos_type))).append("</cos_type>");
		xmlBuf.append("<tnd_title>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_tnd_title))).append("</tnd_title>");
		xmlBuf.append("<aim>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_aim))).append("</aim>");
		xmlBuf.append("<duration>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_duration))).append("</duration>");
		xmlBuf.append("<lrn_count>").append(tpTp.tpn_lrn_count).append("</lrn_count>");
		xmlBuf.append("<wb_start_date>").append(cwUtils.escNull(tpTp.tpn_wb_start_date)).append("</wb_start_date>");
		xmlBuf.append("<wb_end_date>").append(cwUtils.escNull(tpTp.tpn_wb_end_date)).append("</wb_end_date>");
		xmlBuf.append("<ftf_start_date>").append(cwUtils.escNull(tpTp.tpn_ftf_start_date)).append("</ftf_start_date>");
		xmlBuf.append("<ftf_end_date>").append(cwUtils.escNull(tpTp.tpn_ftf_end_date)).append("</ftf_end_date>");
		xmlBuf.append("<responser>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_responser))).append("</responser>");
		xmlBuf.append("<introduction>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_introduction))).append("</introduction>");
		xmlBuf.append("<target>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_target))).append("</target>");
		xmlBuf.append("<type>").append(cwUtils.escNull(tpTp.tpn_type)).append("</type>");
		xmlBuf.append("<fee>").append(tpTp.tpn_fee).append("</fee>");
		xmlBuf.append("<remarks>").append(cwUtils.esc4XML(cwUtils.escNull(tpTp.tpn_remark))).append("</remarks>");
		xmlBuf.append("<training_center id=\"").append(tpTp.tpn_tcr_id).append("\">");
		xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(DbTrainingCenter.getTcrTitle(con, tpTp.tpn_tcr_id)))).append("</title>").append("</training_center>");
		xmlBuf.append("<status>").append(tpTp.tpn_status).append("</status>");
		xmlBuf.append("<upd_timestamp>").append(cwUtils.format(tpTp.tpn_update_timestamp)).append("</upd_timestamp>");
		xmlBuf.append("<submit_timestamp>").append(cwUtils.escNull(tpTp.tpn_submit_timestamp)).append("</submit_timestamp>");
		xmlBuf.append("</plan>");
		
		return xmlBuf.toString();
	}

	/**
	 * 
	 * @param con
	 * @param prof
	 * @param tpTp
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public void updTrainingPlan(Connection con, loginProfile prof, dbTpTrainingPlan tpTp) throws SQLException, cwSysMessage {
		Timestamp curTime = cwSQL.getTime(con);
        if(!tpTp.isLastUpd(con, tpTp.tpn_update_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        if(!(tpTp.tpn_status != null && (tpTp.tpn_status.equalsIgnoreCase(dbTpTrainingPlan.TPN_STATUS_PREPARED) || tpTp.tpn_status.equalsIgnoreCase(dbTpTrainingPlan.TPN_STATUS_DECLINED)))) {
        	  throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
        }
		tpTp.tpn_update_timestamp = curTime;
		tpTp.tpn_update_usr_id = prof.usr_id;
		tpTp.upd(con);
//		dbTpPlanEntity tpPlanEnt = new dbTpPlanEntity();
//		String[] pne_type_lst = {dbTpPlanEntity.PNE_TYPE_PLAN_RECTOR, dbTpPlanEntity.PNE_TYPE_PLAN_MASTER, dbTpPlanEntity.PNE_TYPE_PLAN_CHARGE_GROUP} ;
//		tpPlanEnt.delByTpnId(con, tpTp.tpn_id, pne_type_lst);
//		insPlanEntity(con, prof, tpTp);
		return;
	}
	public void delTriangPlanAsMarkUp(Connection con, loginProfile prof, long tpn_id) throws SQLException {
		
	}
	/**
	 * @param con 
	 * @param tcr_id: 前当培训组织ID
 	 * @param tpn_status：培训计划状态
	 * @param year：计划时间:年
	 * @param month：计划时间:月
	 * @param tc_enabled: 是否应用了培训组织
	 * @param sear_code 
	 * @param prof
	 * @return 培训计划xml
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwSysMessage
	 * @throws cwException 
	 */
	public String getOrgTrainingPlanXML(Connection con, WizbiniLoader wizbini, loginProfile prof, String tpn_status, String[] status_lst, String tpn_type, long year, long month, boolean is_makeup, cwPagination page) throws SQLException, qdbException, cwSysMessage, cwException {
		StringBuffer xmlBuf = new StringBuffer();
		Vector tcr_vec = new Vector();
		boolean is_filter = false;
		if(is_makeup) {
			if((tpn_status !=null && !tpn_status.equals("all")) || year !=0 || month !=0 ) {
				is_filter = true;
			} else {
				is_filter = false;
			}
		} else {
			if ((tpn_status != null && !tpn_status.equals("all")) || year != 0 || month != 0 || (tpn_type != null && !tpn_type.equals("all"))) {
				is_filter = true;
			} else {
				is_filter = false;
			}
		}

		ViewTrainingCenter viewTc = new ViewTrainingCenter();
		List tcr_lst =  null;
		//由于新加的自定义角色功能有不和培训中心相关的逻辑与计划管理原来的设计有些相冲
		//经讨论后修改为若角色不和培训中心相关的就显示用户的顶层培训中心的内容
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			 tcr_lst = viewTc.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, false);
		}else{
			tcr_lst = new ArrayList();
			tcr_lst.add(viewTc.getTrainingCenterById(con,prof.my_top_tc_id));
		}
		
		if (tcr_lst != null) {
			Iterator itr = tcr_lst.iterator();
			while(itr.hasNext()) {
				DbTrainingCenter tcr =(DbTrainingCenter) itr.next();
				if(tcr != null ) {
					tcr_vec.addElement(new Long(tcr.getTcr_id()));
				}
			}
		}else{
			tcr_vec.add(0L);
		}
		if(year == -1) {
			long minYear = getMinTraPlanYear(con, wizbini);
			year = minYear;
		}
		if(month == -1) {
			Timestamp cur_time = cwSQL.getTime(con);
			Calendar today=Calendar.getInstance();
			today.setTime(cur_time);
			month = today.get(Calendar.MONTH)+1;
		}
		if (tpn_type == null) {
			tpn_type = "all";
		}
		if (tpn_status == null) {
			tpn_status = "all";
		}

		Vector v_plan = planLstAsVector(con, cwUtils.vector2list(tcr_vec), tpn_status, status_lst, tpn_type, year, month, page,prof.current_role);
		
		xmlBuf.append("<plan_lst>").append(cwUtils.NEWL);
		if (v_plan != null && v_plan.size() > 0) {
			for (int i = 0; i < v_plan.size(); i++) {
				dbTpTrainingPlan plan = (dbTpTrainingPlan) v_plan.elementAt(i);
				xmlBuf.append("<plan id=\"").append(plan.tpn_id).append("\" ") 
	                   .append(" type=\"").append(plan.tpn_type).append("\"> ")
				       .append("<code>").append(cwUtils.esc4XML(cwUtils.escNull(plan.tpn_code))).append("</code>")
				       .append("<name>").append(cwUtils.esc4XML(cwUtils.escNull(plan.tpn_name))).append("</name>")
				       .append("<status>").append(cwUtils.esc4XML(plan.tpn_status)).append("</status>")
				       .append("<training_center tcr_id=\"").append(plan.tpn_tcr_id).append("\" >")
				       .append(cwUtils.esc4XML(plan.tpn_tcr_title)).append("</training_center>");
		        xmlBuf.append("</plan>");
			}
		}
		xmlBuf.append("<cur_specify")
	       .append(" year=\"").append(year).append("\" ") 
	       .append(" month=\"").append(month).append("\" ")
           .append(" plan_type=\"").append(cwUtils.esc4XML(tpn_type.toUpperCase())).append("\" ")
	       .append(" plan_status=\"").append(cwUtils.esc4XML(tpn_status.toUpperCase())).append("\" ")
	       .append(" >");
	    xmlBuf.append("<is_filter>").append(is_filter).append("</is_filter>");  
		xmlBuf.append(getYearList(con, wizbini));
		xmlBuf.append("</cur_specify>");
		xmlBuf.append("</plan_lst>").append(cwUtils.NEWL);
		page.totalPage = (int)Math.ceil((float)page.totalRec/(float)page.pageSize);
		xmlBuf.append(page.asXML());
		return xmlBuf.toString();
	}

	public String getYearList(Connection con, WizbiniLoader wizbini) throws SQLException {
		StringBuffer xmlBuf = new StringBuffer();
		long minYear = getMinTraPlanYear(con, wizbini);
		long maxYear = minYear + 5;
		xmlBuf.append("<year_lst>");
		for (int i = (int)maxYear; i >= minYear; i--) {
			xmlBuf.append("<year>").append(i).append("</year>");
		}
		xmlBuf.append("</year_lst>");
		xmlBuf.append("<month_lst>");
		for (int i = 1; i < 13; i++) {
			xmlBuf.append("<month>").append(i).append("</month>");
		}
		xmlBuf.append("</month_lst>");
		return xmlBuf.toString();
	}
	
	/**
	 * @return 根据过滤条件搜索的培训计划
	 * @throws SQLException
	 */
	private Vector planLstAsVector(Connection con, String tcr_id_lst, String tpn_status, String[] status_lst, String tpn_type, long year, 
			long month, cwPagination page , String current_role) throws SQLException {
		Vector v_plan = new Vector();
		StringBuffer SQLBuf = new StringBuffer(1024);
		SQLBuf.append(" SELECT tpn_id, tpn_code, tpn_name, tpn_type, tpn_status, tpn_tcr_id, tcr_title ")
			.append(" FROM tpTrainingPlan, tcTrainingCenter ")
			.append(" WHERE tcr_status = ? AND tcr_id = tpn_tcr_id ");
		if(AccessControlWZB.isRoleTcInd(current_role)){
			SQLBuf.append(" AND tpn_tcr_id IN " + tcr_id_lst);
		}
		
		if(status_lst !=null && status_lst.length > 0) {
			SQLBuf.append(" AND tpn_status IN ").append(cwUtils.array2list(status_lst));
		}
		
		if (year != 0) {
			SQLBuf.append(" AND " + cwSQL.getPartOfDate("tpn_date", cwSQL.YEAR) + " = ? ");
		}
		
		if (month != 0) {
			SQLBuf.append(" And " + cwSQL.getPartOfDate("tpn_date", cwSQL.MONTH)+ " = ? ");
		}
		
		if (tpn_status != null && !tpn_status.equalsIgnoreCase("all")) {
			SQLBuf.append(" And tpn_status = ? ");	
		}
		
		if (tpn_type != null && !tpn_type.equalsIgnoreCase("all")) {
			SQLBuf.append(" And tpn_type = ? ");
		}
		
		if (page.sortCol == null) {
			page.sortCol ="tpn_code";
		}
		
		if (page.sortOrder == null) {
			page.sortOrder = SORT_BY_ASC;
		}
		
		if (page != null){
			if (page.pageSize <= 0) {
				page.pageSize = 10;
			}
			if (page.curPage <= 0) {
				page.curPage = 1;
			}			
		}
		
		SQLBuf.append(" Order by ").append(page.sortCol);
		SQLBuf.append(" ").append(page.sortOrder);
		
		PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
		int index = 1;
		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
		if (year != 0) {
			stmt.setLong(index++, year);
		}
		if (month != 0) {
			stmt.setLong(index++, month);
		}
		if (tpn_status != null && !tpn_status.equalsIgnoreCase("all")) {
			stmt.setString(index++, tpn_status);
		}
		if (tpn_type != null && !tpn_type.equalsIgnoreCase("all")) {
			stmt.setString(index++, tpn_type);
		}
		
		int count = 1;
		dbTpTrainingPlan plan;
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			if ((count > (page.curPage - 1) * page.pageSize) && (count <= page.curPage * page.pageSize)) {
				plan = new dbTpTrainingPlan();
				plan.tpn_id = rs.getLong("tpn_id");
				plan.tpn_code = rs.getString("tpn_code");
				plan.tpn_name = rs.getString("tpn_name");
				plan.tpn_type = rs.getString("tpn_type");
				plan.tpn_status = rs.getString("tpn_status");
				plan.tpn_tcr_title = rs.getString("tcr_title");
				plan.tpn_tcr_id = rs.getLong("tpn_tcr_id");
				v_plan.addElement(plan);
			}
			page.totalRec++;
			count++;
		}
		stmt.close();
		return v_plan;
	}

	/**提交编外计划，如果当前计划所在培训组织是顶层，则状态直接设为"已审批"；否则为"等待审批"
	 * @param con
	 * @param tpn_id
	 * @param tpn_update_timestamp
	 * @param prof 
	 * @throws cwSysMessage 
	 * @throws qdbException 
	 * @throws SQLException 
	 */
	public void referMarkupPlan(Connection con, long tpn_id, Timestamp tpn_update_timestamp, loginProfile prof) throws qdbException, cwSysMessage, SQLException {
	    dbTpTrainingPlan tpn = new dbTpTrainingPlan();
	    tpn.tpn_update_timestamp = tpn_update_timestamp;
	    tpn.tpn_id = tpn_id;
	    tpn.checkTimeStamp(con);
	    tpn.tpn_tcr_id = tpn.getTcrIDbyTpnID(con);
	    long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
	    if(tpn.tpn_tcr_id == sup_tcr_id){
	      tpn.tpn_status = dbTpTrainingPlan.TPN_STATUS_APPROVED;
	    }else{
			long parent_tcr_id = DbTrainingCenter.getParentTcId(con, tpn.tpn_tcr_id);
			Vector tadm_vec = DbTrainingCenter.getTcTaOfficer(con, parent_tcr_id, prof.current_role);
			if (tadm_vec == null || tadm_vec.size() == 0) {
				throw new cwSysMessage("TPN008");
			}
			tpn.tpn_status = dbTpTrainingPlan.TPN_STATUS_PENDING;
	    }
	    Timestamp curTime =  cwSQL.getTime(con);
	    tpn.tpn_update_timestamp = curTime;
	    tpn.tpn_update_usr_id = prof.usr_id;
	    tpn.tpn_submit_timestamp = curTime;
	    tpn.tpn_submit_usr_id = prof.usr_id;
	    tpn.upd_status(con);
	}
	public static void updaePlanStatus(Connection con, long tpn_id, loginProfile prof) throws SQLException {
	    Timestamp curTime =  cwSQL.getTime(con);
	    dbTpTrainingPlan tpn = new dbTpTrainingPlan();
	    tpn.tpn_id = tpn_id;
	    tpn.get(con);
	    tpn.tpn_status = dbTpTrainingPlan.TPN_STATUS_IMPLEMENTED;
	    tpn.tpn_update_timestamp = curTime;
	    tpn.tpn_update_usr_id = prof.usr_id;
	    tpn.upd_status(con);
	}

	/** 删除年度计划记录, 同时删除年度计划文件
	 * @param con
	 * @param wizbini
	 * @param year
	 * @param tcr_id
	 * @param ypn_tcr_id
	 * @param upd_timestamp
	 * @param prof
	 * @throws cwSysMessage 
	 * @throws qdbException 
	 * @throws cwException 
	 * @throws SQLException 
	 */
	public void delYearPlan(Connection con, WizbiniLoader wizbini, long ypn_year, long ypn_tcr_id, Timestamp upd_timestamp) 
	  throws qdbException, cwSysMessage, SQLException, cwException {
        dbTpYearPlan ypn = new dbTpYearPlan();
        ypn.ypn_update_timestamp = upd_timestamp;
        ypn.ypn_year = ypn_year;
        ypn.ypn_tcr_id = ypn_tcr_id;
        ypn.checkTimeStamp(con);
        ypn.del(con, wizbini);
	}

	public void submitYearPlan(Connection con, long ypn_year, long ypn_tcr_id, Timestamp upd_timestamp, loginProfile prof, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException {
		dbTpYearPlan ypn = new dbTpYearPlan();
		ypn.ypn_update_timestamp = upd_timestamp;
		ypn.ypn_year = ypn_year;
		ypn.ypn_tcr_id = ypn_tcr_id;
		ypn.checkTimeStamp(con);
        long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
        if (ypn.ypn_tcr_id == sup_tcr_id) {
			ypn.ypn_status = dbTpYearPlan.YPN_STATUS_APPROVED;
			DecomposePlan(con, wizbini, ypn.ypn_year, prof, ypn.ypn_tcr_id);
		} else {
			long parent_tcr_id = DbTrainingCenter.getParentTcId(con, ypn.ypn_tcr_id);
			Vector tadm_vec = DbTrainingCenter.getTcTaOfficer(con, parent_tcr_id, prof.current_role);
			if (tadm_vec == null || tadm_vec.size() == 0) {
				throw new cwSysMessage("TPN008");
			}
			ypn.checkSubmitTimeStamp(con);
			ypn.ypn_status = dbTpYearPlan.YPN_STATUS_PENDING;
		}
        Timestamp curTiime = cwSQL.getTime(con);
		ypn.ypn_update_timestamp =  curTiime;
		ypn.ypn_update_usr_id = prof.usr_id;
		ypn.ypn_submit_timestamp = curTiime;
		ypn.ypn_submit_usr_id = prof.usr_id;
		ypn.upd_status(con);
	}
	
	public static String getYearPlanXML(Connection con, WizbiniLoader wizbini, loginProfile prof, cwPagination page) throws SQLException, cwSysMessage, cwException {
		StringBuffer xml = new StringBuffer(1024);
		boolean isSupTa = ViewTrainingCenter.isSuperTA(con, prof.root_ent_id, prof.usr_ent_id, prof.current_role);
		if(!AccessControlWZB.isRoleTcInd(prof.current_role) 
				&& AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_YEAR_PALN)){
			isSupTa =true;
		}
		if (isSupTa) {
			long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
			dbTpYearPlan tpy = new dbTpYearPlan();
			Vector vec_year = tpy.getSupTcYearPlan(con, sup_tcr_id);
			long minYear = getMinTraPlanYear(con, wizbini);	
			long maxYear = minYear + 5;
    		xml.append("<sup_tc_year_lst sup_tcr_id=\"").append(sup_tcr_id).append("\" title=\"").append(cwUtils.esc4XML(cwUtils.escNull(DbTrainingCenter.getTcrTitle(con, sup_tcr_id)))).append("\">");
    		for(int i = (int)maxYear; i>=minYear; i--) {
    			if(!vec_year.contains(new Long(i))) {
    				xml.append("<sup_tc_year>").append(i).append("</sup_tc_year>");
    			}
    		}
    		xml.append("</sup_tc_year_lst>");
		}
		Vector yearPlan = new Vector();
		yearPlan = getBranchTcYearPlanLst(con, prof, page);
		xml.append("<year_plan_lst>");
		for(int i=0; i<yearPlan.size(); i++) {
			tpYearPlan yp = (tpYearPlan)yearPlan.elementAt(i);
			xml.append("<year_plan year=\"").append(yp.yp_year).append("\" ");
			xml.append(" status=\"");
			if(yp.yp_status == null) {
				xml.append("");
			} else {
				xml.append(yp.yp_status);
			}
			xml.append("\" submit_start_time=\"");
			xml.append(cwUtils.escNull(yp.yp_submit_start_time));
			xml.append("\" submit_end_time=\"");
			xml.append(cwUtils.escNull(yp.yp_submit_end_time));
			xml.append("\" tcr_id=\"").append(yp.yp_tcr_id);
			xml.append("\" tcr_title=\"").append(cwUtils.esc4XML(cwUtils.escNull(yp.yp_tcr_title)));
			xml.append("\" update_time=\"").append(cwUtils.escNull(yp.yp_upd_time))
				.append("\" file_name=\"").append(cwUtils.escNull(yp.yp_file_name));
			xml.append("\" hasSubBtn=\"").append(yp.hasSubBtn)
				.append("\" hasRepBtn=\"").append(yp.hasRepBtn)
				.append("\" hasDelBtn=\"").append(yp.hasDelBtn)
				.append("\" hasDwlBtn=\"").append(yp.hasDwlBtn)
				.append("\" hasAddBtn=\"").append(yp.hasAddBtn)
				.append("\" >");
			if(yp.hasDwlBtn) {
				xml.append("<uri>").append("/plan/").append(yp.yp_ypn_tcr_id).append("/").append(yp.yp_year).append("/").append(yp.yp_file_name).append("</uri>");
			}
			xml.append("</year_plan>");
		}
		xml.append("</year_plan_lst>");
		page.totalPage = (int)Math.ceil((float)page.totalRec/(float)page.pageSize);
		xml.append(page.asXML());
		return xml.toString();
	}
	
	private static Vector getBranchTcYearPlanLst(Connection con, loginProfile prof, cwPagination page) throws SQLException {
		Vector vec = new Vector();
		Timestamp cur_time = cwSQL.getTime(con);
		StringBuffer sql = new StringBuffer(512);
		sql.append(" select ysg_year, ysg_submit_start_datetime, ysg_submit_end_datetime, ypn_year, ypn_status, ypn_tcr_id, ypn_approve_timestamp, ypn_update_timestamp, ypn_file_name, c.tcr_id tcr_id, c.tcr_title tcr_title")
		   .append(" from tpYearSetting")
		   .append(" inner join tcTrainingCenter p on ( ysg_tcr_id = p.tcr_id  and p.tcr_ste_ent_id = ? and p.tcr_status = ?) ")
		   .append(" inner join tcTrainingCenter c on ( c.tcr_parent_tcr_id = p.tcr_id and c.tcr_ste_ent_id = ? and c.tcr_status = ? ) ");
			if(AccessControlWZB.isRoleTcInd(prof.current_role)){
				sql.append(" inner join tcTrainingCenterOfficer on ( tco_tcr_id = c.tcr_id and  tco_usr_ent_id = ? and tco_rol_ext_id = ? )");
			}
		   
		sql.append(" left join tpYearPlan on ( ypn_year = ysg_year and ypn_tcr_id = c.tcr_id ) ")
		   .append(" where ").append(cwSQL.subFieldLocation("ysg_child_tcr_id_lst","c.tcr_id", true));
		if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
    	  	sql.append(" and ypn_tcr_id = ?");
		}
	   sql.append(" union ");
	   sql.append(" select ypn_year ysg_year,").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP)).append(" ysg_submit_start_datetime,")
	      .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP)).append(" ysg_submit_end_datetime,")
	      .append(" ypn_year, ypn_status, ypn_tcr_id,")
	      .append(cwSQL.get_null_sql(cwSQL.COL_TYPE_TIMESTAMP)).append(" ypn_approve_timestamp,")
	      .append(" ypn_update_timestamp, ypn_file_name, tcr_id, tcr_title")
	      .append(" from tpYearPlan ")
	      .append(" inner join tcTrainingCenter on (ypn_tcr_id = tcr_id and tcr_status = ? and tcr_parent_tcr_id is null  )");
	      if(AccessControlWZB.isRoleTcInd(prof.current_role)){
	    	  	sql.append(" inner join tcTrainingCenterOfficer on (tcr_id = tco_tcr_id and tco_usr_ent_id = ? and tco_rol_ext_id = ? )");
			}
	     sql.append(" where ypn_status in (?, ?) ");
	     if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
    	  	sql.append(" and ypn_tcr_id = ?");
	     }
	     
		if(page != null) {
			if (page.sortCol == null) {
				page.sortCol ="ysg_year";
			}
			if (page.sortOrder == null) {
				page.sortOrder = SORT_BY_DESC;
			}
			
			if (page != null){
				if (page.pageSize <= 0) {
					page.pageSize = 10;
				}
				if (page.curPage <= 0) {
					page.curPage = 1;
				}			
			}
		} else {
			page = new cwPagination();
			page.sortCol ="ypn_year";
			page.sortOrder = SORT_BY_DESC;
		}
		sql.append(" order by ").append(page.sortCol).append(" ").append(page.sortOrder);
		
		PreparedStatement stmt = con.prepareStatement(sql.toString());
        try{
    		int index = 1;
    		stmt.setLong(index++, prof.root_ent_id);
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		stmt.setLong(index++, prof.root_ent_id);
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
    			stmt.setLong(index++, prof.usr_ent_id);
    			stmt.setString(index++, prof.current_role);
    		}
	   	     if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
	   	    	stmt.setLong(index++, prof.my_top_tc_id);
	   	     }
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
    			stmt.setLong(index++, prof.usr_ent_id);
    			stmt.setString(index++, prof.current_role);
    		}
    		stmt.setString(index++, dbTpYearPlan.YPN_STATUS_PREPARED);
    		stmt.setString(index++, dbTpYearPlan.YPN_STATUS_APPROVED);
	   	     if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
	   	    	stmt.setLong(index++, prof.my_top_tc_id);
	   	     }
    		ResultSet rs = stmt.executeQuery();
    		int count = 0;
    		while (rs.next()) {
    			if(count >= ((page.curPage-1) * page.pageSize) && count < (page.curPage * page.pageSize) ) {
    				tpYearPlan yp = new tpYearPlan();
    				Timestamp end_time = rs.getTimestamp("ysg_submit_end_datetime");
    				if(end_time != null) {
    						yp.yp_year = rs.getLong("ysg_year");
    						yp.yp_ypn_tcr_id = rs.getLong("ypn_tcr_id");
    						yp.yp_tcr_title = rs.getString("tcr_title");
    						yp.yp_tcr_id = rs.getLong("tcr_id");
    						yp.yp_submit_end_time = end_time;
    						yp.yp_approve_timestamp = rs.getTimestamp("ypn_approve_timestamp");
    						yp.yp_submit_start_time = rs.getTimestamp("ysg_submit_start_datetime");
    						yp.yp_status = rs.getString("ypn_status");
    						yp.yp_upd_time = rs.getTimestamp("ypn_update_timestamp");
    						yp.yp_file_name = rs.getString("ypn_file_name");
    						checkButton(yp, cur_time);
    						vec.add(yp);
    				} else {
    					yp.yp_year = rs.getLong("ysg_year");;
    					yp.yp_ypn_tcr_id = rs.getLong("ypn_tcr_id");
    					yp.yp_tcr_id = rs.getLong("tcr_id");
    					yp.yp_tcr_title = rs.getString("tcr_title");
    					yp.yp_status = rs.getString("ypn_status");
    					yp.yp_upd_time = rs.getTimestamp("ypn_update_timestamp");
    					yp.yp_submit_end_time = end_time;
    					yp.yp_submit_start_time = rs.getTimestamp("ysg_submit_start_datetime");
    					yp.yp_file_name = rs.getString("ypn_file_name");
    					if(yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_APPROVED)) {
    						yp.hasDwlBtn = true;//superTc提交过的纪录直接进入“已审批”状态，所以不会有提交、更换、删除按钮，只有下载
    					} else if(yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_PREPARED)) {
    						yp.hasSubBtn = true;
    						yp.hasRepBtn = true;
    						yp.hasDelBtn = true;
    						yp.hasDwlBtn = true;
    					}
    					vec.add(yp);
    				}
    			}
    			count++;
    		}
    		page.totalRec = count;
        } finally {
            if(stmt !=null ){
                stmt.close();
            }
        }
		return vec;
	}
	
	//提交	在提交期限内，且状态是准备中或审批未通过才有此按钮。
	//更换	在截至时间之前（包括期限内和开始时间前），且状态是准备中或审批未通过才有此按钮。
	//删除	上传过计划，且状态是准备中或审批未通过才有此按钮，与时间无关。
	//下载	上传过计划，与状态无关，与时间无关。
	//添加	还没上传过计划（在截至时间之前，不管是否到了开始时间）
	private static void checkButton(tpYearPlan yp, Timestamp cur_time) {
		if(yp != null) {
			if(yp.yp_status != null && (yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_PREPARED)
					|| yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_DECLINED))) {
				if(cur_time.before(yp.yp_submit_end_time) || cur_time.equals(yp.yp_submit_end_time)) {
					yp.hasRepBtn = true;
					if(cur_time.after(yp.yp_submit_start_time) || cur_time.equals(yp.yp_submit_start_time)) {
						if (yp.yp_status.equalsIgnoreCase(dbTpYearPlan.YPN_STATUS_DECLINED)) {
							if(yp.yp_approve_timestamp !=null && yp.yp_approve_timestamp.equals(yp.yp_upd_time)) {
								yp.hasSubBtn = false;
							} else {
								yp.hasSubBtn = true;		
							}
						} else {
							yp.hasSubBtn = true;		
						}
					}
				}
			}
			if(yp.yp_ypn_tcr_id > 0) {
				yp.hasDwlBtn = true;
				if(yp.yp_status != null && (yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_PREPARED)
						|| yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_DECLINED))) {
					yp.hasDelBtn = true;
				}
			} else {
				if(cur_time.before(yp.yp_submit_end_time) || cur_time.equals(yp.yp_submit_end_time)) {
					yp.hasAddBtn = true;
				}
			}
		}
	}
	
	private static Vector getSupTcYearPlanLst(Connection con, loginProfile prof, long tcr_id) throws SQLException {
		Vector vec = new Vector();
		StringBuffer sql = new StringBuffer(512);
		sql.append(" select ypn_year, ypn_status, ypn_update_timestamp, ypn_file_name")
			.append(" from tpYearPlan, tcTrainingCenter")
			.append(" where ypn_tcr_id =tcr_id and tcr_id = ? and tcr_status = ? ")
			.append(" and ypn_status in(?, ?)");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
        try{
    		int index = 1;
    		stmt.setLong(index++, tcr_id);
    		stmt.setString(index++, DbTrainingCenter.STATUS_OK);
    		stmt.setString(index++, dbTpYearPlan.YPN_STATUS_PREPARED);
    		stmt.setString(index++, dbTpYearPlan.YPN_STATUS_APPROVED);
    		ResultSet rs = stmt.executeQuery();
    		Vector vec_year = new Vector();
    		while (rs.next()) {
    			long year = rs.getLong("ypn_year");
    			vec_year.add(new Long(year));
    			tpYearPlan yp = new tpYearPlan();
    			yp.yp_year = year;
    			yp.yp_status = rs.getString("ypn_status");
    			yp.yp_upd_time = rs.getTimestamp("ypn_upd_timestamp");
    			yp.yp_file_name = rs.getString("ypn_file_name");
    			if(yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_APPROVED)) {
    				yp.hasDwlBtn = true;//superTc提交过的纪录直接进入“已审批”状态，所以不会有提交、更换、删除按钮，只有下载
    			} else if(yp.yp_status.equals(dbTpYearPlan.YPN_STATUS_PREPARED)) {
    				yp.hasSubBtn = true;
    				yp.hasRepBtn = true;
    				yp.hasDelBtn = true;
    				yp.hasDwlBtn = true;
    			}
    			vec.add(yp);
    		}
    		Calendar c = Calendar.getInstance(Locale.CHINESE); 
    		int cur_year = c.get(Calendar.YEAR);
    		for(int i=cur_year+5; i>=cur_year+1; i--) {
    			if(!vec_year.contains(new Long(i))) {
    				tpYearPlan yp = new tpYearPlan();
    				yp.yp_year = i;
    				yp.hasAddBtn = true; //构造的年份还没有上传过，只有添加按钮
    				vec.add(yp);
    			}
    		}
        } finally {
            if(stmt !=null ){
                stmt.close();
            }
        }
		return vec;
	}
	
	/**
	 * 编外审批列表
	 * @param con
	 * @param status
	 * @param sear_code_name
	 * @param page
	 * @return
	 * @throws SQLException
	 * @throws cwSysMessage 
	 */
	public String getMakeupplanXml(Connection con, loginProfile prof, String status, cwPagination page) throws SQLException, cwSysMessage{
		StringBuffer result = new StringBuffer();
		ViewTrainingCenter viewTc = new ViewTrainingCenter();
		List tcr_lst = null;
		//由于新加的自定义角色功能有不和培训中心相关的逻辑与计划管理原来的设计有些相冲
		//经讨论后修改为若角色不和培训中心相关的就显示用户的顶层培训中心的内容
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			tcr_lst = viewTc.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, false);
		}else{
			tcr_lst = new ArrayList();
			tcr_lst.add(viewTc.getTrainingCenterById(con, prof.my_top_tc_id));
		}
		
		Vector tcr_vec = new Vector();
		if (tcr_lst != null) {
			Iterator itr = tcr_lst.iterator();
			while(itr.hasNext()) {
				DbTrainingCenter tcr =(DbTrainingCenter) itr.next();
				if(tcr != null ) {
					tcr_vec.addElement(new Long(tcr.getTcr_id()));
				}
			}
		} else {
			tcr_vec.add(0L);
		}
		dbTpTrainingPlan trainingPlan = new dbTpTrainingPlan();
		result.append(getplanStatusXml());
		result.append(trainingPlan.getPlanStausCntXml(con, cwUtils.vector2list(tcr_vec),prof.current_role));
		result.append(trainingPlan.getPlanXml(con, status, cwUtils.vector2list(tcr_vec), page,prof.current_role));
		return result.toString();
	}
	/**
	 * 审批编外计划、年度计划
	 * @param con
	 * @param statusType
	 * @param id
	 * @param update_timestamp
	 * @throws SQLException
	 * @throws qdbErrMessage
	 * @throws cwSysMessage 
	 * @throws qdbException 
	 * @throws SQLException 
	 * @throws cwException 
	 */
	public void auditingPlan(Connection con, String statusType, long[] id, Timestamp[] update_timestamp, String plantype, WizbiniLoader wizbini, loginProfile prof, long[] tcr_id) 
		throws qdbException, cwSysMessage, SQLException, cwException{
		if (plantype.equals(dbTpTrainingPlan.TPN_TYPE_MAKEUP)){
			dbTpTrainingPlan trainingPlan = null;
			for (int i=0; i<id.length; i++){
				trainingPlan = new dbTpTrainingPlan();
				trainingPlan.tpn_id = id[i];
				trainingPlan.tpn_approve_usr_id = prof.usr_id;
				trainingPlan.tpn_update_timestamp = update_timestamp[i];
				trainingPlan.tpn_update_usr_id = prof.usr_id;
			    trainingPlan.checkTimeStamp(con);
			    if (statusType.equals("A")){
			    	trainingPlan.tpn_status = dbTpTrainingPlan.TPN_STATUS_APPROVED;	    	
			    }else{
			    	trainingPlan.tpn_status = dbTpTrainingPlan.TPN_STATUS_DECLINED;
			    }
			    trainingPlan.auditingPlan(con);
			}			
		}else{
			dbTpYearPlan yearPlan = null;
			for (int i=0; i<id.length; i++){
				yearPlan = new dbTpYearPlan();
				yearPlan.ypn_year = id[i];
				yearPlan.ypn_tcr_id = tcr_id[i];
				yearPlan.ypn_update_timestamp = update_timestamp[i];
				yearPlan.ypn_update_usr_id = prof.usr_id;
			    yearPlan.ypn_approve_usr_id = prof.usr_id;
				yearPlan.checkTimeStamp(con);
			    if (statusType.equals("A")){
			    	yearPlan.ypn_status = dbTpTrainingPlan.TPN_STATUS_APPROVED;	 
			    }else{
			    	yearPlan.ypn_status = dbTpTrainingPlan.TPN_STATUS_DECLINED;
			    }
			    yearPlan.auditingPlan(con);
			    if (statusType.equals("A")){
			    	//分解年度计划
			    	DecomposePlan(con, wizbini, yearPlan.ypn_year, prof, tcr_id[i]);
			    }
			}			
		}
	}
	/**
	 * 计划状态列表
	 * @return
	 */
	public String getplanStatusXml(){
		StringBuffer result = new StringBuffer();
		result.append("<process>")
			  .append("<status id=\"").append(dbTpTrainingPlan.TPN_STATUS_APPROVED).append("\"/>")
			  .append("<status id=\"").append(dbTpTrainingPlan.TPN_STATUS_PENDING).append("\"/>")
			  .append("<status id=\"").append(dbTpTrainingPlan.TPN_STATUS_DECLINED).append("\"/>");
		result.append("</process>");
		return result.toString();
	}
	/**
	 * 年度审核列表
	 * @param con
	 * @param status
	 * @param sear_code_name
	 * @param page
	 * @return
	 * @throws SQLException
	 * @throws cwSysMessage 
	 */
	public String getYearplanXml(Connection con, loginProfile prof, String status,  cwPagination page) throws SQLException, cwSysMessage{
		StringBuffer result = new StringBuffer();
		ViewTrainingCenter viewTc = new ViewTrainingCenter();
		List tcr_lst = null;
		//由于新加的自定义角色功能有不和培训中心相关的逻辑与计划管理原来的设计有些相冲
		//经讨论后修改为若角色不和培训中心相关的就显示用户的顶层培训中心的内容
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			 tcr_lst = viewTc.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, false);
		}else{
			tcr_lst = new ArrayList();
			tcr_lst.add(viewTc.getTrainingCenterById(con, prof.my_top_tc_id));
		}
		
		Vector tcr_vec = new Vector();
		if (tcr_lst != null) {
			Iterator itr = tcr_lst.iterator();
			while(itr.hasNext()) {
				DbTrainingCenter tcr =(DbTrainingCenter) itr.next();
				if(tcr != null ) {
					tcr_vec.addElement(new Long(tcr.getTcr_id()));
				}
			}
		}  else {
			tcr_lst = new ArrayList();
			tcr_vec.add(0L);
		}
		dbTpYearPlan yearplan = new dbTpYearPlan();
		result.append(getplanStatusXml());
		result.append(yearplan.getPlanStausCntXml(con, cwUtils.vector2list(tcr_vec)));
		result.append(yearplan.getPlanXml(con, status, cwUtils.vector2list(tcr_vec), page));
		return result.toString();
	}
	/**
	 * 分解年度计划
	 * @param con
	 * @param sourceFile
	 * @param prof
	 * @param wizbini
	 * @return
	 * @throws cwException
	 * @throws cwSysMessage
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwSysMessage 
	 * @throws cwException 
	 */
    public void DecomposePlan(Connection con, WizbiniLoader wizbini, long ypn_year, loginProfile prof, long ypn_tcr_id)throws SQLException, qdbException, cwSysMessage, cwException {
        try{
        	dbTpYearPlan yearPlan = new dbTpYearPlan();
        	yearPlan.ypn_year = ypn_year;
        	yearPlan.ypn_tcr_id = ypn_tcr_id;
        	yearPlan.get(con);
    	    String DirPath = wizbini.getWebDocRoot() + cwUtils.SLASH + dbTpYearPlan.YPN_FILE_DIR_UPLOAD + cwUtils.SLASH + ypn_tcr_id + cwUtils.SLASH + ypn_year;
    	    File dir = new File(DirPath);
            String[] fList = dir.list();; 
            if(null == fList || fList.length == 0){
                throw new cwSysMessage("TPN009");
            }
            String sourceFile = DirPath + cwUtils.SLASH + fList[0];
    	    File inputWorkbook = new File(sourceFile);
            tpPlanUpLoad up_plan = new tpPlanUpLoad (con, prof.label_lan, ypn_tcr_id, ypn_year);
            up_plan.uploadPlan(inputWorkbook,  null, true, prof,ypn_year, ypn_tcr_id);
            if(!up_plan.passed){
                throw new cwSysMessage("TPN009");
            }
           
        }  catch (IOException e) {
            throw new cwException("read file error:" + e.getMessage());
        }
    }
    /**
     * 从excel文件构造dbTpTrainingPlan对象
     * @param con
     * @param case_index
     * @param inValue
     * @param plan
     * @param entity
     * @param root_ent_id
     * @throws qdbException 
     * @throws cwException
     * @throws SQLException
     * @throws qdbException
     * @throws SQLException 
     */
    private void putField(Connection con, int case_index, String inValue, dbTpTrainingPlan plan, long root_ent_id) throws qdbException, SQLException{
        switch (case_index) {
            case 0:
            	inValue += " 00:00:00.0";
            	plan.tpn_date = Timestamp.valueOf(inValue);
                break;
            case 1:
            	plan.tpn_code = inValue;
                break;
            case 2:
            	plan.tpn_name = inValue;
                break;
            case 3:
//            	plan.tpn_grade = Integer.parseInt(inValue);
                break;
            case 4:
//            	plan.tpn_rector_list = new long[1];
//            	plan.tpn_rector_list[0] = dbRegUser.getEntIdBySteUsrId(con,inValue);
            	break;
            case 5:
//            	plan.tpn_master_list = new long[1];
//            	plan.tpn_master_list[0] = dbRegUser.getEntIdBySteUsrId(con,inValue);
            	break;
            case 6:
//            	plan.tpn_tnd_id = aeTreeNode.getTreeNodeIdByCode(con, inValue, root_ent_id);
            	break;
            case 7:
//            	plan.tpn_content = inValue;
            	break;
            case 8:
            	plan.tpn_aim = inValue;
            	break;
            case 9:
            	if(inValue == "") {
            		inValue = "0";
            	}
            	plan.tpn_lrn_count = Long.parseLong(inValue);
            	break;
            case 10:
            	if(inValue == "") {
            		inValue = "0";
            	}
//            	plan.tpn_days = Long.parseLong(inValue);
            	break;
            case 11:
            	if (!inValue.equals("")){
                	inValue += " 00:00:00.0";
//                	plan.tpn_start_date = Timestamp.valueOf(inValue);
            	}
            	break;
            case 12:
            	if (!inValue.equals("")){
                	inValue += " 23:59:00.0";
//                	plan.tpn_end_date = Timestamp.valueOf(inValue);            		
            	}
            	break;
            case 13:
            	if(inValue == "") {
            		inValue = "0";
            	}
//            	plan.tpn_pattern_ind = Long.parseLong(inValue);
            	break;
            case 14:
            	if(inValue == "") {
            		inValue = "0";
            	}
            	plan.tpn_fee = Float.parseFloat(inValue);
            	break;
            case 15:
            	plan.tpn_remark  = inValue;
            	break;
            case 16:
//            	plan.tpn_usg_ent_id = dbEntity.getEntIdByEnt_ste_uid(con, inValue);
            default:
                break;
        } 
  }
    /**
     * 分解成多个年度计划后插入数据库
     * @param con
     * @param prof
     * @param plan
     * @param tcr_id
     * @throws SQLException
     * @throws qdbException
     * @throws cwSysMessage
     */
    public void DecomposeExec(Connection con, loginProfile prof, dbTpTrainingPlan plan,  long tcr_id) throws SQLException, qdbException, cwSysMessage{
		Timestamp curTime = cwSQL.getTime(con);
		plan.tpn_tcr_id = tcr_id;
    	plan.tpn_type = dbTpTrainingPlan.TPN_TYPE_YEAR;
        plan.tpn_status = dbTpTrainingPlan.TPN_STATUS_APPROVED;
        plan.tpn_approve_usr_id = prof.usr_id;
        plan.tpn_approve_timestamp = curTime;
    	insTrainingPlan(con, prof, plan);
    }
    
	public void insTrainingPlan(Connection con, loginProfile prof, dbTpTrainingPlan tpTp) throws SQLException, qdbException, cwSysMessage {
		Timestamp curTime = cwSQL.getTime(con);
        
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(tpTp.tpn_date); 

        tpTp.tpn_code = "temp_code";
        
		tpTp.tpn_create_timestamp = curTime;
		tpTp.tpn_create_usr_id = prof.usr_id;
		tpTp.tpn_update_timestamp = curTime;
		tpTp.tpn_update_usr_id = prof.usr_id;
		tpTp.ins(con);
		//tpn_code auto set
		tpTp.tpn_code = tpTp.tpn_id + "";
		tpTp.updCode(con);
		return ;
	}
    
    public String genPlanCodeSuffix(Connection con, String prefix) throws SQLException {
       String code = "";
       String SQL = " SELECT max(tpn_code) code FROM tpTrainingPlan WHERE tpn_code like ? ";
       PreparedStatement stmt = con.prepareStatement(SQL);
       stmt.setString(1, prefix+"%");
       ResultSet rs = stmt.executeQuery();
       String max_code = null;
       if (rs.next()){  
          max_code = rs.getString("code");
          if(max_code != null){
              code = GenCode(max_code, prefix);
          }else{
              code = prefix + "00001";

          }
       }else{
           code = prefix + "00001";
        
       }
       stmt.close();
       return code;
    }
        
    private String GenCode(String max_code, String perfix) throws SQLException{
        String temp_code =  max_code.substring(perfix.length());
        String code =  "";
        if(temp_code.startsWith("0")){
            long max_code_suffix = new Long (temp_code).longValue();
            long new_code_suffix_ = max_code_suffix +1;
            String code_suffix = new Long(new_code_suffix_).toString();
            int zero_size = temp_code.length() - code_suffix.length();
            for (int k=0; k<zero_size; k++) {
                code_suffix = "0" + code_suffix;
            }
            code = perfix + code_suffix;
        }else{
            code= perfix+ (new Long(temp_code).longValue() +1);
        }
        return code;
    }

	/** 
	 * get training class list information
	 * @param con
	 * @param prof
	 * @param status
	 * @param page
	 * @param tcr_id
	 * @return
	 * @throws SQLException
	 * @throws cwSysMessage
	 * @throws cwException
	 */
	public String getTrainingClassLstAsXML(Connection con, loginProfile prof, tpPlanReqParam urlp) throws SQLException, cwSysMessage, cwException {
		//, tpPlanReqParam urlp
		long tcr_id = urlp.tcr_id;
		StringBuffer xml = new StringBuffer();
        cwTree tree = new cwTree();
        xml.append(tree.genNavTrainingCenterTree(con, prof, true));
        if (tcr_id <= 0){
        	tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
        }
        DbTrainingCenter trainingCenter = DbTrainingCenter.getInstance(con, tcr_id);
        xml.append(trainingCenter.obj2Xml(con, true));
        
        //set param for firter record
        //set param: training center ids
        DbTrainingCenter dbtc = new DbTrainingCenter();
		dbtc.tcr_id = tcr_id;
		dbtc.setTcr_ste_ent_id(prof.root_ent_id);
		Vector tcr_vec = new Vector();
		Vector child_tc_lst = new Vector();
        if(urlp.sear_by_code_name){
            if(urlp.search_tcr_id > 0){
                tcr_vec.add(new Long(urlp.search_tcr_id));
            }else{
                child_tc_lst =DbTcRelation.getSubTcList(con,tcr_id);
                tcr_vec.addAll(child_tc_lst);
                tcr_vec.add(new Long(tcr_id));
            }
        }else{
            tcr_vec.add(new Long(tcr_id));
            child_tc_lst = dbtc.getChildTcList(con);
            if(child_tc_lst.size()>0) {
                for(int i=0; i<child_tc_lst.size(); i++){
                    DbTrainingCenter tc = (DbTrainingCenter)child_tc_lst.elementAt(i);
                    tcr_vec.add(new Long(tc.tcr_id));
                }
            }
            
        }
        /*
        //if(urlp.search_tcr_id == -1 || urlp.search_tcr_id == 0 || ){
            if( urlp.sear_by_code_name){
                child_tc_lst =DbTcRelation.getSubTcList(con,tcr_id);
                
            }else{
                child_tc_lst = dbtc.getChildTcList(con);
            }

        	tcr_vec.add(new Long(tcr_id));
        //} else {
        	tcr_vec.add(new Long(urlp.search_tcr_id));
        //}
        if(child_tc_lst.size()>0 && !urlp.sear_by_code_name) {
			for(int i=0; i<child_tc_lst.size(); i++){
				DbTrainingCenter tc = (DbTrainingCenter)child_tc_lst.elementAt(i);
				tcr_vec.add(new Long(tc.tcr_id));
			}
		}
        */
        //set param: training center date
        Timestamp cur_time = cwSQL.getTime(con);
		Calendar today=Calendar.getInstance();
		today.setTime(cur_time);
		long cur_year = today.get(Calendar.YEAR);
		if(urlp.tpn_date_year == -1) {
			urlp.tpn_date_year = cur_year;
		}
		if(urlp.tpn_date_month == -1) {
			urlp.tpn_date_month = today.get(Calendar.MONTH)+1;
		}
		//set param: training center status
		if(urlp.tpn_status == null) {
			urlp.tpn_status = "all";
		}
        
//        dbTpTrainingClass tpClass = new dbTpTrainingClass();
//        //String status, long year, long month, long search_tcr_id, String search_code
//		xml.append(tpClass.getTpTrainingClassLst(con, tcr_id, cwUtils.vector2list(tcr_vec), urlp.tpn_status, urlp.tpn_date_year, urlp.tpn_date_month, urlp.search_tcr_id, urlp.sear_code_name, urlp.sear_by_code_name, urlp.cwPage));
		Vector tcVec = dbtc.getChildTcList(con);
		String tcrTitle = DbTrainingCenter.getTcrTitle(con,tcr_id);
//		xml.append(tpClass.getPrepForFilter(tcrTitle, tcVec, tcr_id, cur_year, urlp.tpn_date_year, urlp.tpn_date_month, urlp.search_tcr_id, urlp.tpn_status, urlp.sear_code_name));
		
		return xml.toString();
	}

	public void implementPlan(Connection con, loginProfile prof,/* dbTpTrainingClass tpTc,*/ Timestamp tpn_update_timestamp) throws SQLException, qdbException, cwSysMessage {
		Timestamp curTime = cwSQL.getTime(con);
		
		dbTpTrainingPlan tpn = new dbTpTrainingPlan();
	    tpn.tpn_update_timestamp = tpn_update_timestamp;
//	    tpn.tpn_id = tpTc.tcl_tpn_id;
	    tpn.checkTimeStamp(con);
	    
	  	tpn.tpn_status = dbTpTrainingPlan.TPN_STATUS_IMPLEMENTED;
	    tpn.tpn_update_timestamp =  curTime;
	    tpn.tpn_update_usr_id = prof.usr_id;
	    tpn.upd_status(con);
	    
//	    tpTc.tcl_create_timestamp = curTime;
//		tpTc.tcl_create_usr_id = prof.usr_id;
//		tpTc.tcl_upd_timestamp = curTime;
//		tpTc.tcl_upd_usr_id = prof.usr_id;
//		tpTc.ins(con);
//		insPlanEntity(con, prof, tpTc);
//		
//		dbTpClassCriteria cct = new dbTpClassCriteria();
//		cct.clc_tcl_tpn_id = tpTc.tcl_tpn_id;
//		cct.clc_name = dbTpClassCriteria.CLC_KAOQING;
//		cct.clc_count = 1;
//		cct.clc_full_score = 0;
//		cct.clc_eligible_score = 0;
//		cct.clc_obligation_score = 0;
//		cct.clc_weight = 100;
//		cct.clc_fix_ind = 1;
//		cct.clc_create_timestamp = curTime;
//		cct.clc_create_usr_id = prof.usr_id;
//		cct.clc_upd_timestamp = curTime;
//		cct.clc_upd_usr_id = prof.usr_id;
//		cct.ins(con);
		return ;
	}
    
    public String getLayerItmLstXML(Connection con,  long tcl_tpn_id) throws SQLException, cwSysMessage {
        StringBuffer xml = new StringBuffer(); 
        dbTpTrainingPlan tpn = new dbTpTrainingPlan();
//        Vector itm_lst = aeTreeNode.getTreeNodeItmlst( con, tpn.getPlanTreeNode(con, tcl_tpn_id));
        xml.append("<itm_lst>");
//        if(itm_lst != null && itm_lst.size() > 0){
//            for(int i = 0 ; i<itm_lst.size(); i++ ){
//                aeItem itm = (aeItem)itm_lst.get(i);
//                xml.append("<itm itm_id=\"").append(itm.itm_id).append("\">");
//                xml.append("<itm_code>").append(cwUtils.esc4XML(itm.itm_code)).append("</itm_code>");
//                xml.append("<itm_title>").append(cwUtils.esc4XML(itm.itm_title)).append("</itm_title>");
//                xml.append("<itm_pattern_ind>").append(itm.itm_pattern_ind).append("</itm_pattern_ind>");
//                xml.append("<itm_duration_hour>").append((int)itm.itm_duration_hour).append("</itm_duration_hour>");
//                xml.append("</itm>");
//            }
//        }
        xml.append("</itm_lst>");
      
        return xml.toString();
    }

	
	public String getTemplateDownXml(Connection con, tpPlanReqParam urlp) throws SQLException{
		StringBuffer result = new StringBuffer();
		dbTpTrainingPlan plan = new dbTpTrainingPlan();
		plan.tpn_id = urlp.tpn_id;
		plan.get(con);
		result.append("<clc_tcl_tpn_id>")
				.append("<id>").append(urlp.tpn_id).append("</id>")
				.append("<name>").append(cwUtils.esc4XML(cwUtils.escNull(plan.tpn_name))).append("</name>")
				.append("<tcr_id>").append(urlp.tcr_id).append("</tcr_id>")
				.append("</clc_tcl_tpn_id>");
		return result.toString();
	}
	
	public String getInstructorCompletedItemAsXML(Connection con, long usr_ent_id, long owner_ent_id, Timestamp csd_end_date_start, Timestamp csd_end_date_end, String orderBy, String sortOrder, long page, long page_size, String itm_title_code) throws SQLException {
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, owner_ent_id));
		String displayName = dbRegUser.getDisplayBil(con, usr_ent_id);
        xmlBuf.append("<usr id =\"").append(usr_ent_id).append("\">").append(cwUtils.esc4XML(displayName)).append("</usr>");
		Timestamp curTime = cwSQL.getTime(con);
		if(page == 0) {
        	page = 1;
        }
        if(page_size == 0) {
        	page_size = 10;
        }
        if(orderBy == null){
        	orderBy = "tpn_code";
        }
        if(sortOrder == null) {
        	sortOrder = "asc";
        }
       
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append("select tpn_id, tpn_code, tpn_name,itm_id, itm_code, itm_title, itm_type, csd_id, csd_duration, csd_end_date")
        .append(" from tpTrainingPlan, tpClassSchedule, aeItem, tpClassCellTeacher ") 
        .append(" where tpn_id =csd_tcl_tpn_id and  itm_id=csd_itm_id and csd_id=cct_csd_id ")
        .append(" and tpn_status = ? and cct_usr_ent_id = ?  ");
        if(csd_end_date_start != null){
        	SQLBuf.append(" and csd_end_date >= ? ");
        }
        if(csd_end_date_end != null){
        	SQLBuf.append(" and csd_end_date <= ? ");
        }
        if(itm_title_code != null && !"".equals(itm_title_code)) {
        	SQLBuf.append(" and (lower(itm_title) LIKE ? OR lower(itm_code) LIKE ?) ");
        }
        SQLBuf.append("order by " + orderBy).append(" " + sortOrder);
        SQLBuf.append(" for read only with ur ");
        PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(SQLBuf.toString());
			int index = 1;
			stmt.setString(index++, dbTpTrainingPlan.TPN_STATUS_IMPLEMENTED);
			stmt.setLong(index++, usr_ent_id);
			Timestamp start_time = csd_end_date_start;
			Timestamp end_time = csd_end_date_end;
			if(start_time !=null && end_time != null && (start_time.after(end_time))) {
				Timestamp temp_date = end_time;
				end_time = start_time;
				start_time = temp_date;
			}
	        if(csd_end_date_start != null){
	        	stmt.setTimestamp(index++, start_time);
	        }
	        if(csd_end_date_end != null){
	        	stmt.setTimestamp(index++, end_time);
	        }
	        if(itm_title_code != null && !"".equals(itm_title_code)) {
				stmt.setString(index++, "%" + itm_title_code + "%");
				stmt.setString(index++, "%" + itm_title_code + "%");
	        }
			ResultSet rs = stmt.executeQuery();
	        long start = page_size * (page-1);
	        long count = 0;
	        long csd_total_duration = 0;
			xmlBuf.append("<item_list>");
			while(rs.next()) {
				if(count >= start && count <start+page_size) {
					long tpn_id = rs.getLong("tpn_id");
					String tpn_code = rs.getString("tpn_code");
					String tpn_name = rs.getString("tpn_name");
					long itm_id = rs.getLong("itm_id");
					String itm_code = rs.getString("itm_code");
					String itm_title = rs.getString("itm_title");					
					String itm_type = rs.getString("itm_type");
					long csd_id = rs.getLong("csd_id");
					long csd_duration = rs.getLong("csd_duration");
					Timestamp csd_end_date = rs.getTimestamp("csd_end_date");
					
					xmlBuf.append("<training_cell csd_id=\"").append(csd_id).append("\">")
			        .append("<end_date>").append(csd_end_date).append("</end_date>")
			        .append("<duration>").append(csd_duration).append("</duration>");

					xmlBuf.append("<plan tpn_id=\"").append(tpn_id).append("\">")
			        .append("<tpn_code>").append(cwUtils.esc4XML(tpn_code)).append("</tpn_code>")
			        .append("<tpn_name>").append(cwUtils.esc4XML(tpn_name)).append("</tpn_name>")
			        .append("</plan>");
			        
					xmlBuf.append("<item itm_id=\"").append(itm_id).append("\"")
					.append(" type=\"").append(itm_type).append("\">")
					.append("<itm_code>").append(cwUtils.esc4XML(itm_code)).append("</itm_code>")
			        .append("<itm_title>").append(cwUtils.esc4XML(itm_title)).append("</itm_title>")
			        .append("</item>");
					xmlBuf.append("</training_cell>");
				}
				csd_total_duration = csd_total_duration + rs.getLong("csd_duration");
				count++;
			}
			xmlBuf.append("</item_list>");
			xmlBuf.append("<csd_total_duration>").append(csd_total_duration).append("</csd_total_duration>");
			xmlBuf.append("<s_csd_end_date>")
	        .append("<start>").append(cwUtils.escNull(csd_end_date_start)).append("</start>")
	        .append("<end>").append(cwUtils.escNull(csd_end_date_end)).append("</end>")
			.append("</s_csd_end_date>");
			xmlBuf.append("<items timestamp=\"").append(curTime).append("\"");
	        xmlBuf.append(" page_size=\"").append(page_size).append("\"");
	        xmlBuf.append(" cur_page=\"").append(page).append("\"");
	        xmlBuf.append(" sortorder=\"").append(sortOrder).append("\"");
	        xmlBuf.append(" orderby=\"").append(orderBy).append("\"");
	        xmlBuf.append(" total_search=\"").append(count).append("\"/>").append(cwUtils.NEWL);
		} finally {
			if(stmt!=null) stmt.close();
		}
		return xmlBuf.toString();
	}

	public static String getAppPlanLst(Connection con, long usr_ent_id) throws qdbException, SQLException {
		StringBuffer result_xml = new StringBuffer();
		StringBuffer list_xml = new StringBuffer();
		String sql = "select tpn_id, tpn_date, tpn_name, tcr_parent_tcr_id from tpTrainingPlan, tctrainingcenter" +
				" where tpn_status = ? and tcr_id = tpn_tcr_id and tpn_tcr_id in " +
				"(select tcr_id from tctrainingcenter where tcr_parent_tcr_id in " +
				"(select tco_tcr_id from tcTrainingCenterOfficer where tco_usr_ent_id = ?)) for read only with ur ";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, dbTpTrainingPlan.TPN_STATUS_PENDING);
			stmt.setLong(index++, usr_ent_id);
			ResultSet rs = stmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				if (count < 5) {
					list_xml.append("<app_prove name=\"").append(cwUtils.esc4XML(rs.getString("tpn_name"))).append("\"")
					  .append(" time=\"").append(rs.getTimestamp("tpn_date")).append("\"")
			          .append(" tpn_id=\"").append(rs.getLong("tpn_id")).append("\"")
			          .append(" parent_tcr_id=\"").append(rs.getLong("tcr_parent_tcr_id")).append("\"/>");
				}
				count++;
			}
			result_xml.append("<app_prove_list count=\"").append(count).append("\">");
			result_xml.append(list_xml);
			result_xml.append("</app_prove_list>");
		}finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result_xml.toString();
	}
    
    public String getTpnNameByTpnId(Connection con,long tpn_id) throws SQLException {
		String tpn_name = null;
		String sql = "select tpn_name from tpTrainingplan where tpn_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);	    
		int index = 1;
        try{
    		stmt.setLong(index++, tpn_id);
    		ResultSet rs = stmt.executeQuery();
    		while(rs.next()) {
    			tpn_name = rs.getString("tpn_name");
    		}
        } finally {
        	if (stmt != null) {
                stmt.close();
            }
        }
		return tpn_name;
	}
   public String getUploadPlanPrepXML(Connection con, long tcr_id, long year, Timestamp ypn_year) throws SQLException {
	   StringBuffer xmlBuf = new StringBuffer();
	   
		DbTrainingCenter tc = new DbTrainingCenter();
		tc.tcr_id = tcr_id;
		tc.get(con);
		
		xmlBuf.append("<upload_plan year=\"").append(year).append("\" ypn_year=\"").append(cwUtils.escNull(ypn_year)).append("\">");
		xmlBuf.append("<training_center id=\"").append(tcr_id).append("\">");
		xmlBuf.append("<title>").append(cwUtils.esc4XML(cwUtils.escNull(tc.tcr_title))).append("</title>");
		xmlBuf.append("</training_center >");
		xmlBuf.append("</upload_plan>");
		
		return xmlBuf.toString();
   }
   public static long getMinTraPlanYear(Connection con, WizbiniLoader wizbini) throws SQLException {
		Timestamp cur_time = cwSQL.getTime(con);
		Calendar today = Calendar.getInstance();
		today.setTime(cur_time);
		long cur_year = today.get(Calendar.YEAR);
		long startYear = wizbini.cfgSysSetupadv.getTraPlanYear();
		long minYear = (cur_year < startYear) ? startYear : cur_year;
		return minYear;
	}
}

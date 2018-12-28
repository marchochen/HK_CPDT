package com.cw.wizbank.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class TrainCostStatReport extends ReportTemplate {
	public static NumberFormat nf = NumberFormat.getPercentInstance();
	public static DecimalFormat df = new DecimalFormat("#,###.##");
	
	public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) throws SQLException, qdbException, cwException, IOException, MalformedURLException, cwSysMessage {
		return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, Report.TRAIN_COST_STAT, null);
	 }
	 
	public String[] getReportHelper(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, ViewReportSpec.Data data, long usr_ent_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException {
        		
	   Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

	   String[] ent_id_lst = null;
	   String[] s_usg_ent_id_lst = null;
	   String[] tnd_id_lst = null;
	   long[] itm_id_lst = null;
	   Timestamp start_datetime = null;
	   Timestamp end_datetime = null;
	   Vector spec_values;
	   String lrn_scope_sql = null;
	   
       if(wizbini.cfgTcEnabled) {
    	   boolean all_user_ind = false;
    	   boolean all_cos_ind = false;
    	   boolean answer_for_lrn = false;
    	   boolean answer_for_course_lrn = false;
    	   boolean answer_for_course = false;
    	   boolean answer_for_lrn_course = false;
    	   if(spec_pairs.containsKey("all_user_ind")) {
    		   spec_values = (Vector)spec_pairs.get("all_user_ind");
    		   if(((String)spec_values.get(0)).equals("1")) {
    			   all_user_ind  = true;
    		   }
    	   }
    	   if(spec_pairs.containsKey("answer_for_lrn")) {
    		   spec_values = (Vector)spec_pairs.get("answer_for_lrn");
    		   if(((String)spec_values.get(0)).equals("1")) {
    			   answer_for_lrn  = true;
    		   }
    	   }
    	   if(spec_pairs.containsKey("answer_for_course_lrn")) {
    		   spec_values = (Vector)spec_pairs.get("answer_for_course_lrn");
    		   if(((String)spec_values.get(0)).equals("1")) {
    			   answer_for_course_lrn  = true;
    		   }
    	   }
    	   if(spec_pairs.containsKey("answer_for_course")) {
    		   spec_values = (Vector)spec_pairs.get("answer_for_course");
    		   if(((String)spec_values.get(0)).equals("1")) {
    			   answer_for_course  = true;
    		   }
    	   }
    	   if(spec_pairs.containsKey("answer_for_lrn_course")) {
    		   spec_values = (Vector)spec_pairs.get("answer_for_lrn_course");
    		   if(((String)spec_values.get(0)).equals("1")) {
    			   answer_for_lrn_course  = true;
    		   }
    	   }
    	   if(all_user_ind) {
//    		   Vector vec = new Vector();
    		   if((answer_for_lrn && answer_for_course_lrn )||(!answer_for_lrn && !answer_for_course_lrn )) {
//    			   vec = ViewLearnerReport.getAllLrn(con, prof.usr_ent_id, prof.root_ent_id);
    			   lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
    		   } else if (answer_for_lrn && !answer_for_course_lrn) {
//    			   vec = ViewLearnerReport.getMyRspLrn(con, prof.usr_ent_id, prof.root_ent_id);
    			   lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
    		   } else if (!answer_for_lrn && answer_for_course_lrn) {
//    			   vec = ViewLearnerReport.getMyRspCosEnrollLrn(con, prof.usr_ent_id, prof.root_ent_id);
    			   lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
    		   } 
/*    		   if(vec.size() == 0) {
    			   ent_id_lst = new String[1];
    			   ent_id_lst[0] = "0";
    		   } else {
        		   ent_id_lst = new String[vec.size()];
        		   for (int i=0; i<vec.size(); i++) {
        			   Long ent_id = (Long)vec.elementAt(i);
        			   ent_id_lst[i] = ent_id.longValue() + "";
        		   }        			   
    		   }*/
    	   }
    	   if(!spec_pairs.containsKey("tnd_id") && !spec_pairs.containsKey("itm_id")) {
    		   all_cos_ind = true;
    	   }
    	   if(all_cos_ind) {
    		   Vector vec = new Vector();
    		   if((answer_for_course && answer_for_lrn_course )||(!answer_for_course && !answer_for_lrn_course )) {
    			   vec = ViewLearnerReport.getAllCos(con, prof.usr_ent_id, prof.root_ent_id);
    		   } else if (answer_for_course && !answer_for_lrn_course) {
    			   vec = ViewLearnerReport.getMyRspCos(con, prof.usr_ent_id, prof.root_ent_id);
    		   } else if (!answer_for_course && answer_for_lrn_course) {
    			   vec = ViewLearnerReport.getMyRspLrnEnrollCos(con, prof.usr_ent_id, prof.root_ent_id);
    		   }
    		   if(vec.size() == 0) {
    			   itm_id_lst = new long[1];
    			   itm_id_lst[0] = 0;
    		   } else {
    			   itm_id_lst = new long[vec.size()];
    			   for(int i=0; i<vec.size(); i++) {
    				   itm_id_lst[i] = ((Long)vec.elementAt(i)).longValue();
    			   }
    		   }
    	   }
       }
       
       if (spec_pairs.containsKey("usr_ent_id")||spec_pairs.containsKey("usg_ent_id")) {
    	   if((spec_values=(Vector)spec_pairs.get("usr_ent_id"))==null){
    		   spec_values = (Vector)spec_pairs.get("usg_ent_id");
   		   }

   		   ent_id_lst = new String[spec_values.size()];

   		   for (int i=0; i<spec_values.size(); i++) {
   			   ent_id_lst[i] = (String)spec_values.elementAt(i);
   		   }
   	   }

   	   if (spec_pairs.containsKey("s_usg_ent_id_lst")) {
   		   spec_values = (Vector)spec_pairs.get("s_usg_ent_id_lst");

   		   s_usg_ent_id_lst  = new String[spec_values.size()];
   		   Supervisor sup = new Supervisor(con, prof.usr_ent_id);
   		   Vector  v_ent_id  =  new  Vector();
           
   		   for (int i=0; i<spec_values.size(); i++) {
   			   s_usg_ent_id_lst[i] = (String)spec_values.elementAt(i);
   			   if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_STAFF)) {
   				   Vector vStaff = sup.getStaffEntIdVector(con);
   				   for (int k=0; k<vStaff.size(); k++){
   					   v_ent_id.addElement(vStaff.elementAt(k)); 
   				   }
   			   } else if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_DIRECT_STAFF)) {
   				   Vector vStaff = sup.getDirectStaffEntIdVector(con);
   				   for (int k=0; k<vStaff.size(); k++){
   					   v_ent_id.addElement(vStaff.elementAt(k)); 
   				   }
   			   }else{
   				   v_ent_id.addElement(Long.valueOf(s_usg_ent_id_lst[i]));
   			   }
   		   }
   		   ent_id_lst = new String[v_ent_id.size()];
   		   for(int i=0; i<v_ent_id.size(); i++) {
   			   ent_id_lst[i] = (v_ent_id.elementAt(i).toString());
   		   }
   	   }

   	   if (spec_pairs.containsKey("tnd_id")) {
   		   spec_values = (Vector)spec_pairs.get("tnd_id");

   		   tnd_id_lst = new String[spec_values.size()];

   		   for (int i=0; i<spec_values.size(); i++) {
   			   tnd_id_lst[i] = (String)spec_values.elementAt(i);
   		   }
   	   }
   	   
   	   if (spec_pairs.containsKey("itm_id")) {
		   spec_values = (Vector)spec_pairs.get("itm_id");
		   itm_id_lst = new long[spec_values.size()];
		   for(int i=0; i<spec_values.size(); i++) {
			   itm_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
		   }
	   }
	   
	   if (spec_pairs.containsKey("start_datetime")) {
		   spec_values = (Vector)spec_pairs.get("start_datetime");
		   start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
	   }

	   if (spec_pairs.containsKey("end_datetime")) {
		   spec_values = (Vector)spec_pairs.get("end_datetime");
		   end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
	   }

	   String[] reportXML=null;
	   reportXML = new String[1];
	   reportXML[0] = getReport(con, prof, ent_id_lst, itm_id_lst, tnd_id_lst, start_datetime, end_datetime, (Vector)spec_pairs.get("content_lst"), lrn_scope_sql);
	   return reportXML;
   } 
	 
    public String[] getReportView(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rte_id, String[] spec_name, String[] spec_value, long usr_ent_id, String rsp_title, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;

        String[] reportXML = getReportHelper(con, sess, page, prof, env, data, usr_ent_id, split_ind, override_appr_usg, wizbini); 

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, null, reportXML[i], data.rsp_xml, Report.TRAIN_COST_STAT, rsp_title);
        }
        return reportXML;
    }
    
    public String[] getReportView(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rsp_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);

        String[] reportXML = getReportHelper(con, sess, page, prof, env, data, 0, split_ind, override_appr_usg, wizbini);

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, null, reportXML[i], null, Report.TRAIN_COST_STAT, null);
        }
        return reportXML;
    }
	
	public String getReport(
		Connection con,
		loginProfile prof, 
		String[] ent_id_lst,
		long[] itm_id_lst,
		String[] tnd_id_lst, 
		Timestamp start_datetime,
		Timestamp end_datetime,
		Vector content_vec, String lrn_scope_sql)
		throws SQLException, qdbException, cwSysMessage, cwException, IOException {
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		
		StringBuffer result = new StringBuffer();
		result.append("<report_list>").append(cwUtils.NEWL);
		
		String entIdTableName = null;
        String entIdColName = "usr_ent_ids";
        String itmTableName = null;
        String itmIdColName = "tmp_itm_id";
		try {
			Vector entIds = new Vector();
			List itmIds = new ArrayList();
			setIdsToList(con, entIds, itmIds, ent_id_lst, itm_id_lst, tnd_id_lst, prof);
			
			if (entIds.size() > 0) {
				entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);
			}
			
			if (itmIds.size() > 0) {
				itmTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
			}

			if(entIdTableName != null) {
				cwSQL.insertSimpleTempTable(con, entIdTableName, entIds, cwSQL.COL_TYPE_LONG);
			}
			if(entIdTableName == null && lrn_scope_sql != null ) {
			    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0, lrn_scope_sql);
			}
			
			if(itmTableName != null){
				cwSQL.insertSimpleTempTable(con, itmTableName, itmIds, cwSQL.COL_TYPE_LONG);
			}
			
			stmt  = getTrainCostStatStmt(con, entIdTableName, entIdColName, itmTableName, itmIdColName, start_datetime, end_datetime, false);
			if(null != itmTableName){
				rs = stmt.executeQuery();
			}
			
			int usrCnt = 0;
			double actual = 0;
			double budget = 0;
			double totalCost = 0;
			int totalRecord = 0;
			while (null != rs && rs.next()) {
				totalRecord++;
				usrCnt = rs.getInt("app_cnt");
				actual = rs.getDouble("actual");
				budget = rs.getDouble("budget");
				if (usrCnt > 0) {
					//计算总培训费用
					totalCost += actual;
				}
				if (totalRecord > ReportTemplate.PAGE_SIZE_VIEW_RECORD) {
					continue;
				}
				result.append("<record>").append(cwUtils.NEWL)
			          .append("<budget>").append(df.format(budget)).append("</budget>")
			          .append("<actual>").append(df.format(actual)).append("</actual>")
					  .append("<app_cnt>").append(usrCnt).append("</app_cnt>");
				if (rs.getString("itm_type").equals(aeItem.ITM_TYPE_SELFSTUDY) || rs.getString("itm_type").equals(aeItem.ITM_TYPE_VIDEO)) {
					result.append("<p_itm_title>").append(cwUtils.esc4XML(rs.getString("c_itm_title"))).append("</p_itm_title>")
		                  .append("<p_itm_code>").append(cwUtils.esc4XML(rs.getString("c_itm_code"))).append("</p_itm_code>");
				} else {
					result.append("<p_itm_title>").append(cwUtils.esc4XML(rs.getString("p_itm_title"))).append("</p_itm_title>")
			              .append("<p_itm_code>").append(cwUtils.esc4XML(rs.getString("p_itm_code"))).append("</p_itm_code>")
			              .append("<c_itm_title>").append(cwUtils.esc4XML(rs.getString("c_itm_title"))).append("</c_itm_title>")
			              .append("<c_itm_code>").append(cwUtils.esc4XML(rs.getString("c_itm_code"))).append("</c_itm_code>");
				}
				if (budget > 0) {
					result.append("<exce_rate>").append(nf.format(actual / budget)).append("</exce_rate>");
				}
				if (usrCnt > 0) {
					result.append("<per_cost>").append(df.format(actual / usrCnt)).append("</per_cost>");
				}
				result.append("</record>");
			}
			result.append("<report_summary totalRecord=\"").append(totalRecord).append("\">")
				  .append("<total_cost>").append(df.format(totalCost)).append("</total_cost>")
				  .append("</report_summary>");
		} finally {
			if(itmTableName != null){
				cwSQL.dropTempTable(con, itmTableName);
	        }
	        if(entIdTableName != null) {
	        	cwSQL.dropTempTable(con, entIdTableName);	
	        }
			cwSQL.cleanUp(rs, stmt);
		}
		
		result.append("</report_list>");
		return result.toString();
	}
	
	public static PreparedStatement getTrainCostStatStmt(Connection con, String entIdTableName, String entIdColName, String itmTableName, String itmIdColName, 
					Timestamp start_datetime, Timestamp end_datetime, boolean is_count) throws SQLException {
		String sql = getTrainCostStatSql(aeItem.getItemCosType(), entIdTableName, entIdColName, itmTableName, itmIdColName, 
										start_datetime, end_datetime, is_count);
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		if (!is_count) {
			stmt.setString(index++, aeApplication.ADMITTED);
		}
		stmt.setBoolean(index++, false);
		if (start_datetime != null) {
			stmt.setTimestamp(index++, start_datetime);
		}
		if (end_datetime != null) {
			stmt.setTimestamp(index++, end_datetime);
		}
		if (start_datetime != null) {
			stmt.setTimestamp(index++, start_datetime);
		}
		if (end_datetime != null) {
			stmt.setTimestamp(index++, end_datetime);
		}
		return stmt;
	}
	
	private static String getTrainCostStatSql(String[] itm_type, String entIdTableName, String entIdColName, String itmTableName, 
											String itmIdColName, Timestamp start_datetime, Timestamp end_datetime, boolean is_count) {
		StringBuffer sql = new StringBuffer(); 
		sql.append(" SELECT ");
		if (is_count) {
			sql.append(" COUNT(DISTINCT cItm.itm_id) ");
		} else {
			sql.append(" pItm.itm_code p_itm_code, pItm.itm_title p_itm_title, cItm.itm_type itm_type, cItm.itm_code c_itm_code, cItm.itm_title c_itm_title, ")
			   .append(" sum(ito_budget) budget, sum(ito_actual) actual, app.app_cnt ");
		}
		sql.append(" FROM aeItem cItm ")
		   .append(" LEFT JOIN aeItemCost ON (ito_itm_id = cItm.itm_id) ");
		
		if (!is_count) {
			sql.append(" LEFT JOIN aeItemRelation ON (ire_child_itm_id = cItm.itm_id) ")
			   .append(" LEFT JOIN aeItem pItm ON (ire_parent_itm_id = pItm.itm_id) ")
			   .append(" LEFT JOIN ( ")
			   .append("              SELECT app_itm_id, count(app_id) app_cnt FROM aeApplication  ")
			   .append("              WHERE app_status = ? ")
			   .append(" 			  AND EXISTS (SELECT ").append(entIdColName).append(" FROM ")
			   .append(entIdTableName).append(" WHERE ").append(entIdColName).append(" = app_ent_id) ")
			   .append("              GROUP BY app_itm_id ")
			   .append(" ) app ON (app.app_itm_id = cItm.itm_id) ");
		}
		sql.append(" WHERE cItm.itm_create_run_ind = ? ");
		if (itm_type != null && itm_type.length > 0) {
			sql.append(" AND ( ");
			for (int i = 0; i < itm_type.length; i++) {
				if (i > 0) {
					sql.append(" OR ");
				}
				sql.append(" cItm.itm_type = '").append(itm_type[i]).append("'");
			}
			sql.append(" ) ");
		}
		sql.append(OuterJoinSqlStatements.getReportTrainEndDateSql(start_datetime, end_datetime, false, "cItm"))
		   .append(" AND EXISTS (SELECT ").append(itmIdColName).append(" FROM ")
		   .append(itmTableName).append(" WHERE ").append(itmIdColName).append(" = cItm.itm_id) ");
		if (!is_count) {
			sql.append(" GROUP BY pItm.itm_code, pItm.itm_title, cItm.itm_type, cItm.itm_code, cItm.itm_title, app.app_cnt ");
		}
		return sql.toString();
	}

	public static void setIdsToList(Connection con, List entIds, List itmIds, String[] ent_id_lst, long[] itm_id_lst, String[] tnd_id_lst, 
				loginProfile prof) throws NumberFormatException, SQLException {
		int i, k;
        if (itm_id_lst != null) {
			aeItem itm = new aeItem();
			Vector childItm;
			for (i = 0; i < itm_id_lst.length; i++) {
				itm.itm_id = itm_id_lst[i];
				if (itm.getCreateRunInd(con)) {
					childItm = aeItemRelation.getChildItemId(con, itm_id_lst[i]);
					for (k = 0; k < childItm.size(); k++) {
						itmIds.add((Long) childItm.get(k));
					}
				} else {
					itmIds.add(new Long(itm_id_lst[i]));
				}
			}
		} else if (tnd_id_lst != null) {
			aeTreeNode.getItemsFromNode(con, tnd_id_lst, itmIds);
		}
        
        if (ent_id_lst != null && ent_id_lst.length > 0) {
            List in_ent_ids = cwUtils.string2LongArrayList(ent_id_lst);
            Vector ent_id_vec = new Vector();
            entIds.addAll(LearnerRptExporter.getUserEntId(con, ent_id_vec, in_ent_ids));
            cwUtils.removeDuplicate(entIds);
        }
	}
}

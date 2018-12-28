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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class TrainFeeStatReport extends ReportTemplate {
	public static final String TRAIN_SCOPE_PLAN = "PLAN";
	public static final String TRAIN_SCOPE_UNPLAN = "UNPLAN";
	public static final String TRAIN_SCOPE_ALL = "ALL";
	public static final String DATA_HASH = "DATA_HASH";
	public static final String SUMMARY_HASH = "SUMMARY_HASH";
	public static final String TOTAL_HASH = "TOTAL_HASH";
	public static final String ORDER_LST = "ORDER_LST";
	
	public static NumberFormat nf = NumberFormat.getPercentInstance();
	public static DecimalFormat df = new DecimalFormat("#,###.##");
	
	public class ItmData {
		long itm_id;
		String p_itm_code;
		String p_itm_title;
		String c_itm_code;
		String c_itm_title;
		String itm_dummy_type;
		double total_budget;
		double total_actual;
		List cosLst = new ArrayList();
	}
	
	public class CostData {
		String ito_type;
		double ito_budget;
		double ito_actual;
	}
	
	public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) throws SQLException, qdbException, cwException, IOException, MalformedURLException, cwSysMessage {
		return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, Report.TRAIN_FEE_STAT, null);
	 }
	 
	public String[] getReportHelper(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, ViewReportSpec.Data data, long usr_ent_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException {
        		
	   Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

	   String[] dummy_type_lst = null;
	   Timestamp start_datetime = null;
	   Timestamp end_datetime = null;
	   long tcr_id = 0;
	   String train_scope = null;
	   Vector spec_values; 
	   
	   if (spec_pairs.containsKey("tcr_id")) {
		   spec_values = (Vector)spec_pairs.get("tcr_id");
		   tcr_id = Long.valueOf((String)spec_values.elementAt(0)).longValue();
	   }
	   
	   if (spec_pairs.containsKey("start_datetime")) {
		   spec_values = (Vector)spec_pairs.get("start_datetime");
		   start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
	   }

	   if (spec_pairs.containsKey("end_datetime")) {
		   spec_values = (Vector)spec_pairs.get("end_datetime");
		   end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
	   }
	   
	   if (spec_pairs.containsKey("itm_type")) {
		   spec_values = (Vector)spec_pairs.get("itm_type");
		   dummy_type_lst = new String[spec_values.size()];
		   for(int i = 0; i < spec_values.size(); i++) {
			   dummy_type_lst[i] = (String)spec_values.elementAt(i);
		   }
	   }
	   
	   if (spec_pairs.containsKey("train_scope")) {
		   spec_values = (Vector)spec_pairs.get("train_scope");
		   train_scope = (String)spec_values.elementAt(0);
	   }

	   Vector v=null;
	   if((v=(Vector)spec_pairs.get("sort_col"))!=null){
		   String col=(String)v.elementAt(0);
		   if(col.equals("itm_code")||col.equals("itm_title")){
			   col="t"+col.substring(col.indexOf("_"));
		   }
		   page.sortCol=col;
		   v=null;
	   }
	   if((v=(Vector)spec_pairs.get("sort_order"))!=null){
		   page.sortOrder=(String)v.elementAt(0);
		   v=null;
	   }

	   String[] reportXML=null;
	   reportXML = new String[1];
	   reportXML[0] = getReport(con, page, tcr_id, start_datetime, end_datetime, dummy_type_lst, train_scope);
	   return reportXML;
   } 
	 
    public String[] getReportView(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rte_id, String[] spec_name, String[] spec_value, long usr_ent_id, String rsp_title, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;

        aeItem item = new aeItem();
        String xml_prefix = item.getItemCostAttribute(wizbini,prof.root_id);
        
        TrainFeeStatExporter export = new TrainFeeStatExporter(con, null);
        xml_prefix = export.getItmCostAsXml(prof.cur_lan, xml_prefix);
        
        String[] reportXML = getReportHelper(con, sess, page, prof, env, data, usr_ent_id, split_ind, override_appr_usg, wizbini); 

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, xml_prefix, reportXML[i], data.rsp_xml, Report.TRAIN_FEE_STAT, rsp_title);
        }
        return reportXML;
    }
    
    public String[] getReportView(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rsp_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
        
        aeItem item = new aeItem();
        String xml_prefix = item.getItemCostAttribute(wizbini,prof.root_id);
        
        TrainFeeStatExporter export = new TrainFeeStatExporter(con, null);
        xml_prefix = export.getItmCostAsXml(prof.cur_lan, xml_prefix);
        
        String[] reportXML = getReportHelper(con, sess, page, prof, env, data, 0, split_ind, override_appr_usg, wizbini);

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, xml_prefix, reportXML[i], null, Report.TRAIN_FEE_STAT, null);
        }
        return reportXML;
    }
	
	public String getReport(
		Connection con,
		cwPagination page,
		long tcr_id,
		Timestamp start_datetime,
		Timestamp end_datetime,
		String[] dummy_type_lst,
		String train_scope)
		throws SQLException, qdbException, cwSysMessage, cwException, IOException {
		
		Map reportHash = getDataHash(con, page.sortCol, page.sortOrder, tcr_id, start_datetime, end_datetime, dummy_type_lst, train_scope);
		Map dataHash = (Map)reportHash.get(DATA_HASH);
		Map summaryHash = (Map)reportHash.get(SUMMARY_HASH);
		Map totalHash = (Map)reportHash.get(TOTAL_HASH);
		List orderLst = (List)reportHash.get(ORDER_LST);

		StringBuffer result = new StringBuffer();
		result.append("<report_list>").append(cwUtils.NEWL);
		ItmData itmData = null;
		CostData costData = null;
		CostData summaryCos = null;
		int i, j, s;
		Long itmId = null;
		Set set = null;
		Iterator iter = null;
		Map.Entry entry = null;
		//预览只显示10条数据
		for (i = 0; i < orderLst.size() && i < ReportTemplate.PAGE_SIZE_VIEW_RECORD; i++) {
			itmId = (Long)orderLst.get(i);
			itmData = (ItmData)dataHash.get(itmId);
			
			//培训费用明细
			result.append("<report_group>").append(cwUtils.NEWL)
			      .append("<p_itm_title>").append(cwUtils.esc4XML(itmData.p_itm_title)).append("</p_itm_title>")
			      .append("<p_itm_code>").append(cwUtils.esc4XML(itmData.p_itm_code)).append("</p_itm_code>")
			      .append("<c_itm_title>").append(cwUtils.esc4XML(cwUtils.escNull(itmData.c_itm_title))).append("</c_itm_title>")
			      .append("<c_itm_code>").append(cwUtils.esc4XML(cwUtils.escNull(itmData.c_itm_code))).append("</c_itm_code>")
			      .append("<itm_dummy_type>").append(cwUtils.esc4XML(itmData.itm_dummy_type)).append("</itm_dummy_type>");
			result.append("<cost>")//每个培训的总的费用
				  .append("<total_cos budget=\"").append(df.format(itmData.total_budget))
				  .append("\" actual=\"").append(df.format(itmData.total_actual))
				  .append("\" exce_rate=\"");
			if (itmData.total_budget > 0) {
				result.append(nf.format(itmData.total_actual / itmData.total_budget));
			} else {
				result.append("0%");
			}
			result.append("\"/>");
			
			s = itmData.cosLst.size();
			//各种费用
			for (j = 0; j < s; j++) {
				costData = (CostData)itmData.cosLst.get(j);
				result.append("<cos_data type=\"").append(costData.ito_type)
					  .append("\" budget=\"").append(df.format(costData.ito_budget))
					  .append("\" actual=\"").append(df.format(costData.ito_actual))
					  .append("\" exce_rate=\"");
				if (costData.ito_budget > 0) {
					result.append(nf.format(costData.ito_actual / costData.ito_budget));
				} else {
					result.append("0%");
				}
				result.append("\"/>");
			}
			result.append("</cost>")
		          .append("</report_group>");
		}
		
		//所有培训的总费用
		result.append("<report_summary>").append(cwUtils.NEWL);
		set = summaryHash.entrySet();
		iter = set.iterator();
		while(iter.hasNext()) {
			entry = (Map.Entry)iter.next();
			summaryCos = (CostData)entry.getValue();
			result.append("<cost type=\"").append(summaryCos.ito_type)
			      .append("\" budget=\"").append(df.format(summaryCos.ito_budget))
			      .append("\" actual=\"").append(df.format(summaryCos.ito_actual))
			      .append("\" exce_rate=\"");
			if (summaryCos.ito_budget > 0) {
				result.append(nf.format(summaryCos.ito_actual / summaryCos.ito_budget));
			} else {
				result.append("0%");
			}
			result.append("\"/>");
		}
		
		double totalBudget = ((Double)totalHash.get("totalBudget")).doubleValue();
		double totalActual = ((Double)totalHash.get("totalActual")).doubleValue();
		
		result.append("<total_cost budget=\"").append(df.format(totalBudget))
			  .append("\" actual=\"").append(df.format(totalActual))
			  .append("\" exce_rate=\"");
		if (totalBudget > 0) {
			result.append(nf.format(totalActual / totalBudget));
		} else {
			result.append("0%");
		}
		result.append("\"/>");
		result.append("<cosCnt>").append(dataHash.size()).append("</cosCnt>");
		result.append("</report_summary>").append(cwUtils.NEWL);

		result.append("</report_list>");
		return result.toString();
	}
	
	/**
     * @param tcr_id 培训中心id
     * @param train_scope 培训范围
     * @param dummy_type_lst 培训类型
     */
	public Map getDataHash(Connection con, String sortCol, String sortOrder, long tcr_id, Timestamp start_datetime, Timestamp end_datetime, 
				String[] dummy_type_lst, String train_scope) throws SQLException {
		Map reportData = new HashMap();
		Map dataHash = new HashMap();
		Map summaryHash = new HashMap();
		Map totalHash = new HashMap();
		List orderLst = new ArrayList();
		reportData.put(DATA_HASH, dataHash);
		reportData.put(SUMMARY_HASH, summaryHash);
		reportData.put(TOTAL_HASH, totalHash);
		reportData.put(ORDER_LST, orderLst);
		if (sortOrder == null) {
			sortOrder = ExamPaperStatReport.DEFAULT_SORT_ORDER;
		}
		
		String sqlStr = getSqlByScope(train_scope, start_datetime, end_datetime, dummy_type_lst) + " ORDER BY ";
		if (sortCol.equals("itm_type")) {
			sqlStr += "cItm.itm_type " + sortOrder + ", cItm.itm_blend_ind " + sortOrder + ", cItm.itm_exam_ind " + sortOrder + ", cItm.itm_ref_ind " + sortOrder;
		} else {
			sqlStr += sortCol + " " + sortOrder + ", c_itm_title " + sortOrder;
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String itoType = null;
		CostData summaryCos = null;
		try {
			stmt = con.prepareStatement(sqlStr.toString());
			int index = 1;
			index = setStmt(stmt, index++, tcr_id, start_datetime, end_datetime);
			if (TRAIN_SCOPE_ALL.endsWith(train_scope)) {
				setStmt(stmt, index++, tcr_id, start_datetime, end_datetime);
			}
			rs = stmt.executeQuery();
			long itm_id;
			Long itmIdObj = null;
			ItmData itmData = null;
			CostData itmCost = null;
			double totalBudget = 0;
			double totalActual = 0;
			String type = null;
			while (rs.next()) {
				itm_id = rs.getLong("itm_id");
				itmIdObj = new Long(itm_id);
				type = rs.getString("itm_type");
				if (dataHash.containsKey(itmIdObj)) {
					itmData = (ItmData)dataHash.get(itmIdObj);
				} else {
					orderLst.add(itmIdObj);
					itmData = new ItmData();
					dataHash.put(itmIdObj, itmData);
					itmData.itm_id = itm_id;
					if (aeItem.ITM_TYPE_SELFSTUDY.equals(type) || aeItem.ITM_TYPE_VIDEO.equals(type)|| aeItem.ITM_TYPE_MOBILE.equals(type)) {
						itmData.p_itm_code = rs.getString("c_itm_code");
						itmData.p_itm_title = rs.getString("c_itm_title");
					} else {
						itmData.p_itm_code = rs.getString("p_itm_code");
						itmData.p_itm_title = rs.getString("t_title");
						itmData.c_itm_code = rs.getString("c_itm_code");
						itmData.c_itm_title = rs.getString("c_itm_title");
					}
					
					itmData.itm_dummy_type = aeItemDummyType.getDummyItemType(type, rs.getBoolean("itm_blend_ind"), 
																rs.getBoolean("itm_exam_ind"), rs.getBoolean("itm_ref_ind"));
				}
				itoType = rs.getString("ito_type");
				if (itoType == null) {
					continue;
				}
				
				itmCost = new CostData();
				itmData.cosLst.add(itmCost);
				itmCost.ito_type = itoType;
				itmCost.ito_budget = rs.getDouble("ito_budget");
				itmCost.ito_actual = rs.getDouble("ito_actual");
				itmData.total_budget += itmCost.ito_budget;
				itmData.total_actual += itmCost.ito_actual;
				
				//生成统计所有课程的数据
				if (summaryHash.containsKey(itoType)) {
					summaryCos = (CostData)summaryHash.get(itoType);
				} else {
					summaryCos = new CostData();
					summaryHash.put(itoType, summaryCos);
					summaryCos.ito_type = itmCost.ito_type;
				}
				summaryCos.ito_budget += itmCost.ito_budget;
				summaryCos.ito_actual += itmCost.ito_actual;
				totalBudget += itmCost.ito_budget;
				totalActual += itmCost.ito_actual;
			}
			totalHash.put("totalBudget", new Double(totalBudget));
			totalHash.put("totalActual", new Double(totalActual));
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		
		return reportData;
	}
	
	private String getSqlByScope(String train_scope, Timestamp start_datetime, Timestamp end_datetime, String[] dummy_type_lst) {
		String sql = null;
		if (TRAIN_SCOPE_ALL.endsWith(train_scope)) {
			sql = OuterJoinSqlStatements.getTrainFeeStatReport(TRAIN_SCOPE_PLAN, start_datetime, end_datetime, dummy_type_lst) 
			    + " UNION "
			    + OuterJoinSqlStatements.getTrainFeeStatReport(TRAIN_SCOPE_UNPLAN, start_datetime, end_datetime, dummy_type_lst);
		} else {
			sql = OuterJoinSqlStatements.getTrainFeeStatReport(train_scope, start_datetime, end_datetime, dummy_type_lst);
		}
		return sql;
	}
	
	private int setStmt(PreparedStatement stmt, int index, long tcr_id, Timestamp start_datetime, Timestamp end_datetime) throws SQLException {
		stmt.setLong(index++, tcr_id);
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
		return index;
	}
}

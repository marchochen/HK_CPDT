package com.cw.wizbank.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewFmFacility;
import com.cw.wizbank.db.view.ViewFmFacilitySchedule;
import com.cw.wizbank.fm.FMReservationManager;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class FMFeeReport extends ReportTemplate {
	public static DecimalFormat df = new DecimalFormat("#,###.##");
	
	public class ClassInfo {
		String rsv_purpose;
		String usr_display_bil;
		int rsv_participant_no;
		List resLst = new ArrayList();
	}
	
	public class ReservationInfo {
		Timestamp fsh_date;
		double total_fee;
		List facilityLst = new ArrayList();
	}
	
	public class FacilityInfo {
		Timestamp fsh_start_time;
		Timestamp fsh_end_time;
		String fac_title;
		double fac_fee;
	}
	
	public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) throws SQLException, qdbException, cwException, IOException, MalformedURLException, cwSysMessage {
		return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, Report.FM_FEE, null);
	}
	 
	public String[] getReportHelper(Connection con, loginProfile prof, ViewReportSpec.Data data, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException {
        		
	   Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

	   String[] tnd_id_lst = null;
	   long[] itm_id_lst = null;
	   long tcr_id = 0;
	   Timestamp start_datetime = null;
	   Timestamp end_datetime = null;
	   int fac_type = 0;
	   Vector spec_values; 
	   
       if(wizbini.cfgTcEnabled) {
    	   boolean all_cos_ind = false;
    	   boolean answer_for_course = false;
    	   boolean answer_for_lrn_course = false;
 
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
       
       if (spec_pairs.containsKey("tcr_id")) {
		   spec_values = (Vector)spec_pairs.get("tcr_id");
		   tcr_id = Long.valueOf((String)spec_values.elementAt(0)).longValue();
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
	   
	   if (spec_pairs.containsKey("fac_type")) {
		   spec_values = (Vector)spec_pairs.get("fac_type");
		   fac_type = Integer.valueOf(((String)spec_values.elementAt(0))).intValue();
	   }

	   String[] reportXML=null;
	   reportXML = new String[1];
	   reportXML[0] = getReport(con, prof, tcr_id, itm_id_lst, tnd_id_lst, start_datetime, end_datetime, fac_type);
	   return reportXML;
   } 
	 
    public String[] getReportView(Connection con, HttpServletRequest request, loginProfile prof, String[] spec_name, String[] spec_value, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(0, null, spec_name, spec_value);

        data.rte_id = 0;
        data.rsp_xml = rpt_spec.rsp_xml;

        String[] reportXML = getReportHelper(con, prof, data, wizbini); 

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, 0, null, reportXML[i], data.rsp_xml, Report.FM_FEE, null);
        }
        return reportXML;
    }
	
	public String getReport(
		Connection con,
		loginProfile prof, 
		long tcr_id,
		long[] itm_id_lst,
		String[] tnd_id_lst, 
		Timestamp start_datetime,
		Timestamp end_datetime,
		int fac_type)
		throws SQLException, qdbException, cwSysMessage, cwException, IOException {
		
		StringBuffer result = new StringBuffer();

		Map dataHash  = getFMFeeDataHash(con, prof, itm_id_lst, tnd_id_lst, prof.root_ent_id, tcr_id, start_datetime, end_datetime, fac_type);
		List dataLst = (List)dataHash.get("dataLst");
		int totalRec = ((Integer)dataHash.get("totalRec")).intValue();
		result.append("<report_list totalRec=\"").append(totalRec).append("\">").append(cwUtils.NEWL);
		
		ClassInfo classInfo = null;
		ReservationInfo resInfo = null;
		FacilityInfo facInfo = null;
		int i, j, k, dataSize, resSize, facSize;
		dataSize = dataLst.size();
		for (i = 0; i < dataSize; i++) {
			classInfo = (ClassInfo)dataLst.get(i);
			result.append("<report_group>")
			      .append("<rsv_purpose>").append(cwUtils.esc4XML(classInfo.rsv_purpose)).append("</rsv_purpose>")
			      .append("<rsv_participant_no>").append(classInfo.rsv_participant_no).append("</rsv_participant_no>")
			      .append("<usr_display_bil>").append(cwUtils.esc4XML(classInfo.usr_display_bil)).append("</usr_display_bil>");
			resSize = classInfo.resLst.size();
			for (j = 0; j < resSize; j++) {
				resInfo = (ReservationInfo)classInfo.resLst.get(j);
				result.append("<reservation>")
				      .append("<total_fee>").append(df.format(resInfo.total_fee)).append("</total_fee>")
				      .append("<fsh_date>").append(resInfo.fsh_date).append("</fsh_date>")
				      .append("<fac_lst>");
				facSize = resInfo.facilityLst.size();
				for (k = 0; k < facSize; k++) {
					facInfo = (FacilityInfo)resInfo.facilityLst.get(k);
					result.append("<facility>")
					      .append("<fsh_start_time>").append(facInfo.fsh_start_time).append("</fsh_start_time>")
					      .append("<fsh_end_time>").append(facInfo.fsh_end_time).append("</fsh_end_time>")
					      .append("<fac_title>").append(cwUtils.esc4XML(facInfo.fac_title)).append("</fac_title>")
					      .append("<fac_fee>").append(df.format(facInfo.fac_fee)).append("</fac_fee>")
					      .append("</facility>");
				}
				result.append("</fac_lst>").append("</reservation>");
			}
			result.append("</report_group>");
		}
		result.append("</report_list>");
		return result.toString();
	}

	public Map getFMFeeDataHash(Connection con, loginProfile prof, long[] itm_id_lst, String[] tnd_id_lst, long root_ent_id, long tcr_id, 
										Timestamp start_datetime, Timestamp end_datetime, int fac_type) throws SQLException {
		String itmTableName = null;
        String itmIdColName = "tmp_itm_id";
        PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			List itmIds = getItmIdLst(con, itm_id_lst, tnd_id_lst, prof);
			
			if (itmIds.size() > 0) {
				itmTableName = cwSQL.createSimpleTemptable(con, itmIdColName, cwSQL.COL_TYPE_LONG, 0);
			}
			
			if(itmTableName != null){
				cwSQL.insertSimpleTempTable(con, itmTableName, itmIds, cwSQL.COL_TYPE_LONG);
				stmt = getRptStmt(con, itmTableName, itmIdColName, root_ent_id, tcr_id, start_datetime, end_datetime, fac_type, false);
				rs = stmt.executeQuery();
			}
			
			Map dateHash = handleTransData(rs);
			return dateHash;
		} finally {
			if(itmTableName != null){
				cwSQL.dropTempTable(con, itmTableName);
	        }
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	private Map handleTransData(ResultSet rs) throws SQLException {
		List dataLst = new ArrayList();
		Map dateHash = new HashMap();
		dateHash.put("dataLst", dataLst);
		long new_rsv_id = 0;
		long rsv_id = 0;
		Timestamp new_fsh_date = null;
		Timestamp fsh_date = null;
		
		int totalRec = 0;
		ClassInfo classInfo = null;
		ReservationInfo resInfo = null;
		FacilityInfo facInfo = null;
		
		boolean newClass = false;
		boolean newRes = false;
		while (rs != null && rs.next()) {
			new_rsv_id = rs.getLong("rsv_id");
			new_fsh_date = rs.getTimestamp("fsh_date");
			newClass = false;
			newRes = false;
			if (rsv_id != new_rsv_id || !new_fsh_date.equals(fsh_date)) {
				//统计总记录数
				totalRec++;
				fsh_date = new_fsh_date;
				newRes = true;
				
				if (rsv_id != new_rsv_id) {
					rsv_id = new_rsv_id;
					newClass = true;
				}
			}
			//预览只显示10个
			if (totalRec <= ReportTemplate.PAGE_SIZE_VIEW_RECORD) {
				if (newClass) {
					classInfo = new ClassInfo();
					dataLst.add(classInfo);
					classInfo.rsv_purpose = rs.getString("rsv_purpose");
					classInfo.rsv_participant_no = rs.getInt("rsv_participant_no");
					classInfo.usr_display_bil = rs.getString("usr_display_bil");
				}
				
				if (newRes) {
					resInfo = new ReservationInfo();
					classInfo.resLst.add(resInfo);
					resInfo.fsh_date = fsh_date;
				}
				
				facInfo = new FacilityInfo();
				resInfo.facilityLst.add(facInfo);
				facInfo.fsh_start_time = rs.getTimestamp("fsh_start_time");
				facInfo.fsh_end_time = rs.getTimestamp("fsh_end_time");
				facInfo.fac_title = rs.getString("fac_title");
				facInfo.fac_fee = rs.getDouble("fac_fee");
				resInfo.total_fee += facInfo.fac_fee;
			}
		}
		//总记录数
		dateHash.put("totalRec", new Integer(totalRec));
		return dateHash;
	}
	
	public PreparedStatement getRptStmt(Connection con, String itmTableName, String itmIdColName, long root_ent_id, long tcr_id, 
			Timestamp start_datetime, Timestamp end_datetime, int fac_type, boolean is_count) throws SQLException {
		String sql = getFMFeeStatSql(tcr_id, itmTableName, itmIdColName, start_datetime, end_datetime, fac_type, is_count);
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, FMReservationManager.RSV_STATUS_OK);
		stmt.setString(index++, ViewFmFacilitySchedule.FSH_STATUS_RESERVED);
		stmt.setLong(index++, root_ent_id);
		stmt.setString(index++, ViewFmFacility.FAC_STATUS_ON);
		if (fac_type > 0) {
			stmt.setInt(index++, fac_type);
		}
		stmt.setLong(index++, tcr_id);
		stmt.setBoolean(index++, false);
		stmt.setString(index++, aeItem.ITM_TYPE_CLASSROOM);
		if (start_datetime != null) {
			stmt.setTimestamp(index++, start_datetime);
		}
		if (end_datetime != null) {
			stmt.setTimestamp(index++, end_datetime);
	    }
		return stmt;
	}
	
	private static String getFMFeeStatSql(long tcr_id, String itmTableName, String itmIdColName, 
										Timestamp start_datetime, Timestamp end_datetime, int fac_type, boolean isCount) {
		StringBuffer sql = new StringBuffer(); 
		if (isCount) {
			sql.append(" SELECT rsv_id ");
		} else {
			sql.append(" SELECT rsv_id, rsv_purpose, fsh_date, fsh_start_time, fsh_end_time, usr_display_bil, rsv_participant_no, fac_title, fac_fee ");
		}
		sql.append(" FROM aeItem ")
		   .append(" INNER JOIN fmReservation ON (rsv_status = ? AND rsv_id = itm_rsv_id) ")
		   .append(" INNER JOIN regUser ON (rsv_ent_id = usr_ent_id) ")
		   .append(" INNER JOIN fmFacilitySchedule ON (fsh_status = ? AND fsh_owner_ent_id = ? AND fsh_rsv_id = rsv_id) ")
		   .append(" INNER JOIN fmFacility ON (fac_status = ? AND fac_owner_ent_id = fsh_owner_ent_id AND fac_id = fsh_fac_id ");
		if (fac_type > 0) {
			sql.append(" AND fac_ftp_id = ? ");
		}
		sql.append(" ) WHERE itm_tcr_id = ? ")
		   .append(" AND itm_create_run_ind = ? AND itm_type = ? ");
		if (start_datetime != null) {
			sql.append(" AND itm_eff_start_datetime >= ? ");
		}
		if (end_datetime != null) {
			sql.append(" AND itm_eff_start_datetime <= ? ");
		}
		sql.append(" AND EXISTS (SELECT ").append(itmIdColName).append(" FROM ").append(itmTableName)
		   .append(" WHERE ").append(itmIdColName).append(" = itm_id) ");
		if (isCount) {
			sql.append(" GROUP BY rsv_id, fsh_date ");
		} else {
			sql.append(" ORDER BY rsv_id, fsh_start_time ");
		}
		return sql.toString();
	}

	public static List getItmIdLst(Connection con, long[] itm_id_lst, String[] tnd_id_lst, loginProfile prof) throws NumberFormatException, SQLException {
		List itmIds = new ArrayList();
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
        return itmIds;
	}
}

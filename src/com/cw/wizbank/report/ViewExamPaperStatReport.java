package com.cw.wizbank.report;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.cw.wizbank.ae.db.sql.OuterJoinSqlStatements;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CwnUtil;

public class ViewExamPaperStatReport {
	public static final long PAGE_SIZE_SHOW_STAT_ONLY = 100;
	public static final long PAGE_SIZE_TOTAL_GROUP = 5;
	public static final long PAGE_SIZE_RECORD_USR = 10;
	
	public static final String TST_HASH = "TST_HASH";
	public static final String USR_HASH = "USR_HASH";
	public static final String VIEW_HASH = "VIEW_HASH";
	public static final String SUMMARY_STAT = "SUMMARY_STAT";
	public static final String ORDER_LST = "ORDER_LST";
	
	static final String LAB_90_100 = " 90%-100%";
	static final String LAB_80_90 = " 80%-89%";
	static final String LAB_70_80 = " 70%-79%";
	static final String LAB_60_70 = " 60%-69%";
	static final String LAB_0_60 = " 0%-59%";
	
	MYSQLDbHelper mysqlDbHelper = null;
	
	public class TestInfo {
		public long res_id;
		public String res_title;
		public String itm_title;
		public float mod_max_score;
		public float pass_score;
		public float usr_max_score;
		public float usr_least_score = -999;
		public float usr_total_score;
		public int usr_pass_cnt;
		public float score_90_score;
		public float score_80_score;
		public float score_70_score;
		public float score_60_score;
		public int score_90_100_cnt;
		public int score_80_90_cnt;
		public int score_70_80_cnt;
		public int score_60_70_cnt;
		public int score_0_60_cnt;
		public List usrLst = new ArrayList();
	}
	
	public class SummaryStat {
		public int itmCnt;
		public int tstCnt;
		public int usrCnt;
		public int examineeCnt;
		public int score_90_100_cnt;
		public int score_80_90_cnt;
		public int score_70_80_cnt;
		public int score_60_70_cnt;
		public int score_0_60_cnt;
	}
	
	public class UsrInfo {
		public long usr_ent_id;
		public String usr_ste_usr_id;
		public String usr_display_bil;
		public String usr_email;
		public String usr_tel_1;
		public String usr_extra_2;
		public String usg_display_bil;
		public String ugr_display_bil;
		public String sks_title;
		public Map<String, ModInfo> modHash = new HashMap<String, ModInfo>();
	}
	
	public class ModInfo {
		public long mov_mod_id;
		public boolean is_pass;
		public float mov_score;
		public int mov_total_attempt;
		public Timestamp start_visit_time;
		public Timestamp last_visit_time;
		public long mod_res_id;
	}
	
	public HashMap getTestData(Connection con,
			String[] ent_id_lst,
			int itmCnt,
			List modIds,
			Timestamp att_start_datetime,
			Timestamp att_end_datetime, 
			String col_order,
			boolean show_stat_only,
			boolean is_view,
			ExportController controller, String lrn_scope_sql)
			throws SQLException, cwException {
        String entIdTableName = null;
        String entIdColName = "usr_ent_ids";
        String modTableName = null;
        String modIdColName = "tmp_mod_id";
        Vector usr_ent_ids = null;
        HashMap hs = null;
		boolean isMysql = false;
		String physical_modTableName = null;
		String physical_entIdTableName = null;
		if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
			mysqlDbHelper = new MYSQLDbHelper();
			isMysql = true;
		}
        
        try {
	        // create temp table name of itm_id & ent_id
	        if (ent_id_lst != null && ent_id_lst.length > 0) {
	            List in_ent_ids = cwUtils.string2LongArrayList(ent_id_lst);
	            Vector ent_id_vec = new Vector();
	            usr_ent_ids = LearnerRptExporter.getUserEntId(con, ent_id_vec, in_ent_ids);
	            cwUtils.removeDuplicate(usr_ent_ids);
	            entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);
	        }
	        
			if (modIds != null && modIds.size() > 0) {
				modTableName = cwSQL.createSimpleTemptable(con, modIdColName, cwSQL.COL_TYPE_LONG, 0);
			}
	
			if(entIdColName !=null && entIdTableName != null) {
				cwSQL.insertSimpleTempTable(con, entIdTableName, usr_ent_ids, cwSQL.COL_TYPE_LONG);
			}
			if (entIdTableName == null && lrn_scope_sql != null) {
			    entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0, lrn_scope_sql);
			}
			if(modIds != null){
				cwSQL.insertSimpleTempTable(con, modTableName, modIds, cwSQL.COL_TYPE_STRING);
			}

            if(isMysql){
            	physical_entIdTableName = mysqlDbHelper.tempTable2Physical(con, entIdTableName);
            	physical_modTableName = mysqlDbHelper.tempTable2Physical(con, modTableName);
            }

			//获取测验的信息
			if (modTableName != null) {
				if(isMysql){
					hs = getTstInfo(con, entIdColName,  physical_entIdTableName, modIds, att_start_datetime, att_end_datetime, 
							col_order, modIdColName, physical_modTableName);
				}else{
					hs = getTstInfo(con, entIdColName,  entIdTableName, modIds, att_start_datetime, att_end_datetime, 
							col_order, modIdColName, modTableName);
				}

				
				int usr_count = 0;
				if (usr_ent_ids == null) {
				    usr_count = getCount(con, entIdColName, entIdTableName);
				} else {
				    usr_count = usr_ent_ids.size();
				}
				//获取报告摘要的统计信息和每个学员的信息
				setSummaryData(con, hs, itmCnt, usr_count - 1, show_stat_only, is_view, controller);
		        
			} else {
				if (controller != null) {
					controller.setTotalRow(0);
				}
			}
		} finally {
			if(modTableName != null){
				cwSQL.dropTempTable(con, modTableName);
				if(isMysql){
					mysqlDbHelper.dropTable(con, physical_modTableName);
				}
	        }
	
	        if(entIdTableName != null) {
	        	cwSQL.dropTempTable(con, entIdTableName);	
	        	if(isMysql){
	        		mysqlDbHelper.dropTable(con, physical_entIdTableName);
	        	}
	        }
		}

		return hs;
	}

	public HashMap getTstInfo(Connection con, String entIdColName,String entIdTableName,
			List mod_id_lst, Timestamp att_start_datetime, Timestamp att_end_datetime,
		    String col_order, String modIdColName, String modTableName) throws SQLException, cwException {

		String sql_search = OuterJoinSqlStatements.ExamPaperStatReportSearchTstData(con,
				entIdColName, entIdTableName, mod_id_lst, att_start_datetime,
				att_end_datetime, modIdColName, modTableName, col_order);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap hs = null;
		try {
			stmt = con.prepareStatement(sql_search);
			int index = 1;
			stmt.setString(index++, dbRegUser.USR_STATUS_OK);
			if (att_start_datetime != null) {
				stmt.setTimestamp(index++, att_start_datetime);
			}
			if (att_end_datetime != null) {
				stmt.setTimestamp(index++, att_end_datetime);
			}
			rs = stmt.executeQuery();
			hs = handleTransData(con, rs);
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		
		return hs;
	}
	
	private HashMap handleTransData(Connection con, ResultSet rs) throws SQLException {
		HashMap tstHash = new HashMap();
		HashMap usrHash = new HashMap();
		HashMap tstData = new HashMap();
		List orderLst = new ArrayList();
		tstData.put(TST_HASH, tstHash);
		tstData.put(USR_HASH, usrHash);
		tstData.put(ORDER_LST, orderLst);
		
		Long dataKey = null;
		TestInfo tstInfo = null;
		UsrInfo usrInfo = null;
		ModInfo modInfo = null;
		long res_id = 0;
		long usr_ent_id = 0;
		Long usrId = null;
		while(rs.next()) {
			
			res_id = rs.getLong("res_id");
			dataKey = new Long(res_id);
			if (tstHash.containsKey(dataKey)) {
				tstInfo = (TestInfo)tstHash.get(dataKey);
			} else {
				orderLst.add(dataKey);
				tstInfo = new TestInfo();
				tstHash.put(dataKey, tstInfo);
				tstInfo.res_id = res_id;
				tstInfo.itm_title = rs.getString("t_title");
				tstInfo.res_title = rs.getString("res_title");
				tstInfo.mod_max_score = rs.getFloat("mod_max_score");
				tstInfo.pass_score = tstInfo.mod_max_score * (rs.getFloat("mod_pass_score") / (float)100);
				tstInfo.pass_score = getFormatScore(tstInfo.pass_score);
				tstInfo.score_90_score =  (float)(tstInfo.mod_max_score * 0.9);
				tstInfo.score_80_score =  (float)(tstInfo.mod_max_score * 0.8);
				tstInfo.score_70_score =  (float)(tstInfo.mod_max_score * 0.7);
				tstInfo.score_60_score =  (float)(tstInfo.mod_max_score * 0.6);
			}
			if(rs.getTimestamp("start_visit_time") != null && !rs.getTimestamp("start_visit_time").equals(rs.getTimestamp("last_visit_time"))){
				usr_ent_id = rs.getLong("mov_ent_id");
				if (usr_ent_id > 0) {
					usrId = new Long(usr_ent_id);
					if (usrHash.containsKey(usrId)) {
						usrInfo = (UsrInfo)usrHash.get(usrId);
						if(!tstInfo.usrLst.contains(usrId)){
							tstInfo.usrLst.add(usrId);
						}
					} else {
						usrInfo = new UsrInfo();
						usrHash.put(usrId, usrInfo);
						usrInfo.usr_ent_id = usr_ent_id;
						tstInfo.usrLst.add(usrId);
					}
					
					modInfo = new ModInfo();
					modInfo.mov_mod_id = res_id;
					modInfo.mov_score = rs.getFloat("mov_score");
					modInfo.mov_total_attempt = rs.getInt("mov_total_attempt");
					modInfo.start_visit_time = rs.getTimestamp("start_visit_time");
					modInfo.last_visit_time = rs.getTimestamp("last_visit_time");
					modInfo.mod_res_id = res_id;
					usrInfo.modHash.put((rs.getLong("mvh_tkh_id")+ "_" +res_id), modInfo);
					
					tstInfo.usr_total_score += modInfo.mov_score;
					//最低分
					if (tstInfo.usr_least_score < 0) {
						tstInfo.usr_least_score = modInfo.mov_score;
					} else if (modInfo.mov_score < tstInfo.usr_least_score) {
						tstInfo.usr_least_score = modInfo.mov_score;
					}
					//最高分
					if (tstInfo.usr_max_score < modInfo.mov_score) {
						tstInfo.usr_max_score = modInfo.mov_score;
					}
					//合格人数
					if (modInfo.mov_score >= tstInfo.pass_score) {
						modInfo.is_pass = true;
						tstInfo.usr_pass_cnt++;
					}
					//90%-100% 分数段的人数
					if (modInfo.mov_score <= tstInfo.mod_max_score && modInfo.mov_score >= tstInfo.score_90_score) {
						tstInfo.score_90_100_cnt++;
					}
					//80%-90% 分数段的人数
					else if (modInfo.mov_score < CwnUtil.formatNumber(tstInfo.mod_max_score * new Float(0.9), 4) && modInfo.mov_score >= tstInfo.score_80_score) {
						tstInfo.score_80_90_cnt++;
					}
					//70%-80% 分数段的人数
					else if (modInfo.mov_score < CwnUtil.formatNumber(tstInfo.mod_max_score * new Float(0.8), 4) && modInfo.mov_score >= tstInfo.score_70_score) {
						tstInfo.score_70_80_cnt++;
					}
					//60%-70% 分数段的人数
					else if (modInfo.mov_score < CwnUtil.formatNumber(tstInfo.mod_max_score * new Float(0.7), 4) && modInfo.mov_score >= tstInfo.score_60_score) {
						tstInfo.score_60_70_cnt++;
					}
					//0%-60% 分数段的人数
					else if (modInfo.mov_score < CwnUtil.formatNumber(tstInfo.mod_max_score * new Float(0.6), 4)) {
						tstInfo.score_0_60_cnt++;
					}
				}
			}
		}
		return tstData;
	}
	
	//提取预览要显示的mod_id 和 usr_ent_id，获取每个要显示的user的详细信息 
	public void setSummaryData( Connection con, HashMap tstData, int itmCnt, int usrCnt, boolean show_stat_only, 
								boolean isView, ExportController controller) throws SQLException {
		Map tstHash = (Map)tstData.get(TST_HASH);
		Map usrHash = (Map)tstData.get(USR_HASH);
		List orderLst = (List)tstData.get(ORDER_LST);
		//保存预览会显示的id，key: mod_id,  value: usr_ent_id list
		Map viewHash = new HashMap();
		tstData.put(VIEW_HASH, viewHash);
		
		SummaryStat sumStat = new SummaryStat();
		tstData.put(SUMMARY_STAT, sumStat);
		//课程总数
		sumStat.itmCnt = itmCnt;
		//试卷总数
		sumStat.tstCnt = tstHash.size();
		//学员总数
		sumStat.usrCnt = usrCnt;
		//参加考试总人数
		sumStat.examineeCnt = usrHash.size();
		
		int count = 0;
		int i, j, s, pageSize;
		List usrIds = new ArrayList();
		TestInfo tstInfo = null;
		Long entIdObj = null;
		Long modIdObj = null;
		List viewUsrLst = null;
		for (i = 0; i < orderLst.size(); i++) {
			modIdObj = (Long)orderLst.get(i);
			tstInfo = (TestInfo)tstHash.get(modIdObj);
			
			s = tstInfo.usrLst.size();
			if (isView) {
				//预览页面只显示最多5个测验模块
				pageSize = ReportTemplate.PAGE_SIZE_VIEW_PHOTO;
				if (show_stat_only) {
					//预览页面只显示最多10个测验统计摘要
					pageSize = ReportTemplate.PAGE_SIZE_VIEW_RECORD;
				}
				if (count < pageSize) {
					viewUsrLst = new ArrayList();
					viewHash.put(modIdObj, viewUsrLst);
				} else {
					break;
				}
				//预览页面只显示最多10个学员信息
				if (s > ReportTemplate.PAGE_SIZE_VIEW_RECORD) {
					s = ReportTemplate.PAGE_SIZE_VIEW_RECORD;
				}
			}
			
			if (!show_stat_only) {
			    //把每个参加测验的学员id 提取出来，不保留重复的id
				for (j = 0; j < s; j++) {
					entIdObj = (Long)tstInfo.usrLst.get(j);
				    if (usrIds.indexOf(entIdObj) == -1) {
				    	usrIds.add(entIdObj);
					}
				    if (isView && count < ReportTemplate.PAGE_SIZE_VIEW_PHOTO) {
				    	viewUsrLst.add(entIdObj);
				    }
				}
			}
			count++;
		}
		
		if (controller != null) {
			if (show_stat_only) {
				//测验数
				controller.setTotalRow(tstHash.size());
			} else {
				//查数据库的人数 + 每个测验的人数
				controller.setTotalRow(usrIds.size() + getExamControllerCnt(tstHash));
			}
		}
		
		String entIdTableName = null;
        String entIdColName = "tmp_ent_id";
        if (usrIds.size() > 0) {
        	entIdTableName = cwSQL.createSimpleTemptable(con, entIdColName, cwSQL.COL_TYPE_LONG, 0);

        	if(entIdTableName != null) {
        		cwSQL.insertSimpleTempTable(con, entIdTableName, usrIds, cwSQL.COL_TYPE_LONG);
        		if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
        			mysqlDbHelper = new MYSQLDbHelper();
        			String physical_entIdTableName = mysqlDbHelper.tempTable2Physical(con, entIdTableName);
        			setUserData(con, entIdColName, physical_entIdTableName, usrHash, controller);
        			mysqlDbHelper.dropTable(con, physical_entIdTableName);
        		}else{
        			setUserData(con, entIdColName, entIdTableName, usrHash, controller);
        		}
        		cwSQL.dropTempTable(con, entIdTableName);
    		}
		}
	}
	
	private void setUserData(Connection con, String entIdColName, String entIdTableName, Map usrHash, ExportController controller) throws SQLException {
		String sql = " SELECT usr_ent_id, usr_ste_usr_id, usr_display_bil, usr_email, usr_tel_1,usr_extra_2, usg_display_bil, ugr_display_bil, sks_title "
			       + " FROM regUser "
			       + " INNER JOIN entityRelation usg ON (usg.ern_parent_ind = ? AND usg.ern_type = ? AND usg.ern_child_ent_id = usr_ent_id) "
			       + " INNER JOIN userGroup ON (usg_ent_id = usg.ern_ancestor_ent_id) "
			       + " INNER JOIN entityRelation ugr ON (ugr.ern_parent_ind = ? AND ugr.ern_type = ? AND ugr.ern_child_ent_id = usr_ent_id) "
			       + " INNER JOIN userGrade ON (ugr_ent_id = ugr.ern_ancestor_ent_id) "
			       + " LEFT JOIN RegUserSkillSet ON (uss_ent_id = usr_ent_id) "
			       + " LEFT JOIN cmSkillSet ON (sks_ske_id = uss_ske_id) "
			       + " WHERE EXISTS (SELECT " + entIdColName +  " FROM " + entIdTableName + " WHERE " + entIdColName + " = usr_ent_id) ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setBoolean(index++, true);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setBoolean(index++, true);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			rs = stmt.executeQuery();
			Long keyObj = null;
			UsrInfo usrInfo = null;
			while (rs.next()) {
				keyObj = new Long(rs.getLong("usr_ent_id"));
				usrInfo = (UsrInfo)usrHash.get(keyObj);
				if (usrInfo != null) {
					usrInfo.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
					usrInfo.usr_display_bil = rs.getString("usr_display_bil");
					usrInfo.usr_email = rs.getString("usr_email");
					usrInfo.usr_tel_1 = rs.getString("usr_tel_1");
					usrInfo.usr_extra_2 = rs.getString("usr_extra_2");
					usrInfo.usg_display_bil = rs.getString("usg_display_bil");
					usrInfo.ugr_display_bil = rs.getString("ugr_display_bil");
					usrInfo.sks_title = rs.getString("sks_title");
				}
				if (controller != null) {
					if (controller.isCancelled()) {
						break;
					}
					controller.next();
				}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public String getStatImage(String chartImgSavedPath, String tempPathName, String cur_lang, int score_90_100_cnt, 
						int score_80_90_cnt, int score_70_80_cnt, int score_60_70_cnt, int score_0_60_cnt) throws IOException {
		List chartdata = new ArrayList();
		ReportChart rChart = new ReportChart();
		ReportChart.PiePlotData pieData = null;
		pieData = rChart.new PiePlotData();
		pieData.label = LangLabel.getValue(cur_lang, "789") + "(" + LAB_90_100 + ")";
		pieData.value = score_90_100_cnt;
		pieData.pie_ats_id = 0;
		chartdata.add(pieData);
		
		pieData = rChart.new PiePlotData();
		pieData.label = LangLabel.getValue(cur_lang, "790") + "(" + LAB_80_90 + ")";
		pieData.value = score_80_90_cnt;
		pieData.pie_ats_id = 2;
		chartdata.add(pieData);
		
		pieData = rChart.new PiePlotData();
		pieData.label = LangLabel.getValue(cur_lang, "lab_normal") + "(" + LAB_70_80 + ")";
		pieData.value = score_70_80_cnt;
		pieData.pie_ats_id = 3;
		chartdata.add(pieData);
		
		pieData = rChart.new PiePlotData();
		pieData.label = LangLabel.getValue(cur_lang, "lab_passed") + "(" + LAB_60_70 + ")";
		pieData.value = score_60_70_cnt;
		pieData.pie_ats_id = 4;
		chartdata.add(pieData);
		
		pieData = rChart.new PiePlotData();
		pieData.label = LangLabel.getValue(cur_lang, "lab_failed") + "(" + LAB_0_60 + ")";
		pieData.value = score_0_60_cnt;
		pieData.pie_ats_id = 1;
		chartdata.add(pieData);
		
		String title = LangLabel.getValue(cur_lang, "748");
		String imgpath = cwUtils.SLASH + tempPathName + cwUtils.SLASH + rChart.create(chartImgSavedPath, title, chartdata);
		imgpath = cwUtils.replaceSlashToHttp(imgpath);
		return ".." + imgpath;
	}
	
	private int getExamControllerCnt(Map tstHash) {
		int cnt = 0;
		TestInfo tstInfo = null;
		Map.Entry entry = null;
		Set dataSet = tstHash.entrySet();
		Iterator iter = dataSet.iterator();
		while (iter.hasNext()) {
			entry = (Map.Entry)iter.next();
			tstInfo = (TestInfo)entry.getValue();
			cnt += tstInfo.usrLst.size();
		}
		return cnt;
	}
	
	private int getCount(Connection con, String colName,String tableName) throws SQLException {
	    int count =  0;
	    String sql = "select count(distinct " + colName + ") from " + tableName;
	    PreparedStatement pstmt = con.prepareStatement(sql);
	    ResultSet rs = pstmt.executeQuery();
	    if (rs.next())
	        count = rs.getInt(1);
	    pstmt.close();
	    return count;
	}
	
	private float getFormatScore(float score) {
		return new Float(cwUtils.formatNumber(score, 1)).floatValue();
	}
}

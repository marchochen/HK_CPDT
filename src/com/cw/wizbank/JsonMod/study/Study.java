package com.cw.wizbank.JsonMod.study;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.commonBean.ItemTypeBean;
import com.cw.wizbank.JsonMod.study.bean.ApprovalCourseBean;
import com.cw.wizbank.JsonMod.study.bean.CourseBean;
import com.cw.wizbank.JsonMod.study.bean.CourseTypeBean;
import com.cw.wizbank.JsonMod.study.bean.PendingCourseBean;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.db.DbAppnApprovalList;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

/**
 * 处理学习模块的业务类
 * @author kimyu
 */
public class Study {
    //JDBC product name : oracle
    public static final String ProductName_ORACLE = "oracle";
    
    // JDBC product name : microsoft sql server
    public static final String ProductName_MSSQL = "microsoft sql server";
    
    // JDBC product name : db2
    public static final String ProductName_DB2 = "db2";
    
    // JDBC product name : Postgres SQL
    public static final String ProductName_POSTGRESQL = "postgresql";
    
    // 所有正在等待中、学习中、已结束3个状态下的课程标识
	public static final int GET_ALL_MY_COURSES = 0;
	
	// 待审批课程标识
	public static final int GET_MY_PENDING_COURSES = 1;	
	
	// 当前已审批的课程标识
	public static final int GET_APPROVAL_COURSES = 2;
	
	// 课程搜索状态
	public static final String STATUS_PENDING_WAITING = "PENDING_WAITING";	// 等待审批
	public static final String STATUS_STUDYING = "STUDYING";				// 学习中
	public static final String STATUS_FINISHED = "FINISHED";				// 已结束
	public static final String STATUS_ATTEND = "ATTEND";					// 已完成
	public static final String STATUS_INCOMPLETE = "INCOMPLETE";			// 未完成
	public static final String STATUS_WITHDRAWN = "WITHDRAWN";				// 已放弃
	
	/**
	 * 获取与该学员相关的所有待审批课程信息
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 审批课程的集合
	 * @throws SQLException
	 */
	public Vector getMyPendingCourses(Connection con, long usr_ent_id, String inLang, BaseParam param, String itmDir)
		throws SQLException {
		
		StudyModuleParam studyModuleParam = (StudyModuleParam)param;
		
		// 查询与该学员相关的审批课程SQL
		String sql = "select appn.app_ent_id usr_ent_id, appn.app_id, appn.app_itm_id, "		// 学员usr_ent_id、学员报名记录表aeApplication的app_id、app_itm_id
			+ cwSQL.replaceNull("citm2.itm_id", "citm.itm_id") + " itm_id, "			// 课程ID
			+ " citm.itm_type, citm.itm_icon, "													// 课程类型(5.0以前版本)、课程图标
//			+ " citm.itm_exam_ind, citm.itm_blend_ind, citm.itm_ref_ind, citm.itm_run_ind, citm.itm_create_run_ind, "	// 是否是考试、是否是混合、是否是参考、是否是班级、是否可以开设班级
			+ cwSQL.replaceNull("citm2.itm_exam_ind", "citm.itm_exam_ind") + " itm_exam_ind, "		// 是否是考试
			+ cwSQL.replaceNull("citm2.itm_blend_ind", "citm.itm_blend_ind") + " itm_blend_ind, "	// 是否是混合
			+ cwSQL.replaceNull("citm2.itm_ref_ind", "citm.itm_ref_ind") + " itm_ref_ind, "			// 是否是参考
			+ cwSQL.replaceNull("citm2.itm_run_ind", "citm.itm_run_ind") + " itm_run_ind, "			// 是否是班级
			+ cwSQL.replaceNull("citm2.itm_create_run_ind", "citm.itm_create_run_ind") + " itm_create_run_ind, "// 是否可以开设班级
			+ cwSQL.replaceNull("citm2.itm_title", "citm.itm_title")  + " itm_title, "	// 课程名称
			+ " appn.app_create_timestamp, appn.app_status, appn.app_upd_timestamp,"				// 申请日期，审批状态
			+ " citm.itm_eff_start_datetime, citm.itm_eff_end_datetime, "	// 离线课程(或混合课程)的开始时间、结束时间
			+ " citm.itm_content_eff_start_datetime, citm.itm_content_eff_end_datetime "		// 混合课程的网上内容(或网上课程)开始时间、结束时间
			+ " from aeApplication appn "
			+ " inner join aeItem citm on (appn.app_itm_id=citm.itm_id and citm.itm_status='ON') "
			// 加入班级对应的离线课程
			+ " left join aeItemRelation citmR on appn.app_itm_id=citmR.ire_child_itm_id "
			+ " left join aeItem citm2 on citmR.ire_parent_itm_id=citm2.itm_id "
			+ " where app_ent_id=? "	// 传入：学员的usr_ent_id
			+ "	and (appn.app_status='Pending' or appn.app_status='Waiting') ";
		
		if ("COS".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_COS, "citm", true);
		} else if ("EXAM".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM, "citm", true);
		} else if ("INTEGRATED".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED, "citm", true);
		}
		
		// 排序
		if(studyModuleParam.getSort() != null && studyModuleParam.getDir() != null){
			sql += " order by  ?  ?";
		}else {
			sql += " order by appn.app_upd_timestamp desc ";
		}
		
		Vector pendingCourseVector = new Vector();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, usr_ent_id);
			if(studyModuleParam.getSort() != null && studyModuleParam.getDir() != null){
				pstmt.setString(2, studyModuleParam.getSort());
				pstmt.setString(3, studyModuleParam.getDir());
			}
			int count=0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int end = (studyModuleParam.getStart() + studyModuleParam.getLimit() - 1);// 每页所显示记录的最大序号
				
				// 采用假分页技术(所有数据库都通用)
				if(count >= studyModuleParam.getStart() && count <= end) {
					PendingCourseBean pendingCourseBean = new PendingCourseBean();
					
					pendingCourseBean.setApp_id(rs.getLong("app_id"));									// 对应学员报名记录表aeApplication的app_id
					pendingCourseBean.setApp_itm_id(rs.getLong("app_itm_id"));							// 对应学员报名记录表aeApplication的app_itm_id
					pendingCourseBean.setItm_id(rs.getLong("itm_id"));									// 课程ID
					pendingCourseBean.setApp_status(rs.getString("app_status"));						// 审批状态(即报名状态)
					pendingCourseBean.setItm_title(rs.getString("itm_title"));							// 课程名称
					pendingCourseBean.setItm_type(rs.getString("itm_type"));							// 课程类型
					pendingCourseBean.setApp_create_timestamp(rs.getTimestamp("app_create_timestamp"));	// 申请日期
					pendingCourseBean.setItm_eff_start_datetime(rs.getTimestamp("itm_eff_start_datetime"));	// 面授期间-开始日期
					pendingCourseBean.setItm_eff_end_datetime(rs.getTimestamp("itm_eff_end_datetime"));		// 面授期间-结束时间
					pendingCourseBean.setItm_content_eff_start_datetime(rs.getTimestamp("itm_content_eff_start_datetime"));	// 混合课程的网上内容-开始时间
					pendingCourseBean.setItm_content_eff_end_datetime(rs.getTimestamp("itm_content_eff_end_datetime"));		// 混合课程的网上内容-结束时间
					
					//last approver
					HashMap map = this.getLastApproverInfo(con, usr_ent_id, pendingCourseBean.getApp_id());
					if (map.get("aal_action_timestamp") != null) {
						pendingCourseBean.setAal_action_timestamp((Timestamp)map.get("aal_action_timestamp"));
						pendingCourseBean.setLast_approval_date((Timestamp)map.get("aal_action_timestamp"));
					} else {
						pendingCourseBean.setAal_action_timestamp(rs.getTimestamp("app_upd_timestamp"));	
					}
					pendingCourseBean.setUsr_display_bil((String)map.get("usr_display_bil"));

					// 下一(批)审批者
					pendingCourseBean.setNext_approver(this.getWaitingApproverNames(con, usr_ent_id, pendingCourseBean.getApp_id()));
					
					boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
					boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
					boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
					boolean itm_run_ind = rs.getBoolean("itm_run_ind");
					boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
					
					// 设置以下属性以用于前台判断该课程是什么类型的课程(网上课程，面授课程，混合课程)
					pendingCourseBean.setItm_blend_ind(itm_blend_ind);
					pendingCourseBean.setItm_exam_ind(itm_exam_ind);
					pendingCourseBean.setItm_create_run_ind(itm_create_run_ind);
					pendingCourseBean.setItm_run_ind(itm_run_ind);
					pendingCourseBean.setItm_ref_ind(itm_ref_ind);
					
					String itmDummyType = aeItemDummyType.getDummyItemType(pendingCourseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
					pendingCourseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, itmDummyType));
					pendingCourseBean.setItm_dummy_type(itmDummyType);
					
					String itmIcon = rs.getString("itm_icon");
					String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("itm_id"), itmIcon);
				    pendingCourseBean.setItm_icon(itmIconPath);
					
					pendingCourseVector.add(pendingCourseBean);
				}
				
				count++;
			} // end while
			
			studyModuleParam.setTotal_rec(count);	// 设置总记录数
		} catch(SQLException se) {
			System.err.println("[Get_MyPending_Courses_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return pendingCourseVector;
	}
	
	/**
	 * 获取某位学员已报名正在学习中的课程信息
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param param URL参数信息
	 * @param itmDir
	 * @return 已报名正在学习中的课程集合
	 */
	public Vector getCurrentCourses(Connection con, long usr_ent_id, String inLang, BaseParam param, String itmDir)
		throws SQLException {
		
		StudyModuleParam studyModuleParam = (StudyModuleParam)param;
		
		String getDateStr = cwSQL.getDate();
		
		// 查询某位学员已报名正在学习中的课程SQL
		String sql = "select appn.app_id, appn.app_itm_id, appn.app_ent_id, citm.itm_type, "	// 学员报名记录ID，学员ID，课程类型
			+ cwSQL.replaceNull("citm2.itm_id", "citm.itm_id") + " itm_id, "			// 课程ID
			+ cwSQL.replaceNull("citm2.itm_title", "citm.itm_title") + " itm_title, "	// 课程名
//			+ " citm.itm_exam_ind, citm.itm_blend_ind, citm.itm_ref_ind, citm.itm_run_ind, citm.itm_create_run_ind, "	// 是否是考试、是否是混合、是否是参考、是否是班级、是否可以开设班级
			+ cwSQL.replaceNull("citm2.itm_exam_ind", "citm.itm_exam_ind") + " itm_exam_ind, "		// 是否是考试
			+ cwSQL.replaceNull("citm2.itm_blend_ind", "citm.itm_blend_ind") + " itm_blend_ind, "	// 是否是混合
			+ cwSQL.replaceNull("citm2.itm_ref_ind", "citm.itm_ref_ind") + " itm_ref_ind, "			// 是否是参考
			+ cwSQL.replaceNull("citm2.itm_run_ind", "citm.itm_run_ind") + " itm_run_ind, "			// 是否是班级
			+ cwSQL.replaceNull("citm2.itm_integrated_ind", "citm.itm_integrated_ind") + " itm_integrated_ind, " // 是否是集成培训
			+ cwSQL.replaceNull("citm2.itm_create_run_ind", "citm.itm_create_run_ind") + " itm_create_run_ind, "// 是否可以开设班级
			+ " citm.itm_content_eff_start_datetime, citm.itm_content_eff_end_datetime, "	// 用于网上课程的处理
			+ cwSQL.replaceNull("citm2.itm_icon", "citm.itm_icon") + " itm_icon, "	// 课程图标
			// 混合课程的网上内容(或网上课程)开始时间(注：网上课程开始时间若为空，则以报名成功日期代替(前提：aeApplication表中的app_status字段值为'Admitted')，则可以将app_upd_timestamp视为学员报名日期)
			+ cwSQL.replaceNull("citm.itm_content_eff_start_datetime", "appn.app_upd_timestamp") + " content_eff_start_datetime, "
			// 混合课程的网上内容(或网上课程)结束时间(注：网上课程结束时间若为空，则以报名成功日期+网上课程有效天数)
			+ cwSQL.replaceNull("citm.itm_content_eff_end_datetime", cwSQL.dateadd("appn.app_upd_timestamp", "citm.itm_content_eff_duration")) + " content_eff_end_datetime, "
			+ " citm.itm_content_eff_end_datetime, citm.itm_content_eff_duration, "	// 该两字段组合起来，用于识别混合课程的网上内容(或网上课程)的结束日期是否是无限
			+ " citm.itm_eff_start_datetime, citm.itm_eff_end_datetime, "	// 离线课程(或混合课程)对应班级的开始时间、结束时间
			+ " appn.app_upd_timestamp, appn.app_status, "					// 报名被成功录取的日期，课程审批状态
			+ " attStatus.ats_type, "				// 学习状态
			+ " courseEval.cov_score, "				// 分数
			+ " courseEval.cov_total_time, "		// 累计学习时长
			+ " courseEval.cov_last_acc_datetime, "	// 学员上次访问课程内容的日期
			+ " courseEval.cov_progress, "			// 进度
			+ " courseEval.cov_tkh_id, "
			+ " cour.cos_res_id "
			+ " from aeApplication appn "
			// 加入学员相关课程表
			+ " inner join aeItem citm on (citm.itm_status='ON' and appn.app_itm_id=citm.itm_id) "
			// 加入学员学习课程内容的评估表
			+ " left join course cour on citm.itm_id=cour.cos_itm_id "
			// 加入课程内容
			+ " left join courseEvaluation courseEval on ( "
			+ " 	appn.app_ent_id=courseEval.cov_ent_id "
			+ " 	and cour.cos_res_id=courseEval.cov_cos_id "
			+ " 	and appn.app_tkh_id=courseEval.cov_tkh_id "
			+ " ) "
			// 加入考勤表
			+ " left join aeAttendance attend on appn.app_id=attend.att_app_id "
			// 加入考勤状态标识表
			+ " left join aeAttendanceStatus attStatus on attend.att_ats_id=attStatus.ats_id "
			// 加入课程与班级关系表，用以识别假如学员报读的是班级的离线课程名称
			+ " left join aeItemRelation citmR on (appn.app_itm_id=citmR.ire_child_itm_id) "
			+ " left join aeItem citm2 on ( "
			+ " 	citm2.itm_create_run_ind = 1 "
			+ " 	and citmR.ire_parent_itm_id=citm2.itm_id "
			+ " ) "
			+ " where appn.app_ent_id=? "				// 传入：学员的usr_ent_id
			+ " and appn.app_status='Admitted' "		// 已报名
			+ " and (citm.itm_status='ON' and (citm2.itm_status='ON' or citm2.itm_status is null)) "	// 处理离线课程(或离线考试)未发布，对应的班级(或考批次)已发布的情况，此种情况学员是看不到的
			// 判断是课程是在结束时间以内
			+ " and ( " 
			+ " 		(citm.itm_blend_ind = 1 and (" + getDateStr + " < citm.itm_eff_end_datetime or " + getDateStr + " < citm.itm_content_eff_end_datetime)) "	// 混合课程(考试)的处理
			+ "			or (citm.itm_blend_ind = 0 and citm.itm_run_ind=0 and citm.itm_create_run_ind = 0 and citm.itm_ref_ind = 0"	// 网上课程(考试)的处理
//			+ "				and ( "
			+ "             and attStatus.ats_type in ('" + aeAttendanceStatus.STATUS_TYPE_PROGRESS + "')"
//			+ "						((citm.itm_content_eff_duration is null or citm.itm_content_eff_duration = 0) and citm.itm_content_eff_end_datetime is null) "
//		    + "   				 	or (" + getDateStr + " < citm.itm_content_eff_end_datetime) "
//		    + "			       	 	or (citm.itm_content_eff_duration is not null and " + getDateStr + " < "+ cwSQL.dateadd("attend.att_create_timestamp", "citm.itm_content_eff_duration") +") "
//		    + "					) "
		    + "			   ) "
		    + "			or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 and (" + getDateStr + " < citm.itm_eff_end_datetime)) "	// 面授课程(考试)的处理
		    + "		) "
		    // 考勤状态不能是'未完成'、'已放弃'
		    + " and (attStatus.ats_type <> '" + aeAttendanceStatus.STATUS_TYPE_INCOMPLETE + "' and attStatus.ats_type <> '" + aeAttendanceStatus.STATUS_TYPE_WITHDRAWN + "') ";
		
		if ("COS".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_COS, "citm", true);
		} else if ("EXAM".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM, "citm", true);
		} else if ("INTEGRATED".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED, "citm", true);
		}
		
		if (studyModuleParam.getSort() == null) {	// 默认排序(按上次访问倒序排列)
			sql += " order by cov_last_acc_datetime desc";
		}
		
		Vector currentCourseVector = new Vector();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, usr_ent_id);
			if (studyModuleParam.getSort() != null &&  studyModuleParam.getDir() != null) {
				pstmt.setString(2, studyModuleParam.getSort());
				pstmt.setString(3, studyModuleParam.getDir());
			}
			int count=0;
			rs = pstmt.executeQuery();
			int end = (studyModuleParam.getStart() + studyModuleParam.getLimit() - 1);// 每页所显示记录的最大序号
			while (rs.next()) {
				// 采用假分页技术(所有数据库都通用)
				if(count >= studyModuleParam.getStart() && count <= end) {
					ApprovalCourseBean approvalCourseBean = new ApprovalCourseBean();
					
					approvalCourseBean.setApp_id(rs.getLong("app_id"));				// 报名记录表aeApplication的app_id
					approvalCourseBean.setApp_itm_id(rs.getLong("app_itm_id")); 	// 报名记录表aeApplication的app_itm_id
					approvalCourseBean.setItm_id(rs.getLong("itm_id"));				// 课程ID
					approvalCourseBean.setApp_status(rs.getString("app_status"));	// 审批状态(即报名状态)
					approvalCourseBean.setAts_type(rs.getString("ats_type"));		// 学习状态
					approvalCourseBean.setItm_title(rs.getString("itm_title"));		// 课程标题
					approvalCourseBean.setItm_type(rs.getString("itm_type"));		// 课程类型(5.0以前版本)
					approvalCourseBean.setApp_upd_timestamp(rs.getTimestamp("app_upd_timestamp"));				// 报名被成功录取的时间
					approvalCourseBean.setItm_eff_start_datetime(rs.getTimestamp("itm_eff_start_datetime"));	// 离线课程(或混合课程)的开始时间
					approvalCourseBean.setItm_eff_end_datetime(rs.getTimestamp("itm_eff_end_datetime"));		// 离线课程(或混合课程)的结束时间
					approvalCourseBean.setItm_content_eff_start_datetime(rs.getTimestamp("content_eff_start_datetime"));	// 混合课程的网上内容(或网上课程)开始时间
					
					// 混合课程的网上内容(或网上课程)结束时间
					if (rs.getTimestamp("itm_content_eff_end_datetime") == null && rs.getInt("itm_content_eff_duration") == 0) {// 若网上课程结束日期设定为不限
						approvalCourseBean.setItm_content_eff_end_datetime(null);
					} else {
						approvalCourseBean.setItm_content_eff_end_datetime(rs.getTimestamp("content_eff_end_datetime"));	
					}
					
					approvalCourseBean.setCov_total_time(Utilities.parseSecond2Time(rs.getDouble("cov_total_time")));		// 总计学习时长
					approvalCourseBean.setCov_score(rs.getDouble("cov_score"));						// 分数
					approvalCourseBean.setCov_last_acc_datetime(rs.getTimestamp("cov_last_acc_datetime"));// 学员上次访问课程内容时间
					approvalCourseBean.setCov_progress(rs.getDouble("cov_progress"));				// 进度
					approvalCourseBean.setCov_tkh_id(rs.getLong("cov_tkh_id"));
					approvalCourseBean.setCos_res_id(rs.getLong("cos_res_id"));
					
					// 判断某门课程的标识(网上课程，面授课程，混合课程)
					boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
					boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
					boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
					boolean itm_run_ind = rs.getBoolean("itm_run_ind");
					boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
					boolean itm_integrated_ind = rs.getBoolean("itm_integrated_ind");
					
					// 设置以下属性以用于前台判断该课程是什么类型的课程(网上课程，面授课程，混合课程， 集成培训)
					approvalCourseBean.setItm_blend_ind(itm_blend_ind);
					approvalCourseBean.setItm_exam_ind(itm_exam_ind);
					approvalCourseBean.setItm_create_run_ind(itm_create_run_ind);
					approvalCourseBean.setItm_run_ind(itm_run_ind);
					approvalCourseBean.setItm_ref_ind(itm_ref_ind);
					approvalCourseBean.setItm_integrated_ind(itm_integrated_ind);
					
					// 对网上课程的内容设定了"当学员被成功报名"至"年-月-日"的特殊处理(此种情况下，对应的itm_content_eff_start_datetime字段不为空)
					if(itm_blend_ind == false && itm_run_ind == false 
						&& itm_create_run_ind == false && itm_ref_ind == false) {
						
						if (rs.getTimestamp("itm_content_eff_start_datetime") != null
								&& rs.getTimestamp("itm_content_eff_end_datetime") != null) {
							
							approvalCourseBean.setItm_content_eff_start_datetime(rs.getTimestamp("app_upd_timestamp"));
						}
					}
					
//					approvalCourseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, aeItemDummyType.getDummyItemType(approvalCourseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind)));
					String itmDummyType = aeItemDummyType.getDummyItemType(approvalCourseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
					approvalCourseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, itmDummyType));
					approvalCourseBean.setItm_dummy_type(itmDummyType);
					
					String itmIcon = rs.getString("itm_icon");
					String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("itm_id"), itmIcon);
					approvalCourseBean.setItm_icon(itmIconPath);
					
					approvalCourseBean.setComp_criteria(con, approvalCourseBean.getCov_tkh_id(), approvalCourseBean.getCos_res_id());
					currentCourseVector.add(approvalCourseBean);
				}
				
				count++;
			} // end while
			
			studyModuleParam.setTotal_rec(count);	// 设置总记录数
		} catch(SQLException se) {
			System.err.println("[Get_Current_Course_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return currentCourseVector;
	}
	
//	/**
//	 * 获取学员未报名推荐的课程
//	 * @param con 数据库连接器
//	 * @param usr_ent_id 用户ID
//	 * @return 被推荐的课程
//	 * @throws SQLException
//	 */
//	public Vector getUnSignUpRecommendedCos(Connection con, long usr_ent_id) throws SQLException {
//		
//		// 未报名推荐的课程SQL	
//		String sql = "select itm_id, itm_title "
//			+ " from aeItemTargetRuleDetail, "	// 规则表
//			+ " 	 aeItem, "					// 课程(或班级)表
//			+ " 	 aeItemRelation "			// 课程与班级关系表
//			 				   		   // 精确查找学员所在的用户组ID
//			+ " where ird_group_id in (select ern_ancestor_ent_id "	// 用户组ID(对应Entity表的ent_id)
//			+ "				   		   from EntityRelation "		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
//			+ "				   		   where ern_child_ent_id=?	"	// 传入：学员的ID(RegUser表中的usr_ent_id)
//			//+ "				  	     		 and ern_parent_ind=1 "	// 限定只查找学员所属的父组
//			+ "					     		 and ern_type='USR_PARENT_USG') "
//								   // 精确查找学员所属的职务ID 	
//			+ "		  and ird_grade_id in (select ern_ancestor_ent_id "	// 职务ID(对应Entity表的ent_id)
//			+ "		 					   from EntityRelation "		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
//			+ "					   		   where ern_child_ent_id=? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
//			+ "						     		 and ern_parent_ind=1 "	// 限定只查找学员所属的职务
//			+ "						     		 and ern_type='USR_CURRENT_UGR' "
//			+ "  					  ) "
//			+ " 	  and (ird_upt_id = -1 "
//			  						  	   // 精确查找用户所属的岗位能力
//			+ "  	   	   or ird_upt_id in ( "// 查询用户的能力
//			+ "  	   						   select skb_ske_id ske_id "
//			+ "  	   					  	   from RegUserSkillSet, cmSkillSet, "		// 学员与岗位的关系表，岗位表，
//			+ "										cmSkillSetCoverage,	cmSkillBase "	// 岗位与能力关系表，能力表
//			+ "						   	   	   where uss_ent_id=? "	// 传入：学员ID(RegUser表的usr_ent_id)	
//			+ "								 	 	 and uss_ske_id=sks_ske_id "
//			+ "								 		 and sks_type='SKP' "
//			+ "								 	 	 and ssc_sks_skb_id=sks_skb_id "
//			+ "								 	 	 and ssc_skb_id=skb_id "
//			+ "						   	   	   union "
//									   	   	   // 查询用户所属的岗位
//			+ "						   	   	   select uss_ske_id ske_id "
//			+ "						   	   	   from RegUserSkillSet "	// 用户与岗位的关系表
//			+ "						   	   	   where uss_ent_id=? "		// 传入：学员ID(RegUser表的usr_ent_id)
//			+ "  	   					  	  ) "
//			+ "	  		  ) "
//			+ "	      and itm_status ='ON' "
//			+ "		  and ird_type='TARGET_LEARNER' "
//			+ "		  and itm_run_ind = 0 "
//			+ "		  and not exists (select null "
//			+ "  				  	  from aeApplication " 
//			+ "						  left join aeItemRelation on app_itm_id = ire_child_itm_id " 
//			+ " 					  where app_ent_id=? "	// 传入：学员ID(RegUser表的usr_ent_id)
//		    + "		            	  		and app_status in ('Pending','Admitted') "
//		    + "		                		and (ire_child_itm_id = itm_id or app_itm_id = itm_id) "
//			+ "		 				 ) ";
//		
//		
//		Vector unSignUpCommendedCosVector = new Vector();
//		
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		try {
//			pstmt = con.prepareStatement(sql);
//			
//			int count = 1;
//			pstmt.setLong(count++, usr_ent_id);
//			pstmt.setLong(count++, usr_ent_id);
//			pstmt.setLong(count++, usr_ent_id);
//			pstmt.setLong(count++, usr_ent_id);
//			pstmt.setLong(count++, usr_ent_id);
//			
//			rs = pstmt.executeQuery();
//			while (rs.next()) {
//				CourseBean courseBean = new CourseBean();
//				
//				courseBean.setItm_id(rs.getLong("itm_id"));
//				courseBean.setItm_title(rs.getString("itm_title"));
//				
//				unSignUpCommendedCosVector.addElement(courseBean);
//			} // end while
//			
//
//		} catch(SQLException se) {
//			System.err.println("[Get_UnSignUp_Commended_Cos_Exception]: " + se.getMessage());
//			throw se;
//		} finally {
//			// 关闭相关的数据库操作
//			if (rs != null) {
//				rs.close();
//				rs = null;
//			}
//			
//			if (pstmt != null) {
//				pstmt.close();
//				pstmt = null;
//			}
//		}
//		
//		return unSignUpCommendedCosVector;
//	}
	
	/**
	 * 获取学员未报名推荐的课程
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @return 被推荐的课程
	 * @throws SQLException
	 */
	public Vector getUnSignUpRecommendedCos(Connection con, long usr_ent_id) throws SQLException {
		
		// 查询未报名推荐的课程的SQL	
		String sql = " select distinct citm.itm_id, citm.itm_title "
			+ " from " 
			+ " (	select ird_itm_id itm_id "	// 查询该学员所有符合给定规则的课程ID
			+ " 	from aeItemTargetRuleDetail "  	   
			+ " 	where 	ird_group_id in ( "
			+ "					select ern_ancestor_ent_id "	// 查找学员所属用户组			   		   
			+ "					from EntityRelation " 				   		   
			+ "					where ern_child_ent_id = ? "		// 传入：学员的ID(RegUser表中的usr_ent_id)				  	     		 
//			+ "						  and ern_parent_ind = 1 "				     		 
			+ "						  and ern_type = 'USR_PARENT_USG' "
			+ "				)"
			+ " 			and ird_grade_id in ( "
			+ "						select ern_ancestor_ent_id "// 查找学员所属职级	 					   
			+ "						from EntityRelation " 					   		   
			+ "						where ern_child_ent_id = ? " 	// 传入：学员ID(RegUser表的usr_ent_id)					     		 
//			+ "							  and ern_parent_ind = 1 "						     		 
			+ "							  and ern_type = 'USR_CURRENT_UGR' "
			+ "				)"	
			+ " 			and (ird_upt_id = -1 "   	   	   
			+ "			   		 or ird_upt_id in ( "			// 查询用户的能力   	   						   
            + "                                     select upr_upt_id "
            + "                                     from UserPositionRelation "
            + "                                     where upr_usr_ent_id = ? "
			+ "					 ) "	
			+ "				) " 	 
			+ " 			and ird_type = 'TARGET_LEARNER'"
			+ " ) t1 "
			+ " inner join aeItem citm on t1.itm_id = citm.itm_id "
			+ " left join "	
			+ " (	select " + cwSQL.replaceNull("citm2.itm_id", "citm1.itm_id") + " itm_id "	// 查询该学员所有已报名和等待审批的课程ID
			+ " 	from aeApplication "
			+ " 	inner join aeItem citm1 on (citm1.itm_id = app_itm_id and citm1.itm_status = 'ON') "
			+ " 	left join aeItemRelation on app_itm_id = ire_child_itm_id "
			+ " 	left join aeItem citm2 on ire_parent_itm_id = citm2.itm_id "
			+ " 	where app_ent_id = ? "	// 传入：学员ID(RegUser表的usr_ent_id)
			+ "			  and app_status in ('Pending','Admitted') "
			+ "	) t2 on (t1.itm_id = t2.itm_id) "
			+ " where t2.itm_id is null ";
		
		
		Vector unSignUpCommendedCosVector = new Vector();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			
			int count = 1;
			pstmt.setLong(count++, usr_ent_id);
			pstmt.setLong(count++, usr_ent_id);
			pstmt.setLong(count++, usr_ent_id);
			pstmt.setLong(count++, usr_ent_id);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CourseBean courseBean = new CourseBean();
				
				courseBean.setItm_id(rs.getLong("itm_id"));
				courseBean.setItm_title(rs.getString("itm_title"));
				
				unSignUpCommendedCosVector.addElement(courseBean);
			} // end while
			

		} catch(SQLException se) {
			System.err.println("[Get_UnSignUp_Commended_Cos_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return unSignUpCommendedCosVector;
	}
	
	/**
	 * 获取正在等待中、学习中、已结束3个状态下的课程信息
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 正在等待中、学习中、已结束3个状态下的课程集合
	 */
	public Vector getAllMyCourses(Connection con, long usr_ent_id, String inLang, BaseParam param, String itmDir)
		throws SQLException {
		
		return getAnyTypeCourses(con, usr_ent_id, inLang, Study.GET_ALL_MY_COURSES, null, null, param, itmDir);
		
	}
	
	/**
	 * 获取与学员相关的已结束的课程(前提：课程学习状态不能是正在学习中的课程，可以是已完成的课程、或未完成的、或已放弃的课程)
	 * 前提：必须是与学员相关的已审批的课程
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 与学员相关的已结束的课程集合
	 * @throws SQLException 
	 */
	public Vector getMyHistoryCourses(Connection con, long usr_ent_id, String inLang, BaseParam param, String itmDir)
		throws SQLException {
		
		// 设置要查询的与学员相关的课程状态选项(除了正在学习中这一状态，可以是其他所有状态)
//		List atsTypeList = new ArrayList();
//		atsTypeList.add("ATTEND");
//		atsTypeList.add("INCOMPLETE");
//		atsTypeList.add("WITHDRAWN");
				
		// 获取与学员相关的已结束的课程(状态：不能是正在学习中的课程，可以是已完成的课程、或未完成的、或已放弃的课程)
		// 前提：必须是与学员相关的已审批的课程
//		Vector historyCourseVector = getAnyTypeCourses(con, usr_ent_id, inLang,
//				Study.GET_APPROVAL_COURSES, atsTypeList, null, param, itmDir);
		Vector historyCourseVector = getAnyTypeCourses(con, usr_ent_id, inLang,
				Study.GET_APPROVAL_COURSES, null, null, param, itmDir);
		
		return historyCourseVector;
	}
	
	/**
	 * 获取接下来若干个天数应出席的课程(按课程期限列出所有后续若干个天数学员可以学习的网上课程和离线课程)
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param numOfDays 给定的天数
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 接下来若干个天数应出席的课程集合
	 * @throws SQLException
	 */
	public Vector getAttendCourses(Connection con, long usr_ent_id, String inLang, 
			int numOfDays, BaseParam param, String itmDir)
		throws SQLException {
		
		// 设置要查询的与学员相关的课程状态选项(可以是正在学习中这一状态，或还未参与)
		List atsTypeList = new ArrayList();
		atsTypeList.add("PROGRESS");
		atsTypeList.add("NULL");	// 加入此选项，表示学员已报名这门课程，但可能还未参与

		// 以下为新加入的用于判断接下来若干个天数应出席的课程
		// 查询条件：(课程开始时间<=当前时间 and 课程结束时间>=当前时间) or (课程开始时间>=当前时间 and 课程开始时间<=当前时间+设定的天数)
		String curDate = cwSQL.getDate();
		String appendCondition =
			  " and ( "
			// 课堂(或网上课程)开始时间(注：网上课程开始时间若为空，则以报名成功日期代替(前提：aeApplication表中的app_status字段值为'Admitted')，则可以将app_upd_timestamp视为学员报名日期)
			// 课堂(或网上课程)结束时间(注：网上课程结束时间若为空，则以报名成功日期+网上课程有效天数)
			+ " 	( " + cwSQL.replaceNull("citm.itm_content_eff_start_datetime", "appn.app_upd_timestamp") + " <= " + curDate + " and " + cwSQL.replaceNull("citm.itm_content_eff_end_datetime", cwSQL.dateadd("appn.app_upd_timestamp", "citm.itm_content_eff_duration")) + " >= " + curDate + " ) "
			+ "  	 or ( " + cwSQL.replaceNull("citm.itm_content_eff_start_datetime", "appn.app_upd_timestamp") + " >= " + curDate + " and " + cwSQL.replaceNull("citm.itm_content_eff_start_datetime", "appn.app_upd_timestamp") + " <= " + cwSQL.dateadd(null, new Integer(numOfDays).toString()) + " ) "
			+ " ) ";
		
		// 查询前提：必须是已审批课程(Study.GET_APPROVAL_COURSES)
		// 这样配合新加入的用于判断接下来若干个天数月应出席的课程条件(appendContion)，才能查询出已审批的网上课程的开始与结束日期
		Vector attendCourseVector = getAnyTypeCourses(con, usr_ent_id, inLang,
				Study.GET_APPROVAL_COURSES, atsTypeList, appendCondition, param, itmDir);
		
		
		return attendCourseVector;
	}
	
	/**
	 * 获取与学员相关的即将结束的课程
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param numOfDays 给定的天数
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 与学员相关的即将结束的课程集合
	 * @throws SQLException
	 */
	public Vector getShouldFinishedCourses(Connection con, long usr_ent_id, String inLang, 
			int numOfDays, BaseParam param, String itmDir)
		throws SQLException {
		
		// 设置要查询的与学员相关的课程状态选项(可以是正在学习中这一状态，或还未参与)
		List atsTypeList = new ArrayList();
		atsTypeList.add("PROGRESS");
		atsTypeList.add("NULL");	// 加入此选项，表示学员已报名这门课程，但可能还未参与
		
		// 以下为新加入的用于判断快结束的课程的条件
		// 查询条件：(课程结束日期>=当前日期 and 课程结束日期<=当前时间+设定的天数)
		String appendCondition =
			  " and ( "
			// 课堂(或网上课程)开始时间(注：网上课程开始时间若为空，则以报名成功日期代替(前提：aeApplication表中的app_status字段值为'Admitted')，则可以将app_upd_timestamp视为学员报名日期)
			// 课堂(或网上课程)结束时间(注：网上课程结束时间若为空，则以报名成功日期+网上课程有效天数)
			+ cwSQL.replaceNull("citm.itm_eff_end_datetime", cwSQL.dateadd("appn.app_upd_timestamp", "citm.itm_content_eff_duration"))
			+ " >= " + cwSQL.getDate() + " and "
			+ cwSQL.replaceNull("citm.itm_eff_end_datetime", cwSQL.dateadd("appn.app_upd_timestamp", "citm.itm_content_eff_duration"))
			+ " <= " + cwSQL.dateadd(null, new Integer(numOfDays).toString())
			+ " ) ";
		
		// 查询前提：必须是已审批课程(Study.GET_APPROVAL_COURSES)
		// 这样配合新加入的用于判断快结束的课程条件(appendContion)，才能查询出已审批的网上课程的结束日期
		Vector attendCourseVector = getAnyTypeCourses(con, usr_ent_id, inLang,
				Study.GET_APPROVAL_COURSES, atsTypeList, appendCondition, param, itmDir);
		
		Vector shouldFinishedCourseVector = new Vector();
		
		return shouldFinishedCourseVector;
	}
	
	/**
	 * return list of ItemTypeBean {@link com.cw.wizbank.JsonMod.commonBean.ItemTypeBean ItemTypeBean}}.
	 * @param con
	 * @param owner_ent_id site ent id.
	 * @param groupType  specified course type , eg:COS:课程，EXAM：考试，REF:参考， null for all.
	 * @param lang TODO
	 * @return
	 * @throws SQLException
	 */
	public static Vector getItemTypes(Connection con, long owner_ent_id, List groupType, String cur_lang) throws SQLException {
		DbItemType[] itm_types = DbItemType.getAllItemTypeInOrg(con, owner_ent_id);
		Vector allItemTypeVec = new Vector();
		Vector vcos = new Vector();
		Vector vexam = new Vector();
		Vector vinteg = new Vector();
		Vector vref = new Vector();
		String ALL = "ALL";
		for(int i = 0;i < itm_types.length; i++) {
			DbItemType itm_type = itm_types[i];
			ItemTypeBean tbean = new ItemTypeBean();
			tbean.setItm_blend_ind(itm_type.ity_blend_ind);
			tbean.setItm_exam_ind(itm_type.ity_exam_ind);
			tbean.setItm_ref_ind(itm_type.ity_ref_ind);
			tbean.setItm_type(itm_type.ity_id);
			tbean.setItm_create_run_ind(itm_type.ity_create_run_ind);
			tbean.setItm_integ_ind(itm_type.ity_integ_ind);
		
			String dummy_type = aeItemDummyType.getDummyItemType(tbean.getItm_type(), tbean.isItm_blend_ind(), tbean.isItm_exam_ind(), tbean.isItm_ref_ind());
			tbean.setItm_dummy_type(dummy_type);
			tbean.setLabel(aeItemDummyType.getItemLabelByDummyType(cur_lang, tbean.getItm_dummy_type()));
//				courseTypeBean.setIs_group(is_group);
			if (tbean.isItm_exam_ind()) {
				vexam.add(tbean);
			} else if (tbean.isItm_ref_ind()) {
				vref.add(tbean);
			} else if (tbean.isItm_integ_ind()) {	
				vinteg.add(tbean);
			} else {
				vcos.add(tbean);
			}
		}
		{
			if (vinteg.size() > 0) {
				if (groupType == null || groupType.contains(aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED)) {
					ItemTypeBean tbean = new ItemTypeBean();
					tbean.setIs_group_type(true);
					tbean.setItm_dummy_type(aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED);
					tbean.setLabel(aeItemDummyType.getItemLabelByDummyType(cur_lang, ALL + aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER + tbean.getItm_dummy_type()));
					allItemTypeVec.add(tbean);
				}
			}
		}
		{
			if (vcos.size() > 0) {
				if (groupType == null || groupType.contains(aeItemDummyType.ITEM_DUMMY_TYPE_COS)) {
					ItemTypeBean tbean = new ItemTypeBean();
					tbean.setIs_group_type(true);
					tbean.setItm_dummy_type(aeItemDummyType.ITEM_DUMMY_TYPE_COS);
//					hard code here to add a label "all" before a group type label
					tbean.setLabel(aeItemDummyType.getItemLabelByDummyType(cur_lang, ALL + aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER + tbean.getItm_dummy_type()));
					allItemTypeVec.add(tbean);
					allItemTypeVec.addAll(vcos);
				}
			}
		}
		{
			if (vexam.size() > 0) {
				if (groupType == null || groupType.contains(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM)) {
					ItemTypeBean tbean = new ItemTypeBean();
					tbean.setIs_group_type(true);
					tbean.setItm_dummy_type(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM);
//					hard code here to add a label "all" before a group type label
					tbean.setLabel(aeItemDummyType.getItemLabelByDummyType(cur_lang, ALL + aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER + tbean.getItm_dummy_type()));
					allItemTypeVec.add(tbean);
					allItemTypeVec.addAll(vexam);
				}
			}
		}
		{
			if (vref.size() > 0) {
				if (groupType == null || groupType.contains(aeItemDummyType.ITEM_DUMMY_TYPE_REF)) {
					ItemTypeBean tbean = new ItemTypeBean();
					tbean.setIs_group_type(true);
					tbean.setItm_dummy_type(aeItemDummyType.ITEM_DUMMY_TYPE_REF);
//					hard code here to add a label "all" before a group type label
					tbean.setLabel(aeItemDummyType.getItemLabelByDummyType(cur_lang, ALL + aeItemDummyType.ITEM_DUMMY_TYPE_DELIMITER + tbean.getItm_dummy_type()));
					allItemTypeVec.add(tbean);
					allItemTypeVec.addAll(vref);
				}
			}
		}
		return allItemTypeVec;
	}
	
	
	/**
	 * 获取系统中的课程类型(Wizbank5.0以前版本)
	 * @param con 数据库连接器
	 * @return 课程类型的信息集合
	 */
	public Vector getCourseTypes(Connection con) throws SQLException {
		
		String sql = "select ity_owner_ent_id, ity_id, ity_run_ind "	// 参照Entity表的ent_id，课程或班级的类型标识，是否为班级
			+ " from aeItemType where ity_run_ind=0";
		
		Vector courseTypeVector = new Vector();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				CourseTypeBean courseTypeBean = new CourseTypeBean();
				
				courseTypeBean.setIty_owner_ent_id(rs.getInt("ity_owner_ent_id"));	// 参照Entity表的ent_id
				courseTypeBean.setIty_id(rs.getString("ity_id"));		// 课程或班级的类型标识	
				courseTypeBean.setIty_run_ind(rs.getInt("ity_run_ind"));// 是否为班级，用于区分课程还是班级(可选值：0 否，1 是)
				
				courseTypeVector.add(courseTypeBean);
			}
			
		} catch(SQLException se) {
			System.err.println("[Get_Course_Types_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}
		
		return courseTypeVector;
	}
	
	/**
	 * 搜索符合条件的课程
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param param URL参数信息
	 * @return 与学员相关的课程集合
	 */
	public Vector searchCourses(Connection con, long usr_ent_id, String inLang, BaseParam param, String itmDir) 
		throws SQLException {
		
		StudyModuleParam studyModuleParam = (StudyModuleParam)param;
		
		String srh_range = studyModuleParam.getSrh_range();	// 分辨是从我的全部课程页搜索还是从我的历史课程搜索(可选值："ALL","HIS")
		
		String srh_key = Utilities.filter(studyModuleParam.getSrh_key());				// 关键词
		String srh_key_type = (String) studyModuleParam.getSrh_key_type();			//是否全文搜索
		String[] srh_itm_type_lst = studyModuleParam.getSrh_itm_type_lst();	// 课程类型选项
		String[] srh_status_lst = studyModuleParam.getSrh_status_lst();		// 课程状态选项
		String[] srh_att_status_lst = studyModuleParam.getSrh_att_status_lst();//结训状态
		// 以下是时间期限搜索参数的可选值：
		// 即时开始 IMMEDIATE、 最近一周 LAST_1_WEEK、 最近两周 LAST_2_WEEK
		// 最近的一个月 LAST_1_MONTH、 最近的两个月 LAST_2_MONTH、 不限 UNLIMITED
		String srh_last_acc_period = studyModuleParam.getSrh_last_acc_period();	// 上次访问参与期限
		String srh_admitted_period = studyModuleParam.getSrh_admitted_period();	// 录取时间的期限
		String srh_end_period = studyModuleParam.getSrh_end_period();			// 课程结束时间的期限
		
		// 以下为新加入的用于搜索符合要求课程的条件
		String appendCondition = "";
		
		// 若是从我的历史课程页面搜索，且前台没有传入课程状态选项
//		if ("HIS".equalsIgnoreCase(srh_range)
//			&& (srh_status_lst == null || (srh_status_lst != null && srh_status_lst.length <= 0))) {
//			appendCondition += " and attStatus.ats_type in ('ATTEND', 'INCOMPLETE', 'WITHDRAWN') ";
//		}
		
		// 加入匹配关键词的查询条件
		if (srh_key != null && !"".equals(srh_key)) {
			if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {
				appendCondition += " and "
					+ cwSQL.replaceNull("citm2.itm_srh_content", "citm.itm_srh_content")
					+ " like ? ";
			} else {
				appendCondition += " and "
					+ cwSQL.replaceNull("citm2.itm_title", "citm.itm_title")
					+ " like ? ";
			}
		}
		
		// 加入课程类型判断(单选)
		if (srh_itm_type_lst != null && srh_itm_type_lst.length > 0) {
			appendCondition += aeItemDummyType.genSqlByItemDummyType(srh_itm_type_lst[0], "citm", true);
		}
		
		HashMap statusMap = new HashMap();
		statusMap.put(Study.STATUS_PENDING_WAITING, " (appn.app_status='"+aeApplication.PENDING+"' or appn.app_status='"+aeApplication.WAITING+"') ");
		
		String getDateStr = cwSQL.getDate();
		String studying_subSql =
			  " appn.app_status='" + aeApplication.ADMITTED + "' "
			+ " and ( " 
			+ " 		(citm.itm_blend_ind = 1 and (" + getDateStr + " < citm.itm_eff_end_datetime or " + getDateStr + " < citm.itm_content_eff_end_datetime)) "	// 混合课程(考试)的处理
			+ "			or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 0 and citm.itm_create_run_ind = 0 and citm.itm_ref_ind = 0"	// 网上课程(考试)的处理
			+ "              and attStatus.ats_type = '" + aeAttendanceStatus.STATUS_TYPE_PROGRESS + "'"
//			+ "				and ( "
//			+ "						((citm.itm_content_eff_duration is null or citm.itm_content_eff_duration = 0) and citm.itm_content_eff_end_datetime is null) "
//		    + "   				 	or (" + getDateStr + " < citm.itm_content_eff_end_datetime) "
//		    + "			       	 	or (citm.itm_content_eff_duration is not null and " + getDateStr + " < "+ cwSQL.dateadd("attend.att_create_timestamp", "citm.itm_content_eff_duration") +") "
//		    + "					) "
		    + "			   ) "
		    + "			or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 and (" + getDateStr + " < citm.itm_eff_end_datetime)) "	// 面授课程(考试)的处理
		    + "		) "
		    // 考勤状态不能是'未完成'、'已放弃'
		    + " and (attStatus.ats_type <> '" + aeAttendanceStatus.STATUS_TYPE_INCOMPLETE + "' and attStatus.ats_type <> '" + aeAttendanceStatus.STATUS_TYPE_WITHDRAWN + "') ";
		statusMap.put(Study.STATUS_STUDYING, studying_subSql);
		
		String finished_subSql =
			  " appn.app_status='" + aeApplication.ADMITTED + "' "
			+ " and ( "
			+ "			( " 
			+ " 			(citm.itm_blend_ind = 1 and (" + getDateStr + ">citm.itm_eff_end_datetime and " + getDateStr + " > citm.itm_content_eff_end_datetime)) "	// 混合课程(考试)的处理
			+ "				 or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 0 and citm.itm_create_run_ind = 0 and citm.itm_ref_ind = 0"	// 网上课程(考试)的处理
			+ "                  and attStatus.ats_type in ('" + aeAttendanceStatus.STATUS_TYPE_ATTEND + "' , '" + aeAttendanceStatus.STATUS_TYPE_INCOMPLETE + "' , '" + aeAttendanceStatus.STATUS_TYPE_WITHDRAWN + "')"
//			+ "					 and ( "
//			+ "							not((citm.itm_content_eff_duration is null or citm.itm_content_eff_duration = 0) and citm.itm_content_eff_end_datetime is null) "
//		    + "   				 		and (" + getDateStr + " > citm.itm_content_eff_end_datetime "
//		    + "			       	 		 	or (citm.itm_content_eff_duration is not null and citm.itm_content_eff_duration <> 0 and " + getDateStr + " > "+ cwSQL.dateadd("attend.att_create_timestamp", "citm.itm_content_eff_duration") +") "
//		    + "							) "
//		    + "			    	 ) "
		    + "				 ) "
		    + "				 or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 and (" + getDateStr + " > citm.itm_eff_end_datetime)) "	// 面授课程(考试)的处理
		    + "			)"
		    // 考勤状态可以是'未完成'、'已放弃'
		    + " 		or (attStatus.ats_type in('" + aeAttendanceStatus.STATUS_TYPE_INCOMPLETE + "' , '" + aeAttendanceStatus.STATUS_TYPE_WITHDRAWN + "')) "
		    + "	) ";
		statusMap.put(Study.STATUS_FINISHED, finished_subSql);
		
		statusMap.put(Study.STATUS_ATTEND, " appn.app_status='" + aeApplication.ADMITTED + "' and attStatus.ats_type = '" + aeAttendanceStatus.STATUS_TYPE_ATTEND + "' ");
		statusMap.put(Study.STATUS_INCOMPLETE, " appn.app_status='" + aeApplication.ADMITTED + "' and attStatus.ats_type = '" + aeAttendanceStatus.STATUS_TYPE_INCOMPLETE + "' ");
		statusMap.put(Study.STATUS_WITHDRAWN, " appn.app_status='" + aeApplication.ADMITTED + "' and attStatus.ats_type = '" + aeAttendanceStatus.STATUS_TYPE_WITHDRAWN+ "' ");
		
		// 根据前台传递的课程状态搜索参数组合成搜索条件(形如and(... or ...))
		if (srh_status_lst != null && srh_status_lst.length > 0) {
			
			appendCondition += " and ( ";
			
			for (int i = 0; i < srh_status_lst.length;) {
				if(!srh_status_lst[i].equalsIgnoreCase("FINISHED") && !srh_status_lst[i].equalsIgnoreCase("PENDING_WAITING") && !srh_status_lst[i].equalsIgnoreCase("STUDYING")){
					throw new SQLException("Param Error");
				}
				// 匹配从客户端传递过来的搜索选项值
				appendCondition += (String)statusMap.get(srh_status_lst[i]);
												
				i++;
				if (i < srh_status_lst.length) {
					appendCondition += " or ";
				}
				
			} // end for
			
			appendCondition += " ) ";
		} // end if
		
		if (srh_att_status_lst != null && srh_att_status_lst.length > 0) {
			appendCondition += " and attStatus.ats_type in ( ";
			for (int i= 0; i < srh_att_status_lst.length; i++) {
				if(!srh_att_status_lst[i].equalsIgnoreCase("ATTEND") && !srh_att_status_lst[i].equalsIgnoreCase("INCOMPLETE") && !srh_att_status_lst[i].equalsIgnoreCase("WITHDRAWN")){
					throw new SQLException("Param Error");
				}
				appendCondition += "'" + srh_att_status_lst[i].trim() + "'" + ((i < srh_att_status_lst.length - 1) ? "," : "");
			}
			appendCondition += " ) ";
		}
		// 以下为加入时间期限搜索参数，可选值如下：
		// 即时开始 IMMEDIATE、 最近一周 LAST_1_WEEK、 最近两周 LAST_2_WEEK
		// 最近的一个月 LAST_1_MONTH、 最近的两个月 LAST_2_MONTH、 不限 UNLIMITED
		String[] periods = {"IMMEDIATE",
							"LAST_1_WEEK", "LAST_2_WEEK", 
							"LAST_1_MONTH", "LAST_2_MONTH",
							"UNLIMITED"};
		
		// 加入上次访问参与期限判断条件
		boolean flag_1 = false;
		for (int i = 0; i < periods.length; i++) {
			String elementOfPeriods = periods[i];
			
			if (srh_last_acc_period != null && !"".equals(srh_last_acc_period)
					&& elementOfPeriods.equalsIgnoreCase(srh_last_acc_period)) { // 若从客户端传来的值srh_last_acc_period不为空且能匹配periods中的值
				
				if (elementOfPeriods.equalsIgnoreCase("IMMEDIATE")) {
					// and datediff(day, null, attend.att_timestamp)=0
					appendCondition += " and "
						+ cwSQL.datediff(null, "cov_last_acc_datetime") + " = 0 ";
					
					flag_1 = true;
				}
				
				if (elementOfPeriods.endsWith("WEEK")) {
					// 记下第一次出现"_"的索引位置
					int firstUnderlineIndex = elementOfPeriods.indexOf("_");
					// 截取从数字出现到末尾的子串(即"数字_WEEK")
					String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
					// 截取数字字符串并转换成数字
					int weekPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));
					
					// and (cov_last_acc_datetime>=dateadd(day, -7, getdate()) and cov_last_acc_datetime<=getdate())
					appendCondition += " and ( "
						+ " cov_last_acc_datetime >= " + cwSQL.dateadd(null, new Integer(weekPeriod * (-7)).toString())
						+ " and cov_last_acc_datetime <= " + cwSQL.getDate()
						+ " ) ";
					
					flag_1 = true;
				}
				
				if (elementOfPeriods.endsWith("MONTH")) {
					// 记下第一次出现"_"的索引位置
					int firstUnderlineIndex = elementOfPeriods.indexOf("_");
					// 截取从数字出现到末尾的子串(即"数字_MONTH")
					String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
					// 截取数字字符串并转换成数字
					int monthPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));
					
					// and (cov_last_acc_datetime>=dateadd(month, -1, getdate()) and cov_last_acc_datetime<=getdate())
					appendCondition += " and ( "
						+ " cov_last_acc_datetime >= " + cwSQL.addMonth(cwSQL.getDate(), new Integer(monthPeriod * (-1)).toString())
						+ " and cov_last_acc_datetime <= " + cwSQL.getDate()
						+ " ) ";
					
					flag_1 = true;
				}
			} // end outer if
			
			if (flag_1) {
				break;
			}
			
		} // end for
		
		// 加入录取时间的期限条件的判断
		boolean flag_2 = false;
		for (int i = 0; i < periods.length; i++) {
			String elementOfPeriods = periods[i];
			
			if (srh_admitted_period != null && !"".equals(srh_admitted_period)
					&& elementOfPeriods.equalsIgnoreCase(srh_admitted_period)) { // 若从客户端传来的值srh_admitted_period不为空且能匹配periods中的值
				
				if (elementOfPeriods.equalsIgnoreCase("IMMEDIATE")) {
					// and datediff(day, getdate(), attend.att_create_timestamp) = 0
					appendCondition += " and "
						+ cwSQL.datediff(null, "attend.att_create_timestamp") + " = 0 ";
					
					flag_2 = true;
				}
				
				if (elementOfPeriods.endsWith("WEEK")) {
					// 记下第一次出现"_"的索引位置
					int firstUnderlineIndex = elementOfPeriods.indexOf("_");
					// 截取从数字出现到末尾的子串(即"数字_WEEK")
					String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
					// 截取数字字符串并转换成数字
					int weekPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));
					
					// and (attend.att_create_timestamp >= dateadd(day, -7, getdate()) or attend.att_create_timestamp <= getdate())
					appendCondition += " and ( "
						+ " attend.att_create_timestamp >= " + cwSQL.dateadd(null, new Integer(weekPeriod * (-7)).toString())
						+ " and attend.att_create_timestamp <= " + cwSQL.getDate()
						+ " ) ";
					
					flag_2 = true;
				}
				
				if (elementOfPeriods.endsWith("MONTH")) {
					// 记下第一次出现"_"的索引位置
					int firstUnderlineIndex = elementOfPeriods.indexOf("_");
					// 截取从数字出现到末尾的子串(即"数字_MONTH")
					String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
					// 截取数字字符串并转换成数字
					int monthPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));
					
					// and (attend.att_timestamp >= dateadd(month, -1, getdate()) or attend.att_timestamp <= getdate())
					appendCondition += " and ( "
						+ " attend.att_create_timestamp >= " + cwSQL.addMonth(cwSQL.getDate(), new Integer(monthPeriod * (-1)).toString())
						+ " and attend.att_create_timestamp <= " + cwSQL.getDate()
						+ " ) ";
					
					flag_2 = true;
				}
				
			} // end outer if
			
			if (flag_2) {
				break;
			}
			
		} // end for
		
		
		String sub1 = " citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 ";	// 面授课程
		String sub2 = " citm.itm_blend_ind = 1 and citm.itm_run_ind =1 ";	// 混合课程
		String sub3 = " citm.itm_blend_ind = 0 and citm.itm_run_ind = 0 and citm.itm_create_run_ind = 0 and citm.itm_ref_ind = 0 ";	// 网上课程
		
		// 加入课程结束时间的期限的判断
//		String endTimeSql = cwSQL.replaceNull(con, "citm.itm_eff_end_datetime", cwSQL.dateadd(con, "attend.att_create_timestamp", "citm.itm_content_eff_duration"));
		boolean flag_3 = false;
		for (int i = 0; i < periods.length; i++) {
			String elementOfPeriods = periods[i];
			
			if (srh_end_period != null && !"".equals(srh_end_period)
					&& elementOfPeriods.equalsIgnoreCase(srh_end_period)) { // 若从客户端传来的值srh_end_period不为空且能匹配periods中的值
				
				if (elementOfPeriods.equalsIgnoreCase("IMMEDIATE")) {
					// and datediff(day, getdate(), 课程结束时间)=0
//					appendCondition += " and "
//						+ cwSQL.datediff(con, null, endTimeSql) + " = 0";
					
					appendCondition +=
						  " and ( "
						+ "			(" + sub1 + " and " + cwSQL.datediff(null, "citm.itm_eff_end_datetime") + " = 0) "	// 面授课程处理
						+ "			or (" + sub2 + " and " + cwSQL.datediff(null, "citm.itm_eff_end_datetime") + " = 0) "	// 混合课程处理
						+ "			or (" + sub3	// 网上课程的处理
						+ " 				and ( "
						+ "							(citm.itm_content_eff_end_datetime is not null and " + cwSQL.datediff(null, "citm.itm_content_eff_end_datetime") + " = 0) "
						+ " 						or (citm.itm_content_eff_duration > 0 and " + cwSQL.datediff(null, cwSQL.dateadd("attend.att_create_timestamp", "citm.itm_content_eff_duration")) + " = 0) "
						+ "					) "
						+ " 		) "
						+ " ) ";
					flag_3 = true;
				}
				
				if (elementOfPeriods.endsWith("WEEK")) {
					// 记下第一次出现"_"的索引位置
					int firstUnderlineIndex = elementOfPeriods.indexOf("_");
					// 截取从数字出现到末尾的子串(即"数字_WEEK")
					String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
					// 截取数字字符串并转换成数字
					int weekPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));
					
					// and (课程结束时间>=dateadd(day, -7, getdate()) and 课程结束时间<=getdate())
//					appendCondition += " and ( "
//						+ endTimeSql + " >= " + cwSQL.dateadd(con, null, new Integer(weekPeriod * (-7)).toString())
//						+ " and " + endTimeSql + " <= " + cwSQL.getDate(con)
//						+ " ) ";
					appendCondition +=
						  " and ( "
						+ "			(" + sub1 + " and citm.itm_eff_end_datetime between " + cwSQL.dateadd(null, new Integer(weekPeriod * (-7)).toString()) + " and " + getDateStr + ") "	// 面授课程处理
						+ "			or (" + sub2 + " and citm.itm_eff_end_datetime between " + cwSQL.dateadd(null, new Integer(weekPeriod * (-7)).toString()) + " and " + getDateStr + ") "	// 混合课程处理
						+ "			or (" + sub3	// 网上课程的处理
						+ " 				and ( "
						+ "							(citm.itm_content_eff_end_datetime is not null and citm.itm_content_eff_end_datetime between " + cwSQL.dateadd(null, new Integer(weekPeriod * (-7)).toString()) + " and " + getDateStr + ") "
						+ " 						or (citm.itm_content_eff_duration > 0 and " + cwSQL.dateadd("attend.att_create_timestamp", "citm.itm_content_eff_duration") + " between " + cwSQL.dateadd(null, new Integer(weekPeriod * (-7)).toString()) + " and " + getDateStr + ") "
						+ "					) "
						+ " 		) " 
						+ " ) ";
					
					flag_3 = true;
				}
				
				if (elementOfPeriods.endsWith("MONTH")) {
					// 记下第一次出现"_"的索引位置
					int firstUnderlineIndex = elementOfPeriods.indexOf("_");
					// 截取从数字出现到末尾的子串(即"数字_MONTH")
					String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
					// 截取数字字符串并转换成数字
					int monthPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));
					
					// and (课程结束时间>=dateadd(month, -1, getdate()) and 课程结束时间<=getdate())
//					appendCondition += " and ( "
//						+ endTimeSql + " >= " + cwSQL.addMonth(con, cwSQL.getDate(con), new Integer(monthPeriod * (-1)).toString())
//						+ " and " + endTimeSql + " <= " + cwSQL.getDate(con)
//						+ " ) ";
					appendCondition +=
						  " and ( "
						+ "			(" + sub1 + " and citm.itm_eff_end_datetime between " + cwSQL.addMonth(null, new Integer(monthPeriod * (-1)).toString()) + " and " + getDateStr + ") "	// 面授课程处理
						+ "			or (" + sub2 + " and citm.itm_eff_end_datetime between " + cwSQL.addMonth(null, new Integer(monthPeriod * (-1)).toString()) + " and " + getDateStr + ") "	// 混合课程处理
						+ "			or (" + sub3	// 网上课程的处理
						+ " 				and ( "
						+ "							(citm.itm_content_eff_end_datetime is not null and citm.itm_content_eff_end_datetime between " + cwSQL.addMonth(null, new Integer(monthPeriod * (-1)).toString()) + " and " + getDateStr + ") "
						+ " 						or (citm.itm_content_eff_duration > 0 and " + cwSQL.dateadd("attend.att_create_timestamp", "citm.itm_content_eff_duration") + " between " + cwSQL.addMonth(null, new Integer(monthPeriod * (-1)).toString()) + " and " + getDateStr + ") "
						+ "					) "
						+ " 		) " 
						+ " ) ";
					
					flag_3 = true;
				}
				
			} // end outer if
			
			if (flag_3) {
				break;
			}
			
		} // end for
		
		// 搜索符合条件的课程
		Vector searchCourseVector = null;
		if ("ALL".equalsIgnoreCase(studyModuleParam.getSrh_range())) {
			searchCourseVector = getAnyTypeCourses(con, usr_ent_id, inLang, Study.GET_ALL_MY_COURSES, null, appendCondition, studyModuleParam, itmDir);
		} else if ("HIS".equalsIgnoreCase(studyModuleParam.getSrh_range())) {
			searchCourseVector = getAnyTypeCourses(con, usr_ent_id, inLang, Study.GET_APPROVAL_COURSES, null, appendCondition, studyModuleParam, itmDir);
		}
		
		
		return searchCourseVector;
	}
	
	/**
	 * 获取与学员相关的任意课程信息，并可以自动接收客户端排序参数对查询结果排序
	 * 课程类型：可以是与学员相关的待审批课程、或与学员相关的审批通过的课程、或以上两种情况的课程
	 * 与学员相关的课程的学习状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程中的一种情形和多种情形
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员usr_ent_id
	 * @param inLang 语言
	 * @param appStatus 课程类型标识(可以取值Study.GET_ALL_MY_COURSES、Study.GET_MY_PENDING_COURSES、Study.GET_APPROVE_COURSES)
	 * @param atsTypeList 与学员相关的课程学习状态选项(若为空，则默认检索出与该用户相关的所有学习状态下的课程)
	 * @param appendAssuredCondition 附加的确定的(即条件中不带？)查询条件(若为空，则没有附加的查询条件)
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 与学员相关的课程集合
	 */
	private Vector getAnyTypeCourses(Connection con, long usr_ent_id, 
			String inLang, int appStatus, List atsTypeList, 
			String appendAssuredCondition, BaseParam param, String itmDir)
		throws SQLException {
		
		StudyModuleParam studyModuleParam = (StudyModuleParam)param;
		
		// 查询与学员相关的课程(状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程)SQL
		String sql = getAnyTypeCoursesSql(con, appStatus, atsTypeList, appendAssuredCondition, studyModuleParam);
		
		String sort = studyModuleParam.getSort();
		String dir = studyModuleParam.getDir();
		String srh_key = studyModuleParam.getSrh_key_type();
		String srh_key_type = (String) studyModuleParam.getSrh_key_type();
		if (sort == null || "".equals(sort)) {
			sort = "appn.app_upd_timestamp";
			dir = "desc";
		}
		
		// 排序(默认按报名日期倒序排列)
		sql += " order by " + sort + " " + dir;
		
		Vector anyTypeCourseVector = new Vector();
		Vector resultVec = new Vector();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			int index = 1;
			pstmt.setLong(index++, usr_ent_id);
			if(sort != null && dir != null){
				pstmt.setString(2, sort);
				pstmt.setString(3, dir);
			}
			if (srh_key != null && !"".equals(srh_key)) {
				if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {
					pstmt.setString(index++, "%"+srh_key+"%");
				}else{
					pstmt.setString(index++, "%"+srh_key+"%");
				}
			}
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (!"Admitted".equals(rs.getString("app_status"))) {// 若是待审批的课程
					PendingCourseBean pendingCourseBean = new PendingCourseBean();
					
					pendingCourseBean.setApp_id(rs.getLong("app_id"));			// 对应学员报名记录表aeApplication的app_id
					pendingCourseBean.setApp_itm_id(rs.getLong("app_itm_id")); 	// 对应学员报名记录表aeApplication的app_itm_id
					pendingCourseBean.setItm_id(rs.getLong("itm_id"));			// 课程ID
					pendingCourseBean.setApp_status(rs.getString("app_status"));// 审批状态(即报名状态)
					pendingCourseBean.setItm_type(rs.getString("itm_type"));	// 课程类型itm_type
					pendingCourseBean.setItm_title(rs.getString("itm_title"));	// 课程名称itm_title
					
					String itmIcon = rs.getString("itm_icon");
					String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("itm_id"), itmIcon);
					pendingCourseBean.setItm_icon(itmIconPath);
					
					pendingCourseBean.setItm_content_eff_start_datetime(rs.getTimestamp("content_eff_start_datetime"));	// 混合课程的网上内容(或网上课程)开始时间
					
					// 混合课程的网上内容(或网上课程)结束时间
					if (rs.getTimestamp("itm_content_eff_end_datetime") == null && rs.getInt("itm_content_eff_duration") == 0) {// 若网上课程结束日期设定为不限
						pendingCourseBean.setItm_content_eff_end_datetime(null);
					} else {
						pendingCourseBean.setItm_content_eff_end_datetime(rs.getTimestamp("content_eff_end_datetime"));	
					}
					
					pendingCourseBean.setItm_eff_start_datetime(rs.getTimestamp("itm_eff_start_datetime"));	// 离线课程(或混合课程)的开始时间
					pendingCourseBean.setItm_eff_end_datetime(rs.getTimestamp("itm_eff_end_datetime"));		// 离线课程(或混合课程)的结束时间
					pendingCourseBean.setApp_create_timestamp(rs.getTimestamp("app_create_timestamp"));	// 申请日期(即报名日期)
					
					//last approver
					HashMap map = this.getLastApproverInfo(con, usr_ent_id, pendingCourseBean.getApp_id());
					if (map.get("aal_action_timestamp") != null) {
						pendingCourseBean.setAal_action_timestamp((Timestamp)map.get("aal_action_timestamp"));
						pendingCourseBean.setLast_approval_date((Timestamp)map.get("aal_action_timestamp"));
					} else {
						pendingCourseBean.setAal_action_timestamp(rs.getTimestamp("app_upd_timestamp"));	
					}
					pendingCourseBean.setUsr_display_bil((String)map.get("usr_display_bil"));
					
					// 下一(批)审批者(注：该字段只能取别名)
					pendingCourseBean.setNext_approver(this.getWaitingApproverNames(con, usr_ent_id, pendingCourseBean.getApp_id()));
					
					boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
					boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
					boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
					boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
					boolean itm_run_ind = rs.getBoolean("itm_run_ind");
					boolean itm_integrated_ind = rs.getBoolean("itm_integrated_ind");
					
					// 设置以下属性以用于前台判断该课程是什么类型的课程(网上课程，面授课程，混合课程)
					pendingCourseBean.setItm_blend_ind(itm_blend_ind);
					pendingCourseBean.setItm_exam_ind(itm_exam_ind);
					pendingCourseBean.setItm_create_run_ind(itm_create_run_ind);
					pendingCourseBean.setItm_run_ind(itm_run_ind);
					pendingCourseBean.setItm_ref_ind(itm_ref_ind);
					pendingCourseBean.setItm_integrated_ind(itm_integrated_ind);
					
					String itmDummyType = aeItemDummyType.getDummyItemType(pendingCourseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
					pendingCourseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, itmDummyType));
					pendingCourseBean.setItm_dummy_type(itmDummyType);
					
//					pendingCourseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, aeItemDummyType.getDummyItemType(pendingCourseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind)));
					
					anyTypeCourseVector.add(pendingCourseBean);
				} else {	// 若是已审批的课程
					ApprovalCourseBean approvalCourseBean = new ApprovalCourseBean();
					
					approvalCourseBean.setApp_id(rs.getLong("app_id"));				// 报名记录表aeApplication的app_id
					approvalCourseBean.setApp_itm_id(rs.getLong("app_itm_id")); 	// 对应学员报名记录表aeApplication的app_itm_id
					approvalCourseBean.setItm_id(rs.getLong("itm_id"));				// 课程ID
					approvalCourseBean.setApp_status(rs.getString("app_status"));	// 审批状态(即报名状态)
					approvalCourseBean.setItm_type(rs.getString("itm_type"));		// 课程类型
					approvalCourseBean.setItm_title(rs.getString("itm_title"));		// 课程标题
					
					String itmIcon = rs.getString("itm_icon");
					String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("itm_id"), itmIcon);
					approvalCourseBean.setItm_icon(itmIconPath);
					
					approvalCourseBean.setItm_content_eff_start_datetime(rs.getTimestamp("content_eff_start_datetime"));// 混合课程的网上内容(或网上课程)开始时间
					
					// 混合课程的网上内容(或网上课程)结束时间
					if (rs.getTimestamp("itm_content_eff_end_datetime") == null && rs.getInt("itm_content_eff_duration") == 0) {// 若网上课程结束日期设定为不限
						approvalCourseBean.setItm_content_eff_end_datetime(null);
					} else {
						approvalCourseBean.setItm_content_eff_end_datetime(rs.getTimestamp("content_eff_end_datetime"));	
					}
					
					approvalCourseBean.setItm_eff_start_datetime(rs.getTimestamp("itm_eff_start_datetime"));	// 离线课程(或混合课程)的开始时间
					approvalCourseBean.setItm_eff_end_datetime(rs.getTimestamp("itm_eff_end_datetime"));		// 离线课程(或混合课程)的结束时间
					approvalCourseBean.setApp_upd_timestamp(rs.getTimestamp("app_upd_timestamp"));	// 报名被成功录取的时间
					approvalCourseBean.setAts_type(rs.getString("ats_type"));						// 该门课程的学习状态(状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程)
					approvalCourseBean.setCov_total_time(Utilities.parseSecond2Time(rs.getDouble("cov_total_time")));			// 总计学习时长
					approvalCourseBean.setCov_score(rs.getDouble("cov_score"));						// 分数
					approvalCourseBean.setCov_last_acc_datetime(rs.getTimestamp("cov_last_acc_datetime"));// 学员上次访问课程内容时间
					approvalCourseBean.setAtt_timestamp(rs.getTimestamp("att_timestamp"));			// 考勤日期
					approvalCourseBean.setCov_progress(rs.getDouble("cov_progress"));				// 进度
					approvalCourseBean.setCov_tkh_id(rs.getLong("cov_tkh_id"));
					approvalCourseBean.setCos_res_id(rs.getLong("cos_res_id"));
					
					boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
					boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
					boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
					boolean itm_integrated_ind = rs.getBoolean("itm_integrated_ind");
					
					String itmDummyType = aeItemDummyType.getDummyItemType(approvalCourseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
					approvalCourseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, itmDummyType));
					approvalCourseBean.setItm_dummy_type(itmDummyType);
					
//					approvalCourseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, aeItemDummyType.getDummyItemType(approvalCourseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind)));
					
					boolean itm_run_ind = rs.getBoolean("itm_run_ind");
					boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
					Timestamp itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
					Timestamp itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
					Timestamp att_create_timestamp = rs.getTimestamp("att_create_timestamp");
					int itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
					
					// 设置以下属性以用于前台判断该课程是什么类型的课程(网上课程，面授课程，混合课程)
					approvalCourseBean.setItm_blend_ind(itm_blend_ind);
					approvalCourseBean.setItm_create_run_ind(itm_create_run_ind);
					approvalCourseBean.setItm_run_ind(itm_run_ind);
					approvalCourseBean.setItm_ref_ind(itm_ref_ind);
					approvalCourseBean.setItm_integrated_ind(itm_integrated_ind);
					
					// 判断是否是已结束课程
					if (aeApplication.ADMITTED.equals(approvalCourseBean.getApp_status())) {
						if (isCosOfHistory(con, itm_blend_ind, 
							itm_run_ind, itm_create_run_ind, itm_ref_ind, 
							itm_eff_end_datetime, itm_content_eff_end_datetime, 
							att_create_timestamp, itm_content_eff_duration,approvalCourseBean.getAts_type())) {
							
							approvalCourseBean.setContent_status("end");
						}
						// 若是'未完成'、'已放弃'课程，不论课程结束时间是否已到期，也视为已结束课程
						if (aeAttendanceStatus.STATUS_TYPE_INCOMPLETE.equalsIgnoreCase(approvalCourseBean.getAts_type())
							|| aeAttendanceStatus.STATUS_TYPE_WITHDRAWN.equalsIgnoreCase(approvalCourseBean.getAts_type())) {
							approvalCourseBean.setContent_status("end");
						}
					}
					approvalCourseBean.setComp_criteria(con, approvalCourseBean.getCov_tkh_id(), approvalCourseBean.getCos_res_id());
						
					if ("get_my_hist_cos".equalsIgnoreCase(studyModuleParam.getCmd()) && "end".equalsIgnoreCase(approvalCourseBean.getContent_status())) {	// 若查询已结束课程(当课程已完成而未到结束日. 它都应该在"学习中"而不是已结束) 
						anyTypeCourseVector.add(approvalCourseBean);
					} else if ("get_all_my_cos".equalsIgnoreCase(studyModuleParam.getCmd())) {
						anyTypeCourseVector.add(approvalCourseBean);
					} else if ("srh_my_cos".equalsIgnoreCase(studyModuleParam.getCmd()) && "ALL".equalsIgnoreCase(studyModuleParam.getSrh_range())) {
						anyTypeCourseVector.add(approvalCourseBean);
					} else if ("srh_my_cos".equalsIgnoreCase(studyModuleParam.getCmd()) && "HIS".equalsIgnoreCase(studyModuleParam.getSrh_range()) && "end".equalsIgnoreCase(approvalCourseBean.getContent_status())) {
						anyTypeCourseVector.add(approvalCourseBean);
					}
				}
			} // end while
			
			int total_size = anyTypeCourseVector.size();
			studyModuleParam.setTotal_rec(total_size);	// 设置总记录数
			
			if (total_size > 0) {
				int start = studyModuleParam.getStart();
				int end = (studyModuleParam.getStart() + studyModuleParam.getLimit() - 1);// 每页所显示记录的最大序号
				
				for (int i = 0; i < total_size; i++) {
					if (i >= start && i <= end) {
						resultVec.add(anyTypeCourseVector.get(i));
					}
				}
			}
			
		} catch(SQLException se) {
			System.err.println("[Get_Any_Type_Courses_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return resultVec;
	}
	
	/**
	 * 判断是否是已结束课程(或考试)
	 * @param con 数据库连接器
	 * @param itm_blend_ind 是否是混合
	 * @param itm_run_ind 是否是班级
	 * @param itm_create_run_ind 是否可以开设班级
	 * @param itm_ref_ind 是否是参考
	 * @param itm_eff_end_datetime 离线课程(或混合课程)对应班级的结束时间
	 * @param itm_content_eff_end_datetime 网上课程(或混合课程)的网上内容结束时间
	 * @param att_create_timestamp 报名成功录取时间
	 * @param itm_content_eff_duration 网上课程(或混合课程)的网上内容期限
	 * @return 用户报读的该门课程(或考试)是否结束
	 * @throws SQLException
	 */
	public boolean isCosOfHistory(Connection con, boolean itm_blend_ind,
			boolean itm_run_ind, boolean itm_create_run_ind, boolean itm_ref_ind,
			Timestamp itm_eff_end_datetime, Timestamp itm_content_eff_end_datetime, 
			Timestamp att_create_timestamp, int itm_content_eff_duration, String ats_type) throws SQLException {
		
		Timestamp cur_time = cwSQL.getTime(con);
		
		boolean flag = false;
		
		/*
		  "     ( " 
		+ " 		(citm.itm_blend_ind = 1 and (" + getDateStr + " > citm.itm_eff_end_datetime or " + getDateStr + " > citm.itm_content_eff_end_datetime)) "	// 混合课程(考试)的处理
		+ "			or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 0 and citm.itm_create_run_ind = 0 and citm.itm_ref_ind = 0"	// 网上课程(考试)的处理
		+ "				and ( "
		+ "						not((citm.itm_content_eff_duration is null or citm.itm_content_eff_duration = 0) and citm.itm_content_eff_end_datetime is null) "
	    + "   				 	and (" + getDateStr + ">citm.itm_content_eff_end_datetime "
	    + "			       	 		 or (citm.itm_content_eff_duration is not null and citm.itm_content_eff_duration <> 0 and " + getDateStr + " > "+ cwSQL.dateadd(con, "attend.att_create_timestamp", "citm.itm_content_eff_duration") +") "
	    + "						) "
	    + "			    ) "
	    + " 		) "
	    + "			or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 and (" + getDateStr + " > citm.itm_eff_end_datetime)) "	// 面授课程(考试)的处理
	    + "		) ";
		*/
		
		// 混合课程(考试)的处理
		if (itm_blend_ind == true
			&& (cur_time.after(itm_eff_end_datetime) && cur_time.after(itm_content_eff_end_datetime))) {
			return true;
		}
		
		// 网上课程(考试)的处理
		if((itm_blend_ind == false && itm_run_ind == false && itm_create_run_ind == false && itm_ref_ind == false)
			&& (
			        ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_ATTEND)
			        || ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_INCOMPLETE)
			        || ats_type.equalsIgnoreCase(aeAttendanceStatus.STATUS_TYPE_WITHDRAWN)
/*					!(itm_content_eff_duration == 0 && itm_content_eff_end_datetime == null)
					&& ((itm_content_eff_end_datetime != null && cur_time.after(itm_content_eff_end_datetime))
						|| (itm_content_eff_duration > 0 && cur_time.after(aeUtils.getTimeAfter(att_create_timestamp, Calendar.DATE, itm_content_eff_duration)))
					)*/
			)
		) {
			return true;
		}
		
		// 面授课程(考试)的处理
		if (itm_blend_ind == false && itm_run_ind == false && itm_create_run_ind == true && itm_ref_ind == false && cur_time.after(itm_eff_end_datetime)) {
			return true;
		}
		
		return flag;
	}
	
	
	private HashMap getLastApproverInfo(Connection con, long usr_ent_id, long app_id) throws SQLException {
		String sql = " select aal_action_timestamp, usr_display_bil "
			 + " from aeAppnApprovalList appr, RegUser "
			 + " where appr.aal_app_id = ? " 
			 + " 	   and appr.aal_action_timestamp=( "
			 + "				select max(aal_action_timestamp) "
			 + " 				from aeAppnApprovalList "
			 + "				where aal_app_id = ? "
			 + "			   		  and aal_status = ? "
			 + "	   ) "
			 + " 	   and appr.aal_status = ? " 
			 + "       and appr.aal_action_taker_usr_ent_id = usr_ent_id ";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HashMap map = new HashMap();
		try {
			pstmt = con.prepareStatement(sql);
			int index = 1;
			pstmt.setLong(index++, app_id);
			pstmt.setLong(index++, app_id);
			pstmt.setString(index++, DbAppnApprovalList.STATUS_HISTORY);
			pstmt.setString(index++, DbAppnApprovalList.STATUS_HISTORY);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				map.put("aal_action_timestamp", rs.getTimestamp("aal_action_timestamp"));
				map.put("usr_display_bil", rs.getString("usr_display_bil"));
			}
		} finally {
			if (pstmt != null) pstmt.close();
		}
		return map;
	}
	/**
	 * 获取与学员报名某门课程相关的下一(批)审批者信息
	 * @param con 数据库连接器
	 * @param usr_ent_id 学员的usr_ent_id
	 * @param app_id 报名学员表aeApplication的主键app_id
	 * @return 下一(批)审批者昵称
	 * @throws SQLException
	 */
	private List getWaitingApproverNames(Connection con, long usr_ent_id, long app_id)
		throws SQLException {
		
		// 查询与学员相关的待审批者SQL
		String sql = "select apprList.aal_usr_ent_id approver_id, rUser.usr_display_bil approver_name "	// 待审批者ID，待审批者昵称
			+ " from aeAppnApprovalList apprList "
			+ " inner join RegUser rUser on apprList.aal_usr_ent_id=rUser.usr_ent_id "
			+ " where apprList.aal_status='PENDING' "
			+ " 	and apprList.aal_app_ent_id=? "	// 传入：学员的usr_ent_id
			+ " 	and apprList.aal_app_id=?";		// 传入：报名学员表aeApplication的主键app_id
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List approverNameList = null;
		try {
			pstmt = con.prepareStatement(sql);
			int index = 1;
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, app_id);
			
			approverNameList = new ArrayList();
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String waitingApproverName = rs.getString("approver_name");
				
				HashMap approverMap = new HashMap();
				approverMap.put("name", waitingApproverName);
				
				approverNameList.add(approverMap);
			}
		} catch(SQLException se) {
			System.err.println("[Get_Waiting_Approver_Names_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		
		return approverNameList;
	}
	
	/**
	 * 获取与学员相关的任意课程信息SQL
	 * 课程类型：可以是与学员相关的待审批课程、或与学员相关的审批通过的课程、或以上两种情况的课程
	 * 与学员相关的课程的学习状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程中的一种情形和多种情形
	 * @param con 数据库连接器
	 * @param appStatus 课程标识(可以取值Study.GET_ALL_MY_COURSES、Study.GET_MY_PENDING_COURSES、Study.GET_APPROVE_COURSES)
	 * @param atsTypeList 与学员相关的课程学习状态选项(若为空，则默认检索出与该用户相关的所有学习状态下的课程)
	 * @param appendCondition 附加的查询条件(若为空，则没有附加的查询条件)
	 * @return 与学员相关的课程集合SQL
	 * @throws SQLException
	 */
	private String getAnyTypeCoursesSql(Connection con, int appStatus, List atsTypeList, String appendCondition, BaseParam param)
		throws SQLException {
		
		StudyModuleParam studyModuleParam = (StudyModuleParam)param;
		
		// 查询与用户相关的课程(状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程)SQL
		String sql = "select appn.app_id, appn.app_itm_id, "	// 报名学员的app_id、报读课程(或班级)的app_itm_id
			+ cwSQL.replaceNull("citm2.itm_id", "citm.itm_id") + " itm_id, "	// 课程ID
			+ cwSQL.replaceNull("citm2.itm_title", "citm.itm_title") + " itm_title, citm.itm_type, "// 课程名，课程类型
			+ cwSQL.replaceNull("citm2.itm_icon", "citm.itm_icon") + " itm_icon, "	// 课程图标
//			+ " citm.itm_exam_ind, citm.itm_blend_ind, citm.itm_ref_ind, citm.itm_run_ind, citm.itm_create_run_ind, "	// 是否是考试、是否是混合、是否是参考、是否是班级、是否可以开设班级
			+ cwSQL.replaceNull("citm2.itm_integrated_ind", "citm.itm_integrated_ind") + " itm_integrated_ind, "		// 是否是集成培训
			+ cwSQL.replaceNull("citm2.itm_exam_ind", "citm.itm_exam_ind") + " itm_exam_ind, "		// 是否是考试
			+ cwSQL.replaceNull("citm2.itm_blend_ind", "citm.itm_blend_ind") + " itm_blend_ind, "	// 是否是混合
			+ cwSQL.replaceNull("citm2.itm_ref_ind", "citm.itm_ref_ind") + " itm_ref_ind, "			// 是否是参考
			+ cwSQL.replaceNull("citm2.itm_run_ind", "citm.itm_run_ind") + " itm_run_ind, "			// 是否是班级
			+ cwSQL.replaceNull("citm2.itm_create_run_ind", "citm.itm_create_run_ind") + " itm_create_run_ind, "// 是否可以开设班级
			// 混合课程的网上内容(或网上课程)开始时间(注：网上课程开始时间若为空，则以报名成功日期代替(前提：aeApplication表中的app_status字段值为'Admitted')，则可以将app_upd_timestamp视为学员报名日期)
			+ " case when appn.app_status='Admitted' then " + cwSQL.replaceNull("citm.itm_content_eff_start_datetime", "appn.app_upd_timestamp")
			+ " 	 when appn.app_status='Pending' or appn.app_status='Waiting' then citm.itm_content_eff_start_datetime "
			+ " 	 else null"
			+ " end	content_eff_start_datetime, "
			// 混合课程的网上内容(或网上课程)结束时间(注：网上课程结束时间若为空，则以报名成功日期+网上课程有效天数)
			+ " case when appn.app_status='Admitted' then " + cwSQL.replaceNull("citm.itm_content_eff_end_datetime", cwSQL.dateadd("appn.app_upd_timestamp", "citm.itm_content_eff_duration"))
			+ " 	 when appn.app_status='Pending' or appn.app_status='Waiting' then citm.itm_content_eff_end_datetime "
			+ " 	 else null "
			+ " end content_eff_end_datetime, "
			+ " citm.itm_content_eff_end_datetime, citm.itm_content_eff_duration, "	// 该两字段组合起来，用于识别混合课程的网上内容(或网上课程)的结束日期是否是无限
			+ " citm.itm_eff_start_datetime, citm.itm_eff_end_datetime, "	// 离线课程(或混合课程)对应班级的开始时间、结束时间
			+ " appn.app_status, appn.app_create_timestamp, appn.app_upd_timestamp,"				// 审批状态，申请日期(即报名日期)
			+ " attend.att_create_timestamp, "								// 报名成功日期
			// 报名被成功录取的日期
			+ " case when appn.app_status='Admitted' then  appn.app_upd_timestamp "
			+ " 	 else null "
			+ " end app_upd_timestamp, "
			// 该门课程的学习状态(状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程)
			+ " attStatus.ats_type, "
			+ " courseEval.cov_total_time, courseEval.cov_score, "	// 总计学习时长，分数
			+ " cov_last_acc_datetime, attend.att_timestamp, "		// 学员上次访问课程内容时间，考勤日期
			// 进度
			+ " case when appn.app_status='Admitted' then courseEval.cov_progress "
			+ " 	 else null "
			+ " end cov_progress, "
			+ " courseEval.cov_tkh_id, "
			+ " cour.cos_res_id "
			+ " from aeApplication appn "
			+ " inner join aeItem citm on (itm_status='ON' and appn.app_itm_id=citm.itm_id) "
			// 加入考勤表
			+ " left join aeAttendance attend on appn.app_id=attend.att_app_id "
			// 加入考勤状态标识表
			+ " left join aeAttendanceStatus attStatus on attend.att_ats_id=attStatus.ats_id "
			// 加入课程内容表
			+ " left join Course cour on citm.itm_id=cour.cos_itm_id "
			// 加入学员学习课程内容的评估表
			+ " left join CourseEvaluation courseEval on ( "
			+ " 	appn.app_ent_id=courseEval.cov_ent_id "
			+ " 	and cour.cos_res_id=courseEval.cov_cos_id "
			+ " 	and appn.app_tkh_id=courseEval.cov_tkh_id "
			+ " ) "
			// 加入班级与离线课程的关系表，用以识别假如学员报读的是班级的离线课程名称
			+ " left join aeItemRelation citmR on citm.itm_id=citmR.ire_child_itm_id "
			+ " left join aeItem citm2 on ( "
			+ " 	citm2.itm_create_run_ind = 1 "
			+ " 	and citmR.ire_parent_itm_id=citm2.itm_id "
			+ " ) "
			// 加入报名成功记录表(该表记录学员报名成功时的一些跟踪信息)
			+ " left join Trackinghistory track on appn.app_tkh_id=track.tkh_id "
			+ " where app_ent_id=? "	// 传入：学员的usr_ent_id";
			+ " and (citm.itm_status='ON' and (citm2.itm_status='ON' or citm2.itm_status is null)) ";	// 处理离线课程(或离线考试)未发布，对应的班级(或考批次)已发布的情况，此种情况学员是看不到的
		
		if ("COS".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_COS, "citm", true);
		} else if ("EXAM".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM, "citm", true);
		} else if ("INTEGRATED".equalsIgnoreCase(studyModuleParam.getType())) {
			sql += aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED, "citm", true);
		}
		
		// 加入对与学员相关课程类型的判断(类型：所有相关的课程、或待审批的课程、或已审批的课程)
		if (appStatus == Study.GET_ALL_MY_COURSES) {			// 所有相关的课程
			sql += " and app_status in ('Pending', 'Waiting', 'Admitted') ";;
		} else if (appStatus == Study.GET_MY_PENDING_COURSES) {	// 待审批的课程
			sql += " and (app_status='Pending' or app_status='Waiting') ";
		} else if (appStatus == Study.GET_APPROVAL_COURSES) {	// 已审批的课程
			sql += " and app_status='Admitted' ";
		}
		
		// 加入与学员相关课程的学习状态的判断(状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程)
		// atsTypeList若为空，则默认检索出与该用户相关的所有学习状态下的课程
		// 以下条件形如" and (... or ...) "
		int optionSize = (atsTypeList != null) ? atsTypeList.size() : 0;
		if (atsTypeList != null && optionSize > 0) {
			sql += " and ( ";
			
			int optionCount = 1;
			for (Iterator iter = atsTypeList.iterator(); iter.hasNext();) {
				String optionStr = (String)iter.next();
				
				// 加入第count个选项
				if (optionStr.equalsIgnoreCase("NULL")) {
					sql += " attStatus.ats_type is null ";
				} else {
					sql += " attStatus.ats_type='" + optionStr + "' ";
				}
				
				optionCount++;
				if (optionCount <= optionSize) {
					sql += " or ";
				}
				
			} // end for
			
			sql += " ) ";
		} // end if
		
//		if ("get_my_hist_cos".equalsIgnoreCase(studyModuleParam.getCmd())) {	// 若查询已结束课程(当课程已完成而未到结束日. 它都应该在"学习中"而不是已结束) 
//			String getDateStr = getDate(con);
//			
//			sql +=
//		  "     ( " 
//		+ " 		(citm.itm_blend_ind = 1 and (" + getDateStr + " > citm.itm_eff_end_datetime or " + getDateStr + " > citm.itm_content_eff_end_datetime)) "	// 混合课程(考试)的处理
//		+ "			or (citm.itm_blend_ind=0 and citm.itm_run_ind=0 and citm.itm_create_run_ind = 0 and citm.itm_ref_ind = 0"	// 网上课程(考试)的处理
//		+ "				and ( "
//		+ "						not((citm.itm_content_eff_duration is null or citm.itm_content_eff_duration = 0) and citm.itm_content_eff_end_datetime is null) "
//	    + "   				 	and (" + getDateStr + ">citm.itm_content_eff_end_datetime "
//	    + "			       	 		 or (citm.itm_content_eff_duration is not null and citm.itm_content_eff_duration <> 0 and " + getDateStr + " > "+ cwSQL.dateadd(con, "attend.att_create_timestamp", "citm.itm_content_eff_duration") +") "
//	    + "						) "
//	    + "			    ) "
//	    + " 		) "
//	    + "			or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 and (" + getDateStr + ">citm.itm_eff_end_datetime)) "	// 面授课程(考试)的处理
//	    + "		) ";
//		}
		
		// 加入附加的查询条件
		if (appendCondition != null) {
			sql += appendCondition;
		}
		
		return sql;
	}
	
	
}
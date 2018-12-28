<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_rpt.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="rpt_type" select="/report/report_body/template/@type"/>
	<xsl:variable name="ent_id" select="/report/meta/cur_usr/@ent_id"/>
	<xsl:variable name="template_id" select="/report/report_body/template/@id"/>
	<xsl:variable name="rpt_name" select="/report/report_body/spec/title"/>
	<xsl:variable name="rsp_id" select="/report/report_body/spec/@spec_id"/>
	<xsl:variable name="role_list" select="/report/report_body/meta/role_list"/>
	<xsl:variable name="attendance_status_list" select="/report/report_body/meta/attendance_status_list"/>
	<xsl:variable name="student_id" select="report/report_body/student/@ent_id"/>
	<xsl:variable name="num" select="count(report/report_body/report_list/record/student[@ent_id=$student_id])"/>
	<xsl:variable name="total" select="count(report/report_body/report_list/record/student)"/>
	<xsl:variable name="cur_page" select="/report/report_body/report_list/pagination/@cur_page"/>
	<xsl:variable name="page_size"  select="/report/report_body/report_list/pagination/@page_size"/>
	<xsl:variable name="total_rec" select="/report/report_body/report_list/pagination/@total_rec"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	
	<xsl:variable name="lab_score" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_score')" />
	
	<xsl:variable name="cur_rec">
		<xsl:choose>
			<xsl:when test="$total_rec > 20"><xsl:value-of select="$page_size"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$total_rec"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cur_sort_col" select="/report/report_body/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/report/report_body/pagination/@sort_order"/>
	<xsl:variable name="page_timestamp" select="/report/report_body/pagination/@timestamp"/>
	<xsl:variable name="itm_type_lst" select="/report/report_body/meta/item_type_list"/>
	<xsl:variable name="col_size">
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/atm_stat_list/atm_stat) > 0">
				<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'content_lst'])"/>
			</xsl:when>
			<xsl:otherwise>2</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order = 'asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="rpt_xls" select="/report/report_body/template/xsl_list/xsl[@type = 'download']"/>
	<!-- =============================================================== -->
	<xsl:variable name="isStudent">
		<xsl:choose>
			<xsl:when test="report/report_body/student">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/report">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mgt_rpt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var mgt_rpt = new wbManagementReport;
			unit = 0;
			
			lrn_soln = new wbLearnSolution;
			
			function sortCol(colName, order){
				wb_utils_nav_get_urlparam('sort_col',colName,'sort_order',order,'timestamp','');
			}
		
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="rsp_id" value=""/>
				<input type="hidden" name="spec_name" value=""/>
				<input type="hidden" name="spec_value" value=""/>
				<input type="hidden" name="download" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="rte_id" value=""/>
				<input type="hidden" name="usr_ent_id" value=""/>
				<input type="hidden" name="rpt_type" value=""/>
				<input type="hidden" name="rpt_type_lst" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_mgmt_rpt">學員學習報告</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">報告詳情</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_leaner">學員</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_cos_title">課程</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">註冊日期</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">沒有記錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">詳情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
			<!-- -->
			<xsl:with-param name="lab_lrn_mod">測驗模塊</xsl:with-param>
			<!--    =====================-->
			<xsl:with-param name="lab_group_by">分組統計</xsl:with-param>
			<xsl:with-param name="lab_group_by_Q">題目</xsl:with-param>
			<xsl:with-param name="lab_group_by_RF">資源檔案夾</xsl:with-param>
			<xsl:with-param name="lab_group_by_QT">題目類型</xsl:with-param>
			<xsl:with-param name="lab_res_folder">資源檔案夾(編號)</xsl:with-param>
			<xsl:with-param name="lab_qu_type">題目類型</xsl:with-param>
			<xsl:with-param name="lab_dif">難度</xsl:with-param>
			<xsl:with-param name="lab_attempts">嘗試次數</xsl:with-param>
			<xsl:with-param name="lab_correct">答對次數</xsl:with-param>
			<xsl:with-param name="lab_incorrect">答錯次數</xsl:with-param>
			<xsl:with-param name="lab_partial_correct">未完全答對次數</xsl:with-param>
			<xsl:with-param name="lab_n_graded">尚未評分次數</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均分數</xsl:with-param>
			<xsl:with-param name="lab_per_att">全部答對次數</xsl:with-param>
			<xsl:with-param name="lab_que">題目數</xsl:with-param>
			<xsl:with-param name="lab_leaners">學員人數</xsl:with-param>
			<xsl:with-param name="lab_num_attempts">個別嘗試</xsl:with-param>
			<xsl:with-param name="lab_all_attemptts">所有嘗試</xsl:with-param>
			<xsl:with-param name="lab_sub_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_attempt_type">嘗試類別</xsl:with-param>
			<xsl:with-param name="lab_que_title">題目標題(編號)</xsl:with-param>
			<xsl:with-param name="lab_attempt">嘗試 #</xsl:with-param>
			<xsl:with-param name="lab_all_attempt">所有嘗試</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_no_user_attempt">沒有嘗試</xsl:with-param>
			<xsl:with-param name="lab_showing">本頁顯示 1 - <xsl:value-of select="$cur_rec"/> 個，共找到 <xsl:value-of select="$total_rec"/> 個</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我負責的學員</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">報讀我負責課程的學員</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_mgmt_rpt">学员学习报告</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">报告详情</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_leaner">学员</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有学员</xsl:with-param>
			<xsl:with-param name="lab_cos_title">课程</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to"> 至 </xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">录取日期</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有记录</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">详情</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">测验模块</xsl:with-param>
			<!--    =====================-->
			<xsl:with-param name="lab_group_by">分组统计</xsl:with-param>
			<xsl:with-param name="lab_group_by_Q">题目</xsl:with-param>
			<xsl:with-param name="lab_group_by_RF">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_group_by_QT">题目类型</xsl:with-param>
			<xsl:with-param name="lab_res_folder">资源文件夹(编号)</xsl:with-param>
			<xsl:with-param name="lab_qu_type">题目类型</xsl:with-param>
			<xsl:with-param name="lab_dif">难度</xsl:with-param>
			<xsl:with-param name="lab_attempts">尝试次数</xsl:with-param>
			<xsl:with-param name="lab_correct">答对次数</xsl:with-param>
			<xsl:with-param name="lab_incorrect">答错次数</xsl:with-param>
			<xsl:with-param name="lab_partial_correct">未完全答对次数</xsl:with-param>
			<xsl:with-param name="lab_n_graded">尚未评分次数</xsl:with-param>
			<xsl:with-param name="lab_avg_score">平均分数</xsl:with-param>
			<xsl:with-param name="lab_per_att">全部答对次数</xsl:with-param>
			<xsl:with-param name="lab_que">题目数</xsl:with-param>
			<xsl:with-param name="lab_leaners">学员人数</xsl:with-param>
			<xsl:with-param name="lab_num_attempts">个别尝试</xsl:with-param>
			<xsl:with-param name="lab_all_attemptts">所有尝试</xsl:with-param>
			<xsl:with-param name="lab_sub_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_attempt_type">尝试类别</xsl:with-param>
			<xsl:with-param name="lab_que_title">题目标题(编号)</xsl:with-param>
			<xsl:with-param name="lab_attempt">尝试 #</xsl:with-param>
			<xsl:with-param name="lab_all_attempt">所有尝试</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_showing">本页显示第 1 - <xsl:value-of select="$cur_rec"/> 个，共找到 <xsl:value-of select="$total_rec"/> 个</xsl:with-param>
			<xsl:with-param name="lab_no_user_attempt">没有尝试</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">我负责的学员</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">报读我负责课程的学员</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_mgmt_rpt">Management report</xsl:with-param>
			<xsl:with-param name="lab_rpt_details">Report details</xsl:with-param>
			<!-- search criteria text -->
			<xsl:with-param name="lab_lrn_group">Group</xsl:with-param>
			<xsl:with-param name="lab_leaner">Learner</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">All learners</xsl:with-param>
			<xsl:with-param name="lab_cos_title">Course</xsl:with-param>
			<xsl:with-param name="lab_from">From </xsl:with-param>
			<xsl:with-param name="lab_to"> To </xsl:with-param>
			<xsl:with-param name="lab_att_create_timestamp">Enrollment date</xsl:with-param>
			<!-- content result text -->
			<xsl:with-param name="lab_no_item">No record found</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<!-- button text -->
			<xsl:with-param name="lab_g_form_btn_detail">Detail</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
			<xsl:with-param name="lab_lrn_mod">Test module</xsl:with-param>
			<!--    =====================-->
			<xsl:with-param name="lab_group_by">Group by</xsl:with-param>
			<xsl:with-param name="lab_group_by_Q">Question</xsl:with-param>
			<xsl:with-param name="lab_group_by_RF">Resource folder</xsl:with-param>
			<xsl:with-param name="lab_group_by_QT">Question type</xsl:with-param>
			<xsl:with-param name="lab_res_folder">Resource folder (ID)</xsl:with-param>
			<xsl:with-param name="lab_qu_type">Question type</xsl:with-param>
			<xsl:with-param name="lab_dif">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_attempts">Attempts</xsl:with-param>
			<xsl:with-param name="lab_correct">Correct</xsl:with-param>
			<xsl:with-param name="lab_incorrect">Incorrect</xsl:with-param>
			<xsl:with-param name="lab_partial_correct">Partial correct</xsl:with-param>
			<xsl:with-param name="lab_n_graded">Not graded</xsl:with-param>
			<xsl:with-param name="lab_avg_score">Average score</xsl:with-param>
			<xsl:with-param name="lab_per_att">Perfect attempts</xsl:with-param>
			<xsl:with-param name="lab_que">Questions</xsl:with-param>
			<xsl:with-param name="lab_leaners">Learners</xsl:with-param>
			<xsl:with-param name="lab_num_attempts">Numbered attempts</xsl:with-param>
			<xsl:with-param name="lab_all_attemptts">All attempts</xsl:with-param>
			<xsl:with-param name="lab_sub_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_attempt_type">Attempt type</xsl:with-param>
			<xsl:with-param name="lab_que_title">Question title (ID)</xsl:with-param>
			<xsl:with-param name="lab_attempt">Attempt #</xsl:with-param>
			<xsl:with-param name="lab_all_attempt">All attempts</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_showing">Showing 1 - <xsl:value-of select="$cur_rec"/> of <xsl:value-of select="$total_rec"/></xsl:with-param>
			<xsl:with-param name="lab_no_user_attempt">No attempts</xsl:with-param>
			<xsl:with-param name="lab_answer_for_lrn">My responsible learners</xsl:with-param>
			<xsl:with-param name="lab_answer_for_course_lrn">Learners that have enrolled in my responsible courses</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_rpt_details"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_leaner"/>
		<xsl:param name="lab_leaners"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_my_direct_staff"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_cos_title"/>
		<xsl:param name="lab_att_status"/>
		<xsl:param name="lab_show_in_progress_attendance"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_not_specified"/>
		<xsl:param name="lab_item_content"/>
		<xsl:param name="lab_user_content"/>
		<xsl:param name="lab_others_content"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_att_remark"/>
		<xsl:param name="lab_app_ext_4"/>
		<xsl:param name="lab_cov_score"/>
		<xsl:param name="lab_att_rate"/>
		<xsl:param name="lab_cov_last_acc_datetime"/>
		<xsl:param name="lab_cov_commence_datetime"/>
		<xsl:param name="lab_total_attempt"/>
		<xsl:param name="lab_cov_total_time"/>
		<xsl:param name="lab_grand_total"/>
		<xsl:param name="lab_g_form_btn_detail"/>
		<xsl:param name="lab_g_form_btn_export"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_lrn_mod"/>
		<!--================================-->
		<xsl:param name="lab_group_by"/>
		<xsl:param name="lab_group_by_Q"/>
		<xsl:param name="lab_group_by_RF"/>
		<xsl:param name="lab_group_by_QT"/>
		<xsl:param name="lab_num_attempts"/>
		<xsl:param name="lab_all_attemptts"/>
		<xsl:param name="lab_sub_date"/>
		<xsl:param name="lab_attempt_type"/>
		<xsl:param name="lab_res_folder"/>
		<xsl:param name="lab_qu_type"/>
		<xsl:param name="lab_dif"/>
		<xsl:param name="lab_attempts"/>
		<xsl:param name="lab_correct"/>
		<xsl:param name="lab_incorrect"/>
		<xsl:param name="lab_partial_correct"/>
		<xsl:param name="lab_n_graded"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_per_att"/>
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_que_title"/>
		<xsl:param name="lab_attempt"/>
		<xsl:param name="lab_all_attempt"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_showing"/>
		<xsl:param name="lab_no_user_attempt"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$rpt_name != ''">
						<xsl:value-of select="$rpt_name"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_rte_title">
							<xsl:with-param name="rte_type" select="/report/report_body/template/@type"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(/report/report_body/report_list/atm_stat_list) = 0">
				<xsl:if test="report_body/spec/data_list/data[@name='ent_id'] or report_body/spec/data_list/data[@name='s_usg_ent_id_lst'] or report_body/spec/data_list/data[@name='itm_id']  or report_body/spec/data_list/data[@name='att_create_start_datetime'] or report_body/spec/data_list/data[@name='att_create_end_datetime'] or report_body/spec/data_list/data[@name='all_user_ind'] or report_body/spec/data_list/data[@name='mod_id'] or report_body/spec/data_list/data[@name='ats_id']">
					<xsl:call-template name="report_criteria">
						<xsl:with-param name="this_width">100%</xsl:with-param>
						<xsl:with-param name="lab_cos" select="$lab_cos"/>
						<xsl:with-param name="lab_cos_title" select="$lab_cos_title"/>
						<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
						<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
						<xsl:with-param name="lab_leaner" select="$lab_leaner"/>
						<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
						<xsl:with-param name="lab_my_direct_staff" select="$lab_my_direct_staff"/>
						<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
						<xsl:with-param name="lab_from" select="$lab_from"/>
						<xsl:with-param name="lab_na" select="$lab_na"/>
						<xsl:with-param name="lab_to" select="$lab_to"/>
						<xsl:with-param name="lab_all" select="$lab_all"/>
						<xsl:with-param name="lab_lrn_mod" select="$lab_lrn_mod"/>
						<xsl:with-param name="lab_group_by" select="$lab_group_by"/>
						<xsl:with-param name="lab_group_by_Q" select="$lab_group_by_Q"/>
						<xsl:with-param name="lab_group_by_RF" select="$lab_group_by_RF"/>
						<xsl:with-param name="lab_group_by_QT" select="$lab_group_by_QT"/>
						<xsl:with-param name="lab_num_attempts" select="$lab_num_attempts"/>
						<xsl:with-param name="lab_all_attemptts" select="$lab_all_attemptts"/>
						<xsl:with-param name="lab_sub_date" select="$lab_sub_date"/>
						<xsl:with-param name="lab_attempt_type" select="$lab_attempt_type"/>
						<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
						<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>	
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_item"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<!-- start draw table header -->
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td colspan="{$col_size + 3}">
							<xsl:if test="report_body/spec/data_list/data[@name='ent_id'] or report_body/spec/data_list/data[@name='s_usg_ent_id_lst'] or report_body/spec/data_list/data[@name='itm_id']  or report_body/spec/data_list/data[@name='att_create_start_datetime'] or report_body/spec/data_list/data[@name='att_create_end_datetime'] or report_body/spec/data_list/data[@name='all_user_ind'] or report_body/spec/data_list/data[@name='mod_id'] or report_body/spec/data_list/data[@name='ats_id']">
								<xsl:call-template name="report_criteria">
									<xsl:with-param name="this_width">100%</xsl:with-param>
									<xsl:with-param name="lab_cos" select="$lab_cos"/>
									<xsl:with-param name="lab_cos_catalog" select="$lab_cos_catalog"/>
									<xsl:with-param name="lab_cos_title" select="$lab_cos_title"/>
									<xsl:with-param name="lab_all_cos" select="$lab_all_cos"/>
									<xsl:with-param name="lab_lrn_group" select="$lab_lrn_group"/>
									<xsl:with-param name="lab_leaner" select="$lab_leaner"/>
									<xsl:with-param name="lab_by_all_user" select="$lab_by_all_user"/>
									<xsl:with-param name="lab_my_direct_staff" select="$lab_my_direct_staff"/>
									<xsl:with-param name="lab_att_create_timestamp" select="$lab_att_create_timestamp"/>
									<xsl:with-param name="lab_from" select="$lab_from"/>
									<xsl:with-param name="lab_na" select="$lab_na"/>
									<xsl:with-param name="lab_to" select="$lab_to"/>
									<xsl:with-param name="lab_all" select="$lab_all"/>
									<xsl:with-param name="lab_lrn_mod" select="$lab_lrn_mod"/>
									<xsl:with-param name="lab_group_by" select="$lab_group_by"/>
									<xsl:with-param name="lab_group_by_Q" select="$lab_group_by_Q"/>
									<xsl:with-param name="lab_group_by_RF" select="$lab_group_by_RF"/>
									<xsl:with-param name="lab_group_by_QT" select="$lab_group_by_QT"/>
									<xsl:with-param name="lab_num_attempts" select="$lab_num_attempts"/>
									<xsl:with-param name="lab_all_attemptts" select="$lab_all_attemptts"/>
									<xsl:with-param name="lab_sub_date" select="$lab_sub_date"/>
									<xsl:with-param name="lab_attempt_type" select="$lab_attempt_type"/>
									<xsl:with-param name="lab_answer_for_lrn" select="$lab_answer_for_lrn"/>
									<xsl:with-param name="lab_answer_for_course_lrn" select="$lab_answer_for_course_lrn"/>
								</xsl:call-template>
							</xsl:if>
						</td>
					</tr>
				</table>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
					<tr class="SecBg wzb-ui-table-head">
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<!--
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'usr_content_lst']" mode="title"/>
-->
						<xsl:choose>
							<xsl:when test="report_body/spec/data_list/data[@name = 'group_by']/@value = 'QUE'">
								<td nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_que_title"/>
									</span>
								</td>
								<xsl:if test="report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_fdr'">
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="$lab_res_folder"/>
										</span>
									</td>
								</xsl:if>
								<xsl:if test="report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_type'">
									<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<xsl:value-of select="$lab_qu_type"/>
										</span>
									</td>
								</xsl:if>
								
								<td nowrap="nowrap" align="center">
									<span class="SmallText">
										<xsl:value-of select="$lab_score"/>
									</span>
								</td>
								
								<xsl:if test="report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_diff'">
									<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<xsl:value-of select="$lab_dif"/>
										</span>
									</td>
								</xsl:if>
							</xsl:when>
							<xsl:when test="report_body/spec/data_list/data[@name = 'group_by']/@value = 'RES_FDR'">
								<td nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="$lab_res_folder"/>
									</span>
								</td>
							</xsl:when>
							<xsl:when test="report_body/spec/data_list/data[@name = 'group_by']/@value = 'QUE_TYPE'">
								<td nowrap="nowrap" align="center">
									<span class="SmallText">
										<xsl:value-of select="$lab_qu_type"/>
									</span>
								</td>
							</xsl:when>
						</xsl:choose>
						<td nowrap="nowrap">
							<span class="SmallText">
								<xsl:value-of select="$lab_attempt_type"/>
							</span>
						</td>
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="title">
							<xsl:with-param name="lab_attempts" select="$lab_attempts"/>
							<xsl:with-param name="lab_correct" select="$lab_correct"/>
							<xsl:with-param name="lab_incorrect" select="$lab_incorrect"/>
							<xsl:with-param name="lab_partial_correct" select="$lab_partial_correct"/>
							<xsl:with-param name="lab_n_graded" select="$lab_n_graded"/>
							<xsl:with-param name="lab_avg_score" select="$lab_avg_score"/>
							<xsl:with-param name="lab_per_att" select="$lab_per_att"/>
							<xsl:with-param name="lab_que" select="$lab_que"/>
							<xsl:with-param name="lab_leaners" select="$lab_leaners"/>
						</xsl:apply-templates>
						<!--<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>-->
						<td valign="middle">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:apply-templates select="report_body/report_list/atm_stat_list/atm_stat">
						<xsl:with-param name="lab_na" select="$lab_na"/>
						<xsl:with-param name="lab_attempt" select="$lab_attempt"/>
						<xsl:with-param name="lab_all_attempt" select="$lab_all_attempt"/>
						<xsl:with-param name="lab_easy" select="$lab_easy"/>
						<xsl:with-param name="lab_normal" select="$lab_normal"/>
						<xsl:with-param name="lab_hard" select="$lab_hard"/>
						<xsl:with-param name="lab_no_user_attempt" select="$lab_no_user_attempt"/>
					</xsl:apply-templates>
				</table>
				
				<xsl:if test="report_body/spec/data_list/data[@name = 'group_by']/@value != 'QUE_TYPE'">
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
						<tr>
							<td>
								<span class="text"> <xsl:value-of select="$lab_showing"/></span>
							</td>
						</tr>
					</table>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:if test="count(report_body/report_list/atm_stat_list/atm_stat)&gt;0">
							<xsl:variable name="title">
								<xsl:call-template name="escape_js">
									<xsl:with-param name="input_str" select="report_body/spec/title/text()"/>
								</xsl:call-template>
							</xsl:variable>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_export"/>
								<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.rslt_dl_rpt_adv('<xsl:value-of select="$rpt_xls"/>','<xsl:value-of select="$title"/>')</xsl:with-param>
							</xsl:call-template>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</xsl:if>
						<!--
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_save"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:mgt_rpt.ins_rpt_res_prep_popup(document.frmXml,'<xsl:value-of select="$rsp_id"/>', '','<xsl:value-of select="$ent_id"/>','<xsl:value-of select="$rpt_type"/>')</xsl:with-param>
						</xsl:call-template>-->
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:window.close()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="atm_stat">
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_attempt"/>
		<xsl:param name="lab_all_attempt"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_no_user_attempt"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">StatRowsEven</xsl:when>
				<xsl:otherwise>StatRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:for-each select="attempt">
			<tr class="{$row_class}">
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<xsl:choose>
					<xsl:when test="position() = 1">
						<xsl:choose>
							<xsl:when test="/report/report_body/spec/data_list/data[@name = 'group_by']/@value = 'QUE'">
								<td nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="../@res_title"/>(<xsl:value-of select="../@res_id"/>)
									</span>
								</td>
								<xsl:if test="/report/report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_fdr'">
									<td nowrap="nowrap">
										<span class="SmallText">
											<xsl:value-of select="../@res_fdr_title"/>(<xsl:value-of select="../@res_fdr_id"/>)

										</span>
									</td>
								</xsl:if>
								<xsl:if test="/report/report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_type'">
									<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<xsl:choose>
												<xsl:when test="../@res_type = 'GEN~AICC'">
													<xsl:value-of select="$lab_gen"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'WCT'">
													<xsl:value-of select="$lab_wct"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'SSC'">
													<xsl:value-of select="$lab_ssc"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'QUE'">
													<xsl:value-of select="$lab_que"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'MC'">
													<xsl:value-of select="$lab_mc"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'FB'">
													<xsl:value-of select="$lab_fb"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'MT'">
													<xsl:value-of select="$lab_mt"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'TF'">
													<xsl:value-of select="$lab_tf"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'ES'">
													<xsl:value-of select="$lab_es"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'FSC'">
													<xsl:value-of select="$lab_fixed_sc"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'DSC'">
													<xsl:value-of select="$lab_dna_sc"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'ASM'">
													<xsl:value-of select="$lab_asm"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'FAS'">
													<xsl:value-of select="$lab_fas"/>
												</xsl:when>
												<xsl:when test="../@res_type = 'DAS'">
													<xsl:value-of select="$lab_das"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_na"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
									</td>
								</xsl:if>
								
								<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<xsl:value-of select="../@que_score"></xsl:value-of>
										</span>
								</td>
								
								<xsl:if test="/report/report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_diff'">
									<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<xsl:choose>
												<xsl:when test="../@res_diff = '1'">
													<xsl:value-of select="$lab_easy"/>
												</xsl:when>
												<xsl:when test="../@res_diff = '2'">
													<xsl:value-of select="$lab_normal"/>
												</xsl:when>
												<xsl:when test="../@res_diff = '3'">
													<xsl:value-of select="$lab_hard"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_na"/>
												</xsl:otherwise>
											</xsl:choose>
										</span>
									</td>
								</xsl:if>
							</xsl:when>
							<xsl:when test="/report/report_body/spec/data_list/data[@name = 'group_by']/@value = 'RES_FDR'">
								<td nowrap="nowrap">
									<span class="SmallText">
										<xsl:value-of select="../@res_fdr_title"/>(<xsl:value-of select="../@res_fdr_id"/>)
									</span>
								</td>
							</xsl:when>
							<xsl:when test="/report/report_body/spec/data_list/data[@name = 'group_by']/@value = 'QUE_TYPE'">
								<td nowrap="nowrap" align="center">
									<span class="SmallText">
										<xsl:choose>
											<xsl:when test="../@res_type = 'GEN~AICC'">
												<xsl:value-of select="$lab_gen"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'WCT'">
												<xsl:value-of select="$lab_wct"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'SSC'">
												<xsl:value-of select="$lab_ssc"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'QUE'">
												<xsl:value-of select="$lab_que"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'MC'">
												<xsl:value-of select="$lab_mc"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'FB'">
												<xsl:value-of select="$lab_fb"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'MT'">
												<xsl:value-of select="$lab_mt"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'TF'">
												<xsl:value-of select="$lab_tf"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'ES'">
												<xsl:value-of select="$lab_es"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'FSC'">
												<xsl:value-of select="$lab_fixed_sc"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'DSC'">
												<xsl:value-of select="$lab_dna_sc"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'ASM'">
												<xsl:value-of select="$lab_asm"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'FAS'">
												<xsl:value-of select="$lab_fas"/>
											</xsl:when>
											<xsl:when test="../@res_type = 'DAS'">
												<xsl:value-of select="$lab_das"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_na"/>
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</xsl:when>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="/report/report_body/spec/data_list/data[@name = 'group_by']/@value = 'QUE'">
								<td nowrap="nowrap" align="center">
									<span class="SmallText">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</span>
								</td>
								<xsl:if test="/report/report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_fdr'">
									<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</span>
									</td>
								</xsl:if>
								<xsl:if test="/report/report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_type'">
									<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</span>
									</td>
								</xsl:if>
								
								<td nowrap="nowrap" align="center">
									<span class="SmallText">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</span>
								</td>
								
								<xsl:if test="/report/report_body/spec/data_list/data[@name = 'content_lst']/@value = 'res_diff'">
									<td nowrap="nowrap" align="center">
										<span class="SmallText">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</span>
									</td>
								</xsl:if>
							</xsl:when>
							<xsl:when test="/report/report_body/spec/data_list/data[@name = 'group_by']/@value = 'RES_FDR'">
								<td nowrap="nowrap" align="center">
									<span class="SmallText">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</span>
								</td>
							</xsl:when>
							<xsl:when test="/report/report_body/spec/data_list/data[@name = 'group_by']/@value = 'QUE_TYPE'">
								<td nowrap="nowrap" align="center">
									<span class="SmallText">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</span>
								</td>
							</xsl:when>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				<td nowrap="nowrap" align="center">
					<span class="SmallText">
						<xsl:choose>
							<xsl:when test="@id = 0">
								<xsl:value-of select="$lab_all_attempt"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_attempt"/><xsl:value-of select="@id"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
				<xsl:choose>
					<xsl:when test="@attempt_cnt=0">
						<xsl:variable name="atm_col_size">
							<xsl:choose>
								<xsl:when test="/report/report_body/spec/data_list/data[@name = 'group_by']/@value = 'QUE'">
									<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'content_lst' and @value!='res_fdr' and @value!='res_type' and @value!='res_diff'])"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="count(/report/report_body/spec/data_list/data[@name = 'content_lst'])"/> 
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<td colspan="{$atm_col_size}" align="center">
							<xsl:value-of select="$lab_no_user_attempt"/>
						</td>
					</xsl:when>
					<xsl:otherwise>							
						<xsl:apply-templates select="/report/report_body/spec/data_list/data[@name = 'content_lst']" mode="value">
							<xsl:with-param name="this" select="."/>
							<xsl:with-param name="lab_na" select="$lab_na"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data[@name = 'content_lst']" mode="title">
		<xsl:param name="lab_attempts"/>
		<xsl:param name="lab_correct"/>
		<xsl:param name="lab_incorrect"/>
		<xsl:param name="lab_partial_correct"/>
		<xsl:param name="lab_n_graded"/>
		<xsl:param name="lab_avg_score"/>
		<xsl:param name="lab_per_att"/>
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_leaners"/>
		<xsl:choose>
			<xsl:when test="@value = 'res_fdr' or @value = 'res_type' or @value = 'res_diff' or @value = 'res_title'">
			</xsl:when>
			<xsl:otherwise>
				<td nowrap="nowrap">
					<span class="SmallText">
		       	<xsl:choose>
							<xsl:when test="@value ='attempt_cnt'">
								<xsl:value-of select="$lab_attempts"/>
							</xsl:when>
							<xsl:when test="@value = 'correct'">
								<xsl:value-of select="$lab_correct"/>
							</xsl:when>
							<xsl:when test="@value = 'incorrect'">
								<xsl:value-of select="$lab_incorrect"/>
							</xsl:when>
							<xsl:when test="@value ='partial_correct'">
								<xsl:value-of select="$lab_partial_correct"/>
							</xsl:when>
							<xsl:when test="@value ='not_graded'">
								<xsl:value-of select="$lab_n_graded"/>
							</xsl:when>
							<xsl:when test="@value ='avg_sore'">
								<xsl:value-of select="$lab_avg_score"/>
							</xsl:when>
							<xsl:when test="@value ='learners'">
								<xsl:value-of select="$lab_leaners"/>
							</xsl:when>
							<xsl:when test="@value = 'perfect_attempts'">
								<xsl:value-of select="$lab_per_att"/>
							</xsl:when>
							<xsl:when test="@value = 'questions'">
								<xsl:value-of select="$lab_que"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@value"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template match="data[@name='content_lst']" mode="value">
		<xsl:param name="lab_na"/>
		<xsl:param name="this"/>
		<xsl:choose>
			<xsl:when test="@value = 'res_fdr' or @value = 'res_type' or @value = 'res_diff' or @value = 'res_title'">
			</xsl:when>
			<xsl:otherwise>
				<td nowrap="nowrap" align="center"> <!-- right  显示列比较少时  数据对不齐 -->
					<span class="SmallText">
						<xsl:choose>
							<xsl:when test="@value = 'attempt_cnt'">
								<xsl:choose>
									<xsl:when test="$this/@attempt_cnt=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@attempt_cnt"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="@value = 'correct'">  <!-- 答对次数 -->
								<xsl:choose>
									<xsl:when test="$this/@correct=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@correct"/>
									</xsl:otherwise>
								</xsl:choose>
								(<xsl:value-of select="$this/@cor_percent"/>%)
							</xsl:when>
							<xsl:when test="@value = 'incorrect'">  <!-- 答错次数 -->
								<xsl:choose>
									<xsl:when test="$this/@incorrect=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@incorrect"/>
									</xsl:otherwise>
								</xsl:choose>
							(<xsl:value-of select="$this/@incor_percent"/>%)
							</xsl:when>
							<xsl:when test="@value = 'partial_correct'">   <!-- 未完全答对次数 -->
								<xsl:choose>
									<xsl:when test="$this/@partial_correct=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@partial_correct"/>
									</xsl:otherwise>
								</xsl:choose>
							(<xsl:value-of select="$this/@par_cor_percent"/>%)
							</xsl:when>
							<xsl:when test="@value = 'not_graded'">
								<xsl:choose>
									<xsl:when test="$this/@not_graded=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@not_graded"/>
									</xsl:otherwise>
								</xsl:choose>
							(<xsl:value-of select="$this/@not_graded_percent"/>%)
							</xsl:when>
							<xsl:when test="@value = 'avg_sore'">
								<xsl:choose>
									<xsl:when test="$this/@avg_score_precent=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@avg_score_precent"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="@value = 'learners'">
								<xsl:choose>
									<xsl:when test="$this/@learners=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@learners"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="@value = 'perfect_attempts'">
								<xsl:choose>
									<xsl:when test="$this/@perfect_attempts=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@perfect_attempts"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="@value = 'questions'">
								<xsl:choose>
									<xsl:when test="$this/@questionsts=''">0</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$this/@questions"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>		                       
				</span>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="status">
		<xsl:value-of select="desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template match="*" mode="value">
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<td nowrap="nowrap">
			<xsl:choose>
				<xsl:when test="@type = 'constant_label' or @type = 'textarea' or @type = 'text' or @type = 'item_access_pickup' or @type = 'catalog_attachment' or @type = 'iac_select' ">
					<xsl:attribute name="align">LEFT</xsl:attribute>
				</xsl:when>
				<xsl:when test="@type = 'pos_amount' or @type = 'constant_datetime' or @type = 'date' or @type = 'pos_int' ">
					<xsl:attribute name="align">CENTER</xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="$is_run = 'true'">
					<span class="SmallText">
						<i>
							<!--get value from itm_gen_frm_utils.xsl -->
							<xsl:variable name="value">
								<xsl:apply-templates select="." mode="gen_field"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$value != ''">
									<xsl:copy-of select="$value"/>
								</xsl:when>
								<xsl:otherwise>
									<span class="Text">
										<xsl:value-of select="$lab_na"/>
									</span>
								</xsl:otherwise>
							</xsl:choose>
						</i>
					</span>
				</xsl:when>
				<xsl:otherwise>
					<span class="SmallText">
						<!--get value from itm_gen_frm_utils.xsl -->
						<xsl:variable name="value">
							<xsl:apply-templates select="." mode="gen_field"/>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$value != ''">
								<xsl:copy-of select="$value"/>
							</xsl:when>
							<xsl:otherwise>
								<span class="SmallText">
									<xsl:value-of select="$lab_na"/>
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="no_value">
		<xsl:param name="is_run"/>
		<xsl:param name="lab_na"/>
		<xsl:variable name="field_no" select="@value"/>
		<xsl:variable name="field_align">
			<xsl:choose>
				<xsl:when test="@name = 'itm_type'">LEFT</xsl:when>
				<xsl:when test="@name = 'itm_content_lst'">
					<xsl:variable name="field_type" select="/report/report_body/report_list/record/item/valued_template/section/*[name() = $field_no]/@type"/>
					<xsl:choose>
						<xsl:when test="$field_type = 'constant_label' or $field_type = 'textarea' or $field_type = 'text' or $field_type = 'item_access_pickup' or $field_type = 'catalog_attachment'">LEFT</xsl:when>
						<xsl:when test="$field_type = 'pos_amount'">CENTER</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@name = 'run_content_lst'">
					<xsl:variable name="field_type" select="/report/report_body/report_list/record/run/valued_template/section/*[name() = $field_no]/@type"/>
					<xsl:choose>
						<xsl:when test="$field_type = 'textarea' ">LEFT</xsl:when>
						<xsl:when test="$field_type = 'constant_datetime' or $field_type = 'date' or $field_type = 'pos_int'">CENTER</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:when test="@name = 'content_lst'">CENTER</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<td valign="top" align="{$field_align}">
			<xsl:choose>
				<xsl:when test="$is_run = 'true'">
					<span class="SmallText">
						<i>
							<xsl:value-of select="$lab_na"/>
						</i>
					</span>
				</xsl:when>
				<xsl:otherwise>
					<span class="SmallText">
						<xsl:value-of select="$lab_na"/>
					</span>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	<!-- =================================================================================== -->
	<xsl:template name="report_criteria">
		<xsl:param name="this_width"/>
		<xsl:param name="lab_cos"/>
		<xsl:param name="lab_cos_catalog"/>
		<xsl:param name="lab_cos_title"/>
		<xsl:param name="lab_all_cos"/>
		<xsl:param name="lab_leaners"/>
		<xsl:param name="lab_lrn_group"/>
		<xsl:param name="lab_leaner"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_my_direct_staff"/>
		<xsl:param name="lab_att_create_timestamp"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_na"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_lrn_mod"/>
		<xsl:param name="lab_group_by"/>
		<xsl:param name="lab_group_by_Q"/>
		<xsl:param name="lab_group_by_RF"/>
		<xsl:param name="lab_group_by_QT"/>
		<xsl:param name="lab_num_attempts"/>
		<xsl:param name="lab_all_attemptts"/>
		<xsl:param name="lab_sub_date"/>
		<xsl:param name="lab_attempt_type"/>
		<xsl:param name="lab_answer_for_lrn"/>
		<xsl:param name="lab_answer_for_course_lrn"/>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$this_width"/>
		</xsl:call-template>
		<table cellpadding="3" cellspacing="0" border="0" width="{$this_width}" class="Bg">
			<tr>
				<td width="150" align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<!-- course title -->
			<xsl:if test="report_body/spec/data_list/data[@name='itm_id']">
				<tr>
					<td align="right" valign="top" class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_cos_title"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:apply-templates select="report_body/spec//data[@name='itm_id']" mode="title"/>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- mod list -->
			<xsl:if test="report_body/spec/data_list/data[@name='mod_id']">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_lrn_mod"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:for-each select="report_body/spec/data_list/data[@name='mod_id']">
								<xsl:variable name="mod_id">
									<xsl:value-of select="@value"/>
								</xsl:variable>
								<xsl:value-of select="/report/report_body/presentation/data[@name='mod_id' and @value=$mod_id]/@display"/>
								<xsl:if test="position()!=last()">, </xsl:if>
							</xsl:for-each>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- learner -->
			<xsl:if test="report_body/spec/data_list/data[@name='usr_ent_id'] or report_body/spec/data_list/data[@name='all_user_ind' and @value='1']">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_leaner"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td  class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='all_user_ind']/@value = '1'">
									<xsl:choose>
										<xsl:when test="$tc_enabled='true'">
											<xsl:choose>
												<xsl:when test="report_body/spec/data_list/data[@name='answer_for_lrn']/@value = '1' and report_body/spec/data_list/data[@name='answer_for_course_lrn']/@value = '0' ">
													<xsl:value-of select="$lab_answer_for_lrn"/>
												</xsl:when>
												<xsl:when test="report_body/spec/data_list/data[@name='answer_for_lrn']/@value = '0' and report_body/spec/data_list/data[@name='answer_for_course_lrn']/@value = '1' ">
													<xsl:value-of select="$lab_answer_for_course_lrn"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="$lab_by_all_user"/>
												</xsl:otherwise>										
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_by_all_user"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="report_body/spec/data_list/data[@name='usr_ent_id']">
										<xsl:variable name="ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:value-of select="/report/report_body/presentation/data[@name='usr_ent_id' and @value=$ent_id]/@display"/>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- user group -->
			<xsl:if test="report_body/spec/data_list/data[@name='usg_ent_id'] ">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_lrn_group"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="count(/report/report_body/presentation/data[@name='usg_ent_id']) = 0">
									<xsl:value-of select="$lab_na"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="report_body/spec/data_list/data[@name='usg_ent_id']">
										<xsl:variable name="usg_ent_id">
											<xsl:value-of select="@value"/>
										</xsl:variable>
										<xsl:value-of select="/report/report_body/presentation/data[@name='usg_ent_id' and @value=$usg_ent_id]/@display"/>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<!-- attendance date -->
			<xsl:choose>
				<xsl:when test="count(report_body/spec/data_list/data[@name='att_create_start_datetime']) = 0 and count(report_body/spec/data_list/data[@name='att_create_end_datetime']) = 0">
					<!--<tr>
						<td align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_att_create_timestamp"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td>
							<span class="Text">
								<xsl:value-of select="$lab_na"/>
							</span>
						</td>
					</tr>-->
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td align="right" valign="top"  class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_att_create_timestamp"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:value-of select="$lab_from"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='att_create_start_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_create_start_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_to"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='att_create_end_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='att_create_end_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!-- attempt date -->
			<xsl:choose>
				<xsl:when test="count(report_body/spec/data_list/data[@name='attempt_start_datetime']) = 0 and count(report_body/spec/data_list/data[@name='attempt_end_datetime']) = 0">
					<!--<tr>
						<td align="right" valign="top">
							<span class="TitleText">
								<xsl:value-of select="$lab_sub_date"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td>
							<span class="Text">
								<xsl:value-of select="$lab_na"/>
							</span>
						</td>
					</tr>-->
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td align="right" valign="top"  class="wzb-form-label">
							<span class="TitleText">
								<xsl:value-of select="$lab_sub_date"/>
								<xsl:text>：</xsl:text>
							</span>
						</td>
						<td class="wzb-form-control">
							<span class="Text">
								<xsl:value-of select="$lab_from"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='attempt_start_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='attempt_start_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_to"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:choose>
									<xsl:when test="report_body/spec/data_list/data[@name='attempt_end_datetime']">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp" select="report_body/spec/data_list/data[@name='attempt_end_datetime']/@value"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_na"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!-- attemp type-->
			<xsl:if test="report_body/spec/data_list/data[@name='attempt_type'] ">
				<tr>
					<td align="right" valign="top"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_attempt_type"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="count(report_body/spec/data_list/data[@name='attempt_type'] ) = 0">
									<xsl:value-of select="$lab_na"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="report_body/spec/data_list/data[@name='attempt_type']">
										<xsl:if test="@value = 'NUMBERED'">
											<xsl:value-of select="$lab_num_attempts"/>
										</xsl:if>
										<xsl:if test="@value = 'ALL'">
											<xsl:value-of select="$lab_all_attemptts"/>
										</xsl:if>
										<xsl:if test="position()!=last()">, </xsl:if>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<xsl:if test="report_body/spec/data_list/data[@name='group_by']">
				<tr>
					<td align="right"  class="wzb-form-label">
						<span class="TitleText">
							<xsl:value-of select="$lab_group_by"/>
							<xsl:text>：</xsl:text>
						</span>
					</td>
					<td class="wzb-form-control">
						<span class="Text">
							<xsl:choose>
								<xsl:when test="report_body/spec/data_list/data[@name='group_by']/@value = 'QUE'">
									<xsl:value-of select="$lab_group_by_Q"/>
								</xsl:when>
								<xsl:when test="report_body/spec/data_list/data[@name='group_by']/@value = 'RES_FDR'">
									<xsl:value-of select="$lab_group_by_RF"/>
								</xsl:when>
								<xsl:when test="report_body/spec/data_list/data[@name='group_by']/@value = 'QUE_TYPE'">
									<xsl:value-of select="$lab_group_by_QT"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_na"/>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td align="right" height="10">
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data" mode="title">
		<xsl:variable name="name" select="@name"/>
		<xsl:variable name="value" select="@value"/>
		<xsl:for-each select="/report/report_body/presentation/data[@name=$name and @value = $value]/parent">
			<xsl:value-of select="@display"/>(<xsl:value-of select="@itm_code"/>) > 
	</xsl:for-each>
		<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@display"/>(<xsl:value-of select="/report/report_body/presentation/data[@name=$name and @value = $value]/@itm_code"/>)
			</xsl:template>
</xsl:stylesheet>

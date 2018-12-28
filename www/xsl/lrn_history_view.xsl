<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<!-- call common xsl -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>	
	<xsl:import href="share/lrn_history_view_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="student_id" select="report/report_body/student/@ent_id"/>
	<xsl:variable name="num" select="count(report/report_body/report_list/record/student[@ent_id=$student_id])"/>
	<xsl:variable name="total" select="count(report/report_body/report_list/record/student)"/>
	<xsl:variable name="cur_page" select="/report/report_body/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/report/report_body/pagination/@page_size"/>
	<xsl:variable name="total_rec" select="/report/report_body/pagination/@total_rec"/>
	<xsl:variable name="cur_sort_col" select="/report/report_body/pagination/@sort_col"/>
	<xsl:variable name="cur_sort_order" select="/report/report_body/pagination/@sort_order"/>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order = 'asc' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_timestamp" select="/report/report_body/pagination/@timestamp"/>
	<xsl:variable name="data_list" select="/report/report_body/spec/data_list"/>
	<xsl:variable name="tc_enabled" select="/report/meta/tc_enabled"/>
	<!-- =============================================================== -->
	<xsl:variable name="isStudent">
		<xsl:choose>
			<xsl:when test="report/report_body/student">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="supervise_module"/>
		<xsl:apply-templates select="report"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="supervise_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<xsl:template match="report">
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			lrn_soln = new wbLearnSolution;

			var p
			p = '';
			if (getUrlParam('p') == '1') {p = true;}
			else {p = false;}
			unit = 0;
			
			function sortCol(colName, order){
				wb_utils_nav_get_urlparam('sort_col',colName,'sort_order',order,'timestamp','');
			}
			
			
			function init(){
				if(getUrlParam('fromHP')=='NO'){
					if(document.all){
					//IE
						
						document.all['div1'].style.display = 'block'
						document.all['div2'].style.display = 'block'
					}else if(document.getElementById!=null){
					//NS6
						document.getElementById('div1').style.display = 'block'
						document.getElementById('div2').style.display = 'block'
					}else{
					//NS4
						document.layers['div1'].visibility = 'show'
						document.layers['div2'].visibility = 'show'
					}
				}
			}
			
			
		]]></script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<form name="frmXml" onsubmit="return false;">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_home">首頁</xsl:with-param>
			<xsl:with-param name="lab_cal_yr">歷年</xsl:with-param>
			<xsl:with-param name="lab_lrn_history">學習歷程</xsl:with-param>
			<xsl:with-param name="lab_lrn_rpt">學習報告</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_grade"><xsl:value-of select="$lab_grade"/></xsl:with-param>
			<xsl:with-param name="lab_compl_date">考勤日期</xsl:with-param>
			<xsl:with-param name="lab_enroll_date">入學日期</xsl:with-param>
			<xsl:with-param name="lab_category">子目錄</xsl:with-param>
			<xsl:with-param name="lab_duration">時限</xsl:with-param>
			<xsl:with-param name="lab_status">考勤狀態</xsl:with-param>
			<xsl:with-param name="lab_result">結果</xsl:with-param>
			<xsl:with-param name="lab_showing">顯示</xsl:with-param>
			<xsl:with-param name="lab_prev">前5個結果</xsl:with-param>
			<xsl:with-param name="lab_next">後5個結果</xsl:with-param>
			<xsl:with-param name="lab_total">合計</xsl:with-param>
			<xsl:with-param name="lab_grand_total">總數</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_no_item">找不到紀錄</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">資源回收筒</xsl:with-param>
			<xsl:with-param name="lab_remark">備註</xsl:with-param>
			<xsl:with-param name="lab_details">詳細情況</xsl:with-param>
			<xsl:with-param name="lab_track_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_of">學習歷程</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_course_title_keywords">課程標題關鍵字</xsl:with-param>
			<xsl:with-param name="lab_course_catalog">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_attendance_status">完成狀態</xsl:with-param>
			<xsl:with-param name="lab_number_of_courses_taken">已學習的課程數目</xsl:with-param>
			<xsl:with-param name="lab_total_credit_earned">學分總數</xsl:with-param>
			<xsl:with-param name="lab_credit">學分</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_detailed_records">詳細記錄</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_desc">你可以在此看到他已完成課程的完整記錄。 當他的考勤狀態結束時就標誌著這個課程已經完成。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_refresh">更新</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">搜尋</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次訪問</xsl:with-param>
			<xsl:with-param name="lab_time_spent">花費時間</xsl:with-param>
			<xsl:with-param name="lab_score">成績</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_from">入學日期從</xsl:with-param>
			<xsl:with-param name="is_student_own_view">false</xsl:with-param>
			<xsl:with-param name="popup">true</xsl:with-param>
		   <xsl:with-param name="lab_tc_title">培訓中心</xsl:with-param>
		   <xsl:with-param name="lab_compl_completedate">结训日期</xsl:with-param>
		   <xsl:with-param name="tc_enabled" select="$tc_enabled"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_home">首页</xsl:with-param>
			<xsl:with-param name="lab_cal_yr">历年</xsl:with-param>
			<xsl:with-param name="lab_lrn_history">学习历程</xsl:with-param>
			<xsl:with-param name="lab_lrn_rpt">学习报告</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_group"><xsl:value-of select="$lab_group"/></xsl:with-param>
			<xsl:with-param name="lab_grade"><xsl:value-of select="$lab_grade"/></xsl:with-param>
			<xsl:with-param name="lab_compl_date">考勤日期</xsl:with-param>
			<xsl:with-param name="lab_enroll_date">录取日期</xsl:with-param>
			<xsl:with-param name="lab_category">子目录</xsl:with-param>
			<xsl:with-param name="lab_duration">时限</xsl:with-param>
			<xsl:with-param name="lab_status">完成状态</xsl:with-param>
			<xsl:with-param name="lab_result">结果</xsl:with-param>
			<xsl:with-param name="lab_showing">显示</xsl:with-param>
			<xsl:with-param name="lab_prev">前5个结果</xsl:with-param>
			<xsl:with-param name="lab_next">后5个结果</xsl:with-param>
			<xsl:with-param name="lab_total">合计</xsl:with-param>
			<xsl:with-param name="lab_grand_total">总计</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有查到报告</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">回收站</xsl:with-param>
			<xsl:with-param name="lab_remark">备注</xsl:with-param>
			<xsl:with-param name="lab_details">详细情况</xsl:with-param>
			<xsl:with-param name="lab_track_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_of">学习历程</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_course_title_keywords">课程标题关键字</xsl:with-param>
			<xsl:with-param name="lab_course_catalog">课程目录</xsl:with-param>
			<xsl:with-param name="lab_attendance_status">完成状态</xsl:with-param>
			<xsl:with-param name="lab_number_of_courses_taken">已学习的课程数目</xsl:with-param>
			<xsl:with-param name="lab_total_credit_earned">学分总计</xsl:with-param>
			<xsl:with-param name="lab_credit">学分</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_detailed_records">详细记录</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_desc">你可以在这里查看该员工学习的历史记录，只有已结束的课程才在此处显示。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_refresh">刷新</xsl:with-param>
			<xsl:with-param name="lab_last_access">上次访问</xsl:with-param>
			<xsl:with-param name="lab_time_spent">学习时长</xsl:with-param>
			<xsl:with-param name="lab_score">成绩</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_from">录取日期从</xsl:with-param>
			<xsl:with-param name="is_student_own_view">false</xsl:with-param>
			<xsl:with-param name="popup">true</xsl:with-param>
			<xsl:with-param name="lab_tc_title">培训中心</xsl:with-param>
			<xsl:with-param name="lab_compl_completedate">结训日期</xsl:with-param>
		   <xsl:with-param name="tc_enabled" select="$tc_enabled"/>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_home">Home</xsl:with-param>
			<xsl:with-param name="lab_cal_yr">Calendar year</xsl:with-param>
			<xsl:with-param name="lab_lrn_history">Learning history</xsl:with-param>
			<xsl:with-param name="lab_lrn_rpt">Learner report</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_group">
				<xsl:value-of select="$lab_group"/>
			</xsl:with-param>
			<xsl:with-param name="lab_grade">
				<xsl:value-of select="$lab_grade"/>
			</xsl:with-param>
			<xsl:with-param name="lab_compl_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_enroll_date">Enrollment date</xsl:with-param>
			<xsl:with-param name="lab_category">Category</xsl:with-param>
			<xsl:with-param name="lab_duration">Duration (hrs)</xsl:with-param>
			<xsl:with-param name="lab_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_result">Result</xsl:with-param>
			<xsl:with-param name="lab_showing">Showing</xsl:with-param>
			<xsl:with-param name="lab_prev">prev 5</xsl:with-param>
			<xsl:with-param name="lab_next">next 5</xsl:with-param>
			<xsl:with-param name="lab_total">Total</xsl:with-param>
			<xsl:with-param name="lab_grand_total">Grand total</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_no_item">The learner has no learning history records.</xsl:with-param>
			<xsl:with-param name="lab_na">--</xsl:with-param>
			<xsl:with-param name="lab_recycle_bin">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_remark">Remarks</xsl:with-param>
			<xsl:with-param name="lab_details">Details</xsl:with-param>
			<xsl:with-param name="lab_track_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_of">Learning history of</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_course_title_keywords">Course title keywords</xsl:with-param>
			<xsl:with-param name="lab_course_catalog">Course catalog</xsl:with-param>
			<xsl:with-param name="lab_attendance_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_number_of_courses_taken">Number of courses taken</xsl:with-param>
			<xsl:with-param name="lab_total_credit_earned">Total credit earned</xsl:with-param>
			<xsl:with-param name="lab_credit">Credit</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_detailed_records">Detailed records</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_desc">Here you can view the history of the learner's past learning results. Courses are displayed here if the learner's course learning statuses are finalized.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_search">Search</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_refresh">Refresh</xsl:with-param>
			<xsl:with-param name="lab_last_access">Last access</xsl:with-param>
			<xsl:with-param name="lab_time_spent">Time spent</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_lrn_history_from">Enrollment date from</xsl:with-param>
			<xsl:with-param name="is_student_own_view">false</xsl:with-param>
			<xsl:with-param name="popup">true</xsl:with-param>
		   <xsl:with-param name="lab_tc_title">Training center</xsl:with-param>
		   <xsl:with-param name="lab_compl_completedate">Completion date</xsl:with-param>
		   <xsl:with-param name="tc_enabled" select="$tc_enabled"/>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

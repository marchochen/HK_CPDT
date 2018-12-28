<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
    <xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils_process.xsl"/>
	
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<!-- other -->
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="training_type" select="/applyeasy/meta/training_type"/>
	<xsl:variable name="itm_integrated_ind" select="/applyeasy/meta/itm_integrated_ind"/>
	<xsl:variable name="wrk_tpl_id" select="/applyeasy/meta/wrk_tpl_id"/>
	<xsl:variable name="ity_id" select="/applyeasy/meta/ity_id"/>
	<xsl:variable name="itm_app_approval_type" select="/applyeasy/meta/itm_app_approval_type"/>
	<xsl:variable name="tvw_id" select="/applyeasy/meta/tvw_id"/>
	<xsl:variable name="itm_dummy_type" select="/applyeasy/meta/itm_dummy_type"/>
	
	<xsl:variable name="lab_next_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '101')"/>
	<xsl:variable name="button_cancel" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global.button_cancel')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				itm_lst = new wbItem
				ann = new wbAnnouncement
				course_lst = new wbCourse	
				rpt = new wbReport
				attn = new wbAttendance
				cmt = new wbScoreScheme;
				function changeContentDefPic() {
					var frmObj = document.frmXml;
					if (frmObj.itm_content_def[0].checked) {
						document.itm_content_def_pic0.border = 1;
						document.itm_content_def_pic1.border = 0;
					}
					else
					if (frmObj.itm_content_def[1].checked) {
						document.itm_content_def_pic0.border = 0;
						document.itm_content_def_pic1.border = 1;
					}
				}
				
				function submitContentDef() {
					var frmObj = document.frmXml;
					frmObj.action = wb_utils_ae_servlet_url;
					frmObj.method = 'get';
					frmObj.stylesheet.value = 'itm_simple_add.xsl';
					if (frmObj.itm_content_def[0].checked || frmObj.itm_content_def[1].checked) {
						frmObj.submit();
					}
					else
					{
						alert(wb_msg_select_content_def);
					}
				}
			]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmXml" method="post">
					<input type="hidden" name="cmd" value="ae_simple_ins_itm_perp"/>
					<input type="hidden" name="stylesheet" value=""/>
					<input type="hidden" name="training_type" value="{$training_type}"/>
					<input type="hidden" name="itm_integrated_ind" value="{$itm_integrated_ind}"/>
					<input type="hidden" name="wrk_tpl_id" value="{$wrk_tpl_id}"/>
					<input type="hidden" name="ity_id" value="{$ity_id}"/>
					<input type="hidden" name="itm_app_approval_type" value="{$itm_app_approval_type}"/>
					<input type="hidden" name="tvw_id" value="{$tvw_id}"/>
					<input type="hidden" name="itm_dummy_type" value="{$itm_dummy_type}"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">線上內容</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>訊息</xsl:with-param>
			<xsl:with-param name="lab_learning_mod">
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>單元
			</xsl:with-param>
			<xsl:with-param name="lab_learning_mod_desc">在線學習内容。</xsl:with-param>
			<xsl:with-param name="lab_content_schedule">線上內容時間表</xsl:with-param>
			<xsl:with-param name="lab_content_schedule_desc">指定線上內容學習的開始和結束日期。</xsl:with-param>
			<xsl:with-param name="lab_course_reminder">
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>提示
			</xsl:with-param>
			<xsl:with-param name="lab_course_reminder_desc">指定觸發提示的時間及條件。</xsl:with-param>
			<xsl:with-param name="lab_announcement">通告</xsl:with-param>
			<xsl:with-param name="lab_announcement_desc">只對已報名學員發佈的公共資訊。</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">查看用戶學習報告。</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting">快速參考設置</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting_desc">
				指定學員是否可以在不進行<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>報名的情況下預覽
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容
			</xsl:with-param>
			<xsl:with-param name="lab_completion_cri">結訓條件</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">
				完成<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的條件。
			</xsl:with-param>
			<xsl:with-param name="lab_course_result">計分項目</xsl:with-param>
			<xsl:with-param name="lab_course_result_desc">可以計算分數的在線/離線學習内容。</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_score_scheme">計分項目</xsl:with-param>
			<xsl:with-param name="lab_score_scheme_desc">
				*設定此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的計分項目以及其計分方法，這些設定將成爲此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>日後所開班別的預設值。
			</xsl:with-param>
			<xsl:with-param name="lab_grad_term">結訓條件</xsl:with-param>
			<xsl:with-param name="lab_grad_term_desc">*設定此
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的結訓條件，這些設定將會成爲此
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>日後所開班別的預設值。</xsl:with-param>
			<xsl:with-param name="lab_cos_measure">考勤</xsl:with-param>
			<xsl:with-param name="lab_score_record">成績記錄</xsl:with-param>
			<xsl:with-param name="lab_attendence">出席率</xsl:with-param>
			<xsl:with-param name="lab_grad_record">結訓記錄</xsl:with-param>
			<xsl:with-param name="lab_set_grad_term">結訓條件設定</xsl:with-param>
			<xsl:with-param name="lab_train_record">受訓記錄</xsl:with-param>
			<xsl:with-param name="lab_establish_type">
				請選擇此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的內容的建立方式：
			</xsl:with-param>
			<xsl:with-param name="lab_unification_content">
				班別使用<b>統一的<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容</b>
			</xsl:with-param>
			<xsl:with-param name="lab_independency_content">
				班別使用<b>獨立的<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容</b>
			</xsl:with-param>
			<xsl:with-param name="lab_unification_content_desc">
				僅需建立一套內容，該<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的所有班別的學員使用同一套內容。
			</xsl:with-param>
			<xsl:with-param name="lab_independency_content_desc">每個班別有其自己的一套內容，每個班別的學員只能看到其所在班別的內容。</xsl:with-param>
			<xsl:with-param name="lab_course_compose">
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容由以下部分組成：
			</xsl:with-param>
			<xsl:with-param name="lab_earlier_model">先修模塊</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">
				學習在線<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容的限制次序。
			</xsl:with-param>
			<xsl:with-param name="lab_notice">注意：你現在必須作出選擇，一旦選擇被確認，它將不能被撤回。</xsl:with-param>
			<xsl:with-param name="lab_no_establish_type">
				還沒有決定此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容的建立方式。
			</xsl:with-param>
			<xsl:with-param name="lab_click_establish_type">
				請點擊這裡決定<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容的建立方式。
			</xsl:with-param>
			<xsl:with-param name="lab_limit_order">
				學習在線<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容的限制次序。
			</xsl:with-param>
			<xsl:with-param name="lab_establish_content">如果你要建立內容，請到特定班別的<b>內容</b>裡建立。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">在线内容</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_learning_mod">学习单元</xsl:with-param>
			<xsl:with-param name="lab_learning_mod_desc">在线学习内容。</xsl:with-param>
			<xsl:with-param name="lab_content_schedule">在线内容时间表</xsl:with-param>
			<xsl:with-param name="lab_content_schedule_desc">指定在线内容学习的开始和结束日期。</xsl:with-param>
			<xsl:with-param name="lab_course_reminder">				
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>提示</xsl:with-param>
			<xsl:with-param name="lab_course_reminder_desc">指定触发提示的时间及条件。</xsl:with-param>
			<xsl:with-param name="lab_announcement">公告</xsl:with-param>
			<xsl:with-param name="lab_announcement_desc">只对已报名学员发布的公共信息。</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">查看用户学习报告。</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting">快速参考设置</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting_desc">
				指定学员是否可以在不进行课程报名的情况下预览<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容</xsl:with-param>
			<xsl:with-param name="lab_completion_cri">结训条件</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">
				完成<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的条件。
			</xsl:with-param>
			<xsl:with-param name="lab_course_result">计分项目</xsl:with-param>
			<xsl:with-param name="lab_course_result_desc">可以计算分数的在线/离线学习内容。</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_score_scheme">计分项目</xsl:with-param>
			<xsl:with-param name="lab_score_scheme_desc">
				*设定此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的计分项目以及其计分项目，这些设定将会成为此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>日后所开班别的预设值。
			</xsl:with-param>
			<xsl:with-param name="lab_grad_term">结训条件</xsl:with-param>
			<xsl:with-param name="lab_grad_term_desc">
				*设定此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的结训条件，这些设定将会成为此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>日后所开班别的预设值。
			</xsl:with-param>
			<xsl:with-param name="lab_cos_measure">考勤</xsl:with-param>
			<xsl:with-param name="lab_score_record">成绩记录</xsl:with-param>
			<xsl:with-param name="lab_attendence">出席率</xsl:with-param>
			<xsl:with-param name="lab_grad_record">结训记录</xsl:with-param>
			<xsl:with-param name="lab_set_grad_term">结训条件设定</xsl:with-param>
			<xsl:with-param name="lab_train_record">受训记录</xsl:with-param>
			<xsl:with-param name="lab_establish_type">
				请选择此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的内容的建立方式：
			</xsl:with-param>
			<xsl:with-param name="lab_unification_content">
				班级使用<b>统一的<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容</b>
			</xsl:with-param>
			<xsl:with-param name="lab_independency_content">
				班级使用<b>独立的<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容
			    </b>
			</xsl:with-param>
			<xsl:with-param name="lab_unification_content_desc">
				仅需建立一套内容，该<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的所有班级的学员使用同一套内容。</xsl:with-param>
			<xsl:with-param name="lab_independency_content_desc">每个班级有其自己的一套内容，每个班级的学员只能看到其所在班级的内容。
			</xsl:with-param>
			<xsl:with-param name="lab_course_compose">
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容由以下部分组成：
			</xsl:with-param>
			<xsl:with-param name="lab_earlier_model">先修模块</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">
				学习在线<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容的限制次序。
			</xsl:with-param>
			<xsl:with-param name="lab_notice">注意：您现在必须作出选择，一旦选择被确认，它将不能被撤回。</xsl:with-param>
			<xsl:with-param name="lab_no_establish_type">
				还没有决定此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容的建立方式。
			</xsl:with-param>
			<xsl:with-param name="lab_click_establish_type">
				请点击这里决定<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容的建立方式。
			</xsl:with-param>
			<xsl:with-param name="lab_limit_order">
				学习在线<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容的限制次序。
			</xsl:with-param>
			<xsl:with-param name="lab_establish_content">如果您要建立内容，请到特定班级的<b>内容</b>里建立。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">Content</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_learning_mod">Learning module</xsl:with-param>
			<xsl:with-param name="lab_learning_mod_desc">Online <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> content.</xsl:with-param>
			<xsl:with-param name="lab_content_schedule">Online content schedule</xsl:with-param>
			<xsl:with-param name="lab_content_schedule_desc">Specify the overall start and end dates for the online content.</xsl:with-param>
			<xsl:with-param name="lab_course_reminder"><xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">Exam</xsl:when>
				<xsl:otherwise>Course</xsl:otherwise>
				</xsl:choose> reminder</xsl:with-param>
			<xsl:with-param name="lab_course_reminder_desc">Specify the schedule and the conditions that trigger the course reminders.</xsl:with-param>
			<xsl:with-param name="lab_announcement">Notice</xsl:with-param>
			<xsl:with-param name="lab_announcement_desc">Public message available only to enrolled learners.</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">Run the tracking reports of user activity statistics.</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting">Quick reference settings</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting_desc">
				Specify whether learners can preview the <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> content without enrolling in the <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose>.
			</xsl:with-param>
			<xsl:with-param name="lab_completion_cri">Completion criteria</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">Condition for completing this <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> solution.</xsl:with-param>
			<xsl:with-param name="lab_course_result">Scoring item</xsl:with-param>
			<xsl:with-param name="lab_course_result_desc">Offline/online <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> content that will contribute score.</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_score_scheme">Scoring item</xsl:with-param>
			<xsl:with-param name="lab_score_scheme_desc">*Set scoring item of the <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> and its scoring scheme. This setting will be used as the default value in the classes of this <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> created thereafter.</xsl:with-param>
			<xsl:with-param name="lab_grad_term">Completion criteria</xsl:with-param>
			<xsl:with-param name="lab_grad_term_desc">*Set completion criteria of the <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose>. This setting will be used as the default value in the classes of this <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> created thereafter.</xsl:with-param>
			<xsl:with-param name="lab_score_record">Score</xsl:with-param>
			<xsl:with-param name="lab_attendence">Attendance</xsl:with-param>
			<xsl:with-param name="lab_grad_record">Result</xsl:with-param>
			<xsl:with-param name="lab_cos_measure">Result</xsl:with-param>
			<xsl:with-param name="lab_set_grad_term">Configurations</xsl:with-param>
			<xsl:with-param name="lab_train_record">Learner's results</xsl:with-param>
			<xsl:with-param name="lab_establish_type">Please choose the way <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> content of this learning solution is to be built:</xsl:with-param>
			<xsl:with-param name="lab_unification_content">Class to use <b>Common <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> content</b>
			</xsl:with-param>
			<xsl:with-param name="lab_independency_content">Class to use <b>Independent <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> content</b>
			</xsl:with-param>
			<xsl:with-param name="lab_unification_content_desc">Only one set of content has to be built and learners enrolled to any classes in this learning solution will use that same set of content.</xsl:with-param>
			<xsl:with-param name="lab_independency_content_desc">Each class can have its own set of content and learners enrolled to a class will see the content specific to that class.</xsl:with-param>
			<xsl:with-param name="lab_course_compose"><xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">Exam</xsl:when>
				<xsl:otherwise>Course</xsl:otherwise>
				</xsl:choose> content comprises the following components:</xsl:with-param>
			<xsl:with-param name="lab_earlier_model">Module prerequisite</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">Restricted sequence of studying the online <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> content.</xsl:with-param>
			<xsl:with-param name="lab_notice">Important: You have to make the choice now and it cannot be revoked once the choice is confirmed.</xsl:with-param>
			<xsl:with-param name="lab_no_establish_type">The way to build this <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> content is not decided yet.</xsl:with-param>
			<xsl:with-param name="lab_click_establish_type">Please click here to decide the way <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">exam</xsl:when>
				<xsl:otherwise>course</xsl:otherwise>
				</xsl:choose> content is built.</xsl:with-param>
			<xsl:with-param name="lab_limit_order">Restricted sequence of studying the online <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> content.</xsl:with-param>
			<xsl:with-param name="lab_establish_content">If you want to build content, please go to the <br><b>Content</b> section of specific classes.</br></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_online_content"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_learning_mod"/>
		<xsl:param name="lab_learning_mod_desc"/>
		<xsl:param name="lab_content_schedule"/>
		<xsl:param name="lab_content_schedule_desc"/>
		<xsl:param name="lab_course_reminder"/>
		<xsl:param name="lab_course_reminder_desc"/>
		<xsl:param name="lab_announcement"/>
		<xsl:param name="lab_announcement_desc"/>
		<xsl:param name="lab_tracking_rpt"/>
		<xsl:param name="lab_tracking_rpt_desc"/>
		<xsl:param name="lab_quick_reference_setting"/>
		<xsl:param name="lab_quick_reference_setting_desc"/>
		<xsl:param name="lab_completion_cri"/>
		<xsl:param name="lab_completion_cri_desc"/>
		<xsl:param name="lab_course_result"/>
		<xsl:param name="lab_course_result_desc"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_score_scheme"/>
		<xsl:param name="lab_score_scheme_desc"/>
		<xsl:param name="lab_grad_term"/>
		<xsl:param name="lab_grad_term_desc"/>
		<xsl:param name="lab_cos_measure"/>
		<xsl:param name="lab_score_record"/>
		<xsl:param name="lab_attendence"/>
		<xsl:param name="lab_grad_record"/>
		<xsl:param name="lab_set_grad_term"/>
		<xsl:param name="lab_train_record"/>
		<xsl:param name="lab_establish_type"/>
		<xsl:param name="lab_unification_content"/>
		<xsl:param name="lab_independency_content"/>
		<xsl:param name="lab_unification_content_desc"/>
		<xsl:param name="lab_independency_content_desc"/>
		<xsl:param name="lab_course_compose"/>
		<xsl:param name="lab_earlier_model"/>
		<xsl:param name="lab_earlier_model_desc"/>
		<xsl:param name="lab_notice"/>
		<xsl:param name="lab_no_establish_type"/>
		<xsl:param name="lab_click_establish_type"/>
		<xsl:param name="lab_limit_order"/>
		<xsl:param name="lab_establish_content"/>
		<xsl:variable name="lab_g_create">
			<!-- 获取新增文字 -->
			<xsl:call-template name="get_lab">
				<xsl:with-param name="lab_title">create</xsl:with-param>
			</xsl:call-template>
			
			<!-- 获取需要创建的课程类型文字 -->
			<xsl:variable name="type_name">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title" select="$training_type"/>
				</xsl:call-template>
			</xsl:variable>
			
			<!-- 获取需要创建的课程类型文字替换成相应的大小写 -->	
			<xsl:value-of select="translate($type_name,$uppercase,$lowercase)"/>
		</xsl:variable>
		<!-- 合并文字成为标题-->
		<xsl:variable name="lab_g_create_ls">
			<xsl:value-of select="concat($lab_g_create,' - 第二步：选择内容模式')"></xsl:value-of>
		</xsl:variable>
		<xsl:variable name="parent_code">
			<xsl:choose>
				<xsl:when test="/applyeasy/meta/training_type = 'EXAM'">FTN_AMD_EXAM_MGT</xsl:when>
				<xsl:otherwise>FTN_AMD_ITM_COS_MAIN</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
			<xsl:with-param name="page_title"><xsl:value-of select="$lab_g_create"></xsl:value-of></xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_g_create_ls"/>
		</xsl:call-template>
		<xsl:call-template name="wb_utils_process">
			<xsl:with-param name="itm_type">CLASSROOM</xsl:with-param>
			<xsl:with-param name="cur_tabs">2</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
			<!--=================================== -->
			<xsl:call-template name="no_establish_type">
				<xsl:with-param name="lab_establish_type" select="$lab_establish_type"/>
				<xsl:with-param name="lab_unification_content" select="$lab_unification_content"/>
				<xsl:with-param name="lab_independency_content" select="$lab_independency_content"/>
				<xsl:with-param name="lab_unification_content_desc" select="$lab_unification_content_desc"/>
				<xsl:with-param name="lab_independency_content_desc" select="$lab_independency_content_desc"/>
				<xsl:with-param name="lab_course_compose" select="$lab_course_compose"/>
				<xsl:with-param name="lab_learning_mod" select="$lab_learning_mod"/>
				<xsl:with-param name="lab_earlier_model" select="$lab_earlier_model"/>
				<xsl:with-param name="lab_notice" select="$lab_notice"/>
				<xsl:with-param name="lab_course_result" select="$lab_course_result"/>
				<xsl:with-param name="lab_completion_cri" select="$lab_completion_cri"/>
				<xsl:with-param name="lab_announcement" select="$lab_announcement"/>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="no_establish_type">
		<xsl:param name="lab_establish_type"/>
		<xsl:param name="lab_unification_content"/>
		<xsl:param name="lab_independency_content"/>
		<xsl:param name="lab_unification_content_desc"/>
		<xsl:param name="lab_independency_content_desc"/>
		<xsl:param name="lab_learning_mod"/>
		<xsl:param name="lab_earlier_model"/>
		<xsl:param name="lab_course_result"/>
		<xsl:param name="lab_completion_cri"/>
		<xsl:param name="lab_announcement"/>
		<xsl:param name="lab_course_compose"/>
		<xsl:param name="lab_notice"/>
		<table>
			<tr>
				<td height="10" colspan="2">
					<xsl:value-of select="$lab_establish_type"/>
				</td>
			</tr>
			<tr>
				<td width="50%">
					<input type="radio" name="itm_content_def" id="itm_content_def0" value="PARENT" onclick="changeContentDefPic()"/>
					<label for="itm_content_def0">
						<xsl:copy-of select="$lab_unification_content"/>
					</label>
				</td>
				<td width="50%">
					<input type="radio" name="itm_content_def" id="itm_content_def1" value="CHILD" onclick="changeContentDefPic()"/>
					<label for="itm_content_def1">
						<xsl:copy-of select="$lab_independency_content"/>
					</label>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img name="itm_content_def_pic0" src="{$wb_img_lang_path}content_level_cos.gif" onClick="document.frmXml.itm_content_def[0].checked=true;changeContentDefPic()"/>
				</td>
				<td align="center">
					<img name="itm_content_def_pic1" src="{$wb_img_lang_path}content_level_cls.gif" onClick="document.frmXml.itm_content_def[1].checked=true;changeContentDefPic()"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<xsl:value-of select="$lab_unification_content_desc"/>
				</td>
				<td valign="top">
					<xsl:value-of select="$lab_independency_content_desc"/>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td colspan="3">
					<xsl:value-of select="$lab_course_compose"/>
				</td>
			</tr>
			<tr>
				<td width="5%">
				</td>
				<td width="90%" valign="top">
					1. <xsl:value-of select="$lab_learning_mod"/>
				</td>
				<td width="5%">
				</td>
			</tr>
			<tr>
				<td width="5%">
				</td>
				<td width="90%" valign="top">
					2. <xsl:value-of select="$lab_earlier_model"/>
				</td>
				<td width="5%">
				</td>
			</tr>
			<tr>
				<td width="5%">
				</td>
				<td width="90%" valign="top">
					3. <xsl:value-of select="$lab_course_result"/>
				</td>
				<td width="5%">
				</td>
			</tr>
			<tr>
				<td width="5%">
				</td>
				<td width="90%" valign="top">
					4. <xsl:value-of select="$lab_completion_cri"/>
				</td>
				<td width="5%">
				</td>
			</tr>
			<tr>
				<td width="5%">
				</td>
				<td width="90%" valign="top">
					5. <xsl:value-of select="$lab_announcement"/>
				</td>
				<td width="5%">
				</td>
			</tr>
			<tr>
				<td height="10" colspan="3" class="TitleText">
					<i>
						<b>
							<xsl:value-of select="$lab_notice"/>
						</b>
					</i>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_next_btn"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:submitContentDef();</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
			</xsl:call-template>

			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$button_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
				<!-- <xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param> -->
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

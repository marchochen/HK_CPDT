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
	<!-- other -->
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="cos_id" select="/applyeasy/item/@cos_res_id"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_type" select="/applyeasy/item/@type"/>
	<xsl:variable name="itm_title" select="/applyeasy/item/title"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="itm_status" select="/applyeasy/item/@status"/>
	<xsl:variable name="turn_itm_status">
		<xsl:choose>
			<xsl:when test="$itm_status = 'OFF'">ON</xsl:when>
			<xsl:otherwise>OFF</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="create_run_ind" select="/applyeasy/item/@create_run_ind"/>
	<xsl:variable name="cur_month" select="/applyeasy/item/child_items/cur_time/@month"/>
	<xsl:variable name="cur_year" select="/applyeasy/item/child_items/cur_time/@year"/>
	<xsl:variable name="itm_type_list_root" select="/applyeasy/item/child_items/item_type_list"/>
	<xsl:variable name="itm_apply_ind" select="/applyeasy/item/@apply_ind"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="usr_id" select="/applyeasy/meta/cur_usr/@ent_id"/>
	<xsl:variable name="escaped_title">
		<xsl:call-template name="escape_js">
			<xsl:with-param name="input_str" select="/applyeasy/item/title"/>
		</xsl:call-template>
	</xsl:variable>
	<!-- 内容模式-->
	<xsl:variable name="label_core_training_management_314" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_314')" />
	<xsl:variable name="itm_content_def" select="/applyeasy/item/@content_def"/>
	<xsl:variable name="has_mod" select="/applyeasy/item/@has_mod"/>
	<xsl:variable name="is_new_cos">
		<xsl:choose>
			<xsl:when test="/applyeasy/is_new_cos/text() ='true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="lab_next_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '101')"/>
	<xsl:variable name="lab_ok_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '872')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy/item"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy/item">
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
					if (frmObj.content_def[0].checked) {
						document.content_def_pic0.border = 1;
						document.content_def_pic1.border = 0;
					}
					else
					if (frmObj.content_def[1].checked) {
						document.content_def_pic0.border = 0;
						document.content_def_pic1.border = 1;
					}
				}
				
				function submitContentDef(itm_id, res_id) {
					var frmObj = document.frmXml;
					frmObj.action = wb_utils_ae_servlet_url;
					frmObj.url_success.value =_wbItemGetItemDetailURL(itm_id);
					if (frmObj.content_def[0].checked || frmObj.content_def[1].checked) {
						if(frmObj.content_def[1].checked){
							frmObj.url_success.value = _wbItemGetItemDetailURL(itm_id);
						}
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
			<xsl:with-param name="lab_learning_mod">在線
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>学习</xsl:otherwise>
				</xsl:choose>内容
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
			<xsl:with-param name="lab_completion_cri">結訓條件設置</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">
				完成<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的條件。
			</xsl:with-param>
			<xsl:with-param name="lab_course_result">計分規則</xsl:with-param>
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
				</xsl:choose>日後所開班級的預設值。
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
				</xsl:choose>日後所開班級的預設值。</xsl:with-param>
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
				班級使用<b>統一的<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容</b>
			</xsl:with-param>
			<xsl:with-param name="lab_independency_content">
				班級使用<b>獨立的<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>線上學習</xsl:otherwise>
				</xsl:choose>內容</b>
			</xsl:with-param>
			<xsl:with-param name="lab_unification_content_desc">
				僅需建立一套內容，該<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>的所有班級的學員使用同一套內容。
			</xsl:with-param>
			<xsl:with-param name="lab_independency_content_desc">每個班級有其自己的一套內容，每個班級的學員只能看到其所在班級的內容。</xsl:with-param>
			<xsl:with-param name="lab_course_compose">
				<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容由以下部分組成：
			</xsl:with-param>
			<xsl:with-param name="lab_earlier_model">先修模塊設定</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">
				學習在線<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考試</xsl:when>
				<xsl:otherwise>課程</xsl:otherwise>
				</xsl:choose>內容的限制次序。
			</xsl:with-param>
			<xsl:with-param name="lab_notice">請注意：您現在必須作出選擇，同時請你一定要考慮清楚後再確認，一旦選擇被確認，它將不能被撤回。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確認並繼續建立內容</xsl:with-param>
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
			<xsl:with-param name="lab_establish_content">如果你要建立內容，請到特定班級的<b>線上學習內容</b>裡建立。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">网上学习内容</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_learning_mod">网上学习内容</xsl:with-param>
			<xsl:with-param name="lab_learning_mod_desc">网上学习内容。</xsl:with-param>
			<xsl:with-param name="lab_content_schedule">网上学习内容时间表</xsl:with-param>
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
			<xsl:with-param name="lab_completion_cri">结训条件设置</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">
				完成<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的条件。
			</xsl:with-param>
			<xsl:with-param name="lab_course_result">计分规则</xsl:with-param>
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
				</xsl:choose>日后所开班级的预设值。
			</xsl:with-param>
			<xsl:with-param name="lab_grad_term">结训条件</xsl:with-param>
			<xsl:with-param name="lab_grad_term_desc">
				*设定此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>的结训条件，这些设定将会成为此<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>日后所开班级的预设值。
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
				<xsl:otherwise>网上学习</xsl:otherwise>
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
			<xsl:with-param name="lab_earlier_model">先修模块设置</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">
				学习在线<xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">考试</xsl:when>
				<xsl:otherwise>课程</xsl:otherwise>
				</xsl:choose>内容的限制次序。
			</xsl:with-param>
			<xsl:with-param name="lab_notice">请注意：您现在必须作出选择，同时请你一定要考虑清楚后再确认，一旦选择被确认，它将不能被撤回。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确认并继续建立内容</xsl:with-param>
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
			<xsl:with-param name="lab_establish_content">如果您要建立内容，请到特定班级的<b>网上学习内容</b>里建立。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">Content</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_learning_mod">Online content</xsl:with-param>
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
			<xsl:with-param name="lab_completion_cri">Completion criteria settings</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">Condition for completing this <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> solution.</xsl:with-param>
			<xsl:with-param name="lab_course_result">Scoring record</xsl:with-param>
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
				<xsl:otherwise>online</xsl:otherwise>
				</xsl:choose> content</b>
			</xsl:with-param>
			<xsl:with-param name="lab_unification_content_desc">Only one set of content has to be built and learners enrolled to any classes in this learning solution will use that same set of content.</xsl:with-param>
			<xsl:with-param name="lab_independency_content_desc">Each class can have its own set of content and learners enrolled to a class will see the content specific to that class.</xsl:with-param>
			<xsl:with-param name="lab_course_compose"><xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">Exam</xsl:when>
				<xsl:otherwise>Course</xsl:otherwise>
				</xsl:choose> content comprises the following components:</xsl:with-param>
			<xsl:with-param name="lab_earlier_model">Prerequisite settings</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">Restricted sequence of studying the online <xsl:choose>
				<xsl:when test="//item/@exam_ind='true'">examination</xsl:when>
				<xsl:otherwise>learning</xsl:otherwise>
				</xsl:choose> content.</xsl:with-param>
			<xsl:with-param name="lab_notice">Important: You have to make the choice now and it cannot be revoked once the choice is confirmed.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">Confirm and proceed to build content</xsl:with-param>
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
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_no_establish_type"/>
		<xsl:param name="lab_click_establish_type"/>
		<xsl:param name="lab_limit_order"/>
		<xsl:param name="lab_establish_content"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>        
		<xsl:if test="$is_new_cos = 'false'">
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">05</xsl:with-param>
			<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
		</xsl:call-template>
		</xsl:if>
        <div class="wzb-item-main">
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:value-of select="//itm_action_nav/@itm_title"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
							<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
								<xsl:value-of select="//itm_action_nav/@itm_title"/>
							</a>
						</xsl:when>
						<xsl:when test="/applyeasy/item/@run_ind = 'false'">
							<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
								<xsl:value-of select="/applyeasy/item/title"/>
							</a>
							
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
								<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
								<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
							</xsl:apply-templates>
							
						</xsl:otherwise>
					</xsl:choose>
					<span class="NavLink">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$label_core_training_management_314"/>
					</span>
					
				</xsl:with-param>
			</xsl:call-template>
			<!--=================================== -->
			<xsl:choose>
				<xsl:when test="/applyeasy/item/@type='CLASSROOM' and $itm_content_def = ''">
					<xsl:choose>
						<xsl:when test="/applyeasy/item/@create_run_ind = 'true'">
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
								<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
								<xsl:with-param name="lab_course_result" select="$lab_course_result"/>
								<xsl:with-param name="lab_completion_cri" select="$lab_completion_cri"/>
								<xsl:with-param name="lab_announcement" select="$lab_announcement"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="/applyeasy/item/@create_run_ind = 'false'">
							<table>
								<tr>
									<td>
										<xsl:value-of select="$lab_no_establish_type"/>
									</td>
								</tr>
								<tr>
									<td height="10">
									</td>
								</tr>
								<tr>
									<td>
										<a href="javascript:itm_lst.ae_get_online_content_info({/applyeasy/item/nav/item/@id})" class="TitleText">
											<xsl:value-of select="$lab_click_establish_type"/>
										</a>
									</td>
								</tr>
							</table>
						</xsl:when>
					</xsl:choose>
				</xsl:when>
				<xsl:otherwise>
					<table>
						<tr>
							<td width="5%">
							</td>
							<xsl:choose>
								<xsl:when test="/applyeasy/item/@type='CLASSROOM' and /applyeasy/item/@create_run_ind = 'true' and $itm_content_def = 'CHILD' ">
									<td valign="top" width="45%">
										<xsl:copy-of select="$lab_establish_content"/>
									</td>
								</xsl:when>
								<xsl:otherwise>
								
								</xsl:otherwise>
							</xsl:choose>
							<td valign="top" width="48%">
								<xsl:choose>
									<xsl:when test="$itm_content_def ='PARENT'">
										<xsl:call-template name="show_content_def_img">
											<xsl:with-param name="lab_unification_content" select="$lab_unification_content"/>
											<xsl:with-param name="lab_unification_content_desc" select="$lab_unification_content_desc"/>
											<xsl:with-param name="picture" select="concat($wb_img_lang_path, 'content_level_cos.gif')"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:when test="$itm_content_def ='CHILD'">
										<xsl:call-template name="show_content_def_img">
											<xsl:with-param name="lab_unification_content" select="$lab_independency_content"/>
											<xsl:with-param name="lab_unification_content_desc" select="$lab_independency_content_desc"/>
											<xsl:with-param name="picture" select="concat($wb_img_lang_path, 'content_level_cls.gif')"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="show_content_def_img"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
					</table>
				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_link">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="location"/>
		<tr>
			<td valign="top">
				<dl>
					<dt>
						<a class="TitleText" href="{$location}">
							<xsl:value-of select="$lab_title"/>
						</a>
						<br/>
						<xsl:value-of select="$lab_content"/>
						<br/>
						<br/>
					</dt>
				</dl>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="show_content_link">
		<xsl:param name="lab_learning_mod"/>
		<xsl:param name="lab_learning_mod_desc"/>
		<xsl:param name="lab_course_result"/>
		<xsl:param name="lab_course_result_desc"/>
		<xsl:param name="lab_completion_cri"/>
		<xsl:param name="lab_completion_cri_desc"/>
		<xsl:param name="lab_announcement"/>
		<xsl:param name="lab_announcement_desc"/>
		<xsl:param name="lab_earlier_model"/>
		<xsl:param name="lab_earlier_model_desc"/>
		<table>
			<xsl:call-template name="draw_link">
				<xsl:with-param name="lab_title" select="$lab_learning_mod"/>
				<xsl:with-param name="location">javascript:course_lst.edit_cos('<xsl:value-of select="$cos_id"/>','<xsl:value-of select="$itm_type"/>','<xsl:value-of select="$create_run_ind"/>','<xsl:value-of select="$itm_content_def"/>','<xsl:value-of select="$has_mod"/>')</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_learning_mod_desc"/>
			</xsl:call-template>
			<xsl:if test="$itm_type != 'VIDEO' and $itm_type != 'MOBILE'">
				<xsl:call-template name="draw_link">
					<xsl:with-param name="lab_title" select="$lab_earlier_model"/>
					<xsl:with-param name="location">javascript:itm_lst.get_mod_pre(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
					<xsl:with-param name="lab_content" select="$lab_earlier_model_desc"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:call-template name="draw_link">
				<xsl:with-param name="lab_title" select="$lab_course_result"/>
				<xsl:with-param name="location">javascript:cmt.get_score_scheme_list(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_course_result_desc"/>
			</xsl:call-template>
			<xsl:call-template name="draw_link">
				<xsl:with-param name="lab_title" select="$lab_completion_cri"/>
				<xsl:with-param name="location">javascript:cmt.get_criteria('<xsl:value-of select="$itm_id"/>');</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_completion_cri_desc"/>
			</xsl:call-template>
			<xsl:call-template name="draw_link">
				<xsl:with-param name="lab_title" select="$lab_announcement"/>
				<xsl:with-param name="location">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_id"/>','','','','','',true)</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_announcement_desc"/>
			</xsl:call-template>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="show_content_def_img">
		<xsl:param name="lab_unification_content"/>
		<xsl:param name="lab_unification_content_desc"/>
		<xsl:param name="picture"><xsl:value-of select="$wb_img_path"/>tp.gif</xsl:param>
		<table>
			<tr>
				<td class="Text" valign="top">
					<xsl:copy-of select="$lab_unification_content"/>
				</td>
			</tr>
			<tr>
				<td>
					<img src="{$picture}"/>
				</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="$lab_unification_content_desc"/>
				</td>
			</tr>
		</table>
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
		<xsl:param name="lab_g_form_btn_ok"/>
		
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
			
		</table>
		<xsl:call-template name="wb_ui_space"/>
		<table>
			<tr>
				<td height="10" colspan="2">
					<xsl:value-of select="$lab_establish_type"/>
				</td>
			</tr>
			<tr>
				<td width="50%">
					<input type="radio" name="content_def" id="content_def0" value="PARENT" onclick="changeContentDefPic()"/>
					<label for="content_def0">
						<xsl:copy-of select="$lab_unification_content"/>
					</label>
				</td>
				<td width="50%">
					<input type="radio" name="content_def" id="content_def1" value="CHILD" onclick="changeContentDefPic()"/>
					<label for="content_def1">
						<xsl:copy-of select="$lab_independency_content"/>
					</label>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img name="content_def_pic0" src="{$wb_img_lang_path}content_level_cos.gif" onClick="document.frmXml.content_def[0].checked=true;changeContentDefPic()"/>
				</td>
				<td align="center">
					<img name="content_def_pic1" src="{$wb_img_lang_path}content_level_cls.gif" onClick="document.frmXml.content_def[1].checked=true;changeContentDefPic()"/>
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
			
			
			<tr>
				<td height="10" colspan="3" class="TitleText">
					<i>
						<b>
						    <span class = "wzb-ui-warn-text">
						        <br/>
								<xsl:value-of select="$lab_notice"/>
							</span>
						</b>
					</i>
				</td>
			</tr>
		</table>
		
		<xsl:choose>
			<xsl:when test="$is_new_cos='true'">
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_next_btn"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:submitContentDef('<xsl:value-of select="$itm_id"/>', '<xsl:value-of select="$cos_id"/>','<xsl:value-of select="$itm_type"/>','<xsl:value-of select="$create_run_ind"/>','<xsl:value-of select="$itm_content_def"/>','<xsl:value-of select="$has_mod"/>');</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
					</xsl:call-template>
		
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_ok_btn"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
					</xsl:call-template>
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:submitContentDef('<xsl:value-of select="$itm_id"/>', '<xsl:value-of select="$cos_id"/>','<xsl:value-of select="$itm_type"/>','<xsl:value-of select="$create_run_ind"/>','<xsl:value-of select="$itm_content_def"/>','<xsl:value-of select="$has_mod"/>')</xsl:with-param>
					</xsl:call-template>
					
					
				</div>
			</xsl:otherwise>
		</xsl:choose>
		<input type="hidden" name="cmd" value="ae_set_content_def"/>
		<input type="hidden" name="itm_id" value="{$itm_id}"/>
		<input type="hidden" name="url_success" value=""/>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

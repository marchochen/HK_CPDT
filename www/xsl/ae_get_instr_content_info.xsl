<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<!-- other -->
	<!--<xsl:import href="share/itm_gen_details_share.xsl"/>-->
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
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_mote.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				crit = new wbCriteria
				itm_lst = new wbItem
				ann = new wbAnnouncement
				course_lst = new wbCourse	
				rpt = new wbReport
				mote = new wbMote
				app = new wbApplication
				attn = new wbAttendance
				cmt = new wbScoreScheme;
				itm_lst = new wbItem			
				
				window.onunload = unloadHandler;
				function unloadHandler(){
					wb_utils_set_cookie('lrn_soln_itm_title','')
				}
				
				function init(){
					wb_utils_set_cookie('lrn_soln_itm_title',']]><xsl:value-of select="$escaped_title"/><![CDATA[')
				}
			]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="javascript:init()">
				<form name="frmXml">
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
			<xsl:with-param name="lab_learning_mod">課程單元</xsl:with-param>
			<xsl:with-param name="lab_learning_mod_desc">在線學習内容。</xsl:with-param>
			<xsl:with-param name="lab_content_schedule">線上內容時間表</xsl:with-param>
			<xsl:with-param name="lab_content_schedule_desc">指定線上內容學習的開始和結束日期。</xsl:with-param>
			<xsl:with-param name="lab_course_reminder">課程提示</xsl:with-param>
			<xsl:with-param name="lab_course_reminder_desc">指定觸發提示的時間及條件。</xsl:with-param>
			<xsl:with-param name="lab_announcement">通告</xsl:with-param>
			<xsl:with-param name="lab_announcement_desc">只對已報名學員發佈的公共資訊。</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">查看用戶學習報告。</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting">快速參考設置</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting_desc">指定學員是否可以在不進行課程報名的情況下預覽課程內容</xsl:with-param>
			<xsl:with-param name="lab_completion_cri">結訓條件</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">完成課程的條件。</xsl:with-param>
			<xsl:with-param name="lab_course_result">計分項目</xsl:with-param>
			<xsl:with-param name="lab_course_result_desc">可以計算分數的在線/離線學習内容。</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			
			<xsl:with-param name="lab_instr_lst">我的教學課程</xsl:with-param>
			<xsl:with-param name="lab_score_scheme">計分項目</xsl:with-param>
			<xsl:with-param name="lab_score_scheme_desc">*設定此課程的計分項目以及其計分方法，這些設定將成爲此課程日後所開班別的預設值。</xsl:with-param>
			<xsl:with-param name="lab_grad_term">結訓條件</xsl:with-param>
			<xsl:with-param name="lab_grad_term_desc">*設定此課程的結訓條件，這些設定將會成爲此課程日後所開班別的預設值。</xsl:with-param>
			<xsl:with-param name="lab_cos_measure">考勤</xsl:with-param>
			<xsl:with-param name="lab_score_record">成績記錄</xsl:with-param>
			<xsl:with-param name="lab_attendence">出席率</xsl:with-param>
			<xsl:with-param name="lab_grad_record">結訓記錄</xsl:with-param>
			<xsl:with-param name="lab_set_grad_term">結訓條件設定</xsl:with-param>
			<xsl:with-param name="lab_train_record">受訓記錄</xsl:with-param>
				<xsl:with-param name="lab_earlier_model">先修模塊</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">學習在線課程內容的限制次序。</xsl:with-param>
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
			<xsl:with-param name="lab_course_reminder">课程提示</xsl:with-param>
			<xsl:with-param name="lab_course_reminder_desc">指定触发提示的时间及条件。</xsl:with-param>
			<xsl:with-param name="lab_announcement">公告</xsl:with-param>
			<xsl:with-param name="lab_announcement_desc">只对已报名学员发布的公共信息。</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">查看用户学习报告。</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting">快速参考设置</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting_desc">指定学员是否可以在不进行课程报名的情况下预览课程内容</xsl:with-param>
			<xsl:with-param name="lab_completion_cri">结训条件</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">完成课程的条件。</xsl:with-param>
			<xsl:with-param name="lab_course_result">计分项目</xsl:with-param>
			<xsl:with-param name="lab_course_result_desc">可以计算分数的在线/离线学习内容。</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_instr_lst">我的教学课程</xsl:with-param>
			
			<xsl:with-param name="lab_score_scheme">计分项目</xsl:with-param>
			<xsl:with-param name="lab_score_scheme_desc">*设定此课程的计分项目以及其计分项目，这些设定将会成为此课程日后所开班别的预设值。</xsl:with-param>
			<xsl:with-param name="lab_grad_term">结训条件</xsl:with-param>
			<xsl:with-param name="lab_grad_term_desc">*设定此课程的结训条件，这些设定将会成为此课程日后所开班别的预设值。</xsl:with-param>
			<xsl:with-param name="lab_cos_measure">考勤</xsl:with-param>
			<xsl:with-param name="lab_score_record">成绩记录</xsl:with-param>
			<xsl:with-param name="lab_attendence">出席率</xsl:with-param>
			<xsl:with-param name="lab_grad_record">结训记录</xsl:with-param>
			<xsl:with-param name="lab_set_grad_term">结训条件设定</xsl:with-param>
			<xsl:with-param name="lab_train_record">受训记录</xsl:with-param>
			<xsl:with-param name="lab_earlier_model">先修模块</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">学习在线课程内容的限制次序。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_online_content">Content</xsl:with-param>
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/> information</xsl:with-param>
			<xsl:with-param name="lab_learning_mod">Learning module</xsl:with-param>
			<xsl:with-param name="lab_learning_mod_desc">Online learning content.</xsl:with-param>
			<xsl:with-param name="lab_content_schedule">Online content schedule</xsl:with-param>
			<xsl:with-param name="lab_content_schedule_desc">Specify the overall start and end dates for the online content.</xsl:with-param>
			<xsl:with-param name="lab_course_reminder">Course reminder</xsl:with-param>
			<xsl:with-param name="lab_course_reminder_desc">Specify the schedule and the conditions that trigger the course reminders.</xsl:with-param>
			<xsl:with-param name="lab_announcement">Notice</xsl:with-param>
			<xsl:with-param name="lab_announcement_desc">Public message available only to enrolled learners.</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_tracking_rpt_desc">Run the tracking reports of user activity statistics.</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting">Quick reference settings</xsl:with-param>
			<xsl:with-param name="lab_quick_reference_setting_desc">Specify whether learners can preview the course content without enrolling in the course.</xsl:with-param>
			<xsl:with-param name="lab_completion_cri">Completion criteria</xsl:with-param>
			<xsl:with-param name="lab_completion_cri_desc">Condition for completing this learning solution.</xsl:with-param>
			<xsl:with-param name="lab_course_result">Scoring item</xsl:with-param>
			<xsl:with-param name="lab_course_result_desc">Offline/online learning content that will contribute score.</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>

			<xsl:with-param name="lab_score_scheme">Scoring item</xsl:with-param>
			<xsl:with-param name="lab_score_scheme_desc">*Set scoring item of the course and its scoring scheme. This setting will be used as the default value in the classes of this course created thereafter.</xsl:with-param>
			<xsl:with-param name="lab_grad_term">Completion criteria</xsl:with-param>
			<xsl:with-param name="lab_grad_term_desc">*Set completion criteria of the course. This setting will be used as the default value in the classes of this course created thereafter.</xsl:with-param>
			<xsl:with-param name="lab_score_record">Score</xsl:with-param>
			<xsl:with-param name="lab_attendence">Attendance</xsl:with-param>
			<xsl:with-param name="lab_grad_record">Result</xsl:with-param>
			<xsl:with-param name="lab_cos_measure">Result</xsl:with-param>
			<xsl:with-param name="lab_set_grad_term">Configurations</xsl:with-param>
			<xsl:with-param name="lab_train_record">Learner's results</xsl:with-param>
			<xsl:with-param name="lab_instr_lst">My teaching courses</xsl:with-param>
			<xsl:with-param name="lab_earlier_model">Module prerequisite</xsl:with-param>
			<xsl:with-param name="lab_earlier_model_desc">Restricted sequence of studying the online learning content.</xsl:with-param>
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
		<xsl:param name="lab_instr_lst"/>
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
		<xsl:param name="lab_earlier_model"/>
		<xsl:param name="lab_earlier_model_desc"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_TEACHING_COURSE_LIST</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="/applyeasy/item/title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
						<a href="javascript:wb_utils_nav_go('FTN_AMD_TEACHING_COURSE_LIST');" class="NavLink">
							<xsl:value-of select="$lab_instr_lst"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:choose>
							<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
								<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
									<xsl:value-of select="//itm_action_nav/@itm_title"/>
								</a>
							</xsl:when>
							<xsl:when test="/applyeasy/item/@run_ind = 'false'">
								<a href="javascript:itm_lst.get_itm_instr_view({$itm_id})" class="NavLink">
									<xsl:value-of select="/applyeasy/item/title"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:itm_lst.get_itm_instr_view({$itm_id})" class="NavLink">
									<xsl:for-each select="/applyeasy/item/nav/item">
										<xsl:value-of select="title"/>
										<xsl:if test="position() != count(/applyeasy/item/nav/item)">
											<xsl:text> - </xsl:text>
										</xsl:if>
									</xsl:for-each>
								</a>
							</xsl:otherwise>
						</xsl:choose>
			
						<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_content"/>
						</span>
			</xsl:with-param>
		</xsl:call-template>
		<table>
			<xsl:if test="/applyeasy/item/@qdb_ind = 'true'">
				<xsl:call-template name="template">
					<xsl:with-param name="lab_title" select="$lab_learning_mod"/>
					<xsl:with-param name="lab_link">javascript:course_lst.edit_cos(<xsl:value-of select="$cos_id"/>)</xsl:with-param>
					<xsl:with-param name="lab_content" select="$lab_learning_mod_desc"/>
				</xsl:call-template>
			</xsl:if>
			
			<xsl:if test="/applyeasy/item/@qdb_ind = 'true'">
				<xsl:call-template name="template">
					<xsl:with-param name="lab_title" select="$lab_earlier_model"/>
					<xsl:with-param name="lab_link">javascript:itm_lst.get_mod_pre(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
					<xsl:with-param name="lab_content" select="$lab_earlier_model_desc"/>
				</xsl:call-template>
			</xsl:if>

			<!-- todo -->
			<xsl:if test="/applyeasy/item/item_type_meta/@reminder_criteria_ind = 'true'">
				<xsl:call-template name="template">
					<xsl:with-param name="lab_title" select="$lab_course_reminder"/>
					<xsl:with-param name="lab_link">javascript:crit.get_reminder_mod_lst('<xsl:value-of select="$itm_id"/>','',"reminder_crit_lst", 'notification')</xsl:with-param>
					<xsl:with-param name="lab_content" select="$lab_course_reminder_desc"/>
				</xsl:call-template>
			</xsl:if>
			<xsl:call-template name="template">
				<xsl:with-param name="lab_title" select="$lab_course_result"/>
				<xsl:with-param name="lab_link">javascript:cmt.get_score_scheme_list(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_course_result_desc"/>
			</xsl:call-template>
			<xsl:call-template name="template">
				<xsl:with-param name="lab_title" select="$lab_completion_cri"/>
				<xsl:with-param name="lab_link">javascript:cmt.get_criteria('<xsl:value-of select="$itm_id"/>');</xsl:with-param>
				<xsl:with-param name="lab_content" select="$lab_completion_cri_desc"/>
			</xsl:call-template>
			<xsl:if test="/applyeasy/item/@qdb_ind = 'true'">
				<xsl:call-template name="template">
					<xsl:with-param name="lab_title" select="$lab_announcement"/>
					<xsl:with-param name="lab_link">javascript:ann.sys_lst('all','RES','<xsl:value-of select="$cos_id"/>','','','','','',true)</xsl:with-param>
					<xsl:with-param name="lab_content" select="$lab_announcement_desc"/>
				</xsl:call-template>
			</xsl:if>
			<!--<xsl:if test="/applyeasy/item/@qdb_ind = 'true'">
				<xsl:call-template name="template">
					<xsl:with-param name="lab_title" select="$lab_tracking_rpt"/>
					<xsl:with-param name="lab_link">javascript:rpt.open_cos_lrn_lst(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
					<xsl:with-param name="lab_content" select="$lab_tracking_rpt_desc"/>
				</xsl:call-template>
			</xsl:if>-->
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="template">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_link"/>
		<tr>
			<td width="5%">
			</td>
			<td width="90%" valign="top">
				<dl>
					<dt>
						<a class="TitleText" href="{$lab_link}">
							<xsl:value-of select="$lab_title"/>
						</a>
						<br/>
						<span class="Text">
							<xsl:value-of select="$lab_content"/>
						</span>
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
</xsl:stylesheet>

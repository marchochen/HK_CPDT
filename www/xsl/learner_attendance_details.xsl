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
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl "/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_img_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_score.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<!-- other -->
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- 需浏览 -->
	<xsl:variable name="label_core_training_management_271" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_271')" />
	<!-- 需合格 -->
	<xsl:variable name="label_core_training_management_272" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_272')" />
	<!-- 需參與 -->
	<xsl:variable name="label_core_training_management_273" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_273')" />
	<!-- 需提交 -->
	<xsl:variable name="label_core_training_management_274" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_274')" />
	<!-- 需完成 -->
	<xsl:variable name="label_core_training_management_383" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_383')" />
	<!--==========================================================================================-->
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="JavaScript" TYPE="text/javascript"><![CDATA[
		  itm_lst = new wbItem;
		  attd = new wbAttendance;
				]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmXml">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ===================================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="applyeasy">
			<xsl:with-param name="lab_report_card">結訓紀錄</xsl:with-param>
			<xsl:with-param name="lab_basic_information">基本資料</xsl:with-param>
			<xsl:with-param name="lab_training_record">結訓紀錄</xsl:with-param>
			<xsl:with-param name="lab_course">課程名稱</xsl:with-param>
			<xsl:with-param name="lab_class">班級名稱</xsl:with-param>
			<xsl:with-param name="lab_status">結訓記錄</xsl:with-param>
			<xsl:with-param name="lab_last_modified_date">修改日期</xsl:with-param>
			<xsl:with-param name="lab_last_modified_by">修改者</xsl:with-param>
			<xsl:with-param name="lab_completion_date">結訓日期</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_attendance">出席率</xsl:with-param>
			<xsl:with-param name="lab_fulfill_other_criteria">其他條件</xsl:with-param>
			<xsl:with-param name="lab_item_description">項目名稱</xsl:with-param>
			<xsl:with-param name="lab_criteria">條件</xsl:with-param>
			<xsl:with-param name="lab_fulfill_the_criteria">已滿足</xsl:with-param>
			<xsl:with-param name="lab_online_module">線上模塊</xsl:with-param>
			<xsl:with-param name="lab_scoring_item">計分項目</xsl:with-param>
			<xsl:with-param name="lab_btn_return">返回</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_cond_0">需閱讀一段培訓資料</xsl:with-param>
			<xsl:with-param name="lab_cond_1">需瀏覽一個課件</xsl:with-param>
			<xsl:with-param name="lab_cond_2">需完成一個課件/已取得一個課件合格分數</xsl:with-param>
			<xsl:with-param name="lab_cond_3">需遞交一項作業</xsl:with-param>
			<xsl:with-param name="lab_cond_4">需遞交一份課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_cond_5">需參與一個討論區</xsl:with-param>
			<xsl:with-param name="lab_cond_6">需參與一個聊天室</xsl:with-param>
			<xsl:with-param name="lab_cond_7">需參與一個解答欄</xsl:with-param>
			<xsl:with-param name="lab_scoring_cond_0">需遞交/考過</xsl:with-param>
			<xsl:with-param name="lab_scoring_cond_1">需合格</xsl:with-param>
			<xsl:with-param name="lab_cond_none">--</xsl:with-param>
			<xsl:with-param name="lab_btn_modify">更改結訓日期與備註</xsl:with-param>
			<xsl:with-param name="lab_run_info">班級信息</xsl:with-param>
			<xsl:with-param name="lab_performance">成績</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="applyeasy">
			<xsl:with-param name="lab_report_card">结训纪录</xsl:with-param>
			<xsl:with-param name="lab_basic_information">基本资料</xsl:with-param>
			<xsl:with-param name="lab_training_record">结训纪录</xsl:with-param>
			<xsl:with-param name="lab_course">课程名称</xsl:with-param>
			<xsl:with-param name="lab_class">班级名称</xsl:with-param>
			<xsl:with-param name="lab_status">结训记录</xsl:with-param>
			<xsl:with-param name="lab_last_modified_date">修改日期</xsl:with-param>
			<xsl:with-param name="lab_last_modified_by">修改者</xsl:with-param>
			<xsl:with-param name="lab_completion_date">结训日期</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_attendance">出席率</xsl:with-param>
			<xsl:with-param name="lab_fulfill_other_criteria">其他条件</xsl:with-param>
			<xsl:with-param name="lab_item_description">项目名称</xsl:with-param>
			<xsl:with-param name="lab_criteria">条件</xsl:with-param>
			<xsl:with-param name="lab_fulfill_the_criteria">已满足</xsl:with-param>
			<xsl:with-param name="lab_online_module">线上模块</xsl:with-param>
			<xsl:with-param name="lab_scoring_item">记分项目</xsl:with-param>
			<xsl:with-param name="lab_btn_return">返回</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_cond_0">需阅读一段培训资料</xsl:with-param>
			<xsl:with-param name="lab_cond_1">需浏览一个课件</xsl:with-param>
			<xsl:with-param name="lab_cond_2">需完成一个课件/已取得一个课件合格分数</xsl:with-param>
			<xsl:with-param name="lab_cond_3">需递交一项功课</xsl:with-param>
			<xsl:with-param name="lab_cond_4">需递交一份课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_cond_5">需参与一个讨论区</xsl:with-param>
			<xsl:with-param name="lab_cond_6">需参与一个聊天室</xsl:with-param>
			<xsl:with-param name="lab_cond_7">需参与一个解答栏</xsl:with-param>
			<xsl:with-param name="lab_scoring_cond_0">需递交/考过</xsl:with-param>
			<xsl:with-param name="lab_scoring_cond_1">需合格</xsl:with-param>
			<xsl:with-param name="lab_cond_none">--</xsl:with-param>
			<xsl:with-param name="lab_btn_modify">更改结训日期与备注</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_performance">成绩</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="applyeasy">
			<xsl:with-param name="lab_report_card">Completion result</xsl:with-param>
			<xsl:with-param name="lab_basic_information">Basic information</xsl:with-param>
			<xsl:with-param name="lab_training_record">Training record</xsl:with-param>
			<xsl:with-param name="lab_course">Course</xsl:with-param>
			<xsl:with-param name="lab_class">Class</xsl:with-param>
			<xsl:with-param name="lab_status">Learning status</xsl:with-param>
			<xsl:with-param name="lab_last_modified_date">Modified</xsl:with-param>
			<xsl:with-param name="lab_last_modified_by">Modified by</xsl:with-param>
			<xsl:with-param name="lab_completion_date">Completion date</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_attendance">Attendance rate</xsl:with-param>
			<xsl:with-param name="lab_fulfill_other_criteria">Other criteria</xsl:with-param>
			<xsl:with-param name="lab_item_description">Item description</xsl:with-param>
			<xsl:with-param name="lab_criteria">Criteria</xsl:with-param>
			<xsl:with-param name="lab_fulfill_the_criteria">Fulfilled</xsl:with-param>
			<xsl:with-param name="lab_online_module">Online module</xsl:with-param>
			<xsl:with-param name="lab_scoring_item">Scoring item</xsl:with-param>
			<xsl:with-param name="lab_btn_return">Back</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_cond_0">Viewed a training document/video clip</xsl:with-param>
			<xsl:with-param name="lab_cond_1">Attempted an eLearning courseware</xsl:with-param>
			<xsl:with-param name="lab_cond_2">Completed/passed an eLearning courseware</xsl:with-param>
			<xsl:with-param name="lab_cond_3">Submitted an assignment</xsl:with-param>
			<xsl:with-param name="lab_cond_4">Submitted a course evaluation form</xsl:with-param>
			<xsl:with-param name="lab_cond_5">Participated in a forum</xsl:with-param>
			<xsl:with-param name="lab_cond_6">Participated in a chatroom</xsl:with-param>
			<xsl:with-param name="lab_cond_7">Particpated in a Q&#38;A discussion</xsl:with-param>
			<xsl:with-param name="lab_scoring_cond_0">Attempted the item</xsl:with-param>
			<xsl:with-param name="lab_scoring_cond_1">Passed the item</xsl:with-param>
			<xsl:with-param name="lab_cond_none">--</xsl:with-param>
			<xsl:with-param name="lab_btn_modify">Modify completion date and remark</xsl:with-param>
			<xsl:with-param name="lab_run_info">Class information</xsl:with-param>
			<xsl:with-param name="lab_performance">Performance</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ===================================================================================== -->
	<xsl:template match="applyeasy">
		<xsl:param name="lab_report_card"/>
		<xsl:param name="lab_basic_information"/>
		<xsl:param name="lab_training_record"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_class"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_last_modified_date"/>
		<xsl:param name="lab_last_modified_by"/>
		<xsl:param name="lab_completion_date"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_attendance"/>
		<xsl:param name="lab_fulfill_other_criteria"/>
		<xsl:param name="lab_item_description"/>
		<xsl:param name="lab_criteria"/>
		<xsl:param name="lab_fulfill_the_criteria"/>
		<xsl:param name="lab_online_module"/>
		<xsl:param name="lab_scoring_item"/>
		<xsl:param name="lab_btn_return"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_cond_0"/>
		<xsl:param name="lab_cond_1"/>
		<xsl:param name="lab_cond_2"/>
		<xsl:param name="lab_cond_3"/>
		<xsl:param name="lab_cond_4"/>
		<xsl:param name="lab_cond_5"/>
		<xsl:param name="lab_cond_6"/>
		<xsl:param name="lab_cond_7"/>
		<xsl:param name="lab_scoring_cond_0"/>
		<xsl:param name="lab_scoring_cond_1"/>
		<xsl:param name="lab_cond_none"/>
		<xsl:param name="lab_btn_modify"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_performance"/>
		<xsl:variable name="itm_id" select="item/@id"/>
		<xsl:variable name="app_id" select="attendance/@app_id"/>
		<xsl:variable name="att_status" select="attendance/@status"/>
		<xsl:variable name="is_integrated" select="item/@itm_integrated_ind"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
		</xsl:call-template>
		
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">113</xsl:with-param>
		</xsl:call-template>
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="/applyeasy/item/@run_ind = 'false'">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="/applyeasy/nav/item/title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:history.back()" class="NavLink">
							<xsl:value-of select="$lab_report_card"/>
						</a>
						<span class="NavLink">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="/applyeasy/attendance/user/name/@display_name"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="/applyeasy/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:history.back()" class="NavLink">
							<xsl:value-of select="$lab_report_card"/>
						</a>
						<span class="NavLink">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="/applyeasy/attendance/user/name/@display_name"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		
		<table>
			<tr>
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_modify"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:attd.chg_usr_status('<xsl:value-of select="attendance/@app_id"/>','<xsl:value-of select="item/@id"/>',<xsl:value-of select="attendance/@status"/>)</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<!-- =========== Basic Information ============ -->
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_basic_information"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<!-- Name -->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_dis_name"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="attendance/user/name/@display_name"/>
				</td>
			</tr>
			<!-- Course -->
			<xsl:if test="nav/item[position()=1]/@run_ind='false'">
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_course"/>：
					</td>
					<td class="wzb-form-control">
						<xsl:value-of select="nav/item[position()=1]/title"/>
					</td>
				</tr>
			</xsl:if>
			<!-- Class -->
			<xsl:if test="nav/item/following-sibling::item/@run_ind='true'">
				<tr>
					<td class="wzb-form-label">
						<xsl:value-of select="$lab_class"/>
						<xsl:text>：&#160;&#160;</xsl:text>
					</td>
					<td class="wzb-form-control">
						<a href="javascript:itm_lst.get_item_run_detail({nav/item/following-sibling::item/@id})" class="NavLink">
							<xsl:value-of select="nav/item/following-sibling::item/title"/>
						</a>
					</td>
				</tr>
			</xsl:if>
			<!-- Last modified date -->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_last_modified_date"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:choose>
						<xsl:when test="attendance/att_update_timestamp = ''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp" select="attendance/att_update_timestamp"/>
								<xsl:with-param name="dis_time">T</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!-- Last modified by -->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_last_modified_by"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="update_usr/user/name/@display_name"/>
				</td>
			</tr>
		</table>
		<!-- =========== Training Record ==============-->
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_training_record"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<!-- Status -->
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;">
					<xsl:value-of select="$lab_status"/>：
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:variable name="usr_attd_id">
						<xsl:value-of select="attendance/@status"/>
					</xsl:variable>
					<xsl:call-template name="get_ats_title">
						<xsl:with-param name="ats_id" select="$usr_attd_id"/>
					</xsl:call-template>
				</td>
			</tr>
			<!-- Completion date -->
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;">
					<xsl:value-of select="$lab_completion_date"/>：
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="attendance/@att_timestamp = ''">--</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="display_time">
								<xsl:with-param name="my_timestamp" select="attendance/@att_timestamp"/>
								<xsl:with-param name="dis_time">T</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!-- Score -->
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;">
					<xsl:value-of select="$lab_score"/>：
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="attendance/cov_score != ''">
							<xsl:call-template name="display_score">
								<xsl:with-param name="score" select="attendance/cov_score"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!-- Attendance -->
			<xsl:if test="//itm_action_nav/@itm_type = 'CLASSROOM'">
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;">
					<xsl:value-of select="$lab_attendance"/>：
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="attendance/@rate != ''">
							<xsl:call-template name="display_score">
								<xsl:with-param name="score" select="attendance/@rate"/>
							</xsl:call-template>%
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>--</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			</xsl:if>
			<!-- Fulfill other criteria -->
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;">
					<xsl:value-of select="$lab_fulfill_other_criteria"/>：
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:value-of select="$lab_item_description"/>
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:value-of select="$lab_criteria"/>
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:value-of select="$lab_fulfill_the_criteria"/>
				</td>
			</tr>
			<!-- Online module -->
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;">
					<xsl:value-of select="$lab_online_module"/>：
				</td>
				<xsl:if test="count(fulfill_other_criteria/online_module/item)=0">
					<td style="padding:10px 0 10px 10px; color:#333;">--</td>
					<td style="padding:10px 0 10px 10px; color:#333;">--</td>
					<td style="padding:10px 0 10px 10px; color:#333;">--</td>
				</xsl:if>
				<xsl:apply-templates select="fulfill_other_criteria/online_module/item " mode="criteria_online">
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
					<xsl:with-param name="lab_cond_0" select="$lab_cond_0"/>
					<xsl:with-param name="lab_cond_1" select="$lab_cond_1"/>
					<xsl:with-param name="lab_cond_2" select="$lab_cond_2"/>
					<xsl:with-param name="lab_cond_3" select="$lab_cond_3"/>
					<xsl:with-param name="lab_cond_4" select="$lab_cond_4"/>
					<xsl:with-param name="lab_cond_5" select="$lab_cond_5"/>
					<xsl:with-param name="lab_cond_6" select="$lab_cond_6"/>
					<xsl:with-param name="lab_cond_7" select="$lab_cond_7"/>
				</xsl:apply-templates>
			</tr>
			<!-- Scoring item -->
			<tr>
				<td style="padding:10px 0; text-align:right; color:#666;">
					<xsl:value-of select="$lab_scoring_item"/>：
				</td>
				<xsl:if test="count(fulfill_other_criteria/scoring_item/item)=0">
					<td style="padding:10px 0 10px 10px; color:#333;">--</td>
					<td style="padding:10px 0 10px 10px; color:#333;">--</td>
					<td style="padding:10px 0 10px 10px; color:#333;">--</td>
				</xsl:if>
				<xsl:apply-templates select="fulfill_other_criteria/scoring_item/item" mode="criteria">
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
					<xsl:with-param name="lab_cond_0" select="$lab_scoring_cond_0"/>
					<xsl:with-param name="lab_cond_1" select="$lab_scoring_cond_1"/>
					<xsl:with-param name="lab_cond_2" select="$lab_cond_none"/>
					<xsl:with-param name="lab_cond_3" select="$lab_cond_none"/>
					<xsl:with-param name="lab_cond_4" select="$lab_cond_none"/>
					<xsl:with-param name="lab_cond_5" select="$lab_cond_none"/>
					<xsl:with-param name="lab_cond_6" select="$lab_cond_none"/>
					<xsl:with-param name="lab_cond_7" select="$lab_cond_none"/>
				</xsl:apply-templates>
			</tr>
		</table>
	</div>
	</xsl:template>
	<!-- ========================================================================================= -->
	<xsl:template match="item" mode="criteria_online">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_cond_0"/>
		<xsl:param name="lab_cond_1"/>
		<xsl:param name="lab_cond_2"/>
		<xsl:param name="lab_cond_3"/>
		<xsl:param name="lab_cond_4"/>
		<xsl:param name="lab_cond_5"/>
		<xsl:param name="lab_cond_6"/>
		<xsl:param name="lab_cond_7"/>
		<xsl:choose>
			<xsl:when test="position()=1">
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:value-of select="title"/>
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
				<xsl:choose>
						<xsl:when test="@subtype = 'GLO'">
							<xsl:value-of select="$label_core_training_management_271"/>
						</xsl:when>
						<xsl:when test="@subtype = 'RDG' or @subtype = 'REF' or @subtype = 'VOD'">
							<xsl:choose>
								<xsl:when test="@status = 'IFCP'">
									<xsl:value-of select="$label_core_training_management_271"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_383"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@subtype = 'NETG_COK' or @subtype = 'SCO' or @subtype = 'AICC_AU'">
							<xsl:choose>
								<xsl:when test="@status = 'IFCP'">
									<xsl:value-of select="$label_core_training_management_271"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_383"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@subtype = 'DXT' or @subtype = 'ASS' or @subtype = 'TST'">
							<xsl:choose>
								<xsl:when test="@status = 'IFCP'">
									<xsl:value-of select="$label_core_training_management_274"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_272"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@subtype = 'SVY'">
							<xsl:choose>
								<xsl:when test="@status = 'CP'">
									<xsl:value-of select="$label_core_training_management_274"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_273"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@subtype = 'FOR' or @subtype = 'FAQ'">
							 <xsl:value-of select="$lab_cond_0"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="@status = 'IFCP'">
									<xsl:value-of select="$label_core_training_management_271"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_272"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="@status=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="fulfill_the_criteria='true'">
									<xsl:value-of select="$lab_yes"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_no"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<tr>
					<td>
					</td>
					<td style="padding:10px 0 10px 10px; color:#333;">
						<xsl:value-of select="title"/>
					</td>
					<td style="padding:10px 0 10px 10px; color:#333;">
						<xsl:choose>
							<xsl:when test=" @subtype = 'GLO'">
								<xsl:value-of select="$label_core_training_management_271"/>
							</xsl:when>
							<xsl:when test="@subtype = 'RDG' or @subtype = 'REF' or @subtype = 'VOD'">
							<xsl:choose>
								<xsl:when test="@status = 'IFCP'">
									<xsl:value-of select="$label_core_training_management_271"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_383"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
							<xsl:when test=" @subtype = 'NETG_COK' or @subtype = 'SCO'  or @subtype = 'AICC_AU'">
								<xsl:choose>
									<xsl:when test="@status = 'IFCP'">
										<xsl:value-of select="$label_core_training_management_271"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$label_core_training_management_383"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="@subtype = 'DXT' or @subtype = 'ASS'  or @subtype = 'TST'">
								<xsl:choose>
								<xsl:when test="@status = 'IFCP'">
									<xsl:value-of select="$label_core_training_management_274"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_272"/>
								</xsl:otherwise>
							</xsl:choose>
							</xsl:when>
							<xsl:when test="@subtype = 'SVY'">
								<xsl:choose>
									<xsl:when test="@status = 'CP'">
										<xsl:value-of select="$label_core_training_management_274"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$label_core_training_management_273"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="@subtype = 'FOR' or @subtype = 'FAQ'">
								 <xsl:value-of select="$lab_cond_0"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="@status = 'IFCP'">
										<xsl:value-of select="$label_core_training_management_271"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$label_core_training_management_272"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td style="padding:10px 0 10px 10px; color:#333;">
						<xsl:choose>
							<xsl:when test="@status=''">--</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="fulfill_the_criteria='true'">
										<xsl:value-of select="$lab_yes"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_no"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ========================================================================================= -->
	<xsl:template match="item" mode="criteria">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_cond_0"/>
		<xsl:param name="lab_cond_1"/>
		<xsl:param name="lab_cond_2"/>
		<xsl:param name="lab_cond_3"/>
		<xsl:param name="lab_cond_4"/>
		<xsl:param name="lab_cond_5"/>
		<xsl:param name="lab_cond_6"/>
		<xsl:param name="lab_cond_7"/>
		<xsl:choose>
			<xsl:when test="position()=1">
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:value-of select="title"/>
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="@desc_option='0'">
							<xsl:value-of select="$lab_cond_0"/>
						</xsl:when>
						<xsl:when test="@desc_option='1'">
							<xsl:value-of select="$lab_cond_1"/>
						</xsl:when>
						<xsl:when test="@desc_option='2'">
							<xsl:value-of select="$lab_cond_2"/>
						</xsl:when>
						<xsl:when test="@desc_option='3'">
							<xsl:value-of select="$lab_cond_3"/>
						</xsl:when>
						<xsl:when test="@desc_option='4'">
							<xsl:value-of select="$lab_cond_4"/>
						</xsl:when>
						<xsl:when test="@desc_option='5'">
							<xsl:value-of select="$lab_cond_5"/>
						</xsl:when>
						<xsl:when test="@desc_option='6'">
							<xsl:value-of select="$lab_cond_6"/>
						</xsl:when>
						<xsl:when test="@desc_option='7'">
							<xsl:value-of select="$lab_cond_7"/>
						</xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</td>
				<td style="padding:10px 0 10px 10px; color:#333;">
					<xsl:choose>
						<xsl:when test="@desc_option=''">--</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="fulfill_the_criteria='true'">
									<xsl:value-of select="$lab_yes"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_no"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<tr>
					<td>
					</td>
					<td style="padding:10px 0 10px 10px; color:#333;">
						<xsl:value-of select="title"/>
					</td>
					<td style="padding:10px 0 10px 10px; color:#333;">
						<xsl:choose>
							<xsl:when test="@desc_option='0'">
								<xsl:value-of select="$lab_cond_0"/>
							</xsl:when>
							<xsl:when test="@desc_option='1'">
								<xsl:value-of select="$lab_cond_1"/>
							</xsl:when>
							<xsl:when test="@desc_option='2'">
								<xsl:value-of select="$lab_cond_2"/>
							</xsl:when>
							<xsl:when test="@desc_option='3'">
								<xsl:value-of select="$lab_cond_3"/>
							</xsl:when>
							<xsl:when test="@desc_option='4'">
								<xsl:value-of select="$lab_cond_4"/>
							</xsl:when>
							<xsl:when test="@desc_option='5'">
								<xsl:value-of select="$lab_cond_5"/>
							</xsl:when>
							<xsl:when test="@desc_option='6'">
								<xsl:value-of select="$lab_cond_6"/>
							</xsl:when>
							<xsl:when test="@desc_option='7'">
								<xsl:value-of select="$lab_cond_7"/>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</td>
					<td style="padding:10px 0 10px 10px; color:#333;">
						<xsl:choose>
							<xsl:when test="@desc_option=''">--</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="fulfill_the_criteria='true'">
										<xsl:value-of select="$lab_yes"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_no"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ========================================================================================= -->
	<!--this template can be reused in aeItem's Moudle to draw nav-link of items which can run-->
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
	<!-- ====================================================================================================== -->
</xsl:stylesheet>

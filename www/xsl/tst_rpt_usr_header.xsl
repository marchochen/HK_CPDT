<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/student_report/student/test/@id"/>
	<xsl:variable name="usr_id" select="/student_report/student/@ent_id"/>
	<xsl:variable name="attempt_cur" select="/student_report/attempt_list/@current"/>
	<xsl:variable name="mod_title" select="/student_report/student/test/header/title"/>
	<xsl:variable name="my_completion_status" select="/student_report/student/test/@completion_status"/>
	<xsl:variable name="my_tst_score" select="/student_report/student/test/@score"/>
	<xsl:variable name="my_tst_pass_score" select="/student_report/student/test/header/@pass_score"/>
	<xsl:variable name="tst_max_score" select="sum(/student_report/student/test/body//question/body/interaction/@score)"/>
	<xsl:variable name="course_id" select="/student_report/student/test/header/@course_id"/>
	<xsl:variable name="tkh_id" select="/student_report/student/test/@tkh_id"/>
	<xsl:variable name= "ent_id" select="/student_report/student/@ent_id"/>
	<xsl:variable name="is_graded">
		<xsl:choose>
			<xsl:when test="/student_report/attempt_list/attempt[@id = $attempt_cur]/@status != 'NOT GRADED'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="/student_report"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/student_report">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_report">學習報告</xsl:with-param>
			<xsl:with-param name="lab_que">題目</xsl:with-param>
			<xsl:with-param name="lab_obj">資源文件夹</xsl:with-param>
			<xsl:with-param name="lab_no_attp">嘗試次數：</xsl:with-param>
			<xsl:with-param name="lab_view">顯示</xsl:with-param>
			<xsl:with-param name="lab_total_score">分數：</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_correct">正確</xsl:with-param>
			<xsl:with-param name="lab_sum_by_obj">學習報告(以題目類別區分)</xsl:with-param>
			<xsl:with-param name="lab_stu_penc">個人得分百份率</xsl:with-param>
			<xsl:with-param name="lab_cos_penc">整體得分百份率</xsl:with-param>
			<xsl:with-param name="lab_diff">難度</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>					
			<xsl:with-param name="lab_pass">合格</xsl:with-param>
			<xsl:with-param name="lab_fail">不合格</xsl:with-param>
			<xsl:with-param name="lab_not_attp">仍未嘗試</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇類型：</xsl:with-param>
			<xsl:with-param name="lab_user_report">用戶報告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_score_summary">總分</xsl:with-param>
			<xsl:with-param name="lab_your_score">分數</xsl:with-param>
			<xsl:with-param name="lab_passing_percentage">合格率</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_course_avg">課程平均</xsl:with-param>
			<xsl:with-param name="lab_que_summary">題目摘要</xsl:with-param>
			<xsl:with-param name="lab_obj_summary">題目類別摘要</xsl:with-param>
			<xsl:with-param name="lab_no_que">無利用的題目</xsl:with-param>
			<xsl:with-param name="lab_no_obj">無使用的資源文件夹</xsl:with-param>
			<xsl:with-param name="lab_g_btn_details">詳細</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_not_graded">尚未評分</xsl:with-param>
			<xsl:with-param name="lab_dash">--</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_report">学习报告</xsl:with-param>
			<xsl:with-param name="lab_que">题目</xsl:with-param>
			<xsl:with-param name="lab_obj">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_no_attp">答题次数：</xsl:with-param>
			<xsl:with-param name="lab_view">显示</xsl:with-param>
			<xsl:with-param name="lab_total_score">分数：</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_correct">正确</xsl:with-param>
			<xsl:with-param name="lab_sum_by_obj">学习报告(以题目类别区分)</xsl:with-param>
			<xsl:with-param name="lab_stu_penc">个人得分率</xsl:with-param>
			<xsl:with-param name="lab_cos_penc">整体得分率</xsl:with-param>
			<xsl:with-param name="lab_diff">难度</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>					
			<xsl:with-param name="lab_pass">合格</xsl:with-param>
			<xsl:with-param name="lab_fail">不合格</xsl:with-param>
			<xsl:with-param name="lab_not_attp">还未进行</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择类型：</xsl:with-param>
			<xsl:with-param name="lab_user_report">用户报告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_score_summary">总分</xsl:with-param>
			<xsl:with-param name="lab_your_score">分数</xsl:with-param>
			<xsl:with-param name="lab_passing_percentage">合格率</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_course_avg">课程平均</xsl:with-param>
			<xsl:with-param name="lab_que_summary">题目摘要</xsl:with-param>
			<xsl:with-param name="lab_obj_summary">题目类别摘要</xsl:with-param>
			<xsl:with-param name="lab_no_que">没有可用题目</xsl:with-param>
			<xsl:with-param name="lab_no_obj">没有可用文件夹</xsl:with-param>
			<xsl:with-param name="lab_g_btn_details">详细</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_not_graded">尚未评分</xsl:with-param>
			<xsl:with-param name="lab_dash">--</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_report">Report</xsl:with-param>
			<xsl:with-param name="lab_que">Question</xsl:with-param>
			<xsl:with-param name="lab_obj">Folder</xsl:with-param>
			<xsl:with-param name="lab_no_attp">Attempt number：</xsl:with-param>
			<xsl:with-param name="lab_view">View</xsl:with-param>
			<xsl:with-param name="lab_total_score">Learner score：</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_correct">Correct</xsl:with-param>
			<xsl:with-param name="lab_sum_by_obj">Summary by objective</xsl:with-param>
			<xsl:with-param name="lab_stu_penc">Learner percentage</xsl:with-param>
			<xsl:with-param name="lab_cos_penc">Course percentage</xsl:with-param>
			<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>					
			<xsl:with-param name="lab_pass">Passed</xsl:with-param>
			<xsl:with-param name="lab_fail">Failed</xsl:with-param>
			<xsl:with-param name="lab_not_attp">Not attempted</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select type：</xsl:with-param>
			<xsl:with-param name="lab_user_report">User report</xsl:with-param>
			<xsl:with-param name="lab_usage_report">Usage report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_score_summary">Score summary</xsl:with-param>
			<xsl:with-param name="lab_your_score">Learner score</xsl:with-param>
			<xsl:with-param name="lab_passing_percentage">Passing percentage</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_course_avg">Course average</xsl:with-param>
			<xsl:with-param name="lab_que_summary">Question summary</xsl:with-param>
			<xsl:with-param name="lab_obj_summary">Question folder summary</xsl:with-param>
			<xsl:with-param name="lab_no_que">No question available</xsl:with-param>
			<xsl:with-param name="lab_no_obj">No objective available</xsl:with-param>
			<xsl:with-param name="lab_g_btn_details">Details</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_not_graded">Not graded</xsl:with-param>
			<xsl:with-param name="lab_dash">--</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_report"/>
		<xsl:param name="lab_usr_report"/>
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_obj"/>
		<xsl:param name="lab_no_attp"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_correct"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_sum_by_obj"/>
		<xsl:param name="lab_stu_penc"/>
		<xsl:param name="lab_cos_penc"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_pass"/>
		<xsl:param name="lab_fail"/>
		<xsl:param name="lab_not_attp"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_your_score"/>
		<xsl:param name="lab_passing_percentage"/>
		<xsl:param name="lab_course_avg"/>
		<xsl:param name="lab_que_summary"/>
		<xsl:param name="lab_obj_summary"/>
		<xsl:param name="lab_no_que"/>
		<xsl:param name="lab_no_obj"/>
		<xsl:param name="lab_g_btn_details"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>		
		<xsl:param name="lab_score_summary"/>
		<xsl:param name="lab_not_graded"/>
		<xsl:param name="lab_dash"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					var rpt = new wbReport
					var report_type = wb_utils_get_cookie('report_type'); 
					var course_title = wb_utils_get_cookie('course_title'); 
					var module_name = wb_utils_get_cookie("mod_name")
					var module_url = wb_utils_get_cookie("mod_url")
					var total_average_score = 0
					var total_course_penc = 0
			]]></SCRIPT>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch" onSubmit="return status()">
				<script>
					<xsl:for-each select="student/test/body/question">
						<xsl:variable name="qid" select="@id"/><![CDATA[ total_average_score += ]]><xsl:value-of select="sum(//result[@id = $qid]/interaction/@usr_score)"/>;
					</xsl:for-each>
				</script>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text"><xsl:value-of select="$lab_tracking_report"/>&#160;-&#160;<xsl:value-of select="$mod_title"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_sub_title">
					<xsl:with-param name="text"><img src="{$wb_img_path}user.gif" border="0" align="absmiddle"/><xsl:text>&#160;</xsl:text><xsl:value-of select="student/@display_bil"/> (<xsl:value-of select="student/@id"/>)</xsl:with-param>
				</xsl:call-template>
				<xsl:choose>
					<xsl:when test="count(attempt_list/attempt) &gt; 0">
						<table width="{$wb_gen_table_width}" border="0" cellpadding="1" cellspacing="0">
							<tr>
								<td>
									<span class="TitleText">&#160;<xsl:value-of select="$lab_no_attp"/>&#160;</span>
									<span class="Text">
										<select size="1" class="Select" onChange="rpt.usr_atm('{$mod_id}','{$usr_id}',this.options[this.selectedIndex].value,'', {$tkh_id})">
											<xsl:for-each select="attempt_list/attempt">
												<option value="{@id}">
													<xsl:if test="@id = $attempt_cur">
														<xsl:attribute name="selected">selected</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="position()"/>
												</option>
											</xsl:for-each>
										</select>
									</span>
								</td>
							</tr>
						</table>
						<xsl:call-template name="wb_ui_line"/>
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$lab_score_summary"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table width="{$wb_gen_table_width}" border="0" cellpadding="3" cellspacing="0" class="Bg">
							<tr>
								<td width="20%" align="right" valign="top" height="10">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="80%" valign="top" height="10">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							<tr>
								<td width="20%" align="right" valign="top">
									<span class="TitleText">
										<xsl:value-of select="$lab_your_score"/>：</span>
								</td>
								<td width="80%" valign="top">
									<span class="Text">
										<xsl:choose>
											<xsl:when test="$is_graded = 'true'">
												<xsl:value-of select="$my_tst_score"/>/<xsl:value-of select="$tst_max_score"/> (<script><![CDATA[document.write(Math.round(total_average_score/]]><xsl:value-of select="$tst_max_score"/><![CDATA[*100))]]></script>%)
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_dash"/>/<xsl:value-of select="$tst_max_score"/> (<xsl:value-of select="$lab_dash"/>%)
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</tr>
							<tr>
								<td width="20%" align="right" valign="top">
									<span class="TitleText">
										<xsl:value-of select="$lab_passing_percentage"/>：</span>
								</td>
								<td width="80%" valign="top">
									<span class="Text">
										<xsl:value-of select="$my_tst_pass_score"/>
									</span>
								</td>
							</tr>
							<tr>
								<td width="20%" align="right" valign="top">
									<span class="TitleText">
										<xsl:value-of select="$lab_status"/>：</span>
								</td>
								<td width="80%" valign="top">
									<span class="Text">
										<xsl:choose>
											<xsl:when test="$is_graded = 'false'">
												<xsl:value-of select="$lab_not_graded"/>
											</xsl:when>
											<xsl:when test="$my_completion_status = 'P'">
												<xsl:value-of select="$lab_pass"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="$lab_fail"/>
											</xsl:otherwise>
										</xsl:choose>
									</span>
								</td>
							</tr>
							<tr>
								<td width="20%" align="right" valign="top" height="10">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<td width="80%" valign="top" height="10">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
						</table>
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$lab_que_summary"/>
						</xsl:call-template>
						<xsl:call-template name="overview">
							<xsl:with-param name="lab_que" select="$lab_que"/>
							<xsl:with-param name="lab_correct" select="$lab_correct"/>
							<xsl:with-param name="lab_obj" select="$lab_obj"/>
							<xsl:with-param name="lab_view" select="$lab_view"/>
							<xsl:with-param name="lab_total_score" select="$lab_total_score"/>
							<xsl:with-param name="lab_diff" select="$lab_diff"/>
							<xsl:with-param name="lab_score" select="$lab_score"/>
							<xsl:with-param name="lab_status" select="$lab_status"/>
							<xsl:with-param name="lab_pass" select="$lab_pass"/>
							<xsl:with-param name="lab_fail" select="$lab_fail"/>
							<xsl:with-param name="lab_cos_penc" select="$lab_cos_penc"/>
							<xsl:with-param name="lab_no_que" select="$lab_no_que"/>
							<xsl:with-param name="lab_g_btn_details" select="$lab_g_btn_details"/>
							<xsl:with-param name="lab_easy" select="$lab_easy"/>
							<xsl:with-param name="lab_normal" select="$lab_normal"/>
							<xsl:with-param name="lab_hard" select="$lab_hard"/>							
							<xsl:with-param name="lab_not_graded" select="$lab_not_graded"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$lab_obj_summary"/>
						</xsl:call-template>
						<xsl:call-template name="sum_by_obj">
							<xsl:with-param name="lab_obj" select="$lab_obj"/>
							<xsl:with-param name="lab_stu_penc" select="$lab_stu_penc"/>
							<xsl:with-param name="lab_cos_penc" select="$lab_cos_penc"/>
							<xsl:with-param name="lab_score" select="$lab_score"/>
							<xsl:with-param name="lab_no_obj" select="$lab_no_obj"/>
							<xsl:with-param name="lab_not_graded" select="$lab_not_graded"/>
							<xsl:with-param name="lab_dash" select="$lab_dash"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_not_attp"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<TABLE cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td align="center">
						<div class="wzb-bar">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_close"/></xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
							</xsl:call-template>
							</div>
						</td>
					</tr>
				</TABLE>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="overview">
		<xsl:param name="lab_que"/>
		<xsl:param name="lab_correct"/>
		<xsl:param name="lab_obj"/>
		<xsl:param name="lab_view"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_pass"/>
		<xsl:param name="lab_fail"/>
		<xsl:param name="lab_cos_penc"/>
		<xsl:param name="lab_no_que"/>
		<xsl:param name="lab_g_btn_details"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>		
		<xsl:param name="lab_not_graded"/>
		<xsl:choose>
			<xsl:when test="count(student/test/body/question) &gt; 0">
				<xsl:call-template name="wb_ui_line"/>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
					<tr class="wzb-ui-table-head"><!-- SecBg -->
						<td width="2%">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_que"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_correct"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_obj"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_diff"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_score"/>
							</span>
						</td>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td width="2%">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:for-each select="student/test/body/question">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
							<xsl:variable name="qid" select="@id"/>
							<td width="2%">
								<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<td>
								<span class="Text">
									<xsl:value-of select="@order"/>
								</span>
								<xsl:text/>
								<xsl:if test="count(//result[@id=$qid]/interaction[@flag='true'])">
									<img src="{$wb_img_path}rpt_sflag.gif" border="0"/>
								</xsl:if>
							</td>
							<td align="center">
								<xsl:variable name="correct_interaction" select="count(//result[@id=$qid]/interaction[@correct='true'])"/>
								<xsl:variable name="total_interaction" select="count(//result[@id=$qid]/interaction)"/>
								<xsl:choose>
									<xsl:when test="body/interaction/@type='ES' and number(//result[@id = $qid]/interaction/@usr_score) = -1">
										<span class="wbRowText">
											<xsl:value-of select="$lab_not_graded"/>
										</span>
									</xsl:when>
									<xsl:when test="body/interaction/@type='ES' and //result[@id=$qid]/interaction/@usr_score = 0">
										<img src="{$wb_img_path}rpt_x.gif" border="0"/>
									</xsl:when>
									<xsl:when test="body/interaction/@type='ES'and //result[@id=$qid]/interaction/@usr_score &lt; body/interaction/@score">
										<img src="{$wb_img_path}rpt_halfx.gif" border="0"/>
									</xsl:when>
									<xsl:when test="$correct_interaction = $total_interaction">
										<img src="{$wb_img_path}rpt_right.gif" border="0"/>
									</xsl:when>
									<xsl:when test="$correct_interaction = 0">
										<img src="{$wb_img_path}rpt_x.gif" border="0"/>
									</xsl:when>
									<xsl:otherwise>
										<img src="{$wb_img_path}rpt_halfx.gif" border="0"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
								<span class="Text">
									<xsl:value-of select="header/objective/desc/text()"/>
								</span>
							</td>
							<td align="center">
								<span class="Text">
								<xsl:choose>
									<xsl:when test="header/@difficulty = '1'">
										<xsl:value-of select="$lab_easy"/>
									</xsl:when>
									<xsl:when test="header/@difficulty = '2'">
										<xsl:value-of select="$lab_normal"/>
									</xsl:when>
									<xsl:when test="header/@difficulty = '3'">
										<xsl:value-of select="$lab_hard"/>
									</xsl:when>
								</xsl:choose>
								</span>
							</td>
							<td align="center">
								<span class="Text">
									<xsl:variable name="que_score" select="sum(body/interaction/@score)"/>
									<xsl:choose>
										<xsl:when test="body/interaction/@type='ES' and number(//result[@id = $qid]/interaction/@usr_score) = -1"><xsl:text>--</xsl:text></xsl:when>
										<xsl:otherwise><xsl:value-of select="sum(//result[@id = $qid]/interaction/@usr_score)"/></xsl:otherwise>
									</xsl:choose>
									<xsl:text>/</xsl:text><xsl:value-of select="$que_score"/>
									<xsl:if test="not(body/interaction/@type='ES'and number(//result[@id = $qid]/interaction/@usr_score) = -1)">
									<script><![CDATA[ total_average_score += ]]><xsl:value-of select="sum(//result[@id = $qid]/interaction/@usr_score)"/>
									</script>
									</xsl:if>
								</span>
							</td>
							<td align="right">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_btn_details"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:rpt.usr_ind('<xsl:value-of select="@id"/>','<xsl:value-of select="$mod_id"/>','<xsl:value-of select="$usr_id"/>','<xsl:value-of select="$attempt_cur"/>','',<xsl:value-of select="$tkh_id"/>)</xsl:with-param>
								</xsl:call-template>
							</td>
							<td width="2%">
								<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text"><xsl:value-of select="$lab_no_que"/></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--=======================================================================-->
	<xsl:template name="sum_by_obj">
		<xsl:param name="lab_obj"/>
		<xsl:param name="lab_stu_penc"/>
		<xsl:param name="lab_cos_penc"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_no_obj"/>
		<xsl:param name="lab_not_graded"/>
		<xsl:param name="lab_dash"/>
		<xsl:choose>
			<xsl:when test="count(student/test/body/objective) &gt; 0">
				<xsl:call-template name="wb_ui_line"/>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr class="SecBg">
						<td width="2%">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td width="51%">
							<span class="TitleText">
								<xsl:value-of select="$lab_obj"/>
							</span>
						</td>
						<td width="25%" align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_stu_penc"/>
							</span>
						</td>
						<td width="20%" align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_cos_penc"/>
							</span>
						</td>
						<td width="2%">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:for-each select="student/test/body/objective">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
							<td width="2%">
								<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<xsl:variable name="obj_id" select="@id"/>
							<td width="53%">
								<span class="Text">
									<xsl:value-of select="//test/body/question/header/objective[@id=$obj_id]"/>
								</span>
							</td>
							<td width="25%" align="center">
								<span class="Text">
									<xsl:choose>
										<xsl:when test="$is_graded = 'true'"><xsl:value-of select="round((sum(//result[@obj_id=$obj_id]/interaction/@usr_score) div sum(//objective[$obj_id=@id]/../../outcome/@score))*100)"/>%</xsl:when>
										<xsl:otherwise><xsl:value-of select="$lab_dash"/></xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
							<td width="20%" align="center">
								<span class="Text">
									<xsl:value-of select="@percentage"/>%</span>
							</td>
							<td width="2%">
								<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								<script><![CDATA[ total_course_penc += ]]><xsl:value-of select="@percentage * sum(//question/header/objective[@id=$obj_id]/../../outcome/@score)"/>
								</script>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_obj"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

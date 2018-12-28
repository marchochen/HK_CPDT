<?xml version="1.0" encoding="UTF-8"?>
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
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="share/question_body_share.xsl"/>
	<xsl:import href="share/wb_layout_share.xsl"/>
	<xsl:import href="share/wb_object_share.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/student_report/student/test/@id"/>
	<xsl:variable name="course_id" select="/student_report/student/test/header/@course_id"/>
	<xsl:variable name="mod_title" select="/student_report/student/test/header/title"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="/student_report/student"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/student_report/student">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_diff">難度</xsl:with-param>
			<xsl:with-param name="lab_objective">資源文件夹</xsl:with-param>
			<xsl:with-param name="lab_score">題目得分</xsl:with-param>
			<xsl:with-param name="lab_your_score">學員分數</xsl:with-param>
			<xsl:with-param name="lab_report">學習報告</xsl:with-param>
			<xsl:with-param name="lab_report_det">學習報告詳情</xsl:with-param>
			<xsl:with-param name="lab_grp_report">小組學習報告</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇類型:</xsl:with-param>
			<xsl:with-param name="lab_user_report">用戶報告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟蹤報告</xsl:with-param>
			<xsl:with-param name="lab_dash">--</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_diff">难度</xsl:with-param>
			<xsl:with-param name="lab_objective">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_score">题目得分</xsl:with-param>
			<xsl:with-param name="lab_your_score">学员得分</xsl:with-param>
			<xsl:with-param name="lab_report">学习报告</xsl:with-param>
			<xsl:with-param name="lab_report_det">学习报告详情</xsl:with-param>
			<xsl:with-param name="lab_grp_report">小组学习报告</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择类型：</xsl:with-param>
			<xsl:with-param name="lab_user_report">用户报告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">跟踪报告</xsl:with-param>
			<xsl:with-param name="lab_dash">--</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_objective">Folder</xsl:with-param>
			<xsl:with-param name="lab_score">Question score</xsl:with-param>
			<xsl:with-param name="lab_your_score">Learner score</xsl:with-param>
			<xsl:with-param name="lab_report">Report</xsl:with-param>
			<xsl:with-param name="lab_report_det">Report detail</xsl:with-param>
			<xsl:with-param name="lab_grp_report">Group report</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select type :</xsl:with-param>
			<xsl:with-param name="lab_user_report">User report</xsl:with-param>
			<xsl:with-param name="lab_usage_report">Usage report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Tracking report</xsl:with-param>
			<xsl:with-param name="lab_dash">--</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_grp_report"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_report"/>
		<xsl:param name="lab_report_det"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_your_score"/>
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
			]]></SCRIPT>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch" onSubmit="return status()">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_tracking_report"/>&#160;-&#160;<xsl:value-of select="$mod_title"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_sub_title">
					<xsl:with-param name="text">
						<xsl:value-of select="@display_bil"/> (<xsl:value-of select="@id"/>)</xsl:with-param>
				</xsl:call-template>
				<xsl:for-each select="test/body/question">
					<!-- question info start -->
					<!-- heading -->
					<!-- content -->
					<xsl:variable name="que_id"><xsl:value-of select="@id"/></xsl:variable>
					<xsl:variable name="container_id"><xsl:value-of select="//parent_container[@que_id=$que_id]/text()"/></xsl:variable>
						<xsl:if test="$container_id!='' and $container_id!=0">
							<xsl:call-template name="wb_ui_head">
								<xsl:with-param name="text" select="//container[@id=$container_id]/title/text()"/>
							</xsl:call-template>
							<xsl:call-template name="wb_ui_line"/>
							<table border="0" cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" class="bg">
								<tr>
									<td colspan="2"><IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
								</tr>
								<tr>
									<td>
										<table border="0" cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" class="bg">
											<tr>
												<td valign="top">
													<span class="Text">
														<xsl:for-each select="//container[@id=$container_id]/body/text() | //container[@id=$container_id]/body/html">
															<xsl:choose>
																<xsl:when test="name() = 'html'">
																	<xsl:value-of disable-output-escaping="yes" select="."/>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:call-template name="unescape_html_linefeed">
																		<xsl:with-param name="my_right_value">
																			<xsl:value-of select="."/>
																		</xsl:with-param>
																	</xsl:call-template>
																</xsl:otherwise>
															</xsl:choose>
														</xsl:for-each>
													</span>
												</td>
												<td align="right" rowspan="2" valign="top">
													<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
												</td>
											</tr>
											<tr>
												<td>
													<xsl:choose>
														<xsl:when test="//container[@id=$container_id]/body/object/@type!=''"><xsl:apply-templates select="//container[@id=$container_id]/body/object"/></xsl:when>
														<xsl:otherwise><xsl:apply-templates select="body/object"/></xsl:otherwise>
													</xsl:choose>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="2"><IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td>
								</tr>
							</table>
							<xsl:call-template name="wb_ui_space"/>
						</xsl:if>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="concat(@order, '. ', header/title)"/>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<xsl:call-template name="public_que_info">
								<xsl:with-param name="lab_diff" select="$lab_diff"/>
								<xsl:with-param name="lab_easy" select="$lab_easy"/>
								<xsl:with-param name="lab_hard" select="$lab_hard"/>
								<xsl:with-param name="lab_item_type" select="$lab_item_type"/>
								<xsl:with-param name="lab_normal" select="$lab_normal"/>
								<xsl:with-param name="lab_objective" select="$lab_objective"/>
								<xsl:with-param name="lab_score" select="$lab_score"/>
								<xsl:with-param name="lab_type" select="$lab_type"/>
								<xsl:with-param name="lab_your_score" select="$lab_your_score"/>
								<xsl:with-param name="lab_dash" select="$lab_dash"/>
							</xsl:call-template>
							<xsl:call-template name="question_body">
								<xsl:with-param name="width" select="$wb_gen_table_width"/>
								<xsl:with-param name="que_id" select="@id"/>
								<xsl:with-param name="view">RPT</xsl:with-param>
							</xsl:call-template>
					<!-- question info end -->
					<xsl:if test="position() = last()">
						<table border="0" cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}">
							<tr align="center">
							    <td>
							     <div class ="wzb-bar" >
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
									</xsl:call-template>
									</div>
								</td>
							</tr>
						</table>
					</xsl:if>
					<xsl:call-template name="wb_ui_space"/>
				</xsl:for-each>
			</form>
		</body>
	</xsl:template>
<xsl:template name="public_que_info">
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_objective"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_your_score"/>
		<xsl:param name="lab_dash"/>
	<table border="0" cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" class="Bg">
	<tr>
		<td colspan="4" height="10">
			<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="right" valign="top">
			<span class="TitleText">
				<xsl:value-of select="$lab_type"/>：</span>
		</td>
		<td width="30%">
			<span class="Text">
				<xsl:choose>
					<xsl:when test="body/interaction/@type='MC'">
						<xsl:value-of select="$lab_mc"/>
					</xsl:when>
					<xsl:when test="body/interaction/@type='FB'">
						<xsl:value-of select="$lab_fb"/>
					</xsl:when>
					<xsl:when test="body/interaction/@type='MT'">
						<xsl:value-of select="$lab_mt"/>
					</xsl:when>
					<xsl:when test="body/interaction/@type='TF'">
						<xsl:value-of select="$lab_tf"/>
					</xsl:when>
					<xsl:when test="body/interaction/@type='ES'">
						<xsl:value-of select="$lab_es"/>
					</xsl:when>
				</xsl:choose>
			</span>
		</td>
		<td width="20%" align="right" valign="top">
			<span class="TitleText">
				<xsl:value-of select="$lab_diff"/>：</span>
		</td>
		<td width="30%" valign="top">
			<span class="Text">
				<xsl:choose>
					<xsl:when test="header/@difficulty='1'">
						<xsl:value-of select="$lab_easy"/>
					</xsl:when>
					<xsl:when test="header/@difficulty='2'">
						<xsl:value-of select="$lab_normal"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_hard"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</tr>
	<tr>
		<td width="20%" align="right" valign="top">
			<span class="TitleText">
				<xsl:value-of select="$lab_objective"/>：</span>
		</td>
		<td width="30%">
			<span class="Text">
				<xsl:value-of select="header/objective"/>
			</span>
		</td>
		<td width="20%" align="right" valign="top">
			<span class="TitleText">
				<xsl:value-of select="$lab_score"/>：</span>
		</td>
		<td width="30%" valign="top">
			<span class="Text">
				<xsl:choose>
					<xsl:when test="body/interaction/@type = 'ES'">
						<xsl:value-of select="body/interaction/@score"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="sum(outcome/@score)"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
	</tr>
	<tr>
		<td width="20%" align="right" valign="top">
			<span class="TitleText">
				<xsl:value-of select="$lab_your_score"/>：</span>
		</td>
		<td width="30%">
			<span class="Text">
				<xsl:variable name="qid" select="@id"/>
				<xsl:choose>
					<xsl:when test="(//question[@id=$qid]/body/interaction/@type='ES') and sum(//result[@id=$qid]/interaction/@usr_score) = -1">
						<xsl:value-of select="$lab_dash"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="sum(//result[@id=$qid]/interaction/@usr_score)"/>
					</xsl:otherwise>
				</xsl:choose>
			</span>
		</td>
		<td width="20%" align="right" valign="top">
			<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
		</td>
		<td width="30%" valign="top">
			<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
		</td>
	</tr>
	<tr>
		<td colspan="4" height="10">
			<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
		</td>
	</tr>
</table>
	<div class ="wzb-bar"></div>
</xsl:template>
</xsl:stylesheet>

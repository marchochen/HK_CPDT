<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!--可import-->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>

	<!-- share -->
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="share/question_body_share.xsl"/>
	<!--==============-->
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/student_report/student/test/@id"/>
	<xsl:variable name="course_id" select="/student_report/student/test/header/@course_id"/>
	<xsl:variable name="mod_title" select="/student_report/student/test/header/title"/>
	
	<xsl:template name="DOCTYPE">
		<![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"/>]]>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:value-of select="document('')/*/xsl:template[@name='DOCTYPE']/node()" disable-output-escaping="yes"/>
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
			<xsl:with-param name="lab_score">題目得分</xsl:with-param>
			<xsl:with-param name="lab_your_score">您的分數</xsl:with-param>
			<xsl:with-param name="lab_report">學習報告</xsl:with-param>
			<xsl:with-param name="lab_report_det">學習報告詳情</xsl:with-param>
			<xsl:with-param name="lab_grp_report">小組學習報告</xsl:with-param>
			<xsl:with-param name="lab_item_type">請選擇類型:</xsl:with-param>
			<xsl:with-param name="lab_user_report">用戶報告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用報告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">報告</xsl:with-param>
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
			<xsl:with-param name="lab_score">题目得分</xsl:with-param>
			<xsl:with-param name="lab_your_score">您的分数</xsl:with-param>
			<xsl:with-param name="lab_report">学习报告</xsl:with-param>
			<xsl:with-param name="lab_report_det">学习报告详情</xsl:with-param>
			<xsl:with-param name="lab_grp_report">小组学习报告</xsl:with-param>
			<xsl:with-param name="lab_item_type">请选择类型：</xsl:with-param>
			<xsl:with-param name="lab_user_report">用户报告</xsl:with-param>
			<xsl:with-param name="lab_usage_report">使用报告</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">报告</xsl:with-param>
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
			<xsl:with-param name="lab_score">Question score</xsl:with-param>
			<xsl:with-param name="lab_your_score">Your score</xsl:with-param>
			<xsl:with-param name="lab_report">Report</xsl:with-param>
			<xsl:with-param name="lab_report_det">Report detail</xsl:with-param>
			<xsl:with-param name="lab_grp_report">Group report</xsl:with-param>
			<xsl:with-param name="lab_item_type">Please select type :</xsl:with-param>
			<xsl:with-param name="lab_user_report">User report</xsl:with-param>
			<xsl:with-param name="lab_usage_report">Usage report</xsl:with-param>
			<xsl:with-param name="lab_tracking_report">Report</xsl:with-param>
			<xsl:with-param name="lab_dash">--</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_your_score"/>
		<xsl:param name="lab_report"/>
		<xsl:param name="lab_report_det"/>
		<xsl:param name="lab_grp_report"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_user_report"/>
		<xsl:param name="lab_usage_report"/>
		<xsl:param name="lab_tracking_report"/>
		<xsl:param name="lab_dash"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<link rel="stylesheet" href="../static/css/three.css"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					var rpt = new wbReport
			]]></SCRIPT>
<!-- 			<xsl:call-template name="wb_css"> -->
<!-- 				<xsl:with-param name="view">wb_ui</xsl:with-param> -->
<!-- 			</xsl:call-template> -->
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0"
		 oncontextmenu='return false'
		 ondragstart='return false' 
		 onselectstart ='return false' 
		 onselect='document.selection.empty()'
		 oncopy='document.selection.empty()' 
		 onbeforecopy='return false' 


		>
		<noscript>
　             　<iframe scr="*.htm"></iframe>
　　            </noscript>


			<form name="frmSearch" onSubmit="return status()">
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_tracking_report"/>&#160;-&#160;<xsl:value-of select="$mod_title"/>
					</xsl:with-param>
					<xsl:with-param name="new_template">true</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text">
						<xsl:value-of select="@display_bil"/> (<xsl:value-of select="@id"/>)</xsl:with-param>
					<xsl:with-param name="new_template">true</xsl:with-param>
				</xsl:call-template>
				<xsl:for-each select="test/body/question">
					<!-- question info start -->
					<xsl:variable name="que_id">
						<xsl:value-of select="@id"/>
					</xsl:variable>
					<xsl:variable name="container_id">
						<xsl:value-of select="//parent_container[@que_id=$que_id]/text()"/>
					</xsl:variable>
					<xsl:if test="$container_id!='' and $container_id!=0">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="//container[@id=$container_id]/title/text()"/>
							<xsl:with-param name="new_template">true</xsl:with-param>
						</xsl:call-template>
						<div class="report_info clean_margin">
							<table border="0" cellpadding="3" cellspacing="0" width="100%" class="bg">
								<tr>
									<td colspan="2">
										<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
								<tr>
									<td>
										<table border="0" cellpadding="3" cellspacing="0" width="100%" class="bg">
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
														<xsl:when test="//container[@id=$container_id]/body/object/@type!=''">
															<xsl:apply-templates select="//container[@id=$container_id]/body/object"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:apply-templates select="body/object"/>
														</xsl:otherwise>
													</xsl:choose>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
							</table>
						</div>
					</xsl:if>
					<!-- heading -->
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="concat(@order, '. ', header/title)"/>
						<xsl:with-param name="new_template">true</xsl:with-param>
					</xsl:call-template>
					<!-- question info end -->
					<xsl:call-template name="question_body">
						<xsl:with-param name="width">100%</xsl:with-param>
						<xsl:with-param name="que_id" select="@id"/>
						<xsl:with-param name="view">LRNRPT</xsl:with-param>
					</xsl:call-template>
					<xsl:if test="position() = last()">
						<div class="report_botton">
							<a href="javascript:;" onclick="javascript:history.back()">
								<xsl:value-of select="$lab_g_form_btn_ok"/>
							</a>
						</div>
					</xsl:if>
				</xsl:for-each>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>

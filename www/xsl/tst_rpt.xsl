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
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/question_body_share.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/student_report/student/test/@id"/>
	<xsl:variable name="course_id" select="/student_report/student/test/header/@course_id"/>
	<xsl:variable name="mod_title" select="/student_report/student/test/header/title/text()"/>
	<xsl:variable name="attempt_cur" select="/student_report/attempt_list/@current"/>
	<xsl:variable name="is_item" select="/student_report/is_item/text()"/>
	
	<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
	<xsl:variable name="label_core_training_management_393" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')"/>
	<xsl:variable name="label_core_training_management_30" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_30')"/>
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
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_no_attp">嘗試次數：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_no_attp">答题次数：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_no_attp">Attempt number : </xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_no_attp"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					var module_lst = new wbModule;
			]]></SCRIPT>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			
			<xsl:call-template name="itm_action_nav">
				<xsl:with-param  name="cur_node_id">120</xsl:with-param>
			</xsl:call-template>

			<!--测评详细报告页面判断显示(课程或者是考试) 菜单样式以及模块显示-->
			<xsl:choose>
				<xsl:when test="//item/@exam_ind='false'">
					<xsl:call-template name="wb_ui_hdr">
						<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
						<xsl:with-param name="parent_code">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="wb_ui_hdr">
						<xsl:with-param name="belong_module">FTN_AMD_EXAM_MGT</xsl:with-param>
						<xsl:with-param name="parent_code">FTN_AMD_EXAM_MGT</xsl:with-param>
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
			
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<xsl:apply-templates select="/student_report/item/nav/item" mode="nav">
						<xsl:with-param name="lab_run_info"><xsl:value-of select="$label_core_training_management_250" /></xsl:with-param>
					</xsl:apply-templates>
					<span class="NavLink">
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="javascript:itm_lst.itm_evaluation_report({$course_id },'TST')"><xsl:value-of select="$label_core_training_management_393"/></a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:module_lst.view_submission_lst({$course_id },{$mod_id },'all')"><xsl:value-of select="$label_core_training_management_30"/></a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$mod_title"/>
					</span>
				</xsl:with-param>
			</xsl:call-template>	
						
			<form>
				<xsl:call-template name="wb_ui_sub_title">
					<xsl:with-param name="text">
						<xsl:value-of select="@display_bil"/> (<xsl:value-of select="@id"/>)</xsl:with-param>
				</xsl:call-template>
				<table width="{$wb_gen_table_width}" border="0" cellpadding="1" cellspacing="0">
					<tr>
						<td>
							<span class="TitleText">&#160;<xsl:value-of select="$lab_no_attp"/>&#160;</span>
							<span class="Text">
								<select size="1" class="Select" onchange="javascript:module_lst.view_submission({@ent_id},{test/@id},this.options[this.selectedIndex].value,0,{test/@tkh_id},{$course_id})">
									<xsl:for-each select="../attempt_list/attempt">
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
				<xsl:for-each select="test/body/question">
					<!-- question info start -->
					<xsl:variable name="que_id"><xsl:value-of select="@id"/></xsl:variable>
					<xsl:variable name="parent_id"><xsl:value-of select="//parent_container[@que_id=$que_id]/text()"/></xsl:variable>
					<!-- heading -->
						<xsl:if test="$parent_id!='' and $parent_id!=0">
								<xsl:call-template name="wb_ui_head">
									<xsl:with-param name="text" select="//container[@id=$parent_id]/title/text()"/>
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
															<xsl:for-each select="//container[@id=$parent_id]/body/text() | //container[@id=$parent_id]/body/html">
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
															<xsl:when test="//container[@id=$parent_id]/body/object/@type!=''"><xsl:apply-templates select="//container[@id=$parent_id]/body/object"/></xsl:when>
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
							<xsl:for-each select=".">
								<xsl:call-template name="wb_ui_head">
									<xsl:with-param name="text" select="concat(@order, '. ', header/title)"/>
								</xsl:call-template>
								<xsl:call-template name="wb_ui_line"/>
								<xsl:call-template name="question_body">
									<xsl:with-param name="width" select="$wb_gen_table_width"/>
									<xsl:with-param name="que_id" select="@id"/>
									<xsl:with-param name="view">RPT</xsl:with-param>
								</xsl:call-template>
							</xsl:for-each>
					<!-- question info end -->
					<xsl:if test="position() != last()">
					<xsl:call-template name="wb_ui_space"/>
					</xsl:if>
				</xsl:for-each>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>

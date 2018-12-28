<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_img_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="subtype">
		<xsl:choose>
			<xsl:when test="//fixed_scenario"><xsl:value-of select="/fixed_scenario/header/@subtype"/></xsl:when>
			<xsl:when test="//dynamic_scenario"><xsl:value-of select="/dynamic_scenario/header/@subtype"/></xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="env" select="/resource_content/env"/>
	<xsl:variable name="cur_usr" select="/resource_content/cur_usr/@id"/>
	<xsl:variable name="mod_timestamp" select="/resource_content/header/resource/@timestamp"/>
	<xsl:variable name="mod_status" select="/resource_content/header/resource/@status"/>
	<xsl:variable name="continer_id">
		<xsl:choose>
			<xsl:when test="//fixed_scenario"><xsl:value-of select="/fixed_scenario/@id"/></xsl:when>
			<xsl:when test="//dynamic_scenario"><xsl:value-of select="/dynamic_scenario/@id"/></xsl:when>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="fixed_scenario | dynamic_scenario">
			<xsl:with-param name="lab_question">題目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder">重新排序</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_no_res">還沒有任何題目</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="fixed_scenario | dynamic_scenario">
			<xsl:with-param name="lab_question">题目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder">重新排序</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_no_res">还没有任何题目</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="fixed_scenario | dynamic_scenario">
			<xsl:with-param name="lab_question">Question</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder">Reorder</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_no_res">No questions added</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fixed_scenario | dynamic_scenario">
		<xsl:param name="lab_question"/>
		<xsl:param name="lab_g_txt_btn_reorder"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_no_res"/>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>wb_scenario.js
			</title>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}wb_question.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}wb_module.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}wb_scenario.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="JavaScript"><![CDATA[
	
	exc = new wbExc
	que = new wbQuestion
	wb_tst = new wbTst;
	sc = new wbScControl;
	
	que_obj_id = getParentUrlParam('que_obj_id_ass');
	cos_id = getParentUrlParam('cos_id')
	
]]></script>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="pragma" content="no-cache"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" rightmargin="0" bottommargin="0">
			<form name="frmXml" enctype="multipart/form-data">
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_question"/>
					</xsl:with-param>
					<xsl:with-param name="extra_td">
							<td align="right">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:sc.add_que_prep(<xsl:value-of select="$continer_id"/>,'<xsl:value-of select="$subtype"/>')</xsl:with-param>
								</xsl:call-template>
							</td>
						<xsl:if test="count(/fixed_scenario/question_list/question)&gt;1 or count(/dynamic_scenario/question_list/question)&gt;1">
							<xsl:choose>
								<xsl:when test="$subtype='FSC'">
								<script language="JavaScript"><![CDATA[wb_utils_set_cookie('res_subtype','FSC');]]></script>
								</xsl:when>
							</xsl:choose>
							<xsl:if test="$subtype!='DSC'">
								<td align="right">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_reorder"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:exc.reorder_que_prep(<xsl:value-of select="/fixed_scenario/@id"/>,'<xsl:value-of select="/fixed_scenario/header/@status"/>','<xsl:value-of select="/fixed_scenario/@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									</xsl:call-template>
								</td>
							</xsl:if>
						</xsl:if>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:choose>
					<xsl:when test="count(/fixed_scenario/question_list/question)=0 and count(/dynamic_scenario/question_list/question)=0">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_res"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_line"/>
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="Bg">
							<tr>
								<td>
									<table cellpadding="5" cellspacing="0" border="0">
										<xsl:for-each select="question_list/question">
											<xsl:variable name="order_num">
												<xsl:value-of select="position()"/>
											</xsl:variable>
											<tr>
												<td align="right" valign="top">
													<a href="javascript:wb_utils_set_cookie('res_timestamp',unescape('{$mod_timestamp}'));que.read_in_module('{@id}','{$subtype}')" class="Text">
														<xsl:value-of select="$order_num"/>.</a>
												</td>
												<td>
													<a href="javascript:que.read_in_module('{@id}','{$subtype}')" class="Text">
														<xsl:value-of select="@title"/>
													</a>
												</td>
											</tr>
										</xsl:for-each>
									</table>
								</td>
							</tr>
						</table>
						<xsl:call-template name="wb_ui_line"/>
					</xsl:otherwise>
				</xsl:choose>
			</form>
		</body>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="env"/>
</xsl:stylesheet>

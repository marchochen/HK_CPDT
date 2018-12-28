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
	<xsl:variable name="subtype" select="/resource_content/header/resource/@subtype"/>
	<xsl:variable name="env" select="/resource_content/env"/>
	<xsl:variable name="cur_usr" select="/resource_content/cur_usr/@id"/>
	<xsl:variable name="mod_timestamp" select="/resource_content/header/resource/@timestamp"/>
	<xsl:variable name="mod_status" select="/resource_content/header/resource/@status"/>
	<xsl:variable name="mod_id" select="/resource_content/header/resource/@id"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="resource_content">
			<xsl:with-param name="lab_question">題目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder">重新排序</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_no_res">還沒有任何題目</xsl:with-param>
			<xsl:with-param name="lab_import">導入</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="resource_content">
			<xsl:with-param name="lab_question">题目</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder">重新排序</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_no_res">还没有任何题目</xsl:with-param>
			<xsl:with-param name="lab_import">导入</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="resource_content">
			<xsl:with-param name="lab_question">Question</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder">Reorder</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_no_res">No questions found</xsl:with-param>
			<xsl:with-param name="lab_import">Import</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="resource_content">
		<xsl:param name="lab_question"/>
		<xsl:param name="lab_g_txt_btn_reorder"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_no_res"/>
		<xsl:param name="lab_import"/>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}wb_question.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}wb_module.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script type="text/javascript" language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script language="JavaScript"><![CDATA[
	
	exc = new wbExc
	que = new wbQuestion
	wb_tst = new wbTst;
	
	que_obj_id = getParentUrlParam('que_obj_id_ass');

	function getParentUrlParam(name) {
	strParam = window.parent.location.search
	idx1 = strParam.indexOf(name + "=")
	if (idx1 == -1)	return ""

	idx1 = idx1 + name.length + 1
	idx2 = strParam.indexOf("&", idx1)

	if (idx2 != -1)
		len = idx2 - idx1
	else
		len = strParam.length

	return unescape(strParam.substr(idx1, len))
}	
	
	function go_obj(vhash){
	course_id = getParentUrlParam("course_id")
	window.parent.content.location.href=wb_tst.info_content_url(]]><xsl:value-of select="$mod_id"/><![CDATA[,course_id) + vhash;}
]]></script>
			<meta http-equiv="pragma" content="no-cache"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body onload="wb_utils_set_cookie('subtype','{$subtype}')" leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" rightmargin="0" bottommargin="0">
			<form name="frmXml" enctype="multipart/form-data">
				<!-- <xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_question"/>
					</xsl:with-param>
					<xsl:with-param name="extra_td">
						
					</xsl:with-param>
				</xsl:call-template> -->
				
				<table style="margin-top:15px;  margin-bottom: 15px;">
				<xsl:if test="(header/resource/@subtype='SVY') or (header/resource/@subtype='TNA') or (header/resource/@subtype='EVN')">
						<td align="center" style="margin-left: 20px;">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:que.add_evn_que_prep(<xsl:value-of select="$mod_id"/>,'MC','<xsl:value-of select="$subtype"/>')</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_import"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:que.imp_evn_que_prep(<xsl:value-of select="$mod_id"/>,'MC','<xsl:value-of select="$subtype"/>')</xsl:with-param>
							</xsl:call-template>
							<xsl:if test="count(item) &gt; 1">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_reorder"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:exc.reorder_que_prep(<xsl:value-of select="header/resource/@id"/>,'<xsl:value-of select="header/resource/@status"/>','<xsl:value-of select="header/resource/@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									</xsl:call-template>
							</xsl:if>
						</td>
					</xsl:if>
						
				</table>		
				<xsl:choose>
					<xsl:when test="count(item) = 0">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_res"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<table>
							<tr>
								<td>
									<table>
										<xsl:for-each select="item">
											<tr>
											    <td align="center" valign="top" style="  width: 25px;" >
													<!-- <xsl:value-of select="@order"/>. -->
												</td> 
												<td  >
													<a    href="javascript:que.read_in_module('{@id}','{$subtype}')" class="Text">
													<xsl:value-of select="@order"/>.<xsl:value-of select="title"/>
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

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_updown_box.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
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
			<xsl:with-param name="lab_reorder">重新排序</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="resource_content">
			<xsl:with-param name="lab_question">题目</xsl:with-param>
			<xsl:with-param name="lab_reorder">重新排序</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="resource_content">
			<xsl:with-param name="lab_question">Question</xsl:with-param>
			<xsl:with-param name="lab_reorder">Reorder</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="resource_content">
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_reorder"/>
		<xsl:param name="lab_question"/>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}wb_question.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
	
				exc = new wbExc
				que = new wbQuestion
				wb_tst = new wbTst;
				
				function go_obj(vhash){
					course_id = getParentUrlParam("course_id")
					window.parent.content.location.href=wb_tst.info_content_url(]]><xsl:value-of select="$mod_id"/><![CDATA[,course_id) + vhash;
					
				}
	
			]]></script>
			<meta http-equiv="pragma" content="no-cache"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body class="Bg" leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" rightmargin="0" bottommargin="0">
			<form name="frmXml">
				<input type="hidden" name="module" />
				<input type="hidden" name="res_id"/>
				<input type="hidden" name="res_upd_timestamp"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="mod_id"/>
				<input type="hidden" name="que_id_lst"/>
				<input type="hidden" name="que_order_lst"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="mod_timestamp"/>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_question"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table>
					<tr>
						<td>					
							<xsl:call-template name="wb_updown_box">
								<xsl:with-param name="height">200</xsl:with-param>
								<xsl:with-param name="width">180</xsl:with-param>
								<xsl:with-param name="margintop">10</xsl:with-param>
								<xsl:with-param name="size" select="count(item)"/>
								<xsl:with-param name="name">que_lst</xsl:with-param>
								<xsl:with-param name="frm">document.frmXml</xsl:with-param>
								<xsl:with-param name="option_list">
								<xsl:for-each select="item">
									<option value="{@id}"><xsl:value-of select="title"/></option>
								</xsl:for-each>
								</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<xsl:if test="count(item) != 0">
				</xsl:if>
				<div class="wzb-bar">
					<xsl:if test="count(item) != 0">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="class">btn wzb-btn-blue margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:exc.reorder_que_exec(document.frmXml,<xsl:value-of select="count(//item)"/>,<xsl:value-of select="header/resource/@id"/>,'<xsl:value-of select="header/resource/@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="class">btn wzb-btn-blue margin-right10</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="env"/>
</xsl:stylesheet>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="utils/wb_ui_title.xsl"/>
<xsl:import href="utils/wb_ui_line.xsl"/>
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/wb_gen_form_button.xsl"/>
<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="utils/wb_goldenman.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>
<xsl:import href="share/usr_detail_label_share.xsl"/>

<xsl:output indent="yes"/>
<!-- =============================================================== -->
<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
<xsl:variable name="ugr_display_bil" select="/tree/item/@title"/>
<xsl:variable name="ugr_tcr_id" select="/tree/item/@ugr_tcr_id"/>
<xsl:variable name="ugr_tcr_title" select="/tree/item/@ugr_tcr_title"/>
<xsl:variable name="ugr_grade_code" select="/tree/item/@grade_code"/>
<xsl:variable name="ugr_timestamp" select="/tree/item/@timestamp"/>
<xsl:variable name="tc_enabled" select="/tree/meta/tc_enabled"/>
<xsl:variable name="tc_independent" select="/tree/meta/tc_independent"/>

<!-- =============================================================== -->
<xsl:template match="/">
	<xsl:apply-templates select="tree"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="tree">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="lang_ch">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">修改<xsl:value-of select="$lab_grade"/></xsl:with-param>
		<xsl:with-param name="lab_title">職級標題</xsl:with-param>
		<xsl:with-param name="lab_tcr">培训中心</xsl:with-param>
		<xsl:with-param name="lab_grade_code">編號</xsl:with-param>
		<xsl:with-param name="lab_required">為必填</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_code_desc">(长度不能超过255，且不能包含逗号)</xsl:with-param>
		<xsl:with-param name="lab_name_desc">(不可超過80個字元（40個中文字）及不可有斜線)</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_gb">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">修改<xsl:value-of select="$lab_grade"/></xsl:with-param>
		<xsl:with-param name="lab_title">职级标题</xsl:with-param>
		<xsl:with-param name="lab_tcr">培训中心</xsl:with-param>
		<xsl:with-param name="lab_grade_code">编号</xsl:with-param>
		<xsl:with-param name="lab_required">为必填</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_code_desc">(长度不能超过255，且不能包含逗号)</xsl:with-param>
		<xsl:with-param name="lab_name_desc">(长度不能超过80个字符（40个中文字），且不能包含斜线)</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_en">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">Edit grade</xsl:with-param>
		<xsl:with-param name="lab_title">Grade title</xsl:with-param>
		<xsl:with-param name="lab_tcr">Training center</xsl:with-param>
		<xsl:with-param name="lab_grade_code">Code</xsl:with-param>
		<xsl:with-param name="lab_required">Required</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		<xsl:with-param name="lab_code_desc">(Not more than 255 characters and must not contain comma)</xsl:with-param>
		<xsl:with-param name="lab_name_desc">(Not more than 60 characters and must not contain slash)</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="content">	
	<xsl:param name="lab_nav_title"/>
	<xsl:param name="lab_title"/>
	<xsl:param name="lab_tcr"/>
	<xsl:param name="lab_grade_code"/>
	<xsl:param name="lab_required"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_code_desc"/>
	<xsl:param name="lab_name_desc"/>
	
	<head>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
		<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
		<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
		<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}wb_goldenman.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
		<script language="JavaScript" src="{$wb_js_path}wb_ugr.js"/>
		<script language="JavaScript"><![CDATA[
			ugr = new wbUgr
			var goldenman = new wbGoldenMan
			function cancel(){
				ugr_ent_id = getUrlParam('ugr_ent_id')
				window.location = ugr.ugr_url(ugr_ent_id);
			}
			
		]]></script>
		<!-- CSS -->
		<link rel="stylesheet" href="../static/css/base.css"/>
			<!--alert样式  -->
		<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
		<script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
		<script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_lm_{$wb_cur_lang}.js"/>
		<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
		<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
		<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
		<xsl:call-template name="new_css"/>	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload = "">
	<form name="frmXml">
	<input type="hidden" name="cmd"/>
	<input type="hidden" name="url_success"/>
	<input type="hidden" name="url_failure"/>
	<input type="hidden" name="stylesheet"/>
	<input type="hidden" name="ugr_ent_id"/>
	<input type="hidden" name="ugr_timestamp" value="{$ugr_timestamp}"/>
	<input type="hidden" name="flag" value="true"/>
		<!-- <xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text"><xsl:value-of select="$lab_nav_title"/></xsl:with-param>
		</xsl:call-template> -->
		<table>
			<tbody>
				<tr>
					<td style="text-align:left;" class="wzb-form-label"><xsl:value-of select="$lab_nav_title"/></td>
				</tr>
			</tbody>
		</table>
		<div style="margin-bottom:10px;width: 100%; height: 1px;" class="wzb-ui-line"></div>
		<table>
			<tr> 
				<td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><xsl:value-of select="$lab_grade_code"/>：</td>
				<td class="wzb-form-control">
					<input type="text" class="wzb-inputText" style="width:200px;" name="ugr_grade_code" maxlength="255" value="{$ugr_grade_code}"/>
					<br/>
					<xsl:value-of select="$lab_code_desc"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><xsl:value-of select="$lab_title"/>：</td>
				<td class="wzb-form-control">
					<input type="text" class="wzb-inputText" style="width:200px;" name="ugr_display_bil" maxlength="60" value="{$ugr_display_bil}"/>
					<br/>
					<xsl:value-of select="$lab_name_desc"/>
				</td>
			</tr>
			<!-- 
			<xsl:if test="$tc_independent='true'">
				<tr>
					<td class="wzb-form-label" valign="top" ><span class="wzb-form-star">*</span><xsl:value-of select="$lab_tcr"/>:<xsl:value-of select="//ugr_tcr_id"/></td>
					<td class="wzb-form-control">
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="field_name">tcr_id</xsl:with-param>
							<xsl:with-param name="name">tcr_id</xsl:with-param>
							<xsl:with-param name="box_size">1</xsl:with-param>
							<xsl:with-param name="tree_type">training_center</xsl:with-param>
							<xsl:with-param name="select_type">2</xsl:with-param>
							<xsl:with-param name="pick_leave">0</xsl:with-param>
							<xsl:with-param name="pick_root">0</xsl:with-param>
							<xsl:with-param name="from_eip">1</xsl:with-param>
							<xsl:with-param name="single_option_value"><xsl:value-of select="$ugr_tcr_id"/></xsl:with-param>
							<xsl:with-param name="single_option_text"><xsl:value-of select="$ugr_tcr_title"/></xsl:with-param>
						</xsl:call-template>
						<input type="hidden" name="ugr_tcr_id"/>
					</td>
				</tr>
			</xsl:if>
			 -->
			<tr>
				<td class="wzb-form-label"></td>
				<td class="wzb-form-control"><span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/></td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:ugr.ugr_upd_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:cancel()</xsl:with-param>
			</xsl:call-template>
		</div>
		</form>
	</body>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>

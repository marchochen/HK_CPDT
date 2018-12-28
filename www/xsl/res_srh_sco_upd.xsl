<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="share/wb_layout_share.xsl"/>
	<xsl:import href="share/wb_object_share.xsl"/>
	<xsl:import href="share/res_action_init_share.xsl"/>
	<xsl:import href="share/res_action_share.xsl"/>	
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>	
	<!--<xsl:import href="share/test_player_share.xsl"/>-->
	<xsl:output indent="yes"/>
	<xsl:variable name="mode">UPD</xsl:variable>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_inst">來源</xsl:with-param>
			<xsl:with-param name="lab_file_crs">清單檔案</xsl:with-param>
			<xsl:with-param name="lab_required">注意: 標有*的必須填寫。</xsl:with-param>
			<xsl:with-param name="lab_keep_exist_res">保留現有課件</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_inst">来源</xsl:with-param>
			<xsl:with-param name="lab_file_crs">清单文件</xsl:with-param>
			<xsl:with-param name="lab_required">注意：标有*的必须填写。</xsl:with-param>
			<xsl:with-param name="lab_keep_exist_res">保留现有课件</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_step_inst">Source</xsl:with-param>
			<xsl:with-param name="lab_file_crs">Manifest file</xsl:with-param>
			<xsl:with-param name="lab_required">* Indicates the field is required</xsl:with-param>
			<xsl:with-param name="lab_keep_exist_res">Keep existing source</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">Finish</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="content">
		<xsl:param name="lab_cos_wiz"/>
		<xsl:param name="lab_step_inst"/>
		<xsl:param name="lab_file_crs"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_keep_exist_res"/>
		<xsl:param name="lab_g_form_btn_finish"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			wiz = new wbCosWizard
			
			function status(){
				return false;
			}	
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml" enctype="multipart/form-data" onsubmit="return status()">
				<input value="" name="rename" type="hidden"/>
				<input value="" name="cmd" type="hidden"/>
				<input value="" name="env" type="hidden"/>
				<input value="" name="module" type="hidden"/>
				<input value="" name="imsmanifest_file_name" type="hidden"/>
				<input value="" name="aicc_crs_filename" type="hidden"/>
				<input name="res_subtype" value="RES_SCO" type="hidden"/>
				<input type="hidden" name="res_type" value="SCORM"/>
				<input type="hidden" name="res_src_type" value="SCORM_FILES"/>
				<input value="" name="cos_id" type="hidden"/>
				<input value="" name="obj_id" type="hidden"/>
				<input type="hidden" name="res_id" value="{/resource/@id}"/>
				<input type="hidden" name="res_filename" value="{/resource/body/source}"/>
				<input type="hidden" name="res_timestamp" value="{/resource/@timestamp}"/>
				<input type="hidden" name="res_subtype" value="{/resource/header/@subtype}"/>
				<input type="hidden" name="url_failure" value="javascript:window.location.href=wb_utils_get_cookie('search_result_url')"/>
				<input type="hidden" name="url_success" value="javascript:window.location.href=gen_get_cookie('url_success')"/>
				<xsl:call-template name="draw_header"/>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_step_inst"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="Bg">
					<tr>
						<td width="20%" align="right" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
						<td width="80%" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
					</tr>
					<tr>
						<td align="right" height="10">
							<input type="radio" name="res_format" id="res_format1" value="0" onclick="frmXml.aicc_crs.disabled = false;"/>
						</td>
						<td width="80%" height="10">
							<label for="res_format1">
							<span class="Text">
								<xsl:value-of select="$lab_file_crs"/>:
							</span>
							<input type="file" size="20" style="width:300px;" name="aicc_crs" class="wzb-inputText" disabled="disabled"/>
							</label>
						</td>
					</tr>
					<tr>
						<td width="20%" align="right" valign="top">
							<input type="radio" name="res_format" checked="checked" id="res_format2" value="1" onclick="frmXml.aicc_crs.disabled = true;"/>
						</td>
						<td width="80%">
							<label for="res_format2">
							<span class="Text"><xsl:value-of select="$lab_keep_exist_res"/><!--<xsl:text>：</xsl:text>-->
							<br/>
							<a class="Text" href="{/resource/body/source}" target="_blank">
							<xsl:choose>
								<xsl:when test="string-length(/resource/body/source) &gt; 60">
								<xsl:value-of select="substring(/resource/body/source,0,60)"/>...
								</xsl:when>
								<xsl:otherwise><xsl:value-of select="/resource/body/source"/></xsl:otherwise>
							</xsl:choose>
							</a>
							</span>
							</label>
						</td>
					</tr>
					<tr>
						<td></td>
						<td><xsl:call-template name="show_scorm_files" /></td>
					</tr>
					<input type="hidden" size="20" name="cos_url_prefix" value=""/>
					<tr>
						<td align="right" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
						<td width="80%" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
					</tr>
				</table>
				<xsl:call-template name="wb_ui_line"/>
				<!-- =============================================================== -->
				<xsl:call-template name="res_additional_information">
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="mode">UPD</xsl:with-param>
					<xsl:with-param name="save_function">javascript:wiz.upd_res_scorm_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					<xsl:with-param name="cancel_function">javascript:parent.location.reload();</xsl:with-param>
				</xsl:call-template>
				<!-- =============================================================== -->
				<xsl:call-template name="wb_ui_footer"/>
			</form>
		</body>
	</xsl:template>
<!-- =============================================================== -->
<xsl:template name="show_scorm_files">
	<xsl:for-each select="/resource/body/scorm_info/tableofcontents/item">
		<xsl:call-template name="show_item"/>
	</xsl:for-each >
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="show_item" match="item">
	<xsl:param name="space"/>
	<xsl:choose>
		<xsl:when test="itemtype = 'FDR'"><xsl:value-of select="$space"/><xsl:value-of select="@title"/><br/>
		</xsl:when>
		<xsl:otherwise>&#160;<xsl:value-of select="$space"/><a href="javascript:module_lst.preview_scorm('{src_link}','0',0,'0','','')" class="Text"><xsl:value-of select="@title"/></a><br/>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:apply-templates select="item">
		<xsl:with-param name="space">&#160;<xsl:value-of select="$space"/></xsl:with-param>
	</xsl:apply-templates>
</xsl:template>
</xsl:stylesheet>

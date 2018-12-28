<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="share/wb_layout_share.xsl"/>
	<xsl:import href="share/wb_object_share.xsl"/>
	<xsl:import href="share/res_action_init_share.xsl"/>
	<xsl:import href="share/res_action_share.xsl"/>	
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
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
			<xsl:with-param name="lab_file_zip">壓縮包(zip格式)</xsl:with-param>
			<xsl:with-param name="mange_source">管理資源</xsl:with-param>
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
			<xsl:with-param name="lab_file_zip">压缩包(zip格式)</xsl:with-param>
			<xsl:with-param name="mange_source">管理资源</xsl:with-param>
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
			<xsl:with-param name="lab_file_zip">Packed file(in .zip format)</xsl:with-param>
			<xsl:with-param name="mange_source">Management resources</xsl:with-param>
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
		<xsl:param name="lab_file_zip"/>
		<xsl:param name="mange_source"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_upload_util.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_resource.js" type="text/javascript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script language="JavaScript"><![CDATA[
			var res = new wbResource;
			wiz = new wbCosWizard
			module_lst = new wbModule
			obj = new wbObjective
			var successUrl = 
			function status(){
				return false;
			}
		]]></script>
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
				<input type="hidden" name="url_failure" value="javascript:window.history.go(-1)"/>
				<input type="hidden" name="url_success" value="javascript:window.location.href = wb_utils_invoke_servlet('cmd','get_res','res_id',{/resource/@id},'stylesheet','res_get.xsl','url_failure',self.location.href)"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<span class="NavLink">
							<xsl:for-each select="//header/objective/path/node">
								<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
									<xsl:value-of select="."/>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
							</xsl:for-each>
							<a href="javascript:obj.manage_obj_lst('','{//header/objective/@id}','','','')" class="NavLink">
								<xsl:value-of select="//header/objective/desc"/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="//header/objective/@id"/>','','','false')</xsl:with-param>
								<xsl:with-param name="class">NavLink</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:choose>
								<xsl:when test="//question/header/title">
									<xsl:value-of select="//question/header/title"/>
								</xsl:when>
								<xsl:when test="//resource/body/title">
									<xsl:value-of select="//resource/body/title"/>
								</xsl:when>
							</xsl:choose>
						</span>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">SCORM</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_step_inst"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table cellspacing="0" cellpadding="0" border="0" width="{$wb_gen_table_width}" class="Bg">
					<tr>
						<td width="20%" align="right" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
						<td width="80%" height="10">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
					</tr>
					<tr>
						<td align="right" height="10" valign="top" class="wzb-form-label">
							<span class="wzb-form-star">*</span>
							<input type="radio" name="res_format" id="res_format1" value="0" onclick="frmXml.aicc_crs.disabled = false;frmXml.aicc_zip.disabled = true;"/>
						</td>
						<td width="80%" height="10" class="wzb-form-control">
							<label for="res_format1">
								<xsl:value-of select="$lab_file_crs"/>：
							</label>
							<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_crs</xsl:with-param>
									<xsl:with-param name="onchange">javascript:frmXml.res_format[0].checked=true;</xsl:with-param>
								</xsl:call-template>
						</td>
					</tr>
					
					
					<tr>
						<td align="right" height="10" valign="top" class="wzb-form-label">
							<span class="wzb-form-star">*</span>
							<input type="radio" name="res_format" id="res_format3" value="2" onclick="frmXml.aicc_zip.disabled = false;frmXml.aicc_crs.disabled = true;"/>
						</td>
						<td width="80%" height="10" class="wzb-form-control">
							<label for="res_format3">
								<xsl:value-of select="$lab_file_zip"/>：
							</label>
							<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_zip</xsl:with-param>
									<xsl:with-param name="onchange">javascript:frmXml.res_format[1].checked=true;</xsl:with-param>
								</xsl:call-template>
						</td>
					</tr>
					
					<tr>
						<td width="20%" align="right" valign="top" class="wzb-form-label">
							<input type="radio" name="res_format" checked="checked" id="res_format2" value="1" onclick="frmXml.aicc_crs.disabled = true;frmXml.aicc_zip.disabled = true;"/>
						</td>
						<td width="80%" class="wzb-form-control">
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
							<br/><xsl:call-template name="show_scorm_files" />
						</td>
					</tr>
					<input type="hidden" size="20" name="cos_url_prefix" value=""/>
					<!-- 这个是隐藏的<ifame>作为表单提交后处理的后台目标-->
					<iframe id='target_upload' name='target_upload' src='' style='display:none'></iframe>
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
					<xsl:with-param name="cancel_function">javascript:window.history.go(-1);</xsl:with-param>
				</xsl:call-template>
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

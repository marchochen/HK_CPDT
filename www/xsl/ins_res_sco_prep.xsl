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
	<!--<xsl:import href="share/test_player_share.xsl"/>-->
	<xsl:output indent="yes"/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="mange_source">管理資源</xsl:with-param>
			<xsl:with-param name="lab_step_inst">來源</xsl:with-param>
			<xsl:with-param name="lab_file_crs">清單檔案</xsl:with-param>
			<xsl:with-param name="lab_required">注意: 標有*的必須填寫。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_zip">壓縮包(zip格式)</xsl:with-param>
			<xsl:with-param name="lab_sco_ver">請選擇Scorm版本</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_1.2">1.2</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_2004">2004</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_notes">請和您的課件供應商確定該課件的版本，選擇不正確的版本可能會導致課件無法播放。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="mange_source">管理资源</xsl:with-param>
			<xsl:with-param name="lab_step_inst">来源</xsl:with-param>
			<xsl:with-param name="lab_file_crs">清单文件</xsl:with-param>
			<xsl:with-param name="lab_required">注意：标有*的必须填写。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_zip">压缩包(zip格式)</xsl:with-param>
			<xsl:with-param name="lab_sco_ver">请选择Scorm版本</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_1.2">1.2</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_2004">2004</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_notes">请和您的课件供应商确定该课件的版本，选择不正确的版本可能会导致课件无法播放。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="mange_source">Management resources</xsl:with-param>
			<xsl:with-param name="lab_step_inst">Source</xsl:with-param>
			<xsl:with-param name="lab_file_crs">Manifest file</xsl:with-param>
			<xsl:with-param name="lab_required">* Indicates the field is required</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">Finish</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_file_zip">Packed file(in .zip format)</xsl:with-param>
			<xsl:with-param name="lab_sco_ver">Please choose <b>Scorm</b> Version</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_1.2">1.2</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_2004">2004</xsl:with-param>
			<xsl:with-param name="lab_sco_ver_notes">Please confirm the right version with your courseware provider, for the courseware wont't be played with wrong version.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="content">
		<xsl:param name="lab_cos_wiz"/>
		<xsl:param name="mange_source"/>
		<xsl:param name="lab_step_inst"/>
		<xsl:param name="lab_file_crs"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_g_form_btn_finish"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_file_zip"/>
		<xsl:param name="lab_sco_ver"/>
		<xsl:param name="lab_sco_ver_1.2"/>
		<xsl:param name="lab_sco_ver_2004"/>
		<xsl:param name="lab_sco_ver_notes"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_upload_util.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.form.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script language="JavaScript"><![CDATA[
			wiz = new wbCosWizard
				obj = new wbObjective
			function status(){
				return false;
			}
			function change_btn(frm){
		  	if(frm.src_type){
		  		if (frm.src_type.length>=2 && frm.src_type[1].checked) {
		  			frm.aicc_crs.outerHTML = frm.aicc_crs.outerHTML;
		  		}else if (frm.src_type[0].checked){
		  			frm.aicc_zip.outerHTML = frm.aicc_zip.outerHTML;
		  		}
		  	}
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
				<!-- url_success/url_failure: Firefox 不可以使用 location.reload()，因为在Firefox，
				     history.go(-1)会触发子frame的reload()。如果该子frame是一个form post的结果，
				      浏览器会提示一般刷新form post的警告。
				-->
				<input type="hidden" name="url_failure" value="javascript:location.href = wb_utils_get_cookie('mod_url');"/>
				<input type="hidden" name="url_success" value="javascript:location.href = wb_utils_get_cookie('mod_url');"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text"><xsl:value-of select="$lab_operate_add"></xsl:value-of>SCORM</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
						<span class="NavLink">
						
							<xsl:for-each select="//user/objective/path/node">
								<a href="javascript:obj.manage_obj_lst('','{@id}','','','')" class="NavLink">
									<xsl:value-of select="."/>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
							</xsl:for-each>
						
							<a href="javascript:obj.manage_obj_lst('','{//user/objective/@id}','','','false')" class="NavLink">
									<xsl:value-of select="//user/objective/desc"/>
							</a>
							 
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							
							<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="//user/objective/@id"/>','','','false')</xsl:with-param>
										<xsl:with-param name="class">NavLink</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$lab_operate_add"></xsl:value-of> SCORM
						</span>
					</xsl:with-param>
				</xsl:call-template>
				
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_step_inst"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table class="margin-top28">
					<tr>
						<td valign="top" align="right" height="10">
							<span class="wzb-form-star">*</span>
						</td>
						<td width="80%" height="10">
								<input type="radio" name="src_type" id="rdo_src_type_pick_from_file" value="" onclick="javascript:change_btn(document.frmXml)" checked="checked"/>
								<label for="rdo_src_type_pick_from_file">
								<xsl:value-of select="$lab_file_crs"/>：</label>
								<br/>
							<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_crs</xsl:with-param>
									<xsl:with-param name="onchange">javascript:frmXml.src_type[0].checked=true;change_btn(document.frmXml)</xsl:with-param>
								</xsl:call-template>
						</td>
					</tr>
					<input type="hidden" size="20" name="cos_url_prefix" value=""/>
					
					<tr style="height: 45px;">
						<td align="right" height="10">
								&#160;
						</td>
						<td width="80%" height="10">
						         <br/>
						        <input type="radio" name="src_type" id="rdo_src_type_pick_from_zip_file" value="" onclick="javascript:change_btn(document.frmXml)"/>
								<label for="rdo_src_type_pick_from_zip_file">
								<xsl:value-of select="$lab_file_zip"/>：</label>
								<br/>
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_zip</xsl:with-param>
									<xsl:with-param name="onchange">javascript:frmXml.src_type[1].checked=true;change_btn(document.frmXml)</xsl:with-param>
								</xsl:call-template>
						</td>
					</tr>
					<tr id="sco_ver_chooser">
						<td align="right" valign="top"></td>
						<td  >
						    <br/>
						    <xsl:value-of select="$lab_sco_ver"/>：
						    <input type="radio" name="sco_ver" id="sco_ver_12" value="1.2" checked="checked"/>
							<label for="sco_ver12"><xsl:value-of select="$lab_sco_ver_1.2"/></label>&#160;&#160;
							<input type="radio" name="sco_ver" id="sco_ver2004" value="2004"/>
							<label for="sco_ver_2004"><xsl:value-of select="$lab_sco_ver_2004"/></label><br/>
							<span class="Text" style="color:red"><xsl:value-of select="$lab_sco_ver_notes"/></span>
						 </td>
					</tr>
					<!-- 这个是隐藏的<ifame>作为表单提交后处理的后台目标-->
				    <iframe id='target_upload' name='target_upload' src='' style='display: none'></iframe>
				    <tr>
						<td>
						</td>
						<td height="10" class="wzb-ui-module-text">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
						</td>
					</tr>
				</table>
				<!-- =============================================================== -->
				<xsl:call-template name="res_additional_information">
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="mode">INS</xsl:with-param>
					<xsl:with-param name="save_function">javascript:wiz.ins_res_scorm_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					<xsl:with-param name="cancel_function">javascript:history.go(-1);</xsl:with-param>
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>

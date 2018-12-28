<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>	
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/draw_res_option_list.xsl"/>

	<xsl:output indent="yes"/>

	<xsl:variable name="cur_lan" select="/upload_res/res_upload_limit/meta/cur_usr/@curLan"/>
	<xsl:variable name="cur_site" select="/upload_res/res_upload_limit/meta/cur_usr/@root_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="upload_res">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess;
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return false" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="NO"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="que_type"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" />
				<input type="hidden" name="url_failure" />
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	
	
	
	
	<xsl:variable name="lab_g_form_btn_next" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_144')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_145')"/>
	<xsl:variable name="lab_file_location" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_146')"/>
	<xsl:variable name="lab_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_147')"/>
	<xsl:variable name="lab_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_148')"/>
	<xsl:variable name="lab_description" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_149')"/>
	<xsl:variable name="lab_history" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_150')"/>
	<xsl:variable name="lab_que_type" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_151')"/>
	<xsl:variable name="lab_upload_instruction" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_152')"/>
	<xsl:variable name="lab_desc_requirement" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_153')"/>
	<xsl:variable name="lab_option_1" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_154')"/>
	<xsl:variable name="lab_option_2" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_155')"/>
	<xsl:variable name="lab_template" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_156')"/>
	<xsl:variable name="lab_instr" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_157')"/>
	
	<xsl:variable name="upload_limit_count">
		<xsl:value-of select="//res_upload_limit/text()"/>
	</xsl:variable>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		
	
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
			<xsl:with-param name="extra_td">
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_template"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Import.get_template(document.frmXml, '<xsl:value-of select="$cur_lan"></xsl:value-of>')</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_instr"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Import.get_instr(document.frmXml)</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
					</xsl:call-template>
					<!-- <xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_history"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Log.get_log()</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
					</xsl:call-template> -->
				</td>
			</xsl:with-param>
		</xsl:call-template>
		
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_que_type"/>：
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td>
								<select name="que_type" class="wzb-form-select">
									<xsl:call-template name="draw_que_option_list"/>
								</select>
							</td>
							<td>
								<input type="hidden" name="lab_que_type" value="{$lab_que_type}"/>						
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_file_location"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="wb_gen_input_file">
						<xsl:with-param name="name">src_filename_path</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="src_filename"/>
					<br/>
					<span class="wzb-form-label">
						<xsl:value-of select="$lab_upload_instruction"/><xsl:value-of select="$upload_limit_count"></xsl:value-of>
					</span>
				</td>
			</tr>
			<tr style="display:none;">
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_description"/>：
				</td>
				<td class="wzb-form-control" >
					<textarea class="wzb-inputTextArea" name="ulg_desc" style="width:300px;" rows="4"/>
					<br/>
					<xsl:value-of select="$lab_desc_requirement"/>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right">
				</td>
				<td width="80%">
					<table>
						<tr>
							<td width="1" valign="top">
								<input type="radio" name="allow_update" id="opt_1" value="true" checked="checked"/>
							</td>
							<td>
								<label for="opt_1" class="margin-right5" style="font-weight: normal;">
									<xsl:value-of select="$lab_option_1"/>
								</label>
							</td>
						</tr>
						<tr>
							<td width="1" valign="top">
								<input type="radio" name="allow_update" id="opt_2" value="false"/>
							</td>
							<td>
								<label for="opt_2" class="margin-right5" style="font-weight: normal;">
									<xsl:value-of select="$lab_option_2"/>
								</label>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Import.execRewriting(document.frmXml,'<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="$lab_que_type"/>')</xsl:with-param>
			</xsl:call-template>					
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_nav_go('FTN_AMD_RES_MAIN',<xsl:value-of select="//cur_usr/@ent_id"/>)</xsl:with-param>
			</xsl:call-template>					
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

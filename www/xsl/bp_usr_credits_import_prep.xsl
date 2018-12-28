<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>	
	<xsl:output indent="yes"/>
	
	<xsl:variable name="cur_lan" select="/Upload/meta/cur_usr/@curLan"/>
	<xsl:variable name="cur_site" select="/Upload/meta/cur_usr/@root_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="Upload">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_credit.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess;
			var credit = new wbCredit;
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return false" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="NO"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="template_url"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">檔位置</xsl:with-param>
			<xsl:with-param name="lab_desc">在下面指定包含積分資訊的檔。該檔必須符合指定的試算表範本格式。</xsl:with-param>
			<xsl:with-param name="lab_title">匯入積分資訊 - 第一步：積分檔上載</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(不超過2000個字元)</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上載文檔內的記錄數目上限為<xsl:value-of select="max_upload_count"/>。如要上載更多的記錄，請分批上載。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_log">查看記錄</xsl:with-param>
			<xsl:with-param name="lab_template">文檔範本</xsl:with-param>
			<xsl:with-param name="lab_instr">範本說明</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_desc">在下面指定包含积分信息的文件。该文件必须符合指定的电子表格模板格式。</xsl:with-param>
			<xsl:with-param name="lab_title">导入积分信息 - 第一步：积分文件上载</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(不超过2000个字符)</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上载文件内的记录数目上限为<xsl:value-of select="max_upload_count"/>。如要上载更多的记录，请分批上载。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_log">查看记录</xsl:with-param>
			<xsl:with-param name="lab_template">文件模板</xsl:with-param>
			<xsl:with-param name="lab_instr">模板说明</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_file_location">File location</xsl:with-param>
			<xsl:with-param name="lab_desc">Specify the file containing the credit profiles below. The file must be in a specific format according to a spreadsheet template.</xsl:with-param>
			<xsl:with-param name="lab_title">Import credit profile - step 1: file upload</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(Not more than 2000 characters)</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">Maximum number of records allowed in the file is <xsl:value-of select="max_upload_count"/>. Please upload in separate batches if more records are intended.</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_log">View history</xsl:with-param>
			<xsl:with-param name="lab_template">File template</xsl:with-param>
			<xsl:with-param name="lab_instr">Template instruction</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_file_location"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_desc_requirement"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_upload_instruction"/>
		<xsl:param name="lab_g_txt_btn_log"/>
		<xsl:param name="lab_template"/>
		<xsl:param name="lab_instr"/>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
			<xsl:with-param name="page_title">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">lab_import_credit_info</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
			
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>
		<table>
			<tr>
				<td align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_template"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Credit.get_tpl(document.frmXml, '<xsl:value-of select="$cur_lan"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_instr"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Credit.get_instr()</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_log"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Credit.get_log()</xsl:with-param>
					</xsl:call-template>
				</td>
	
			</tr>
		</table>

		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
				</td>
				<!--  
				<td class="wzb-form-control">
					<a href="javascript:Batch.Credit.get_tpl(document.frmXml, '{$cur_lan}')" class="Text"><xsl:value-of select="$lab_template"/></a>					
					<xsl:text>&#160;|&#160;</xsl:text>
					<a href="javascript:Batch.Credit.get_instr()" class="Text"><xsl:value-of select="$lab_instr"/></a>
				</td>
			-->
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_file_location"/>：
				</td>
				<td class="wzb-form-control">
					<span>
						<xsl:call-template name="wb_gen_input_file">
							<xsl:with-param name="name">src_filename_path</xsl:with-param>
						</xsl:call-template>
						<input type="hidden" name="src_filename"/>
					</span>
					<span style="font-size: 12px;">
						*<xsl:value-of select="$lab_upload_instruction"/>
					</span>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_description"/>：
				</td>
				<td class="wzb-form-control" >
					<textarea class="wzb-inputTextArea" name="upload_desc" style="width:300px;" rows="4"/>
					<br/>
					<span style="font-size: 12px;">
						<xsl:value-of select="$lab_desc_requirement"/>
					</span>
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
				<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Credit.exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:credit.set_learner_point()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

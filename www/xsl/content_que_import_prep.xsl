<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/draw_res_option_list.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	
	<xsl:variable name="cur_lan" select="/content/meta/cur_usr/@curLan"/>
	<xsl:variable name="cur_site" select="/content/meta/cur_usr/@root_id"/>
	<xsl:variable name="obj_id" select="/content/obj/@id"/>
	<xsl:variable name="que_type" select="/content/que_type/@name"/>
	<xsl:variable name="mod_type" select="/content/mod_type/@name"/>
	<xsl:variable name="wb_gen_table_width">585</xsl:variable>
	<!-- =============================================================== -->
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:apply-templates />
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="content">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_content.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" src="{$wb_js_path}jquery.js"/>
			<!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			
			
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			cont = new wbContent;
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<div style="margin:10px">
				<form name="frmXml" onsubmit="return false" enctype="multipart/form-data">
					<input type="hidden" name="rename" value="NO"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="que_type"/>
					<input type="hidden" name="module" value=""/>
					<input type="hidden" name="url_success" />
					<input type="hidden" name="url_failure" />
					<input type="hidden" name="que_obj_id" value="{$obj_id}"/>
					<input type="hidden" name="mod_type" value="{$mod_type}"/>
			
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</div>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">檔案位置</xsl:with-param>
			<xsl:with-param name="lab_desc">在下面指定包含題目的檔案。該檔必須符合指定的試算表範本格式。</xsl:with-param>
			<xsl:with-param name="lab_title">匯入題目資源 - 第一步：檔案上載</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_history">查看記錄</xsl:with-param>
			<xsl:with-param name="lab_que_type">題目類型</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上載文檔內的記錄數目上限為200。如要上載更多的記錄，請分批上載。</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(不超過1000個字元)</xsl:with-param>
			<xsl:with-param name="lab_option_1">對系統中已存在的記錄執行更新操作</xsl:with-param>
			<xsl:with-param name="lab_option_2">對系統中已存在的記錄作為錯誤記錄處理</xsl:with-param>
			<xsl:with-param name="lab_template">文檔範本</xsl:with-param>
			<xsl:with-param name="lab_instr">範本說明</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_desc">在下面指定包含题目的文件。该文件必须符合指定的电子表格模板格式。</xsl:with-param>
			<xsl:with-param name="lab_title">导入题目资源 - 第一步：文件上载</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_history">查看记录</xsl:with-param>
			<xsl:with-param name="lab_que_type">题目类型</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上载文件内的记录数目上限为200。如要上载更多的记录，请分批上载。</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(不超过1000个字符)</xsl:with-param>
			<xsl:with-param name="lab_option_1">对系统中已存在的记录执行更新操作</xsl:with-param>
			<xsl:with-param name="lab_option_2">对系统中已存在的记录作为错误记录处理</xsl:with-param>
			<xsl:with-param name="lab_template">文件模板</xsl:with-param>
			<xsl:with-param name="lab_instr">模板说明</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_file_location">File location</xsl:with-param>
			<xsl:with-param name="lab_desc">Specify the file containing the questions. The file must be in a specific format according to a spreadsheet template.</xsl:with-param>
			<xsl:with-param name="lab_title">Import question – step 1: file upload</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_history">View history</xsl:with-param>
			<xsl:with-param name="lab_que_type">Question type</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">Maximum number of records allowed in the file is 200. Please upload in separate batches if more records are intended.</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(Not more than 1000 characters)</xsl:with-param>
			<xsl:with-param name="lab_option_1">Handle records that are found existing in system as data update</xsl:with-param>
			<xsl:with-param name="lab_option_2">Handle records that are found existing in system as errors</xsl:with-param>
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
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_history"/>
		<xsl:param name="lab_que_type"/>
		<xsl:param name="lab_template"/>
		<xsl:param name="lab_instr"/>
		<xsl:param name="lab_upload_instruction"/>
		<xsl:param name="lab_desc_requirement"/>
		<xsl:param name="lab_option_1"/>
		<xsl:param name="lab_option_2"/>
		
		<!-- <xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template> -->
		<xsl:value-of select="$lab_title"/>
		<xsl:call-template name="wb_ui_line"/>
		
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_que_type"/>：
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td>
								<select name="que_type" class="wzb-select">
									<xsl:call-template name="draw_que_option_list_content">
									</xsl:call-template>
								</select>
							</td>
							<td>
								<input type="hidden" name="lab_que_type" value="{$lab_que_type}"/>
								<xsl:text>&#160;&#160;</xsl:text>
								<a href="javascript:cont.ImpTemplate(document.frmXml, '{$cur_lan}')" class="btn wzb-btn-orange margin-right4"><xsl:value-of select="$lab_template"/></a>					
								<!--<xsl:text>&#160;&#160;</xsl:text>                     class="text"  -->
								<a href="javascript:cont.ImpInstr(document.frmXml,'{$mod_type}')" class="btn wzb-btn-orange margin-right4"><xsl:value-of select="$lab_instr"/></a>							
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
				<!--<input type="file" name="src_filename_path" class="wzb-inputText" style="width: 300;"/> -->
					<input type="hidden" name="src_filename"/>
					<br/>
					<span>
						<xsl:value-of select="$lab_upload_instruction"/>
					</span>
				<!-- <xsl:value-of select="$lab_upload_instruction"/>  -->	
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
			<tr style="display:none">
				<td class="wzb-form-label" >
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td width="1">
								<input type="radio" name="allow_update" id="opt_1" value="true" checked="checked"/>
							</td>
							<td>
								<label for="opt_1">
									<span class="Text">
										<xsl:value-of select="$lab_option_1"/>
									</span>
								</label>
							</td>
						</tr>
						<tr>
							<td width="1">
								<input type="radio" name="allow_update" id="opt_2" value="false"/>
							</td>
							<td>
								<label for="opt_2">
									<span class="Text">
										<xsl:value-of select="$lab_option_2"/>
									</span>
								</label>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-labell">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:cont.ImpExec(document.frmXml,'<xsl:value-of select="$wb_lang"/>', '<xsl:value-of select="$lab_que_type"/>')</xsl:with-param>
			</xsl:call-template>					
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:cont.prep_page();</xsl:with-param>
			</xsl:call-template>					
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

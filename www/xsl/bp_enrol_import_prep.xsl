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
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="cur_lan" select="/applyeasy/meta/cur_usr/@curLan"/>
	<xsl:variable name="cur_site" select="/applyeasy/meta/cur_usr/@root_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess;
			app = new wbApplication
			itm_lst = new wbItem
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return status()" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="NO"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="itm_id" value="{/applyeasy/item/@id}"/>
				<input type="hidden" value="INSERT" name="upload_type"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_file_location">文檔位置</xsl:with-param>
			<xsl:with-param name="lab_desc_1">在下面指定包含報名記錄資訊的文檔。該檔必須符合指定的試算表範本格式。</xsl:with-param>
			<xsl:with-param name="lab_title">匯入報名記錄 – 第一步：文檔上載</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_option_1">將系統中的記錄作為登記更新記錄</xsl:with-param>
			<xsl:with-param name="lab_option_2">將所有記錄都作為新的登記記錄</xsl:with-param>
			<xsl:with-param name="lab_option_3">將系統中的登記記錄作為錯誤記錄</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_view_history">查看記錄</xsl:with-param>
			<xsl:with-param name="lab_template">文檔範本</xsl:with-param>
			<xsl:with-param name="lab_instr">範本說明</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">處理报名</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">匯入報名記錄</xsl:with-param>
			<xsl:with-param name="lab_run_info">班級資訊</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(不超過2000個字元)</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上載文檔內的記錄數目上限為<xsl:value-of select="max_upload_count"/>。如要上載更多的記錄，請分批上載。</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction2">注意﹕如上載文件內的記錄數目多於<xsl:value-of select="spawn_threshold"/>﹐系統會在背景處理上載的請求。 當完成上載的請求後﹐系統會以電郵通知你。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_desc_1">在下面指定包含报名记录信息的文件。该文件必须符合指定的电子表格模板格式。</xsl:with-param>
			<xsl:with-param name="lab_title">导入报名记录 – 第一步：文件上载</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_option_1">将系统中的记录作为登记更新记录</xsl:with-param>
			<xsl:with-param name="lab_option_2">将所有记录都作为新的登记记录</xsl:with-param>
			<xsl:with-param name="lab_option_3">将系统中的登记记录作为错误记录</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_view_history">查看记录</xsl:with-param>
			<xsl:with-param name="lab_template">文件模板</xsl:with-param>
			<xsl:with-param name="lab_instr">模板说明</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">处理报名</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">导入报名记录</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(不超过2000个字符)</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">上载文件内的记录数目上限为<xsl:value-of select="max_upload_count"/>。如要上载更多的记录，请分批上载。</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction2">注意：如上载文件内的记录数目多于<xsl:value-of select="spawn_threshold"/>，系统会在后台处理上载的请求。 当完成上载的请求后，系统会以电邮通知你。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_file_location">File location</xsl:with-param>
			<xsl:with-param name="lab_desc_1">Specify the file containing the enrollments below. The file must be in a specific format according to a spreadsheet template.</xsl:with-param>
			<xsl:with-param name="lab_title">Import enrollment - step 1: file upload</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_option_1">Handle records that are found existing in system as enrollment update</xsl:with-param>
			<xsl:with-param name="lab_option_2">Handle records all records as new enrollment</xsl:with-param>
			<xsl:with-param name="lab_option_3">Handle records that are found existing in system as errors</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_view_history">View history</xsl:with-param>
			<xsl:with-param name="lab_template">File template</xsl:with-param>
			<xsl:with-param name="lab_instr">Template instruction</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">Process enrollment</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">Import enrollment</xsl:with-param>
			<xsl:with-param name="lab_run_info">Class information</xsl:with-param>
			<xsl:with-param name="lab_desc_requirement">(Not more than 2000 characters)</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction">Maximum number of records allowed in the file is <xsl:value-of select="max_upload_count"/>. Please upload in separate batches if more records are intended.</xsl:with-param>
			<xsl:with-param name="lab_upload_instruction2">Note: The import process will be conducted in a separate background job if the number of  records exceeds <xsl:value-of select="spawn_threshold"/>. A notification email will be sent to you upon completion.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_file_location"/>
		<xsl:param name="lab_desc_1"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_option_1"/>
		<xsl:param name="lab_option_2"/>
		<xsl:param name="lab_option_3"/>
		<xsl:param name="lab_enrollment_approval"/>
		<xsl:param name="lab_enrollment_upload"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_view_history"/>
		<xsl:param name="lab_desc_requirement"/>
		<xsl:param name="lab_upload_instruction"/>
		<xsl:param name="lab_upload_instruction2"/>
		<xsl:param name="lab_template"/>
		<xsl:param name="lab_instr"/>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">111</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<!--============================================================================-->
		<!--
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="item/@run_ind='true'">
						<a href="Javascript:itm_lst.get_item_detail({item/parent/@id})" class="NavLink">
							<xsl:value-of select="item/parent/@title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_list({item/parent/@id})" class="NavLink">
							<xsl:choose>
								<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
							</xsl:choose>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_detail({item/@id})" class="NavLink">
							<xsl:value-of select="item/@title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="Javascript:itm_lst.get_item_detail({item/@id})" class="NavLink">
							<xsl:value-of select="item/@title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="Javascript:app.get_application_list('',{item/@id})" class="NavLink">
					<xsl:value-of select="$lab_enrollment_approval"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:value-of select="$lab_enrollment_upload"/>
			</xsl:with-param>
		</xsl:call-template>
		-->
		<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="/applyeasy/item/@run_ind = 'false'">
								<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
									<xsl:value-of select="/applyeasy/item/title"/>
								</a>
								<span class="NavLink"><xsl:text>&#160;&gt;&#160;</xsl:text></span>
								<a href="javascript:app.get_application_list('',{$itm_id})" class="NavLink">
									<xsl:value-of select="$lab_enrollment_approval"/>
								</a>
								<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_enrollment_upload"/>								</span>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="/applyeasy/item/nav/item" mode="nav">
									<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
									<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
								</xsl:apply-templates>
								<span class="NavLink"><xsl:text>&#160;&gt;&#160;</xsl:text></span>
								<a href="javascript:app.get_application_list('',{$itm_id})" class="NavLink">
									<xsl:value-of select="$lab_enrollment_approval"/>
								</a>
								<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_enrollment_upload"/>								</span>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
		<!--============================================================================-->
		
		<table>
			<tr>
				<td height="10" colspan="2">
					<xsl:call-template name="wb_ui_desc">
						<xsl:with-param name="text" select="$lab_desc_1"/>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td width="60%">
					<!--

					<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="width">100%</xsl:with-param>
						
					</xsl:call-template>
					-->
				</td>
				<td height="10" align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_template"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.get_tpl('<xsl:value-of select="$cur_lan"/>', '<xsl:value-of select="$cur_site"/>')</xsl:with-param>
					</xsl:call-template>	
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_instr"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.get_instr()</xsl:with-param>
					</xsl:call-template>	
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange </xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_view_history"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Log.get_log('<xsl:value-of select="item/@id"/>')</xsl:with-param>
					</xsl:call-template>		
				</td>
			</tr>

		</table>
		<table>
			<!--
			<tr>
				<td width="20%" align="right">
					<span class="TitleText">*<xsl:value-of select="$lab_file_location"/>:</span>
				</td>
				<td width="80%">
					<input type="file" name="src_filename_path" style="width: 300;" class="wzb-inputText"/>
					<input type="hidden" name="src_filename"/>
				</td>
			</tr>
			-->		
			<tr>
				<td class="wzb-form-label" align="right" valign="top">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_file_location"/>：
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="wb_gen_input_file">
						<xsl:with-param name="name">src_filename_path</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="src_filename"/>
					<span class="wzb-ui-desc-text">
						<xsl:value-of select="$lab_upload_instruction"/>
						<br/>
						<xsl:value-of select="$lab_upload_instruction2"/>
					</span>
				</td>
			</tr>
			<!--
			<tr>
				<td width="20%" align="right">
					<span class="TitleText">
						<xsl:value-of select="$lab_description"/>:</span>
				</td>
				<td width="80%">
					<input type="text" name="upload_desc" style="width: 300;" class="wzb-inputText"/>
				</td>
			</tr>
			-->
			<tr>
				<td align="right" valign="top" class="wzb-form-label">
					<xsl:value-of select="$lab_description"/>：
				</td>
				<td class="wzb-form-control">
					<textarea class="wzb-inputTextArea" name="upload_desc" style="width:300px;" rows="4"/>
					<br/>
					<span class="wzb-ui-desc-text">
						<xsl:value-of select="$lab_desc_requirement"/>
					</span>
				</td>
			</tr>
			<!--<tr>
				<td width="20%" align="right">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%">
					<table cellpadding="0" cellspacing="0" width="100%" border="0">
						<tr>
							<td>
								<input type="radio" value="INSERT_N_UPDATE" name="upload_type"/>
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
							<td>
								<input type="radio" value="INSERT_ON_DUPLICATE" name="upload_type"/>
							</td>
							<td>
								<label for="opt_2">
									<span class="Text">
										<xsl:value-of select="$lab_option_2"/>
									</span>
								</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" value="INSERT" name="upload_type"/>
							</td>
							<td>
								<label for="opt_2">
									<span class="Text">
										<xsl:value-of select="$lab_option_3"/>
									</span>
								</label>
							</td>
						</tr>
					</table>
				</td>
			</tr>-->
			<tr>
				<td class="wzb-form-label" align="right">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:app.get_application_list('','<xsl:value-of select="item/@id"/>')</xsl:with-param>
			</xsl:call-template>
		</div>
	</div>
	</xsl:template>
	
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<span class="NavLink">&#160;&gt;&#160;</span>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=last()">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<span>&#160;&gt;&#160;</span>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- =============================================================== -->
</xsl:stylesheet>

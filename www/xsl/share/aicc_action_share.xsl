<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../cust/wb_cust_const.xsl"/>
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../utils/escape_doub_quo.xsl"/>
	<xsl:import href="../utils/wb_gen_form_button.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>
	<xsl:import href="../utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>
	<xsl:import href="../utils/wb_ui_footer.xsl"/>
	<xsl:import href="../utils/wb_ui_space.xsl"/>
	<xsl:import href="../utils/wb_gen_input_file.xsl"/>
	<xsl:import href="res_label_share.xsl"/>
	<!-- ===============================================-->
	<xsl:variable name="res_id" select="/resource/@id"/>
	<xsl:variable name="timestamp" select="/resource/@timestamp"/>
	<xsl:variable name="subtype" select="/resource/header/@subtype"/>
	<!-- ===============================================-->
	<xsl:template name="aicc_html_header">
		<xsl:param name="mode"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_aicc.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_upload_util.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script language="JavaScript"><![CDATA[	
			obj = new wbObjective
				aicc =new wbAicc
				function init() {
					return;
				}
				function get_focus() {
					if(document.frmXml.res_format != null)
						document.frmXml.res_format[0].checked=true
					return	
				}
				function read_aicc() {
					aicc.read(']]><xsl:value-of select="/resource/@id"/><![CDATA[')
				}
				function check_aicc(){
					(document.frmXml.res_format) && (document.frmXml.res_format[1]) && (document.frmXml.res_format[1].checked = true);
				}
				]]></script>
		</head>
	</xsl:template>
	<!-- ====================================================== -->
	<xsl:template name="aicc_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="url_success"/>
		<xsl:param name="url_failure"/>
		<xsl:param name="src"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="aicc_body_">
					<xsl:with-param name="lab_source">來源</xsl:with-param>
					<xsl:with-param name="lab_upload_aicc">上傳 AICC 檔案</xsl:with-param>
					<xsl:with-param name="lab_upload_aicc_zip">上傳 AICC 檔案壓縮包</xsl:with-param>
					<xsl:with-param name="lab_keep_exist_res">保留現有課件</xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
					<xsl:with-param name="src" select="$src"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="aicc_body_">
					<xsl:with-param name="lab_source">来源</xsl:with-param>
					<xsl:with-param name="lab_upload_aicc">上传AICC文件</xsl:with-param>
					<xsl:with-param name="lab_upload_aicc_zip">上传 AICC 压缩包</xsl:with-param>
					<xsl:with-param name="lab_keep_exist_res">保留现有课件</xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
					<xsl:with-param name="src" select="$src"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="aicc_body_">
					<xsl:with-param name="lab_source">Source</xsl:with-param>
					<xsl:with-param name="lab_upload_aicc">Upload AICC Files</xsl:with-param>
					<xsl:with-param name="lab_keep_exist_res">Keep existing source</xsl:with-param>
					<xsl:with-param name="lab_upload_aicc_zip">Upload AICC zipped files</xsl:with-param>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="url_success" select="$url_success"/>
					<xsl:with-param name="url_failure" select="$url_failure"/>
					<xsl:with-param name="src" select="$src"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================================================== -->
	<xsl:template name="aicc_body_">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="url_success"/>
		<xsl:param name="url_failure"/>
		<xsl:param name="lab_source"/>
		<xsl:param name="lab_upload_aicc"/>
		<xsl:param name="lab_upload_aicc_zip"/>
		<xsl:param name="lab_keep_exist_res"/>
		<xsl:param name="src"/>
		<input type="hidden" name="res_type" value="AICC"/>
		<input type="hidden" name="obj_id"/>
		<input type="hidden" name="annotation_html"/>
		<input type="hidden" name="res_src_link" value="{$src}"/>
		<input type="hidden" name="res_src_type"/>
		<input type="hidden" name="zip_filename"/>
		<input type="hidden" name="url_failure" value="{$url_failure}"/>
		<input type="hidden" name="url_success" value="javascript:parent.self.location.href = gen_get_cookie('url_success')"/>
		<input type="hidden" name="res_subtype" value="SSC"/>
		<input type="hidden" name="module" value=""/>
		<xsl:choose>
			<xsl:when test="$mode='INS'">
				<input type="hidden" name="cmd" value="ins_res"/>
			</xsl:when>
			<xsl:when test="$mode='UPD'">
				<input type="hidden" name="cmd" value="upd_res"/>
				<input type="hidden" name="res_id" value="{$res_id}"/>
				<input type="hidden" name="res_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="res_subtype" value="{$subtype}"/>
			</xsl:when>
			<xsl:when test="$mode='PST'">
				<input type="hidden" name="cmd" value="ins_res"/>
				<input type="hidden" name="res_id" value="{$res_id}"/>
				<input type="hidden" name="res_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="res_subtype" value="{$subtype}"/>
				<input type="hidden" name="copy_media_from" value="{$res_id}"/>
			</xsl:when>
		</xsl:choose>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_source"/>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table class="margin-top28">
			<tr>
				<td align="right" widht="20%">
				</td>
				<td width="80%">
					<table>
						<tr>
							<td align="left" valign="top" colspan="2">
								<label for="rdo_res_format_1">
									<input type="radio" id="rdo_res_format_1" name="res_format" value="AICC_FILES" checked="checked" onFocus="this.form.res_format[0].checked=true">
										<xsl:if test="$mode != 'INS'">
											<xsl:attribute name="checkec">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_upload_aicc"/>
									<xsl:text>：</xsl:text>
								</label>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<span class="wzb-form-star">*</span>The Course File(CRS)
								<br/>
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_crs</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:get_focus()</xsl:with-param>
									
								</xsl:call-template>
								<input type="hidden" name="aicc_crs_filename"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<span class="wzb-form-star">*</span>Course Structure File(CST)
								<br/>
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_cst</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:get_focus()</xsl:with-param>
									
								</xsl:call-template>
								<input type="hidden" name="aicc_cst_filename"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<span class="wzb-form-star">*</span>Description File(DES)
								<br/>
								
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_des</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:get_focus()</xsl:with-param>
									
								</xsl:call-template>
								<input type="hidden" name="aicc_des_filename"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								<span class="wzb-form-star">*</span>Assignable Unit File(AU)
								<br/>
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_au</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:get_focus()</xsl:with-param>
									
								</xsl:call-template>
								<input type="hidden" name="aicc_au_filename"/>
							</td>
						</tr>
						<tr>
							<td width="20">
							</td>
							<td>
								Objective Relationship File(ORT)
								<br/>
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_ort</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:get_focus()</xsl:with-param>
									
								</xsl:call-template>
								<input type="hidden" name="aicc_ort_filename"/>
							</td>
						</tr>
						<!-- for import aicc zip files -->
						<tr>
							<td colspan="2">
								<label for="rdo_res_format_2">
									<input type="radio" id="rdo_res_format_2" name="res_format" value="AICC_FILES"/>
									<xsl:value-of select="$lab_upload_aicc_zip"/>
									<xsl:text>：</xsl:text>
								</label>
								<input type="hidden" name="mod_src_link" value=""/>
									
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">aicc_zip</xsl:with-param>
									<xsl:with-param name="onfocus">javascript:check_aicc()</xsl:with-param>
									
								</xsl:call-template>
								<!-- 这个是隐藏的<ifame>作为表单提交后处理的后台目标-->
								<iframe id="target_upload" name="target_upload" src="" style="display: none"/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<xsl:if test="$mode != 'INS'">
				<tr>
					<td align="right" widht="20%" class="wzb-form-control">
					</td>
					<td width="80%" class="wzb-form-control">
						<table cellpadding="0" cellspacing="0" border="0" class="Bg">
							<tr>
								<td width="20" align="right" valign="top">
									<input type="radio" name="res_format" checked="checked"/>
								</td>
								<td>
									<xsl:value-of select="$lab_keep_exist_res"/><xsl:text>：</xsl:text> 
									<br/>
									<a class="Text" href="{/resource/body/source}" target="_blank">
										<xsl:choose>
											<xsl:when test="string-length(/resource/body/source) &gt; 60">
												<xsl:value-of select="substring(/resource/body/source,0,60)"/>...
											</xsl:when>
											<xsl:otherwise><xsl:value-of select="/resource/body/source"/></xsl:otherwise>
										</xsl:choose>
									</a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td class="wzb-form-label">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td height="10" class="wzb-ui-module-text wzb-form-control">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ====================================== header =============================================== -->
	<xsl:template name="draw_header">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="dh_lang_ch"/>
			</xsl:when>
			<xsl:when test="$wb_lang ='gb'">
				<xsl:call-template name="dh_lang_gb"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="dh_lang_en"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="dh_lang_ch">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="res_search_result">搜索結果</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_gb">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="res_search_result">检索结果</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_en">
		<xsl:call-template name="draw_header_content">
				<xsl:with-param name="res_search_result">Search Result</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="draw_header_content">
		<xsl:param name="res_search_result"/>
	</xsl:template>
	<!-- ====================================== aicc add info =============================================== -->
	<xsl:template name="aicc_additional_information">
		<xsl:param name="mode"/>
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="res_additional_information_">
					<xsl:with-param name="mange_source">管理資源</xsl:with-param>
					<xsl:with-param name="res_add_info">其他資料</xsl:with-param>
					<xsl:with-param name="res_title_keyword">標題</xsl:with-param>
					<xsl:with-param name="res_lang">*語言</xsl:with-param>
					<xsl:with-param name="res_desc">簡介</xsl:with-param>
					<xsl:with-param name="res_lang_cn">繁體中文</xsl:with-param>
					<xsl:with-param name="res_lang_gb">簡體中文</xsl:with-param>
					<xsl:with-param name="res_lang_en">英文</xsl:with-param>
					<xsl:with-param name="res_diff">難度</xsl:with-param>
					<xsl:with-param name="res_diff_easy">容易</xsl:with-param>
					<xsl:with-param name="res_diff_normal">一般</xsl:with-param>
					<xsl:with-param name="res_diff_hard">困難</xsl:with-param>
					<xsl:with-param name="res_dur">*限時</xsl:with-param>
					<xsl:with-param name="res_status">狀態</xsl:with-param>
					<xsl:with-param name="res_status_on">在線</xsl:with-param>
					<xsl:with-param name="res_status_off">離線</xsl:with-param>
					<xsl:with-param name="res_personal">個人</xsl:with-param>
					<xsl:with-param name="res_public">共享</xsl:with-param>
					<xsl:with-param name="lab_please_select">-請選擇-</xsl:with-param>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="lab_g_form_btn_save">儲存</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
					<xsl:with-param name="header" select="$header"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="res_additional_information_">
				<xsl:with-param name="mange_source">管理资源</xsl:with-param>
					<xsl:with-param name="res_add_info">其他资料</xsl:with-param>
					<xsl:with-param name="res_title_keyword">标题</xsl:with-param>
					<xsl:with-param name="res_lang">*语言</xsl:with-param>
					<xsl:with-param name="res_desc">简介</xsl:with-param>
					<xsl:with-param name="res_lang_cn">繁体中文</xsl:with-param>
					<xsl:with-param name="res_lang_gb">简体中文</xsl:with-param>
					<xsl:with-param name="res_lang_en">英文</xsl:with-param>
					<xsl:with-param name="res_diff">难度</xsl:with-param>
					<xsl:with-param name="res_diff_easy">容易</xsl:with-param>
					<xsl:with-param name="res_diff_normal">一般</xsl:with-param>
					<xsl:with-param name="res_diff_hard">困难</xsl:with-param>
					<xsl:with-param name="res_dur">时限</xsl:with-param>
					<xsl:with-param name="res_status">状态</xsl:with-param>
					<xsl:with-param name="res_status_on">在线</xsl:with-param>
					<xsl:with-param name="res_status_off">离线</xsl:with-param>
					<xsl:with-param name="res_personal">个人</xsl:with-param>
					<xsl:with-param name="res_public">共享</xsl:with-param>
					<xsl:with-param name="lab_please_select">-请选择-</xsl:with-param>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="lab_g_form_btn_save">保存</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
					<xsl:with-param name="header" select="$header"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="res_additional_information_">
				<xsl:with-param name="mange_source">Management resources</xsl:with-param>
					<xsl:with-param name="res_add_info">Basic information</xsl:with-param>
					<xsl:with-param name="res_title_keyword">Title</xsl:with-param>
					<xsl:with-param name="res_lang">*Language</xsl:with-param>
					<xsl:with-param name="res_desc">Description</xsl:with-param>
					<xsl:with-param name="res_lang_cn">Traditional Chinese</xsl:with-param>
					<xsl:with-param name="res_lang_gb">Simplified Chinese</xsl:with-param>
					<xsl:with-param name="res_lang_en">English</xsl:with-param>
					<xsl:with-param name="res_diff">Difficulty</xsl:with-param>
					<xsl:with-param name="res_diff_easy">Easy</xsl:with-param>
					<xsl:with-param name="res_diff_normal">Normal</xsl:with-param>
					<xsl:with-param name="res_diff_hard">Hard</xsl:with-param>
					<xsl:with-param name="res_dur">*Duration</xsl:with-param>
					<xsl:with-param name="res_status">Status</xsl:with-param>
					<xsl:with-param name="res_status_on">Online</xsl:with-param>
					<xsl:with-param name="res_status_off">Offiline</xsl:with-param>
					<xsl:with-param name="res_personal">Personal</xsl:with-param>
					<xsl:with-param name="res_public">Public</xsl:with-param>
					<xsl:with-param name="lab_please_select">-Please select-</xsl:with-param>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="save_function" select="$save_function"/>
					<xsl:with-param name="cancel_function" select="$cancel_function"/>
					<xsl:with-param name="lab_g_form_btn_save">Save</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
					<xsl:with-param name="header" select="$header"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ========================================================================== -->
	<xsl:template name="res_additional_information_">
	
	<xsl:param name="mange_source"/>
		<xsl:param name="res_add_info"/>
		<xsl:param name="res_title_keyword"/>
		<xsl:param name="res_lang"/>
		<xsl:param name="res_desc"/>
		<xsl:param name="res_lang_cn"/>
		<xsl:param name="res_lang_gb"/>
		<xsl:param name="res_lang_en"/>
		<xsl:param name="res_diff"/>
		<xsl:param name="res_diff_easy"/>
		<xsl:param name="res_diff_normal"/>
		<xsl:param name="res_diff_hard"/>
		<xsl:param name="res_dur"/>
		<xsl:param name="res_status"/>
		<xsl:param name="res_status_on"/>
		<xsl:param name="res_status_off"/>
		<xsl:param name="res_personal"/>
		<xsl:param name="res_public"/>
		<xsl:param name="lab_please_select"/>
		<xsl:param name="mode"/>
		<xsl:param name="width"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="lab_g_form_btn_save"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="header"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		
		<xsl:choose>
			<xsl:when test="$header ='YES' and $mode ='UPD'">	<!-- 添加AICC -->
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
								<xsl:when test="title">
									<xsl:value-of select="title"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="/resource/body/title"/>
								</xsl:otherwise>
							</xsl:choose>
						</span>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<span class="NavLink">
							<xsl:for-each select="//user/objective/path/node">
								<a href="javascript:obj.manage_obj_lst('','{@id}','','','')" class="NavLink">
									<xsl:value-of select="."/>
								</a>
								<xsl:text>&#160;&gt;&#160;</xsl:text>
							</xsl:for-each>
							<a href="javascript:obj.manage_obj_lst('','{//user/objective/@id}','','','')" class="NavLink">
								<xsl:value-of select="//user/objective/desc"/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							
							<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="//user/objective/@id"/>','','','false')</xsl:with-param>
										<xsl:with-param name="class">NavLink</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$lab_operate_add" /> AICC
						</span>
					</xsl:with-param>
				</xsl:call-template> 
			</xsl:otherwise>
		</xsl:choose>

		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$res_add_info"/>
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table>
			<tr>
				<td class="wzb-form-label" style="padding: 26px 0 0;">
					<span class="wzb-form-star">*</span><xsl:value-of select="$res_title_keyword"/>：
				</td>
				<td class="wzb-form-control" style="padding: 27px 0 2px 10px;">
					<xsl:variable name="escaped_title">
						<xsl:call-template name="escape_doub_quo">
							<xsl:with-param name="my_right_value">
								<xsl:value-of select="/resource/body/title"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:variable>
					<input class="wzb-inputText" type="text" name="res_title" style="width: 300px;"/>
					<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					document.frmXml.res_title.value="]]><xsl:value-of select="$escaped_title"/><![CDATA[";]]></SCRIPT>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$res_desc"/>：
				</td>
				<td class="wzb-form-control">
					<textarea name="res_desc" rows="2" style="width: 300px; height:100px;" class="wzb-inputTextArea">
						<xsl:value-of select="/resource/body/desc"/>
					</textarea>
				</td>
			</tr>
			<input type="hidden" name="res_lan"/>
			<input type="hidden" name="res_privilege" value="CW"/>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_save"/>
				<xsl:with-param name="wb_gen_btn_href" select="$save_function"/>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
				<xsl:with-param name="id">submitButton</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href" select="$cancel_function"/>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
				<xsl:with-param name="id">cancelButton</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>

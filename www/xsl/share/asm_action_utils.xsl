<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>
	<xsl:import href="../utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="../utils/escape_doub_quo.xsl"/>
	<xsl:import href="../utils/escape_backslash.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>
	<xsl:import href="../utils/wb_gen_form_button.xsl"/>
	<xsl:import href="../share/res_label_share.xsl"/>
	<xsl:variable name="res_id" select="$root_tag/@id"/>
	<xsl:variable name="obj_id" select="$root_tag/objective/@id"/>
	<xsl:variable name="timestamp" select="$root_tag/@timestamp"/>
	<xsl:variable name="subtype" select="$root_tag/header/@subtype"/>
	<xsl:variable name="res_duration" select="$root_tag/header/@duration"/>
	<xsl:variable name="allow_shuffle_ind" select="$root_tag/header/@allow_shuffle_ind"/>
	
	<xsl:variable name="mange_source">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">管理資源</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">管理资源</xsl:when>
			<xsl:otherwise>Management resources</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<!-- ================================================================================================== -->
	<xsl:template name="asm_html_header">
		<xsl:param name="mode"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script src="{$wb_js_path}wb_assessment.js" language="JavaScript"/>
			<script src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[	
			
				asm =new wbAssessment;

				obj = new wbObjective;
				
				function init() {
				}
				function get_focus() {
					if(document.frmXml.res_format != null)
						document.frmXml.res_format[0].checked=true
					return	
				}
				function read_asm() {
					asm.read(']]><xsl:value-of select="/resource/@id"/><![CDATA[')
				}
				
				]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================================================== -->
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
			<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
			<xsl:with-param name="res_search_result">搜索結果</xsl:with-param>
			<xsl:with-param name="lab_adm_home">管理員平台</xsl:with-param>
			<xsl:with-param name="lab_syb_manager">資源結構圖</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_gb">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
			<xsl:with-param name="res_search_result">检索结果</xsl:with-param>
			<xsl:with-param name="lab_adm_home">管理员平台</xsl:with-param>
			<xsl:with-param name="lab_syb_manager">资源结构图</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_en">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">Knowledge manager</xsl:with-param>
			<xsl:with-param name="res_search_result">Search result</xsl:with-param>
			<xsl:with-param name="lab_adm_home">Administrator home</xsl:with-param>
			<xsl:with-param name="lab_syb_manager">Knowledge manager</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================================================== -->
	<xsl:template name="draw_header_content">
		<xsl:param name="lab_res_manager"/>
		<xsl:param name="res_search_result"/>

		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				静态资源
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ====================================== header =============================================== -->
	<!-- ====================================== aicc add info =============================================== -->
	<xsl:template name="asm_additional_information">
		<xsl:param name="mode"/>
		<xsl:param name="width"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="type"/>
		<xsl:param name="page_title"/>
		<input type="hidden" name="module" value="quebank.QueBankModule"/>
		<input type="hidden" name="res_type" value="ASM"/>
		<input type="hidden" name="res_difficulty"/>
		<input type="hidden" name="url_success" value="javascript:window.location.href=gen_get_cookie('url_success')"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="qct_select_logic"/>
		<xsl:choose>
			<xsl:when test="$mode='INS'">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="res_subtype"/>
				<input type="hidden" name="res_privilege" value="CW"/>
				<input type="hidden" name="res_lan" value="ISO-8859-1"/>
				<script language="JavaScript"><![CDATA[
						sub_type = getUrlParam('sub_type');
						document.frmXml.res_subtype.value = sub_type;
				  		if ( sub_type == 'FAS') {
							document.frmXml.cmd.value = "ins_fixed_assessment";
						}
						else {
							document.frmXml.cmd.value = "ins_dynamic_assessment";
							document.frmXml.qct_select_logic.value = "RND";
						}
				]]></script>
				<input type="hidden" name="obj_id" value="{$obj_id}"/>
				<input type="hidden" name="qct_allow_shuffle_ind" value="0"/>
			</xsl:when>
			<xsl:when test="$mode='UPD'">
				<xsl:choose>
					<xsl:when test="$subtype = 'FAS'">
						<input type="hidden" name="cmd" value="upd_fixed_assessment"/>
					</xsl:when>
					<xsl:otherwise>
						<input type="hidden" name="cmd" value="upd_dynamic_assessment"/>
						<script language="JavaScript"><![CDATA[
							document.frmXml.qct_select_logic.value = "RND";
						]]></script>
					</xsl:otherwise>
				</xsl:choose>
				<input type="hidden" name="res_id" value="{$res_id}"/>
				<input type="hidden" name="obj_id" value="{$obj_id}"/>
				<input type="hidden" name="res_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="res_subtype" value="{$subtype}"/>
				<input type="hidden" name="res_privilege" value="CW"/>
				<input type="hidden" name="res_duration" value="{$res_duration}"/>
				<input type="hidden" name="qct_allow_shuffle_ind" value="{$allow_shuffle_ind}"/>
			</xsl:when>
			<xsl:when test="$mode='PST'">
				<input type="hidden" name="cmd" value="ins_res"/>
				<input type="hidden" name="obj_id"/>
				<input type="hidden" name="res_id" value="{$res_id}"/>
				<input type="hidden" name="res_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="res_subtype" value="{$subtype}"/>
				<input type="hidden" name="res_duration" value="{$res_duration}"/>
				<input type="hidden" name="copy_media_from" value="{$res_id}"/>
				<input type="hidden" name="qct_allow_shuffle_ind" value="{$allow_shuffle_ind}"/>
			</xsl:when>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="res_additional_information_">
					<xsl:with-param name="lab_title_keyword">標題</xsl:with-param>
					<xsl:with-param name="lab_desc">簡介</xsl:with-param>
					<xsl:with-param name="lab_status">狀態</xsl:with-param>
					<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
					<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
					<xsl:with-param name="lab_select_logic">選擇邏輯</xsl:with-param>
					<xsl:with-param name="lab_random_logic">隨機性</xsl:with-param>
					<xsl:with-param name="lab_adaptive_logic">適應性</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
					<xsl:with-param name="mode">
						<xsl:value-of select="$mode"/>
					</xsl:with-param>
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="save_function">
						<xsl:value-of select="$save_function"/>
					</xsl:with-param>
					<xsl:with-param name="cancel_function">
						<xsl:value-of select="$cancel_function"/>
					</xsl:with-param>
					<xsl:with-param name="type">
						<xsl:value-of select="$type"/>
					</xsl:with-param>
					<xsl:with-param name="page_title" select="$page_title"></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="res_additional_information_">
					<xsl:with-param name="lab_title_keyword">标题</xsl:with-param>
					<xsl:with-param name="lab_desc">简介</xsl:with-param>
					<xsl:with-param name="lab_status">状态</xsl:with-param>
					<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
					<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
					<xsl:with-param name="lab_select_logic">*选择逻辑</xsl:with-param>
					<xsl:with-param name="lab_random_logic">随机性</xsl:with-param>
					<xsl:with-param name="lab_adaptive_logic">适应性</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">保存</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
					<xsl:with-param name="mode">
						<xsl:value-of select="$mode"/>
					</xsl:with-param>
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="save_function">
						<xsl:value-of select="$save_function"/>
					</xsl:with-param>
					<xsl:with-param name="cancel_function">
						<xsl:value-of select="$cancel_function"/>
					</xsl:with-param>
					<xsl:with-param name="type">
						<xsl:value-of select="$type"/>
					</xsl:with-param>
					<xsl:with-param name="page_title" select="$page_title"></xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="res_additional_information_">
					<xsl:with-param name="lab_title_keyword">Title</xsl:with-param>
					<xsl:with-param name="lab_desc">Description</xsl:with-param>
					<xsl:with-param name="lab_status">Status</xsl:with-param>
					<xsl:with-param name="lab_status_on">Published</xsl:with-param>
					<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
					<xsl:with-param name="lab_select_logic">*Question selection logic</xsl:with-param>
					<xsl:with-param name="lab_adaptive_logic">Adaptive</xsl:with-param>
					<xsl:with-param name="lab_random_logic">Random</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
					<xsl:with-param name="mode">
						<xsl:value-of select="$mode"/>
					</xsl:with-param>
					<xsl:with-param name="width">
						<xsl:value-of select="$width"/>
					</xsl:with-param>
					<xsl:with-param name="save_function">
						<xsl:value-of select="$save_function"/>
					</xsl:with-param>
					<xsl:with-param name="cancel_function">
						<xsl:value-of select="$cancel_function"/>
					</xsl:with-param>
					<xsl:with-param name="type">
						<xsl:value-of select="$type"/>
					</xsl:with-param>
					<xsl:with-param name="page_title" select="$page_title"></xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="res_additional_information_">
		<xsl:param name="lab_title_keyword"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_select_logic"/>
		<xsl:param name="lab_adaptive_logic"/>
		<xsl:param name="lab_random_logic"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="page_title"></xsl:param>
		<xsl:param name="mode"/>
		<xsl:param name="width"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="lab_operate_add"></xsl:param>
		<xsl:param name="type"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="page_title">
				<xsl:value-of select="$page_title"></xsl:value-of>
			</xsl:with-param>
		</xsl:call-template>
		
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
					<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:for-each select="//user/objective/path/node">
						<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
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
					</xsl:when>
					<xsl:otherwise>
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
					    
					</xsl:otherwise>
				</xsl:choose>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$page_title"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_title_keyword"/> :
				</td>
				<td class="wzb-form-control">
					<input type="text" name="res_title" style="width:400px;" class="wzb-inputText" maxlength="255">
						<xsl:attribute name="value"><xsl:value-of disable-output-escaping="yes" select="$root_tag/body/title"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_desc"/>
					<xsl:text> :&#160;</xsl:text>
				</td>
				<td class="wzb-form-control">
					<textarea rows="6" cols="50" style="width:400px;" name="res_desc" class="wzb-inputTextArea">
						<xsl:value-of disable-output-escaping="yes" select="$root_tag/body/desc"/>
					</textarea>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_status"/> :
				</td>
				<td class="wzb-form-control">
					<select class="wzb-select">
						<xsl:attribute name="name">res_status</xsl:attribute>
						<option>
							<xsl:attribute name="value">ON</xsl:attribute>
							<xsl:if test="$root_tag/header/@status='ON'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_status_on"/>
						</option>
						<option>
							<xsl:attribute name="value">OFF</xsl:attribute>
							<xsl:if test="$root_tag/header/@status='OFF'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_status_off"/>
						</option>
					</select>
				</td>
			</tr>
			<tr>
				<td width="20%" height="10">
				</td>
				<td width="80%" height="10" class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_ok"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:value-of select="$save_function"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:value-of select="$cancel_function"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>

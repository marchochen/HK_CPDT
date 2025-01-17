<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ========================================================== -->
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- ========================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="user/course"/>
		</html>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template match="course">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			
			title = getUrlParam('title')
			
			function show_course() {
				course_lst = new wbCourse; 
				cos_id = wb_utils_get_cookie('course_id');
				url = course_lst.view_info_url(cos_id);
				window.location = url;
			}
			
			function canelAdd() {
				show_course();
				window.parent.cancelAdd();
			}
			
			function addFolder() {
				if (ValidateFolderTitle(document.frmXml.title,']]><xsl:value-of select="$wb_lang"/><![CDATA[')){
				window.parent.editNode(document.frmXml.title.value);
				wb_utils_set_cookie('folder_title',document.frmXml.title.value);
				folder_read_mode();}
			}
			
			function folder_read_mode() {
				url = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','ca_view_folder.xsl')
				window.location = url;
			}
			function status(){
					addFolder()
					return false;
			}
			function ValidateFolderTitle(fld, lang){
				fld.value = wbUtilsTrimString(fld.value);
				if (!gen_validate_empty_field(fld, eval('wb_msg_' + lang + '_title'), lang)) {
					fld.focus()
					return false;
				}
				if (fld.value.length > 50) {
					alert(eval('wb_msg_' + lang + '_title_not_longer'));
					fld.focus();
					return false;
				}
				return true;
			}
			]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<BODY marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<FORM name="frmXml" onSubmit="return status()">
				<xsl:call-template name="wb_init_lab"/>
			</FORM>
		</BODY>
	</xsl:template>
	<!-- ============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_folder">模塊夾</xsl:with-param>
			<xsl:with-param name="lab_new_folder">新增模塊夾</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_folder">模块夹</xsl:with-param>
			<xsl:with-param name="lab_new_folder">添加模块夹</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_folder">Folder</xsl:with-param>
			<xsl:with-param name="lab_new_folder">New folder</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_folder"/>
		<xsl:param name="lab_new_folder"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_new_folder"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_title"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input type="text" name="title" value="" class="wzb-inputText" style="width:300px;"  maxlength="120"/>
					<script><![CDATA[							
						document.frmXml.title.value = title;
						document.frmXml.title.focus();
					]]></script>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_type"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_folder"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:addFolder();</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:canelAdd();</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
	<xsl:template match="cur_time"/>
</xsl:stylesheet>

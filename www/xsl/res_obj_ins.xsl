<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- ============================================================= -->
	<xsl:variable name="obj_id" select="/objective_list/objective/@id"/>
   <xsl:variable name="curfolder" select="/objective_list/folders/text()" />
	<xsl:variable name="obj_parent_id">
		<xsl:choose>
			<xsl:when test="count(/objective_list/header/node) = 0">0</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/objective_list/header/node[position() = last()]/@id"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="syl_id" select="/objective_list/objective/syllabus/@id"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_add_sys_obj">新增資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_description">文件夾名稱</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_select">--請選擇--</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder">共享此資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder_desc">（其他管理員可以查看並使用該文件夾下的資源）</xsl:with-param>
			<xsl:with-param name="lab_required">为必填</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_add_sys_obj">添加资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_description">文件夹名称</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_select">--请选择--</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder">共享此文件夹</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder_desc">（所有管理员可以查看并使用该文件夹下的资源）</xsl:with-param>
			<xsl:with-param name="lab_required">为必填</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_add_sys_obj">Add resource folder</xsl:with-param>
			<xsl:with-param name="lab_description">Folder name</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_select">--Please select--</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder">Share this folder with others</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder_desc">(All training administrators can view and use resources under this folder)</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="content">
		<xsl:param name="lab_add_sys_obj"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_share_this_folder"/>
		<xsl:param name="lab_share_this_folder_desc"/>
		<xsl:param name="lab_required"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			obj = new wbObjective
		
			function status(){
				return false
			}
		]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml" onSubmit="return status()">
				<input type="hidden" name="cmd" value="ins_obj"/>
				<input type="hidden" name="obj_id_parent" value="{$obj_id}"/>
				<input type="hidden" name="syl_id" value="{$syl_id}"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_add_sys_obj"/>
				</xsl:call-template>
				
				<table>
					<tr>
						<td width="40%" align="right" class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_description"/>：&#160;
						</td>
						<td width="60%" align="left" class="wzb-form-control">
							<input type="text" name="obj_desc" size="27" maxlength="255" style="width:350px;" class="wzb-inputText"/>
						</td>
					</tr>
					<!--  
					<tr>
						<td width="20%" align="right">
							
						</td>
						<td width="80%">
							<input type="checkbox" name="obj_share_ind" value="true" class=" vtop wzb-inputText"/>
							<xsl:value-of select="$lab_share_this_folder"/>
							<xsl:value-of select="$lab_share_this_folder_desc"/>
						</td>
					</tr>-->
					<tr>
						<td width="20%" align="right"></td>
						<td width="65%" align="left" class="wzb-form-control">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/>
						</td>
					</tr>
					<input type="hidden" name="obj_type" value="SYB"/>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:obj.ins_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$curfolder"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="cur_tcr_id" select="/objective/access_control/training_center/@id"/>
	<xsl:variable name="cur_tcr_title" select="/objective/access_control/training_center/title"/>
	<xsl:variable name="obj_id_parent" select="/objective/@obj_id_parent"/>
	<xsl:variable name="isTcIndependent" select="/objective/isTcIndependent"/>
	
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript">
			<![CDATA[
			var obj = new wbObjective;	
			var goldenman = new wbGoldenMan;
			function removeTcrDependant() {
				document.frmXml.tcr_id_lst_single.value = '';
				document.frmXml.tcr_id_lst.options[0].text = '';
				document.frmXml.tcr_id_lst.options[0].value = '';
			}					
			]]>
			</script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml)">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">文件夾名稱</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder">共享此資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder_desc">（所有管理員可以查看並管理該文件夾下的資源）</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_required">為必填</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">文件夹名称</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder">共享此文件夹</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder_desc">（所有管理员可以查看并管理该文件夹下的资源）</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
			<xsl:with-param name="lab_required">为必填</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">Folder name</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder">Share this folder with others</xsl:with-param>
			<xsl:with-param name="lab_share_this_folder_desc">(All training administrators can view and manager resources under this folder)</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
	<xsl:param name="lab_inst"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_share_this_folder"/>
	<xsl:param name="lab_share_this_folder_desc"/>
	<xsl:param name="lab_tc"/>
	<xsl:param name="lab_required"/>
		<form name="frmXml" onSubmit="obj.upd_exec(document.frmXml,'{$wb_lang}')">
			<input type="hidden" name="cmd" value="upd_obj"/>
			<input type="hidden" name="obj_id" value="{objective/@id}"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="obj_tcr_id" value=""/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<span class="NavLink">
						<xsl:for-each select="/objective/path/node">
							<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
								<xsl:value-of select="."/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						</xsl:for-each>
						<xsl:value-of select="/objective/desc"/>
					</span>
				</xsl:with-param>
		  </xsl:call-template>
			
			<table>
				<tr>
					<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_inst"/>：
					</td>
					<td class="wzb-form-control">
						<input type="text" name="obj_desc" size="30" style="width:350px;" value="{objective/desc}" class="wzb-inputText"/>
					</td>
				</tr>
				<tr>
					<td width="20%" align="right">
						
					</td>
				</tr>
				
				<xsl:if test="$obj_id_parent = 0">
				
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tc"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:call-template name="wb_goldenman">
								<xsl:with-param name="field_name">tcr_id_lst</xsl:with-param>
								<xsl:with-param name="name">tcr_id_lst</xsl:with-param>
								<xsl:with-param name="box_size">1</xsl:with-param>
								<xsl:with-param name="tree_type">training_center</xsl:with-param>
								<xsl:with-param name="select_type">2</xsl:with-param>
								<xsl:with-param name="pick_leave">0</xsl:with-param>
								<xsl:with-param name="pick_root">0</xsl:with-param>
								<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>										
								<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
								<xsl:with-param name="remove_function">removeTcrDependant()</xsl:with-param>
								<xsl:with-param name="single_option_value"><xsl:value-of select="$cur_tcr_id"/></xsl:with-param>
								<xsl:with-param name="single_option_text"><xsl:value-of select="$cur_tcr_title"/></xsl:with-param>
							</xsl:call-template>
						</td>
						<input name="mtc_tcr_id" type="hidden" value=""/>
					</tr>
				
				</xsl:if>	
				
				<tr>
					<td width="20%" align="right">
						
					</td>
					<td width="80%">
					<!--     屏蔽文件夹的共享功能
						<xsl:if test="$isTcIndependent = 'false'">
							<input type="checkbox" name="obj_share_ind" value="true">
								<xsl:if test="objective/shared/text() = 'true'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input>
							<xsl:text>&#160;</xsl:text>
							<span class="Text">
								<xsl:value-of select="$lab_share_this_folder"/>
								<xsl:value-of select="$lab_share_this_folder_desc"/>
							</span>
						</xsl:if>   
						  -->
					</td>
				</tr>
				<tr>
					<td width="20%" align="right"></td>
					<td width="65%" align="left" class="wzb-form-control">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/>
					</td>
				</tr>
			</table>
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:obj.upd_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
				</xsl:call-template>
			</div>
		</form>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>

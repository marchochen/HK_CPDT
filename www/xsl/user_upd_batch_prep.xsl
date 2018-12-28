<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:variable name="cur_role" select="/user_manager/meta/cur_usr/role/@id"/>
	<xsl:variable name="usg_id" select="/user_manager/group/@id"/>
	<xsl:variable name="usg_title" select="/user_manager/group/@title"/>
	<xsl:variable name="ugr_id" select="/user_manager/default_grade/attribute_list/entity/@id"/>
	<xsl:variable name="ugr_title" select="/user_manager/default_grade/attribute_list/entity/@display_bil"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<xsl:apply-templates select="user_manager"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user_manager">
		<html> 
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>  
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>

			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[	
			goldenman = new wbGoldenMan;
			usr = new wbUserGroup;
			window.onload = function(){
				var usg_title =]]>'<xsl:value-of select="$usg_title"/>'<![CDATA[;
				var usg_id =]]>'<xsl:value-of select="$usg_id"/>'<![CDATA[;
				
				document.frmXml.usr_group_lst_single.value=usg_title.replace(/(^\s*)|(\s*$)/g, "");
				document.frmXml.usr_group_lst.options[0].value=trim(usg_id);

				var ugr_title =]]>'<xsl:value-of select="$ugr_title"/>'<![CDATA[;
				var ugr_id =]]>'<xsl:value-of select="$ugr_id"/>'<![CDATA[;
				document.frmXml.usr_grade_lst_single.value=ugr_title.replace(/(^\s*)|(\s*$)/g, "");
				document.frmXml.usr_grade_lst.options[0].value=trim(ugr_id);
				
			}
			

			function   trim(str) 
			{
			return str.replace(/\ /g,"");
			} 
			
		        ]]></SCRIPT>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<FORM name="frmXml" method="post" enctype="multipart/form-data">
				<input type="hidden" name="rename" value="no"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="url_failure"/>			
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="cur_group_id" value="{$usg_id}"/>
				 <xsl:if test="$cur_role ='ADM_1'">
				<input type="hidden" name="usr_extra_datetime_11 " id="usr_extra_datetime_11"  value=""/>
				
				 </xsl:if>
				<xsl:call-template name="wb_init_lab"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
					<xsl:with-param name="page_title">
						<xsl:call-template name="get_lab">
							<xsl:with-param name="lab_title">label_core_user_management_9</xsl:with-param>
						</xsl:call-template>
					</xsl:with-param>
				</xsl:call-template>	
			</FORM>
		</BODY>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_account_validity">帳號有效期：</xsl:with-param>
			<xsl:with-param name="lab_user_group">用戶組：</xsl:with-param>
			<xsl:with-param name="lab_user_grade">職務：</xsl:with-param>
			<xsl:with-param name="lab_title_desc">編輯信息</xsl:with-param>
			<xsl:with-param name="lab_bottom_desc">為必填</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
		        <xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_account_validity">账号有效期：</xsl:with-param>
			<xsl:with-param name="lab_user_group">用户组：</xsl:with-param>
			<xsl:with-param name="lab_user_grade">职务：</xsl:with-param>
			<xsl:with-param name="lab_title_desc">编辑信息</xsl:with-param>
			<xsl:with-param name="lab_bottom_desc">为必填</xsl:with-param>
			
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_account_validity">Account validity:</xsl:with-param>			
			<xsl:with-param name="lab_user_group">User group:</xsl:with-param>
			<xsl:with-param name="lab_user_grade">User grade:</xsl:with-param>
			<xsl:with-param name="lab_title_desc">Editing  information:</xsl:with-param>
			<xsl:with-param name="lab_bottom_desc">Required </xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/user_manager/user">
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_account_validity"/>
		<xsl:param name="lab_user_group"/>
		<xsl:param name="lab_user_grade"/>
		<xsl:param name="lab_title_desc"/>
		<xsl:param name="lab_bottom_desc"/>
		<xsl:call-template name="wb_ui_hdr"/>		
		<!--
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
			<xsl:value-of select="$lab_title_desc"/>
			</xsl:with-param>
		</xsl:call-template>
		  -->
		<table class="Bg" cellspacing="0" cellpadding="3" border="0" width="982" >
			<tr height="20">
				<td width="20%" align="right" >
					
				</td>
				<td align="left">
				
				</td>
			</tr>
		       <xsl:if test="$cur_role ='ADM_1'">
			<tr height="20">
				<td width="20%" align="right" >
					<xsl:value-of select="$lab_account_validity"/>
				</td>
				<td align="left">
				
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">user_effective_datetime</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">user_effective_datetime</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
						<xsl:with-param name="format">2</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			</xsl:if>
			<tr>
				<td  width="20%" align="right" style="vertical-align:top" >
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_user_group"/>						
				</td>	
				<td >
				<xsl:call-template name="wb_goldenman">
					<xsl:with-param name="attribute_type">GRADE</xsl:with-param>
					<xsl:with-param name="title" >USG</xsl:with-param>
					<xsl:with-param name="field_name">usr_group_lst</xsl:with-param>
					<xsl:with-param name="tree_type">user_group</xsl:with-param>
					<xsl:with-param name="select_type">2</xsl:with-param>
					<xsl:with-param name="box_size">1</xsl:with-param>
					<xsl:with-param name="pick_root">0</xsl:with-param>
					<xsl:with-param name="hidden_field">usr_group_lst_type</xsl:with-param>
					<xsl:with-param name="hidden_field_value">USR_PARENT_USG</xsl:with-param>
					<xsl:with-param name="required_field">true</xsl:with-param>
					<xsl:with-param name="override_appr_usg">1</xsl:with-param>
					<xsl:with-param name="show_not_syn_option">false</xsl:with-param>
					
				</xsl:call-template>
				</td>
			</tr>
			 
			<tr style="display:none;">
				<td  width="20%" align="right" style="vertical-align:top" >
					<xsl:value-of select="$lab_user_grade"/>					
				</td>	
				<td >	
				<xsl:call-template name="wb_goldenman">
					<xsl:with-param name="attribute_type">UGR</xsl:with-param>
					<xsl:with-param name="title" >UGR</xsl:with-param>
					<xsl:with-param name="field_name">usr_grade_lst</xsl:with-param>
					<xsl:with-param name="tree_type">grade</xsl:with-param>
					<xsl:with-param name="select_type">5</xsl:with-param>
					<xsl:with-param name="box_size">1</xsl:with-param>
					<xsl:with-param name="hidden_field">usr_grade_lst_type</xsl:with-param>
					<xsl:with-param name="hidden_field_value">USR_CURRENT_UGR</xsl:with-param>
					<xsl:with-param name="required_field">true</xsl:with-param>
					<xsl:with-param name="override_appr_usg">1</xsl:with-param>
					<xsl:with-param name="show_not_syn_option">false</xsl:with-param>
				</xsl:call-template>
				</td>
			</tr>
			
			<tr height="10">
				<td width="187" align="right" >
					
				</td>
				<td width="330" align="left" style="line-height: 1;padding:0px;">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_bottom_desc"/>
				</td>
			</tr>
		
		</table>
		<div class="wzb-bar">
		<table cellpadding="3" cellspacing="0" border="0" width="982">
			<tr>
				<td align="center" valign="middle">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">usr.user.upd_batch_exec(document.frmXml, '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<img border="0" width="1" src="{$wb_img_path}tp.gif"/>
					<xsl:variable name="back_ftn">
						
							ITM_MAIN
						
					</xsl:variable>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:usr.group.manage_grp( wb_utils_get_cookie("cur_user_group_id"))</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>	
	<!-- =============================================================== -->
	<xsl:template match="meta"/>	
	
</xsl:stylesheet>
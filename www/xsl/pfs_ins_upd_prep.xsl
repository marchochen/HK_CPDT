<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="utils/wb_ui_title.xsl"/>
<xsl:import href="utils/wb_ui_line.xsl"/>
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/wb_goldenman.xsl"/>
<xsl:import href="utils/wb_gen_form_button.xsl"/>
<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>
<xsl:import href="share/usr_detail_label_share.xsl"/>
<!-- =============================================================== -->
<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
<xsl:variable name="tc_enabled" select="/tree/meta/tc_enabled"/>
<xsl:variable name="tc_independent" select="/tree/meta/tc_independent"/>
<!-- =============================================================== -->
<xsl:template match="/">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="lang_ch">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">添加职业发展序列</xsl:with-param>
		<xsl:with-param name="lab_nav_title_upd">修改职业发展序列</xsl:with-param>
		<xsl:with-param name="lab_title">標題</xsl:with-param>
		<xsl:with-param name="lab_required">為必填</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_name_desc">(不可超過50個位數及不可有斜線)</xsl:with-param>
		<xsl:with-param name="lab_btn_del">删除</xsl:with-param>
		<xsl:with-param name="lab_ugr_title">职位名称</xsl:with-param>
		<xsl:with-param name="lab_option_0">请选择</xsl:with-param>
		<xsl:with-param name="lab_itm_lst">必修课程列表</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_gb">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">添加职业发展序列</xsl:with-param>
		<xsl:with-param name="lab_nav_title_upd">修改职业发展序列</xsl:with-param>
		<xsl:with-param name="lab_title">标题</xsl:with-param>
		<xsl:with-param name="lab_required">为必填</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_name_desc">(长度不能超过50，且不能包含斜线)</xsl:with-param>
		<xsl:with-param name="lab_btn_del">删除</xsl:with-param>
		<xsl:with-param name="lab_ugr_title">职位名称</xsl:with-param>
		<xsl:with-param name="lab_option_0">请选择</xsl:with-param>
		<xsl:with-param name="lab_itm_lst">必修课程列表</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_en">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_nav_title">Add Profession</xsl:with-param>
		<xsl:with-param name="lab_nav_title_upd">Update Profession</xsl:with-param>
		<xsl:with-param name="lab_title">Title</xsl:with-param>
		<xsl:with-param name="lab_required">Required</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		<xsl:with-param name="lab_name_desc">(Not more than 50 characters and must not contain slash)</xsl:with-param>
		<xsl:with-param name="lab_btn_del">Delete</xsl:with-param>
		<xsl:with-param name="lab_ugr_title">Grade</xsl:with-param>
		<xsl:with-param name="lab_option_0">Please select</xsl:with-param>
		<xsl:with-param name="lab_itm_lst">Compulsory course</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="content">	
	<xsl:param name="lab_nav_title"/>
	<xsl:param name="lab_nav_title_upd"/>
	<xsl:param name="lab_title"/>
	<xsl:param name="lab_required"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_name_desc"/>
	<xsl:param name="lab_btn_del"/>
	<xsl:param name="lab_ugr_title"/>
	<xsl:param name="lab_option_0"/>
	<xsl:param name="lab_itm_lst"/>
	<head>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
		<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
		<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
		<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}wb_goldenman.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script language="JavaScript" src="{$wb_js_path}wb_pfs.js"/>
		<script language="JavaScript"><![CDATA[
			pfs = new wbpfs
			var goldenman = new wbGoldenMan
			function cancel(){
				window.location = pfs.pfs_home_url();
			}
			var psilength = ]]> <xsl:value-of select="count(/profession/psi_list/psi)"/> <![CDATA[;
		]]></script>
		<!-- CSS -->
		<xsl:call-template name="new_css"/>
	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="">
	<form name="frmXml" onsubmit="return false">
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="module"/>
		<input type="hidden" name="pfs_id" value="{/profession/pfs_id}"/>
		
		<table style="margin-top:2px;">
			<tr>
				<td>
					<xsl:choose>
						<xsl:when test="/profession/pfs_id &gt; 0">
							<xsl:value-of select="$lab_nav_title_upd"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_nav_title"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<xsl:if test="/profession/pfs_id &gt; 0">
					<td style="text-align:right">
						<xsl:call-template name="wb_gen_button">	
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_del"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:pfs.pfs_del(<xsl:value-of select="/profession/pfs_id"/>)</xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:if>
			</tr>
			<tr>
				<td colspan="3" ><xsl:call-template name="wb_ui_line"/></td>
			</tr>
		</table>
		
		<table>
			<tr>
				<td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><xsl:value-of select="$lab_title"/>:</td>
				<td class="wzb-form-control">
					<input type="text" style="width:200px;" class="wzb-inputText" name="pfs_title" maxlength="50" value="{/profession/pfs/pfs_title}"/>
					<br/>
					<xsl:value-of select="$lab_name_desc"/>
				</td>
			</tr>
			<xsl:for-each select="/profession/psi_list/psi">
				<xsl:variable name="index" select="position()"/>
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_ugr_title"/>:
					</td>
					<td class="wzb-form-control">
						<select class="wzb-select" name="psi_ugr_id_{$index}">
							<option value="0"><xsl:value-of select="$lab_option_0"/></option>
							<xsl:for-each select="/profession/ugr_list/ugr">
								<option value="{ugr_ent_id}">
									<xsl:if test="/profession/psi_list/psi[$index]/psi_ugr_id = ugr_ent_id">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="ugr_display_bil"/>
								</option>
							</xsl:for-each>
						</select>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_itm_lst"/>:
					</td>
					<td class="wzb-form-control">
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="field_name">itm_id_lst_<xsl:value-of select="$index"/></xsl:with-param>
							<xsl:with-param name="name">itm_id_lst_<xsl:value-of select="$index"/></xsl:with-param>
							<xsl:with-param name="box_size">4</xsl:with-param>
							<xsl:with-param name="tree_type">item</xsl:with-param>
							<xsl:with-param name="select_type">3</xsl:with-param>
							<xsl:with-param name="args_type">row</xsl:with-param>
							<xsl:with-param name="complusory_tree">0</xsl:with-param>
							<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							<xsl:with-param name="pick_root">0</xsl:with-param>
							<!-- <xsl:with-param name="itm_id"><xsl:value-of select="$itm_id"/></xsl:with-param> -->
							<xsl:with-param name="add_function">
							goldenman.opentree('item',3,'itm_id_lst_<xsl:value-of select="$index"/>','SELFSTUDY','1','','','1','0', '0', '', '0','0', '0', '', '', '0', '1', '', '','','','')
							</xsl:with-param>
							<xsl:with-param name="option_list">
								<xsl:for-each select="itm_lst/itm">
									<option value="{itm_id}">
										<xsl:value-of select="itm_title"/>
									</option>
								</xsl:for-each>
							</xsl:with-param>
							<xsl:with-param name="label_add_btn">
								<xsl:value-of select="$lab_gen_select"/>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
			<input type="hidden" name="psi_itm_id_lst" value=""/>
			<input type="hidden" name="psi_ugr_id_lst" value=""/>
			<tr>
				<td class="wzb-form-label"></td>
				<td class="wzb-form-control"><span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/></td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:pfs.pfs_ins_upd_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')
			</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:cancel()</xsl:with-param>
			</xsl:call-template>
		</div>
		</form>
	</body>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>

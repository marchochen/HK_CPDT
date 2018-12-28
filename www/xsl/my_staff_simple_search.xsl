<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<!-- usr utils -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/usr_search_form_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:variable name="wb_gen_table_width" select="510"/>
	<xsl:variable name="filter_user_group">0</xsl:variable>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/supervise_module">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_search">用戶檢索</xsl:with-param>
			<xsl:with-param name="lab_search_inst">請選擇查詢標準：</xsl:with-param>
			<xsl:with-param name="lab_usr_name">用戶名/全名：</xsl:with-param>
			<xsl:with-param name="lab_search_scope">搜索範圍：</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_search">用户检索</xsl:with-param>
			<xsl:with-param name="lab_search_inst">请选择用户检索条件。</xsl:with-param>
			<xsl:with-param name="lab_usr_name">用户名/全名：</xsl:with-param>
			<xsl:with-param name="lab_search_scope">搜索范围：</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_search">User search</xsl:with-param>
			<xsl:with-param name="lab_search_inst">Please specify the search criteria below:</xsl:with-param>
			<xsl:with-param name="lab_usr_name">User ID / full name：</xsl:with-param>
			<xsl:with-param name="lab_search_scope">Search in：</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_usr_search"/>
		<xsl:param name="lab_search_inst"/>
		<xsl:param name="lab_usr_name"/>
		<xsl:param name="lab_search_scope"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_mystaff.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
		usr = new wbUserGroup;
		var goldenman = new wbGoldenMan;
		var mystaff = new wbMyStaff;
		function status(){return false;}

		function alive() { return true; }


	]]></script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="new_css"></xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="document.frmXml.s_usr_name.focus();">
			<form name="frmXml" method="post" onsubmit="return status()">
				<input type="hidden" name="cmd" value="search_ent_lst"/>
				<input type="hidden" name="ent_id" value=""/>
				<input type="hidden" name="usr_grade" value=""/>
				<input type="hidden" name="stylesheet" value="my_staff_usr_lst.xsl"/>
				<input type="hidden" name="module" value="supervise.SuperviseModule"/>
				<input type="hidden" name="s_usg_ent_id_lst" value="my_staff"/>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_usr_search"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_search_inst"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table  cellpadding="3" border="0" width="{$wb_gen_table_width}" height="86" cellspacing="0" class="Bg">
					<tr>
						<td width="20%" align="right" >
							<span class="TitleText">
								<xsl:copy-of select="$lab_usr_name"/>
							</span>
						</td>
						<td width="80%" style="padding-left:6px">
							<span class="Text">
								<input type="text" class="InputFrm" name="s_usr_name" maxlength="255" size="30" style="width:300px" value=""/>
							</span>
						</td>
					</tr>
					<tr>
						<td width="20%" align="right" >
							<span class="TitleText">
								<xsl:copy-of select="$lab_search_scope"/>
							</span>
						</td>
						<td width="80%" style="padding-left:6px">
							<span class="Text">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="width">200</xsl:with-param>
									<xsl:with-param name="field_name">usr_group_lst</xsl:with-param>
									<xsl:with-param name="tree_type">my_staff</xsl:with-param>
									<xsl:with-param name="select_type">2</xsl:with-param>
									<xsl:with-param name="box_size">1</xsl:with-param>
									<xsl:with-param name="single_option_text"><xsl:value-of select="group_member_list/desc"/></xsl:with-param>
									<xsl:with-param name="single_option_value"><xsl:value-of select="group_member_list/@id"/></xsl:with-param>
									<xsl:with-param name="label_add_btn"><xsl:value-of select="$lab_gen_select"/></xsl:with-param>
									<xsl:with-param name="filter_user_group"><xsl:value-of select="$filter_user_group"/></xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
					</tr>
				</table>
				<xsl:call-template name="wb_ui_line"/>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td align="center">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:mystaff.search_staff_exec(document.frmXml)</xsl:with-param>
							</xsl:call-template>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="group_member_list/all_role_list/role">
		<xsl:param name="mode"/>
		<xsl:variable name="cur_role">
			<xsl:value-of select="@id"/>
		</xsl:variable>
		<xsl:if test="not(position() mod 2 = 0)">
			<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
		</xsl:if>
		<td>
			<span>
				<input type="checkbox" name="usr_role" value="{@id}"/>
			</span>
			<span class="Text">
				<xsl:text>&#160;</xsl:text>
				<xsl:choose>
					<xsl:when test="$mode = 'js'">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str">
								<xsl:call-template name="get_rol_title"/>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="get_rol_title"/>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;</xsl:text>
			</span>
		</td>
		<xsl:choose>
			<xsl:when test="position() mod 2 = 0">
				<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<td width="10">
					<xsl:text>&#160;</xsl:text>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

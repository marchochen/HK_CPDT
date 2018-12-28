<?xml version="1.0"?>
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
	<xsl:variable name="filter_user_group">1</xsl:variable>
<xsl:output indent="yes"/>

<!-- =============================================================== -->
<xsl:template match="/user_manager">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="lang_ch">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_usr_search">用戶檢索</xsl:with-param>
		<xsl:with-param name="lab_search_inst">請選擇查詢標準:</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_legend">注意:次查詢不包含已經<xsl:value-of select="$lab_const_enrolled_sm"/>課程的學員</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_gb">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_usr_search">用户搜索</xsl:with-param>
		<xsl:with-param name="lab_search_inst">请选择用户搜索条件。</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		<xsl:with-param name="lab_legend">注意：此次查询不包含已经<xsl:value-of select="$lab_const_enrolled_sm"/>该课程的学员</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_en">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_usr_search">User search</xsl:with-param>
		<xsl:with-param name="lab_search_inst">Please specify the search criteria below:</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
		<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		<xsl:with-param name="lab_legend">Note: Learners that have <xsl:value-of select="$lab_const_enrolled_sm"/> in the course are excluded from the search</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="content">
	<xsl:param name="lab_usr_search"/>
	<xsl:param name="lab_search_inst"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_legend"/>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
		<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<!--alert样式  -->
		 <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
		
		<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
		<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
		<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
		
		<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
		<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
		<!--alert样式  end -->
		<script language="JavaScript" type="text/javascript"><![CDATA[
		usr = new wbUserGroup;
		var goldenman = new wbGoldenMan;
		function status(){return false;}

		function alive() { return true; }

	]]></script>
		<xsl:call-template name="new_css"/>
	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="document.frmXml.s_usr_id.focus();">
		<form name="frmXml" method="post" onsubmit="javascript:usr.search.popup_search_exec(document.frmXml,'{$wb_lang}', 'usr_supervisor_search_result.xsl'); return false">
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="ent_id" value="{/group_member_list/@id}"/>
			<input type="hidden" name="s_role_types" value=""/>
			<input type="hidden" name="stylesheet" value=""/>
			<input type="hidden" name="fld" value=""/>
			<input type="hidden" name="sel_opt" value=""/>
			<input type="hidden" name="close_opt" value=""/>
			<input type="hidden" name="s_itm_id" value=""/>
			<input type="hidden" name="s_search_enrolled" value=""/>
			<input type="hidden" name="s_search_role" value=""/>
			<input type="hidden" name="refresh_opt" value=""/>
			<input type="hidden" name="disabled_opt" value=""/>
			<input type="hidden" name="filter_user_group" value=""/>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_usr_search"/>
				</xsl:with-param>
				<xsl:with-param name="width">510</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text">
						<xsl:value-of select="$lab_search_inst"/>
							<script language="javascript" type="text/javascript"><![CDATA[
							str = ']]><br/><xsl:value-of select="$lab_legend"/><![CDATA['
			if (getUrlParam('s_itm_id')!='' && getUrlParam('s_itm_id')!=null) {document.write(str);}
		]]></script>

				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line"/>
			<table cellpadding="3" border="0" width="{$wb_gen_table_width}" height="16" cellspacing="0" class="Bg">
				<xsl:apply-templates select="$profile_attributes/*[(not(@active) or @active = 'true') and (@searchable = 'all')]" mode="profile_attributes"/>
			</table>
			<xsl:call-template name="wb_ui_line"/>
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:usr.search.popup_search_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>', 'usr_supervisor_search_result.xsl')</xsl:with-param>
						</xsl:call-template>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:self.close()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
			<script language="JavaScript" type="text/javascript">
				<![CDATA[
					str='<input type="submit" value="" size="0" style="height : 0px;width : 0px;visibility: hidden;"/>'
					if (document.all || document.getElementById!=null){
						document.write(str);
					}
				]]>
			</script>
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
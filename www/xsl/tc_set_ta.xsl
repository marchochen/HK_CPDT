<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:variable name="admin_role" select="tc_module/training_center/role_list"/>
	<xsl:variable name="tc" select="tc_module/training_center"/>
	<xsl:variable name="root_ent_id" select="tc_module/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="last_upd_time" select="$tc/last_updated/@timestamp"/>
	<xsl:variable name="role_id" select="//cur_usr/role/@id"/>
	<!-- ================================================================== -->
	<xsl:template match="/">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_tc_mgt.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				    ct_mgt = new wbTcMgt;
					goldenman = new wbGoldenMan;
					usr = new wbUserGroup;
					var role_tadm = "TADM";
					var role_instr = "INSTR";	
			        function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
						]]><xsl:for-each select="$admin_role/role"><![CDATA[
							if(fld_name == ']]><xsl:value-of select="@id"/><![CDATA[_usr_ent_id_lst'){]]>
								<xsl:value-of select="@id"/><![CDATA[_usr_ent_id_lst(usr_argv);
							}
						]]></xsl:for-each><![CDATA[
			        }			
			    ]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmXml">
					<input type="hidden" name="module"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<input type="hidden" name="url_failure1"/>
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="upd_time" value="{$last_upd_time}"/>
					<input name="role_num" type="hidden">
						<xsl:attribute name="value"><xsl:value-of select="count($admin_role/role)"/></xsl:attribute>
					</input>
					<input type="hidden" name="tc_id" value="{$tc/@id}"/>
					<xsl:call-template name="content"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	
	
	<xsl:variable name="lab_set_ta_title" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_122')"/>
	<xsl:variable name="lab_ta" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_123')"/>
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_124')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_125')"/>
	<xsl:variable name="lab_set_ta_desc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_ss.label_core_system_setting_126')"/>
	
	
	
	<!-- ============================================================================  -->
	<xsl:template name="content">
	
		<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_POSTER_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_TC_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_set_ta_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_set_ta_desc"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_space">
			<xsl:with-param name="width" select="$wb_gen_table_width"/>
		</xsl:call-template>
		<xsl:if test="count($admin_role/role)>0">
			<xsl:apply-templates select="$admin_role/role">
				<xsl:with-param name="lab_ta" select="$lab_ta"/>
			</xsl:apply-templates>
		</xsl:if>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_ok"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">Javascript:ct_mgt.set_ta_exec(frmXml,'<xsl:value-of select="$tc/@id"/>')</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:ct_mgt.tc_detail('<xsl:value-of select="$tc/@id"/>');</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="role">
		<xsl:param name="lab_ta"/>
		<xsl:variable name="id" select="@id"/>
		<xsl:variable name="field_name">
			<xsl:value-of select="@id"/>_usr_ent_id_lst</xsl:variable>
		<input type="hidden" name="role_{position()}" value="{@id}"/>
		<input type="hidden">
			<xsl:attribute name="name"><xsl:value-of select="@id"/>_usr_lst</xsl:attribute>
		</input>
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:choose>
					   	<xsl:when test="starts-with(@ id, 'TADM_')"><xsl:value-of select="$lab_ta"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="@title"/></xsl:otherwise>
				    </xsl:choose>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">
							<xsl:value-of select="$field_name"/>
						</xsl:with-param>
						<xsl:with-param name="name">usr_ent_id_lst</xsl:with-param>
						<xsl:with-param name="box_size">4</xsl:with-param>
						<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
						<xsl:with-param name="select_type">4</xsl:with-param>
						<xsl:with-param name="pick_leave">0</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>
						<xsl:with-param name="label_add_btn">
							<xsl:value-of select="$lab_gen_select"/>
						</xsl:with-param>
						<xsl:with-param name="option_list">
							<xsl:apply-templates select="$tc/officer_list/role[@id = $id]/entity"/>
						</xsl:with-param>
						<xsl:with-param name="search">true</xsl:with-param>
						<xsl:with-param name="rol_ext_id"><xsl:value-of select="$id"/></xsl:with-param>
						<xsl:with-param name="filter_user_group">1</xsl:with-param>
						<xsl:with-param name="search_function">
							javascript:usr.search.popup_search_prep('<xsl:value-of select="$field_name"/>','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '<xsl:value-of select="$id"/>', '0','', '','', '1', '<xsl:value-of select="$tc/@id"/>')
						</xsl:with-param>
						<xsl:with-param name="parent_tcr_id">
							<xsl:if test="starts-with($role_id, 'TADM_')">
								<xsl:value-of select="$tc/parent_tcr/@id"/>
							</xsl:if>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="entity">
		<option value="{@id}">
			<xsl:value-of select="text()"/>
		</option>
	</xsl:template>
</xsl:stylesheet>

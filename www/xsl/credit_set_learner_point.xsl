<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>	
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:output indent="yes"/>
	
	<xsl:variable name="lab_809" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '809')"/>
	<xsl:variable name="lab_813" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '813')"/>
	<xsl:variable name="lab_840" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '840')"/>
	<xsl:variable name="lab_841" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '841')"/>
	<xsl:variable name="lab_842" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '842')"/>
	<xsl:variable name="lab_843" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '843')"/>
	<xsl:variable name="lab_844" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '844')"/>
	<xsl:variable name="lab_845" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '845')"/>
	<xsl:variable name="lab_846" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '846')"/>
	<xsl:variable name="lab_847" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '847')"/>
	<xsl:variable name="lab_329" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/>
	<xsl:variable name="lab_330" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
	<xsl:variable name="lab_cty_INTEGRAL_EMPTY" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_cty_INTEGRAL_EMPTY')"/>

	<xsl:variable name="root_ent_id" select="/credit/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="bonus_cnt" select="count(/credit/manual_cyt_lst[@deduction_ind='false']/manual_cyt)"/>
	<xsl:variable name="deduct_cnt" select="count(/credit/manual_cyt_lst[@deduction_ind='true']/manual_cyt)"/>
	<xsl:variable name="max_count">1000</xsl:variable>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:apply-templates select="credit"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="credit">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_credit.js"/>
			<script language="Javascript"><![CDATA[
			var credit = new wbCredit;
			var usr = new wbUserGroup;
			goldenman = new wbGoldenMan;
			function status() {
				credit.set_point_search(document.frmAction, ']]><xsl:value-of select="$wb_lang"/><![CDATA[');
				return false;
			}
			function search() {
				if (event.keyCode == 13) {
					credit.set_point_search(document.frmAction, ']]><xsl:value-of select="$wb_lang"/><![CDATA[');
				}
				return false;
			}	
			function chang_bonus_type(frm) {
				obj_type = eval('frm.change_type');
				obj_bpt = eval('frm.cty_id');
				if (obj_type) {
					var my_option = new Option();
					if (obj_type.options[obj_type.selectedIndex].value == 1) {
						obj_bpt.length = ]]><xsl:value-of select="$bonus_cnt + 1"/><![CDATA[;
						]]><xsl:for-each select="manual_cyt_lst[@deduction_ind='false']/manual_cyt"><![CDATA[
							obj_bpt.options[]]><xsl:value-of select="position()"/><![CDATA[] = new Option(']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str" select="code"/></xsl:call-template><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[');
						]]></xsl:for-each><![CDATA[
					} else {
						obj_bpt.length = ]]><xsl:value-of select="$deduct_cnt + 1"/><![CDATA[;
						]]><xsl:for-each select="manual_cyt_lst[@deduction_ind='true']/manual_cyt"><![CDATA[
							obj_bpt.options[]]><xsl:value-of select="position()"/><![CDATA[] = new Option(']]><xsl:call-template name="escape_js"><xsl:with-param name="input_str" select="code"/></xsl:call-template><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[');
						]]></xsl:for-each><![CDATA[
					}
				}
			}
			
			function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
				ent_id_lst(usr_argv);
			}
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" onload="wb_utils_gen_form_focus(document.frmAction)">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_813"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:wb_utils_nav_go('CREDIT_OTHER_MAIN');" class="NavLink">
					<xsl:value-of select="$lab_809"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;
					<xsl:value-of select="$lab_813"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<form name="frmAction" onsubmit="return status()">
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="extra_td">
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_846"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:credit.import_prep()</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" ><xsl:value-of select="$lab_cty_INTEGRAL_EMPTY"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript: credit.set_learner_point(true);</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_847"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:credit.set_cos_point_perp()</xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:with-param>
			</xsl:call-template>
			<!-- <xsl:call-template name="wb_ui_line"/> -->			
			<table class="margin-top28">
				<!--选择用户或用户组-->
				<tr>
					<td class="wzb-form-label" valign="top">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_840"/>：
					</td>
					<td class="wzb-form-control">
						<table>				
							<tr>
								<td>
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="frm">document.frmAction</xsl:with-param>
										<xsl:with-param name="field_name">ent_id_lst</xsl:with-param>
										<xsl:with-param name="name">ent_id_lst</xsl:with-param>
										<xsl:with-param name="box_size">8</xsl:with-param>
										<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
										<xsl:with-param name="select_type">1</xsl:with-param>
										<xsl:with-param name="pick_leave">0</xsl:with-param>
										<xsl:with-param name="pick_root">0</xsl:with-param>
										<xsl:with-param name="label_add_btn">
											<xsl:value-of select="$lab_gen_select"/>
										</xsl:with-param>
										<xsl:with-param name="search">true</xsl:with-param>
										<xsl:with-param name="search_function">javascript:usr.search.popup_search_prep('ent_id_lst','','<xsl:value-of select="$root_ent_id"/>', '0', '', '', '0', '0')</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_841"/>：
					</td>
					<td nowrap="nowrap" class="wzb-form-control">
						<select class="wzb-form-select" name="change_type" onchange="javascript:chang_bonus_type(document.frmAction);" style="margin-right:10px;">
							<option value="1" selected="selected">
								<xsl:value-of select="$lab_842"/>
							</option>
							<option value="-1">
								<xsl:value-of select="$lab_843"/>
							</option>
						</select>
						<select class="wzb-form-select" name="cty_id">
							<option value="0" selected="selected">
								<xsl:value-of select="$lab_844"/>
							</option>
							<xsl:apply-templates select="manual_cyt_lst[@deduction_ind='false']/manual_cyt"/>
						</select>
					</td>
				</tr>
				<!--积分-->
				<tr>
					<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_845"/>：
					</td>
					<td class="wzb-form-control">
						<input type="text" id="{$lab_845}" size="{$wb_gen_num_input_size}" name="input_point" class="wzb-inputText" maxlength="6" value=""/>
					</td>
				</tr>
				<tr>
					<td class="wzb-form-label">
					</td>
					<td class="wzb-ui-module-text">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
					</td>
				</tr>
			</table>
			<div class="wzb-bar">
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_329"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:credit.set_learner_point_exec(frmAction);</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_gen_form_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_330"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_nav_go('CREDIT_OTHER_MAIN');</xsl:with-param>
				</xsl:call-template>
			</div>
			<input type="hidden" name="search_type"/>
			<input type="hidden" name="ubd_usr_ent_id"/>
			<input type="hidden" name="usr_n_usg_id_lst"/>
			<input type="hidden" name="cty_deduction_ind"/>
			<input type="hidden" name="change_point"/>
			<input type="hidden" name="module"/>
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="stylesheet"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
		</form>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="manual_cyt">
		<option value="{@id}">
			<xsl:value-of select="code"/>
		</option>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>

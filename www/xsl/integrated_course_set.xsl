<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ====================================================================================================== -->
	<xsl:variable name="lab_875" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '875')"/><!-- Please specify a course/exam that this Integrated Learning consisist of.  -->
	<xsl:variable name="lab_876" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '876')"/><!-- Compulsory -->
	<xsl:variable name="lab_877" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '877')"/><!-- Elective -->
	<xsl:variable name="lab_878" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '878')"/><!-- Requirement -->
	<xsl:variable name="lab_879" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '879')"/><!-- course/exam -->
	<xsl:variable name="lab_872" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '872')"/><!-- Complete -->
	<xsl:variable name="lab_870" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '870')"/><!-- Course/Exam -->
	<xsl:variable name="lab_377" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '377')"/><!-- Requirement -->
	<xsl:variable name="lab_265" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '265')"/><!-- type -->
	<xsl:variable name="lab_329" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/><!-- OK -->
	<xsl:variable name="lab_330" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/><!-- Cancel -->
	<xsl:variable name="lab_335" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '335')"/><!-- * Required -->
	<xsl:variable name="lab_874" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1185')"/><!-- courses -->
	<xsl:variable name="lab_title">
		<xsl:choose>
			<xsl:when test="integrated_learning/complete_condition/icd/@id > 0">
				<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '873')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text> </xsl:text>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '879')"/>
	</xsl:variable>

	<xsl:variable name="itm_id" select="integrated_learning/complete_condition/@itm_id"/>
	<xsl:variable name="icc_id" select="integrated_learning/complete_condition/@icc_id"/>
	<xsl:variable name="tc_enabled" select="/integrated_learning/meta/tc_enabled"/>
	<!-- ====================================================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="integrated_learning"/>
		</html>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="integrated_learning">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			itm_lst = new wbItem;
			goldenman = new wbGoldenMan;
			]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="icd_type"/>
				<input type="hidden" name="itm_condition_list"/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="icc_id" value="{$icc_id}"/>
				<input type="hidden" name="icd_id" value="{complete_condition/icd/@id}"/>
				<input type="hidden" name="icd_update_timestamp" value="{complete_condition/icd/@update_timestamp}"/>
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template name="content">
		<xsl:call-template name="itm_action_nav">
	    	<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
			<xsl:with-param  name="cur_node_id">06</xsl:with-param>
		</xsl:call-template>
	<div class="wzb-item-main">

		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_875"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
					<xsl:value-of select="//itm_action_nav/@itm_title"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_874"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_265"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<input type="radio" name="icd_type_radio" id="compulsory" value="COMPULSORY">
						<xsl:if test="complete_condition/type='COMPULSORY'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<xsl:value-of select="$lab_876"/>
					<input style="margin-left:15px;" type="radio" name="icd_type_radio" id="compulsory" value="ELECTIVE">
						<xsl:if test="complete_condition/type='ELECTIVE'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<xsl:value-of select="$lab_877"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<input type="hidden" name="lab_878" value="{$lab_878}"/>
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_878" />
					<xsl:text>：</xsl:text>
				</td>
				<xsl:variable name="completed_item_count">
					<xsl:if test="complete_condition/completed_item_count"> <xsl:value-of select="complete_condition/completed_item_count"/></xsl:if>
				</xsl:variable>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_872"/>
					<input type="text" class="wzb-inputText" style="width:50px; margin-left:10px;margin-right:10px;" name="icd_completed_item_count" value="{$completed_item_count}"></input>
					
					<xsl:value-of select="$lab_879"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<input type="hidden" name="lab_870" value="{$lab_870}"/>
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_870"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="tree_type">
						<xsl:choose>
							<xsl:when test="$tc_enabled = 'true'">tc_catalog_item_integrated</xsl:when>
							<xsl:otherwise>item</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">itm_id_lst</xsl:with-param>
						<xsl:with-param name="name">itm_id_lst</xsl:with-param>
						<xsl:with-param name="box_size">4</xsl:with-param>
						<xsl:with-param name="tree_type"><xsl:value-of select="$tree_type"/></xsl:with-param>
						<xsl:with-param name="select_type">3</xsl:with-param>
						<xsl:with-param name="args_type">row</xsl:with-param>
						<xsl:with-param name="complusory_tree">0</xsl:with-param>
						<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>
						<xsl:with-param name="itm_id"><xsl:value-of select="$itm_id"/></xsl:with-param>
						<!--
						<xsl:with-param name="add_function">
						goldenman.opentree('<xsl:value-of select="$tree_type"/>',3,'itm_id_lst','','1','','','1','0', '0', '', '0','0', '0', '', '', '0', '1', '', '','','',<xsl:value-of select="$itm_id"/>)
						</xsl:with-param>
-->
						<xsl:with-param name="option_list">
							<xsl:for-each select="complete_condition/itm_lst/itm">
								<option value="{@id}">
									<xsl:value-of select="@title"/>
								</option>
							</xsl:for-each>
						</xsl:with-param>
						<xsl:with-param name="label_add_btn">
							<xsl:value-of select="$lab_gen_select"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_335"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_329"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">Javascript:itm_lst.set_condition_exec(frmXml)</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_330"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">Javascript:itm_lst.get_course_list(<xsl:value-of select="$itm_id"/>);</xsl:with-param>
			</xsl:call-template>
		</div>
		</div>
	</xsl:template>
	<!-- ====================================================================================================== -->
</xsl:stylesheet>

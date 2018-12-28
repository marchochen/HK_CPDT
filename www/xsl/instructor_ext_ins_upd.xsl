<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl" />
	<xsl:import href="cust/wb_cust_const.xsl" />
	<xsl:import href="utils/wb_css.xsl" />
	<xsl:import href="utils/display_time.xsl" />
	<xsl:import href="utils/wb_gen_form_button.xsl" />
	<xsl:import href="utils/wb_ui_footer.xsl" />
	<xsl:import href="utils/wb_ui_head.xsl" />
	<xsl:import href="utils/wb_ui_line.xsl" />
	<xsl:import href="utils/wb_ui_hdr.xsl" />
	<xsl:import href="utils/wb_ui_title.xsl" />
	<xsl:import href="utils/wb_utils.xsl" />
	<xsl:import href="utils/wb_gen_button.xsl" />
	<xsl:import href="utils/wb_ui_show_no_item.xsl" />

	<xsl:import href="share/instr_tpl_share.xsl" />
	<xsl:import href="share/usr_detail_label_share.xsl" />
	<!-- =========================================================================== -->
	<xsl:variable name="lab_page_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_INT_INSTRUCTOR')" />
	<xsl:variable name="lab_btn_save" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '159')" />
	<xsl:variable name="lab_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')" />
	<xsl:variable name="lab_btn_add" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1046')" />
	<xsl:variable name="lab_btn_del" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')" />
	<xsl:variable name="lab_939" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '939')" />
	<xsl:variable name="lab_940" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '940')" />
	<xsl:variable name="lab_941" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '941')" />
	<xsl:variable name="lab_942" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '942')" />
	<xsl:variable name="lab_943" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '943')" />
	<xsl:variable name="lab_944" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '944')" />
	<xsl:variable name="lab_945" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '945')" />
	<xsl:variable name="lab_946" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '946')" />
	<xsl:variable name="lab_947" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '947')" />
	<xsl:variable name="lab_948" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '948')" />
	<xsl:variable name="lab_949" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '949')" />
	<xsl:variable name="lab_950" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '950')" />
	<xsl:variable name="lab_951" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '951')" />
	<xsl:variable name="lab_952" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '952')" />
	<xsl:variable name="lab_953" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '953')" />
	<xsl:variable name="lab_954" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '954')" />
	<xsl:variable name="lab_955" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '955')" />
	<xsl:variable name="lab_956" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '956')" />
	<xsl:variable name="lab_957" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '957')" />
	<xsl:variable name="lab_958" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '958')" />
	<xsl:variable name="lab_959" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '959')" />
	<xsl:variable name="lab_960" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '960')" />
	<xsl:variable name="lab_961" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '961')" />
	<xsl:variable name="lab_962" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '962')" />
	<xsl:variable name="lab_963" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '963')" />
	<xsl:variable name="lab_964" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '964')" />
	<xsl:variable name="lab_965" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '965')" />
	<xsl:variable name="lab_966" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '966')" />
	<xsl:variable name="lab_967" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '967')" />
	<xsl:variable name="lab_968" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '968')" />
	<xsl:variable name="lab_969" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '969')" />
	<xsl:variable name="lab_1055" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1055')" />
	<xsl:variable name="lab_1056" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1056')" />
	<xsl:variable name="lab_1057" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1057')" />
	<xsl:variable name="lab_1058" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1058')" />
	<xsl:variable name="lab_1059" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1059')" />
	<xsl:variable name="lab_1060" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1060')" />
	<xsl:variable name="lab_1061" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1061')" />
	<xsl:variable name="lab_1062" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1062')" />
	<xsl:variable name="lab_1063" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1063')" />
	<xsl:variable name="lab_1065" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1065')" />
	<xsl:variable name="lab_1072" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1072')" />
	<xsl:variable name="lab_1073" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1073')" />
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	
	<xsl:variable name="instr" select="/instructor/instructor" />
	<xsl:variable name="refuser" select="/instructor/ref_user/user" />
	
 
 
	<xsl:variable name="default_usr_icon" select="/instructor/meta/default_usr_icon/text()" />
	
	<xsl:variable name="default_inst_icon" >user/instructor_default.png</xsl:variable>
	
	
	<xsl:variable name="iti_property_options" select="/instructor/meta/iti_property_options" />
	<xsl:variable name="iti_type_options" select="/instructor/meta/iti_type_options" />
	<xsl:variable name="iti_level_options" select="/instructor/meta/iti_level_options" />
	<xsl:variable name="iti_gender_options" select="/instructor/meta/iti_gender_options" />

	<xsl:variable name="is_edit">
		<xsl:choose>
			<xsl:when test="$instr/iti_ent_id != '' and $instr/iti_ent_id &gt; 0">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =========================================================================== -->
	<xsl:output indent="yes" />
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank" />
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}" />
				<link rel="stylesheet" type="text/css" href="../static/css/thickbox.css" />
			
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.migrate.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_instructor.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
				
				<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>
				<script type="text/javascript"><![CDATA[
					usr = new wbUserGroup;
					instr = new wbInstructor;
				]]></script>
			</head>
			<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
				<form enctype="multipart/form-data" name="frmXml">
					<input name="module" type="hidden" />
					<input name="cmd" type="hidden" />
					<input name="url_success" type="hidden" />
					<input name="url_failure" type="hidden" />
					<input name="iti_type_mark" type="hidden" value="EXT"/>
					<input name="iti_ent_id" type="hidden" value="{$instr/iti_ent_id}"/>
						
					<xsl:call-template name="content" />
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =========================================================================== -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_EXT_INSTRUCTOR_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_EXT_INSTRUCTOR_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$is_edit = 'true'">
						<xsl:value-of select="$lab_1073" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_1073" />
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>

		<!-- ===================== Search Form ===================== -->
		<table>
			<tr>
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_939"/>
					<xsl:with-param name="val" select="$instr/iti_name"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="maxlength">20</xsl:with-param>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="field_name">iti_name</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="select_tpl">
					<xsl:with-param name="lab" select="$lab_gender"/>
					<xsl:with-param name="val" select="$instr/iti_gender"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="field_name">iti_gender</xsl:with-param>
					<xsl:with-param name="options" select="$iti_gender_options" />
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="datetime_tpl">
					<xsl:with-param name="lab" select="$lab_bday"/>
					<xsl:with-param name="val" select="$instr/iti_bday"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="field_name">iti_bday</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_942"/>
					<xsl:with-param name="val" select="$instr/iti_mobile"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="maxlength">16</xsl:with-param>
					<xsl:with-param name="field_name">iti_mobile</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_e_mail"/>
					<xsl:with-param name="val" select="$instr/iti_email"/>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="maxlength">50</xsl:with-param>
					<xsl:with-param name="field_name">iti_email</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:variable name="usericon">
					<xsl:choose>
						<xsl:when test="$instr/iti_img != $default_usr_icon"><xsl:value-of select="$instr/iti_img"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$default_inst_icon"/></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="instr_icon_tpl">
					<xsl:with-param name="lab" select="$lab_950"/>
					<xsl:with-param name="val" select="$usericon"/>
					<xsl:with-param name="lab_color">color:#666;</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 讲师级别 -->
				<xsl:call-template name="select_tpl">
					<xsl:with-param name="lab" select="$lab_951"/>
					<xsl:with-param name="val" select="$instr/iti_level"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="field_name">iti_level</xsl:with-param>
					<xsl:with-param name="options" select="$iti_level_options" />
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 授课类别 -->
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_952"/>
					<xsl:with-param name="val" select="$instr/iti_cos_type"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="maxlength">20</xsl:with-param>
					<xsl:with-param name="width">200</xsl:with-param>
					<xsl:with-param name="field_name">iti_cos_type</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 主讲课程 -->
				<xsl:call-template name="textarea_tpl">
					<xsl:with-param name="lab" select="$lab_955"/>
					<xsl:with-param name="val" select="$instr/iti_main_course"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="field_name">iti_main_course</xsl:with-param>
					<xsl:with-param name="colspan">1</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 讲师简介 -->
				<xsl:call-template name="textarea_tpl">
					<xsl:with-param name="lab" select="$lab_1055"/>
					<xsl:with-param name="val" select="$instr/iti_introduction"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="field_name">iti_introduction</xsl:with-param>
					<xsl:with-param name="colspan">1</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 擅长领域 -->
				<xsl:call-template name="textarea_tpl">
					<xsl:with-param name="lab" select="$lab_1056"/>
					<xsl:with-param name="val" select="$instr/iti_expertise_areas"/>
					<xsl:with-param name="required">true</xsl:with-param>
					<xsl:with-param name="field_name">iti_expertise_areas</xsl:with-param>
					<xsl:with-param name="colspan">1</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 擅长行业 -->
				<xsl:call-template name="textarea_tpl">
					<xsl:with-param name="lab" select="$lab_1057"/>
					<xsl:with-param name="val" select="$instr/iti_good_industry"/>
					<xsl:with-param name="required">false</xsl:with-param>
					<xsl:with-param name="field_name">iti_good_industry</xsl:with-param>
					<xsl:with-param name="colspan">1</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 所在机构 -->
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_1058"/>
					<xsl:with-param name="val" select="$instr/iti_training_company"/>
					<xsl:with-param name="required">false</xsl:with-param>
					<xsl:with-param name="maxlength">50</xsl:with-param>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="field_name">iti_training_company</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 所在机构联系人 -->
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_1059"/>
					<xsl:with-param name="val" select="$instr/iti_training_contacts"/>
					<xsl:with-param name="required">false</xsl:with-param>
					<xsl:with-param name="maxlength">20</xsl:with-param>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="field_name">iti_training_contacts</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 所在机构电话 -->
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_1060"/>
					<xsl:with-param name="val" select="$instr/iti_training_tel"/>
					<xsl:with-param name="required">false</xsl:with-param>
					<xsl:with-param name="maxlength">16</xsl:with-param>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="field_name">iti_training_tel</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 所在机构邮箱 -->
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_1061"/>
					<xsl:with-param name="val" select="$instr/iti_training_email"/>
					<xsl:with-param name="required">false</xsl:with-param>
					<xsl:with-param name="maxlength">50</xsl:with-param>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="field_name">iti_training_email</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 所在机构地址 -->
				<xsl:call-template name="text_tpl">
					<xsl:with-param name="lab" select="$lab_1062"/>
					<xsl:with-param name="val" select="$instr/iti_training_address"/>
					<xsl:with-param name="required">false</xsl:with-param>
					<xsl:with-param name="maxlength">200</xsl:with-param>
					<xsl:with-param name="width">500</xsl:with-param>
					<xsl:with-param name="field_name">iti_training_address</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="lab_val_tpl">
					<xsl:with-param name="lab" select="$lab_960"/>
					<xsl:with-param name="val" select="$instr/iti_score"/>
					<xsl:with-param name="colspan">4</xsl:with-param>
				</xsl:call-template>
			</tr>
		</table>
		
		<!-- 删除掉可提供课程 -->
		<!-- <br />		
		<table>
			<tr>
				<td align="left" valign="bottom"><span class="Head"><xsl:value-of select="$lab_1065" /></span></td>
				<td align="right" valign="bottom">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_add"/>
						<xsl:with-param name="wb_gen_btn_href">javascript: instr.add_course(document.frmXml);</xsl:with-param>
						<xsl:with-param name="class" >btn wzb-btn-orange</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>	
		</table>
		<xsl:call-template name="wb_ui_line" />
		<table>
			<tr>
				<td><span class="wzb-form-star">*</span><xsl:value-of select="$lab_962" /></td>
				<td><span class="wzb-form-star">*</span><xsl:value-of select="$lab_963" /></td>
				<td><span class="wzb-form-star">*</span><xsl:value-of select="$lab_964" /></td>
				<td><span class="wzb-form-star">*</span><xsl:value-of select="$lab_965" /></td>
				<td><span class="wzb-form-star">*</span><xsl:value-of select="$lab_966" /></td>
				<td></td>
			</tr>
			<tbody id="instr_cos_contariner"></tbody>
		</table>
		<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
		<input type="hidden" name="lab_btn_del" value="{$lab_btn_del}" />
		<input type="hidden" name="lab_ics_title" value="{$lab_962}" />
		<input type="hidden" name="lab_ics_fee" value="{$lab_963}" />
		<input type="hidden" name="lab_ics_hours" value="{$lab_965}" />
		<input type="hidden" name="lab_ics_target" value="{$lab_964}" />
		<input type="hidden" name="lab_ics_content" value="{$lab_966}" />
		<input type="hidden" name="ics_title" />
		<input type="hidden" name="ics_fee" />
		<input type="hidden" name="ics_hours" />
		<input type="hidden" name="ics_target" />
		<input type="hidden" name="ics_content" />
		<script type="text/javascript"><![CDATA[
			$(document).ready(function() {
			]]>
			<xsl:choose>
			<xsl:when test="$is_edit = 'true' and $instr and count($instr/instr_cos/cos) &gt;0" >
				<xsl:for-each select="$instr/instr_cos/cos">
				<![CDATA[
				var ics_title = ']]><xsl:value-of select="ics_title/text()"/><![CDATA[';
				var ics_fee = ']]><xsl:value-of select="ics_fee/text()"/><![CDATA[';
				var ics_target = ']]><xsl:value-of select="ics_target/text()"/><![CDATA[';
				var ics_hours = ']]><xsl:value-of select="ics_hours/text()"/><![CDATA[';
				var ics_content = ']]><xsl:value-of select="ics_content/text()"/><![CDATA[';
				instr.add_course(document.frmXml, ics_title, ics_fee, ics_hours, ics_target, ics_content);
				]]>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise><![CDATA[
				for (var i = 0; i < 2; i++) {
					instr.add_course(document.frmXml);
				}
			]]></xsl:otherwise>
			</xsl:choose>
		<![CDATA[
			});
		]]></script> -->
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_save"/>
				<xsl:with-param name="wb_gen_btn_href">javascript: instr.save_exec(document.frmXml,'EXT');</xsl:with-param>
				<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_cancel"/>
				<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back();</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>
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
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	
	<xsl:import href="share/instr_tpl_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<!-- =========================================================================== -->
	<xsl:variable name="lab_page_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_INT_INSTRUCTOR')" />
	<xsl:variable name="lab_btn_save" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')" />
	<xsl:variable name="lab_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')" />
	<xsl:variable name="lab_btn_add" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1046')" />
	<xsl:variable name="lab_btn_del" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')" />
	<xsl:variable name="lab_btn_edit" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '873')" />
	<xsl:variable name="lab_btn_back" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '55')" />
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
	<xsl:variable name="lab_979" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '979')" />
	<xsl:variable name="lab_1055" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1055')" />
	<xsl:variable name="lab_1056" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1056')" />
	<xsl:variable name="lab_1057" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1057')" />
	<xsl:variable name="lab_1058" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1058')" />
	<xsl:variable name="lab_1059" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1059')" />
	<xsl:variable name="lab_1060" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1060')" />
	<xsl:variable name="lab_1061" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1061')" />
	<xsl:variable name="lab_1062" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1062')" />
	<xsl:variable name="lab_1063" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1063')" />
	<xsl:variable name="lab_1064" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1064')" />
	<xsl:variable name="lab_1065" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1065')" />
	<xsl:variable name="lab_1071" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1126')" />
	<xsl:variable name="lab_1187" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1187')" />
	<xsl:variable name="lab_1188" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1188')" />
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	
	<xsl:variable name="instr" select="/instructor/instructor" />
 
 
	<xsl:variable name="default_usr_icon" select="/instructor/meta/default_usr_icon/text()" />
	<xsl:variable name="isExcludes" select="/instructor/meta/isExcludes/text()" />

	
		<!--  导师默认头像 -->
	<xsl:variable name="default_inst_icon"  >user/instructor_default.png</xsl:variable>
	
	<xsl:variable name="iti_property_options" select="/instructor/meta/iti_property_options" />
	<xsl:variable name="iti_type_options" select="/instructor/meta/iti_type_options" />
	<xsl:variable name="iti_level_options" select="/instructor/meta/iti_level_options" />
	<xsl:variable name="iti_gender_options" select="/instructor/meta/iti_gender_options" />
	<xsl:variable name="iti_cos_count" select="count(/instructor/instructor/instr_cos/cos)" />
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
				
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.migrate.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_instructor.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
				
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
				<script type="text/javascript" src="../static/js/thickbox-compressed.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_defaultImage.js"/>
				<script type="text/javascript"><![CDATA[
					usr = new wbUserGroup;
					instr = new wbInstructor;
				]]></script>
				<xsl:call-template name="new_css" />
			</head>
			<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
				<form name="frmXml">
					<input name="module" type="hidden" />
					<input name="cmd" type="hidden" />
					<input name="url_success" type="hidden" />
					<input name="url_failure" type="hidden" />
					<input name="iti_ent_id" type="hidden" value="{$instr/iti_ent_id}"/>
					<input name="lab_del_confirm" type="hidden" value="{$lab_1064}"/>
						
					<xsl:call-template name="content" />
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =========================================================================== -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_EXT_INSTRUCTOR_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$instr/iti_name" />
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:if test="$isExcludes != 'true'">
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<a href="javascript: instr.list('EXT');" class="NavLink">
						<xsl:value-of select="$lab_1071" />
					</a>
					<span class="NavLink">
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$instr/iti_name" />
					</span>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		
		<table>
			<tr>
				<td align="right" style="padding:10px 8px;">
				    <xsl:variable name="iti_recommend">
						<xsl:choose>
							<xsl:when test="$instr/iti_recommend &lt; 1 ">1</xsl:when>
							<xsl:otherwise>0</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="iti_recommend_title">
					    <xsl:choose>
							<xsl:when test="$instr/iti_recommend &lt; 1 ">
<!-- 							推荐 --><xsl:value-of select="$lab_1187"></xsl:value-of>
							</xsl:when>
							<xsl:otherwise>
<!-- 							取消推荐 --><xsl:value-of select="$lab_1188"></xsl:value-of>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<!-- <xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name">
					     	<xsl:value-of select="$iti_recommend_title"></xsl:value-of>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript: instr.recommend_instr('<xsl:value-of select="$instr/iti_ent_id"/>', '<xsl:value-of select="$iti_recommend"/>')</xsl:with-param>
					</xsl:call-template> -->
				
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_979"/>
						<xsl:with-param name="wb_gen_btn_href">Javascript: instr.view_course('<xsl:value-of select="$instr/iti_ent_id"/>', '<xsl:value-of select="$instr/iti_type_mark" />')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_edit"/>
						<xsl:with-param name="wb_gen_btn_href">javascript: instr.ext_ins_upd_prep('<xsl:value-of select="$instr/iti_ent_id" />');</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_del"/>
						<xsl:with-param name="wb_gen_btn_href">javascript: instr.del_exec(document.frmXml, '<xsl:value-of select="$instr/iti_type_mark" />', '<xsl:value-of select="$instr/iti_ent_id" />');</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		
		<table>
			<tr>
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_939"/>
					<xsl:with-param name="val" select="$instr/iti_name"/>
					<xsl:with-param name="width">80%</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="view_field_opt_tpl">
					<xsl:with-param name="lab" select="$lab_gender"/>
					<xsl:with-param name="val" select="$instr/iti_gender"/>
					<xsl:with-param name="options" select="$iti_gender_options" />
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="view_field_date_tpl">
					<xsl:with-param name="lab" select="$lab_bday"/>
					<xsl:with-param name="val" select="$instr/iti_bday"/>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_942"/>
					<xsl:with-param name="val" select="$instr/iti_mobile"/>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_e_mail"/>
					<xsl:with-param name="val" select="$instr/iti_email"/>
				</xsl:call-template>
			</tr>
			<tr>
				<xsl:call-template name="view_cust_field_tpl">
					<xsl:with-param name="lab" select="$lab_950"/>
					<xsl:with-param name="val">
						<xsl:variable name="usericon">
							<xsl:choose>
								<xsl:when test="$instr/iti_img != $default_usr_icon"><xsl:value-of select="$instr/iti_img"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$default_inst_icon"/></xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<img src="../{$usericon}" border="0" width="50" height="50" style="border-radius:50%;"/>  
					</xsl:with-param>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 讲师级别 -->
				<xsl:call-template name="view_field_opt_tpl">
					<xsl:with-param name="lab" select="$lab_951"/>
					<xsl:with-param name="val" select="$instr/iti_level"/>
					<xsl:with-param name="options" select="$iti_level_options" />
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 授课类别 -->
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_952"/>
					<xsl:with-param name="val" select="$instr/iti_cos_type"/>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 主将课程 -->
				<xsl:call-template name="view_field_text_tpl">
					<xsl:with-param name="lab" select="$lab_955"/>
					<xsl:with-param name="val" select="$instr/iti_main_course"/>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 讲师简介 -->
				<xsl:call-template name="view_field_text_tpl">
					<xsl:with-param name="lab" select="$lab_1055"/>
					<xsl:with-param name="val" select="$instr/iti_introduction"/>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 擅长领域 -->
				<xsl:call-template name="view_field_text_tpl">
					<xsl:with-param name="lab" select="$lab_1056"/>
					<xsl:with-param name="val" select="$instr/iti_expertise_areas"/>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 擅长行业 -->
				<xsl:call-template name="view_field_text_tpl">
					<xsl:with-param name="lab" select="$lab_1057"/>
					<xsl:with-param name="val" select="$instr/iti_good_industry"/>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 所在机构 -->
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_1058"/>
					<xsl:with-param name="val" select="$instr/iti_training_company"/>
				</xsl:call-template>
			</tr>
			<tr>
				<!-- 所在机构联系人 -->
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_1059"/>
					<xsl:with-param name="val" select="$instr/iti_training_contacts"/>
				</xsl:call-template>				
			</tr>
			<tr>
				<!-- 所在机构电话 -->
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_1060"/>
					<xsl:with-param name="val" select="$instr/iti_training_tel"/>
				</xsl:call-template>	
			</tr>
			<tr>
				<!-- 所在机构邮箱 -->
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_1061"/>
					<xsl:with-param name="val" select="$instr/iti_training_email"/>
				</xsl:call-template>			
			</tr>
			<tr>
				<!-- 所在机构地址 -->
				<xsl:call-template name="view_field_tpl">
					<xsl:with-param name="lab" select="$lab_1062"/>
					<xsl:with-param name="val" select="$instr/iti_training_address"/>
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
		
		<br />
		<xsl:if test="$iti_cos_count &gt;0">		
			<table><!-- 可提供课程如下 -->
				<tr>
					<td align="left" valign="bottom" class="wzb-before"><xsl:value-of select="$lab_1065" /></td>
				</tr>
					
			</table>
			<table class="table wzb-ui-table"><!-- 可提供课程列表 -->
				<tr class="wzb-ui-table-head">
					<td><span class="TitleText"><xsl:value-of select="$lab_962" /></span></td>
					<td><span class="TitleText"><xsl:value-of select="$lab_963" /></span></td>
					<td><span class="TitleText"><xsl:value-of select="$lab_964" /></span></td>
					<td><span class="TitleText"><xsl:value-of select="$lab_965" /></span></td>
					<td align="right"><span class="TitleText"><xsl:value-of select="$lab_966" /></span></td>
				</tr>
				<tbody id="instr_cos_contariner"></tbody>
			</table>
		</xsl:if>
		<script type="text/javascript">
		<![CDATA[
			$(document).ready(function() {
		]]>
			<xsl:if test="$instr and count($instr/instr_cos/cos) &gt;0" >
				<xsl:for-each select="$instr/instr_cos/cos">
		<![CDATA[
				var ics_title = ']]><xsl:value-of select="ics_title/text()"/><![CDATA[';
				var ics_fee = ']]><xsl:value-of select="ics_fee/text()"/><![CDATA[';
				var ics_target = ']]><xsl:value-of select="ics_target/text()"/><![CDATA[';
				var ics_hours = ']]><xsl:value-of select="ics_hours/text()"/><![CDATA[';
				var ics_content = ']]><xsl:value-of select="ics_content/text()"/><![CDATA[';
				instr.add_view_course(ics_title, ics_fee, ics_hours, ics_target, ics_content);
		]]>
				</xsl:for-each>
			</xsl:if>
		<![CDATA[
			});
		]]></script>
	</xsl:template>
</xsl:stylesheet>
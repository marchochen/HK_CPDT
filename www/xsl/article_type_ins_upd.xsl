<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/kindeditor.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/article_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	
	<xsl:variable name="tc_enabled" select="//meta/tc_enabled"/>
	<xsl:variable name="page_variant" select="//meta/page_variant"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_aty_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan8')"/> 	
	<xsl:variable name="lab_article_field_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '79')"/> 	
	<xsl:variable name="lab_tc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/> 	

	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 	
	<xsl:variable name="lab_add_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/> 	
	
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '590')"/> 	
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	
	<!-- ============================================================= -->
	<xsl:variable name="ins_or_upd">
		<xsl:choose>
			<xsl:when test="/article_module/aty/@id &gt;= 1">UPD</xsl:when>
			<xsl:otherwise>INS</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="aty_title" select="/article_module/aty/@title" />
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_article.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}wb_goldenman.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
 				var wbarticle = new wbArticle;
				var goldenman = new wbGoldenMan
				
				// 绑定input事件
				$(document).on("keydown","input[name='aty_title']",function(e){
					if(e.which == 13){ // 按enter
						$("input[name='frmSubmitBtn']")[0].click();
						return false;
					}
				});

 			]]></script> 
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form enctype="multipart/form-data" name="frmXml">
			<input type="hidden" name="module"/>
			<input type="hidden" name="cmd">			
				<xsl:attribute name="value">
					<xsl:choose>
						<xsl:when test="$ins_or_upd = 'INS'">add_article_type</xsl:when>
						<xsl:otherwise>add_article_type</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</input>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="aty_id" value="{//aty/@id}"/>
			<input type="hidden" name="curLan" value="{//cur_usr/@curLan}" />
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_ARTICLE_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$ins_or_upd='INS'">
							<xsl:value-of select="$lab_add_button"/>
						</xsl:when>
						<xsl:when test="$ins_or_upd='UPD'">
							<xsl:value-of select="$lab_update_button"/>
						</xsl:when>
					</xsl:choose>

					 <xsl:value-of select="$lab_aty_title"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:apply-templates select="aty"/>
		</form>
	</xsl:template>

	
	<!-- =============================================================== -->
	<xsl:template match="aty">
		<table>
	<!--标题-->
			<tr>
				<td class="wzb-form-label">
						<span class="wzb-form-star">*</span><xsl:value-of select="$lab_article_field_title"/>：
						<input type="hidden" name="lab_article_field_title" value="{$lab_article_field_title}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" name="aty_title"  class="wzb-inputText" value="{$aty_title}"/>
				</td>
			</tr>
			
			<!-- tc select 
			<xsl:if test="$tc_enabled='true'">
				<tr>
					<td width="20%" align="right">
						<span class="TitleText">
							*<xsl:value-of select="$lab_tc"/>:</span>
					</td>
					
					<xsl:variable name="cur_tcr_id">
						<xsl:choose>
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//aty/@tcr_id"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="//default_training_center/@id"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:variable name="cur_tcr_title">
						<xsl:choose>
							<xsl:when test="not(//default_training_center)"><xsl:value-of select="//aty/@tcr_title"/></xsl:when>
							<xsl:otherwise><xsl:value-of select="//default_training_center/title"/></xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<td width="80%">
						<xsl:call-template name="wb_goldenman">
							<xsl:with-param name="field_name">tcr_id</xsl:with-param>
							<xsl:with-param name="name">tcr_id</xsl:with-param>
							<xsl:with-param name="box_size">1</xsl:with-param>
							<xsl:with-param name="tree_type">training_center</xsl:with-param>
							<xsl:with-param name="select_type">2</xsl:with-param>
							<xsl:with-param name="pick_leave">0</xsl:with-param>
							<xsl:with-param name="pick_root">0</xsl:with-param>
							<xsl:with-param name="single_option_value"><xsl:value-of select="$cur_tcr_id"/></xsl:with-param>
							<xsl:with-param name="single_option_text"><xsl:value-of select="$cur_tcr_title"/></xsl:with-param>
						</xsl:call-template>
					</td>
					<input type="hidden" name="aty_tcr_id"/>
				</tr>
				
			</xsl:if>			
-->

			<tr>
				<td class="wzb-form-label">
				</td>
				<td class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
	<!-- =============================================================== -->
								
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.ins_upd_article_type(document.frmXml,  '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:wbarticle.get_article_type_maintain()</xsl:with-param>
			</xsl:call-template>
		</div>
		
	</xsl:template>
	
	<!-- =============================================================== -->
	
</xsl:stylesheet>



<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>	
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="share/lab_credit_type.xsl"/>
	<!-- cust utils -->
	<!-- -->
	<xsl:output indent="yes"/>
	<xsl:variable name="cur_usr">
		<xsl:value-of select="auto_bonus_point/meta/cur_usr/@id"/>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:variable name="lab_809" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '809')"/>
	<xsl:variable name="lab_810" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '810')"/>
	<xsl:variable name="lab_814" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '814')"/>
	<xsl:variable name="lab_815" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '815')"/>
	<xsl:variable name="lab_816" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '816')"/>
	<xsl:variable name="lab_817" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '817')"/>
	<xsl:variable name="lab_818" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '818')"/>
	<xsl:variable name="lab_819" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '819')"/>
	<xsl:variable name="lab_329" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/>
	<xsl:variable name="lab_330" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="main">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_credit.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="Javascript"><![CDATA[
				var credit = new wbCredit;
			]]></script>
			</head>
			<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
				<form name="frmXml" class="wzb-form">
					<input type="hidden" name="cmd" value=""/>
					<input type="hidden" name="module" value=""/>
					<input type="hidden" name="url_success" value=""/>
					<input type="hidden" name="url_failure" value=""/>
					<input type="hidden" name="bp_upd_time" value="{//credit/auto_cty_lst/cty/@update_timestamp}"/>
					<xsl:call-template name="content"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_810"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:credit.get_manage_page()" class="NavLink">
					<xsl:value-of select="$lab_809"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_810"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<table>
			
			<tr valign="top">
				<td class="wzb-form-label">
					<table>
						<tr>
							<td width="40%" align="left" valign="bottom" class="wzb-before"><xsl:value-of select="$lab_817"/></td>
						</tr>
						<xsl:for-each select="//credit/auto_cty_lst/cty[code='ZD_INIT']">
							<xsl:call-template name="draw_credit_type">
								<xsl:with-param name="left">40%</xsl:with-param>
								<xsl:with-param name="right">60%</xsl:with-param>
							</xsl:call-template>
						</xsl:for-each>
						<xsl:for-each select="//credit/auto_cty_lst/cty[code='SYS_NORMAL_LOGIN']">
							<xsl:call-template name="draw_credit_type">
								<xsl:with-param name="left">40%</xsl:with-param>
								<xsl:with-param name="right">60%</xsl:with-param>
							</xsl:call-template>
						</xsl:for-each>
					</table>
					<table>
						<tr>
							<td width="40%" align="left" valign="bottom" class="wzb-before"><xsl:value-of select="$lab_819"/></td>
						</tr>
						<xsl:for-each select="//credit/auto_cty_lst/cty[@relation_type!='COS' and code!='ZD_INIT' and code!='SYS_NORMAL_LOGIN' and code!='SYS_ANWSER_BOUNTY' and code!='SYS_QUESTION_BOUNTY']">
							<xsl:call-template name="draw_credit_type">
								<xsl:with-param name="left">40%</xsl:with-param>
								<xsl:with-param name="right">60%</xsl:with-param>
							</xsl:call-template>
						</xsl:for-each>
					</table>
				</td>
				<td class="wzb-form-control">
					<table>
						<tr>
							<td width="60%" align="left" valign="bottom" class="wzb-before"><xsl:value-of select="$lab_818"/></td>
						</tr>
						<xsl:for-each select="//credit/auto_cty_lst/cty[@relation_type='COS']">
							<xsl:call-template name="draw_credit_type">
								<xsl:with-param name="left">60%</xsl:with-param>
								<xsl:with-param name="right">40%</xsl:with-param>
							</xsl:call-template>
						</xsl:for-each>
					</table>
				</td>
			</tr>
			<tr>
				<td width="50%" height="10"></td>
				<td width="50%" height="10"></td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_329"/>
				<xsl:with-param name="wb_gen_btn_href">Javascript:credit.upd_autobonus(document.frmXml)</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_330"/>
				<xsl:with-param name="wb_gen_btn_href">Javascript:credit.get_manage_page()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_credit_type">
		<xsl:param name="left"></xsl:param>
		<xsl:param name="right"></xsl:param>
		<xsl:variable name="label_name">
			<xsl:call-template name="get_cty_name">
				<xsl:with-param name="cty_code"><xsl:value-of select="code"/></xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="cty_id"><xsl:value-of select="concat('id_', lower_code)"/></xsl:variable>
		<xsl:variable name="cty_score"><xsl:value-of select="concat('score_', lower_code)"/></xsl:variable>
		<xsl:variable name="cty_hit"><xsl:value-of select="concat('hit_', lower_code)"/></xsl:variable>
		<xsl:variable name="wb_gen_num_input_length">5</xsl:variable>
		<tr>
			<td class="wzb-form-label">
				<xsl:value-of select="$label_name"/>：
			</td>
			<td class="wzb-form-control" align="left">
				<input class="wzb-inputText" size="{$wb_gen_num_input_size}" name="{$cty_score}" type="text" maxlength="{$wb_gen_num_input_length}" value="{default_credits}" label="{$label_name}"/>
				<xsl:if test="period!=''">
					<xsl:choose>
						<xsl:when test="period='DAY'">
							&#160;<xsl:value-of select="$lab_814"/>&#160;<input class="wzb-inputText" size="{$wb_gen_num_input_size}" name="{$cty_hit}" type="text" maxlength="{$wb_gen_num_input_length}" value="{hit}" label="{$lab_816}"/>&#160;<xsl:value-of select="$lab_816"/>						
						</xsl:when>
						<xsl:when test="period='MONTH'">
							&#160;<xsl:value-of select="$lab_815"/>&#160;<input class="wzb-inputText" size="{$wb_gen_num_input_size}" name="{$cty_hit}" type="text" maxlength="{$wb_gen_num_input_length}" value="{hit}" label="{$lab_816}" />&#160;<xsl:value-of select="$lab_816"/>				
						</xsl:when>
					</xsl:choose>	
				</xsl:if>
			</td>
			<input type="hidden" name="{$cty_id}" value="{@id}"/>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>

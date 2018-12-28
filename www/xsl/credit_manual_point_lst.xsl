<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>	
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="share/lab_credit_type.xsl"/>
	<!-- cust utils -->
	<!-- -->
	<xsl:output indent="yes"/>
	<xsl:variable name="deduction_ind" select="/credit/manual_cyt_lst/@deduction_ind"/>
	<xsl:variable name="sort_order" select="/credit/sort/@sort_order"/>
	<xsl:variable name="cty_cnt" select="count(/credit/manual_cyt_lst/manual_cyt)"/>
	
	<xsl:variable name="lab_809" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '809')"/>
	<xsl:variable name="lab_811" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '811')"/>
	<xsl:variable name="lab_812" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '812')"/>
	<xsl:variable name="lab_837" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '837')"/>
	<xsl:variable name="lab_838" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '838')"/>
	<xsl:variable name="lab_839" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '839')"/>
	<xsl:variable name="lab_473" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/>
	<xsl:variable name="lab_257" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/>
	<xsl:variable name="lab_703" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '703')"/>
	<xsl:variable name="lab_40" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_bdm.label_core_basic_data_management_40')"/>
	<xsl:variable name="lab_set_deducted_points" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_set_deducted_points')"/>
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_credit.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[
			var credit = new wbCredit;
			function status() {
				credit.add_manual_point(document.frmSearch, ']]><xsl:value-of select="$deduction_ind"/><![CDATA[', ']]><xsl:value-of select="$wb_lang"/><![CDATA[');
				return false;
			}
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0" onload="document.frmSearch.cty_code.focus()">
			<form name="frmSearch" onsubmit="return status()">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="cty_deduction_ind"/>
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$deduction_ind = 'true'">
						<xsl:value-of select="$lab_812"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_811"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:wb_utils_nav_go('CREDIT_OTHER_MAIN');" class="NavLink">
					<xsl:value-of select="$lab_809"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;
					<xsl:choose>
						<xsl:when test="$deduction_ind = 'true'">
							<xsl:value-of select="$lab_812"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_811"/>
						</xsl:otherwise>
					</xsl:choose>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<table>
			<tr>
				<xsl:variable name="_code">
					<xsl:choose>
						<xsl:when test="$deduction_ind='true' "><xsl:value-of select="$lab_838"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_837"/></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<td align="right" nowrap="nowrap" widht="10%">
					<xsl:value-of select="$_code"/>：&#160;
				</td>
				<td align="left" widht="20%" >
					<input type="text" size="50" name="cty_code"  class="wzb-inputText"/>
					<input type="hidden" name="lab_cty_code" value="{$_code}"/>
				</td>
				<td width="70%" class="padding-left10">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_473"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:credit.add_manual_point(frmSearch, '<xsl:value-of select="$deduction_ind"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
						<xsl:with-param name="class">btn wzb-btn-blue margin-bottom1</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="$cty_cnt=0">
				<xsl:choose>
						<xsl:when test="$deduction_ind='true' ">
							<xsl:call-template name="wb_ui_show_no_item">
								<xsl:with-param name="text" select="$lab_set_deducted_points"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_ui_show_no_item">
								<xsl:with-param name="text" select="$lab_839"/>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<div class="margin-top28"></div>
				<table class="tabel wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="50%">
							<xsl:choose>
								<xsl:when test="$deduction_ind = 'true'">
									<a href="javascript: credit.set_manual_point('true','{$sort_order}');" >
									<xsl:value-of select="$lab_703"/>
									<xsl:choose>
										<xsl:when test="$sort_order='desc'">
											<i class="fa fa-caret-down skin-color"></i>
										</xsl:when>
										<xsl:otherwise>
											<i class="fa skin-color fa-caret-up"></i>
										</xsl:otherwise>
									</xsl:choose>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript: credit.set_manual_point('false','{$sort_order}');" >
									<xsl:value-of select="$lab_703"/>
									<xsl:choose>
										<xsl:when test="$sort_order='desc'">
											<i class="fa fa-caret-down skin-color"></i>
										</xsl:when>
										<xsl:otherwise>
											<i class="fa skin-color fa-caret-up"></i>
										</xsl:otherwise>
									</xsl:choose>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:value-of select="$lab_40"/>
						</td>
					</tr>
					<xsl:apply-templates select="manual_cyt_lst/manual_cyt"/>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template match="manual_cyt">
		<tr>
			<td>
				<xsl:value-of select="code"/>
			</td>
			<td valign="top" nowrap="nowrap">
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$lab_257"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:credit.del_manual_point(<xsl:value-of select="@id"/>,'<xsl:value-of select="$deduction_ind"/>')</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- ================================================================ -->
</xsl:stylesheet>

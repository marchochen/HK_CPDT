<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/credit">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:variable name="lab_809" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '809')"/>
	<xsl:variable name="lab_810" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '810')"/>
	<xsl:variable name="lab_811" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '811')"/>
	<xsl:variable name="lab_812" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '812')"/>
	<xsl:variable name="lab_813" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '813')"/>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_credit.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var credit = new wbCredit;
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_809"/>
			</xsl:with-param>
		</xsl:call-template>
			<div class="jifenguanli">
			
			 
			<!-- 
			<xsl:if test="credit_setting_main='true'">
				<xsl:call-template name="ADM_CREDIT_MAIN"/>
			</xsl:if>
			
			<xsl:call-template name="TADM_CREDIT_MAIN"/>
			 -->
			<!-- 
			<xsl:if test="credit_other_main='true'">
				<xsl:call-template name="TADM_CREDIT_MAIN"/>			
			</xsl:if> 
			-->
 			<a class="zidongjf" href="JavaScript:credit.get_auto_credit()">
						<xsl:value-of select="$lab_810"/>
			</a>
			<a class="shougongjf" href="javascript: credit.set_manual_point('false','desc');">
				<xsl:value-of select="$lab_811"/>
			</a>
			<a class="shougongkf" href="javascript: credit.set_manual_point('true');">
				<xsl:value-of select="$lab_812"/>
			</a>
			<a class="xueyuanjf" href="javascript: credit.set_learner_point();">
				<xsl:value-of select="$lab_813"/>
			</a>
			</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="ADM_CREDIT_MAIN">
		
			<a class="zidongjf" href="JavaScript:credit.get_auto_credit()">
						<xsl:value-of select="$lab_810"/>
			</a>

	</xsl:template>
	<xsl:template  name="TADM_CREDIT_MAIN">

					<a class="shougongjf" href="javascript: credit.set_manual_point('false','desc');">
						<xsl:value-of select="$lab_811"/>
					</a>
					<a class="shougongkf" href="javascript: credit.set_manual_point('true');">
						<xsl:value-of select="$lab_812"/>
					</a>
	
					<a class="xueyuanjf" href="javascript: credit.set_learner_point();">
						<xsl:value-of select="$lab_813"/>
					</a>
	</xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="module_id" select="module/@id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			course_lst = new wbCourse;				
			function resize_frame(){
				if(document.all){
					if(parent && parent.window && parent.window.resize_fs){
						parent.window.resize_fs(document.all['bottom_img'].offsetTop +5 );	
					}
				}else if(document.getElementById != null){
					if(parent && parent.window && parent.window.resize_fs){
						parent.window.resize_fs(document.getElementById('bottom_img').offsetTop +5 );	
					}			
				}
			}
			]]></SCRIPT>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<base target="_parent"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onLoad="resize_frame()">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_evn_builder">課程評估制作-题目管理</xsl:with-param>
			<xsl:with-param name="lab_evn_builder">公共調查問卷-題目管理</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_evn_builder">课程评估创建-题目管理</xsl:with-param>
			<xsl:with-param name="lab_evn_builder">公共调查问卷-题目管理</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_evn_builder">Course evaluation builder</xsl:with-param>
			<xsl:with-param name="lab_evn_builder">Survey builder</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_course_evn_builder"/>
		<xsl:param name="lab_evn_builder"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text_class" >pop-up-title</xsl:with-param>
			<xsl:with-param name="text">
				<xsl:if test="header/@subtype = 'SVY'">
					<xsl:value-of select="$lab_course_evn_builder"/>&#160;</xsl:if>
				<xsl:if test="header/@subtype = 'EVN'">
					<xsl:value-of select="$lab_evn_builder"/>&#160;</xsl:if>
				<!-- <xsl:value-of select="header/title/text()"/> -->
			</xsl:with-param>
		</xsl:call-template>
		<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0" id="bottom_img"/>
		<xsl:call-template name="wb_ui_line"/>
	</xsl:template>
</xsl:stylesheet>

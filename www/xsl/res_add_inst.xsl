<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="user"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="user">
		<head>
			<title>
				<xsl:value-of select="wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
		</head>
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst_gen">左側列表中顯示的當前文件夹的教材。點擊鏈結可以查看資源的詳細資訊。點擊 <b>添加</b> 可以將對應的資源複製到您的課程內容中。</xsl:with-param>
			<xsl:with-param name="lab_inst_aicc">左側列表中顯示的當前文件夹的AICC課件。點擊鏈結可以查看資源的詳細資訊。點擊 <b>添加</b> 可以將對應的資源加入到您的課程內容中。</xsl:with-param>
			<xsl:with-param name="lab_inst_test">左側列表中顯示的當前文件夹的測驗。點擊鏈結可以查看資源的詳細資訊。點擊 <b>添加</b> 可以將對應的測驗加入到您的課程內容中。</xsl:with-param>
			<xsl:with-param name="lab_inst_scorm">左側列表中顯示的當前文件夹的SCORM課件。點擊鏈結可以查看資源的詳細資訊。點擊 <b>添加</b> 可以將對應的資源加入到您的課程內容中。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst_gen">左侧列表中显示的当前文件夹下的学习资源。点击链接可以查看资源的详细信息。点击 <b>添加</b> 可以将对应的资源复制到您的课程内容中。</xsl:with-param>
			<xsl:with-param name="lab_inst_aicc">左侧列表中显示的当前文件夹下的AICC课件。点击链接可以查看资源的详细信息。点击 <b>添加</b> 可以将对应的资源加入到您的课程内容中。</xsl:with-param>
			<xsl:with-param name="lab_inst_test">左侧列表中显示的当前文件夹下的测验。点击链接可以查看资源的详细信息。点击 <b>添加</b> 可以将对应的测验加入到您的课程内容中。</xsl:with-param>
			<xsl:with-param name="lab_inst_scorm">左侧列表中显示的当前文件夹下的SCORM课件。点击链接可以查看资源的详细信息。点击 <b>添加</b> 可以将对应的资源加入到您的课程内容中。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst_gen">Listed on the left are the learning materials available in this folder. You can view the details by clicking the links. Click <b>Add</b> to add a copy of the learning material to your course content.</xsl:with-param>
			<xsl:with-param name="lab_inst_aicc">Listed on the left are the AICC coursewares available in this folder. You can view the details by clicking the links. Click <b>Add</b> to add the courseware to your course content.</xsl:with-param>
			<xsl:with-param name="lab_inst_test">Listed on the left are the tests available in this folder. You can view the details by clicking the links. Click <b>Add</b> to add the test to your course content.</xsl:with-param>
			<xsl:with-param name="lab_inst_scorm">Listed on the left are the SCORM coursewares available in this folder. You can view the details by clicking the links. Click <b>Add</b> to add the courseware to your course content.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_inst_gen"/>
		<xsl:param name="lab_inst_aicc"/>
		<xsl:param name="lab_inst_test"/>
		<xsl:param name="lab_inst_scorm"/>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="wb_ui_line"/>
			<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="10" class="Bg">
				<tr>
					<td height="10">
						<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					</td>
				</tr>
				<tr>
					<td>
						<div class="wzb-ui-module-text">
							<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							if(parent.parent.hidden && parent.parent.hidden.document.frmXml.res_type.value == 'GEN'){
								document.write(']]><xsl:copy-of select="$lab_inst_gen"/><![CDATA[');
							}else if(parent.parent.hidden && parent.parent.hidden.document.frmXml.res_type.value == 'AICC'){
								document.write(']]><xsl:copy-of select="$lab_inst_aicc"/><![CDATA[');
							}else if(parent.parent.hidden && parent.parent.hidden.document.frmXml.res_type.value == 'ASM'){
								document.write(']]><xsl:copy-of select="$lab_inst_test"/><![CDATA[');
							}
							else if(parent.parent.hidden && parent.parent.hidden.document.frmXml.res_type.value == 'SCORM'){
								document.write(']]><xsl:copy-of select="$lab_inst_scorm"/><![CDATA[');
							}
						]]></script>
						</div>
					</td>
				</tr>
				<tr>
					<td height="10">
						<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					</td>
				</tr>
			</table>
			<xsl:call-template name="wb_ui_line"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

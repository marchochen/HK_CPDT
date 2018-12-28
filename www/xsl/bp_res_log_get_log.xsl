<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="cur_page" select="/que_log/que_log/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/que_log/que_log/pagination/@page_size"/>
	<xsl:variable name="timestamp" select="/que_log/que_log/pagination/@timestamp"/>
	<xsl:variable name="total" select="/que_log/que_log/pagination/@total_rec"/>
	<!-- =============================================================== -->
	<xsl:template match="/que_log">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[	
			
			Batch = new wbBatchProcess
			
		]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<xsl:call-template name="content"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:variable name="lab_history" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_158')"/>
	<xsl:variable name="lab_res_type" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_159')"/>
	<xsl:variable name="lab_no_log" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_160')"/>
	<xsl:variable name="lab_date" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_161')"/>
	<xsl:variable name="lab_process" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_162')"/>
	<xsl:variable name="lab_file" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_163')"/>
	<xsl:variable name="lab_description" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_164')"/>
	<xsl:variable name="lab_done_by" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_165')"/>
	<xsl:variable name="lab_log_files" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_166')"/>
	<xsl:variable name="lab_import" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_167')"/>
	<xsl:variable name="lab_export" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_168')"/>
	<xsl:variable name="lab_g_form_btn_back" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_169')"/>
	<xsl:variable name="lab_mc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_170')"/>
	<xsl:variable name="lab_mt" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_171')"/>
	<xsl:variable name="lab_fb" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_172')"/>
	<xsl:variable name="lab_tf" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_173')"/>
	<xsl:variable name="lab_es" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_174')"/>
	<xsl:variable name="lab_fsc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_175')"/>
	<xsl:variable name="lab_dsc" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_176')"/>
	<xsl:variable name="lab_bp_desc">
		<xsl:value-of select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_177')"/>
		<br/>
		<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>
		<xsl:value-of select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_178')"/>
		<br/>
		<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>
		<xsl:value-of select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_179')"/>
		<br/>
	</xsl:variable>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
	
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_history"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_bp_desc"/>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(que_log/record) &gt;=1">
				<table class="wzb-ui-table">
					<tr>
						<td width="15%" align="left">
							<xsl:value-of select="$lab_date"/>
						</td>
						<td width="8%">
							<xsl:value-of select="$lab_process"/>
						</td>
						<td width="27%" align="left">
							<xsl:value-of select="$lab_file"/>
						</td>
						<td width="10%" align="left">
							<xsl:value-of select="$lab_res_type"/>
						</td>
						<td width="15%" align="left">
							<xsl:value-of select="$lab_description"/>
						</td>
						<td width="10%" align="left">
							<xsl:value-of select="$lab_done_by"/>
						</td>
						<td width="15%" align="left">
							<xsl:value-of select="$lab_log_files"/>
						</td>
					</tr>
					<xsl:for-each select="que_log/record">
						<tr>
							<td align="left">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp" select="@timestamp"/>
									<xsl:with-param name="dis_time">T</xsl:with-param>
								</xsl:call-template>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="@process = 'IMPORT'">
										<xsl:value-of select="$lab_import"/>
									</xsl:when>
									<xsl:when test="@process = 'EXPORT'">
										<xsl:value-of select="$lab_export"/>
									</xsl:when>
								</xsl:choose>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="uploaded_file/uri != ''">
										<a href="..{uploaded_file/uri}" target="_blank">
											<xsl:value-of select="uploaded_file/filename"/>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="uploaded_file/filename"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td align="left">
								<xsl:choose>
									<xsl:when test="@subtype='MC'"><xsl:value-of select="$lab_mc"/></xsl:when>
									<xsl:when test="@subtype='FB'"><xsl:value-of select="$lab_fb"/></xsl:when>
									<xsl:when test="@subtype='MT'"><xsl:value-of select="$lab_mt"/></xsl:when>
									<xsl:when test="@subtype='TF'"><xsl:value-of select="$lab_tf"/></xsl:when>
									<xsl:when test="@subtype='ES'"><xsl:value-of select="$lab_es"/></xsl:when>
									<xsl:when test="@subtype='FSC'"><xsl:value-of select="$lab_fsc"/></xsl:when>
									<xsl:when test="@subtype='DSC'"><xsl:value-of select="$lab_dsc"/></xsl:when>
								</xsl:choose>
							</td>
							<td align="left">
								<xsl:value-of select="desc"/>
							</td>
							<td align="left">
								<xsl:value-of select="@performer"/>
							</td>
							<td align="left">
								<xsl:if test="log_file_list/log_file[@type = 'SUCCESS']/uri != ''">
									<a href="..{log_file_list/log_file[@type = 'SUCCESS']/uri}" target="_blank">
										<xsl:value-of select="log_file_list/log_file[@type = 'SUCCESS']/filename"/>
									</a>
									<xsl:if test="log_file_list/log_file[@type = 'UNSUCCESS']/uri != '' or log_file_list/log_file[@type = 'ERROR']/uri != '' ">&#160;,&#160;</xsl:if>
								</xsl:if>
								<xsl:if test="log_file_list/log_file[@type = 'UNSUCCESS']/uri != ''">
									<a href="..{log_file_list/log_file[@type = 'UNSUCCESS']/uri}" target="_blank">
										<xsl:value-of select="log_file_list/log_file[@type = 'UNSUCCESS']/filename"/>
									</a>
									<xsl:if test="log_file_list/log_file[@type = 'ERROR']/uri != '' ">&#160;,&#160;</xsl:if>
								</xsl:if>
								<xsl:if test="log_file_list/log_file[@type = 'ERROR']/uri != ''">
									<a href="..{log_file_list/log_file[@type = 'ERROR']/uri}" target="_blank">
										<xsl:value-of select="log_file_list/log_file[@type = 'ERROR']/filename"/>
									</a>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_log"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<xsl:call-template name="wb_ui_pagination">
				<xsl:with-param name="cur_page" select="$cur_page"/>
				<xsl:with-param name="page_size" select="$page_size"/>
				<xsl:with-param name="timestamp" select="$timestamp"/>
				<xsl:with-param name="total" select="$total"/>
				<xsl:with-param name="width">
					<xsl:value-of select="$wb_gen_table_width"/>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Import.prep_page()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>

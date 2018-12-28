<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
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
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="cur_page" select="/ims_log/ims_log/pagination/@cur_page"/>
	<xsl:variable name="page_size" select="/ims_log/ims_log/pagination/@page_size"/>
	<xsl:variable name="timestamp" select="/ims_log/ims_log/pagination/@timestamp"/>
	<xsl:variable name="total" select="/ims_log/ims_log/pagination/@total_rec"/>  
	
	
	
	<!-- =============================================================== -->
	<xsl:template match="/ims_log">
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_credit.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[	
			
			var credit = new wbCredit;
			
		]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_bp">用戶資訊匯入記錄</xsl:with-param>
			<xsl:with-param name="lab_no_log">沒有可用的記錄</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_process">過程</xsl:with-param>
			<xsl:with-param name="lab_file">文件</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_done_by">作者</xsl:with-param>
			<xsl:with-param name="lab_import">匯入</xsl:with-param>
			<xsl:with-param name="lab_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_bp">积分信息导入记录</xsl:with-param>
			<xsl:with-param name="lab_no_log">没有可用的记录</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_process">过程</xsl:with-param>
			<xsl:with-param name="lab_file">文件</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_done_by">作者</xsl:with-param>
			<xsl:with-param name="lab_import">导入</xsl:with-param>
			<xsl:with-param name="lab_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">后退</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_bp">Credit import history</xsl:with-param>
			<xsl:with-param name="lab_no_log">No records found</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_process">Process</xsl:with-param>
			<xsl:with-param name="lab_file">File</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_done_by">Done by</xsl:with-param>
			<xsl:with-param name="lab_import">Import</xsl:with-param>
			<xsl:with-param name="lab_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_bp"/>
		<xsl:param name="lab_no_log"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_process"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_done_by"/>
		<xsl:param name="lab_import"/>
		<xsl:param name="lab_export"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_CREDIT_SETTING_MAIN</xsl:with-param>
		</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_bp"/>
			</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:credit.import_prep()" class="NavLink">
					<xsl:call-template name="get_lab">
						<xsl:with-param name="lab_title">lab_import_credit_info</xsl:with-param>
					</xsl:call-template>
				</a>
				<span class="NavLink">&#160;&gt;&#160;
					<xsl:value-of select="$lab_bp"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>	
		<xsl:choose>
			<xsl:when test="count(ims_log/record) &gt;=1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:value-of select="$lab_date"/>
						</td>
						<td nowrap="nowrap">
							<xsl:value-of select="$lab_process"/>
						</td>
						<td>
							<xsl:value-of select="$lab_file"/>
						</td>
						<td>
							<xsl:value-of select="$lab_description"/>
						</td>
						<td>
							<xsl:value-of select="$lab_done_by"/>
						</td>
					</tr>
					<xsl:for-each select="ims_log/record">
						<tr>
							<td>
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp" select="@timestamp"/>
									<xsl:with-param name="dis_time">T</xsl:with-param>
								</xsl:call-template>
							</td>
							<td>
								<xsl:choose>
									<xsl:when test="@process = 'IMPORT'">
										<xsl:value-of select="$lab_import"/>
									</xsl:when>
									<xsl:when test="@process = 'EXPORT'">
										<xsl:value-of select="$lab_export"/>
									</xsl:when>
								</xsl:choose>
							</td>
							<td>
								<xsl:choose>
									<xsl:when test="uploaded_file/uri != ''">
										<a class="Text" href="..{uploaded_file/uri}" target="_blank"><xsl:value-of select="uploaded_file/filename"/></a>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="uploaded_file/filename"/>
									</xsl:otherwise>
								</xsl:choose>
							</td>
							<td>
								<xsl:value-of select="desc"/>
							</td>
							<td>
								<xsl:value-of select="@performer"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
				</xsl:call-template>				
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_log"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:credit.import_prep()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>

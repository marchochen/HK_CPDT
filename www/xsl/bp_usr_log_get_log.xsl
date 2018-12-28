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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[	
			
			Batch = new wbBatchProcess
			
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
			<xsl:with-param name="lab_bp">用戶資訊匯入和匯出記錄</xsl:with-param>
			<xsl:with-param name="lab_bp_desc">可能的記錄檔：<br/><img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>success.txt - 顯示成功匯入的記錄的細節<br/><img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>failure.txt - 顯示未成功匯入的記錄的細節<br/></xsl:with-param>
			<xsl:with-param name="lab_no_log">沒有可用的記錄</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_process">過程</xsl:with-param>
			<xsl:with-param name="lab_file">文件</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_done_by">作者</xsl:with-param>
			<xsl:with-param name="lab_log_files">記錄檔</xsl:with-param>
			<xsl:with-param name="lab_import">匯入</xsl:with-param>
			<xsl:with-param name="lab_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="success_msg">顯示成功匯入的記錄的細節</xsl:with-param>
			<xsl:with-param name="failue_msg"> 顯示未成功匯入的記錄的細節</xsl:with-param>
			<xsl:with-param name="err_msg">顯示阻礙操作完成的錯誤</xsl:with-param>
			<xsl:with-param name="view_log">顯示阻礙操作完成的錯誤</xsl:with-param>
			<xsl:with-param name="view_log">查看記錄</xsl:with-param>
			<xsl:with-param name="lab_title">導入用戶資訊</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_bp">用户信息导入和导出记录</xsl:with-param>
			<xsl:with-param name="lab_bp_desc">可能的记录文件：<br/><img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>success.txt - 显示成功导入的记录的细节<br/><img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>failure.txt - 显示未成功导入的记录的细节<br/></xsl:with-param>
			<xsl:with-param name="lab_no_log">没有可用的记录</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_process">过程</xsl:with-param>
			<xsl:with-param name="lab_file">文件</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_done_by">作者</xsl:with-param>
			<xsl:with-param name="lab_log_files">记录文件</xsl:with-param>
			<xsl:with-param name="lab_import">导入</xsl:with-param>
			<xsl:with-param name="lab_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">后退</xsl:with-param>
			<xsl:with-param name="success_msg">显示成功导入的记录的细节</xsl:with-param>
			<xsl:with-param name="failue_msg">显示未成功导入的记录的细节</xsl:with-param>
			<xsl:with-param name="err_msg">显示阻碍操作完成的错误</xsl:with-param>
			<xsl:with-param name="view_log">查看记录</xsl:with-param>
			<xsl:with-param name="lab_title">导入用户信息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_bp">User profile import history</xsl:with-param>
			<xsl:with-param name="lab_bp_desc">Possible log files:<br/><img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>success.txt - show details of records that are successfully imported<br/><img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>failure.txt - show details of records that are failed to be imported<br/></xsl:with-param>
			<xsl:with-param name="lab_no_log">No records found</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_process">Process</xsl:with-param>
			<xsl:with-param name="lab_file">File</xsl:with-param>
			<xsl:with-param name="lab_description">Description</xsl:with-param>
			<xsl:with-param name="lab_done_by">Done by</xsl:with-param>
			<xsl:with-param name="lab_log_files">Log files</xsl:with-param>
			<xsl:with-param name="lab_import">Import</xsl:with-param>
			<xsl:with-param name="lab_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="success_msg">show details of records that are successfully imported</xsl:with-param>
			<xsl:with-param name="failue_msg">show details of records that are failed to be imported</xsl:with-param>
			<xsl:with-param name="err_msg">show errors that prevent the process to complete</xsl:with-param>
			<xsl:with-param name="view_log">View history</xsl:with-param>
			<xsl:with-param name="lab_title">Import user information</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_bp"/>
		<xsl:param name="lab_bp_desc"/>
		<xsl:param name="lab_no_log"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_process"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_description"/>
		<xsl:param name="lab_done_by"/>
		<xsl:param name="lab_log_files"/>
		<xsl:param name="lab_import"/>
		<xsl:param name="lab_export"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="success_msg"/>
		<xsl:param name="failue_msg"/>
		<xsl:param name="err_msg"/>
		<xsl:param name="view_log"/>
		<xsl:param name="lab_title"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_bp"/>
		</xsl:call-template>
			
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
						<a href="javascript:Batch.User.Import.prep()" >
							<xsl:value-of select="$lab_title"/>
						</a>
				</span>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<span class="NavLink">
					<xsl:value-of select="$view_log"/>
			    </span>
			</xsl:with-param>
	  </xsl:call-template>
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_bp_desc"/>
			</xsl:call-template>		
		<xsl:choose>
			<xsl:when test="count(ims_log/record) &gt;=1">
				<table class="tabel wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="15%">
							<xsl:value-of select="$lab_date"/>
						</td>
						<td width="10%" style="padding-right:-20px;">
							<xsl:value-of select="$lab_process"/>
						</td>
						<td width="25%" align="left" >
							<xsl:value-of select="$lab_file"/>
						</td>
						<td width="27%" align="left"  >
							<xsl:value-of select="$lab_description"/>
						</td>
						<td width="15%"  align="left">
							<xsl:value-of select="$lab_done_by"/>
						</td>
						<td width="8%" align="right" >
							<xsl:value-of select="$lab_log_files"/>
						</td>
					</tr>
					<xsl:for-each select="ims_log/record">
						<tr>
							<td  style="width:10%">
								<xsl:call-template name="display_time">
									<xsl:with-param name="my_timestamp" select="@timestamp"/>
									<xsl:with-param name="dis_time">T</xsl:with-param>
								</xsl:call-template>
							</td>
							<td style="algin:left;">
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
							<td>
								<xsl:if test="log_file_list/log_file[@type = 'SUCCESS']/uri != ''">
									<a href="..{log_file_list/log_file[@type = 'SUCCESS']/uri}" target="_blank" title="{$success_msg}"><xsl:value-of select="log_file_list/log_file[@type = 'SUCCESS']/filename"/></a>
									<xsl:if test="log_file_list/log_file[@type = 'UNSUCCESS']/uri != '' or log_file_list/log_file[@type = 'ERROR']/uri != '' ">&#160;,&#160;</xsl:if>
								</xsl:if>
								<xsl:if test="log_file_list/log_file[@type = 'UNSUCCESS']/uri != ''">
									<a href="..{log_file_list/log_file[@type = 'UNSUCCESS']/uri}" target="_blank" title="{$failue_msg}"><xsl:value-of select="log_file_list/log_file[@type = 'UNSUCCESS']/filename"/></a>
									<!-- <xsl:if test="log_file_list/log_file[@type = 'ERROR']/uri != '' ">&#160;,&#160;</xsl:if> -->
								</xsl:if>
								<!-- <xsl:if test="log_file_list/log_file[@type = 'ERROR']/uri != ''">
									<a href="..{log_file_list/log_file[@type = 'ERROR']/uri}" target="_blank" title="{$err_msg}"><xsl:value-of select="log_file_list/log_file[@type = 'ERROR']/filename"/></a>
								</xsl:if> -->
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
		<div class=""> 
			<xsl:call-template name="wb_ui_pagination">
				<xsl:with-param name="cur_page" select="$cur_page"/>
				<xsl:with-param name="page_size" select="$page_size"/>
				<xsl:with-param name="timestamp" select="$timestamp"/>
				<xsl:with-param name="total" select="$total"/>
				<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>

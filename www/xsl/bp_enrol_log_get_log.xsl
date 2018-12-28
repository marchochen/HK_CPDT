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
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[	
			
			Batch = new wbBatchProcess
			app = new wbApplication
			itm_lst = new wbItem
			
		]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
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
			<xsl:with-param name="lab_bp">匯入登記記錄和完成結果記錄</xsl:with-param>
			<xsl:with-param name="lab_bp_desc">
			可能的記錄檔：<br/>
<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>success.txt–顯示成功匯入的記錄的細節<br/>
<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>failure.txt–顯示未成功匯入的記錄的細節<br/></xsl:with-param>
			<xsl:with-param name="lab_no_log">沒有可用的記錄</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_process">過程</xsl:with-param>
			<xsl:with-param name="lab_file">文件</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_done_by">作者</xsl:with-param>
			<xsl:with-param name="lab_log_files">記錄</xsl:with-param>
			<xsl:with-param name="lab_import">匯入</xsl:with-param>
			<xsl:with-param name="lab_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>	
			<xsl:with-param name="lab_view_history">查看記錄</xsl:with-param>
				<xsl:with-param name="lab_enrollment_approval">處理报名</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">匯入報名記錄</xsl:with-param>
			<xsl:with-param name="lab_run_info">班級資訊</xsl:with-param>		
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_bp">导入登记记录和完成结果记录</xsl:with-param>
			<xsl:with-param name="lab_bp_desc">
			可能的记录文件：<br/>
<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>success.txt–显示成功导入的记录的细节<br/>
<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>failure.txt–显示未成功导入的记录的细节<br/></xsl:with-param>
			<xsl:with-param name="lab_no_log">没有可用的记录</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_process">过程</xsl:with-param>
			<xsl:with-param name="lab_file">文件</xsl:with-param>
			<xsl:with-param name="lab_description">描述</xsl:with-param>
			<xsl:with-param name="lab_done_by">作者</xsl:with-param>
			<xsl:with-param name="lab_log_files">记录</xsl:with-param>
			<xsl:with-param name="lab_import">导入</xsl:with-param>
			<xsl:with-param name="lab_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">后退</xsl:with-param>	
			<xsl:with-param name="lab_view_history">查看记录</xsl:with-param>	
			<xsl:with-param name="lab_enrollment_approval">处理报名</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">导入报名记录</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_bp">Enrollment and completion result import history</xsl:with-param>
			<xsl:with-param name="lab_bp_desc">
			Possible log files:<br/>
<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>success.txt - show details of records that are successfully imported<br/>
<img src="{$wb_img_path}tp.gif" width="10" height="1" border="0"/>failure.txt - show details of records that are failed to be imported<br/></xsl:with-param>
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
			<xsl:with-param name="lab_view_history">View history</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">Process enrollment</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">Import enrollment</xsl:with-param>
			<xsl:with-param name="lab_run_info">Class information</xsl:with-param>

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
		<xsl:param name="lab_enrollment_approval"/>
		<xsl:param name="lab_enrollment_upload"/>
		<xsl:param name="lab_view_history"/>
		<xsl:param name="lab_run_info"/>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">111</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:value-of select="//itm_action_nav/@itm_title"/>
				</xsl:with-param>
			</xsl:call-template>
			<!--============================================================================-->
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="item/@run_ind='true'">
						<a href="Javascript:itm_lst.get_item_detail({item/parent/@id})" class="NavLink">
							<xsl:value-of select="item/parent/@title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_list({item/parent/@id})" class="NavLink">
							<xsl:choose>
								<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
							</xsl:choose>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_detail({item/@id})" class="NavLink">
							<xsl:value-of select="item/@title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="Javascript:itm_lst.get_item_detail({item/@id})" class="NavLink">
							<xsl:value-of select="item/@title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
					<a href="Javascript:app.get_application_list('',{item/@id})" class="NavLink">
					<xsl:value-of select="$lab_enrollment_approval"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="Javascript:Batch.Enrol.Import.prep('{item/@id}')" class="NavLink">
					<xsl:value-of select="$lab_enrollment_upload"/>
				</a>

				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:value-of select="$lab_view_history"/>
			</xsl:with-param>
		</xsl:call-template>
		<!--============================================================================-->
		
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_bp_desc"/>
			</xsl:call-template>		
		<xsl:choose>
			<xsl:when test="count(ims_log/record) &gt;=1">
				<table class="table wzb-ui-table margin-top28" border="0" cellspacing="0" cellpadding="3" width="{$wb_gen_table_width}">
					<tr class="wzb-ui-table-head">
						<td width="4">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_date"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_process"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_file"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_description"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_done_by"/>
							</span>
						</td>
						<td>
							<span class="TitleText">
								<xsl:value-of select="$lab_log_files"/>
							</span>
						</td>
						<td width="4">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:for-each select="ims_log/record">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
							<td>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<td>
								<span class="Text">
									<xsl:call-template name="display_time">
										<xsl:with-param name="my_timestamp" select="@timestamp"/>
										<xsl:with-param name="dis_time">T</xsl:with-param>
									</xsl:call-template>
								</span>
							</td>
							<td>
								<span class="Text">
									<xsl:choose>
										<xsl:when test="@process = 'IMPORT'">
											<xsl:value-of select="$lab_import"/>
										</xsl:when>
										<xsl:when test="@process = 'EXPORT'">
											<xsl:value-of select="$lab_export"/>
										</xsl:when>
									</xsl:choose>
								</span>
							</td>
							<td>
								<span class="Text">
									<xsl:choose>
										<xsl:when test="uploaded_file/uri != ''">
											<a class="Text" href="..{uploaded_file/uri}" target="_blank"><xsl:value-of select="uploaded_file/filename"/></a>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="uploaded_file/filename"/>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
							<td>
								<span class="Text">
									<xsl:value-of select="desc"/>
								</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<td>
								<span class="Text">
									<xsl:value-of select="@performer"/>
								</span>
							</td>
							<td>
								<span class="Text">
									<xsl:if test="log_file_list/log_file[@type = 'SUCCESS']/uri != ''">
										<a class="Text" href="..{log_file_list/log_file[@type = 'SUCCESS']/uri}" target="_blank"><xsl:value-of select="log_file_list/log_file[@type = 'SUCCESS']/filename"/></a>
										<xsl:if test="log_file_list/log_file[@type = 'UNSUCCESS']/uri != '' or log_file_list/log_file[@type = 'ERROR']/uri != '' ">&#160;,&#160;</xsl:if>
									</xsl:if>
									<xsl:if test="log_file_list/log_file[@type = 'UNSUCCESS']/uri != ''">
										<a class="Text" href="..{log_file_list/log_file[@type = 'UNSUCCESS']/uri}" target="_blank"><xsl:value-of select="log_file_list/log_file[@type = 'UNSUCCESS']/filename"/></a>
										<!-- <xsl:if test="log_file_list/log_file[@type = 'ERROR']/uri != '' ">&#160;,&#160;</xsl:if> -->
									</xsl:if>
									<!-- <xsl:if test="log_file_list/log_file[@type = 'ERROR']/uri != ''">
										<a class="Text" href="..{log_file_list/log_file[@type = 'ERROR']/uri}" target="_blank"><xsl:value-of select="log_file_list/log_file[@type = 'ERROR']/filename"/></a>
									</xsl:if> -->
								</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>
							<td>
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
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
		        <xsl:call-template name="wb_ui_line"/>
		        &#160;
				<table cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" border="0">
					<tr>
						<td align="center">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.prep('<xsl:value-of select="item/@id"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
			</div>
				<xsl:call-template name="wb_ui_footer"/>		
	</xsl:template>
</xsl:stylesheet>

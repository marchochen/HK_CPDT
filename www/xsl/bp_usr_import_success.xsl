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
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:variable name="success_entity" select="/ims_log/data_import/success_entity/total"/>
	<xsl:variable name="unsuccess_entity" select="/ims_log/data_import/unsuccess_entity/total"/>
	<xsl:output indent="yes"/>
	<xsl:strip-space elements="*"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="//data_import"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="data_import">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess;
		]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onLoad="wb_utils_init_css(document.frmXml,'text','password','textarea','file')">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
		   <xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_title">匯入用戶資訊 - 結果</xsl:with-param>
			<xsl:with-param name="lab_success_entries">成功的輸入項</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">未成功的輸入項</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">查看記錄檔</xsl:with-param>
			<xsl:with-param name="lab_failed">匯入過程失敗</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
			<xsl:with-param name="lab_upload_success1">文件已經成功上傳至後臺！</xsl:with-param>
			<xsl:with-param name="lab_get_eamil">導入完畢後你將會收到一封郵件。</xsl:with-param>
			<xsl:with-param name="lab_check_detail">如果需要查看導入詳細，請在上傳頁面點擊查看記錄。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
		   <xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_title">导入用户信息 - 结果</xsl:with-param>
			<xsl:with-param name="lab_success_entries">成功的输入项</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">未成功的输入项</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">查看记录文件</xsl:with-param>
			<xsl:with-param name="lab_failed">导入过程失败</xsl:with-param>
			<xsl:with-param name="lab_reason">原因</xsl:with-param>
			<xsl:with-param name="lab_upload_success1">文件已经成功上传至后台！</xsl:with-param>
			<xsl:with-param name="lab_get_eamil">导入完毕后你将会收到一封邮件。</xsl:with-param>
			<xsl:with-param name="lab_check_detail">如果需要查看导入详细，请在上传页面点击查看记录。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
	   <xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_title">Import user profile - result</xsl:with-param>
			<xsl:with-param name="lab_success_entries">Success entries</xsl:with-param>
			<xsl:with-param name="lab_failed_entries">Failed entries</xsl:with-param>
			<xsl:with-param name="lab_view_log_file">View log file</xsl:with-param>
			<xsl:with-param name="lab_failed">Import process failed to complete. </xsl:with-param>
			<xsl:with-param name="lab_reason">Reason</xsl:with-param>
			<xsl:with-param name="lab_upload_success1">File had been successfully uploaded to the background.</xsl:with-param>
			<xsl:with-param name="lab_get_eamil">You will receive an email when the process finished.</xsl:with-param>
			<xsl:with-param name="lab_check_detail">If you want to view detail log, please go to 'View history'.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_success_entries"/>
		<xsl:param name="lab_failed_entries"/>
		<xsl:param name="lab_view_log_file"/>
		<xsl:param name="lab_failed"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_reason"/>
		<xsl:param name="lab_upload_success1"/>
		<xsl:param name="lab_get_eamil"/>
		<xsl:param name="lab_check_detail"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_USR_INFO</xsl:with-param>
		</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_title"/>
			</xsl:call-template>		
			<!--<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_desc"/>
			</xsl:call-template>-->
			
		<xsl:call-template name="wb_ui_line"/>
		<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
			<tr >
				<td colspan="2" height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			

					<tr >
						<td colspan="2">
							<xsl:value-of select="$lab_upload_success1"/><br/><xsl:value-of select="$lab_get_eamil"/>
							<br/><xsl:value-of select="$lab_check_detail"/>
						</td>
					</tr>

			

			
			<xsl:if test="error/reason != ''">
				<tr class="wbRowRefBg">
					<td colspan="3">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td colspan="2">
						<span class="Text"><font color="red">
							<xsl:value-of select="$lab_failed"/></font>
						</span>
					</td>
				</tr>
				<tr>
					<td width="10%">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td colspan="2">
						<span class="Text"><font color="red">
							<xsl:value-of select="$lab_reason"/><xsl:text>&#160;:&#160;</xsl:text><xsl:value-of select="/ims_log/data_import/error/reason"/></font>
						</span>
					</td>
				</tr>				
			</xsl:if>
			<tr>
				<td colspan="3" height="10">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>			
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.User.Import.prep()</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>		
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
</xsl:stylesheet>

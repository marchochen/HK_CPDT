<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
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
	<xsl:import href="utils/wb_gen_input_file.xsl" />
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width">600</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
				<script language="Javascript" TYPE="text/javascript"><![CDATA[
			var attd = new wbAttendance
			Batch = new wbBatchProcess;
		]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="new_css" />
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form name="frmXml" onsubmit="return false" enctype="multipart/form-data">
					<input type="hidden" name="rename" value="NO"/>
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="stylesheet"/>
					<input type="hidden" name="itm_id"/>
					<input type="hidden" name="module" value=""/>
					<input type="hidden" name="url_success"/>
					<input type="hidden" name="url_failure"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="evalmanagement">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">檔位置</xsl:with-param>
			<xsl:with-param name="lab_export">匯出模板</xsl:with-param>
			<xsl:with-param name="lab_desc1">在下面指定包含出席訊息的文件。點擊</xsl:with-param>
			<xsl:with-param name="lab_desc2">此處</xsl:with-param>
			<xsl:with-param name="lab_desc3">下載模板。以Excel打開模板，修改相關的出席信息，在Excel内把模板另存為Unicode文本，然後把Unicode文本檔案提交回系統。</xsl:with-param>
			<xsl:with-param name="lab_title">匯入出席率 –第一步：文件上傳</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="evalmanagement">
			<xsl:with-param name="lab_g_form_btn_next">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_file_location">文件位置</xsl:with-param>
			<xsl:with-param name="lab_export">导出模板</xsl:with-param>
			<xsl:with-param name="lab_desc1">在下面指定包含出席信息的文件。点击</xsl:with-param>
			<xsl:with-param name="lab_desc2">此处</xsl:with-param>
			<xsl:with-param name="lab_desc3">下载模板。以Excel打开模板，修改相关的出席信息，在Excel里把模板另存为Unicode文本，然后把Unicode文本档案提交回系统。</xsl:with-param>
			<xsl:with-param name="lab_title">导入出席率 –第一步：文件上传</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="evalmanagement">
			<xsl:with-param name="lab_g_form_btn_next">Next</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_file_location">File location</xsl:with-param>
			<xsl:with-param name="lab_export">Export template</xsl:with-param>
			<xsl:with-param name="lab_desc1">Specify the file containing the score below. Click </xsl:with-param>
			<xsl:with-param name="lab_desc2">here </xsl:with-param>
			<xsl:with-param name="lab_desc3">to download the spreadsheet template for importing attendance. Open the spreadsheet with excel, mark the attendance, save the spreadsheet as unicode text from excel; and upload the saved unicode text file back to the system.</xsl:with-param>
			<xsl:with-param name="lab_title">Import attendance rate – step 1: file upload</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="evalmanagement">
		<xsl:param name="lab_g_form_btn_next"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_file_location"/>
		<xsl:param name="lab_export"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc1"/>
		<xsl:param name="lab_desc2"/>
		<xsl:param name="lab_desc3"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_title"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_desc1"/>
				<a href="javascript:attd.dl_att_rate_lst({itm_id})">
					<xsl:value-of select="$lab_desc2"/>
				</a>
				<xsl:value-of select="$lab_desc3"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="20%" align="right" valign="top" class="wzb-form-label">
					<span class="TitleText">
						<xsl:value-of select="$lab_file_location"/>：</span>
				</td>
				<td width="80%" class="wzb-form-control">
					<span class="Text">
						<xsl:call-template name="wb_gen_input_file">
							<xsl:with-param name="name">src_filename_path</xsl:with-param>
						</xsl:call-template>
						<!-- <input type="file" name="src_filename_path" class="wzb-inputText" style="width: 300;"/> -->
						<input type="hidden" name="src_filename"/>
					</span>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
				<tr>
					<td align="center">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_next"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:Batch.AttdRate.Import.exec(document.frmXml,<xsl:value-of select="itm_id"/>, '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:parent.close()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

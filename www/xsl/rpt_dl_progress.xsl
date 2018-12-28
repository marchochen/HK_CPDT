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
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript"><![CDATA[
var timer;
var req;
var last_pos = 0;
var window_name;
var rpt_type;
var export_stat_only;
if (getUrl('window_name')) {
	window_name = getUrlParam('window_name');
} else {
	alert('window_name not found.error');
}
if (getUrl('rpt_type')) {
	rpt_type = getUrlParam('rpt_type');
} else {
	alert('rpt_type not found.error');
}
if (getUrl('export_stat_only')) {
	export_stat_only = getUrlParam('export_stat_only');
} else {
	export_stat_only = false;
}

function get_rpt() {
	var get_rpt_url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'get_rpt', 'rpt_type', rpt_type, 'window_name', window_name, 'download',4,'export_stat_only',export_stat_only);
	if (getUrl('rsp_id')) {
		rsp_id = getUrlParam('rsp_id');
		get_rpt_url = setUrlParam('rsp_id',rsp_id,get_rpt_url);
	}
	if(getUrl('ils_itm_id')){
		var ils_itm_id = getUrlParam('ils_itm_id');
		get_rpt_url = setUrlParam('ils_itm_id',ils_itm_id,get_rpt_url);
	}
	$.ajax({
		url: get_rpt_url,
	   	success: function(req, options){},
	   	error: function(req, options){},
	   	method: 'GET'
	});
	timer = window.setInterval("get_rpt_progress()",1000);
}

function get_rpt_progress(){
	var get_rpt_pro_url = wb_utils_invoke_disp_servlet('module', 'report.ReportProgressModule', 'cmd', 'get_rpt_pro', 'window_name', window_name)

	$.ajax({
	   url: get_rpt_pro_url,
	   success: function(xml){
	   		onChange(xml);
	   },
	   error: function(req, options){
	   		clearInterval(timer);
			alert("There was a problem retrieving the XML data:\n");
	   },
	   dataType : "xml",
	   method: 'GET'
	});
}

function sent_stop_flag(flag) {
	var stop_flag_url = wb_utils_invoke_disp_servlet('module', 'report.ReportProgressModule', 'cmd', 'cancel', 'window_name', window_name);
	$.ajax({
	   url: stop_flag_url,
	   success: function(req, options){},
	   error: function(req, options){},
	   method: 'GET'
	});
	if (flag) {
		self.close();
	}
}

function onChange(xml) {
			resultXml = xml;
			if (resultXml.getElementsByTagName('error')[0] == null) {
				if (resultXml.getElementsByTagName('progress')[0] != null) {
					var cur_pos = parseInt(resultXml.getElementsByTagName('progress')[0].firstChild.nodeValue);
					if (cur_pos >= 100) {
						document.getElementById('pro_bar').src = wb_img_path + 'percent_bar_fill.gif';
						document.getElementById('pro_bar').width = 400;
						document.getElementById('cur_pos_value').innerHTML = 100;
						document.getElementById('pro_bar_empty').width = 0;
						if (resultXml.getElementsByTagName('file_path')[0] != null) {
							clearInterval(timer);
							filePath = resultXml.getElementsByTagName('file_path')[0].firstChild.nodeValue;
							downloadComplete(filePath);
						}
					}
					else if (cur_pos >= last_pos) {
						document.getElementById('pro_bar').src = wb_img_path + 'percent_bar_fill.gif';
						document.getElementById('pro_bar').width = cur_pos * 4;
						document.getElementById('cur_pos_value').innerHTML = cur_pos;
						document.getElementById('pro_bar_empty').width = 400 - cur_pos * 4;
						last_pos = cur_pos;
					}
					else if (cur_pos == -1) {
						document.getElementById('pro_bar').src = wb_img_path + 'percent_bar_fill.gif';
						document.getElementById('pro_bar').width = 400;
						document.getElementById('cur_pos_value').innerHTML = 100;
						document.getElementById('pro_bar_empty').width = 0;
						
						document.getElementById("up_desc").innerHTML = lab_complete;
						document.getElementById("error_row").innerHTML = lab_no_record;
						document.getElementById("btn_cancel_id").value = lab_g_form_btn_close;
						clearInterval(timer);
					}
				}
			} else {
				var error_msg = resultXml.getElementsByTagName('error')[0].firstChild.nodeValue;
				document.getElementById('error_row').innerHTML = lab_error + ' ' + error_msg;
				clearInterval(timer);
				sent_stop_flag(false);

	}
}

function empty() {}

function downloadComplete(filePath) {
	document.getElementById("btn_cancel_id").value = lab_g_form_btn_close;
	document.getElementById("up_desc").innerHTML = lab_complete;
	document.getElementById("file_path").href = filePath;
	document.getElementById("file_path").innerHTML = lab_file_link;
	//wbUtilsOpenWin(filePath);
}
		]]></script>
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="new_css">
				</xsl:call-template>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="get_rpt()">
				<xsl:call-template name="wb_init_lab"/>
			</body>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_mgmt_rpt">報告導出</xsl:with-param>
			<xsl:with-param name="lab_up_desc">正在生成報告，請稍候，生成報告過程中不要關閉此窗口。</xsl:with-param>
			<xsl:with-param name="lab_no_record">報告紀錄數為零</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_error">生成報告過程中發生錯誤，請聯係系統管理員，錯誤描述如下：</xsl:with-param>
			<xsl:with-param name="lab_complete">報告生成完畢</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_file_link">下載報告</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_mgmt_rpt">报告导出</xsl:with-param>
			<xsl:with-param name="lab_up_desc">正在生成报告，请稍候。生成报告过程中请不要关闭此窗口。</xsl:with-param>
			<xsl:with-param name="lab_no_record">报告记录数为零</xsl:with-param>
			<xsl:with-param name="lab_error">生成报告过程中发生错误，请联系系统管理员，错误描述如下：</xsl:with-param>
			<xsl:with-param name="lab_complete">报告生成完毕。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_file_link">下载报告</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_mgmt_rpt">Report export</xsl:with-param>
			<xsl:with-param name="lab_up_desc">Report is being generated. Please do not close this window.</xsl:with-param>
			<xsl:with-param name="lab_no_record">No record found</xsl:with-param>
			<xsl:with-param name="lab_error">Error occurred during report generation (please contact system administrator):</xsl:with-param>
			<xsl:with-param name="lab_complete">Report generation completed.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_file_link">Download the report</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_mgmt_rpt"/>
		<xsl:param name="lab_up_desc"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_error"/>
		<xsl:param name="lab_complete"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_file_link"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="width">420</xsl:with-param>
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_mgmt_rpt"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width">420</xsl:with-param>
		</xsl:call-template>
		<table cellpadding="3" cellspacing="0" border="0" width="420" class="Bg">
			<tr>
				<td>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td align="left">
					<span id="up_desc"><xsl:value-of select="$lab_up_desc"/></span><xsl:text>&#160;(</xsl:text><span id="cur_pos_value">0</span><xsl:text>%)</xsl:text>
				</td>
			</tr>
			<tr>
				<td align="left" valign="middle" width="100%" id="rpt_progress">
					<img src="{$wb_img_path}percent_bar_end.gif" border="0" align="middle"/>
					<img id="pro_bar" src="{$wb_img_path}percent_bar_empty.gif" width="1" height="12" border="0" align="middle"/>
					<img id="pro_bar_empty" src="{$wb_img_path}percent_bar_empty.gif" width="399" height="12" border="0" align="middle"/>
					<img src="{$wb_img_path}percent_bar_end.gif" border="0" align="middle"/>
				</td>
			</tr>
			<tr>
				<td align="left">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/><span id="error_row"></span><a href="" id="file_path" target="_blank"></a>
				</td>
			</tr>
			<tr>
				<td>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width">420</xsl:with-param>
		</xsl:call-template>
		<table cellspacing="0" cellpadding="3" border="0" width="420">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">Javascript:sent_stop_flag(true)</xsl:with-param>
						<xsl:with-param name="id">btn_cancel_id</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<script language="JavaScript"><![CDATA[
			var lab_file_link = ']]><xsl:value-of select="$lab_file_link"/><![CDATA[';
			var lab_no_record = ']]><xsl:value-of select="$lab_no_record"/><![CDATA[';
			var lab_error = ']]><xsl:value-of select="$lab_error"/><![CDATA[';
			var lab_no_record = ']]><xsl:value-of select="$lab_no_record"/><![CDATA[';
			var lab_complete = ']]><xsl:value-of select="$lab_complete"/><![CDATA[';
			var wb_img_path= ']]><xsl:value-of select="$wb_img_path"/><![CDATA[';
			var lab_g_form_btn_close = ']]><xsl:value-of select="$lab_g_form_btn_close"/><![CDATA[';
		]]></script>
	</xsl:template>
</xsl:stylesheet>

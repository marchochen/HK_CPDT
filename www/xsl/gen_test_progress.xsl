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
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript"><![CDATA[
var timer;
var req;
var last_pos = 0;
var window_name;

if (getUrl('window_name')) {
	window_name = getUrlParam('window_name');
} else {
	alert('window_name not found.error');
}

function gen_test() {
	timer = window.setInterval("gen_test_progress()",1000);
}

function gen_test_progress(){
	var gen_test_pro_url = wb_utils_invoke_disp_servlet('module', 'report.ReportProgressModule', 'cmd', 'get_rpt_pro', 'window_name', window_name)
	req = getXMLHttpRequest();
	req.onreadystatechange = onChange;
	req.open("GET", gen_test_pro_url,true);
	req.send(null);
}

var progress_bar_height = parseInt(wb_utils_testplayer_fs_row_progress_height);
var hide_progress_bar_interval = null;
function sent_stop_flag() {
	var stop_flag_url = wb_utils_invoke_disp_servlet('module', 'report.ReportProgressModule', 'cmd', 'cancel', 'window_name', window_name);
	req = getXMLHttpRequest();
	req.open("GET", stop_flag_url,true);
	req.send(null);
	hide_progress_bar_interval = setInterval("hideProgressBar()", 1);
}

function hideProgressBar() {
	progress_bar_height -= 5;
	if (progress_bar_height >= 0) {
		window.parent.document.getElementById('all_frameid').rows = progress_bar_height + wb_utils_testplayer_fs_row_progress_subfix;
	} else {
		clearInterval(hide_progress_bar_interval);
	}
}

function onChange() {
	// only if req shows "loaded"
	if (req.readyState == 4) {
		// only if "OK"
		if (req.status == 200) {
			resultXml = req.responseXML;
			if (resultXml.getElementsByTagName('error')[0] == null) {
				if (resultXml.getElementsByTagName('progress')[0] != null) {
					var cur_pos = parseInt(resultXml.getElementsByTagName('progress')[0].firstChild.nodeValue);
					if (cur_pos >= 100) {
						document.getElementById('pro_bar').src = wb_img_path + 'percent_bar_fill.gif';					
						document.getElementById('pro_bar').width = 400;
						document.getElementById('cur_pos_value').innerText = 100;
						document.getElementById('pro_bar_empty').width = 0;
						clearInterval(timer);
						downloadComplete();
					}
					else if (cur_pos >= last_pos) {
						document.getElementById('pro_bar').src = wb_img_path + 'percent_bar_fill.gif';					
						document.getElementById('pro_bar').width = cur_pos * 4;
						document.getElementById('cur_pos_value').innerText = cur_pos;
						document.getElementById('pro_bar_empty').width = 400 - cur_pos * 4;
						last_pos = cur_pos;
					}
					else if (cur_pos == -1) {
						document.getElementById("error_row").innerText = lab_no_record;
						//document.getElementById("btn_cancel_id").value = lab_g_form_btn_close;
						clearInterval(timer);
					}
				}
			} else {
				var error_msg = resultXml.getElementsByTagName('error')[0].firstChild.nodeValue;
				document.getElementById('error_row').innerText = lab_error + ' ' + error_msg;
				clearInterval(timer);
				sent_stop_flag();
			}
		} else {
			clearInterval(timer);
			alert("There was a problem retrieving the XML data:\n");
		}
	}
}

function downloadComplete() {
	//document.getElementById("btn_cancel_id").value = lab_g_form_btn_close;
	document.getElementById("up_desc").innerText = lab_complete;
	sent_stop_flag();
}
		]]></script>
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="gen_test()">
				<xsl:call-template name="wb_init_lab"/>
			</body>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_up_desc">正在生成測驗，請稍候...</xsl:with-param>
			<xsl:with-param name="lab_no_record">生成試題數為零</xsl:with-param>
			<xsl:with-param name="lab_error">生成測驗過程中發生錯誤，請聯絡系統管理員，錯誤描述如下:</xsl:with-param>
			<xsl:with-param name="lab_complete">生成測驗成功。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<!-- header text -->
			<xsl:with-param name="lab_up_desc">正在生成测验，请稍候...</xsl:with-param>
			<xsl:with-param name="lab_no_record">生成试题数为零</xsl:with-param>
			<xsl:with-param name="lab_error">生成测验过程中发生错误，请联系系统管理员，错误描述如下:</xsl:with-param>
			<xsl:with-param name="lab_complete">生成测验成功。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_up_desc">The test is being generated...</xsl:with-param>
			<xsl:with-param name="lab_no_record">No record found</xsl:with-param>
			<xsl:with-param name="lab_error">Error occurred during generating test(please contact system administrator):</xsl:with-param>
			<xsl:with-param name="lab_complete">Test generation completed.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_up_desc"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="lab_error"/>
		<xsl:param name="lab_complete"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
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
		<xsl:call-template name="wb_ui_line"/>
		<script language="JavaScript"><![CDATA[  
			var lab_no_record = ']]><xsl:value-of select="$lab_no_record"/><![CDATA[';
			var lab_error = ']]><xsl:value-of select="$lab_error"/><![CDATA[';
			var lab_complete = ']]><xsl:value-of select="$lab_complete"/><![CDATA[';
			var wb_img_path= ']]><xsl:value-of select="$wb_img_path"/><![CDATA[';
			var lab_g_form_btn_close = ']]><xsl:value-of select="$lab_g_form_btn_close"/><![CDATA[';
		]]></script>
	</xsl:template>
</xsl:stylesheet>

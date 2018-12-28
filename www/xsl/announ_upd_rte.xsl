<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/html_edit_pane.xsl"/>
	<xsl:import href="utils/kindeditor.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/announ_ins_upd_share.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ========================================================= -->
	<xsl:variable name="cur_time">
		<xsl:value-of select="announcement/message/cur_time"/>
	</xsl:variable>
	<xsl:variable name="tc_enabled" select="/announcement/meta/tc_enabled"/>
	<xsl:variable name="isMobile" select="/announcement/isMobile"/>
	<!-- ========================================================= -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="announcement"/>
		</html>
	</xsl:template>
	<!-- ========================================================= -->
	<xsl:template match="announcement">
		<xsl:apply-templates select="message/item"/>
	</xsl:template>
	<!-- ========================================================= -->
	<xsl:template match="item">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_upd_ann"/>
		<xsl:param name="lab_msg_title"/>
		<xsl:param name="lab_msg_icon"/>
		<xsl:param name="lab_msg_body"/>
		<xsl:param name="lab_msg_begin_date"/>
		<xsl:param name="lab_msg_end_date"/>
		<xsl:param name="lab_msg_receipt"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_msg_status"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="lab_tc"/>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}rte2.js"/>
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}rte1.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_rm_{$wb_cur_lang}.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[		
			announcement = new wbAnnouncement();
			var goldenman = new wbGoldenMan
			$(function(){
				init();
				ins_default_date();
			});
			function add_ann(){
				if (document.frmXml.msg_body.type!="textarea"){
					document.frmXml.msg_body.value = getHTML();
					document.frmXml.msg_text.value = getText();
				}
				announcement.add_sys_ann_exec(document.frmXml,']]><xsl:value-of select="$wb_lang"/><![CDATA[');
			}
			
			function init(){
				document.frmXml.msg_title.value=]]>'<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="title"/></xsl:with-param></xsl:call-template>'<![CDATA[
				document.frmXml.msg_title.focus();
				
			}
			
			function ins_default_date(){
				var frm = document.frmXml
				//Get the server current time
				str = "]]><xsl:value-of select="$cur_time"/><![CDATA["
				cur_day = str.substring((str.lastIndexOf('-') + 1), str.indexOf(' '))
				cur_month = str.substring((str.indexOf('-') + 1), str.lastIndexOf('-'))
				cur_year = str.substring(0, str.indexOf('-'))
				cur_hour = str.substring((str.indexOf(' ') + 1), str.indexOf(':'))
				cur_min = str.substring((str.indexOf(':') + 1), str.lastIndexOf(':'))
				
				frm.cur_dt.value = str
				frm.cur_dt_dd.value = cur_day
				frm.cur_dt_mm.value = cur_month
				frm.cur_dt_yy.value = cur_year
				frm.cur_dt_hour.value = cur_hour
				frm.cur_dt_min.value = cur_min
				
				if (frm.start_date){
					if (frm.start_date[0].checked == true){
						frm.start_dd.value = cur_day
						frm.start_mm.value = cur_month
						frm.start_yy.value = cur_year
						
						frm.start_hour.value = cur_housr
						frm.start_min.value = cur_min
					}
				}

				if (frm.end_date){					
					if (frm.end_date[0].checked == true){
						//frm.end_dd.value = cur_day
						//frm.end_mm.value = cur_month
						//frm.end_yy.value = cur_year
						
						//frm.end_hour.value = "23"
						//frm.end_min.value = "59"
					}
				}
			}	
			
			
			
		]]></script>
			<xsl:call-template name="kindeditor_css"/>
		</head>
		<body ondragstart="return false" leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init();ins_default_date();">
			<form name="frmXml" method="post" enctype="multipart/form-data">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_MSG_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_MSG_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title"><xsl:with-param name="text" select="$lab_upd_ann"/></xsl:call-template>
				<xsl:call-template name="ann_body">
					<xsl:with-param name="mode">upd</xsl:with-param>
					<xsl:with-param name="lab_msg_title" select="$lab_msg_title"/>
					<xsl:with-param name="lab_msg_icon" select="$lab_msg_icon"/>
					<xsl:with-param name="lab_msg_body" select="$lab_msg_body"/>
					<xsl:with-param name="lab_msg_begin_date" select="$lab_msg_begin_date"/>
					<xsl:with-param name="lab_msg_end_date" select="$lab_msg_end_date"/>
					<xsl:with-param name="lab_msg_receipt" select="$lab_msg_receipt"/>
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
					<xsl:with-param name="lab_msg_status" select="$lab_msg_status"/>
					<xsl:with-param name="lab_on" select="$lab_on"/>
					<xsl:with-param name="lab_off" select="$lab_off"/>
					<xsl:with-param name="lab_unlimit" select="$lab_unlimit"/>
					<xsl:with-param name="lab_immediate" select="$lab_immediate"/>
					<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
					<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>
					<xsl:with-param name="lab_tc" select="$lab_tc"/>
					<xsl:with-param name="tc_enabled" select="$tc_enabled"/>
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<!-- ========================================================= -->
</xsl:stylesheet>

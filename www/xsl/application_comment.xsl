<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<xsl:call-template name="new_css"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<!--alert样式  -->
		    <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<script language="Javascript" type="text/javascript"><![CDATA[
				var app =  new wbApplication;
				
				function submit_multi_action(lang){
					frm = window.opener.document.frmAction;
					action = window.opener.document.frmAction.curAction.value;
					if (getChars(document.frmAction.content.value) > 400){
						Dialog.alert(eval('text_label_old.course_remarks')+eval('text_label_old.label_title_length_warn_400'));
						document.frmAction.content.focus();
						return false;
					}else{
						window.opener.submit_multi_action(frm,action,document.frmAction.content.value);
					}
					self.close();
				}
				
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmAction)">
			<form name="frmAction">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_remarks">備註</xsl:with-param>
			<xsl:with-param name="lab_confirm_action">確認操作</xsl:with-param>
			<xsl:with-param name="lab_action">操作</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">確認</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_remarks_requirement">如果同時處理多個報名，他們將使用相同的備註內容。備註的字數請不要超過400個字符（200個中文字）。</xsl:with-param>
			<xsl:with-param name="lab_desc">請在這裏確認報名處理的操作。需要時可以填寫備註作為參考。</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_course">課程</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_remarks">备注</xsl:with-param>
			<xsl:with-param name="lab_confirm_action">确认操作</xsl:with-param>
			<xsl:with-param name="lab_action">操作</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_remarks_requirement">如果同时处理多个报名，他们将使用相同的备注内容。备注的字数请不要超过400字符（200个中文字）。</xsl:with-param>
			<xsl:with-param name="lab_desc">请在这里确认报名处理的操作。需要时可以填写备注作为参考。</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_course">课程</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_remarks">Remarks</xsl:with-param>
			<xsl:with-param name="lab_confirm_action">Confirm action</xsl:with-param>
			<xsl:with-param name="lab_action">Action</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_remarks_requirement">The same remarks will be applied to all enrollments if multiple enrollments are selected. Use no more than 400 characters.</xsl:with-param>
			<xsl:with-param name="lab_desc">Please confirm your enrollment action.  Optionally you can fill in remarks for reference.</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_course">Course</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_action"/>
		<xsl:param name="lab_confirm_action"/>
		<xsl:param name="lab_remarks"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_remarks_requirement"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_course"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_confirm_action"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>

		<xsl:call-template name="wb_ui_line"/>
		<table class="Bg" cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td width="20%" align="right" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_action"/>&#160;:&#160;</span>
				</td>
				<td width="80%">
					<span class="TextBold">
						<script language="Javascript"><![CDATA[document.write(window.opener.document.frmAction.curAction.value);]]></script>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="top">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%">
					<span class="TextBold">
						<script language="Javascript"><![CDATA[ document.write(window.opener.document.frmAction.curActionWarning.value);]]></script>
					</span>
				</td>
			</tr>			
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_learner"/>&#160;:&#160;</span>
				</td>
				<td width="80%">
					<span class="Text">
						<script language="Javascript"><![CDATA[document.write(app.get_display_name_lst(window.opener.document.frmAction));]]></script>
					</span>
				</td>
			</tr>
			<script language="Javascript"><![CDATA[if(window.opener.document.frmAction.itm_title) {
				document.write('<tr><td width="20%" align="right" valign="top"><span class="TitleText">]]><xsl:value-of select="$lab_course"/>&#160;:&#160;<![CDATA[</span></td><td width="80%"><span class="Text">');]]><![CDATA[document.write(window.opener.document.frmAction.itm_title.value);]]><![CDATA[document.write('</span></td></tr>')]]>
}
</script>
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_remarks"/>&#160;:&#160;</span>
				</td>
				<td width="80%" style="height:200px;">
					<textarea class="wzb-inputTextArea" rows="5" cols="20" style="width:300px;" name="content"/>
					<br/>
					<span class="Text">
						<xsl:value-of select="$lab_remarks_requirement"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" border="0">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:submit_multi_action('<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>

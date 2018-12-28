<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/html_edit_pane.xsl"/>
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
	<xsl:import href="utils/kindeditor.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	
		
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ========================================================= -->
	<xsl:variable name="isMobile" select="/announcement/isMobile"/>
	<xsl:variable name="msg_belong_exam_ind" select="/announcement/msg_belong_exam_ind"/>
	<xsl:variable name="cur_time">
		<xsl:value-of select="announcement/message/cur_time"/>
	</xsl:variable>
	
	<!-- 课程公告 -->
	<xsl:variable name="label_core_training_management_236" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_236')"/>
	
	<!-- 考试公告 -->
	<xsl:variable name="label_core_training_management_287" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_287')"/>
	
	<!-- 修改公告 -->
	<xsl:variable name="label_core_training_management_263" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_263')"/>
	
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
		<xsl:param name="lab_msg_status"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_immediate"/>		
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}rte2.js"/>
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}rte1.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_rm_{$wb_cur_lang}.js"></script>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[		
			announcement = new wbAnnouncement();
			itm_lst = new wbItem();
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
			<script language="Javascript"><![CDATA[
			//RTE JS
			var g_state;
			//End RTE JS
		]]></script>
		</head>
		<body ondragstart="return false" leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init();ins_default_date();">
			<form name="frmXml" method="post">
						<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
				<xsl:call-template name="wb_ui_title"><xsl:with-param name="text" select="$lab_upd_ann"/></xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
							<xsl:value-of select="$itm_title"/>
						</a>
						<span class="NavLink">&#160;&gt;&#160;</span>
						<a href="javascript:announcement.sys_lst('all','RES','{$cos_res_id}','','','','','',true)" class="NavLink">
							<xsl:choose>
								<xsl:when test="$msg_belong_exam_ind = 'true'">
									<xsl:value-of select="$label_core_training_management_236"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$label_core_training_management_236"/>
								</xsl:otherwise>
							</xsl:choose>
						</a>
						<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$label_core_training_management_263"/>
						</span>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="ann_body">
					<xsl:with-param name="mode">upd</xsl:with-param>
					<xsl:with-param name="lab_msg_title" select="$lab_msg_title"/>
					<xsl:with-param name="lab_msg_icon" select="$lab_msg_icon"/>
					<xsl:with-param name="lab_msg_body" select="$lab_msg_body"/>
					<xsl:with-param name="lab_msg_begin_date" select="$lab_msg_begin_date"/>
					<xsl:with-param name="lab_msg_end_date" select="$lab_msg_end_date"/>
					<xsl:with-param name="lab_msg_status" select="$lab_msg_status"/>
					<xsl:with-param name="lab_on" select="$lab_on"/>
					<xsl:with-param name="lab_off" select="$lab_off"/>
					<xsl:with-param name="lab_unlimit" select="$lab_unlimit"/>
					<xsl:with-param name="lab_immediate" select="$lab_immediate"/>
					<xsl:with-param name="lab_g_form_btn_ok" select="$lab_g_form_btn_ok"/>
					<xsl:with-param name="lab_g_form_btn_cancel" select="$lab_g_form_btn_cancel"/>					
				</xsl:call-template>
			</form>
		</body>
	</xsl:template>

	<!-- ========================================================= -->
	<xsl:template name="html_edit_pane" priority="10">
		<xsl:param name="supportHTML">true</xsl:param>
		<xsl:param name="body"/>
		<xsl:param name="frm"/>
		<xsl:param name="fld_name"/>
		<input type="hidden" name="{$fld_name}_hidden" value="{$body}"/>
		<script language="Javascript"><![CDATA[
browser = navigator.appName;
if(browser.indexOf("Microsoft")>-1 && ]]><xsl:value-of select="$supportHTML"/><![CDATA[){
	str = '<input type="hidden" value="" name="]]><xsl:value-of select="$fld_name"/><![CDATA["/>';
	str += '<table width="100%" border="0" cellspacing="0" cellpadding="1">';
	str += '<tr class="wbParInfTitleBg">';
	str += '<td>';
	str += '<div id="idEditor" style="VISIBILITY:hidden">';
	str += '<table id="idToolbar" width="100%" cellspacing="0" cellpadding="0" onClick="_CPopup_Hide()" border="0">';
	str += '<tr id="tbTopBar">';
	str += '<td id="tbUpLeft"></td>';
	str += '<td colspan="2" id="tbUpMiddle"></td>';
	str += '<td id="tbUpRight"></td>';
	str += '</tr>';
	str += '<tr>';
	str += '<td id="tbMidLeft"></td>';
	str += '<td id="tbContents" class="wbAnnounceRTEcontent">';
	document.write(str);
	L_TOOLBARGIF_TEXT = "]]><xsl:value-of select="$wb_img_path"/><![CDATA[rte_tbEN.gif";
	L_EMOTICONPATH_TEXT = "]]><xsl:value-of select="$wb_img_path"/><![CDATA[";
	_drawToolbar();
	str = '<td id="tbButtons" align="right" class="wbAnnounceRTEcontent"></td>';
	str += '<td id="tbMidRight"></td>';
	str += '</tr>';
	str += '<tr id="tbbottomBar">';
	str += '<td id="tbLowLeft"></td>';
	str += '<td colspan="2" id="tbLowMiddle"></td>';
	str += '<td id="tbLowRight"></td>';
	str += '</tr>';
	str += '</table>';
	str += '<iframe name="idPopup" style="HEIGHT: 100px; LEFT: 25px; MARGIN-TOP: 8px; POSITION: absolute; VISIBILITY: hidden; Z-INDEX: -1"></iframe>';
	str += '<iframe id="EditBox" style="LEFT: 0px; MARGIN-TOP: 0px;" name="idEditbox" width="100%" height="100" onFocus="_CPopup_Hide()"></iframe>';
	str += '<div id="tbmode">';
	document.write(str);
	_drawModeSelect(']]><xsl:value-of select="$wb_lang"/><![CDATA[');
	str = '</div>';
	str += '</div>';
	str += '</td>';
	str += '</tr>';
	str += '</table>';
	document.write(str);
	_initEditor();
	idEditbox.document.body.innerHTML = ]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$fld_name"/><![CDATA[_hidden.value;
}else{
	str = '<textarea rows="6" cols="40" name="msg_body">';
	str += '</textarea>';
	document.write(str);
	]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$fld_name"/><![CDATA[.value =  ]]><xsl:value-of select="$frm"/><![CDATA[.]]><xsl:value-of select="$fld_name"/><![CDATA[_hidden.value;
}
]]></script>
	</xsl:template>	<!-- ========================================================= -->
	<xsl:template match="cur_usr"/>
	<!-- ========================================================= -->
</xsl:stylesheet>

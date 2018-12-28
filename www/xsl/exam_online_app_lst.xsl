<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="//exam_module/exam_info/@itm_id"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_online_app" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '758')"/>
	<xsl:variable name="lab_no_usr" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '768')"/>
	<xsl:variable name="lab_exam_man" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn2_EXAM_ITEM_MAIN')"/>
	<xsl:variable name="lab_app_total" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '759')"/>
	<xsl:variable name="lab_exam_cnt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '760')"/>
	<xsl:variable name="lab_exam_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '783')"/>
	<xsl:variable name="lab_pause_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '784')"/>
	<xsl:variable name="lab_g_form_btn_stop" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '762')"/>
	<xsl:variable name="lab_g_form_btn_relieve" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '761')"/>
	<xsl:variable name="lab_g_form_btn_send" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '770')"/>
	<xsl:variable name="lab_g_form_btn_submit" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '771')"/>
	<xsl:variable name="lab_pause_cnt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '785')"/>
	<xsl:variable name="lab_select_all" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '786')"/>
	<!-- =============================================================== -->
	<xsl:template match="/exam_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<style type="text/css">
				#toolTip {
					display:none;
					position:absolute;
					background-color:#ffffff;
					padding:5px;
					z-index:999;
					border: thin outset #ffffff;
				}
			</style>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript">
			<![CDATA[
				var wb_item = new wbItem;
				var exam_peen_lst = new Array();
				var pause_peen_lst = new Array();
				var select_id = new Array(); //保存选过的id
				var httpReq;
				
				var lab_select_all = ']]><xsl:value-of select="$lab_select_all"/><![CDATA[';
				
				var no_online_usr, exam_status_lst, pause_status_lst, exam_cnt, pause_cnt, exam_status, pause_status;		
				
				function peen(){
				    this.usr_ent_id = '';
				    this.itm_id = '';
				    this.name = '';
				    this.group = '';
				}
				
				function drewExamPeen() {
					if(exam_peen_lst.length > 0) {
						exam_status.style.display = "";
						var html_code = '<table cellspacing="0" cellpadding="3" border="0" width="' + document.frmXml.table_width.value + '" class="Bg">';
						html_code += '<tr><td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="10" border="0"/></td></tr>'
											
						exam_cnt.innerHTML = exam_peen_lst.length;
						
						if (exam_peen_lst.length > 1) {
							html_code += '<tr><td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
							html_code += '<td><input onclick="javascript:gen_frm_sel_all_checkbox(document.frmXml,this,\'exam_box\');changeAllBox(this,\'exam_box\')" id="sel_all" type="checkbox" name="sel_all_checkbox"';
							
							if (select_id['exam_box'] === true) {
								html_code += ' checked';
							}
							
							html_code += '/><span class="Text">' + lab_select_all + '</span></td></tr>';
						}
						
						
						for(i=0;i < exam_peen_lst.length;i++) {
							if(i%4==0) {
								if(i > 0) {
									html_code += '<td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
									html_code += "</tr>";
								}
								html_code += "<tr>";
								html_code += '<td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
							}
							html_code += '<td><input id="exam_box' + i + '" type="checkbox" name="exam_box" value="' + exam_peen_lst[i].usr_ent_id + '" onClick="changeSel(this)"';
							if (select_id[exam_peen_lst[i].usr_ent_id] === true) {
								html_code += ' checked'
							}
							
							html_code += '/><label for="exam_box' + i + '"><span class="Text" onmouseover="wbUtilsChangeToolTipDis (this,event, \'toolTip\', \'show\', \'' + exam_peen_lst[i].group + '\')" onmouseout="wbUtilsChangeToolTipDis (this,event, \'toolTip\', \'hide\', \'' + exam_peen_lst[i].group + '\')">' + exam_peen_lst[i].name + '</span></label></td>';
						}
						
						html_code += '<td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
						html_code += '</tr>';
	
						html_code += '<tr><td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="10" border="0"/></td></tr>'
						html_code += '</table>';
						exam_status_lst.innerHTML = html_code;
					} else {
						exam_status.style.display = "none";
					}
				}
				
				function drewPausePeen() {
					if(pause_peen_lst.length > 0) {
						pause_status.style.display = "";
						var html_code = '<table cellspacing="0" cellpadding="3" border="0" width="' + document.frmXml.table_width.value + '" class="Bg">';
						html_code += '<tr><td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="10" border="0"/></td></tr>'
											
						pause_cnt.innerHTML = pause_peen_lst.length;
						
						if (pause_peen_lst.length > 1) {
							html_code += '<tr><td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
							html_code += '<td><input onclick="javascript:gen_frm_sel_all_checkbox(document.frmXml,this,\'pause_box\');changeAllBox(this,\'pause_box\')" id="sel_all" type="checkbox" name="sel_all_checkbox"';
							
							if (select_id['pause_box'] === true) {
								html_code += ' checked';
							}
							
							html_code += '/><span class="Text">' + lab_select_all + '</span></td></tr>';
						}
						
						
						for(i=0;i < pause_peen_lst.length;i++) {
							if(i%4==0) {
								if(i > 0) {
									html_code += '<td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
									html_code += "</tr>";
								}
								html_code += "<tr>";
								html_code += '<td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
							}
							
							html_code += '<td><input id="pause_box' + i + '" type="checkbox" name="pause_box" value="' + pause_peen_lst[i].usr_ent_id + '" onClick="changeSel(this)"';
							if (select_id[pause_peen_lst[i].usr_ent_id] === true) {
								html_code += ' checked'
							}
							
							html_code += '/><label for="pause_box' + i + '"><span class="wzb-form-label" onmouseover="wbUtilsChangeToolTipDis (this, event, \'toolTip\', \'show\', \'' + pause_peen_lst[i].group + '\')" onmouseout="wbUtilsChangeToolTipDis (this, event, \'toolTip\', \'hide\', \'' + pause_peen_lst[i].group + '\')">' + pause_peen_lst[i].name + '</span></label></td>';
						}
						
						html_code += '<td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="1" border="0"/></td>';
						html_code += '</tr>';
	
						html_code += '<tr><td><img src="' + document.frmXml.img_path.value + 'tp.gif" width="1" height="10" border="0"/></td></tr>'
						html_code += '</table>';
						pause_status_lst.innerHTML = html_code;
					} else {
						pause_status.style.display = "none";
					}
				}
				
				function drewEmptyTable() {
					if(exam_peen_lst.length == 0 && pause_peen_lst.length == 0) {
						no_online_usr.style.display = "";
					} else {
						no_online_usr.style.display = "none";
					}
				}
				
				function init() {
					no_online_usr = document.getElementById('no_online_usr');
					exam_status_lst = document.getElementById('exam_status_lst');
					pause_status_lst = document.getElementById('pause_status_lst');
					exam_cnt = document.getElementById('exam_cnt');
					pause_cnt = document.getElementById('pause_cnt');
					exam_status = document.getElementById('exam_status');
					pause_status = document.getElementById('pause_status');
					
					var timer = window.setInterval("putDataVision()",10000);
					putDataVision();
				}
				
				function putDataVision() {
					var itm_id = getUrlParam("itm_id");
					var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'get_online_app_list', 'itm_id', itm_id);
					httpReq = getXMLHttpRequest();
					httpReq.onreadystatechange = onChange;
					httpReq.open("GET", url, true);
					httpReq.send(null);
				}
				
				function onChange() {
					// only if req shows "loaded"
					if (httpReq.readyState == 4) {
						// only if "OK"
						if (httpReq.status == 200) {
							resultXml = httpReq.responseXML;
							var exam_list = resultXml.getElementsByTagName('exam_usr_list');
							var pause_list = resultXml.getElementsByTagName('pause_usr_list');
							var temp_peen;
							if(exam_list != null) {
								exam_peen_lst.length = 0;
								for(i=0;i<exam_list.length;i++) {
									temp_peen = new peen();
									//name
									temp_peen.name = exam_list[i].getElementsByTagName('name')[0].firstChild.nodeValue;
									//group
									temp_peen.group = exam_list[i].getElementsByTagName('group')[0].firstChild.nodeValue;
									//ent_id
									temp_peen.usr_ent_id = exam_list[i].getElementsByTagName('ent_id')[0].firstChild.nodeValue;
									//itm_id
									temp_peen.itm_id = exam_list[i].getElementsByTagName('itm_id')[0].firstChild.nodeValue;
									exam_peen_lst[i] = temp_peen;
								}
							}
							if(pause_list != null) {
								pause_peen_lst.length = 0;
								for(i=0;i<pause_list.length;i++) {
									temp_peen = new peen();
									//name
									temp_peen.name = pause_list[i].getElementsByTagName('name')[0].firstChild.nodeValue;
									//group
									temp_peen.group = pause_list[i].getElementsByTagName('group')[0].firstChild.nodeValue;
									//ent_id
									temp_peen.usr_ent_id = pause_list[i].getElementsByTagName('ent_id')[0].firstChild.nodeValue;
									//itm_id
									temp_peen.itm_id = pause_list[i].getElementsByTagName('itm_id')[0].firstChild.nodeValue;
									pause_peen_lst[i] = temp_peen;
								}
							}
							drewExamPeen();
							drewPausePeen();
							drewEmptyTable();
						}
					}
				}
				
				//记录选中的box
				function changeSel(box) {
					if (box.checked) {
						select_id[box.value] = true;
					} else {
						select_id[box.value] = false;
					}
				}
				
				//记录选中所有的box
				function changeAllBox(selAllBox, obj_name) {
					select_id[obj_name] = selAllBox.checked;
					var obj_lst = eval('document.frmXml.' + obj_name);
					if (obj_lst.length === undefined) {
						obj_lst = [obj_lst];
					}
					for (var i = 0, box; box = obj_lst[i]; i++) {
						select_id[box.value] = selAllBox.checked;
					}
				}
				
				//每做一个操作，清除一次记录checkbox选择的缓存
				function refreshSelectId() {
					select_id['exam_box'] = false;
					select_id['pause_box'] = false;
					select_id.length = 0;
					wb_utils_change_checkbox(document.frmXml, false);
				}


			]]>
			</script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml">
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="table_width" value="{$wb_gen_table_width}"/>
			<input type="hidden" name="img_path" value="{$wb_img_path}"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_COS_EVN_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text" select="$lab_online_app"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<span class="NavLink">
						<!--<a href="javascript:wb_utils_adm_syb_lst()" class="NavLink">-->
						<a href="javascript:wb_utils_nav_go('FTN_AMD_EXAM_LIST')" class="NavLink">
							<xsl:value-of select="$lab_exam_man"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="exam_info/itm_title"/>
					</span>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="50"/>
					<span class="TitleText">
						(<xsl:value-of select="$lab_app_total"/>: <xsl:value-of select="exam_info/app_total"/><img border="0" height="1" src="{$wb_img_path}tp.gif" width="20"/><xsl:value-of select="$lab_exam_cnt"/>: <span id="exam_cnt">0</span><img border="0" height="1" src="{$wb_img_path}tp.gif" width="20"/><xsl:value-of select="$lab_pause_cnt"/>: <span id="pause_cnt">0</span>)
					</span>
				</xsl:with-param>
			</xsl:call-template>
			<!-- <xsl:call-template name="wb_ui_line"/> -->
			<div id="usr_lst">
				<div id="exam_status" style="display:none">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="$lab_exam_status"/>
						</xsl:with-param>
						<xsl:with-param name="extra_td">
							<td align="right">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_stop"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:wb_item.exam.sent_msg_to_exam_learner_prep('<xsl:value-of select="$wb_lang"/>', document.frmXml, 'true', 'false', <xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_send"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:wb_item.exam.sent_msg_to_exam_learner_prep('<xsl:value-of select="$wb_lang"/>', document.frmXml, 'false', 'false', <xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_submit"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:javascript:wb_item.exam.sent_msg_to_exam_learner_prep('<xsl:value-of select="$wb_lang"/>', document.frmXml, 'false', 'true', <xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
							</td>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<div id="exam_status_lst">
					</div>
				</div>
				<div id="pause_status" style="display:none">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="$lab_pause_status"/>
						</xsl:with-param>
						<xsl:with-param name="extra_td">
							<td align="right">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name">
										<xsl:value-of select="$lab_g_form_btn_relieve"/>
									</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:wb_item.exam.release_pause_exam('<xsl:value-of select="$wb_lang"/>', document.frmXml, <xsl:value-of select="$itm_id"/>)</xsl:with-param>
								</xsl:call-template>
							</td>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<div id="pause_status_lst">
					</div>
				</div>
				<div id="no_online_usr">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_usr"/>
					</xsl:call-template>
					<!-- <table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="Bg">
						<tr><td><img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/></td></tr>
						<tr><td align="center"><span class="Text"><xsl:value-of select="$lab_no_usr"/></span></td></tr>
						<tr><td><img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/></td></tr>
					</table> -->
				</div>
			</div>
			<!-- <xsl:call-template name="wb_ui_line"/> -->
			<xsl:call-template name="wb_ui_footer"/>
		</form>
		<div id="toolTip" class="SmallText">
			</div>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>

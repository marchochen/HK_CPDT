<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="module_track.xsl"/>
	
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/module/@id"/>
	<xsl:variable name="usr_id" select="/module/cur_usr/@id"/>
	<xsl:variable name="max_upload" select="/module/header/@max_upload"/>
	
	<xsl:template name="DOCTYPE">
		<![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"/>]]>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:value-of select="document('')/*/xsl:template[@name='DOCTYPE']/node()" disable-output-escaping="yes"/> 
		<html>
			<xsl:apply-templates select="module"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
<!-- 			<xsl:call-template name="wb_css"> -->
<!-- 				<xsl:with-param name="view">wb_ui</xsl:with-param> -->
<!-- 			</xsl:call-template> -->
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<link rel="stylesheet" href="../static/css/three.css"/>
			<link rel="stylesheet" href="../static/js/font-awesome/css/font-awesome.min.css"/>
			<script type="text/javascript" SRC="{$wb_js_path}jquery.min.js" LANGUAGE="JavaScript"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_media.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_track.js"/>
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
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			$(function(){
				var file = ']]><xsl:value-of select="/module/header/source/text()"/><![CDATA['
				var type = file.substring(file.lastIndexOf(".") + 1)
				cos_id = ']]><xsl:value-of select="/module/@id"/><![CDATA['
				
				var url = ']]><xsl:value-of select="/module/header/source/text()"/><![CDATA['
				tpl_use = 'blank_template.xsl'
				mod_id = getUrlParam("mod_id");
				tkh_id = ']]><xsl:value-of select="/module/aicc_data/@tkh_id"/><![CDATA['
				usr_id = ']]><xsl:value-of select="/module/cur_usr/@id"/><![CDATA['
				var win_name = 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id;
				var content_url = module_lst.start_content_url("ASS",mod_id,tpl_use,cos_id, tkh_id, win_name)
				$('#url').attr("src", url);
				if(type == "jpg" || type == "jpeg" || type == "png" || type == "bmp" || type == "gig" || type == "tif" || type == "jpe" || type == "gif" || type == "rgb") {
					var picURL = '../resource/' + ']]><xsl:value-of select="@id"/><![CDATA[/' + ']]><xsl:value-of select="/module/header/source/text()"></xsl:value-of><![CDATA['
					$("#content").append("<img src='' id='picURL'>")
					$("#picURL").attr("src", picURL);
				} else {
					$("#content").append('<iframe src="content_url" width="100%" height="700" frameborder="0" id="mainid" />')
					$("#mainid").attr("src", module_lst.start_content_url("ASS",mod_id,tpl_use,cos_id, tkh_id, win_name))
				}
			});
			prog_track = new  wbTrack		
			ass = new wbAssignment
			var module_lst = new wbModule
			var max_upload = ]]><xsl:value-of select="header/@max_upload"/><![CDATA[;
			var mod_id = ]]><xsl:value-of select="header/@mod_id"/><![CDATA[;
			function status(){return false;}
			
			function submitToServer(isColsed){
				unloadHandler(isColsed);
			}
			
			function unloadHandler(isColsed){
				var mod_type = "ASS";
				var tkh_id = getParentUrlParam('tkh_id');
				if(mod_type === 'TST' || mod_type === 'DXT') {
				} else if( mod_type == 'SVY' || mod_type == 'STX'  || mod_type == 'EVN'  || tkh_id == '') {
				} else {
					var snd_module_data_url
					snd_module_data_url = '';
					
					prog_track.send_module_data(document.frmXml, '', '', mod_type, tkh_id, true);
				}
				var ua = navigator.userAgent.toLowerCase();
				if(((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))){
					window.parent.location.href = '/app/course/return/' + tkh_id;
				}
				if (window.parent && window.parent.opener) {
				    if(isColsed){
				      window.parent.close();
				    }
					var href = window.parent.opener.location.href;
					if(href.indexOf("isCont=true") < 0){
						if(href.indexOf("?") > 0){
							href += '&isCont=true';
						} else {
							href += '?isCont=true';
						}
					}
					window.parent.opener.location.href =  href;
				}else{
				}
			}
			
			function init() {
				var cur_time = new Date()  
				document.frmXml.course_id.value = ]]>'<xsl:value-of select="//header/@course_id" />'<![CDATA[
				document.frmXml.lesson_id.value = ]]>'<xsl:value-of select="//module/@id" />'<![CDATA[	
				document.frmXml.tkh_id.value = ]]>'<xsl:value-of select="//module/@tkh_id" />'<![CDATA[
				document.frmXml.start_time.value = ]]>'<xsl:value-of select="//module/aicc_data/attempt/@last_acc_datetime" />'<![CDATA[
				document.frmXml.cur_time.value = cur_time 
				document.frmXml.student_id.value = ]]>'<xsl:value-of select="//cur_usr/@ent_id" />'<![CDATA[
				document.frmXml.lesson_location.value = 'default'
				var mod_status = ']]><xsl:value-of select="/module/aicc_data/attempt/@status" /><![CDATA['
				if (mod_status == '') {
					document.frmXml.lesson_status.value = "I"
				} else {
					document.frmXml.lesson_status.value = mod_status
				}
				
				/*if (isOnlineExam === 'true') {
					timer = window.setInterval('getMessageFromSer()',10000);
				} else {
					window.setInterval("getHeartbeat()", 300000);
				}*/
			}
		]]></SCRIPT>
		<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onunload="javascript:wb_utils_close_preloading()" onload="init()">
			<form name="ass_submit" enctype="multipart/form-data" onsubmit="return status()">
				<xsl:call-template name="wb_init_lab"/>
			</form>
			<form name="frmXml">
				<input type="hidden" name="course_id" value="" />
				<input type="hidden" name="lesson_id" value="" />
				<input type="hidden" name="cur_time" value="" />
				<input type="hidden" name="student_id" value="" />
				<input type="hidden" name="tkh_id" value="" />
				<input type="hidden" name="lesson_location" />
				<input type="hidden" name="time" value="" />
				<input type="hidden" name="lesson_status" value="" />
				<input type="hidden" name="url_success" value="" />
				<input type="hidden" name="cmd" value="" />
				<input type="hidden" name="module" value="" />
				<input type="hidden" name="mod_id" value="" />
				<input type="hidden" name="start_time" value="" />
				<input type="hidden" name="encrypted_start_time" value="skip" />
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">內容</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_info_1">在完成學習後請點擊“離開”以記錄學習進度。</xsl:with-param>
			<xsl:with-param name="lab_info_2">請勿直接關閉瀏覽器退出，</xsl:with-param>
			<xsl:with-param name="lab_info_3">請您必須點擊“離開”按鈕退出，才能記錄妳的學習進度。</xsl:with-param>
			<xsl:with-param name="lab_leave">離開</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作業提交</xsl:with-param>
			<xsl:with-param name="lab_title_desc">點擊<a href="javascript:ass.start_exec('{/module/header/@subtype}',{/module/@id},{/module/header/@course_id},'{$wb_lang}','{/module/@tkh_id}','{//cur_usr/@ent_id}');" class="TextBold">這裡</a>查看作業說明。</xsl:with-param>
			<xsl:with-param name="lab_step1">第一步</xsl:with-param>
			<xsl:with-param name="lab_file_upload">上傳檔案</xsl:with-param>
			<xsl:with-param name="lab_inst">請輸入上傳檔案數目。</xsl:with-param>
			<xsl:with-param name="lab_inst3">我要上傳</xsl:with-param>
			<xsl:with-param name="lab_file">檔案</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_inst2">請按下面的 "瀏覽" 按鈕上傳你的作業。</xsl:with-param>
			<xsl:with-param name="lab_inst4">你可在所選檔案下面的空格內輸入該檔案的簡介。</xsl:with-param>
			<xsl:with-param name="lab_file_no">輸入上傳檔案數目</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next_step">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">内容</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_info_1">在完成学习后请点击“离开”以记录学习进度。</xsl:with-param>
			<xsl:with-param name="lab_info_2">请勿直接关闭浏览器退出，</xsl:with-param>
			<xsl:with-param name="lab_info_3">请您必须点击“离开”按钮退出，才能记录你的学习进度。</xsl:with-param>
			<xsl:with-param name="lab_leave">离开</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作业提交</xsl:with-param>
			<xsl:with-param name="lab_title_desc">点击<a href="javascript:ass.start_exec('{/module/header/@subtype}',{/module/@id},{/module/header/@course_id},'{$wb_lang}','{/module/@tkh_id}','{//cur_usr/@ent_id}');" class="TextBold">这里</a>查看作业说明。</xsl:with-param>
			<xsl:with-param name="lab_step1">第一步</xsl:with-param>
			<xsl:with-param name="lab_file_upload">上传文档</xsl:with-param>
			<xsl:with-param name="lab_inst">请输入上传文档数目。</xsl:with-param>
			<xsl:with-param name="lab_inst3">我要上传</xsl:with-param>
			<xsl:with-param name="lab_file">文档</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_inst2">请按下面的"浏览"按钮上传作业。注意：上传的文档请不要使用中文文件名。</xsl:with-param>
			<xsl:with-param name="lab_inst4">你可以为上传的文件输入简介。</xsl:with-param>
			<xsl:with-param name="lab_file_no">输入上传文档数目</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next_step">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_inst">Instruction</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_info_1">After completing your reading, click "Exit" to record your learning progress.</xsl:with-param>
			<xsl:with-param name="lab_info_2">Do not close the browser to exit, </xsl:with-param>
			<xsl:with-param name="lab_info_3">You must click the "Exit" button to exit in order to record your learning progress.</xsl:with-param>
			<xsl:with-param name="lab_leave">Exit</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">Assignment submission</xsl:with-param>
			<xsl:with-param name="lab_title_desc">Click <a href="javascript:ass.start_exec('{/module/header/@subtype}',{/module/@id},{/module/header/@course_id},'{$wb_lang}','{/module/@tkh_id}','{//cur_usr/@ent_id}');" class="TextBold">here</a> for the assignment instruction.</xsl:with-param>
			<xsl:with-param name="lab_step1">Step 1</xsl:with-param>
			<xsl:with-param name="lab_file_upload">File upload</xsl:with-param>
			<xsl:with-param name="lab_inst">Please specify how many files you want to upload.</xsl:with-param>
			<xsl:with-param name="lab_inst3">I want to upload</xsl:with-param>
			<xsl:with-param name="lab_file">file(s)</xsl:with-param>
			<xsl:with-param name="lab_desc">Description:</xsl:with-param>
			<xsl:with-param name="lab_inst2">Please upload your assignment files using the "Browse..." button below.</xsl:with-param>
			<xsl:with-param name="lab_inst4">You may enter description of the file(s) in the box below to it.</xsl:with-param>
			<xsl:with-param name="lab_file_no">Specify upload file number</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next_step">Next step</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_info_1"/>
		<xsl:param name="lab_info_2"/>
		<xsl:param name="lab_info_3"/>
		<xsl:param name="lab_leave"/>
		
		<div class="work_input_desc wzb-title-11 wzb-banner-bg14" align="left" style="padding: 0 0 0 24px; background:#27c5b8;">
			<xsl:value-of select="/module/header/title/text()"/>
		</div>
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_inst"/>
			</xsl:with-param>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		
		<div class="content_info report_title " style="padding:10px 0px;">
			<xsl:choose>
				<xsl:when test="/module/header/instruction/text() != ''">
				<p class="work_font">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value"><xsl:value-of disable-output-escaping="yes" select="/module/header/instruction"/></xsl:with-param>	
					</xsl:call-template>
				</p>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="/module/header/source/@type = 'URL'">
						<iframe src="" width="100%" height="700" frameborder="0" id="url" />
					</xsl:if>
					<xsl:if test="/module/header/source/@type = 'FILE'">
						<p class="work_font" style="text-align:center;">
							<div id="content"  style="text-align:center;" />
						</p>
					</xsl:if>
					<xsl:if test="/module/header/source/@type = 'ZIPFILE'">
						<p class="work_font" style="text-align:center;">
							<div id="content" style="text-align:center;"/>
						</p>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="header">
		<xsl:param name="lab_ass_submission"/>
		<xsl:param name="lab_title_desc"/>
		<xsl:param name="lab_step1"/>
		<xsl:param name="lab_file_upload"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_inst2"/>
		<xsl:param name="lab_inst3"/>
		<xsl:param name="lab_inst4"/>
		<xsl:param name="lab_file_no"/>
		<xsl:param name="lab_g_form_btn_next_step"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_ass_submission"/>
				<xsl:text>&#160;-&#160;</xsl:text>
				<xsl:value-of select="header/title"/>
			</xsl:with-param>
		</xsl:call-template>
		<div class="work_step">
			<span class="yellow_icon">1</span>
			<xsl:value-of select="$lab_step1"/>
			<xsl:text>&#160;:&#160;</xsl:text>
			<xsl:value-of select="$lab_file_upload"/>
		</div>
		<div class="work_font">
			<xsl:choose>
				<xsl:when test="$max_upload = '-1' ">
					<xsl:value-of select="$lab_inst"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$lab_inst2"/>
					<br/>
					<xsl:value-of select="$lab_inst4"/>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		<!-- Content -->
		<div class="report_info">
			<table class="Bg" border="0" cellpadding="3" cellspacing="0" width="100%">
				<xsl:choose>
					<xsl:when test="$max_upload != '-1' ">
						<xsl:call-template name="ass_submit_list">
							<xsl:with-param name="lab_desc" select="$lab_desc"/>
							<xsl:with-param name="lab_file" select="$lab_file"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="ass_submit">
							<xsl:with-param name="lab_inst3" select="$lab_inst3"/>
							<xsl:with-param name="lab_file" select="$lab_file"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</table>
		</div>
		<!-- button-->
		<div class="report_botton">
			<xsl:variable name="submit_function">
				<xsl:choose>
					<xsl:when test="$max_upload = '-1' ">javascript:ass.submit_unlimit_step_one(document.ass_submit,'<xsl:value-of select="$wb_lang"/>')</xsl:when>
					<xsl:otherwise>javascript:ass.submit_step_two(document.ass_submit,'<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<a href="javascript:;" onclick="{$submit_function}">
				<xsl:value-of select="$lab_g_form_btn_next_step"/>
			</a>
			<a href="javascript:;" onclick="javascript:ass.cancel('{/module/@tkh_id}')">
				<xsl:value-of select="$lab_g_form_btn_cancel"/>
			</a>
		</div>
		<input type="hidden" name="cmd" value=""/>
		<input type="hidden" name="mod_id" value="{$mod_id}"/>
		<input type="hidden" name="ass_max_upload" value="{$max_upload}"/>
		<input type="hidden" name="step" value=""/>
		<input type="hidden" name="num_of_files" value=""/>
		<input type="hidden" name="stylesheet" value=""/>
		<input type="hidden" name="tkh_id" value="{/module/@tkh_id}"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="ass_submit_list">
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_uploaded_file"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_file"/>
		<tr>
			<td width="20%" height="10">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
			<td width="80%" height="10">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
		</tr>
		<xsl:for-each select="header/Description/body">
			<xsl:variable name="file_id" select="@id"/>
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:choose>
							<xsl:when test=". != ''">
								<xsl:value-of select="."/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_file"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$file_id"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%" align="left" valign="top">
					<input class="wzb-inputText" name="file_name{@id}" type="file" style="width:350px;"/>
					<input name="file{@id}" type="hidden"/>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_desc"/>
					</span>
				</td>
				<td width="80%" align="left" valign="top">
					<textarea class="wzb-inputTextArea" rows="3" wrap="VIRTUAL" style="width:350px;" cols="35" name="comment{@id}"/>
				</td>
			</tr>
		</xsl:for-each>
		<tr>
			<td width="20%" height="10">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
			<td width="80%" height="10">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="ass_submit">
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_inst3"/>
		<xsl:param name="lab_file"/>
		<tr>
			<td colspan="2">
				<span class="grayC999">
					<xsl:value-of select="$lab_inst3"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="count(header/Description/body) = 0">
							<input class="work_doc_num" type="text" name="no_of_upload" maxlength="2" size="2" value=""/>
						</xsl:when>
						<xsl:otherwise>
							<input class="work_doc_num" type="text" name="no_of_upload" maxlength="2" size="2" value="{count(header/Description/body)}"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_file"/>
				</span>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<xsl:template match="env"/>
</xsl:stylesheet>

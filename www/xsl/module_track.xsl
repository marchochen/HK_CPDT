<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl" />
	<xsl:import href="cust/wb_cust_const.xsl" />
	<xsl:import href="utils/wb_utils.xsl" />
	<xsl:import href="utils/escape_js.xsl" />
	<xsl:import href="utils/wb_init_lab.xsl" />
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:output indent="yes" />
	<!-- ========================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main" />
		</html>
	</xsl:template>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_monitor_msg" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '756')" />
	<!-- ========================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="pragma" content="no-cache" />
			<title>
				<xsl:value-of select="$wb_wizbank" />
			</title>
			<link rel="stylesheet" href="../static/css/three.css"/>
			<link rel="stylesheet" href="../static/js/font-awesome/css/font-awesome.min.css"/>
			<script type="text/javascript" SRC="{$wb_js_path}jquery.min.js" LANGUAGE="JavaScript"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"/>
	
			<script language="JavaScript" src="{$wb_js_path}wb_track.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript" />
			<script language="JavaScript" src="{$wb_js_path}jquery.js" type="text/javascript" />
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
			<script language="JavaScript" type="text/javascript"><![CDATA[
				var req;
				prog_track = new  wbTrack		
				module_lst = new wbModule
				
				var mod_id = getParentUrlParam('mod_id');
				var isOnlineExam = ]]>'<xsl:value-of select="//isOnlineExam" />'<![CDATA[;
					
				function submitToServer(isColsed){
					unloadHandler(isColsed);
				}
				
				function unloadHandler(isColsed){
					var mod_type = getParentUrlParam('mod_type');
					var tkh_id = getParentUrlParam('tkh_id');
					
					if (isOnlineExam === 'true' && !pauseed) {
						//remove the learner from online exam learner list
						removeOnlineList();
					}
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
				
				function removeOnlineList() {
					url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'remove_from_online_list', 'mod_id', mod_id);
					$.ajax({
						url : url,
						success : function(req, options) {
							var ua = navigator.userAgent.toLowerCase();
							if (window.parent && window.parent.closed === false && !((ua.indexOf("android") > -1) || ((ua.match(/iphone/i)) || (ua.match(/ipod/i)) || (ua.match(/ipad/i))) || ua.match(/Windows Phone/i) || ua.match(/BlackBerry/i) || ua.match(/webOS/i))) {
								window.parent.close();
							}
						},
						method : 'GET'
					});
				}
				
				function unset_onunload() {
					window.onbeforeunload = null;
				}				
				
				function getHeartbeat() {
					req = getXMLHttpRequest();
					req.open("GET", wb_utils_invoke_servlet('cmd', 'heart_beat'), true);
					req.send();
				}				

				function init() {
					var cur_time = new Date()  
					document.frmXml.course_id.value = ]]>'<xsl:value-of select="//module/@course_id" />'<![CDATA[
					document.frmXml.lesson_id.value = ]]>'<xsl:value-of select="//module/@id" />'<![CDATA[	
					document.frmXml.tkh_id.value = ]]>'<xsl:value-of select="//module/@tkh_id" />'<![CDATA[
					document.frmXml.start_time.value = ]]>'<xsl:value-of select="//module/@start_time" />'<![CDATA[
					document.frmXml.cur_time.value = cur_time 
					document.frmXml.student_id.value = ]]>'<xsl:value-of select="//cur_usr/@ent_id" />'<![CDATA[
					document.frmXml.lesson_location.value = 'default'
					var mod_status = ']]><xsl:value-of select="/module/aicc_data/attempt/@status" /><![CDATA['
					if (mod_status == '') {
						document.frmXml.lesson_status.value = "I"
					} else {
						document.frmXml.lesson_status.value = mod_status
					}
					
					if (isOnlineExam === 'true') {
						timer = window.setInterval('getMessageFromSer()',10000);
					} else {
						window.setInterval("getHeartbeat()", 300000);
					}
				}
				
				function getMessageFromSer() {
					req = getXMLHttpRequest();
					req_url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'get_exam_msg', 'mod_id', mod_id);
					req.onreadystatechange = onChange;
					req.open("GET", req_url,true);
					req.send(null);
				}
				
				function onChange() {
					// only if req shows "loaded"
					if (req.readyState == 4) {
						// only if "OK"
						if (req.status == 200) {
							resultXml = req.responseXML;
							var exam_msg = resultXml.getElementsByTagName('exam_msg');
							var isPause, isTerminate, isZero, content;
							
							for (var i = 0, msg; msg = exam_msg[i]; i++) {
								isPause = msg.getElementsByTagName('isPause')[0].firstChild.nodeValue;
								wb_utils_set_cookie("test_disabled_alert_tip","yes");
								if (isPause === 'true') {
									//暂停测试
									window.parent.pauseed = true;
									window.parent.main.CwQuestionAutoSave(window.parent.main.currentQue, true);
									unset_onunload();
									window.parent.location = window.parent.get_pause_msg_url();
								} else {
									content = msg.getElementsByTagName('content')[0].firstChild.nodeValue;
									isTerminate = msg.getElementsByTagName('terminate_ind')[0].firstChild.nodeValue;
									if (isTerminate === 'true') { // 交卷
										window.parent.main.frmResult.terminate_exam_msg.value = content;
										window.parent.main.frmResult.isTerminateExam.value = isTerminate;
										
										isZero = msg.getElementsByTagName('zero_ind')[0].firstChild.nodeValue;
										if (isZero === 'true') { // 零分
											window.parent.main.frmResult.exam_mark_as_zero.value = isZero;
										}
										window.parent.main.CwQuestionSubmit(1);
									} else {
										// 发送消息
										//alert(']]><xsl:value-of select="$lab_monitor_msg" /><![CDATA[' + content);
										window.localStorage.setItem("serverChange",']]><xsl:value-of select="$lab_monitor_msg" /><![CDATA[' + content);
									}
								}
							}
						}
					}
				}
			]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}" />
<!-- 			<style type="text/css"> -->
<!-- 				body { -->
<!-- 					padding: 0; -->
<!-- 					margin: 0; -->
<!-- 				} -->
				
<!-- 				#top-bar { -->
<!-- 					text-align: right; -->
<!-- 					background: #2D2D2D; -->
<!-- 					border-bottom: 1px solid #000000; -->
<!-- 					padding: 3px 10px; -->
<!-- 					height: 75px; -->
<!-- 				} -->

<!-- 				#top-bar span{ -->
<!-- 					color: red; -->
<!-- 					font-size: 11pt; -->
<!-- 				} -->
<!-- 			</style> -->
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onload="init()">
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
				<input type="hidden" name="encrypted_start_time" value="{//module/@encrypted_start_time}" />
				<xsl:call-template name="wb_init_lab" />
			</form>
		</body>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info_1">在完成学习后请点击“离开”以记录学习进度。</xsl:with-param>
			<xsl:with-param name="lab_info_2">请勿直接关闭浏览器退出，</xsl:with-param>
			<xsl:with-param name="lab_info_3">请您必须点击“离开”按钮退出，才能记录你的学习进度。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info_1">在完成學習後請點擊“離開”以記錄學習進度。</xsl:with-param>
			<xsl:with-param name="lab_info_2">請勿直接關閉瀏覽器退出，</xsl:with-param>
			<xsl:with-param name="lab_info_3">請您必須點擊“離開”按鈕退出，才能記錄妳的學習進度。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_info_1">After completing your reading, click "Exit" to record your learning progress.</xsl:with-param>
			<xsl:with-param name="lab_info_2">Do not close the browser to exit, </xsl:with-param>
			<xsl:with-param name="lab_info_3">You must click the "Exit" button to exit in order to record your learning progress.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_info_1"/>
		<xsl:param name="lab_info_2"/>
		<xsl:param name="lab_info_3"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<span style="font-size:14px;display:block;color:#999;line-height:24px;"><xsl:value-of select="$lab_info_2" /></span>
				<span style="font-size:14px;display:block;line-height:24px;color:#999;"><xsl:value-of select="$lab_info_3" /></span>
			</xsl:with-param>
			<xsl:with-param name="new_template">true</xsl:with-param>
			<xsl:with-param name="close_but">true</xsl:with-param>
			<xsl:with-param name="upper_style">background:#fff;</xsl:with-param>
		</xsl:call-template>
<!-- 		<div id="top-bar"> -->
<!-- 			<table align="right"> -->
<!-- 				<tr> -->
<!-- 					<td align="left"> -->
<!-- 						<span><xsl:value-of select="$lab_info_1" /></span> -->
<!-- 						<br /> -->
<!-- 						<span><xsl:value-of select="$lab_info_2" /></span> -->
<!-- 					</td> -->
<!-- 					<td align="right" valign="top"> -->
						
<!-- 					</td> -->
<!-- 					<td align="right" valign="middle"> -->
<!-- 						<a href="javascript: submitToServer();"> -->
<!-- 							<img border="0" src="{$wb_img_path}exit_{$wb_lang}.png" /> -->
<!-- 						</a> -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 			</table> -->
<!-- 		</div> -->
	</xsl:template>
	<xsl:template match="paths" />
</xsl:stylesheet>

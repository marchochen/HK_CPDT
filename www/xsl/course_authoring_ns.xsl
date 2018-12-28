<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:output  indent="yes"/>
	<xsl:variable name="course_name" select="/course/header/title"/>
	<xsl:variable name="is_new_cos">
		<xsl:choose>
			<xsl:when test="/course/is_new_cos/text() ='true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="itm_type" select="//itm_action_nav/@itm_type"/>
	<!-- =wizBank V3 start============================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_ist_home">教師平台</xsl:with-param>
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_content">网上內容</xsl:with-param>
			<xsl:with-param name="lab_run_info">班別訊息</xsl:with-param>
			<xsl:with-param name="lab_next_btn">下一步</xsl:with-param>
			<xsl:with-param name="lab_ok_btn">完成</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_ist_home">教师平台</xsl:with-param>
			<xsl:with-param name="lab_course_list">课程管理</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_content">网上内容</xsl:with-param>
			<xsl:with-param name="lab_next_btn">下一步</xsl:with-param>
			<xsl:with-param name="lab_ok_btn">完成</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_ist_home">Lecturer home</xsl:with-param>
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_content">Course content</xsl:with-param>
			<xsl:with-param name="lab_run_info">Course information</xsl:with-param>
			<xsl:with-param name="lab_next_btn">下一步</xsl:with-param>
			<xsl:with-param name="lab_ok_btn">OK</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="course">
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_next_btn"/>
		<xsl:param name="lab_ok_btn"/>
		<head>
			<TITLE>
				<xsl:value-of select="//course/header/title/text()"/>
			</TITLE>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_scorescheme.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			wiz = new wbCosWizard
			itm = new wbItem
			cmt = new wbScoreScheme


course_name = ]]>'<xsl:call-template name="escape_js"><xsl:with-param name="input_str" select="$course_name"/></xsl:call-template>'<![CDATA[

wb_utils_set_cookie('course_id', ]]><xsl:value-of select="//course/@id"/><![CDATA[)

wb_utils_set_cookie('title_prev',course_name);
wiz.set_cos_id (]]><xsl:value-of select="//course/@id"/><![CDATA[)

course_lst = new wbCourse; 
cos_id = ]]><xsl:value-of select="//course/@id"/><![CDATA[
course_info_url = course_lst.view_info_url(cos_id,']]><xsl:value-of select="//course/@type"/><![CDATA[');

// save cos_struct_xml
course_timestamp = '';
course_struct_xml_cnt = '';
course_struct_xml_1 = '';

// for del module only
mod_id = '';
mod_timestamp='';

// from JS to Applet
function addNode(node_title, mod_type, mod_id, course_timestamp) {
	self.frames["NavPage"].addNode(node_title, mod_type, mod_id, course_timestamp);
	window.scrollTo(0,0);
}

function editNode(node_title, course_timestamp) {
	self.frames["NavPage"].editNode(node_title, course_timestamp);
	window.scrollTo(0,0);
}

function cancelAdd() {
	self.frames["NavPage"].cancelAdd();
	window.scrollTo(0,0);
}

function cancelEdit() {
	self.frames["NavPage"].cancelEdit();
	window.scrollTo(0,0);
}

function cancelNav() {
	self.frames["NavPage"].cancelNav();
	window.scrollTo(0,0);
}

function cancelDelete() {
	self.frames["NavPage"].cancelDelete();
}

function confirmDelete(course_timestamp) {
	self.frames["NavPage"].confirmDelete(course_timestamp);
}

function set_cos_timestamp(course_timestamp) {
	self.frames["NavPage"].set_cos_timestamp(course_timestamp);
}

// from Applet to JS
//function deleteModule(){
//	alert("delete mod 1 ...");
//	self.frames["ContentPage"].del_module();
//	alert("delete mod 2 ...");
//}

// from JS to JS
function set_del_module_var(id, status, timestamp, attempted, lang) {
	self.frames["NavPage"].set_del_module_var(id, status, timestamp, attempted, lang);
}

// show page
function show_frame_content(myURL) {
	window.frames["ContentPage"].location = myURL;
//	document.all.ContentPage.src = myURL;
}

function saveCourseStructXML(in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
	window.frames["CallServletPage"].location = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','saveCosStructXML.xsl');
	course_timestamp = in_course_timestamp;
	course_struct_xml_cnt = in_course_struct_xml_cnt;
	course_struct_xml_1 = in_course_struct_xml_1;
}

function saveCourseStructSuccess() {
	self.frames["NavPage"].saveCourseStructSuccess();
}

function saveCourseStructFailure() {
	self.frames["NavPage"].saveCourseStructFailure();
}

function mod_edit_mode(myURL, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
	window.frames["ContentPage"].location = myURL;
	course_timestamp = in_course_timestamp;
	course_struct_xml_cnt = in_course_struct_xml_cnt;
	course_struct_xml_1 = in_course_struct_xml_1;
}

function add_module(myURL, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
	course_timestamp = in_course_timestamp;
	course_struct_xml_cnt = in_course_struct_xml_cnt;
	course_struct_xml_1 = in_course_struct_xml_1;
	window.frames["ContentPage"].location = myURL;
}

function delete_module(in_mod_id, in_mod_timestamp, in_course_id, in_course_timestamp, in_course_struct_xml_cnt, in_course_struct_xml_1) {
	course_timestamp = in_course_timestamp;
	course_struct_xml_cnt = in_course_struct_xml_cnt;
	course_struct_xml_1 = in_course_struct_xml_1;
	mod_id = in_mod_id;
	mod_timestamp = in_mod_timestamp;
	window.frames["ContentPage"].location = wb_utils_invoke_servlet('cmd','get_prof','stylesheet','delModule.xsl')
}

function get_cos_timestamp() {
	window.frames["CallServletPage"].location = wb_utils_invoke_servlet('cmd','get_cos_header','course_id',cos_id,'stylesheet','get_cos_timestamp.xsl','isExcludes',true)
}

// finish loading the applet
function finish_loading() {
//	document.all.LoadPage.style.left = '2000px';
//	document.all.NavPage.style.left = '0px';
}

screen_width = 984;
screen_height = 600;

applet_width = 250;
applet_height = screen_height - 180;
content_width = screen_width - applet_width -  1;
content_height = screen_height - 180;
save_width = "0";
//save_height = screen_height - 180;
save_height = "0";

//window.onresize = resizeWindow;

	function resizeWindow() {
		window.resizeTo(820, 420);
	}

	function showIframe(){
		with (document) {
			wb_standard_width = ]]><xsl:value-of select="$wb_gen_table_width"/><![CDATA[;
			var url_nav = wb_utils_invoke_servlet('cmd','get_cos','course_id',]]><xsl:value-of select="//course/@id"/><![CDATA[,'cos_type',']]><xsl:value-of select="//course/@type"/><![CDATA[','stylesheet','ca_navigation_ns.xsl','url_failure','about:blank')
						
			document.getElementById('NavPage').src = url_nav;
			document.getElementById('ContentPage').src = course_info_url;
			document.getElementById('NavPage').width = '100%';
			document.getElementById('ContentPage').width = '100%';
			document.getElementById('CallServletPage').width = save_width;
			$("#CallServletPage").hide();
		}
	}
	
	function resize(){
		with (document) {
			setIframeHeight(document.getElementById('ContentPage'));                 
		}
	}
	
	function setIframeHeight(iframe) {
			var iframe_table_height = 150;
		  //设置左边树iframe的高度
			$('#NavPage').load(function() {
			    var iframeHeight = Math.min(iframe.contentWindow.window.document.documentElement.scrollHeight, iframe.contentWindow.window.document.body.scrollHeight);  
			    var h=$(this).contents().height(); 
			   
			    $(this).height(h +'px');   
			    
			    if( parseInt(h) > parseInt(iframe_table_height)){
			    	iframe_table_height = h;
			    }
			});  
	       //设置右边内容iframe的高度
			$('#ContentPage').load(function() { 
			    var iframeHeight = Math.min(iframe.contentWindow.window.document.documentElement.scrollHeight, iframe.contentWindow.window.document.body.scrollHeight);  
			    var h=$(this).contents().height(); 
			    $(this).height((h)+'px'); 
			    if( parseInt(h) > parseInt(iframe_table_height)){
			    	iframe_table_height = h;
			    }
			}); 
			
	
	/*
	        //设置包住iframe 的table的高度
			$('#iframe_table').load(function() { //方法1 
			    alert(iframe_table_height)
			    $(this).height(iframe_table_height +'px');  
			}); 
		*/
	}
	
	function SetCwinHeight(obj) {
		var iframeid;
		if(obj==1){
			 iframeid = document.getElementById("NavPage"); //iframe id
		}else{
			 iframeid = document.getElementById("ContentPage"); //iframe id
			}
		var iframeHeight2 = Math.min(iframeid.contentWindow.window.document.documentElement.scrollHeight, iframeid.contentWindow.window.document.body.scrollHeight);  
			iframeid.height = "1px";//先给一个够小的初值,然后再长高 
			if (document.getElementById) {
				if (iframeid && !window.opera) {
					if (iframeid.contentDocument && iframeHeight2) {
				/* 980是右边table初始化load的页面高度 && 1411是左边Nag初始化load的页面高度 
					初始化时，table内整体设置成为480固定高度
					*/
				//		if(iframeHeight2<981 || iframeHeight2<1412 ) iframeHeight2=580;
						iframeHeight2=580
						iframeid.height =  iframeHeight2; 
					}
			} 
		} 
	} 
	

]]></SCRIPT>
		<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="showIframe()">
	<!-- 	<xsl:if test="is_new_cos/text() = 'false'">
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">		
				<xsl:variable name="nav_node_id">102</xsl:with-param>
		</xsl:call-template>
		</xsl:if>
		 -->
		<div class="wzb-item-main">
		
			<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="width">100%</xsl:with-param>
			<xsl:with-param name="wihe_class"></xsl:with-param>
				<xsl:with-param name="text">
					<xsl:value-of select="//itm_action_nav/@itm_title"/>
				</xsl:with-param>
			</xsl:call-template>
			<!-- 
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<xsl:apply-templates select="nav/item" mode="nav">
						<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
					</xsl:apply-templates>
					<span class="NavLink">
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_content"/>
					</span>
				</xsl:with-param>
			</xsl:call-template>
 -->
<!--
		<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			var url_nav = wb_utils_invoke_servlet('cmd','get_cos','course_id',]]><xsl:value-of select="//course/@id"/><![CDATA[,'cos_type',']]><xsl:value-of select="//course/@type"/><![CDATA[','stylesheet','ca_navigation_ns.xsl','url_failure','about:blank')
			str=''
			str+='<frameset rows="*" cols="' + save_width + ',' + applet_width + ',' + content_width + '" frameborder="yes" border="10"  framespacing="0">'
			str+='<frame src="../htm/empty.htm" scrolling="NO" noresize name="CallServletPage">'
			str+='<frame src="'  + url_nav + '" frameborder=0 noresize scrolling="NO" name="NavPage">'
			str+='<frame src="' + course_info_url + '" noresize scrolling="AUTO" name="ContentPage">'
			str+='</frameset>'
			document.write(str)
]]></SCRIPT>-->
			<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="margin-top28" id = "iframe_table">
				<tr>
					<td valign="top" width="0%" >
						<iframe id="CallServletPage" src="../htm/empty.htm" scrolling="NO" name="CallServletPage" />
					</td>
					<td valign="top" width="22%" >
						<iframe id="NavPage"  name="NavPage" height="550" frameborder="0" scrolling="no" src="../htm/empty.htm" marginwidth="0" marginheight="0"  />
					</td>
					<td valign="top" width="78%" >
						<iframe id="ContentPage" style="position: absolute;left: 25%;width: 75%;" name="ContentPage"   height="550" frameborder="0"    scrolling="auto"   src="../htm/empty.htm" marginwidth="0" marginheight="0"  />
					</td>
					<script>
						$(function(){  //ie.foxfix.google 滚动条
							function arr(){
								if(navigator.appName == "Microsoft Internet Explorer"){
									document.getElementById('ContentPage').style.height=document.body.clientHeight-68;
									
								} else{
									document.getElementById('ContentPage').style.height=($(window).height())-68+'px';
								}
							}
							document.getElementById('ContentPage').style.height=($(window).height())-68+'px';
							$("#NavPage").height($(window).height()-68)	
							arr();			
							window.onresize=function(){
							$("#NavPage").height($(window).height()-68)	
								arr();	
							}
						})
					</script>
				</tr>
			</table>
			<xsl:if test="$is_new_cos='true'">
			<xsl:choose>
				<xsl:when test="$itm_type = 'AUDIOVIDEO'">
					<div class="wzb-bar">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_ok_btn"></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm.get_item_detail(<xsl:value-of select="//course/@itm_id"/>)</xsl:with-param>
						</xsl:call-template>
					</div>
					</xsl:when>
				<xsl:otherwise>
					<div class="wzb-bar">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_next_btn"></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:cmt.get_criteria(<xsl:value-of select="//course/@itm_id"/>,'<xsl:value-of select="$is_new_cos"/>')</xsl:with-param>
						</xsl:call-template>
		
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_ok_btn"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm.get_item_detail(<xsl:value-of select="//course/@itm_id"/>)</xsl:with-param>
						</xsl:call-template>
					</div>
				</xsl:otherwise>
			</xsl:choose>
			</xsl:if>
		</div>
		</body>
	</xsl:template>
	<xsl:template match="env"/>
	<xsl:template match="cur_usr"/>
	<xsl:template match="header"/>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm.get_item_run_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_run_info"/>
				</a>
				<span class="NavLink">&#160;&gt;&#160;</span>
				<a href="javascript:itm.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>

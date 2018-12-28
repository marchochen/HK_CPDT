<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/tst_ins_upd_utils.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	
	<xsl:output indent="yes"/>
	<!-- ======================================================================= -->
	<xsl:variable name="res_enable">
	<xsl:choose>
		<xsl:when test="count(//cur_usr/granted_functions/functions/function[@id='LRN_RES_MAIN']) = 0">false</xsl:when>
		<xsl:otherwise>true</xsl:otherwise>
	</xsl:choose>	
	</xsl:variable>	
	<xsl:variable name="mod_privilege">
		<xsl:choose>
			<xsl:when test="//cur_usr/@root_display = $root_cw">CW</xsl:when>
			<xsl:otherwise>AUTHOR</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="mod_id" select="/module/@id"/>
	<xsl:variable name="usr_id" select="/module/cur_usr/@id"/>
	<xsl:variable name="status" select="/module/header/@status"/>
	<xsl:variable name="attp" select="/module/header/@attempted"/>
	<xsl:variable name="tpl_type" select="/module/header/@type"/>	
	<xsl:variable name="mod_type" select="/module/header/@type"/>
	<xsl:variable name="mod_subtype" select="/module/header/@subtype"/>
	<xsl:variable name="title" select="/module/header/title"/>
	<xsl:variable name="source_link" select="/module/header/source"/>
	<xsl:variable name="source_type" select="/module/header/source/@type"/>
	<xsl:variable name="desc" select="/module/header/desc"/>
	<xsl:variable name="annotation" select="/module/header/annotation"/>
	<xsl:variable name="inst" select="/module/header/instruction"/>
	<xsl:variable name="diff" select="/module/header/@difficulty"/>
	<xsl:variable name="dur" select="/module/header/@duration"/>
	<xsl:variable name="template" select="/module/header/template_list/@cur_tpl"/>
	<xsl:variable name="timestamp" select="/module/@timestamp"/>
	<xsl:variable name="language" select="/module/@language"/>
	<xsl:variable name="res_instr_name" select="/module/header/instructor"/>
	<xsl:variable name="res_moderator" select="/module/header/moderator "/>
	<xsl:variable name="res_organization" select="/module/header/organization "/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:variable name="max_usr_attempt_num" select="/module/header/@max_usr_attempt"/>
	<xsl:variable name="cos_type" select="/module/course/@type"/>
	<xsl:variable name="isEnrollment_related">
	<xsl:choose>
		<xsl:when test="not (/module/enrollment_related)">0</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="/module/enrollment_related"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<!-- ======================================================================= -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="module"/>
		</html>
	</xsl:template>
	<!-- ======================================================================= -->
	<xsl:template match="module">
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- ======================================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">基本資料</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_mod_title">標題</xsl:with-param>
			<xsl:with-param name="lab_grading_policy">評分制度</xsl:with-param>
			<xsl:with-param name="lab_due_date">截止日期</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">天(從課程開始日期算起)</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(非強制)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_1">對於面授培訓，課程的開始日期是班別的開始日期。</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_2">對於網上課程，課程的開始日期是學員成功報讀的日期。</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">未指定</xsl:with-param>
			<xsl:with-param name="lab_display_option">顯示設置</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_online">已發佈</xsl:with-param>
			<xsl:with-param name="lab_offline">未發佈</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始時間</xsl:with-param>
			<xsl:with-param name="lab_immediate">即時</xsl:with-param>
			<xsl:with-param name="lab_end_date">結束時間</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">儲存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">基本资料</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_mod_title">标题</xsl:with-param>
			<xsl:with-param name="lab_grading_policy">评分制度</xsl:with-param>
			<xsl:with-param name="lab_due_date">作业提交截止时间</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">天(从课程开始日期算起)</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(非强制)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_1">对于面授课堂，课程的开始日期是班级的开始日期。</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_2">对于网上课程，课程的开始日期是注册确认日期。</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">未指定</xsl:with-param>
			<xsl:with-param name="lab_display_option">显示设置</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_online">已发布</xsl:with-param>
			<xsl:with-param name="lab_offline">未发布</xsl:with-param>
			<xsl:with-param name="lab_start_date">开始时间</xsl:with-param>
			<xsl:with-param name="lab_immediate">即时</xsl:with-param>
			<xsl:with-param name="lab_end_date">结束时间</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">保存</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">Basic information</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_mod_title">Title</xsl:with-param>
			<xsl:with-param name="lab_grading_policy">Grading policy</xsl:with-param>
			<xsl:with-param name="lab_due_date">Due date</xsl:with-param>
			<xsl:with-param name="lab_due_date_num">days since the course started.</xsl:with-param>
			<xsl:with-param name="lab_due_date_non_obligatory">(non-obligatory)</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_1">For Classroom Course, the course start date is the class start date.</xsl:with-param>
			<xsl:with-param name="lab_due_date_num_inst_2">For Web-based Course, the course start date is the enrollment confirmation date.</xsl:with-param>
			<xsl:with-param name="lab_ass_due_date_unspecified">Unspecified</xsl:with-param>
			<xsl:with-param name="lab_display_option">Release schedule</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_online">Published</xsl:with-param>
			<xsl:with-param name="lab_offline">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_start_date">Available after</xsl:with-param>
			<xsl:with-param name="lab_immediate">Immediate</xsl:with-param>
			<xsl:with-param name="lab_end_date">Available until</xsl:with-param>
			<xsl:with-param name="lab_unlimit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ======================================================================= -->
	<xsl:template name="content">
		<xsl:param name="lab_basic_info"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_mod_title"/>
		<xsl:param name="lab_grading_policy"/>
		<xsl:param name="lab_due_date"/>
		<xsl:param name="lab_due_date_num"/>
		<xsl:param name="lab_due_date_non_obligatory"/>
		<xsl:param name="lab_due_date_num_inst_1"/>
		<xsl:param name="lab_due_date_num_inst_2"/>
		<xsl:param name="lab_ass_due_date_unspecified"/>
		<xsl:param name="lab_display_option"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_media.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_resource.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_aicc.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
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
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				
			var doAll = (document.all!=null) // IE
			var doDOM =(document.getElementById!=null) //Netscape 6 DOM 1.0
			var doLayer =(document.layers!=null) // Netscape 4.x
			
			mod = new wbModule;
			cos = new wbCourse; 		
			res = new wbResource
			cos_id = getUrlParam("course_id")   	
			cos_name = wb_utils_get_cookie("title_prev")
			wb_utils_set_cookie('mod_type',']]><xsl:value-of select="$mod_subtype"/><![CDATA[')
			tpl_type = ']]><xsl:value-of select="$mod_subtype"/><![CDATA['							
			var hide_content = ''	
			
			//--==Man: For Dynamic Source Type radio button , don't remove
			var src_type_wizpack_id 
			var src_type_file_id 
			var src_type_zipfile_id 
			var src_type_aicc_id 
			var src_type_url_id
			var src_type_ass_inst_id 
			var src_type_pick_aicc_res_id 
			var src_type_pick_res_id 
			var src_type_res_id 
			//--===		
			
			function togStatus(ratingQue){
				frmXml.mod_has_rate_q.value = ratingQue;
			}
					
			function writeElement(id, contents) {
				if(doAll){
	        		var obj= document.all[id]
				}else if(doDOM){
					var obj = document.getElementById(id)
				}
	        	if (obj!=null){
	            obj.innerHTML = contents;
				}
			}
			 			
			function GetCSSPElement(id) {
				if (doAll) {
					return document.all[id];
				}else{
					if (doDOM)
						return document.getElementById(id)
					else
			          return document.layers[id];
				}
			}

			function AssWriteElement(id, contents) {
			        var pEl = GetCSSPElement(id)
			        if (pEl!=null)
			          if (doAll || doDOM)
			            pEl.innerHTML = contents
			          else {
			            pEl.document.open()
			            pEl.document.write(contents) 
			            pEl.document.close()
			          }
			}
	
			function ins_default_date(){
				var frm = document.frmXml
				//Get the server current time
				str = "]]><xsl:value-of select="header/@cur_time"/><![CDATA["
				cur_day = str.substring((str.lastIndexOf('-') + 1), str.indexOf(' '))
				cur_month = str.substring((str.indexOf('-') + 1), str.lastIndexOf('-'))
				cur_year = str.substring(0, str.indexOf('-'))
				
				]]><xsl:if test="( header/@subtype != 'CHT' and header/@subtype  != 'VCR') or translate(substring-before(header/@cur_time, '.'), ':- ', '') &lt; translate(substring-before(header/@eff_start_datetime, '.'), ':- ', '')"><![CDATA[
				if (frm.start_date){

					if (frm.start_date[0].checked == true){
						frm.start_mm.value = cur_month
						frm.start_yy.value = cur_year
						
						frm.start_hour.value = "00"
						frm.start_min.value = "00"
					}
				}
				]]></xsl:if><![CDATA[
				
				if (frm.end_date){					
					if (frm.end_date[0].checked == true){
		//				frm.end_mm.value = cur_month
		//				frm.end_yy.value = cur_year
						
						frm.end_hour.value = "23"
						frm.end_min.value = "59"
					}
				}
			}
				
			function isMaxUpload(){
				if ( document.frmXml.max_uplaod_file[0].checked == true )
					document.frmXml.ass_max_upload.value = ''
			}
				
			function putHTMLvalue(){
				if(document.forms[0].mod_instr.type!="textarea"){
				var myText = getText()
					if(myText.length != 0){
						document.forms[0].mod_instr.value = getHTML();
					}
				}
			}
			
			function chg_max_attp_num(frm){
				frm.max_usr_attempt_unlimited_num.value = '';	
			}
			
			function chg_due_date_ts(frm){
				frm.due_dd.value = ''
				frm.due_mm.value = ''
				frm.due_yy.value = ''
				frm.due_hour.value = ''
				frm.due_min.value = ''
			}
			
			function chg_due_date_day(frm){
				frm.ass_due_date_day.value = ''
			}
			
			function invoke_after_pick(){
				var imgpath = ]]>'<xsl:value-of select="$wb_img_path"/>'<![CDATA[
				var frm = document.frmXml;
				
				if(frm.source_type && frm.source_content && document['src_type_img']){
				
					var source_type = frm.source_type.value;
					var source_content = frm.source_content.value;
					var file_ext = get_file_ext(source_content);
					if(frm.src_type_display){
						frm.src_type_display.value = frm.source_content.value;
					}
					/*if(frm.source_content.value.length > 60){
						var display_str = frm.source_content.value.substring(0,30) + "..." + frm.source_content.value.substring( frm.source_content.value.length - 27, frm.source_content.value.length)
					}else{
						var display_str = frm.source_content.value;
					}
					var contents = "<a class=\"Text\" href=\"" +frm.source_content.value + "\" target=\"_blank\" >" +display_str+ "</a>";
					writeElement('src_display',contents)
					*/
					if(source_content.search('http://') != -1){
						document['src_type_img'].src = imgpath + 'icon_web_browser.gif';
					}					
					switch (file_ext){
						
							case 'doc' :
								document['src_type_img'].src = imgpath + 'icon_word.gif';
							break;
							case 'ppt' :
								document['src_type_img'].src = imgpath + 'icon_ppt.gif';
							break;			
							case 'txt' :
								document['src_type_img'].src = imgpath + 'icon_notepad.gif';
							break;														
							case 'html' :
								document['src_type_img'].src = imgpath + 'icon_web_browser.gif';
							break;									
							case 'htm' :
								document['src_type_img'].src = imgpath + 'icon_web_browser.gif';
							break;	
							case 'swf' :
								document['src_type_img'].src = imgpath + 'icon_swf.gif';
							break;		
							case 'gif' :
								document['src_type_img'].src = imgpath + 'icon_gif.gif';
							break;		
							case 'jpg' :
								document['src_type_img'].src = imgpath + 'icon_jpg.gif';
							break;		
							case 'exe' :
								document['src_type_img'].src = imgpath + 'icon_exe.gif';
							break;	
							case 'xls' :
								document['src_type_img'].src = imgpath + 'icon_xls.gif';
							break;	
							case 'pdf' :
								document['src_type_img'].src = imgpath + 'icon_pdf.gif';
							break;																																												
												
					}
				}
				
			}
			
			function get_file_ext(href){
				return href.substring(href.lastIndexOf('.') + 1,href.length)
			}	
		]]></script>
		<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onunload="wb_utils_close_preloading()" onLoad="ins_default_date()">
			<form name="frmXml" enctype="multipart/form-data">
				<!-- hidden field -->
				<xsl:if test="$mod_subtype='SVY'">
					<input type="hidden" name="mod_max_usr_attempt" value="1"/>
					<input type="hidden" name="mod_has_rate_q">
						<xsl:choose>
							<xsl:when test="header/@has_rate_q='true'">
								<xsl:attribute name="value">true</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="value">false</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>
					</input>
				</xsl:if>
				<xsl:if test="$mod_subtype = 'AICC_AU'">
					<input value="no" name="rename" type="hidden"/>
				</xsl:if>
				<xsl:if test="$mod_subtype = 'NETG_COK'">
					<input value="no" name="rename" type="hidden"/>
					<input name="mod_duration" value="{$dur}" type="hidden"/>
				</xsl:if>
				<input type="hidden" name="annonation_html">
					<xsl:attribute name="value"><xsl:if test="/module/header/annotation/html">y</xsl:if></xsl:attribute>
				</input>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_basic_info"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
					<xsl:call-template name="table_seperator"/>
					<!-- Module Type -->
					<xsl:call-template name="draw_mod_type">
						<xsl:with-param name="lab_type" select="$lab_type"/>
						<xsl:with-param name="mod_type" select="$mod_subtype"/>
						<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"/>
					</xsl:call-template>
					<!-- Module Title -->
					<xsl:call-template name="draw_title">
						<xsl:with-param name="lab_title" select="$lab_mod_title"/>
						<xsl:with-param name="value" select="$title"/>
						<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"/>
					</xsl:call-template>
					<xsl:call-template name="table_seperator"/>
				</table>
				<xsl:call-template name="wb_ui_space">
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
				</xsl:call-template>
				<!-- -->
				<xsl:choose>
					<xsl:when test="$mod_subtype = 'ASS'">
						<xsl:call-template name="wb_ui_space"/>
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text">
								<xsl:value-of select="$lab_grading_policy"/>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line"/>
						<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
							<xsl:call-template name="table_seperator"/>
							<!-- Due Date-->
							<xsl:call-template name="draw_ass_due_date">
								<xsl:with-param name="lab_ass_due_date_unspecified" select="$lab_ass_due_date_unspecified"/>
								<xsl:with-param name="lab_due_date" select="$lab_due_date"/>
								<xsl:with-param name="lab_due_date_num" select="$lab_due_date_num"/>
								<xsl:with-param name="lab_due_date_non_obligatory" select="$lab_due_date_non_obligatory"/>
								<xsl:with-param name="lab_due_date_num_inst_1" select="$lab_due_date_num_inst_1"/>
								<xsl:with-param name="lab_due_date_num_inst_2" select="$lab_due_date_num_inst_2"/>
								<xsl:with-param name="due_date_day" select="header/@due_date_day"/>
								<xsl:with-param name="due_datetime" select="header/@due_datetime"/>
								<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="table_seperator"/>
						</table>
						<xsl:call-template name="wb_ui_space"/>
					</xsl:when>
					<xsl:when test="$mod_subtype = 'AICC_AU'">
					<input type="hidden" name="src_type" value="AICC_FILES"/>
					</xsl:when>
				</xsl:choose>
				<!-- -->
				<xsl:if test="display/option/datetime/@eff_end = 'true' or display/option/general/@status = 'true' or display/option/datetime/@eff_start = 'true'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text" select="$lab_display_option"/>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:call-template name="table_seperator"/>
						<xsl:call-template name="draw_status">
							<xsl:with-param name="lab_status" select="$lab_status"/>
							<xsl:with-param name="lab_online" select="$lab_online"/>
							<xsl:with-param name="lab_offline" select="$lab_offline"/>
							<xsl:with-param name="status" select="$status"/>
							<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
						</xsl:call-template>							
						
						<xsl:choose>
							<xsl:when test="$cos_type = 'AUDIOVIDEO' ">	
							
								<input type="hidden" name="mod_eff_start_datetime" value="IMMEDIATE"/>
								<input type="hidden" name="mod_eff_end_datetime" value="UNLIMITED"/>				

							</xsl:when>
							<xsl:otherwise>
									<xsl:choose>
									<xsl:when test="display/option/datetime/@eff_start = 'true'">
										<xsl:variable name="current_time" select="translate(substring-before(header/@cur_time, '.'), ':- ', '')"/>
										<xsl:variable name="start_time" select="translate(substring-before(header/@eff_start_datetime, '.'), ':- ', '')"/>
										<input type="hidden" name="cur_dt" value="{$current_time}"/>
										<input type="hidden" name="eff_dt" value="{$start_time}"/>
										<xsl:call-template name="draw_effective_start">
											<xsl:with-param name="lab_start_date" select="$lab_start_date"/>
											<xsl:with-param name="lab_immediate" select="$lab_immediate"/>
											<xsl:with-param name="timestamp" select="header/@eff_start_datetime"/>
											<xsl:with-param name="readonly">false</xsl:with-param>
											<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<input type="hidden" name="mod_eff_start_datetime" value="{header/@eff_start_datetime}"/>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="display/option/datetime/@eff_end = 'true'">
										<xsl:call-template name="draw_effective_end">
											<xsl:with-param name="lab_end_date" select="$lab_end_date"/>
											<xsl:with-param name="lab_unlimit" select="$lab_unlimit"/>
											<xsl:with-param name="timestamp" select="header/@eff_end_datetime"/>
											<xsl:with-param name="isEnrollment_related" select="$isEnrollment_related"></xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<input type="hidden" name="mod_eff_end_datetime" value="{header/@eff_end_datetime}"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:call-template name="table_seperator"/>
					</table>
				</xsl:if>
				<!--  -->
				<xsl:call-template name="wb_ui_line"/>
				<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr>
						<td height="19" align="center">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:mod.upd_exec2(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							</xsl:call-template>
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:window.parent.cancelEdit();window.location.href=mod.view_info_url(<xsl:value-of select="$mod_id"/>)</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</table>
				<script language="JavaScript">
			<![CDATA[
			with (document) {	
					write("<input type=\"hidden\" name=\"url_success\" value=\"" + gen_get_cookie('url_success') +"\">")	
					write('<input type="hidden" name="url_failure" value="' +gen_get_cookie('url_failure')+'"/>')				
			}
		//-->]]></script>
				<input type="hidden" name="max_usr_attempt"/>
				<input type="hidden" name="cmd" value="upd_mod2"/>
				<input type="hidden" name="mod_id" value="{$mod_id}"/>
				<input type="hidden" name="mod_type" value="{$mod_type}"/>
				<input type="hidden" name="mod_subtype" value="{$mod_subtype}"/>
				<input type="hidden" name="mod_privilege" value="{mod_privilege}"/>
				<input type="hidden" name="mod_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="annotation_html" value=""/>
				<input type="hidden" name="copy_media_from" value=""/>
				<xsl:if test="($mod_subtype != 'VCR' )and ($mod_subtype!='AICC_AU')">
					<input type="hidden" name="mod_src_type" value="{header/@res_src_type}"/>
				</xsl:if>
				<input type="hidden" name="course_timestamp" value=""/>
				<input type="hidden" name="course_struct_xml_cnt" value=""/>
				<input type="hidden" name="course_struct_xml_1" value=""/>
				<xsl:if test="($mod_subtype = 'NETG_COK') or ($mod_subtype = 'SCO')">
					<input name="mod_src_link" type="hidden" value="{header/@res_src_link}"/>
				</xsl:if>
			</form>
		</body>
	</xsl:template>
	<!-- ======================================================================= -->
</xsl:stylesheet>

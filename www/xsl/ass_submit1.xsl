<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">

	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_id" select="/module/@id"/>
	<xsl:variable name="usr_id" select="/module/cur_usr/@id"/>
	<xsl:variable name="max_upload" select="/module/header/@max_upload"/>
	<xsl:variable name="tkh_id" select="/module/@tkh_id"/>
	
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
	
	<!-- =========================== Label =========================== -->
	<xsl:variable name="no_select_file" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'no_select_file')"/>
	<xsl:variable name="usr_browse" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'usr_browse')"/>
	
	<!-- =============================================================== -->
	<xsl:template match="module">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
<!-- 			<xsl:call-template name="wb_css"> -->
<!-- 				<xsl:with-param name="view">wb_ui</xsl:with-param> -->
<!-- 			</xsl:call-template> -->
			<xsl:call-template name="new_css">
			</xsl:call-template>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<link rel="stylesheet" href="../static/css/three.css"/>
		 <!--   <link rel="stylesheet" href="../static/js/font-awesome/css/font-awesome.min.css"/> -->
			<script type="text/javascript" SRC="{$wb_js_path}jquery.min.js" LANGUAGE="JavaScript"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_media.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
ass = new wbAssignment
var max_upload = ]]><xsl:value-of select="count(header/Description/body)"/><![CDATA[;

]]></SCRIPT>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onunload="wb_utils_close_preloading()">
			<form name="ass_submit" enctype="multipart/form-data">
				<xsl:if test="$max_upload = '-1'">
					<input type="hidden" name="rename" value="N"/>
				</xsl:if>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作業提交</xsl:with-param>
			<xsl:with-param name="lab_step1">第一步</xsl:with-param>
			<xsl:with-param name="lab_required">為必填</xsl:with-param>
			<xsl:with-param name="lab_file_upload">上傳檔案</xsl:with-param>
			<xsl:with-param name="lab_inst">請按下面的 "新增" 按鈕上傳你的作業。</xsl:with-param>
			<xsl:with-param name="lab_inst3">你可在所選檔案旁的空格內輸入該檔案的簡介。</xsl:with-param>
			<xsl:with-param name="lab_file">檔案</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_inst2">請按下面的 "瀏覽" 按鈕上傳你的作業。</xsl:with-param>
			<xsl:with-param name="lab_inst4">你可在所選檔案旁的空格內輸入該檔案的簡介。</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">已上傳檔案</xsl:with-param>
			<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
			<xsl:with-param name="lab_step2">第二步</xsl:with-param>
			<xsl:with-param name="lab_keep_existing_file">保留現有檔案</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev_step">上一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next_step">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作业提交</xsl:with-param>
			<xsl:with-param name="lab_step1">第一步</xsl:with-param>
			<xsl:with-param name="lab_required">为必填</xsl:with-param>
			<xsl:with-param name="lab_file_upload">上传文档</xsl:with-param>
			<xsl:with-param name="lab_inst">请按下面的"浏览"按钮上传作业。</xsl:with-param>
			<xsl:with-param name="lab_inst3">你可以为上传的文件输入简介。</xsl:with-param>
			<xsl:with-param name="lab_file">文档</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_inst2">请按下面的"浏览"按钮上传作业。</xsl:with-param>
			<xsl:with-param name="lab_inst4">你可以为上传的文件输入简介。</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">已上传文档</xsl:with-param>
			<xsl:with-param name="lab_change_to"> 更改为</xsl:with-param>
			<xsl:with-param name="lab_step2">第二步</xsl:with-param>
			<xsl:with-param name="lab_keep_existing_file">保留现有文件。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev_step">上一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next_step">下一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">Assignment submission</xsl:with-param>
			<xsl:with-param name="lab_step1">Step 1</xsl:with-param>
			<xsl:with-param name="lab_required">Required</xsl:with-param>
			<xsl:with-param name="lab_file_upload">File upload</xsl:with-param>
			<xsl:with-param name="lab_inst">Please upload your assignment file(s) using the "<b>Add</b>" button below.</xsl:with-param>
			<xsl:with-param name="lab_inst3">You may enter description of the file(s) in the box next to it.</xsl:with-param>
			<xsl:with-param name="lab_file">File</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_inst2">Please upload your assignment files using the "<b>Browse...</b>" button below.</xsl:with-param>
			<xsl:with-param name="lab_inst4">You may enter description of the file(s) in the box next to it.</xsl:with-param>
			<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">Upload file</xsl:with-param>
			<xsl:with-param name="lab_step2">Step 2</xsl:with-param>
			<xsl:with-param name="lab_keep_existing_file">Keep existing file.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev_step">Prev step</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_next_step">Next step</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="header">
		<xsl:param name="lab_ass_submission"/>
		<xsl:param name="lab_step1"/>
		<xsl:param name="lab_required"/>
		<xsl:param name="lab_file_upload"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_inst2"/>
		<xsl:param name="lab_inst3"/>
		<xsl:param name="lab_inst4"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_uploaded_file"/>
		<xsl:param name="lab_step2"/>
		<xsl:param name="lab_keep_existing_file"/>
		<xsl:param name="lab_g_form_btn_prev_step"/>
		<xsl:param name="lab_g_form_btn_next_step"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_ass_submission"/>
				<xsl:text>&#160;-&#160;</xsl:text>
				<xsl:value-of select="header/title"/>
			</xsl:with-param>
			<!-- <xsl:with-param name="new_template">true</xsl:with-param> -->
		</xsl:call-template>
		<div class="work_step">
			<xsl:choose>
				<xsl:when test="$max_upload = '-1'">
					<span class="yellow_icon" style="font-size:18px;">2</span>
					<xsl:value-of select="$lab_step2"/>
				</xsl:when>
				<xsl:otherwise>
					<span class="yellow_icon">1</span>
					<xsl:copy-of select="$lab_step1"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>&#160;:&#160;</xsl:text>
			<xsl:copy-of select="$lab_file_upload"/>
		</div>
		<div class="work_font">
			<xsl:value-of select="$lab_inst2"/>
			<xsl:value-of select="$lab_inst4"/>
		</div>
		<!-- Content -->
<!-- 		<table class="Bg" border="0" cellpadding="3" cellspacing="0" width="100%"> -->
<!-- 			<tr> -->
<!-- 				<td width="20%" align="right" height="10"> -->
<!-- 					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/> -->
<!-- 				</td> -->
<!-- 				<td width="80%"> -->
<!-- 					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/> -->
<!-- 				</td> -->
<!-- 			</tr>		 -->
			<xsl:call-template name="ass_submit_list">
				<xsl:with-param name="lab_desc" select="$lab_desc"/>
				<xsl:with-param name="lab_uploaded_file" select="$lab_uploaded_file"/>
				<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
				<xsl:with-param name="lab_keep_existing_file" select="$lab_keep_existing_file"/>
				<xsl:with-param name="lab_file" select="$lab_file"/>
			</xsl:call-template>
			<div style="margin-left: 14%;">
			    <span class="wzb-form-star">*</span><xsl:value-of select="$lab_required"/>
			</div>
<!-- 			<tr> -->
<!-- 				<td width="20%" align="right" height="10"> -->
<!-- 					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/> -->
<!-- 				</td> -->
<!-- 				<td width="80%"> -->
<!-- 					<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/> -->
<!-- 				</td> -->
<!-- 			</tr>			 -->
<!-- 		</table> -->
		<!-- button-->
		<div class="report_botton">
			<xsl:if test="$max_upload = '-1'">
				<a href="javascript:;" onclick="javascript:ass.submit_step_one({$mod_id},{count(header/Description/body)},{$tkh_id})">
					<xsl:value-of select="$lab_g_form_btn_prev_step"/>
				</a>
			</xsl:if>
			<a href="javascript:;" onclick="javascript:ass.submit_step_two(document.ass_submit,'{$wb_lang}')">
				<xsl:value-of select="$lab_g_form_btn_next_step"/>
			</a>
			<a href="javascript:;" onclick="javascript:ass.cancel('{/module/@tkh_id}')">
				<xsl:value-of select="$lab_g_form_btn_cancel"/>
			</a>
		</div>
		<input type="hidden" name="cmd" value="submit_ass"/>
		<input type="hidden" name="mod_id" value="{$mod_id}"/>
		<input type="hidden" name="ass_max_upload" value="{$max_upload}"/>
		<input type="hidden" name="step"/>
		<input type="hidden" name="num_of_files"/>
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="tkh_id" value="{$tkh_id}"/>
		<xsl:if test="$max_upload = '-1'">
			<input type="hidden" name="no_of_upload" value="{count(header/Description/body)}"/>
		</xsl:if>
	</xsl:template>
	<xsl:template name="ass_submit_list">
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_uploaded_file"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_keep_existing_file"/>
		<xsl:param name="lab_file"/>
		<xsl:for-each select="header/Description/body">
			<xsl:variable name="file_id" select="@id"/>
			<div class="work_frame">
				<div class="work_div_left" style="width:10%;">
				    <span style="color: #f00;margin-right:3px;">*</span>
					<span class="grayC999">
						<xsl:choose>
							<xsl:when test=". != ''">
								<xsl:value-of select="."/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_file"/>
								<xsl:value-of select="@id"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>&#160;:</xsl:text>
					</span>
				</div>
				<div class="work_div_right" style="width:90%;">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="/module/body/uploadPathTemp/file/@id = $file_id">
								<!--<xsl:value-of select="$lab_uploaded_file"/>&#160;:&#160;<xsl:value-of select="/module/body/uploadPathTemp/file[@id = $file_id]/@name"/>-->
								<xsl:value-of select="$lab_keep_existing_file"/>
								<br/>
								<xsl:value-of select="$lab_change_to"/>&#160;:&#160;
								<div class="file">
   									<input id="file_{@id}" class="file_file" name="file_name{@id}" type="file" onchange="$('#textfield_{@id}').html(this.value);$('#textfield_{@id}').attr('title',this.value);"/>
									<div id="textfield_{@id}" class="file_txt" value="{$no_select_file}"></div>
									<div class="file_button"><xsl:value-of select="$usr_browse"/></div>
								</div>
								<input name="file{@id}" type="hidden" value="{/module/body/uploadPathTemp/file[@id = $file_id]/@name}"/>
							</xsl:when>
							<xsl:otherwise>
								<div class="file">
   									<input id="file_{@id}" class="file_file" name="file_name{@id}" type="file" onchange="$('#textfield_{@id}').html(this.value);$('#textfield_{@id}').attr('title',this.value);"/>
									<div id="textfield_{@id}" class="file_txt" value="{$no_select_file}"></div>
									<div class="file_button" style="line-height:22px;"><xsl:value-of select="$usr_browse"/></div>
								</div>
								<input name="file{@id}" type="hidden"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
				</div>
				<div class="work_div_left" style="width:10%;">
					<span class="grayC999">
						<xsl:value-of select="$lab_desc"/>&#160;:</span>
				</div>
				<div class="work_div_right" style="width:90%;">
					<xsl:choose>
						<xsl:when test="/module/body/uploadPathTemp/file/@id = $file_id">
							<textarea class="wbpl showhide" rows="3" wrap="VIRTUAL" style="width:55%;" cols="35" name="comment{@id}">
								<xsl:value-of select="/module/body/uploadPathTemp/file[@id = $file_id]/."/>
							</textarea>
						</xsl:when>
						<xsl:otherwise>
							<textarea class="wbpl showhide" rows="3" wrap="VIRTUAL" style="width:55%;" cols="35" name="comment{@id}"/>
						</xsl:otherwise>
					</xsl:choose>
				</div>
				<img src="{$wb_img_path}tp.gif" width="1" height="5" border="0"/>
			</div>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="env"/>
</xsl:stylesheet>

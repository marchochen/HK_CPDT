<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/display_filetype_icon.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_input_file.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	<xsl:output indent="yes"/>
	<xsl:variable name="max_score" select="/student_report/student/assignment/header/@max_score"/>
	<xsl:variable name="pass_score" select="/student_report/student/assignment/header/@pass_score"/>
	<xsl:variable name="cos_id" select="/student_report/student/assignment/header/@course_id"/>
	<xsl:variable name="mod_id" select="/student_report/student/assignment/@id"/>
	<xsl:variable name="score" select="/student_report/student/assignment/@score"/>
	<xsl:variable name="grade" select="/student_report/student/assignment/@grade"/>
	
	<xsl:variable name="title" select="/student_report/student/assignment/header/title/text()"/>
	<xsl:variable name="is_item" select="/student_report/is_item/text()"/>
	<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
	<xsl:variable name="label_core_training_management_393" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')"/>
	<xsl:variable name="label_core_training_management_30" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_30')"/>
	<!-- ==================================================================== -->	
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- ==================================================================== -->	
	<xsl:template match="/student_report">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_media.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script language="JavaScript" TYPE="text/javascript"><![CDATA[
	var ass = new wbAssignment;
	var isExcludes = getUrlParam('isExcludes') ? getUrlParam('isExcludes') : 'false';
	var isFromIframe = getUrlParam('isFromIframe') ? getUrlParam('isFromIframe') : 'false';
]]></script>
			<xsl:call-template name="new_css"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" onunload="wb_utils_close_preloading()">
			<form style="height:100%;margin-left: 10px;" name="frmXml" enctype="multipart/form-data">
				<xsl:call-template name="wb_init_lab"/>
			</form>
			
			<style>
				body{overflow:scroll;overflow-y:auto;}
			</style>
		</body>

	</xsl:template>
	<!-- ==================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">備註</xsl:with-param>
			<xsl:with-param name="lab_grade">級別</xsl:with-param>
			<xsl:with-param name="lab_submission_details">提交詳情</xsl:with-param>
			<xsl:with-param name="lab_file">文檔</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_grading">評分</xsl:with-param>
			<xsl:with-param name="lab_scores">分數</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_max_score">滿分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分數</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">已上傳文檔</xsl:with-param>
			<xsl:with-param name="lab_change_to"> 更改為</xsl:with-param>
			<xsl:with-param name="lab_notify_student">請寄確認書至</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_sub_desc">按圖示查看提交的檔</xsl:with-param>
			<xsl:with-param name="lab_download_zip_desc">要以 zip 格式下載所有檔，請按下面的圖示：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">备注</xsl:with-param>
			<xsl:with-param name="lab_grade">级别</xsl:with-param>
			<xsl:with-param name="lab_submission_details">提交详情</xsl:with-param>
			<xsl:with-param name="lab_file">文档</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_grading">评分</xsl:with-param>
			<xsl:with-param name="lab_scores">分数</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分数</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">已上传文档</xsl:with-param>
			<xsl:with-param name="lab_change_to"> 更改为</xsl:with-param>
			<xsl:with-param name="lab_notify_student">请寄确认书至</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_sub_desc">单击图标查看提交的文件</xsl:with-param>
			<xsl:with-param name="lab_download_zip_desc">要以 zip 格式下载所有文件，请单击下面的图标：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_comment">Comment</xsl:with-param>
			<xsl:with-param name="lab_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_submission_details">Submission details</xsl:with-param>
			<xsl:with-param name="lab_file">File</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_grading">Grading</xsl:with-param>
			<xsl:with-param name="lab_scores">Score</xsl:with-param>
			<xsl:with-param name="lab_pass_score">Passing score</xsl:with-param>
			<xsl:with-param name="lab_A">A</xsl:with-param>
			<xsl:with-param name="lab_B">B</xsl:with-param>
			<xsl:with-param name="lab_C">C</xsl:with-param>
			<xsl:with-param name="lab_D">D</xsl:with-param>
			<xsl:with-param name="lab_F">Fail</xsl:with-param>
			<xsl:with-param name="lab_max_score">Maximum score</xsl:with-param>
			<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
			<xsl:with-param name="lab_uploaded_file">Upload file</xsl:with-param>
			<xsl:with-param name="lab_notify_student">Please send an acknowledgment to</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_sub_desc">Click the icon(s) to view the submitted file(s).</xsl:with-param>
			<xsl:with-param name="lab_download_zip_desc">To download all the file(s) in zip format, click the icon below:</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ==================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_comment"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_submission_details"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_grading"/>
		<xsl:param name="lab_scores"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_uploaded_file"/>
		<xsl:param name="lab_notify_student"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_sub_desc"/>
		<xsl:param name="lab_download_zip_desc"/>
		<xsl:param name="lab_pass_score"/>
		<input type="hidden" name="rename" value="no"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">HOMEWORK_CORRECTION</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$is_item = true or $is_item='true'">
				<!-- nav -->
				<xsl:call-template name="itm_action_nav">
					<xsl:with-param  name="cur_node_id">120</xsl:with-param>
				</xsl:call-template>
				
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:apply-templates select="item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info"><xsl:value-of select="$label_core_training_management_250" /></xsl:with-param>
						</xsl:apply-templates>
						<span class="NavLink">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<a href="javascript:itm_lst.itm_evaluation_report({$cos_id },'TST')"><xsl:value-of select="$label_core_training_management_393"/></a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<a href="Javascript:ass.view_submission({$cos_id},{$mod_id},'all',1,'true')"><xsl:value-of select="$lab_submission_details"/></a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$lab_grading" />&#160;-&#160;<xsl:value-of select="$title" />
						</span>
					</xsl:with-param>
				</xsl:call-template>
				<!-- nav end -->
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_title">
				    <xsl:with-param name="text_style">margin-left: -10px;</xsl:with-param>
					<xsl:with-param name="text" select="$lab_grading"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_submission_details"/>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_line"/>
		<table class="Bg" cellpadding="5" cellspacing="0" width="{$wb_gen_table_width}" border="0">
			<tr>
				<td colspan="2">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<span class="Text">
						<xsl:value-of select="$lab_sub_desc"/>
					</span>
				</td>
			</tr>
			<xsl:for-each select="student/assignment/body/uploadPath/file[@type = 'STUDENT' ]">
				<xsl:variable name="cur_file_position" select="position()"/>
				<tr>
					<td width="10%" valign="top" align="right">
						<xsl:variable name="href_name">
							<xsl:call-template name="escape_js">
								<xsl:with-param name="input_str" select="@name"/>
							</xsl:call-template>
						</xsl:variable>
						<a href="../{../@student}{$href_name}" target="_blank">
							<xsl:call-template name="display_filetype_icon">
								<xsl:with-param name="fileName" select="@name"/>
								<xsl:with-param name="imageSize">32</xsl:with-param>
							</xsl:call-template>
						</a>
					</td>
					<td valign="top" width="90%">
						<span class="Text">
							<xsl:if test="../../../header/Description/body[position() = $cur_file_position]/text()">
								<xsl:value-of select="../../../header/Description/body[@id = $cur_file_position]/text()"/>
							</xsl:if>
						</span>
						<span class="Text">
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="."/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</td>
				</tr>
			</xsl:for-each>
			<tr>
				<td colspan="2">
					<span class="Text">
						&#160;
					</span>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<span class="Text">
						<xsl:value-of select="$lab_download_zip_desc"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="60" valign="top" align="right">
					<a href="javascript:ass.download(document.frmXml,'{$mod_id}','{$wb_lang}','{student/assignment/@tkh_id}','true')" class="assResultLink" target="_self">
						<img src="{$wb_img_path}ico_download.gif" border="0"/>
					</a>
				</td>
				<td>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</tr>
		</table>
		<!-- <xsl:call-template name="wb_ui_line"/> -->
		<xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_grading"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table class="Bg" cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td class="padding6" width="20%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td class="padding6" width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr >
				<td class="padding6" width="20%" align="right" >
					<span class="TitleText">
						<xsl:choose>
							<xsl:when test="$max_score = -1">
								<input type="hidden" name="grading_format" value="grade"/>
								<xsl:value-of select="$lab_grade"/>
							</xsl:when>
							<xsl:otherwise>
								<input type="hidden" name="grading_format" value="score"/>
								<input type="hidden" name="max_score" value="{$max_score}"/>
								<xsl:value-of select="$lab_scores"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td class="padding6" width="80%" >
					<xsl:choose>
						<xsl:when test="$max_score = -1">
							<span class="Text">
								<select name="pgr_grade" class="Select">
									<option value="A+">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'A+'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_A"/>+</option>
									<option value="A">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'A'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_A"/>
									</option>
									<option value="A-">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'A-'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_A"/>-</option>
									<option value="B+">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'B+'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_B"/>+</option>
									<option value="B">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'B'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_B"/>
									</option>
									<option value="B-">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'B-'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_B"/>-</option>
									<option value="C+">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'C+'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_C"/>+</option>
									<option value="C">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'C'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_C"/>
									</option>
									<option value="C-">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'C-'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_C"/>-</option>
									<option value="D">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'D'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_D"/>
									</option>
									<option value="F">
										<xsl:if test="student/assignment/@status = 'GRADED' and $grade = 'F'">
											<xsl:attribute name="selected">selected</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="$lab_F"/>
									</option>
								</select>
							</span>
						</xsl:when>
						<xsl:otherwise>
							<xsl:choose>
								<xsl:when test="student/assignment/@status = 'GRADED'">
									<input class="wzb-inputText" type="text" name="pgr_score" value="{$score}" style="width:100px;"/>
								</xsl:when>
								<xsl:otherwise>
									<input class="wzb-inputText" type="text" name="pgr_score" style="width:100px;"/>
								</xsl:otherwise>
							</xsl:choose>
							<img src="{$wb_img_path}tp.gif" width="20" height="1" border="0"/>
							<span class="Text">
								<xsl:value-of select="$lab_max_score"/>&#160;:&#160;<xsl:value-of select="$max_score"/>; &#160; <xsl:value-of select="$lab_pass_score"/>&#160;:&#160;<xsl:value-of select="$pass_score"/>&#160;
							</span>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td class="padding6" width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_comment"/>：</span>
				</td>
				<td class="padding6" width="80%">
					<textarea class="wzb-inputTextArea" rows="4" wrap="VIRTUAL" style="width:350px;" cols="35" name="ass_comment">
						<xsl:if test="student/assignment/@status = 'GRADED'">
							<xsl:value-of select="student/assignment/body/uploadPath/file[@type = 'TEACHER']/@desc"/>
						</xsl:if>
					</textarea>
				</td>
			</tr>
			<tr>
				<td class="padding6" width="20%" align="right">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td class="padding6" width="80%">
					<xsl:choose>
						<xsl:when test="student/assignment/@status = 'GRADED' and count(student/assignment/body/uploadPath/file[@type = 'TEACHER' and @name != '']) > 0">
							<span class="Text">
								<xsl:value-of select="$lab_uploaded_file"/>&#160;:&#160;<xsl:value-of select="student/assignment/body/uploadPath/file[@type = 'TEACHER']/@name"/>
								<br/>
								<img src="{$wb_img_path}tp.gif" width="15" height="1" border="0"/>
								<xsl:value-of select="$lab_change_to"/>&#160;
								<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">ass_upload_file</xsl:with-param>
								</xsl:call-template>
								<br/>
								<input type="hidden" name="ass_filename" value="{student/assignment/body/uploadPath/file[@type = 'TEACHER']/@name}"/>
							</span>
						</xsl:when>
						<xsl:otherwise>
							
							<xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">ass_upload_file</xsl:with-param>
							</xsl:call-template>
							<br/>
							<input type="hidden" name="ass_filename"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right">
				</td>
				<td width="80%">
					<label for="chk_send_email">
						<span class="Text">
							<input id="chk_send_email" name="send_email" type="hidden" value="false" />&#160;
							
							<input type="hidden" name="email" value="{student/@email}"/>
						</span>
					</label>
					<!-- <div style="margin-top:-28px;margin-left:15px;">
					<xsl:value-of select="$lab_notify_student"/>&#160;<xsl:value-of select="student/@display_bil"/>
					</div> -->
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<table cellpadding="0" cellspacing="0" width="{$wb_gen_table_width}" border="0">
				<tr>
					<td align="center">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:ass.grade_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>',<xsl:value-of select="$cos_id"/>,<xsl:value-of select="$mod_id"/>,'graded','<xsl:value-of select="$is_item"/>',isFromIframe)</xsl:with-param>
						</xsl:call-template>
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</div>
		<input type="hidden" name="cmd" value="grade_ass"/>
		<input type="hidden" name="mod_id" value="{$mod_id}"/>
		<input type="hidden" name="pgr_usr_id" value="{student/@ent_id}"/>
		<input type="hidden" name="num_of_files" value="1"/>
		<input type="hidden" name="tkh_id" value="{student/assignment/@tkh_id}"/>
		<input type="hidden" name="url_success" value="../resource/{$mod_id}/ad3523fdads3242fafd/assignments.zip"/>
	</xsl:template>
</xsl:stylesheet>

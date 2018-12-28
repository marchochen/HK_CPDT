<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/display_filetype_icon.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="max_score" select="/student_report/student/assignment/header/@max_score"/>
	<xsl:variable name="pass_score" select="/student_report/student/assignment/header/@pass_score"/>
	<xsl:variable name="score" select="/student_report/student/assignment/@score"/>
	<xsl:variable name="grade" select="/student_report/student/assignment/@grade"/>
	
	<xsl:template name="DOCTYPE">
		<![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"/>]]>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:value-of select="document('')/*/xsl:template[@name='DOCTYPE']/node()" disable-output-escaping="yes"/> 
		<html>
			<xsl:apply-templates select="student_report"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="student_report">
		<head>
			<meta http-equiv="pragma" content="no-cache"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<link rel="stylesheet" href="../static/css/three.css"/>
			
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_assignment.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			$(function(){
				var file = ']]><xsl:value-of select="/student_report/student/assignment/header/source/text()"/><![CDATA['
				var type = file.substring(file.lastIndexOf(".") + 1)
				cos_id = ']]><xsl:value-of select="/student_report/student/assignment/@id"/><![CDATA['
				var url = ']]><xsl:value-of select="/student_report/student/assignment/header/source/text()"/><![CDATA['
				tpl_use = 'blank_template.xsl'
				mod_id = getUrlParam("mod_id");
				tkh_id = ']]><xsl:value-of select="/student_report/student/assignment/@tkh_id"/><![CDATA['
				usr_id = ']]><xsl:value-of select="/student_report/meta/cur_usr/@id"/><![CDATA['
				var win_name = 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id;
				$("#mainid").attr("src", module_lst.start_content_url("ASS",mod_id,tpl_use,cos_id, tkh_id, win_name))
				$('#url').attr("src", url);
				if(type == "jpg" || type == "png" || type == "bmp" || type == "gig" || type == "tif" || type == "jpe" || type == "gif") {
					var picURL = '../resource/' + ']]><xsl:value-of select="/student_report/student/assignment/@id"/><![CDATA[/' + ']]><xsl:value-of select="/student_report/student/assignment/header/source/text()"></xsl:value-of><![CDATA['
					$("#content").append("<img src='' id='picURL'>")
					$("#picURL").attr("src", picURL);
				} else {
					$("#content").append('<iframe src="content_url" width="100%" height="700" frameborder="0" id="mainid" />')
					$("#mainid").attr("src", module_lst.start_content_url("ASS",mod_id,tpl_use,cos_id, tkh_id, win_name))
				}
			});
			
			var module_lst = new wbModule;
			ass = new wbAssignment;
			
			var preview = function(field){
				
				var path = field.getAttribute("rel");
				
				var isDoc = function(fileExt){
					if(!fileExt){
						return false;
					}
					fileExt = fileExt.substring(0,3).toLowerCase();
					
					if(fileExt == "doc"||fileExt == "xls"||fileExt == "ppt" || fileExt == "pdf"){
						return true;
					}
					return false;
				}
				
				var fileExt = path.substring(path.lastIndexOf(".")+1, path.length);
				
				if(isDoc(fileExt)){
					path = wb_utils_controller_base+'idv/preview?filePath='+path;
				}
				window.open(path);
			};
			
			]]></SCRIPT>

			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_ass">作業</xsl:with-param>
			<xsl:with-param name="lab_view_result">成績</xsl:with-param>
			<xsl:with-param name="lab_result">成績</xsl:with-param>
			<xsl:with-param name="lab_score">分數</xsl:with-param>
			<xsl:with-param name="lab_max_score">最高分數</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分數</xsl:with-param>
			<xsl:with-param name="lab_comment">備註</xsl:with-param>
			<xsl:with-param name="lab_grade">級別</xsl:with-param>
			<xsl:with-param name="lab_submission_details">提交詳情</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_file">檔案</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_click_to_download">請按此下載備註檔案</xsl:with-param>
			<xsl:with-param name="lab_not_garded_yet">尚未評分</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_not_grade">沒有級別</xsl:with-param>
			<xsl:with-param name="lab_sub_desc">按圖示查看已提交的檔案。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_download_zip_desc">下載ZIP格式的備註檔案，請按以下圖示:</xsl:with-param>
			<xsl:with-param name="lab_view_result_desc">點擊<a href="javascript:ass.start_exec('{//assignment/header/@subtype}',{//assignment/@id},{//assignment/header/@course_id},'{$wb_lang}','{//assignment/@tkh_id}','{//cur_usr/@ent_id}');" class="TextBold">這裡</a>查看作業說明。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">课程目录</xsl:with-param>
			<xsl:with-param name="lab_view_result">成绩</xsl:with-param>
			<xsl:with-param name="lab_result">成绩</xsl:with-param>
			<xsl:with-param name="lab_score">分数</xsl:with-param>
			<xsl:with-param name="lab_max_score">满分</xsl:with-param>
			<xsl:with-param name="lab_pass_score">合格分数</xsl:with-param>
			<xsl:with-param name="lab_comment">备注</xsl:with-param>
			<xsl:with-param name="lab_grade">级别</xsl:with-param>
			<xsl:with-param name="lab_submission_details">提交详情</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_file">文档</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_click_to_download">点击此处以下载评论文档</xsl:with-param>
			<xsl:with-param name="lab_not_garded_yet">尚未评分</xsl:with-param>
			<xsl:with-param name="lab_A">甲</xsl:with-param>
			<xsl:with-param name="lab_B">乙</xsl:with-param>
			<xsl:with-param name="lab_C">丙</xsl:with-param>
			<xsl:with-param name="lab_D">丁</xsl:with-param>
			<xsl:with-param name="lab_F">不合格</xsl:with-param>
			<xsl:with-param name="lab_ass">作业</xsl:with-param>
			<xsl:with-param name="lab_not_grade">没有级别</xsl:with-param>
			<xsl:with-param name="lab_sub_desc">点击图标查看已提交的文档。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_download_zip_desc">下载ZIP格式的评论文档，请点击以下图标:</xsl:with-param>
			<xsl:with-param name="lab_view_result_desc">点击<a href="javascript:ass.start_exec('{//assignment/header/@subtype}',{//assignment/@id},{//assignment/header/@course_id},'{$wb_lang}','{//assignment/@tkh_id}','{//cur_usr/@ent_id}');" class="TextBold">这里</a>查看作业说明。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_ass">Assignment</xsl:with-param>
			<xsl:with-param name="lab_view_result">Results</xsl:with-param>
			<xsl:with-param name="lab_result">Result</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
			<xsl:with-param name="lab_max_score">Maximum score</xsl:with-param>
			<xsl:with-param name="lab_pass_score">Passing score</xsl:with-param>
			<xsl:with-param name="lab_comment">Comment</xsl:with-param>
			<xsl:with-param name="lab_grade">Grade</xsl:with-param>
			<xsl:with-param name="lab_submission_details">Submission details</xsl:with-param>
			<xsl:with-param name="lab_submission_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_file">File</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_click_to_download">Click here to download the comment file</xsl:with-param>
			<xsl:with-param name="lab_not_garded_yet">Not graded</xsl:with-param>
			<xsl:with-param name="lab_A">A</xsl:with-param>
			<xsl:with-param name="lab_B">B</xsl:with-param>
			<xsl:with-param name="lab_C">C</xsl:with-param>
			<xsl:with-param name="lab_D">D</xsl:with-param>
			<xsl:with-param name="lab_F">Fail</xsl:with-param>
			<xsl:with-param name="lab_not_grade">Not graded</xsl:with-param>
			<xsl:with-param name="lab_sub_desc">Click the icon(s) to view the submitted file(s).</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_download_zip_desc">To download the comment file in zip format, click the icon below:</xsl:with-param>
			<xsl:with-param name="lab_view_result_desc">Click <a href="javascript:ass.start_exec('{//assignment/header/@subtype}',{//assignment/@id},{//assignment/header/@course_id},'{$wb_lang}','{//assignment/@tkh_id}','{//cur_usr/@ent_id}');" class="TextBold">here</a> for the assignment instruction.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_ass"/>
		<xsl:param name="lab_view_result"/>
		<xsl:param name="lab_result"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_max_score"/>
		<xsl:param name="lab_comment"/>
		<xsl:param name="lab_grade"/>
		<xsl:param name="lab_submission_details"/>
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_click_to_download"/>
		<xsl:param name="lab_not_garded_yet"/>
		<xsl:param name="lab_A"/>
		<xsl:param name="lab_B"/>
		<xsl:param name="lab_C"/>
		<xsl:param name="lab_D"/>
		<xsl:param name="lab_F"/>
		<xsl:param name="lab_not_grade"/>
		<xsl:param name="lab_sub_desc"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_download_zip_desc"/>
		<xsl:param name="lab_view_result_desc"/>
		<xsl:param name="lab_pass_score"/>
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="student/assignment/header/title/text()"/>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		<div class="content_info report_title " style="padding:10px 0px;">
			<xsl:choose>
				<xsl:when test="/student_report/student/assignment/header/instruction/text() != ''">
				<p class="work_font">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value"><xsl:value-of select="/student_report/student/assignment/header/instruction/text()" /></xsl:with-param>	
					</xsl:call-template>
				</p>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="/student_report/student/assignment/header/source/@type = 'URL'">
						<iframe src="" width="100%" height="700" frameborder="0" id="url" />
					</xsl:if>
					<xsl:if test="/student_report/student/assignment/header/source/@type = 'FILE'">
						<p class="work_font" style="text-align:center;">
							<div id="content" style="text-align:center;"/>
						</p>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_view_result"/>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		<div class="report_info clean_margin" style="line-height: 50px;padding: 15px;">
			<div class="clearfix">
				<xsl:choose>
					<xsl:when test="$max_score = -1">
						<div class="left_div_width">
							<span class="grayC999"><xsl:value-of select="$lab_grade"/>:</span>
						</div>
						<div class="right_div_width">
							<span>
								<xsl:choose>
									<xsl:when test="student/assignment/@status = 'NOT GRADED' ">--</xsl:when>
									<xsl:when test="$grade = 'A+' ">
										<xsl:value-of select="$lab_A"/>+</xsl:when>
									<xsl:when test="$grade = 'A' ">
										<xsl:value-of select="$lab_A"/>
									</xsl:when>
									<xsl:when test="$grade = 'A-' ">
										<xsl:value-of select="$lab_A"/>-</xsl:when>
									<xsl:when test="$grade = 'B+' ">
										<xsl:value-of select="$lab_B"/>+</xsl:when>
									<xsl:when test="$grade = 'B' ">
										<xsl:value-of select="$lab_B"/>
									</xsl:when>
									<xsl:when test="$grade = 'B-' ">
										<xsl:value-of select="$lab_B"/>-</xsl:when>
									<xsl:when test="$grade = 'C+' ">
										<xsl:value-of select="$lab_C"/>+</xsl:when>
									<xsl:when test="$grade = 'C' ">
										<xsl:value-of select="$lab_C"/>
									</xsl:when>
									<xsl:when test="$grade = 'C-' ">
										<xsl:value-of select="$lab_C"/>-</xsl:when>
									<xsl:when test="$grade = 'D' ">
										<xsl:value-of select="$lab_D"/>
									</xsl:when>
									<xsl:when test="$grade = 'F' ">
										<xsl:value-of select="$lab_F"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_not_grade"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<div class="left_div_width">
							<span class="grayC999"><xsl:value-of select="$lab_score"/>:</span>
						</div>
						<div class="right_div_width">
							<span><xsl:value-of select="$score"/>&#160;(&#160;<xsl:value-of select="$lab_max_score"/>&#160;:&#160;<xsl:value-of select="$max_score"/>&#160;; <xsl:value-of select="$lab_pass_score"/>&#160;:&#160;<xsl:value-of select="$pass_score"/>&#160;)</span>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</div>
			<xsl:choose>
				<xsl:when test="student/assignment/@status = 'GRADED' ">
					<xsl:if test="count(student/assignment/body/uploadPath/file[@type = 'TEACHER' ]) > 0">
						<div class="clearfix">
							<div class="left_div_width">
								<span class="grayC999"><xsl:value-of select="$lab_comment"/>:</span>
							</div>
							<div class="right_div_width">
								<xsl:for-each select="student/assignment/body/uploadPath/file[@type = 'TEACHER' ]">
									<xsl:if test="@desc=''">--</xsl:if>
									<xsl:if test="@name != ''">
									<p>
										<a href="..\{../@teacher}{@name}" class="Text" target="_blank">
											<xsl:call-template name="display_filetype_icon">
												<xsl:with-param name="fileName">
													<xsl:value-of select="@name"/>
												</xsl:with-param>
												<xsl:with-param name="imageSize">32</xsl:with-param>
											</xsl:call-template>
										</a>
									</p>
									</xsl:if>
									<p>
										<xsl:call-template name="unescape_html_linefeed">
											<xsl:with-param name="my_right_value">
												<xsl:value-of select="."/>
											</xsl:with-param>
										</xsl:call-template>
									</p>
									<p>
										<!-- <xsl:if test="//file[position() = 1]/@type = 'STUDENT'">
											<xsl:value-of select="//file[position() = 1]/@desc"/>
										</xsl:if> -->
										<xsl:if test="//file[last()]/@type = 'TEACHER'">
											<xsl:value-of select="//file[last()]/@desc"/>
									</xsl:if>
									</p>
								</xsl:for-each>
							</div>
						</div>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<div class="left_div_width">
						<span class="grayC999"><xsl:value-of select="$lab_comment"/>:</span>
					</div>
					<div class="right_div_width">
						<span>
							<xsl:value-of select="$lab_not_garded_yet"/>
						</span>
					</div>
				</xsl:otherwise>
			</xsl:choose>
			<div class="clearfix">
				<div class="left_div_width">
					<span class="grayC999"><xsl:value-of select="$lab_submission_details"/>:</span>
				</div>
				<div class="right_div_width">
					<span><xsl:value-of select="$lab_sub_desc"/></span>
				</div>
			</div>
			<xsl:for-each select="student/assignment/body/uploadPath/file[@type = 'STUDENT' ]">
				<xsl:variable name="cur_file_position" select="position()"/>
				<div class="clearfix">
					<div class="left_div_width">
						<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
					</div>
					<div class="right_div_width">
						<p>
							<xsl:variable name="href_name">
								<xsl:call-template name="escape_js">
									<xsl:with-param name="input_str">
										<xsl:value-of select="@name"/>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:variable>
							
							<a onclick="javascript:preview(this);" href="javascript:void(0)" rel="{../@student}{$href_name}">
								<xsl:call-template name="display_filetype_icon">
									<xsl:with-param name="fileName">
										<xsl:value-of select="@name"/>
									</xsl:with-param>
									<xsl:with-param name="imageSize">32</xsl:with-param>
								</xsl:call-template>
							</a>
							
							<span>
								<xsl:if test="../../../header/Description/body[position() = $cur_file_position]/text()">
									<xsl:value-of select="../../../header/Description/body[@id = $cur_file_position]/text()"/>
								</xsl:if>
							</span>
							<span>
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="."/>
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</p>
						<p>
							
						</p>
					</div>
				</div>
			</xsl:for-each>
			<div class="clearfix">
				<div class="left_div_width">
					<span class="grayC999"><xsl:value-of select="$lab_submission_date"/> :</span>
				</div>
				<div class="right_div_width">
					<span>
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="student/assignment/@complete_date"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</span>
				</div>
			</div>
		</div>
		<div class="report_botton">
			<a href="javascript:;" onclick="javascript:ass.cancel('{//assignment/@tkh_id}')"><xsl:value-of select="$lab_g_form_btn_close"/></a>
		</div>
	</xsl:template>
</xsl:stylesheet>

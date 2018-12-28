<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const-->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="mod_id" select="/module/@id"/>
	<xsl:variable name="usr_id" select="/module/cur_usr/@id"/>
	<xsl:variable name="max_upload" select="/module/header/@max_upload"/>
	<xsl:variable name="no_of_files" select="count(/module/body/uploadPathTemp/file)"/>
	
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			ass = new wbAssignment
			var max_upload = ]]><xsl:value-of select="header/@max_upload"/><![CDATA[;
			var cur_index = ]]><xsl:value-of select="count(body/uploadPathTemp/file)"/><![CDATA[;
		]]></script>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="ass_submit" enctype="multipart/form-data">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作業提交</xsl:with-param>
			<xsl:with-param name="lab_step2">第二步</xsl:with-param>
			<xsl:with-param name="lab_confirmation">確認</xsl:with-param>
			<xsl:with-param name="lab_inst">以下是你提交的資料</xsl:with-param>
			<xsl:with-param name="lab_file">檔案</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_inst2">你要提供電郵確認嗎</xsl:with-param>
			<xsl:with-param name="lab_inst3">需要，請寄確認信到以下我的電子郵箱</xsl:with-param>
			<xsl:with-param name="lab_title_inst3">電郵確認</xsl:with-param>
			<xsl:with-param name="lab_step3">第三步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev_step">上一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">作业提交</xsl:with-param>
			<xsl:with-param name="lab_step2">第二步</xsl:with-param>
			<xsl:with-param name="lab_confirmation">确认</xsl:with-param>
			<xsl:with-param name="lab_inst">以下是你提交的资料</xsl:with-param>
			<xsl:with-param name="lab_file">文档</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_inst2">你要需要电子邮件确认吗</xsl:with-param>
			<xsl:with-param name="lab_inst3">需要，请寄确认书到以下我的电子邮箱</xsl:with-param>
			<xsl:with-param name="lab_title_inst3">电子邮件确认</xsl:with-param>
			<xsl:with-param name="lab_step3">第三步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev_step">上一步</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">完成</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="header">
			<xsl:with-param name="lab_ass_submission">Assignment submission</xsl:with-param>
			<xsl:with-param name="lab_step2">Step 2</xsl:with-param>
			<xsl:with-param name="lab_confirmation">Confirmation</xsl:with-param>
			<xsl:with-param name="lab_inst">Here are your submission details</xsl:with-param>
			<xsl:with-param name="lab_file">File</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_inst2">Do you want to receive an email acknowledgment of your submission</xsl:with-param>
			<xsl:with-param name="lab_inst3">Yes, please send an acknowledgment to my email address below</xsl:with-param>
			<xsl:with-param name="lab_title_inst3">email acknowledgment</xsl:with-param>
			<xsl:with-param name="lab_step3">Step 3</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_prev_step">Prev step</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_finish">Finish</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="header">
		<xsl:param name="lab_ass_submission"/>
		<xsl:param name="lab_step2"/>
		<xsl:param name="lab_step3"/>
		<xsl:param name="lab_confirmation"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_file"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_inst2"/>
		<xsl:param name="lab_inst3"/>
		<xsl:param name="lab_g_form_btn_prev_step"/>
		<xsl:param name="lab_g_form_btn_finish"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_ass_submission"/>
				<xsl:text>&#160;-&#160;</xsl:text>
				<xsl:value-of select="header/title"/>
			</xsl:with-param>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		<div class="work_step">
			<xsl:choose>
				<xsl:when test="$max_upload = '-1'">
					<span class="yellow_icon">3</span>
					<xsl:copy-of select="$lab_step3"/>
				</xsl:when>
				<xsl:otherwise>
					<span class="yellow_icon">2</span>
					<xsl:copy-of select="$lab_step2"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:text>&#160;:&#160;</xsl:text>
			<xsl:copy-of select="$lab_confirmation"/>
		</div>
		<div class="work_font">
			<xsl:value-of select="$lab_inst"/>
		</div>
		<!-- Content -->
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text"/>
			<xsl:with-param name="new_template">true</xsl:with-param>
		</xsl:call-template>
		<div class="content_info">
			<table class="Bg" cellspacing="0" cellpadding="3" border="0" width="100%">
				<tr class="report_title">
					<td width="35%">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="15"/>
						<span class="grayC999">
							<xsl:value-of select="$lab_file"/>
						</span>
					</td>
					<td width="65%">
						<span class="grayC999">
							<xsl:value-of select="$lab_desc"/>
						</span>
					</td>
				</tr>
				<xsl:for-each select="body/uploadPathTemp/file">
<!-- 					<xsl:variable name="row_class"> -->
<!-- 						<xsl:choose> -->
<!-- 							<xsl:when test="position() mod 2">RowsEven</xsl:when> -->
<!-- 							<xsl:otherwise>RowsOdd</xsl:otherwise> -->
<!-- 						</xsl:choose> -->
<!-- 					</xsl:variable> -->
					<xsl:variable name="cur_file_id" select="@id"/>
					<tr class="report_content_tr">
						<td width="20%" valign="top">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="15"/>
							<span class="Text">
								<xsl:choose>
									<xsl:when test="../../../header/Description/body[@id = $cur_file_id]/. != ''">
										<xsl:value-of select="../../../header/Description/body[@id = $cur_file_id]/."/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_file"/>
										<xsl:text>&#160;</xsl:text>
										<xsl:value-of select="@id"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
						<td width="80%">
							<span class="Text">
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="."/>
									</xsl:with-param>
								</xsl:call-template>
								<xsl:text>&#160;</xsl:text>
							</span>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</div>
		<div class="report_botton">
			<xsl:if test="$max_upload = '-1'">
				<a href="javascript:;" onclick="javascript:ass.submit_step_three(document.ass_submit)">
					<xsl:value-of select="$lab_g_form_btn_prev_step"/>
				</a>
			</xsl:if>
			<a href="javascript:;" onclick="javascript:ass.submit_step_four(document.ass_submit,'{header/@notify}')">
				<xsl:value-of select="$lab_g_form_btn_finish"/>
			</a>
			<a href="javascript:;" onclick="javascript:ass.cancel('{/module/@tkh_id}')">
				<xsl:value-of select="$lab_g_form_btn_cancel"/>
			</a>
		</div>
		<input type="hidden" name="cmd" value="submit_ass"/>
		<input type="hidden" name="mod_id" value="{$mod_id}"/>
		<input type="hidden" name="ass_max_uplad" value="{$max_upload}"/>
		<input type="hidden" name="step"/>
		<input type="hidden" name="stylesheet"/>
		<input type="hidden" name="url_failure" value=""/>
		<input name="email" type="hidden" value=""/>
		<input type="hidden" name="tkh_id" value="{/module/@tkh_id}"/>
		<xsl:choose>
			<xsl:when test="$max_upload = '-1' ">
				<input type="hidden" name="num_of_files" value="{$no_of_files}"/>
				<input type="hidden" name="no_of_upload" value="{count(header/Description/body)}"/>
			</xsl:when>
			<xsl:otherwise>
				<input type="hidden" name="num_of_files" value="{$max_upload}"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="$max_upload  ='-1' ">
				<xsl:for-each select="body/uploadPathTemp/file">
					<input type="hidden" name="file{position()}" value="{@name}"/>
					<input type="hidden" name="comment{position()}" value="{.}"/>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="header/Description/body">
					<xsl:variable name="fid" select="@id"/>
					<input type="hidden" name="file{$fid}" value="{/module/body/uploadPathTemp/file[@id = $fid]/@name}"/>
					<input type="hidden" name="comment{$fid}" value="{/module/body/uploadPathTemp/file[@id = $fid]/.}"/>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<xsl:template match="env"/>
	<!-- =============================================================== -->
</xsl:stylesheet>

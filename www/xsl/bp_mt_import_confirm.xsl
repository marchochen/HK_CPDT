<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>	
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="upload_success_cnt" select="count(/Upload/question_list/question)"/>
	<!--<xsl:variable name="upload_failure_cnt" select="count(/Upload/invalid_list/invalid_title_lines/line)"/>-->
	<xsl:variable name="upload_failure_cnt" select="count(//line)"/>
	<xsl:variable name="ulg_id" select="/Upload/question_list/@upload_id"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="Upload/question_list"/>
			<xsl:apply-templates select="Upload/invalid_list"/>
			<xsl:apply-templates select="Upload/invalid_col_name_list"/>
			<xsl:apply-templates select="Upload/invalid_file"/>
			<xsl:apply-templates select="Upload/invalid_encoding"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="question_list | invalid_list | invalid_col_name_list | invalid_file | invalid_encoding">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess
	]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>

		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return status()">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="ulg_id" value="{$ulg_id}"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">匯入题目资源 - 第二步：確認</xsl:with-param>
			<xsl:with-param name="lab_error_title">匯入题目资源 - 錯誤</xsl:with-param>
			<xsl:with-param name="lab_desc">下列是檔中的配對題。按螢幕底部的<b>確定</b> 按鈕完成匯入，或按<b>取消</b> 按鈕停止操作。</xsl:with-param>
			<xsl:with-param name="lab_col_category">資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_score">分數: </xsl:with-param>
			<xsl:with-param name="lab_answers">答案： </xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_general">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_online">已發佈</xsl:with-param>
			<xsl:with-param name="lab_offline">未發佈</xsl:with-param>
			<xsl:with-param name="lab_no_explanation">沒有說明</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">確認</xsl:with-param>
			<!-- -->
			<xsl:with-param name="lab_err_others">其他錯誤</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_category_lines">無效的"資源文件夾"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_resource_lines">無效的"資源編號"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_permission_lines">沒有修改該題目的權限</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_title_lines">無效的"標題"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_question_lines">無效的"題目"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_ashtml_lines">無效的"作爲HTML 處理"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_source_lines">無效的"來源"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_target_lines">無效的"目標"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_answers_lines">無效的"答案"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_scores_lines">無效的"分數"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_difficulty_lines">無效的"難度"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_status_lines">無效的"狀態"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_description_lines">無效的"描述"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column_header_lines">無效的資訊欄</xsl:with-param>
			<xsl:with-param name="lab_err_duplicate_column_header_lines">資訊欄重複出現</xsl:with-param>
			<xsl:with-param name="lab_err_missing_column_header_lines">必須提供下列資訊欄</xsl:with-param>
			<!-- -->
			<xsl:with-param name="lab_no_upload_record">上載檔沒有題目</xsl:with-param>
			<xsl:with-param name="lab_line">行數</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">條題目資料出現錯誤:</xsl:with-param>
			<xsl:with-param name="lab_error_msg">請糾正錯誤並重新匯入檔。</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_file_format">無效的檔案格式</xsl:with-param>
			<xsl:with-param name="lab_invalid_encoding">上傳檔案的編碼方式必須為Unicode.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">导入题目资源 - 第二步：确认</xsl:with-param>
			<xsl:with-param name="lab_error_title">导入题目资源 - 错误</xsl:with-param>
			<xsl:with-param name="lab_desc">下列是文件中的配对题。单击屏幕底部的<b>确定</b> 按钮完成导入，或单击<b>取消</b> 按钮停止操作。</xsl:with-param>
			<xsl:with-param name="lab_score">分数: </xsl:with-param>
			<xsl:with-param name="lab_answers">答案： </xsl:with-param>
			<xsl:with-param name="lab_col_category">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_general">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_online">已发布</xsl:with-param>
			<xsl:with-param name="lab_offline">未发布</xsl:with-param>
			<xsl:with-param name="lab_no_explanation">没有说明</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">确认</xsl:with-param>
			<!-- -->
			<xsl:with-param name="lab_err_others">其他错误</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_category_lines">无效的"资源文件夹"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_resource_lines">无效的"资源编号"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_permission_lines">没有修改该题目的权限</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_title_lines">无效的"标题"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_question_lines">无效的"题目"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_ashtml_lines">无效的"作为HTML 处理"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_source_lines">无效的"来源"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_target_lines">无效的"目标"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_answers_lines">无效的"答案"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_scores_lines">无效的"分数"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_difficulty_lines">无效的"难度"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_status_lines">无效的"状态"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_description_lines">无效的"描述"</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column_header_lines">无效的信息栏</xsl:with-param>
			<xsl:with-param name="lab_err_duplicate_column_header_lines">信息栏重复出现</xsl:with-param>
			<xsl:with-param name="lab_err_missing_column_header_lines">必须提供下列信息栏</xsl:with-param>
				<!-- -->
			<xsl:with-param name="lab_line">行数</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">上载文件没有题目</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">条题目资料出现错误：</xsl:with-param>
			<xsl:with-param name="lab_error_msg">请纠正错误并重新导入文件。</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_file_format">无效的文件格式</xsl:with-param>
			<xsl:with-param name="lab_invalid_encoding">请对上传的文件使用Unicode编码格式。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Import question – step 2: confirmation</xsl:with-param>
			<xsl:with-param name="lab_error_title">Import question – error</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the questions detected from the file. Click <b>Confirm</b> at the bottom to complete the import, or <b>Cancel</b> to stop the process.</xsl:with-param>
			<xsl:with-param name="lab_col_category">Resource folder</xsl:with-param>
			<xsl:with-param name="lab_score">Score: </xsl:with-param>
			<xsl:with-param name="lab_answers">Answers： </xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_general">Normal</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_online">Published</xsl:with-param>
			<xsl:with-param name="lab_offline">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_no_explanation">No explanation</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">Ok</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_err_others">Others error</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_category_lines">Invalid folder ID</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_resource_lines">Invalid resource ID</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_permission_lines">No right to modify the question</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_title_lines">Invalid values for Title</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_question_lines">Invalid values for question</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_ashtml_lines">Invalid as HTML</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_source_lines">Invalid values for source</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_target_lines">Invalid values for target</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_answers_lines">Invalid values for answers</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_scores_lines">Invalid values for scores</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_difficulty_lines">Invalid values for difficulty</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_status_lines">Invalid values for status</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_description_lines">Invalid values for Description</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">No upload record</xsl:with-param>
			<xsl:with-param name="lab_line">Line</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">error(s) are found in the file:</xsl:with-param>
			<xsl:with-param name="lab_error_msg">Please correct the errors and import the file again.</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column_header_lines">Invalid column header</xsl:with-param>
			<xsl:with-param name="lab_err_duplicate_column_header_lines">Duplicated column name</xsl:with-param>
			<xsl:with-param name="lab_err_missing_column_header_lines">The following column(s) must be supplied</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_file_format">Invalid file format</xsl:with-param>
			<xsl:with-param name="lab_invalid_encoding">The import file is not saved in unicode text format.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_err_invalid_category_lines"/>
		<xsl:param name="lab_err_invalid_resource_lines"/>
		<xsl:param name="lab_err_invalid_permission_lines"/>
		<xsl:param name="lab_err_invalid_title_lines"/>
		<xsl:param name="lab_err_invalid_question_lines"/>
		<xsl:param name="lab_err_invalid_ashtml_lines"/>
		<xsl:param name="lab_err_invalid_source_lines"/>
		<xsl:param name="lab_err_invalid_target_lines"/>
		<xsl:param name="lab_err_invalid_answers_lines"/>
		<xsl:param name="lab_err_invalid_scores_lines"/>
		<xsl:param name="lab_err_invalid_difficulty_lines"/>
		<xsl:param name="lab_err_invalid_status_lines"/>
		<xsl:param name="lab_err_invalid_description_lines"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_error_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_answers"/>
		<xsl:param name="lab_col_category"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_general"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_err_others"/>
		<xsl:param name="lab_no_upload_record"/>
		<xsl:param name="lab_no_explanation"/>
		<xsl:param name="lab_line"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_errors_are_found"/>
		<xsl:param name="lab_error_msg"/>
		<xsl:param name="lab_err_invalid_column_header_lines"/>
		<xsl:param name="lab_err_duplicate_column_header_lines"/>
		<xsl:param name="lab_err_missing_column_header_lines"/>
		<xsl:param name="lab_err_invalid_file_format"/>
		<xsl:param name="lab_invalid_encoding"/>

		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$upload_failure_cnt = 0">
						<xsl:value-of select="$lab_title"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_error_title"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="$upload_failure_cnt = 0">
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_desc"/>
			</xsl:call-template>			
		</xsl:if>
		<xsl:call-template name="wb_ui_line"/>		

		<xsl:choose>
			<xsl:when test="name() = 'invalid_file'">
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="wbRowText">
								<xsl:value-of select="$lab_err_invalid_file_format"/>
							</span>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="wbRowText">
								<xsl:value-of select="$lab_error_msg"/>
							</span>
						</td>
					</tr>
				</table>				
			</xsl:when>
			<xsl:when test="name() = 'invalid_encoding'">
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="wbRowText">
								<xsl:value-of select="$lab_invalid_encoding"/>
							</span>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="wbRowText">
								<xsl:value-of select="$lab_error_msg"/>
							</span>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="name() = 'invalid_col_name_list'">
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="wbRowText">
								<xsl:choose>
									<xsl:when test="@type = 'DUPLICATE'">
										<xsl:value-of select="$lab_err_duplicate_column_header_lines"/>
									</xsl:when>
									<xsl:when test="@type = 'MISSING'">
										<xsl:value-of select="$lab_err_missing_column_header_lines"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="$lab_err_invalid_column_header_lines"/>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
					<xsl:for-each select="invalid_col_name">
						<tr>
							<td width="10">
								<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
							</td>			
							<td width="750">
								<span class="wbRowText">
									<xsl:value-of select="text()"/>
								</span>
							</td>
						</tr>
					</xsl:for-each>
					<tr>
						<td colspan="2">
							<span class="wbRowText">
								<xsl:value-of select="$lab_error_msg"/>
							</span>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="$upload_failure_cnt != 0">
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<xsl:if test="$upload_failure_cnt != '' and $upload_failure_cnt != '0'">
								<table cellpadding="3" cellspacing="0" border="0" width="100%">
									<tr>
										<td>
											<span class="wbRowText">
												<xsl:value-of select="$upload_failure_cnt"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="$lab_errors_are_found"/>
											</span>
										</td>
									</tr>
								</table>
							</xsl:if>
							<table cellpadding="3" cellspacing="0" border="0" width="100%">
								<xsl:for-each select="*[line]">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td colspan="3">
											<span class="wbRowText">
												<xsl:value-of select="$lab_line"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:for-each select="line">
													<xsl:value-of select="@num"/>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>,&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
												<xsl:text>：</xsl:text>
												<xsl:text>&#160;</xsl:text>
												<xsl:choose>
													<xsl:when test="name() = 'invalid_category_lines'">
														<xsl:value-of select="$lab_err_invalid_category_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_resource_lines'">
														<xsl:value-of select="$lab_err_invalid_resource_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_permission_lines'">
														<xsl:value-of select="$lab_err_invalid_permission_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_title_lines'">
														<xsl:value-of select="$lab_err_invalid_title_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_question_lines'">
														<xsl:value-of select="$lab_err_invalid_question_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_ashtml_lines'">
														<xsl:value-of select="$lab_err_invalid_ashtml_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_source_lines'">
														<xsl:value-of select="$lab_err_invalid_source_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_target_lines'">
														<xsl:value-of select="$lab_err_invalid_target_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_answers_lines'">
														<xsl:value-of select="$lab_err_invalid_answers_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_scores_lines'">
														<xsl:value-of select="$lab_err_invalid_scores_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_difficulty_lines'">
														<xsl:value-of select="$lab_err_invalid_difficulty_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_status_lines'">
														<xsl:value-of select="$lab_err_invalid_status_lines"/>
													</xsl:when>
													<xsl:when test="name() = 'invalid_description_lines'">
														<xsl:value-of select="$lab_err_invalid_description_lines"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="$lab_err_others"/>
													</xsl:otherwise>
												</xsl:choose>
											</span>
										</td>
									</tr>
								</xsl:for-each>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<table cellpadding="3" cellspacing="0" border="0" width="100%">
								<tr>
									<td>
										<span class="wbRowText">
											<xsl:value-of select="$lab_error_msg"/>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="count(question) &gt;=1">
						<div style="overflow-x: scroll;">
							<table border="0" cellspacing="0" cellpadding="0" width="100%" class="table wzb-ui-table">
								<tr class="wbRowHeadBarBg wzb-ui-table-head">						
									<xsl:apply-templates select="../used_column/column" mode="draw_header">
										<xsl:with-param name="lab_col_category" select="$lab_col_category"/>
									</xsl:apply-templates>
								</tr>
								<xsl:apply-templates select="question">
									<xsl:with-param name="lab_no" select="$lab_no"/>
									<xsl:with-param name="lab_yes" select="$lab_yes"/>
									<xsl:with-param name="lab_easy" select="$lab_easy"/>
									<xsl:with-param name="lab_general" select="$lab_general"/>
									<xsl:with-param name="lab_hard" select="$lab_hard"/>
									<xsl:with-param name="lab_online" select="$lab_online"/>
									<xsl:with-param name="lab_offline" select="$lab_offline"/>
									<xsl:with-param name="lab_no_explanation" select="$lab_no_explanation"/>
									<xsl:with-param name="lab_score" select="$lab_score"/>
									<xsl:with-param name="lab_answers" select="$lab_answers"/>
								</xsl:apply-templates>
							</table>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<table border="0" cellspacing="0" cellpadding="3" width="{$wb_gen_table_width}">
							<tr class="wbRowsOdd">						
								<td align="center"><span class="wbRowText">
									<xsl:value-of select="$lab_no_upload_record"/>
								</span></td>
							</tr>
						</table>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		
		<div class="wzb-bar">
			<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0" class="wbGenFooterBarBg">
				<tr>
					<td align="center">
						<xsl:choose>
							<xsl:when test="$upload_failure_cnt != 0">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Import.prep_page()</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="$upload_success_cnt &gt;= 1">
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Res.Import.confirm(document.frmXml, 'MT')</xsl:with-param>
									</xsl:call-template>
									<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								</xsl:if>
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
									<xsl:with-param name="wb_gen_btn_href">
										<xsl:choose>
											<xsl:when test="not($ulg_id) or $ulg_id = ''">javascript:Batch.Res.Import.prep_page()</xsl:when>
											<xsl:otherwise>javascript:Batch.Res.Import.cancel(<xsl:value-of select="$ulg_id"/>, 'MT')</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</table>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="used_column"/>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_header">
		<xsl:param name="lab_col_category"/>
		<xsl:choose>
			<xsl:when test="@id = '0'">
				<td nowrap="nowrap">
					<span class="wbRowheadBarSText">
						<xsl:value-of select="$lab_col_category"/>
					</span>
				</td>
			</xsl:when>
			<xsl:when test="@id = '24' or @id = '25'">
			</xsl:when>
			<xsl:when test="(number(@id) &lt; 4) or (number(@id) &gt; 25)">
				<td nowrap="nowrap">
					<span class="wbRowheadBarSText">
						<xsl:value-of select="text()"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<!--
			<xsl:otherwise>
				<td nowrap="nowrap">
					<span class="wbRowheadBarSText">
						<xsl:value-of select="text()"/>
					</span>
				</td>
			</xsl:otherwise>
			-->
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="question">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_general"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_no_explanation"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_answers"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">wbRowsEven</xsl:when>
				<xsl:otherwise>wbRowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr valign="middle" class="{$row_class}">
			<xsl:apply-templates select="../../used_column/column" mode="draw_item">
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
				<xsl:with-param name="question" select="."/>
				<xsl:with-param name="lab_easy" select="$lab_easy"/>
				<xsl:with-param name="lab_general" select="$lab_general"/>
				<xsl:with-param name="lab_hard" select="$lab_hard"/>
				<xsl:with-param name="lab_online" select="$lab_online"/>
				<xsl:with-param name="lab_offline" select="$lab_offline"/>
				<xsl:with-param name="lab_no_explanation" select="$lab_no_explanation"/>
			</xsl:apply-templates>
		</tr>
		<tr valign="middle" class="{$row_class}">
			<td valign="top" align="left" colspan="8">
				<span class="wbRowText">
					<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="wbGenFooterBarBg wzb-ui-table-2">
						<xsl:variable name="numRow" select="count(body/interaction)"/>
						<xsl:for-each select="outcome">
							<tr valign="middle" class="{$row_class}">
								<xsl:if test="position() = 1">
									<td width="100" valign="center" rowspan="{$numRow}" style="border:0;">
										<span class="wbRowheadBarSText">
											<xsl:value-of select="$lab_answers"/>
										</span>
									</td>
								</xsl:if>
								<xsl:call-template name="draw_matching_gai">
										<xsl:with-param name="lab_score" select="$lab_score"/>
								</xsl:call-template>
							</tr>
						</xsl:for-each>
					</table>
				</span>
			</td>
		</tr>
				
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_matching">
		<xsl:param name="lab_score"/>
		<xsl:variable name="score" select="@score"/>
		<xsl:variable name="order" select="@order"/>
		<xsl:variable name="feedback" select="../../outcome[@order=$order]/feedback/@condition"/>
		<xsl:variable name="source" select="../source/item[@id=$feedback]/text"/>
		<td nowrap="nowrap" valign="top" align="left">
			<span class="wbRowText">
				<xsl:if test="$source !='' and text!=''">
				<xsl:value-of select="$source"/> - <xsl:value-of select="text"/> (<xsl:value-of select="$lab_score"/> <xsl:value-of select="@score"/>)
				</xsl:if>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_matching_gai">
		<xsl:param name="lab_score"/>
		<xsl:for-each select="feedback">
			<xsl:variable name="score" select="@score"/>
			<xsl:variable name="sourceorder" select="../@order"/>
			<xsl:variable name="feedback" select="@condition"/>
			<xsl:variable name="source" select="../../body/source/item[@id=$feedback]/text"/>
			<xsl:variable name="tager_text" select="../../body/interaction[@order=$sourceorder]/text"/>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:if test="$source !='' and $tager_text!=''">
					<xsl:value-of select="$source"/> - <xsl:value-of select="$tager_text"/> (<xsl:value-of select="$lab_score"/> <xsl:value-of select="@score"/>)
					</xsl:if>
				</span>
			</td>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_item">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="question"/>
		<xsl:param name="lab_no_explanation"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_general"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		
		<xsl:choose>
			<xsl:when test="@id = '0'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:value-of select="$question/header/objective/@title"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '1'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:value-of select="$question/@resource_id"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '2'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:value-of select="$question/header/title/text()"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '3'">
				<td valign="top" align="left">
					<span class="wbRowText">
						<xsl:for-each select="$question/body/text()|$question/body/html">
								<xsl:choose>
									<xsl:when test="name() = 'html'">
										<xsl:value-of disable-output-escaping="yes" select="."/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="unescape_html_linefeed">
											<xsl:with-param name="my_right_value"><xsl:value-of select="."/></xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '4'">
				
			</xsl:when>
			<!--
			<xsl:when test="text() = 'Source 1'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=1]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 2'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=2]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 3'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=3]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 4'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=4]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 5'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=5]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 6'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=6]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 7'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=7]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 8'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=8]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 9'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=9]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Source 10'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/source/item[@id=10]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>			
			<xsl:when test="text() = 'Target 1'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=1]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 2'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=2]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 3'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=3]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 4'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=4]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 5'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=5]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 6'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=6]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 7'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=7]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 8'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=8]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 9'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=9]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Target 10'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText"><xsl:value-of select="$question/body/interaction[@order=10]/text"/></span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>			
			<xsl:when test="text() = 'Answers'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:for-each select="$question/outcome">
							<xsl:value-of select="feedback/@condition"/>
							<xsl:if test="position() != last()">
								<xsl:text>,</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Scores'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:for-each select="$question/body/interaction">
							<xsl:value-of select="@score"/>
							<xsl:if test="position() != last()">
								<xsl:text>,</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			-->
			<xsl:when test="@id = '26'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:choose>
							<xsl:when test="$question/header/@difficulty = '1'">
								<xsl:value-of select="$lab_easy"/>
							</xsl:when>
							<xsl:when test="$question/header/@difficulty = '2'">
								<xsl:value-of select="$lab_general"/>
							</xsl:when>
							<xsl:when test="$question/header/@difficulty = '3'">
								<xsl:value-of select="$lab_hard"/>
							</xsl:when>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Duration'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:value-of select="$question/header/@duration"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '27'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:choose>
							<xsl:when test="$question/header/@status = 'ON'">
								<xsl:value-of select="$lab_online"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_offline"/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '28'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:call-template name="unescape_html_linefeed">
							<xsl:with-param name="my_right_value"><xsl:value-of select="$question/header/desc/text()"/></xsl:with-param>
						</xsl:call-template>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Explanation'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="wbRowText">
						<xsl:choose>
							<xsl:when test="$question/explanation/rationale[position() = 1]/. = 'No explanation provided'">
								<xsl:value-of select="$lab_no_explanation"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$question/explanation/rationale[position() = 1]/."/>
							</xsl:otherwise>
						</xsl:choose>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<!--
			<xsl:otherwise>
				<td nowrap="nowrap" valign="top" align="left">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:otherwise>
			-->
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_ans">
		<xsl:param name="question"/>
		<xsl:param name="ans_choice"/>
		<span class="wbRowSText">
			<xsl:choose>
				<xsl:when test="$question/body/interaction/option[position() = $ans_choice]/. != ''">
					<xsl:choose>
						<xsl:when test="$question/body/interaction/option[position() = $ans_choice]/html">
							<xsl:value-of disable-output-escaping="yes" select="$question/body/interaction/option[position() = $ans_choice]/."/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value"><xsl:value-of select="$question/body/interaction/option[position() = $ans_choice]/."/></xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
<!--					<xsl:value-of select="$question/body/interaction/option[position() = $ans_choice]/."/>	-->
				</xsl:when>
				<xsl:otherwise>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</xsl:template>
</xsl:stylesheet>

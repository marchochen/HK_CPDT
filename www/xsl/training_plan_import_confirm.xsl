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
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="upload_success_cnt" select="count(/tptrainingplan/plan_list/plan)"/>
	<xsl:variable name="upload_failure_cnt" select="count(//line)"/>
	<xsl:variable name="tcr_id" select="/tptrainingplan/meta/plan_prep_info/tcr_id"/>
	<xsl:variable name="year" select="/tptrainingplan/meta/plan_prep_info/year"/>
	<xsl:variable name="ypn_year" select="/tptrainingplan/meta/plan_prep_info/ypn_year"/>
	<xsl:variable name="type" select="/tptrainingplan/plan_list/@type"/>
	<xsl:variable name="upd_timestamp" select="/tptrainingplan/meta/upd_timestamp"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="tptrainingplan/plan_list"/>
			<xsl:apply-templates select="tptrainingplan/invalid_list"/>
			<xsl:apply-templates select="tptrainingplan/invalid_col_name_list"/>
			<xsl:apply-templates select="tptrainingplan/invalid_file"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match=" invalid_list | invalid_col_name_list | plan_list | invalid_file">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_training_plan.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			plan = new wbTrainingPlan;
	]]></script>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="new_css"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return status()" enctype="multipart/form-data">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="upd_timestamp" value="{$upd_timestamp}"/>
				<input type="hidden" name="tcr_id" value="{$tcr_id}"/>
				<input type="hidden" name="year" value="{$year}"/>
				<input type="hidden" name="ypn_year" value="{$ypn_year}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">導入年度計劃 - 第二步：確認</xsl:with-param>
			<xsl:with-param name="lab_error_title">導入年度計劃 - 錯誤</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是文件中的計劃信息。按屏幕底部的<b>確定</b> 按鈕完成導入，或按<b>取消</b> 按鈕停止操作。</xsl:with-param>
			<xsl:with-param name="lab_err_col_empty">列不能為空</xsl:with-param>
			<xsl:with-param name="lab_err_format">格式有誤</xsl:with-param>
			<xsl:with-param name="lab_err_too_long">的內容過長</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_found">找不到指定的</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_content">指定的內容無效</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">確認</xsl:with-param>
			<xsl:with-param name="lab_err_others">其他錯誤</xsl:with-param>
			<xsl:with-param name="lab_from">第</xsl:with-param>
			<xsl:with-param name="lab_row">行</xsl:with-param>
			<xsl:with-param name="lab_col">列</xsl:with-param>
			<xsl:with-param name="lab_line">行數</xsl:with-param>
			<xsl:with-param name="lab_col">列數</xsl:with-param>
			<xsl:with-param name="lab_error_msg">請糾正錯誤並重新導入文件。</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_file">找不到指定的文件</xsl:with-param>
			<xsl:with-param name="lab_spec_empty">警告：未指定</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column_header_lines">無效的信息欄</xsl:with-param>
			<xsl:with-param name="lab_err_duplicate_column_header_lines">信息欄重複出現</xsl:with-param>
			<xsl:with-param name="lab_err_missing_column_header_lines">必須提供下列信息欄</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_xls_sheet">在上傳文件中未找到名稱為「年度規劃填寫」的工作表。包含導入數據的工作表名稱必須為「年度規劃填寫」。</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">上載文件沒有年度計劃</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">個年度計劃出現錯誤：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">导入年度计划 - 第二步：确认</xsl:with-param>
			<xsl:with-param name="lab_error_title">导入年度计划 - 错误</xsl:with-param>
			<xsl:with-param name="lab_desc">以下是文件中的计划信息。单击屏幕底部的<b>确定</b> 按钮完成导入，或单击<b>取消</b> 按钮停止操作。</xsl:with-param>
			<xsl:with-param name="lab_err_col_empty">列不能为空</xsl:with-param>
			<xsl:with-param name="lab_err_format">格式有误</xsl:with-param>
			<xsl:with-param name="lab_err_too_long">的内容过长</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_found">找不到指定的</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_content">指定的内容无效</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">确认</xsl:with-param>
			<xsl:with-param name="lab_err_others">其他错误</xsl:with-param>
			<xsl:with-param name="lab_from">第</xsl:with-param>
			<xsl:with-param name="lab_row">行</xsl:with-param>
			<xsl:with-param name="lab_col">列</xsl:with-param>
			<xsl:with-param name="lab_line">行数</xsl:with-param>
			<xsl:with-param name="lab_col">列数</xsl:with-param>
			<xsl:with-param name="lab_error_msg">请纠正错误并重新导入文件。</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_file">找不到指定的文件</xsl:with-param>
			<xsl:with-param name="lab_spec_empty">警告：未指定</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column_header_lines">无效的信息栏</xsl:with-param>
			<xsl:with-param name="lab_err_duplicate_column_header_lines">信息栏重复出现</xsl:with-param>
			<xsl:with-param name="lab_err_missing_column_header_lines">必须提供下列信息栏</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_xls_sheet">在上传文件中未找到名称为“年度规划填写”的工作表。包含导入数据的工作表名称必须为“年度规划填写”。</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">上载文件没有年度计划</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">个年度计划出现错误：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Import annual training plan – step 2: confirmation</xsl:with-param>
			<xsl:with-param name="lab_error_title">Import annual training plan – error</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the training plan detected from the file. Click <b>Confirm</b> at the bottom to complete the import, or <b>Cancel</b> to stop the process.</xsl:with-param>
			<xsl:with-param name="lab_err_col_empty"> must be specified</xsl:with-param>
			<xsl:with-param name="lab_err_format"> format is incorrect</xsl:with-param>
			<xsl:with-param name="lab_err_too_long"> is too long</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_found"> is invalid</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_content"> is invalid</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_err_others">Other error</xsl:with-param>
			<xsl:with-param name="lab_from">From </xsl:with-param>
			<xsl:with-param name="lab_row">Line</xsl:with-param>
			<xsl:with-param name="lab_col">Column</xsl:with-param>
			<xsl:with-param name="lab_line">Line</xsl:with-param>
			<xsl:with-param name="lab_col">Column</xsl:with-param>
			<xsl:with-param name="lab_error_msg">Please correct the error and import again.</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_file"> file not found</xsl:with-param>
			<xsl:with-param name="lab_spec_empty">Warning ：not specified</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column_header_lines">invalid column header</xsl:with-param>
			<xsl:with-param name="lab_err_duplicate_column_header_lines">column header is repeated</xsl:with-param>
			<xsl:with-param name="lab_err_missing_column_header_lines">Please provide the following column header</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_xls_sheet">Cannot found a valid worksheet from the upload file.</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">The upload file does not contain any training plan</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">errors found：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_error_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_err_invalid_xls_sheet"/>
		<xsl:param name="lab_err_col_empty"/>
		<xsl:param name="lab_err_format"/>
		<xsl:param name="lab_err_too_long"/>
		<xsl:param name="lab_err_invalid_found"/>
		<xsl:param name="lab_err_invalid_content"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_err_others"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_row"/>
		<xsl:param name="lab_col"/>
		<xsl:param name="lab_line"/>
		<xsl:param name="lab_error_msg"/>
		<xsl:param name="lab_err_invalid_file"/>
		<xsl:param name="lab_spec_empty"/>
		<xsl:param name="lab_err_duplicate_column_header_lines"/>
		<xsl:param name="lab_err_missing_column_header_lines"/>
		<xsl:param name="lab_err_invalid_content_lines"/>
		<xsl:param name="lab_no_upload_record"/>
		<xsl:param name="lab_err_invalid_column_header_lines"/>
		<xsl:param name="lab_errors_are_found"/>
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
		<xsl:choose>
			<xsl:when test="name() = 'invalid_file'">
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="Text">
								<xsl:value-of select="$lab_err_invalid_file"/>
							</span>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="Text">
								<xsl:value-of select="$lab_error_msg"/>
							</span>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="name() = 'invalid_col_name_list'">
			
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td colspan="2">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="Text">
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
								<span class="Text">
									<xsl:value-of select="text()"/>
								</span>
							</td>
						</tr>
					</xsl:for-each>
					<tr>
						<td colspan="2">
							<span class="Text">
								<xsl:value-of select="$lab_error_msg"/>
							</span>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="$upload_failure_cnt != 0 and not(//invalid_xls_sheet)">
				<xsl:call-template name="wb_ui_line"/>
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td colspan="2">
							<span class="Text">
								<xsl:value-of select="$lab_err_invalid_column_header_lines"/>
							</span>
						</td>
					</tr>
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<xsl:if test="$upload_failure_cnt != '' and $upload_failure_cnt != '0'">
								<table cellpadding="3" cellspacing="0" border="0" width="100%">
									<tr>
										<td>
											<span class="Text">
												<xsl:value-of select="$upload_failure_cnt"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:value-of select="$lab_errors_are_found"/>
											</span>
										</td>
									</tr>
								</table>
							</xsl:if>
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<xsl:for-each select="invalid_lines/line">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td colspan="3">
											<span class="Text">
												<xsl:choose>
													<xsl:when test="error_type='EMPTY_COLUMN_ERROR'">
														<xsl:value-of select="$lab_from"/>
														<xsl:value-of select="@row"/>
														<xsl:value-of select="$lab_row"/>:"<xsl:value-of select="invalid_field"/>"<xsl:value-of select="$lab_err_col_empty"/>
													</xsl:when>
													<xsl:when test="error_type='FORMAT_ERROR'">
														<xsl:value-of select="$lab_from"/>
														<xsl:value-of select="@row"/>
														<xsl:value-of select="$lab_row"/>:"<xsl:value-of select="invalid_field"/>"<xsl:value-of select="$lab_err_format"/>
													</xsl:when>
													<xsl:when test="error_type='TOO_LONG_ERROR'">
														<xsl:value-of select="$lab_from"/>
														<xsl:value-of select="@row"/>
														<xsl:value-of select="$lab_row"/>:"<xsl:value-of select="invalid_field"/>"<xsl:value-of select="$lab_err_too_long"/>
													</xsl:when>
													<xsl:when test="error_type='INVALID_CNT_ERROR'">
														<xsl:value-of select="$lab_from"/>
														<xsl:value-of select="@row"/>
														<xsl:value-of select="$lab_row"/>:"<xsl:value-of select="invalid_field"/>"<xsl:value-of select="$lab_err_invalid_content"/>
													</xsl:when>
													<xsl:when test="error_type='NOT_FOUND_ERROR'">
														<xsl:value-of select="$lab_from"/>
														<xsl:value-of select="@row"/>
														<xsl:value-of select="$lab_row"/>:<xsl:value-of select="$lab_err_invalid_found"/>"<xsl:value-of select="invalid_field"/>"
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
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td>
										<span class="Text">
											<xsl:value-of select="$lab_error_msg"/>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:when test="//invalid_xls_sheet">
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<xsl:value-of select="$lab_err_invalid_xls_sheet"/>
						</td>
					</tr>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="count(plan) &gt;=1">
						<table class="table wzb-ui-table" border="0" cellspacing="0" cellpadding="0" width="{$wb_gen_table_width}">
							<tr class="SecBg wzb-ui-table-head">
								<td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<xsl:apply-templates select="//plan_column/column" mode="draw_header">
								</xsl:apply-templates>
								<td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							<xsl:for-each select="plan">
								<xsl:variable name="row_class">
									<xsl:choose>
										<xsl:when test="position() mod 2">RowsOdd</xsl:when>
										<xsl:otherwise>RowsEven</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:call-template name="plan">
									<xsl:with-param name="row_class" select="$row_class"/>
								</xsl:call-template>
							</xsl:for-each>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_upload_record"/>
						</xsl:call-template>
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
									<xsl:with-param name="wb_gen_btn_href">javascript:plan.Import.cancel('<xsl:value-of select="$tcr_id"/>','<xsl:value-of select="$year"/>','<xsl:value-of select="$ypn_year"/>','<xsl:value-of select="$upd_timestamp"/>');</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="$upload_success_cnt &gt;= 1">
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:plan.Import.exec(document.frmXml)</xsl:with-param>
									</xsl:call-template>
									<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								</xsl:if>
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:plan.Import.cancel('<xsl:value-of select="$tcr_id"/>','<xsl:value-of select="$year"/>','<xsl:value-of select="$ypn_year"/>','<xsl:value-of select="$upd_timestamp"/>');</xsl:with-param>
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
	<xsl:template match="column" mode="draw_header">
		<td nowrap="nowrap">
			<span class="RowheadBarSText">
				<xsl:value-of select="text()"/>
			</span>
		</td>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="plan">
		<xsl:param name="row_class"/>
		<tr valign="middle" class="{$row_class}">
			<td width="4">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="substring(plan_date/text(), 0,8)"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="name/text()"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="cos_type/text()"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="tnd_title/text()"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="introduction/text()"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="aim/text()"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<!--<xsl:value-of select="res_person_list/usr/@display"/>-->
					<xsl:value-of select="target/text()"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="responser/text()"/>
					<!--<xsl:value-of select="master_list/usr/@display"/> -->
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:if test="duration !='0.0'">
						<xsl:value-of select="duration/text()"/>
					</xsl:if>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="ftf_start_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="ftf_end_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="wb_start_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:call-template name="display_time">
						<xsl:with-param name="my_timestamp">
							<xsl:value-of select="wb_end_date/text()"/>
						</xsl:with-param>
					</xsl:call-template>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:if test="lrn_count !='0'">
						<xsl:value-of select="lrn_count/text()"/>
					</xsl:if>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:if test="fee !='0.0'">
						<xsl:value-of select="fee/text()"/>
					</xsl:if>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<span class="wbRowText">
					<xsl:value-of select="remarks/text()"/>
				</span>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<td nowrap="nowrap" valign="top" align="left">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>

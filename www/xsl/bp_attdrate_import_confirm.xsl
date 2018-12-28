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
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/wb_module_status_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="wb_gen_table_width">600</xsl:variable>
	<xsl:variable name="upload_failure_cnt" select="count(/attdance_rate/fail_list/line)"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess
	]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="new_css" />
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form name="frmXml" onsubmit="return status()">
					<input type="hidden" name="cmd"/>
					<input type="hidden" name="module" value=""/>
					<input type="hidden" name="itm_id"/>
					<input type="hidden" name="url_success" value=""/>
					<input type="hidden" name="url_failure" value=""/>
					<input type="hidden" name="stylesheet" value=""/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="attdance_rate">
			<xsl:with-param name="lab_title">匯入出席率–第二步： 確認</xsl:with-param>
			<xsl:with-param name="lab_error_title">匯入出席率–錯誤</xsl:with-param>
			<xsl:with-param name="lab_desc">以下所列是從檔中發現的記錄。單擊螢幕底部的<b>確定</b> 按鈕完成匯入，或單擊<b>取消</b> 按鈕停止操作。</xsl:with-param>
			<xsl:with-param name="lab_err_others">其他錯誤</xsl:with-param>
			<xsl:with-param name="lab_edundant_err">有冗餘列</xsl:with-param>
			<xsl:with-param name="lab_app_id_err">申請序號錯誤</xsl:with-param>
			<xsl:with-param name="lab_not_Enrolled">學員不是處於已報名狀態</xsl:with-param>
			<xsl:with-param name="lab_err_name">學員名字錯誤</xsl:with-param>
			<xsl:with-param name="lab_err_rate">出席率錯誤</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">沒有上載記錄</xsl:with-param>
			<xsl:with-param name="lab_total_upload">總數上載</xsl:with-param>
			<xsl:with-param name="lab_upload_success">上載成功</xsl:with-param>
			<xsl:with-param name="lab_upload_failure">上載失敗</xsl:with-param>
			<xsl:with-param name="lab_line">行數</xsl:with-param>
			<xsl:with-param name="lab_column">列數</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">顯示名稱</xsl:with-param>
			<xsl:with-param name="lab_password">密碼</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">確認</xsl:with-param>
			<xsl:with-param name="lab_usr_profiles">用戶資料</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">條用戶資料出現錯誤：</xsl:with-param>
			<xsl:with-param name="lab_detected_from_file">條用戶資料被查出：</xsl:with-param>
			<xsl:with-param name="lab_error_msg">請糾正錯誤並重新匯入檔。</xsl:with-param>
			<xsl:with-param name="lab_col_course_title">課程標題</xsl:with-param>
			<xsl:with-param name="lab_col_class_title">類別標題</xsl:with-param>
			<xsl:with-param name="lab_err_item_not_enroll_level">教學解決方案不適用</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="attdance_rate">
			<xsl:with-param name="lab_title">导入出席率–第二步： 确认</xsl:with-param>
			<xsl:with-param name="lab_error_title">导入出席率–错误</xsl:with-param>
			<xsl:with-param name="lab_desc">以下所列是从文件中发现记录。单击屏幕底部的<b>确定</b> 按钮完成导入，或单击<b>取消</b> 按钮停止操作。</xsl:with-param>
			<xsl:with-param name="lab_err_others">其他错误</xsl:with-param>
			<xsl:with-param name="lab_edundant_err">有冗余列</xsl:with-param>
			<xsl:with-param name="lab_app_id_err">申请序号错误</xsl:with-param>				
			<xsl:with-param name="lab_not_Enrolled">学员不是处于已报名状态</xsl:with-param>
			<xsl:with-param name="lab_err_name">学员名字错误</xsl:with-param>				
			<xsl:with-param name="lab_err_rate">出席率错误</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">没有上载记录</xsl:with-param>
			<xsl:with-param name="lab_total_upload">总数上载</xsl:with-param>
			<xsl:with-param name="lab_upload_success">上载成功</xsl:with-param>
			<xsl:with-param name="lab_upload_failure">上载失败</xsl:with-param>
			<xsl:with-param name="lab_line">行数</xsl:with-param>
			<xsl:with-param name="lab_column">列数</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">显示名称</xsl:with-param>
			<xsl:with-param name="lab_password">密码</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">确认</xsl:with-param>
			<xsl:with-param name="lab_usr_profiles">用户资料</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">条用户资料出现错误：</xsl:with-param>
			<xsl:with-param name="lab_detected_from_file">条用户资料被查出：</xsl:with-param>
			<xsl:with-param name="lab_error_msg">请纠正错误并重新导入文件。</xsl:with-param>
			<xsl:with-param name="lab_col_course_title">课程标题</xsl:with-param>
			<xsl:with-param name="lab_col_class_title">类别标题</xsl:with-param>
			<xsl:with-param name="lab_err_item_not_enroll_level">教学解决方案不适用</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="attdance_rate">
			<xsl:with-param name="lab_title">Import attendance rate – step 2: confirmation</xsl:with-param>
			<xsl:with-param name="lab_error_title">Import attendance rate – error</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the  results detected from the file. Click <b>Confirm</b> at the bottom to complete the import, or <b>Cancel</b> to stop the process.</xsl:with-param>
			<xsl:with-param name="lab_err_others">Others error</xsl:with-param>
			<xsl:with-param name="lab_edundant_err">Edundant column error</xsl:with-param>
			<xsl:with-param name="lab_app_id_err">Application ID error</xsl:with-param>			
			<xsl:with-param name="lab_not_Enrolled">Status not enrolled Error</xsl:with-param>		
			<xsl:with-param name="lab_err_name">Learner name eroor</xsl:with-param>			
			<xsl:with-param name="lab_err_rate">Attendance rate error</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">No upload record</xsl:with-param>
			<xsl:with-param name="lab_total_upload">Total uploaded</xsl:with-param>
			<xsl:with-param name="lab_upload_success">Successful uploaded</xsl:with-param>
			<xsl:with-param name="lab_upload_failure">Failed to upload</xsl:with-param>
			<xsl:with-param name="lab_line">Line</xsl:with-param>
			<xsl:with-param name="lab_column">Column</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">Display name</xsl:with-param>
			<xsl:with-param name="lab_password">Password</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">Ok</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_usr_profiles">User profiles</xsl:with-param>
			<xsl:with-param name="lab_detected_from_file">user profile(s) are detected from the file:</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">error(s) are found in the file:</xsl:with-param>
			<xsl:with-param name="lab_error_msg">Please correct the errors and import the file again.</xsl:with-param>
			<xsl:with-param name="lab_col_course_title">Course title</xsl:with-param>
			<xsl:with-param name="lab_col_class_title">Class title</xsl:with-param>
			<xsl:with-param name="lab_err_item_not_enroll_level">Learning solution not applicable</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="attdance_rate">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_error_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_err_others"/>
		<xsl:param name="lab_err_invalid_credit_type"/>
		<xsl:param name="lab_no_upload_record"/>
		<xsl:param name="lab_usr_manager"/>
		<xsl:param name="lab_usr_reg_approval"/>
		<xsl:param name="lab_usr_prof_maintain"/>
		<xsl:param name="lab_usr_bat_upload"/>
		<xsl:param name="lab_upload_usr_info"/>
		<xsl:param name="lab_preview"/>
		<xsl:param name="lab_upload_usr"/>
		<xsl:param name="lab_upload_failed"/>
		<xsl:param name="lab_upload_failed_duplicated_id_lines"/>
		<xsl:param name="lab_upload_failed_invalid_bday_lines"/>
		<xsl:param name="lab_upload_failed_others_failed_lines"/>
		<xsl:param name="lab_no_upload_usr"/>
		<xsl:param name="lab_first_name"/>
		<xsl:param name="lab_last_name"/>
		<xsl:param name="lab_form"/>
		<xsl:param name="lab_class"/>
		<xsl:param name="lab_class_num"/>
		<xsl:param name="lab_email"/>
		<xsl:param name="lab_student_id"/>
		<xsl:param name="lab_club_1"/>
		<xsl:param name="lab_club_2"/>
		<xsl:param name="lab_club_3"/>
		<xsl:param name="lab_club_4"/>
		<xsl:param name="lab_club_5"/>
		<xsl:param name="lab_total_upload"/>
		<xsl:param name="lab_upload_success"/>
		<xsl:param name="lab_upload_failure"/>
		<xsl:param name="lab_line"/>
		<xsl:param name="lab_column"/>
		<xsl:param name="lab_usr_display_bil"/>
		<xsl:param name="lab_password"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_confirm"/>
		<xsl:param name="lab_g_form_btn_back"/>
		<xsl:param name="lab_usr_profiles"/>
		<xsl:param name="lab_errors_are_found"/>
		<xsl:param name="lab_detected_from_file"/>
		<xsl:param name="lab_error_msg"/>
		<xsl:param name="lab_col_course_title"/>
		<xsl:param name="lab_col_class_title"/>
		<xsl:param name="lab_err_item_not_enroll_level"/>
		<xsl:param name="lab_edundant_err"/>
		<xsl:param name="lab_app_id_err"/>											
		<xsl:param name="lab_not_Enrolled"/>											
		<xsl:param name="lab_err_name"/>											
		<xsl:param name="lab_err_rate"	/>											


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
			<xsl:when test="$upload_failure_cnt != 0">
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="0" class="Bg">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<xsl:if test="$upload_failure_cnt != '' and $upload_failure_cnt != '0'">
								<table cellpadding="0" cellspacing="0" border="0" width="100%">
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
							
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td colspan="3">
											<span class="Text">
												<xsl:value-of select="$lab_line"/>
												<xsl:text>&#160;</xsl:text>
												<xsl:for-each select="attdance_rate/fail_list/line">
													<xsl:value-of select="@num"/>
													<xsl:text>：</xsl:text>
													<xsl:choose>
														<xsl:when test="@type='edundantColumn'">
															<xsl:value-of select="$lab_edundant_err"/>
														</xsl:when>	
														<xsl:when test="@type='noSuchApplication'">
															<xsl:value-of select="$lab_app_id_err"/>
														</xsl:when>
														<xsl:when test="@type='notEnrolled'">
															<xsl:value-of select="$lab_not_Enrolled"/>
														</xsl:when>
														<xsl:when test="@type='nameErr'">
															<xsl:value-of select="$lab_err_name"/>
														</xsl:when>
														<xsl:when test="@type='illogicRateRange'">
															<xsl:value-of select="$lab_err_rate"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="$lab_err_others"/>
														</xsl:otherwise>
													</xsl:choose>
													<xsl:choose>
														<xsl:when test="position() = last()"/>
														<xsl:otherwise>
															<xsl:text>&#160;,&#160;&#160;</xsl:text>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</span>
										</td>
									</tr>
								
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
			
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="count(attdance_rate/records/record) &gt;=1">
						<table border="0"  class="wzb-ui-table " cellspacing="0" cellpadding="0" width="{$wb_gen_table_width}">
							
								<!-- <td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td> -->
								<xsl:apply-templates select="attdance_rate/records/record" mode="attd"/>
								<!-- <td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td> -->
		
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
			<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3">
				<tr>
					<td>
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
				</tr>
				<tr>
					<td align="center">
						<xsl:choose>
							<xsl:when test="$upload_failure_cnt != 0">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:Batch.AttdRate.Import.confirm(document.frmXml,'<xsl:value-of select="attdance_rate/itm_id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									</xsl:call-template>
									<xsl:text>&#160;&#160;</xsl:text>
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
				</table>
		</div>

	</xsl:template>
	<!-- =============================================================== -->
		<xsl:template match="record" mode="attd">
		<xsl:variable name="position"><xsl:value-of select="position()"/></xsl:variable>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2 = 1">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<tr class="{$row_class}">
			<xsl:for-each select="column">
				<td width="10">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td>
					<span>
						<xsl:if test="$position=1"><xsl:attribute name="class">TitleText</xsl:attribute>
						                           <xsl:attribute name="style">color:#999999;</xsl:attribute></xsl:if>
						<xsl:value-of select="."/>
					</span>
				</td>
			</xsl:for-each>
		</tr>	
	</xsl:template>
		<!-- =============================================================== -->
</xsl:stylesheet>

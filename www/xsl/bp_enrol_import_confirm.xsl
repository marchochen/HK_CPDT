<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns1="http://www.cyberwisdom.net" version="1.0">
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
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="upload_success_cnt" select="Upload/upload_enrollment/record_count"/>
	<xsl:variable name="upload_failure_cnt" select="count(/Upload/upload_enrollment/failure_list/failure/line)"/>
	<xsl:variable name="upload_show_cnt" select="count(Upload/upload_enrollment/enterprise/membership)"/>
	<xsl:variable name="credit_type_list" select="Upload/upload_enrollment/credit_type_list"/>
	<xsl:variable name="item_code_ref_list" select="Upload/upload_enrollment/item_code_ref_list"/>
	<xsl:variable name="total_upload_cnt" select="$upload_success_cnt + $upload_failure_cnt"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="Upload/upload_enrollment"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="upload_enrollment">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" TYPE="text/javascript"><![CDATA[
			Batch = new wbBatchProcess
			app = new wbApplication
			itm_lst = new wbItem
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
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="upload_type" value="{@type}"/>
				<input type="hidden" name="itm_id" value="{/Upload/item/@id}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">匯入登記記錄和完成結果–第二步： 確認</xsl:with-param>
			<xsl:with-param name="lab_error_title">匯入登記記錄和完成結果–錯誤</xsl:with-param>
			<xsl:with-param name="lab_desc">下列是檔中有關報名和結訓的資訊（首<xsl:value-of select="$upload_show_cnt"/>個）。點擊底部的<b>確認</b>按鈕完成匯入，或點擊<b>取消</b>按鈕停止匯入操作。點擊<a href="javascript:Batch.Enrol.Import.viewall('{/Upload/item/@id}');">這裡</a>可以查看文件中的所有登記記錄和完成結果。</xsl:with-param>
			<xsl:with-param name="lab_err_others">其他錯誤</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_date_format">無效資料格式</xsl:with-param>
			<xsl:with-param name="lab_err_user_not_exist">用戶不存在</xsl:with-param>
			<xsl:with-param name="lab_err_user_not_target">用戶不是目標學員</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_completion_status">無效完成狀態</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_completion_date">完成狀態為“In Progress”時不允許有完成時間</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column">無效列格式</xsl:with-param>
			<xsl:with-param name="lab_err_item_not_exist">教學解決方案不存在</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_enrollment_status">無效登記狀態</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_numberic_format">無效資料格式</xsl:with-param>
			<xsl:with-param name="lab_err_missing_required_field">缺少必要的域</xsl:with-param>
			<xsl:with-param name="lab_err_duplicated_enrollment">報名記錄重復</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_credit_type">無效學分類型</xsl:with-param>
			<xsl:with-param name="lab_err_enrollment_n_completion_status_not_match">登記記錄和完成狀況不匹配</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">沒有記錄</xsl:with-param>
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
			<xsl:with-param name="lab_err_item_not_incharge">!!!The learning solution is not in-charged by you</xsl:with-param>
			<xsl:with-param name="lab_err_date_order">完成時間不能早于錄取時間</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_sheet">在上傳文件中未找到名稱為“Data”的工作表。包含導入資料的工作表名稱必須為“Data”。</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_usr_status">用戶已被刪除</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_usr_role">用戶沒有學員角色</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">處理报名</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">匯入報名記錄</xsl:with-param>
			<xsl:with-param name="lab_run_info">班級資訊</xsl:with-param>
			<xsl:with-param name="lab_errors_max_comment">"Completion Remarks" 的長度超出200個字符</xsl:with-param>
			<xsl:with-param name="lab_total_num">
				<xsl:text>&#160;&#160;&#160;&#160;</xsl:text>文件中的報名記錄數目: <xsl:value-of select="$upload_success_cnt"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">导入报名记录 – 第二步：确认</xsl:with-param>
			<xsl:with-param name="lab_error_title">导入报名记录和完成结果–错误</xsl:with-param>
			<xsl:with-param name="lab_desc">以下所列是文件中的前<xsl:value-of select="$upload_show_cnt"/>条有关报名和结训的信息。点击底部的<b>确认</b>按钮完成导入，或点击<b>取消</b>按钮停止导入操作。点击<a href="javascript:Batch.Enrol.Import.viewall({/Upload/item/@id});">这里</a>可以查看文件中的所有报名记录和完成结果。</xsl:with-param>
			<xsl:with-param name="lab_err_others">其他错误</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_date_format">无效数据格式</xsl:with-param>
			<xsl:with-param name="lab_err_user_not_exist">用户不存在</xsl:with-param>
			<xsl:with-param name="lab_err_user_not_target">用户不是目标学员</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_completion_status">无效完成状态</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_completion_date">完成状态为“In Progress”时不允许有完成时间</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column">无效列格式</xsl:with-param>
			<xsl:with-param name="lab_err_item_not_exist">教学解决方案不存在</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_enrollment_status">无效报名状态</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_numberic_format">无效数据格式</xsl:with-param>
			<xsl:with-param name="lab_err_missing_required_field">缺少必要的域</xsl:with-param>
			<xsl:with-param name="lab_err_duplicated_enrollment">报名记录重复</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_credit_type">无效学分类型</xsl:with-param>
			<xsl:with-param name="lab_err_enrollment_n_completion_status_not_match">报名记录和完成状况不匹配</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">没有记录</xsl:with-param>
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
			<xsl:with-param name="lab_err_item_not_incharge">!!!The learning solution is not in-charged by you</xsl:with-param>
			<xsl:with-param name="lab_err_date_order">完成时间不能早于录取时间</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_sheet">在上传文件中未找到名称为“Data”的工作表。包含导入数据的工作表名称必须为“Data”。</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_usr_status">用户已被删除</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_usr_role">用户没有学员角色</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">处理报名</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">导入报名记录</xsl:with-param>
			<xsl:with-param name="lab_run_info">班级信息</xsl:with-param>
			<xsl:with-param name="lab_errors_max_comment">"Completion Remarks" 的长度超出200个字符.</xsl:with-param>
			<xsl:with-param name="lab_total_num">
				<xsl:text>&#160;&#160;&#160;&#160;</xsl:text>文件中的报名记录数目: <xsl:value-of select="$upload_success_cnt"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">Import enrollment - step 2: confirmation</xsl:with-param>
			<xsl:with-param name="lab_error_title">Import enrollment and completion result – error</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the first <xsl:value-of select="$upload_show_cnt"/>
 enrollment and completion results detected from the file. Click <b>Confirm</b> at the bottom to complete the import, or <b>Cancel</b> to stop the precess. Click <a href="javascript:Batch.Enrol.Import.viewall({/Upload/item/@id});">here</a> to view all enrollment and completion results.</xsl:with-param>
			<xsl:with-param name="lab_err_others">Others error</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_date_format">Invalid date format</xsl:with-param>
			<xsl:with-param name="lab_err_user_not_exist">User not exist</xsl:with-param>
			<xsl:with-param name="lab_err_user_not_target">The learner does not belong to the target user groups of the class</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_completion_status">Invalid learning status</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_completion_date">Completion date not allowed for "In Progress" learning status</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_column">Invalid column format</xsl:with-param>
			<xsl:with-param name="lab_err_item_not_exist">Learning Solution not exist</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_enrollment_status">Invalid enrollment status</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_numberic_format">Invalid numberic format</xsl:with-param>
			<xsl:with-param name="lab_err_missing_required_field">Missing required field</xsl:with-param>
			<xsl:with-param name="lab_err_duplicated_enrollment">Duplicated enrollment</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_credit_type">Invalid credit type</xsl:with-param>
			<xsl:with-param name="lab_err_enrollment_n_completion_status_not_match">Enrollment and learning status not match</xsl:with-param>
			<xsl:with-param name="lab_no_upload_record">Record not found</xsl:with-param>
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
			<xsl:with-param name="lab_err_item_not_incharge">The learning solution is not in-charged by you</xsl:with-param>
			<xsl:with-param name="lab_err_date_order">Completion Date cannot be earlier than enrollment date</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_usr_status">User has been deleted</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_usr_role">User does not have the role of learner</xsl:with-param>
			<xsl:with-param name="lab_enrollment_approval">Enrollment</xsl:with-param>
			<xsl:with-param name="lab_enrollment_upload">Import enrollment</xsl:with-param>
			<xsl:with-param name="lab_run_info">Class information</xsl:with-param>
			<xsl:with-param name="lab_err_invalid_sheet">The worksheet named “Data” cannot be found in the uploaded file. The worksheet that is containing the data to be imported must be named “Data”.</xsl:with-param>
			<xsl:with-param name="lab_errors_max_comment">"Completion remarks" is longer than 200 characters</xsl:with-param>
			<xsl:with-param name="lab_total_num">
				<xsl:text>&#160;&#160;&#160;&#160;</xsl:text>Total number of detected enrollments: <xsl:value-of select="$upload_success_cnt"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_error_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_err_others"/>
		<xsl:param name="lab_err_invalid_date_format"/>
		<xsl:param name="lab_err_user_not_exist"/>
		<xsl:param name="lab_err_user_not_target"/>
		<xsl:param name="lab_err_invalid_completion_status"/>
		<xsl:param name="lab_err_invalid_completion_date"/>
		<xsl:param name="lab_err_invalid_column"/>
		<xsl:param name="lab_err_item_not_exist"/>
		<xsl:param name="lab_err_invalid_enrollment_status"/>
		<xsl:param name="lab_err_invalid_numberic_format"/>
		<xsl:param name="lab_err_missing_required_field"/>
		<xsl:param name="lab_err_duplicated_enrollment"/>
		<xsl:param name="lab_err_enrollment_n_completion_status_not_match"/>
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
		<xsl:param name="lab_err_item_not_incharge"/>
		<xsl:param name="lab_err_date_order"/>
		<xsl:param name="lab_err_invalid_sheet"/>
		<xsl:param name="lab_err_invalid_usr_status"/>
		<xsl:param name="lab_err_invalid_usr_role"/>
		<xsl:param name="lab_enrollment_approval"/>
		<xsl:param name="lab_enrollment_upload"/>
		<xsl:param name="lab_errors_max_comment"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_total_num"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
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
		<!--============================================================================-->
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="../item/@run_ind='true'">
						<a href="Javascript:itm_lst.get_item_detail({../item/parent/@id})" class="NavLink">
							<xsl:value-of select="../item/parent/@title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_list({../item/parent/@id})" class="NavLink">
							<xsl:choose>
								<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
							</xsl:choose>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_detail({../item/@id})" class="NavLink">
							<xsl:value-of select="../item/@title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="Javascript:itm_lst.get_item_detail({../item/@id})" class="NavLink">
							<xsl:value-of select="../item/@title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="Javascript:app.get_application_list('',{../item/@id})" class="NavLink">
					<xsl:value-of select="$lab_enrollment_approval"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:value-of select="$lab_enrollment_upload"/>
			</xsl:with-param>
		</xsl:call-template>
		<!--============================================================================-->
		<xsl:if test="$upload_failure_cnt = 0">
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_desc"/>
			</xsl:call-template>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="$upload_failure_cnt != 0">
				<!-- <xsl:call-template name="wb_ui_line"/> -->
				<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
					<tr>
						<td width="10">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
						<td>
							<xsl:if test="$upload_failure_cnt != '' and $upload_failure_cnt != '0' and failure_list/failure/@type != 'INVALIDSHEET'">
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
							<table cellpadding="3" cellspacing="0" border="0" width="100%">
								<xsl:for-each select="failure_list/failure[line]">
									<tr>
										<td width="10">
											<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
										</td>
										<td colspan="3">
											<span class="Text">
												<xsl:if test="@type != 'INVALIDSHEET'">
													<xsl:value-of select="$lab_line"/>
													<xsl:text>&#160;</xsl:text>
													<xsl:for-each select="line">
														<xsl:value-of select="text()"/>
														<xsl:if test="column/text() != ''">(<xsl:value-of select="$lab_column"/>
															<xsl:text/>
															<xsl:value-of select="column/text()"/>)</xsl:if>
														<xsl:choose>
															<xsl:when test="position() = last()"/>
															<xsl:otherwise>
																<xsl:text>,&#160;</xsl:text>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:for-each>
													<xsl:text>：</xsl:text>
													<xsl:text>&#160;</xsl:text>
												</xsl:if>
												<xsl:choose>
													<xsl:when test="@type = 'INVALID_DATE_FORMAT'">
														<xsl:value-of select="$lab_err_invalid_date_format"/>
													</xsl:when>
													<xsl:when test="@type = 'USER_NOT_EXIST'">
														<xsl:value-of select="$lab_err_user_not_exist"/>
													</xsl:when>
													<xsl:when test="@type = 'USER_NOT_TARGET'">
														<xsl:value-of select="$lab_err_user_not_target"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_COMPLETION_STATUS'">
														<xsl:value-of select="$lab_err_invalid_completion_status"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_COMPLETION_DATE'">
														<xsl:value-of select="$lab_err_invalid_completion_date"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_COLUMN'">
														<xsl:value-of select="$lab_err_invalid_column"/>
													</xsl:when>
													<xsl:when test="@type = 'ITEM_NOT_EXIST'">
														<xsl:value-of select="$lab_err_item_not_exist"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_ENROLLMENT_STATUS'">
														<xsl:value-of select="$lab_err_invalid_enrollment_status"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_NUMBERIC_FORMAT'">
														<xsl:value-of select="$lab_err_invalid_numberic_format"/>
													</xsl:when>
													<xsl:when test="@type = 'MISSING_REQUIRED_FIELD'">
														<xsl:value-of select="$lab_err_missing_required_field"/>
													</xsl:when>
													<xsl:when test="@type = 'DUPLICATED_ENROLLMENT'">
														<xsl:value-of select="$lab_err_duplicated_enrollment"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_CREDIT_TYPE'">
														<xsl:value-of select="$lab_err_invalid_credit_type"/>
													</xsl:when>
													<xsl:when test="@type = 'ENROLLMENT_N_COMPLETION_STATUS_NOT_MATCH'">
														<xsl:value-of select="$lab_err_enrollment_n_completion_status_not_match"/>
													</xsl:when>
													<xsl:when test="@type = 'ITEM_NOT_ENROLL_LEVEL'">
														<xsl:value-of select="$lab_err_item_not_enroll_level"/>
													</xsl:when>
													<xsl:when test="@type = 'ITEM_NOT_INCHARGE'">
														<xsl:value-of select="$lab_err_item_not_incharge"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_DATE_ORDER'">
														<xsl:value-of select="$lab_err_date_order"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_USER_STATUS'">
														<xsl:value-of select="$lab_err_user_not_exist"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALID_USER_ROLE'">
														<xsl:value-of select="$lab_err_invalid_usr_role"/>
													</xsl:when>
													<xsl:when test="@type = 'INVALIDSHEET'">
														<xsl:value-of select="$lab_err_invalid_sheet"/>
													</xsl:when>
													<xsl:when test="@type = 'MAXCOMMENT'">
														<xsl:value-of select="$lab_errors_max_comment"/>
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
										<span class="Text">
											<xsl:value-of select="$lab_error_msg"/>
										</span>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<xsl:call-template name="wb_ui_line"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="count(enterprise/membership/member) &gt;=1">
						<!-- <xsl:call-template name="wb_ui_line"/> -->
						<xsl:call-template name="wb_ui_desc">
							<xsl:with-param name="text" select="$lab_total_num"/>
						</xsl:call-template>
						<table border="0" cellspacing="0" cellpadding="3" class="table wzb-ui-table margin-top28" width="{$wb_gen_table_width}">
							<tr class="wzb-ui-table-head">
								<td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
								<xsl:apply-templates select="used_column/column[text() != 'Role Type']" mode="draw_header">
									<xsl:with-param name="lab_col_course_title" select="$lab_col_course_title"/>
									<xsl:with-param name="lab_col_class_title" select="$lab_col_class_title"/>
								</xsl:apply-templates>
								<td width="4">
									<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
								</td>
							</tr>
							<xsl:apply-templates select="enterprise"/>
						</table>
						<xsl:call-template name="wb_ui_line"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_upload_record"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		&#160;
		<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td align="center">
					<xsl:choose>
						<xsl:when test="$upload_failure_cnt != 0">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.prep('<xsl:value-of select="/Upload/item/@id"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:if test="$upload_success_cnt &gt;= 1">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_confirm"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.confirm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
							</xsl:if>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.prep('<xsl:value-of select="/Upload/item/@id"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="enterprise">
		<xsl:apply-templates select="membership/member"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="used_column"/>
	<!-- =============================================================== -->
	<xsl:template match="member">
		<xsl:variable name="member" select="."/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr valign="middle" class="{$row_class}">
			<td width="4">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
			<xsl:apply-templates select="../../../used_column/column" mode="draw_item">
				<xsl:with-param name="member" select="$member"/>
				<xsl:with-param name="my_id" select="../sourcedid/id"/>
			</xsl:apply-templates>
			<td width="4">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_header">
		<xsl:param name="lab_col_course_title"/>
		<xsl:param name="lab_col_class_title"/>
		<td nowrap="nowrap">
			<span class="wbRowheadBarSText">
				<xsl:value-of select="text()"/>
			</span>
		</td>
		<xsl:if test="text() = 'Learning Solution Code'">
			<td nowrap="nowrap">
				<span class="wbRowheadBarSText">
					<xsl:value-of select="$lab_col_course_title"/>
				</span>
			</td>
			<td nowrap="nowrap">
				<span class="wbRowheadBarSText">
					<xsl:value-of select="$lab_col_class_title"/>
				</span>
			</td>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_item">
		<xsl:param name="staff_id"/>
		<xsl:param name="member"/>
		<xsl:param name="my_id"/>
		<xsl:choose>
			<xsl:when test="@id = '0'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/sourcedid/id"/>
					</span>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Learning Solution Code'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/../sourcedid/id"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td nowrap="nowrap">
					<span class="Text">
						<xsl:value-of select="$item_code_ref_list/item_code_ref[code = $my_id]/parent_title"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td nowrap="nowrap">
					<span class="Text">
						<xsl:value-of select="$item_code_ref_list/item_code_ref[code = $my_id]/title"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '1'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Enrollment Status']/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="text() = 'Enrollment Date'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:variable name="str_length" select="string-length($member/role/extension/ns1:extension/ns1:enrollmentdate)"/>
						<xsl:value-of select="substring(translate($member/role/extension/ns1:extension/ns1:enrollmentdate,'T',' '),0,$str_length - 2)"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '2'">
				<td nowrap="nowrap" valign="top" align="left">
			
					<span class="Text">
					
						<xsl:if test="$member/role/finalresult[mode = 'Completion Status']/result != ''">
						<xsl:variable name="status"><xsl:value-of select="$member/role/finalresult[mode = 'Completion Status']/result"/></xsl:variable>
<xsl:variable name="status_id">
						<xsl:choose>
							<xsl:when test="$status = 'I'">2</xsl:when>
							<xsl:when test="$status = 'C'">1</xsl:when>
							<xsl:when test="$status = 'F'">3</xsl:when>
							<xsl:when test="$status = 'W'">4</xsl:when>
							

						</xsl:choose>
						</xsl:variable>
						<xsl:call-template name="get_ats_title">
							<xsl:with-param name="ats_id" select="$status_id"/>
						</xsl:call-template>
						
						</xsl:if>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
	
					<!--
					<span class="Text">
						<xsl:if test="$member/role/finalresult[mode = 'Completion Status']/result != ''">
							<xsl:call-template name="display_progress_tracking">
								<xsl:with-param name="status">
									<xsl:value-of select="$member/role/finalresult[mode = 'Completion Status']/result"/>
								</xsl:with-param>
								<xsl:with-param name="show_text">true</xsl:with-param>
								<xsl:with-param name="type">course</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					</span>
					-->
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '3'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:variable name="str_length" select="string-length($member/role/extension/ns1:extension/ns1:completiondate)"/>
						<xsl:value-of select="substring(translate($member/role/extension/ns1:extension/ns1:completiondate,'T',' '),0,$str_length - 2)"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id  = '4'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Completion Status']/comments"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '5'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Assessment Result']/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '6'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = 'Attendance Rate']/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="substring-before(text(),' ') = 'Metric'">
				<xsl:variable name="my_id" select="substring-after(text(),' ')"/>
				<xsl:variable name="credit_type_list_id" select="$credit_type_list/credit_type[@seq_id = $my_id]/@type"/>
				<xsl:variable name="credit_type_name" select="concat('Accreditation-',$credit_type_list_id)"/>
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/finalresult[mode = $credit_type_name]/result"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<!--enrollment workflow-->
			<xsl:when test="@id = '7'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/extension/ns1:extension/ns1:enrollmentworkflow"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<!--send mail-->
			<xsl:when test="@id = '8'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$member/role/extension/ns1:extension/ns1:sendmail"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td nowrap="nowrap">
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	
	<xsl:template match="status">
		<xsl:value-of select="desc[@lan = $wb_lang_encoding]/@name"/>
	</xsl:template>
</xsl:stylesheet>

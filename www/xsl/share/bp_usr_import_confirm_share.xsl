<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="upload_success_cnt" select="count(Upload/upload_user/enterprise/person)"/>
	<xsl:variable name="upload_failure_cnt" select="count(Upload/upload_user/failed_lines/line)"/>
	<xsl:variable name="total_upload_cnt" select="$upload_success_cnt + $upload_failure_cnt"/>
	<!-- -->
	<xsl:variable name="missing_field_failure_cnt" select="count(Upload/upload_user/missing_field_lines/line)"/>
	<xsl:variable name="invalid_gradecode_cnt" select="count(Upload/upload_user/invalid_gradecode_lines/line)"/>
	<xsl:variable name="invalid_role_cnt" select="count(Upload/upload_user/invalid_role_lines/line)"/>
	<xsl:variable name="invalid_groupcode_cnt" select="count(Upload/upload_user/invalid_groupcode_lines/line)"/>
	<xsl:variable name="empty_password_cnt" select="count(Upload/upload_user/empty_password_lines/line)"/>
	<xsl:variable name="invalid_supervise_groupcode_cnt" select="count(Upload/upload_user/invalid_supervise_groupcode_lines/line)"/>
	<xsl:variable name="invalid_direct_supervisor_id_cnt" select="count(Upload/upload_user/invalid_direct_supervisor_id_lines/line)"/>
	<xsl:variable name="invalid_group_supervisor_group_cnt" select="count(Upload/upload_user/invalid_group_supervisor_group_lines/line)"/>
	<xsl:variable name="dup_id_failure_cnt" select="count(Upload/upload_user/duplicated_id_lines/line)"/>
	<xsl:variable name="invalid_field_cnt" select="count(Upload/upload_user/invalid_field_lines/line)"/>
	<xsl:variable name="other_failure_cnt" select="count(Upload/upload_user/others_failed_lines/line)"/>
	<xsl:variable name="invalid_org_grp_name_cnt" select="count(Upload/upload_user/invalid_org_grp_name_lines/line)"/>
	<xsl:variable name="no_group_specified_cnt" select="count(Upload/upload_user/no_group_specified/line)"/>
	<xsl:variable name="group_code_ref_list" select="Upload/upload_user/group_code_ref_list"/>
	<xsl:variable name="grade_code_ref_list" select="Upload/upload_user/grade_code_ref_list"/>
	<xsl:variable name="held_by_pending_appn_cnt" select="count(Upload/upload_user/held_by_pending_appn/line)"/>
	<xsl:variable name="held_by_pending_approval_appn_cnt" select="count(Upload/upload_user/held_by_pending_approval_appn/line)"/>
	<xsl:variable name="total" select="Upload/upload_user/user_count"/>
	<xsl:variable name="cur_total">20</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="Upload/upload_user"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="upload_user">
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
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
			<form name="frmXml" onsubmit="return status()">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" />
				<input type="hidden" name="url_failure" />
				<input type="hidden" name="stylesheet" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>

	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_usr_manager">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_usr_reg_approval">用戶註冊審批</xsl:with-param>
			<xsl:with-param name="lab_usr_prof_maintain">用戶管理</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">批量用戶上傳</xsl:with-param>
			<xsl:with-param name="lab_upload_usr_info">上傳用戶資料</xsl:with-param>
			<xsl:with-param name="lab_preview">預覽</xsl:with-param>
			<xsl:with-param name="lab_upload_usr">上載用戶</xsl:with-param>
			<xsl:with-param name="lab_upload_failed">上載失敗行數</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_missing_field">必須指定用戶資訊欄</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_duplicated_id_lines">用戶名已存在</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_field">用戶資訊欄格式錯誤</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_others_failed_lines">其他</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_groupcode">無效的組代碼</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_empty_password">對於新用戶，必須提供用戶名和密碼</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_supervise_groupcode">無效的最高報名審批用戶組代碼</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_direct_supervise_id">無效的直屬上司ID</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_role_line">無效的角色</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_org_grp_name_lines">無效的機構或組名稱</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_no_group_specified">未指定組</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_group_supervisor_group_lines">無效的需要審批報名請求的用戶組上司所在的組代碼</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_held_by_pending_appn">由於用戶還有等待審批的報名請求，未能改變用戶所在的用戶組/需要審批報名請求的用戶組上司所在的組代碼/直屬上司</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_held_by_pending_approval_appn">由於用戶還有等待他審批的報名請求，未能改變用戶的下屬部門</xsl:with-param>
			<xsl:with-param name="lab_male">男性</xsl:with-param>
			<xsl:with-param name="lab_female">女性</xsl:with-param>
			<xsl:with-param name="lab_no_upload_usr">上載檔沒有用戶記錄</xsl:with-param>
			<xsl:with-param name="lab_first_name">名</xsl:with-param>
			<xsl:with-param name="lab_last_name">姓</xsl:with-param>
			<xsl:with-param name="lab_form">級別</xsl:with-param>
			<xsl:with-param name="lab_class">班別</xsl:with-param>
			<xsl:with-param name="lab_class_num">班號</xsl:with-param>
			<xsl:with-param name="lab_email">電子郵件</xsl:with-param>
			<xsl:with-param name="lab_student_id">學號</xsl:with-param>
			<xsl:with-param name="lab_club_1">第1組</xsl:with-param>
			<xsl:with-param name="lab_club_2">第2組</xsl:with-param>
			<xsl:with-param name="lab_club_3">第3組</xsl:with-param>
			<xsl:with-param name="lab_club_4">第4組</xsl:with-param>
			<xsl:with-param name="lab_club_5">第5組</xsl:with-param>
			<xsl:with-param name="lab_total_upload">總數上載</xsl:with-param>
			<xsl:with-param name="lab_upload_success">上載成功</xsl:with-param>
			<xsl:with-param name="lab_upload_failure">上載失敗</xsl:with-param>
			<xsl:with-param name="lab_line">行數</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">顯示名稱</xsl:with-param>
			<xsl:with-param name="lab_password">密碼</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">確認</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_show_all">顯示所有</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm_imm">確認並立即導入</xsl:with-param>
			<xsl:with-param name="lab_usr_profiles">用戶資料</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">條用戶資料出現錯誤：</xsl:with-param>
			<xsl:with-param name="lab_unknown_col">警告：用戶資訊欄無法通過驗證。</xsl:with-param>
			<xsl:with-param name="lab_detected_from_file">條用戶資料被查出：</xsl:with-param>
			<xsl:with-param name="lab_title">匯入用戶資訊 - 第二步：確認</xsl:with-param>
			<xsl:with-param name="lab_error_title">匯入用戶資訊 - 錯誤</xsl:with-param>
			<xsl:with-param name="lab_error_msg">請糾正錯誤並重新匯入檔。</xsl:with-param>
			<xsl:with-param name="lab_desc">下列是檔中的用戶資訊（首二十個）。按螢幕底部的<b>確定</b> 按鈕完成匯入，或按<b>取消</b> 按鈕停止操作。點擊<a href="javascript:Batch.User.Import.preview();">這裡</a>可以查看文件中的所有用戶資訊。</xsl:with-param>
			<xsl:with-param name="lab_desc2_1">上載文件內的記錄數目多於</xsl:with-param>
			<xsl:with-param name="lab_desc2_2">。系統會在背景處理上載的請求。 當完成上載的請求後﹐系統會以電郵通知你。</xsl:with-param>
			<xsl:with-param name="lab_group_code_ref">組代碼參考</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_gradecode_cnt">無效職務代碼</xsl:with-param>
			<xsl:with-param name="lab_number_of_users">檔中的用戶資訊數目</xsl:with-param>
			<xsl:with-param name="lab_showing_usr">本頁顯示</xsl:with-param>
			<xsl:with-param name="lab_page_of_usr">個，共找到</xsl:with-param>
			<xsl:with-param name="lab_page_piece_usr">個</xsl:with-param>
			<xsl:with-param name="lab_get_source">查看源文件</xsl:with-param>
			<xsl:with-param name="lab_get_count">記錄總數：</xsl:with-param>
			<xsl:with-param name="lab_get_pass">通過記錄數：</xsl:with-param>
			<xsl:with-param name="lab_get_unpass">錯誤記錄數：</xsl:with-param>
			<xsl:with-param name="lab_get_check_msg">檢查信息</xsl:with-param>
			<xsl:with-param name="lab_have_no_msg">沒有錯誤信息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_get_count">记录总数：</xsl:with-param>
			<xsl:with-param name="lab_get_pass">通过记录数：</xsl:with-param>
			<xsl:with-param name="lab_get_unpass">错误记录数：</xsl:with-param>
			<xsl:with-param name="lab_get_check_msg">检查信息</xsl:with-param>
			<xsl:with-param name="lab_have_no_msg">没有错误信息</xsl:with-param>
			<xsl:with-param name="lab_get_source">查看源文件</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">用户管理</xsl:with-param>
			<xsl:with-param name="lab_usr_reg_approval">用户注册审批</xsl:with-param>
			<xsl:with-param name="lab_usr_prof_maintain">用户管理</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">批量用户上传</xsl:with-param>
			<xsl:with-param name="lab_upload_usr_info">上传用户资料</xsl:with-param>
			<xsl:with-param name="lab_preview">预览</xsl:with-param>
			<xsl:with-param name="lab_upload_usr">上载用户</xsl:with-param>
			<xsl:with-param name="lab_upload_failed">上载失败行数</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_missing_field">必须指定用户信息栏</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_duplicated_id_lines">用户名已存在</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_field">用户信息栏格式错误</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_others_failed_lines">其他</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_groupcode">无效的组代码</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_empty_password">对于新用户，必须提供用户名和密码</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_supervise_groupcode">无效的最高报名审批用户组代码</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_direct_supervise_id">无效的直属上司ID</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_role_line">无效的角色</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_org_grp_name_lines">无效的机构或组名称</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_no_group_specified">未指定组</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_group_supervisor_group_lines">无效的需要审批报名请求的用户组领导所在的组代码</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_held_by_pending_appn">由於用户还有等待审批的报名请求， 未能改变用户所在的用户组/需要审批报名请求的用户组领导所在的组代码/直接领导</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_held_by_pending_approval_appn">由於用户还有等待他审批的报名请求，未能改变用户的下属部门</xsl:with-param>
			<xsl:with-param name="lab_male">男性</xsl:with-param>
			<xsl:with-param name="lab_female">女性</xsl:with-param>
			<xsl:with-param name="lab_no_upload_usr">上载文件没有用户记录</xsl:with-param>
			<xsl:with-param name="lab_first_name">名</xsl:with-param>
			<xsl:with-param name="lab_last_name">姓</xsl:with-param>
			<xsl:with-param name="lab_form">级别</xsl:with-param>
			<xsl:with-param name="lab_class">班别</xsl:with-param>
			<xsl:with-param name="lab_class_num">班号</xsl:with-param>
			<xsl:with-param name="lab_email">电子邮件</xsl:with-param>
			<xsl:with-param name="lab_student_id">学号</xsl:with-param>
			<xsl:with-param name="lab_club_1">第1组</xsl:with-param>
			<xsl:with-param name="lab_club_2">第2组</xsl:with-param>
			<xsl:with-param name="lab_club_3">第3组</xsl:with-param>
			<xsl:with-param name="lab_club_4">第4组</xsl:with-param>
			<xsl:with-param name="lab_club_5">第5组</xsl:with-param>
			<xsl:with-param name="lab_total_upload">总数上载</xsl:with-param>
			<xsl:with-param name="lab_upload_success">上载成功</xsl:with-param>
			<xsl:with-param name="lab_upload_failure">上载失败</xsl:with-param>
			<xsl:with-param name="lab_line">行数</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">显示名称</xsl:with-param>
			<xsl:with-param name="lab_password">密码</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">返回</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">确认</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_show_all">显示所有</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm_imm">确认并立即导入</xsl:with-param>
			<xsl:with-param name="lab_usr_profiles">用户资料</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">条用户资料出现错误：</xsl:with-param>
			<xsl:with-param name="lab_unknown_col">警告：用户信息栏无法通过验证。</xsl:with-param>
			<xsl:with-param name="lab_detected_from_file">条用户资料被查出：</xsl:with-param>
			<xsl:with-param name="lab_title">导入用户信息 - 第二步：确认</xsl:with-param>
			<xsl:with-param name="lab_error_title">导入用户信息 - 错误</xsl:with-param>
			<xsl:with-param name="lab_error_msg">请纠正错误并重新导入文件。</xsl:with-param>
			<xsl:with-param name="lab_desc">以下所列是文件中的前20条用户信息。点击屏幕底部的<b>确定</b>按钮完成导入，或单击<b>取消</b>按钮停止操作。点击<a href="javascript:Batch.User.Import.preview();">这里</a>可以查看文件中的所有用户信息。</xsl:with-param>
			<xsl:with-param name="lab_desc2_1">如上载文件内的记录数目多于</xsl:with-param>
			<xsl:with-param name="lab_desc2_2">。 系统会在后台处理上载的请求。 当完成上载的请求后，系统会以电邮通知你。</xsl:with-param>
			<xsl:with-param name="lab_group_code_ref">用户组标题</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_gradecode_cnt">无效职务代码</xsl:with-param>
			<xsl:with-param name="lab_number_of_users">文件中的用户信息的数目</xsl:with-param>
			<xsl:with-param name="lab_showing_usr">本页显示第</xsl:with-param>
			<xsl:with-param name="lab_page_of_usr">个，共找到</xsl:with-param>
			<xsl:with-param name="lab_page_piece_usr">个</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_get_count">Total number of records：</xsl:with-param>
			<xsl:with-param name="lab_get_pass">Pass record(s) ：</xsl:with-param>
			<xsl:with-param name="lab_get_unpass">Error record(s)：</xsl:with-param>
			<xsl:with-param name="lab_get_check_msg">Inspection information</xsl:with-param>
			<xsl:with-param name="lab_have_no_msg">No error message</xsl:with-param>
			<xsl:with-param name="lab_get_source">Get source file</xsl:with-param>
			<xsl:with-param name="lab_usr_manager">User management</xsl:with-param>
			<xsl:with-param name="lab_usr_reg_approval">User registration approval</xsl:with-param>
			<xsl:with-param name="lab_usr_prof_maintain">User profile maintenance</xsl:with-param>
			<xsl:with-param name="lab_usr_bat_upload">Batch user upload</xsl:with-param>
			<xsl:with-param name="lab_upload_usr_info">Batch user upload</xsl:with-param>
			<xsl:with-param name="lab_preview">Preview </xsl:with-param>
			<xsl:with-param name="lab_upload_usr">Upload User</xsl:with-param>
			<xsl:with-param name="lab_upload_failed">Failed to upload user</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_missing_field">Field must be supplied</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_duplicated_id_lines">User ID already exists</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_field">Format of the field is invalid</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_others_failed_lines">Unexpected error</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_groupcode">Invalid group code</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_empty_password">For new user, password must be supplied</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_supervise_groupcode">Invalid supervised group code</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_direct_supervise_id">Invalid supervisor IDs</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_role_line">Invalid role</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_org_grp_name_lines">Invalid organization or group name</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_group_supervisor_group_lines">Invalid group supervisors under this group for approval</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_no_group_specified">No group specified</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_held_by_pending_appn">User cannot change his/her group, group supervisors for approval, direct superviser if he/she has pending applications</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_held_by_pending_approval_appn">User cannot change his/her supervised group if he/she has pending approval records</xsl:with-param>
			<xsl:with-param name="lab_male">Male</xsl:with-param>
			<xsl:with-param name="lab_female">Female</xsl:with-param>
			<xsl:with-param name="lab_no_upload_usr">There are no records found in the input file.</xsl:with-param>
			<xsl:with-param name="lab_first_name">First name</xsl:with-param>
			<xsl:with-param name="lab_last_name">Last name</xsl:with-param>
			<xsl:with-param name="lab_form">Form</xsl:with-param>
			<xsl:with-param name="lab_class">Class</xsl:with-param>
			<xsl:with-param name="lab_class_num">Class number</xsl:with-param>
			<xsl:with-param name="lab_email">Email</xsl:with-param>
			<xsl:with-param name="lab_student_id">Student ID</xsl:with-param>
			<xsl:with-param name="lab_club_1">Club 1</xsl:with-param>
			<xsl:with-param name="lab_club_2">Club 2</xsl:with-param>
			<xsl:with-param name="lab_club_3">Club 3</xsl:with-param>
			<xsl:with-param name="lab_club_4">Club 4</xsl:with-param>
			<xsl:with-param name="lab_club_5">Club 5</xsl:with-param>
			<xsl:with-param name="lab_total_upload">Total uploaded</xsl:with-param>
			<xsl:with-param name="lab_upload_success">Successful uploaded</xsl:with-param>
			<xsl:with-param name="lab_upload_failure">Failed to upload</xsl:with-param>
			<xsl:with-param name="lab_line">Line</xsl:with-param>
			<xsl:with-param name="lab_usr_display_bil">Display name</xsl:with-param>
			<xsl:with-param name="lab_password">Password</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_back">Back</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_show_all">Show all</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_confirm_imm">Confirm and import immediately</xsl:with-param>
			<xsl:with-param name="lab_usr_profiles">User profiles</xsl:with-param>
			<xsl:with-param name="lab_detected_from_file">user profile(s) are detected from the file:</xsl:with-param>
			<xsl:with-param name="lab_errors_are_found">error(s) are found in the file:</xsl:with-param>
			<xsl:with-param name="lab_unknown_col">Warning: column(s) could not be recognized : </xsl:with-param>
			<xsl:with-param name="lab_title">Import User Profile – Step 2: Confirmation</xsl:with-param>
			<xsl:with-param name="lab_error_title">Import User Profile - Error</xsl:with-param>
			<xsl:with-param name="lab_error_msg">Please correct the errors and import the file again.</xsl:with-param>
			<xsl:with-param name="lab_desc">Listed below are the first 20 user profiles detected from the file. Click <b>Confirm</b> at the bottom to complete the import, or <b>Cancel</b> to stop the process. Click <a href="javascript:Batch.User.Import.preview();">here</a> to view all detected user profiles.</xsl:with-param>
			<xsl:with-param name="lab_desc2_1">The number of user profilles detected has exceeded</xsl:with-param>
			<xsl:with-param name="lab_desc2_2">. As a result, the import process will be conducted in the background after confirmation. A notifcation email will be sent to you upon completion.</xsl:with-param>
			<xsl:with-param name="lab_group_code_ref">Group code reference</xsl:with-param>
			<xsl:with-param name="lab_upload_failed_invalid_gradecode_cnt">Invalid grade code</xsl:with-param>
			<xsl:with-param name="lab_number_of_users">Total number of detected user profiles</xsl:with-param>
			<xsl:with-param name="lab_showing_usr">Showing</xsl:with-param>
			<xsl:with-param name="lab_page_of_usr">of</xsl:with-param>
			<xsl:with-param name="lab_page_piece_usr"></xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="enterprise">
		<xsl:param name="lab_male"/>
		<xsl:param name="lab_female"/>
		<xsl:apply-templates select="person">
			<xsl:with-param name="lab_male" select="$lab_male"/>
			<xsl:with-param name="lab_female" select="$lab_female"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="used_column"/>
	<!-- =============================================================== -->
	<xsl:template match="person">
		<xsl:param name="lab_male"/>
		<xsl:param name="lab_female"/>
		<!-- -->
		<xsl:variable name="staff_id" select="sourcedid/id"/>
		<xsl:variable name="form_id" select="../membership[member/sourcedid/id != $staff_id]/sourcedid/id"/>
		<xsl:variable name="class_id" select="../membership[member/sourcedid/id != $staff_id]/member/sourcedid/id"/>
		<xsl:variable name="membership_cnt" select="count(../membership)"/>
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
			<xsl:apply-templates select="../../used_column/column[text() != 'Role Type']" mode="draw_item">
				<xsl:with-param name="person" select="."/>
				<xsl:with-param name="staff_id"><xsl:value-of select="$staff_id"/></xsl:with-param>
				<xsl:with-param name="lab_male" select="$lab_male"/>
				<xsl:with-param name="lab_female" select="$lab_female"/>
			</xsl:apply-templates>
			<td width="4">
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_header">
		<xsl:param name="lab_group_code_ref"/>
		<xsl:choose>
			<xsl:when test="@id= '10'">
				<td nowrap="nowrap">
					<span class="TitleText">
						<xsl:value-of select="text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td nowrap="nowrap">
					<span class="TitleText">
						<xsl:value-of select="$lab_group_code_ref"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id > 16) and (@active='false')">
			</xsl:when>
			<xsl:otherwise>
				<td nowrap="nowrap">
					<xsl:choose>
						<xsl:when test="@id = '5'">
							<xsl:attribute name="align">center</xsl:attribute>
						</xsl:when>
					</xsl:choose>
					<span class="TitleText">
						<xsl:value-of select="text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="column" mode="draw_item">
		<xsl:param name="lab_male"/>
		<xsl:param name="person"/>
		<xsl:param name="staff_id"/>
		<xsl:param name="lab_female"/>
		<xsl:param name="my_id"/>
		<xsl:choose>
			<xsl:when test="text() = 'Line'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$person/extension/line_num/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '0'">
				<td nowrap="nowrap" valign="top" align="left">
					<span class="Text">
						<xsl:value-of select="$staff_id"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '1'">
				<td valign="top">
					<span class="Text">
						<xsl:value-of select="$person/userid/@password"/>
						<xsl:text>&#160;</xsl:text>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '2'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/name/fn"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '57'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/name/nickname"/>
					</span>
					<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '3'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:choose>
							<xsl:when test="$person/demographics/gender = 1">
								<xsl:value-of select="$lab_female"/>
							</xsl:when>
							<xsl:when test="$person/demographics/gender = 2">
								<xsl:value-of select="$lab_male"/>
							</xsl:when>
						</xsl:choose>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '4'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_bday">
							<xsl:value-of select="$person/demographics/bday/text()"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_bday,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '5'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/email/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '6'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/tel[@teltype = '1']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '7'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/tel[@teltype = '2']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '8'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/job_title/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '9'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/grade/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>			
			<xsl:when test="@id = '10'">
				<xsl:variable name="my_id" select="../../enterprise/membership[sourcedid/id/text() != 'ROOT' and not(starts-with(sourcedid/id/text(), 'dummy')) and member[sourcedid/id/text() = $staff_id and not(role/subrole)]]/sourcedid/id/text()"/>
				<td nowrap="nowrap" valign="top">
					<span class="Text">
								<xsl:value-of select="$my_id"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
				<td nowrap="nowrap" valign="top">
					<!-- = $my_id-->
					<span class="wbRowText">
							<xsl:value-of select="$group_code_ref_list/group_code_ref[code = $my_id]/title"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '11'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:for-each select="../../enterprise/membership[starts-with(sourcedid/id/text(),'dummy_direct_supervise_group') and member[sourcedid/id/text() = $staff_id and role/@roletype = '04']]">
							<xsl:for-each select="member[sourcedid/id/text() != $staff_id and role/@roletype != '04']">
								<xsl:value-of select="sourcedid/id/text()"/>
								<xsl:if test="position() != last()">
									<xsl:text>,&#160;</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '12'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/join_date/text()"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '13'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:for-each select="../../enterprise/membership[sourcedid/id/text() = 'ROOT']/member[sourcedid/id/text() = $staff_id]/role">
							<xsl:value-of select="subrole"/>
							<xsl:if test="position() != last()">
								<xsl:text>,&#160;</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '14'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:for-each select="../../enterprise/membership[starts-with(sourcedid/id/text(),'dummy_supervise_group') and member[sourcedid/id/text() = $staff_id and role/@roletype = '05']]">
							<xsl:for-each select="member[sourcedid/id/text() != $staff_id and role/@roletype != '05']">
								<xsl:value-of select="sourcedid/id/text()"/>
								<xsl:if test="position() != last()">
									<xsl:text>,&#160;</xsl:text>
								</xsl:if>
							</xsl:for-each>
						</xsl:for-each>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '15'">
				<xsl:variable name="my_id" select="../../enterprise/membership[member[sourcedid/id/text() = $staff_id and role/subrole = 'APPN_APPROVAL_MEMBER']]/sourcedid/id/text()"/>
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:if test="$my_id != 'DUMMY_RESET_APPN_APPROVAL_GROUP'">
							<xsl:value-of select="$my_id"/>
						</xsl:if>								
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '16'">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/sourcedid/source/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '17') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '1']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '18') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '2']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '19') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '3']/text()"/>asdasdsadasds
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '20' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '4']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '21' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '5']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '22' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '6']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '23' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '7']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '24' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '8']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '25' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '9']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '26' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '10']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '27' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '11']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '28' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '12']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '29' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '13']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '30' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '14']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '31' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '15']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '32' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '16']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '33' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '17']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '34' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '18']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '35' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '19']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="@id = '36' and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:variable name="tmp_day">
							<xsl:value-of select="$person/extension/extension/ext[@exttype = '20']"/>
						</xsl:variable>
						<xsl:value-of select="substring-before($tmp_day,'T')"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '37') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '21']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '38') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '22']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '39') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '23']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '40') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '24']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '41') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '25']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '42') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '26']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '43') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '27']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '44') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '28']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '45') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '29']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '46') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '30']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '47') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '31']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '48') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '32']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '49') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '33']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '50') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '34']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '51') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '35']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '52') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '36']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '53') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '37']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '54') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '38']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '55') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '39']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:when test="(@id = '56') and (@active = 'true')">
				<td nowrap="nowrap" valign="top">
					<span class="Text">
						<xsl:value-of select="$person/extension/extension/ext[@exttype = '40']/text()"/>
					</span><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
				</td>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	
</xsl:stylesheet>

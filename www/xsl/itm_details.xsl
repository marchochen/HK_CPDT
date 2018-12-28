<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<!-- other -->
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="itm_upd.xsl"/>

	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="mod_id" select="/applyeasy/item/forum/@id"/>
	<xsl:variable name="cos_res_id" select="/applyeasy/item/@cos_res_id"/>
	<xsl:variable name="itm_type" select="/applyeasy/item/@type"/>
	<xsl:variable name="itm_exam_ind" select="/applyeasy/item/item_type_meta/@exam_ind"/>
	<xsl:variable name="itm_title" select="/applyeasy/item/title"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="itm_status" select="/applyeasy/item/@status"/>
	<xsl:variable name="turn_itm_status">
		<xsl:choose>
			<xsl:when test="$itm_status = 'OFF'">ON</xsl:when>
			<xsl:otherwise>OFF</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="create_run_ind" select="/applyeasy/item/@create_run_ind"/>
	<xsl:variable name="cur_month" select="/applyeasy/item/child_items/cur_time/@month"/>
	<xsl:variable name="cur_year" select="/applyeasy/item/child_items/cur_time/@year"/>
	<xsl:variable name="itm_type_list_root" select="/applyeasy/item/child_items/item_type_list"/>
	<xsl:variable name="itm_apply_ind" select="/applyeasy/item/@apply_ind"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="usr_id" select="/applyeasy/meta/cur_usr/@ent_id"/>
	<xsl:variable name="current_role" select="/applyeasy/current_role"/>
	<xsl:variable name="escaped_title">
		<xsl:call-template name="escape_js">
			<xsl:with-param name="input_str" select="/applyeasy/item/title"/>
		</xsl:call-template>
	</xsl:variable>
	<xsl:variable name="lab_run">
		<xsl:choose>
			<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="tp_plan">
		<xsl:choose>
			<xsl:when test="/applyeasy/item/@plan_code !=''">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<xsl:variable name="hasItmCosMain">
		<xsl:choose>
			<xsl:when test="$page_variant/@hasItmEditBtn = 'true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	
	<xsl:variable name="type" select="/applyeasy/item/nav/item/@exam_ind"></xsl:variable>
	
	<!-- 基本信息 -->
	<xsl:variable name="label_core_training_management_229" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_229')"/>
	<!-- 高级信息 -->
	<xsl:variable name="label_core_training_management_230" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_230')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:apply-templates select="applyeasy/item"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy/item">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
					
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_itm_req.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_message.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_report.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_criteria.js"/>
				<script language="javascript" type="text/javascript" src="{$wb_js_path}wb_mote.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}sso_link.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
				crit = new wbCriteria
				msg = new wbMessage
				itm_lst = new wbItem
				ann = new wbAnnouncement
				course_lst = new wbCourse
				var module_lst = new wbModule;	
				rpt = new wbReport
				mote = new wbMote
				app = new wbApplication
				attn = new wbAttendance
				itmReq = new wbItemReq
				usr = new wbUserGroup
				sso = new wbSSO
				wiz = new wbCosWizard
				
				window.onunload = unloadHandler;
				function unloadHandler(){
					wb_utils_set_cookie('lrn_soln_itm_title','')
				}
				
				function init(){
					wb_utils_set_cookie('lrn_soln_itm_title',']]><xsl:value-of select="$escaped_title"/><![CDATA[')
				}
			]]></script>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="javascript:init()">
				<form name="frmXml">
					<!-- for sso link get -->
					<input type="hidden" name="learner_course_home" value="{//learner_course_home/location/text()}"/>
					<input type="hidden" name="root" value="{//root/text()}"/>
					<input type="hidden" name="sso_link" value=""/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="valued_template">
			<xsl:with-param name="lab_run_info">
					<xsl:choose>
						<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
					</xsl:choose>
					訊息
			</xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> 訊息</xsl:with-param>
			<xsl:with-param name="lab_home">首頁</xsl:with-param>
			<xsl:with-param name="lab_code">編號</xsl:with-param>
			<xsl:with-param name="lab_name">課程名稱</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_ppl_allow">人數</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_prpts">屬性</xsl:with-param>
			<xsl:with-param name="lab_stdy_mrks">學分</xsl:with-param>
			<xsl:with-param name="lab_start_time">開始日期</xsl:with-param>
			<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_require">必選</xsl:with-param>
			<xsl:with-param name="lab_elective">選修</xsl:with-param>
			<xsl:with-param name="lab_day">日</xsl:with-param>
			<xsl:with-param name="lab_type_other">其他</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">此類沒有課程</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_unlimit">無限</xsl:with-param>
			<xsl:with-param name="lab_prog_dtl">課程</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_details">課程詳情 - </xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_workflow">報名流程</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_book_sys">預訂設施</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_run_info">
				<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notify">通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_request_approval">請求批准</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_approval">批准</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_show">發佈</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_hide">取消發佈</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_run">取消<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_joining_inst">開課通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_process_enrol">處理<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_result">考勤</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">彻底刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_request_approval_publish">申請審核</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_approval_publish">取消審核申請</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_approval_publish">批准申請並發佈</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_decline_request_approval">拒絕申請</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_publish">發佈</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_unpublish">取消發佈</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_chg_prpts">更改屬性</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_session_info">
				<xsl:value-of select="$lab_const_session"/>信息</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_completion_cri">完成準則</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_content">网上内容</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_performance">成績</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_comments">學員課程評論</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_cost">課程費用</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_pre_exm">預修和豁免課程</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_course">取消</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_figure">學分</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_approval_status_preapprove">未審核</xsl:with-param>
			<xsl:with-param name="lab_approval_status_pending_approval">等待審核</xsl:with-param>
			<xsl:with-param name="lab_approval_status_approved">審核通過並已發佈</xsl:with-param>
			<xsl:with-param name="lab_approval_status_approved_off">取消發佈（離線）</xsl:with-param>
			<xsl:with-param name="lab_approval_status_pending_reapproval">等待再審核</xsl:with-param>
			<xsl:with-param name="lab_approval_action_req_appr">申請審核</xsl:with-param>
			<xsl:with-param name="lab_approval_action_cancel_req_appr">取消審核申請</xsl:with-param>
			<xsl:with-param name="lab_approval_action_appr_pub">批准申請並發佈</xsl:with-param>
			<xsl:with-param name="lab_approval_action_decline_appr_pub">拒絕申請</xsl:with-param>
			<xsl:with-param name="lab_ils_btn_content">日程表</xsl:with-param>
			<xsl:with-param name="lab_requisite">先修條件</xsl:with-param>
			<xsl:with-param name="lab_sso_link">複製單點登錄鏈結</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_tarlrn">目標學員</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_courses">課程</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_forum">論壇</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ann">公告</xsl:with-param>
			<xsl:with-param name="lab_grad_record">結訓記錄</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_share">共享</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_stop_share">取消共享</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_offline_pkg">導入離綫課件</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="valued_template">
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose>信息
			</xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> 信息</xsl:with-param>
			<xsl:with-param name="lab_home">首页</xsl:with-param>
			<xsl:with-param name="lab_code">编号</xsl:with-param>
			<xsl:with-param name="lab_name">课程名称</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_ppl_allow">人数</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_prpts">属性</xsl:with-param>
			<xsl:with-param name="lab_stdy_mrks">学分</xsl:with-param>
			<xsl:with-param name="lab_start_time">开始日期</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_require">必选</xsl:with-param>
			<xsl:with-param name="lab_elective">选修</xsl:with-param>
			<xsl:with-param name="lab_day">日</xsl:with-param>
			<xsl:with-param name="lab_type_other">其他</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">此类没有课程</xsl:with-param>
			<xsl:with-param name="lab_month">月</xsl:with-param>
			<xsl:with-param name="lab_unlimit">无限</xsl:with-param>
			<xsl:with-param name="lab_prog_dtl">课程</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_details">课程详情 - </xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_workflow">报名工作流</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_book_sys">预订设施</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_run_info">
				<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notify">通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_request_approval">请求批准</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_approval">批准</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_show">发布</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_hide">取消发布</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_run">取消<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_joining_inst">开课通知</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_process_enrol">处理<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_result">考勤</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">彻底删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_request_approval_publish">申请审核</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_approval_publish">取消申请</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_approval_publish">批准请求并发布</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_decline_request_approval">拒绝请求</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_publish">发布</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_unpublish">取消发布</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_chg_prpts">更改属性</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_session_info">
				<xsl:value-of select="$lab_const_session"/>信息</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_completion_cri">完成准则</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_cost">课程费用</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_content">内容</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_performance">成绩</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_comments">评论管理</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_pre_exm">免修</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_course">取消</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_figure">学分</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_approval_status_preapprove">未审核</xsl:with-param>
			<xsl:with-param name="lab_approval_status_pending_approval">等待审核</xsl:with-param>
			<xsl:with-param name="lab_approval_status_approved">审核通过并已发布</xsl:with-param>
			<xsl:with-param name="lab_approval_status_approved_off">取消发布（离线）</xsl:with-param>
			<xsl:with-param name="lab_approval_status_pending_reapproval">等待再审核</xsl:with-param>
			<xsl:with-param name="lab_approval_action_req_appr">申请审核</xsl:with-param>
			<xsl:with-param name="lab_approval_action_cancel_req_appr">取消申请</xsl:with-param>
			<xsl:with-param name="lab_approval_action_appr_pub">批准请求并发布</xsl:with-param>
			<xsl:with-param name="lab_approval_action_decline_appr_pub">拒绝请求</xsl:with-param>
			<xsl:with-param name="lab_ils_btn_content">日程表</xsl:with-param>
			<xsl:with-param name="lab_requisite">先修条件</xsl:with-param>
			<xsl:with-param name="lab_sso_link">复制单点登录链接</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_tarlrn">目标学员</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_courses">课程</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_forum">论坛</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ann">公告</xsl:with-param>
			<xsl:with-param name="lab_grad_record">结训记录</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_share">共享</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_stop_share">取消共享</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_offline_pkg">导入离线课件</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="valued_template">
			<xsl:with-param name="lab_run_info">
				<xsl:choose>
					<xsl:when test="$training_type='EXAM'"><xsl:value-of select="$lab_const_exam_run"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_const_run"/></xsl:otherwise>
				</xsl:choose> information
			</xsl:with-param>
			<xsl:with-param name="lab_session_info">
				<xsl:value-of select="$lab_const_session"/> information</xsl:with-param>
			<xsl:with-param name="lab_home">Home</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_name">Course name</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_ppl_allow">Capacity</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_prpts">Properties</xsl:with-param>
			<xsl:with-param name="lab_stdy_mrks">Marks</xsl:with-param>
			<xsl:with-param name="lab_start_time">Start date</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_require">Require</xsl:with-param>
			<xsl:with-param name="lab_elective">Elective</xsl:with-param>
			<xsl:with-param name="lab_day">day</xsl:with-param>
			<xsl:with-param name="lab_type_other">Other</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">No item in this category</xsl:with-param>
			<xsl:with-param name="lab_month">Month</xsl:with-param>
			<xsl:with-param name="lab_unlimit">Unlimit</xsl:with-param>
			<xsl:with-param name="lab_prog_dtl">Item</xsl:with-param>
			<xsl:with-param name="lab_learning_solution_details">Learning solution details - </xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit_workflow">Workflow</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_book_sys">Facility management</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_run_info">
				<xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_notify">Notify</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_request_approval">Request approval</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_approval">Approval</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_show">Publish</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_change_hide">Unpublish</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_run">Cancel <xsl:value-of select="$lab_run"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_joining_inst">Joining instruction</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_process_enrol">
				<xsl:value-of select="$lab_const_enrollment"/>
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_result">Result</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_request_approval_publish">Request approval</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_approval_publish">Cancel request</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_approval_publish">Approval and publish</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_decline_request_approval">Decline request</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_publish">Publish</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_unpublish">Unpublish</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_chg_prpts">Change properties</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_session_info">
				<xsl:value-of select="$lab_const_session"/> information</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_cost">Expenditure</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_completion_cri">Completion criteria</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_content">Content</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_performance">Performance</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_comments">Learners' comments</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_pre_exm">Prerequisite and exemption</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_cancel_course">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_item_figure">Figure</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_approval_status_preapprove">Preapprove</xsl:with-param>
			<xsl:with-param name="lab_approval_status_pending_approval">Pending approval</xsl:with-param>
			<xsl:with-param name="lab_approval_status_approved">Approved</xsl:with-param>
			<xsl:with-param name="lab_approval_status_approved_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_approval_status_pending_reapproval">Pending reapproval</xsl:with-param>
			<xsl:with-param name="lab_approval_action_req_appr">Request approval</xsl:with-param>
			<xsl:with-param name="lab_approval_action_cancel_req_appr">Cancel request</xsl:with-param>
			<xsl:with-param name="lab_approval_action_appr_pub">Approve and publish</xsl:with-param>
			<xsl:with-param name="lab_approval_action_decline_appr_pub">Decline request</xsl:with-param>
			<xsl:with-param name="lab_ils_btn_content">Timetable</xsl:with-param>
			<xsl:with-param name="lab_requisite">Prerequisite</xsl:with-param>
			<xsl:with-param name="lab_sso_link">Copy SSO link</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_tarlrn">Target learner</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_courses">Courses</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_forum">Discussion forum</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_ann">Notice</xsl:with-param>
			<xsl:with-param name="lab_grad_record">Completion result</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_share">Share</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_stop_share">Stop share</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_offline_pkg">Import courseWare package</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="valued_template">
		<xsl:param name="lab_home"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_name"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_ppl_allow"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_prpts"/>
		<xsl:param name="lab_stdy_mrks"/>
		<xsl:param name="lab_start_time"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_require"/>
		<xsl:param name="lab_elective"/>
		<xsl:param name="lab_day"/>
		<xsl:param name="lab_type_other"/>
		<xsl:param name="lab_itm_empty"/>
		<xsl:param name="lab_month"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_prog_dtl"/>
		<xsl:param name="lab_learning_solution_details"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_sso_link"/>
		<xsl:param name="lab_g_txt_btn_edit_workflow"/>
		<xsl:param name="lab_g_txt_btn_book_sys"/>
		<xsl:param name="lab_g_txt_btn_run_info"/>
		<xsl:param name="lab_g_txt_btn_notify"/>
		<xsl:param name="lab_g_txt_btn_request_approval"/>
		<xsl:param name="lab_g_txt_btn_approval"/>
		<xsl:param name="lab_g_txt_btn_change_show"/>
		<xsl:param name="lab_g_txt_btn_change_hide"/>
		<xsl:param name="lab_g_txt_btn_cancel_run"/>
		<xsl:param name="lab_g_txt_btn_joining_inst"/>
		<xsl:param name="lab_g_txt_btn_process_enrol"/>
		<xsl:param name="lab_g_txt_btn_result"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_request_approval_publish"/>
		<xsl:param name="lab_g_txt_btn_cancel_approval_publish"/>
		<xsl:param name="lab_g_txt_btn_approval_publish"/>
		<xsl:param name="lab_g_txt_btn_decline_request_approval"/>
		<xsl:param name="lab_g_txt_btn_publish"/>
		<xsl:param name="lab_g_txt_btn_unpublish"/>
		<xsl:param name="lab_g_form_btn_chg_prpts"/>
		<xsl:param name="lab_g_form_btn_add"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_item_cost"/>
		<xsl:param name="lab_g_txt_btn_session_info"/>
		<xsl:param name="lab_g_txt_btn_completion_cri"/>
		<xsl:param name="lab_g_txt_btn_content"/>
		<xsl:param name="lab_g_txt_btn_performance"/>
		<xsl:param name="lab_g_txt_btn_comments"/>
		<xsl:param name="lab_g_txt_btn_pre_exm"/>
		<xsl:param name="lab_g_txt_btn_cancel_course"/>
		<xsl:param name="lab_g_txt_btn_item_figure"/>
		<xsl:param name="lab_g_txt_btn_ok"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_approval_status_preapprove"/>
		<xsl:param name="lab_approval_status_pending_approval"/>
		<xsl:param name="lab_approval_status_approved"/>
		<xsl:param name="lab_approval_status_approved_off"/>
		<xsl:param name="lab_approval_status_pending_reapproval"/>
		<xsl:param name="lab_approval_action_req_appr"/>
		<xsl:param name="lab_approval_action_cancel_req_appr"/>
		<xsl:param name="lab_approval_action_appr_pub"/>
		<xsl:param name="lab_approval_action_decline_appr_pub"/>
		<xsl:param name="lab_requisite"/>
		<xsl:param name="lab_ils_btn_content"/>
		<xsl:param name="lab_g_txt_btn_tarlrn"/>
		<xsl:param name="lab_g_txt_btn_courses"/>
		<xsl:param name="lab_g_txt_btn_forum"/>
		<xsl:param name="lab_g_txt_btn_ann"/>
		<xsl:param name="lab_grad_record"/>
		<xsl:param name="lab_g_txt_btn_share"/>
		<xsl:param name="lab_g_txt_btn_stop_share"/>
		<xsl:param name="lab_g_txt_btn_offline_pkg"/>
		<xsl:param name="lab_run_info" />
		<xsl:param name="lab_session_info" />
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	
		<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="../nav/item" mode="nav">
					<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
					<xsl:with-param name="lab_session_info" select="$lab_session_info"/>
				</xsl:apply-templates>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
			<xsl:with-param  name="cur_node_id">01</xsl:with-param>
		</xsl:call-template>
	
		<table cellspacing="0" width="984" border="0">
			<tbody>
				<tr>
					<td style="border-bottom: 1px solid #ddd;">
						<ul class="nav nav-tabs page-tabs" role="tablist" style="border-bottom:0;">
							<li role="presentation" class="active"><a
								aria-controls="basic" role="tab" data-toggle="tab"
								href="#basic"><xsl:value-of select="$label_core_training_management_229"/></a></li>
							<li role="presentation"><a aria-controls="senior" role="tab"
								data-toggle="tab" href="#senior"><xsl:value-of select="$label_core_training_management_230"/></a></li>
						</ul>
					</td>
				<td align="right" style="border-bottom: 1px solid #ddd;">
					<xsl:if test="$page_variant/@hasItmEditBtn = 'true' and ../@run_ind = 'false'">
						<xsl:if test="$page_variant/@hasItmEditBtn = 'true'">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.upd_item_prep(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
						<!--共享/取消共享-->
						<!-- <xsl:if test="$page_variant/@hasShareBtn = 'true'  and /applyeasy/item/@type != 'AUDIOVIDEO'">
							<xsl:if test="../../item/@itm_share_ind = 'false'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_share"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.share_itm(<xsl:value-of select="$itm_id"/>,true)</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
							<xsl:if test="../../item/@itm_share_ind = 'true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_stop_share"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.share_itm(<xsl:value-of select="$itm_id"/>, false)</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</xsl:if> -->
					</xsl:if>
					<xsl:if test="$page_variant/@hasItmCancelCourseBtn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_cancel_course"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.cancel_item_prep(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$page_variant/@hasItmCancelRunBtn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_cancel_run"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.cancel_item_prep(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="$page_variant/@hasItmDelBtn = 'true'">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.del_item_exec(<xsl:value-of select="$itm_id"/>,'<xsl:value-of select="$itm_updated_timestamp"/>','<xsl:value-of select="$wb_lang"/>','','','<xsl:value-of select="/applyeasy/training_type"/>','<xsl:value-of select="$tp_plan"/>','true')</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
			</tr>
			</tbody>
		</table>
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane active" id="basic">
				<xsl:apply-templates select="section[@id = 1]">
					<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
					<xsl:with-param name="lab_g_txt_btn_book_sys" select="$lab_g_txt_btn_book_sys"/>
					<xsl:with-param name="lab_g_txt_btn_run_info" select="$lab_g_txt_btn_run_info"/>
					<xsl:with-param name="lab_g_txt_btn_notify" select="$lab_g_txt_btn_notify"/>
					<xsl:with-param name="lab_g_txt_btn_request_approval" select="$lab_g_txt_btn_request_approval"/>
					<xsl:with-param name="lab_g_txt_btn_approval" select="$lab_g_txt_btn_approval"/>
					<xsl:with-param name="lab_g_txt_btn_change_show" select="$lab_g_txt_btn_change_show"/>
					<xsl:with-param name="lab_g_txt_btn_change_hide" select="$lab_g_txt_btn_change_hide"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_run" select="$lab_g_txt_btn_cancel_run"/>
					<xsl:with-param name="lab_g_txt_btn_joining_inst" select="$lab_g_txt_btn_joining_inst"/>
					<xsl:with-param name="lab_g_txt_btn_process_enrol" select="$lab_g_txt_btn_process_enrol"/>
					<xsl:with-param name="lab_g_txt_btn_result" select="$lab_g_txt_btn_result"/>
					<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
					<xsl:with-param name="lab_g_txt_btn_session_info" select="$lab_g_txt_btn_session_info"/>
					<xsl:with-param name="lab_g_txt_btn_completion_cri" select="$lab_g_txt_btn_completion_cri"/>
					<xsl:with-param name="lab_g_txt_btn_content" select="$lab_g_txt_btn_content"/>
					<xsl:with-param name="lab_g_txt_btn_pre_exm" select="$lab_g_txt_btn_pre_exm"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_course" select="$lab_g_txt_btn_cancel_course"/>
					<xsl:with-param name="lab_g_txt_btn_item_figure" select="$lab_g_txt_btn_item_figure"/>
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
					<xsl:with-param name="lab_approval_status_preapprove" select="$lab_approval_status_preapprove"/>
					<xsl:with-param name="lab_approval_status_pending_approval" select="$lab_approval_status_pending_approval"/>
					<xsl:with-param name="lab_approval_status_approved" select="$lab_approval_status_approved"/>
					<xsl:with-param name="lab_approval_status_approved_off" select="$lab_approval_status_approved_off"/>
					<xsl:with-param name="lab_approval_status_pending_reapproval" select="$lab_approval_status_pending_reapproval"/>
					<xsl:with-param name="lab_approval_action_req_appr" select="$lab_approval_action_req_appr"/>
					<xsl:with-param name="lab_approval_action_cancel_req_appr" select="$lab_approval_action_cancel_req_appr"/>
					<xsl:with-param name="lab_approval_action_appr_pub" select="$lab_approval_action_appr_pub"/>
					<xsl:with-param name="lab_approval_action_decline_appr_pub" select="$lab_approval_action_decline_appr_pub"/>
				</xsl:apply-templates>
			</div>
			<div role="tabpanel" class="tab-pane" id="senior">
				<xsl:apply-templates select="section[@id = 2]">
					<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
					<xsl:with-param name="lab_g_txt_btn_book_sys" select="$lab_g_txt_btn_book_sys"/>
					<xsl:with-param name="lab_g_txt_btn_run_info" select="$lab_g_txt_btn_run_info"/>
					<xsl:with-param name="lab_g_txt_btn_notify" select="$lab_g_txt_btn_notify"/>
					<xsl:with-param name="lab_g_txt_btn_request_approval" select="$lab_g_txt_btn_request_approval"/>
					<xsl:with-param name="lab_g_txt_btn_approval" select="$lab_g_txt_btn_approval"/>
					<xsl:with-param name="lab_g_txt_btn_change_show" select="$lab_g_txt_btn_change_show"/>
					<xsl:with-param name="lab_g_txt_btn_change_hide" select="$lab_g_txt_btn_change_hide"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_run" select="$lab_g_txt_btn_cancel_run"/>
					<xsl:with-param name="lab_g_txt_btn_joining_inst" select="$lab_g_txt_btn_joining_inst"/>
					<xsl:with-param name="lab_g_txt_btn_process_enrol" select="$lab_g_txt_btn_process_enrol"/>
					<xsl:with-param name="lab_g_txt_btn_result" select="$lab_g_txt_btn_result"/>
					<xsl:with-param name="lab_g_txt_btn_remove" select="$lab_g_txt_btn_remove"/>
					<xsl:with-param name="lab_g_txt_btn_session_info" select="$lab_g_txt_btn_session_info"/>
					<xsl:with-param name="lab_g_txt_btn_completion_cri" select="$lab_g_txt_btn_completion_cri"/>
					<xsl:with-param name="lab_g_txt_btn_content" select="$lab_g_txt_btn_content"/>
					<xsl:with-param name="lab_g_txt_btn_pre_exm" select="$lab_g_txt_btn_pre_exm"/>
					<xsl:with-param name="lab_g_txt_btn_cancel_course" select="$lab_g_txt_btn_cancel_course"/>
					<xsl:with-param name="lab_g_txt_btn_item_figure" select="$lab_g_txt_btn_item_figure"/>
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
					<xsl:with-param name="lab_approval_status_preapprove" select="$lab_approval_status_preapprove"/>
					<xsl:with-param name="lab_approval_status_pending_approval" select="$lab_approval_status_pending_approval"/>
					<xsl:with-param name="lab_approval_status_approved" select="$lab_approval_status_approved"/>
					<xsl:with-param name="lab_approval_status_approved_off" select="$lab_approval_status_approved_off"/>
					<xsl:with-param name="lab_approval_status_pending_reapproval" select="$lab_approval_status_pending_reapproval"/>
					<xsl:with-param name="lab_approval_action_req_appr" select="$lab_approval_action_req_appr"/>
					<xsl:with-param name="lab_approval_action_cancel_req_appr" select="$lab_approval_action_cancel_req_appr"/>
					<xsl:with-param name="lab_approval_action_appr_pub" select="$lab_approval_action_appr_pub"/>
					<xsl:with-param name="lab_approval_action_decline_appr_pub" select="$lab_approval_action_decline_appr_pub"/>
				</xsl:apply-templates>
			</div>
		</div>
	</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="section">
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_book_sys"/>
		<xsl:param name="lab_g_txt_btn_run_info"/>
		<xsl:param name="lab_g_txt_btn_notify"/>
		<xsl:param name="lab_g_txt_btn_request_approval"/>
		<xsl:param name="lab_g_txt_btn_approval"/>
		<xsl:param name="lab_g_txt_btn_change_show"/>
		<xsl:param name="lab_g_txt_btn_change_hide"/>
		<xsl:param name="lab_g_txt_btn_cancel_run"/>
		<xsl:param name="lab_g_txt_btn_joining_inst"/>
		<xsl:param name="lab_g_txt_btn_process_enrol"/>
		<xsl:param name="lab_g_txt_btn_result"/>
		<xsl:param name="lab_g_txt_btn_remove"/>
		<xsl:param name="lab_g_form_btn_chg_prpts"/>
		<xsl:param name="lab_g_form_btn_add"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_g_txt_btn_session_info"/>
		<xsl:param name="lab_g_txt_btn_completion_cri"/>
		<xsl:param name="lab_g_txt_btn_content"/>
		<xsl:param name="lab_g_txt_btn_pre_exm"/>
		<xsl:param name="lab_g_txt_btn_cancel_course"/>
		<xsl:param name="lab_g_txt_btn_item_figure"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_approval_status_preapprove"/>
		<xsl:param name="lab_approval_status_pending_approval"/>
		<xsl:param name="lab_approval_status_approved"/>
		<xsl:param name="lab_approval_status_approved_off"/>
		<xsl:param name="lab_approval_status_pending_reapproval"/>
		<xsl:param name="lab_approval_action_req_appr"/>
		<xsl:param name="lab_approval_action_cancel_req_appr"/>
		<xsl:param name="lab_approval_action_appr_pub"/>
		<xsl:param name="lab_approval_action_decline_appr_pub"/>
		<table>
			<tr>
				<td height="10" align="right" width="20%">
				</td>
				<td width="80%">
				</td>
			</tr>
			<xsl:apply-templates select="*[name() != 'auto_enroll_target_learners']" mode="draw_row">
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
				<xsl:with-param name="lab_approval_status_preapprove" select="$lab_approval_status_preapprove"/>
				<xsl:with-param name="lab_approval_status_pending_approval" select="$lab_approval_status_pending_approval"/>
				<xsl:with-param name="lab_approval_status_approved" select="$lab_approval_status_approved"/>
				<xsl:with-param name="lab_approval_status_approved_off" select="$lab_approval_status_approved_off"/>
				<xsl:with-param name="lab_approval_status_pending_reapproval" select="$lab_approval_status_pending_reapproval"/>
				<xsl:with-param name="lab_approval_action_req_appr" select="$lab_approval_action_req_appr"/>
				<xsl:with-param name="lab_approval_action_cancel_req_appr" select="$lab_approval_action_cancel_req_appr"/>
				<xsl:with-param name="lab_approval_action_appr_pub" select="$lab_approval_action_appr_pub"/>
				<xsl:with-param name="lab_approval_action_decline_appr_pub" select="$lab_approval_action_decline_appr_pub"/>
			</xsl:apply-templates>
			<tr>
				<td height="10" align="right" width="20%">
				</td>
				<td width="80%">
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- Blocker for draw_row mode =============================================-->
	<xsl:template match="title | hidden" mode="draw_row"/>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="draw_row">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_approval_status_preapprove"/>
		<xsl:param name="lab_approval_status_pending_approval"/>
		<xsl:param name="lab_approval_status_approved"/>
		<xsl:param name="lab_approval_status_approved_off"/>
		<xsl:param name="lab_approval_status_pending_reapproval"/>
		<xsl:param name="lab_approval_action_req_appr"/>
		<xsl:param name="lab_approval_action_cancel_req_appr"/>
		<xsl:param name="lab_approval_action_appr_pub"/>
		<xsl:param name="lab_approval_action_decline_appr_pub"/>
		<xsl:variable name="draw">
			<xsl:call-template name="get_show"/>
		</xsl:variable>
		<xsl:if test="contains($draw,'true')">
			<tr>
				<td align="right" valign="top" class="wzb-form-label">
					<xsl:choose>
						<xsl:when test="name() != 'link_list'">
							<xsl:variable name="field_desc">
								<xsl:call-template name="get_desc"/>
							</xsl:variable>
							<xsl:if test="$field_desc != ''">
								<xsl:value-of select="$field_desc"/>
								<xsl:text>：</xsl:text>
							</xsl:if>
						</xsl:when>
						<xsl:otherwise>&#160;</xsl:otherwise>
					</xsl:choose>
				</td>
				<td valign="top"  class="wzb-form-control">
					<xsl:if test="prefix">
						<xsl:call-template name="prefix"/>
					</xsl:if>
					<xsl:apply-templates select="." mode="gen_field">
						<xsl:with-param name="text_class">Text</xsl:with-param>
						<xsl:with-param name="lab_yes" select="$lab_yes"/>
						<xsl:with-param name="lab_no" select="$lab_no"/>
						<xsl:with-param name="lab_approval_status_preapprove" select="$lab_approval_status_preapprove"/>
						<xsl:with-param name="lab_approval_status_pending_approval" select="$lab_approval_status_pending_approval"/>
						<xsl:with-param name="lab_approval_status_approved" select="$lab_approval_status_approved"/>
						<xsl:with-param name="lab_approval_status_approved_off" select="$lab_approval_status_approved_off"/>
						<xsl:with-param name="lab_approval_status_pending_reapproval" select="$lab_approval_status_pending_reapproval"/>
						<xsl:with-param name="lab_approval_action_req_appr" select="$lab_approval_action_req_appr"/>
						<xsl:with-param name="lab_approval_action_cancel_req_appr" select="$lab_approval_action_cancel_req_appr"/>
						<xsl:with-param name="lab_approval_action_appr_pub" select="$lab_approval_action_appr_pub"/>
						<xsl:with-param name="lab_approval_action_decline_appr_pub" select="$lab_approval_action_decline_appr_pub"/>
					</xsl:apply-templates>
					<xsl:if test="suffix">
						<xsl:call-template name="suffix"/>
					</xsl:if>
				</td>
			</tr>
			<xsl:call-template name="draw_separate"/>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

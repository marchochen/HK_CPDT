<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/trun_date.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<!-- Modification -->
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no"/>
	<!-- =============================================================== -->
	<xsl:variable name="root_ent_id" select="/applyeasy/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="itm_id" select="/applyeasy/approval_list/approval_record/item/@id"/>
	<xsl:variable name="workflow_process_root" select="/applyeasy/workflow/process"/>
	<xsl:variable name="type" select="/applyeasy/approval_list/@type"/>
	<xsl:variable name="cur_page" select="/applyeasy/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/applyeasy/pagination/@page_size"/>
	<xsl:variable name="timestamp" select="/applyeasy/pagination/@timestamp"/>
	<xsl:variable name="sort_col" select="/applyeasy/pagination/@sort_col"/>
	<xsl:variable name="sort_order" select="/applyeasy/pagination/@sort_order"/>
	<xsl:variable name="my_role" select="/applyeasy/meta/cur_usr/role/@id"/>
	<xsl:variable name="app_process_status" select="/applyeasy/approval_list/approval_record/application/pending_status/text()"/>
	<xsl:variable name="to_sort_order">
		<xsl:choose>
			<xsl:when test="$sort_order = 'asc' or $sort_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- <xsl:variable name="hasActionBtn" select="/applyeasy/meta/page_variant/"/> -->
	<xsl:variable name="hasActionBtn">true</xsl:variable>
	<!-- =============================================================== -->

	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript">
				<![CDATA[
					usr = new wbUserGroup;
					itm = new wbItem;
					app =  new wbApplication
					function select_type(frm, type){
							url = self.location.href;
							url = setUrlParam('aal_status', type, url);
							url = setUrlParam('cur_page', '1', url);
							url = setUrlParam('sort_order', '', url);
						window.location.href = url;
						return;
					}
					function change_order(order) {
						url = self.location.href;
						url = setUrlParam('sort_order', order);
						url = setUrlParam('cur_page', '1', url);
						window.location.href = url;
						return;
					}
					
				]]>
			</script>


			<script language="Javascript" type="text/javascript"><![CDATA[
				
			function reload_me() {
				self.location.reload();
			}
			
			function change_action(frm,action,action_desc_label,next_status_id,require_action){
				
				if(action==""){return;}
				
				if(_wbApplicationGetMultiActionLst(frm)==""){
					alert(eval("wb_msg_"+ "]]><xsl:value-of select="$wb_lang"/><![CDATA[" +"_select_applicant"));
					return;
				}
				
				//unloadPSrh('1');
				
				frm.curAction.value = action;
				if(require_action == 'true'){
					url = wb_utils_invoke_servlet("cmd","get_prof","stylesheet","application_comment.xsl");
					frm.curAction.value = action;
					if(action_desc_label != ''){
						frm.curActionWarning.value = eval('frm.' + action_desc_label + '.value')
					}
					wbUtilsOpenWin(url,"commentWin",false,"width=700,height=600,toolbar=no,status=no");
				}else{
					if(confirm (eval('wb_msg_' + ']]><xsl:value-of select="$wb_lang"/><![CDATA[' + '_confirm')) ) {
					submit_multi_action(frm,action);
						
					}
				}
			}
			
			function submit_multi_action(frm,action,content){
				var isWorkflowAction = 'false';
				if (content!=null)
					frm.content.value = content;
				]]><xsl:for-each select="$workflow_process_root/status[@name = $app_process_status]/action[access/approver/@type = 'current_approvers']">
					<xsl:variable name="process_id" select="../../@id"/>
					<xsl:variable name="next_status_id" select="@next_status"/>
					<xsl:variable name="next_status" select="/applyeasy/workflow/process[@id = $process_id]/status[@id=$next_status_id]/@name"/><![CDATA[
					if( action == "]]><xsl:value-of select="@name"/><![CDATA["){					
						isWorkflowAction = 'true';
						if(]]><xsl:value-of select="@next_status"/><![CDATA[ == 0){
							if(!confirm(wb_msg_remove_enrollment_confirm))
								return;
						}
						app.multi_action_exec(frm,]]><xsl:value-of select="../../@id"/>,<xsl:value-of select="../@id"/>,<xsl:value-of select="@id"/>,'<xsl:value-of select="../@name"/>','<xsl:value-of select="$next_status"/>','<xsl:value-of select="@verb"/>','<xsl:value-of select="$wb_lang"/>',false,<xsl:value-of select="$itm_id"/><![CDATA[)
					}]]></xsl:for-each><![CDATA[
			}
			function doFeedParam(){
				param = new Array();
				tmpObj1 = new Array();
				tmpObj2 = new Array();
				tmpObj3 = new Array();
				tmpObj4 = new Array();
				tmpObj5 = new Array();
				
				tmpObj1[0] = 'cmd';
				tmpObj1[1] = 'ae_make_multi_actn';
					
				param[param.length] = tmpObj1;
				
				tmpObj2[0] = 'app_id_lst';
				app_id_lst = _wbApplicationGetMultiActionLst(document.frmAction);
				tmpObj2[1] = app_id_lst.split("~");
				param[param.length] = tmpObj2;
				
				tmpObj3[0] = 'app_nm_lst';
				app_nm_lst = _wbApplicationGetMultiActionNameLst(document.frmAction);
				tmpObj3[1] = app_nm_lst.split("~");
				param[param.length] = tmpObj3;

				tmpObj4[0] = 'app_upd_timestamp_lst';
				app_upd_timestamp_lst = _wbApplicationGetMultiActionTimeStampeLst(document.frmAction);
				tmpObj4[1] = app_upd_timestamp_lst.split("~");
				param[param.length] = tmpObj4;
				
				tmpObj5[0] = 'content';
				tmpObj5[1] = document.frmAction.content.value;
				param[param.length] = tmpObj5;
				
				return param;
			}
			function init(){				
				var url, openScreenLeft, openScreenTop,enl_psrh_lst_srhTime				
				
				url = wb_utils_get_cookie('enl_psrh_lst')
				openScreenLeft = wb_utils_get_cookie('enl_psrh_lst_opnScnLeft')
				openScreenTop = wb_utils_get_cookie('enl_psrh_lst_opnScnTop')
				enl_psrh_lst_srhTime =  wb_utils_get_cookie('enl_psrh_lst_srhTime')
				
				if (url != null && url != ""){			
					url = url + '&close_when_empty=1';	
					
					var width, height,str_feature
					width = '528'
					height = '367'
					
					str_feature = 'toolbar='		+ 'no'
							+ ',width=' 				+ width
							+ ',height=' 				+ height
							+ ',scrollbars='			+ 'yes'
							+ ',resizable='				+ 'no'
							+ ',status='				+ 'no';	
					if(document.all) {str_feature += ',top=' + openScreenTop + ',left=' + openScreenLeft;}	
					else {str_feature += ',screenX=' + openScreenLeft + ',screenY=' + openScreenTop;}
								
					if (enl_psrh_lst_srhTime == getUrlParam('enl_psrh_lst_srhTime')) {
						popup_usr_search = wbUtilsOpenWin(url,'popup_usr_search',false,str_feature);
					}				
				}
				
				app_nm = new Array();
				]]><xsl:for-each select="/applyeasy/approval_list/approval_record/applicant">
					<xsl:variable name="escaped_display_name">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str" select="display_bil/text()"/>
						</xsl:call-template>
					</xsl:variable><![CDATA[
					app_nm[app_nm.length] = ']]><xsl:value-of select="$escaped_display_name"/><![CDATA[';]]></xsl:for-each><![CDATA[
				
			}
				]]>
			</script>



		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<!-- Modification replace "frmXml" with "frmAction"  -->
			<form name="frmAction">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="app_id_lst" value=""/>
				<input type="hidden" name="app_id" value=""/>
				<input type="hidden" name="app_upd_timestamp_lst" value=""/>
				<input type="hidden" name="process_id" value=""/>
				<input type="hidden" name="status_id" value=""/>
				<input type="hidden" name="content"/>
				<input type="hidden" name="action_id" value=""/>
				<input type="hidden" name="fr" value=""/>
				<input type="hidden" name="to" value=""/>
				<input type="hidden" name="verb" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="curActionWarning" value=""/>
				<input type="hidden" name="curAction" value=""/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrol_approval"><xsl:value-of select="$lab_const_enrollment"/>審批</xsl:with-param>
			<xsl:with-param name="lab_pls_select">請選擇</xsl:with-param>
			<xsl:with-param name="lab_pending_list">等待審批</xsl:with-param>
			<xsl:with-param name="lab_history">已審批</xsl:with-param>
			<xsl:with-param name="lab_pend_desc">以下是需要你審批的報名請求。按<b>詳細資訊</b>查看報名的詳情。按<b>"審批"</b>或<b>"拒絕"</b>作出你的決定。</xsl:with-param>
			<xsl:with-param name="lab_pending_since">開始等待日期</xsl:with-param>
			<xsl:with-param name="lab_learner">學員</xsl:with-param>
			<xsl:with-param name="lab_ds">直屬上司</xsl:with-param>
			<xsl:with-param name="lab_gs">用戶組上司</xsl:with-param>
			<xsl:with-param name="lab_tadm">培訓管理員</xsl:with-param>
			<xsl:with-param name="lab_group">用戶組</xsl:with-param>
			<xsl:with-param name="lab_course">課程</xsl:with-param>
			<xsl:with-param name="lab_app_role">審批身份</xsl:with-param>
			<xsl:with-param name="lab_latest_decision">最近的審批決定</xsl:with-param>
			<xsl:with-param name="lab_approve">批準</xsl:with-param>
			<xsl:with-param name="lab_decline">拒絕</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_confirm">確認</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_processed_by">決定者</xsl:with-param>
			<xsl:with-param name="lab_no_item">沒有審批記錄。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_info">詳細資訊</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrol_approval"><xsl:value-of select="$lab_const_enrollment"/>审批</xsl:with-param>
			<xsl:with-param name="lab_pls_select">请选择</xsl:with-param>
			<xsl:with-param name="lab_pending_list">等待审批</xsl:with-param>
			<xsl:with-param name="lab_history">已审批</xsl:with-param>
			<xsl:with-param name="lab_pend_desc">以下是需要您审批的报名请求。 点击<b>详细信息</b>查看报名的详情。 点击<b>"审批"</b>或<b>"拒绝"</b>作出您的决定。</xsl:with-param>
			<xsl:with-param name="lab_pending_since">开始等待日期</xsl:with-param>
			<xsl:with-param name="lab_learner">学员</xsl:with-param>
			<xsl:with-param name="lab_ds">直属领导</xsl:with-param>
			<xsl:with-param name="lab_gs">用户组领导</xsl:with-param>
			<xsl:with-param name="lab_tadm">培训管理员</xsl:with-param>
			<xsl:with-param name="lab_group">用户组</xsl:with-param>
			<xsl:with-param name="lab_course">课程</xsl:with-param>
			<xsl:with-param name="lab_app_role">审批身份</xsl:with-param>
			<xsl:with-param name="lab_latest_decision">最近的审批决定</xsl:with-param>
			<xsl:with-param name="lab_approve">批准</xsl:with-param>
			<xsl:with-param name="lab_decline">拒绝</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_confirm">确认</xsl:with-param>
			<xsl:with-param name="lab_date">日期</xsl:with-param>
			<xsl:with-param name="lab_processed_by">决定者</xsl:with-param>
			<xsl:with-param name="lab_no_item">没有审批记录。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_info">详细信息</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_enrol_approval">Enrollment approval</xsl:with-param>
			<xsl:with-param name="lab_pls_select">Please select</xsl:with-param>
			<xsl:with-param name="lab_pending_list">Pending approvals</xsl:with-param>
			<xsl:with-param name="lab_history">Previous approvals</xsl:with-param>
			<xsl:with-param name="lab_pend_desc">Listed below is a list of enrollments pending your approval. For each enrollment, click <b>Details</b> to view the enrollment details, click <b>"approval"</b> or <b>"decline"</b> to make your approval decision.</xsl:with-param>
			<xsl:with-param name="lab_pending_since">Pending since</xsl:with-param>
			<xsl:with-param name="lab_learner">Learner</xsl:with-param>
			<xsl:with-param name="lab_ds">Direct supervisor</xsl:with-param>
			<xsl:with-param name="lab_gs">Group supervisor</xsl:with-param>
			<xsl:with-param name="lab_tadm">Training administrator</xsl:with-param>
			<xsl:with-param name="lab_group">Group</xsl:with-param>
			<xsl:with-param name="lab_course">Course</xsl:with-param>
			<xsl:with-param name="lab_app_role">Approval role</xsl:with-param>
			<xsl:with-param name="lab_latest_decision">Latest decision</xsl:with-param>
			<xsl:with-param name="lab_approve">Approve</xsl:with-param>
			<xsl:with-param name="lab_decline">Decline</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_confirm">Confirm</xsl:with-param>
			<xsl:with-param name="lab_date">Date</xsl:with-param>
			<xsl:with-param name="lab_processed_by">Done by</xsl:with-param>
			<xsl:with-param name="lab_no_item">No approvals found.</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_info">Details</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<xsl:param name="lab_enrol_approval"/>
		<xsl:param name="lab_pls_select"/>
		<xsl:param name="lab_pending_list"/>
		<xsl:param name="lab_history"/>
		<xsl:param name="lab_pend_desc"/>
		<xsl:param name="lab_pending_since"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_ds"/>
		<xsl:param name="lab_gs"/>
		<xsl:param name="lab_tadm"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_app_role"/>
		<xsl:param name="lab_latest_decision"/>
		<xsl:param name="lab_approve"/>
		<xsl:param name="lab_decline"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_confirm"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_processed_by"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_g_txt_btn_info"/>
		
		<xsl:variable name="pending_class">
			<xsl:if test="$type = 'PENDING'">
				<xsl:text>active</xsl:text>
			</xsl:if>
		</xsl:variable>
		<xsl:variable name="history_class">
			<xsl:if test="$type = 'HISTORY'">
				<xsl:text>active</xsl:text>
			</xsl:if>
		</xsl:variable>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>	
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_enrol_approval"/>
		</xsl:call-template>
		
		<xsl:if test="$type = 'PENDING'">
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text" select="$lab_pend_desc"/>
			</xsl:call-template>
		</xsl:if>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" >
			<tr>
				<td>
					<ul  id='tab_menu' class="nav nav-tabs page-tabs" role="tablist">
						<li role="presentation" class="{$pending_class}">
							<a tabName="pending" href="javascript:void(0)"  onclick="select_type(document.frmAction, 'PENDING');" aria-controls="faq" role="tab" data-toggle="tab">
								<xsl:value-of select="$lab_pending_list"/>
							</a>
						</li>											
						<li role="presentation" class="{$history_class}">					
							<a tabName="history" href="javascript:void(0)"  onclick="select_type(document.frmAction, 'HISTORY');" aria-controls="faq" role="tab" data-toggle="tab">									
								<xsl:value-of select="$lab_history"/>
							</a>
						</li>
					</ul>
				</td>
			</tr>
		</table>
		<xsl:if test="$type = 'PENDING'">
			<table style="margin-top: -36px;">
				<tr>
					<td></td>
					<td align="right">
						<xsl:variable name="cur_status" select="approval_list/approval_record/application/pending_status/text()"/>
						<xsl:apply-templates select="/applyeasy/workflow/process/status[@name = $cur_status]">
							<xsl:with-param name="app_id" select="approval_list/approval_record/application/@id"/>
							<xsl:with-param name="upd_timestamp" select="approval_list/approval_record/application/update_timestamp/text()"/>
							<xsl:with-param name="lab_approve" select="$lab_approve"/>
							<xsl:with-param name="lab_decline" select="$lab_decline"/>
						</xsl:apply-templates>	
					</td>
				</tr>
			</table>
		</xsl:if>
		
		<xsl:choose>
			<xsl:when test="$type = 'PENDING'">
				<xsl:apply-templates mode="pending_list" select=".">
					<xsl:with-param name="lab_pls_select" select="$lab_pls_select"/>
					<xsl:with-param name="lab_pending_list" select="$lab_pending_list"/>
					<xsl:with-param name="lab_history" select="$lab_history"/>
					<xsl:with-param name="lab_pend_desc" select="$lab_pend_desc"/>
					<xsl:with-param name="lab_pending_since" select="$lab_pending_since"/>
					<xsl:with-param name="lab_learner" select="$lab_learner"/>
					<xsl:with-param name="lab_ds" select="$lab_ds"/>
					<xsl:with-param name="lab_gs" select="$lab_gs"/>
					<xsl:with-param name="lab_tadm" select="$lab_tadm"/>
					<xsl:with-param name="lab_group" select="$lab_group"/>
					<xsl:with-param name="lab_course" select="$lab_course"/>
					<xsl:with-param name="lab_app_role" select="$lab_app_role"/>
					<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
					<xsl:with-param name="lab_g_txt_btn_info" select="$lab_g_txt_btn_info"/>
				</xsl:apply-templates>
			</xsl:when>
			<xsl:when test="$type = 'HISTORY'">
				<xsl:apply-templates mode="history_list" select=".">
					<xsl:with-param name="lab_pls_select" select="$lab_pls_select"/>
					<xsl:with-param name="lab_pending_list" select="$lab_pending_list"/>
					<xsl:with-param name="lab_history" select="$lab_history"/>
					<xsl:with-param name="lab_learner" select="$lab_learner"/>
					<xsl:with-param name="lab_ds" select="$lab_ds"/>
					<xsl:with-param name="lab_gs" select="$lab_gs"/>
					<xsl:with-param name="lab_tadm" select="$lab_tadm"/>
					<xsl:with-param name="lab_group" select="$lab_group"/>
					<xsl:with-param name="lab_course" select="$lab_course"/>
					<xsl:with-param name="lab_latest_decision" select="$lab_latest_decision"/>
					<xsl:with-param name="lab_approve" select="$lab_approve"/>
					<xsl:with-param name="lab_decline" select="$lab_decline"/>
					<xsl:with-param name="lab_cancel" select="$lab_cancel"/>
					<xsl:with-param name="lab_confirm" select="$lab_confirm"/>
					<xsl:with-param name="lab_date" select="$lab_date"/>
					<xsl:with-param name="lab_processed_by" select="$lab_processed_by"/>
					<xsl:with-param name="lab_no_item" select="$lab_no_item"/>
					<xsl:with-param name="lab_g_txt_btn_info" select="$lab_g_txt_btn_info"/>
				</xsl:apply-templates>
			</xsl:when>
		</xsl:choose>
		<xsl:if test="//approval_record">
			<xsl:call-template name="wb_ui_pagination">
				<xsl:with-param name="cur_page" select="$cur_page"/>
				<xsl:with-param name="page_size" select="$page_size"/>
				<xsl:with-param name="total" select="$total"/>
				<xsl:with-param name="width" select="$wb_gen_table_width"/>
				<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
			</xsl:call-template>	
		</xsl:if>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template mode="pending_list" match="applyeasy">
		<xsl:param name="lab_pls_select"/>
		<xsl:param name="lab_pending_list"/>
		<xsl:param name="lab_history"/>
		<xsl:param name="lab_pend_desc"/>
		<xsl:param name="lab_pending_since"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_ds"/>
		<xsl:param name="lab_gs"/>
		<xsl:param name="lab_tadm"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_app_role"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_g_txt_btn_info"/>
		<xsl:choose>
			<xsl:when test="count(approval_list/approval_record) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_no_item"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
			
				<table class="table wzb-ui-table margin-top28" cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr class="wzb-ui-table-head">
						<!-- Modification -->
						<td width="17%" ailgn="left">
							<xsl:call-template name="select_all_checkbox">
							<xsl:with-param name="chkbox_lst_cnt">
							<xsl:value-of select="count(approval_list/approval_record)"/>
							</xsl:with-param>
							<xsl:with-param name="display_icon">false</xsl:with-param>
							<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
							<xsl:with-param name="frm_name">frmAction</xsl:with-param>
							</xsl:call-template>
							
							<a href="javascript:change_order('{$to_sort_order}')" class="TitleText">
								<xsl:value-of select="$lab_pending_since"/>
								<xsl:call-template name="wb_sortorder_cursor">
									<xsl:with-param name="img_path">
										<xsl:value-of select="$wb_img_path"/>
									</xsl:with-param>
									<xsl:with-param name="sort_order">
										<xsl:value-of select="$sort_order"/>
									</xsl:with-param>
								</xsl:call-template>
							</a>
							
						</td>
						<td width="15%" ailgn="left">
							<span class="TitleText">
								<xsl:value-of select="$lab_learner"/>
							</span>
						</td>
						<td width="27%" ailgn="left">
							<span class="TitleText">
								<xsl:value-of select="$lab_group"/>
							</span>
						</td>
						<td width="15%" ailgn="left">
							<span class="TitleText">
								<xsl:value-of select="$lab_course"/>
							</span>
						</td>
						<td width="15%" ailgn="left">
							<span class="TitleText">
								<xsl:value-of select="$lab_app_role"/>
							</span>
						</td>
						
						<td width="15%" ailgn="left">
							<IMG src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</tr>
					<xsl:for-each select="approval_list/approval_record">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>					
						<tr class="{$row_class}">
							<!-- Modification -->
							<td width="17%">
								<xsl:variable name="app_ent_nm" select="applicant/display_bil/text()"/>
								<input type="checkbox" value="{application/@id}" name="app_id" id="{$app_ent_nm}"/>
								<input type="hidden" name="app_upd_timestamp_{application/@id}" value="{application/update_timestamp/text()}"/>
								<input type="hidden" name="display_name_{application/@id}" value="{$app_ent_nm}"/>
								
								<span class="Text">
									<xsl:call-template name="trun_date">
										<xsl:with-param name="my_timestamp">
											<xsl:value-of select="create_timestamp/text()"/>
										</xsl:with-param>
									</xsl:call-template>
								</span>
							</td>
							<td width="15%" ailgn="left">
								<span class="Text">
									<xsl:variable name="app_ent_id" select="applicant/@ent_id"/>
									<a class="Text" href="javascript:usr.user.manage_usr_popup({$app_ent_id},{$root_ent_id},'','')">
										<xsl:value-of select="applicant/display_bil/text()"/>
									</a>	
								</span>
							</td>
							<td width="15%" ailgn="left">
								<span class="Text">
									<xsl:value-of select="applicant/group_full_path/text()"/>
								</span>
							</td>
							<td width="15%" ailgn="left">
								<xsl:variable name="app_ent_id" select="applicant/@ent_id"/>
								<xsl:variable name="itm_id" select="item/@id"/>
								<a class="Text" href="javascript:itm.itm_view_popup({$itm_id}, {$app_ent_id})">
									<xsl:value-of select="item/title/text()"/>
									<xsl:if test="item/item">
										<xsl:text> - </xsl:text>
										<xsl:value-of select="item/item/title/text()"/>
									</xsl:if>
								</a>
							</td>
							<td width="15%" ailgn="left" >
								<span class="Text">
									<xsl:choose>
										<xsl:when test="approver/approval_role/text() = 'DIRECT_SUPERVISE'"><xsl:value-of select="$lab_ds"/></xsl:when>
										<xsl:when test="approver/approval_role/text() = 'SUPERVISE'"><xsl:value-of select="$lab_gs"/></xsl:when>
										<xsl:when test="approver/approval_role/text() = 'TADM'"><xsl:value-of select="$lab_tadm"/></xsl:when>
										<xsl:when test="approver/approval_role/text() = 'LRNR'"><xsl:value-of select="$lab_learner"/></xsl:when>
									</xsl:choose>
								</span>
							</td>
							<td width="15%" ailgn="left" nowrap="nowrap" style="text-align:right;">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_info"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:app.enrol_approval.get_lrn_info(<xsl:value-of select="application/@id"/>)</xsl:with-param>
								</xsl:call-template>
								<!-- Modification -->
								<!--<xsl:choose>
									<xsl:when test="$hasActionBtn = 'true'">
										<xsl:variable name="cur_status" select="application/pending_status/text()"/>
										<xsl:apply-templates select="/applyeasy/workflow/process/status[@name = $cur_status]">
											<xsl:with-param name="app_id" select="application/@id"/>
											<xsl:with-param name="upd_timestamp" select="application/update_timestamp/text()"/>
										</xsl:apply-templates>
									</xsl:when>
									<xsl:otherwise><IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/></xsl:otherwise>
								</xsl:choose>-->
								<IMG src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
								<!-- Modification -->
							</td>
						</tr>
					</xsl:for-each>
				</table>
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="status">
		<xsl:param name="app_id"/>
		<xsl:param name="upd_timestamp"/>
		<xsl:param name="lab_approve"/>
		<xsl:param name="lab_decline"/>
		<xsl:for-each select="action">
			<xsl:if test="access/approver/@type = 'current_approvers'">
				<xsl:variable name="next_status_id" select="@next_status"/>
				<xsl:variable name="next_status" select="../../status[@id = $next_status_id]/@name"/>
				<xsl:variable name="require_reason" select="@require_reason"/>
				<xsl:variable name="js_var_suffix" select="concat('_', ../../@id, '_', ../@id, '_', @id)"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript">
					var action_this_status_name<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="../@name"/>';
					var action_next_status_name<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="$next_status"/>';
					var action_verb<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="@verb"/>';
					var action<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="@name"/>';
				</script>
				<xsl:variable name ="button_name">
					<xsl:choose>
					    <!-- 审批 -->
						<xsl:when test="@id='1' "><xsl:value-of select="$lab_approve"/></xsl:when>
						 <!-- 拒绝 -->
						<xsl:when test="@id='2' "><xsl:value-of select="$lab_decline"/></xsl:when>
					</xsl:choose>
				</xsl:variable>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="wb_gen_btn_name" select="$button_name"/>
					<xsl:with-param name="class">btn wzb-btn-orange margin-right4 </xsl:with-param>
					<xsl:with-param name="wb_gen_btn_href">javascript:change_action(document.frmAction,action<xsl:value-of select="$js_var_suffix"/>,'curActionWarning',<xsl:value-of select="$next_status_id"/>,'<xsl:value-of select="$require_reason"/>')</xsl:with-param>
<!--
					<xsl:with-param name="wb_gen_btn_href">javascript:change_action(document.frmAction, '<xsl:value-of select="$wb_lang"/>', <xsl:value-of select="$app_id"/>, '<xsl:value-of select="$upd_timestamp"/>', '<xsl:value-of select="../../@id"/>', '<xsl:value-of select="../@id"/>', '<xsl:value-of select="@id"/>', action_this_status_name<xsl:value-of select="$js_var_suffix"/>, action_next_status_name<xsl:value-of select="$js_var_suffix"/>, action_verb<xsl:value-of select="$js_var_suffix"/>, '<xsl:value-of select="$require_reason"/>')</xsl:with-param>
-->

				</xsl:call-template>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template mode="history_list" match="applyeasy">
		<xsl:param name="lab_pls_select"/>
		<xsl:param name="lab_pending_list"/>
		<xsl:param name="lab_history"/>
		<xsl:param name="lab_learner"/>
		<xsl:param name="lab_ds"/>
		<xsl:param name="lab_gs"/>
		<xsl:param name="lab_tadm"/>
		<xsl:param name="lab_group"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_latest_decision"/>
		<xsl:param name="lab_approve"/>
		<xsl:param name="lab_decline"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_confirm"/>
		<xsl:param name="lab_date"/>
		<xsl:param name="lab_processed_by"/>
		<xsl:param name="lab_no_item"/>
		<xsl:param name="lab_g_txt_btn_info"/>
		<xsl:choose>
			<xsl:when test="count(approval_list/approval_record) = 0">
			
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_no_item"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table margin-top28" cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
					<tr class="SecBg wzb-ui-table-head">
						<td width="14%">
							<a href="javascript:change_order('{$to_sort_order}')" class="TitleText">
								<xsl:value-of select="$lab_date"/>
								<xsl:call-template name="wb_sortorder_cursor">
									<xsl:with-param name="img_path">
										<xsl:value-of select="$wb_img_path"/>
									</xsl:with-param>
									<xsl:with-param name="sort_order">
										<xsl:value-of select="$sort_order"/>
									</xsl:with-param>
								</xsl:call-template>
							</a>
						</td>
						<td width="14%" align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_learner"/>
							</span>
						</td>
						<td width="14%" align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_group"/>
							</span>
						</td>
						<td width="16%" align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_course"/>
							</span>
						</td>
						<td width="14%" align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_latest_decision"/>
							</span>
						</td>
						<td width="16%" align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_processed_by"/>
							</span>
						</td>
						<td></td>						
					</tr>
					<xsl:for-each select="approval_list/approval_record">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>					
						<tr class="{$row_class}">
							<td>
								<span class="Text">
									<xsl:call-template name="trun_date">
										<xsl:with-param name="my_timestamp">
											<xsl:value-of select="create_timestamp/text()"/>
										</xsl:with-param>
									</xsl:call-template>
								</span>
							</td>
							<td align="center">
								<span class="Text">
									<xsl:variable name="app_ent_id" select="applicant/@ent_id"/>
									<xsl:value-of select="applicant/display_bil/text()"/><br/>
								</span>
							</td>
							<td align="center">
								<span class="Text">								
									<xsl:value-of select="applicant/group_full_path/text()"/>
								</span>
							</td>
							<td align="center">
								<xsl:variable name="itm_id" select="item/@id"/>
								<xsl:value-of select="item/title/text()"/>
								<xsl:if test="item/item">
									<xsl:text> - </xsl:text>
									<xsl:value-of select="item/item/title/text()"/>
								</xsl:if>
							</td>
							<td align="center">
								<span class="Text">
									<xsl:choose>
										<xsl:when test="action/text() = 'APPROVED'"><xsl:value-of select="$lab_approve"/></xsl:when>
										<xsl:when test="action/text() = 'DISAPPROVED'"><xsl:value-of select="$lab_decline"/></xsl:when>
										<xsl:when test="action/text() = 'ENROLLED'"><xsl:value-of select="$lab_confirm"/></xsl:when>
										<xsl:when test="action/text() = 'CANCELLED'"><xsl:value-of select="$lab_cancel"/></xsl:when>
									</xsl:choose>
								</span>
							</td>
							<td align="center">
								<span class="Text">
									<xsl:value-of select="action_taker/display_bil/text()"/>
									<br/>
									<xsl:text>(</xsl:text>
									<xsl:choose>
										<xsl:when test="action_taker/approval_role/text() = 'DIRECT_SUPERVISE'"><xsl:value-of select="$lab_ds"/></xsl:when>
										<xsl:when test="action_taker/approval_role/text() = 'SUPERVISE'"><xsl:value-of select="$lab_gs"/></xsl:when>
										<xsl:when test="action_taker/approval_role/text() = 'TADM'"><xsl:value-of select="$lab_tadm"/></xsl:when>
										<xsl:when test="action_taker/approval_role/text() = 'LRNR'"><xsl:value-of select="$lab_learner"/></xsl:when>
									</xsl:choose>
									<xsl:text>)</xsl:text>
								</span>
							</td>							
							<td align="right">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_info"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:app.enrol_approval.get_lrn_history_info(<xsl:value-of select="application/@id"/>)</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:for-each>					
				</table>
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	
	<!-- =============================================================== -->
</xsl:stylesheet>


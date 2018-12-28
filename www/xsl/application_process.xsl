<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<!-- itm utils -->
	<xsl:import href="share/itm_gen_details_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="label" select="/applyeasy/meta/label_reference_data_list"/>
	<xsl:variable name="lab_enrollment_details" select="$label/label[@name = 'lab_enrollment_details']"/>
	<xsl:variable name="lab_learner" select="$label/label[@name = 'lab_learner']"/>
	<xsl:variable name="lab_course" select="$label/label[@name = 'lab_course']"/>
	<xsl:variable name="lab_enrollment_log" select="$label/label[@name = 'lab_enrollment_log']"/>
	<xsl:variable name="lab_action" select="$label/label[@name = 'lab_action']"/>
	<xsl:variable name="lab_date" select="$label/label[@name = 'lab_date']"/>
	<xsl:variable name="lab_by" select="$label/label[@name = 'lab_by']"/>
	<xsl:variable name="lab_no_remarks" select="$label/label[@name = 'lab_no_remarks']"/>
	<xsl:variable name="lab_remarks_by" select="$label/label[@name = 'lab_remarks_by']"/>
	<xsl:variable name="lab_content" select="$label/label[@name = 'lab_content']"/>
	<xsl:variable name="lab_remarks" select="$label/label[@name = 'lab_remarks']"/>
	<xsl:variable name="lab_add_remark" select="$label/label[@name = 'lab_add_remark']"/>
	<xsl:variable name="lab_no_action" select="$label/label[@name = 'lab_no_action']"/>
	<xsl:variable name="lab_confirm_action" select="$label/label[@name = 'lab_confirm_action']"/>
	<xsl:variable name="lab_no_more_action" select="$label/label[@name = 'lab_no_more_action']"/>
	<xsl:variable name="lab_next_action" select="$label/label[@name = 'lab_next_action']"/>
	<xsl:variable name="lab_process_application" select="$label/label[@name = 'lab_process_application']"/>
	<xsl:variable name="lab_applicant_info" select="$label/label[@name = 'lab_applicant_info']"/>
	<xsl:variable name="lab_g_txt_btn_view_lrn_his" select="$label/label[@name = 'lab_g_txt_btn_view_lrn_his']"/>
	<xsl:variable name="lab_g_form_btn_add_comment" select="$label/label[@name = 'lab_g_form_btn_add_comment']"/>
	<xsl:variable name="lab_g_form_btn_ok" select="$label/label[@name = 'lab_g_form_btn_ok']"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="$label/label[@name = 'lab_g_form_btn_cancel']"/>
	<xsl:variable name="lab_applied" select="$label/label[@name = 'lab_applied']"/>
	<xsl:variable name="lab_remarks_requirement" select="$label/label[@name = 'lab_remarks_requirement']"/>
	<xsl:variable name="lab_status" select="$label/label[@name = 'lab_status']"/>
	<xsl:variable name="lab_pending_approver" select="$label/label[@name = 'lab_pending_approver']"/>
	<xsl:variable name="lab_DIRECT_SUPERVISE" select="$label/label[@name = 'lab_DIRECT_SUPERVISE']"/>
	<xsl:variable name="lab_TADM" select="$label/label[@name = 'lab_TADM']"/>
	<xsl:variable name="lab_SUPERVISE" select="$label/label[@name = 'lab_SUPERVISE']"/>
	<xsl:variable name="lab_na" select="$label/label[@name = 'lab_na']"/>
	<xsl:variable name="lab_g_form_btn_back" select="$label/label[@name = 'lab_g_form_btn_back']"/>
	<xsl:variable name="lab_btn_edit_com" select="$label/label[@name = 'lab_btn_edit_com']"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="applyeasy/item/@id"/>
	<xsl:variable name="itm_title" select="applyeasy/item/@title"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="app_id" select="/applyeasy/application/@id"/>
	<xsl:variable name="app_ent_id" select="/applyeasy/application/applicant/@ent_id"/>
	<xsl:variable name="app_name" select="/applyeasy/application/applicant/display_name"/>
	<xsl:variable name="cos_info" select="/applyeasy/item_template/item/valued_template/section/*"/>
	<xsl:variable name="enrollment_log" select="/applyeasy/application/history/action_history"/>
	<xsl:variable name="app_update_timestamp" select="/applyeasy/application/@update_datetime"/>
	<xsl:variable name="comment_history" select="/applyeasy/application/history/comment_history"/>
	<xsl:variable name="application_process" select="/applyeasy/application/application_process"/>
	<xsl:variable name="my_role" select="/applyeasy/meta/cur_usr/@common_role_id"/>
	<xsl:variable name="root_ent_id" select="/applyeasy/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="workflow_process_root" select="/applyeasy/workflow/process"/>
	<xsl:variable name="app_process_status" select="/applyeasy/application/application_process/process/status/@name"/>
	<xsl:variable name="lab_data_import" select="$label/label[@name = 'lab_data_import']"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_lrn_soln.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript"><![CDATA[
			app =  new wbApplication;		
			usr = new wbUserGroup;
			lrn_soln =  new wbLearnSolution;
			itm_lst = new wbItem;
			frmAppr = getUrlParam("frmAppr");
			current = 0;
			function srh_lrn_history_prep(ent_id){			
				var url = wb_utils_invoke_disp_servlet('module', 'report.ReportModule', 'cmd', 'lrn_soln_hist', 'usr_ent_id', ent_id,  'stylesheet', 'lrn_history_view.xsl', 'p', '1', 'sort_col', 'att_timestamp', 'sort_order', 'desc');				var str_feature = 'width='+ '780'
					+ ',height=' 				+ '500'
					+ ',scrollbars='			+ 'yes'
					+ ',resizable='			+ 'yes'
					+ ',toolbar='				+ 'no'
					+ ',screenX='				+ '10'
					+ ',screenY='				+ '10'
					+ ',status='				+ 'yes';
				
				wbUtilsOpenWin(url,'view_lrn_history',false,str_feature);
			}
			
			function change_action(frm, action, action_desc_label, next_status_id){
				lang = ']]><xsl:value-of select="$wb_lang"/><![CDATA['
			
				if(action == ""){
					return;
				}
			
				//unloadPSrh('1');
				if(next_status_id != 0){
					var url = wb_utils_invoke_servlet("cmd", "get_prof", "stylesheet", "application_comment.xsl");
					frm.curAction.value = action;
					if(action_desc_label != ''){
						var ele = eval('frm.' + action_desc_label)
						if(ele[0]) {
							frm.curActionWarning.value = eval('frm.' + action_desc_label + '[0].value')
						} else {
							frm.curActionWarning.value = eval('frm.' + action_desc_label + '.value')
						}
					}
				wbUtilsOpenWin(url, "commentWin", false, "width=700,height=600,toolbar=no,status=no");
				}else{
					//Case Remove
					submit_multi_action(frm,action);
				}
			}			
			
			function submit_multi_action(frm,action,content){
				var isWorkflowAction = 'false';
				//]]><xsl:copy-of select="$app_process_status"/><![CDATA[
				if (content!=null)
					frm.content.value = content;
				]]><xsl:for-each select="$workflow_process_root/status[@name = $app_process_status]/action[access/role/@id = $my_role]">
					<xsl:variable name="process_id" select="../../@id"/>
					<xsl:variable name="next_status_id" select="@next_status"/>
					<xsl:variable name="next_status" select="/applyeasy/workflow/process[@id = $process_id]/status[@id=$next_status_id]/@name"/><![CDATA[
					if( action == "]]><xsl:value-of select="@name"/><![CDATA["){					
						isWorkflowAction = 'true';
						if(]]><xsl:value-of select="@next_status"/><![CDATA[ == 0){
							if(!confirm(wb_msg_remove_enrollment_confirm))
								return;
						}
							app.action_exec(frmAction,]]><xsl:value-of select="$app_id"/><![CDATA[,]]><xsl:value-of select="../../@id"/><![CDATA[,]]><xsl:value-of select="../@id"/><![CDATA[,]]><xsl:value-of select="@id"/><![CDATA[,']]><xsl:value-of select="../@name"/><![CDATA[',']]><xsl:value-of select="$next_status"/><![CDATA[',']]><xsl:value-of select="@verb"/><![CDATA[','',']]><xsl:value-of select="@next_status"/><![CDATA[')
						
					}]]></xsl:for-each><![CDATA[
			}
			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmAction">
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="itm_title" value="{/applyeasy/item/title}"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="app_id" value="{$app_id}"/>
				<input type="hidden" name="app_id_lst"/>
				<input type="hidden" name="app_upd_timestamp_lst" value="{$app_update_timestamp}"/>
				<input type="hidden" name="upd_timestamp" value="{$app_update_timestamp}"/>
				<input type="hidden" name="process_id"/>
				<input type="hidden" name="status_id"/>
				<input type="hidden" name="action_id"/>
				<input type="hidden" name="fr"/>
				<input type="hidden" name="to"/>
				<input type="hidden" name="verb"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="curAction"/>
				<input type="hidden" name="curActionWarning"/>
				<input type="hidden" name="content"/>
				<input type="hidden" name="appn_display_name" value="{$app_name}"/>
				<xsl:apply-templates/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">
				<xsl:choose>
					<xsl:when test="//item/@exam_ind='true'">FTN_AMD_EXAM_MGT</xsl:when>
					<xsl:otherwise>FTN_AMD_ITM_COS_MAIN</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
			<xsl:with-param name="parent_code">
				<xsl:choose>
					<xsl:when test="//item/@exam_ind='true'">FTN_AMD_EXAM_MGT</xsl:when>
					<xsl:otherwise>FTN_AMD_ITM_COS_MAIN</xsl:otherwise>
				</xsl:choose>
			
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_process_application"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_enrollment_details"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
			<!-- 返回课程详细信息连接 -->
			<a href="Javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
				<xsl:value-of select="$itm_title"/>
			</a>
			<xsl:text>&#160;&gt;&#160;</xsl:text>
			<!-- 返回处理报名连接 -->
			<a href="Javascript:app.get_application_list('',{$itm_id},'','','','')" class="NavLink">
				<xsl:value-of select="$lab_process_application"/>
			</a>
			<xsl:text>&#160;&gt;&#160;</xsl:text>
			<!-- 当前的报名用户名 -->
			<xsl:value-of select="$app_name"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
						<xsl:value-of select="$lab_learner"/>
						<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<table	>
						<tr>
							<td style="width:80px;">
							<a href="javascript:usr.user.manage_usr_popup({$app_ent_id},{$root_ent_id},'','')" class="Text">
								<xsl:value-of select="$app_name"/>
								</a>
								</td>
								<td>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_view_lrn_his"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:srh_lrn_history_prep(<xsl:value-of select="$app_ent_id"/>)</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
						<xsl:choose>
							<xsl:when test="//item/@exam_ind='true'">
								<xsl:choose>
									<xsl:when test="//cur_usr/@curLan='zh-cn'">考试</xsl:when>
									<xsl:when test="//cur_usr/@curLan='zh-hk'">考試</xsl:when>
									<xsl:otherwise>Exam</xsl:otherwise>
								</xsl:choose>								
							</xsl:when>
							<xsl:otherwise><xsl:value-of select="$lab_course"/></xsl:otherwise>							
						</xsl:choose><xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="itm_id" select="item_template/item[@run_ind='false']/@id"/>
					<a href="javascript:itm_lst.itm_view_popup({$itm_id})" class="Text">
						<xsl:value-of select="item_template/item[@run_ind='false']/title/text()"/>
						<xsl:if test="item_template/item[@run_ind='true']">
							<xsl:text> - </xsl:text>
							<xsl:value-of select="item_template/item[@run_ind='true']/title/text()"/>
						</xsl:if>
					</a>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_status"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="application/application_process/process/status/@name"/>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_pending_approver"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:variable name="pending_approval_role" select="application/pending_approval_role/text()"/>
					<xsl:choose>
						<xsl:when test="$pending_approval_role = 'DIRECT_SUPERVISE'">
							<xsl:value-of select="$lab_DIRECT_SUPERVISE"/>
						</xsl:when>
						<xsl:when test="$pending_approval_role = 'TADM'">
							<xsl:value-of select="$lab_TADM"/>
						</xsl:when>
						<xsl:when test="$pending_approval_role = 'SUPERVISE'">
							<xsl:value-of select="$lab_SUPERVISE"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_na"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!--
			<xsl:apply-templates select="$cos_info[not(@extract) or @extract != 'true']" mode="draw_row"/>
			<xsl:apply-templates select="/applyeasy/valued_template/section/*" mode="draw_row"/>
			-->
			<xsl:if test="count(application/attempt/action_attempt/action)= 0">
				<xsl:apply-templates select="$application_process/process" mode="process">
					<xsl:with-param name="lab_next_action" select="$lab_next_action"/>
					<xsl:with-param name="lab_no_more_action" select="$lab_no_more_action"/>
				</xsl:apply-templates>
			</xsl:if>
		</table>
		<xsl:call-template name="wb_ui_space"/>
		<xsl:choose>
			<xsl:when test="$enrollment_log/action">
				<xsl:apply-templates select="$enrollment_log" mode="enrollment_log"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_enrollment_log"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_action"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<!-- start add comment -->
		<xsl:choose>
			<xsl:when test="count(application/attempt/action_attempt/action)!= 0"/>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_space"/>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
				<xsl:value-of select="$lab_add_remark"/>
			</xsl:with-param>
					<xsl:with-param name="extra_td">
						<td align="right">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_add_comment"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:app.ins_comment_prep(<xsl:value-of select="$app_id"/>,'<xsl:value-of select="$app_update_timestamp"/>')</xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:choose>
					<xsl:when test="count($comment_history/comment[@action_id='0']) != 0">
						<table class="table wzb-ui-table">
							<tr class="wzb-ui-table-head">
								<td width="30%">
									<xsl:value-of select="$lab_by"/>
								</td>
								<td width="30%" align="center">
									<xsl:value-of select="$lab_date"/>
								</td>
								<td width="46%">
									<xsl:value-of select="$lab_content"/>
								</td>
							</tr>
							<xsl:for-each select="$comment_history/comment[@action_id='0']">
								<tr>
									<td>
										<xsl:value-of select="display_name"/>
									</td>
									<td align="center">
										<xsl:call-template name="display_time">
											<xsl:with-param name="my_timestamp">
												<xsl:value-of select="@upd_timestamp"/>
											</xsl:with-param>
											<xsl:with-param name="dis_time">T</xsl:with-param>
										</xsl:call-template>
									</td>
									<td>
										<xsl:call-template name="unescape_html_linefeed">
											<xsl:with-param name="my_right_value" select="content"/>
										</xsl:call-template>
										<xsl:if test="content = ''">--</xsl:if>
									</td>
									<td>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_edit_com"/>
											<!--
											<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_edit_com"/>
											lab_g_txt_btn_view_lrn_his
											-->
											<xsl:with-param name="wb_gen_btn_href">javascript:app.get_comment('<xsl:value-of select="@id"/>','<xsl:value-of select="@action_id"/>')</xsl:with-param>
										</xsl:call-template>
									</td>
								</tr>
							</xsl:for-each>
						</table>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_remarks"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		<!-- start action comment -->
		<xsl:if test="count(application/attempt/action_attempt/action)!= 0">
			<xsl:apply-templates select="application/attempt/action_attempt/action" mode="action_comment"/>
		</xsl:if>
		<div class="wzb-bar" style="border-top:none;">
		    <xsl:call-template name="wb_ui_line"/>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:app.get_application_list('',<xsl:value-of select="$itm_id"/>)</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- Blocker for draw_row mode -->
	<xsl:template match="title | hidden" mode="draw_row"/>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="draw_row">
		<xsl:variable name="draw">
			<xsl:call-template name="get_show"/>
		</xsl:variable>
		<xsl:if test="$draw = 'true'">
			<tr>
				<td align="right" valign="top">
					<xsl:variable name="text_class">
						<xsl:choose>
							<xsl:when test="@marked='yes'">TitleTextBold</xsl:when>
							<xsl:otherwise/>
						</xsl:choose>
					</xsl:variable>
					<xsl:call-template name="get_desc"/>
					<xsl:text>：</xsl:text>
				</td>
				<td valign="top">
					<xsl:if test="prefix">
						<xsl:call-template name="prefix"/>
					</xsl:if>
					<xsl:apply-templates select="." mode="gen_field">
						<xsl:with-param name="text_class">Text</xsl:with-param>
					</xsl:apply-templates>
					<xsl:if test="suffix">
						<xsl:call-template name="suffix"/>
					</xsl:if>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="enrollment_log">
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_enrollment_log"/>
			</xsl:with-param>
		</xsl:call-template>
		<table class="table wzb-ui-table">
			<tr class="wzb-ui-table-head">
				<td width="20%">
					<xsl:value-of select="$lab_action"/>
				</td>
				<td width="20%">
					<xsl:value-of select="$lab_by"/>
				</td>
				<td width="20%" align="center">
					<xsl:value-of select="$lab_date"/>
				</td>
				<td width="20%">
					<xsl:value-of select="$lab_remarks"/>
				</td>
				<td width="20%" align="right"></td>
			</tr>
			<xsl:for-each select="action">
				<xsl:variable name="action_id" select="@id"/>
				<tr>
					<td>
						<xsl:choose>      
							<xsl:when test="@type != ''">
								<xsl:if test="@type = 'UPLOAD'"><xsl:value-of select="$lab_data_import"/></xsl:if>
								<xsl:if test="@type = 'SYNC'"><xsl:value-of select="$lab_data_import"/></xsl:if>
								<xsl:if test="@type = 'AUTO'">Auto-enrollment</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="@verb"/>
							</xsl:otherwise>	
						</xsl:choose>
					</td>
					<td>
						<xsl:value-of select="display_name"/>
						<xsl:choose>
							<xsl:when test="approval_role">
								<xsl:choose>
									<xsl:when test="approval_role = 'DIRECT_SUPERVISE'">
											(<xsl:value-of select="$lab_DIRECT_SUPERVISE"/>)
										</xsl:when>
									<xsl:when test="approval_role = 'TADM'">
											(<xsl:value-of select="$lab_TADM"/>)
										</xsl:when>
									<xsl:when test="approval_role = 'SUPERVISE'">
											(<xsl:value-of select="$lab_SUPERVISE"/>)
										</xsl:when>
								</xsl:choose>
							</xsl:when>
						</xsl:choose>
					</td>
					<td align="center">
						<xsl:call-template name="display_time">
							<xsl:with-param name="my_timestamp">
								<xsl:value-of select="@upd_timestamp"/>
							</xsl:with-param>
							<xsl:with-param name="dis_time">T</xsl:with-param>
						</xsl:call-template>
					</td>
					<td>
						<xsl:choose>
							<xsl:when test="../../comment_history/comment[@action_id = $action_id]">
								<!-- DENNIS -->
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:choose>
											<xsl:when test="../../comment_history/comment[@action_id = $action_id]/content = '' ">--</xsl:when>
											<xsl:when test="../../comment_history/comment[@action_id = $action_id]/content = 'undefined' ">--</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="../../comment_history/comment[@action_id = $action_id]/content"/>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:otherwise>--</xsl:otherwise>
						</xsl:choose>
					</td>
					<td align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_edit_com"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:app.get_comment('','<xsl:value-of select="@id"/>')</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="process">
		<xsl:variable name="process_id" select="@id"/>
		<xsl:variable name="status_id" select="status/@id"/>
		<tr>
			<td height="10" align="right" width="20%">
				<xsl:text>&#160;</xsl:text>
			</td>
			<td width="80%">
				<xsl:apply-templates select="/applyeasy/workflow/process[@id = $process_id]/status[@id = $status_id]" mode="status">
					<xsl:with-param name="lab_no_more_action">
						<xsl:value-of select="$lab_no_more_action"/>
					</xsl:with-param>
				</xsl:apply-templates>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<!--
	<xsl:template match="*" mode="status">
		<xsl:param name="lab_no_more_action"/>
		<xsl:choose>
			<xsl:when test="count(action/access/role[@id = $my_role]) =0">
				<span class="TitleText">
					<xsl:value-of select="$lab_no_more_action"/>
				</span>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="action">
					<xsl:if test="access/role[@id = $my_role]">
						<xsl:variable name="process_id" select="../../@id"/>
						<xsl:variable name="next_status_id" select="@next_status"/>
						<xsl:variable name="next_status" select="/applyeasy/workflow/process[@id = $process_id]/status[@id=$next_status_id]/@name"/>
						<xsl:choose>
							<xsl:when test="@next_status != 0">
								<input type="button" class="Btn" value="{@name}" onclick="javascript:app.action_exec(frmAction,{$app_id},{../../@id},{../@id},{@id},'{../@name}','{$next_status}','{@verb}',frmAppr)"/>
							</xsl:when>
							<xsl:otherwise>
								<script language="Javascript"><![CDATA[if(frmAppr=="true"){document.write('<span style="position:absolute;visibility:hidden;">');}]]></script>
								<input type="button" class="Btn" value="{@name}" onclick="javascript:app.single_multi_action_exec(document.frmAction,{$process_id},{../@id},{@id},'{../@name}','{$next_status}','{@verb}','{$wb_lang}','',{$itm_id},frmAppr)"/>
								<script language="Javascript"><![CDATA[if(frmAppr=="true"){document.write('</span>');}]]></script>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
					<xsl:if test="position() != last()">
						<img src="{$wb_img_path}tp.gif" width="2" height="1" border="0"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	-->
	<xsl:template match="*" mode="status">
		<xsl:for-each select="action">
			<xsl:if test="access/role[@id = $my_role]">
				<xsl:variable name="require_reason" select="@require_reason"/>
				<xsl:variable name="process_id" select="../../@id"/>
				<xsl:variable name="next_status_id" select="@next_status"/>
				<xsl:variable name="next_status" select="/applyeasy/workflow/process[@id = $process_id]/status[@id=$next_status_id]/@name"/>
				<xsl:choose>
					<xsl:when test="$require_reason = 'true'">
						<xsl:variable name="label_name" select="access/role[@id = $my_role]/label/@name"/>
						<input type="hidden" name="{$label_name}" value="{$label/label[@name = $label_name]}"/>
						<xsl:variable name="js_var_suffix">
							<xsl:choose>
								<xsl:when test="@id = '-1'">
									<xsl:value-of select="concat('_', ../../@id, '_', ../@id, '__1' )"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat('_', ../../@id, '_', ../@id, '_', @id )"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<script LANGUAGE="JavaScript" TYPE="text/javascript">
							var action_this_status_name<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="@name"/>';
						</script>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="@name"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:change_action(document.frmAction,action_this_status_name<xsl:value-of select="$js_var_suffix"/>,'<xsl:value-of select="$label_name"/>',<xsl:value-of select="@next_status"/>)</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:variable name="label_name" select="access/role[@id = $my_role]/label/@name"/>
						<input type="hidden" name="{$label_name}" value="{$label/label[@name = $label_name]}"/>
						<xsl:variable name="js_var_suffix">
							<xsl:choose>
								<xsl:when test="@id = '-1'">
									<xsl:value-of select="concat('_', ../../@id, '_', ../@id, '__1' )"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="concat('_', ../../@id, '_', ../@id, '_', @id )"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<script LANGUAGE="JavaScript" TYPE="text/javascript">
							var action_this_status_name<xsl:value-of select="$js_var_suffix"/> = '<xsl:value-of select="@name"/>';
						</script>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="@name"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:change_action(document.frmAction,action_this_status_name<xsl:value-of select="$js_var_suffix"/>,'<xsl:value-of select="$label_name"/>',<xsl:value-of select="@next_status"/>)</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="position() != last()">
					<img src="{$wb_img_path}tp.gif" width="2" height="1" border="0"/>
				</xsl:if>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="action_comment">
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td colspan="3" height="8">
					<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_confirm_action"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table cellspacing="0" cellpadding="3" border="0" width="{$wb_gen_table_width}" class="Bg">
			<tr>
				<td width="20%" align="right" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_action"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%">
					<span class="Text">
						<xsl:value-of select="@verb"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" valign="top">
					<span class="TitleText">
						<xsl:value-of select="$lab_remarks"/>
						<xsl:text>：</xsl:text>
					</span>
				</td>
				<td width="80%">
					<textarea rows="5" cols="20" style="width:400px;" name="content" class="wzb-inputTextArea"/>
					<br/>
					<span class="Text">
						<xsl:value-of select="$lab_remarks_requirement"/>
					</span>
				</td>
			</tr>
			<tr>
				<td width="20%" align="right" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%" height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table cellpadding="3" cellspacing="0" width="{$wb_gen_table_width}" border="0">
			<tr>
				<td align="center">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:app.ins_history_exec(document.frmAction,'<xsl:value-of select="$itm_id"/>',frmAppr, '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:app.cancel_ins_history_exec(document.frmAction,frmAppr)</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

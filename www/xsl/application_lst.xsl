<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/check_client.xsl"/>
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_form_select_action.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<!-- usr utils -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="cur_page" select="/applyeasy/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/pagination/@total_rec"/>
	<xsl:variable name="page_size" select="/applyeasy/pagination/@page_size"/>
	<xsl:variable name="order_by" select="/applyeasy/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/applyeasy/pagination/@sort_order"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' or $cur_order = 'asc' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- -->
	<xsl:variable name="process_status_cnt_list_root" select="/applyeasy/process_status_cnt_list"/>
	<xsl:variable name="app_process_status" select="/applyeasy/application_list/@status"/>
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_targeted_lrn_cnt" select="count(/applyeasy/targeted_lrn/target_list/target)"/>
	<xsl:variable name="parent_itm_id" select="/applyeasy/item/parent/@id"/>
	<xsl:variable name="workflow_process_root" select="/applyeasy/workflow/process"/>
	<xsl:variable name="filter_value" select="/applyeasy/filter_value"/>
	<!-- -->
	<xsl:variable name="my_role" select="/applyeasy/meta/cur_usr/role/@id"/>
	<xsl:variable name="page_variant_root" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="cur_time" select="/applyeasy/item/@cur_time"/>
	<xsl:variable name="appn_eff_end_datetime" select="/applyeasy/item/@appn_eff_end_datetime"/>
	<xsl:variable name="root_ent_id" select="/applyeasy/meta/cur_usr/@root_ent_id"/>
	<xsl:variable name="label" select="/applyeasy/meta/label_reference_data_list"/>
	<xsl:variable name="total_process_status_cnt" select="/applyeasy/process_status_cnt_list/total_process_status_cnt/@cnt"/>
	<xsl:variable name="profile_attributes" select="/applyeasy/meta/profile_attributes"/>
	<xsl:variable name="columns" select="/applyeasy/columns"/>
	<xsl:variable name="integrated_ind" select="/applyeasy/item/@itm_integrated_ind"/>
	<!-- =============================================================== -->
	<!-- init language labels -->
	<xsl:variable name="lab_no_application" select="$label/label[@name = 'lab_no_application']"/>
	<xsl:variable name="lab_run_info" select="$label/label[@name = 'lab_run_info']"/>
	<xsl:variable name="lab_all_type" select="$label/label[@name = 'lab_all_type']"/>
	<xsl:variable name="lab_add" select="$label/label[@name = 'lab_add']"/>
	<xsl:variable name="lab_sharp" select="$label/label[@name = 'lab_sharp']"/>
	<xsl:variable name="lab_star" select="$label/label[@name = 'lab_star']"/>
	<xsl:variable name="lab_by" select="$label/label[@name = 'lab_by']"/>
	<xsl:variable name="lab_applied" select="$label/label[@name = 'lab_applied']"/>
	<xsl:variable name="lab_on" select="$label/label[@name = 'lab_on']"/>
	<xsl:variable name="lab_status" select="$label/label[@name = 'lab_status']"/>
	<xsl:variable name="lab_please_select" select="$label/label[@name = 'lab_please_select']"/>
	<xsl:variable name="lab_remove" select="$label/label[@name = 'lab_remove']"/>
	<xsl:variable name="lab_enrollment_approval"  select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1157')"/>
	<xsl:variable name="lab_perform_action" select="$label/label[@name = 'lab_perform_action']"/>
	<xsl:variable name="lab_attended_before" select="$label/label[@name = 'lab_attended_before']"/>
	<xsl:variable name="lab_no_show_before" select="$label/label[@name = 'lab_no_show_before']"/>
	<xsl:variable name="lab_non_target_lrn" select="$label/label[@name = 'lab_non_target_lrn']"/>
	<xsl:variable name="lab_non_target_enr" select="$label/label[@name = 'lab_non_target_enr']"/>
	<xsl:variable name="lab_modify" select="$label/label[@name = 'lab_modify']"/>
	<xsl:variable name="lab_lost_and_found" select="$label/label[@name = 'lab_lost_and_found']"/>
	<xsl:variable name="lab_g_txt_btn_notify" select="$label/label[@name = 'lab_g_txt_btn_notify']"/>
	<xsl:variable name="lab_g_txt_btn_add" select="$label/label[@name = 'lab_g_txt_btn_add']"/>
	<xsl:variable name="lab_g_txt_btn_export" select="$label/label[@name = 'lab_g_txt_btn_export']"/>
	<xsl:variable name="lab_no_perform_action" select="$label/label[@name = 'lab_no_perform_action']"/>
	<xsl:variable name="lab_elective_target_learner" select="$label/label[@name = 'lab_elective_target_learner']"/>
	<xsl:variable name="lab_compulsory_target_learner" select="$label/label[@name = 'lab_compulsory_target_learner']"/>
	<xsl:variable name="lab_prerequisite" select="$label/label[@name = 'lab_prerequisite']"/>
	<xsl:variable name="lab_exemption" select="$label/label[@name = 'lab_exemption']"/>
	<xsl:variable name="lab_g_txt_btn_remove" select="$label/label[@name = 'lab_g_txt_btn_remove']"/>
	<xsl:variable name="lab_enrollment_workflow" select="$label/label[@name = 'lab_enrollment_workflow']"/>
	<xsl:variable name="lab_enrollment_list" select="$label/label[@name = 'lab_enrollment_list']"/>
	<xsl:variable name="lab_enrol_list_desc" select="$label/label[@name = 'lab_enrol_list_desc']"/>
	<xsl:variable name="lab_g_txt_btn_edit_workflow" select="$label/label[@name = 'lab_g_txt_btn_edit_workflow']"/>
	<xsl:variable name="lab_pending_approver" select="$label/label[@name = 'lab_pending_approver']"/>
	<xsl:variable name="lab_wf" select="$label/label[@name = 'lab_wf']"/>
	<xsl:variable name="lab_wf_ds" select="$label/label[@name = 'lab_wf_ds']"/>
	<xsl:variable name="lab_wf_tadm" select="$label/label[@name = 'lab_wf_tadm']"/>
	<xsl:variable name="lab_wf_ds_gs" select="$label/label[@name = 'lab_wf_ds_gs']"/>
	<xsl:variable name="lab_wf_ds_gs_tadm" select="$label/label[@name = 'lab_wf_ds_gs_tadm']"/>
	<xsl:variable name="lab_wf_ds_tadm" select="$label/label[@name = 'lab_wf_ds_tadm']"/>
	<xsl:variable name="lab_na" select="$label/label[@name = 'lab_na']"/>
	<xsl:variable name="lab_no_application_of" select="$label/label[@name = 'lab_no_application_of']"/>
	<xsl:variable name="lab_import" select="$label/label[@name = 'lab_import']"/>
	<xsl:variable name="lab_filter" select="$label/label[@name = 'lab_filter']"/>
	<xsl:variable name="lab_adv_filter" select="$label/label[@name = 'lab_adv_filter']"/>
	<xsl:variable name="lab_clear_filter" select="$label/label[@name = 'lab_clear_filter']"/>
	<xsl:variable name="lab_filter_title" select="$label/label[@name = 'lab_filter_title']"/>
	<xsl:variable name="lab_check_popup_window" select="$label/label[@name = 'lab_check_popup_window']"/>
	<xsl:variable name="lab_appn_ps_status_enrolled" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'lab_appn_ps_status_enrolled')"/>
	<xsl:variable name="lab_appn_ps_status_pending_approval" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'lab_appn_ps_status_pending_approval')"/>
	<xsl:variable name="lab_appn_ps_status_waitlisted" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'lab_appn_ps_status_waitlisted')"/>
	<xsl:variable name="lab_appn_ps_status_not_approved" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'lab_appn_ps_status_not_approved')"/>
	<xsl:variable name="lab_appn_ps_status_cancelled" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'lab_appn_ps_status_cancelled')"/>
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_application.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_message.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_batchprocess.js"/>
			<script language="Javascript" type="text/javascript"><![CDATA[
			Batch = new wbBatchProcess
			usr = new wbUserGroup
			itm_lst = new wbItem
			msg = new wbMessage
			app =  new wbApplication			
			app_process_status = getUrlParam("app_process_status")
			var popup_usr_search;
				
			function select_type(frm){
				unloadPSrh('1');
				itm_id = getUrlParam("itm_id")
				if(itm_id == null) {itm_id = '';}
				select_val = frm.type_sel_frm.options[frm.type_sel_frm.selectedIndex].value
				app.set_appn_status_filter(select_val,itm_id)				
			}	
			
			function reload_me() {
				self.location.reload();
			}
			
			function change_action(frm, action,action_desc_label,next_status_id){
				//action = frm.sel_frm.options[frm.sel_frm.selectedIndex].value
				
				if(action==""){return;}
				
				if(_wbApplicationGetMultiActionLst(frm)==""){
					alert(eval("wb_msg_"+ "]]><xsl:value-of select="$wb_lang"/><![CDATA[" +"_select_applicant"));
					return;
				}
				
				unloadPSrh('1');
				
				if(next_status_id != 0){
					url = wb_utils_invoke_servlet("cmd","get_prof","stylesheet","application_comment.xsl");
					frm.curAction.value = action;
					if(action_desc_label != ''){
						var ele = eval('frm.' + action_desc_label)
						if(ele[0]) {
							frm.curActionWarning.value = eval('frm.' + action_desc_label + '[0].value')
						} else {
							frm.curActionWarning.value = eval('frm.' + action_desc_label + '.value')
						}
					}
					wbUtilsOpenWin(url,"commentWin",false,"width=500,height=320,toolbar=no,status=no");
				}else{
					//Case Remove
					submit_multi_action(frm,action);
				}
			}
						
			function submit_multi_action(frm,action,content){
				var isWorkflowAction = 'false';
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
						app.multi_action_exec(frm,]]><xsl:value-of select="../../@id"/>,<xsl:value-of select="../@id"/>,<xsl:value-of select="@id"/>,'<xsl:value-of select="../@name"/>','<xsl:value-of select="$next_status"/>','<xsl:value-of select="@verb"/>','<xsl:value-of select="$wb_lang"/>',false,<xsl:value-of select="$itm_id"/><![CDATA[)
					}]]></xsl:for-each><![CDATA[
			}
			
			function set_cur_status(frm) {
				wb_utils_set_cookie('appr_app_process_status', frm.type_sel_frm.value);
			}
			
			function new_enrollment(auto_enroll){
			var auto_enroll_ind;
				if (auto_enroll[0].checked)  auto_enroll_ind = 'true';
				if (auto_enroll[1].checked)  auto_enroll_ind = 'false';
					]]><xsl:variable name="_itm_id">
					<xsl:choose>
						<xsl:when test="//item/parent">
							<xsl:value-of select="//item/parent/@id"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$itm_id"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable><![CDATA[usr.search.popup_search_prep_ind('ent_ids_lst','',']]><xsl:value-of select="$root_ent_id"/><![CDATA[','0',']]><xsl:value-of select="$itm_id"/><![CDATA[','0','0','0','1','','',auto_enroll_ind,'')]]><![CDATA[
			}
			
			function new_enrollment_no(){
				]]><xsl:variable name="__itm_id">
					<xsl:choose>
						<xsl:when test="//item/parent">
							<xsl:value-of select="//item/parent/@id"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$itm_id"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable><![CDATA[usr.search.popup_search_prep('ent_ids_lst','',']]><xsl:value-of select="$root_ent_id"/><![CDATA[','0',']]><xsl:value-of select="$itm_id"/><![CDATA[','0','','0','1','','','1')]]><![CDATA[
			}

			function doFeedParam(){
				param = new Array();
				tmpObj1 = new Array();
				tmpObj2 = new Array();
				tmpObj3 = new Array();
				tmpObj4 = new Array();
				tmpObj5 = new Array();
				
				tmpObj1[0] = 'cmd';
				action = document.frmAction.sel_frm.options[document.frmAction.sel_frm.selectedIndex].value
				if(action=="remove")
					tmpObj1[1] = 'ae_del_appn';
				else
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
				
			function notify(itm_id){
				var frm = document.frmAction
				var _app_process_status = frm.type_sel_frm.options[frm.type_sel_frm.selectedIndex].value
				var ent_id_lst = app.get_ent_id_lst(frm)
				if( ent_id_lst ==""){
					alert(eval("wb_msg_"+ "]]><xsl:value-of select="$wb_lang"/><![CDATA[" +"_select_applicant"));
					return;
				}				
				msg.init('enrollment_notify',']]><xsl:value-of select="$itm_id"/><![CDATA[','','','',_app_process_status,ent_id_lst)
			}
						
			
			function getPopupUsrLst(fld_name, id_lst, nm_lst,blank, auto_enroll_ind,isExclude) {	
							tmp_ent_id_lst = id_lst.replace(/~%~/g,"~")
				tmp_ent_nm_lst = nm_lst.replace(/~%~/g,"~")
				app.new_enrollment_exec(document.frmAction,']]><xsl:value-of select="$wb_lang"/><![CDATA[',tmp_ent_id_lst,'doFeedParam2',']]><xsl:value-of select="$parent_itm_id"/><![CDATA[',']]><xsl:value-of select="$itm_id"/><![CDATA[','refresh_popup_usr_search',auto_enroll_ind,isExclude);
			}
						
			function doFeedParam2(){
				param = new Array();
				tmpObj1 = new Array();
				tmpObj2 = new Array();
				tmpObj3 = new Array();
				
				tmpObj1[0] = 'cmd';
				tmpObj1[1] = 'ae_ins_multi_appn';
				param[param.length]=tmpObj1;
				
				tmpObj2[0] = 'ent_id_lst';
				ent_id_lst = tmp_ent_id_lst;
				tmpObj2[1] = ent_id_lst.split("~");
				param[param.length] = tmpObj2;
				
				//tmpObj3[0] = 'ent_nm_lst';
				//ent_nm_lst = tmp_ent_nm_lst;
				//tmpObj3[1] = ent_nm_lst.split("~");
				//param[param.length] = tmpObj3;
				
				return param;
			}
			
			function refresh_popup_usr_search(mylocation){	
				var url, screenLeft, screenTop, openScreenLeft, openScreenTop, curDate, parentUrl, psrh_exist
				curDate = new Date()
				if (!popup_usr_search.closed) {psrh_exist = true;}
				else {psrh_exist = false;}
				if (psrh_exist) {
					url = ''
					url = popup_usr_search.location;
				}else {url = ''; }				

				if(document.all){//Internet Explorer
					screenLeft = window.screenLeft
					screenTop = window.screenTop
					openScreenLeft = screenLeft + 130
					openScreenTop = screenTop + 130
				}else{//Netscape & Others
					screenLeft = window.screenX
					screenTop = window.screenY	
					openScreenLeft = screenLeft + 130
					openScreenTop = screenTop + 130
				}		
				wb_utils_set_cookie('setCookieOpt','0')
							
				wb_utils_set_cookie('enl_psrh_lst', url)
				wb_utils_set_cookie('enl_psrh_lst_opnScnLeft', openScreenLeft)
				wb_utils_set_cookie('enl_psrh_lst_opnScnTop', openScreenTop)
				wb_utils_set_cookie('enl_psrh_lst_srhTime',curDate)
				if (psrh_exist) {
					popup_usr_search.close();
				}
				parentUrl = setUrlParam('enl_psrh_lst_srhTime',curDate,mylocation)	
				window.location =parentUrl
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
				]]><xsl:for-each select="/applyeasy/application_list/application">
					<xsl:variable name="escaped_display_name">
						<xsl:call-template name="escape_js">
							<xsl:with-param name="input_str" select="user/@name"/>
						</xsl:call-template>
					</xsl:variable><![CDATA[
					app_nm[app_nm.length] = ']]><xsl:value-of select="$escaped_display_name"/><![CDATA[';]]></xsl:for-each><![CDATA[
				
			}
			
			function unloadPSrh(closeOpt){
				var setCookieOpt = wb_utils_get_cookie('setCookieOpt')

				if (closeOpt == null || closeOpt == "") {closeOpt = '0';}
				if (setCookieOpt == null || setCookieOpt == "") {setCookieOpt = '1';}
								
				if (window.popup_usr_search) {
					if(document.all) {
						if (closeOpt == '1' && window.popup_usr_search.close) {
							window.popup_usr_search.close();
						}
					} else {
						if (closeOpt == '1' && window.popup_usr_search.alive) {
							window.popup_usr_search.close();
						}					
					}

					if (setCookieOpt == '1') {
						wb_utils_set_cookie('enl_psrh_lst','')
						wb_utils_set_cookie('enl_psrh_lst_opnScrnLeft','')
						wb_utils_set_cookie('enl_psrh_lst_opnScnTop','')
					}
				}
				
				wb_utils_set_cookie('setCookieOpt', '')
			}			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" >
			<form name="frmAction" onsubmit="return false;">
				<input type="hidden" name="itm_title" value="{/applyeasy/item/@title}"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="app_id_lst"/>
				<input type="hidden" name="process_id"/>
				<input type="hidden" name="status_id"/>
				<input type="hidden" name="action_id"/>
				<input type="hidden" name="fr"/>
				<input type="hidden" name="to"/>
				<input type="hidden" name="verb"/>
				<input type="hidden" name="app_upd_timestamp_lst"/>
				<input type="hidden" name="comment_lst"/>
				<input type="hidden" name="content"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="ent_id_lst"/>
				<input type="hidden" name="curAction"/>
				<input type="hidden" name="curActionWarning"/>
				<input type="hidden" name="filter_type"/>
				<input type="hidden" name="filter_value"/>
				<input type="hidden" name="order_by" value="{$cur_order}"/>
				<input type="hidden" name="sort_by" value="{$order_by}"/>
				<xsl:apply-templates/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">111</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main" style="padding-left:0;">		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="item/@run_ind='true'">
						<a href="Javascript:itm_lst.get_item_detail({item/parent/@id})" class="NavLink">
							<xsl:value-of select="item/parent/@title"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_list({item/parent/@id})" class="NavLink">
							<xsl:choose>
								<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
								<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
							</xsl:choose>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<a href="Javascript:itm_lst.get_item_run_detail({item/@id})" class="NavLink">
							<xsl:value-of select="item/@title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="Javascript:itm_lst.get_item_detail({item/@id})" class="NavLink">
							<xsl:value-of select="item/@title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:value-of select="$lab_enrollment_approval"/>
			</xsl:with-param>
		</xsl:call-template>
		<table>
			<tr>				
				<td align="center">
					<xsl:call-template name="check_client">
						<xsl:with-param name="lab_check_client"><xsl:value-of select="$lab_check_popup_window"/></xsl:with-param>
						<xsl:with-param name="img_path" select="$wb_img_path"/>
						<xsl:with-param name="check_item">POPUP</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<xsl:value-of select="$lab_enrollment_workflow"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="item/@app_approval_type = ''">
						<xsl:value-of select="$lab_wf"/>
					</xsl:when>
					<xsl:when test="item/@app_approval_type = 'TADM'">
						<xsl:value-of select="$lab_wf_tadm"/>
					</xsl:when>
					<xsl:when test="item/@app_approval_type = 'DIRECT_SUPERVISE'">
						<xsl:value-of select="$lab_wf_ds"/>
					</xsl:when>
					<xsl:when test="item/@app_approval_type = 'DIRECT_SUPERVISE_SUPERVISE'">
						<xsl:value-of select="$lab_wf_ds_gs"/>
					</xsl:when>
					<xsl:when test="item/@app_approval_type = 'DIRECT_SUPERVISE_SUPERVISE_TADM'">
						<xsl:value-of select="$lab_wf_ds_gs_tadm"/>
					</xsl:when>
					<xsl:when test="item/@app_approval_type = 'DIRECT_SUPERVISE_TADM'">
						<xsl:value-of select="$lab_wf_ds_tadm"/>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:apply-templates select="workflow"/>
		<xsl:apply-templates select="application_list"/>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="workflow">
		<table>
			<tr>
				<td>
					
				</td>
					<td width="70%" align="right"  class="wzb-form-control">
						<table>
							<tr>
								<td align="right" width="90%">
									<div class="wzb-form-search">
										<xsl:variable name="filter_title">
											<xsl:value-of select="$lab_login_id"/>
											<xsl:value-of select="$lab_filter_title"/>
											<xsl:value-of select="$lab_dis_name"/>
										</xsl:variable>
										<input type="text" size="15" name="filter" class="form-control" style="width:130px;" nowrap="nowrap" title="{$filter_title}" value="{$filter_value}"/>
										<input type="button" class="form-submit margin-right4" value="" onclick="app.set_appn_simple_filter('{$itm_id}', document.frmAction)"/>
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_adv_filter"/>
											<xsl:with-param name="wb_gen_btn_href">
											<xsl:choose>
												<xsl:when test="../item/@run_ind='true'">javascript:app.adv_filter_prep('<xsl:value-of select="../item/@id"/>')</xsl:when>
												<xsl:otherwise>javascript:app.adv_filter_prep('<xsl:value-of select="../item/@id"/>')</xsl:otherwise></xsl:choose></xsl:with-param>
										</xsl:call-template>
										<xsl:if test="/applyeasy/has_filter = 'true'">
											<xsl:call-template name="wb_gen_button">
												<xsl:with-param name="wb_gen_btn_name" select="$lab_clear_filter"/>
												<xsl:with-param name="wb_gen_btn_href">javascript:app.appn_clear_filter('<xsl:value-of select="../item/@id"/>')</xsl:with-param>
											</xsl:call-template>	
										</xsl:if>		
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				</table>
				<table>
				<tr>
				<td width="30%" style="text-align:left;" class="wzb-form-label">
						<xsl:value-of select="$lab_please_select"/>
						<xsl:choose>
							<xsl:when test="process/status">
								<select class="wzb-form-select" name="type_sel_frm" onchange="select_type(document.frmAction)">

									<option value="">
										<xsl:value-of select="$lab_all_type"/>
										<xsl:text> (</xsl:text>
										<xsl:value-of select="$total_process_status_cnt"/>
										<xsl:text>)</xsl:text>
									</option>
									<xsl:for-each select="process/status[@name!='_Exit' and  @name != '_Init'  and  @name != 'Class Cancelled']">

										<xsl:if test="(@id != 6 and @id != 5 )">
											<option value="{@id}">
												<xsl:if test="$app_process_status = @name">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												<xsl:choose>
												    <!-- 审批 -->
													<xsl:when test="@id='1' "><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'work_flow_app_status_pending')"/></xsl:when>
													 <!-- 拒绝 -->
													<xsl:when test="@id='2' "><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'work_flow_app_status_wait')"/></xsl:when>
													<xsl:when test="@id='3' "><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'work_flow_app_status_enrol')"/></xsl:when>
													<xsl:when test="@id='4' "><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'work_flow_app_status_declined')"/></xsl:when>
													<xsl:when test="@id='5' "><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'work_flow_app_status_canel')"/></xsl:when>
												</xsl:choose>

												<xsl:variable name="my_name" select="@name"/>
												<xsl:variable name="my_cnt" select="$process_status_cnt_list_root/process_status_cnt[@name = $my_name]/@cnt"/>
												<xsl:variable name="my_cnt_enrolled" select="$process_status_cnt_list_root/process_status_cnt[@id='3']/@cnt"/>
												
												<xsl:choose>
													<xsl:when test="@id='3' and $my_cnt_enrolled &gt; 0">
														<xsl:text>&#160;(</xsl:text>
														<xsl:value-of select="$my_cnt_enrolled"/>
														<xsl:text>)</xsl:text>
													</xsl:when>
													<xsl:when test="$my_cnt">
														<xsl:text>&#160;(</xsl:text>
														<xsl:value-of select="$my_cnt"/>
														<xsl:text>)</xsl:text>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>&#160;(0)</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</option>
										</xsl:if>
									</xsl:for-each>
								</select>
							</xsl:when>
							<xsl:otherwise>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				<td align="right" width="50%"  class="wzb-form-control">
						<script type="text/javascript" language="javascript">
						<![CDATA[
							function _go() {
								var btns = document.getElementsByName("frmSubmitBtn");
								btns[0].disabled = true;
								app.set_appn_simple_filter(']]><xsl:value-of select="../item/@id"/>'<![CDATA[, document.frmAction);
								return false;
							}]]>
						</script>
					
					<xsl:if test=" count(../application_list/application) &gt; 0 and count($workflow_process_root/status[@name = $app_process_status]/action[@id != -1]) != 0">
							<xsl:for-each select="$workflow_process_root/status[@name = $app_process_status]/action[@id != -1]">
								<xsl:if test="access/role/@id = $my_role">
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
										<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="@name"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:change_action(document.frmAction,action_this_status_name<xsl:value-of select="$js_var_suffix"/>,'<xsl:value-of select="$label_name"/>',<xsl:value-of select="@next_status"/>)</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
					</xsl:if>
					
						<xsl:call-template name="wb_gen_button">	
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_notify"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:notify()</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_export"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:app.dl_application_rpt(document.frmAction,app_process_status,'<xsl:value-of select="$itm_id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						</xsl:call-template>
						<!-- remove multiple button-->
						<xsl:if test="$page_variant_root/@hasRemoveAppnBtn = 'true' and ../application_list/@status = ''">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_remove"/>
								<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:app.remove_multi_application(document.frmAction,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
							</xsl:call-template>
						</xsl:if>
					
					<!-- for remove button case , when action id=-1 will be displayed at the end-->
					<xsl:if test=" count(../application_list/application) &gt; 0 and count($workflow_process_root/status[@name = $app_process_status]/action[@id = -1]) &gt; 0">
						
							<xsl:for-each select="$workflow_process_root/status[@name = $app_process_status]/action[@id = -1]">
								<xsl:if test="access/role/@id = $my_role">
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
										<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name" select="@name"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:change_action(document.frmAction,action_this_status_name<xsl:value-of select="$js_var_suffix"/>,'<xsl:value-of select="$label_name"/>',<xsl:value-of select="@next_status"/>)</xsl:with-param>
									</xsl:call-template>
								</xsl:if>
							</xsl:for-each>
						
				</xsl:if>
				
					<xsl:variable name="wb_gen_btn_href">
						<xsl:choose>
							<xsl:when test="../item/@app_approval_type = ''">javascript:new_enrollment_no()</xsl:when>
							<xsl:otherwise>javascript:app.auto_enroll_ind()</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add"/>
						<xsl:with-param name="wb_gen_btn_href" select="$wb_gen_btn_href"/>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_import"/>
						<xsl:with-param name="wb_gen_btn_href" select="$wb_gen_btn_href"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:Batch.Enrol.Import.prep('<xsl:value-of select="../item/@id"/>')</xsl:with-param>
					</xsl:call-template>
				</td>
			
					
			</tr>
		</table>
	</xsl:template>
	<xsl:template name="get_label">
		<xsl:param name="label_name"/>
		<xsl:param name="label_type"/>
		<xsl:variable name="label_usr" select="$profile_attributes/*[name() = $label_name]/label[@xml:lang = $cur_lang]"/>
		<xsl:choose>
			<xsl:when test="$label_usr !=''"><xsl:value-of select="$label_usr"/></xsl:when>
			<xsl:when test="$label_type != '' and $label_type = 'jslabel'"><xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, $label_name)"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$label/label[@name = $label_name]"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="application_list">
		<xsl:choose>
			<xsl:when test="count(application) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="$app_process_status = ''">
								<xsl:value-of select="$lab_no_application"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$lab_no_application_of"/>
								<xsl:text>"</xsl:text>
								<xsl:value-of select="$app_process_status"/>
								<xsl:text>"</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
		
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="1" style="padding-right:0px;padding-left:0px;width:3px" align="left">
							<span style="margin:0;text-align:right;padding-right:0px;">
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt">
										<xsl:value-of select="count(application)"/>
									</xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
									<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
									<xsl:with-param name="frm_name">frmAction</xsl:with-param>
								</xsl:call-template>
							</span>
						</td>
						<xsl:for-each select="$columns/col">
							<xsl:if test="@active='true' and ($integrated_ind != 'true' or @id != 'integrated_lrn')">
								<td>
									<xsl:variable name="s_db_col" select="db_col/@name"/>
									<xsl:choose>
										<xsl:when test="@extra='no'">
											<xsl:choose>
												<xsl:when test="$order_by =$s_db_col">
													<a href="javascript:app.get_application_list(app_process_status,{$itm_id},{$cur_page},{$page_size},'{$s_db_col}','{$sort_order}')" class="TitleText">
														
													<xsl:call-template name="get_label">
														<xsl:with-param name="label_name" select="@label"></xsl:with-param>
														<xsl:with-param name="label_type" select="@label_type"></xsl:with-param>
													</xsl:call-template>
														<xsl:call-template name="wb_sortorder_cursor">
															<xsl:with-param name="img_path">
																<xsl:value-of select="$wb_img_path"/>
															</xsl:with-param>
															<xsl:with-param name="sort_order">
																<xsl:value-of select="$cur_order"/>
															</xsl:with-param>
														</xsl:call-template>
													</a>
												</xsl:when>
												<xsl:otherwise>
													<a href="javascript:app.get_application_list(app_process_status,{$itm_id},{$cur_page},{$page_size},'{$s_db_col}','asc')" class="TitleText">
														<xsl:call-template name="get_label">
															<xsl:with-param name="label_name" select="@label"></xsl:with-param>
															<xsl:with-param name="label_type" select="@label_type"></xsl:with-param>
														</xsl:call-template>
													</a>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<span class="TitleText">
												<xsl:call-template name="get_label">
													<xsl:with-param name="label_name" select="@label"></xsl:with-param>
													<xsl:with-param name="label_type" select="@label_type"></xsl:with-param>
												</xsl:call-template>
											</span>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</xsl:if>
						</xsl:for-each>
					</tr>
					<xsl:for-each select="application">
						<tr>
							<td width="1" style="padding-right:0px;padding-left:0px;width:3px">
								<span style="margin:0;text-align:right;padding-right:0px;">
									<input type="checkbox" value="{@id}" name="app_id" id="{user/@name}"/>
									<input type="hidden" name="app_upd_timestamp_{@id}" value="{@timestamp}"/>
									<input type="hidden" name="ent_id_{@id}" value="{user/@ent_id}"/>
									<input type="hidden" value="{user/@name}" name="display_name_{@id}"/>
								</span>
							</td>
							<xsl:variable name="app_id" select="@id"/>
							<xsl:variable name="app_export_col" select="app_export_col"/>
							<xsl:variable name="app" select="."/>
							<xsl:for-each select="$columns/col">
								<xsl:if test="@active='true' and ($integrated_ind != 'true' or @id != 'integrated_lrn')">
									<xsl:variable name="id_" select="@id"/>
									<xsl:variable name="type_" select="db_col/@type"/>
									<xsl:choose>
										<xsl:when test="@id='usr_id'">
											<td>
												<a class="Text" href="javascript:app.process_application({$app_id},false);set_cur_status(document.frmAction);unloadPSrh('1')">
													<xsl:value-of select="$app_export_col/*[name()=$id_]"/>
												</a>
												<xsl:if test="$app/targeted_lrn_ind != 'true' and $itm_targeted_lrn_cnt>0">
													<sup>
														&#160;<xsl:value-of select="$lab_star"/>
													</sup>
												</xsl:if>
												<xsl:if test="$app/no_show_count and ($app/no_show_count != 0)">
													<sup>
														&#160;<xsl:value-of select="$lab_sharp"/>
															<xsl:value-of select="no_show_count"/>
													</sup>
												</xsl:if>
												<xsl:if test="$app/attended_before='YES'">
													<sup>
														&#160;<xsl:value-of select="$lab_add"/>
													</sup>
												</xsl:if>
											</td>
										</xsl:when>
										<xsl:when test="@id='direct_supervisors'">
											<td>
												<xsl:choose>
													<xsl:when test="$app_export_col/*[name()='direct_supervisors']/direct_supervisor/entity">
														<xsl:for-each select="$app_export_col/*[name()='direct_supervisors']/direct_supervisor/entity[@type = 'USR']">
															<xsl:value-of select="@display_bil"/>
															<xsl:if test="position() != last()">,</xsl:if>
														</xsl:for-each>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>--</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>									
										</xsl:when>
										<xsl:when test="@id='gender'">
											<td>
												<xsl:choose>
													<xsl:when test="$app_export_col/*[name()=$id_]='F'">
														<xsl:value-of select="$lab_gender_f"/>
													</xsl:when>
													<xsl:when test="$app_export_col/*[name()=$id_]='M'">
														<xsl:value-of select="$lab_gender_m"/>
													</xsl:when>														
													<xsl:otherwise>
														<xsl:value-of select="$lab_gender_unspecified"/>
													</xsl:otherwise>
												</xsl:choose>
											</td>			
										</xsl:when>
										<xsl:when test="$type_='timestamp'">
											<td nowrap="nowrap">
												<xsl:variable name="value_" select="$app_export_col/*[name()=$id_]"/>
												<xsl:choose>
													<xsl:when test="$value_ !=''">
														<xsl:call-template name="display_time">
														<xsl:with-param name="my_timestamp">
															<xsl:value-of select="$app_export_col/*[name()=$id_]"/>
														</xsl:with-param>
														</xsl:call-template>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>--</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>
										</xsl:when>
										<xsl:when test="@id='pending_approval_role'">
											<td align="center">
												<xsl:choose>
													<xsl:when test="$app_export_col/pending_approval_role = ''">
														<xsl:value-of select="$lab_na"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:variable name="pending_approval_role" select="$app_export_col/pending_approval_role/text()"/>
														<xsl:value-of select="$label/label[@name = $pending_approval_role]"/>
													</xsl:otherwise>
												</xsl:choose>
											</td>									
										</xsl:when>
										<xsl:when test="contains(@id,'extension')='true'">
											<td align="center">
												<xsl:variable name="value_" select="$app_export_col/*[name()=$id_]"/>
												<xsl:choose>
													<xsl:when test="$value_ !='' and $profile_attributes/*[name() =$id_]/option_list">
														<xsl:value-of select="$profile_attributes/*[name() =$id_]/option_list/option[@id=$value_]/label[@xml:lang=$cur_lang]"/>
													</xsl:when>
													<xsl:when test="$value_ !=''"><xsl:value-of select="$value_"/></xsl:when>
													<xsl:otherwise>
														<xsl:text>--</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>				
										</xsl:when>
										<xsl:when test="@id='role'">
											<td align="center">
												<xsl:choose>
													<xsl:when test="$app_export_col/*[name()='role']/granted_roles">
														<xsl:for-each select="$app_export_col/*[name()='role']/granted_roles/role">
															<xsl:call-template name="get_rol_title"/>
															<xsl:if test="position() != last()">,&#160;</xsl:if>
														</xsl:for-each>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>--</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>	
										</xsl:when>
										<xsl:when test="@id='integrated_lrn'">
											<td>
												<xsl:choose>
													<xsl:when test="count($app/be_integrated_lrn_lst/intg_lrn) > 0">
														<xsl:for-each select="$app/be_integrated_lrn_lst/intg_lrn">
															<a href="Javascript:itm_lst.get_item_detail({@id})" class="Text">
																<xsl:value-of select="text()"/>
															</a>
															<xsl:if test="position() != last()"><br/></xsl:if>
														</xsl:for-each>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>--</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>									
										</xsl:when>
										<xsl:otherwise>
											<td>
												<xsl:choose>
													<xsl:when test="$id_ = 'status'">
														<xsl:choose>
															<xsl:when test="$app_export_col/*[name()=$id_] = 'Enrolled'">
																<xsl:value-of select="$lab_appn_ps_status_enrolled" />
															</xsl:when>
															<xsl:when test="$app_export_col/*[name()=$id_] = 'Pending Approval'">
																<xsl:value-of select="$lab_appn_ps_status_pending_approval" />
															</xsl:when>
															<xsl:when test="$app_export_col/*[name()=$id_] = 'Waitlisted'">
																<xsl:value-of select="$lab_appn_ps_status_waitlisted" />
															</xsl:when>
															<xsl:when test="$app_export_col/*[name()=$id_] = 'Not Approved'">
																<xsl:value-of select="$lab_appn_ps_status_not_approved" />
															</xsl:when>
															<xsl:when test="$app_export_col/*[name()=$id_] = 'Cancelled'">
																<xsl:value-of select="$lab_appn_ps_status_cancelled" />
															</xsl:when>
															<xsl:otherwise>
																<xsl:value-of select="$app_export_col/*[name()=$id_]"/>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:when>
													<xsl:when test="$app_export_col/*[name()=$id_] != ''">
														<xsl:value-of select="$app_export_col/*[name()=$id_]"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text>--</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:if>
							</xsl:for-each>
						</tr>
					</xsl:for-each>
					<tr>
						<table>
						<tr>
						<td colspan="7" align="right">
                            +<span  style="color:gray;"><xsl:value-of select="$lab_attended_before_enrrol"/></span>&#160;
                            <xsl:if test="application/no_show_count">
                                    #<span  style="color:gray;"><xsl:value-of select="$lab_no_show_before_enrrol"/></span>&#160;
                            </xsl:if>
                            <xsl:if test="application/targeted_lrn_ind">
                                <xsl:if test="$itm_targeted_lrn_cnt>0">
                                <xsl:choose>
                                    <xsl:when test="/applyeasy/item/@run_ind = 'true'">
                                    *<span  style="color:gray;"><xsl:value-of select="$lab_non_target_enr_enrrol"/></span>
                                    </xsl:when>
                                    <xsl:otherwise>
                                    *<span  style="color:gray;"><xsl:value-of select="$lab_non_target_lrn_enrrol"/></span>
                                    </xsl:otherwise>
                                </xsl:choose>
                                </xsl:if>
                            </xsl:if>
                        </td>
						</tr>
						</table>
					</tr>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="cur_page_name">app_lst_page</xsl:with-param>
					<xsl:with-param name="page_size_name">app_lst_page_size</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

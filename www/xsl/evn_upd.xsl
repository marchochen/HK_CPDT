<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!--utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/html_linefeed.xsl"/>
	<!-- cust utils -->
	<!-- other -->
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_privilege">CW</xsl:variable>
	<xsl:variable name="mod_id" select="module/@id"/>
	<xsl:variable name="status" select="module/header/@status"/>
	<xsl:variable name="mobile_status" select="module/isMobile"/>
	<xsl:variable name="mod_type" select="module/header/@type"/>
	<xsl:variable name="mod_subtype" select="module/header/@subtype"/>
	<xsl:variable name="title" select="module/header/title"/>
	<xsl:variable name="mod_desc" select="module/header/desc"/>
	<xsl:variable name="timestamp" select="module/@timestamp"/>
	<xsl:variable name="tc_enabled" select="module/tc_enabled"/>
	<xsl:variable name="root_ent_id" select="module/cur_usr/@root_ent_id"/>
	<xsl:variable name="ent_id" select="module/cur_usr/@ent_id"/>
	<xsl:variable name="rol_learner">NLRN_<xsl:value-of select="$root_ent_id"/>
	</xsl:variable>
	<xsl:variable name="attempt_nbr" select="/module/header/@attempt_nbr"/>
	<!-- ===================================================================== -->
	<xsl:template match="/module">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- ===================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">基本資料</xsl:with-param>
			<xsl:with-param name="lab_mod_title">標題</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_status">發佈狀態</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即時</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_date">結束時間</xsl:with-param>
			<xsl:with-param name="lab_publish_date">發佈時間</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_display_option">顯示設置</xsl:with-param>
			<xsl:with-param name="lab_detail_info">詳細資料</xsl:with-param>
			<xsl:with-param name="lab_visible">已發佈</xsl:with-param>
			<xsl:with-param name="lab_invisible">未發佈</xsl:with-param>
			<xsl:with-param name="lab_upd_forum">修改問卷</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_user">發佈對像</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中設置“主要培訓中心”<br/>設置後，在任何指定培訓中心的操作中，將默認的指定爲設定的主要培訓中心</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_has_attempt_msg">問卷已有學員提交，不可修改培訓中心和發佈對像</xsl:with-param>
			<xsl:with-param name="lab_send_moble">發佈至移動端</xsl:with-param>
			<xsl:with-param name="lab_dis_moble">(移動調查問卷只支持選擇題)</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">基本资料</xsl:with-param>
			<xsl:with-param name="lab_mod_title">标题</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_status">发布状态</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即时</xsl:with-param>
			<xsl:with-param name="lab_start_date">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_date">结束时间</xsl:with-param>
			<xsl:with-param name="lab_publish_date">发布时间</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_display_option">显示设置</xsl:with-param>
			<xsl:with-param name="lab_detail_info">详细资料</xsl:with-param>
			<xsl:with-param name="lab_visible">已发布</xsl:with-param>
			<xsl:with-param name="lab_invisible">未发布</xsl:with-param>
			<xsl:with-param name="lab_upd_forum">修改调查问卷</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_user">发布对象</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中设置“主要培训中心”<br/>设置后，在任何指定培训中心的操作中，将默认的指定为设定的主要培训中心</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_has_attempt_msg">问卷已有学员提交，不可修改培训中心和发布对象</xsl:with-param>
			<xsl:with-param name="lab_send_moble">发布至移动端</xsl:with-param>
			<xsl:with-param name="lab_dis_moble">(移动调查问卷只支持选择题)</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_basic_info">Basic information</xsl:with-param>
			<xsl:with-param name="lab_mod_title">Title</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_unlimit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_immediate">Immediate</xsl:with-param>
			<xsl:with-param name="lab_start_date">Available after</xsl:with-param>
			<xsl:with-param name="lab_end_date">Available until</xsl:with-param>
			<xsl:with-param name="lab_publish_date">Publish date</xsl:with-param>
			<xsl:with-param name="lab_moderator">Moderator</xsl:with-param>
			<xsl:with-param name="lab_display_option">Release schedule</xsl:with-param>
			<xsl:with-param name="lab_detail_info">Detail information</xsl:with-param>
			<xsl:with-param name="lab_visible">Published</xsl:with-param>
			<xsl:with-param name="lab_invisible">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_upd_forum">Edit survey</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_user">Target</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center </xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">You can specify “Major Training Center” in “My Preference”. <br/>After that, your major training center will be default selected wherever a selection of training center is needed.</xsl:with-param>
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_has_attempt_msg">Survey has been submitted. Training center and published object cannot be changed.</xsl:with-param>
			<xsl:with-param name="lab_send_moble">Publish to mobile</xsl:with-param>
			<xsl:with-param name="lab_dis_moble">Mobile only supports multiple choice questionnaire</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ===================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_basic_info"/>
		<xsl:param name="lab_mod_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_publish_date"/>
		<xsl:param name="lab_moderator"/>
		<xsl:param name="lab_display_option"/>
		<xsl:param name="lab_detail_info"/>
		<xsl:param name="lab_visible"/>
		<xsl:param name="lab_invisible"/>
		<xsl:param name="lab_upd_forum"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_user"/>
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_def_tc_desc"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_has_attempt_msg"/>
		<xsl:param name="lab_send_moble"/>
		<xsl:param name="lab_dis_moble"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_evaluation.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js" type="text/javascript"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[

			var goldenman = new wbGoldenMan;
			var evlua = new wbEvaluation;
			usr = new wbUserGroup;
			wb_utils_set_cookie('mod_type',']]><xsl:value-of select="$mod_subtype"/><![CDATA[')
			
			$(function(){ 
				ins_default_date();
				wb_utils_gen_form_focus(document.frmXml);
				setModifiableOfTcAndObject();
			}); 
			
			function ins_default_date(){
				frmXml.start_hour.value = "00"
				frmXml.start_min.value = "00"
					
				frmXml.end_hour.value = "23"
				frmXml.end_min.value = "59"
			}
			
			function setModifiableOfTcAndObject() {
				 var attempt_nbr = ]]><xsl:value-of select="$attempt_nbr"/><![CDATA[;
				 if(attempt_nbr != '' && Number(attempt_nbr) > 0) {
					 //training center
					 document.frmXml.tcr_id_lst.disabled = true;
					 document.frmXml.genaddtcr_id_lst.disabled = true;
					 document.frmXml.genremovetcr_id_lst.disabled = true;
					 //published object
					 document.frmXml.usr_id_lst.disabled = true;
					 document.frmXml.genaddusr_id_lst.disabled = true;
					 document.frmXml.gensearchusr_id_lst.disabled = true;
					 document.frmXml.genremoveusr_id_lst.disabled = true;
				 }
			}
			
			//add publish object by search	
			function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
				ent_id_lst(usr_argv);
			}
			function ent_id_lst(){
				var args = ent_id_lst.arguments;
				AddTreeOption(document.frmXml.usr_id_lst,0,args,'col')
			}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" >
			<form name="frmXml" enctype="multipart/form-data">
				<input type="hidden" name="mod_is_public" value="TRUE"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_EVN_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_EVN_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_upd_forum"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_basic_info"/>
				</xsl:call-template>
				<table>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_type"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="$lab_eval"/>
						</td>
					</tr>
					<xsl:if test="display/option/general/@title = 'true'">
						<tr>
							<td class="wzb-form-label">
								<span class="wzb-form-star">*</span>
								<xsl:value-of select="$lab_mod_title"/>：
							</td>
							<td class="wzb-form-control">
								<input size="20" style="width:300px;" name="mod_title" type="text" maxlength="120" class="wzb-inputText"/>
								<xsl:variable name="escaped_mod_title">
									<xsl:call-template name="escape_js">
										<xsl:with-param name="input_str">
											<xsl:value-of select="$title"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:variable>
								<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
								document.frmXml.mod_title.value="]]><xsl:value-of select="$escaped_mod_title"/><![CDATA[";
							]]></SCRIPT>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="display/option/general/@desc = 'true'">
						<tr>
							<td class="wzb-form-label" valign="top">
								<span class="wzb-form-star">*</span>
								<xsl:value-of select="$lab_desc"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:variable name="escaped_mod_desc">
									<xsl:call-template name="escape_js">
										<xsl:with-param name="input_str">
											<xsl:value-of select="$mod_desc"/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:variable>
								<textarea class="wzb-inputTextArea" name="mod_desc" style="width:300px;" rows="4" maxlength="500">
									<xsl:value-of select="$mod_desc"/>
								</textarea>
							</td>
						</tr>
						<input name="lab_mod_desc" type="hidden" value="{$lab_desc}"/>
					</xsl:if>
				</table>
				<xsl:if test="display/option/datetime/@eff_end = 'true' or display/option/general/@status = 'true' or display/option/datetime/@eff_start = 'true'">
					<xsl:call-template name="wb_ui_head">
						<xsl:with-param name="text">
							<xsl:value-of select="$lab_display_option"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_ui_line"/>
					<table>
						<!-- tc select -->
						<xsl:if test="$tc_enabled='true' and $mod_subtype = 'EVN'">
							<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
								//confirm_msg & confirm_function for wb_goldenman(tree_type = training_center)
								var confirm_msg = wb_msg_itp_reset_dependent_1 + ']]><xsl:value-of select="$lab_user"/><![CDATA[' + wb_msg_itp_reset_dependent_2;
								function resetForTcAlter() {
									RemoveAllOptions(document.frmXml.usr_id_lst);
									RemoveSingleOption(document.frmXml.tcr_id_lst_single, document.frmXml.tcr_id_lst);
								}
								function removeTcrDependant() {
									if(!confirm(confirm_msg)) {
										return false;
									}
									//remove user list
									RemoveAllOptions(document.frmXml.usr_id_lst);
									//remove training center fields
									RemoveSingleOption(document.frmXml.tcr_id_lst_single, document.frmXml.tcr_id_lst);
								}
								function openUserAndUserGroupTreeTcEnabled(tcr_id){
									if(tcr_id === undefined || tcr_id === null || tcr_id === '' || tcr_id == '0'){
										alert(wb_msg_tc);
									} else {
										openUserAndUserGroupTree(tcr_id)
									}
								}
								function openUserAndUserGroupTree(tcr_id){
									goldenman.opentree('user_group_and_user',1,'usr_id_lst','','0','','','1','0', '0', '0', '0','1', '0', '','',tcr_id, 1);
								}
								function checkedSelectedTcr() {
									var tcr_id = document.frmXml.tcr_id_lst.options[0].value;
									if(tcr_id === undefined || tcr_id === null || tcr_id === ''|| tcr_id == '0'){
										alert(wb_msg_tc);
										return false;
									} else {
										usr.search.popup_search_prep ('usr_id_lst','','1','0','', '','0','0','','','','1',tcr_id,]]>'<xsl:value-of select="$rol_learner"/><![CDATA[')
									}
								}
								]]></script>
							<xsl:if test="$attempt_nbr != '' and $attempt_nbr > 0">
								<tr>
									<td class="wzb-form-label">
									</td>
									<td class="wzb-form-control">
										<span class="Text">
											<xsl:value-of select="$lab_has_attempt_msg"/>
										</span>
									</td>
								</tr>
							</xsl:if>
							<tr>
								<td class="wzb-form-label">
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_tc"/>：
								</td>
								<xsl:variable name="cur_tcr_id">
									<xsl:choose>
										<xsl:when test="/module/training_center">
											<xsl:value-of select="/module/training_center/@id"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="//default_training_center/@id"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:variable name="cur_tcr_title">
									<xsl:choose>
										<xsl:when test="/module/training_center">
											<xsl:value-of select="/module/training_center/title"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="//default_training_center/title"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<td class="wzb-form-control">
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="field_name">tcr_id_lst</xsl:with-param>
										<xsl:with-param name="name">tcr_id_lst</xsl:with-param>
										<xsl:with-param name="box_size">1</xsl:with-param>
										<xsl:with-param name="tree_type">training_center</xsl:with-param>
										<xsl:with-param name="select_type">2</xsl:with-param>
										<xsl:with-param name="pick_leave">0</xsl:with-param>
										<xsl:with-param name="pick_root">0</xsl:with-param>
										<xsl:with-param name="single_option_value">
											<xsl:value-of select="$cur_tcr_id"/>
										</xsl:with-param>
										<xsl:with-param name="single_option_text">
											<xsl:value-of select="$cur_tcr_title"/>
										</xsl:with-param>
										<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>
										<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
										<xsl:with-param name="remove_function">removeTcrDependant()</xsl:with-param>
									</xsl:call-template>
								</td>
								<input name="mtc_tcr_id" type="hidden"/>
							</tr>
						</xsl:if>
						<tr>
							<input name="lab_user" type="hidden" value="{$lab_user}"/>
							<td class="wzb-form-label" valign="top">
								<span class="wzb-form-star">*</span>
								<xsl:value-of select="$lab_user"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="width">320</xsl:with-param>
									<xsl:with-param name="field_name">usr_id_lst</xsl:with-param>
									<xsl:with-param name="name">usr_id_lst</xsl:with-param>
									<xsl:with-param name="tree_type">user_group_and_user</xsl:with-param>
									<xsl:with-param name="select_type">1</xsl:with-param>
									<xsl:with-param name="box_size">5</xsl:with-param>
									<xsl:with-param name="pick_leave">0</xsl:with-param>
									<xsl:with-param name="option_list">
										<xsl:apply-templates select="/module/eval_target_lst"/>
									</xsl:with-param>
									<xsl:with-param name="search">true</xsl:with-param>
									<xsl:with-param name="search_function">
										<xsl:choose>
											<xsl:when test="$tc_enabled='true'">javascript:checkedSelectedTcr()</xsl:when>
											<xsl:otherwise>javascript:usr.search.popup_search_prep ('usr_id_lst','','1','0','', '','0','0','','','','0','0','<xsl:value-of select="$rol_learner"/>')</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
									<xsl:with-param name="ftn_ext_id"/>
									<xsl:with-param name="add_function">
										<xsl:choose>
											<xsl:when test="$tc_enabled='true'">openUserAndUserGroupTreeTcEnabled(document.frmXml.tcr_id_lst.options[0].value)</xsl:when>
											<xsl:otherwise>openUserAndUserGroupTree()</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
						<xsl:if test="display/option/general/@status = 'true'">
							<tr>
								<td class="wzb-form-label" valign="top">
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_send_moble"/>：
								</td>
								<td class="wzb-form-control">
									<input type="radio" name="mobile_status" value="true">
										<xsl:if test="$mobile_status='true'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_yes"/>
									<br/>
									
									<input type="radio" name="mobile_status" value="false">
										<xsl:if test="$mobile_status='false'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_no"/>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label" valign="top">
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_status"/>：
								</td>
								<td class="wzb-form-control">
									<input type="radio" name="mod_status_ind" value="ON">
										<xsl:if test="$status='ON'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_visible"/>
									<br/>
									<input type="radio" name="mod_status_ind" value="OFF">
										<xsl:if test="$status='OFF'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<xsl:value-of select="$lab_invisible"/>
									<input type="hidden" name="mod_status"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="display/option/datetime/@eff_start = 'true' and display/option/datetime/@eff_end = 'true'">
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_publish_date"/>：
								</td>
								<td class="wzb-form-control">
									<input type="hidden" name="lab_publish_date" value="{$lab_publish_date}"/>
									<xsl:value-of select="$lab_from"/>
									<xsl:text>&#160;</xsl:text>
									<xsl:variable name="eff_start_date" select="header/@eff_start_datetime"/>
									<xsl:call-template name="display_form_input_time">
									<xsl:with-param name="fld_name">start</xsl:with-param>
									<xsl:with-param name="timestamp">
										<xsl:value-of select="$eff_start_date"/>
									</xsl:with-param>
									<xsl:with-param name="hidden_fld_name">start</xsl:with-param>
								</xsl:call-template>
								<input name="mod_eff_start_datetime" type="hidden" value=""/>
								<xsl:text>&#160;</xsl:text>
								<xsl:value-of select="$lab_to"/>
								<xsl:text>&#160;</xsl:text>
								<xsl:variable name="eff_end_date" select="header/@eff_end_datetime"/>
								<xsl:call-template name="display_form_input_time">
									<xsl:with-param name="fld_name">end</xsl:with-param>
									<xsl:with-param name="timestamp">
										<xsl:value-of select="$eff_end_date"/>
									</xsl:with-param>
									<xsl:with-param name="hidden_fld_name">mod_eff_end_datetime</xsl:with-param>
								</xsl:call-template>
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td class="wzb-form-label">&#160;</td>
							<td class="wzb-form-control wzb-ui-desc-text">
								<span class="wzb-form-star">*</span>
								<xsl:value-of select="$lab_all_info_required"/>
							</td>
						</tr>
					</table>
				</xsl:if>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:evlua.upd_evn_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">wb_utils_nav_go('FTN_AMD_EVN_MAIN', '<xsl:value-of select="$ent_id" />', '<xsl:value-of select="$wb_lang"/>')<!-- javascript:history.back() --></xsl:with-param>
					</xsl:call-template>
				</div>
				<xsl:if test="count(header/template_list/template) = 1">
					<input type="hidden" name="tpl_name" value="{header/template_list/@cur_tpl}"/>
				</xsl:if>
				<input type="hidden" name="cmd" value="upd_mod"/>
				<input type="hidden" name="mod_id" value="{$mod_id}"/>
				<input type="hidden" name="mod_type" value="{$mod_type}"/>
				<input type="hidden" name="mod_subtype" value="{$mod_subtype}"/>
				<input type="hidden" name="mod_privilege" value="{$mod_privilege}"/>
				<input type="hidden" name="mod_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="usr_ent_id_lst" value=""/>
				<input type="hidden" name="mod_max_usr_attempt" value="1"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="eval_target_lst/target">
		<option value="{@ent_id}">
			<xsl:value-of select="text()"/>
		</option>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

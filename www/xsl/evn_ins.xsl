<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<!-- cust utils -->
	<xsl:import href="share/wb_module_type_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="my_privilege">CW</xsl:variable>
	<xsl:variable name="tc_enabled" select="//template_list/tc_enabled"/>
	<xsl:variable name="root_ent_id" select="//template_list/cur_usr/@root_ent_id"/>
	<xsl:variable name="ent_id" select="//template_list/cur_usr/@ent_id"/>
	<xsl:variable name="rol_learner">NLRN_<xsl:value-of select="$root_ent_id"/></xsl:variable>
	<!-- =================================================================== -->
	<xsl:template match="/template_list">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_ins_forum">新增問卷</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即時</xsl:with-param>
			<xsl:with-param name="lab_start_date">開始時間</xsl:with-param>
			<xsl:with-param name="lab_end_date">結束時間</xsl:with-param>
			<xsl:with-param name="lab_publish_date">發佈時間</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_status">發佈狀態</xsl:with-param>
			<xsl:with-param name="lab_visible">已發佈</xsl:with-param>
			<xsl:with-param name="lab_invisible">未發佈</xsl:with-param>
			<xsl:with-param name="lab_basic_info">基本資料</xsl:with-param>
			<xsl:with-param name="lab_display_option">顯示設置</xsl:with-param>
			<xsl:with-param name="lab_detail_info">詳細資料</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有學員</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">只有這些學員</xsl:with-param>
			<xsl:with-param name="lab_user">發佈對像</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中設置“主要培訓中心”<br/>設置後，在任何指定培訓中心的操作中，將默認的指定爲設定的主要培訓中心</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_send_moble">發佈至移動端</xsl:with-param>
			<xsl:with-param name="lab_dis_moble">(移動調查問卷只支持選擇題)</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_title_desc">注：标题输入的文字长度不得超过50</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_ins_forum">添加调查问卷</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_unlimit">不限</xsl:with-param>
			<xsl:with-param name="lab_immediate">即时</xsl:with-param>
			<xsl:with-param name="lab_start_date">开始时间</xsl:with-param>
			<xsl:with-param name="lab_end_date">结束时间</xsl:with-param>
			<xsl:with-param name="lab_publish_date">发布时间</xsl:with-param>
			<xsl:with-param name="lab_moderator">主持人</xsl:with-param>
			<xsl:with-param name="lab_status">发布状态</xsl:with-param>
			<xsl:with-param name="lab_visible">已发布</xsl:with-param>
			<xsl:with-param name="lab_invisible">未发布</xsl:with-param>
			<xsl:with-param name="lab_basic_info">基本资料</xsl:with-param>
			<xsl:with-param name="lab_display_option">显示设置</xsl:with-param>
			<xsl:with-param name="lab_detail_info">详细资料</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">所有学员</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">指定学员</xsl:with-param>
			<xsl:with-param name="lab_user">发布对象</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">你可以在“我的喜好”中设置“主要培训中心”<br/>设置后，在任何指定培训中心的操作中，将默认的指定为设定的主要培训中心</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_send_moble">发布至移动端</xsl:with-param>
			<xsl:with-param name="lab_dis_moble">(移动调查问卷只支持选择题)</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_title_desc">注：标题输入的文字长度不得超过50</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_ins_forum">Add survey</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_unlimit">Unlimited</xsl:with-param>
			<xsl:with-param name="lab_immediate">Immediate</xsl:with-param>
			<xsl:with-param name="lab_start_date">Available after</xsl:with-param>
			<xsl:with-param name="lab_end_date">Available until</xsl:with-param>
			<xsl:with-param name="lab_publish_date">Publish date</xsl:with-param>
			<xsl:with-param name="lab_moderator">Moderator</xsl:with-param>
			<xsl:with-param name="lab_status">Published status</xsl:with-param>
			<xsl:with-param name="lab_visible">Published</xsl:with-param>
			<xsl:with-param name="lab_invisible">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_basic_info">Basic information</xsl:with-param>
			<xsl:with-param name="lab_display_option">Release schedule</xsl:with-param>
			<xsl:with-param name="lab_detail_info">Detail information</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_by_all_user">All learners under my primary responsible user group</xsl:with-param>
			<xsl:with-param name="lab_by_selected_user">Only these learners under my primary responsible user group</xsl:with-param>
			<xsl:with-param name="lab_user">Target</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center</xsl:with-param>
			<xsl:with-param name="lab_def_tc_desc">You can specify "Major Training Center" in "My Preference". <br/>After that, your major training center will be default selected wherever a selection of training center is needed.</xsl:with-param>	
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_send_moble">Publish to mobile</xsl:with-param>
			<xsl:with-param name="lab_dis_moble">Mobile only supports multiple choice questionnaire</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_title_desc">注：标题输入的文字长度不得超过50</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_ins_forum"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_unlimit"/>
		<xsl:param name="lab_immediate"/>
		<xsl:param name="lab_start_date"/>
		<xsl:param name="lab_end_date"/>
		<xsl:param name="lab_publish_date"/>
		<xsl:param name="lab_moderator"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_visible"/>
		<xsl:param name="lab_invisible"/>
		<xsl:param name="lab_basic_info"/>
		<xsl:param name="lab_display_option"/>
		<xsl:param name="lab_detail_info"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_by_all_user"/>
		<xsl:param name="lab_by_selected_user"/>
		<xsl:param name="lab_user"/>	
		<xsl:param name="lab_tc"/>
		<xsl:param name="lab_def_tc_desc"/>	
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_send_moble"/>
		<xsl:param name="lab_dis_moble"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_title_desc"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_forum.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_evaluation.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js" type="text/javascript"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
		var evlua = new wbEvaluation;
		var goldenman = new wbGoldenMan;
		wb_utils_set_cookie('url_add_mod',window.location.href)
		tpl_subtype = getUrlParam("tpl_subtype") 
		wb_utils_set_cookie('mod_type',tpl_subtype)
		
		usr = new wbUserGroup;
			function usr_change(frm, obj){
					if(obj.value == 0){
						var pos = true;
						var neg = false;
					}else if (obj.value == 1){
						var pos = false;
						var neg = true;
					}else{
						var pos =true;
						var neg = true;
					}
					if(frm.usr_ent_id_lst.type == 'select-multiple'){
						frm.usr_ent_id_lst.disabled = pos ;
						if(frm.genaddusr_ent_id_lst){
							frm.genaddusr_ent_id_lst.disabled = pos ;
						}
						if(frm.genremoveusr_ent_id_lst){
							frm.genremoveusr_ent_id_lst.disabled = pos ;
						}		
						if(frm.gensearchusr_ent_id_lst){
							frm.gensearchusr_ent_id_lst.disabled = pos ;
						}		
						if(pos == true){
							frm.usr_ent_id_lst.options.length = 0
						}
					}
			}
						
		function ins_default_date(){
			var frm = document.frmXml
			//Get the server current time
			str = "]]><xsl:value-of select="//cur_time/."/><![CDATA["
			cur_day = str.substring((str.lastIndexOf('-') + 1), str.indexOf(' '))
			cur_month = str.substring((str.indexOf('-') + 1), str.lastIndexOf('-'))
			cur_year = str.substring(0, str.indexOf('-'))
			
			if(frm.usr_sel_all_user && frm.usr_sel_all_user[0].checked){
				usr_change(frm,frm.usr_sel_all_user[0])
			}else if(frm.usr_sel_all_user && frm.usr_sel_all_user[1].checked){
				usr_change(frm,frm.usr_sel_all_user[1])
			}
		
			frm.start_mm.value = cur_month
			frm.start_yy.value = cur_year
			frm.start_dd.value = cur_day
			frm.start_hour.value = "00"
			frm.start_min.value = "00"
		}
		
		//add publish object by search	
		function getPopupUsrLst(fld_name,id_lst,nm_lst, usr_argv){
			ent_id_lst(usr_argv);
		}
		function ent_id_lst(){
			var args = ent_id_lst.arguments;
			AddTreeOption(document.frmXml.usr_id_lst,0,args,'col')
		}
		
		
		window.onload = function(){
			ins_default_date();
			;wb_utils_gen_form_focus(document.frmXml)
		};
		
		
	]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="ins_default_date();wb_utils_gen_form_focus(document.frmXml)">
			<form enctype="multipart/form-data" name="frmXml">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_EVN_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_EVN_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_ins_forum"/>
					</xsl:with-param>
				</xsl:call-template>
				<!-- 1 -->
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_basic_info"/>
					</xsl:with-param>
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
								<xsl:value-of select="$lab_title"/>：
							</td>
							<td class="wzb-form-control">
								<input size="35" name="mod_title" type="text" style="width:300px;" maxlength="120" class="wzb-inputText"/>
								<!-- <span class="wzb-ui-desc-text margin-left15">
									<xsl:value-of select="$lab_title_desc"/>
								</span> -->
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
								<textarea class="wzb-inputTextArea" name="mod_desc" style="width:300px;" rows="4" maxlength="500"/>
							</td>
							<input name="lab_mod_desc" type="hidden" value="{$lab_desc}"/>
						</tr>
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
						<xsl:if test="$tc_enabled='true'">
							<script LANGUAGE="JavaScript" TYPE="text/javascript">
								<![CDATA[
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
										return false;
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
								]]>
							</script>
							<tr>
								<td class="wzb-form-label">
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_tc"/>：
								</td>
								<xsl:variable name="cur_tcr_id">
									<xsl:choose>
										<xsl:when test="not(//default_training_center)"><xsl:value-of select="//training_center/@id"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="//default_training_center/@id"/></xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:variable name="cur_tcr_title">
									<xsl:choose>
										<xsl:when test="not(//default_training_center)"><xsl:value-of select="//training_center/title"/></xsl:when>
										<xsl:otherwise><xsl:value-of select="//default_training_center/title"/></xsl:otherwise>
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
										<xsl:with-param name="single_option_value"><xsl:value-of select="$cur_tcr_id"/></xsl:with-param>
										<xsl:with-param name="single_option_text"><xsl:value-of select="$cur_tcr_title"/></xsl:with-param>
										<xsl:with-param name="confirm_function">resetForTcAlter</xsl:with-param>										
										<xsl:with-param name="confirm_msg">confirm_msg</xsl:with-param>
										<xsl:with-param name="remove_function">removeTcrDependant()</xsl:with-param>
									</xsl:call-template>
								</td>
								<input name="mtc_tcr_id" type="hidden" value=""/>
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
									<xsl:with-param name="pick_root">0</xsl:with-param>
									<xsl:with-param name="search">true</xsl:with-param>
									<xsl:with-param name="search_function">
										<xsl:choose>
											<xsl:when test="$tc_enabled='true'">javascript:checkedSelectedTcr()</xsl:when>
											<xsl:otherwise>javascript:usr.search.popup_search_prep ('usr_id_lst','','1','0','', '','0','0','','','','0','0','<xsl:value-of select="$rol_learner"/>')</xsl:otherwise>
										</xsl:choose>
									</xsl:with-param>
									<xsl:with-param name="ftn_ext_id"></xsl:with-param>
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
									</input>
									<xsl:value-of select="$lab_yes"/>
									<br/>
									
									<input type="radio" name="mobile_status" value="false">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</input>
									<xsl:value-of select="$lab_no"/>
									<input type="hidden" name="mobile_ind"/>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label" valign="top">
									<span class="wzb-form-star">*</span>
									<xsl:value-of select="$lab_status"/>：
								</td>
								<td class="wzb-form-control">
									<input type="radio" name="mod_status" value="ON"/>
									<xsl:value-of select="$lab_visible"/>
									<br/>
									<input type="radio" name="mod_status" value="OFF" checked="checked"/>
									<xsl:value-of select="$lab_invisible"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="display/option/datetime/@eff_start = 'true' and display/option/datetime/@eff_end = 'true'">
							<tr>
								<td class="wzb-form-label">
									<span class="wzb-form-star">*</span>
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
										<xsl:with-param name="focus_rad_btn_name">lab_publish_date</xsl:with-param>
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
										<xsl:with-param name="focus_rad_btn_name">lab_publish_date</xsl:with-param>
									</xsl:call-template>
									<input name="mod_eff_end_datetime" type="hidden" value=""/>
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
						<xsl:with-param name="wb_gen_btn_href">Javascript:evlua.ins_evn_exec(frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_nav_go('FTN_AMD_EVN_MAIN', '<xsl:value-of select="$ent_id"/>', '<xsl:value-of select="$wb_lang"/>')<!-- javascript:history.back(); --></xsl:with-param>
					</xsl:call-template>
				</div>
				<input type="hidden" name="tpl_name" value="Evaluation Template"/>
				<input type="hidden" name="mod_is_public" value="TRUE"/>
				<input type="hidden" name="mod_max_usr_attempt" value="1"/>
				<input type="hidden" name="cmd" value="ins_mod"/>
				<input type="hidden" name="mod_privilege" value="{$my_privilege}"/>
				<input type="hidden" name="mod_type"/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="mod_subtype" value="EVN"/>
				<input type="hidden" name="usr_ent_id_lst" value=""/>
			</form>
		</body>
	</xsl:template>
	<!--==================================================================-->
</xsl:stylesheet>

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
	<!-- cust utils -->
	<xsl:output indent="yes"/>
	<xsl:variable name="mod_privilege">CW</xsl:variable>
	<!--
		<xsl:choose>
			<xsl:when test="forum/meta/cur_usr/@root_display = $root_cw">CW</xsl:when>
			<xsl:otherwise>AUTHOR</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	-->
	<xsl:variable name="mod_id" select="module/@id"/>
	<xsl:variable name="status" select="module/header/@status"/>
	<xsl:variable name="mod_type" select="module/header/@type"/>
	<xsl:variable name="mod_subtype" select="module/header/@subtype"/>
	<xsl:variable name="title" select="module/header/title"/>
	<xsl:variable name="timestamp" select="module/@timestamp"/>
	<xsl:variable name="tc_enabled" select="/module/tc_enabled"/>
	<xsl:variable name="root_ent_id" select="/module/cur_usr/@root_ent_id"/>
	<xsl:variable name="ent_id" select="/template_list/cur_usr/@ent_id"/>
	<xsl:variable name="rol_learner">NLRN_<xsl:value-of select="$root_ent_id"/></xsl:variable>
	<!-- ===================================================================== -->
	<xsl:template match="/module">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- ===================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_mod_title">標題</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_active">已發佈</xsl:with-param>
			<xsl:with-param name="lab_inactive">未發佈</xsl:with-param>
			<xsl:with-param name="lab_upd_forum">修改課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_tc">培訓中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_mod_title">标题</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_active">已发布</xsl:with-param>
			<xsl:with-param name="lab_inactive">未发布</xsl:with-param>
			<xsl:with-param name="lab_upd_forum">修改课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_tc">培训中心</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_mod_title">Title</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_active">Published</xsl:with-param>
			<xsl:with-param name="lab_inactive">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_upd_forum">Edit evaluation</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_tc">Training center</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ===================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_mod_title"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_active"/>
		<xsl:param name="lab_inactive"/>
		<xsl:param name="lab_upd_forum"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_tc"/>
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
			<script language="JavaScript" src="{$wb_js_path}wb_cos_evaluation.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_goldenman.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			var goldenman = new wbGoldenMan;
			var coseval = new wbCosEvaluation;
			wb_utils_set_cookie('mod_type',']]><xsl:value-of select="$mod_subtype"/><![CDATA[')
			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="wb_utils_gen_form_focus(document.frmXml)">
			<form name="frmXml" enctype="multipart/form-data" onsubmit="coseval.upd_cos_evn_exec(document.frmXml,'{$wb_lang}');return false;">
				
				<xsl:call-template name="wb_ui_hdr">				
					<xsl:with-param name="belong_module">FTN_AMD_COS_EVN_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_COS_EVN_MAIN</xsl:with-param>
				</xsl:call-template> 
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_upd_forum"/>
				</xsl:call-template>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
					<!-- 培训中心 -->
					<xsl:if test="$tc_enabled='true'">
						<script LANGUAGE="JavaScript" TYPE="text/javascript">
							<![CDATA[
							function resetForTcAlter() {
								RemoveAllOptions(document.frmXml.usr_id_lst);
								RemoveSingleOption(document.frmXml.tcr_id_lst_single, document.frmXml.tcr_id_lst);
							}
							function removeTcrDependant() {
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
								goldenman.opentree('user_group_and_user',1,'usr_id_lst','','0','','','1','0', '0', '0', '0','1', '0', 'EVN_LIST','',tcr_id, 1);
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
							<td width="20%" align="right" class="wzb-form-label">
								<span class="wzb-form-star">*</span><span class="TitleText"><xsl:value-of select="$lab_mod_title"/>：</span>
							</td>
							<td width="80%" class="wzb-form-control">
								<span class="Text"><input size="20" style="width:300px;" name="mod_title" type="text" maxlength="255" class="wzb-inputText"/><xsl:variable name="escaped_mod_title"><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$title"/></xsl:with-param></xsl:call-template></xsl:variable><SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
										document.frmXml.mod_title.value="]]><xsl:value-of select="$escaped_mod_title"/><![CDATA[";
									]]></SCRIPT></span>
							</td>
						</tr>
						<tr>
							<td align="right" class="wzb-form-label">
								<span class="TitleText">
									<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tc"/>：</span>
							</td>
							<xsl:variable name="cur_tcr_id">
								<xsl:choose>
									<xsl:when test="not(//default_training_center)"><xsl:value-of select="//mod_tcr/@id"/></xsl:when>
									<xsl:otherwise><xsl:value-of select="//default_training_center/@id"/></xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="cur_tcr_title">
								<xsl:choose>
									<xsl:when test="not(//default_training_center)"><xsl:value-of select="//mod_tcr/@title"/></xsl:when>
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
							<input name="mod_tcr_id" type="hidden" value=""/>
						</tr>
					</xsl:if>
					<tr>
						<td width="20%" align="right" valign="top"  class="wzb-form-label">
							<span class="wzb-form-star">*</span><span class="TitleText"><xsl:value-of select="$lab_status"/>：</span>
						</td>
						<td width="80%" colspan="2" class="wzb-form-control">
							<span class="Text"><label for="rdo_mod_status_ind_1"><input id="rdo_mod_status_ind_1" type="radio" name="mod_status_ind" value="ON"><xsl:if test="$status='ON'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if></input><xsl:value-of select="$lab_active"/></label><br/><label for="rdo_mod_status_ind_2"><input id="rdo_mod_status_ind_2" type="radio" name="mod_status_ind" value="OFF"><xsl:if test="$status='OFF'"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if></input><xsl:value-of select="$lab_inactive"/></label><input type="hidden" name="mod_status"/></span>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
						</td>
						<td class="wzb-ui-module-text">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
						</td>
				   </tr>
				</table>
				<div class="wzb-bar">
					<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
						<tr>
							<td height="19" align="center">
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:coseval.upd_cos_evn_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								<xsl:call-template name="wb_gen_form_button">
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_nav_go('FTN_AMD_COS_EVN_MAIN', '<xsl:value-of select="$ent_id"/>', '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
						
					</table>
				</div>
				<xsl:call-template name="wb_ui_footer"/>
				<xsl:if test="count(header/template_list/template) = 1">
					<input type="hidden" name="tpl_name" value="Survey Template"/>
				</xsl:if>
				<input type="hidden" name="mod_is_public" value="TRUE"/>
				<input type="hidden" name="cmd" value="upd_mod"/>
				<input type="hidden" name="mod_id" value="{$mod_id}"/>
				<input type="hidden" name="mod_type" value="{$mod_type}"/>
				<input type="hidden" name="mod_subtype" value="{$mod_subtype}"/>
				<input type="hidden" name="mod_privilege" value="{$mod_privilege}"/>
				<input type="hidden" name="mod_timestamp" value="{$timestamp}"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="mod_max_usr_attempt" value="1"/>
				<input type="hidden" name="mod_eff_start_datetime" value=""/>
				<input type="hidden" name="mod_eff_end_datetime" value=""/>
			</form>
		</body>
	</xsl:template>
	<!-- ===================================================================== -->
</xsl:stylesheet>

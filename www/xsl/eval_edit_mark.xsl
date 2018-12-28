<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
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
	<xsl:import href="utils/wb_ui_sub_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="lrn_ent_id" select="/evalmanagement/eval_item/@lrn_ent_id"/>
	<xsl:variable name="cmt_id" select="/evalmanagement/eval_item/@cmt_id"/>
	<xsl:variable name="app_id" select="/evalmanagement/eval_item/@app_id"/>
	<xsl:variable name="cmt_tkh_id" select="/evalmanagement/eval_item/@cmt_tkh_id"/>
	<xsl:variable name="cmt_max_score" select="round(/evalmanagement/eval_item/@cmt_max_score)"/>
	<xsl:variable name="cmt_score" select="round(/evalmanagement/eval_item/@cmt_score)"/>
	<!-- 计分记录 -->
	<xsl:variable name="label_core_training_management_241" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_241')"/>
	 
	<!-- =============================================================== -->
	<xsl:template match="/evalmanagement">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_edit_mark">成績編輯</xsl:with-param>
			<xsl:with-param name="lab_input_score">分數</xsl:with-param>
			<xsl:with-param name="lab_lrn">學員</xsl:with-param>
			<xsl:with-param name="edit_score">修改分數</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_edit_mark">成绩编辑</xsl:with-param>		
			<xsl:with-param name="lab_input_score">分数</xsl:with-param>
			<xsl:with-param name="lab_lrn">学员</xsl:with-param>
			<xsl:with-param name="edit_score">修改分数</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_edit_mark">Edit mark</xsl:with-param>
			<xsl:with-param name="lab_input_score">Score</xsl:with-param>
			<xsl:with-param name="lab_lrn">Learner</xsl:with-param>
			<xsl:with-param name="edit_score">Edit score</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_edit_mark"/>
		<xsl:param name="lab_input_score"/>
		<xsl:param name="lab_lrn"/>
		<xsl:param name="edit_score"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_evalmgt.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
				evalmgt = new wbEvalManagement;
				itm_lst = new wbItem;
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onLoad="feedParam(frm);frm.cmt_score.focus();">
			<form name="frm" method="get" onsubmit="javascript:evalmgt.upd_mark(frm,'{$wb_lang}');return false;">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="lrn_ent_id" value="{$lrn_ent_id}"/>
				<input type="hidden" name="lrn_name" value=""/>				
				<input type="hidden" name="app_id" value="{$app_id}"/>
				<input type="hidden" name="cmt_id" value="{$cmt_id}"/>
				<input type="hidden" name="cmt_tkh_id" value="{$cmt_tkh_id}"/>
				<input type="hidden" name="cmt_max_score" value="{$cmt_max_score}"/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code" select="$parent_code"/>
				</xsl:call-template>
			<div class="wzb-item-main">
				<!-- navigation -->
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text">
						<xsl:value-of select="//itm_action_nav/@itm_title"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
					<xsl:with-param name="text">
						<xsl:choose>
							<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
								<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
									<xsl:value-of select="//itm_action_nav/@itm_title"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
									<xsl:value-of select="$itm_title"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
							<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$label_core_training_management_241"/>
							</span>
							
					</xsl:with-param>
				</xsl:call-template>
			<!--<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text" select="$lab_edit_mark"/>
				</xsl:call-template>
			-->
<!--			<xsl:call-template name="usr_detail_seperate_hdr"/>
				<table cellpadding="3" border="0" width="{$wb_gen_table_width}" height="16" cellspacing="0" class="Bg">
				</table>
-->
				<table>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="concat($lab_lrn,'：')"/>
						</td>
						<td class="wzb-form-control"><xsl:value-of select="eval_item/@lrn_name"/></td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="concat($lab_input_score,'：')"/>
						</td>
						<td class="wzb-form-control">
							<xsl:choose>
								<xsl:when test="string($cmt_score) = 'NaN'">
									<input type="text" name="cmt_score" value="" title="{$lab_input_score}" class="wzb-inputText"/>
								</xsl:when>
								<xsl:otherwise>
									<input type="text" name="cmt_score" value="{$cmt_score}" title="{$lab_input_score}" class="wzb-inputText"/>
								</xsl:otherwise>
							</xsl:choose>
							
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_ok"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.upd_mark(frm,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name">
							<xsl:value-of select="$lab_g_form_btn_cancel"/>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_href">javascript:evalmgt.get_scoring_item_marking_lst(frm)</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
					</xsl:call-template>
				</div>
				<script language="JavaScript" type="text/javascript"><![CDATA[
					str='<input type="submit" value="" size="0" style="height : 0px;width : 0px;visibility: hidden;"/>'
					if (document.all || document.getElementById!=null){
						document.write(str);
					}
				]]></script>
			</div>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>

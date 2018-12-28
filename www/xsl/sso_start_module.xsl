<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="content"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_track.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_assignment.js" type="text/javascript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			module_lst = new wbModule;
			prog_track = new wbTrack;
			var ass = new wbAssignment
			var tpl_use = '';

			function start_mod() {
				if (mod_type == 'CHT') {
					tpl_use = 'chat.xsl';
				} else if (mod_type == 'FOR') {
					tpl_use = 'forum.xsl';
				} else if (mod_type == 'VCR') {
					tpl_use = 'lrn_vcr.xsl';
				} else if (mod_type == 'FAQ') {
					tpl_use = 'lrn_faq.xsl';
				}else if (mod_type == 'VOD') {
					tpl_use = 'lrn_vod.xsl';
				}else {
					tpl_use = stylesheet;
				}
				
				if (is_diff_domain == 'true') {
					wb_utils_set_cookie('sso_diff_domain_' + tkh_id, is_diff_domain);
				}
				
				if (tpl_use == '') {
					tpl_use = 'blank_template.xsl';
				}
				
				if (mod_type == 'ASS') {
					if (attempt_nbr > 0) {
						ass.view_result(mod_id,usr_id,'false', tkh_id)
					} else {
						ass.submit_step_one(mod_id,'',tkh_id, false);
					}
				} else if (mod_type == 'SVY') {
					module_lst.start_exec(mod_type,mod_id,'svy_player.xsl',cos_id,']]><xsl:value-of select="$wb_lang"/><![CDATA[',tkh_id, '', '', '', '', true);
				}else if (mod_type == 'AICC_AU') {
					module_lst.start_aicc_au(res_src_link,']]><xsl:value-of select="//cur_usr/@ent_id"/><![CDATA[',cos_id,mod_id,mod_web_launch,mod_vendor,tkh_id, true);
				}else if (mod_type == 'SCO') {
					module_lst.start_scorm(res_src_link,']]><xsl:value-of select="//cur_usr/@ent_id"/><![CDATA[',cos_id,mod_id,mod_web_launch,mod_vendor,tkh_id, true);
				}else if (mod_type == 'REF') {
					module_lst.start_ref_exec(mod_type,mod_id,'lrn_get_reference_lst.xsl',cos_id,tkh_id, true);
				}else if (mod_type == 'NETG_COK') {
					module_lst.start_netg(cos_id,mod_id,res_src_link,tkh_id, false);
				}else if (mod_type == 'TST' || mod_type == 'DXT') {
					module_lst.start_prev(mod_type, mod_id, cos_id, tkh_id, true, ']]><xsl:value-of select="//cur_usr/@ent_id"/><![CDATA[', true, true);
				}else {
					module_lst.start_exec(mod_type,mod_id,tpl_use,cos_id,']]><xsl:value-of select="$wb_lang"/><![CDATA[',tkh_id,max_attempt,attempt_nbr,mod_status,sub_after_passed_ind, true);
				}
			}
			]]></SCRIPT>
		</head>
		<body onload="start_mod();"/>
		<script language="JavaScript"><![CDATA[
		mod_id = ']]><xsl:value-of select="/SSO_module_content/module/@id"/><![CDATA['
		cos_id = ']]><xsl:value-of select="/SSO_module_content/module/@cos_id"/><![CDATA['
		usr_id = ']]><xsl:value-of select="/SSO_module_content/module/@usr_id"/><![CDATA['
		mod_type = ']]><xsl:value-of select="/SSO_module_content/module/@type"/><![CDATA['
		mod_status = ']]><xsl:value-of select="/SSO_module_content/module/@mod_status"/><![CDATA['
		tkh_id = ']]><xsl:value-of select="/SSO_module_content/tkh_info/@tkh_id"/><![CDATA['
		max_attempt = ']]><xsl:value-of select="/SSO_module_content/module/@max_attempt"/><![CDATA['
		attempt_nbr = ']]><xsl:value-of select="/SSO_module_content/module/@attempt_nbr"/><![CDATA['
		sub_after_passed_ind = ']]><xsl:value-of select="/SSO_module_content/module/@sub_after_passed_ind"/><![CDATA['
		//essay_grade_status = ']]><xsl:value-of select="/SSO_module_content/essay_grade_status/text()"/><![CDATA['
		res_src_link = ']]><xsl:value-of select="/SSO_module_content/module/@res_src_link"/><![CDATA['
		mod_vendor = ']]><xsl:value-of select="/SSO_module_content/module/@mod_vendor"/><![CDATA['
		mod_web_launch = ']]><xsl:value-of select="/SSO_module_content/module/@mod_web_launch"/><![CDATA['
		is_diff_domain = ']]><xsl:value-of select="/SSO_module_content/module/@diff_domain"/><![CDATA['
		stylesheet = ']]><xsl:value-of select="/SSO_module_content/stylesheet/text()"/><![CDATA['
		]]></script>
	</xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="cur_usr_role" select="/applyeasy/meta/cur_usr/role/@id"/>
	<xsl:variable name="mod_id" select="/applyeasy/header/mod_id"></xsl:variable>
	<xsl:variable name="mod_type" select="/applyeasy/header/mod_type"></xsl:variable>
	<xsl:variable name="cos_id" select="/applyeasy/header/course_id"></xsl:variable>
	<xsl:variable name="tkh_id" select="/applyeasy/header/tkh_id"></xsl:variable>
	<xsl:variable name="tst_play_template">
		<xsl:choose>
			<xsl:when test="test_style ='only'">tst_player1.xsl</xsl:when>
			<xsl:when test="test_style ='many'">tst_player_many.xsl</xsl:when>
			<xsl:otherwise>tst_player_many.xsl</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_test_player_help_width">986</xsl:variable>
	<xsl:template name="DOCTYPE">
		<![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"/>]]>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
		<xsl:value-of select="document('')/*/xsl:template[@name='DOCTYPE']/node()" disable-output-escaping="yes"/>
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_instr">即將開始測驗</xsl:with-param>
			<xsl:with-param name="lab_instr_2">使用說明</xsl:with-param>
			<xsl:with-param name="lab_instr1">點擊“馬上開始”按鈕將立即開始本測驗，并且計算考試的嘗試次數。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_begin_test">馬上開始</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
			<xsl:with-param name="lab_not_start_desc">此測驗目前還未開始，請與培訓管理員聯繫。</xsl:with-param>
			<xsl:with-param name="lab_not_start">測驗未開始</xsl:with-param>
			<xsl:with-param name="lab_test_desc">考試界面使用說明：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_instr">即将开始测验</xsl:with-param>
			<xsl:with-param name="lab_instr_2">使用说明</xsl:with-param>
			<xsl:with-param name="lab_instr1">点击“马上开始”按钮将立即开始本测验，并且计算考试的尝试次数。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_begin_test">马上开始</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
			<xsl:with-param name="lab_not_start_desc">此测验目前还未开始，请与培训管理员联系。</xsl:with-param>
			<xsl:with-param name="lab_not_start">测验未开始</xsl:with-param>
			<xsl:with-param name="lab_test_desc">考试界面使用说明：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_instr">Launching the test</xsl:with-param>
			<xsl:with-param name="lab_instr_2">Usage</xsl:with-param>
			<xsl:with-param name="lab_instr1">Click the “Start” button at the bottom to launch the test. Attempt will be counted.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_begin_test">Start</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
			<xsl:with-param name="lab_not_start_desc">This test is not started, please contact your training administrator.</xsl:with-param>
			<xsl:with-param name="lab_not_start">Test not started</xsl:with-param>
			<xsl:with-param name="lab_test_desc">Introduction：</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_instr"/>
		<xsl:param name="lab_instr_2"/>
		<xsl:param name="lab_instr1"/>
		<xsl:param name="lab_g_form_btn_begin_test"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_not_start_desc"/>
		<xsl:param name="lab_not_start"/>
		<xsl:param name="lab_test_desc" />
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<link rel="stylesheet" href="../static/css/base.css"/>
			<link rel="stylesheet" href="../static/css/learner.css"/>
			<link rel="stylesheet" href="../static/css/exam.css"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			
			<script language="JavaScript" type="text/javascript"><![CDATA[
			var module_lst = new wbModule
			function set_privilege(){
				]]><xsl:if test="$cur_usr_role = 'NLRN_1' or $cur_usr_role = 'LRNMAN_1'"><![CDATA[wb_utils_set_cookie('isLearnerRole',1)]]></xsl:if><![CDATA[
			}
			]]>
			</script>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		</head>
			<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
				<center style="width:1200px;margin:0 auto;">
					<table border="0" cellpadding="0" cellspacing="0" width="{$wb_gen_table_width}">
						<tr>
							<td>
								<xsl:call-template name="help">
									<xsl:with-param name="lab_instr" select="$lab_instr"/>
									<xsl:with-param name="lab_instr_2" select="$lab_instr_2"/>
									<xsl:with-param name="lab_instr1" select="$lab_instr1"/>
									<xsl:with-param name="lab_g_form_btn_begin_test" select="$lab_g_form_btn_begin_test"/>
									<xsl:with-param name="lab_g_form_btn_close" select="$lab_g_form_btn_close"/>
									<xsl:with-param name="lab_not_start" select="$lab_not_start"/>
									<xsl:with-param name="lab_not_start_desc" select="$lab_not_start_desc"/>
									<xsl:with-param name="lab_test_desc" select="$lab_test_desc"/>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</center>
				<xsl:variable name="js_begin_test">
					<xsl:choose>
						<xsl:when test="isStartTest='true' and not(not_started)">
							<xsl:choose>
								<xsl:when test="isSso='true'">javascript:module_lst.start_exec('<xsl:value-of select="$mod_type"/>',<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="$tst_play_template"/>',<xsl:value-of select="$cos_id"/>,'{$wb_lang}',<xsl:value-of select="$tkh_id"/>,'','','','',true,'<xsl:value-of select="//cur_usr/@ent_id"/>');set_privilege()</xsl:when>
								<xsl:otherwise>javascript:module_lst.start_exec('<xsl:value-of select="$mod_type"/>',<xsl:value-of select="$mod_id"/>,'<xsl:value-of select="$tst_play_template"/>',<xsl:value-of select="$cos_id"/>,'{$wb_lang}',<xsl:value-of select="$tkh_id"/>,'','','','',false,'<xsl:value-of select="//cur_usr/@ent_id"/>', '<xsl:value-of select="test_style"/>');set_privilege()</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							javascript:Dialog.alert('<xsl:value-of select="$lab_not_start_desc"/>',function(){mobile_close(window,'<xsl:value-of select="$mod_type"/>');});
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<div class="xyd_warning_2">
				    <div class="xyd_warning_middle_2">
				        <em class="xyd_warning_bg_2"></em>
				        <i class="xyd_warning_deng_2"></i>
				        	<xsl:value-of select="$lab_instr1" />
				        <span class="btn wzb-btn-orange-2" style="margin:0 0 0 20px;" onclick="{$js_begin_test }"><xsl:value-of select="$lab_g_form_btn_begin_test" /></span>
				    </div>
				</div>
			</body>
		
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="help">
		<xsl:param name="lab_instr"/>
		<xsl:param name="lab_instr_2"/>
		<xsl:param name="lab_instr1"/>
		<xsl:param name="lab_g_form_btn_begin_test"/>
		<xsl:param name="lab_g_form_btn_close"/>
		<xsl:param name="lab_not_start"/>
		<xsl:param name="lab_not_start_desc"/>
		<xsl:param name="lab_test_desc" />
		
<!-- 		<xsl:call-template name="wb_ui_line"> -->
<!-- 			<xsl:with-param name="width" select="$wb_test_player_help_width"/> -->
<!-- 		</xsl:call-template> -->
		<div class="Line" style="height: 1px;"/>
		<table width="{$wb_gen_table_width}" border="0">
			<tr>
				<td class="Text" align="left">
				<div style="font-size:20px;padding:5px 0;color:#333;"><xsl:value-of select="$lab_test_desc" /></div>
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="0" cellspacing="0" width="{$wb_gen_table_width}" border="0">
						<tr>
							<td width="100%">
								<xsl:choose>
									<xsl:when test="test_style='only'"><img src="{$wb_img_lang_path}test_player_help.jpg" border="0" width="100%"/> </xsl:when>
									<xsl:otherwise><img src="{$wb_img_lang_path}test_many_help.jpg" border="0" width="100%"/> </xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div class="Line" style="height: 1px;"/>
		<table cellpadding="3" cellspacing="0" width="{$wb_test_player_help_width}" border="0">
			<tr>
				<td>
					<img src="{$wb_img_path}tp.gif" height="10" width="0"/>
				</td>
			</tr>
			<tr>
				<td align="center">
					<div class="area_page" style="padding:130px 0 0 136px;">
						
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

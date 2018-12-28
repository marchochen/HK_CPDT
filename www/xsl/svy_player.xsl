<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/wb_object_share.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>	
	<xsl:import href="share/svy_player_share.xsl"/>	
<!-- ========================================================== -->
<xsl:template name="content">
	<xsl:param name="lab_g_form_btn_submit"/>
	<xsl:param name="lab_g_form_btn_cancel"/>
	<xsl:param name="lab_g_form_btn_close"/>
	<xsl:param name="lab_no_que"/>

<!-- =========================== Label =========================== -->
<xsl:variable name="button_submit" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'btn_submit')"/>
<xsl:variable name="button_close" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'button_close')"/>
<xsl:variable name="global_que_total" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global_que_total')"/>
<xsl:variable name="global_show_more" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'global_show_more')"/>
<xsl:variable name="label_Course" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'personal_course')"/>

<html>
<head>
<title><xsl:value-of select="$wb_wizbank"/></title>
<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
<link rel="stylesheet" href="../static/js/font-awesome/css/font-awesome.min.css"/>
<link rel="stylesheet" href="../static/js/bootstrap/css/bootstrap.css"/>
<link rel="stylesheet" href="../static/css/learner.css"/>
<link rel="stylesheet" href="../static/css/base.css"/>
<link rel="stylesheet" href="../static/theme/blue/css/style.css"/>
<link rel="stylesheet" href="../static/js/layer/skin/layer.css"/>
<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
<script type="text/javascript" SRC="{$wb_js_path}jquery.min.js" LANGUAGE="JavaScript"/>
<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"/>
			
<script type="text/javascript" SRC="{$wb_js_path}layer.js" LANGUAGE="JavaScript"/>
<script type="text/javascript" SRC="{$wb_js_path}menu.js" LANGUAGE="JavaScript"/>
<script type="text/javascript" SRC="{$wb_js_path}wb_module.js" LANGUAGE="JavaScript"/>
<script type="text/javascript" SRC="{$wb_js_path}wb_utils.js" LANGUAGE="JavaScript"/>
<script type="text/javascript" SRC="{$wb_js_path}gen_utils.js" LANGUAGE="JavaScript"/>
<script type="text/javascript" SRC="{$wb_js_path}urlparam.js" LANGUAGE="JavaScript"/>
<script type="text/javascript" src="../static/js/basic.js"/>
<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>



<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
<script type="text/javascript" src="../js/jquery.prompt.js"></script>
<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
<script type="text/javascript" src="../static/js/cwn_utils.js"></script>

<script LANGUAGE="JavaScript">
<![CDATA[
// ---------------- Initializaton ---------------------

var qset = new CwQuestionSet()
var mod = new wbModule;
var InternetExplorer = navigator.appName.indexOf("Microsoft") == 0;
var totalQ = ]]><xsl:value-of select="$quiz/@size" /><![CDATA[;
var startQ = 1; 
var currentQue = 1; 
var testDuration = ]]><xsl:value-of select="$quiz/header/@duration"/><![CDATA[ * 60; // in second


function status(){
	return false;
}

function submit_svy(hasRateQue){
	]]><xsl:choose>
		<xsl:when test="contains(//cur_usr/role/@id,'NLRN')"><![CDATA[
		CwQuestionSubmit();
	]]></xsl:when>
	<xsl:otherwise><![CDATA[
		Dialog.alert(wb_msg_]]><xsl:value-of select="$wb_lang"/><![CDATA[_preview_mode)]]>
	</xsl:otherwise>
	</xsl:choose><![CDATA[
}
]]><xsl:if test="//cur_usr[@rol_admin='1' or @rol_teacher='1']"><![CDATA[
gen_del_cookie(COOKIE_QUE_FLG);
]]></xsl:if>
<xsl:if test="//cur_usr[@rol_student='1']"><![CDATA[
wb_utils_set_cookie('ACTIVE_MODULE',]]><xsl:value-of select="/quiz/@id"/><![CDATA[);
]]></xsl:if>
</script>
<!-- 	<xsl:call-template name="wb_css"> -->
<!-- 		<xsl:with-param name="view">wb_ui</xsl:with-param> -->
<!-- 	</xsl:call-template> -->
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_initInteractions();">
	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
			<div class="wzb-model-14">
				<div class="xyd-fixbox">
					<div class="xyd-fixcut xyd-survey">
						<div class="xyd-survey-box">
						
							<xsl:choose>
								<xsl:when test="//header/@subtype='SVY'">
									<div class="xyd-survey-left">
										<xsl:value-of select="$label_Course"/>：<xsl:value-of select="//header/@course_title" />
							    		<i></i>
							    		<!-- <xsl:value-of select="$quiz/header/title/text()"/> -->
									</div>
								</xsl:when>
								<xsl:otherwise>
									<div class="xyd-survey-left">
										<i></i>
										<xsl:value-of select="$quiz/header/title/text()"/>
									</div>
								</xsl:otherwise>
							</xsl:choose>
							
							<div class="xyd-survey-right">
								<xsl:if test="$quiz/@size>0 and contains(//cur_usr/role/@id,'NLRN')">
									<input onclick="javascript:submit_svy()" type="button" value="{$button_submit}" class="btn wzb-btn-yellow margin-right15" />
								</xsl:if>
								<input onclick="javascript:mobile_close(window.parent,'{$quiz/aicc_data/@tkh_id}')" type="button" value="{$button_close}" class="btn wzb-btn-yellow" />
							</div>
						</div>
						<xsl:choose>
							<xsl:when test="//header/@subtype='SVY'">
								<span id="que_total">
									<script LANGUAGE="JavaScript">
							    	document.getElementById('que_total').innerHTML = '<xsl:value-of select="$global_que_total"/>'.replace(/\$data/g, <xsl:value-of select="count($quiz/question)"/>)
						    		</script>
						    		<!-- 共<xsl:value-of select="count($quiz/question)"/>题 -->
								</span>
							</xsl:when>
							<xsl:otherwise>
								<span id="que_total">
									<script LANGUAGE="JavaScript">
							    	document.getElementById('que_total').innerHTML = '<xsl:value-of select="$global_que_total"/>'.replace(/\$data/g, <xsl:value-of select="count($quiz/question)"/>)
						    		</script>
						    		<!-- 共<xsl:value-of select="count($quiz/question)"/>题 -->
								</span>
							</xsl:otherwise>
						</xsl:choose>
					</div>
				</div>
				
				<xsl:choose>
					<xsl:when test="count($quiz/question) = 0">
						<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_no_que"/>
						</xsl:call-template>
						<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3">
							<tr>
								<td align="center">
									<xsl:call-template name="wb_gen_form_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_close"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:parent.close()</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</xsl:when>
					
					<xsl:otherwise>
						<div class="xyd-survey-list diaocha" style="padding: 0 45px 100px 45px">
							<ul>
								<xsl:apply-templates select="$quiz/question" mode="TST"/>
								
								<xsl:if test="count($quiz/question) > 3">
									
									<li class="xyd-survey-more dcmore" style="display: list-item;">
							            <span class="xyd-survey-icon"><i class="fa fa-angle-double-down"></i></span>
							            <dl>
							                <dt><span class="swop_color"><xsl:value-of select="$global_show_more"/></span></dt>
							                <dd>
							                </dd>
							            </dl>
        							</li>
								</xsl:if>
							</ul>
						</div>
						
						<form action="{$wb_servlet_qdbaction_url}" method="post" name="frmResult">
							<input type="hidden" name="is_cos_eval" value="true"/>
							<input name="cmd" type="hidden"/>
							<input name="mod_type" type="hidden" value="{$quiz/header/@subtype}"/>
							<input name="charset" type="hidden" value="{//@charset}"/>
							<input name="pgr_usr_id" type="hidden" value="{//cur_usr/@ent_id}"/>
							<input name="pgr_mod_id" type="hidden" value="{$quiz/@id}"/>
							<input name="pgr_start_time" type="hidden" value="{$quiz/@start_time}"/>
							<input name="pgr_used_time" type="hidden" value=""/>
							<!-- in second -->
							<input name="pgr_status" type="hidden" value="OK"/>
							<!-- no use yet -->
							<input name="cos_id" type="hidden" value="{//header/@course_id}"/>
							<input name="tkh_id" type="hidden" value="{$quiz/aicc_data/@tkh_id}"/>
							<input type="hidden" name="mov_last_upd_timestamp" value=""/>
							<xsl:for-each select="$quiz/question">
								<xsl:variable name="_i" select="position()"/>
								<input name="atm_int_res_id_{$_i}" type="hidden" value=""/>
								<input name="atm_flag_{$_i}" type="hidden" value=""/>
								<xsl:for-each select="body/interaction">
									<xsl:variable name="_j" select="@order"/>
									<input name="atm_int_order_{$_i}_{$_j}" type="hidden" value=""/>
									<input name="atm_response_{$_i}_{$_j}" type="hidden" value=""/>
									<input name="atm_response_ext_{$_i}_{$_j}" type="hidden" value=""/>
								</xsl:for-each>
							</xsl:for-each>
							<input name="url_success" type="hidden" value=""/>
							<input name="url_failure" type="hidden" value=""/>
						</form>
						
					</xsl:otherwise>
					
				</xsl:choose>

			</div>

		</div>
	</div>
</body>
</html>
</xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../wb_const.xsl"/>
	<xsl:import href="../utils/escape_js.xsl"/>
	<xsl:import href="../utils/escape_all.xsl"/>
	<xsl:import href="../utils/escape_carriage_return.xsl"/>
	<xsl:import href="../utils/wb_gen_form_button.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="../utils/html_linefeed.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_ui_desc.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_ui_space.xsl"/>
	<xsl:import href="../share/res_label_share.xsl"/>
	<xsl:import href="../utils/wb_gen_input_file.xsl"/>
	<xsl:import href="../utils/wb_ui_hdr.xsl"/>
	<xsl:import href="../utils/wb_ui_title.xsl"/>		
	<xsl:import href="../utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="../utils/kindeditor.xsl"/>	
	
	<xsl:import href="../share/wb_layout_share.xsl"/>
	<xsl:import href="../share/wb_object_share.xsl"/>
	<!-- =============================================================== -->
	<xsl:variable name="que_id" select="/question/@id"/>
	<xsl:variable name="que_obj_id" select="/question/header/objective/@id"/>
	<xsl:variable name="que_timestamp" select="/question/@timestamp"/>
	<xsl:variable name="id_obj" select="/question_form/objective/@id"/>
	<xsl:variable name="mod_type" select="/question_form/@mod_type"/>
	<xsl:variable name="mod_type_1" select="/question/@mod_type"/>
	<xsl:variable name="edit_type" select="RES_UPD" />
	<xsl:variable name="is_evn_que">
		<xsl:choose>
			<xsl:when test="$mod_type='SVY' or $mod_type_1='SVY' or $mod_type='EVN' or $mod_type_1='EVN'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="applet_img_path"><xsl:value-of select="substring($wb_media_path, 4)"/>app/</xsl:variable>
	<xsl:variable name="mange_source">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">管理資源</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">管理资源</xsl:when>
			<xsl:otherwise>Management resources</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_add_que">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">增加題目</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">添加题目</xsl:when>
			<xsl:otherwise>Question</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_edit_que">
		<xsl:choose>
			<xsl:when test="$wb_lang = 'ch'">修改題目</xsl:when>
			<xsl:when test="$wb_lang = 'gb'">修改题目</xsl:when>
			<xsl:otherwise>Edit Question</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_file_type" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan5')"/>
	<!-- =============================================================== -->
	<xsl:template name="mc_html_header">
		<xsl:param name="mode"/>
		<xsl:param name="isOpen"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:if test="$isOpen = 'true'">
				<script type="text/javascript" src="{$wb_js_path}jquery.js" language="JavaScript"/>
				
				<!--alert样式  -->
				<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
			</xsl:if>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_rm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}que_send_mc.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_media_path}app/wb_applet_skin.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<xsl:choose>
				<xsl:when test="$mode='INS' ">
					<script language="JavaScript"><![CDATA[			
					que = new wbQuestion;
					mc = new wbMC;
					res = new wbResource						
		obj = new wbObjective
					gObjective = "]]><xsl:value-of select="question_form/objective/@id"/><![CDATA["
					gQuestionType = "]]><xsl:value-of select="question_form/@type"/><![CDATA["
					
					function changeQueType(type) {
						mod_type = getUrlParam("mod_type")
						]]>
						<xsl:choose>
							<xsl:when test="$isOpen = 'true'">
								<![CDATA[que.add_evn_que_prep(gObjective,type,mod_type)]]>
							</xsl:when>
							<xsl:otherwise>
								<![CDATA[que.add_que_prep(gObjective,type,mod_type)]]>
							</xsl:otherwise>
						</xsl:choose><![CDATA[
					}
					
					function send(lang){	
						mc.sendFrm(document.frmXml,lang,']]><xsl:value-of select="$isOpen = 'true'"/><![CDATA[');
					}
								
					
					function init(){			
						mod_type = getUrlParam("mod_type");								
						if (mod_type!="SVY" && mod_type!="EVN"){
							document.frmXml.score_choice.selectedIndex = 2								
						}		
						if(frmXml.mc_option && frmXml.mc_option.length == 10){
							document.getElementById('add_option_button').style.display = 'none';
						}
						if(frmXml.editor){
							editor.focus();
						}
						//document.frmXml.qst_text.focus();
					}			
				]]></script>
				</xsl:when>
				<xsl:otherwise>
					<script language="JavaScript"><![CDATA[
	obj = new wbObjective
					mc = new wbMC;
					que = new wbQuestion;
					function read_que(){
						que = new wbQuestion
						que.read(']]><xsl:value-of select="/question/@id"/><![CDATA[')
					}
					
					function init(){	
						if(frmXml.mc_option && frmXml.mc_option.length == 10){
							document.getElementById('add_option_button').style.display = 'none';
						}
						if(frmXml.editor){
							editor.focus();
						}
						
						]]><xsl:if test="$is_evn_que = 'false'">
							<xsl:choose>
								<xsl:when test="/question/outcome/@logic = 'AND'"><![CDATA[document.frmXml.score_choice.selectedIndex = 0 ;]]></xsl:when>
								<xsl:when test="/question/outcome/@logic = 'OR'"><![CDATA[document.frmXml.score_choice.selectedIndex = 1;]]></xsl:when>
								<xsl:otherwise>
									<xsl:if test="count(/question/outcome/feedback/@score) > 1"><![CDATA[document.frmXml.score_choice.selectedIndex = 3;]]></xsl:if>
									<xsl:if test="count(/question/outcome/feedback/@score) = 1"><![CDATA[document.frmXml.score_choice.selectedIndex = 2;]]></xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>
						<![CDATA[
						return;
					}
					]]></script>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="new_css"/>
			<xsl:call-template name="kindeditor_css"/>
		</head>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="tf_html_header">
		<xsl:param name="mode"/>
		<xsl:param name="isOpen"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:if test="$isOpen = 'true'">
				<script type="text/javascript" src="{$wb_js_path}jquery.js" language="JavaScript"/>
				<!--alert样式  -->
				<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
			</xsl:if>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}que_send_tf.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_media_path}app/wb_applet_skin.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<xsl:choose>
				<xsl:when test="$mode='INS' ">
					<script language="JavaScript"><![CDATA[			
					que = new wbQuestion;
					tf = new wbTF;
					res = new wbResource						
		obj = new wbObjective
					gObjective = "]]><xsl:value-of select="question_form/objective/@id"/><![CDATA["
					gQuestionType = "]]><xsl:value-of select="question_form/@type"/><![CDATA["
					
					function changeQueType(type) {
						mod_type = getUrlParam("mod_type")
						]]>
						<xsl:choose>
							<xsl:when test="$isOpen = 'true'">
								<![CDATA[que.add_evn_que_prep(gObjective,type,mod_type)]]>
							</xsl:when>
							<xsl:otherwise>
								<![CDATA[que.add_que_prep(gObjective,type,mod_type)]]>
							</xsl:otherwise>
						</xsl:choose><![CDATA[
						
					}
					
					function send(lang){	
						tf.sendFrm(document.frmXml,lang);
					}
												
					function init(){			
						mod_type = getUrlParam("mod_type");
								
						if (mod_type!="SVY"){
							document.frmXml.score_choice.selectedIndex = 2
						}
						if(frmXml.editor){
							editor.focus();
						}
						//document.frmXml.qst_text.focus();	
					}			
				]]></script>
				</xsl:when>
				<xsl:otherwise>
					<script language="JavaScript"><![CDATA[
					que = new wbQuestion;
					tf = new wbTF;
			obj = new wbObjective
					function read_que() {
						que = new wbQuestion
						que.read(']]><xsl:value-of select="/question/@id"/><![CDATA[')
					}							
					
					function init() {
					]]><xsl:if test="not($mod_type='SVY' or $mod_type_1='SVY')">
							<xsl:choose>
								<xsl:when test="/question/outcome/@logic = 'AND'"><![CDATA[document.frmXml.score_choice.selectedIndex = 0 ]]></xsl:when>
								<xsl:when test="/question/outcome/@logic = 'OR'"><![CDATA[document.frmXml.score_choice.selectedIndex = 1]]></xsl:when>
								<xsl:otherwise>
									<xsl:if test="count(/question/outcome/feedback/@score) > 1"><![CDATA[document.frmXml.score_choice.selectedIndex = 3]]></xsl:if>
									<xsl:if test="count(/question/outcome/feedback/@score) = 1"><![CDATA[document.frmXml.score_choice.selectedIndex = 2]]></xsl:if>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:if>	  
						<![CDATA[
							if(frmXml.editor){
								editor.focus();
							}
							//document.frmXml.qst_text.focus();
						}
						]]></script>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="kindeditor_css"/>
		</head>
	</xsl:template>

	<!-- =============================================================== -->
	<xsl:template name="fb_html_header">
		<xsl:param name="mode"/>
		<xsl:param name="isOpen"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:if test="$isOpen = 'true'">
				<script type="text/javascript" src="{$wb_js_path}jquery.js" language="JavaScript"/>
				
				<!--alert样式  -->
				<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
			</xsl:if>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_rm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}que_send_fb.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_media_path}app/wb_applet_skin.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script language="JavaScript">
				<xsl:choose>
					<xsl:when test="$mode='INS' "><![CDATA[
	                obj = new wbObjective
					que = new wbQuestion;
					fb = new wbFB;
					res = new wbResource
					gObjective = "]]><xsl:value-of select="question_form/objective/@id"/><![CDATA["
					gQuestionType = "]]><xsl:value-of select="question_form/@type"/><![CDATA["
					
					function changeQueType(type) {
						mod_type = getUrlParam("mod_type")
						]]>
						<xsl:choose>
							<xsl:when test="$isOpen = 'true'">
								<![CDATA[que.add_evn_que_prep(gObjective,type,mod_type)]]>
							</xsl:when>
							<xsl:otherwise>
								<![CDATA[que.add_que_prep(gObjective,type,mod_type)]]>
							</xsl:otherwise>
						</xsl:choose><![CDATA[
					}
					
					function send(lang){	
						fb.sendFrm(document.frmXml,lang,'',']]><xsl:value-of select="$wb_common_img_path"/><![CDATA[',']]><xsl:value-of select="$isOpen = 'true'"/><![CDATA[')		
					}
							
					function init(){		
						mod_type = getUrlParam("mod_type")
						if(frmXml.editor){
							editor.focus();
						}
					}]]></xsl:when>
					<xsl:otherwise><![CDATA[
					fb = new wbFB
					que = new wbQuestion
					obj = new wbObjective
					function read_que() {
						que = new wbQuestion
						que.read(']]><xsl:value-of select="/question/@id"/><![CDATA[')
					}
					
					
					function init(){
					/*	mod_type = getUrlParam("mod_type");	
				]]><![CDATA[
					if (mod_type!='SVY' && mod_type!='EVN')
						document.applets["fbQuestion"].setNumBlank(]]><xsl:value-of select="count(//interaction)"/><![CDATA[);]]><xsl:if test="$is_evn_que = 'false'">
							<xsl:for-each select="/question/body/interaction"><![CDATA[document.applets["fbAnswer"].addBlank(]]><xsl:value-of select="@order"/><![CDATA[);
						document.applets["fbAnswer"].saveMaxLength(]]><xsl:value-of select="@order"/><![CDATA[,]]><xsl:value-of select="@length"/><![CDATA[);]]><xsl:variable name="int_id" select="@order"/>
								<xsl:for-each select="../../outcome[@order=$int_id]">
									<xsl:for-each select="feedback">
										<xsl:variable name="int_condition" select="@condition"/><![CDATA[document.applets["fbAnswer"].saveAnswerData(]]><xsl:value-of select="$int_id"/><![CDATA[,']]><xsl:value-of select="@condition"/><![CDATA[',']]><xsl:value-of select="../../explanation[@order=$int_id]/rationale[@condition=$int_condition]"/><![CDATA[',']]><xsl:value-of select="@score"/><![CDATA[',']]><xsl:value-of select="@type"/><![CDATA[',']]><xsl:value-of select="@case_sensitive"/><![CDATA[',']]><xsl:value-of select="@space_sensitive"/><![CDATA[');]]></xsl:for-each><![CDATA[
						document.applets["fbAnswer"].resetAnsList();]]></xsl:for-each>
							</xsl:for-each>
						</xsl:if>
						<xsl:variable name="question_text">
							<xsl:for-each select="/question/body/text()|/question/body/interaction|/question/body/html">
								<xsl:choose>
									<xsl:when test="name() = 'interaction'">
										<xsl:variable name="order" select="@order"/>
										<xsl:text>[___' +</xsl:text><![CDATA[wb_msg_]]><xsl:value-of select="$wb_lang"/><![CDATA[_blank +']]><xsl:value-of select="$order"/>
										<xsl:text>___]</xsl:text>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="escape_all"><xsl:with-param name="input_str"><xsl:apply-templates select="."/></xsl:with-param></xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</xsl:variable><![CDATA[
									if (mod_type!='SVY' && mod_type!='EVN')
								document.applets["fbQuestion"].setQuestionText(']]><xsl:value-of select="$question_text"/><![CDATA[')*/
						
						if(frmXml.editor){
							editor.focus();
						}
				}				
					function send(lang){	
						fb.sendFrm(document.frmXml,lang,'',']]><xsl:value-of select="$wb_common_img_path"/><![CDATA[',']]><xsl:value-of select="$isOpen = 'true'"/><![CDATA[')
					}
		]]></xsl:otherwise>
				</xsl:choose>
			</script>
			<xsl:call-template name="kindeditor_css"/>
		</head>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="es_html_header">
		<xsl:param name="mode"/>
		<xsl:param name="isOpen"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:if test="$isOpen = 'true'">
				<script type="text/javascript" src="{$wb_js_path}jquery.js" language="JavaScript"/>
				<!--alert样式  -->
				<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
			</xsl:if>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}que_send_es.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_media_path}app/wb_applet_skin.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<script language="JavaScript">
				<xsl:choose>
					<xsl:when test="$mode='INS' "><![CDATA[
	obj = new wbObjective
					que = new wbQuestion;
					es = new wbES;
					res = new wbResource
					gObjective = "]]><xsl:value-of select="question_form/objective/@id"/><![CDATA["
					gQuestionType = "]]><xsl:value-of select="question_form/@type"/><![CDATA["
					
					function changeQueType(type) {
						mod_type = getUrlParam("mod_type")
						]]>
						<xsl:choose>
							<xsl:when test="$isOpen = 'true'">
								<![CDATA[que.add_evn_que_prep(gObjective,type,mod_type)]]>
							</xsl:when>
							<xsl:otherwise>
								<![CDATA[que.add_que_prep(gObjective,type,mod_type)]]>
							</xsl:otherwise>
						</xsl:choose><![CDATA[
					}
					
					function send(lang){	
						es.sendFrm(document.frmXml,lang)		
					}
							
					function init(){		
						mod_type = getUrlParam("mod_type")
						if(frmXml.editor){
							editor.focus();
						}
					}]]></xsl:when>
					<xsl:otherwise><![CDATA[
					es = new wbES
					obj = new wbObjective
					que = new wbQuestion;
					function read_que() {
						que = new wbQuestion
						que.read(']]><xsl:value-of select="/question/@id"/><![CDATA[')
					}
					
					function init(){	
						if(frmXml.editor){
							editor.focus();
						}				
					}														
							
					function send(lang){	
						es.sendFrm(document.frmXml,lang)
					}
		]]></xsl:otherwise>
				</xsl:choose>
			</script>
			<xsl:call-template name="kindeditor_css"/>
		</head>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="mt_html_header">
		<xsl:param name="mode"/>
		<xsl:param name="isOpen"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="wb_wizbank"/>
			</title>
			<xsl:if test="$isOpen = 'true'">
				<script type="text/javascript" src="{$wb_js_path}jquery.js" language="JavaScript"/>
				<!--alert样式  -->
				<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
			</xsl:if>
			<script type="text/javascript" src="../../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js" language="JavaScript"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"/>
			<script type="text/javascript" src="{$wb_js_path}que_send_mt.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_media_path}app/wb_applet_skin.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<xsl:choose>
				<xsl:when test="$mode='INS'">
					<script language="JavaScript"><![CDATA[			
					obj = new wbObjective	
			que = new wbQuestion;
			mt = new wbMT;				
			res = new wbResource
			gObjective = "]]><xsl:value-of select="question_form/objective/@id"/><![CDATA["
			gQuestionType = "]]><xsl:value-of select="question_form/@type"/><![CDATA["
			
			function changeQueType(type) {
				mod_type = getUrlParam("mod_type")
				]]>
				<xsl:choose>
					<xsl:when test="$isOpen = 'true'">
						<![CDATA[que.add_evn_que_prep(gObjective,type,mod_type)]]>
					</xsl:when>
					<xsl:otherwise>
						<![CDATA[que.add_que_prep(gObjective,type,mod_type)]]>
					</xsl:otherwise>
				</xsl:choose><![CDATA[
			}				
			
			function init(){			
				document.frmXml.que_textfield.focus();
			}	]]></script>
				</xsl:when>
				<xsl:otherwise>
					<script language="JavaScript"><![CDATA[				
			mt = new wbMT	
			obj = new wbObjective
			function read_que() {
				que = new wbQuestion
				que.read(']]><xsl:value-of select="/question/@id"/><![CDATA[')
			}

			function init() { 													
				}
			]]></script>
				</xsl:otherwise>
			</xsl:choose>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			var source_num = 2;
			var target_num = 2;
			]]><xsl:if test="$mode ='UPD' "><![CDATA[
				source_num = ]]><xsl:value-of select="count(/question/body/interaction)"/> <![CDATA[;
				target_num = source_num;
			]]></xsl:if><![CDATA[
		function checkradio(id){
			var radio_obj = document.getElementById(id);
			radio_obj.checked  = true;
		}
		]]></script>
			<xsl:call-template name="kindeditor_css"/>
		</head>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="sc_html_header">
		<xsl:param name="mode"/>
		<xsl:param name="isOpen"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="wb_wizbank"/>
			</title>
			<xsl:if test="$isOpen = 'true'">
				<script type="text/javascript" src="{$wb_js_path}jquery.js" language="JavaScript"/>
				<!--alert样式  -->
				<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
			</xsl:if>
			<script type="text/javascript" src="{$wb_js_path}que_send_sc.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_question.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_media.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_resource.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_media_path}app/wb_applet_skin.js" language="JavaScript"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="{$wb_js_path}wb_objective.js" language="JavaScript"/>
			<xsl:choose>
				<xsl:when test="$mode='INS'">
					<script language="JavaScript"><![CDATA[
					obj = new wbObjective
						que = new wbQuestion;
						sc = new wbSC;
						res = new wbResource
						gObjective = "]]><xsl:value-of select="question_form/objective/@id"/><![CDATA["
						gQuestionType = "]]><xsl:value-of select="question_form/@type"/><![CDATA["
						
						function changeQueType(type) {
							mod_type = getUrlParam("mod_type")
							]]>
							<xsl:choose>
								<xsl:when test="$isOpen = 'true'">
									<![CDATA[que.add_evn_que_prep(gObjective,type,mod_type)]]>
								</xsl:when>
								<xsl:otherwise>
									<![CDATA[que.add_que_prep(gObjective,type,mod_type)]]>
								</xsl:otherwise>
							</xsl:choose><![CDATA[
						}
						
						function init(){
							if(frmXml.editor){
								editor.focus();
							}
						}	
			   ]]></script>
				</xsl:when>
				<xsl:otherwise>
					<script language="JavaScript"><![CDATA[
			sc = new wbSC
			que = new wbQuestion;
			obj = new wbObjective
			function read_que() {
				que = new wbQuestion
				que.read(']]><xsl:value-of select="/question/@id"/><![CDATA[')
			}

			function init() {
				if(frmXml.editor){
					editor.focus();
				}
			}
			]]></script>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:call-template name="kindeditor_css"/>
		</head>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="fb_question_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="fb_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_que_header">題目內容</xsl:with-param>
					<xsl:with-param name="lab_que">题目</xsl:with-param>
					<xsl:with-param name="lab_as_htm">將內容以HTML處理</xsl:with-param>
					<xsl:with-param name="lab_hint">提示?</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_remove_media">刪除媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒體檔案(JPG,GIF,PNG)</xsl:with-param>
					<xsl:with-param name="lab_media_file_remark">(圖片不會在移動端顯示)</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_case_sensitive">大小寫須相符</xsl:with-param>
					<xsl:with-param name="lab_score">分數</xsl:with-param>
					<xsl:with-param name="lab_explanation">說明</xsl:with-param>
					<xsl:with-param name="lab_space_sensitive">空白處不可略</xsl:with-param>
					<xsl:with-param name="lab_type">類型</xsl:with-param>
					<xsl:with-param name="lab_text">純文字</xsl:with-param>
					<xsl:with-param name="lab_number">數字</xsl:with-param>
					<xsl:with-param name="lab_max_char_allow">最多字數</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">儲存</xsl:with-param>
					<xsl:with-param name="lab_modify_desc">用戶可以選中填空圖標,通過點擊工具欄右邊 [__] 圖標來修改內容和答案.</xsl:with-param>
					<xsl:with-param name="lab_media_file_support">媒體檔案只支援JPG, GIF, PNG格式文檔</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="fb_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_que_header">题目内容</xsl:with-param>
					<xsl:with-param name="lab_que">题目</xsl:with-param>
					<xsl:with-param name="lab_as_htm">将内容以HTML处理</xsl:with-param>
					<xsl:with-param name="lab_hint">提示？</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒体文档</xsl:with-param>
					<xsl:with-param name="lab_remove_media">删除媒体文档</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改为</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒体文档(JPG,GIF,PNG)</xsl:with-param>
					<xsl:with-param name="lab_media_file_remark">(图片不会在移动端显示)</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_case_sensitive">大小写须相符</xsl:with-param>
					<xsl:with-param name="lab_score">分数</xsl:with-param>
					<xsl:with-param name="lab_explanation">说明</xsl:with-param>
					<xsl:with-param name="lab_space_sensitive">空白处不可略</xsl:with-param>
					<xsl:with-param name="lab_type">类型</xsl:with-param>
					<xsl:with-param name="lab_text">纯文字</xsl:with-param>
					<xsl:with-param name="lab_number">数字</xsl:with-param>
					<xsl:with-param name="lab_max_char_allow">最多字数</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">保存</xsl:with-param>
					<xsl:with-param name="lab_modify_desc">用户可以选中填空图标,通过点击工具栏右边 [__] 图标来修改内容和答案.</xsl:with-param>
					<xsl:with-param name="lab_media_file_support">媒体档案只支持JPG, GIF, PNG格式文档</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="fb_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_que">Question</xsl:with-param>
					<xsl:with-param name="lab_que_header">Question text</xsl:with-param>
					<xsl:with-param name="lab_as_htm">Treat content as HTML</xsl:with-param>
					<xsl:with-param name="lab_hint">Hint?</xsl:with-param>
					<xsl:with-param name="lab_keep_media">Keep the media file</xsl:with-param>
					<xsl:with-param name="lab_remove_media">Remove the media file</xsl:with-param>
					<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
					<xsl:with-param name="lab_media_file">Media file(JPG,GIF,PNG)</xsl:with-param>
					<xsl:with-param name="lab_media_file_remark">(Image will not be shown in mobile)</xsl:with-param>
					<xsl:with-param name="lab_answer">Answer</xsl:with-param>
					<xsl:with-param name="lab_case_sensitive">Case sensitive</xsl:with-param>
					<xsl:with-param name="lab_score">Score</xsl:with-param>
					<xsl:with-param name="lab_explanation">Explanation</xsl:with-param>
					<xsl:with-param name="lab_space_sensitive">Space sensitive</xsl:with-param>
					<xsl:with-param name="lab_type">Type</xsl:with-param>
					<xsl:with-param name="lab_text">Text</xsl:with-param>
					<xsl:with-param name="lab_number">Number</xsl:with-param>
					<xsl:with-param name="lab_max_char_allow">Max. characters allowed</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">Save</xsl:with-param>
					<xsl:with-param name="lab_modify_desc">User can modify the Fill-in-the-Blank answer object by selecting it and clicking the [__] botton at right-most of tool bar.</xsl:with-param>
					<xsl:with-param name="lab_media_file_support">media types should be in 'jpg,jpeg,gif,png'</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="fb_question_body_">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:param name="lab_que_header"/>
		<xsl:param name="lab_as_htm"/>
		<xsl:param name="lab_hint"/>
		<xsl:param name="lab_keep_media"/>
		<xsl:param name="lab_remove_media"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_media_file"/>
		<xsl:param name="lab_media_file_remark"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_case_sensitive"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_space_sensitive"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_text"/>
		<xsl:param name="lab_number"/>
		<xsl:param name="lab_max_char_allow"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_fb_evn"/>
		<xsl:param name="lab_modify_desc"/>
		<xsl:param name="lab_media_file_support"/>
		<xsl:param name="lab_que"/>
		<input type="hidden" name="inter_num_" value=""/>
		<input type="hidden" name="inter_score_" value=""/>
		<input type="hidden" name="inter_type_" value=""/>
		<input type="hidden" name="inter_length_" value=""/>
		<input type="hidden" name="inter_opt_num_" value=""/>
		<input type="hidden" name="inter_opt_body_" value=""/>
		<input type="hidden" name="inter_opt_score_" value=""/>
		<input type="hidden" name="inter_opt_cond_" value=""/>
		<input type="hidden" name="inter_opt_exp_" value=""/>
		<input type="hidden" name="inter_opt_html_" value=""/>
		<input type="hidden" name="inter_opt_case_" value=""/>
		<input type="hidden" name="inter_opt_space_" value=""/>
		<input type="hidden" name="inter_opt_type_" value=""/>
	      <xsl:if test="$header != 'NO'">	   <!-- 弹出窗模式  不显示标题和上级菜单  对其他地方有影响可以去掉-->
		   <xsl:call-template name="ques_header_nav">
				<xsl:with-param name="mode" select="$mode"></xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$mode='INS'">
							<xsl:value-of select="$lab_add_que"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of disable-output-escaping="yes" select="/question/body/text() | /question/body/html | /question/body/interaction"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
		 </xsl:if> 
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_que"/>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
			<xsl:with-param name="extra_td">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<td align="right">
							<xsl:call-template name="draw_que_type_box">
								<xsl:with-param name="cur_sel">FB</xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"><xsl:with-param name="width" select="$width"/></xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="5" cellspacing="0" border="0" width="100%">
					
						<xsl:if test="not($mod_type='SVY' or $mod_type_1='SVY' or $mod_type='EVN' or $mod_type_1='EVN') and /question/@id &gt;0 ">
						
							<td colspan="2">
								<span class="Text">
									<xsl:value-of select="$lab_modify_desc"/>
								</span>
							</td>
						</xsl:if>
									
						<tr>
							<td colspan="2">
								<xsl:choose>
									<xsl:when test="not($mod_type = 'SVY' or $mod_type ='EVN' or $mod_type_1 = 'SVY' or $mod_type_1 = 'EVN')">
										<xsl:call-template name="kindeditor_panel">
											<xsl:with-param name="body">
												<xsl:for-each select="/question/body/text() | /question/body/html | /question/body/interaction">
													<xsl:variable name="order" select="@order" />
													<xsl:choose>
														<xsl:when test="name() = 'html'">
															<xsl:value-of disable-output-escaping="yes" select="."/>
														</xsl:when>
														<xsl:when test="name() = 'interaction'">
															<xsl:for-each select="/question/outcome">
																<xsl:if test="@order = $order">
																	<img id="{@score}" title="{feedback/@condition}" alt="FB_blank[|]_answer=[FB_split~]{feedback/@condition}[FB_split~]{@score}[FB_split~]{../explanation[@order=$order]/rationale}[FB_split~]" src="{$wb_common_img_path}ans.gif"/>
																</xsl:if>
															</xsl:for-each>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="."/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:for-each>
											</xsl:with-param>
											<xsl:with-param name="frm">document.frmXml</xsl:with-param>
											<xsl:with-param name="fld_name">qst_body</xsl:with-param>
											<xsl:with-param name="option">kindeditorFBOptions</xsl:with-param>
											<xsl:with-param name="rows">2</xsl:with-param>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<div>
										<!--  
										<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
										 ]]><xsl:variable name="que_text">
												<xsl:call-template name="html_linefeed"><xsl:with-param name="js">true</xsl:with-param><xsl:with-param name="my_right_value"><xsl:for-each select="/question/body/text()|/question/body/interaction|/question/body/html"><xsl:call-template name="escape_all"><xsl:with-param name="input_str"><xsl:apply-templates select="."/></xsl:with-param></xsl:call-template></xsl:for-each></xsl:with-param></xsl:call-template>
											</xsl:variable><![CDATA[
													str='<textarea class="wzb-inputText" rows="6" cols="60" name="que_body_1" style="width:100%;">]]><xsl:value-of select="$que_text"/><![CDATA[</textarea>';
								  		document.write(str);
												
								  		]]></SCRIPT>
								  		-->
								  		<xsl:call-template name="kindeditor_panel">
												<xsl:with-param name="body">
													<xsl:for-each select="/question/body/text() | /question/body/html |/question/body/interaction">
														<xsl:choose>
															<xsl:when test="name() = 'html'">
																<xsl:value-of disable-output-escaping="yes" select="."/>
															</xsl:when>
															<xsl:otherwise>
																<xsl:value-of disable-output-escaping="yes" select="."/>
															</xsl:otherwise>
														</xsl:choose>
													</xsl:for-each>
												</xsl:with-param>
												<xsl:with-param name="frm">document.frmXml</xsl:with-param>
												<xsl:with-param name="fld_name">que_body_1</xsl:with-param>
												<xsl:with-param name="option">kindeditorMCOptions</xsl:with-param>
												<xsl:with-param name="rows">4</xsl:with-param>
											</xsl:call-template>
										</div>
										<div style="display:none;">
											<label for="_as_html">
												<input type="checkbox" name="asHTML" id="_as_html">
													<xsl:if test="/question/body/html"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
												</input>
												<span class="Text">
													<xsl:value-of select="$lab_as_htm"/>
												</span>									
											</label>								
										</div>							
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>
						<!--  问答题时  屏蔽上传附件
                        <xsl:if test="$mod_type = 'SVY' or $mod_type ='EVN' or $mod_type_1 = 'SVY' or $mod_type_1 = 'EVN'">
						<xsl:call-template name="draw_media_row">
							<xsl:with-param name="lab_keep_media" select="$lab_keep_media"/>
							<xsl:with-param name="lab_remove_media" select="$lab_remove_media"/>
							<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
							<xsl:with-param name="lab_media_file" select="concat($lab_media_file,$lab_media_file_remark)"/>
							<xsl:with-param name="lab_media_file_support" select="$lab_media_file_support"/>
						</xsl:call-template>								
						</xsl:if> -->							
					</table>
				</td>
			</tr>
			<tr>
				<td height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>			
		</table>
		<!-- 
		<xsl:if test="$is_evn_que = 'false'">
			<xsl:call-template name="wb_ui_space">
			<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td>
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[	
						str='<APPLET codebase="../applet" CODE="fb_label.class" archive="fb_label.jar" NAME="fbLabel" WIDTH="130" HEIGHT="25" MAYSCRIPT="MAYSCRIPT">'
						str+='<param NAME="bgR" VALUE="'+ 130 +'"/>'
						str+='<param NAME="bgG" VALUE="'+ 130 +'"/>'  			
						str+='<param NAME="bgB" VALUE="'+ 130 +'"/>'  			
						str+='<param NAME="fontR" VALUE="'+ 255 +'"/>'
						str+='<param NAME="fontG" VALUE="'+ 255 +'"/>'
						str+='<param NAME="fontB" VALUE="'+ 255 +'"/>'

						str+='<param name="font_size" value="12"/>'
						str+='<param NAME="lan" VALUE="]]><xsl:value-of select="$wb_lang"/><![CDATA["/>'
						str+='</APPLET>'
						document.write(str);
					]]></SCRIPT>
					</td>
				</tr>
			</table>		
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
			<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
				<tr>
					<td height="10">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
				</tr>			
				<tr>
					<td>
					<table cellpadding="5" cellspacing="0" border="0" width="100%">
						<tr>
							<td valign="top">							
									<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
									str='<applet codebase="../applet" code="fb_answer.class" archive="fb_answer.jar" width="200" height="135" mayscript="mayscript" name="fbAnswer" >'
									
									str+='<param NAME="bgR" VALUE="'+ 255 +'"/>'
									str+='<param NAME="bgG" VALUE="'+ 255 +'"/>'
									str+='<param NAME="bgB" VALUE="'+ 255 +'"/>'  		
								  	str+='<param NAME="fieldR" VALUE="' + 255 + '"/>'
									str+='<param NAME="fieldG" VALUE="' + 255 + '"/>'
									str+='<param NAME="fieldB" VALUE="' + 255 + '"/>'
			
									str+='<param NAME="new_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[new.gif"/>'
									str+='<param NAME="new_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[new_over.gif"/>'  			
									str+='<param NAME="new_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[new_dis.gif"/>'  			
									str+='<param NAME="del_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del.gif"/>'
									str+='<param NAME="del_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del_over.gif"/>'  			
									str+='<param NAME="del_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del_dis.gif"/>'    	
									str+='<param NAME="lan" VALUE="]]><xsl:value-of select="$wb_lang"/><![CDATA["/>' 		
									str+='</applet>'
									document.write(str);
								]]></SCRIPT>
							</td>
							<td width="100%" valign="top">
									<table border="0" width="100%" cellpadding="5" cellspacing="0">
										<tr>
											<td nowrap="nowrap">											
												<span class="Text">
													<xsl:value-of select="$lab_answer"/><xsl:text> ：</xsl:text>
												</span>
											</td>
											<td width="100%">
												<input type="text" name="answer" style="width:100%;" class="wzb-inputText"/>												
											</td>
										</tr>
									</table>
									<table border="0" width="100%" cellpadding="5" cellspacing="0">
										<tr>
											<td width="130" valign="middle">												
												<input type="checkbox" id="cs_id" name="cs" value="checkbox" onClick="check_type()"/>
												<label for="cs_id">
												<span class="Text">
													<xsl:value-of select="$lab_case_sensitive"/>
												</span>
												</label>
											</td>
											<td valign="middle">
												<span class="Text">
													<xsl:value-of select="$lab_score"/><xsl:text> ：</xsl:text>
												</span>
												<input type="text" name="score" maxlength="5" style="width:30px;" class="wzb-inputText" size="5"/>
											</td>
										</tr>
										<tr>
											<td valign="middle">
												<input type="checkbox" id="ss_id" name="ss" value="checkbox" onClick="check_type()"/>
												<label for="ss_id">
												<span class="Text">
													<xsl:value-of select="$lab_space_sensitive"/>
												</span>
												</label>
											</td>
											<td valign="middle">
												<span class="Text">
													<xsl:value-of select="$lab_type"/><xsl:text> ：</xsl:text>
												</span> 
												<span class="Text">
													<select name="type" onChange="check_type()" class="Select">
														<option value="Text" selected="selected">
															<xsl:value-of select="$lab_text"/>
														</option>
														<option value="Number">
															<xsl:value-of select="$lab_number"/>
														</option>
													</select>
												</span>
											</td>
										</tr>
										</table>
										<table border="0" width="100%" cellpadding="5" cellspacing="0">
										<tr>
											<td nowrap="nowrap">
												<span class="Text">
													<xsl:value-of select="$lab_explanation"/><xsl:text>：</xsl:text>
												</span>												
											</td>
											<td width="100%">
												<input type="text" name="explanation" style="width:100%;" class="wzb-inputText"/>												
											</td>
										</tr>									
										<tr>
											<td colspan="2" align="center"><xsl:call-template name="wb_gen_button"><xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param><xsl:with-param name="wb_gen_btn_href">javascript:saveAnswer('<xsl:value-of select="$wb_lang"/>',document.frmXml)</xsl:with-param><xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param></xsl:call-template></td>
										</tr>
									</table>							
								</td>
							</tr>
							<tr>
								<td>				
									<span class="Text">
									<xsl:value-of select="$lab_max_char_allow"/><xsl:text>：</xsl:text>
									</span>
									<input class="wzb-inputText" type="text" name="maxChar" size="3" maxlength="5" onFocus="setBlankNum()" onBlur="checkMaxLength('{$wb_lang}')"/>								
								</td>
							</tr>							
						</table>
					</td>
				</tr>
				<tr>
					<td  height="10">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
				</tr>				
			</table>
		</xsl:if>
		 -->
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="es_question_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="es_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">將內容以HTML處理</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_remove_media">刪除媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_score">分數</xsl:with-param>
					<xsl:with-param name="lab_answer_with_file">允許學員提交檔案附件作為答案。</xsl:with-param>
					<xsl:with-param name="lab_model_answer">參考答案</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
			
				<xsl:call-template name="es_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">将内容以HTML处理</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒体文档</xsl:with-param>
					<xsl:with-param name="lab_remove_media">删除媒体文档</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改为</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒体文档</xsl:with-param>
					<xsl:with-param name="lab_score">分数</xsl:with-param>
					<xsl:with-param name="lab_answer_with_file">允许学员提交文档附件作为答案。</xsl:with-param>
					<xsl:with-param name="lab_model_answer">参考答案</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="es_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">Treat content as HTML</xsl:with-param>
					<xsl:with-param name="lab_keep_media">Keep the media file</xsl:with-param>
					<xsl:with-param name="lab_remove_media">Remove the media file</xsl:with-param>
					<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
					<xsl:with-param name="lab_media_file">Media file</xsl:with-param>
					<xsl:with-param name="lab_score">Score</xsl:with-param>
					<xsl:with-param name="lab_answer_with_file">Allow learner to submit file attachments as answer</xsl:with-param>
					<xsl:with-param name="lab_model_answer">Model answer</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="es_question_body_">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:param name="lab_as_htm"/>
		<xsl:param name="lab_keep_media"/>
		<xsl:param name="lab_remove_media"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_media_file"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_answer_with_file"/>
		<xsl:param name="lab_model_answer"/>
		<input type="hidden" name="inter_num_" value="1"/>
		<input type="hidden" name="inter_type_" value="ES"/>
		<input type="hidden" name="inter_length_" value="5000"/>
		<input type="hidden" name="inter_opt_num_" value="1"/>
		<input type="hidden" name="inter_opt_type_" value="ES"/>
		<input type="hidden" name="inter_opt_score_"/>
		<input type="hidden" name="inter_opt_exp_"/>
		<input type="hidden" name="que_submit_file_ind"/>
		<xsl:if test="$header = 'YES' ">
			<xsl:call-template name="ques_header_nav">
				<xsl:with-param name="mode" select="$mode"></xsl:with-param>
			</xsl:call-template>
					
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$mode='INS'">
							<xsl:value-of select="$lab_add_que"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_edit_que"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_que"/>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
			<xsl:with-param name="extra_td">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<td align="right">
							<xsl:call-template name="draw_que_type_box">
								<xsl:with-param name="cur_sel">ES</xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			<tr>
				<td align="center">
					<table cellpadding="5" cellspacing="0" border="0" width="100%">
						<tr align="left">
							<td colspan="2">
								<xsl:call-template name="kindeditor_panel">
									<xsl:with-param name="body">
										<xsl:for-each select="/question/body/text() | /question/body/html">
											<xsl:choose>
												<xsl:when test="name() = 'html'">
													<xsl:value-of disable-output-escaping="yes" select="."/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="."/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:for-each>
									</xsl:with-param>
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="fld_name">que_body_1</xsl:with-param>
									<xsl:with-param name="option">kindeditorMCOptions</xsl:with-param>
									<xsl:with-param name="rows">2</xsl:with-param>
								</xsl:call-template>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_model_answer"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td align="center">
					<table cellpadding="5" cellspacing="0" border="0" width="100%">
						<tr>
							<td>
								<textarea cols="40" style=" width:100%;" rows="10" name="inter_opt_exp_1" class="wzb-inputTextArea">
									<xsl:value-of select="/question/explanation/rationale/text()"/>
								</textarea>
							</td>
						</tr>
						<tr align="left">
							<td>
								<span class="Text">
									<xsl:value-of select="$lab_score"/>
									<xsl:text> ：</xsl:text>
								</span>
								<input type="text" class="wzb-inputText" name="inter_opt_score_1" style="width:50px;" value="{/question/body/interaction/@score}"/>
							</td>
						</tr>
						
					</table>
				</td>
			</tr>
			<tr>
				<td height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
<!-- 		<xsl:call-template name="wb_ui_line"> -->
<!-- 			<xsl:with-param name="width" select="$width"/> -->
<!-- 		</xsl:call-template> -->
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="mt_question_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="mt_question_body_">
					<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
					<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">將內容以HTML處理</xsl:with-param>
					<xsl:with-param name="lab_hint">提示?</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_remove_media">刪除媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_score">分數</xsl:with-param>
					<xsl:with-param name="lab_explanation">說明</xsl:with-param>
					<xsl:with-param name="lab_media_size">媒體檔案範圍</xsl:with-param>
					<xsl:with-param name="lab_width">闊度</xsl:with-param>
					<xsl:with-param name="lab_height">高度</xsl:with-param>
					<xsl:with-param name="lab_btn_ok">確定</xsl:with-param>
					<xsl:with-param name="lab_btn_add_opt">添加配對</xsl:with-param>
					<xsl:with-param name="lab_btn_del_opt">刪除配對</xsl:with-param>
					<xsl:with-param name="lab_source">來源</xsl:with-param>
					<xsl:with-param name="lab_target">目標</xsl:with-param>
					<xsl:with-param name="lab_answer_number">目標序號</xsl:with-param>
					<xsl:with-param name="lab_img_des">圖片規格建議：寬80px，高80px</xsl:with-param>
					<xsl:with-param name="lab_img_only_des">媒體檔案只支持上傳JPG, GIF, PNG格式的圖片</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="mt_question_body_">
					<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
					<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">将内容以HTML处理</xsl:with-param>
					<xsl:with-param name="lab_hint">提示？</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒体文档</xsl:with-param>
					<xsl:with-param name="lab_remove_media">删除媒体文档</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改为</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒体文档</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_score">分数</xsl:with-param>
					<xsl:with-param name="lab_explanation">说明</xsl:with-param>
					<xsl:with-param name="lab_media_size">媒体档案范围</xsl:with-param>
					<xsl:with-param name="lab_width">宽度</xsl:with-param>
					<xsl:with-param name="lab_height">高度</xsl:with-param>
					<xsl:with-param name="lab_btn_ok">OK</xsl:with-param>
					<xsl:with-param name="lab_btn_add_opt">添加配对</xsl:with-param>
					<xsl:with-param name="lab_btn_del_opt">删除配对</xsl:with-param>
					<xsl:with-param name="lab_source">来源</xsl:with-param>
					<xsl:with-param name="lab_target">目标</xsl:with-param>
					<xsl:with-param name="lab_answer_number">目标序号</xsl:with-param>
					<xsl:with-param name="lab_img_des">图片规格建议：宽80px，高80px</xsl:with-param>
					<xsl:with-param name="lab_img_only_des">媒体文档只支持上传JPG, GIF, PNG格式的图片</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="mt_question_body_">
					<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
					<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">Treat content as HTML</xsl:with-param>
					<xsl:with-param name="lab_hint">Hint ?</xsl:with-param>
					<xsl:with-param name="lab_keep_media">Keep the media file</xsl:with-param>
					<xsl:with-param name="lab_remove_media">Remove the media file</xsl:with-param>
					<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
					<xsl:with-param name="lab_media_file">Media file</xsl:with-param>
					<xsl:with-param name="lab_answer">Answer</xsl:with-param>
					<xsl:with-param name="lab_score">Score</xsl:with-param>
					<xsl:with-param name="lab_explanation">Explanation</xsl:with-param>
					<xsl:with-param name="lab_media_size">Media file dimension</xsl:with-param>
					<xsl:with-param name="lab_width">Width</xsl:with-param>
					<xsl:with-param name="lab_height">Height</xsl:with-param>
					<xsl:with-param name="lab_num_source">Number of source(s)</xsl:with-param>
					<xsl:with-param name="lab_num_target">Number of target(s)</xsl:with-param>
					<xsl:with-param name="lab_btn_ok">OK</xsl:with-param>
					<xsl:with-param name="lab_btn_add_opt">Add matching</xsl:with-param>
					<xsl:with-param name="lab_btn_del_opt">Delete matching</xsl:with-param>
					<xsl:with-param name="lab_source">Source</xsl:with-param>
					<xsl:with-param name="lab_target">Target</xsl:with-param>
					<xsl:with-param name="lab_answer_number">Answer</xsl:with-param>
					<xsl:with-param name="lab_img_des">Image size recommendation: width 80px, height 80px</xsl:with-param>
					<xsl:with-param name="lab_img_only_des">Media fileSupport JPG, GIF, PNG files only</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="mt_question_body_">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:param name="lab_as_htm"/>
		<xsl:param name="lab_hint"/>
		<xsl:param name="lab_keep_media"/>
		<xsl:param name="lab_remove_media"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_media_file"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_media_size"/>
		<xsl:param name="lab_width"/>
		<xsl:param name="lab_height"/>
		<xsl:param name="lab_num_source"/>
		<xsl:param name="lab_num_target"/>
		<xsl:param name="lab_btn_ok"/>
		<xsl:param name="lab_btn_add_opt"/>
		<xsl:param name="lab_btn_del_opt"/>
		<xsl:param name="lab_source"/>
		<xsl:param name="lab_target"/>
		<xsl:param name="lab_answer_number"/>
		<xsl:param name="lab_img_des" />
		<xsl:param name="lab_img_only_des" />
		
		<input type="hidden" name="inter_num_" value=""/>
		<input type="hidden" name="inter_type_" value=""/>
		<input type="hidden" name="num_source_" value=""/>
		<input type="hidden" name="inter_opt_num_" value=""/>
		<input type="hidden" name="inter_opt_body_" value=""/>
		<input type="hidden" name="inter_opt_exp_" value=""/>
		<input type="hidden" name="inter_opt_score_" value=""/>
		<input type="hidden" name="source_text_" value=""/>
		<input type="hidden" name="target_text_" value=""/>
		<input type="hidden" name="source_media_" value=""/>
		<input type="hidden" name="target_media_" value=""/>
		<input type="hidden" name="lab_score" value="{$lab_score}"/>
		<input type="hidden" name="lab_explanation" value="{$lab_explanation}"/>
		<input type="hidden" name="media_file_width_" maxlength="3" value="80" size="3" />
		<input type="hidden" name="media_file_height_" maxlength="3" size="3" value="80" />
		<input type="hidden" name="lab_answer_number" value="{$lab_answer_number}"/>
		<input type="hidden" name="rename" value="false"/>
		
	<!-- 	<xsl:value-of select="$header"/> -->
		
		<xsl:if test="$header = 'YES'">
			<xsl:call-template name="ques_header_nav">
				<xsl:with-param name="mode" select="$mode"></xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$mode='INS'">
							<xsl:value-of select="$lab_add_que"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_edit_que"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
	   </xsl:if>
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_que"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<xsl:if test="$mode = 'INS'"><td align="right">
				<xsl:call-template name="draw_que_type_box">
					<xsl:with-param name="cur_sel">MT</xsl:with-param>
				</xsl:call-template>
				</td></xsl:if>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg margin-top28">
			<tr>
				<td align="center">
					<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr>
							<td colspan="2">
								<div>
									<xsl:call-template name="kindeditor_panel">
										<xsl:with-param name="body">
										<xsl:for-each select="/question/body/text() | /question/body/html">
											<xsl:choose>
												<xsl:when test="name() = 'html'">
													<xsl:value-of disable-output-escaping="yes" select="."/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="."/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:for-each>
										</xsl:with-param>
										<xsl:with-param name="frm">document.frmXml</xsl:with-param>
										<xsl:with-param name="fld_name">que_textfield</xsl:with-param>
										<xsl:with-param name="option">kindeditorMCOptions</xsl:with-param>
										<xsl:with-param name="rows">2</xsl:with-param>
									</xsl:call-template>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

		<xsl:call-template name="wb_ui_space">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head"><xsl:with-param name="text" select="$lab_answer"/></xsl:call-template>
		<xsl:call-template name="wb_ui_line">
			<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}"  class="Bg">
			<tr>
				<td>
					<table border="0" cellpadding="5" cellspacing="0" width="100%">
						<tr>
							<td>
								<div id="answer_1" class="wzb-table-other-1">
									<table border="0" cellpadding="0" cellspacing="0" width="100%" style="text-align:left;">
										<tr>
											<td width="100%" valign="top">
												<div id="source_div">
													<div id="s_div_1">
														<table>
															<tr>
																<td width="2%"></td>
																<td class="TitleText" width="40%"><xsl:value-of select="$lab_source"/></td>
																<td class="TitleText" width="10%"><xsl:value-of select="$lab_target"/></td>
																<td class="TitleText" width="10%"><xsl:value-of select="$lab_score"/></td>
																<td width="30%"></td>
															</tr>
															<tr>
																<td >1.</td>
																<td >
																	<input type="text" name="source_text_1" style="width:350px;"  class="wzb-inputText" maxlength="300">
																		<xsl:attribute name="value">
																			<xsl:choose>
																				<xsl:when test="$mode='INS'">source 1</xsl:when>
																				<xsl:otherwise>
																					<xsl:value-of select="/question/body/source/item[@id = 1]/text"/>
																				</xsl:otherwise>
																			</xsl:choose>	
																		</xsl:attribute>
																	</input>
																</td>
																<td>
																	<input type="text" name="answer_num_1" style="width:50px;" class="wzb-inputText" maxlength="2">
																		<xsl:attribute name="value">
																			<xsl:choose>
																				<xsl:when test="$mode='INS'"></xsl:when>
																				<xsl:otherwise>
																					<xsl:variable name="obj" select="/question/outcome[feedback/@condition = 1]/@order"/>
																					<xsl:value-of select="$obj"/>
																				</xsl:otherwise>
																			</xsl:choose>
																		</xsl:attribute>
																	</input>
																</td>
																<td>
																	<input type="text" name="score_1" style="width:50px;" class="wzb-inputText" maxlength="3">
																		<xsl:attribute name="value">
																			<xsl:choose>
																				<xsl:when test="$mode='INS'"></xsl:when>
																				<xsl:otherwise>
																					<xsl:value-of select="/question/outcome/feedback[@condition = 1]/@score"/>
																				</xsl:otherwise>
																			</xsl:choose>
																		</xsl:attribute>
																	</input>
																</td>
																<td></td>
															</tr>
															<tr>
																<td>
																</td>
																<td>
																<table>
																	<tbody>
																		<tr>
																			<td>
																				<xsl:if test="/question/body/source/item[@id=1]/object">
																					<xsl:variable name="media_width" select="/question/body/media/@width"/>
																					<xsl:variable name="media_height" select="/question/body/media/@height"/>
																					<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																						<xsl:if test="/question/body/source/item[@id=1]/object">
																							<xsl:apply-templates select="/question/body/source/item[@id=1]/object"/>
																						</xsl:if>
																						<xsl:if test="text">
																							<span class="SmallText">
																								<xsl:call-template name="unescape_html_linefeed">
																									<xsl:with-param name="my_right_value" select="text"/>
																								</xsl:call-template>
																							</span>
																						</xsl:if>
																					</div>
																				</xsl:if>
																			</td>
																			<td>
																				<xsl:variable name="img_url" select="concat(//res_dir_url,/question/@id, '/', /question/body/source/item[@id=1]/object/@data)"/>
																				<xsl:variable name="img_name" select="/question/body/source/item[@id=1]/object/@data"/>
																				
																				<xsl:choose>
																					<xsl:when test="$img_name != ''">
																						<input type="radio" id="rdo_source_1_1" name="rdo_source_1" value="1" checked="checked" onClick="this.form.source_media_1.value='{$img_url}';this.form.tmp_source_media_1.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_source_1_1">
																								<xsl:value-of select="$lab_keep_media"/>
																							</label>
																						</span>
																						<br/>
																						<input type="radio" id="rdo_source_1_2" name="rdo_source_1" value="2" onClick="this.form.source_media_1.value='';this.form.tmp_source_media_1.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_source_1_2">
																								<xsl:value-of select="$lab_remove_media"/>
																							</label>
																						</span>
																						<br/>
																						<input type="radio" id="rdo_source_1_3" name="rdo_source_1" value="3" onclick="this.form.tmp_source_media_1.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_source_1_3">
																								<xsl:value-of select="$lab_change_to"/>
																							</label>
																						</span>
																						<br/>
																						<xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">tmp_source_media_1</xsl:with-param>
																							<xsl:with-param name="onclick">checkradio('rdo_source_1_3')</xsl:with-param>
																							<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
																						</xsl:call-template>
																						<span><xsl:value-of select="$lab_img_only_des" /></span>
																						<br/>
																						<span><xsl:value-of select="$lab_img_des" /></span>
																						<input type="hidden" name="source_media_1" value="{$img_url}"/>
																					</xsl:when>
																					<xsl:otherwise>
																						<xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">source_media_1</xsl:with-param>
											
																						</xsl:call-template>
																						<span>
																							<xsl:value-of select="$lab_img_only_des" />
																						</span><br/>
																						<span><xsl:value-of select="$lab_img_des" /></span>
																					</xsl:otherwise>
																				</xsl:choose>
																			</td>
																		</tr>
																	</tbody>
																</table>
																	
																</td>
															</tr>
														</table>
													</div>
													<div id="s_div_2" style="margin:10px 0;">
														<table>
															<tr>
																<td width="2%">2.</td>
																<td width="40%">
																	<input type="text" name="source_text_2" style="width:350px;"  class="wzb-inputText" maxlength="300">
																		<xsl:attribute name="value">
																			<xsl:choose>
																				<xsl:when test="$mode='INS'">source 2</xsl:when>
																				<xsl:otherwise>
																					<xsl:value-of select="/question/body/source/item[@id = 2]/text"/>
																				</xsl:otherwise>
																			</xsl:choose>	
																		</xsl:attribute>
																	</input>
																</td>
																<td width="10%">
																	<input type="text" name="answer_num_2" style="width:50px;" class="wzb-inputText" maxlength="2">
																		<xsl:attribute name="value">
																			<xsl:choose>
																				<xsl:when test="$mode='INS'"></xsl:when>
																				<xsl:otherwise>
																					<xsl:variable name="obj" select="/question/outcome[feedback/@condition = 2]/@order"/>
																					<xsl:value-of select="$obj"/>
																				</xsl:otherwise>
																			</xsl:choose>
																		</xsl:attribute>
																	</input>
																</td>
																<td width="10%">
																	<input type="text" name="score_2" style="width:50px;" class="wzb-inputText" maxlength="3">
																		<xsl:attribute name="value">
																			<xsl:choose>
																				<xsl:when test="$mode='INS'"></xsl:when>
																				<xsl:otherwise>
																					<xsl:value-of select="/question/outcome/feedback[@condition = 2]/@score"/>
																				</xsl:otherwise>
																			</xsl:choose>
																		</xsl:attribute>
																	</input>
																</td>
																<td width="30%"></td>
															</tr>
															<tr>
																<td></td>
																<td>
																	<table>
																	<tbody>
																		<tr>
																			<td>
																				<xsl:if test="/question/body/source/item[@id=2]/object">
																					<xsl:variable name="media_width" select="/question/body/media/@width"/>
																					<xsl:variable name="media_height" select="/question/body/media/@height"/>
																					<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																						<xsl:if test="/question/body/source/item[@id=2]/object">
																							<xsl:apply-templates select="/question/body/source/item[@id=2]/object"/>
																						</xsl:if>
																						<xsl:if test="text">
																							<span class="SmallText">
																								<xsl:call-template name="unescape_html_linefeed">
																									<xsl:with-param name="my_right_value" select="text"/>
																								</xsl:call-template>
																							</span>
																						</xsl:if>
																					</div>
																				</xsl:if>
																			</td>
																			<td>
																				<xsl:variable name="img_url" select="concat(//res_dir_url,/question/@id, '/', /question/body/source/item[@id=2]/object/@data)"/>
																				<xsl:variable name="img_name" select="/question/body/source/item[@id=2]/object/@data"/>
																				<xsl:choose>
																					<xsl:when test="$img_name != ''">
																						<input type="radio" id="rdo_source_2_1" name="rdo_source_2" value="1" checked="checked" onClick="this.form.source_media_2.value='{$img_url}';this.form.tmp_source_media_2.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_source_2_1">
																								<xsl:value-of select="$lab_keep_media"/>
																							</label>
																						</span>
																						<br/>
																						<input type="radio" id="rdo_source_2_2" name="rdo_source_2" value="2" onClick="this.form.source_media_2.value='';this.form.tmp_source_media_2.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_source_2_2">
																								<xsl:value-of select="$lab_remove_media"/>
																							</label>
																						</span>
																						<br/>
																						<input type="radio" id="rdo_source_2_3" name="rdo_source_2" value="3" onclick="this.form.tmp_source_media_2.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_source_2_3">
																								<xsl:value-of select="$lab_change_to"/>
																							</label>
																						</span>
																						<br/>
																						<xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">tmp_source_media_2</xsl:with-param>
																							<xsl:with-param name="onclick">checkradio('rdo_source_2_3')</xsl:with-param>
																							<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
																						</xsl:call-template>
																						<span>
																							<xsl:value-of select="$lab_img_only_des" />
																						</span>
																						<br/>
																						<span><xsl:value-of select="$lab_img_des" /></span>
			
																						<input type="hidden" name="source_media_2" value="{$img_url}"/>
																					</xsl:when>
																					<xsl:otherwise>
																						
																						<xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">source_media_2</xsl:with-param>
																						</xsl:call-template>
																						<span>
																							<xsl:value-of select="$lab_img_only_des" />
																						</span><br/>
																						<span><xsl:value-of select="$lab_img_des" /></span>
																						
																					</xsl:otherwise>
																				</xsl:choose>
																			</td>
																		</tr>
																	</tbody>
																	</table>
																</td>
															</tr>
														</table>
													</div>
													<xsl:if test="$mode='UPD'">
														<xsl:for-each select="/question/body/source/item[@id > 2]">
															<xsl:variable name="number" select="position() + 2"/>
															<div id="s_div_{$number}" style="padding:5px 0 0 0">
																<table>
																	<tr>
																		<td width="2%"><xsl:value-of select="$number"/>.</td>
																		<td width="40%">
																			<input type="text" name="source_text_{$number}" style="width:350px;" class="wzb-inputText" maxlength="300" >
																				<xsl:attribute name="value">
																					<xsl:value-of select="/question/body/source/item[@id = $number]/text"/>
																				</xsl:attribute>
																			</input>
																		</td>
																		<td width="10%">
																			<input type="text" name="answer_num_{$number}" style="width:50px;" class="wzb-inputText" maxlength="3">
																				<xsl:attribute name="value">
																					<xsl:choose>
																						<xsl:when test="$mode='INS'"></xsl:when>
																						<xsl:otherwise>
																							<xsl:variable name="obj" select="/question/outcome[feedback/@condition = $number]/@order"/>
																							<xsl:value-of select="$obj"/>
																						</xsl:otherwise>
																					</xsl:choose>
																				</xsl:attribute>
																			</input>
																		</td>
																		<td width="10%">
																			<input type="text" name="score_{$number}" style="width:50px;" class="wzb-inputText" maxlength="3">
																				<xsl:attribute name="value">
																					<xsl:value-of select="/question/outcome/feedback[@condition = $number]/@score"/>
																				</xsl:attribute>
																			</input>
																		</td>
																		<td width="30%"></td>
																	</tr>
																	<tr>
																		<td></td>
																		<td>
																			<table>
																			<tbody>
																				<tr>
																					<td>
																						<xsl:if test="/question/body/source/item[@id=$number]/object">
																							<xsl:variable name="media_width" select="/question/body/media/@width"/>
																							<xsl:variable name="media_height" select="/question/body/media/@height"/>
																							<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																								<xsl:if test="/question/body/source/item[@id=$number]/object">
																									<xsl:apply-templates select="/question/body/source/item[@id=$number]/object"/>
																								</xsl:if>
																								<xsl:if test="text">
																									<span class="SmallText">
																										<xsl:call-template name="unescape_html_linefeed">
																											<xsl:with-param name="my_right_value" select="text"/>
																										</xsl:call-template>
																									</span>
																								</xsl:if>
																							</div>
																						</xsl:if>
																					</td>
																					<td>
																					<xsl:variable name="img_url" select="concat(//res_dir_url,/question/@id, '/', /question/body/source/item[@id=$number]/object/@data)"/>
																					<xsl:variable name="img_name" select="/question/body/source/item[@id=$number]/object/@data"/>
																					<xsl:choose>
																						<xsl:when test="$img_name != ''">
																							<input type="radio" id="rdo_source_{$number}_1" name="rdo_source_{$number}" value="1" checked="checked" onClick="this.form.source_media_{$number}.value='{$img_url}';this.form.tmp_source_media_{$number}.disabled = false"/>
																							<span class="Text">
																								<label for="rdo_source_{$number}_1">
																									<xsl:value-of select="$lab_keep_media"/>
																								</label>
																							</span>
																							<br/>
																							<input type="radio" id="rdo_source_{$number}_2" name="rdo_source_{$number}" value="2" onClick="this.form.source_media_{$number}.value='';this.form.tmp_source_media_{$number}.disabled = false"/>
																							<span class="Text">
																								<label for="rdo_source_{$number}_2">
																									<xsl:value-of select="$lab_remove_media"/>
																								</label>
																							</span>
																							<br/>
																							<input type="radio" id="rdo_source_{$number}_3" name="rdo_source_{$number}" value="3" onclick="this.form.tmp_source_media_{$number}.disabled = false"/>
																							<span class="Text">
																								<label for="rdo_source_{$number}_3">
																									<xsl:value-of select="$lab_change_to"/>
																								</label>
																							</span>
																							<br/>
																							<xsl:call-template name="wb_gen_input_file">
																								<xsl:with-param name="name">tmp_source_media_<xsl:value-of select="$number"/></xsl:with-param>
																								<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
																								<xsl:with-param name="onclick">checkradio('rdo_source_<xsl:value-of select="$number"/>_3')</xsl:with-param>
																							</xsl:call-template>
																							<span>
																								<xsl:value-of select="$lab_img_only_des" />
																							</span>
																							<br/>
																							<span><xsl:value-of select="$lab_img_des" /></span>
																							<input type="hidden" name="source_media_{$number}" value="{$img_url}"/>
																						</xsl:when>
																						<xsl:otherwise>
																							
																							<xsl:call-template name="wb_gen_input_file">
																								<xsl:with-param name="name">source_media_<xsl:value-of select="$number"/></xsl:with-param>
																								<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
																							</xsl:call-template>
																							<span>
																								<xsl:value-of select="$lab_img_only_des" />
																							</span><br/>
																							<span ><xsl:value-of select="$lab_img_des" /></span>
													
																						</xsl:otherwise>
																					</xsl:choose>
																					</td>
																				</tr>
																			</tbody>
																		</table>
																		</td>
																	</tr>
																</table>
															</div>
														</xsl:for-each>
													</xsl:if>
												</div>
											</td>
										</tr>
										<tr>
											<td width="100%" valign="top">
												<div id="target_div">
													<div id="t_div_1">
														<table>
															<tr>
																<td width="2%"></td>
																<td width="40%" class="TitleText">
																	<xsl:value-of select="$lab_target"/>
																</td>
																<td width="10%"></td>
																<td width="10%"></td>
																<td width="30%"></td>
															</tr>
															<tr>
																<td>1.</td>
																<td>
																	<input type="text" name="target_text_1" style="width:350px;"  class="wzb-inputText" maxlength="300">
																		<xsl:attribute name="value">
																			<xsl:choose>
																				<xsl:when test="$mode='INS'">target 1</xsl:when>
																				<xsl:otherwise>
																					<xsl:value-of select="/question/body/interaction[@order = 1]/text"/>
																				</xsl:otherwise>
																			</xsl:choose>	
																		</xsl:attribute>
																	</input>
																</td>
															</tr>
															<tr>
																<td></td>
																<td>
																	<table>
																		<tbody>
																			<tr>
																				<td>
																					<xsl:if test="/question/body/interaction[@order=1]/object">
																						<xsl:variable name="media_width" select="/question/body/media/@width"/>
																						<xsl:variable name="media_height" select="/question/body/media/@height"/>
																						<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																							<xsl:if test="/question/body/interaction[@order=1]/object">
																								<xsl:apply-templates select="/question/body/interaction[@order=1]/object"/>
																							</xsl:if>
																							<xsl:if test="text">
																								<span class="SmallText">
																									<xsl:call-template name="unescape_html_linefeed">
																										<xsl:with-param name="my_right_value" select="text"/>
																									</xsl:call-template>
																								</span>
																							</xsl:if>
																						</div>
																					</xsl:if>
																				</td>
																				<td>
																				<xsl:variable name="img_url" select="concat(//res_dir_url,/question/@id, '/', /question/body/interaction[@order=1]/object/@data)"/>
																				<xsl:variable name="img_name" select="/question/body/interaction[@order=1]/object/@data"/>
																				<xsl:choose>
																					<xsl:when test="$img_name != ''">
																						<input type="radio" id="rdo_target_1_1" name="rdo_target_1" value="1" checked="checked" onClick="this.form.target_media_1.value='{$img_url}';this.form.tmp_target_media_1.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_target_1_1">
																								<xsl:value-of select="$lab_keep_media"/>
																							</label>
																						</span>
																						<br/>
																						<input type="radio" id="rdo_target_1_2" name="rdo_target_1" value="2" onClick="this.form.target_media_1.value='';this.form.tmp_target_media_1.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_target_1_2">
																								<xsl:value-of select="$lab_remove_media"/>
																							</label>
																						</span>
																						<br/>
																						<input type="radio" id="rdo_target_1_3" name="rdo_target_1" value="3" onclick="this.form.tmp_target_media_1.disabled = false"/>
																						<span class="Text">
																							<label for="rdo_target_1_3">
																								<xsl:value-of select="$lab_change_to"/>
																							</label>
																						</span>
																						<br/>
																						<xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">tmp_target_media_1</xsl:with-param>
																							<xsl:with-param name="onclick">checkradio('rdo_target_1_3')</xsl:with-param>
																							<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
																						</xsl:call-template>
																						<!-- <xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">target_media_1</xsl:with-param>		
																							<xsl:with-param name="value" select="$img_url"/>	
																						</xsl:call-template>																			
																						<xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">tmp_target_media_1</xsl:with-param>		
																							<xsl:with-param name="disabled">disabled</xsl:with-param>
																						</xsl:call-template> -->
																						<span>
																							<xsl:value-of select="$lab_img_only_des" />
																						</span>
																						<br/>
																						<span><xsl:value-of select="$lab_img_des" /></span>
																						<input type="hidden" name="target_media_1" value="{$img_url}"/>					
																					</xsl:when>
																					<xsl:otherwise>
																						
																						<xsl:call-template name="wb_gen_input_file">
																							<xsl:with-param name="name">target_media_1</xsl:with-param>
															
																						</xsl:call-template>
																						<span>
																							<xsl:value-of select="$lab_img_only_des" />
																						</span><br/>
																						<span><xsl:value-of select="$lab_img_des" /></span>
																					</xsl:otherwise>
																				</xsl:choose>
																			</td>
																			</tr>
																		</tbody>
																	</table>
																</td>
															</tr>
														</table>
													</div>
													
													<div id="t_div_2"  style="padding:10px 0 0 0">
													<table>
														<tr>
															<td width="2%">2.</td>
															<td width="40%">
																<input type="text" name="target_text_2" style="width:350px;"  class="wzb-inputText" maxlength="300">
																	<xsl:attribute name="value">
																		<xsl:choose>
																			<xsl:when test="$mode='INS'">target 2</xsl:when>
																			<xsl:otherwise>
																				<xsl:value-of select="/question/body/interaction[@order = 2]/text"/>
																			</xsl:otherwise>
																		</xsl:choose>	
																	</xsl:attribute>
																</input>
															</td>
															<td width="10%"></td>
															<td width="10%"></td>
															<td width="30%"></td>
														</tr>
														<tr>
															<td></td>
															<td>
																<table>
																	<tbody>
																		<tr>
																			<td>
																				<xsl:if test="/question/body/interaction[@order=2]/object">
																					<xsl:variable name="media_width" select="/question/body/media/@width"/>
																					<xsl:variable name="media_height" select="/question/body/media/@height"/>
																					<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																						<xsl:if test="/question/body/interaction[@order=2]/object">
																							<xsl:apply-templates select="/question/body/interaction[@order=2]/object"/>
																						</xsl:if>
																						<xsl:if test="text">
																							<span class="SmallText">
																								<xsl:call-template name="unescape_html_linefeed">
																									<xsl:with-param name="my_right_value" select="text"/>
																								</xsl:call-template>
																							</span>
																						</xsl:if>
																					</div>
																				</xsl:if>
																			</td>
																			<td>
																			<xsl:variable name="img_url" select="concat(//res_dir_url,/question/@id, '/', /question/body/interaction[@order=2]/object/@data)"/>
																			<xsl:variable name="img_name" select="/question/body/interaction[@order=2]/object/@data"/>
																			<xsl:choose>
																				<xsl:when test="$img_name != ''">
																					<input type="radio" id="rdo_target_2_1" name="rdo_target_2" value="1" checked="checked" onClick="this.form.target_media_2.value='{$img_url}';this.form.tmp_target_media_2.disabled = false "/>
																					<span class="Text">
																						<label for="rdo_target_2_1">
																							<xsl:value-of select="$lab_keep_media"/>
																						</label>
																					</span>
																					<br/>
																					<input type="radio" id="rdo_target_2_2" name="rdo_target_2" value="2" onClick="this.form.target_media_2.value='';this.form.tmp_target_media_2.disabled = false"/>
																					<span class="Text">
																						<label for="rdo_target_2_2">
																							<xsl:value-of select="$lab_remove_media"/>
																						</label>
																					</span>
																					<br/>
																					<input type="radio" id="rdo_target_2_3" name="rdo_target_2" value="3" onclick="this.form.tmp_target_media_2.disabled = false"/>
																					<span class="Text">
																						<label for="rdo_target_2_3">
																							<xsl:value-of select="$lab_change_to"/>
																						</label>
																					</span>
																					<br/>
																					<xsl:call-template name="wb_gen_input_file">
																						<xsl:with-param name="name">tmp_target_media_2</xsl:with-param>
																						<xsl:with-param name="onclick">checkradio('rdo_target_2_3')</xsl:with-param>
																						<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
																					</xsl:call-template>
																					<span >
																						<xsl:value-of select="$lab_img_only_des" />
																					</span><br/>
																					<span ><xsl:value-of select="$lab_img_des" /></span>
																					<input type="hidden" name="target_media_2" value="{$img_url}"/>
																				</xsl:when>
																				<xsl:otherwise>
																					
																					
																					<xsl:call-template name="wb_gen_input_file">
																						<xsl:with-param name="name">target_media_2</xsl:with-param>
										
																					</xsl:call-template>
																					<span>
																						<xsl:value-of select="$lab_img_only_des" />
																					</span><br/>
																					<span ><xsl:value-of select="$lab_img_des" /></span>
																				</xsl:otherwise>
																			</xsl:choose>
																		</td>
																		</tr>
																	</tbody>
																</table>
															</td>
														</tr>
													</table>
													</div>
													<xsl:if test="$mode='UPD'">
														<xsl:for-each select="/question/body/interaction[@order > 2]">
															<xsl:variable name="number" select="position() + 2"/>
															<div id="t_div_{$number}"  style="padding:5px 0 0 0">
																<table>
																	<tr>
																		<td width="2%"><xsl:value-of select="$number"/>.</td>
																		<td width="40%">
																			<input type="text" name="target_text_{$number}" style="width:350px;"  class="wzb-inputText" maxlength="300">
																				<xsl:attribute name="value">
																					<xsl:value-of select="/question/body/interaction[@order = $number]/text"/>
																				</xsl:attribute>
																			</input>
																		</td>
																		<td width="10%"></td>
																		<td width="10%"></td>
																		<td width="30%"></td>
																	</tr>
																	<tr>
																		<td></td>
																		<td>
																			<table>
																				<tbody>
																					<tr>
																						<td>
																							<xsl:if test="/question/body/interaction[@order=$number]/object">
																								<xsl:variable name="media_width" select="/question/body/media/@width"/>
																								<xsl:variable name="media_height" select="/question/body/media/@height"/>
																								<div STYLE="position:relative;width:{$media_width}px;height:{$media_height}px;border-width:1px;border-style:solid;overflow:auto">
																									<xsl:if test="/question/body/interaction[@order=$number]/object">
																										<xsl:apply-templates select="/question/body/interaction[@order=$number]/object"/>
																									</xsl:if>
																									<xsl:if test="text">
																										<span class="SmallText">
																											<xsl:call-template name="unescape_html_linefeed">
																												<xsl:with-param name="my_right_value" select="text"/>
																											</xsl:call-template>
																										</span>
																									</xsl:if>
																								</div>
																							</xsl:if>
																						</td>
																						<td>
																						<xsl:variable name="img_url" select="concat(//res_dir_url,/question/@id, '/', /question/body/interaction[@order=$number]/object/@data)"/>
																						<xsl:variable name="img_name" select="/question/body/interaction[@order=$number]/object/@data"/>
																						<xsl:choose>
																							<xsl:when test="$img_name != ''">
																								<input type="radio" id="rdo_target_{$number}_1" name="rdo_target_{$number}" value="1" checked="checked" onClick="this.form.target_media_{$number}.value='{$img_url}';this.form.tmp_target_media_{$number}.disabled = false"/>
																								<span class="Text">
																									<label for="rdo_target_{$number}_1">
																										<xsl:value-of select="$lab_keep_media"/>
																									</label>
																								</span>
																								<br/>
																								<input type="radio" id="rdo_target_{$number}_2" name="rdo_target_{$number}" value="2" onClick="this.form.target_media_{$number}.value='';this.form.tmp_target_media_{$number}.disabled = false"/>
																								<span class="Text">
																									<label for="rdo_target_{$number}_2">
																										<xsl:value-of select="$lab_remove_media"/>
																									</label>
																								</span>
																								<br/>
																								<input type="radio" id="rdo_target_{$number}_3" name="rdo_target_{$number}" value="3" onclick="this.form.tmp_target_media_{$number}.disabled = false"/>
																								<span class="Text">
																									<label for="rdo_target_{$number}_3">
																										<xsl:value-of select="$lab_change_to"/>
																									</label>
																								</span>
																								<br/>
																								<xsl:call-template name="wb_gen_input_file">
																									<xsl:with-param name="name">tmp_target_media_<xsl:value-of select="$number"/></xsl:with-param>
																									<!-- <xsl:with-param name="disabled">disabled</xsl:with-param> -->
																									<xsl:with-param name="onclick">checkradio('rdo_target_<xsl:value-of select="$number"/>_3')</xsl:with-param>
																								</xsl:call-template>
																								<span >
																									<xsl:value-of select="$lab_img_only_des" />
																								</span><br/>
																								<span><xsl:value-of select="$lab_img_des" /></span>
																								
																								<input type="hidden" name="target_media_{$number}" value="{$img_url}"/>
																							</xsl:when>
																							<xsl:otherwise>
																								
																								<xsl:call-template name="wb_gen_input_file">
																									<xsl:with-param name="name">target_media_<xsl:value-of select="$number"/></xsl:with-param>
																	
																								</xsl:call-template>
																								<span >
																									<xsl:value-of select="$lab_img_only_des" />
																								</span><br/>
																								<span><xsl:value-of select="$lab_img_des" /></span>
																							</xsl:otherwise>
																						</xsl:choose>
																					</td>
																				</tr>
																			</tbody>
																		</table>
																		</td>
																	</tr>
																</table>
															</div>
														</xsl:for-each>
													</xsl:if>
												</div>
											</td>
										</tr>
										<tr>
											<td style="padding:10px 20px">
												<table>
													<tr>
														<td width="20%">
															<xsl:call-template name="wb_gen_form_button">
																<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_add_opt" />
																<xsl:with-param name="wb_gen_btn_href">Javascript:mt.add_option()</xsl:with-param>
															</xsl:call-template>
														</td>
														<td width="20%">
															<div id="del_opt_btn">
																<xsl:attribute name="style">
																	<xsl:choose>
																		<xsl:when test="$mode='UPD' and count(/question/body/interaction) >= 3">display:''</xsl:when>
																		<xsl:otherwise>display:none</xsl:otherwise>
																	</xsl:choose>
																</xsl:attribute>
																<xsl:call-template name="wb_gen_form_button">
																	<xsl:with-param name="wb_gen_btn_name" select="$lab_btn_del_opt" />
																	<xsl:with-param name="wb_gen_btn_href">Javascript:mt.del_option(document.frmXml)</xsl:with-param>
																</xsl:call-template>
															</div>
														</td>
														<td></td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</div>
						</td>						
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<img border="0" height="10" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="tf_question_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="tf_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">將內容以HTML處理</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_allow_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_score">分數 :</xsl:with-param>
					<xsl:with-param name="lab_explanation">說明</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_remove_media">刪除媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改為: </xsl:with-param>
					<xsl:with-param name="lab_multi_and">多項選擇：全對才得分</xsl:with-param>
					<xsl:with-param name="lab_multi_or">多項選擇：答對一個即得分</xsl:with-param>
					<xsl:with-param name="lab_single_one">單項選擇：只有一個正確答案</xsl:with-param>
					<xsl:with-param name="lab_single_more">單項選擇：有多個正確答案</xsl:with-param>
					<xsl:with-param name="lab_true">是</xsl:with-param>
					<xsl:with-param name="lab_false">否</xsl:with-param>
					<xsl:with-param name="lab_correct_answer">正確答案 :</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="tf_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">将内容以HTML处理</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒体文档</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_allow_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_score">分数 :</xsl:with-param>
					<xsl:with-param name="lab_explanation">说明</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒体文档: </xsl:with-param>
					<xsl:with-param name="lab_remove_media">删除媒体文档: </xsl:with-param>
					<xsl:with-param name="lab_change_to">更改为: </xsl:with-param>
					<xsl:with-param name="lab_multi_and">多项选择: 全对才得分</xsl:with-param>
					<xsl:with-param name="lab_multi_or">多项选择: 每答对一个即得分</xsl:with-param>
					<xsl:with-param name="lab_single_one">单项选择: 只有一个正确答案</xsl:with-param>
					<xsl:with-param name="lab_single_more">单项选择: 有多个正确答案</xsl:with-param>
					<xsl:with-param name="lab_true">对</xsl:with-param>
					<xsl:with-param name="lab_false">错</xsl:with-param>
					<xsl:with-param name="lab_correct_answer">正确答案 :</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="tf_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">Treat content as HTML</xsl:with-param>
					<xsl:with-param name="lab_media_file">Media file</xsl:with-param>
					<xsl:with-param name="lab_answer">Answer</xsl:with-param>
					<xsl:with-param name="lab_allow_shuffle">Answers can be shuffled</xsl:with-param>
					<xsl:with-param name="lab_score">Score :</xsl:with-param>
					<xsl:with-param name="lab_keep_media">Keep the media file:</xsl:with-param>
					<xsl:with-param name="lab_remove_media">Remove the media file:</xsl:with-param>
					<xsl:with-param name="lab_change_to">Change to:</xsl:with-param>
					<xsl:with-param name="lab_explanation">Explanation</xsl:with-param>
					<xsl:with-param name="lab_multi_and">Multiple choices for all correct answers</xsl:with-param>
					<xsl:with-param name="lab_multi_or">Multiple choices for each correct answer</xsl:with-param>
					<xsl:with-param name="lab_single_one">Single choice with one correct answer</xsl:with-param>
					<xsl:with-param name="lab_single_more">Single choice with more than one correct answer</xsl:with-param>
					<xsl:with-param name="lab_true">True</xsl:with-param>
					<xsl:with-param name="lab_false">False</xsl:with-param>
					<xsl:with-param name="lab_correct_answer">Correct answer :</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="tf_question_body_">
	
		<xsl:param name="width"/>
		<xsl:param name="lab_as_htm"/>
		<xsl:param name="header"/>
		<xsl:param name="lab_media_file"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_allow_shuffle"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_keep_media"/>
		<xsl:param name="lab_multi_and"/>
		<xsl:param name="lab_multi_or"/>
		<xsl:param name="lab_single_one"/>
		<xsl:param name="lab_single_more"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_correct_answer"/>
		<xsl:param name="lab_remove_media"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="mode"/>
		<input type="hidden" name="inter_num_"/>
		<input type="hidden" name="inter_shuffle_"/>
		<input type="hidden" name="inter_score_"/>
		<input type="hidden" name="inter_type_"/>
		<input type="hidden" name="inter_opt_body_"/>
		<input type="hidden" name="inter_opt_media_"/>
		<input type="hidden" name="inter_opt_html_"/>
		<input type="hidden" name="inter_opt_score_"/>
		<input type="hidden" name="inter_opt_num_"/>
		<input type="hidden" name="inter_opt_exp_"/>
		<input type="hidden" name="inter_opt_cond_"/>
		<input type="hidden" name="inter_logic_"/>
		
		<xsl:if test="$header = 'YES' ">
			<xsl:call-template name="ques_header_nav">
				<xsl:with-param name="mode" select="$mode"></xsl:with-param>
			</xsl:call-template>
			
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$mode='INS'">
							<xsl:value-of select="$lab_add_que"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_edit_que"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>	
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_que"/>
			</xsl:with-param>
			<xsl:with-param name="extra_td">
				<xsl:if test="$mode = 'INS'"><td align="right">
				<xsl:call-template name="draw_que_type_box">
					<xsl:with-param name="cur_sel">TF</xsl:with-param>
				</xsl:call-template>
				</td></xsl:if>			
				</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
				<tr>
					<td height="10">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
				</tr>
				<tr>
					<td>
						<table cellpadding="5" cellspacing="0" border="0" width="100%">
							<tr align="left">
								<td colspan="2">
									<xsl:call-template name="kindeditor_panel">
										<xsl:with-param name="body">
											<xsl:for-each select="/question/body/text() | /question/body/html">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="."/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</xsl:with-param>
										<xsl:with-param name="frm">document.frmXml</xsl:with-param>
										<xsl:with-param name="fld_name">qst_text</xsl:with-param>
										<xsl:with-param name="option">kindeditorMCOptions</xsl:with-param>
										<xsl:with-param name="rows">2</xsl:with-param>
									</xsl:call-template>
									<!-- 
									<div>
										<textarea rows="6" name="qst_text" style="width:100%;" cols="50" wrap="virtual" class="wzb-inputTextArea">
											<xsl:for-each select="/question/body/text()|/question/body/html">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="."/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</textarea>
									</div>
									<div>
										<input type="checkbox" id="rdo_ck_qst_text" name="ck_qst_text">
											<xsl:if test="/question/body/html">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<span class="Text">
											<label for="rdo_ck_qst_text">
												<xsl:value-of select="$lab_as_htm"/>
											</label>
										</span>
									</div>
									 -->
								</td>
							</tr>
							<!-- 
						<xsl:call-template name="draw_media_row">
							<xsl:with-param name="lab_keep_media" select="$lab_keep_media"/>
							<xsl:with-param name="lab_remove_media" select="$lab_remove_media"/>
							<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
							<xsl:with-param name="lab_media_file" select="$lab_media_file"/>
						</xsl:call-template>
						 -->
						</table>
					</td>
				</tr>
				<tr>
					<td height="10">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
				</tr>
			</table>
		<xsl:if test="not($mod_type='SVY' or $mod_type_1='SVY')">
			<xsl:call-template name="wb_ui_space"/>
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_answer"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line"/>
			<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
				<tr>
					<td height="10">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</tr>
				<tr>
					<td>
						<table cellpadding="0" cellspacing="5" border="0" width="100%">
							<tr>
								<td align="right" width="30%">
									<span class="TitleText">
										<xsl:value-of select="$lab_correct_answer"/>
									</span>
								</td>
								<!-- 对 -->
								<td align="left" width="70%" style="padding-left:10px;" >
									<span class="Text">                                                                                                   
										<input  type="radio" value="1" id="rdo_que_ans01true" name="rdo_que_ans01" onClick="this.form.inter_opt_score_.value='#[|]0[|]';">
											<xsl:if test="//outcome/feedback[@condition='1']/@score > 0  or not(//outcome/feedback/@score)">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<xsl:text>&#160;</xsl:text>
										<label for="rdo_que_ans01true">
										<xsl:value-of select="$lab_true"/>
										</label>
									</span>
								</td>
							</tr>
							<!-- 错 -->
							<tr>
								<td>
									<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
								</td>
								<td align="left" style="padding-left:10px;">
									<span class="Text">
										<input type="radio" name="rdo_que_ans01" id="rdo_que_ans01false" value="2" onClick="this.form.inter_opt_score_.value='0[|]#[|]';">
											<xsl:if test="//outcome/feedback[@condition='2']/@score > 0 ">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<xsl:text>&#160;</xsl:text>
										<label for="rdo_que_ans01false">
										<xsl:value-of select="$lab_false"/>
										</label>
									</span>
								</td>
							</tr>
							<tr>
								<td align="right">
									<span class="TitleText">
										<xsl:value-of select="$lab_score"/>
									</span>
								</td>
								<td align="left" style="padding-left:10px;">
									<xsl:variable name="score">
										<xsl:choose>
											<xsl:when test="//outcome/feedback[@score]/@score">
												<xsl:value-of select="//outcome/feedback[@score]/@score"/>
											</xsl:when>
											<xsl:otherwise>1</xsl:otherwise>
										</xsl:choose>
									</xsl:variable>
									<input type="text" name="que_score" value="{$score}" size="2" maxlength="5" class="wzb-inputText"/>
								</td>
							</tr>
						</table>
						<xsl:choose>
							<xsl:when test="//outcome/feedback[@condition='1']/@score > 0   or  not(//outcome/feedback/@score)">
								<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">document.frmXml.inter_opt_score_.value='#[|]0[|]';</SCRIPT>
							</xsl:when>
							<xsl:otherwise>
								<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript">document.frmXml.inter_opt_score_.value='0[|]#[|]';</SCRIPT>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<tr>
					<td height="10">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</tr>				
			</table>
		</xsl:if>
		<input type="hidden" value="3" name="score_choice"/>
		<input type="hidden" name="ans_shuffle" value="1"/>	
		<xsl:if test="$mod_type='SVY' or $mod_type_1='SVY'">
			<input type="hidden" name="que_score" value="0"/>
		</xsl:if>
		<!-- <xsl:call-template name="wb_ui_space"/>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text" select="$lab_explanation"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td>
					<table cellpadding="5" border="0" width="100%" cellspacing="0">
						<tr>
							<td>
								<textarea rows="6" cols="50" name="exp_text" style="width:100%;" class="wzb-inputTextArea" wrap="virtual">
									<xsl:value-of select="/question/explanation/rationale[@id=1]"/>
								</textarea>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="10">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table> -->
	</xsl:template>

	<!-- =============================================================================================== -->
	<xsl:template name="sc_question_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:param name="sc_type"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="sc_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="sc_type" select="$sc_type"/>
					<xsl:with-param name="lab_as_htm">將內容以HTML處理</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_choices">選擇</xsl:with-param>
					<xsl:with-param name="lab_choice">選擇</xsl:with-param>
					<xsl:with-param name="lab_allow_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_score">分數</xsl:with-param>
					<xsl:with-param name="lab_explanation">說明</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_remove_media">刪除媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
					<xsl:with-param name="lab_multi_and">多項選擇: 全對才得分</xsl:with-param>
					<xsl:with-param name="lab_multi_or">多項選擇: 答對一個即得分</xsl:with-param>
					<xsl:with-param name="lab_single_one">單項選擇: 只有一個正確答案</xsl:with-param>
					<xsl:with-param name="lab_single_more">單項選擇: 有多個正確答案</xsl:with-param>
					<xsl:with-param name="lab_que_shuffle">此情景題的題目在生成測驗時會被打亂次序</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="sc_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="sc_type" select="$sc_type"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_as_htm">将内容以HTML处理</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒体文档</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_choices">选择</xsl:with-param>
					<xsl:with-param name="lab_choice">选择</xsl:with-param>
					<xsl:with-param name="lab_allow_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_score">分数</xsl:with-param>
					<xsl:with-param name="lab_explanation">说明</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒体文档</xsl:with-param>
					<xsl:with-param name="lab_remove_media">删除媒体文档</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改为</xsl:with-param>
					<xsl:with-param name="lab_multi_and">多项选择: 全对才得分</xsl:with-param>
					<xsl:with-param name="lab_multi_or">多项选择 : 每答对一个即得分</xsl:with-param>
					<xsl:with-param name="lab_single_one">单项选择: 只有一个正确答案</xsl:with-param>
					<xsl:with-param name="lab_single_more">单项选择: 有多个正确答案</xsl:with-param>
					<xsl:with-param name="lab_que_shuffle">此情景题的题目在生成测验时会被打乱次序</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="sc_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="sc_type" select="$sc_type"/>
					<xsl:with-param name="lab_as_htm">Treat content as HTML</xsl:with-param>
					<xsl:with-param name="lab_media_file">Media file</xsl:with-param>
					<xsl:with-param name="lab_answer">Answer</xsl:with-param>
					<xsl:with-param name="lab_choices">Choices</xsl:with-param>
					<xsl:with-param name="lab_choice">Choice</xsl:with-param>
					<xsl:with-param name="lab_allow_shuffle">Answers can be shuffled</xsl:with-param>
					<xsl:with-param name="lab_score">Score</xsl:with-param>
					<xsl:with-param name="lab_keep_media">Keep the media file</xsl:with-param>
					<xsl:with-param name="lab_remove_media">Remove the media file</xsl:with-param>
					<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
					<xsl:with-param name="lab_explanation">Explanation</xsl:with-param>
					<xsl:with-param name="lab_multi_and">Multiple response for all correct answers</xsl:with-param>
					<xsl:with-param name="lab_multi_or">Multiple response for each correct answer</xsl:with-param>
					<xsl:with-param name="lab_single_one">Single response with one correct answer</xsl:with-param>
					<xsl:with-param name="lab_single_more">Single response with more than one correct answer</xsl:with-param>
					<xsl:with-param name="lab_que_shuffle">The questions in this scenario will be shuffled when generating a test</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	
	<!-- =============================================================================================== -->	
	<xsl:template name="sc_question_body_">
	
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="lab_as_htm"/>
		<xsl:param name="lab_media_file"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_choices"/>
		<xsl:param name="lab_choice"/>
		<xsl:param name="lab_allow_shuffle"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_keep_media"/>
		<xsl:param name="lab_remove_media"/>
		<xsl:param name="lab_multi_and"/>
		<xsl:param name="lab_multi_or"/>
		<xsl:param name="lab_single_one"/>
		<xsl:param name="lab_single_more"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_que_shuffle"/>
		<xsl:param name="mode"/>
		<xsl:param name="sc_type"/>
		<input type="hidden" name="inter_num_" value="0"/>
		<input type="hidden" name="inter_opt_num_" value="0"/>
		<input type="hidden" name="sc_que_shuffle" value=""/>
		
		<xsl:call-template name="ques_header_nav">
			<xsl:with-param name="mode" select="$mode"></xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:value-of select="$lab_add_que"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_edit_que"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
				<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
					<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name" select="/question/header/title"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:que.get('<xsl:value-of select="/question/@id"/>')</xsl:with-param>
								<xsl:with-param name="class">NavLink</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:value-of select="$lab_add_que"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_edit_que"/>
					</xsl:otherwise>
				</xsl:choose>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:value-of select="$lab_add_que"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_edit_que"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_que"/>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
			<xsl:with-param name="extra_td">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<td align="right" width="{$width - 150}">
							<xsl:call-template name="draw_que_type_box">
								<xsl:with-param name="cur_sel"><xsl:value-of select="$sc_type"/></xsl:with-param>
							</xsl:call-template>
						</td>
					</xsl:when>
					<xsl:otherwise>
						<td align="right" width="{$width - 150}">
							<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						</td>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			<tr>
				<td align="center">
					<table cellpadding="5" cellspacing="0" border="0" width="100%">
						<tr align="left">
							<td colspan="2">
								<xsl:call-template name="kindeditor_panel">
									<xsl:with-param name="body">
										<xsl:for-each select="/question/body/text() | /question/body/html">
											<xsl:choose>
												<xsl:when test="name() = 'html'">
													<xsl:value-of disable-output-escaping="yes" select="."/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="."/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:for-each>
									</xsl:with-param>
									<xsl:with-param name="frm">document.frmXml</xsl:with-param>
									<xsl:with-param name="fld_name">qst_text</xsl:with-param>
									<xsl:with-param name="option">kindeditorMCOptions</xsl:with-param>
									<xsl:with-param name="rows">2</xsl:with-param>
								</xsl:call-template>
								<!-- 
								<div>
									<textarea class="wzb-inputTextArea" cols="50" style="width:100%;" rows="6" name="qst_text">
										<xsl:for-each select="/question/body/text() | /question/body/html">
											<xsl:choose>
												<xsl:when test="name() = 'html'">
													<xsl:value-of disable-output-escaping="yes" select="."/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="."/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:for-each>
									</textarea>
								</div>
								<div>
									<label for="_as_html">
										<input type="checkbox" name="ck_qst_text" id="_as_html">
											<xsl:if test="/question/body/html">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
										<span class="Text">
											<xsl:value-of select="$lab_as_htm"/>
										</span>
									</label>
								</div>
								 -->
							</td>
						</tr>
						<!-- 
						<xsl:call-template name="draw_media_row">
							<xsl:with-param name="lab_keep_media" select="$lab_keep_media"/>
							<xsl:with-param name="lab_remove_media" select="$lab_remove_media"/>
							<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
							<xsl:with-param name="lab_media_file" select="$lab_media_file"/>
						</xsl:call-template>
						 -->
					</table>
				</td>
			</tr>
			<xsl:if test="$sc_type != 'DSC'">
				<tr align="left">
					<td>
						<input type="checkbox" name="que_shuffle" id="que_shuffle">
							<xsl:if test="//allow_shuffle_ind/text()=1"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
						</input>
						<label for="que_shuffle"><xsl:value-of select="$lab_que_shuffle"/></label>
					</td>
				</tr>
			</xsl:if>
			<tr>
				<td height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
	</xsl:template>	
	<!-- =============================================================================================== -->
	<xsl:template name="mc_question_body">
		<xsl:param name="width"/>
		<xsl:param name="mode"/>
		<xsl:param name="header"/>
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="mc_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_que">題目</xsl:with-param>
					<xsl:with-param name="lab_as_htm">將內容以HTML處理</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_choices">選项</xsl:with-param>
					<xsl:with-param name="lab_choice">選擇</xsl:with-param>				
					<xsl:with-param name="lab_allow_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_score">分數</xsl:with-param>
					<xsl:with-param name="lab_explanation">說明</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_remove_media">刪除媒體檔案</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改為</xsl:with-param>
					<xsl:with-param name="lab_multi_and">多項選擇：全對才得分</xsl:with-param>
					<xsl:with-param name="lab_multi_or">多項選擇：答對一個即得分</xsl:with-param>
					<xsl:with-param name="lab_single_one">單項選擇：只有一個正確答案</xsl:with-param>
					<xsl:with-param name="lab_single_more">單項選擇：有多個正確答案</xsl:with-param>
					<xsl:with-param name="lab_add_option">添加選項</xsl:with-param>
					<xsl:with-param name="lab_correct_answer">正確答案</xsl:with-param>
					<xsl:with-param name="lab_answer_value">答案</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="mc_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_que">题目</xsl:with-param>
					<xsl:with-param name="lab_as_htm">将内容以HTML处理</xsl:with-param>
					<xsl:with-param name="lab_media_file">媒体文档</xsl:with-param>
					<xsl:with-param name="lab_answer">答案</xsl:with-param>
					<xsl:with-param name="lab_choices">选项</xsl:with-param>
					<xsl:with-param name="lab_choice">选择</xsl:with-param>			
					<xsl:with-param name="lab_allow_shuffle">可更改答案次序</xsl:with-param>
					<xsl:with-param name="lab_score">分数</xsl:with-param>
					<xsl:with-param name="lab_explanation">说明</xsl:with-param>
					<xsl:with-param name="lab_keep_media">保留媒体文档</xsl:with-param>
					<xsl:with-param name="lab_remove_media">删除媒体文档</xsl:with-param>
					<xsl:with-param name="lab_change_to">更改为</xsl:with-param>
					<xsl:with-param name="lab_multi_and">多项选择：全对才得分</xsl:with-param>
					<xsl:with-param name="lab_multi_or">多项选择：每答对一个即得分</xsl:with-param>
					<xsl:with-param name="lab_single_one">单项选择：只有一个正确答案</xsl:with-param>
					<xsl:with-param name="lab_single_more">单项选择：有多个正确答案</xsl:with-param>
					<xsl:with-param name="lab_add_option">添加选项</xsl:with-param>
					<xsl:with-param name="lab_correct_answer">正确答案</xsl:with-param>
					<xsl:with-param name="lab_answer_value">答案</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="mc_question_body_">
					<xsl:with-param name="width" select="$width"/>
					<xsl:with-param name="mode" select="$mode"/>
					<xsl:with-param name="header" select="$header"/>
					<xsl:with-param name="lab_que">Question</xsl:with-param>
					<xsl:with-param name="lab_as_htm">Treat content as HTML</xsl:with-param>
					<xsl:with-param name="lab_media_file">Media file</xsl:with-param>
					<xsl:with-param name="lab_answer">Answer</xsl:with-param>
					<xsl:with-param name="lab_choices">Choices</xsl:with-param>
					<xsl:with-param name="lab_choice">Choice</xsl:with-param>
					<xsl:with-param name="lab_allow_shuffle">Answers can be shuffled</xsl:with-param>
					<xsl:with-param name="lab_score">Score</xsl:with-param>
					<xsl:with-param name="lab_keep_media">Keep the media file</xsl:with-param>
					<xsl:with-param name="lab_remove_media">Remove the media file</xsl:with-param>
					<xsl:with-param name="lab_change_to">Change to</xsl:with-param>
					<xsl:with-param name="lab_explanation">Explanation</xsl:with-param>
					<xsl:with-param name="lab_multi_and">Multiple response for all correct answers</xsl:with-param>
					<xsl:with-param name="lab_multi_or">Multiple response for each correct answer</xsl:with-param>
					<xsl:with-param name="lab_single_one">Single response with one correct answer</xsl:with-param>
					<xsl:with-param name="lab_single_more">Single response with more than one correct answer</xsl:with-param>
					<xsl:with-param name="lab_add_option">Add option</xsl:with-param>
					<xsl:with-param name="lab_correct_answer">Is correct</xsl:with-param>
					<xsl:with-param name="lab_answer_value">Answer value</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="mc_question_body_">
		<xsl:param name="width"/>
		<xsl:param name="header"/>
		<xsl:param name="lab_as_htm"/>
		<xsl:param name="lab_media_file"/>
		<xsl:param name="lab_answer"/>
		<xsl:param name="lab_choices"/>
		<xsl:param name="lab_choice"/>
		<xsl:param name="lab_allow_shuffle"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="lab_explanation"/>
		<xsl:param name="lab_keep_media"/>
		<xsl:param name="lab_remove_media"/>
		<xsl:param name="lab_multi_and"/>
		<xsl:param name="lab_multi_or"/>
		<xsl:param name="lab_single_one"/>
		<xsl:param name="lab_single_more"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="mode"/>
		<xsl:param name="lab_add_option"/>
		<xsl:param name="lab_correct_answer"/>
		<xsl:param name="lab_answer_value"/>
		<xsl:param name="lab_que"/>
		<input type="hidden" name="inter_num_" value=""/>
		<input type="hidden" name="inter_shuffle_" value=""/>
		<input type="hidden" name="inter_score_" value=""/>
		<input type="hidden" name="inter_type_" value=""/>
		<input type="hidden" name="inter_opt_body_" value=""/>
		<input type="hidden" name="inter_opt_media_" value=""/>
		<input type="hidden" name="inter_opt_html_" value=""/>
		<input type="hidden" name="inter_opt_score_" value=""/>
		<input type="hidden" name="inter_opt_num_" value=""/>
		<input type="hidden" name="inter_opt_exp_" value=""/>
		<input type="hidden" name="inter_opt_cond_" value=""/>
		<input type="hidden" name="inter_logic_" value=""/>
		<xsl:if test="/question_form/container">
			<input type="hidden" name="container_res_id" value="{/question_form/container/@id}"/>
		</xsl:if>
	
	   <xsl:if test="$header != 'NO'  " >	   <!-- 弹出窗模式  不显示标题和上级菜单  对其他地方有影响可以去掉-->
		 <xsl:call-template name="ques_header_nav">
			<xsl:with-param name="mode" select="$mode"></xsl:with-param>
		</xsl:call-template> 
	    
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:value-of select="$lab_add_que"/>
					</xsl:when>
					<xsl:otherwise>
					   <xsl:value-of select="$lab_edit_que"/>
						<!-- <xsl:value-of select="/question/header/title"/> -->
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template> 
		</xsl:if>
	  
		 <xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_que"/>
			</xsl:with-param>
			<xsl:with-param name="width" select="$width"/>
			<xsl:with-param name="extra_td">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:if test="$mod_type!='FSC' and $mod_type!='DSC'">
							<td align="right">
								<xsl:call-template name="draw_que_type_box">
									<xsl:with-param name="cur_sel">MC</xsl:with-param>
								</xsl:call-template>
							</td>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<td align="right">
						</td>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table border="0" cellpadding="0" cellspacing="0" width="{$width}" class="Bg">
			<tr>
				<td height="10">
					<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
			<tr>
				<td align="center">
					<table>
						<tr align="left">
							<td colspan="2">
								<div>
									<xsl:call-template name="kindeditor_panel">
										<xsl:with-param name="body">
											<xsl:for-each select="/question/body/text() | /question/body/html">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="."/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</xsl:with-param>
										<xsl:with-param name="frm">document.frmXml</xsl:with-param>
										<xsl:with-param name="fld_name">qst_text</xsl:with-param>
										<xsl:with-param name="option">kindeditorMCOptionsUpd</xsl:with-param>
										<xsl:with-param name="rows">2</xsl:with-param>
									</xsl:call-template>
									<!-- 
									<textarea class="wzb-inputTextArea" cols="50" style="width:100%;" rows="6" name="qst_text">
										<xsl:for-each select="/question/body/text() | /question/body/html">
											<xsl:choose>
												<xsl:when test="name() = 'html'">
													<xsl:value-of disable-output-escaping="yes" select="."/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="."/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:for-each>
									</textarea>
									 -->
								</div>
								<!-- 
								<div>
									<label for="_as_html">
										<input type="checkbox" name="ck_qst_text" id="_as_html">
											<xsl:if test="/question/body/html"><xsl:attribute name="checked">checked</xsl:attribute></xsl:if>
										</input>
										<span class="Text">
											<xsl:value-of select="$lab_as_htm"/>
										</span>
									</label>
								</div>
								 -->
							</td>
						</tr>
						<!-- 
						<xsl:call-template name="draw_media_row">
							<xsl:with-param name="lab_keep_media" select="$lab_keep_media"/>
							<xsl:with-param name="lab_remove_media" select="$lab_remove_media"/>
							<xsl:with-param name="lab_change_to" select="$lab_change_to"/>
							<xsl:with-param name="lab_media_file" select="$lab_media_file"/>
						</xsl:call-template>
						 -->
					</table>
				</td>
			</tr>	
		</table>
		<xsl:choose>
			<xsl:when test="$is_evn_que = 'true'">
				<xsl:call-template name="svy_answer_form">
				<xsl:with-param name="width" select="$width"/>
				<xsl:with-param name="lab_choices" select="$lab_choices"/>
				<xsl:with-param name="lab_choice" select="$lab_choice"/>
				<xsl:with-param name="lab_score" select="$lab_score"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text" select="$lab_answer"/>
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width" select="$width"/>
			</xsl:call-template>
				<table>
					<tr>
						<td>
							<table>
								<tr>
									<td>
										<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
										str='<applet codebase="../applet" code="multiplechoice.class" archive="multiplechoice.jar" width="550" height="200" name="MULTIPLECHOICE" mayscript="mayscript">'
										str+='<param NAME="lang" VALUE="]]><xsl:value-of select="$encoding"/><![CDATA["/>'
										str+='<param NAME="bgR" VALUE="'+ 255 +'"/>'
										str+='<param NAME="bgG" VALUE="'+ 255 +'"/>' 			
										str+='<param NAME="bgB" VALUE="' + 255 + '"/>'
										str+='<param NAME="fontR" VALUE="' + 0 + '"/>'
										str+='<param NAME="fontG" VALUE="' + 0 + '"/>'
										str+='<param NAME="fontB" VALUE="' + 0 + '"/>'
										str+='<param NAME="fieldR" VALUE="' + 255 + '"/>'
										str+='<param NAME="fieldG" VALUE="' + 255 + '"/>'
										str+='<param NAME="fieldB" VALUE="' + 255 + '"/>'				
				
										str+='<param NAME="lan" VALUE="]]><xsl:value-of select="$wb_lang"/><![CDATA["/>' 
										str+='<param NAME="new_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[new.gif"/>'
										str+='<param NAME="new_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[new_over.gif"/>'  		
										str+='<param NAME="new_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[new_dis.gif"/>'  			
										str+='<param NAME="del_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del.gif"/>'
										str+='<param NAME="del_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del_over.gif"/>'  			
										str+='<param NAME="del_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del_dis.gif"/>'
										
										str+='<param NAME="up_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[up.gif"/>'
										str+='<param NAME="up_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[up_over.gif"/>'  			
										str+='<param NAME="up_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[up_dis.gif"/>'  			
										str+='<param NAME="down_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[down.gif"/>'
										str+='<param NAME="down_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[down_over.gif"/>'  			
										str+='<param NAME="down_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[down_dis.gif"/>'  			
										
										str+='<param NAME="showm_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[show_media.gif"/>'
										str+='<param NAME="showm_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[show_media_over.gif"/>'  			
										str+='<param NAME="showm_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[show_media_dis.gif"/>'  			
										str+='<param NAME="addm_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[add_media.gif"/>'
										str+='<param NAME="addm_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[add_media_over.gif"/>'  			
										str+='<param NAME="addm_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[add_media_dis.gif"/>'  			
										str+='<param NAME="delm_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del_media.gif"/>'
										str+='<param NAME="delm_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del_media_over.gif"/>'  			
										str+='<param NAME="delm_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[del_media_dis.gif"/>'  			
										str+='<param NAME="save_off" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[save.gif"/>'
										str+='<param NAME="save_over" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[save_over.gif"/>'  			
										str+='<param NAME="save_dis" VALUE="]]><xsl:value-of select="$applet_img_path"/><![CDATA[save_dis.gif"/>'
										str+='</applet>'	  
										//document.write(str);
									]]></SCRIPT>
									
									<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
										function addnewOption(){

											var count = document.frmXml.mc_option.length;
											var option = document.getElementById('new_option_template').innerHTML;
											var mc_option_editor_id = 'mc_option_editor_' + (count + 1);
											
											if(option.indexOf('mc_option_chx_template')){
												option = option.replace('mc_option_chx_template', 'mc_option_chx');
											}
											if(option.indexOf('mc_option_template')){
												option = option.replace('mc_option_template', 'mc_option');
											}
											if(option.indexOf('mc_option_editor_template')){
												option = option.replace('mc_option_editor_template', mc_option_editor_id);
											}
											if(option.indexOf('label_value')){
												option = option.replace('label_value', String.fromCharCode(64 + parseInt(count + 1)));
											}
							
											var div = 'new_option_' + (count + 1);
											document.getElementById(div).innerHTML = option;
											document.getElementById(div).style.display = 'block';
											
											var new_mc_option_editor = KindEditor.create('#' + mc_option_editor_id, kindeditorMCOptions);
											
											if(count == 4){
							                	mc_option_editor_5 = new_mc_option_editor;
							                }
							                if(count == 5){
							                	mc_option_editor_6 = new_mc_option_editor;
							                }
							                if(count == 6){
							                	mc_option_editor_7 = new_mc_option_editor;
							                }
							                if(count == 7){
							                	mc_option_editor_8 = new_mc_option_editor;
							                }
							                if(count == 8){
							                	mc_option_editor_9 = new_mc_option_editor;
							                }
							                if(count == 9){
							                	mc_option_editor_10 = new_mc_option_editor;
							                	document.getElementById('add_option_button').style.display = 'none';
							                }
										}
									]]></SCRIPT>
										<table width="100%">
											<tr align="left">
												<td width="20"></td>
												<td width="70"><xsl:value-of select="$lab_correct_answer" /></td>
												<td><span class="TitleText"><xsl:value-of select="$lab_answer_value" /></span></td>
											</tr>
										</table>  <!-- 修改选择题时调用   option已修改-->
										<xsl:if test="count(/question/body/interaction/option) > 0">
											<xsl:for-each select="/question/body/interaction/option">
												<xsl:variable name="id"><xsl:value-of select="@id" /></xsl:variable>
												<table width="100%">
													<tr valign="top" align="left">
														<td style="padding-top:5px;"><label id="option_label" name="option_label"><xsl:number value="@id" format="A" lang="en"/></label></td>
														<td align="center" width="80" style="padding-top:5px;">
															<input type="checkbox" name="mc_option_chx" style="margin-top:-2px;">
																<xsl:if test="/question/outcome/feedback[@condition = $id]/@score">
																	<xsl:attribute name="checked">checked</xsl:attribute>
																</xsl:if>
															</input>
														</td>
														<td style="padding:5px 0;">
															<xsl:call-template name="kindeditor_panel">
																<xsl:with-param name="editor_id">mc_option_editor_<xsl:value-of select="$id"/></xsl:with-param>
																<xsl:with-param name="body">
																	<xsl:choose>
																		<xsl:when test="html"><xsl:value-of select="html"/></xsl:when>
																		<xsl:otherwise><xsl:value-of select="text()"/></xsl:otherwise>
																	</xsl:choose>
																</xsl:with-param>
																<xsl:with-param name="frm">document.frmXml</xsl:with-param>
																<xsl:with-param name="fld_name">mc_option</xsl:with-param>
																<xsl:with-param name="option">kindeditorMCOptionsUpd</xsl:with-param>
																<xsl:with-param name="rows">2</xsl:with-param>
															</xsl:call-template>
														</td>
													</tr>
												</table>
											</xsl:for-each>
										</xsl:if>
										<table width="100%">
											<xsl:if test="not(/question/body/interaction/option) or count(/question/body/interaction/option) &lt;= 0">
												<xsl:call-template name="mc_option">
													<xsl:with-param name="number">1</xsl:with-param>
												</xsl:call-template>
											</xsl:if>
											<xsl:if test="not(/question/body/interaction/option) or count(/question/body/interaction/option) &lt;= 1">
												<xsl:call-template name="mc_option">
													<xsl:with-param name="number">2</xsl:with-param>
												</xsl:call-template>
											</xsl:if>
											<xsl:if test="not(/question/body/interaction/option) or count(/question/body/interaction/option) &lt;= 2">
												<xsl:call-template name="mc_option">
													<xsl:with-param name="number">3</xsl:with-param>
												</xsl:call-template>
											</xsl:if>
											<xsl:if test="not(/question/body/interaction/option) or count(/question/body/interaction/option) &lt;= 3">
												<xsl:call-template name="mc_option">
													<xsl:with-param name="number">4</xsl:with-param>
												</xsl:call-template>
											</xsl:if>
										</table>
										<div id="new_option_5" style="display: none;"></div>
										<div id="new_option_6" style="display: none;"></div>
										<div id="new_option_7" style="display: none;"></div>
										<div id="new_option_8" style="display: none;"></div>
										<div id="new_option_9" style="display: none;"></div>
										<div id="new_option_10" style="display: none;"></div>
										
										<div id="add_option_button" style="text-align: left;">
											<xsl:call-template name="wb_gen_form_button">
												<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_add_option" /></xsl:with-param>
												<xsl:with-param name="wb_gen_btn_href">javascript: addnewOption();</xsl:with-param>
											</xsl:call-template>
										</div>
										<div id="new_option_template" style="display: none;">
											<table width="100%">
												<tr valign="top" align="left">
													<td style="width:15px;"><label name="option_label" id="option_label">label_value</label></td>
													<td align="center" width="80"><input type="checkbox" name="mc_option_chx_template" style="margin-top:-2px;" /></td>
													<td style="padding:5px 0;">
														<textarea rows="2" cols="68" id="mc_option_editor_template" name="mc_option_template" />
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
								<tr align="left">
									<td style="padding:5px 0;">
										<!-- Calculate score in Survey -->
										<xsl:value-of select="$lab_score"/><xsl:text>：</xsl:text>
										<xsl:variable name="score">
											<xsl:choose>
												<xsl:when test="//outcome/feedback[@score]/@score">
													<xsl:value-of select="//outcome/feedback[@score]/@score"/>
												</xsl:when>
												<xsl:otherwise>1</xsl:otherwise>
											</xsl:choose>
										</xsl:variable>
										<input type="text" name="que_score" value="{$score}" size="2" maxlength="5" class="wzb-inputText"/>
										<xsl:text>&#160;</xsl:text>
										<select class="wzb-form-select" name="score_choice">
											<option value="1">
												<xsl:value-of select="$lab_multi_and"/>
											</option>
											<option value="2">
												<xsl:value-of select="$lab_multi_or"/>
											</option>
											<option value="3">
												<xsl:value-of select="$lab_single_one"/>
											</option>
											<!-- 
											<option value="4">
												<xsl:value-of select="$lab_single_more"/>
											</option>
											 -->
										</select>
									</td>
								</tr>
								<tr align="left">
									<td nowrap="nowrap">
										<label for="_shuffle">
											<input type="checkbox" name="ans_shuffle" value="1" id="_shuffle">
												<xsl:if test="//interaction/@shuffle = 'Y' ">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
											</input>
											<span class="Text">
												<xsl:value-of select="$lab_allow_shuffle"/>
											</span>
										</label>
									</td>
								</tr>					
							</table>
						</td>
					</tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="additional_information">
		<xsl:param name="width"/>
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="mode"/>
		<xsl:param name="upd_type" />
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="additional_information_">
					<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
					<xsl:with-param name="save_function"><xsl:value-of select="$save_function"/></xsl:with-param>
					<xsl:with-param name="cancel_function"><xsl:value-of select="$cancel_function"/></xsl:with-param>
					<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
					<xsl:with-param name="upd_type" select="$upd_type" />
					<xsl:with-param name="lab_add_info">其他資料</xsl:with-param>
					<xsl:with-param name="lab_title_keyword">標題</xsl:with-param>
					<xsl:with-param name="lab_evn_topic">*主題</xsl:with-param>
					<xsl:with-param name="lab_lang">*語言</xsl:with-param>
					<xsl:with-param name="lab_desc">簡介</xsl:with-param>
					<xsl:with-param name="lab_lang_gb">簡體中文</xsl:with-param>
					<xsl:with-param name="lab_lang_cn">繁體中文</xsl:with-param>
					<xsl:with-param name="lab_lang_en">英文</xsl:with-param>
					<xsl:with-param name="lab_diff">難度</xsl:with-param>
					<xsl:with-param name="lab_diff_easy">容易</xsl:with-param>
					<xsl:with-param name="lab_diff_normal">一般</xsl:with-param>
					<xsl:with-param name="lab_diff_hard">困難</xsl:with-param>
					<xsl:with-param name="lab_dur">*限時</xsl:with-param>
					<xsl:with-param name="lab_status">狀態</xsl:with-param>
					<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
					<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
					<xsl:with-param name="lab_personal">個人</xsl:with-param>
					<xsl:with-param name="lab_public">共享</xsl:with-param>
					<xsl:with-param name="lab_min">分鐘</xsl:with-param>
					<xsl:with-param name="lab_please_select">-請選擇-</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$wb_lang='gb'">
				<xsl:call-template name="additional_information_">
					<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
					<xsl:with-param name="save_function"><xsl:value-of select="$save_function"/></xsl:with-param>
					<xsl:with-param name="cancel_function"><xsl:value-of select="$cancel_function"/></xsl:with-param>
					<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
					<xsl:with-param name="upd_type" select="$upd_type" />
					<xsl:with-param name="lab_add_info">其他资料</xsl:with-param>
					<xsl:with-param name="lab_title_keyword">标题</xsl:with-param>
					<xsl:with-param name="lab_evn_topic">*主题</xsl:with-param>
					<xsl:with-param name="lab_lang">*语言</xsl:with-param>
					<xsl:with-param name="lab_desc">简介</xsl:with-param>
					<xsl:with-param name="lab_lang_cn">繁体中文</xsl:with-param>
					<xsl:with-param name="lab_lang_gb">简体中文</xsl:with-param>
					<xsl:with-param name="lab_lang_en">英文</xsl:with-param>
					<xsl:with-param name="lab_diff">难度</xsl:with-param>
					<xsl:with-param name="lab_diff_easy">容易</xsl:with-param>
					<xsl:with-param name="lab_diff_normal">一般</xsl:with-param>
					<xsl:with-param name="lab_diff_hard">困难</xsl:with-param>
					<xsl:with-param name="lab_dur">*时限</xsl:with-param>
					<xsl:with-param name="lab_status">状态</xsl:with-param>
					<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
					<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
					<xsl:with-param name="lab_personal">个人</xsl:with-param>
					<xsl:with-param name="lab_public">共享</xsl:with-param>
					<xsl:with-param name="lab_min">分钟</xsl:with-param>
					<xsl:with-param name="lab_please_select">-请选择-</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">保存</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="additional_information_">
					<xsl:with-param name="width"><xsl:value-of select="$width"/></xsl:with-param>
					<xsl:with-param name="save_function"><xsl:value-of select="$save_function"/></xsl:with-param>
					<xsl:with-param name="cancel_function"><xsl:value-of select="$cancel_function"/></xsl:with-param>
					<xsl:with-param name="mode"><xsl:value-of select="$mode"/></xsl:with-param>
					<xsl:with-param name="upd_type" select="$upd_type" />
					<xsl:with-param name="lab_add_info">Basic information</xsl:with-param>
					<xsl:with-param name="lab_title_keyword">Title</xsl:with-param>
					<xsl:with-param name="lab_evn_topic">*Topic</xsl:with-param>
					<xsl:with-param name="lab_lang">*Language</xsl:with-param>
					<xsl:with-param name="lab_desc">Description</xsl:with-param>
					<xsl:with-param name="lab_lang_cn">Traditional chinese</xsl:with-param>
					<xsl:with-param name="lab_lang_gb">Simplified chinese</xsl:with-param>
					<xsl:with-param name="lab_lang_en">English</xsl:with-param>
					<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
					<xsl:with-param name="lab_diff_easy">Easy</xsl:with-param>
					<xsl:with-param name="lab_diff_normal">Normal</xsl:with-param>
					<xsl:with-param name="lab_diff_hard">Hard</xsl:with-param>
					<xsl:with-param name="lab_dur">*Duration</xsl:with-param>
					<xsl:with-param name="lab_status">Status</xsl:with-param>
					<xsl:with-param name="lab_status_on">Published</xsl:with-param>
					<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
					<xsl:with-param name="lab_personal">Personal</xsl:with-param>
					<xsl:with-param name="lab_public">Public</xsl:with-param>
					<xsl:with-param name="lab_min">min(s)</xsl:with-param>
					<xsl:with-param name="lab_please_select">-Please select-</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
					<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="additional_information_">
		<xsl:param name="save_function"/>
		<xsl:param name="cancel_function"/>
		<xsl:param name="mode"/>
		<xsl:param name="upd_type" />
		<xsl:param name="lab_add_info"/>
		<xsl:param name="lab_title_keyword"/>
		<xsl:param name="lab_evn_topic"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_lang_en"/>
		<xsl:param name="lab_lang_cn"/>
		<xsl:param name="lab_lang_gb"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_diff_easy"/>
		<xsl:param name="lab_diff_normal"/>
		<xsl:param name="lab_diff_hard"/>
		<xsl:param name="lab_dur"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_personal"/>
		<xsl:param name="lab_public"/>
		<xsl:param name="lab_min"/>
		<xsl:param name="lab_please_select"/>
		<xsl:param name="width"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:call-template name="wb_ui_space">
		<xsl:with-param name="width" select="$width"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_head"><xsl:with-param name="text" select="$lab_add_info"/><xsl:with-param name="width" select="$width"/></xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_title_keyword"/> ：
				</td>
				<td class="wzb-form-control">
					<input type="text" name="que_title" style="width:400px;" class="wzb-inputText" maxlength="255">
						<xsl:attribute name="value"><xsl:value-of disable-output-escaping="yes" select="/question/header/title"/></xsl:attribute>
					</input>
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_desc"/><xsl:text> ：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<textarea rows="6" cols="50" style="width:400px;"   name="que_desc" class="wzb-inputTextArea"><xsl:value-of disable-output-escaping="yes" select="/question/header/desc"/></textarea>
				</td>
			</tr>
			<!--add this line-->
			<!--<xsl:if test="$upd_type = $edit_type">-->
			<xsl:choose>
				<xsl:when test="$is_evn_que = 'false'">
					<xsl:if test="$upd_type = $edit_type">
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_diff"/> ：
						</td>
						<td class="wzb-form-control">
							<select class="wzb-select">
								<xsl:attribute name="name">que_difficulty</xsl:attribute>
								<option>
									<xsl:attribute name="value">1</xsl:attribute>
									<xsl:if test="/question/header/@difficulty='1'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$lab_diff_easy"/>
								</option>
								<option>
									<xsl:attribute name="value">2</xsl:attribute>
									<xsl:if test="/question/header/@difficulty='2' or not(/question/header/@difficulty)">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$lab_diff_normal"/>
								</option>
								<option>
									<xsl:attribute name="value">3</xsl:attribute>
									<xsl:if test="/question/header/@difficulty='3'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$lab_diff_hard"/>
								</option>
							</select>
						</td>
					</tr>
					</xsl:if>
					<xsl:if test="not($upd_type = $edit_type)">
						<input type="hidden" name="que_difficulty" value="{/question/header/@difficulty}" />
					</xsl:if>
					<input size="5">
						<xsl:attribute name="class">wzb-inputText</xsl:attribute>
						<xsl:attribute name="type">hidden</xsl:attribute>
						<xsl:attribute name="name">que_duration</xsl:attribute>
						<xsl:attribute name="size">8</xsl:attribute>
						<xsl:attribute name="value">
						<xsl:choose>
							<xsl:when test="/question/header/@duration != '' "><xsl:value-of select="/question/header/@duration"/>
					        </xsl:when>
					        <xsl:otherwise><xsl:text>1.0</xsl:text></xsl:otherwise>
					   </xsl:choose>
					</xsl:attribute>
					</input>
					<input value="CW" name="que_privilege" type="hidden"/>
					<xsl:if test="$upd_type = $edit_type">
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_status"/> ：
						</td>
						<td class="wzb-form-control">
							<select class="wzb-select">
								<xsl:attribute name="name">que_status</xsl:attribute>
								<option>
									<xsl:attribute name="value">ON</xsl:attribute>
									<xsl:if test="/question/header/@status='ON'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$lab_status_on"/>
								</option>
								<option>
									<xsl:attribute name="value">OFF</xsl:attribute>
									<xsl:if test="/question/header/@status='OFF'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="$lab_status_off"/>
								</option>
							</select>
						</td>
					</tr>
					</xsl:if>
					<xsl:if test="not($upd_type = $edit_type)">
						<input type="hidden" name="que_status" value="{/question/header/@status}" />
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="encoding">
						<xsl:choose>
							<xsl:when test="/question/@language">
								<xsl:value-of select="/question/@language"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="//cur_usr/@encoding"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<input name="res_lan" type="hidden" value="{$encoding}"/>
					<input name="que_status" type="hidden" value="ON"/>
				</xsl:otherwise>
			</xsl:choose>
			<tr>
				<td width="20%" height="10">
				</td>
				<td width="80%" height="10" align="left" class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_ok"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:value-of select="$save_function"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name">
					<xsl:value-of select="$lab_g_form_btn_cancel"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">
					<xsl:value-of select="$cancel_function"/>
				</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
			</xsl:call-template>
		</div>
		<xsl:choose>
			<xsl:when test="$mode='UPD'">
				<input type="hidden" name="cmd" value="upd_q"/>
				<input type="hidden" name="que_id" value="{$que_id}"/>
				<input type="hidden" name="que_timestamp" value="{$que_timestamp}"/>
				<input type="hidden" name="que_obj_id" value="{$que_obj_id}"/>
			</xsl:when>
			<xsl:when test="$mode='INS'">
				<xsl:choose>
					<xsl:when test="$is_evn_que = 'true'">
						<input type="hidden" name="cmd" value="ins_suq"/>
					</xsl:when>
					<xsl:otherwise>
						<input type="hidden" name="cmd" value="ins_q"/>
					</xsl:otherwise>
				</xsl:choose>
				<input type="hidden" name="que_obj_id" value="{$id_obj}"/>
			</xsl:when>
			<xsl:when test="$mode='PST'">
				<input type="hidden" name="cmd" value="ins_q"/>
				<input type="hidden" name="que_timestamp" value="{$que_timestamp}"/>
				<input type="hidden" name="copy_media_from" value="{$que_id}"/>
				<script><![CDATA[
				document.write('<input type = "hidden" name="que_obj_id" value="' + getParentUrlParam('obj_id') +'">')
			]]></script>
			</xsl:when>
		</xsl:choose>
		<input type="hidden" name="url_next" value="javascript:parent.location.reload()"/>
		<input type="hidden" name="url_success" vaue="javascript:gen_get_cookie('url_success')"/>
		<input type="hidden" name="url_failure" value="javascript:parent.self.location.reload()"/>
		<input type="hidden" name="que_body_" value=""/>
		<input type="hidden" name="que_media_" value=""/>
		<input type="hidden" name="que_html_" value="Y"/>
		<input type="hidden" name="que_diff_" value=""/>
		<input type="hidden" name="que_dur_" value=""/>
		<input type="hidden" name="que_folder_" value="CW"/>
		<input type="hidden" name="que_status_" value=""/>
		<input type="hidden" name="que_title_" value=""/>
		<input type="hidden" name="que_desc_" value=""/>
		<input type="hidden" name="que_hint_" value=""/>
		<input type="hidden" name="res_lan_" value=""/>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="draw_header">
		<xsl:choose>
			<xsl:when test="$wb_lang='ch'">
				<xsl:call-template name="dh_lang_ch"/>
			</xsl:when>
			<xsl:when test="$wb_lang ='gb'">
				<xsl:call-template name="dh_lang_gb"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="dh_lang_en"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="dh_lang_ch">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
			<xsl:with-param name="res_search_result">搜索結果</xsl:with-param>
			<xsl:with-param name="lab_adm_home">管理員平台</xsl:with-param>
			<xsl:with-param name="lab_syb_manager">資源結構圖</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_gb">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
			<xsl:with-param name="res_search_result">检索结果</xsl:with-param>
			<xsl:with-param name="lab_adm_home">管理员平台</xsl:with-param>
			<xsl:with-param name="lab_syb_manager">资源结构图</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="dh_lang_en">
		<xsl:call-template name="draw_header_content">
			<xsl:with-param name="lab_res_manager">Knowledge manager</xsl:with-param>
			<xsl:with-param name="res_search_result">Search result</xsl:with-param>
			<xsl:with-param name="lab_adm_home">Administrator home</xsl:with-param>
			<xsl:with-param name="lab_syb_manager">Knowledge manager</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="draw_header_content">
		<xsl:param name="lab_res_manager"/>
		<xsl:param name="res_search_result"/>
		<xsl:param name="lab_adm_home"/>
		<xsl:param name="lab_syb_manager"/>
		<xsl:call-template name="wb_ui_hdr"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//question/header/title">
						<xsl:value-of select="//question/header/title"/>
					</xsl:when>
					<xsl:when test="//resource/body/title">
						<xsl:value-of select="//resource/body/title"/>
					</xsl:when>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
					<a href="javascript:window.location.href=wb_utils_get_cookie('search_result_url')" class="NavLink">
						<xsl:value-of select="$res_search_result"/>
					</a>
					<xsl:text>	&#160;&gt;&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="//question/header/title">
							<xsl:value-of select="//question/header/title"/>
						</xsl:when>
						<xsl:when test="//resource/body/title">
							<xsl:value-of select="//resource/body/title"/>
						</xsl:when>
					</xsl:choose>
				</span>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="svy_answer_form">
		<xsl:param name="width"/>
		<xsl:param name="lab_choices"/>
		<xsl:param name="lab_choice"/>
		<xsl:param name="lab_score"/>
		<xsl:call-template name="wb_ui_head"><xsl:with-param name="text" select="$lab_choices"/><xsl:with-param name="width" select="$width"/></xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label" style="padding:5px;">
					<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '265')"/> ：
				</td>
				<td colspan="2" class="wzb-form-control">
					<script><![CDATA[
						function setQueMcType(mc_type) {
							var que_mc_type = document.getElementById('que_mc_type');
							que_mc_type.value = mc_type;
						}
					]]></script>
					<xsl:variable name="que_mc_logic" select="/question/body/interaction[@order='1']/@logic"/>
					<input type="radio" id="rdo_que_mc_type_01" name="rdo_que_mc_type" onclick="setQueMcType('SINGLE');">
						<xsl:if test="not($que_mc_logic) or $que_mc_logic = 'SINGLE'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<label for="rdo_que_mc_type_01">
						<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '685')"/>
					</label>
					<xsl:text> </xsl:text>
					<input type="radio" id="rdo_que_mc_type_02" name="rdo_que_mc_type" onclick="setQueMcType('OR');" style="margin-left:10px;">
						<xsl:if test="$que_mc_logic = 'OR'">
							<xsl:attribute name="checked">checked</xsl:attribute>
						</xsl:if>
					</input>
					<label for="rdo_que_mc_type_02">
						<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '689')"/>
					</label>
					<input type="hidden" name="que_mc_type" id="que_mc_type" value="SINGLE"/>
				</td>
			</tr>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="require">true</xsl:with-param><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">1</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="require">true</xsl:with-param><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">2</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">3</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">4</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">5</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">6</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">7</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">8</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">9</xsl:with-param></xsl:call-template>
			<xsl:call-template name="svy_choice_form"><xsl:with-param name="lab_choice" select="$lab_choice"/><xsl:with-param name="lab_score" select="$lab_score"/><xsl:with-param name="choice_num">10</xsl:with-param></xsl:call-template>			
		</table>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="svy_choice_form">
		<xsl:param name="lab_choice"/>
		<xsl:param name="lab_score"/>
		<xsl:param name="require"/>
		<xsl:param name="choice_num"/>
		<xsl:variable name="option_id" select="/question/body/interaction[@order='1']/option[@id=$choice_num and not(@other_option_ind)]/@id"/>
		<input type="hidden" name="max_choice_count" value="10"/>
		<tr>
			<td class="wzb-form-label">
				<xsl:if test="$require = 'true'"><span class="wzb-form-star">*</span></xsl:if>
				<xsl:value-of select="$lab_choice"/>&#160;<xsl:value-of select="$choice_num"/> ：
			</td>
			<td width="30%" style="padding:10px 0 10px 10px; color:#333;">
				<input class="wzb-inputText" name="choice_{$choice_num}_body" value="{/question/body/interaction[@order='1']/option[@id=$option_id]}" maxlength="50"/>
			</td>
			<td width="50%">
				<xsl:value-of select="$lab_score"/>
				<xsl:text>&#160;</xsl:text>
				<xsl:variable name="score">
					<xsl:if test="/question/outcome[@order='1']/feedback[@condition = $option_id]">
						<xsl:choose>
							<xsl:when test="/question/outcome[@order='1']/feedback[@condition = $option_id]/@score">
								<xsl:value-of select="/question/outcome[@order='1']/feedback[@condition = $option_id]/@score"/>
							</xsl:when>
							<xsl:otherwise>0</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:variable>
				<input class="wzb-inputText" name="choice_{$choice_num}_score" size="3" maxlength="4" value="{$score}"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="svy_choice_form_other">
		<xsl:param name="lab_other">
			<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '697')"/>
		</xsl:param>
		<xsl:param name="lab_score"/>
		<xsl:param name="require"/>
		<xsl:param name="choice_num"/>
		<input type="hidden" name="max_choice_count" value="10"/>
		<script><![CDATA[
			function changeOtherOption(obj, choice_id) {
				var other_body = document.getElementById('choice_' + choice_id + '_body');
				var other_score = document.getElementById('choice_' + choice_id + '_score');
				if(obj.checked == true) {
					other_body.disabled = false;
					other_score.disabled = false;
				} else {
					other_body.disabled = true;
					other_score.disabled = true;
				}
			}
		]]></script>
		<xsl:variable name="option_id" select="/question/body/interaction[@order='1']/option[@other_option_ind='true']/@id"/>
		<input name="lab_other_option" type="hidden" value="{$lab_other}"/>
		<tr>
			<td width="20%" align="right">
				<input type="checkbox" name="has_other_option" onclick="changeOtherOption(this, '{$choice_num}')" value="true">
					<xsl:if test="string-length($option_id) &gt; 0 ">
						<xsl:attribute name="checked">checked</xsl:attribute>
					</xsl:if>
				</input>
				<span class="TitleText">
					<xsl:if test="$require = 'true'">*</xsl:if>
					<xsl:value-of select="$lab_other"/>&#160;:</span>
			</td>
			<td width="30%">
				<input class="wzb-inputText" id="choice_{$choice_num}_body" name="choice_{$choice_num}_body" value="{/question/body/interaction[@order='1']/option[@other_option_ind='true']}" maxlength="50">
						<xsl:if test="string-length($option_id) = 0 ">
							<xsl:attribute name="disabled">disabled</xsl:attribute>
						</xsl:if>
				</input>
			</td>

			<td width="50%">
				<xsl:value-of select="$lab_score"/>
				<xsl:text>&#160;</xsl:text>
				<xsl:variable name="score">
					<xsl:if test="/question/outcome[@order='1']/feedback[@condition = $option_id]">
						<xsl:choose>
							<xsl:when test="/question/outcome[@order='1']/feedback[@condition = $option_id]/@score">
								<xsl:value-of select="/question/outcome[@order='1']/feedback[@condition = $option_id]/@score"/>
							</xsl:when>
							<xsl:otherwise>0</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
				</xsl:variable>
				<input class="wzb-inputText" id="choice_{$choice_num}_score" name="choice_{$choice_num}_score" size="3" maxlength="4" value="{$score}">
					<xsl:if test="string-length($option_id) = 0 ">
						<xsl:attribute name="disabled">disabled</xsl:attribute>
					</xsl:if>
				</input>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================================================== -->
	<xsl:template name="draw_que_type_box">
		<xsl:param name="cur_sel">MC</xsl:param>
		<select class="wzb-select" name="que_type" onChange="changeQueType(this.options[this.selectedIndex].value)">
			<option value="MC">
				<xsl:if test="$cur_sel = 'MC'">
					<xsl:attribute name="selected">selected</xsl:attribute>
				</xsl:if>
				<xsl:value-of select="$lab_mc"/>
			</option>			
			<option value="FB">
				<xsl:if test="$cur_sel = 'FB'">
					<xsl:attribute name="selected">selected</xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="$is_evn_que = 'true'">
						<xsl:value-of select="$lab_fb_evn"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$lab_fb"/>
					</xsl:otherwise>
				</xsl:choose>
			</option>
			<xsl:if test="$is_evn_que = 'false'">
				<option value="MT">
					<xsl:if test="$cur_sel = 'MT'">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$lab_mt"/>
				</option>
				<option value="TF" >
					<xsl:if test="$cur_sel = 'TF'">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$lab_tf"/>
				</option>	
				<option value="ES" >
					<xsl:if test="$cur_sel = 'ES'">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$lab_es"/>
				</option>					
				<!-- <option value="FSC">   屏蔽动态情景题
					<xsl:if test="$cur_sel = 'FSC'">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$lab_fixed_sc"/>
				</option>
				<option value="DSC">
					<xsl:if test="$cur_sel = 'DSC'">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$lab_dna_sc"/>
				</option> -->
			</xsl:if>					
		</select>
	</xsl:template>
	<!-- =============================================================================================== -->	
	<xsl:template name="draw_media_row">
		<xsl:param name="lab_keep_media"/>
		<xsl:param name="lab_remove_media"/>
		<xsl:param name="lab_change_to"/>
		<xsl:param name="lab_media_file"/>
		<xsl:param name="lab_media_file_support"/>
		<xsl:choose>
			<xsl:when test="/question/body/object">
				<tr>
					<td nowrap="nowrap" valign="top" align="center">
						<xsl:apply-templates select="/question/body/object"/>
					</td>
				</tr>
				<tr>
					<td valign="top">
						<input type="radio" id="rdo_que_media01_1" name="rdo_que_media01" value="1" checked="checked" onClick="this.form.que_media.value='{/question/body/object/@data}';this.form.tmp_que_media01.disabled = true"/>
						<span class="Text">
							<label for="rdo_que_media01_1">
								<xsl:value-of select="$lab_keep_media"/>
							</label>
						</span>
						<br/>
						<input type="radio" id="rdo_que_media01_2" name="rdo_que_media01" value="2" onClick="this.form.que_media.value='';this.form.tmp_que_media01.disabled = true"/>
						<span class="Text">
							<label for="rdo_que_media01_2">
								<xsl:value-of select="$lab_remove_media"/>
							</label>
						</span>
						<br/>
						<input type="radio" id="rdo_que_media01_3" name="rdo_que_media01" value="3" onclick="this.form.tmp_que_media01.disabled = false"/>
						<span class="Text">
							<label for="rdo_que_media01_3">
								<xsl:value-of select="$lab_change_to"/>
							</label>
						</span>
							<xsl:call-template name="wb_gen_input_file">
								<xsl:with-param name="name">tmp_que_media01</xsl:with-param>
								<xsl:with-param name="disabled">disabled</xsl:with-param>
								<xsl:with-param name="onBlur">doAutoCheck(this,this.form.rdo_que_media01[2], this.form.rdo_que_media01[0], this.form.que_media)</xsl:with-param>
								<xsl:with-param name="onfocus">this.form.rdo_que_media01[2].checked=true</xsl:with-param>
							</xsl:call-template>
						<input type="hidden" name="que_media" value="{/question/body/object/@data}"/>
						<xsl:if test="$lab_media_file_support != ''">
							<br/>
							<span class="Text"><xsl:value-of select="$lab_media_file_support"/></span>
						</xsl:if>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<tr>
					<td>
					     <div >
					      <div style="Float:left">
							<xsl:value-of select="$lab_media_file"/>
							<xsl:text> ：</xsl:text>
							</div>
							<div style="Float:left">
							      <xsl:call-template name="wb_gen_input_file">
									<xsl:with-param name="name">que_media</xsl:with-param>
								</xsl:call-template></div>
						 </div>		
					</td>
				</tr>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="mc_option">
		<xsl:param name="number"/>
		<tr valign="top" align="left">
			<td><label id="option_label" name="option_label"><xsl:number value="$number" format="A" lang="en"/></label></td>
			<td align="center" width="80"><input type="checkbox" name="mc_option_chx" style="margin-top:-2px;" /></td>
			<td style="padding:0 0 5px 0;">
				<xsl:call-template name="kindeditor_panel">
					<xsl:with-param name="editor_id">mc_option_editor_<xsl:value-of select="$number" /></xsl:with-param>
					<xsl:with-param name="frm">document.frmXml</xsl:with-param>
					<xsl:with-param name="fld_name">mc_option</xsl:with-param>
					<xsl:with-param name="option">kindeditorMCOptions</xsl:with-param>
					<xsl:with-param name="rows">2</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="ques_header_nav">
		
		<xsl:param name="mode"></xsl:param>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<span class="NavLink">
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:for-each select="//question_form/objective/path/node">
							<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
								<xsl:value-of select="."/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						</xsl:for-each>
						<a href="javascript:obj.manage_obj_lst('','{//question_form/objective/@id}','','','')" class="NavLink">
							<xsl:value-of select="//question_form/objective/desc"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="//question_form/objective/@id"/>','','','false')</xsl:with-param>
									<xsl:with-param name="class">NavLink</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
					
						<xsl:for-each select="//header/objective/path/node">
							<a href="javascript:obj.manage_obj_lst('','{@id}','','','false')" class="NavLink">
								<xsl:value-of select="."/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						</xsl:for-each>
						<a href="javascript:obj.manage_obj_lst('','{//header/objective/@id}','','','false')" class="NavLink">
							<xsl:value-of select="//header/objective/desc"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name" select="$mange_source"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:obj.show_obj_lst('','<xsl:value-of select="//header/objective/@id"/>','','','false')</xsl:with-param>
									<xsl:with-param name="class">NavLink</xsl:with-param>
						</xsl:call-template>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="//header/title"/>
					    
					</xsl:otherwise>
				</xsl:choose>
				<xsl:choose>
					<xsl:when test="$mode='INS'">
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_add_que"/>
					</xsl:when>
				</xsl:choose>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		
	</xsl:template>
	
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	 
	<xsl:import href="../wb_const.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="../utils/escape_doub_quo.xsl"/>
	<xsl:import href="../utils/escape_all.xsl"/>
	<xsl:import href="../utils/wb_utils.xsl"/>
	<xsl:import href="../utils/wb_utils_button.xsl"/>
	<xsl:import href="../utils/wb_ui_head.xsl"/>
	<xsl:import href="../utils/wb_ui_line.xsl"/>
	<xsl:import href="../utils/wb_gen_form_button.xsl"/>
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../utils/wb_init_lab.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>	
	<xsl:import href="../cust/wb_cust_const.xsl"/>
    
	<!-- =============================================================== -->
<!-- 	<xsl:variable name="_mc" select="java:com.cw.wizbank.util.LangLabel.getValue('lab_mc')"/> 
	<xsl:variable name="_fb" select="java:com.cw.wizbank.util.LangLabel.getValue('lab_fb')"/> 
	<xsl:variable name="_mt" select="java:com.cw.wizbank.util.LangLabel.getValue('lab_mt')"/>
	<xsl:variable name="_tf" select="java:com.cw.wizbank.util.LangLabel.getValue('lab_tf')"/> 
	<xsl:variable name="_es" select="java:com.cw.wizbank.util.LangLabel.getValue('lab_es')"/>  -->
<!-- 	<xsl:variable name="_title" select="java:com.cw.wizbank.util.LangLabel.getValue('BS006')"/>  -->
	<!-- =============================================================== -->
	<!-- =============================================================== -->
	<xsl:template name="show_test_player_many">
		<xsl:call-template name="wb_init_lab">
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:variable name="submit_test">
		<xsl:choose>
			<xsl:when test="//page_variant[@submitTest='true']">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hasESFileSubmit">
		<xsl:choose>
			<xsl:when test="count(//question/header[@que_submit_file_ind = 'Y']) != 0">true</xsl:when>
			<xsl:otherwise>true</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="sg_mc_cnt" select="count($quiz/question[header/@type = 'MC' and body/interaction/@logic='SINGLE'])"/>
	<xsl:variable name="sg_mc_sum" select="sum($quiz/question/header[@type = 'MC' and ../body/interaction/@logic='SINGLE']/@score)"/>
	<xsl:variable name="mt_mc_cnt" select="count($quiz/question[header/@type = 'MC' and (body/interaction/@logic='AND' or body/interaction/@logic='OR')])"/>
	<xsl:variable name="mt_mc_sum" select="sum($quiz/question/header[@type = 'MC' and (../body/interaction/@logic='AND' or ../body/interaction/@logic='OR')]/@score)"/>
	<xsl:variable name="mt_mc_start" select="$sg_mc_cnt"/>
	<xsl:variable name="fb_cnt" select="count($quiz/question[header/@type = 'FB'])"/>
	<xsl:variable name="fb_sum" select="sum($quiz/question/header[@type = 'FB']/@score)"/>
	<xsl:variable name="fb_start" select="$mt_mc_start+$mt_mc_cnt"/>
	<xsl:variable name="mt_cnt" select="count($quiz/question[header/@type = 'MT'])"/>
	<xsl:variable name="mt_sum" select="sum($quiz/question/header[@type = 'MT']/@score)"/>
	<xsl:variable name="mt_start" select="$fb_start+$fb_cnt"/>
	<xsl:variable name="tf_cnt" select="count($quiz/question[header/@type = 'TF'])"/>
	<xsl:variable name="tf_sum" select="sum($quiz/question/header[@type = 'TF']/@score)"/>
	<xsl:variable name="tf_start" select="$mt_start+$mt_cnt"/>
	<xsl:variable name="es_cnt" select="count($quiz/question[header/@type = 'ES'])"/>
	<xsl:variable name="es_sum" select="sum($quiz/question/header[@type = 'ES']/@score)"/>
	<xsl:variable name="es_start" select="$tf_start+$tf_cnt"/>
	<xsl:variable name="fsc_cnt" select="count($quiz/question[header/@type = 'FSC']/question_list/question)"/>
	<xsl:variable name="fsc_sum" select="sum($quiz/question[header/@type = 'FSC']/@score)"/>
	<xsl:variable name="fsc_start" select="$es_start+$es_cnt"/>
	<xsl:variable name="dsc_cnt" select="count($quiz/question[header/@type = 'DSC']/question_list/question)"/>
	<xsl:variable name="dsc_sum" select="sum($quiz/question[header/@type = 'DSC']/@score)"/>
	<xsl:variable name="dsc_start" select="$fsc_start+$fsc_cnt"/>
	<xsl:variable name="que_title_width">998</xsl:variable>
	<xsl:variable name="show_answer_ind" select="$quiz/header/@show_answer_ind"/>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="test_player">
			<xsl:with-param name="lab_help">幫助</xsl:with-param>
			<xsl:with-param name="lab_reset">重置</xsl:with-param>
			<xsl:with-param name="lab_true">是</xsl:with-param>
			<xsl:with-param name="lab_false">否</xsl:with-param>
			<xsl:with-param name="lab_add_attachment">加入附件</xsl:with-param>
			<xsl:with-param name="lab_complete">完成</xsl:with-param>
			<xsl:with-param name="lab_time_left">剩餘時間</xsl:with-param>
			<xsl:with-param name="lab_examinee_name">姓名</xsl:with-param>
			<xsl:with-param name="lab_examinee_id">用户名</xsl:with-param>
			<xsl:with-param name="lab_examinee_usg">试题名</xsl:with-param>
			<xsl:with-param name="lab_prev">上一題</xsl:with-param>
			<xsl:with-param name="lab_next">下一題</xsl:with-param>
			<xsl:with-param name="lab_submit">遞交</xsl:with-param>
			<xsl:with-param name="lab_btn_save">保存並暫停</xsl:with-param>
			<xsl:with-param name="lab_help_intr">（說明：把適當的項目拖曳至右邊）</xsl:with-param>
			<xsl:with-param name="lab_questionnum">題目總數</xsl:with-param>
			<xsl:with-param name="lab_ans_here">你的答案</xsl:with-param>
			<xsl:with-param name="lab_delete_all">刪除所有</xsl:with-param>
			<xsl:with-param name="lab_delete">刪除</xsl:with-param>
			<xsl:with-param name="lab_next_unanswer">下一未回答</xsl:with-param>
			<xsl:with-param name="lab_next_mark">下一已標記</xsl:with-param>
			<xsl:with-param name="lab_mark">添加標記</xsl:with-param>
			<xsl:with-param name="lab_unmark">取消標記</xsl:with-param>
			<xsl:with-param name="lab_unattempt">未回答</xsl:with-param>
			<xsl:with-param name="lab_already_answer">已回答</xsl:with-param>
			<xsl:with-param name="lab_already_mark">已標記</xsl:with-param>
			<xsl:with-param name="lab_option_size">選項大小</xsl:with-param>
			<xsl:with-param name="lab_option_size_1">小</xsl:with-param>
			<xsl:with-param name="lab_option_size_2">中</xsl:with-param>
			<xsl:with-param name="lab_option_size_3">大</xsl:with-param>
			<xsl:with-param name="lab_option_source">來源</xsl:with-param>
			<xsl:with-param name="lab_option_target">目標</xsl:with-param>
			<xsl:with-param name="lab_total_score">試卷總分</xsl:with-param>
			<xsl:with-param name="lab_select_que">題目選擇</xsl:with-param>
			<xsl:with-param name="lab_prev_page">前十題</xsl:with-param>
			<xsl:with-param name="lab_next_page">後十題</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="lang_gb">
		<xsl:call-template name="test_player">
			<xsl:with-param name="lab_help">帮助</xsl:with-param>
			<xsl:with-param name="lab_reset">重置</xsl:with-param>
			<xsl:with-param name="lab_true">对</xsl:with-param>
			<xsl:with-param name="lab_false">错</xsl:with-param>
			<xsl:with-param name="lab_add_attachment">加入附件</xsl:with-param>
			<xsl:with-param name="lab_complete">完成</xsl:with-param>
			<xsl:with-param name="lab_time_left">剩余时间</xsl:with-param>
			<xsl:with-param name="lab_examinee_name">姓名</xsl:with-param>
			<xsl:with-param name="lab_examinee_id">用户名</xsl:with-param>
			<xsl:with-param name="lab_examinee_usg">试题名</xsl:with-param>
			<xsl:with-param name="lab_prev">上一题</xsl:with-param>
			<xsl:with-param name="lab_next">下一题</xsl:with-param>
			<xsl:with-param name="lab_submit">提交</xsl:with-param>
			<xsl:with-param name="lab_btn_save">保存并暂停</xsl:with-param>
			<xsl:with-param name="lab_help_intr">（说明：把适当的选项拖曳至右边）</xsl:with-param>
			<xsl:with-param name="lab_questionnum">题目总数</xsl:with-param>
			<xsl:with-param name="lab_ans_here">您的答案</xsl:with-param>
			<xsl:with-param name="lab_delete_all">删除所有</xsl:with-param>
			<xsl:with-param name="lab_delete">删除</xsl:with-param>
			<xsl:with-param name="lab_next_unanswer">下一未回答</xsl:with-param>
			<xsl:with-param name="lab_next_mark">下一已标签</xsl:with-param>
			<xsl:with-param name="lab_mark">添加标签</xsl:with-param>
			<xsl:with-param name="lab_unmark">取消标签</xsl:with-param>
			<xsl:with-param name="lab_unattempt">未回答</xsl:with-param>
			<xsl:with-param name="lab_already_answer">已回答</xsl:with-param>
			<xsl:with-param name="lab_already_mark">已标记</xsl:with-param>
			<xsl:with-param name="lab_option_size">选项大小</xsl:with-param>
			<xsl:with-param name="lab_option_size_1">小</xsl:with-param>
			<xsl:with-param name="lab_option_size_2">中</xsl:with-param>
			<xsl:with-param name="lab_option_size_3">大</xsl:with-param>
			<xsl:with-param name="lab_option_source">来源</xsl:with-param>
			<xsl:with-param name="lab_option_target">目标</xsl:with-param>
			<xsl:with-param name="lab_total_score">试卷总分</xsl:with-param>
			<xsl:with-param name="lab_select_que">题目选择</xsl:with-param>
			<xsl:with-param name="lab_prev_page">前十题</xsl:with-param>
			<xsl:with-param name="lab_next_page">后十题</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="lang_en">
		<xsl:call-template name="test_player">
			<xsl:with-param name="lab_help">Help</xsl:with-param>
			<xsl:with-param name="lab_reset">Reset</xsl:with-param>
			<xsl:with-param name="lab_true">True</xsl:with-param>
			<xsl:with-param name="lab_false">False</xsl:with-param>
			<xsl:with-param name="lab_add_attachment">Add Attachment</xsl:with-param>
			<xsl:with-param name="lab_complete">Complete</xsl:with-param>
			<xsl:with-param name="lab_time_left">Remaining Time</xsl:with-param>
			<xsl:with-param name="lab_examinee_name">姓名</xsl:with-param>
			<xsl:with-param name="lab_examinee_id">用户名</xsl:with-param>
			<xsl:with-param name="lab_examinee_usg">试题名</xsl:with-param>
			<xsl:with-param name="lab_prev">Previous</xsl:with-param>
			<xsl:with-param name="lab_next">Next</xsl:with-param>
			<xsl:with-param name="lab_submit">Submit</xsl:with-param>
			<xsl:with-param name="lab_btn_save">Save and Suspend</xsl:with-param>
			<xsl:with-param name="lab_help_intr">(Instruction:Drag the appoproiate item(s) to the right hand side.)</xsl:with-param>
			<xsl:with-param name="lab_questionnum">Total Questions</xsl:with-param>
			<xsl:with-param name="lab_ans_here">Your Answer</xsl:with-param>
			<xsl:with-param name="lab_delete_all">Delete All</xsl:with-param>
			<xsl:with-param name="lab_delete">Delete</xsl:with-param>
			<xsl:with-param name="lab_next_unanswer">Next Unattempt</xsl:with-param>
			<xsl:with-param name="lab_next_mark">Next Flagged</xsl:with-param>
			<xsl:with-param name="lab_mark">Flag</xsl:with-param>
			<xsl:with-param name="lab_unmark">Unflag</xsl:with-param>
			<xsl:with-param name="lab_unattempt">Unattempt</xsl:with-param>
			<xsl:with-param name="lab_already_answer">Attempted</xsl:with-param>
			<xsl:with-param name="lab_already_mark">Flagged</xsl:with-param>
			<xsl:with-param name="lab_option_size">Option Size</xsl:with-param>
			<xsl:with-param name="lab_option_size_1">Small</xsl:with-param>
			<xsl:with-param name="lab_option_size_2">Medium</xsl:with-param>
			<xsl:with-param name="lab_option_size_3">Large</xsl:with-param>
			<xsl:with-param name="lab_option_source">Source</xsl:with-param>
			<xsl:with-param name="lab_option_target">Target</xsl:with-param>
			<xsl:with-param name="lab_total_score">Total Score</xsl:with-param>
			<xsl:with-param name="lab_select_que">Choice Question</xsl:with-param>
			<xsl:with-param name="lab_prev_page">Prev Page</xsl:with-param>
			<xsl:with-param name="lab_next_page">Next Page</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="test_player">
		<xsl:param name="lab_help"/>
		<xsl:param name="lab_reset"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_add_attachment"/>
		<xsl:param name="lab_mark"/>
		<xsl:param name="lab_complete"/>
		<xsl:param name="lab_time_left"/>
		<xsl:param name="lab_examinee_name"/>
		<xsl:param name="lab_examinee_id"/>
		<xsl:param name="lab_examinee_usg"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="lab_submit"/>
		<xsl:param name="lab_btn_save"/>
		<xsl:param name="lab_help_intr"/>
		<xsl:param name="lab_questionnum"/>
		<xsl:param name="lab_ans_here"/>
		<xsl:param name="lab_delete_all"/>
		<xsl:param name="lab_delete"/>
		<xsl:param name="lab_next_unanswer"/>
		<xsl:param name="lab_next_mark"/>
		<xsl:param name="lab_unmark"/>
		<xsl:param name="lab_unattempt"/>
		<xsl:param name="lab_already_answer"/>
		<xsl:param name="lab_already_mark"/>
		<xsl:param name="lab_option_size"/>
		<xsl:param name="lab_option_size_1"/>
		<xsl:param name="lab_option_size_2"/>
		<xsl:param name="lab_option_size_3"/>
		<xsl:param name="lab_option_source"/>
		<xsl:param name="lab_option_target"/>
		<xsl:param name="lab_total_score"/>
		<xsl:param name="lab_select_que"/>
		<xsl:param name="lab_prev_page"/>
		<xsl:param name="lab_next_page"/>
		<head>
			<meta http-equiv="X-UA-Compatible" content="IE=8" /> 
			<title>
				<xsl:value-of select="$wb_wizbank"/>
				<xsl:text> - </xsl:text>
				<xsl:value-of select="$quiz/header/title"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">test_player</xsl:with-param>
			</xsl:call-template>
			<script type="text/javascript" src="{$wb_js_path}wb_module.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}menu_many.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}layer.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_htmlplayer.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}interactionclass.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}elemdragclass.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}behdraglayer.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_es_lib.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}overlib.js"/>
			<script type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
		
	// ---------------- Initializaton ---------------------
	var reflesh_parent = false;
	var qset = new CwQuestionSet()
	var mod = new wbModule;
	var InternetExplorer = (navigator.appName.indexOf("Microsoft") == 0);
	var totalQ = ]]><xsl:value-of select="$quiz/@size"/><![CDATA[;
	var startQ = 1; 
	var currentQue = 1; 
	var testDuration = ]]><xsl:value-of select="$quiz/header/@duration"/><![CDATA[ * 60; // in second
	var auto_save_time = ]]><xsl:value-of select="$quiz/header/@time_left"/><![CDATA[;
	var timeLeft;
	if (auto_save_time == 0) {
		timeLeft = testDuration;
	} else {
		timeLeft = auto_save_time;
	}
	var test_lang = ']]><xsl:value-of select="$wb_lang"/><![CDATA[';
	var ttl_pair = 0;
	var submit_test = ]]><xsl:value-of select="$submit_test"/><![CDATA[;	
	var hasSubmitFile = ]]><xsl:value-of select="$hasESFileSubmit"/><![CDATA[;	
	var isOnlineExam = ]]>'<xsl:value-of select="//isOnlineExam"/>'<![CDATA[;
	var mod_id = ]]>'<xsl:value-of select="$quiz/@id"/>'<![CDATA[;
	var module_lst = new wbModule
	// ----- Hook for Internet Explorer  -----------	
	
	function status(){
		return false;
	}

//Create Submit File Questions Array
function UploadQue(id,que_id){
	this.id = id
	this.que_id = que_id
}

function restoreMT() {
	for (var i = 1; i <= totalQ; i++) {
		if (qset.getQuestion(i).interactions[0].getType() == 'MT') {
			//alert(" mt   "+i+"  "+qset.getQuestion(i).answer_array.length);
			showMtAns(i);	
		}
	}
}

function SaveAndSuspendFunc() {
	if (!submit_test) {
		alert(wb_msg_preview_mode_save_suspend);
		return;
	} else {
		if (isOnlineExam === 'true' && !window.parent.pauseed) {
			//remove the learner from online exam learner list
			removeOnlineList();
		}
		CwQuestionAutoSaveAll(true);
		window.parent.track.unset_onunload();
		if(parent.window && parent.window.opener){
			reflesh_parent = true;
		}
	}
}

function helpFunc(que_id, tkh_id) {
	if (!submit_test) {
		alert(wb_msg_preview_mode_help);
		return;
	} else {
		module_lst.start_prev('', que_id, '', tkh_id, false, '', false, '', false)
	}
}
	function setHidFlag() {
		frmResult.db_flag_cookie.value = gen_get_cookie(COOKIE_QUE_FLG);
	}

	function removeOnlineList() {
		req = getXMLHttpRequest();
		req_url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'remove_from_online_list', 'mod_id', mod_id);
		req.open("GET", req_url, true);
		req.send(null);
	}
	window.onbeforeunload = unloadHandler;

	function unloadHandler(){
		if(reflesh_parent){
	     	parent.window.opener.location.reload();
	     }
	}
	
	
]]><xsl:if test="$hasESFileSubmit = 'true'">
	var UploadQueSet =  new Array()
	<xsl:for-each select="descendant::question[header/@type != 'FSC' and header/@type != 'DSC']">
						<xsl:if test="header/@que_submit_file_ind = 'Y'"><![CDATA[
		UploadQueSet[UploadQueSet.length] =  new UploadQue(]]><xsl:value-of select="position()"/>,<xsl:value-of select="@id"/><![CDATA[)]]></xsl:if>
					</xsl:for-each>
				</xsl:if><![CDATA[
	
	gen_del_cookie(COOKIE_QUE_FLG);
	var cookie_val = ']]><xsl:value-of select="$quiz/db_flag_cookie"/><![CDATA['
	if (cookie_val != 'blank') {
		gen_set_cookie(COOKIE_QUE_FLG, cookie_val);
	}
	if(submit_test == false){
		//gen_del_cookie(COOKIE_QUE_FLG);
	}else{
		wb_utils_set_cookie('ACTIVE_MODULE',]]><xsl:value-of select="/quiz/@id"/><![CDATA[);
	}
	]]></script>
 		</head>
		<body  leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="MM_initInteractions();setHidFlag();init();restoreMT();">
		
		<table border="0" align="center" cellspacing="0" cellpadding="0" width="1004">
			<tr>
				<td id="head_td">
					<xsl:variable name="contentWidth" select="$que_title_width + 20"/>
					<div style="width: {$contentWidth} ;height: 188px; overflow:hidden;">
						<div id="titlediv" style="width: 1020px;   height: 188px;overflow:auto;z-index:100;position: fixed; top: 0px ; background-color:#FFE8D6; background-image:url({$wb_img_path}test_banner.jpg);background-repeat:no-repeat;">
						<!-- 模块标题和按钮 -->
							<table cellspacing="0" cellpadding="0" border="0px" width="100%" class="mainContent" style="">
								<tr>
									<td colspan="4">
										<img src="{$wb_img_path}tp.gif" height="10" width="1" border="0"/>
									</td>
								</tr>
								<tr>
									<td align="left" width="2%">
										<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
									</td>
									<td align="left" nowrap="nowrap" width="20%" valign="top">
										<table border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td colspan="2">
													<img src="{//user_photo}" border="0" width="90px" height="90px"/>
												</td>
											</tr>
											<tr>
												<td class="TitleText statInfo" width="5%" align="left" nowrap="nowrap"><xsl:value-of select="$lab_examinee_name"/>：</td>
												<td class="TitleText statInfo" width="20%"><xsl:value-of select="$quiz/meta/cur_usr/@display_bil"/></td>
											</tr>
											<tr>
												<td class="TitleText statInfo" width="5%" align="left" nowrap="nowrap"><xsl:value-of select="$lab_examinee_id"/>：</td>
												<td class="TitleText statInfo" width="20%"><xsl:value-of select="$quiz/meta/cur_usr/@id"/></td>
											</tr>
											<tr>
												<td class="TitleText statInfo" width="5%" align="left" nowrap="nowrap"><xsl:value-of select="$lab_examinee_usg"/>：</td>
												<td class="TitleText statInfo" width="20%"><xsl:value-of select="$quiz/header/title"/></td>
											</tr>
											<tr>
												<td colspan="2">
													<img src="{$wb_img_path}tp.gif" height="5" width="1" border="0"/>
												</td>
											</tr>
										</table>
									</td>
									<td align="left" nowrap="nowrap" width="75%">
										<table border="0" cellspacing="0" cellpadding="0" width="100%">
											<tr>
												<td colspan="2">
													<img src="{$wb_img_path}tp.gif" height="20" width="1" border="0"/>
												</td>
											</tr>
											<tr>
												<td>
													<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
												</td>
												<td>
													<table border="0" cellpadding="0" width="100%">
														<tr>
															<td align="left" valign="top">
																<img src="{$wb_img_path}no_answered.jpg" height="16" width="20" border="0"/>&#160;<span class="TitleText"><xsl:value-of select="$lab_unattempt"/></span>
															</td>
															<td nowrap="nowrap" align="left">
																<span class="TitleText">
																	
																</span>
															</td>
															<td align="center" valign="top">
																<img src="{$wb_img_path}answered.jpg" height="16" width="20" border="0"/>&#160;<span class="TitleText"><xsl:value-of select="$lab_already_answer"/></span>
															</td>
															<td nowrap="nowrap" align="center">
																<span class="TitleText">
																	
																</span>
															</td>
															<td align="center" valign="top">
																<img src="{$wb_img_path}marked_que.jpg" height="16" width="20" border="0"/>&#160;<span class="TitleText"><xsl:value-of select="$lab_already_mark"/></span>
															</td>
															<td class="TitleText statInfo" align="right" nowrap="nowrap" id="time_remain_text" valign="top"><xsl:value-of select="$lab_time_left"/>:</td>
															<td class="Title statInfo" width="15%" valign="top"><span id="time_remain_value" style="color:red;">0</span></td>
															<td width="15%" align="center" valign="bottom">
															<xsl:call-template name="wb_utils_button">
																<xsl:with-param name="text" select="$lab_submit"/>
																<xsl:with-param name="href">javascript:submitTest()</xsl:with-param>
															</xsl:call-template>
																<!-- <xsl:call-template name="wb_bs_test_button">
																	<xsl:with-param name="text" select="$lab_submit"/>
																	<xsl:with-param name="href">javascript:submitTest()</xsl:with-param>
																</xsl:call-template> -->
															</td>
															<xsl:if test="$quiz/header/@show_save_and_suspend_ind=1">
																<td>
																	<xsl:call-template name="wb_utils_button">
																		<xsl:with-param name="text" select="$lab_btn_save"/>
																		<xsl:with-param name="href">javascript:SaveAndSuspendFunc()</xsl:with-param>
																	</xsl:call-template>
																</td>
															</xsl:if>
														</tr>
													</table>
												</td>
											</tr>
											<xsl:if test="$sg_mc_cnt>0">
												<tr valign="top">
													<td width="20%" class="mainContent">
														<span class="TitleText">
															单项选择题<br/>（<xsl:value-of select="$sg_mc_cnt"/>题，共<xsl:value-of select="$sg_mc_sum"/>分）
														</span>
													</td>
													<td align="left" width="60%">
														<!-- 题号索引 -->
														<table cellspacing="0" cellpadding="0">
															<tr valign="bottom">
															<td>
																<xsl:for-each select="$quiz/question[body/interaction/@logic='SINGLE']">
																	<div id="slot_td_idx{position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																	<a  onclick="changeSlot('{position()}','flag_layer{position()}')"  class="Text" style="cursor:pointer;">
																			<span class="UnSelectedQue" id="slot_idx{position()}"><xsl:if test="position() &lt; 10">0</xsl:if><xsl:value-of select="position()"/></span>
																		</a>
																	</div>
																</xsl:for-each>
															</td>	
															</tr>
														</table>
														<span id="inter_msg" class="TitleText"/>
													</td>
												</tr>
											</xsl:if>
											<xsl:if test="$mt_mc_cnt>0">
												<tr valign="top">
													<td width="20%" class="mainContent">
														<span class="TitleText">
															多项选择题<br/>（<xsl:value-of select="$mt_mc_cnt"/>题，共<xsl:value-of select="$mt_mc_sum"/>分）
														</span>
													</td>
													<td align="left" width="60%">
														<!-- 题号索引 -->
														<table cellspacing="0" cellpadding="0">
															<tr valign="bottom">
															<td>
																<xsl:for-each select="$quiz/question[body/interaction/@logic='AND' or body/interaction/@logic='OR']">
																	<div id="slot_td_idx{$mt_mc_start+position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																		<a  onclick="changeSlot('{$mt_mc_start+position()}','flag_layer{$mt_mc_start+position()}')"  class="Text" style="cursor:pointer;">
																			<span class="UnSelectedQue" id="slot_idx{$mt_mc_start+position()}"><xsl:if test="$mt_mc_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$mt_mc_start+position()"/></span>
																		</a>
																	</div>
																</xsl:for-each>
															</td>
															</tr>
														</table>
														<span id="inter_msg" class="TitleText"/>
													</td>
												</tr>
											</xsl:if>
											<xsl:if test="$fb_cnt>0">
											<tr valign="top">
												<td width="20%" class="mainContent">
													<span class="TitleText">
														填空题<br/>（<xsl:value-of select="$fb_cnt"/>题，共<xsl:value-of select="$fb_sum"/>分）
													</span>
												</td>
												<td align="left" width="60%">
													<!-- 题号索引 -->
													<table cellspacing="0" cellpadding="0">
														<tr valign="bottom">
														<td>
															<xsl:for-each select="$quiz/question[header/@type = 'FB']">
																<div id="slot_td_idx{$fb_start+position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																	<a  onclick="changeSlot('{$fb_start+position()}','flag_layer{$fb_start+position()}')"  class="Text" style="cursor:pointer;">	
																		<span class="UnSelectedQue" id="slot_idx{$fb_start+position()}"><xsl:if test="$fb_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$fb_start+position()"/></span>
																	</a>
																</div>
															</xsl:for-each>
															</td>
														</tr>
													</table>
													<span id="inter_msg" class="TitleText"/>
												</td>
											</tr>
											</xsl:if>
											<xsl:if test="$mt_cnt>0">
												<tr valign="top">
												<td width="20%" class="mainContent">
													<span class="TitleText">
														配对题<br/>（<xsl:value-of select="$mt_cnt"/>题，共<xsl:value-of select="$mt_sum"/>分）
													</span>
												</td>
												<td align="left" width="60%">
													<!-- 题号索引 -->
													<table cellspacing="0" cellpadding="0">
														<tr valign="bottom">
														<td>
															<xsl:for-each select="$quiz/question[header/@type = 'MT']">
																<div id="slot_td_idx{$mt_start+position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																	<a  onclick="changeSlot('{$mt_start+position()}','flag_layer{$mt_start+position()}')"  class="Text" style="cursor:pointer;">	
																		<span class="UnSelectedQue" id="slot_idx{$mt_start+position()}"><xsl:if test="$mt_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$mt_start+position()"/></span>
																	</a>
																</div>
															</xsl:for-each>
														</td>
														</tr>
													</table>
													<span id="inter_msg" class="TitleText"/>
												</td>
											</tr>
											</xsl:if>
											<xsl:if test="$tf_cnt>0">
											<tr valign="top">
												<td width="20%" class="mainContent">
													<span class="TitleText">
														判断题<br/>（<xsl:value-of select="$tf_cnt"/>题，共<xsl:value-of select="$tf_sum"/>分）
													</span>
												</td>
												<td align="left" width="60%">
													<!-- 题号索引 -->
													<table cellspacing="0" cellpadding="0">
														<tr valign="bottom">
														<td>
															<xsl:for-each select="$quiz/question[header/@type = 'TF']">
																<div id="slot_td_idx{$tf_start+position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																	<a  onclick="changeSlot('{$tf_start+position()}','flag_layer{$tf_start+position()}')"  class="Text" style="cursor:pointer;">	
																		<span class="UnSelectedQue" id="slot_idx{$tf_start+position()}"><xsl:if test="$tf_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$tf_start+position()"/></span>
																	</a>
																</div>
															</xsl:for-each>
														</td>
														</tr>
													</table>
													<span id="inter_msg" class="TitleText"/>
												</td>
											</tr>
											</xsl:if>
											<xsl:if test="$es_cnt>0">
											<tr valign="top">
												<td width="20%" class="mainContent">
													<span class="TitleText">
														问答题<br/>（<xsl:value-of select="$es_cnt"/>题，共<xsl:value-of select="$es_sum"/>分）
													</span>
												</td>
												<td align="left" width="60%">
													<!-- 题号索引 -->
													<table cellspacing="0" cellpadding="0">
														<tr valign="bottom">
														<td>
															<xsl:for-each select="$quiz/question[header/@type = 'ES']">
																<div id="slot_td_idx{$es_start+position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																	<a  onclick="changeSlot('{$es_start+position()}','flag_layer{$es_start+position()}')"  class="Text" style="cursor:pointer;">	
																		<span class="UnSelectedQue" id="slot_idx{$es_start+position()}"><xsl:if test="$es_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$es_start+position()"/></span>
																	</a>
																</div>
															</xsl:for-each>
														</td>
														</tr>
													</table>
													<span id="inter_msg" class="TitleText"/>
												</td>
											</tr>
											</xsl:if>
											<xsl:if test="$fsc_cnt>0">
											<tr valign="top">
												<td width="20%" class="mainContent">
													<span class="TitleText">
														静态情景题<br/>（<xsl:value-of select="$fsc_cnt"/>题，共<xsl:value-of select="$fsc_sum"/>分）
													</span>
												</td>
												<td align="left" width="60%">
													<!-- 题号索引 -->
													<table cellspacing="0" cellpadding="0">
														<tr valign="bottom">
														<td>
															<xsl:for-each select="$quiz/question[header/@type = 'FSC']/question_list/question">
																<div id="slot_td_idx{$fsc_start+position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																	<a  onclick="changeSlot('{$fsc_start+position()}','flag_layer{$fsc_start+position()}')"  class="Text" style="cursor:pointer;">
																		<span class="UnSelectedQue" id="slot_idx{$fsc_start+position()}"><xsl:if test="$fsc_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$fsc_start+position()"/></span>
																	</a>
																</div>
															</xsl:for-each></td>
														</tr>
													</table>
													<span id="inter_msg" class="TitleText"/>
												</td>
											</tr>
											</xsl:if>
											<xsl:if test="$dsc_cnt>0">
											<tr valign="top">
												<td width="20%" class="mainContent">
													<span class="TitleText">
														动态情景题<br/>（<xsl:value-of select="$dsc_cnt"/>题，共<xsl:value-of select="$dsc_sum"/>分）
													</span>
												</td>
												<td align="left" width="60%">
													<!-- 题号索引 -->
													<table cellspacing="0" cellpadding="0">
														<tr valign="bottom">
														<td>
															<xsl:for-each select="$quiz/question[header/@type = 'DSC']/question_list/question">
																<div id="slot_td_idx{$dsc_start+position()}" align="center" nowrap="nowrap" valign="bottom" width="20px" style="border:solid 1px #FFD2B1;background-color:white;float:left">
																	<a  onclick="changeSlot('{$dsc_start+position()}','flag_layer{$dsc_start+position()}')"  class="Text" style="cursor:pointer;">
																		<span class="UnSelectedQue" id="slot_idx{$dsc_start+position()}"><xsl:if test="$dsc_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$dsc_start+position()"/></span>
																	</a>
																</div>
															</xsl:for-each>
														</td>
														</tr>
													</table>
													<span id="inter_msg" class="TitleText"/>
												</td>
											</tr>
											</xsl:if>
										</table>
									</td>
									<td align="left" width="3%">
										<img src="{$wb_img_path}tp.gif" height="1" width="1" border="0"/>
									</td>
								</tr>
							</table>
						<!--xsl:call-template name="wb_ui_line"/-->
						</div>
						<div id="titlediv_hidden" style="height: 10px;width: 1004px;z-index:90;visible:hidden;top:0px;">
						</div>
					</div>
					<form name="frmResult" method="post" action="{$wb_servlet_qdbaction_url}">
						<input name="cmd" type="hidden" value=""/>
						<input name="test_style" type="hidden" value="many"/>
						<input name="tkh_id" type="hidden" value="{$quiz/aicc_data/@tkh_id}"/>
						<input name="wb_lang" type="hidden" value="{$wb_lang}"/>
						<input name="charset" type="hidden" value="{//@charset}"/>
						<input name="pgr_usr_id" type="hidden" value="{//cur_usr/@ent_id}"/>
						<input name="pgr_mod_id" type="hidden" value="{$quiz/@id}"/>
						<input name="pgr_start_time" type="hidden" value="{$quiz/@start_time}"/>
						<input name="pgr_used_time" type="hidden" value=""/>
						<input name="pgr_status" type="hidden" value="OK"/>
						<input name="mod_type" type="hidden" value="{$quiz/header/@subtype}"/>
						<input type="hidden" name="timestamp" value="{$quiz/@start_time}"/>
						<!-- These are prepared for auto-save!!!-->
						<input name="module" type="hidden" value=""/>
						<input type="hidden" name="db_flag_cookie" value=""/>
						<input type="hidden" name="cur_tkh_id" value="{$quiz/aicc_data/@tkh_id}"/>
						<input type="hidden" name="cur_mod_id" value="{$quiz/header/@mod_id}"/>
						<input type="hidden" name="time_left" value=""/>
						<input type="hidden" name="flag" value=""/>
						<input type="hidden" name="window_name" value=""/>
						<input type="hidden" name="total_que" value="{$quiz/@size}"/>
						<input type="hidden" name="cur_que_id" value="1"/>
						<input type="hidden" name="cur_page_offset" value="1"/>
						<input type="hidden" name="cur_unatm_cnt" value="{$quiz/@size}"/>
						<input type="hidden" name="cur_atm_cnt" value="0"/>
						<input type="hidden" name="cur_flag_cnt" value="0"/>
						<input type="hidden" name="remain_time" value=""/>
						<input id="submit_test_id" type="hidden" name="submit_test" value="{$submit_test}"/>
						<!-- for exam item -->
						<input type="hidden" name="terminate_exam_msg" value=""/>
						<input type="hidden" name="isTerminateExam" value="false"/>
						<input type="hidden" name="exam_mark_as_zero" value="false"/>
						<input type="hidden" name="show_answer_ind" value="{$show_answer_ind}"/>
						
						<xsl:for-each select="$quiz/question/body/interaction">
							<xsl:variable name="_n" select="@order"/>
							<input type="hidden" name="atm{$_n}" value=""/>
							<input type="hidden" name="flag{$_n}" value=""/>
						</xsl:for-each>
					
						<label id="hidden_que_varible" style="display:none">
							
						</label>
						<!--
						<xsl:variable name="_i" select="position()"/>
						<input name="atm_int_res_id_{$_i}" type="hidden"/>
						<input name="atm_flag_{$_i}" type="hidden"/>
						<xsl:for-each select="body/interaction">
							<xsl:variable name="_j" select="@order"/>
							<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
							<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
						</xsl:for-each>
						
						-->
						<xsl:apply-templates select="$quiz" mode="quiz"/>
						<input name="url_success" type="hidden"/>
						<input name="url_failure" type="hidden"/>
					</form>
					<xsl:if test="count($quiz/question/body/interaction[@type='MT']) != 0">
						<script>
							if(!document.all){
								if(!document.getElementById){
								alert("Sorry, Matching Not Support Yet");
								}
							}
						</script>
					</xsl:if>
					<div id="overDiv" style="position:absolute; visibility:hide;z-index:10000;"/>
				</td>
			</tr>
			<tr height="30">
				<td align="center" width="60%" style="background-color:#FFE8D6;">
					<span class="TitleText"><xsl:value-of select="$quiz/header/title"/></span>	
				</td>
			</tr>
			<tr>
				<td valign="top" style="border:solid 1px #FFD2B1;">
					<xsl:apply-templates select="$quiz" mode="TST">
						<xsl:with-param name="lab_help" select="$lab_help"/>
						<xsl:with-param name="lab_reset" select="$lab_reset"/>
						<xsl:with-param name="lab_true" select="$lab_true"/>
						<xsl:with-param name="lab_false" select="$lab_false"/>
						<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
						<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
						<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
						<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
						<xsl:with-param name="lab_delete" select="$lab_delete"/>
						<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
						<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
						<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
						<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
						<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
						<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
						<xsl:with-param name="lab_prev" select="$lab_prev"/>
						<xsl:with-param name="lab_next" select="$lab_next"/>
					</xsl:apply-templates>
				</td>
			</tr>
		</table>	
		<div id="testBottom" style="position: absolute;visibility: hidden;background-color: #87dbfa;">
			<img src="{$wb_img_path}tp.gif" height="30" width="1" border="0"/>
		</div>
	</body>
	</xsl:template>
	<xsl:template match="*" mode="TST">
		<xsl:param name="lab_help"/>
		<xsl:param name="lab_reset"/>
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_add_attachment"/>
		<xsl:param name="lab_help_intr"/>
		<xsl:param name="lab_ans_here"/>
		<xsl:param name="lab_delete_all"/>
		<xsl:param name="lab_delete"/>
		<xsl:param name="lab_option_size"/>
		<xsl:param name="lab_option_size_1"/>
		<xsl:param name="lab_option_size_2"/>
		<xsl:param name="lab_option_size_3"/>
		<xsl:param name="lab_option_source"/>
		<xsl:param name="lab_option_target"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<!--<xsl:variable name="cur_tkh_id" select="/quiz/aicc_data/@tkh_id"/>
		<xsl:variable name="cur_mod_id" select="/quiz/header/@mod_id"/>-->
		<xsl:for-each select="question[body/interaction[@logic='SINGLE']]">
			<xsl:call-template name="mc_que">
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@logic='AND' or @logic='OR']]">
			<xsl:call-template name="mc_que">
				<xsl:with-param name="my_start" select="$mt_mc_start"/>
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='FB']]">
			<xsl:call-template name="fb_que">
				<xsl:with-param name="my_start" select="$fb_start"/>
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='MT']]">
			<xsl:call-template name="mt_que">
				<xsl:with-param name="my_start" select="$mt_start"/>
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='TF']]">
			<xsl:call-template name="tf_que">
				<xsl:with-param name="my_start" select="$tf_start"/>
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='ES']]">
			<xsl:call-template name="es_que">
				<xsl:with-param name="my_start" select="$es_start"/>
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="question[header[@type='FSC']]/question_list/question">
			<xsl:call-template name="sc_que">
				<xsl:with-param name="my_start" select="$fsc_start"/>
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
		<xsl:for-each select="question[header[@type='DSC']]/question_list/question">
			<xsl:call-template name="sc_que">
				<xsl:with-param name="my_start" select="$dsc_start"/>
				<xsl:with-param name="lab_help" select="$lab_help"/>
				<xsl:with-param name="lab_reset" select="$lab_reset"/>
				<xsl:with-param name="lab_true" select="$lab_true"/>
				<xsl:with-param name="lab_false" select="$lab_false"/>
				<xsl:with-param name="lab_add_attachment" select="$lab_add_attachment"/>
				<xsl:with-param name="lab_help_intr" select="$lab_help_intr"/>
				<xsl:with-param name="lab_ans_here" select="$lab_ans_here"/>
				<xsl:with-param name="lab_delete_all" select="$lab_delete_all"/>
				<xsl:with-param name="lab_delete" select="$lab_delete"/>
				<xsl:with-param name="lab_option_size" select="$lab_option_size"/>
				<xsl:with-param name="lab_option_size_1" select="$lab_option_size_1"/>
				<xsl:with-param name="lab_option_size_2" select="$lab_option_size_2"/>
				<xsl:with-param name="lab_option_size_3" select="$lab_option_size_3"/>
				<xsl:with-param name="lab_option_source" select="$lab_option_source"/>
				<xsl:with-param name="lab_option_target" select="$lab_option_target"/>
				<xsl:with-param name="lab_prev" select="$lab_prev"/>
				<xsl:with-param name="lab_next" select="$lab_next"/>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	<!-- =============================================================== -->
	<!--  test player - es interaction  -->
	<xsl:template name="es_que">
		<xsl:param name="lab_add_attachment"/>
		<xsl:param name="lab_help"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="my_start"/>
		<xsl:variable name="_pos" select="$my_start+position()"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}_tmp"><img src="{$wb_img_path}tp.gif" height="30" width="1" border="0"/></div>
		<div id="layer{$_pos}" class="" style="width:980px;z-index:90;" onmouseout="leaveQue({$_pos})">
			<form name="frm{$_pos}" onSubmit="return status()" enctype="multipart/form-data" method="post">
				<input type="hidden" name="que_id" value="{@id}"/>
				<input type="hidden" name="timestamp" value="{//quiz/@start_time}"/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="autosave_pos" value="{$_pos}"/>
				<input type="hidden" name="cur_que_id" value="{@id}"/>
				<input type="hidden" name="cur_tkh_id" value="{../aicc_data/@tkh_id}"/>
				<input type="hidden" name="cur_mod_id" value="{../header/@mod_id}"/>
				<input type="hidden" name="time_left" value=""/>
				<input type="hidden" name="start_time" value="{$quiz/@start_time}"/>
				<input type="hidden" name="save_by_usr" value=""/>
				<input type="hidden" name="flag" value=""/>
				<input type="hidden" name="int_size" value=""/>
				<xsl:if test="position()=1">
					<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
						<tr height="30">
							<td align="left" valign="top">
								<span class="TitleText"><xsl:text>问答题（</xsl:text><xsl:value-of select="$es_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$es_sum"/><xsl:text>分）</xsl:text></span>
							</td>
						</tr>
					</table>
				</xsl:if>
				<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
					<tr>
						<td align="center" width="20px" valign="top">
							<a href="javascript:flagQue({$_pos});" class="Text"   name="flag_layer{$_pos}">
								<img src="{$wb_img_path}que_normal_flag.jpg" border="0"  id="flag_layer{$_pos}"/>
							</a>
						</td>
						<td valign="top">
							<table cellpadding="0" cellspacing="0" border="0" width="100%">
								<tr>
									<td nowrap="nowrap" valign="top">
										<span class="TitleText">
											<xsl:value-of select="$_pos"/>
											<xsl:text>.&#160;</xsl:text>
										</span>
									</td>
									<td width="100%" style="word-break: break-all">
										<span class="Text">
											<xsl:for-each select="body/text() | body/html">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:call-template name="unescape_html_linefeed">
															<xsl:with-param name="my_right_value">
																<xsl:value-of select="."/>
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</span>
									</td>
								</tr>
							</table>
						</td>
						<td align="right" valign="top">
							<xsl:apply-templates select="body/object"/>
						</td>
					</tr>
				</table>
				<xsl:variable name="que_submit_file_ind" select="header/@que_submit_file_ind"/>
				<table cellpadding="5" cellspacing="0" border="0" width="100%">
					<tr>
						<td>&#160;</td>
					</tr>
					<tr>
						<td>
							<xsl:for-each select="body/interaction">
								<xsl:if test="$que_submit_file_ind = 'Y'">
									<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
																function helpTooltip() {
											var ht = document.getElementById('help_tooltip');
											if (ht.style.display == 'none') {
												ht.style.display = 'block';
											} else {
												ht.style.display = 'none';
											}
										}
									]]></script>
									<table cellpadding="5" cellspacing="0" border="0" width="100%" height="60">
										<tr>
											<td nowrap="nowrap" valign="bottom">
												<input class="Btn" type="button">
													<xsl:attribute name="value"><xsl:value-of select="$lab_add_attachment"/></xsl:attribute>
													<xsl:attribute name="onclick">javascript:es_add_file_field(<xsl:value-of select="$que_id"/>,<xsl:value-of select="$_pos"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:attribute>
												</input>
												<xsl:text>&#160;</xsl:text>
												<a class="Text" href="javascript:;">
													<xsl:attribute name="onmouseover">helpTooltip();</xsl:attribute>
													<xsl:attribute name="onmouseout">helpTooltip();</xsl:attribute>
													<xsl:value-of select="$lab_help"/>
												</a>
											</td>
											<td>
												<div id="help_tooltip" style="display: none; border: 1px solid #CCC; background: #EEE;padding: 3px;">
													<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[document.write(wb_msg_upload_attach_inst)]]></script>
												</div>
											</td>
										</tr>
									</table>
								</xsl:if>
							</xsl:for-each>
						</td>
					</tr>
					<tr>
						<td>
							<span id="es_file_{$que_id}"/>
						</td>
					</tr>
					<tr>
						<td width="100%">
							<xsl:for-each select="body/interaction">
								<textarea rows="10" style="width:740px" class="wzb-inputTextArea" cols="100" name="txt_q{$_pos}_i{@order}" onChange="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value)" onKeyPress="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value);">
									<xsl:value-of select="../../../restore/question[@id=$que_id]/interaction/value"/>
								</textarea>
								<input type="hidden" name="autosave_q{$_pos}_i{@order}" value="{../../../restore/question[@id=$que_id]/interaction/value}"/>
								<input type="hidden" name="responsebil_order_dbflag{@order}" value=""/>
							</xsl:for-each>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script language="JavaScript" type="text/javascript"><![CDATA[	
				q = new CwQuestion ('layer]]><xsl:value-of select="$_pos"/><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[')	
			    ]]><xsl:for-each select="body/interaction"><![CDATA[
				interaction = new CwInteraction (']]><xsl:value-of select="../../@id"/><![CDATA[',']]><xsl:value-of select="@order"/><![CDATA[', 'ES', eval(getControlString("txt_q",]]><xsl:value-of select="$_pos"/><![CDATA[,]]><xsl:value-of select="@order"/><![CDATA[)))
				var restore_answer = eval('frm' + ]]><xsl:value-of select="$_pos"/><![CDATA[ + '.autosave_q' + ]]><xsl:value-of select="$_pos"/><![CDATA[ + '_i' + ]]><xsl:value-of select="@order"/><![CDATA[);
				interaction.setAnswer(restore_answer.value);
				q.addInteraction(interaction)
				]]></xsl:for-each><![CDATA[
				qset.addQuestion(q)		
		]]></script>
	</xsl:template>
	<!-- =============================================================== -->
	<!--  test player - fb interaction  -->
	<xsl:template name="fb_que">
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="my_start"/>
		<xsl:variable name="_pos" select="$my_start+position()"/>
		<xsl:variable name="que_id" select="@id"/>
		<!--<xsl:variable name="int_cnt" select="count(body/interaction)"/>-->
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}_tmp"><img src="{$wb_img_path}tp.gif" height="30" width="1" border="0"/></div>
		<div id="layer{$_pos}" class="mainContent" style="width:980px;z-index:90;" onmouseout="leaveQue({$_pos})">
			<form name="frm{$_pos}" onSubmit="return status()">
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="time_left" value=""/>
				<input type="hidden" name="cur_tkh_id" value="{../aicc_data/@tkh_id}"/>
				<input type="hidden" name="cur_mod_id" value="{../header/@mod_id}"/>
				<input type="hidden" name="cur_que_id" value="{$que_id}"/>
				<input type="hidden" name="autosave_pos" value="{$_pos}"/>
				<input name="start_time" type="hidden" value="{$quiz/@start_time}"/>
				<input type="hidden" name="save_by_usr" value=""/>
				<input type="hidden" name="flag" value=""/>
				<input type="hidden" name="int_size" value=""/>
				<xsl:if test="position()=1">
					<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
						<tr height="30">
							<td align="left" valign="top">
								<span class="TitleText"><xsl:text>填空题（</xsl:text><xsl:value-of select="$fb_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$fb_sum"/><xsl:text>分）</xsl:text></span>
							</td>
						</tr>
					</table>
				</xsl:if>
				<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
					<tr>
						<td align="center" width="20px" valign="top">
							<a href="javascript:flagQue({$_pos});" class="Text" name="flag_layer{$_pos}">
								<img src="{$wb_img_path}que_normal_flag.jpg" border="0"    id="flag_layer{$_pos}"/>
							</a>
						</td>
						<td valign="top">
							<table cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td nowrap="nowrap" valign="top">
										<span class="TitleText">
											<xsl:value-of select="$_pos"/>
											<xsl:text>.&#160;</xsl:text>
										</span>
									</td>
									<td width="100%" style="word-break: break-all">
										<span class="Text">
											<xsl:for-each select="body/text() | body/html | body/interaction">
												<xsl:variable name="int_order" select="@order"/>
												<xsl:choose>
													<xsl:when test="name() = 'interaction'">
														<input class="wzb-inputText" type="text" name="txt_q{$_pos}_i{@order}" size="{@length}" onChange="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value)" onKeyPress="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value)" value="{../../../restore/question[@id=$que_id]/interaction[@order=$int_order]}"/>
														<input type="hidden" name="autosave_q{$_pos}_i{@order}" value="{../../../restore/question[@id=$que_id]/interaction[@order=$int_order]}"/>
														<input type="hidden" name="responsebil_order_dbflag{@order}" value=""/>
													</xsl:when>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:call-template name="unescape_html_linefeed">
															<xsl:with-param name="my_right_value">
																<xsl:value-of select="."/>
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</span>
									</td>
								</tr>
							</table>
						</td>
						<td align="right" valign="top">
							<xsl:apply-templates select="body/object"/>
						</td>
					</tr>
					<tr>
						<td align="center">
							<table border="0" cellspacing="0" cellpadding="0">
								<tr>
									<img src="{$wb_img_path}tp.gif" height="30" width="1" border="0"/>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script language="JavaScript" type="text/javascript"><![CDATA[
		
				q = new CwQuestion ('layer]]><xsl:value-of select="$_pos"/><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[')
			    ]]><xsl:for-each select="body/interaction">
				<xsl:variable name="int_order" select="@order"/><![CDATA[
				interaction = new CwInteraction (']]><xsl:value-of select="../../@id"/><![CDATA[',']]><xsl:value-of select="@order"/><![CDATA[', 'FB', eval(getControlString("txt_q",]]><xsl:value-of select="$_pos"/>,<xsl:value-of select="@order"/><![CDATA[)))
				var restore_answer = eval('frm' + ]]><xsl:value-of select="$_pos"/><![CDATA[ + '.autosave_q' + ]]><xsl:value-of select="$_pos"/><![CDATA[ + '_i' + ]]><xsl:value-of select="@order"/><![CDATA[);
				interaction.setAnswer(restore_answer.value);		
				q.addInteraction(interaction)
				]]></xsl:for-each><![CDATA[
				qset.addQuestion(q)		
		
		]]></script>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="*" mode="DSC">
		<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td>
					<font face="arial">
						<xsl:for-each select="body/text() | body/html | body/interaction">
							<xsl:choose>
								<xsl:when test="name() = 'html'">
									<xsl:value-of disable-output-escaping="yes" select="."/>
								</xsl:when>
								<xsl:when test="name() = 'interaction'"/>
								<xsl:otherwise>
									<xsl:call-template name="unescape_html_linefeed">
										<xsl:with-param name="my_right_value">
											<xsl:value-of select="."/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</font>
				</td>
			</tr>
			<tr>
				<td align="right" valign="top"  style="word-break: break-all">
					<table border="0" width="100%" cellspacing="3" cellpadding="0">
						<xsl:for-each select="body/object">
							<tr>
								<td valign="top">
									<table border="0" cellspacing="3" cellpadding="0">
										<tr>
											<td valign="middle">
												<xsl:apply-templates select="."/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!--================================================= -->
	<xsl:template match="*" mode="FSC">
		<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td style="word-break: break-all">
					<font face="arial">
						<xsl:for-each select="body/text() | body/html | body/interaction">
							<xsl:choose>
								<xsl:when test="name() = 'html'">
									<xsl:value-of disable-output-escaping="yes" select="."/>
								</xsl:when>
								<xsl:when test="name() = 'interaction'"/>
								<xsl:otherwise>
									<xsl:call-template name="unescape_html_linefeed">
										<xsl:with-param name="my_right_value">
											<xsl:value-of select="."/>
										</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</font>
				</td>
			</tr>
			<tr>
				<td style="word-break: break-all">
					<table border="0" width="100%" cellspacing="3" cellpadding="0">
						<xsl:for-each select="body/object">
							<tr>
								<td valign="top" >
									<table border="0" cellspacing="3" cellpadding="0">
										<tr>
											<td class="wbParInfMultiTitleText">
												<xsl:value-of select="position()"/> : </td>
											<td valign="middle">
												<xsl:apply-templates select="."/>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</xsl:for-each>
					</table>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- test player - mc interaction -->
	<xsl:template name="mc_que">
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="my_start">0</xsl:param>
		<xsl:variable name="_pos" select="$my_start + position()"/>
		<xsl:variable name="_order" select="body/interaction/@order"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:variable name="cur_mod_id">
			<xsl:choose>
				<xsl:when test="../../header/@type = 'FSC' or ../../header/@type = 'DSC'">
					<xsl:value-of select="../../../header/@mod_id"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="../header/@mod_id"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="cur_tkh_id">
			<xsl:choose>
				<xsl:when test="../../header/@type = 'FSC' or ../../header/@type = 'DSC'">
					<xsl:value-of select="../../../aicc_data/@tkh_id"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="../aicc_data/@tkh_id"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
<!-- 		<div id="layer{$_pos}" class="mainContent ContentDiv" style="width:1000px; height:300px; z-index:{$_pos};"> -->
		<div id="layer{$_pos}" class="mainContent" style="width:980px;z-index:90px;" onmouseout="leaveQue({$_pos})" ><!-- onmouseout="leaveQue({$_pos})" -->
			<form name="frm{$_pos}" onSubmit="return status()">
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="time_left" value=""/>
				<input type="hidden" name="cur_tkh_id" value="{$cur_tkh_id}"/>
				<input type="hidden" name="cur_mod_id" value="{$cur_mod_id}"/>
				<input type="hidden" name="cur_que_id" value="{$que_id}"/>
				<input type="hidden" name="autosave_pos" value="{$_pos}"/>
				<input name="start_time" type="hidden" value="{$quiz/@start_time}"/>
				<input type="hidden" name="save_by_usr" value=""/>
				<input type="hidden" name="flag" value=""/>
				<input type="hidden" name="int_size" value=""/>
				<xsl:if test="(position()=1)">
					<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
						<tr height="30" >
							<td align="left" valign="top">
							<input type="hidden" name = "flag_layer0"/>
								<xsl:choose>
									<xsl:when test="body/interaction/@logic='SINGLE'">
										<span class="TitleText"><xsl:text>单项选择题（</xsl:text><xsl:value-of select="$sg_mc_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$sg_mc_sum"/><xsl:text>分）</xsl:text></span>
									</xsl:when>
									<xsl:when test="body/interaction/@logic='AND' or body/interaction/@logic='OR'">
										<span class="TitleText"><xsl:text>多项选择题（</xsl:text><xsl:value-of select="$mt_mc_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$mt_mc_sum"/><xsl:text>分）</xsl:text></span>
									</xsl:when>
								</xsl:choose>
							</td>
						</tr>
					</table>
				</xsl:if>
				<xsl:if test="../../header/@type = 'FSC'">
					<xsl:apply-templates select="../.." mode="FSC"/>
				</xsl:if>
				<xsl:if test="../../header/@type = 'DSC'">
					<xsl:apply-templates select="../.." mode="DSC"/>
				</xsl:if>
				<table width="{$que_title_width}" border="0px" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" width="20px" valign="top">
							<a href="javascript:flagQue({$_pos});" class="Text" >    <!-- name="flag_layer{$_pos}" -->
								<img src="{$wb_img_path}que_normal_flag.jpg" border="0"  id="flag_layer{$_pos}"/>
							</a>
						</td>
						<td valign="top">
							<table cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td nowrap="nowrap" valign="top">
										<span class="TitleText">
											<xsl:value-of select="$_pos"/>
											<xsl:text>.&#160;</xsl:text>
										</span>
									</td>
									<td width="100%" style="word-break: break-all">
										<span class="Text">
											<xsl:for-each select="body/text() | body/html | body/interaction">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:when test="name() = 'interaction'"/>
													<xsl:otherwise>
														<xsl:call-template name="unescape_html_linefeed">
															<xsl:with-param name="my_right_value">
																<xsl:value-of select="."/>
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
											<xsl:apply-templates select="body/object"/>
										</span>
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellspacing="0" cellpadding="5">
								<tr>
									<td>
										<input type="hidden" name="autosave_q{$_pos}_i{$_order}" value="{../restore/question[@id=$que_id]/interaction/@pas_response_bil}"/>
										<input type="hidden" name="responsebil_order_dbflag{$_order}" value=""/>
										<xsl:for-each select="body/interaction[@type='MC']/option">
											<xsl:variable name="optionPos" select="position()"/>
											<xsl:variable name="optionId" select="@id"/>
											<table border="0" cellspacing="0" cellpadding="5">
												<tr>
													<td>
													<table><tr>
														<td valign = 'top'>
															<xsl:choose>
																<xsl:when test="../../../outcome/@logic='AND' or ../../../outcome/@logic='OR'">
																	<input type="checkbox" name="rdo_q{$_pos}_i{$_order}" value="{@id}" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer('');qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswerShuffle('')">
																		<xsl:for-each select="../../../../restore/question[@id=$que_id]/interaction/value">
																			<xsl:if test="$optionId=text()">
																				<xsl:attribute name="checked">checked</xsl:attribute>
																			</xsl:if>
																		</xsl:for-each>
																	</input>
																</xsl:when>
																<xsl:otherwise>
																	<input type="radio" name="rdo_q{$_pos}_i{$_order}" value="{@id}" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer(this.value);qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswerShuffle({$optionPos})">
																		<xsl:if test="$optionId=../../../../restore/question[@id=$que_id]/interaction/value">
																			<xsl:attribute name="checked">checked</xsl:attribute>
																		</xsl:if>
																	</input>
																</xsl:otherwise>
															</xsl:choose>
															
															<span class="TitleText">
																<xsl:number format="A. "/>
															</span>
														</td>
														<td valign = 'top'>
															<span class="Text">
																<xsl:choose>
																	<xsl:when test="html">
																		<xsl:value-of disable-output-escaping="yes" select="html"/>
																	</xsl:when>
																	<xsl:otherwise>
																		<xsl:call-template name="unescape_html_linefeed">
																			<xsl:with-param name="my_right_value">
																				<xsl:value-of select="."/>
																			</xsl:with-param>
																		</xsl:call-template>
																	</xsl:otherwise>
																</xsl:choose>
															</span>
															<xsl:apply-templates select="object"/>
														</td>
														</tr></table>
													</td>	
												</tr>
											</table>
										</xsl:for-each>
									</td>
								</tr>
							</table><br/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script language="JavaScript" type="text/javascript"><![CDATA[

				q = new CwQuestion ('layer]]><xsl:value-of select="$_pos"/><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[')
				]]><xsl:variable name="mc_type">
				<xsl:choose>
					<xsl:when test="outcome/@logic = 'AND' or outcome/@logic = 'OR'">MC_M</xsl:when>
					<xsl:otherwise>MC</xsl:otherwise>
				</xsl:choose>
			</xsl:variable><![CDATA[
				interaction = new CwInteraction (']]><xsl:value-of select="@id"/><![CDATA[',']]><xsl:value-of select="$_order"/><![CDATA[', ']]><xsl:value-of select="$mc_type"/><![CDATA[', eval(getControlString("rdo_q",]]><xsl:value-of select="$_pos"/>,<xsl:value-of select="$_order"/><![CDATA[)));]]>
				<xsl:choose>
					<xsl:when test="$mc_type='MC_M'">
						<![CDATA[interaction.setAnswer('');interaction.setAnswerShuffle('');]]>
					</xsl:when>
					<xsl:otherwise>
						<![CDATA[interaction.setAnswer(']]><xsl:value-of select="../restore/question[@id=$que_id]/interaction/value"/><![CDATA[');]]>
					</xsl:otherwise>
				</xsl:choose>
				<![CDATA[q.addInteraction(interaction)
				qset.addQuestion(q)						
		]]></script>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- test player - TF interaction -->
	<xsl:template name="tf_que">
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="my_start"/>
		<xsl:variable name="_pos" select="$my_start+position()"/>
		<xsl:variable name="_order" select="body/interaction/@order"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}" class="mainContent" style="width:980px;z-index:90;" onmouseout="leaveQue({$_pos})">
			<form name="frm{$_pos}" onSubmit="return status()">
				<input type="hidden" name="autosave_pos" value="{$_pos}"/>
				<input type="hidden" name="time_left" value=""/>
				<input type="hidden" name="cur_tkh_id" value="{../aicc_data/@tkh_id}"/>
				<input type="hidden" name="cur_mod_id" value="{../header/@mod_id}"/>
				<input type="hidden" name="cur_que_id" value="{$que_id}"/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input name="start_time" type="hidden" value="{$quiz/@start_time}"/>
				<input type="hidden" name="save_by_usr" value=""/>
				<input type="hidden" name="flag" value=""/>
				<input type="hidden" name="int_size" value=""/>
				<xsl:if test="position()=1">
					<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
						<tr height="30">
							<td align="left" valign="top">
								<span class="TitleText"><xsl:text>判断题（</xsl:text><xsl:value-of select="$tf_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$tf_sum"/><xsl:text>分）</xsl:text></span>
							</td>
						</tr>
					</table>
				</xsl:if>
				<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
					<tr>
						<td align="center" width="20px" valign="top">
							<a href="javascript:flagQue({$_pos});" class="Text" name="flag_layer{$_pos}">
								<img src="{$wb_img_path}que_normal_flag.jpg" border="0"    id="flag_layer{$_pos}"/>
							</a>
						</td>
						<td valign="top">
							<table cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td nowrap="nowrap" valign="top">
										<span class="TitleText">
											<xsl:value-of select="$_pos"/>
											<xsl:text>.&#160;</xsl:text>
										</span>
									</td>
									<td width="100%"  style="word-break: break-all"> 
										<span class="Text">
											<xsl:for-each select="body/text() | body/html | body/interaction">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:when test="name() = 'interaction'"/>
													<xsl:otherwise>
														<xsl:call-template name="unescape_html_linefeed">
															<xsl:with-param name="my_right_value">
																<xsl:value-of select="."/>
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each>
										</span>
									</td>
								</tr>
							</table>
							<table width="100%" border="0" cellspacing="0" cellpadding="5">
								<tr>
									<td>
										<table border="0" cellspacing="0" cellpadding="5">
											<tr>
												<td>
													<input id="rdo_q{$_pos}_i{$_order}_1" type="radio" name="rdo_q{$_pos}_i{$_order}" value="1" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer(this.value)">
														<xsl:if test="../restore/question[@id=$que_id]/interaction/value=1">
															<xsl:attribute name="checked">checked</xsl:attribute>
														</xsl:if>
													</input>
													<label for="rdo_q{$_pos}_i{$_order}_1">
														<span class="TitleText">
															<xsl:value-of select="$lab_true"/>
														</span>
													</label>
												</td>
											</tr>
										</table>
										<table border="0" cellspacing="0" cellpadding="5">
											<tr>
												<td>
													<input type="radio" id="rdo_q{$_pos}_i{$_order}_2" name="rdo_q{$_pos}_i{$_order}" value="2" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer(this.value)">
														<xsl:if test="../restore/question[@id=$que_id]/interaction/value=2">
															<xsl:attribute name="checked">checked</xsl:attribute>
														</xsl:if>
													</input>
													<label for="rdo_q{$_pos}_i{$_order}_2">
														<span class="TitleText">
															<xsl:value-of select="$lab_false"/>
														</span>
													</label>
												</td>
											</tr>
										</table>
										<input type="hidden" name="autosave_q{$_pos}_i{$_order}" value="{../restore/question[@id=$que_id]/interaction/value}"/>
										<input type="hidden" name="responsebil_order_dbflag{$_order}" value=""/>
									</td>
								</tr>
							</table>
						</td>
						<td valign="top" align="right">
							<xsl:apply-templates select="body/object"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script language="JavaScript" type="text/javascript"><![CDATA[
				q = new CwQuestion ('layer]]><xsl:value-of select="$_pos"/><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[')
				interaction = new CwInteraction (']]><xsl:value-of select="@id"/><![CDATA[',']]><xsl:value-of select="$_order"/><![CDATA[', 'TF')	
				interaction.setAnswer(']]><xsl:value-of select="../restore/question[@id=$que_id]/interaction/value"/><![CDATA[');	
				q.addInteraction(interaction)
				qset.addQuestion(q)
		]]></script>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- test player - MT interaction -->
	<xsl:template name="mt_que">
		<xsl:param name="lab_reset"/>
		<xsl:param name="lab_help"/>
		<xsl:param name="lab_help_intr"/>
		<xsl:param name="lab_ans_here"/>
		<xsl:param name="lab_delete_all"/>
		<xsl:param name="lab_delete"/>
		<xsl:param name="lab_option_size"/>
		<xsl:param name="lab_option_size_1"/>
		<xsl:param name="lab_option_size_2"/>
		<xsl:param name="lab_option_size_3"/>
		<xsl:param name="lab_option_source"/>
		<xsl:param name="lab_option_target"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="my_start"/>
		<xsl:variable name="_pos" select="$my_start+position()"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:variable name="border_width">1</xsl:variable>
		<xsl:variable name="layer_width">1000</xsl:variable>
		<xsl:variable name="layer_height">700</xsl:variable>
		<xsl:variable name="div_height">50</xsl:variable>
		<xsl:variable name="parent_height">600</xsl:variable>
		<xsl:variable name="G0Parent_top">90</xsl:variable>
		<xsl:variable name="G0Parent_table_width">599</xsl:variable>
		<xsl:variable name="G0Parent_width">750</xsl:variable>
		<xsl:variable name="G0Result_width">250</xsl:variable>
		<xsl:variable name="source_td_width">200</xsl:variable>
		<xsl:variable name="target_radio_width">20</xsl:variable>
		<xsl:variable name="sourcenumvari" select="count($quiz/question[@id=$que_id]/body/source/item)"/>
		<xsl:variable name="targetnumvari" select="count($quiz/question[@id=$que_id]/body/interaction)"/>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div align="left" id="layer{$_pos}" class="mainContent" style="width:980px;overflow:visible;z-index:90;" onmouseout="leaveQue({$_pos})">
			
			<xsl:if test="position()=1">
				<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
					<tr height="30">
						<td align="left" valign="top">
							<span class="TitleText"><xsl:text>配对题（</xsl:text><xsl:value-of select="$mt_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$mt_sum"/><xsl:text>分）</xsl:text></span>
						</td>
					</tr>
				</table>
			</xsl:if>
			<!-- 题干 -->
			<table border="0" width="{$que_title_width}">
				<tr align="left">
					<td align="center" width="20px" valign="top">
						<a href="javascript:flagQue({$_pos});" class="Text" name="flag_layer{$_pos}" >
							<img src="{$wb_img_path}que_normal_flag.jpg" border="0"   id="flag_layer{$_pos}"/>
						</a>
					</td>
					<td width="20" valign="top" nowrap="nowrap">
						<span class="TitleText">
							<xsl:value-of select="$_pos"/>
							<xsl:text>.&#160;</xsl:text>
						</span>
					</td>
					<td  valign="top" style="word-break: break-all">
						<span class="Text">
							<xsl:for-each select="body/text() | body/html | body/interaction">
								<xsl:choose>
									<xsl:when test="name() = 'html'">
										<xsl:value-of disable-output-escaping="yes" select="."/>
									</xsl:when>
									<xsl:when test="name() = 'interaction'"/>
									<xsl:otherwise>
										<xsl:call-template name="unescape_html_linefeed">
											<xsl:with-param name="my_right_value">
												<xsl:value-of select="."/>
											</xsl:with-param>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</span>
					</td>
					<xsl:if test="$quiz/question[@order=$_pos]/body/object/@data != ''">
						<td valign="top">
							<img src="../{concat('resource/',$quiz/question[@order=$_pos]/@id, '/', ./body/object/@data)}"/>
						</td>
					</xsl:if>
				</tr>
			</table>
			
			<!-- 选项大小 -->
			<table width="100%" border="0">
				<tr>
					<td class="Text" align="right">
						<xsl:value-of select="$lab_option_size"/>
						<xsl:text>：</xsl:text>
						<input type="radio" name="opt_size_{$_pos}" id="opt_size_1_{$_pos}" onclick="changeOptionSize(OptionMinSize,{$_pos})"/>
						<label for="opt_size_1_{$_pos}">
							<xsl:value-of select="$lab_option_size_1"/>
						</label>
						<xsl:text>&#160;</xsl:text>
						<input type="radio" name="opt_size_{$_pos}" id="opt_size_2_{$_pos}" onclick="changeOptionSize(OptionMidSize,{$_pos})" checked="checked"/>
						<label for="opt_size_2_{$_pos}">
							<xsl:value-of select="$lab_option_size_2"/>
						</label>
						<xsl:text>&#160;</xsl:text>
						<input type="radio" name="opt_size_{$_pos}" id="opt_size_3_{$_pos}" onclick="changeOptionSize(OptionMaxSize,{$_pos})"/>
						<label for="opt_size_3_{$_pos}">
							<xsl:value-of select="$lab_option_size_3"/>
						</label>
						<xsl:text>&#160;</xsl:text>
					</td>
				</tr>
			</table>
			<!-- 选项 + 您的答案 -->
			<table border="0" cellpadding="0" cellspacing="0" style="border-bottom:1px solid black;">
				<!-- 表头 -->
				<tr valign="bottom">
					<td width="{$G0Parent_width}">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td width="50%">
									<xsl:call-template name="wb_ui_head">
										<xsl:with-param name="text" select="$lab_option_source"/>
										<xsl:with-param name="width">100%</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="wb_ui_line">
										<xsl:with-param name="width">100%</xsl:with-param>
									</xsl:call-template>
								</td>
								<td width="50%">
									<xsl:call-template name="wb_ui_head">
										<xsl:with-param name="text" select="$lab_option_target"/>
										<xsl:with-param name="width">100%</xsl:with-param>
									</xsl:call-template>
									<xsl:call-template name="wb_ui_line">
										<xsl:with-param name="width">100%</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</td>
					<td width="{$G0Result_width}">
						<xsl:call-template name="wb_ui_head">
							<xsl:with-param name="text" select="$lab_ans_here"/>
							<xsl:with-param name="width">100%</xsl:with-param>
							<xsl:with-param name="extra_td">
								<td align="right">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name" select="$lab_delete_all"/>
										<xsl:with-param name="wb_gen_btn_href">javascript:deleteAllOption(<xsl:value-of select="$_pos"/>)</xsl:with-param>
									</xsl:call-template>
								</td>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_ui_line">
							<xsl:with-param name="width">100%</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td>
						<!-- 选项 -->
						<!--<div id="G0{$_pos}Parent" class="div" style="position:absolute;border:{$border_width}px solid red;border-right:none;left:0;top:{$G0Parent_top};width:{$G0Parent_width};height:{$parent_height};overflow:auto;background:white;" onclick="showHideDragedDiv(event,{$_pos})" onmousemove="changeDragedPosition(event,{$_pos})">-->
						<div id="G0{$_pos}OptionTable" class="div" style="position:relative;width:{$G0Parent_width};height:{$parent_height};overflow:auto;" onclick="showHideDragedDiv(event,{$_pos})" onmousemove="changeDragedPosition(event,{$_pos})">
							<xsl:variable name="media_width">
								<xsl:choose>
									<xsl:when test="body/media">
										<xsl:value-of select="body/media/@width"/>
									</xsl:when>
									<xsl:otherwise>100</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="media_height">
								<xsl:choose>
									<xsl:when test="body/media">
										<xsl:value-of select="body/media/@height"/>
									</xsl:when>
									<xsl:otherwise>100</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>
							<xsl:variable name="_pos_left">40</xsl:variable>
							<xsl:variable name="_pos_top">80</xsl:variable>
							<xsl:variable name="_pos_left_2">410</xsl:variable>
							<xsl:variable name="_pos_top_2">80</xsl:variable>
							<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
								<tr valign="top">
									<!-- 来源 -->
									<td width="50%">
										<table border="0" width="100%" cellspacing="0" cellpadding="0">
											<xsl:for-each select="body/source/item">
												<xsl:variable name="item_id" select="@id"/>
												<xsl:variable name="_position" select="position()"/>
												<tr>
													<td>
														<table border="0" cellpadding="0" cellspacing="0" onmouseover="overOption('source', {$item_id}, {$_pos})" onmouseout="outOption('source', {$item_id}, {$_pos})">
															<xsl:if test="count(object)!=0">
																<tr>
																	<td height="20" align="center" width="20" rowspan="2">
																		<input type="radio" name="sourceRad" id="G0{$_pos}sourceRadio_{$item_id}"/>
																	</td>
																	<td id="G0{$_pos}sourceTd_{$item_id}">
																		<img id="G0{$_pos}sourceImg_{$item_id}" src="../{concat('resource/',../../../@id, '/', ./object/@data)}"/>
																	</td>
																</tr>
															</xsl:if>
															<tr>
																<xsl:if test="count(object)=0">
																	<td height="20" align="center" width="20">
																		<input type="radio" name="sourceRad" id="G0{$_pos}sourceRadio_{$item_id}"/>
																	</td>
																</xsl:if>
																<td valign="top">
																	<xsl:if test="count(object)=0">
																		<xsl:attribute name="id">G0<xsl:value-of select="$_pos"/>sourceTd_<xsl:value-of select="$item_id"/></xsl:attribute>
																	</xsl:if>
																	<div id="G0{$_pos}sourceText_{$item_id}" class="div">
																		<table border="0" cellspacing="0" cellpadding="0">
																			<tr>
																				<td class="Text">
																					<xsl:value-of select="./text"/>
																				</td>
																			</tr>
																		</table>
																	</div>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr height="40">
													<td>
														<img src="{$wb_img_path}tp.gif"/>
													</td>
												</tr>
											</xsl:for-each>
											<tr height="100">
												<td>
													<img src="{$wb_img_path}tp.gif"/>
												</td>
											</tr>
										</table>
									</td>
									<!-- 目标 -->
									<td width="50%" style="border-left:2px dashed gray;">
										<table border="0" width="100%" cellspacing="0" cellpadding="0">
											<xsl:for-each select="body/interaction">
												<xsl:variable name="int_ord" select="@order"/>
												<tr>
													<td>
														<table border="0" cellpadding="0" cellspacing="0" onmouseover="overOption('target', {$int_ord}, {$_pos})" onmouseout="outOption('target', {$int_ord}, {$_pos})">
															<xsl:if test="count(object)!=0">
																<tr>
																	<td height="20" align="center" width="20" rowspan="2">
																		<input type="radio" name="targetRad" id="G0{$_pos}targetRadio_{$int_ord}"/>
																	</td>
																	<td id="G0{$_pos}targetTd_{$int_ord}">
																		<img id="G0{$_pos}targetImg_{$int_ord}" src="../{concat('resource/',../../@id, '/', ./object/@data)}" align="middle"/>
																	</td>
																</tr>
															</xsl:if>
															<tr>
																<xsl:if test="count(object)=0">
																	<td height="20" align="center" width="20">
																		<input type="radio" name="targetRad" id="G0{$_pos}targetRadio_{$int_ord}"/>
																	</td>
																</xsl:if>
																<td valign="top">
																	<xsl:if test="count(object)=0">
																		<xsl:attribute name="id">G0<xsl:value-of select="$_pos"/>targetTd_<xsl:value-of select="$int_ord"/></xsl:attribute>
																	</xsl:if>
																	<div id="G0{$_pos}targetText_{$int_ord}" class="div">
																		<table border="0" cellspacing="0" cellpadding="0">
																			<tr>
																				<td class="Text">
																					<xsl:value-of select="./text"/>
																				</td>
																			</tr>
																		</table>
																	</div>
																</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr height="40">
													<td>
														<img src="{$wb_img_path}tp.gif"/>
													</td>
												</tr>
											</xsl:for-each>
											<tr height="100">
												<td>
													<img src="{$wb_img_path}tp.gif"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							<!-- 漂浮的层 -->
							<div id="G0{$_pos}DragedDiv" style="position:absolute;left:0;top:0;visibility:hidden;">
								<table cellpadding="0" cellspacing="0">
									<tr>
										<td>
											<img id="G0{$_pos}DragedImg" style="display:none"/>
										</td>
									</tr>
									<tr>
										<td>
											<div id="G0{$_pos}DragedText" class="div" style="position:absolute;overflow:hidden;"/>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</td>
					<td valign="top">
						<!-- 您的答案 -->
						<!--<div id="G0{$_pos}ResultParent" class="div" style="position:absolute;border:{$border_width}px solid red;left:{$G0Parent_width}px; top:{$G0Parent_top}px; width:{$G0Result_width}px; height:{$parent_height}px; overflow:auto; background:white;">-->
						<!--<div id="G0{$_pos}ResultParent" class="div" style="position:relative;width:{$G0Result_width}px;height:{$parent_height}px;overflow:auto;">-->
						<div id="G0{$_pos}AnswerTable" style="position:absolute;width:{$G0Result_width}px;height:{$parent_height}px;overflow:auto;border-left:1px solid black;"/>
					</td>
				</tr>
			</table>
			<center>
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<img src="{$wb_img_path}tp.gif" height="30" width="1" border="0"/>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<img src="{$wb_img_path}tp.gif" height="1" width="15" border="0"/>
						</td>
						<td>
						</td>
					</tr>
				</table>
			</center>
		</div>
		<!-- 一个看不到的table，用来复制成答案 -->
		<table id="G0{$_pos}cloneTable" border="0" cellpadding="0" cellspacing="0" style="display:none" onmouseover="overResult(event, this)" onmouseout="outResult(event, this)">
			<tr>
				<td>
					<img style="display:none"/>
				</td>
				<td>
					<img style="display:none"/>
				</td>
				<td rowspan="3" align="right" valign="top">
					<!-- add some space for FF, since the delete button will overlap with the table border -->
					<img src="{$wb_img_path}tp.gif"/>
					<br/>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_delete"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:deleteOneOption(<xsl:value-of select="$_pos"/>, this)</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<tr>
				<td>
					<div class="div" style="position:absolute;overflow:hidden;"/>
				</td>
				<td>
					<div class="div" style="position:absolute;overflow:hidden;"/>
				</td>
			</tr>
			<tr height="40">
				<td colspan="2">
					<img src="{$wb_img_path}tp.gif"/>
				</td>
			</tr>
		</table>
		<form name="frm{$_pos}" onSubmit="return status()">
			<input type="hidden" name="autosave_pos" value="{$_pos}"/>
			<input type="hidden" name="time_left" value=""/>
			<input type="hidden" name="cur_tkh_id" value="{../aicc_data/@tkh_id}"/>
			<input type="hidden" name="cur_mod_id" value="{../header/@mod_id}"/>
			<input type="hidden" name="cur_que_id" value="{$que_id}"/>
			<input type="hidden" name="sourcenum" value="{$sourcenumvari}"/>
			<input type="hidden" name="targetnum" value="{$targetnumvari}"/>
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="module" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<input type="hidden" name="start_time" value="{$quiz/@start_time}"/>
			<input type="hidden" name="save_by_usr" value=""/>
			<input type="hidden" name="flag" value=""/>
			<input type="hidden" name="int_size" value=""/>
			<input type="hidden" name="source_td_width" value="{$source_td_width}"/>
			<input type="hidden" name="target_radio_width" value="{$target_radio_width}"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_order" select="@order"/>
				<input type="hidden" name="autosave_q{$_pos}_i{@order}" value="{../../../restore/question[@id=$que_id]/interaction[@order=$_order]/@pas_response_bil}"/>
				<input type="hidden" name="responsebil_order_dbflag{@order}" value=""/>
			</xsl:for-each>
			<xsl:for-each select="body/source/item">
				<xsl:variable name="item_id" select="@id"/>
				<xsl:variable name="media_height">
					<xsl:choose>
						<xsl:when test="../../media">
							<xsl:value-of select="../../media/@height"/>
						</xsl:when>
						<xsl:otherwise>100</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="this_que" select="../../../../restore/question[@id=$que_id]"/>
				<xsl:variable name="_pos_top">150</xsl:variable>
				<xsl:variable name="_pos_left">
					<xsl:choose>
						<xsl:when test="$this_que != null or $this_que != ''">
							<xsl:choose>
								<xsl:when test="count($this_que/interaction/value[text() = $item_id]) > 0">410</xsl:when>
								<xsl:otherwise>40</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>40</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="my_pos_top">
					<xsl:choose>
						<xsl:when test="$this_que != null or $this_que != ''">
							<xsl:choose>
								<xsl:when test="count($this_que/interaction/value[text() = $item_id]) > 0">
									<xsl:variable name="int_order">
										<xsl:value-of select="$this_que/interaction[value = $item_id]/@order"/>
									</xsl:variable>
									<xsl:value-of select="$_pos_top + (15 * $int_order) + ($media_height * ($int_order - 1))"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$_pos_top + (15 * position()) + ($media_height * (position() - 1))"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$_pos_top + (15 * position()) + ($media_height * (position() - 1))"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<input type="hidden" name="Source{@id}_left" value="{$_pos_left}"/>
				<input type="hidden" name="Source{@id}_top" value="{$my_pos_top}"/>
			</xsl:for-each>
		</form>
		<xsl:comment> === Draw JS Function ===</xsl:comment>
		<script language="JavaScript" type="text/javascript"><![CDATA[
	function newG0]]><xsl:value-of select="$_pos"/><![CDATA[(){
		G0]]><xsl:value-of select="$_pos"/><![CDATA[ = new MM_interaction('G0]]><xsl:value-of select="$_pos"/><![CDATA[',1,0,null);
		for (var i=1; i<=]]><xsl:value-of select="count(body/interaction)"/><![CDATA[; i++ ){
			G0]]><xsl:value-of select="$_pos"/><![CDATA[.add('drag','Taget' + i,null,0,1,1,0);
		}]]><xsl:for-each select="body/source/item">
				<xsl:variable name="item_id" select="@id"/><![CDATA[
		for ( var j = 1; j <=]]><xsl:value-of select="count(../../../outcome//feedback[@condition=$item_id])"/><![CDATA[; j++) {
			G0]]><xsl:value-of select="$_pos"/><![CDATA[.add('drag','Source]]><xsl:value-of select="@id"/><![CDATA[_' + j,null,1,0,1,0);
			for ( var k=1; k<=]]><xsl:value-of select="count(../../interaction)"/><![CDATA[; k++ ){
			  G0]]><xsl:value-of select="$_pos"/><![CDATA[.add('dragTarg','Source]]><xsl:value-of select="@id"/><![CDATA[_' + j,'Taget' + k,0,0,20,'Center','0:0',1);			
			}		
		}]]></xsl:for-each><![CDATA[
		G0]]><xsl:value-of select="$_pos"/><![CDATA[.init();
	}
	if (window.newG0]]><xsl:value-of select="$_pos"/><![CDATA[ == null) window.newG0]]><xsl:value-of select="$_pos"/><![CDATA[ = newG0]]><xsl:value-of select="$_pos"/><![CDATA[;
	if (!window.MM_initIntFns) window.MM_initIntFns = ''; window.MM_initIntFns += 'newG0]]><xsl:value-of select="$_pos"/><![CDATA[();';
	]]></script>
		<script language="JavaScript" type="text/javascript"><![CDATA[
	ttl_pair = 0;]]><xsl:for-each select="body/source/item">
				<xsl:variable name="item_id" select="@id"/><![CDATA[
	ttl_pair += ]]><xsl:value-of select="count(../../../outcome//feedback[@condition=$item_id])"/><![CDATA[;]]></xsl:for-each><![CDATA[
	q = new CwQuestion ('layer]]><xsl:value-of select="$_pos"/><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[')
	for(i=1; i<= ttl_pair; i++){
		interaction = new CwInteraction (']]><xsl:value-of select="@id"/><![CDATA[',i, 'MT');
		q.addInteraction(interaction);
	}
	qset.addQuestion(q);]]><xsl:for-each select="../restore/question[@id = $que_id]/interaction"><![CDATA[
	var q=qset.getQuestion(]]><xsl:value-of select="$_pos"/><![CDATA[);	
	var targetId = ]]><xsl:value-of select="@order"/><![CDATA[;
	var answerstr = ']]><xsl:value-of select="@pas_response_bil"/><![CDATA[';
	var temp_answer_array=new Array();
	
	var ans_index=Number(]]><xsl:value-of select="$_pos"/><![CDATA[)-1;
	temp_answer_array=answerstr.split("~");
	
	for(var i=0;i<temp_answer_array.length-1;i++){
		if(temp_answer_array!=null||temp_answer_array!=""){
			q.answer_array[q.answer_array.length]=temp_answer_array[i]+"_"+targetId;
		}				
	}]]></xsl:for-each>
		</script>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- test player - mc interaction -->
	<xsl:template name="sc_que">
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:param name="my_start">0</xsl:param>
		<xsl:variable name="_pos" select="$my_start + position()"/>
		<xsl:variable name="_order" select="body/interaction/@order"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:variable name="cur_mod_id">
			<xsl:value-of select="../../../header/@mod_id"/>
		</xsl:variable>
		<xsl:variable name="cur_tkh_id">
			<xsl:value-of select="../../../aicc_data/@tkh_id"/>
		</xsl:variable>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}" class="mainContent" style="width:980px;z-index:90;" onmouseout="leaveQue({$_pos})">
			<form name="frm{$_pos}" onSubmit="return status()">
				<input type="hidden" name="module" value=""/>
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="time_left" value=""/>
				<input type="hidden" name="cur_tkh_id" value="{$cur_tkh_id}"/>
				<input type="hidden" name="cur_mod_id" value="{$cur_mod_id}"/>
				<input type="hidden" name="cur_que_id" value="{$que_id}"/>
				<input type="hidden" name="autosave_pos" value="{$_pos}"/>
				<input name="start_time" type="hidden" value="{$quiz/@start_time}"/>
				<input type="hidden" name="save_by_usr" value=""/>
				<input type="hidden" name="flag" value=""/>
				<input type="hidden" name="int_size" value=""/>
				<xsl:if test="position()=1">
					<table width="{$que_title_width}" border="0" cellspacing="0" cellpadding="5">
						<tr height="30">
							<td align="left" valign="top">
								<xsl:choose>
									<xsl:when test="../../header/@type = 'DSC'">
										<span class="TitleText"><xsl:text>动态情景题（</xsl:text><xsl:value-of select="$dsc_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$dsc_cnt"/><xsl:text>分）</xsl:text></span>
									</xsl:when>
									<xsl:when test="../../header/@type = 'FSC'">
										<span class="TitleText"><xsl:text>静态情景题（</xsl:text><xsl:value-of select="$fsc_cnt"/><xsl:text>题，共</xsl:text><xsl:value-of select="$fsc_cnt"/><xsl:text>分）</xsl:text></span>
									</xsl:when>
								</xsl:choose>
							</td>
						</tr>
					</table>
				</xsl:if>
				<xsl:if test="../../header/@type = 'FSC'">
					<xsl:apply-templates select="../.." mode="FSC"/>
				</xsl:if>
				<xsl:if test="../../header/@type = 'DSC'">
					<xsl:apply-templates select="../.." mode="DSC"/>
				</xsl:if>
				<table width="{$que_title_width}" border="0px" cellspacing="0" cellpadding="5">
					<tr>
						<td align="center" width="20px" valign="top">
							<a href="javascript:flagQue({$_pos});" class="Text" name="flag_layer{$_pos}">
								<img src="{$wb_img_path}que_normal_flag.jpg" border="0"    id="flag_layer{$_pos}"/>
							</a>
						</td>
						<td valign="top">
							<table cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td nowrap="nowrap" valign="top">
										<span class="TitleText">
											<xsl:value-of select="$_pos"/>
											<xsl:text>.&#160;</xsl:text>
										</span>
									</td>
									<td width="100%" style="word-break: break-all">
										<span class="Text">
											<xsl:for-each select="body/text() | body/html | body/interaction">
												<xsl:choose>
													<xsl:when test="name() = 'html'">
														<xsl:value-of disable-output-escaping="yes" select="."/>
													</xsl:when>
													<xsl:when test="name() = 'interaction'"/>
													<xsl:otherwise>
														<xsl:call-template name="unescape_html_linefeed">
															<xsl:with-param name="my_right_value">
																<xsl:value-of select="."/>
															</xsl:with-param>
														</xsl:call-template>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:for-each><br/>
											<xsl:apply-templates select="body/object"/>
										</span>
									</td>
								</tr>
							</table>
							<br/>
							<table width="100%" border="0" cellspacing="0" cellpadding="5">
								<tr>
									<td>
										<input type="hidden" name="autosave_q{$_pos}_i{$_order}" value="{../restore/question[@id=$que_id]/interaction/@pas_response_bil}"/>
										<input type="hidden" name="responsebil_order_dbflag{$_order}" value=""/>
										<xsl:for-each select="body/interaction[@type='MC']/option">
											<xsl:variable name="optionPos" select="position()"/>
											<xsl:variable name="optionId" select="@id"/>
											<table border="0" cellspacing="0" cellpadding="5">
												<tr>
													<td>
													<table><tr>
														<td valign = 'top'>
															<xsl:choose>
																<xsl:when test="../../../outcome/@logic='AND' or ../../../outcome/@logic='OR'">
																	<input type="checkbox" name="rdo_q{$_pos}_i{$_order}" value="{@id}" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer('');qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswerShuffle('')">
																		<xsl:for-each select="../../../../restore/question[@id=$que_id]/interaction/value">
																			<xsl:if test="$optionId=text()">
																				<xsl:attribute name="checked">checked</xsl:attribute>
																			</xsl:if>
																		</xsl:for-each>
																	</input>
																</xsl:when>
																<xsl:otherwise>
																	<input type="radio" name="rdo_q{$_pos}_i{$_order}" value="{@id}" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer(this.value);qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswerShuffle({$optionPos})">
																		<xsl:if test="$optionId=../../../../restore/question[@id=$que_id]/interaction/value">
																			<xsl:attribute name="checked">checked</xsl:attribute>
																		</xsl:if>
																	</input>
																</xsl:otherwise>
															</xsl:choose>
															
															<span class="TitleText">
																<xsl:number format="A. "/>
															</span>
														</td>
														<td valign = 'top'>
															<span class="Text">
																<xsl:choose>
																	<xsl:when test="html">
																		<xsl:value-of disable-output-escaping="yes" select="html"/>
																	</xsl:when>
																	<xsl:otherwise>
																		<xsl:call-template name="unescape_html_linefeed">
																			<xsl:with-param name="my_right_value">
																				<xsl:value-of select="."/>
																			</xsl:with-param>
																		</xsl:call-template>
																	</xsl:otherwise>
																</xsl:choose>
															</span>
															<xsl:apply-templates select="object"/>
														</td>
														</tr></table>
													</td>
												</tr>
											</table>
										</xsl:for-each>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<script language="JavaScript" type="text/javascript"><![CDATA[

				q = new CwQuestion ('layer]]><xsl:value-of select="$_pos"/><![CDATA[', ']]><xsl:value-of select="@id"/><![CDATA[')
				]]><xsl:variable name="mc_type">
				<xsl:choose>
					<xsl:when test="outcome/@logic = 'AND' or outcome/@logic = 'OR'">MC_M</xsl:when>
					<xsl:otherwise>MC</xsl:otherwise>
				</xsl:choose>
			</xsl:variable><![CDATA[
				interaction = new CwInteraction (']]><xsl:value-of select="@id"/><![CDATA[',']]><xsl:value-of select="$_order"/><![CDATA[', ']]><xsl:value-of select="$mc_type"/><![CDATA[', eval(getControlString("rdo_q",]]><xsl:value-of select="$_pos"/>,<xsl:value-of select="$_order"/><![CDATA[)));]]>
				<xsl:choose>
					<xsl:when test="$mc_type='MC_M'">
						<![CDATA[interaction.setAnswer('');interaction.setAnswerShuffle('');]]>
					</xsl:when>
					<xsl:otherwise>
						<![CDATA[interaction.setAnswer(']]><xsl:value-of select="../restore/question[@id=$que_id]/interaction/value"/><![CDATA[');]]>
					</xsl:otherwise>
				</xsl:choose>
				<![CDATA[q.addInteraction(interaction)
				qset.addQuestion(q)						
		]]></script>
	</xsl:template>
	<!-- ===========================================-->
	<!--Draw hidden field-->
	<xsl:template match="*" mode="quiz">
		<xsl:for-each select="question[body/interaction[@logic='SINGLE']]">
			<xsl:comment>question <xsl:value-of select="position()"/></xsl:comment>
			<xsl:variable name="_i" select="position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@logic='AND' or @logic='OR']]">
			<xsl:comment>question <xsl:value-of select="$mt_mc_start+position()"/></xsl:comment>
			<xsl:variable name="_i" select="$mt_mc_start+position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='FB']]">
			<xsl:comment>question <xsl:value-of select="$fb_start+position()"/></xsl:comment>
			<xsl:variable name="_i" select="$fb_start+position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='MT']]">
			<xsl:comment>question <xsl:value-of select="$mt_start+position()"/></xsl:comment>
			<xsl:variable name="_i" select="$mt_start+position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='TF']]">
			<xsl:comment>question <xsl:value-of select="$tf_start+position()"/></xsl:comment>
			<xsl:variable name="_i" select="$tf_start+position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
		<xsl:for-each select="question[body/interaction[@type='ES']]">
			<xsl:comment>question <xsl:value-of select="$es_start+position()"/></xsl:comment>
			<xsl:variable name="_i" select="$es_start+position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
		<xsl:for-each select="question[header[@type='FSC']]/question_list/question">
			<xsl:comment>question <xsl:value-of select="$fsc_start+position()"/></xsl:comment>
			<xsl:variable name="_i" select="$fsc_start+position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
		<xsl:for-each select="question[header[@type='DSC']]/question_list/question">
			<xsl:comment>question <xsl:value-of select="$dsc_start+position()"/></xsl:comment>
			<xsl:variable name="_i" select="$dsc_start+position()"/>
			<input name="atm_int_res_id_{$_i}" type="hidden"/>
			<input name="atm_flag_{$_i}" type="hidden"/>
			<xsl:for-each select="body/interaction">
				<xsl:variable name="_j" select="@order"/>
				<input name="atm_int_order_{$_i}_{$_j}" type="hidden"/>
				<input name="atm_response_{$_i}_{$_j}" type="hidden"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>

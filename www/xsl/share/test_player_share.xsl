<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="../wb_const.xsl"/>
	<xsl:import href="../utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="../utils/escape_doub_quo.xsl"/>
	<xsl:import href="../utils/escape_all.xsl"/>
	<xsl:import href="../utils/wb_utils.xsl"/>
	<xsl:import href="../utils/wb_css.xsl"/>
	<xsl:import href="../utils/wb_init_lab.xsl"/>
	<xsl:import href="../utils/wb_gen_button.xsl"/>	
	<xsl:import href="../cust/wb_cust_const.xsl"/>
	<!-- =============================================================== -->
	<xsl:template name="show_test_player">
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
	
	<!-- =========================== Label =========================== -->
	<xsl:variable name="button_submit" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'btn_submit')"/>
	<xsl:variable name="test_mc_and" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'test_mc_and')"/>
	<xsl:variable name="test_mc_single" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'test_mc_single')"/>
	<xsl:variable name="test_fb" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'test_fb')"/>
	<xsl:variable name="test_mt" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'test_mt')"/>
	<xsl:variable name="test_tf" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'test_tf')"/>
	<xsl:variable name="test_es" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'test_es')"/>
	<xsl:variable name="usr_name" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'usr_name')"/>
	
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
			<xsl:with-param name="lab_examinee_name">全名</xsl:with-param>
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
			<xsl:with-param name="lab_leave_prompt">你正在考试，如果此时离开，将保存本次考试次数，但不会记录本次考试结果。</xsl:with-param>
			<xsl:with-param name="lab_exam_more">更多</xsl:with-param>
			<xsl:with-param name="lab_exam_up">收起</xsl:with-param>
			<xsl:with-param name="lab_save_and_suspend">已經保存和暫停您的測驗，確定要退出該頁面嗎？</xsl:with-param>
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
			<xsl:with-param name="lab_examinee_name">全名</xsl:with-param>
			<xsl:with-param name="lab_prev">上一题</xsl:with-param>
			<xsl:with-param name="lab_next">下一题</xsl:with-param>
			<xsl:with-param name="lab_submit">递交</xsl:with-param>
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
			<xsl:with-param name="lab_already_mark">已标签</xsl:with-param>
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
			<xsl:with-param name="lab_leave_prompt">你正在考试，如果此时离开，将保存本次考试次数，但不会记录本次考试结果。</xsl:with-param>
			<xsl:with-param name="lab_exam_more">更多</xsl:with-param>
			<xsl:with-param name="lab_exam_up">收起</xsl:with-param>
			<xsl:with-param name="lab_save_and_suspend">已经保存和暂停您的测验，确定要退出该页面吗？</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	<xsl:template name="lang_en">
		<xsl:call-template name="test_player">
			<xsl:with-param name="lab_help">Help</xsl:with-param>
			<xsl:with-param name="lab_reset">Reset</xsl:with-param>
			<xsl:with-param name="lab_true">True</xsl:with-param>
			<xsl:with-param name="lab_false">False</xsl:with-param>
			<xsl:with-param name="lab_add_attachment">Add attachment</xsl:with-param>
			<xsl:with-param name="lab_complete">Complete</xsl:with-param>
			<xsl:with-param name="lab_time_left">Remaining time</xsl:with-param>
			<xsl:with-param name="lab_examinee_name">Name</xsl:with-param>
			<xsl:with-param name="lab_prev">Previous</xsl:with-param>
			<xsl:with-param name="lab_next">Next</xsl:with-param>
			<xsl:with-param name="lab_submit">Submit</xsl:with-param>
			<xsl:with-param name="lab_btn_save">Save and suspend</xsl:with-param>
			<xsl:with-param name="lab_help_intr">(Instruction:Drag the appoproiate item(s) to the right hand side.)</xsl:with-param>
			<xsl:with-param name="lab_questionnum">Number of question</xsl:with-param>
			<xsl:with-param name="lab_ans_here">Your answer</xsl:with-param>
			<xsl:with-param name="lab_delete_all">Delete all</xsl:with-param>
			<xsl:with-param name="lab_delete">Delete</xsl:with-param>
			<xsl:with-param name="lab_next_unanswer">Next unattempt</xsl:with-param>
			<xsl:with-param name="lab_next_mark">Next flagged</xsl:with-param>
			<xsl:with-param name="lab_mark">Flag</xsl:with-param>
			<xsl:with-param name="lab_unmark">Unflag</xsl:with-param>
			<xsl:with-param name="lab_unattempt">Unattempt</xsl:with-param>
			<xsl:with-param name="lab_already_answer">Attempted</xsl:with-param>
			<xsl:with-param name="lab_already_mark">Flagged</xsl:with-param>
			<xsl:with-param name="lab_option_size">Option size</xsl:with-param>
			<xsl:with-param name="lab_option_size_1">Small</xsl:with-param>
			<xsl:with-param name="lab_option_size_2">Medium</xsl:with-param>
			<xsl:with-param name="lab_option_size_3">Large</xsl:with-param>
			<xsl:with-param name="lab_option_source">Source</xsl:with-param>
			<xsl:with-param name="lab_option_target">Target</xsl:with-param>
			<xsl:with-param name="lab_total_score">Total score</xsl:with-param>
			<xsl:with-param name="lab_select_que">Choose question</xsl:with-param>
			<xsl:with-param name="lab_prev_page">Prev page</xsl:with-param>
			<xsl:with-param name="lab_next_page">Next page</xsl:with-param>
			<xsl:with-param name="lab_leave_prompt">你正在考试，如果此时离开，将保存本次考试次数，但不会记录本次考试结果。</xsl:with-param>
			<xsl:with-param name="lab_exam_more">more</xsl:with-param>
			<xsl:with-param name="lab_exam_up">收起</xsl:with-param>
			<xsl:with-param name="lab_save_and_suspend">You have saved and pause your test, do you want to exit the page?</xsl:with-param>
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
		<xsl:param name="lab_leave_prompt"/>
		<xsl:param name="lab_exam_more"/>
		<xsl:param name="lab_exam_up"/>
		<xsl:param name="lab_save_and_suspend"/>
		<noscript>
　             　	<iframe scr="*.htm"></iframe>
　　            </noscript>
		<head>
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
			<title>
				<xsl:value-of select="$wb_wizbank"/>
				<xsl:text> - </xsl:text>
				<xsl:value-of select="$quiz/header/title"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
<!-- 			<xsl:call-template name="wb_css"> -->
<!-- 				<xsl:with-param name="view">wb_ui</xsl:with-param> -->
<!-- 			</xsl:call-template> -->
<!-- 			<xsl:call-template name="wb_css"> -->
<!-- 				<xsl:with-param name="view">test_player</xsl:with-param> -->
<!-- 			</xsl:call-template> -->
			<link rel="stylesheet" href="../static/css/learner.css"/>
			<link rel="stylesheet" href="../static/js/bootstrap/css/bootstrap.css"/>
			<link rel="stylesheet" href="../static/css/exam.css"/>
			<link rel="stylesheet" href="../static/js/font-awesome/css/font-awesome.min.css"/>
			<script type="text/javascript" SRC="{$wb_js_path}jquery.js" LANGUAGE="JavaScript"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_{$wb_cur_lang}.js"/>
			
			<script type="text/javascript" src="{$wb_js_path}wb_module.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="../static/js/cwn_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}gen_utils.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}urlparam.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}menu.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}layer.js" language="JavaScript"/>
<!-- 			<script type="text/javascript" src="{$wb_js_path}wb_htmlplayer.js" language="JavaScript"/> -->
			<script type="text/javascript" src="{$wb_js_path}interactionclass.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}elemdragclass.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}behdraglayer.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}wb_es_lib.js" language="JavaScript"/>
			<script type="text/javascript" src="{$wb_js_path}overlib.js"/>
			<script type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<!--alert样式  -->
			<!-- <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> -->
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<!--alert样式  end -->
			<script language="JavaScript" type="text/javascript"><![CDATA[
		
	// ---------------- Initializaton ---------------------
	var refresh_parent = false;
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

/**
 *保存并退出
**/
function SaveAndSuspendFunc() {
	saveTestData();
	if (submit_test) {
		/* if(Dialog.alert(']]><xsl:value-of select="$lab_save_and_suspend"/><![CDATA[')){
			ifload = true;
			//parent.window.close();
		} */
		//暂停计时器
		clearInterval(timeRemainIntervalObj);
		Dialog.confirm({text:']]><xsl:value-of select="$lab_save_and_suspend"/><![CDATA[', callback: function (answer) {
				if(!answer){
				    //取消，开启计时器
				    timeRemainIntervalObj = setInterval('refreshTimeRemain()', 1000);
				}else{
				    //确定，退出当前网页
					ifload = true;
					parent.window.close();
				}
			}
		})
	}
}

/**
 *题目数据保存
**/
function saveTestData(){
	CwQuestionAutoSave(currentQue, true);
	if (!submit_test) {
		Dialog.alert(wb_msg_preview_mode_save_suspend);
		return;
	} else {
		if (isOnlineExam === 'true' && !window.parent.pauseed) {
			//remove the learner from online exam learner list
			removeOnlineList();
		}
		//CwQuestionAutoSave(currentQue, true);
		window.parent.track.unset_onunload();
		if(parent.window && parent.window.opener){
			refresh_parent = true;
		}
	}
}

function changeflag(){ifload = false; }

function helpFunc(que_id, tkh_id) {
	if (!submit_test) {
		Dialog.alert(wb_msg_preview_mode_help);
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

    window.addEventListener("storage", function(e){ 
		if(e.key == 'serverChange'){
			if(e.newValue != '' && e.newValue != undefined){
			 window.Dialog.alert(e.newValue);
			 window.localStorage.removeItem("serverChange");
			}
		} 
	});

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
	var ifload = true;
	//鼠标在页面内移动即代表学员参加考试
	window.addEventListener("load", function(){
			document.addEventListener("mousemove",function(){
				ifload = false;
			})
	})

	window.onbeforeunload = function(){
		//是否需要刷新父窗口，不管最后知否确认先刷新
    	if(refresh_parent){
	     	parent.window.opener.location.reload();
	    }

		//submit_test:是否预览状态
		//ifload:是否已经切换题目（代表是否已经做题）
		//submitted:是否提交试卷
		if(!submit_test || ifload || submitted){
			return;
		}

		//保存数据，该保存不安全，可能无法完整保存数据
		saveTestData();
	   	//离开前提示
    	return ']]><xsl:value-of select="$lab_leave_prompt"/><![CDATA[';
	    	
    }

	]]></script>
	<xsl:call-template name="new_css"/>
</head>
<body  leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="MM_initInteractions();setHidFlag();init();restoreMT();"
 oncontextmenu='return false'
 ondragstart='return false' 
 onselectstart ='return false' 
 onselect='document.selection.empty()'
 oncopy='document.selection.empty()' 
 onbeforecopy='return false' 
>
	<div class="upper">
		<div class="upper-info">
			<!-- 模块标题和按钮 -->
			<xsl:choose>
				<xsl:when test="$quiz/header/@show_save_and_suspend_ind=1">
					<div class="upper-tit"  style="width: 560px;"><xsl:value-of select="$quiz/header/title"/></div>
				</xsl:when>
				<xsl:otherwise>
					<div class="upper-tit" ><xsl:value-of select="$quiz/header/title"/></div>
				</xsl:otherwise>
			</xsl:choose>
		  
			<div class="upper-tool">
		  		<input id="help_btn" name="wiztxt" class="upper-btn upper-blue margin-right10" type="button" value="{$lab_help}" onclick="javascript:helpFunc({/quiz/@id}, '{/quiz/aicc_data/@tkh_id}')"/>
		   		<xsl:if test="$quiz/header/@show_save_and_suspend_ind=1">
		   			<input name="wiztxt" class="upper-btn upper-blue margin-right10" type="button" value="{$lab_btn_save}" onclick="javascript:SaveAndSuspendFunc()"/>
		   		</xsl:if>
		   		<input name="wiztxt" class="upper-btn upper-orange submit-test" type="button" value="{$button_submit}" onclick="javascript:submitTest();"/>
			</div>
		</div>  
	</div>
	<div class="area">
		<div class="area-info clearfix" id='over'>
			<xsl:if test="count($quiz/question[body/interaction[@type='MT']]) != 0">
				<script>
					if(!document.all){
						if(!document.getElementById){
							alert("Sorry, Matching Not Support Yet");
						}
					}
				</script>
			</xsl:if>
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

			<div class="right" id="head_td">
			
					<!-- 用户信息 -->
				<div class="xyd-user-box clearfix">
						
			       	<div class="wzb-user wzb-user68">
					     <a href="javascript:void(0)"><img class="wzb-pic" style="width:58px;height:58px;" src="{$quiz/user_photo}"/></a>
					     <p class="companyInfo" style="display: none;"></p>
				       	</div>
				
				   	<div class="xyd-user-content">
					    <p><span><xsl:value-of select="$lab_examinee_name"/>：</span><xsl:value-of select="$quiz/meta/cur_usr/@display_bil"/></p>
				   		<p><span><xsl:value-of select="$usr_name"/>：</span><xsl:value-of select="$quiz/meta/cur_usr/@site_usr_id"/></p>
					   	</div>
					   	
					</div> <!-- user_msg End -->
					
				   
					<!-- 测验信息 -->
				<p class="margin-top15 margin-left10" style="margin-top:12px;"><span class="margin-right15"><xsl:value-of select="$lab_questionnum"/>：</span><span id="total_que_value"></span></p>
				<p class="margin-top10 margin-left10"><span class="margin-right15"><xsl:value-of select="$lab_total_score"/>：</span><span id="total_score_value"><xsl:value-of select="$quiz/@total_score"/></span></p>
				<p class="margin-top10 margin-left10"><span class="margin-right15"><xsl:value-of select="$lab_time_left"/>：</span><span class="user-size" id="time_remain_value"></span></p>
				   
				<div class="pick">

					<div class="pick-tit"><xsl:value-of select="$lab_select_que"/></div>
					
					<!-- 题号索引 --> 
					<div class="pick-box">
						<xsl:for-each select="$quiz/question[body/interaction[@type!='DSC' and @type!='FSC']]">
							<xsl:if test="position() &lt; 31">
								<a id="slot_td_idx{position()}"  class="graycolor">
									<span class="UnSelectedQue" onclick="changeSlot('{position()}','flag_layer{position()}');" id="slot_idx{position()}"><xsl:if test="position() &lt; 10">0</xsl:if><xsl:value-of select="position()"/></span>
								</a>
							</xsl:if>
							<xsl:if test="position() = 30 and position() != last()"><div><input id="exam-more" type="button" value="{$lab_exam_more}" class="btn wzb-btn-blue" style="margin:0 0 0 180px;" /></div></xsl:if>
							<span class="exam-more-content" style="display:none;">
								<xsl:if test="position() &gt; 30">
									<a id="slot_td_idx{position()}"  class="graycolor">
										<span class="UnSelectedQue" onclick="changeSlot('{position()}','flag_layer{position()}');" id="slot_idx{position()}"><xsl:if test="position() &lt; 10">0</xsl:if><xsl:value-of select="position()"/></span>
									</a>
								</xsl:if>
							</span>
							<xsl:if test="position() = last()"><div><input id="exam-up" type="button" value="{$lab_exam_up}" class="btn wzb-btn-blue" style="display:none;margin:0 0 0 180px;" /></div></xsl:if>
						</xsl:for-each>
						<xsl:for-each select="$quiz/question[header[@type='FSC' or @type='DSC']]/question_list/question">
							<xsl:if test="position() &lt; 31">
								<a id="slot_td_idx{$fsc_start+position()}" href="javascript:changeSlot('{$fsc_start+position()}','flag_layer{$fsc_start+position()}');"  class="graycolor">
									<span class="UnSelectedQue" id="slot_idx{$fsc_start+position()}"><xsl:if test="$fsc_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$fsc_start+position()"/></span>
								</a>
							</xsl:if>
							<xsl:if test="position() = 30 and position() != last()"><div><input id="exam-more" type="button" value="{$lab_exam_more}" class="btn wzb-btn-blue" style="margin:0 0 0 180px;" /></div></xsl:if>
							<span class="exam-more-content" style="display:none;">
								<xsl:if test="position() &gt; 30">
									<a id="slot_td_idx{$fsc_start+position()}" href="javascript:changeSlot('{$fsc_start+position()}','flag_layer{$fsc_start+position()}');"  class="graycolor">
										<span class="UnSelectedQue" id="slot_idx{$fsc_start+position()}"><xsl:if test="$fsc_start+position() &lt; 10">0</xsl:if><xsl:value-of select="$fsc_start+position()"/></span>
									</a>
								</xsl:if>
							</span>
							<xsl:if test="position() = last()"><div><input id="exam-up" type="button" value="{$lab_exam_up}" class="btn wzb-btn-blue" style="display:none;margin:0 0 0 180px;" /></div></xsl:if>
						</xsl:for-each>
						<p id="inter_msg" class="TitleText" style="color:red;padding-left:60px;display:none"/>
						<script type="text/javascript">
						  $("#exam-more").click(function(){
						    $(".exam-more-content").show();
						    $("#exam-more").hide();
						    $("#exam-up").show();
						  })
						  $("#exam-up").click(function(){
						    $("#exam-more").show();
						    $(".exam-more-content").hide();
						    $("#exam-up").hide();
						  })
						</script>
					</div>
	
					<!-- 题目统计 -->
					<div style="height:15px;"></div>
					<div class="pick-info clearfix">
						<div class="pick-mess pick-mess-new">
					   		<div class="graycolor" style="display:inline-block;height:20px;width:20px;margin: -5px 0;"></div>
					    	<span>
					    		<xsl:value-of select="$lab_unattempt"/>
					    	</span>
					    	<font id="unattempt_value"> 0</font>       
					  	</div>
     
	    				<div class="pick-mess pick-mess-new">
	        				<div class="yellowcolor" style="display:inline-block;height:20px;width:20px;margin: -5px 0;"></div>
	        				<span>
	        					<xsl:value-of select="$lab_already_answer"/>
	        				</span>
	          				<font id="attempted_value"> 0</font> 
	     				</div>   
     
	     				<div class="pick-mess pick-mess-new">
	         				<div style="display:inline-block;height:2px;width:20px;margin: 0;background-color: #f00;"></div>
	          				<span>
		          				<xsl:value-of select="$lab_already_mark"/>
	          				</span>
	          				<font id="flagged_value"> 0</font>
	     				</div>
					</div>
					
					<div class="pick-desc">            
					     <p>
					     	<span>
						     	<a class="margin-right10" href="javascript:nextUnattemptQue()">
						     		<img width="30px" src="../static/images/leftnone.png" alt=""/>
						     		<span>
							     		<xsl:value-of select="$lab_next_unanswer"/>
						     		</span>
						     	</a>
					     	</span>
					     </p>
					     <p>
							<span>
						     	<a href="javascript:nextFlaggedQue()">
						     		<img width="30px" src="../static/images/leftsel.png" alt=""/>
						     		<span>
							     		<xsl:value-of select="$lab_next_mark"/>
						     		</span>
						     	</a>
							</span>
					     </p>
					     <p>
					     	<span>
						     	<a class="margin-right30" href="javascript:flagQue(frmResult.cur_que_id.value);redrawSlotInd()">
						     		<i class="fa fa-star" style="color: #f00;font-size:20px;margin-left:12px"></i> 
						     		<span>
							     		<xsl:value-of select="$lab_mark"/>
						     		</span>
						     	</a>
					     	</span>
					     </p>
					     <p>
					     	<span >
						     	<a href="javascript:unflagQue(frmResult.cur_que_id.value);redrawSlotInd()">
						     		<i class="fa fa-star" style="color:#ccc;font-size:20px;margin-left:12px"></i> 
						     		<span>
							     		<xsl:value-of select="$lab_unmark"/>
						     		</span>
						     	</a>
					     	</span>
					     </p>
					</div>
				</div>
				<div class="margin-top10"></div>
				<form name="frmResult" method="post" action="{$wb_servlet_qdbaction_url}"> 
				<input name="cmd" type="hidden" value=""/>
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
				<label id="hidden_que_varible" style="display:none"/>
				<xsl:apply-templates select="$quiz" mode="quiz"/>
				<input name="url_success" type="hidden"/>
				<input name="url_failure" type="hidden"/>
			</form>
			</div> <!-- right End -->
		</div>
		<div id="testBottom" style="position: absolute;visibility: hidden;">
			<img src="{$wb_img_path}tp.gif" height="0" width="1" border="0"/>
		</div>
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
		<xsl:apply-templates select="descendant::question[header/@type != 'FSC' and header/@type != 'DSC']" mode="TST">
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
	</xsl:template>
	<!-- =============================================================== -->
	<!--  test player - es interaction  -->
	<xsl:template match="question[body/interaction[@type='ES']]" mode="TST">
		<xsl:param name="lab_add_attachment"/>
		<xsl:param name="lab_help"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:variable name="_pos" select="position()"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}" class="left" style="z-index:{$_pos};">
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
				<dl class="list clearfix">
			   		<dt>
						<a href="javascript:;" id="flagStar{$_pos}" onclick="javascript:flagQue({$_pos});redrawSlotInd();">
			   				<i class="fa grayC999 fa-star" />
			   			</a>
					</dt>
			     	<dd>
			       		<div class="list_tit">
			       			<div style="padding-right: 5px;">
			       				<xsl:value-of select="$_pos"/>.
			       				<span>[<xsl:value-of select="$test_es"/>]</span>
			       			</div>
						</div>
						<P class="fuwenben-list">
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
							<xsl:apply-templates select="body/object"/>
							<xsl:variable name="que_submit_file_ind" select="header/@que_submit_file_ind"/>
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
									<img border="0" height="1" src="{$wb_img_path}tp.gif" width="10"/>
									<!-- 问答题屏蔽掉上传文件入口 -->
									<!-- <input type="button" class="btn wzb-btn-blue"  style="padding:3px 8px;">
										<xsl:attribute name="value"><xsl:value-of select="$lab_add_attachment"/></xsl:attribute>
										<xsl:attribute name="onclick">javascript:es_add_file_field(<xsl:value-of select="$que_id"/>,<xsl:value-of select="$_pos"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:attribute>
									</input>
									<xsl:text>&#160;</xsl:text>
									<a class="Text" href="javascript:;">
										<xsl:attribute name="onmouseover">helpTooltip();</xsl:attribute>
										<xsl:attribute name="onmouseout">helpTooltip();</xsl:attribute>
										<xsl:value-of select="$lab_help"/>
									</a>
									<div id="help_tooltip" class="questipDesc" style="display: none; border: 1px solid #CCC; background: #EEE;padding: 3px;">
										<script LANGUAGE="JavaScript" TYPE="text/javascript">
											<![CDATA[document.write(wb_msg_upload_attach_inst)]]>
										</script>
									</div> -->
								</xsl:if>
							</xsl:for-each>
						</P>
						<span id="es_file_{$que_id}"/>
						<xsl:for-each select="body/interaction">
							<textarea class="wbpl showhide" style="margin:10px 0"  name="txt_q{$_pos}_i{@order}" cols="70" rows="10" onChange="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value)" onKeyPress="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value);">
								<xsl:value-of select="../../../restore/question[@id=$que_id]/interaction/value"/>
							</textarea>
							<input type="hidden" name="autosave_q{$_pos}_i{@order}" value="{../../../restore/question[@id=$que_id]/interaction/value}"/>
							<input type="hidden" name="responsebil_order_dbflag{@order}" value=""/>
						</xsl:for-each>
			   		</dd>
				</dl>
				
				<div class="area_page">
					<a class="btn wzb-btn-blue margin-right4" id="prevQue{$_pos}"  onclick="javascript:prevQue();"><xsl:value-of select="$lab_prev"/></a>
			   		<a class="btn wzb-btn-blue margin-right4" id="nextQue{$_pos}"  onclick="javascript:nextQue()"><xsl:value-of select="$lab_next"/></a>
			   		<a class="btn wzb-btn-blue margin-right4  submit-test" id="submitQue{$_pos}" style="visibility:hidden;"  onclick="javascript:submitTest();"><xsl:value-of select="$button_submit"/></a>
			   		
			 	</div>
			 				 	<div id="showMsg{$_pos}" style="display:none;">
				 	<br/>
			 	</div>
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
	<xsl:template match="question[body/interaction[@type='FB']]" mode="TST">
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:variable name="_pos" select="position()"/>
		<xsl:variable name="que_id" select="@id"/>
		<!--<xsl:variable name="int_cnt" select="count(body/interaction)"/>-->
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}" class="left" style="z-index:{$_pos};">
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
				<dl class="list clearfix">
			   		<dt>
						<a href="javascript:;" id="flagStar{$_pos}" onclick="javascript:flagQue({$_pos});redrawSlotInd();">
			   				<i class="fa grayC999 fa-star"/>
			   			</a>
					</dt>
			     	<dd>
			       		<div class="list_tit">
			       			<div style="padding-right: 5px;">
			       				<xsl:value-of select="$_pos"/>.
			       				<span>[<xsl:value-of select="$test_fb"/>]</span>
			       			</div>
			       		</div>
			       		<p class="fuwenben-list">
			       			<xsl:for-each select="body/text() | body/html | body/interaction">
								<xsl:variable name="int_order" select="@order"/>
								<xsl:choose>
									<xsl:when test="name() = 'interaction'">
										<input class="wzb-inputText" type="text" name="txt_q{$_pos}_i{@order}" size="{@length}" onChange="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value)" onKeyPress="qset.getQuestion({$_pos}).getInteraction({@order}).setAnswer(this.value)" value="{../../../restore/question[@id=$que_id]/interaction[@order=$int_order]}"  style="border-bottom:1px solid;text-align:center;margin:0 5px 5px 0;"/>
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
							<xsl:apply-templates select="body/object"/>
			       		</p>
			   		</dd>
				</dl>
				
				<div class="area_page">
					<a  class="btn wzb-btn-blue margin-right4" id="prevQue{$_pos}"  onclick="javascript:prevQue()"><xsl:value-of select="$lab_prev"/></a>
			   		<a  class="btn wzb-btn-blue margin-right4" id="nextQue{$_pos}" onclick="javascript:nextQue()"><xsl:value-of select="$lab_next"/></a>
			   		<a class="btn wzb-btn-blue margin-right4  submit-test" id="submitQue{$_pos}" style="visibility:hidden;" onclick="javascript:submitTest();"><xsl:value-of select="$button_submit"/></a>
			   		
			 	</div>
			 				 	<div id="showMsg{$_pos}" style="display:none;">
				 	<br/>
			 	</div>
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
		<p class="dcmess">
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
			<xsl:for-each select="body/object">
				<xsl:apply-templates select="."/>
			</xsl:for-each>
		</p>
	</xsl:template>
	<!--================================================= -->
	<xsl:template match="*" mode="FSC">
		<p class="dcmess">
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
			<xsl:for-each select="body/object">
				<xsl:value-of select="position()"/> : 
				<xsl:apply-templates select="."/>
			</xsl:for-each>
		</p>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- test player - mc interaction -->
	<xsl:template match="question[body/interaction[@type='MC']]" mode="TST">
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:variable name="_pos" select="position()"/>
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
		<div id="layer{$_pos}" class="left" style="z-index:{$_pos};">
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
				<xsl:if test="../../header/@type = 'FSC'">
					<xsl:apply-templates select="../.." mode="FSC"/>
				</xsl:if>
				<xsl:if test="../../header/@type = 'DSC'">
					<xsl:apply-templates select="../.." mode="DSC"/>
				</xsl:if>
				<dl class="list clearfix">
			   		<dt>
						<a href="javascript:;" id="flagStar{$_pos}" onclick="javascript:flagQue({$_pos});redrawSlotInd();">
			   				<i class="fa fa-star color-gray999"/>
			   			</a>
					</dt>
			     	<dd>
			       		<div class="list_tit">
			       			<div style="padding-right: 5px;">
			       				<xsl:value-of select="$_pos"/>.
				       			<xsl:choose>
				       				<xsl:when test="body/interaction[@type='MC']/@logic='AND' or body/interaction[@type='MC']/@logic='OR'">
				       					<span>[<xsl:value-of select="$test_mc_and"/>]</span>
				       				</xsl:when>
				       				<xsl:otherwise>
				       					<span>[<xsl:value-of select="$test_mc_single"/>]</span>
				       				</xsl:otherwise>
				       			</xsl:choose>
			       			</div>
			       		</div>
		       			<p class="fuwenben-list">
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
						</p>
			            
			            <input type="hidden" name="autosave_q{$_pos}_i{$_order}" value="{../restore/question[@id=$que_id]/interaction/@pas_response_bil}"/>
						<input type="hidden" name="responsebil_order_dbflag{$_order}" value=""/>
						<xsl:for-each select="body/interaction[@type='MC']/option">
							<xsl:variable name="optionPos" select="position()"/>
							<xsl:variable name="optionId" select="@id"/>
							<div class="list-info">
								<label for="rdo_q{$_pos}_i{@id}" style="padding:0 0 0 48px;">
									
										<xsl:choose>
											<xsl:when test="../../../outcome/@logic='AND' or ../../../outcome/@logic='OR'">
												<input type="checkbox" id="rdo_q{$_pos}_i{@id}" class="xsval" style="margin:0 0 2px -48px;" name="rdo_q{$_pos}_i{$_order}" value="{@id}" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer('');qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswerShuffle('')">
													<xsl:for-each select="../../../../restore/question[@id=$que_id]/interaction/value">
														<xsl:if test="$optionId=text()">
															<xsl:attribute name="checked">checked</xsl:attribute>
														</xsl:if>
													</xsl:for-each>
												</input>
											</xsl:when>
											<xsl:otherwise>
												<input type="radio" id="rdo_q{$_pos}_i{@id}" class="xsval" style="margin:0 0 2px -48px;" name="rdo_q{$_pos}_i{$_order}" value="{@id}" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer(this.value);qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswerShuffle({$optionPos})">
													<xsl:if test="$optionId=../../../../restore/question[@id=$que_id]/interaction/value">
														<xsl:attribute name="checked">checked</xsl:attribute>
													</xsl:if>
												</input>
											</xsl:otherwise>
										</xsl:choose>
										
										<span style="margin:0 8px 0;">
											<xsl:number format="A."/>
										</span>
										<span class="fuwenben-list">										
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
											<xsl:apply-templates select="object"/>
										</span>
										
								</label>
							</div>
						</xsl:for-each>   
			   		</dd>
				</dl>
     
			 	<div class="area_page">
					<a  class="btn wzb-btn-blue margin-right4" id="prevQue{$_pos}" onclick="javascript:prevQue()"><xsl:value-of select="$lab_prev"/></a>
			   		<a  class="btn wzb-btn-blue margin-right4" id="nextQue{$_pos}"  onclick="javascript:nextQue()"><xsl:value-of select="$lab_next"/></a>
			   		<a class="btn wzb-btn-blue margin-right4 submit-test" id="submitQue{$_pos}" style="visibility:hidden;" onclick="submitTest();"><xsl:value-of select="$button_submit"/></a>
			 	</div>
			 	<div id="showMsg{$_pos}" style="display:none;">
				 	<br/>
			 	</div>
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
	<xsl:template match="question[body/interaction[@type='TF']]" mode="TST">
		<xsl:param name="lab_true"/>
		<xsl:param name="lab_false"/>
		<xsl:param name="lab_prev"/>
		<xsl:param name="lab_next"/>
		<xsl:variable name="_pos" select="position()"/>
		<xsl:variable name="_order" select="body/interaction/@order"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}" class="left" style="z-index:{$_pos};">
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
				<dl class="list clearfix">
			   		<dt>
						<a href="javascript:;" id="flagStar{$_pos}" onclick="javascript:flagQue({$_pos});redrawSlotInd();">
			   				<i class="fa grayC999 fa-star" />
			   			</a>
					</dt>
			     	<dd>
			       		<div class="list_tit fuwenben-list">
			       			<div style="padding-right: 5px;">
			       				<xsl:value-of select="$_pos"/>.
			       				<span>[<xsl:value-of select="$test_tf"/>]</span>
			       			</div>
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
						</div>
						<p class="dcmess">
							<label for="rdo_q{$_pos}_i{$_order}_1" style="width:100%">
								<input id="rdo_q{$_pos}_i{$_order}_1" type="radio" name="rdo_q{$_pos}_i{$_order}" class="xsval" value="1" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer(this.value)">
									<xsl:if test="../restore/question[@id=$que_id]/interaction/value=1">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="10"/>
								<xsl:value-of select="$lab_true"/>
							</label>
						</p>
						<p class="dcmess">
							<label for="rdo_q{$_pos}_i{$_order}_2" style="width:100%">
								<input type="radio" id="rdo_q{$_pos}_i{$_order}_2" name="rdo_q{$_pos}_i{$_order}" class="xsval" value="2" onClick="qset.getQuestion({$_pos}).getInteraction({$_order}).setAnswer(this.value)">
									<xsl:if test="../restore/question[@id=$que_id]/interaction/value=2">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
								</input>
								<img border="0" height="1" src="{$wb_img_path}tp.gif" width="10"/>
								<xsl:value-of select="$lab_false"/>
							</label>
						</p>
						<input type="hidden" name="autosave_q{$_pos}_i{$_order}" value="{../restore/question[@id=$que_id]/interaction/value}"/>
						<input type="hidden" name="responsebil_order_dbflag{$_order}" value=""/>
						<xsl:apply-templates select="body/object"/>
			   		</dd>
				</dl>
				
				<div class="area_page">
					<a  class="btn wzb-btn-blue margin-right4" id="prevQue{$_pos}"  onclick="javascript:prevQue()"><xsl:value-of select="$lab_prev"/></a>
			   		<a  class="btn wzb-btn-blue margin-right4" id="nextQue{$_pos}"  onclick="javascript:nextQue()"><xsl:value-of select="$lab_next"/></a>
			   		<a class="btn wzb-btn-blue margin-right4 submit-test" id="submitQue{$_pos}" style="visibility:hidden;" onclick="javascript:submitTest();"><xsl:value-of select="$button_submit"/></a>
			   		
			 	</div>
			 	<div id="showMsg{$_pos}" style="display:none;">
				 	<br/>
			 	</div>
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
	<xsl:template match="question[body/interaction[@type='MT']]" mode="TST">
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
		<xsl:variable name="_pos" select="position()"/>
		<xsl:variable name="que_id" select="@id"/>
		<xsl:variable name="border_width">1</xsl:variable>
		<xsl:variable name="layer_width">1000</xsl:variable>
		<xsl:variable name="layer_height">700</xsl:variable>
		<xsl:variable name="div_height">50</xsl:variable>
		<xsl:variable name="parent_height" select="count(body/source/item)*35 + 80"/>
		<xsl:variable name="G0Parent_top">90</xsl:variable>
		<xsl:variable name="G0Parent_table_width">599</xsl:variable>
		<xsl:variable name="G0Parent_width">750</xsl:variable>
		<xsl:variable name="G0Result_width">250</xsl:variable>
		<xsl:variable name="source_td_width">200</xsl:variable>
		<xsl:variable name="target_radio_width">20</xsl:variable>
		<xsl:variable name="sourcenumvari" select="count($quiz/question[@id=$que_id]/body/source/item)"/>
		<xsl:variable name="targetnumvari" select="count($quiz/question[@id=$que_id]/body/interaction)"/>
		<xsl:comment>============ layer<xsl:value-of select="$_pos"/> ===========================================</xsl:comment>
		<div id="layer{$_pos}" class="left" style="overflow:visible">
			
			<!-- 题干 -->
			<dl class="list clearfix">
		   		<dt>
					<a href="javascript:;" id="flagStar{$_pos}" onclick="javascript:flagQue({$_pos});redrawSlotInd();">
		   				<i class="fa grayC999 fa-star" />
		   			</a>
				</dt>
		     	<dd>
		       		<div class="list_tit fuwenben-list">
		       			<div style="padding-right: 5px;">
			       				<xsl:value-of select="$_pos"/>.
		       				<span>[<xsl:value-of select="$test_mt"/>]</span>
		       			</div>
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
						<xsl:if test="$quiz/question[@order=$_pos]/body/object/@data != ''">
							<img src="../{concat('resource/',$quiz/question[@order=$_pos]/@id, '/', ./body/object/@data)}"/>
						</xsl:if>
					</div>
					<p class="dcmess">
						<div class="mt_head"><xsl:value-of select="$lab_option_source"/></div>
						<div class="mt_head"><xsl:value-of select="$lab_option_target"/></div>
						<div class="mt_head" style="padding-left:20px;">
							<xsl:value-of select="$lab_ans_here"/>
							<span style="float:right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_delete_all"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:deleteAllOption(<xsl:value-of select="$_pos"/>)</xsl:with-param>
						</xsl:call-template>
							</span>
						</div>
					</p>
					<div id="G0{$_pos}OptionTable" style="position:relative;height:{$parent_height}px;overflow:auto;float:left;width:400px;" onclick="showHideDragedDiv(event,{$_pos})" onmousemove="changeDragedPosition(event,{$_pos})">
						<span style="width:170px;float:left;height:{$parent_height}px;margin:0 0 10px 0;">
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
							
							<xsl:for-each select="body/source/item">
								<xsl:variable name="item_id" select="@id"/>
								<xsl:variable name="_position" select="position()"/>
								<p class="dcmess margin-bottom10" onmouseover="overOption('source', {$item_id}, {$_pos})" onmouseout="outOption('source', {$item_id}, {$_pos})" style="height:auto;">
									<xsl:if test="count(object)!=0">
										<input type="radio" name="sourceRad" id="G0{$_pos}sourceRadio_{$item_id}"/>
										<!-- <span id="G0{$_pos}sourceTd_{$item_id}">
											<img id="G0{$_pos}sourceImg_{$item_id}" src="../{concat('resource/',../../../@id, '/', ./object/@data)}"/>
										</span>
										<br/>
										<img border="0" src="{$wb_img_path}tp.gif" width="13"/> -->
									</xsl:if>
									<xsl:if test="count(object)=0">
										<input type="radio" name="sourceRad" id="G0{$_pos}sourceRadio_{$item_id}"/>
									</xsl:if>
									<xsl:if test="count(object)=0">
										<xsl:attribute name="id">G0<xsl:value-of select="$_pos"/>sourceTd_<xsl:value-of select="$item_id"/></xsl:attribute>
									</xsl:if>
									<span id="G0{$_pos}sourceText_{$item_id}" style="line-height:18px;">
										<xsl:value-of select="./text"/>
									</span>
									<xsl:if test="count(object)!=0">
										<br/>
										<span id="G0{$_pos}sourceTd_{$item_id}">
											<img id="G0{$_pos}sourceImg_{$item_id}" src="../{concat('resource/',../../../@id, '/', ./object/@data)}"/>
										</span>
										<br/>
										<img border="0" src="{$wb_img_path}tp.gif" width="13"/>
									</xsl:if>
								</p>
							</xsl:for-each>
						</span>
						<span style="border-left:1px dashed gray;width:190px;float:left;padding:0 0 0 10px;">
							<xsl:for-each select="body/interaction">
								<xsl:variable name="int_ord" select="@order"/>
								<p class="dcmess margin-bottom10" style="padding-left:3px;height:auto;" onmouseover="overOption('target', {$int_ord}, {$_pos})" onmouseout="outOption('target', {$int_ord}, {$_pos})">
									<xsl:if test="count(object)!=0">
										<input type="radio" name="targetRad" id="G0{$_pos}targetRadio_{$int_ord}"/>
										<!-- <span id="G0{$_pos}targetTd_{$int_ord}">
											<img id="G0{$_pos}targetImg_{$int_ord}" src="../{concat('resource/',../../@id, '/', ./object/@data)}" align="middle"/>
										</span>
										<br/>
										<img border="0" src="{$wb_img_path}tp.gif" width="13"/> -->
									</xsl:if>
									<xsl:if test="count(object)=0">
										<input type="radio" name="targetRad" id="G0{$_pos}targetRadio_{$int_ord}"/>
									</xsl:if>
									<xsl:if test="count(object)=0">
										<xsl:attribute name="id">G0<xsl:value-of select="$_pos"/>targetTd_<xsl:value-of select="$int_ord"/></xsl:attribute>
									</xsl:if>
									<span id="G0{$_pos}targetText_{$int_ord}" style="line-height:18px;">
										<xsl:value-of select="./text"/>
									</span>
									<xsl:if test="count(object)!=0">
										<br/>
										<span id="G0{$_pos}targetTd_{$int_ord}">
											<img id="G0{$_pos}targetImg_{$int_ord}" src="../{concat('resource/',../../@id, '/', ./object/@data)}" align="middle"/>
										</span>
										<br/>
										<img border="0" src="{$wb_img_path}tp.gif" width="13"/>
									</xsl:if>
								</p>
							</xsl:for-each>
						</span>
						<div id="G0{$_pos}DragedDiv" style="position:absolute;left:0;top:0;visibility:hidden;">
							<img id="G0{$_pos}DragedImg" style="display:none"/>
							<div id="G0{$_pos}DragedText" style="position:absolute;overflow:hidden;"/>
						</div>
					</div>
					<div id="G0{$_pos}AnswerTable" style="position: relative;width:200px;height:{$parent_height}px;overflow:auto;border-left:1px solid black;float:left;"/>
				</dd>
			</dl>
			
			<!-- 选项大小 -->
<!-- 			<table width="100%" border="0"> -->
<!-- 				<tr> -->
<!-- 					<td class="Text" align="right"> -->
<!-- 						<xsl:value-of select="$lab_option_size"/> -->
<!-- 						<xsl:text>:</xsl:text> -->
<!-- 						<input type="radio" name="opt_size_{$_pos}" id="opt_size_1_{$_pos}" onclick="changeOptionSize(OptionMinSize,{$_pos})"/> -->
<!-- 						<label for="opt_size_1_{$_pos}"> -->
<!-- 							<xsl:value-of select="$lab_option_size_1"/> -->
<!-- 						</label> -->
<!-- 						<xsl:text>&#160;</xsl:text> -->
<!-- 						<input type="radio" name="opt_size_{$_pos}" id="opt_size_2_{$_pos}" onclick="changeOptionSize(OptionMidSize,{$_pos})" checked="checked"/> -->
<!-- 						<label for="opt_size_2_{$_pos}"> -->
<!-- 							<xsl:value-of select="$lab_option_size_2"/> -->
<!-- 						</label> -->
<!-- 						<xsl:text>&#160;</xsl:text> -->
<!-- 						<input type="radio" name="opt_size_{$_pos}" id="opt_size_3_{$_pos}" onclick="changeOptionSize(OptionMaxSize,{$_pos})"/> -->
<!-- 						<label for="opt_size_3_{$_pos}"> -->
<!-- 							<xsl:value-of select="$lab_option_size_3"/> -->
<!-- 						</label> -->
<!-- 						<xsl:text>&#160;</xsl:text> -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 			</table> -->
			<div class="area_page">
				<a  class="btn wzb-btn-blue margin-right4" id="prevQue{$_pos}"  onclick="javascript:prevQue()"><xsl:value-of select="$lab_prev"/></a>
		   		<a  class="btn wzb-btn-blue margin-right4" id="nextQue{$_pos}"  onclick="javascript:nextQue()"><xsl:value-of select="$lab_next"/></a>
		   		<a class="btn wzb-btn-blue margin-right4 submit-test" id="submitQue{$_pos}" style="visibility:hidden;" onclick="javascript:submitTest();"><xsl:value-of select="$button_submit"/></a>
		 	</div>
		 		<div id="showMsg{$_pos}" style="display:none;">
				 	<br/>
			 	</div>
		</div>
		<!-- 一个看不到的table，用来复制成答案 -->
		<table id="G0{$_pos}cloneTable" border="0" cellpadding="0" cellspacing="0" style="display:none">
			<tr>
				<td width="80px;">
					<img style="display:none"/>
				</td>
				<td width="80px;">
					<img style="display:none"/>
				</td>
				<td name="delBut" rowspan="3" align="right" valign="top">
					<!-- add some space for FF, since the delete button will overlap with the table border -->
<!-- 					<img src="{$wb_img_path}tp.gif"/> -->
<!-- 					<br/> -->
<!-- 					<xsl:call-template name="wb_gen_form_button"> -->
<!-- 						<xsl:with-param name="wb_gen_btn_name" select="$lab_delete"/> -->
<!-- 						<xsl:with-param name="wb_gen_btn_href">javascript:deleteOneOption(<xsl:value-of select="$_pos"/>, this)</xsl:with-param> -->
<!-- 					</xsl:call-template> -->
					<a href="javascript:;" onclick="javascript:deleteOneOption({$_pos}, this);">
						<i class="glyphicon grayCdbd f14 glyphicon-remove"/>
					</a>
				</td>
			</tr>
			<tr>
				<td width="80px;" valign="top" onmouseover="overResult(event, this)" onmouseout="outResult(event, this)">
					<div class="div" style="line-height: 18px;"/>
				</td>
				<td width="80px;" valign="top" onmouseover="overResult(event, this)" onmouseout="outResult(event, this)">
					<div class="div" style="line-height: 18px;"/>
				</td>
			</tr>
			<tr height="25">
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
	<!-- ===========================================-->
	<!--Draw hidden field-->
	<xsl:template match="*" mode="quiz">
		<xsl:for-each select="descendant::question[header/@type != 'FSC' and header/@type != 'DSC']">
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
	</xsl:template>

</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/completion_criteria_cond_lst.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/CourseCriteria/meta/item/@id"/>
	<xsl:variable name="ccr_id" select="/CourseCriteria/comp_cond_lst/ccr/@id"/>
	<xsl:variable name="content_def" select="/CourseCriteria/meta/item/@content_def"/>
	<xsl:variable name="current_role" select="/CourseCriteria/current_role"/>
	
	<xsl:variable name="is_new_cos">
		<xsl:choose>
			<xsl:when test="/CourseCriteria/is_new_cos/text() ='true'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="lab_next_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '101')"/>
	<xsl:variable name="lab_ok_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '872')"/>
	<!-- 课程、考试的总分并不是直接取在学习内容的所得分，而是通过<b>计分项目</b>功能中设置的计分规则计算而得到。所以，如果你现在要发布的这门课程是需要以学员最后获得分数多少来衡量学员的学习结果的，那么你必须先设置好计分项目后才能发布课程、考试。否则，学员在该课程、考试的得分将为0分。对于分数的计算规则，详细请看计分项目功能页面的说明。 -->
	<xsl:variable name="label_core_training_management_318" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_318')" />
	
	<xsl:variable name="editable">
		<xsl:value-of select="/CourseCriteria/editable/text()"/>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/CourseCriteria">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_comp_cond.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_attendance.js"/>
			<script language="JavaScript"><![CDATA[
			var itm_lst = new wbItem;
			var attn = new wbAttendance;
			var cond =  new wbCompCond;
			
			var content_def =']]><xsl:value-of select="$content_def"/><![CDATA[';
			function del_confirm(title, id, upd_time){
				if(window.confirm(wb_msg_ils_del_confirm.replace("[title]",title))){
					frmXml.upd_timestamp.value = upd_time;
					itm_lst.ae_upd_course_lesson(frmXml, id, 'delete');
				}
			}
			
			function dosomething() {
				if (!frmXml.add_on.checked) {
					frmXml.offline_cond.value = '';
				}
			}
			
			function clearPass() {
				if (!frmXml.mark_status.checked) {
					frmXml.pass_mark.value = '';
				}
			}
			
			function clearAtt() {
				if (!frmXml.attend_status.checked) {
					frmXml.pass_attend.value = '';
				}
			}
			
			function change_online_module() {
				index = parseInt(frmXml.online_count.value);
				if (!frmXml.online_module.checked) {
					for(i = 1;i <= index;i++) {
						//getObj('o_item_desc_' + i).disabled = 'disabled';
						//getObj('o_item_cond_sel_' + i).disabled = 'disabled';
						getObj('o_item_mod_sel_' + i).disabled = 'disabled';						
					}
				}else if (frmXml.online_module.checked){
					for(i = 1;i <= index;i++) {
						//getObj('o_item_desc_' + i).disabled = false;
						//getObj('o_item_cond_sel_' + i).disabled = false;
						getObj('o_item_mod_sel_' + i).disabled = false;						
					}
				}
			}
			
			function change_offline_module() {
				index = parseInt(frmXml.offline_count.value);
				if (!frmXml.offline_module.checked) {
					for(i = 1;i <= index;i++) {
						getObj('s_item_cond_sel_' + i).disabled = 'disabled';
					}
				}else if (frmXml.offline_module.checked){
					for(i = 1;i <= index;i++) {
						getObj('s_item_cond_sel_' + i).disabled = false;
					}
				}
			}

			function getObj(index) {
				var obj = document.getElementById(index);
				return obj;
			}
			
			function change(val){
				var chk_recal = document.getElementById("chk_recal");
				var chk_recal_date = document.getElementById("chk_recal_date");
				if (chk_recal.checked) {
					if(val == 0 && chk_recal_date.checked){
						chk_recal_date.checked = false;
					}
				}
				if (chk_recal_date.checked) {
					if(val == 1 && chk_recal.checked){
						chk_recal.checked = false;
					}
				}
			}
			
			function recal_check() {
				if (!frmXml.recal_box.checked) {
					frmXml.recal_date[0].disabled = 'disabled';
					frmXml.recal_date[1].disabled = 'disabled';
				}else {
					frmXml.recal_date[0].disabled = false;					
					frmXml.recal_date[1].disabled = false;					
				}
			}
			
			function init(editable) {]]><xsl:if test="$editable = 'true'"><![CDATA[

					onlineCount = parseInt(frmXml.online_count.value);
					offlineCount = parseInt(frmXml.offline_count.value);
					if (!frmXml.online_module.checked) {
						for(i = 1;i <= onlineCount ;i++) {
							//getObj('o_item_desc_' + i).disabled = 'disabled';
							//getObj('o_item_cond_sel_' + i).disabled = 'disabled';
							getObj('o_item_mod_sel_' + i).disabled = 'disabled';						
						}
					}
									
					if (offlineCount > 0) {
						if (!frmXml.offline_module.checked) {
							for(i = 1;i <= offlineCount ;i++) {
								getObj('s_item_cond_sel_' + i).disabled = 'disabled';
							}
						}
					}
					
					if (typeof(frmXml.recal_date) != 'undefined') {
						frmXml.recal_date[0].disabled = 'disabled';
						frmXml.recal_date[1].disabled = 'disabled';
					}

				]]></xsl:if><![CDATA[

			}
			
			function checkOption(obj){
				if(obj.value==''){
					obj.value='';
					return;
				}
				
				var is_public = getObj('mod_public_'+obj.value).value;
				if(is_public == 'OFF' && content_def != 'PARENT'){
					if(!confirm(eval("wb_msg_mod_not_public"))){
						obj.value='';
					}
				}
			}
			
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="init()">
			<form name="frmXml">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_title">結訓條件設置</xsl:with-param>
			<xsl:with-param name="lab_no_title">沒有結訓條件</xsl:with-param>
			<xsl:with-param name="lab_to_complate">學員要完成本<xsl:choose>
							<xsl:when test="$itm_exam_ind='true'">考試</xsl:when>
							<xsl:otherwise>課程</xsl:otherwise>
						</xsl:choose>必須：</xsl:with-param>
			<xsl:with-param name="lab_desc">系統會按結訓條件判定學員的學習狀態。如果你設定計分之模塊為線上內容要求之一，那該模塊則不可設置成為計分項目。</xsl:with-param>
			<xsl:with-param name="lab_pass_mark">得到的分數必須大於或等於：</xsl:with-param>
			<xsl:with-param name="lab_pass_attend">出席率必須大於或等於：</xsl:with-param>
			<xsl:with-param name="lab_online_module">參與以下在線模塊：</xsl:with-param>
			<xsl:with-param name="lab_txt_head_desc">項目描述</xsl:with-param>
			<xsl:with-param name="lab_txt_head_cond">條件</xsl:with-param>
			<xsl:with-param name="lab_txt_item">項目</xsl:with-param>
			<xsl:with-param name="lab_mod_name">模塊標題：</xsl:with-param>
			<xsl:with-param name="lab_offline_module">參與以下計分項目：</xsl:with-param>
			<xsl:with-param name="lab_count_item">計分項目：</xsl:with-param>
			<xsl:with-param name="lab_no_scoring_item">目前並沒有計分項目。於設定計分項目要求之前，必需先設定計分項目。</xsl:with-param>
			<xsl:with-param name="lab_add_criteria">說明（僅供參考）：</xsl:with-param>
			<xsl:with-param name="lab_txt_recal">重新計算當前<xsl:choose>
							<xsl:when test="$itm_exam_ind='true'">考試</xsl:when>
							<xsl:otherwise>課程</xsl:otherwise>
						</xsl:choose>每個學員的所有成績：</xsl:with-param>
			<xsl:with-param name="lab_txt_recal_no_date">
				<b>不改變</b>學員的完成日期，重新計算後學員當前的完成日期會被保留。</xsl:with-param>
			<xsl:with-param name="lab_txt_recal_date">
				<b>改變</b>學員的完成日期，重新計算後學員當前的完成日期會改為當前日期。</xsl:with-param>
			<xsl:with-param name="lab_no_more_2k">（不能超過2000個字符）</xsl:with-param>
			<xsl:with-param name="lab_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_lang">ch</xsl:with-param>
			<xsl:with-param name="lab_attempted">已嘗試</xsl:with-param>
			<xsl:with-param name="lab_passed">合格</xsl:with-param>
			<xsl:with-param name="lab_viewed">已查閱</xsl:with-param>
			<xsl:with-param name="lab_submitted">已提交</xsl:with-param>
			<xsl:with-param name="lab_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_participated">曾參與</xsl:with-param>
			<xsl:with-param name="lab_pre_status">要求狀態</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_title">结训条件设置</xsl:with-param>
			<xsl:with-param name="lab_no_title">没有结训条件</xsl:with-param>
			<xsl:with-param name="lab_to_complate">学员要完成本<xsl:choose>
							<xsl:when test="$itm_exam_ind='true'">考试</xsl:when>
							<xsl:otherwise>课程</xsl:otherwise>
						</xsl:choose>必须：</xsl:with-param>
			<xsl:with-param name="lab_desc">您可以在这里设定学员完成<xsl:choose>
							<xsl:when test="$itm_exam_ind='true'">考试</xsl:when>
							<xsl:otherwise>课程</xsl:otherwise>
						</xsl:choose>所需要的条件。
			</xsl:with-param>
			<xsl:with-param name="lab_pass_mark">得到的分数必须大于或等于：</xsl:with-param>
			<xsl:with-param name="lab_pass_attend">出席率必须大于或等于：</xsl:with-param>
			<xsl:with-param name="lab_online_module">参与以下在线模块：</xsl:with-param>
			<xsl:with-param name="lab_txt_head_desc">项目描述</xsl:with-param>
			<xsl:with-param name="lab_txt_head_cond">条件</xsl:with-param>
			<xsl:with-param name="lab_txt_item">项目</xsl:with-param>
			<xsl:with-param name="lab_mod_name">模块标题：</xsl:with-param>
			<xsl:with-param name="lab_offline_module">参与以下计分项目：</xsl:with-param>
			<xsl:with-param name="lab_count_item">计分项目：</xsl:with-param>
			<xsl:with-param name="lab_no_scoring_item">无计分项目</xsl:with-param>
			<xsl:with-param name="lab_add_criteria">说明（仅供参考）：</xsl:with-param>
			<xsl:with-param name="lab_txt_recal">重新计算当前<xsl:choose>
							<xsl:when test="$itm_exam_ind='true'">考试</xsl:when>
							<xsl:otherwise>课程</xsl:otherwise>
						</xsl:choose>每个学员的所有成绩：</xsl:with-param>
			<xsl:with-param name="lab_txt_recal_no_date">
				<b>不改变</b>学员的完成日期，重新计算后学员当前的完成日期会被保留。</xsl:with-param>
			<xsl:with-param name="lab_txt_recal_date">
				<b>改变</b>学员的完成日期，重新计算后学员当前的完成日期会改为当前日期。</xsl:with-param>
			<xsl:with-param name="lab_no_more_2k">（不能超过2000个字符）</xsl:with-param>
			<xsl:with-param name="lab_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_lang">gb</xsl:with-param>
			<xsl:with-param name="lab_attempted">已尝试</xsl:with-param>
			<xsl:with-param name="lab_passed">合格</xsl:with-param>
			<xsl:with-param name="lab_viewed">已查阅</xsl:with-param>
			<xsl:with-param name="lab_submitted">已提交</xsl:with-param>
			<xsl:with-param name="lab_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_participated">曾参与</xsl:with-param>
			<xsl:with-param name="lab_pre_status">要求状态</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>information</xsl:with-param>
			<xsl:with-param name="lab_desc">System will determine the learning status of a learner according to the completion criteria. If you have defined a scoring module as an online content requirement, you cannot make it as a scoring item.</xsl:with-param>
			<xsl:with-param name="lab_title">Completion criteria settings</xsl:with-param>
			<xsl:with-param name="lab_no_title">No completion criteria</xsl:with-param>
			<xsl:with-param name="lab_to_complate">To complete this <xsl:choose>
							<xsl:when test="$itm_exam_ind='true'">exam</xsl:when>
							<xsl:otherwise>course</xsl:otherwise>
						</xsl:choose>, the learner has to：</xsl:with-param>
			<xsl:with-param name="lab_pass_mark">Attain this score or above：</xsl:with-param>
			<xsl:with-param name="lab_pass_attend">Attain this attendance rate or above:</xsl:with-param>
			<xsl:with-param name="lab_online_module">Participate in all the following online learning modules：</xsl:with-param>
			<xsl:with-param name="lab_txt_head_desc">Item description</xsl:with-param>
			<xsl:with-param name="lab_txt_head_cond">Criteria</xsl:with-param>
			<xsl:with-param name="lab_txt_item">Item</xsl:with-param>
			<xsl:with-param name="lab_mod_name">Module name:</xsl:with-param>
			<xsl:with-param name="lab_offline_module">Participate in all the following scoring item：</xsl:with-param>
			<xsl:with-param name="lab_add_criteria">explain (for reference only)：</xsl:with-param>
			<xsl:with-param name="lab_count_item">Calculate item：</xsl:with-param>
			<xsl:with-param name="lab_no_scoring_item">Currently there is no scoring item. You must set up scoring item before making a scoring item requirement.</xsl:with-param>
			<xsl:with-param name="lab_txt_recal">Recalculate all existing results of this <xsl:choose>
							<xsl:when test="$itm_exam_ind='true'">exam</xsl:when>
							<xsl:otherwise>course</xsl:otherwise>
						</xsl:choose> using the new criteria：</xsl:with-param>
			<xsl:with-param name="lab_txt_recal_no_date">
				<b>WITHOUT</b> changing the completion date for each learner. The current completion date of each learner will remain.</xsl:with-param>
			<xsl:with-param name="lab_txt_recal_date">
				<b>AND</b> change the completion date for each learner to today's date. After recalculation, each learner will be updated with a new completion date.</xsl:with-param>
			<xsl:with-param name="lab_no_more_2k">(Not more than 2000 characters)</xsl:with-param>
			<xsl:with-param name="lab_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_lang">en</xsl:with-param>
			<xsl:with-param name="lab_attempted">Attempted</xsl:with-param>
			<xsl:with-param name="lab_passed">Passed</xsl:with-param>
			<xsl:with-param name="lab_viewed">Viewed</xsl:with-param>
			<xsl:with-param name="lab_submitted">Submitted</xsl:with-param>
			<xsl:with-param name="lab_completed">Completed</xsl:with-param>
			<xsl:with-param name="lab_participated">Participated</xsl:with-param>
			<xsl:with-param name="lab_pre_status">Required status</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_pass_mark"/>
		<xsl:param name="lab_pass_attend"/>
		<xsl:param name="lab_online_module"/>
		<xsl:param name="lab_txt_head_desc"/>
		<xsl:param name="lab_txt_head_cond"/>
		<xsl:param name="lab_txt_item"/>
		<xsl:param name="lab_mod_name"/>
		<xsl:param name="lab_offline_module"/>
		<xsl:param name="lab_add_criteria"/>
		<xsl:param name="lab_count_item"/>
		<xsl:param name="lab_no_scoring_item"/>
		<xsl:param name="lab_txt_recal"/>
		<xsl:param name="lab_txt_recal_no_date"/>
		<xsl:param name="lab_txt_recal_date"/>
		<xsl:param name="lab_no_more_2k"/>
		<xsl:param name="lab_ok"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_content"/>        
		<xsl:param name="lab_to_complate"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_no_title"/>
		<xsl:param name="lab_attempted"/>
		<xsl:param name="lab_passed"/>
		<xsl:param name="lab_viewed"/>
		<xsl:param name="lab_submitted"/>
		<xsl:param name="lab_completed"/>
		<xsl:param name="lab_participated"/>
		<xsl:param name="lab_pre_status"/>
		<xsl:variable name="pass_ind" select="comp_cond_lst/ccr/@pass_ind"/>
		<xsl:variable name="pass_score" select="comp_cond_lst/ccr/@pass_score"/>
		<xsl:variable name="attend_rate" select="comp_cond_lst/ccr/@attend_rate"/>
		<xsl:variable name="online_modules" select="comp_cond_lst/ccr/cmt_online"/>
		<xsl:variable name="online_module_options" select="comp_cond_lst/ccr/online_module"/>
		<xsl:variable name="scoring_items" select="comp_cond_lst/ccr/cmt_scoring"/>
		
	
	<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="$is_new_cos = 'false'">
	    <xsl:call-template name="itm_action_nav">
	    	<xsl:with-param name="view_mode">simple</xsl:with-param>
			<xsl:with-param name="is_add">false</xsl:with-param>
			<xsl:with-param  name="cur_node_id">03</xsl:with-param>
		</xsl:call-template>
	</xsl:if>
	
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_desc"/>
		</xsl:call-template>
		<!--================================================================================-->
		<script language="JavaScript"><![CDATA[
function changeStatus(this_sel, content, radio_status_name, mod) {
	id = 0;
    if(mod != null && id != undefined){
    	id = mod.value;
    }
	index = this_sel.selectedIndex;
	type = this_sel.options[index].id;
	type_selected_status(type, content, radio_status_name, 'true','', id);
}

function type_selected_status(type, content, radio_status_name, editable, pre_status, id) {

	var modStatusObj = eval(content);
	type = type.toUpperCase();
	
	var input_type = '';
	var input_value_0 = '';
	var input_value_1 = '';
	var input_label_0 = '';
	var input_label_1 = '';
	var input_disable_1 = false;
	var input_checked;
	var input_name = radio_status_name;
	if (pre_status != null && pre_status != '') {
		pre_status = pre_status.toUpperCase();
	}
	value = '--';
	if (type != null && type != '') {
		if (type == 'GLO') {
			input_type = 'hidden';
			input_value_0 = 'IFCP';
			input_label_0 = ']]><xsl:value-of select="$lab_viewed"/><![CDATA[';
		}
		
		if (type == 'VOD' || type == 'REF' || type == 'RDG') {
			if (editable == 'true') {
				if(getObj('duration_'+id) && getObj('duration_'+id).value < 1){
					input_disable_1 = true;
				}
				input_type = 'radio';
				input_value_0 = 'IFCP';
				input_label_0 = ']]><xsl:value-of select="$lab_viewed"/><![CDATA[';
				input_value_1 = 'CP';
				input_label_1 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				if (pre_status == "CP") {
					input_checked = 1;
				} else {
					input_checked = 0;
				}
			//===================================================================
			} else {
				if (pre_status != null && pre_status != '' && pre_status=="IFCP") {
					input_type = 'hidden';
					input_value_0 = 'IFCP';
					input_label_0 = ']]><xsl:value-of select="$lab_viewed"/><![CDATA[';
				} else {
					input_type = 'hidden';
					input_value_1 = 'CP';
					input_label_1 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				}
			}
		}
		
		if ( type == 'SVY') {
			
		
			input_type = 'hidden';
			input_value_0 = 'CP';
			input_label_0 = ']]><xsl:value-of select="$lab_submitted"/><![CDATA[';
		}
		if (type == 'FOR' || type == 'FAQ' || type == 'CHT') {
			input_type = 'hidden';
			input_value_0 = 'IFCP';
			input_label_0 = ']]><xsl:value-of select="$lab_participated"/><![CDATA[';
		}
		if (type == 'AICC_AU' || type == 'NETG_COK' || type == 'SCO') {
			if (editable == 'true') {
				//======================================================================
				input_type = 'radio';
				input_value_0 = 'IFCP';
				input_label_0 = ']]><xsl:value-of select="$lab_attempted"/><![CDATA[';
				input_value_1 = 'CP';
				input_label_1 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				if (pre_status != null && pre_status != '' && pre_status == "CP") {
					input_checked = 1;
				} else {
					input_checked = 0;
				}
				//=================================================================
			} else {
				if (pre_status != null && pre_status != '' && pre_status == "IFCP") {
					input_type = 'hidden';
					input_value_0 = 'IFCP';
					input_label_0 = ']]><xsl:value-of select="$lab_attempted"/><![CDATA[';
				} else {
					input_type = 'hidden';
					input_value_0 = 'CP';
					input_label_0 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				}
			}
		}
		if (type == 'TST' || type == 'DXT' ||type == 'ASS') {
			if (editable == 'true') {
				input_type = 'radio';
				input_value_0 = 'IFCP';
				input_label_0 = ']]><xsl:value-of select="$lab_submitted"/><![CDATA[';
				input_value_1 = 'CP';
				input_label_1 = ']]><xsl:value-of select="$lab_passed"/><![CDATA[';
				if (pre_status == "CP") {
					input_checked = 1;
				} else {
					input_checked = 0;
				}
			//===================================================================
			} else {
				if (pre_status != null && pre_status != '' && pre_status=="IFCP") {
					input_type = 'hidden';
					input_value_0 = 'IFCP';
					input_label_0 = ']]><xsl:value-of select="$lab_attempted"/><![CDATA[';
				} else {
					input_type = 'hidden';
					input_value_0 = 'CP';
					input_label_0 = ']]><xsl:value-of select="$lab_passed"/><![CDATA[';
				}
			}
		}
		if (input_type == 'radio') {
			value = ' <input type="' + input_type + '" name="' + input_name + '" value="' + input_value_0 + '" id="' + input_name + '_1"/><label for="' + input_name + '_1">' + input_label_0 + '</label>'
			      + '&nbsp;';
			      if(input_disable_1){
			      	 value = value+ ' <input disabled = "' + input_disable_1 + '" type="' + input_type + '" name="' + input_name + '" value="' + input_value_1 + '" id="' + input_name + '_2"/><label for="' + input_name + '_2">' + input_label_1 + '</label>';
			      }else{
			      	value = value+ ' <input type="' + input_type + '" name="' + input_name + '" value="' + input_value_1 + '" id="' + input_name + '_2"/><label for="' + input_name + '_2">' + input_label_1 + '</label>';
			      }
			    
		} else {
			value = ' <input type="' + input_type + '" name="' + input_name + '" value="' + input_value_0 + '" />' + input_label_0;
		}
	}
	modStatusObj.innerHTML = value;
	if (input_type == 'radio') {
		var radioObj = eval('document.frmXml.' + radio_status_name);
		radioObj[input_checked].checked = true;
	}
}
]]></script>
		<!--================================================================================-->
		<xsl:call-template name="wb_ui_nav_link">       
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="meta/item/@run_ind = 'false'">
					
						<xsl:choose>
							<xsl:when test="$current_role='INSTR_1'">
								<a href="javascript:itm_lst.get_itm_instr_view({$itm_id})" class="NavLink">
									<xsl:value-of select="meta/item/title"/>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
									<xsl:value-of select="meta/item/title"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
						<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_title"/>
						</span>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="meta/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
						<span class="NavLink">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="$lab_title"/>
						</span>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="$editable = 'false'">
				<xsl:choose>
					<xsl:when test="$pass_score != 0 or $attend_rate != 0 or count($online_modules/item[@desc_option!='']) >0 or count($scoring_items/item[@desc_option!=''])>0 or /CourseCriteria/comp_cond_lst/ccr/offline_cond/text()!=''">
						<table>
							<tr>
								<td>
								</td>
								<td align="left" nowrap="nowrap" colspan="2">
									<xsl:value-of select="$lab_to_complate"/>
								</td>
							</tr>
							<xsl:if test="$pass_score != 0">
								<tr>
									<td>
									</td>
									<td colspan="2">
											<xsl:value-of select="$lab_pass_mark"/>
											<xsl:value-of select="$pass_score"/>
										 </td>
								</tr>
							</xsl:if>
							<xsl:if test="$attend_rate != 0 and $itm_type != 'SELFSTUDY'">
								<tr>
									<td>
									</td>
									<td colspan="2">
											<xsl:value-of select="$lab_pass_attend"/>
											<xsl:value-of select="$attend_rate"/>
										%</td>
								</tr>
							</xsl:if>
							<xsl:if test="count($online_modules/item[@desc_option!='']) >0">
								<tr>
									<td>
									</td>
									<td colspan="2" >
										<label style="margin-top: 8px;padding-left: 4px;">
										<xsl:value-of select="$lab_online_module"/>
										</label>
									</td>
								</tr>
								<tr>
									<td>
									</td>
									<td colspan="2">
										<table>
											<tr>
												<!-- 
												<td align="center">
													<span class="SmallText">
														<xsl:value-of select="$lab_txt_head_desc"/>
													</span>
												</td>
												 -->
												<td>
													<xsl:value-of select="$lab_mod_name"/>
												</td>
												<td>
													<xsl:value-of select="$lab_pre_status"/>
												</td>
											</tr>
											<xsl:for-each select="$online_modules/item[@desc_option!='']">
												<tr>
													<!-- 
													<td align="center">
														<span class="SmallText">
															<xsl:value-of select="title"/>
														</span>
														
													</td>
													 -->
													<td width="50%">             
														<xsl:call-template name="gen_module_lst">
															<xsl:with-param name="name">o_item_mod_sel_<xsl:value-of select="position()"/>
															</xsl:with-param>
															<xsl:with-param name="default" select="@res_id"/>
															<xsl:with-param name="options" select="$online_module_options"/>
															<xsl:with-param name="display">true</xsl:with-param>
															<xsl:with-param name="style">width:70%;margin: 10px 0;</xsl:with-param>
														</xsl:call-template>
													</td>
													<td width="50%">
														<span class="Text" id="mod_status_{position()}">
															<script language="JavaScript">
															<![CDATA[ type=']]><xsl:value-of select="res_subtype"/><![CDATA[' ;
															
															]]><![CDATA[ id=']]><xsl:value-of select="@res_id"/><![CDATA[';
															]]><![CDATA[ pre_status=']]><xsl:value-of select="cmr_status"/><![CDATA[';
															]]><![CDATA[ content='mod_status_]]><xsl:value-of select="position()"/><![CDATA[';
															]]><![CDATA[ radio_status='mod_pre_]]><xsl:value-of select="position()"/><![CDATA[_status';]]><![CDATA[ editable=']]><xsl:value-of select="$editable"/><![CDATA['; 
															 type_selected_status(type,content,radio_status,editable,pre_status,id);
															]]>
															</script>
														</span>
														
														<!-- 
														<xsl:call-template name="gen_comp_crt_cond_lst">
															<xsl:with-param name="name">o_item_cond_sel_<xsl:value-of select="position()"/>
															</xsl:with-param>
															<xsl:with-param name="lang" select="$wb_lang"/>
															<xsl:with-param name="default" select="@desc_option"/>
														    <xsl:with-param name="display">true</xsl:with-param>

														</xsl:call-template>
														 -->
													</td>
												</tr>
											</xsl:for-each>
										</table>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="count($scoring_items/item[@desc_option!=''])>0">
								<tr>
									<td colspan="3">
									</td>
								</tr>
								<tr>
									<td>
									</td>
									<td colspan="2">
											<xsl:value-of select="$lab_offline_module"/>
									</td>
								</tr>
								<tr>
									<td>
									</td>
									<td colspan="2" align="left">
										<table>
											<tr>
												<td align="center">
													<xsl:value-of select="$lab_count_item"/>
												</td>
												<td align="center">
													<xsl:value-of select="$lab_txt_head_cond"/>
												</td>
											</tr>
											<xsl:for-each select="$scoring_items/item[@desc_option!='']">
												<tr>
													<td align="center">
														<xsl:value-of select="title"/>
													</td>
													<td align="center">
														<xsl:call-template name="gen_comp_crt_cond_lst2">
															<xsl:with-param name="name">s_item_cond_sel_<xsl:value-of select="position()"/>
															</xsl:with-param>
															<xsl:with-param name="lang" select="$wb_lang"/>
															<xsl:with-param name="default" select="@desc_option"/>															<xsl:with-param name="display">true</xsl:with-param>
														</xsl:call-template>
													</td>
												</tr>
											</xsl:for-each>
										</table>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="/CourseCriteria/comp_cond_lst/ccr/offline_cond/text()!=''">
								<tr>
									<td colspan="3">
									</td>
								</tr>
								<tr>
									<td>
									</td>
									<td colspan="2" width="100%">
										<label for="add_on3">
											<span class="SmallText">
												<xsl:value-of select="$lab_add_criteria"/>
											</span>
										</label>
									</td>
								</tr>
								<tr>       
									<td>
									</td>
									<td colspan="2">
										<xsl:value-of select="comp_cond_lst/ccr/offline_cond"/>
									</td>
								</tr>
							</xsl:if>
							<tr>
								<td colspan="3">
								</td>
							</tr>
						</table>
						<xsl:call-template name="wb_ui_line"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_title"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<table>
					<tr>
						<td align="left" nowrap="nowrap" colspan="3">
							<xsl:value-of select="$lab_to_complate"/>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<input type="checkbox" id="mark_status_on" name="mark_status" value="on" onclick="clearPass()">
								<xsl:if test="$pass_score != 0">
									<xsl:attribute name="checked">true</xsl:attribute>
								</xsl:if>
							</input>
							<input type="hidden" name="lab_percent" value="{$lab_pass_mark}"/>
							<label for="mark_status_on">
								<xsl:value-of select="$lab_pass_mark"/>
							</label>
							<input class="wzb-inputText" type="text" value="{$pass_score}" name="pass_mark" size="3" maxlength="3" onFocus="frmXml.mark_status_on.checked='true'"/>
							<br/>
							<xsl:call-template name="wb_ui_desc">
								<xsl:with-param name="text">
								    <xsl:copy-of select="$label_core_training_management_318"/>
								    </xsl:with-param>
							</xsl:call-template>
							</td>
					</tr>
					<xsl:if test="$itm_type != 'SELFSTUDY'">
						<tr>
							<td colspan="3">
								<input type="checkbox" name="attend_status" value="on" id="att_rate" onclick="clearAtt()">
									<xsl:if test="$attend_rate != 0">
										<xsl:attribute name="checked">true</xsl:attribute>
									</xsl:if>
								</input>
								<input type="hidden" name="lab_attend" value="{$lab_pass_attend}"/>
								<label for="att_rate">
									<xsl:value-of select="$lab_pass_attend"/>
								</label>
								<input class="wzb-inputText" type="text" value="{$attend_rate}" name="pass_attend" size="3" maxlength="3" onFocus="frmXml.att_rate.checked='true'"/> %</td>
						</tr>
					</xsl:if>
					<tr>
						<td colspan="3" >
							<input type="checkbox" name="online_module" value="on" id="online_module" onclick="change_online_module();">
								<xsl:if test="count($online_modules/item[@desc_option!='']) >0">
									<xsl:attribute name="checked">true</xsl:attribute>
								</xsl:if>
							</input>
							<input type="hidden" name="online_count" value="{count($online_modules/item)}"/>
							<label for="online_module">
								<xsl:value-of select="$lab_online_module"/>
							</label>
						</td>
					</tr>
					<tr>
						<td style="width:3px;">
						</td>
						<td colspan="2">
							<table>
								<tr>
									<!-- 
									<td align="center">
										<span class="SmallText">
											<xsl:value-of select="$lab_txt_head_desc"/>
										</span>
									</td>
									 -->
									<td>
										<xsl:value-of select="$lab_mod_name"/>
									</td>
									<td>
										<xsl:value-of select="$lab_pre_status"/>
									</td>
								</tr>
									<xsl:for-each select="/CourseCriteria/comp_cond_lst/ccr/online_module/module">
									<input type="hidden" id="mod_public_{@id}" value="{res_status}"/>
									<input type="hidden" id="duration_{@id}" value="{duration}"/>
								</xsl:for-each>
								<xsl:for-each select="$online_modules/item">
									<tr>
										<!-- 
										<td align="center">
											<input type="text" class="wzb-inputText" size="30" maxlength="100" id="{concat('o_item_desc_',position())}" name="{concat('o_item_desc_',position())}" value="{title}"/>
										</td>
										 -->
										<td width="50%">
											<xsl:call-template name="gen_module_lst">
												<xsl:with-param name="name">o_item_mod_sel_<xsl:value-of select="position()"/>
												</xsl:with-param>
												<xsl:with-param name="default" select="@res_id"/>
												<xsl:with-param name="options" select="$online_module_options"/>
												<xsl:with-param name="p" select="position()"/>
												<xsl:with-param name="func">checkOption(this)</xsl:with-param>
												<xsl:with-param name="style">width:70%;margin: 10px 0;</xsl:with-param>
											</xsl:call-template>
										</td>
										<td width="50%">
											<span class="Text" id="mod_status_{position()}">
												<script language="JavaScript">
												<![CDATA[ type=']]><xsl:value-of select="res_subtype"/><![CDATA[' ;
												]]><![CDATA[ id=']]><xsl:value-of select="@res_id"/><![CDATA[';
												]]><![CDATA[ pre_status=']]><xsl:value-of select="cmr_status"/><![CDATA[';
												]]><![CDATA[ content='mod_status_]]><xsl:value-of select="position()"/><![CDATA[';]]><![CDATA[ radio_status='mod_pre_]]><xsl:value-of select="position()"/><![CDATA[_status';]]><![CDATA[ editable=']]><xsl:value-of select="$editable"/><![CDATA['; 
												 type_selected_status(type,content,radio_status,editable,pre_status,id);
												]]>
												</script>
											</span>
											<!-- 
											<xsl:call-template name="gen_comp_crt_cond_lst">
												<xsl:with-param name="name">o_item_cond_sel_<xsl:value-of select="position()"/>
												</xsl:with-param>
												<xsl:with-param name="lang" select="$wb_lang"/>
												<xsl:with-param name="default" select="@desc_option">
											</xsl:with-param>
											</xsl:call-template>
											 -->
											<!--===================================================================-->
											<input type="hidden" name="{concat('o_cmt_id_', position())}" value="{@cmt_id}"/>
											<input type="hidden" name="{concat('o_cmr_id_', position())}" value="{@cmr_id}"/>
										</td>
									</tr>
								</xsl:for-each>
							
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<input type="checkbox" name="offline_module" value="on" id="offline_module" onclick="change_offline_module();">
								<xsl:choose>
									<xsl:when test="not(count($scoring_items/item)>0)">
										<xsl:attribute name="disabled">true</xsl:attribute>
									</xsl:when>
									<xsl:otherwise>
										<xsl:if test="count($scoring_items/item[@desc_option!=''])>0">
											<xsl:attribute name="checked">true</xsl:attribute>
										</xsl:if>
									</xsl:otherwise>
								</xsl:choose>
							</input>
							<input type="hidden" name="offline_count" value="{count($scoring_items/item)}"/>
							<label for="offline_module" >
								<xsl:if test="not(count($scoring_items/item)>0)">
									<xsl:attribute name="class">SmallTextDisabled</xsl:attribute>
								</xsl:if>
								<xsl:value-of select="$lab_offline_module"/>
							</label>
						</td>
					</tr>
					<xsl:choose>
						<xsl:when test="count($scoring_items/item)>0">
							<tr>
								<td>
								</td>
								
								<td colspan="2" >
									<table style="margin-left:3px;">
										<tr>
											<td align="left">
												<xsl:value-of select="$lab_count_item"/>
											</td>
											<td align="left">
												<xsl:value-of select="$lab_txt_head_cond"/>
											</td>
										</tr>
										<xsl:for-each select="$scoring_items/item">
											<tr >
												<td align="left" style="padding-bottom:5px;">
													<xsl:value-of select="title"/>
												</td>
												<td align="left" style="padding-bottom:5px;">
													<xsl:call-template name="gen_comp_crt_cond_lst2">
														<xsl:with-param name="name">s_item_cond_sel_<xsl:value-of select="position()"/>
														</xsl:with-param>
														<xsl:with-param name="lang" select="$wb_lang"/>
														<xsl:with-param name="default" select="@desc_option"/>
													</xsl:call-template>
												
													<!--========================================================-->
													<input type="hidden" name="{concat('s_cmt_id_', position())}" value="{@cmt_id}"/>
													<input type="hidden" name="{concat('s_cmr_id_', position())}" value="{@cmr_id}"/>
												</td>
											</tr>
										</xsl:for-each>
									</table>
								</td>
							</tr>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td colspan="4" align="center">
									<xsl:value-of select="$lab_no_scoring_item"/>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="$is_new_cos != 'true'">
					    <!--  <tr>  不显示说明
							<td colspan="3" width="100%">
								<input type="checkbox" name="add_on" value="checkbox" id="add_on3" onClick="dosomething();">
									<xsl:if test="/CourseCriteria/comp_cond_lst/ccr/offline_cond/text()!=''">
										<xsl:attribute name="checked">true</xsl:attribute>
									</xsl:if>
								</input>
								<label for="add_on3">
									<xsl:value-of select="$lab_add_criteria"/>
								</label>
							</td>
						</tr>
						<tr>
							<td style="width:10px"></td>
							<td colspan="2">
								<textarea name="offline_cond" rows="3" cols="80" onKeyPress="frmXml.add_on.checked='true'">
									<xsl:value-of select="comp_cond_lst/ccr/offline_cond"/>
								</textarea>
							</td>
							<input type="hidden" name="lab_offline_cond" value="{$lab_add_criteria}"/>
						</tr>
						<tr>
							<td style="width:10px"></td>
							<td colspan="2" class="wzb-ui-module-text">
								<xsl:value-of select="$lab_no_more_2k"/>
							</td>
						</tr> -->
						<!--
				<xsl:if test="/CourseCriteria/meta/item/item_type_meta/@create_run_ind != 'true'">
				-->
						<tr>
							<td colspan="3">
								<input type="checkbox" name="recal_box" id="recal_box" value="YES" onclick="recal_check();"/>
								<label for="recal_box">
									<span class="SmallText" >
										<xsl:value-of select="$lab_txt_recal"/>
									</span>
								</label>
							</td>
						</tr>
						<tr>
							<!--<td align="right" valign="top">
							<input type="checkbox" name="chk_recal" id="chk_recal" onclick="change(0)"/>
					  </td>-->
							<td colspan="3">
								<span style="margin-left:20px;">
									<input type="radio" name="recal_date" id="recal_date" value="NO" checked="checked"/>
									<label for="recal_date">
										<span class="smallText">
											<xsl:copy-of select="$lab_txt_recal_no_date"/>
										</span>
									</label>
								</span>
							</td>
						</tr>
						<tr>
							<!--<td valign="top">
								<input type="checkbox" name="chk_recal_date" id="chk_recal_date" onclick="change(1)"/>
					  </td>-->
							<td colspan="3">
								<span style="margin-left:20px;">
									<input type="radio" name="recal_date" id="recal_date_2" value="YES"/>
									<label for="recal_date_2">
										<span class="smallText" >
											<xsl:copy-of select="$lab_txt_recal_date"/>
										</span>
									</label>
								</span>
							</td>
						</tr>
				</xsl:if>
				</table>
				<xsl:choose>
					<xsl:when test="$is_new_cos != 'true'">
						<div class="wzb-bar">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_ok"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:cond.validateRunform(frmXml,'<xsl:value-of select="$lab_lang"/>');</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="class">btn wzb-btn-blue margin-right10 wzb-btn-big</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_cancel"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
						</div>
					</xsl:when>

					<xsl:when test="$is_new_cos = 'true' and //itm_action_nav/@itm_run_ind = 'true'">
						<div class="wzb-bar">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_ok_btn"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:cond.validateRunform(frmXml,'<xsl:value-of select="$lab_lang"/>');</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<div class="wzb-bar">
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_next_btn"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:cond.validateRunform(frmXml,'<xsl:value-of select="$lab_lang"/>');</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
			
							<xsl:call-template name="wb_gen_button">
								<xsl:with-param name="wb_gen_btn_name">
									<xsl:value-of select="$lab_ok_btn"/>
								</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_item_detail(<xsl:value-of select="$itm_id"/>)</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
							</xsl:call-template>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_footer"/>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="upd_timestamp" value="{comp_cond_lst/ccr/@last_update}"/>
		<input type="hidden" name="itm_id" value="{$itm_id}"/>
		<input type="hidden" name="ccr_id" value="{$ccr_id}"/>
		<input type="hidden" name="online_item_count" value="{count($online_modules/item)}"/>
		<input type="hidden" name="scoring_item_count" value="{count($scoring_items/item)}"/>
		<input type="hidden" name="itm_run_ind" value="{count($scoring_items/item)}"/>
		<input type="hidden" name="is_new_cos" value="{//itm_action_nav/@itm_run_ind}"/>
	</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="nav">
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_session_info"/>
		<xsl:variable name="_count" select="count(preceding-sibling::item)"/>
		<xsl:choose>
			<xsl:when test="@run_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.get_item_run_list({$value})" class="NavLink">
					<xsl:choose>
						<xsl:when test="$itm_exam_ind = 'true'"><xsl:value-of select="$lab_const_exam_manage"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_const_cls_manage"/></xsl:otherwise>
					</xsl:choose>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:when test="@session_ind = 'true'">
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<xsl:variable name="value">
					<xsl:for-each select="preceding-sibling::item">
						<xsl:if test="position()=$_count">
							<xsl:value-of select="@id"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:variable>
				<a href="javascript:itm_lst.session.get_session_list({$value})" class="NavLink">
					<xsl:value-of select="$lab_session_info"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<a href="javascript:itm_lst.get_item_run_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:itm_lst.get_item_detail({@id})" class="NavLink">
					<xsl:value-of select="title"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

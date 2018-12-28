<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>

	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="itm_id" select="/prerequisite/prerequisite_module/item/@id"/>
	<xsl:variable name="current_role" select="/prerequisite/current_role"/>
	<xsl:variable name="editable">
		<xsl:choose>
			<xsl:when test="count(/prerequisite/prerequisite_module/mod_list/module) &gt; 1">
				<xsl:value-of select="/prerequisite/prerequisite_module/editable/text()"/>
			</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/prerequisite">
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
			<script language="JavaScript"><![CDATA[
			itm_lst = new wbItem;
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="">
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
			<xsl:with-param name="lab_title">模塊次序</xsl:with-param>
			<xsl:with-param name="lab_attempted">已嘗試</xsl:with-param>
			<xsl:with-param name="lab_passed">已通過</xsl:with-param>
			<xsl:with-param name="lab_viewed">已查閱</xsl:with-param>
			<xsl:with-param name="lab_submitted">已提交</xsl:with-param>
			<xsl:with-param name="lab_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_participated">已參與</xsl:with-param>
			<xsl:with-param name="lab_module_title">模塊</xsl:with-param>
			<xsl:with-param name="lab_pre">先修模塊</xsl:with-param>
			<xsl:with-param name="lab_pre_status">條件</xsl:with-param>
			<xsl:with-param name="lab_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_lang">ch</xsl:with-param>
			<xsl:with-param name="lab_no_module">沒有線上學習模塊</xsl:with-param>
			<xsl:with-param name="lab_unspecified">-- 未指定 --</xsl:with-param>
			<xsl:with-param name="lab_alt_RDG">教材</xsl:with-param>
			<xsl:with-param name="lab_alt_REF">參考</xsl:with-param>
			<xsl:with-param name="lab_alt_GLO">詞彙表</xsl:with-param>
			<xsl:with-param name="lab_alt_VOD">視頻點播</xsl:with-param>
			<xsl:with-param name="lab_alt_ASS">作業</xsl:with-param>
			<xsl:with-param name="lab_alt_SVY">課程評估問卷</xsl:with-param>
			<xsl:with-param name="lab_alt_FOR">討論區</xsl:with-param>
			<xsl:with-param name="lab_alt_FAQ">解答欄</xsl:with-param>
			<xsl:with-param name="lab_alt_CHT">聊天室</xsl:with-param>
			<xsl:with-param name="lab_alt_AICC_AU">AICC 課件</xsl:with-param>
			<xsl:with-param name="lab_alt_NETG_COK">NETg 課件</xsl:with-param>
			<xsl:with-param name="lab_alt_SCO">SCORM Courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_TST">測驗</xsl:with-param>
			<xsl:with-param name="lab_alt_DXT">測驗</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_pass">合格</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>信息</xsl:with-param>
			<xsl:with-param name="lab_title">先修模块设置</xsl:with-param>
			<xsl:with-param name="lab_attempted">已尝试</xsl:with-param>
			<xsl:with-param name="lab_passed">已通过</xsl:with-param>
			<xsl:with-param name="lab_viewed">已查阅</xsl:with-param>
			<xsl:with-param name="lab_submitted">已提交</xsl:with-param>
			<xsl:with-param name="lab_completed">已完成</xsl:with-param>
			<xsl:with-param name="lab_participated">已参与</xsl:with-param>
			<xsl:with-param name="lab_module_title">模块</xsl:with-param>
			<xsl:with-param name="lab_pre">先修模块</xsl:with-param>
			<xsl:with-param name="lab_pre_status">条件</xsl:with-param>
			<xsl:with-param name="lab_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_content">内容</xsl:with-param>
			<xsl:with-param name="lab_lang">gb</xsl:with-param>
			<xsl:with-param name="lab_no_module">没有网上学习模块</xsl:with-param>
			<xsl:with-param name="lab_unspecified">-- 未指定 --</xsl:with-param>
			<xsl:with-param name="lab_alt_RDG">教材</xsl:with-param>
			<xsl:with-param name="lab_alt_REF">参考</xsl:with-param>
			<xsl:with-param name="lab_alt_GLO">词汇表</xsl:with-param>
			<xsl:with-param name="lab_alt_VOD">视频点播</xsl:with-param>
			<xsl:with-param name="lab_alt_ASS">作业</xsl:with-param>
			<xsl:with-param name="lab_alt_SVY">课程评估问卷</xsl:with-param>
			<xsl:with-param name="lab_alt_FOR">论坛</xsl:with-param>
			<xsl:with-param name="lab_alt_FAQ">答疑栏</xsl:with-param>
			<xsl:with-param name="lab_alt_CHT">聊天室</xsl:with-param>
			<xsl:with-param name="lab_alt_AICC_AU">AICC 课件</xsl:with-param>
			<xsl:with-param name="lab_alt_NETG_COK">NETg 课件</xsl:with-param>
			<xsl:with-param name="lab_alt_SCO">SCORM Courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_TST">测验</xsl:with-param>
			<xsl:with-param name="lab_alt_DXT">测验</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_pass">合格</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_run_info">
				<xsl:value-of select="$lab_const_run"/>information</xsl:with-param>
			<xsl:with-param name="lab_title">Module sequence</xsl:with-param>
			<xsl:with-param name="lab_attempted">Attempted</xsl:with-param>
			<xsl:with-param name="lab_passed">Passed</xsl:with-param>
			<xsl:with-param name="lab_viewed">Viewed</xsl:with-param>
			<xsl:with-param name="lab_submitted">Submitted</xsl:with-param>
			<xsl:with-param name="lab_completed">Completed</xsl:with-param>
			<xsl:with-param name="lab_participated">Participated</xsl:with-param>
			<xsl:with-param name="lab_module_title">Module</xsl:with-param>
			<xsl:with-param name="lab_pre">Prerequisite module</xsl:with-param>
			<xsl:with-param name="lab_pre_status">Required status</xsl:with-param>
			<xsl:with-param name="lab_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_content">Content</xsl:with-param>
			<xsl:with-param name="lab_lang">en</xsl:with-param>
			<xsl:with-param name="lab_no_module">No online modules found</xsl:with-param>
			<xsl:with-param name="lab_unspecified">-- Unspecified --</xsl:with-param>
			<xsl:with-param name="lab_alt_RDG">Learning material</xsl:with-param>
			<xsl:with-param name="lab_alt_REF">Reference links</xsl:with-param>
			<xsl:with-param name="lab_alt_GLO">English glossary</xsl:with-param>
			<xsl:with-param name="lab_alt_VOD">Video on demand</xsl:with-param>
			<xsl:with-param name="lab_alt_ASS">Assignment</xsl:with-param>
			<xsl:with-param name="lab_alt_SVY">Course evaluation</xsl:with-param>
			<xsl:with-param name="lab_alt_FOR">Forum</xsl:with-param>
			<xsl:with-param name="lab_alt_FAQ">Q&amp;A</xsl:with-param>
			<xsl:with-param name="lab_alt_CHT">Chat room</xsl:with-param>
			<xsl:with-param name="lab_alt_AICC_AU">AICC courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_NETG_COK">NETg courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_SCO">SCORM courseware</xsl:with-param>
			<xsl:with-param name="lab_alt_TST">Test</xsl:with-param>
			<xsl:with-param name="lab_alt_DXT">Test</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_pass">Passed</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_run_info"/>
		<xsl:param name="lab_attempted"/>
		<xsl:param name="lab_passed"/>
		<xsl:param name="lab_viewed"/>
		<xsl:param name="lab_submitted"/>
		<xsl:param name="lab_completed"/>
		<xsl:param name="lab_participated"/>
		<xsl:param name="lab_module_title"/>
		<xsl:param name="lab_pre"/>
		<xsl:param name="lab_pre_status"/>
		<xsl:param name="lab_ok"/>
		<xsl:param name="lab_cancel"/>
		<xsl:param name="lab_content"/>
		<xsl:param name="lab_to_complate"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_no_module"/>
		<xsl:param name="lab_unspecified"/>
		<xsl:param name="lab_alt_RDG"/>
		<xsl:param name="lab_alt_REF"/>
		<xsl:param name="lab_alt_GLO"/>
		<xsl:param name="lab_alt_VOD"/>
		<xsl:param name="lab_alt_ASS"/>
		<xsl:param name="lab_alt_SVY"/>
		<xsl:param name="lab_alt_FOR"/>
		<xsl:param name="lab_alt_FAQ"/>
		<xsl:param name="lab_alt_CHT"/>
		<xsl:param name="lab_alt_AICC_AU"/>
		<xsl:param name="lab_alt_NETG_COK"/>
		<xsl:param name="lab_alt_SCO"/>
		<xsl:param name="lab_alt_TST"/>
		<xsl:param name="lab_alt_DXT"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_pass"/>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">104</xsl:with-param>
		</xsl:call-template>
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
		<!--================================================================================-->
		<script language="JavaScript"><![CDATA[
		
	function getObj(index) {
		var obj = document.getElementById(index);
		return obj;
	}
function changeStatus(this_sel, content, radio_status_name, mod) {
	id = 0;
    if(mod != null && id != undefined){
    	id = mod.value;
    }
	index = this_sel.selectedIndex;
	type = this_sel.options[index].id;
	type_selected_status(type, content, radio_status_name, 'true','', id,false, 0);
}

function type_selected_status(type, content, radio_status_name, editable, pre_status, id, has_set_duration, duration) {
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
			input_value_0 = 'I';
			input_label_0 = ']]><xsl:value-of select="$lab_viewed"/><![CDATA[';
		}
		if (type == 'VOD' || type == 'RDG' || type == 'REF') {
			if (editable == 'true') {
			    if(!has_set_duration && getObj('duration_'+id)){
			    	duration = getObj('duration_'+id).value;
			    }
				if(duration < 1){
					input_disable_1 = true;
				}
				input_type = 'radio';
				input_value_1 = 'C';
				input_label_1 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				input_value_0 = 'I';
				input_label_0 = ']]><xsl:value-of select="$lab_viewed"/><![CDATA[';
				if (pre_status == "CP" || pre_status == "P" || pre_status == "C") {
					input_checked = 1;
				} else {
					input_checked = 0;
				}
			//===================================================================
			} else {
				if (pre_status != null && pre_status != '' && pre_status=="I") {
					input_type = 'hidden';
					input_value_0 = 'I';
					input_label_0 = ']]><xsl:value-of select="$lab_viewed"/><![CDATA[';
				} else {
					input_type = 'hidden';
					input_value_0 = 'C';
					input_label_0 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				}
			}
		}
		
		if ( type == 'SVY') {
			
		
			input_type = 'hidden';
			input_value_0 = 'C';
			input_label_0 = ']]><xsl:value-of select="$lab_submitted"/><![CDATA[';
		}
		if (type == 'FOR' || type == 'FAQ' || type == 'CHT') {
			input_type = 'hidden';
			input_value_0 = 'I';
			input_label_0 = ']]><xsl:value-of select="$lab_participated"/><![CDATA[';
		}
		if (type == 'AICC_AU' || type == 'NETG_COK' || type == 'SCO') {
			if (editable == 'true') {
				//======================================================================
				input_type = 'radio';
				input_value_0 = 'I';
				input_label_0 = ']]><xsl:value-of select="$lab_attempted"/><![CDATA[';
				input_value_1 = 'C';
				input_label_1 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				if (pre_status != null && pre_status != '' && pre_status == "C") {
					input_checked = 1;
				} else {
					input_checked = 0;
				}
				//=================================================================
			} else {
				if (pre_status != null && pre_status != '' && pre_status == "I") {
					input_type = 'hidden';
					input_value_0 = 'I';
					input_label_0 = ']]><xsl:value-of select="$lab_attempted"/><![CDATA[';
				} else {
					input_type = 'hidden';
					input_value_0 = 'C';
					input_label_0 = ']]><xsl:value-of select="$lab_completed"/><![CDATA[';
				}
			}
		}
		if (type == 'TST' || type == 'DXT' ||type == 'ASS') {
			if (editable == 'true') {
				input_type = 'radio';
				input_value_0 = 'I';
				input_label_0 = ']]><xsl:value-of select="$lab_submitted"/><![CDATA[';
				input_value_1 = 'P';
				input_label_1 = ']]><xsl:value-of select="$lab_pass"/><![CDATA[';
				if (pre_status == "P") {
					input_checked = 1;
				} else {
					input_checked = 0;
				}
			//===================================================================
			} else {
				if (pre_status != null && pre_status != '' && pre_status=="I") {
					input_type = 'hidden';
					input_value_0 = 'I';
					input_label_0 = ']]><xsl:value-of select="$lab_attempted"/><![CDATA[';
				} else {
					input_type = 'hidden';
					input_value_0 = 'P';
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
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true' ">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<xsl:apply-templates select="meta/item/nav/item" mode="nav">
							<xsl:with-param name="lab_run_info" select="$lab_run_info"/>
							<xsl:with-param name="lab_session_info" select="$lab_run_info"/>
						</xsl:apply-templates>
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_title"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<!--
		<xsl:call-template name="wb_ui_line"/>
		-->
		<xsl:choose>
			<xsl:when test="count(/prerequisite/prerequisite_module/mod_list/module) &gt; 0">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td align="left" valign="bottom">
							<xsl:value-of select="$lab_type"/>
						</td>
						<td>
							<xsl:value-of select="$lab_module_title"/>
						</td>
						<td>
							<xsl:value-of select="$lab_pre"/>
						</td>
						<td align="right">
							<xsl:value-of select="$lab_pre_status"/>
						</td>
					</tr>
					<!--=============================================================================================================-->
					<xsl:for-each select="/prerequisite/prerequisite_module/mod_structure/tableofcontents/descendant::item[itemtype='MOD']">
						<xsl:variable name="mod_id" select="@identifierref"/>
						<xsl:variable name="type" select="restype"/>
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsOdd</xsl:when>
								<xsl:otherwise>RowsEven</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:if test="count(/prerequisite/prerequisite_module/mod_list/module[@id = $mod_id]) &gt; 0">
							<xsl:variable name="pre_id">
								<xsl:value-of select="/prerequisite/prerequisite_module/mod_list/module[@id= $mod_id]/pre_module/@id"/>
							</xsl:variable>
							<xsl:variable name="pre_status">
								<xsl:value-of select="/prerequisite/prerequisite_module/mod_list/module[@id= $mod_id]/pre_module/checked_status"/>
							</xsl:variable>
							<!--
							<xsl:variable name="pre_id_status">
								<xsl:value-of select="$pre_id"/>_<xsl:value-of select="$pre_status"/>
							</xsl:variable>

							-->
							<xsl:variable name="alt_label">
								<xsl:choose>
									<xsl:when test="$type='RDG'">
										<xsl:value-of select="$lab_alt_RDG"/>
									</xsl:when>
									<xsl:when test="$type='REF'">
										<xsl:value-of select="$lab_alt_REF"/>
									</xsl:when>
									<xsl:when test="$type='GLO'">
										<xsl:value-of select="$lab_alt_GLO"/>
									</xsl:when>
									<xsl:when test="$type='VOD'">
										<xsl:value-of select="$lab_alt_VOD"/>
									</xsl:when>
									<xsl:when test="$type='ASS'">
										<xsl:value-of select="$lab_alt_ASS"/>
									</xsl:when>
									<xsl:when test="$type='SVY'">
										<xsl:value-of select="$lab_alt_SVY"/>
									</xsl:when>
									<xsl:when test="$type='FOR'">
										<xsl:value-of select="$lab_alt_FOR"/>
									</xsl:when>
									<xsl:when test="$type='FAQ'">
										<xsl:value-of select="$lab_alt_FAQ"/>
									</xsl:when>
									<xsl:when test="$type='CHT'">
										<xsl:value-of select="$lab_alt_CHT"/>
									</xsl:when>
									<xsl:when test="$type='AICC_AU'">
										<xsl:value-of select="$lab_alt_AICC_AU"/>
									</xsl:when>
									<xsl:when test="$type='NETG_COK'">
										<xsl:value-of select="$lab_alt_NETG_COK"/>
									</xsl:when>
									<xsl:when test="$type='SCO'">
										<xsl:value-of select="$lab_alt_SCO"/>
									</xsl:when>
									<xsl:when test="$type='TST'">
										<xsl:value-of select="$lab_alt_TST"/>
									</xsl:when>
									<xsl:when test="$type='DXT'">
										<xsl:value-of select="$lab_alt_DXT"/>
									</xsl:when>
									<xsl:otherwise/>
								</xsl:choose>
							</xsl:variable>
							<tr>
								<td align="left">
									<input type="hidden" name="mod_id" value="{$mod_id}"/>
									<input type="hidden" id="duration_{$mod_id}" value="{/prerequisite/prerequisite_module/mod_list/module[@id = $mod_id]/duration}"/>
									<img width="16px" heigth="16px" src="{$wb_img_path}sico_{/prerequisite/prerequisite_module/mod_list/module[@id = $mod_id]/type}.gif" border="0" alt="{$alt_label}"/>
									<span class="Text">&#160;</span>
								</td>
								<td align="left">
									<xsl:value-of select="@title"/>
								</td>
								<td>
									<xsl:choose>
										<xsl:when test="$editable='true'">
											<select class="wzb-form-select" name="mod_pre_{$mod_id}_id" onchange="changeStatus(this,'mod_status_{$mod_id}','mod_pre_{$mod_id}_status', this)" width="350px" style="width:350px">
												<option value="" id="">
													<xsl:value-of select="$lab_unspecified"/>
												</option>
												<xsl:for-each select="/prerequisite/prerequisite_module/mod_list/module[@id != $mod_id]">
													<xsl:choose>
														<xsl:when test="string-length(title)>30">
															<option title="{title}" value="{@id}" id="{type}">
															<xsl:if test="@id=$pre_id">
																<xsl:attribute name="selected">selected</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="substring(title,0,30)"/>...
															</option>
														</xsl:when>
														<xsl:otherwise>
															<option  value="{@id}" id="{type}">
															<xsl:if test="@id=$pre_id">
																<xsl:attribute name="selected">selected</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="title"/>
															</option>
														</xsl:otherwise>
													</xsl:choose>
													
												</xsl:for-each>
											</select>
										</xsl:when>
										<xsl:otherwise>
											<xsl:choose>
												<xsl:when test="/prerequisite/prerequisite_module/mod_list/module[@id = $pre_id]">
													<xsl:value-of select="/prerequisite/prerequisite_module/mod_list/module[@id = $pre_id]/title"/>
												</xsl:when>
												<xsl:otherwise>
													--
												</xsl:otherwise>
											</xsl:choose>
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td align="right">
									<span class="Text" id="mod_status_{$mod_id}">
										<script language="JavaScript">
											<!--
									<xsl:choose>
										<xsl:when test="pre_id_status !='_' and  count(/prerequisite/prerequisite_module/mod_list/module[@id != $pre_id])  &gt; 0">                          -->
										<![CDATA[ type=']]><xsl:value-of select="/prerequisite/prerequisite_module/mod_list/module[@id = $pre_id]/type"/><![CDATA[' ;
										]]><![CDATA[ pre_status=']]><xsl:value-of select="$pre_status"/><![CDATA[';
										]]><![CDATA[ mod_id=']]><xsl:value-of select="$mod_id"/><![CDATA[';
										]]><![CDATA[ duration=']]><xsl:value-of select="/prerequisite/prerequisite_module/mod_list/module[@id = $pre_id]/duration"/><![CDATA[';
										]]><![CDATA[ content='mod_status_]]><xsl:value-of select="$mod_id"/><![CDATA[';
										]]><![CDATA[ radio_status='mod_pre_]]><xsl:value-of select="$mod_id"/><![CDATA[_status';
										]]><![CDATA[ editable=']]><xsl:value-of select="$editable"/><![CDATA['; 
										 type_selected_status(type,content,radio_status,editable,pre_status,mod_id, true,duration);
										]]><!--
										</xsl:when>
										<xsl:otherwise></xsl:otherwise>
									</xsl:choose>
									-->
										</script>
									</span>
								</td>
							</tr>
						</xsl:if>
					</xsl:for-each>
				</table>
				<xsl:if test="$editable='true'">
					<div class="wzb-bar">
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_ok"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.upd_mod_pre(frmXml,'<xsl:value-of select="$lab_lang"/>');</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name">
								<xsl:value-of select="$lab_cancel"/>
							</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.get_item_detail(frmXml.itm_id.value);</xsl:with-param>
						</xsl:call-template>
					</div>
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<!--
					<tr>
						<td/>
						<td colspan="4" align="center">
							<span class="Text">
								<xsl:value-of select="$lab_no_module"/>
							</span>
						</td>
					</tr>
					-->
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_module"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<!--==============================================================================================================-->
		<!--
			<tr>
				<td colspan="4">
					<img width="1" src="{$wb_img_path}tp.gif" height="1" border="0"/>
				</td>
			</tr>
			-->
		<xsl:call-template name="wb_ui_footer"/>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="module"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="itm_id" value="{$itm_id}"/>
	</div>
	</xsl:template>
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

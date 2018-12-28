<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/draw_res_option_list.xsl"/>
	<xsl:import href="utils/wb_gen_img_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="share/res_label_share.xsl"/>
	<xsl:variable name="root_obj_tcr_id" select="/objective_list/meta/root_obj_tcr_id/text()"/>
	<xsl:variable name="obj_id" select="/objective_list/objective/@id"/>
	<xsl:variable name="share_mode" select="/objective_list/share_mode/text()"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/objective_list/list/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/objective_list/list/pagination/@cur_page"/>
	<xsl:variable name="total" select="/objective_list/list/pagination/@total_rec"/>
	<xsl:variable name="page_timestamp" select="/objective_list/list/pagination/@timestamp"/>
	<xsl:variable name="page_var" select="/objective_list/list/page_variant" />
	<!-- sorting variable -->
	<xsl:variable name="sort_col" select="/objective_list/list/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/objective_list/list/pagination/@sort_order"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:variable name="obj_type" select="/objective_list/list/header/objective/@type"/>
	<xsl:variable name="_cnt_que">
		<xsl:choose>
			<xsl:when test="/objective_list/list/header/@privilege='AUTHOR'">
				<xsl:value-of select="count(/objective_list/list/body/item[@owner=//cur_usr/@id])"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="count(/objective_list/list/body/item)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="objective_list"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="objective_list">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_course.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_objective.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_resource.js" type="text/javascript"/>

			<script language="JavaScript" src="{$wb_js_path}wb_question.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_assessment.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_scenario.js" type="text/javascript"/>
			<script langugae="javascript" type="text/javascript" src="{$wb_js_path}wb_aicc.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cos_wizard.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}que_send_fb.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}que_send_mt.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}que_send_mc.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}que_send_sc.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			window.onload = function(){
				init();
				wb_utils_set_cookie('mod_url',parent.location.href);
			}
			course_lst = new wbCourse;
			course_id = getUrlParam("course_id");
			course_name = wb_utils_get_cookie("title_prev");
			var res = new wbResource;
			var obj = new wbObjective;

			gType		= getParentUrlParam('res_type')
			gSubType	= getParentUrlParam('res_subtype')
			gPrivilege	= getParentUrlParam('res_privilege')
			gStatus 	= getParentUrlParam('res_status')
			gOrder	= getParentUrlParam('sort_order')
			gCol		= getParentUrlParam('sort_col')
			gLan		= getUrlParam('res_lan')
			gDiff		= getParentUrlParam('res_difficulty')

			que = new wbQuestion;
			res = new wbResource;
			asm = new wbAssessment;
			sc = new wbScControl;
			aicc = new wbAicc
			mc = new wbMC
			fb = new wbFB
			mt = new wbMT
			cos = new wbCosWizard

			que_obj_id = getParentUrlParam('obj_id')
			// cookies for servlet use
			gen_set_cookie('url_success',window.parent.location.href,'','','','')
			gen_set_cookie('url_failure',window.parent.location.href,'','','','')
			wb_utils_set_cookie('url_prev',window.parent.location.href)
			que_obj_id_syb = getParentUrlParam('que_obj_id_syb')
			res_privilege = getParentUrlParam('res_privilege')

			if (gOrder == null || gOrder == '') {
				gOrder = 'DESC';
			}
			//
			function getParentUrlParam(name) {
				strParam = window.parent.location.search
				idx1 = strParam.indexOf(name + "=")
				if (idx1 == -1)	return ""

				idx1 = idx1 + name.length + 1
				idx2 = strParam.indexOf("&", idx1)

				if (idx2 != -1)
					len = idx2 - idx1
				else
					len = strParam.length

				return unescape(strParam.substr(idx1, len))
			}

			function getIndex(gType,gSubType){
				var idx = 0;
				]]>
				<!-- Call draw_res_option_lst.xsl -->
				<xsl:call-template name="draw_js_res_que_array"/>
				<![CDATA[
				for(var i =0;i<TypeList.length;i++){
					if(TypeList[i] == gType){
						idx = i;
					}
					if(TypeList[i] == gSubType){
						idx = i;
					}
				}
				return idx;
			}
			//
			function init() {
				frm = document.forms[0];
				var res_index = getIndex(gType,gSubType);
				
				frm.res_type.selectedIndex = res_index;

				if (gStatus == 'ON') {
					frm.res_status.selectedIndex = 1
				} else if (gStatus == 'OFF') {
					frm.res_status.selectedIndex = 2
				}
				if (gDiff == '1'){
					frm.res_difficulty.selectedIndex = 1
				}else if(gDiff =='2'){
					frm.res_difficulty.selectedIndex = 2
				}else if(gDiff == '3'){
					frm.res_difficulty.selectedIndex = 3
				}
			}

		]]></SCRIPT>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<base target="_parent"/>
		</head>
		<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0" >
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_categories">資源文件夾</xsl:with-param>
			<xsl:with-param name="lab_res_manager">管理資源</xsl:with-param>
			<xsl:with-param name="lab_course_list">課程目錄</xsl:with-param>
			<xsl:with-param name="lab_edit_module">修改單元內容</xsl:with-param>
			<xsl:with-param name="lab_owner">建立者</xsl:with-param>
			<xsl:with-param name="lab_online">已發佈</xsl:with-param>
			<xsl:with-param name="lab_offline">未發佈</xsl:with-param>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_myself">本人</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_order">次序</xsl:with-param>
			<xsl:with-param name="lab_mod_time">修改時間</xsl:with-param>
			<xsl:with-param name="lab_res_id">資源編號</xsl:with-param>
			<xsl:with-param name="lab_lang">語言</xsl:with-param>
			<xsl:with-param name="lab_type">種類</xsl:with-param>
			<xsl:with-param name="lab_diff">難度</xsl:with-param>
			<xsl:with-param name="lab_english">英文</xsl:with-param>
			<xsl:with-param name="lab_ch">繁體中文</xsl:with-param>
			<xsl:with-param name="lab_gb">簡體中文</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_refresh">更新</xsl:with-param>
			<xsl:with-param name="lab_descending">(倒序)</xsl:with-param>
			<xsl:with-param name="lab_ascending">(順序)</xsl:with-param>
			<xsl:with-param name="lab_add_inst">新增資源</xsl:with-param>
			<xsl:with-param name="lab_remove_inst">刪除所選資源</xsl:with-param>
			<xsl:with-param name="lab_paste_inst">貼上已複製的資源</xsl:with-param>
			<xsl:with-param name="lab_copy_inst"> 複製已選取的資源</xsl:with-param>
			<xsl:with-param name="lab_online_inst">將已選的資源變為在線</xsl:with-param>
			<xsl:with-param name="lab_offline_inst">將已選的資源變為離線</xsl:with-param>
			<xsl:with-param name="lab_download_inst">將已選的資源下載到你的電腦</xsl:with-param>
			<xsl:with-param name="lab_select_all1">全選</xsl:with-param>
			<xsl:with-param name="lab_no_res">還沒有任何資源</xsl:with-param>
			<xsl:with-param name="lab_res_code">編號</xsl:with-param>
			<xsl:with-param name="lab_res_title">標題</xsl:with-param>
			<xsl:with-param name="lab_res_type">類型</xsl:with-param>
			<xsl:with-param name="lab_res_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_res_date">修改日期</xsl:with-param>
			<xsl:with-param name="lab_button_add">添加</xsl:with-param>
			<xsl:with-param name="lab_button_delete">刪除</xsl:with-param>
			<xsl:with-param name="lab_button_copy">拷貝</xsl:with-param>
			<xsl:with-param name="lab_button_paste">粘貼</xsl:with-param>
			<xsl:with-param name="lab_button_publish">發佈</xsl:with-param>
			<xsl:with-param name="lab_button_cancel">取消發佈</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_categories">资源文件夹</xsl:with-param>
			<xsl:with-param name="lab_res_manager">管理资源</xsl:with-param>
			<xsl:with-param name="lab_course_list">课程清单</xsl:with-param>
			<xsl:with-param name="lab_edit_module">修改模块内容</xsl:with-param>
			<xsl:with-param name="lab_owner">建立者</xsl:with-param>
			<xsl:with-param name="lab_online">已发布</xsl:with-param>
			<xsl:with-param name="lab_offline">未发布</xsl:with-param>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_myself">我的</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_order">排序</xsl:with-param>
			<xsl:with-param name="lab_mod_time">更新时间</xsl:with-param>
			<xsl:with-param name="lab_res_id">资源编号</xsl:with-param>
			<xsl:with-param name="lab_lang">语言</xsl:with-param>
			<xsl:with-param name="lab_type">种类</xsl:with-param>
			<xsl:with-param name="lab_diff">难度</xsl:with-param>
			<xsl:with-param name="lab_english">英文</xsl:with-param>
			<xsl:with-param name="lab_ch">繁体中文</xsl:with-param>
			<xsl:with-param name="lab_gb">简体中文</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_refresh">刷新</xsl:with-param>
			<xsl:with-param name="lab_descending">(逆序)</xsl:with-param>
			<xsl:with-param name="lab_ascending">(顺序)</xsl:with-param>
			<xsl:with-param name="lab_add_inst">添加资源</xsl:with-param>
			<xsl:with-param name="lab_remove_inst">删除所选资源</xsl:with-param>
			<xsl:with-param name="lab_paste_inst">粘贴已复制的资源</xsl:with-param>
			<xsl:with-param name="lab_copy_inst">复制已选的资源</xsl:with-param>
			<xsl:with-param name="lab_online_inst">将已选的资源变为在线</xsl:with-param>
			<xsl:with-param name="lab_offline_inst">将已选的资源变为离线</xsl:with-param>
			<xsl:with-param name="lab_download_inst">将已选的资源下载到你的电脑</xsl:with-param>
			<xsl:with-param name="lab_select_all1">全选</xsl:with-param>
			<xsl:with-param name="lab_no_res">还没有任何资源</xsl:with-param>
			<xsl:with-param name="lab_res_code">编号</xsl:with-param>
			<xsl:with-param name="lab_res_title">标题</xsl:with-param>
			<xsl:with-param name="lab_res_type">类型</xsl:with-param>
			<xsl:with-param name="lab_res_status">状态</xsl:with-param>
			<xsl:with-param name="lab_res_date">修改日期</xsl:with-param>
			<xsl:with-param name="lab_button_add">添加</xsl:with-param>
			<xsl:with-param name="lab_button_delete">删除</xsl:with-param>
			<xsl:with-param name="lab_button_copy">复制</xsl:with-param>
			<xsl:with-param name="lab_button_paste">粘贴</xsl:with-param>
			<xsl:with-param name="lab_button_publish">发布</xsl:with-param>
			<xsl:with-param name="lab_button_cancel">取消发布</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_categories">Resource folders</xsl:with-param>
			<xsl:with-param name="lab_res_manager">Manage resources</xsl:with-param>
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_edit_module">Edit module</xsl:with-param>
			<xsl:with-param name="lab_owner">Creator</xsl:with-param>
			<xsl:with-param name="lab_online">Published</xsl:with-param>
			<xsl:with-param name="lab_offline">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_select_all">--All--</xsl:with-param>
			<xsl:with-param name="lab_myself">Myself</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_order">Order</xsl:with-param>
			<xsl:with-param name="lab_mod_time">Last modified</xsl:with-param>
			<xsl:with-param name="lab_res_id">Resource ID</xsl:with-param>
			<xsl:with-param name="lab_lang">Language</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_english">English</xsl:with-param>
			<xsl:with-param name="lab_ch">Traditional Chinese</xsl:with-param>
			<xsl:with-param name="lab_gb">Simplified Chinese</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_refresh">Refresh</xsl:with-param>
			<xsl:with-param name="lab_descending"> (descending)</xsl:with-param>
			<xsl:with-param name="lab_ascending"> (ascending)</xsl:with-param>
			<xsl:with-param name="lab_add_inst">Add a new resource</xsl:with-param>
			<xsl:with-param name="lab_remove_inst">Remove selected resources</xsl:with-param>
			<xsl:with-param name="lab_paste_inst">Paste copied resources</xsl:with-param>
			<xsl:with-param name="lab_copy_inst">Copy selected resources</xsl:with-param>
			<xsl:with-param name="lab_online_inst">Turn selected resources online</xsl:with-param>
			<xsl:with-param name="lab_offline_inst">Turn selected resources offline</xsl:with-param>
			<xsl:with-param name="lab_download_inst">Download selected resource files</xsl:with-param>
			<xsl:with-param name="lab_select_all1">Select all</xsl:with-param>
			<xsl:with-param name="lab_no_res">No resources found.</xsl:with-param>
			<xsl:with-param name="lab_res_code">ID</xsl:with-param>
			<xsl:with-param name="lab_res_title">Title</xsl:with-param>
			<xsl:with-param name="lab_res_type">Type</xsl:with-param>
			<xsl:with-param name="lab_res_status">Status</xsl:with-param>
			<xsl:with-param name="lab_res_date">Last modified</xsl:with-param>
			<xsl:with-param name="lab_button_add">Add</xsl:with-param>
			<xsl:with-param name="lab_button_delete">Delete</xsl:with-param>
			<xsl:with-param name="lab_button_copy">Copy</xsl:with-param>
			<xsl:with-param name="lab_button_paste">Paste</xsl:with-param>
			<xsl:with-param name="lab_button_publish">Publish</xsl:with-param>
			<xsl:with-param name="lab_button_cancel">Unpublish</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_all_categories"/>
		<xsl:param name="lab_res_manager"/>
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_edit_module"/>
		<xsl:param name="lab_owner"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_myself"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_order"/>
		<xsl:param name="lab_mod_time"/>
		<xsl:param name="lab_res_id"/>
		<xsl:param name="lab_lang"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_english"/>
		<xsl:param name="lab_ch"/>
		<xsl:param name="lab_gb"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_g_txt_btn_refresh"/>
		<xsl:param name="lab_descending"/>
		<xsl:param name="lab_ascending"/>
		<xsl:param name="lab_add_inst"/>
		<xsl:param name="lab_remove_inst"/>
		<xsl:param name="lab_paste_inst"/>
		<xsl:param name="lab_copy_inst"/>
		<xsl:param name="lab_online_inst"/>
		<xsl:param name="lab_offline_inst"/>
		<xsl:param name="lab_download_inst"/>
		<xsl:param name="lab_select_all1"/>
		<xsl:param name="lab_no_res"/>
		<xsl:param name="lab_res_code"/>
		<xsl:param name="lab_res_title"/>
		<xsl:param name="lab_res_type"/>
		<xsl:param name="lab_res_status"/>
		<xsl:param name="lab_res_date"/>
		<xsl:param name="lab_button_add"/>
		<xsl:param name="lab_button_delete"/>
		<xsl:param name="lab_button_copy"/>
		<xsl:param name="lab_button_paste"/>
		<xsl:param name="lab_button_publish"/>
		<xsl:param name="lab_button_cancel"/>
		<xsl:variable name="choice" select="/objective_list/folders/text()" />
		<xsl:variable name="show_all" select="/objective_list/meta/show_all"/>
		<form id="bottom_img">
		<input type="hidden" name="res_subtype"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_res_manager"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="$share_mode != 'true'">
			<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<span class="NavLink">
						<!--  
						<a href="javascript:wb_utils_adm_syb_lst('','{$choice}','{$show_all}','{$root_obj_tcr_id}')" class="NavLink">
							<xsl:value-of select="$lab_all_categories"/>
						</a>-->
						<xsl:for-each select="objective/path/node">
							<a href="javascript:obj.manage_obj_lst('','{@id}','','{$choice}','{$show_all}')" class="NavLink">
								<xsl:value-of select="."/>
							</a>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
						</xsl:for-each>
						<a href="javascript:obj.manage_obj_lst('','{objective/@id}','','{$choice}','{$show_all}')" class="NavLink">
							<xsl:value-of select="objective/desc"/>
						</a>
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						
							<xsl:value-of select="$lab_res_manager"/>
						
					</span>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<table>
			<tr >
				<td width="18%">
					<!-- Type -->
					<xsl:value-of select="$lab_type"/>：
					<select name="res_type" onchange="obj.refresh_obj_lst(document.forms[0],'true')" class="wzb-select">
						<xsl:call-template name="draw_res_option_list"/>
					</select>
				</td>
				<td width="15%">
					<!-- <a href="javascript:obj.refresh_obj_lst(document.forms[0])" target="_self">
						<img src="{$wb_img_path}sico_reload.gif" width="16" height="16" border="0" align="absmiddle"/>
					</a> -->
						<xsl:value-of select="$lab_status"/>：
						<select name="res_status" onchange="obj.refresh_obj_lst(document.forms[0],'true')" class="wzb-select">
							<option value="">
								<xsl:value-of select="$lab_select_all"/>
							</option>
							<option value="ON">
								<xsl:value-of select="$lab_online"/>
							</option>
							<option value="OFF">
								<xsl:value-of select="$lab_offline"/>
							</option>
						</select>
					</td>
					<td width="15%">
						<!-- <a href="javascript:obj.refresh_obj_lst(document.forms[0])" target="_self">
							<img src="{$wb_img_path}sico_reload.gif" width="16" height="16" border="0" align="absmiddle"/>
						</a> -->
							<xsl:value-of select="$lab_diff"/>：
							<select name="res_difficulty" onchange="obj.refresh_obj_lst(document.forms[0],'true')" class="wzb-select">
								<option value="">
									<xsl:value-of select="$lab_select_all"/>
								</option>
								<option value="1">
									<xsl:value-of select="$lab_easy"/>
								</option>
								<option value="2">
									<xsl:value-of select="$lab_normal"/>
								</option>
								<option value="3">
									<xsl:value-of select="$lab_hard"/>
								</option>
							</select>
						<!-- <a href="javascript:obj.refresh_obj_lst(document.forms[0])" target="_self">
							<img src="{$wb_img_path}sico_reload.gif" width="16" height="16" border="0" align="absmiddle"/>
						</a> -->
					<!-- Language -->
					<input type="hidden" name="res_lan" value=""/>
				</td>
				<td valign="bottom" align="right" width="55%">
					<input type="button" class="btn wzb-btn-orange  margin-right4" data-toggle="modal" data-target="#myModal" value="{$lab_button_add}"/>
					<input type="button" class="btn wzb-btn-orange  margin-right4" name="res_remove_off" value="{$lab_button_delete}" onclick="res.del_res(document.frmQue, '{$wb_lang}', '{$obj_id}')"/>
					<input type="button" class="btn wzb-btn-orange  margin-right4" name="res_copy_off" value="{$lab_button_copy}" onclick="res.copy_lst(document.frmQue, '{$wb_lang}')"/>
					<input type="button" class="btn wzb-btn-orange  margin-right4" name="res_paste_off" value="{$lab_button_paste}" onclick="res.pasteLst('{$obj_id}', '{$wb_lang}', document.frmQue, res_privilege)"/>
					<input type="button" class="btn wzb-btn-orange margin-right4" name="res_online_off" value="{$lab_button_publish}" onclick="res.change_status(document.frmQue,'ON', '{$wb_lang}')"/>
					<input type="button" class="btn wzb-btn-orange " name="res_offline_off" value="{$lab_button_cancel}" onclick="res.change_status(document.frmQue,'OFF', '{$wb_lang}')"/>
				</td>
			</tr>
		</table>
		</form>
		<table width="100%" border="0">
			<tr>
				<td>
					<xsl:apply-templates select="list/body">
						<xsl:with-param name="lab_add_inst" select="$lab_add_inst"/>
						<xsl:with-param name="lab_remove_inst" select="$lab_remove_inst"/>
						<xsl:with-param name="lab_paste_inst" select="$lab_paste_inst"/>
						<xsl:with-param name="lab_copy_inst" select="$lab_copy_inst"/>
						<xsl:with-param name="lab_online_inst" select="$lab_online_inst"/>
						<xsl:with-param name="lab_offline_inst" select="$lab_offline_inst"/>
						<xsl:with-param name="lab_download_inst" select="$lab_download_inst"/>
						<xsl:with-param name="lab_select_all" select="$lab_select_all1"/>
						<xsl:with-param name="lab_no_res" select="$lab_no_res"/>
						<xsl:with-param name="lab_online" select="$lab_online"/>
						<xsl:with-param name="lab_offline" select="$lab_offline"/>
						<xsl:with-param name="lab_res_code"><xsl:value-of select="$lab_res_code"/></xsl:with-param>
						<xsl:with-param name="lab_res_title"><xsl:value-of select="$lab_res_title"/></xsl:with-param>
						<xsl:with-param name="lab_res_type"><xsl:value-of select="$lab_res_type"/></xsl:with-param>
						<xsl:with-param name="lab_res_status"><xsl:value-of select="$lab_res_status"/></xsl:with-param>
						<xsl:with-param name="lab_res_date"><xsl:value-of select="$lab_res_date"/></xsl:with-param>
						<xsl:with-param name="lab_button_add">
							<xsl:value-of select="$lab_button_add"/>
						</xsl:with-param>
						<xsl:with-param name="lab_button_delete">
								<xsl:value-of select="$lab_button_delete"/>
						</xsl:with-param>
						<xsl:with-param name="lab_button_copy"><xsl:value-of select="$lab_button_copy"/></xsl:with-param>
						<xsl:with-param name="lab_button_paste"><xsl:value-of select="$lab_button_paste"/></xsl:with-param>
						<xsl:with-param name="lab_button_publish"><xsl:value-of select="$lab_button_publish"/></xsl:with-param>
						<xsl:with-param name="lab_button_cancel"><xsl:value-of select="$lab_button_cancel"/></xsl:with-param>
					</xsl:apply-templates>
				</td>
			</tr>
		</table>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">
							<span aria-hidden="true">X</span>
						</button>
						<h4 class="modal-title textleft" id="myModalLabel">
							<xsl:value-of select="$lab_add_inst"/>
						</h4>
					</div>
					<div class="modal-body">
						<table width="{$wb_gen_table_width}" border="0" cellspacing="0" cellpadding="3" class="Bg">
							<tr>
								<td colspan="5" height="10">
								</td>
							</tr>
							<tr>
								<td>
								</td>
								<td>
									<xsl:value-of select="$lab_gen"/>
								</td>
								<td>
									<xsl:value-of select="$lab_que"/>
								</td>
								<td>
									<xsl:value-of select="$lab_asm"/>
								</td>
								<td>
								</td>
							</tr>
							<tr>
								<td width="10%">
									<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
								<td valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="3">
										<tr>
											<td align="right">
												<a href="javascript:aicc.ins_prep('SSC',que_obj_id)">
													<img src="{$wb_img_path}icol_ssc.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:aicc.ins_prep('SSC',que_obj_id)" class="Text">
													<xsl:value-of select="$lab_ssc"/>
												</a>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a href="javascript:cos.ins_res_scorm_prep('RES_SCO',que_obj_id)" style="margin-right:3px;">
													<img src="{$wb_img_path}icol_sco.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:cos.ins_res_scorm_prep('RES_SCO',que_obj_id)" class="Text">
													<xsl:value-of select="$lab_sco"/>
												</a>
											</td>
										</tr>
									</table>
								</td>
								<td valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="3">
										<tr>
											<td align="right">
												<a href="javascript:que.add_que_prep(que_obj_id,'MC','','true')">
													<img src="{$wb_img_path}icol_mc.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:que.add_que_prep(que_obj_id,'MC','','true')" class="Text">
													<xsl:value-of select="$lab_mc"/>
												</a>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a href="javascript:que.add_que_prep(que_obj_id,'FB','','true')">
													<img src="{$wb_img_path}icol_fb.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:que.add_que_prep(que_obj_id,'FB','','true')" class="Text">
													<xsl:value-of select="$lab_fb"/>
												</a>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a href="javascript:que.add_que_prep(que_obj_id,'MT','','true')">
													<img src="{$wb_img_path}icol_mt.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:que.add_que_prep(que_obj_id,'MT','','true')" class="Text">
													<xsl:value-of select="$lab_mt"/>
												</a>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a href="javascript:que.add_que_prep(que_obj_id,'TF','','true')">
													<img src="{$wb_img_path}icol_tf.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:que.add_que_prep(que_obj_id,'TF','','true')" class="Text">
													<xsl:value-of select="$lab_tf"/>
												</a>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a href="javascript:que.add_que_prep(que_obj_id,'ES','','true')">
													<img src="{$wb_img_path}icol_es.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:que.add_que_prep(que_obj_id,'ES','','true')" class="Text">
													<xsl:value-of select="$lab_es"/>
												</a>
											</td>
										</tr>
										<!-- 屏蔽静/动态情景题
										   <tr>
											<td align="right">
												<a href="javascript:que.add_que_prep(que_obj_id,'FSC','','true')">
													<img src="{$wb_img_path}icol_fsc.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:que.add_que_prep(que_obj_id,'FSC','','true')" class="Text">
													<xsl:value-of select="$lab_fixed_sc"/>
												</a>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a href="javascript:que.add_que_prep(que_obj_id,'DSC','','true')">
													<img src="{$wb_img_path}icol_dsc.gif" border="0" vspace="5" hspace="5"/>
												</a>
											</td>
											<td width="100%">
												<a href="javascript:que.add_que_prep(que_obj_id,'DSC','','true')" class="Text">
													<xsl:value-of select="$lab_dna_sc"/>
												</a>
											</td>
										</tr> -->
									</table>
								</td>
								<td valign="top">
									<table width="100%" border="0" cellspacing="0" cellpadding="3">
										<tr>
											<td align="right">
												<a href="javascript:asm.ins_prep('FAS', que_obj_id)" >
													<img src="{$wb_img_path}icol_fas.gif" border="0"  vspace="5"  hspace="5" />
												</a>
											</td>
											<td width="100%">
												<a href="javascript:asm.ins_prep('FAS', que_obj_id)" class="Text">
													<xsl:value-of select="$lab_fas"/>
												</a>
											</td>
										</tr>
										<tr>
											<td align="right">
												<a href="javascript:asm.ins_prep('DAS', que_obj_id)" >
													<img src="{$wb_img_path}icol_das.gif" border="0"  vspace="5"  hspace="5" />
												</a>
											</td>
											<td width="100%">
												<a href="javascript:asm.ins_prep('DAS', que_obj_id)" class="Text">
													<xsl:value-of select="$lab_das"/>
												</a>
											</td>
										</tr>
									</table>
								</td>
								<td width="10%">
									<IMG border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
		</div>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="list/body">
		<xsl:param name="lab_add_inst"/>
		<xsl:param name="lab_remove_inst"/>
		<xsl:param name="lab_paste_inst"/>
		<xsl:param name="lab_copy_inst"/>
		<xsl:param name="lab_online_inst"/>
		<xsl:param name="lab_offline_inst"/>
		<xsl:param name="lab_download_inst"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_no_res"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_res_code"/>
		<xsl:param name="lab_res_title"/>
		<xsl:param name="lab_res_type"/>
		<xsl:param name="lab_res_status"/>
		<xsl:param name="lab_res_date"/>
		<xsl:param name="lab_button_add"/>
		<xsl:param name="lab_button_delete"/>
		<xsl:param name="lab_button_copy"/>
		<xsl:param name="lab_button_paste"/>
		<xsl:param name="lab_button_publish"/>
		<xsl:param name="lab_button_cancel"/>
		<form name="frmQue" method="post">
				<input type="hidden" name="cmd" value=""/>
				<input type="hidden" name="res_status" value=""/>
				<input type="hidden" name="to_obj_id" value=""/>
				<input type="hidden" name="obj_id" value=""/>
				<input type="hidden" name="res_id_lst" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<table>
					<xsl:if test="count(item)!=0">
						<tr>
							<td width="165" valign="top">
								<table class="wzb-ui-table">
									<tr class="wzb-ui-table-head">
										<td width="40%">
											<xsl:call-template name="select_all_checkbox">
												<xsl:with-param name="display_icon">false</xsl:with-param>
												<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
												<xsl:with-param name="frm_name">frmQue</xsl:with-param>
											</xsl:call-template>
										
											<xsl:call-template name="orderTpl">
												<xsl:with-param name="orderName">res_title</xsl:with-param>
												<xsl:with-param name="labTitle">
													<xsl:value-of select="$lab_res_title"/>
												</xsl:with-param>
											</xsl:call-template>
											
										</td>
										<td width="15%">
											<xsl:call-template name="orderTpl">
												<xsl:with-param name="orderName">res_id</xsl:with-param>
												<xsl:with-param name="labTitle">
													<xsl:value-of select="$lab_res_code"/>
												</xsl:with-param>
											</xsl:call-template>
										</td>
										<td width="15%">
											<xsl:call-template name="orderTpl">
												<xsl:with-param name="orderName">res_subtype</xsl:with-param>
												<xsl:with-param name="labTitle">
													<xsl:value-of select="$lab_res_type"/>
												</xsl:with-param>
											</xsl:call-template>
										</td>
										<td width="15%">
											<xsl:call-template name="orderTpl">
												<xsl:with-param name="orderName">res_status</xsl:with-param>
												<xsl:with-param name="labTitle">
													<xsl:value-of select="$lab_res_status"/>
												</xsl:with-param>
											</xsl:call-template>
										</td>
										<td width="15%" align="right">
											<xsl:call-template name="orderTpl">
												<xsl:with-param name="orderName">res_upd_date</xsl:with-param>
												<xsl:with-param name="labTitle">
													<xsl:value-of select="$lab_res_date"/>
												</xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
									<xsl:choose>
										<xsl:when test="/list/header/@privilege='AUTHOR'">
											<xsl:apply-templates select="item[@owner=//cur_usr/@id]">
												<xsl:with-param name="lab_online">
													<xsl:value-of select="$lab_online"/>
												</xsl:with-param>
												<xsl:with-param name="lab_offline">
													<xsl:value-of select="$lab_offline"/>
												</xsl:with-param>
											</xsl:apply-templates>
										</xsl:when>
										<xsl:otherwise>
											<xsl:apply-templates select="item">
												<xsl:with-param name="lab_online">
													<xsl:value-of select="$lab_online"/>
												</xsl:with-param>
												<xsl:with-param name="lab_offline">
													<xsl:value-of select="$lab_offline"/>
												</xsl:with-param>
											</xsl:apply-templates>
										</xsl:otherwise>
									</xsl:choose>
								</table>
							</td>
						</tr>
					</xsl:if>
				</table>
				<xsl:choose>
					<xsl:when test="count(item)=0">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_res"/>
						</xsl:call-template>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="wb_ui_pagination">
							<xsl:with-param name="cur_page" select="$cur_page"/>
							<xsl:with-param name="page_size" select="$page_size"/>
							<xsl:with-param name="total" select="$total"/>
							<xsl:with-param name="timestamp" select="$page_timestamp"/>
							<xsl:with-param name="link_txt_class">SmallText</xsl:with-param>
							<xsl:with-param name="txt_class">SmallText</xsl:with-param>
							<xsl:with-param name="select_class">SmallText</xsl:with-param>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</form>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item">
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:if test="text()!=''">
			<tr>
				<td>
						
						<input type="checkbox" name="que_{@id}" value="{@id}"/>
						<input type="hidden" name="que_{@id}_type" value="{@type}"/>
						
						<xsl:choose>
						<xsl:when test="@type='QUE'">
							<xsl:choose>
								<xsl:when test="@subtype='FSC' or @subtype='DSC'">
									<a href="javascript:sc.get({@id},'{@subtype}')" class="font">
										<xsl:value-of select="."/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:que.get({@id})" class="font">
										<xsl:value-of select="."/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@type='GEN'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="."/>
							</a>
						</xsl:when>
						<xsl:when test="@type='AICC'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="."/>
							</a>
						</xsl:when>
						<xsl:when test="@type='ASM' or @type='MOD'">
							<a href="javascript:asm.get({@id},'{@subtype}')" class="font">
								<xsl:value-of select="." />
							</a>
						</xsl:when>
						<xsl:when test="@type='SCORM'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="."/>
							</a>
						</xsl:when>
						<xsl:when test="@type='NETGCOK'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="."/>
							</a>
						</xsl:when>
					</xsl:choose>
				</td>
				<td>
									<xsl:choose>
						<xsl:when test="@type='QUE'">
							<xsl:choose>
								<xsl:when test="@subtype='FSC' or @subtype='DSC'">
									<a href="javascript:sc.get({@id},'{@subtype}')" >
										<xsl:value-of select="@id"/>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:que.get({@id})" class="font">
										<xsl:value-of select="@id"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="@type='GEN'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="@id"/>
							</a>
						</xsl:when>
						<xsl:when test="@type='AICC'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="@id"/>
							</a>
						</xsl:when>
						<xsl:when test="@type='ASM' or @type='MOD'">
							<a href="javascript:asm.get({@id},'{@subtype}')" class="font">
								<xsl:value-of select="@id" />
							</a>
						</xsl:when>
						<xsl:when test="@type='SCORM'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="@id"/>
							</a>
						</xsl:when>
						<xsl:when test="@type='NETGCOK'">
							<a href="javascript:res.get({@id})" class="font">
								<xsl:value-of select="@id"/>
							</a>
						</xsl:when>
					</xsl:choose>
				</td>
				<td>
					<xsl:call-template name="getResText">
						<xsl:with-param name="resType"><xsl:value-of select="@subtype"/></xsl:with-param>
					</xsl:call-template>
				</td>
				<td>
					<xsl:choose>
						<xsl:when test="@status = 'ON'">
							<xsl:value-of select="$lab_online"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_offline"/>
						</xsl:otherwise>
					</xsl:choose>

				</td>
				<td align="right">
					<xsl:value-of select="@date"/>
				</td>
			</tr>

		</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<xsl:template match="header"/>
	<!-- =============================================================== -->
	<xsl:template name="orderTpl">
		<xsl:param name="orderName"/>
		<xsl:param name="labTitle"/>
		<xsl:variable name="_order">
			<xsl:choose>
				<xsl:when test="$sort_col = $orderName ">
					<xsl:value-of select="$order_by"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$cur_order"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$sort_col = $orderName">
				<a href="javascript:wb_utils_nav_get_urlparam('sort_col', '{$orderName}','sort_order','{$_order}','cur_page','1')" class="gray">
					<xsl:value-of select="$labTitle"/>
					<xsl:call-template name="wb_sortorder_cursor">
						<xsl:with-param name="img_path">
							<xsl:value-of select="$wb_img_path"/>
						</xsl:with-param>
						<xsl:with-param name="sort_order">
							<xsl:value-of select="$cur_order"/>
						</xsl:with-param>
					</xsl:call-template>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a href="javascript:wb_utils_nav_get_urlparam('sort_col', '{$orderName}','sort_order','ASC','cur_page','1')" class="gray">
					<xsl:value-of select="$labTitle"/>
				</a>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

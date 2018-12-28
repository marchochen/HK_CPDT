<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/draw_res_option_list.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="language" select="//cur_usr/@language"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_sys_list">資源結構圖</xsl:with-param>
			<xsl:with-param name="lab_res_manager">教材管理</xsl:with-param>
			<xsl:with-param name="lab_adv_search">進階搜尋</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_res_id">資源題目編號</xsl:with-param>
			<xsl:with-param name="lab_res_id_exact">編號</xsl:with-param>
			<xsl:with-param name="lab_res_id_range">範圍</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_desc">簡介</xsl:with-param>
			<xsl:with-param name="lab_cre_time">建立時間</xsl:with-param>
			<xsl:with-param name="lab_upd_time">更新時間</xsl:with-param>
			<xsl:with-param name="lab_date_input_mask">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_mins">分鐘</xsl:with-param>
			<xsl:with-param name="lab_diff">難度</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困難</xsl:with-param>
			<xsl:with-param name="lab_owner">建立者 (昵稱)</xsl:with-param>
			<xsl:with-param name="lab_personal">個人</xsl:with-param>
			<xsl:with-param name="lab_public">共享</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_online">已發佈</xsl:with-param>
			<xsl:with-param name="lab_offline">未發佈</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_sort_by">篩選次序</xsl:with-param>
			<xsl:with-param name="lab_asc_order">順序</xsl:with-param>
			<xsl:with-param name="lab_desc_order">倒序</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_search">搜尋</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_sys_list">资源结构图</xsl:with-param>
			<xsl:with-param name="lab_res_manager">资源管理</xsl:with-param>
			<xsl:with-param name="lab_adv_search">高级搜索</xsl:with-param>
			<xsl:with-param name="lab_all">全部</xsl:with-param>
			<xsl:with-param name="lab_res_id">资源题目编号</xsl:with-param>
			<xsl:with-param name="lab_res_id_exact">具体编号</xsl:with-param>
			<xsl:with-param name="lab_res_id_range">编号范围</xsl:with-param>
			<xsl:with-param name="lab_from">由</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_desc">简介</xsl:with-param>
			<xsl:with-param name="lab_cre_time">制定时间</xsl:with-param>
			<xsl:with-param name="lab_upd_time">更新时间</xsl:with-param>
			<xsl:with-param name="lab_date_input_mask">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_mins">分钟</xsl:with-param>
			<xsl:with-param name="lab_diff">难度</xsl:with-param>
			<xsl:with-param name="lab_easy">容易</xsl:with-param>
			<xsl:with-param name="lab_normal">一般</xsl:with-param>
			<xsl:with-param name="lab_hard">困难</xsl:with-param>
			<xsl:with-param name="lab_owner">建立者(昵称)</xsl:with-param>
			<xsl:with-param name="lab_personal">个人</xsl:with-param>
			<xsl:with-param name="lab_public">共享</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_online">已发布</xsl:with-param>
			<xsl:with-param name="lab_offline">未发布</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_sort_by">排序</xsl:with-param>
			<xsl:with-param name="lab_asc_order">顺序</xsl:with-param>
			<xsl:with-param name="lab_desc_order">逆序</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_search">搜索</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_sys_list">Syllabus list</xsl:with-param>
			<xsl:with-param name="lab_res_manager">Learning resource manager</xsl:with-param>
			<xsl:with-param name="lab_adv_search">Advanced search for learning resources</xsl:with-param>
			<xsl:with-param name="lab_all">All</xsl:with-param>
			<xsl:with-param name="lab_res_id">Resource ID</xsl:with-param>
			<xsl:with-param name="lab_res_id_exact">Exact number</xsl:with-param>
			<xsl:with-param name="lab_res_id_range">Range</xsl:with-param>
			<xsl:with-param name="lab_from">From</xsl:with-param>
			<xsl:with-param name="lab_to">To</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_cre_time">Created time</xsl:with-param>
			<xsl:with-param name="lab_upd_time">Last modified time</xsl:with-param>
			<xsl:with-param name="lab_date_input_mask">YYYY-MM-DD</xsl:with-param>
			<xsl:with-param name="lab_mins">Minute(s)</xsl:with-param>
			<xsl:with-param name="lab_diff">Difficulty</xsl:with-param>
			<xsl:with-param name="lab_easy">Easy</xsl:with-param>
			<xsl:with-param name="lab_normal">Normal</xsl:with-param>
			<xsl:with-param name="lab_hard">Hard</xsl:with-param>
			<xsl:with-param name="lab_owner">Creator (name)</xsl:with-param>
			<xsl:with-param name="lab_personal">Personal</xsl:with-param>
			<xsl:with-param name="lab_public">Public</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_online">Published</xsl:with-param>
			<xsl:with-param name="lab_offline">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_sort_by">Sort by</xsl:with-param>
			<xsl:with-param name="lab_asc_order">Ascending order</xsl:with-param>
			<xsl:with-param name="lab_desc_order">Descending order</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_search">Search</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_sys_list"/>
		<xsl:param name="lab_res_manager"/>
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_res_id"/>
		<xsl:param name="lab_res_id_exact"/>
		<xsl:param name="lab_res_id_range"/>
		<xsl:param name="lab_from"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_cre_time"/>
		<xsl:param name="lab_upd_time"/>
		<xsl:param name="lab_date_input_mask"/>
		<xsl:param name="lab_mins"/>
		<xsl:param name="lab_diff"/>
		<xsl:param name="lab_easy"/>
		<xsl:param name="lab_normal"/>
		<xsl:param name="lab_hard"/>
		<xsl:param name="lab_owner"/>
		<xsl:param name="lab_personal"/>
		<xsl:param name="lab_public"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_online"/>
		<xsl:param name="lab_offline"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_sort_by"/>
		<xsl:param name="lab_asc_order"/>
		<xsl:param name="lab_desc_order"/>
		<xsl:param name="lab_g_form_btn_search"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_search.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			seh = new wbSearch

		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="adv_search" method="post">
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_RES_MAIN</xsl:with-param>
					<xsl:with-param name="page_title" select="$lab_adv_search"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_adv_search"/>
				</xsl:call-template>
				<table>
					<tr>
						<td class="wzb-form-label" style="vertical-align:top;" rowspan="2">
							<xsl:value-of select="$lab_res_id"/>：
						</td>
						<td class="wzb-form-control">
							<input  type="radio" id="rdo_search_id_1" name="rdo_search_id" onClick="seh.chg_id_criteria(document.adv_search)"/>
							<label for="rdo_search_id_1">
								<span class="font gray">
									<xsl:value-of select="$lab_res_id_exact"/>：</span>
							</label>
							<input style="margin-left:14px" class="wzb-inputText" maxlength="9" type="text" name="search_id" size="9" onFocus="this.form.rdo_search_id[0].checked=true;seh.chg_id_criteria(document.adv_search)"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-control">
							<input type="radio" id="rdo_search_id_2" name="rdo_search_id" onClick="seh.chg_id_criteria(document.adv_search)"/>
							<label for="rdo_search_id_2">
								<span class="font gray">
									<xsl:value-of select="$lab_res_id_range"/>：<xsl:value-of select="$lab_from"/>
								</span>
							</label>
							<xsl:text>&#160;</xsl:text>
							<input class="wzb-inputText" type="text" maxlength="9" name="search_id_after" size="9" onFocus="this.form.rdo_search_id[1].checked=true;seh.chg_id_criteria(document.adv_search)"/>
							<xsl:text>&#160;</xsl:text>
							<span class="font gray">
								<xsl:value-of select="$lab_to"/>
							</span>
							<xsl:text>&#160;</xsl:text>
							<input class="wzb-inputText" type="text" maxlength="9" name="search_id_before" size="9" onFocus="this.form.rdo_search_id[1].checked=true;seh.chg_id_criteria(document.adv_search)"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_title"/>：
						</td>
						<td class="wzb-form-control">
							<input class="wzb-inputText" type="text" name="search_title" size="18"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_desc"/>：
						</td>
						<td class="wzb-form-control">
							<input class="wzb-inputText" type="text" name="search_desc" size="18"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<span class="font">
								<xsl:value-of select="$lab_cre_time"/>：</span>
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="$lab_from"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">c_a</xsl:with-param>
								<xsl:with-param name="frm">adv_search</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_to"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">c_b</xsl:with-param>
								<xsl:with-param name="frm">adv_search</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<span class="font">
								<xsl:value-of select="$lab_upd_time"/>：</span>
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="$lab_from"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">u_a</xsl:with-param>
								<xsl:with-param name="frm">adv_search</xsl:with-param>
							</xsl:call-template>
							<xsl:text>&#160;</xsl:text>
							<xsl:value-of select="$lab_to"/>
							<xsl:text>&#160;</xsl:text>
							<xsl:call-template name="display_form_input_time">
								<xsl:with-param name="fld_name">u_b</xsl:with-param>
								<xsl:with-param name="frm">adv_search</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>

					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_diff"/>：
						</td>
						<td class="wzb-form-control">
							<input type="checkbox" name="chk_diff_easy" id="chk_diff_easy_id" checked="checked"/>
							<label class="margin-right5" for="chk_diff_easy_id" style="font-weight: normal;">
								<xsl:value-of select="$lab_easy"/>
							</label>
							<input type="checkbox" name="chk_diff_normal" id="chk_diff_normal_id" checked="checked"/>
							<label class="margin-right5" for="chk_diff_normal_id" style="font-weight: normal;">
								<xsl:value-of select="$lab_normal"/>
							</label>
							<input type="checkbox" name="chk_diff_hard" id="chk_diff_hard_id" checked="checked"/>
							<label class="margin-right5" for="chk_diff_hard_id" style="font-weight: normal;">
								<xsl:value-of select="$lab_hard"/>
							</label>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_owner"/>：
						</td>
						<td class="wzb-form-control">
							<input class="wzb-inputText" type="text" name="search_owner" size="18"/>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_status"/>：
						</td>
						<td class="wzb-form-control">
							<select name="search_status" size="1" class="wzb-form-select">
								<option selected="selected" value="">
									<xsl:value-of select="$lab_all"/>
								</option>
								<option value="ON">
									<xsl:value-of select="$lab_online"/>
								</option>
								<option value="OFF">
									<xsl:value-of select="$lab_offline"/>
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_type"/>：
						</td>
						<td class="wzb-form-control">
							<select name="search_type_selection" onChange="seh.changeSec(document.adv_search)" class="wzb-form-select">
								<xsl:call-template name="draw_res_option_list"/>
							</select>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_sort_by"/>：
						</td>
						<td class="wzb-form-control">
							<select name="search_key" size="1" class="wzb-form-select">
								<option selected="selected" value="res_id">
									<xsl:value-of select="$lab_res_id"/>
								</option>
								<option value="res_title">
									<xsl:value-of select="$lab_title"/>
								</option>
								<option value="res_create_date">
									<xsl:value-of select="$lab_cre_time"/>
								</option>
								<option value="res_upd_date">
									<xsl:value-of select="$lab_upd_time"/>
								</option>
								<option value="res_difficulty">
									<xsl:value-of select="$lab_diff"/>
								</option>
								<option value="res_usr_id_owner">
									<xsl:value-of select="$lab_owner"/>
								</option>
								<option value="res_status">
									<xsl:value-of select="$lab_status"/>
								</option>
								<option value="res_subtype">
									<xsl:value-of select="$lab_type"/>
								</option>
							</select>
							<select name="search_order" size="1" class="wzb-form-select">
								<option selected="selected" value="asc">
									<xsl:value-of select="$lab_asc_order"/>
								</option>
								<option value="desc">
									<xsl:value-of select="$lab_desc_order"/>
								</option>
							</select>
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_search"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:seh.adv_exec(document.adv_search,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
					</xsl:call-template>
				</div>
				<input type="hidden" name="cmd" value="search"/>
				<input type="hidden" name="charset" value="{$language}"/>
				<input type="hidden" name="search_type"/>
				<input type="hidden" name="search_sub_type"/>
				<input type="hidden" name="search_create_time_before"/>
				<input type="hidden" name="search_create_time_after"/>
				<input type="hidden" name="search_update_time_before"/>
				<input type="hidden" name="search_update_time_after"/>
				<input type="hidden" name="search_diff_lst"/>
				<input type="hidden" name="stylesheet"/>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>

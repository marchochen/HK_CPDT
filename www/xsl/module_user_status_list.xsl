<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_form_select_action.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<!-- customize utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="share/itm_nav_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>	
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no"/>
	<!-- =============================================================== -->
	<xsl:variable name="mod_id" select="/content/module_submission/module_info/header/@mod_id"/>
	<xsl:variable name="cos_id" select="/content/module_submission/module_info/header/@course_id"/>
	<xsl:variable name="mov_status" select="/content/module_submission/submission_list/@cur_status"/>
	<xsl:variable name="cur_page" select="/content/module_submission/pagination/@cur_page"/>
	<xsl:variable name="total_rec" select="/content/module_submission/pagination/@total_rec"/>
	<xsl:variable name="total_page" select="/content/module_submission/pagination/@total_page"/>
	<xsl:variable name="sort_col" select="/content/module_submission/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/content/module_submission/pagination/@sort_order"/>
	<xsl:variable name="page_size" select="/content/module_submission/pagination/@page_size"/>
	<xsl:variable name="ts" select="/content/module_submission/pagination/@timestamp"/>
	<xsl:variable name="status_completed">
		<xsl:for-each select="/content/module_submission/status_count_list/status">
			<xsl:choose>
				<xsl:when test="@id = 'C'">
					<xsl:value-of select="@count" />
				</xsl:when>
			</xsl:choose>
		</xsl:for-each>
	</xsl:variable>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc' or $cur_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cur_status" select="/content/module_submission/submission_list/@cur_status"/>
	<xsl:variable name="label_core_training_management_250" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_250')"/>
	<xsl:variable name="label_core_training_management_393" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_393')"/>
	<xsl:variable name="label_core_training_management_30" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_30')"/>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<xsl:call-template name="new_css"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_tm_{$wb_cur_lang}.js"/>
			<script language="Javascript" type="text/javascript"><![CDATA[
				mod = new wbModule;
			
				function doFeedParam(){
					param = new Array();
					tmpObj1 = new Array();
					tmpObj2 = new Array();
					tmpObj3 = new Array();
					tmpObj4 = new Array();
					tmpObj5 = new Array();
					tmpObj6 = new Array();
					tmpObj7 = new Array();
					
					tmpObj1[0] = 'cmd';
					tmpObj1[1] = 'chg_user_mov_status';
					param[param.length] = tmpObj1;

					tmpObj2[0] = 'usr_ent_id';					
					ent_id_lst = wbModuleEvaluationGetSelectedEntId();
					tmpObj2[1] = ent_id_lst.split("~");
					param[param.length] = tmpObj2;
					tmpObj3[0] = 'mov_status';
					tmpObj3[1] = document.frmAction.to_status.value;
					param[param.length] = tmpObj3;
					
					tmpObj4[0] = 'mov_mod_id';
					tmpObj4[1] = document.frmAction.mov_mod_id.value;
					param[param.length] = tmpObj4;

					tmpObj5[0] = 'mov_last_upd_timestamp';
					time_lst = wbModuleEvaluationGetSelectedTimestamp();
					tmpObj5[1] = time_lst.split("~");
					param[param.length] = tmpObj5;

					tmpObj6[0] = 'tkh_id';
					tkh_id_lst = wbModuleEvaluationGetSelectedTkhId();
					tmpObj6[1] = tkh_id_lst.split("~");
					param[param.length] = tmpObj6;

					tmpObj7[0] = 'winName';
					tmpObj7[1] = 'module_submission';
					param[param.length] = tmpObj7;
					return param;
				}
				
				function wbModuleEvaluationGetSelectedTkhId(){
					tkh_lst = '';
					n = document.frmAction.elements.length
					flag = true;
					for (i = 0; i < n; i++){
						ele = document.frmAction.elements[i];
						if (ele.type == "checkbox" && ele.name == 'tkh_id'){
							if (ele.checked == true){
								if( tkh_lst == '' )
									tkh_lst = ele.value;
								else
									tkh_lst += '~' + ele.value;
							}
						}
					}
					return tkh_lst;
				}
				
				function wbModuleEvaluationGetSelectedEntId(){
					ent_lst = '';
					n = document.frmAction.elements.length
					flag = true;
					for (i = 0; i < n; i++){
						ele = document.frmAction.elements[i];
						if (ele.type == "checkbox" && ele.name == 'tkh_id'){
							if (ele.checked == true){
								if( ent_lst == '' )
									ent_lst = eval("document.frmAction.usr_ent_id_" + ele.value + ".value");
								else
									ent_lst += '~' + eval("document.frmAction.usr_ent_id_" + ele.value + ".value");
							}
						}
					}
					return ent_lst;
				}
				
				function wbModuleEvaluationGetSelectedTimestamp(){
					timestamp_lst = '';
					n = document.frmAction.elements.length
					flag = true;
					selected_cnt = 0;
					for (i = 0; i < n; i++){
						ele = document.frmAction.elements[i];
						if (ele.type == "checkbox" && ele.name == 'tkh_id'){
							if (ele.checked == true){
								if(selected_cnt < 1 )
									timestamp_lst = eval("document.frmAction.timestamp_" + ele.value + ".value");
								else
									timestamp_lst += '~' + eval("document.frmAction.timestamp_" + ele.value + ".value");
									
								selected_cnt++;
							}
						}
					}
					return timestamp_lst;
				}
				
				function self_refresh() {
					mod.get_subn_list_by_status(document.frmAction.mov_mod_id.value, document.frmAction.mov_status.value);
					return;				
				}
			]]></script>
			<script>
				$(function(){
					$(".table tr:first-child td").css("border-top","1px solid #ddd")
					
				})
			</script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmAction">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="mov_mod_id" value="{$mod_id}"/>
				<input type="hidden" name="mov_status" value="{$mov_status}"/>
				<input type="hidden" name="page_size" value="10"/>
				<input type="hidden" name="page" value="1"/>
				<input type="hidden" name="sort_order" value="asc"/>
				<input type="hidden" name="sort_col" value="usr_display_bil"/>
				<input type="hidden" name="timestamp" value="{$ts}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_type">提交詳情</xsl:with-param>
			<xsl:with-param name="lab_select_mov_status">請選擇狀態</xsl:with-param>
			<xsl:with-param name="lab_status_completed">已嘗試</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">未嘗試</xsl:with-param>
			<xsl:with-param name="lab_submission_list">提交列表</xsl:with-param>
			<xsl:with-param name="lab_change_status_to">更改狀態至</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_no_record">尚未提交</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">匯出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">關閉</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_type">提交详情</xsl:with-param>
			<xsl:with-param name="lab_select_mov_status">请选择状态</xsl:with-param>
			<xsl:with-param name="lab_status_completed">已尝试</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">未尝试</xsl:with-param>
			<xsl:with-param name="lab_submission_list">提交列表</xsl:with-param>
			<xsl:with-param name="lab_change_status_to">更改状态至</xsl:with-param>
			<xsl:with-param name="lab_submission_date">提交日期</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_no_record">还没有提交结果</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">回收站</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">导出</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">关闭</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_select_all">All</xsl:with-param>
			<xsl:with-param name="lab_type">Submission details</xsl:with-param>
			<xsl:with-param name="lab_select_mov_status">Please select status</xsl:with-param>
			<xsl:with-param name="lab_status_completed">Completed</xsl:with-param>
			<xsl:with-param name="lab_status_not_attempted">Not attempted</xsl:with-param>
			<xsl:with-param name="lab_submission_list">Submission list</xsl:with-param>
			<xsl:with-param name="lab_change_status_to">Change status to</xsl:with-param>
			<xsl:with-param name="lab_submission_date">Submission date</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_no_record">No submission</xsl:with-param>
			<xsl:with-param name="lab_lost_and_found">Recycle bin</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_export">Export</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_close">Close</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="content">
	<xsl:param name="lab_select_all"/>
	<xsl:param name="lab_type"/>
	<xsl:param name="lab_select_mov_status"/>
	<xsl:param name="lab_status_completed"/>
	<xsl:param name="lab_status_not_attempted"/>
	<xsl:param name="lab_submission_list"/>
	<xsl:param name="lab_change_status_to"/>
	<xsl:param name="lab_submission_date"/>
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_no_record"/>
	<xsl:param name="lab_lost_and_found"/>
	<xsl:param name="lab_g_txt_btn_export"/>
	<xsl:param name="lab_g_form_btn_close"/>
		
		<!-- nav -->
		<xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">120</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:apply-templates select="/content/meta/item/nav/item" mode="nav">
					<xsl:with-param name="lab_run_info"><xsl:value-of select="$label_core_training_management_250" /></xsl:with-param>
				</xsl:apply-templates>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<a href="javascript:itm_lst.itm_evaluation_report({$cos_id },'TST')"><xsl:value-of select="$label_core_training_management_393"/></a>
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_type"/> - <xsl:value-of select="module_submission/module_info/header/title"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<!-- nav end -->
	
		<xsl:apply-templates select="module_submission">
			<xsl:with-param name="lab_type" select="$lab_type"/>
			<xsl:with-param name="lab_select_mov_status" select="$lab_select_mov_status"/>
			<xsl:with-param name="lab_select_all" select="$lab_select_all"/>
			<xsl:with-param name="lab_submission_list" select="$lab_submission_list"/>
			<xsl:with-param name="lab_change_status_to" select="$lab_change_status_to"/>
			<xsl:with-param name="lab_g_txt_btn_export" select="$lab_g_txt_btn_export"/>
			<xsl:with-param name="lab_status_not_attempted" select="$lab_status_not_attempted"/>
			<xsl:with-param name="lab_status_completed" select="$lab_status_completed"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="module_submission/submission_list">
			<xsl:with-param name="lab_submission_date" select="$lab_submission_date"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_no_record" select="$lab_no_record"/>
			<xsl:with-param name="lab_status_not_attempted" select="$lab_status_not_attempted"/>
			<xsl:with-param name="lab_status_completed" select="$lab_status_completed"/>
			<xsl:with-param name="lab_lost_and_found" select="$lab_lost_and_found"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module_submission/submission_list">
		<xsl:param name="lab_submission_date"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_no_record"/>
		<xsl:param name="lab_lost_and_found"/>
		<xsl:param name="lab_status_not_attempted"/>
		<xsl:param name="lab_status_completed"/>
		<xsl:choose>
			<xsl:when test="count(submission) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_record"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="table wzb-ui-table">
					<tr>
						<td>
							<xsl:call-template name="select_all_checkbox">
								<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="count(submission)"/></xsl:with-param>
								<xsl:with-param name="display_icon">false</xsl:with-param>
								<xsl:with-param name="chkbox_lst_nm">tkh_id</xsl:with-param>
								<xsl:with-param name="frm_name">frmAction</xsl:with-param>
							</xsl:call-template>
							<xsl:choose>
								<xsl:when test="$sort_col = 'usr_display_bil' ">
									<a href="javascript:mod.sort_subn_list('usr_display_bil','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:mod.sort_subn_list('usr_display_bil','asc')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_group"/>
							</span>
						</td>
						<td align="center">
							<span class="TitleText">
								<xsl:value-of select="$lab_submission_date"/>
							</span>
						</td>
						<td align="right">
							<span class="TitleText">
								<xsl:value-of select="$lab_status"/>
							</span>
						</td>
					</tr>
					<xsl:for-each select="submission">
						<xsl:variable name="row_class">
							<xsl:choose>
								<xsl:when test="position() mod 2">RowsEven</xsl:when>
								<xsl:otherwise>RowsOdd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<tr class="{$row_class}">
							<td>
								<input type="checkbox" value="{@tkh_id}" name="tkh_id" id="{tkh_id}"/>
								<span class="Text">
									<xsl:value-of select="user/name/@display_name"/>
								</span>
							</td>
							<td align="center">
								<span class="Text">
									<xsl:choose>
										<xsl:when test="user/user_attribute_list/attribute_list/entity[@type = 'USG']">
											<xsl:choose>
												<xsl:when test="user/full_path != ''">
													<xsl:value-of select="user/full_path"/>
												</xsl:when>
												<xsl:otherwise>--</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_lost_and_found"/>
										</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
							<td align="center">
								<span class="Text">
									<xsl:choose>
										<xsl:when test="@submission_timestamp != ''">
											<xsl:call-template name="display_time">
												<xsl:with-param name="my_timestamp"><xsl:value-of select="@submission_timestamp"/></xsl:with-param>
												<xsl:with-param name="dis_time">T</xsl:with-param>
											</xsl:call-template>
										</xsl:when>
										<xsl:otherwise>--</xsl:otherwise>
									</xsl:choose>
								</span>
							</td>
							<td align="right">
								<span class="Text">
									<xsl:choose>
										<xsl:when test="@status = 'C'">
											<xsl:value-of select="$lab_status_completed"/>
										</xsl:when>
										<xsl:when test="@status = 'N' or @status = '' ">
											<xsl:value-of select="$lab_status_not_attempted"/>
										</xsl:when>
									</xsl:choose>
								</span>
							</td>
							<input type="hidden" name="timestamp_{@tkh_id}" value="{@last_update_timestamp}"/>
							<input type="hidden" name="usr_ent_id_{@tkh_id}" value="{user/@ent_id}"/>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total_rec"/>
					<xsl:with-param name="timestamp" select="$ts"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="module_submission">
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_select_mov_status"/>
		<xsl:param name="lab_status_not_attempted"/>
		<xsl:param name="lab_status_completed"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_submission_list"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_g_txt_btn_export"/>
		<xsl:param name="lab_change_status_to"/>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td align="left">
					<span class="wzb-form-label" style="text-align: left;width:90px;float:left">
						<xsl:text>&#160;</xsl:text>
						<xsl:value-of select="$lab_select_mov_status"/>：
					</span>
					<xsl:apply-templates select="status_count_list">
						<xsl:with-param name="lab_status_not_attempted"><xsl:value-of select="$lab_status_not_attempted"/></xsl:with-param>
						<xsl:with-param name="lab_select_all"><xsl:value-of select="$lab_select_all"/></xsl:with-param>
						<xsl:with-param name="lab_status_completed"><xsl:value-of select="$lab_status_completed"/></xsl:with-param>
					</xsl:apply-templates>
				</td>
				<td align="right" height="35">
					<xsl:if test="$total_rec &gt; 0 and $status_completed > 0">
						<xsl:call-template name="wb_gen_button_orange">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_export"/>
							<xsl:with-param name="wb_gen_btn_href">Javascript:mod.export_report_by_tkhids(document.frmAction.mov_mod_id.value, document.frmAction.mov_status.value, document.frmAction)</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="status_count_list">
		<xsl:param name="lab_status_not_attempted"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_status_completed"/>
		<span class="TitleText" >
			<select style="margin-top:6px;" name="sel_subn" class="Select" onchange="mod.get_subn_list_by_status(document.frmAction.mov_mod_id.value, this.options[this.selectedIndex].value,{$cos_id})">
				<option value="">
					<xsl:if test="$cur_status = ''">
						<xsl:attribute name="selected">selected</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$lab_select_all"/>
					<xsl:text>&#160;(</xsl:text>
					<xsl:value-of select="@total"/>
					<xsl:text>)</xsl:text>
				</option>
				<xsl:for-each select="status">
					<option value="{@id}">
						<xsl:if test="$cur_status = @id">
							<xsl:attribute name="selected">selected</xsl:attribute>
						</xsl:if>
						<xsl:choose>
							<xsl:when test="@id = 'C'">
								<xsl:value-of select="$lab_status_completed"/>
							</xsl:when>
							<xsl:when test="@id = 'N'">
								<xsl:value-of select="$lab_status_not_attempted"/>
							</xsl:when>
						</xsl:choose>
						<xsl:text>&#160;(</xsl:text>
						<xsl:value-of select="@count"/>
						<xsl:text>)</xsl:text>
					</option>
				</xsl:for-each>
			</select>
		</span>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

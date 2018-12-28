<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/display_form_input_time.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:variable name="tcEnabled" select="//meta/tc_enabled"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	
	<xsl:variable name="training_type" select="/applyeasy/cur_training_type"/>
	<xsl:variable name="is_integrated">
		<xsl:choose>
			<xsl:when test="$training_type ='INTEGRATED'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript"><![CDATA[
			itm_lst = new wbItem
			goldenman = new wbGoldenMan
			runInd = new Array()

			function select_type(val){
				if(document.frmXml.show_run_ind.type != 'hidden'){
				for(i=0; i<runInd.length; i++) {
					if( runInd[i] == val) {
						document.frmXml.show_run_ind.disabled = false;
						return;
					}
				}
				document.frmXml.show_run_ind.disabled = true;		
				}	
			}
			
			
			function tc_change(frm, disable) {
				if (frm.tcr_id_lst_box) {
					if(frm.genremoveint_training_provider) {
						frm.genremoveint_training_provider.disabled=disable;
					}
					if(frm.genaddint_training_provider) {
						frm.genaddint_training_provider.disabled=disable;
					}
					if (disable == true) {
						frm.tcr_id_lst_box.length=0;
					}
				}
			}
			
			function cat_change(frm, disable) {
				if (frm.tnd_id) {
					if (frm.genaddtnd_id) {
						frm.genaddtnd_id.disabled=disable;
					}
					if (frm.genremovetnd_id) {
						frm.genremovetnd_id.disabled=disable;
					}
					if (disable == true) {
						frm.tnd_id.length=0;
					}
				}
			}
			
			function changeChekQuota(frm){
			
				if(frm.itm_only_open_enrol_now.checked == true){
					frm.itm_only_open_enrol_quota_now.disabled = false
				} else {
					frm.itm_only_open_enrol_quota_now.checked = false;
					frm.itm_only_open_enrol_quota_now.disabled = true;
				}
				
			}
			
			function init_cat_sel() {
				cat_change(document.frmXml, true);
			}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_gen_form_focus(document.frmXml); init_cat_sel()">
			<form name="frmXml" method="post">
				<input type="hidden" name="all_ind" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="tvw_id" value=""/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet" value=""/>
				<input type="hidden" name="orderby" value=""/>
				<input type="hidden" name="sortorder" value=""/>
				<input type="hidden" name="show_respon" value=""/>
				<input type="hidden" name="cat_public_ind" value=""/>
				<input type="hidden" name="tcr_id_lst" value=""/>
				<input type="hidden" name="training_type" value="{$training_type}"/>
				<input type="hidden" name="adv_srh_ind" value="true"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		
		<xsl:apply-templates>
			<xsl:with-param name="lab_all_cata">全目錄</xsl:with-param>
			<xsl:with-param name="lab_title">標題關鍵字</xsl:with-param>
			<xsl:with-param name="lab_partial">部分</xsl:with-param>
			<xsl:with-param name="lab_exact">精確匹配</xsl:with-param>
			<xsl:with-param name="lab_category">目錄</xsl:with-param>
			<xsl:with-param name="lab_competence">技能</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_app_date">報名開始日期</xsl:with-param>
			<xsl:with-param name="lab_cos_date">開始日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_date_format">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_basic_info">基本訊息</xsl:with-param>
			<xsl:with-param name="lab_target_learner">目標學員</xsl:with-param>
			<xsl:with-param name="lab_targeted_learner">目標學員</xsl:with-param>
			<xsl:with-param name="lab_adv_search">
				<!-- <xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template> -->進階搜尋
			</xsl:with-param>
			<xsl:with-param name="lab_cos_lvl"> 級別</xsl:with-param>
			<xsl:with-param name="lab_run_lvl">班級級別</xsl:with-param>
			<xsl:with-param name="lab_inst_info">請指定搜尋標準:</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_item_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_item_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_item_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_code">
				<!-- <xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template> -->編號
			</xsl:with-param>
			<xsl:with-param name="lab_all_catalogs">所有目錄</xsl:with-param>
			<xsl:with-param name="lab_only_these_catalogs">指定以下目錄</xsl:with-param>
			<xsl:with-param name="lab_options">選項</xsl:with-param>
			<xsl:with-param name="lab_open_enrol_desc">只搜索當前可報名的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>
				<xsl:if test="$is_integrated !='true'">或<xsl:call-template name="get_class_lab">
					<xsl:with-param name="training_type" select="$training_type"/>
				</xsl:call-template>
				</xsl:if>。</xsl:with-param>
			<xsl:with-param name="lab_open_enrol_quota_desc">對於開放報名的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>，只搜索那些當前擁有剩餘名額的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>。</xsl:with-param>
			<xsl:with-param name="lab_class_room_desc">對於离线<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>，只搜索在以下時間範圍內開始的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>:</xsl:with-param>
			<xsl:with-param name="lab_tcr">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_all_tcs">所有培訓中心</xsl:with-param>
			<xsl:with-param name="lab_only_these_tcs">指定以下培訓中心</xsl:with-param>
			<xsl:with-param name="itm_head_txt">
				<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>搜索
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">新增</xsl:with-param>
			<xsl:with-param name="lab_code_desc">請輸入課程、考試、公開課編號</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates>
			<xsl:with-param name="lab_all_cata">目录</xsl:with-param>
			<xsl:with-param name="lab_title">名称关键字</xsl:with-param>
			<xsl:with-param name="lab_partial">部分</xsl:with-param>
			<xsl:with-param name="lab_exact">精确匹配</xsl:with-param>
			<xsl:with-param name="lab_category">目录</xsl:with-param>
			<xsl:with-param name="lab_competence">技能</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_select_all">--全部--</xsl:with-param>
			<xsl:with-param name="lab_app_date">开始日期</xsl:with-param>
			<xsl:with-param name="lab_cos_date">开始日期</xsl:with-param>
			<xsl:with-param name="lab_to">至</xsl:with-param>
			<xsl:with-param name="lab_date_format">年-月-日</xsl:with-param>
			<xsl:with-param name="lab_basic_info">基本信息</xsl:with-param>
			<xsl:with-param name="lab_target_learner">目标学员</xsl:with-param>
			<xsl:with-param name="lab_targeted_learner">目标学员</xsl:with-param>
			<xsl:with-param name="lab_adv_search">
				<!-- <xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template> -->高级搜索
			</xsl:with-param>
			<xsl:with-param name="lab_cos_lvl">
				<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>级别
			</xsl:with-param>
			<xsl:with-param name="lab_run_lvl">班级级别</xsl:with-param>
			<xsl:with-param name="lab_inst_info">请指定搜索标准：</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_item_status">状态</xsl:with-param>
			<xsl:with-param name="lab_item_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_item_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_code">
				<!-- <xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template> -->编号
			</xsl:with-param>
			<xsl:with-param name="lab_all_catalogs">所有目录</xsl:with-param>
			<xsl:with-param name="lab_only_these_catalogs">指定以下目录</xsl:with-param>
			<xsl:with-param name="lab_options">选项</xsl:with-param>
			<xsl:with-param name="lab_open_enrol_desc">只搜索当前可报名的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>
				<xsl:if test="$is_integrated !='true'">或<xsl:call-template  name="get_class_lab">
					<xsl:with-param name="training_type" select="$training_type"/>
				</xsl:call-template>
				</xsl:if>。</xsl:with-param>
			<xsl:with-param name="lab_open_enrol_quota_desc">对于开放报名的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>，只搜索那些当前拥有剩余名额的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>。</xsl:with-param>
			<xsl:with-param name="lab_class_room_desc">对于离线<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>，只搜索在以下时间范围内开始的<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>:</xsl:with-param>
			<xsl:with-param name="lab_tcr">培训中心</xsl:with-param>
			<xsl:with-param name="itm_head_txt">
				<xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template>搜索
			</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">添加</xsl:with-param>
			<xsl:with-param name="lab_all_tcs">所有培训中心</xsl:with-param>
			<xsl:with-param name="lab_only_these_tcs">指定以下培训中心</xsl:with-param>
			<xsl:with-param name="lab_code_desc">请输入课程、考试、公开课编号</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates>
			<xsl:with-param name="lab_all_cata">Learning catalogs</xsl:with-param>
			<xsl:with-param name="lab_title">Title keywords</xsl:with-param>
			<xsl:with-param name="lab_partial">Partial</xsl:with-param>
			<xsl:with-param name="lab_exact">Exact match</xsl:with-param>
			<xsl:with-param name="lab_category">Learning catalog</xsl:with-param>
			<xsl:with-param name="lab_competence">Competencies</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_select_all">--All--</xsl:with-param>
			<xsl:with-param name="lab_app_date">Enorllment start date</xsl:with-param>
			<xsl:with-param name="lab_cos_date">Course start date</xsl:with-param>
			<xsl:with-param name="lab_to">to</xsl:with-param>
			<xsl:with-param name="lab_date_format">(YYYY-MM-DD)</xsl:with-param>
			<xsl:with-param name="lab_basic_info">Search criteria</xsl:with-param>
			<xsl:with-param name="lab_target_learner">Targeted learners</xsl:with-param>
			<xsl:with-param name="lab_targeted_learner">Targeted learners</xsl:with-param>
			<xsl:with-param name="lab_adv_search">
				<!-- <xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template> --> Advanced search
			</xsl:with-param>
			<xsl:with-param name="lab_cos_lvl">display at course level</xsl:with-param>
			<xsl:with-param name="lab_run_lvl">display at class level</xsl:with-param>
			<xsl:with-param name="lab_inst_info">Please specify the search criteria below:</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_item_status">Status</xsl:with-param>
			<xsl:with-param name="lab_item_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_item_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_code">
				<!-- <xsl:call-template name="get_ity_title">
					<xsl:with-param name="dummy_type" select="$training_type"/>
				</xsl:call-template> --> code
			</xsl:with-param>
			<xsl:with-param name="lab_all_catalogs">All catalogs</xsl:with-param>
			<xsl:with-param name="lab_only_these_catalogs">Only these catalogs</xsl:with-param>
			<xsl:with-param name="lab_options">Options</xsl:with-param>
			<xsl:with-param name="lab_open_enrol_desc">only search those <xsl:if test="$is_integrated !='true'">course or class that </xsl:if>are open for enrollment.</xsl:with-param>
			<xsl:with-param name="lab_open_enrol_quota_desc">For learning solutions that are currently open for enrollment, only search those have quota available.</xsl:with-param>
			<xsl:with-param name="lab_class_room_desc">For classroom training, only search those with class starts in this period:</xsl:with-param>
			<xsl:with-param name="lab_tcr">Training center</xsl:with-param>
			<xsl:with-param name="itm_head_txt">Learning solution search</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">Add</xsl:with-param>
			<xsl:with-param name="lab_all_tcs">All training centers</xsl:with-param>
			<xsl:with-param name="lab_only_these_tcs">Only these training centers</xsl:with-param>
			<xsl:with-param name="lab_code_desc">Please specify the code of course or video</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="applyeasy">
		<xsl:param name="lab_all_cata"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_partial"/>
		<xsl:param name="lab_exact"/>
		<xsl:param name="lab_category"/>
		<xsl:param name="lab_competence"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_select_all"/>
		<xsl:param name="lab_app_date"/>
		<xsl:param name="lab_cos_date"/>
		<xsl:param name="lab_to"/>
		<xsl:param name="lab_date_format"/>
		<xsl:param name="lab_basic_info"/>
		<xsl:param name="lab_target_learner"/>
		<xsl:param name="lab_targeted_learner"/>
		<xsl:param name="lab_adv_search"/>
		<xsl:param name="lab_cos_lvl"/>
		<xsl:param name="lab_run_lvl"/>
		<xsl:param name="lab_inst_info"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_item_status"/>
		<xsl:param name="lab_item_status_on"/>
		<xsl:param name="lab_item_status_off"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_all_catalogs"/>
		<xsl:param name="lab_only_these_catalogs"/>
		<xsl:param name="lab_options"/>
		<xsl:param name="lab_open_enrol_desc"/>
		<xsl:param name="lab_open_enrol_quota_desc"/>
		<xsl:param name="lab_class_room_desc"/>
		<xsl:param name="lab_tcr"/>
		<xsl:param name="itm_head_txt"/>
		<xsl:param name="lab_g_txt_btn_add_lrn_sol"/>
		<xsl:param name="lab_all_tcs"/>
		<xsl:param name="lab_only_these_tcs"/>
		<xsl:param name="lab_code_desc" />

		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_CAT_MAIN</xsl:with-param>
		</xsl:call-template>
		
		
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_adv_search"/>
		</xsl:call-template>
		<!-- <xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text" select="$lab_inst_info"/>
		</xsl:call-template> -->
		<table>
			<tr>
				<td class="wzb-form-label" valign="top">
					<xsl:value-of select="$lab_code"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" class="wzb-inputText" name="code" style="width:300px;"/>
					 <br/><span class="wzb-ui-module-text"><xsl:value-of select="$lab_code_desc" /></span> 
				</td>
			</tr>
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_title"/>：
				</td>
				<td class="wzb-form-control">
					<input type="text" class="wzb-inputText" name="title" style="width:300px;"/>
					<xsl:text>&#160;</xsl:text>
					<label for="exact_id1">
						<input type="checkbox" name="exact" value="false" id="exact_id1"/>
						<xsl:value-of select="$lab_exact"/>
					</label>
				</td>
			</tr>
			<xsl:if test="count(item_reference_data/item_type_list/item_type) > 0 and $is_integrated = 'false'">
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_type"/>：
				</td>
				<td class="wzb-form-control">
					<select name="dummy_type" class="wzb-form-select" onchange="select_type(this.options[this.selectedIndex].value)">
						<xsl:if test="$training_type = 'ALL'">
							<option value="ALL">
								<xsl:value-of select="$lab_select_all"/>
							</option>
						</xsl:if>
						<xsl:for-each select="item_reference_data/item_type_list/item_type">
							<xsl:if test="not(@group_ind)">
								<option value="{@id}">
								
								<xsl:call-template name="get_ity_title">
									<xsl:with-param name="dummy_type" select="@title"/>
								</xsl:call-template>
							
								</option>
							</xsl:if>
						</xsl:for-each>
					</select>
					<!--
					<xsl:choose>
					<xsl:when test="count(/applyeasy/item_reference_data/item_type_meta_list/item_type_meta[@create_run_ind = 'true']) != 0">
					<img src="{$wb_img_path}tp.gif" width="1" height="8" border="0"/>
					<select name="show_run_ind" class="Select" disabled="disabled">
						<option value="false"><xsl:value-of select="$lab_cos_lvl"/></option>
						<option value="true"><xsl:value-of select="$lab_run_lvl"/></option>
					</select>
					</xsl:when>
					<xsl:otherwise>-->
					<input type="hidden" name="show_run_ind" value="false"/>
					<!--						</xsl:otherwise>
					</xsl:choose>-->
				</td>
			</tr>
			</xsl:if>
			<!--在此处加按状态搜索选项-->
			<xsl:choose>
				<xsl:when test="meta/cur_usr/role/@id = 'NLRN_1'">
					<!--//学生身份不显示状态搜索-->
				</xsl:when>
				<xsl:otherwise>
					<!-- 显示状态选项 -->
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_item_status"/>：
						</td>
						<td class="wzb-form-control">
							<input type="checkbox" name="status_chk" value="ON" id="status_on" checked="checked"/>
							<label for="status_on">
								<xsl:value-of select="$lab_item_status_on"/>
							</label>&#160;&#160;
							<input type="checkbox" name="status_chk" value="OFF" id="status_off" checked="checked"/>
							<label for="status_off">
								<xsl:value-of select="$lab_item_status_off"/>
							</label>
							<input type="hidden" name="status" value=""/>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
			<!--搜索状态选项结束-->
			<xsl:if test="$tcEnabled='true'">
				<!--  training center -->
				<tr>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_tcr"/>：
					</td>
					<td class="wzb-form-control">
						<table>
							<!--改为默认第二个选项,去掉"全部培训中心"-->
							<tr style="display:none">
								<td><input type="radio" name="tcr_sel_all" value="1" id="tc_sel_all" onclick="tc_change(document.frmXml, true)"/></td>
								<td><label for="tc_sel_all"><xsl:value-of select="$lab_all_tcs"/></label></td>
							</tr>
							<tr style="display:none">
								<td><input type="radio" name="tcr_sel_all" value="0" id="tc_sel_these" onclick="tc_change(document.frmXml, false)" checked="checked"/></td>
								<td><label for="tc_sel_these"><xsl:value-of select="$lab_only_these_tcs"/>：</label></td>
							</tr>
							<tr>
								<td>
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="frm">document.frmXml</xsl:with-param>
										<xsl:with-param name="name">int_training_provider</xsl:with-param>
										<xsl:with-param name="field_name">tcr_id_lst_box</xsl:with-param>
										<xsl:with-param name="tree_type">training_center</xsl:with-param>
										<xsl:with-param name="select_type">1</xsl:with-param>
										<xsl:with-param name="pick_root">0</xsl:with-param>
										<!--
										<xsl:with-param name="add_function">goldenman.pickmultitcrwin('tcr_id_lst_box')</xsl:with-param>
										-->
										<xsl:with-param name="option_list">
											<xsl:for-each select="item_reference_data/training_center_list/center">
												<option value="{@id}">
													<xsl:value-of select="."/>
												</option>
											</xsl:for-each>
										</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</xsl:if>
			<!-- catalog -->
			<tr>
					<input type="hidden" name="tnd_id_lst"/>
					<td class="wzb-form-label" valign="top">
						<xsl:value-of select="$lab_category"/>：
					</td>
					<td class="wzb-form-control">
						<input type="hidden" name="catalog_checkbox_ind" value="true"/>
						<table>
							<tr>
								<td width="2%" valign="top" col="2" >
									<input type="radio" name="cat_rdo" id="cat_all" checked="checked" onclick="cat_change(document.frmXml, true)"/>
								    <xsl:value-of select="$lab_all_catalogs"/>
								</td>
								<!-- <td width="98%">
									<label for="cat_all">
										
									</label>
								</td> -->
						    </tr>
							<tr> 
								<td valign="top" width="2%" col="2" >
									<input type="radio" name="cat_rdo" id="cat_select" onclick="cat_change(document.frmXml, false)"/>
								    <xsl:value-of select="$lab_only_these_catalogs"/> :
								</td>
								<!-- <td>
									<label for="cat_select">
										
										<xsl:text>：</xsl:text>
									</label>
								</td> -->
							</tr>
							<tr>
								<td align="left" col="2" >
									<xsl:variable name="tree_type">
										<xsl:choose>
											<xsl:when test="$tcEnabled = 'true'">tc_catalog</xsl:when>
											<xsl:otherwise>catalog</xsl:otherwise>
										</xsl:choose>
									</xsl:variable>								
									<xsl:call-template name="wb_goldenman">
										<xsl:with-param name="field_name">tnd_id</xsl:with-param>
										<xsl:with-param name="name">tnd_id</xsl:with-param>
										<xsl:with-param name="box_size">4</xsl:with-param>
																				
										<xsl:with-param name="tree_type"><xsl:value-of select="$tree_type"/></xsl:with-param>
										<xsl:with-param name="args_type">row</xsl:with-param>
										<xsl:with-param name="complusory_tree">0</xsl:with-param>
										<xsl:with-param name="select_type">1</xsl:with-param>
										<xsl:with-param name="pick_root">0</xsl:with-param>
										<xsl:with-param name="custom_js_code_extra">args = goldenman.set_global_catalog_label(args,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
										<xsl:with-param name="option_list">
											<xsl:apply-templates select="item_reference_data/catalog_list/catalog"/>
										</xsl:with-param>
										<xsl:with-param name="label_add_btn">
											<xsl:value-of select="$lab_gen_select"/>
										</xsl:with-param>
									</xsl:call-template>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			<xsl:variable name="span_show">
				<xsl:choose>
					<xsl:when test="$training_type ='COS' or $training_type ='EXAM'"></xsl:when>
					<xsl:otherwise>none</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			<xsl:variable name="span_show1">
				<xsl:choose>
					<xsl:when test="$training_type ='COS' or $training_type ='EXAM' or $training_type ='INTEGRATED'"></xsl:when>
					<xsl:otherwise>none</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
			

			<tr style="display:{$span_show}">
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_options"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:value-of select="$lab_class_room_desc"/>
					<br/>
				</td>
			</tr>
			<tr  style="display:{$span_show}">
				<td class="wzb-form-label">
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">eff_from</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">eff_from</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
					<xsl:text>&#160;</xsl:text>
					<xsl:value-of select="$lab_to"/>
					<xsl:text>&#160;</xsl:text>
					<xsl:call-template name="display_form_input_time">
						<xsl:with-param name="fld_name">eff_to</xsl:with-param>
						<xsl:with-param name="hidden_fld_name">eff_to</xsl:with-param>
						<xsl:with-param name="show_label">Y</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<!-- 
			<tr  style="display:{$span_show1}">
				<td width="20%" align="right" valign="top">
					<xsl:choose>
						<xsl:when test="$training_type ='INTEGRATED'">
							<span class="TitleText">
								<xsl:value-of select="$lab_options"/><xsl:text>：</xsl:text>
							</span>
						</xsl:when>
						<xsl:otherwise>
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
				<td width="80%">
					<span class="Text">
						<label for="itm_open_check_box1">
							<input id="itm_open_check_box1" type="checkbox" name="itm_only_open_enrol_now" onclick="changeChekQuota(document.frmXml)"/>
							<xsl:value-of select="$lab_open_enrol_desc"/>
						</label>
					</span>
				</td>
			</tr>
			<tr  style="display:{$span_show1}">
				<td width="20%" align="right" valign="top">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
				</td>
				<td width="80%">
					<img border="0" height="1" src="{$wb_img_path}tp.gif" width="15"/>
					<span class="Text">
						<label for="itm_quota_check_box1">
							<input id="itm_quota_check_box1" type="checkbox" name="itm_only_open_enrol_quota_now" disabled="disabled"/>
							<xsl:value-of select="$lab_open_enrol_quota_desc"/>
						</label>
					</span>
				</td>
			</tr>
			 -->
			 <tr>
				<td width="35%" align="right">
				</td>
				<td width="65%" align="left">
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.search_item_exec(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:window.history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>

</xsl:stylesheet>

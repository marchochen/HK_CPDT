<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/unescape_html_linefeed.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="cat_tcr_id" select="/applyeasy/cat_tcr_id"/>
	<xsl:variable name="node_id" select="/applyeasy/node/@node_id"/>
	<xsl:variable name="tnd_id" select="/applyeasy/node/nav/node[position() = last()]/@node_id"/>
	<xsl:variable name="isNode">
		<xsl:choose>
			<xsl:when test="/applyeasy/node/@type = 'CATALOG'">true</xsl:when>
			<xsl:otherwise>false</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="cur_node_id" select="/applyeasy/node/@node_id"/>
	<xsl:variable name="root_ent_id" select="/applyeasy/cur_usr/@root_ent_id"/>
	<xsl:variable name="cata_id" select="/applyeasy/node/@cat_id"/>
	<xsl:variable name="order_by" select="/applyeasy/node/pagination/@sort_col"/>
	<xsl:variable name="sort_order" select="/applyeasy/node/pagination/@sort_order"/>
	<xsl:variable name="order">
		<xsl:choose>
			<xsl:when test="$sort_order = 'asc' or $sort_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="itm_type_list_root" select="/applyeasy/node/item_type_list"/>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="page_size" select="/applyeasy/node/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/applyeasy/node/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/node/pagination/@total_rec"/>
	<xsl:variable name="show_properties" select="/applyeasy/meta/page_variant/@ShowNodeStatus"/>
	<xsl:variable name="tnd_cnt_lst" select="/applyeasy/node/tnd_cnt_lst"/>
	
	<!-- 没有子目录 -->
	<xsl:variable name="label_core_training_management_308" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_308')" />
	
	<!-- =============================================================== -->
	<xsl:template match="/applyeasy">
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
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_cata_lst.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}sso_link.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			itm_lst = new wbItem
			cata_lst = new wbCataLst
			sso = new wbSSO;
			var parent_tcr_id = getUrlParam('cat_tcr_id');
			
			function status(){
			itm_lst.simple_search_item_exec(document.frmSearch,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
			return false;
			}
			
			gen_set_cookie('url_success',self.location.href)
			wb_utils_set_cookie('cata_view',getUrlParam('list'))
			
			function changeTabs(tab){
				$("input[name='active_tab']").val(tab);
			}
			$(function(){
				var active_tab = getUrlParam('active_tab');
				if(active_tab != undefined && active_tab != null && active_tab != '' && active_tab != 'undefined'){
					$("ul[role='tablist'] li").each(function(){
						$(this).removeClass('active');
						if($(this).children('a').attr('aria-controls') == active_tab){
							$(this).addClass('active');
						}
					});
					$(".tab-pane").each(function(){
						$(this).removeClass('active');
					});
					$("#" + active_tab).addClass('active');
				}
			})
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmSearch" onSubmit="return status()">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="itm_life_status_not_equal_lst"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="tnd_id" value="{@node_id}"/>
				<input type="hidden" name="tnd_id_lst" value="{@node_id}"/>
				<input type="hidden" name="exact" value="false"/>
				<input type="hidden" name="page_size" value="10"/>
				<input type="hidden" name="page" value="1"/>
				<input type="hidden" name="sortorder" value="asc"/>
				<input type="hidden" name="orderby" value="itm_code"/>
				<input type="hidden" name="tvw_id" value=""/>
				<input type="hidden" name="all_ind" value="true"/>
				<input type="hidden" name="training_type" value="ALL"/>
				<!-- for sso link get -->
				<input type="hidden" name="learner_catalog" value="{//learner_catalog/location/text()}"/>
				<input type="hidden" name="root" value="{//root/text()}"/>
				<input type="hidden" name="tcr_id_lst" value="-1"/>
				<input type="hidden" name="sso_link" value=""/>
				<input type="hidden" name="active_tab" value="CourseList"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">課程目錄管理</xsl:with-param>
			<xsl:with-param name="lab_inst">請選擇所需目錄</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">還沒有任何課程</xsl:with-param>
			<xsl:with-param name="lab_code">編號</xsl:with-param>
			<xsl:with-param name="lab_title">培訓名稱</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_attbs">屬性</xsl:with-param>
			<xsl:with-param name="lab_status_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_status_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_cat_code">編號</xsl:with-param>
			<xsl:with-param name="lab_cat_name">目錄名稱</xsl:with-param>
			<xsl:with-param name="lab_total">總數</xsl:with-param>
			<xsl:with-param name="lab_all_usr">所有</xsl:with-param>
			<xsl:with-param name="lab_all_usr_in">用戶</xsl:with-param>
			<xsl:with-param name="lab_search_by_all">所有目錄/培訓</xsl:with-param>
			<xsl:with-param name="lab_search_by_index">此目錄/培訓下</xsl:with-param>
			<xsl:with-param name="lab_other">其他</xsl:with-param>
			<xsl:with-param name="lab_del">已刪除</xsl:with-param>
			<xsl:with-param name="lab_item_type">培訓類型</xsl:with-param>
			<xsl:with-param name="lab_items">所有培訓</xsl:with-param>
			<xsl:with-param name="lab_cata_content">內容</xsl:with-param>
			<xsl:with-param name="lab_node_content">子目錄內容</xsl:with-param>
			<xsl:with-param name="lab_acss_restrict">存取範圍</xsl:with-param>
			<xsl:with-param name="lab_catalog">目錄</xsl:with-param>
			<xsl:with-param name="lab_category">子目錄</xsl:with-param>
			<xsl:with-param name="lab_categories">子目錄</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">描述</xsl:with-param>
			<xsl:with-param name="lab_cata_creator">創建者</xsl:with-param>
			<xsl:with-param name="lab_remove_from_category">從目前子目錄刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">新增子目錄</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_set">設置移動目錄</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_cancel">取消移動目錄</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_msg">已存在移動課程目錄，請先取消已設置的移動課程目錄。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">刪除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">新增培訓</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">刪除</xsl:with-param>
			<xsl:with-param name="lab_sso_link">複製單點登錄鏈結</xsl:with-param>
			<xsl:with-param name="lab_tcr">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_child_node">子目錄</xsl:with-param>
			<xsl:with-param name="lab_course">培訓</xsl:with-param>
			<xsl:with-param name="lab_course_list">课程列表</xsl:with-param>
			<xsl:with-param name="lab_catalog_info">目录資訊</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">课程目录管理</xsl:with-param>
			<xsl:with-param name="lab_inst">请选择所需类别目录</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">还没有任何课程</xsl:with-param>
			<xsl:with-param name="lab_code">编号</xsl:with-param>
			<xsl:with-param name="lab_title">名称</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_attbs">类别</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_cat_code">编号</xsl:with-param>
			<xsl:with-param name="lab_cat_name">
			     <xsl:choose>
				 <xsl:when test="$isNode = 'false'">子</xsl:when>
				 <xsl:otherwise></xsl:otherwise>
			</xsl:choose>目录名称
			 </xsl:with-param>
			<xsl:with-param name="lab_total">总计</xsl:with-param>
			<xsl:with-param name="lab_all_usr">所有</xsl:with-param>
			<xsl:with-param name="lab_all_usr_in">用户</xsl:with-param>
			<xsl:with-param name="lab_search_by_all">所有目录/培训</xsl:with-param>
			<xsl:with-param name="lab_search_by_index">此目录/培训</xsl:with-param>
			<xsl:with-param name="lab_other">其他</xsl:with-param>
			<xsl:with-param name="lab_del">已删除</xsl:with-param>
			<xsl:with-param name="lab_item_type">培训类型</xsl:with-param>
			<xsl:with-param name="lab_items">所有培训</xsl:with-param>
			<xsl:with-param name="lab_cata_content">目录内容</xsl:with-param>
			<xsl:with-param name="lab_node_content">子目录内容</xsl:with-param>
			<xsl:with-param name="lab_acss_restrict">存取范围</xsl:with-param>
			<xsl:with-param name="lab_catalog">目录</xsl:with-param>
			<xsl:with-param name="lab_category">子目录</xsl:with-param>
			<xsl:with-param name="lab_categories">子目录</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">描述</xsl:with-param>
			<xsl:with-param name="lab_cata_creator">创建者</xsl:with-param>
			<xsl:with-param name="lab_remove_from_category">从当前子目录删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">添加子目录</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_set">设置移动目录</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_cancel">取消移动目录</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_msg">已存在移动课程目录，请先取消已设置的移动课程目录。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">修改</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">删除</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">添加培训</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">删除</xsl:with-param>
			<xsl:with-param name="lab_sso_link">复制单点登录链接</xsl:with-param>
			<xsl:with-param name="lab_tcr">培训中心</xsl:with-param>
			<xsl:with-param name="lab_child_node">子目录</xsl:with-param>
			<xsl:with-param name="lab_course">培训</xsl:with-param>
			<xsl:with-param name="lab_course_list">课程列表</xsl:with-param>
			<xsl:with-param name="lab_catalog_info">目录信息</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">Learning catalog</xsl:with-param>
			<xsl:with-param name="lab_inst">Please select a suitable catalog</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">No learning solutions available in this category.</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_attbs">Attributes</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_cat_code">Code</xsl:with-param>
			<xsl:with-param name="lab_cat_name">Name</xsl:with-param>
			<xsl:with-param name="lab_total">Total</xsl:with-param>
			<xsl:with-param name="lab_all_usr">All users in </xsl:with-param>
			<xsl:with-param name="lab_all_usr_in"/>
			<xsl:with-param name="lab_search_by_all">All catalog</xsl:with-param>
			<xsl:with-param name="lab_search_by_index">This catalog/category only</xsl:with-param>
			<xsl:with-param name="lab_other">Other</xsl:with-param>
			<xsl:with-param name="lab_del">deleted</xsl:with-param>
			<xsl:with-param name="lab_item_type">Learning solution type</xsl:with-param>
			<xsl:with-param name="lab_items">Learning solutions</xsl:with-param>
			<xsl:with-param name="lab_cata_content">Catalog content</xsl:with-param>
			<xsl:with-param name="lab_node_content">Category content</xsl:with-param>
			<xsl:with-param name="lab_acss_restrict">User who should see this catalog</xsl:with-param>
			<xsl:with-param name="lab_catalog">Catalog</xsl:with-param>
			<xsl:with-param name="lab_category">Category</xsl:with-param>
			<xsl:with-param name="lab_categories">Subcategories</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">Description</xsl:with-param>
			<xsl:with-param name="lab_cata_creator">Creator</xsl:with-param>
			<xsl:with-param name="lab_remove_from_category">Remove from category</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add">Add sub-catalog</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_set">Set mobile catalog</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_cancel">Cancel mobile catalog</xsl:with-param>
			<xsl:with-param name="lab_btn_mobile_msg">Please cancel the existing mobile catalog first.</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_edit">Edit</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_del">Delete</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_lrn_sol">Add learning solution</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_remove">Remove</xsl:with-param>
			<xsl:with-param name="lab_sso_link">Copy SSO link</xsl:with-param>
			<xsl:with-param name="lab_tcr">Training center</xsl:with-param>
			<xsl:with-param name="lab_child_node">Sub-catalog</xsl:with-param>
			<xsl:with-param name="lab_course">Courses</xsl:with-param>
			<xsl:with-param name="lab_course_list">Course list</xsl:with-param>
			<xsl:with-param name="lab_catalog_info">Catalog information</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_all_cata"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_itm_empty"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_attbs"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_cat_code"/>
		<xsl:param name="lab_cat_name"/>
		<xsl:param name="lab_total"/>
		<xsl:param name="lab_no_assigned_usr"/>
		<xsl:param name="lab_all_usr"/>
		<xsl:param name="lab_all_usr_in"/>
		<xsl:param name="lab_search_by_all"/>
		<xsl:param name="lab_search_by_index"/>
		<xsl:param name="lab_other"/>
		<xsl:param name="lab_remove_from_category"/>
		<xsl:param name="lab_del"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_items"/>
		<xsl:param name="lab_cata_content"/>
		<xsl:param name="lab_node_content"/>
		<xsl:param name="lab_acss_restrict"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_category"/>
		<xsl:param name="lab_categories"/>
		<xsl:param name="lab_cata_desc"/>
		<xsl:param name="lab_cata_creator"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_btn_mobile_set"/>
		<xsl:param name="lab_btn_mobile_cancel"/>
		<xsl:param name="lab_btn_mobile_msg"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_del"/>
		<xsl:param name="lab_g_txt_btn_add_lrn_sol"/>
		<xsl:param name="lab_g_form_btn_remove"/>
		<xsl:param name="lab_sso_link"/>
		<xsl:param name="lab_tcr"/>
		<xsl:param name="lab_child_node"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_course_list"/>
		<xsl:param name="lab_catalog_info"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CAT_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="node/title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="$show_properties != 'true'">
			<xsl:call-template name="wb_ui_desc">
				<xsl:with-param name="text">
					<xsl:call-template name="unescape_html_linefeed">
						<xsl:with-param name="my_right_value">
							<xsl:value-of select="node/desc"/>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:wb_utils_cata_lst('',{$cat_tcr_id})" class="NavLink">
					<xsl:value-of select="$lab_all_cata"/>
				</a>
				<xsl:for-each select="node/nav/node">
					<xsl:choose>
						<xsl:when test="position() != last()">
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<a href="javascript:wb_utils_node_lst({@node_id},'','','','','',{$cat_tcr_id})" class="NavLink">
								<xsl:value-of select="title"/>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&#160;&gt;&#160;</xsl:text>
							<xsl:value-of select="title"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:choose>
			<xsl:when test="count(node/child_nodes/child)=0 and count(node/item_nodes/item) = 0 and $show_properties!= 'true'">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_itm_empty"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
		<!-- 搜索框 -->
		<table class="wzb-ui-head">
			<tr>
				<td align="right">
					<div class="wzb-form-search" style="float:right;">
						<input type="text" size="11" name="title_code" class="form-control "
							style="width:130px;" />
						<input type="button" class="form-submit margin-right4" value=""
							onclick="itm_lst.simple_search_item_exec(document.frmSearch, '{$wb_lang}')" />
						<a href="javascript:itm_lst.search_item_prep({$tnd_id})" class="btn wzb-btn-blue margin-right2 vbtm">
							<xsl:value-of select="$lab_adv_search" />
						</a>
					</div>
				</td>
			</tr>
		</table>
		  
		   
			<xsl:apply-templates select="node">
					<xsl:with-param name="lab_inst" select="$lab_inst"/>
					<xsl:with-param name="lab_status" select="$lab_status"/>
					<xsl:with-param name="lab_item_type" select="$lab_item_type"/>
					<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
					<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
					<xsl:with-param name="lab_cat_code" select="$lab_cat_code"/>
					<xsl:with-param name="lab_cat_name" select="$lab_cat_name"/>
					<xsl:with-param name="lab_search_by_index" select="$lab_search_by_index"/>
					<xsl:with-param name="lab_search_by_all" select="$lab_search_by_all"/>
					<xsl:with-param name="lab_cata_content" select="$lab_cata_content"/>
					<xsl:with-param name="lab_node_content" select="$lab_node_content"/>
					<xsl:with-param name="lab_acss_restrict" select="$lab_acss_restrict"/>
					<xsl:with-param name="lab_catalog" select="$lab_catalog"/>
					<xsl:with-param name="lab_category" select="$lab_category"/>
					<xsl:with-param name="lab_categories" select="$lab_categories"/>
					<xsl:with-param name="lab_cata_desc" select="$lab_cata_desc"/>
					<xsl:with-param name="lab_cata_creator" select="$lab_cata_creator"/>
					<xsl:with-param name="lab_remove_from_category" select="$lab_remove_from_category"/>
					<xsl:with-param name="lab_g_txt_btn_add" select="$lab_g_txt_btn_add"/>
					<xsl:with-param name="lab_btn_mobile_set" select="$lab_btn_mobile_set"/>
					<xsl:with-param name="lab_btn_mobile_cancel" select="$lab_btn_mobile_cancel"/>
					<xsl:with-param name="lab_btn_mobile_msg" select="$lab_btn_mobile_msg"/>
					<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
					<xsl:with-param name="lab_g_txt_btn_del" select="$lab_g_txt_btn_del"/>
					<xsl:with-param name="lab_g_txt_btn_add_lrn_sol" select="$lab_g_txt_btn_add_lrn_sol"/>
					<xsl:with-param name="lab_sso_link" select="$lab_sso_link"/>
					<xsl:with-param name="lab_tcr" select="$lab_tcr"/>
					<xsl:with-param name="lab_child_node" select="$lab_child_node"/>
					<xsl:with-param name="lab_course" select="$lab_course"/>
					<xsl:with-param name="lab_catalog_info" select="$lab_catalog_info"/>
					
				</xsl:apply-templates>
			
				<xsl:apply-templates select="node/child_nodes">
					<xsl:with-param name="lab_inst" select="$lab_inst"/>
					<xsl:with-param name="lab_status" select="$lab_status"/>
					<xsl:with-param name="lab_item_type" select="$lab_item_type"/>
					<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
					<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
					<xsl:with-param name="lab_cat_code" select="$lab_cat_code"/>
					<xsl:with-param name="lab_cat_name" select="$lab_cat_name"/>
					<xsl:with-param name="lab_search_by_index" select="$lab_search_by_index"/>
					<xsl:with-param name="lab_search_by_all" select="$lab_search_by_all"/>
					<xsl:with-param name="lab_cata_content" select="$lab_cata_content"/>
					<xsl:with-param name="lab_node_content" select="$lab_node_content"/>
					<xsl:with-param name="lab_acss_restrict" select="$lab_acss_restrict"/>
					<xsl:with-param name="lab_catalog" select="$lab_catalog"/>
					<xsl:with-param name="lab_category" select="$lab_category"/>
					<xsl:with-param name="lab_categories" select="$lab_categories"/>
					<xsl:with-param name="lab_cata_desc" select="$lab_cata_desc"/>
					<xsl:with-param name="lab_cata_creator" select="$lab_cata_creator"/>
					<xsl:with-param name="lab_remove_from_category" select="$lab_remove_from_category"/>
					<xsl:with-param name="lab_g_txt_btn_add" select="$lab_g_txt_btn_add"/>
					<xsl:with-param name="lab_btn_mobile_set" select="$lab_btn_mobile_set"/>
					<xsl:with-param name="lab_btn_mobile_cancel" select="$lab_btn_mobile_cancel"/>
					<xsl:with-param name="lab_btn_mobile_msg" select="$lab_btn_mobile_msg"/>
					<xsl:with-param name="lab_g_txt_btn_edit" select="$lab_g_txt_btn_edit"/>
					<xsl:with-param name="lab_g_txt_btn_del" select="$lab_g_txt_btn_del"/>
					<xsl:with-param name="lab_g_txt_btn_add_lrn_sol" select="$lab_g_txt_btn_add_lrn_sol"/>
					<xsl:with-param name="lab_sso_link" select="$lab_sso_link"/>
					<xsl:with-param name="lab_tcr" select="$lab_tcr"/>
					<xsl:with-param name="lab_child_node" select="$lab_child_node"/>
					<xsl:with-param name="lab_course" select="$lab_course"/>
				</xsl:apply-templates>
		
				<xsl:apply-templates select="node/item_nodes">
					<xsl:with-param name="lab_items" select="$lab_items"/>
					<xsl:with-param name="lab_itm_empty" select="$lab_itm_empty"/>
					<xsl:with-param name="lab_code" select="$lab_code"/>
					<xsl:with-param name="lab_title" select="$lab_title"/>
					<xsl:with-param name="lab_type" select="$lab_type"/>
					<xsl:with-param name="lab_status" select="$lab_status"/>
					<xsl:with-param name="lab_attbs" select="$lab_attbs"/>
					<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
					<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
					<xsl:with-param name="lab_total" select="$lab_total"/>
					<xsl:with-param name="lab_other" select="$lab_other"/>
					<xsl:with-param name="lab_remove_from_category" select="$lab_remove_from_category"/>
					<xsl:with-param name="lab_g_form_btn_remove" select="$lab_g_form_btn_remove"/>
					<xsl:with-param name="lab_course_list" select="$lab_course_list"/>
					
				</xsl:apply-templates>
	
				
			
			<!-- node items list -->

	
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="node">
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_cat_code"/>
		<xsl:param name="lab_cat_name"/>
		<xsl:param name="lab_search_by_index"/>
		<xsl:param name="lab_search_by_all"/>
		<xsl:param name="lab_cata_content"/>
		<xsl:param name="lab_node_content"/>
		<xsl:param name="lab_acss_restrict"/>
		<xsl:param name="lab_cata_desc"/>
		<xsl:param name="lab_category"/>
		<xsl:param name="lab_categories"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_cata_lst"/>
		<xsl:param name="lab_cata_creator"/>
		<xsl:param name="lab_remove_from_category"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_btn_mobile_set"/>
		<xsl:param name="lab_btn_mobile_cancel"/>
		<xsl:param name="lab_btn_mobile_msg"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_del"/>
		<xsl:param name="lab_g_txt_btn_add_lrn_sol"/>
		<xsl:param name="lab_sso_link"/>
		<xsl:param name="lab_tcr"/>
		<xsl:param name="lab_child_node"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_catalog_info"/>
		<!-- node info -->
		<xsl:variable name="_edit_function">
			<xsl:choose>
				<xsl:when test="$isNode = 'true'">javascript:cata_lst.edit_prep(<xsl:value-of select="@cat_id"/>,parent_tcr_id)</xsl:when>
				<xsl:otherwise>javascript:cata_lst.node_list.edit_prep('<xsl:value-of select="@node_id"/>')</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="_del_function">
			<xsl:choose>
				<xsl:when test="$isNode = 'true'">javascript:cata_lst.del_exec(<xsl:value-of select="@cat_id"/>,'<xsl:value-of select="last_updated/@timestamp"/>','<xsl:value-of select="$wb_lang"/>')</xsl:when>
				<xsl:otherwise>javascript:cata_lst.node_list.del_exec(<xsl:value-of select="@node_id"/>,'<xsl:value-of select="last_updated/@timestamp"/>','<xsl:value-of select="nav/node[position() = last() - 1]/@node_id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
			<xsl:if test="$show_properties = 'true'">
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_catalog_info"/>
					</xsl:with-param>
				
				 
					<xsl:with-param name="extra_td">
						<!-- simple -->
						<td>
							<div class="wzb-form-search">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_edit"/>
									<xsl:with-param name="wb_gen_btn_href" select="$_edit_function"/>
								</xsl:call-template>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_del"/>
									<xsl:with-param name="wb_gen_btn_href" select="$_del_function"/>
								</xsl:call-template>
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.node_list.add_prep('<xsl:value-of select="$node_id"/>')</xsl:with-param>
								</xsl:call-template>
			
							</div>
						</td>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_line"/>
				<table>
					<xsl:if test="code and code !='' and code != 'null'">
						<tr>
							<td class="wzb-form-label">
								<xsl:value-of select="$lab_cat_code"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="code"/>
								<xsl:text>&#160;</xsl:text>
							</td>
						</tr>
					</xsl:if>
					<tr>
						<td class="wzb-form-label">
							<xsl:value-of select="$lab_cat_name"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:value-of select="title"/>
							<xsl:text>&#160;</xsl:text>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_cata_desc"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:call-template name="unescape_html_linefeed">
								<xsl:with-param name="my_right_value">
									<xsl:value-of select="desc"/>
								</xsl:with-param>
							</xsl:call-template>
							<xsl:if test="string-length(desc) = 0">
								<xsl:text>--</xsl:text>
							</xsl:if>
							<xsl:text>&#160;</xsl:text>
						</td>
					</tr>
					<xsl:choose>
						<xsl:when test="$page_variant/@ShowNodeStatus = 'true'">
							<tr>
								<td class="wzb-form-label">
									<xsl:value-of select="$lab_status"/>：
								</td>
								<td class="wzb-form-control">
									<xsl:choose>
										<xsl:when test="@status = 'ON' ">
											<xsl:value-of select="$lab_status_on"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$lab_status_off"/>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:text>&#160;</xsl:text>
								</td>
							</tr>
						</xsl:when>
					</xsl:choose>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_cata_creator"/>：
						</td>
						<td class="wzb-form-control">
								<xsl:call-template name="unescape_html_linefeed">
									<xsl:with-param name="my_right_value">
										<xsl:value-of select="creator"/>
									</xsl:with-param>
								</xsl:call-template>
								<xsl:if test="string-length(creator) = 0">
									 -
							    </xsl:if>
						</td>
					</tr>
					<!-- add cat tc info -->
					<xsl:if test="$isNode = 'true' and //tc_enabled='true'">
						<tr>
							<td class="wzb-form-label" valign="top">
								<xsl:value-of select="$lab_tcr"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:value-of select="training_center/title"/>
							</td>
						</tr>
					</xsl:if>
				</table>
			</xsl:if>
	</xsl:template>
	<xsl:template match="node/child_nodes">
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_item_type"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_cat_code"/>
		<xsl:param name="lab_cat_name"/>
		<xsl:param name="lab_search_by_index"/>
		<xsl:param name="lab_search_by_all"/>
		<xsl:param name="lab_cata_content"/>
		<xsl:param name="lab_node_content"/>
		<xsl:param name="lab_acss_restrict"/>
		<xsl:param name="lab_cata_desc"/>
		<xsl:param name="lab_category"/>
		<xsl:param name="lab_categories"/>
		<xsl:param name="lab_catalog"/>
		<xsl:param name="lab_cata_lst"/>
		<xsl:param name="lab_cata_creator"/>
		<xsl:param name="lab_remove_from_category"/>
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:param name="lab_btn_mobile_set"/>
		<xsl:param name="lab_btn_mobile_cancel"/>
		<xsl:param name="lab_btn_mobile_msg"/>
		<xsl:param name="lab_g_txt_btn_edit"/>
		<xsl:param name="lab_g_txt_btn_del"/>
		<xsl:param name="lab_g_txt_btn_add_lrn_sol"/>
		<xsl:param name="lab_sso_link"/>
		<xsl:param name="lab_tcr"/>
		<xsl:param name="lab_child_node"/>
		<xsl:param name="lab_course"/>
		
		
		<xsl:choose>
			<xsl:when test="count(child) = 0">
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_head">
					<xsl:with-param name="text">
						<xsl:value-of select="$lab_child_node"/>
					</xsl:with-param>
				</xsl:call-template>
				
				<xsl:call-template name="wb_ui_line"/>
				<!-- child node list -->
				<table border="0">
				<!--  
					<xsl:variable name="row">
						<xsl:choose>
							<xsl:when test="count(child) &lt; 4">1</xsl:when>
							
							<xsl:otherwise>
								<xsl:value-of select="floor(count(child) div 4) + 1"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<tr>
						<xsl:for-each select="child">
							<xsl:if test="position() mod $row = 1 ">
								<xsl:text disable-output-escaping="yes">&lt;td width="33%" valign="top"&gt;&lt;table border="0" cellpadding="3" cellspacing="0" border="0"&gt;</xsl:text>
							</xsl:if>
							<tr>
								<xsl:call-template name="draw_node">
									<xsl:with-param name="lab_remove_from_category">
										<xsl:value-of select="$lab_remove_from_category"/>
									</xsl:with-param>
									<xsl:with-param name="lab_child_node" select="$lab_child_node"/>
									<xsl:with-param name="lab_course" select="$lab_course"/>
								</xsl:call-template>
							</tr>
							<xsl:if test="position() mod $row = 0 and position() != last()">
								<xsl:text disable-output-escaping="yes">&lt;/table&gt;&lt;/td&gt;</xsl:text>
							</xsl:if>
							<xsl:if test="position() = last()">
								<xsl:text disable-output-escaping="yes">&lt;/table&gt;&lt;/td&gt;&lt;td width="1%"&gt;&lt;/td&gt;</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</tr>
					-->
					
					<xsl:for-each select="child">
							<xsl:if test="position() mod 4 = 1 ">
								<xsl:text disable-output-escaping="yes">&lt;tr&gt;</xsl:text>
							</xsl:if>
							<td width="25%" valign="top">
								<xsl:call-template name="draw_node">
									<xsl:with-param name="lab_remove_from_category">
										<xsl:value-of select="$lab_remove_from_category"/>
									</xsl:with-param>
									<xsl:with-param name="lab_child_node" select="$lab_child_node"/>
									<xsl:with-param name="lab_course" select="$lab_course"/>
								</xsl:call-template>
							</td>
							<td width="1%" >
								
							</td>
							<xsl:if test="position() mod 4 = 0 or position() = last()">
								<xsl:text disable-output-escaping="yes">&lt;/tr&gt;</xsl:text>
								<tr>
									<td height="10" colspan="2">
										<img border="0" height="1" width="1" src="{$wb_img_path}tp.gif"/>
									</td>
								</tr>
							</xsl:if>
						
						</xsl:for-each>
				</table>
			
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="node/item_nodes">
		<xsl:param name="lab_items" />
		<xsl:param name="lab_itm_empty" />
		<xsl:param name="lab_code" />
		<xsl:param name="lab_title" />
		<xsl:param name="lab_type" />
		<xsl:param name="lab_status" />
		<xsl:param name="lab_attbs" />
		<xsl:param name="lab_status_on" />
		<xsl:param name="lab_status_off" />
		<xsl:param name="lab_total" />
		<xsl:param name="lab_other" />
		<xsl:param name="lab_remove_from_category" />
		<xsl:param name="lab_g_form_btn_remove" />
		<xsl:param name="lab_course_list" />
		
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_course_list"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_line"/>
		<xsl:choose>
			<xsl:when test="count(item) = 0">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_itm_empty"/>
				</xsl:call-template>
				<!-- empty item -->
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="35%"> 
							<xsl:choose>
								<xsl:when test="$order_by = 'tnd_title'">
									<a href="javascript:wb_utils_node_lst({$tnd_id},'item','tnd_title','{$order}','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
										<xsl:value-of select="$lab_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_node_lst({$tnd_id},'item','tnd_title','asc','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
										<xsl:value-of select="$lab_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="20%" align="left">
							<xsl:choose>
								<xsl:when test="$order_by = 'itm_code'">
									<a href="javascript:wb_utils_node_lst({$tnd_id},'item','itm_code','{$order}','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
										<xsl:value-of select="$lab_code"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_node_lst({$tnd_id},'item','itm_code','asc','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
										<xsl:value-of select="$lab_code"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td align="left" width="20%">
							<xsl:choose>
								<xsl:when test="$order_by = 'itm_type' ">
									<a href="javascript:wb_utils_node_lst({$tnd_id},'item','itm_type','{$order}','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
										<xsl:value-of select="$lab_type"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:wb_utils_node_lst({$tnd_id},'item','itm_type','asc','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
										<xsl:value-of select="$lab_type"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<xsl:choose>
							<xsl:when test="$page_variant/@ShowItemStatus = 'true'">
								<td align="right" width="10%">
									<xsl:choose>
										<xsl:when test="$order_by = 'itm_status' ">
											<a href="javascript:wb_utils_node_lst({$tnd_id},'item','itm_status','{$order}','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
												<xsl:value-of select="$lab_status"/>
												<xsl:call-template name="wb_sortorder_cursor">
													<xsl:with-param name="img_path" select="$wb_img_path"/>
													<xsl:with-param name="sort_order" select="$sort_order"/>
												</xsl:call-template>
											</a>
										</xsl:when>
										<xsl:otherwise>
											<a href="javascript:wb_utils_node_lst({$tnd_id},'item','itm_status','asc','{$cur_page}','{$page_size}',{$cat_tcr_id})" class="TitleText">
												<xsl:value-of select="$lab_status"/>
											</a>
										</xsl:otherwise>
									</xsl:choose>
								</td>
							</xsl:when>
						</xsl:choose>
					</tr>
					<xsl:for-each select="item">
						<tr valign="middle">
							<!-- item  -->
							<xsl:call-template name="course_lst">
								<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
								<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
								<xsl:with-param name="lab_other" select="$lab_other"/>
								<xsl:with-param name="lab_remove_from_category" select="$lab_remove_from_category"/>
							</xsl:call-template>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="course_lst">
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_other"/>
		<xsl:param name="lab_remove_from_category"/>
		<xsl:variable name="node_nm">
			<xsl:call-template name="escape_js">
				<xsl:with-param name="input_str">
					<xsl:value-of select="//applyeasy/node/title"/>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:variable>
		<td>
				<!-- Access Control -->
			<xsl:choose>
				<xsl:when test="$page_variant/@hasItemDtlLink = 'true'">
					<a href="javascript:itm_lst.get_item_detail({@item_id})" class="Text color-blue108">
						<xsl:value-of select="title"/>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a href="javascript:itm_lst.get_item_lrn_detail({@item_id})" class="Text">
						<xsl:value-of select="title"/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
			<!-- Access Control -->
		</td>
		<td align="left" width="10%">
			<xsl:choose>
				<xsl:when test="@item_code != ''">
					<xsl:value-of select="@item_code"/>
				</xsl:when>
				<xsl:when test="@item_code = ''">
				</xsl:when>
			</xsl:choose>
		</td>
		<td align="left" style="">
			<xsl:call-template name="get_ity_title">
				<xsl:with-param name="itm_type" select="@item_dummy_type"/>
			</xsl:call-template>
		</td>
		<xsl:choose>
			<xsl:when test="$page_variant/@ShowItemStatus = 'true'">
				<td align="right">
					<xsl:choose>
						<xsl:when test="@status = 'OFF'">
							<xsl:value-of select="$lab_status_off"/>
						</xsl:when>
						<xsl:when test="@status = 'ON'">
							<xsl:value-of select="$lab_status_on"/>
						</xsl:when>
						<xsl:otherwise>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="draw_node">
		<xsl:param name="lab_remove_from_category"/>
		<xsl:param name="lab_child_node"/>
		<xsl:param name="lab_course"/>
		
			<table>
				<tr>
					<td width="20" valign="top">
						<xsl:choose>
							<xsl:when test=" @type = 'LINK' ">
								<img src="{$wb_img_path}ico_link.gif" border="0"/>
							</xsl:when>
							<xsl:otherwise>
								<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					<td width="680" valign="top">
						<a href="javascript:wb_utils_node_lst({@node_id},'','','','','',{$cat_tcr_id})" class="Text">
							<xsl:value-of select="title"/>
						</a>
						(<xsl:value-of select="@child_node_count"/>&#160;<xsl:value-of select="$lab_child_node"/>)
					</td>
				</tr>
				<tr>
					<td width="35">
					</td>
					<td><xsl:variable name="tnd_id_" select="@node_id"/>
						<xsl:choose>
							<xsl:when test=" @type != 'LINK'">
								<span class="Text">
									<xsl:text>&#160;</xsl:text>
									<xsl:choose>
										<xsl:when test="$page_variant/@noShowItemCount = 'false'">	
											<xsl:choose>
												<xsl:when test="$page_variant/@ShowItemStatus = 'true'">
													<xsl:value-of select="@cur_level_item_count"/>
												</xsl:when>
												<xsl:otherwise>
													<xsl:value-of select="@on_item_count"/>
												</xsl:otherwise>
											</xsl:choose>
										</xsl:when>
										<xsl:otherwise>
											<xsl:if test="$tnd_cnt_lst">
												<xsl:choose>
												<xsl:when test="$tnd_cnt_lst/tnd_cnt[@tnd_id=$tnd_id_]"><xsl:value-of select="$tnd_cnt_lst/tnd_cnt[@tnd_id=$tnd_id_]/@cnt"/></xsl:when>
												<xsl:otherwise>0</xsl:otherwise>
												</xsl:choose>
											</xsl:if>
										</xsl:otherwise>
									</xsl:choose>
									&#160;<xsl:value-of select="$lab_course"/>
								</span>
							</xsl:when>
							<xsl:when test="@type = 'LINK'">
								<xsl:text>&#160;&#160;</xsl:text>
								<!-- Access Control -->
								<xsl:choose>
									<xsl:when test="$page_variant/@hasDelCataBtn = 'true'">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="wb_gen_btn_name" select="$lab_remove_from_category"/>
											<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.node_list.del_exec(<xsl:value-of select="@node_id"/>,'<xsl:value-of select="last_updated/@timestamp"/>','<xsl:value-of select="../../nav/node[position() = last()]/@node_id"/>','<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi" select="position()"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
									</xsl:otherwise>
								</xsl:choose>
								<!-- Access Control -->
							</xsl:when>
						</xsl:choose>
					</td>
				</tr>
			</table>
		
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

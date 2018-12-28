<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="share/div_tree_share.xsl"/>
	<xsl:import href="share/gen_tree_js.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
	<xsl:variable name="cat_tcr_id" select="/applyeasy/cat_tcr_id"/>	
	<xsl:variable name="cata_cnt" select="count(/applyeasy/catalog_list/catalog)"/>	
	<xsl:variable name="cur_sort_col" select="/applyeasy/catalog_list/pagination/@sort_col"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/applyeasy/catalog_list/pagination/@page_size"/>
	<xsl:variable name="cur_sort_order" select="/applyeasy/catalog_list/pagination/@sort_order"/>
	<xsl:variable name="cur_page" select="/applyeasy/catalog_list/pagination/@cur_page"/>
	<xsl:variable name="total" select="/applyeasy/catalog_list/pagination/@total_rec"/>
	<xsl:variable name="timestamp" select="/applyeasy/catalog_list/pagination/@timestamp"/>
	<xsl:variable name="tnd_cnt_lst" select="/applyeasy/catalog_list/tnd_cnt_lst"/>
	<xsl:variable name="sort_order_by">
		<xsl:choose>
			<xsl:when test="$cur_sort_order = 'ASC' or $cur_sort_order ='asc'">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="tc_enabled" select="/applyeasy/meta/tc_enabled"/>
	<xsl:variable name="is_show_all" select="/applyeasy/catalog_list/show_all"/>
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_cata_lst.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[	
			itm_lst = new wbItem	
				cata_lst = new wbCataLst
			function status(){
					itm_lst.simple_search_item_exec(document.frmSearch,']]><xsl:value-of select="$wb_lang"/><![CDATA[')
					return false;
			}
			
			function show_content(tcr_id) {
				cata_lst.show_content(tcr_id);
			}
			
			function load_tree() {
				if (frmSearch.tc_enabled_ind) {
					page_onload(250);
				}
			}
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_set_cookie('url_prev',self.location.href);load_tree()">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="catalog_list">
			<xsl:with-param name="lab_all_cata">課程目錄管理</xsl:with-param>
			<xsl:with-param name="lab_inst">請選擇所需目錄</xsl:with-param>
			<xsl:with-param name="lab_cata_empty">還沒有任何目錄</xsl:with-param>
			<xsl:with-param name="lab_categ_empty">還沒有任何子目錄</xsl:with-param>
			<xsl:with-param name="lab_edit_cata">修改</xsl:with-param>
			<xsl:with-param name="lab_del_cata">刪除</xsl:with-param>
			<xsl:with-param name="lab_public_cata">共享</xsl:with-param>
			<xsl:with-param name="lab_private_cata">個人</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_status_on">在線</xsl:with-param>
			<xsl:with-param name="lab_status_off">離線</xsl:with-param>
			<xsl:with-param name="lab_recycle">資源回收筒</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">以下列出的是所有可用的培訓目錄。在每個目錄下你可以找到用來區分不同培訓類型的子目錄。你也可以按連結來瀏覽目錄，並查看有關的詳情，或者使用搜尋功能查找指定的培訓。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_cata">新增目錄</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder_cata">排序</xsl:with-param>
			<xsl:with-param name="lab_cata_all">顯示所有目錄</xsl:with-param>
			<xsl:with-param name="lab_cata_del">删除目录</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_child_cata">子目錄數</xsl:with-param>
			<xsl:with-param name="lab_course_count">培訓數</xsl:with-param>
			<xsl:with-param name="lab_child_node">子目錄</xsl:with-param>
			<xsl:with-param name="lab_course">培訓</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">培訓中心</xsl:with-param>
			<xsl:with-param name="lab_on">已發佈</xsl:with-param>
			<xsl:with-param name="lab_off">未發佈</xsl:with-param>
			<xsl:with-param name="lab_cata_by_tcr">按培訓中心顯示</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">當前培訓中心</xsl:with-param>
		   <xsl:with-param name="lab_root_training_center">所有培訓中心</xsl:with-param>
		   <xsl:with-param name="lab_name_no">課程名稱、編號</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="catalog_list">
			<xsl:with-param name="lab_all_cata">课程目录管理</xsl:with-param>
			<xsl:with-param name="lab_inst">请选择所需目录</xsl:with-param>
			<xsl:with-param name="lab_cata_empty">还没有任何目录</xsl:with-param>
			<xsl:with-param name="lab_categ_empty">还没有任何子目录</xsl:with-param>
			<xsl:with-param name="lab_edit_cata">修改</xsl:with-param>
			<xsl:with-param name="lab_del_cata">删除</xsl:with-param>
			<xsl:with-param name="lab_public_cata">共享</xsl:with-param>
			<xsl:with-param name="lab_private_cata">个人</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_status_on">在线</xsl:with-param>
			<xsl:with-param name="lab_status_off">离线</xsl:with-param>
			<xsl:with-param name="lab_recycle">临时目录</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">以下为培训目录列表，可以点击目录名称查看该目录下的子目录及培训信息，也可以使用搜索功能查找培训。</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_cata">添加目录</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder_cata">排序</xsl:with-param>
			<xsl:with-param name="lab_cata_all">显示所有目录</xsl:with-param>
			<xsl:with-param name="lab_cata_del">删除目录</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_child_cata">子目录数</xsl:with-param>
			<xsl:with-param name="lab_course_count">培训数</xsl:with-param>
			<xsl:with-param name="lab_child_node">子目录</xsl:with-param>
			<xsl:with-param name="lab_course">培训</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">培训中心</xsl:with-param>
			<xsl:with-param name="lab_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_cata_by_tcr">按培训中心显示</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">当前培训中心</xsl:with-param>
		   <xsl:with-param name="lab_root_training_center">所有培训中心</xsl:with-param>
		   <xsl:with-param name="lab_name_no">课程名称、编号</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="catalog_list">
			<xsl:with-param name="lab_all_cata">Course catalog management</xsl:with-param>
			<xsl:with-param name="lab_inst">Learning catalogs</xsl:with-param>
			<xsl:with-param name="lab_cata_empty">No learning catalogs are currently available.</xsl:with-param>
			<xsl:with-param name="lab_categ_empty">No learning categorys are currently available.</xsl:with-param>
			<xsl:with-param name="lab_edit_cata">Edit</xsl:with-param>
			<xsl:with-param name="lab_del_cata">Del</xsl:with-param>
			<xsl:with-param name="lab_public_cata">Public</xsl:with-param>
			<xsl:with-param name="lab_private_cata">Private</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_status_on">ON</xsl:with-param>
			<xsl:with-param name="lab_status_off">OFF</xsl:with-param>
			<xsl:with-param name="lab_recycle">Unassigned</xsl:with-param>
			<xsl:with-param name="lab_cata_desc">Listed below are all the available learning catalogs. Under each catalog you will find categories that classify different types of learning solution. Click the links to browse through the catalog and view the details of learning solutions, or use the search function to find specific items.</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_add_cata">Add catalog</xsl:with-param>
			<xsl:with-param name="lab_g_txt_btn_reorder_cata">Reorder</xsl:with-param>
			<xsl:with-param name="lab_cata_all">Show all catalogs</xsl:with-param>
			<xsl:with-param name="lab_cata_del">Remove catalog</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_child_cata">No. of sub-catalogs</xsl:with-param>
			<xsl:with-param name="lab_course_count">No. of courses</xsl:with-param>
			<xsl:with-param name="lab_child_node">Subcategories</xsl:with-param>
			<xsl:with-param name="lab_course">Courses</xsl:with-param>
			<xsl:with-param name="lab_tcr_title">Training center</xsl:with-param>
			<xsl:with-param name="lab_on">Published</xsl:with-param>
			<xsl:with-param name="lab_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_cata_by_tcr">Show by training center</xsl:with-param>
			<xsl:with-param name="lab_cur_tcr">Current training center</xsl:with-param>
		   <xsl:with-param name="lab_root_training_center">All training centers</xsl:with-param>
		   <xsl:with-param name="lab_name_no"></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="catalog_list">
		<xsl:param name="lab_all_cata"/>
		<xsl:param name="lab_inst"/>
		<xsl:param name="lab_edit_cata"/>
		<xsl:param name="lab_del_cata"/>
		<xsl:param name="lab_cata_empty"/>
		<xsl:param name="lab_categ_empty"/>
		<xsl:param name="lab_public_cata"/>
		<xsl:param name="lab_private_cata"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_recycle"/>
		<xsl:param name="lab_cata_desc"/>
		<xsl:param name="lab_g_txt_btn_add_cata"/>
		<xsl:param name="lab_g_txt_btn_reorder_cata"/>
		<xsl:param name="lab_cata_all"/>
		<xsl:param name="lab_cata_del"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_child_cata"/>
		<xsl:param name="lab_course_count"/>
		<xsl:param name="lab_child_node"/>
		<xsl:param name="lab_course"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:param name="lab_cata_by_tcr"/>
		<xsl:param name="lab_cur_tcr"/>
		<xsl:param name="lab_root_training_center"/>
		<xsl:param name="lab_name_no"/>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CAT_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title"><xsl:with-param name="text" select="$lab_all_cata"/></xsl:call-template>
		<xsl:call-template name="wb_ui_desc"></xsl:call-template>
		<!-- search start -->
		<form name="frmSearch" onSubmit="return status()">
			<input type="hidden" name="cmd"/>
			<input type="hidden" name="stylesheet"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" name="tnd_id" value="0"/>
			<input type="hidden" name="exact" value="false"/>
			<input type="hidden" name="page_size" value="10"/>
			<input type="hidden" name="page" value="1"/>
			<input type="hidden" name="all_ind" value="true"/>
			<input type="hidden" name="sortorder" value="asc"/>
			<input type="hidden" name="orderby" value="itm_code"/>
			<input type="hidden" name="training_type" value="ALL"/>
			<input type="hidden" name="itm_life_status_not_equal_lst"/>
			<input type="hidden" name="tvw_id"/>
			<input type="hidden" name="cat_id_lst"/>
			<input type="hidden" name="cat_upd_timestamp_lst"/>
			<input type="hidden" name="tcr_id_lst" value="-1"/>
			<!-- tree nav -->
			<table>
				<tr>
					<td align="right">
						<table>
							<tr>
								<td width="45%">
									<xsl:if test="$tc_enabled='true'">
										<xsl:variable name="title_var">
											<xsl:choose>
												<xsl:when test="$is_show_all"><xsl:value-of select="$lab_root_training_center"/></xsl:when>
												<xsl:otherwise><xsl:value-of select="//cur_training_center/title"/></xsl:otherwise>
											</xsl:choose>
										</xsl:variable>
										<xsl:call-template name="div_tree">
											<xsl:with-param name="lab_cur_tcr" select="$lab_cur_tcr"/>
											<xsl:with-param name="lab_root_training_center" select="$lab_root_training_center"/>
											<xsl:with-param name="title" select="$title_var"/>
										</xsl:call-template>
										<input type="hidden" name="tc_enabled_ind"/>
									</xsl:if>
								</td>
								<td width="55%" align="right">
									<div class="wzb-form-search">
										<input type="text" size="11" name="title_code" class="form-control" style="width:130px;" placeholder="{$lab_name_no}"/>
										 <input type="button" class="form-submit margin-right4" value="" onclick="itm_lst.simple_search_item_exec(document.frmSearch,'{$wb_lang}')"/>
										 <a class="btn wzb-btn-blue margin-right4 vbtm" href="javascript:itm_lst.search_item_prep()">
										<xsl:value-of select="$lab_adv_search"/>
										</a>
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_add_cata"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.add_prep('', '<xsl:value-of select="cur_training_center/@id"/>')</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
									
									
									<xsl:if test="$page_variant/@hasAddCataBtn = 'true' and $tc_enabled = 'true' and $cata_cnt&gt;0 and /applyeasy/catalog_list/show_all='true'">
										<xsl:call-template name="wb_gen_button">
											<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_cata_del"/></xsl:with-param>
											<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.mult_del_exec(document.frmSearch,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
											<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									
									
									</div>
									
								</td>
								<td>
									
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<!-- search end -->
			<!-- <xsl:if test="$page_variant/@hasOrderCataBtn = 'true' or $page_variant/@hasAddCataBtn = 'true'">
			
			<xsl:if test="not(/applyeasy/catalog_list/show_all)">
				<xsl:call-template name="wb_ui_line"/>
			</xsl:if>
			
				<table>
					<tr>
						<td align="right">
							<xsl:if test="$page_variant/@hasOrderCataBtn = 'true'">
								<xsl:call-template name="wb_gen_button"><xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_reorder_cata"/></xsl:with-param><xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.order_cata_lst_prep()</xsl:with-param><xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param></xsl:call-template>
							</xsl:if>
							Access Control
							<xsl:choose>
								<xsl:when test="$page_variant/@hasAddCataBtn = 'true'">
									<xsl:call-template name="wb_gen_button">
										<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_txt_btn_add_cata"/></xsl:with-param>
										<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.add_prep('', '<xsl:value-of select="cur_training_center/@id"/>')</xsl:with-param>
										<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
									</xsl:call-template>
								</xsl:when>
								<xsl:otherwise>
								</xsl:otherwise>
							</xsl:choose>
							显示删除目录按钮
							<xsl:if test="$page_variant/@hasAddCataBtn = 'true' and $tc_enabled = 'true' and $cata_cnt&gt;0 and /applyeasy/catalog_list/show_all='true'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_cata_del"/></xsl:with-param>
									<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.mult_del_exec(document.frmSearch,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
								</xsl:call-template>
							</xsl:if>
						</td>
					</tr>
				</table>
			</xsl:if> -->
			<xsl:choose>
				<xsl:when test="$is_show_all = 'true'">
					<xsl:call-template name="show_all_cata">
						<xsl:with-param name="lab_status"><xsl:value-of select="$lab_status"/></xsl:with-param>
						<xsl:with-param name="lab_title"><xsl:value-of select="$lab_title"/></xsl:with-param>
						<xsl:with-param name="lab_child_cata"><xsl:value-of select="$lab_child_cata"/></xsl:with-param>
						<xsl:with-param name="lab_course_count"><xsl:value-of select="$lab_course_count"/></xsl:with-param>
						<xsl:with-param name="lab_tcr_title"><xsl:value-of select="$lab_tcr_title"/></xsl:with-param>
						<xsl:with-param name="lab_cata_empty"><xsl:value-of select="$lab_cata_empty"/></xsl:with-param>
						<xsl:with-param name="lab_edit_cata" select="$lab_edit_cata"/>
						<xsl:with-param name="lab_on" select="$lab_on"/>
						<xsl:with-param name="lab_off" select="$lab_off"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="wb_ui_space"/>
					<xsl:call-template name="wb_ui_line"/>
					<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}" class="Bg">
						<xsl:choose>
							<xsl:when test="count(catalog)=0">
								<!-- Access Control -->
								<xsl:choose>
									<xsl:when test="$page_variant/@hasCataLostFoundLink = 'true'">
									    <tr>
											<td align="left" height="50">
												<xsl:call-template name="recycle_bin"><xsl:with-param name="lab_recycle"><xsl:value-of select="$lab_recycle"/></xsl:with-param></xsl:call-template>
											</td>
										</tr>
										<xsl:call-template name="wb_ui_show_no_item">
											<xsl:with-param name="text" select="$lab_cata_empty"/>
										</xsl:call-template>
									</xsl:when>
									<xsl:otherwise>
										<xsl:call-template name="wb_ui_show_no_item">
											<xsl:with-param name="text" select="$lab_cata_empty"/>
										</xsl:call-template>
									</xsl:otherwise>
								</xsl:choose>
								<!-- Acess Control -->
							</xsl:when>
							<xsl:otherwise>
								<tr>
									<td height="10">
										<img src="{$wb_img_path}tp.gif" border="0"/>
									</td>
								</tr>
								<tr>
									<td>
										<xsl:comment>
									start===============================================================
									</xsl:comment>
										<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
											<xsl:variable name="row">
												<xsl:choose>
													<xsl:when test="count(catalog) &lt; 4">
														<xsl:value-of select="count(catalog)"/>
													</xsl:when>
													<xsl:when test="count(catalog) mod 3 = 0">
														<xsl:value-of select="count(catalog) div 3"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="floor(count(catalog) div 3) + 1"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:variable>
											<tr>
												<xsl:for-each select="catalog">
													<xsl:if test="position() = 1 or position() mod $row = 1 ">
														<xsl:text disable-output-escaping="yes">&lt;td width="33%" valign="top"&gt;&lt;table border="0" cellpadding="0" cellspacing="0" border="0"&gt;</xsl:text>
													</xsl:if>
													<tr>
														<xsl:call-template name="cata_list">
															<xsl:with-param name="lab_categ_empty"><xsl:value-of select="$lab_categ_empty"/></xsl:with-param>
															<xsl:with-param name="lab_recycle"><xsl:value-of select="$lab_recycle"/></xsl:with-param>
															<xsl:with-param name="lab_child_node" select="$lab_child_node"/>
															<xsl:with-param name="lab_course" select="$lab_course"/>
														</xsl:call-template>
													</tr>
													<xsl:if test="position() mod $row = 0 and position() != last()">
														<xsl:text disable-output-escaping="yes">&lt;/table&gt;&lt;/td&gt;</xsl:text>
													</xsl:if>
													<xsl:if test="position() = last()">
														<xsl:if test="$page_variant/@hasCataLostFoundLink = 'true'">
															<tr>
																<td valign="top">
																	<xsl:call-template name="recycle_bin"><xsl:with-param name="lab_recycle"><xsl:value-of select="$lab_recycle"/></xsl:with-param></xsl:call-template>
																</td>
															</tr>
														</xsl:if>
														<xsl:text disable-output-escaping="yes">&lt;/table&gt;&lt;/td&gt;&lt;td width="1%"&gt;&lt;/td&gt;</xsl:text>
													</xsl:if>
												</xsl:for-each>
											</tr>
										</table>
									</td>
								</tr>
							</xsl:otherwise>
						</xsl:choose>
						<tr class="bg">
							<td height="10">
								<img src="{$wb_img_path}tp.gif" border="0"/>
							</td>
						</tr>
					</table>
					<xsl:call-template name="wb_ui_line"/>
				</xsl:otherwise>
			</xsl:choose>		
			<xsl:call-template name="wb_ui_footer"/>
		</form>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="cata_list">
		<xsl:param name="lab_categ_empty"/>
		<xsl:param name="lab_recycle"/>
		<xsl:param name="lab_child_node"/>
		<xsl:param name="lab_course"/>
		<td valign="top" height="40" style="padding-bottom:28px">
			<table>
				<tr>
					<td width="35" align="left">
						<a href="javascript:wb_utils_node_lst({@tnd_id},'','','','','',{$cat_tcr_id})">
							<img src="{$wb_img_path}ico_big_folder.gif" border="0"/>
						</a>
					</td>
					<td>
						<a href="javascript:wb_utils_node_lst({@tnd_id},'','','','','',{$cat_tcr_id})" class="TitleTextBold color-blue108">
							<xsl:value-of select="title"/>
						</a>
						(<xsl:value-of select="count(child_nodes/child)"/>&#160;<xsl:value-of select="$lab_child_node"/>)
					</td>
				</tr>
				<tr>
					<td width="35">
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
					<td><xsl:variable name="tnd_id_" select="@tnd_id"/>
						<xsl:choose>
							<xsl:when test="$tnd_cnt_lst">
									<xsl:choose>
										<xsl:when test="$tnd_cnt_lst/tnd_cnt[@tnd_id=$tnd_id_]"><xsl:value-of select="$tnd_cnt_lst/tnd_cnt[@tnd_id=$tnd_id_]/@cnt"/></xsl:when>
										<xsl:otherwise>0</xsl:otherwise>
									</xsl:choose>
							</xsl:when>
							<xsl:otherwise><xsl:value-of select="@cur_level_item_count"/></xsl:otherwise>
						</xsl:choose>
						&#160;<xsl:value-of select="$lab_course"/>
					</td>
				</tr>
			</table>
		</td>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="recycle_bin">
		<!-- <xsl:param name="lab_recycle"/>
		<table cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td width="35" align="left">
					<a href="javascript:itm_lst.get_orphan_item()">
						<img src="{$wb_img_path}ico_big_folder_unassign.gif" border="0" align="absmiddle"/>
					</a>
				</td>
				<td>
							<a href="javascript:itm_lst.get_orphan_item()" class="TitleTextBold">
								<xsl:value-of select="$lab_recycle"/>
							</a>
				</td>
			</tr>
		</table> -->
	</xsl:template>
	<!-- 显示全部目录============================================================= -->
	<xsl:template name="show_all_cata">
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_child_cata"/>
		<xsl:param name="lab_course_count"/>
		<xsl:param name="lab_tcr_title"/>
		<xsl:param name="lab_cata_empty"/>
		<xsl:param name="lab_edit_cata"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<table class="table wzb-ui-table">
			<xsl:choose>
				<xsl:when test="count(catalog)=0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="text" select="$lab_cata_empty"/>
					</xsl:call-template>
				</xsl:when>
			<xsl:otherwise>
				<tr class="wzb-ui-table-head">
					<td align="right" width="42">
						<!-- Access Control -->
						<xsl:call-template name="select_all_checkbox">
							<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$cata_cnt"/></xsl:with-param>
							<xsl:with-param name="display_icon">false</xsl:with-param>
							<xsl:with-param name="frm_name">frmSearch</xsl:with-param>
						</xsl:call-template>
					</td>
					<td align="left">
						<xsl:variable name="_order">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'cat_title'">
									<xsl:value-of select="$sort_order_by"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$cur_sort_order"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$cur_sort_col = 'cat_title'">
								<a href="javascript:wb_utils_nav_get_urlparam('sort_col','cat_title','sort_order','{$_order}','cur_page','1')" class="TitleText">
									<xsl:value-of select="$lab_title"/>
									<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
										<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
									</xsl:call-template>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:wb_utils_nav_get_urlparam('sort_col','cat_title','sort_order','asc','cur_page','1')" class="TitleText">
									<xsl:value-of select="$lab_title"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
					
					<xsl:if test="$page_variant/@hasAddCataBtn = 'true'">
						<td align="center">
							<xsl:value-of select="$lab_child_cata"/>
						</td>
						<td align="center">
							<xsl:value-of select="$lab_course_count"/>
						</td>
						<!-- Access Control -->
							<td align="center">
								<xsl:variable name="_order">
									<xsl:choose>
										<xsl:when test="$cur_sort_col = 'cat_status' ">
											<xsl:value-of select="$sort_order_by"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:value-of select="$cur_sort_order"/>
										</xsl:otherwise>
									</xsl:choose>
								</xsl:variable>
								<xsl:choose>
									<xsl:when test="$cur_sort_col = 'cat_status' ">
										<a href="javascript:wb_utils_nav_get_urlparam('sort_col','cat_status','sort_order','{$_order}','cur_page','1')" class="TitleText">
											<xsl:value-of select="$lab_status"/>
											<xsl:call-template name="wb_sortorder_cursor">
												<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
												<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
											</xsl:call-template>
										</a>
									</xsl:when>
									<xsl:otherwise>
										<a href="javascript:wb_utils_nav_get_urlparam('sort_col','cat_status','sort_order','asc','cur_page','1')" class="TitleText">
											<xsl:value-of select="$lab_status"/>
										</a>
									</xsl:otherwise>
								</xsl:choose>
							</td>
					</xsl:if>
					<!--培训中心-->
					<td align="center">
						<xsl:variable name="_order">
							<xsl:choose>
								<xsl:when test="$cur_sort_col = 'tcr_title' ">
									<xsl:value-of select="$sort_order_by"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$cur_sort_order"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:choose>
							<xsl:when test="$cur_sort_col = 'tcr_title' ">
								<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','{$_order}','cur_page','1')" class="TitleText">
									<xsl:value-of select="$lab_tcr_title"/>
									<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:with-param>
										<xsl:with-param name="sort_order"><xsl:value-of select="$cur_sort_order"/></xsl:with-param>
									</xsl:call-template>
								</a>
							</xsl:when>
							<xsl:otherwise>
								<a href="javascript:wb_utils_nav_get_urlparam('sort_col','tcr_title','sort_order','asc','cur_page','1')" class="TitleText">
									<xsl:value-of select="$lab_tcr_title"/>
								</a>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				</xsl:otherwise>
				</xsl:choose>
				<xsl:apply-templates select="catalog">
					<xsl:with-param name="lab_edit_cata" select="$lab_edit_cata"/>
					<xsl:with-param name="lab_on" select="$lab_on"/>
					<xsl:with-param name="lab_off" select="$lab_off"/>
				</xsl:apply-templates>
			</table>
			<!-- Pagination -->
			<xsl:if test=" $cata_cnt&gt;0">
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="timestamp" select="$timestamp"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width"><xsl:value-of select="$wb_gen_table_width"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</xsl:template>
	<!-- ============================================================= -->
	<xsl:template match="catalog">
		<xsl:param name="lab_edit_cata"/>
		<xsl:param name="lab_on"/>
		<xsl:param name="lab_off"/>
		<xsl:variable name="row_class">
			<xsl:choose>
				<xsl:when test="position() mod 2">RowsEven</xsl:when>
				<xsl:otherwise>RowsOdd</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<tr class="{$row_class}">
			<td align="right" width="42">
				<input type="checkbox" name="cat_id" value="{@cat_id}"/>
			</td>
			<td align="left" width="190">
				<a href="javascript:wb_utils_node_lst({@tnd_id},'','','','','',{$cat_tcr_id})" class="Text">
					<xsl:value-of select="title"/>
				</a>
				<xsl:variable name="cat_upd_time_name"><xsl:text>cat_upd_timestamp_</xsl:text><xsl:value-of select="@cat_id"/></xsl:variable>
				<input type="hidden" name="{$cat_upd_time_name}" value="{last_updated/@timestamp}"/>
			</td>
			<xsl:if test="$page_variant/@hasAddCataBtn = 'true'">
				<td align="center">
					<xsl:value-of select="count(child_nodes/child)"/>
				</td>
				<td align="center">
					<xsl:value-of select="@cur_level_item_count"/>
				</td>
				<!-- Access Control -->
				<td align="center">
					<xsl:choose>
						<xsl:when test="@status = 'ON'">
							<xsl:value-of select="$lab_on"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$lab_off"/>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
			<!--显示培训中心-->
			<td align="center" width="190">
				<xsl:value-of select="training_center/title"/>
			</td>
			<xsl:choose>
				<xsl:when test="$page_variant/@hasAddCataBtn = 'true'">
					<td align="right" style="padding-right:0px;">
						<!-- Access Control -->
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_edit_cata"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:cata_lst.edit_prep('<xsl:value-of select="@cat_id"/>')</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
						</xsl:call-template>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<td>
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
					</td>
				</xsl:otherwise>
			</xsl:choose>
			<!-- <td>
				<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
			</td> -->
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

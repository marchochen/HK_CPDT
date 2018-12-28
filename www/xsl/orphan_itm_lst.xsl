<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="tcr_id" select="/applyeasy/cat_tcr_id"/>
	<xsl:variable name="tnd_id" select="/applyeasy/node/nav/node[position() = last()]/@node_id"/>
	<xsl:variable name="order_by" select="/applyeasy/items/@orderby"/>
	<xsl:variable name="sort_order" select="/applyeasy/items/@sortorder"/>
	<xsl:variable name="order">
		<xsl:choose>
			<xsl:when test="$sort_order = 'asc' or $sort_order = 'ASC' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="itm_type_list_root" select="/applyeasy/items/item_type_list"/>
	<xsl:variable name="page_variant" select="/applyeasy/meta/page_variant"/>
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
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			itm_lst = new wbItem
			]]></script>
		</head>
		<BODY leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="itemAction">
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</BODY>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">全目錄</xsl:with-param>
			<xsl:with-param name="lab_categ_empty">沒有任何類別</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">沒有任何課程</xsl:with-param>
			<xsl:with-param name="lab_code">編號</xsl:with-param>
			<xsl:with-param name="lab_title">標題</xsl:with-param>
			<xsl:with-param name="lab_type">類型</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_type_cos">課程</xsl:with-param>
			<xsl:with-param name="lab_type_prog">專業</xsl:with-param>
			<xsl:with-param name="lab_type_wizb">網絡課程</xsl:with-param>
			<xsl:with-param name="lab_type_other">其他</xsl:with-param>
			<xsl:with-param name="lab_status_on">在線</xsl:with-param>
			<xsl:with-param name="lab_status_off">離線</xsl:with-param>
			<xsl:with-param name="lab_orphan_itm_lst">資源回收筒</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">课程目录</xsl:with-param>
			<xsl:with-param name="lab_categ_empty">没有任何类别</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">没有任何课程</xsl:with-param>
			<xsl:with-param name="lab_code">编号</xsl:with-param>
			<xsl:with-param name="lab_title">标题</xsl:with-param>
			<xsl:with-param name="lab_type">类型</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_type_cos">课程</xsl:with-param>
			<xsl:with-param name="lab_type_prog">专业</xsl:with-param>
			<xsl:with-param name="lab_type_wizb">网络课程</xsl:with-param>
			<xsl:with-param name="lab_type_other">其他</xsl:with-param>
			<xsl:with-param name="lab_status_on">已发布</xsl:with-param>
			<xsl:with-param name="lab_status_off">未发布</xsl:with-param>
			<xsl:with-param name="lab_orphan_itm_lst">临时目录</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_all_cata">Learning catalogs</xsl:with-param>
			<xsl:with-param name="lab_categ_empty">There is no category</xsl:with-param>
			<xsl:with-param name="lab_itm_empty">There is no item</xsl:with-param>
			<xsl:with-param name="lab_code">Code</xsl:with-param>
			<xsl:with-param name="lab_title">Title</xsl:with-param>
			<xsl:with-param name="lab_type">Type</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_type_cos">Course</xsl:with-param>
			<xsl:with-param name="lab_type_prog">Program</xsl:with-param>
			<xsl:with-param name="lab_type_wizb">Online course</xsl:with-param>
			<xsl:with-param name="lab_type_other">Other</xsl:with-param>
			<xsl:with-param name="lab_status_on">Published</xsl:with-param>
			<xsl:with-param name="lab_status_off">Unpublished</xsl:with-param>
			<xsl:with-param name="lab_orphan_itm_lst">Unassigned</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_all_cata"/>
		<xsl:param name="lab_categ_empty"/>
		<xsl:param name="lab_itm_empty"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_type_cos"/>
		<xsl:param name="lab_type_prog"/>
		<xsl:param name="lab_type_wizb"/>
		<xsl:param name="lab_type_other"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_orphan_itm_lst"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_CAT_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_orphan_itm_lst"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:wb_utils_cata_lst('',{$tcr_id})" class="NavLink">
					<xsl:value-of select="$lab_all_cata"/>
				</a>
				<xsl:text>&#160;&gt;&#160;</xsl:text>
				<span class="NavLink">
					<xsl:value-of select="$lab_orphan_itm_lst"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:apply-templates select="items">
			<xsl:with-param name="lab_itm_empty" select="$lab_itm_empty"/>
			<xsl:with-param name="lab_code" select="$lab_code"/>
			<xsl:with-param name="lab_title" select="$lab_title"/>
			<xsl:with-param name="lab_type" select="$lab_type"/>
			<xsl:with-param name="lab_status" select="$lab_status"/>
			<xsl:with-param name="lab_type_cos" select="$lab_type_cos"/>
			<xsl:with-param name="lab_type_prog" select="$lab_type_prog"/>
			<xsl:with-param name="lab_type_wizb" select="$lab_type_wizb"/>
			<xsl:with-param name="lab_type_other" select="$lab_type_other"/>
			<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
			<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="items">
		<xsl:param name="lab_itm_empty"/>
		<xsl:param name="lab_code"/>
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_type"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_type_cos"/>
		<xsl:param name="lab_type_prog"/>
		<xsl:param name="lab_type_wizb"/>
		<xsl:param name="lab_type_other"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<!-- <xsl:call-template name="wb_ui_head"/> -->
		<xsl:choose>
			<xsl:when test="count(item) &gt; 0">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:choose>
								<xsl:when test="$order_by = 'itm_title'">
									<a href="javascript:itm_lst.get_orphan_item('itm_title','{$order}')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
										<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path" select="$wb_img_path"/>
										<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm_lst.get_orphan_item('itm_title','asc')" class="TitleText">
										<xsl:value-of select="$lab_title"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>							
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$order_by = 'itm_code'">
									<a href="javascript:itm_lst.get_orphan_item('itm_code','{$order}')" class="TitleText">
										<xsl:value-of select="$lab_code"/>
										<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path" select="$wb_img_path"/>
										<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm_lst.get_orphan_item('itm_code','asc')" class="TitleText">
										<xsl:value-of select="$lab_code"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>						
						</td>
						
						<td align="center">
							<xsl:choose>
								<xsl:when test="$order_by = 'itm_type'">
									<a href="javascript:itm_lst.get_orphan_item('itm_type','{$order}')" class="TitleText">
										<xsl:value-of select="$lab_type"/>
										<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path" select="$wb_img_path"/>
										<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm_lst.get_orphan_item('itm_type','asc')" class="TitleText">
										<xsl:value-of select="$lab_type"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>						
				
						</td>
						<td align="right">
							<xsl:choose>
								<xsl:when test="$order_by = 'itm_status'">
									<a href="javascript:itm_lst.get_orphan_item('itm_status','{$order}')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
										<xsl:call-template name="wb_sortorder_cursor">
										<xsl:with-param name="img_path" select="$wb_img_path"/>
										<xsl:with-param name="sort_order" select="$sort_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm_lst.get_orphan_item('itm_status','asc')" class="TitleText">
										<xsl:value-of select="$lab_status"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>							
						</td>
					</tr>
					<xsl:for-each select="item">
						<tr valign="middle">
							<xsl:call-template name="course_lst">
								<xsl:with-param name="lab_type_prog" select="$lab_type_prog"/>
								<xsl:with-param name="lab_type_cos" select="$lab_type_cos"/>
								<xsl:with-param name="lab_type_wizb" select="$lab_type_wizb"/>
								<xsl:with-param name="lab_type_other" select="$lab_type_other"/>
								<xsl:with-param name="lab_status_on" select="$lab_status_on"/>
								<xsl:with-param name="lab_status_off" select="$lab_status_off"/>
							</xsl:call-template>
						</tr>
					</xsl:for-each>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_itm_empty"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="wb_ui_footer"/>
		<input type="hidden" name="cmd"/>
		<input type="hidden" name="url_success"/>
		<input type="hidden" name="url_failure"/>
		<input type="hidden" name="itm_id_lst"/>
		<input type="hidden" name="itm_upd_timestamp_lst"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="course_lst">
		<xsl:param name="lab_type_cos"/>
		<xsl:param name="lab_type_prog"/>
		<xsl:param name="lab_type_wizb"/>
		<xsl:param name="lab_type_other"/>
		<xsl:param name="lab_status_on"/>
		<xsl:param name="lab_status_off"/>
		<xsl:param name="lab_item_access"/>
		<td width="40%">
			<xsl:choose>
				<xsl:when test="$page_variant/@hasItemDtlLink = 'true'">
					<a href="javascript:itm_lst.get_item_detail({@item_id})" class="Text">
						<xsl:value-of select="title"/>
					</a>
				</xsl:when>
				<xsl:otherwise>
					<a href="javascript:itm_lst.get_item_lrn_detail({@item_id})" class="Text">
						<xsl:value-of select="title"/>
					</a>
				</xsl:otherwise>
			</xsl:choose>
		</td>
		<td width="10%">
			<xsl:value-of select="@item_code"/>
		</td>
		<td align="center" width="35%">
			<xsl:call-template name="get_ity_title">
				<xsl:with-param name="itm_type" select="@dummy_type"/>
			</xsl:call-template>
			<!--<xsl:variable name="my_type" select="@item_type"/>
			<xsl:choose>
				<xsl:when test="$itm_type_list_root/item_type[@id = $my_type]">
					<xsl:call-template name="get_ity_title">
						<xsl:with-param name="itm_type" select="$my_type"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$lab_type_other"/>
				</xsl:otherwise>
			</xsl:choose>-->
		</td>
		<td align="right" width="10%">
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
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<!-- share -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<xsl:variable name="cur_page" select="/item_target/page/@cur_page"/>
	<xsl:variable name="total" select="/item_target/page/@total_search"/>
	<xsl:variable name="page_size" select="/item_target/page/@page_size"/>
	<xsl:variable name="order_by" select="/item_target/page/@orderby"/>
	<xsl:variable name="cur_order" select="/item_target/page/@sortorder"/>
	<xsl:variable name="sort_order">
		<xsl:choose>
			<xsl:when test="$cur_order = 'asc' ">desc</xsl:when>
			<xsl:otherwise>asc</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ================================================================== -->
	<xsl:template match="/item_target">
		<xsl:call-template name="main"/>
	</xsl:template>
	<!--=================================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[
			      itm = new wbItem;
			      
			      function changeMsg(is_change) {
			      	var group_lst = getUrlParam("target_group_lst");
			      	if(is_change || group_lst == '') {
			      		wb_utils_preloading(wb_preview_msg, 'tp.gif');
			      		var select_obj = document.getElementsByTagName("select");
							for(var i = 0; i < select_obj.length; i++){
								select_obj[i].style.visibility="hidden";
						   }
			      	}
			      }
			    ]]></script>
				<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="changeMsg(false)">
				<form name="frmXml">
					<input type="hidden" name="page_size" value="10"/>
					<input type="hidden" name="cur_page" value="1"/>
					<input type="hidden" name="sort_order" value="asc"/>
					<input type="hidden" name="sort_col" value="usr_ste_usr_id"/>
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_target_user">沒有符合規則的學員</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_target_user">没有符合规则的学员</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_no_target_user">No learner satisfies the rule.</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="content">
		<xsl:param name="lab_no_target_user"/>
		<xsl:choose>
			<xsl:when test="not(target_user_lst/user)">
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text_id">preview_msg</xsl:with-param>
					<xsl:with-param name="text" select="$lab_no_target_user"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td>
							<xsl:choose>
								<xsl:when test="$order_by = 'usr_ste_usr_id' ">
									<a href="javascript:itm.sort_target_user('usr_ste_usr_id','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_usr_id"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm.sort_target_user('usr_ste_usr_id','asc')" class="TitleText">
										<xsl:value-of select="$lab_usr_id"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$order_by = 'usr_display_bil' ">
									<a href="javascript:itm.sort_target_user('usr_display_bil','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm.sort_target_user('usr_display_bil','asc')" class="TitleText">
										<xsl:value-of select="$lab_dis_name"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$order_by = 'usg_display_bil' ">
									<a href="javascript:itm.sort_target_user('usg_display_bil','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_group"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm.sort_target_user('usg_display_bil','asc')" class="TitleText">
										<xsl:value-of select="$lab_group"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:choose>
								<xsl:when test="$order_by = 'ugr_display_bil' ">
									<a href="javascript:itm.sort_target_user('ugr_display_bil','{$sort_order}')" class="TitleText">
										<xsl:value-of select="$lab_grade"/>
										<xsl:call-template name="wb_sortorder_cursor">
											<xsl:with-param name="img_path" select="$wb_img_path"/>
											<xsl:with-param name="sort_order" select="$cur_order"/>
										</xsl:call-template>
									</a>
								</xsl:when>
								<xsl:otherwise>
									<a href="javascript:itm.sort_target_user('ugr_display_bil','asc')" class="TitleText">
										<xsl:value-of select="$lab_grade"/>
									</a>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<xsl:for-each select="target_user_lst/user">
						<tr>
							<td>
								<xsl:value-of select="usr_ste_usr_id"/>
							</td>
							<td>
								<xsl:value-of select="usr_display_bil"/>
							</td>
							<td>
								<xsl:value-of select="usg_display_bil"/>
							</td>
							<td>
								<xsl:value-of select="ugr_display_bil"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
					<xsl:with-param name="width" select="$wb_gen_table_width"/>
					<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ============================================================================  -->
</xsl:stylesheet>

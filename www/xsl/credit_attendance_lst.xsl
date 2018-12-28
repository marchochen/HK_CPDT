<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:strip-space elements="*"/>
	<xsl:output indent="no"/>
	<!-- =============================================================== -->
	<xsl:variable name="page_variant" select="/credit/pagination"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="$page_variant/@page_size"/>
	<xsl:variable name="cur_page" select="$page_variant/@cur_page"/>
	<xsl:variable name="total" select="$page_variant/@total_rec"/>
	<xsl:variable name="itm_id" select="/credit/item/@id"/>
	<xsl:variable name="wb_gen_table_width">482</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="credit"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="credit">
		
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" TYPE="text/javascript"><![CDATA[
		
		var attd = new wbAttendance
		cur_page = ]]><xsl:value-of select="$cur_page"/><![CDATA[
		
	]]></script>
		
		<xsl:call-template name="wb_init_lab"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="user_lst">
			<xsl:with-param name="lab_no_attd">沒有學員</xsl:with-param>
			<xsl:with-param name="lab_dis_name">全名</xsl:with-param>
			<xsl:with-param name="lab_usr_id">用戶名</xsl:with-param>
			<xsl:with-param name="lab_status">狀態</xsl:with-param>
			<xsl:with-param name="lab_status_yes">已積分</xsl:with-param>
			<xsl:with-param name="lab_status_no">未積分</xsl:with-param>
			<xsl:with-param name="lab_score">積分</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="user_lst">
			<xsl:with-param name="lab_no_attd">没有学员</xsl:with-param>
			<xsl:with-param name="lab_dis_name">全名</xsl:with-param>
			<xsl:with-param name="lab_usr_id">用户名</xsl:with-param>
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_status_yes">已积分</xsl:with-param>
			<xsl:with-param name="lab_status_no">未积分</xsl:with-param>
			<xsl:with-param name="lab_score">积分</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="user_lst">
			<xsl:with-param name="lab_no_attd">No learners found</xsl:with-param>
			<xsl:with-param name="lab_dis_name">Full name</xsl:with-param>
			<xsl:with-param name="lab_usr_id">User ID</xsl:with-param>
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_status_yes">Credited</xsl:with-param>
			<xsl:with-param name="lab_status_no">None-credited</xsl:with-param>
			<xsl:with-param name="lab_score">Score</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- ========================================================================================= -->
	<xsl:template match="user_lst">
		<xsl:param name="lab_no_attd"/>
		<xsl:param name="lab_dis_name"/>
		<xsl:param name="lab_usr_id"/>
		<xsl:param name="lab_status"/>
		<xsl:param name="lab_status_yes"/>
		<xsl:param name="lab_status_no"/>
		<xsl:param name="lab_score"/>
		<!-- List Header -->
		<xsl:choose>
			<xsl:when test="count(user) &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="10">
							<xsl:call-template name="select_all_checkbox">
								<xsl:with-param name="chkbox_lst_cnt">
									<xsl:value-of select="count(user)"/>
								</xsl:with-param>
								<xsl:with-param name="display_icon">false</xsl:with-param>
								<xsl:with-param name="style">margin-top:6px;</xsl:with-param>
							</xsl:call-template>
						</td>
						<td>
							<xsl:value-of select="$lab_dis_name"/>
						</td>
						<td>
							<xsl:value-of select="$lab_usr_id"/>
						</td>
						<td align="center">
							<xsl:value-of select="$lab_status"/>
						</td>
						<td align="right">
							<xsl:value-of select="$lab_score"/>
						</td>
					</tr>
					<xsl:apply-templates select="user">
						<xsl:with-param name="lab_status_yes" select="$lab_status_yes"/>
						<xsl:with-param name="lab_status_no" select="$lab_status_no"/>
					</xsl:apply-templates>
				</table>
				<!--<xsl:call-template name="wb_ui_pagination">
					<xsl:with-param name="cur_page" select="$cur_page"/>
					<xsl:with-param name="page_size" select="$page_size"/>
					<xsl:with-param name="total" select="$total"/>
				</xsl:call-template>-->
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_attd"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ====================================================================================================== -->
	<xsl:template match="user">
		<xsl:param name="lab_status_yes"/>
		<xsl:param name="lab_status_no"/>
		<tr>
			<td width="10" valign="top">
				<input type="checkbox" style="margin : 6px;" name="app_id" value="{@app_id}"/>
			</td>
			<td>
				<xsl:value-of select="@display_bil"/>
			</td>
			<!-- 3 -->
			<td>
				<xsl:value-of select="@usr_id"/>
				<input type="hidden" name="usr_ent_id" value="{@id}"/>
			</td>
			<td align="center">
				<xsl:choose>
					<xsl:when test="itm_manual_credit"><xsl:value-of select="$lab_status_yes"/></xsl:when>
					<xsl:otherwise><xsl:value-of select="$lab_status_no"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td align="right">
				<xsl:choose>
					<xsl:when test="itm_manual_credit"><xsl:value-of select="itm_manual_credit"/></xsl:when>
					<xsl:otherwise>--</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ======================================================================================-->
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/fm_share.xsl"/>
	
	<xsl:output  indent="yes"/>
	<xsl:strip-space elements="*"/>
	<xsl:variable name="cur_type_id" select="/fm/facility_list/@cur_type_id"/>
	<xsl:variable name="page_variant_root" select="/fm/meta/page_variant"/>
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var isExcludes = getUrlParam('isExcludes');
				fm = new wbFm(isExcludes);
				
				function _refresh(){
					var fac_type= document.frmAction.select_type[document.frmAction.select_type.selectedIndex].value
						fm.get_facility_info_lst(fac_type)
				}
			]]></script>
				<xsl:call-template name="wb_css">
					<xsl:with-param name="view">wb_ui</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="new_css"/>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
				<form name="frmAction">
					<xsl:call-template name="wb_init_lab"/>
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_select_facility_below">選擇下面的設施</xsl:with-param>
			<xsl:with-param name="lab_no_facility">沒有可用設施</xsl:with-param>
			<xsl:with-param name="lab_facility_info">設施訊息</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">設施類型:</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_select_facility_below">选择下面的设施</xsl:with-param>
			<xsl:with-param name="lab_no_facility">没有可用设施</xsl:with-param>
			<xsl:with-param name="lab_facility_info">设施信息</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">设施类型：</xsl:with-param>
			<xsl:with-param name="lab_all">-- 全部 --</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="fm">
			<xsl:with-param name="lab_select_facility_below">Select facilities below</xsl:with-param>
			<xsl:with-param name="lab_no_facility">No facility avaiable</xsl:with-param>
			<xsl:with-param name="lab_facility_info">Facility information</xsl:with-param>
			<xsl:with-param name="lab_select_facility_type">Facility type :</xsl:with-param>
			<xsl:with-param name="lab_all">-- All --</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="fm">
		<xsl:param name="lab_all"/>
		<xsl:param name="lab_facility_info"/>
		<xsl:param name="lab_select_facility_type"/>
		<xsl:param name="lab_select_facility_below"/>
		<xsl:param name="lab_no_facility"/>
		
		<xsl:call-template name="fm_head"/>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_facility_info"/>
		</xsl:call-template>
		
		<table cellpadding="3" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td>
					<span class="TitleText"><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_select_facility_type"/></span>
					<xsl:text>&#160;</xsl:text>
					<span><select class="Select" name="select_type" onchange="_refresh()"><option value=""><xsl:value-of select="$lab_all"/></option><xsl:for-each select="facility_list/facility_type"><option value="{@id}"><xsl:if test="$cur_type_id = @id"><xsl:attribute name="selected">selected</xsl:attribute></xsl:if><xsl:call-template name="fm_display_title"/></option></xsl:for-each></select></span>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_space"/>
		<!-- <xsl:call-template name="wb_ui_line"/> -->
		
		<xsl:apply-templates select="facility_list">
			<xsl:with-param name="lab_select_facility_below"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
			<xsl:with-param name="lab_no_facility"><xsl:value-of select="$lab_no_facility"/></xsl:with-param>
		</xsl:apply-templates>
		<!-- <xsl:call-template name="wb_ui_line"/>  -->
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_list">
		<xsl:param name="lab_select_facility_below"/>
		<xsl:param name="lab_no_facility"/>
		<xsl:apply-templates mode="fm">
			<xsl:with-param name="lab_select_facility_below"><xsl:value-of select="$lab_select_facility_below"/></xsl:with-param>
			<xsl:with-param name="lab_no_facility"><xsl:value-of select="$lab_no_facility"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="facility_type" mode="fm">
		<xsl:param name="lab_no_facility"/>
		<xsl:param name="lab_select_facility_below"/>
		<xsl:param name="col_count">4</xsl:param>
		<xsl:if test="($cur_type_id = '') or ($cur_type_id = @id)">
		<xsl:call-template name="wb_ui_head">
			<xsl:with-param name="text">
				<xsl:call-template name="fm_display_title"/>
			</xsl:with-param>
		</xsl:call-template>                
		<xsl:call-template name="wb_ui_line"/>
			<xsl:choose>
				<xsl:when test="count(facility_subtype) = 0">
					<xsl:call-template name="wb_ui_show_no_item">
						<xsl:with-param name="top_line">false</xsl:with-param>
						<xsl:with-param name="bottom_line">false</xsl:with-param>
						<xsl:with-param name="text" select="$lab_no_facility"/>
						<xsl:with-param name="bg_class">Bg</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>
					<table border="0" cellpadding="0" cellspacing="0" width="{$wb_gen_table_width}" class="Bg">
						<tr>
							<td>
								<xsl:call-template name="wb_ui_desc">
											<xsl:with-param name="text" select="$lab_select_facility_below"/>
								</xsl:call-template>                
							</td>
						</tr>
					</table>
					<table border="0" cellpadding="0" cellspacing="0" width="{$wb_gen_table_width}" class="Bg">	
						<tr>
							<td>
								<xsl:for-each select="facility_subtype">
									<xsl:if test="position() mod  $col_count = 1">
										<xsl:text disable-output-escaping="yes">&lt;table border="0" cellpadding="0" cellspacing="0"&gt;&lt;tr&gt;</xsl:text>
									</xsl:if>
									<td valign="top" align="left">
										<xsl:apply-templates select="." mode="fm">
											<xsl:with-param name="xsl_prefix"><xsl:value-of select="../@xsl_prefix"/></xsl:with-param>
											<xsl:with-param name="hasSelectAll">false</xsl:with-param>
											<xsl:with-param name="frm_name">frmAction</xsl:with-param>
											<xsl:with-param name="hasAddBtn"><xsl:choose><xsl:when test="$page_variant_root/@hasAddFacBtn = 'true'">true</xsl:when><xsl:otherwise>false</xsl:otherwise></xsl:choose></xsl:with-param>
											<xsl:with-param name="col_count"><xsl:value-of select="$col_count"/></xsl:with-param>
											<xsl:with-param name="fac_details_edit">true</xsl:with-param>
										</xsl:apply-templates>
									</td>
									<xsl:if test="(position() mod  $col_count = 0) or (position() = last())">
										<xsl:text disable-output-escaping="yes">&lt;/tr&gt;&lt;/table&gt;</xsl:text>
										<br/>
										<img src="{$wb_img_path}tp.gif" border="0" align="absmiddle"/>
									</xsl:if>
								</xsl:for-each>
							</td>
						</tr>
					</table>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>

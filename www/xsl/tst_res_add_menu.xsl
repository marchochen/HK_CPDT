<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>	
	<xsl:output indent="yes"/>
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="/list/pagination/@page_size"/>
	<xsl:variable name="cur_page" select="/list/pagination/@cur_page"/>
	<xsl:variable name="total" select="/list/pagination/@total_rec"/>
	<xsl:variable name="page_timestamp" select="/list/pagination/@timestamp"/>
	<!-- sorting variable -->
	<xsl:variable name="sort_by" select="/list/pagination/@sort_col"/>
	<xsl:variable name="cur_order" select="/list/pagination/@sort_order"/>
	<xsl:variable name="order_by">
		<xsl:choose>
			<xsl:when test="$cur_order = 'ASC' ">DESC</xsl:when>
			<xsl:otherwise>ASC</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<!-- ==================================================================== -->
	<xsl:variable name="obj_id" select="/list/header/objective/@id"/>
	<xsl:variable name="obj_type" select="/list/header/objective/@type"/>
	<xsl:variable name="_cnt_que">
		<xsl:choose>
			<xsl:when test="/list/header/@privilege='AUTHOR'">
				<xsl:value-of select="count(/list/body/item[@owner=//cur_usr/@id])"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="count(/list/body/item)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="wb_gen_table_width">100%</xsl:variable>
	<!-- ==================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="list/body"/>
		</html>
	</xsl:template>
	<!-- ==================================================================== -->
	<xsl:template match="list/body">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="pragma" content="no-cache"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>			
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
	res = new wbResource;
	obj = new wbObjective;
	
	var module_type = parent.parent.hidden.document.frmXml.mod_subtype.value;

]]></SCRIPT>
		<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" rightmargin="0" bottommargin="0">
			<xsl:call-template name="wb_init_lab"/>
		</body>
	</xsl:template>
	<!-- ==================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_no_res">還沒有任何資源</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_txt_btn_add">添加</xsl:with-param>
			<xsl:with-param name="lab_no_res">还没有任何资源</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_g_txt_btn_add">Add</xsl:with-param>
			<xsl:with-param name="lab_no_res">No resources found.</xsl:with-param>			
		</xsl:call-template>
	</xsl:template>
	<!-- ==================================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_no_res"/>	
		<xsl:param name="lab_g_txt_btn_add"/>
		<form name="frmQue" method="post" target="_parent">
			<input type="hidden" name="cmd" value="ins_mod_que"/>
			<input type="hidden" name="mod_id" value=""/>
			<input type="hidden" name="que_id_lst" value=""/>
			<input type="hidden" name="obj_id" value="{//objective/@id}"/>
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="url_failure" value=""/>
			<xsl:call-template name="wb_ui_line"/>
			<xsl:call-template name="wb_ui_space"/>
			<table width="{$wb_gen_table_width}" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td width="{$wb_gen_table_width}" valign="top">
						<table border="0"  width="{$wb_gen_table_width}">
							<xsl:choose>
								<xsl:when test="/list/header/@privilege='AUTHOR'">
									<xsl:apply-templates select="item[@owner=//cur_usr/@id]">
										<xsl:with-param name="lab_g_txt_btn_add" select="$lab_g_txt_btn_add"/>
									</xsl:apply-templates>
								</xsl:when>
								<xsl:otherwise>
									<xsl:apply-templates>
										<xsl:with-param name="lab_g_txt_btn_add" select="$lab_g_txt_btn_add"/>
									</xsl:apply-templates>
								</xsl:otherwise>
							</xsl:choose>
						</table>
					</td>
				</tr>
			</table>
			<xsl:choose>
				<xsl:when test="/list/header/@privilege='AUTHOR'">
					<xsl:choose>
					<xsl:when test="count(item[@owner=//cur_usr/@id]) = 0">
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
							<xsl:with-param name="wzb-page-style">text-align: left;padding: 17px 0 5px;</xsl:with-param>
						</xsl:call-template>			
					</xsl:otherwise>
					</xsl:choose>					
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
					<xsl:when test="count(item) = 0">
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
							<xsl:with-param name="wzb-page-style">text-align: left;padding: 17px 0 5px;</xsl:with-param>
						</xsl:call-template>			
					</xsl:otherwise>
					</xsl:choose>	
				</xsl:otherwise>
			</xsl:choose>			
		</form>
	</xsl:template>
	<!-- ====================================================================== -->
	<xsl:template match="item">
		<xsl:param name="lab_g_txt_btn_add"/>
		<xsl:if test="text()!=''">
			<tr>
				<td>
					<xsl:choose>
						<xsl:when test="@subtype = 'WCT'">
							<img src="{$wb_img_path}sico_wct.gif" border="0"/>
						</xsl:when>
						<xsl:when test="@subtype = 'SSC'">
							<img src="{$wb_img_path}sico_ssc.gif" border="0"/>
						</xsl:when>
						<xsl:when test="@subtype = 'FAS'">
							<img src="{$wb_img_path}sico_fas.gif" border="0"/>
						</xsl:when>
						<xsl:when test="@subtype = 'DAS'">
							<img src="{$wb_img_path}sico_das.gif" border="0"/>
						</xsl:when>
					</xsl:choose>
				</td>
				<td nowrap="nowrap" class = "wzb-form-label">
		
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_add"/>
						<xsl:with-param name="wb_gen_btn_href"><xsl:choose>
							<xsl:when test="@subtype = 'FAS'">javascript:obj.pick_res_exec(window,<xsl:value-of select="@id"/>,'TST')</xsl:when>
							<xsl:when test="@subtype = 'DAS'">javascript:obj.pick_res_exec(window,<xsl:value-of select="@id"/>,'DXT')</xsl:when>
							<xsl:when test="@subtype = 'RES_SCO'">javascript:obj.pick_res_exec(window,<xsl:value-of select="@id"/>,'SCORM')</xsl:when>
							<xsl:when test="@subtype = 'SSC'">javascript:obj.pick_res_exec(window,<xsl:value-of select="@id"/>,'AICC_AU')</xsl:when>
							<xsl:otherwise>javascript:obj.pick_res_exec(window,<xsl:value-of select="@id"/>,'unknow')</xsl:otherwise>
						</xsl:choose>
						</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_target">_parent</xsl:with-param>
					</xsl:call-template>
				</td>
				<td>
					<a href="javascript:res.read_in_select_res({@id}, '{@subtype}')" class="Text">
						<xsl:value-of select="concat(@id, '. ', text())"/>
					</a>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template match="header"/>
	<xsl:template match="obj_access"/>
</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- others -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_gen_tab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/display_time.xsl"/>
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- others -->
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/usr_gen_tab_share.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="share/label_lrn_soln.xsl"/>
	<xsl:output indent="yes"/>
	
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_upt_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan1')"/> 	
	<xsl:variable name="lab_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_no')"/> 	
	<xsl:variable name="lab_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'wb_imp_tem_title')"/> 
	<xsl:variable name="lab_upd_time" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan2')"/>
	<xsl:variable name="lab_add_upt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/>
	<xsl:variable name="lab_upd_upt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '873')"/>
	<xsl:variable name="lab_del_upt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/>
	<xsl:variable name="lab_search" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '400')"/>
	<xsl:variable name="lab_usr_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_ftn_USR_INFO_MAIN')"/>
	<xsl:variable name="lab_no_upt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lan3')"/>
	<xsl:variable name="lab_position_main" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_position_main')"/>
	<xsl:variable name="lab_position_list" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'position_list')"/>
	
	<!-- =============================================================== -->
	<!-- paginatoin variables -->
	<xsl:variable name="page_size" select="user_postion/upt_list/@page_size"/>
	<xsl:variable name="cur_page" select="user_postion/upt_list/@cur_page"/>
	<xsl:variable name="page_total" select="user_postion/upt_list/@total"/>
	<xsl:variable name="entity_cnt">
		<xsl:value-of select="count(user_postion/upt_list/upt)"/>
	</xsl:variable>
	<!-- =============================================================== -->
	<xsl:template match="/user_postion">
		<xsl:apply-templates select="upt_list"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="upt_list">
		<xsl:param name="lab_desc"/>
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_userposition.js"/>
				<script language="JavaScript" type="text/javascript"><![CDATA[			
				usr = new wbUserGroup;
				upt = new wbUserPosition;
				
			]]></script>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			</head>
			<body marginwidth="0" marginheight="0" topmargin="0" leftmargin="0">
				<form name="frmXml">
					<xsl:call-template name="content"/> 
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<!-- heading -->
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
						<xsl:with-param name="page_title">
				<xsl:call-template name="get_lab">
					<xsl:with-param name="lab_title">global.FTN_AMD_POSITION_MAIN</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_position_main"/>
			</xsl:with-param>
		</xsl:call-template>
		<!--  
		<xsl:call-template name="usr_gen_tab">
			<xsl:with-param name="usr_target_tab">5</xsl:with-param>
		</xsl:call-template>-->
		<table>
			<tr>
				<td width="60%" align="right">
					<div class="wzb-form-search">
						<input type="text" class="form-control" name="upt_search_text"/>
						<input type="button" class="form-submit margin-right4" value="" onclick="upt.search(document.frmXml)"/>
						<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right4</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_add_upt"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:upt.add(document.frmXml)</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_del_upt"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:upt.eff(document.frmXml)</xsl:with-param>
						</xsl:call-template>
					</div>
				</td>
			</tr>
		</table>
		<xsl:choose>
			<xsl:when test="$entity_cnt &gt;= 1">
				<table class="table wzb-ui-table">
					<tr class="wzb-ui-table-head">
						<td width="33%">
							<span>
								<xsl:call-template name="select_all_checkbox">
									<xsl:with-param name="chkbox_lst_cnt"><xsl:value-of select="$entity_cnt"/></xsl:with-param>
									<xsl:with-param name="display_icon">false</xsl:with-param>
									<xsl:with-param name="sel_all_chkbox_nm">sel_all_checkbox</xsl:with-param>
									<xsl:with-param name="frm_name">frmXml</xsl:with-param>
								</xsl:call-template>
							</span>
							<xsl:value-of select="$lab_title"/>
						</td>	
						<td width="33%">
							<xsl:value-of select="$lab_code"/>
						</td>												
											
						<td width="34%" align="center">
							<xsl:value-of select="$lab_upd_time"/>
						</td>
					</tr>
					<xsl:apply-templates select="upt"/>
				</table>
		<xsl:call-template name="wb_ui_pagination">
			<xsl:with-param name="cur_page" select="$cur_page"/>
			<xsl:with-param name="page_size" select="$page_size"/>
			<xsl:with-param name="page_size_name">page_size</xsl:with-param>
			<xsl:with-param name="cur_page_name">cur_page</xsl:with-param>
			<xsl:with-param name="total" select="$page_total"/>
		</xsl:call-template>	
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="wb_ui_show_no_item">
					<xsl:with-param name="text" select="$lab_no_upt"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="upt">
		<tr>
			<td >
				<span>
					<input type="checkbox" name="upt_code_list" value="{@code}"/>
				</span>
				<xsl:value-of select="@title"/>
			</td>
			<td >
				<xsl:value-of select="@code"/>
			</td>			
			
			<td align="center">
				<xsl:call-template name="display_time">
					<xsl:with-param name="my_timestamp">
						<xsl:value-of select="@upd_time"/>
					</xsl:with-param>
					<xsl:with-param name="dis_time">T</xsl:with-param>
				</xsl:call-template>
			</td>
			<td>
				<xsl:call-template name="wb_gen_button">
					<xsl:with-param name="class">btn wzb-btn-blue</xsl:with-param>
					<xsl:with-param name="wb_gen_btn_name" select="$lab_upd_upt"/>
					<xsl:with-param name="wb_gen_btn_href">javascript:upt.modify('<xsl:value-of select="@code"/>')</xsl:with-param>
				</xsl:call-template>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="cur_usr"/>
	<!-- =============================================================== -->
</xsl:stylesheet>

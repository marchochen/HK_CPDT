<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<!-- share -->
	<xsl:import href="share/sys_tab_share.xsl"/>
	
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/>
	<xsl:variable name="itm_type_lst" select="/applyeasy/item/item_type/@id"/>
	
	<xsl:variable name="lab_no" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1148')"/>
	<xsl:variable name="lab_next_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '101')"/>
	<xsl:variable name="lab_ok_btn" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '872')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 	
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
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
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="Javascript"><![CDATA[	
				itm = new wbItem;

				$(document).ready(function(){
				  $("#item_status0id").click(function(){
					$("#next").show();
				  });
				  
				  $("#item_status1id").click(function(){
					$("#next").hide();
				  });
				  $("#item_status2id").click(function(){
					$("#next").hide();
				  });
				  
				});
		
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmResult">
				<input type="hidden" name="module"/>
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="stylesheet"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<input type="hidden" name="itm_upd_timestamp" value="{$itm_updated_timestamp}"/>
				<input type="hidden" name="itm_id" value="{$itm_id}"/>
				<input type="hidden" name="itm_id_lst" value="{$itm_id}"/>
				<input type="hidden" name="itm_status_lst"/>
				<input type="hidden" name="itm_upd_timestamp_lst" value="{$itm_updated_timestamp}"/>
				<input type="hidden" name="show_sys_msg" value="true"/>
	 			<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_tab_publish">在目錄中發佈</xsl:with-param>
			<xsl:with-param name="lab_target_learner">仅目標學員</xsl:with-param>
			<xsl:with-param name="lab_all_learner">所有学员</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_title">发布</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_tab_publish">在目录中发布</xsl:with-param>
			<xsl:with-param name="lab_target_learner">仅目标学员</xsl:with-param>
			<xsl:with-param name="lab_all_learner">所有学员</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
			<xsl:with-param name="lab_title">发布</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_tab_publish">Publish to catalog</xsl:with-param>
			<xsl:with-param name="lab_target_learner">Only Target learner</xsl:with-param>
			<xsl:with-param name="lab_all_learner">All Learner</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
			<xsl:with-param name="lab_title">Publish Course</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_target_learner"/>
		<xsl:param name="lab_tab_publish"/>
		<xsl:param name="lab_all_learner"/>
		<xsl:param name="lab_no"/>
		<xsl:param name="lab_title"/>
		
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_ITM_COS_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				创建课程 - 第五步：设置发布范围
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:itm.get_item_detail({item/@id})" class="NavLink">
					<xsl:value-of select="item/nav/item/title"/>
				</a>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_title"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<table>
			<tr>
				<td valign="top" class="wzb-form-label">
					<xsl:value-of select="$lab_tab_publish"/>
				</td>
				<td class="wzb-form-control">
					<span class="wbFormRightText">
						<label for="item_status0id">
							<input id="item_status0id" type="radio" name="item_status_" checked="checked" value="TARGET_LEARNER" class="wzb-inputText"/>
							<xsl:value-of select="$lab_target_learner"/></label>
							<br/>
						<label for="item_status1id">
							<input id="item_status1id" type="radio" name="item_status_" value="ALL" class="wzb-inputText"/>
							<xsl:value-of select="$lab_all_learner"/></label>
							<br/>
						<label for="item_status2id">
							<input id="item_status2id" type="radio" name="item_status_" value="OFF" class="wzb-inputText"/>
							<xsl:value-of select="$lab_no"/></label>
							<input name="itm_status" type="hidden"/>
					</span>
				</td>
			</tr>
		</table>
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="id">next</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_next_btn"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_LEARNER', 'true');</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="id">ok</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_ok_btn"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.upd_item_target_ref(document.frmResult,'item_status', 'true');</xsl:with-param>
			</xsl:call-template>
		</div>
		<xsl:call-template name="wb_ui_footer"/>
	</xsl:template>
<!-- ============================================================= -->
</xsl:stylesheet>

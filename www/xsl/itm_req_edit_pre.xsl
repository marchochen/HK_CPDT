<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:import href="utils/select_all_checkbox.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/> 
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	
    <xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<!-- cust utils -->
	<!-- ================================================================= -->
	<xsl:output indent="yes"/>
	<xsl:variable name="req_cond" select="/applyeasy/item/requirement/condition"/>
	<xsl:variable name="req_upd_time" select="/applyeasy/item/requirement/last_updated/@timestamp"/>
	<xsl:variable name="lab_requisite" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1155')" />
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<!-- ================================================================ -->
	<xsl:template match="/">
		<xsl:call-template name="item_req"/>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="item_req">
		<html>
			<xsl:call-template name="draw_header"/>
			<xsl:call-template name="draw_body"/>
		</html>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_header">
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<meta http-equiv="Content-Type" content="text/html; charset={$wb_lang_encoding}"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_module.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_itm_req.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript"><![CDATA[
			itm_req = new wbItemReq;
			itm_lst = new wbItem;
			goldenman = new wbGoldenMan;
			itrOrder = getUrlParam('itr_order');
		]]></script>
		</head>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_body">
		<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">
			<form name="frmXml">
				<input type="hidden" name="pre_type" value="ENROLLMENT"/>
				<input type="hidden" name="upd_time" value="{$req_upd_time}"/>
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
	        <xsl:with-param name="lab_desc">請在下面指定一門或者多門課程。</xsl:with-param>
			<xsl:with-param name="lab_ins_title">添加先修條件</xsl:with-param>
			<xsl:with-param name="lab_upd_title">脩改先修條件</xsl:with-param>
			<xsl:with-param name="lab_pre_itm">課程/考試</xsl:with-param>
		    <xsl:with-param name="lab_ok_btn">確認</xsl:with-param>
			<xsl:with-param name="lab_cancel_btn">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
		    <xsl:with-param name="lab_desc">请在下面指定一门或者多门课程。</xsl:with-param>
			<xsl:with-param name="lab_ins_title">添加先修条件</xsl:with-param>
			<xsl:with-param name="lab_upd_title">修改先修条件</xsl:with-param>
			<xsl:with-param name="lab_pre_itm">课程/考试</xsl:with-param>
		    <xsl:with-param name="lab_ok_btn">确认</xsl:with-param>
			<xsl:with-param name="lab_cancel_btn">取消</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
		    <xsl:with-param name="lab_desc">Please specify one or more learning solutions below.</xsl:with-param>
			<xsl:with-param name="lab_ins_title">Add prerequisite</xsl:with-param>
			<xsl:with-param name="lab_upd_title">Edit prerequisite</xsl:with-param>
			<xsl:with-param name="lab_pre_itm">Course/Exam</xsl:with-param> <!-- Learning solution -->
		    <xsl:with-param name="lab_ok_btn">OK</xsl:with-param>
			<xsl:with-param name="lab_cancel_btn">Cancel</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="content">
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_ins_title"/>
		<xsl:param name="lab_upd_title"/>
		<xsl:param name="lab_pre_itm"/>
	    <xsl:param name="lab_ok_btn"/>
		<xsl:param name="lab_cancel_btn"/>
		<xsl:variable name="mode">
			<xsl:choose>
				<xsl:when test="$req_cond">UPDATE</xsl:when>
				<xsl:otherwise>INSERT</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		 <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">105</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
		
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:itm_lst.get_item_detail({$itm_id})" class="NavLink">
					<xsl:value-of select="//itm_action_nav/@itm_title"/>	
				</a>
				<span class="NavLink">&#160;&gt;&#160;<xsl:value-of select="$lab_requisite"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_desc">
			<xsl:with-param name="text">
				<xsl:value-of select="$lab_desc"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="draw_detail_lst">
			<xsl:with-param name="lab_pre_itm" select="$lab_pre_itm"/>
		</xsl:call-template>
		 <div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_ok_btn"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm_req.itm_pre_exec(frmXml,'<xsl:value-of select="$wb_lang"/>',<xsl:value-of select="$itm_id"/>,itrOrder,'<xsl:value-of select="$mode"/>')</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_cancel_btn"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">Javascript:history.back()</xsl:with-param>
				<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
			</xsl:call-template>
		</div>
		</div>
	</xsl:template>
	<!-- ================================================================ -->
	<xsl:template name="draw_detail_lst">
		<xsl:param name="lab_pre_itm"/>
		<table>
		   <tr>
				<td class="wzb-form-label" valign="top">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_pre_itm"/>
					<xsl:text>：</xsl:text>
				</td>
				<td class="wzb-form-control">
					<xsl:call-template name="wb_goldenman">
						<xsl:with-param name="field_name">req_itm_id_lst_fld</xsl:with-param>
						<xsl:with-param name="name">req_itm_id_lst_fld</xsl:with-param>
						<xsl:with-param name="box_size">4</xsl:with-param>
						<xsl:with-param name="tree_type">item</xsl:with-param>
						<xsl:with-param name="select_type">4</xsl:with-param>
						<xsl:with-param name="pick_leave">0</xsl:with-param>
						<xsl:with-param name="complusory_tree">0</xsl:with-param>
						<xsl:with-param name="pick_root">0</xsl:with-param>
						<xsl:with-param name="label_add_btn">
							<xsl:value-of select="$lab_gen_select"/>
						</xsl:with-param>
						<xsl:with-param name="parent_tcr_id" select="/applyeasy/item/@itm_tcr_id"/>
						<xsl:with-param name="option_list">
							<xsl:apply-templates select="/applyeasy/item/requirement/condition/rule_details/item"/>
						</xsl:with-param>
					</xsl:call-template>
					<input type="hidden" name="wb_itm_id" value="{$itm_id}"/>
				</td>
			</tr>
			<tr>
				<td width="35%" align="right">
				</td>
				<td width="65%" align="left" class="wzb-ui-module-text">
					<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template match="item"> 
		<option value="{@id}">
			<xsl:value-of select="@display_bil"/>
		</option>
	</xsl:template>
	
<!-- =============================================================== -->
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	<xsl:output indent="yes"/>
	
	<xsl:variable name="is_new_cos" select="item_target/is_new_cos"/>
	<!-- 目标学员 -->
	<xsl:variable name="label_core_training_management_233" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_233')"/>
	<!-- 添加规则 -->
	<xsl:variable name="label_core_training_management_264" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_264')"/>
	<!-- 修改规则 -->
	<xsl:variable name="label_core_training_management_265" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_core_training_management_265')"/>
	 
	<!-- =============================================================== -->
	<xsl:template match="/item_target">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="main">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
				<script language="JavaScript" src="{$wb_js_path}wb_item.js" type="text/javascript"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<!--alert样式  -->
			    <script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/> 
				
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				
				<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<!--alert样式  end -->
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					itm = new wbItem;
					itm_target_type = getUrlParam("itm_target_type");
					itm_id = getUrlParam("itm_id");
					rule_id = getUrlParam("rule_id");
					is_new_cos = getUrlParam("is_new_cos");
					function showIframe(){
						with (document) {
							wb_standard_width = ]]><xsl:value-of select="$wb_gen_table_width"/><![CDATA[;
							
							document.getElementById('set_target_rule').src = itm.set_target_rule_prev_url(itm_id, itm_target_type, rule_id, 'ae_set_item_target_rule.xsl', is_new_cos);
							document.getElementById('prview_target_rule').src = itm.get_preview_target_url('', '');
							document.getElementById('prview_target_rule').width = wb_standard_width - 376;
						}
					}
					
					function SetCwinHeight(obj) {
							var iframeid;
							if(obj==1){
								 iframeid = document.getElementById("set_target_rule"); //iframe id
							}else{
								 iframeid = document.getElementById("prview_target_rule"); //iframe id
								}
								var iframeHeight2 = Math.min(iframeid.contentWindow.window.document.documentElement.scrollHeight, iframeid.contentWindow.window.document.body.scrollHeight);  
									iframeid.height = "1px";//先给一个够小的初值,然后再长高 
									if (document.getElementById) { 
										if (iframeid && !window.opera) {
											if (iframeid.contentDocument && iframeHeight2) {
										/*
											1872是右边table初始化load的页面高度 && 750是左边Nag初始化load的页面高度 
											初始化时，table内整体设置成为750固定高度
										*/
										//		if(iframeHeight2<751 || iframeHeight2 <1873) iframeHeight2=750;
										 		iframeHeight2=750
												iframeid.height =  iframeHeight2; 
											}
									} 
								} 
				} 
					
					
				]]></SCRIPT>
			</head>
			<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="showIframe()">
				<xsl:call-template name="wb_init_lab"/>
				<!--<iframe name="set_target_rule" style="left:400px;top:150px;position:absolute;" width="200" scrolling="NO" src="#"/>-->
			</body>
			
		</html>
	</xsl:template>
	<!-- ========================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_rule">添加規則</xsl:with-param>
			<xsl:with-param name="lab_edit_rule">修改規則</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_rule">添加规则</xsl:with-param>
			<xsl:with-param name="lab_edit_rule">修改规则</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_add_rule">Add rule</xsl:with-param>
			<xsl:with-param name="lab_edit_rule">Edit rule</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================================  -->
	<xsl:template name="content">
		<xsl:param name="lab_add_rule"/>
		<xsl:param name="lab_edit_rule"/>
		<xsl:variable name="lab_title">
			<xsl:choose>
				<xsl:when test="target_rule/@id and target_rule/@id!=''"><xsl:value-of select="$lab_edit_rule"/></xsl:when>
				<xsl:otherwise><xsl:value-of select="$lab_add_rule"/></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:if test="$is_new_cos = 'false'">
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">106</xsl:with-param>
		</xsl:call-template>
		</xsl:if>
		<div class="wzb-item-main">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/> 
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<a href="javascript:itm.get_item_detail({$itm_id})" class="NavLink">
					<xsl:value-of select="$itm_title"/>
				</a>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
				</span>
				<a href="javascript:itm.get_target_rule({$itm_id}, 'TARGET_LEARNER');" class="NavLink">
					<xsl:value-of select="$label_core_training_management_233"/>
				</a>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:choose>
						<xsl:when test="target_rule/@id and target_rule/@id!=''"><xsl:value-of select="$label_core_training_management_265"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$label_core_training_management_264"/></xsl:otherwise>
					</xsl:choose>
					
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<table cellpadding="0" cellspacing="0" border="0" width="{$wb_gen_table_width}">
			<tr>
				<td valign="top">
					<iframe id="set_target_rule" frameborder="0" width="380" height="600" scrolling="auto" src="../htm/empty.htm" onload = "javascript: SetCwinHeight(1)"/>
				</td>
				<td valign="top">
					<iframe id="prview_target_rule" frameborder="0"  width="604" height="725" scrolling="no" src="../htm/empty.htm" onload = "javascript: SetCwinHeight(1)"/>
				</td>
			</tr>
		</table>
		</div>
	</xsl:template>
</xsl:stylesheet>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="utils/wb_init_lab.xsl"/>
<xsl:import href="utils/wb_css.xsl"/>
<xsl:import href="utils/wb_ui_title.xsl"/>
<xsl:import href="utils/wb_ui_line.xsl"/>
<xsl:import href="cust/wb_cust_const.xsl"/>
<xsl:import href="utils/wb_gen_button.xsl"/>
	
<xsl:output indent="yes"/>
<!-- =============================================================== -->
<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>

<xsl:variable name="ugr_ent_id" select="/tree/item/@identifierref"/>
<xsl:variable name="ugr_display_bil" select="/tree/item/@title"/>
<!--<xsl:variable name="ugr_grade_code" select="/tree/item/@grade_code"/>-->
<xsl:variable name="ugr_grade_code" select="/tree/item/@ugr_code"/>
<xsl:variable name="ugr_tcr_title" select="/tree/item/@ugr_tcr_title"/>
<xsl:variable name="tc_independent" select="/tree/meta/tc_independent"/>

<!-- =============================================================== -->
<xsl:template match="/">
	<xsl:apply-templates select="tree"/>
</xsl:template>
<!-- =============================================================== -->
<xsl:template match="tree">
	<html><xsl:call-template name="wb_init_lab"/></html>
</xsl:template>
<!-- =============================================================== -->
<xsl:template name="lang_ch">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_title">標題</xsl:with-param>
		<xsl:with-param name="lab_tcr">培训中心</xsl:with-param>
		<xsl:with-param name="lab_grade_code">編號</xsl:with-param>
		<xsl:with-param name="lab_edit">修改</xsl:with-param>
		<xsl:with-param name="lab_del">刪除</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_gb">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_title">标题</xsl:with-param>
		<xsl:with-param name="lab_tcr">培训中心</xsl:with-param>
		<xsl:with-param name="lab_grade_code">编号</xsl:with-param>
		<xsl:with-param name="lab_edit">修改</xsl:with-param>
		<xsl:with-param name="lab_del">删除</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template name="lang_en">
	<xsl:call-template name="content">
		<xsl:with-param name="lab_title">Title</xsl:with-param>
		<xsl:with-param name="lab_tcr">Training center</xsl:with-param>
		<xsl:with-param name="lab_grade_code">Code</xsl:with-param>
		<xsl:with-param name="lab_edit">Edit</xsl:with-param>
		<xsl:with-param name="lab_del">Remove</xsl:with-param>
	</xsl:call-template>
</xsl:template>

<!-- =============================================================== -->
<xsl:template name="content">	
	<xsl:param name="lab_title"/>
	<xsl:param name="lab_tcr"/>
	<xsl:param name="lab_grade_code"/>
	<xsl:param name="lab_edit"/>
	<xsl:param name="lab_del"/>
	<xsl:param name="lab_affected_pfs_tip"/>
	<head>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
		<title><xsl:value-of select="$wb_wizbank"/></title>
		<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
		<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
		<script language="JavaScript" src="{$wb_js_path}wb_ugr.js"/>
		<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
		<script language="JavaScript" src="../static/js/cwn_utils.js"/>
		<script type="text/javascript" src="../static/js/jquery.js"/>
		<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
		<script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"/>
		<script language="Javascript" type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_lm_{$wb_cur_lang}.js"/>
		<!--alert样式  -->
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
		
		<script language="JavaScript"><![CDATA[
			ugr = new wbUgr

			function edit(ugr_ent_id) {
				window.location = ugr.ugr_upd_prep_url(ugr_ent_id);
			}
			
			function del(ugr_ent_id) {
			var url =wb_utils_controller_base+"admin/profession/getAffectedPfs";
			$.ajax({
					url : url,
				    data:{id:ugr_ent_id},  
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
	                  if(data.flag){
			Dialog.alert(fetchLabel('label_core_learning_map_126'));
			}else{
				window.location = ugr.ugr_del_url('', ugr_ent_id);
			}
					}
				})
	
		}
		]]></script>
		<!-- CSS -->
		<xsl:call-template name="new_css"/>
	</head>
	<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="wb_utils_init_css(document.frmXml,'text','textarea');">
	<form name="frmXml">
	<input type="hidden" name="cmd"/>
	<input type="hidden" name="url_success"/>
	<input type="hidden" name="url_failure"/>
	<input type="hidden" name="stylesheet"/>
	<input type="hidden" name="ugr_ent_id"/>
	<input type="hidden" name="flag" value="true"/>
		<!--
		<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text"><xsl:value-of select="$ugr_display_bil"/></xsl:with-param>
		</xsl:call-template>
		-->
		<table>
			<tr>
				<td class="wzb-form-label" style="text-align:left;width:70%">
					<xsl:value-of select="$ugr_display_bil"/>
				</td>
				<td class="wzb-form-control" align="right">
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_edit"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:edit(<xsl:value-of select="$ugr_ent_id"/>)</xsl:with-param>
					</xsl:call-template>
					<xsl:call-template name="wb_gen_button">
						<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
						<xsl:with-param name="wb_gen_btn_name" select="$lab_del"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:del(<xsl:value-of select="$ugr_ent_id"/>)</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		<xsl:call-template name="wb_ui_line"/>
		<table>
			<tr>
				<td class="wzb-form-label"><xsl:value-of select="$lab_grade_code"/>：</td>
				<td class="wzb-form-control"><xsl:value-of select="$ugr_grade_code"/></td>
			</tr>
			<tr>
				<td class="wzb-form-label"><xsl:value-of select="$lab_title"/>：</td>
				<td class="wzb-form-control"><xsl:value-of select="$ugr_display_bil"/></td>
			</tr>
			<!-- 
			<xsl:if test="$tc_independent='true'">
			<tr>
				<td class="wzb-form-label"><xsl:value-of select="$lab_tcr"/>：</td>
				<td class="wzb-form-control"><xsl:value-of select="$ugr_tcr_title"/></td>
			</tr>
			</xsl:if>
			 -->
		</table>
		</form>
	</body>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>

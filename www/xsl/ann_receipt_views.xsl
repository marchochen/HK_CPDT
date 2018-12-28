<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" xmlns:java="http://xml.apache.org/xalan/java"
	exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl" />
	<xsl:import href="cust/wb_cust_const.xsl" />
	<xsl:import href="utils/wb_css.xsl" />
	<xsl:import href="utils/wb_gen_form_button.xsl" />
	<xsl:import href="utils/wb_ui_footer.xsl" />
	<xsl:import href="utils/wb_ui_head.xsl" />
	<xsl:import href="utils/wb_ui_line.xsl" />
	<xsl:import href="utils/wb_ui_hdr.xsl" />
	<xsl:import href="utils/wb_ui_title.xsl" />
	<xsl:import href="utils/wb_utils.xsl" />
	<xsl:import href="utils/wb_gen_button.xsl" />
	<xsl:import href="utils/wb_ui_show_no_item.xsl" />
	<xsl:import href="utils/wb_sortorder_cursor.xsl"/>
	<xsl:import href="utils/wb_ui_pagination.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_ui_nav_link.xsl"/>
	<!-- =========================================================================== -->
	<xsl:variable name="table_width">
		<xsl:value-of select="$wb_gen_table_width"/>
	</xsl:variable>
	<xsl:variable name="rec" select="/rec/receipt/rec_list" />
	<xsl:variable name="msg_id" select="/rec/msg/@id"/>
	<xsl:variable name="lab_title" select="/rec/msg/@title"/>
	<xsl:variable name="lab_g_lst_btn_receipt" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_im.label_core_information_management_4')"/>
	<xsl:variable name="lab_g_form_btn_close" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '772')" />
	<xsl:variable name="lab_g_form_btn_back" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '300')" />
	<xsl:variable name="lab_usr_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_user_id')" />
	<xsl:variable name="lab_usg_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_group')" />
	<xsl:variable name="lab_views_date" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1152')" />
	<xsl:variable name="lab_ann_receipt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1153')" />
	<xsl:variable name="label_core_training_management_268" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_tm.label_core_training_management_268')" />
	<!-- =========================================================================== -->
	<xsl:output indent="yes" />
	<xsl:template match="/">
		<html>
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank" />
				</title>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_instructor.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_usergroup.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js" />
				<script language="Javascript" type="text/javascript" src="{$wb_js_path}wb_goldenman.js" />
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_announcement.js"/>
				<script type="text/javascript"><![CDATA[
					announcement = new wbAnnouncement;
					
					usr = new wbUserGroup;
					instr = new wbInstructor;
					goldenman =  new wbGoldenMan
					
					function getPopupUsrLst(fld_name, id_lst, nm_lst, blank, auto_enroll_ind, usr_id_lst) {
						instr.int_ins_upd_prep(0, id_lst);
					}
					$(function(){
    					$(".lastTd1:last").attr('style','border-bottom:0px');  
    					$(".lastTd2:last").attr('style','border-bottom:0px;padding-left:140px');  
    					$(".lastTd3:last").attr('style','border-bottom:0px');  
					}); 
				]]></script>
			</head>
			<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0">
				<form name="frmXml">
					<input type="hidden" name="module" />
					<input type="hidden" name="cmd" />
					<xsl:call-template name="content" />
				</form>
			</body>
		</html>
	</xsl:template>
	<!-- =========================================================================== -->
	<xsl:template name="content">
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module">FTN_AMD_MSG_MAIN</xsl:with-param>
			<xsl:with-param name="parent_code">FTN_AMD_MSG_MAIN</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text" select="$lab_ann_receipt" />
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
				<xsl:with-param name="text">
					<!-- <a href="javascript:wb_utils_nav_go('FTN_AMD_MSG_MAIN');" class="NavLink">
							<xsl:value-of select="$lab_title"/>
					</a> 
					<xsl:text>&#160;&gt;&#160;</xsl:text>-->
					<a href="javascript:announcement.get_ann_dtl({$msg_id});" id="dtl_title" class="NavLink">
						<xsl:value-of select="$lab_title"/>
					</a>
					<span class="NavLink">
						<xsl:text>&#160;&gt;&#160;</xsl:text>
						<xsl:value-of select="$lab_ann_receipt"/>
					</span>
				</xsl:with-param>
			</xsl:call-template>
		<table class="table wzb-ui-table">
			<tr class="wzb-ui-table-head">
				<td width="30%">
					<xsl:value-of select="$lab_usr_name"/>
				</td>
				<td width="35%" align="left" style="padding-left:140px;">
					<xsl:value-of select="$lab_usg_name"/>
				</td>
				<td width="40%"  align="right">
					<xsl:value-of select="$lab_views_date"/>
				</td>
			</tr>
			<xsl:choose>
				<xsl:when test="count($rec)=0">
					<tr>
						<td colspan = "3" style="border-bottom:0px">
							<xsl:call-template name="wb_ui_show_no_item">
								<xsl:with-param name="text" select="$label_core_training_management_268" />
							</xsl:call-template>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$rec">
						<tr>
							<td class="lastTd1">
								<xsl:value-of select="@usr_name"/>
							</td>
							<td class="lastTd2" align="left" style="padding-left:140px;">
								<xsl:choose>
									<xsl:when test="@usg_name != ''">
										<xsl:value-of select="@usg_name"/>
									</xsl:when>
									<xsl:otherwise>--</xsl:otherwise>
							</xsl:choose>
							</td>
							<td align="right" class="lastTd3">
								<xsl:value-of select="@receipt_date"/>
							</td>
						</tr>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</table>
		
		<!-- 
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_back"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
		
		-->
		<xsl:call-template name="wb_ui_footer" />
	</xsl:template>
	</xsl:stylesheet>
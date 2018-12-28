<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_gen_button.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="isTerminate" select="//isTerminate"/>
	<xsl:variable name="isPause" select="//isPause"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '769')"/>
	<xsl:variable name="lab_mark_as_zero" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '763')"/>
	<xsl:variable name="lab_yes" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_yes')"/>
	<xsl:variable name="lab_no" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_no_1')"/>
	<xsl:variable name="lab_input_message" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '773')"/>
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/>
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
	<xsl:variable name="lab_pause_reason" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '787')"/>
	<xsl:variable name="lab_desc_requirement" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '855')"/>
	<xsl:variable name="lab_required" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '335')"/>
	<!-- =============================================================== -->
	<xsl:template match="/exam_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<!--alert样式  -->
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}jquery.js"/>
			
			<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
			<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
			<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
			
			<script language="Javascript" type="text/javascript" src="../../static/js/cwn_utils.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript" />
			<script LANGUAGE="JavaScript" TYPE="text/javascript">
			<![CDATA[
				var wb_item = new wbItem;

                var isColse = getParentUrlParam('isColse');
                if(isColse){
                  window.close();
                }
			]]>
			</script>
			<xsl:call-template name="new_css"/>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml">
			<input type="hidden" name="url_success" value=""/>
			<input type="hidden" name="cmd" value=""/>
			<input type="hidden" name="module" value=""/>
			<input type="hidden" name="itm_id" value="{itm_id}"/>
			<input type="hidden" name="ent_id_lst" value="{ent_id_lst}"/>
			<input type="hidden" name="isTerminate" value=""/>
			<input type="hidden" name="isPause" value=""/>
			<input type="hidden" name="isMarkAsZero" value=""/>
			
			<xsl:call-template name="wb_ui_line">
				<xsl:with-param name="width">500</xsl:with-param>
			</xsl:call-template>
			<table cellpadding="3" cellspacing="0" border="0" width="500" class="Bg">
				<xsl:call-template name="space"/>
				<tr>
					<td width="1">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
					<td width="22%" align="right" valign="top" class="wzb-form-label"><xsl:value-of select="$lab_name"/>：</td>
					<td width="80%" align="left" class="wzb-form-control">
						
							<xsl:for-each select="usr_display_bil">
								<xsl:if test="position() != 1">, </xsl:if>
								<xsl:if test="position() mod 4 = 0"><br/></xsl:if>
								<xsl:value-of select="."/>
							</xsl:for-each>
					
					</td>
					<td width="1">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
				</tr>
				<xsl:if test="$isTerminate = 'true'">
					<tr>
						<td width="1">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
						<td width="22%" align="right" valign="top" class="wzb-form-label"><xsl:value-of select="$lab_mark_as_zero"/>：</td>
						<td width="80%" align="left">
							<input type="radio" name="markAsZero" value="true" id="zero_true">
								<label for="zero_true">
									<span class="Text"><xsl:value-of select="$lab_yes"/></span>
								</label>
							</input>
							<img border="0" height="5" src="{$wb_img_path}tp.gif" width="20"/>
							<input type="radio" name="markAsZero" value="false" id="zero_false" checked="checked">
								<label for="zero_false">
									<span class="Text"><xsl:value-of select="$lab_no"/></span>
								</label>
							</input>
						</td>
						<td width="1">
							<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						</td>
					</tr>
				</xsl:if>
				<tr>
					<td width="1">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
					<td width="22%" align="right" valign="top"  class="wzb-form-label">
					
							<xsl:choose>
								<xsl:when test="$isPause = 'true'">
									<xsl:value-of select="$lab_pause_reason"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="$lab_input_message"/>
								</xsl:otherwise>
							</xsl:choose>：
						
					</td>
					<td width="80%" align="left">
						<textarea rows="4" cols="30" name="msg_body"/>
						<br/>
						(<xsl:value-of select="$lab_desc_requirement"/>)
					</td>
					<td width="1">
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
					</td>
				</tr>
				
				<xsl:call-template name="space"/>
			</table>
		
			<div class="wzb-bar">
						<xsl:variable name="ok_js">
							<xsl:choose>
								<xsl:when test="$isTerminate = 'true'">javascript:wb_item.exam.terminate_exam(document.frmXml, '<xsl:value-of select="$lab_input_message"/>');</xsl:when>
								<xsl:when test="$isPause = 'true'">javascript:wb_item.exam.pause_exam(document.frmXml, '<xsl:value-of select="$lab_pause_reason"/>');</xsl:when>
								<xsl:otherwise>javascript:wb_item.exam.sent_msg_to_exam_learner(document.frmXml, '<xsl:value-of select="$lab_input_message"/>');</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href"><xsl:value-of select="$ok_js"/>;window.opener.refreshSelectId();</xsl:with-param>
						</xsl:call-template>
						<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
						<xsl:call-template name="wb_gen_form_button">
							<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
							<xsl:with-param name="wb_gen_btn_href">javascript:window.close()</xsl:with-param>
						</xsl:call-template>
			</div>
		</form>
	</xsl:template>
	<xsl:template match="cur_usr"/>
	<xsl:template name="space">
		<tr>
			<td height="10" width="1">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
			<td height="10" width="20%">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
			<td width="80%">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
			<td height="10" width="1">
				<img border="0" height="1" src="{$wb_img_path}tp.gif" width="1"/>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

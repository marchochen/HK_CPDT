<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- cust -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_ui_title">
		<xsl:choose>
			<xsl:when test="user_postion/status = 'Add'">
				<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '473')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '873')"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '949')"/>
	</xsl:variable>
	<xsl:variable name="lab_code">
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '949')"/>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_no')"/>
	</xsl:variable>	
	<xsl:variable name="lab_title"> 
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '949')"/>
		<xsl:value-of select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '703')"/>
	</xsl:variable>
	<xsl:variable name="lab_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1108')"/>
	<xsl:variable name="lab_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/>
	<xsl:variable name="lab_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/>
	<xsl:variable name="lab_tcr" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/>
	
	<!-- =============================================================== -->
	<xsl:template match="/user_postion">
		<html>
			<xsl:call-template name="content"/>
		</html>
	</xsl:template>
	
	<!-- =============================================================== -->
	<xsl:template name="content">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_usergroup.js"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" src="{$wb_js_path}wb_userposition.js"/>
			<script language="Javascript" src="{$wb_js_path}wb_goldenman.js"/>
			<script language="JavaScript"><![CDATA[
			usr = new wbUserGroup;
			upt = new wbUserPosition;
			var goldenman = new wbGoldenMan;
		
		]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml" enctype="multipart/form-data" >
				<input type="hidden" name="cmd">
					<xsl:attribute name="value">
						<xsl:choose>
							<xsl:when test="/user_postion/status = 'Add'">insert_position</xsl:when>
							<xsl:otherwise>update_position</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>
				</input>
				<input type="hidden" name="module"/>
				<!--<input type="hidden" name="ent_ste_uid"/>-->
				<input type="hidden" name="ent_id_parent" value=""/>
				<input type="hidden" name="ent_id_root" value=""/>
				<input type="hidden" name="url_success" value=""/>
				<input type="hidden" name="url_failure" value=""/>
				<input type="hidden" name="url_failure1" value=""/>
				<input type="hidden" name="ent_syn_ind" value=""/>
				<input type="hidden" name="usg_budget" value=""/>
				<xsl:call-template name="wb_ui_hdr">
					<xsl:with-param name="belong_module">FTN_AMD_POSITION_MAIN</xsl:with-param>
					<xsl:with-param name="parent_code">FTN_AMD_POSITION_MAIN</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_ui_title"/>
				</xsl:call-template>
				<table>
					<!--<input type="hidden" name="ent_ste_uid" value=""/>-->
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_code"/>：
						</td>
						<td class="wzb-form-control">
							<xsl:choose>
								<xsl:when test="/user_postion/upt/@upt_code != ''">
									<xsl:value-of select="/user_postion/upt/@upt_code"/>
									<input type="hidden" name="upt_code" value="{/user_postion/upt/@upt_code}"/>
								</xsl:when>
								<xsl:otherwise>
									<input style="width:300px" type="text" name="upt_code" size="27" maxlength="255" class="wzb-inputText"/>
								</xsl:otherwise>
							</xsl:choose>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_title"/>：
						</td>
						<td class="wzb-form-control">
							<input style="width:300px" class="wzb-inputText" type="text" name="upt_title" size="27" maxlength="60" value="{/user_postion/upt/@upt_title}"/><br/>
						</td>
					</tr>
					<xsl:if test="/user_postion/tc_independent = 'true'">
						<tr>
							<td class="wzb-form-label" valign="top" >
								<span class="wzb-form-star">*</span><xsl:value-of select="$lab_tcr"/>：
							</td>
							<td class="wzb-form-control">
								<xsl:call-template name="wb_goldenman">
									<xsl:with-param name="field_name">tcr_id</xsl:with-param>
									<xsl:with-param name="name">tcr_id</xsl:with-param>
									<xsl:with-param name="box_size">1</xsl:with-param>
									<xsl:with-param name="tree_type">training_center</xsl:with-param>
									<xsl:with-param name="select_type">2</xsl:with-param>
									<xsl:with-param name="pick_leave">0</xsl:with-param>
									<xsl:with-param name="pick_root">0</xsl:with-param>
									<xsl:with-param name="from_eip">1</xsl:with-param>
									<xsl:with-param name="single_option_value"><xsl:value-of select="/user_postion/upt/@upt_tcr_id"/></xsl:with-param>
									<xsl:with-param name="single_option_text"><xsl:value-of select="/user_postion/upt/@upt_tcr_title"/></xsl:with-param>
								</xsl:call-template>
								<input type="hidden" name="upt_tcr_id"/>
							</td>
						</tr>
					</xsl:if>
					<tr>
						<td class="wzb-form-label" valign="top">
							<xsl:value-of select="$lab_desc"/>：
						</td>
						<td class="wzb-form-control">
							<textarea rows="6" wrap="VIRTUAL" style="width:300px;" cols="30" name="upt_desc" value="{/user_postion/upt/@upt_desc}">
							<xsl:value-of select="/user_postion/upt/@upt_desc"/></textarea>
						</td>
					</tr>
					<tr>
						<td width="35%" align="right">
						</td>
						<td width="65%" align="left" class="wzb-ui-module-text" style="padding: 10px 0 10px 10px;font-size:12px;">
							<span class="wzb-form-star">*</span><xsl:value-of select="$lab_info_required"/>
						</td>
					</tr>
				</table>
				<div class="wzb-bar">
					<xsl:choose>
						<xsl:when test="/user_postion/status = 'Add'">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_ok"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:upt.ins(document.frmXml)</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_ok"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:upt.upd(document.frmXml)</xsl:with-param>
							</xsl:call-template>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:call-template name="wb_gen_form_button">
						<xsl:with-param name="wb_gen_btn_name" select="$lab_cancel"/>
						<xsl:with-param name="wb_gen_btn_href">javascript:upt.search(document.frmXml)</xsl:with-param>
					</xsl:call-template>
				</div>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
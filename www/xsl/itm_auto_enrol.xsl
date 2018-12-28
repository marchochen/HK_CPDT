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
	<!-- share -->
	<xsl:import href="share/sys_tab_share.xsl"/>
	<xsl:import href="share/itm_gen_frm_share.xsl"/>
	<xsl:import href="share/usr_detail_label_share.xsl"/>
	<xsl:import href="share/itm_gen_action_nav_share.xsl"/>
	
	<xsl:variable name="itm_id" select="/applyeasy/item/@id"/>
	<xsl:variable name="itm_updated_timestamp" select="/applyeasy/item/last_updated/@timestamp"/>
	<xsl:variable name="run_ind" select="/applyeasy/item/@run_ind"/>
	<xsl:variable name="session_ind" select="/applyeasy/item/@session_ind"/>
	<xsl:variable name="error_code" select="/applyeasy/meta/error_message/code/text()"/>
	<xsl:variable name="error_msg" select="/applyeasy/meta/error_message/content"/>
	<xsl:variable name="itm_type_lst" select="/applyeasy/item/item_type/@id"/>
	
	<xsl:variable name="lab_select_default_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_select_default_image')"/>
	<xsl:variable name="lab_default_images" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_default_images')"/>
	<xsl:variable name="lab_upload_image" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, 'lab_upload_image')"/>
	<xsl:variable name="lab_button_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_cur_lang, '329')"/>
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
				<xsl:call-template name="wb_init_lab"/>
			</form>
		</body>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_target_learner">目標學員</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">在培訓目錄中發佈</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">自動報名</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_target_learner">目标学员</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">在培训目录中发布</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">自动报名</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_yes">是</xsl:with-param>
			<xsl:with-param name="lab_no">否</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_target_learner">Target learner</xsl:with-param>
			<xsl:with-param name="lab_tab_publish">Publish to catalog</xsl:with-param>
			<xsl:with-param name="lab_tab_autoenrol">Auto enrollment</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_yes">Yes</xsl:with-param>
			<xsl:with-param name="lab_no">No</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<xsl:param name="lab_target_learner"/>
		<xsl:param name="lab_tab_publish"/>
		<xsl:param name="lab_tab_autoenrol"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<xsl:call-template name="wb_ui_hdr">
			<xsl:with-param name="belong_module" select="$belong_module"></xsl:with-param>
			<xsl:with-param name="parent_code" select="$parent_code"/>
		</xsl:call-template>
	    <xsl:call-template name="itm_action_nav">
			<xsl:with-param  name="cur_node_id">106</xsl:with-param>
		</xsl:call-template>
        <div class="wzb-item-main">
		<xsl:call-template name="wb_ui_title">
			<xsl:with-param name="text">
				<xsl:value-of select="//itm_action_nav/@itm_title"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:call-template name="wb_ui_nav_link">
			<xsl:with-param name="text">
				<xsl:choose>
					<xsl:when test="//itm_action_nav/hasTeachingCourse/text()='true'">
						<a href="javascript:itm_lst.get_itm_instr_view({//itm_action_nav/@itm_id})" class="NavLink">
							<xsl:value-of select="//itm_action_nav/@itm_title"/>
						</a>
					</xsl:when>
					<xsl:otherwise>
						<a href="javascript:itm.get_item_detail({item/@id})" class="NavLink">
							<xsl:value-of select="item/nav/item/title"/>
						</a>
					</xsl:otherwise>
				</xsl:choose>
				<span class="NavLink">
					<xsl:text>&#160;&gt;&#160;</xsl:text>
					<xsl:value-of select="$lab_target_learner"/>
				</span>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:variable name="lab_tab_2">
			<xsl:if test="item/@type = 'SELFSTUDY' or item/@type = 'VIDEO' or item/@type='MOBILE'"><xsl:value-of select="$lab_tab_autoenrol"/></xsl:if>
		</xsl:variable>
		<xsl:call-template name="sys_gen_tab">
			<xsl:with-param name="tab_1" select="$lab_target_learner"/>
			<xsl:with-param name="tab_1_href">javascript:itm.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_LEARNER');</xsl:with-param>
			
			<xsl:with-param name="tab_2" select="$lab_tab_2"/>
			<xsl:with-param name="tab_2_href">javascript:itm.get_item_auto_enrol(<xsl:value-of select="$itm_id"/>);</xsl:with-param>
			<xsl:with-param name="current_tab" select="$lab_tab_autoenrol"/>
		</xsl:call-template>
		<table>
			<xsl:apply-templates select="item/valued_template/section/*[name() = 'auto_enroll_target_learners']" mode="field">
				<xsl:with-param name="lab_yes" select="$lab_yes"/>
				<xsl:with-param name="lab_no" select="$lab_no"/>
			</xsl:apply-templates>
		</table>		
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_ok"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.upd_item_target_ref(document.frmResult,'auto_enroll_target_learners');</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name"><xsl:value-of select="$lab_g_form_btn_cancel"/></xsl:with-param>
				<xsl:with-param name="wb_gen_btn_href">javascript:itm.get_target_rule(<xsl:value-of select="$itm_id"/>, 'TARGET_LEARNER');</xsl:with-param>
			</xsl:call-template>
		</div>
		</div>
	</xsl:template>
<!-- ============================================================= -->
	<xsl:template match="*" mode="field">
		<xsl:param name="lab_yes"/>
		<xsl:param name="lab_no"/>
		<tr>
			<td class="wzb-form-label" valign="top">
					<xsl:variable name="field_desc">
						<xsl:call-template name="get_desc"/>
					</xsl:variable>
					<xsl:if test="$field_desc != ''">
						<xsl:choose>
							<xsl:when test="@paramname = $error_code">
								<font color="red">
									<xsl:value-of select="$field_desc"/>
									<xsl:text>：</xsl:text>
								</font>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="$field_desc"/>
								<xsl:text>：</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:if>
			</td>
			<td class="wzb-form-control">
				<xsl:if test="prefix">
					<xsl:call-template name="prefix"/>
				</xsl:if>
				<xsl:apply-templates select="." mode="field_type">
					<xsl:with-param name="text_class">wbFormRightText</xsl:with-param>
					<xsl:with-param name="lab_yes" select="$lab_yes"/>
					<xsl:with-param name="lab_no" select="$lab_no"/>
				</xsl:apply-templates>
				<xsl:if test="suffix">
					<xsl:call-template name="suffix"/>
				</xsl:if>
				<table>
					<xsl:for-each select="subfield_list/*">
						<tr>
							<xsl:if test="title">
								<td valign="top">
									<xsl:value-of select="title/desc[@lan = $wb_lang_encoding]/@name"/>
									<xsl:text>：</xsl:text>
								</td>
							</xsl:if>
							<td valign="top">
								<xsl:apply-templates select="." mode="field_type">
									<xsl:with-param name="text_class">wbFormRightText</xsl:with-param>
									<xsl:with-param name="lab_yes" select="$lab_yes"/>
									<xsl:with-param name="lab_no" select="$lab_no"/>
								</xsl:apply-templates>
								<xsl:if test="suffix">
									<xsl:call-template name="suffix"/>
								</xsl:if>
								<xsl:if test="../../@arrange = 'vertical'">
									<xsl:if test="position() != last()">
									</xsl:if>
								</xsl:if>
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</td>
		</tr>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_space.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_goldenman.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="share/label_role.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:template match="/eip_module">
		<html>
			<xsl:call-template name="main"/>
		</html>
	</xsl:template>
	<!-- =========================== Label =========================== -->
	<xsl:variable name="lab_eip_mgt" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN001')"/> 	
	<xsl:variable name="lab_eip_add_botton" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN002')"/> 	
	<xsl:variable name="lab_eip_code" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN003')"/> 	
	<xsl:variable name="lab_eip_name" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN004')"/>
	<xsl:variable name="lab_ta_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN331')"/>  	
	<xsl:variable name="lab_eip_account_num" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN005')"/> 	
	<xsl:variable name="lab_eip_account_used" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN006')"/> 	
	<xsl:variable name="lab_eip_account_left" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN007')"/> 	
	<xsl:variable name="lab_eip_domain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN008')"/> 	
	<!-- <xsl:variable name="lab_eip_login_bg" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN009')"/> -->
	<xsl:variable name="lab_eip_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN010')"/>  
	<xsl:variable name="lab_upd_style" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '880')"/>  	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 
	<xsl:variable name="lab_view_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '504')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 
	<xsl:variable name="lab_eip_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '704')"/> 		
	<xsl:variable name="lab_eip_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_eip_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 
	<xsl:variable name="lab_eip_update_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '705')"/> 	
	<xsl:variable name="lab_eip_update_timestamp" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '706')"/> 
	<xsl:variable name="lab_tcr_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/>
	<xsl:variable name="lab_basic_info" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN014')"/>
	<xsl:variable name="lab_g_txt_btn_set_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN015')"/>
	<xsl:variable name="lab_g_txt_btn_set_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN016')"/>
	<xsl:variable name="lab_eip_empty_data" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_empty_data')"/>
	<xsl:variable name="lab_eip_peak" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1205')"/> 
	<!-- ============================================================= -->
	<xsl:variable name="tc_enabled" select="/eip_module/meta/tc_enabled"/>
	<xsl:variable name="eip_id" select="/eip_module/eip/@id"/>
	<xsl:variable name="eip_update_timestamp" select="/eip_module/eip/@update_timestamp"/>
	<xsl:variable name="admin_role" select="/eip_module/training_center/role_list"/>
	<xsl:variable name="tc" select="/eip_module/training_center"/>
	<xsl:variable name="hasUpdPrivilege" select="/eip_module/hasUpdPrivilege/text()"/>
	<xsl:variable name="site_name" select="//eip/@id"/>
    <!--<xsl:variable name="media_file_path" select="concat('../eip/', $site_name , '/', //eip/@login_bg )"/> -->
	<xsl:variable name="account_used" select="/eip_module/eip/@account_used"/>

	<!-- ============================================================= -->
	<xsl:template name="main">
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<TITLE>
				<xsl:value-of select="$wb_wizbank"/>
			</TITLE>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_eip.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_tc_mgt.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var eip = new wbEIP;
				ct_mgt = new wbTcMgt;
				
				var defaultLoginBg = '../wb_image/login_bg.png';
				var eipLoginBg = ']]><xsl:value-of select="//eip/@login_bg"/><![CDATA[';
				var curLoginBg = '';
				if(eipLoginBg === '') {
					curLoginBg = defaultLoginBg;
				}else{
					curLoginBg = '../eip/]]><xsl:value-of select="$eip_id"/><![CDATA[/'+eipLoginBg;
				}

				    
				function initLoginBgDemo() {	
					setDemoLoginBg(document.getElementById('login_bg'), curLoginBg);
				}
				
				function setDemoLoginBg(obj, src) {
					obj.src = src;
					obj.width = '400';
					obj.heigth = '100';
					document.getElementById('login_bg_url').href = src;
				}
			]]></script> 
		</head>
		<!--  
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0" onload="initLoginBgDemo()">
			<xsl:call-template name="content"/>
		</body>-->
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<xsl:call-template name="content"/>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml">
			<input name="module" type="hidden"/>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<input type="hidden" id="account_used" name="account_used" value="{$account_used}"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_EIP_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_EIP_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:value-of select="eip/@name"/>
				</xsl:with-param>
			</xsl:call-template>
					
			<xsl:call-template name="wb_ui_head">
				<xsl:with-param name="text">
					<xsl:value-of select="$lab_basic_info"/>
				</xsl:with-param>
				<xsl:with-param name="extra_td">
					<xsl:if test="$hasUpdPrivilege = 'true'">
					<td  align="right">
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_update_button"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:eip.ins_upd_eip_prep(<xsl:value-of select="$eip_id"/>)</xsl:with-param>
						</xsl:call-template>
						<xsl:choose>
							<xsl:when test="eip/@status = 'ON'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_set_status_off"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:eip.set_status('<xsl:value-of select="$eip_id"/>', 'OFF', '<xsl:value-of select="$eip_update_timestamp"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
							<xsl:when test="eip/@status = 'OFF'">
								<xsl:call-template name="wb_gen_button">
									<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
									<xsl:with-param name="wb_gen_btn_name" select="$lab_g_txt_btn_set_status_on"/>
									<xsl:with-param name="wb_gen_btn_href">javascript:eip.set_status('<xsl:value-of select="$eip_id"/>', 'ON', '<xsl:value-of select="$eip_update_timestamp"/>')</xsl:with-param>
								</xsl:call-template>
							</xsl:when>
						</xsl:choose>
						<!-- 屏蔽企业管理中修改页面风格功能 -->
						<!-- 
						<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="wb_gen_btn_name" select="$lab_upd_style"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:ct_mgt.set_tc_style_prep(<xsl:value-of select="eip/@tcr_id"/>, 'zh-cn', <xsl:value-of select="$eip_id"/>)</xsl:with-param>
						</xsl:call-template> 
						-->
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange margin-right10</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_eip_empty_data"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:eip.empty_data(<xsl:value-of select="$eip_id"/>, <xsl:value-of select="eip/@tcr_id"/>)</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="wb_gen_button">
							<xsl:with-param name="class">btn wzb-btn-orange</xsl:with-param>
							<xsl:with-param name="wb_gen_btn_name" select="$lab_del_button"/>
							<xsl:with-param name="wb_gen_btn_href">javascript:eip.del_eip(<xsl:value-of select="$eip_id"/>, '<xsl:value-of select="$eip_update_timestamp"/>')</xsl:with-param>
						</xsl:call-template>
					</td>
					</xsl:if>
				</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_line"/>
			
			<xsl:apply-templates select="eip"/>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="eip">
		<table class="wzb-ui-detail">
			<!--编号-->
			<tr>
				<td class="wzb-ui-detail-label">
					<xsl:value-of select="$lab_eip_code"/>：
				</td>
				<td class="wzb-ui-detail-text">
					<xsl:value-of select="@code"/>
				</td>
			</tr>
			<!--名称-->
			<tr>
				<td class="wzb-ui-detail-label">
					<xsl:value-of select="$lab_eip_name"/>：
				</td>
				<td class="wzb-ui-detail-text">
					<xsl:value-of select="@name"/>
				</td>
			</tr>
			<!--培训中心-->
			<xsl:if test="$tc_enabled='true'">
				<tr>
					<td class="wzb-ui-detail-label">
						<xsl:value-of select="$lab_tcr_title"/>：
					</td>
					<td class="wzb-ui-detail-text">
						<xsl:value-of select="@tcr_title"/>
					</td>
				</tr>
				<xsl:if test="count($admin_role/role)>0">
					<xsl:apply-templates select="$admin_role/role"/>
				</xsl:if>
			</xsl:if>
			<!--状态-->
			<tr>
				<td class="wzb-ui-detail-label">
					<xsl:value-of select="$lab_eip_status"/>：
				</td>
				<td class="wzb-ui-detail-text">
					<xsl:choose>
						<xsl:when test="@status='ON'"><xsl:value-of select="$lab_eip_status_on"/></xsl:when>
						<xsl:otherwise><xsl:value-of select="$lab_eip_status_off"/></xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
			<!--租用帐户数-->
			<tr>
				<td class="wzb-ui-detail-label">
					<xsl:value-of select="$lab_eip_account_num"/>(<xsl:value-of select="$lab_eip_account_used"/>/<xsl:value-of select="$lab_eip_account_left"/>)：
				</td>
				<td class="wzb-ui-detail-text">
					<xsl:value-of select="@account_num"/>(<xsl:value-of select="@account_used"/>/<xsl:value-of select="@account_leaving"/>)
				</td>
			</tr>
			<!-- 在线人数 -->
			<tr>
				<td class="wzb-ui-detail-label">
					<xsl:value-of select="$lab_eip_peak"/>：
				</td>
				<td class="wzb-ui-detail-text">
					<xsl:value-of select="@peak_count"/>/<xsl:value-of select="@eip_max_peak_count"/>
				</td>
			</tr>
			<!--域名-->
			<tr>
				<td class="wzb-ui-detail-label">
					<xsl:value-of select="$lab_eip_domain"/>：
				</td>
				<td class="wzb-ui-detail-text">
					<xsl:choose>
						<xsl:when test="@domain != '' and @domain != 'null'"><xsl:value-of select="@domain"/></xsl:when>
						<xsl:otherwise>--</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
		</table>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="role">
		<xsl:variable name="id" select="@id" />
		<xsl:variable name="name" select="@name" />
		<tr>
			<td class="wzb-ui-detail-label">
				<xsl:value-of select="$name" />
				<xsl:text>：</xsl:text>
			</td>
			<td class="wzb-ui-detail-text">
				<xsl:choose>
					<xsl:when test="$tc/officer_list/role[@id = $id]/entity">
						<xsl:apply-templates select="$tc/officer_list/role[@id = $id]/entity" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>--</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</td>
		</tr>
	</xsl:template>
	<!-- ============================================================================ -->
	<xsl:template match="entity">
		<xsl:choose>
			<xsl:when test="position()!=last()">
				<xsl:value-of select="text()" />
				<xsl:text>, </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="text()" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>

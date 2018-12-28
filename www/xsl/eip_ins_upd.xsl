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
	<xsl:variable name="lab_eip_account_num" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN005')"/> 	
	<xsl:variable name="lab_eip_account_used" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN006')"/> 	
	<xsl:variable name="lab_eip_account_left" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN007')"/> 	
	<xsl:variable name="lab_eip_domain" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN008')"/> 	
	<!--<xsl:variable name="lab_eip_login_bg" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN009')"/>-->
	<xsl:variable name="lab_eip_desc" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN010')"/>  
	<xsl:variable name="lab_no_eip" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN011')"/>  	
	<xsl:variable name="lab_del_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '257')"/> 
	<xsl:variable name="lab_view_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '504')"/> 	
	<xsl:variable name="lab_update_button" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '708')"/> 
	<xsl:variable name="lab_eip_status" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '704')"/> 		
	<xsl:variable name="lab_eip_status_on" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '765')"/> 	
	<xsl:variable name="lab_eip_status_off" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '766')"/> 
	<xsl:variable name="lab_eip_update_user" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '705')"/> 	
	<xsl:variable name="lab_eip_update_timestamp" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '706')"/> 
	<xsl:variable name="lab_tcr_title" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'lab_tc')"/> 
	<xsl:variable name="lab_info_required" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '335')"/> 	
	<xsl:variable name="lab_g_form_btn_ok" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '329')"/> 	
	<xsl:variable name="lab_g_form_btn_cancel" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '330')"/> 
	<xsl:variable name="lab_894" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '894')"/><!-- Keep Media File -->
	<xsl:variable name="lab_wizbank_default" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN012')"/><!-- use wizbank default -->
	<xsl:variable name="lab_eip_bg_size" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, 'LN013')"/>
	<xsl:variable name="lab_max_peak_count" select="java:com.cw.wizbank.util.LangLabel.getValue($wb_lang_encoding, '1206')"/><!-- suggested to be 1003*573 pixels, within 2MB in size -->
	<xsl:variable name="label_core_live_management_44" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_lvm.label_core_live_management_44')"/>	
	<xsl:variable name="label_core_live_management_51" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_lvm.label_core_live_management_51')"/>
	<xsl:variable name="label_core_live_management_52" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_lvm.label_core_live_management_52')"/>
	<xsl:variable name="label_core_live_management_53" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_lvm.label_core_live_management_53')"/>
	<xsl:variable name="label_core_live_management_95" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_lvm.label_core_live_management_95')"/>
	<xsl:variable name="label_core_live_management_96" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_lvm.label_core_live_management_96')"/>
	<xsl:variable name="label_core_live_management_99" select="java:com.cwn.wizbank.utils.LabelContent.get($wb_cur_lang, 'label_lvm.label_core_live_management_99')"/>
	<!-- ============================================================= -->
	<xsl:variable name="ins_or_upd">
		<xsl:choose>
			<xsl:when test="/eip_module/eip/@id &gt;= 1">UPD</xsl:when>
			<xsl:otherwise>INS</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="tc_enabled" select="/eip_module/meta/tc_enabled"/>
	<xsl:variable name="site_name" select="//eip/@id"/>
	<!--  <xsl:variable name="media_file_path" select="concat('../eip/', $site_name , '/', //eip/@login_bg )"/>-->
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
			<script language="Javascript" type="text/javascript" SRC="{$wb_js_path}wb_goldenman.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/label_ss_{$wb_cur_lang}.js"/>
			
			<script LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				var eip = new wbEIP;
				var goldenman = new wbGoldenMan;
				
				 function changeRadioChk(obj, fileObj) {
						if(obj != null) {
							if(obj.value == '0'){
								fileObj.disabled = true;
							} else if(obj.value == '1') {
								fileObj.disabled = true;
							} else if(obj.value == '2') {
								fileObj.disabled = false;
							}
						}
				    }
				    
				var defaultLoginBg = '../wb_image/login_bg.png';
				var eipLoginBg = ']]><xsl:value-of select="//eip/@login_bg"/><![CDATA[';
				var curLoginBg = '';
				if(eipLoginBg === '') {
					curLoginBg = defaultLoginBg;
				}else{
					curLoginBg = '../eip/]]><xsl:value-of select="//eip/@id"/><![CDATA[/'+eipLoginBg;
				}

			    function changeLoginBg(obj) {
						var loginBgObj = document.getElementById('login_bg');

						if(obj != null) {
							if(obj.name === 'login_bg_radio') {
								var loginBgValue = '';
								if(obj.value === '0') {
									loginBgValue = curLoginBg;
								} else if(obj.value === '1') {
									loginBgValue = defaultLoginBg;
								} else if(obj.value === '2') {
									loginBgValue = frmXml.login_bg_file.value;
								}
								setDemoLoginBg(loginBgObj, loginBgValue);
							}
						}
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
		<body>
			<div class="">
			<xsl:call-template name="content"/>
			</div>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<form name="frmXml" enctype="multipart/form-data" class="wzb-form">
			<input name="module" type="hidden"/>
			<input name="cmd" type="hidden">
				<xsl:attribute name="value">
					<xsl:choose>
						<xsl:when test="$ins_or_upd = 'INS'">ins_eip_exec</xsl:when>
						<xsl:otherwise>upd_eip_exec</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</input>
			<input type="hidden" name="url_success"/>
			<input type="hidden" name="url_failure"/>
			<!--  <input type="hidden" name="login_bg_type"/>-->
			<input type="hidden" name="eip_id" value="{eip/@id}"/>
			<input name="eip_update_timestamp" type="hidden" value="{eip/@update_timestamp}"/>
			<xsl:call-template name="wb_ui_hdr">
				<xsl:with-param name="belong_module">FTN_AMD_EIP_MAIN</xsl:with-param>
				<xsl:with-param name="parent_code">FTN_AMD_EIP_MAIN</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_ui_title">
				<xsl:with-param name="text">
					<xsl:choose>
						<xsl:when test="$ins_or_upd='INS'">
							<xsl:value-of select="$lab_eip_add_botton"/>
						</xsl:when>
						<xsl:when test="$ins_or_upd='UPD'">
							<xsl:value-of select="eip/@name"/>
						</xsl:when>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>
			
			<xsl:apply-templates select="eip"/>
		</form>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="eip">
		<table>
			<!--编号-->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_eip_code"/>：
					<input type="hidden" name="lab_eip_code" value="{$lab_eip_code}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:100px;" maxlength="50" name="eip_code" value="{@code}" class="wzb-inputText"/>
				</td>
			</tr>
			<!--名称-->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span>
						<xsl:value-of select="$lab_eip_name"/>：
						<input type="hidden" name="lab_eip_name" value="{$lab_eip_name}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" maxlength="255" name="eip_name" value="{@name}" class="wzb-inputText"/>
				</td>
			</tr>
			<!--培训中心-->
			<xsl:if test="$tc_enabled='true'">
				<tr>
					<td class="wzb-form-label">
						<span class="wzb-form-star">*</span>
						<xsl:value-of select="$lab_tcr_title"/>：
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
							<xsl:with-param name="parent_tcr_id">1</xsl:with-param>
							<xsl:with-param name="from_eip">1</xsl:with-param>
							<xsl:with-param name="single_option_value"><xsl:value-of select="@tcr_id"/></xsl:with-param>
							<xsl:with-param name="single_option_text"><xsl:value-of select="@tcr_title"/></xsl:with-param>
						</xsl:call-template>
					</td>
					<input type="hidden" name="eip_tcr_id"/>
				</tr>
			</xsl:if>
			<!--状态-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_eip_status"/>：
				</td>
				<td class="wzb-form-control">
					<select name="eip_status" class="Select">
						<option value="ON">
							<xsl:if test="@status='ON'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_eip_status_on"/>
						</option>
						<option value="OFF">
							<xsl:if test="@status='OFF'">
								<xsl:attribute name="selected">selected</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="$lab_eip_status_off"/>
						</option>
					</select>
				</td>
			</tr>
			<!--租用帐户数-->
			<tr>
				<td class="wzb-form-label">
					<span class="wzb-form-star">*</span>
					<xsl:value-of select="$lab_eip_account_num"/>：
					<input type="hidden" name="lab_eip_account_num" value="{$lab_eip_account_num}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:100px;" maxlength="50" name="eip_account_num" value="{@account_num}" class="wzb-inputText"/>
				</td>
			</tr>
			<!-- 允许最大在线人数  lab_max_peak_count-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_max_peak_count"/>：
					<input type="hidden" name="lab_max_peak_count" value="{$lab_max_peak_count}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:100px;" maxlength="50" name="eip_max_peak_count" value="{@eip_max_peak_count}" class="InputFrm"/>
				</td>
			</tr>
			<!--域名-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$lab_eip_domain"/>：
					<input type="hidden" name="lab_eip_domain" value="{$lab_eip_domain}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" maxlength="50" name="eip_domain" value="{@domain}" class="wzb-inputText"/>
				</td>
			</tr>
			<!-- 直播模式-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_live_management_51"/>：
					<input type="hidden" name="lab_live_mode" value="{$label_core_live_management_51}"></input>
					<input type="hidden" name="eip_live_mode"  value="" />
				</td>
				<td class="wzb-form-control">
					<span>
						<input type="checkbox" name="eip_live_mode_checkbox"  value="QCLOUD" >
							<xsl:for-each select="../eip_live_mode/eip_live_mode_option">
								<xsl:if test="text() = 'QCLOUD'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:for-each>
						</input>
						<xsl:value-of select="$label_core_live_management_52"/>
					</span>
					<span style="margin-left:10px;">
						<input type="checkbox" name="eip_live_mode_checkbox" value="VHALL" >
							<xsl:for-each select="../eip_live_mode/eip_live_mode_option">
								<xsl:if test="text() = 'VHALL'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:for-each>
						</input>
						<xsl:value-of select="$label_core_live_management_53"/>
					</span>
					<span style="margin-left:10px;">
						<input type="checkbox" name="eip_live_mode_checkbox" value="GENSEE" >
							<xsl:for-each select="../eip_live_mode/eip_live_mode_option">
								<xsl:if test="text() = 'GENSEE'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</xsl:for-each>
						</input>
						<xsl:value-of select="$label_core_live_management_99"/>
					</span>
				</td>
			</tr>
			<!-- 简易直播模式账号-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_live_management_95"/>：
					<input type="hidden" name="lab_live_qcloud_secretid" value="{$label_core_live_management_95}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:300px;" name="eip_live_qcloud_secretid" value="{@eip_live_qcloud_secretid}" class="wzb-inputText"/>
				</td>
			</tr>
			<!-- 简易直播模式密码-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_live_management_96"/>：
					<input type="hidden" name="lab_live_qcloud_secretkey" value="{$label_core_live_management_96}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="text" style="width:300px;" name="eip_live_qcloud_secretkey" value="{@eip_live_qcloud_secretkey}" class="wzb-inputText"/>
				</td>
			</tr>
			<!-- 直播并发数-->
			<tr>
				<td class="wzb-form-label">
					<xsl:value-of select="$label_core_live_management_44"/>：
					<input type="hidden" name="lab_live_max_count" value="{$label_core_live_management_44}"></input>
				</td>
				<td class="wzb-form-control">
					<input type="Text" style="width:100px;" maxlength="50" name="eip_live_max_count" value="{@eip_live_max_count}" class="InputFrm"/>
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
		<div class="wzb-bar">
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:eip.ins_upd_eip_exec(document.frmXml,  '<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="wb_gen_form_button">
				<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
				<xsl:with-param name="wb_gen_btn_href">javascript:history.back()</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
</xsl:stylesheet>

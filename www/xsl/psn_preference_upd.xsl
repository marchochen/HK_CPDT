<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/wb_ui_hdr.xsl"/>
	<xsl:import href="utils/wb_ui_title.xsl"/>
	<xsl:import href="utils/wb_ui_desc.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<xsl:import href="utils/wb_ui_footer.xsl"/>
	<xsl:import href="utils/wb_ui_show_no_item.xsl"/>
	<xsl:import href="utils/get_lang_code_label.xsl"/>
	<!-- cust utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================== -->
	<xsl:variable name="lang_selectable" select="/PsnPreference/personalization/skin_list/@lang_selectable"/>
	<xsl:variable name="skin_selectable" select="/PsnPreference/personalization/skin_list/@skin_selectable"/>
	<xsl:variable name="cur_user" select="/PsnPreference/meta/cur_usr"/>
	<!-- =============================================================== -->
	<xsl:template match="/PsnPreference">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="lang_ch">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">我的喜好</xsl:with-param>
			<xsl:with-param name="lab_desc">定義你的個人設置：</xsl:with-param>
			<xsl:with-param name="lab_skin">外觀</xsl:with-param>
			<xsl:with-param name="lab_skin_desc">這裡可以定義整個系統的介面風格，包括上方的圖片、背景的顏色等等。</xsl:with-param>
			<xsl:with-param name="lab_language">語言</xsl:with-param>
			<xsl:with-param name="lab_language_desc">這裡可以設置系統介面使用的語言。注意：這裡的語言設置，不會對系統內的資料產生任何影響。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">重置</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_select">--請選擇--</xsl:with-param>
			<xsl:with-param name="lab_no_option">沒有可選項</xsl:with-param>
			<xsl:with-param name="lab_set_major_tc">主要培訓中心</xsl:with-param>
			<xsl:with-param name="lab_set_major_tc_desc">這裡可以設置你主要管理的培訓中心，設置爲主要培訓中心後，在相關選擇培訓中心的操作中，將會默認選擇該培訓中心。</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">我的喜好</xsl:with-param>
			<xsl:with-param name="lab_desc">定义你的个人设置：</xsl:with-param>
			<xsl:with-param name="lab_skin">外观</xsl:with-param>
			<xsl:with-param name="lab_skin_desc">这里可以定义整个系统的界面风格，包括上方的图片、背景的颜色等等。</xsl:with-param>
			<xsl:with-param name="lab_language">语言</xsl:with-param>
			<xsl:with-param name="lab_language_desc">这里可以设置系统界面使用的语言。注意：这里的语言设置，不会对系统内的数据产生任何影响。</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">重置</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">取消</xsl:with-param>
			<xsl:with-param name="lab_select">--请选择--</xsl:with-param>
			<xsl:with-param name="lab_no_option">没有可选项</xsl:with-param>
			<xsl:with-param name="lab_set_major_tc">主要培训中心</xsl:with-param>
			<xsl:with-param name="lab_set_major_tc_desc">这里可以设置你主要管理的培训中心，设置为主要培训中心后，在相关选择培训中心的操作中，将会默认选择该培训中心。</xsl:with-param>

		</xsl:call-template>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:call-template name="content">
			<xsl:with-param name="lab_title">My preference</xsl:with-param>
			<xsl:with-param name="lab_desc">Specify your personal preferences below:</xsl:with-param>
			<xsl:with-param name="lab_skin">Skin</xsl:with-param>
			<xsl:with-param name="lab_skin_desc">This option determines the overall look-n-feel of the site such as page header and footer design, background colors, font styles, etc.</xsl:with-param>
			<xsl:with-param name="lab_language">Language</xsl:with-param>
			<xsl:with-param name="lab_language_desc">This option determines the language for menu, labels, prompts, messages, etc. Note: it does not control the language of the data. For example, the value of user name, course title, etc. will not be affected.</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_reset">Reset</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_cancel">Cancel</xsl:with-param>
			<xsl:with-param name="lab_select">--Please select--</xsl:with-param>
			<xsl:with-param name="lab_no_option">No options available.</xsl:with-param>
			<xsl:with-param name="lab_set_major_tc">Major training center</xsl:with-param>
			<xsl:with-param name="lab_set_major_tc_desc">You can assign a major training center here. After that, your major training center will be default selected wherever a selection of training center is needed.</xsl:with-param>

		</xsl:call-template>
	</xsl:template>
	<!-- =============================================================== -->
	<!-- parentinfo start -->
	<xsl:template name="content">
		<xsl:param name="lab_title"/>
		<xsl:param name="lab_desc"/>
		<xsl:param name="lab_skin"/>
		<xsl:param name="lab_skin_desc"/>
		<xsl:param name="lab_language"/>
		<xsl:param name="lab_language_desc"/>
		<xsl:param name="lab_g_form_btn_ok"/>
		<xsl:param name="lab_g_form_btn_reset"/>
		<xsl:param name="lab_g_form_btn_cancel"/>
		<xsl:param name="lab_select"/>
		<xsl:param name="lab_no_option"/>
		<xsl:param name="lab_set_major_tc"/>
		<xsl:param name="lab_set_major_tc_desc"/>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_psn_preference.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript"><![CDATA[
			var wbPfr = new wbPsnPreference;
			
			function changeSkin(frm){
				var langOption;
				if(frm.lang.type == 'select-one'){
				for (var i=0; i < frm.skin_id.length; i++){
					if (frm.skin_id.options[i].selected ){
						if (frm.skin_id.options[i].value == ''){
							frm.lang.length = 0;
							langOption = new Option;
							langOption.value = '';
							langOption.text = ']]><xsl:value-of select="$lab_select"/><![CDATA[';
							addOption(frm.lang,langOption)
						}
						]]><xsl:for-each select="personalization/skin_list/skin"><![CDATA[
								if (frm.skin_id.options[i].value == ']]><xsl:value-of select="@id"/><![CDATA['){
									frm.lang.length = 0;
									langOption = new Option;
									langOption.value = '';
									langOption.text = ']]><xsl:value-of select="$lab_select"/><![CDATA[';
									addOption(frm.lang,langOption)
									]]><xsl:for-each select="lang_list/lang">
						<xsl:variable name="lang_opt_text">
							<xsl:call-template name="get_lang_code_label">
								<xsl:with-param name="code">
									<xsl:value-of select="."/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:variable><![CDATA[
										langOption = new Option;
										langOption.value = ']]><xsl:value-of select="."/><![CDATA[';
										langOption.text = ']]><xsl:value-of select="$lang_opt_text"/><![CDATA[';
										]]><xsl:choose>
							<xsl:when test="(. = $cur_user/@label) or (. = 'zh-hk' and $cur_user/@label = 'Big5') or (. = 'zh-cn' and $cur_user/@label = 'GB2312') or (. = 'en-us' and $cur_user/@label = 'ISO-8859-1')"><![CDATA[
										langOption.selected = true;
										]]></xsl:when>
						</xsl:choose><![CDATA[
										addOption(frm.lang,langOption)
										]]></xsl:for-each><![CDATA[
								}

						]]></xsl:for-each><![CDATA[
					}
				}
				}
			}
			]]></script>
		</head>
		<body leftmargin="0" marginwidth="0" marginheight="0" topmargin="0">
			<form name="frmXml">
				<input type="hidden" name="cmd"/>
				<input type="hidden" name="module"/>
				<input type="hidden" name="url_success"/>
				<input type="hidden" name="url_failure"/>
				<!-- navigation -->
				<xsl:call-template name="wb_ui_hdr">
									<xsl:with-param name="belong_module">FTN_AMD_USR_INFO</xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_title">
					<xsl:with-param name="text" select="$lab_title"/>
				</xsl:call-template>
				<xsl:call-template name="wb_ui_desc">
					<xsl:with-param name="text" select="$lab_desc"/>
				</xsl:call-template>
				<xsl:choose>
					<xsl:when test="$lang_selectable = 'false' and $skin_selectable = 'false' and not(tc_lst)">
						<xsl:call-template name="wb_ui_show_no_item">
							<xsl:with-param name="text" select="$lab_no_option"/>
						</xsl:call-template>
						<div class="wzb-form-star">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_gen_home()</xsl:with-param>
							</xsl:call-template>
						</div>
					</xsl:when>
					<xsl:otherwise>
						<table>
							<xsl:choose>
								<xsl:when test="$skin_selectable = 'true'">
									<tr>
										<td width="20%" align="right" valign="top">
											<xsl:value-of select="$lab_skin"/>:
										</td>
										<td width="80%" valign="top">
											<select name="skin_id" size="1" class="Select" onchange="javascript:changeSkin(document.frmXml)">
												<xsl:for-each select="personalization/skin_list/skin">
													<option value="{@id}">
														<xsl:if test="@id=$cur_user/@skin">
															<xsl:attribute name="selected">selected</xsl:attribute>
														</xsl:if>
														<xsl:value-of select="name"/>
													</option>
												</xsl:for-each>
											</select>
											<br/>
											<xsl:value-of select="$lab_skin_desc"/>
										</td>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<!--							<input type="hidden" name="skin_id" value="{$cur_user/@skin}"/>-->
								</xsl:otherwise>
							</xsl:choose>
							<xsl:choose>
								<xsl:when test="$lang_selectable = 'true'">
									<tr>
										<td class="wzb-form-label" valign="top">
											<xsl:value-of select="$lab_language"/>:
										</td>
										<td class="wzb-form-control">
											<select name="lang" size="1" class="Select">
												<option value="">
													<xsl:value-of select="$lab_select"/>
												</option>
												<xsl:for-each select="personalization/skin_list/skin[@id = $cur_user/@skin]/lang_list/lang">
													<option value="{.}">
														<xsl:choose>
															<xsl:when test="(. = $cur_user/@label ) or (. = 'zh-hk' and $cur_user/@label = 'Big5') or (. = 'zh-cn' and $cur_user/@label = 'GB2312') or (. = 'en-us' and $cur_user/@label = 'ISO-8859-1')">
																<xsl:attribute name="selected">selected</xsl:attribute>
															</xsl:when>
														</xsl:choose>
														<xsl:call-template name="get_lang_code_label">
															<xsl:with-param name="code">
																<xsl:value-of select="."/>
															</xsl:with-param>
														</xsl:call-template>
													</option>
												</xsl:for-each>
											</select>
											<br/>
											<xsl:value-of select="$lab_language_desc"/>
										</td>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<!--							<input type="hidden" name="lang">
								<xsl:attribute name="value">
									<xsl:choose>
										<xsl:when test="$cur_user/@label = 'Big5'" >zh-hk</xsl:when>
										<xsl:when test="$cur_user/@label = 'GB2312'" >zh-cn</xsl:when>
										<xsl:otherwise>en-us</xsl:otherwise>
									</xsl:choose>
								</xsl:attribute>
							</input>-->
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="tc_lst">
								<tr>
									<td class="wzb-form-label" valign="top">
										<xsl:value-of select="$lab_set_major_tc"/>:
									</td>
									<td class="wzb-form-control">
										<select name="major_tc_id" size="1" class="Select">
											<option value="0">
												<xsl:value-of select="$lab_select"/>
											</option>
											<xsl:for-each select="tc_lst/tc">
												<option value="{@id}">
													<xsl:if test="@id=../../major_tc/@id">
														<xsl:attribute name="selected">selected</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="name"/>
												</option>
											</xsl:for-each>
										</select>		
										<br/>
										<xsl:value-of select="$lab_set_major_tc_desc"/>
									</td>
								</tr>								
							</xsl:if>
						</table>
						<div class="wzb-bar">
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_ok"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:wbPfr.save_my_preference(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
								<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_reset"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:wbPfr.del_my_preference()</xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_gen_form_button">
								<xsl:with-param name="wb_gen_btn_name" select="$lab_g_form_btn_cancel"/>
								<xsl:with-param name="wb_gen_btn_href">javascript:wb_utils_gen_home()</xsl:with-param>
							</xsl:call-template>
						</div>
					</xsl:otherwise>
				</xsl:choose>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>

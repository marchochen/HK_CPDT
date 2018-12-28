<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- utils -->
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_init_lab.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="utils/wb_gen_form_button.xsl"/>
	<xsl:import href="utils/change_lowercase.xsl"/>
	<xsl:import href="utils/wb_ui_head.xsl"/>
	<xsl:import href="utils/wb_ui_line.xsl"/>
	<!-- customized utils -->
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<!-- const -->
	<xsl:import href="wb_const.xsl"/>
	<xsl:output indent="yes"/>
	<!-- =============================================================================== -->
	<xsl:template match="/">
		<html>
			<xsl:call-template name="wb_init_lab"/>
		</html>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template name="lang_ch">
		<xsl:apply-templates select="message">
			<xsl:with-param name="lab_status">狀況</xsl:with-param>
			<xsl:with-param name="lab_error">錯誤</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">確定</xsl:with-param>
			<xsl:with-param name="lab_continue_create">繼續添加</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_gb">
		<xsl:apply-templates select="message">
			<xsl:with-param name="lab_status">状态</xsl:with-param>
			<xsl:with-param name="lab_error">错误</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">确定</xsl:with-param>
			<xsl:with-param name="lab_continue_create">继续添加</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template name="lang_en">
		<xsl:apply-templates select="message">
			<xsl:with-param name="lab_status">Status</xsl:with-param>
			<xsl:with-param name="lab_error">Error</xsl:with-param>
			<xsl:with-param name="lab_g_form_btn_ok">OK</xsl:with-param>
			<xsl:with-param name="lab_continue_create">Create another</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template match="message">
	<xsl:param name="lab_status"/>
	<xsl:param name="lab_error"/>
	<xsl:param name="lab_g_form_btn_ok"/>
	<xsl:param name="lab_continue_create"/>
	<xsl:variable name="training_type">
		<xsl:choose>
			<xsl:when test="training_type"><xsl:value-of select="training_type"/></xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
		<head>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
				<xsl:text> - </xsl:text>
				<xsl:choose>
					<xsl:when test="title = 'STATUS'">
						<xsl:value-of select="$lab_status"/>
					</xsl:when>
					<xsl:when test="title =  'ERROR'">
						<xsl:value-of select="$lab_error"/>
					</xsl:when>
					<xsl:otherwise></xsl:otherwise>
				</xsl:choose>
			</title>
			<xsl:call-template name="wb_css">
				<xsl:with-param name="view">wb_ui</xsl:with-param>
			</xsl:call-template>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
			<script language="javascript" type="text/javascript"><![CDATA[
				itm_lst = new wbItem
				function do_module() {
				]]><xsl:if test="//module">
					<xsl:choose>
						<xsl:when test="//module/@action = 'INSERT'">wb_utils_set_cookie('mod_id',<xsl:value-of select="//module/@id"/>)
							<xsl:choose>
								<xsl:when test="title = 'STATUS'">window.parent.addNode('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="//module"/></xsl:with-param></xsl:call-template>', '<xsl:value-of select="//module/@subtype"/>', <xsl:value-of select="//module/@id"/>, '<xsl:value-of select="//course/@timestamp"/>');</xsl:when>
								<xsl:when test="title = 'ERROR'">window.parent.cancelAdd();</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="//module/@action = 'UPDATE'">
							<xsl:choose>
								<xsl:when test="title = 'STATUS'">window.parent.editNode('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="//module"/></xsl:with-param></xsl:call-template>');</xsl:when>
								<xsl:when test="title = 'ERROR'">window.parent.cancelEdit();</xsl:when>
							</xsl:choose>
						</xsl:when>
						<xsl:when test="//module/@action = 'DELETE'">
							<xsl:choose>
								<xsl:when test="title = 'STATUS'">window.parent.confirmDelete('<xsl:value-of select="//course/@timestamp"/>');</xsl:when>
								<xsl:when test="title = 'ERROR'">window.parent.cancelDelete();</xsl:when>
							</xsl:choose>
						</xsl:when>
					</xsl:choose>
				</xsl:if><![CDATA[
			}

			function enter(){
				ftn = "]]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="body/button/@url"/></xsl:with-param></xsl:call-template><![CDATA[";
				if((ftn.indexOf("javascript:")>-1)||(ftn.indexOf("Javascript:")>-1)){
					]]><xsl:value-of select="substring-after(body/button/@url,'avascript:')"/><![CDATA[
				}else if(ftn==""){
					wb_utils_gen_home();
				}else{
					window.location.href = "]]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="body/button/@url"/></xsl:with-param></xsl:call-template><![CDATA["
				}
				return false;
			}

			function go_url(url){
				self.location.href = url;
			}

			function url_error(url){
				alert('Not supported URL format: ' + url )
			}
		]]></script>
		</head>
		<body leftmargin="0" marginheight="0" marginwidth="0" topmargin="0" onload="javascript:do_module();document.frmXml.elements[0].focus();">
			<form name="frmXml" onsubmit="enter();return false;">
				<table border="0" cellpadding="0" cellspacing="0" width="{$wb_gen_msg_box_width}">
					<tr>
						<td>
							<img src="{$wb_img_path}tp.gif" width="1" height="10" border="0"/>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:call-template name="wb_ui_head">
								<xsl:with-param name="width">100%</xsl:with-param>
								<xsl:with-param name="text"><xsl:choose><xsl:when test="title = 'STATUS'"><xsl:value-of select="$lab_status"/></xsl:when><xsl:when test="title =  'ERROR'"><xsl:value-of select="$lab_error"/></xsl:when><xsl:otherwise><xsl:text>&#160;</xsl:text></xsl:otherwise></xsl:choose></xsl:with-param>
							</xsl:call-template>
							<xsl:call-template name="wb_ui_line">
								<xsl:with-param name="width">100%</xsl:with-param>
							</xsl:call-template>
							<table cellpadding="0" cellspacing="0" width="100%" border="0">
								<tr>
									<td width="1" class="Line">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
									<td width="100%">
										<table cellpadding="15" cellspacing="0" width="100%" border="0">
											<tr><td height="10"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td></tr>
											<tr>
												<td>
													<xsl:if test="title= 'ERROR'">
														<img src="{$wb_img_path}ico_warning.gif" width="30" height="24" border="0" align="absmiddle"/>
														<xsl:text>&#160;</xsl:text>
													</xsl:if>
													<span class="TitleText">
														<xsl:value-of disable-output-escaping="yes" select="body/text"/>
													</span>
												</td>
											</tr>
											<tr><td height="10"><img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/></td></tr>
										</table>
									</td>
									<td width="1" class="Line">
										<img src="{$wb_img_path}tp.gif" width="1" height="1" border="0"/>
									</td>
								</tr>
							</table>
							<xsl:call-template name="wb_ui_line">
								<xsl:with-param name="width">100%</xsl:with-param>
							</xsl:call-template>
							<table cellpadding="3" cellspacing="0" width="100%" border="0">
								<tr>
									<td align="center">
										<xsl:for-each select="body/button">
											<xsl:variable name="url">
												<xsl:choose>
													<xsl:when test="@url = ''">javascript:wb_utils_gen_home()</xsl:when>
													<xsl:otherwise>
														<xsl:value-of select="@url"/>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:variable>

											<xsl:call-template name="wb_gen_form_button">
												<xsl:with-param name="wb_gen_btn_name">
													<xsl:text>&#160;</xsl:text>
													<xsl:value-of select="$lab_continue_create"/>
													<xsl:text>&#160;</xsl:text>
												</xsl:with-param>
													<xsl:with-param name="wb_gen_btn_href">javascript:itm_lst.select_add_item_type_prep('<xsl:value-of select="$training_type"/>')
												</xsl:with-param>
												<xsl:with-param name="wb_gen_btn_multi">0</xsl:with-param>
											</xsl:call-template>
											<xsl:text>&#160;</xsl:text>
											<xsl:text>&#160;</xsl:text>

											<xsl:call-template name="wb_gen_form_button">
												<xsl:with-param name="wb_gen_btn_name"><xsl:text>&#160;</xsl:text><xsl:value-of select="$lab_g_form_btn_ok"/><xsl:text>&#160;</xsl:text></xsl:with-param>
												<xsl:with-param name="wb_gen_btn_href"><xsl:call-template name="determine_url"><xsl:with-param name="url"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template></xsl:with-param>
												<xsl:with-param name="wb_gen_btn_multi">1</xsl:with-param>
												<xsl:with-param name="wb_gen_btn_target"><xsl:value-of select="@target"/></xsl:with-param>
											</xsl:call-template>
											<xsl:if test="position() != last()">
												<img src="{$wb_img_path}tp.gif" width="5" height="1" border="0"/>
											</xsl:if>
										</xsl:for-each>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<script language="javascript" type="text/javascript">
				<![CDATA[
				if(document.all){
				 document.write('<input type="submit" value="" size="0" style="height:0px;width:0px;visibility:hidden;">')
				}
				]]>
				</script>
			</form>
		</body>
	</xsl:template>
	<!-- =============================================================================== -->
	<xsl:template name="determine_url">
		<xsl:param name="url"/>
		<xsl:variable name="js_str">
			<xsl:call-template name="change_lowercase">
				<xsl:with-param name="input_value" select="substring($url,1,11)"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="contains($js_str,'javascript:')">
				<xsl:value-of select="$url"/>
			</xsl:when>
			<xsl:when test="contains(substring($url,1,7),'http://') or contains(substring($url,1,8),'https://')">javascript:go_url('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>')</xsl:when>
			<xsl:when test="contains(substring($url,1,11),'../servlet/') or contains(substring($url,1,11),'/servlet/')">javascript:go_url('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>')</xsl:when>
			<xsl:when test="contains(substring($url,1,1),'/') or contains(substring($url,1,11),'/servlet/')">javascript:go_url('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>')</xsl:when>
			<xsl:otherwise>javascript:url_error('<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$url"/></xsl:with-param></xsl:call-template>')</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================================== -->
</xsl:stylesheet>

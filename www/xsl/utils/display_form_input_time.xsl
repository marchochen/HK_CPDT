<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="display_form_input_hhmm.xsl"/>
<!-- ==================================================================== -->
<xsl:template name="display_form_input_time">
	<xsl:param name="fld_name"/><!-- input field name -->
	<xsl:param name="hidden_fld_name"/><!-- hidden field name -->
	<xsl:param name="timestamp"/><!-- default date -->
	<xsl:param name="def_time"/><!-- default time -->
	<xsl:param name="def_date"/>
	<xsl:param name="frm">document.frmXml</xsl:param>
	<xsl:param name="show_label">Y</xsl:param><!-- display datetime label -->
	<xsl:param name="display_form_input_hhmm">N</xsl:param><!-- display time input field -->
	<xsl:param name="format">2</xsl:param><!-- { [1] DD-MM-YYYY | [2] YYYY-MM-DD } -->
	<xsl:param name="focus_rad_btn_name"/><!-- automatically focus the existing radio button before the datetime input field -->
	<xsl:param name="caching_function"/>
	<xsl:param name="class">wzb-inputText</xsl:param>
	<xsl:param name="display_img_calendar_picker"/><!-- show calendar picker img-->
	<xsl:param name="clickAction"/><!--点击时执行事件-->
	<!-- timestamp hidden field name -->
	<xsl:if test="$hidden_fld_name != ''"><input type="hidden" name="{$hidden_fld_name}" value=""/></xsl:if>
	<xsl:choose>
		<xsl:when test="$format = '1'">
			<xsl:element name="input">
				<xsl:attribute name="type">text</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_dd</xsl:attribute>
				<xsl:attribute name="maxlength">2</xsl:attribute>
				<xsl:attribute name="size">2</xsl:attribute>
				<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,9,2)"/></xsl:attribute>		
				<xsl:attribute name="onKeyup">javascript:auto_focus_field(<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_dd,2,<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_mm)</xsl:attribute>
				<xsl:if test="not($focus_rad_btn_name = '')"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
				<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
			</xsl:element>
			<xsl:if test="$timestamp = '' and $def_date != ''">
			<script type="text/javascript" language="JavaScript">
				<xsl:choose>
					<xsl:when test="$def_date = 'current'">
						<![CDATA[
							var thisdate = new Date();
						]]>
						<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_dd.value = <![CDATA[thisdate.getDate();]]>
					</xsl:when>
				</xsl:choose>
			</script>
			</xsl:if>
			<xsl:text>-</xsl:text>
			<xsl:element name="input">
				<xsl:attribute name="type">text</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_mm</xsl:attribute>
				<xsl:attribute name="maxlength">2</xsl:attribute>
				<xsl:attribute name="size">2</xsl:attribute>
				<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,6,2)"/></xsl:attribute>		
				<xsl:attribute name="onKeyup">javascript:auto_focus_field(<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_mm,2,<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_yy)</xsl:attribute>
				<xsl:if test="not($focus_rad_btn_name = '')"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
				<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
			</xsl:element>
			<xsl:if test="$timestamp = '' and $def_date != ''">
			<script type="text/javascript" language="JavaScript">
				<xsl:choose>
					<xsl:when test="$def_date = 'current'">
						<![CDATA[
							var thisdate = new Date();
						]]>
						<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_mm.value = <![CDATA[thisdate.getMonth() + 1;]]>
					</xsl:when>
				</xsl:choose>
			</script>
			</xsl:if>
			<xsl:text>-</xsl:text>
			<xsl:element name="input">
				<xsl:attribute name="type">text</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_yy</xsl:attribute>
				<xsl:attribute name="maxlength">4</xsl:attribute>
				<xsl:attribute name="size">4</xsl:attribute>
				<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,1,4)"/></xsl:attribute>
				<xsl:attribute name="onKeyup">javascript:auto_focus_field(<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_yy,4,<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_hour)</xsl:attribute>
				<xsl:if test="not($focus_rad_btn_name = '')"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
				<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
			</xsl:element>
			<xsl:if test="$timestamp = '' and $def_date != ''">
			<script type="text/javascript" language="JavaScript">
				<xsl:choose>
					<xsl:when test="$def_date = 'current'">
						<![CDATA[
							var thisdate = new Date();
						]]>
						<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_yy.value = <![CDATA[thisdate.getFullYear();]]>
					</xsl:when>
				</xsl:choose>
			</script>
			</xsl:if>
			<xsl:if test="$show_label = 'Y'"><xsl:text> </xsl:text><xsl:value-of select="$lab_dd_mm_yy"/></xsl:if>
		</xsl:when>
		<xsl:otherwise>
			<xsl:element name="input">
				<xsl:attribute name="type">text</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_yy</xsl:attribute>
				<xsl:attribute name="maxlength">4</xsl:attribute>
				<xsl:attribute name="size">4</xsl:attribute>
				<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,1,4)"/></xsl:attribute>	
				<xsl:attribute name="onKeyup">javascript:auto_focus_field(<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_yy,4,<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_mm)</xsl:attribute>				
				<xsl:if test="not($focus_rad_btn_name = '')"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
				<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
				<xsl:if test="$timestamp = '' and $def_date != ''">
				<script type="text/javascript" language="JavaScript">
					<xsl:choose>
						<xsl:when test="$def_date = 'current'">
							<![CDATA[
								var thisdate = new Date();
							]]>
							<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_yy.value = <![CDATA[thisdate.getFullYear();]]>
						</xsl:when>
					</xsl:choose>
					</script>
				</xsl:if>
			</xsl:element>
			<xsl:text>-</xsl:text>
			<xsl:element name="input">
				<xsl:attribute name="type">text</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_mm</xsl:attribute>
				<xsl:attribute name="maxlength">2</xsl:attribute>
				<xsl:attribute name="size">2</xsl:attribute>
				<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,6,2)"/></xsl:attribute>		
				<xsl:attribute name="onKeyup">javascript:auto_focus_field(<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_mm,2,<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_dd)</xsl:attribute>
				<xsl:if test="not($focus_rad_btn_name = '')"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
				<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
			</xsl:element>
			<xsl:if test="$timestamp = '' and $def_date != ''">
			<script type="text/javascript" language="JavaScript">
				<xsl:choose>
					<xsl:when test="$def_date = 'current'">
						<![CDATA[
							var thisdate = new Date();
						]]>
						<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_mm.value = <![CDATA[thisdate.getMonth() + 1;]]>
					</xsl:when>
				</xsl:choose>
				</script>
			</xsl:if>
			<xsl:text>-</xsl:text>
			<xsl:element name="input">
				<xsl:attribute name="type">text</xsl:attribute>
				<xsl:attribute name="name"><xsl:value-of select="$fld_name"/>_dd</xsl:attribute>
				<xsl:attribute name="maxlength">2</xsl:attribute>
				<xsl:attribute name="size">2</xsl:attribute>
				<xsl:attribute name="class" ><xsl:value-of select="$class"/></xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="substring($timestamp,9,2)"/></xsl:attribute>		
				<xsl:attribute name="onKeyup">javascript:auto_focus_field(<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_dd,2,<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_hour)</xsl:attribute>
				<xsl:if test="not($focus_rad_btn_name = '')"><xsl:attribute name="onfocus">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click()</xsl:attribute></xsl:if>
				<xsl:if test="not($caching_function = '')"><xsl:attribute name="onblur"><xsl:copy-of select="$caching_function"/></xsl:attribute></xsl:if>
			</xsl:element>
			<xsl:if test="$timestamp = '' and $def_date != ''">
			<script type="text/javascript" language="JavaScript">
				<xsl:choose>
					<xsl:when test="$def_date = 'current'">
						<![CDATA[
							var thisdate = new Date();
						]]>
						<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>_dd.value = <![CDATA[thisdate.getDate();]]>
					</xsl:when>
				</xsl:choose>
			</script>
			</xsl:if>
			<xsl:if test="$show_label = 'Y'"><xsl:text> </xsl:text><xsl:value-of select="$lab_yy_mm_dd"/></xsl:if>
		</xsl:otherwise>
	</xsl:choose>

	<xsl:choose>
		<xsl:when test="$display_img_calendar_picker != 'N'">
			<xsl:text>&#160;</xsl:text><script language="JavaScript" src="{$wb_js_path}date-picker.js"></script>
			<a href="" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
			<xsl:attribute name="href">
				<xsl:choose>
					<xsl:when test="$focus_rad_btn_name != ''">javascript:<xsl:value-of select="$frm"/>.<xsl:value-of select="$focus_rad_btn_name"/>.click();show_calendar('<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>', '','','','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$wb_css_path"/>wb_ui.css');</xsl:when>
					<xsl:otherwise>javascript:show_calendar('<xsl:value-of select="$frm"/>.<xsl:value-of select="$fld_name"/>', '','','','<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="$wb_css_path"/>wb_ui.css');</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="$clickAction != ''"><xsl:value-of select="$clickAction"/></xsl:if>
			</xsl:attribute>
			<img src="{$wb_img_path}btn_calendar.gif" border="0" align="absmiddle" />
			</a><xsl:text>&#160;</xsl:text>
		</xsl:when>
		<xsl:otherwise/>
	</xsl:choose>



	<xsl:choose>
		<xsl:when test="$display_form_input_hhmm = 'Y'">
			<xsl:text>&#160;</xsl:text>
			<xsl:call-template name="display_form_input_hhmm">
				<xsl:with-param name="fld_name"><xsl:value-of select="$fld_name"/></xsl:with-param>
				<xsl:with-param name="frm"><xsl:value-of select="$frm"/></xsl:with-param>
				<xsl:with-param name="timestamp"><xsl:value-of select="$timestamp"/></xsl:with-param>
				<xsl:with-param name="def_time"><xsl:value-of select="$def_time"/></xsl:with-param>
				<xsl:with-param name="show_label"><xsl:value-of select="$show_label"/></xsl:with-param>
				<xsl:with-param name="focus_rad_btn_name"><xsl:value-of select="$focus_rad_btn_name"/></xsl:with-param>
				<xsl:with-param name="caching_function"><xsl:value-of select="$caching_function"/></xsl:with-param>
			</xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:choose>
				<xsl:when test="$timestamp != ''">
					<input type="hidden" name="{$fld_name}_hour" value="{substring($timestamp,12,2)}"/>
					<input type="hidden" name="{$fld_name}_min" value="{substring($timestamp,15,2)}"/>					
				</xsl:when>
				<xsl:when test="$def_time != '' and $timestamp = ''">
					<input type="hidden" name="{$fld_name}_hour" value="{substring($def_time,1,2)}"/>
					<input type="hidden" name="{$fld_name}_min" value="{substring($def_time,4,2)}"/>
				</xsl:when>
				<xsl:otherwise>
					<input type="hidden" name="{$fld_name}_hour" value="00"/>
					<input type="hidden" name="{$fld_name}_min" value="00"/>
				</xsl:otherwise>
			</xsl:choose>	
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>
</xsl:stylesheet>
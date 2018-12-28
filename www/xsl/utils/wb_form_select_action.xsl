<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="escape_js.xsl"/>
<!-- ====================================================================================================== -->
<xsl:template name="wb_form_select_action">
	<xsl:param name="frm"/>
	<xsl:param name="field_name"/>
	<xsl:param name="sel_title"/>
	<xsl:param name="sel_width">150</xsl:param>
	<xsl:param name="sel_size">1</xsl:param>
	<xsl:param name="sel_class">wzb-form-select</xsl:param>
	<xsl:param name="onChangeFunction"/>
	<xsl:param name="onChangeValidateFunction"/>
	<xsl:param name="option_list"/>	
	<!-- -->
	<xsl:param name="show_submit_btn">false</xsl:param>
	<xsl:param name="submit_btn_name">sel_frm_submit_btn</xsl:param>
	<xsl:param name="submit_btn_on">ico_select_action_on.gif</xsl:param>
	<xsl:param name="submit_btn_on_width">18</xsl:param>
	<xsl:param name="submit_btn_on_height">18</xsl:param>
	<xsl:param name="submit_btn_off">tp.gif</xsl:param>
	<xsl:param name="submit_btn_off_width">1</xsl:param>
	<xsl:param name="submit_btn_off_height">1</xsl:param>
	<!-- -->
	<xsl:if test="$show_submit_btn = 'true'">
		<script language="javascript" type="text/javascript"><![CDATA[
			submit_action_btn_on = new Image;
			submit_action_btn_on.src = ']]><xsl:value-of select="$wb_img_path"/><xsl:value-of select="$submit_btn_on"/><![CDATA['
			submit_action_btn_off = new Image;
			submit_action_btn_off.src = ']]><xsl:value-of select="$wb_img_path"/><xsl:value-of select="$submit_btn_off"/><![CDATA['
			
			function ]]><xsl:value-of select="$field_name"/><![CDATA[_change_action(frm, lang){
				if (document.all) {
					if (frm.]]><xsl:value-of select="$field_name"/><![CDATA[.selectedIndex != 0){
					
						]]><xsl:choose><xsl:when test="$onChangeValidateFunction != ''"><![CDATA[
						if (]]><xsl:value-of select="$onChangeValidateFunction"/><![CDATA[){
							
							]]><xsl:if test="$show_submit_btn = 'true'"><![CDATA[		
							document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.src = submit_action_btn_on.src
							document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.height = ']]><xsl:value-of select="$submit_btn_on_height"/><![CDATA['
							document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.width = ']]><xsl:value-of select="$submit_btn_on_width"/><![CDATA['
							]]></xsl:if><![CDATA[
							return;
						}
						]]></xsl:when><xsl:otherwise><![CDATA[
						
						]]><xsl:if test="$show_submit_btn = 'true'"><![CDATA[			
						document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.src = submit_action_btn_on.src
						document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.height = ']]><xsl:value-of select="$submit_btn_on_height"/><![CDATA['
						document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.width = ']]><xsl:value-of select="$submit_btn_on_width"/><![CDATA['
						return;
						]]></xsl:if><![CDATA[
						
						]]></xsl:otherwise></xsl:choose><![CDATA[
						
					}else if (frm.]]><xsl:value-of select="$field_name"/><![CDATA[.selectedIndex == 0){
					
						]]><xsl:if test="$show_submit_btn = 'true'"><![CDATA[					
						document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.src = submit_action_btn_off.src
						document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.height = ']]><xsl:value-of select="$submit_btn_on_height"/><![CDATA['
						document.]]><xsl:value-of select="$submit_btn_name"/><![CDATA[.width = ']]><xsl:value-of select="$submit_btn_on_width"/><![CDATA['
						]]></xsl:if><![CDATA[
					}
				}
			}
		]]></script>		
	</xsl:if>
	<!-- selection box -->
	<table>
		<tr>
			<xsl:if test="$sel_title != ''">
				<td width="25%" align="right"><xsl:copy-of select="$sel_title"/></td>
			</xsl:if>
			<td width="75%" align="left">
				<select width="{$sel_width}" style="width:{$sel_width}px" class="{$sel_class}" size="{$sel_size}" name="{$field_name}">
					<xsl:choose>
						<xsl:when test="$show_submit_btn = 'true'">
							<xsl:attribute name="onchange">javascript:<xsl:value-of select="$field_name"/>_change_action(<xsl:value-of select="$frm"/>,'<xsl:value-of select="$wb_lang"/>')</xsl:attribute></xsl:when>
						<xsl:when test="$onChangeFunction != ''"><xsl:attribute name="onchange">javascript:<xsl:value-of select="$onChangeFunction"/></xsl:attribute></xsl:when>			
					</xsl:choose>
					<xsl:copy-of select="$option_list"/>
				</select>
			</td>	
			<!-- submit button -->
			<xsl:if test="$show_submit_btn = 'true'">
				<td><img src="{$wb_img_path}tp.gif" width="2" height="1" border="0"/></td>
				<td width="{$submit_btn_on_width}">	
					<script language="javascript" type="text/javascript"><![CDATA[
						if (!document.all && (document.getElementById || document.layers != null)) {
							var str = ""
							str += '<a href="javascript:]]><xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="$onChangeFunction"/></xsl:with-param></xsl:call-template><![CDATA[">'
							str += '<img src="]]><xsl:value-of select="$wb_img_path"/><xsl:value-of select="$submit_btn_on"/><![CDATA[" border="0" />'
							str += '</a>'
							document.write(str)
						}					
					]]></script>
					<xsl:choose>
						<xsl:when test="$onChangeFunction != ''">
							<a href="javascript:{$onChangeFunction}">					
								<xsl:element name="img">
									<xsl:attribute name="name"><xsl:value-of select="$submit_btn_name"/></xsl:attribute>
									<xsl:attribute name="align">absmiddle</xsl:attribute>
									<xsl:attribute name="border">0</xsl:attribute>
									<xsl:attribute name="height"><xsl:value-of select="$submit_btn_on_height"/></xsl:attribute>
									<xsl:attribute name="width"><xsl:value-of select="$submit_btn_on_width"/></xsl:attribute>
									<xsl:attribute name="src"><xsl:value-of select="$wb_img_path"/><xsl:value-of select="$submit_btn_off"/></xsl:attribute>				
								</xsl:element>
							</a>
						</xsl:when>
						<xsl:otherwise>
							<xsl:element name="img">
									<xsl:attribute name="name"><xsl:value-of select="$submit_btn_name"/></xsl:attribute>
									<xsl:attribute name="align">absmiddle</xsl:attribute>
									<xsl:attribute name="border">0</xsl:attribute>
									<xsl:attribute name="height"><xsl:value-of select="$submit_btn_on_height"/></xsl:attribute>
									<xsl:attribute name="width"><xsl:value-of select="$submit_btn_on_width"/></xsl:attribute>
									<xsl:attribute name="src"><xsl:value-of select="$wb_img_path"/><xsl:value-of select="$submit_btn_off"/></xsl:attribute>				
								</xsl:element>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</xsl:if>
		</tr>
	</table>
</xsl:template>
<!-- ====================================================================================================== -->
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="gen_check_box">
		<xsl:param name="name"/>
		<xsl:param name="default" select="/user_manager/user/extra_multipleoption_31"/>
		<xsl:param name="options"/>
		<xsl:param name="cur_att_name"/>
		<xsl:for-each select="$options/*">
			<xsl:variable name="id" select="@id"/>
			<label for="{concat($cur_att_name,$id)}">
						<input type="checkbox" name="{$name}" id="{concat($cur_att_name,$id)}" value="{$id}">
							<xsl:if test="$default/option[@id=$id]">
								<xsl:attribute name="checked">checked</xsl:attribute>
							</xsl:if>
						</input>
				<span class="Text">
					<xsl:value-of select="concat(' ',label[@xml:lang=$cur_lang],' ')"/>
				</span>
			</label>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="gen_drop_down_box">
		<xsl:param name="name"/>
		<xsl:param name="options"/>
		<xsl:param name="default"/>
			<select name="{$name}" size="1" class="Select">
				<option value=""><xsl:value-of select="$lab_not_specified"/></option>
					<xsl:for-each select="$options/*">
						<xsl:variable name="id" select="@id"/>
						<xsl:choose>
							<xsl:when test="$id=$default">
								<option value="{$id}" selected="selected"><xsl:value-of select="label[@xml:lang = $cur_lang]"/></option>
							</xsl:when>
							<xsl:otherwise>
								<option value="{$id}"><xsl:value-of select="label[@xml:lang = $cur_lang]"/></option>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
			</select>
	</xsl:template>
</xsl:stylesheet>

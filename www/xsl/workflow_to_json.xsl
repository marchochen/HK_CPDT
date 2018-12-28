<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:template match="/">
		{<xsl:apply-templates select="*"/>}
	</xsl:template>
	<!-- ============================================================================ -->
	<!-- action_rule -->
	<xsl:template match="action_rule">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
			<xsl:with-param name="node_content">
				<xsl:apply-templates select="action" mode="action_rule"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:text>,</xsl:text>
	</xsl:template>
	<xsl:template match="action" mode="action_rule">
		<xsl:call-template name="get_array"/>
	</xsl:template>
	<!-- ============================================================================ -->
	<!-- workflow -->
	<xsl:template match="workflow">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
			<xsl:with-param name="node_content">
				<xsl:apply-templates select="process"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:text>,</xsl:text>
	</xsl:template>
	<xsl:template match="process">
		<xsl:call-template name="get_node">
			<xsl:with-param name="node_content">
				<xsl:apply-templates select="status" mode="workflow"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="status" mode="workflow">
		<xsl:call-template name="get_array">
			<xsl:with-param name="sub_obj">
				<xsl:apply-templates select="action" mode="workflow"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="action" mode="workflow">
		<xsl:call-template name="get_array">
			<xsl:with-param name="sub_obj">
				<xsl:apply-templates select="access"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="access">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
			<xsl:with-param name="node_content">
				<xsl:apply-templates select="role"/>
				<xsl:if test="approver">
					<xsl:text>,</xsl:text>
					<xsl:apply-templates select="approver"/>
				</xsl:if>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="role">
		<xsl:call-template name="get_array">
			<xsl:with-param name="sub_obj">
				<xsl:apply-templates select="label"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="label">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="approver">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================================ -->
	<!-- queue_rule -->
	<xsl:template match="queue_rule">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
			<xsl:with-param name="node_content">
				<xsl:apply-templates select="next_queue"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:text>,</xsl:text>
	</xsl:template>
	<xsl:template match="next_queue">
		<xsl:call-template name="get_array">
			<xsl:with-param name="sub_obj">
				<xsl:apply-templates select="conditions"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="conditions">
		<xsl:call-template name="get_node">
			<xsl:with-param name="node_content">
				<xsl:apply-templates select="condition"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="condition">
		<xsl:call-template name="get_array"/>
	</xsl:template>
	<!-- ============================================================================ -->
	<!-- event_trigger -->
	<xsl:template match="event_trigger">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
			<xsl:with-param name="node_content">
				<xsl:apply-templates select="event"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="event">
		<xsl:call-template name="get_array">
			<xsl:with-param name="sub_obj">
				<xsl:apply-templates select="event_func"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="event_func">
		<xsl:call-template name="get_array">
			<xsl:with-param name="sub_obj">
				<xsl:if test="func_param">
					<xsl:apply-templates select="func_param"/>
				</xsl:if>
				<xsl:if test="func_param and func_return">
					<xsl:text>,</xsl:text>
				</xsl:if>
				<xsl:if test="func_return">
					<xsl:apply-templates select="func_return"/>
				</xsl:if>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="func_return">
		<xsl:call-template name="get_array">
			<xsl:with-param name="sub_obj">
				<xsl:if test="event_func">
					<xsl:apply-templates select="event_func"/>
				</xsl:if>
				<xsl:if test="event_func and action">
					<xsl:text>,</xsl:text>
				</xsl:if>
				<xsl:if test="action">
					<xsl:apply-templates select="action"/>
				</xsl:if>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="func_param">
		<xsl:call-template name="get_array"/>
	</xsl:template>
	<xsl:template match="action">
		<xsl:call-template name="get_node">
			<xsl:with-param name="additional_comma">false</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<!-- ============================================================================ -->
	<!-- functional template -->
	<xsl:template name="get_att">
        <xsl:for-each select="@*">
			"<xsl:value-of select="local-name()"/>":"<xsl:value-of select="current()"/>"<xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="get_node">
		<xsl:param name="node_content"/>
		<xsl:param name="additional_comma">true</xsl:param>
		"<xsl:value-of select="local-name()"/>":{
		<xsl:call-template name="get_att"/>
		<xsl:if test="$additional_comma = 'true'">
			<xsl:text>,</xsl:text>
        </xsl:if>
		<xsl:copy-of select="$node_content"/>
		}
	</xsl:template>
	<xsl:template name="get_array">
		<xsl:param name="sub_obj"/>
		<xsl:if test="position() = 1">
			"<xsl:value-of select="local-name()"/>":[
		</xsl:if>
        {<xsl:call-template name="get_att"/>
        <xsl:if test="$sub_obj != ''">
			<xsl:text>,</xsl:text>
			<xsl:copy-of select="$sub_obj"/>
        </xsl:if>
		}<xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
		<xsl:if test="position() = last()">]</xsl:if>
	</xsl:template>
	<!-- ============================================================================ -->
</xsl:stylesheet>

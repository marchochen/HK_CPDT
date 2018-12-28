<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_const.xsl"/>
<xsl:variable name="img_path"><xsl:value-of select="$wb_img_path"/></xsl:variable>
<xsl:output omit-xml-declaration="yes"/>

	<!-- =============================================================== -->
	<xsl:template match="/tableofcontents">
		<xsl:call-template name="tree_json"/>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="tree_json">	
		<xsl:variable name="relation">
			<xsl:apply-templates select="item" mode="relation"/>
		</xsl:variable>
		<xsl:variable name="folder">
			<xsl:apply-templates select="item" mode="folder"/>
		</xsl:variable>
		<xsl:variable name="structure">
			<xsl:apply-templates select="item" mode="structure"/>
		</xsl:variable>
		{
			folder_structure:{
					id:"<xsl:value-of select="@identifier"/>",
					text:"<xsl:value-of select="@title"/>",
					leaf:false,
					 children:[
						<xsl:value-of select="normalize-space($folder)"/>
						]
			},
			structure:[
					<xsl:value-of select="normalize-space($structure)"/>
			],
			mod_relation:[
					<xsl:value-of select="substring(normalize-space($relation),0,string-length(normalize-space($relation)))"/>
					
			]
		}
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="folder">
		<xsl:choose>
			<xsl:when test="itemtype = 'FDR'">
				{
					id:"<xsl:value-of select="@identifier"/>",
					text:"<xsl:value-of select="@title"/>",
					leaf:false,
					itemtype:"<xsl:value-of select="itemtype"/>",
					 children:[
						<xsl:apply-templates select="item" mode="folder"/>
					]
				}
				<xsl:if test="position() !=last()">,</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="structure">
		{
							<xsl:if test="@identifierref">
								id:<xsl:value-of select="@identifierref"/>,
							</xsl:if>
							identifier:"<xsl:value-of select="@identifier"/>",
							title:"<xsl:value-of select="@title"/>",
							itemtype:"<xsl:value-of select="itemtype"/>",
							restype:"<xsl:value-of select="restype"/>",
							children:[
								<xsl:apply-templates select="item" mode="structure"/>
							]
		}
		<xsl:if test="position() !=last()">,</xsl:if>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="item" mode="relation">
		<xsl:if test="itemtype = 'MOD'">
			{
				mod_id:<xsl:value-of select="@identifierref"/>,
				folder:"<xsl:value-of select="../@identifier"/>"	
		</xsl:if>	
			<xsl:apply-templates select="item" mode="relation"/>	
		<xsl:if test="itemtype = 'MOD'">			
			},
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:param name="lang"/>
	<xsl:output omit-xml-declaration="yes"/>
	
	<xsl:template match="/">
		  [
		   {
		  "name":"personal",
		  "attr_lst":[
		  <xsl:apply-templates select="user_management/user_profile/profile_attributes/*[@group='personal']" mode="getList"/>
		  ]
		  },
		  {
		  "name":"contact",
		  "attr_lst":[
		  <xsl:apply-templates select="user_management/user_profile/profile_attributes/*[@group='contact']" mode="getList"/>
		  ]
		  },
		  {
		  "name":"position",
		  "attr_lst":[
		  <xsl:apply-templates select="user_management/user_profile/profile_attributes/*[@group='position']" mode="getList"/>
		  ]
		  },
		  {
		  "name":"other",
		  "attr_lst":[
		  <xsl:apply-templates select="user_management/user_profile/profile_attributes/*[@group='other']" mode="getList"/>
		  ]
		  }
		  <!--,
		  {
		  "name":system,
		  lable:系统保留,
		  attr_lst:[
		  <xsl:apply-templates select="user_management/user_profile/profile_attributes/*[@group='system']" mode="getList"></xsl:apply-templates>
		  ]
		  }
		-->
		  ]
	</xsl:template>
	<xsl:template match="*" mode="getList">
		<!--<xsl:if test="@group='personal'">-->
       {
        "name":"<xsl:value-of select="local-name()"/>",
        "label":"<xsl:value-of select="label[@xml:lang=$lang]"/>",
        <xsl:for-each select="@*">
          "<xsl:value-of select="local-name()"/>":"<xsl:value-of select="current()"/>"
          <xsl:if test="position() != last()">,</xsl:if>
			<xsl:if test="position() = last() and count(../option_list) > 0">,</xsl:if>
		</xsl:for-each>
		<xsl:if test="option_list">
          "option_lst":[
          <xsl:apply-templates select="option_list/*" mode="getOptionList"/>
          ]
        </xsl:if>
        
        }
        <xsl:if test="position() != last()">,</xsl:if>
		<!-- </xsl:if>-->
		<!-- <xsl:if test="@group='contact'">
      {
      name:<xsl:value-of select="local-name()"/>,
      label:<xsl:value-of select="label[@xml:lang=$language]"/>,
      <xsl:for-each select="@*">
        <xsl:value-of select="local-name()"></xsl:value-of>:<xsl:value-of select="current()"></xsl:value-of>,
      </xsl:for-each>
      <xsl:if test="option_list">
        option_lst:[
        <xsl:apply-templates select="option_list/*" mode="getOptionList"></xsl:apply-templates>
        ]
      </xsl:if>

      }
    </xsl:if>-->
	</xsl:template>
	<xsl:template match="*" mode="getOptionList">
    {
      "value":"<xsl:value-of select="@id"/>",
      "label":"<xsl:value-of select="label[@xml:lang=$lang]"/>"
    }<xsl:if test="position() != last()">,</xsl:if>
	</xsl:template>
</xsl:stylesheet>

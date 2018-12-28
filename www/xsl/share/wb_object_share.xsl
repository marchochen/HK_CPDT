<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!-- ========================================================= -->
	<xsl:template match="object[@type = 'image/gif' or @type = 'image/jpg' or @type = 'image/png']">
		<xsl:choose>
			<xsl:when test="name(..) = 'interaction' ">
				<img src="../{concat('resource/',../../../@id, '/', @data)}"/>
			</xsl:when>
			<xsl:when test="name(..) = 'item' or name(..) = 'option'">
				<img src="../{concat('resource/',../../../../@id, '/', @data)}"/>
			</xsl:when>
			<xsl:otherwise>
				<img src="../{concat('resource/',../../@id, '/', @data)}"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ========================================================= -->	
	<xsl:template match="object[@type = 'image/gif' or @type = 'image/jpg' or @type = 'image/png']" mode="java">
		<xsl:choose>
			<xsl:when test="name(..) = 'interaction' ">document.write('<img src="../{concat('resource/',../../../@id, '/', @data)}"/>')</xsl:when>
			<xsl:when test="name(..) = 'item' or name(..) = 'option'">document.write('<img src="../{concat('resource/',../../../../@id, '/', @data)}"/>')</xsl:when>
			<xsl:otherwise>document.write('<img src="../{concat('resource/',../../@id, '/', @data)}"/>')</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ========================================================= -->	
	<xsl:template match="object[@type = 'application/x-shockwave-flash']">
		<xsl:param name="width"/>
		<xsl:param name="height"/>
		<xsl:choose>
			<xsl:when test="name(..) = 'interaction' ">
				<xsl:variable name="qid" select="../../../@id"/>
				<xsl:variable name="m_width">
					<xsl:choose>
						<xsl:when test="../../media">
							<xsl:value-of select="../../media/@width"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="m_height">
					<xsl:choose>
						<xsl:when test="../../media">
							<xsl:value-of select="../../media/@height"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="filename">../<xsl:value-of select="concat('resource/', $qid, '/', @data)"/>
				</xsl:variable>
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0" id="media_{$qid}_{../@order}" name="media_{$qid}_{../@order}" width="{$m_width}" height="{$m_height}">
					<param name="movie" value="{$filename}"/>
					<param name="quality" value="high"/>
					<param name="swLiveConnect" value="true"/>
					<embed src="{$filename}" quality="high" swliveconnect="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" name="media_{$qid}_{../@order}" width="{$m_width}" height="{$m_height}"/>
				</object>
			</xsl:when>
			<xsl:when test="name(..) = 'item'  or name(..) = 'option'">
				<xsl:variable name="qid" select="../../../../@id"/>
				<xsl:variable name="m_width">
					<xsl:choose>
						<xsl:when test="../../../media">
							<xsl:value-of select="../../../media/@width"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="m_height">
					<xsl:choose>
						<xsl:when test="../../../media">
							<xsl:value-of select="../../../media/@height"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="filename">../<xsl:value-of select="concat('resource/', $qid, '/', @data)"/>
				</xsl:variable>
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0" id="media_{$qid}_{../@id}" name="media_{$qid}_{../@id}" width="{$m_width}" height="{$m_height}">
					<param name="movie" value="{$filename}"/>
					<param name="quality" value="high"/>
					<param name="swLiveConnect" value="true"/>
					<embed src="{$filename}" quality="high" swliveconnect="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" name="media_{$qid}_{../@id}" width="{$m_width}" height="{$m_height}"/>
				</object>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="qid" select="../../@id"/>
				<xsl:variable name="m_width">
					<xsl:choose>
						<xsl:when test="$width = '' ">300</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$width"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="m_height">
					<xsl:choose>
						<xsl:when test="$height= '' ">300</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$height"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="filename">../<xsl:value-of select="concat('resource/', $qid, '/', @data)"/>
				</xsl:variable>
				<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0" id="media_{$qid}" name="media_{$qid}" width="{$m_width}" height="{$m_height}">
					<param name="movie" value="{$filename}"/>
					<param name="quality" value="high"/>
					<param name="swLiveConnect" value="true"/>
					<embed src="{$filename}" quality="high" swliveconnect="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" name="media_{$qid}" width="{$m_width}" height="{$m_height}"/>
				</object>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ========================================================= -->	
	<xsl:template match="object[@type = 'application/x-shockwave-flash']" mode="java">
		<xsl:param name="width"/>
		<xsl:param name="height"/>
		<xsl:choose>
			<xsl:when test="name(..) = 'interaction' ">
				<xsl:variable name="qid" select="../../../@id"/>
				<xsl:variable name="m_width">
					<xsl:choose>
						<xsl:when test="../../media">
							<xsl:value-of select="../../media/@width"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="m_height">
					<xsl:choose>
						<xsl:when test="../../media">
							<xsl:value-of select="../../media/@height"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="filename">../<xsl:value-of select="concat('resource/', $qid, '/', @data)"/>
				</xsl:variable>
		
		document.write(' <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0" id="media_{$qid}_{../@order}" name="media_{$qid}_{../@order}" width="{$m_width}" height="{$m_height}">')
		document.write('<param name="movie" value="{$filename}"/>')
		document.write('<param name="quality" value="high"/>')
		document.write('<param name="swLiveConnect" value="true"/>')
		document.write('<embed src="{$filename}" quality="high" swliveconnect="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" name="media_{$qid}_{../@order}" width="{$m_width}" height="{$m_height}">')
		document.write('</embed>')
		document.write('</object>')
		
	</xsl:when>
			<xsl:when test="name(..) = 'item'  or name(..) = 'option'">
				<xsl:variable name="qid" select="../../../../@id"/>
				<xsl:variable name="m_width">
					<xsl:choose>
						<xsl:when test="../../../media">
							<xsl:value-of select="../../../media/@width"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="m_height">
					<xsl:choose>
						<xsl:when test="../../../media">
							<xsl:value-of select="../../../media/@height"/>
						</xsl:when>
						<xsl:otherwise>165</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="filename">../<xsl:value-of select="concat('resource/', $qid, '/', @data)"/>
				</xsl:variable>
			
			document.write('<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0" id="media_{$qid}_{../@id}" name="media_{$qid}_{../@id}" width="{$m_width}" height="{$m_height}">')
			document.write('<param name="movie" value="{$filename}"/>')
			document.write('<param name="quality" value="high"/>')
			document.write('<param name="swLiveConnect" value="true"/>')
			document.write('<embed src="{$filename}" quality="high" swliveconnect="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" name="media_{$qid}_{../@id}" width="{$m_width}" height="{$m_height}">')
			document.write('</embed>')
			document.write('</object>')
		
	</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="qid" select="../../@id"/>
				<xsl:variable name="m_width">
					<xsl:choose>
						<xsl:when test="$width = '' ">300</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$width"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="m_height">
					<xsl:choose>
						<xsl:when test="$height= '' ">300</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$height"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="filename">../<xsl:value-of select="concat('resource/', $qid, '/', @data)"/>
				</xsl:variable>
		
		document.write('<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0" id="media_{$qid}" name="media_{$qid}" width="{$m_width}" height="{$m_height}">')
		document.write('<param name="movie" value="{$filename}"/>')
		document.write('<param name="quality" value="high"/>')
		document.write('<param name="swLiveConnect" value="true"/>')
		document.write('<embed src="{$filename}" quality="high" swliveconnect="true" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash" name="media_{$qid}" width="{$m_width}" height="{$m_height}">')
		document.write('</embed>')
		document.write('</object>')
		
	</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- ========================================================= -->	
	<xsl:template match="object[@type = 'text/ezmath']">
		<embed>
			<xsl:attribute name="type">text/ezmath</xsl:attribute>
			<xsl:attribute name="pluginspage">http://www.w3.org/People/Raggett/EzMath</xsl:attribute>
			<xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute>
			<xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
			<xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
			<xsl:attribute name="alt"><xsl:value-of select="."/></xsl:attribute>
		</embed>
	</xsl:template>
	<!-- ========================================================= -->	
	<xsl:template match="object[@type = 'text/ezmath']" mode="java">
	document.write('<embed type="text/ezmath" pluginspage="http://www.w3.org/People/Raggett/EzMath" align="{@align}" width="{@width}" height="{@height}" alt="{.}">')
	document.write('</embed>')
</xsl:template>
	<!-- ========================================================= -->
</xsl:stylesheet>

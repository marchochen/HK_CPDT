<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:template match="/">
		<html>
			<xsl:apply-templates select="file_submit"/>
		</html>
	</xsl:template>
	
	<xsl:template match="file_submit">
			<head>
				<title>
					<xsl:value-of select="$wb_wizbank"/>
				</title>
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					function start_file_submit(){
						if (parent.main && parent.main.submit_que_file) {
							parent.main.submit_que_file(']]><xsl:value-of select="$wb_lang"/><![CDATA[');
						}
					}
				]]></SCRIPT>
			</head>
			<body onload="start_file_submit()">
				<table cellpadding="10" cellspacing="0" border="0">
					<tr>
						<td>
							<font size="2">
								<xsl:choose>
									<xsl:when test="$wb_lang = 'ch'">下載中，請稍候...</xsl:when>
									<xsl:when test="$wb_lang = 'gb'">装载中，请稍候...</xsl:when>								
									<xsl:otherwise>Loading... please wait.</xsl:otherwise>
								</xsl:choose>
							</font>
						</td>
					</tr>
				</table>
			</body>
	</xsl:template>
</xsl:stylesheet>

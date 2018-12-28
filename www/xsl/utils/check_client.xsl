<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:template name="check_client">
		<xsl:param name="lab_check_client"/>
		<xsl:param name="img_path"/>
		<xsl:param name="check_item"/>

		<script language="JavaScript" src="../htm/client_check/utils.js"/>
		<!-- <table cellspacing="0" cellpadding="0" border="0">
			<tr>
				<td id="client_check_td_above">
					<img border="0" height="1" src="{$img_path}tp.gif" width="1"/>
				</td>
			</tr>
			<tr>
				<td nowrap="nowrap">
					<a class="ClientCheckText" href="../htm/client_check/{$wb_cur_lang}/index.htm?cekItem={$check_item}" id="client_check_link" target="_blank"/>
				</td>
			</tr>
			<tr>
				<td id="client_check_td_below">
					<img border="0" height="1" src="{$img_path}tp.gif" width="1"/>
				</td>
			</tr>
		</table> -->
		<noscript>
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<td height="20">
						<img border="0" height="1" src="{$img_path}tp.gif" width="1"/>
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap">
						<a class="ClientCheckText" href="../htm/client_check/{$wb_cur_lang}/index.htm" target="_blank">
							<xsl:value-of select="$lab_check_client"/>
						</a>
					</td>
				</tr>
				<tr>
					<td height="20">
						<img border="0" height="1" src="{$img_path}tp.gif" width="1"/>
					</td>
				</tr>
			</table>
		</noscript>
	</xsl:template>
</xsl:stylesheet>

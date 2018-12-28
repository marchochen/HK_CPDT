<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/que_action_init_share.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="new_css"/>
			<xsl:choose>
				<xsl:when test="//interaction[@type='MC'] or /question/header[@type='MC']">
					<xsl:call-template name="que_mc">
						<xsl:with-param name="save_function">javascript:mc.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="//question/@mod_type"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="width" select="$wb_frame_table_width"/>
						<xsl:with-param name="mode">UPD</xsl:with-param>
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 弹出页面，需要引入jquery -->
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//interaction[@type='FB']">
					<xsl:call-template name="que_fb">
						<xsl:with-param name="save_function">javascript:fb.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="//question/@mod_type"/>','',true)</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="width" select="$wb_frame_table_width"/>
						<xsl:with-param name="mode">UPD</xsl:with-param>
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 弹出页面，需要引入jquery -->
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//interaction[@type='ES']">
					<xsl:call-template name="que_es">
						<xsl:with-param name="save_function">javascript:es.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="//question/@mod_type"/>',true)</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="width" select="$wb_frame_table_width"/>
						<xsl:with-param name="mode">UPD</xsl:with-param>
						<xsl:with-param name="isOpen">true</xsl:with-param>
					</xsl:call-template>
				</xsl:when>					
				<xsl:when test="//interaction[@type='MT']">
					<xsl:call-template name="que_mt">
						<xsl:with-param name="save_function">javascript:mt.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>',true)</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="width" select="$wb_frame_table_width"/>
						<xsl:with-param name="isOpen">true</xsl:with-param>
						<xsl:with-param name="mode">UPD</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//interaction[@type='TF']">
					<xsl:call-template name="que_tf">
						<xsl:with-param name="save_function">javascript:tf.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>',true)</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="width" select="$wb_frame_table_width"/>
						<xsl:with-param name="isOpen">true</xsl:with-param>
						<xsl:with-param name="mode">UPD</xsl:with-param>
					</xsl:call-template>
				</xsl:when>				
				<xsl:when test="//header[@type='FSC']">
					<xsl:call-template name="que_fixed_sc">
						<xsl:with-param name="save_function">javascript:sc.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="width" select="$wb_frame_table_width"/>
						<xsl:with-param name="mode">UPD</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//header[@type='DSC']">
					<xsl:call-template name="que_dna_sc">
						<xsl:with-param name="save_function">javascript:sc.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.back()</xsl:with-param>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="width" select="$wb_frame_table_width"/>
						<xsl:with-param name="mode">UPD</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</html>
	</xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="share/que_action_init_share.xsl"/>
	<xsl:variable name="wb_gen_table_width" select="$wb_frame_table_width"/>
	<xsl:variable name="edit_type">RES_UPD</xsl:variable>
    <xsl:output method="html" doctype-system="about:legacy-compat" encoding="UTF-8" indent="yes" />
    <!-- =============================================================== -->
	<xsl:template match="/">
		<html>
			<head>
				<script type="text/javascript" src="../static/js/jquery.js"></script>
				<script type="text/javascript" src="../static/js/jquery.dialogue.js"></script>
				<script type="text/javascript" src="../static/js/jquery.qtip/jquery.qtip.js"></script>
				<script type="text/javascript" src="../static/js/cwn_utils.js"></script>
				<script type="text/javascript" src="../static/js/i18n/{$wb_cur_lang}/global_{$wb_cur_lang}.js"></script>
				<link rel="stylesheet" href="../static/js/jquery.qtip/jquery.qtip.css" />
				<xsl:call-template name="new_css"/>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
					//由于xsl 中 alert 比较多 样式比较丑，所以 在这里直接替换掉
				    window.alert = function(text){
					   Dialog.alert(text);
				    }
				]]></SCRIPT>
			</head>
			<xsl:choose>
				<xsl:when test="//question_form[@type='MC']">
					<xsl:call-template name="que_mc">
						<xsl:with-param name="mode">INS</xsl:with-param>
						<xsl:with-param name="save_function">javascript:mc.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="//question_form/@mod_type"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_success">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_failure">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="upd_type" select="$edit_type" />
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 是否是弹框 -->
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//question_form[@type='TF']">
					<xsl:call-template name="que_tf">
						<xsl:with-param name="mode">INS</xsl:with-param>
						<xsl:with-param name="save_function">javascript:tf.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="//question_form/@mod_type"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_success">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_failure">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="upd_type" select="$edit_type" />
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 是否是弹框 -->
					</xsl:call-template>
				</xsl:when>				
				<xsl:when test="//question_form[@type='FB']">
					<xsl:call-template name="que_fb">
						<xsl:with-param name="mode">INS</xsl:with-param>
						<xsl:with-param name="save_function">javascript:fb.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="//question_form/@mod_type"/>','',true)</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_success">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_failure">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="upd_type" select="$edit_type" />
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 是否是弹框 -->
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//question_form[@type='ES']">
					<xsl:call-template name="que_es">
						<xsl:with-param name="mode">INS</xsl:with-param>
						<xsl:with-param name="save_function">javascript:es.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','<xsl:value-of select="//question_form/@mod_type"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_success">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_failure">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="upd_type" select="$edit_type" />
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 是否是弹框 -->
					</xsl:call-template>
				</xsl:when>									
				<xsl:when test="//question_form[@type='MT']">
					<xsl:call-template name="que_mt">
						<xsl:with-param name="mode">INS</xsl:with-param>
						<xsl:with-param name="save_function">javascript:mt.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_success">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_failure">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="upd_type" select="$edit_type" />
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 是否是弹框 -->
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//question_form[@type='FSC']">
					<xsl:call-template name="que_fixed_sc">
						<xsl:with-param name="mode">INS</xsl:with-param>
						<xsl:with-param name="save_function">javascript:sc.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_success">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_failure">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="upd_type" select="$edit_type" />
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 是否是弹框 -->
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="//question_form[@type='DSC']">
					<xsl:call-template name="que_dna_sc">
						<xsl:with-param name="mode">INS</xsl:with-param>
						<xsl:with-param name="save_function">javascript:sc.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>')</xsl:with-param>
						<xsl:with-param name="cancel_function">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_success">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="url_failure">javascript:history.go(-1)</xsl:with-param>
						<xsl:with-param name="width" select="$wb_gen_table_width"/>
						<xsl:with-param name="header">NO</xsl:with-param>
						<xsl:with-param name="upd_type" select="$edit_type" />
						<xsl:with-param name="isOpen">true</xsl:with-param><!-- 是否是弹框 -->
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
</xsl:stylesheet>
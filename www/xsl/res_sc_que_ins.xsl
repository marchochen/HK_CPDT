<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:import href="wb_const.xsl"/>
<xsl:import href="utils/wb_utils.xsl"/>
<xsl:import href="share/wb_layout_share.xsl"/>
<xsl:import href="share/wb_object_share.xsl"/>
<!--xsl:import href="que_ins_utils.xsl"/-->
<xsl:import href="share/que_action_init_share.xsl"/>
<xsl:output indent="yes"/>
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
					<!--<xsl:with-param name="save_function">javascript:alert(document.frmXml.url_success.value);</xsl:with-param>-->
					<xsl:with-param name="save_function">javascript:gen_set_cookie('url_success','javascript:window.parent.location.reload();self.close()');mc.sendFrm(document.frmXml,'<xsl:value-of select="$wb_lang"/>','true');</xsl:with-param>
				  	<xsl:with-param name="cancel_function">javascript:window.history.back();</xsl:with-param>
				  	<xsl:with-param name="url_success">../htm/close_and_reload_window.htm</xsl:with-param>
				  	<xsl:with-param name="url_failure">'../htm/close_window.htm</xsl:with-param>
				  	<xsl:with-param name="width" select="$wb_frame_table_width"/>
				  	<xsl:with-param name="header">NO</xsl:with-param>
			  		<xsl:with-param name="isOpen">true</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</html>
</xsl:template>
<!-- =============================================================== -->
</xsl:stylesheet>

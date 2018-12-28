<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/escape_js.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	<!-- ============================================================= -->
	<xsl:variable name="rsv_id" select="fm/reservation/@id"/>
	<!-- ============================================================= -->
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<TITLE>
					<xsl:value-of select="$wb_wizbank"/>
				</TITLE>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_fm.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_item.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
							
				var return_url = wb_utils_fm_get_cookie('rsv_return_url')
				var itm_id = wb_utils_fm_get_cookie('rsv_itm_id')
				var url_success = wb_utils_fm_get_cookie('url_success')

				if (return_url != '' && return_url != null)	 {
					return_url += '&' + ']]><xsl:apply-templates select="fm/reservation/url_param_list/url_param"/><![CDATA['
					if(url_success != '' && url_success != null){
						return_url += '&' + 'url_success=' + escape(setUrlParam('rsv_id',']]><xsl:value-of select="$rsv_id"/><![CDATA[',url_success))
						if(wb_utils_fm_get_cookie('cur_rsv_id')==''){
							wb_utils_fm_set_cookie('work_rsv_id',']]><xsl:value-of select="$rsv_id"/><![CDATA[')
						}
					}else{
						return_url += '&' + 'url_success=' + '../htm/close_window.htm';
					}
					wb_utils_fm_set_cookie('cur_rsv_id','')
					window.parent.location = return_url
					/*
					return_url += '&' + 'url_success=' + escape(window.parent.opener.location)
					return_url += '&' + 'url_failure=' + escape(window.parent.opener.location)
					if (window.parent.opener != null){
						window.parent.opener.location= return_url
						window.parent.close()
					}
					*/
				}else{
					if(wb_utils_fm_get_cookie('cur_rsv_id')=="" && wb_utils_fm_get_cookie('work_rsv_id')=="")
						wb_utils_fm_set_cookie('work_rsv_id',']]><xsl:value-of select="$rsv_id"/><![CDATA[')
					wb_utils_fm_set_cookie('cur_rsv_id','')
					wbFmGetReservationDetails(]]><xsl:value-of select="$rsv_id"/><![CDATA[)				
				}		
			]]></SCRIPT>
			<xsl:call-template name="new_css"/>
			</head>
		</html>
	</xsl:template>
	<!-- ============================================================= -->
	<xsl:template match="url_param">
		<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="name"/></xsl:with-param></xsl:call-template>=<xsl:call-template name="escape_js"><xsl:with-param name="input_str"><xsl:value-of select="value"/></xsl:with-param></xsl:call-template><xsl:if test="position() != last()">&amp;</xsl:if>
	</xsl:template>
	<!-- ============================================================= -->
</xsl:stylesheet>

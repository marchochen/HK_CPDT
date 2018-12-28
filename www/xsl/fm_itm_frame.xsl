<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
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
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			var isExcludes = getUrlParam('isExcludes');
				fm = new wbFm(isExcludes);		
			]]><xsl:choose>
						<xsl:when test="/fm/reservation_cart/facility_schedule_list/facility_type/facility_schedule"><![CDATA[
					wb_utils_fm_set_cookie('rsv_return_url',wb_utils_fm_get_cookie('old_rsv_return_url'))
					wb_utils_fm_set_cookie('rsv_itm_id',wb_utils_fm_get_cookie('old_rsv_itm_id'))
					wb_utils_fm_set_cookie('rsv_itm_title',wb_utils_fm_get_cookie('old_rsv_itm_title'))
					wb_utils_fm_set_cookie('work_rsv_itm_title',wb_utils_fm_get_cookie('old_work_rsv_itm_title'))
					wb_utils_fm_set_cookie('url_success',wb_utils_fm_get_cookie('old_url_success'))
					wb_utils_fm_set_cookie('cur_rsv_id',wb_utils_fm_get_cookie('old_cur_rsv_id'))
					wb_utils_fm_set_cookie('work_rsv_id',wb_utils_fm_get_cookie('old_work_rsv_id'))
					wb_utils_fm_set_cookie('cart',wb_utils_fm_get_cookie('old_cart'))
					var url = _wbFmGetCartURL();wb_utils_fm_set_cookie("get_cart","true");
					]]></xsl:when>
						<xsl:otherwise><![CDATA[
					var url
					wb_utils_fm_set_cookie("get_cart","");
					
					if (getUrlParam('rsv_id') != '' && getUrlParam('rsv_id') > 0){
						 url = fm.get_rsv_details_url(getUrlParam('rsv_id'))
					}else{
						 url = fm.get_facility_list_url('','fm_cart_add.xsl')
					}
					]]></xsl:otherwise>
					</xsl:choose><![CDATA[
					var dummy_url = fm.get_blank_url()
					dummy_url += "&isExcludes=true"
					url += "&isExcludes=true"
		]]></SCRIPT>
			<xsl:call-template name="new_css"/>
			</head>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			var str=''
			str+='<frameset rows="0,*" framespacing="0" frameborder="0">'
    			str+='<frame name="fm_dummy" src="'+ dummy_url+'" marginwidth="0" marginheight="0" scrolling="no" frameborder="0" >'
    			str+='<frame name="fm_content" src="' + url +'" marginwidth="0" marginheight="0" scrolling="auto" frameborder="0" >'
			str+='</frameset>'

			with(document){
				document.write(str)
			}
		]]></SCRIPT>
		</html>
	</xsl:template>
</xsl:stylesheet>

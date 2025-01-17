<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:import href="utils/wb_css.xsl"/>
	<xsl:output indent="yes"/>
	<xsl:variable name="charset" select="/objective_list/@charset"/>
	<xsl:variable name="que_obj_id_syb" select="/objective_list/header/node[@type='SYB'][position() = last()]/@id"/>
	<xsl:variable name="que_obj_id_ass" select="/objective_list/header/node[@type='ASS']/@id "/>
	<xsl:variable name="que_obj_id" select="/objective_list/header/node[position() = last()]/@id "/>
	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
				<TITLE>
					<xsl:value-of select="$wb_wizbank"/>
				</TITLE>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_course.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}gen_utils.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_objective.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_question.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}que_send_mc.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}que_send_mt.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}que_send_fb.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_media.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_resource.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}urlparam.js"/>
				<script language="JavaScript" type="text/javascript" src="{$wb_js_path}wb_utils.js"/>
				<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
				<script language="JavaScript"><![CDATA[<!--	
	course_lst = new wbCourse
	obj = new wbObjective
	que = new wbQuestion
	mc = new wbMC;
	mt = new wbMT;
	fb = new wbFB;
	res = new wbResource
	
	que_id = getUrlParam('que_id')
	gPrivilege	= getUrlParam('res_privilege')	
	gOrder		= getUrlParam('sort_order')
	gCol		= getUrlParam('sort_col')	
	gDifficulty	= getUrlParam('res_difficulty')
	gType		= getUrlParam('res_type')
	gSubType	= getUrlParam('res_subtype')
	gBegin		= getUrlParam('begin_num')
	gEnd		= getUrlParam('end_num')

	
	if(gOrder == null || gOrder == ''){
		gOrder = 'DESC'
	}	

	function resize_fs(h){
		if(document.all){
			document.all['fs'].rows = h + ",*"
		}else if(document.getElementById != null){
			document.getElementById("fs").rows = h + ",*"
		}
	}	
	
	function go_pick(){
	var cookie_tcr_id = wb_utils_get_cookie('tcr_id')
	window.location.href = obj.pick_obj_url(parent.hidden.document.frmXml.res_type.value, cookie_tcr_id);
	}

	//-->
	]]></script>
			</head>
			<xsl:call-template name="new_css"/>
			<script language="JavaScript"><![CDATA[<!--	
	var url_navigation_get= wb_utils_invoke_servlet('cmd','get_prof','stylesheet','res_add_nav.xsl')
	
	str='<frameset border="0" framespacing="0" rows="85,*" cols="*" id="fs">'
	str+='<frame src="'
	str+= url_navigation_get
	str+='" name="nav" frameborder="0" scrolling="NO" />'
	str+='<frameset cols="190,*" rows="*" frameborder="NO">'
	str+='<frame marginwidth="0" marginheight="0" src="'
	str+=obj.get_pick_res_lst_url(que_id,gPrivilege,gOrder,gCol,'',gDifficulty,gType,gSubType)	
	str+='" name="menu" frameborder="NO" marginheight="0" marginwidth="10" noresize="noresize" scrolling="YES" />'	
 	str+='<frame marginwidth="0" marginheight="0" src="'
	str+= obj.pick_res_inst_url();
	str+='" name="content" marginwidth="0" marginheight="0" />'
	str+='</frameset>'
	str+='</frameset>'

with (document){
	document.write(str);

 }
	//-->]]></script>
		</html>
	</xsl:template>
</xsl:stylesheet>

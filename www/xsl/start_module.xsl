<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output method='html' version='1.0' encoding='UTF-8' indent='yes'/>
	<xsl:template name="DOCTYPE">
		<![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"/>]]>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template match="/">
		<xsl:value-of select="document('')/*/xsl:template[@name='DOCTYPE']/node()" disable-output-escaping="yes"/>  
		<html>
			<xsl:call-template name="content"/>
		</html>
	</xsl:template>
	<!-- =============================================================== -->
	<xsl:template name="content">
		<head>
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<meta http-equiv="Cache-Control" content="no-store"/>
			<meta http-equiv="Pragma" content="no-cache"/>
			<meta http-equiv="Expires" content="0"/>
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			<script language="JavaScript" src="{$wb_js_path}gen_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_utils.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}urlparam.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_module.js" type="text/javascript"/>
			<script language="JavaScript" src="{$wb_js_path}wb_track.js" type="text/javascript"/>	
			<script language="Javascript" type="text/javascript" src="{$wb_js_lang_path}wb_label.js"/>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
			module_lst = new wbModule;
			prog_track = new wbTrack;
			]]></SCRIPT>
		
			<script language="JavaScript"><![CDATA[
			mod_id = getUrlParam("mod_id")
			cos_id = getUrlParam("cos_id")
			tpl_use = getUrlParam("tpl_use")
			mod_type_param = getUrlParam("mod_type")
			test_style = getUrlParam("test_style")
			var mod_type='';
			
		
			switch (mod_type_param)
			{
			    
			    case 'VOD':
				  mod_type="VOD";
				  break;
				  
				case 'AICC_AU':
				  mod_type="AICC_AU";
				  break;
				  
				case 'NETG_COK':
				  mod_type="NETG_COK";
				  break;
				  
				case 'SCO':
				  mod_type="SCO";
				  break;
				  
				case 'COURSE':
				  mod_type="COURSE";
				  break;
				  
				case 'TRAINER':
				  mod_type="TRAINER";
				  break;
				  
				case 'PRE_COURSE':
				  mod_type="PRE_COURSE";
				  break;
	
			     case 'SVY':
				  mod_type="SVY";
				  break;
				  
				 case 'VOT':
				  mod_type="VOT";
				  break;
				  
				case 'ASM':
				  mod_type="ASM";
				  break;
				  
				case 'EAS':
				  mod_type="EAS";
				  break;
				  
				case 'EVN':
				  mod_type="EVN";
				  break;
				  
				case 'RDG':
				  mod_type="RDG";
				  break;
				  
				case 'REF':
				  mod_type="REF";
				  break;
				  
				case 'GLO':
				  mod_type="GLO";
				  break;
				  
				  
				case 'TST':
				  mod_type="TST";
				  break;
				  
			    case 'DXT':
				  mod_type="DXT";
				  break;
				  
				case 'STX':
				  mod_type="STX";
				  break;
				  
				case 'EXC':
				  mod_type="EXC";
				  break;
				  
			    case 'GEN':
				  mod_type="GEN";
				  break;
				  
			    case 'ASS':
				  mod_type="ASS";
				  break;
				  
			    case 'CHT':
				  mod_type="CHT";
				  break;
				  
			    case 'VCR':
				  mod_type="VCR";
				  break;
				  
			    case 'FOR':
				  mod_type="FOR";
				  break;
				  
			    case 'FAQ':
				  mod_type="FAQ";
				  break;			  			  			  			  			
			}
			
			tkh_id = ']]><xsl:value-of select="/module/aicc_data/@tkh_id"/><![CDATA['
			usr_id = ']]><xsl:value-of select="/module/cur_usr/@id"/><![CDATA['
			var win_name = 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id;
			if(test_style == 'many')
				tpl_use = 'tst_view_many.xsl';
			wb_utils_set_cookie('mod_id',mod_id)
			
			var is_pause = ']]><xsl:value-of select="//isPause"/><![CDATA['
			
			function get_pause_msg_url(){
				var url = wb_utils_invoke_disp_servlet('module', 'JsonMod.exam.ExamModule', 'cmd', 'show_pause_exam_msg', 'mod_id', mod_id, 'url_success', '../htm/close_window.htm');
				return url;
			}
			
			if (is_pause === 'true') {
				location = get_pause_msg_url();
			} else {
				with (document) {
					var frameset_row = wb_utils_testplayer_fs_row;
					var frameset_progress = '';
					if (mod_type == 'TST' || mod_type == 'DXT') {
						frameset_row = wb_utils_testplayer_fs_row_progress;
						frameset_progress = '<frame  name="progress_bar" scrolling="NO" noresize src="';
						frameset_progress += module_lst.gen_test_progress_url(win_name,test_style);
						frameset_progress += '" marginwidth="0" marginheight="0">';
					} else if (mod_type == 'EVN' || mod_type == 'SVY') {
						frameset_row = wb_utils_testplayer_fs_row_evn;
					}
		
					str = '<frameset rows="' + frameset_row + '" frameborder="0" border="0" framespacing="0" name="all_frame" id="all_frameid">'
					str += frameset_progress
					
					var track_str = ''; 	
					track_str += '<frame frameborder="0" name="track" scrolling="NO" noresize src="'
					track_str += prog_track.module_track_url(usr_id,mod_type,mod_id,cos_id, tkh_id)
					track_str +='" marginwidth="0" marginheight="0">'
					
					var main_str = '';
					main_str +='<frame id="mainid" name="main" scrolling="auto" src="'
					main_str += module_lst.start_content_url(mod_type,mod_id,tpl_use,cos_id, tkh_id, win_name)
					main_str +='" marginwidth="0" marginheight="0">'			
							
					if (mod_type == 'TST' || mod_type == 'DXT') {
						str += main_str + track_str;
					} else if (mod_type == 'EVN' || mod_type == 'SVY') {
						str +=  main_str;
					} else {
						str += track_str + main_str;
					}
							
					str += '<frame name="file_submit" scrolling="NO" noresize src="about:blank" marginwidth="0" marginheight="0">'
					str +='</frameset>'
					//debug  only
					//newwin = window.open('about:blank','win','')
					//newwin.document.write('<textarea rows="10" cols="100">'  + str + '</textarea>')
					document.write(str);
				}
			}
			]]></script>
		</head>
	</xsl:template>
</xsl:stylesheet>

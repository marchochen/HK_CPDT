<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	
	<xsl:variable name="cos_id" select="/module/@course_id" />	
	<xsl:variable name="usr_id" select="/module/cur_usr/@ent_id" />	
	<xsl:variable name="res_id" select="/module/@id" />
	<xsl:variable name="usr" select="/module/cur_usr/@id" />
	<xsl:variable name="video_url">
		<xsl:choose>
			<xsl:when test="/module/header/source/@type = 'URL' or contains(/module/header/source/@type,'ONLINEVIDEO_')">
				<xsl:value-of select="/module/header/source"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat('../resource/', /module/@id, '/', /module/header/source)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	
	<!-- =============================================== -->
	<xsl:template match="/">
		<html>
			<xsl:apply-templates/>
		</html>
	</xsl:template>
	<!-- ========================================================== -->
	<xsl:template match="module">
		<head>
			<meta http-equiv="pragma" content="no-cache"/>
			<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
			<meta http-equiv="Content-Type" content="text/html; charset={$encoding}"/>
			<meta http-equiv="Cache-Control" content="no-store"/>
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
			<script language="Javascript" type="text/javascript" src="../static/js/jquery.js"></script>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				module_lst = new wbModule;
				prog_track = new wbTrack;
				$(function() {
					var type = ']]><xsl:value-of select="/module/header/@res_src_type" /><![CDATA[';
					if(type.indexOf('ONLINEVIDEO_') != -1) {
						$("#my_video_1").remove();
						var html = '<iframe name="api_video" width="100%" height="100%" sandbox="allow-scripts allow-same-origin"'
							html +=	' src="]]><xsl:value-of select="/module/header/src/@link" /><![CDATA["'
							html += ' autoplay="autoplay" class="video-js vjs-default-skin vjs-big-play-centered"/>'
						$("body").append(html);
					}
				});
			]]></SCRIPT>
			
			<!-- 播放器样式 -->
			<link href="../static/js/video/video-js.css" rel="stylesheet" type="text/css" />
			<!-- 如果要支持IE8 -->
			<!-- <script language="Javascript" type="text/javascript" src="../static/js/video/ie8/videojs-ie8.js"></script> -->
			<!-- 播放器js -->
			<script language="Javascript" type="text/javascript" src="../static/js/video/video.js"></script>
			<script language="Javascript" type="text/javascript" src="../static/js/video/video.cwn.js"></script>

		</head>
		<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" style="background:#eff3f5;">
  			<div id='iframe_header'></div>
  			<!-- <iframe frameborder="0" name="track" scrolling="NO" noresize="no" 
  				src="javascript:prog_track.module_track_url(usr_id,mod_type,mod_id,cos_id, tkh_id)"
				 marginwidth="0" marginheight="0" ></iframe> -->
  			
  			<div class="blank_div"></div>
			<div align="center">
				<video id="my_video_1" class="video-js vjs-default-skin vjs-big-play-centered" controls="controls" 
			      autoplay="autoplay" preload="auto" width="100%" height="100%" poster="" oncontextmenu="return false;" >
			    	<source src="{$video_url}" type='video/mp4' />
			    </video>
  			</div>
		  	
		  	<script type="text/javascript">	<![CDATA[ 
		  	
	  			viewUserId = ']]><xsl:value-of select="$usr_id"/><![CDATA[';
	  		    viewCourseId = ']]><xsl:value-of select="$cos_id"/><![CDATA[';
	  			courseNum = ']]><xsl:value-of select="$res_id"/><![CDATA[';
	  			
	  			//跑马灯
	  			//context_text = ']]><xsl:value-of select="$usr"/><![CDATA[';
	  			
	  			videoCwn("my_video_1");
	  			
	  			]]>
			</script>
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
				}
				
				tkh_id = ']]><xsl:value-of select="./aicc_data/@tkh_id"/><![CDATA['
				usr_id = ']]><xsl:value-of select="./cur_usr/@ent_id"/><![CDATA['
				var win_name = 'test_player' + usr_id + '_' + tkh_id + '_' + mod_id;
				wb_utils_set_cookie('mod_id',mod_id)
				
				
				with (document) {
					var frameset_row = wb_utils_testplayer_fs_row;
		
					str = "";	
					
					var track_str = ''; 	
					track_str += '<iframe width="100%" height="60" frameborder="0" name="track" scrolling="NO" noresize src="'
					track_str += prog_track.module_track_url(usr_id,mod_type,mod_id,cos_id, tkh_id)
					track_str +='" marginwidth="0" marginheight="0">'
					
					str += track_str;
							
					str += '<iframe name="file_submit" scrolling="NO" noresize src="about:blank" marginwidth="0" marginheight="0">'
					
					document.getElementById("iframe_header").innerHTML = str
				}
			]]></script>
		</body>
	</xsl:template>
</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:import href="wb_const.xsl"/>
	<xsl:import href="cust/wb_cust_const.xsl"/>
	<xsl:import href="utils/wb_utils.xsl"/>
	<xsl:output indent="yes"/>
	
	<xsl:variable name="cos_id" select="/module/header/@course_id" />	
	<xsl:variable name="usr_id" select="/module/cur_usr/@ent_id" />	
	<xsl:variable name="res_dir_url" select="/module/res_dir_url" />	
	<xsl:variable name="res_id" select="/module/@id" />
	<xsl:variable name="video_url">
		<xsl:choose>
			<xsl:when test="/module/header/source/@type = 'URL' or contains(/module/header/source/@type,'ONLINEVIDEO_')">
				<xsl:value-of select="/module/header/source"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat($res_dir_url, /module/@id, '/', /module/header/source)"/>
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
			<title>
				<xsl:value-of select="$wb_wizbank"/>
			</title>
			
			<!-- 播放器样式 -->
			<link href="../static/js/video/video-js.css" rel="stylesheet" type="text/css" />
			<!-- 如果要支持IE8 -->
		   <!--  <script language="Javascript" type="text/javascript" src="../static/js/video/ie8/videojs-ie8.js"></script>   -->
			<!-- 播放器js -->
			<script language="Javascript" type="text/javascript" src="../static/js/video/video.js"></script>
			<script language="Javascript" type="text/javascript" src="../static/js/video/video.cwn.js"></script>
			<script language="Javascript" type="text/javascript" src="../static/js/jquery.js"></script>
			<SCRIPT LANGUAGE="JavaScript" TYPE="text/javascript"><![CDATA[
				$(function() {
					var type = ']]><xsl:value-of select="/module/header/@res_src_type" /><![CDATA[';
					if(type.indexOf('ONLINEVIDEO_') != -1) {
						$("#my_video_1").remove();
						var html = '<iframe width="100%" height="100%" sandbox="allow-scripts allow-same-origin"'
							html +=	' src="]]><xsl:value-of select="/module/header/src/@link" /><![CDATA["'
							html += ' autoplay="autoplay" class="video-js vjs-default-skin vjs-big-play-centered"/>'
						$("body").append(html);
					}
				});
			]]></SCRIPT>
			
		</head>
		<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
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
		  			
		  			videoCwn("my_video_1");
		  			
		  			]]>
				</script>
		</body>
	</xsl:template>
</xsl:stylesheet>

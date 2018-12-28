<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../common/taglibs.jsp"%>
<html>
<head>
	<title>我的微课程</title>
	<!-- videoJS -->
	<link href="http://vjs.zencdn.net/4.0/video-js.css" rel="stylesheet">
	<script src="http://vjs.zencdn.net/4.0/video.js"></script>
	
</head>

<body align="center">
	<video id="example_video_1" class="video-js vjs-default-skin" 
		   data-setup='{ "controls": true, "autoplay": false, "preload": "auto", "example_option":true}'
		   width="396" height="296"
	       poster="${weixinCoursePic }">
	 	   <source src="${weixinCourseUrl }" type='video/mp4' />
	</video>
</body>

</html>


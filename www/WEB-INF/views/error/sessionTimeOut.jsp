<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.qdb.*"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/meta.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/js/sess_time_out_label.js"></script>
<script type="text/javascript">

	$(function(){
		$("div.losepage_tit").html(labels['${lang}']['session_timeout_message']);
		$("div.losepage_info").html(labels['${lang}']['session_timeout_message_2']);
		$("div.losepage_desc").html(labels['${lang}']['session_timeout_message_3']);
	})
	
	function go() {
		<%
		    String url = qdbAction.static_env.URL_RELOGIN;
		    if (url != null) {
		        if (url.startsWith("http://") || url.startsWith("https://")) {
		        } else {
		            url = url.substring(url.lastIndexOf("../") + 3);
		        }
		    }
		%>
		var url = '<%=url%>';
		if(!url.indexOf("http://") == 0 && !url.indexOf("https://") == 0){
			url ='${ctx}' + '/' + url;
		}
	    window.location.href = url;
	}
	
	function get_losepage_info()
	{
		var second = $('#second').html()-1;
		if(second >= 0){
			$('#second').html(second)
		}else{
			window.clearInterval(int);
			go();
		}
	}
	var int=setInterval(get_losepage_info,1000);
</script>
<meta http-equiv="refresh" content="5;URL=javascript:go()">
</head>
<body>


<div class="">
<div class="clearfix">
<div class="mode">
     <div class="losepage sessionTimeOut losepage-out">
          <div class="losepage_tit"></div>
          <div class="losepage_info"></div>
          <div class="losepage_desc"></div>
     </div>
</div> <!-- mod End-->
</div>

</div> <!-- xyd-wrapper End-->

<div class="xyd-bottom">
	<div class="xyd-bottom-box">
     	<div class="pull-left"><lb:get key="wizbank_version"/></div>
     	<div class="pull-right"><lb:get key="login_web_hk"/> <a class="margin-right15 color-gray999" href="http://www.cyberwisdom.net" title="">cyberwisdom.net</a>  <lb:get key="login_web_ch"/> <a class="color-gray999" href="http://www.cyberwisdom.net.cn" title="">cyberwisdom.net.cn</a></div>
	</div>
</div>
<script type="text/javascript">
if($(".wzb-main")){
	$(".xyd-bottom").hide();
	$(".mode").parent(".clearfix").css('background','#fff');
}
</script>
</body>
</html>
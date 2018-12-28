<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../common/taglibs.jsp"%>

<html>
<head>
	<title>我的微课程</title>
	
	<script language="JavaScript" type="text/javascript" src="${ctx}/js/jquery.js"></script>	
	
    <script type="text/javascript">
    //http://www.cnblogs.com/hoojo/p/longPolling_comet_jquery_iframe_ajax.html  长轮询介绍文章
    
    	var longPollUrl = ctx + '/wizWeixin/testLongPoll.do';
    	
    	//普通轮询 Ajax方式
	    function commonLunxun(){
	        $(function () {
	            window.setInterval(function () {
	                $.get(
	                	longPollUrl, 
	                	{"requestTime": new Date().getTime()}, 
	                    function (data) {
	                    	$("#logs").append("[commonLunxun data: " + data + " ]<br/>");
	                	}
	                );
	            }, 3000);
	            
	        });
	    }
	    
	    //普通轮询 iframe方式
	    function iframLunxun(){
            $(function () {
                window.setInterval(function () {
                    $("#logs").append("[iframLunxun data: " + $($("#frame").get(0).contentDocument).find("body").text() + " ]<br/>");
                    var url = longPollUrl +  '?requestTime='+new Date().getTime();
                    $("#frame").attr("src", url);
                    // 延迟1秒再重新请求
                    window.setTimeout(function () {
                        window.frames["polling"].location.reload();
                    }, 1000);
                }, 5000);
                
            });
	    }
	    
	    //长轮询 iframe方式
	    function iframLongLunxun(){
	    	 $(function () {
        		window.setInterval(function () {
        			var url = longPollUrl +  '?requestTime='+new Date().getTime();
                    var $iframe = $('<iframe name="polling2" style="display: none;" src="' + url + '"></iframe>');
                    $("body").append($iframe);
                
                    $iframe.load(function () {
                        $("#logs").append("[data: " + $($iframe.get(0).contentDocument).find("body").text() + " ]<br/>");
                        $iframe.remove();
                    });
                }, 5000);
                
            });
	    }
	    
	    //长轮询 iframe递归方式
	    function iframDiguiLongLunxun(){
   			var url = longPollUrl +  '?requestTime='+new Date().getTime();
            var $iframe = $('<iframe name="polling2" style="display: none;" src="' + url + '"></iframe>');
            $("body").append($iframe);
        
            $iframe.load(function () {
                $("#logs").append("[data: " + $($iframe.get(0).contentDocument).find("body").text() + " ]<br/>");
                $iframe.remove();
                
                // 递归
                iframDiguiLongLunxun();
            });
	    }
	    
	    //ajax实现长连接
	    function ajaxLongPolling(){
	        $.ajax({
	        	url : longPollUrl,
	            data: {"requestTime": new Date().getTime()},
                dataType: "text",
                timeout: 5000,
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#logs").append("[state: " + textStatus + ", error: " + errorThrown + " ]<br/>");
                    if (textStatus == "timeout") { // 请求超时
                            longPolling(); // 递归调用
                        
                        // 其他错误，如网络错误等
                        } else { 
                            ajaxLongPolling();
                        }
                    },
                success: function (data, textStatus) {
                    $("#logs").append("[state: " + textStatus + ", data: { " + data + "} ]<br/>");
                    
                    //if (textStatus == "success") { // 请求成功
                        ajaxLongPolling();
                    //}
                }
	        });
	    }
	    
    </script>
	
	<script type="text/javascript">
		//commonLunxun();
		//iframLunxun();
		//iframLongLunxun();
		//iframDiguiLongLunxun();
		ajaxLongPolling();
	</script>
	
</head>

<body>
	<iframe id="frame" name="polling" style="display: none;"></iframe>
	<div id="logs"></div>
</body>

</html>


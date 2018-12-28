<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.cw.wizbank.qdb.*"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
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
	//setTimeout(go, 5000);
</script>
<meta http-equiv="refresh" content="5;URL=javascript:go()">
</head>
<body>
	<div id="container">
		<div class="error_msg" align="center">
			<table style="width: 50%; margin: 10px 0px;">
				    <tr><td height="20"> </td></tr>
				<tr>
					<td align="left" class="error-title">
						<wzb:label key="lab_error" /> 
						<wzb:hr width="50%" />
					</td>
				</tr>
				<tr>
						<td align="left" class="error-text"><wzb:label key="session_timeout_message" /></td>
				</tr>
				<tr>
						<td align="left" class="error-text"><wzb:label key="session_timeout_message_2" /></td>

				</tr>
			</table>
		</div>
	</div>
</body>
</html>
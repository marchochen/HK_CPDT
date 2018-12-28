<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ page import="com.cw.wizbank.qdb.*,java.io.*,com.cw.wizbank.util.*"%>
<%
	loginProfile prof = (loginProfile) pageContext.getAttribute("prof");
	String lang = "en-us";
	if (prof != null) {
		lang = prof.cur_lan;
	}
%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	function go() {
		var url = '${ctx}/app/home';
		window.location.href = url;
	}
</script>
</head>
<body>
	<%
		String msg = "server error";
		cwSysMessage ex = (cwSysMessage) request.getAttribute("exception");
		if (ex == null) {
			ex = (cwSysMessage) request.getAttribute("error_ex");
		}
		if (ex != null) {
			msg = ex.getSystemMessage(lang);
		}
	%>
	<div id="container" class="container">
		<div class="error">
			<div class="error-title">
				<lb:get key="lab_error"/>
			</div>
			<wzb:hr />
			<div class="error-text">
<%-- 				<%=msg%> --%>
				server error
			</div>
			<div class="error-text">
				<lb:get key="lab_error_msg"/>
			</div>
		</div>
	</div>
</body>
</html>
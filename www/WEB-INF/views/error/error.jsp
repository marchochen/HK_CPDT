<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/meta.jsp"%>

<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	function go() {
		//var url = '${ctx}/app/home';
		//window.location.href = url;
		history.go(-1);
	}
</script>
</head>
<body>
	<%
		String msg = "server error";
		Exception ex = (Exception) request.getAttribute("exception");
		if (ex == null) {
			ex = (Exception) request.getAttribute("error_ex");
		}
		if (ex != null) {
			msg += ex.getMessage();
		}
	%>
	<div id="container" class="container">
		<div class="error">
			<div class="error-title">
				<lb:get key="lab_error"/>
			</div>
			<wzb:hr />
			<div class="error-text">
				<lb:get key="lab_error"/>
			</div>
			<div class="error-text serverError" style="overflow: scroll; height: 380px;">
				<pre>
<%-- 				<% --%>
<!-- // 					ex.printStackTrace(new PrintWriter(out)); -->
<!-- // 				ex.getMessage(); -->
<%-- 				%> --%>
<%-- 				<%=ex.getMessage() %> --%>
				server error
				</pre>
			</div>
		</div>
	</div>
</body>
</html>
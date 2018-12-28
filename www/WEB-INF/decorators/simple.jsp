<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../views/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="../views/common/meta.jsp"%>
<title><decorator:title default="Wizbank" /></title>
<decorator:head />
<script type="text/javascript" src="${ctx}/static/js/basic.js"></script>
</head>
<body>
	<c:if test="${show_login_header_ind == true}">
		<%@ include file="../views/user/loginHeader.jsp"%>
	</c:if>
	<decorator:body />
	<c:if test="${show_all_footer_ind == true}">
		<%@ include file="../views/user/loginFooter.jsp"%>
	</c:if>
</body>
</html>
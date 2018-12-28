<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp" %>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title></title>
<script type="text/javascript">

</script>
</head>
<body>

<form:form action="add" method="post" commandName="snsDoing" modelAttribute="snsDoing">
	<table width="100%" border="0" cellspacing="0" cellpadding="0"  class="editTable">
		<tr>
			<td align="right">
			标题
			</td>
			<td align="left">
			<c:choose>
				<c:when test="${status eq 'update' }">
				<form:hidden path="s_doi_id" />
				</c:when>
			</c:choose>
			<form:input path="s_doi_title" title="请输入title" id="dt"/>
			</td>
		</tr>
		<tr>
			<td class="left" colspan="2">
			<input type="submit" class="buttonInput" value="保存"/>
			<input type="button" class="buttonInput" value="返回" onclick="history.go(-1)"/></td>
		</tr>

	</table>
	
</form:form>
</body>
</html>
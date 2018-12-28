<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp" %>    
<!DOCTYPE html>
<html>
<head>
<title></title>
</head>
<body>
<div>
<table >
	<tr>
		<td>标题</td>
	</tr>
	<c:forEach items="${page.results}" var="obj" >
		<tr>
			<td>${obj.s_doi_title }</td>

			<td>
				<a href="${ctx }/app/demo/add?id=${obj.s_doi_id}" class="padding">
					更新
				</a>
				<a href="${ctx }/app/demo/delete/${obj.s_doi_id}" class="padding">
					删除
				</a>
			</td>
		</tr>
	</c:forEach>
</table>
</div>
</body>
</html>
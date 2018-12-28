<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="../views/common/taglibs.jsp"%>
<%@ attribute name="key" required="false"%>
<%@ attribute name="text" required="false"%>
<%@ attribute name="title" required="false"%>
<%@ attribute name="style" required="false"%>
<c:set var="default_style" value="wzb-common-text"></c:set>
<c:choose>
	<c:when test="${empty key and empty text}">
		<span class="${(empty style) ? default_style : style}" title="${title}"><jsp:doBody /></span>
	</c:when>
	<c:otherwise>
		<c:if test="${not empty key}"><c:set var="text"><lb:get key="${key}" /></c:set></c:if>
		<span class="${(empty style) ? default_style : style}" title="${title}">${text}</span>
	</c:otherwise>
</c:choose>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="/wizbank/label" prefix="lb"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="wzb"%>
<%@ taglib uri="/wizbank/title" prefix="title"%>


<%@ taglib uri="/wizbank" prefix="wb"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="wb_image" value="${ctx}/static/images/" />
<c:set var="wb_theme" value="${ctx}/static/theme" />
<c:set var="wb_theme_image" value="${ctx}/static/theme/blue/images" />
<c:set var="prof" value="${sessionScope.auth_login_profile}" />
<c:set var="sns_enabled" value="${sessionScope.sns_enabled}"/>
<c:set var="show_login_header_ind" value="${sessionScope.showloginHeaderInd}"/>

<c:set var="hide_header_ind" value="${hideHeaderInd}"/>

<c:set var="show_all_footer_ind" value="${sessionScope.showAllFooterInd}"/>
<c:choose>
	<c:when test="${lang != null}">
		<c:set var="lang" value="${lang}" />
	</c:when>
	<c:otherwise>
		<c:set var="lang" value="${empty prof ? 'en-us' : prof.cur_lan}" />
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${label_lan != null}">
		<c:set var="label_lan" value="${label_lan}" />
	</c:when>
	<c:otherwise>
		<c:set var="label_lan" value="${empty prof ? 'en' : prof.cur_label_lan}" />
	</c:otherwise>
</c:choose>

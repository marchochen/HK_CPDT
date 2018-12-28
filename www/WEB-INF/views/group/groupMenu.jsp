<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<script type="text/javascript">
	$(function() {
		var command = "${command}";
		if(command == "groupDetail"){
			var a_id = Wzb.getUrlParam('a_id');
			$("#"+a_id).addClass("cur");
		}else{
			$("#${command}").addClass("cur");		
		}
	})
</script>

<div class="xyd-sidebar">
<div class="xyd-user">
<c:if test="${isNormal == true }"><div class="xyd-user-box clearfix">
<div class="wzb-user wzb-user82">
     <a href="javascript:;"><img class="wzb-pic" src="${regUser.usr_photo}" alt="<lb:get key="know_ta"/>" /></a>
     <p class="companyInfo"><lb:get key="know_ta"/></p>
     <div class="cornerTL">&nbsp;</div>
     <div class="cornerTR">&nbsp;</div>
     <div class="cornerBL">&nbsp;</div>
     <div class="cornerBR">&nbsp;</div>
</div>

<div class="xyd-user-content">
     <a href="${ctx}/app/personal/${regUser.usr_ent_id}" class="wzb-link04" title="${regUser.usr_display_bil}">${regUser.usr_display_bil}</a>
     <p>${regUser.usg_display_bil}</p>
     <p><c:if test="${regUser.ugr_display_bil != 'Unspecified'}">${regUser.ugr_display_bil}</c:if></p>
</div>
</div> <!-- usermess End--></c:if>
</div>

<ul class="wzb-list-15">
    <li><a id="groupList" href="${ctx}/app/group/groupList/0"><c:choose><c:when test="${isNormal == true }"><lb:get key="group_my"/></c:when><c:otherwise><lb:get key="group_list"/></c:otherwise></c:choose></a></li>
    <c:if test="${isNormal == true }"><li><a id="groupFind" href="${ctx}/app/group/groupFind/0"><lb:get key="group_find"/></a></li></c:if>
    <c:if test="${isNormal == true }"><li><a id="groupOpen" href="${ctx}/app/group/groupOpen/0"><lb:get key="group_openMenu"/></a></li></c:if>
    <li><a id="groupCreate" href="${ctx}/app/group/groupCreate/0"><lb:get key="group_create"/></a></li>
</ul>
</div> <!-- xyd-sidebar End-->

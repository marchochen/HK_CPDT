<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<script type="text/javascript">
	$(function() {
		$("#${command}").addClass("cur");
	})
</script>
<div class="xyd-sidebar">
<div class="xyd-user">
<div class="xyd-user-box clearfix">
<div class="wzb-user wzb-user82">
     <a href="javascript:;"><img class="wzb-pic" src="${ctx}${regUser.usr_photo}" alt="了解Ta" /></a>
     <p class="companyInfo">了解Ta</p>
     <div class="cornerTL">&nbsp;</div>
     <div class="cornerTR">&nbsp;</div>
     <div class="cornerBL">&nbsp;</div>
     <div class="cornerBR">&nbsp;</div>
</div>

<div class="xyd-user-content">
     <a href="javascript:;" class="wzb-link04" title="Andy">${regUser.usr_display_bil}</a>
     <p>${regUser.usg_display_bil}</p>
     <p><c:if test="${regUser.ugr_display_bil != 'Unspecified'}">${regUser.ugr_display_bil}</c:if></p>
</div>
</div> <!-- usermess End-->

</div>

<div class="wzb-model-7">
<div class="wzb-percent-biao">
<c:choose>
	<c:when test="${command == 'myKnow'}">
	        <a href="javascript:;" onclick="changeTab($('li[name=\'my_question\']'))" class="wzb-percent-2">
		   <em>${solved_num}</em>
		   <span><lb:get key="know_my"/><lb:get key="know_question_content"/></span>
		</a>
		<a href="javascript:;" onclick="changeTab($('li[name=\'my_answer\']'))" class="wzb-percent-2">
		   <em>${unsolved_num}</em>
		   <span><lb:get key="know_my"/><lb:get key="know_answer"/></span>
		</a>
	</c:when>
<c:otherwise>
<c:choose>
	<c:when test="${command == 'allKnow'}">
		<a class="wzb-percent-2" href="javascript:;" onclick="changeTab($('li[name=\'SOLVED\']'))">
	</c:when>
	<c:otherwise>
		<a class="wzb-percent-2" href="${ctx}/app/know/allKnow/SOLVED">
	</c:otherwise>
</c:choose>
   <em>${solved_num}</em>
   <span><lb:get key="know_SOLVED"/></span>
</a>

<c:choose>
   <c:when test="${command == 'allKnow'}">
	<a class="wzb-percent-2" href="javascript:;" onclick="changeTab($('li[name=\'UNSOLVED\']'))">
  </c:when>
  <c:otherwise>
	<a class="wzb-percent-2" href="${ctx}/app/know/allKnow/UNSOLVED">
  </c:otherwise>
</c:choose>

   <em>${unsolved_num}</em>
   <span><lb:get key="know_UNSOLVED"/></span>
</a>
</c:otherwise>
</c:choose>
</div>
</div>

<ul class="wzb-list-15">    
    <li><a id="allKnow" href="${ctx }/app/know/allKnow" title=""><i class="fa fa-tasks"></i><lb:get key="status_all"/><lb:get key="know_question"/></a></li>
    <li><a id="myKnow" href="${ctx }/app/know/myKnow" title=""><i class="fa fa-question-circle"></i><lb:get key="know_my"/><lb:get key="know_question"/></a></li>
    <li><a id="askKnow" href="${ctx }/app/know/askKnow" title=""><i class="fa fa-pencil"></i><lb:get key="know_my_want"/></a></li>
    <li><a id="myKnowHelp" href="${ctx }/app/know/myKnowHelp" title=""><i class="fa fa-question-circle"></i><lb:get key="lab_knowmenu_help"/></a></li>
    <%-- <li><a id="knowHelp" href="${ctx }/app/know/knowHelp" title=""><i class="fa fa-lightbulb-o"></i><lb:get key="know_help"/></a></li>  屏蔽帮助功能--%>
</ul>
</div> <!-- xyd-sidebar End-->
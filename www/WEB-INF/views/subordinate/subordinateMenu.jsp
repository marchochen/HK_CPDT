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
     <a href="javascript:;"><img class="wzb-pic" src="${ctx}${regUser.usr_photo}"/></a>
     <p class="companyInfo">了解Ta</p>
     <div class="cornerTL">&nbsp;</div>
     <div class="cornerTR">&nbsp;</div>
     <div class="cornerBL">&nbsp;</div>
     <div class="cornerBR">&nbsp;</div>
</div>

<div class="xyd-user-content">
     <a href="javascript:;" class="wzb-link04" title="${regUser.usr_display_bil}">${regUser.usr_display_bil}</a>
     <p>${regUser.usg_display_bil}</p>
	 <p><c:if test="${regUser.ugr_display_bil != 'Unspecified'}">${regUser.ugr_display_bil}</c:if></p>
</div>
</div> <!-- usermess End-->

<p><lb:get key="personal_my_credit"/>：<span class="skin-color"><a href="${ctx}/app/personal/personalCredits/${regUser.usr_ent_id}"  class="skin-color">${total_credits}</a></span>
</p>
<P><lb:get key="course_credit"/>：<span class="">${learn_credits}</span></P>
</div>

<%-- <div class="wzb-model-7">
<c:if test="${sns_enabled == true}">
<div class="wzb-percent-biao">
     <div class="wzb-percent-3">
          <a class="wzb-percent-link" href="${ctx}/app/personal/personalValuation/${regUser.usr_ent_id}">
             <em>${likes}</em>
             <span><lb:get key="rank_praise"/></span>
          </a>
     </div>
     
     <div class="wzb-percent-3">
          <a class="wzb-percent-link" href="${ctx}/app/personal/personalUserList/attention/${regUser.usr_ent_id}">
             <em>${attent}</em>
             <span><lb:get key="personal_attention"/></span>
          </a>
     </div>
     
     <div class="wzb-percent-3">
          <a class="wzb-percent-link" href="${ctx}/app/personal/personalUserList/fans/${regUser.usr_ent_id}">
             <em>${fans}</em>
             <span><lb:get key="personal_fans"/></span>
          </a>
     </div>
</div>
</c:if>
</div> --%>

<%-- <div class="wzb-model-1">
     <a title="发送信息" href="${ctx}/app/subordinate/subordinateMessage" class="xyd-user-survey"><i class="fa skin-color fa-pencil-square-o"></i> <lb:get key="subordinate_send_message"/></a>
</div> --%>

<ul class="wzb-list-15">
    <li><a id="subordinateList" href="${ctx}/app/subordinate/subordinateList"><lb:get key="subordinate_list"/></a></li>
    <li><a id="subordinateReport" href="${ctx}/app/subordinate/subordinateReport"><lb:get key="subordinate_report"/></a></li>
    <li><a id="subordinateApproval" href="${ctx}/app/subordinate/subordinateApproval"><lb:get key="subordinate_approval"/></a></li>
    <c:if test="${cpd_enable == true}">
    <c:choose>
        <c:when test="${lang =='en-us'}">
	        <li style="height:50px;">
	        <a id="cpdOutstandingReport" href="${ctx}/app/cpdOutstandingReport/cpdOutstandingReport"  style="height:50px;line-height: 24px">
	            <lb:get key="label_cpd_outstanding_hours_report"/>
	        </a>
	        </li>
        </c:when>
        <c:otherwise>
	        <li >
	        <a id="cpdOutstandingReport" href="${ctx}/app/cpdOutstandingReport/cpdOutstandingReport" >
	            <lb:get key="label_cpd_outstanding_hours_report"/>
	        </a>
	        </li>
        </c:otherwise>
    </c:choose>
    
    <li>
    	<a id="cpdIndividualReport" href="${ctx}/app/cpdIndividualReport/cpdIndividualReport">
    		<lb:get key="label_cpd_individual_hours_report"/>
    	</a>
    </li>
    </c:if>
</ul>
</div> <!-- xyd-sidebar End-->

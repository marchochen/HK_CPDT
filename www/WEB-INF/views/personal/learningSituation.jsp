<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="personalMenu.jsp"></jsp:include>

			<div class="xyd-article">
                <div class="wzb-title-2 margin-bottom25"><lb:get key="personal_deadline" />：<fmt:formatDate value="${learningSituation.ls_update_time}" pattern="yyyy-MM-dd HH:mm:ss" />
                <lb:get key="personal_deadline_notes" />
                </div>

                <table class="wzb-table-five wzb-table-red">
                    <tr>
                        <th rowspan="2"><p>
                                <lb:get key="personal_situation" />
                            </p>
                            <p>
                                <lb:get key="personal_pandect" />
                            </p></th>
                        <td>
                            <lb:get key="personal_total_learn_duration" />
                        </td>
                        <td>
                            <lb:get key="personal_total_credit" />
                        </td>
                        <td>
                            <lb:get key="usr_credit" />
                        </td>
                        <%-- <td>
                            <lb:get key="lab_kb_credit_ranking" />
                        </td> --%>
                    </tr>
                    <tr class="trfont">
                        <td>
                            <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                   <a class="wzb-link02" href="${ctx}/app/rank/learningRank">${learningSituation.ls_learn_duration_str}</a>
                               </c:when>
                               <c:otherwise>
                                   ${learningSituation.ls_learn_duration_str}
                               </c:otherwise>
                            </c:choose>                       
                            
                        </td>
                        <td>${learningSituation.ls_learn_credit}</td>
                        <td>
                            <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                   <a class="wzb-link02" href="${ctx}/app/rank/creditRank">${learningSituation.ls_total_integral}</a>
                               </c:when>
                               <c:otherwise>
                                   ${learningSituation.ls_total_integral}
                               </c:otherwise>
                            </c:choose> 
                        </td>
                        <%-- <td>
                            <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                  <a class="wzb-link02" href="${ctx}/app/rank/creditRank">${learningSituation.rownum}</a>
                               </c:when>
                               <c:otherwise>
                                  ${learningSituation.rownum}
                               </c:otherwise>
                            </c:choose>
                        </td> --%>
                    </tr>
                </table>

                <table class="wzb-table-five wzb-table-blue">
                    <tr>
                        <th rowspan="2"><p>
                                <lb:get key="personal_course_study" />
                            </p></th>
                        <td>
                            <lb:get key="personal_total_courses" />
                        </td>
                        <td>
                            <lb:get key="status_completed" />
                        </td>
                        <td>
                            <lb:get key="status_fail" />
                        </td>
                        <td>
                            <lb:get key="status_inprogress" />
                        </td>
                        <td>
                            <lb:get key="status_pending" />
                        </td>
                    </tr>
                    <tr class="trfont">
                        <td>${learningSituation.ls_total_courses}</td>
                        <td>
                           <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                  <a class="wzb-link02" href="${ctx}/app/course/signup?data=C">${learningSituation.ls_course_completed_num}</a>
                               </c:when>
                               <c:otherwise>
                                  ${learningSituation.ls_course_completed_num}
                               </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${learningSituation.ls_course_fail_num}</td>
                        <td>
                           <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                  <a class="wzb-link02" href="${ctx}/app/course/signup?data=I">${learningSituation.ls_course_inprogress_num}</a>
                               </c:when>
                               <c:otherwise>
                                  ${learningSituation.ls_course_inprogress_num}
                               </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                          <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                  <a class="wzb-link02" href="${ctx}/app/course/signup?data=notIAndC">${learningSituation.ls_course_pending_num}</a>
                               </c:when>
                               <c:otherwise>
                                  ${learningSituation.ls_course_pending_num}
                               </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>

         <%--        <table class="wzb-table-five wzb-table-gray">
                    <tr>
                        <th rowspan="2"><p>
                                <lb:get key="menu_test" />
                            </p></th>
                        <td>
                            <lb:get key="personal_total_exams" />
                        </td>
                        <td>
                            <lb:get key="status_completed" />
                        </td>
                        <td>
                            <lb:get key="status_fail" />
                        </td>
                        <td>
                            <lb:get key="status_inprogress" />
                        </td>
                        <td>
                            <lb:get key="status_pending" />
                        </td>
                    </tr>
                    <tr class="trfont">
                        <td>${learningSituation.ls_total_exams}</td>
                        <td>
                           <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                   <a class="wzb-link02" href="${ctx}/app/exam/signup?data=C">${learningSituation.ls_exam_completed_num}</a>
                               </c:when>
                               <c:otherwise>
                                  ${learningSituation.ls_exam_completed_num}
                               </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${learningSituation.ls_exam_fail_num}</td>
                        <td>
                           <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                   <a class="wzb-link02" href="${ctx}/app/exam/signup?data=I">${learningSituation.ls_exam_inprogress_num}</a>
                               </c:when>
                               <c:otherwise>
                                  ${learningSituation.ls_exam_inprogress_num}
                               </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                   <a class="wzb-link02" href="${ctx}/app/exam/signup?data=notIAndC">${learningSituation.ls_exam_pending_num}</a>
                               </c:when>
                               <c:otherwise>
                                  ${learningSituation.ls_exam_pending_num}
                               </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table> --%>
                <%-- <c:if test="${sns_enabled == true}">
                    <table class="wzb-table-five wzb-table-green">
                        <tr>
                            <th rowspan="8"><p>
                                    <lb:get key="menu_community" />
                                </p></th>
                            <td rowspan="2" class="trfont">
                                <lb:get key="know_my" />
                            </td>
                            <td>
                                <lb:get key="personal_fans" />
                            </td>
                            <td>
                                <lb:get key="personal_attention" />
                            </td>
                            <td>
                                <lb:get key="personal_be_praised" />
                            </td>
                            <td>
                                <lb:get key="personal_praise_others" />
                            </td>
                            <td>
                                <lb:get key="sns_collect" />
                            </td>
                        </tr>
                        <tr class="trfont">
                            <td>
                                <c:choose>
	                               <c:when test="${prof.usr_ent_id eq usrEntId }">
	                                    <a class="wzb-link02" href="${ctx}/app/personal/personalUserList/fans/${prof.usr_ent_id}">${learningSituation.ls_fans_num}</a>
	                               </c:when>
	                               <c:otherwise>
	                                 ${learningSituation.ls_fans_num}
	                               </c:otherwise>
	                            </c:choose>
                            </td>
                            <td>
                                <c:choose>
	                               <c:when test="${prof.usr_ent_id eq usrEntId }">
	                                   <a class="wzb-link02" href="${ctx}/app/personal/personalUserList/attention/${prof.usr_ent_id}">${learningSituation.ls_attention_num}</a>
	                               </c:when>
	                               <c:otherwise>
	                                  ${learningSituation.ls_attention_num}
	                               </c:otherwise>
	                            </c:choose>
                            </td>
                            <td>
                                <c:choose>
	                               <c:when test="${prof.usr_ent_id eq usrEntId }">
	                                  <a class="wzb-link02" href="${ctx}/app/personal/personalValuation/${prof.usr_ent_id}">${learningSituation.ls_praised_num}</a>
	                               </c:when>
	                               <c:otherwise>
	                                  ${learningSituation.ls_praised_num}
	                               </c:otherwise>
	                            </c:choose>
                            </td>
                            <td>${learningSituation.ls_praise_others_num}</td>
                            <td>
                                <c:choose>
	                               <c:when test="${prof.usr_ent_id eq usrEntId }">
	                                   <a class="wzb-link02" href="${ctx}/app/personal/personalCollect/${prof.usr_ent_id}">${learningSituation.ls_collect_num}</a>
	                               </c:when>
	                               <c:otherwise>
	                                 ${learningSituation.ls_collect_num}
	                               </c:otherwise>
	                            </c:choose>
                            </td>
                        </tr>

                        <tr class="trline">
                            <td rowspan="2" class="trfont">
                                <lb:get key="menu_group" />
                            </td>
                            <td colspan="2">
                                <lb:get key="credits_SYS_CREATE_GROUP" />
                            </td>
                            <td colspan="2">
                                <lb:get key="credits_SYS_JION_GROUP" />
                            </td>
                            <td colspan="2">
                                <lb:get key="personal_group_speech" />
                            </td>
                        </tr>

                        <tr class="trfont">
                            <td colspan="2">${learningSituation.ls_create_group_num}</td>
                            <td colspan="2">${learningSituation.ls_join_group_num}</td>
                            <td colspan="2">${learningSituation.ls_group_speech_num}</td>
                        </tr>
                        <tr class="trline">
                            <td rowspan="2" class="trfont">
                                <lb:get key="personal_know" />
                            </td>
                            <td colspan="3">
                                <lb:get key="personal_question_number" />
                            </td>
                            <td colspan="3">
                                <lb:get key="personal_feedback_number" />
                            </td>
                        </tr>

                        <tr class="trfont">
                            <td colspan="3">
                                <c:choose>
	                               <c:when test="${prof.usr_ent_id eq usrEntId }">
	                                   <a class="wzb-link02" href="${ctx}/app/know/myKnow">${learningSituation.ls_question_num}</a>
	                               </c:when>
	                               <c:otherwise>
	                                  ${learningSituation.ls_question_num}
	                               </c:otherwise>
	                            </c:choose>
                            </td>
                            <td colspan="3">
                                <c:choose>
	                               <c:when test="${prof.usr_ent_id eq usrEntId }">
                                        <a class="wzb-link02" href="${ctx}/app/know/myKnow?data=my_answer">${learningSituation.ls_answer_num}</a>	                               </c:when>
	                               <c:otherwise>
	                                  ${learningSituation.ls_exam_pending_num}
	                               </c:otherwise>
	                            </c:choose>
                            </td>
                        </tr>
                        <tr class="trline">
                            <td rowspan="2" class="trfont">
                                <lb:get key="lab_kb_item" />
                            </td>
                            <td colspan="3">
                                <lb:get key="lab_kb_index_share" />
                            </td>
                            <td colspan="3">
                                <lb:get key="lab_kb_index_access" />
                            </td>
                        </tr>

                        <tr class="trfont">
                            <td colspan="3">
                                <c:choose>
	                               <c:when test="${prof.usr_ent_id eq usrEntId }">
	                                   <a class="wzb-link02" href="${ctx}/app/kb/center/list?tab=APPROVED"> ${learningSituation.ls_share_count} </a>
	                               </c:when>
	                               <c:otherwise>
	                                  ${learningSituation.ls_share_count} 
	                               </c:otherwise>
	                            </c:choose>
                            </td>
                            <td colspan="3">${learningSituation.ls_access_count}</td>
                        </tr>
                    </table>
                </c:if> --%>
			</div> <!-- xyd-article End-->
		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
</html>
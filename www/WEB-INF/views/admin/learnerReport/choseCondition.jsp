<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rp_${lang}.js"></script>
	<table class="report-table-td margin-bottom15">
      		<tbody>
	          <tr>
	              <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_45" />：</td>
	              <td class="wzb-form-control">
              		<c:choose>
              			<c:when test="${choseCondition.exportUser == 0 }">
              				
              				<lb:get key="label_rp.label_core_report_46" /> <c:if test="${choseCondition.includeDelUser}">（<lb:get key="label_rp.label_core_report_47" />）</c:if>
              				
              			</c:when>
              			<c:when test="${choseCondition.exportUser == 1 }">
              				<c:forEach items="${choseCondition.exportUserNames}" var="itm" varStatus="status">
              				 	<c:out value="${itm}"/>
              				 	<c:if test="${status.last eq false}">
              						,
						    	</c:if> 
              				 </c:forEach>
              			</c:when>
              			<c:when test="${choseCondition.exportUser == 2 }">
              				<c:forEach items="${choseCondition.exportGroupNames}" var="itm" varStatus="status">
              				 	<c:out value="${itm}"/>
              				 	<c:if test="${status.last eq false}">
              						,
						    	</c:if> 
              				 </c:forEach>
              			</c:when>
              		</c:choose>
	              </td>
	          </tr>
	
	          <tr>

	              <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_107" />：</td>
	              <td class="wzb-form-control">
	              	<c:choose>
              			<c:when test="${choseCondition.exportCourse == 0 }">
              				
              				<lb:get key="label_rp.label_core_report_50" />
              				
              			</c:when>
              			<c:when test="${choseCondition.exportCourse == 1 }">
              				<c:forEach items="${choseCondition.exportCourseNames}" var="itm" varStatus="status">
              				 	<c:out value="${itm}"/>
              				 	<c:if test="${status.last eq false}">
              						,
						    	</c:if> 
              				 </c:forEach>
              			</c:when>
              			<c:when test="${choseCondition.exportCourse == 2 }">
              				 <c:forEach items="${choseCondition.exportCatalogNames}" var="itm" varStatus="status">
              				 	<c:out value="${itm}"/>
              				 	<c:if test="${status.last eq false}">
              						,
						    	</c:if> 
              				 </c:forEach>
              			</c:when>
              		</c:choose>
	              </td>
	          </tr>
	
	          <tr>
	              <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_103" />：</td>
	              <td class="wzb-form-control">
	              	<c:forEach items="${choseCondition.courseType}" var="itm" varStatus="status" >
		              	<c:choose>
	              			<c:when test="${itm == 0 }">
	              				<lb:get key="label_rp.label_core_report_53" />
	              			</c:when>
	              			<c:when test="${itm == 1 }">
	              				<lb:get key="label_rp.label_core_report_54" />
	              			</c:when>
	              			<c:when test="${itm == 2 }">
	              				<lb:get key="label_rp.label_core_report_55" />
	              			</c:when>
	              			<c:when test="${itm == 3 }">
	              				<lb:get key="label_rp.label_core_report_56" />
	              			</c:when>
	              			<c:when test="${itm == 4 }">
	              				<lb:get key="label_rp.label_core_report_57" />
	              			</c:when>
	              		</c:choose>
	              		<c:if test="${!status.last}">,</c:if>
	              	</c:forEach>
	              </td>
	          </tr>
			
			  <c:if test="${not empty choseCondition.appnStartDisplayDatetime or not empty choseCondition.appnEndDisplayDatetime }">
	          <tr>
	              <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_58" />：</td>
	              <td class="wzb-form-control">
	              	<c:choose>
	              		<c:when test="${not empty choseCondition.appnStartDisplayDatetime}">
	              			<c:out value="${choseCondition.appnStartDisplayDatetime}"></c:out>
	              		</c:when>
	              		<c:otherwise>
	              			--
	              		</c:otherwise>
	              	</c:choose>
	              	<lb:get key="label_rp.label_core_report_59" /> 
         		    <c:choose>
	              		<c:when test="${not empty choseCondition.appnEndDisplayDatetime}">
	              			<c:out value="${choseCondition.appnEndDisplayDatetime}"></c:out>
	              		</c:when>
	              		<c:otherwise>
	              			--
	              		</c:otherwise>
	              	</c:choose>
	              </td>
	          </tr>
			 </c:if>
			
			<c:if test="${not empty choseCondition.attStartDispalyTime or not empty choseCondition.attEndDisplayTime }">
	          <tr>
	              <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_60" />：</td>
	              <td class="wzb-form-control">
	               <c:choose>
	              		<c:when test="${not empty choseCondition.attStartDispalyTime}">
	              			<c:out value="${choseCondition.attStartDispalyTime}"></c:out>
	              		</c:when>
	              		<c:otherwise>
	              			--
	              		</c:otherwise>
	              	</c:choose>
	              	<lb:get key="label_rp.label_core_report_59" />
		            <c:choose>
	              		<c:when test="${not empty choseCondition.attEndDisplayTime}">
	              			<c:out value="${choseCondition.attEndDisplayTime}"></c:out>
	              		</c:when>
	              		<c:otherwise>
	              			--
	              		</c:otherwise>
	              	</c:choose>
	              </td>
	          </tr>
			</c:if>
			
	          <tr>
	              <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_61" />：</td>
	              <td class="wzb-form-control">
	              	<c:forEach items="${choseCondition.courseStatus}" var="itm" varStatus="status">
		              	<c:choose>
	              			<c:when test="${itm == 'I' }">
	              				<lb:get key="label_rp.label_core_report_65" />
	              			</c:when>
	              			<c:when test="${itm == 'C' }">
	              				<lb:get key="label_rp.label_core_report_66" />
	              			</c:when>
	              			<c:when test="${itm == 'F' }">
	              				<lb:get key="label_rp.label_core_report_67" />
	              			</c:when>
	              			<c:when test="${itm == 'W' }">
	              				<lb:get key="label_rp.label_core_report_68" />
	              			</c:when>
	              		</c:choose>
	              		<c:if test="${!status.last}"> 
	              		,
	              		</c:if>
	              	</c:forEach>
	              	
	              	<c:if test="${fn:length(choseCondition.appStatus) > 0}">
	                	<c:if test="${fn:length(choseCondition.courseStatus) > 0}">,</c:if>
	              	</c:if>
	           
	              	<c:forEach items="${choseCondition.appStatus}" var="itm" varStatus="status">
		              	<c:choose>
	              			<c:when test="${itm == 'Pending' }">
	              				<lb:get key="label_rp.label_core_report_62" />
	              			</c:when>
	              			<c:when test="${itm == 'Waiting' }">
	              				<lb:get key="label_rp.label_core_report_63" />
	              			</c:when>
	              			<c:when test="${itm == 'Rejected' }">
	              				<lb:get key="label_rp.label_core_report_64" />
	              			</c:when>
	              		</c:choose>
	              		<c:if test="${!status.last}"> 
	              		,
	              		</c:if>
	              	</c:forEach>
	              </td>
	          </tr>
	
	          <tr>
	              <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_69" />：</td>
	              <td class="wzb-form-control">
	              		<c:choose>
	              			<c:when test="${choseCondition.analysisType == 0 }">
	              				<lb:get key="label_rp.label_core_report_70" />
	              				<c:if test="${choseCondition.includeNoDataCourse == 1 }"><lb:get key="label_rp.label_core_report_71" /></c:if>
	              			</c:when>
	              			<c:when test="${choseCondition.analysisType == 1 }">
	              				<lb:get key="label_rp.label_core_report_72" />
	              				<c:if test="${choseCondition.includeNoDataUser == 1 }"><lb:get key="label_rp.label_core_report_73" /></c:if>
	              			</c:when>
	              		</c:choose>
	                  <span class="ask-in margin4 allhint-style-1"></span>
	                  <script type="text/javascript">
	                      $(".allhint-style-1").mouseenter(function(){
	                          layer.tips(fetchLabel('label_core_report_138'), '.allhint-style-1', {
	                            tips: [2, 'rgba(128,128,128,0.9)'],
	                            time:50000
	                          });
	                      });
	                      $(".allhint-style-1").mouseleave(function(){
	                          layer.tips()
	                      })
	                  </script>
	              </td>
	          </tr>
	      </tbody>
	</table>

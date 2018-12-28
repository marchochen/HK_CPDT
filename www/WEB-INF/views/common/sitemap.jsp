<%@ page isELIgnored="false" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="./taglibs.jsp"%>
<c:set var="hasStaff" value="${not empty prof.hasStaff and prof.hasStaff eq true}" />
<div class="wzb-sitemap-div">
	<table class="wzb-sitemap-table">
		<tr>
			<td class="wzb-sitemap-header-l"></td>
			<td nowrap="nowrap" class="wzb-sitemap-header" style="width: 50px;"></td>
			<td nowrap="nowrap" class="wzb-sitemap-header">
				<wzb:sitemap-link type="header" href="javascript: go('${ctx}/app/course/course_center');" key="11" />
			</td>
			<td nowrap="nowrap" class="wzb-sitemap-header">
				<wzb:sitemap-link type="header" href="javascript: go('${ctx}/app/course/learning_center/COS/current');" key="16" />
			</td>
			<td nowrap="nowrap" class="wzb-sitemap-header">
				<wzb:sitemap-link type="header" href="javascript: go('${ctx}/app/course/learning_center/EXAM/current');" key="21" />
			</td>
			<td nowrap="nowrap" class="wzb-sitemap-header">
				<wzb:sitemap-link type="header" href="javascript: go('${ctx}/app/course/learning_center/INTEGRATED/current');" key="862" />
			</td>
			<c:if test="${not empty prof.hasStaff and prof.hasStaff eq true}">
				<td nowrap="nowrap" class="wzb-sitemap-header">
					<wzb:sitemap-link type="header" href="${ctx}/app/user/get_my_staff" key="26" />
				</td>
			</c:if>
			<td nowrap="nowrap" class="wzb-sitemap-header">
				<wzb:sitemap-link type="header" href="${ctx}/app/ann/default" key="30" />
			</td>
			<td class="wzb-sitemap-header-r"></td>
		</tr>
		<tr>
			<td class="wzb-sitemap-m-l"></td>
			<td class="wzb-sitemap-m-c" valign="top"></td>
			<td class="wzb-sitemap-m-c" valign="top" nowrap="nowrap" style="width: ${hasStaff ? '150px' : '170px'};">
				<ul class="wzb-sitemap-list">
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/course_center');" key="12" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/pop_estimated_course/hot_course');" key="13" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/nominate_course/all');" key="14" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/get_my_plan');" key="15" />
					</li>
				</ul>
			</td>
			<td class="wzb-sitemap-m-c" valign="top" nowrap="nowrap" style="width: ${hasStaff ? '150px' : '170px'};">
				<ul class="wzb-sitemap-list">
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/COS/all');" key="17" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/COS/pending');" key="18" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/COS/current');" key="19" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/COS/finished');" key="20" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/user/get_credit_rank');" key="851" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/user/get_my_credit');" key="853" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/home/cert');" key="992" />
					</li>
				</ul>
			</td>
			<td class="wzb-sitemap-m-c" valign="top" nowrap="nowrap" style="width: ${hasStaff ? '150px' : '170px'};">
				<ul class="wzb-sitemap-list">
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/EXAM/all');" key="22" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app//course/learning_center/EXAM/pending');" key="23" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/EXAM/current');" key="24" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/EXAM/finished');" key="25" />
					</li>
				</ul>
			</td>
			<td class="wzb-sitemap-m-c" valign="top" nowrap="nowrap" style="width: ${hasStaff ? '150px' : '170px'};">
				<ul class="wzb-sitemap-list">
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/INTEGRATED/all');" key="906" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/INTEGRATED/pending');" key="907" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/INTEGRATED/current');" key="908" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/learning_center/INTEGRATED/finished');" key="909" />
					</li>
				</ul>
			</td>
			<c:if test="${not empty prof.hasStaff and prof.hasStaff eq true}">
				<td class="wzb-sitemap-m-c" valign="top" nowrap="nowrap" style="width: 150px;">
					<ul class="wzb-sitemap-list">
						<li>
							<wzb:sitemap-link href="javascript: go('${ctx}/app/user/get_my_staff');" key="27" />
						</li>
						<li>
							<wzb:sitemap-link href="javascript: go('${ctx}/app/report/my_staff_report');" key="28" />
						</li>
						<li>
							<wzb:sitemap-link href="javascript: go('${ctx}/app/user/app_approval/get_app_pend');" key="29" />
						</li>
					</ul>
				</td>
			</c:if>
			<td class="wzb-sitemap-m-c" valign="top" nowrap="nowrap" style="width: ${hasStaff ? '150px' : '180px'};">
				<ul class="wzb-sitemap-list">
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/ann/default');" key="31" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/answers/answers/que_unans_lst');" key="345" />
					</li>
					<li>
						<wzb:sitemap-link href="javascript: go('${ctx}/app/course/material_center');" key="695" />
					</li>
				</ul>
			</td>
			<td nowrap="nowrap" class="wzb-sitemap-m-r"></td>
		</tr>
		<tr>
			<td class="wzb-sitemap-b-l"></td>
			<td class="wzb-sitemap-b-c" colspan="${(not empty prof.hasStaff and prof.hasStaff eq true) ? 7 : 6}"></td>
			<td class="wzb-sitemap-b-r"></td>
		</tr>
	</table>
</div>
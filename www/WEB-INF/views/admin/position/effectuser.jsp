<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet"
	href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script>
	var tab = '<c:if test="${active == 'c'}">2</c:if>';
</script>
</head>
<body>
	<input type="hidden" name="belong_module" value="FTN_AMD_USR_INFO_MGT" />

	<title:get function="global.FTN_AMD_USR_INFO_MGT" />

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i> <lb:get
					key="label_lm.label_core_learning_map_1" /></a></li>
		<li class="active"><lb:get
				key="label_lm.label_core_learning_map_85" />
			<!-- 岗位管理--></li>
		<li class="active"><lb:get
				key="label_lm.label_core_learning_map_113" /></li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<div class="panel-heading">
			<lb:get key="label_lm.label_core_learning_map_85" />
			<!-- 岗位管理 -->
		</div>

		<div class="panel-body">

			<div class="margin-top28" id="table-tab">
				<c:choose>
					<c:when test="${fn:length(list) > 0}">
						<div>
							<i class="wiz-jing"></i>
							<div class="wzb-ui-desc-text">
								<lb:get key="label_lm.label_core_learning_map_106" />
								"<b>${positionTitle }</b>"
								<lb:get key="label_lm.label_core_learning_map_107" />
								<lb:get key="label_lm.label_core_learning_map_108" />
							</div>
						</div>
						<table class="table wzb-ui-table">
							<tbody>
								<tr>
									<td align="left" colSpan="3" style="border: 0;"><lb:get
											key="label_lm.label_core_learning_map_109" /></td>
								</tr>
								<tr class="wzb-ui-table-head">
									<td width="30%" align="left"><lb:get
											key="label_lm.label_core_learning_map_110" /></td>
									<td width="30%" align="center"><lb:get
											key="label_lm.label_core_learning_map_111" /></td>
									<td width="40%" align="right"><lb:get
											key="label_lm.label_core_learning_map_112" /></td>
								</tr>
								<c:forEach var="user" items="${list }">
									<tr>
										<td align="left">${user.usr_ste_usr_id }</td>
										<td align="center">${user.usr_display_bil }</td>
										<td align="right">${user.usg_display_bil }</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>
						<div class="losedata">
							<i class="fa fa-folder-open-o"></i>
							<p>
								<lb:get key="label_lm.label_core_learning_map_116" />
							</p>
						</div>
					</c:otherwise>
				</c:choose>

			</div>
			<form id="effectForm"
				action="${ctx }/app/admin/position/delUptaffectuser" method="post">
				<input type="hidden" name="ids" value="${ids }">
				<div class="wzb-bar">
					<input id="createBtn" type="button" name="frmSubmitBtn"
						value="<lb:get key='global.button_ok'/>"
						class="btn wzb-btn-blue wzb-btn-big margin-right15"
						onclick="javascript:doSub();">
					<!-- 确定 -->
					<input type="button" name="frmSubmitBtn"
						value="<lb:get key='global.button_cancel'/>"
						class="btn wzb-btn-blue wzb-btn-big"
						onclick="javascript:window.history.go(-1)">
					<!-- 取消 -->
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		function doSub() {
			Dialog.confirm({
				text : cwn.getLabel("label_core_learning_map_106") + '"'
						+ '${positionTitle }' + '"'
						+ cwn.getLabel("label_core_learning_map_115"),
				callback : function(answer) {
					if (answer) {
						$("#effectForm").submit();
					}
				}
			})
		}
	</script>
	<script type="text/javascript"
		src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
	<script type="text/javascript"
		src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>

</body>
</html>
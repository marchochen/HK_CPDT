<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />

<title></title>
</head>
<body>
	<div class="xyd-wrapper">
		<div class="xyd-main clearfix">
			<jsp:include page="../personal/personalMenu.jsp"></jsp:include>
			<div class="xyd-article">
			<!-- 标题 -->
			  <div class="wzb-title-12"><lb:get key="label_cm.label_core_community_management_194"/></div>
				<div role="tabpanel" class="wzb-tab-3">
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active">
							<a href="#list_ing" aria-controls="home" role="tab" data-toggle="tab" style="padding-left:0;"><lb:get key="label_rm.label_core_requirements_management_51"/><!-- 进行中 --></a></li>
						<li role="presentation"><a href="#list_ed"
							aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="label_rm.label_core_requirements_management_52"/><!-- 已结束 --></a></li>
					</ul>

					<!-- Tab panes -->
					<div class="tab-content margin-top15">
						<div role="tabpanel" class="tab-pane active" id="list_ing">
						</div>
						<div role="tabpanel" class="tab-pane" id="list_ed">
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="${ctx}/static/js/i18n/${lang}/label_rm_${lang}.js"></script>
	<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript">
		var usrEntId = ${usrEntId};
	</script>
	<script type="text/javascript" src="${ctx}/static/js/front/voting/voting.js"></script>
	<script id="operateBtnTemplate" type="text/x-jsrender">
		<input type="button" onclick="window.location.href='{{>url}}'" class="btn wzb-btn-orange" value="{{>text}}" name="frmSubmitBtn">
	</script>
</body>
</html>

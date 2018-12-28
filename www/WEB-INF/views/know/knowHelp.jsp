<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<title></title>
</head>
<!-- 简体中文帮助页面 -->
<body>

	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="knowMenu.jsp"></jsp:include>
			<div id="know_jsp" class="xyd-article">
				<div class="wzb-title-2"><lb:get key="know_help" /></div>

				<dl class="wzb-help-01">
					<dt>
						<lb:get key="know_help_01_title" />
					</dt>
					<dd>
						<lb:get key="know_help_01_desc" />
					</dd>
				</dl>

				<dl class="wzb-help-02">
					<dt>
						<lb:get key="know_POPULAR" />
					</dt>
					<dd>
						<p>
							<lb:get key="know_help_02_desc_1" />
						</p>

						<p class="mt20">
							<lb:get key="know_help_02_desc_2" />
							"<span class="skin-color"><lb:get key="know_SOLVED" /></span>"
							<lb:get key="know_help_02_desc_3" />
							"<span class="skin-color"><lb:get key="know_vote_detail" /></span>"
							<lb:get key="know_help_02_desc_4" />
							<span class="skin-color">"<lb:get key="know_good" /></span>"
							<lb:get key="know_or" />
							"<span class="skin-color"><lb:get key="know_no_good" /></span>"
							<lb:get key="know_help_02_desc_5" />
						</p>

						<lb:get key="know_help_wdicon" />

						<p class="mt20">
							<lb:get key="know_help_02_desc_6" />
						</p>
					</dd>
				</dl>

			</div>
			<!-- xyd-article End-->
		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
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
				<h1 class="wzb-title-14">${voting.vot_title }</h1>
				<p>${voting.vot_content}</p>
				<div role="tabpanel" class="wzb-tab-2 margin-top30 wzb-border-top">
					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<span><lb:get key='label_core_requirements_management_14'/><!-- 参与人数 --><strong>${voting.responseCount == null ? "0" : voting.responseCount}</strong></span>
						<li role="presentation" class="active"><a href="#home"
							aria-controls="home" role="tab" data-toggle="tab"><!-- 饼图版 --><lb:get key="label_core_requirements_management_15"/> <i
								class="fa fa-pie-chart"></i></a></li>
						<li role="presentation" class=""><a href="#profile"
							aria-controls="profile" role="tab" data-toggle="tab"><!-- 列表版 --><lb:get key="global_style_list"/> <i
								class="fa fa-th-list"></i></a></li>
					</ul>

					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="home">
							<c:choose>
								<c:when test="${fn:length(result) eq 0 }">
									<div class="datatable-stat" style="display: block;">
										<div class="losedata"><i class="fa fa-folder-open-o"></i>
										<p><lb:get key="voting_not_result" /></p></div>
									</div>
								</c:when>
								<c:otherwise>
									<div class="chart-box clearfix">
										<div class="chart-responsive">
											<canvas id="pieChart" width="260" height="260"
												style="width: 260px; height: 260px;"></canvas>
										</div>
										<c:set var="cssClass" value="${fn:split('fa fa-circle-o progress-bar-blue,fa fa-circle-o progress-bar-purple,fa fa-circle-o progress-bar-green,fa fa-circle-o progress-bar-orange,fa fa-circle-o progress-bar-pink,fa fa-circle-o progress-bar-yellow,fa fa-circle-o progress-bar-indigo,fa fa-circle-o progress-bar-red,fa fa-circle-o progress-bar-light,fa fa-circle-o progress-bar-breen', ',')}" />
										
										<ul class="chart-legend clearfix">
											<c:forEach items="${result }" var="r" varStatus="status">
												<li><em><span><fmt:formatNumber type="number" value="${r.count/r.total*100}" pattern="0.00" maxFractionDigits="2"/>%</span> <i
												class="${cssClass[status.index] }"></i></em> ${r.label}</li>
											</c:forEach>
										</ul>
										<script src="${ctx }/static/admin/js/Chart.min.js" type="text/javascript"></script>
									</div>
								</c:otherwise>
							</c:choose>
						</div>
						<div role="tabpanel" class="tab-pane" id="profile">
							<c:choose>
								<c:when test="${fn:length(result) eq 0 }">
									<div class="datatable-stat" style="display: block;">
										<div class="losedata"><i class="fa fa-folder-open-o"></i>
										<p><lb:get key="voting_not_result" /></p></div>
									</div>
								</c:when>
								<c:otherwise>
									<table class="table wzb-ui-table">
										<tbody>
											<tr class="wzb-ui-table-head">
												<td width="10%" align="left"><lb:get key="label_core_requirements_management_39" /></td>
												<td width="35%"><lb:get key="label_core_requirements_management_40" /></td>
												<td width="40%"><lb:get key="label_core_requirements_management_41" /></td>
												<td width="15%" align="right"><lb:get key="label_core_requirements_management_42" /></td>
											</tr>
											<c:set var="cssClass2" value="${fn:split('progress-bar progress-bar-blue,progress-bar progress-bar-purple,progress-bar progress-bar-green,progress-bar progress-bar-orange,progress-bar progress-bar-pink,progress-bar progress-bar-yellow,progress-bar progress-bar-indigo,progress-bar progress-bar-red,progress-bar progress-bar-light,progress-bar progress-bar-breen', ',')}" />
											<c:forEach items="${result}" var="r" varStatus="status">
												<tr>
													<td align="left">${status.index+1}</td>
													<td>${r.label }</td>
													<td>
														<div class="progress progress-xs">
															<div style="width: <fmt:formatNumber type="number" value="${r.count/r.total*100}" pattern="0.00" maxFractionDigits="2"/>%"
																class="${cssClass2[status.index]}"></div>
														</div><fmt:formatNumber type="number" value="${r.count/r.total*100}" pattern="0.00" maxFractionDigits="2"/>%
													</td>
													<td align="right">${r.count}</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var PieData = [];
		var colors = ['#00c0ef','#dd4b39','#00a65a','#f39c12','#c86af1','#ffd21d','#2d3e50','#ff0000','#657694','#428bca'];
		<c:forEach items="${result }" var="r" varStatus="status">
			var labe="${r.label}";
		    if(labe.length>10){
		    	labe=labe.substring(0,10)+'...';
		    }
		    var dataItem = {
					value:${r.count},
					color:colors[${status.index}],
					highlight:colors[${status.index}],
					label :labe
			};
			PieData.push(dataItem);
		</c:forEach>
	</script>
	<script type="text/javascript"
		src="${ctx}/static/js/front/voting/viewResult.js"></script>
</body>
</html>

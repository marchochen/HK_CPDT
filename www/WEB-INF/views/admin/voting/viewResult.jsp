<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet"
	href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
</head>
<body>
<input type="hidden" name="belong_module" value="FTN_AMD_DEMAND_MGT"/>

	
	
	<title:get function="global.FTN_AMD_DEMAND_MGT"/>

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i
				class="fa wzb-breadcrumb-home fa-home"></i> <lb:get
					key="label_rm.label_core_requirements_management_1" /></a></li>
		<li><a href="${ctx}/app/admin/voting"><!-- 投票 --><lb:get key="label_rm.label_core_requirements_management_2" /></a></li>
		<li class="active">
			<!-- 查看投票结果 --><lb:get key="label_rm.label_core_requirements_management_13"/>
		</li>
	</ol>
	<!-- wzb-breadcrumb End-->
	<div class="panel wzb-panel">
		<div class="panel-heading">
			${voting.vot_title }
		</div>

		<div class="panel-body">
			<p>${voting.vot_content}</p>

			<div role="tabpanel" class="wzb-tab-2 wzb-border-top">
				<!-- Nav tabs -->
				<ul class="nav nav-tabs" role="tablist">
					<span><lb:get key='label_rm.label_core_requirements_management_14'/>
					<!-- 参与人数 --><strong>${voting.responseCount == null ? "0" : voting.responseCount}</strong></span>
					<li role="presentation" class="active"><a href="#home"
						aria-controls="home" role="tab" data-toggle="tab"><!-- 饼图版 -->
						<lb:get key="label_rm.label_core_requirements_management_15"/> <i
							class="fa fa-pie-chart"></i></a></li>
					<li role="presentation" class=""><a href="#profile"
						aria-controls="profile" role="tab" data-toggle="tab"><!-- 列表版 --><lb:get key="global.global_style_list"/> <i
							class="fa fa-th-list"></i></a></li>
				</ul>

				<!-- Tab panes -->
				<div class="tab-content">
					<c:choose>
						<c:when test="${!empty result && fn:length(result) > 0 }">
							<div role="tabpanel" class="tab-pane active" id="home"> <!-- 饼图版 -->
								<div class="chart-box clearfix">
									<div class="chart-responsive">
										<canvas id="pieChart" width="260" height="260"
											style="width: 260px; height: 260px;"></canvas>
									</div>
									
									<c:set var="cssClass" value="${fn:split('fa fa-circle-o progress-bar-blue,fa fa-circle-o progress-bar-purple,fa fa-circle-o progress-bar-green,fa fa-circle-o progress-bar-orange,fa fa-circle-o progress-bar-pink,fa fa-circle-o progress-bar-yellow,fa fa-circle-o progress-bar-indigo,fa fa-circle-o progress-bar-red,fa fa-circle-o progress-bar-light,fa fa-circle-o progress-bar-breen', ',')}" />
									
									<!-- <ul class="chart-legend clearfix">
										<li><em><span>20%</span> <i
												class="fa fa-circle-o progress-bar-blue"></i></em> 新员工入职培训</li>
										<li><em><span>10%</span> <i
												class="fa fa-circle-o progress-bar-purple"></i></em> 会计入门基础课程</li>
										<li><em><span>5%</span> <i
												class="fa fa-circle-o progress-bar-green"></i></em> 商务谈判技巧课程</li>
										<li><em><span>10%</span> <i
												class="fa fa-circle-o progress-bar-orange"></i></em> 团队合作技巧与实践课程</li>
										<li><em><span>10%</span> <i
												class="fa fa-circle-o progress-bar-pink"></i></em> 平台入门培训课程</li>
										<li><em><span>5%</span> <i
												class="fa fa-circle-o progress-bar-yellow"></i></em> 讲师资格证指导课程</li>
										<li><em><span>5%</span> <i
												class="fa fa-circle-o progress-bar-indigo"></i></em> 行政绩效考核评估标准课程</li>
										<li><em><span>15%</span> <i
												class="fa fa-circle-o progress-bar-red"></i></em> 商务英语系列</li>
										<li><em><span>10%</span> <i
												class="fa fa-circle-o progress-bar-light"></i></em> 销售从业技巧课程</li>
										<li><em><span>10%</span> <i
												class="fa fa-circle-o progress-bar-breen"></i></em> 开发人员标准语言培训课程</li>
									</ul> -->
									<ul class="chart-legend clearfix">
									
									<c:forEach items="${result }" var="r" varStatus="status">
										<li><em><span><fmt:formatNumber type="number" value="${r.count/r.total*100}" pattern="0.00" maxFractionDigits="2"/>%</span> <i
												class="${cssClass[status.index] }"></i></em> ${r.label}</li>
									</c:forEach>
									
									</ul>
									<script src="${ctx}/static/admin/js/Chart.min.js"
										type="text/javascript"></script>
								</div>
							</div>
							
							<div role="tabpanel" class="tab-pane" id="profile"> <!-- 列表版 -->
								<table class="table wzb-ui-table">
									<tbody>
										<tr class="wzb-ui-table-head">
											<td width="10%" align="left"><lb:get key="label_rm.label_core_requirements_management_39"/></td>
											<td width="35%"><lb:get key="label_rm.label_core_requirements_management_40"/></td>
											<td width="40%"><lb:get key="label_rm.label_core_requirements_management_41"/></td>
											<td width="15%" align="right"><lb:get key="label_rm.label_core_requirements_management_42"/></td>
										</tr>
										<c:forEach items="${result}" var="r" varStatus="status">
											<tr>
												<td align="left">${status.index+1}</td>
												<td>${r.label }</td>
												<td>
													<div class="progress progress-xs">
														<div style="width: <fmt:formatNumber type="number" value="${r.count/r.total*100}" pattern="0.00" maxFractionDigits="2"/>%;line-height:2;"
															class="${cssClass[status.index]}"></div>
													</div><fmt:formatNumber type="number" value="${r.count/r.total*100}" pattern="0.00" maxFractionDigits="2"/>%
												</td>
												<td align="right">${r.count}</td>
											</tr>
										</c:forEach>
										<!-- <tr>
											<td align="center">1</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-blue"></div>
												</div>20%
											</td>
											<td align="center">11</td>
										</tr>
										<tr>
											<td align="center">2</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-purple"></div>
												</div>20%
											</td>
											<td align="center">22</td>
										</tr>
										<tr>
											<td align="center">3</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-green"></div>
												</div>20%
											</td>
											<td align="center">33</td>
										</tr>
										<tr>
											<td align="center">4</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-orange"></div>
												</div>20%
											</td>
											<td align="center">44</td>
										</tr>
										<tr>
											<td align="center">5</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-pink"></div>
												</div>20%
											</td>
											<td align="center">55</td>
										</tr>
										<tr>
											<td align="center">6</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-yellow"></div>
												</div>20%
											</td>
											<td align="center">55</td>
										</tr>
										<tr>
											<td align="center">7</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-indigo"></div>
												</div>20%
											</td>
											<td align="center">55</td>
										</tr>
										<tr>
											<td align="center">8</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%" class="progress-bar progress-bar-red"></div>
												</div>20%
											</td>
											<td align="center">55</td>
										</tr>
										<tr>
											<td align="center">9</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 20%"
														class="progress-bar progress-bar-light"></div>
												</div>20%
											</td>
											<td align="center">55</td>
										</tr>
										<tr>
											<td align="center">10</td>
											<td>新员工入职培训</td>
											<td>
												<div class="progress progress-xs">
													<div style="width: 30%"
														class="progress-bar progress-bar-breen"></div>
												</div>30%
											</td>
											<td align="center">55</td>
										</tr> -->
									</tbody>
								</table>
							</div>
							
						</c:when>
						<c:otherwise>
							<div style="margin-top:10px" class="losedata">
								<i class="fa fa-folder-open-o"></i>
								<p><lb:get key="voting_not_result" /></p>
							</div>
						</c:otherwise>
					</c:choose>
					
				</div>
			</div>
			<!-- wzb-tab-2 end -->

		</div>
	</div>
	<!-- wzb-panel End-->
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rm_${lang}.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_item.js"></script>
	<script type="text/javascript" src="${ctx}/js/wb_application.js"></script>
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
					label : labe
			};
			PieData.push(dataItem);
		</c:forEach>
	</script>
	<script type="text/javascript"
		src="${ctx}/static/admin/js/voting/viewResult.js"></script>
</body>
</html>
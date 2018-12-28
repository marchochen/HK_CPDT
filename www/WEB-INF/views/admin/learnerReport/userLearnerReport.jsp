<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/bootstrap/css/bootstrap.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/admin/css/font-awesome/css/font-awesome.min.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/layer/skin/layer.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/thickbox.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/admin/css/admin.css"/>
<c:if test="${lang eq 'en-us'}">
	<!-- 兼容英文的css -->
	<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
	<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
</c:if>
<link rel="stylesheet" type="text/css" href="${ctx}/js/datepicker/laydate/skins/molv/laydate.css" />

<script type="text/javascript" src="${ctx}/js/jquery.js"></script>
<script type="text/javascript" src="${ctx}/static/js/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/base.js"></script>
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.core.js"></script>
<script type="text/javascript" src="${ctx}/js/tree/js/jquery.ztree.excheck.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jsrender.js"></script>
<script type="text/javascript" src="${ctx}/static/admin/js/Chart.min.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rp_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript">
window.onload = function(){
	highLightMainMenu("FTN_AMD_TRAINING_REPORT_MGT");//高亮菜单
}

function down_excel(){
	var downloadPath ='';
	
	layer.open({
	 	  type: 1,//弹出类型 
	      area: ['500px', '274px'], //宽高
	      title : fetchLabel("label_core_report_140"),//标题 
		  content: '<div class="pop-up-word">'+
		 				'<span id="successMsg" style="display:none;"><lb:get key="label_rp.label_core_report_163"/></span>'+
		 				'<div id="download_loading" class="layer-loading"></div>'+
		 			'</div>'+
		 			'<div class="wzb-bar">'+
		 				'<input id="downloadBtn" disabled="disabled" value="<lb:get key="label_rp.label_core_report_139"/>" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">'+
		 			'</div>',
		  success: function(layero, index){
				$.ajax({
			        url : "${ctx}/app/admin/learnerReport/expor",
			        type : 'POST',
			        data:  {voJsonString : '${voJsonString}'},
			        dataType : 'json',
			        traditional : true,
			        success : function(data) {
			        	downloadPath = data.fileUri;
			        	$('#download_loading').hide();
			        	$('#successMsg').show();
			        	$('#downloadBtn').removeAttr("disabled");
			        	$('#downloadBtn').click(function(){
			        		if(downloadPath!=''){
			        			window.location.href = downloadPath;
			        		}
			        	});
			        }
			     });
		  }
	});
}

function back(){
	$("#formBack").submit();
}
</script>

</head>
<body>

		 <input type="hidden" name="belong_module" value=FTN_AMD_TRAINING_REPORT_MGT/>
		 
		 <form id="formBack" action="${ctx}/app/admin/learnerReport/back"  method="post">
			 <input type="hidden" name="pageExportUser" id="pageExportUser" value="${pageExportUser }"/>
			 <input type="hidden" name="pageExportUserIdsText" id="pageExportUserIdsText" value="${pageExportUserIdsText }"/>
			 <input type="hidden" name="pageExportCourse" id="pageExportCourse" value="${pageExportCourse }"/>
			 <input type="hidden" name="pageExportCourseIdsText" id="pageExportCourseIdsText" value="${pageExportCourseIdsText }"/>
			 <input type="hidden" name="pageCourseType" id="pageCourseType" value="${pageCourseType }"/>
			 <input type="hidden" name="pageAppnStartDatetime" id="pageAppnStartDatetime" value="${pageAppnStartDatetime }"/>
			 <input type="hidden" name="pageAppnEndDatetime" id="pageAppnEndDatetime" value="${pageAppnEndDatetime }"/>
			 <input type="hidden" name="pageAttStartTime" id="pageAttStartTime" value="${pageAttStartTime }"/>
			 <input type="hidden" name="pageAttEndTime" id="pageAttEndTime" value="${pageAttEndTime }"/>
			 <input type="hidden" name="pageAppStatus" id="pageAppStatus" value="${pageAppStatus }"/>
		   	 <input type="hidden" name="pageCourseStatus" id="pageCourseStatus" value="${pageCourseStatus }"/>
			 <input type="hidden" name="pageResultDataStatistic" id="pageResultDataStatistic" value="${pageResultDataStatistic }"/>
			 <input type="hidden" name="pageIsExportDetail" id="pageIsExportDetail" value="${pageIsExportDetail }"/>
			 <input type="hidden" name="pageUserInfo" id="pageUserInfo" value="${pageUserInfo }"/>
			 <input type="hidden" name="pageCourseInfo" id="pageCourseInfo" value="${pageCourseInfo }"/>
			 <input type="hidden" name="pageOtherInfo" id="pageOtherInfo" value="${pageOtherInfo }"/>
		 </form>
		
 	 	<title:get function="global.FTN_AMD_TRAINING_REPORT_MGT"/>
    	
      
        <ol class="breadcrumb wzb-breadcrumb">
            <li>
            	<a href="javascript:wb_utils_gen_home(true);">
            		<i class="fa wzb-breadcrumb-home fa-home"></i>
            		<lb:get key="label_lm.label_core_learning_map_1" />
            	</a>
            </li>
            <li><a href="javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');"><lb:get key="global.FTN_AMD_TRAINING_REPORT_MGT" /></a></li>
            <li class="active"><lb:get key="global.FUN_LEARNER" /></li>
        </ol>
        
        
        <div class="panel wzb-panel">
		    <div class="report-title">
		        <div class="wzb-title-15">
		            <i class="report-yi"></i><lb:get key="label_rp.label_core_report_44" />
		        </div>
		    </div>
		    
		    <div>
		    	<p class="margin-top20 margin-left15 font-size18" style="display:block;"><lb:get key="label_rp.label_core_report_135" /></p>
				
		        <jsp:include page="choseCondition.jsp"></jsp:include>
		        
        		<div style="width:100%;height:10px;background:#f2f2f2;margin-bottom:20px;"></div>
        		
        		<div class="margin-top28 cleafix" style="width:100%;">
		            <div style="width:100%;display:inline-block;">
		                <div class="col-sm-4">
		                    <table class="report-table-td">
		                        <tbody>
		                            <tr>
		                                <td class="wzb-form-label"><p class="font-size18 color-gray333" style="display:block;"><lb:get key="label_rp.label_core_report_127" /></p></td>
		                                <td class="wzb-form-control"></td>
		                            </tr>
		                            <tr>
		                                <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_123" />：</td>
		                                <td class="wzb-form-control">
		                                	<c:out value="${reportData.totleUserCount}" default="0"></c:out>
		                                </td>
		                            </tr>
		                            <tr>
		                                <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_110" />：</td>
		                                <td class="wzb-form-control">
		                                	<c:out value="${reportData.enrollUserCount}" default="0"></c:out>
		                                </td>
		                            </tr>
		                            <tr>
		                                <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_124" />：</td>
		                                <td class="wzb-form-control">
		                                	<c:out value="${reportData.avgEnrollUserCount}" default="0"></c:out>
		                                </td>
		                            </tr>
		                            <tr>
		                                <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_125" />：</td>
		                                <td class="wzb-form-control">
		                                	<c:out value="${reportData.totleCovTotleTime}" default="0"></c:out>
		                                </td>
		                            </tr>
		                            <tr>
		                                <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_126" />：</td>
		                                <td class="wzb-form-control">
		                                	<c:out value="${reportData.avgCovTotleTime}" default="0"></c:out>
		                                </td>
		                            </tr>
		                        </tbody>
		                    </table>
		                </div>
		                <div class="col-sm-8">
		                    <div class="chart-box-2 clearfix">
		                    	<c:if test="${reportData.pieDataTotleValue gt 0}">
			                        <div class="chart-responsive-2">
			                            <canvas id="pieChart" width="260" height="260" style="width: 260px; height: 260px;"></canvas>
			                        </div>
			                       <c:set var="cssClass" value="${fn:split('progress-bar-blue,progress-bar-purple,progress-bar-green,
			                       			progress-bar-orange,progress-bar-pink,progress-bar-yellow,progress-bar-indigo,progress-bar-red,
			                       			progress-bar-light,progress-bar-breen', ',')}" />
			                       <ul class="chart-legend-2 clearfix">
			                       
	                       			<c:forEach items="${reportData.pieDataList}" var="r" varStatus="status">
										<li>
											<div style="display:inline-block;">
												<p><c:out value="${r.label}"></c:out></p>
												<em><i class="fa fa-circle-o ${cssClass[status.index]}"></i>
					                            	<span>
					                            		
					                            		<fmt:formatNumber type="number" value="${r.value}" pattern="0" 
					                            			maxFractionDigits="0"/>
				                            			<span class="color-gray999">
					                            			(<fmt:formatNumber type="number" value="${r.percentage}" pattern="0.00" 
					                            			maxFractionDigits="2"/>%)
				                            			</span>
					                            	</span>
			                            		</em>
			                            	</div>
		                            	</li>
									</c:forEach>
		                        	</ul>
		                        </c:if>

		                        <script type="text/javascript">
		                            $(function(){
		                              var pie = $("#pieChart").get(0);
		                              if(pie != undefined && pie != ''){
			                              var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
			                              var pieChart = new Chart(pieChartCanvas);
				                      	  var PieData = [];
				                    	  var colors = ['#00c0ef','#dd4b39','#00a65a','#f39c12','#c86af1','#ffd21d','#2d3e50','#ff0000','#657694','#428bca'];
				                    		<c:forEach items="${reportData.pieDataList}" var="r" varStatus="status">
				                    		    var labe="${r.label}";
				                    			var dataItem = {
				                    					value:${r.percentage},
				                    					color:colors[${status.index}],
				                    					highlight:colors[${status.index}],
				                    					label : labe
				                    			};
				                    			PieData.push(dataItem);
				                    		</c:forEach>
			                              var pieOptions = {
			                                segmentShowStroke: true,
			                                segmentStrokeColor: "#fff",
			                                segmentStrokeWidth: 1,
			                                percentageInnerCutout: 50, 
			                                animationSteps: 100,
			                                animationEasing: "easeOutBounce",
			                                animateRotate: true,
			                                animateScale: false,
			                                responsive: true,
			                                maintainAspectRatio: false,
			                                tooltipTemplate: "<%= "<%=label%\>"%> : <%= "<%=value%\>"%>%"
			                              };
			                              pieChart.Doughnut(PieData, pieOptions);
			                            }
		                            }	  
		                            );
		                        </script>
		                    </div>
		                </div>
		            </div>
		        </div>
		        
		        <div style="width:100%;height:10px;background:#f2f2f2;margin-bottom:20px;"></div>
	            <div style="overflow:auto;">
	            	
		            <div class="tab-content report-table" style="width:3000px;padding:0 15px;">
		                <div class="tab-pane active datatable" style="width:100%;">
		                    <div class="datatable-body" style="overflow:auto;">
		                        <table cellpadding="0" cellspacing="0" class="datatable-table">
		                            <thead class="datatable-table-thead">
		                                <tr class="datatable-table-row">
		                                    <th width="4%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_78" /></th>
		                                    <th width="4%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_79" /></th>
		                                    <th width="9%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_80" /></th>
		                                    <th width="5%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_110" /></th>
	                                    	<c:if test="${fn:contains(choseCondition.courseStatus,'C')}">
		                                    	<th width="3%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_66" /></th>
		                                    	<th width="3%" class="datatable-table-column-header">（%）</th>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.courseStatus,'F')}">
		                                    	<th width="3%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_67" /></th>
		                                    	<th width="3%" class="datatable-table-column-header">（%）</th>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.courseStatus,'W')}">
		                                    	<th width="3%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_68" /></th>
		                                    	<th width="3%" class="datatable-table-column-header">（%）</th>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.courseStatus,'I')}">
		                                    	<th width="3%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_65" /></th>
		                                    	<th width="3%" class="datatable-table-column-header">（%）</th>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.appStatus,'Pending')}">
		                                    	<th width="4%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_112" /></th>
		                                    	<th width="3%" class="datatable-table-column-header">（%）</th>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.appStatus,'Rejected')}">
		                                    	<th width="3%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_64" /></th>
		                                    	<th width="3%" class="datatable-table-column-header">（%）</th>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.appStatus,'Waiting')}">
		                                    	<th width="4%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_63" /></th>
		                                    	<th width="3%" class="datatable-table-column-header">（%）</th>
		                                    </c:if>
		                                    <th width="7%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_114" /></th>
		                                    <th width="6%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_115" /></th>
		                                    <th width="6%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_116" /></th>
		                                    <th width="3%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_117" /></th>
		                                    <th width="3%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_118" /></th>
		                                    <th width="4%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_119" /></th>
                                            <th width="4%" class="datatable-table-column-header"><lb:get key="label_rp.label_core_report_165" /></th>
		                                </tr>
		                            </thead>
		                            <tbody class="datatable-table-tbody">
		                            <c:forEach items="${reportData.reportList}" var="itm" begin="0" end="9">
		                            	<tr class="datatable-table-row">
		                                    <td class="datatable-table-column"><c:out value="${itm.userDispalyName}"/></td>
		                                    <td class="datatable-table-column"><c:out value="${itm.userFullName}"/></td>
		                                    <td class="datatable-table-column"><c:out value="${itm.gourpName}"/></td>
		                                    <td class="datatable-table-column"><c:out value="${itm.enrollCount}"/></td>
		                                    
		                                    <c:if test="${fn:contains(choseCondition.courseStatus,'C')}">
		                                    	<c:if test="${not empty itm.completedCount}">
                                    				<td class="datatable-table-column"><c:out value="${itm.completedCount}"/></td>
                                    				<td class="datatable-table-column"><c:out value="${itm.completedPercentage}"/>%</td>
		                                    	</c:if>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.courseStatus,'F')}">
                                    			<c:if test="${not empty itm.failCount}">
			                                    	<td class="datatable-table-column"><c:out value="${itm.failCount}"/></td>
			                                    	<td class="datatable-table-column"><c:out value="${itm.failPercentage}"/>%</td>
			                                    </c:if>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.courseStatus,'W')}">
                             			        <c:if test="${not empty itm.withdrawnCount}">
			                                    	<td class="datatable-table-column"><c:out value="${itm.withdrawnCount}"/></td>
			                                    	<td class="datatable-table-column"><c:out value="${itm.withdrawnPercentage}"/>%</td>
			                                    </c:if>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.courseStatus,'I')}">
                                			    <c:if test="${not empty itm.inProgressCount}">
			                                    	<td class="datatable-table-column"><c:out value="${itm.inProgressCount}"/></td>
			                                    	<td class="datatable-table-column"><c:out value="${itm.inProgressPercentage}"/>%</td>
			                                    </c:if>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.appStatus,'Pending')}">
		                                    	<c:if test="${not empty itm.penddingCount}">
			                                    	<td class="datatable-table-column"><c:out value="${itm.penddingCount}"/></td>
			                                    	<td class="datatable-table-column"><c:out value="${itm.penddingPercentage}"/>%</td>
			                                    </c:if>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.appStatus,'Rejected')}">
	                                    		<c:if test="${not empty itm.rejectedCount}">
			                                    	<td class="datatable-table-column"><c:out value="${itm.rejectedCount}"/></td>
			                                    	<td class="datatable-table-column"><c:out value="${itm.rejectedPercentage}"/>%</td>
			                                    </c:if>
		                                    </c:if>
		                                    <c:if test="${fn:contains(choseCondition.appStatus,'Waiting')}">
		                                    	<c:if test="${not empty itm.waitingCount}">
			                                    	<td class="datatable-table-column"><c:out value="${itm.waitingCount}"/></td>
			                                    	<td class="datatable-table-column"><c:out value="${itm.waitingPercentage}"/>%</td>
			                                    </c:if>
		                                    </c:if>
 
		                                    <td class="datatable-table-column">
		                                    	<c:choose>
			                                    	<c:when test="${not empty itm.totleCovTotleDisplayTime}">
			                                    		<c:out value="${itm.totleCovTotleDisplayTime}"/>
			                                    	</c:when>
			                                    	<c:otherwise>
			                                    		--
			                                    	</c:otherwise>
			                                    </c:choose>
		                                    </td>
		                                    <td class="datatable-table-column">
			                                    <c:choose>
			                                    	<c:when test="${not empty itm.avgCovTotleDisplayTime}">
			                                    		<c:out value="${itm.avgCovTotleDisplayTime}"/>
			                                    	</c:when>
			                                    	<c:otherwise>
			                                    		--
			                                    	</c:otherwise>
			                                    </c:choose>
		                                    </td>
											<c:choose>
												<c:when test="${itm.avgAttRatePercentage == null}">
													<td class="datatable-table-column">--</td>
												</c:when>
												<c:otherwise>
													<td class="datatable-table-column"><c:out value="${itm.avgAttRatePercentage}"/></td>
												</c:otherwise>
											</c:choose>
		                                    <c:choose>
		                                    	<c:when test="${itm.maxScore == null}">
		                                    		<td class="datatable-table-column">--</td>
		                                    	</c:when>
		                                    	<c:otherwise>
		                                    		<td class="datatable-table-column"><c:out value="${itm.maxScore}"/></td>
		                                    	</c:otherwise>
		                                    </c:choose>
		                                    <c:choose>
		                                    	<c:when test="${itm.minScore == null}">
		                                    		<td class="datatable-table-column">--</td>
		                                    	</c:when>
		                                    	<c:otherwise>
		                                    		<td class="datatable-table-column"><c:out value="${itm.minScore}"/></td>
		                                    	</c:otherwise>
		                                    </c:choose>
		                                    <c:choose>
		                                    	<c:when test="${itm.avgScore == null}">
		                                    		<td class="datatable-table-column">--</td>
		                                    	</c:when>
		                                    	<c:otherwise>
		                                    		<td class="datatable-table-column"><c:out value="${itm.avgScore}"/></td>
		                                    	</c:otherwise>
		                                    </c:choose>
                                            <c:choose>
                                                <c:when test="${itm.totalIesCredit == null}">
                                                    <td class="datatable-table-column">--</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td class="datatable-table-column"><c:out value="${itm.totalIesCredit}"/></td>
                                                </c:otherwise>
                                            </c:choose>
		                                </tr>
		                            </c:forEach>
		                            </tbody>
		                        </table>
		                        <c:if test="${fn:length(reportData.reportList) == 0}">
		                         <div class="datatable-stat">
	                                 <div class="losedata" style='margin-top:50px'>
	                                   <i class="fa fa-folder-open-o"></i>
	                                   <p><lb:get key="lab_table_empty" /></p>
	                                 </div>
	                              </div>
	                            </c:if> 
		                    </div>
		                </div>
		            </div>
		        </div>
		        <c:choose>
		        	<c:when test="${fn:length(reportData.reportList)>10}">
		        		<p class="color-gray999 margin-left15"><lb:get key="label_rp.label_core_report_136" />&nbsp;<c:out value="${fn:length(reportData.reportList)}"></c:out>&nbsp;<lb:get key="label_rp.label_core_report_137" /></p>
		        	</c:when>
		        	<c:otherwise>
		        		<p class="color-gray999 margin-left15"><lb:get key="label_rp.label_core_report_136" />&nbsp;<c:out value="${fn:length(reportData.reportList)}"></c:out>&nbsp;<lb:get key="label_rp.label_core_report_158" /></p>
		        	</c:otherwise>
		        </c:choose>
	            <div class="wzb-bar">
	            	<c:if test="${fn:length(reportData.reportList)>0}">
	                	<input style="" name="down_excel" onclick="down_excel();" value="<lb:get key="label_rp.label_core_report_109"/>" type="button" class="btn wzb-btn-blue wzb-btn-big margin-right10">
	               </c:if>
                   <c:choose>
                       <c:when test="${pageReturnRptAll==true}">
                            <input style="" name=" " value="<lb:get key="button_back"/>" onclick="javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
                       </c:when>
                       <c:otherwise>
                            <input name=" " onclick="Javascript:back();" value="<lb:get key="button_back"/>" type="button" class="btn wzb-btn-blue wzb-btn-big">
                        </c:otherwise>
                   </c:choose>
	            </div>
		    </div>
        </div>  <!-- panel End-->

</body>
</html>
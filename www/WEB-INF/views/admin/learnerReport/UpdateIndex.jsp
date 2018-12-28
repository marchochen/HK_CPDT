<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/admin/css/admin.css"/>
<c:if test="${lang eq 'en-us'}">
	<!-- 兼容英文的css -->
	<link rel="stylesheet" href="${ctx}/static/css/base-en.css"/>
	<link rel="stylesheet" href="${ctx}/static/admin/css/admin-en.css"/>
</c:if>
<link rel="stylesheet" href="${ctx}/js/tree/css/ztree.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/datepicker/skin/WdatePicker.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/datepicker/laydate/skins/molv/laydate.css"/>
<script type="text/javascript" src="${ctx}/static/js/layer/layer.js"></script> 
<script type="text/javascript" src="${ctx}/js/datepicker/laydate/laydate.js"></script>
<script type="text/javascript" src="${ctx}/js/date-picker.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_mgt_rpt.js"></script>

<script>
mgt_rpt = new wbManagementReport;

window.onload = function(){

	if('${pageExportUser}' != "") {		// 学员
		var arr_exportUser = '${pageExportUser}'.split(",");
        var arr_exportUserIds = "${pageExportUserIds}".split(",");
		var arr_exportUserIdsText = "${pageExportUserIdsText}".split(",");
		$("input[name='exportUser']").each(function(){
			if(this.value == arr_exportUser[0]) {
				$("input[name='exportUser']").eq(this.value).attr("checked", true);
				if(arr_exportUser[0] == "0") {
					if(arr_exportUser[1]){
						$("input[name='includeDelUser']").attr("checked", true);
					}
				} else if(arr_exportUser[0] == "1") {
					$("input[name='includeDelUser']").attr("disabled", true);
					for(var i=0; i<arr_exportUserIds.length; i++) {
						$("#exportUserIds").append("<option value="+arr_exportUserIds[i]+">"+arr_exportUserIdsText[i]+"</option>")
					}
				} else {
					$("input[name='includeDelUser']").attr("disabled", true);
					for(var i=0; i<arr_exportUserIds.length; i++) {
						$("#exportGroupIds").append("<option value="+arr_exportUserIds[i]+">"+arr_exportUserIdsText[i]+"</option>")
					}
				}
			}
		});
	} else {
		$("input[name='exportUser']").eq(0).attr("checked", true);
		$("input[name='includeDelUser']").attr("checked", true);
	}
	
	if('${pageExportCourse}' != "") {			// 课程/考试
		var arr_exportCourse = '${pageExportCourse}'.split(",");
        var arr_exportCourseIds = '${pageExportCourseIds}'.split(",");
		var arr_exportCourseIdsText = '${pageExportCourseIdsText}'.split(",");
		$("input[name='exportCourse']").each(function(){
			if(this.value == arr_exportCourse[0]) {
				$("input[name='exportCourse']").eq(this.value).attr("checked", true);
				if(arr_exportCourse[0] == "1") {
					for(var i=0; i<arr_exportCourseIds.length; i++) {
						$("#exportCourseIds").append("<option value="+arr_exportCourseIds[i]+">"+arr_exportCourseIdsText[i]+"</option>")
					}
				} else if(arr_exportCourse[0] == "2") {
					for(var i=0; i<arr_exportCourseIds.length; i++) {
						$("#exportCatalogIds").append("<option value="+arr_exportCourseIds[i]+">"+arr_exportCourseIdsText[i]+"</option>")
					}
				}
			}
		});
	} else {
		$("input[name='exportCourse']").eq(0).attr("checked", true);
	}
	
	if('${pageCourseType}' != "") {		// 课程/考试类型
		var arr_courseType = '${pageCourseType}'.split(",");	
		arr_courseType.forEach(function(item){
			$("input[name='courseType']").each(function(){
				if($(this).val() == item) {
					$(this).attr("checked", true);
				}
			});
		});
	} else {
		$("input[name^='courseType']").attr("checked", true);
	}
	
	if('${pageAppnStartDatetime}' != "") {	// 报名日期
		var arr_appnStartDatetime = '${pageAppnStartDatetime}'.split(",");
		$("#appn_start_datetime_yy").val(arr_appnStartDatetime[0]);
		$("#appn_start_datetime_mm").val(arr_appnStartDatetime[1]);
		$("#appn_start_datetime_dd").val(arr_appnStartDatetime[2]);
	}
	if('${pageAppnEndDatetime}' != "") {
		var arr_appnEndDatetime = '${pageAppnEndDatetime}'.split(",");
		$("#appn_end_datetime_yy").val(arr_appnEndDatetime[0]); 
		$("#appn_end_datetime_mm").val(arr_appnEndDatetime[1]); 
		$("#appn_end_datetime_dd").val(arr_appnEndDatetime[2]); 
	}
	
	if('${pageAttStartTime}' != "") {	// 结训日期
		var arr_attStartTime = '${pageAttStartTime}'.split(",");
		$("#att_start_time_yy").val(arr_attStartTime[0]);
		$("#att_start_time_mm").val(arr_attStartTime[1]);
		$("#att_start_time_dd").val(arr_attStartTime[2]);
	}
	if('${pageAttEndTime}' != "") {
		var arr_attEndTime = '${pageAttEndTime}'.split(",");
		$("#att_end_time_yy").val(arr_attEndTime[0]);
		$("#att_end_time_mm").val(arr_attEndTime[1]);
		$("#att_end_time_dd").val(arr_attEndTime[2]);
	}
	
	if('${pageAppStatus}' != "") {	// 报名状态
		var arr_appStatus = '${pageAppStatus}'.split(",");	
		arr_appStatus.forEach(function(item){
			$("input[name='appStatus']").each(function(){
				if($(this).val() == item) {
					$(this).attr("checked", true);
				}
			});
		});
	} else if('${pageCourseStatus}' != ""){
		$("input[name^='appStatus']").attr("checked", false);
	}else{
		$("input[name^='appStatus']").attr("checked", true);
	}
	
	if('${pageCourseStatus}' != "") {	// 学习状态
		var arr_courseStatus = '${pageCourseStatus}'.split(",");	
		arr_courseStatus.forEach(function(item){
			$("input[name='courseStatus']").each(function(){
				if($(this).val() == item) {
					$(this).attr("checked", true);
				}
			});
		});
	}else if('${pageAppStatus}' != ""){
		$("input[name^='courseStatus']").attr("checked", false);
	} else {
		$("input[name^='courseStatus']").attr("checked", true);
	}
	
	var courseCreditValid = 0;
	if('${pageResultDataStatistic}' != "") {	// 结果数据统计方式
		var arr_resultDataStatistic = '${pageResultDataStatistic}'.split(",");
		if(arr_resultDataStatistic[0] == "0") {
			$("input[name='resultDataStatistic']").eq(0).attr("checked", true);
			$("input[name='includeNoDataUser']").attr("disabled", true);
			if(arr_resultDataStatistic[1]) {
				$("input[name='includeNoDataCourse']").attr("checked", true);
			}
		} else {
			courseCreditValid=1;
			$("input[name='resultDataStatistic']").eq(1).attr("checked", true);
			$("input[name='includeNoDataUser']").attr("disabled", false);
			$("input[name='includeNoDataCourse']").attr("disabled", true);
			if(arr_resultDataStatistic[1]) {
				$("input[name='includeNoDataUser']").attr("checked", true);
			}
		}
	} else {
		$("input[name='resultDataStatistic']").eq(0).attr("checked", true);
		$("input[name='includeNoDataCourse']").attr("checked", true);
	}
	
	if('${pageIsExportDetail}' != "") {		// 同时导出明细记录
		var arr_isExportDetail = '${pageIsExportDetail}'.split(",");
		var arr_userInfo = '${pageUserInfo}'.split(",");
		var arr_courseInfo = '${pageCourseInfo}'.split(",");
		var arr_otherInfo = '${pageOtherInfo}'.split(",");
		if(arr_isExportDetail[0] == 'true') {
			$("input[name='isExportDetail']").eq(0).attr("checked", true);
			$("input[name^=userInfo]").attr("disabled", false);
			$("input[name^=courseInfo]").attr("disabled", false);
			$("input[name^=otherInfo]").attr("disabled", false);
		} else {
			$("input[name='isExportDetail']").eq(1).attr("checked", true);
		}
		arr_userInfo.forEach(function(item){		// 用户信息
			$("input[name='userInfo']").each(function(){
				if(this.value == item) {
					$(this).attr("checked", true);
				}
			});
		});
		arr_courseInfo.forEach(function(item){		// 课程信息
			$("input[name='courseInfo']").each(function(){
				if(this.value == item) {
					$(this).attr("checked", true);
				}
			});
		});
		arr_otherInfo.forEach(function(item){		// 其他
			$("input[name='otherInfo']").each(function(){
				if(this.value == item) {
					if(item=="courseCredit"){
                        $(this).attr("checked", true);
	                    if(courseCreditValid==0){
                            $(this).attr("disabled", true);
	                    }
	                }else{
	                	$(this).attr("checked", true);
	                }
					
				}
			});
		});
	} else {
		$("input[name='isExportDetail']").eq(1).attr("checked", true);
		$("input[name^=userInfo]").attr("checked", true);
		$("input[name^=courseInfo]").attr("checked", true);
		$("input[name^=otherInfo]").attr("checked", true);
	}
	
	highLightMainMenu("FTN_AMD_TRAINING_REPORT_MGT");//高亮菜单
	
    /* laydate.skin('molv');
	var start = {
	  elem: '#appnStartDatetime',
	  format: 'YYYY-MM-DD'
	};
	var end = {
	  elem: '#appnEndDatetime',
	  format: 'YYYY-MM-DD',
	  max: '2099-06-16'
	};
	
	//laydate(start);
	//laydate(end);
	
	var time_start = {
	   elem: '#attStartTime',
	   format: 'YYYY-MM-DD'
	};
	var time_end = {
	  elem: '#attEndTime',
	  format: 'YYYY-MM-DD',
	  max: '2099-06-16'
	};
	
	//laydate(time_start);
	//laydate(time_end);
	laydate(time_start);
	laydate(time_end); */
	 $('#view_report_btn').click(function(){
		 if(!validateForm()){
			 return;
		 }
		 layer.load();
		 
		 var page_exportUser = new Array();
		 var page_exportUserIds_text = new Array();
		 var page_exportCourse = new Array();
		 var page_exportCourseIds_text = new Array();
		 var page_courseType = new Array();
		 var page_appnStartDatetime = new Array();
		 var page_appnEndDatetime = new Array();
		 var page_attStartTime = new Array();
		 var page_attEndTime = new Array();
		 var page_appStatus = new Array();
		 var page_courseStatus = new Array();
		 var page_resultDataStatistic = new Array();
		 var page_isExportDetail = new Array();
		 var page_userInfo = new Array();
		 var page_courseInfo = new Array();
		 var page_otherInfo = new Array();
		 
		 var exportUser_val = $("input[name='exportUser']:checked").val();	// 学员
		 page_exportUser.push(exportUser_val);	
		 if(exportUser_val == '0') {
			 page_exportUser.push($("input[name='includeDelUser']:checked").val());
		 }
		 if(exportUser_val == '1') {
			 $("#exportUserIds option").each(function(){
				 page_exportUser.push(this.value);
				 page_exportUserIds_text.push($(this).text());
			 });
		 }
		 if(exportUser_val == '2') {
			 $("#exportGroupIds option").each(function(){
				 page_exportUser.push(this.value);
				 page_exportUserIds_text.push($(this).text());
			 });
		 }
		 $("#pageExportUser").val(page_exportUser);
		 $("#pageExportUserIdsText").val(page_exportUserIds_text);
		 
		 var exportCourse_val = $("input[name='exportCourse']:checked").val();	// 课程/考试
		 page_exportCourse.push(exportCourse_val);
		 if(exportCourse_val == '1') {
			 $("#exportCourseIds option").each(function(){
				 page_exportCourse.push(this.value);
				 page_exportCourseIds_text.push($(this).text());
			 });
		 }
		 if(exportCourse_val == '2') {
			 $("#exportCatalogIds option").each(function(){
				 page_exportCourse.push(this.value);
				 page_exportCourseIds_text.push($(this).text());
			 });
		 }
		 $("#pageExportCourse").val(page_exportCourse);
		 $("#pageExportCourseIdsText").val(page_exportCourseIds_text);
		 
 	     $("input[name='courseType']:checked").each(function(){	// 课程/考试类型
 	    	page_courseType.push(this.value);
		 });
 	    $("#pageCourseType").val(page_courseType);
 	     
 	     var appnStartDatetime_yy = $("#appn_start_datetime_yy").val();	// 报名日期
 	     var appnStartDatetime_mm = $("#appn_start_datetime_mm").val();
 	     var appnStartDatetime_dd = $("#appn_start_datetime_dd").val();
 	     var appnEndDatetime_yy = $("#appn_end_datetime_yy").val();
 	     var appnEndDatetime_mm = $("#appn_end_datetime_mm").val();
 	     var appnEndDatetime_dd = $("#appn_end_datetime_dd").val();
 	     if(appnStartDatetime_yy != "") {
 	    	page_appnStartDatetime.push(appnStartDatetime_yy);
 	    	page_appnStartDatetime.push(appnStartDatetime_mm);
 	    	page_appnStartDatetime.push(appnStartDatetime_dd);
 	     }
 	    $("#pageAppnStartDatetime").val(page_appnStartDatetime);
    	if(appnEndDatetime_yy != "") {
    		page_appnEndDatetime.push(appnEndDatetime_yy);
    		page_appnEndDatetime.push(appnEndDatetime_mm);
    		page_appnEndDatetime.push(appnEndDatetime_dd);
    	}
    	$("#pageAppnEndDatetime").val(page_appnEndDatetime);
 	     
 	     var attStartTime_yy = $("#att_start_time_yy").val();	// 结训日期
 	     var attStartTime_mm = $("#att_start_time_mm").val();	
 	     var attStartTime_dd = $("#att_start_time_dd").val();
 	     var attEndTime_yy = $("#att_end_time_yy").val();
	     var attEndTime_mm = $("#att_end_time_mm").val();
	     var attEndTime_dd = $("#att_end_time_dd").val();
 	     if(attStartTime_yy != "") {
 	    	page_attStartTime.push(attStartTime_yy);
 	    	page_attStartTime.push(attStartTime_mm);
 	    	page_attStartTime.push(attStartTime_dd);
 	     }
 	     $("#pageAttStartTime").val(page_attStartTime);
 	     if(attEndTime_yy != "") {
 	    	page_attEndTime.push(attEndTime_yy);
 	    	page_attEndTime.push(attEndTime_mm);
 	    	page_attEndTime.push(attEndTime_dd);
 	     }
 	     $("#pageAttEndTime").val(page_attEndTime);
 	     
 	     $("input[name='appStatus']:checked").each(function(){	// 报名状态
 	    	page_appStatus.push(this.value);
 	     });
 	     $("#pageAppStatus").val(page_appStatus);
 	     
 	     $("input[name='courseStatus']:checked").each(function(){	// 学习状态
 	    	page_courseStatus.push(this.value);
 	     });
 	     $("#pageCourseStatus").val(page_courseStatus);
 	     
 	     var resultDataStatistic_val = $("input[name=resultDataStatistic]:checked").val();	// 结果数据统计方式
 	     var includeNoDataCourse_val = $("input[name=includeNoDataCourse]:checked").val();
 	     var includeNoDataUser_val = $("input[name=includeNoDataUser]:checked").val();
 	     page_resultDataStatistic.push(resultDataStatistic_val);
 	     if(resultDataStatistic_val == '0') {
	 	     page_resultDataStatistic.push(includeNoDataCourse_val);
 	     } else {
	 	     page_resultDataStatistic.push(includeNoDataUser_val);
 	     }
 	     $("#pageResultDataStatistic").val(page_resultDataStatistic);
 	     
 	     var isExportDetail_val = $("input[name='isExportDetail']:checked").val();	// 同时导出明细记录
 	     $("#pageIsExportDetail").val(isExportDetail_val);
    	 $("input[name='userInfo']:checked").each(function(){	// 用户信息
    		page_userInfo.push(this.value);
    	 });
    	 $("#pageUserInfo").val(page_userInfo);
    	 $("input[name='courseInfo']:checked").each(function(){	// 课程信息
    		 page_courseInfo.push(this.value);
    	 })
    	 $("#pageCourseInfo").val(page_courseInfo);
    	 $("input[name='otherInfo']:checked").each(function(){	// 其他
    		 page_otherInfo.push(this.value);
    	 })
    	 $("#pageOtherInfo").val(page_otherInfo);
 	     
		 var checkVal = $('input[name="resultDataStatistic"]:checked').val();
		 if(checkVal==0){
			  $('#formCondition').attr('action','${ctx}' + '/app/admin/learnerReport/getLearnerReportByCourse');
		 }else{
			 $('#formCondition').attr('action','${ctx}' + '/app/admin/learnerReport/getLearnerReportByUser');
		 }
		 var userNames=new Array();
		 $("#exportUserIds option").each(function(index){  
			  userNames[index]=$(this).text();
	          $(this).attr('selected','selected'); 
	     })  
	     $('#exportUserNames').val(userNames);
	     
		 var groupNames=new Array();
	     $("#exportGroupIds option").each(function(index){  
	    	 groupNames[index]=$(this).text();
	          $(this).attr('selected','selected'); 
	     })  
	     $('#exportGroupNames').val(groupNames);
	     
	     var courseNames=new Array();
	     $("#exportCourseIds option").each(function(index){  
	    	 courseNames[index]=$(this).text();
	          $(this).attr('selected','selected'); 
	     })  
	     $('#exportCourseNames').val(courseNames);
	     
	     var catalogNames=new Array();
	     $("#exportCatalogIds option").each(function(index){  
	    	 catalogNames[index]=$(this).text();
	          $(this).attr('selected','selected'); 
	     })  
	     $('#exportCatalogNames').val(catalogNames);
	     
		 $('#formCondition').submit();
	}); 

}

</script>
<%@ include file="../common/userTree.jsp"%>
<%@ include file="../common/groupTree.jsp"%>
<%@ include file="../common/courseTree.jsp"%>
<%@ include file="../common/catalogTree.jsp"%>

<script type="text/javascript" src="${ctx}/static/admin/js/learnerReport/index.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_lm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_rp_${lang}.js"></script>
<script type="text/javascript">
function down_excel(){
	
	 if(!validateForm()){
		 return;
	 }

	var userNames=new Array();
	 $("#exportUserIds option").each(function(index){  
		  userNames[index]=$(this).text();
          $(this).attr('selected','selected'); 
     })  
     $('#exportUserNames').val(userNames);
     
	 var groupNames=new Array();
     $("#exportGroupIds option").each(function(index){  
    	 groupNames[index]=$(this).text();
          $(this).attr('selected','selected'); 
     })  
     $('#exportGroupNames').val(groupNames);
     
     var courseNames=new Array();
     $("#exportCourseIds option").each(function(index){  
    	 courseNames[index]=$(this).text();
          $(this).attr('selected','selected'); 
     })  
     $('#exportCourseNames').val(courseNames);
     
     var catalogNames=new Array();
     $("#exportCatalogIds option").each(function(index){  
    	 catalogNames[index]=$(this).text();
          $(this).attr('selected','selected'); 
     })  
     $('#exportCatalogNames').val(catalogNames);
     initDate();
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
			        data:  $('#formCondition').serialize(),
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

function preSubmit(){
	initDate();
	return true;
}

function initDate(){
	 frm = document.formCondition
     $("#pageAppnStartDatetime").val('');
     $("#pageAppnEndDatetime").val('');
     $("#pageAttStartTime").val('');
     $("#pageAttEndTime").val('');
     $('#appnStartDatetime').val('');
     $('#appnEndDatetime').val('');
     $('#attStartTime').val('');
     $('#attEndTime').val('');
	 var page_appnStartDatetime = new Array();
	 var page_appnEndDatetime = new Array();
	 var page_attStartTime = new Array();
	 var page_attEndTime = new Array();
     
	     var appnStartDatetime_yy = $("#appn_start_datetime_yy").val();	// 报名日期
 	     var appnStartDatetime_mm = $("#appn_start_datetime_mm").val();
 	     var appnStartDatetime_dd = $("#appn_start_datetime_dd").val();
 	     var appnEndDatetime_yy = $("#appn_end_datetime_yy").val();
 	     var appnEndDatetime_mm = $("#appn_end_datetime_mm").val();
 	     var appnEndDatetime_dd = $("#appn_end_datetime_dd").val();
 	     if(appnStartDatetime_yy != "") {
 	    	page_appnStartDatetime.push(appnStartDatetime_yy);
 	    	page_appnStartDatetime.push(appnStartDatetime_mm);
 	    	page_appnStartDatetime.push(appnStartDatetime_dd);
 	    	$("#appnStartDatetime").val(appnStartDatetime_yy+'-'+appnStartDatetime_mm+'-'+appnStartDatetime_dd);
 	     }
 	    $("#pageAppnStartDatetime").val(page_appnStartDatetime);
 	    
    	if(appnEndDatetime_yy != "") {
    		page_appnEndDatetime.push(appnEndDatetime_yy);
    		page_appnEndDatetime.push(appnEndDatetime_mm);
    		page_appnEndDatetime.push(appnEndDatetime_dd);
    		$("#appnEndDatetime").val(appnEndDatetime_yy+'-'+appnEndDatetime_mm+'-'+appnEndDatetime_dd);
    	}
    	$("#pageAppnEndDatetime").val(page_appnEndDatetime);
    	
 	     var attStartTime_yy = $("#att_start_time_yy").val();	// 结训日期
 	     var attStartTime_mm = $("#att_start_time_mm").val();	
 	     var attStartTime_dd = $("#att_start_time_dd").val();
 	     var attEndTime_yy = $("#att_end_time_yy").val();
	     var attEndTime_mm = $("#att_end_time_mm").val();
	     var attEndTime_dd = $("#att_end_time_dd").val();
 	     if(attStartTime_yy != "") {
 	    	page_attStartTime.push(attStartTime_yy);
 	    	page_attStartTime.push(attStartTime_mm);
 	    	page_attStartTime.push(attStartTime_dd);
 	    	$("#attStartTime").val(attStartTime_yy+'-'+attStartTime_mm+'-'+attStartTime_dd);
 	     }
 	     $("#pageAttStartTime").val(page_attStartTime);
 	     
 	     if(attEndTime_yy != "") {
 	    	page_attEndTime.push(attEndTime_yy);
 	    	page_attEndTime.push(attEndTime_mm);
 	    	page_attEndTime.push(attEndTime_dd);
 	    	$("#attEndTime").val(attEndTime_yy+'-'+attEndTime_mm+'-'+attEndTime_dd);
 	     }
 	     $("#pageAttEndTime").val(page_attEndTime);
 	
}

/* function checked(){
	if($("input[name='exportUser']:checked").val()==${pageExportUser }){
		$("input[name='exportUser']").attr("checked","checked");
	}
	
} */




function save(){

         var page_exportUser = new Array();
         var page_exportUserIds = new Array();
         var page_exportUserIds_text = new Array();
         var page_exportCourse = new Array();
         var page_exportCourseIds = new Array();
         var page_exportCourseIds_text = new Array();
         var page_courseType = new Array();
         var page_appnStartDatetime = new Array();
         var page_appnEndDatetime = new Array();
         var page_attStartTime = new Array();
         var page_attEndTime = new Array();
         var page_appStatus = new Array();
         var page_courseStatus = new Array();
         var page_resultDataStatistic = new Array();
         var page_isExportDetail = new Array();
         var page_userInfo = new Array();
         var page_courseInfo = new Array();
         var page_otherInfo = new Array();
         
         var exportUser_val = $("input[name='exportUser']:checked").val();  // 学员
         page_exportUser.push(exportUser_val);  
         if(exportUser_val == '0') {
             page_exportUser.push($("input[name='includeDelUser']:checked").val());
         }
         if(exportUser_val == '1') {
             $("#exportUserIds option").each(function(){
                 page_exportUser.push(this.value);
                 page_exportUserIds.push(this.value);
                 page_exportUserIds_text.push($(this).text());
             });
         }
         if(exportUser_val == '2') {
             $("#exportGroupIds option").each(function(){
                 page_exportUser.push(this.value);
                 page_exportUserIds.push(this.value);
                 page_exportUserIds_text.push($(this).text());
             });
         }
         $("#pageExportUser").val(page_exportUser);
         $("#pageExportUserIdsText").val(page_exportUserIds_text);
         $("#pageExportUserIds").val(page_exportUserIds);
         
         var exportCourse_val = $("input[name='exportCourse']:checked").val();  // 课程/考试
         page_exportCourse.push(exportCourse_val);
         if(exportCourse_val == '1') {
             $("#exportCourseIds option").each(function(){
                 page_exportCourse.push(this.value);
                 page_exportCourseIds.push(this.value);
                 page_exportCourseIds_text.push($(this).text());
             });
         }
         if(exportCourse_val == '2') {
             $("#exportCatalogIds option").each(function(){
                 page_exportCourse.push(this.value);
                 page_exportCourseIds.push(this.value);
                 page_exportCourseIds_text.push($(this).text());
             });
         }
         $("#pageExportCourse").val(page_exportCourse);
         $("#pageExportCourseIds").val(page_exportCourseIds);
         $("#pageExportCourseIdsText").val(page_exportCourseIds_text);
         
         $("input[name='courseType']:checked").each(function(){ // 课程/考试类型
            page_courseType.push(this.value);
         });
        $("#pageCourseType").val(page_courseType);
         
         var appnStartDatetime_yy = $("#appn_start_datetime_yy").val(); // 报名日期
         var appnStartDatetime_mm = $("#appn_start_datetime_mm").val();
         var appnStartDatetime_dd = $("#appn_start_datetime_dd").val();
         var appnEndDatetime_yy = $("#appn_end_datetime_yy").val();
         var appnEndDatetime_mm = $("#appn_end_datetime_mm").val();
         var appnEndDatetime_dd = $("#appn_end_datetime_dd").val();
         if(appnStartDatetime_yy != "") {
            page_appnStartDatetime.push(appnStartDatetime_yy);
            page_appnStartDatetime.push(appnStartDatetime_mm);
            page_appnStartDatetime.push(appnStartDatetime_dd);
         }
        $("#pageAppnStartDatetime").val(page_appnStartDatetime);
    if(appnEndDatetime_yy != "") {
        page_appnEndDatetime.push(appnEndDatetime_yy);
        page_appnEndDatetime.push(appnEndDatetime_mm);
        page_appnEndDatetime.push(appnEndDatetime_dd);
    }
    $("#pageAppnEndDatetime").val(page_appnEndDatetime);
         
         var attStartTime_yy = $("#att_start_time_yy").val();   // 结训日期
         var attStartTime_mm = $("#att_start_time_mm").val();   
         var attStartTime_dd = $("#att_start_time_dd").val();
         var attEndTime_yy = $("#att_end_time_yy").val();
         var attEndTime_mm = $("#att_end_time_mm").val();
         var attEndTime_dd = $("#att_end_time_dd").val();
         if(attStartTime_yy != "") {
            page_attStartTime.push(attStartTime_yy);
            page_attStartTime.push(attStartTime_mm);
            page_attStartTime.push(attStartTime_dd);
         }
         $("#pageAttStartTime").val(page_attStartTime);
         if(attEndTime_yy != "") {
            page_attEndTime.push(attEndTime_yy);
            page_attEndTime.push(attEndTime_mm);
            page_attEndTime.push(attEndTime_dd);
         }
         $("#pageAttEndTime").val(page_attEndTime);
         
         $("input[name='appStatus']:checked").each(function(){  // 报名状态
            page_appStatus.push(this.value);
         });
         $("#pageAppStatus").val(page_appStatus);
         
         $("input[name='courseStatus']:checked").each(function(){   // 学习状态
            page_courseStatus.push(this.value);
         });
         $("#pageCourseStatus").val(page_courseStatus);
         
         var resultDataStatistic_val = $("input[name=resultDataStatistic]:checked").val();  // 结果数据统计方式
         var includeNoDataCourse_val = $("input[name=includeNoDataCourse]:checked").val();
         var includeNoDataUser_val = $("input[name=includeNoDataUser]:checked").val();
         page_resultDataStatistic.push(resultDataStatistic_val);
         if(resultDataStatistic_val == '0') {
             page_resultDataStatistic.push(includeNoDataCourse_val);
         } else {
             page_resultDataStatistic.push(includeNoDataUser_val);
         }
         $("#pageResultDataStatistic").val(page_resultDataStatistic);
         
         var isExportDetail_val = $("input[name='isExportDetail']:checked").val();  // 同时导出明细记录
         $("#pageIsExportDetail").val(isExportDetail_val);
     $("input[name='userInfo']:checked").each(function(){   // 用户信息
        page_userInfo.push(this.value);
     });
     $("#pageUserInfo").val(page_userInfo);
     $("input[name='courseInfo']:checked").each(function(){ // 课程信息
         page_courseInfo.push(this.value);
     })
     $("#pageCourseInfo").val(page_courseInfo);
     $("input[name='otherInfo']:checked").each(function(){  // 其他
         page_otherInfo.push(this.value);
     })
     $("#pageOtherInfo").val(page_otherInfo);
         
         var checkVal = $('input[name="resultDataStatistic"]:checked').val();
         /* if(checkVal==0){
              $('#formCondition').attr('action','${ctx}' + '/app/admin/learnerReport/getLearnerReportByCourse');
         }else{
             $('#formCondition').attr('action','${ctx}' + '/app/admin/learnerReport/getLearnerReportByUser');
         } */
         var userNames=new Array();
         $("#exportUserIds option").each(function(index){  
              userNames[index]=$(this).text();
              $(this).attr('selected','selected'); 
         })  
         $('#exportUserNames').val(userNames);
         
         var groupNames=new Array();
         $("#exportGroupIds option").each(function(index){  
             groupNames[index]=$(this).text();
              $(this).attr('selected','selected'); 
         })  
         $('#exportGroupNames').val(groupNames);
         
         var courseNames=new Array();
         $("#exportCourseIds option").each(function(index){  
             courseNames[index]=$(this).text();
              $(this).attr('selected','selected'); 
         })  
         $('#exportCourseNames').val(courseNames);
         
         var catalogNames=new Array();
         $("#exportCatalogIds option").each(function(index){  
             catalogNames[index]=$(this).text();
              $(this).attr('selected','selected'); 
         })  
         $('#exportCatalogNames').val(catalogNames);
         

}

</script>

</head>
<body>
      <form id="formCondition" name="formCondition" action="${ctx}/app/admin/learnerReport/getLearnerReportByUser" onsubmit="preSubmit()">
        <input type="hidden" name="belong_module" value=FTN_AMD_TRAINING_REPORT_MGT/>
		<input type="hidden" id="exportUserNames" name="exportUserNames" value=""/>
		<input type="hidden" id="exportGroupNames" name="exportGroupNames" value=""/>
		<input type="hidden" id="exportCourseNames" name="exportCourseNames" value=""/>
		<input type="hidden" id="exportCatalogNames" name="exportCatalogNames" value=""/>
		<input type="hidden" name="language" value="${language}"/>
		<input type="hidden" name="pageExportUser" id="pageExportUser" value="${pageExportUser }"/>
             <input type="hidden" name="pageExportUserIds" id="pageExportUserIds" value="${pageExportUserIds}"/>
			 <input type="hidden" name="pageExportUserIdsText" id="pageExportUserIdsText" value="${pageExportUserIdsText }"/>
			 <input type="hidden" name="pageExportCourse" id="pageExportCourse" value="${pageExportCourse }"/>
             <input type="hidden" name="pageExportCourseIds" id="pageExportCourseIds" value="${pageExportCourseIds}"/>
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
		<input type="hidden" name="rte_id" id="rte_id" value=""/>
        <input type="hidden" name="rsp_id" id="rsp_id" value=""/>
		<input type="hidden" name="usr_ent_id" id="usr_ent_id" value=""/>
		<input type="hidden" name="rpt_type" id="rpt_type" value=""/>
		<input type="hidden" name="cmd" id="cmd" value=""/>
		<input type="hidden" name="module" id="module" value=""/>
		<input type="hidden" name="download" id="download" value=""/>
		<input type="hidden" name="rpt_type_lst" id="rpt_type_lst" value=""/>
		<input type="hidden" name="stylesheet" id="stylesheet" value=""/>
		<input type="hidden" name="url_success" id="url_success" value=""/>
		<input type="hidden" name="url_failure" id="url_failure" value=""/>
		<input type="hidden" name="spec_name" id="spec_name" value=""/>
		<input type="hidden" name="spec_value" id="spec_value" value=""/>
		<input type="hidden" name="window_name" id="window_name" value=""/>
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
            <div class="panel-body">
                 <table>
                 <tr>
                 <td><span  style="color:#999;"><lb:get key="label_rp.label_core_report_168"/></span></td>
                 </tr>
                 <tr  style="height: 10px;"><td></td></tr>
                </table>
                <table>
                    <tbody>
                    	<tr>  
                            <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_166"/>：</td>
                            <td class="wzb-form-control">
                                <span class="wzb-live-content">
                                    <input type="text" id="rsp_title" size="50" maxlength="200"  class="wzb-inputText" value="${reportSpec.rsp_title}" name="rsp_title" />
                                    &nbsp;
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_45" />：</td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="exportUser" value="0" type="radio" onclick="checkedOrDisabledById('exportUser',0,'','includeDelUser')"><lb:get key="label_rp.label_core_report_46"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox padding-left20">
                                                    <input name="includeDelUser" value="true" type="checkbox"><lb:get key="label_rp.label_core_report_47" />
                                                </label>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="exportUser" value="1" type="radio" onclick="checkedOrDisabledById('exportUser',1,'includeDelUser');"><lb:get key="label_rp.label_core_report_48"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <span class="margin-left20">
                                                    <select name="exportUserIds" id="exportUserIds" value="" class="wzb-select" 
                                                    style="width:400px;height:120px;overflow-x:auto;" width="400" multiple="multiple">
                                                      
                                                    </select> 
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="wzb-form-control" style="padding-left:20px;">
                                                <input id="user_choose_btn" type="button" name="" value="<lb:get key="button_select"/>"
                                                		class="btn wzb-btn-blue margin-right4 xueyuan">
                                                <input type="button" name=" " value="<lb:get key="button_delete"/>" class="btn wzb-btn-blue margin-right4" onclick="removeSelectedOptionsById('exportUserIds');">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="exportUser" value="2" type="radio" onclick="checkedOrDisabledById('exportUser',2,'includeDelUser');"><lb:get key="label_rp.label_core_report_49"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <span class="margin-left20">
                                                    <select name="exportGroupIds" id="exportGroupIds" value="" class="wzb-select" style="width:400px;height:120px;overflow-x:auto;" width="400" multiple="">
                                                    </select>
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="wzb-form-control" style="padding-left:20px;">
                                                <input id="group_choose_btn" type="button" name=" " value="<lb:get key="button_select"/>" class="btn wzb-btn-blue margin-right4 xueyuan">
                                                <input type="button" name=" " value="<lb:get key="button_delete"/>" class="btn wzb-btn-blue margin-right4" onclick="removeSelectedOptionsById('exportGroupIds');">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_107"/>：</td>
                            <td class="wzb-form-control">
                                <label class="wzb-form-checkbox">
                                    <input name="exportCourse" value="0" type="radio"><lb:get key="label_rp.label_core_report_50"/>
                                </label>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="exportCourse" value="1" type="radio" ><lb:get key="label_rp.label_core_report_51"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <span class="margin-left20">
                                                    <select name="exportCourseIds" id="exportCourseIds" class="wzb-select" style="width:400px;height:120px;overflow-x:auto;" width="400" multiple="">
                                                        
                                                    </select>
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="wzb-form-control" style="padding-left:20px;">
                                                <input id="course_choose_btn" type="button" name=" " value="<lb:get key="button_select"/>" class="btn wzb-btn-blue margin-right4 kecheng">
                                                <input type="button" name=" " value="<lb:get key="button_delete"/>" class="btn wzb-btn-blue margin-right4"  onclick="removeSelectedOptionsById('exportCourseIds');">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="exportCourse" value="2" type="radio"><lb:get key="label_rp.label_core_report_52"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign="top">
                                                <span class="margin-left20">
                                                    <select name="exportCatalogIds" id="exportCatalogIds" class="wzb-select" style="width:400px;height:120px;overflow-x:auto;" width="400" multiple="">
                                                    </select>
                                                </span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="wzb-form-control" style="padding-left:20px;">
                                                <input type="button" name="catalog_choose_btn" id="catalog_choose_btn" value="<lb:get key="button_select"/>" class="btn wzb-btn-blue margin-right4 kecheng">
                                                <input type="button" name=" " value="<lb:get key="button_delete"/>" class="btn wzb-btn-blue margin-right4"  onclick="removeSelectedOptionsById('exportCatalogIds');">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>

                        <tr>  
                            <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_103"/>：</td>
                            <td class="wzb-form-control">
                                <label class="wzb-form-checkbox">
                                    <input name="courseType" value="0" type="checkbox"><lb:get key="label_rp.label_core_report_53"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="courseType" value="1" type="checkbox"><lb:get key="label_rp.label_core_report_54"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="courseType" value="2" type="checkbox"><lb:get key="label_rp.label_core_report_55"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="courseType" value="3" type="checkbox"><lb:get key="label_rp.label_core_report_56"/>
                                </label>
							<!-- 
                                <label class="wzb-form-checkbox">
                                    <input name="courseType" value="4" type="checkbox"><lb:get key="label_rp.label_core_report_57"/>
                                </label>
                                 -->
                            </td>
                        </tr>

                        <tr>  
                            <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_58"/>：</td>
                            <td class="wzb-form-control">
                                <lb:get key="label_rp.label_core_report_108"/>
                                <span class="wzb-live-content">  <!--placeholder="请输入报名日期开始时间"  placeholder="请输入报名日期结束时间"-->
                                    <!-- <input type="text" id="appnStartDatetime" class="form-control Wdate margin-right10" style="width:220px;"  value="" name="appnStartDatetime" />
                                    -->
                                    <input type="hidden" id="appnStartDatetime" value="" name="appnStartDatetime" />
                                    <input type="text" id="appn_start_datetime_yy" size="4" maxlength="4" class="wzb-inputText" value="" name="appn_start_datetime_yy" />
                                    -
                                    <input type="text" id="appn_start_datetime_mm" size="2" maxlength="2" class="wzb-inputText" value="" name="appn_start_datetime_mm" />
                                    -
                                    <input type="text" id="appn_start_datetime_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="appn_start_datetime_dd" />
                                    <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                                    	href="javascript:show_calendar('appn_start_datetime', '','','','${label_lan}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                     <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                    </a>
                                    &nbsp;
                                </span>
                                <lb:get key="label_rp.label_core_report_59"/>
                                <span class="wzb-live-content">
                                    <!-- <input type="text" id="appnEndDatetime" class="form-control Wdate margin-left10" style="width:220px;"  value="" name="appnEndDatetime" />
                                    -->
                                    <input type="hidden" id="appnEndDatetime" value="" name="appnEndDatetime" />
                                    <input type="text" id="appn_end_datetime_yy" size="4" maxlength="4" class="wzb-inputText" value="" name="appn_end_datetime_yy" />
                                    -
                                    <input type="text" id="appn_end_datetime_mm" size="2" maxlength="2" class="wzb-inputText" value="" name="appn_end_datetime_mm" />
                                    -
                                    <input type="text" id="appn_end_datetime_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="appn_end_datetime_dd" />
                                    <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                                    	href="javascript:show_calendar('appn_end_datetime', '','','','${label_lan}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                     <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                    </a>
                                    &nbsp;
                                </span>
                            </td>
                        </tr>

                        <tr>  
                            <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_60"/>：</td>
                            <td class="wzb-form-control">
                                <lb:get key="label_rp.label_core_report_108"/>
                                <span class="wzb-live-content">
                                   <!--  <input type="text" id="attStartTime" class="form-control Wdate margin-right10" style="width:220px;"  value="" name="attStartTime">
                                   -->
                                   <input type="hidden" id=attStartTime value="" name="attStartTime" />
                                   <input type="text" id="att_start_time_yy" size="4" maxlength="4" class="wzb-inputText" value="" name="att_start_time_yy" />
                                    -
                                    <input type="text" id="att_start_time_mm" size="2" maxlength="2" class="wzb-inputText" value="" name="att_start_time_mm" />
                                    -
                                    <input type="text" id="att_start_time_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="att_start_time_dd" />
                                    <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                                    	href="javascript:show_calendar('att_start_time', '','','','${label_lan}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                     <img src="../../../wb_image/btn_calendar.gif" border="0" />
                                    </a>
                                    &nbsp;
                                </span>
                                <lb:get key="label_rp.label_core_report_59"/>
                                <span class="wzb-live-content">
                                    <!-- <input type="text" id="attEndTime" class="form-control Wdate margin-left10" style="width:220px;" value="" name="attEndTime" />
                                    -->
                                    <input type="hidden" id="attEndTime" value="" name="attEndTime" />
                                    <input type="text" id="att_end_time_yy" size="4" maxlength="4" class="wzb-inputText" value="" name="att_end_time_yy" />
                                    -
                                    <input type="text" id="att_end_time_mm" size="2" maxlength="2" class="wzb-inputText" value="" name="att_end_time_mm" />
                                    -
                                    <input type="text" id="att_end_time_dd" size="2" maxlength="2" class="wzb-inputText" value="" name="att_end_time_dd" />
                                    <a onmouseout="window.status='';return true;" onmouseover="window.status='Date Picker';return true;"
                                    	href="javascript:show_calendar('att_end_time', '','','','${label_lan}','${ctx}../../../cw/skin4/images/gb/css/wb_ui.css');">
                                     <img src="${ctx}../../../wb_image/btn_calendar.gif" border="0" />
                                    </a>
                                    &nbsp;
                                </span>
                                <span class="ask-in margin4 allhint-style-1"></span>
                                <script type="text/javascript">
                                     $(".allhint-style-1").mouseenter(function(){
				                          layer.tips(fetchLabel('label_core_report_151'), '.allhint-style-1', {
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

                        <tr>
                            <td></td>
                            <td class="color-gray999 padding-left10"></td>
                        </tr>

                        <tr>  
                            <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_61"/>：</td>
                            <td class="wzb-form-control">
                                <label class="wzb-form-checkbox" >
                                    <input name="appStatus" value="Pending" type="checkbox"><lb:get key="label_rp.label_core_report_62"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="appStatus" value="Waiting" type="checkbox"><lb:get key="label_rp.label_core_report_63"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="appStatus" value="Rejected" type="checkbox"><lb:get key="label_rp.label_core_report_64"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="courseStatus" value="I" type="checkbox"><lb:get key="label_rp.label_core_report_65"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="courseStatus" value="C" type="checkbox"><lb:get key="label_rp.label_core_report_66"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="courseStatus" value="F" type="checkbox"><lb:get key="label_rp.label_core_report_67"/>
                                </label>

                                <label class="wzb-form-checkbox">
                                    <input name="courseStatus" value="W" type="checkbox"><lb:get key="label_rp.label_core_report_68"/>
                                </label>
                            </td>
                        </tr>

                        <tr>
                            <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_69"/>：</td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="resultDataStatistic" value="0" type="radio" onclick="checkedOrDisabledById('','','includeNoDataUser','includeNoDataCourse','creditInvalid');">
                                                    <lb:get key="label_rp.label_core_report_70"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox padding-left20">
                                                    <input name="includeNoDataCourse" value="1" type="checkbox"><lb:get key="label_rp.label_core_report_71"/>
                                                </label>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="resultDataStatistic" value="1" type="radio" onclick="checkedOrDisabledById('','','includeNoDataCourse','includeNoDataUser','creditValid');">
                                                    <lb:get key="label_rp.label_core_report_72"/>
                                                </label>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox padding-left20">
                                                    <input name="includeNoDataUser" value="1" type="checkbox" disabled="disabled"><lb:get key="label_rp.label_core_report_73"/>
                                                </label>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td class="wzb-form-label"><lb:get key="label_rp.label_core_report_74"/>：</td>
                            <td class="wzb-form-control">
                                <label class="wzb-form-checkbox">
                                    <input name="isExportDetail" value="true" type="radio" onclick="infoRemoveDisabled();"><lb:get key="label_rp.label_core_report_75"/>
                                </label>
                                <label class="wzb-form-checkbox">
                                    <input name="isExportDetail" value="false" type="radio" onclick="infoAddDisabled();"><lb:get key="label_rp.label_core_report_76"/>
                                </label>
                            </td>
                        </tr>

                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><lb:get key="label_rp.label_core_report_77"/>：</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="userInfo" value="userDispalyName" type="checkbox"><lb:get key="label_rp.label_core_report_78"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="userInfo" value="userFullName" type="checkbox"><lb:get key="label_rp.label_core_report_79"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="userInfo" value="gruopName" type="checkbox"><lb:get key="label_rp.label_core_report_80"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="userInfo" value="gradeName" type="checkbox"><lb:get key="label_rp.label_core_report_81"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="userInfo" value="userEmail" type="checkbox"><lb:get key="label_rp.label_core_report_82"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="userInfo" value="userTel" type="checkbox"><lb:get key="label_rp.label_core_report_83"/>
                                                </label>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><lb:get key="label_rp.label_core_report_104"/>：</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="courseInfo" value="itmCode" type="checkbox"><lb:get key="label_rp.label_core_report_84"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="courseInfo" value="itmTitle" type="checkbox"><lb:get key="label_rp.label_core_report_85"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="courseInfo" value=itmType type="checkbox"><lb:get key="label_rp.label_core_report_86"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="courseInfo" value="tcrTitle" type="checkbox"><lb:get key="label_rp.label_core_report_87"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="courseInfo" value="classCode" type="checkbox"><lb:get key="label_rp.label_core_report_88"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="courseInfo" value="classTitle" type="checkbox"><lb:get key="label_rp.label_core_report_89"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="courseInfo" value="courseCatalog" type="checkbox"><lb:get key="label_rp.label_core_report_160"/>
                                                </label>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td class="wzb-form-label"></td>
                            <td class="wzb-form-control">
                                <table>
                                    <tbody>
                                        <tr>
                                            <td><lb:get key="label_rp.label_core_report_105"/>：</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="appCreateTime" type="checkbox"><lb:get key="label_rp.label_core_report_90"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="appStatus" type="checkbox"><lb:get key="label_rp.label_core_report_91"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="attTime" type="checkbox"><lb:get key="label_rp.label_core_report_92"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="covStatus" type="checkbox"><lb:get key="label_rp.label_core_report_93"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="covCommenceDatetime" type="checkbox"><lb:get key="label_rp.label_core_report_94"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="covTotalTime" type="checkbox"><lb:get key="label_rp.label_core_report_95"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="covScore" type="checkbox"><lb:get key="label_rp.label_core_report_96"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="attRate" type="checkbox"><lb:get key="label_rp.label_core_report_97"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="attemptCount" type="checkbox"><lb:get key="label_rp.label_core_report_98"/>
                                                </label>
                                                <label class="wzb-form-checkbox">
                                                    <input name="otherInfo" value="courseCredit"  id="courseCredit"  type="checkbox"><lb:get key="label_rp.label_core_report_164"/>
                                                </label>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>

                        <tr>
                            <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_99"/>：</td>
                            <td class="wzb-form-control">
                                <lb:get key="label_rp.label_core_report_100"/></br>
                                <lb:get key="label_rp.label_core_report_106"/></br>
                                <lb:get key="label_rp.label_core_report_102"/></br>
                                <span  style="color:#999;"><lb:get key="label_rp.label_core_report_156"/></span>
                            </td>
                        </tr>
                        
                       <%--  <tr>
                            <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_156"/>：</td>
                            <td class="wzb-form-control" style="color:#999;">
                                <lb:get key="label_rp.label_core_report_102"/>
                            </td>
                        </tr>
                        
                        <tr>
                            <td class="wzb-form-label" valign="top"><lb:get key="label_rp.label_core_report_157"/>：</td>
                            <td class="wzb-form-control" style="color:#999;">
                                <lb:get key="label_rp.label_core_report_106"/>
                            </td>
                        </tr> --%>
                    </tbody>
                </table>

              <%--   <div>
                    <p><lb:get key="label_rp.label_core_report_101"/>：</p>
                    <div class="color-gray999" style="margin-left:60px;">
                        <lb:get key="label_rp.label_core_report_102"/>  
                    </div>
                </div> --%>

                <div class="wzb-bar">
                    <input id="view_report_btn" style="" name="" value="<lb:get key="button_see"/>" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
                    <input style="" name="" value="<lb:get key="lab_kb_submit"/>"  onclick="save();javascript:mgt_rpt.upd_rpt_exec_jsp(document.formCondition,'${data.rte_id }',  '${reportSpec.rsp_id }', '${prof.usr_ent_id}','LEARNER','${label_lan}');" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">                    
                    <input style="" name=" " value="<lb:get key="button_cancel"/>" onclick="javascript:wb_utils_nav_go('FTN_AMD_TRAINING_REPORT_MGT',${prof.usr_ent_id},'${label_lan}');" type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big">
                </div>

            </div>  <!-- panel-body End-->
        </div>  <!-- panel End-->       
   </form>
</body>
</html>
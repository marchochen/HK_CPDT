$(function(){
	var uSelector = new userSelector();
	uSelector.selectedCallBack = function(data){
		changeExportUser($("input[name='exportUser']:checked").val());
		for(var i=0;i<data.length;i++){
		  if(checkId('exportUserIds',data[i].id,data[i].name)){
			  $("<option value='"+data[i].id+"'>"+data[i].name+"</option>").appendTo("#exportUserIds");
		  }
	    }
	};
	var gSelector = new groupSelector();
	gSelector.selectedCallBack = function(data){
		changeExportUser($("input[name='exportUser']:checked").val());
		for(var i=0;i<data.length;i++){
		  if(checkId('exportGroupIds',data[i].id,data[i].name)){
		    $("<option value='"+data[i].id+"'>"+data[i].name+"</option>").appendTo("#exportGroupIds");
		  }
	    }
	}
	
	var cSelector = new courseSelector();
	cSelector.selectedCallBack = function(data){
		changeExportCourse($("input[name='exportCourse']:checked").val());
		for(var i=0;i<data.length;i++){
		  if(checkId('exportCourseIds',data[i].id,data[i].name)){
		     $("<option value='"+data[i].id+"'>"+data[i].name+"</option>").appendTo("#exportCourseIds");
		  }
	    }
	}
	
	var caSelector = new catalogSelector();
	caSelector.selectedCallBack = function(data){
		changeExportCourse($("input[name='exportCourse']:checked").val());
		for(var i=0;i<data.length;i++){
		  if(checkId('exportCatalogIds',data[i].id,data[i].name)){
		    $("<option value='"+data[i].id+"'>"+data[i].name+"</option>").appendTo("#exportCatalogIds");
		  }
	    }
	}
	
	$('#user_choose_btn').click(function(){
		uSelector.show();
		checkedOrDisabledById('exportUser',1,'includeDelUser');
        $("#exportGroupIds").find("option").remove();
	});
	
	$('#course_choose_btn').click(function(){
		cSelector.show();
		$('input:radio[name=exportCourse]').eq(1).attr("checked",'true'); 
	});
	
	$('#group_choose_btn').click(function(){
		gSelector.show();
		checkedOrDisabledById('exportUser',2,'includeDelUser');
        $("#exportUserIds").find("option").remove();
	});
	
	$('#catalog_choose_btn').click(function(){
		caSelector.show();
		$('input:radio[name=exportCourse]').eq(2).attr("checked",'true'); 
	});
	
	$("input:radio[name='exportUser']").change(function (){ 
		changeExportUser($(this).val());
	});
	
	$("input:radio[name='exportCourse']").change(function (){ 
		changeExportCourse($(this).val());
	});
	
	//默认 同时导出明细记录 选中否
	infoAddDisabled();
});

function changeExportUser(val){
	if(val==1){
		$("#exportGroupIds").find("option").remove();
	}else if(val==2){
		$("#exportUserIds").find("option").remove();
	}else{
		$("#exportGroupIds").find("option").remove();
		$("#exportUserIds").find("option").remove();
	}
}

function changeExportCourse(val){
	if(val==1){
		$("#exportCatalogIds").find("option").remove();
	}else if(val==2){
		$("#exportCourseIds").find("option").remove();
	}else{
		$("#exportCatalogIds").find("option").remove();
		$("#exportCourseIds").find("option").remove();
	}
}

//检测是否存在相同选项内容    
function checkId(selectId,value,text){
	
	if(value != undefined && value != '' && text != undefined && text != ''){
		var stext = $("#"+selectId+" option[value='" + value + "']").text();
		if(text != stext){
			return true;
		}
	}	
	
	return false;
}

//移除当前选中项
function removeSelectedOptionsById(id){
   	var options = document.getElementById(id).options;
   	for(var i = (options.length - 1); i >= 0; i--){
   		var o = options[i];
   		if(o.selected){
   			options[i] = null;
   		}
   	}
   	options.selectedIndex = -1;
   }

//添加checked  num    添加disabled   移除disabled
function checkedOrDisabledById(radioName,num,aCheckName,rCheckName,courseCredit){	
	 if(radioName != undefined && radioName != '' && num != undefined && num != ''){
		 $('input:radio[name='+radioName+']').eq(num).attr("checked",'true'); 
	 }
	 if(aCheckName != undefined && aCheckName != ''){
		 $("[name="+aCheckName+"]:checkbox").attr("disabled", true);
	 }
	 if(rCheckName != undefined && rCheckName != ''){
		 $("[name="+rCheckName+"]:checkbox").removeAttr("disabled");
	 }
	 var isExportDetail_val = $("input[name='isExportDetail']:checked").val();  
	 if( courseCredit=='creditInvalid' &&  isExportDetail_val=='true'){
        //$("#courseCredit").attr("disabled", true);
        $("#courseCredit").removeAttr("disabled");
	 }else if(courseCredit=='creditValid'  && isExportDetail_val=='true'){
        $("#courseCredit").removeAttr("disabled");
	 }
}


function  infoAddDisabled(){
	$("[name='userInfo']:checkbox").attr("disabled", true);
	$("[name='courseInfo']:checkbox").attr("disabled", true);
	$("[name='otherInfo']:checkbox").attr("disabled", true);
}

function infoRemoveDisabled(){
	$("[name='userInfo']:checkbox").removeAttr("disabled");
	$("[name='courseInfo']:checkbox").removeAttr("disabled");
	$("[name='otherInfo']:checkbox").removeAttr("disabled");
	
     /*var resultDataStatistic = $("input[name='resultDataStatistic']:checked").val();  
     if(resultDataStatistic==0){
          $("#courseCredit").attr("disabled", true);
     }*/
     
}

function validateForm(){

	var exportUser = $("input[name='exportUser']:checked").val();
	if(exportUser==1){
		if($("#exportUserIds option").size()<=0){
			layer.alert(fetchLabel("label_core_report_141"),{title:fetchLabel("label_core_report_145")});
			return false;
		}
	}else if(exportUser==2){
		if($("#exportGroupIds option").size()<=0){
			layer.alert(fetchLabel("label_core_report_142"),{title:fetchLabel("label_core_report_145")});
			return false;
		}
	}
	
	var expoetCourse = $("input[name='exportCourse']:checked").val();
	if(expoetCourse==1){
		if($("#exportCourseIds option").size()<=0){
			layer.alert(fetchLabel("label_core_report_143"),{title:fetchLabel("label_core_report_145")});
			return false;
		}
	}else if(expoetCourse==2){
		if($("#exportCatalogIds option").size()<=0){
			layer.alert(fetchLabel("label_core_report_144"),{title:fetchLabel("label_core_report_145")});
			return false;
		}
	}
	if($("input[name='isExportDetail']:checked").val()=='true'){ 
		var pass = false;
		$("input[name='userInfo']").each(function(){
			if($(this).is(':checked')){
			   pass = true;
		   }
		});
		$("input[name='courseInfo']").each(function(){
			if($(this).is(':checked')){
			   pass = true;
		   }
		});
		$("input[name='otherInfo']").each(function(){
			if($(this).is(':checked')){
			   pass = true;
		   }
		});
		if(!pass){
			layer.alert(fetchLabel("label_core_report_146"),{title:fetchLabel("label_core_report_145")});
			return pass;
		}
	}
	
	if($("input[name='courseType']:checked").length<=0){
		layer.alert(fetchLabel("label_core_report_149"),{title:fetchLabel("label_core_report_145")});
		return false;
	}

	var statusPass = true;
	if($("input[name='appStatus']:checked").length<=0){
		statusPass = false;
	}
	
	if($("input[name='courseStatus']:checked").length<=0){
		statusPass = statusPass||false;
	}else{
		statusPass = statusPass||true;
	}
	if(!statusPass){
		layer.alert(fetchLabel("label_core_report_150"),{title:fetchLabel("label_core_report_145")});
		return false;
	}
	
	//报名开始日期
	if(getDatetime('appn_start_datetime','appnStartDatetime','label_core_report_58')){
		return false;
	}
	//报名结束日期
	if(getDatetime('appn_end_datetime','appnEndDatetime','label_core_report_58')){
		return false;
	}
	//结训开始日期
	if(getDatetime('att_start_time','attStartTime','label_core_report_60')){
		return false;
	}
	//结训结束日期
	if(getDatetime('att_end_time','attEndTime','label_core_report_60')){
		return false;
	}
	
	//报名开始日期与结束日期比较
	if($('#appnStartDatetime').val() != '' && $('#appnEndDatetime').val() != ''){
		 var appnStartDatetime = new Date($('#appnStartDatetime').val().replace(/\-/g, "\/"));  
		 var appnEndDatetime = new Date($('#appnEndDatetime').val().replace(/\-/g, "\/"));  
		 
		 if(appnStartDatetime > appnEndDatetime) {  
		  Dialog.alert(fetchLabel("label_core_report_153"));  
		  return false;  
		 }
	}
	//结训开始日期与结束日期比较
	if($('#attStartTime').val() != '' && $('#attEndTime').val() != ''){
		 var attStartTime = new Date($('#attStartTime').val().replace(/\-/g, "\/"));  
		 var attEndTime = new Date($('#attEndTime').val().replace(/\-/g, "\/"));  
		 
		 if(attStartTime > attEndTime) {  
		  Dialog.alert(fetchLabel("label_core_report_153"));  
		  return false;  
		 }
	}
	
	return true;
}


function getDatetime(spec,valueName,label){
	var DATE_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/;
	var date;
	var appn_end_datetime_yy = $("#"+spec+"_yy").val();
	var appn_end_datetime_mm = $("#"+spec+"_mm").val();
	var appn_end_datetime_dd = $("#"+spec+"_dd").val();
	if(appn_end_datetime_yy != '' || appn_end_datetime_mm != '' || appn_end_datetime_dd != ''){
		date = appn_end_datetime_yy +'-'+ appn_end_datetime_mm +'-'+ appn_end_datetime_dd
	}else if(appn_end_datetime_yy == '' && appn_end_datetime_mm == '' && appn_end_datetime_dd == ''){
		date = "";
	}
		
	if(DATE_FORMAT.test(date) && date != undefined){
		$("#"+valueName).attr("value",date);
	}else if(date != ""){
		Dialog.alert('"'+fetchLabel(label) +'"'+ fetchLabel("label_core_report_152"));
		return true;
	}
	
	return false;
}




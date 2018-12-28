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
	var gSelector = groupSelector();
	gSelector.selectedCallBack = function(data){
		changeExportUser($("input[name='exportUser']:checked").val());
		for(var i=0;i<data.length;i++){
		  if(checkId('exportGroupIds',data[i].id,data[i].name)){
		    $("<option value='"+data[i].id+"'>"+data[i].name+"</option>").appendTo("#exportGroupIds");
		  }
	    }
	}
	
	
	$('#user_choose_btn').click(function(){
		uSelector.show();
		checkedOrDisabledById('exportUser',1);
	});
	
	
	$('#group_choose_btn').click(function(){
		gSelector.show();
		checkedOrDisabledById('exportUser',2);
	});
	
	
	$("input:radio[name='exportUser']").change(function (){ 
		changeExportUser($(this).val());
	});
	
    var cpdTypes = $("input[name='cghi_ct_id']").get(0);
    if ( typeof(cpdTypes)!="undefined" ){ 
    	$("input[name='cghi_ct_id']").get(0).checked=true; 
    }
    
    reloadTable();
	
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

//添加checked  num
function checkedOrDisabledById(radioName,num){	
	 if(radioName != undefined && radioName != '' && num != undefined && num != ''){
		 $('input:radio[name='+radioName+']').eq(num).attr("checked",'true'); 
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
}






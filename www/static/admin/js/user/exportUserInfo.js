
$(function(){
	
});

function showUser(){
    var uSelector = new userSelector();
	   uSelector.selectedCallBack = function(data){
		//console.log(data)
	   for(var i=0;i<data.length;i++){
		  if(checkId('uid',data[i].id,data[i].name)){
		   $("<option value='"+data[i].id+"'>"+data[i].name+"</option>").appendTo("#uid");
		  }
	   }
	 };
	uSelector.show();
   }    

function showUserGroup(){
      var gSelector = new groupSelector();
	   gSelector.selectedCallBack = function(data){
		//console.log(data)
		for(var i=0;i<data.length;i++){
		  if(checkId('gid',data[i].id,data[i].name)){
		     $("<option value='"+data[i].id+"'>"+data[i].name+"</option>").appendTo("#gid");
		  }
		}
	  };
	 gSelector.show();
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


function removeSelectedOptions(id){
   	var options = document.getElementById(id).options;
   	for(var i = (options.length - 1); i >= 0; i--){
   		var o = options[i];
   		if(o.selected){
   			options[i] = null;
   		}
   	}
   	options.selectedIndex = -1;
   }

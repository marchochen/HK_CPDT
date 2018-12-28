var encrytor = wbEncrytor();
var dt;
$(function(){
	dt = $("#itemList").table({
        url : contextPath + '/app/admin/specialTopic/pageJson',
		colModel : dtModel,
        userView : true,
        rp : 10,
		rowSize : 1,    
        hideHeader : false,
		sortname : 'ust_update_time',  
		sortorder : 'desc',
        usepager : true,
        params : {}
    });
	 document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {

		    	  var p = {
		                  searchText : $("input[name='searchText']").val()
		              }
		          itmParams = $.extend(itmParams, p);
		          $(dt).reloadTable({
		              params : itmParams
		          });
		     }
		}
	$("#searchBtn").unbind("click");
	$("#searchBtn").bind("click",function(){
		var p = {
                searchText : $("input[name='searchText']").val()
            };
        $(dt).reloadTable({
            params : p
        });
	});
	
	$("#addSte").unbind("click");
	$("#addSte").bind("click",function(){
		window.location.href = contextPath + '/app/admin/specialTopic/create';
	});
	
	deleteSte = function(id){
		Dialog.confirm({
			text : cwn.getLabel('label_core_learning_map_32'),
			callback : function(answer) {
				if (answer) {
					window.location.href = contextPath+"/app/admin/specialTopic/delete?encrypt_ust_id="+encrytor.cwnEncrypt(id);
				}
			}
			})
	}

	updateSteBtn = function(field,id){
		
		window.location.href = contextPath + '/app/admin/specialTopic/updatePage?encrypt_ust_id='+encrytor.cwnEncrypt(id);
	}

	viewResultBtn = function(id){
        
	  	  layer.open({
	              type: 2,
	              skin:'layui-layer-lan',
	              area: ['1200px', '600px'], //宽高
	              title : cwn.getLabel('label_core_learning_map_81'),
	              content:  contextPath + '/app/admin/specialTopic/viewResult?ust_id='+id
	          });   

	       
	}

	cancelPublished = function(id){
		window.location.href = contextPath + '/app/admin/specialTopic/cancelPublish?ust_id='+id;
	}
	published = function(id){
		window.location.href = contextPath + '/app/admin/specialTopic/publish?ust_id='+id;
	}
});


var dtModel = [
               {
              		display : '<input id="cms_all" onclick="getCheckedIds()" type="checkbox" value="" name="cms_all_checkbox" />',//"",
              		align : 'left',
              		width : '1%',
              		sortable : false,
              		format : function(data) {
              			p = {
              				text : data.ust_id
              			}
              			return $('#input-template').render(p);
              		}
              	},   
{
    display : cwn.getLabel('label_core_learning_map_22'),//"标题",
    align : 'left',
    width : '19%',
    sortable : true,
    name : 'ust_title',
		format : function(data) {
			var title;
			if(getChars(data.ust_title)>20){
				title="<p title='"+data.ust_title+"'>"+data.ust_title.substring(0,15)+"...</p>";
			}else{
				title=data.ust_title;
			}
			p = {
				text :title
			};
			return $('#text-template').render(p);
		}
	}
	, 
	{
		display : cwn.getLabel('label_core_learning_map_25'),//"更新时间",
		align : 'left',
		width : '15%',
		sortable : true,
		dateFormat: "yy-mm-dd",
		name : 'ust_update_time',
		format : function(data) {
			p = {
					text :Wzb.displayTime(data.ust_update_time, Wzb.time_format_ymd)
			};
			return $('#text-template').render(p);
		}
	}
	, 
	{
		display : cwn.getLabel('label_core_learning_map_28'),//"状态",
		align : 'left',
		width : '10%',
		sortable : true,
		name : 'ust_status',
		format : function(data) {
			var status='';
			if(data.ust_status==0){
				status=cwn.getLabel('global_publish_off');
			}else{
				status=cwn.getLabel('global_publish_on');
			}
			p = {
					text : status
				};
			return $('#text-template').render(p);
		}
	} 
	, 
	{
		display : cwn.getLabel('label_core_learning_map_29'),//"推送到首页专栏",
		align : 'left',
		width : '15%',
		sortable : true,
		name : 'ust_showindex',
		format : function(data) {
			if(data.ust_showindex==0){
				data.ust_showindex=cwn.getLabel('status_no');
			}else{
				data.ust_showindex=cwn.getLabel('status_yes');
			}
			
			p = {
					text : data.ust_showindex
				};
			return $('#text-template').render(p);
		}
	} 
	, 
	{
        display : "",
        align : 'right',
        width : '40%',
        format : function(data) {
        	p = {
        		id:data.ust_id,
        		status:data.ust_status,
        		responseCount:data.responseCount
        	};
        	return $('#operateBtnTemplate').render(p);
        }
    } 

];
function batchdelTopic() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.open({
			  title: cwn.getLabel("warning_notice"),
			  content: cwn.getLabel("label_core_learning_map_135")
			});     
		return
	}
	Dialog.confirm({
		text : cwn.getLabel("label_core_learning_map_32"),
		callback : function(answer) {
			if (answer) {
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/specialTopic/batDelete?ids=" + ids,
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
						$('#cms_all').prop("checked", false);
							$(dt).reloadTable();
					}
				})
			}
		}
	})
}
function batchPublishTopic() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.alert(cwn.getLabel("label_core_learning_map_135"));
		return
	}
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/specialTopic/batPublish?ids=" + ids,
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
						$('#cms_all').prop("checked", false);
							$(dt).reloadTable();
					}
	})
}
function batchCancelPublishTopic() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.alert(cwn.getLabel("label_core_learning_map_135"));
		return
	}
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/specialTopic/batCancelPublish?ids=" + ids,
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
						$('#cms_all').prop("checked", false);
							$(dt).reloadTable();
					}
				})
}
function getCheckedIds() {
	var isChecked = $('#cms_all').is(":checked");
	if (!isChecked) {
		$('input[name="cms_checkbox"]').each(function() {
			$(this).prop("checked", false);
		});
	} else {
		$('input[name="cms_checkbox"]').each(function() {
			$(this).prop("checked", true);
		});
	}
}
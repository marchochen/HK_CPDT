var dt;
$(function(){
	dt = $("#itemList").table({
        url : contextPath + '/app/admin/positionMap/pageJson',
		colModel : dtModel,
        userView : true,
        rp : 10,
		rowSize : 1,    
        hideHeader : false,
		sortname : 'upm_seq_no',  
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
		    	  $('#cms_all').prop("checked", false);
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
		$('#cms_all').prop("checked", false);
        $(dt).reloadTable({
            params : p
        });
	});

	$("#addPstmap").unbind("click");
	$("#addPstmap").bind("click",function(){
		window.location.href = contextPath + '/app/admin/positionMap/add';
	});
	
	deletePstmap = function(id){
		Dialog.confirm({
			text : cwn.getLabel('label_core_learning_map_7_1'),
			callback : function(answer) {
				if (answer) {
					window.location.href = contextPath+"/app/admin/positionMap/delete?upm_id="+id;
				}
			}
			})
	}

	updatePstmap = function(field,id){
		
		window.location.href = contextPath + '/app/admin/positionMap/updatePage?upm_id='+id;
	}

	viewResultBtn = function(id){
		window.location.href = contextPath + '/app/admin/positionMap/viewResult?upm_id='+id;
	}

	cancelPublished = function(id){
		window.location.href = contextPath + '/app/admin/positionMap/publishAndCancel?upm_status=0&upm_id='+id;
	}
	published = function(id){
		window.location.href = contextPath + '/app/admin/positionMap/publishAndCancel?upm_status=1&upm_id='+id;
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
           				text : data.upm_id
           			}
           			return $('#input-template').render(p);
           		}
           	},       
{
    display : cwn.getLabel('label_core_learning_map_22'),//"标题",
    align : 'left',
    width : '27%',
    sortable : true,
    name : 'upt_title',
		format : function(data) {
              var  desc;
			if (typeof(data.upt_desc) == "undefined") { 
				desc="";
			} else{
				desc=data.upt_desc
			} 
			p = {
				   text : "<a href="+contextPath+ "/app/admin/position/updatePage?upt_id="+data.upt_id+"  title="+desc+">"+data.upt_title+"</a>"
			};
			return $('#text-template').render(p);
		}
	}
	, 
	{
		display : cwn.getLabel('label_core_learning_map_21'),//"编号",
		align : 'left',
		width : '23%',
		sortable : true,
		name : 'upt_code',
		format : function(data) {
			p = {
				text : data.upt_code
			};
			return $('#text-template').render(p);
		}
	}
	, 
	{
		display : cwn.getLabel('label_core_learning_map_17'),//"关键指数",
		align : 'left',
		width : '10%',
		sortable : true,
		name : 'upm_seq_no',
		format : function(data) {
			p = {
					text : data.upm_seq_no
				};
			return $('#text-template').render(p);
		}
	} 
	, 
	{
		display : cwn.getLabel('label_core_learning_map_20'),//"分类",
		align : 'left',
		width : '10%',
		sortable : false,
		name : 'upc_title',
		format : function(data) {
			if(data.upc_title==null||data.upc_title==""){
				data.upc_title=cwn.getLabel('label_core_learning_map_84');
			}
			p = {
					text : data.upc_title
				};
			return $('#text-template').render(p);
		}
	} 
	, 
	/*{
        display : cwn.getLabel('label_core_requirements_management_11'),//"回应数目",
        align : 'center',
        width : '10%',
        sortable : true,
        name : 'responseCount',
        format : function(data) {
        	var count = 0;
        	if(data.responseCount){
        		count = data.responseCount;
        	}
            p = {
                text : count
            };
            return $('#text-template').render(p);
        }
    } 
	, */
	{
        display : "",
        align : 'right',
        width : '30%',
        format : function(data) {
        	p = {
        		id:data.upm_id,
        		status:data.upm_status,
        		responseCount:data.responseCount
        	};
        	return $('#operateBtnTemplate').render(p);
        }
    } 

];


function batchdelMap() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.open({
			  title: cwn.getLabel("warning_notice"),
			  content: cwn.getLabel("label_core_learning_map_137")
			});     
		return
	}
	Dialog.confirm({
		text : cwn.getLabel("label_core_learning_map_7_1"),
		callback : function(answer) {
			if (answer) {
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/positionMap/batDelete?ids=" + ids,
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
function batchPublishMap() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.alert(cwn.getLabel("label_core_learning_map_137"));
		return
	}
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/positionMap/batPublish?ids=" + ids,
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
						$('#cms_all').prop("checked", false);
							$(dt).reloadTable();
					}
	})
}
function batchCancelPublishMap() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.alert(cwn.getLabel("label_core_learning_map_137"));
		return
	}
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/positionMap/batCancelPublish?ids=" + ids,
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
var dt;
var encrytor = wbEncrytor();
$(function(){
	dt = $("#itemList").table({
        url : contextPath + '/app/admin/profession/pageJson',
		colModel : dtModel,
        userView : true,
        rp : 10,
		rowSize : 1,    
        hideHeader : false,
		sortname : 'pfs_update_time',  
		sortorder : 'desc',
        usepager : true,
        params : {}
    });
	
	$("#searchBtn").unbind("click");
	$("#searchBtn").bind("click",function(){
		var p = {
                searchText : $("input[name='searchText']").val()
            };
        $(dt).reloadTable({
            params : p
        });
	});
	
	$("#addBtn").unbind("click");
	$("#addBtn").bind("click",function(){
		window.location.href = contextPath + '/app/admin/profession/add';
	});
	
	deleteBtn = function(id){
		Dialog.confirm({
			text : cwn.getLabel('label_core_learning_map_7'),
			callback : function(answer) {
				if (answer) {
					window.location.href = contextPath+"/app/admin/profession/delete?encrypt_pfs_id="+encrytor.cwnEncrypt(id);
				}
			}
			})
	}
	updateBtn = function(field,id){
		
		window.location.href = contextPath + '/app/admin/profession/updatePage?encrypt_pfs_id='+encrytor.cwnEncrypt(id);
	}

	viewResultBtn = function(id){
		window.location.href = contextPath + '/app/admin/profession/viewResult?encrypt_pfs_id='+encrytor.cwnEncrypt(id);
	}
	published = function(id){
		window.location.href = contextPath + '/app/admin/profession/published?encrypt_pfs_id='+encrytor.cwnEncrypt(id);
	}
	cancelPublished = function(id){
		window.location.href = contextPath + '/app/admin/profession/cancelPublished?encrypt_pfs_id='+encrytor.cwnEncrypt(id);
	}
});


var dtModel = [
   {
                		display : '<input id="cms_all" onclick="getCheckedIds()" type="checkbox" value="" name="cms_all_checkbox" />',
                		align : 'left',
                		width : '1%',
                		sortable : false,
                		format : function(data) {
                			p = {
                				text : data.pfs_id
                			}
                			return $('#input-template').render(p);
                		}
                	},        
	{
		display : cwn.getLabel('label_core_learning_map_5'),//"所属路径",
		align : 'left',
		width : '60%',
		sortable : true,
		name : 'pfs_title',
		format : function(data) {
			p = {
				text : data.pfs_title
			};
			return $('#text-template').render(p);
		}
	}
	, 
	/*{
		display : cwn.getLabel('label_core_learning_map_6'),//"必修课",
		align : 'left',
		width : '10%',
		sortable : true,
		name : 'itm_title_lst',
		format : function(data) {
			p = {
					text : data.itm_title_lst
				};
			return $('#text-template').render(p);
		}
	} ,*/
	{
		display : cwn.getLabel('label_core_learning_map_25'),//"更新时间",
		align : 'left',
		width : '10%',
		sortable : true,
		name : 'pfs_update_time',
		format : function(data) {
			p = {
					text : Wzb.displayTime(data.pfs_update_time, Wzb.time_format_ymd)
			};
			return $('#text-template').render(p);
		}
	} 
	, 
	{
        display : "",
        align : 'right',
        width : '30%',
        format : function(data) {
        	p = {
        		id:data.pfs_id,
        		status:data.pfs_status,
        		responseCount:data.responseCount
        	};
        	return $('#operateBtnTemplate').render(p);
        }
    } 

];
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
function batchdel() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.open({
			  title: cwn.getLabel("warning_notice"),
			  content: cwn.getLabel("label_core_learning_map_136")
			});     
		return
	}
	Dialog.confirm({
		text : cwn.getLabel('label_core_learning_map_7'),
		callback : function(answer) {
			if (answer) {
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/profession/batDelete?ids=" + ids,
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
function batchPublish() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.alert(cwn.getLabel("label_core_learning_map_136"));
		return
	}
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/profession/batPublish?ids=" + ids,
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
						$('#cms_all').prop("checked", false);
							$(dt).reloadTable();
					}
	})
}
function batchCancelPublish() {
	var ids="";
	 $('input[name="cms_checkbox"]:checked').each(function() {
		 	ids += $(this).val() + ','; 
		 });
	if(ids==""){
		layer.alert(cwn.getLabel("label_core_learning_map_136"));
		return
	}
				ids=ids.substring(0,ids.length-1);
				$.ajax({
					url : contextPath + "/app/admin/profession/batCancelPublish?ids=" + ids,
					dataType : 'json',
					cache:false,  
					type : "post",
					success : function(data) {
						$('#cms_all').prop("checked", false);
							$(dt).reloadTable();
					}
				})
}
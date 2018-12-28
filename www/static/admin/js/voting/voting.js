var dt,tcrId,searchText;
var encrytor = wbEncrytor();
$(function(){
	dt = $("#itemList").table({
        url : contextPath + '/app/admin/voting/pageJson',
		colModel : dtModel,
        userView : true,
        rp : 10,
		rowSize : 1,    
        hideHeader : false,
		sortname : 'vot_update_timestamp',  
		sortorder : 'desc',
        usepager : true,
        params : {}
    });
	
	$("#searchBtn").unbind("click");
	$("#searchBtn").bind("click",function(){
		searchText = $("input[name='searchText']").val();
		var p = {
		   tcrId : tcrId,
           searchText : searchText
        };
        $(dt).reloadTable({
            params : p
        });
	});
	
	$("#searchText").keydown(function (e) {
	      var curKey = e.which;
	      if (curKey == 13) {
	        $("#searchBtn").click();
	        return false;
	      }
	    });
	
	
	$("#createVotingBtn").unbind("click");
	$("#createVotingBtn").bind("click",function(){
		if(tcrId == undefined) tcrId = 0;
		window.location.href = contextPath + '/app/admin/voting/createVotingPage?tcrId=' + tcrId;
	});
	
	deleteVoting = function(id){
		if(window.confirm(cwn.getLabel('label_core_requirements_management_6'))){
			window.location.href = contextPath+"/app/admin/voting/delete?vot_id="+id;
		}
	}

	updateVotingBtn = function(field,id){
		var count = parseInt($(field).parent().siblings("td:last").text());
		if(count>0){
			var notice = cwn.getLabel("label_core_requirements_management_12");
			alert(notice);
			return;
		}
		window.location.href = contextPath + '/app/admin/voting/updatePage?encrypt_vot_id='+encrytor.cwnEncrypt(id);
	}

	viewResultBtn = function(id){
		window.location.href = contextPath + '/app/admin/voting/viewResult?encrypt_vot_id='+encrytor.cwnEncrypt(id);
	}

	cancelPublished = function(id){
		window.location.href = contextPath + '/app/admin/voting/cancelPublished?encrypt_vot_id='+encrytor.cwnEncrypt(id);
	}
});


function treeClick(e, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    nodes = zTree.getSelectedNodes();
    var v = [];
    for (var i = 0, l = nodes.length; i < l; i++) {
        v.push(nodes[i].name);
    }
    if (v.length > 0) {
        v = v.join(',');
    }
    $("#tcr_text").html(v);
    $("#"+ treeId).parents(".cwn-dropdown").removeClass("open");
    tcrId = treeNode.id;
    var p = {
        tcrId : tcrId,
        searchText : $("input[name='searchText']").val()
    }
    $(dt).reloadTable({
        params : p
    });
}

var dtModel = [
{
    display : cwn.getLabel('global_title'),//"标题",
    align : 'left',
    width : '15%',
    sortable : true,
    name : 'vot_title',
		format : function(data) {
			p = {
				text : data.vot_title
			};
			return $('#text-template').render(p);
		}
	}
	,
	{
	    display : cwn.getLabel('global_training_center'),//"培训中心",
	    align : 'left',
	    width : '12%',
	    sortable : true,
	    name : 'tcr_title',
			format : function(data) {
				p = {
					text : data.tcr_title
				};
				return $('#text-template').render(p);
			}
	}
	, 
	{
		display : cwn.getLabel('label_core_requirements_management_67'),//"发布日期",
		align : 'left',
		width : '23%',
		sortable : true,
		name : 'vot_update_timestamp',
		format : function(data) {
			p = {
				text : data.vot_update_timestamp
			};
			return $('#text-template').render(p);
		}
	}
	, 
	{
		display : cwn.getLabel('global_status'),//"状态",
		align : 'left',
		width : '10%',
		sortable : true,
		name : 'vot_status',
		format : function(data) {
			var status = "";
			if("OFF" === data.vot_status){
				status = cwn.getLabel('label_core_requirements_management_31');
			}else{
				status = cwn.getLabel('label_core_requirements_management_30');
			}
			p = {
				text : status
			};
			return $('#text-template').render(p);
		}
	} 
	, 
	{
        display : cwn.getLabel('label_core_requirements_management_11'),//"回应数目",
        align : 'left',
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
	, 
	{
        display : "",
        align : 'right',
        width : '30%',
        format : function(data) {
        	p = {
        		id:data.vot_id,
        		status:data.vot_status,
        		responseCount:data.responseCount
        	};
        	return $('#operateBtnTemplate').render(p);
        }
    } 

];
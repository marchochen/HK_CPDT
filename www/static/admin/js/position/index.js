var dt,itmParams;
var type="0";
$(function(){
	
	 $('.page-tabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		    if(e.target.hash == '#grade') {
		    	$("#grade-select").show();
		    	$("#grade_label").show();
	    		tab1(e.target.hash);
	    		type="0";
	    	} else if(e.target.hash == '#gradeType') {
	    		$("#grade-select").hide();
	    		$("#grade_label").hide();
	    		tab2(e.target.hash);
	    		type="1";
	    	} else {
	    		$("#grade-select").show();
	    		$("#grade_label").show();
	    		tab1(e.target.hash);
	    		type="0";
	    	}
	 })
	 $("#grade-select").change(function(){
            var p = {
	    			  upc_id:$(this).val(),
	                  searchText : $("input[name='searchText']").val()
	              }
	          itmParams = $.extend(itmParams, p);
	          $(dt).reloadTable({
	              params : itmParams
	          });

    });
	 
	 if(typeof tab == 'undefined' || tab == undefined || tab == '') {
		tab = "1";
	}
    var hash = $('.page-tabs a[data-toggle="tab"]:eq('+ (tab-1) +')').attr("href");
    //初始化数据
    //tabcontent 显示

    eval("tab"+ tab + "('" + hash + "')");
	$('.page-tabs a[data-toggle="tab"]:eq(' + (tab-1) + ')').tab("show");
	 
	 /*
    dt = $("#grouplist").table({
        url : contextPath + '/app/admin/group/list',
		colModel : listModel,
        rp : 10,
        hideHeader : false,
		sortname : 's_grp_create_datetime',
		sortorder : 'desc',
        usepager : true
    });
	 */
	 document.onkeydown = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {

		    	  var p = {
		    			  upc_id:$("#grade-select").val(),
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
				upc_id:$("#grade-select").val(),
                searchText : $("input[name='searchText']").val()
            };
		itmParams = $.extend(itmParams, p);
        $(dt).reloadTable({
            params : itmParams
        });
	});
	
	$("#createBtn").unbind("click");
	$("#createBtn").bind("click",function(){
		if(type=="0"){
		window.location.href = contextPath + '/app/admin/position/create';
		}else{
		window.location.href = contextPath + '/app/admin/position/createCatalog';
		}
	});
	$("#deleteBtn").unbind("click");
	$("#deleteBtn").bind("click",function(){
		var ids="";
		if(type=="0"){
			window.location.href = contextPath + '/app/admin/position/batDel?ids='+ids;
			}else{
			window.location.href = contextPath + '/app/admin/position/batDelCatalog?ids='+ids;
			}
	});

	updateBtn = function(field,id){
		if(type=="0"){
			window.location.href = contextPath + '/app/admin/position/updatePage?upt_id='+id;
			}else{
				window.location.href = contextPath + '/app/admin/position/updatePageCatalog?upc_id='+id;
			}
		
	}

})


function tab1()
{	
	$("#gradeList").empty();
	$("#gradeTypeList").empty();
	  dt = $("#gradeList").table({
	        url : contextPath + '/app/admin/position/pageJson',
			colModel : listModel,
	        rp : 10,
	        hideHeader : false,
			sortname : 'pfs_update_time',
			sortorder : 'desc',
	        usepager : true,
	        params:itmParams
	    });
}


function tab2()
{
	$("#gradeList").empty();
	$("#gradeTypeList").empty();
	 dt = $("#gradeTypeList").table({
		 url : contextPath + '/app/admin/position/catalogPageJson',
			colModel : typeModel,
	        rp : 10,
	        hideHeader : false,
			sortname : 'upc_update_datetime',
			sortorder : 'desc',
	        usepager : true,
	        params:itmParams
	    });
}





//list
var listModel = [
                 {
                		display : '<input id="cms_all" onclick="getCheckedIds()" type="checkbox" value="" name="cms_all_checkbox" />',//"",
                		align : 'left',
                		width : '1%',
                		sortable : false,
                		format : function(data) {
                			p = {
                				text : data.upt_id
                			}
                			return $('#input-template').render(p);
                		}
                	},   
            {
			display : cwn.getLabel('label_core_learning_map_15'),//"编号",
			align : 'left',
			width : '27%',
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
	    display : cwn.getLabel('label_core_learning_map_14'),//"标题",
	    align : 'left',
	    width : '23%',
	    sortable : true,
	    name : 'upt_title',
			format : function(data) {
				p = {
					text : data.upt_title
				};
				return $('#text-template').render(p);
			}
		}
		, 
		
		/*
		{
					display : cwn.getLabel('label_core_learning_map_16'),//"岗位类型",
					align : 'left',
					width : '20%',
					sortable : true,
					name : 'upc_title',
					format : function(data) {
						if (typeof(data.upc_title) == "undefined") { 
							data.upc_title=cwn.getLabel('label_core_learning_map_84');
							}  
						p = {
								text : data.upc_title
							};
						return $('#text-template').render(p);
					}
				} 
				, */
		
		{
			display : cwn.getLabel('label_core_learning_map_25'),//"更新时间",
			align : 'center',
			width : '25%',
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
	        display : cwn.getLabel("global_operation"),
	        align : 'right',
	        width : '24%',
	        format : function(data) {
	        	p = {
	        		id:data.upt_id,
	        		responseCount:data.responseCount
	        	};
	        	return $('#operateBtnTemplate').render(p);
	        }
	    } 
]


typeModel  = [
{
	display : '<input id="cms_all" onclick="getCheckedIds()" type="checkbox" value="" name="cms_all_checkbox" />',//"",
	align : 'left',
	width : '4%',
	sortable : false,
	format : function(data) {
		p = {
			text : data.upc_id
		}
		return $('#input-template').render(p);
	}
     },     
     {
			display : cwn.getLabel('label_core_learning_map_15'),//"编号",
			align : 'left',
			width : '23%',
			sortable : true,
			name : 'upc_update_datetime',
			format : function(data) {
				p = {
						text : Wzb.displayTime(data.upc_update_datetime, Wzb.time_format_ymd)
				};
				return $('#text-template').render(p);
			}
		}
		,
              {
	    display : cwn.getLabel('label_core_learning_map_14'),//"标题",
	    align : 'left',
	    width : '47%',
	    sortable : true,
	    name : 'upc_title',
			format : function(data) {
				p = {
					text : data.upc_title
				};
				return $('#text-template').render(p);
			}
		}
		, 
		
		{
	        display : cwn.getLabel("global_operation"),
	        align : 'right',
	        width : '30%',
	        format : function(data) {
	        	p = {
	        		id:data.upc_id,
	        		responseCount:data.responseCount
	        	};
	        	return $('#operateBtnTemplate').render(p);
	        }
}]

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
		layer.alert(cwn.getLabel("please_select"));
		return
	}
	
	ids=ids.substring(0,ids.length-1);
	if(type=="0"){
		$.ajax({
			url : contextPath + "/app/admin/position/batchdel?ids=" + ids,
			dataType : 'json',
			type : "post",
			success : function(data) {
				if(data.status ==2){
					layer.alert(cwn.getLabel("label_core_learning_map_105"));	
				}else if( data.status ==1){
					layer.load();
					window.location.href=contextPath+'/app/admin/position/getEffectuser?ids='+data.ids
				}
			}
		})
	}else if(type=="1"){
	Dialog.confirm({
		text : cwn.getLabel("warning_delete_notice"),
		callback : function(answer) {
			if (answer) {
				$.ajax({
					url : contextPath + "/app/admin/position/batchdelCatalog?ids=" + ids,
					dataType : 'json',
					type : "post",
					success : function(data) {
						if(data.error != undefined){
							layer.alert(cwn.getLabel("label_core_learning_map_"+ data.error));	
						}else if(data && data.status == 'success'){
							$(dt).reloadTable();
						}
					}
				})
			}
		}
	})
	}
}
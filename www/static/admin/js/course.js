var dt, tcrId, itmParams;
var itm_lst = new wbItem;
var app = new wbApplication;
$(function(){
	itmParams = {itm_type : type};
	var model = dtModel;
	if(cos_type) {
		itmParams = $.extend(itmParams, {cos_type : cos_type})
	}
	if(type == 'open'){
		model = openCourseModel;
	}
    dt = $("#itemList").table({
        url : contextPath + '/app/admin/course/pageJson',
        gridTemplate : function(data){
          data.itm_type_str = cwn.getItemType(data.itm_type, data.itm_exam_ind);
          if(data.itm_publish_timestamp == null || data.itm_publish_timestamp == '' ||　data.itm_publish_timestamp == undefined){
        	  data.itm_publish_timestamp = '--';
          }
          
          if(data.itm_create_run_ind == 1){
        	  data.isOffline = true;
          }
          
          data.lang = lang;
		  return $('#courseGridTemplate').render(data);
		},
		colModel : model,
        userView : true,
        view : 'grid',
        rp : 10,
		rowSize : 1,
        hideHeader : false,
		sortname : 'itm_upd_timestamp',
		sortorder : 'desc',
        usepager : true,
        params : itmParams,
        onSuccess : function(data){
        	var ele_li;
        	if(data.params.view == 'grid') {
        		ele_li = $("a[aria-controls='home']").parent("li");
        	} else {
        		ele_li = $("a[aria-controls='profile']").parent("li");
        	}
        	if(!$(ele_li).hasClass("active")){
    			$(ele_li).addClass("active").siblings().removeClass("active");
    		}
        	//去除每页最后一个课程（或考试）的底边框
        	$(".datatable-table-grid-contrainer").children(".datatable-table-grid-1:last").find("dl").css("border-bottom","0px");
        
        	$(".quick-container").hover(function(){
        		$(this).children(".wzb-quick").toggle();
        		$(this).children(".quick-icon").toggleClass('active');
        	});
        
        }

    }); 

	

    $("a[data-toggle='tab']").click(function(){
        var datatg = this;
        if( $(datatg).attr("aria-controls") == 'profile' ) {
    		$(dt).reloadTable({
    	        view : 'list',
    		});
    		$("ul[role='tablist'] em").hide();
        } else if( $(datatg).attr("aria-controls") == 'home' ){
    		$(dt).reloadTable({
                view : 'grid'
    		});
    		$("ul[role='tablist'] em").show();
        }
    })
    
    $("ul[role=tablist] em").click(function(){
    	var soft = "desc";
    	var softname = $(this).attr("data-column");
    	var ele_i = $(this).find("i");
    	if($(ele_i).length > 0) {
    		if($(ele_i).hasClass("fa-caret-down")){
    			soft = "asc";
    		}
        	$(ele_i).toggleClass("fa-caret-up").toggleClass("fa-caret-down");
    	} else {
    		$(this).append("<i class='fa fa-caret-down skin-color'></i>");
    		$(this).siblings("em").find("i").remove();
    		soft = "desc";
    	}    	
		$(dt).reloadTable({
			sortname : softname,
			sortorder : soft,
		});
    	
    })
    
    
    $("#searchBtn").click(function(){
        var p = {
                searchText : $("input[name='searchText']").val()
            }
        itmParams = $.extend(itmParams, p);
        $(dt).reloadTable({
            params : itmParams
        });
    })
    
})

 $(document).keydown(function(event){
	  if(event.keyCode ==13){
		  $("#searchBtn").click();
	  }
 });

var dtModel = [
           {
           		display : cwn.getLabel('global_name'),//"名称",
           		align : 'left',
           		width : '8%',
           		sortable : true,
           		name : 'itm_title',
           		format : function(data) {
           			p = {
           				text : "<p><a class='wzb-link01 {{cla}}' href='javascript:itm_lst.get_item_detail("+data.itm_id+")' title=''>"+data.itm_title+"</a></p>"
           			}
           			if(data.itm_mobile_ind == 'yes'){
           				p.text =  p.text.replace("{{cla}}","wzb-phone-icon");
           			}else{
           				p.text = p.text.replace("{{cla}}","");
           			}
           			return p.text;
           		}
           	}, 
{
    display : cwn.getLabel('global_code'),//"编号",
    align : 'left',
    width : '8%',
    sortable : true,
    name : 'itm_code',
		format : function(data) {
			p = {
				text : data.itm_code
			}
			return p.text;
		}
	},{
		display : cwn.getLabel('global_kind'),//"类型",
		align : 'left',
		width : '6%',
		sortable : true,
		name : 'itm_type',
		format : function(data) {
			data.itm_type_str = cwn.getItemType(data.itm_type, data.itm_exam_ind);
			p = {
				text : data.itm_type_str
			}
			return p.text;
		}
	} , {
        display : cwn.getLabel('label_core_training_management_27'),//"发布日期",
        align : 'left',
        width : '8%',
        sortable : true,
        name : 'itm_publish_timestamp',
        format : function(data) {
            p = {
                text : data.itm_publish_timestamp == undefined ? '--' : data.itm_publish_timestamp
            }
            return p.text;
        }
    } , {
        display : cwn.getLabel('label_core_training_management_339'),//"最后修改日期",
        align : 'left',
        width : '8%',
        sortable : true,
        name : 'itm_upd_timestamp',
        format : function(data) {
            var date = data.itm_upd_timestamp;
            p = {
                text : date == undefined ? '--' : date
            };
            return p.text;
        }
    },
    {
        display : "",//快速入口按钮
        align : 'right',
        width : '1%',
        sortable : false,
        format : function(data) {
        	data.isOffline = false;
            if(data.itm_create_run_ind == 1){
            	data.isOffline = true;
            }
            return $('#itmQuickEntranceTemplate').render(data);
        }
    }
];

//公开课
var openCourseModel = [
	{
		display : cwn.getLabel('global_name'),//"名称",
		align : 'left',
		width : '6%',
		sortable : true,
   		name : 'itm_title',
   		format : function(data) {
   			p = {
   				text : "<p><a class='wzb-link01' href='javascript:itm_lst.get_item_detail("+data.itm_id+")' title=''>"+data.itm_title+"</a></p>"
   			}
   			return p.text;
   		}
     }, {
        display : cwn.getLabel('global_code'),//"编号",
        align : 'left',
        width : '6%',
        sortable : true,
        name : 'itm_code',
    		format : function(data) {
    			p = {
    				text : data.itm_code
    			}
    			return p.text;
    		}
    	},

    	{
            display : cwn.getLabel('label_core_training_management_27'),//"发布日期",
            align : 'left',
            width : '8%',
            sortable : true,
            name : 'itm_publish_timestamp',
            format : function(data) {
                p = {
                    text : data.itm_publish_timestamp == undefined ? '--' : data.itm_publish_timestamp
                }
                return p.text;
            }
        } , {
            display : cwn.getLabel('label_core_training_management_339'),//"最后修改日期",
            align : 'left',
            width : '8%',
            sortable : true,
            name : 'itm_upd_timestamp',
            format : function(data) {
                var date = data.itm_upd_timestamp;
                p = {
                    text : date == undefined ? '--' : date
                };
                return p.text;
            }
        },
        {
            display : "",//快速入口按钮
            align : 'right',
            width : '1%',
            sortable : false,
            format : function(data) {
            	data.isOffline = false;
                if(data.itm_create_run_ind == 1){
                	data.isOffline = true;
                }
                return $('#itmQuickEntranceTemplate').render(data);
            }
        }

    ];

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
    if(treeNode.level == 0)
    {
    	treeNode.id = 0; 
    }
    var p = {
        tcrId : tcrId = treeNode.id,
        searchText : $("input[name='searchText']").val()
    }
    itmParams = $.extend(itmParams, p);
    $(dt).reloadTable({
        params : itmParams
    });
    initCatalogTree();
}

function treeCatalogClick(e, treeId, treeNode) {
	var p = {
		tndId : treeNode.id,
	}
    itmParams = $.extend(itmParams, p);
    $(dt).reloadTable({
        params : itmParams
    });
}

function COSTypeChoose(){
	$("#cos_type_choose").toggle();
}
function EXAMTypeChoose(){
	$("#exam_type_choose").toggle();
}
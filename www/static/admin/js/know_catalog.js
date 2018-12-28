var dt;
$(function() {
/*	if(type == null || type == ''){
		changeTab($(".now"));
	} else {
		changeTab($("li[name='" + type +"}']"));
	}*/
	
    $('.page-tabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    	changeTab(e.target.hash);
    })
    
    if(typeof tab == 'undefined' || tab == undefined || tab == '') {
		tab = "1";
	}
    var hash = $('.page-tabs a[data-toggle="tab"]:eq('+ (tab-1) +')').attr("href");
    //初始化数据
    changeTab($("#home"));
    $('.page-tabs a[data-toggle="tab"]:eq(' + (tab-1) + ')').tab("show");
   
	$("#search_content").prompt(cwn.getLabel("label_core_community_management_35"));
	
	
	$.ajax({
		url : '/app/admin/know/allKnow/kca/CATALOG/0',
		type : 'POST',
		dataType : 'json',
		async : false,
		success : function(data){
			for(var i=0;i<data.kca.length;i++){
				var html = '<a title="' + data.kca[i].kca_title + '" href="javascript:;" onclick="changeCatalog(this, ' 
						+ data.kca[i].kca_id + ', \'CATALOG\')" id="catalog_' + data.kca[i].kca_id + '" value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</a>';
				$("#parent_catalog").append(html);
			}
		}
	});
	$.ajax({
		url : '/app/admin/know/allKnow/kca/NORMAL/0',
		type : 'POST',
		dataType : 'json',
		async : false,
		success : function(data){
			for(var i=0;i<data.kca.length;i++){
				var html = $("#child_catalog").html() + '<a title="' + data.kca[i].kca_title + '" href="javascript:;" onclick="changeCatalog(this, ' 
						+ data.kca[i].kca_id + ', \'NORMAL\')" id="catalog_' + data.kca[i].kca_id + '" value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</a>';
				$("#child_catalog").html(html);
			}
			if(data.kca.length == 0){
				$("#child_catalog").html("<span class='grayC999' style='padding: 4px 8px;float: left;'>" + cwn.getLabel("label_core_community_management_146") + cwn.getLabel("label_core_community_management_8") + "</span>");
			}
		}
	});
/*		if(${parent_catalog_id != null}){
		$("#catalog_${parent_catalog_id}").click();
	}
	if(${child_catalog_id != null and child_catalog_id > 0}){
		$("#catalog_${child_catalog_id}").click();
	}*/
})
	
var kcaId = 0;
// 切换Tab
function changeTab(thisObj){
	var queType = $(thisObj).attr("name");		
	var url = '/app/admin/know/'+command+'/que/' + queType + '/' + kcaId;
	var searchContent = $("#search_content").val();
	if(searchContent == cwn.getLabel('label_core_community_management_35')){
		searchContent = "";
	}
	$(thisObj).html("");
	dt = $(thisObj).table({
		url : url,
		params : {
			searchContent : searchContent
		},
		colModel : colModel,
		rp : 10,
		showpager : 5,
		sortname : 'que_create_timestamp',
		sortorder : 'desc',
		usepager : true
	})
}
	
var colModel = [ {
	name : '"knowCatalog.kca_title"',
	display : cwn.getLabel('label_core_community_management_6'),
	width : '20%',
	sortable : true,
	format : function(data) {
		p = {
			text : data.knowCatalog.kca_title
		};
		return $('#text-template').render(p);
	}
}, {
	name : 'que_title',
	display : cwn.getLabel('label_core_community_management_16'),
	width : '35%',
	sortable : true,
	format : function(data) {
		p = {
			className : 'toolcolor',
			href : '/app/admin/know/knowDetail/' + data.que_type + '/' + data.que_id,
			title : data.que_title
		};
		return $('#a-template').render(p);
	}
}, {
	name : 'ask_num',
	display : cwn.getLabel('label_core_community_management_18'),
	width : '20%',
	sortable : true,
	align : "center",
	format : function(data) {
		p = {
			text : data.ask_num
		};
		return $('#text-center-template').render(p);
	}
}, {
	name : 'que_create_timestamp',
	display : cwn.getLabel('label_core_community_management_19'),
	width : '25%',
	sortable : true,
	align : "center",
	format : function(data) {
		p = {
			text : data.que_create_timestamp.substring(0,10)
		};
		return $('#text-center-template').render(p);
	}
} ]


// 切换分类
function changeCatalog(thisObj, kca_id, kca_type){
	$("#" + $(thisObj).parent().attr("id") + " .cur").removeClass("cur");
	$(thisObj).addClass("cur");
	kcaId = kca_id;
	$.ajax({
		url : '/app/admin/know/allKnow/kca/NORMAL/' + kca_id,
		type : 'POST',
		dataType : 'json',
		params : {
			kca_type : kca_type,
			kca_id : kca_id
		},
		success : function(data){
			if(kca_type == 'CATALOG'){
				$("#child_catalog").html("");
				for(var i=0;i<data.kca.length;i++){
					var html = $("#child_catalog").html() + '<a title="' + data.kca[i].kca_title + '" href="javascript:;" onclick="changeCatalog(this, ' 
							+ data.kca[i].kca_id + ', \'NORMAL\')" id="catalog_' + data.kca[i].kca_id + '" value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</a>';
					$("#child_catalog").html(html);
				}
				if(data.kca.length == 0){
					$("#child_catalog").html("<span class='grayC999' style='padding: 4px 8px;float: left;'>" + cwn.getLabel("label_core_community_management_146") + cwn.getLabel("label_core_community_management_8") + "</span>");
				}
			}
			
			var searchContent = $("#search_content").val();
			if(searchContent == cwn.getLabel('label_core_community_management_35')){
				searchContent = "";
			}
			$(dt).reloadTable({
				params : {
					searchContent : searchContent
				}
			})
		}
	});
}

// 搜索问题
function searchKnow(){
	var searchContent = $("#search_content").val();
	if(searchContent == cwn.getLabel('label_core_community_management_35')){
		searchContent = "";
	}
	$(dt).reloadTable({
		params : {
			searchContent : searchContent
		}
	})
}
var encrytor = wbEncrytor();
var dt, queType;
$(function() {
/*	if(type == null || type == ''){
		changeTab($(".now"));
	} else {
		changeTab($("li[name='" + type +"}']"));
	}*/
	
    $('.page-tabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    	if(e.target.hash == '#myquestion' || e.target.hash == '#myanswer' || e.target.hash == '#myknowhelp')  {
    		changeMyTab(e.target.hash)
    	} else {
    		changeTab(e.target.hash);
    	}
    })
    
    if(typeof tab == 'undefined' || tab == undefined || tab == '') {
		tab = "1";
	}
    var hash = $('.page-tabs a[data-toggle="tab"]:eq('+ (tab-1) +')').attr("href");
    //初始化数据
    changeTab($("#home"));
    $('.page-tabs a[data-toggle="tab"]:eq(' + (tab-1) + ')').tab("show");
   
	$("#search_content").prompt(cwn.getLabel("label_core_community_management_35")).next().click(function(){
		var searchContent = $("#search_content").val();
		if(searchContent == cwn.getLabel('label_core_community_management_35')){
			searchContent = "";
		}
		$(dt).reloadTable({
			params : {
				searchContent : searchContent
			}
		})
		
	});
	
	
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
				$("#child_catalog").html("<span class='grayC999' style='padding: 4px 8px;'>" + cwn.getLabel("label_core_community_management_124") + "</span>");
			}
		}
	});
})
	
var kcaId = 0;
// 切换Tab
function changeTab(thisObj){
	queType = $(thisObj).attr("name");		
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

name : 'que_title',
display : cwn.getLabel('label_core_community_management_16'),
width : '35%',
sortable : true,
format : function(data) {
	p = {
		className : 'toolcolor',
		href : '/app/admin/know/detail/' + encrytor.cwnEncrypt(data.que_id)+'?ftn_type='+ftn_type,
		title : data.que_title
	};
	return $('#a-template').render(p);
}
}, {
	name : 'kca_title',
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
	name : 'ask_num',
	display : cwn.getLabel('label_core_community_management_18'),
	width : '20%',
	sortable : true,
	align : "left",
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
	align : "right",
	format : function(data) {
		p = {
			text : data.que_create_timestamp.substring(0,10)
		};
		return $('#text-template').render(p);
	}
} ]



//切换Tab
function changeMyTab(thisObj){
	var searchContent = $("#search_content").val();
	if(searchContent == cwn.getLabel('label_core_community_management_35')){
		searchContent = "";
	}
	$(thisObj).html("");
	dt = $(thisObj).empty().table({
		url : '/app/admin/know/myKnow/' + $(thisObj).attr("name"),
		colModel : QAModel,
		params : {
			searchContent : searchContent
		},
		rp : 10,
		showpager : 5,
		usepager : true
	})
}

var QAModel = [ {

name : 'que_title',
display : fetchLabel('label_core_community_management_16'),
width : '35%',
sortable : true,
format : function(data) {
	p = {
		className : 'toolcolor',
		href : '/app/admin/know/detail/' + data.que_type + '/' + encrytor.cwnEncrypt(data.que_id) +'?ftn_type='+ftn_type,
		title : data.que_title
	};
	return $('#a-template').render(p);
}
}, {
	name : 'kca_title',
	display : fetchLabel('label_core_community_management_17'),
	width : '25%',
	sortable : true,
	format : function(data) {
		p = {
			text : data.knowCatalog.kca_title
		};
		return $('#text-template').render(p);
	}
}, {
	name : 'ask_num',
	display : fetchLabel('label_core_community_management_18'),
	width : '13%',
	sortable : true,
	align : "left",
	format : function(data) {
		p = {
			text : data.ask_num
		};
		return $('#text-center-template').render(p);
	}
}, {
	name : 'que_create_timestamp',
	display : fetchLabel('label_core_community_management_19'),
	width : '18%',
	sortable : true,
	align : "left",
	format : function(data) {
		p = {
			text : data.que_create_timestamp.substring(0,10)
		};
		return $('#text-center-template').render(p);
	}
}, {
	name : 'que_type',
	display : fetchLabel('label_core_community_management_154'),
	width : '14%',
	sortable : true,
	align : "right",
	format : function(data) {
		
		
		// 这个标签没有出现过 是 我的问题tab 但是估计是
		/*know_SOLVED : '已解决问题',
		know_UNSOLVED : '待解决问题',
		know_POPULAR : '精选问题',
		know_FAQ : 'FAQ',
		*/
		
		var type = "";
		if(data.que_type=='SOLVED')
		{
			type="label_core_community_management_10";
		}
		else if(data.que_type=='UNSOLVED')
		{
			type="label_core_community_management_9";
		}
		else if(data.que_type=='POPULAR')
		{
			type="label_core_community_management_11";
		}
		else
		{
			type="label_core_community_management_12";
		}
		
		
		
		p = {
			text : fetchLabel(type)
		};
		return $('#text-template').render(p);
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
					$("#child_catalog").html("<span class='grayC999' style='padding: 4px 8px;'>" + cwn.getLabel("none") + cwn.getLabel("label_core_community_management_8") + "</span>");
				}
			}
			var url = '/app/admin/know/'+ command + '/que/' + queType + '/' + kca_id;
			var searchContent = $("#search_content").val();
			if(searchContent == cwn.getLabel('label_core_community_management_35')){
				searchContent = "";
			}
			$(dt).reloadTable({
				url : url,
				params : {
					kcaType : kca_type,
					kcaId : kca_id,
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

$(document).keydown(function(event){
	  if(event.keyCode ==13){
		  searchKnow();
	  }
});
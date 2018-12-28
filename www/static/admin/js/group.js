var dt;
var encrytor = wbEncrytor();
$(function(){
	
	
	
	 $('.page-tabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		    if(e.target.hash == '#mygroup') {
	    		tab1(e.target.hash);
	    	} else if(e.target.hash == '#joingroup') {
	    		tab2(e.target.hash);
	    	} else if(e.target.hash == '#browseropengroup') {
	    		tab3(e.target.hash);
	    	} else {
	    		tab1(e.target.hash);
	    	}
	 })
	
	 
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
	if(showtab == "joingroup") $('.page-tabs a[data-toggle="tab"]:eq(1)').tab("show");
	else if(showtab == "opengroup") $('.page-tabs a[data-toggle="tab"]:eq(2)').tab("show");
	else $('.page-tabs a[data-toggle="tab"]:eq(0)').tab("show");
	
    $("#addgroup").click(function(){
    	window.location.href= contextPath + "/app/admin/group/toAdd?isView="+isView;
    });
    
    $(document).keydown(function(event){
  	  if(event.keyCode ==13){
  		  ssearchGroup();
  	  }
  });
})


function tab1()
{
	$("#opengrouplist").empty();
	$("#joingrouplist").empty();
	$("#grouplist").empty();
	  dt = $("#grouplist").table({
	        url : contextPath + '/app/admin/group/getSnsGroupList/my/'+userEntId,
	        
 
			colModel : listModel,
	        rp : 10,
	        hideHeader : false,
			sortname : 's_grp_create_datetime',
			sortorder : 'desc',
	        usepager : true
	    });
}


function tab2()
{
	$("#opengrouplist").empty();
	$("#joingrouplist").empty();
	$("#grouplist").empty();
	 dt = $("#joingrouplist").table({
	        url : contextPath + '/app/admin/group/getSnsGroupList/find/' + userEntId,
			colModel : joinModel,
	        rp : 10,
	        hideHeader : false,
			sortname : 's_grp_create_datetime',
			sortorder : 'desc',
	        usepager : true
	    });
}


function tab3()
{
	$("#opengrouplist").empty();
	$("#joingrouplist").empty();
	$("#grouplist").empty();
	 dt = $("#opengrouplist").table({
	        url : contextPath + ' /app/admin/group/getSnsGroupList/open/'+userEntId,
			colModel : openModel,
	        rp : 10,
	        hideHeader : false,
			sortname : 's_grp_create_datetime',
			sortorder : 'desc',
	        usepager : true
	    });
}



//list
var listModel = [{
	display : cwn.getLabel('label_core_community_management_63'),
	tdWidth : '40%',
	sortable : true,
	name : 's_grp_title',
	format : function(data) {
		p = {
			className : 'wzb-link02',
			title : data.s_grp_title,
			href : contextPath + '/app/admin/group/detail/' + encrytor.cwnEncrypt(data.s_grp_id) + "?showtab=mygroup&isView=" + isView
		}
		return $('#a-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_64'),
	align : "left",
	tdWidth : '15%',
	sortable : true,
	name : '"member_total"',
	format : function(data) {
		p = {
			text : data.member_total
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_65'),
	align : "left",
	tdWidth : '15%',
	sortable : true,
	name : '"member_wait"',
	format : function(data) {
		p = {
			text : data.member_wait
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_66'),
	align : "left",
	tdWidth : '15%',
	sortable : true,
	name : '"user.usr_display_bil"',
	format : function(data) {
		p = {
			text : data.user.usr_display_bil
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : cwn.getLabel('global_create_date'),
	align : "right",
	tdWidth : '15%',
	sortable : true,
	name : 's_grp_create_datetime',
	format : function(data) {
		p = {
			text : data.s_grp_create_datetime.substring(0,10)
		}
		return $('#text-template').render(p);
	}
}
]


joinModel  = [{
	display : fetchLabel('label_core_community_management_63'),
	tdWidth : '30%',
	format : function(data) {
		p = {
			className : 'wzb-link02',
			title : data.s_grp_title,
			href : contextPath+ '/app/admin/group/detail/'  + encrytor.cwnEncrypt(data.s_grp_id) + "?showtab=joingroup&isView=" + isView
		}
		return $('#a-template').render(p);
	}
}, {
	display : fetchLabel('label_core_community_management_64'),
	align : "left",
	tdWidth : '16%',
	format : function(data) {
		p = {
			text : data.member_total
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : fetchLabel('label_core_community_management_66'),
	align : "left",
	tdWidth : '16%',
	format : function(data) {
		p = {
			text : data.user.usr_display_bil
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : fetchLabel('global_create_date'),
	align : "right",
	tdWidth : '17%',
	format : function(data) {
		p = {
			text : data.s_grp_create_datetime.substring(0,10)
		}
		return $('#text-template').render(p);
	}
}, /*{
	display : fetchLabel('global_operation'),
	align : "right",
	tdWidth : '20%',
	format : function(data) {
		var applyInd = true;
		if(data.s_gpm == undefined || data.s_gpm.s_gpm_status == 3){
			applyInd = false;
		}
		p = {
			event : 'applyJoinGroup(this,' + data.s_grp_id + ')',
			applyInd : applyInd
		}
		return $('#button-template').render(p);
	}
}*/]



openModel = [{
	display : fetchLabel('label_core_community_management_63'),
	tdWidth : '28%',
	format : function(data) {
		p = {
			className : 'wzb-link02',
			title : data.s_grp_title,
			href : contextPath+ '/app/admin/group/detail/' + encrytor.cwnEncrypt(data.s_grp_id)  + "?showtab=opengroup&isView=" + isView
		}
		return $('#a-template').render(p);
	}
}, {
	display : fetchLabel('label_core_community_management_64'),
	align : "left",
	tdWidth : '24%',
	format : function(data) {
		p = {
			text : '--' //data.member_total
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : fetchLabel('label_core_community_management_66'),
	align : "left",
	tdWidth : '24%',
	format : function(data) {
		p = {
			text : data.user.usr_display_bil
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : fetchLabel('global_create_date'),
	align : "right",
	tdWidth : '24%',
	format : function(data) {
		p = {
			text : data.s_grp_create_datetime.substring(0,10)
		}
		return $('#text-template').render(p);
	}
}]


function del(id) {
	Dialog.confirm({
		text : cwn.getLabel("warning_delete_notice"),
		callback : function(answer) {
			if (answer) {
				$.ajax({
					url : contextPath + "/app/admin/group/del/" + id,
					dataType : 'json',
					type : "post",
					success : function(data) {
						if (data && data.status == 'success') {
							$(dt).reloadTable();
						}
					}
				})
			}
		}
	})
}

function searchGroup() {
	var searchContent = $("input[name='searchContent']").val();
	if (searchContent == fetchLabel('search_type_qunzu')) {
		searchContent = '';
	}
	$(dt).reloadTable({
		params : {
			searchContent : searchContent
		}
	})
}
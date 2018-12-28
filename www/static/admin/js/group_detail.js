$(function(){   
    //detail.jsp
    $('.page-tabs a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    	if(e.target.hash == '#home' ) {
    		tab1(e.target.hash);
    	} else if(e.target.hash == '#profile') {
    		tab2(e.target.hash);
    	} else if(e.target.hash == '#approval') {
    		tab3(e.target.hash);
    	} else if(e.target.hash == '#settings') {
    		tab4(e.target.hash);
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
    if(grpPrivate == '2' && tab == 4) {
        eval("tab"+ tab + "('" + 2 + "')");
    	$('.page-tabs a[data-toggle="tab"]:eq(' + 1 + ')').tab("show");
    } else {
        eval("tab"+ tab + "('" + hash + "')");
    	$('.page-tabs a[data-toggle="tab"]:eq(' + (tab-1) + ')').tab("show");
    }
    
    

    //委派事件
    
    //委派添加成员事件
    
    var popupObj = $("#popupAddContent ");
    /*
	$(popupObj).on("click",".wzb-add-group-member" function(){
		var this_ = this;
		var userEntId = $(this).attr("data");
		$.ajax({
			url : contextPath + '/app/group/addGroupMember/' + grpId + '/' + userEntId,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				if(data.error != undefined){
					Dialog.alert(cwn.getLabel('label_core_community_management_' + data.error));
				} else {
					$(this_).html('<i class="fa f14 fbold mr5 fa-check"></i>' + cwn.getLabel('label_core_community_management_140'));
					$(this_).removeClass("wzb-btn-blue");
					//$(this_).off("click");
				}
			}
		});
	})	
	
	*/
	//点击搜索
	$(popupObj).find(".form-search .form-submit").off('click').click(function(){
		addMember($(popupObj), grpPrivate == '3' );
	})   
	//添加所有
	$("#addAll").off('click').click(function(){
    		var userIds = []
    		$(popupObj).find("input[name=groupuser]").each(function(){
    			userIds.push($(this).val());
    		})
    		var target = $(popupObj).find("input[name=groupuser]").next("span");
    		$.ajax({
    			url : contextPath + '/app/admin/group/addMembers/' + grpId,
    			type : 'POST',
    			dataType : 'json',
    			data : {
    				userIds : userIds.join(',')
    			},
    			success : function(data){
    				if(data.error != undefined){
    					Dialog.alert(cwn.getLabel('label_core_community_management_' + data.error));
    				} else {
    					$(target).html('<i class="fa f14 fbold mr5 fa-check"></i>' + cwn.getLabel('label_core_community_management_140'));
    					$(target).removeClass("wzb-btn-blue");
    				}
    			}
    		});
    })
    //只显示讲师
    $("#instrOnly").off('click').click(function(){
		if($(this).hasClass("instrOnly")) {
			$(this).removeClass("instrOnly");
			addMember($(popupObj), false);
			$(this).html(cwn.getLabel("label_core_community_management_97"));	
		}else {
			addMember($(popupObj), true);
			$(this).addClass("instrOnly");
			$(this).html(cwn.getLabel("label_core_community_management_141"));
		}
	})
	
    // 第二个选项卡
	var hash = "#profile";
	//搜索
    $("#profile").find(".form-search .form-submit").off('click').click(function(){
		myMember($(hash));
	});
	//清除所有
	var clearAll = $(hash).find('.form-search .form-tool-right button[type=button]:last');
	$(clearAll).off('click')
	$(clearAll).click(function(){
		var userIds = [];
		$(hash).find("input[name=groupuser]").each(function(){
			userIds.push($(this).val());
		})
		Dialog.confirm({text: cwn.getLabel('warning_clear_all'), callback: function (answer) {
			if(answer){
				$.ajax({
					url: contextPath + '/app/admin/group/clear/'+ grpId,
					type : 'post',
					async : false,
					dataType : 'json',
					data : {
						userIds : userIds.join(",")
					},
					success : function(result){
						myMember($(hash));
					}
				});
			}
		}});
	});
	var percent = $(hash).find(".wzb-percent");
	//转让管理员
	$(percent).on('click', '.wzb-transfer', function(){
		var this_ = this;
		var userEntId = $(this).attr("data");
		$.ajax({
			url : contextPath + '/app/group/changeManager/' + grpId + '/' + userEntId,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				if(data.error == undefined && data.error != ''){
					window.location.reload();
				} else {
					Dialog.alert(cwn.getLabel(data.error));//"label_core_community_management_"+
				}
			}
		})
	})
	//删除成员
	$(percent).on('click', '.wzb-delete-01', function(){
		var this_ = this;
		var userEntId = $(this).attr("data");
		$.ajax({
			url : contextPath + '/app/admin/group/clear/' + grpId,
			type : 'POST',
			dataType : 'json',
			data : {
				userIds : userEntId
			},
			success : function(data){
				if(data.error == undefined && data.error != ''){
					myMember($(hash));
				} else {
					Dialog.alert(cwn.getLabel("label_core_community_management_" + data.error));  
				}
			}
		})
	})
    
})

var cwnEditerInited = false;
function tab1(hash){
	
	//防止重复初始化编辑器
	if(!cwnEditerInited){
		$("#editer").cwnEditer({
	        sessionid : sessionid ,
	        btnTitle : cwn.getLabel("label_core_community_management_113"),
	        fileInitUrl : contextPath +  '/app/upload/Group/uncommit',
	        fileDelUrl : contextPath +  '/app/upload/del/',
	        promptInformation : cwn.getLabel("label_core_community_management_223"),
	        uploadBtns : [
	            {id:'uploadImg', type : 'image', popup : true, name:'<i class="fa fa-file-image-o"></i>&nbsp;' + cwn.getLabel('label_core_community_management_115'), uploadUrl : contextPath + '/app/upload/handle?module=Group&type=Img', onlineUrl : contextPath + '/app/upload/online?module=Group&type=Img'},
	            {id:'uploadDoc', type : 'document', popup : true, name:'<i class="fa fa-file-word-o"></i>&nbsp;' + cwn.getLabel('label_core_community_management_114'), uploadUrl : contextPath + '/app/upload/handle?module=Group&type=Doc', onlineUrl : contextPath + '/app/upload/online?module=Group&type=Doc'},
	            {id:'uploadVideo', type : 'video', popup : true, name:'<i class="fa fa-file-video-o"></i>&nbsp;' + cwn.getLabel('label_core_community_management_116'), uploadUrl : contextPath + '/app/upload/handle?module=Group&type=Video', onlineUrl : contextPath + '/app/upload/online?module=Group&type=Video'}
	        ]
	    });
		cwnEditerInited = true;
	}
    
    
    $("#commentList").empty().cwnDoing({
        editerId : 'editer',
        commentUrl : contextPath + '/app/doing/user/json/' + meId + '/Group/' + grpId,
        commentDisplay : true,
        displayNum : 10,
        params : {
            userId : meId,
            targetId : targetId,
            module : "Group",
            action : 'group',
            id : grpId,
            showtab: showtab
        }
    });
}

function tab2(hash){

	//群组成员
	myMember($(hash));
	
	//隐藏弹框
	var popupObj = $("#popupAddContent");
	$(popupObj).on('hide.bs.modal', function (event) {
    	myMember($(hash));
    })
    //添加成员弹框
    $(popupObj).on('show.bs.modal', function (event) {
    	//初始化弹框列表
    	addMember($(popupObj), grpPrivate == '3');
    })      	
	
}

function tab3(hash){
	var tabul = $(hash).find(".nav-tabs");
	
	appTab("#waiting");
	
	$(tabul).on('shown.bs.tab', function (ele) {
	  	appTab(ele.target.hash);
    })
}

function tab4(hash){
	$("#s_grp_title").prompt(cwn.getLabel('please_input') + cwn.getLabel('label_core_community_management_63'));
	$("#s_grp_desc").prompt(cwn.getLabel('please_input') + cwn.getLabel('label_core_community_management_69'));
	
    $(".s_grp_private[value='" + grpPrivate + "']").attr('checked','true');
}

function myMember(hash){
	var percent = $(hash).find(".wzb-percent");
	$(percent).empty();
	var memberdt = null;
	memberdt = $(percent).table({
		url : contextPath  + '/app/group/getSnsGroupMemberList/myMember/' + grpId,
		params : {
			searchContent : $(hash).find(".form-search input[type='text']").val()
		},
		gridTemplate : function(data){
			p = {
				image :  contextPath + data.user.usr_photo,
				usr_display_bil : data.user.usr_display_bil,
				usg_display_bil : data.user.usg_display_bil,
				isNormal : isNormal,
				ugr_display_bil : data.user.ugr_display_bil,
				href : contextPath + '/app/personal/' + data.s_gpm_usr_id,
				usr_ent_id : data.s_gpm_usr_id,
				isManager : isManager,
				group_manager_id : grpUid,
				meId : meId
			}
			return $('#memberTemplate').render(p);
		},
		view : 'grid',
		rowSize : 4,
		rp : 12,
		showpager : 5,
		usepager : true,
		hideHeader : true,
		useCss : 'wzb-percent',
		onSuccess : function(test,p){
			
			
			if(p.total <=1)
			{
				$(test).parent().find('button').attr("disabled","disabled");
			}
			else
			{
				$(test).parent().find('button').removeAttr("disabled");
			}
			
			
			//hover事件
			$(".wzb-display-01").hover(function(){
			     $(this).find(".wzb-transfer").show();
				 $(this).find(".wzb-delete-01").show();
			},function(){
			     $(this).find(".wzb-transfer").hide();
				 $(this).find(".wzb-delete-01").hide();
			});
		}
	});
}

function addMember(obj, instrOnly) {
	var percent = $(obj).find(".wzb-percent")
	$(percent).empty();
	$(percent).table({
		url : contextPath + '/app/admin/group/findMembers/' + grpId,
		params : {
			searchContent : $(obj).find(".form-search input[type='text']").val(),
			instrOnly : instrOnly
		},
		gridTemplate : function(data){
			p = {
				image : contextPath + data.usr_photo,
				usr_display_bil : data.usr_display_bil,
				usg_display_bil : data.usg_display_bil,
				isNormal : isNormal,
				ugr_display_bil : data.ugr_display_bil,
				href : contextPath + '/app/personal/' + data.usr_ent_id,
				usr_ent_id : data.usr_ent_id,
				add : true,
				grp_id : grpId
			}
			return $('#addMemberTemplate').render(p);
		},
		view : 'grid',
		rowSize : 4,
		rp : 12,
		showpager : 5,
		usepager : true,
		hideHeader : true,
		trLine : false,
		
		onSuccess : function(){
		}
		
	 });
}

function addUser(userEntId,grpId){
	$.ajax({
		url : contextPath + '/app/group/addGroupMember/' + grpId + '/' + userEntId,
		type : 'POST',
		dataType : 'json',
		success : function(data){
			if(data.error != undefined){
				Dialog.alert(cwn.getLabel('label_core_community_management_' + data.error));
			} else {
				$("#user_"+userEntId).html($("#cancel-user-button").render({usr_ent_id:userEntId , grp_id:grpId}));
			}
		}
	});
}

function cancelUser(userEntId,grpId){
	//取消已选中的人
		var p = {
			usr_ent_id : userEntId,
			grp_id : grpId
		}
		$("#user_" + userEntId).html($("#add-user-button").render(p));

		$.ajax({
			url : contextPath + '/app/admin/group/clear/' + grpId,
			type : 'POST',
			dataType : 'json',
			data : {
				userIds : userEntId
			},
			success : function(data){
				if(data.error == undefined && data.error != ''){
					myMember($("#profile"));
				} else {
					Dialog.alert(cwn.getLabel("label_core_community_management_" + data.error));  
				}
			}
	});
}


function appTab(e){
	var tabcontent;
	var target;
	var type = "pending";
	if(typeof e == 'string') {
		tabcontent = $(e).find(".tabcontent");
		target = e;
	} else {
		target = e.target.hash;
		tabcontent = $(e.target.hash).find(".tabcontent");
	}
	var colModel = approveColModel;
	if(target == '#waiting'){
		colModel = pendingColModel;
		type = "pending";
	} else if(target == '#passing'){
		colModel = approveColModel;
		type = "admitted";
	} else if(target == '#refusal') {
		colModel = approveColModel;
		type = "rejected";
	}
	
	$(tabcontent).empty();
	var contentdt = $(tabcontent).table({
		url : contextPath + '/app/group/getSnsGroupMemberList/' + type + '/' + grpId,
		colModel : colModel,
		rp : 10,
		showpager : 5,
		usepager : true,
		sortname : 's_gpm_apply_datetime',
		sortorder : 'desc',
		onSuccess: function(data){
			
		 
			
			
			//var table = $(data).find(".datatable-table");
			var tr =  $(data).find("tbody tr");
			if(tr == undefined  || tr.length==0){
				// 沒有找到了 
				$("#approve").css("display","none");
				$("#reject").css("display","none");
			}else if(tr != undefined  && tr.length > 0 && target == '#waiting'){
				$("#approve").css("display","inline-block");
				$("#reject").css("display","inline-block");
			}
			
			
			var trs = $(data).find("tr");
			$.each(trs,function(i,tr){
			//	console.info(tr);
				
				var td = tr.children.item(1);
				$(td).css("padding-left","0px");
			})
			
			
		}
	})
	
	if(target == '#waiting') {
		var first = $(target).find(".wzb-bar").children(":first");
		$(first).off('click');
		$(first).click(function(){
			appGroupUser(1, function(){
				$(contentdt).reloadTable();
			});
		})
		var last = $(target).find(".wzb-bar").children(":last");
		$(last).off('click');
		$(last).click(function(){ 
			appGroupUser(3, function(){
				$(contentdt).reloadTable();
			});
		})
	}
}

approveColModel = [{
	display : cwn.getLabel('label_core_community_management_119'),
	width : '20%',
    sortable : true,
    name : 's_gpm_apply_datetime',
	format : function(data) {
		p = {
			text : data.s_gpm_apply_datetime.substring(0,16)
		}
		return $('#text-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_120'),
	width : '20%',
	align : 'center',
    sortable : true,
    name : 'usr_display_bil',
	format : function(data) {
		p = {
			text : data.user.usr_display_bil
		}
		return $('#text-center-template').render(p);
	}		
}, {
	display : cwn.getLabel('label_core_community_management_121'),
	width : '20%',
	align : 'center',
    sortable : true,
    name : 'usg_display_bil',
	format : function(data) {
		p = {
			text : data.user.usg_display_bil
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_122'),
	width : '20%',
	align : 'center',
    sortable : true,
    name : 's_gpm_status',
	format : function(data) {
		p = {
			text : cwn.getLabel('label_core_community_management_15' + data.s_gpm_status)
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_123'),
	width : '20%',
	align : 'center',
    sortable : true,
    name : 's_gpm_check_datetime',
	format : function(data) {
		if(data.s_gpm_status == 0){
			return '';
		}
		p = {
			text : data.s_gpm_check_datetime.substring(0,10)
		}
		return $('#text-center-template').render(p);
	}
}]

pendingColModel = [{
	display : '<input type="checkbox" class="qzsel" onclick="checkedAll(this)"/>',
	width : '1%',
    sortable : false,
    name : 's_gpm_id',
	format : function(data) {
		p = {
			name : 's_gpm_id',
			val : data.s_gpm_id
		}
		return $('#checkbox-template').render(p) + '<input type="hidden" value="' + data.user.usr_ent_id + '">';
	}
}, {
	display : cwn.getLabel('label_core_community_management_119'),
	width : '39%',
    sortable : true,
    name : 's_gpm_apply_datetime',
	format : function(data) {
		p = {
			text : data.s_gpm_apply_datetime.substring(0,16)
		}
		return $('#text-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_120'),
	width : '20%',
	align : 'center',
    sortable : true,
    name : 'usr_display_bil',
	format : function(data) {
		p = {
			text : data.user.usr_display_bil
		}
		return $('#text-center-template').render(p);
	}		
}, {
	display : cwn.getLabel('label_core_community_management_121'),
	width : '20%',
	align : 'center',
    sortable : true,
    name : 'usg_display_bil',
	format : function(data) {
		p = {
			text : data.user.usg_display_bil
		}
		return $('#text-center-template').render(p);
	}
}, {
	display : cwn.getLabel('label_core_community_management_122'),
	width : '20%',
	align : 'center',
    sortable : true,
    name : 's_gpm_status',
	format : function(data) {
		p = {
			text : cwn.getLabel('label_core_community_management_15' + data.s_gpm_status)
		}
		return $('#text-center-template').render(p);
	}
}]


function changeGroupCard(thisObj, img_name){
	if($(thisObj).val() == 2){
		$("input[name='image']").attr("disabled", false);
		$("#textfield").css("border", " 1px #AAAAAA solid");
	} else {
		//$("input[name='image']").attr("disabled", true)
		$("input[name='s_grp_card']").val(img_name);
		//$("#textfield").css("border", " 1px #ebebeb solid");
	}
}

function updateSnsGroup(){
	var group_title_prompt = cwn.getLabel('please_input') + cwn.getLabel('label_core_community_management_63');
	if($("input[name='s_grp_title']").val() == '' || $("input[name='s_grp_title']").val() == group_title_prompt){
		Dialog.alert(cwn.getLabel('label_core_community_management_80'));
		return false;
	}
	if(getChars($("input[name='s_grp_title']").val()) > 80){
		Dialog.alert(fetchLabel('label_core_community_management_218'));
		return false;
	}
	
	if(getChars($("textarea[name='s_grp_desc']").val()) > 400){
		Dialog.alert(fetchLabel('group_desc') + fetchLabel('label_title_length_warn_400'));
		return false;
	}
	
	if($("input[name='group_card']:checked").val() == 2){
		if($("input[name='image']").val() != ''){
			var file_ext = $("input[name='image']").val().substring($("input[name='image']").val().lastIndexOf(".") + 1);
			if(file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png' && file_ext != 'jpeg'){
				Dialog.alert(cwn.getLabel('label_core_community_management_143'));
				return;
			}
		} else {
			Dialog.alert(cwn.getLabel('label_core_community_management_144'));
			return;
		}
	}else{
		$("input[name='image']").attr("disabled", true)
	}
	$("#groupForm").submit();
	/*$.ajax({
		url : contextPath + '/app/group/applyJoinGroup/'+ grpId,
		type : 'POST',
		dataType : 'json',
		success : function(data){
			
			
			var text ;
			
			if(data.message =='group_apply_ok' ){
				text = "label_core_community_management_147"
			}
			else{
				text = "label_core_community_management_148"
			}
				
			
			Dialog.alert(fetchLabel(text));
			$(".pjia").html('<a class="btn wzb-btn-orange" href="javascript:;" onclick="signOutGroup(\'label_core_community_management_180\')">' + fetchLabel('label_core_community_management_149') + '</a>');
		}
	});*/
}

 
function applyJoinGroup(){
	$.ajax({
		url : contextPath + '/app/group/applyJoinGroup/'+ grpId,
		type : 'POST',
		dataType : 'json',
		success : function(data){
			var text ;
			if(data.message =='group_apply_ok' ){
				text = "label_core_community_management_147"
			}
			else{
				text = "label_core_community_management_148"
			}
			Dialog.alert(fetchLabel(text));
			$(".pjia").html('<a class="btn wzb-btn-orange" href="javascript:;" onclick="signOutGroup(\'label_core_community_management_180\')">' + fetchLabel('label_core_community_management_149') + '</a>');
		}
	});
}

 

function signOutGroup(text){
	Dialog.confirm({text:fetchLabel(text), callback: function (answer) {
			if(answer){
				$.ajax({
					url : contextPath + '/app/admin/group/signOutGroup/'+ grpId,
					type : 'POST',
					success : function() {
						window.location=contextPath+'/app/admin/group'
					}
				});
			}
		}
	});
}

function checkedAll(thisObj){
	if($(thisObj).attr("checked") == undefined){
		$("input[name='s_gpm_id']").attr("checked", false);
	} else {
		$("input[name='s_gpm_id']").attr("checked", true);
	}
}

function appGroupUser(status, callback){
	var gpmIds = new Array();
	var userIds = new Array();
	var checkeds = $("input[name='s_gpm_id']:checked");
	if(checkeds.length == 0){
		Dialog.alert(fetchLabel('label_core_community_management_152'));
		return;
	}
	for(var i=0;i<checkeds.length;i++){
		gpmIds.push(checkeds.eq(i).val());
		userIds.push(checkeds.eq(i).next().val());
	}
	$.ajax({
		url : contextPath + '/app/admin/group/updateGpmStatus/' + grpId + '/' + status,
		dataType : 'json',
		type : 'post',
		data : {
			'userIds' : userIds.join(','),
			'gpmIds' : gpmIds.join(',')
		},
		success : function(){
			if(callback) {
				callback(this);
			}
		}
	})
}

function OpenOrRetract(thisObj){
	if($(thisObj).find("span").html() == fetchLabel('click_down')){
		$(thisObj).find("span").html(fetchLabel('click_up'));
		$(thisObj).find("i").removeClass("fa-angle-down");
		$(thisObj).find("i").addClass("fa-angle-up");
		//$("#desc_content").removeClass("font_width_limit");
		$("#grp_desc").css("display","inline");
		$("#desc_content").css("display","block");
		$("#desc_content").css("white-space","normal");
	} else {
		$(thisObj).find("span").html(fetchLabel('click_down'));
		$(thisObj).find("i").removeClass("fa-angle-up");
		$(thisObj).find("i").addClass("fa-angle-down");
		//$("#desc_content").addClass("font_width_limit");
		$("#grp_desc").css("display","inline");
	    $("#desc_content").css("white-space","nowrap");
		//$("#desc_content").css("display","-webkit-box");
	}
}
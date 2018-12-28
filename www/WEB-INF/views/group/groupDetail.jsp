<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<link href="${ctx }/static/js/jquery.uploadify/uploadify.css" rel="stylesheet" type="text/css" />  
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_util.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>

<style type="text/css">
	.logcot{ width:510px; height:130px; margin:200px auto 280px auto; padding-left:155px; background:url(${ctx}/static/images/ok.png) no-repeat;}
</style>
<title></title>
<script type="text/javascript">
	var sns = new Sns();
	var targetId;
	var isNormal = ${isNormal};
	var meId = ${regUser.usr_ent_id};
	var isManager =${isManager};
	
	var module = "Group";
	var grp_id = ${snsGroup.s_grp_id};
	var appTkhID;
	var a_id = Wzb.getUrlParam('a_id');
	$(function() {
		var len = 130;
		var desc = $("#grp_desc").attr("data");
		var group_desc_sub = desc ? substr(desc, 0, len) : "";
		if(desc == undefined || getChars(desc) < len) {
			$("#grp_desc").next().css("display","none");
		}
		$("#grp_desc").html(group_desc_sub);
		
		//简介展开收起
		$("a.open_desc").live('click',function() {
			var sub = $(this).children("i");
			if($(sub).hasClass("fa-angle-down")) {
				$(sub).removeClass("fa-angle-down").addClass("fa-angle-up").prev("span").html(fetchLabel('click_up'));
				var pv = $(this).prev();
				var data = $(pv).attr("data");
				if(data != undefined && data != '') {
					$(pv).empty().append(data);
				}
			} else {
				$(sub).removeClass("fa-angle-up").addClass("fa-angle-down").prev("span").html(fetchLabel('click_down'));
				var pv = $(this).prev();
				var data = $(pv).attr("data");
				if(data != undefined && data != '') {
					$(pv).empty().append(substr(data, 0, len));
				}
			}
		});
		
		//群组修改成功就弹出提示
		if("${result_msg }"== "update_ok"){
			Dialog.showSuccessMessage("${ctx}/app/group/groupDetail/${snsGroup.s_grp_id}?a_id=groupList&tab=group_set");
		}else if("${result_msg }"== "power_error"){
			Dialog.alert(fetchLabel("power_error"));
		}
		if(Wzb.getUrlParam('tab') == 'group_set'){
			$('li a[href="#settings"]').parent().click();
		}
		
		if(a_id == "groupOpen"){
			$(".wzb-link01").attr("href","${ctx}/app/group/groupOpen/0");
		}else if(a_id == "groupFind"){
			$(".wzb-link01").attr("href","${ctx}/app/group/groupFind/0");
		}else{
			$(".wzb-link01").attr("href","${ctx}/app/group/groupList/0");
		}
		
		if(isNormal != true)
		{
		$("div#menu,div#header").remove();
		}
		//$("#formText").prompt(fetchLabel('validate_max_400'));
		
		if('${tab}' == 'group_set'){
			$(".wbtabnav li").last().click();
		}
		
		dt = $("#groupDoingList").table({
			url : '${ctx}/app/doing/user/json/' + meId + '/Group/' + grp_id,
			colModel : colModel,
			rowCallback : function(data){
				$("#groupDoingList .title:last").css("width",$(".cont").width()*0.85);
				var replies = data.replies;
				loadSubComment(data.replies, $("#groupDoingList .parcel .queen:last"));
				$("#groupDoingList .parcel:last .review:first").find("span").html(data.replies?data.replies.length:0);

				$.templates({
					fileTemplate : '<p class="mt10 f14"><a href="${ctx}{{>url}}" target="_blank">{{>name}}</a></p>',
					imgTemplate : '<p class="mt10 f14"><a href="${ctx}{{>url}}" target="_blank"><img width="60" src="${ctx}{{>url}}"/></a></p>'
				})
				var fileList = data.fileList;
				if(fileList != undefined){
					$.each(fileList,function(i, val){
						var url,name;
						name = val.mtf_file_name;
						url = val.mtf_url;
						if(val.mtf_file_type == 'url'){
							name = val.mtf_url;
						} else {
							url = url + "/" + val.mtf_file_rename;
						}
						var p ={
							name : name,
							url : url		
						}
						var  fileHtml ;
						if(val.mtf_type=='Img'){
							fileHtml = $.render.imgTemplate(p)
						} else {
							fileHtml = $.render.fileTemplate(p);
						}
						$("#groupDoingList .parcel:last").find(".writing01").after(fileHtml);
					});
				}

				
				$("#groupDoingList .parcel .like:last").like({
					count : data.snsCount?data.snsCount.s_cnt_like_count:0,
					flag : data.is_user_like,
					id : data.s_doi_id,
					module: module,
					tkhId : 0
				})
				
				$('.wzb-user').hover(function(){
					var el = $(this);
					el.find('div').stop().animate({width:200,height:200},'slow',function(){
						el.find('p').fadeIn('fast');
					});
				
				},function(){
					var el = $(this);
					el.find('p').stop(true,true).hide();
					el.find('div').stop().animate({width:60,height:60},'fast');
				}).click(function(){
					if(isNormal == true)
					window.location.href = $(this).find('a').attr('href');
				});
				
			},
			rp : 10,
			hideHeader : true,
			usepager : true
		})
		
		//点击回复按钮
		$(".huifu").live('click',function(){
			$(this).parents(".parcel").eq(0).find("form").remove();
			$(this).parents(".parcel").eq(0).find(".queen").append($("#commentFormTemplate").render());
			var text = "";
			if($(this).attr("uname")){;
				 text = fetchLabel('detail_comment_back') + $(this).attr("uname") + "："; //"回复"
			} else {
				 text = fetchLabel('doing_publish_comment');
			}
			$("textarea.replypl").val(text);
			$("textarea.replypl").prompt(text).attr("uid", $(this).attr("uid"))
 			.attr('did',$(this).attr("did"));;
		})
		
		//评论回复功能
		$(".review").live('click',function(){
			var parcel = $(this).parents(".parcel").eq(0);
			$(parcel).find(".queen").show();
			var text = "";
			if($(this).attr("uname")){;
				 text = fetchLabel('detail_comment_back') + $(this).attr("uname") + ":"; //"回复"
			} else {
				 text = fetchLabel('doing_publish_comment');
			}
			if(text == $(parcel).find("form").find("textarea").val()){
				$(parcel).find("form").remove();
				$(parcel).find(".queen").hide();
			} else {
				$(parcel).find("form").remove();
				$(parcel).find(".queen").append($("#commentFormTemplate").render());
				$("textarea.replypl").prompt(text).attr("uid", $(this).attr("uid"))
				.attr('did',$(this).attr("did"));;
			}
		})
		//删除
		$(".delete").live('click',function(){
			//区分是不是删除二级的
			var reply = $(this);
			var parcel = $(this).closest(".parcel");
			var topParentID = $(this).closest(".queen").attr("data");
			sns.comment.del($(this).attr("data"),topParentID, function(result){
				if(result && result.status == 'success'){
					if($(reply).hasClass("desc")){
						$(parcel).find(".queen").empty();
						loadSubComment(result.replies, $(parcel).find(".queen"));
						$(parcel).find(".review span").html(result.replies?result.replies.length:0);
					} else{
						$(dt).reloadTable({
							url : '${ctx}/app/doing/user/json/' + userId + '/Doing/' + userId
						});
					}
				}
			});
		});
		$(".deleteDoing").live('click',function(){
			//区分是不是删除二级的
			var reply = $(this);	
			sns.doing.del($(this).attr("data"), function(result){
// 				if(result && result.status == 'success'){
					$(dt).reloadTable({
						url : '${ctx}/app/doing/user/json/' + meId  + '/Group/' + grp_id
					});
// 				}
			});
		});

		//评论
		$("#courseCommentSubmit").click(function(){
			var note = $("#courseComment").val();
			if(note == $("#courseComment").attr('prompt') || note == '' || note == undefined){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 400 ){
					Dialog.alert(fetchLabel('validate_max_400')); //'不能超过400个字符'
					return;
				}
			}
			targetId = meId;
			sns.comment.add(targetId, 0, 0, appTkhID?appTkhID:0, module, note, function(result){
				if(result.status=='success') {
					$(dt).reloadTable({
						url : '${ctx}/app/doing/user/json/' + meId  + '/Group/' + grp_id
					});
				}
					//成功刷新
			});
		});

		//评论回复
		$(".replybtn").live('click', function(){
			var replyToId = $(this).closest(".parcel").attr("did");
			//var replyToId = $(textarea).attr("did");		//分级
			var textarea = $(this).prev();
			var toUserId = $(textarea).attr("uid");
			var note = $(textarea).val();
			var replyId = $(textarea).attr("did");
			if(note == $(textarea).attr('prompt')){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 400 ){
					Dialog.alert(fetchLabel('validate_max_400')); //'不能超过400个字符'
					return;
				}
			}
			//新增
	 		sns.comment.add(replyToId,  replyId?replyId:replyToId, toUserId,  appTkhID?appTkhID:0, module, note, function(result){
				if(result.replies){
					var queen = $(textarea).closest(".queen");
					$(queen).empty();
					loadSubComment(result.replies,$(queen));

					//数量增加
					$(queen).closest(".parcel").find(".review span").html(result.replies?result.replies.length:0);
				}
			}); 
		});
		
	})
	
	colModel = [{
		format : function(data) {
			$.extend(data,{isMeInd : (meId == data.s_doi_uid),
				isNormal : isNormal,
				s_doi_create_datetime : data.s_doi_create_datetime.substring(0,16),
				s_doi_title : data.s_doi_title.replace(/"/g,"")})
			
			return $('#doing-Template').render(data);
		}
	}]
	
	function changeTab(thisObj,id){
		$(".active").removeClass("active");
		$(thisObj).addClass("active");
		$(".wbtabcont").hide();
		$("#" + id).show();
		if(id == 'group_detail'){
			
		} else if(id == 'member_list'){
			refreshMyMember('');
		} else if(id == 'approve_list'){
			changeApproveTab($(".qztabnav").children().eq(0),"pending");
		} else if(id == 'group_set'){
			$("input[name='s_grp_title']").val('${snsGroup.s_grp_title}');

			$("input[name='s_grp_private']").eq(${snsGroup.s_grp_private}).attr("checked", true);
			if('${snsGroup.s_grp_card}'.length > 0){
				$("input[name='s_grp_card']").val('${snsGroup.s_grp_card}');
				$("input[name='group_card']").eq(0).attr("onclick","changeGroupCard(this,'" + '${snsGroup.s_grp_card}' + "')");
				$("#group_image").attr("src", "${ctx}${snsGroup.card_actual_path}");
			}
		}
	}
	
	function refreshMyMember(searchContent){
		$("#my_member").html('');
		$("#my_member").table({
			url : '${ctx}/app/group/getSnsGroupMemberList/myMember/${s_grp_id}',
			params : {
				searchContent : searchContent
			},
			gridTemplate : function(data){
				p = {
					image : data.user.usr_photo,
					usr_display_bil : data.user.usr_display_bil,
					usg_display_bil : data.user.usg_display_bil,
					isNormal : isNormal,
					ugr_display_bil : data.user.ugr_display_bil,
					href : 'javascript:;',
					usr_ent_id : data.s_gpm_usr_id,
					isManager : ${isManager},
					group_manager_id : ${snsGroup.s_grp_uid},
					meId : meId,
					add : false
				}
				return $('#member-template').render(p);
			},
			rowCallback : function(){
				photoDoing();
			},
			view : 'grid',
			rowSize : 3,
			rp : 12,
			showpager : 5,
			usepager : true,
			hideHeader : true,
			onSuccess : function(test,p){
				
				
				if(p.total <=1)
				{
					$(test).parent().find(".form-tool-right:eq(0)").find('a:eq(1)').attr('disabled','disabled')
				}
				else
				{
					$(test).parent().find(".form-tool-right:eq(0)").find('a:eq(1)').removeAttr("disabled");
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
		$("#my_member").attr("style",$("#my_member").attr("style") + "overflow:visible;");
		$("#my_member").find(".datatable-body").attr("style","overflow:visible;");
	}
	
	$("#addMember").live('click', function(){
		 $('#msgBox').modal('show');
		 refreshAddMember('', '${snsGroup.s_grp_private eq "3"}');
	});
    
    $("#instrOnly").live('click', function(){
		if($(this).hasClass("instrOnly")) {
			$(this).removeClass("instrOnly");
	        refreshAddMember('', false)
			$(this).val(fetchLabel('group_show_instr_only'));
		}else {
	        refreshAddMember('', true)
			$(this).addClass("instrOnly");
			$(this).val(fetchLabel('group_all_users'));
		}
    })
	
	function refreshAddMember(searchContent, instrOnly){
		 $("#add_member").html('');
		 $("#add_member").table({
			url : '${ctx}/app/group/findGroupMemberList/${s_grp_id}',
			params : {
				searchContent : searchContent,
				instrOnly : instrOnly
			},
			gridTemplate : function(data){
				p = {
					image : data.usr_photo,
					usr_display_bil : data.usr_display_bil,
					usg_display_bil : data.usg_display_bil,
					isNormal : isNormal,
					isManager : isManager,
					ugr_display_bil : data.ugr_display_bil,
					href : 'javascript:;',
					usr_ent_id : data.usr_ent_id,
					add : true
				}
				return $('#member-template').render(p);
			},
			rowCallback : function(){
				photoDoing();
			},
			view : 'grid',
			rowSize : 3,
			rp : 12,
			showpager : 5,
			usepager : true,
			hideHeader : true,
			trLine : false,
			onSuccess : function(test,p){
				
				
			 
			}
			
			
		 });
	}
	
	function changeApproveTab(thisObj, type){
		$(".cur").removeClass("cur");
		$(thisObj).addClass("cur");
		$("#approve_list .wbtable").html('');
		var colModel = approveColModel;
		if(type == 'pending'){
			colModel = pendingColModel;
			$(".qzcont").show();
		} else {
			$(".qzcont").hide();
		}
		$("#approve_list .wbtable").table({
			url : '${ctx}/app/group/getSnsGroupMemberList/' + type + '/${s_grp_id}',
			colModel : colModel,
			rp : 10,
			showpager : 5,
			usepager : true,
			onSuccess: function(data){
				var total = data.params.total;
				if(total == 0){
					$(".qzcont").hide();
				}
				var a_id = Wzb.getUrlParam('a_id');
				$("#"+a_id).addClass("cur");
			}
		})
	}
	
	approveColModel = [{
		display : fetchLabel('group_apply_date'),
		width : '20%',
		format : function(data) {
			p = {
				text : data.s_gpm_apply_datetime.substring(0,16)
			}
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('group_student_name'),
		width : '20%',
		align : 'center',
		format : function(data) {
			p = {
				text : data.user.usr_display_bil
			}
			return $('#text-center-template').render(p);
		}		
	}, {
		display : fetchLabel('usr_group'),
		width : '20%',
		align : 'center',
		format : function(data) {
			p = {
				text : data.user.usg_display_bil
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_approve_status'),
		width : '20%',
		align : 'center',
		format : function(data) {
			p = {
				text : fetchLabel('group_approve_status_' + data.s_gpm_status)
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_approve_date'),
		width : '20%',
		align : 'center',
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
		width : '5%',
		format : function(data) {
			p = {
				name : 's_gpm_id',
				val : data.s_gpm_id
			}
			return $('#checkbox-template').render(p) + '<input type="hidden" value="' + data.user.usr_ent_id + '">';
		}
	}, {
		display : fetchLabel('group_apply_date'),
		width : '35%',
		format : function(data) {
			p = {
				text : data.s_gpm_apply_datetime.substring(0,16)
			}
			return $('#text-template').render(p);
		}
	}, {
		display : fetchLabel('group_student_name'),
		width : '20%',
		align : 'center',
		format : function(data) {
			p = {
				text : data.user.usr_display_bil
			}
			return $('#text-center-template').render(p);
		}		
	}, {
		display : fetchLabel('usr_group'),
		width : '20%',
		align : 'center',
		format : function(data) {
			p = {
				text : data.user.usg_display_bil
			}
			return $('#text-center-template').render(p);
		}
	}, {
		display : fetchLabel('group_approve_status'),
		width : '20%',
		align : 'center',
		format : function(data) {
			p = {
				text : fetchLabel('group_approve_status_' + data.s_gpm_status)
			}
			return $('#text-center-template').render(p);
		}
	}]
	
	function checkedAll(thisObj){
		if($(thisObj).attr("checked") == undefined){
			$("input[name='s_gpm_id']").attr("checked", false);
		} else {
			$("input[name='s_gpm_id']").attr("checked", true);
		}
	}
	
	function updateGpmStatus(s_gpm_status){
		var arrayObj = new Array();
		var usr_ent_id_list = new Array();
		var checkedObj = $("input[name='s_gpm_id']:checked");
		if(checkedObj.length == 0){
			Dialog.alert(fetchLabel('subordinate_please_select'));
			return;
		}
		for(var i=0;i<checkedObj.length;i++){
			arrayObj.push(checkedObj.eq(i).val());
			usr_ent_id_list.push(checkedObj.eq(i).next().val());
		}
		$("#approve_list .wbtable").html('');
		$("#approve_list .wbtable").table({
			url : '${ctx}/app/group/updateGpmStatus/${s_grp_id}/' + s_gpm_status + '/' + arrayObj + '/' + usr_ent_id_list,
			colModel : pendingColModel,
			rp : 10,
			showpager : 5,
			async : false,
			usepager : true,
			onSuccess: function(data){
				var total = data.params.total;
				if(total == 0){
					$(".qzcont").hide();
				}
			}
		});
	}
	
	function changeGroupCard(thisObj, img_name){
		if($(thisObj).val() == 2){
			//$("input[name='image']").attr("disabled", false);
		} else {
			//$("input[name='image']").attr("disabled", true);
			$("input[name='s_grp_card']").val(img_name);
		}
	}
	
	function updateSnsGroup(){
		if($("input[name='s_grp_title']").val() == ''){
			Dialog.alert(fetchLabel('group_name_not_null'));
			return false;
		}
		if(getChars($("input[name='s_grp_title']").val()) > 80){
			Dialog.alert(fetchLabel('group_name') + fetchLabel('label_title_length_warn_80'));
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
					Dialog.alert(fetchLabel('usr_img_type_limit'));
					return;
				}
			} else {
				Dialog.alert(fetchLabel('usr_select_upload_head'));
				return;
			}
		}else{
			$("input[name='image']").attr("disabled", true);
		}
		$("#groupForm").submit();
	}
	
	function addGroupMember(usr_ent_id){
		$.ajax({
			url : '${ctx}/app/group/addGroupMember/${s_grp_id}/' + usr_ent_id,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				if(data.error != undefined){
					Dialog.alert(fetchLabel('group_' + data.error));
				} else {
					$("#member_" + usr_ent_id).html($("#cancel-user-button").render({usr_ent_id:usr_ent_id}));
				}
			}
		});
	}
	
	//取消已选中的讲师
	function cancelGroupMember(usr_ent_id){
		var p = {
			usr_ent_id : usr_ent_id,
		}
		deleteGroupMember(usr_ent_id);
		$("#member_" + usr_ent_id).html($("#add-user-button").render(p));
		
	}
	
	function searchUserList(name){
		$(".wzb-find").html('');
		var searchContent = $("input[name='" + name + "']").val();
		if(searchContent == fetchLabel('attention_find_desc')){
			searchContent = '';
		}
		if(name == 'search_my_member'){
			refreshMyMember(searchContent);
		} else {
			refreshAddMember(searchContent);
		}
	}
	
	function showUpdateGroup(usr_ent_id){
		$("#group_assignment_" + usr_ent_id).show();
		$("#delete_member_" + usr_ent_id).show();
	}
	
	function hideUpdateGroup(usr_ent_id){
		$("#group_assignment_" + usr_ent_id).hide();
		$("#delete_member_" + usr_ent_id).hide();
	}
	
	function deleteGroupMember(usr_ent_id){
		$.ajax({
			url : '${ctx}/app/group/deleteGroupMember/${s_grp_id}/' + usr_ent_id,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				if(data.error == undefined && data.error != ''){
					refreshMyMember('');
				} else {
					Dialog.alert(fetchLabel(data.error));
				}
			}
		});
	}
	
	function changeManager(usr_ent_id){
		$.ajax({
			url : '${ctx}/app/group/changeManager/${s_grp_id}/' + usr_ent_id,
			type : 'POST',
			dataType : 'json',
			success : function(data){
				if(data.error == undefined && data.error != ''){
					window.location.href = '${ctx}/app/group/groupDetail/${s_grp_id}';
				} else {
					Dialog.alert(fetchLabel(data.error));
				}
			}
		})
	}
	
	function applyJoinGroup(){
		$.ajax({
			url : '${ctx}/app/group/applyJoinGroup/${s_grp_id}',
			type : 'POST',
			dataType : 'json',
			success : function(data){
				Dialog.alert(fetchLabel(data.message));  //grayC333
				$(".pjia").html('<a class="btn wzb-btn-orange" href="javascript:;" onclick="signOutGroup(\'group_confirm_cancel_app\')">' + fetchLabel('group_cancel_app') + '</a>');
			}
		});
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
	
	function signOutGroup(text){
		Dialog.confirm({text:fetchLabel(text), callback: function (answer) {
				if(answer){
					$.ajax({
						url : '${ctx}/app/group/signOutGroup/${snsGroup.s_grp_id}',
						type : 'POST',
						success : function() {
							window.location.href = '${ctx}/app/group/groupList/0';
						}
					});
				}
			}
		});
	}
	
	function loadSubComment(replies, obj){
		$.each(replies,function(i,val){
			$.extend(val,{isMeInd : meId == val.s_cmt_uid, isNormal : isNormal})
			$(obj).append($("#commentDescTemplate").render(val));
			if(isNormal){
				$("#art_like_count_" + val.s_cmt_id).like({
					count : val.snsCount ? val.snsCount.s_cnt_like_count:0,
					flag : val.is_user_like,
					id : val.s_cmt_id,
					module: 'Group',
					isComment : 1,
					tkhId : 0
				})
			}
			if(val.replies != undefined && val.replies.length>0){
				loadSubComment(val.replies,$(obj).find(".reply:last"));
			}
			$(obj).find(".wbdu:last").css("width",$(".cont").width()*0.8);
		})
	}
	
</script>

<script id="add-user-button" type="text/x-jsrender">
	<a name="addGroup" class="wzb-link04" href="javascript:;" title='<lb:get key="global.button_add"/>' onclick="addGroupMember({{>usr_ent_id}})">
		<i class="fa fa-plus"></i>
		<lb:get key="global.button_add"/>
	</a>
</script>
<script id="cancel-user-button" type="text/x-jsrender">
	<span class="skin-color">
		<i class="fa f14 fbold mr5 fa-check"></i>
		<lb:get key="group_add_ok"/>
	</span> | 
	<a name="clearGroup" id="delete_member_{{>usr_ent_id}}" class="grayC666" href="javascript:;" onclick="cancelGroupMember({{>usr_ent_id}})"><lb:get key="global.button_cancel"/></a>
</script>
<!-- 动态模板 -->
<jsp:include page="../common/doingTemplate.jsp"></jsp:include>
<script id="doing-Template" type="text/x-jsrender">
		<div class="wzb-trend parcel clearfix" did="{{>s_doi_id}}">
			<div class="wzb-user wzb-user68">
		      	{{if s_doi_target_type == '1' && operator}}
					<a class="trendpic" {{if isNormal == true}}href="javascript:;"{{/if}}> <img src="${ctx}{{>operator.usr_photo}}" class="wzb-pic"/></a>
				{{else}}
					<a class="trendpic" {{if isNormal == true}}href="javascript:;"{{/if}}> <img src="${ctx}{{>user.usr_photo}}" class="wzb-pic"/></a>
				{{/if}}
				{{if isNormal == true}}
					<p class="companyInfo"><lb:get key="know_ta"/></p>
					<div class="cornerTL">&nbsp;</div>
					<div class="cornerTR">&nbsp;</div>
					<div class="cornerBL">&nbsp;</div>
					<div class="cornerBR">&nbsp;</div>
				{{/if}}
			</div>
		 
			<div>
				{{if s_doi_target_type == '1'}}
					<p class="color-gray999"><a href="javascript:;" class="skin-color mr5" title="">
					{{if operator}}{{>operator.usr_display_bil}}{{else}}{{>user.usr_display_bil}}{{/if}}
					</a><lb:get key="doing_in"/> {{>s_doi_create_datetime}} {{>s_doi_act_str}}:</p>
				{{else}}
					<p class="color-gray999">{{if isNormal == true}}<a href="javascript:;" class="skin-color margin-right5" title="">{{/if}}<span>{{>user.usr_display_bil}}</span>{{if isNormal == true}}</a>{{/if}}
					{{>s_doi_create_datetime}} {{>s_doi_act_str}}:</p>
				{{/if}}
		      
				<p class="margin-top10 writing01">{{>s_doi_title}}</p>
				<div class="margin-top10">
		      	{{if s_doi_target_type != '1'}}

					<a class="review margin-right15 color-gray666" uname="{{>user.usr_display_bil}}" uid="{{>user.usr_ent_id}}" href="javascript:void(0);"><i class="fa skin-color margin-right5 fa-comment"></i><lb:get key="sns_comment"/>(<span>0</span>)</a>
				{{/if}}
				{{if s_doi_uid == '${prof.usr_ent_id}' || isNormal == false}}
					<a class="color-gray666 deleteDoing" data="{{>s_doi_id}}" href="javascript:;"><i class="glyphicon skin-color margin-right5 glyphicon-remove"></i><lb:get key="button_del"/></a>
				{{/if}}
				</div>
		        <div class="queen mt15" data="{{>s_doi_id}}">                   

				</div>
			</div>
		</div>
</script>
<!-- 贊功能 -->
<%-- {{if s_doi_act != 'like'}}
   <a class="margin-right15 color-gray666 like" id="doi_{{>s_doi_id}}" href="javascript:;" title=""><i class="fa skin-color margin-right5 fa-thumbs-o-up"></i><lb:get key="sns_like"/>(<span>0</span>)</a>
{{/if}} --%>
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="groupMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
                <div class="wzb-title-2" style="font-weight: normal;"><a class="wzb-link01" href="" title=""><lb:get key="group_list"/></a> > ${snsGroup.s_grp_title}</div>
                    
				<div role="tabpanel" class="wzb-tab-1 margin-top15">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="active" onclick="changeTab(this,'group_detail')"><a href="#home" aria-controls="home" role="tab" data-toggle="tab"><lb:get key="group_detail"/></a></li>
                        <c:if test="${s_grp_private!=2 and (isGroupMember != null and isGroupMember == true or isNormal != true)}">
                             <li role="presentation" onclick="changeTab(this,'member_list')"><a href="#profile" aria-controls="profile" role="tab" data-toggle="tab"><lb:get key="group_members"/></a></li>
                             <c:if test="${isManager != null and isManager == true or isNormal != true}">
                                 <li role="presentation" onclick="changeTab(this,'approve_list')"><a href="#approval" aria-controls="approval" role="tab" data-toggle="tab"><lb:get key="group_member_approve"/></a></li>
                                 <li role="presentation" onclick="changeTab(this,'group_set')"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab"><lb:get key="group_information_set"/></a></li>
                             </c:if>
                        </c:if>
                        <c:if test="${s_grp_private==2 and (isManager != null and isManager == true or isNormal != true)}">
                             <li role="presentation" onclick="changeTab(this,'group_set')"><a href="#settings" aria-controls="settings" role="tab" data-toggle="tab"><lb:get key="group_information_set"/></a></li>
                        </c:if>
                    </ul>                                          
                                                                                                         
				    <div class="tab-content">      
						<div class="wbtabcont" id="group_detail"> 	   
							<dl class="wzb-list-2 clearfix">
                                  <dd>
                                       <img class="psdpic" src="${snsGroup.card_actual_path}">
                                       <c:if test="${isManager == true or s_grp_private!=2}">
                                            <c:choose>									   
                                                <c:when test="${isNormal != true or isManager == true}">
                                                    <span class="pjia"><a class="btn wzb-btn-orange" href="javascript:;" onclick="signOutGroup('group_confirm_dissolve')"><lb:get key="group_dissolve"/></a></span>
                                                </c:when>
                                                <c:when test="${snsGroup.s_gpm.s_gpm_status == 0}">
                                                    <span class="pjia"><a class="btn wzb-btn-orange" href="javascript:;" onclick="signOutGroup('group_confirm_cancel_app')"><lb:get key="group_cancel_app"/></a></span>
                                                </c:when>
                                                <c:when test="${snsGroup.s_gpm.s_gpm_status == 3 or snsGroup.s_gpm.s_gpm_status == null}">
                                                    <span class="pjia"><a class="btn wzb-btn-orange" href="javascript:;" onclick="applyJoinGroup()"><lb:get key="group_join"/></a></span>
                                                </c:when>
                                                <c:when test="${snsGroup.s_gpm.s_gpm_status == 1}">
                                                   <span class="pjia"><a class="btn wzb-btn-blue" onclick="signOutGroup('group_confirm_sign_out')"><lb:get key='group_sign_out'/></a></span>
                                                </c:when>
                                            </c:choose>
                                        </c:if>
                                  </dd>
                                  <dt>
                                       <p><span class="color-gray999"><lb:get key="group_create_time"/>：</span><fmt:formatDate value="${snsGroup.s_grp_create_datetime}" pattern="yyyy-MM-dd"/></p>
                                       <p><span class="color-gray999"><lb:get key="group_manager"/>：</span>${snsGroup.user.usr_display_bil}</p>
                                       <p>
								            <span class="color-gray999"><lb:get key="group_desc"/>：</span>
								            <c:choose>
								               <c:when test="${empty snsGroup.s_grp_desc}">
								                   --
								               </c:when>
								               <c:otherwise>
								                  <span id="grp_desc" data="${snsGroup.s_grp_desc}"></span>
								                   <a class="wzb-show skin-color open_desc" style="" >
														<span><lb:get key="click_down"/></span>
														<i class="fa fa-angle-down"></i>
												 	</a>
								               </c:otherwise>
								            </c:choose>
                                       </p>
                                  </dt>
                            </dl> <!-- wzb-list-2 End-->
				             
				            <c:if test="${s_grp_private==2 or snsGroup.s_gpm.s_gpm_status == 1 or isNormal != true}">
                                <div class="wzb-title-2" style="font-weight:normal;"><lb:get key="group_say_words"/></div>
<script>
	$(function(){
		//进来或者刷新首先清空以前遗留的数据
		$.getJSON("${ctx}/app/upload/delete/Group",function(result){
		});
			
		//文件框
		$("#uploadImg").qtip({
		    id : 'uploadImgBoxQitp',
		    content: {
		        text: $("#uploadImgBox")
		    },
		    position: {
		        at: 'center left',
		        my: 'left center',
		        adjust: {
		            x: 40
		        }
		    },
		    show: {  
		        event: 'click'
		    },
		    hide: '',
		    style: {
		        classes: 'qtip-bootstrap',
		            width: 353
		    },
		    events: {
		        show: function(event, api) {
		    	    $('#file_upload').uploadify({
		    		    'uploader' : '${ctx}/app/upload/Group/Img;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
		    	        'swf'  : '${ctx}/static/js/jquery.uploadify/uploadify.swf?var='+(new Date()).getTime(),
		    	        'cancelImg' : '${ctx}/static/js/jquery.uploadify/uploadify-cancel.png',
		    	        'buttonText': fetchLabel('global_select_file'),
		    	        'fileObjName' : 'file',
						//'queueID' : 'file_upload',//'uploadList',
		    	        'multi'     : false,
		    	        'auto'      : true,
		     	        'fileTypeExts'   : '*.jpg;*.gif;*.png;*.JPG',
		    	        'fileTypeDesc'  : 'Image files',
		    	        //'fileSizeLimit' : '50MB', 
		    			'formData'  : {'jsessionid':'${pageContext.session.id}','uploadType':'ajax'},
		    			'onUploadStart':function(file){
		    				$("#header-overlay-pc").unbind("click");
		    	            if(file.size > 50*1024*1024){ //定义允许文件的大小为50M
		    	            	$("#qtip-uploadImgBoxQitp").hide();
		    	            	$("#header-overlay-pc").hide();
		    	            	Dialog.alert(fetchLabel("upload_tip_image"));
		    		            $("#file_upload").uploadify('stop');
		    		            $("#file_upload").uploadify('cancel', '*');
		    	            }
		    			},
		    			'onUploadComplete' : function() {  
		                    $("#file_upload").uploadify("destroy");
		                    $("#qtip-uploadImgBoxQitp").hide();
		                    $("#header-overlay-pc").hide();
		                },
		    	        'onUploadSuccess': function(file, data, response) {
		    	        	data = eval('(' + data + ')');
							if(data.status == 'fail'){
								Dialog.alert(data.errorMsg);
							}else{
			    	        	$('#header-overlay-pc').click(function(){
			      			    	$("#qtip-uploadVideoBoxQitp").hide();
			      			    	$("#qtip-uploadDocBoxQitp").hide();
			      			    	$("#qtip-uploadImgBoxQitp").hide();
			      			    	$('#header-overlay-pc').hide();
			      			    })
			    		        //var dt =  eval('(' + data + ')');
			    				//var val = dt.tmf;
			                	//loadFile(val,dt.path);    
			    	        	initFile(); 
							}
		    	        }           
		    	    });

		        }
/* 	        ,
		        blur:function(){
					
			    } */
		    }
		});

		$("#uploadDoc").qtip({
		    id : 'uploadDocBoxQitp',
		    content: {
		        text: $("#uploadDocBox")
		    },
		    position: {
		        at: 'center left',
		        my: 'left center',
		        adjust: {
		            x: 40
		        }
		    },
		    show: 'click',
		    hide: '',
		    style: {
		        classes: 'qtip-bootstrap',
		            width: 353
		    },
		    events: {
		        show: function(event, api) {
		    	    $('#doc_upload').uploadify({
		    		    'uploader' : '${ctx}/app/upload/Group/Doc;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
		    	        'swf'  : '${ctx}/static/js/jquery.uploadify/uploadify.swf?var='+(new Date()).getTime(),
		    	        'cancelImg' : '${ctx}/static/js/jquery.uploadify/uploadify-cancel.png',
		    	        'buttonText': fetchLabel('global_select_file'),
		    	        'fileObjName' : 'file',
		    	        'multi'     : false,
		    	        'auto'      : true,
		    	        'fileTypeExts'   : '*.doc; *.docx; *.xls; *.xlsx; *.ppt; *.pdt; *.pdf',
						//'fileExt'   : '*.jpg;*.gif;*.png;*.JPG',
						//'fileDesc'  : 'Image Files',
						//'fileSizeLimit' : '50MB',
		    			'formData'  : {'jsessionid':'${pageContext.session.id}','uploadType':'ajax'},
		    			'onUploadStart':function(file){
		    				$("#header-overlay-pc").unbind("click");
		    	            if(file.size > 50*1024*1024){ //定义允许文件的大小为50M
		    	            	$("#qtip-uploadDocBoxQitp").hide();
		    	            	$("#header-overlay-pc").hide();
		    	            	Dialog.alert(fetchLabel("upload_tip_doc"));
		    		            $("#doc_upload").uploadify('stop');
		    		            $("#doc_upload").uploadify('cancel', '*');
		    	            }
		    			},
		    			'onUploadComplete' : function() {  
		                    $("#doc_upload").uploadify("destroy");
		    				$("#qtip-uploadDocBoxQitp").hide();
		    				$("#header-overlay-pc").hide();
		                },
		    	        'onUploadSuccess': function(file, data, response) {
							data = eval('(' + data + ')');
							if(data.status == 'fail'){
								Dialog.alert(data.errorMsg);
							}else{
			    	        	$('#header-overlay-pc').click(function(){
			      			    	$("#qtip-uploadVideoBoxQitp").hide();
			      			    	$("#qtip-uploadDocBoxQitp").hide();
			      			    	$("#qtip-uploadImgBoxQitp").hide();
			      			    	$('#header-overlay-pc').hide();
			      			    })
			    		       // var dt =  eval('(' + data + ')');
			    				//var val = dt.tmf;
			                	//loadFile(val,dt.path);    
			    	        	initFile();   
							}
		    	        }           
		    	    });

		        }
		    }
		});

		$("#uploadVideo").qtip({
		    id : 'uploadVideoBoxQitp',
		    content: {
		        text: $("#uploadVideoBox")
		    },
		    position: {
		        at: 'center left',
		        my: 'left center',
		        adjust: {
		            x: 40
		        }
		    },
		    show: 'click',
		    hide: '',
		    style: {
		        classes: 'qtip-bootstrap',
		            width: 353
		    },
		    events: {
		        show: function(event, api) {

		    	    $('#video_upload').uploadify({
		    		    'uploader' : '${ctx}/app/upload/Group/Video;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
		    	        'swf'  : '${ctx}/static/js/jquery.uploadify/uploadify.swf?var='+(new Date()).getTime(),
		    	        'cancelImg' : '${ctx}/static/js/jquery.uploadify/uploadify-cancel.png',
		    	        'buttonText': fetchLabel('global_select_file'),
		    	        'fileObjName' : 'file',
		    	        'multi'     : false,
		    	        'auto'      : true,
		     	        'fileTypeExts'   : '*.mp4',
		    	        'fileTypeDesc'  : 'mp4 files',
		    	        //'fileSizeLimit' : '50MB',
		    			'formData'  : {'jsessionid':'${pageContext.session.id}','uploadType':'ajax'},
		    			'onUploadStart':function(file){
		    				$("#header-overlay-pc").unbind("click");
		    	            if(file.size > 50*1024*1024){ //定义允许文件的大小为50M
		    	            	$("#qtip-uploadVideoBoxQitp").hide();
		    	            	$("#header-overlay-pc").hide();
		    	            	Dialog.alert(fetchLabel("upload_tip_vedio"));
		    		            $("#video_upload").uploadify('stop');
		    		            $("#video_upload").uploadify('cancel', '*');
		    	            }
		    			},
		    			'onUploadComplete' : function() {  
		                    $("#video_upload").uploadify("destroy");
		    				$("#qtip-uploadVideoBoxQitp").hide();
		    				$("#header-overlay-pc").hide();
		                },
		      	        'onUploadSuccess': function(file, data, response) {
		      	        	data = eval('(' + data + ')');
							if(data.status == 'fail'){
								Dialog.alert(data.errorMsg);
							}else{
			      	        	$('#header-overlay-pc').click(function(){
			      			    	$("#qtip-uploadVideoBoxQitp").hide();
			      			    	$("#qtip-uploadDocBoxQitp").hide();
			      			    	$("#qtip-uploadImgBoxQitp").hide();
			      			    	$('#header-overlay-pc').hide();
			      			    })
			    		       // var dt =  eval('(' + data + ')');
			    				//var val = dt.tmf;
			                	//loadFile(val,dt.path);      
			                	initFile();
							}
		    	        }           
		    	    });

		        }
		    }
		});
	
			$("#uploadVideoBox .nav li").live('click',function(){
				$(this).addClass("active").siblings().removeClass("active");
				var index = $(this).index();
				if(index == 1){
	
					$("#videoContent_0").hide();
					$("#videoContent_1").show();
				} else {
	
					$("#videoContent_1").hide();
					$("#videoContent_0").show();
				}
			})
	
			$("#uploadImgBox .nav li").live('click',function(){
				$(this).addClass("active").siblings().removeClass("active");
				var index = $(this).index();
				if(index == 1){
					$("#content_0").hide();
					$("#content_1").show();
				} else {
					$("#content_1").hide();
					$("#content_0").show();
				}
			})
	
			//初始化文件列表
			initFile();
	
			//删除按钮的显示
			$(".imgBox img").live('mouseover',function(){
				$(this).next().find(".file-temp-del").show();
			})
			$(".imgBox img").live('mouseout',function(){
				$(this).next().find(".file-temp-del").hide();
			})
			$(".imgBox i").live('mouseover',function(){
				$(this).show();
			})		
			$(".imgBox i").live('mouseout',function(){
				$(this).hide();
			})
			//删除实现
			$(".file-temp-del").live('click',function(){
				var id = $(this).attr("data");
				var flag = $(this).parent("a").prev().hasClass("sendcont");
				var obj;
				if(flag){
					obj = $(this).closest(".sendinfo");
			    } else {
					obj = $(this).closest("li");
				}
	 			$.ajax({
					url : '${ctx}/app/upload/del/'+ id ,
					success : function(result){
						$(obj).remove();
					}
				})
			});
	
			//评论
			$("#formSubmit").click(function(){
				var note = $("#formText").val();
				if(note == $("#formText").attr('prompt') || note =='' || note== undefined){
					note = '';
					Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
					return;
				} else {
					if(getChars(note) > 400 ){
						Dialog.alert(fetchLabel('validate_max_400')); //'不能超过400个字符'
						return;
					}
				}
				var action = "group";
				sns.doing.add(note, action, module, grp_id, function(result){
			 		if(result.status=='success'){
			 			$(dt).reloadTable({
							url : '${ctx}/app/doing/user/json/' + meId  + '/Group/' + grp_id
						}); //成功刷新
			 			$("#formText").val('');
			 			$("#docBox").empty();
			 			$("#imgBox").empty();
			 		}
				});
			});
	
	
			$("#formCancel").click(function(){
				var formText = $("#formText").val();
				if(formText == null || formText == '' || formText == undefined){
					return;
				}
				var module = "Group";
				var text = fetchLabel('alert_clear_text');//"确认清空吗？";
				Dialog.confirm({text:text, callback: function (answer) {
					if(answer){
			 			$.ajax({
							url : '${ctx}/app/upload/delete/'+ module ,
							dataType : 'json',
							type : 'post',
							async : false,
							success : function(result){
								//$(obj).remove();
								if(result.status == 'success'){
									$(".imgBox ul").empty();
									$("#docBox").empty();
									$("#formText").val('');
								}
							}
						})
					}
					}
				});
			});
		
		 	$.templates({
		 		docTemplate: '<div class="sendinfo"><span class="sendcont"><a href="{{>url}}">{{>name}}</a></span><a class="sendre grayC999" href="javascript:;"><i data="{{>id}}" class="glyphicon f14 grayCdbd glyphicon-remove file-temp-del"></i></a></div>',
				upImgTemplate: '<li style="position:relative"><img width="60" style="min-height:10px" src="{{>url}}"/><a href="javascript:;" style="position: absolute;bottom: -14px;right: -5px;"><i data="{{>id}}" class="glyphicon f14 grayCdbd glyphicon-remove file-temp-del" style="color:red; display:none;"></i></a></li>'
		 	});
		    $("#uploadVideoBtn").live('click',function(){
				var url = $("#uploadVideoUrl").val();
				if(url == undefined || url == ''){
					Dialog.alert(fetchLabel("upload_tips_online"));
					$("#qtip-uploadVideoBoxQitp").hide();
					$("#header-overlay-pc").hide();
					return ; //alert("不能为空");
				}
				$.ajax({
					url : '${ctx}/app/upload/Group/Video/online',
					async : false,
					type : 'post',
					dataType : 'json',
					data : {
						url : url
					},
					success : function(result){
						$("#uploadVideoUrl").val('');
						$("#qtip-uploadVideoBoxQitp").hide();
						$("#header-overlay-pc").hide();
						loadFile(result.tmf,result.path);
					}
				})
			});
	
	
		    $("#uploadImgBtn").live('click',function(){
				var url = $("#uploadImgUrl").val();
				if(url == undefined || url == ''){
					Dialog.alert(fetchLabel("upload_tips_online"));
					$("#qtip-uploadImgBoxQitp").hide();
					$("#header-overlay-pc").hide();
					return ; //alert("不能为空");
				}
				$.ajax({
					url : '${ctx}/app/upload/Group/Img/online',
					async : false,
					type : 'post',
					dataType : 'json',
					data : {
						url : url
					},
					success : function(result){
						$("#uploadImgUrl").val('');
						$("#qtip-uploadImgBoxQitp").hide();
						$("#header-overlay-pc").hide();
						loadFile(result.tmf,result.path);
					}
				})
				
			});
		    
		    $('#header-overlay-pc').click(function(){
		    	$("#qtip-uploadVideoBoxQitp").hide();
		    	$("#qtip-uploadDocBoxQitp").hide();
		    	$("#qtip-uploadImgBoxQitp").hide();
		    	$('#header-overlay-pc').hide();
		    });
	
	})
	
	function initFile(){
		$("#imgBox").empty();
		$("#docBox").empty();
		$.getJSON("${ctx}/app/upload/Group/noMaster",function(result){
			var mtfs = result.mtfList;
			if(mtfs != undefined){
	            $.each(mtfs,function(i,val){
	            	loadFile(val,result.path);
		        })
			}
		});
	}
	
	function loadFile(val,path){
	    var url,name;
		if(val.mtf_file_type=='url'){
			url = val.mtf_url;
			name = val.mtf_url; 
		} else {
			url =	contextPath+"/"+ path + "/" + val.mtf_file_rename;
			name = val.mtf_file_name;
		}
	    if(val.mtf_type == 'Img'){
	    	$(".imgBox ul").append($.render.upImgTemplate({
	            id : val.mtf_id,
				url : url
			}));
	    } else {
	        $("#docBox").append($.render.docTemplate({
	            id : val.mtf_id,
				url : url,
				name : name
			}));
	    }
	}
	
	function addAll(){
		$("a[name='addGroup']").click();
	}
	
	function clearAll(){
		$("a[name='clearGroup']").click();
	}
	
	
</script>
								<div id="uploadImgBox" style="display: none">
									<div>
										<ul class="nav nav-tabs">
										  <li class="active"><a href="javascript:;"><lb:get key="upload_img"/></a></li>
										  <li><a href="javascript:;"><lb:get key="online_img"/></a></li>
										</ul>
									</div>
									<div class="content">
										<div class="line" style="border:1px solid;"></div>
										<div style="padding: 10px 0 10px 0;" id="content_0">
											<p class="mb10 color-gray666"><lb:get key="upload_tip_image"/></p>
											<input type="file"  name="file_upload" class="pull-left" id="file_upload"/>
										</div>
										<div style="padding: 10px 0 10px 0;display: none;" id="content_1">
											<p class="mb10 color-gray666"><lb:get key="lab_group_online_addr"/></p>
											URL：<input type="text" class="wzb-inputText"  name="url" class="pull-left" id="uploadImgUrl"/>
											<input type="button" id="uploadImgBtn" class="wbtj btn wzb-btn-orange" value='<lb:get key="button_ok"/>' style="height: auto;padding: 3px 8px;margin-bottom:5px;"/>
										</div>
									</div>
								</div>
								
								<div id="uploadDocBox" style="display: none">
									<div>
										<ul class="nav nav-tabs" role="tablist">
										  <li class="active"><a href="javascript:;"><lb:get key="upload_doc"/></a></li>
										</ul>
									</div>
									<div class="content">
										<div class="line" style="border:1px solid;"></div>
										<div style="padding: 10px 0 10px 0;" id="">
											<p class="mb10"><lb:get key="upload_tip_doc"/></p>
											<input type="file"  name="doc_upload" class="fl" id="doc_upload"/>
										</div>
									</div>
								</div>
								
								<div id="uploadVideoBox" style="display: none">
									<div>
										<ul class="nav nav-tabs" role="tablist">
										  <li class="active"><a href="javascript:;"><lb:get key="upload_video"/></a></li>
										  <li><a href="javascript:;"><lb:get key="online_video"/></a></li>
										</ul>
									</div>
									<div class="content">
										<div class="line" style="border:1px solid;"></div>
										<div style="padding: 10px 0 10px 0;" id="videoContent_0">
											<p class="mb10"><lb:get key="upload_tip_vedio"/></p>
											<input type="file"  name="video_upload" class="fl" id="video_upload"/>
										</div>
										<div style="padding: 10px 0 10px 0;display: none;" id="videoContent_1">
											<p class="mb10 color-gray666"><lb:get key="lab_group_online_addr"/></p>
											URL：<input type="text" class="wzb-inputText"  name="url" class="fl" id="uploadVideoUrl"/>
											<input type="button" id="uploadVideoBtn" class="wbtj btn wzb-btn-orange" value='<lb:get key="button_ok"/>' style="height: auto;padding: 3px 8px;margin-bottom:5px;"/>
										</div>
									</div>
								</div>
                                <div class="header-overlay-pc" id="header-overlay-pc" style="display: none;"></div>
                                <div class="wzb-send">
                                     <form action="#" method="post">      
                                         <textarea id="formText" placeholder="<lb:get key="validate_max_400"/>" class="wzb-textarea-04"></textarea>
                                         <span class="color-gray666"><lb:get key="label_cm.label_core_community_management_223"/></span>
                                         <div class="wzb-send-tool clearfix">
                                              <div class="imgBox clearfix">
                                                   <ul id="imgBox">
                                                       <!-- 图片 -->
                                                   </ul>
                                              </div>
                                              <div class="docBox" id="docBox" style="">
                                                    <!-- 文件 -->
                                              </div>
                                         </div>
                                        
                                         <div class="margin-top10 clearfix">
                                              <div class="pull-left">
                                                   <span class="color-gray666"><lb:get key="global_attachment"/>：</span>
                                                   <a id="uploadImg" onclick="$('#header-overlay-pc').show();" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-image-o"></i><lb:get key="doing_image"/></a>
                                                   <a id="uploadDoc" onclick="$('#header-overlay-pc').show();" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-word-o"></i><lb:get key="doing_doc"/></a>
                                                   <a id="uploadVideo" onclick="$('#header-overlay-pc').show();" href="javascript:;" class="wzb-link03"><i class="fa fa-file-video-o"></i><lb:get key="doing_video"/></a>					    
                                              </div>
                                              <div class="pull-right">
                                                   <input class="btn wzb-btn-yellow" id="formCancel" type="button" value='<lb:get key="button_cancel"/>'/>
                                                   <input class="btn wzb-btn-yellow" id="formSubmit" type="button" value='<lb:get key="btn_post"/>'/>
                                              </div> 		
                                        </div>
                                    </form>
                              </div> <!-- send End-->
				              
                              <div class="wzb-title-2" style="font-weight: normal;"><lb:get key="group_speech"/></div>
				              
                              <div id="groupDoingList"></div>
                              
				         </div>
				        </c:if>
        
        				<!-- 群组成员  -->
	        			<c:if test="${s_grp_private!=2 and isGroupMember != null and isGroupMember == true or isNormal != true}">
							<div style="display:none" class="wbtabcont" id="member_list">                                
                                <div class="form-search form-tool margin-bottom25">                 
                                     <input type="text" class="form-control" name="search_my_member" value='<lb:get key="attention_find_desc"/>' onfocus="if(value==fetchLabel('attention_find_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('attention_find_desc')}"/><input type="button" onclick="searchUserList('search_my_member')" class="form-submit" value=""/>
                                      
                                      <c:if test="${isManager == true or isNormal != true}">
                                      <div class="form-tool-right">
                                           <a class="btn wzb-btn-yellow" href="javascript:;" id="addMember"><lb:get key="group_add_member"/></a>
                                           <a class="btn wzb-btn-yellow" href="javascript:;" onclick="clearAll()"><lb:get key="subordinate_clear_all"/></a>
                                      </div>
                                      </c:if>
                                </div>
                                
					            <div class="wzb-find clearfix" id="my_member"></div>
                                    
					            <div id="msgBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
									<div class="modal-dialog" style="width:730px;margin:-360px 0 0 -382px;">
										<div class="modal-content cont">
				                    		<div class="modal-header">
				                          		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="refreshMyMember('')">&times;</button>
				                          		<h4 class="modal-title" id="myModalLabel"><div class="pfindtit"><lb:get key="personal_find"/></div></h4>
				                        	</div>
                                            
                                            <div class="wzb-model-3" style="height:559px;overflow-y:auto;">
                                                <div class="modal-body" style="padding-left:0px;">
                                                    <div class="form-search form-tool">                 
                                                         <input type="text" name="search_add_member" class="form-control" value='<lb:get key="attention_find_desc"/>' onfocus="if(value==fetchLabel('attention_find_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('attention_find_desc')}"/><input type="button" class="form-submit" value="" onclick="searchUserList('search_add_member')"/>
                                                    </div>
                                                </div>
                                                
                                                <div class="form-tool wzb-title-2" style="margin:0 0 20px 0;">                 
                                                      <lb:get key="subordinate_filter_result"/>
                                                      
                                                      <div class="form-tool-right">
                                                           <input type="button" class="btn wzb-btn-yellow" value='<lb:get key="button_add_all"/>' onclick="addAll()"/>
                                                           
                                                           <c:if test="${snsGroup.s_grp_private != '3' }">
                                                           <wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1">
                                                           <input type="button" value='<lb:get key="group_instr_only"/>' class="btn wzb-btn-yellow margin-left15" id="instrOnly"/>
                                                           </wb:hasRole>
                                                           </c:if>
                                                      </div>
                                                  </div>
                                                
                                                <div class="wzb-find clearfix" id="add_member"></div>
                                            </div>
				                      		<div class="modal-footer">
												<button type="button" class="btn wzb-btn-blue wzb-btn-big" data-dismiss="modal" onclick="refreshMyMember('')"><lb:get key="button_close"/></button>
											</div>
				                      	</div>
									</div>
								</div>
					        </div>
				        </c:if>
				        
				        <!-- 成员审批  -->
					        <div style="display:none" class="wbtabcont" id="approve_list">
                                 <div class="qztab">
					                <div class="qztabinfo">
					                     <dl class="qztabnav">
					                         <dd class="cur" onclick="changeApproveTab(this,'pending')"><lb:get key="group_approve_pending"/></dd>
					                         <dd class="qzline" onclick="changeApproveTab(this,'admitted')"><lb:get key="group_approve_admitted"/></dd>
					                         <dd class="qzline" onclick="changeApproveTab(this,'rejected')"><lb:get key="group_approve_rejected"/></dd>
					                     </dl>
					                </div>
					                        
					                <div class="qzbox pfind">
					                    <div class="qztabcont wbtable"></div>
					                    <%-- <div class="">
					                    	<input name="pertxt" class="wbtj mr20 fontfamily swop_bg" type="text" value='' />
					                    	<input name="pertxt" class="wbtj fontfamily swop_bg" type="text" />
					                    </div> --%>
					                    
					                    <div class="wzb-bar qzcont">
                             				<input type="button" onclick="updateGpmStatus(1)" class="btn wzb-btn-orange wzb-btn-big margin-right15" value="<lb:get key="button_approval"/>" name="frmSubmitBtn">
                             				<input type="button" onclick="updateGpmStatus(3)" class="btn wzb-btn-orange wzb-btn-big" value="<lb:get key="button_refuse"/>" name="frmSubmitBtn">
                        				</div>
					                    
					                </div>
					            </div> <!-- qztab end --> 
					        </div>
				  
				        
				        <!-- 信息设置  -->
				    
				        <div class="wbtabcont" style="display:none" id="group_set">
					             <div class="psdmod">
									<form action="${ctx}/app/group/groupDetail/update/snsGroup" method="post" enctype="multipart/form-data" id="groupForm">
                                         <input name="s_grp_id" type="hidden" value="${snsGroup.s_grp_id}">
                                         <table>
                                                <tr>
                                                     <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="group_name"/>：</td>
                                                     
                                                     <td class="wzb-form-control">
                                                         <div class="wzb-selector"><input type="text" class="form-control"  name="s_grp_title"/></div>
                                                     </td>
                                                </tr>
                                                
                                                <tr>
                                                     <td class="wzb-form-label" valign="top"><lb:get key="group_desc"/>：</td>
                                                     
                                                     <td class="wzb-form-control">
                                                         <div class="wzb-selector"><textarea id="kbi_desc" name="s_grp_desc" class="form-control">${snsGroup.s_grp_desc}</textarea></div>
                                                     </td>
                                                </tr>
                                                
                                                <tr>
                                                     <td class="wzb-form-label" valign="top"><lb:get key="group_card"/>：</td>
                                                     
                                                     <td class="wzb-form-control">
                                                         <dl class="wzb-list-8">
                                                             <dd>
                                                                 <img src="${ctx}/static/images/qunzu.jpg" id="group_image">
                                                                 <p><label for="psda"><input type="radio" id="psda" onclick="changeGroupCard(this,'')" checked="checked" value="0" name="group_card"><lb:get key="usr_keep_head"/></label></p>
                                                                 <p><label for="psdb"><input type="radio" id="psdb" onclick="changeGroupCard(this,'')" value="1" name="group_card"><lb:get key="usr_default_head"/></label></p>
                                                                 <p><label for="psdc"><input type="radio" id="psdc" onclick="changeGroupCard(this,'')" value="2" name="group_card"><lb:get key="usr_upload_head"/></label></p>
                                                                 <div class="file" onclick="document.getElementById('psdc').checked=true;">
                                                                      <input id="file" class="file_file" name="image" type="file"  onchange="$('#textfield').val(this.value);$('#textfield').attr('title',this.value);"/>
                                                                      <input id="textfield" class="file_txt" value='<lb:get key="no_select_file"/>' />
                                                                     <div class="file_button"><lb:get key="usr_browse"/></div>
                                                                 </div>
                                                                 <input type="hidden" name="s_grp_card" value=''>
                                                             </dd> 
                                                         </dl>      
                                                     </td>
                                                </tr>
                                                
                                                <tr>
                                                     <td class="wzb-form-label" valign="top"><lb:get key="group_privacy"/>：</td>
                                                     
                                                     <td class="wzb-form-control">
                                                        <c:if test="${snsGroup.s_grp_private == '2' }">
                                                             <label class="wzb-input-label" for="psdg"><input checked="checked" type="radio" id="psdg" onclick="" value="2" name="s_grp_private"><lb:get key="group_open"/></label>
                                                        </c:if>
                                                        <c:if test="${snsGroup.s_grp_private != '2' }">
	                                                         <label class="wzb-input-label" for="psde"><input type="radio" id="psde" checked="checked" onclick="" value="0" name="s_grp_private"><lb:get key="group_public"/></label>
	                                                         
	                                                         <label class="wzb-input-label" for="psdf"><input type="radio" id="psdf" onclick="" value="1" name="s_grp_private"><lb:get key="group_not_public"/></label>
	                                                         <label class="wzb-input-label" for="psdg"><input type="radio" id="psdg" onclick="" value="2" name="s_grp_private"><lb:get key="group_open"/></label>
	                                                         <wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1" >
	                                                             <label class="wzb-input-label" for="psdh"><input type="radio" id="psdh" class="s_grp_private"  value="3" name="s_grp_private"><lb:get key="label_cm.label_core_community_management_78"/> </label>
	                                                         </wb:hasRole>   
                                                         </c:if>                                                       
                                                         <%-- <label class="wzb-input-label" for="psde"><input type="radio" id="psde" checked="checked" onclick="" value="0" name="s_grp_private"><lb:get key="group_public"/></label>
                                                         
                                                         <label class="wzb-input-label" for="psdf"><input type="radio" id="psdf" onclick="" value="1" name="s_grp_private"><lb:get key="group_not_public"/></label>
                                                         <label class="wzb-input-label" for="psdg"><input type="radio" id="psdg" onclick="" value="2" name="s_grp_private"><lb:get key="group_open"/></label>
                                                         <wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1" >
                                                             <label class="wzb-input-label" for="psdh"><input type="radio" id="psdh" class="s_grp_private"  value="3" name="s_grp_private">仅讲师可加入</label>
                                                         </wb:hasRole>  --%>
                                                     </td>
                                                </tr>
                                                  <tr>
											         <td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span></td>
											         <td class="wzb-form-control" >
											             	<!-- 为必填 --><lb:get key="label_rm.label_core_requirements_management_35" />
											         </td>
											    </tr>
                                            </table>
                                            
                                            <div class="wzb-bar">
                                                 <input type="button"  onclick="updateSnsGroup()" class="btn wzb-btn-orange wzb-btn-big margin-right15" value='<lb:get key="button_ok"/>' name="frmSubmitBtn">
                                                 <%-- 	<input type="button" class="btn wzb-btn-orange wzb-btn-big" name="pertxt" value='<lb:get key="button_cancel"/>'/> --%>
                                            </div>
                                    </form> 
								</div>
					        </div>
			
				        
				    </div>
				</div> <!-- wbtab end -->

			</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->

</body>
</html>
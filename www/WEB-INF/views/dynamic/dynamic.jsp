<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>wizbank学习 考试_主页</title>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript">
var dt;
var targetId;
var appTkhID;
var sns = new Sns();
var meId = '${prof.usr_ent_id}';
var dttype = 'all';
$(function(){
	$("#presonlist a").click(function(){
		$(this).addClass("cur").parent().siblings().find("a").removeClass("cur");	
		dttype = $(this).attr("data");
		if(dttype == 'mine'){
			url = '${ctx}/app/doing/user/json/' + meId + '/Doing/' + meId;
			$(dt).reloadTable({
				url : url
			});
		} else {
			$(dt).reloadTable({
				url : '${ctx}/app/doing/dynamicJson/' + dttype
			});
		}
	});	
	dt = $("#doingList").table({
		url : '${ctx}/app/doing/dynamicJson/all',
		colModel : dtModel,
		rp : 10,
		hideHeader : true,
		usepager : true,
		rowCallback : function(data){
			var replies = data.replies;
			loadSubComment(data.replies, $("#doingList .wzb-trend .wzb-trend-parcel:last"));
			$("#doi_" + data.s_doi_id).next().find("span").html(data.replies?data.replies.length:0);
			
			//targetId = data.s_doi_id;

			$.templates({
				fileTemplate : '<p class="mt10 f14"><a href="{{>url}}" target="_blank">{{>name}}</a></p>',
				imgTemplate : '<p class="mt10 f14"><a href="{{>url}}" target="_blank"><img width="60" src="${ctx}{{>url}}"/></a></p>'
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
						fileHtml = $.render.fileTemplate(p)
					}
					$("#doingList .wzb-trend:last").find(".title").after(fileHtml);
				});
			}

			
			$("#doi_" + data.s_doi_id).like({
				count : data.snsCount?data.snsCount.s_cnt_like_count:0,
				flag : data.is_user_like,
				id : data.s_doi_id,
				module: 'Doing',//data.s_doi_module,
				tkhId : 0
			})
			photoDoing();
			$("#doingList .title:last").css("width",$("#doingList").width()*0.85);
			$("#doingList .replydesc:last").css("width",$("#doingList").width()*0.85);
		},
	});


	var module = 'Doing';

	//点击回复按钮
	$(".margin-right15.grayC666").live('click',function(){
		$(this).parents(".wzb-trend").eq(0).find("form").remove();
		$(this).parents(".wzb-trend").eq(0).find(".wzb-trend-parcel").append($("#commentFormTemplate").render());
		var text = "";
		if($(this).attr("uname")){;
			 text = fetchLabel('detail_comment_back') + $(this).attr("uname") + ":"; //"回复"
		} else {
			 text = fetchLabel('doing_publish_comment');
		}
		$("textarea.wzb-textarea-01").prompt(text);
		$("textarea.wzb-textarea-01").prompt(text).attr("uid", $(this).attr("uid")).attr('did',$(this).attr("did"));;
	})
	
	//评论回复功能
	$(".review").live('click',function(){
		var parcel = $(this).parents(".wzb-trend").eq(0);
		$(parcel).find(".wzb-trend-parcel").show();
		var text = "";
		if($(this).attr("uname")){;
			 text = fetchLabel('detail_comment_back') + $(this).attr("uname") + ":"; //"回复"
		} else {
			 text = fetchLabel('doing_publish_comment');
		}
		if(text == $(parcel).find("form").find("textarea").val()){
			$(parcel).find("form").remove();
			$(parcel).find(".wzb-trend-parcel").hide();
		} else {
			$(parcel).find("form").remove();
			$(parcel).find(".wzb-trend-parcel").append($("#commentFormTemplate").render());
			$("textarea.wzb-textarea-01").prompt(text).attr("uid", $(this).attr("uid"))
			.attr('did',$(this).attr("did"));;
		}
	})
	//删除
	$(".delete").live('click',function(){
			//区分是不是删除二级的
			var reply = $(this);
			var parcel = $(this).closest(".wzb-trend");
			sns.comment.del($(this).attr("data"), function(result){
				if(result && result.status == 'success'){
					if($(reply).hasClass("desc")){
						$(parcel).find(".wzb-trend-parcel").empty();
						loadSubComment(result.replies, $(parcel).find(".wzb-trend-parcel"));
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
			$(dt).reloadTable({
				url : '${ctx}/app/doing/dynamicJson/' + dttype
			});
		});
	});

	

	//评论
	$("#courseCommentSubmit").click(function(){
		var note = $("#courseComment").val();
		if(note == $("#courseComment").attr('prompt')){
			note = '';
			Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
			return;
		} else {
			if(getChars(note) > 200 ){
				Dialog.alert(fetchLabel('validate_max_200')); //'不能超过200个字符'
			}
		}
		targetId = meId;
		sns.comment.add(targetId, 0, 0, appTkhID?appTkhID:0, module, note, function(result){
			if(result.status=='success') {
				$(dt).reloadTable({
					url : '${ctx}/app/doing/dynamicJson/' + dttype
				});
			}
			//成功刷新
		});
	});

	//评论回复
	$(".replybtn").live('click', function(){
		var replyToId = $(this).closest(".wzb-trend").attr("did");
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
			if(getChars(note) > 200 ){
				Dialog.alert(fetchLabel('validate_max_200')); //'不能超过200个字符'
			}
		}
		//新增
 		sns.comment.add(replyToId, replyId?replyId:replyToId, toUserId,  appTkhID?appTkhID:0, module, note, function(result){
			if(result.replies){
				var queen = $(textarea).closest(".wzb-trend-parcel");
				$(queen).empty();
				loadSubComment(result.replies,$(queen));

				//数量增加
				$(queen).closest(".wzb-trend").find(".review span").html(result.replies?result.replies.length:0);
			
			}
		}); 
	});
})

	//递归获取二级以下的评论
	function loadSubComment(replies, obj){
		if(replies && replies.length){
			$.each(replies,function(i,val){
				$.extend(val,{isNormal : true})
				$(obj).append($("#commentDescTemplate").render(val));
 				$("#art_like_count_" + val.s_cmt_id).like({
					count : val.snsCount ? val.snsCount.s_cnt_like_count:0,
					flag : val.is_user_like,
					id : val.s_cmt_id,
					module: 'Doing',
					tkhId : 0,
					isComment : 1
				})
				if(val.replies != undefined && val.replies.length>0){
					loadSubComment(val.replies,$(obj).find(".wzb-reply:last"));
				}
 				$(obj).find(".wbdu").css("width",$("#doingList").width()*0.80);
			})
		}
	}
//动态
var dtModel = [ {
	format : function(data) {
		$.extend(data,{isMeInd : data.s_doi_uid == meId,s_doi_create_datetime : data.s_doi_create_datetime.substring(0,16),
			s_doi_title : data.s_doi_title?data.s_doi_title.replace(/"/g,""):''})
		return $('#doingTemplate').render(data);
	}
} ];
</script>

<!-- 动态模板 -->
<jsp:include page="../common/doingTemplate.jsp"></jsp:include>

</head>
<body>

<div class="xyd-wrapper">
<div id="main" class="xyd-main clearfix">
<div class="xyd-sidebar">
<div class="xyd-user">
<div class="xyd-user-box clearfix">
<div class="wzb-user wzb-user82">
     <a href="${ctx}/app/personal/${regUser.usr_ent_id}"><img class="wzb-pic" src="${ctx}${regUser.usr_photo}" /></a>
     <p class="companyInfo"><lb:get key="know_ta"/></p>
     <div class="cornerTL">&nbsp;</div>
     <div class="cornerTR">&nbsp;</div>
     <div class="cornerBL">&nbsp;</div>
     <div class="cornerBR">&nbsp;</div>
</div>

<div class="xyd-user-content">
     <a href="${ctx }/app/personal/${regUser.usr_ent_id}" class="wzb-link04" title="${regUser.usr_display_bil}">${regUser.usr_display_bil}</a>
     <p>${regUser.usg_display_bil}</p>
     <p><c:if test="${regUser.ugr_display_bil != 'Unspecified'}">${regUser.ugr_display_bil}</c:if></p>
</div>
</div> <!-- xyd-user-box End-->
</div>

<ul class="wzb-list-15" id="presonlist">    
    <li><a class="cur" href="javascript:;" data="all"><lb:get key="doing_all"/></a></li>
    <li><a href="javascript:;" data="course"><lb:get key="doing_course"/></a></li>
    <li><a href="javascript:;" data="group"><lb:get key="doing_group"/></a></li>
    <li><a href="javascript:;" data="answer"><lb:get key="doing_answer"/></a></li>   
    <li><a href="javascript:;" data="mine"><lb:get key="doing_me"/></a></li>   
</ul>
</div> <!-- xyd-sidebar End-->

<div class="xyd-article">
     <div class="wzb-title-2"><lb:get key="doing_list"/></div>
    
	 <div id="doingList"></div>
</div> <!-- xyd-article End-->

</div>
</div> <!-- xyd-wrapper End-->


</body>
</html> 
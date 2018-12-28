<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

<!-- 评论 -->
<script id="commentTemplate" type="text/x-jsrender">
<div class="wzb-trend clearfix" did="{{>s_cmt_id}}">
     <div class="wzb-user wzb-user68">
          {{if manage != true}} <a href="${ctx }/app/personal/{{>user.usr_ent_id}}"> {{/if}}<img class="wzb-pic" src="${ctx}{{>user.usr_photo}}" alt="了解Ta">{{if manage != true}}</a>{{/if}}
          
        {{if manage != true}}
		  <p class="companyInfo" style="display: none;"><lb:get key="label_cm.label_core_community_management_29"/></p>
          <div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
          <div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
          <div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
          <div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
		{{/if}}
      </div>
              
          
      <div class="wzb-trend-content">
            <div class="color-gray999">
                  <span class="pull-right" style="margin-right: 50px">
                       <em class="margin-right15" style="font-style:normal;">{{>s_cmt_create_datetime}}</em>
                       <a class="wzb-link03 margin-right15 like" href="javascript:;">
							<i class="fa skin-color fa-thumbs-o-up"></i>
							<span>0</span>
					   </a>
                       <a uname="{{>user.usr_display_bil}}"  uid="{{>s_cmt_uid}}" did="{{>s_cmt_id}}" class="wzb-link03 margin-right15 review" href="javascript:;"><i class="fa skin-color fa-comment"></i><span>0</span></a>
						
					   {{if commonManage == true}}
                          <a class="wzb-link03" href="javascript:;"><i data="{{>s_cmt_id}}" class="del fa skin-color fa-times"></i></a>
						
						{{else}}
						  <a class="wzb-link03" href="javascript:;">&nbsp;</a>
					   {{/if}}
                   </span>
                             
                   {{if manage != true}}<a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="">{{/if}}
						{{if user}}
		            		{{>user.usr_display_bil}}
	            		{{/if}}
				   {{if manage != true}}</a>{{/if}} 
             </div>
                       
      </div>


	  <div class="wzb-trend-parcel">
			{{>s_cmt_content}}
		<div class="queen mt15"></div>
      </div>	
	

</div>
</script>
<script id="commentDescTemplate" type="text/x-jsrender">

<div class="wzb-reply clearfix">
      {{if manage != true}}
			<a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-reply-pic" title="">
	{{else}}
		<span class="wzb-reply-pic">
	{{/if}}
			<img src="${ctx}{{>user.usr_photo}}">
	{{if manage != true}}
			</a>
	{{else}}
			</span>
	{{/if}}
                                
     <div class="wzb-reply-content">
          <p class="color-gray999 wbdu">
			{{if user}}
				{{if manage != true}}<a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="">{{>user.usr_display_bil}}</a>
				{{else}}
					{{>user.usr_display_bil}}
				{{/if}}
			{{/if}}

			{{if toUser}}
				<span class="grayC999"><lb:get key="detail_comment_to"/></span>
				{{if manage != true}}<a href="${ctx }/app/personal/{{>toUser.usr_ent_id}}" class="wzb-link04" title="">{{>toUser.usr_display_bil}}</a>
				{{else}}
					{{>user.usr_display_bil}}
				{{/if}}
				:
			{{/if}}
			{{>s_cmt_content}}
		  </p>
          <p>
			 <span class="margin-right15 color-gray999">{{>s_cmt_create_datetime}}</span>
			 <a class="margin-right15 grayC666" uname="{{>user.usr_display_bil}}" uid="{{>s_cmt_uid}}" did="{{>s_cmt_id}}" href="javascript:void(0);">
				 <i class="fa skin-color fa-comment"></i>
			</a>

			{{if commonManage == true}}
				<a class="grayC666" href="javascript:;">
				 	<i class="del fa skin-color fa-times" data="{{>s_cmt_id}}"></i>
			 	</a>

				{{else}}
				<a class="grayC666" href="javascript:;">
			 	</a>
			{{/if}}
		  </p>
     </div>
</div>

</script>
<script id="commentFormTemplate" type="text/x-jsrender">

<form class="wbedit" method="post" action="#">
	<textarea class="wzb-textarea-01 align-bottom margin-right10"></textarea>
	<input class="btn wzb-btn-yellow align-bottom replybtn" type="button" value="<lb:get key='btn_submit'/>">
</form>

</script>


<script>
var commentTable;
var commentTabUrl;
$(function(){
    $("#courseComment").prompt(fetchLabel("validate_max_400"));	//最多输入400个字符
    
    var appTkhID = 0;
    if(typeof tkhId != 'undefined'){
    	appTkhID = tkhId;
    }
    
	//点击回复按钮
	$(".margin-right15.grayC666").live('click',function(){
		$(this).parents(".wzb-trend").eq(0).find("form").remove();
		$(this).parents(".wzb-trend").eq(0).find(".wzb-trend-parcel").append($("#commentFormTemplate").render());

		var text = fetchLabel('detail_comment_back') + $(this).attr("uname") + ":"; //"回复"
		$(this).parents(".wzb-trend").eq(0).find("form").find("textarea.wzb-textarea-01").val(text);
		$(this).parents(".wzb-trend").eq(0).find("form").find("textarea.wzb-textarea-01").prompt(text).attr("uid", $(this).attr("uid"))
 		.attr('did',$(this).attr("did"));;
	})
	
	//评论回复功能
	$(".review").live('click',function(){
		var parcel = $(this).parents(".wzb-trend").eq(0);
		$(parcel).find(".wzb-trend-parcel").find(".queen").show();
		var text = fetchLabel('detail_comment_back') + $(this).attr("uname") + ":";	//"回复"
		if(text == $(parcel).find("form").find("textarea").val()){
			$(parcel).find("form").remove();
			$(parcel).find(".wzb-trend-parcel").find(".queen").hide();
		} else {
			$(parcel).find("form").remove();
			$(parcel).find(".wzb-trend-parcel").find(".queen").append($("#commentFormTemplate").render());
			$(parcel).find("form").find("textarea.wzb-textarea-01").val(text);
			$(parcel).find("form").find("textarea.wzb-textarea-01").prompt(text).attr("uid", $(this).attr("uid"))
 			.attr('did',$(this).attr("did"));;
		}
	})
	//删除
	$(".del").live('click',function(){
		//区分是不是删除二级的
		var reply = $(this);
		sns.comment.del($(this).attr("data"),$(reply).closest(".wzb-trend").attr("did"), function(result){
			if(result && result.status == 'success'){
				if($(reply).parent().hasClass("grayC666")){
					var numSpan = $(reply).closest(".wzb-trend").find(".review span");
					$(numSpan).html(result.replies.length);
					//$(reply).closest(".queen").html('');
					loadSubComment(result.replies,$(reply).closest(".queen"));
				} else{
					$(commentTable).reloadTable({
						url : commentTabUrl,
						params : {
							selectType : 1,
						}
					});
				}
			}
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
		sns.comment.add(targetId, 0, 0, appTkhID?appTkhID:0, module, note, function(result){
			if(result.status=='success') {		
				$(commentTable).reloadTable({
					url : commentTabUrl,
					params : {
						selectType : 1,
					}
				});
				$("#courseComment").val('');
			}
		});
	});

	//评论回复
	$(".replybtn").live('click', function(){
		var replyToId = $(this).closest(".wzb-trend").attr("did");
		var textarea = $(this).prev();
		var replyId = $(textarea).attr("did");		//分级
		var toUserId = $(textarea).attr("uid");
		var note = $(textarea).val();
		if(note == $(textarea).attr('prompt') || note == undefined || note == ''){
			note = '';
			Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
			return;
		} else {
			if(getChars(note) > 400 ){
				Dialog.alert(fetchLabel('validate_max_400')); //'不能超过400个字符'
				return;
			}
		}
		///alert(replyId);return;
		//新增
 		sns.comment.add(replyToId, replyId?replyId:replyToId, toUserId,  appTkhID?appTkhID:0, module, note, function(result){
			if(result.replies){
				var queen = $(textarea).closest(".wzb-trend-parcel").find(".queen");
				$(queen).empty();
				loadSubComment(result.replies,$(queen));
				$(textarea).closest(".wzb-trend-parcel").find("form").remove();
				//数量增加
				var numSpan = $(queen).closest(".wzb-trend").find(".review span");
				var count = $(numSpan).html();
				count++;
				$(numSpan).html(count);
			}
		}); 
	});
	
	//展开列表
	$("#commentCount em").click(function(){
		if($(this).find("i").hasClass("fa-angle-down")){
			$(this).find("i").removeClass("fa-angle-down").addClass("fa-angle-up");
			$("#commentCount em span").html(fetchLabel('click_up'));
			$("#comment_lst .wzb-trend-parcel .queen").show();	//展开列表
			
		} else {
			$(this).find("i").removeClass("fa-angle-up").addClass("fa-angle-down");
			$("#commentCount em span").html(fetchLabel('click_down'));
			$("#comment_lst .wzb-trend-parcel .queen").hide();	//展开列表
		}
	});
})

var commentModel = [ {
	format : function(data) {
		return $('#commentTemplate').render(data);
	}
}];



//加载评论列表
function loadComment(module){ 	
	$("#comment_lst_content").empty();
 	if(targetId == undefined && module =='Article') {
 		targetId = $("#article_list li a:eq(0)").attr("id").replace("art","");
	}
    commentTabUrl = contextPath + '/app/comment/' + module + '/commentPageJson/' + targetId;
	commentTable = $("#comment_lst_content").table({
		url : commentTabUrl,
		colModel : commentModel,
		rowCallback : function(val){

			photoDoing();
			if($("#comment_lst_content:hidden").attr("id") == undefined){
				$(".wzb-trend-parcel").css("width",$("#comment_lst_content").width()*0.9);
			} else {
				$(".wzb-trend-parcel").css("width",$("#openBox").width()*0.8);
			}
			//评论数
			$("#comment_lst_content .wzb-trend:last").find(".review span").html(val.replies.length)
			loadSubComment(val.replies, $("#comment_lst_content .wzb-trend:last .wzb-trend-parcel").find(".queen"));

			//alert(val.snsCount.s_cnt_like_count);
			$("#comment_lst_content .wzb-trend:last .like").like({
				count : val.snsCount.s_cnt_like_count,
				flag : val.is_user_like,
				id : val.s_cmt_id,
				module: module,
				isComment : 1,
				tkhId : 0
			});
		},
		rp : globalPageSize,
		showpager : 3,
		hideHeader : true,
		usepager : false,
		loadmore : true,
		onSuccess : function(data){
			var total=data.params.total;
			if(total!=undefined&&total==0)
			{
				$("#wbshow").attr("style","display:none");
			}
			$("#commentCount span").html(fetchLabel('label_core_community_management_173',[data.params.total]));
			$("#itm_comment_count span").html(data.params.total);
			$("#commentCount i").removeClass("fa-angle-down").addClass("fa-angle-up");
			$("#commentCount em span").html(fetchLabel('click_up'));
		}
	});
	
	
}


//递归获取二级以下的评论
function loadSubComment(replies, obj){
	$(obj).html('');
	$.each(replies,function(i,val){
		$(obj).append($("#commentDescTemplate").render(val));
		if(val.replies != undefined && val.replies.length>0){
			loadSubComment(val.replies,$(obj).find(".wzb-reply:last"));
		}
		if($("#comment_lst_content:hidden").attr("id") == undefined){
			$(obj).find(".wbdu").css("width",$("#comment_lst_content").width()*0.80);
		} else {
			$(obj).find(".wbdu").css("width",$("#openBox").width()*0.70);
		}
	})
}
</script>

</html>
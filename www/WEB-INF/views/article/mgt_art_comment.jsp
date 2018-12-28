<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>

<title></title>
<style type="text/css">
	.datatable .datatable-pager {
		width: 227px; 
		margin-left: -17px;
	}
</style>
<script type="text/javascript">
	// 公告
	var dt = null;
	var targetId;
	var sns = new Sns();
   	var module = 'Article';
	$(function(){
		$("div#menu,div#header").remove();
// 		$.ajax({
// 			url : '${ctx}/app/article/articleType',
// 			type : 'POST',
// 			dataType : 'json',
// 			async : false,
// 			success : function(data){
// 				var html = $("#article_type_list").html();
// 				for(var i=0;i<data.article_type_list.length;i++){
// 					html += '<option value="' + data.article_type_list[i].aty_id + '">' + data.article_type_list[i].aty_title + '</option>';
// 				}
// 				$("#article_type_list").html(html);
// 			}
// 		});
		
// 		var aty_id = 0;
// 		if($("#article_type_list").children().length > 0){
// 			aty_id = $("#article_type_list :selected").val();
// 		}
// 		dt = $("#article_list").table({
// 			url : '${ctx}/app/article/pageJson/' + aty_id,
// 			colModel : colModel,
// 			rp : 10,
// 			showpager : 3,
// 			hideHeader : true,
// 			usepager : true,
// 			onSuccess : function(){
				detail("${id}");	//选中第一个
// 			}
// 		});

//  	$(".art").live('click',function(){
//  		targetId = $(this).attr("id").replace("art","");
// 		detail(targetId);
// 	});
	})

	var colModel = [ {
		format : function(data) {
			var p = {
				id : data.art_id,	
				title : data.art_title,
				usr_display_bil : data.user.usr_display_bil,
				time : data.art_create_datetime//Wzb.displayTime(data.msg_begin_date, Wzb.time_format_ymdhm)
			};
			return $('#articlesTemplate').render(p);
		}
	}];


	function detail(id){
// 		if($("#article_list li").length > 0){
// 			if(id == undefined || id== '' || id < 1 ) {
// 				targetId = $("#article_list li a:eq(0)").attr("id").replace("art","");
// 			} else {
				targetId = id;
// 			}
			$.getJSON("${ctx}/app/article/detailJson/" + targetId, function(result){
			 		var data = result.article;
			 		var p = {
			 				art_id : data.art_id,
			 				content: Wzb.htmlDecode(data.art_content),
			 				title : data.art_title,
			 				username : data.user.usr_display_bil,
			 				time : data.art_create_datetime
		// 	 				userIcon : '${ctx}/user/user.png'
			 	 		}
			 	 	var html = $('#articleDetailTemplate').render(p);
			 	 	$("#articleCont").html(Wzb.htmlDecode(html));
				    //评论列表加载
				    if(${sns_enabled} == true){
				     	//$("#comment_lst_content").empty();
						loadComment(module);
				    }
	
					$("#art_like_count_" + data.art_id).like({
						count : result.snsCount ? result.snsCount.s_cnt_like_count:0,
						flag : result.sns ? result.sns.like : false,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					})
					$("#art_collect_count_" + data.art_id).collect({
						count : result.snsCount?result.snsCount.s_cnt_collect_count:0,
						flag : result.sns?result.sns.collect : false,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					})
					if(${sns_enabled} == true){
						$("#art_share_count_" + data.art_id).share({
							count : result.snsCount?result.snsCount.s_cnt_share_count:0,
							flag : result.sns?result.sns.share : false,
							id : data.art_id,
							module: 'Article',
							tkhId : 0
						})	
					} else {
						$("#art_share_count_" + data.art_id).remove();
					}
							
			 })
			 //选中的图标
			 $("#article_list li").removeClass("now");
			 $("#art" + targetId).parent("li").addClass("now");
			 $("#comment_lst").show();
// 		} else {
// 			$("#comment_lst").hide();
// 		}
	}
	
// 	function changeArticleType(){
// 		$("#article_list").html('');
// 		$("#article_list").table({
// 			url : '${ctx}/app/article/pageJson/' + $("#article_type_list :selected").val(),
// 			colModel : colModel,
// 			rp : 10,
// 			showpager : 3,
// 			hideHeader : true,
// 			usepager : true,
// 			onSuccess : function(){
// 				$("#articleCont").html('');
// 				detail();
// 			}
// 		});
// 	}
	
</script>

<!-- template start -->
<script id="articlesTemplate" type="text/x-jquery-tmpl">
<li>
	<a class="wzb-link02 f14 art" href="javascript:;" id="art{{>id}}" title="{{>title}}"><i class="glyphicon glyphicon-volume-up"></i>{{>title}}</a>
	<p>{{>time.substring(0,10)}}</p>
</li>
</script>
<script id="articleDetailTemplate" type="text/x-jquery-tmpl">
<h1 class="messtit fontfamily">{{>title}}</h1>
<div class="messinfo">
	<div class="fr">
	</div>
	<span class="f14">{{>time}}</span>
</div>
		
<div class="messcont">
	<p>{{>content}}</p>
</div>
</script>
<!-- template end -->
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
	
		<div class="col-md-12 padlr0">
			<div class="cont">	
				<ul id="article_list"></ul>
				<div id="articleCont"></div>
				
				<c:if test="${sns_enabled == true}"> 
					<div class="messview" id="comment_lst">
						<form method="post" action="#">
						       <textarea class="wbpl showhide" id="courseComment"></textarea>
						       <input type="button" class="wbtj fontfamily alignbtn" id="courseCommentSubmit" value='<lb:get key="btn_submit"/>'/>
						</form>
						 
			             <div class="wbxin fontfamily" id="commentCount">
			               	<span></span>
<%-- 			               	<em id="wbshow" class="state skin-color"><span><lb:get key="know_open"/></span><i class="fa mL3 fa-angle-down"></i> --%>
<!-- 			               	</em> -->
			             </div>
			 
			            <jsp:include page="../common/comment.jsp"/>
	<!-- 					评论列表加载入口 line 88 -->
						<div id="comment_lst_content"></div>
	
					</div> <!-- messview End-->
					
					
				</c:if>
			</div> <!-- cont End-->
		</div>
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>
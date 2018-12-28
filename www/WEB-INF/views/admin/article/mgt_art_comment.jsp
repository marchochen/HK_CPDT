<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx }/static/js/jquery.uploadify/uploadify.css"/>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>

<title></title>

<!-- template start -->
<script id="articlesTemplate" type="text/x-jquery-tmpl">
<li>
	<a class="wzb-link02 f14 art" href="javascript:;" id="art{{>id}}" title="{{>title}}"><i class="glyphicon glyphicon-volume-up"></i>{{>title}}</a>
	<p>{{>time.substring(0,10)}}</p>
</li>
</script>

<script id="articleDetailTemplate" type="text/x-jquery-tmpl">
<div class="wzb-hold-box clearfix">
     <div class="wzb-hold-label">
          <img src="${ctx}{{>icon}}" alt="direct" />
     </div>

     <div class="wzb-hold-content">
          <h2 class="wzb-hold-title">{{>title}}</h2>

          <div class="wzb-article-tool">
               <%--<a id="art_like_count_{{>art_id}}" class="wzb-link03" href="javascript:;" title=""><i class="fa skin-color fa-thumbs-o-up"></i><lb:get key="label_cm.label_core_community_management_34"/>(<span>0</span>)</a>--%>
               <%--<a id="art_collect_count_{{>art_id}}" class="wzb-link03" href="javascript:;" title=""><i class="fa skin-color fa-star"></i><lb:get key="sns_collect"/>(<span>0</span>)</a>--%>
               <em class="wzb-link03" title='<lb:get key="course_publish_time"/>'><i class="fa skin-color fa-clock-o"></i>{{>time}}</em>
         </div>
     </div>
</div>

<div class="messcont">
	<p><div id="content" style="display:none;">{{>content}}</div></p>
</div>
</script>

<script type="text/javascript">
function photoDoing(){
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
		if($(this).find('a').attr('href') != undefined){
			window.location.href = $(this).find('a').attr('href');
		}
	});
}

</script>
<!-- template end -->
</head>
<body>
	<div class="wzb-banner wzb-banner-bg01" style="opacity: 1; background: rgb(204, 0, 153);">
		<i class="fa wzb-banner-icon fa-globe"></i>  <lb:get key="global.FTN_AMD_ARTICLE_MGT"/>
	</div>

	<ol class="breadcrumb wzb-breadcrumb">
		<li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="global.lab_menu_started" /></a></li>
		<li><a href="javascript:wb_utils_nav_go('FTN_AMD_ARTICLE_MAIN', '${loginUser.usr_ent_id }', '${label_lan }')"><lb:get key="lab_article_main"/> </a></li>
		<li class="active"><lb:get key="label_im.label_core_information_management_49"/> </li>
	</ol>
	<!-- wzb-breadcrumb End-->

	<div class="panel wzb-panel">
		<div class="panel-heading" id="article_list"><lb:get key="label_im.label_core_information_management_49"/></div>

		<div class="panel-body">

			<div class="panel-content">
				<span id="articleCont"></span>

			    <%--<c:if test="${sns_enabled == true}">--%>
	                <%--<div class="messview" id="comment_lst">--%>
	                     <%--<form action="#" method="post" class="margin-top10">--%>
	                           <%--<textarea class="wzb-textarea-01 align-bottom margin-right10" id="courseComment"></textarea>--%>
	                           <%--<input type="button" class="btn wzb-btn-yellow wzb-btn-big align-bottom" id="courseCommentSubmit" value='<lb:get key="btn_submit"/>'/>--%>
	                     <%--</form>--%>


	                    <%--<div class="wzb-title-2 margin-top30" id="commentCount">--%>
	                         <%--<span> _评论（共0条）</span>--%>
	                    <%--</div>--%>

	                    <%--<jsp:include page="../../common/comment.jsp"/>--%>
	                    <%--<div id="comment_lst_content"></div>--%>

	                <%--</div> <!-- messview End-->--%>

	            <%--</c:if>--%>

			</div>
		</div>
	</div>
	<!-- wzb-panel End-->

<script id="commentTemplate" type="text/x-jsrender">
 <div class="wzb-trend clearfix" did="{{>s_cmt_id}}">
     <div class="wzb-user wzb-user68">
        {{if s_doi_target_type == '1' && operator}}
            <a {{if isNormal == true}}href="${ctx}/app/personal/{{>operator.usr_ent_id}}"{{/if}}> <img class="wzb-pic" src="{{>operator.usr_photo}}"/></a>
        {{else}}
            <a {{if isNormal == true}}href="${ctx}/app/personal/{{>user.usr_ent_id}}"{{/if}}> <img class="wzb-pic" src="{{>user.usr_photo}}"/></a>
        {{/if}}
        {{if isNormal == true}}
            <p class="companyInfo"><lb:get key="label_cm.label_core_community_management_29"/></p>
            <div class="cornerTL">&nbsp;</div>
            <div class="cornerTR">&nbsp;</div>
            <div class="cornerBL">&nbsp;</div>
            <div class="cornerBR">&nbsp;</div>
        {{/if}}
     </div>

     <div class="wzb-trend-content">
          <div class="color-gray999">
             <span class="pull-right">
                   <em class="margin-right15">2014-07-02 14:06</em>
            {{if s_doi_target_type != '1'}}
                {{if s_doi_act != 'like'}}
                    <a class="wzb-link03 margin-right15 wzb-sns-like" id="doi_{{>s_doi_id}}" href="javascript:;" title=""><i class="fa skin-color mr5 fa-thumbs-o-up"></i><lb:get key="label_cm.label_core_community_management_34"/>(<span>0</span>)</a>
                {{/if}}
                <a class="wzb-link03 margin-right15 review wzb-sns-comment" href="javascript:void(0);"><i class="fa skin-color mr5 fa-comment"></i><lb:get key="sns_comment"/>(<span>{{>replies.length}}</span>)</a>
            {{/if}}
            {{if s_doi_uid == '${prof.usr_ent_id}' || isNormal == false}}
                <a class="wzb-link03 wzb-sns-del-doing" data="{{>s_doi_id}}" href="javascript:;"><i class="glyphicon skin-color mr5 glyphicon-remove"></i><lb:get key="button_del"/></a>
            {{/if}}
             </span>

             <a style="cursor: text" href="javascript:void(0)" class="wzb-link04" title="">{{>user.usr_display_bil}}</a>
          </div>

          <p>{{>s_cmt_content}}</p>
     </div>

     <div class="wzb-trend-parcel">
 		{{for replies }}
			{{include tmpl="#replyTemplate" /}}
    	{{/for}}
     </div>
</div>
</script>
<script id="replyTemplate" type="text/x-jsrender">
<div class="wzb-reply clearfix" >
    <a class="wzb-reply-pic" href="${ctx}/app/personal/{{>user.usr_ent_id}}"> <img src="{{>user.usr_photo}}"/></a>

    <div class="wzb-reply-content">
        <p class="color-gray999">
            {{if user && isNormal}}
                    <a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="" target="">{{>user.usr_display_bil}}</a>
                {{else}}
                    <span class="wzb-link04">{{>user.usr_display_bil}}</span>
            {{/if}}
            {{if toUser}}
                <span class="grayC999"><lb:get key="detail_comment_to"/></span>
                {{if isNormal}}
                    <a href="${ctx }/app/personal/{{>toUser.usr_ent_id}}" class="wzb-link04" title="" target="_blank">{{>toUser.usr_display_bil}}</a>
                {{else}}
                    <span class="wzb-link04">{{>toUser.usr_display_bil}}</span>
                {{/if}}
            {{/if}}
			：{{:s_cmt_content}}
        </p>
         <p>
			<span class="margin-right15 color-gray999">{{>s_cmt_create_datetime}}</span>
			<a class="margin-right15 grayC666" href="javascript:void(0);"><i uname="{{>user.usr_display_bil}}" uid="{{>s_cmt_uid}}" did="{{>s_cmt_id}}" class="fa color-blue00a fa-comment"></i></a>
			{{if s_cmt_uid == '${prof.usr_ent_id}' || !isNormal}}
			<a class="grayC666" href="javascript:;"><i data="{{>s_cmt_id}}" class="fa color-blue00a fa-times"></i></a>
			{{/if}}
		 </p>
    </div>
</div>
</script>
<script id="replyFormTemplate" type="text/x-jsrender">
<form class="wbedit" method="post" action="#">
  <textarea class="wzb-textarea-03 align-bottom margin-right10"></textarea>
  <button class="btn wzb-btn-yellow align-bottom" type="button"><lb:get key="button_reply"/></button>
</form>
</script>
<script id="memberInfoTemplate" type="text/x-jsrender">
<dl class="wzb-list-7 clearfix">
		<dd style="width:80px;">
			<div class="wzb-user wzb-user68">
				<a {{if isNormal == true}}href="{{>href}}"{{/if}} class="mypic"> <img src="{{>image}}"></a>
				{{if isNormal == true}}
					<p class="companyInfo" style="display: none;"><lb:get key="label_cm.label_core_community_management_29"/></p>
				 	<div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
				 	<div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
				{{/if}}
			</div>
		</dd>
       <dt>
			<a class="wzb-link04" href="{{>href}}">
				{{>usr_display_bil}}
     		</a>
	 		<p>{{>usg_display_bil}}</p>
	 		<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
       </dt>
</dl>
</script>
<script id="checkbox-template" type="text/x-jsrender">
     <input type="checkbox" class="qzsel" onclick="{{>event}}" name='{{>name}}' value="{{>val}}"/>
</script>
<script id="text-template" type="text/x-jsrender">
        {{>text}}
</script>
<script id="text-center-template" type="text/x-jsrender">
    <div class="text-center">{{>text}}</div>
</script>

<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.cwn.js"></script>
<script type="text/javascript">
	// 公告
	var dt = null;
	var targetId;
	var sns = new Sns();
   	var module = 'Article';
   	var meId = '${loginUser.usr_ent_id}';
   	var isNormal;
	$(function(){
		detail("${id}");	//选中第一个

	    var commentTabUrl = contextPath + '/app/comment/' + module + '/commentPageJson/' + targetId;
	    $("#commentList").empty().cwnCommnent({
	        editerId : 'commentEditer',
	        commentUrl : commentTabUrl,
	        commentDisplay : true,
	        displayNum : 10,
	        params : {
	            userId : meId,
	            targetId : targetId,
	            module : module,
	            id : '${id}',
	            isComment : 1
	        }
	    });
	});

	var colModel = [ {
		format : function(data) {
			var p = {
				id : data.art_id,
				title : data.art_title,
				usr_display_bil : data.user.usr_display_bil,
				time : data.art_create_datetime
			};
			return $('#articlesTemplate').render(p);
		}
	}];


	function detail(id){
			targetId = id;
			$.getJSON("${ctx}/app/article/detailJson/" + targetId, function(result){
			 		var data = result.article;
			 		var p = {
			 				art_id : data.art_id,
			 				content: Wzb.htmlDecode(data.art_content),
			 				title : data.art_title,
			 				username : data.user.usr_display_bil,
			 				time : data.art_create_datetime,
			 				icon : data.art_icon_file
			 	 		};
			 	 	var html = $('#articleDetailTemplate').render(p);
			 	 	$("#articleCont").html(Wzb.htmlDecode(html));
			 	 	//评论列表加载
				    if(${sns_enabled} == true){
						loadComment(module);
				    }

					$("#art_like_count_" + data.art_id).like({
						count : result.snsCount ? result.snsCount.s_cnt_like_count:0,
						flag : result.sns ? result.sns.like : false,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					});
					$("#art_collect_count_" + data.art_id).collect({
						count : result.snsCount?result.snsCount.s_cnt_collect_count:0,
						flag : result.sns?result.sns.collect : false,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					});
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
					$("#art_create_datetime").html(data.art_create_datetime.substring(0,10))

			 });;;
			 //选中的图标
			 $("#article_list li").removeClass("now");
			 $("#art" + targetId).parent("li").addClass("now");
			 $("#comment_lst").show();
			 
			 
			 // 解决显示编辑器编辑好的字体代码时不受*{}全局影响
			 var time = setInterval(function(){
				 var contentHtml = $('#content').html();
				 //var height = $('#content').height() + 30;
				 if(contentHtml != null && contentHtml != 'undefined' && contentHtml.length > 0){
					 clearInterval(time);
					 $('#content').html('<iframe id="iframe"  src ="" width="100%" scrolling="no" frameborder="0"></iframe>');
					 $("#iframe").contents().find("body").append(contentHtml);
					 $('#content').show();
					 var bodyContentHeight = $('#iframe').contents().find('body').height();
					 $('#iframe').height(bodyContentHeight + 15)
				 }
			 },100);
	}
</script>
</body>
</html>
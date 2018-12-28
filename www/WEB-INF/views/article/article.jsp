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
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>

<title></title>

<script type="text/javascript">
	// 公告
	var dt = null;
	var targetId=${atr_id};
	var sns = new Sns();
   	var module = 'Article';
	$(function(){
		$.getJSON("${ctx}/app/article/detailJson/" + targetId, function(result){
	 		var data = result.article;
	 		$.extend(data,{content: Wzb.htmlDecode(data.art_content)});
	 	 	var html = $('#articleDetailTemplate').render(data);
	 	 	$("#articleCont").html(Wzb.htmlDecode(html));
		    //评论列表加载
		    if(${sns_enabled} == true){
				/* loadComment(module); */
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
					tkhId : 0,
					width : $("#art_share_count_" + data.art_id).width()/2
				})
			} else {
				$("#art_share_count_" + data.art_id).remove();
			}

	 })
	 $("#comment_lst").show();
	});



</script>

<script id="articleDetailTemplate" type="text/x-jquery-tmpl">
<div class="wzb-title-6"><a class="wzb-link01" href="${ctx}/app/article" title='<lb:get key="article_title"/>'><lb:get key="article_title"/></a> &gt; <a class="wzb-link01" href="${ctx}/app/article?aty_id={{>articleType.aty_id}}">{{>articleType.aty_title}}</a></div>

<div class="wzb-hold-box clearfix">
     <div class="wzb-hold-label">
          <img src="${ctx}{{>art_icon_file}}" alt="direct" />
     </div>

     <div class="wzb-hold-content">
          <h2 class="wzb-hold-title">{{>art_title}}</h2>

          <div class="wzb-article-tool">
               <!--<a id="art_like_count_{{>art_id}}" class="wzb-link03" href="javascript:;" title=""><i class="fa skin-color fa-thumbs-o-up"></i><lb:get key="label_cm.label_core_community_management_34"/>(<span>0</span>)</a>-->
               <a id="art_collect_count_{{>art_id}}" class="wzb-link03" href="javascript:;" title=""><i class="fa skin-color fa-star"></i><lb:get key="sns_collect"/>(<span>0</span>)</a>
               <em><i class="fa skin-color fa-clock-o"></i>{{>art_create_datetime.substring(0,10)}}</em>
         </div>
     </div>
</div>

<div class="messcont">
	<p><div id="content" style="display:none;">{{>content}}</div></p>
</div>
</script>
<!-- template end -->
</head>
<body>
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
        <div class="wzb-model-10">
            <div id="articleCont">
            	
            </div>
            
            <%-- <c:if test="${sns_enabled == true}">
            	<div style="border-top: 5px solid #f2f2f2;margin:30px 0 20px 0;"></div>
                <div class="messview" id="comment_lst">
                     <form action="#" method="post" class="margin-top10">
                           <textarea class="wzb-textarea-01 align-bottom margin-right10" id="courseComment"></textarea>
	                           <input type="button" class="btn wzb-btn-yellow wzb-btn-big align-bottom" id="courseCommentSubmit" value='<lb:get key="btn_submit"/>'/>
                     </form>


                    <div class="wzb-title-2 margin-top30" id="commentCount">
                         <span> _评论（共0条）</span>
        <!--                	<em id="wbshow" class="state skin-color"><span>展开_</span></span><i class="fa mL3 fa-angle-down"></i></em> -->
                    </div>

                    <jsp:include page="../common/comment.jsp"/>
<!-- 					评论列表加载入口 line 88 -->
                    <div id="comment_lst_content"></div>

                </div> <!-- messview End-->


            </c:if> --%>
        </div> <!-- cont End-->
	</div>
</div> <!-- xyd-wrapper End-->
<script type="text/javascript">
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
</script>
</body>
</html>
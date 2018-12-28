<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
	<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
	<title></title>
	<script type="text/javascript">
	
		var tndId;
		var article;
		var sns = new Sns();
		var aty_id = ${aty_id ? aty_id : 0};
		var sortorder = 'desc';
		var sortname = "art_create_datetime";
	
		$(function(){
			
			var colModel = [{
				width : '100%',
				format : function(data) {
					$.extend(data,{
						encId : wbEncrytor().cwnEncrypt(data.art_id), 
						article_publish_timestamp : Wzb.displayTime(data.art_create_datetime, Wzb.time_format_ymd)
					});
					return $('#newestTemplate').render(data);
				}
			}];
			
			//拿到最新创建的一条资讯
			$("#newest_article").table({
				url : '${ctx}/app/article/pageJson/0',
				colModel : colModel,
				rp : 1,
				sortname : 'art_create_datetime',
				sortorder : 'desc',
				hideHeader : true,
				usepager : false,
				rowCallback : function(data){
					
					$("#newest_article").parent().show();
					
					$("#art_like").like({
						count : data.snsCount?data.snsCount.s_cnt_like_count:0,
						flag : data.userLike?data.userLike : false,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					});
					$("#art_collect").collect({
						count : data.snsCount?data.snsCount.s_cnt_collect_count:0,
						flag : data.sns?data.sns.collect : false,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					});
					$("#art_share").share({
						count : data.snsCount?data.snsCount.s_cnt_share_count:0,
						flag : data.sns?data.sns.share : false,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					});
				}
			});
			
			//导航模板
			$.templates({
				catalogSingle : '<a class="" style="margin: 0 15px 15px 0;" data="{{>aty_id}}" title="{{>aty_title}}" href="javascript:;">{{>aty_title}}</a>'
			});
		
			//加载分类
			$.ajax({
				url : '${ctx}/app/article/articleType',
				type : 'POST',
				dataType : 'json',
				async : false,
				success : function(data){
					var p = {
						aty_id : '0',
						aty_title : fetchLabel('search_type_all')
					}
					var html = $.render.catalogSingle(p) + Wzb.htmlDecode($.render.catalogSingle(data.article_type_list));
						
					$('#catalog').empty().append(html);
					$('#catalog a').addClass("tnd");
					$("#catalog a[data='" + aty_id + "']").addClass("cur");
				}
			});
			
			//加载文章列表
			article = $('#article_lst').table({
				url : '${ctx}/app/article/pageJson/' + aty_id,
				gridTemplate : function(data){
					$.extend(data,{
						encId : wbEncrytor().cwnEncrypt(data.art_id),
						article_publish_timestamp : Wzb.displayTime(data.art_create_datetime, Wzb.time_format_ymd)
					});
					return $('#articleTemplate').render(data);
				},
				rowCallback : function(data){
					$("#article_lst .like:last").like({
						count : data.snsCount?data.snsCount.s_cnt_like_count:0,
						flag : data.userLike,
						id : data.art_id,
						module: 'Article',
						tkhId : 0
					});
					courseDoing();
				},
				view : 'grid',
				rp : 12,
				sortname : 'art_create_datetime',
				sortorder : 'desc',
				hideHeader : true,
				usepager : true,
				width : '104%'
			});
			
			//点击导航
			$(".tnd").live('click', function(){
				if(!$(this).hasClass("cur")){
					tndId = $(this).attr("data");
					$(this).addClass("cur").siblings().removeClass("cur");
				} else {
					tndId = 0;
				}
				//查询该类型的资讯
				$(article).reloadTable({
					url : '${ctx}/app/article/pageJson/'+tndId,
				});
			});
			
			//排序切换（最新、人气）
			$(".wblearn li").click(function(){
				$(this).addClass("active").siblings().removeClass("active");
				
				var index = $(this).index();
				
				if(index == 0){
					sortname = 'art_create_datetime';
				} else if(index == 1){
					sortname = 's_vtl_log_id_count';
				}
				
				var aty_id = tndId || 0;
				$(article).reloadTable({
					url : '${ctx}/app/article/pageJson/' + aty_id,
					sortname : sortname,
					sortorder : sortorder
				});
			});
			
		});
	
	</script>
	<!-- 资讯列表模板 --> 
	<script id="articleTemplate" type="text/x-jsrender">
		<li>
			<div class="wzb-list-hover">
				<div class="subim">
					 <a class="subxian" href="${ctx}/app/article/detail/{{>encId}}"><img src="${ctx}{{>art_icon_file}}"></a>
					 <a class="subbox"  href="${ctx}/app/article/detail/{{>encId}}" title="">{{>art_title}}</a>
		
					 <div class="subcont">
						  <div class="subarea">
							   <div class="subbar">
									<a class="subinfo" href="${ctx}/app/article/detail/{{>encId}}" title=""><em>{{>art_introduction}}</em></a>
									<div class="subdesc">
<!--<a title="" href="javascript:;" class="pull-left color-gray666 like">
<i class="fa skin-color fa-thumbs-o-up"></i>
 <span>0</span></a> -->
<span class="pull-left"><i class="fa skin-color fa-clock-o"></i>{{>article_publish_timestamp}}</span></div>
							   </div>
							   <div class="subfilter"></div>
						  </div>
					 </div>
				</div>
			</div>
		</li>
	</script>
	<!-- 最新资讯模板 --> 
	<script id="newestTemplate" type="text/x-jsrender">
		<div class="wzb-hold-box clearfix" style=" padding:0 0 0 250px;border:0;">
		     <div class="wzb-hold-label" style="margin-left:-230px;">
		          <a href="${ctx}/app/article/detail/{{>encId}}"><img src="${ctx}{{>art_icon_file}}" alt="direct" /></a>
		     </div>
		
		     <div class="wzb-hold-content">
		          <h2 class="wzb-hold-title wzb-hold-title-2"><a class="wzb-link01" href="${ctx}/app/article/detail/{{>encId}}" title="{{>art_title}}">{{>art_title}}</a></h2>
		
		          <p style="max-width: 690px;">{{>art_introduction}}</p>
		
		          <div class="wzb-article-tool">
		               <!--<a id="art_like" class="wzb-link03" href="javascript:;" title="点赞"><i class="fa skin-color fa-thumbs-o-up"></i><lb:get key="label_cm.label_core_community_management_34"/>(<span>0</span>)</a>--> 
		               <a id="art_collect" class="wzb-link03" href="javascript:;" title="发布"><i class="fa skin-color fa-star"></i><lb:get key="sns_collect"/>(<span>0</span>)</a>
		               <a href="javascript:;" style="color:#666;cursor:default;"><i class="fa skin-color fa-clock-o"></i>{{>article_publish_timestamp}}</a>
		         </div>
		     </div>
		</div>
	</script>

</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			
			<!-- 最新的一条资讯 -->
			<div class="hold-info clearfix" style="display:none;">
				<div id="newest_article" ></div>
				<div class="margin-bottom20"></div>
			</div>
			
			<!-- 导航 -->
			<div class="xyd-hold xyd-hold-green clearfix">
		        <div class="xyd-hold-left">
		             <i class="fa fa-file-text-o"></i>
		             <strong><lb:get key="menu_article"/></strong>
		        </div>
		
		        <div class="xyd-hold-right" style="width:87%;">
		            <dl class="wzb-list-9 clearfix">
		                 <dt><lb:get key="collect_article_type"/>：</dt>
		                 <dd id="catalog"></dd>
		            </dl>
		        </div>
		    </div>
		
			<!-- 内容列表 -->
			<div role="tabpanel" class="wzb-tab-3">
				<ul class="nav nav-tabs wblearn" role="tablist">
					<li role="presentation" class="active"><a href="#zuixin" aria-controls="zuixin" role="tab" data-toggle="tab" title=""><lb:get key="open_the_new"/></a></li>
					<%-- <li role="presentation" id='renqi'><a href="#renqi" aria-controls="renqi" role="tab" data-toggle="tab" title=""><lb:get key="open_popularity"/></a></li> --%>
				</ul>
		
				<div class="tab-content">
					<div class="wbtabcont">
						<ul class="wzb-list-14 wzb-width-14" id="article_lst">
					 	</ul>
					</div>
			    </div>
			</div> 
			
		</div>
	</div>
</body>
</html>
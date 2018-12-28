<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
		<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
		<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
		<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>
		
		<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
		
		<!-- 播放器样式 -->
		<link href="${ctx}/static/js/video/video-js.css" rel="stylesheet" type="text/css" />
		<!-- 播放器js -->
		<script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/video.js"></script>
		<script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/video.cwn.js"></script>

		<title></title>
		<script type="text/javascript">
			var opens;
			var tndId;
			var recordData;
			var prevTime = 0;
			var itmId = window.location.href.replace(/.*\//,"");
			var tkhId = -1;
			var sns = new Sns();
			var targetId = itmId;
			var module = 'Course';
			$(function(){
				$("#note").click(function(){
					 CKobject.getObjectById('ckplayer_picBox').videoPause();
					 getStudyRecord();
					 $("#msgBox").remove();
					$("body").append($("#globleCouBoxTemplate").render(recordData));
				    //弹框消失的事件
					$('#msgBox').on('hidden.bs.modal',function(e){
						 CKobject.getObjectById('ckplayer_picBox').videoPlay();
					})
					$('#msgBox').modal('show');
				});
			
				opens = $("#open_lst").table({
					url : '${ctx}/app/catalog/openJson',
					gridTemplate : function(data){
						$.extend(data,{itm_publish_timestamp : Wzb.displayTime(data.itm_publish_timestamp, Wzb.time_format_ymd)})
						return $('#openTemplate').render(data);
					},
					view : 'grid',
					rp : 16,
					hideHeader : true,
					usepager : true
				});
				$.templates({
					catalogSingle : '<a class="" data="{{>tnd_id}}" title="{{>tnd_title}}" href="javascript:;">{{>tnd_title}}</a>'
				});
				//获取目录
				getCatalog('', true);
				//点击选中目录
				$(".tnd").live('click', function(){
					if(!$(this).hasClass("cur")){
						tndId = $(this).attr("data");
						$(this).addClass("cur").siblings().removeClass("cur");
						if(!$(this).hasClass("second")){
							getCatalog(tndId, false);
						}
					} else {
						tndId = "";
						$(this).removeClass("cur");
						if($(this).hasClass("second")){
							tndId = $("#catalogFirst").find(".cur").attr("data");
						} else {
							$("#catalogSecond").empty();
						}
					}
					$(opens).reloadTable({
						url : '${ctx}/app/catalog/openJson',
						params : {
							tndId : tndId
						}
					});
			
				})
				var detailUrl = "${ctx}/app/course/openDetailJson/" + itmId;
			
				//填充课程数据
				var videoUrl = '';
			
				$.getJSON(detailUrl, function(result) {
					    viewUserId = result.userEntId;
					    viewCourseId = result.item.itm_id;
					    if(result.coscontent && result.coscontent != "")
					    	courseNum = result.coscontent[0].res_id;
			
						var app = result.app;
						var curDate = result.curDate;
						var item = result.item;
			
			 			//定义模板
			 			$.templates({
			 				imgLiTemplate : '<li title="{{>res_title}}"><div><img src="${ctx}/{{if res_img_link}}resource/{{>res_id}}/{{>res_img_link}}{{else}}static/images/a03.jpg{{/if}}" class="{{>res_src_type}}" id="{{>res_id}}"  data="{{>res_src_link}}" alt="{{>res_title}}"/><span class="open-show"><p>{{>res_title}}</p></span></div></li>'
						});
						//课程详情
						item.itm_publish_timestamp = item.itm_publish_timestamp ? Wzb.displayTime(item.itm_publish_timestamp, Wzb.time_format_ymd) : '';
						var itemTypeStr = getCourseTypeStr(item.itm_type);
						$.extend(item,{itemTypeStr: itemTypeStr,sns_enabled:${sns_enabled}})
						//标题
						$("#openTitle").html(item.itm_title);
						
						item.itm_desc = replaceVlaue(item.itm_desc);
						
						$("#detail").html($('#detailTemplate').render(item));
			
						$(".wzb-article-tool").html($("#openAcountTemplate").render(item));
			
						//赞，分享，收藏
						$("#itm_like_count").like({
							count : result.snsCount?result.snsCount.s_cnt_like_count:0,
							flag : result.sns.like,
							id : itmId,
							module: 'Course',
							tkhId : 0
						})
						$("#itm_collect_count").collect({
							count : result.snsCount?result.snsCount.s_cnt_collect_count:0,
							flag : result.sns.collect,
							id : itmId,
							module: 'Course',
							tkhId : 0
						})
						$("#itm_share_count").share({
							count : result.snsCount?result.snsCount.s_cnt_share_count:0,
							flag : result.sns.share,
							id : itmId,
							module: 'Course',
							tkhId : 0
						})
			
						//点击率
						$("#itm_access_count span").html(item.ies_access_count);
						// 加载评论列表
			//loadComment(module);
			
						//所属目录分类
						var itmCatalog = result.itmCatalog;
						var catalogs = [];
						$.each(itmCatalog, function(i,val){
							catalogs.push(val.cat_title);
						})
						$("#itmCatalog").html(catalogs.join(","));
			
						var coscontent = result.coscontent;
						if(coscontent != undefined && coscontent != '') {
			
							var video = coscontent[0];
							if(video && video.res_src_type == 'URL'){
								videoUrl = video.res_src_link;
							} else {
								videoUrl = "<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%>" + contextPath + "/resource/" + video.res_id + "/" + video.res_src_link;
							}
			
							$(".openinfo").append($.render.imgLiTemplate(coscontent));
							$(".openinfo li:eq(0)").addClass("on");
							//公开课_播放页
							(function(){
								function G(s){
									return document.getElementById(s);
								}
			
								function getStyle(obj, attr){
									if(obj.currentStyle){
										return obj.currentStyle[attr];
									}else{
										return getComputedStyle(obj, false)[attr];
									}
								}
			
								function Animate(obj,json){
									if(obj.timer){
										clearInterval(obj.timer);
									}
									obj.timer = setInterval(function(){
										for(var attr in json){
											var iCur = parseInt(getStyle(obj, attr));
											iCur = iCur ? iCur : 0;
											var iSpeed = (json[attr] - iCur) / 5;
											iSpeed = iSpeed > 0 ? Math.ceil(iSpeed) : Math.floor(iSpeed);
											obj.style[attr] = iCur + iSpeed + 'px';
											if(iCur == json[attr]){
												clearInterval(obj.timer);
											}
										}
									}, 30);
								}
			
								var oList = G("xyd-open-info");
								var oPrev = G("xyd-open-prev");
								var oNext = G("xyd-open-next");
			
								var oListLi = oList.getElementsByTagName("li");
								var len2 = oListLi.length;
			
								if(oListLi.length == 1){
									$("#xyd-open-info").hide();
									$("#xyd-open-prev").hide();
									$("#xyd-open-next").hide();
								}
			
								var oListUl = oList.getElementsByTagName("ul")[0];
								var w2 = oListLi[0].offsetWidth;
			
								oListUl.style.width = w2 * len2 + "px";
			
								var index = 0;
								var num = 6;
								var num2 = Math.ceil(num / 2);
			
								function Change(){
								    if(oListLi.length>num){
									    if(index < num2){
										    Animate(oListUl, {left: 0});
									    }else if(index + num2 <= len2){
									    	Animate(oListUl, {left: - (index - num2) * w2});
									    }else{
									    	Animate(oListUl, {left: - (len2 - num) * w2});
									    }
			                        }
									for (var i = 0; i < len2; i++) {
										if(oListLi[i] == undefined) return;
										oListLi[i].className = "";
										if(i == index){
											oListLi[i].className = "on";
											
											//点击左右时播放对应视频
											var url = null;
											if($(oListLi[i]).find("img").hasClass("URL")){
												url = $(oListLi[i]).find("img").attr("data");
											} else {
												url = "<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%>" + contextPath + "/resource/" + $(oListLi[i]).find("img").attr("id") + "/" + $(oListLi[i]).find("img").attr("data");
											}
											courseNum = $(oListLi[i]).find("img").attr("id");
											
											if(coscontent[i].res_src_type.indexOf('ONLINEVIDEO_') != -1) {
												$("#my_video_1").hide();
												if(document.getElementById('api_video') == null) {
													$("#video_div_1").append('<iframe name="api_video" id="api_video" sandbox="allow-scripts allow-same-origin" src="' + coscontent[i].res_src_online_link + '"class="video-js vjs-default-skin vjs-big-play-centered" />');
												} else {
													$("#api_video").show();
													$("#api_video").attr("src", coscontent[i].res_src_online_link);
												}
											} else {
												$("#api_video").hide();
												$("#my_video_1").show();
												$("#video_source").attr("src",url);
												$("#my_video_1_html5_api").attr("src",url);
												videoCwn("my_video_1");
											}
										}
									}
								}
			
								oNext.onclick = function(){
									index ++;
									index = index == len2 ? 0 : index;
									Change();
								}
			
								oPrev.onclick = function(){
									index --;
									index = index == -1 ? len2 -1 : index;
									Change();
								}
			
								for(var i = 0; i < len2; i++){
									oListLi[i].index = i;
									oListLi[i].onclick = function(){
										index = this.index;
										Change();
									}
								}
							})();
							
							//播放视频
							if(video.res_src_type.indexOf('ONLINEVIDEO_') != -1) {
								$("#my_video_1").hide();
								if(document.getElementById('api_video') == null) {
									$("#video_div_1").append('<iframe name="api_video" id="api_video" sandbox="allow-scripts allow-same-origin" src="' + video.res_src_online_link + '"class="video-js vjs-default-skin vjs-big-play-centered" />');
								} else {
									$("#api_video").show();
									$("#api_video").attr("src", video.res_src_online_link);
								}
							} else {
								$("#video_source").attr("src",videoUrl);
								videoCwn("my_video_1");
							}
						}else{
							$("#xyd-open-move").hide();
							$("#losedata").show();
						}
					});
			
					$(".openinfo img").live('click',function(){
						var url = null;
						if($(this).hasClass("URL")){
							url = $(this).attr("data");
						} else {
							url = "<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%>" + contextPath + "/resource/" + $(this).attr("id") + "/" + $(this).attr("data");
						}
						courseNum = $(this).attr("id");
						
						//播放视频
						$("#video_source").attr("src",url);
						videoCwn("my_video_1");
					});
			
					$("#nav2 li").click(function(){
						$(this).addClass("now").siblings().removeClass("now");
						$(".wbtabcont:eq("+$(this).index()+")").show().siblings().hide();
						if($(".wbtabcont:eq("+$(this).index()+")").attr("id") == 'comment_lst'){
							// 加载评论列表
							//loadComment(module);
						}
					});
					
					$("#itm_comment_count").live("click",function(){
						$("#comment_lst_li").click();
					});
			
					//点击评论显示评论列表
					$("#itm_comment_count").live('click', function(){
						$("#nav2 li:last").addClass("now").siblings().removeClass("now");
						$(".wbtabcont:last").show().siblings().hide();
					})
			});
		    function getStudyRecord(){
		    	var data = {userId : viewUserId,courseId : viewCourseId,courseNum : courseNum};
		    	$.ajax({url:"${ctx}/app/course/getCourseRecord",async:false,data : data,success : function(data){
		    		recordData = strToJson(data);
		    	}});
		    }
		    function saveText(){
		    	var text = $("textarea[name=courseNote]").val();
		    	$.post("${ctx}/app/course/noteCourseRecord",{userId : viewUserId,courseId : viewCourseId,courseNum : courseNum,pcrNote : text},function(data){
		
		    	});
		    }

			function getCatalog(tndId, flag){
				//获取目录
				var url = "${ctx}/app/catalog/openJson";
				if(tndId && tndId>0) url += "?tndId=" + tndId;
				$.getJSON(url, function(result) {
					var catalogFirst = result.catalog.first;
					var catalogSecond = result.catalog.second;
					if(flag) {
						$("#catalogFirst").empty().append($.render.catalogSingle(catalogFirst));
						$("#catalogFirst a").addClass("tnd");
					}
					if(catalogSecond){
						$("#catalogSecond").empty().append($.render.catalogSingle(catalogSecond));
						$("#catalogSecond a").addClass("tnd second");
					}
				});
			}
	</script>
	
 	<!-- template start -->
	<script id="openTemplate" type="text/x-jsrender">
		<li>
			 <a class="fwim" href="${ctx}/app/course/detail/{{>itm_id}}">
				  <div class="main_img">
					   <img class="fwpic" src="${ctx}{{>itm_icon}}">
					   <div class="show">
							<span class="imgArea"><em><lb:get key='index_click_to_study'/></em></span>
					   </div>
				 </div>
				 <div class="fwimcont">{{>itm_title}}</div>
				 <div class="fwimbg"></div>
			</a>
		
			<div class="fwdesc">
			     <div class="pull-left clearfix"><a class="color-gray666" href="#" title=""><i class="fa skin-color fa-thumbs-o-up"></i> 103</a></div>
				 <div class="pull-right color-gray666">{{>itm_publish_timestamp}}</div>
			</div>
		</li>
	</script>

	<!-- 课程详细模板 -->
	<script id="detailTemplate" type="text/x-jsrender">
		<table class="wzb-table-three">
			<tr>
				 <td class="wzb-table-title" valign="top"><lb:get key="open_own_kinds"/>：</td>
				 <td class="wzb-table-content" id="itmCatalog"></td>
			</tr>
		
			<tr>
				 <td class="wzb-table-title" valign="top"><lb:get key="course_introduction"/>：</td>
				 <td class="wzb-table-content">
				     {{if itm_desc}}
						 {{:itm_desc}}
					 {{else}}
						 <lb:get key="detail_no_message"/>
					 {{/if}}
				 </td>
			</tr>
		</table>
	</script>

	<script id="openAcountTemplate" type="text/x-jsrender">
<!--  <a class="wzb-link03" href="javascript:;" title='<lb:get key="sns_like"/>' id="itm_like_count"><i class="fa skin-color fa-thumbs-o-up"></i><lb:get key="sns_like"/>(<span>0</span>)</a>-->
		<a class="wzb-link03" href="javascript:;" title='<lb:get key="sns_collect"/>' id="itm_collect_count"><i class="fa skin-color fa-star"></i><lb:get key="sns_collect"/>(<span>0</span>)</a>
<!-- <a class="wzb-link03" href="javascript:;" title='<lb:get key="sns_comment"/>' id="itm_comment_count"><i class="fa skin-color fa-comment"></i><lb:get key="sns_comment"/>(<span>0</span>)</a>-->
		<em style="cursor:text;margin-right:45px;color:#666;" id="itm_access_count"><i class="fa fa-hand-o-up"></i><lb:get key="global_click_rate"/>(<span>0</span>)</em>
		<em class="color-gray666"><i class="fa skin-color fa-clock-o"></i>{{>itm_publish_timestamp}}</em>
	</script>

	<script id="globleCouBoxTemplate"  type="text/x-jquery-tmpl">
		<div id="msgBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" style="width:650px;">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
						<h4 class="modal-title" id="myModalLabel"><lb:get key="detail_note"/></h4>
					</div>
					<div class="modal-body" style="width:600px;overflow-x:auto;">
						<div>
						<table>
							<tr>
								<td height="90"><textarea rows="10" cols="80" name="courseNote">{{>note}}</textarea></td>
							</tr>
							<tr>
								<td></td>
							</tr>
						</table>
					</div>
	           </div>
	           <div class="modal-footer">
		             <button type="button" onClick="saveText();" class="btn btn-default" data-dismiss="modal"><lb:get key="btn_submit"/></button>
	           </div>
	         </div>
	       </div>
		 </div>
	</script>
	 <!-- template end -->
</head>

<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="wzb-model-10">
				<div class="wzb-title-6">
					<a type="button" class="btn wzb-btn-yellow margin-right15" href="${ctx}/app/course/open" title="">
						<lb:get key="open_back_list"/><!-- 返回列表 -->
					</a> 
					<span class="openset" id="openTitle">
						<lb:get key="open_ad_title"/>
					</span>
				</div>

				<div class="xyd-open-move" id ="xyd-open-move">
					<span id="xyd-open-prev"><i class="fa color-gray999 fa-angle-left"></i></span>
					<span id="xyd-open-next"><i class="fa skin-color fa-angle-right"></i></span>
					<div id="" class="xyd-open-box">
						 <div align="center" id="video_div_1">
						    <video id="my_video_1" class="video-js vjs-default-skin vjs-big-play-centered" controls="controls" 
						      autoplay="autoplay" preload="auto" width="100%" height="100%" poster="" oncontextmenu="return false;" >
								<source id="video_source" src="" type='video/mp4' />
						    </video>
				 		</div>
					</div>
					<div id="xyd-open-info" class="xyd-open-info">
						<ul class="openinfo">
				
						</ul>
					</div>
				</div>
				<div id ="losedata" style="margin-top:10px;display:none;" class="losedata">
					<i class="fa fa-folder-open-o"></i>
					<p><lb:get key="label_lose_data"/></p>
				</div>

				<div class="wzb-article-tool">
				<!--       <a class="fl" href="#" title=""><i class="fa skin-color mr5 fa-thumbs-o-up"></i>_赞()</a> -->
				<!--       <a class="fl numr" href="#" title=""><i class="fa skin-color mr5 fa-star"></i>_收藏()</a> -->
				<!--       <a class="fl numr" href="#" title=""><i class="fa skin-color mr5 fa-share-alt"></i>_分享()</a> -->
				<!--       <a class="fl numr" href="#" title=""><i class="fa skin-color mr5 fa-comment"></i>_评论()</a> -->
				<!--       <a class="fl numr" href="#" title=""><i class="fa skin-color mr5 fa-hand-o-up"></i>_点击率()</a> -->
				<!--       <a class="fl numr" href="#" title=""><i class="glyphicon skin-color mr5 glyphicon-time"></i>_发布 <span></span></a> -->
				</div>
			</div>

			<div role="tabpanel" class="wzb-tab-5 margin-top15" id="openBox">
				<ul class="nav nav-tabs" role="tablist" id="nav2">
				    <li role="presentation" class="active"><a href="#detail" aria-controls="detail" role="tab" data-toggle="tab"><lb:get key="detail_course_detail"/></a></li>
    <%-- <c:if test="${sns_enabled}">
				        <li role="presentation"><a href="#comment_lst" aria-controls="comment_lst" id="comment_lst_li" role="tab" data-toggle="tab"><lb:get key="sns_label_comment"/></a></li>
    </c:if> --%>
				</ul>

				<div class="tab-content">
				     <div role="tabpanel" class="tab-pane active" id="detail">
				
				     </div>
     <%-- <div role="tabpanel" class="tab-pane" id="comment_lst">
				          <div class="panel-content">
				                <form action="#" method="post" class="margin-top10">
				                      <textarea class="wzb-textarea-01 align-bottom margin-right10" id="courseComment"></textarea>
				                      <input type="button" class="btn wzb-btn-yellow wzb-btn-big align-bottom" id="courseCommentSubmit" value='<lb:get key="btn_submit"/>'>
				                </form>
				
				                <div class="wzb-title-2 margin-top15" id="commentCount"><span> _评论（共0条）</span></div>
				
				                <div id="comment_lst_content"></div>
				
				                <jsp:include page="../common/comment.jsp"/>
				         </div>
    </div> --%>
				    </div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
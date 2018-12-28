<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/ckplayer/ckplayer.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/ckplayer/js/offlights.js" charset="utf-8"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js" charset="utf-8"></script>

<!-- 播放器样式 -->
<link href="${ctx}/static/js/video/video-js.css" rel="stylesheet" type="text/css" />
<!-- 如果要支持IE8 -->
<%-- <script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/ie8/videojs-ie8.js"></script> --%>
<!-- 播放器js -->
<script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/video.js"></script>
<script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/video.cwn.js"></script>


<style type="text/css" media="screen">
body {
	margin: 0;
	padding: 0;
	overflow: auto;
}
</style>
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
	var targetId=${kbItem.kbi_id};
	var sns = new Sns();
   	var module = 'Knowledge';
	$(function(){
		$.getJSON("${ctx}/app/kb/center/view/detailJson/" + targetId, function(result){
		    //评论列表加载
		   	if(${sns_enabled} == true){
		     	//$("#comment_lst_content").empty();
				//loadComment(module);
		    }

			$("#kbi_like_count").like({
				count : result.snsCount ? result.snsCount.s_cnt_like_count:0,
				flag : result.sns ? result.sns.like : false,
				id : targetId,
				module: 'Knowledge',
				tkhId : 0
			})
			$("#kbi_collect_count").collect({
				count : result.snsCount?result.snsCount.s_cnt_collect_count:0,
				flag : result.sns?result.sns.collect : false,
				id : targetId,
				module: 'Knowledge',
				tkhId : 0
			})
			if(${sns_enabled} == true){
				$("#kbi_share_count").share({
					count : result.snsCount?result.snsCount.s_cnt_share_count:0,
					flag : result.sns?result.sns.share : false,
					id : targetId,
					module: 'Knowledge',
					tkhId : 0,
					width : $("#kbi_share_count").width()/2
				})
			} else {
				$("#kbi_share_count").remove();
			}

			/* $('.fa-share-alt').parent().click(function(){
				$("#shareText"+targetId).val("");
		 		$("#shareText"+targetId).removeAttr(" ")
			}); */
	 })
	 $("#comment_lst").show();
	});
	
	document.oncontextmenu = function() {
		return false;
	}

	document.onkeydown = function(event){
		if(event.keyCode == 123) {
			event.returnValue = false;
		}
	}
</script>
</head>
<body>
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<div class="wzb-model-10">
				<div id="knowledgeCont">
					<div class="wzb-title-6">
						<a class="btn wzb-btn-yellow margin-right10" href="${ctx}/app/kb/${type}/${source}?tab=${tab}" title="<lb:get key='open_back_list'/>">
							<lb:get key='open_back_list' />
						</a>
						${kbItem.kbi_title}
					</div>
					<c:choose>
						<c:when test="${kbItem.kbi_type=='ARTICLE'}">
							<div class="panel-content">
								<p>${kbItem.kbi_content}</p>
							</div>
						</c:when>
						<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
							<div style="background: #222222;width:100%;height:660px;">
								<iframe scrolling="yes" id="ifm" allowtransparency="true" allowfullscreen="true" width="100%" height="650px" frameBorder="0" scrolling="no"></iframe>
							</div>
							<script type="text/javascript">

								sessionStorage.clear();
								var pdfUrl = '${ctx}/attachment/${kbItem.attachments[0].kba_id}/${kbItem.attachments[0].kba_filename}';
								var items = getPdfImgItems(pdfUrl);

								sessionStorage.setItem("photoItems",JSON.stringify(items));
								//$("#ifm").attr("src", "${ctx}/htm/photoSwipe/photeSwiper_em.htm");
								$("#ifm").attr("src", "${ctx}/app/idv/preview?filePath="+pdfUrl);
								
								/**
								 * 获取pdf对应的图片
								 */
								function getPdfImgItems(fileUrl){
									var url = fileUrl+'.js';
									var pages;
									var height;
									var width;
									var items =" [ ";
									$.ajaxSettings.async = false;
									$.getJSON(url, function(data) {
										if(typeof(data)== "string"){
											data =$.parseJSON(data);
										}
										pages = data[0].pages;
										height = data[0].height;
										width = data[0].width;
										for(var i=1;i<=pages;i++){

											var str_json = " {";
											str_json += "src: '../../.." + fileUrl + "_"+i+".jpg',"+"w:"+width+",h:"+height+"}";
											if(i!=pages){
												str_json += ",";
											}
											items += str_json;
										}
										items += "]";
										items = eval("("+items+")");

									 });
									return items;
								}

					        </script>
						</c:when>
						<c:when test="${kbItem.kbi_type=='VEDIO'}">
							<div align="center">
							    <video id="my_video_1" class="video-js vjs-default-skin vjs-big-play-centered" controls="controls" 
							      autoplay="autoplay" preload="auto" width="100%" height="100%"  oncontextmenu="return false;" >
									<c:choose>
										<c:when test="${!empty kbItem.kbi_online && kbItem.kbi_online == 'ONLINE'}">
											<source src="${kbItem.kbi_content}" type='video/mp4' />
									 	</c:when>
									 	<c:otherwise>
									    	<source src="${ctx}${kbItem.attachments[0].kba_url}" type='video/mp4' />
									 	</c:otherwise>
							    	</c:choose>
							    </video>
				  			</div>
				  			
				  			<script type="text/javascript">
					  			viewUserId = '${prof.usr_ent_id }';
					  		    viewCourseId = '${kbItem.kbi_id }';
					  			courseNum = '${kbItem.attachments[0].kba_id}';
					  			
					  			videoCwn("my_video_1");
							</script>
				  			
							<div class="opencont" id="video"></div>
							
						</c:when>
						<c:when test="${kbItem.kbi_type=='IMAGE'}">

                            <div>
								<iframe id="photoSwiper" scrolling="no" frameborder="0" height="650" allowtransparency="true" allowfullscreen="true" name="photoSwiper" title="photoSwiper" style="width: 100%; overflow: hidden;"></iframe>
							</div>


							<script>

								window.onload = function(){
									var items = [];
									var item;
									var temArr;
									var w,h;
									<c:forEach items="${kbItem.attachments}" var="attachment">
										item = {src:"${ctx}${attachment.kba_url}",w:500,h:500};
										temArr = item.src.substring(0,item.src.lastIndexOf(".")).split("_");
										if(temArr && temArr.length==3){//取图片的宽高，数组的后两位
											w = temArr[temArr.length-2];
										    h = temArr[temArr.length-1];
										    item.w = w;
										    item.h = h;
										}

										items.push(item);
									</c:forEach>

									//$("#photoSwiper")[0].contentWindow.openPhotoSwipe(items);
									sessionStorage.setItem("photoItems",JSON.stringify(items));
									$("#photoSwiper")[0].src = "${ctx}/htm/photoSwipe/photeSwiper_em.htm";
								}

							</script>

						</c:when>
					</c:choose>
					<div class="wzb-article-tool margin-bottom25">
						<%-- <a id="kbi_like_count" class="wzb-link03" href="javascript:;" title="">
							<i class="fa skin-color fa-thumbs-o-up"></i>
							<lb:get key="sns_like" />
							(<span>0</span>)
						</a> --%>
						<a id="kbi_collect_count" class="wzb-link03" href="javascript:;" title="">
							<i class="fa skin-color fa-star"></i>
							<lb:get key="sns_collect" />
							(<span>0</span>)
						</a>
						<em style="margin-right:45px;color:#999;">
							<i class="fa  fa-hand-o-up"></i>
							<lb:get key="global_click_rate" />
							(${kbItem.kbi_access_count})
						</em>
						<c:if test="${kbItem.kbi_type =='DOCUMENT' || kbItem.kbi_type =='VEDIO'}">
							<c:choose>
								<c:when test="${kbItem.kbi_download =='ALLOW'}">
									<script type="text/javascript">
										$(function(){
											$('#download').click(function(){
										    	$.ajax({
										    		async : false,
													url : "${ctx}/app/kb/center/download_count/${kbItem.kbi_id}",
													dataType :'json',
													success : function(data) {
														 $('#kbi_download_count').empty();   //清空resText里面的所有内容
														 $('#kbi_download_count').html("("+data.kbi_download_count+")");
													}
												});
										    });
										});
									</script>
									<c:choose>
										<c:when test="${!empty kbItem.kbi_online  && kbItem.kbi_online eq 'ONLINE'}">
											<a id='download' class="wzb-link03" href="${kbItem.kbi_content}" target="_blank" title="<lb:get key="lab_kb_DOWNLOAD"/>">
												<i class="fa skin-color fa-download"></i>
												<lb:get key="lab_kb_DOWNLOAD" />
												<span id="kbi_download_count">(${kbItem.kbi_download_count})<span>
											</a>
										</c:when>
										<c:otherwise>
											<a id='download' class="wzb-link03" href="${ctx}/app/kb/center/download/${kbItem.kbi_id}" target="_blank" title="<lb:get key="lab_kb_DOWNLOAD"/>">
												<i class="fa skin-color fa-download"></i>
												<lb:get key="lab_kb_DOWNLOAD" />
												<span id="kbi_download_count">(${kbItem.kbi_download_count})<span>
											</a>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<span style="margin-right:50px;color:#999;" href="javascrit:;" title='<lb:get key="lab_file_not_download"/>'>
										<i class="fa fa-download"></i>
										<lb:get key="lab_kb_DOWNLOAD" />
										<span id="kbi_download_count">(${kbItem.kbi_download_count})</span>
									</span>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${kbItem.kbi_publish_datetime != null && not empty kbItem.kbi_publish_datetime}">
							<span class="color-gray666"><i class="fa skin-color fa-clock-o"></i>
							      ${fn:substring(kbItem.kbi_publish_datetime, 0, 10)}
							</span>
						</c:if>
					</div>
				</div>
				<%-- <c:if test="${sns_enabled == true}">
					<div class="panel-content" id="comment_lst">
						<form method="post" action="#">
							<textarea class="wzb-textarea-01 align-bottom margin-right10" id="courseComment"></textarea>
							<input type="button" class="btn wzb-btn-yellow wzb-btn-big align-bottom" id="courseCommentSubmit" value='<lb:get key="btn_submit"/>' />
						</form>

						<div class="wzb-title-2 margin-top30" id="commentCount">
							<span>_评论（共0条）</span> 
							
							<em id="wbshow" class="wzb-show skin-color">
							  <span><lb:get key="click_down" />_</span><i class="fa fa-angle-down"></i>
							</em>
						</div>

						<jsp:include page="../common/comment.jsp" />
						<!-- 					评论列表加载入口 line 88 -->
						<div id="comment_lst_content"></div>
					</div>
					<!-- panel-content End-->


				</c:if> --%>

			</div>
		</div>
	</div>
	<!-- xyd-wrapper End-->
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../../common/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx }/static/js/jquery.uploadify/uploadify.css"/>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_km_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/js/wb_usergroup.js"></script>

<!-- 播放器样式 -->
<link href="${ctx}/static/js/video/video-js.css" rel="stylesheet" type="text/css" />
<!-- 如果要支持IE8 -->
<%-- <script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/ie8/videojs-ie8.js"></script> --%>
<!-- 播放器js -->
<script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/video.js"></script>
<script language="Javascript" type="text/javascript" src="${ctx}/static/js/video/video.cwn.js"></script>

<script type="text/javascript">
usr = new wbUserGroup;
document.oncontextmenu = function() {
	return false;
}

document.onkeydown = function(event){
	if(event.keyCode == 123) {
		event.returnValue = false;
	}
}
</script>
<title></title>
<script id="kbItemsTemplate" type="text/x-jquery-tmpl">
<li>
	<a class="wzb-link02 f14 art" href="javascript:;" id="art{{>id}}" title="{{>title}}"><i class="glyphicon glyphicon-volume-up"></i>{{>title}}</a>
	<p>{{>time.substring(0,10)}}</p>
</li>
</script>
</head>
<body>

	<input type="hidden" value="FTN_AMD_KNOWLEDGE_MGT" name="belong_module"/><!-- 菜单栏设置 -->
	<title:get function="global.FTN_AMD_KNOWLEDGE_MGT"/>

	 <ol class="breadcrumb wzb-breadcrumb" style=" border-radius: 0; padding: 0 0 0 15px;background: #fff;">
	   <li><a href="javascript:wb_utils_gen_home(true);"><i class="fa wzb-breadcrumb-home fa-home"></i><lb:get key="label_km.label_core_knowledge_management_1"/></a></li>
	   <c:choose>
		 	<c:when test="${source eq 'index'}">
		 		<li><a href="${ctx}/app/admin/kb/storege"><lb:get key="label_km.label_core_knowledge_management_2"/></a></li>
		 		<li class="active">${kbItem.kbi_title}</li>
		 	</c:when>
		 	<c:otherwise>
		 		<li><a href="javascript:wb_utils_nav_go('FTN_AMD_KNOWLEDEG_APP','${loginUser.usr_ent_id }', '${label_lan }')"><lb:get key="label_km.label_core_knowledge_management_45"/></a></li>
	   			<li class="active"><lb:get key="label_km.label_core_knowledge_management_46"/></li>
		 	</c:otherwise>
		 </c:choose>
	 </ol> <!-- wzb-breadcrumb End-->

	<div class="panel wzb-panel" style="margin-bottom:0;border: none; border-radius: 0;">
		<div class="panel-heading">
			<c:choose>
			 	<c:when test="${source eq 'index'}">
			 		${kbItem.kbi_title }
			 	</c:when>
			 	<c:otherwise>
			 		<lb:get key="label_km.label_core_knowledge_management_46"/>
			 	</c:otherwise>
			 </c:choose>
		</div>

		<div class="panel-body">
					<!-- 标题-->
			     <div class="wzb-title-6">
						${kbItem.kbi_title}
					</div>
					<!-- 窗口 -->
					<c:choose>
						<c:when test="${kbItem.kbi_type=='ARTICLE'}">
							<div class="panel-content">
								<p>${kbItem.kbi_content}</p>
							</div>
						</c:when>
						<c:when test="${kbItem.kbi_type=='DOCUMENT'}">
							<div style="background: #222222;width:100%;height:660px;">
								<iframe id="ifm" allowtransparency="true" allowfullscreen="true" width="100%" height="650px" frameBorder="0" scrolling="yes"></iframe>
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
							      autoplay="autoplay" preload="auto" width="100%" height="100%" poster="" oncontextmenu="return false;" >
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
					
				     <div class="wzb-hold-content">
						<div class="wzb-article-tool margin-bottom25">
						     <a id="kb_like_count_${kbItem.kbi_id}" class="wzb-link03 margin-right15" href="javascript:;" title="<lb:get key='label_km.label_core_knowledge_management_47'/>">
						     	<i class="fa fa-thumbs-o-up"></i><lb:get key="label_km.label_core_knowledge_management_47"/> (<span>0</span>)</a>
						     <a id="kb_collect_count_${kbItem.kbi_id}" class="wzb-link03 margin-right15" href="javascript:;" title='<lb:get key="label_core_knowledge_management_48"/>'>
						     	<i class="fa skin-color fa-star"></i><lb:get key="label_km.label_core_knowledge_management_48"/> (<span>0</span>)</a>
						    <%--  <a class="wzb-link03" href="javascrit:;" title='<lb:get key="global_click_rate"/>'>
								<i class="fa skin-color fa-hand-o-up"></i>
								<lb:get key="global_click_rate" />
								(${kbItem.kbi_access_count})
							</a> --%>
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
											<c:when test="${!empty kbItem.kbi_online && kbItem.kbi_online eq 'ONLINE'}">
												<a id='download' class="wzb-link03" href="${kbItem.kbi_content}" target="_blank" title="<lb:get key="lab_kb_DOWNLOAD"/>">
													<i class="fa skin-color fa-download"></i>
													<lb:get key="lab_kb_DOWNLOAD" />
													<span id="kbi_download_count">(${kbItem.kbi_download_count})<span>
												</a>
											</c:when>
											<c:otherwise>
												<a id='download' class="wzb-link03" href="${ctx}/app/kb/center/download/${kbItem.kbi_id}" target="_blank"  title="<lb:get key="lab_kb_DOWNLOAD"/>">
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
								<span class="color-gray666" title='<lb:get key="course_publish_time"/>'><i class="fa skin-color fa-clock-o"></i>${fn:substring(kbItem.kbi_publish_datetime, 0, 10)}</span>
							</c:if>
						</div>     
					</div>
				

			<form action="#" method="post" class="margin-bottom25" id="commentEditer">
			      <textarea class="wzb-textarea-01 align-bottom margin-right10 margin-top20 cwn-editer-textarea" placeholder="<lb:get key='validate_max_400'/>"></textarea>
			      <input type="button" value="<lb:get key='global.button_submit'/>" class="btn wzb-btn-yellow wzb-btn-big align-bottom wzb-btn-submit">
			</form>

			<div class="wzb-title-2">
				<lb:get key="label_km.label_core_knowledge_management_52"/><span class="wzb-comment-count">0</span><lb:get key="label_km.label_core_knowledge_management_53"/>
				<em id="wbshow" class="wzb-show skin-color"><span><lb:get key="click_up" /></span><i class="fa fa-angle-up"></i></em></div>

			<span id="commentList"></span>

		</div>
	</div>  <!-- wzb-panel End-->

<script id="commentTemplate" type="text/x-jsrender">
 <div class="wzb-trend clearfix" did="{{>s_cmt_id}}">
     <div class="wzb-user wzb-user68">
        {{if s_doi_target_type == '1' && operator}}
            <a {{if isNormal == true}}href="${ctx}/app/personal/{{>operator.usr_ent_id}}"{{/if}}> <img class="wzb-pic" src="{{>operator.usr_photo}}"/></a>
        {{else}}
            <a {{if isNormal == true}}href="${ctx}/app/personal/{{>user.usr_ent_id}}"{{/if}}> <img class="wzb-pic" src="{{>user.usr_photo}}"/></a>
        {{/if}}
        {{if isNormal == true}}
            <p class="companyInfo"><lb:get key="label_km.label_core_knowledge_management_54"/></p>
            <div class="cornerTL">&nbsp;</div>
            <div class="cornerTR">&nbsp;</div>
            <div class="cornerBL">&nbsp;</div>
            <div class="cornerBR">&nbsp;</div>
        {{/if}}
     </div>

     <div class="wzb-trend-content">
          <div class="color-gray999">
             <span class="pull-right">
                   <em class="margin-right15">{{>s_cmt_create_datetime}}</em>
            	{{if s_doi_target_type != '1'}}
                	{{if s_doi_act != 'like'}}
                    	<a class="wzb-link03 margin-right15 wzb-sns-like" id="doi_{{>s_doi_id}}" href="javascript:;" title=""><i class="fa fa-thumbs-o-up"></i><lb:get key="label_km.label_core_knowledge_management_47"/>(<span>0</span>)</a>
                	{{/if}}
                	<a class="wzb-link03 margin-right15 review wzb-sns-comment" uname="{{>user.usr_display_bil}}" href="javascript:void(0);"><i class="fa color-blue00a fa-comment"></i><lb:get key="label_km.label_core_knowledge_management_50"/>(<span>{{>replies.length}}</span>)</a>
            	{{/if}}
            	{{if s_doi_uid == '${prof.usr_ent_id}' || isNormal == false}}
                	<a class="wzb-link03 wzb-sns-del-doing" data="{{>s_doi_id}}" href="javascript:;"><i class="fa color-blue00a fa-comment"></i><lb:get key="global.button_del"/></a>
            	{{/if}}
				<a class="wzb-link03 delete" data="{{>s_cmt_id}}" href="javascript:;"><i class="fa color-blue00a fa-times" data="{{>s_cmt_id}}"></i><lb:get key="global.button_del"/></a>
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
    <a class="wzb-reply-pic" href="${ctx}javascript:usr.user.manage_usr('{{>user.usr_ent_id}}','','','')"> <img src="{{>user.usr_photo}}"/></a>

    <div class="wzb-reply-content">
        <p class="color-gray999">
            {{if user && isNormal}}
                    <a href="${ctx }/app/personal/{{>user.usr_ent_id}}" class="wzb-link04" title="" target="">{{>user.usr_display_bil}}</a>
                {{else}}
                    <span class="wzb-link04">{{>user.usr_display_bil}}</span>
            {{/if}}
            {{if toUser}}
                <span class="grayC999"><lb:get key="label_km.label_core_knowledge_management_55"/></span>
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
  <button class="btn wzb-btn-yellow align-bottom" type="button"><lb:get key="global.button_reply"/></button>
</form>
</script>
<script id="memberInfoTemplate" type="text/x-jsrender">
<dl class="wzb-list-7 clearfix">
		<dd style="width:80px;">
			<div class="wzb-user wzb-user68">
				<a {{if isNormal == true}}href="{{>href}}"{{/if}} class="mypic"> <img src="{{>image}}"></a>
				{{if isNormal == true}}
					<p class="companyInfo" style="display: none;"><lb:get key="label_km.label_core_knowledge_management_54"/></p>
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

	var dt = null;
	var targetId;
	var sns = new Sns();
   	var module = 'Knowledge';
   	var meId = '${loginUser.usr_ent_id}';
   	var isNormal;
   	var commentList = null;
	$(function(){
		detail("${kbItem.kbi_id}");	//选中第一个

		var commentTabUrl = contextPath + '/app/comment/' + module + '/commentPageJson/' + targetId;
		commentList = $("#commentList").empty().cwnCommnent({
	        editerId : 'commentEditer',
	        commentUrl : commentTabUrl,
	        commentDisplay : true,
	        displayNum : 10,
	        params : {
	            userId : meId,
	            targetId : targetId,
	            module : module,
	            id : '${kbItem.kbi_id}',
	            isComment : 1
	        }
	    });
		
		//展开列表
		$("#wbshow").click(function(){
			if($(this).find("i").hasClass("fa-angle-down")){
				$(this).find("i").removeClass("fa-angle-down").addClass("fa-angle-up");
				$("#wbshow span").html(fetchLabel('click_up'));
				$(".wzb-trend .wzb-trend-parcel").show();	//展开列表
				
			} else {
				$(this).find("i").removeClass("fa-angle-up").addClass("fa-angle-down");
				$("#wbshow span").html(fetchLabel('click_down'));
				$(".wzb-trend .wzb-trend-parcel").hide();	//展开列表
			}
		});
		
	})
	
	var colModel = [ {
		format : function(data) {
			var p = {
				id : data.kbi_id,
				title : data.kbi_title,
				usr_display_bil : data.usr_display_bil,
				time : data.kbi_create_datetime//Wzb.displayTime(data.msg_begin_date, Wzb.time_format_ymdhm)
			};
			return $('#articlesTemplate').render(p);
		}
	}];

	//知识详情
	function detail(id){
			targetId = id;
			$.getJSON("${ctx}/app/admin/kb/detailJson/" + targetId, function(result){
			 		var data = result.kbItem;
			 		var p = {
			 				art_id : data.kbi_id,
			 				content: Wzb.htmlDecode(data.kbi_content),
			 				title : data.kbi_title,
			 				username : data.usr_display_bil,
			 				time : data.kbi_create_datetime
			 	 		}

			 	 	$("#kb_like_count_" + data.kbi_id).like({
						count : result.snsCount ? result.snsCount.s_cnt_like_count:0,
						flag : result.sns ? result.sns.like : false,
						id : data.kbi_id,
						module: 'Knowledge',
						tkhId : 0
					})
					$("#kb_collect_count_" + data.kbi_id).collect({
						count : result.snsCount?result.snsCount.s_cnt_collect_count:0,
						flag : result.sns?result.sns.collect : false,
						id : data.kbi_id,
						module: 'Knowledge',
						tkhId : 0
					})
					if(${sns_enabled} == true){
						$("#kb_share_count_" + data.kbi_id).share({
							count : result.snsCount?result.snsCount.s_cnt_share_count:0,
							flag : result.sns?result.sns.share : false,
							id : data.kbi_id,
							module: 'Knowledge',
							tkhId : 0
						})
					} else {
						$("#kb_share_count_" + data.kbi_id).remove();
					}
					$("#kb_create_datetime").html(data.kbi_create_datetime.substring(0,10))

			 })
			 //选中的图标
			 $("#knowledeg_list li").removeClass("now");
			 $("#art" + targetId).parent("li").addClass("now");
			 $("#commentList").show();
	}

	//返回列表按钮
	function backList(){
		window.location.href = "javascript:wb_utils_nav_go('FTN_AMD_KNOWLEDEG_APP','${loginUser.usr_ent_id }', '${label_lan }');"
	}
</script>
</body>
</html>
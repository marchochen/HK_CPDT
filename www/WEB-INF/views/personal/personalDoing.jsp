<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<link href="${ctx }/static/js/jquery.uploadify/uploadify.css" rel="stylesheet" type="text/css" />  
<script type="text/javascript" src="${ctx}/js/jquery.prompt.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>

<title></title>
<script type="text/javascript">
// 	var userId = window.location.href.replace(/.*\//,"");
	var userId = ${usrEntId};
	var meId = '${prof.usr_ent_id}';
	var targetId;
	var appTkhID;
	var sns = new Sns();
	var dt;
	var module = 'Doing';
	$(function() {

		$("#formText").prompt(fetchLabel('doing_input_comment_desc'));
		
		dt = $("#doingList").table({
			url : '${ctx}/app/doing/user/json/' + userId + '/Doing/' + userId,
			colModel : colModel,
			rp : 10,
			rowCallback : function(data){
				$("#doingList .title:last").css("width",$("#doingList").width()*0.85);
				$("#doingList .replydesc:last").css("width",$("#doingList").width()*0.85);
				//targetId = data.s_doi_id;
				var replies = data.replies;
				loadSubComment(data.replies, $("#doingList .wzb-trend .wzb-trend-parcel:last"));
				$("#doingList .wzb-trend:last").find(".review span").html(data.replies?data.replies.length:0);

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
							fileHtml = $.render.fileTemplate(p);
						}
						$("#doingList .wzb-trend:last").find(".title").after(fileHtml);
					});
				}
				
 				$("#doi_" + data.s_doi_id).like({
					count : data.snsCount?data.snsCount.s_cnt_like_count:0,
					flag : data.is_user_like,
					id : data.s_doi_id,
					module: 'Doing',
					tkhId : 0
				}) 
				photoDoing();
			},
			hideHeader : true,
			usepager : true
		})

		//点击回复按钮
		$(".huifu").live('click',function(){
			$(this).parents(".wzb-trend").eq(0).find("form").remove();
			$(this).parents(".wzb-trend").eq(0).find(".wzb-trend-parcel").append($("#commentFormTemplate").render());
			var text = "";
			if($(this).attr("uname")){
				 text = fetchLabel('detail_comment_back') + $(this).attr("uname") + ":"; //"回复"
			} else {
				 text = fetchLabel('doing_publish_comment');
			}
			$("textarea.wzb-textarea-01").val(text);
 			$("textarea.wzb-textarea-01").prompt(text).attr("uid", $(this).attr("uid"))
 			.attr('did',$(this).attr("did"));;
		})
		
		//评论回复功能
		$(".review").live('click',function(){
			var parcel = $(this).parents(".wzb-trend").eq(0);
			$(parcel).find(".wzb-trend-parcel").show();
			var text = "";
			if($(this).attr("uname")){
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
			var reply = $(this);	
			sns.doing.del($(this).attr("data"), function(result){
// 				if(result && result.status == 'success'){
					$(dt).reloadTable({
						url : '${ctx}/app/doing/user/json/' + userId + '/Doing/' + userId
					});
// 				}
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
			//alert(replyId);
			//return;
			if(note == $(textarea).attr('prompt')){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 200 ){
					Dialog.alert(fetchLabel('validate_max_200')); //'不能超过100个字符'
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
	
	colModel = [{
		format : function(data) {
			$.extend(data,{isMeInd : (meId == data.s_doi_uid),
				s_doi_create_datetime : data.s_doi_create_datetime.substring(0,16),
				s_doi_title : data.s_doi_title?data.s_doi_title.replace(/"/g,""):''})
			return $('#doingTemplate').render(data);
		}
	}]

	//递归获取二级以下的评论
	function loadSubComment(replies, obj){
		if(replies){
			$.each(replies,function(i,val){
				$.extend(val,{isMeInd : meId == val.s_cmt_uid, isNormal : true})
				$(obj).append($("#commentDescTemplate").render(val));
				$(obj).find(".wbdu:last").css("width",$("#doingList").width()*0.8);
				$("#art_like_count_" + val.s_cmt_id).die("click");
 				$("#art_like_count_" + val.s_cmt_id).like({
					count : val.snsCount ? val.snsCount.s_cnt_like_count:0,
					flag : val.is_user_like,
					id : val.s_cmt_id,
					module: module,
					isComment : 1,
					tkhId : 0
				})
				if(val.replies != undefined && val.replies.length > 0){
					loadSubComment(val.replies,$(obj).find(".wzb-reply:last"));
				}
			})
		}
	}

</script>
<!-- 动态模板 -->
<jsp:include page="../common/doingTemplate.jsp"></jsp:include>

</head>
<body>
<input type="hidden" id="sessionId" value="${pageContext.session.id}" />  
<input type="hidden" value="Course" id="s_clt_module">
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">

<jsp:include page="personalMenu.jsp"></jsp:include>
<script type="text/javascript">
$(function(){
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
		    hide: 'unfocus',
		    style: {
		        classes: 'qtip-bootstrap',
		            width: 353
		    },
		    events: {
		        show: function(event, api) {
		    	    $('#file_upload').uploadify({
		    		    'uploader' : '${ctx}/app/upload/Doing/Img;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
		    	        'swf'  : '${ctx}/static/js/jquery.uploadify/uploadify.swf?var='+(new Date()).getTime(),
		    	        'cancelImg' : '${ctx}/static/js/jquery.uploadify/uploadify-cancel.png',
		    	        'buttonText': fetchLabel('global_select_file'),
		    	        'fileObjName' : 'file',
// 		     	        'queueID' : 'file_upload',//'uploadList',
		    	        'multi'     : false,
		    	        'auto'      : true,
		     	        'fileTypeExts'   : '*.jpg;*.gif;*.png;*.JPG',
		    	        'fileTypeDesc'  : 'Image files',
		    	        'fileSizeLimit' : '50MB', 
		    			'formData'  : {'jsessionid':'${pageContext.session.id}'},
		    			'onUploadComplete' : function() {  
		                    $("#file_upload").uploadify("destroy");
		                    $("#qtip-uploadImgBoxQitp").hide();
		                },
		    	        'onUploadSuccess': function(file, data, response) {
		    		        //var dt =  eval('(' + data + ')');
		    				//var val = dt.tmf;
		                	//loadFile(val,dt.path);  
		    	        	initFile();       
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
		    hide: 'unfocus',
		    style: {
		        classes: 'qtip-bootstrap',
		            width: 353
		    },
		    events: {
		        show: function(event, api) {
		    	    $('#doc_upload').uploadify({
		    		    'uploader' : '${ctx}/app/upload/Doing/Doc;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
		    	        'swf'  : '${ctx}/static/js/jquery.uploadify/uploadify.swf?var='+(new Date()).getTime(),
		    	        'cancelImg' : '${ctx}/static/js/jquery.uploadify/uploadify-cancel.png',
		    	        'buttonText': fetchLabel('global_select_file'),
		    	        'fileObjName' : 'file',
		    	        'multi'     : false,
		    	        'auto'      : true,
						//'fileTypeExts'   : '*.exe;*.bat',
						//'fileTypeDesc'  : 'no exe bat Files',
						'fileSizeLimit' : '50MB', 
		    			'formData'  : {'jsessionid':'${pageContext.session.id}'},
		    			'onUploadComplete' : function() {  
		                    $("#doc_upload").uploadify("destroy");
		    				$("#qtip-uploadDocBoxQitp").hide();
		                },
		    	        'onUploadSuccess': function(file, data, response) {
		    		       // var dt =  eval('(' + data + ')');
		    				//var val = dt.tmf;
		                	//loadFile(val,dt.path);    
		    	        	initFile();       
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
		    hide: 'unfocus',
		    style: {
		        classes: 'qtip-bootstrap',
		            width: 353
		    },
		    events: {
		        show: function(event, api) {

		    	    $('#video_upload').uploadify({
		    		    'uploader' : '${ctx}/app/upload/Doing/Video;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
		    	        'swf'  : '${ctx}/static/js/jquery.uploadify/uploadify.swf?var='+(new Date()).getTime(),
		    	        'cancelImg' : '${ctx}/static/js/jquery.uploadify/uploadify-cancel.png',
		    	        'buttonText': fetchLabel('global_select_file'),
		    	        'fileObjName' : 'file',
		    	        'multi'     : false,
		    	        'auto'      : true,
		     	        'fileTypeExts'   : '*.mp4',
		    	        'fileTypeDesc'  : 'mp4 files',
		    	        'fileSizeLimit' : '50MB', 
		    			'formData'  : {'jsessionid':'${pageContext.session.id}'},
		    			'onUploadComplete' : function() {  
		                    $("#video_upload").uploadify("destroy");
		    				$("#qtip-uploadVideoBoxQitp").hide();
		                },
		      	        'onUploadSuccess': function(file, data, response) {
		    		       // var dt =  eval('(' + data + ')');
		    				//var val = dt.tmf;
		                	//loadFile(val,dt.path);      
		                	initFile();    
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
			if(note == $("#formText").attr('prompt')||note==""){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 200 ){
					Dialog.alert(fetchLabel('validate_max_200')); //'不能超过100个字符'
					return;
				}
			}
			var action = "doing";
			sns.doing.add(note, action, module, userId, function(result){
		 		if(result.status=='success'){
		 			$(dt).reloadTable({
						url : '${ctx}/app/doing/user/json/' + userId + '/Doing/' + userId
					}); //成功刷新
		 			$("#formText").val('');
		 			$("#docBox").empty();
		 			$("#imgBox").empty();
		 		}
			});
		});


		$("#formCancel").click(function(){
			var module = "Doing";
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
	 		docTemplate: '<span class="wzb-send-info"><a href="{{>url}}">{{>name}}</a></span><a href="#" class="wzb-send-close color-gray999"><i data="{{>id}}" class="fa fa-times"></i></a>',
			upImgTemplate: '<li><img width="60" style="min-height:10px" src="${ctx}{{>url}}"/><a href="javascript:;"><i data="{{>id}}" class="glyphicon f14 grayCdbd glyphicon-remove file-temp-del" style="position: absolute;left: 45px;color:red; display:none;"></i></a></li>'
	 	});

	    $("#uploadVideoBtn").live('click',function(){
			var url = $("#uploadVideoUrl").val();
			if(url == undefined || url == ''){
				return ; //alert("不能为空");
			}
			$.ajax({
				url : '${ctx}/app/upload/Doing/Video/online',
				async : false,
				type : 'post',
				dataType : 'json',
				data : {
					url : url
				},
				success : function(result){
					$("#uploadVideoUrl").val('');
					$("#qtip-uploadVideoBoxQitp").hide();
					loadFile(result.tmf,result.path);
				}
			})
		});


	    $("#uploadImgBtn").live('click',function(){
			var url = $("#uploadImgUrl").val();
			if(url == undefined || url == ''){
				return ; //alert("不能为空");
			}
			$.ajax({
				url : '${ctx}/app/upload/Doing/Img/online',
				async : false,
				type : 'post',
				dataType : 'json',
				data : {
					url : url
				},
				success : function(result){
					$("#uploadImgUrl").val('');
					$("#qtip-uploadImgBoxQitp").hide();
					loadFile(result.tmf,result.path);
				}
			})
			
		});

})

function initFile(){
	$("#imgBox").empty();
	$("#docBox").empty();
	$.getJSON("${ctx}/app/upload/Doing/noMaster",function(result){
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
		url =	"/"+ path + "/" + val.mtf_file_rename;
		name = val.mtf_file_name;
	}
	if(name== '' || name == undefined || url == undefined || url ==''){
		return ;
	}
    if(val.mtf_type == 'Img'){
    	$("#imgBox").append($.render.upImgTemplate({
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
					<p class="mb10"><lb:get key="upload_tip_image"/></p>
					<input type="file"  name="file_upload" class="fl" id="file_upload"/>
					
				</div>
				<div style="padding: 10px 0 10px 0;display: none;" id="content_1">
					URL:<input type="text" class="pertxt"  name="url" class="fl" id="uploadImgUrl"/>
					<input type="button" id="uploadImgBtn" class="wbtj btn wzb-btn-orange" value='<lb:get key="button_ok"/>' style="height: auto;line-height: 26px;"/>
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
					URL:<input type="text" class="pertxt"  name="url" class="fl" id="uploadVideoUrl"/>
					<input type="button" id="uploadVideoBtn" class="wbtj btn wzb-btn-orange" value='<lb:get key="button_ok"/>' style="height: auto;line-height: 26px;"/>
				</div>
			</div>
		</div>
		
		<div class="xyd-article">
            <c:if test="${isMeInd == 'true'}">
                <div class="wzb-title-2"><lb:get key="publish_doing"/></div>
            
                <div class="wzb-send">
                     <form action="#" method="post">      
                         <textarea id="formText" class="wzb-textarea-04"><lb:get key="doing_input_comment_desc"/></textarea>
                         
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
                                   <a id="uploadImg" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-image-o"></i><lb:get key="doing_image"/></a>
                                   <a id="uploadDoc" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-word-o"></i><lb:get key="doing_doc"/></a>
                                   <a id="uploadVideo" href="javascript:;" class="wzb-link03"><i class="fa fa-file-video-o"></i><lb:get key="doing_video"/></a>					    
                              </div>
                              <div class="pull-right">
                                   <input class="btn wzb-btn-yellow" id="formCancel" type="button" value='<lb:get key="button_cancel"/>'/>
                                   <input class="btn wzb-btn-yellow" id="formSubmit" type="button" value='<lb:get key="btn_post"/>'/>
                              </div> 		
                        </div>
                    </form>
                </div> <!-- send End-->
            </c:if>
            
            <div class="wzb-title-2"><lb:get key="personal_my_dynamic"/></div>
            
            <div id="doingList"></div>
		</div> <!-- xyd-article End-->
	
	</div>
</div> <!-- xyd-wrapper End-->

</body>
</html>
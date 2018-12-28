<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript"
	src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<link href="${ctx }/static/js/jquery.uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<title></title>


<c:if test="${lang == 'en-us' }">

	<style type="text/css">
			.myselect{
				border: 1px solid; 
			}
	</style>
</c:if>

<script id="add-user-button" type="text/x-jsrender">
	<a name="addUser" class="wzb-link04" href="javascript:;" onclick="addSendUser({{>usr_ent_id}}, '{{>usr_display_bil}}')">
		<i class="fa fa-plus"></i>
		<lb:get key="global.button_add"/>
	</a>
</script>
<script id="cancel-user-button" type="text/x-jsrender">
	<span class="skin-color">
		<i class="fa f14 fbold mr5 fa-check"></i>
		<lb:get key="label_im.label_core_information_management_36"/>
	</span> | 
	<a name="cancelUser" class="grayC666" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')"><lb:get key="global.button_cancel"/></a>
</script>

<script type="text/javascript">
	$(function() {
		//进来或刷新先删除掉以前上传的照片
		$.getJSON("${ctx}/app/upload/delete/KnowQuestion",function(result){});
		
		$.ajax({
			url : '${ctx}/app/know/allKnow/kca/CATALOG/0',
			type : 'POST',
			async : false,
			dataType : 'json',
			success : function(data){
				for(var i=0;i<data.kca.length;i++){
					var html = $("#parent_catalog").html() + '<option value="' + data.kca[i].kca_id + '" title="' + data.kca[i].kca_title + '">' + data.kca[i].kca_title + '</option>';
					$("#parent_catalog").html(html);
				}
				$("#parent_catalog").children().eq(0).attr("selected",true); 
			}
		});
		
		if($("#parent_catalog").children().length > 0){
			$.ajax({
				url : '${ctx}/app/know/allKnow/kca/NORMAL/' + $("#parent_catalog :selected").attr("value"),
				type : 'POST',
				async : false,
				dataType : 'json',
				success : function(data){
					var html = '<option value="0" title="<lb:get key="know_no_select" />"><lb:get key="know_no_select" /></option>';
					for(var i=0;i<data.kca.length;i++){
						html += '<option value="' + data.kca[i].kca_id + '" title="' + data.kca[i].kca_title + '">' + data.kca[i].kca_title + '</option>';
					}
					$("#child_catalog").html(html);
					$("#child_catalog").children().eq(0).attr("selected",true); 
				}
			});
		}
		//为悬赏积分输入框加入事件
		$('#queBounty').keyup(function(){
			 creditsConfirm();
		});
        
		//选择邀请用户
	    $("#addInstructorModal").on('show.bs.modal', function (e) {
	    	addUser();
	    });
	    
		//只显示讲师
	    $("#instrOnly").live('click', function(){
			if($(this).hasClass("instrOnly")) {
				$(this).removeClass("instrOnly");
				addUser('', false)
				$(this).val(fetchLabel('group_show_instr_only'));
			}else {
				addUser('', true)
				$(this).addClass("instrOnly");
				$(this).val(fetchLabel('group_all_users'));
			}
	    })
	})
	
	
	function addUser(searchContent,instrOnly){
		$(".modal-dialog .wzb-pop-1-main").html('');
        $(".modal-dialog").find(".wzb-pop-1-main").empty().table({
        	url : contextPath + '/app/user/getInstructors',
        	params : {
				searchContent : searchContent,
				instrOnly : instrOnly
			},
    		gridTemplate : function(data){
    			p = {
    				image : contextPath + data.usr_photo,
    				usr_display_bil : data.usr_display_bil,
    				usg_display_bil : data.usg_display_bil,
    				ugr_display_bil : data.ugr_display_bil,
    				href : 'javascript:;',
    				usr_ent_id : data.usr_ent_id,
    				add : true,
    				iti_level : fetchLabel("know_teacher_"+data.iti_level),
    				iti_score : data.iti_score,
    				isNormal : true
    			}
    			return $('#instructorTemplate').render(p);
    		},
    		view : 'grid',
    		rowSize : 4,
    		rp : 12,
    		showpager : 5,
    		usepager : true,
    		hideHeader : true,
    		trLine : false, 
            onSuccess : function(data){
                //给添加按钮事件
            	//默认选中的给予标记选中
             	 $(".wzb-choose-box .wzb-choose-info").each(function(){
             		var usr_ent_id = $(this).attr("value");
             		var usr_display_bil =  $(this).find(".wzb-choose-detail").text();
             		p = {
             			usr_ent_id : usr_ent_id,
             			usr_display_bil : usr_display_bil
             		}
             		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
                });                
            }
    	 });
	}
	
	//添加选中的讲师
	function addSendUser(usr_ent_id,usr_display_bil){
		var p = {
				usr_ent_id : usr_ent_id,
				usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#cancel-user-button").render(p));
		
		$(".wzb-choose-box").html($(".wzb-choose-box").html() + $("#detele-user-button").render(p));
	}
	//取消已选中的讲师
	function cancelUser(usr_ent_id, usr_display_bil){
		var p = {
			usr_ent_id : usr_ent_id,
			usr_display_bil : usr_display_bil
		}
		$("#user_" + usr_ent_id).html($("#add-user-button").render(p));
		
		$("#send_user_" + usr_ent_id).remove();
	}
	
	//选择所有用户
	function addAll(){
		$("a[name='addUser']").click();
	}
	//搜索用户
	function searchUserList(){
		searchContent = $("input[name='search_add_member']").val();
		if(fetchLabel('attention_find_desc') == searchContent){
			searchContent =  "";
		}
		addUser(searchContent);
	}
	
	function clearAll(){
		$("a[name='clearAll()']").click();
		$(".wzb-choose-box").html('');
	}
	
	var before_bounty = "";
	function creditsConfirm(){
		var bounty=$('#queBounty').val();
		var reg=/^\d+(\.\d+)?$/;
		var reg2 = /^\d+(\.\d{1,2})?$/;
		
		var returnObj={
				flag:true,
		        msg:'<lb:get key="know_inputBouty_tip1"/>'
		};
		if(bounty!=''&&bounty!='0')	{			
		    if(reg.test(bounty)){
		    	// 保留两位
		    	if (!reg2.test(bounty)) {
		    		bounty = $('#queBounty').val(Math.floor(bounty * 100) / 100);
		    	}
		    	
		    	$('#bountyMsg span').text(returnObj.msg);
		    	$('#bountyMsg span').css({'color':'black'});
		    	if(bounty>${credits}){
		    		returnObj.msg='<lb:get key="know_inputBouty_tip2"/>';
		    		$('#bountyMsg span').text(returnObj.msg);
			    	$('#bountyMsg span').css({'color':'red'});
			    	returnObj.flag=false;
		    	}
		    }else{
		    	returnObj.msg='<lb:get key="know_inputBouty_tip5"/>';
		    	$('#bountyMsg span').text(returnObj.msg);
		    	$('#bountyMsg span').css({'color':'red'});
		    	returnObj.flag=false;
		    }
		}
		console.info(returnObj);
		return returnObj;
	}
	//切换分类
	function changeCatalog(){
		$.ajax({
			url : '${ctx}/app/know/allKnow/kca/NORMAL/' + $("#parent_catalog :selected").attr("value"),
			type : 'POST',
			dataType : 'json',
			success : function(data){
				var html = '<option value="0">' + fetchLabel("know_no_select") + '</option>';
				for(var i=0;i<data.kca.length;i++){
					html += '<option value="' + data.kca[i].kca_id + '">' + data.kca[i].kca_title + '</option>';
				}
				$("#child_catalog").html(html);
				$("#child_catalog").children().eq(0).attr("selected",true); 
			}
		});
	}
	
	//提交提问
	function submitAddKnow(){
		if($("input[name='queTitle']").val() == ''){
			Dialog.alert(fetchLabel('know_myask_not_null'));
			return;
		}
		if(getChars($("input[name='queTitle']").val())>400){
			Dialog.alert(fetchLabel('know_myask_not_null'));
			return;
		}
		if(getChars($("textarea[name='queContent']").val()) > 2000){
			Dialog.alert(fetchLabel('know_ask_desc_not_null'));
			return;
		}
		if($("#parent_catalog :selected").attr("value") == undefined){
			Dialog.alert(fetchLabel('know_select_catalog_error'));
			return;
		}
		
		var count = 0;
		$("#parent_catalog option:selected").each(function(){
            count++;
        });
		if(count > 1){
			Dialog.alert(fetchLabel("label_know_validate_select_only_one_tips"));
			return;
		}
		
		count = 0;
		$("#child_catalog option:selected").each(function(){
            count++;
        });
		if(count > 1){
			Dialog.alert(fetchLabel("label_know_validate_select_only_one_tips"));
			return;
		}
		
		var obj=creditsConfirm();
		if(!obj.flag){
			$('#queBounty').focus();
			Dialog.alert(obj.msg);
			return;
		}

        var entIds = [];
        $(".wzb-choose-box .wzb-choose-info").each(function(){
       	 entIds.push($(this).attr("value"));
       	})
        $("input[name=que_ask_ent_ids]").val(entIds.join(','));
        
		$("#addQuestionForm").submit();
	}
	
	
	//========================回答js==============================
	$(function(){
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
		    hide: '',
		    style: {
		        classes: 'qtip-bootstrap',
		            width: 353
		    },
		    events: {
		        show: function(event, api) {
		    	    $('#file_upload').uploadify({
		    		    'uploader' : '${ctx}/app/upload/KnowQuestion/Img;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
		    	        'swf'  : '${ctx}/static/js/jquery.uploadify/uploadify.swf?var='+(new Date()).getTime(),
		    	        'cancelImg' : '${ctx}/static/js/jquery.uploadify/uploadify-cancel.png',
		    	        'buttonText': fetchLabel('global_select_file'),
		    	        'fileObjName' : 'file',
						//'queueID' : 'file_upload',//'uploadList',
		    	        'multi'     : false,
		    	        'auto'      : true,
		     	        'fileTypeExts'   : '*.jpg;*.gif;*.png;*.JPG',
		    	        'fileTypeDesc'  : 'Image files',
		    	        //'fileSizeLimit' : '50MB', 
		    			'formData'  : {'jsessionid':'${pageContext.session.id}','uploadType':'ajax'},
		    			'onUploadStart':function(file){
		    				$("#header-overlay-pc").unbind("click");
		    	            if(file.size > 50*1024*1024){ //定义允许文件的大小为50M
		    	            	$("#qtip-uploadImgBoxQitp").hide();
		    	            	$("#header-overlay-pc").hide();
		    	            	Dialog.alert(fetchLabel("upload_tip_image"));
		    		            $("#file_upload").uploadify('stop');
		    		            $("#file_upload").uploadify('cancel', '*');
		    	            }
		    			},
		    			'onUploadComplete' : function() {  
		                    $("#file_upload").uploadify("destroy");
		                    $("#qtip-uploadImgBoxQitp").hide();
		                    $("#header-overlay-pc").hide();
		                },
		    	        'onUploadSuccess': function(file, data, response) {
		    	        	data = eval('(' + data + ')');
		    				if(data.status == 'fail'){
		    					Dialog.alert(data.errorMsg);
		    				}else{
			    	        	$('#header-overlay-pc').click(function(){
			      			    	$("#qtip-uploadImgBoxQitp").hide();
			      			    	$('#header-overlay-pc').hide();
			      			    })
			    	        	initFile();    
		    				}
		    	        }           
		    	    });

		        }
		    }
		});

		$('#header-overlay-pc').click(function(){
	    	$("#qtip-uploadImgBoxQitp").hide();
	    	$('#header-overlay-pc').hide();
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
		/* $("#formSubmit").click(function(){
			var note = $("#formText").val();
			if(note == $("#formText").attr('prompt') || note =='' || note== undefined){
				note = '';
				Dialog.alert(fetchLabel('detail_comment_not_empty')); //'评论不能为空！'
				return;
			} else {
				if(getChars(note) > 200 ){
					Dialog.alert(fetchLabel('validate_max_200')); //'不能超过200个字符'
				}
			}
			var action = "group";
			sns.doing.add(note, action, module, grp_id, function(result){
		 		if(result.status=='success'){
		 			$(dt).reloadTable({
						url : '${ctx}/app/doing/user/json/' + meId  + '/Group/' + grp_id
					}); //成功刷新
		 			$("#formText").val('');
		 			$("#docBox").empty();
		 			$("#imgBox").empty();
		 		}
			});
		}); */
	
		 	$.templates({
				upImgTemplate: '<li style="position:relative"><img width="60" style="min-height:10px" src="{{>url}}"/><a href="javascript:;" style="position: absolute;bottom: -14px;right: -5px;"><i data="{{>id}}" class="glyphicon f14 grayCdbd glyphicon-remove file-temp-del" style="color:red; display:none;"></i></a></li>'
		 	});
	
		    $("#uploadImgBtn").live('click',function(){
				var url = $("#uploadImgUrl").val();
				if(url == undefined || url == ''){
					Dialog.alert(fetchLabel("upload_tips_online"));
					$("#qtip-uploadImgBoxQitp").hide();
					$("#header-overlay-pc").hide();
					return;
				}
				$.ajax({
					url : '${ctx}/app/upload/KnowQuestion/Img/online',
					async : false,
					type : 'post',
					dataType : 'json',
					data : {
						url : url
					},
					success : function(result){
						$("#uploadImgUrl").val('');
						$("#qtip-uploadImgBoxQitp").hide();
						$("#header-overlay-pc").hide();
						loadFile(result.tmf,result.path);
					}
				})
				
			});
	
	});
	
	function initFile(){
		$("#imgBox").empty();
		$.getJSON("${ctx}/app/upload/KnowQuestion/noMaster",function(result){
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
			url =	contextPath+"/"+ path + "/" + val.mtf_file_rename;
			name = val.mtf_file_name;
		}
	    if(val.mtf_type == 'Img'){
	    	$(".imgBox ul").append($.render.upImgTemplate({
	            id : val.mtf_id,
				url : url
			}));
	    }
	}
	
	//=========================end================================
</script>
</head>
<body>
	<form action="addKnowQuestion" method="post" id="addQuestionForm">
		<div class="xyd-wrapper">
			<div id="main" class="xyd-main clearfix">
				<jsp:include page="knowMenu.jsp"></jsp:include>
				<div id="know_jsp" class="xyd-article">
					<div class="wzb-title-2"><lb:get key="know_my_want" /></div>

					<table class="margin-top15">
						<tr>
							<td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><lb:get
									key="know_my" /><lb:get key="know_question_content" />：</td>

							<td class="wzb-form-control">
								<div class="wzb-selector">
									<input type="text" class="form-control" name="queTitle"
										id="kbi_title" value="" maxlength="400" />
								</div> <span class="color-gray999"><lb:get
										key="know_my_put_question_desc" /></span>
							</td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><lb:get
									key="know_question_supplement" />：</td>

							<td class="wzb-form-control">
								<div class="wzb-selector">
									<div class="header-overlay-pc" id="header-overlay-pc" style="display: none;"></div>
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
													<p class="mb10 color-gray666"><lb:get key="upload_tip_image"/></p>
													<input type="file"  name="file_upload" class="pull-left" id="file_upload"/>
												</div>
												<div style="padding: 10px 0 10px 0;display: none;" id="content_1">
													<p class="mb10 color-gray666"><lb:get key="lab_group_online_addr"/></p>
													URL：<input type="text" class="wzb-inputText"  name="url" class="pull-left" id="uploadImgUrl"/>
													<input type="button" id="uploadImgBtn" class="wbtj btn wzb-btn-orange" value='<lb:get key="button_ok"/>' style="height: auto;padding: 3px 8px;margin-bottom:5px;"/>
												</div>
											</div>
										</div>
										<div class="">
			                                	<textarea onkeydown="return noMaxlength(event,this,2000);"  rows="3" class="form-control wzb-textarea-04" name="queContent"></textarea>  
			                                    <div class="color-gray999"><lb:get key="know_question_supplement_desc" /></div>
			                                    <div class="clearfix">
			                                         <div class="pull-left">
			                                              <span class="color-gray666"><lb:get key="global_attachment"/>：</span>
			                                              <a id="uploadImg"  onclick="$('#header-overlay-pc').show();" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-image-o"></i><lb:get key="doing_image"/></a>
			                                         </div>
			                                   </div>
			                                   <div class="margin-top10 wzb-send-tool clearfix">
			                                         <div class="imgBox clearfix">
			                                              <ul id="imgBox">
			                                                  <!-- 图片 -->
			                                              </ul>
			                                         </div>
			                                    </div>
			                            </div> <!-- send End-->
									
								</div> 
							</td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><lb:get
									key="know_question_categories" />：</td>

							<td class="wzb-form-control"><select multiple="multiple"
								class="form-control" style="width:130px; height: 130px;" id="parent_catalog"
								name="kcaIdOne" onchange="changeCatalog()" ></select> <span
								class="wzb-form-arrow"><i
									class="fa fa-arrow-right skin-color"></i></span> <select
								multiple="multiple" class="form-control" style="width:130px; height: 130px;"
								id="child_catalog" name="kcaIdTwo"></select></td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><lb:get
									key="know_bounty" />：</td>

							<td class="wzb-form-control">
								<div class="wzb-selector">
									<input type="text" class="form-control" id="queBounty"
										name="queBounty" value="" maxlength="50" />
								</div> <span id="bountyMsg" class="color-gray999">
								<lb:get key="know_inputBouty_tip4" />：${credits}<br/>
								<lb:get key="know_inputBouty_tip1" /></span>
							</td>
						</tr>

						<tr>
							<td class="wzb-form-label" valign="top"><lb:get key="label_cm.label_core_community_management_49"/>：</td>

							<td class="wzb-form-control">
								<div class="wzb-choose-box" style="width: 400px;">
									<c:forEach items="${users }" var="user">
										<div id="send_user_${user.usr_ent_id }" class="wzb-choose-info" value="${user.usr_ent_id }">
											<span class="wzb-choose-detail">${user.usr_display_bil }</span>
											<a class="wzb-choose-area" href="javascript:;" onclick="cancelUser(${user.usr_ent_id },'${user.usr_display_bil }')">
												<i class="fa fa-remove"></i>
											</a>
										</div>
									</c:forEach>
								  </div>
								<input type="hidden" name="que_ask_ent_ids" />

								<div class="margin-top10">
									<a class="btn wzb-btn-orange" data-toggle="modal"
										data-target="#addInstructorModal"><lb:get key="button_search"/></a>
									<a class="btn wzb-btn-orange margin-left15" 
									href="javascript:;" onclick="clearAll()">
										<lb:get key="label_im.label_core_information_management_29"/>
									</a>
									
								</div>

								<div class="modal fade" id="addInstructorModal" tabindex="-1"
									role="dialog" aria-labelledby="addInstructorModalLabel"
									aria-hidden="true">
									<div class="modal-dialog" style="margin:-380px 0 0 -310px;">
										<div class="modal-content cont" >
				                    		<div class="modal-header">
				                          		<button type="button" class="close" data-dismiss="modal" aria-hidden="true" >&times;</button>
				                          		<h4 class="modal-title" id="myModalLabel"><div class="pfindtit"><lb:get key="label_cm.label_core_community_management_49"/></div></h4>
				                        	</div>
                                            
                                            <div class="wzb-model-3" style="padding: 0px 15px 15px 15px;height:500px;overflow-y:auto;" >
												<div class="modal-body" style="padding-left:0px;">
                                                    <div class="form-search form-tool">                 
                                                         <input type="text" name="search_add_member" class="form-control" value='<lb:get key="attention_find_desc"/>' onfocus="if(value==fetchLabel('attention_find_desc')){value=''}" onblur="if (value ==''){value=fetchLabel('attention_find_desc')}"/><input type="button" class="form-submit" value="" onclick="searchUserList()"/>
                                                    </div>
                                                </div>
                                                <div class="form-tool wzb-title-2" style="font-weight: normal;margin-bottom:20px">                 
                                                      <lb:get key="subordinate_filter_result"/>
                                                      
                                                      <div class="form-tool-right">
                                                           <input type="button" class="btn wzb-btn-yellow" value='<lb:get key="button_add_all"/>' onclick="addAll()"/>
                                                           
                                                           <%-- <wb:hasRole rolExtIds="INSTR_1,ADM_1,TADM_1"> --%>
                                                           <input type="button" value='<lb:get key="group_instr_only"/>' class="btn wzb-btn-yellow margin-left15" id="instrOnly"/>
                                                           <%-- </wb:hasRole> --%>
                                                     </div>
                                               </div>
                                                
												<div class="wzb-pop-1-main"></div>
                                            </div>
                                            
                                            <div class="modal-footer">
												<button type="button" class="btn wzb-btn-blue wzb-btn-big" data-dismiss="modal"><lb:get key="button_close"/></button>
											</div>
				                      	</div>
									</div>
								</div>
							</td>
						</tr>
					</table>

					<div class="wzb-bar">
						<input type="button" name="frmSubmitBtn"
							value='<lb:get key="button_ok"/>'
							class="btn wzb-btn-orange wzb-btn-big margin-right15"
							onclick="submitAddKnow()"> <input type="button"
							name="frmSubmitBtn" value='<lb:get key="button_cancel"/>'
							class="btn wzb-btn-orange wzb-btn-big"
							onclick="javascript:window.location.href='${ctx }/app/know/allKnow'">
					</div>

				</div>
				<!-- xyd-article End-->
			</div>
		</div>
		<!-- xyd-wrapper End-->
	</form>

<script type="text/x-jsrender" id="instructorTemplate">
<div class="wzb-display-01 wzb-percent-4"> 
   <dl class="wzb-list-7 clearfix">
       <dd>
            <div class="wzb-user wzb-user68">
                <a {{if isNormal == true}}href="{{>href}}"{{/if}}> <img class="wzb-pic" src="{{>image}}"></a>
                {{if isNormal == true}} 
                    <p class="companyInfo" style="display: none;"><lb:get key="know_ta"/></p>
                    <div class="cornerTL" style="width: 60px; height: 60px;">&nbsp;</div>
                    <div class="cornerTR" style="width: 60px; height: 60px;">&nbsp;</div>
                    <div class="cornerBL" style="width: 60px; height: 60px;">&nbsp;</div>
                    <div class="cornerBR" style="width: 60px; height: 60px;">&nbsp;</div>
                {{/if}}
            </div>
       </dd>
      
       <dt>
            <a class="wzb-link04" href="javascript:;" title="{{>usr_display_bil}}">{{>usr_display_bil}}</a>
            <p>{{>usg_display_bil}}</p>
			<p>{{if ugr_display_bil != 'Unspecified'}}{{>ugr_display_bil}}{{/if}}</p>
       </dt>
  </dl>
	<span id="user_{{>usr_ent_id}}" class="wzb-user-plus">
				{{if add == true}}
					<a name="addUser" class="wzb-link04" href="javascript:;" onclick="addSendUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
						<i class="fa fa-plus"></i>
						<lb:get key="global.button_add"/>
					</a>
				{{else}}
					<a name="addUser" class="wzb-link04" href="javascript:;" onclick="cancelUser({{>usr_ent_id}},'{{>usr_display_bil}}')">
						<i class="fa fa-plus"></i>
						<lb:get key="global.button_add"/>
					</a>
				{{/if}}
		</span>
</div>
</script>
</body>
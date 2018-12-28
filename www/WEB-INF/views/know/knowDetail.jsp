<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<link href="${ctx }/static/js/jquery.uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/jquery.sns.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/cwn_utils.js"></script>
<script type="text/javascript" src="${ctx}/static/js/jquery.uploadify/jquery.uploadify.js"></script>
<title></title>
<script type="text/javascript">
	var sns = new Sns();
	$(function(){
		
		//进来或刷新先删除掉以前上传的照片
		$.getJSON("${ctx}/app/upload/delete/KnowQuestion",function(result){});
		$.getJSON("${ctx}/app/upload/delete/Know",function(result){});
		
		//回答内容提示
		$("textarea[name='ansContent']").val(fetchLabel('label_core_community_management_127'));
    	$("textarea[name='ansContent']").focus(function(){
    		if($("textarea[name='ansContent']").val() == fetchLabel('label_core_community_management_127')){
    			$("textarea[name='ansContent']").val("");
    		}
    	});
    	$("textarea[name='ansContent']").blur(function(){
    		if($("textarea[name='ansContent']").val().trim() == ""){
    			$("textarea[name='ansContent']").val(fetchLabel('label_core_community_management_127'));
    		}
    	});
		
		if(${isTADM}){
			$("#header").remove();
			$("#menu").remove();
			$("#footer").remove();
			$("#left_menu").remove();
			$("#main").css({width: "738px", "min-width": "738px"});
			$("#know_jsp").removeClass("col-xs-9").addClass("col-xs-12");
		}
		
		if(${know_detail.que_type != 'FAQ'}){
			$.ajax({
				url : '${ctx}/app/know/answer/${know_detail.que_id}'  + '/' + $("#answer_list_size").val(),
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					getAnswerList(data, '${know_detail.que_type}');
				}
			});
		}
		
		$.ajax({
			url : '${ctx}/app/know/relevantKnow/${know_detail.que_id}/${know_detail.que_type}/${know_detail.knowCatalog.kca_id}',
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				for(var i = 0; i < data.know_list.results.length;i++)
				{
					data.know_list.results[i].enc_que_id = wbEncrytor().cwnEncrypt(data.know_list.results[i].que_id);
				}
				var html = $("#relatedQuestionTempelate").render(data.know_list.results)
                if(data.know_list.results.length == 0){
                    html = '<li>' + fetchLabel('label_core_community_management_126') + '</li>';
                }
                $("#know_list").html(html);
			}
		});
	})
	
	//获取回答列表
	function getAnswerList(data, queType){
		if(data.ask_list.totalRecord < 4){
			$("#wbshow").hide();
		} else {
			$("#wbshow").show();
		}
		var html = '';
		$("#answer_list").html('');
		for(var i=0;i<data.ask_list.results.length;i++){
			var ask_detail = data.ask_list.results[i];
			var is_specify = false;
			$(".wzb-wenda-info .mr20 .wzb-link04").each(function(){
				if($(this).attr("data") == ask_detail.ans_create_ent_id){
					is_specify = true;
				}
			});
			
			$.extend(ask_detail,{set_best_ind: ${prof.usr_ent_id} == ${know_detail.que_create_ent_id} && queType == 'UNSOLVED', isTADM: ${isTADM},is_specify:is_specify})	   
			ask_detail.ans_content=replaceVlaue(ask_detail.ans_content);
			$("#answer_list").append($("#answer-template").render(ask_detail));
			$("#answer_list .wzb-wenda-bar:last a").like({
				count : data.ask_list.results[i].snsCount.s_cnt_like_count,
				flag : data.ask_list.results[i].is_user_like,
				id : data.ask_list.results[i].ans_id,
				module: 'Answer',
				tkhId : 0
			});
			//图片显示
			$.templates({
				fileTemplate : '<p class="mt10 f14"><a href="${ctx}{{>url}}" target="_blank">{{>name}}</a></p>',
				imgTemplate : '<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${ctx}{{>url}}" target="_blank"><img width="60" src="${ctx}{{>url}}"/></a></p>'
			})
			var fileList = ask_detail.fileList;
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
					$("#answer_list #ans_content_"+ask_detail.ans_id).after(fileHtml);
				});
			}
		}
		if(data.ask_list.results.length == 0){
			html += '<div class="wdarea-list clearfix grayC999" style="padding:5px 5px 5px 10px;"><th>' + fetchLabel('know_not_available') + '</th></div>';
			$("#answer_list").html(html);
		}
		photoDoing();
	}
	
	//提交回答
	function addKnowAnswer(){
		var ac = $("textarea[name='ansContent']").val();
		if(getChars(ac) <= 0 || ac == fetchLabel('label_core_community_management_127')){
			Dialog.alert(fetchLabel('label_core_community_management_221'));
			return;
		}
		if( getChars(ac) > 2000){
			Dialog.alert(fetchLabel('know_answer_content_max_tips'));
			return;
		}
		if(getChars($("input[name='ansReferContent']").val()) > 800){
			Dialog.alert(fetchLabel('know_reference_material_error'));
			return;
		}
		$.ajax({
			url : '${ctx}/app/know/addKnowAnswer/${know_detail.que_id}',
			type : 'post',
			dataType : 'json',
			data : {
				ansContent: $("textarea[name='ansContent']").val(),
				ansReferContent : $("input[name='ansReferContent']").val()
			},
			success : function() {
				$.ajax({
					url : '${ctx}/app/know/answer/${know_detail.que_id}/' + $("#answer_list_size").val(),
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						getAnswerList(data, '${know_detail.que_type}');
						$("textarea[name='ansContent']").val('');
						$("input[name='ansReferContent']").val('');
						$("#imgBox").empty();
					}
				});
			}
		});
	}
	
	//取消提问
	function delAnswer(){
		var text = fetchLabel('know_del_question');
		if(${isTADM}){
			text = fetchLabel('know_confirm_delete');
		}
		Dialog.confirm({text:text, callback: function (answer) {
				if(answer){
					$.ajax({
						url : '${ctx}/app/know/delKnowQuestion/${know_detail.que_id}',
						type : 'POST',
						success : function() {
							if(${isTADM}){
								var parent = window.parent.opener;
				 				parent.location.href = parent.location.href;
				 				window.close();
							} else {
								window.location.href = '${ctx}/app/know/myKnow';
							}
						}
					});
				}
			}
		});
	}
	
	//问题补充框显示与隐藏
	function knowSupplement(){
		if($("#knowSupplement:hidden").length > 0){
			initFile("imgBox2","KnowQuestion");
			$("#knowSupplement").show();
		} else {
			$("#knowSupplement").hide();
		}
	}
	
	//展开收起
	function changeSize(){
		if($("#answer_list_size").val() == 3){
			$("#answer_list_size").val(9999);
			$("#wbshow").html(fetchLabel('know_collect') + '<i class="fa mL3 fa-angle-up"></i>');
		} else {
			$("#answer_list_size").val(3);
			$("#wbshow").html(fetchLabel('know_open') + '<i class="fa mL3 fa-angle-down"></i>');
		}
		$.ajax({
			url : '${ctx}/app/know/answer/${know_detail.que_id}'  + '/' + $("#answer_list_size").val(),
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				getAnswerList(data, '${know_detail.que_type}');
			}
		});
	}
	
	//问题补充提交
	function changeQueContent(){
		if(getChars($("#queContent").val()) > 2000){
			Dialog.alert(fetchLabel('know_ask_desc_not_null'));
			return;
		}
		$.ajax({
			url : '${ctx}/app/know/changeQueContent/${know_detail.que_id}',
			data : {
				queContent : $("#queContent").val()
			},
			type : 'POST',
			success : function() {
				//获取问答信息，更新图片
				$.ajax({
					url : '${ctx}/app/know/knowDetail/json/${know_detail.que_type}/${know_detail.que_id}',
					type : 'POST',
					dataType : 'json',
					success : function(data) {
						//图片显示
						$.templates({
							fileTemplate : '<p class="mt10 f14"><a href="${ctx}{{>url}}" target="_blank">{{>name}}</a></p>',
							imgTemplate : '<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${ctx}{{>url}}" target="_blank"><img width="60" src="${ctx}{{>url}}"/></a></p>'
						})
						var fileList = data.know_detail.fileList;
						if(fileList != undefined){
							var  fileHtml = "";
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
								
								if(val.mtf_type=='Img' ){
									fileHtml += $.render.imgTemplate(p)
								} else {
									fileHtml += $.render.fileTemplate(p);
								}
							});
							$("#know_content_img").html(fileHtml);
						}							
					}
				});
				if($("#queContent") != undefined){
					if($("#queContent").val() != ''){
						var html = '<p class="mt10"><span class="grayC999">' + fetchLabel('know_question') + fetchLabel('know_supplement') 
									+ '： </span>' + $("#queContent").val() + '</p>';
						$("#que_supplement").html(html);
						$("#que_supplement").show();
					}else{
						$("#que_supplement").hide();
					}
				}
				$("#knowSupplement").hide();
			}
		});
	}
	
	//设为最佳回答
	function setBestAnswer(ansId, ansEntId){
		Dialog.confirm({text:fetchLabel('know_set_best_answer_confirm'), callback: function (answer) {
				if(answer){
					$.ajax({
						url : '${ctx}/app/know/setBestAnswer/' + ansId + '/' + ansEntId + '/${know_detail.que_id}',
						type : 'POST',
						success : function() {
							window.location.href = '${ctx}/app/know/knowDetail/SOLVED/'+wbEncrytor().cwnEncrypt(${know_detail.que_id});
						}
					});
				}
			}
		});
	}
	
	//删除该条回答
	function delThisAnswer(ansId, ansEntId){
		Dialog.confirm({text:fetchLabel('know_del_answer_confirm'), callback: function (answer) {
				if(answer){
					$.ajax({
						url : '${ctx}/app/know/delThisAnswer/' + ansId + '/' + ansEntId + '/${know_detail.que_id}',
						type : 'POST',
						success : function() {
							window.location.reload();
						}
					});
				}
			}
		});
	}
	
	//回答评价
	function changeAnsVote(goodInd){
		$.ajax({
			url : '${ctx}/app/know/changeAnsVote/${know_detail.que_id}/' + goodInd,
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				if(data.evaluationInd == true){
					Dialog.alert(fetchLabel('know_not_repeat_evaluation'));
				} else {
					$("#evaluate_sum").html(data.answerSituation.ans_vote_total);
					$("#vote_for_rate").html(data.good_rate);
					$("#vote_for_sum").html('（' + data.answerSituation.ans_vote_for + '）');
					$("#vote_down_rate").html(data.not_good_rate);
					$("#vote_down_sum").html('（' + data.answerSituation.ans_vote_down + '）');
					$("#comment_ind").val("true");
				}
			}
		})
	}
	
	function showCatalog(){
		var iTop = (window.screen.availHeight-30-500)/2;
		var iLeft = (window.screen.availWidth-10-500)/2;   
		str_feature = 'width=' + 450 + ',height=' + 500 + ',scrollbars=' + 'yes' + ',resizable=' + 'yes'+ ',top='+ iTop + ',left='+iLeft;
		prep_win = wbUtilsOpenWin('', 'tree_prep', false, str_feature);
		prep_win.location.href = '${ctx}/app/know/knowCatalogTree?que_id_lst=${know_detail.que_id}';
		prep_win.focus();
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
		    		    'uploader' : '${ctx}/app/upload/Know/Img;jsessionid=${pageContext.session.id}?Func=uploadwallpaper2Dfs',
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
			      			    	$("#qtip-uploadImgBoxQitp2").hide();
			      			    	$('#header-overlay-pc').hide();
			      			    })
			    	        	initFile("imgBox","Know");     
		    				}
		    	        }           
		    	    });

		        }
		    }
		    
		});
		
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
		initFile("imgBox","Know");
	
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
				url : '${ctx}/app/upload/Know/Img/online',
				async : false,
				type : 'post',
				dataType : 'json',
				data : {
					url : url
				},
				success : function(result){
					$("#uploadImgUrl").val('');
					$("#header-overlay-pc").hide();
					$("#qtip-uploadImgBoxQitp").hide();
					loadFile(result.tmf,result.path,".imgBox");
				}
			})
			
		});
	
	    
	    $('#header-overlay-pc').click(function(){
	    	$("#qtip-uploadImgBoxQitp").hide();
	    	$("#qtip-uploadImgBoxQitp2").hide();
	    	$('#header-overlay-pc').hide();
    	})
	    
	});
	
	function initFile(id,module){
		$("#"+id).empty();
		$.getJSON("${ctx}/app/upload/"+module+"/noMaster",function(result){
			var mtfs = result.mtfList;
			if(mtfs != undefined){
	            $.each(mtfs,function(i,val){
	            	loadFile(val,result.path,"."+id);
		        })
			}
		});
	}
	
	function loadFile(val,path,id){
	    var url,name;
		if(val.mtf_file_type=='url'){
			url = val.mtf_url;
			name = val.mtf_url; 
		} else {
			url =	contextPath+"/"+ path + "/" + val.mtf_file_rename;
			name = val.mtf_file_name;
		}
	    if(val.mtf_type == 'Img'){
	    	$(id+" ul").append($.render.upImgTemplate({
	            id : val.mtf_id,
				url : url
			}));
	    }
	}
	
	//=========================end================================
	//========================问答补充js==============================
	$(function(){
		$("#uploadImg2").qtip({
		    id : 'uploadImgBoxQitp2',
		    content: {
		        text: $("#uploadImgBox2")
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
		    	    $('#file_upload2').uploadify({
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
		    	            	$("#qtip-uploadImgBoxQitp2").hide();
		    	            	$("#header-overlay-pc").hide();
		    	            	Dialog.alert(fetchLabel("upload_tip_image"));
		    		            $("#file_upload2").uploadify('stop');
		    		            $("#file_upload2").uploadify('cancel', '*');
		    	            }
		    			},
		    			'onUploadComplete' : function() {  
		                    $("#file_upload2").uploadify("destroy");
		                    $("#qtip-uploadImgBoxQitp2").hide();
		                    $("#header-overlay-pc").hide();
		                },
		    	        'onUploadSuccess': function(file, data, response) {
		    	        	data = eval('(' + data + ')');
		    				if(data.status == 'fail'){
		    					Dialog.alert(data.errorMsg);
		    				}else{
			    	        	$('#header-overlay-pc').click(function(){
			    	        		$("#qtip-uploadImgBoxQitp").hide();
			      			    	$("#qtip-uploadImgBoxQitp2").hide();
			      			    	$('#header-overlay-pc').hide();
			      			    })
			    	        	initFile("imgBox2","KnowQuestion");   
		    				}
		    	        }           
		    	    });

		        }
		    }
		});

		$("#uploadImgBox2 .nav li").live('click',function(){
			$(this).addClass("active").siblings().removeClass("active");
			var index = $(this).index();
			if(index == 1){
				$("#content_2").hide();
				$("#content_3").show();
			} else {
				$("#content_3").hide();
				$("#content_2").show();
			}
		})
	
		//初始化文件列表
		initFile("imgBox2","KnowQuestion");
	
		//删除按钮的显示
		$(".imgBox2 img").live('mouseover',function(){
			$(this).next().find(".file-temp-del").show();
		})
		$(".imgBox2 img").live('mouseout',function(){
			$(this).next().find(".file-temp-del").hide();
		})
		$(".imgBox2 i").live('mouseover',function(){
			$(this).show();
		})		
		$(".imgBox2 i").live('mouseout',function(){
			$(this).hide();
		})
	
	    $("#uploadImgBtn2").live('click',function(){
			var url = $("#uploadImgUrl2").val();
			if(url == undefined || url == ''){
				Dialog.alert(fetchLabel("upload_tips_online"));
				$("#qtip-uploadImgBoxQitp2").hide();
				$('#header-overlay-pc').hide();
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
					$("#uploadImgUrl2").val('');
					$("#qtip-uploadImgBoxQitp2").hide();
					$('#header-overlay-pc').hide();
					loadFile(result.tmf,result.path,".imgBox2");
				}
			})
			
		});
	
	});
	
	//=========================end================================
	/* window.onload = function(){
		$("#imgBox").find("i").trigger("click"); //进来或刷新先删除掉以前上传的照片
		$("#imgBox2").find("i").trigger("click");
	} */
</script>
<script id="answer-template" type="text/x-jsrender">
<div class="wzb-wenda-list clearfix">           
    <div class="wzb-user wzb-user68">
            {{if !isTADM}}
                <a href="javascript:;"> 
                   <img class="wzb-pic" src="${ctx}{{>regUser.usr_photo}}">
                </a>
            {{else}}
                <span> 
                    <img class="wzb-pic" src="${ctx}{{>regUser.usr_photo}}">
                </span>
            {{/if}}
            {{if !isTADM}}
                <p style="display: none;" class="companyInfo">
                    <lb:get key="label_cm.label_core_community_management_29"/>
                </p>
                <div style="width: 60px; height: 60px;" class="cornerTL">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerTR">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerBL">&nbsp;</div>
                <div style="width: 60px; height: 60px;" class="cornerBR">&nbsp;</div>
            {{/if}}
    </div>

    <div class="wzb-wenda-content">
            {{if !isTADM}}   
                <span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><a class="wzb-link04" title="" href="javascript:;" class="mr20 skin-color">{{:regUser.usr_display_bil}}</a> 
            {{else}}
					<br/>
						<a class="wzb-link04" href="javascript:;" onclick="delThisAnswer({{>ans_id}}, {{>ans_create_ent_id}})">
							<lb:get key="know_del_answer"/>
						</a>
					<br/>
                <span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><span class="wzb-link04">{{>regUser.usr_display_bil}}</span>
            {{/if}}       
                
            {{if is_specify }}
                <i class="glyphicon glyphicon-star" style="color: #33CC33;" />
            {{/if}}
            <em class="wzb-wenda-time"> <span class="mr20 color-gray999"><lb:get key="know_ask_time"/>：</span>{{>ans_create_timestamp.substring(0,10)}}</em>
			{{if set_best_ind && !isTADM}}
					<br/>
				<div style="float:right;">
						<a class="wzb-link04" href="javascript:;" onclick="delThisAnswer({{>ans_id}}, {{>ans_create_ent_id}})">
							<lb:get key="know_del_answer"/>
						</a> |
                <a class="wzb-link04 " href="javascript:;" onclick="setBestAnswer({{>ans_id}}, {{>ans_create_ent_id}})">
                    <lb:get key="know_set_best_answer"/>
                </a>
				</div>
            {{/if}}
    
            <p class="color-gray666" id="ans_content_{{>ans_id}}">{{:ans_content}}</p> 
         
             <p class="color-gray666"><lb:get key="know_reference_material"/>：
             {{if ans_refer_content}}
              {{>ans_refer_content}}  
             {{else}} 
                --      
             {{/if}}
             </p> 
    </div>

   

</div>
</script>
<%-- 点赞
 <div class="wzb-wenda-bar">
        <a class="wzb-link05" href="javascript:;" ><i class="fa fa-thumbs-o-up"></i><p><lb:get key="rank_praise"/><span>{{>snsCount.s_cnt_like_count}}</span></p></a>
    </div>
 --%>
<script type="text/x-jsrender" id="relatedQuestionTempelate">
<li onclick="javascript:window.location.href = '${ctx}/app/know/knowDetail/{{:que_type}}/{{:enc_que_id}}';">
    <span class="pull-right margin-right15 color-gray999">{{:que_create_timestamp.substring(0,10) }}</span>
    <a href="/app/know/knowDetail/{{:que_type}}/{{:enc_que_id}}" class="wzb-link03"><i class="fa fa-square"></i>{{:que_title}}</a>
</li>
</script>
</head>
<body>
	<input type="hidden" id="answer_list_size" value="3">
	<div class="xyd-wrapper">
		<div id="main" class="xyd-main clearfix">
			<jsp:include page="knowMenu.jsp"></jsp:include>
			<div id="know_jsp" class="xyd-article">
					<c:if test="${!isTADM}">
						<div class="modtit fontfamily">
							<a class="wzb-link04" style="font-size: 16px;" href="${ctx}/app/know/allKnow" title=""><lb:get key="know_all_catalog"/></a><span style="font-size: 18px;color: #999;"> ></span>
							<a class="wzb-link04" href="${ctx}/app/know/allKnow/${parent_catalog.kca_id}/0">${parent_catalog.kca_title}</a>
							<c:if test="${child_catalog != null}">
								 <span style="font-size: 18px;color: #999;"> ></span> <a class="wzb-link04" href="${ctx}/app/know/allKnow/${parent_catalog.kca_id}/${child_catalog.kca_id}">${child_catalog.kca_title}</a>
							</c:if>
						</div>
					</c:if>

					<div class="wzb-wenda clearfix">
					<div class="wzb-wenda-tit fontfamily skin-bg">
					     <p>
					     	<c:choose>
					     		<c:when test="${know_detail.que_type == 'UNSOLVED'}">
					     			<i class="fa f38 fa-question"></i>
					     		</c:when>
					     		<c:when test="${know_detail.que_type == 'FAQ'}">
					     			<i class="fa f38 white" style="font-size: 30px;">FAQ</i>
					     		</c:when>
					     		<c:otherwise>
					     			<i class="wzb-wenda-size">OK</i>
					     		</c:otherwise>
					     	</c:choose>
					     </p>
					     <p><lb:get key="know_${know_detail.que_type}"/></p>
					</div>
					
					<c:if test="${know_detail.que_create_ent_id == my_usr_id or isTADM}">
						<form action="delKnowQuestion" method="post" id="delQueForm">
							<div class="wzb-wenda-tool">
								<div class="wzb-wenda-bd">
									<c:choose>
										<c:when test="${isTADM}">
											<span class="questip skin-color" onclick="showCatalog()"><lb:get key="know_change_catalog"/></span> | <a class="skin-color" href="javascript:;" onclick="delAnswer()"><lb:get key="button_del"/></a> 
											
											<!-- 改变分类框 -->
<!-- 											<div id="msgBox" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"> -->
<%-- 												<jsp:include page="../know/knowCatalogTree.jsp"/> --%>
<!-- 											</div> -->
											
										</c:when>
										<c:otherwise>
											<c:if test="${know_detail.que_type != 'SOLVED'}">
												<a class="skin-color" href="javascript:;" onclick="delAnswer()"><lb:get key="credits_ZD_CANCEL_QUE"/></a> | <a class="skin-color" href="javascript:;"  onclick="knowSupplement()"><lb:get key="know_requestion_supplement"/></a>
											</c:if>
										   	<!-- 问题补充框 -->
										   	<div class="questipList" id="knowSupplement">
										   		<div class="questipBg"><i class="fa fa-caret-up"></i></div>
										      	<div class="questipDesc">
										      		<c:choose>
										          		<c:when test="${know_detail.que_content != ''}"><c:set var="content" value="${know_detail.que_content}"/></c:when>
										          		<c:otherwise><c:set var="know_supplement_desc"><lb:get key="know_supplement_desc_1"/></c:set></c:otherwise>
										       		</c:choose>
										          	
										          	
										          	<div id="uploadImgBox2" style="display: none">
														<div>
															<ul class="nav nav-tabs">
															  <li class="active"><a href="javascript:;"><lb:get key="upload_img"/></a></li>
															  <li><a href="javascript:;"><lb:get key="online_img"/></a></li>
															</ul>
														</div>
														<div class="content">
															<div class="line" style="border:1px solid;"></div>
															<div style="padding: 10px 0 10px 0;" id="content_2">
																<p class="mb10 color-gray666"><lb:get key="upload_tip_image"/></p>
																<input type="file"  name="file_upload2" class="pull-left" id="file_upload2"/>
															</div>
															<div style="padding: 10px 0 10px 0;display: none;" id="content_3">
																<p class="mb10 color-gray666"><lb:get key="lab_group_online_addr"/></p>
																URL：<input type="text" class="wzb-inputText"  name="url" class="pull-left" id="uploadImgUrl2"/>
																<input type="button" id="uploadImgBtn2" class="wbtj btn wzb-btn-orange" value='<lb:get key="button_ok"/>' style="height: auto;padding: 3px 8px;margin-bottom:5px;"/>
															</div>
														</div>
													</div>
													<div class="">
						                                	<textarea id="queContent" class="queskang" style="height:120px;" placeholder="${know_supplement_desc}">${content}</textarea>  
						                                    
						                                    <div class="clearfix">
						                                         <div class="pull-left">
						                                              <span class="color-gray666"><lb:get key="global_attachment"/>：</span>
						                                              <a id="uploadImg2"  onclick="$('#header-overlay-pc').show();" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-image-o"></i><lb:get key="doing_image"/></a>
						                                         </div>
						                                   </div>
						                                   <div class="margin-top10 wzb-send-tool clearfix">
						                                         <div class="imgBox2 clearfix">
						                                              <ul id="imgBox2">
						                                                  <!-- 图片 -->
						                                              </ul>
						                                         </div>
						                                    </div>
						                            </div> <!-- send End-->
										  			<lb:get key="know_supplement_desc_2"/><input type="button" class="questj" name="myquesbtn" value='<lb:get key="button_ok"/>' onclick="changeQueContent()"/>    
										    	</div>
											</div> <!-- questipList End -->
										</c:otherwise>
									</c:choose>
								</div>     
							</div>
						</form>
					</c:if>
										
					<div class="wzb-wenda-info">
						 <h3><a class="color-gray333" href="###" title="">${know_detail.que_title}</a></h3>
					     <div id="que_supplement" style="width:100%;${know_detail.que_content != ''?'display:inline-block;':''}">
						     <c:if test="${know_detail.que_content != ''}">
						        <p style="float:left;"><span class="color-gray999"><lb:get key="label_cm.label_core_community_management_22"/>：</span><p style="line-height: 29px;">${know_detail.que_content}</p></p>
	     					 </c:if>
     					 </div>
     					 <div id="know_content_img">
     					 	<c:if test="${know_detail.fileList != null && fn:length(know_detail.fileList) > 0}">
	     					 	<c:forEach var="val" items="${know_detail.fileList }" varStatus="status">
	     					 		<c:choose>
								 		<c:when test="${val.mtf_file_type == 'url' }">
								 			<c:set var="url" value="${val.mtf_url}"></c:set>
								 		</c:when>
								 		<c:otherwise>
								 			<c:set var="url" value="${val.mtf_url}/${val.mtf_file_rename }"></c:set>
								 		</c:otherwise>
							 		</c:choose>
		     					 	<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${url}" target="_blank"><img width="60" src="${url}"/></a></p>
	     					 	</c:forEach>
     					 	</c:if>
     					 </div>
     					 <p class="margin-top15 color-gray999" style="clear:both;">
	     					<c:choose>
					            <c:when test="${isTADM}">
					                <lb:get key="label_cm.label_core_community_management_23"/>：<span class="wzb-link04">${know_detail.regUser.usr_display_bil}</span>
					            </c:when>
					            <c:otherwise>
					                <lb:get key="label_cm.label_core_community_management_23"/>：<a class="wzb-link04" href="javascript:;" title="">${know_detail.regUser.usr_display_bil}</a>
					            </c:otherwise>
					        </c:choose>  
					     	<span class="margin-left15">
					     		<lb:get key="label_cm.label_core_community_management_19"/>：<span class="color-gray333"><fmt:formatDate value="${know_detail.que_create_timestamp}" pattern="yyyy-MM-dd"/></span>
					     	</span>
							<span class="margin-left15"><lb:get key="label_cm.label_core_community_management_24"/>：<span class="color-gray333">${know_detail.ask_num}</span></span>
     					    <c:if test="${ !empty know_detail.que_bounty && know_detail.que_bounty!=0}">                                            
					            <span class="margin-left15 color-gray999"><lb:get key="know_bounty"/>：<span class="color-gray333">${know_detail.que_bounty}</span></span>
					        </c:if>
					     </p>
					     <c:if test="${know_detail.que_type != 'FAQ'}">
                         <p>
                         	<c:if test="${users!= null || fn:length(users) > 0}">
	                         	<span class="mr20 color-gray999">
					<!--                         指定回答 -->
					                <lb:get key="label_cm.label_core_community_management_25"/> ：
					                <span class="color-gray333"><c:if test="${empty  users}">--</c:if></span>
					                <c:forEach items="${users }" var="user" varStatus="status">
					                    <a class="wzb-link04" href="javascript:;" data="${user.usr_ent_id}" title=""> ${user.usr_display_bil }</a>
					                    <c:if test="${!status.last }"> &nbsp;|&nbsp; </c:if>                
					                </c:forEach>
					            </span>
                            </c:if>
                            <c:if test="${users == null && fn:length(users) == 0}">
                              <span class="mr20 color-gray999">
					                <lb:get key="label_cm.label_core_community_management_25"/> ： <span class="color-gray333">--</span>
					            </span>
                            </c:if>
                         </p>
                         </c:if>
					</div>
					</div> <!-- wdbar End-->
					
					<c:if test="${know_detail.que_type == 'SOLVED'}">
						<div class="wzb-wenda-area">
							<div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg02"><span>
									<lb:get key="know_best_answer"/>
							     </span></strong></div>
							<div id="bestAnswer" class="wzb-wenda-list clearfix">
                                <div class="wzb-user wzb-user68">
                                	<c:choose>
                                		<c:when test="${isTADM}">
							                <span> 
							                    <img class="wzb-pic" src="${ctx}${best_answer.regUser.usr_photo}">
							                </span>
                                		</c:when>
                                		<c:otherwise>
                                			<a href="javascript:;"> 
							                    <img class="wzb-pic" src="${ctx}${best_answer.regUser.usr_photo}">
							                </a>
                                			<p style="display: none;" class="companyInfo">
							                    <lb:get key="label_cm.label_core_community_management_29"/>
							                </p>
							                <div style="width: 60px; height: 60px;" class="cornerTL">&nbsp;</div>
							                <div style="width: 60px; height: 60px;" class="cornerTR">&nbsp;</div>
							                <div style="width: 60px; height: 60px;" class="cornerBL">&nbsp;</div>
							                <div style="width: 60px; height: 60px;" class="cornerBR">&nbsp;</div>
	                                	</c:otherwise>
                                	</c:choose>
                                </div>
							  	
							  	<div class="wzb-wenda-content">
							          	<c:choose>
							          		<c:when test="${isTADM}">
							          			<span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><span class="wzb-link04">${best_answer.regUser.usr_display_bil}</span>
							          		</c:when>
							          		<c:otherwise>
							          		   <span class="mr20 color-gray999"> <lb:get key="know_ask_user"/>：</span><a class="wzb-link04" title="" href="javascript:;" class="mr20 skin-color">${best_answer.regUser.usr_display_bil}</a> 
							          		</c:otherwise>
							          	</c:choose>
							          	<em class="wzb-wenda-time"> <span class="mr20 color-gray999"><lb:get key="know_ask_time"/>：</span><fmt:formatDate value="${best_answer.ans_create_timestamp}" pattern="yyyy-MM-dd"/></em>
							         
							          <p class="mt5 f14">${best_answer.ans_content}</p>
							          <c:if test="${best_answer.fileList != null && fn:length(best_answer.fileList) > 0}">
										 	<c:forEach var="val" items="${best_answer.fileList }" varStatus="status">
											 	<c:choose>
											 		<c:when test="${val.mtf_file_type == 'url' }">
											 			<c:set var="best_answer_url" value="${val.mtf_url}"></c:set>
											 		</c:when>
											 		<c:otherwise>
											 			<c:set var="best_answer_url" value="${val.mtf_url}/${val.mtf_file_rename }"></c:set>
											 		</c:otherwise>
										 		</c:choose>
											 	<p class="mt10 f14" style="display:inline-block;margin:0 10px 10px 0;"><a href="${best_answer_url}" target="_blank"><img width="60" src="${best_answer_url}"/></a></p>
										 	</c:forEach>
									 	</c:if>
							          
							          <c:if test="${best_answer.ans_refer_content != null and best_answer.ans_refer_content != ''}">
							          	<p class="mt5 grayC666"><lb:get key="know_reference_material"/>:${best_answer.ans_refer_content}</p>         
							    	  </c:if>
							    	   <c:if test="${best_answer.ans_refer_content == null or best_answer.ans_refer_content == ''}">
							          	<p class="mt5 grayC666"><lb:get key="know_reference_material"/>：--</p>         
							    	  </c:if>
							    </div>
							  <%-- 
							  	<c:if test="${!isTADM}">
							  		<div class="wzb-wenda-bar">
							  			<a class="wzb-link05" href="javascript:;" ><i class="fa fa-thumbs-o-up"></i><p><lb:get key="rank_praise"/><span>${best_answer.snsCount.s_cnt_like_count}</span></p></a>
								    </div>
							    </c:if>
							     --%>
							</div>
							<div class="wdarea-mess">
							     <span class="grayC333"><lb:get key="know_answer_good_ind"/></span>
							     <span class="mL5 grayC999"><lb:get key="know_currently"/><span class="mL5 mr5 f18 grayC333" id="evaluate_sum">${best_answer.ans_vote_total}</span><lb:get key="know_personal_evaluation"/></span>
							     <em class="beta_bg" onclick="changeAnsVote('true')"><lb:get key="know_good"/></em><span class="f14" id="vote_for_rate">${good_rate}</span><span class="grayC999" id="vote_for_sum">（${best_answer.ans_vote_for == null? 0 : best_answer.ans_vote_for}）</span>
							     <em class="beta_bg" onclick="changeAnsVote('false')"><lb:get key="know_no_good"/></em><span class="f14" id="vote_down_rate">${not_good_rate}</span><span class="grayC999" id="vote_down_sum">（${best_answer.ans_vote_down == null? 0 : best_answer.ans_vote_down}）</span> 
							</div>
						</div> <!-- wdposi End-->
						<script type="text/javascript">
			     			$(function(){
		     					$("#bestAnswer .wzb-wenda-bar:last a").like({
		     						count : ${best_answer.snsCount.s_cnt_like_count},
		     						flag : ${best_answer.is_user_like},
		     						id : ${best_answer.ans_id},
		     						module: 'Answer',
		     						tkhId : 0
		     					});
			     			})
			     		</script>
					</c:if>
					
					<div class="wzb-wenda-area">
						<c:choose>
							<c:when test="${know_detail.que_type == 'FAQ'}">
								<div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg02"><span>
									<lb:get key="know_faq_answer"/>
							     </span></strong></div>
					     		<div id="faqAnswer">
					     			<div class="wzb-wenda-list clearfix">
					     				<div class="wzb-user wzb-user68">
					     				<a class="wzb-pic" href="javascript:;">  <img class="wzb-pic" src="${ctx}${answer.regUser.usr_photo}">
					     					</a>
					     				   <!--   头像显示不全
					     					<a class="wdarea-pic" href="javascript:;"><img src="${ctx}${answer.regUser.usr_photo}"/></a>
								  		   -->
								  		</div>
					     				<div class="wzb-wenda-content">
				     						<span class="mr20 color-gray999"><lb:get key="know_ask_user"/>：</span><span class="wzb-link04">${answer.regUser.usr_display_bil}</span> 
				     						<em class="wzb-wenda-time"> <span class="mr20 color-gray999"><lb:get key="know_ask_time"/>：</span><fmt:formatDate value="${answer.ans_create_timestamp}" pattern="yyyy-MM-dd"/></em>
											<p class="color-gray666">${answer.ans_content}</p>
										</div>
										<%-- 
										<c:if test="${!isTADM}">
											<div class="wzb-wenda-bar">
												<a class="wzb-link05" href="javascript:;" ><i class="fa fa-thumbs-o-up"></i><p><lb:get key="rank_praise"/><span>${answer.snsCount.s_cnt_like_count}</span></p></a>
											</div>
										</c:if>
										 --%>
									</div>
									<script type="text/javascript">
						     			$(function(){
					     					$("#faqAnswer .wzb-wenda-bar:last a").like({
					     						count : ${answer.snsCount.s_cnt_like_count},
					     						flag : ${answer.is_user_like},
					     						id : ${answer.ans_id},
					     						module: 'Answer',
					     						tkhId : 0
					     					});
						     			})
						     		</script>
								</div>
							</c:when>
							<c:otherwise>
								<div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg01"><span><lb:get key="label_cm.label_core_community_management_26"/></span></strong> <em id="wbshow" class="fr state skin-color" style="float:right;cursor:pointer;" onclick="changeSize()">
					            <lb:get key="label_cm.label_core_community_management_27"/><i class="fa mL3 fa-angle-down"></i></em></div>
					            <div id="answer_list"></div>
							</c:otherwise>
						</c:choose>
					</div> <!-- wdarea End-->
					
					<div class="header-overlay-pc" id="header-overlay-pc" style="display: none;"></div>
					<c:if test="${know_detail.que_type == 'UNSOLVED' and !isTADM}">
						<div class="wzb-wenda-area">
						     <div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg02"><span>
							<!--      我的回答 --><lb:get key="label_cm.label_core_community_management_14"/>
							     </span></strong></div>
						     
						     <table>
						<!--              发表信息 -->
						           <tr>
						             <td colspan="2">
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
					             		<div class="wzb-send">
			                                <form action="#" method="post">      
			                                    <%-- <textarea id="formText" placeholder="<lb:get key="doing_input_comment_desc"/>" class="wzb-textarea-04"><lb:get key="doing_input_comment_desc"/></textarea> --%>
			                                    <textarea placeholder="<lb:get key='label_core_community_management_127'/>"  prompt="<lb:get key='label_core_community_management_127'/> " name="ansContent" class="wzb-textarea-04" style="" id="formText"></textarea>
			                                    <span class="color-gray666"><lb:get key='label_core_community_management_221'/></span>
			                                    
			                                    <div class="wzb-send-tool clearfix">
			                                         <div class="imgBox clearfix">
			                                              <ul id="imgBox">
			                                                  <!-- 图片 -->
			                                              </ul>
			                                         </div>
			                                        
			                                        <!--  <div class="docBox" id="docBox" style="">
			                                               文件
			                                         </div> -->
			                                    </div>
			                                   
			                                    <div class="margin-top10 clearfix">
			                                         <div class="pull-left">
			                                              <span class="color-gray666"><lb:get key="global_attachment"/>：</span>
			                                              <a id="uploadImg" onclick="$('#header-overlay-pc').show();" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-image-o"></i><lb:get key="doing_image"/></a>
			                                             <%--  <a id="uploadDoc" href="javascript:;" class="wzb-link03 margin-right15"><i class="fa fa-file-word-o"></i><lb:get key="doing_doc"/></a>
			                                              <a id="uploadVideo" href="javascript:;" class="wzb-link03"><i class="fa fa-file-video-o"></i><lb:get key="doing_video"/></a> --%>					    
			                                         </div>
			                                         <%-- <div class="pull-right">
			                                              <input class="btn wzb-btn-yellow" id="formCancel" type="button" value='<lb:get key="button_cancel"/>'/>
			                                              <input class="btn wzb-btn-yellow" id="formSubmit" type="button" value='<lb:get key="btn_post"/>'/>
			                                         </div> --%> 		
			                                   </div>
			                               </form>
			                            </div> <!-- send End-->
						             
						             </td>
						        </tr>
						        
						        <tr>
						             <td class="wzb-form-label">
						<!--              参考资料 -->
						                <lb:get key="know_reference_material"/>：
						             </td>
						             
						             <td class="wzb-form-control">
						                 <div class="wzb-selector"><input type="text" value="" name="ansReferContent" class="form-control"></div>
						             </td>
						        </tr>
						        
						        <tr>
						             <td class="wzb-form-label"></td>
						             
						             <td class="wzb-form-control">
						<!--                  如果您的回答是从其它地方引用，请表明出处。限 255 个字以内。 --><lb:get key="know_reference_material_desc"/>
						             </td>
						        </tr>
						        
						        <tr>
						             <td class="wzb-form-label"></td>
						             
						             <td class="wzb-form-control">
						                 <input type="button" onclick="addKnowAnswer()" class="btn wzb-btn-blue wzb-btn-big margin-right15" value="<lb:get key='global.button_submit'/>" name="frmSubmitBtn">
						             </td>
						        </tr>
						    </table>
						</div> <!-- wdarea End-->
					</c:if>

					<c:if test="${!isTADM}">
						<div class="wzb-wenda-area">
						     <div class="wzb-wenda-title"><strong class="wzb-wenda-break wzb-wenda-bg03"><span>
						<!--      相关问题 --><lb:get key="label_cm.label_core_community_management_28"/>
						     </span></strong></div>
						         
						     <ul class="wzb-list-10" id="know_list">
						<!--         相关问题 -->
						     </ul>
						</div> <!-- wzb-wenda-area End-->
					</c:if>
			</div> <!-- xyd-article End-->
		</div>
	</div>	<!-- xyd-wrapper End-->
</body>
</html>
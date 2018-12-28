<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="${ctx}/js/urlparam.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/base.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/learner.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/static/theme/blue/css/style.css"/>
<!-- 编辑器插件 -->
<script type="text/javascript" charset="utf-8" src="${ctx}/static/ueditorjs/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/static/ueditorjs/ueditor.all.js"> </script>
<%-- <script type="text/javascript" charset="utf-8" src="${ctx}/static/ueditorjs/lang/en/en.js"> </script>
<script type="text/javascript" charset="utf-8" src="${ctx}/static/ueditorjs/lang/zh-cn/zh-cn.js"> </script> --%>

<script type="text/javascript" charset="utf-8" src="${ctx}/static/js/layer/layer.js"> </script>
<script type="text/javascript" charset="utf-8" src="${ctx}/static/js/yujiazai.js"> </script>
<link rel="stylesheet" type="text/css" href="${ctx}/static/js/layer/skin/layer.css"/>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-default.css" />
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-admin.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>




<title>wizbank学习 帮助中心</title>
<!-- <style type="text/css">
.datatable .datatable-pager {
	width: 227px;
	margin-left: -17px;
}
</style> -->

<script type="text/javascript">
	// 问题类型
	var questionTypeAct = 'save';
	var questionAct = 'save';
	var que_id = 0;
	var catalog;
	var dt;
	$(function(){
		 $('#questionTypeForm').validate({
			rules:{
				 hqt_type_name:"required",
				 hqt_top_index : {
				       required:true,
				       number:true
				 }
			},
			messages:{
				 hqt_type_name:"类型标题不能为空",
				 hqt_top_index : {
				       required:"置顶指数不能为空",
				       number:"请输入有效数字"
				 }
			}
			
		}); 
		
		  $('#questionForm').validate({
			rules:{
				hqt_number:"required",
				hq_title :"required",
				hq_type_id :"required"
				/* hq_width :{
				       required:true,
				       number:true
				 } */
			},
			messages:{
				hqt_number:"编号不能为空",
				hq_title :"标题不能为空",
				hq_type_id :"所属功能模板不能为空"
				/* hq_width : {
				       required:"提示信息宽度不能为空",
				       number:"请输入有效数字"
				} */
			}
			
		}); 
		
		$('#question_type li a').click(function(){
			$('#question_type li a').removeClass();
			$(this).addClass("cur"); 
		});
		
		//类型目录
        var catalogModel = [ {
            display : fetchLabel('label_core_knowledge_management_17'),
            format : function(data) {
                var title = '';
                var css = ''
                title = data.type_name
                if (que_id == data.id) {
                    css = 'active'
                }
                p = {
                    name : title,
                    id : data.id,
                    isAdmin : ${is_admin},
                    css : css
                }
                return $('#quetype-template').render(p);
            }
        } ]
		
       //提示点
        var dtModel = [ {
            display : '<input id="kbi_all" onclick="getCheckedIds()" type="checkbox" value="" name="kbi_all_checkbox" />' +'编号',
            format : function(data) {
            	//console.log(data)
              // debugger
                p = {
                    hq_id : data.hq_id,
                    text  : data.hqt_number
                }
                return $('#input-template').render(p);
            }
        }, {
            display : '标题',
            format : function(data) {
               
                p = {
                    text : data.hq_title
                }
                return $('#text-center-template').render(p);
            }
        }, {
            display : '操作',
            format : function(data) {
                p = {
                   hq_id : data.hqt_number
                }
                return $('#upd-template').render(p);
            }
        } ]
		
		
		
        catalog = $("#question_type").table({
            url : '${ctx}/app/help/admin/indexJson',
            colModel : catalogModel,
            rp : 10,
            showpager : 3,
            hideHeader : true,
            usepager : true,
            useCss : "list-group wzb-list-1"
        });
		
          dt = $("#question_context").table({
            url : '${ctx}/app/help/questions',
            colModel : dtModel,
            rp : 10,
            hideHeader : false,
            usepager : true,
            params : {
            	isAdmin : 'true',
            	typeId : '',
            	searchContext : ''
            },
            sortorder : 'desc'
        });   
         
         
		//$('#type_item_0').click();
	});

	//弹出层文本请求
	
	//全选 
	 function getCheckedIds() {
        var isChecked = $('#kbi_all').attr("checked");
        if (!isChecked) {
            $('input[name="kbi_checkbox"]').each(function() {
                $(this).attr("checked", false);
            });
        } else {
            $('input[name="kbi_checkbox"]').each(function() {
                $(this).attr("checked", true);
            });
        }
    }
	//批量删除
	 function delQtByIds() {
	        var ids = '';
	        $('input[name="kbi_checkbox"]:checked').each(function() {
	            ids += $(this).val() + ',';
	        });
	        if (ids == '') {
	            Dialog.alert('未选择需要删除的内容!');
	        } else {
	            if (confirm('确认删除选中内容？')) {
	                 $.post("${ctx}/app/help/admin/deleteByIds", {
	                    ids : ids
	                }, function() {
	                	returnToQuestionList();
	                    $('input[name="kbi_all_checkbox"]').each(function() {
	                        $(this).attr("checked", false);
	                    });
	                }); 
	            }
	        }
	    }
	
	
	
	function getQuestions(isHot,btn,typeId,searchContext){
		   
	        $(dt).reloadTable({
	            params : {
	            	isAdmin : 'true',
	            	typeId : typeId,
	            	searchContext : searchContext
	            },
	            sortname : 'hq_id',
	            sortorder : 'desc'
	        });
	        $('#question_edit').hide();
			$('#question_context').show();
			$('#question_context_top').show();
			$('#show-helpdome').hide()
		
	}
	
	
	
	
	function addQuestionType(){
		cleanQuestionTypeForm();
		questionTypeAct = 'save';
	    $("#qtip-overlay").show();
	    $(".kuang").show();
	    $(".kuang #question_type_form_title").html("添加功能模块");
	}
		
	function closeQuestionTypeForm(){
        $("#qtip-overlay").hide();
        $(".kuang").hide();
	}
	
	
	//修改分类
	function editQuestionType(id){
		
		
		questionTypeAct = 'update';
		$(".kuang #del_question_type_btn").css('display','');
		$(".kuang #question_type_form_title").html("修改功能模块");
		$.ajax({
			url:'${ctx}/app/help/questionType/' + id,
			type:'post',
			async:true,
			dataType : 'json',
			success:function(data){
			    $("#qtip-overlay").show();
			    $(".kuang").show();
			    $(".kuang #hqt_type_name").val(data.type_name);
			    $(".kuang #hqt_top_index").val(data.top_index);
			    $(".kuang #hqt_id").val(data.id);
			    $(".kuang #hqt_language").val(data.language);
			    $(".kuang #hqt_is_publish").val(data.is_publish);
			}
		});
	}
	
	function cleanQuestionTypeForm(){
	    $(".kuang #hqt_type_name").val('');
	    $(".kuang #hqt_top_index").val('');
	    $(".kuang #hqt_id").val('');
	    $(".kuang #hqt_language").val('zh-cn');
	    $(".kuang #hqt_is_publish").val('1');
	    $(".kuang #del_question_type_btn").css('display','none');
	}
	
	function questionSearch(){
		var selTypeItemId =  $('#sel_type_item_id').val();
		var selBtn = $('#type_item_'+selTypeItemId);
		var searchContext = $('#question_search_context').val();
		getQuestions($("#is_hot").val(),$(selBtn),selTypeItemId,searchContext);
		$('#question_context').show();
		$('#question_edit').hide();
	}
	
	function quesionDetail(questionId){
		$.ajax({  
	        type:'get',      
		    url: "${ctx}/app/help/question/"+questionId,
		    cache:false,  
		    dataType:'json',  
		    success:function(data){  
				var p = {
						isAdmin : ${is_admin},
			 			question : data
			 	 };
				var html = $('#questionDetailTemplate').render(p);	
				//console.log(data)
				$("#question_context").html(Wzb.htmlDecode(html).replace(/&amp;nbsp;/g,'&nbsp'));
		    	
		    }  
		});  
	}
	
	function returnToQuestionList(){
		$('#type_item_0').click();
		$('#question_edit').hide();
		$('.xyd-sidebar').show();
		$('.xyd-article').show();
		/* var selTypeItemId =  $('#sel_type_item_id').val();
		var selBtn = $('#type_item_'+selTypeItemId);
		getQuestions($("#is_hot").val(),$(selBtn),selTypeItemId); */
	}
	
	function questionPublish(questionId,publishAct){
		$.ajax({  
	        type:'post',      
		    url: "${ctx}/app/help/question/publish/"+questionId,
		    cache:false,  
		    dataType:'json',  
		    data:{
		    	publishAct : publishAct
		    },  
		    success:function(data){  
		    	if(data.success==true){
					var p = {
							isAdmin : ${is_admin},
				 			question : data
				 	 };
					var html = $('#questionDetailTemplate').render(p);
					$("#question_context").html(Wzb.htmlDecode(html));
					returnToQuestionList();
		    	}else{
		    		alert('发布失败');
		    	}
		    }  
		});  
	}
	function yuyin(name){
		 var shijian = name.children('p').children('audio')[0].duration/60;
			layer.tips(shijian.toFixed(2)+'s  点击播放',name.get(0),{
				tips: [1, 'rgba(128,128,128,0.9)']
			});
	}
	function yuyinPlay(name){
		name.children('p').children('audio')[0].play();
	}

	function tanhao(name,yu){
		if(yu == 1){
			layer.tips(UE.getEditor('editor').getContentTxt(),name,{
				tips: [1, 'rgba(128,128,128,0.9)']
			});
		}else{
			layer.tips(UE.getEditor('editor1').getContentTxt(),name,{
				tips: [1, 'rgba(128,128,128,0.9)']
			});
		}
		
	}
	
	function bianji(){
		//编辑器预览
		$('div[title=预览]').unbind('click');
		$('div[title=Preview]').unbind('click')
		
		
		$('div[title=Preview]').click(function(){//英文版
			var fangshi = $('.wzb-table-title.moban_active .cur').attr('data-name');
			var namewidth = $('.wzb-table-title.moban_active .cur').attr('data-moban').split(',')[0];
			var nameheight = $('.wzb-table-title.moban_active .cur').attr('data-moban').split(',')[1];
			var Ucontent = Ucontent = '<div style="padding:20px 20px 0 20px;width:100%">\
	 		 	'+UE.getEditor('editor1').getContent()+'\
	 		 	<p class="res" style="color:#fff;position: absolute;top:0px">'+$('#hqt_number').val()+'</p>\
	 		 	</div>'
			var typeOpen = 1;
	 		var ifdirection;
	 		//console.log(fangshi)
			switch(fangshi){
				case 'yuyin':
					Ucontent = '<div style="padding:20px 20px 0 20px;width:100%"><div style="margin:0 auto" onclick="yuyinPlay($(this))" onmouseover = "yuyin($(this))" class="yin">\
 		 				'+UE.getEditor('editor1').getContent()+'\
						</div><p class="res" style="color:#fff;position: absolute;top:0px">'+$('#hqt_number').val()+'</p>\
 		 			</div>'
 		 			break;
				case 'moban_left':
					typeOpen = 2
					ifdirection = 4
					break;
				case 'moban_right':
					typeOpen = 2
					ifdirection = 2
					break;
				case 'redtan':
					Ucontent = '<span style="height:20px;float:left" class="redtan"></span>'+UE.getEditor('editor1').getContent()
					break;
				case 'yellowtan' :
					Ucontent = '<span onclick="tanhao(this,2)" style="width:20px;height:20px;display:block;margin:0 auto;margin-top:100px;" class="yellowtan"></span>'
					break;
				case 'fankui':
					Ucontent = '<div style="width:549px;margin:0 auto"><div class="fankui"></div><div class="fankui-right"><h3 style="color:#999">操作成功</h3>'+UE.getEditor('editor1').getContent()+'</div></div>'
					break;
				case 'gong' :
					Ucontent = '<div class="gong"><div style="width:670px;">'+UE.getEditor('editor1').getContent()+'</div></div>'
					break;
			}
			
		if(typeOpen == 2){
			//console.log(this)
			layer.tips(UE.getEditor('editor1').getContentTxt(), this, {
				  tips: [ifdirection, 'rgba(128,128,128,0.9)'] //还可配置颜色
				});
		}else{
			layer.open({
		 		type: 1,
			  	title: false,
			  	closeBtn: 1,
			  	type:1,
			  	shadeClose: true,
		 	 	 skin: 'demo-class', //加上边框
		 	 	 area: [namewidth+'px',nameheight+'px'], //宽高
		 		 content:Ucontent
		 		});
		}
	})
		
	
	
		
		$('div[title=预览]').click(function(){//中文版
			var fangshi = $('.wzb-table-title.moban_active .cur').attr('data-name');
			var namewidth = $('.wzb-table-title.moban_active .cur').attr('data-moban').split(',')[0];
			var nameheight = $('.wzb-table-title.moban_active .cur').attr('data-moban').split(',')[1];
			var Ucontent = Ucontent = '<div style="padding:20px 20px 0 20px;width:100%">\
	 		 	'+UE.getEditor('editor').getContent()+'\
	 		 	<p class="res" style="color:#fff;position: absolute;top:0px">'+$('#hqt_number').val()+'</p>\
	 		 	</div>'
			var typeOpen = 1;
	 		var ifdirection;
	 		//console.log(fangshi)
			switch(fangshi){
				case 'yuyin':
					Ucontent = '<div style="padding:20px 20px 0 20px;width:100%"><div style="margin:0 auto" onclick="yuyinPlay($(this))" onmouseover = "yuyin($(this))" class="yin">\
 		 				'+UE.getEditor('editor').getContent()+'\
						</div><p class="res" style="color:#fff;position: absolute;top:0px">'+$('#hqt_number').val()+'</p>\
 		 			</div>'
 		 			break;
				case 'moban_left':
					typeOpen = 2
					ifdirection = 4
					break;
				case 'moban_right':
					typeOpen = 2
					ifdirection = 2
					break;
				case 'redtan':
					Ucontent = '<span style="height:20px;float:left" class="redtan"></span>'+UE.getEditor('editor').getContent()
					break;
				case 'yellowtan' :
					Ucontent = '<span onclick="tanhao(this,1)" style="width:20px;height:20px;display:block;margin:0 auto;margin-top:100px;" class="yellowtan"></span>'
					break;
				case 'fankui':
					Ucontent = '<div style="width:549px;margin:0 auto"><div class="fankui"></div><div class="fankui-right"><h3 style="color:#999">操作成功</h3>'+UE.getEditor('editor').getContent()+'</div></div>'
					break;
				case 'gong' :
					Ucontent = '<div class="gong"><div style="width:670px;">'+UE.getEditor('editor').getContent()+'</div></div>'
					break;
			}
			
		if(typeOpen == 2){
			//console.log(ifdirection)
			layer.tips(UE.getEditor('editor').getContentTxt(), this, {
				  tips: [ifdirection, 'rgba(128,128,128,0.9)'] //还可配置颜色
				});
		}else{
			layer.open({
		 		type: 1,
			  	title: false,
			  	closeBtn: 1,
			  	type:1,
			  	shadeClose: true,
		 	 	 skin: 'demo-class', //加上边框
		 	 	 area: [namewidth+'px',nameheight+'px'], //宽高
		 		 content:Ucontent
		 		});
		}
	})
}
	
	
	
	function addQuestion(){
		questionAct = 'save';
		$('.xyd-sidebar').hide();
		$('.xyd-article').hide();
		$('#question_edit').show();
		cleanQuestion();
		$('#question_form_title').html('添加提示信息');
		//console.log($('.moban_active'))
		$('.moban_active').each(function(){
			$(this).children().children('span').attr('class','');
			$(this).children().children('div').children('span').attr('class','');
		})
		UE.getEditor('editor').setContent('')
		UE.getEditor('editor1').setContent('')
		bianji();
	
		
	}
	
	function cleanQuestion(){
    	$("#questionForm #hqt_number").val('');
    	$('#questionForm #hq_title').val('');
    	$("#questionForm #hq_width").val('');
    	$('#questionForm #hq_height').val('');
    	//$("#questionForm #hq_language").val('zh-cn');
    	//$("#questionForm #hq_is_publish").val('1');
    	//UE.getEditor('editor').setContent(replaceHtmlTag(''), isAppendTo);
		//UE.getEditor('editor1').setContent(replaceHtmlTag(''), isAppendTo);
    	$("#questionForm #hq_type_id option:eq(1)").attr('selected','selected');
    	//$("#questionForm #hq_top_index").val('');
    	//$("#questionForm input[name='hq_is_hot'][value='1']").attr('checked','true');
    	//$("#questionForm #del_question_btn").css('display','none');
    	$('#error_hq_content').css('display','none');
	}
	
	
	//修改问题  获取问题详情 请在这里获取 
	function editQuestion(questionId,isAppendTo){
	 	questionAct = 'update';
	 	$('#error_hq_content').css('display','none');
	 	$('#question_context_top').css('display','none');
		$("#questionForm #del_question_btn").css('display','');
		
	$('.xyd-article').hide()
	$('.xyd-sidebar').hide()
		$('#question_form_title').html('修改提示信息');
		$.ajax({  
	        type:'get',      
		    url: "${ctx}/app/help/question/"+questionId,
		    cache:false,  
		    dataType:'json',  
		    success:function(data){  
				$('#question_context').hide();
				$('#question_edit').show();
				
		    	$("#questionForm #hq_id").val(data.hq_id);
		    	$('#questionForm #hqt_number').val(data.hqt_number);
		    	//editor.html();
		    	UE.getEditor('editor').setContent(replaceHtmlTag(data.hq_content_cn), isAppendTo);
		    	UE.getEditor('editor1').setContent(replaceHtmlTag(data.hq_content_us), isAppendTo);
		    	
		    	$("#questionForm #hq_title").val(data.hq_title);
		    	$("#questionForm #hq_width").val(data.hq_width);
		    	//$("#questionForm #hq_type_id").val(data.hq_type_id);
		    	$("#questionForm #hq_height").val(data.hq_height);
		    	
		    	//hq_template
		    	$('.wzb-table-title.moban_active span').each(function(){
		    		if($(this).attr('data-moban') == data.hq_template){
		    			$('.wzb-table-title.moban_active span').attr('class','')
		    			$(this).prevAll().removeClass('cur')
						$(this).nextAll().removeClass('cur')
		    			$(this).attr('class','cur')
		    		}
		    	})
		    	//$("#questionForm #hq_type_id option:eq("+data.hq_type_id+")").attr('selected','selected');
		    	$("#questionForm #hq_type_id option[value='"+data.hq_type_id+"']").attr("selected",true);
		    	//$("#questionForm input[name='hq_type_id'][value='"+data.hq_type_id+"']").attr('checked','true');
		    }  
		});  
		bianji()
	}
	
	function replaceHtmlTag(context){
		context = context.replace(/&lt;/g,"<");
		context = context.replace(/&gt;/g,">");
		context = context.replace(/&amp;/g,"&");
		context = context.replace(/&nbsp;/g," ");
		return context;
	}
	
</script>

<!-- template start -->
<script id="questionTemplate" type="text/x-jquery-tmpl">
            <div class="xyd-search" style="width:100%;">
				{{if isAdmin}}
                  <input type="button" class="btn wzb-btn-orange pull-right" value="<lb:get key='label_core_community_management_204'/>" 
					onclick="addQuestion()">
				{{/if}}
                <form class="form-search form-souso pull-right" style="margin:-1px 5px 0 0;">
                    <input name="question_search_context" type="text" class="form-control" 
							placeholder="<lb:get key='label_core_community_management_203'/>" value="{{:searchContext}}"
							id="question_search_context">
					<input type="button" class="form-button" value="" onclick="questionSearch()">
                </form>
            </div>
            <h4>{{:question_type_title}}</h4>
            <ul class="wzb-list-30">
				{{if typeList && typeList.length}}
					{{for typeList }}
						<li><a class="wzb-link02" href="javascript:void(0)" onclick="quesionDetail({{:hq_id}})">{{:hq_title}}</a></li>
					{{/for}}
				{{/if}}
            </ul>
</script>

<script id="questionDetailTemplate" type="text/x-jquery-tmpl">
            <div class="xyd-search" style="width:100%;">
				{{if isAdmin}}
                <input type="button" class="btn wzb-btn-orange pull-right" value="<lb:get key="button_update"/>" onclick="editQuestion({{:question.hq_id}})">
				
                <form class="form-search form-souso pull-right" style="margin:-1px 5px 0 0;">     
					{{if question.hq_is_publish}}
                		<input type="button" class="btn wzb-btn-orange" value="<lb:get key="global_unpublish"/>" onclick="questionPublish({{:question.hq_id}},0)">
					{{else}}
						<input type="button" class="btn wzb-btn-orange" value="<lb:get key="global_publish"/>" onclick="questionPublish({{:question.hq_id}},1)">
					{{/if}}
                </form>
				{{/if}}
                <form class="form-search form-souso pull-right" style="margin:-1px 5px 0 0;">     
                	<input type="button" class="btn wzb-btn-orange" value="<lb:get key="button_back" />" onclick="returnToQuestionList()">
                </form>
            </div>
            <h4 style="padding-bottom: 10px;border-bottom: 1px dotted #eee;">{{:question.hq_title}}</h4>
            <p>{{>question.hq_content}}</p>

</script>

</head>
<body>

<div class="xyd-wrapper">
    <div class="xyd-main clearfix" id="main">
        <div class="xyd-sidebar">
            <h3 class="wzb-title-4 skin-bg"><lb:get key="menu_help" /><i class="fa fa-sanzuo fa-caret-up"></i></h3>
            <c:if test="${is_admin == true}">
            	<span class="button-center"><input type="button" class="btn wzb-btn-orange wzb-btn-big" value="<lb:get key="label_core_community_management_202" />" onclick="addQuestionType()"></span>
            </c:if>
            <ul class="wzb-list-17 datatable" id="question_type">
            	<!-- 热门问题 -->
                <li>
                	<a class="cur" href="javascript:void(0)" title="" onclick="getQuestions(true,this,0)" id="type_item_0">
                		<lb:get key='label_cm.label_core_community_management_7'/>
                	</a>
                </li>
           	    <%-- <c:forEach items="${type_list}" var="type_item">
					<li>
						<a href="javascript:void(0)" title="" id="type_item_${type_item.id}" onclick="getQuestions(false,this,${type_item.id})">
						  <c:if test="${is_admin == true}">
							<i class="fa skin-color fa-pencil-square-o" onclick="editQuestionType(${type_item.id})"></i>
						  </c:if>
						  <c:out value="${type_item.type_name}"></c:out>
						</a>
					</li>
			 	</c:forEach> --%> 
			 	
			 	<!--弹出  -->
			 	<!-- <li class="yuzhan cur">
			 		<a onclick='ifname(this)' data-name='玩玩我,100,100,2,10'>aa</a>
			 	
				</li> -->
			 	
			 	
			 	<!--弹出end  -->
			 	
			 	<a show-msg-class='ask-big' show-msg-code='33333' show-msg-event='click' show-msg-language='zn'></a>
			 	<a show-msg-class='ask-big' show-msg-code='aaaaaa' show-msg-event='click' show-msg-language='zn'></a>
			 	<a show-msg-class='ask-big' show-msg-code='666' show-msg-event='click' show-msg-language='zn'></a>
			 	
			 	
			 	
			 	<input type="hidden" id="is_hot"/>
			 	<input type="hidden" id="sel_type_item_id"/>
            </ul>
        </div>
        <div class="xyd-article" style="position: relative">
            <div id='question_context_top'>
            
            <a id='zhanshi' >展示</a>
            <script>
            	$('#zhanshi').click(function(){
            		layer.open({
            			  type: 2,
            			  title: '帮助展示',
            			  shadeClose: true,
            			  shade: 0.8,
            			  area: ['90%', '90%'],
            			  content: '${ctx}/app/help/vip'
            			}); 
            	})
            </script>
	               <div class="xyd-search" style="width:100%;">
	                  <input type="button" class="btn wzb-btn-orange" style="float:right;" value="<lb:get key='label_core_community_management_204'/>" 
						onclick="addQuestion()">
						
					<input type="button" class="btn wzb-btn-orange margin-right4" style="float:right;" value="删除" 
						onclick="delQtByIds()">	
	                <form class="form-search form-souso pull-right" style="margin:2px 4px 2px 0;">
	                    <input name="question_search_context" type="text" class="form-control" style="float:left;"
								placeholder="搜索" value=""
								id="question_search_context">
						<input type="button" class="form-button" value="" onclick="questionSearch()" style="float:left;">
	                </form>
	            </div>
	            <h4>全部</h4>
            </div>
        	 <div id='question_context'>
        		
        	</div>  
        
			<jsp:include page="questionType.jsp"></jsp:include> 
			
			
		
        </div>
        <div id='question_edit' style="padding:20px;background:#fff;display: none;"><jsp:include page="question.jsp"></jsp:include></div>
        </div>
        
    </div>
    
</div> <!-- xyd-wrap End-->


<script id="quetype-template" type="text/x-jsrender">
   <li>
	<a href="javascript:void(0)" title="" id="type_item_{{>id}}" onclick="getQuestions(false,this,{{>id}})">
		 {{if isAdmin == true }}
			<i class="fa skin-color fa-pencil-square-o" onclick="editQuestionType({{>id}})"></i>
	     {{/if}}
		{{>name}}
	</a>
   </li>
</script>
<script id="text-center-template" type="text/x-jsrender">
        <div>{{>text}}</div>
</script>
<script id="input-template" type="text/x-jsrender">
  <input type="checkbox" name="kbi_checkbox" value="{{>hq_id}}" />
  {{>text}}
</script>

<script id="upd-template" type="text/x-jsrender">
  <input type="button" class="btn wzb-btn-orange " value="预览" onclick = 'tishi($(this))' >
  <input type="button" class="btn wzb-btn-orange " value="<lb:get key="button_update"/>" onclick="editQuestion('{{>hq_id}}')">
</script>
<script>



		function tishi(name){
				$.ajax({
					 	type:'get',      
					    url: "/app/help/question/"+name.parents('.datatable-table-row').children('td:nth-child(1)').text().replace(/^\s+|\s+$/g,""),
					    cache:false,  
					    dataType:'json',  
					    success:function(data){
					    	var nameshowWidth = data.hq_template.split(',')[0]==1?100:data.hq_template.split(',')[0];
			 				var nameshowHeight = data.hq_template.split(',')[1]==1?100:data.hq_template.split(',')[1];
			 				var tmp = data.hq_template.split(',')[3];
			 				
			 				//console.log(tmp)
			 				
			 				if(tmp == 'butong'){
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [nameshowWidth +"px",nameshowHeight+'px'], //宽高
				 			 		content: '<div style="padding:20px 20px 0 20px;width:100%">\
		 			 					'+Wzb.htmlDecode(data.hq_content_cn)+'\
		 			 						<p class="res" style="color:#fff;position: absolute;top:0px">\
		 			 							'+data.hqt_number+'\
		 			 						</p>\
		 			 				  </div>'
				 			 		});
			 				}else if(tmp == 'red'){
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [nameshowWidth +"px",nameshowHeight+'px'], //宽高
				 			 		content: '<span style="height:20px;float:left" class="redtan"></span>'+Wzb.htmlDecode(data.hq_content_cn)
				 			 		});
			 				}else if(tmp == 'fankui'){
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [data.hq_width +"px",data.hq_height+'px'], //宽高
				 			 		content: '<div style="width:549px;margin:0 auto"><div class="fankui"></div><div class="fankui-right"><h3 style="color:#999">操作成功</h3>'+Wzb.htmlDecode(data.hq_content_cn)+'</div></div>'
				 			 		});
			 				}else if(tmp == 'yuyin'){
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [data.hq_width +"px",data.hq_height+'px'], //宽高
				 			 		content: '<div style="padding:48px 20px 0 20px;width:100%"><div style="margin:0 auto" onclick="yuyinPlay($(this))" onmouseover = "yuyin($(this))" class="yin">\
			 		 				'+Wzb.htmlDecode(data.hq_content_cn)+'\
									</div><p class="res" style="color:#fff;position: absolute;top:0px">'+$('#hqt_number').val()+'</p>\
			 		 			</div>'
				 			 		});
			 				}else if(tmp == 'right'){
			 					var  moban = Wzb.htmlDecode(data.hq_content_cn)
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [data.hq_width +"px",data.hq_height+'px'], //宽高
				 			 		content: '<div style="width:20px;height:20px;margin:70px auto" class="ask-in" onclick="showclick($(this),2)"><div style="display:none">'+moban+'</div></div>'
				 			 	});
			 				}else if(tmp == 'left'){
			 					var  moban = Wzb.htmlDecode(data.hq_content_cn)
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [data.hq_width+"px",data.hq_height+'px'], //宽高
				 			 		content: '<div style="width:20px;height:20px;margin:70px auto" class="ask-in" onclick="showclick($(this),4)"><div style="display:none">'+moban+'</div></div>'
				 			 	});
			 				}else if(tmp == 'yellow'){
			 					var  moban = Wzb.htmlDecode(data.hq_content_cn)
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [data.hq_width +"px",data.hq_height+'px'], //宽高
				 			 		content: '<div style="width:20px;height:20px;margin:70px auto" class="yellowtan" onclick="showclick($(this),1)"><div style="display:none">'+moban+'</div></div>'
				 			 	});
			 				}else if(tmp == 'gong'){
			 					layer.open({
				 			 		type: 1,
				 				  	title:false,
				 				  	closeBtn: 1,
				 				  	shadeClose: true,
				 				  	skin: 'layui-layer-demo',
				 			 		 area: [data.hq_width +"px",data.hq_height+'px'], //宽高
				 			 		content: '<div class="gong"><div style="width:670px	">'+Wzb.htmlDecode(data.hq_content_cn)+'</div></div>'
				 			 	});
			 				}
					    	
				    }
				});  
		}
			
		function showclick(name,fangxiang){
			//console.log(fangxiang)
				layer.tips(name.children('div').html(), name, {
					  tips: [Number(fangxiang),'rgba(128,128,128,0.9)'] //还可配置颜色
				});
			}
		
		
window.onload  = function(){	
	helpshow()
}
	


	
</script>
</body>
</html>
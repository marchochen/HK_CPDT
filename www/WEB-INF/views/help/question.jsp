<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

<jsp:include page="${ctx}/WEB-INF/views/common/meta.kindeditor.jsp"></jsp:include>
<script type="text/javascript">
	var editor = null;
	
	function questionSave(){
		if($('#questionForm').valid()){
			var url = '${ctx}/app/help/question/'+questionAct;
			if(questionAct=='update'){
				url = url+'/'+$("#questionForm #hq_id").val();
			}
			//判断编辑器是否有内容
			var htmlieditor_cn;
			var htmlieditor_us;
			 if(UE.getEditor('editor1').hasContents()){
				htmlieditor_us = UE.getEditor('editor1').getContent()
			}
			 if(UE.getEditor('editor').hasContents()){
				htmlieditor_cn = UE.getEditor('editor').getContent()
			}
	
			 var moban = $('.moban_active .cur').data('moban');
			 if(moban == null || moban == undefined){
				Dialog.alert("请选择一个模板"); 
				 return;
			 }
			 var flg = false;
			 $.ajax({  
		        type:'post',      
			    url: '${ctx}/app/help/question/check_number',  
			    async:false, 
			    data:{
			    	hq_id : $("#questionForm #hq_id").val(),
			    	hqt_number : $("#questionForm #hqt_number").val(),
			    	question_act : questionAct
			    },  
			    cache:false,  
			    dataType:'json',  
			    success:function(data){
			    	if(data.success==false){
			    		Dialog.alert("编号已存在");
			    		flg = true;
			    	}
			    	
			       }  
			});
			
			if(flg){
				return; 
			}
			
			$.ajax({  
		        type:'post',      
			    url: url,  
			    async:false, 
			    data:{
			    	hq_id : $("#questionForm #hq_id").val(),
			    	hqt_number : $("#questionForm #hqt_number").val(),
			    	hq_title : $("#questionForm #hq_title").val(),
			    	hq_width : $("#questionForm #hq_width").val(),
			    	hq_height : $("#questionForm #hq_height").val(),
			    	hq_type_id : $("#questionForm #hq_type_id").val(),
			    	hq_content_us : UE.getEditor('editor1').getContent(),
			    	hq_content_cn : UE.getEditor('editor').getContent(),
			    	hq_template : $('.moban_active .cur').data('moban')
			    },  
			    cache:false,  
			    dataType:'json',  
			    success:function(data){
			    	if(data.success==true){
			    		returnToQuestionList();
			    	}
			       }  
			});  
		}
		
	}
	
	
	
	
	function isQuestionContextEmp(){
		
		
		/* editor.sync();
		var contentEmp =false;
		var content = $('#questionForm #hq_content').val();
		if(''==content || null==content){
			$('#error_hq_content').css('display','');
			$('#error_hq_content').text('内容不能为空');
			contentEmp = true;
		}else{
			$('#error_hq_content').css('display','none');
		} */
		/* 判断是不是空 */
		var arr = [];
        arr.push(UE.getEditor('editor').hasContents());
        if(!arr[0]){
        	$('#error_hq_content').css('display','');
			$('#error_hq_content').text('内容不能为空');
        }
        //console.log(arr)
		return arr[0];
	}
	
	function questionDel(){
		$.ajax({  
	        type:'delete',      
		    url: '${ctx}/app/help/question/'+$("#questionForm #hq_id").val(),
		    cache:false,  
		    dataType:'json',  
		    success:function(data){ 
		    	if(data.success==true){
		    		returnToQuestionList();
		    	}
		    }  
		});  
	}
</script>


<h4 style="padding-bottom: 10px; border-bottom: 1px solid #eee;" id="question_form_title">添加问题</h4>
<form id="questionForm">
<input type="hidden" id="hq_id">
<table class="wzb-table-three">
	<tbody>
	<!-- 所属分类 -->
		<tr>
			<td class="wzb-table-title" align="left">
				<%-- <lb:get key='label_cm.label_core_community_management_205' />： --%>
				所属功能模板：
			</td>
			<td class="wzb-table-content ">
				<div class="wzb-selector">
					<select class="wzb-select" name="hq_type_id" style="height:34px;width:57%;margin-right: 80px;" id="hq_type_id">
		           	    <c:forEach items="${type_list}" var="type_item">
		           	    	<option value="${type_item.id}" >
								<c:out value="${type_item.type_name}"></c:out>
							</option>
					 	</c:forEach>
					</select>
					<label class="error" id="error_hq_type_id" style="" for='hq_type_id'></label>
				</div>
			</td>
		</tr>
	<!-- <tr>
			<td class="wzb-table-title">提醒方式：</td>
			<td class="wzb-table-content">
				<select class='tishi_fangshi' style='width:50%;height:34px;'>
					<option value='1'>点击弹层层 </option>
					<option value='2'>触碰提示</option>
					<option value='3'>页面内显示</option>
				</select>
			</td>
		</tr>
		 -->
	    <tr>
			<td class="wzb-table-title" >
				编号：
		    </td>
				<td class="wzb-table-content">
				  <input placeholder="请输入编号" id="hqt_number" name="hqt_number" value="" class="form-control" style='width:50%;'/>
				  <label class="error" id="error_hqt_number" style="" for='hqt_number'></label>
			</td>
		</tr>
		<tr>
			<td class="wzb-table-title"><lb:get key="global_title" />：</td>

			<td class="wzb-table-content">
				<input placeholder="请输入标题" type="text" value="" style='width:50%;' id="hq_title" name="hq_title" class="form-control">
				<label class="error" id="error_hq_title" style="" for='hq_title'></label>
			</td>
		</tr> 
		
		
		<!--设置高度  -->
		 <tr style='display:none'>
			<td class="wzb-table-title">
				提示信息宽度:
		    </td>
				<td class="wzb-table-content">
				  <input id="hq_width"/>
			</td>
		</tr>
		<tr style='display:none'>
			<td class="wzb-table-title">
				提示信息高度:
			</td>
			<td class="wzb-table-content">
			  <input id="hq_height"/>
		    </td>
		</tr> 
		<!-- 模板-->
		<!-- 点击模板 -->
		<tr style='height: 146px;'>
			<td  valign="top" class="wzb-table-title">点击提示模板：</td>
			<td valign="top"  class="wzb-table-title moban_active" >
					<div    style="width:70px;height:50px;    margin-top: -7px;">
					<span data-moban='370,170,1,butong'><em></em>370*170</span>
						<img src='../../../static/images/moban_370x170.jpg'/>
					</div>
					<div style="width:70px;height:50px;    margin-top: -7px;">
					<span  data-moban='370,236,1,butong' ><em></em>370*236</span>
						 <img src='../../../static/images/moban_370x236.jpg'/> 
						
					</div>
					<div  style="width:70px;height:50px;    margin-top: -7px;">
					<span data-moban='570,364,1,butong' ><em></em>570*364</span>
						 <img src='../../../static/images/moban_570x364.jpg'/>
						
					</div>
					<div  style="width:70px;height:50px;    margin-top: -7px;">
					<span data-moban='770,492,1,butong' ><em></em>770*492</span>
						 <img src='../../../static/images/moban_770x492.jpg'/> 
						
					</div>
					<div style="width:70px;height:50px;    margin-top: -7px;">
					<span  data-moban='970,540,1,butong'><em></em>970*540</span>
						<img src='../../../static/images/moban_970x620.jpg'/> 
					</div>
			</td>
		</tr>
		<!-- 触摸提示模板 -->
		<tr style='height:99px;'>
			<td valign="top" class="wzb-table-title">触摸提示模板：</td>
			<td valign="top" class="wzb-table-title moban_active" >		
				<div class='moban_chumo_tishi'>
					<div    style="width:70px;height:50px;">
					<span data-moban='370,170,0,left'  data-name="moban_left"><em></em>箭头靠左</span>
						 <img src='../../../static/images/moban_left1.jpg'/>
					</div>
					<div style="width:70px;height:50px;">
					<span data-name='moban_right' data-moban='370,170,0,right' ><em></em>箭头靠右</span>
					    <img src='../../../static/images/moban_left2.jpg'/> 
					</div>
				</div>
				
				
				<div class="moban_header">
					<div style="width:70px;height:50px;">
					<span data-moban='370,170,2,yuyin' data-name='yuyin'><em></em>语音提示+点击播放</span>
						<img src='../../../static/images/moban_yuyim.jpg'/> 
					</div>
				</div>
			</td>
		</tr>
		<!-- 页面内显示模板 -->
		<tr style='margin-top:10px;height:116px;'>
			<td valign="top" class="wzb-table-title" >页面内显示模板 ：</td>
			<td valign="top" class="wzb-table-title moban_active" >
			
				<div class='moban_header'>
					<div style="width:70px;height:50px;">
					<span data-moban='890,200,2,gong' data-name='gong' ><em></em>功能介绍</span>
						<img src='../../../static/images/moban_gongneng.jpg'/> 
					</div>
				</div>
			
				
			
				
					
					<div class='moban_header'>
						<div style="width:70px;height:50px;">
							<span data-moban='598,261,2,fankui' data-name='fankui'><em></em>操作反馈</</span>
							<img src='../../../static/images/moban_fankui.jpg'/> 
						</div>
					</div>
				
				
					
					
					
					
					
				
				<div class='moban_header_tanhao'>
					<div style="width:70px;height:50px;">
					<span data-moban='370,170,0,red' data-name='redtan' ><em></em>红色叹号</span>
						<img src='../../../static/images/moban_tanhao.jpg'/> 
						
					</div>
					<div style="width:70px;height:50px;">
					<span  data-moban='370,170,0,yellow' data-name='yellowtan' ><em></em>黄色叹号</span>
						<img src='../../../static/images/moban_tanhao.jpg'/> 	
					</div>
				</div>
				
				
			</td>
		</tr>
		
		
		<style>
		.wzb-table-three td.wzb-table-title{width:81px;}
			.title_moban{color:#ff8c00}
			td.wzb-table-title{width:110px;}
			.wzb-table-title.moban_active{height:100px;}
			.wzb-table-title.moban_active div img{height:100%;width:100%;display:block}
			.wzb-table-title.moban_active div{margin-top: -3px;text-align: center;float:left;margin-right:10px;}
			.wzb-table-title.moban_active div span{width:77px !important;color:#333;line-height: 45px;;width:70px;height:45px;display:block}
			.wzb-table-title.moban_active div:hover{cursor: pointer;}
			.wzb-table-title.moban_active .cur em{background:#00aeef;color:#fff;}
			.moban_chumofangxiang{color:#333;}
			.moban_chumofangxiang .active{color:#ff8c00}
			.moban_header{height:132px;overflow:hidden;float:left;}
			.moban_header div:first-child{width:100%}
			.moban_header_tanhao div:first-child{width:100%}
			.wzb-table-title.moban_active div span em{margin-top: 14px;float:left;width:15px;height:15px;display:inline-block;border:1px solid #999;border-radius: 100%;}
		</style>
		<script>
			$('.moban_chumofangxiang em').click(function(){
				 //console.log($('.moban_chumo_tishi div span').hasClass('cur'))
					$('.moban_chumofangxiang em').removeClass('active')
					$(this).attr('class','active')
				
				
			})
		
			//编辑器模板尺寸选择
			 $('.moban_active div span').click(function(){
				$('.moban_active div span').removeClass('cur')
				$(this).toggleClass('cur')
				 if(!$('.moban_chumo_tishi div span').hasClass('cur')){
					$('.moban_chumofangxiang em').removeClass('active')
				}
				$('#hq_width').val($(this).attr('data-moban').split(',')[0])
				$('#hq_height').val($(this).attr('data-moban').split(',')[1])
				var bianhao  = $(this).attr('data-moban').split(',')[2];
				var innerhtml;//自定义模板
				/* UE.getEditor('editor').setContent('')
				UE.getEditor('editor1').setContent('') */
				//console.log(bianhao)
				if(bianhao==1){
					if(!UE.getEditor('editor').hasContents()){
						innerhtml = '<p style="width:100%;">\
							<span style="display:inline-block;font-size:16px;color:#999;">自动积分与难度系数</span>\
							<span style="display:block;width:100%;border-top:1px solid #eee;margin:10px 0px;"></span>\
							<span>早在2015年春节除夕夜，微信就借势春晚，利用“摇一摇”功能引起亿万红包参与。而历时发展三年的微信红包，每年的新春创意献礼，已成为中国人过年的新年俗，刷新了国人的红包观念。</span>\
						</p>';
						UE.getEditor('editor').execCommand('insertHtml',innerhtml )
					}
					if(!UE.getEditor('editor1').hasContents()){
						innerhtml = '<p style="width:100%;">\
							<span style="display:inline-block;font-size:16px;color:#999;">自动积分与难度系数</span>\
							<span style="display:block;width:100%;border-top:1px solid #eee;margin:10px 0px;"></span>\
							<span>早在2015年春节除夕夜，微信就借势春晚，利用“摇一摇”功能引起亿万红包参与。而历时发展三年的微信红包，每年的新春创意献礼，已成为中国人过年的新年俗，刷新了国人的红包观念。</span>\
						</p>';
						UE.getEditor('editor1').execCommand('insertHtml',innerhtml )
					}
					  
				}else if(bianhao==2){
					
				}
				
			
			}) 
			
			//图片预览
			 $('.moban_active div img').click(function(){
				//console.log($(this).attr('src'))
				layer.open({
					  type: 1,
					  content: '<img style="width:100%;" src='+$(this).attr('src')+'>',
					  area: ['100%', '100%'],
					  maxmin: true
				});
			}) 
				
			
			
		</script>
		
		
		<tr>
			<td class="wzb-table-title" >提示信息<lb:get key="global_content" />(中文)：</td>
		</tr>
		<tr>
			<td  colspan="2">
			<script id="editor" type="text/plain" style="width:100%;height:500px;" name="hq_content_cn" ></script>
			<label class="error" id="error_hq_content" style="display: none;'" for='hq_content_cn'></label>
			</td>
		</tr>
		<tr>
			<td class="wzb-table-title" ></td>
		</tr>
		<tr>
			<td class="wzb-table-title" >提示信息<lb:get key="global_content" />(英文)：</td>
		</tr>
		<tr>
			<td colspan='2'>
					<script id="editor1" type="text/plain" style="width:100%;height:500px;" name="hq_content_us" ></script>
					<label class="error" id="error_hq_content" style="display: none;'" for='hq_content_us'></label>
			</td>
		</tr>
		<script type="text/javascript">
							    //实例化编辑器
							    //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
							    var ue = UE.getEditor('editor',{
					                lang:'zh-cn',
					                imageScaleEnabled:true,
					                autoFloatEnabled:false
					            });
							    var ue1 = UE.getEditor('editor1',{
							    	lang:'en',
							    	imageScaleEnabled:true,
							    	autoFloatEnabled:false
							    });
							    
							    	
							

		</script>
		
		
		
	</tbody>
</table>
<div class="wzb-bar">
	<a class="wzb-btn-orange wzb-btn-big margin-right15" onclick="questionSave()"><lb:get key='global.button_ok'/></a> 
	<a class="bg-btn wzb-btn-orange wzb-btn-big margin-right15" onclick="returnToQuestionList()"><lb:get key='global.button_cancel'/></a>
</div>
<div>
&nbsp;</br>
&nbsp;</br>
&nbsp;</br>
&nbsp;</br>
</div>
</form>
<script>
	//用户选定
	$('.tishi_fangshi').change(function(){
		//console.log($(this).val())
	})
</script>

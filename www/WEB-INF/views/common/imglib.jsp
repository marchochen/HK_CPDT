<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>

<link rel="stylesheet" href="${ctx}/static/css/thickbox.css" />
<script src="${ctx}/static/js/thickbox-compressed.js" type="text/javascript"></script>

<script>
if(defaultImageTemplateName == undefined || defaultImageTemplateName == ''){
	defaultImageTemplateName = "live";
}

$(function(){
	$("#select_image").html($("#selectImageTemplate").render(p));
	initDefaultImage(defaultImageTemplateName, 'topic', false);
	if(handle_type == 'add'){
	   var img_obj = document.getElementById("image_id");
	   if(typeof(img_obj) == "undefined"){
		   initDefaultImage(defaultImageTemplateName, 'topic', true);
	   }
	}
});

function initDefaultImage(type, name, addInd){
	var initv = "init";
	$.getJSON(wb_utils_app_base + 'app/defaultImage/' + type , function(result_data) {
		var image_html = "";
		
		$.each(result_data, function(index, name) {
			//将所有图片组成html
			image_html += $("#imageTemplate").render({wb_utils_app_base:wb_utils_app_base, type:type, name:name});
		});
		
		//写入所有图片
		$('#defaultImages').html(image_html);
		
		var sum = result_data.length - 1;
		var num = parseInt(Math.round(Math.random() * sum));
		//给默认选中的图片加上区别样式
		$("#defaultImages a:eq("+num+")").addClass("cur");
 	
		if(addInd){
			selectImage();
		}
		
		if(handle_type == "add"){
			useDefaultImage();
		}
	});
}

function changeImage(thisObj) {
	$("#" + $(thisObj).parent().attr("id") + " .cur").removeClass("cur");
	$(thisObj).addClass("cur");
}

function selectImage(){
	$("input[name='"+input_hidden_name+"']").val($('#defaultImages .cur').attr("value"));
	$("img[name='live_image']").attr("src", $('#defaultImages .cur img').attr('src'));
	$("input[name='imgurl']").val($('#defaultImages .cur img').attr('src'));
	tb_remove();
}

function show_default_image(){
	$("#default_image").attr("checked","checked");
	$("#default_btn").click();
}

function useDefaultImage(event){
	event = jQuery.Event(event);
	event.stopPropagation();
	
	//console.log($('#defaultImages .cur img').attr('src'));
	$("img[name='"+input_name+"']").attr("src", $('#defaultImages .cur img').attr('src'));
	$("input[name='imgurl']").val($('#image_id').attr('src'));
	$("input[name='"+input_hidden_name+"']").val($('#defaultImages .cur').attr('value'));
}

function image_change(obj) {
	var img_obj = document.getElementById("image_id");
	var file_obj = document.getElementById("file_photo_url");
	if(obj === "remain_image") {
		//clearFileInput(file_obj);
		img_obj.src = $('#curimg').val();
		$("input[name=remain_photo_ind]").val("true");
	} else if(obj === "default_image") {
		//clearFileInput(file_obj);
		$("input[name=remain_photo_ind]").val("false");
		$("input[name='imgurl']").val($('#image_id').attr('src'));
	} else {
		file_obj.disabled = false;
		$("input[name=remain_photo_ind]").val("false");
		if(document.all.image.files && document.all.image.files[0]){
			img_obj.src = window.URL.createObjectURL(document.all.image.files[0]);
		}
	}
}

function clearFileInput(file_obj){
	var file_obj2= file_obj.cloneNode(false);
	file_obj2.onchange= file_obj.onchange; 
	file_obj.parentNode.replaceChild(file_obj2,file_obj);
}

function previewLocalImage(obj) {
	var name=document.getElementById("file_photo_url").value;
	var type=name.substr(name.lastIndexOf(".")+1).toLowerCase();
    if(type != "bmp" && type != "jpg" && type!= "gif" &&type != "png" && type != "jpeg"){
        Dialog.alert(fetchLabel('label_core_learning_map_122'));
        $(".file_txt").val("");
   }else{
		var img_obj = document.getElementById("image_id");
		img_obj.src = window.URL.createObjectURL(obj.files[0]);
   }
}

</script>
<!-- 选择图片模板 -->
<script id="selectImageTemplate" type="text/x-jsrender">
	<table>
		<tbody>
			<tr>
				<td style="width: 340px; " rowspan="4">
					<div>
						{{if type =='update' }}
								<img width="326px" height="143px" border="0" id="image_id" name="{{:input_name}}" src="{{: image_url }}" />
								<input value="{{: image_url }}" type="hidden" id="curimg" />
						{{else}}
								<img width="326px" height="143px" border="0" id="image_id" name="{{:input_name}}" src="" />
								<input value="" type="hidden" id="curimg">
						{{/if}}
					</div>
				</td>
				{{if type =='update' }} <!-- 保留当前头像 -->
					<td>
						<input name="image_radio" onclick="image_change('remain_image');" value="0" checked type="radio" id="remain_image" role="auto">
						<span class="wbFormRightText"> <lb:get key="label_lm.label_core_learning_map_44" /></span> 
						<input value="true" type="hidden" name="remain_photo_ind" />
					</td>
				{{/if}}
			</tr>
			<tr>
				<td>
					<span>
						<input value="1" onclick="useDefaultImage();image_change('default_image')" {{if type=='add'}}checked{{/if}} name="image_radio" id="default_image" type="radio" role="default"> 
						<input value="<lb:get key='lab_kb_default_images'/>" style="border: 1px solid transparent; padding: 3px 8px;" class="wzb-btn-blue" type="button" name="" 
								onclick="useDefaultImage();show_default_image();image_change('default_image')">
						</a> 
						<input value="{{: image_url }}" type="hidden" name="{{:input_hidden_name}}" />
						<input value="{{: image_url }}" type="hidden" name="imgurl" />
					</span>
					<!-- 默认图库弹出窗口 -->
					<div id="myOnPageContent" style="display: none;">
						<div class="thickbox-tit">
							<lb:get key="label_km.label_core_knowledge_management_37" />
						</div>
						<div class="thickbox-cont thickbox-user clearfix thickbox-content-2" id="defaultImages"></div>
						<div class="norm-border thickbox-footer">
							<input value="<lb:get key='global.button_ok'/>" onclick="selectImage()" name="pertxt" class="margin-right10 btn wzb-btn-blue wzb-btn-big" type="button"> 
							<input value="<lb:get key='global.button_cancel'/>" name="pertxt" class=" TB_closeWindowButton  btn wzb-btn-blue wzb-btn-big" type="button">
						</div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<input onclick="image_change('local_image')" name="image_radio" value="2" id="local_image" type="radio">
					<span class="wbFormRightText"><lb:get key="label_lm.label_core_learning_map_48" /></span>
					<div style="margin: 2px 0 0 0; height: 0;" class="file">
						<input onchange="$(this).siblings('.file_txt').html(this.value);$(this).siblings('.file_txt').val(this.value);previewLocalImage(this);image_change('local_image')" type="file" name="image" class="file_file" onfocus="" onblur="" 
							onclick="document.getElementById('local_image').checked=true;" title="" value="" id="file_photo_url"> 
						<input value='<lb:get key="no_select_file"/>' class="file_txt">
						<div class="file_button-blue"><lb:get key="usr_browse" /></div>
					</div> 
					<br> 
					<span class="text">&nbsp;<lb:get key="label_core_live_management_31" /><br></span>
				</td>
			</tr>
		</tbody>
	</table>
</script>
<script id="imageTemplate" type="text/x-jsrender">
	<a  href="javascript:;" onclick="changeImage(this)" value="{{: name}}">
		<img   src="{{: wb_utils_app_base}}imglib/{{: type}}/{{: name}}"/>
		<div class="thickbox-bg"></div>
	</a>
</script>
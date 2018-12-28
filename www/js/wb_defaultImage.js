var preview_name;
function initDefaultImage(type, name, addInd, callback){
	var initv = "init";
	$.getJSON(wb_utils_app_base + 'app/defaultImage/' + type , function(rData) {
		var str = '';
		$.each(rData, function(index, name) {
			str += '<a  href="javascript:;" onclick="changeImage(this)" value="' + name + '">'
				+ '<img   src="' + wb_utils_app_base +'imglib/'+ type + '/' + name + '"/>'
				+ '<div class="thickbox-bg"></div></a>';
		});
		var sum=rData.length-1;
		var num = parseInt(Math.round(Math.random()*sum));
		$('#defaultImages').html(str);
		$("#defaultImages a:eq("+num+")").addClass("cur");
		
 	
		if(addInd){
			selectImage();
		}
		
		if(callback){
			callback();
		}
		
	});
	preview_name = name;
}

function changeImage(thisObj) {
	$("#" + $(thisObj).parent().attr("id") + " .cur").removeClass("cur");
	$(thisObj).addClass("cur");
}

function selectImage(){
	$("input[name='default_image']").val($('#defaultImages .cur').attr("value"));
	$("img[name='" + preview_name + "_preview']").attr("src", $('#defaultImages .cur img').attr('src'));
	tb_remove();
}

function show_default_image(){
	$("#default_btn").click();
}

function useDefaultImage(event){
	
	 event = jQuery.Event(event);
	 event.stopPropagation();
	
	// 添加去掉的东西
	 $("#default_btn").prev().removeAttr("disabled");
	  
	 //$("#msg_icon_file").attr("disabled","disabled");  //
	 //("#file_photo_url").attr("disabled","disabled"); 
	 //$("#field99__file").attr("disabled","disabled"); 
	 console.log($('#defaultImages .cur img').attr('src'));
	$("img[name='" + preview_name + "_preview']").attr("src", $('#defaultImages .cur img').attr('src'));
	$("input[name='default_image']").val($('#defaultImages .cur').attr('value'));
}

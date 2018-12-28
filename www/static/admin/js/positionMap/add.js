
$(function(){
	
    $("#adminForm").validate({
    	submitHandler: function(form){
    		
    		var flag = true;
    		var src = $('#extension_43_id').attr('src');
    		$("input[name='imgurl']").val(src);
    		var seq=$("#upm_seq_no").val();
    		if(!isNaN(seq)){
				var reg=/^(100|[1-9]\d|\d)$/;
			if(reg.test(seq)&&seq!=0)
			{
				$('#error-seq').hide();
			}else{
				$('#error-seq').show();
				$('#error-seq').html(cwn.getLabel('label_core_learning_map_35'));
				flag=false;
				
			}
			}else{
				$('#error-seq').show();
				$('#error-seq').html(cwn.getLabel('label_core_learning_map_35'));
				flag=false;
				}
    		if(!$.trim($("#upt_title").val())){
    			$("#error-title").show();
    			$("#error-title").html(cwn.getLabel('label_core_learning_map_33'));
    			flag = false;
    		}else{
    			$("#error-title").hide();
    		}
    		
    		if(!$.trim($("#qid").val())){
    			$("#error-course").show();
    			$("#error-course").html(cwn.getLabel('label_core_learning_map_34'));
    			flag = false;
    		}else{
    			$("#error-course").hide();
    		}
    		if($("input[name='image_radio']:checked").val() == 2){
    			if($("input[name='image']").val() != ''){
    				var file_ext = $("input[name='image']").val().substring($("input[name='image']").val().lastIndexOf(".") + 1);
    				if(file_ext != 'jpg' && file_ext != 'gif' && file_ext != 'png' && file_ext != 'jpeg'){
    					Dialog.alert(cwn.getLabel('label_core_learning_map_122'));
    					return;
    				}
    			} else {
    				Dialog.alert(cwn.getLabel('label_core_learning_map_128'));
    				return;
    			}
    		}
    		
    		
    		if(!flag){
    			return;
    		}
    		form.submit();   //提交表单   
        }  
/*       ,  
 *       rules:{
        	upt_title:{
                required:true
            },
            qid:{
            	required:true,
            	dateFormat:true
            },
            upm_seq_no:{
            	required:true,
            	dateFormat:true
            }
        }
        ,
        messages:{
        	upt_title:{
                required:cwn.getLabel('label_core_learning_map_33')
            },
            qid:{
            	required:cwn.getLabel('label_core_learning_map_37'),
            	dateFormat: cwn.getLabel('label_core_learning_map_34')
            },
            upm_seq_no:{
            	required:cwn.getLabel('label_core_learning_map_38'),
            	dateFormat:cwn.getLabel('label_core_learning_map_35')
            }
        }*/
    });
});
function clearAll(){
	$('.wzb-choose-box').html(' <input type="hidden" id="qid" name="qid" value="">');
}
function delOption(obj){
	$(obj).parent('p').remove();
	optionLength--;
}
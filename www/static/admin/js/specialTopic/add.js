
$(function(){
	
    $("#adminForm").validate({
    	submitHandler: function(form){
    		var flag = true;
    		  var entIds = [];
    	        $("#experts .wzb-choose-info").each(function(){
    	       	 entIds.push($(this).attr("value"));
    	       	})
    	        $("input[name=expert_ids]").val(entIds.join(','));
    	    	var src = $('#extension_43_id').attr('src');
        		$("input[name='imgurl']").val(src);
        		
        		if(!$.trim($("#ust_title").val())){
        			$("#error-title").show();
        			flag = false;
        		}else{
        			if(getChars($("#ust_title").val())>80){
        				$("#error-title").html(cwn.getLabel('label_core_learning_map_134'));
        				$("#error-title").show();
        				flag = false;
    				}else{
    					//判断是否重复
        				$.ajax({  
            		        type:'post',      
            			    url:contextPath +'/app/admin/specialTopic/checkExistTitle',  
            			    data:{ust_title:$("#ust_title").val(),old_value:$("#old_title").val()},  
            			    cache:false,  
            			    dataType:'json',
            			    async: false,
            			    success:function(data){  
            			    	if(data.stauts==1){
            			    		$("#error-title").html(cwn.getLabel('label_core_learning_map_129'));
            			    		$("#error-title").show();
            		    			flag = false;
            			    	}
        			    		else{
        			    			$("#error_title").hide();
        			    		}
            			       }  
            			    });  
    				}
        		}
        		
        		if(!$.trim($("#ust_summary").val())){
        			$("#error-summary").show();
        			flag = false;
        		}else{
        			if(getChars($("#ust_summary").val())>400){
        				$("#error-summary").html(cwn.getLabel('label_core_learning_map_121'));
        				$("#error-summary").show();
        				flag = false;
        			}else{
        				
        				$("#error-summary").hide();
        			}
        		}
        		if($.trim($("#ust_content").val())){
        			if(getChars($("#ust_content").val())>2000){
        				$("#error-content").html(cwn.getLabel('label_core_learning_map_100'));
        				$("#error-content").show();
        				flag = false;
        			}else{
        				
        				$("#error-content").hide();
        			}
        		}
        		if(!$.trim($("#q1").val())){
        			$("#error-course").show();
        			$("#error-course").html(cwn.getLabel('label_core_learning_map_34'));
        			flag = false;
        		}else{
        			$("#error-course").hide();
        		}
        		if($("input[name='image_radio']:checked").val() == 2){
        			if($("input[name='image']").val() != ''){
        				var file_ext = $("input[name='image']").val().substring($("input[name='image']").val().lastIndexOf(".") + 1);
        				//转换成小写
        				file_ext = file_ext.toLowerCase();
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
        },   
        rules:{
        	ust_title:{
                required:true
            },
            ust_summary:{
            	required:true,
            }
        }
        ,
        messages:{
        	ust_title:{
                required:cwn.getLabel('label_core_learning_map_36')
            },
            ust_summary:{
            	required:cwn.getLabel('label_core_learning_map_37'),
            }
       
        }
    });
});
/*document.onkeydown = function(e){ 
    var ev = document.all ? window.event : e;
     if(ev.keyCode==13) {
    	 return false;
     }
}*/
function clearAll(){
	$('#s1').html(' <input type="hidden" id="qid" name="qid" value="">');
}
function clearExpertAll(){
	$('#experts').html('');
}
function delOption(obj){
	$(obj).parent('p').remove();
	optionLength--;
}
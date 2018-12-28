
$(function(){
	
    $("#adminForm").validate({
    	submitHandler: function(form){
    		var flag = true;
    		var id=$("#catalogId").val();
    		$('#upc_id').val(id);
    		if(!$.trim($("#upt_title").val())){
    			$("#error_title").show();
    			flag = false;
    		}else{
    			if(getChars($("#upt_title").val())>80){
    				$("#error_title").html(cwn.getLabel('label_core_learning_map_118'));
    				$("#error_title").show();
    				flag = false;
    			}else{
    			
    			$.ajax({  
    		        type:'post',      
    			    url:contextPath +'/app/admin/position/checkExistTitle',  
    			    data:{upt_title:$("#upt_title").val(),old_id:$("#old_upt_id").val()},  
    			    cache:false,  
    			    dataType:'json',
    			    async: false,
    			    success:function(data){  
    			    	if(data.stauts==1){
    			    		$("#error_title").html(fetchLabel('label_core_learning_map_123'));
    			    		$("#error_title").show();
    		    			flag = false;
    			    	}
    			    		else{
    			    			$("#error_title").hide();
    			    		}
    			       }  
    			    });  
    			
    		}
    			}
    		if(!$.trim($("#upt_code").val())){
    			$("#error_code").show();
    			flag = false;
    		}else {
    			if(getChars($("#upt_code").val())>20){
    				$("#error_code").html(cwn.getLabel('label_core_learning_map_120'));
    				$("#error_code").show();
    				flag = false;
    			}else{
    			$.ajax({  
    		        type:'post',      
    			    url:contextPath +'/app/admin/position/checkExistCode',  
    			    data:{upt_code:$("#upt_code").val(),old_id:$("#old_upt_id").val()},  
    			    cache:false,  
    			    dataType:'json',
    			    async: false,
    			    success:function(data){  
    			    	if(data.stauts==1){
    			    		$("#error_code").html(fetchLabel('label_core_learning_map_82_1'));
    			    		$("#error_code").show();
    		    			flag = false;
    			    	}
    			    		else{
    			    			$("#error_code").hide();
    			    		}
    			       }  
    			    });  
    		}
    			}
    		if(!flag){
    			return;
    		}
    		form.submit();   //提交表单   
        },
        rules:{
        	upt_code:{
        		required:true
        	},
        	upt_title:{
        		required:true
        	}
        }
        ,
        messages:{
        	upt_code:{
                required:cwn.getLabel('label_core_learning_map_77')
            },
        	upt_title:{
                required:cwn.getLabel('label_core_learning_map_133')
            }
        }
    });
});

function delOption(obj){
	$(obj).parent('p').remove();
	optionLength--;
}
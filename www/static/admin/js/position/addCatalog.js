
$(function(){
	
    $("#adminForm").validate({
    	submitHandler: function(form){
    		
    		var flag = true;
    		var title=$("#upc_title").val();
    		
    		if(title==""||title==null){
    			flag=false;
    		}
    		if(!$.trim($("#upc_title").val())){
    			$("#error_title").show();
    			flag = false;
    		}else{
    			if(getChars($("#upc_title").val())>80){
    				$("#error_title").html(cwn.getLabel('label_core_learning_map_118'));
    				$("#error_title").show();
    				flag = false;
    			}else{
    			$.ajax({  
    		        type:'post',      
    			    url:contextPath +'/app/admin/position/checkCatalogExistTitle',  
    			    data:{upc_title:$("#upc_title").val(),old_id:$("#old_upc_id").val()},  
    			    cache:false,  
    			    dataType:'json',
    			    async: false,
    			    success:function(data){  
    			    	if(data.stauts==1){
    			    		$("#error_title").html(fetchLabel('label_core_learning_map_83'));
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
    	
    		if(!flag){
    			return;
    		}
    		form.submit();   //提交表单   
        },   
        rules:{
        	upc_title:{
                required:true
            }
        }
        ,
        messages:{
        	upc_title:{
                required:cwn.getLabel('label_core_learning_map_75')
            }
        }
    });
});

function delOption(obj){
	$(obj).parent('p').remove();
	optionLength--;
}
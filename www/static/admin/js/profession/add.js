$(function(){
	
	function checkGid(){
		var bool=true;
		  var opIds = [];
		  
		 var s=$("input[name='name']");
		   for(var j=0;j<s.length;j++){
			   var index=s[j].id.substring(4,s[j].id.length);
			   opIds.push(index);
		   }
			for (var n = 0; n< opIds.length; n++) {
				//判断职级是为空
				if(!$.trim($("#name"+opIds[n]).val())){
        			 $("#error_g"+opIds[n]).html(cwn.getLabel('label_core_learning_map_101'));
         			$("#error_g"+opIds[n]).show();
         			bool = false;
         		}
				//判断课程是否为空
				 if(!$.trim($("#q"+opIds[n]).val())){
        			 $("#error_q"+opIds[n]).html(cwn.getLabel('label_core_learning_map_34'));
        			 $("#error_q"+opIds[n]).show();
        			 bool = false;
        		 }
			}
     			for (var j = 0; j < opIds.length-1; j++) {
     				//判断职级是否重复
     				if(typeof($("#g"+opIds[j]).val())!="undefined"){
     				
     						for ( var k= opIds.length-1; k > j; k -- ) { 
     						if ($("#g"+opIds[j]).val()==$("#g"+opIds[k]).val()) { 
     							$("#error_g"+opIds[j]).html(cwn.getLabel('label_core_learning_map_119'));
     		        			$("#error_g"+opIds[j]).show();
     		        			$("#error_g"+opIds[k]).html(cwn.getLabel('label_core_learning_map_119'));
     		        			$("#error_g"+opIds[k]).show();
     						bool=false;
     						} 
     						} 
     						}
     					}
     			return bool;
     		}
		
    $("#adminForm").validate({
    	submitHandler: function(form){
    		var flag = true;
    		 var cnt=$("input[name='name']").length;
    		 if(cnt==0){
    			 alert(cwn.getLabel('label_core_learning_map_127'));
    			 return;
    		 }
    		if(!$.trim($("#pfs_title").val())){
    			$("#error_title").show();
    			flag = false;
    		}else{
    			if(getChars($("#pfs_title").val())>80){
    				$("#error_title").html(cwn.getLabel('label_core_learning_map_118'));
    				$("#error_title").show();
    				flag = false;
    			}else{
    				//判断是否重复
    				$.ajax({  
        		        type:'post',      
        			    url:contextPath +'/app/admin/profession/checkExistTitle',  
        			    data:{pfs_title:$("#pfs_title").val(),old_value:$("#old_title").val()},  
        			    cache:false,  
        			    dataType:'json',
        			    async: false,
        			    success:function(data){  
        			    	if(data.stauts==1){
        			    		$("#error_title").html(fetchLabel('label_core_learning_map_125'));
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
    		if(flag){
    			flag=checkGid();
    		}
    		if(!flag){
    			return;
    		}
    		 form.submit();   //提交表单   
        }  
       ,  
        rules:{
        	pfs_title:{
                required:true
            },
            gid:{
                required:true
            },
            qid:{
                required:true
            }
        }
        ,
        messages:{
        pfs_title:{
                required:cwn.getLabel('label_core_learning_map_102')
        },
        gid:{
        	required:cwn.getLabel('label_core_learning_map_101')
        },
        qid:{
        	required:cwn.getLabel('label_core_learning_map_34')
        }
        }
    });
});
function clearAll(op){
	$('#s'+op).html(' <input type="hidden" id="qid'+op+'" name="qid" value="">');
}
function delOption(obj){
	$(obj).parent('p').remove();
	optionLength--;
}

$(document).ready(function(){ 
	//rewriteSaveForm();
	rewriteInputFocus();
	
	initForm();
});

//初始化表单元素
function initForm(){
	var accountObj = $("#account");//账号输入框
	var passwordObj = $("#password");//密码输入框
	accountObj.val('请输入账号')
	
	accountObj.focus();
}

//重写saveForm
function rewriteSaveForm(){
	$('#saveForm').submit(function(){
		return submitForm();
	});
}

//重写输入框的
function rewriteInputFocus(){
	var accountObj = $("#account");//账号输入框
	var passwordObj = $("#password");//密码输入框
	
   	accountObj.focus(function(){
   		if('请输入账号'==$(this).val()){
   			$(this).val('');
   		}
   		$(this).css("border","solid");
	    $(this).css("border-color","yellow");
  	});
  	accountObj.blur(function(){
   		if(''==$(this).val()){
   			$(this).val('请输入账号');
   		}
    	$(this).css("border","none");
    	passwordObj.focus();
  	});
  	
   	passwordObj.focus(function(){
   		$(this).css("border","solid");
	    $(this).css("border-color","yellow");
  	});
  	passwordObj.blur(function(){
    	$(this).css("border","none");
  	});  	
}

//提交表单
function submitForm(){
	if(!checkForm()){
		return false;
	}	
/*	var options = { 
		url : '/app/wizWeixin/bindWizBank',
		async: false,
		cache: false,
		type : 'POST', 
		//timeout : 10000,
		success : function(returnData){ 
			//alert(returnData);
			if(returnData == 'true'){
				$('#bindSuccessPage').show();
				$('#bindingPage').hide();
				
				//parent.closePopWindow();
				//parent.reloadDataGrid();
			}else{
				alert('很抱歉,微信绑定e-Learning账号失败!');
			}
		},
		error : function(){
			alert('System error,保存失败!');
		} 
	};
	$('#saveForm').ajaxSubmit(options);
	return false;*/
/*	$.ajax({
		url : '/app/wizWeixin/bindWizBank',
		async: false,
		cache: false,
		type : 'POST', 
		//timeout : 10000,
		data : {
			weixinUserOpenId : $('#weixinUserOpenId').val(),
			fwhCode : $('#fwhCode').val(),
			password : $('#password').val(),
			account : $('#account').val(),
		},
		success : function(returnData){ 
			//alert(returnData);
			if(returnData == 'true'){
				$('#bindSuccessPage').show();
				$('#bindingPage').hide();
				
				//parent.closePopWindow();
				//parent.reloadDataGrid();
			}else{
				alert('很抱歉,微信绑定e-Learning账号失败!');
			}
		},
		error : function(){
			alert('System error,保存失败!');
		} 
	});
	return false;*/
}

//表单验证
function checkForm(){
	var account = $('#account');
	var password = $('#password');
	if($.trim(account.val()).length == 0){
		alert('请输入e-Learning账号!');
		account.focus();
		return false;
	}
	if($.trim(password.val()).length == 0){
		alert('请输入e-Learning密码!');
		password.focus();
		return false;
	}
	return true;
}

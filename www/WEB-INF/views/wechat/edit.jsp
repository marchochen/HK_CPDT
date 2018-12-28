<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="../common/taglibs.jsp"%>
<html>
<head>
	<title>微信绑定</title>	
	<meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no">
	<link type="text/css" rel="stylesheet" href="${ctx}/mobile/css/basic.css" />
	
	<script language="JavaScript" type="text/javascript" src="${ctx}/js/jquery.js"></script>
	<script language="JavaScript" type="text/javascript" src="${ctx}/js/jquery.form.js"></script>
	<style type="text/css">
	*{
	padding:0;margin:0;
	font-family:"Microsoft YaHei",Arial,Helvetica,sans-serif,"宋体";
	}
	.banner:after {
		display: block;
		width: 100%;
	}
	.bindForm{
		width:100%;
		margin: 0 auto;
	}
	.bindForm .textinput {
		margin-top:8%;
		text-align: center;
	}
	.bindForm .textinput .input, .input:focus {
		border: 1px solid #D7CACA;
		width: 85%;
		min-height: 38px;
 		-webkit-appearance: none; 
	}
	.bindForm .textinput .button{
		height:40px;
		width:85%;
	 	margin-top: 6%; 
		color : #FFF;
		background: #90e601;
		border-radius : 4px;
		font-size: 15px;
		border:none;
	}
	.bindForm .textinput .nameinput{
		border-top-left-radius:4px;
		border-top-right-radius:4px;
		border-bottom: none;
	}
	.bindForm .textinput .pwdinput{
		border-bottom-left-radius:4px;
		border-bottom-right-radius:4px;
	}
	.bindForm .bindDesc {
		width:85%;
		padding-top:5%;
		line-height:16px;
		font-size:16px;
		color:#7ac604;
		margin:0 auto;
		text-align: left;
	}
	.success {
		text-align: center;
		padding-top:5%;
	}
	.success .successText{
		color:#ffde00;
		font-size:32px;
		margin-top:5%;
		font-weight:600;
	}
	.success .successDesc{
		color:#7ac604;
		padding-top:5%;
		font-size:16px;
		line-height:22px;
/* 		font-weight:600; */
	}
	</style>
</head>
<body>
<body>

<div class="wrap" id="wizwrap">
<div class="list-pic-5" style="padding-top: 667px; background: url(${ctx}/poster/loginPage/cw/adv62.jpg) 50% 50% / 100% 100% no-repeat;">
<div class="own-box">
<div class="own-box-area">
	<div class="wiz-box">
<%--           <img alt="" src="${wb_image}wechat/bind_banner.jpg"> --%>
		<div style="padding-top: 55%; background: url(${wb_image}wechat/bind_banner.jpg) 50% 50% / 100% 100% no-repeat;"></div>
		<div style="padding-top:25%;"></div>
		<div id="inputArea">
			<form class="wiz-form" id="saveForm" method="post" action="${ctx }/app/wechat/bind" onsubmit="return submitForm();">
				<input type="hidden" name="userOpenId" value="${userOpenId }" id="userOpenId" />
				<input type="hidden" name="fwhCode" value="${fwhCode }" id="fwhCode" />
				<div class="wiz-email"><input type="text"  id="account" name="account" required  class="wiz-txt-1"></div>
				<div class="wiz-pws" style="margin-bottom:35px;"><input type="Password" name="password" id="password" required class="wiz-txt-1"></div>
				<div class="wiz-info" style="position: relative;"><input  type="submit" name="wiztxt" class="wiz-btn-1" style="color:#90E601;font-weight:600;" value="绑定微信">
				</div>
		     </form>
		     <div class="bindDesc" style="width:280px;padding-top:5%;line-height:16px;font-size:14px;color:#FFF;margin:0 auto;text-align: left;">
				通过wizBank在线学习系统账号和微信绑定，成功后，就能在微信中进行丰富的内容、测试查询等其他信息
		 	 </div>
	 	 </div>
     </div>

	 
	 <div id="success" class="success" style="display: none">
			<img src="${wb_image}wechat/success.png" width="30%"/>
			<div class="successText">绑定成功</div>
			<div class="successDesc">已绑定成功，快返回到微信</br>开始您的虚席精彩之旅吧！</div>
	 </div>
</div>     
</div>

<div class="list-pic-5-bg"></div>
<script type="text/javascript">
$(function(){
	$(".list-pic-5").css("paddingTop",$(window).height());
	$(".list-pic-5-bg").css("height",$(window).height());
});
</script>
</div> <!-- list-pic-3 End -->
</div> <!-- xyd-wrapper End-->


<%-- <div>
	<div class="banner" style="height:40%">
		<img src="${wb_image}wechat/banner.jpg" width="100%" height="100%"></img>
	</div>
	<div class="bindForm">
		<div id="inputArea" class="textinput" >
			<form id="saveForm" method="post" action="${ctx }/app/wechat/bind" onsubmit="return submitForm();">
				<input type="hidden" name="userOpenId" value="${userOpenId }" id="userOpenId" />
				<input type="hidden" name="fwhCode" value="${fwhCode }" id="fwhCode" />
				<div>
					<input  id="account" name="account" value="${account}" type="text" class="nameinput input" placeholder="请输入用户名"/>
				</div>
				<div>
					<input  id="password" name="password" value="${password}"  type="password" class="pwdinput input" placeholder="请输入密码"/>
				</div>
				<div>
					<input type="submit" class="button" value="绑定微信"/>
				</div>
			</form>
			<div class="bindDesc">
				通过wizBank在线学习系统账号和微信绑定，成功后，就能在微信中进行丰富的内容、测试查询等其他信息
			</div>
		</div>
		<div id="success" class="success" style="display: none">
			<img src="${wb_image}wechat/success.png" width="30%"/>
			<div class="successText">绑定成功</div>
			<div class="successDesc">已绑定成功，快返回到微信</br>开始您的虚席精彩之旅吧！</div>
		</div>

		
	</div>
	<div>
		
	</div>
</div> --%>
	<script type="text/javascript" language="javascript" >
		$(function(){
			var status = '${status}';
			if(status == 'error') {
				alert('很抱歉,微信绑定e-Learning账号失败!');
			}
			rewriteInputFocus();
			
			initForm();
		});

		//初始化表单元素
		function initForm(){
			$("#account").focus();
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
		  	});
		  	accountObj.blur(function(){
		   		if(''==$(this).val()){
		   			$(this).val('请输入账号');
		   		}
		    	passwordObj.focus();
		  	}); 	
		}

		//提交表单
		function submitForm(){
			if(!checkForm()){
				return false;
			}
			var options = { 
				url : '${ctx }/app/wechat/bind',
				async: false,
				cache: false,
				dataType : 'json',
				type : 'POST', 
				success : function(data){
					if(data){
						if(data.status && data.status == 'LGS01') {
							//成功
							$("#inputArea").hide();
							$("#success").fadeIn(1000);
						} else {
							if(data.status=='LGF00'){
								alert("系统错误，未知错误！")
							} else if(data.status=='LGF01') {
								alert("用户不存在！")
							} else if(data.status=='LGF04') {
								alert("密码错误！")
							} else if(data.status=='LGF05') {
								alert("用户账号已被锁定，请与管理员联系！")
							} else if(data.status == "error"){
								alert.add("登录失败 : " + data.msg);
							} else {
								alert.add("登录失败：code : " + data.status);
							}
						}
					}
				},
				error : function(){
					alert('System error,保存失败!');
				} 
			};
			$('#saveForm').ajaxSubmit(options);
			return false;
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
	</script>
</body>

</html>


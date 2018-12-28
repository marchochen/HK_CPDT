<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<%@ include file="../common/meta.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_ss_${lang}.js"></script>
<title></title>
<script type="text/javascript">
	var min = ${minLength};
	var max = ${maxLength};
	
	$(function(){ 
		
		<c:if test="${not empty policyCompareCount and policyCompareCount != 0}">
			//新密码不能跟以往${policyCompareCount }次历史密码一样。<br/>
			$("#prompt").append(fetchLabel('label_core_system_setting_158').replace('{{N}}',"${policyCompareCount}") + "<br/>");
		</c:if>
		
		$('#prompt').append(fetchLabel('lab_passwd_length').replace('$minLength',min).replace('$maxLength',max));
		
		<c:if test="${not empty notChangePwdDays and notChangePwdDays != 0}">
		    //你已超过{{N}}天没有修改过你的密码了，为了账号安全，请及时更新你的密码！
			$("#notChangePwdDays").html(fetchLabel('label_core_system_setting_160').replace('{{N}}',"${notChangePwdDays}"));
		</c:if>
		
	});
	window.onload = function(){
		if("${response_status }" == "success"){
			Dialog.showSuccessMessage("${success_url}");
		}
	}
	function submitPassword(){

		var checkPassword = true;
		$("input[type='password']").each(
			function(){
				if($(this).val() == ''){
					Dialog.alert(fetchLabel('usr_' + $(this).attr("name")) + " " +fetchLabel('usr_is_not_null'));
					return checkPassword = false;
				}
			}	
		);
		if(!checkPassword){
			return;
		}
		
		var old_password = $("input[name='old_password']").val();
		var new_password = $("input[name='new_password']").val();
		
		if(new_password.search(/[^a-zA-Z0-9_-]/) != -1){
			Dialog.alert(fetchLabel('usr_password_type'));
			return
		}
		if(!/^(?=.*[A-Za-z]+)(?=.*[0-9]+)[A-za-z0-9_-]*$/.test(new_password)){
			Dialog.alert(fetchLabel('usr_password_check'));
			return 
		}
		if(new_password.length<${minLength} || new_password.length>${maxLength}){
			Dialog.alert(fetchLabel('usr_password_size').replace(/min/g,${minLength}).replace(/max/g,${maxLength}));
			return
		}
		
		if(new_password != $("input[name='confirm_password']").val()){
			Dialog.alert(fetchLabel('usr_check_password'));
			return;
		}
		
		$.ajax({
			url: "${ctx}/app/personal/updatePassword?time="+new Date(),
			data:{  
		    	old_password : old_password,
		    	new_password : new_password
		    },
		    dataType : 'json',
		    success:function(data) {
		    	
		    	var result = data.result;
				var message = data.message;//提示信息
		    	
		    	if('update_ok' == result){
		    		Dialog.alert(data.message,function(){
		    			window.location.href = data.success_url;
		    		});
		    	}else{
		    		Dialog.alert(data.message);
		    		
		    		switch(result){
					case 'usr_password_has_existed' ://新密码和之前历史记录一样
						$("input[name='new_password']").val('');
						$("input[name='confirm_password']").val('');
						break;
					case 'usr_old_password_error' ://密码错误
						$("input[name='old_password']").val('');
						break;
					case 'old_and_new_similar' ://旧密码和新密码一样
						$("input[name='new_password']").val('');
						$("input[name='confirm_password']").val('');
						break;
					}
		    	}
		    }
		});
	}
</script>
</head>
<body>


<c:if test="${fromlogin == true}">

<div class="xyd-header">
  <div class="xyd-header-box">
  	   <div class="xyd-logo">
			<c:if test="${!empty logo}">
				<c:if test="${lang == 'zh-cn'}">
					<img src="${ctx}/poster/cw/${logo.sp_logo_file_cn}"/>
				</c:if>
				<c:if test="${lang == 'zh-hk'}">
					<img src="${ctx}/poster/cw/${logo.sp_logo_file_hk}"/>
				</c:if>
				<c:if test="${lang == 'en-us'}">
					<img src="${ctx}/poster/cw/${logo.sp_logo_file_us}"/>
				</c:if>
			</c:if>
	  </div>
  </div>     
</div>

</c:if>

<div class="xyd-wrapper">
	<div class="clearfix xyd-main">
		
		<c:if test="${not fromlogin }">
			<jsp:include page="personalMenu.jsp"></jsp:include>
		</c:if>
		
		<div class="${fromlogin ? "wzb-model-10" : "xyd-article" }">
			<h3 class="pertit font-size18">
				<i class="fa f22 mr5 fa-lock font-size18"></i>
				<lb:get key="usr_change_psd" />
			</h3>
			
			<div id="notChangePwdDays" class="color-gray999">
			</div>

			<form action="" method="post" id="changePsdFrom" onsubmit="return false;">
				<table>
					<tr>
						<td class="wzb-form-label"><span><lb:get key="usr_name"/> ：</span></td>
						<td class="wzb-form-control">${prof.usr_ste_usr_id}</td>
					</tr>
					<tr>
						<td class="wzb-form-label"><span class="wzb-form-star">*</span><span><lb:get key="usr_old_password"/> ：</span></td>
						<td class="wzb-form-control"><input type="password"  autocomplete="off"  class="form-control" style="width:400px;" name="old_password" value=""/></td>
					</tr>
					<tr>
						<td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><span><lb:get key="usr_new_password"/> ：</span></td>
						<td class="wzb-form-control">
						<input type="password" class="form-control" style="width:400px;"  autocomplete="off"  name="new_password" value=""/>
						<div id="prompt" style="color:#999;">
							<lb:get key="label_core_system_setting_157"/><br/>
						</div>
						</td>
					</tr>
					<tr>
						<td class="wzb-form-label"><span class="wzb-form-star">*</span><span> <lb:get key="usr_confirm_password"/> ：</span></td>
						<td class="wzb-form-control"><input type="password"  autocomplete="off"  class="form-control" style="width:400px;" name="confirm_password" value=""/></td>
					</tr>
					<tr>
						<td class="wzb-form-label"></td>
						<td class="wzb-form-control"><span class="wzb-form-star">*</span> <lb:get key="usr_required"/></td>
					</tr>
				</table>
			    
			    <div class="wzb-bar">
			    	<input type="button" class="btn wzb-btn-orange wzb-btn-big margin-right15" onclick="submitPassword()"value="<lb:get key="button_ok"/>"/>
			    	<c:if test="${not forceChange }">
			    		<input type="button" onclick="javascript:wb_utils_cancelChangePwd();" class="btn wzb-btn-orange wzb-btn-big" value="<lb:get key="label_core_system_setting_159"/>" />
			    	</c:if>
			    </div>
			</form>
		</div>
	</div>
	
</div> <!-- wrap End-->


<div id="top"></div>
</body>
</html>
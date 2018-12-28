<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/taglibs.jsp"%>
<%@ include file="../common/template.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctx}/static/js/jquery-table/css/jquery-table-learner.css" />
<script type="text/javascript" src="${ctx}/static/js/jquery-table/jquery.table.js"></script>
<script type="text/javascript" src="${ctx}/static/js/front/course.js"></script>
<script type="text/javascript" src="${ctx}/static/js/i18n/${lang}/label_cm_${lang}.js"></script>
<title></title>
<script type="text/javascript">
	var min = ${minLength};
	var max = ${maxLength};
	
	$(function(){ 
		$('#prompt').html(fetchLabel('lab_passwd_length').replace('$minLength',min).replace('$maxLength',max));
	}); 
	function submitPassword(){
		
		var checkPassword = true;
		$("input[type='password']").each(
			function(){
				//console.log($(this))
				if($(this).val() == ''){
					Dialog.alert(fetchLabel('usr_' + $(this).attr("name")) +" "+ fetchLabel('usr_is_not_null'));
					return checkPassword = false;
				}
				if($(this).val().length < min || $(this).val().length > max){

					Dialog.alert(fetchLabel('usr_password_size').replace('min',min).replace('max',max));
					return checkPassword = false;
				}
			}	
		);
		if(!checkPassword){
			return;
		}
		
		if($("input[name='new_password']").val().length<${minLength} || $("input[name='new_password']").val().length>${maxLength}){
			Dialog.alert(fetchLabel('usr_password_size').replace(/min/g,${minLength}).replace(/max/g,${maxLength}));
			return
		}
	
		if($("input[name='new_password']").val().search(/[^a-zA-Z0-9_-]/) != -1){
			Dialog.alert(fetchLabel('usr_password_type'));
			return
		}
		if(!/^(?=.*[A-Za-z]+)(?=.*[0-9]+)[A-za-z0-9_-]*$/.test($("input[name='new_password']").val())){
			Dialog.alert(fetchLabel('usr_password_check'));
			return 
		}
		if($("input[name='new_password']").val() != $("input[name='confirm_password']").val()){
			Dialog.alert(fetchLabel('usr_check_password'));
			return;
		}
		if($("input[name='new_password']").val() == $("input[name='old_password']").val())
		{
			Dialog.alert(fetchLabel('label_core_community_management_213'));
			return;
		}
		//$("#changePsdFrom").submit();
		param = {
			old_password : $("input[name='old_password']").val(),
			new_password : $("input[name='new_password']").val(),
			confirm_password : $("input[name='confirm_password']").val()
		}
		
		$.ajax({
			url : '/app/personal/updatePassword',
			type : 'POST',
			dataType : 'json',
			data : param,
			async : false,
			success : function(data){
				if(data.message == "update_ok"){
					Dialog.showSuccessMessage(location.href);
				}else if(data.message == "usr_old_password_error"){
					Dialog.alert(fetchLabel("login_update_pwd_error"));
				}else{
					Dialog.alert(fetchLabel("login_update_pwd_error"));
				}
			}

		});
	}
	
</script>
</head>
<body >
<div class="xyd-wrapper">
	<div id="main" class="xyd-main clearfix">
		<jsp:include page="personalMenu.jsp"></jsp:include>
		
		<div class="xyd-article">
			<div class="wzb-title-2"><i class="fa font-size18 fa-lock"></i> <lb:get key="usr_change_psd"/></div>

			<form action="${ctx}/app/personal/updatePassword" method="post" id="changePsdFrom">
			<table class="margin-top15">
				<tr>
					 <td class="wzb-form-label"><lb:get key="usr_name"/> :</td>
					 
					 <td class="wzb-form-control">
						 ${regUser.usr_display_bil}  
					 </td>
				</tr>
				
				<tr>
					 <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="usr_old_password"/> :</td>
					 
					 <td class="wzb-form-control">
						 <input type="password" class="form-control wzb-text-05" name="old_password" value="">
					 </td>
				</tr>
				
				<tr>
					 <td valign="top" class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="usr_new_password"/> :</td>
					 
					 <td class="wzb-form-control">
						 <input type="password" class="form-control wzb-text-05" name="new_password" value="">
						 <div id="prompt"> </div>
					 </td>
				</tr>
				
				<tr>
					 <td class="wzb-form-label"><span class="wzb-form-star">*</span><lb:get key="usr_confirm_password"/> :</td>
					 
					 <td class="wzb-form-control">
						 <input type="password" class="form-control wzb-text-05" name="confirm_password" value="">
					 </td>
				</tr>
				
				<tr>
					 <td class="wzb-form-label"></td>
					 
					 <td class="wzb-form-control"><span class="wzb-form-star">*</span><lb:get key="usr_required"/></div>
					 </td>
				</tr>
			</table>

			<div class="wzb-bar">
				 <input type="button" class="btn wzb-btn-orange wzb-btn-big margin-right15" value='<lb:get key="button_ok"/>' onclick="submitPassword()" name="frmSubmitBtn">
				 <%-- <input type="button" onclick="javascript:void(0);" class="btn wzb-btn-blue wzb-btn-big" value='<lb:get key="button_cancel"/>' name="frmSubmitBtn"> --%>
			</div>
			</form>

		</div> <!-- xyd-article End-->
	</div>
</div> <!-- xyd-wrapper End-->
</body>
</html>
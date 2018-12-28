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
<title></title>
<script type="text/javascript">
	var min = ${minLength};
	var max = ${maxLength};
	
	$(function(){ 
		$('#prompt').html(fetchLabel('lab_passwd_length').replace('$minLength',min).replace('$maxLength',max));
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
		
		if($("input[name='new_password']").val().search(/[^a-zA-Z0-9_-]/) != -1){
			Dialog.alert(fetchLabel('usr_password_type'));
			return
		}
		if(!/^(?=.*[A-Za-z]+)(?=.*[0-9]+)[A-za-z0-9_-]*$/.test($("input[name='new_password']").val())){
			Dialog.alert(fetchLabel('usr_password_check'));
			return 
		}
		if($("input[name='new_password']").val().length<${minLength} || $("input[name='new_password']").val().length>${maxLength}){
			Dialog.alert(fetchLabel('usr_password_size').replace(/min/g,${minLength}).replace(/max/g,${maxLength}));
			return
		}
		
		if($("input[name='new_password']").val() != $("input[name='confirm_password']").val()){
			Dialog.alert(fetchLabel('usr_check_password'));
			return;
		}
		
		if($("input[name='new_password']").val() == $("input[name='old_password']").val()){
			Dialog.alert(fetchLabel('label_core_community_management_213'));
			return;
		}
		
		 $.ajax({  
			    url:"${ctx}/app/personal/personalMustChangePsd/checkOldPassword",
			    data:{  
			    	old_password : $("input[name='old_password']").val()
			    },  
			    async: false,
			    type:'post',  
			    cache:false,  
			    dataType:'json',  
			    success:function(data) {  
			        if(data.success==false){
			        	Dialog.alert(fetchLabel('usr_new_password_error'));
			        	return;
			        }
			        $("#changePsdFrom").submit();
			     },
			     error:function(){   
			    	 $("#changePsdFrom").submit();
			     }
		});
		
	}
</script>
</head>
<body>
<div class="panel wzb-panel wzb-repassword">
	<div class="clearfix margin-top50">
		<div class="wzb-repassword-center">
			<div class="wzb-repassword-padding">
					<h3 class="pertit fontfamily">
						<i class="fa f22 mr5 fa-lock"></i>&nbsp;
						<lb:get key="usr_change_psd" />
					</h3>

					<form action="${ctx}/app/personal/personalMustChangePsd/update/changePassword" method="post" id="changePsdFrom">
						<table>
							<tr>
								<td class="wzb-form-label" style="width:40%"><span><lb:get key="usr_name"/> ：</span></td>
								<td class="wzb-form-control">${regUser.usr_display_bil}</td>
							</tr>
							<tr>
								<td class="wzb-form-label"><span class="wzb-form-star">*</span><span><lb:get key="usr_old_password"/> ：</span></td>
								<td class="wzb-form-control"><input type="password" class="form-control" style="width: 200px;" name="old_password" value=""/></td>
							</tr>
							<tr>
								<td class="wzb-form-label" valign="top"><span class="wzb-form-star">*</span><span><lb:get key="usr_new_password"/> ：</span></td>
								<td class="wzb-form-control">
								<input type="password" class="form-control" style="width: 200px;" name="new_password" value=""/>
								<div id="prompt"> </div>
								</td>
							</tr>
							<tr>
								<td class="wzb-form-label"><span class="wzb-form-star">*</span><span> <lb:get key="usr_confirm_password"/> ：</span></td>
								<td class="wzb-form-control"><input type="password" class="form-control" style="width: 200px;" name="confirm_password" value=""/></td>
							</tr>
							<tr>
								<td class="wzb-form-label"></td>
								<td class="wzb-form-control"><span class="wzb-form-star">*</span> <lb:get key="usr_required"/></td>
							</tr>
						</table>
					    
					    <div class="wzb-bar">
					    	<input type="button" class="btn wzb-btn-blue margin-right10 wzb-btn-big" name="pertxt" value='<lb:get key="button_ok"/>' onclick="submitPassword()"/>
	<%-- 				    	<input type="text" class="wbtj fontfamily swop_bg" name="pertxt" value='<lb:get key="button_cancel"/>'/> --%>
					    </div>
				</form>
			</div> <!-- cont End-->
		</div>
	
	</div>
</div> <!-- wrap End-->
<!-- <div id="menu"></div> -->
<div id="top"></div>
</body>
</html>